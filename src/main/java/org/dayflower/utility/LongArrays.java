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

import java.util.Objects;
import java.util.function.LongSupplier;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code long[]} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LongArrays {
	private LongArrays() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public static long[] create(final int length, final LongSupplier longSupplier) {
		final long[] array = new long[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(longSupplier, "longSupplier == null");
		
		for(int i = 0; i < array.length; i++) {
			array[i] = longSupplier.getAsLong();
		}
		
		return array;
	}
}