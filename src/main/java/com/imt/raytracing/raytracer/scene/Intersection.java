package com.imt.raytracing.raytracer.scene;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.geometry.shape.Shape;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.geometry.shape.Triangle;
import com.imt.raytracing.geometry.shape.Plane;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.light.DirectionalLight;
import com.imt.raytracing.raytracer.light.Light;
import com.imt.raytracing.raytracer.light.PointLight;

/**
 * Represents the result of a successful ray-object collision.
 * It stores all the necessary geometric information (point, normal, distance) 
 * required for shading and illumination calculations.
 */
public class Intersection {

    /** The distance along the ray to the intersection point. */
    public double t;
    /** The Shape object that was hit. */
    public Shape shape;
    /** The actual 3D coordinates of the intersection point. */
    public Point point;
    /** The surface normal vector at the intersection point. */
    public Vector normal;
    /** The ray that caused this intersection. */
    public Ray incomingRay;

    /**
     * Constructs an Intersection object, calculating the hit point and the normal vector.
     * @param t The distance along the ray where the hit occurred.
     * @param shape The shape that was hit.
     * @param incomingRay The ray that intersected the shape.
     */
    public Intersection(double t, Shape shape, Ray incomingRay) {
        this.t = t;
        this.shape = shape;
        this.incomingRay = incomingRay;

        this.point = incomingRay.origin.add(incomingRay.direction.mul(t));

        if (shape instanceof Sphere s) {
            this.normal = point.sub(s.center).normalize();
        }
        else if (shape instanceof Triangle tri) {
            this.normal = tri.normal; 
        }
        else if (shape instanceof Plane pl) {
            this.normal = pl.normal;  
        }
        else {
            throw new RuntimeException("Unknown Shape encountered during Intersection calculation.");
        }

        if (this.normal.dot(incomingRay.direction) > 0) {
            this.normal = this.normal.mul(-1);
        }
    }

    /** * Checks if the intersection point is in shadow with respect to a specific light source. 
     * This is done by casting a shadow ray from the hit point towards the light.
     * @param scene The entire scene containing all objects.
     * @param light The light source to check against.
     * @return true if an object blocks the light (shadow), false otherwise.
     */
    public boolean isShadowed(Scene scene, Light light) {

        Vector L; 
        double maxDist = Double.POSITIVE_INFINITY;

    if (light instanceof DirectionalLight dl) {
            L = dl.direction.normalize(); 
        } else {
            PointLight pl = (PointLight) light;
            L = pl.origin.sub(point).normalize();
            maxDist = pl.origin.sub(point).length();
        }

        // Apply an epsilon offset to the starting point to prevent the shadow ray from 
        // immediately intersecting its own shape (self-shadowing artifact).
        Ray shadowRay = new Ray(
                point.add(normal.mul(1e-4)),
                L
        );

        var hit = scene.closestIntersection(shadowRay);

        if (hit.isEmpty())
            return false; // No object hit between the point and the light (or infinity for directional light)

        return hit.get().t < maxDist;
    }

    /** * Calculates the **Diffuse** illumination component using the Lambertian model.
     * The brightness depends on the angle between the normal and the light direction.
     * @param light The light source to calculate diffuse intensity for.
     * @return The calculated diffuse Color component.
     */
    protected Color diffuse(Light light) {
        Vector L = (light instanceof DirectionalLight dl)
                    ? dl.direction.normalize() 
                    : ((PointLight) light).origin.sub(point).normalize();

        double dot = Math.max(0, normal.dot(L));

        return new Color(
                dot * light.color.x * shape.diffuse.x,
                dot * light.color.y * shape.diffuse.y,
                dot * light.color.z * shape.diffuse.z
        );
    }

    /** * Calculates the **Specular** highlight component using the Blinn-Phong model.
     * The highlight depends on the angle between the normal and the half-vector (H).
     * @param light The light source to calculate specular intensity for.
     * @return The calculated specular Color component.
     */
    protected Color specularPhong(Light light) {

        Vector L = (light instanceof DirectionalLight dl)
                    ? dl.direction.normalize() 
                    : ((PointLight) light).origin.sub(point).normalize();

        // View vector V: points from the hit point back to the camera 
        Vector V = incomingRay.direction.mul(-1).normalize();
        
        // Half-vector H: the halfway vector between L and V
        Vector H = L.add(V).normalize();

        // Calculate Blinn-Phong power: max(0, N dot H) ^ shininess
        double dot = Math.max(0, normal.dot(H));
        double p = Math.pow(dot, shape.shininess);

        return new Color(
                p * light.color.x * shape.specular.x,
                p * light.color.y * shape.specular.y,
                p * light.color.z * shape.specular.z
        );
    }

    /** * Calculates the final shaded Color at the intersection point by summing the 
     * contributions of all lights (Diffuse + Specular) and checking for shadows.
     * @param scene The scene to access all light sources.
     * @return The final illuminated Color (excluding Ambient).
     */
    public Color shade(Scene scene, int depth) {

        Color col = new Color(0, 0, 0);

        // ---------- AMBIENT ----------
        col.x += scene.ambient.x * shape.diffuse.x;
        col.y += scene.ambient.y * shape.diffuse.y;
        col.z += scene.ambient.z * shape.diffuse.z;

        // ---------- DIRECT LIGHTING ----------
        for (Light light : scene.lights) {

            if (isShadowed(scene, light))
                continue;

            // diffuse
            if (!shape.diffuse.isBlack()) {
                Color d = diffuse(light);
                col.addLocal(d);
            }

            // specular
            Color s = specularPhong(light);
            col.addLocal(s);
        }

        // ---------- REFLECTION ----------
        if (depth < scene.maxdepth && !shape.specular.isBlack() && scene.maxdepth > 1) {

            Vector r = computeReflectionDirection();

            Ray reflected = new Ray(
                    point.add(normal.mul(1e-4)),
                    r
            );

            var hit = scene.closestIntersection(reflected);

            if (hit.isPresent()) {
                Color reflectedColor = hit.get().shade(scene, depth + 1);

                col.x += reflectedColor.x * shape.specular.x;
                col.y += reflectedColor.y * shape.specular.y;
                col.z += reflectedColor.z * shape.specular.z;
            }
        }

        return col;
    }


    private Vector computeReflectionDirection() {
        Vector d = incomingRay.direction.normalize();
        Vector n = normal.normalize();
        // r = d + 2 * (n ⋅ (-d)) * n
        double k = 2 * n.dot(d.mul(-1));
        return d.add(n.mul(k)).normalize();
    }
}
