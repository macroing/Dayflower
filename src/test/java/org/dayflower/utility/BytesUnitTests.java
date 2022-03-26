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
public final class BytesUnitTests {
	public BytesUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testToByte() {
		final byte expectedA = 0;
		final byte actualA = Bytes.toByte(0);
		
		final byte expectedB = 127;
		final byte actualB = Bytes.toByte(127);
		
		final byte expectedC = -128;
		final byte actualC = Bytes.toByte(128);
		
		final byte expectedD = -1;
		final byte actualD = Bytes.toByte(255);
		
		assertEquals(expectedA, actualA);
		assertEquals(expectedB, actualB);
		assertEquals(expectedC, actualC);
		assertEquals(expectedD, actualD);
	}
}