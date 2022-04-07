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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
}