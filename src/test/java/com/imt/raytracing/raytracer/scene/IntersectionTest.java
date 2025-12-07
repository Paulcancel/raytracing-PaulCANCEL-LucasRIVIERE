package com.imt.raytracing.raytracer.scene;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.geometry.shape.Shape;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.geometry.shape.Triangle;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.light.DirectionalLight;
import com.imt.raytracing.raytracer.light.PointLight;



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


class TestScene extends Scene {
    public TestScene(int width, int height) {
        super(width, height);
    }

    private Optional<Intersection> shadowHit = Optional.empty();

    public void setShadowHit(Optional<Intersection> hit) {
        this.shadowHit = hit;
    }

    @Override
    public Optional<Intersection> closestIntersection(Ray ray) {
        // Return the predefined result for shadow rays
        return shadowHit;
    }
}

/**
 * Unit tests for the Intersection class, verifying construction, normal calculation, 
 * shadow testing, and shading models (Diffuse and Specular).
 */
public class IntersectionTest {

    private static final double EPSILON = 1e-4;
    private Intersection intersection;
    private Ray incomingRay;
    private TestScene scene;
    private Sphere sphere;
    private Triangle triangle;
    private Color diffuseRed = new Color(1.0, 0.0, 0.0);
    private Color specularWhite = new Color(1.0, 1.0, 1.0);

    @BeforeEach
    void setUp() {
        // Setup a simple ray, centered sphere, and intersection point
        Point rayOrigin = new Point(0, 0, 10);
        Vector rayDirection = new Vector(0, 0, -1);
        incomingRay = new Ray(rayOrigin, rayDirection); // Direction is (0, 0, -1) (normalized)

        sphere = new Sphere(new Point(0, 0, 0), 5.0, diffuseRed, specularWhite, 50.0);
        
        // Ray hits the sphere at t=5.0 (at point (0, 0, 5))
        double t = 5.0;
        intersection = new Intersection(t, sphere, incomingRay);
        
        scene = new TestScene(100, 100);
        scene.shapes.add(sphere);
    }

    // --- Constructor Tests ---

    @Test
    void testIntersectionConstruction_SphereNormal() {
        // Hit point P = (0, 0, 10) + 5.0 * (0, 0, -1) = (0, 0, 5)
        assertEquals(5.0, intersection.point.z, EPSILON);
        
        // Sphere Normal = (P - Center) normalized = ((0, 0, 5) - (0, 0, 0)) -> (0, 0, 1)
        assertEquals(0.0, intersection.normal.x, EPSILON);
        assertEquals(1.0, intersection.normal.z, EPSILON); // Normal points outward (+Z)
    }

    @Test
    void testIntersectionConstruction_TriangleNormal() {
        // Triangle in XZ plane with precomputed normal (0, 1, 0)
        Point ta = new Point(0, 0, 0);
        Point tb = new Point(1, 0, 0);
        Point tc = new Point(0, 0, 1);
        
        // The Triangle constructor calculates normal = (B-A) x (C-A). 
        // (1, 0, 0) x (0, 0, 1) = (0, -1, 0).
        triangle = new Triangle(ta, tb, tc, diffuseRed, specularWhite, 50.0); 
        
        // Ray hits the triangle at t=10.0 (Point: (0, 0, 0))
        Intersection triInter = new Intersection(10.0, triangle, incomingRay); 

        // Triangle Normal should use the precomputed normal
        assertEquals(0.0, triInter.normal.x, EPSILON);
        assertEquals(-1.0, triInter.normal.y, EPSILON);
    }

    // --- Shadow Tests (isShadowed) ---

    // Note: The epsilon used for the shadow ray start point is 1e-4

    @Test
    void testIsShadowed_DirectionalLight_NoBlocker() {
        // Directional light coming from +Z, hitting the normal (+Z)
        DirectionalLight light = new DirectionalLight(new Vector(0, 0, 1), new Color(1, 1, 1));
        
        // Ray direction (L) = light.direction * -1 = (0, 0, -1)
        scene.setShadowHit(Optional.empty()); // No object blocks the shadow ray
        
        assertFalse(intersection.isShadowed(scene, light), "Should not be shadowed when no object is hit.");
    }
    
    @Test
    void testIsShadowed_DirectionalLight_IsBlocked() {
        // Directional light coming from +Z
        DirectionalLight light = new DirectionalLight(new Vector(0, 0, 1), new Color(1, 1, 1));
        
        // Shadow Ray (L) goes (0, 0, -1) (towards origin)
        
        // Blocker is found at t=1.0 (closer than infinity maxDist)
        Shape blocker = new TestShape(null, null, 0);
        Intersection shadowHit = new Intersection(1.0, blocker, new Ray(intersection.point, new Vector(0, 0, -1)));
        scene.setShadowHit(Optional.of(shadowHit));
        
        assertTrue(intersection.isShadowed(scene, light), "Should be shadowed when blocker is found.");
    }

    @Test
    void testIsShadowed_PointLight_NoBlocker() {
        // Point light at (0, 0, 20) (farther away from the hit point (0, 0, 5))
        PointLight light = new PointLight(new Point(0, 0, 20), new Color(1, 1, 1)); 
        
        scene.setShadowHit(Optional.empty()); 
        
        assertFalse(intersection.isShadowed(scene, light), "Should not be shadowed.");
    }
    
