package com.imt.raytracing.raytracer.light;

import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;

/**
 * Represents a **Directional Light** source.
 * This light source is infinitely far away, meaning all light rays travel in the 
 * exact same direction across the entire scene, like the sun.
 */
public class DirectionalLight extends Light {
    /** The direction vector indicating where the light is coming from or pointing to. 
     * In ray tracing, this vector is usually normalized (unit length).
     */
    public Vector direction;

    /**
     * Constructs a Directional Light source.
     * @param direction The direction of the light.
     * @param color The Color (intensity) of the light.
     */
    public DirectionalLight(Vector direction, Color color) {
        super(color);
        this.direction = direction;
    }
    
}