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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class PackedIntComponentOrderUnitTests {
	public PackedIntComponentOrderUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvert() {
		final int[] arrayA = new int[] {255, 1, 2, 3, 255, 4, 5, 6};
		final int[] arrayB = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, arrayA);
		final int[] arrayC = PackedIntComponentOrder.convert(PackedIntComponentOrder.ARGB, PackedIntComponentOrder.ABGR, arrayB);
		final int[] arrayD = PackedIntComponentOrder.convert(PackedIntComponentOrder.ABGR, PackedIntComponentOrder.ARGB, arrayC);
		
		assertEquals(2, arrayB.length);
		assertEquals(2, arrayC.length);
		assertEquals(2, arrayD.length);
		
		assertArrayEquals(arrayB, arrayD);
		
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.convert(PackedIntComponentOrder.ARGB, PackedIntComponentOrder.ABGR, null));
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.convert(PackedIntComponentOrder.ARGB, null, new int[0]));
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.convert(null, PackedIntComponentOrder.ABGR, new int[0]));
	}
	
	@Test
	public void testGetComponentCount() {
		assertEquals(4, PackedIntComponentOrder.ABGR.getComponentCount());
		assertEquals(4, PackedIntComponentOrder.ARGB.getComponentCount());
		assertEquals(3, PackedIntComponentOrder.BGR.getComponentCount());
		assertEquals(3, PackedIntComponentOrder.RGB.getComponentCount());
	}
	
	@Test
	public void testGetShiftA() {
		assertEquals(24, PackedIntComponentOrder.ABGR.getShiftA());
		assertEquals(24, PackedIntComponentOrder.ARGB.getShiftA());
		assertEquals(-1, PackedIntComponentOrder.BGR.getShiftA());
		assertEquals(-1, PackedIntComponentOrder.RGB.getShiftA());
	}
	
	@Test
	public void testGetShiftB() {
		assertEquals(16, PackedIntComponentOrder.ABGR.getShiftB());
		assertEquals( 0, PackedIntComponentOrder.ARGB.getShiftB());
		assertEquals(16, PackedIntComponentOrder.BGR.getShiftB());
		assertEquals( 0, PackedIntComponentOrder.RGB.getShiftB());
	}
	
	@Test
	public void testGetShiftG() {
		assertEquals(8, PackedIntComponentOrder.ABGR.getShiftG());
		assertEquals(8, PackedIntComponentOrder.ARGB.getShiftG());
		assertEquals(8, PackedIntComponentOrder.BGR.getShiftG());
		assertEquals(8, PackedIntComponentOrder.RGB.getShiftG());
	}
	
	@Test
	public void testGetShiftR() {
		assertEquals( 0, PackedIntComponentOrder.ABGR.getShiftR());
		assertEquals(16, PackedIntComponentOrder.ARGB.getShiftR());
		assertEquals( 0, PackedIntComponentOrder.BGR.getShiftR());
		assertEquals(16, PackedIntComponentOrder.RGB.getShiftR());
	}
	
	@Test
	public void testHasShiftA() {
		assertTrue(PackedIntComponentOrder.ABGR.hasShiftA());
		assertTrue(PackedIntComponentOrder.ARGB.hasShiftA());
		assertFalse(PackedIntComponentOrder.BGR.hasShiftA());
		assertFalse(PackedIntComponentOrder.RGB.hasShiftA());
	}
	
	@Test
	public void testHasShiftB() {
		assertTrue(PackedIntComponentOrder.ABGR.hasShiftB());
		assertTrue(PackedIntComponentOrder.ARGB.hasShiftB());
		assertTrue(PackedIntComponentOrder.BGR.hasShiftB());
		assertTrue(PackedIntComponentOrder.RGB.hasShiftB());
	}
	
	@Test
	public void testHasShiftG() {
		assertTrue(PackedIntComponentOrder.ABGR.hasShiftG());
		assertTrue(PackedIntComponentOrder.ARGB.hasShiftG());
		assertTrue(PackedIntComponentOrder.BGR.hasShiftG());
		assertTrue(PackedIntComponentOrder.RGB.hasShiftG());
	}
	
	@Test
	public void testHasShiftR() {
		assertTrue(PackedIntComponentOrder.ABGR.hasShiftR());
		assertTrue(PackedIntComponentOrder.ARGB.hasShiftR());
		assertTrue(PackedIntComponentOrder.BGR.hasShiftR());
		assertTrue(PackedIntComponentOrder.RGB.hasShiftR());
	}
	
	@Test
	public void testPackArrayComponentOrderByteArray() {
		final int[] arrayA = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, new byte[] {(byte)(255), (byte)(1), (byte)(2), (byte)(3), (byte)(255), (byte)(4), (byte)(5), (byte)(6)});
		final int[] arrayB = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.BGRA, new byte[] {(byte)(3), (byte)(2), (byte)(1), (byte)(255), (byte)(6), (byte)(5), (byte)(4), (byte)(255)});
		
		assertEquals(2, arrayA.length);
		assertEquals(2, arrayB.length);
		
		assertArrayEquals(arrayA, arrayB);
		
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, (byte[])(null)));
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.pack(null, new byte[0]));
		
		assertThrows(IllegalArgumentException.class, () -> PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, new byte[1]));
	}
	
	@Test
	public void testPackArrayComponentOrderIntArray() {
		final int[] arrayA = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, new int[] {255, 1, 2, 3, 255, 4, 5, 6});
		final int[] arrayB = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.BGRA, new int[] {3, 2, 1, 255, 6, 5, 4, 255});
		
		assertEquals(2, arrayA.length);
		assertEquals(2, arrayB.length);
		
		assertArrayEquals(arrayA, arrayB);
		
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, (int[])(null)));
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.pack(null, new int[0]));
		
		assertThrows(IllegalArgumentException.class, () -> PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, new int[1]));
	}
	
	@Test
	public void testPackIntIntInt() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30);
		
		final int colorAExpected = ((10 & 0xFF) <<  0) | ((20 & 0xFF) << 8) | ((30 & 0xFF) << 16);
		final int colorBExpected = ((10 & 0xFF) << 16) | ((20 & 0xFF) << 8) | ((30 & 0xFF) <<  0);
		final int colorCExpected = ((10 & 0xFF) <<  0) | ((20 & 0xFF) << 8) | ((30 & 0xFF) << 16);
		final int colorDExpected = ((10 & 0xFF) << 16) | ((20 & 0xFF) << 8) | ((30 & 0xFF) <<  0);
		
		assertEquals(colorAExpected, colorA);
		assertEquals(colorBExpected, colorB);
		assertEquals(colorCExpected, colorC);
		assertEquals(colorDExpected, colorD);
	}
	
	@Test
	public void testPackIntIntIntInt() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30, 40);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30, 40);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30, 40);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30, 40);
		
		final int colorAExpected = ((10 & 0xFF) <<  0) | ((20 & 0xFF) << 8) | ((30 & 0xFF) << 16) | ((40 & 0xFF) << 24);
		final int colorBExpected = ((10 & 0xFF) << 16) | ((20 & 0xFF) << 8) | ((30 & 0xFF) <<  0) | ((40 & 0xFF) << 24);
		final int colorCExpected = ((10 & 0xFF) <<  0) | ((20 & 0xFF) << 8) | ((30 & 0xFF) << 16);
		final int colorDExpected = ((10 & 0xFF) << 16) | ((20 & 0xFF) << 8) | ((30 & 0xFF) <<  0);
		
		assertEquals(colorAExpected, colorA);
		assertEquals(colorBExpected, colorB);
		assertEquals(colorCExpected, colorC);
		assertEquals(colorDExpected, colorD);
	}
	
	@Test
	public void testUnpackA() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30, 40);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30, 40);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30, 40);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30, 40);
		
		final int colorAComponentA = PackedIntComponentOrder.ABGR.unpackA(colorA);
		final int colorBComponentA = PackedIntComponentOrder.ARGB.unpackA(colorB);
		final int colorCComponentA = PackedIntComponentOrder.BGR.unpackA(colorC);
		final int colorDComponentA = PackedIntComponentOrder.RGB.unpackA(colorD);
		
		assertEquals( 40, colorAComponentA);
		assertEquals( 40, colorBComponentA);
		assertEquals(255, colorCComponentA);
		assertEquals(255, colorDComponentA);
	}
	
	@Test
	public void testUnpackArrayComponentOrderIntArray() {
		final int[] arrayA = new int[] {255, 1, 2, 3, 255, 4, 5, 6};
		final int[] arrayB = PackedIntComponentOrder.ARGB.pack(ArrayComponentOrder.ARGB, arrayA);
		final int[] arrayC = PackedIntComponentOrder.ARGB.unpack(ArrayComponentOrder.ARGB, arrayB);
		final int[] arrayD = new int[] {1, 2, 3, 4, 5, 6};
		final int[] arrayE = PackedIntComponentOrder.RGB.pack(ArrayComponentOrder.RGB, arrayD);
		final int[] arrayF = PackedIntComponentOrder.RGB.unpack(ArrayComponentOrder.RGB, arrayE);
		
		assertEquals(8, arrayC.length);
		assertEquals(6, arrayF.length);
		
		assertArrayEquals(arrayA, arrayC);
		assertArrayEquals(arrayD, arrayF);
		
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.unpack(ArrayComponentOrder.ARGB, null));
		assertThrows(NullPointerException.class, () -> PackedIntComponentOrder.ARGB.unpack(null, new int[0]));
	}
	
	@Test
	public void testUnpackB() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30, 40);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30, 40);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30, 40);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30, 40);
		
		final int colorAComponentB = PackedIntComponentOrder.ABGR.unpackB(colorA);
		final int colorBComponentB = PackedIntComponentOrder.ARGB.unpackB(colorB);
		final int colorCComponentB = PackedIntComponentOrder.BGR.unpackB(colorC);
		final int colorDComponentB = PackedIntComponentOrder.RGB.unpackB(colorD);
		
		assertEquals(30, colorAComponentB);
		assertEquals(30, colorBComponentB);
		assertEquals(30, colorCComponentB);
		assertEquals(30, colorDComponentB);
	}
	
	@Test
	public void testUnpackG() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30, 40);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30, 40);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30, 40);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30, 40);
		
		final int colorAComponentG = PackedIntComponentOrder.ABGR.unpackG(colorA);
		final int colorBComponentG = PackedIntComponentOrder.ARGB.unpackG(colorB);
		final int colorCComponentG = PackedIntComponentOrder.BGR.unpackG(colorC);
		final int colorDComponentG = PackedIntComponentOrder.RGB.unpackG(colorD);
		
		assertEquals(20, colorAComponentG);
		assertEquals(20, colorBComponentG);
		assertEquals(20, colorCComponentG);
		assertEquals(20, colorDComponentG);
	}
	
	@Test
	public void testUnpackR() {
		final int colorA = PackedIntComponentOrder.ABGR.pack(10, 20, 30, 40);
		final int colorB = PackedIntComponentOrder.ARGB.pack(10, 20, 30, 40);
		final int colorC = PackedIntComponentOrder.BGR.pack(10, 20, 30, 40);
		final int colorD = PackedIntComponentOrder.RGB.pack(10, 20, 30, 40);
		
		final int colorAComponentR = PackedIntComponentOrder.ABGR.unpackR(colorA);
		final int colorBComponentR = PackedIntComponentOrder.ARGB.unpackR(colorB);
		final int colorCComponentR = PackedIntComponentOrder.BGR.unpackR(colorC);
		final int colorDComponentR = PackedIntComponentOrder.RGB.unpackR(colorD);
		
		assertEquals(10, colorAComponentR);
		assertEquals(10, colorBComponentR);
		assertEquals(10, colorCComponentR);
		assertEquals(10, colorDComponentR);
	}
}