/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.util;

import static org.dayflower.util.Ints.max;
import static org.dayflower.util.Ints.min;

import java.util.List;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that checks parameter argument validity.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ParameterArguments {
	private ParameterArguments() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Checks that {@code list} and all of its elements are not {@code null}.
	 * <p>
	 * Returns {@code list}.
	 * <p>
	 * If either {@code list}, an element in {@code list} or {@code name} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param <T> the generic type of {@code list}
	 * @param list the {@code List} to check
	 * @param name the name of the parameter argument used for {@code list}, that will be part of the message for the {@code NullPointerException}
	 * @return {@code list}
	 * @throws NullPointerException thrown if, and only if, either {@code list}, an element in {@code list} or {@code name} are {@code null}
	 */
	public static <T> List<T> requireNonNullList(final List<T> list, final String name) {
		Objects.requireNonNull(name, "name == null");
		Objects.requireNonNull(list, String.format("%s == null", name));
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i) == null) {
				throw new NullPointerException(String.format("%s.get(%d) == null", name, Integer.valueOf(i)));
			}
		}
		
		return list;
	}
	
	/**
	 * Checks that {@code objects} and all of its elements are not {@code null}.
	 * <p>
	 * Returns {@code objects}.
	 * <p>
	 * If either {@code objects}, an element in {@code objects} or {@code name} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param <T> the generic type of {@code objects}
	 * @param objects the array to check
	 * @param name the name of the parameter argument used for {@code objects}, that will be part of the message for the {@code NullPointerException}
	 * @return {@code objects}
	 * @throws NullPointerException thrown if, and only if, either {@code objects}, an element in {@code objects} or {@code name} are {@code null}
	 */
	public static <T> T[] requireNonNullArray(final T[] objects, final String name) {
		Objects.requireNonNull(name, "name == null");
		Objects.requireNonNull(objects, String.format("%s == null", name));
		
		for(int i = 0; i < objects.length; i++) {
			Objects.requireNonNull(objects[i], String.format("%s[%s] == null", name, Integer.toString(i)));
		}
		
		return objects;
	}
	
	/**
	 * Checks that {@code array.length} is equal to {@code arrayLengthExpected}.
	 * <p>
	 * Returns {@code array}.
	 * <p>
	 * If {@code array.length} is not equal to {@code arrayLengthExpected}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to check
	 * @param arrayLengthExpected the expected length to compare against
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length} is not equal to {@code arrayLengthExpected}
	 */
	public static byte[] requireExactArrayLength(final byte[] array, final int arrayLengthExpected, final String name) {
		if(array.length != arrayLengthExpected) {
			throw new IllegalArgumentException(String.format("%s.length != %d: %s.length == %d", name, Integer.valueOf(arrayLengthExpected), name, Integer.valueOf(array.length)));
		}
		
		return array;
	}
	
	/**
	 * Checks that {@code value} is finite.
	 * <p>
	 * Returns {@code value}.
	 * <p>
	 * If either {@code Double.isInfinite(value)} or {@code Double.isNaN(value)} returns {@code true}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value to check
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code Double.isInfinite(value)} or {@code Double.isNaN(value)} returns {@code true}
	 */
	public static double requireFiniteValue(final double value, final String name) {
		if(Double.isInfinite(value)) {
			throw new IllegalArgumentException(String.format("Double.isInfinite(%s) == true", name));
		} else if(Double.isNaN(value)) {
			throw new IllegalArgumentException(String.format("Double.isNaN(%s) == true", name));
		} else {
			return value;
		}
	}
	
	/**
	 * Checks that {@code value} is finite.
	 * <p>
	 * Returns {@code value}.
	 * <p>
	 * If either {@code Float.isInfinite(value)} or {@code Float.isNaN(value)} returns {@code true}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value to check
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code Float.isInfinite(value)} or {@code Float.isNaN(value)} returns {@code true}
	 */
	public static float requireFiniteValue(final float value, final String name) {
		if(Float.isInfinite(value)) {
			throw new IllegalArgumentException(String.format("Float.isInfinite(%s) == true", name));
		} else if(Float.isNaN(value)) {
			throw new IllegalArgumentException(String.format("Float.isNaN(%s) == true", name));
		} else {
			return value;
		}
	}
	
	/**
	 * Checks that {@code value} is equal to {@code valueExpected}.
	 * <p>
	 * Returns {@code value}.
	 * <p>
	 * If {@code value} is not equal to {@code valueExpected}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value to check
	 * @param valueExpected the expected value to compare against
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code value} is not equal to {@code valueExpected}
	 */
	public static int requireExact(final int value, final int valueExpected, final String name) {
		if(value != valueExpected) {
			throw new IllegalArgumentException(String.format("%s != %d: %s == %d", name, Integer.valueOf(valueExpected), name, Integer.valueOf(value)));
		}
		
		return value;
	}
	
	/**
	 * Checks that {@code value} is in the range {@code [Ints.min(edgeA, edgeB), Ints.max(edgeA, edgeB)]}.
	 * <p>
	 * Returns {@code value}.
	 * <p>
	 * If {@code value} is less than {@code Ints.min(edgeA, edgeB)} or greater than {@code Ints.max(edgeA, edgeB)}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param value the value to check
	 * @param edgeA the minimum or maximum value allowed
	 * @param edgeB the maximum or minimum value allowed
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code value} is less than {@code Ints.min(edgeA, edgeB)} or greater than {@code Ints.max(edgeA, edgeB)}
	 */
	public static int requireRange(final int value, final int edgeA, final int edgeB, final String name) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			throw new IllegalArgumentException(String.format("%s < %d: %s == %d", name, Integer.valueOf(minimum), name, Integer.valueOf(value)));
		} else if(value > maximum) {
			throw new IllegalArgumentException(String.format("%s > %d: %s == %d", name, Integer.valueOf(maximum), name, Integer.valueOf(value)));
		} else {
			return value;
		}
	}
	
	/**
	 * Checks that {@code value} is in the range {@code [Ints.min(edgeA, edgeB), Ints.max(edgeA, edgeB)]}.
	 * <p>
	 * Returns {@code value}.
	 * <p>
	 * If {@code value} is less than {@code Ints.min(edgeA, edgeB)} or greater than {@code Ints.max(edgeA, edgeB)}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code nameFormat} or {@code nameFormatArguments} are invalid, an {@code IllegalFormatException} will be thrown.
	 * 
	 * @param value the value to check
	 * @param edgeA the minimum or maximum value allowed
	 * @param edgeB the maximum or minimum value allowed
	 * @param nameFormat the name format of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @param nameFormatArguments the name format arguments of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code value}
	 * @throws IllegalArgumentException thrown if, and only if, {@code value} is less than {@code Ints.min(edgeA, edgeB)} or greater than {@code Ints.max(edgeA, edgeB)}
	 * @throws IllegalFormatException thrown if, and only if, either {@code nameFormat} or {@code nameFormatArguments} are invalid
	 */
	public static int requireRangef(final int value, final int edgeA, final int edgeB, final String nameFormat, final Object... nameFormatArguments) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			final String name = String.format(nameFormat, nameFormatArguments);
			
			throw new IllegalArgumentException(String.format("%s < %d: %s == %d", name, Integer.valueOf(minimum), name, Integer.valueOf(value)));
		} else if(value > maximum) {
			final String name = String.format(nameFormat, nameFormatArguments);
			
			throw new IllegalArgumentException(String.format("%s > %d: %s == %d", name, Integer.valueOf(maximum), name, Integer.valueOf(value)));
		} else {
			return value;
		}
	}
	
	/**
	 * Checks that {@code array.length} is equal to {@code arrayLengthExpected}.
	 * <p>
	 * Returns {@code array}.
	 * <p>
	 * If {@code array.length} is not equal to {@code arrayLengthExpected}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to check
	 * @param arrayLengthExpected the expected length to compare against
	 * @param name the name of the variable that will be part of the message of the {@code IllegalArgumentException}
	 * @return {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length} is not equal to {@code arrayLengthExpected}
	 */
	public static int[] requireExactArrayLength(final int[] array, final int arrayLengthExpected, final String name) {
		if(array.length != arrayLengthExpected) {
			throw new IllegalArgumentException(String.format("%s.length != %d: %s.length == %d", name, Integer.valueOf(arrayLengthExpected), name, Integer.valueOf(array.length)));
		}
		
		return array;
	}
}