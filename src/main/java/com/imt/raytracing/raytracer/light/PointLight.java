package com.imt.raytracing.raytracer.light;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.imaging.Color;

/**
 * Represents a **Point Light** source.
 * This light emits equally in all directions from a single, fixed position (the origin), 
 * similar to a light bulb.
 * The intensity of the light typically diminishes with distance from the origin.
 */
public class PointLight extends Light {
    /** The fixed position in 3D space from which the light originates. */
    public Point origin;

    /**
     * Constructs a Point Light source.
     * @param origin The Point representing the position of the light source.
     * @param color The Color (intensity) of the light.
     */
    public PointLight(Point origin, Color color) {
        super(color);
        this.origin = origin;
    }
    
}