package com.imt.raytracing;

import javax.imageio.ImageIO;

import com.imt.raytracing.imaging.Renderer;
import com.imt.raytracing.parsing.SceneFileParser;
import com.imt.raytracing.raytracer.scene.Scene;

import java.awt.image.BufferedImage;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {

        SceneFileParser parser = new SceneFileParser();
        Scene scene = parser.parse(args[0]);

        Renderer renderer = new Renderer();
        BufferedImage img = renderer.render(scene);

        ImageIO.write(img, "png", new File(scene.getOutput()));
        System.out.println("Image générée dans " + scene.getOutput());
    }
}

