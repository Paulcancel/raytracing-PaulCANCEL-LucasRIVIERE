package com.imt.raytracing.raytracer;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;

/**
 * Unit tests for the Camera class, verifying correct initialization of 
 * its geometric and viewing parameters.
 */
public class CameraTest {

    private Point lookFrom;
    private Point lookAt;
    private Vector up;
    private double fov;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() {
        lookFrom = new Point(0, 0, 10);
        lookAt = new Point(0, 0, 0);
        up = new Vector(0, 1, 0);
        fov = 60.0;
    }

    @Test
    void testCameraConstruction() {
        Camera camera = new Camera(lookFrom, lookAt, up, fov);

        // Test 1: Check Look From position
        assertEquals(lookFrom, camera.getLookFrom(), "LookFrom should be correctly initialized.");
        assertEquals(10.0, camera.getLookFrom().z, EPSILON);

        // Test 2: Check Look At target
        assertEquals(lookAt, camera.getLookAt(), "LookAt should be correctly initialized.");

        // Test 3: Check Up vector
        assertEquals(up, camera.getUp(), "Up vector should be correctly initialized.");
        assertEquals(1.0, camera.getUp().y, EPSILON);

        // Test 4: Check Field of View
        assertEquals(fov, camera.getFov(), EPSILON, "FOV should be correctly initialized.");
    }
    
    @Test
    void testCameraSetters() {
        Camera camera = new Camera(lookFrom, lookAt, up, fov);
        
        // Define new values
        Point newLookFrom = new Point(5, 5, 5);
        double newFov = 90.0;

        // Use setters
        camera.setLookFrom(newLookFrom);
        camera.setFov(newFov);
        
        // Verify changes
        assertEquals(newLookFrom, camera.getLookFrom(), "Setter for LookFrom should work.");
        assertEquals(90.0, camera.getFov(), EPSILON, "Setter for FOV should work.");
    }
}