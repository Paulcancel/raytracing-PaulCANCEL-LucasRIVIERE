package com.imt.raytracing.geometry.shape;

import java.util.Optional;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.scene.Intersection;

/**
 * Infinite plane defined by a point and a normal vector.
 *
 * The plane provides material properties via the Shape base class.
 */
public class Plane extends Shape {

    /**
     * A point lying on the plane.
     */
    public Point p0;       

    /**
     * Unit normal vector of the plane (points away from the surface).
     */
    public Vector normal;   

    public Color diffuse;
    public Color specular;
    public double shininess;

    /**
     * Create a plane with a reference point, normal and material properties.
     *
     * The normal is normalized during construction.
     *
     * @param p0 a point on the plane
     * @param normal plane normal (will be normalized)
     * @param diffuse diffuse color
     * @param specular specular color
     * @param shininess Phong shininess coefficient
     */
    public Plane(Point p0, Vector normal,
                 Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.p0 = p0;
        this.normal = normal.normalize();

    }

    /**
     * Intersect the ray with the plane.
     *
     * Uses the analytic solution t = (p0 - origin) · n / (d · n).
     * Returns Optional.empty() when the ray is parallel to the plane or
     * when the intersection is behind the ray origin (t <= 0).
     *
     * @param ray the ray to test
     * @return Optional containing an IntersectionPlane if hit, otherwise Optional.empty()
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {

        double denom = normal.dot(ray.direction);

        // Si denom ≈ 0 : rayon parallèle → pas d'intersection
        if (Math.abs(denom) < 1e-8) return Optional.empty();

        double t = p0.sub(ray.origin).dot(normal) / denom;

        if (t <= 0) return Optional.empty();

        return Optional.of(new IntersectionPlane(t, this, ray));
    }

    /** 
     * Intersection subclass that supplies the constant plane normal.
     *
     * Instances of this class represent a ray-plane intersection and
     * provide the precomputed normal for shading.
     */
    public static class IntersectionPlane extends Intersection {

        public IntersectionPlane(double t, Plane plane, Ray ray) {
            super(t, plane, ray);
            this.normal = plane.normal;
        }
    }
}
