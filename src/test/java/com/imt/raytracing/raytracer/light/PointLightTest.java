package com.imt.raytracing.raytracer.light;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.imaging.Color;

/**
 * Unit tests for the PointLight class, verifying correct initialization 
 * of the origin position and inherited color property.
 */
public class PointLightTest {

    private Color testColor;
    private Point testOrigin;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        testColor = new Color(1.0, 1.0, 0.0);
        testOrigin = new Point(10.0, 5.0, -2.0);
    }

    @Test
    void testPointLightConstruction() {
        PointLight light = new PointLight(testOrigin, testColor);

        // Test 1: Check inherited color property
        assertNotNull(light.color, "Light color should not be null.");
        assertEquals(testColor.x, light.color.x, EPSILON, "Color red component must match.");
        
        // Test 2: Check origin point
        assertNotNull(light.origin, "Origin point should not be null.");
        assertEquals(testOrigin.x, light.origin.x, EPSILON, "Origin X coordinate must match.");
        assertEquals(testOrigin.y, light.origin.y, EPSILON, "Origin Y coordinate must match.");
        assertEquals(testOrigin.z, light.origin.z, EPSILON, "Origin Z coordinate must match.");
    }

    @Test
    void testPointLightConstruction_OriginAtZero() {
        Point zeroOrigin = new Point(0, 0, 0);
        PointLight light = new PointLight(zeroOrigin, testColor);
        
        assertEquals(0.0, light.origin.x, EPSILON);
        assertEquals(0.0, light.origin.y, EPSILON);
        assertEquals(0.0, light.origin.z, EPSILON);
    }
}