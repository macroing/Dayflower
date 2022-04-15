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

import org.macroing.java.util.Arrays;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code int[]} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IntArrays {
	private IntArrays() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the index of {@code value} in {@code array}, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param value the {@code int} value to find the index for
	 * @param array the {@code int[]} to search for {@code value} in
	 * @return the index of {@code value} in {@code array}, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int indexOf(final int value, final int[] array) {
		Objects.requireNonNull(array, "array == null");
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] == value) {
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns the index of {@code value} in {@code array}, or {@code -1} if it cannot be found.
	 * <p>
	 * If either {@code value} or {@code array} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isIncrementingByValueLength} is {@code true} and {@code array.length % value.length} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the parameter argument {@code isIncrementingByValueLength} is {@code true}, this method assumes {@code array} contains sub-structures with equal lengths, namely {@code value.length}. This will yield a faster search, but also restrictions on
	 * {@code array}. It requires a length that is a multiple of {@code value.length}. If {@code array} does not contain sub-structures with equal lengths, {@code isIncrementingByValueLength} should be {@code false}.
	 * <p>
	 * If the parameter argument {@code isReturningRelativeIndex} is {@code true}, the relative index of {@code value} in {@code array} will be returned. The relative index represents the index of the sub-structure in {@code array}. It works best if
	 * the parameter argument {@code isIncrementingByValueLength} is {@code true} and its restrictions are met.
	 * <p>
	 * If the parameter argument {@code isReturningRelativeIndex} is {@code false}, the absolute index of {@code value} in {@code array} will be returned. The absolute index represents the index in {@code array}.
	 * 
	 * @param value the {@code int[]} value to find the index for
	 * @param array the {@code int[]} to search for {@code value} in
	 * @param isIncrementingByValueLength {@code true} if, and only if, {@code array} consists of sub-structures with a length of {@code value.length}, {@code false} otherwise
	 * @param isReturningRelativeIndex {@code true} if, and only if, the relative index should be returned, {@code false} otherwise
	 * @return the index of {@code value} in {@code array}, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code isIncrementingByValueLength} is {@code true} and {@code array.length % value.length} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code value} or {@code array} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int indexOf(final int[] value, final int[] array, final boolean isIncrementingByValueLength, final boolean isReturningRelativeIndex) {
		Objects.requireNonNull(value, "value == null");
		Objects.requireNonNull(array, "array == null");
		
		if(isIncrementingByValueLength) {
			ParameterArguments.requireExact(array.length % value.length, 0, "array.length % value.length");
		}
		
		final int count = isIncrementingByValueLength ? array.length / value.length : array.length;
		final int length = isIncrementingByValueLength ? value.length : 1;
		
		for(int indexAbsolute = 0, indexRelative = 0; indexRelative < count; indexAbsolute += length, indexRelative++) {
			if(Arrays.equals(array, value, indexAbsolute, 0, length)) {
				return isReturningRelativeIndex ? indexRelative : indexAbsolute;
			}
		}
		
		return -1;
	}
	
	/**
	 * Performs a splice operation on the {@code int[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntArrays.splice(array, offset, array.length);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code int[]}
	 * @param offset the offset for the removal, which may be negative
	 * @return a new {@code int[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int[] splice(final int[] array, final int offset) {
		return splice(array, offset, array.length);
	}
	
	/**
	 * Performs a splice operation on the {@code int[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntArrays.splice(array, offset, length, new int[0]);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code int[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @return a new {@code int[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int[] splice(final int[] array, final int offset, final int length) {
		return splice(array, offset, length, new int[0]);
	}
	
	/**
	 * Performs a splice operation on the {@code int[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If either {@code array} or {@code arrayReplacement} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntArrays.splice(array, offset, length, arrayReplacement, new int[0]);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code int[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @param arrayReplacement an {@code int[]} that acts as replacement
	 * @return a new {@code int[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayReplacement} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int[] splice(final int[] array, final int offset, final int length, final int[] arrayReplacement) {
		return splice(array, offset, length, arrayReplacement, new int[0]);
	}
	
	/**
	 * Performs a splice operation on the {@code int[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code int[]} with the result of the operation.
	 * <p>
	 * If either {@code array}, {@code arrayReplacement} or {@code arrayMatcher} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the input {@code int[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @param arrayReplacement an {@code int[]} that acts as replacement
	 * @param arrayMatcher an {@code int[]} that matches the part of {@code array} that will be replaced
	 * @return a new {@code int[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code array}, {@code arrayReplacement} or {@code arrayMatcher} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static int[] splice(final int[] array, final int offset, final int length, final int[] arrayReplacement, final int[] arrayMatcher) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayReplacement, "arrayReplacement == null");
		Objects.requireNonNull(arrayMatcher, "arrayMatcher == null");
		
		final int[] arrayA = array;
		final int[] arrayB = arrayReplacement;
		final int[] arrayC = array;
		
		final int arrayASrcPos = 0;
		final int arrayALength = Ints.saturate(offset >= 0 ? offset : arrayA.length + offset, 0, arrayA.length);
		
		final int arrayBSrcPos = 0;
		final int arrayBLength = arrayB.length;
		
		final int arrayCSrcPos = Ints.saturate(length >= 0 ? length + arrayALength : arrayC.length + length, arrayALength, arrayC.length);
		final int arrayCLength = arrayC.length - arrayCSrcPos;
		
		final int arrayDSrcPos0 = 0;
		final int arrayDSrcPos1 = arrayALength;
		final int arrayDSrcPos2 = arrayALength + arrayBLength;
		final int arrayDLength  = arrayA.length - (arrayCSrcPos - arrayALength) + arrayBLength;
		
		if(arrayCSrcPos - arrayALength > 0 && !Arrays.equals(array, arrayMatcher, arrayALength, 0, Ints.min(arrayCSrcPos - arrayALength, arrayMatcher.length))) {
			return array;
		}
		
		final int[] arrayD = new int[arrayDLength];
		
		System.arraycopy(arrayA, arrayASrcPos, arrayD, arrayDSrcPos0, arrayALength);
		System.arraycopy(arrayB, arrayBSrcPos, arrayD, arrayDSrcPos1, arrayBLength);
		System.arraycopy(arrayC, arrayCSrcPos, arrayD, arrayDSrcPos2, arrayCLength);
		
		return arrayD;
	}
}