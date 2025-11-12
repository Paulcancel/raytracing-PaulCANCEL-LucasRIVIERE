package com.imt.raytracing;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
