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
package org.dayflower.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class LongsUnitTests {
	public LongsUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testReverseBits() {
		final long a = 1234L;
		final long b = Longs.reverseBits(a);
		final long c = Longs.reverseBits(b);
		
		final String stringA = Long.toBinaryString(a);
		final String stringB = Long.toBinaryString(b);
		final String stringC = Long.toBinaryString(c);
		
		assertEquals(a, c);
		
		assertEquals("10011010010", stringA);
		assertEquals("100101100100000000000000000000000000000000000000000000000000000", stringB);
		assertEquals("10011010010", stringC);
	}
}