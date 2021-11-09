/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
	public void testReadAByteArrayInt() {
		final byte[] arrayARGB = {(byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGR = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayBGRA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		final byte[] arrayRGB = {(byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0), (byte)(0)};
		final byte[] arrayRGBA = {(byte)(0), (byte)(0), (byte)(0), (byte)(255), (byte)(0), (byte)(0), (byte)(0), (byte)(255)};
		
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readA(arrayARGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.ARGB.readA(arrayARGB, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readA(arrayBGR, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGR.readA(arrayBGR, 3));
		
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readA(arrayBGRA, 0));
		assertEquals((byte)(255), ArrayComponentOrder.BGRA.readA(arrayBGRA, 4));
		
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readA(arrayRGB, 0));
		assertEquals((byte)(255), ArrayComponentOrder.RGB.readA(arrayRGB, 3));
		
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
}