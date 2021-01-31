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
import java.util.function.LongSupplier;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code long} values.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Longs {
	private Longs() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code long} with the bits of {@code value} reversed.
	 * 
	 * @param value a {@code long} value
	 * @return a {@code long} with the bits of {@code value} reversed
	 */
	public static long reverseBits(final long value) {
		final long value0 = Ints.reverseBits((int)(value >>>  0));
		final long value1 = Ints.reverseBits((int)(value >>> 32));
		
		return (value0 << 32) | value1;
	}
	
	/**
	 * Returns a {@code long[]} with a length of {@code length} and is filled with {@code 0L}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Longs.array(length, 0L);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the {@code long[]}
	 * @return a {@code long[]} with a length of {@code length} and is filled with {@code 0L}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static long[] array(final int length) {
		return array(length, 0L);
	}
	
	/**
	 * Returns a {@code long[]} with a length of {@code length} and is filled with {@code long} values from {@code longSupplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code longSupplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the {@code long[]}
	 * @param longSupplier the {@code LongSupplier} to fill the {@code long[]} with
	 * @return a {@code long[]} with a length of {@code length} and is filled with {@code long} values from {@code longSupplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code longSupplier} is {@code null}
	 */
	public static long[] array(final int length, final LongSupplier longSupplier) {
		final long[] array = new long[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(longSupplier, "longSupplier == null");
		
		for(int i = 0; i < array.length; i++) {
			array[i] = longSupplier.getAsLong();
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code long[]} with a length of {@code length} and is filled with {@code value}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param length the length of the {@code long[]}
	 * @param value the {@code long} value to fill the {@code long[]} with
	 * @return a {@code long[]} with a length of {@code length} and is filled with {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static long[] array(final int length, final long value) {
		final long[] array = new long[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Arrays.fill(array, value);
		
		return array;
	}
}