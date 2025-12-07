package com.imt.raytracing.geometry.shape;

import java.util.Optional;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.scene.Intersection;

/**
 * Sphere defined by a center point and a radius.
 *
 * Intersection is computed by solving the quadratic equation for ray-sphere intersection.
 */
public class Sphere extends Shape {
    /**
     * Center of the sphere.
     */
    public Point center;

    /**
     * Radius of the sphere.
     */
    public double radius;

    /**
     * Create a sphere with given center, radius and material properties.
     *
     * @param center sphere center
     * @param radius sphere radius
     * @param diffuse diffuse color
     * @param specular specular color
     * @param shininess Phong shininess coefficient
     */
    public Sphere(Point center, double radius,
                  Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.center = center;
        this.radius = radius;

    }

    /**
     * Intersect the ray with the sphere.
     *
     * Solves a*t^2 + b*t + c = 0 where the coefficients are derived from the ray
     * origin/direction and the sphere center/radius. Returns the nearest positive
     * t if the ray hits the sphere, otherwise Optional.empty().
     *
     * @param ray the ray to test
     * @return Optional containing Intersection if hit, otherwise Optional.empty()
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {

        Vector oc = ray.origin.sub(center);

        double a = ray.direction.dot(ray.direction);
        double b = 2.0 * oc.dot(ray.direction);
        double c = oc.dot(oc) - radius * radius;

        double delta = b*b - 4*a*c;
        if (delta < 0) return Optional.empty();

        double sqrt = Math.sqrt(delta);
        double t1 = (-b - sqrt) / (2*a);
        double t2 = (-b + sqrt) / (2*a);

        double t = Double.POSITIVE_INFINITY;

        if (t1 > 0) t = t1;
        if (t2 > 0 && t2 < t) t = t2;

        if (t == Double.POSITIVE_INFINITY)
            return Optional.empty();

        return Optional.of(new Intersection(t, this, ray));
    }
}
