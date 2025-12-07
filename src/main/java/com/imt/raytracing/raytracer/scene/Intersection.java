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

        // Calculate the impact point P = Ray_Origin + t * Ray_Direction
        this.point = incomingRay.origin.add(incomingRay.direction.mul(t));

        // Calculate the normal vector based on the type of shape hit
        if (shape instanceof Sphere s) {
            // Sphere normal points from the center to the hit point
            this.normal = point.sub(s.center).normalize();
        }
        else if (shape instanceof Triangle tri) {
            // Triangle normal is the pre-calculated, constant surface normal
            this.normal = tri.normal; 
        }
        else if (shape instanceof Plane pl) {
            // Plane normal is the pre-calculated, constant surface normal
            this.normal = pl.normal;  
        }
        else {
            throw new RuntimeException("Unknown Shape encountered during Intersection calculation.");
        }
    }

    /** * Checks if the intersection point is in shadow with respect to a specific light source. 
     * This is done by casting a shadow ray from the hit point towards the light.
     * @param scene The entire scene containing all objects.
     * @param light The light source to check against.
     * @return true if an object blocks the light (shadow), false otherwise.
     */
    public boolean isShadowed(Scene scene, Light light) {

        Vector L; // Vector pointing *to* the light source
        double maxDist = Double.POSITIVE_INFINITY;

        if (light instanceof DirectionalLight dl) {
            // Light direction is constant and goes towards the light source (inverse of light's direction vector)
            L = dl.direction.mul(-1).normalize();
        } else {
            // Point light: direction is from hit point to light origin
            PointLight pl = (PointLight) light;
            L = pl.origin.sub(point).normalize();
            // Max distance is the actual distance to the point light source
            maxDist = pl.origin.sub(point).length();
        }

        // Apply an epsilon offset to the starting point to prevent the shadow ray from 
        // immediately intersecting its own shape (self-shadowing artifact).
        Ray shadowRay = new Ray(
                point.add(normal.mul(1e-4)),
                L
        );

        // Check for the closest intersection along the shadow ray
        var hit = scene.closestIntersection(shadowRay);

        if (hit.isEmpty())
            return false; // No object hit between the point and the light (or infinity for directional light)

        // The point is shadowed if an object is hit and that object is closer than the point light source (maxDist)
        // For directional lights, maxDist is infinity, so any hit blocks the light.
        return hit.get().t < maxDist;
    }

    /** * Calculates the **Diffuse** illumination component using the Lambertian model.
     * The brightness depends on the angle between the normal and the light direction.
     * @param light The light source to calculate diffuse intensity for.
     * @return The calculated diffuse Color component.
     */
    protected Color diffuse(Light light) {

        // Get the normalized light direction vector L (from point to light source)
        Vector L = (light instanceof DirectionalLight dl)
                ? dl.direction.mul(-1).normalize()
                : ((PointLight) light).origin.sub(point).normalize();

        // Calculate Lambert's cosine law: max(0, N dot L)
        double dot = Math.max(0, normal.dot(L));

        // Multiply dot product by light color and shape's diffuse color component-wise
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

        // Get the normalized light direction vector L (from point to light source)
        Vector L = (light instanceof DirectionalLight dl)
                ? dl.direction.mul(-1).normalize()
                : ((PointLight) light).origin.sub(point).normalize();

        // View vector V: points from the hit point back to the camera (inverse of the incoming ray direction)
        Vector V = incomingRay.direction.mul(-1).normalize();
        
        // Half-vector H: the halfway vector between L and V
        Vector H = L.add(V).normalize();

        // Calculate Blinn-Phong power: max(0, N dot H) ^ shininess
        double dot = Math.max(0, normal.dot(H));
        double p = Math.pow(dot, shape.shininess);

        // Multiply power by light color and shape's specular color component-wise
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
    public Color shade(Scene scene) {

        Color col = new Color(0,0,0);

        // Iterate over every light source in the scene
        for (Light light : scene.lights) {

            // Skip the light if the point is in shadow relative to it
            if (isShadowed(scene, light))
                continue;

            // Calculate and accumulate Diffuse component
            // Optimization: Skip diffuse calculation if the shape's diffuse color is black
            if (shape.diffuse.x > 0 || shape.diffuse.y > 0 || shape.diffuse.z > 0) {
                Color d = diffuse(light);
                col.x += d.x;
                col.y += d.y;
                col.z += d.z;
            }

            // Calculate and accumulate Specular component
            // (Note: Specular highlights usually apply regardless of diffuse color)
            Color sp = specularPhong(light);
            col.x += sp.x;
            col.y += sp.y;
            col.z += sp.z;
        }

        return col;
    }
}
