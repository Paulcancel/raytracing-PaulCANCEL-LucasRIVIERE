package com.imt.raytracing.raytracer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;

/**
 * Unit tests for the Ray class, verifying correct initialization and 
 * mandatory normalization of the direction vector.
 */
public class RayTest {

    private Point testOrigin;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        testOrigin = new Point(1.0, 2.0, 3.0);
    }

    @Test
    void testRayConstruction_DirectionIsNormalized() {
        // Test a non-unit direction vector (length = sqrt(1^2 + 2^2 + 3^2) = sqrt(14))
        Vector unnormalizedDirection = new Vector(1.0, 2.0, 3.0);
        
        Ray ray = new Ray(testOrigin, unnormalizedDirection);

        // Test 1: Check origin remains unchanged
        assertEquals(testOrigin.x, ray.origin.x, EPSILON, "Origin X should match.");
        
        // Test 2: Check if the direction vector is normalized
        assertEquals(1.0, ray.direction.length(), EPSILON, "Direction vector must be unit length.");
        
        // Test 3: Check direction components (1/sqrt(14), 2/sqrt(14), 3/sqrt(14))
        double expectedLength = Math.sqrt(14.0);
        assertEquals(1.0 / expectedLength, ray.direction.x, EPSILON, "Direction X component must be normalized.");
        assertEquals(3.0 / expectedLength, ray.direction.z, EPSILON, "Direction Z component must be normalized.");
    }

    @Test
    void testRayConstruction_DirectionAlreadyUnit() {
        // Test a vector that is already normalized (e.g., along the X-axis)
        Vector unitDirection = new Vector(1.0, 0.0, 0.0);
        
        Ray ray = new Ray(testOrigin, unitDirection);

        // Check if the vector remains (1, 0, 0)
        assertEquals(1.0, ray.direction.x, EPSILON);
        assertEquals(0.0, ray.direction.y, EPSILON);
        
        // Check length is still 1.0
        assertEquals(1.0, ray.direction.length(), EPSILON, "Direction vector should remain unit length.");
    }
}