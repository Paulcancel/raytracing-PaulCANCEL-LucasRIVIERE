package com.imt.raytracing.raytracer;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;


/**
 * Represents the virtual Camera (or viewer) in the scene.
 * It defines the position, orientation, and viewing angle (Field of View) 
 * needed to generate the primary rays.
 */
public class Camera {
    // The position of the camera's eye in world space.
    public Point lookFrom;

    // The point in world space the camera is focused on.
    public Point lookAt;

    // The vector defining the world's "up" direction, used to set the camera's orientation.
    public Vector up;

    // The Field of View (FOV), usually given in degrees, which determines 
    // the angular extent of the visible scene.
    public double fov;

    public Camera(Point lookFrom, Point lookAt, Vector up, double fov) {
        this.lookFrom = lookFrom;
        this.lookAt = lookAt;
        this.up = up;
        this.fov = fov;
    }

    public Point getLookFrom() {
        return lookFrom;
    }

    public Point getLookAt() {
        return lookAt;
    }

    public Vector getUp() {
        return up;
    }

    public double getFov() {
        return fov;
    }

    public void setLookFrom(Point lookFrom) {
        this.lookFrom = lookFrom;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }
}