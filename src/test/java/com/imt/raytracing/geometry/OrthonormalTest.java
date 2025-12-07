package com.imt.raytracing.geometry;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Orthonormal class, verifying the creation of a proper 
 * orthonormal basis (unit length and orthogonality).
 */
public class OrthonormalTest {

    private static final double EPSILON = 1e-9;

    /**
     * Helper method to verify a vector's length is 1.
     */
    private void assertIsUnitVector(Vector v, String message) {
        assertEquals(1.0, v.length(), EPSILON, message);
    }

    /**
     * Helper method to verify two vectors are orthogonal (dot product is 0).
     */
    private void assertIsOrthogonal(Vector v1, Vector v2, String message) {
        assertEquals(0.0, v1.dot(v2), EPSILON, message);
    }

    @Test
    void testOrthonormalBasis_StandardCase() {
        // Camera position (Look From): (0, 0, 10)
        Point lookFrom = new Point(0, 0, 10);
        // Target (Look At): (0, 0, 0)
        Point lookAt = new Point(0, 0, 0);
        // World Up: (0, 1, 0)
        Vector up = new Vector(0, 1, 0);

        Orthonormal basis = new Orthonormal(lookFrom, lookAt, up);

        // Expected vectors:
        // w = (lookFrom - lookAt) normalized = (0, 0, 10) -> (0, 0, 1) (Backward/Depth)
        // u = up x w normalized = (0, 1, 0) x (0, 0, 1) = (1, 0, 0) (Right/Horizontal)
        // v = w x u normalized = (0, 0, 1) x (1, 0, 0) = (0, 1, 0) (Up/Vertical)

        // Test 1: Vector components (Expected values)
        assertEquals(0.0, basis.w.x, EPSILON);
        assertEquals(0.0, basis.w.y, EPSILON);
        assertEquals(1.0, basis.w.z, EPSILON, "W vector should be (0, 0, 1)");

        assertEquals(1.0, basis.u.x, EPSILON);
        assertEquals(0.0, basis.u.y, EPSILON);
        assertEquals(0.0, basis.u.z, EPSILON, "U vector should be (1, 0, 0)");

        assertEquals(0.0, basis.v.x, EPSILON);
        assertEquals(1.0, basis.v.y, EPSILON);
        assertEquals(0.0, basis.v.z, EPSILON, "V vector should be (0, 1, 0)");

        // Test 2: Unit Length (w, u, v must have length 1)
        assertIsUnitVector(basis.w, "W must be a unit vector.");
        assertIsUnitVector(basis.u, "U must be a unit vector.");
        assertIsUnitVector(basis.v, "V must be a unit vector.");

        // Test 3: Orthogonality (w, u, v must be mutually orthogonal)
        assertIsOrthogonal(basis.w, basis.u, "W and U must be orthogonal.");
        assertIsOrthogonal(basis.w, basis.v, "W and V must be orthogonal.");
        assertIsOrthogonal(basis.u, basis.v, "U and V must be orthogonal.");
    }

    @Test
    void testOrthonormalBasis_RotatedCase() {
        // Camera position (Look From): (10, 0, 0)
        Point lookFrom = new Point(10, 0, 0);
        // Target (Look At): (0, 0, 0)
        Point lookAt = new Point(0, 0, 0);
        // World Up: (0, 1, 0)
        Vector up = new Vector(0, 1, 0);

        Orthonormal basis = new Orthonormal(lookFrom, lookAt, up);

        // Expected vectors:
        // w = (lookFrom - lookAt) normalized = (10, 0, 0) -> (1, 0, 0)
        // u = up x w normalized = (0, 1, 0) x (1, 0, 0) = (0, 0, -1)
        // v = w x u normalized = (1, 0, 0) x (0, 0, -1) = (0, 1, 0)

        // Test 1: Vector components (Expected values)
        assertEquals(1.0, basis.w.x, EPSILON);
        assertEquals(0.0, basis.w.y, EPSILON);
        assertEquals(0.0, basis.w.z, EPSILON, "W vector should be (1, 0, 0)");

        assertEquals(0.0, basis.u.x, EPSILON);
        assertEquals(0.0, basis.u.y, EPSILON);
        assertEquals(-1.0, basis.u.z, EPSILON, "U vector should be (0, 0, -1)");

        assertEquals(0.0, basis.v.x, EPSILON);
        assertEquals(1.0, basis.v.y, EPSILON);
        assertEquals(0.0, basis.v.z, EPSILON, "V vector should be (0, 1, 0)");

        // Test 2: Unit Length (w, u, v must have length 1)
        assertIsUnitVector(basis.w, "W must be a unit vector.");
        assertIsUnitVector(basis.u, "U must be a unit vector.");
        assertIsUnitVector(basis.v, "V must be a unit vector.");

        // Test 3: Orthogonality (w, u, v must be mutually orthogonal)
        assertIsOrthogonal(basis.w, basis.u, "W and U must be orthogonal.");
        assertIsOrthogonal(basis.w, basis.v, "W and V must be orthogonal.");
        assertIsOrthogonal(basis.u, basis.v, "U and V must be orthogonal.");
    }
    
    @Test
    void testOrthonormalBasis_UpVectorNotPerpendicular() {
        // Case where 'up' vector is not perpendicular to the view direction (LookFrom - LookAt).
        // W calculation should correct this automatically via the cross product.
        
        Point lookFrom = new Point(0, 0, 10);
        Point lookAt = new Point(0, 0, 0);
        // Up vector is slightly tilted: (0.5, 1, 0)
        Vector upTilted = new Vector(0.5, 1, 0);

        Orthonormal basis = new Orthonormal(lookFrom, lookAt, upTilted);
        
        // Expected W remains (0, 0, 1)
        // Expected U = upTilted x W normalized = (0.5, 1, 0) x (0, 0, 1) = (1, -0.5, 0) normalized
        
        Vector expectedU = new Vector(1, -0.5, 0).normalize();
        
        // Test U and W component-wise
        assertEquals(0.0, basis.w.x, EPSILON);
        assertEquals(1.0, basis.w.z, EPSILON);
        
        assertEquals(expectedU.x, basis.u.x, EPSILON);
        assertEquals(expectedU.y, basis.u.y, EPSILON);

        // Test 2: Unit Length
        assertIsUnitVector(basis.w, "W must be unit.");
        assertIsUnitVector(basis.u, "U must be unit.");
        assertIsUnitVector(basis.v, "V must be unit.");

        // Test 3: Orthogonality (Crucial test for tilted up vector)
        assertIsOrthogonal(basis.w, basis.u, "W and U must be orthogonal.");
        assertIsOrthogonal(basis.w, basis.v, "W and V must be orthogonal.");
        assertIsOrthogonal(basis.u, basis.v, "U and V must be orthogonal.");
    }
}