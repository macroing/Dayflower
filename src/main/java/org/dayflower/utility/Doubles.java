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
public final class Doubles {
	private Doubles() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		final boolean isNaNA = Double.isNaN(a);
		final boolean isNaNB = Double.isNaN(b);
		
		if(!isNaNA && !isNaNB) {
			return Math.min(a, b);
		} else if(!isNaNA) {
			return a;
		} else if(!isNaNB) {
			return b;
		} else {
			return Double.NaN;
		}
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
//	TODO: Add Unit Tests!
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
		final double z = doSolveCubicForQuartic(-0.5D * p, -r, 0.5D * r * p - 0.125D * q * q);
		
		double d1 = 2.0D * z - p;
		double d2;
		
		if(d1 < 0.0D) {
			return new double[0];
		} else if(d1 < 1.0e-10D) {
			d2 = z * z - r;
			
			if(d2 < 0.0D) {
				return new double[0];
			}
			
			d2 = Math.sqrt(d2);
		} else {
			d1 = Math.sqrt(d1);
			d2 = 0.5D * q / d1;
		}
		
		final double q1 = d1 * d1;
		final double q2 = -0.25D * bA;
		final double pm = q1 - 4.0D * (z - d2);
		final double pp = q1 - 4.0D * (z + d2);
		
		if(pm >= 0.0D && pp >= 0.0D) {
			final double pmSqrt = Math.sqrt(pm);
			final double ppSqrt = Math.sqrt(pp);
			
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
			final double pmSqrt = Math.sqrt(pm);
			
			return new double[] {
				-0.5D * (d1 + pmSqrt) + q2,
				-0.5D * (d1 - pmSqrt) + q2
			};
		} else if(pp >= 0.0D) {
			final double ppSqrt = Math.sqrt(pp);
			
			return new double[] {
				+0.5D * (d1 - ppSqrt) + q2,
				+0.5D * (d1 + ppSqrt) + q2
			};
		} else {
			return new double[0];
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doSolveCubicForQuartic(final double p, final double q, final double r) {
		final double pSquared = p * p;
		final double q0 = (pSquared - 3.0D * q) / 9.0D;
		final double q0Cubed = q0 * q0 * q0;
		final double r0 = (p * (pSquared - 4.5D * q) + 13.5D * r) / 27.0D;
		final double r0Squared = r0 * r0;
		final double d = q0Cubed - r0Squared;
		final double e = p / 3.0D;
		
		if(d >= 0.0D) {
			final double d0 = r0 / Math.sqrt(q0Cubed);
			final double theta = Math.acos(d0) / 3.0D;
			final double q1 = -2.0D * Math.sqrt(q0);
			final double q2 = q1 * Math.cos(theta) - e;
			
			return q2;
		}
		
		final double q1 = Math.pow(Math.sqrt(r0Squared - q0Cubed) + Math.abs(r0), 1.0D / 3.0D);
		final double q2 = r0 < 0.0D ? (q1 + q0 / q1) - e : -(q1 + q0 / q1) - e;
		
		return q2;
	}
}