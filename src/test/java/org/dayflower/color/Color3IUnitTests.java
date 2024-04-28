/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
 * 
 * This file is part of Dayflower.
 * 
 * Dayflower is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Dayflower is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Dayflower. If not, see <http://www.gnu.org/licenses/>.
 */
package org.dayflower.color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import org.dayflower.mock.DataOutputMock;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class Color3IUnitTests {
	public Color3IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddColor3IColor3I() {
		final Color3I a = new Color3I(1, 2, 3);
		final Color3I b = new Color3I(2, 3, 4);
		final Color3I c = Color3I.add(a, b);
		
		assertEquals(3, c.r);
		assertEquals(5, c.g);
		assertEquals(7, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.add(a, null));
		assertThrows(NullPointerException.class, () -> Color3I.add(null, b));
	}
	
	@Test
	public void testAddColor3IInt() {
		final Color3I a = new Color3I(1, 2, 3);
		final Color3I b = Color3I.add(a, 2);
		
		assertEquals(3, b.r);
		assertEquals(4, b.g);
		assertEquals(5, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.add(null, 2));
	}
	
	@Test
	public void testArrayInt() {
		final Color3I[] colors = Color3I.array(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color3I color : colors) {
			assertNotNull(color);
			
			assertEquals(Color3I.BLACK, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3I.array(-1));
	}
	
	@Test
	public void testArrayIntIntFunction() {
		final Color3I[] colors = Color3I.array(10, index -> Color3I.RED);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color3I color : colors) {
			assertNotNull(color);
			
			assertEquals(Color3I.RED, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3I.array(-1, index -> Color3I.RED));
		
		assertThrows(NullPointerException.class, () -> Color3I.array(10, index -> null));
		assertThrows(NullPointerException.class, () -> Color3I.array(10, null));
	}
	
	@Test
	public void testAverage() {
		final Color3I color = new Color3I(0, 1, 2);
		
		assertEquals(1, color.average());
	}
	
	@Test
	public void testBlendColor3IColor3I() {
		final Color3I a = new Color3I(1, 1, 1);
		final Color3I b = new Color3I(3, 3, 3);
		final Color3I c = Color3I.blend(a, b);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color3I.blend(null, b));
	}
	
	@Test
	public void testBlendColor3IColor3IColor3IColor3IDoubleDouble() {
		final Color3I a = new Color3I(0, 0, 0);
		final Color3I b = new Color3I(4, 0, 0);
		final Color3I c = new Color3I(0, 0, 0);
		final Color3I d = new Color3I(0, 4, 0);
		final Color3I e = Color3I.blend(a, b, c, d, 0.5D, 0.5D);
		
		assertEquals(1, e.r);
		assertEquals(1, e.g);
		assertEquals(0, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, b, c, null, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, b, null, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, null, c, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color3I.blend(null, b, c, d, 0.5D, 0.5D));
	}
	
	@Test
	public void testBlendColor3IColor3IDouble() {
		final Color3I a = new Color3I(1, 1, 1);
		final Color3I b = new Color3I(5, 5, 5);
		final Color3I c = Color3I.blend(a, b, 0.25D);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Color3I.blend(null, b, 0.25D));
	}
	
	@Test
	public void testBlendColor3IColor3IDoubleDoubleDouble() {
		final Color3I a = new Color3I(1, 1, 1);
		final Color3I b = new Color3I(3, 3, 3);
		final Color3I c = Color3I.blend(a, b, 0.0D, 0.5D, 1.0D);
		
		assertEquals(1, c.r);
		assertEquals(2, c.g);
		assertEquals(3, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.blend(a, null, 0.0D, 0.5D, 1.0D));
		assertThrows(NullPointerException.class, () -> Color3I.blend(null, b, 0.0D, 0.5D, 1.0D));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Color3I.clearCache();
		
		assertEquals(0, Color3I.getCacheSize());
		
		final Color3I a = new Color3I(0, 0, 0);
		final Color3I b = new Color3I(0, 0, 0);
		final Color3I c = Color3I.getCached(a);
		final Color3I d = Color3I.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Color3I.getCached(null));
		
		assertEquals(1, Color3I.getCacheSize());
		
		Color3I.clearCache();
		
		assertEquals(0, Color3I.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color3I(  0,   0,   0), Color3I.BLACK);
		assertEquals(new Color3I(  0,   0, 255), Color3I.BLUE);
		assertEquals(new Color3I(  0, 255, 255), Color3I.CYAN);
		assertEquals(new Color3I(128, 128, 128), Color3I.GRAY);
		assertEquals(new Color3I(  0, 255,   0), Color3I.GREEN);
		assertEquals(new Color3I(255,   0, 255), Color3I.MAGENTA);
		assertEquals(new Color3I(255,   0,   0), Color3I.RED);
		assertEquals(new Color3I(255, 255, 255), Color3I.WHITE);
		assertEquals(new Color3I(255, 255,   0), Color3I.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color3I color = new Color3I();
		
		assertEquals(0, color.r);
		assertEquals(0, color.g);
		assertEquals(0, color.b);
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color3I color = new Color3I(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3I((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color3I color = new Color3I(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3I((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor4D() {
		final Color3I color = new Color3I(new Color4D(1.0D, 1.0D, 1.0D));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3I((Color4D)(null)));
	}
	
	@Test
	public void testConstructorColor4F() {
		final Color3I color = new Color3I(new Color4F(1.0F, 1.0F, 1.0F));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3I((Color4F)(null)));
	}
	
	@Test
	public void testConstructorColor4I() {
		final Color3I color = new Color3I(new Color4I(255, 255, 255));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3I((Color4I)(null)));
	}
	
	@Test
	public void testConstructorDouble() {
		final Color3I color = new Color3I(1.0D);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Color3I color = new Color3I(0.0D, 0.5D, 1.0D);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testConstructorFloat() {
		final Color3I color = new Color3I(1.0F);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Color3I color = new Color3I(0.0F, 0.5F, 1.0F);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testConstructorInt() {
		final Color3I color = new Color3I(255);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color3I color = new Color3I(0, 128, 255);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
	}
	
	@Test
	public void testEqualsColor3I() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = new Color3I(0, 1, 2);
		final Color3I c = new Color3I(0, 1, 3);
		final Color3I d = new Color3I(0, 3, 2);
		final Color3I e = new Color3I(3, 1, 2);
		final Color3I f = null;
		
		assertTrue(a.equals(a));
		assertTrue(a.equals(b));
		assertTrue(b.equals(a));
		
		assertFalse(a.equals(c));
		assertFalse(c.equals(a));
		assertFalse(a.equals(d));
		assertFalse(d.equals(a));
		assertFalse(a.equals(e));
		assertFalse(e.equals(a));
		assertFalse(a.equals(f));
	}
	
	@Test
	public void testEqualsObject() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = new Color3I(0, 1, 2);
		final Color3I c = new Color3I(0, 1, 3);
		final Color3I d = new Color3I(0, 3, 2);
		final Color3I e = new Color3I(3, 1, 2);
		final Color3I f = null;
		
		assertEquals(a, a);
		assertEquals(a, b);
		assertEquals(b, a);
		
		assertNotEquals(a, c);
		assertNotEquals(c, a);
		assertNotEquals(a, d);
		assertNotEquals(d, a);
		assertNotEquals(a, e);
		assertNotEquals(e, a);
		assertNotEquals(a, f);
		assertNotEquals(f, a);
	}
	
	@Test
	public void testFromIntARGB() {
		final int colorARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3I color = Color3I.fromIntARGB(colorARGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
	}
	
	@Test
	public void testFromIntARGBToIntB() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntARGBToIntB(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToIntG() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntARGBToIntG(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToIntR() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntARGBToIntR(colorARGB));
	}
	
	@Test
	public void testFromIntRGB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3I color = Color3I.fromIntRGB(colorRGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
	}
	
	@Test
	public void testFromIntRGBToIntB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntRGBToIntB(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToIntG() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntRGBToIntG(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToIntR() {
		final int colorRGB = ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color3I.fromIntRGBToIntR(colorRGB));
	}
	
	@Test
	public void testGrayscaleAverage() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleAverage(a);
		
		assertEquals(1, b.r);
		assertEquals(1, b.g);
		assertEquals(1, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleB() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleB(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleB(null));
	}
	
	@Test
	public void testGrayscaleG() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleG(a);
		
		assertEquals(1, b.r);
		assertEquals(1, b.g);
		assertEquals(1, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleG(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color3I a = new Color3I(1, 2, 3);
		final Color3I b = Color3I.grayscaleLightness(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleMax() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleMax(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleMax(null));
	}
	
	@Test
	public void testGrayscaleMin() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleMin(a);
		
		assertEquals(0, b.r);
		assertEquals(0, b.g);
		assertEquals(0, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleMin(null));
	}
	
	@Test
	public void testGrayscaleR() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = Color3I.grayscaleR(a);
		
		assertEquals(0, b.r);
		assertEquals(0, b.g);
		assertEquals(0, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleR(null));
	}
	
	@Test
	public void testGrayscaleRelativeLuminance() {
		final Color3I a = new Color3I((int)(255.0D / 0.212671D), (int)(255.0D / 0.715160D), (int)(255.0D / 0.072169D));
		final Color3I b = Color3I.grayscaleRelativeLuminance(a);
		
		assertEquals(255 * 3 - 1, b.r);
		assertEquals(255 * 3 - 1, b.g);
		assertEquals(255 * 3 - 1, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.grayscaleRelativeLuminance(null));
	}
	
	@Test
	public void testHashCode() {
		final Color3I a = new Color3I(0, 1, 2);
		final Color3I b = new Color3I(0, 1, 2);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testInvert() {
		final Color3I a = new Color3I(200, 200, 200);
		final Color3I b = Color3I.invert(a);
		
		assertEquals(55, b.r);
		assertEquals(55, b.g);
		assertEquals(55, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.invert(null));
	}
	
	@Test
	public void testIsBlack() {
		final Color3I a = new Color3I(0, 0, 0);
		final Color3I b = new Color3I(0, 0, 1);
		final Color3I c = new Color3I(0, 1, 0);
		final Color3I d = new Color3I(1, 0, 0);
		final Color3I e = new Color3I(0, 1, 1);
		final Color3I f = new Color3I(1, 1, 0);
		final Color3I g = new Color3I(1, 1, 1);
		
		assertTrue(a.isBlack());
		
		assertFalse(b.isBlack());
		assertFalse(c.isBlack());
		assertFalse(d.isBlack());
		assertFalse(e.isBlack());
		assertFalse(f.isBlack());
		assertFalse(g.isBlack());
	}
	
	@Test
	public void testIsBlue() {
		final Color3I a = new Color3I(0, 0, 255);
		final Color3I b = new Color3I(1, 1, 255);
		
		assertTrue(a.isBlue());
		
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueIntInt() {
		final Color3I a = new Color3I(  0,   0, 255);
		final Color3I b = new Color3I(127, 127, 255);
		final Color3I c = new Color3I(255, 255, 255);
		
		assertTrue(a.isBlue(128, 128));
		assertTrue(b.isBlue(128, 128));
		
		assertFalse(b.isBlue(128, 255));
		assertFalse(b.isBlue(255, 128));
		assertFalse(c.isBlue(128, 128));
	}
	
	@Test
	public void testIsCyan() {
		final Color3I a = new Color3I(  0, 255, 255);
		final Color3I b = new Color3I(  0, 127, 127);
		final Color3I c = new Color3I(255, 255, 255);
		final Color3I d = new Color3I(  0, 127, 255);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		
		assertFalse(c.isCyan());
		assertFalse(d.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color3I a = new Color3I(  0,   0,   0);
		final Color3I b = new Color3I(127, 127, 127);
		final Color3I c = new Color3I(255, 255, 255);
		final Color3I d = new Color3I(  0,   0, 127);
		final Color3I e = new Color3I(  0, 127, 127);
		final Color3I f = new Color3I(  0, 127,   0);
		final Color3I g = new Color3I(  0, 127, 255);
		
		assertTrue(a.isGrayscale());
		assertTrue(b.isGrayscale());
		assertTrue(c.isGrayscale());
		
		assertFalse(d.isGrayscale());
		assertFalse(e.isGrayscale());
		assertFalse(f.isGrayscale());
		assertFalse(g.isGrayscale());
	}
	
	@Test
	public void testIsGreen() {
		final Color3I a = new Color3I(0, 255, 0);
		final Color3I b = new Color3I(1, 255, 1);
		
		assertTrue(a.isGreen());
		
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenIntInt() {
		final Color3I a = new Color3I(  0, 255,   0);
		final Color3I b = new Color3I(127, 255, 127);
		final Color3I c = new Color3I(255, 255, 255);
		
		assertTrue(a.isGreen(128, 128));
		assertTrue(b.isGreen(128, 128));
		
		assertFalse(b.isGreen(128, 255));
		assertFalse(b.isGreen(255, 128));
		assertFalse(c.isGreen(128, 128));
	}
	
	@Test
	public void testIsMagenta() {
		final Color3I a = new Color3I(255,   0, 255);
		final Color3I b = new Color3I(127,   0, 127);
		final Color3I c = new Color3I(255, 255, 255);
		final Color3I d = new Color3I(  0, 127, 255);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		
		assertFalse(c.isMagenta());
		assertFalse(d.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color3I a = new Color3I(255, 0, 0);
		final Color3I b = new Color3I(255, 1, 1);
		
		assertTrue(a.isRed());
		
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedIntInt() {
		final Color3I a = new Color3I(255,   0,   0);
		final Color3I b = new Color3I(255, 127, 127);
		final Color3I c = new Color3I(255, 255, 255);
		
		assertTrue(a.isRed(128, 128));
		assertTrue(b.isRed(128, 128));
		
		assertFalse(b.isRed(128, 255));
		assertFalse(b.isRed(255, 128));
		assertFalse(c.isRed(128, 128));
	}
	
	@Test
	public void testIsWhite() {
		final Color3I a = new Color3I(255, 255, 255);
		final Color3I b = new Color3I(255, 255,   0);
		final Color3I c = new Color3I(255,   0,   0);
		final Color3I d = new Color3I(  0,   0,   0);
		
		assertTrue(a.isWhite());
		
		assertFalse(b.isWhite());
		assertFalse(c.isWhite());
		assertFalse(d.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color3I a = new Color3I(255, 255,   0);
		final Color3I b = new Color3I(127, 127,   0);
		final Color3I c = new Color3I(255, 255, 255);
		final Color3I d = new Color3I(  0, 127, 255);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		
		assertFalse(c.isYellow());
		assertFalse(d.isYellow());
	}
	
	@Test
	public void testLightness() {
		final Color3I color = new Color3I(1, 2, 3);
		
		assertEquals(2, color.lightness());
	}
	
	@Test
	public void testMax() {
		final Color3I color = new Color3I(0, 1, 2);
		
		assertEquals(2, color.max());
	}
	
	@Test
	public void testMin() {
		final Color3I color = new Color3I(0, 1, 2);
		
		assertEquals(0, color.min());
	}
	
	@Test
	public void testMultiplyColor3IInt() {
		final Color3I a = new Color3I(1, 2, 3);
		final Color3I b = Color3I.multiply(a, 2);
		
		assertEquals(2, b.r);
		assertEquals(4, b.g);
		assertEquals(6, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.multiply(null, 2));
	}
	
	@Test
	public void testRandom() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.random();
			
			assertTrue(color.r >= 0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 255);
		}
	}
	
	@Test
	public void testRandomBlue() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomBlue();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
		}
	}
	
	@Test
	public void testRandomBlueIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomBlue(100, 200);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >= 0 && color.g <= 200);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
		}
	}
	
	@Test
	public void testRandomCyan() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomCyan();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomCyanIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomCyan(9, 100);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >= 9 && color.g <= 255);
			assertTrue(color.b >= 9 && color.b <= 255);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomGrayscale() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomGrayscale();
			
			assertTrue(color.r >= 0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 255);
			
			assertEquals(color.r, color.g);
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomGreen() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomGreen();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
		}
	}
	
	@Test
	public void testRandomGreenIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomGreen(100, 200);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 200);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
		}
	}
	
	@Test
	public void testRandomMagenta() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomMagenta();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
		}
	}
	
	@Test
	public void testRandomMagentaIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomMagenta(9, 100);
			
			assertTrue(color.r >= 9 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 100);
			assertTrue(color.b >= 9 && color.b <= 255);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
		}
	}
	
	@Test
	public void testRandomRed() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomRed();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
		}
	}
	
	@Test
	public void testRandomRedIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomRed(100, 200);
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 100);
			assertTrue(color.b >= 0 && color.b <= 200);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
		}
	}
	
	@Test
	public void testRandomYellow() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomYellow();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
		}
	}
	
	@Test
	public void testRandomYellowIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color3I color = Color3I.randomYellow(9, 100);
			
			assertTrue(color.r >= 9 && color.r <= 255);
			assertTrue(color.g >= 9 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 100);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
		}
	}
	
	@Test
	public void testRead() throws IOException {
		final Color3I a = new Color3I(255, 128, 0);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeInt(a.r);
		dataOutput.writeInt(a.g);
		dataOutput.writeInt(a.b);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3I b = Color3I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Color3I.read(null));
		assertThrows(UncheckedIOException.class, () -> Color3I.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRelativeLuminance() {
		final Color3I color = new Color3I((int)(255.0D / 0.212671D), (int)(255.0D / 0.715160D), (int)(255.0D / 0.072169D));
		
		assertEquals(255 * 3 - 1, color.relativeLuminance());
	}
	
	@Test
	public void testSaturateColor3I() {
		final Color3I a = Color3I.saturate(new Color3I(-1, -1, -1));
		final Color3I b = Color3I.saturate(new Color3I(128, 128, 128));
		final Color3I c = Color3I.saturate(new Color3I(512, 512, 512));
		
		assertEquals(0, a.r);
		assertEquals(0, a.g);
		assertEquals(0, a.b);
		
		assertEquals(128, b.r);
		assertEquals(128, b.g);
		assertEquals(128, b.b);
		
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.saturate(null));
	}
	
	@Test
	public void testSaturateColor3IIntInt() {
		final Color3I a = Color3I.saturate(new Color3I(-10, -10, -10), -5, +5);
		final Color3I b = Color3I.saturate(new Color3I(-10, -10, -10), +5, -5);
		final Color3I c = Color3I.saturate(new Color3I(2, 2, 2), -5, +5);
		final Color3I d = Color3I.saturate(new Color3I(2, 2, 2), +5, -5);
		final Color3I e = Color3I.saturate(new Color3I(10, 10, 10), -5, +5);
		final Color3I f = Color3I.saturate(new Color3I(10, 10, 10), +5, -5);
		
		assertEquals(-5, a.r);
		assertEquals(-5, a.g);
		assertEquals(-5, a.b);
		
		assertEquals(-5, b.r);
		assertEquals(-5, b.g);
		assertEquals(-5, b.b);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		
		assertEquals(2, d.r);
		assertEquals(2, d.g);
		assertEquals(2, d.b);
		
		assertEquals(5, e.r);
		assertEquals(5, e.g);
		assertEquals(5, e.b);
		
		assertEquals(5, f.r);
		assertEquals(5, f.g);
		assertEquals(5, f.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.saturate(null, -5, +5));
	}
	
	@Test
	public void testSepia() {
		final Color3I a = new Color3I(255, 255, 255);
		final Color3I b = Color3I.sepia(a);
		
		assertEquals((int)(255.0D * 1.351D), b.r);
		assertEquals((int)(255.0D * 1.203D), b.g);
		assertEquals((int)(255.0D * 0.937D), b.b);
		
		assertThrows(NullPointerException.class, () -> Color3I.sepia(null));
	}
	
	@Test
	public void testToIntARGB() {
		final int expectedIntARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3I color = new Color3I(0, 128, 255);
		
		assertEquals(expectedIntARGB, color.toIntARGB());
	}
	
	@Test
	public void testToIntB() {
		final Color3I a = new Color3I(0, 0,   0);
		final Color3I b = new Color3I(0, 0, 128);
		final Color3I c = new Color3I(0, 0, 255);
		final Color3I d = new Color3I(0, 0, 510);
		
		assertEquals(  0, a.toIntB());
		assertEquals(128, b.toIntB());
		assertEquals(255, c.toIntB());
		assertEquals(255, d.toIntB());
	}
	
	@Test
	public void testToIntG() {
		final Color3I a = new Color3I(0,   0, 0);
		final Color3I b = new Color3I(0, 128, 0);
		final Color3I c = new Color3I(0, 255, 0);
		final Color3I d = new Color3I(0, 510, 0);
		
		assertEquals(  0, a.toIntG());
		assertEquals(128, b.toIntG());
		assertEquals(255, c.toIntG());
		assertEquals(255, d.toIntG());
	}
	
	@Test
	public void testToIntR() {
		final Color3I a = new Color3I(  0, 0, 0);
		final Color3I b = new Color3I(128, 0, 0);
		final Color3I c = new Color3I(255, 0, 0);
		final Color3I d = new Color3I(510, 0, 0);
		
		assertEquals(  0, a.toIntR());
		assertEquals(128, b.toIntR());
		assertEquals(255, c.toIntR());
		assertEquals(255, d.toIntR());
	}
	
	@Test
	public void testToIntRGB() {
		final int expectedIntRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3I color = new Color3I(0, 128, 255);
		
		assertEquals(expectedIntRGB, color.toIntRGB());
	}
	
	@Test
	public void testToString() {
		final Color3I color = new Color3I(0, 128, 255);
		
		assertEquals("new Color3I(0, 128, 255)", color.toString());
	}
	
	@Test
	public void testWrite() {
		final Color3I a = new Color3I(255, 128, 0);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3I b = Color3I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}