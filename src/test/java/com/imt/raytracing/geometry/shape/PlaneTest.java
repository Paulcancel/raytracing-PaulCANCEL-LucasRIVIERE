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
import com.imt.raytracing.geometry.shape.Plane.IntersectionPlane;

/**
 * Unit tests for the Plane class, covering various ray intersection scenarios.
 */
public class PlaneTest {

    private Plane plane;
    private Color diffuse;
    private Color specular;
    private double shininess;

    @BeforeEach
    void setUp() {
        // Define a simple plane: the XZ plane, passing through (0, 0, 0) with a normal pointing up (Y-axis)
        Point p0 = new Point(0, 0, 0);
        Vector normal = new Vector(0, 1, 0); // Normalized to (0, 1, 0)
        diffuse = new Color(0.5, 0.5, 0.5);
        specular = new Color(0.1, 0.1, 0.1);
        shininess = 10.0;
        
        plane = new Plane(p0, normal, diffuse, specular, shininess);
    }


    @Test
    void testPlaneConstructionAndNormalization() {
        // Test construction with a non-normalized normal
        Vector nonUnitNormal = new Vector(0, 5, 0);
        Plane testPlane = new Plane(new Point(1, 2, 3), nonUnitNormal, diffuse, specular, shininess);

        // Check that the normal was correctly normalized
        assertEquals(0.0, testPlane.normal.x, 1e-9);
        assertEquals(1.0, testPlane.normal.y, 1e-9);
        assertEquals(0.0, testPlane.normal.z, 1e-9);
    }


    @Test
    void testIntersection_DirectHit() {
        // Ray starts at (0, 10, 0) and points straight down
        Ray ray = new Ray(new Point(0, 10, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        assertTrue(inter.isPresent(), "Ray should hit the plane.");
        
        // Expected t value: distance is 10, direction is -1
        double expected_t = 10.0;
        assertEquals(expected_t, inter.get().t, 1e-9, "t value should be 10.0.");
        
        // Expected hit point P = (0, 10, 0) + 10 * (0, -1, 0) = (0, 0, 0)
        Point hitPoint = inter.get().point;
        assertEquals(0.0, hitPoint.x, 1e-9);
        assertEquals(0.0, hitPoint.y, 1e-9);
        assertEquals(0.0, hitPoint.z, 1e-9);

        // Check if the correct IntersectionPlane subclass is used and normal is set
        assertTrue(inter.get() instanceof IntersectionPlane, "Intersection should be of type IntersectionPlane.");
        assertEquals(plane.normal.x, inter.get().normal.x, 1e-9);
        assertEquals(plane.normal.y, inter.get().normal.y, 1e-9);
        assertEquals(plane.normal.z, inter.get().normal.z, 1e-9);
    }


    @Test
    void testIntersection_ParallelRay_OnPlane() {
        // Ray starts on the plane and is parallel (e.g., along X-axis)
        Ray ray = new Ray(new Point(1, 0, 0), new Vector(1, 0, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        assertFalse(inter.isPresent(), "Ray parallel to the plane should not hit (denom ≈ 0).");
    }

    @Test
    void testIntersection_ParallelRay_OffPlane() {
        // Ray starts above the plane and is parallel
        Ray ray = new Ray(new Point(1, 5, 0), new Vector(1, 0, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        assertFalse(inter.isPresent(), "Ray parallel to the plane should not hit (denom ≈ 0).");
    }

    @Test
    void testIntersection_IntersectionBehindOrigin() {
        // Ray starts below the plane at (0, -5, 0) and points down
        Ray ray = new Ray(new Point(0, -5, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        // Intersection occurs at t=-5 (behind the ray origin)
        assertFalse(inter.isPresent(), "Intersection behind the ray origin (t <= 0) should be ignored.");
    }
    
    @Test
    void testIntersection_IntersectionOnOrigin() {
        // Ray starts exactly on the plane and points down
        Ray ray = new Ray(new Point(0, 0, 0), new Vector(0, -1, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        // Intersection occurs at t=0 (on the ray origin)
        assertFalse(inter.isPresent(), "Intersection exactly on the origin (t <= 0) should be ignored.");
    }

    @Test
    void testIntersection_RayAwayFromPlane() {
        // Ray starts above (0, 10, 0) and points up, away from the XZ plane
        Ray ray = new Ray(new Point(0, 10, 0), new Vector(0, 1, 0)); 
        
        Optional<Intersection> inter = plane.intersect(ray);

        assertFalse(inter.isPresent(), "Ray pointing away from the plane should not hit.");
    }
}