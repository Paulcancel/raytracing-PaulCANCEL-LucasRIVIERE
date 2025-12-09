package com.imt.raytracing.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Point class, verifying its constructors and affine operations 
 * (Point - Point = Vector, Point + Vector = Point).
 */
public class PointTest {

    private Point p1;
    private Point p2;
    private Vector v1;
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = AbstractVec3.EPSILON;

    @BeforeEach
    void setUp() {
        // Test points
        p1 = new Point(5.0, 10.0, 3.0);
        p2 = new Point(1.0, 2.0, 4.0);
        
        // Test vector
        v1 = new Vector(2.0, -1.0, 0.5);
    }

    
    @Test
    void testDefaultConstructor() {
        Point p = new Point();
        assertEquals(0.0, p.x, EPSILON);
        assertEquals(0.0, p.y, EPSILON);
        assertEquals(0.0, p.z, EPSILON);
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(5.0, p1.x, EPSILON);
        assertEquals(10.0, p1.y, EPSILON);
        assertEquals(3.0, p1.z, EPSILON);
    }


    @Test
    void testSub_PointMinusPoint_ReturnsVector() {
        // P1 - P2 = (5, 10, 3) - (1, 2, 4) = (4, 8, -1)
        Vector result = p1.sub(p2);
        
        assertEquals(4.0, result.x, EPSILON);
        assertEquals(8.0, result.y, EPSILON);
        assertEquals(-1.0, result.z, EPSILON);
        
        // Ensure the returned type is Vector
        assertTrue(result instanceof Vector, "P1 - P2 must return a Vector.");
    }

    @Test
    void testAdd_PointPlusVector_ReturnsPoint() {
        // P1 + V1 = (5, 10, 3) + (2, -1, 0.5) = (7, 9, 3.5)
        Point result = p1.add(v1);
        
        assertEquals(7.0, result.x, EPSILON);
        assertEquals(9.0, result.y, EPSILON);
        assertEquals(3.5, result.z, EPSILON);
        
        // Ensure the returned type is Point
        assertTrue(result instanceof Point, "P1 + V1 must return a Point.");
    }
    

    @Test
    void testInheritedMul_ReturnsPoint() {
        // P1 * 2.0 = (5, 10, 3) * 2 = (10, 20, 6)
        AbstractVec3 result = p1.mul(2.0);
        
        assertEquals(10.0, result.x, EPSILON);
        assertEquals(20.0, result.y, EPSILON);
        assertEquals(6.0, result.z, EPSILON);
        
        assertTrue(result instanceof Point, "Inherited operations on Point must return a Point.");
    }
}