package com.imt.raytracing.raytracer.scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.shape.Shape;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.Camera;
import com.imt.raytracing.raytracer.Ray;
import com.imt.raytracing.raytracer.light.Light;


/**
 * Represents the entire 3D environment to be rendered.
 * It holds the camera, image settings, global lighting, and all geometric objects (shapes).
 */
public class Scene {
    // --- Image Settings ---
    public int width;
    public int height;
    public Camera camera;
    public String output = "img/jalon3/output.png";
    public int maxdepth = 1;
    
    // --- Lighting & Geometry ---
    public Color ambient = new Color();
    public List<Light> lights = new ArrayList<>();
    public List<Shape> shapes = new ArrayList<>();
    
    // --- Vertex Data for Meshes ---
    public List<Point> vertices = new ArrayList<>();
    public int maxverts = 0; // Maximum number of vertices allowed for meshes


    public Scene(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Scene() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Camera getCamera() {
        return camera;
    }

    public String getOutput() {
        return output;
    }

    public int getMaxdepth() {
        return maxdepth;
    }

    public Color getAmbient() {
        return ambient;
    }

    public List<Light> getLights() {
        return lights;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public List<Point> getVertices() {
        return vertices;
    }

    public int getMaxverts() {
        return maxverts;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Sets the dimensions of the final image.
     * @param width The width in pixels.
     * @param height The height in pixels.
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Adds a light source to the scene.
     * @param light The Light object to add.
     */
    public void addLight(Light light) {
        this.lights.add(light);
    }

    /**
     * Adds a shape (e.g., Sphere, Plane, Triangle) to the scene's list of objects.
     * Note: The current method signature only accepts Sphere, but the 'shapes' list 
     * holds the general Shape type.
     * @param shape The Sphere object to add.
     */
    public void addShape(Sphere shape) {
        this.shapes.add(shape);
    }

    /**
     * Finds the closest valid intersection between a given ray and any shape in the scene.
     * This is the fundamental function for visibility testing (ray casting and shadow rays).
     * @param ray The Ray to test intersections against.
     * @return An Optional containing the closest Intersection object, or empty if no shape is hit.
     */
    public Optional<Intersection> closestIntersection(Ray ray) {
        Intersection best = null;

        // Iterate through all shapes in the scene
        for (Shape s : shapes) {
            Optional<Intersection> inter = s.intersect(ray);
            
            // If an intersection occurred
            if (inter.isPresent()) {
                // Check if this intersection is the closest one so far (smallest positive 't' value)
                if (best == null || inter.get().t < best.t)
                    best = inter.get();
            }
        }
        // Return the closest intersection found, or null/empty if none was found
        return Optional.ofNullable(best);
    }
}