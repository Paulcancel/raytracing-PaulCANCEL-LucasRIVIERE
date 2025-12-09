package com.imt.raytracing.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Vector class, verifying its algebraic and geometric operations.
 */
public class VectorTest {

    private Vector v1;
    private Vector v2;
    private Vector zeroVector;
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        // Test vectors
        v1 = new Vector(1.0, 2.0, 3.0);
        v2 = new Vector(4.0, -1.0, 2.5);
        zeroVector = new Vector(0, 0, 0);
    }

    
    @Test
    void testParameterizedConstructor() {
        assertEquals(1.0, v1.x, EPSILON);
        assertEquals(2.0, v1.y, EPSILON);
        assertEquals(3.0, v1.z, EPSILON);
    }


    @Test
    void testAdd() {
        // (1, 2, 3) + (4, -1, 2.5) = (5, 1, 5.5)
        Vector result = v1.add(v2);
        assertEquals(5.0, result.x, EPSILON);
        assertEquals(1.0, result.y, EPSILON);
        assertEquals(5.5, result.z, EPSILON);
    }

    @Test
    void testSub() {
        // (1, 2, 3) - (4, -1, 2.5) = (-3, 3, 0.5)
        Vector result = v1.sub(v2);
        assertEquals(-3.0, result.x, EPSILON);
        assertEquals(3.0, result.y, EPSILON);
        assertEquals(0.5, result.z, EPSILON);
    }

    @Test
    void testMul_ScalarMultiplication() {
        // (1, 2, 3) * 3.0 = (3, 6, 9)
        double scalar = 3.0;
        Vector result = v1.mul(scalar);
        assertEquals(3.0, result.x, EPSILON);
        assertEquals(6.0, result.y, EPSILON);
        assertEquals(9.0, result.z, EPSILON);
    }


    @Test
    void testDotProduct() {
        // (1, 2, 3) · (4, -1, 2.5) = (1*4) + (2*-1) + (3*2.5) = 4 - 2 + 7.5 = 9.5
        double result = v1.dot(v2);
        assertEquals(9.5, result, EPSILON);
    }

    @Test
    void testCrossProduct() {
        // v1 x v2 = (1, 2, 3) x (4, -1, 2.5)
        // x = (2*2.5) - (3*-1) = 5 + 3 = 8
        // y = (3*4) - (1*2.5) = 12 - 2.5 = 9.5
        // z = (1*-1) - (2*4) = -1 - 8 = -9
        Vector result = v1.cross(v2);
        assertEquals(8.0, result.x, EPSILON);
        assertEquals(9.5, result.y, EPSILON);
        assertEquals(-9.0, result.z, EPSILON);
    }
    
    @Test
    void testCrossProduct_Orthogonality() {
        // Result of cross product must be orthogonal to both input vectors (dot product should be 0)
        Vector crossResult = v1.cross(v2);
        assertEquals(0.0, crossResult.dot(v1), EPSILON, "Cross product result must be orthogonal to v1.");
        assertEquals(0.0, crossResult.dot(v2), EPSILON, "Cross product result must be orthogonal to v2.");
    }


    @Test
    void testLength() {
        // (1, 2, 3).length = sqrt(1^2 + 2^2 + 3^2) = sqrt(14)
        double expected = Math.sqrt(14.0);
        assertEquals(expected, v1.length(), EPSILON);
        
        assertEquals(0.0, zeroVector.length(), EPSILON);
    }

    @Test
    void testNormalize_StandardVector() {
        // v1 = (1, 2, 3) -> length = sqrt(14)
        double len = Math.sqrt(14.0);
        Vector result = v1.normalize();
        
        assertEquals(1.0 / len, result.x, EPSILON);
        assertEquals(2.0 / len, result.y, EPSILON);
        assertEquals(3.0 / len, result.z, EPSILON);
        
        assertEquals(1.0, result.length(), EPSILON);
    }
}