/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
public final class Color4IUnitTests {
	public Color4IUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddColor4IColor4I() {
		final Color4I a = new Color4I(1, 2, 3, 4);
		final Color4I b = new Color4I(2, 3, 4, 5);
		final Color4I c = Color4I.add(a, b);
		
		assertEquals(3, c.r);
		assertEquals(5, c.g);
		assertEquals(7, c.b);
		assertEquals(4, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.add(a, null));
		assertThrows(NullPointerException.class, () -> Color4I.add(null, b));
	}
	
	@Test
	public void testAddColor4IInt() {
		final Color4I a = new Color4I(1, 2, 3, 4);
		final Color4I b = Color4I.add(a, 2);
		
		assertEquals(3, b.r);
		assertEquals(4, b.g);
		assertEquals(5, b.b);
		assertEquals(4, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.add(null, 2));
	}
	
	@Test
	public void testArrayInt() {
		final Color4I[] colors = Color4I.array(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color4I color : colors) {
			assertNotNull(color);
			
			assertEquals(Color4I.TRANSPARENT, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color4I.array(-1));
	}
	
	@Test
	public void testArrayIntIntFunction() {
		final Color4I[] colors = Color4I.array(10, index -> Color4I.RED);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color4I color : colors) {
			assertNotNull(color);
			
			assertEquals(Color4I.RED, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color4I.array(-1, index -> Color4I.RED));
		
		assertThrows(NullPointerException.class, () -> Color4I.array(10, index -> null));
		assertThrows(NullPointerException.class, () -> Color4I.array(10, null));
	}
	
	@Test
	public void testAverage() {
		final Color4I color = new Color4I(0, 1, 2, 3);
		
		assertEquals(1, color.average());
	}
	
	@Test
	public void testBlendARGBIntIntDouble() {
		final int a = Color4I.toIntARGB(1, 1, 1, 1);
		final int b = Color4I.toIntARGB(5, 5, 5, 5);
		final int c = Color4I.blendARGB(a, b, 0.25D);
		
		assertEquals(2, Color4I.fromIntARGBToIntR(c));
		assertEquals(2, Color4I.fromIntARGBToIntG(c));
		assertEquals(2, Color4I.fromIntARGBToIntB(c));
		assertEquals(2, Color4I.fromIntARGBToIntA(c));
	}
	
	@Test
	public void testBlendARGBIntIntDoubleDoubleDoubleDouble() {
		final int a = Color4I.toIntARGB(1, 1, 1, 1);
		final int b = Color4I.toIntARGB(3, 3, 3, 3);
		final int c = Color4I.blendARGB(a, b, 0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(1, Color4I.fromIntARGBToIntR(c));
		assertEquals(2, Color4I.fromIntARGBToIntG(c));
		assertEquals(3, Color4I.fromIntARGBToIntB(c));
		assertEquals(5, Color4I.fromIntARGBToIntA(c));
	}
	
	@Test
	public void testBlendARGBIntIntFloat() {
		final int a = Color4I.toIntARGB(1, 1, 1, 1);
		final int b = Color4I.toIntARGB(5, 5, 5, 5);
		final int c = Color4I.blendARGB(a, b, 0.25F);
		
		assertEquals(2, Color4I.fromIntARGBToIntR(c));
		assertEquals(2, Color4I.fromIntARGBToIntG(c));
		assertEquals(2, Color4I.fromIntARGBToIntB(c));
		assertEquals(2, Color4I.fromIntARGBToIntA(c));
	}
	
	@Test
	public void testBlendARGBIntIntFloatFloatFloatFloat() {
		final int a = Color4I.toIntARGB(1, 1, 1, 1);
		final int b = Color4I.toIntARGB(3, 3, 3, 3);
		final int c = Color4I.blendARGB(a, b, 0.0F, 0.5F, 1.0F, 2.0F);
		
		assertEquals(1, Color4I.fromIntARGBToIntR(c));
		assertEquals(2, Color4I.fromIntARGBToIntG(c));
		assertEquals(3, Color4I.fromIntARGBToIntB(c));
		assertEquals(5, Color4I.fromIntARGBToIntA(c));
	}
	
	@Test
	public void testBlendARGBIntIntIntIntDoubleDouble() {
		final int a = Color4I.toIntARGB(0, 0, 0, 0);
		final int b = Color4I.toIntARGB(4, 0, 4, 0);
		final int c = Color4I.toIntARGB(0, 0, 0, 0);
		final int d = Color4I.toIntARGB(0, 4, 0, 4);
		final int e = Color4I.blendARGB(a, b, c, d, 0.5D, 0.5D);
		
		assertEquals(1, Color4I.fromIntARGBToIntR(e));
		assertEquals(1, Color4I.fromIntARGBToIntG(e));
		assertEquals(1, Color4I.fromIntARGBToIntB(e));
		assertEquals(1, Color4I.fromIntARGBToIntA(e));
	}
	
	@Test
	public void testBlendARGBIntIntIntIntFloatFloat() {
		final int a = Color4I.toIntARGB(0, 0, 0, 0);
		final int b = Color4I.toIntARGB(4, 0, 4, 0);
		final int c = Color4I.toIntARGB(0, 0, 0, 0);
		final int d = Color4I.toIntARGB(0, 4, 0, 4);
		final int e = Color4I.blendARGB(a, b, c, d, 0.5F, 0.5F);
		
		assertEquals(1, Color4I.fromIntARGBToIntR(e));
		assertEquals(1, Color4I.fromIntARGBToIntG(e));
		assertEquals(1, Color4I.fromIntARGBToIntB(e));
		assertEquals(1, Color4I.fromIntARGBToIntA(e));
	}
	
	@Test
	public void testBlendColor4IColor4I() {
		final Color4I a = new Color4I(1, 1, 1, 1);
		final Color4I b = new Color4I(3, 3, 3, 3);
		final Color4I c = Color4I.blend(a, b);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		assertEquals(2, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color4I.blend(null, b));
	}
	
	@Test
	public void testBlendColor4IColor4IColor4IColor4IDoubleDouble() {
		final Color4I a = new Color4I(0, 0, 0, 0);
		final Color4I b = new Color4I(4, 0, 4, 0);
		final Color4I c = new Color4I(0, 0, 0, 0);
		final Color4I d = new Color4I(0, 4, 0, 4);
		final Color4I e = Color4I.blend(a, b, c, d, 0.5D, 0.5D);
		
		assertEquals(1, e.r);
		assertEquals(1, e.g);
		assertEquals(1, e.b);
		assertEquals(1, e.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, b, c, null, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, b, null, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, null, c, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4I.blend(null, b, c, d, 0.5D, 0.5D));
	}
	
	@Test
	public void testBlendColor4IColor4IDouble() {
		final Color4I a = new Color4I(1, 1, 1, 1);
		final Color4I b = new Color4I(5, 5, 5, 5);
		final Color4I c = Color4I.blend(a, b, 0.25D);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		assertEquals(2, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Color4I.blend(null, b, 0.25D));
	}
	
	@Test
	public void testBlendColor4IColor4IDoubleDoubleDoubleDouble() {
		final Color4I a = new Color4I(1, 1, 1, 1);
		final Color4I b = new Color4I(3, 3, 3, 3);
		final Color4I c = Color4I.blend(a, b, 0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(1, c.r);
		assertEquals(2, c.g);
		assertEquals(3, c.b);
		assertEquals(5, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.blend(a, null, 0.0D, 0.5D, 1.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color4I.blend(null, b, 0.0D, 0.5D, 1.0D, 2.0D));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Color4I.clearCache();
		
		assertEquals(0, Color4I.getCacheSize());
		
		final Color4I a = new Color4I(0, 0, 0, 0);
		final Color4I b = new Color4I(0, 0, 0, 0);
		final Color4I c = Color4I.getCached(a);
		final Color4I d = Color4I.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Color4I.getCached(null));
		
		assertEquals(1, Color4I.getCacheSize());
		
		Color4I.clearCache();
		
		assertEquals(0, Color4I.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color4I(  0,   0,   0, 255), Color4I.BLACK);
		assertEquals(new Color4I(  0,   0, 255, 255), Color4I.BLUE);
		assertEquals(new Color4I(  0, 255, 255, 255), Color4I.CYAN);
		assertEquals(new Color4I(128, 128, 128, 255), Color4I.GRAY);
		assertEquals(new Color4I(  0, 255,   0, 255), Color4I.GREEN);
		assertEquals(new Color4I(255,   0, 255, 255), Color4I.MAGENTA);
		assertEquals(new Color4I(255,   0,   0, 255), Color4I.RED);
		assertEquals(new Color4I(  0,   0,   0,   0), Color4I.TRANSPARENT);
		assertEquals(new Color4I(255, 255, 255, 255), Color4I.WHITE);
		assertEquals(new Color4I(255, 255,   0, 255), Color4I.YELLOW);
		
		assertEquals(Color4I.toIntARGB(  0,   0,   0, 255), Color4I.BLACK_A_R_G_B);
		assertEquals(Color4I.toIntARGB(  0,   0, 255, 255), Color4I.BLUE_A_R_G_B);
		assertEquals(Color4I.toIntARGB(  0, 255, 255, 255), Color4I.CYAN_A_R_G_B);
		assertEquals(Color4I.toIntARGB(128, 128, 128, 255), Color4I.GRAY_A_R_G_B);
		assertEquals(Color4I.toIntARGB(  0, 255,   0, 255), Color4I.GREEN_A_R_G_B);
		assertEquals(Color4I.toIntARGB(255,   0, 255, 255), Color4I.MAGENTA_A_R_G_B);
		assertEquals(Color4I.toIntARGB(255,   0,   0, 255), Color4I.RED_A_R_G_B);
		assertEquals(Color4I.toIntARGB(  0,   0,   0,   0), Color4I.TRANSPARENT_A_R_G_B);
		assertEquals(Color4I.toIntARGB(255, 255, 255, 255), Color4I.WHITE_A_R_G_B);
		assertEquals(Color4I.toIntARGB(255, 255,   0, 255), Color4I.YELLOW_A_R_G_B);
	}
	
	@Test
	public void testConstructor() {
		final Color4I color = new Color4I();
		
		assertEquals(  0, color.r);
		assertEquals(  0, color.g);
		assertEquals(  0, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color4I color = new Color4I(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3DDouble() {
		final Color4I color = new Color4I(new Color3D(1.0D, 1.0D, 1.0D), 0.0D);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3D)(null), 0.0D));
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color4I color = new Color4I(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor3FFloat() {
		final Color4I color = new Color4I(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3F)(null), 0.0F));
	}
	
	@Test
	public void testConstructorColor3I() {
		final Color4I color = new Color4I(new Color3I(255, 255, 255));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3I)(null)));
	}
	
