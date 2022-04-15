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