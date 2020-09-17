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

import java.util.concurrent.ThreadLocalRandom;

/**
 * The class {@code Doubles} contains methods for performing basic numeric operations such as the elementary exponential, logarithm, square root and trigonometric functions.
 * <p>
 * You can think of this class as an extension of {@code Math}. It does what {@code Math} does for the {@code double}, {@code float} and {@code int} types, but for the {@code double} type only. In addition to this it also adds new methods.
 * <p>
 * This class does not contain all methods from {@code Math}. The methods in {@code Math} that deals with the {@code float} type can be found in the class {@link Floats}. The methods in {@code Math} that deals with the {@code int} type can be found
 * in the class {@link Ints}.
 * <p>
 * The documentation in this class should be comprehensive. But some of the details covered in the documentation of the {@code Math} class may be missing. To get the full documentation for a particular method, you may want to look at the documentation
 * of the corresponding method in the {@code Math} class. This is, of course, only true if the method you're looking at exists in the {@code Math} class.
 * <p>
 * Not all methods in the {@code Math} class that should be added to this class, may have been added yet.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public class Doubles {
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter.
	 */
	public static final double PI = Math.PI;
	
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 180.0.
	 */
	public static final double PI_DIVIDED_BY_180 = PI / 180.0D;
	
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 2.0.
	 */
	public static final double PI_DIVIDED_BY_2 = PI / 2.0D;
	
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 4.0.
	 */
	public static final double PI_DIVIDED_BY_4 = PI / 4.0D;
	
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, multiplied by 2.0.
	 */
	public static final double PI_MULTIPLIED_BY_2 = PI * 2.0D;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI_MULTIPLIED_BY_2}.
	 */
	public static final double PI_MULTIPLIED_BY_2_RECIPROCAL = 1.0D / PI_MULTIPLIED_BY_2;
	
	/**
	 * The {@code double} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, multiplied by 4.0.
	 */
	public static final double PI_MULTIPLIED_BY_4 = PI * 4.0D;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI}.
	 */
	public static final double PI_RECIPROCAL = 1.0D / PI;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Doubles() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code a} is equal to {@code b}, {@code false} otherwise.
	 * 
	 * @param a a {@code double} value
	 * @param b a {@code double} value
	 * @return {@code true} if, and only if, {@code a} is equal to {@code b}, {@code false} otherwise
	 */
	public static boolean equal(final double a, final double b) {
		return Double.compare(a, b) == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code a} is equal to {@code b} and {@code b} is equal to {@code c}, {@code false} otherwise.
	 * 
	 * @param a a {@code double} value
	 * @param b a {@code double} value
	 * @param c a {@code double} value
	 * @return {@code true} if, and only if, {@code a} is equal to {@code b} and {@code b} is equal to {@code c}, {@code false} otherwise
	 */
	public static boolean equal(final double a, final double b, final double c) {
		return equal(a, b) && equal(b, c);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is {@code Double.NaN}, {@code false} otherwise.
	 * 
	 * @param value a {@code double} value
	 * @return {@code true} if, and only if, {@code value} is {@code Double.NaN}, {@code false} otherwise
	 */
	public static boolean isNaN(final double value) {
		return Double.isNaN(value);
	}
	
	/**
	 * Returns the absolute version of {@code value}.
	 * <p>
	 * If the argument is not negative, the argument is returned. If the argument is negative, the negation of the argument is returned.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is positive zero or negative zero, the result is positive zero.</li>
	 * <li>If the argument is infinite, the result is positive infinity.</li>
	 * <li>If the argument is NaN, the result is NaN.</li>
	 * </ul>
	 * <p>
	 * In other words, the result is the same as the value of the expression:
	 * <pre>
	 * {@code
	 * Double.longBitsToDouble((Double.doubleToLongBits(value) << 1) >>> 1)
	 * }
	 * </pre>
	 * 
	 * @param value a {@code double} value
	 * @return the absolute version of {@code value}
	 * @see Math#abs(double)
	 */
	public static double abs(final double value) {
		return Math.abs(value);
	}
	
	/**
	 * Returns the arc cosine of {@code value}.
	 * <p>
	 * The returned angle is in the range 0.0 through pi.
	 * <p>
	 * Special case:
	 * <ul>
	 * <li>If the argument is NaN or its absolute value is greater than 1, then the result is NaN.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param value the value whose arc cosine is to be returned
	 * @return the arc cosine of {@code value}
	 * @see Math#acos(double)
	 */
	public static double acos(final double value) {
		return Math.acos(value);
	}
	
	/**
	 * Returns the arc sine of {@code value}.
	 * <p>
	 * The returned angle is in the range -pi / 2 through pi / 2.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or its absolute value is greater than 1, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign as the argument.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param value the value whose arc sine is to be returned
	 * @return the arc sine of {@code value}
	 * @see Math#asin(double)
	 */
	public static double asin(final double value) {
		return Math.asin(value);
	}
	
	/**
	 * Returns the result of {@code asin(value)} divided by pi.
	 * 
	 * @param value the value whose arc sine divided by pi is to be returned
	 * @return the result of {@code asin(value)} divided by pi
	 * @see #asin(float)
	 */
	public static double asinpi(final double value) {
		return asin(value) / PI;
	}
	
	/**
	 * Returns the arc tangent of {@code value}.
	 * <p>
	 * The returned angle is in the range -pi / 2 through pi / 2.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign as the argument.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param value the value whose arc tangent is to be returned
	 * @return the arc tangent of {@code value}
	 * @see Math#atan(double)
	 */
	public static double atan(final double value) {
		return Math.atan(value);
	}
	
	/**
	 * Returns the angle <i>theta</i> from the conversion of rectangular coordinates (x, y) to polar coordinates (r, <i>theta</i>).
	 * <p>
	 * This method computes the phase <i>theta</i> by computing an arc tangent of y / x in the range of <i>-pi</i> to <i>pi</i>.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If either argument is NaN, then the result is NaN.</li>
	 * <li>If the first argument is positive zero and the second argument is positive, or the first argument is positive and finite and the second argument is positive infinity, then the result is positive zero.</li>
	 * <li>If the first argument is negative zero and the second argument is positive, or the first argument is negative and finite and the second argument is positive infinity, then the result is negative zero.</li>
	 * <li>If the first argument is positive zero and the second argument is negative, or the first argument is positive and finite and the second argument is negative infinity, then the result is the {@code float} value closest to pi.</li>
	 * <li>If the first argument is negative zero and the second argument is negative, or the first argument is negative and finite and the second argument is negative infinity, then the result is the {@code float} value closest to -pi.</li>
	 * <li>If the first argument is positive and the second argument is positive zero or negative zero, or the first argument is positive infinity and the second argument is finite, then the result is the {@code float} value closest to pi / 2.</li>
	 * <li>If the first argument is negative and the second argument is positive zero or negative zero, or the first argument is negative infinity and the second argument is finite, then the result is the {@code float} value closest to -pi / 2.</li>
	 * <li>If both arguments are positive infinity, then the result is the {@code float} value closest to pi / 4.</li>
	 * <li>If the first argument is positive infinity and the second argument is negative infinity, then the result is the {@code float} value closest to 3 * pi / 4.</li>
	 * <li>If the first argument is negative infinity and the second argument is positive infinity, then the result is the {@code float} value closest to -pi / 4.</li>
	 * <li>If both arguments are negative infinity, then the result is the {@code float} value closest to -3 * pi / 4.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 2 ulps of the exact result. Results must be semi-monotonic.
	 * 
	 * @param y the ordinate coordinate
	 * @param x the abscissa coordinate
	 * @return the angle <i>theta</i> from the conversion of rectangular coordinates (x, y) to polar coordinates (r, <i>theta</i>)
	 * @see Math#atan2(double, double)
	 */
	public static double atan2(final double y, final double x) {
		return Math.atan2(y, x);
	}
	
	/**
	 * Returns the result of {@code atan2(y, x)} divided by (pi * 2).
	 * 
	 * @param y the ordinate coordinate
	 * @param x the abscissa coordinate
	 * @return the result of {@code atan2(y, x)} divided by (pi * 2)
	 * @see #atan2(float, float)
	 */
	public static double atan2pi2(final double y, final double x) {
		return atan2(y, x) / PI_MULTIPLIED_BY_2;
	}
	
	/**
	 * Performs a bilinear interpolation operation on the supplied values.
	 * <p>
	 * Returns the result of the bilinear interpolation operation.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Doubles.lerp(Doubles.lerp(value00, value01, tX), Doubles.lerp(value10, value11, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param value00 a {@code double} value
	 * @param value01 a {@code double} value
	 * @param value10 a {@code double} value
	 * @param value11 a {@code double} value
	 * @param tX the X-axis factor
	 * @param tY the Y-axis factor
	 * @return the result of the bilinear interpolation operation
	 * @see #lerp(float, float, float)
	 */
	public static double blerp(final double value00, final double value01, final double value10, final double value11, final double tX, final double tY) {
		return lerp(lerp(value00, value01, tX), lerp(value10, value11, tX), tY);
	}
	
	/**
	 * Returns the smallest (closest to negative infinity) {@code double} value that is greater than or equal to {@code value} and is equal to a mathematical integer.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer, then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.</li>
	 * <li>If the argument value is less than zero but greater than -1.0, then the result is negative zero.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param value a value
	 * @return the smallest (closest to negative infinity) {@code double} value that is greater than or equal to {@code value} and is equal to a mathematical integer
	 * @see Math#ceil(double)
	 */
	public static double ceil(final double value) {
		return Math.ceil(value);
	}
	
	/**
	 * Returns the trigonometric cosine of {@code angleRadians}.
	 * <p>
	 * Special case:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param angleRadians an angle, in radians
	 * @return the trigonometric cosine of {@code angleRadians}
	 * @see Math#cos(double)
	 */
	public static double cos(final double angleRadians) {
		return Math.cos(angleRadians);
	}
	
	/**
	 * Returns Euler's number {@code e} raised to the power of {@code exponent}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN, the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive infinity.</li>
	 * <li>If the argument is negative infinity, then the result is positive zero.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param exponent the exponent to raise {@code e} to
	 * @return Euler's number {@code e} raised to the power of {@code exponent}
	 * @see Math#exp(double)
	 */
	public static double exp(final double exponent) {
		return Math.exp(exponent);
	}
	
	/**
	 * Returns the largest (closest to positive infinity) {@code double} value that is less than or equal to {@code value} and is equal to a mathematical integer.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer, then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.</li>
	 * </ul>
	 * 
	 * @param value a value
	 * @return the largest (closest to positive infinity) {@code double} value that is less than or equal to {@code value} and is equal to a mathematical integer
	 * @see Math#floor(double)
	 */
	public static double floor(final double value) {
		return Math.floor(value);
	}
	
	/**
	 * Returns the fractional part of {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Doubles.fractionalPart(value, false);
	 * }
	 * </pre>
	 * 
	 * @param value a value
	 * @return the fractional part of {@code value}
	 */
	public static double fractionalPart(final double value) {
		return fractionalPart(value, false);
	}
	
	/**
	 * Returns the fractional part of {@code value}.
	 * <p>
	 * The fractional part of {@code value} is calculated in the following way:
	 * <pre>
	 * {@code
	 * double fractionalPart = value < 0.0D && isUsingCeilOnNegativeValue ? ceil(value) - value : value - floor(value);
	 * }
	 * </pre>
	 * 
	 * @param value a value
	 * @param isUsingCeilOnNegativeValue {@code true} if, and only if, {@code Doubles.ceil(double)} should be used if {@code value} is negative, {@code false} otherwise
	 * @return the fractional part of {@code value}
	 */
	public static double fractionalPart(final double value, final boolean isUsingCeilOnNegativeValue) {
		return value < 0.0D && isUsingCeilOnNegativeValue ? ceil(value) - value : value - floor(value);
	}
	
	/**
	 * Returns {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise.
	 * 
	 * @param value the value to check
	 * @param threshold the threshold to use
	 * @param valueAdd the value that might be added to {@code value}
	 * @return {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise
	 */
	public static double getOrAdd(final double value, final double threshold, final double valueAdd) {
		return value < threshold ? value + valueAdd : value;
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns the result of the linear interpolation operation.
	 * 
	 * @param value0 a {@code double} value
	 * @param value1 a {@code double} value
	 * @param t the factor
	 * @return the result of the linear interpolation operation
	 */
	public static double lerp(final double value1, final double value2, final double t) {
		return (1.0D - t) * value1 + t * value2;
	}
	
	/**
	 * Returns the natural logarithm (base {@code e}) of the {@code double} value {@code value}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive infinity.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is negative infinity.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param value a value
	 * @return the natural logarithm (base {@code e}) of the {@code double} value {@code value}
	 * @see Math#log(double)
	 */
	public static double log(final double value) {
		return Math.log(value);
	}
	
	/**
	 * Returns the greater value of {@code a} and {@code b}.
	 * <p>
	 * The result is the argument closer to positive infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the other negative zero, the result is positive zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the greater value of {@code a} and {@code b}
	 * @see Math#max(double, double)
	 */
	public static double max(final double a, final double b) {
		return Math.max(a, b);
	}
	
	/**
	 * Returns the greater value of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * The result is the argument closer to positive infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the two others negative zero, the result is positive zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return the greater value of {@code a}, {@code b} and {@code c}
	 */
	public static double max(final double a, final double b, final double c) {
		return max(max(a, b), c);
	}
	
	/**
	 * Returns the smaller value of {@code a} and {@code b}.
	 * <p>
	 * The result is the value closer to negative infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the other is negative zero, the result is negative zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the smaller value of {@code a} and {@code b}
	 * @see Math#min(double, double)
	 */
	public static double min(final double a, final double b) {
		return Math.min(a, b);
	}
	
	/**
	 * Returns the smaller value of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * The result is the value closer to negative infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the others negative zero, the result is negative zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return the smaller value of {@code a}, {@code b} and {@code c}
	 */
	public static double min(final double a, final double b, final double c) {
		return min(min(a, b), c);
	}
	
	/**
	 * Returns the smaller value of {@code a} and {@code b}.
	 * <p>
	 * The result is the value closer to negative infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If one value is NaN, then the result is the other value. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive
	 * zero. If one argument is positive zero and the other is negative zero, the result is negative zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @return the smaller value of {@code a} and {@code b}
	 */
	public static double minOrNaN(final double a, final double b) {
		final boolean isNaNA = isNaN(a);
		final boolean isNaNB = isNaN(b);
		
		if(!isNaNA && !isNaNB) {
			return min(a, b);
		} else if(!isNaNA) {
			return a;
		} else if(!isNaNB) {
			return b;
		} else {
			return Double.NaN;
		}
	}
	
	/**
	 * Returns the normalized representation of {@code value}.
	 * <p>
	 * If {@code value} is greater than or equal to {@code min(a, b)} and less than or equal to {@code max(a, b)}, the normalized representation of {@code value} will be between {@code 0.0D} (inclusive) and {@code 1.0D} (inclusive).
	 * 
	 * @param value the {@code double} value to normalize
	 * @param a the {@code double} value that represents the minimum or maximum boundary
	 * @param b the {@code double} value that represents the maximum or minimum boundary
	 * @return the normalized representation of {@code value}
	 */
	public static double normalize(final double value, final double a, final double b) {
		final double maximum = max(a, b);
		final double minimum = min(a, b);
		final double valueNormalized = (value - minimum) / (maximum - minimum);
		
		return valueNormalized;
	}
	
	/**
	 * Returns {@code base} raised to the power of {@code exponent}.
	 * <p>
	 * For the full documentation of this method, see {@link Math#pow(double, double)}.
	 * 
	 * @param base the base
	 * @param exponent the exponent
	 * @return {@code base} raised to the power of {@code exponent}
	 * @see Math#pow(double, double)
	 */
	public static double pow(final double base, final double exponent) {
		return Math.pow(base, exponent);
	}
	
	/**
	 * Returns a pseudorandom {@code double} value between {@code 0.0D} (inclusive) and {@code 1.0D} (exclusive).
	 * 
	 * @return a pseudorandom {@code double} value between {@code 0.0D} (inclusive) and {@code 1.0D} (exclusive)
	 */
	public static double random() {
		return ThreadLocalRandom.current().nextDouble();
	}
	
	/**
	 * Returns the remainder of {@code x} and {@code y}.
	 * 
	 * @param x the left hand side of the remainder operation
	 * @param y the right hand side of the remainder operation
	 * @return the remainder of {@code x} and {@code y}
	 */
	public static double remainder(final double x, final double y) {
		return x - (int)(x / y) * y;
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
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Doubles.saturate(value, 0.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param value the value to saturate (or clamp)
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static double saturate(final double value) {
		return saturate(value, 0.0D, 1.0D);
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
	public static double saturate(final double value, final double edgeA, final double edgeB) {
		final double minimum = min(edgeA, edgeB);
		final double maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
	/**
	 * Returns the trigonometric sine of {@code angleRadians}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign as the argument.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param angleRadians an angle, in radians
	 * @return the trigonometric sine of {@code angleRadians}
	 * @see Math#sin(double)
	 */
	public static double sin(final double angleRadians) {
		return Math.sin(angleRadians);
	}
	
	/**
	 * Solves the cubic system for the quartic system based on the values {@code p}, {@code q} and {@code r}.
	 * <p>
	 * Returns a {@code double} with the result of the operation.
	 * 
	 * @param p a value
	 * @param q a value
	 * @param r a value
	 * @return a {@code double} with the result of the operation
	 */
	public static double solveCubicForQuartic(final double p, final double q, final double r) {
		final double pSquared = p * p;
		final double q0 = (pSquared - 3.0D * q) / 9.0D;
		final double q0Cubed = q0 * q0 * q0;
		final double r0 = (p * (pSquared - 4.5D * q) + 13.5D * r) / 27.0D;
		final double r0Squared = r0 * r0;
		final double d = q0Cubed - r0Squared;
		final double e = p / 3.0D;
		
		if(d >= 0.0D) {
			final double d0 = r0 / sqrt(q0Cubed);
			final double theta = acos(d0) / 3.0D;
			final double q1 = -2.0D * sqrt(q0);
			final double q2 = q1 * cos(theta) - e;
			
			return q2;
		}
		
		final double q1 = pow(sqrt(r0Squared - q0Cubed) + abs(r0), 1.0D / 3.0D);
		final double q2 = r0 < 0.0D ? (q1 + q0 / q1) - e : -(q1 + q0 / q1) - e;
		
		return q2;
	}
	
	/**
	 * Performs a smoothstep operation on {@code value} and the edges {@code edgeA} and {@code edgeB}.
	 * <p>
	 * Returns a {@code double} value.
	 * 
	 * @param value a {@code double} value
	 * @param edgeA one of the edges
	 * @param edgeB one of the edges
	 * @return a {@code double} value
	 */
	public static double smoothstep(final double value, final double edgeA, final double edgeB) {
		final double minimumValue = min(edgeA, edgeB);
		final double maximumValue = max(edgeA, edgeB);
		
		final double x = saturate((value - minimumValue) / (maximumValue - minimumValue), 0.0D, 1.0D);
		
		return x * x * (3.0D - 2.0D * x);
	}
	
	/**
	 * Returns the correctly rounded positive square root of {@code value}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or less than zero, then the result is NaN.</li>
	 * <li>If the argument is positive infinity, then the result is positive infinity.</li>
	 * <li>If the argument is positive zero or negative zero, then the result is the same as the argument.</li>
	 * </ul>
	 * <p>
	 * Otherwise, the result is the {@code double} value closest to the true mathematical square root of the argument value.
	 * 
	 * @param value a value
	 * @return the correctly rounded positive square root of {@code value}
	 * @see Math#sqrt(double)
	 */
	public static double sqrt(final double value) {
		return Math.sqrt(value);
	}
	
	/**
	 * Returns the trigonometric tangent of {@code angleRadians}.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument is NaN or an infinity, then the result is NaN.</li>
	 * <li>If the argument is zero, then the result is a zero with the same sign as the argument.</li>
	 * </ul>
	 * <p>
	 * The computed result must be within 1 ulp of the exact result. Results must be semi-monotonic.
	 * 
	 * @param angleRadians an angle, in radians
	 * @return the trigonometric tangent of {@code angleRadians}
	 * @see Math#tan(double)
	 */
	public static double tan(final double angleRadians) {
		return Math.tan(angleRadians);
	}
	
	/**
	 * Returns an approximately equivalent angle measured in degrees from an angle measured in radians.
	 * <p>
	 * The conversion from radians to degrees is generally inexact.
	 * 
	 * @param angleRadians an angle, in radians
	 * @return an approximately equivalent angle measured in degrees from an angle measured in radians
	 * @see Math#toDegrees(double)
	 */
	public static double toDegrees(final double angleRadians) {
		return Math.toDegrees(angleRadians);
	}
	
	/**
	 * Returns a {@code double} representation of a {@code float} value.
	 * 
	 * @param value a {@code float} value
	 * @return a {@code double} representation of a {@code float} value
	 */
	public static double toDouble(final float value) {
		return value;
	}
	
	/**
	 * Returns a {@code double} representation of an {@code int} value.
	 * 
	 * @param value an {@code int} value
	 * @return a {@code double} representation of an {@code int} value
	 */
	public static double toDouble(final int value) {
		return value;
	}
	
	/**
	 * Returns an approximately equivalent angle measured in radians from an angle measured in degrees.
	 * <p>
	 * The conversion from degrees to radians is generally inexact.
	 * 
	 * @param angleDegrees an angle, in degrees
	 * @return an approximately equivalent angle measured in radians from an angle measured in degrees
	 * @see Math#toRadians(double)
	 */
	public static double toRadians(final double angleDegrees) {
		return Math.toRadians(angleDegrees);
	}
	
	/**
	 * Returns {@code value} or its wrapped around representation.
	 * <p>
	 * If {@code value} is greater than or equal to {@code min(a, b)} and less than or equal to {@code max(a, b)}, {@code value} will be returned. Otherwise it will wrap around on either side until it is contained in the interval
	 * {@code [min(a, b), max(a, b)]}.
	 * 
	 * @param value the value to potentially wrap around
	 * @param a one of the values in the interval to wrap around
	 * @param b one of the values in the interval to wrap around
	 * @return {@code value} or its wrapped around representation
	 */
	public static double wrapAround(final double value, final double a, final double b) {
		final double minimumValue = min(a, b);
		final double maximumValue = max(a, b);
		
		double currentValue = value;
		
		while(currentValue < minimumValue || currentValue > maximumValue) {
			if(currentValue < minimumValue) {
				currentValue = maximumValue - (minimumValue - currentValue);
			} else if(currentValue > maximumValue) {
				currentValue = minimumValue + (currentValue - maximumValue);
			}
		}
		
		return currentValue;
	}
	
	/**
	 * Attempts to solve the quadratic system based on the values {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns a {@code double[]}, with a length of {@code 2}, that contains the result.
	 * <p>
	 * If the quadratic system could not be solved, the result will contain the values {@code Double.NaN}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return a {@code double[]}, with a length of {@code 2}, that contains the result
	 */
	public static double[] solveQuadraticSystem(final double a, final double b, final double c) {
		final double[] result = new double[] {Double.NaN, Double.NaN};
		
		final double discriminantSquared = b * b - 4.0D * a * c;
		
		if(discriminantSquared >= 0.0D) {
			final double discriminant = sqrt(discriminantSquared);
			final double quadratic = -0.5D * (b < 0.0D ? b - discriminant : b + discriminant);
			final double result0 = quadratic / a;
			final double result1 = equal(quadratic, 0.0D) ? result0 : c / quadratic;
			
			result[0] = min(result0, result1);
			result[1] = max(result0, result1);
		}
		
		return result;
	}
	
	/**
	 * Attempts to solve the quartic system based on the values {@code a}, {@code b}, {@code c}, {@code d} and {@code e}.
	 * <p>
	 * Returns a {@code double[]}, with a length of {@code 0}, {@code 2} or {@code 4}, that contains the result.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @param e a value
	 * @return a {@code double[]}, with a length of {@code 0}, {@code 2} or {@code 4}, that contains the result
	 */
	public static double[] solveQuartic(final double a, final double b, final double c, final double d, final double e) {
		final double aReciprocal = 1.0D / a;
		final double bA = b * aReciprocal;
		final double bASquared = bA * bA;
		final double cA = c * aReciprocal;
		final double dA = d * aReciprocal;
		final double eA = e * aReciprocal;
		final double p = -0.375D * bASquared + cA;
		final double q = 0.125D * bASquared * bA - 0.5D * bA * cA + dA;
		final double r = -0.01171875D * bASquared * bASquared + 0.0625D * bASquared * cA - 0.25D * bA * dA + eA;
		final double z = solveCubicForQuartic(-0.5D * p, -r, 0.5D * r * p - 0.125D * q * q);
		
		double d1 = 2.0D * z - p;
		double d2;
		
		if(d1 < 0.0D) {
			return new double[0];
		} else if(d1 < 1.0e-10D) {
			d2 = z * z - r;
			
			if(d2 < 0.0D) {
				return new double[0];
			}
			
			d2 = sqrt(d2);
		} else {
			d1 = sqrt(d1);
			d2 = 0.5D * q / d1;
		}
		
		final double q1 = d1 * d1;
		final double q2 = -0.25D * bA;
		final double pm = q1 - 4.0D * (z - d2);
		final double pp = q1 - 4.0D * (z + d2);
		
		if(pm >= 0.0D && pp >= 0.0D) {
			final double pmSqrt = sqrt(pm);
			final double ppSqrt = sqrt(pp);
			
			final double[] results = new double[4];
			
			results[0] = -0.5D * (d1 + pmSqrt) + q2;
			results[1] = -0.5D * (d1 - pmSqrt) + q2;
			results[2] = +0.5D * (d1 + ppSqrt) + q2;
			results[3] = +0.5D * (d1 - ppSqrt) + q2;
			
			for(int i = 1; i < 4; i++) {
				for(int j = i; j > 0 && results[j - 1] > results[j]; j--) {
					final double resultJ0 = results[j - 0];
					final double resultJ1 = results[j - 1];
					
					results[j - 0] = resultJ1;
					results[j - 1] = resultJ0;
				}
			}
			
			return results;
		} else if(pm >= 0.0D) {
			final double pmSqrt = sqrt(pm);
			
			return new double[] {
				-0.5D * (d1 + pmSqrt) + q2,
				-0.5D * (d1 - pmSqrt) + q2
			};
		} else if(pp >= 0.0D) {
			final double ppSqrt = sqrt(pp);
			
			return new double[] {
				+0.5D * (d1 - ppSqrt) + q2,
				+0.5D * (d1 + ppSqrt) + q2
			};
		} else {
			return new double[0];
		}
	}
}