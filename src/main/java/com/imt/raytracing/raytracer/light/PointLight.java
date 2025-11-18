package com.imt.raytracing.raytracer.light;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;

public class PointLight extends Light {
    private Point origin;

    public PointLight(Point origin, Color color) {
        super(color);
        this.origin = origin;
    }
    
}
