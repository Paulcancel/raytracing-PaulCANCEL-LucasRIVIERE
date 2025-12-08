package com.imt.raytracing.imaging;

import com.imt.raytracing.geometry.AbstractVec3;

/**
 * Represents an RGB color, typically with components (R, G, B) stored as 
 * double-precision floating-point values.
 * Colors in ray tracing often exceed the standard [0, 1] range before final clamping 
 * due to lighting calculations.
 */
public class Color extends AbstractVec3 {

    /**
     * Constructs a default black Color (0, 0, 0).
     */
    public Color() {
        super(0, 0, 0); // Default is black
    }

    /**
     * Constructs a Color with specified R, G, B components.
     * @param r The Red component (usually between 0.0 and 1.0, but can be higher).
     * @param g The Green component.
     * @param b The Blue component.
     */
    public Color(double r, double g, double b) {
        super(r, g, b);
    }

    /**
     * Utility method used by parent class operations to create a new instance 
     * of the correct type (Color) after an operation.
     */
    @Override
    protected AbstractVec3 create(double x, double y, double z) {
        return new Color(x, y, z);
    }

    /**
     * Clamps the color components to force their values to be within the standard [0, 1] range.
     * This is typically done before final output to a display format.
     * @return A new Color with components clamped between 0 and 1.
     */
    public Color clamp() {
        return new Color(
            Math.min(1, Math.max(0, x)),
            Math.min(1, Math.max(0, y)),
            Math.min(1, Math.max(0, z))
        );
    }

    /**
     * Converts the floating-point Color components (assumed to be in [0, 1]) 
     * to a single 32-bit integer representing standard 8-bit RGB (0-255).
     * The method performs rounding and clamping before scaling to 255.
     * @return The integer representation of the color (AARRGGBB format, where AA is 00).
     */
    public int toRGB() {
        // Clamp and scale the double components to 0-255 integers
        int red = (int) Math.round(Math.min(1, Math.max(0, x)) * 255);
        int green = (int) Math.round(Math.min(1, Math.max(0, y)) * 255);
        int blue = (int) Math.round(Math.min(1, Math.max(0, z)) * 255);

        // Combine the 8-bit components into a single 32-bit integer (AARRGGBB, where AA is 00)
        return ((red & 0xff) << 16)
             + ((green & 0xff) << 8)
             + (blue & 0xff);
    }

    /**
     * Returns a formatted string representation of the Color's components.
     * @return A string showing the R, G, and B components with 3 decimal places.
     */
    @Override
    public String toString() {
        return String.format("Color(r=%.3f, g=%.3f, b=%.3f)", x, y, z);
    }

    public boolean isBlack() {
        return x == 0 && y == 0 && z == 0;
    }

    public void addLocal(Color c) {
        this.x += c.x;
        this.y += c.y;
        this.z += c.z;
    }
}