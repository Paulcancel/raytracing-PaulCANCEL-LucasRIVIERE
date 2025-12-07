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

/**
 * Unit tests for the Sphere class, covering various ray intersection scenarios.
 */
public class SphereTest {

    private Sphere sphere;
    private Color diffuse;
    private Color specular;
    private double shininess;
    private Point center;
    private double radius;

    @BeforeEach
    void setUp() {
        // Define a standard sphere centered at (0, 0, 0) with radius 5
        center = new Point(0, 0, 0);
        radius = 5.0;
        diffuse = new Color(0.5, 0.5, 0.5);
        specular = new Color(0.1, 0.1, 0.1);
        shininess = 10.0;
        
        sphere = new Sphere(center, radius, diffuse, specular, shininess);
    }

    // --- Intersection Tests: Valid Hits (t > 0) ---

    @Test
    void testIntersection_TwoHitsClosestIsFront() {
        // Ray starts at (0, 0, -10) and points along +Z
        Ray ray = new Ray(new Point(0, 0, -10), new Vector(0, 0, 1)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        assertTrue(inter.isPresent(), "The ray should hit the sphere at two points.");
        
        // Expected intersections: 
        // t1 = 5 (at point (0, 0, -5))
        // t2 = 15 (at point (0, 0, 5))
        // Closest positive t is t1=5
        double expected_t = 5.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "The closest t value should be 5.0.");
        
        // Check hit point
        Point hitPoint = inter.get().point;
        assertEquals(0.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(-5.0, hitPoint.z, 1e-9);
    }

    @Test
    void testIntersection_RayOriginInsideSphere() {
        // Ray starts at (0, 0, 0) (inside) and points along +Z
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        assertTrue(inter.isPresent(), "Ray starting inside should hit the sphere surface once.");

        // Expected intersections: 
        // t1 = -5 (behind)
        // t2 = 5 (in front)
        // Closest positive t is t2=5
        double expected_t = 5.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "The closest t value should be 5.0 (exit point).");
        
        // Check hit point
        Point hitPoint = inter.get().point;
        assertEquals(0.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(5.0, hitPoint.z, 1e-9);
    }

    @Test
    void testIntersection_TangentHit() {
        // Ray starts at (5, 5, 0) and points straight towards (5, 0, 0)
        Ray ray = new Ray(new Point(5, 5, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        assertTrue(inter.isPresent(), "The ray should hit the sphere tangentially (delta ≈ 0).");

        // Expected intersection at (5, 0, 0): t = 5
        double expected_t = 5.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "The t value should be 5.0.");
        
        // Check hit point
        Point hitPoint = inter.get().point;
        assertEquals(5.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(0.0, hitPoint.z, 1e-9);
    }
    
    // --- Intersection Tests: Misses (t <= 0 or delta < 0) ---

    @Test
    void testIntersection_Missed() {
        // Ray starts at (0, 10, 0) and points along +Z (it misses the sphere)
        Ray ray = new Ray(new Point(0, 10, 0), new Vector(0, 0, 1)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        assertFalse(inter.isPresent(), "The ray should miss the sphere (delta < 0).");
    }

    @Test
    void testIntersection_BehindOrigin() {
        // Ray starts at (0, 0, 10) and points along +Z, away from the sphere (0, 0, 0)
        Ray ray = new Ray(new Point(0, 0, 10), new Vector(0, 0, 1)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        // Intersections: t1=-15, t2=-5. Both are negative.
        assertFalse(inter.isPresent(), "Intersection behind the ray origin (t <= 0) should be ignored.");
    }
    
    @Test
    void testIntersection_StartingOnSurface() {
        // Ray starts exactly on the surface at (5, 0, 0) and points away
        Ray ray = new Ray(new Point(5, 0, 0), new Vector(1, 0, 0)); 
        
        Optional<Intersection> inter = sphere.intersect(ray);

        // Intersections: t1=0, t2=-10. t=0 should be ignored by t > epsilon check.
        assertFalse(inter.isPresent(), "Intersection exactly on the origin (t <= epsilon) should be ignored.");
    }
}