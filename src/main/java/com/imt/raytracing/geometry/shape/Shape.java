package com.imt.raytracing.geometry.shape;

import java.util.Optional;

import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.scene.Intersection;

import lombok.AllArgsConstructor;

/**
 * Base class for geometric shapes in the raytracer.
 *
 * Provides common material properties (diffuse, specular, shininess)
 * and a default intersect method that can be overridden by concrete shapes.
 */
@AllArgsConstructor
public class Shape {
    /**
     * Diffuse color of the surface.
     */
    public Color diffuse;

    /**
     * Specular color of the surface.
     */
    public Color specular;

    /**
     * Phong shininess coefficient used for specular highlights.
     */
    public double shininess;
    
    /**
     * Compute intersection of the given ray with this shape.
     *
     * Subclasses should override this method and return an Optional containing
     * an Intersection if the ray hits the shape, or Optional.empty() otherwise.
     *
     * @param ray the ray to test
     * @return Optional containing Intersection if hit, otherwise Optional.empty()
     */
    public Optional<Intersection> intersect(Ray ray) {
        return Optional.empty();
    }
}
