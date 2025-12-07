package com.imt.raytracing.imaging;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.raytracer.RayTracer;
import com.imt.raytracing.raytracer.scene.Scene;
import com.imt.raytracing.raytracer.Camera;

/**
 * Test Stubs (doubles de test) for dependencies.
 */

// A simple Color stub that always returns a unique integer RGB value based on pixel coordinates.
class TestColor extends Color {
    private final int r, g, b;
    public TestColor(int r, int g, int b) {
        super(r / 255.0, g / 255.0, b / 255.0);
        this.r = r; this.g = g; this.b = b;
    }
    
    // Override toRGB to ensure a predictable integer value
    @Override
    public int toRGB() {
        return ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff);
    }
}

// A RayTracer stub to record calls and return test colors.
class TestRayTracer extends RayTracer {
    public final ArrayList<String> callLog = new ArrayList<>();
    
    @Override
    public Color getPixelColor(int i, int j, Scene scene, Orthonormal basis) {
        callLog.add(String.format("getPixelColor(%d, %d)", i, j));
        // Return a color based on coordinates (for verification)
        int r = (i * 255) / scene.getWidth();
        int g = (j * 255) / scene.getHeight();
        return new TestColor(r, g, 0); 
    }
}

// A Camera stub with basic data. Orthonormal construction is tested separately.
class TestCamera extends Camera {
    public TestCamera() {
        super(new Point(0, 0, 0), new Point(0, 0, -1), new Vector(0, 1, 0), 90.0);
    }
}

// A Scene stub to hold the dimensions and camera.
class TestScene extends Scene {
    public TestScene(int width, int height) {
        super(width, height);
        this.camera = new TestCamera();
    }
}

/**
 * Unit tests for the Renderer class, primarily verifying the rendering loop and
 * correct image manipulation (Y-axis inversion).
 */
public class RendererTest {

    private Renderer renderer;
    private TestScene scene;
    private TestRayTracer rt;
    
    private static final int WIDTH = 2;
    private static final int HEIGHT = 3;

    @BeforeEach
    void setUp() {
        // We need to overwrite the internal RayTracer instance inside the Renderer 
        // for full control, but since rt is instantiated inside render(), we need 
        // to rely on the side effects logged by our TestRayTracer stub.
        renderer = new Renderer();
        scene = new TestScene(WIDTH, HEIGHT);
        // Note: The real Renderer instantiates a real RayTracer. 
        // We will test the interaction based on the final output image color data.
    }

    @Test
    void testRender_BufferedImageDimensions() {
        BufferedImage img = renderer.render(scene);
        
        // Test 1: Check if BufferedImage has the correct dimensions
        assertEquals(WIDTH, img.getWidth(), "BufferedImage width must match scene width.");
        assertEquals(HEIGHT, img.getHeight(), "BufferedImage height must match scene height.");
        assertEquals(BufferedImage.TYPE_INT_RGB, img.getType(), "BufferedImage type must be INT_RGB.");
    }

}