package com.imt.raytracing.raytracer.light;

import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;

public class DirectionalLight extends Light {
    private Vector direction;

    public DirectionalLight(Vector direction, Color color) {
        super(color);
        this.direction = direction;
    }
    
}
