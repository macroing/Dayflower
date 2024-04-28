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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ArrayComponentOrderUnitTests {
	public ArrayComponentOrderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvertArrayComponentOrderArrayComponentOrderByteArray() {
		final byte[] arrayA = {(byte)(10), (byte)(20), (byte)(30)};
		final byte[] arrayB = ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, arrayA);
		final byte[] arrayC = ArrayComponentOrder.convert(ArrayComponentOrder.ARGB, ArrayComponentOrder.BGR, arrayB);
		
		assertEquals((byte)( 0), arrayB[0]);
		assertEquals((byte)(30), arrayB[1]);
		assertEquals((byte)(20), arrayB[2]);
		assertEquals((byte)(10), arrayB[3]);
		
		assertEquals((byte)(10), arrayC[0]);
		assertEquals((byte)(20), arrayC[1]);
		assertEquals((byte)(30), arrayC[2]);
		
		assertThrows(IllegalArgumentException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, new byte[] {(byte)(255)}));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(null, ArrayComponentOrder.ARGB, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, null, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, (byte[])(null)));
	}
	
	@Test
	public void testConvertArrayComponentOrderArrayComponentOrderDoubleArray() {
		final double[] arrayA = {10.0D, 20.0D, 30.0D};
		final double[] arrayB = ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, arrayA);
		final double[] arrayC = ArrayComponentOrder.convert(ArrayComponentOrder.ARGB, ArrayComponentOrder.BGR, arrayB);
		
		assertEquals( 1.0D, arrayB[0]);
		assertEquals(30.0D, arrayB[1]);
		assertEquals(20.0D, arrayB[2]);
		assertEquals(10.0D, arrayB[3]);
		
		assertEquals(10.0D, arrayC[0]);
		assertEquals(20.0D, arrayC[1]);
		assertEquals(30.0D, arrayC[2]);
		
		assertThrows(IllegalArgumentException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, new double[] {1.0D}));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(null, ArrayComponentOrder.ARGB, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, null, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, (double[])(null)));
	}
	
	@Test
	public void testConvertArrayComponentOrderArrayComponentOrderFloatArray() {
		final float[] arrayA = {10.0F, 20.0F, 30.0F};
		final float[] arrayB = ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, arrayA);
		final float[] arrayC = ArrayComponentOrder.convert(ArrayComponentOrder.ARGB, ArrayComponentOrder.BGR, arrayB);
		
		assertEquals( 1.0F, arrayB[0]);
		assertEquals(30.0F, arrayB[1]);
		assertEquals(20.0F, arrayB[2]);
		assertEquals(10.0F, arrayB[3]);
		
		assertEquals(10.0F, arrayC[0]);
		assertEquals(20.0F, arrayC[1]);
		assertEquals(30.0F, arrayC[2]);
		
		assertThrows(IllegalArgumentException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, new float[] {1.0F}));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(null, ArrayComponentOrder.ARGB, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, null, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, (float[])(null)));
	}
	
	@Test
	public void testConvertArrayComponentOrderArrayComponentOrderIntArray() {
		final int[] arrayA = {10, 20, 30};
		final int[] arrayB = ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, arrayA);
		final int[] arrayC = ArrayComponentOrder.convert(ArrayComponentOrder.ARGB, ArrayComponentOrder.BGR, arrayB);
		
		assertEquals( 0, arrayB[0]);
		assertEquals(30, arrayB[1]);
		assertEquals(20, arrayB[2]);
		assertEquals(10, arrayB[3]);
		
		assertEquals(10, arrayC[0]);
		assertEquals(20, arrayC[1]);
		assertEquals(30, arrayC[2]);
		
		assertThrows(IllegalArgumentException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, new int[] {255}));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(null, ArrayComponentOrder.ARGB, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, null, arrayA));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.convert(ArrayComponentOrder.BGR, ArrayComponentOrder.ARGB, (int[])(null)));
	}
	
	@Test
	public void testGetComponentCount() {
		assertEquals(4, ArrayComponentOrder.ARGB.getComponentCount());
		assertEquals(3, ArrayComponentOrder.BGR.getComponentCount());
		assertEquals(4, ArrayComponentOrder.BGRA.getComponentCount());
		assertEquals(3, ArrayComponentOrder.RGB.getComponentCount());
		assertEquals(4, ArrayComponentOrder.RGBA.getComponentCount());
	}
	
	@Test
	public void testGetOffsetA() {
		assertEquals(+0, ArrayComponentOrder.ARGB.getOffsetA());
		assertEquals(-1, ArrayComponentOrder.BGR.getOffsetA());
		assertEquals(+3, ArrayComponentOrder.BGRA.getOffsetA());
		assertEquals(-1, ArrayComponentOrder.RGB.getOffsetA());
		assertEquals(+3, ArrayComponentOrder.RGBA.getOffsetA());
	}
	
	@Test
	public void testGetOffsetB() {
		assertEquals(3, ArrayComponentOrder.ARGB.getOffsetB());
		assertEquals(0, ArrayComponentOrder.BGR.getOffsetB());
		assertEquals(0, ArrayComponentOrder.BGRA.getOffsetB());
		assertEquals(2, ArrayComponentOrder.RGB.getOffsetB());
		assertEquals(2, ArrayComponentOrder.RGBA.getOffsetB());
	}
	
	@Test
	public void testGetOffsetG() {
		assertEquals(2, ArrayComponentOrder.ARGB.getOffsetG());
		assertEquals(1, ArrayComponentOrder.BGR.getOffsetG());
		assertEquals(1, ArrayComponentOrder.BGRA.getOffsetG());
		assertEquals(1, ArrayComponentOrder.RGB.getOffsetG());
		assertEquals(1, ArrayComponentOrder.RGBA.getOffsetG());
	}
	
	@Test
	public void testGetOffsetR() {
		assertEquals(1, ArrayComponentOrder.ARGB.getOffsetR());
		assertEquals(2, ArrayComponentOrder.BGR.getOffsetR());
		assertEquals(2, ArrayComponentOrder.BGRA.getOffsetR());
		assertEquals(0, ArrayComponentOrder.RGB.getOffsetR());
		assertEquals(0, ArrayComponentOrder.RGBA.getOffsetR());
	}
	
	@Test
	public void testHasOffsetA() {
		assertTrue(ArrayComponentOrder.ARGB.hasOffsetA());
		assertFalse(ArrayComponentOrder.BGR.hasOffsetA());
		assertTrue(ArrayComponentOrder.BGRA.hasOffsetA());
		assertFalse(ArrayComponentOrder.RGB.hasOffsetA());
		assertTrue(ArrayComponentOrder.RGBA.hasOffsetA());
	}
	
	@Test
	public void testHasOffsetB() {
		assertTrue(ArrayComponentOrder.ARGB.hasOffsetB());
		assertTrue(ArrayComponentOrder.BGR.hasOffsetB());
		assertTrue(ArrayComponentOrder.BGRA.hasOffsetB());
		assertTrue(ArrayComponentOrder.RGB.hasOffsetB());
		assertTrue(ArrayComponentOrder.RGBA.hasOffsetB());
	}
	
	@Test
	public void testHasOffsetG() {
		assertTrue(ArrayComponentOrder.ARGB.hasOffsetG());
		assertTrue(ArrayComponentOrder.BGR.hasOffsetG());
		assertTrue(ArrayComponentOrder.BGRA.hasOffsetG());
		assertTrue(ArrayComponentOrder.RGB.hasOffsetG());
		assertTrue(ArrayComponentOrder.RGBA.hasOffsetG());
	}
	
	@Test
	public void testHasOffsetR() {
		assertTrue(ArrayComponentOrder.ARGB.hasOffsetR());
		assertTrue(ArrayComponentOrder.BGR.hasOffsetR());
		assertTrue(ArrayComponentOrder.BGRA.hasOffsetR());
		assertTrue(ArrayComponentOrder.RGB.hasOffsetR());
		assertTrue(ArrayComponentOrder.RGBA.hasOffsetR());
	}
	
	@Test
	public void testReadAAsInt() {
		final byte[] arrayARGB = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayRGB = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readAAsInt(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readAAsInt(arrayARGB, 4));
		
		assertEquals(0, ArrayComponentOrder.BGR.readAAsInt(arrayBGR, 0));
		assertEquals(0, ArrayComponentOrder.BGR.readAAsInt(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readAAsInt(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readAAsInt(arrayBGRA, 4));
		
		assertEquals(0, ArrayComponentOrder.RGB.readAAsInt(arrayRGB, 0));
		assertEquals(0, ArrayComponentOrder.RGB.readAAsInt(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readAAsInt(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readAAsInt(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readAAsInt(arrayARGB, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readAAsInt(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readAAsInt(null, 0));
	}
	
	@Test
	public void testReadAByteArrayInt() {
		final byte[] arrayARGB = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayRGB = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readA(arrayARGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readA(arrayARGB, 4));
		
		assertEquals((byte)(0), ArrayComponentOrder.BGR.readA(arrayBGR, 0));
		assertEquals((byte)(0), ArrayComponentOrder.BGR.readA(arrayBGR, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readA(arrayBGRA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readA(arrayBGRA, 4));
		
		assertEquals((byte)(0), ArrayComponentOrder.RGB.readA(arrayRGB, 0));
		assertEquals((byte)(0), ArrayComponentOrder.RGB.readA(arrayRGB, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readA(arrayRGBA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readA(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readA((byte[])(null), 0));
	}
	
	@Test
	public void testReadADoubleArrayInt() {
		final double[] arrayARGB = {1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D};
		final double[] arrayBGR = {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
		final double[] arrayBGRA = {0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D};
		final double[] arrayRGB = {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
		final double[] arrayRGBA = {0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D};
		
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readA(arrayARGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readA(arrayARGB, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.BGR.readA(arrayBGR, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGR.readA(arrayBGR, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readA(arrayBGRA, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readA(arrayBGRA, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.RGB.readA(arrayRGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGB.readA(arrayRGB, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readA(arrayRGBA, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readA(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readA((double[])(null), 0));
	}
	
	@Test
	public void testReadAFloatArrayInt() {
		final float[] arrayARGB = {1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F};
		final float[] arrayBGR = {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
		final float[] arrayBGRA = {0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F};
		final float[] arrayRGB = {0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F};
		final float[] arrayRGBA = {0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F};
		
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readA(arrayARGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readA(arrayARGB, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.BGR.readA(arrayBGR, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGR.readA(arrayBGR, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readA(arrayBGRA, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readA(arrayBGRA, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.RGB.readA(arrayRGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGB.readA(arrayRGB, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readA(arrayRGBA, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readA(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readA((float[])(null), 0));
	}
	
	@Test
	public void testReadAIntArrayInt() {
		final int[] arrayARGB = {255, 0, 0, 0, 255, 0, 0, 0};
		final int[] arrayBGR = {0, 0, 0, 0, 0, 0};
		final int[] arrayBGRA = {0, 0, 0, 255, 0, 0, 0, 255};
		final int[] arrayRGB = {0, 0, 0, 0, 0, 0};
		final int[] arrayRGBA = {0, 0, 0, 255, 0, 0, 0, 255};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readA(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readA(arrayARGB, 4));
		
		assertEquals(0, ArrayComponentOrder.BGR.readA(arrayBGR, 0));
		assertEquals(0, ArrayComponentOrder.BGR.readA(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readA(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readA(arrayBGRA, 4));
		
		assertEquals(0, ArrayComponentOrder.RGB.readA(arrayRGB, 0));
		assertEquals(0, ArrayComponentOrder.RGB.readA(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readA(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readA(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, -1));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readA(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readA((int[])(null), 0));
	}
	
	@Test
	public void testReadBAsInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayBGR = {(byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayRGB = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readBAsInt(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readBAsInt(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readBAsInt(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readBAsInt(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readBAsInt(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readBAsInt(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readBAsInt(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readBAsInt(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readBAsInt(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readBAsInt(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readBAsInt(arrayARGB, -4));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readBAsInt(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readBAsInt(null, 0));
	}
	
	@Test
	public void testReadBByteArrayInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayBGR = {(byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayRGB = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readB(arrayARGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readB(arrayARGB, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readB(arrayBGR, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readB(arrayBGR, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readB(arrayBGRA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readB(arrayBGRA, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readB(arrayRGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readB(arrayRGB, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readB(arrayRGBA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readB(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, -4));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readB((byte[])(null), 0));
	}
	
	@Test
	public void testReadBDoubleArrayInt() {
		final double[] arrayARGB = {0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D};
		final double[] arrayBGR = {1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D};
		final double[] arrayBGRA = {1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D};
		final double[] arrayRGB = {0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 1.0D};
		final double[] arrayRGBA = {0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D};
		
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readB(arrayARGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readB(arrayARGB, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.BGR.readB(arrayBGR, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGR.readB(arrayBGR, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readB(arrayBGRA, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readB(arrayBGRA, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.RGB.readB(arrayRGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGB.readB(arrayRGB, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readB(arrayRGBA, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readB(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, -4));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readB((double[])(null), 0));
	}
	
	@Test
	public void testReadBFloatArrayInt() {
		final float[] arrayARGB = {0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F};
		final float[] arrayBGR = {1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
		final float[] arrayBGRA = {1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F};
		final float[] arrayRGB = {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F};
		final float[] arrayRGBA = {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
		
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readB(arrayARGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readB(arrayARGB, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.BGR.readB(arrayBGR, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGR.readB(arrayBGR, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readB(arrayBGRA, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readB(arrayBGRA, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.RGB.readB(arrayRGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGB.readB(arrayRGB, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readB(arrayRGBA, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readB(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, -4));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readB((float[])(null), 0));
	}
	
	@Test
	public void testReadBIntArrayInt() {
		final int[] arrayARGB = {0, 0, 0, 255, 0, 0, 0, 255};
		final int[] arrayBGR = {255, 0, 0, 255, 0, 0};
		final int[] arrayBGRA = {255, 0, 0, 0, 255, 0, 0, 0};
		final int[] arrayRGB = {0, 0, 255, 0, 0, 255};
		final int[] arrayRGBA = {0, 0, 255, 0, 0, 0, 255, 0};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readB(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readB(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readB(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readB(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readB(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readB(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readB(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readB(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readB(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readB(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, -4));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readB(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readB((int[])(null), 0));
	}
	
	@Test
	public void testReadGAsInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayRGB = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readGAsInt(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readGAsInt(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readGAsInt(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readGAsInt(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readGAsInt(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readGAsInt(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readGAsInt(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readGAsInt(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readGAsInt(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readGAsInt(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readGAsInt(arrayARGB, -3));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readGAsInt(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readGAsInt(null, 0));
	}
	
	@Test
	public void testReadGByteArrayInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayRGB = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readG(arrayARGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readG(arrayARGB, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readG(arrayBGR, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readG(arrayBGR, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readG(arrayBGRA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readG(arrayBGRA, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readG(arrayRGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readG(arrayRGB, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readG(arrayRGBA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readG(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, -3));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readG((byte[])(null), 0));
	}
	
	@Test
	public void testReadGDoubleArrayInt() {
		final double[] arrayARGB = {0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D};
		final double[] arrayBGR = {0.0D, 1.0D, 0.0D, 0.0D, 1.0D, 0.0D};
		final double[] arrayBGRA = {0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D};
		final double[] arrayRGB = {0.0D, 1.0D, 0.0D, 0.0D, 1.0D, 0.0D};
		final double[] arrayRGBA = {0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D};
		
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readG(arrayARGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readG(arrayARGB, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.BGR.readG(arrayBGR, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGR.readG(arrayBGR, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readG(arrayBGRA, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readG(arrayBGRA, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.RGB.readG(arrayRGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGB.readG(arrayRGB, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readG(arrayRGBA, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readG(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, -3));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readG((double[])(null), 0));
	}
	
	@Test
	public void testReadGFloatArrayInt() {
		final float[] arrayARGB = {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
		final float[] arrayBGR = {0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F};
		final float[] arrayBGRA = {0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
		final float[] arrayRGB = {0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F};
		final float[] arrayRGBA = {0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
		
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readG(arrayARGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readG(arrayARGB, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.BGR.readG(arrayBGR, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGR.readG(arrayBGR, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readG(arrayBGRA, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readG(arrayBGRA, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.RGB.readG(arrayRGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGB.readG(arrayRGB, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readG(arrayRGBA, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readG(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, -3));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readG((float[])(null), 0));
	}
	
	@Test
	public void testReadGIntArrayInt() {
		final int[] arrayARGB = {0, 0, 255, 0, 0, 0, 255, 0};
		final int[] arrayBGR = {0, 255, 0, 0, 255, 0};
		final int[] arrayBGRA = {0, 255, 0, 0, 0, 255, 0, 0};
		final int[] arrayRGB = {0, 255, 0, 0, 255, 0};
		final int[] arrayRGBA = {0, 255, 0, 0, 0, 255, 0, 0};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readG(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readG(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readG(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readG(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readG(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readG(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readG(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readG(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readG(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readG(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, -3));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readG(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readG((int[])(null), 0));
	}
	
	@Test
	public void testReadRAsInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayRGB = {(byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readRAsInt(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readRAsInt(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readRAsInt(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readRAsInt(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readRAsInt(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readRAsInt(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readRAsInt(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readRAsInt(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readRAsInt(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readRAsInt(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readRAsInt(arrayARGB, -2));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readRAsInt(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readRAsInt(null, 0));
	}
	
	@Test
	public void testReadRByteArrayInt() {
		final byte[] arrayARGB = {(byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0)};
		final byte[] arrayRGB = {(byte)(255), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readR(arrayARGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readR(arrayARGB, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readR(arrayBGR, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readR(arrayBGR, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readR(arrayBGRA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readR(arrayBGRA, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readR(arrayRGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readR(arrayRGB, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readR(arrayRGBA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGBA.readR(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, -2));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readR((byte[])(null), 0));
	}
	
	@Test
	public void testReadRDoubleArrayInt() {
		final double[] arrayARGB = {0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D};
		final double[] arrayBGR = {0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 1.0D};
		final double[] arrayBGRA = {0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D};
		final double[] arrayRGB = {1.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D};
		final double[] arrayRGBA = {1.0D, 0.0D, 0.0D, 0.0D, 1.0D, 0.0D, 0.0D, 0.0D};
		
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readR(arrayARGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.ARGB.readR(arrayARGB, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.BGR.readR(arrayBGR, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGR.readR(arrayBGR, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readR(arrayBGRA, 0));
		assertEquals(1.0D, ArrayComponentOrder.BGRA.readR(arrayBGRA, 4));
		
		assertEquals(1.0D, ArrayComponentOrder.RGB.readR(arrayRGB, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGB.readR(arrayRGB, 3));
		
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readR(arrayRGBA, 0));
		assertEquals(1.0D, ArrayComponentOrder.RGBA.readR(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, -2));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readR((double[])(null), 0));
	}
	
	@Test
	public void testReadRFloatArrayInt() {
		final float[] arrayARGB = {0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
		final float[] arrayBGR = {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F};
		final float[] arrayBGRA = {0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F};
		final float[] arrayRGB = {1.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F};
		final float[] arrayRGBA = {1.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F};
		
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readR(arrayARGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.ARGB.readR(arrayARGB, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.BGR.readR(arrayBGR, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGR.readR(arrayBGR, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readR(arrayBGRA, 0));
		assertEquals(1.0F, ArrayComponentOrder.BGRA.readR(arrayBGRA, 4));
		
		assertEquals(1.0F, ArrayComponentOrder.RGB.readR(arrayRGB, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGB.readR(arrayRGB, 3));
		
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readR(arrayRGBA, 0));
		assertEquals(1.0F, ArrayComponentOrder.RGBA.readR(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, -2));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readR((float[])(null), 0));
	}
	
	@Test
	public void testReadRIntArrayInt() {
		final int[] arrayARGB = {0, 255, 0, 0, 0, 255, 0, 0};
		final int[] arrayBGR = {0, 0, 255, 0, 0, 255};
		final int[] arrayBGRA = {0, 0, 255, 0, 0, 0, 255, 0};
		final int[] arrayRGB = {255, 0, 0, 255, 0, 0};
		final int[] arrayRGBA = {255, 0, 0, 0, 255, 0, 0, 0};
		
		assertEquals(255, ArrayComponentOrder.ARGB.readR(arrayARGB, 0));
		assertEquals(255, ArrayComponentOrder.ARGB.readR(arrayARGB, 4));
		
		assertEquals(255, ArrayComponentOrder.BGR.readR(arrayBGR, 0));
		assertEquals(255, ArrayComponentOrder.BGR.readR(arrayBGR, 3));
		
		assertEquals(255, ArrayComponentOrder.BGRA.readR(arrayBGRA, 0));
		assertEquals(255, ArrayComponentOrder.BGRA.readR(arrayBGRA, 4));
		
		assertEquals(255, ArrayComponentOrder.RGB.readR(arrayRGB, 0));
		assertEquals(255, ArrayComponentOrder.RGB.readR(arrayRGB, 3));
		
		assertEquals(255, ArrayComponentOrder.RGBA.readR(arrayRGBA, 0));
		assertEquals(255, ArrayComponentOrder.RGBA.readR(arrayRGBA, 4));
		
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, -2));
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> ArrayComponentOrder.ARGB.readR(arrayARGB, +8));
		assertThrows(NullPointerException.class, () -> ArrayComponentOrder.ARGB.readR((int[])(null), 0));
	}
}