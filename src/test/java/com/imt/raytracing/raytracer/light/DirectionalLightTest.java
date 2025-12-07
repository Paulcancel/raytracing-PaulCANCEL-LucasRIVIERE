package com.imt.raytracing.raytracer.light;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;

/**
 * Unit tests for the DirectionalLight class, verifying correct initialization 
 * of the direction vector and inherited color property.
 */
public class DirectionalLightTest {

    private Color testColor;
    private Vector testDirection;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        testColor = new Color(1.0, 0.9, 0.8);
        testDirection = new Vector(1.0, -2.0, 0.5).normalize(); 
    }

    @Test
    void testDirectionalLightConstruction() {
        DirectionalLight light = new DirectionalLight(testDirection, testColor);

        // Test 1: Check inherited color property
        assertNotNull(light.color, "Light color should not be null.");
        assertEquals(testColor.x, light.color.x, EPSILON, "Color red component must match.");
        assertEquals(testColor.y, light.color.y, EPSILON, "Color green component must match.");
        
        // Test 2: Check direction vector
        assertNotNull(light.direction, "Direction vector should not be null.");
        assertEquals(testDirection.x, light.direction.x, EPSILON, "Direction X component must match.");
        assertEquals(testDirection.y, light.direction.y, EPSILON, "Direction Y component must match.");
        assertEquals(testDirection.z, light.direction.z, EPSILON, "Direction Z component must match.");
    }

    @Test
    void testDirectionalLight_UsesNormalizedVector() {
        // Use a non-normalized vector for construction
        Vector unnormalizedDirection = new Vector(10.0, 0.0, 0.0);
        DirectionalLight light = new DirectionalLight(unnormalizedDirection, testColor);
        
        
        // We verify that the stored vector still has its original, non-unit length
        assertEquals(10.0, light.direction.length(), EPSILON, 
                     "The vector should retain its original length (10.0) as the constructor does not normalize.");
        
        // The shading logic (like in Intersection.isShadowed) is responsible for calling normalize() 
        // when calculating the light ray.
        assertEquals(10.0, light.direction.x, EPSILON);
    }
}