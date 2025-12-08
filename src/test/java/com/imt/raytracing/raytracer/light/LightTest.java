package com.imt.raytracing.raytracer.light;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.imaging.Color;

/**
 * Unit tests for the Light base class, verifying correct initialization 
 * of the color/intensity property.
 */
public class LightTest {

    private Color testColor;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        // Define a test color (e.g., bright white light)
        testColor = new Color(1.0, 1.0, 1.0);
    }

    @Test
    void testLightConstruction_ColorInitialization() {
        // Instantiate the Light object using the provided constructor
        Light light = new Light(testColor);

        // Test 1: Check if the color property is correctly set
        assertNotNull(light.color, "Light color should not be null.");
        
        // Test 2: Verify the color components match the input
        assertEquals(1.0, light.color.x, EPSILON, "Color red component must match.");
        assertEquals(1.0, light.color.y, EPSILON, "Color green component must match.");
        assertEquals(1.0, light.color.z, EPSILON, "Color blue component must match.");
    }
    
    @Test
    void testLightConstruction_ZeroColor() {
        // Instantiate the Light object with black color (0, 0, 0)
        Color blackColor = new Color(0, 0, 0);
        Light light = new Light(blackColor);
        
        // Verify the light is black
        assertEquals(0.0, light.color.x, EPSILON);
        assertEquals(0.0, light.color.y, EPSILON);
    }
}