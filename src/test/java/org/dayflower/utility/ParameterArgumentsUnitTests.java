/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class ParameterArgumentsUnitTests {
	public ParameterArgumentsUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testRequireExact() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExact(1, 1, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireExact(1, 2, "value"));
		
		assertEquals(1, ParameterArguments.requireExact(1, 1, "value"));
	}
	
	@Test
	public void testRequireExactArrayLengthByteArrayIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength((byte[])(null), 1, "array"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength(new byte[1], 1, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireExactArrayLength(new byte[1], 2, "array"));
		
		assertArrayEquals(new byte[1], ParameterArguments.requireExactArrayLength(new byte[1], 1, "array"));
	}
	
	@Test
	public void testRequireExactArrayLengthDoubleArrayIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength((double[])(null), 1, "array"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength(new double[1], 1, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireExactArrayLength(new double[1], 2, "array"));
		
		assertArrayEquals(new double[1], ParameterArguments.requireExactArrayLength(new double[1], 1, "array"));
	}
	
	@Test
	public void testRequireExactArrayLengthFloatArrayIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength((float[])(null), 1, "array"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength(new float[1], 1, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireExactArrayLength(new float[1], 2, "array"));
		
		assertArrayEquals(new float[1], ParameterArguments.requireExactArrayLength(new float[1], 1, "array"));
	}
	
	@Test
	public void testRequireExactArrayLengthIntArrayIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength((int[])(null), 1, "array"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireExactArrayLength(new int[1], 1, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireExactArrayLength(new int[1], 2, "array"));
		
		assertArrayEquals(new int[1], ParameterArguments.requireExactArrayLength(new int[1], 1, "array"));
	}
	
	@Test
	public void testRequireFiniteValueDoubleString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireFiniteValue(0.0D, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Double.NaN, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Double.NEGATIVE_INFINITY, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Double.POSITIVE_INFINITY, "value"));
		
		assertEquals(0.0D, ParameterArguments.requireFiniteValue(0.0D, "value"));
	}
	
	@Test
	public void testRequireFiniteValueFloatString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireFiniteValue(0.0F, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Float.NaN, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Float.NEGATIVE_INFINITY, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireFiniteValue(Float.POSITIVE_INFINITY, "value"));
		
		assertEquals(0.0F, ParameterArguments.requireFiniteValue(0.0F, "value"));
	}
	
	@Test
	public void testRequireNonNullArray() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullArray(null, "array"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullArray(new String[] {}, null));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullArray(new String[] {"A", null, "C"}, "array"));
		
		assertArrayEquals(new String[] {"A", "B", "C"}, ParameterArguments.requireNonNullArray(new String[] {"A", "B", "C"}, "array"));
	}
	
	@Test
	public void testRequireNonNullList() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullList(null, "list"));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullList(new ArrayList<>(), null));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireNonNullList(Arrays.asList("A", null, "C"), "list"));
		
		assertEquals(Arrays.asList("A", "B", "C"), ParameterArguments.requireNonNullList(Arrays.asList("A", "B", "C"), "list"));
	}
	
	@Test
	public void testRequireRangeDoubleDoubleDoubleString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRange(0.0D, 0.0D, 0.0D, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0.0D, 1.0D, 2.0D, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0.0D, 2.0D, 1.0D, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3.0D, 1.0D, 2.0D, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3.0D, 2.0D, 1.0D, "value"));
		
		assertEquals(0.0D, ParameterArguments.requireRange(0.0D, 0.0D, 1.0D, "value"));
	}
	
	@Test
	public void testRequireRangeFloatFloatFloatString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRange(0.0F, 0.0F, 0.0F, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0.0F, 1.0F, 2.0F, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0.0F, 2.0F, 1.0F, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3.0F, 1.0F, 2.0F, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3.0F, 2.0F, 1.0F, "value"));
		
		assertEquals(0.0F, ParameterArguments.requireRange(0.0F, 0.0F, 1.0F, "value"));
	}
	
	@Test
	public void testRequireRangeIntArrayIntIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRange(new int[0], 0, 0, null));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRange(null, 0, 0, "array"));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(new int[] {0, 1, 2}, 1, 2, "array"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(new int[] {0, 1, 2}, 2, 1, "array"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(new int[] {1, 2, 3}, 1, 2, "array"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(new int[] {1, 2, 3}, 2, 1, "array"));
		
		assertArrayEquals(new int[] {1, 2}, ParameterArguments.requireRange(new int[] {1, 2}, 1, 2, "array"));
	}
	
	@Test
	public void testRequireRangeIntIntIntString() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRange(0, 0, 0, null));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0, 1, 2, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(0, 2, 1, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3, 1, 2, "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRange(3, 2, 1, "value"));
		
		assertEquals(0, ParameterArguments.requireRange(0, 0, 1, "value"));
	}
	
	@Test
	public void testRequireRangef() {
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRangef(0, 0, 0, null));
		assertThrows(NullPointerException.class, () -> ParameterArguments.requireRangef(0, 0, 0, "", (Object[])(null)));
		
		assertThrows(IllegalFormatException.class, () -> ParameterArguments.requireRangef(0, 1, 2, "%s"));
		
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRangef(0, 1, 2, "%s", "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRangef(0, 2, 1, "%s", "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRangef(3, 1, 2, "%s", "value"));
		assertThrows(IllegalArgumentException.class, () -> ParameterArguments.requireRangef(3, 2, 1, "%s", "value"));
		
		assertEquals(0, ParameterArguments.requireRangef(0, 0, 1, "%s", "value"));
	}
}