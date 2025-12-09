package com.imt.raytracing.geometry;


/**
 * Represents a Vector in 3D space.
 * A Vector has both magnitude (length) and direction, but no fixed position.
 * It provides common vector operations necessary for ray tracing.
 */
public class Vector {
    // The components of the vector.
    public double x, y, z;

    /**
     * Constructs a Vector with the specified components.
     * @param x The x-component.
     * @param y The y-component.
     * @param z The z-component.
     */
    public Vector(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Adds this Vector to another Vector (Vector addition).
     * @param v The Vector to add.
     * @return A new Vector representing the sum.
     */
    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Subtracts another Vector from this Vector (Vector subtraction).
     * @param v The Vector to subtract.
     * @return A new Vector representing the difference.
     */
    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Multiplies this Vector by a scalar value (Scalar multiplication).
     * @param k The scalar value.
     * @return A new Vector scaled by k.
     */
    public Vector mul(double k) {
        return new Vector(x * k, y * k, z * k);
    }

    /**
     * Computes the Dot Product (scalar product) of this Vector and another Vector.
     * The result is a scalar that indicates the angle between the two vectors.
     * @param v The other Vector.
     * @return The scalar dot product.
     */
    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }

    /**
     * Computes the Cross Product (vector product) of this Vector and another Vector.
     * The result is a new Vector perpendicular to both input vectors.
     * @param v The other Vector.
     * @return A new Vector perpendicular to both input vectors.
     */
    public Vector cross(Vector v) {
        return new Vector(
            y*v.z - z*v.y,
            z*v.x - x*v.z,
            x*v.y - y*v.x
        );
    }

    /**
     * Calculates the length (magnitude) of the Vector.
     * This is the square root of the dot product of the vector with itself.
     * @return The length of the Vector.
     */
    public double length() { return Math.sqrt(dot(this)); }

    // A small value used for checking near-zero lengths.
    private static final double EPSILON = 1e-9;

    /**
     * Returns a normalized (unit) Vector with the same direction but a length of 1.
     * If the vector length is near zero, it handles the division carefully.
     * @return A new unit Vector.
     */
    public Vector normalize() {
        double L = length();
        if (L < EPSILON) {
            return new Vector(-x / L, -y / L, -z / L); 
        }
        return new Vector(x / L, y / L, z / L);
    }
}