    @Test
    void testIsShadowed_PointLight_IsBlocked() {
        // Point light at (0, 0, 10). Hit point is (0, 0, 5). Max distance = 5.0
        PointLight light = new PointLight(new Point(0, 0, 10), new Color(1, 1, 1)); 
        
        // Blocker found at t=1.0 (t < maxDist=5.0)
        Shape blocker = new TestShape(null, null, 0);
        Intersection shadowHit = new Intersection(1.0, blocker, new Ray(intersection.point, new Vector(0, 0, 1)));
        scene.setShadowHit(Optional.of(shadowHit));
        
        assertTrue(intersection.isShadowed(scene, light), "Should be shadowed when blocker is closer than light.");
    }

    @Test
    void testIsShadowed_PointLight_BlockerTooFar() {
        // Point light at (0, 0, 10). Hit point is (0, 0, 5). Max distance = 5.0
        PointLight light = new PointLight(new Point(0, 0, 10), new Color(1, 1, 1)); 
        
        // Blocker found at t=6.0 (t > maxDist=5.0)
        Shape blocker = new TestShape(null, null, 0);
        Intersection shadowHit = new Intersection(6.0, blocker, new Ray(intersection.point, new Vector(0, 0, 1)));
        scene.setShadowHit(Optional.of(shadowHit));
        
        assertFalse(intersection.isShadowed(scene, light), "Should NOT be shadowed when blocker is farther than point light.");
    }

    // --- Shading Tests (Diffuse & Specular) ---

    @Test
    void testDiffuse_FacingLight() {
        // Light L direction (from point to light) is (0, 0, 1)
        PointLight light = new PointLight(new Point(0, 0, 10), new Color(1, 1, 1)); 
        
        // Normal N is (0, 0, 1). N dot L = 1.0 (max brightness)
        double expectedDot = 1.0;
        
        // Final Color = (N dot L) * LightColor * DiffuseColor
        // Result = 1.0 * (1, 1, 1) * (1, 0, 0) = (1, 0, 0)
        Color result = intersection.diffuse(light);
        
        assertEquals(expectedDot * 1.0 * 1.0, result.x, EPSILON);
        assertEquals(expectedDot * 1.0 * 0.0, result.y, EPSILON);
    }
    
    @Test
    void testDiffuse_GrazingAngle() {
        // Light L direction (from point to light) is (1, 0, 0)
        PointLight light = new PointLight(new Point(5, 0, 5), new Color(1, 1, 1)); 
        
        
        // Result = 0.0 * ... = (0, 0, 0)
        Color result = intersection.diffuse(light);
        
        assertEquals(0.0, result.x, EPSILON);
        assertEquals(0.0, result.y, EPSILON);
    }

    @Test
    void testSpecularPhong_FacingLight_ZeroHighlight() {
        // Light L direction (from point to light) is (0, 0, 1)
        PointLight light = new PointLight(new Point(0, 0, 10), new Color(1, 1, 1)); 
        
        // View V direction (from point to camera) is (0, 0, 1)
        // Normal N is (0, 0, 1)
        
        // Half-vector H = (L + V) normalized = ((0, 0, 1) + (0, 0, 1)) -> (0, 0, 1)
        // N dot H = 1.0 (maximum specular highlight)
        double expectedP = Math.pow(1.0, sphere.shininess); // expectedP = 1.0
        
        // Final Color = expectedP * LightColor * SpecularColor
        // Result = 1.0 * (1, 1, 1) * (1, 1, 1) = (1, 1, 1)
        Color result = intersection.specularPhong(light);
        
        assertEquals(expectedP * 1.0 * 1.0, result.x, EPSILON);
    }

    @Test
    void testSpecularPhong_TiltedHighlight() {
        // Let's use simpler vectors for testing the Phong calculation directly:
        // N = (0, 1, 0)
        // L = (1, 1, 0) normalized (from light source)
        // V = (1, 1, 0) normalized (to camera)
        // Intersection is set up for N=(0, 0, 1) and V=(0, 0, 1)
        
        // Use the initial intersection where N=(0, 0, 1) and V=(0, 0, 1)
        // Light L direction: (1, 0, 0) (from light source)
        PointLight light = new PointLight(new Point(5, 0, 5), new Color(1, 1, 1)); // L is (1, 0, 0) normalized
        
        // L = (1, 0, 0)
        // V = (0, 0, 1) (incomingRay.direction * -1)
        // H = (L + V) normalized = (1, 0, 1) normalized ≈ (0.707, 0, 0.707)
        // N = (0, 0, 1)
        // N dot H = 0.707
        double dotNH = 1.0 / Math.sqrt(2); // ≈ 0.7071
        
        // Blinn-Phong power p = (N dot H)^50
        double expectedP = Math.pow(dotNH, sphere.shininess); 
        
        Color result = intersection.specularPhong(light);
        
        assertEquals(expectedP, result.x, EPSILON, "Specular highlight intensity should match Blinn-Phong calculation.");
    }
}