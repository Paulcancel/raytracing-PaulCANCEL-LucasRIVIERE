package com.imt.raytracing.geometry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector {
    private double x, y, z;

    public Vector(double x, double y, double z) {
        this.x = x; this.y = y; this.z = z;
    }

    public Vector add(Vector v) {
        return new Vector(x + v.x, y + v.y, z + v.z);
    }

    public Vector sub(Vector v) {
        return new Vector(x - v.x, y - v.y, z - v.z);
    }

    public Vector mul(double k) {
        return new Vector(x * k, y * k, z * k);
    }

    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public Vector cross(Vector v) {
        return new Vector(
            y*v.z - z*v.y,
            z*v.x - x*v.z,
            x*v.y - y*v.x
        );
    }

    public double length() { return Math.sqrt(dot(this)); }

    public Vector normalize() {
        double L = length();
        return new Vector(x/L, y/L, z/L);
    }
}
