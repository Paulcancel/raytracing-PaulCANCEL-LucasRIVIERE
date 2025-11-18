package com.imt.raytracing.raytracer;

import com.imt.raytracing.geometry.Point;
import com.imt.raytracing.geometry.Vector;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Camera {
    // LookFrom
    private Point lookFrom;

    // LookAt
    private Point lookAt;

    // Up vector
    private Vector up;

    // FOV
    private double fov;
}
