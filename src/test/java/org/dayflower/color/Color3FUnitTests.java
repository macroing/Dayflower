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
public final class Color3FUnitTests {
	public Color3FUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddAndMultiplyColor3FColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addAndMultiply(a, b, c);
		
		assertEquals( 7.0F, d.r);
		assertEquals(14.0F, d.g);
		assertEquals(23.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(null, b, c));
	}
	
	@Test
	public void testAddAndMultiplyColor3FColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addAndMultiply(a, b, c, 2.0F);
		
		assertEquals(13.0F, d.r);
		assertEquals(26.0F, d.g);
		assertEquals(43.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, b, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(a, null, c, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addAndMultiply(null, b, c, 2.0F));
	}
	
	@Test
	public void testAddColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = Color3F.add(a, b);
		
		assertEquals(3.0F, c.r);
		assertEquals(5.0F, c.g);
		assertEquals(7.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.add(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.add(null, b));
	}
	
	@Test
	public void testAddColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.add(a, 2.0F);
		
		assertEquals(3.0F, b.r);
		assertEquals(4.0F, b.g);
		assertEquals(5.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.add(null, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FColor3FFloatFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = new Color3F(4.0F, 5.0F, 6.0F);
		final Color3F e = Color3F.addMultiplyAndDivide(a, b, c, d, 2.0F, 2.0F);
		
		assertEquals( 25.0F, e.r);
		assertEquals( 62.0F, e.g);
		assertEquals(123.0F, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, c, null, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, d, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, d, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, d, 2.0F, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addMultiplyAndDivide(a, b, c, 2.0F);
		
		assertEquals( 4.0F, d.r);
		assertEquals( 8.0F, d.g);
		assertEquals(13.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, 2.0F));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3FColor3FColor3FFloatFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = new Color3F(3.0F, 4.0F, 5.0F);
		final Color3F d = Color3F.addMultiplyAndDivide(a, b, c, 2.0F, 2.0F);
		
		assertEquals( 7.0F, d.r);
		assertEquals(14.0F, d.g);
		assertEquals(23.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, b, null, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(a, null, c, 2.0F, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.addMultiplyAndDivide(null, b, c, 2.0F, 2.0F));
	}
	
	@Test
	public void testAddSample() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(1.5F, 1.5F, 1.5F);
		final Color3F e = Color3F.addSample(a, c, 2);
		final Color3F f = Color3F.addSample(e, b, 3);
		final Color3F g = Color3F.addSample(f, d, 4);
		
		assertEquals(0.5F, e.r);
		assertEquals(0.5F, e.g);
		assertEquals(0.5F, e.b);
		
		assertEquals(0.5F, f.r);
		assertEquals(0.5F, f.g);
		assertEquals(0.5F, f.b);
		
		assertEquals(0.75F, g.r);
		assertEquals(0.75F, g.g);
		assertEquals(0.75F, g.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.addSample(a, null, 1));
		assertThrows(NullPointerException.class, () -> Color3F.addSample(null, b, 1));
	}
	
	@Test
	public void testArrayInt() {
		final Color3F[] colors = Color3F.array(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color3F color : colors) {
			assertNotNull(color);
			
			assertEquals(Color3F.BLACK, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3F.array(-1));
	}
	
	@Test
	public void testArrayIntIntFunction() {
		final Color3F[] colors = Color3F.array(10, index -> Color3F.RED);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color3F color : colors) {
			assertNotNull(color);
			
			assertEquals(Color3F.RED, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3F.array(-1, index -> Color3F.RED));
		
		assertThrows(NullPointerException.class, () -> Color3F.array(10, index -> null));
		assertThrows(NullPointerException.class, () -> Color3F.array(10, null));
	}
	
	@Test
	public void testArrayRandom() {
		final Color3F[] colors = Color3F.arrayRandom(10);
		
		assertNotNull(colors);
		
		assertEquals(10, colors.length);
		
		for(final Color3F color : colors) {
			assertNotNull(color);
			
			assertTrue(color.r >= 0.0F && color.r <= 1.0F);
			assertTrue(color.g >= 0.0F && color.g <= 1.0F);
			assertTrue(color.b >= 0.0F && color.b <= 1.0F);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3F.arrayRandom(-1));
	}
	
	@Test
	public void testArrayReadByteArray() {
		final byte[] array = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		final Color3F[] colors = Color3F.arrayRead(array);
		
		assertNotNull(colors);
		
		assertEquals(3, colors.length);
		
		final Color3F a = colors[0];
		final Color3F b = colors[1];
		final Color3F c = colors[2];
		
		assertNotNull(a);
		
		assertEquals(1.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertNotNull(b);
		
		assertEquals(0.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertNotNull(c);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(1.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead((byte[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color3F.arrayRead(new byte[] {(byte)(255), (byte)(255)}));
	}
	
	@Test
	public void testArrayReadByteArrayArrayComponentOrder() {
		final byte[][] arrays = {
			{(byte)(128), (byte)(255), (byte)(  0), (byte)(  0), (byte)(128), (byte)(  0), (byte)(255), (byte)(  0), (byte)(128), (byte)(0), (byte)(  0), (byte)(255)},
			{(byte)(  0), (byte)(  0), (byte)(255), (byte)(  0), (byte)(255), (byte)(  0), (byte)(255), (byte)(  0), (byte)(  0)},
			{(byte)(  0), (byte)(  0), (byte)(255), (byte)(128), (byte)(  0), (byte)(255), (byte)(  0), (byte)(128), (byte)(255), (byte)(0), (byte)(  0), (byte)(128)},
			{(byte)(255), (byte)(  0), (byte)(  0), (byte)(  0), (byte)(255), (byte)(  0), (byte)(  0), (byte)(  0), (byte)(255)},
			{(byte)(255), (byte)(  0), (byte)(  0), (byte)(128), (byte)(  0), (byte)(255), (byte)(  0), (byte)(128), (byte)(  0), (byte)(0), (byte)(255), (byte)(128)}
		};
		
		final ArrayComponentOrder[] arrayComponentOrders = ArrayComponentOrder.values();
		
		for(int i = 0; i < arrays.length; i++) {
			final Color3F[] colors = Color3F.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color3F a = colors[0];
			final Color3F b = colors[1];
			final Color3F c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0F, a.r);
			assertEquals(0.0F, a.g);
			assertEquals(0.0F, a.b);
			
			assertNotNull(b);
			
			assertEquals(0.0F, b.r);
			assertEquals(1.0F, b.g);
			assertEquals(0.0F, b.b);
			
			assertNotNull(c);
			
			assertEquals(0.0F, c.r);
			assertEquals(0.0F, c.g);
			assertEquals(1.0F, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead((byte[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead(new byte[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color3F.arrayRead(new byte[] {(byte)(255), (byte)(255)}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayReadIntArray() {
		final int[] array = {255, 0, 0, 0, 255, 0, 0, 0, 255};
		
		final Color3F[] colors = Color3F.arrayRead(array);
		
		assertNotNull(colors);
		
		assertEquals(3, colors.length);
		
		final Color3F a = colors[0];
		final Color3F b = colors[1];
		final Color3F c = colors[2];
		
		assertNotNull(a);
		
		assertEquals(1.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertNotNull(b);
		
		assertEquals(0.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertNotNull(c);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(1.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead((int[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color3F.arrayRead(new int[] {255, 255}));
	}
	
	@Test
	public void testArrayReadIntArrayArrayComponentOrder() {
		final int[][] arrays = {
			{128, 255,   0,   0, 128,   0, 255,   0, 128, 0,   0, 255},
			{  0,   0, 255,   0, 255,   0, 255,   0,   0},
			{  0,   0, 255, 128,   0, 255,   0, 128, 255, 0,   0, 128},
			{255,   0,   0,   0, 255,   0,   0,   0, 255},
			{255,   0,   0, 128,   0, 255,   0, 128,   0, 0, 255, 128}
		};
		
		final ArrayComponentOrder[] arrayComponentOrders = ArrayComponentOrder.values();
		
		for(int i = 0; i < arrays.length; i++) {
			final Color3F[] colors = Color3F.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color3F a = colors[0];
			final Color3F b = colors[1];
			final Color3F c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0F, a.r);
			assertEquals(0.0F, a.g);
			assertEquals(0.0F, a.b);
			
			assertNotNull(b);
			
			assertEquals(0.0F, b.r);
			assertEquals(1.0F, b.g);
			assertEquals(0.0F, b.b);
			
			assertNotNull(c);
			
			assertEquals(0.0F, c.r);
			assertEquals(0.0F, c.g);
			assertEquals(1.0F, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead((int[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3F.arrayRead(new int[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color3F.arrayRead(new int[] {255, 255}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayUnpackIntArray() {
		final int[] array = {
			PackedIntComponentOrder.ARGB.pack(255,   0,   0),
			PackedIntComponentOrder.ARGB.pack(  0, 255,   0),
			PackedIntComponentOrder.ARGB.pack(  0,   0, 255)
		};
		
		final Color3F[] colors = Color3F.arrayUnpack(array);
		
		assertNotNull(colors);
		
		assertEquals(3, colors.length);
		
		final Color3F a = colors[0];
		final Color3F b = colors[1];
		final Color3F c = colors[2];
		
		assertNotNull(a);
		
		assertEquals(1.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertNotNull(b);
		
		assertEquals(0.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertNotNull(c);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(1.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayUnpack(null));
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
			
			final Color3F[] colors = Color3F.arrayUnpack(array, packedIntComponentOrder);
			
			assertNotNull(colors);
			
			assertEquals(3, colors.length);
			
			final Color3F a = colors[0];
			final Color3F b = colors[1];
			final Color3F c = colors[2];
			
			assertNotNull(a);
			
			assertEquals(1.0F, a.r);
			assertEquals(0.0F, a.g);
			assertEquals(0.0F, a.b);
			
			assertNotNull(b);
			
			assertEquals(0.0F, b.r);
			assertEquals(1.0F, b.g);
			assertEquals(0.0F, b.b);
			
			assertNotNull(c);
			
			assertEquals(0.0F, c.r);
			assertEquals(0.0F, c.g);
			assertEquals(1.0F, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3F.arrayUnpack(null, PackedIntComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3F.arrayUnpack(new int[] {}, null));
	}
	
	@Test
	public void testAverage() {
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(0.5F, color.average());
	}
	
	@Test
	public void testBlendColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(3.0F, 3.0F, 3.0F);
		final Color3F c = Color3F.blend(a, b);
		
		assertEquals(2.0F, c.r);
		assertEquals(2.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b));
	}
	
	@Test
	public void testBlendColor3FColor3FColor3FColor3FFloatFloat() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(2.0F, 0.0F, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F d = new Color3F(0.0F, 2.0F, 0.0F);
		final Color3F e = Color3F.blend(a, b, c, d, 0.5F, 0.5F);
		
		assertEquals(0.5F, e.r);
		assertEquals(0.5F, e.g);
		assertEquals(0.0F, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, b, c, null, 0.5F, 0.5F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, b, null, d, 0.5F, 0.5F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null, c, d, 0.5F, 0.5F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b, c, d, 0.5F, 0.5F));
	}
	
	@Test
	public void testBlendColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(5.0F, 5.0F, 5.0F);
		final Color3F c = Color3F.blend(a, b, 0.25F);
		
		assertEquals(2.0F, c.r);
		assertEquals(2.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null, 0.25F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b, 0.25F));
	}
	
	@Test
	public void testBlendColor3FColor3FFloatFloatFloat() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = Color3F.blend(a, b, 0.0F, 0.5F, 1.0F);
		
		assertEquals(1.0F, c.r);
		assertEquals(1.5F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.blend(a, null, 0.0F, 0.5F, 1.0F));
		assertThrows(NullPointerException.class, () -> Color3F.blend(null, b, 0.0F, 0.5F, 1.0F));
	}
	
	@Test
	public void testClearCacheAndGetCacheSizeAndGetCached() {
		Color3F.clearCache();
		
		assertEquals(0, Color3F.getCacheSize());
		
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F c = Color3F.getCached(a);
		final Color3F d = Color3F.getCached(b);
		
		assertThrows(NullPointerException.class, () -> Color3F.getCached(null));
		
		assertEquals(1, Color3F.getCacheSize());
		
		Color3F.clearCache();
		
		assertEquals(0, Color3F.getCacheSize());
		
		assertTrue(a != b);
		assertTrue(a == c);
		assertTrue(a == d);
		
		assertTrue(b != a);
		assertTrue(b != c);
		assertTrue(b != d);
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color3F(0.0F, 0.0F, 0.0F), Color3F.BLACK);
		assertEquals(new Color3F(0.0F, 0.0F, 1.0F), Color3F.BLUE);
		assertEquals(new Color3F(0.0F, 1.0F, 1.0F), Color3F.CYAN);
		assertEquals(new Color3F(0.5F, 0.5F, 0.5F), Color3F.GRAY);
		assertEquals(new Color3F(0.0F, 1.0F, 0.0F), Color3F.GREEN);
		assertEquals(new Color3F(1.0F, 0.0F, 1.0F), Color3F.MAGENTA);
		assertEquals(new Color3F(1.0F, 0.0F, 0.0F), Color3F.RED);
		assertEquals(new Color3F(1.0F, 1.0F, 1.0F), Color3F.WHITE);
		assertEquals(new Color3F(1.0F, 1.0F, 0.0F), Color3F.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color3F color = new Color3F();
		
		assertEquals(0.0F, color.r);
		assertEquals(0.0F, color.g);
		assertEquals(0.0F, color.b);
	}
	
	@Test
	public void testConstructorColor3D() {
		final Color3F color = new Color3F(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3F((Color3D)(null)));
	}
	
	@Test
	public void testConstructorColor3I() {
		final Color3F color = new Color3F(new Color3I(255, 255, 255));
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3F((Color3I)(null)));
	}
	
	@Test
	public void testConstructorColor4D() {
		final Color3F color = new Color3F(new Color4D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3F((Color4D)(null)));
	}
	
	@Test
	public void testConstructorColor4F() {
		final Color3F color = new Color3F(new Color4F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3F((Color4F)(null)));
	}
	
	@Test
	public void testConstructorColor4I() {
		final Color3F color = new Color3F(new Color4I(255, 255, 255, 255));
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3F((Color4I)(null)));
	}
	
	@Test
	public void testConstructorFloat() {
		final Color3F color = new Color3F(2.0F);
		
		assertEquals(2.0F, color.r);
		assertEquals(2.0F, color.g);
		assertEquals(2.0F, color.b);
	}
	
	@Test
	public void testConstructorFloatFloatFloat() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(2.0F, color.b);
	}
	
	@Test
	public void testConstructorInt() {
		final Color3F color = new Color3F(255);
		
		assertEquals(1.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(1.0F, color.b);
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color3F color = new Color3F(0, 255, 255 * 2);
		
		assertEquals(0.0F, color.r);
		assertEquals(1.0F, color.g);
		assertEquals(2.0F, color.b);
	}
	
	@Test
	public void testDivideColor3FColor3F() {
		final Color3F a = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), new Color3F(2.0F, 2.0F, 2.0F));
		final Color3F b = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), new Color3F(0.0F, 0.0F, 0.0F));
		final Color3F c = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), new Color3F(Float.NaN));
		final Color3F d = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), new Color3F(Float.NEGATIVE_INFINITY));
		final Color3F e = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), new Color3F(Float.POSITIVE_INFINITY));
		
		assertEquals(0.50F, a.r);
		assertEquals(0.75F, a.g);
		assertEquals(1.00F, a.b);
		
		assertEquals(Float.POSITIVE_INFINITY, b.r);
		assertEquals(Float.POSITIVE_INFINITY, b.g);
		assertEquals(Float.POSITIVE_INFINITY, b.b);
		
		assertEquals(Float.NaN, c.r);
		assertEquals(Float.NaN, c.g);
		assertEquals(Float.NaN, c.b);
		
		assertEquals(-0.0F, d.r);
		assertEquals(-0.0F, d.g);
		assertEquals(-0.0F, d.b);
		
		assertEquals(0.0F, e.r);
		assertEquals(0.0F, e.g);
		assertEquals(0.0F, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.divide(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.divide(null, a));
	}
	
	@Test
	public void testDivideColor3FFloat() {
		final Color3F a = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), 2.0F);
		final Color3F b = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), 0.0F);
		final Color3F c = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), Float.NaN);
		final Color3F d = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), Float.NEGATIVE_INFINITY);
		final Color3F e = Color3F.divide(new Color3F(1.0F, 1.5F, 2.0F), Float.POSITIVE_INFINITY);
		
		assertEquals(0.50F, a.r);
		assertEquals(0.75F, a.g);
		assertEquals(1.00F, a.b);
		
		assertEquals(Float.POSITIVE_INFINITY, b.r);
		assertEquals(Float.POSITIVE_INFINITY, b.g);
		assertEquals(Float.POSITIVE_INFINITY, b.b);
		
		assertEquals(Float.NaN, c.r);
		assertEquals(Float.NaN, c.g);
		assertEquals(Float.NaN, c.b);
		
		assertEquals(-0.0F, d.r);
		assertEquals(-0.0F, d.g);
		assertEquals(-0.0F, d.b);
		
		assertEquals(0.0F, e.r);
		assertEquals(0.0F, e.g);
		assertEquals(0.0F, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.divide(null, 2.0F));
	}
	
	@Test
	public void testEqualsColor3F() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F c = new Color3F(0.0F, 0.5F, 2.0F);
		final Color3F d = new Color3F(0.0F, 2.0F, 1.0F);
		final Color3F e = new Color3F(2.0F, 0.5F, 1.0F);
		final Color3F f = null;
		
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
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F c = new Color3F(0.0F, 0.5F, 2.0F);
		final Color3F d = new Color3F(0.0F, 2.0F, 1.0F);
		final Color3F e = new Color3F(2.0F, 0.5F, 1.0F);
		final Color3F f = null;
		
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
		
		final Color3F color = Color3F.fromIntARGB(colorARGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
	}
	
	@Test
	public void testFromIntARGBToFloatB() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntARGBToFloatB(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToFloatG() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntARGBToFloatG(colorARGB));
	}
	
	@Test
	public void testFromIntARGBToFloatR() {
		final int colorARGB = ((0 & 0xFF) << 24) | ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntARGBToFloatR(colorARGB));
	}
	
	@Test
	public void testFromIntRGB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3F color = Color3F.fromIntRGB(colorRGB);
		
		assertEquals(  0, color.toIntR());
		assertEquals(128, color.toIntG());
		assertEquals(255, color.toIntB());
	}
	
	@Test
	public void testFromIntRGBToFloatB() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntRGBToFloatB(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToFloatG() {
		final int colorRGB = ((0 & 0xFF) << 16) | ((255 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntRGBToFloatG(colorRGB));
	}
	
	@Test
	public void testFromIntRGBToFloatR() {
		final int colorRGB = ((255 & 0xFF) << 16) | ((0 & 0xFF) << 8) | ((0 & 0xFF) << 0);
		
		assertEquals(1.0F, Color3F.fromIntRGBToFloatR(colorRGB));
	}
	
	@Test
	public void testGrayscaleAverage() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleAverage(a);
		
		assertEquals(0.5F, b.r);
		assertEquals(0.5F, b.g);
		assertEquals(0.5F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleB() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleB(a);
		
		assertEquals(1.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(1.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleB(null));
	}
	
	@Test
	public void testGrayscaleG() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleG(a);
		
		assertEquals(0.5F, b.r);
		assertEquals(0.5F, b.g);
		assertEquals(0.5F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleG(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.grayscaleLightness(a);
		
		assertEquals(2.0F, b.r);
		assertEquals(2.0F, b.g);
		assertEquals(2.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleMax() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.grayscaleMax(a);
		
		assertEquals(2.0F, b.r);
		assertEquals(2.0F, b.g);
		assertEquals(2.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleMax(null));
	}
	
	@Test
	public void testGrayscaleMin() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.grayscaleMin(a);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleMin(null));
	}
	
	@Test
	public void testGrayscaleR() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = Color3F.grayscaleR(a);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleR(null));
	}
	
	@Test
	public void testGrayscaleRelativeLuminance() {
		final Color3F a = new Color3F(1.0F / 0.212671F, 1.0F / 0.715160F, 1.0F / 0.072169F);
		final Color3F b = Color3F.grayscaleRelativeLuminance(a);
		
		assertEquals(3.0F, b.r);
		assertEquals(3.0F, b.g);
		assertEquals(3.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.grayscaleRelativeLuminance(null));
	}
	
	@Test
	public void testHasInfinities() {
		final Color3F a = new Color3F(Float.POSITIVE_INFINITY, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, Float.POSITIVE_INFINITY, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, Float.POSITIVE_INFINITY);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasInfinites());
		assertTrue(b.hasInfinites());
		assertTrue(c.hasInfinites());
		assertFalse(d.hasInfinites());
	}
	
	@Test
	public void testHasNaNs() {
		final Color3F a = new Color3F(Float.NaN, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, Float.NaN, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, Float.NaN);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.0F);
		
		assertTrue(a.hasNaNs());
		assertTrue(b.hasNaNs());
		assertTrue(c.hasNaNs());
		assertFalse(d.hasNaNs());
	}
	
	@Test
	public void testHashCode() {
		final Color3F a = new Color3F(0.0F, 0.5F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testInvert() {
		final Color3F a = new Color3F(0.75F, 0.75F, 0.75F);
		final Color3F b = Color3F.invert(a);
		
		assertEquals(0.25F, b.r);
		assertEquals(0.25F, b.g);
		assertEquals(0.25F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.invert(null));
	}
	
	@Test
	public void testIsBlack() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F d = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F e = new Color3F(0.0F, 1.0F, 1.0F);
		final Color3F f = new Color3F(1.0F, 1.0F, 0.0F);
		final Color3F g = new Color3F(1.0F, 1.0F, 1.0F);
		
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
		final Color3F a = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 1.0F);
		
		assertTrue(a.isBlue());
		
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueFloatFloat() {
		final Color3F a = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 1.0F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isBlue(0.5F, 0.5F));
		assertTrue(b.isBlue(0.5F, 0.5F));
		
		assertFalse(b.isBlue(0.5F, 1.0F));
		assertFalse(b.isBlue(1.0F, 0.5F));
		assertFalse(c.isBlue(0.5F, 0.5F));
	}
	
	@Test
	public void testIsCyan() {
		final Color3F a = new Color3F(0.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		
		assertFalse(c.isCyan());
		assertFalse(d.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F e = new Color3F(0.0F, 0.5F, 0.5F);
		final Color3F f = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F g = new Color3F(0.0F, 0.5F, 1.0F);
		
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
		final Color3F a = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 1.0F, 0.5F);
		
		assertTrue(a.isGreen());
		
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenFloatFloat() {
		final Color3F a = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 1.0F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isGreen(0.5F, 0.5F));
		assertTrue(b.isGreen(0.5F, 0.5F));
		
		assertFalse(b.isGreen(0.5F, 1.0F));
		assertFalse(b.isGreen(1.0F, 0.5F));
		assertFalse(c.isGreen(0.5F, 0.5F));
	}
	
	@Test
	public void testIsMagenta() {
		final Color3F a = new Color3F(1.0F, 0.0F, 1.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		
		assertFalse(c.isMagenta());
		assertFalse(d.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(1.0F, 0.5F, 0.5F);
		
		assertTrue(a.isRed());
		
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedFloatFloat() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(1.0F, 0.5F, 0.5F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		
		assertTrue(a.isRed(0.5F, 0.5F));
		assertTrue(b.isRed(0.5F, 0.5F));
		
		assertFalse(b.isRed(0.5F, 1.0F));
		assertFalse(b.isRed(1.0F, 0.5F));
		assertFalse(c.isRed(0.5F, 0.5F));
	}
	
	@Test
	public void testIsWhite() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = new Color3F(1.0F, 1.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F d = new Color3F(0.0F, 0.0F, 0.0F);
		
		assertTrue(a.isWhite());
		
		assertFalse(b.isWhite());
		assertFalse(c.isWhite());
		assertFalse(d.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color3F a = new Color3F(1.0F, 1.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.5F, 0.0F);
		final Color3F c = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		
		assertFalse(c.isYellow());
		assertFalse(d.isYellow());
	}
	
	@Test
	public void testLightness() {
		final Color3F color = new Color3F(1.0F, 2.0F, 3.0F);
		
		assertEquals(2.0F, color.lightness());
	}
	
	@Test
	public void testMax() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(2.0F, color.max());
	}
	
	@Test
	public void testMaxColor3FColor3F() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = new Color3F(1.0F, 0.0F, 2.0F);
		final Color3F c = Color3F.max(a, b);
		
		assertEquals(1.0F, c.r);
		assertEquals(1.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.max(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.max(null, b));
	}
	
	@Test
	public void testMaxTo1() {
		final Color3F a = new Color3F(1.0F, 1.0F, 2.0F);
		final Color3F b = Color3F.maxTo1(a);
		final Color3F c = Color3F.maxTo1(Color3F.WHITE);
		
		assertEquals(0.5F, b.r);
		assertEquals(0.5F, b.g);
		assertEquals(1.0F, b.b);
		
		assertEquals(Color3F.WHITE, c);
		
		assertThrows(NullPointerException.class, () -> Color3F.maxTo1(null));
	}
	
	@Test
	public void testMin() {
		final Color3F color = new Color3F(0.0F, 1.0F, 2.0F);
		
		assertEquals(0.0F, color.min());
	}
	
	@Test
	public void testMinColor3FColor3F() {
		final Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
		final Color3F b = new Color3F(1.0F, 0.0F, 2.0F);
		final Color3F c = Color3F.min(a, b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.min(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.min(null, b));
	}
	
	@Test
	public void testMinTo0() {
		final Color3F a = new Color3F(-1.0F, 0.0F, 1.0F);
		final Color3F b = Color3F.minTo0(a);
		final Color3F c = Color3F.minTo0(Color3F.WHITE);
		
		assertEquals(0.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(2.0F, b.b);
		
		assertEquals(Color3F.WHITE, c);
		
		assertThrows(NullPointerException.class, () -> Color3F.minTo0(null));
	}
	
	@Test
	public void testMultiplyAndSaturateNegative() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = Color3F.multiplyAndSaturateNegative(a, -2.0F);
		final Color3F c = Color3F.multiplyAndSaturateNegative(a, +2.0F);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(2.0F, c.r);
		assertEquals(2.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiplyAndSaturateNegative(null, 2.0F));
	}
	
	@Test
	public void testMultiplyColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = Color3F.multiply(a, b);
		
		assertEquals(2.0F, c.r);
		assertEquals(4.0F, c.g);
		assertEquals(6.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(null, b));
	}
	
	@Test
	public void testMultiplyColor3FColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = new Color3F(5.0F, 5.0F, 5.0F);
		final Color3F d = Color3F.multiply(a, b, c);
		
		assertEquals(10.0F, d.r);
		assertEquals(20.0F, d.g);
		assertEquals(30.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(null, b, c));
	}
	
	@Test
	public void testMultiplyColor3FColor3FColor3FColor3F() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = new Color3F(5.0F, 5.0F, 5.0F);
		final Color3F d = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F e = Color3F.multiply(a, b, c, d);
		
		assertEquals(20.0F, e.r);
		assertEquals(40.0F, e.g);
		assertEquals(60.0F, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(null, b, c, d));
	}
	
	@Test
	public void testMultiplyColor3FColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = new Color3F(2.0F, 2.0F, 2.0F);
		final Color3F c = Color3F.multiply(a, b, 5.0F);
		
		assertEquals(10.0F, c.r);
		assertEquals(20.0F, c.g);
		assertEquals(30.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiply(a, null, 5.0F));
		assertThrows(NullPointerException.class, () -> Color3F.multiply(null, b, 5.0F));
	}
	
	@Test
	public void testMultiplyColor3FFloat() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.multiply(a, 2.0F);
		
		assertEquals(2.0F, b.r);
		assertEquals(4.0F, b.g);
		assertEquals(6.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.multiply(null, 2.0F));
	}
	
	@Test
	public void testNegate() {
		final Color3F a = new Color3F(1.0F, 2.0F, 3.0F);
		final Color3F b = Color3F.negate(a);
		final Color3F c = Color3F.negate(b);
		
		assertEquals(-1.0F, b.r);
		assertEquals(-2.0F, b.g);
		assertEquals(-3.0F, b.b);
		
		assertEquals(+1.0F, c.r);
		assertEquals(+2.0F, c.g);
		assertEquals(+3.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.negate(null));
	}
	
	@Test
	public void testNormalize() {
		final Color3F a = Color3F.normalize(new Color3F(0.0F, 0.0F, 0.0F));
		final Color3F b = Color3F.normalize(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.3333333333333333F, b.r);
		assertEquals(0.3333333333333333F, b.g);
		assertEquals(0.3333333333333333F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.normalize(null));
	}
	
	@Test
	public void testNormalizeRelativeLuminance() {
		final Color3F a = Color3F.normalizeRelativeLuminance(new Color3F(0.0F, 0.0F, 0.0F));
		final Color3F b = Color3F.normalizeRelativeLuminance(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0F, a.r);
		assertEquals(1.0F, a.g);
		assertEquals(1.0F, a.b);
		
		assertEquals(1.0F, b.r);
		assertEquals(1.0F, b.g);
		assertEquals(1.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.normalizeRelativeLuminance(null));
	}
	
	@Test
	public void testPack() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		final int packedA = a.pack();
		final int packedB = b.pack();
		final int packedC = c.pack();
		
		final Color3F d = Color3F.unpack(packedA);
		final Color3F e = Color3F.unpack(packedB);
		final Color3F f = Color3F.unpack(packedC);
		
		assertEquals(a, d);
		assertEquals(b, e);
		assertEquals(c, f);
	}
	
	@Test
	public void testPackPackedIntComponentOrder() {
		final Color3F a = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		
		final PackedIntComponentOrder[] packedIntComponentOrders = PackedIntComponentOrder.values();
		
		for(final PackedIntComponentOrder packedIntComponentOrder : packedIntComponentOrders) {
			final int packedA = a.pack(packedIntComponentOrder);
			final int packedB = b.pack(packedIntComponentOrder);
			final int packedC = c.pack(packedIntComponentOrder);
			
			final Color3F d = Color3F.unpack(packedA, packedIntComponentOrder);
			final Color3F e = Color3F.unpack(packedB, packedIntComponentOrder);
			final Color3F f = Color3F.unpack(packedC, packedIntComponentOrder);
			
			assertEquals(a, d);
			assertEquals(b, e);
			assertEquals(c, f);
		}
		
		assertThrows(NullPointerException.class, () -> a.pack(null));
	}
	
	@Test
	public void testRandom() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.random();
			
			assertTrue(color.r >= 0.0F && color.r <= 1.0F);
			assertTrue(color.g >= 0.0F && color.g <= 1.0F);
			assertTrue(color.b >= 0.0F && color.b <= 1.0F);
		}
	}
	
	@Test
	public void testRandomBlue() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomBlue();
			
			assertTrue(color.r >= 0.0F && color.r <= 0.0F);
			assertTrue(color.g >= 0.0F && color.g <= 0.0F);
			assertTrue(color.b >  0.0F && color.b <= 1.0F);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
		}
	}
	
	@Test
	public void testRandomBlueFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomBlue(0.25F, 0.50F);
			
			assertTrue(color.r >= 0.0F && color.r <= 0.25F);
			assertTrue(color.g >= 0.0F && color.g <= 0.50F);
			assertTrue(color.b >  0.0F && color.b <= 1.00F);
			
			assertTrue(color.b > color.r);
			assertTrue(color.b > color.g);
		}
	}
	
	@Test
	public void testRandomCyan() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomCyan();
			
			assertTrue(color.r >= 0.0F && color.r <= 0.0F);
			assertTrue(color.g >  0.0F && color.g <= 1.0F);
			assertTrue(color.b >  0.0F && color.b <= 1.0F);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomCyanFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomCyan(0.50F, 0.25F);
			
			assertTrue(color.r >= 0.0F && color.r <= 0.25F);
			assertTrue(color.g >= 0.5F && color.g <= 1.00F);
			assertTrue(color.b >= 0.5F && color.b <= 1.00F);
			
			assertTrue(color.g > color.r);
			assertTrue(color.b > color.r);
			
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomGrayscale() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomGrayscale();
			
			assertTrue(color.r >= 0.0F && color.r <= 1.0F);
			assertTrue(color.g >= 0.0F && color.g <= 1.0F);
			assertTrue(color.b >= 0.0F && color.b <= 1.0F);
			
			assertEquals(color.r, color.g);
			assertEquals(color.g, color.b);
		}
	}
	
	@Test
	public void testRandomGreen() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomGreen();
			
			assertTrue(color.r >= 0.0F && color.r <= 0.0F);
			assertTrue(color.g >  0.0F && color.g <= 1.0F);
			assertTrue(color.b >= 0.0F && color.b <= 0.0F);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
		}
	}
	
	@Test
	public void testRandomGreenFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomGreen(0.25F, 0.50F);
			
			assertTrue(color.r >= 0.0F && color.r <= 0.25F);
			assertTrue(color.g >  0.0F && color.g <= 1.00F);
			assertTrue(color.b >= 0.0F && color.b <= 0.50F);
			
			assertTrue(color.g > color.r);
			assertTrue(color.g > color.b);
		}
	}
	
	@Test
	public void testRandomMagenta() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomMagenta();
			
			assertTrue(color.r >  0.0F && color.r <= 1.0F);
			assertTrue(color.g >= 0.0F && color.g <= 0.0F);
			assertTrue(color.b >  0.0F && color.b <= 1.0F);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
		}
	}
	
	@Test
	public void testRandomMagentaFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomMagenta(0.50F, 0.25F);
			
			assertTrue(color.r >= 0.5F && color.r <= 1.00F);
			assertTrue(color.g >= 0.0F && color.g <= 0.25F);
			assertTrue(color.b >= 0.5F && color.b <= 1.00F);
			
			assertTrue(color.r > color.g);
			assertTrue(color.b > color.g);
			
			assertEquals(color.r, color.b);
		}
	}
	
	@Test
	public void testRandomRed() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomRed();
			
			assertTrue(color.r >  0.0F && color.r <= 1.0F);
			assertTrue(color.g >= 0.0F && color.g <= 0.0F);
			assertTrue(color.b >= 0.0F && color.b <= 0.0F);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
		}
	}
	
	@Test
	public void testRandomRedFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomRed(0.25F, 0.50F);
			
			assertTrue(color.r >  0.0F && color.r <= 1.00F);
			assertTrue(color.g >= 0.0F && color.g <= 0.25F);
			assertTrue(color.b >= 0.0F && color.b <= 0.50F);
			
			assertTrue(color.r > color.g);
			assertTrue(color.r > color.b);
		}
	}
	
	@Test
	public void testRandomYellow() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomYellow();
			
			assertTrue(color.r >  0.0F && color.r <= 1.0F);
			assertTrue(color.g >  0.0F && color.g <= 1.0F);
			assertTrue(color.b >= 0.0F && color.b <= 0.0F);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
		}
	}
	
	@Test
	public void testRandomYellowFloatFloat() {
		for(int i = 0; i < 1000; i++) {
			final Color3F color = Color3F.randomYellow(0.50F, 0.25F);
			
			assertTrue(color.r >= 0.5F && color.r <= 1.00F);
			assertTrue(color.g >= 0.5F && color.g <= 1.00F);
			assertTrue(color.b >= 0.0F && color.b <= 0.25F);
			
			assertTrue(color.r > color.b);
			assertTrue(color.g > color.b);
			
			assertEquals(color.r, color.g);
		}
	}
	
	@Test
	public void testRead() throws IOException {
		final Color3F a = new Color3F(1.0F, 0.5F, 0.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeFloat(a.r);
		dataOutput.writeFloat(a.g);
		dataOutput.writeFloat(a.b);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3F b = Color3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Color3F.read(null));
		assertThrows(UncheckedIOException.class, () -> Color3F.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testRelativeLuminance() {
		final Color3F color = new Color3F(1.0F / 0.212671F, 1.0F / 0.715160F, 1.0F / 0.072169F);
		
		assertEquals(3.0F, color.relativeLuminance());
	}
	
	@Test
	public void testSaturateColor3F() {
		final Color3F a = Color3F.saturate(new Color3F(-1.0F, -1.0F, -1.0F));
		final Color3F b = Color3F.saturate(new Color3F(0.5F, 0.5F, 0.5F));
		final Color3F c = Color3F.saturate(new Color3F(2.0F, 2.0F, 2.0F));
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.5F, b.r);
		assertEquals(0.5F, b.g);
		assertEquals(0.5F, b.b);
		
		assertEquals(1.0F, c.r);
		assertEquals(1.0F, c.g);
		assertEquals(1.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.saturate(null));
	}
	
	@Test
	public void testSaturateColor3FFloatFloat() {
		final Color3F a = Color3F.saturate(new Color3F(-10.0F, -10.0F, -10.0F), -5.0F, +5.0F);
		final Color3F b = Color3F.saturate(new Color3F(-10.0F, -10.0F, -10.0F), +5.0F, -5.0F);
		final Color3F c = Color3F.saturate(new Color3F(2.0F, 2.0F, 2.0F), -5.0F, +5.0F);
		final Color3F d = Color3F.saturate(new Color3F(2.0F, 2.0F, 2.0F), +5.0F, -5.0F);
		final Color3F e = Color3F.saturate(new Color3F(10.0F, 10.0F, 10.0F), -5.0F, +5.0F);
		final Color3F f = Color3F.saturate(new Color3F(10.0F, 10.0F, 10.0F), +5.0F, -5.0F);
		
		assertEquals(-5.0F, a.r);
		assertEquals(-5.0F, a.g);
		assertEquals(-5.0F, a.b);
		
		assertEquals(-5.0F, b.r);
		assertEquals(-5.0F, b.g);
		assertEquals(-5.0F, b.b);
		
		assertEquals(2.0F, c.r);
		assertEquals(2.0F, c.g);
		assertEquals(2.0F, c.b);
		
		assertEquals(2.0F, d.r);
		assertEquals(2.0F, d.g);
		assertEquals(2.0F, d.b);
		
		assertEquals(5.0F, e.r);
		assertEquals(5.0F, e.g);
		assertEquals(5.0F, e.b);
		
		assertEquals(5.0F, f.r);
		assertEquals(5.0F, f.g);
		assertEquals(5.0F, f.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.saturate(null, -5.0F, +5.0F));
	}
	
	@Test
	public void testSepia() {
		final Color3F a = new Color3F(1.0F, 1.0F, 1.0F);
		final Color3F b = Color3F.sepia(a);
		
		assertEquals(1.351F, b.r);
		assertEquals(1.203F, b.g);
		assertEquals(0.937F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.sepia(null));
	}
	
	@Test
	public void testSqrt() {
		final Color3F a = new Color3F(16.0F, 25.0F, 36.0F);
		final Color3F b = Color3F.sqrt(a);
		
		assertEquals(4.0F, b.r);
		assertEquals(5.0F, b.g);
		assertEquals(6.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.sqrt(null));
	}
	
	@Test
	public void testSubtractColor3FColor3F() {
		final Color3F a = new Color3F(5.0F, 6.0F, 7.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = Color3F.subtract(a, b);
		
		assertEquals(3.0F, c.r);
		assertEquals(3.0F, c.g);
		assertEquals(3.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Color3F.subtract(null, b));
	}
	
	@Test
	public void testSubtractColor3FColor3FColor3F() {
		final Color3F a = new Color3F(10.0F, 11.0F, 12.0F);
		final Color3F b = new Color3F(5.0F, 6.0F, 7.0F);
		final Color3F c = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F d = Color3F.subtract(a, b, c);
		
		assertEquals(3.0F, d.r);
		assertEquals(2.0F, d.g);
		assertEquals(1.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.subtract(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3F.subtract(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3F.subtract(null, b, c));
	}
	
	@Test
	public void testSubtractColor3FColor3FFloat() {
		final Color3F a = new Color3F(5.0F, 6.0F, 7.0F);
		final Color3F b = new Color3F(2.0F, 3.0F, 4.0F);
		final Color3F c = Color3F.subtract(a, b, 2.0F);
		
		assertEquals(1.0F, c.r);
		assertEquals(1.0F, c.g);
		assertEquals(1.0F, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.subtract(a, null, 2.0F));
		assertThrows(NullPointerException.class, () -> Color3F.subtract(null, b, 2.0F));
	}
	
	@Test
	public void testSubtractColor3FFloat() {
		final Color3F a = new Color3F(5.0F, 6.0F, 7.0F);
		final Color3F b = Color3F.subtract(a, 2.0F);
		
		assertEquals(3.0F, b.r);
		assertEquals(4.0F, b.g);
		assertEquals(5.0F, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.subtract(null, 2.0F));
	}
	
	@Test
	public void testToIntARGB() {
		final int expectedIntARGB = ((255 & 0xFF) << 24) | ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(expectedIntARGB, color.toIntARGB());
	}
	
	@Test
	public void testToIntB() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.0F, 0.5F);
		final Color3F c = new Color3F(0.0F, 0.0F, 1.0F);
		final Color3F d = new Color3F(0.0F, 0.0F, 2.0F);
		
		assertEquals(  0, a.toIntB());
		assertEquals(128, b.toIntB());
		assertEquals(255, c.toIntB());
		assertEquals(255, d.toIntB());
	}
	
	@Test
	public void testToIntG() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.0F, 0.5F, 0.0F);
		final Color3F c = new Color3F(0.0F, 1.0F, 0.0F);
		final Color3F d = new Color3F(0.0F, 2.0F, 0.0F);
		
		assertEquals(  0, a.toIntG());
		assertEquals(128, b.toIntG());
		assertEquals(255, c.toIntG());
		assertEquals(255, d.toIntG());
	}
	
	@Test
	public void testToIntR() {
		final Color3F a = new Color3F(0.0F, 0.0F, 0.0F);
		final Color3F b = new Color3F(0.5F, 0.0F, 0.0F);
		final Color3F c = new Color3F(1.0F, 0.0F, 0.0F);
		final Color3F d = new Color3F(2.0F, 0.0F, 0.0F);
		
		assertEquals(  0, a.toIntR());
		assertEquals(128, b.toIntR());
		assertEquals(255, c.toIntR());
		assertEquals(255, d.toIntR());
	}
	
	@Test
	public void testToIntRGB() {
		final int expectedIntRGB = ((0 & 0xFF) << 16) | ((128 & 0xFF) << 8) | ((255 & 0xFF) << 0);
		
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals(expectedIntRGB, color.toIntRGB());
	}
	
	@Test
	public void testToString() {
		final Color3F color = new Color3F(0.0F, 0.5F, 1.0F);
		
		assertEquals("new Color3F(0.0F, 0.5F, 1.0F)", color.toString());
	}
	
	@Test
	public void testToneMapFilmicCurveACESModifiedVersion1() {
		final Color3F a = Color3F.toneMapFilmicCurveACESModifiedVersion1(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapFilmicCurveACESModifiedVersion1(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapFilmicCurveACESModifiedVersion1(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapFilmicCurveACESModifiedVersion1(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.8037974F, d.r);
		assertEquals(0.8037974F, d.g);
		assertEquals(0.8037974F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapFilmicCurveACESModifiedVersion1(null, 1.0F));
	}
	
	@Test
	public void testToneMapFilmicCurveColor3FFloatFloatFloatFloatFloatFloat() {
		final Color3F a = Color3F.toneMapFilmicCurve(new Color3F(0.0F, 0.0F, 0.0F), 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F);
		final Color3F b = Color3F.toneMapFilmicCurve(new Color3F(0.0F, 0.0F, 0.0F), 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F);
		final Color3F c = Color3F.toneMapFilmicCurve(new Color3F(1.0F, 1.0F, 1.0F), 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F);
		final Color3F d = Color3F.toneMapFilmicCurve(new Color3F(1.0F, 1.0F, 1.0F), 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(1.0F, d.r);
		assertEquals(1.0F, d.g);
		assertEquals(1.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapFilmicCurve(null, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F));
	}
	
	@Test
	public void testToneMapFilmicCurveColor3FFloatFloatFloatFloatFloatFloatFloatFloat() {
		final Color3F a = Color3F.toneMapFilmicCurve(new Color3F(0.0F, 0.0F, 0.0F), 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, -Float.MAX_VALUE);
		final Color3F b = Color3F.toneMapFilmicCurve(new Color3F(0.0F, 0.0F, 0.0F), 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, -Float.MAX_VALUE);
		final Color3F c = Color3F.toneMapFilmicCurve(new Color3F(1.0F, 1.0F, 1.0F), 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, 1.0F, 0.0F, -Float.MAX_VALUE);
		final Color3F d = Color3F.toneMapFilmicCurve(new Color3F(1.0F, 1.0F, 1.0F), 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -Float.MAX_VALUE);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(1.0F, d.r);
		assertEquals(1.0F, d.g);
		assertEquals(1.0F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapFilmicCurve(null, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, -Float.MAX_VALUE));
	}
	
	@Test
	public void testToneMapFilmicCurveGammaCorrection22() {
		final Color3F a = Color3F.toneMapFilmicCurveGammaCorrection22(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapFilmicCurveGammaCorrection22(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapFilmicCurveGammaCorrection22(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapFilmicCurveGammaCorrection22(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.84118825F, d.r);
		assertEquals(0.84118825F, d.g);
		assertEquals(0.84118825F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapFilmicCurveGammaCorrection22(null, 1.0F));
	}
	
	@Test
	public void testToneMapReinhard() {
		final Color3F a = Color3F.toneMapReinhard(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapReinhard(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapReinhard(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapReinhard(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.5F, d.r);
		assertEquals(0.5F, d.g);
		assertEquals(0.5F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapReinhard(null, 1.0F));
	}
	
	@Test
	public void testToneMapReinhardModifiedVersion1() {
		final Color3F a = Color3F.toneMapReinhardModifiedVersion1(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapReinhardModifiedVersion1(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapReinhardModifiedVersion1(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapReinhardModifiedVersion1(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.53125F, d.r);
		assertEquals(0.53125F, d.g);
		assertEquals(0.53125F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapReinhardModifiedVersion1(null, 1.0F));
	}
	
	@Test
	public void testToneMapReinhardModifiedVersion2() {
		final Color3F a = Color3F.toneMapReinhardModifiedVersion2(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapReinhardModifiedVersion2(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapReinhardModifiedVersion2(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapReinhardModifiedVersion2(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.63212055F, d.r);
		assertEquals(0.63212055F, d.g);
		assertEquals(0.63212055F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapReinhardModifiedVersion2(null, 1.0F));
	}
	
	@Test
	public void testToneMapUnreal3() {
		final Color3F a = Color3F.toneMapUnreal3(new Color3F(0.0F, 0.0F, 0.0F), 0.0F);
		final Color3F b = Color3F.toneMapUnreal3(new Color3F(0.0F, 0.0F, 0.0F), 1.0F);
		final Color3F c = Color3F.toneMapUnreal3(new Color3F(1.0F, 1.0F, 1.0F), 0.0F);
		final Color3F d = Color3F.toneMapUnreal3(new Color3F(1.0F, 1.0F, 1.0F), 1.0F);
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(0.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(0.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(1.0F / 1.155F * 1.019F, d.r);
		assertEquals(1.0F / 1.155F * 1.019F, d.g);
		assertEquals(1.0F / 1.155F * 1.019F, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3F.toneMapUnreal3(null, 1.0F));
	}
	
	@Test
	public void testUnpackInt() {
		final Color3F a = Color3F.unpack(PackedIntComponentOrder.ARGB.pack(  0,   0,   0));
		final Color3F b = Color3F.unpack(PackedIntComponentOrder.ARGB.pack(255,   0,   0));
		final Color3F c = Color3F.unpack(PackedIntComponentOrder.ARGB.pack(  0, 255,   0));
		final Color3F d = Color3F.unpack(PackedIntComponentOrder.ARGB.pack(  0,   0, 255));
		final Color3F e = Color3F.unpack(PackedIntComponentOrder.ARGB.pack(255, 255, 255));
		
		assertEquals(0.0F, a.r);
		assertEquals(0.0F, a.g);
		assertEquals(0.0F, a.b);
		
		assertEquals(1.0F, b.r);
		assertEquals(0.0F, b.g);
		assertEquals(0.0F, b.b);
		
		assertEquals(0.0F, c.r);
		assertEquals(1.0F, c.g);
		assertEquals(0.0F, c.b);
		
		assertEquals(0.0F, d.r);
		assertEquals(0.0F, d.g);
		assertEquals(1.0F, d.b);
		
		assertEquals(1.0F, e.r);
		assertEquals(1.0F, e.g);
		assertEquals(1.0F, e.b);
	}
	
	@Test
	public void testUnpackIntPackedIntComponentOrder() {
		for(final PackedIntComponentOrder packedIntComponentOrder : PackedIntComponentOrder.values()) {
			final Color3F a = Color3F.unpack(packedIntComponentOrder.pack(  0,   0,   0), packedIntComponentOrder);
			final Color3F b = Color3F.unpack(packedIntComponentOrder.pack(255,   0,   0), packedIntComponentOrder);
			final Color3F c = Color3F.unpack(packedIntComponentOrder.pack(  0, 255,   0), packedIntComponentOrder);
			final Color3F d = Color3F.unpack(packedIntComponentOrder.pack(  0,   0, 255), packedIntComponentOrder);
			final Color3F e = Color3F.unpack(packedIntComponentOrder.pack(255, 255, 255), packedIntComponentOrder);
			
			assertEquals(0.0F, a.r);
			assertEquals(0.0F, a.g);
			assertEquals(0.0F, a.b);
			
			assertEquals(1.0F, b.r);
			assertEquals(0.0F, b.g);
			assertEquals(0.0F, b.b);
			
			assertEquals(0.0F, c.r);
			assertEquals(1.0F, c.g);
			assertEquals(0.0F, c.b);
			
			assertEquals(0.0F, d.r);
			assertEquals(0.0F, d.g);
			assertEquals(1.0F, d.b);
			
			assertEquals(1.0F, e.r);
			assertEquals(1.0F, e.g);
			assertEquals(1.0F, e.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3F.unpack(PackedIntComponentOrder.ARGB.pack(0, 0, 0), null));
	}
	
	@Test
	public void testWrite() {
		final Color3F a = new Color3F(1.0F, 0.5F, 0.0F);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3F b = Color3F.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}