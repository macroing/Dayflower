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
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.LongSupplier;

import org.dayflower.java.io.LongArrayOutputStream;

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
	 * Returns {@code true} if, and only if, the elements of {@code arrayA} starting at {@code offsetArrayA} are equal to the elements of {@code arrayB} starting at {@code offsetArrayB}, {@code false} otherwise.
	 * <p>
	 * If either {@code arrayA} or {@code arrayB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code offsetArrayA} is less than {@code 0} or greater than or equal to {@code arrayA.length}, {@code offsetArrayB} is less than {@code 0} or greater than or equal to {@code arrayB.length}, {@code length} is less than {@code 0},
	 * {@code offsetArrayA + length} is less than {@code 0} or greater than {@code arrayA.length} or {@code offsetArrayB + length} is less than {@code 0} or greater than {@code arrayB.length}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param arrayA a {@code long[]}
	 * @param arrayB a {@code long[]}
	 * @param offsetArrayA the offset to start at in {@code arrayA}
	 * @param offsetArrayB the offset to start at in {@code arrayB}
	 * @param length the length of the sub-arrays to test for equality
	 * @return {@code true} if, and only if, the elements of {@code arrayA} starting at {@code offsetArrayA} are equal to the elements of {@code arrayB} starting at {@code offsetArrayB}, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code offsetArrayA} is less than {@code 0} or greater than or equal to {@code arrayA.length}, {@code offsetArrayB} is less than {@code 0} or greater than or equal to
	 *                                  {@code arrayB.length}, {@code length} is less than {@code 0}, {@code offsetArrayA + length} is less than {@code 0} or greater than {@code arrayA.length} or {@code offsetArrayB + length} is less than {@code 0}
	 *                                  or greater than {@code arrayB.length}
	 * @throws NullPointerException thrown if, and only if, either {@code arrayA} or {@code arrayB} are {@code null}
	 */
	public static boolean equal(final long[] arrayA, final long[] arrayB, final int offsetArrayA, final int offsetArrayB, final int length) {
		Objects.requireNonNull(arrayA, "arrayA == null");
		Objects.requireNonNull(arrayB, "arrayB == null");
		
		ParameterArguments.requireRange(offsetArrayA, 0, arrayA.length - 1, "offsetArrayA");
		ParameterArguments.requireRange(offsetArrayB, 0, arrayB.length - 1, "offsetArrayB");
		ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length");
		ParameterArguments.requireRange(offsetArrayA + length, 0, arrayA.length, "offsetArrayA + length");
		ParameterArguments.requireRange(offsetArrayB + length, 0, arrayB.length, "offsetArrayB + length");
		
		for(int i = 0; i < length; i++) {
			if(arrayA[offsetArrayA + i] != arrayB[offsetArrayB + i]) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the index of {@code value} in {@code array}, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param value the {@code long} value to find the index for
	 * @param array the {@code long[]} to search for {@code value} in
	 * @return the index of {@code value} in {@code array}, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static int indexOf(final long value, final long[] array) {
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
	 * @param value the {@code long[]} value to find the index for
	 * @param array the {@code long[]} to search for {@code value} in
	 * @param isIncrementingByValueLength {@code true} if, and only if, {@code array} consists of sub-structures with a length of {@code value.length}, {@code false} otherwise
	 * @param isReturningRelativeIndex {@code true} if, and only if, the relative index should be returned, {@code false} otherwise
	 * @return the index of {@code value} in {@code array}, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code isIncrementingByValueLength} is {@code true} and {@code array.length % value.length} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code value} or {@code array} are {@code null}
	 */
	public static int indexOf(final long[] value, final long[] array, final boolean isIncrementingByValueLength, final boolean isReturningRelativeIndex) {
		Objects.requireNonNull(value, "value == null");
		Objects.requireNonNull(array, "array == null");
		
		if(isIncrementingByValueLength) {
			ParameterArguments.requireExact(array.length % value.length, 0, "array.length % value.length");
		}
		
		final int count = isIncrementingByValueLength ? array.length / value.length : array.length;
		final int length = isIncrementingByValueLength ? value.length : 1;
		
		for(int indexAbsolute = 0, indexRelative = 0; indexRelative < count; indexAbsolute += length, indexRelative++) {
			if(equal(array, value, indexAbsolute, 0, length)) {
				return isReturningRelativeIndex ? indexRelative : indexAbsolute;
			}
		}
		
		return -1;
	}
	
	/**
	 * Returns a {@code long[]} representation of {@code objects} using {@code arrayFunction}.
	 * <p>
	 * If either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.convert(objects, arrayFunction, 0);
	 * }
	 * </pre>
	 * 
	 * @param <T> the generic type
	 * @param objects a {@code List} of type {@code T} with {@code Object} instances to convert into {@code long[]} instances
	 * @param arrayFunction a {@code Function} that maps {@code Object} instances of type {@code T} into {@code long[]} instances
	 * @return a {@code long[]} representation of {@code objects} using {@code arrayFunction}
	 * @throws NullPointerException thrown if, and only if, either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}
	 */
	public static <T> long[] convert(final List<T> objects, final Function<T, long[]> arrayFunction) {
		return convert(objects, arrayFunction, 0);
	}
	
	/**
	 * Returns a {@code long[]} representation of {@code objects} using {@code arrayFunction}.
	 * <p>
	 * If either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code minimumLength} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param <T> the generic type
	 * @param objects a {@code List} of type {@code T} with {@code Object} instances to convert into {@code long[]} instances
	 * @param arrayFunction a {@code Function} that maps {@code Object} instances of type {@code T} into {@code long[]} instances
	 * @param minimumLength the minimum length of the returned {@code long[]} if, and only if, either {@code objects.isEmpty()} or the array has a length of {@code 0}
	 * @return a {@code long[]} representation of {@code objects} using {@code arrayFunction}
	 * @throws IllegalArgumentException thrown if, and only if, {@code minimumLength} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code objects}, at least one of its elements, {@code arrayFunction} or at least one of its results are {@code null}
	 */
	public static <T> long[] convert(final List<T> objects, final Function<T, long[]> arrayFunction, final int minimumLength) {
		ParameterArguments.requireNonNullList(objects, "objects");
		
		Objects.requireNonNull(arrayFunction, "arrayFunction == null");
		
		ParameterArguments.requireRange(minimumLength, 0, Integer.MAX_VALUE, "minimumLength");
		
		if(objects.isEmpty()) {
			return create(minimumLength);
		}
		
		try(final LongArrayOutputStream longArrayOutputStream = new LongArrayOutputStream()) {
			for(final T object : objects) {
				longArrayOutputStream.write(arrayFunction.apply(object));
			}
			
			final long[] array = longArrayOutputStream.toLongArray();
			
			return array.length == 0 ? create(minimumLength) : array;
		}
	}
	
	/**
	 * Returns a {@code long[]} representation of {@code byteArray}.
	 * <p>
	 * If {@code byteArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.convert(byteArray, false);
	 * }
	 * </pre>
	 * 
	 * @param byteArray a {@code byte[]}
	 * @return a {@code long[]} representation of {@code byteArray}
	 * @throws NullPointerException thrown if, and only if, {@code byteArray} is {@code null}
	 */
	public static long[] convert(final byte[] byteArray) {
		return convert(byteArray, false);
	}
	
	/**
	 * Returns a {@code long[]} representation of {@code byteArray}.
	 * <p>
	 * If {@code byteArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param byteArray a {@code byte[]}
	 * @param isUnsigned {@code true} if, and only if, unsigned values should be used, {@code false} otherwise
	 * @return a {@code long[]} representation of {@code byteArray}
	 * @throws NullPointerException thrown if, and only if, {@code byteArray} is {@code null}
	 */
	public static long[] convert(final byte[] byteArray, final boolean isUnsigned) {
		Objects.requireNonNull(byteArray, "byteArray == null");
		
		final long[] longArray = new long[byteArray.length];
		
		for(int i = 0; i < byteArray.length; i++) {
			longArray[i] = isUnsigned ? byteArray[i] & 0xFF : byteArray[i];
		}
		
		return longArray;
	}
	
	/**
	 * Returns a {@code long[]} with a length of {@code length} and is filled with {@code 0L}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.create(length, 0L);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the {@code long[]}
	 * @return a {@code long[]} with a length of {@code length} and is filled with {@code 0L}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static long[] create(final int length) {
		return create(length, 0L);
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
	public static long[] create(final int length, final LongSupplier longSupplier) {
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
	public static long[] create(final int length, final long value) {
		final long[] array = new long[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Arrays.fill(array, value);
		
		return array;
	}
	
	/**
	 * Performs a merge operation on the {@code long[]} instance {@code array} and the {@code long} {@code value}.
	 * <p>
	 * Returns a new {@code long[]} with {@code array} and {@code value} merged.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.merge(array, new long[] {value});
	 * }
	 * </pre>
	 * 
	 * @param array the {@code long[]} that comes first
	 * @param value the {@code long} that comes second
	 * @return a new {@code long[]} with {@code array} and {@code value} merged
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static long[] merge(final long[] array, final long value) {
		return merge(array, new long[] {value});
	}
	
	/**
	 * Performs a merge operation on the {@code long[]} instances {@code arrayA} and {@code arrayB}.
	 * <p>
	 * Returns a new {@code long[]} with {@code arrayA} and {@code arrayB} merged.
	 * <p>
	 * If either {@code arrayA} or {@code arrayB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayA the {@code long[]} that comes first
	 * @param arrayB the {@code long[]} that comes second
	 * @return a new {@code long[]} with {@code arrayA} and {@code arrayB} merged
	 * @throws NullPointerException thrown if, and only if, either {@code arrayA} or {@code arrayB} are {@code null}
	 */
	public static long[] merge(final long[] arrayA, final long[] arrayB) {
		Objects.requireNonNull(arrayA, "arrayA == null");
		Objects.requireNonNull(arrayB, "arrayB == null");
		
		final long[] arrayC = new long[arrayA.length + arrayB.length];
		
		final int offsetArrayA = 0;
		final int offsetArrayB = arrayA.length;
		
		System.arraycopy(arrayA, 0, arrayC, offsetArrayA, arrayA.length);
		System.arraycopy(arrayB, 0, arrayC, offsetArrayB, arrayB.length);
		
		return arrayC;
	}
	
	/**
	 * Performs a merge operation on the {@code long[]} instances in {@code arrays}.
	 * <p>
	 * Returns a new {@code long[]} with {@code arrays} merged.
	 * <p>
	 * If either {@code arrays} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrays the {@code long[][]} instance to combine into one {@code long[]}
	 * @return a new {@code long[]} with {@code arrays} merged
	 * @throws NullPointerException thrown if, and only if, either {@code arrays} or at least one of its elements are {@code null}
	 */
	public static long[] merge(final long[]... arrays) {
		Objects.requireNonNull(arrays, "arrays == null");
		
		for(int i = 0; i < arrays.length; i++) {
			Objects.requireNonNull(arrays[i], "arrays[" + i + "] == null");
		}
		
		try(final LongArrayOutputStream longArrayOutputStream = new LongArrayOutputStream()) {
			for(final long[] array : arrays) {
				longArrayOutputStream.write(array);
			}
			
			return longArrayOutputStream.toLongArray();
		}
	}
	
	/**
	 * Performs a splice operation on the {@code long[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code long[]} with the result of the operation.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.splice(array, offset, array.length);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code long[]}
	 * @param offset the offset for the removal, which may be negative
	 * @return a new {@code long[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static long[] splice(final long[] array, final int offset) {
		return splice(array, offset, array.length);
	}
	
	/**
	 * Performs a splice operation on the {@code long[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code long[]} with the result of the operation.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.splice(array, offset, length, new long[0]);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code long[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @return a new {@code long[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static long[] splice(final long[] array, final int offset, final int length) {
		return splice(array, offset, length, new long[0]);
	}
	
	/**
	 * Performs a splice operation on the {@code long[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code long[]} with the result of the operation.
	 * <p>
	 * If either {@code array} or {@code arrayReplacement} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * LongArrays.splice(array, offset, length, arrayReplacement, new long[0]);
	 * }
	 * </pre>
	 * 
	 * @param array the input {@code long[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @param arrayReplacement an {@code long[]} that acts as replacement
	 * @return a new {@code long[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayReplacement} are {@code null}
	 */
	public static long[] splice(final long[] array, final int offset, final int length, final long[] arrayReplacement) {
		return splice(array, offset, length, arrayReplacement, new long[0]);
	}
	
	/**
	 * Performs a splice operation on the {@code long[]} instance {@code array}.
	 * <p>
	 * Returns a new {@code long[]} with the result of the operation.
	 * <p>
	 * If either {@code array}, {@code arrayReplacement} or {@code arrayMatcher} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the input {@code long[]}
	 * @param offset the offset for the removal, which may be negative
	 * @param length the length of the removal, which may be negative
	 * @param arrayReplacement an {@code long[]} that acts as replacement
	 * @param arrayMatcher an {@code long[]} that matches the part of {@code array} that will be replaced
	 * @return a new {@code long[]} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code array}, {@code arrayReplacement} or {@code arrayMatcher} are {@code null}
	 */
	public static long[] splice(final long[] array, final int offset, final int length, final long[] arrayReplacement, final long[] arrayMatcher) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayReplacement, "arrayReplacement == null");
		Objects.requireNonNull(arrayMatcher, "arrayMatcher == null");
		
		final long[] arrayA = array;
		final long[] arrayB = arrayReplacement;
		final long[] arrayC = array;
		
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
		
		if(arrayCSrcPos - arrayALength > 0 && !equal(array, arrayMatcher, arrayALength, 0, Ints.min(arrayCSrcPos - arrayALength, arrayMatcher.length))) {
			return array;
		}
		
		final long[] arrayD = new long[arrayDLength];
		
		System.arraycopy(arrayA, arrayASrcPos, arrayD, arrayDSrcPos0, arrayALength);
		System.arraycopy(arrayB, arrayBSrcPos, arrayD, arrayDSrcPos1, arrayBLength);
		System.arraycopy(arrayC, arrayCSrcPos, arrayD, arrayDSrcPos2, arrayCLength);
		
		return arrayD;
	}
}