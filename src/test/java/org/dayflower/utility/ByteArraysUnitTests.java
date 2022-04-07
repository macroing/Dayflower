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
public final class ByteArraysUnitTests {
	public ByteArraysUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testConvert() {
		assertArrayEquals(new byte[] {(byte)(1), (byte)(2), (byte)(3)}, ByteArrays.convert(new int[] {1, 2, 3}));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.convert(null));
	}
	
	@Test
	public void testCreateInt() {
		assertArrayEquals(new byte[] {(byte)(0), (byte)(0), (byte)(0)}, ByteArrays.create(3));
		
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.create(-1));
	}
	
	@Test
	public void testCreateIntByte() {
		assertArrayEquals(new byte[] {(byte)(1), (byte)(1), (byte)(1)}, ByteArrays.create(3, (byte)(1)));
		
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.create(-1, (byte)(1)));
	}
	
	@Test
	public void testCreateIntByteByteByteByte() {
		assertArrayEquals(new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4), (byte)(1), (byte)(2), (byte)(3), (byte)(4)}, ByteArrays.create(8, (byte)(1), (byte)(2), (byte)(3), (byte)(4)));
		
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.create(-1, (byte)(1), (byte)(2), (byte)(3), (byte)(4)));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.create(+2, (byte)(1), (byte)(2), (byte)(3), (byte)(4)));
	}
	
	@Test
	public void testEqual() {
		final byte[] arrayA = new byte[] {(byte)(0), (byte)(1), (byte)(2), (byte)(3)};
		final byte[] arrayB = new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)};
		
		assertTrue(ByteArrays.equal(arrayA, arrayB, 1, 0, 3));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.equal(arrayA, null, 1, 0, 3));
		assertThrows(NullPointerException.class, () -> ByteArrays.equal(null, arrayB, 1, 0, 3));
		
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, -1, +0, +3));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +4, +0, +3));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +1, -1, +3));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +1, +4, +3));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +1, +0, -1));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +0, +1, +4));
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.equal(arrayA, arrayB, +0, +1, +5));
	}
	
	@Test
	public void testIndexOfByteArrayByteArrayBooleanBoolean() {
		assertEquals(+2, ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, false, false));
		assertEquals(+2, ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, false, true));
		assertEquals(+2, ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, true, false));
		assertEquals(+1, ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, true, true));
		assertEquals(-1, ByteArrays.indexOf(new byte[] {(byte)(4), (byte)(5)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, true, true));
		assertEquals(-1, ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, new byte[] {(byte)(1), (byte)(2), (byte)(3)}, false, false));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.indexOf(new byte[] {(byte)(3), (byte)(4)}, null, false, false));
		assertThrows(NullPointerException.class, () -> ByteArrays.indexOf(null, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, false, false));
		
		assertThrows(IllegalArgumentException.class, () -> ByteArrays.indexOf(new byte[] {(byte)(4), (byte)(5), (byte)(6)}, new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, true, true));
	}
	
	@Test
	public void testIndexOfByteByteArray() {
		assertEquals(+1, ByteArrays.indexOf((byte)(2), new byte[] {(byte)(1), (byte)(2), (byte)(3)}));
		assertEquals(-1, ByteArrays.indexOf((byte)(4), new byte[] {(byte)(1), (byte)(2), (byte)(3)}));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.indexOf((byte)(2), null));
	}
	
	@Test
	public void testMergeByteArrayByte() {
		assertArrayEquals(new byte[] {(byte)(1), (byte)(2), (byte)(3)}, ByteArrays.merge(new byte[] {(byte)(1), (byte)(2)}, (byte)(3)));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.merge(null, (byte)(3)));
	}
	
	@Test
	public void testMergeByteArrayByteArray() {
		assertArrayEquals(new byte[] {(byte)(1), (byte)(2), (byte)(3), (byte)(4)}, ByteArrays.merge(new byte[] {(byte)(1), (byte)(2)}, new byte[] {(byte)(3), (byte)(4)}));
		
		assertThrows(NullPointerException.class, () -> ByteArrays.merge(new byte[] {(byte)(1), (byte)(2)}, null));
		assertThrows(NullPointerException.class, () -> ByteArrays.merge(null, new byte[] {(byte)(3), (byte)(4)}));
	}
}