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
}