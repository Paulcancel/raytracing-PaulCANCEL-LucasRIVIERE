package com.imt.raytracing.raytracer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.geometry.shape.Shape;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.scene.Intersection;
import com.imt.raytracing.raytracer.scene.Scene;

/**
 * Test Stubs (doubles de test) for dependencies.
 */

// Camera stub aligned with the world axes for easy vector math verification
class TestCamera extends Camera {
    public TestCamera(Point lookFrom, double fov) {
        // LookAt: (0, 0, -1)
        // Up: (0, 1, 0)
        super(lookFrom, new Point(0, 0, -1), new Vector(0, 1, 0), fov);
    }
}

class TestShape extends Sphere {
    // Simule une Sphere très simple pour passer le test 'instanceof Sphere'
    public TestShape(Color d, Color s, double sh) {
        // Le centre et le rayon n'ont pas d'importance ici
        super(new Point(0, 0, 0), 1.0, d, s, sh); 
    }
    
    // Le test d'ombre utilisera le Point et le Rayon du hit.
    @Override
    public Optional<Intersection> intersect(Ray ray) {
        // Intersect est utilisé dans Scene.closestIntersection, donc on peut le laisser tel quel
        return Optional.empty(); 
    }
}

// Scene stub with predefined dimensions and camera
class TestScene extends Scene {
    public TestScene(int width, int height, double fov) {
        this.width = width;
        this.height = height;
        this.camera = new TestCamera(new Point(0, 0, 0), fov);
    }
    
    // Stub the intersection method for shading tests
    private Optional<Intersection> intersectionResult = Optional.empty();
    
    public void setIntersectionResult(Optional<Intersection> result) {
        this.intersectionResult = result;
    }

    @Override
    public Optional<Intersection> closestIntersection(Ray ray) {
        return intersectionResult;
    }
}

/**
 * Unit tests for the RayTracer class, focusing on the ray generation formula 
 * (getPixelColor) and the handling of hits/misses.
 */
public class RayTracerTest {

    private RayTracer rayTracer;
    private TestScene scene;
    private Orthonormal basis;
    
    private static final int WIDTH = 200;
    private static final int HEIGHT = 100;
    private static final double FOV = 90.0;
    private static final double EPSILON = 1e-4;

    @BeforeEach
    void setUp() {
        rayTracer = new RayTracer();
        scene = new TestScene(WIDTH, HEIGHT, FOV);
        
        // Base Orthonormal basis for (0, 0, 0) looking at (0, 0, -1) with Up (0, 1, 0)
        // w=(0, 0, 1), u=(1, 0, 0), v=(0, 1, 0)
        basis = new Orthonormal(scene.camera.lookFrom, scene.camera.lookAt, scene.camera.up);
        
        // Sanity check on the test basis
        assertEquals(1.0, basis.u.x, EPSILON);
        assertEquals(0.0, basis.w.z, EPSILON); 
    }
}