package com.imt.raytracing;

public class Vector extends AbstractVec3 {

    public Vector() {
        super(0, 0, 0);
    }

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new Vector(x, y, z);
    }

    // Produit vectoriel renvoie un Vector (spécifique)
    @Override
    public Vector cross(AbstractVec3 v) {
        return new Vector(
            y * v.getZ() - z * v.getY(),
            z * v.getX() - x * v.getZ(),
            x * v.getY() - y * v.getX()
        );
    }

    // Normalisation renvoie un Vector
    @Override
    public Vector normalize() {
        double len = length();
        if (len == 0) return new Vector(0, 0, 0);
        return new Vector(x / len, y / len, z / len);
    }
}