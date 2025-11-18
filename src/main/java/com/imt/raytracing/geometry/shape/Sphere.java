package com.imt.raytracing.geometry.shape;

import java.util.Optional;

import com.imt.raytracing.geometry.Intersection;
import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;

public class Sphere {
    public Point center;
    public double radius;
    public Color color;

    public Sphere(Point center, double radius, Color color) {
        this.center = center;
        this.radius = radius;
        this.color = color;
    }

    public Optional<Intersection> intersect(Ray ray) {
        Vector oc = ray.origin.sub(center);

        double a = ray.direction.dot(ray.direction);
        double b = 2.0 * oc.dot(ray.direction);
        double c = oc.dot(oc) - radius * radius;

        double delta = b * b - 4 * a * c;
        if (delta < 0) return Optional.empty();

        double sqrtDelta = Math.sqrt(delta);
        double t1 = (-b - sqrtDelta) / (2 * a);
        double t2 = (-b + sqrtDelta) / (2 * a);

        double t = -1;

        if (t1 > 0 && (t1 < t2 || t2 < 0))
            t = t1;
        else if (t2 > 0)
            t = t2;

        if (t <= 0) return Optional.empty();

        return Optional.of(new Intersection(t, this));
    }
}
