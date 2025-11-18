package com.imt.raytracing.geometry;

import com.imt.raytracing.geometry.shape.Sphere;

public class Intersection {
    public double t;
    public Sphere sphere;

    public Intersection(double t, Sphere sphere) {
        this.t = t;
        this.sphere = sphere;
    }
}
