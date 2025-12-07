package com.imt.raytracing;

import javax.imageio.ImageIO;

import com.imt.raytracing.imaging.Renderer;
import com.imt.raytracing.parsing.SceneFileParser;
import com.imt.raytracing.raytracer.scene.Scene;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * The entry point of the ray tracing application.
 * It is responsible for parsing the scene file, triggering the rendering process, 
 * and saving the final image to a file.
 */
public class Main {
    /**
     * The main method executes the ray tracing process.
     * @param args Command line arguments. Expects the path to the scene file as the first argument (args[0]).
     * @throws Exception If file operations or parsing fails.
     */
    public static void main(String[] args) throws Exception {

        // 1. Parse the scene file
        SceneFileParser parser = new SceneFileParser();
        // The scene file path is expected as the first argument (e.g., java -jar raytracer.jar myscene.txt)
        Scene scene = parser.parse(args[0]);

        // 2. Render the scene
        Renderer renderer = new Renderer();
        // The renderer processes the scene and produces the final image data
        BufferedImage img = renderer.render(scene);

        // 3. Save the output image
        // Use the output file path specified in the scene file
        ImageIO.write(img, "png", new File(scene.getOutput()));
        
        // Final confirmation message
        System.out.println("Image generated at " + scene.getOutput());
    }
}