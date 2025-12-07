package com.imt.raytracing.imaging;

import java.awt.image.BufferedImage;

import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.raytracer.RayTracer;
import com.imt.raytracing.raytracer.scene.Scene;

/**
 * The main rendering class responsible for processing a Scene and producing a final 
 * BufferedImage by shooting rays through every pixel.
 */
public class Renderer {

    /**
     * Renders the given scene by iterating over all pixels and calculating the color 
     * using the RayTracer.
     * @param scene The Scene object containing the camera, objects, and lighting.
     * @return A BufferedImage representing the final rendered image.
     */
    public BufferedImage render(Scene scene) {

        // Instantiate the RayTracer engine to calculate ray-object intersections and shading.
        RayTracer rt = new RayTracer();

        // Create the image buffer that will store the final output.
        BufferedImage img = new BufferedImage(scene.getWidth(), scene.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // Calculate the Orthonormal Basis for the camera's view transformation.
        // This defines the coordinate system (u, v, w vectors) for generating view rays.
        Orthonormal basis = new Orthonormal(
                scene.getCamera().getLookFrom(),
                scene.getCamera().getLookAt(),
                scene.getCamera().getUp()
        );

        // Start the main rendering loop: iterate over all rows (j) and columns (i).
        for (int j = 0; j < scene.getHeight(); j++) {
            for (int i = 0; i < scene.getWidth(); i++) {
                // Calculate the color for the current pixel (i, j).
                Color c = rt.getPixelColor(i, j, scene, basis);
                
                // Set the pixel color in the image buffer.
                // The j-index is inverted (scene.getHeight() - j - 1) because 
                // ray tracers often use the bottom-left corner as (0, 0), 
                // while BufferedImage uses the top-left corner as (0, 0).
                img.setRGB(i, scene.getHeight() - j - 1, c.toRGB());
            }
        }

        return img;
    }
}