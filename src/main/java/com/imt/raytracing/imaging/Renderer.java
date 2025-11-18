package com.imt.raytracing.imaging;

import java.awt.image.BufferedImage;

import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.raytracer.RayTracer;
import com.imt.raytracing.raytracer.scene.Scene;

public class Renderer {

    public BufferedImage render(Scene scene) {

        RayTracer rt = new RayTracer();

        BufferedImage img = new BufferedImage(scene.getWidth(), scene.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Orthonormal basis = new Orthonormal(
                scene.getCamera().getLookFrom(),
                scene.getCamera().getLookAt(),
                scene.getCamera().getUp()
        );

        for (int j = 0; j < scene.getHeight(); j++) {
            for (int i = 0; i < scene.getWidth(); i++) {
                Color c = rt.getPixelColor(i, j, scene, basis);
                img.setRGB(i, scene.getHeight() - j - 1, c.toRGB());
            }
        }

        return img;
    }
}
