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
public final class DoubleArraysUnitTests {
	public DoubleArraysUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testCreateIntDoubleDoubleDoubleDouble() {
		assertArrayEquals(new double[] {1.0D, 2.0D, 3.0D, 4.0D, 1.0D, 2.0D, 3.0D, 4.0D}, DoubleArrays.create(8, 1.0D, 2.0D, 3.0D, 4.0D));
		
		assertThrows(IllegalArgumentException.class, () -> DoubleArrays.create(-1, 1.0D, 2.0D, 3.0D, 4.0D));
		assertThrows(IllegalArgumentException.class, () -> DoubleArrays.create(+2, 1.0D, 2.0D, 3.0D, 4.0D));
	}
}