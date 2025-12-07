package com.imt.raytracing.geometry;

/**
 * Represents a Point in 3D space.
 * In 3D graphics and ray tracing, a Point has a location but no magnitude or direction, 
 * adhering to the rules of affine geometry.
 */
public class Point extends AbstractVec3 {

    /**
     * Constructs a default Point at the origin (0, 0, 0).
     */
    public Point() {
        super(0, 0, 0);
    }

    /**
     * Constructs a Point with the specified coordinates.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Subtracts another Point from this Point.
     * The result of subtracting two Points is a Vector (the displacement between them).
     * * @param p The Point to subtract.
     * @return A new Vector from Point p to this Point.
     */
    public Vector sub(Point p) {
        return new Vector(x - p.x, y - p.y, z - p.z);
    }

    /**
     * Adds a Vector to this Point.
     * The result of adding a Vector to a Point is a new Point (translation).
     * * @param v The Vector to add (the translation).
     * @return A new Point resulting from the translation.
     */
    public Point add(Vector v) {
        return new Point(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    /**
     * Utility method used by parent class operations to create a new instance 
     * of the correct type (Point) after an operation.
     */
    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new Point(x, y, z);
    }
}