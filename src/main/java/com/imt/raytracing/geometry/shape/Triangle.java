package com.imt.raytracing.geometry.shape;

import java.util.Optional;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.scene.Intersection;

/**
 * Triangle defined by three points (a, b, c).
 *
 * The triangle precomputes a constant face normal used for shading.
 * Intersection tests use the Möller–Trumbore algorithm.
 */
public class Triangle extends Shape {

    /**
     * Triangle vertices in counter-clockwise order.
     */
    public Point a, b, c;

    /**
     * Precomputed unit normal of the triangle face.
     */
    public Vector normal;


    /**
     * Construct a triangle with vertices and material properties.
     *
     * The face normal is computed as normalize((b - a) × (c - a)).
     *
     * @param a vertex A
     * @param b vertex B
     * @param c vertex C
     * @param diffuse diffuse color
     * @param specular specular color
     * @param shininess Phong shininess coefficient
     */
    public Triangle(Point a, Point b, Point c,
                    Color diffuse, Color specular, double shininess) {
        super(diffuse, specular, shininess);
        this.a = a;
        this.b = b;
        this.c = c;


        this.normal = b.sub(a).cross(c.sub(a)).normalize();
    }

    /**
     * Intersect the ray with the triangle using Möller–Trumbore algorithm.
     *
     * Returns an IntersectionTriangle containing the constant face normal on hit,
     * or Optional.empty() when there is no valid intersection.
     *
     * @param ray the ray to test
     * @return Optional containing IntersectionTriangle if hit, otherwise Optional.empty()
     */
    @Override
    public Optional<Intersection> intersect(Ray ray) {

        Vector ab = b.sub(a);
        Vector ac = c.sub(a);

        Vector pvec = ray.direction.cross(ac);
        double det = ab.dot(pvec);

        if (Math.abs(det) < 1e-8) return Optional.empty();

        double invDet = 1.0 / det;

        Vector tvec = ray.origin.sub(a);
        double u = tvec.dot(pvec) * invDet;
        if (u < 0 || u > 1) return Optional.empty();

        Vector qvec = tvec.cross(ab);
        double v = ray.direction.dot(qvec) * invDet;
        if (v < 0 || u + v > 1) return Optional.empty();

        double t = ac.dot(qvec) * invDet;
        if (t <= 0) return Optional.empty();

        return Optional.of(new IntersectionTriangle(t, this, ray));
    }

    public static class IntersectionTriangle extends Intersection {

        public IntersectionTriangle(double t, Triangle tri, Ray ray) {
            super(t, tri, ray);
            this.normal = tri.normal; 
        }
    }
}
