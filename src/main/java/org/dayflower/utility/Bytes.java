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
package org.dayflower.utility;

import java.util.Arrays;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code byte} values.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Bytes {
	private Bytes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code byte} representation of an {@code int} value.
	 * 
	 * @param value an {@code int} value
	 * @return a {@code byte} representation of an {@code int} value
	 */
	public static byte toByte(final int value) {
		return (byte)(value);
	}
	
	/**
	 * Returns a {@code byte[]} with a length of {@code length} and is filled with {@code 0}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Bytes.array(length, Bytes.toByte(0));
	 * }
	 * </pre>
	 * 
	 * @param length the length of the {@code byte[]}
	 * @return a {@code byte[]} with a length of {@code length} and is filled with {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static byte[] array(final int length) {
		return array(length, toByte(0));
	}
	
	/**
	 * Returns a {@code byte[]} with a length of {@code length} and is filled with {@code value}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param length the length of the {@code byte[]}
	 * @param value the {@code byte} value to fill the {@code byte[]} with
	 * @return a {@code byte[]} with a length of {@code length} and is filled with {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static byte[] array(final int length, final byte value) {
		final byte[] array = new byte[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Arrays.fill(array, value);
		
		return array;
	}
	
	/**
	 * Returns a {@code byte[]} with a length of {@code length} and is filled with {@code value0}, {@code value1}, {@code value2} and {@code value3} in a repeated pattern.
	 * <p>
	 * If {@code length} is less than {@code 0} or it cannot be evenly divided by {@code 4}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param length the length of the {@code byte[]}
	 * @param value0 the {@code byte} at the relative offset {@code 0}
	 * @param value1 the {@code byte} at the relative offset {@code 1}
	 * @param value2 the {@code byte} at the relative offset {@code 2}
	 * @param value3 the {@code byte} at the relative offset {@code 3}
	 * @return a {@code byte[]} with a length of {@code length} and is filled with {@code value0}, {@code value1}, {@code value2} and {@code value3} in a repeated pattern
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0} or it cannot be evenly divided by {@code 4}
	 */
	public static byte[] array(final int length, final byte value0, final byte value1, final byte value2, final byte value3) {
		final byte[] array = new byte[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		if(array.length % 4 != 0) {
			throw new IllegalArgumentException(String.format("%d %% 4 != 0", Integer.valueOf(length)));
		}
		
		for(int i = 0; i < length; i += 4) {
			array[i + 0] = value0;
			array[i + 1] = value1;
			array[i + 2] = value2;
			array[i + 3] = value3;
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code byte[]} representation of {@code intArray}.
	 * <p>
	 * If {@code intArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intArray an {@code int[]}
	 * @return a {@code byte[]} representation of {@code intArray}
	 * @throws NullPointerException thrown if, and only if, {@code intArray} is {@code null}
	 */
	public static byte[] toArray(final int[] intArray) {
		Objects.requireNonNull(intArray, "intArray == null");
		
		final byte[] byteArray = new byte[intArray.length];
		
		for(int i = 0; i < intArray.length; i++) {
			byteArray[i] = (byte)(intArray[i]);
		}
		
		return byteArray;
	}
}