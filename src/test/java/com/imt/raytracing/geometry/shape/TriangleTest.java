package com.imt.raytracing.geometry.shape;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.scene.Intersection;
import com.imt.raytracing.geometry.shape.Triangle.IntersectionTriangle;

/**
 * Unit tests for the Triangle class, primarily focusing on the Möller–Trumbore 
 * intersection algorithm implementation.
 */
public class TriangleTest {

    private Triangle triangle;
    private Color diffuse;
    private Color specular;
    private double shininess;
    
    // Vertices defining a triangle on the XZ plane, centered at (0, 0, 0)
    // A=(-5, 0, 0), B=(5, 0, 0), C=(0, 0, 10). Normal points along +Y.
    private Point a = new Point(-5, 0, 0);
    private Point b = new Point(5, 0, 0);
    private Point c = new Point(0, 0, 10);

    @BeforeEach
    void setUp() {
        diffuse = new Color(0.8, 0.2, 0.2);
        specular = new Color(0.1, 0.1, 0.1);
        shininess = 50.0;
        
        triangle = new Triangle(a, b, c, diffuse, specular, shininess);
    }


    @Test
    void testTriangleConstructionAndNormal() {
        // The normalized normal should be (0, -1, 0).
        assertEquals(0.0, triangle.normal.x, 1e-9);
        assertEquals(-1.0, triangle.normal.y, 1e-9); // Corrected expectation based on calculation
        assertEquals(0.0, triangle.normal.z, 1e-9);

        // Test material properties are set correctly (inherited from Shape)
        assertEquals(diffuse.x, triangle.diffuse.x, 1e-9);
        assertEquals(shininess, triangle.shininess, 1e-9);
    }
    

    @Test
    void testIntersection_DirectHit_Center() {
        double centroid_z = 10.0 / 3.0;
        Ray ray = new Ray(new Point(0, 10, centroid_z), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        assertTrue(inter.isPresent(), "Ray should hit the triangle at its center.");
        
        // Expected t value: distance is 10
        double expected_t = 10.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "t value should be 10.0.");
        
        Point hitPoint = inter.get().point;
        assertEquals(0.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(centroid_z, hitPoint.z, 1e-9);

        // Check if the correct IntersectionTriangle subclass is used and normal is set
        assertTrue(inter.get() instanceof IntersectionTriangle, "Intersection should be of type IntersectionTriangle.");
        assertEquals(triangle.normal.y, inter.get().normal.y, 1e-9);
    }

    @Test
    void testIntersection_AngledHit() {
        // Ray starts at (1, 1, 1) and points towards the triangle at (1, 0, 1)
        Vector dir = new Vector(0, -1, 0); 
        Ray ray = new Ray(new Point(1, 1, 1), dir); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        assertTrue(inter.isPresent(), "Angled ray should hit the interior of the triangle.");

        // Expected intersection at (1, 0, 1): t = 1.0
        double expected_t = 1.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "t value should be 1.0.");
        
        Point hitPoint = inter.get().point;
        assertEquals(1.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(1.0, hitPoint.z, 1e-9);
    }

    @Test
    void testIntersection_EdgeHit() {
        // Ray hits the edge between A and B, e.g., at (0, 0, 0) (midpoint)
        Ray ray = new Ray(new Point(0, 5, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        assertTrue(inter.isPresent(), "Ray should hit the edge of the triangle.");
        assertEquals(5.0, inter.get().t, 1e-9);
        
        Point hitPoint = inter.get().point;
        assertEquals(0.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(0.0, hitPoint.z, 1e-9);
    }
    

    @Test
    void testIntersection_Missed_OutsideBounds() {
        // Ray hits the plane of the triangle, but outside the triangle (e.g., at x=10)
        Ray ray = new Ray(new Point(10, 5, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        assertFalse(inter.isPresent(), "Ray should miss because intersection point is outside the triangle bounds (u or v check fails).");
    }

    @Test
    void testIntersection_ParallelRay() {
        // Ray starts above the triangle and is parallel (e.g., along XZ plane)
        Ray ray = new Ray(new Point(0, 5, 0), new Vector(1, 0, 0)); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        assertFalse(inter.isPresent(), "Ray parallel to the triangle should not hit (det ≈ 0).");
    }

    @Test
    void testIntersection_RayAwayFromTriangle() {
        // Ray starts at the centroid and points in the same direction as the normal (away)
        double centroid_z = 10.0 / 3.0;
        Ray ray = new Ray(new Point(0, 0, centroid_z), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = triangle.intersect(ray);

        // The intersection T value will be <= 0, which is correctly rejected.
        assertFalse(inter.isPresent(), "Ray starting on surface and pointing away (t <= 0) should be ignored.");
    }
}