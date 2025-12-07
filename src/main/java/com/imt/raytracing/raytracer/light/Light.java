package com.imt.raytracing.raytracer.light;

import com.imt.raytracing.imaging.Color;

import lombok.AllArgsConstructor;

/**
 * The base class for all light sources in the ray tracing scene.
 * It primarily holds the **intensity and color** of the light emission.
 */
@AllArgsConstructor
public class Light {
    // The color and intensity of the light source.
    public Color color;
}