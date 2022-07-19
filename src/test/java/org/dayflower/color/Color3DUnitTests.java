/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
public final class Color3DUnitTests {
	public Color3DUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAddAndMultiplyColor3DColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = new Color3D(3.0D, 4.0D, 5.0D);
		final Color3D d = Color3D.addAndMultiply(a, b, c);
		
		assertEquals( 7.0D, d.r);
		assertEquals(14.0D, d.g);
		assertEquals(23.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(null, b, c));
	}
	
	@Test
	public void testAddAndMultiplyColor3DColor3DColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = new Color3D(3.0D, 4.0D, 5.0D);
		final Color3D d = Color3D.addAndMultiply(a, b, c, 2.0D);
		
		assertEquals(13.0D, d.r);
		assertEquals(26.0D, d.g);
		assertEquals(43.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(a, b, null, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(a, null, c, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addAndMultiply(null, b, c, 2.0D));
	}
	
	@Test
	public void testAddColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = Color3D.add(a, b);
		
		assertEquals(3.0D, c.r);
		assertEquals(5.0D, c.g);
		assertEquals(7.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.add(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.add(null, b));
	}
	
	@Test
	public void testAddColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = Color3D.add(a, 2.0D);
		
		assertEquals(3.0D, b.r);
		assertEquals(4.0D, b.g);
		assertEquals(5.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.add(null, 2.0D));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3DColor3DColor3DColor3DDoubleDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = new Color3D(3.0D, 4.0D, 5.0D);
		final Color3D d = new Color3D(4.0D, 5.0D, 6.0D);
		final Color3D e = Color3D.addMultiplyAndDivide(a, b, c, d, 2.0D, 2.0D);
		
		assertEquals( 25.0D, e.r);
		assertEquals( 62.0D, e.g);
		assertEquals(123.0D, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, b, c, null, 2.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, b, null, d, 2.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, null, c, d, 2.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(null, b, c, d, 2.0D, 2.0D));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3DColor3DColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = new Color3D(3.0D, 4.0D, 5.0D);
		final Color3D d = Color3D.addMultiplyAndDivide(a, b, c, 2.0D);
		
		assertEquals( 4.0D, d.r);
		assertEquals( 8.0D, d.g);
		assertEquals(13.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, b, null, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, null, c, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(null, b, c, 2.0D));
	}
	
	@Test
	public void testAddMultiplyAndDivideColor3DColor3DColor3DDoubleDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = new Color3D(3.0D, 4.0D, 5.0D);
		final Color3D d = Color3D.addMultiplyAndDivide(a, b, c, 2.0D, 2.0D);
		
		assertEquals( 7.0D, d.r);
		assertEquals(14.0D, d.g);
		assertEquals(23.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, b, null, 2.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(a, null, c, 2.0D, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.addMultiplyAndDivide(null, b, c, 2.0D, 2.0D));
	}
	
	@Test
	public void testAddSample() {
		final Color3D a = new Color3D(0.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.5D, 0.5D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D d = new Color3D(1.5D, 1.5D, 1.5D);
		final Color3D e = Color3D.addSample(a, c, 2);
		final Color3D f = Color3D.addSample(e, b, 3);
		final Color3D g = Color3D.addSample(f, d, 4);
		
		assertEquals(0.5D, e.r);
		assertEquals(0.5D, e.g);
		assertEquals(0.5D, e.b);
		
		assertEquals(0.5D, f.r);
		assertEquals(0.5D, f.g);
		assertEquals(0.5D, f.b);
		
		assertEquals(0.75D, g.r);
		assertEquals(0.75D, g.g);
		assertEquals(0.75D, g.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.addSample(a, null, 1));
		assertThrows(NullPointerException.class, () -> Color3D.addSample(null, b, 1));
	}
	
	@Test
	public void testArrayInt() {
		final Color3D[] colors = Color3D.array(10);
		
		assertNotNull(colors);
		assertEquals(10, colors.length);
		
		for(final Color3D color : colors) {
			assertNotNull(color);
			assertEquals(Color3D.BLACK, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3D.array(-1));
	}
	
	@Test
	public void testArrayIntSupplier() {
		final Color3D[] colors = Color3D.array(10, () -> Color3D.RED);
		
		assertNotNull(colors);
		assertEquals(10, colors.length);
		
		for(final Color3D color : colors) {
			assertNotNull(color);
			assertEquals(Color3D.RED, color);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3D.array(-1, () -> Color3D.RED));
		assertThrows(NullPointerException.class, () -> Color3D.array(10, () -> null));
		assertThrows(NullPointerException.class, () -> Color3D.array(10, null));
	}
	
	@Test
	public void testArrayRandom() {
		final Color3D[] colors = Color3D.arrayRandom(10);
		
		assertNotNull(colors);
		assertEquals(10, colors.length);
		
		for(final Color3D color : colors) {
			assertNotNull(color);
			assertTrue(color.r >= 0.0D && color.r < 1.0D);
			assertTrue(color.g >= 0.0D && color.g < 1.0D);
			assertTrue(color.b >= 0.0D && color.b < 1.0D);
		}
		
		assertThrows(IllegalArgumentException.class, () -> Color3D.arrayRandom(-1));
	}
	
	@Test
	public void testArrayReadByteArray() {
		final byte[] array = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		final Color3D[] colors = Color3D.arrayRead(array);
		
		assertNotNull(colors);
		assertEquals(3, colors.length);
		
		final Color3D a = colors[0];
		final Color3D b = colors[1];
		final Color3D c = colors[2];
		
		assertNotNull(a);
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertNotNull(b);
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertNotNull(c);
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead((byte[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color3D.arrayRead(new byte[] {(byte)(255), (byte)(255)}));
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
			final Color3D[] colors = Color3D.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			assertEquals(3, colors.length);
			
			final Color3D a = colors[0];
			final Color3D b = colors[1];
			final Color3D c = colors[2];
			
			assertNotNull(a);
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			
			assertNotNull(b);
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			
			assertNotNull(c);
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead((byte[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead(new byte[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color3D.arrayRead(new byte[] {(byte)(255), (byte)(255)}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayReadIntArray() {
		final int[] array = {255, 0, 0, 0, 255, 0, 0, 0, 255};
		
		final Color3D[] colors = Color3D.arrayRead(array);
		
		assertNotNull(colors);
		assertEquals(3, colors.length);
		
		final Color3D a = colors[0];
		final Color3D b = colors[1];
		final Color3D c = colors[2];
		
		assertNotNull(a);
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertNotNull(b);
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertNotNull(c);
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead((int[])(null)));
		assertThrows(IllegalArgumentException.class, () -> Color3D.arrayRead(new int[] {255, 255}));
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
			final Color3D[] colors = Color3D.arrayRead(arrays[i], arrayComponentOrders[i]);
			
			assertNotNull(colors);
			assertEquals(3, colors.length);
			
			final Color3D a = colors[0];
			final Color3D b = colors[1];
			final Color3D c = colors[2];
			
			assertNotNull(a);
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			
			assertNotNull(b);
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			
			assertNotNull(c);
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead((int[])(null), ArrayComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3D.arrayRead(new int[] {}, null));
		assertThrows(IllegalArgumentException.class, () -> Color3D.arrayRead(new int[] {255, 255}, ArrayComponentOrder.ARGB));
	}
	
	@Test
	public void testArrayUnpackIntArray() {
		final int[] array = {
			PackedIntComponentOrder.ARGB.pack(255,   0,   0),
			PackedIntComponentOrder.ARGB.pack(  0, 255,   0),
			PackedIntComponentOrder.ARGB.pack(  0,   0, 255)
		};
		
		final Color3D[] colors = Color3D.arrayUnpack(array);
		
		assertNotNull(colors);
		assertEquals(3, colors.length);
		
		final Color3D a = colors[0];
		final Color3D b = colors[1];
		final Color3D c = colors[2];
		
		assertNotNull(a);
		assertEquals(1.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertNotNull(b);
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertNotNull(c);
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(1.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayUnpack(null));
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
			
			final Color3D[] colors = Color3D.arrayUnpack(array, packedIntComponentOrder);
			
			assertNotNull(colors);
			assertEquals(3, colors.length);
			
			final Color3D a = colors[0];
			final Color3D b = colors[1];
			final Color3D c = colors[2];
			
			assertNotNull(a);
			assertEquals(1.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			
			assertNotNull(b);
			assertEquals(0.0D, b.r);
			assertEquals(1.0D, b.g);
			assertEquals(0.0D, b.b);
			
			assertNotNull(c);
			assertEquals(0.0D, c.r);
			assertEquals(0.0D, c.g);
			assertEquals(1.0D, c.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3D.arrayUnpack(null, PackedIntComponentOrder.ARGB));
		assertThrows(NullPointerException.class, () -> Color3D.arrayUnpack(new int[] {}, null));
	}
	
	@Test
	public void testAverage() {
		final Color3D color = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertEquals(0.5D, color.average());
	}
	
	@Test
	public void testBlendColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = new Color3D(3.0D, 3.0D, 3.0D);
		final Color3D c = Color3D.blend(a, b);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.blend(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.blend(null, b));
	}
	
	@Test
	public void testBlendColor3DColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = new Color3D(5.0D, 5.0D, 5.0D);
		final Color3D c = Color3D.blend(a, b, 0.25D);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.blend(a, null, 0.25D));
		assertThrows(NullPointerException.class, () -> Color3D.blend(null, b, 0.25D));
	}
	
	@Test
	public void testBlendColor3DColor3DDoubleDoubleDouble() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = Color3D.blend(a, b, 0.0D, 0.5D, 1.0D);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.5D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.blend(a, null, 0.0D, 0.5D, 1.0D));
		assertThrows(NullPointerException.class, () -> Color3D.blend(null, b, 0.0D, 0.5D, 1.0D));
	}
	
	@Test
	public void testConstants() {
		assertEquals(new Color3D(0.76D, 0.60D, 0.33D), Color3D.AU_AZTEK);
		assertEquals(new Color3D(0.83D, 0.69D, 0.22D), Color3D.AU_METALLIC);
		assertEquals(new Color3D(0.00D, 0.00D, 0.00D), Color3D.BLACK);
		assertEquals(new Color3D(0.00D, 0.00D, 1.00D), Color3D.BLUE);
		assertEquals(new Color3D(0.72D, 0.45D, 0.20D), Color3D.CU);
		assertEquals(new Color3D(0.00D, 1.00D, 1.00D), Color3D.CYAN);
		assertEquals(new Color3D(0.01D, 0.01D, 0.01D), Color3D.GRAY_0_01);
		assertEquals(new Color3D(0.10D, 0.10D, 0.10D), Color3D.GRAY_0_10);
		assertEquals(new Color3D(0.20D, 0.20D, 0.20D), Color3D.GRAY_0_20);
		assertEquals(new Color3D(0.25D, 0.25D, 0.25D), Color3D.GRAY_0_25);
		assertEquals(new Color3D(0.30D, 0.30D, 0.30D), Color3D.GRAY_0_30);
		assertEquals(new Color3D(0.50D, 0.50D, 0.50D), Color3D.GRAY_0_50);
		assertEquals(new Color3D(1.50D, 1.50D, 1.50D), Color3D.GRAY_1_50);
		assertEquals(new Color3D(1.55D, 1.55D, 1.55D), Color3D.GRAY_1_55);
		assertEquals(new Color3D(2.00D, 2.00D, 2.00D), Color3D.GRAY_2_00);
		assertEquals(new Color3D(0.00D, 1.00D, 0.00D), Color3D.GREEN);
		assertEquals(new Color3D(1.00D, 0.00D, 1.00D), Color3D.MAGENTA);
		assertEquals(new Color3D(1.00D, 0.50D, 0.00D), Color3D.ORANGE);
		assertEquals(new Color3D(1.00D, 0.00D, 0.00D), Color3D.RED);
		assertEquals(new Color3D(1.00D, 1.00D, 1.00D), Color3D.WHITE);
		assertEquals(new Color3D(1.00D, 1.00D, 0.00D), Color3D.YELLOW);
	}
	
	@Test
	public void testConstructor() {
		final Color3D color = new Color3D();
		
		assertEquals(0.0D, color.r);
		assertEquals(0.0D, color.g);
		assertEquals(0.0D, color.b);
	}
	
	@Test
	public void testConstructorColor3F() {
		final Color3D color = new Color3D(new Color3F(1.0F, 1.0F, 1.0F));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3D((Color3F)(null)));
	}
	
	@Test
	public void testConstructorColor4D() {
		final Color3D color = new Color3D(new Color4D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
		
		assertThrows(NullPointerException.class, () -> new Color3D((Color4D)(null)));
	}
	
	@Test
	public void testConstructorDouble() {
		final Color3D color = new Color3D(2.0D);
		
		assertEquals(2.0D, color.r);
		assertEquals(2.0D, color.g);
		assertEquals(2.0D, color.b);
	}
	
	@Test
	public void testConstructorDoubleDoubleDouble() {
		final Color3D color = new Color3D(0.0D, 1.0D, 2.0D);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(2.0D, color.b);
	}
	
	@Test
	public void testConstructorInt() {
		final Color3D color = new Color3D(255);
		
		assertEquals(1.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
	}
	
	@Test
	public void testConstructorIntIntInt() {
		final Color3D color = new Color3D(0, 255, 300);
		
		assertEquals(0.0D, color.r);
		assertEquals(1.0D, color.g);
		assertEquals(1.0D, color.b);
	}
	
	@Test
	public void testDivideColor3DColor3D() {
		final Color3D a = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), new Color3D(2.0D, 2.0D, 2.0D));
		final Color3D b = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), new Color3D(0.0D, 0.0D, 0.0D));
		final Color3D c = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), new Color3D(Double.NaN));
		final Color3D d = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), new Color3D(Double.NEGATIVE_INFINITY));
		final Color3D e = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), new Color3D(Double.POSITIVE_INFINITY));
		
		assertEquals(0.50D, a.r);
		assertEquals(0.75D, a.g);
		assertEquals(1.00D, a.b);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(0.0D, c.b);
		
		assertEquals(-0.0D, d.r);
		assertEquals(-0.0D, d.g);
		assertEquals(-0.0D, d.b);
		
		assertEquals(0.0D, e.r);
		assertEquals(0.0D, e.g);
		assertEquals(0.0D, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.divide(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.divide(null, a));
	}
	
	@Test
	public void testDivideColor3DDouble() {
		final Color3D a = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), 2.0D);
		final Color3D b = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), 0.0D);
		final Color3D c = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), Double.NaN);
		final Color3D d = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), Double.NEGATIVE_INFINITY);
		final Color3D e = Color3D.divide(new Color3D(1.0D, 1.5D, 2.0D), Double.POSITIVE_INFINITY);
		
		assertEquals(0.50D, a.r);
		assertEquals(0.75D, a.g);
		assertEquals(1.00D, a.b);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(0.0D, c.b);
		
		assertEquals(-0.0D, d.r);
		assertEquals(-0.0D, d.g);
		assertEquals(-0.0D, d.b);
		
		assertEquals(0.0D, e.r);
		assertEquals(0.0D, e.g);
		assertEquals(0.0D, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.divide(null, 2.0D));
	}
	
	@Test
	public void testEqualsColor3D() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D c = new Color3D(0.0D, 0.5D, 2.0D);
		final Color3D d = new Color3D(0.0D, 2.0D, 1.0D);
		final Color3D e = new Color3D(2.0D, 0.5D, 1.0D);
		final Color3D f = null;
		
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
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D c = new Color3D(0.0D, 0.5D, 2.0D);
		final Color3D d = new Color3D(0.0D, 2.0D, 1.0D);
		final Color3D e = new Color3D(2.0D, 0.5D, 1.0D);
		final Color3D f = null;
		
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
	public void testGrayscaleAverage() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = Color3D.grayscaleAverage(a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleAverage(null));
	}
	
	@Test
	public void testGrayscaleComponent1() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = Color3D.grayscaleComponent1(a);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleComponent1(null));
	}
	
	@Test
	public void testGrayscaleComponent2() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = Color3D.grayscaleComponent2(a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleComponent2(null));
	}
	
	@Test
	public void testGrayscaleComponent3() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = Color3D.grayscaleComponent3(a);
		
		assertEquals(1.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(1.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleComponent3(null));
	}
	
	@Test
	public void testGrayscaleLightness() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = Color3D.grayscaleLightness(a);
		
		assertEquals(2.0D, b.r);
		assertEquals(2.0D, b.g);
		assertEquals(2.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleLightness(null));
	}
	
	@Test
	public void testGrayscaleLuminance() {
		final Color3D a = new Color3D(1.0D / 0.212671D, 1.0D / 0.715160D, 1.0D / 0.072169D);
		final Color3D b = Color3D.grayscaleLuminance(a);
		
		assertEquals(3.0D, b.r);
		assertEquals(3.0D, b.g);
		assertEquals(3.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleLuminance(null));
	}
	
	@Test
	public void testGrayscaleMaximum() {
		final Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
		final Color3D b = Color3D.grayscaleMaximum(a);
		
		assertEquals(2.0D, b.r);
		assertEquals(2.0D, b.g);
		assertEquals(2.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleMaximum(null));
	}
	
	@Test
	public void testGrayscaleMinimum() {
		final Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
		final Color3D b = Color3D.grayscaleMinimum(a);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.grayscaleMinimum(null));
	}
	
	@Test
	public void testHasInfinities() {
		final Color3D a = new Color3D(Double.POSITIVE_INFINITY, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.0D, Double.POSITIVE_INFINITY, 0.0D);
		final Color3D c = new Color3D(0.0D, 0.0D, Double.POSITIVE_INFINITY);
		final Color3D d = new Color3D(0.0D, 0.0D, 0.0D);
		
		assertTrue(a.hasInfinites());
		assertTrue(b.hasInfinites());
		assertTrue(c.hasInfinites());
		assertFalse(d.hasInfinites());
	}
	
	@Test
	public void testHasNaNs() {
		final Color3D a = new Color3D(Double.NaN, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.0D, Double.NaN, 0.0D);
		final Color3D c = new Color3D(0.0D, 0.0D, Double.NaN);
		final Color3D d = new Color3D(0.0D, 0.0D, 0.0D);
		
		assertTrue(a.hasNaNs());
		assertTrue(b.hasNaNs());
		assertTrue(c.hasNaNs());
		assertFalse(d.hasNaNs());
	}
	
	@Test
	public void testHashCode() {
		final Color3D a = new Color3D(0.0D, 0.5D, 1.0D);
		final Color3D b = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertEquals(a.hashCode(), a.hashCode());
		assertEquals(a.hashCode(), b.hashCode());
	}
	
	@Test
	public void testInvert() {
		final Color3D a = new Color3D(0.75D, 0.75D, 0.75D);
		final Color3D b = Color3D.invert(a);
		
		assertEquals(0.25D, b.r);
		assertEquals(0.25D, b.g);
		assertEquals(0.25D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.invert(null));
	}
	
	@Test
	public void testIsBlack() {
		final Color3D a = new Color3D(0.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.0D, 0.0D, 1.0D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isBlack());
		assertFalse(b.isBlack());
		assertFalse(c.isBlack());
	}
	
	@Test
	public void testIsBlue() {
		final Color3D a = new Color3D(0.0D, 0.0D, 1.0D);
		final Color3D b = new Color3D(0.5D, 0.5D, 1.0D);
		
		assertTrue(a.isBlue());
		assertFalse(b.isBlue());
	}
	
	@Test
	public void testIsBlueDoubleDouble() {
		final Color3D a = new Color3D(0.0D, 0.0D, 1.0D);
		final Color3D b = new Color3D(0.5D, 0.5D, 1.0D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isBlue(0.5D, 0.5D));
		assertTrue(b.isBlue(0.5D, 0.5D));
		assertFalse(c.isBlue(0.5D, 0.5D));
	}
	
	@Test
	public void testIsCyan() {
		final Color3D a = new Color3D(0.0D, 1.0D, 1.0D);
		final Color3D b = new Color3D(0.0D, 0.5D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D d = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isCyan());
		assertTrue(b.isCyan());
		assertFalse(c.isCyan());
		assertFalse(d.isCyan());
	}
	
	@Test
	public void testIsGrayscale() {
		final Color3D a = new Color3D(0.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.5D, 0.5D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D d = new Color3D(0.0D, 0.0D, 0.5D);
		final Color3D e = new Color3D(0.0D, 0.5D, 0.5D);
		final Color3D f = new Color3D(0.0D, 0.5D, 0.0D);
		final Color3D g = new Color3D(0.0D, 0.5D, 1.0D);
		
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
		final Color3D a = new Color3D(0.0D, 1.0D, 0.0D);
		final Color3D b = new Color3D(0.5D, 1.0D, 0.5D);
		
		assertTrue(a.isGreen());
		assertFalse(b.isGreen());
	}
	
	@Test
	public void testIsGreenDoubleDouble() {
		final Color3D a = new Color3D(0.0D, 1.0D, 0.0D);
		final Color3D b = new Color3D(0.5D, 1.0D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isGreen(0.5D, 0.5D));
		assertTrue(b.isGreen(0.5D, 0.5D));
		assertFalse(c.isGreen(0.5D, 0.5D));
	}
	
	@Test
	public void testIsMagenta() {
		final Color3D a = new Color3D(1.0D, 0.0D, 1.0D);
		final Color3D b = new Color3D(0.5D, 0.0D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D d = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isMagenta());
		assertTrue(b.isMagenta());
		assertFalse(c.isMagenta());
		assertFalse(d.isMagenta());
	}
	
	@Test
	public void testIsRed() {
		final Color3D a = new Color3D(1.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(1.0D, 0.5D, 0.5D);
		
		assertTrue(a.isRed());
		assertFalse(b.isRed());
	}
	
	@Test
	public void testIsRedDoubleDouble() {
		final Color3D a = new Color3D(1.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(1.0D, 0.5D, 0.5D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		
		assertTrue(a.isRed(0.5D, 0.5D));
		assertTrue(b.isRed(0.5D, 0.5D));
		assertFalse(c.isRed(0.5D, 0.5D));
	}
	
	@Test
	public void testIsWhite() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = new Color3D(1.0D, 1.5D, 2.0D);
		final Color3D d = new Color3D(0.0D, 0.0D, 0.0D);
		
		assertTrue(a.isWhite());
		assertTrue(b.isWhite());
		assertFalse(c.isWhite());
		assertFalse(d.isWhite());
	}
	
	@Test
	public void testIsYellow() {
		final Color3D a = new Color3D(1.0D, 1.0D, 0.0D);
		final Color3D b = new Color3D(0.5D, 0.5D, 0.0D);
		final Color3D c = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D d = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertTrue(a.isYellow());
		assertTrue(b.isYellow());
		assertFalse(c.isYellow());
		assertFalse(d.isYellow());
	}
	
	@Test
	public void testLightness() {
		final Color3D color = new Color3D(1.0D, 2.0D, 3.0D);
		
		assertEquals(2.0D, color.lightness());
	}
	
	@Test
	public void testLuminance() {
		final Color3D color = new Color3D(1.0D / 0.212671D, 1.0D / 0.715160D, 1.0D / 0.072169D);
		
		assertEquals(3.0D, color.luminance());
	}
	
	@Test
	public void testMaximum() {
		final Color3D color = new Color3D(0.0D, 1.0D, 2.0D);
		
		assertEquals(2.0D, color.maximum());
	}
	
	@Test
	public void testMaximumColor3DColor3D() {
		final Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
		final Color3D b = new Color3D(1.0D, 0.0D, 2.0D);
		final Color3D c = Color3D.maximum(a, b);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.maximum(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.maximum(null, b));
	}
	
	@Test
	public void testMaximumTo1() {
		final Color3D a = new Color3D(1.0D, 1.0D, 2.0D);
		final Color3D b = Color3D.maximumTo1(a);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(1.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.maximumTo1(null));
	}
	
	@Test
	public void testMinimum() {
		final Color3D color = new Color3D(0.0D, 1.0D, 2.0D);
		
		assertEquals(0.0D, color.minimum());
	}
	
	@Test
	public void testMinimumColor3DColor3D() {
		final Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
		final Color3D b = new Color3D(1.0D, 0.0D, 2.0D);
		final Color3D c = Color3D.minimum(a, b);
		
		assertEquals(0.0D, c.r);
		assertEquals(0.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.minimum(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.minimum(null, b));
	}
	
	@Test
	public void testMinimumTo0() {
		final Color3D a = new Color3D(-1.0D, 0.0D, 1.0D);
		final Color3D b = Color3D.minimumTo0(a);
		
		assertEquals(0.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(2.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.minimumTo0(null));
	}
	
	@Test
	public void testMultiplyAndSaturateNegative() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = Color3D.multiplyAndSaturateNegative(a, -2.0D);
		final Color3D c = Color3D.multiplyAndSaturateNegative(a, +2.0D);
		
		assertEquals(0.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiplyAndSaturateNegative(null, 2.0D));
	}
	
	@Test
	public void testMultiplyColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = Color3D.multiply(a, b);
		
		assertEquals(2.0D, c.r);
		assertEquals(4.0D, c.g);
		assertEquals(6.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(null, b));
	}
	
	@Test
	public void testMultiplyColor3DColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = new Color3D(5.0D, 5.0D, 5.0D);
		final Color3D d = Color3D.multiply(a, b, c);
		
		assertEquals(10.0D, d.r);
		assertEquals(20.0D, d.g);
		assertEquals(30.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(null, b, c));
	}
	
	@Test
	public void testMultiplyColor3DColor3DColor3DColor3D() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = new Color3D(5.0D, 5.0D, 5.0D);
		final Color3D d = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D e = Color3D.multiply(a, b, c, d);
		
		assertEquals(20.0D, e.r);
		assertEquals(40.0D, e.g);
		assertEquals(60.0D, e.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, b, c, null));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, b, null, d));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, null, c, d));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(null, b, c, d));
	}
	
	@Test
	public void testMultiplyColor3DColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = new Color3D(2.0D, 2.0D, 2.0D);
		final Color3D c = Color3D.multiply(a, b, 5.0D);
		
		assertEquals(10.0D, c.r);
		assertEquals(20.0D, c.g);
		assertEquals(30.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiply(a, null, 5.0D));
		assertThrows(NullPointerException.class, () -> Color3D.multiply(null, b, 5.0D));
	}
	
	@Test
	public void testMultiplyColor3DDouble() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = Color3D.multiply(a, 2.0D);
		
		assertEquals(2.0D, b.r);
		assertEquals(4.0D, b.g);
		assertEquals(6.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.multiply(null, 2.0D));
	}
	
	@Test
	public void testNegate() {
		final Color3D a = new Color3D(1.0D, 2.0D, 3.0D);
		final Color3D b = Color3D.negate(a);
		final Color3D c = Color3D.negate(b);
		
		assertEquals(-1.0D, b.r);
		assertEquals(-2.0D, b.g);
		assertEquals(-3.0D, b.b);
		
		assertEquals(+1.0D, c.r);
		assertEquals(+2.0D, c.g);
		assertEquals(+3.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.negate(null));
	}
	
	@Test
	public void testNormalize() {
		final Color3D a = Color3D.normalize(new Color3D(0.0D, 0.0D, 0.0D));
		final Color3D b = Color3D.normalize(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(0.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertEquals(0.3333333333333333D, b.r);
		assertEquals(0.3333333333333333D, b.g);
		assertEquals(0.3333333333333333D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.normalize(null));
	}
	
	@Test
	public void testNormalizeLuminance() {
		final Color3D a = Color3D.normalizeLuminance(new Color3D(0.0D, 0.0D, 0.0D));
		final Color3D b = Color3D.normalizeLuminance(new Color3D(1.0D, 1.0D, 1.0D));
		
		assertEquals(1.0D, a.r);
		assertEquals(1.0D, a.g);
		assertEquals(1.0D, a.b);
		
		assertEquals(1.0D, b.r);
		assertEquals(1.0D, b.g);
		assertEquals(1.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.normalizeLuminance(null));
	}
	
	@Test
	public void testPack() {
		final Color3D a = new Color3D(1.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.0D, 1.0D, 0.0D);
		final Color3D c = new Color3D(0.0D, 0.0D, 1.0D);
		
		final int packedA = a.pack();
		final int packedB = b.pack();
		final int packedC = c.pack();
		
		final Color3D d = Color3D.unpack(packedA);
		final Color3D e = Color3D.unpack(packedB);
		final Color3D f = Color3D.unpack(packedC);
		
		assertEquals(a, d);
		assertEquals(b, e);
		assertEquals(c, f);
	}
	
	@Test
	public void testPackPackedIntComponentOrder() {
		final Color3D a = new Color3D(1.0D, 0.0D, 0.0D);
		final Color3D b = new Color3D(0.0D, 1.0D, 0.0D);
		final Color3D c = new Color3D(0.0D, 0.0D, 1.0D);
		
		final PackedIntComponentOrder[] packedIntComponentOrders = PackedIntComponentOrder.values();
		
		for(final PackedIntComponentOrder packedIntComponentOrder : packedIntComponentOrders) {
			final int packedA = a.pack(packedIntComponentOrder);
			final int packedB = b.pack(packedIntComponentOrder);
			final int packedC = c.pack(packedIntComponentOrder);
			
			final Color3D d = Color3D.unpack(packedA, packedIntComponentOrder);
			final Color3D e = Color3D.unpack(packedB, packedIntComponentOrder);
			final Color3D f = Color3D.unpack(packedC, packedIntComponentOrder);
			
			assertEquals(a, d);
			assertEquals(b, e);
			assertEquals(c, f);
		}
		
		assertThrows(NullPointerException.class, () -> a.pack(null));
	}
	
	@Test
	public void testRandom() {
		final Color3D color = Color3D.random();
		
		assertTrue(color.r >= 0.0D && color.r < 1.0D);
		assertTrue(color.g >= 0.0D && color.g < 1.0D);
		assertTrue(color.b >= 0.0D && color.b < 1.0D);
	}
	
	@Test
	public void testRandomComponent1() {
		final Color3D color = Color3D.randomComponent1();
		
		assertTrue(color.r >= 0.0D && color.r < 1.0D);
		assertEquals(0.0D, color.g);
		assertEquals(0.0D, color.b);
	}
	
	@Test
	public void testRandomComponent2() {
		final Color3D color = Color3D.randomComponent2();
		
		assertEquals(0.0D, color.r);
		assertTrue(color.g >= 0.0D && color.g < 1.0D);
		assertEquals(0.0D, color.b);
	}
	
	@Test
	public void testRandomComponent3() {
		final Color3D color = Color3D.randomComponent3();
		
		assertEquals(0.0D, color.r);
		assertEquals(0.0D, color.g);
		assertTrue(color.b >= 0.0D && color.b < 1.0D);
	}
	
	@Test
	public void testRead() throws IOException {
		final Color3D a = new Color3D(1.0D, 0.5D, 0.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final
		DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		dataOutput.writeDouble(a.r);
		dataOutput.writeDouble(a.g);
		dataOutput.writeDouble(a.b);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3D b = Color3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> Color3D.read(null));
		assertThrows(UncheckedIOException.class, () -> Color3D.read(new DataInputStream(new ByteArrayInputStream(new byte[] {}))));
	}
	
	@Test
	public void testSaturateColor3D() {
		final Color3D a = Color3D.saturate(new Color3D(-1.0D, -1.0D, -1.0D));
		final Color3D b = Color3D.saturate(new Color3D(0.5D, 0.5D, 0.5D));
		final Color3D c = Color3D.saturate(new Color3D(2.0D, 2.0D, 2.0D));
		
		assertEquals(0.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertEquals(0.5D, b.r);
		assertEquals(0.5D, b.g);
		assertEquals(0.5D, b.b);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(1.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.saturate(null));
	}
	
	@Test
	public void testSaturateColor3DDoubleDouble() {
		final Color3D a = Color3D.saturate(new Color3D(-10.0D, -10.0D, -10.0D), -5.0D, +5.0D);
		final Color3D b = Color3D.saturate(new Color3D(-10.0D, -10.0D, -10.0D), +5.0D, -5.0D);
		final Color3D c = Color3D.saturate(new Color3D(2.0D, 2.0D, 2.0D), -5.0D, +5.0D);
		final Color3D d = Color3D.saturate(new Color3D(2.0D, 2.0D, 2.0D), +5.0D, -5.0D);
		final Color3D e = Color3D.saturate(new Color3D(10.0D, 10.0D, 10.0D), -5.0D, +5.0D);
		final Color3D f = Color3D.saturate(new Color3D(10.0D, 10.0D, 10.0D), +5.0D, -5.0D);
		
		assertEquals(-5.0D, a.r);
		assertEquals(-5.0D, a.g);
		assertEquals(-5.0D, a.b);
		
		assertEquals(-5.0D, b.r);
		assertEquals(-5.0D, b.g);
		assertEquals(-5.0D, b.b);
		
		assertEquals(2.0D, c.r);
		assertEquals(2.0D, c.g);
		assertEquals(2.0D, c.b);
		
		assertEquals(2.0D, d.r);
		assertEquals(2.0D, d.g);
		assertEquals(2.0D, d.b);
		
		assertEquals(5.0D, e.r);
		assertEquals(5.0D, e.g);
		assertEquals(5.0D, e.b);
		
		assertEquals(5.0D, f.r);
		assertEquals(5.0D, f.g);
		assertEquals(5.0D, f.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.saturate(null, -5.0D, +5.0D));
	}
	
	@Test
	public void testSepia() {
		final Color3D a = new Color3D(1.0D, 1.0D, 1.0D);
		final Color3D b = Color3D.sepia(a);
		
		assertEquals(1.351D, b.r);
		assertEquals(1.203D, b.g);
		assertEquals(0.937D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.sepia(null));
	}
	
	@Test
	public void testSqrt() {
		final Color3D a = new Color3D(16.0D, 25.0D, 36.0D);
		final Color3D b = Color3D.sqrt(a);
		
		assertEquals(4.0D, b.r);
		assertEquals(5.0D, b.g);
		assertEquals(6.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.sqrt(null));
	}
	
	@Test
	public void testSubtractColor3DColor3D() {
		final Color3D a = new Color3D(5.0D, 6.0D, 7.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = Color3D.subtract(a, b);
		
		assertEquals(3.0D, c.r);
		assertEquals(3.0D, c.g);
		assertEquals(3.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.subtract(a, null));
		assertThrows(NullPointerException.class, () -> Color3D.subtract(null, b));
	}
	
	@Test
	public void testSubtractColor3DColor3DColor3D() {
		final Color3D a = new Color3D(10.0D, 11.0D, 12.0D);
		final Color3D b = new Color3D(5.0D, 6.0D, 7.0D);
		final Color3D c = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D d = Color3D.subtract(a, b, c);
		
		assertEquals(3.0D, d.r);
		assertEquals(2.0D, d.g);
		assertEquals(1.0D, d.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.subtract(a, b, null));
		assertThrows(NullPointerException.class, () -> Color3D.subtract(a, null, c));
		assertThrows(NullPointerException.class, () -> Color3D.subtract(null, b, c));
	}
	
	@Test
	public void testSubtractColor3DColor3DDouble() {
		final Color3D a = new Color3D(5.0D, 6.0D, 7.0D);
		final Color3D b = new Color3D(2.0D, 3.0D, 4.0D);
		final Color3D c = Color3D.subtract(a, b, 2.0D);
		
		assertEquals(1.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(1.0D, c.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.subtract(a, null, 2.0D));
		assertThrows(NullPointerException.class, () -> Color3D.subtract(null, b, 2.0D));
	}
	
	@Test
	public void testSubtractColor3DDouble() {
		final Color3D a = new Color3D(5.0D, 6.0D, 7.0D);
		final Color3D b = Color3D.subtract(a, 2.0D);
		
		assertEquals(3.0D, b.r);
		assertEquals(4.0D, b.g);
		assertEquals(5.0D, b.b);
		
		assertThrows(NullPointerException.class, () -> Color3D.subtract(null, 2.0D));
	}
	
	@Test
	public void testToString() {
		final Color3D color = new Color3D(0.0D, 0.5D, 1.0D);
		
		assertEquals("new Color3D(0.0D, 0.5D, 1.0D)", color.toString());
	}
	
	@Test
	public void testUnpackInt() {
		final Color3D a = Color3D.unpack(PackedIntComponentOrder.ARGB.pack(0, 0, 0));
		final Color3D b = Color3D.unpack(PackedIntComponentOrder.ARGB.pack(255, 0, 0));
		final Color3D c = Color3D.unpack(PackedIntComponentOrder.ARGB.pack(0, 255, 0));
		final Color3D d = Color3D.unpack(PackedIntComponentOrder.ARGB.pack(0, 0, 255));
		final Color3D e = Color3D.unpack(PackedIntComponentOrder.ARGB.pack(255, 255, 255));
		
		assertEquals(0.0D, a.r);
		assertEquals(0.0D, a.g);
		assertEquals(0.0D, a.b);
		
		assertEquals(1.0D, b.r);
		assertEquals(0.0D, b.g);
		assertEquals(0.0D, b.b);
		
		assertEquals(0.0D, c.r);
		assertEquals(1.0D, c.g);
		assertEquals(0.0D, c.b);
		
		assertEquals(0.0D, d.r);
		assertEquals(0.0D, d.g);
		assertEquals(1.0D, d.b);
		
		assertEquals(1.0D, e.r);
		assertEquals(1.0D, e.g);
		assertEquals(1.0D, e.b);
	}
	
	@Test
	public void testUnpackIntPackedIntComponentOrder() {
		for(final PackedIntComponentOrder packedIntComponentOrder : PackedIntComponentOrder.values()) {
			final Color3D a = Color3D.unpack(packedIntComponentOrder.pack(0, 0, 0), packedIntComponentOrder);
			final Color3D b = Color3D.unpack(packedIntComponentOrder.pack(255, 0, 0), packedIntComponentOrder);
			final Color3D c = Color3D.unpack(packedIntComponentOrder.pack(0, 255, 0), packedIntComponentOrder);
			final Color3D d = Color3D.unpack(packedIntComponentOrder.pack(0, 0, 255), packedIntComponentOrder);
			final Color3D e = Color3D.unpack(packedIntComponentOrder.pack(255, 255, 255), packedIntComponentOrder);
			
			assertEquals(0.0D, a.r);
			assertEquals(0.0D, a.g);
			assertEquals(0.0D, a.b);
			
			assertEquals(1.0D, b.r);
			assertEquals(0.0D, b.g);
			assertEquals(0.0D, b.b);
			
			assertEquals(0.0D, c.r);
			assertEquals(1.0D, c.g);
			assertEquals(0.0D, c.b);
			
			assertEquals(0.0D, d.r);
			assertEquals(0.0D, d.g);
			assertEquals(1.0D, d.b);
			
			assertEquals(1.0D, e.r);
			assertEquals(1.0D, e.g);
			assertEquals(1.0D, e.b);
		}
		
		assertThrows(NullPointerException.class, () -> Color3D.unpack(PackedIntComponentOrder.ARGB.pack(0, 0, 0), null));
	}
	
	@Test
	public void testWrite() {
		final Color3D a = new Color3D(1.0D, 0.5D, 0.0D);
		
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		final DataOutput dataOutput = new DataOutputStream(byteArrayOutputStream);
		
		a.write(dataOutput);
		
		final byte[] bytes = byteArrayOutputStream.toByteArray();
		
		final Color3D b = Color3D.read(new DataInputStream(new ByteArrayInputStream(bytes)));
		
		assertEquals(a, b);
		
		assertThrows(NullPointerException.class, () -> a.write(null));
		assertThrows(UncheckedIOException.class, () -> a.write(new DataOutputMock()));
	}
}