package com.imt.raytracing.geometry;

public class Orthonormal {
    public Vector u, v, w;

    public Orthonormal(Point lookFrom, Point lookAt, Vector up) {
        w = lookFrom.sub(lookAt).normalize();
        u = up.cross(w).normalize();
        v = w.cross(u).normalize();
    }
}