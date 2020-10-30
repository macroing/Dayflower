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

import java.util.Random;

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
	 * The {@code double} value that represents a machine epsilon.
	 */
	public static final double MACHINE_EPSILON = Math.ulp(1.0D) * 0.5D;
	
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
	 * The reciprocal (or inverse) of {@link #PI_MULTIPLIED_BY_4}.
	 */
	public static final double PI_MULTIPLIED_BY_4_RECIPROCAL = 1.0D / PI_MULTIPLIED_BY_4;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI}.
	 */
	public static final double PI_RECIPROCAL = 1.0D / PI;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Random RANDOM = new Random(0L);
	private static final double SIMPLEX_F2 = 0.3660254037844386D;
	private static final double SIMPLEX_F3 = 1.0D / 3.0D;
	private static final double SIMPLEX_F4 = 0.30901699437494745D;
	private static final double SIMPLEX_G2 = 0.21132486540518713D;
	private static final double SIMPLEX_G3 = 1.0D / 6.0D;
	private static final double SIMPLEX_G4 = 0.1381966011250105D;
	private static final double[] SIMPLEX_GRADIENT_3 = doCreateSimplexGradient3();
	private static final double[] SIMPLEX_GRADIENT_4 = doCreateSimplexGradient4();
	private static final int[] PERMUTATIONS_B = doCreatePermutationsB();
	private static final int[] PERMUTATIONS_B_MODULO_12 = doCreatePermutationsBModulo12();
	
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
	 * Returns {@code true} if, and only if, {@code value} is infinitely large in magnitude, {@code false} otherwise.
	 * 
	 * @param value a {@code double} value
	 * @return {@code true} if, and only if, {@code value} is infinitely large in magnitude, {@code false} otherwise
	 */
	public static boolean isInfinite(final double value) {
		return Double.isInfinite(value);
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
	 * Returns the error of {@code value}.
	 * 
	 * @param value a {@code double} value
	 * @return the error of {@code value}
	 */
	public static double error(final double value) {
		final int sign = value < 0.0D ? -1 : +1;
		
		final double a1 = +0.254829592D;
		final double a2 = -0.284496736D;
		final double a3 = +1.421413741D;
		final double a4 = -1.453152027D;
		final double a5 = +1.061405429D;
		
		final double p = 0.3275911D;
		
		final double x = abs(value);
		final double y = 1.0D / (1.0D + p * x);
		final double z = 1.0D - (((((a5 * y + a4) * y) + a3) * y + a2) * y + a1) * y * exp(-x * x);
		
		return sign * z;
	}
	
	/**
	 * Returns the reciprocal (or inverse) error of {@code value}.
	 * 
	 * @param value a {@code double} value
	 * @return the reciprocal (or inverse) error of {@code value}
	 */
	public static double errorReciprocal(final double value) {
		double p = 0.0D;
		double x = saturate(value, -0.99999D, +0.99999D);
		double y = -log((1.0D - x) * (1.0D + x));
		
		if(y < 5.0D) {
			y = y - 2.5D;
			
			p = +2.81022636e-08D;
			p = +3.43273939e-07D + p * y;
			p = -3.5233877e-06D  + p * y;
			p = -4.39150654e-06D + p * y;
			p = +0.00021858087D  + p * y;
			p = -0.00125372503D  + p * y;
			p = -0.00417768164D  + p * y;
			p = +0.246640727D    + p * y;
			p = +1.50140941D     + p * y;
		} else {
			y = sqrt(y) - 3.0D;
			
			p = -0.000200214257D;
			p = +0.000100950558D + p * y;
			p = +0.00134934322D  + p * y;
			p = -0.00367342844D  + p * y;
			p = +0.00573950773D  + p * y;
			p = -0.0076224613D   + p * y;
			p = +0.00943887047D  + p * y;
			p = +1.00167406D     + p * y;
			p = +2.83297682D     + p * y;
		}
		
		return p * x;
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
	 * Returns the dielectric Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param f0 the reflectance at grazing angle
	 * @return the dielectric Fresnel reflectance based on Schlicks approximation
	 */
	public static double fresnelDielectricSchlick(final double cosTheta, final double f0) {
		return f0 + (1.0D - f0) * pow(max(1.0D - cosTheta, 0.0D), 5.0D);
	}
	
	/**
	 * Returns the gamma of {@code value}.
	 * 
	 * @param value an {@code int} value
	 * @return the gamma of {@code value}
	 */
	public static double gamma(final int value) {
		return (value * MACHINE_EPSILON) / (1.0D - value * MACHINE_EPSILON);
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
	 * Returns the greater value of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * The result is the argument closer to positive infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the two others negative zero, the result is positive zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @return the greater value of {@code a}, {@code b}, {@code c} and {@code d}
	 */
	public static double max(final double a, final double b, final double c, final double d) {
		return max(max(a, b), max(c, d));
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
	 * Returns the smaller value of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * The result is the value closer to negative infinity.
	 * <p>
	 * If the arguments have the same value, the result is that same value. If either value is NaN, then the result is NaN. Unlike the numerical comparison operators, this method considers negative zero to be strictly smaller than positive zero. If one
	 * argument is positive zero and the others negative zero, the result is negative zero.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @return the smaller value of {@code a}, {@code b}, {@code c} and {@code d}
	 */
	public static double min(final double a, final double b, final double c, final double d) {
		return min(min(a, b), min(c, d));
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
	 * Returns the {@code double} value next down after {@code value}.
	 * <p>
	 * This method is based on the function {@code NextFloatDown(float v)} in PBRT.
	 * 
	 * @param value a {@code double} value
	 * @return the {@code double} value next down after {@code value}
	 */
	public static double nextDownPBRT(final double value) {
		if(isInfinite(value) && value < 0.0D) {
			return value;
		}
		
		final double currentValue = equal(value, +0.0D) ? -0.0D : value;
		
		final long oldCurrentValueToLongBits = Double.doubleToLongBits(currentValue);
		final long newCurrentValueToLongBits = currentValue > 0.0D ? oldCurrentValueToLongBits - 1L : oldCurrentValueToLongBits + 1L;
		
		return Double.longBitsToDouble(newCurrentValueToLongBits);
	}
	
	/**
	 * Returns the {@code double} value next up after {@code value}.
	 * <p>
	 * This method is based on the function {@code NextFloatUp(float v)} in PBRT.
	 * 
	 * @param value a {@code double} value
	 * @return the {@code double} value next up after {@code value}
	 */
	public static double nextUpPBRT(final double value) {
		if(isInfinite(value) && value > 0.0D) {
			return value;
		}
		
		final double currentValue = equal(value, -0.0D) ? +0.0D : value;
		
		final long oldCurrentValueToLongBits = Double.doubleToLongBits(currentValue);
		final long newCurrentValueToLongBits = currentValue < 0.0D ? oldCurrentValueToLongBits - 1L : oldCurrentValueToLongBits + 1L;
		
		return Double.longBitsToDouble(newCurrentValueToLongBits);
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
	 * Returns a {@code double} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z
	 */
	public static double perlinFractalXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
	 */
	public static double perlinFractionalBrownianMotionXYZ(final double x, final double y, final double z, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Perlin algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code double} with noise computed by the Perlin algorithm using the coordinates X, Y and Z
	 */
	public static double perlinNoiseXYZ(final double x, final double y, final double z) {
		final double floorX = floor(x);
		final double floorY = floor(y);
		final double floorZ = floor(z);
		
		final int x0 = (int)(floorX) & 0xFF;
		final int y0 = (int)(floorY) & 0xFF;
		final int z0 = (int)(floorZ) & 0xFF;
		
		final double x1 = x - floorX;
		final double y1 = y - floorY;
		final double z1 = z - floorZ;
		
		final double u = x1 * x1 * x1 * (x1 * (x1 * 6.0D - 15.0D) + 10.0D);
		final double v = y1 * y1 * y1 * (y1 * (y1 * 6.0D - 15.0D) + 10.0D);
		final double w = z1 * z1 * z1 * (z1 * (z1 * 6.0D - 15.0D) + 10.0D);
		
		final int a0 = PERMUTATIONS_B[x0] + y0;
		final int a1 = PERMUTATIONS_B[a0] + z0;
		final int a2 = PERMUTATIONS_B[a0 + 1] + z0;
		final int b0 = PERMUTATIONS_B[x0 + 1] + y0;
		final int b1 = PERMUTATIONS_B[b0] + z0;
		final int b2 = PERMUTATIONS_B[b0 + 1] + z0;
		final int hash0 = PERMUTATIONS_B[a1] & 15;
		final int hash1 = PERMUTATIONS_B[b1] & 15;
		final int hash2 = PERMUTATIONS_B[a2] & 15;
		final int hash3 = PERMUTATIONS_B[b2] & 15;
		final int hash4 = PERMUTATIONS_B[a1 + 1] & 15;
		final int hash5 = PERMUTATIONS_B[b1 + 1] & 15;
		final int hash6 = PERMUTATIONS_B[a2 + 1] & 15;
		final int hash7 = PERMUTATIONS_B[b2 + 1] & 15;
		
		final double gradient0U = hash0 < 8 || hash0 == 12 || hash0 == 13 ? x1 : y1;
		final double gradient0V = hash0 < 4 || hash0 == 12 || hash0 == 13 ? y1 : z1;
		final double gradient0 = ((hash0 & 1) == 0 ? gradient0U : -gradient0U) + ((hash0 & 2) == 0 ? gradient0V : -gradient0V);
		final double gradient1U = hash1 < 8 || hash1 == 12 || hash1 == 13 ? x1 - 1.0D : y1;
		final double gradient1V = hash1 < 4 || hash1 == 12 || hash1 == 13 ? y1 : z1;
		final double gradient1 = ((hash1 & 1) == 0 ? gradient1U : -gradient1U) + ((hash1 & 2) == 0 ? gradient1V : -gradient1V);
		final double gradient2U = hash2 < 8 || hash2 == 12 || hash2 == 13 ? x1 : y1 - 1.0D;
		final double gradient2V = hash2 < 4 || hash2 == 12 || hash2 == 13 ? y1 - 1.0D : z1;
		final double gradient2 = ((hash2 & 1) == 0 ? gradient2U : -gradient2U) + ((hash2 & 2) == 0 ? gradient2V : -gradient2V);
		final double gradient3U = hash3 < 8 || hash3 == 12 || hash3 == 13 ? x1 - 1.0D : y1 - 1.0D;
		final double gradient3V = hash3 < 4 || hash3 == 12 || hash3 == 13 ? y1 - 1.0D : z1;
		final double gradient3 = ((hash3 & 1) == 0 ? gradient3U : -gradient3U) + ((hash3 & 2) == 0 ? gradient3V : -gradient3V);
		final double gradient4U = hash4 < 8 || hash4 == 12 || hash4 == 13 ? x1 : y1;
		final double gradient4V = hash4 < 4 || hash4 == 12 || hash4 == 13 ? y1 : z1 - 1.0D;
		final double gradient4 = ((hash4 & 1) == 0 ? gradient4U : -gradient4U) + ((hash4 & 2) == 0 ? gradient4V : -gradient4V);
		final double gradient5U = hash5 < 8 || hash5 == 12 || hash5 == 13 ? x1 - 1.0D : y1;
		final double gradient5V = hash5 < 4 || hash5 == 12 || hash5 == 13 ? y1 : z1 - 1.0D;
		final double gradient5 = ((hash5 & 1) == 0 ? gradient5U : -gradient5U) + ((hash5 & 2) == 0 ? gradient5V : -gradient5V);
		final double gradient6U = hash6 < 8 || hash6 == 12 || hash6 == 13 ? x1 : y1 - 1.0D;
		final double gradient6V = hash6 < 4 || hash6 == 12 || hash6 == 13 ? y1 - 1.0D : z1 - 1.0D;
		final double gradient6 = ((hash6 & 1) == 0 ? gradient6U : -gradient6U) + ((hash6 & 2) == 0 ? gradient6V : -gradient6V);
		final double gradient7U = hash7 < 8 || hash7 == 12 || hash7 == 13 ? x1 - 1.0D : y1 - 1.0D;
		final double gradient7V = hash7 < 4 || hash7 == 12 || hash7 == 13 ? y1 - 1.0D : z1 - 1.0D;
		final double gradient7 = ((hash7 & 1) == 0 ? gradient7U : -gradient7U) + ((hash7 & 2) == 0 ? gradient7V : -gradient7V);
		
		final double lerp0 = gradient0 + u * (gradient1 - gradient0);
		final double lerp1 = gradient2 + u * (gradient3 - gradient2);
		final double lerp2 = gradient4 + u * (gradient5 - gradient4);
		final double lerp3 = gradient6 + u * (gradient7 - gradient6);
		final double lerp4 = lerp0 + v * (lerp1 - lerp0);
		final double lerp5 = lerp2 + v * (lerp3 - lerp2);
		final double lerp6 = lerp4 + w * (lerp5 - lerp4);
		
		return lerp6;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static double perlinTurbulenceXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static double perlinTurbulenceXYZ(final double x, final double y, final double z, final int octaves) {
		double currentX = x;
		double currentY = y;
		double currentZ = z;
		
		double noise = abs(perlinNoiseXYZ(x, y, z));
		
		double weight = 1.0D;
		
		for(int i = 1; i < octaves; i++) {
			weight *= 2.0D;
			
			currentX = x * weight;
			currentY = y * weight;
			currentZ = z * weight;
			
			noise += abs(perlinNoiseXYZ(currentX, currentY, currentZ)) / weight;
		}
		
		return noise;
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
//		return ThreadLocalRandom.current().nextDouble();
		return RANDOM.nextDouble();
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
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinate X
	 */
	public static double simplexFractalX(final double x, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseX(x * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y
	 */
	public static double simplexFractalXY(final double x, final double y, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXY(x * currentFrequency, y * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z
	 */
	public static double simplexFractalXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W
	 */
	public static double simplexFractalXYZW(final double x, final double y, final double z, final double w, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double result = 0.0D;
		
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X
	 */
	public static double simplexFractionalBrownianMotionX(final double x, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseX(x * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y
	 */
	public static double simplexFractionalBrownianMotionXY(final double x, final double y, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXY(x * currentFrequency, y * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
	 */
	public static double simplexFractionalBrownianMotionXYZ(final double x, final double y, final double z, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W
	 */
	public static double simplexFractionalBrownianMotionXYZW(final double x, final double y, final double z, final double w, final double frequency, final double gain, final double minimum, final double maximum, final int octaves) {
		double currentAmplitude = 1.0D;
		double maximumAmplitude = 0.0D;
		
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0D;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0D + (maximum + minimum) / 2.0D;
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinate X
	 */
	public static double simplexNoiseX(final double x) {
		final int i0 = doFastFloorToInt(x);
		final int i1 = i0 + 1;
		
		final double x0 = x - i0;
		final double x1 = x0 - 1.0D;
		
		final double t00 = 1.0D - x0 * x0;
		final double t01 = t00 * t00;
		
		final double t10 = 1.0D - x1 * x1;
		final double t11 = t10 * t10;
		
		final int hash00 = PERMUTATIONS_B[Ints.abs(i0) % PERMUTATIONS_B.length];
		final int hash01 = hash00 & 0x0F;
		final int hash10 = PERMUTATIONS_B[Ints.abs(i1) % PERMUTATIONS_B.length];
		final int hash11 = hash10 & 0x0F;
		
		final double gradient00 = 1.0D + (hash01 & 7);
		final double gradient01 = (hash01 & 8) != 0 ? -gradient00 : gradient00;
		final double gradient02 = gradient01 * x0;
		final double gradient10 = 1.0D + (hash11 & 7);
		final double gradient11 = (hash11 & 8) != 0 ? -gradient10 : gradient10;
		final double gradient12 = gradient11 * x1;
		
		final double n0 = t01 * t01 * gradient02;
		final double n1 = t11 * t11 * gradient12;
		
		return 0.395D * (n0 + n1);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X and Y
	 */
	public static double simplexNoiseXY(final double x, final double y) {
		final double s = (x + y) * SIMPLEX_F2;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		
		final double t = (i + j) * SIMPLEX_G2;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		
		final int i1 = x0 > y0 ? 1 : 0;
		final int j1 = x0 > y0 ? 0 : 1;
		
		final double x1 = x0 - i1 + SIMPLEX_G2;
		final double y1 = y0 - j1 + SIMPLEX_G2;
		final double x2 = x0 - 1.0D + 2.0D * SIMPLEX_G2;
		final double y2 = y0 - 1.0D + 2.0D * SIMPLEX_G2;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1]];
		
		final double t0 = 0.5D - x0 * x0 - y0 * y0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0);
		
		final double t1 = 0.5D - x1 * x1 - y1 * y1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1);
		
		final double t2 = 0.5D - x2 * x2 - y2 * y2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2);
		
		return 70.0D * (n0 + n1 + n2);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y and Z
	 */
	public static double simplexNoiseXYZ(final double x, final double y, final double z) {
		final double s = (x + y + z) * SIMPLEX_F3;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		
		final double t = (i + j + k) * SIMPLEX_G3;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		final double z0 = z - (k - t);
		
		int i1 = 0;
		int j1 = 0;
		int k1 = 0;
		int i2 = 0;
		int j2 = 0;
		int k2 = 0;
		
		if(x0 >= y0) {
			if(y0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			} else if(x0 >= z0) {
				i1 = 1;
				j1 = 0;
				k1 = 0;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			} else {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 1;
				j2 = 0;
				k2 = 1;
			}
		} else {
			if(y0 < z0) {
				i1 = 0;
				j1 = 0;
				k1 = 1;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else if(x0 < z0) {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 0;
				j2 = 1;
				k2 = 1;
			} else {
				i1 = 0;
				j1 = 1;
				k1 = 0;
				i2 = 1;
				j2 = 1;
				k2 = 0;
			}
		}
		
		final double x1 = x0 - i1 + SIMPLEX_G3;
		final double y1 = y0 - j1 + SIMPLEX_G3;
		final double z1 = z0 - k1 + SIMPLEX_G3;
		final double x2 = x0 - i2 + 2.0D * SIMPLEX_G3;
		final double y2 = y0 - j2 + 2.0D * SIMPLEX_G3;
		final double z2 = z0 - k2 + 2.0D * SIMPLEX_G3;
		final double x3 = x0 - 1.0D + 3.0D * SIMPLEX_G3;
		final double y3 = y0 - 1.0D + 3.0D * SIMPLEX_G3;
		final double z3 = z0 - 1.0D + 3.0D * SIMPLEX_G3;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0]]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1]]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2]]];
		final int gi3 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1]]];
		
		final double t0 = 0.6D - x0 * x0 - y0 * y0 - z0 * z0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 2] * z0);
		
		final double t1 = 0.6D - x1 * x1 - y1 * y1 - z1 * z1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 2] * z1);
		
		final double t2 = 0.6D - x2 * x2 - y2 * y2 - z2 * z2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 2] * z2);
		
		final double t3 = 0.6D - x3 * x3 - y3 * y3 - z3 * z3;
		final double n3 = t3 < 0.0D ? 0.0D : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_3[gi3 * 3 + 0] * x3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 1] * y3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 2] * z3);
		
		return 32.0D * (n0 + n1 + n2 + n3);
	}
	
	/**
	 * Returns a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @return a {@code double} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W
	 */
	public static double simplexNoiseXYZW(final double x, final double y, final double z, final double w) {
		final double s = (x + y + z + w) * SIMPLEX_F4;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		final int l = doFastFloorToInt(w + s);
		
		final double t = (i + j + k + l) * SIMPLEX_G4;
		
		final double x0 = x - (i - t);
		final double y0 = y - (j - t);
		final double z0 = z - (k - t);
		final double w0 = w - (l - t);
		
		int rankX = 0;
		int rankY = 0;
		int rankZ = 0;
		int rankW = 0;
		
		if(x0 > y0) {
			rankX++;
		} else {
			rankY++;
		}
		
		if(x0 > z0) {
			rankX++;
		} else {
			rankZ++;
		}
		
		if(x0 > w0) {
			rankX++;
		} else {
			rankW++;
		}
		
		if(y0 > z0) {
			rankY++;
		} else {
			rankZ++;
		}
		
		if(y0 > w0) {
			rankY++;
		} else {
			rankW++;
		}
		
		if(z0 > w0) {
			rankZ++;
		} else {
			rankW++;
		}
		
		final int i1 = rankX >= 3 ? 1 : 0;
		final int j1 = rankY >= 3 ? 1 : 0;
		final int k1 = rankZ >= 3 ? 1 : 0;
		final int l1 = rankW >= 3 ? 1 : 0;
		final int i2 = rankX >= 2 ? 1 : 0;
		final int j2 = rankY >= 2 ? 1 : 0;
		final int k2 = rankZ >= 2 ? 1 : 0;
		final int l2 = rankW >= 2 ? 1 : 0;
		final int i3 = rankX >= 1 ? 1 : 0;
		final int j3 = rankY >= 1 ? 1 : 0;
		final int k3 = rankZ >= 1 ? 1 : 0;
		final int l3 = rankW >= 1 ? 1 : 0;
		
		final double x1 = x0 - i1 + SIMPLEX_G4;
		final double y1 = y0 - j1 + SIMPLEX_G4;
		final double z1 = z0 - k1 + SIMPLEX_G4;
		final double w1 = w0 - l1 + SIMPLEX_G4;
		final double x2 = x0 - i2 + 2.0D * SIMPLEX_G4;
		final double y2 = y0 - j2 + 2.0D * SIMPLEX_G4;
		final double z2 = z0 - k2 + 2.0D * SIMPLEX_G4;
		final double w2 = w0 - l2 + 2.0D * SIMPLEX_G4;
		final double x3 = x0 - i3 + 3.0D * SIMPLEX_G4;
		final double y3 = y0 - j3 + 3.0D * SIMPLEX_G4;
		final double z3 = z0 - k3 + 3.0D * SIMPLEX_G4;
		final double w3 = w0 - l3 + 3.0D * SIMPLEX_G4;
		final double x4 = x0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double y4 = y0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double z4 = z0 - 1.0D + 4.0D * SIMPLEX_G4;
		final double w4 = w0 - 1.0D + 4.0D * SIMPLEX_G4;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		final int ll = l & 0xFF;
		
		final int gi0 = PERMUTATIONS_B[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0 + PERMUTATIONS_B[ll +  0]]]] % 32;
		final int gi1 = PERMUTATIONS_B[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1 + PERMUTATIONS_B[ll + l1]]]] % 32;
		final int gi2 = PERMUTATIONS_B[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2 + PERMUTATIONS_B[ll + l2]]]] % 32;
		final int gi3 = PERMUTATIONS_B[ii + i3 + PERMUTATIONS_B[jj + j3 + PERMUTATIONS_B[kk + k3 + PERMUTATIONS_B[ll + l3]]]] % 32;
		final int gi4 = PERMUTATIONS_B[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1 + PERMUTATIONS_B[ll +  1]]]] % 32;
		
		final double t0 = 0.6D - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		final double n0 = t0 < 0.0D ? 0.0D : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_4[gi0 * 4 + 0] * x0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 1] * y0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 2] * z0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 3] * w0);
		
		final double t1 = 0.6D - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		final double n1 = t1 < 0.0D ? 0.0D : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_4[gi1 * 4 + 0] * x1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 1] * y1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 2] * z1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 3] * w1);
		
		final double t2 = 0.6D - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		final double n2 = t2 < 0.0D ? 0.0D : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_4[gi2 * 4 + 0] * x2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 1] * y2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 2] * z2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 3] * w2);
		
		final double t3 = 0.6D - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		final double n3 = t3 < 0.0D ? 0.0D : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_4[gi3 * 4 + 0] * x3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 1] * y3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 2] * z3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 3] * w3);
		
		final double t4 = 0.6D - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		final double n4 = t4 < 0.0D ? 0.0D : (t4 * t4) * (t4 * t4) * (SIMPLEX_GRADIENT_4[gi4 * 4 + 0] * x4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 1] * y4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 2] * z4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 3] * w4);
		
		return 27.0D * (n0 + n1 + n2 + n3 + n4);
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinate X
	 */
	public static double simplexTurbulenceX(final double x, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseX(x * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y
	 */
	public static double simplexTurbulenceXY(final double x, final double y, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseXY(x * currentFrequency, y * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static double simplexTurbulenceXYZ(final double x, final double y, final double z, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code double} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W
	 */
	public static double simplexTurbulenceXYZW(final double x, final double y, final double z, final double w, final double amplitude, final double frequency, final double gain, final double lacunarity, final int octaves) {
		double currentAmplitude = amplitude;
		double currentFrequency = frequency;
		
		double noise = 0.0D;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
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
	 * Returns the hyperbolic sine of a {@code value}.
	 * 
	 * @param value a {@code double} value
	 * @return the hyperbolic sine of a {@code value}
	 */
	public static double sinh(final double value) {
		return Math.sinh(value);
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
		
		if(equal(discriminantSquared, 0.0D)) {
			final double q = -0.5D * b / a;
			
			final double result0 = q;
			final double result1 = q;
			
			result[0] = result0;
			result[1] = result1;
		} else if(discriminantSquared > 0.0D) {
			final double discriminant = sqrt(discriminantSquared);
			
			final double q = -0.5D * (b > 0.0D ? b + discriminant : b - discriminant);
			
			final double result0 = q / a;
			final double result1 = c / q;
			
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doCreateSimplexGradient3() {
		return new double[] {
			+1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +0.0D,
			+1.0D, +0.0D, +1.0D, -1.0D, +0.0D, +1.0D, +1.0D, +0.0D, -1.0D, -1.0D, +0.0D, -1.0D,
			+0.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D
		};
	}
	
	private static double[] doCreateSimplexGradient4() {
		return new double[] {
			+0.0D, +1.0D, +1.0D, +1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, -1.0D,
			+0.0D, -1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D,
			+1.0D, +0.0D, +1.0D, +1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, -1.0D, +1.0D, +1.0D, +0.0D, -1.0D, -1.0D,
			-1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D,
			+1.0D, +1.0D, +0.0D, +1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, -1.0D,
			-1.0D, +1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D, -1.0D,
			+1.0D, +1.0D, +1.0D, +0.0D, +1.0D, +1.0D, -1.0D, +0.0D, +1.0D, -1.0D, +1.0D, +0.0D, +1.0D, -1.0D, -1.0D, +0.0D,
			-1.0D, +1.0D, +1.0D, +0.0D, -1.0D, +1.0D, -1.0D, +0.0D, -1.0D, -1.0D, +1.0D, +0.0D, -1.0D, -1.0D, -1.0D, +0.0D
		};
	}
	
	private static int doFastFloorToInt(final double value) {
		final int i = (int)(value);
		
		return value < i ? i - 1 : i;
	}
	
	private static int[] doCreatePermutationsA() {
		return new int[] {
			151, 160, 137,  91,  90,  15, 131,  13, 201,  95,  96,  53, 194, 233,   7, 225,
			140,  36, 103,  30,  69, 142,   8,  99,  37, 240,  21,  10,  23, 190,   6, 148,
			247, 120, 234,  75,   0,  26, 197,  62,  94, 252, 219, 203, 117,  35,  11,  32,
			 57, 177,  33,  88, 237, 149,  56,  87, 174,  20, 125, 136, 171, 168,  68, 175,
			 74, 165,  71, 134, 139,  48,  27, 166,  77, 146, 158, 231,  83, 111, 229, 122,
			 60, 211, 133, 230, 220, 105,  92,  41,  55,  46, 245,  40, 244, 102, 143,  54,
			 65,  25,  63, 161,   1, 216,  80,  73, 209,  76, 132, 187, 208,  89,  18, 169,
			200, 196, 135, 130, 116, 188, 159,  86, 164, 100, 109, 198, 173, 186,   3,  64,
			 52, 217, 226, 250, 124, 123,   5, 202,  38, 147, 118, 126, 255,  82,  85, 212,
			207, 206,  59, 227,  47,  16,  58,  17, 182, 189,  28,  42,  23, 183, 170, 213,
			119, 248, 152,   2,  44, 154, 163,  70, 221, 153, 101, 155, 167,  43, 172,   9,
			129,  22,  39, 253,  19,  98, 108, 110,  79, 113, 224, 232, 178, 185, 112, 104,
			218, 246,  97, 228, 251,  34, 242, 193, 238, 210, 144,  12, 191, 179, 162, 241,
			 81,  51, 145, 235, 249,  14, 239, 107,  49, 192, 214,  31, 181, 199, 106, 157,
			184,  84, 204, 176, 115, 121,  50,  45, 127,   4, 150, 254, 138, 236, 205,  93,
			222, 114,  67,  29,  24,  72, 243, 141, 128, 195,  78,  66, 215,  61, 156, 180
		};
	}
	
	private static int[] doCreatePermutationsB() {
		final int[] permutationsA = doCreatePermutationsA();
		final int[] permutationsB = new int[permutationsA.length * 2];
		
		for(int i = 0; i < permutationsB.length; i++) {
			permutationsB[i] = permutationsA[i % permutationsA.length];
		}
		
		return permutationsB;
	}
	
	private static int[] doCreatePermutationsBModulo12() {
		final int[] permutationsBModulo12 = doCreatePermutationsB();
		
		for(int i = 0; i < permutationsBModulo12.length; i++) {
			permutationsBModulo12[i] = permutationsBModulo12[i] % 12;
		}
		
		return permutationsBModulo12;
	}
}