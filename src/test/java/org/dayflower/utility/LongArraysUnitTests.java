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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class LongArraysUnitTests {
	public LongArraysUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvertByteArray() {
		assertArrayEquals(new long[] {1L, 2L, 3L}, LongArrays.convert(new byte[] {1, 2, 3}));
		
		assertThrows(NullPointerException.class, () -> LongArrays.convert(null));
	}
	
	@Test
	public void testConvertByteArrayBoolean() {
		assertArrayEquals(new long[] {1L, 2L, 3L}, LongArrays.convert(new byte[] {1, 2, 3}, false));
		assertArrayEquals(new long[] {-11L, -6L, -1L}, LongArrays.convert(new byte[] {(byte)(245), (byte)(250), (byte)(255)}, false));
		assertArrayEquals(new long[] {245L, 250L, 255L}, LongArrays.convert(new byte[] {(byte)(245), (byte)(250), (byte)(255)}, true));
		
		assertThrows(NullPointerException.class, () -> LongArrays.convert(null, false));
	}
	
	@Test
	public void testCreateInt() {
		assertArrayEquals(new long[] {0L, 0L, 0L}, LongArrays.create(3));
		
		assertThrows(IllegalArgumentException.class, () -> LongArrays.create(-1));
	}
	
	@Test
	public void testCreateIntLong() {
		assertArrayEquals(new long[] {1L, 1L, 1L}, LongArrays.create(3, 1L));
		
		assertThrows(IllegalArgumentException.class, () -> LongArrays.create(-1, 1L));
	}
	
	@Test
	public void testCreateIntLongSupplier() {
		assertArrayEquals(new long[] {1L, 1L, 1L}, LongArrays.create(3, () -> 1L));
		
		assertThrows(IllegalArgumentException.class, () -> LongArrays.create(-1, () -> 1L));
		assertThrows(NullPointerException.class, () -> LongArrays.create(3, null));
	}
	
	@Test
	public void testEqual() {
		final long[] arrayA = new long[] {0L, 1L, 2L, 3L};
		final long[] arrayB = new long[] {1L, 2L, 3L, 4L};
		
		assertTrue(LongArrays.equal(arrayA, arrayB, 1, 0, 3));
		
		assertThrows(NullPointerException.class, () -> LongArrays.equal(arrayA, null, 1, 0, 3));
		assertThrows(NullPointerException.class, () -> LongArrays.equal(null, arrayB, 1, 0, 3));
		
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, -1, +0, +3));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +4, +0, +3));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +1, -1, +3));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +1, +4, +3));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +1, +0, -1));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +0, +1, +4));
		assertThrows(IllegalArgumentException.class, () -> LongArrays.equal(arrayA, arrayB, +0, +1, +5));
	}
	
	@Test
	public void testIndexOfLongArrayLongArrayBooleanBoolean() {
		assertEquals(+2, LongArrays.indexOf(new long[] {3L, 4L}, new long[] {1L, 2L, 3L, 4L}, false, false));
		assertEquals(+2, LongArrays.indexOf(new long[] {3L, 4L}, new long[] {1L, 2L, 3L, 4L}, false, true));
		assertEquals(+2, LongArrays.indexOf(new long[] {3L, 4L}, new long[] {1L, 2L, 3L, 4L}, true, false));
		assertEquals(+1, LongArrays.indexOf(new long[] {3L, 4L}, new long[] {1L, 2L, 3L, 4L}, true, true));
		assertEquals(-1, LongArrays.indexOf(new long[] {4L, 5L}, new long[] {1L, 2L, 3L, 4L}, true, true));
		assertEquals(-1, LongArrays.indexOf(new long[] {3L, 4L}, new long[] {1L, 2L, 3L}, false, false));
		
		assertThrows(NullPointerException.class, () -> LongArrays.indexOf(new long[] {3L, 4L}, null, false, false));
		assertThrows(NullPointerException.class, () -> LongArrays.indexOf(null, new long[] {1L, 2L, 3L, 4L}, false, false));
		
		assertThrows(IllegalArgumentException.class, () -> LongArrays.indexOf(new long[] {4L, 5L, 6L}, new long[] {1L, 2L, 3L, 4L}, true, true));
	}
	
	@Test
	public void testIndexOfLongLongArray() {
		assertEquals(+1, LongArrays.indexOf(2L, new long[] {1L, 2L, 3L}));
		assertEquals(-1, LongArrays.indexOf(4L, new long[] {1L, 2L, 3L}));
		
		assertThrows(NullPointerException.class, () -> LongArrays.indexOf(2L, null));
	}
	
	@Test
	public void testMergeLongArrayLong() {
		assertArrayEquals(new long[] {1L, 2L, 3L}, LongArrays.merge(new long[] {1L, 2L}, 3L));
		
		assertThrows(NullPointerException.class, () -> LongArrays.merge(null, 3L));
	}
	
	@Test
	public void testMergeLongArrayLongArray() {
		assertArrayEquals(new long[] {1L, 2L, 3L, 4L}, LongArrays.merge(new long[] {1L, 2L}, new long[] {3L, 4L}));
		
		assertThrows(NullPointerException.class, () -> LongArrays.merge(new long[] {1L, 2L}, null));
		assertThrows(NullPointerException.class, () -> LongArrays.merge(null, new long[] {3L, 4L}));
	}
	
	@Test
	public void testMergeLongArrays() {
		assertArrayEquals(new long[] {1L, 2L, 3L, 4L, 5L, 6L}, LongArrays.merge(new long[] {1L, 2L}, new long[] {3L, 4L}, new long[] {5L, 6L}));
		
		assertThrows(NullPointerException.class, () -> LongArrays.merge(new long[] {1L, 2L}, new long[] {3L, 4L}, null));
		assertThrows(NullPointerException.class, () -> LongArrays.merge(new long[] {1L, 2L}, null, new long[] {5L, 6L}));
		assertThrows(NullPointerException.class, () -> LongArrays.merge(null, new long[] {3L, 4L}, new long[] {5L, 6L}));
		assertThrows(NullPointerException.class, () -> LongArrays.merge((long[])(null)));
	}
}