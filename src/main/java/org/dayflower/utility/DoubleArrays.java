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

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.java.io.DoubleArrayOutputStream;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code double[]} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DoubleArrays {
	private DoubleArrays() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code double[]} with a length of {@code length} and is filled with {@code value0}, {@code value1}, {@code value2} and {@code value3} in a repeated pattern.
	 * <p>
	 * If {@code length} is less than {@code 0} or it cannot be evenly divided by {@code 4}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param length the length of the {@code double[]}
	 * @param value0 the {@code double} at the relative offset {@code 0}
	 * @param value1 the {@code double} at the relative offset {@code 1}
	 * @param value2 the {@code double} at the relative offset {@code 2}
	 * @param value3 the {@code double} at the relative offset {@code 3}
	 * @return a {@code double[]} with a length of {@code length} and is filled with {@code value0}, {@code value1}, {@code value2} and {@code value3} in a repeated pattern
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0} or it cannot be evenly divided by {@code 4}
	 */
//	TODO: Add Unit Tests!
	public static double[] create(final int length, final double value0, final double value1, final double value2, final double value3) {
		final double[] array = new double[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
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
	 * Performs a merge operation on the {@code double[]} instances in {@code arrays}.
	 * <p>
	 * Returns a new {@code double[]} with {@code arrays} merged.
	 * <p>
	 * If either {@code arrays} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrays the {@code double[][]} instance to combine into one {@code double[]}
	 * @return a new {@code double[]} with {@code arrays} merged
	 * @throws NullPointerException thrown if, and only if, either {@code arrays} or at least one of its elements are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static double[] merge(final double[]... arrays) {
		Objects.requireNonNull(arrays, "arrays == null");
		
		for(int i = 0; i < arrays.length; i++) {
			Objects.requireNonNull(arrays[i], "arrays[" + i + "] == null");
		}
		
		try(final DoubleArrayOutputStream doubleArrayOutputStream = new DoubleArrayOutputStream()) {
			for(final double[] array : arrays) {
				doubleArrayOutputStream.write(array);
			}
			
			return doubleArrayOutputStream.toDoubleArray();
		}
	}
}