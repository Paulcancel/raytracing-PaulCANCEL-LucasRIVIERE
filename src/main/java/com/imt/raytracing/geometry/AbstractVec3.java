package com.imt.raytracing.geometry;


/**
 * Base abstract class representing a 3D vector or point.
 * This class provides core vector arithmetic such as addition, subtraction,
 * scalar multiplication, dot product, cross product, normalization,
 * and component-wise multiplication (Schur product).
 * Concrete subclasses (e.g., {@code Vector}, {@code Point}) must implement
 * the {@link #create(double, double, double)} method to return an instance
 * of their own type.
 */
public abstract class AbstractVec3 {
    /** X coordinate or component. */
    public double x;
    /** Y coordinate or component. */
    public double y;
    /** Z coordinate or component. */
    public double z;

    /**
     * Creates a 3D vector initialized to (0, 0, 0).
     */
    public AbstractVec3() {
        this(0, 0, 0);
    }

    /**
     * Creates a 3D vector with the given coordinates.
     *
     * @param x the x component
     * @param y the y component
     * @param z the z component
     */
    public AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Adds another vector to this one.
     *
     * @param v the vector to add
     * @return a new vector representing {@code this + v}
     */
    public AbstractVec3 add(AbstractVec3 v) {
        return create(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Subtracts another vector from this one.
     *
     * @param v the vector to subtract
     * @return a new vector representing {@code this - v}
     */
    public AbstractVec3 sub(AbstractVec3 v) {
        return create(x - v.x, y - v.y, z - v.z);
    }

    /**
     * Multiplies this vector by a scalar.
     *
     * @param d the scalar value
     * @return a new vector representing {@code this * d}
     */
    public AbstractVec3 mul(double d) {
        return create(x * d, y * d, z * d);
    }

    /**
     * Computes the dot (inner) product with another vector.
     *
     * @param v the other vector
     * @return the dot product value
     */
    public double dot(AbstractVec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    /**
     * Computes the cross product with another vector.
     *
     * @param v the other vector
     * @return a new vector representing {@code this × v}
     */
    public AbstractVec3 cross(AbstractVec3 v) {
        return create(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        );
    }

    /**
     * Computes the Schur (component-wise) product with another vector.
     *
     * @param v the other vector
     * @return a new vector with each component multiplied independently
     */
    public AbstractVec3 schur(AbstractVec3 v) {
        return create(x * v.x, y * v.y, z * v.z);
    }

    /**
     * Computes the Euclidean length of this vector.
     *
     * @return the magnitude of the vector
     */
    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Normalizes this vector to unit length.
     * If the vector is zero-length, (0,0,0) is returned.
     *
     * @return a new normalized vector
     */
    public AbstractVec3 normalize() {
        double len = length();
        if (len == 0) return create(0, 0, 0);
        return create(x / len, y / len, z / len);
    }

    /** Tolerance used for floating-point comparison. */
    public static final double EPSILON = 1e-9;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractVec3 v = (AbstractVec3) obj;
        return Math.abs(x - v.x) < EPSILON &&
               Math.abs(y - v.y) < EPSILON &&
               Math.abs(z - v.z) < EPSILON;
    }

    /**
     * Factory method implemented by subclasses to create new instances
     * of the correct concrete type (e.g., Point or Vector).
     *
     * @param x x component
     * @param y y component
     * @param z z component
     * @return a new instance of the subclass
     */
    protected abstract AbstractVec3 create(double x, double y, double z);

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f, %.4f)", x, y, z);
    }
}
