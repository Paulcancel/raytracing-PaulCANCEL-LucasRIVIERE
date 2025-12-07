package com.imt.raytracing.geometry;

/**
 * Represents an orthonormal basis.
 * This is often used to define a coordinate system for a camera or local object space in ray tracing.
 */
public class Orthonormal {
    /** The u vector, typically the right/horizontal axis of the basis. */
    public Vector u;
    /** The v vector, typically the up/vertical axis of the basis. */
    public Vector v;
    /** The w vector, typically the backward/depth axis of the basis. */
    public Vector w;

    /**
     * Constructs an orthonormal basis from a camera's position, target, and an 'up' direction.
     * The basis is calculated such that:
     *
     * @param lookFrom The position of the camera/eye.
     * @param lookAt The point the camera is looking at.
     * @param up A vector defining the world's 'up' direction (used to orient the camera).
     */
    public Orthonormal(Point lookFrom, Point lookAt, Vector up) {
        // w points in the opposite direction of the view ray (from lookAt to lookFrom)
        w = lookFrom.sub(lookAt).normalize();
        // u is perpendicular to the view direction (w) and the general world up (up)
        u = up.cross(w).normalize();
        // v is perpendicular to both w and u, completing the orthonormal basis
        v = w.cross(u).normalize();
    }
}