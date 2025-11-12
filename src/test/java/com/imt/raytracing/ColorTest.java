package com.imt.raytracing;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ColorTest {
	@Test
	void defaultColorIsBlack() {
		Color c = new Color();
		assertEquals("Color(r=0.000, g=0.000, b=0.000)", c.toString());
		assertEquals(0, c.toRGB());
	}

	@Test
	void clampWorks() {
		Color c = new Color(1.2, -0.1, 0.5).clamp();
		assertEquals("Color(r=1.000, g=0.000, b=0.500)", c.toString());
	}

	@Test
	void toRGBValues() {
		Color c = new Color(1.0, 0.5, 0.0);
		int expected = ((255 & 0xff) << 16) + ((128 & 0xff) << 8) + (0 & 0xff);
		assertEquals(expected, c.toRGB());
	}

	@Test
	void toRGBClampsAndRounds() {
		Color c = new Color(1.2, -0.2, 0.9);
		int expected = ((255 & 0xff) << 16) + ((0 & 0xff) << 8) + ((int) Math.round(0.9 * 255) & 0xff);
		assertEquals(expected, c.toRGB());
	}
}
