package com.imt.raytracing;

public class Point extends AbstractVec3 {

    public Point() {
        super(0, 0, 0);
    }

    public Point(double x, double y, double z) {
        super(x, y, z);
    }

    // Soustraction de deux points -> Vecteur
    public Vector sub(Point p) {
        return new Vector(x - p.x, y - p.y, z - p.z);
    }

    // Addition d’un vecteur à un point -> Point
    public Point add(Vector v) {
        return new Point(x + v.getX(), y + v.getY(), z + v.getZ());
    }

    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new Point(x, y, z);
    }
}
