package com.imt.raytracing.raytracer;

import java.util.Optional;


import com.imt.raytracing.geometry.Intersection;
import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.scene.Scene;


public class RayTracer {

    public Color getPixelColor(int i, int j, Scene scene, Orthonormal basis) {

        double fovr = Math.toRadians(scene.getCamera().getFov());
        double pixelH = Math.tan(fovr / 2);
        double pixelW = pixelH * ((double) scene.getWidth() / scene.getHeight());

        double a = pixelW  * ((i - scene.getWidth()  / 2.0)  + 0.5) / (scene.getWidth()  / 2.0);
        double b = pixelH * ((j - scene.getHeight() / 2.0) + 0.5) / (scene.getHeight() / 2.0);

        Vector dir = basis.u.mul(a)
                .add(basis.v.mul(b))
                .add(basis.w.mul(-1))
                .normalize();

        Ray ray = new Ray(scene.getCamera().getLookFrom(), dir);

        Optional<Intersection> inter = scene.closestIntersection(ray);

        return inter.isPresent() ? scene.getAmbient() : new Color(0,0,0);
    }
}