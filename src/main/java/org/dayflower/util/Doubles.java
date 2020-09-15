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
	private Doubles() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		final double p = -0.375D * bASquared * cA;
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