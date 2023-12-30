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
package org.dayflower.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class IntsUnitTests {
	public IntsUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testAbs() {
		assertEquals(1, Ints.abs(+1));
		assertEquals(1, Ints.abs(-1));
	}
	
	@Test
	public void testConstants() {
		assertEquals(Integer.MAX_VALUE, Ints.MAX_VALUE);
		assertEquals(Integer.MIN_VALUE, Ints.MIN_VALUE);
	}
	
	@Test
	public void testGetPrimeNumberAt() {
		assertEquals(2, Ints.getPrimeNumberAt(0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.getPrimeNumberAt(-1));
		assertThrows(IllegalArgumentException.class, () -> Ints.getPrimeNumberAt(2024));
	}
	
	@Test
	public void testGetPrimeNumberCount() {
		assertEquals(1024, Ints.getPrimeNumberCount());
	}
	
	@Test
	public void testMaxIntInt() {
		assertEquals(2, Ints.max(1, 2));
	}
	
	@Test
	public void testMaxIntIntInt() {
		assertEquals(3, Ints.max(1, 2, 3));
	}
	
	@Test
	public void testMaxIntIntIntInt() {
		assertEquals(4, Ints.max(1, 2, 3, 4));
	}
	
	@Test
	public void testMinIntInt() {
		assertEquals(1, Ints.min(1, 2));
	}
	
	@Test
	public void testMinIntIntInt() {
		assertEquals(1, Ints.min(1, 2, 3));
	}
	
	@Test
	public void testMinIntIntIntInt() {
		assertEquals(1, Ints.min(1, 2, 3, 4));
	}
	
	@Test
	public void testPackIntInt() {
		assertEquals(0xFFFFFFFF, Ints.pack(65535, 65535));
		assertEquals(0x00000000, Ints.pack(    0,     0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(    -1, +0));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+65536, +0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0,     -1));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +65536));
	}
	
	@Test
	public void testPackIntIntIntInt() {
		assertEquals(0xFFFFFFFF, Ints.pack(255, 255, 255, 255));
		assertEquals(0x00000000, Ints.pack(  0,   0,   0,   0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(  -1, +0, +0, +0));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+256, +0, +0, +0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0,   -1, +0, +0));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +256, +0, +0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +0,   -1, +0));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +0, +256, +0));
		
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +0, +0,   -1));
		assertThrows(IllegalArgumentException.class, () -> Ints.pack(+0, +0, +0, +256));
	}
	
	@Test
	public void testPaddingInt() {
		assertEquals(0, Ints.padding(16));
		assertEquals(1, Ints.padding(15));
	}
	
	@Test
	public void testPaddingIntInt() {
		assertEquals(0, Ints.padding(16, 8));
		assertEquals(1, Ints.padding(15, 8));
	}
	
	@Test
	public void testPositiveModulo() {
		assertEquals(-0, Ints.positiveModulo(-2, -1));
		assertEquals(-2, Ints.positiveModulo(-2, -3));
		
		assertEquals(+0, Ints.positiveModulo(-2, +1));
		
		assertEquals(+0, Ints.positiveModulo(+2, -1));
		
		assertEquals(+0, Ints.positiveModulo(+2, +1));
		assertEquals(+2, Ints.positiveModulo(+2, +3));
	}
	
	@Test
	public void testReverseBits() {
		final int a = 1234;
		final int b = Ints.reverseBits(a);
		final int c = Ints.reverseBits(b);
		
		final String stringA = Integer.toBinaryString(a);
		final String stringB = Integer.toBinaryString(b);
		final String stringC = Integer.toBinaryString(c);
		
		assertEquals(a, c);
		
		assertEquals("10011010010", stringA);
		assertEquals("1001011001000000000000000000000", stringB);
		assertEquals("10011010010", stringC);
	}
	
	@Test
	public void testSaturateInt() {
		assertEquals(  0, Ints.saturate(-  1));
		assertEquals(128, Ints.saturate(+128));
		assertEquals(255, Ints.saturate(+256));
	}
	
	@Test
	public void testSaturateIntIntInt() {
		assertEquals(20, Ints.saturate(10, 20, 30));
		assertEquals(20, Ints.saturate(10, 30, 20));
		
		assertEquals(25, Ints.saturate(25, 20, 30));
		assertEquals(25, Ints.saturate(25, 30, 20));
		
		assertEquals(30, Ints.saturate(40, 20, 30));
		assertEquals(30, Ints.saturate(40, 30, 20));
	}
	
	@Test
	public void testToIntDouble() {
		assertEquals(2, Ints.toInt(2.0D));
	}
	
	@Test
	public void testToIntFloat() {
		assertEquals(2, Ints.toInt(2.0F));
	}
	
	@Test
	public void testUnpack() {
		final int[] a = Ints.unpack(0xFFFFFFFF);
		final int[] b = Ints.unpack(0xFFFF0000);
		final int[] c = Ints.unpack(0x00000000);
		
		assertNotNull(a);
		assertNotNull(b);
		assertNotNull(c);
		
		assertEquals(2, a.length);
		assertEquals(2, b.length);
		assertEquals(2, c.length);
		
		assertEquals(65535, a[0]);
		assertEquals(65535, a[1]);
		
		assertEquals(65535, b[0]);
		assertEquals(    0, b[1]);
		
		assertEquals(0, c[0]);
		assertEquals(0, c[1]);
	}
}