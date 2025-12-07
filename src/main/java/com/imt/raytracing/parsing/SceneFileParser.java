package com.imt.raytracing.parsing;

import java.io.*;
import java.util.ArrayList;

import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.scene.Scene;
import com.imt.raytracing.raytracer.Camera;
import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.raytracer.light.DirectionalLight;
import com.imt.raytracing.raytracer.light.PointLight;
import com.imt.raytracing.geometry.shape.Plane;
import com.imt.raytracing.geometry.shape.Sphere;
import com.imt.raytracing.geometry.shape.Triangle;

/**
 * A parser responsible for reading a plain-text scene description file 
 * and building the corresponding Scene object used by the Ray Tracer.
 * It interprets keywords (e.g., "camera", "sphere", "light") and assigns 
 * properties based on the following numeric values.
 */
public class SceneFileParser {

    /**
     * Reads a scene description from a file, parses the commands, and constructs 
     * a complete Scene object.
     * @param filename The path to the scene file (e.g., a .txt or .scene file).
     * @return A fully constructed Scene object.
     * @throws Exception If there is a file error or a syntax error in the scene file.
     */
    public Scene parse(String filename) throws Exception {
        Scene scene = new Scene();

        // Current material properties. These are state variables that apply to the 
        // next shape defined (e.g., sphere, triangle, plane).
        Color currentDiffuse = new Color(0,0,0); 
        Color currentSpecular = new Color(0,0,0);
        double currentShininess = 10.0;

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Loop through every line in the file
            while ((line = br.readLine()) != null) {
                line = line.trim();
                // Skip empty lines or lines starting with a comment '#'
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                // Split the line into tokens based on whitespace
                String[] tok = line.split("\\s+");
                
                // Process the instruction based on the first token (the keyword)
                switch (tok[0]) {
                    case "size":
                        // Set the width and height of the final image
                        scene.width = Integer.parseInt(tok[1]);
                        scene.height = Integer.parseInt(tok[2]);
                        break;
                    case "output":
                        // Set the output filename
                        scene.output = tok[1];
                        break;
                    case "camera":
                        // Define the camera with lookFrom, lookAt, up vector, and field of view (fov)
                        scene.camera = new Camera(
                            new Point(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3])),
                            new Point(Double.parseDouble(tok[4]), Double.parseDouble(tok[5]), Double.parseDouble(tok[6])),
                            new Vector(Double.parseDouble(tok[7]), Double.parseDouble(tok[8]), Double.parseDouble(tok[9])),
                            Double.parseDouble(tok[10])
                        );
                        break;
                    case "ambient":
                        // Set the global ambient light color
                        scene.ambient = new Color(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3]));
                        break;
                    case "diffuse":
                        // Set the current diffuse material color (applies to next shape)
                        currentDiffuse = new Color(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3]));
                        break;
                    case "specular":
                        // Set the current specular material color (applies to next shape)
                        currentSpecular = new Color(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3]));
                        break;
                    case "shininess":
                        // Set the current shininess exponent (applies to next shape)
                        currentShininess = Double.parseDouble(tok[1]);
                        break;
                    case "directional":
                        // Add a directional light source (direction vector and color)
                        scene.lights.add(new DirectionalLight(
                                new Vector(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3])),
                                new Color(Double.parseDouble(tok[4]), Double.parseDouble(tok[5]), Double.parseDouble(tok[6]))
                        ));
                        break;
                    case "point":
                        // Add a point light source (position point and color)
                        scene.lights.add(new PointLight(
                                new Point(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3])),
                                new Color(Double.parseDouble(tok[4]), Double.parseDouble(tok[5]), Double.parseDouble(tok[6]))
                        ));
                        break;
                    case "sphere":
                        // Add a sphere with center (x, y, z) and radius (r), using current material properties
                        Point c = new Point(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3]));
                        double r = Double.parseDouble(tok[4]);
                        scene.shapes.add(new Sphere(c, r, currentDiffuse, currentSpecular, currentShininess));
                        break;

                    case "maxverts":
                        // Define the maximum number of vertices that can be stored for triangle meshes
                        scene.maxverts = Integer.parseInt(tok[1]);
                        scene.vertices = new ArrayList<>(scene.maxverts);
                        break;

                    case "vertex":
                        // Store a single vertex point in the list for later use by 'tri' commands
                        if (scene.vertices.size() >= scene.maxverts)
                            throw new Exception("Error: Too many vertices defined compared to maxverts limit.");

                        scene.vertices.add(new Point(
                                Double.parseDouble(tok[1]),
                                Double.parseDouble(tok[2]),
                                Double.parseDouble(tok[3])
                        ));
                        break;
                    case "tri":
                        // Add a triangle shape using the indices of three previously defined vertices
                        int i1 = Integer.parseInt(tok[1]);
                        int i2 = Integer.parseInt(tok[2]);
                        int i3 = Integer.parseInt(tok[3]);

                        // Check if indices are valid
                        if (i1 >= scene.maxverts || i2 >= scene.maxverts || i3 >= scene.maxverts)
                            throw new Exception("Error: Vertex index out of bounds (>= maxverts).");

                        // Create the triangle using the stored vertex points and current material properties
                        scene.shapes.add(new Triangle(
                                scene.vertices.get(i1),
                                scene.vertices.get(i2),
                                scene.vertices.get(i3),
                                currentDiffuse,
                                currentSpecular,
                                currentShininess
                        ));
                        break;
                    case "plane":
                        // Add a plane shape defined by a point on the plane and its normal vector, 
                        // using current material properties
                        scene.shapes.add(new Plane(
                            new Point(Double.parseDouble(tok[1]), Double.parseDouble(tok[2]), Double.parseDouble(tok[3])),
                            new Vector(Double.parseDouble(tok[4]), Double.parseDouble(tok[5]), Double.parseDouble(tok[6])),
                            currentDiffuse, currentSpecular, currentShininess
                        ));
                        break;
                    default:
                        // Log unknown instructions but continue parsing
                        System.out.println("Ignore instruction: " + tok[0]);
                }
            }
        }
        return scene;
    }
}