	@Test
	public void testConstructorColor3IInt() {
		final Color4I color = new Color4I(new Color3I(255, 255, 255), 0);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color3I)(null), 0));
	}
	
	@Test
	public void testConstructorColor4D() {
		final Color4I color = new Color4I(new Color4D(1.0D, 1.0D, 1.0D, 0.0D));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color4D)(null)));
	}
	
	@Test
	public void testConstructorColor4F() {
		final Color4I color = new Color4I(new Color4F(1.0F, 1.0F, 1.0F, 0.0F));
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4I((Color4F)(null)));
	}
	
	@Test
	public void testConstructorDouble() {
		final Color4I color = new Color4I(1.0D);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Color4I color = new Color4I(1.0D, 0.0D);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Color4I color = new Color4I(0.0D, 0.5D, 1.0D);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Color4I color = new Color4I(0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(510, color.a);
	}
	
	@Test
	public void testConstructorFloat() {
		final Color4I color = new Color4I(1.0F);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorFloatFloat() {
		final Color4I color = new Color4I(1.0F, 0.0F);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Color4I color = new Color4I(0.0F, 0.5F, 1.0F);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorFloatFloatFloatFloat() {
		final Color4I color = new Color4I(0.0F, 0.5F, 1.0F, 2.0F);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(510, color.a);
	}
	
	@Test
	public void testConstructorInt() {
		final Color4I color = new Color4I(255);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorIntInt() {
		final Color4I color = new Color4I(255, 0);
		
		assertEquals(255, color.r);
		assertEquals(255, color.g);
		assertEquals(255, color.b);
		assertEquals(  0, color.a);
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color4I color = new Color4I(0, 128, 255);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(255, color.a);
	}
	
	@Test
	public void testConstructorIntIntIntInt() {
		final Color4I color = new Color4I(0, 128, 255, 300);
		
		assertEquals(  0, color.r);
		assertEquals(128, color.g);
		assertEquals(255, color.b);
		assertEquals(300, color.a);
	}
	
	@Test
	public void testEqualsColor4I() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = new Color4I(0, 1, 2, 3);
		final Color4I c = new Color4I(0, 1, 2, 4);
		final Color4I d = new Color4I(0, 1, 4, 3);
		final Color4I e = new Color4I(0, 4, 2, 3);
		final Color4I f = new Color4I(4, 1, 2, 3);
		final Color4I g = null;
		
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
		assertFalse(f.equals(a));
		assertFalse(a.equals(g));
	}
	
	@Test
	public void testEqualsObject() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = new Color4I(0, 1, 2, 3);
		final Color4I c = new Color4I(0, 1, 2, 4);
		final Color4I d = new Color4I(0, 1, 4, 3);
		final Color4I e = new Color4I(0, 4, 2, 3);
		final Color4I f = new Color4I(4, 1, 2, 3);
		final Color4I g = null;
		
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
		assertNotEquals(a, g);
		assertNotEquals(g, a);
	}
	
	@Test
	public void testFromIntARGB() {
		final int colorARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4I color = Color4I.fromIntARGB(colorARGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
		assertEquals(255, color.toIntA());
	}
	
	@Test
	public void testFromIntARGBToIntA() {
		final int colorARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntARGBToIntA(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToIntB() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntARGBToIntB(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToIntG() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntARGBToIntG(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToIntR() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntARGBToIntR(colorARGB));
	}
	
	@Test
	public void testFromIntRGB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4I color = Color4I.fromIntRGB(colorRGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
		assertEquals(255, color.toIntA());
	}
	
	@Test
	public void testFromIntRGBToIntB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntRGBToIntB(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToIntG() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntRGBToIntG(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToIntR() {
		final int colorRGB = ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(255, Color4I.fromIntRGBToIntR(colorRGB));
	}
	
	@Test
	public void testGrayscaleA() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleA(a);
		
		assertEquals(3, b.r);
		assertEquals(3, b.g);
		assertEquals(3, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleA(null));
	}
	
	@Test
	public void testGrayscaleAverage() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleAverage(a);
		
		assertEquals(1, b.r);
		assertEquals(1, b.g);
		assertEquals(1, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleB() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleB(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleB(null));
	}
	
	@Test
	public void testGrayscaleG() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleG(a);
		
		assertEquals(1, b.r);
		assertEquals(1, b.g);
		assertEquals(1, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleG(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color4I a = new Color4I(1, 2, 3, 4);
		final Color4I b = Color4I.grayscaleLightness(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		assertEquals(4, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleMax() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleMax(a);
		
		assertEquals(2, b.r);
		assertEquals(2, b.g);
		assertEquals(2, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleMax(null));
	}
	
	@Test
	public void testGrayscaleMin() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleMin(a);
		
		assertEquals(0, b.r);
		assertEquals(0, b.g);
		assertEquals(0, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleMin(null));
	}
	
	@Test
	public void testGrayscaleR() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = Color4I.grayscaleR(a);
		
		assertEquals(0, b.r);
		assertEquals(0, b.g);
		assertEquals(0, b.b);
		assertEquals(3, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleR(null));
	}
	
	@Test
	public void testGrayscaleRelativeLuminance() {
		final Color4I a = new Color4I((int)(255.0D / 0.212671D), (int)(255.0D / 0.715160D), (int)(255.0D / 0.072169D), 255 * 3 + 1);
		final Color4I b = Color4I.grayscaleRelativeLuminance(a);
		
		assertEquals(255 * 3 - 1, b.r);
		assertEquals(255 * 3 - 1, b.g);
		assertEquals(255 * 3 - 1, b.b);
		assertEquals(255 * 3 + 1, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.grayscaleRelativeLuminance(null));
	}
	
	@Test
	public void testHashCode() {
		final Color4I a = new Color4I(0, 1, 2, 3);
		final Color4I b = new Color4I(0, 1, 2, 3);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testInvert() {
		final Color4I a = new Color4I(200, 200, 200, 55);
		final Color4I b = Color4I.invert(a);
		
		assertEquals(55, b.r);
		assertEquals(55, b.g);
		assertEquals(55, b.b);
		assertEquals(55, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.invert(null));
	}
	
	@Test
	public void testIsBlack() {
		final Color4I a = new Color4I(0, 0, 0);
		final Color4I b = new Color4I(0, 0, 1);
		final Color4I c = new Color4I(0, 1, 0);
		final Color4I d = new Color4I(1, 0, 0);
		final Color4I e = new Color4I(0, 1, 1);
		final Color4I f = new Color4I(1, 1, 0);
		final Color4I g = new Color4I(1, 1, 1);
		
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
		final Color4I a = new Color4I(0, 0, 255);
		final Color4I b = new Color4I(1, 1, 255);
		
		assertTrue(a.isBlue());
		
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueIntInt() {
		final Color4I a = new Color4I(  0,   0, 255);
		final Color4I b = new Color4I(127, 127, 255);
		final Color4I c = new Color4I(255, 255, 255);
		
		assertTrue(a.isBlue(128, 128));
		assertTrue(b.isBlue(128, 128));
		
		assertFalse(b.isBlue(128, 255));
		assertFalse(b.isBlue(255, 128));
		assertFalse(c.isBlue(128, 128));
	}
	
	@Test
	public void testIsCyan() {
		final Color4I a = new Color4I(  0, 255, 255);
		final Color4I b = new Color4I(  0, 127, 127);
		final Color4I c = new Color4I(255, 255, 255);
		final Color4I d = new Color4I(  0, 127, 255);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		
		assertFalse(c.isCyan());
		assertFalse(d.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color4I a = new Color4I(  0,   0,   0);
		final Color4I b = new Color4I(127, 127, 127);
		final Color4I c = new Color4I(255, 255, 255);
		final Color4I d = new Color4I(  0,   0, 127);
		final Color4I e = new Color4I(  0, 127, 127);
		final Color4I f = new Color4I(  0, 127,   0);
		final Color4I g = new Color4I(  0, 127, 255);
		
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
		final Color4I a = new Color4I(0, 255, 0);
		final Color4I b = new Color4I(1, 255, 1);
		
		assertTrue(a.isGreen());
		
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenIntInt() {
		final Color4I a = new Color4I(  0, 255,   0);
		final Color4I b = new Color4I(127, 255, 127);
		final Color4I c = new Color4I(255, 255, 255);
		
		assertTrue(a.isGreen(128, 128));
		assertTrue(b.isGreen(128, 128));
		
		assertFalse(b.isGreen(128, 255));
		assertFalse(b.isGreen(255, 128));
		assertFalse(c.isGreen(128, 128));
	}
	
	@Test
	public void testIsMagenta() {
		final Color4I a = new Color4I(255,   0, 255);
		final Color4I b = new Color4I(127,   0, 127);
		final Color4I c = new Color4I(255, 255, 255);
		final Color4I d = new Color4I(  0, 127, 255);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		
		assertFalse(c.isMagenta());
		assertFalse(d.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color4I a = new Color4I(255, 0, 0);
		final Color4I b = new Color4I(255, 1, 1);
		
		assertTrue(a.isRed());
		
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedIntInt() {
		final Color4I a = new Color4I(255,   0,   0);
		final Color4I b = new Color4I(255, 127, 127);
		final Color4I c = new Color4I(255, 255, 255);
		
		assertTrue(a.isRed(128, 128));
		assertTrue(b.isRed(128, 128));
		
		assertFalse(b.isRed(128, 255));
		assertFalse(b.isRed(255, 128));
		assertFalse(c.isRed(128, 128));
	}
	
	@Test
	public void testIsWhite() {
		final Color4I a = new Color4I(255, 255, 255);
		final Color4I b = new Color4I(255, 255,   0);
		final Color4I c = new Color4I(255,   0,   0);
		final Color4I d = new Color4I(  0,   0,   0);
		
		assertTrue(a.isWhite());
		
		assertFalse(b.isWhite());
		assertFalse(c.isWhite());
		assertFalse(d.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color4I a = new Color4I(255, 255,   0);
		final Color4I b = new Color4I(127, 127,   0);
		final Color4I c = new Color4I(255, 255, 255);
		final Color4I d = new Color4I(  0, 127, 255);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		
		assertFalse(c.isYellow());
		assertFalse(d.isYellow());
	}
	
	@Test
	public void testLightness() {
		final Color4I color = new Color4I(1, 2, 3, 4);
		
		assertEquals(2, color.lightness());
	}
	
	@Test
	public void testMax() {
		final Color4I color = new Color4I(0, 1, 2, 3);
		
		assertEquals(2, color.max());
	}
	
	@Test
	public void testMin() {
		final Color4I color = new Color4I(0, 1, 2, 3);
		
		assertEquals(0, color.min());
	}
	
	@Test
	public void testMultiplyColor4IInt() {
		final Color4I a = new Color4I(1, 2, 3, 4);
		final Color4I b = Color4I.multiply(a, 2);
		
		assertEquals(2, b.r);
		assertEquals(4, b.g);
		assertEquals(6, b.b);
		assertEquals(4, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.multiply(null, 2));
	}
	
	@Test
	public void testRandom() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.random();
			
			assertTrue(color.r >= 0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 255);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomBlue() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomBlue();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomBlueIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomBlue(100, 200);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >= 0 && color.g <= 200);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomCyan() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomCyan();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomCyanIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomCyan(9, 100);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >= 9 && color.g <= 255);
			assertTrue(color.b >= 9 && color.b <= 255);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomGrayscale() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomGrayscale();
			
			assertTrue(color.r >= 0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 255);
			
			assertEquals(color.r, color.g);
			assertEquals(color.g, color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomGreen() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomGreen();
			
			assertTrue(color.r >= 0 && color.r <=   0);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomGreenIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomGreen(100, 200);
			
			assertTrue(color.r >= 0 && color.r <= 100);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 200);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomMagenta() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomMagenta();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >  0 && color.b <= 255);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomMagentaIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomMagenta(9, 100);
			
			assertTrue(color.r >= 9 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 100);
			assertTrue(color.b >= 9 && color.b <= 255);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomRed() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomRed();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <=   0);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomRedIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomRed(100, 200);
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >= 0 && color.g <= 100);
			assertTrue(color.b >= 0 && color.b <= 200);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomYellow() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomYellow();
			
			assertTrue(color.r >  0 && color.r <= 255);
			assertTrue(color.g >  0 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <=   0);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRandomYellowIntInt() {
		for(int i = 0; i < 1000; i++) {
			final Color4I color = Color4I.randomYellow(9, 100);
			
			assertTrue(color.r >= 9 && color.r <= 255);
			assertTrue(color.g >= 9 && color.g <= 255);
			assertTrue(color.b >= 0 && color.b <= 100);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
			
			assertEquals(255, color.a);
		}
	}
	
	@Test
	public void testRead() throws IOException {
		final Color4I a = new Color4I(255, 128, 0);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeInt(a.r);
		dataOutput.writeInt(a.g);
		dataOutput.writeInt(a.b);
		dataOutput.writeInt(a.a);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color4I b = Color4I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Color4I.read(null));
		assertThrows(UncheckedIOException.class, () -> Color4I.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRelativeLuminance() {
		final Color4I color = new Color4I((int)(255.0D / 0.212671D), (int)(255.0D / 0.715160D), (int)(255.0D / 0.072169D));
		
		assertEquals(255 * 3 - 1, color.relativeLuminance());
	}
	
	@Test
	public void testSaturateColor4I() {
		final Color4I a = Color4I.saturate(new Color4I(-1, -1, -1, -1));
		final Color4I b = Color4I.saturate(new Color4I(128, 128, 128, 128));
		final Color4I c = Color4I.saturate(new Color4I(512, 512, 512, 512));
		
		assertEquals(0, a.r);
		assertEquals(0, a.g);
		assertEquals(0, a.b);
		assertEquals(0, a.a);
		
		assertEquals(128, b.r);
		assertEquals(128, b.g);
		assertEquals(128, b.b);
		assertEquals(128, b.a);
		
		assertEquals(255, c.r);
		assertEquals(255, c.g);
		assertEquals(255, c.b);
		assertEquals(255, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.saturate(null));
	}
	
	@Test
	public void testSaturateColor4IIntInt() {
		final Color4I a = Color4I.saturate(new Color4I(-10, -10, -10, -10), -5, +5);
		final Color4I b = Color4I.saturate(new Color4I(-10, -10, -10, -10), +5, -5);
		final Color4I c = Color4I.saturate(new Color4I(2, 2, 2, 2), -5, +5);
		final Color4I d = Color4I.saturate(new Color4I(2, 2, 2, 2), +5, -5);
		final Color4I e = Color4I.saturate(new Color4I(10, 10, 10, 10), -5, +5);
		final Color4I f = Color4I.saturate(new Color4I(10, 10, 10, 10), +5, -5);
		
		assertEquals(-5, a.r);
		assertEquals(-5, a.g);
		assertEquals(-5, a.b);
		assertEquals(-5, a.a);
		
		assertEquals(-5, b.r);
		assertEquals(-5, b.g);
		assertEquals(-5, b.b);
		assertEquals(-5, b.a);
		
		assertEquals(2, c.r);
		assertEquals(2, c.g);
		assertEquals(2, c.b);
		assertEquals(2, c.a);
		
		assertEquals(2, d.r);
		assertEquals(2, d.g);
		assertEquals(2, d.b);
		assertEquals(2, d.a);
		
		assertEquals(5, e.r);
		assertEquals(5, e.g);
		assertEquals(5, e.b);
		assertEquals(5, e.a);
		
		assertEquals(5, f.r);
		assertEquals(5, f.g);
		assertEquals(5, f.b);
		assertEquals(5, f.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.saturate(null, -5, +5));
	}
	
	@Test
	public void testSepia() {
		final Color4I a = new Color4I(255, 255, 255, 255);
		final Color4I b = Color4I.sepia(a);
		
		assertEquals((int)(255.0D * 1.351D), b.r);
		assertEquals((int)(255.0D * 1.203D), b.g);
		assertEquals((int)(255.0D * 0.937D), b.b);
		assertEquals((int)(255.0D * 1.000D), b.a);
		
		assertThrows(NullPointerException.class, () -> Color4I.sepia(null));
	}
	
	@Test
	public void testSepiaARGB() {
		final int a = Color4I.toIntARGB(100, 100, 100, 255);
		final int b = Color4I.sepiaARGB(a);
		
		assertEquals((int)(100.0D * 1.351D), Color4I.fromIntARGBToIntR(b));
		assertEquals((int)(100.0D * 1.203D), Color4I.fromIntARGBToIntG(b));
		assertEquals((int)(100.0D * 0.937D), Color4I.fromIntARGBToIntB(b));
		assertEquals((int)(255.0D * 1.000D), Color4I.fromIntARGBToIntA(b));
	}
	
	@Test
	public void testToIntA() {
		final Color4I a = new Color4I(0, 0, 0,   0);
		final Color4I b = new Color4I(0, 0, 0, 128);
		final Color4I c = new Color4I(0, 0, 0, 255);
		final Color4I d = new Color4I(0, 0, 0, 510);
		
		assertEquals(  0, a.toIntA());
		assertEquals(128, b.toIntA());
		assertEquals(255, c.toIntA());
		assertEquals(255, d.toIntA());
	}
	
	@Test
	public void testToIntARGB() {
		final int expectedIntARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4I color = new Color4I(0, 128, 255, 255);
		
		assertEquals(expectedIntARGB, color.toIntARGB());
	}
	
	@Test
	public void testToIntB() {
		final Color4I a = new Color4I(0, 0,   0, 0);
		final Color4I b = new Color4I(0, 0, 128, 0);
		final Color4I c = new Color4I(0, 0, 255, 0);
		final Color4I d = new Color4I(0, 0, 510, 0);
		
		assertEquals(  0, a.toIntB());
		assertEquals(128, b.toIntB());
		assertEquals(255, c.toIntB());
		assertEquals(255, d.toIntB());
	}
	
	@Test
	public void testToIntG() {
		final Color4I a = new Color4I(0,   0, 0, 0);
		final Color4I b = new Color4I(0, 128, 0, 0);
		final Color4I c = new Color4I(0, 255, 0, 0);
		final Color4I d = new Color4I(0, 510, 0, 0);
		
		assertEquals(  0, a.toIntG());
		assertEquals(128, b.toIntG());
		assertEquals(255, c.toIntG());
		assertEquals(255, d.toIntG());
	}
	
	@Test
	public void testToIntR() {
		final Color4I a = new Color4I(  0, 0, 0, 0);
		final Color4I b = new Color4I(128, 0, 0, 0);
		final Color4I c = new Color4I(255, 0, 0, 0);
		final Color4I d = new Color4I(510, 0, 0, 0);
		
		assertEquals(  0, a.toIntR());
		assertEquals(128, b.toIntR());
		assertEquals(255, c.toIntR());
		assertEquals(255, d.toIntR());
	}
	
	@Test
	public void testToIntRGB() {
		final int expectedIntRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4I color = new Color4I(0, 128, 255, 255);
		
		assertEquals(expectedIntRGB, color.toIntRGB());
	}
	
	@Test
	public void testToString() {
		final Color4I color = new Color4I(0, 127, 128, 255);
		
		assertEquals("new Color4I(0, 127, 128, 255)", color.toString());
	}
	
	@Test
	public void testWrite() {
		final Color4I a = new Color4I(255, 128, 0);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color4I b = Color4I.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}