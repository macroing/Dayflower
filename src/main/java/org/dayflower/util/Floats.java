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
 * The class {@code Floats} contains methods for performing basic numeric operations such as the elementary exponential, logarithm, square root and trigonometric functions.
 * <p>
 * You can think of this class as an extension of {@code Math}. It does what {@code Math} does for the {@code double}, {@code float} and {@code int} types, but for the {@code float} type only. In addition to this it also adds new methods.
 * <p>
 * This class does not contain all methods from {@code Math}. The methods in {@code Math} that deals with the {@code double} type can be found in the class {@link Doubles}. The methods in {@code Math} that deals with the {@code int} type can be found
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
public final class Floats {
	/**
	 * The {@code float} value that represents a machine epsilon.
	 */
	public static final float MACHINE_EPSILON = Math.ulp(1.0F) * 0.5F;
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter.
	 */
	public static final float PI = (float)(Math.PI);
//	public static final float PI = toFloat(Math.PI); This will cause Aparapi (a GPU-acceleration library for Java) to crash. So do not use methods when declaring constants, unless it is necessary. This comment should be used as a reminder.
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 180.0.
	 */
	public static final float PI_DIVIDED_BY_180 = PI / 180.0F;
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 2.0.
	 */
	public static final float PI_DIVIDED_BY_2 = PI / 2.0F;
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, divided by 4.0.
	 */
	public static final float PI_DIVIDED_BY_4 = PI / 4.0F;
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, multiplied by 2.0.
	 */
	public static final float PI_MULTIPLIED_BY_2 = PI * 2.0F;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI_MULTIPLIED_BY_2}.
	 */
	public static final float PI_MULTIPLIED_BY_2_RECIPROCAL = 1.0F / PI_MULTIPLIED_BY_2;
	
	/**
	 * The {@code float} value that is closer than any other to pi, the ratio of the circumference of a circle to its diameter, multiplied by 4.0.
	 */
	public static final float PI_MULTIPLIED_BY_4 = PI * 4.0F;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI_MULTIPLIED_BY_4}.
	 */
	public static final float PI_MULTIPLIED_BY_4_RECIPROCAL = 1.0F / PI_MULTIPLIED_BY_4;
	
	/**
	 * The reciprocal (or inverse) of {@link #PI}.
	 */
	public static final float PI_RECIPROCAL = 1.0F / PI;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Random RANDOM = new Random(0L);
	private static final float SIMPLEX_F2 = 0.3660254037844386F;
	private static final float SIMPLEX_F3 = 1.0F / 3.0F;
	private static final float SIMPLEX_F4 = 0.30901699437494745F;
	private static final float SIMPLEX_G2 = 0.21132486540518713F;
	private static final float SIMPLEX_G3 = 1.0F / 6.0F;
	private static final float SIMPLEX_G4 = 0.1381966011250105F;
	private static final float[] SIMPLEX_GRADIENT_3 = doCreateSimplexGradient3();
	private static final float[] SIMPLEX_GRADIENT_4 = doCreateSimplexGradient4();
	private static final int[] PERMUTATIONS_B = doCreatePermutationsB();
	private static final int[] PERMUTATIONS_B_MODULO_12 = doCreatePermutationsBModulo12();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Floats() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code a} is equal to {@code b}, {@code false} otherwise.
	 * 
	 * @param a a {@code float} value
	 * @param b a {@code float} value
	 * @return {@code true} if, and only if, {@code a} is equal to {@code b}, {@code false} otherwise
	 */
	public static boolean equal(final float a, final float b) {
		return Float.compare(a, b) == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code a} is equal to {@code b} and {@code b} is equal to {@code c}, {@code false} otherwise.
	 * 
	 * @param a a {@code float} value
	 * @param b a {@code float} value
	 * @param c a {@code float} value
	 * @return {@code true} if, and only if, {@code a} is equal to {@code b} and {@code b} is equal to {@code c}, {@code false} otherwise
	 */
	public static boolean equal(final float a, final float b, final float c) {
		return equal(a, b) && equal(b, c);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is infinitely large in magnitude, {@code false} otherwise.
	 * 
	 * @param value a {@code float} value
	 * @return {@code true} if, and only if, {@code value} is infinitely large in magnitude, {@code false} otherwise
	 */
	public static boolean isInfinite(final float value) {
		return Float.isInfinite(value);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @param value a {@code float} value
	 * @return {@code true} if, and only if, {@code value} is {@code Float.NaN}, {@code false} otherwise
	 */
	public static boolean isNaN(final float value) {
		return Float.isNaN(value);
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
	 * Float.intBitsToFloat(0x7fffffff & Float.floatToIntBits(value))
	 * }
	 * </pre>
	 * 
	 * @param value a {@code float} value
	 * @return the absolute version of {@code value}
	 * @see Math#abs(float)
	 */
	public static float abs(final float value) {
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
	public static float acos(final float value) {
		return toFloat(Math.acos(value));
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
	public static float asin(final float value) {
		return toFloat(Math.asin(value));
	}
	
	/**
	 * Returns the result of {@code asin(value)} divided by pi.
	 * 
	 * @param value the value whose arc sine divided by pi is to be returned
	 * @return the result of {@code asin(value)} divided by pi
	 * @see #asin(float)
	 */
	public static float asinpi(final float value) {
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
	public static float atan(final float value) {
		return toFloat(Math.atan(value));
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
	public static float atan2(final float y, final float x) {
		return toFloat(Math.atan2(y, x));
	}
	
	/**
	 * Returns the result of {@code atan2(y, x)} divided by (pi * 2).
	 * 
	 * @param y the ordinate coordinate
	 * @param x the abscissa coordinate
	 * @return the result of {@code atan2(y, x)} divided by (pi * 2)
	 * @see #atan2(float, float)
	 */
	public static float atan2pi2(final float y, final float x) {
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
	 * Floats.lerp(Floats.lerp(value00, value01, tX), Floats.lerp(value10, value11, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param value00 a {@code float} value
	 * @param value01 a {@code float} value
	 * @param value10 a {@code float} value
	 * @param value11 a {@code float} value
	 * @param tX the X-axis factor
	 * @param tY the Y-axis factor
	 * @return the result of the bilinear interpolation operation
	 * @see #lerp(float, float, float)
	 */
	public static float blerp(final float value00, final float value01, final float value10, final float value11, final float tX, final float tY) {
		return lerp(lerp(value00, value01, tX), lerp(value10, value11, tX), tY);
	}
	
	/**
	 * Returns the smallest (closest to negative infinity) {@code float} value that is greater than or equal to {@code value} and is equal to a mathematical integer.
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
	 * @return the smallest (closest to negative infinity) {@code float} value that is greater than or equal to {@code value} and is equal to a mathematical integer
	 * @see Math#ceil(double)
	 */
	public static float ceil(final float value) {
		return toFloat(Math.ceil(value));
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
	public static float cos(final float angleRadians) {
		return toFloat(Math.cos(angleRadians));
	}
	
	/**
	 * Returns the error of {@code value}.
	 * 
	 * @param value a {@code float} value
	 * @return the error of {@code value}
	 */
	public static float error(final float value) {
		final int sign = value < 0.0F ? -1 : +1;
		
		final float a1 = +0.254829592F;
		final float a2 = -0.284496736F;
		final float a3 = +1.421413741F;
		final float a4 = -1.453152027F;
		final float a5 = +1.061405429F;
		
		final float p = 0.3275911F;
		
		final float x = abs(value);
		final float y = 1.0F / (1.0F + p * x);
		final float z = 1.0F - (((((a5 * y + a4) * y) + a3) * y + a2) * y + a1) * y * exp(-x * x);
		
		return sign * z;
	}
	
	/**
	 * Returns the reciprocal (or inverse) error of {@code value}.
	 * 
	 * @param value a {@code float} value
	 * @return the reciprocal (or inverse) error of {@code value}
	 */
	public static float errorReciprocal(final float value) {
		float p = 0.0F;
		float x = saturate(value, -0.99999F, +0.99999F);
		float y = -log((1.0F - x) * (1.0F + x));
		
		if(y < 5.0F) {
			y = y - 2.5F;
			
			p = +2.81022636e-08F;
			p = +3.43273939e-07F + p * y;
			p = -3.5233877e-06F  + p * y;
			p = -4.39150654e-06F + p * y;
			p = +0.00021858087F  + p * y;
			p = -0.00125372503F  + p * y;
			p = -0.00417768164F  + p * y;
			p = +0.246640727F    + p * y;
			p = +1.50140941F     + p * y;
		} else {
			y = sqrt(y) - 3.0F;
			
			p = -0.000200214257F;
			p = +0.000100950558F + p * y;
			p = +0.00134934322F  + p * y;
			p = -0.00367342844F  + p * y;
			p = +0.00573950773F  + p * y;
			p = -0.0076224613F   + p * y;
			p = +0.00943887047F  + p * y;
			p = +1.00167406F     + p * y;
			p = +2.83297682F     + p * y;
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
	public static float exp(final float exponent) {
		return toFloat(Math.exp(exponent));
	}
	
	/**
	 * Returns the largest (closest to positive infinity) {@code float} value that is less than or equal to {@code value} and is equal to a mathematical integer.
	 * <p>
	 * Special cases:
	 * <ul>
	 * <li>If the argument value is already equal to a mathematical integer, then the result is the same as the argument.</li>
	 * <li>If the argument is NaN or an infinity or positive zero or negative zero, then the result is the same as the argument.</li>
	 * </ul>
	 * 
	 * @param value a value
	 * @return the largest (closest to positive infinity) {@code float} value that is less than or equal to {@code value} and is equal to a mathematical integer
	 * @see Math#floor(double)
	 */
	public static float floor(final float value) {
		return toFloat(Math.floor(value));
	}
	
	/**
	 * Returns the fractional part of {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Floats.fractionalPart(value, false);
	 * }
	 * </pre>
	 * 
	 * @param value a value
	 * @return the fractional part of {@code value}
	 */
	public static float fractionalPart(final float value) {
		return fractionalPart(value, false);
	}
	
	/**
	 * Returns the fractional part of {@code value}.
	 * <p>
	 * The fractional part of {@code value} is calculated in the following way:
	 * <pre>
	 * {@code
	 * float fractionalPart = value < 0.0F && isUsingCeilOnNegativeValue ? ceil(value) - value : value - floor(value);
	 * }
	 * </pre>
	 * 
	 * @param value a value
	 * @param isUsingCeilOnNegativeValue {@code true} if, and only if, {@code Floats.ceil(float)} should be used if {@code value} is negative, {@code false} otherwise
	 * @return the fractional part of {@code value}
	 */
	public static float fractionalPart(final float value, final boolean isUsingCeilOnNegativeValue) {
		return value < 0.0F && isUsingCeilOnNegativeValue ? ceil(value) - value : value - floor(value);
	}
	
	/**
	 * Returns the dielectric Fresnel reflectance based on Schlicks approximation.
	 * 
	 * @param cosTheta the cosine of the angle theta
	 * @param f0 the reflectance at grazing angle
	 * @return the dielectric Fresnel reflectance based on Schlicks approximation
	 */
	public static float fresnelDielectricSchlick(final float cosTheta, final float f0) {
		return f0 + (1.0F - f0) * pow(max(1.0F - cosTheta, 0.0F), 5.0F);
	}
	
	/**
	 * Returns the gamma of {@code value}.
	 * 
	 * @param value an {@code int} value
	 * @return the gamma of {@code value}
	 */
	public static float gamma(final int value) {
		return (value * MACHINE_EPSILON) / (1.0F - value * MACHINE_EPSILON);
	}
	
	/**
	 * Returns {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise.
	 * 
	 * @param value the value to check
	 * @param threshold the threshold to use
	 * @param valueAdd the value that might be added to {@code value}
	 * @return {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise
	 */
	public static float getOrAdd(final float value, final float threshold, final float valueAdd) {
		return value < threshold ? value + valueAdd : value;
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns the result of the linear interpolation operation.
	 * 
	 * @param value0 a {@code float} value
	 * @param value1 a {@code float} value
	 * @param t the factor
	 * @return the result of the linear interpolation operation
	 */
	public static float lerp(final float value1, final float value2, final float t) {
		return (1.0F - t) * value1 + t * value2;
	}
	
	/**
	 * Returns the natural logarithm (base {@code e}) of the {@code float} value {@code value}.
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
	 * @return the natural logarithm (base {@code e}) of the {@code float} value {@code value}
	 * @see Math#log(double)
	 */
	public static float log(final float value) {
		return toFloat(Math.log(value));
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
	 * @see Math#max(float, float)
	 */
	public static float max(final float a, final float b) {
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
	public static float max(final float a, final float b, final float c) {
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
	 * @see Math#min(float, float)
	 */
	public static float min(final float a, final float b) {
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
	public static float min(final float a, final float b, final float c) {
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
	public static float minOrNaN(final float a, final float b) {
		final boolean isNaNA = isNaN(a);
		final boolean isNaNB = isNaN(b);
		
		if(!isNaNA && !isNaNB) {
			return min(a, b);
		} else if(!isNaNA) {
			return a;
		} else if(!isNaNB) {
			return b;
		} else {
			return Float.NaN;
		}
	}
	
	/**
	 * Returns the {@code float} value next down after {@code value}.
	 * <p>
	 * This method is based on the function {@code NextFloatDown(float v)} in PBRT.
	 * 
	 * @param value a {@code float} value
	 * @return the {@code float} value next down after {@code value}
	 */
	public static float nextDownPBRT(final float value) {
		if(isInfinite(value) && value < 0.0F) {
			return value;
		}
		
		final float currentValue = equal(value, +0.0F) ? -0.0F : value;
		
		final int oldCurrentValueToIntBits = Float.floatToIntBits(currentValue);
		final int newCurrentValueToIntBits = currentValue > 0.0F ? oldCurrentValueToIntBits - 1 : oldCurrentValueToIntBits + 1;
		
		return Float.intBitsToFloat(newCurrentValueToIntBits);
	}
	
	/**
	 * Returns the {@code float} value next up after {@code value}.
	 * <p>
	 * This method is based on the function {@code NextFloatUp(float v)} in PBRT.
	 * 
	 * @param value a {@code float} value
	 * @return the {@code float} value next up after {@code value}
	 */
	public static float nextUpPBRT(final float value) {
		if(isInfinite(value) && value > 0.0F) {
			return value;
		}
		
		final float currentValue = equal(value, -0.0F) ? +0.0F : value;
		
		final int oldCurrentValueToIntBits = Float.floatToIntBits(currentValue);
		final int newCurrentValueToIntBits = currentValue < 0.0F ? oldCurrentValueToIntBits - 1 : oldCurrentValueToIntBits + 1;
		
		return Float.intBitsToFloat(newCurrentValueToIntBits);
	}
	
	/**
	 * Returns the normalized representation of {@code value}.
	 * <p>
	 * If {@code value} is greater than or equal to {@code min(a, b)} and less than or equal to {@code max(a, b)}, the normalized representation of {@code value} will be between {@code 0.0F} (inclusive) and {@code 1.0F} (inclusive).
	 * 
	 * @param value the {@code float} value to normalize
	 * @param a the {@code float} value that represents the minimum or maximum boundary
	 * @param b the {@code float} value that represents the maximum or minimum boundary
	 * @return the normalized representation of {@code value}
	 */
	public static float normalize(final float value, final float a, final float b) {
		final float maximum = max(a, b);
		final float minimum = min(a, b);
		final float valueNormalized = (value - minimum) / (maximum - minimum);
		
		return valueNormalized;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Perlin-based fractal algorithm using the coordinates X, Y and Z
	 */
	public static float perlinFractalXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float result = 0.0F;
		
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Perlin-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
	 */
	public static float perlinFractionalBrownianMotionXYZ(final float x, final float y, final float z, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
		float currentAmplitude = 1.0F;
		float maximumAmplitude = 0.0F;
		
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0F;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0F + (maximum + minimum) / 2.0F;
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by the Perlin algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code float} with noise computed by the Perlin algorithm using the coordinates X, Y and Z
	 */
	public static float perlinNoiseXYZ(final float x, final float y, final float z) {
		final float floorX = floor(x);
		final float floorY = floor(y);
		final float floorZ = floor(z);
		
		final int x0 = (int)(floorX) & 0xFF;
		final int y0 = (int)(floorY) & 0xFF;
		final int z0 = (int)(floorZ) & 0xFF;
		
		final float x1 = x - floorX;
		final float y1 = y - floorY;
		final float z1 = z - floorZ;
		
		final float u = x1 * x1 * x1 * (x1 * (x1 * 6.0F - 15.0F) + 10.0F);
		final float v = y1 * y1 * y1 * (y1 * (y1 * 6.0F - 15.0F) + 10.0F);
		final float w = z1 * z1 * z1 * (z1 * (z1 * 6.0F - 15.0F) + 10.0F);
		
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
		
		final float gradient0U = hash0 < 8 || hash0 == 12 || hash0 == 13 ? x1 : y1;
		final float gradient0V = hash0 < 4 || hash0 == 12 || hash0 == 13 ? y1 : z1;
		final float gradient0 = ((hash0 & 1) == 0 ? gradient0U : -gradient0U) + ((hash0 & 2) == 0 ? gradient0V : -gradient0V);
		final float gradient1U = hash1 < 8 || hash1 == 12 || hash1 == 13 ? x1 - 1.0F : y1;
		final float gradient1V = hash1 < 4 || hash1 == 12 || hash1 == 13 ? y1 : z1;
		final float gradient1 = ((hash1 & 1) == 0 ? gradient1U : -gradient1U) + ((hash1 & 2) == 0 ? gradient1V : -gradient1V);
		final float gradient2U = hash2 < 8 || hash2 == 12 || hash2 == 13 ? x1 : y1 - 1.0F;
		final float gradient2V = hash2 < 4 || hash2 == 12 || hash2 == 13 ? y1 - 1.0F : z1;
		final float gradient2 = ((hash2 & 1) == 0 ? gradient2U : -gradient2U) + ((hash2 & 2) == 0 ? gradient2V : -gradient2V);
		final float gradient3U = hash3 < 8 || hash3 == 12 || hash3 == 13 ? x1 - 1.0F : y1 - 1.0F;
		final float gradient3V = hash3 < 4 || hash3 == 12 || hash3 == 13 ? y1 - 1.0F : z1;
		final float gradient3 = ((hash3 & 1) == 0 ? gradient3U : -gradient3U) + ((hash3 & 2) == 0 ? gradient3V : -gradient3V);
		final float gradient4U = hash4 < 8 || hash4 == 12 || hash4 == 13 ? x1 : y1;
		final float gradient4V = hash4 < 4 || hash4 == 12 || hash4 == 13 ? y1 : z1 - 1.0F;
		final float gradient4 = ((hash4 & 1) == 0 ? gradient4U : -gradient4U) + ((hash4 & 2) == 0 ? gradient4V : -gradient4V);
		final float gradient5U = hash5 < 8 || hash5 == 12 || hash5 == 13 ? x1 - 1.0F : y1;
		final float gradient5V = hash5 < 4 || hash5 == 12 || hash5 == 13 ? y1 : z1 - 1.0F;
		final float gradient5 = ((hash5 & 1) == 0 ? gradient5U : -gradient5U) + ((hash5 & 2) == 0 ? gradient5V : -gradient5V);
		final float gradient6U = hash6 < 8 || hash6 == 12 || hash6 == 13 ? x1 : y1 - 1.0F;
		final float gradient6V = hash6 < 4 || hash6 == 12 || hash6 == 13 ? y1 - 1.0F : z1 - 1.0F;
		final float gradient6 = ((hash6 & 1) == 0 ? gradient6U : -gradient6U) + ((hash6 & 2) == 0 ? gradient6V : -gradient6V);
		final float gradient7U = hash7 < 8 || hash7 == 12 || hash7 == 13 ? x1 - 1.0F : y1 - 1.0F;
		final float gradient7V = hash7 < 4 || hash7 == 12 || hash7 == 13 ? y1 - 1.0F : z1 - 1.0F;
		final float gradient7 = ((hash7 & 1) == 0 ? gradient7U : -gradient7U) + ((hash7 & 2) == 0 ? gradient7V : -gradient7V);
		
		final float lerp0 = gradient0 + u * (gradient1 - gradient0);
		final float lerp1 = gradient2 + u * (gradient3 - gradient2);
		final float lerp2 = gradient4 + u * (gradient5 - gradient4);
		final float lerp3 = gradient6 + u * (gradient7 - gradient6);
		final float lerp4 = lerp0 + v * (lerp1 - lerp0);
		final float lerp5 = lerp2 + v * (lerp3 - lerp2);
		final float lerp6 = lerp4 + w * (lerp5 - lerp4);
		
		return lerp6;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static float perlinTurbulenceXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(perlinNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Perlin-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static float perlinTurbulenceXYZ(final float x, final float y, final float z, final int octaves) {
		float currentX = x;
		float currentY = y;
		float currentZ = z;
		
		float noise = abs(perlinNoiseXYZ(x, y, z));
		
		float weight = 1.0F;
		
		for(int i = 1; i < octaves; i++) {
			weight *= 2.0F;
			
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
	public static float pow(final float base, final float exponent) {
		return toFloat(Math.pow(base, exponent));
	}
	
	/**
	 * Returns a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive).
	 * 
	 * @return a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive)
	 */
	public static float random() {
//		return ThreadLocalRandom.current().nextFloat();
		return RANDOM.nextFloat();
	}
	
	/**
	 * Returns the remainder of {@code x} and {@code y}.
	 * 
	 * @param x the left hand side of the remainder operation
	 * @param y the right hand side of the remainder operation
	 * @return the remainder of {@code x} and {@code y}
	 */
	public static float remainder(final float x, final float y) {
		return x - (int)(x / y) * y;
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
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Floats.saturate(value, 0.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param value the value to saturate (or clamp)
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	public static float saturate(final float value) {
		return saturate(value, 0.0F, 1.0F);
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
	public static float saturate(final float value, final float edgeA, final float edgeB) {
		final float minimum = min(edgeA, edgeB);
		final float maximum = max(edgeA, edgeB);
		
		if(value < minimum) {
			return minimum;
		} else if(value > maximum) {
			return maximum;
		} else {
			return value;
		}
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinate X
	 */
	public static float simplexFractalX(final float x, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float result = 0.0F;
		
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseX(x * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X and Y
	 */
	public static float simplexFractalXY(final float x, final float y, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float result = 0.0F;
		
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXY(x * currentFrequency, y * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start at
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y and Z
	 */
	public static float simplexFractalXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float result = 0.0F;
		
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W.
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
	 * @return a {@code float} with noise computed by a Simplex-based fractal algorithm using the coordinates X, Y, Z and W
	 */
	public static float simplexFractalXYZW(final float x, final float y, final float z, final float w, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float result = 0.0F;
		
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		for(int i = 0; i < octaves; i++) {
			result += currentAmplitude * simplexNoiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency);
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return result;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinate X
	 */
	public static float simplexFractionalBrownianMotionX(final float x, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
		float currentAmplitude = 1.0F;
		float maximumAmplitude = 0.0F;
		
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseX(x * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0F;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0F + (maximum + minimum) / 2.0F;
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X and Y
	 */
	public static float simplexFractionalBrownianMotionXY(final float x, final float y, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
		float currentAmplitude = 1.0F;
		float maximumAmplitude = 0.0F;
		
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXY(x * currentFrequency, y * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0F;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0F + (maximum + minimum) / 2.0F;
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param minimum the minimum value to return
	 * @param maximum the maximum value to return
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y and Z
	 */
	public static float simplexFractionalBrownianMotionXYZ(final float x, final float y, final float z, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
		float currentAmplitude = 1.0F;
		float maximumAmplitude = 0.0F;
		
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0F;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0F + (maximum + minimum) / 2.0F;
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W.
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
	 * @return a {@code float} with noise computed by a Simplex-based fractional Brownian motion (fBm) algorithm using the coordinates X, Y, Z and W
	 */
	public static float simplexFractionalBrownianMotionXYZW(final float x, final float y, final float z, final float w, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
		float currentAmplitude = 1.0F;
		float maximumAmplitude = 0.0F;
		
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += simplexNoiseXYZW(x * currentFrequency, y * currentFrequency, z * currentFrequency, w * currentFrequency) * currentAmplitude;
			
			maximumAmplitude += currentAmplitude;
			currentAmplitude *= gain;
			
			currentFrequency *= 2.0F;
		}
		
		noise /= maximumAmplitude;
		noise = noise * (maximum - minimum) / 2.0F + (maximum + minimum) / 2.0F;
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by the Simplex algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @return a {@code float} with noise computed by the Simplex algorithm using the coordinate X
	 */
	public static float simplexNoiseX(final float x) {
		final int i0 = doFastFloorToInt(x);
		final int i1 = i0 + 1;
		
		final float x0 = x - i0;
		final float x1 = x0 - 1.0F;
		
		final float t00 = 1.0F - x0 * x0;
		final float t01 = t00 * t00;
		
		final float t10 = 1.0F - x1 * x1;
		final float t11 = t10 * t10;
		
		final int hash00 = PERMUTATIONS_B[Ints.abs(i0) % PERMUTATIONS_B.length];
		final int hash01 = hash00 & 0x0F;
		final int hash10 = PERMUTATIONS_B[Ints.abs(i1) % PERMUTATIONS_B.length];
		final int hash11 = hash10 & 0x0F;
		
		final float gradient00 = 1.0F + (hash01 & 7);
		final float gradient01 = (hash01 & 8) != 0 ? -gradient00 : gradient00;
		final float gradient02 = gradient01 * x0;
		final float gradient10 = 1.0F + (hash11 & 7);
		final float gradient11 = (hash11 & 8) != 0 ? -gradient10 : gradient10;
		final float gradient12 = gradient11 * x1;
		
		final float n0 = t01 * t01 * gradient02;
		final float n1 = t11 * t11 * gradient12;
		
		return 0.395F * (n0 + n1);
	}
	
	/**
	 * Returns a {@code float} with noise computed by the Simplex algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return a {@code float} with noise computed by the Simplex algorithm using the coordinates X and Y
	 */
	public static float simplexNoiseXY(final float x, final float y) {
		final float s = (x + y) * SIMPLEX_F2;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		
		final float t = (i + j) * SIMPLEX_G2;
		
		final float x0 = x - (i - t);
		final float y0 = y - (j - t);
		
		final int i1 = x0 > y0 ? 1 : 0;
		final int j1 = x0 > y0 ? 0 : 1;
		
		final float x1 = x0 - i1 + SIMPLEX_G2;
		final float y1 = y0 - j1 + SIMPLEX_G2;
		final float x2 = x0 - 1.0F + 2.0F * SIMPLEX_G2;
		final float y2 = y0 - 1.0F + 2.0F * SIMPLEX_G2;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1]];
		
		final float t0 = 0.5F - x0 * x0 - y0 * y0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0);
		
		final float t1 = 0.5F - x1 * x1 - y1 * y1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1);
		
		final float t2 = 0.5F - x2 * x2 - y2 * y2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2);
		
		return 70.0F * (n0 + n1 + n2);
	}
	
	/**
	 * Returns a {@code float} with noise computed by the Simplex algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @return a {@code float} with noise computed by the Simplex algorithm using the coordinates X, Y and Z
	 */
	public static float simplexNoiseXYZ(final float x, final float y, final float z) {
		final float s = (x + y + z) * SIMPLEX_F3;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		
		final float t = (i + j + k) * SIMPLEX_G3;
		
		final float x0 = x - (i - t);
		final float y0 = y - (j - t);
		final float z0 = z - (k - t);
		
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
		
		final float x1 = x0 - i1 + SIMPLEX_G3;
		final float y1 = y0 - j1 + SIMPLEX_G3;
		final float z1 = z0 - k1 + SIMPLEX_G3;
		final float x2 = x0 - i2 + 2.0F * SIMPLEX_G3;
		final float y2 = y0 - j2 + 2.0F * SIMPLEX_G3;
		final float z2 = z0 - k2 + 2.0F * SIMPLEX_G3;
		final float x3 = x0 - 1.0F + 3.0F * SIMPLEX_G3;
		final float y3 = y0 - 1.0F + 3.0F * SIMPLEX_G3;
		final float z3 = z0 - 1.0F + 3.0F * SIMPLEX_G3;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		
		final int gi0 = PERMUTATIONS_B_MODULO_12[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0]]];
		final int gi1 = PERMUTATIONS_B_MODULO_12[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1]]];
		final int gi2 = PERMUTATIONS_B_MODULO_12[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2]]];
		final int gi3 = PERMUTATIONS_B_MODULO_12[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1]]];
		
		final float t0 = 0.6F - x0 * x0 - y0 * y0 - z0 * z0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_3[gi0 * 3 + 0] * x0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 1] * y0 + SIMPLEX_GRADIENT_3[gi0 * 3 + 2] * z0);
		
		final float t1 = 0.6F - x1 * x1 - y1 * y1 - z1 * z1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_3[gi1 * 3 + 0] * x1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 1] * y1 + SIMPLEX_GRADIENT_3[gi1 * 3 + 2] * z1);
		
		final float t2 = 0.6F - x2 * x2 - y2 * y2 - z2 * z2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_3[gi2 * 3 + 0] * x2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 1] * y2 + SIMPLEX_GRADIENT_3[gi2 * 3 + 2] * z2);
		
		final float t3 = 0.6F - x3 * x3 - y3 * y3 - z3 * z3;
		final float n3 = t3 < 0.0F ? 0.0F : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_3[gi3 * 3 + 0] * x3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 1] * y3 + SIMPLEX_GRADIENT_3[gi3 * 3 + 2] * z3);
		
		return 32.0F * (n0 + n1 + n2 + n3);
	}
	
	/**
	 * Returns a {@code float} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param w the W-coordinate
	 * @return a {@code float} with noise computed by the Simplex algorithm using the coordinates X, Y, Z and W
	 */
	public static float simplexNoiseXYZW(final float x, final float y, final float z, final float w) {
		final float s = (x + y + z + w) * SIMPLEX_F4;
		
		final int i = doFastFloorToInt(x + s);
		final int j = doFastFloorToInt(y + s);
		final int k = doFastFloorToInt(z + s);
		final int l = doFastFloorToInt(w + s);
		
		final float t = (i + j + k + l) * SIMPLEX_G4;
		
		final float x0 = x - (i - t);
		final float y0 = y - (j - t);
		final float z0 = z - (k - t);
		final float w0 = w - (l - t);
		
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
		
		final float x1 = x0 - i1 + SIMPLEX_G4;
		final float y1 = y0 - j1 + SIMPLEX_G4;
		final float z1 = z0 - k1 + SIMPLEX_G4;
		final float w1 = w0 - l1 + SIMPLEX_G4;
		final float x2 = x0 - i2 + 2.0F * SIMPLEX_G4;
		final float y2 = y0 - j2 + 2.0F * SIMPLEX_G4;
		final float z2 = z0 - k2 + 2.0F * SIMPLEX_G4;
		final float w2 = w0 - l2 + 2.0F * SIMPLEX_G4;
		final float x3 = x0 - i3 + 3.0F * SIMPLEX_G4;
		final float y3 = y0 - j3 + 3.0F * SIMPLEX_G4;
		final float z3 = z0 - k3 + 3.0F * SIMPLEX_G4;
		final float w3 = w0 - l3 + 3.0F * SIMPLEX_G4;
		final float x4 = x0 - 1.0F + 4.0F * SIMPLEX_G4;
		final float y4 = y0 - 1.0F + 4.0F * SIMPLEX_G4;
		final float z4 = z0 - 1.0F + 4.0F * SIMPLEX_G4;
		final float w4 = w0 - 1.0F + 4.0F * SIMPLEX_G4;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		final int ll = l & 0xFF;
		
		final int gi0 = PERMUTATIONS_B[ii +  0 + PERMUTATIONS_B[jj +  0 + PERMUTATIONS_B[kk +  0 + PERMUTATIONS_B[ll +  0]]]] % 32;
		final int gi1 = PERMUTATIONS_B[ii + i1 + PERMUTATIONS_B[jj + j1 + PERMUTATIONS_B[kk + k1 + PERMUTATIONS_B[ll + l1]]]] % 32;
		final int gi2 = PERMUTATIONS_B[ii + i2 + PERMUTATIONS_B[jj + j2 + PERMUTATIONS_B[kk + k2 + PERMUTATIONS_B[ll + l2]]]] % 32;
		final int gi3 = PERMUTATIONS_B[ii + i3 + PERMUTATIONS_B[jj + j3 + PERMUTATIONS_B[kk + k3 + PERMUTATIONS_B[ll + l3]]]] % 32;
		final int gi4 = PERMUTATIONS_B[ii +  1 + PERMUTATIONS_B[jj +  1 + PERMUTATIONS_B[kk +  1 + PERMUTATIONS_B[ll +  1]]]] % 32;
		
		final float t0 = 0.6F - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (SIMPLEX_GRADIENT_4[gi0 * 4 + 0] * x0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 1] * y0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 2] * z0 + SIMPLEX_GRADIENT_4[gi0 * 4 + 3] * w0);
		
		final float t1 = 0.6F - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (SIMPLEX_GRADIENT_4[gi1 * 4 + 0] * x1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 1] * y1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 2] * z1 + SIMPLEX_GRADIENT_4[gi1 * 4 + 3] * w1);
		
		final float t2 = 0.6F - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (SIMPLEX_GRADIENT_4[gi2 * 4 + 0] * x2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 1] * y2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 2] * z2 + SIMPLEX_GRADIENT_4[gi2 * 4 + 3] * w2);
		
		final float t3 = 0.6F - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		final float n3 = t3 < 0.0F ? 0.0F : (t3 * t3) * (t3 * t3) * (SIMPLEX_GRADIENT_4[gi3 * 4 + 0] * x3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 1] * y3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 2] * z3 + SIMPLEX_GRADIENT_4[gi3 * 4 + 3] * w3);
		
		final float t4 = 0.6F - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		final float n4 = t4 < 0.0F ? 0.0F : (t4 * t4) * (t4 * t4) * (SIMPLEX_GRADIENT_4[gi4 * 4 + 0] * x4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 1] * y4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 2] * z4 + SIMPLEX_GRADIENT_4[gi4 * 4 + 3] * w4);
		
		return 27.0F * (n0 + n1 + n2 + n3 + n4);
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinate X.
	 * 
	 * @param x the X-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinate X
	 */
	public static float simplexTurbulenceX(final float x, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseX(x * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X and Y
	 */
	public static float simplexTurbulenceXY(final float x, final float y, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseXY(x * currentFrequency, y * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @param z the Z-coordinate
	 * @param amplitude the amplitude to start a
	 * @param frequency the frequency to start at
	 * @param gain the amplitude multiplier
	 * @param lacunarity the frequency multiplier
	 * @param octaves the number of iterations to perform
	 * @return a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y and Z
	 */
	public static float simplexTurbulenceXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
		for(int i = 0; i < octaves; i++) {
			noise += currentAmplitude * abs(simplexNoiseXYZ(x * currentFrequency, y * currentFrequency, z * currentFrequency));
			
			currentAmplitude *= gain;
			currentFrequency *= lacunarity;
		}
		
		return noise;
	}
	
	/**
	 * Returns a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W.
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
	 * @return a {@code float} with noise computed by a Simplex-based turbulence algorithm using the coordinates X, Y, Z and W
	 */
	public static float simplexTurbulenceXYZW(final float x, final float y, final float z, final float w, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
		float currentAmplitude = amplitude;
		float currentFrequency = frequency;
		
		float noise = 0.0F;
		
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
	public static float sin(final float angleRadians) {
		return toFloat(Math.sin(angleRadians));
	}
	
	/**
	 * Returns the hyperbolic sine of a {@code value}.
	 * 
	 * @param value a {@code float} value
	 * @return the hyperbolic sine of a {@code value}
	 */
	public static float sinh(final float value) {
		return toFloat(Math.sinh(value));
	}
	
	/**
	 * Performs a smoothstep operation on {@code value} and the edges {@code edgeA} and {@code edgeB}.
	 * <p>
	 * Returns a {@code float} value.
	 * 
	 * @param value a {@code float} value
	 * @param edgeA one of the edges
	 * @param edgeB one of the edges
	 * @return a {@code float} value
	 */
	public static float smoothstep(final float value, final float edgeA, final float edgeB) {
		final float minimumValue = min(edgeA, edgeB);
		final float maximumValue = max(edgeA, edgeB);
		
		final float x = saturate((value - minimumValue) / (maximumValue - minimumValue), 0.0F, 1.0F);
		
		return x * x * (3.0F - 2.0F * x);
	}
	
	/**
	 * Solves the cubic system for the quartic system based on the values {@code p}, {@code q} and {@code r}.
	 * <p>
	 * Returns a {@code float} with the result of the operation.
	 * 
	 * @param p a value
	 * @param q a value
	 * @param r a value
	 * @return a {@code float} with the result of the operation
	 */
	public static float solveCubicForQuartic(final float p, final float q, final float r) {
		final float pSquared = p * p;
		final float q0 = (pSquared - 3.0F * q) / 9.0F;
		final float q0Cubed = q0 * q0 * q0;
		final float r0 = (p * (pSquared - 4.5F * q) + 13.5F * r) / 27.0F;
		final float r0Squared = r0 * r0;
		final float d = q0Cubed - r0Squared;
		final float e = p / 3.0F;
		
		if(d >= 0.0F) {
			final float d0 = r0 / sqrt(q0Cubed);
			final float theta = acos(d0) / 3.0F;
			final float q1 = -2.0F * sqrt(q0);
			final float q2 = q1 * cos(theta) - e;
			
			return q2;
		}
		
		final float q1 = pow(sqrt(r0Squared - q0Cubed) + abs(r0), 1.0F / 3.0F);
		final float q2 = r0 < 0.0F ? (q1 + q0 / q1) - e : -(q1 + q0 / q1) - e;
		
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
	 * Otherwise, the result is the {@code float} value closest to the true mathematical square root of the argument value.
	 * 
	 * @param value a value
	 * @return the correctly rounded positive square root of {@code value}
	 * @see Math#sqrt(double)
	 */
	public static float sqrt(final float value) {
		return toFloat(Math.sqrt(value));
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
	public static float tan(final float angleRadians) {
		return toFloat(Math.tan(angleRadians));
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
	public static float toDegrees(final float angleRadians) {
		return toFloat(Math.toDegrees(angleRadians));
	}
	
	/**
	 * Returns a {@code float} representation of a {@code double} value.
	 * 
	 * @param value a {@code double} value
	 * @return a {@code float} representation of a {@code double} value
	 */
	public static float toFloat(final double value) {
		return (float)(value);
	}
	
	/**
	 * Returns a {@code float} representation of an {@code int} value.
	 * 
	 * @param value an {@code int} value
	 * @return a {@code float} representation of an {@code int} value
	 */
	public static float toFloat(final int value) {
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
	public static float toRadians(final float angleDegrees) {
		return toFloat(Math.toRadians(angleDegrees));
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
	public static float wrapAround(final float value, final float a, final float b) {
		final float minimumValue = min(a, b);
		final float maximumValue = max(a, b);
		
		float currentValue = value;
		
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
	 * Returns a {@code float[]}, with a length of {@code 2}, that contains the result.
	 * <p>
	 * If the quadratic system could not be solved, the result will contain the values {@code Float.NaN}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return a {@code float[]}, with a length of {@code 2}, that contains the result
	 */
	public static float[] solveQuadraticSystem(final float a, final float b, final float c) {
		final float[] result = new float[] {Float.NaN, Float.NaN};
		
		final float discriminantSquared = b * b - 4.0F * a * c;
		
		if(equal(discriminantSquared, 0.0F)) {
			final float q = -0.5F * b / a;
			
			final float result0 = q;
			final float result1 = q;
			
			result[0] = result0;
			result[1] = result1;
		} else if(discriminantSquared > 0.0F) {
			final float discriminant = sqrt(discriminantSquared);
			
			final float q = -0.5F * (b > 0.0F ? b + discriminant : b - discriminant);
			
			final float result0 = q / a;
			final float result1 = c / q;
			
			result[0] = min(result0, result1);
			result[1] = max(result0, result1);
		}
		
		return result;
	}
	
	/**
	 * Attempts to solve the quartic system based on the values {@code a}, {@code b}, {@code c}, {@code d} and {@code e}.
	 * <p>
	 * Returns a {@code float[]}, with a length of {@code 0}, {@code 2} or {@code 4}, that contains the result.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @param e a value
	 * @return a {@code float[]}, with a length of {@code 0}, {@code 2} or {@code 4}, that contains the result
	 */
	public static float[] solveQuartic(final float a, final float b, final float c, final float d, final float e) {
		final float aReciprocal = 1.0F / a;
		final float bA = b * aReciprocal;
		final float bASquared = bA * bA;
		final float cA = c * aReciprocal;
		final float dA = d * aReciprocal;
		final float eA = e * aReciprocal;
		final float p = -0.375F * bASquared + cA;
		final float q = 0.125F * bASquared * bA - 0.5F * bA * cA + dA;
		final float r = -0.01171875F * bASquared * bASquared + 0.0625F * bASquared * cA - 0.25F * bA * dA + eA;
		final float z = solveCubicForQuartic(-0.5F * p, -r, 0.5F * r * p - 0.125F * q * q);
		
		float d1 = 2.0F * z - p;
		float d2;
		
		if(d1 < 0.0F) {
			return new float[0];
		} else if(d1 < 1.0e-10F) {
			d2 = z * z - r;
			
			if(d2 < 0.0F) {
				return new float[0];
			}
			
			d2 = sqrt(d2);
		} else {
			d1 = sqrt(d1);
			d2 = 0.5F * q / d1;
		}
		
		final float q1 = d1 * d1;
		final float q2 = -0.25F * bA;
		final float pm = q1 - 4.0F * (z - d2);
		final float pp = q1 - 4.0F * (z + d2);
		
		if(pm >= 0.0F && pp >= 0.0F) {
			final float pmSqrt = sqrt(pm);
			final float ppSqrt = sqrt(pp);
			
			final float[] results = new float[4];
			
			results[0] = -0.5F * (d1 + pmSqrt) + q2;
			results[1] = -0.5F * (d1 - pmSqrt) + q2;
			results[2] = +0.5F * (d1 + ppSqrt) + q2;
			results[3] = +0.5F * (d1 - ppSqrt) + q2;
			
			for(int i = 1; i < 4; i++) {
				for(int j = i; j > 0 && results[j - 1] > results[j]; j--) {
					final float resultJ0 = results[j - 0];
					final float resultJ1 = results[j - 1];
					
					results[j - 0] = resultJ1;
					results[j - 1] = resultJ0;
				}
			}
			
			return results;
		} else if(pm >= 0.0F) {
			final float pmSqrt = sqrt(pm);
			
			return new float[] {
				-0.5F * (d1 + pmSqrt) + q2,
				-0.5F * (d1 - pmSqrt) + q2
			};
		} else if(pp >= 0.0F) {
			final float ppSqrt = sqrt(pp);
			
			return new float[] {
				+0.5F * (d1 - ppSqrt) + q2,
				+0.5F * (d1 + ppSqrt) + q2
			};
		} else {
			return new float[0];
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float[] doCreateSimplexGradient3() {
		return new float[] {
			+1.0F, +1.0F, +0.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F, +0.0F,
			+1.0F, +0.0F, +1.0F, -1.0F, +0.0F, +1.0F, +1.0F, +0.0F, -1.0F, -1.0F, +0.0F, -1.0F,
			+0.0F, +1.0F, +1.0F, +0.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F
		};
	}
	
	private static float[] doCreateSimplexGradient4() {
		return new float[] {
			+0.0F, +1.0F, +1.0F, +1.0F, +0.0F, +1.0F, +1.0F, -1.0F, +0.0F, +1.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, -1.0F,
			+0.0F, -1.0F, +1.0F, +1.0F, +0.0F, -1.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F, +1.0F, +0.0F, -1.0F, -1.0F, -1.0F,
			+1.0F, +0.0F, +1.0F, +1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +1.0F, +0.0F, -1.0F, +1.0F, +1.0F, +0.0F, -1.0F, -1.0F,
			-1.0F, +0.0F, +1.0F, +1.0F, -1.0F, +0.0F, +1.0F, -1.0F, -1.0F, +0.0F, -1.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F,
			+1.0F, +1.0F, +0.0F, +1.0F, +1.0F, +1.0F, +0.0F, -1.0F, +1.0F, -1.0F, +0.0F, +1.0F, +1.0F, -1.0F, +0.0F, -1.0F,
			-1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +1.0F, +0.0F, -1.0F, -1.0F, -1.0F, +0.0F, +1.0F, -1.0F, -1.0F, +0.0F, -1.0F,
			+1.0F, +1.0F, +1.0F, +0.0F, +1.0F, +1.0F, -1.0F, +0.0F, +1.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, -1.0F, +0.0F,
			-1.0F, +1.0F, +1.0F, +0.0F, -1.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F, +1.0F, +0.0F, -1.0F, -1.0F, -1.0F, +0.0F
		};
	}
	
	private static int doFastFloorToInt(final float value) {
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