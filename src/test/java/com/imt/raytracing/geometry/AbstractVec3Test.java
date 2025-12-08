package com.imt.raytracing.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Concrete test implementation of AbstractVec3 to allow unit testing of the base class methods.
 */
class ConcreteVec3 extends AbstractVec3 {
    public ConcreteVec3(double x, double y, double z) {
        super(x, y, z);
    }
    public ConcreteVec3() {
        super(0, 0, 0);
    }
    
    // Implements the abstract factory method to return instances of the concrete class
    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new ConcreteVec3(x, y, z);
    }
}

/**
 * Unit tests for the AbstractVec3 base class operations.
 */
public class AbstractVec3Test {

    private ConcreteVec3 v1;
    private ConcreteVec3 v2;
    private ConcreteVec3 zero;
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = AbstractVec3.EPSILON;

    @BeforeEach
    void setUp() {
        // Test vectors
        v1 = new ConcreteVec3(1.0, 2.0, 3.0);
        v2 = new ConcreteVec3(4.0, -1.0, 2.5);
        zero = new ConcreteVec3(0, 0, 0);
    }

    // --- Constructor Tests ---
    
    @Test
    void testDefaultConstructor() {
        assertEquals(0.0, zero.x, EPSILON);
        assertEquals(0.0, zero.y, EPSILON);
        assertEquals(0.0, zero.z, EPSILON);
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(1.0, v1.x, EPSILON);
        assertEquals(2.0, v1.y, EPSILON);
        assertEquals(3.0, v1.z, EPSILON);
    }
    
    // --- Vector Arithmetic Tests ---

    @Test
    void testAdd() {
        // (1, 2, 3) + (4, -1, 2.5) = (5, 1, 5.5)
        AbstractVec3 result = v1.add(v2);
        assertEquals(5.0, result.x, EPSILON);
        assertEquals(1.0, result.y, EPSILON);
        assertEquals(5.5, result.z, EPSILON);
        assertTrue(result instanceof ConcreteVec3, "Result must be the concrete type.");
    }

    @Test
    void testSub() {
        // (1, 2, 3) - (4, -1, 2.5) = (-3, 3, 0.5)
        AbstractVec3 result = v1.sub(v2);
        assertEquals(-3.0, result.x, EPSILON);
        assertEquals(3.0, result.y, EPSILON);
        assertEquals(0.5, result.z, EPSILON);
    }

    @Test
    void testMul() {
        // (1, 2, 3) * 3.0 = (3, 6, 9)
        double scalar = 3.0;
        AbstractVec3 result = v1.mul(scalar);
        assertEquals(3.0, result.x, EPSILON);
        assertEquals(6.0, result.y, EPSILON);
        assertEquals(9.0, result.z, EPSILON);
    }
    
    @Test
    void testSchurProduct() {
        // (1, 2, 3) * (4, -1, 2.5) = (4, -2, 7.5)
        AbstractVec3 result = v1.schur(v2);
        assertEquals(4.0, result.x, EPSILON);
        assertEquals(-2.0, result.y, EPSILON);
        assertEquals(7.5, result.z, EPSILON);
    }

    // --- Geometric Tests ---

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
        AbstractVec3 result = v1.cross(v2);
        assertEquals(8.0, result.x, EPSILON);
        assertEquals(9.5, result.y, EPSILON);
        assertEquals(-9.0, result.z, EPSILON);
    }

    @Test
    void testCrossProduct_ParallelVectors() {
        // A vector crossed with itself should be the zero vector
        AbstractVec3 v3 = v1.mul(2.0);
        AbstractVec3 result = v1.cross(v3);
        assertEquals(0.0, result.x, EPSILON);
        assertEquals(0.0, result.y, EPSILON);
        assertEquals(0.0, result.z, EPSILON);
    }

    @Test
    void testLength() {
        // (1, 2, 3).length = sqrt(1^2 + 2^2 + 3^2) = sqrt(1 + 4 + 9) = sqrt(14)
        double expected = Math.sqrt(14.0);
        assertEquals(expected, v1.length(), EPSILON);
        
        assertEquals(0.0, zero.length(), EPSILON);
    }

    @Test
    void testNormalize() {
        // v1 = (1, 2, 3) -> length = sqrt(14)
        double len = Math.sqrt(14.0);
        AbstractVec3 result = v1.normalize();
        
        assertEquals(1.0 / len, result.x, EPSILON);
        assertEquals(2.0 / len, result.y, EPSILON);
        assertEquals(3.0 / len, result.z, EPSILON);
        
        // The result should have unit length
        assertEquals(1.0, result.length(), EPSILON);
    }

    @Test
    void testNormalize_ZeroVector() {
        // Normalizing the zero vector should return the zero vector
        AbstractVec3 result = zero.normalize();
        assertEquals(0.0, result.x, EPSILON);
        assertEquals(0.0, result.y, EPSILON);
        assertEquals(0.0, result.z, EPSILON);
    }

    // --- Utility Tests ---
    
    @Test
    void testEquals() {
        // Equality check with tolerance (EPSILON)
        AbstractVec3 v_close = new ConcreteVec3(1.0 + 1e-10, 2.0 - 1e-10, 3.0);
        AbstractVec3 v_different = new ConcreteVec3(1.0, 2.0, 4.0);
        
        assertTrue(v1.equals(v1)); // Self-reference
        assertTrue(v1.equals(v_close)); // Within tolerance
        assertFalse(v1.equals(v_different)); // Outside tolerance
        assertFalse(v1.equals(null)); // Null check
    }
    
}
        