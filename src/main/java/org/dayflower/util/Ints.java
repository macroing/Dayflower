/**
 * Copyright 2020 J&#246;rgen Lundgren
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

/**
 * The class {@code Ints} contains methods for performing basic numeric operations.
 * <p>
 * You can think of this class as an extension of {@code Math}. It does what {@code Math} does for the {@code double}, {@code float} and {@code int} types, but for the {@code int} type only. In addition to this it also adds new methods.
 * <p>
 * This class does not contain all methods from {@code Math}. The methods in {@code Math} that deals with the {@code double} type can be found in the class {@link Doubles}. The methods in {@code Math} that deals with the {@code float} type can be found
 * in the class {@link Floats}.
 * <p>
 * The documentation in this class should be comprehensive. But some of the details covered in the documentation of the {@code Math} class may be missing. To get the full documentation for a particular method, you may want to look at the documentation
 * of the corresponding method in the {@code Math} class. This is, of course, only true if the method you're looking at exists in the {@code Math} class.
 * <p>
 * Not all methods in the {@code Math} class that should be added to this class, may have been added yet.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Ints {
	private Ints() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the absolute version of {@code value}.
	 * <p>
	 * If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned.
	 * <p>
	 * Note that if the argument is equal to the value of {@code Integer.MIN_VALUE}, the most negative representable {@code int} value, the result is that same value, which is negative.
	 * 
	 * @param value an {@code int} value
	 * @return the absolute version of {@code value}
	 * @see Math#abs(int)
	 */
	public static int abs(final int value) {
		return Math.abs(value);
	}
	
	/**
	 * Returns the greater value of {@code a} and {@code b}.
	 * <p>
	 * The result is the argument closer to the value of {@code Integer.MAX_VALUE}.
	 * <p>
	 * If the arguments have the same value, the result is that same value.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the greater value of {@code a} and {@code b}
	 * @see Math#max(int, int)
	 */
	public static int max(final int a, final int b) {
		return Math.max(a, b);
	}
	
	/**
	 * Returns the smaller value of {@code a} and {@code b}.
	 * <p>
	 * The result the argument closer to the value of {@code Integer.MIN_VALUE}.
	 * <p>
	 * If the arguments have the same value, the result is that same value.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the smaller value of {@code a} and {@code b}
	 * @see Math#min(int, int)
	 */
	public static int min(final int a, final int b) {
		return Math.min(a, b);
	}
	
	/**
	 * Performs a modulo operation on {@code value} given {@code maximumValue}.
	 * <p>
	 * Returns {@code value} or a wrapped around version of it.
	 * <p>
	 * The modulo operation performed by this method differs slightly from the modulo operator in Java.
	 * <p>
	 * If {@code value} is positive, the following occurs:
	 * <pre>
	 * {@code
	 * int changedValue = value % maximumValue;
	 * }
	 * </pre>
	 * If {@code value} is negative, the following occurs:
	 * <pre>
	 * {@code
	 * int changedValue = (value % maximumValue + maximumValue) % maximumValue;
	 * }
	 * </pre>
	 * 
	 * @param value an {@code int} value
	 * @param maximumValue the maximum value
	 * @return {@code value} or a wrapped around version of it
	 */
	public static int modulo(final int value, final int maximumValue) {
		return value < 0 ? (value % maximumValue + maximumValue) % maximumValue : value % maximumValue;
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
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(value, 0, 255);
	 * }
	 * </pre>
	 * 
	 * @param value the value to saturate (or clamp)
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static int saturate(final int value) {
		return saturate(value, 0, 255);
	}
	
	/**
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * If {@code value} is less than {@code min(edgeA, edgeB)}, {@code min(edgeA, edgeB)} will be returned. If {@code value} is greater than {@code max(edgeA, edgeB)}, {@code max(edgeA, edgeB)} will be returned. Otherwise {@code value} will be
	 * returned.
	 * 
	 * @param value the value to saturate (or clamp)
	 * @param edgeA the minimum or maximum value
	 * @param edgeB the maximum or minimum value
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static int saturate(final int value, final int edgeA, final int edgeB) {
		final int minimum = min(edgeA, edgeB);
		final int maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
	/**
	 * Returns an {@code int} representation of a {@code float} value.
	 * 
	 * @param value a {@code float} value
	 * @return an {@code int} representation of a {@code float} value
	 */
	public static int toInt(final float value) {
		return (int)(value);
	}
}