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
public final class Color4DUnitTests {
	public Color4DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddColor4DColor4D() {
		final Color4D a = new Color4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Color4D b = new Color4D(2.0D, 3.0D, 4.0D, 5.0D);
		final Color4D c = Color4D.add(a, b);
		
		assertEquals(3.0D, c.r);
		assertEquals(5.0D, c.g);
		assertEquals(7.0D, c.b);
		assertEquals(4.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.add(a, null));
		assertThrows(NullPointerException.class, () -> Color4D.add(null, b));
	}
	
	@Test
	public void testAddColor4DDouble() {
		final Color4D a = new Color4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Color4D b = Color4D.add(a, 2.0D);
		
		assertEquals(3.0D, b.r);
		assertEquals(4.0D, b.g);
		assertEquals(5.0D, b.b);
		assertEquals(4.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.add(null, 2.0D));
	}
	
	@Test
	public void testArrayInt() {
		final Color4D[] colors = Color4D.array(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color4D color : colors) {
			assertNotNull(color);
			
			assertEquals(Color4D.TRANSPARENT, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color4D.array(-1));
	}
	
	@Test
	public void testArrayIntIntFunction() {
		final Color4D[] colors = Color4D.array(10, index -> Color4D.RED);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color4D color : colors) {
			assertNotNull(color);
			
			assertEquals(Color4D.RED, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color4D.array(-1, index -> Color4D.RED));
		
		assertThrows(NullPointerException.class, () -> Color4D.array(10, index -> null));
		assertThrows(NullPointerException.class, () -> Color4D.array(10, null));
	}
	
	@Test
	public void testArrayRandom() {
		final Color4D[] colors = Color4D.arrayRandom(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color4D color : colors) {
			assertNotNull(color);
			
			assertTrue(color.r >= 0.0D && color.r <= 1.0D);
			assertTrue(color.g >= 0.0D && color.g <= 1.0D);
			assertTrue(color.b >= 0.0D && color.b <= 1.0D);
			
			assertEquals(1.0D, color.a);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color4D.arrayRandom(-1));
	}
	
	@Test
	public void testArrayReadByteArray() {
		final byte[] array = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		final Color4D[] colors = Color4D.arrayRead(array);
		
		assertNotNull(colors);
		
		assertEquals(4, colors.length);
		
		final Color4D a = colors[0];
		final Color4D b = colors[1];
		final Color4D c = colors[2];
		final Color4D d = colors[3];
		
		assertNotNull(a);
		
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		assertEquals(0.0D, a.a);
		
		assertNotNull(b);
		
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(0.0D, b.a);
		
		assertNotNull(c);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		assertEquals(0.0D, c.a);
		
		assertNotNull(d);
		
		assertEquals(0.0D, d.r);
		assertEquals(0.0D, d.g);
		assertEquals(0.0D, d.b);
		assertEquals(1.0D, d.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead((byte[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color4D.arrayRead(new byte[] {(byte)(255), (byte)(255)}));
	}
	
	@Test
	public void testArrayReadByteArrayArrayComponentOrder() {
		final byte[][] arrays = {
			{(byte)(  0), (byte)(255), (byte)(  0), (byte)(0), (byte)(  0), (byte)(  0), (byte)(255), (byte)(0), (byte)(  0), (byte)(0), (byte)(  0), (byte)(255)},
			{(byte)(  0), (byte)(  0), (byte)(255), (byte)(0), (byte)(255), (byte)(  0), (byte)(255), (byte)(0), (byte)(  0)},
			{(byte)(  0), (byte)(  0), (byte)(255), (byte)(0), (byte)(  0), (byte)(255), (byte)(  0), (byte)(0), (byte)(255), (byte)(0), (byte)(  0), (byte)(  0)},
			{(byte)(255), (byte)(  0), (byte)(  0), (byte)(0), (byte)(255), (byte)(  0), (byte)(  0), (byte)(0), (byte)(255)},
			{(byte)(255), (byte)(  0), (byte)(  0), (byte)(0), (byte)(  0), (byte)(255), (byte)(  0), (byte)(0), (byte)(  0), (byte)(0), (byte)(255), (byte)(  0)}
		};
		
		final ArrayComponentOrder[] arrayComponentOrders = ArrayComponentOrder.values();
		
		for(int i = 0; i < arrays.length; i++) {
			final Color4D[] colors = Color4D.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color4D a = colors[0];
			final Color4D b = colors[1];
			final Color4D c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			assertEquals(0.0D, a.a);
			
			assertNotNull(b);
			
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			assertEquals(0.0D, b.a);
			
			assertNotNull(c);
			
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
			assertEquals(0.0D, c.a);
		}
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead((byte[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead(new byte[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color4D.arrayRead(new byte[] {(byte)(255), (byte)(255)}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayReadIntArray() {
		final int[] array = {255, 0, 0, 0, 0, 255, 0, 0, 0, 0, 255, 0, 0, 0, 0, 255};
		
		final Color4D[] colors = Color4D.arrayRead(array);
		
		assertNotNull(colors);
		
		assertEquals(4, colors.length);
		
		final Color4D a = colors[0];
		final Color4D b = colors[1];
		final Color4D c = colors[2];
		final Color4D d = colors[3];
		
		assertNotNull(a);
		
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		assertEquals(0.0D, a.a);
		
		assertNotNull(b);
		
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(0.0D, b.a);
		
		assertNotNull(c);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		assertEquals(0.0D, c.a);
		
		assertNotNull(d);
		
		assertEquals(0.0D, d.r);
		assertEquals(0.0D, d.g);
		assertEquals(0.0D, d.b);
		assertEquals(1.0D, d.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead((int[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color4D.arrayRead(new int[] {255, 255}));
	}
	
	@Test
	public void testArrayReadIntArrayArrayComponentOrder() {
		final int[][] arrays = {
			{  0, 255,   0, 0,   0,   0, 255, 0,   0, 0,   0, 255},
			{  0,   0, 255, 0, 255,   0, 255, 0,   0},
			{  0,   0, 255, 0,   0, 255,   0, 0, 255, 0,   0,   0},
			{255,   0,   0, 0, 255,   0,   0, 0, 255},
			{255,   0,   0, 0,   0, 255,   0, 0,   0, 0, 255,   0}
		};
		
		final ArrayComponentOrder[] arrayComponentOrders = ArrayComponentOrder.values();
		
		for(int i = 0; i < arrays.length; i++) {
			final Color4D[] colors = Color4D.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color4D a = colors[0];
			final Color4D b = colors[1];
			final Color4D c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			assertEquals(0.0D, a.a);
			
			assertNotNull(b);
			
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			assertEquals(0.0D, b.a);
			
			assertNotNull(c);
			
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
			assertEquals(0.0D, c.a);
		}
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead((int[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color4D.arrayRead(new int[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color4D.arrayRead(new int[] {255, 255}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayUnpackIntArray() {
		final int[] array = {
			PackedIntComponentOrder.ARGB.pack(255,   0,   0,   0),
			PackedIntComponentOrder.ARGB.pack(  0, 255,   0,   0),
			PackedIntComponentOrder.ARGB.pack(  0,   0, 255,   0),
			PackedIntComponentOrder.ARGB.pack(  0,   0,   0, 255)
		};
		
		final Color4D[] colors = Color4D.arrayUnpack(array);
		
		assertNotNull(colors);
		
		assertEquals(4, colors.length);
		
		final Color4D a = colors[0];
		final Color4D b = colors[1];
		final Color4D c = colors[2];
		final Color4D d = colors[3];
		
		assertNotNull(a);
		
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		assertEquals(0.0D, a.a);
		
		assertNotNull(b);
		
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(0.0D, b.a);
		
		assertNotNull(c);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		assertEquals(0.0D, c.a);
		
		assertNotNull(d);
		
		assertEquals(0.0D, d.r);
		assertEquals(0.0D, d.g);
		assertEquals(0.0D, d.b);
		assertEquals(1.0D, d.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayUnpack(null));
	}
	
	@Test
	public void testArrayUnpackIntArrayPackedIntComponentOrder() {
		final PackedIntComponentOrder[] packedIntComponentOrders = PackedIntComponentOrder.values();
		
		for(final PackedIntComponentOrder packedIntComponentOrder : packedIntComponentOrders) {
			final int[] array = {
				packedIntComponentOrder.pack(255,   0,   0),
				packedIntComponentOrder.pack(  0, 255,   0),
				packedIntComponentOrder.pack(  0,   0, 255)
			};
			
			final Color4D[] colors = Color4D.arrayUnpack(array, packedIntComponentOrder);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color4D a = colors[0];
			final Color4D b = colors[1];
			final Color4D c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			assertEquals(0.0D, a.a);
			
			assertNotNull(b);
			
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			assertEquals(0.0D, b.a);
			
			assertNotNull(c);
			
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
			assertEquals(0.0D, c.a);
		}
		
		assertThrows(NullPointerException.class, () -> Color4D.arrayUnpack(null, PackedIntComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color4D.arrayUnpack(new int[] {}, null));
	}
	
	@Test
	public void testAverage() {
		final Color4D color = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(0.5D, color.average());
	}
	
	@Test
	public void testBlendColor4DColor4D() {
		final Color4D a = new Color4D(1.0D, 1.0D, 1.0D, 1.0D);
		final Color4D b = new Color4D(3.0D, 3.0D, 3.0D, 3.0D);
		final Color4D c = Color4D.blend(a, b);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		assertEquals(2.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color4D.blend(null, b));
	}
	
	@Test
	public void testBlendColor4DColor4DColor4DColor4DDoubleDouble() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(2.0D, 0.0D, 2.0D, 0.0D);
		final Color4D c = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 2.0D, 0.0D, 2.0D);
		final Color4D e = Color4D.blend(a, b, c, d, 0.5D, 0.5D);
		
		assertEquals(0.5D, e.r);
		assertEquals(0.5D, e.g);
		assertEquals(0.5D, e.b);
		assertEquals(0.5D, e.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, b, c, null, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, b, null, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, null, c, d, 0.5D, 0.5D));
		assertThrows(NullPointerException.class, () -> Color4D.blend(null, b, c, d, 0.5D, 0.5D));
	}
	
	@Test
	public void testBlendColor4DColor4DDouble() {
		final Color4D a = new Color4D(1.0D, 1.0D, 1.0D, 1.0D);
		final Color4D b = new Color4D(5.0D, 5.0D, 5.0D, 5.0D);
		final Color4D c = Color4D.blend(a, b, 0.25D);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		assertEquals(2.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Color4D.blend(null, b, 0.25D));
	}
	
	@Test
	public void testBlendColor4DColor4DDoubleDoubleDoubleDouble() {
		final Color4D a = new Color4D(1.0D, 1.0D, 1.0D, 1.0D);
		final Color4D b = new Color4D(2.0D, 2.0D, 2.0D, 2.0D);
		final Color4D c = Color4D.blend(a, b, 0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.5D, c.g);
		assertEquals(2.0D, c.b);
		assertEquals(3.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.blend(a, null, 0.0D, 0.5D, 1.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color4D.blend(null, b, 0.0D, 0.5D, 1.0D, 2.0D));
	}
	
	@Test
	public void testBlendOver() {
		final Color4D a = new Color4D(0.5D, 0.5D, 0.5D, 0.5D);
		final Color4D b = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		final Color4D c = Color4D.blendOver(a, b);
		
		assertEquals(0.25D, c.r);
		assertEquals(0.25D, c.g);
		assertEquals(0.25D, c.b);
		assertEquals(1.00D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.blendOver(a, null));
		assertThrows(NullPointerException.class, () -> Color4D.blendOver(null, b));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Color4D.clearCache();
		
		assertEquals(0, Color4D.getCacheSize());
		
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		final Color4D b = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		final Color4D c = Color4D.getCached(a);
		final Color4D d = Color4D.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Color4D.getCached(null));
		
		assertEquals(1, Color4D.getCacheSize());
		
		Color4D.clearCache();
		
		assertEquals(0, Color4D.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color4D(0.0D, 0.0D, 0.0D, 1.0D), Color4D.BLACK);
		assertEquals(new Color4D(0.0D, 0.0D, 1.0D, 1.0D), Color4D.BLUE);
		assertEquals(new Color4D(0.0D, 1.0D, 1.0D, 1.0D), Color4D.CYAN);
		assertEquals(new Color4D(0.5D, 0.5D, 0.5D, 1.0D), Color4D.GRAY);
		assertEquals(new Color4D(0.0D, 1.0D, 0.0D, 1.0D), Color4D.GREEN);
		assertEquals(new Color4D(1.0D, 0.0D, 1.0D, 1.0D), Color4D.MAGENTA);
		assertEquals(new Color4D(1.0D, 0.0D, 0.0D, 1.0D), Color4D.RED);
		assertEquals(new Color4D(0.0D, 0.0D, 0.0D, 0.0D), Color4D.TRANSPARENT);
		assertEquals(new Color4D(1.0D, 1.0D, 1.0D, 1.0D), Color4D.WHITE);
		assertEquals(new Color4D(1.0D, 1.0D, 0.0D, 1.0D), Color4D.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color4D color = new Color4D();
		
		assertEquals(0.0D, color.r);
		assertEquals(0.0D, color.g);
		assertEquals(0.0D, color.b);
		assertEquals(1.0D, color.a);
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color4D color = new Color4D(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(1.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3DDouble() {
		final Color4D color = new Color4D(new Color3D(1.0D, 1.0D, 1.0D), 0.0D);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3D)(null), 0.0D));
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color4D color = new Color4D(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(1.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor3FDouble() {
		final Color4D color = new Color4D(new Color3F(1.0F, 1.0F, 1.0F), 0.0D);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3F)(null), 0.0D));
	}
	
	@Test
	public void testConstructorColor3I() {
		final Color4D color = new Color4D(new Color3I(255, 255, 255));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(1.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3I)(null)));
	}
	
	@Test
	public void testConstructorColor3IInt() {
		final Color4D color = new Color4D(new Color3I(255, 255, 255), 0);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color3I)(null), 0));
	}
	
	@Test
	public void testConstructorColor4F() {
		final Color4D color = new Color4D(new Color4F(1.0F, 1.0F, 1.0F, 0.0F));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color4F)(null)));
	}
	
	@Test
	public void testConstructorColor4I() {
		final Color4D color = new Color4D(new Color4I(255, 255, 255, 0));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
		
		assertThrows(NullPointerException.class, () -> new Color4D((Color4I)(null)));
	}
	
	@Test
	public void testConstructorDouble() {
		final Color4D color = new Color4D(2.0D);
		
		assertEquals(2.0D, color.r);
		assertEquals(2.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(1.0D, color.a);
	}
	
	@Test
	public void testConstructorDoubleDouble() {
		final Color4D color = new Color4D(2.0D, 0.0D);
		
		assertEquals(2.0D, color.r);
		assertEquals(2.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(0.0D, color.a);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(1.0D, color.a);
	}
	
	@Test
	public void testConstructorDoubleDoubleDoubleDouble() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D, 3.0D);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(3.0D, color.a);
	}
	
	@Test
	public void testConstructorInt() {
		final Color4D color = new Color4D(255);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(1.0D, color.a);
	}
	
	@Test
	public void testConstructorIntInt() {
		final Color4D color = new Color4D(255, 0);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		assertEquals(0.0D, color.a);
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color4D color = new Color4D(0, 255, 255 * 2);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(1.0D, color.a);
	}
	
	@Test
	public void testConstructorIntIntIntInt() {
		final Color4D color = new Color4D(0, 255, 255 * 2, 255 * 3);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(2.0D, color.b);
		assertEquals(3.0D, color.a);
	}
	
	@Test
	public void testEqualsColor4D() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D c = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 2.0D, 1.5D);
		final Color4D e = new Color4D(0.0D, 2.0D, 1.0D, 1.5D);
		final Color4D f = new Color4D(2.0D, 0.5D, 1.0D, 1.5D);
		final Color4D g = null;
		
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
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D c = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 2.0D, 1.5D);
		final Color4D e = new Color4D(0.0D, 2.0D, 1.0D, 1.5D);
		final Color4D f = new Color4D(2.0D, 0.5D, 1.0D, 1.5D);
		final Color4D g = null;
		
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
		
		final Color4D color = Color4D.fromIntARGB(colorARGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
		assertEquals(255, color.toIntA());
	}
	
	@Test
	public void testFromIntARGBToDoubleA() {
		final int colorARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntARGBToDoubleA(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToDoubleB() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntARGBToDoubleB(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToDoubleG() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntARGBToDoubleG(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToDoubleR() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntARGBToDoubleR(colorARGB));
	}
	
	@Test
	public void testFromIntRGB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4D color = Color4D.fromIntRGB(colorRGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
		assertEquals(255, color.toIntA());
	}
	
	@Test
	public void testFromIntRGBToDoubleB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntRGBToDoubleB(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToDoubleG() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntRGBToDoubleG(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToDoubleR() {
		final int colorRGB = ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0D, Color4D.fromIntRGBToDoubleR(colorRGB));
	}
	
	@Test
	public void testGrayscaleA() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		final Color4D b = Color4D.grayscaleA(a);
		
		assertEquals(1.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(1.0D, b.b);
		assertEquals(1.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleA(null));
	}
	
	@Test
	public void testGrayscaleAverage() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = Color4D.grayscaleAverage(a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		assertEquals(1.5D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleB() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = Color4D.grayscaleB(a);
		
		assertEquals(1.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(1.0D, b.b);
		assertEquals(1.5D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleB(null));
	}
	
	@Test
	public void testGrayscaleG() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = Color4D.grayscaleG(a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		assertEquals(1.5D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleG(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color4D a = new Color4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Color4D b = Color4D.grayscaleLightness(a);
		
		assertEquals(2.0D, b.r);
		assertEquals(2.0D, b.g);
		assertEquals(2.0D, b.b);
		assertEquals(4.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleMax() {
		final Color4D a = new Color4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Color4D b = Color4D.grayscaleMax(a);
		
		assertEquals(2.0D, b.r);
		assertEquals(2.0D, b.g);
		assertEquals(2.0D, b.b);
		assertEquals(3.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleMax(null));
	}
	
	@Test
	public void testGrayscaleMin() {
		final Color4D a = new Color4D(0.0D, 1.0D, 2.0D, 3.0D);
		final Color4D b = Color4D.grayscaleMin(a);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(3.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleMin(null));
	}
	
	@Test
	public void testGrayscaleR() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = Color4D.grayscaleR(a);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(1.5D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleR(null));
	}
	
	@Test
	public void testGrayscaleRelativeLuminance() {
		final Color4D a = new Color4D(1.0D / 0.212671D, 1.0D / 0.715160D, 1.0D / 0.072169D, 0.0D);
		final Color4D b = Color4D.grayscaleRelativeLuminance(a);
		
		assertEquals(3.0D, b.r);
		assertEquals(3.0D, b.g);
		assertEquals(3.0D, b.b);
		assertEquals(0.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.grayscaleRelativeLuminance(null));
	}
	
	@Test
	public void testHashCode() {
		final Color4D a = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		final Color4D b = new Color4D(0.0D, 0.5D, 1.0D, 1.5D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testInvert() {
		final Color4D a = new Color4D(0.75D, 0.75D, 0.75D, 0.75D);
		final Color4D b = Color4D.invert(a);
		
		assertEquals(0.25D, b.r);
		assertEquals(0.25D, b.g);
		assertEquals(0.25D, b.b);
		assertEquals(0.75D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.invert(null));
	}
	
	@Test
	public void testIsBlack() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 0.0D, 1.0D);
		final Color4D c = new Color4D(0.0D, 1.0D, 0.0D);
		final Color4D d = new Color4D(1.0D, 0.0D, 0.0D);
		final Color4D e = new Color4D(0.0D, 1.0D, 1.0D);
		final Color4D f = new Color4D(1.0D, 1.0D, 0.0D);
		final Color4D g = new Color4D(1.0D, 1.0D, 1.0D);
		
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
		final Color4D a = new Color4D(0.0D, 0.0D, 1.0D);
		final Color4D b = new Color4D(0.5D, 0.5D, 1.0D);
		
		assertTrue(a.isBlue());
		
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueDoubleDouble() {
		final Color4D a = new Color4D(0.0D, 0.0D, 1.0D);
		final Color4D b = new Color4D(0.5D, 0.5D, 1.0D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isBlue(0.5D, 0.5D));
		assertTrue(b.isBlue(0.5D, 0.5D));
		
		assertFalse(b.isBlue(0.5D, 1.0D));
		assertFalse(b.isBlue(1.0D, 0.5D));
		assertFalse(c.isBlue(0.5D, 0.5D));
	}
	
	@Test
	public void testIsCyan() {
		final Color4D a = new Color4D(0.0D, 1.0D, 1.0D);
		final Color4D b = new Color4D(0.0D, 0.5D, 0.5D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		
		assertFalse(c.isCyan());
		assertFalse(d.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.5D, 0.5D, 0.5D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 0.5D);
		final Color4D e = new Color4D(0.0D, 0.5D, 0.5D);
		final Color4D f = new Color4D(0.0D, 0.5D, 0.0D);
		final Color4D g = new Color4D(0.0D, 0.5D, 1.0D);
		
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
		final Color4D a = new Color4D(0.0D, 1.0D, 0.0D);
		final Color4D b = new Color4D(0.5D, 1.0D, 0.5D);
		
		assertTrue(a.isGreen());
		
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenDoubleDouble() {
		final Color4D a = new Color4D(0.0D, 1.0D, 0.0D);
		final Color4D b = new Color4D(0.5D, 1.0D, 0.5D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isGreen(0.5D, 0.5D));
		assertTrue(b.isGreen(0.5D, 0.5D));
		
		assertFalse(b.isGreen(0.5D, 1.0D));
		assertFalse(b.isGreen(1.0D, 0.5D));
		assertFalse(c.isGreen(0.5D, 0.5D));
	}
	
	@Test
	public void testIsMagenta() {
		final Color4D a = new Color4D(1.0D, 0.0D, 1.0D);
		final Color4D b = new Color4D(0.5D, 0.0D, 0.5D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		
		assertFalse(c.isMagenta());
		assertFalse(d.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color4D a = new Color4D(1.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(1.0D, 0.5D, 0.5D);
		
		assertTrue(a.isRed());
		
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedDoubleDouble() {
		final Color4D a = new Color4D(1.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(1.0D, 0.5D, 0.5D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isRed(0.5D, 0.5D));
		assertTrue(b.isRed(0.5D, 0.5D));
		
		assertFalse(b.isRed(0.5D, 1.0D));
		assertFalse(b.isRed(1.0D, 0.5D));
		assertFalse(c.isRed(0.5D, 0.5D));
	}
	
	@Test
	public void testIsWhite() {
		final Color4D a = new Color4D(1.0D, 1.0D, 1.0D);
		final Color4D b = new Color4D(1.0D, 1.0D, 0.0D);
		final Color4D c = new Color4D(1.0D, 0.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 0.0D);
		
		assertTrue(a.isWhite());
		
		assertFalse(b.isWhite());
		assertFalse(c.isWhite());
		assertFalse(d.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color4D a = new Color4D(1.0D, 1.0D, 0.0D);
		final Color4D b = new Color4D(0.5D, 0.5D, 0.0D);
		final Color4D c = new Color4D(1.0D, 1.0D, 1.0D);
		final Color4D d = new Color4D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		
		assertFalse(c.isYellow());
		assertFalse(d.isYellow());
	}
	
	@Test
	public void testLightness() {
		final Color4D color = new Color4D(1.0D, 2.0D, 3.0D, 4.0D);
		
		assertEquals(2.0D, color.lightness());
	}
	
	@Test
	public void testMax() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D, 3.0D);
		
		assertEquals(2.0D, color.max());
	}
	
	@Test
	public void testMin() {
		final Color4D color = new Color4D(0.0D, 1.0D, 2.0D, 3.0D);
		
		assertEquals(0.0D, color.min());
	}
	
	@Test
	public void testMultiplyColor4DDouble() {
		final Color4D a = new Color4D(1.0D, 2.0D, 3.0D, 4.0D);
		final Color4D b = Color4D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.r);
		assertEquals(4.0D, b.g);
		assertEquals(6.0D, b.b);
		assertEquals(4.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.multiply(null, 2.0D));
	}
	
	@Test
	public void testPack() {
		final Color4D a = new Color4D(1.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 1.0D, 0.0D, 0.0D);
		final Color4D c = new Color4D(0.0D, 0.0D, 1.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		
		final int packedA = a.pack();
		final int packedB = b.pack();
		final int packedC = c.pack();
		final int packedD = d.pack();
		
		final Color4D e = Color4D.unpack(packedA);
		final Color4D f = Color4D.unpack(packedB);
		final Color4D g = Color4D.unpack(packedC);
		final Color4D h = Color4D.unpack(packedD);
		
		assertEquals(a, e);
		assertEquals(b, f);
		assertEquals(c, g);
		assertEquals(d, h);
	}
	
	@Test
	public void testPackPackedIntComponentOrder() {
		final Color4D a = new Color4D(1.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 1.0D, 0.0D, 0.0D);
		final Color4D c = new Color4D(0.0D, 0.0D, 1.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		
		final PackedIntComponentOrder[] packedIntComponentOrders = PackedIntComponentOrder.values();
		
		for(final PackedIntComponentOrder packedIntComponentOrder : packedIntComponentOrders) {
			final int packedA = a.pack(packedIntComponentOrder);
			final int packedB = b.pack(packedIntComponentOrder);
			final int packedC = c.pack(packedIntComponentOrder);
			final int packedD = d.pack(packedIntComponentOrder);
			
			final Color4D e = Color4D.unpack(packedA, packedIntComponentOrder);
			final Color4D f = Color4D.unpack(packedB, packedIntComponentOrder);
			final Color4D g = Color4D.unpack(packedC, packedIntComponentOrder);
			final Color4D h = Color4D.unpack(packedD, packedIntComponentOrder);
			
			assertEquals(a, e);
			assertEquals(b, f);
			assertEquals(c, g);
			
			if(packedIntComponentOrder.getComponentCount() == 4) {
				assertEquals(d, h);
			} else {
				assertEquals(Color4D.TRANSPARENT, h);
			}
		}
		
		assertThrows(NullPointerException.class, () -> a.pack(null));
	}
	
	@Test
	public void testRandom() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.random();
			
			assertTrue(color.r >= 0.0D && color.r <= 1.0D);
			assertTrue(color.g >= 0.0D && color.g <= 1.0D);
			assertTrue(color.b >= 0.0D && color.b <= 1.0D);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomBlue() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomBlue();
			
			assertTrue(color.r >= 0.0D && color.r <= 0.0D);
			assertTrue(color.g >= 0.0D && color.g <= 0.0D);
			assertTrue(color.b >  0.0D && color.b <= 1.0D);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomBlueDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomBlue(0.25D, 0.50D);
			
			assertTrue(color.r >= 0.0D && color.r <= 0.25D);
			assertTrue(color.g >= 0.0D && color.g <= 0.50D);
			assertTrue(color.b >  0.0D && color.b <= 1.00D);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomCyan() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomCyan();
			
			assertTrue(color.r >= 0.0D && color.r <= 0.0D);
			assertTrue(color.g >  0.0D && color.g <= 1.0D);
			assertTrue(color.b >  0.0D && color.b <= 1.0D);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomCyanDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomCyan(0.50D, 0.25D);
			
			assertTrue(color.r >= 0.0D && color.r <= 0.25D);
			assertTrue(color.g >= 0.5D && color.g <= 1.00D);
			assertTrue(color.b >= 0.5D && color.b <= 1.00D);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomGrayscale() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomGrayscale();
			
			assertTrue(color.r >= 0.0D && color.r <= 1.0D);
			assertTrue(color.g >= 0.0D && color.g <= 1.0D);
			assertTrue(color.b >= 0.0D && color.b <= 1.0D);
			
			assertEquals(color.r, color.g);
			assertEquals(color.g, color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomGreen() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomGreen();
			
			assertTrue(color.r >= 0.0D && color.r <= 0.0D);
			assertTrue(color.g >  0.0D && color.g <= 1.0D);
			assertTrue(color.b >= 0.0D && color.b <= 0.0D);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomGreenDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomGreen(0.25D, 0.50D);
			
			assertTrue(color.r >= 0.0D && color.r <= 0.25D);
			assertTrue(color.g >  0.0D && color.g <= 1.00D);
			assertTrue(color.b >= 0.0D && color.b <= 0.50D);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomMagenta() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomMagenta();
			
			assertTrue(color.r >  0.0D && color.r <= 1.0D);
			assertTrue(color.g >= 0.0D && color.g <= 0.0D);
			assertTrue(color.b >  0.0D && color.b <= 1.0D);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomMagentaDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomMagenta(0.50D, 0.25D);
			
			assertTrue(color.r >= 0.5D && color.r <= 1.00D);
			assertTrue(color.g >= 0.0D && color.g <= 0.25D);
			assertTrue(color.b >= 0.5D && color.b <= 1.00D);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomRed() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomRed();
			
			assertTrue(color.r >  0.0D && color.r <= 1.0D);
			assertTrue(color.g >= 0.0D && color.g <= 0.0D);
			assertTrue(color.b >= 0.0D && color.b <= 0.0D);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomRedDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomRed(0.25D, 0.50D);
			
			assertTrue(color.r >  0.0D && color.r <= 1.00D);
			assertTrue(color.g >= 0.0D && color.g <= 0.25D);
			assertTrue(color.b >= 0.0D && color.b <= 0.50D);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomYellow() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomYellow();
			
			assertTrue(color.r >  0.0D && color.r <= 1.0D);
			assertTrue(color.g >  0.0D && color.g <= 1.0D);
			assertTrue(color.b >= 0.0D && color.b <= 0.0D);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRandomYellowDoubleDouble() {
		for(int i = 0; i < 1000; i++) {
			final Color4D color = Color4D.randomYellow(0.50D, 0.25D);
			
			assertTrue(color.r >= 0.5D && color.r <= 1.00D);
			assertTrue(color.g >= 0.5D && color.g <= 1.00D);
			assertTrue(color.b >= 0.0D && color.b <= 0.25D);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
			
			assertEquals(1.0D, color.a);
		}
	}
	
	@Test
	public void testRead() throws IOException {
		final Color4D a = new Color4D(1.0D, 0.5D, 0.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.r);
		dataOutput.writeDouble(a.g);
		dataOutput.writeDouble(a.b);
		dataOutput.writeDouble(a.a);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color4D b = Color4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Color4D.read(null));
		assertThrows(UncheckedIOException.class, () -> Color4D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRelativeLuminance() {
		final Color4D color = new Color4D(1.0D / 0.212671D, 1.0D / 0.715160D, 1.0D / 0.072169D, 1.0D);
		
		assertEquals(3.0D, color.relativeLuminance());
	}
	
	@Test
	public void testSaturateColor4D() {
		final Color4D a = Color4D.saturate(new Color4D(-1.0D, -1.0D, -1.0D, -1.0D));
		final Color4D b = Color4D.saturate(new Color4D(0.5D, 0.5D, 0.5D, 0.5D));
		final Color4D c = Color4D.saturate(new Color4D(2.0D, 2.0D, 2.0D, 2.0D));
		
		assertEquals(0.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		assertEquals(0.0D, a.a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		assertEquals(0.5D, b.a);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(1.0D, c.b);
		assertEquals(1.0D, c.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.saturate(null));
	}
	
	@Test
	public void testSaturateColor4DDoubleDouble() {
		final Color4D a = Color4D.saturate(new Color4D(-10.0D, -10.0D, -10.0D, -10.0D), -5.0D, +5.0D);
		final Color4D b = Color4D.saturate(new Color4D(-10.0D, -10.0D, -10.0D, -10.0D), +5.0D, -5.0D);
		final Color4D c = Color4D.saturate(new Color4D(2.0D, 2.0D, 2.0D, 2.0D), -5.0D, +5.0D);
		final Color4D d = Color4D.saturate(new Color4D(2.0D, 2.0D, 2.0D, 2.0D), +5.0D, -5.0D);
		final Color4D e = Color4D.saturate(new Color4D(10.0D, 10.0D, 10.0D, 10.0D), -5.0D, +5.0D);
		final Color4D f = Color4D.saturate(new Color4D(10.0D, 10.0D, 10.0D, 10.0D), +5.0D, -5.0D);
		
		assertEquals(-5.0D, a.r);
		assertEquals(-5.0D, a.g);
		assertEquals(-5.0D, a.b);
		assertEquals(-5.0D, a.a);
		
		assertEquals(-5.0D, b.r);
		assertEquals(-5.0D, b.g);
		assertEquals(-5.0D, b.b);
		assertEquals(-5.0D, b.a);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		assertEquals(2.0D, c.a);
		
		assertEquals(2.0D, d.r);
		assertEquals(2.0D, d.g);
		assertEquals(2.0D, d.b);
		assertEquals(2.0D, d.a);
		
		assertEquals(5.0D, e.r);
		assertEquals(5.0D, e.g);
		assertEquals(5.0D, e.b);
		assertEquals(5.0D, e.a);
		
		assertEquals(5.0D, f.r);
		assertEquals(5.0D, f.g);
		assertEquals(5.0D, f.b);
		assertEquals(5.0D, f.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.saturate(null, -5.0D, +5.0D));
	}
	
	@Test
	public void testSepia() {
		final Color4D a = new Color4D(1.0D, 1.0D, 1.0D, 1.0D);
		final Color4D b = Color4D.sepia(a);
		
		assertEquals(1.351D, b.r);
		assertEquals(1.203D, b.g);
		assertEquals(0.937D, b.b);
		assertEquals(1.000D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.sepia(null));
	}
	
	@Test
	public void testSqrt() {
		final Color4D a = new Color4D(16.0D, 25.0D, 36.0D, 49.0D);
		final Color4D b = Color4D.sqrt(a);
		
		assertEquals(4.0D, b.r);
		assertEquals(5.0D, b.g);
		assertEquals(6.0D, b.b);
		assertEquals(7.0D, b.a);
		
		assertThrows(NullPointerException.class, () -> Color4D.sqrt(null));
	}
	
	@Test
	public void testToIntA() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 0.0D, 0.0D, 0.5D);
		final Color4D c = new Color4D(0.0D, 0.0D, 0.0D, 1.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 0.0D, 2.0D);
		
		assertEquals(  0, a.toIntA());
		assertEquals(128, b.toIntA());
		assertEquals(255, c.toIntA());
		assertEquals(255, d.toIntA());
	}
	
	@Test
	public void testToIntARGB() {
		final int expectedIntARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4D color = new Color4D(0.0D, 0.5D, 1.0D, 2.0D);
		
		assertEquals(expectedIntARGB, color.toIntARGB());
	}
	
	@Test
	public void testToIntB() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 0.0D, 0.5D, 0.0D);
		final Color4D c = new Color4D(0.0D, 0.0D, 1.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 0.0D, 2.0D, 0.0D);
		
		assertEquals(  0, a.toIntB());
		assertEquals(128, b.toIntB());
		assertEquals(255, c.toIntB());
		assertEquals(255, d.toIntB());
	}
	
	@Test
	public void testToIntG() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.0D, 0.5D, 0.0D, 0.0D);
		final Color4D c = new Color4D(0.0D, 1.0D, 0.0D, 0.0D);
		final Color4D d = new Color4D(0.0D, 2.0D, 0.0D, 0.0D);
		
		assertEquals(  0, a.toIntG());
		assertEquals(128, b.toIntG());
		assertEquals(255, c.toIntG());
		assertEquals(255, d.toIntG());
	}
	
	@Test
	public void testToIntR() {
		final Color4D a = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
		final Color4D b = new Color4D(0.5D, 0.0D, 0.0D, 0.0D);
		final Color4D c = new Color4D(1.0D, 0.0D, 0.0D, 0.0D);
		final Color4D d = new Color4D(2.0D, 0.0D, 0.0D, 0.0D);
		
		assertEquals(  0, a.toIntR());
		assertEquals(128, b.toIntR());
		assertEquals(255, c.toIntR());
		assertEquals(255, d.toIntR());
	}
	
	@Test
	public void testToIntRGB() {
		final int expectedIntRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color4D color = new Color4D(0.0D, 0.5D, 1.0D, 1.0D);
		
		assertEquals(expectedIntRGB, color.toIntRGB());
	}
	
	@Test
	public void testToString() {
		final Color4D color = new Color4D(0.0D, 0.25D, 0.75D, 1.0D);
		
		assertEquals("new Color4D(0.0D, 0.25D, 0.75D, 1.0D)", color.toString());
	}
	
	@Test
	public void testUnpackInt() {
		final Color4D a = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(  0,   0,   0,   0));
		final Color4D b = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(255,   0,   0,   0));
		final Color4D c = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(  0, 255,   0,   0));
		final Color4D d = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(  0,   0, 255,   0));
		final Color4D e = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(  0,   0,   0, 255));
		final Color4D f = Color4D.unpack(PackedIntComponentOrder.ARGB.pack(255, 255, 255, 255));
		
		assertEquals(0.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		assertEquals(0.0D, a.a);
		
		assertEquals(1.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		assertEquals(0.0D, b.a);
		
		assertEquals(0.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(0.0D, c.b);
		assertEquals(0.0D, c.a);
		
		assertEquals(0.0D, d.r);
		assertEquals(0.0D, d.g);
		assertEquals(1.0D, d.b);
		assertEquals(0.0D, d.a);
		
		assertEquals(0.0D, e.r);
		assertEquals(0.0D, e.g);
		assertEquals(0.0D, e.b);
		assertEquals(1.0D, e.a);
		
		assertEquals(1.0D, f.r);
		assertEquals(1.0D, f.g);
		assertEquals(1.0D, f.b);
		assertEquals(1.0D, f.a);
	}
	
	@Test
	public void testUnpackIntPackedIntComponentOrder() {
		for(final PackedIntComponentOrder packedIntComponentOrder : PackedIntComponentOrder.values()) {
			final Color4D a = Color4D.unpack(packedIntComponentOrder.pack(  0,   0,   0,   0), packedIntComponentOrder);
			final Color4D b = Color4D.unpack(packedIntComponentOrder.pack(255,   0,   0,   0), packedIntComponentOrder);
			final Color4D c = Color4D.unpack(packedIntComponentOrder.pack(  0, 255,   0,   0), packedIntComponentOrder);
			final Color4D d = Color4D.unpack(packedIntComponentOrder.pack(  0,   0, 255,   0), packedIntComponentOrder);
			final Color4D e = Color4D.unpack(packedIntComponentOrder.pack(  0,   0,   0, 255), packedIntComponentOrder);
			final Color4D f = Color4D.unpack(packedIntComponentOrder.pack(255, 255, 255, 255), packedIntComponentOrder);
			
			assertEquals(0.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			assertEquals(0.0D, a.a);
			
			assertEquals(1.0D, b.r);
			assertEquals(0.0D, b.g);
			assertEquals(0.0D, b.b);
			assertEquals(0.0D, b.a);
			
			assertEquals(0.0D, c.r);
			assertEquals(1.0D, c.g);
			assertEquals(0.0D, c.b);
			assertEquals(0.0D, c.a);
			
			assertEquals(0.0D, d.r);
			assertEquals(0.0D, d.g);
			assertEquals(1.0D, d.b);
			assertEquals(0.0D, d.a);
			
			assertEquals(0.0D, e.r);
			assertEquals(0.0D, e.g);
			assertEquals(0.0D, e.b);
			
			if(packedIntComponentOrder.getComponentCount() == 4) {
				assertEquals(1.0D, e.a);
			} else {
				assertEquals(0.0D, e.a);
			}
			
			assertEquals(1.0D, f.r);
			assertEquals(1.0D, f.g);
			assertEquals(1.0D, f.b);
			
			if(packedIntComponentOrder.getComponentCount() == 4) {
				assertEquals(1.0D, f.a);
			} else {
				assertEquals(0.0D, f.a);
			}
		}
		
		assertThrows(NullPointerException.class, () -> Color4D.unpack(PackedIntComponentOrder.ARGB.pack(0, 0, 0, 0), null));
	}
	
	@Test
	public void testWrite() {
		final Color4D a = new Color4D(1.0D, 0.5D, 0.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color4D b = Color4D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}