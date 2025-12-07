package com.imt.raytracing.raytracer;


import java.util.Optional;

import com.imt.raytracing.geometry.Orthonormal;
import com.imt.raytracing.geometry.Vector;
import com.imt.raytracing.imaging.Color;
import com.imt.raytracing.raytracer.scene.Intersection;
import com.imt.raytracing.raytracer.scene.Scene;

/**
 * The core engine of the ray tracing system.
 * It is responsible for generating the primary rays that go from the camera through 
 * the image plane (pixel grid) into the scene, and for determining the color of each pixel.
 */
public class RayTracer {

    /**
     * Calculates the color for a specific pixel (i, j) on the image plane.
     * This involves generating the viewing ray, checking for intersection, and shading the result.
     * @param i The pixel's column index (x-coordinate).
     * @param j The pixel's row index (y-coordinate).
     * @param scene The Scene containing the camera, lights, and shapes.
     * @param basis The Orthonormal basis (u, v, w vectors) defining the camera's orientation.
     * @return The resulting Color of the pixel.
     */
    public Color getPixelColor(int i, int j, Scene scene, Orthonormal basis) {

        // Convert the camera's field of view (FOV) from degrees to radians
        double fovr = Math.toRadians(scene.camera.fov);
        
        // Calculate half the height of the virtual image plane at distance 1
        // (tan(fov/2) is the height of the half-angle triangle)
        double pixelH = Math.tan(fovr / 2);
        
        // Calculate half the width of the virtual image plane, adjusted for aspect ratio
        double pixelW = pixelH * ((double) scene.width / scene.height);

        // Map pixel coordinates (i, j) to continuous coordinates (a, b) on the image plane.
        // The image center is (width/2.0, height/2.0).
        // 'a' is the horizontal coordinate, scaled by half the width of the image plane (pixelW).
        double a = pixelW * ((i - scene.width/2.0) + 0.5) / (scene.width/2.0);
        // 'b' is the vertical coordinate, scaled by half the height of the image plane (pixelH).
        double b = pixelH * ((j - scene.height/2.0) + 0.5) / (scene.height/2.0);

        // Calculate the ray direction vector in world space:
        // Ray_Dir = a*u + b*v - 1*w (w is the depth axis, multiplied by -1 to point into the scene)
        Vector dir = basis.u.mul(a)
                .add(basis.v.mul(b))
                .add(basis.w.mul(-1))
                .normalize();

        // Create the primary ray starting at the camera's position (lookFrom)
        Ray ray = new Ray(scene.camera.lookFrom, dir);

        // Find the closest intersection of this ray with any object in the scene
        Optional<Intersection> inter = scene.closestIntersection(ray);

        // If the ray hits nothing, return black
        if (inter.isEmpty())
            return new Color(0,0,0);

        // If the ray hits an object, calculate the final color using the shading model
        return inter.get().shade(scene);
    }
}