package com.imt.raytracing;

public abstract class AbstractVec3 {

    protected double x;
    protected double y;
    protected double z;

    // ----- Constructeurs -----
    public AbstractVec3() {
        this(0, 0, 0);
    }

    public AbstractVec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // ----- Getters -----
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }

    // ----- Opérations de base -----
    public AbstractVec3 add(AbstractVec3 v) {
        return create(x + v.x, y + v.y, z + v.z);
    }

    public AbstractVec3 sub(AbstractVec3 v) {
        return create(x - v.x, y - v.y, z - v.z);
    }

    public AbstractVec3 mul(double d) {
        return create(x * d, y * d, z * d);
    }

    public double dot(AbstractVec3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public AbstractVec3 cross(AbstractVec3 v) {
        return create(
            y * v.z - z * v.y,
            z * v.x - x * v.z,
            x * v.y - y * v.x
        );
    }

    public AbstractVec3 schur(AbstractVec3 v) {
        return create(x * v.x, y * v.y, z * v.z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public AbstractVec3 normalize() {
        double len = length();
        if (len == 0) return create(0, 0, 0);
        return create(x / len, y / len, z / len);
    }

    // ----- Comparaison -----
    private static final double EPSILON = 1e-9;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        AbstractVec3 v = (AbstractVec3) obj;
        return Math.abs(x - v.x) < EPSILON &&
               Math.abs(y - v.y) < EPSILON &&
               Math.abs(z - v.z) < EPSILON;
    }

    // ----- Méthode utilitaire -----
    protected abstract AbstractVec3 create(double x, double y, double z);

    @Override
    public String toString() {
        return String.format("(%.4f, %.4f, %.4f)", x, y, z);
    }
}
