package com.imt.raytracing.parsing;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.shape.Plane;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.geometry.shape.Triangle;
import com.imt.raytracing.raytracer.light.DirectionalLight;
import com.imt.raytracing.raytracer.light.PointLight;
import com.imt.raytracing.raytracer.scene.Scene;

/**
 * Unit tests for the SceneFileParser class. 
 * This involves simulating a scene file and verifying the Scene object's state 
 * after parsing.
 */
public class SceneFileParserTest {

    private SceneFileParser parser;
    private Path tempFile;
    private static final double EPSILON = 1e-9;

    @BeforeEach
    void setUp() throws IOException {
        parser = new SceneFileParser();
        // Create a temporary file for testing
        tempFile = Files.createTempFile("sceneTest", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        // Clean up the temporary file
        Files.deleteIfExists(tempFile);
    }

    /**
     * Helper method to write content to the temporary scene file.
     */
    private void writeSceneContent(String content) throws IOException {
        try (PrintWriter writer = new PrintWriter(tempFile.toFile())) {
            writer.print(content);
        }
    }

    // --- Basic Scene Setup Commands Tests ---

    @Test
    void testParse_SizeOutputAndAmbient() throws Exception {
        String content = 
            "size 640 480\n" +
            "output image.png\n" +
            "ambient 0.1 0.2 0.3\n" +
            "# This is a comment and should be ignored";

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(640, scene.width, "Scene width should be set correctly.");
        assertEquals(480, scene.height, "Scene height should be set correctly.");
        assertEquals("image.png", scene.output, "Output filename should be set.");
        
        // Check ambient color
        assertEquals(0.1, scene.ambient.x, EPSILON);
        assertEquals(0.2, scene.ambient.y, EPSILON);
        assertEquals(0.3, scene.ambient.z, EPSILON);
    }

    @Test
    void testParse_CameraDefinition() throws Exception {
        String content = 
            "camera 0 0 5 0 0 0 0 1 0 60.0"; // lookFrom, lookAt, up, fov

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertNotNull(scene.camera, "Camera object should be created.");
        
        // Check lookFrom (0, 0, 5)
        assertEquals(5.0, scene.camera.lookFrom.z, EPSILON);
        
        // Check lookAt (0, 0, 0)
        assertEquals(0.0, scene.camera.lookAt.x, EPSILON);
        
        // Check up (0, 1, 0)
        assertEquals(1.0, scene.camera.up.y, EPSILON);
        
        // Check FOV (60.0)
        assertEquals(60.0, scene.camera.fov, EPSILON);
    }

    // --- Light Source Commands Tests ---

    @Test
    void testParse_DirectionalLight() throws Exception {
        String content = 
            "directional 1 0 0 0.8 0.8 0.8"; // direction, color

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(1, scene.lights.size(), "One directional light should be added.");
        assertTrue(scene.lights.get(0) instanceof DirectionalLight, "Light should be DirectionalLight.");
        
        DirectionalLight dl = (DirectionalLight) scene.lights.get(0);
        
        // Check direction (1, 0, 0)
        assertEquals(1.0, dl.direction.x, EPSILON);
        
        // Check color (0.8, 0.8, 0.8)
        assertEquals(0.8, dl.color.x, EPSILON);
    }

    @Test
    void testParse_PointLight() throws Exception {
        String content = 
            "point 10 20 30 0.5 0.5 0.5"; // position, color

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(1, scene.lights.size(), "One point light should be added.");
        assertTrue(scene.lights.get(0) instanceof PointLight, "Light should be PointLight.");
        
        PointLight pl = (PointLight) scene.lights.get(0);
        
        // Check origin (10, 20, 30)
        assertEquals(20.0, pl.origin.y, EPSILON);
        
        // Check color (0.5, 0.5, 0.5)
        assertEquals(0.5, pl.color.x, EPSILON);
    }
    
    // --- Material State and Shape Commands Tests ---

    @Test
    void testParse_MaterialStateAndSphere() throws Exception {
        String content = 
            "diffuse 1.0 0.0 0.0\n" +
            "specular 0.5 0.5 0.5\n" +
            "shininess 100.0\n" +
            "sphere 0 0 0 1.0"; // center, radius

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(1, scene.shapes.size(), "One shape (sphere) should be added.");
        assertTrue(scene.shapes.get(0) instanceof Sphere, "Shape should be Sphere.");
        
        Sphere s = (Sphere) scene.shapes.get(0);
        
        // Check geometry
        assertEquals(1.0, s.radius, EPSILON);
        assertEquals(0.0, s.center.x, EPSILON);
        
        // Check material properties carried by the state
        assertEquals(1.0, s.diffuse.x, EPSILON);
        assertEquals(0.5, s.specular.x, EPSILON);
        assertEquals(100.0, s.shininess, EPSILON);
    }

    @Test
    void testParse_Plane() throws Exception {
        String content = 
            "plane 0 0 0 0 1 0\n"; // point on plane, normal vector

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(1, scene.shapes.size(), "One shape (plane) should be added.");
        Plane p = (Plane) scene.shapes.get(0);
        
        // Check normal
        assertEquals(1.0, p.normal.y, EPSILON);
    }
    
    // --- Vertex and Triangle Commands Tests ---

    @Test
    void testParse_VertexAndTriangle() throws Exception {
        String content = 
            "maxverts 3\n" +
            "vertex 0 0 0\n" + // Index 0
            "vertex 1 0 0\n" + // Index 1
            "vertex 0 1 0\n" + // Index 2
            "tri 0 1 2"; // Triangle using indices

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());

        assertEquals(3, scene.vertices.size(), "Three vertices should be stored.");
        assertEquals(1, scene.shapes.size(), "One triangle should be added.");
        
        Triangle tri = (Triangle) scene.shapes.get(0);
        
        // Check vertices linkage (using known vertex coordinates)
        assertEquals(0.0, tri.a.x, EPSILON); // Vertex 0
        assertEquals(1.0, tri.b.x, EPSILON); // Vertex 1
        assertEquals(1.0, tri.c.y, EPSILON); // Vertex 2

        // Check maxverts boundary error
        String contentError = "maxverts 1\nvertex 0 0 0\nvertex 1 1 1";
        writeSceneContent(contentError);
        assertThrows(Exception.class, () -> parser.parse(tempFile.toString()), 
                     "Should throw exception if vertex count exceeds maxverts.");
    }

    @Test
    void testParse_UnknownInstruction() throws Exception {
        // This test mainly verifies that parsing continues after an unknown instruction
        String content = 
            "size 10 10\n" +
            "foo 1 2 3\n" + // Unknown instruction
            "ambient 0 0 0";

        writeSceneContent(content);
        Scene scene = parser.parse(tempFile.toString());
        
        assertEquals(10, scene.width);
        // We cannot check the console output, but we verify the parsing completed successfully
        // and the last valid instruction ('ambient') was processed.
        assertEquals(0.0, scene.ambient.x);
    }
}