package com.imt.raytracing.raytracer;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;

/**
 * Represents a Ray in 3D space, which is fundamental to ray tracing.
 * A Ray is defined by a starting point (origin) and a direction vector, and is 
 * used to test for intersections with objects in the scene.
 * The ray equation is P(t) = origin + t * direction, where t >= 0.
 */
public class Ray {
    /** The starting position of the ray. */
    public Point origin;
    /** The normalized direction vector of the ray. */
    public Vector direction;

    /**
     * Constructs a Ray.
     * The input direction vector is automatically normalized (scaled to unit length).
     * @param origin The starting Point of the ray.
     * @param direction The direction Vector of the ray.
     */
    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }
}