package com.imt.raytracing.imaging;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.imt.raytracing.geometry.AbstractVec3;

/**
 * Unit tests for the Color class, focusing on constructors, clamping, 
 * and conversion to standard 32-bit RGB integer format.
 */
public class ColorTest {

    private Color c1;
    private Color cHigh;
    private Color cLow;
    
    // Tolerance for floating-point comparisons
    private static final double EPSILON = AbstractVec3.EPSILON;

    @BeforeEach
    void setUp() {
        // Normal color (within [0, 1] range)
        c1 = new Color(0.5, 0.25, 1.0);
        // High intensity color (values > 1, common after lighting/HDR)
        cHigh = new Color(2.0, 0.75, 1.5);
        // Low intensity color (values < 0, mathematically possible but physically impossible)
        cLow = new Color(-0.1, 0.5, -0.9);
    }

    // --- Constructor & Inheritance Tests ---
    
    @Test
    void testDefaultConstructor() {
        Color c = new Color();
        assertEquals(0.0, c.x, EPSILON); // R
        assertEquals(0.0, c.y, EPSILON); // G
        assertEquals(0.0, c.z, EPSILON); // B
    }

    @Test
    void testInheritedArithmeticReturnsColor() {
        // Test that an inherited operation (e.g., addition) returns a Color instance
        Color result = (Color) c1.add(c1);
        assertEquals(1.0, result.x, EPSILON);
        assertTrue(result instanceof Color, "Inherited operations must return a Color instance.");
    }
    
    // --- Clamping Tests ---

    @Test
    void testClamp_NormalColor() {
        // Color is already within [0, 1], so it should remain unchanged.
        Color result = c1.clamp();
        assertEquals(0.5, result.x, EPSILON);
        assertEquals(0.25, result.y, EPSILON);
        assertEquals(1.0, result.z, EPSILON);
    }

    @Test
    void testClamp_HighIntensityColor() {
        // (2.0, 0.75, 1.5) -> Clamped to (1.0, 0.75, 1.0)
        Color result = cHigh.clamp();
        assertEquals(1.0, result.x, EPSILON);
        assertEquals(0.75, result.y, EPSILON);
        assertEquals(1.0, result.z, EPSILON);
    }

    @Test
    void testClamp_LowIntensityColor() {
        // (-0.1, 0.5, -0.9) -> Clamped to (0.0, 0.5, 0.0)
        Color result = cLow.clamp();
        assertEquals(0.0, result.x, EPSILON);
        assertEquals(0.5, result.y, EPSILON);
        assertEquals(0.0, result.z, EPSILON);
    }

    // --- toRGB Conversion Tests ---

    @Test
    void testToRGB_NormalColor() {
        // c1 = (0.5, 0.25, 1.0)
        // Red: 0.5 * 255 = 127.5 -> round(127.5) = 128 (0x80)
        // Green: 0.25 * 255 = 63.75 -> round(63.75) = 64 (0x40)
        // Blue: 1.0 * 255 = 255 -> round(255) = 255 (0xFF)
        // Expected RGB: 0x008040FF (ARGB)
        int expectedRGB = 0x008040FF;
        assertEquals(expectedRGB, c1.toRGB(), "toRGB should convert floating point to 8-bit integer RGB components.");
    }

    @Test
    void testToRGB_HighIntensityColor_ClampingCheck() {
        // cHigh = (2.0, 0.75, 1.5)
        // Red: 2.0 -> clamped to 1.0 * 255 = 255 (0xFF)
        // Green: 0.75 * 255 = 191.25 -> round(191.25) = 191 (0xBF)
        // Blue: 1.5 -> clamped to 1.0 * 255 = 255 (0xFF)
        // Expected RGB: 0xFFBF FF
        int expectedRGB = 0x00FFBFFF;
        assertEquals(expectedRGB, cHigh.toRGB(), "toRGB must clamp values above 1.0 to 255.");
    }

    @Test
    void testToRGB_LowIntensityColor_ClampingCheck() {
        // cLow = (-0.1, 0.5, -0.9)
        // Red: -0.1 -> clamped to 0.0 * 255 = 0 (0x00)
        // Green: 0.5 * 255 = 127.5 -> round(127.5) = 128 (0x80)
        // Blue: -0.9 -> clamped to 0.0 * 255 = 0 (0x00)
        // Expected RGB: 0x008000
        int expectedRGB = 0x00008000;
        assertEquals(expectedRGB, cLow.toRGB(), "toRGB must clamp values below 0.0 to 0.");
    }
    
    @Test
    void testToRGB_RoundingBoundary() {
        // Test rounding up (0.5/255)
        // 0.5 / 255 ≈ 0.00196...
        Color c_boundary = new Color(0.5 / 255.0, 0.5 / 255.0, 0.5 / 255.0);
        // 0.5 * 255 = 0.5. Math.round(0.5) = 1
        
        int expectedRGB = 0x00010101;
        assertEquals(expectedRGB, c_boundary.toRGB(), "Rounding half-up (0.5) should work correctly.");
    }
}