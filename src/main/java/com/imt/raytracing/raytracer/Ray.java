package com.imt.raytracing.raytracer;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;

public class Ray {
    public Point origin;
    public Vector direction;

    public Ray(Point origin, Vector direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }
}
