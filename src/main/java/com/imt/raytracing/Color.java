package com.imt.raytracing;

public class Color extends AbstractVec3 {

    public Color() {
        super(0, 0, 0); // Noir par défaut
    }

    public Color(double r, double g, double b) {
        super(r, g, b);
    }

    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new Color(x, y, z);
    }

    // Clamp pour forcer les valeurs entre 0 et 1
    public Color clamp() {
        return new Color(
            Math.min(1, Math.max(0, x)),
            Math.min(1, Math.max(0, y)),
            Math.min(1, Math.max(0, z))
        );
    }

    // Conversion vers RGB (entier)
    public int toRGB() {
        int red = (int) Math.round(Math.min(1, Math.max(0, x)) * 255);
        int green = (int) Math.round(Math.min(1, Math.max(0, y)) * 255);
        int blue = (int) Math.round(Math.min(1, Math.max(0, z)) * 255);

        return ((red & 0xff) << 16)
             + ((green & 0xff) << 8)
             + (blue & 0xff);
    }

    @Override
    public String toString() {
        return String.format("Color(r=%.3f, g=%.3f, b=%.3f)", x, y, z);
    }
}
