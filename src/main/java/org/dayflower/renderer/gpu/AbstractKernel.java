/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.renderer.gpu;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

import com.aparapi.Kernel;

/**
 * An {@code AbstractKernel} is an abstract implementation of {@code Kernel} that adds functionality.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractKernel extends Kernel {
	private static final float PRNG_NEXT_FLOAT_RECIPROCAL = 1.0F / (1 << 24);
	private static final float SIMPLEX_F2 = 0.3660254037844386F;
	private static final float SIMPLEX_F3 = 1.0F / 3.0F;
	private static final float SIMPLEX_F4 = 0.30901699437494745F;
	private static final float SIMPLEX_G2 = 0.21132486540518713F;
	private static final float SIMPLEX_G2_2 = SIMPLEX_G2 * 2.0F;
	private static final float SIMPLEX_G3 = 1.0F / 6.0F;
	private static final float SIMPLEX_G3_2 = SIMPLEX_G3 * 2.0F;
	private static final float SIMPLEX_G3_3 = SIMPLEX_G3 * 3.0F;
	private static final float SIMPLEX_G4 = 0.1381966011250105F;
	private static final float SIMPLEX_G4_2 = SIMPLEX_G4 * 2.0F;
	private static final float SIMPLEX_G4_3 = SIMPLEX_G4 * 3.0F;
	private static final float SIMPLEX_G4_4 = SIMPLEX_G4 * 4.0F;
	private static final long PRNG_ADDEND = 0xBL;
	private static final long PRNG_MASK = (1L << 48L) - 1L;
	private static final long PRNG_MULTIPLIER = 0x5DEECE66DL;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code float[]} that contains an error {@code float} value.
	 */
	protected float[] eFloatArray_$private$9;
	
	/**
	 * A {@code float[]} that contains the solutions to the quadratic system.
	 */
	protected float[] quadraticSystemArray_$private$2;
	
	/**
	 * A {@code float[]} that contains a gradient for Simplex noise.
	 */
	protected float[] simplexGradient3Array;
	
	/**
	 * A {@code float[]} that contains a gradient for Simplex noise.
	 */
	protected float[] simplexGradient4Array;
	
	/**
	 * The resolution of this {@code AbstractKernel} instance.
	 * <p>
	 * The resolution is the same as {@code resolutionX * resolutionY}.
	 */
	protected int resolution;
	
	/**
	 * The resolution for the X-axis of this {@code AbstractKernel} instance.
	 */
	protected int resolutionX;
	
	/**
	 * The resolution for the Y-axis of this {@code AbstractKernel} instance.
	 */
	protected int resolutionY;
	
	/**
	 * An {@code int[]} that contains permutations for Perlin and Simplex noise.
	 */
	protected int[] permutationsBArray;
	
	/**
	 * An {@code int[]} that contains permutations for Perlin and Simplex noise.
	 */
	protected int[] permutationsBModulo12Array;
	
	/**
	 * A {@code long[]} that contains seed values for the pseudorandom number generator (PRNG).
	 */
	protected long[] seedArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractKernel} instance.
	 */
	protected AbstractKernel() {
		this.eFloatArray_$private$9 = new float[9];
		this.quadraticSystemArray_$private$2 = new float[2];
		this.simplexGradient3Array = new float[0];
		this.simplexGradient4Array = new float[0];
		this.resolutionX = 0;
		this.resolutionY = 0;
		this.permutationsBArray = new int[0];
		this.permutationsBModulo12Array = new int[0];
		this.seedArray = new long[0];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@code byte[]} {@code array} after a call to {@code get(array)}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the {@code byte[]} to get and return
	 * @return the {@code byte[]} {@code array} after a call to {@code get(array)}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public final byte[] getAndReturn(final byte[] array) {
		Objects.requireNonNull(array, "array == null");
		
		get(array);
		
		return array;
	}
	
	/**
	 * Returns the {@code float[]} {@code array} after a call to {@code get(array)}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the {@code float[]} to get and return
	 * @return the {@code float[]} {@code array} after a call to {@code get(array)}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public final float[] getAndReturn(final float[] array) {
		Objects.requireNonNull(array, "array == null");
		
		get(array);
		
		return array;
	}
	
	/**
	 * Disposes of any resources created by this {@code AbstractKernel} instance.
	 */
	@Override
	public final synchronized void dispose() {
		super.dispose();
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractKernel} instance.
	 * <p>
	 * If overriding this method, make sure to call this method using {@code super.setup();}.
	 */
	public void setup() {
		setExplicit(true);
		
		put(this.simplexGradient3Array = doCreateSimplexGradient3Array());
		put(this.simplexGradient4Array = doCreateSimplexGradient4Array());
		put(this.permutationsBArray = doCreatePermutationsBArray());
		put(this.permutationsBModulo12Array = doCreatePermutationsBModulo12Array());
		put(this.seedArray = Arrays.generateLongArray(getResolution(), index -> ThreadLocalRandom.current().nextLong()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is finite, {@code false} otherwise.
	 * 
	 * @param value the {@code float} value to check
	 * @return {@code true} if, and only if, {@code value} is finite, {@code false} otherwise
	 */
	protected final boolean checkIsFinite(final float value) {
		return !checkIsInfinite(value) && !checkIsNaN(value);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is infinite, {@code false} otherwise.
	 * 
	 * @param value the {@code float} value to check
	 * @return {@code true} if, and only if, {@code value} is infinite, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final boolean checkIsInfinite(final float value) {
		return value == Float.NEGATIVE_INFINITY || value == Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code value} is NaN (Not a Number), {@code false} otherwise.
	 * 
	 * @param value the {@code float} value to check
	 * @return {@code true} if, and only if, {@code value} is NaN (Not a Number), {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final boolean checkIsNaN(final float value) {
		return value == Float.NaN;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code a} and {@code b} are nearly equal, {@code false} otherwise.
	 * 
	 * @param a a {@code float} value
	 * @param b a {@code float} value
	 * @return {@code true} if, and only if, {@code a} and {@code b} are nearly equal, {@code false} otherwise
	 */
	protected final boolean checkIsNearlyEqual(final float a, final float b) {
		return a == b || abs(a - b) < max(Float.MIN_VALUE, 128.0F * 0.00000006F * min(abs(a) + abs(b), Float.MAX_VALUE));
	}
	
//	TODO: Add Javadocs!
	protected final boolean eFloatSetQuadratic(final float aValue, final float aLowerBound, final float aUpperBound, final float bValue, final float bLowerBound, final float bUpperBound, final float cValue, final float cLowerBound, final float cUpperBound) {
		final double discrim = (double)(bValue) * (double)(bValue) - 4.0D * aValue * cValue;
		
		if(discrim < 0.0D) {
			return false;
		}
		
		final double rootDiscrim = sqrt(discrim);
		
		eFloatSetValueError(0, (float)(rootDiscrim), 5.9604645E-8F * (float)(rootDiscrim));
		eFloatSetValue(1, -0.5F);
		
		if(bValue < 0.0F) {
			eFloatSetSubtract(2, bValue, bLowerBound, bUpperBound, this.eFloatArray_$private$9[0], this.eFloatArray_$private$9[1], this.eFloatArray_$private$9[2]);
			eFloatSetMultiply(2, this.eFloatArray_$private$9[6], this.eFloatArray_$private$9[7], this.eFloatArray_$private$9[8], this.eFloatArray_$private$9[3], this.eFloatArray_$private$9[4], this.eFloatArray_$private$9[5]);
		} else {
			eFloatSetAdd(2, bValue, bLowerBound, bUpperBound, this.eFloatArray_$private$9[0], this.eFloatArray_$private$9[1], this.eFloatArray_$private$9[2]);
			eFloatSetMultiply(2, this.eFloatArray_$private$9[6], this.eFloatArray_$private$9[7], this.eFloatArray_$private$9[8], this.eFloatArray_$private$9[3], this.eFloatArray_$private$9[4], this.eFloatArray_$private$9[5]);
		}
		
		final float qValue = this.eFloatArray_$private$9[6];
		final float qLowerBound = this.eFloatArray_$private$9[7];
		final float qUpperBound = this.eFloatArray_$private$9[8];
		
		eFloatSetDivide(2, qValue, qLowerBound, qUpperBound, aValue, aLowerBound, aUpperBound);
		
		final float t0Value = this.eFloatArray_$private$9[6];
		final float t0LowerBound = this.eFloatArray_$private$9[7];
		final float t0UpperBound = this.eFloatArray_$private$9[8];
		
		eFloatSetDivide(2, cValue, cLowerBound, cUpperBound, qValue, qLowerBound, qUpperBound);
		
		final float t1Value = this.eFloatArray_$private$9[6];
		final float t1LowerBound = this.eFloatArray_$private$9[7];
		final float t1UpperBound = this.eFloatArray_$private$9[8];
		
		if(t0Value > t1Value) {
			this.eFloatArray_$private$9[0] = t1Value;
			this.eFloatArray_$private$9[1] = t1LowerBound;
			this.eFloatArray_$private$9[2] = t1UpperBound;
			this.eFloatArray_$private$9[3] = t0Value;
			this.eFloatArray_$private$9[4] = t0LowerBound;
			this.eFloatArray_$private$9[5] = t0UpperBound;
		} else {
			this.eFloatArray_$private$9[0] = t0Value;
			this.eFloatArray_$private$9[1] = t0LowerBound;
			this.eFloatArray_$private$9[2] = t0UpperBound;
			this.eFloatArray_$private$9[3] = t1Value;
			this.eFloatArray_$private$9[4] = t1LowerBound;
			this.eFloatArray_$private$9[5] = t1UpperBound;
		}
		
		return true;
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
	protected final double solveCubicForQuarticSystemD(final double p, final double q, final double r) {
		final double pSquared = p * p;
		final double q0 = (pSquared - 3.0D * q) / 9.0D;
		final double q0Cubed = q0 * q0 * q0;
		final double r0 = (p * (pSquared - 4.5D * q) + 13.5D * r) / 27.0D;
		final double r0Squared = r0 * r0;
		final double d = q0Cubed - r0Squared;
		final double e = p / 3.0D;
		final double q1 = d >= 0.0D ? -2.0D * sqrt(q0) : pow(sqrt(r0Squared - q0Cubed) + abs(r0), 1.0D / 3.0D);
		final double q2 = d >= 0.0D ? q1 * cos(acos(r0 / sqrt(q0Cubed)) / 3.0D) - e : r0 < 0.0D ? (q1 + q0 / q1) - e : -(q1 + q0 / q1) - e;
		
		return q2;
	}
	
	/**
	 * Returns {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise.
	 * 
	 * @param value the value to check
	 * @param threshold the threshold to use
	 * @param valueAdd the value that might be added to {@code value}
	 * @return {@code value} if, and only if, {@code value >= threshold}, {@code value + valueAdd} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final float addIfLessThanThreshold(final float value, final float threshold, final float valueAdd) {
		return value < threshold ? value + valueAdd : value;
	}
	
	/**
	 * Returns {@code value} if, and only if, it is finite, {@code 0.0F} otherwise.
	 * 
	 * @param value the value to check and maybe return
	 * @return {@code value} if, and only if, it is finite, {@code 0.0F} otherwise
	 */
	protected final float finiteOrZero(final float value) {
		return checkIsFinite(value) ? value : 0.0F;
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
	protected final float fractionalPart(final float value, final boolean isUsingCeilOnNegativeValue) {
		return value < 0.0F && isUsingCeilOnNegativeValue ? ceil(value) - value : value - floor(value);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns the result of the linear interpolation operation.
	 * 
	 * @param value1 a {@code float} value
	 * @param value2 a {@code float} value
	 * @param t the factor
	 * @return the result of the linear interpolation operation
	 */
	@SuppressWarnings("static-method")
	protected final float lerp(final float value1, final float value2, final float t) {
		return (1.0F - t) * value1 + t * value2;
	}
	
	/**
	 * Returns the greater value of {@code a}, {@code b} and {@code c}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return the greater value of {@code a}, {@code b} and {@code c}
	 */
	protected final float max(final float a, final float b, final float c) {
		return max(max(a, b), c);
	}
	
	/**
	 * Returns the smaller value of {@code a}, {@code b} and {@code c}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @return the smaller value of {@code a}, {@code b} and {@code c}
	 */
	protected final float min(final float a, final float b, final float c) {
		return min(min(a, b), c);
	}
	
	/**
	 * Performs a modulo operation on {@code x} and {@code y}.
	 * <p>
	 * Returns a {@code float} value.
	 * <p>
	 * The modulo operation performed by this method differs slightly from the modulo operator in Java.
	 * <p>
	 * If {@code x} is positive, the following occurs:
	 * <pre>
	 * {@code
	 * float z = remainder(x, y);
	 * }
	 * </pre>
	 * If {@code x} is negative, the following occurs:
	 * <pre>
	 * {@code
	 * float z = remainder((remainder(x, y) + y), y);
	 * }
	 * </pre>
	 * 
	 * @param x a {@code float} value
	 * @param y a {@code float} value
	 * @return a {@code float} value
	 */
	protected final float positiveModuloF(final float x, final float y) {
		return x < 0.0F ? remainder(remainder(x, y) + y, y) : remainder(x, y);
	}
	
	/**
	 * Returns {@code base} raised to the power of {@code 5.0F}.
	 * <p>
	 * This method should be faster than {@code pow(float, float)}.
	 * 
	 * @param base the base
	 * @return {@code base} raised to the power of {@code 5.0F}
	 */
	@SuppressWarnings("static-method")
	protected final float pow5(final float base) {
		return (base * base) * (base * base) * base;
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
	protected final float normalize(final float value, final float a, final float b) {
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
	protected final float perlinFractalXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float perlinFractionalBrownianMotionXYZ(final float x, final float y, final float z, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
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
	protected final float perlinNoiseXYZ(final float x, final float y, final float z) {
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
		
		final int a0 = this.permutationsBArray[x0] + y0;
		final int a1 = this.permutationsBArray[a0] + z0;
		final int a2 = this.permutationsBArray[a0 + 1] + z0;
		final int b0 = this.permutationsBArray[x0 + 1] + y0;
		final int b1 = this.permutationsBArray[b0] + z0;
		final int b2 = this.permutationsBArray[b0 + 1] + z0;
		final int hash0 = this.permutationsBArray[a1] & 15;
		final int hash1 = this.permutationsBArray[b1] & 15;
		final int hash2 = this.permutationsBArray[a2] & 15;
		final int hash3 = this.permutationsBArray[b2] & 15;
		final int hash4 = this.permutationsBArray[a1 + 1] & 15;
		final int hash5 = this.permutationsBArray[b1 + 1] & 15;
		final int hash6 = this.permutationsBArray[a2 + 1] & 15;
		final int hash7 = this.permutationsBArray[b2 + 1] & 15;
		
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
	protected final float perlinTurbulenceXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float perlinTurbulenceXYZ(final float x, final float y, final float z, final int octaves) {
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
	 * Returns a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive).
	 * 
	 * @return a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive)
	 */
	protected final float random() {
		return doNext(24) * PRNG_NEXT_FLOAT_RECIPROCAL;
	}
	
	/**
	 * Returns the remainder of {@code x} and {@code y}.
	 * 
	 * @param x the left hand side of the remainder operation
	 * @param y the right hand side of the remainder operation
	 * @return the remainder of {@code x} and {@code y}
	 */
	@SuppressWarnings("static-method")
	protected final float remainder(final float x, final float y) {
		return x - (int)(x / y) * y;
	}
	
	/**
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * If {@code value} is less than {@code valueMinimum}, {@code valueMinimum} will be returned. If {@code value} is greater than {@code valueMaximum}, {@code valueMaximum} will be returned. Otherwise {@code value} will be returned.
	 * 
	 * @param value the value to saturate (or clamp)
	 * @param valueMinimum the minimum value
	 * @param valueMaximum the maximum value
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	protected final float saturateF(final float value, final float valueMinimum, final float valueMaximum) {
		return max(min(value, valueMaximum), valueMinimum);
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
	protected final float simplexFractalX(final float x, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexFractalXY(final float x, final float y, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexFractalXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexFractalXYZW(final float x, final float y, final float z, final float w, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexFractionalBrownianMotionX(final float x, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
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
	protected final float simplexFractionalBrownianMotionXY(final float x, final float y, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
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
	protected final float simplexFractionalBrownianMotionXYZ(final float x, final float y, final float z, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
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
	protected final float simplexFractionalBrownianMotionXYZW(final float x, final float y, final float z, final float w, final float frequency, final float gain, final float minimum, final float maximum, final int octaves) {
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
	protected final float simplexNoiseX(final float x) {
		final int i0 = doFastFloorToInt(x);
		final int i1 = i0 + 1;
		
		final float x0 = x - i0;
		final float x1 = x0 - 1.0F;
		
		final float t00 = 1.0F - x0 * x0;
		final float t01 = t00 * t00;
		
		final float t10 = 1.0F - x1 * x1;
		final float t11 = t10 * t10;
		
		final int hash00 = this.permutationsBArray[abs(i0) % 512];
		final int hash01 = hash00 & 0x0F;
		final int hash10 = this.permutationsBArray[abs(i1) % 512];
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
	protected final float simplexNoiseXY(final float x, final float y) {
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
		final float x2 = x0 - 1.0F + SIMPLEX_G2_2;
		final float y2 = y0 - 1.0F + SIMPLEX_G2_2;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		
		final int gi0 = this.permutationsBModulo12Array[ii      + this.permutationsBArray[jj     ]];
		final int gi1 = this.permutationsBModulo12Array[ii + i1 + this.permutationsBArray[jj + j1]];
		final int gi2 = this.permutationsBModulo12Array[ii +  1 + this.permutationsBArray[jj +  1]];
		
		final float t0 = 0.5F - x0 * x0 - y0 * y0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (this.simplexGradient3Array[gi0 * 3] * x0 + this.simplexGradient3Array[gi0 * 3 + 1] * y0);
		
		final float t1 = 0.5F - x1 * x1 - y1 * y1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (this.simplexGradient3Array[gi1 * 3] * x1 + this.simplexGradient3Array[gi1 * 3 + 1] * y1);
		
		final float t2 = 0.5F - x2 * x2 - y2 * y2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (this.simplexGradient3Array[gi2 * 3] * x2 + this.simplexGradient3Array[gi2 * 3 + 1] * y2);
		
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
	protected final float simplexNoiseXYZ(final float x, final float y, final float z) {
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
		final float x2 = x0 - i2 + SIMPLEX_G3_2;
		final float y2 = y0 - j2 + SIMPLEX_G3_2;
		final float z2 = z0 - k2 + SIMPLEX_G3_2;
		final float x3 = x0 - 1.0F + SIMPLEX_G3_3;
		final float y3 = y0 - 1.0F + SIMPLEX_G3_3;
		final float z3 = z0 - 1.0F + SIMPLEX_G3_3;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		
		final int gi0 = this.permutationsBModulo12Array[ii      + this.permutationsBArray[jj      + this.permutationsBArray[kk     ]]];
		final int gi1 = this.permutationsBModulo12Array[ii + i1 + this.permutationsBArray[jj + j1 + this.permutationsBArray[kk + k1]]];
		final int gi2 = this.permutationsBModulo12Array[ii + i2 + this.permutationsBArray[jj + j2 + this.permutationsBArray[kk + k2]]];
		final int gi3 = this.permutationsBModulo12Array[ii +  1 + this.permutationsBArray[jj +  1 + this.permutationsBArray[kk +  1]]];
		
		final float t0 = 0.6F - x0 * x0 - y0 * y0 - z0 * z0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (this.simplexGradient3Array[gi0 * 3] * x0 + this.simplexGradient3Array[gi0 * 3 + 1] * y0 + this.simplexGradient3Array[gi0 * 3 + 2] * z0);
		
		final float t1 = 0.6F - x1 * x1 - y1 * y1 - z1 * z1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (this.simplexGradient3Array[gi1 * 3] * x1 + this.simplexGradient3Array[gi1 * 3 + 1] * y1 + this.simplexGradient3Array[gi1 * 3 + 2] * z1);
		
		final float t2 = 0.6F - x2 * x2 - y2 * y2 - z2 * z2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (this.simplexGradient3Array[gi2 * 3] * x2 + this.simplexGradient3Array[gi2 * 3 + 1] * y2 + this.simplexGradient3Array[gi2 * 3 + 2] * z2);
		
		final float t3 = 0.6F - x3 * x3 - y3 * y3 - z3 * z3;
		final float n3 = t3 < 0.0F ? 0.0F : (t3 * t3) * (t3 * t3) * (this.simplexGradient3Array[gi3 * 3] * x3 + this.simplexGradient3Array[gi3 * 3 + 1] * y3 + this.simplexGradient3Array[gi3 * 3 + 2] * z3);
		
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
	protected final float simplexNoiseXYZW(final float x, final float y, final float z, final float w) {
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
		final float x2 = x0 - i2 + SIMPLEX_G4_2;
		final float y2 = y0 - j2 + SIMPLEX_G4_2;
		final float z2 = z0 - k2 + SIMPLEX_G4_2;
		final float w2 = w0 - l2 + SIMPLEX_G4_2;
		final float x3 = x0 - i3 + SIMPLEX_G4_3;
		final float y3 = y0 - j3 + SIMPLEX_G4_3;
		final float z3 = z0 - k3 + SIMPLEX_G4_3;
		final float w3 = w0 - l3 + SIMPLEX_G4_3;
		final float x4 = x0 - 1.0F + SIMPLEX_G4_4;
		final float y4 = y0 - 1.0F + SIMPLEX_G4_4;
		final float z4 = z0 - 1.0F + SIMPLEX_G4_4;
		final float w4 = w0 - 1.0F + SIMPLEX_G4_4;
		
		final int ii = i & 0xFF;
		final int jj = j & 0xFF;
		final int kk = k & 0xFF;
		final int ll = l & 0xFF;
		
		final int gi0 = this.permutationsBArray[ii      + this.permutationsBArray[jj      + this.permutationsBArray[kk      + this.permutationsBArray[ll     ]]]] % 32;
		final int gi1 = this.permutationsBArray[ii + i1 + this.permutationsBArray[jj + j1 + this.permutationsBArray[kk + k1 + this.permutationsBArray[ll + l1]]]] % 32;
		final int gi2 = this.permutationsBArray[ii + i2 + this.permutationsBArray[jj + j2 + this.permutationsBArray[kk + k2 + this.permutationsBArray[ll + l2]]]] % 32;
		final int gi3 = this.permutationsBArray[ii + i3 + this.permutationsBArray[jj + j3 + this.permutationsBArray[kk + k3 + this.permutationsBArray[ll + l3]]]] % 32;
		final int gi4 = this.permutationsBArray[ii +  1 + this.permutationsBArray[jj +  1 + this.permutationsBArray[kk +  1 + this.permutationsBArray[ll +  1]]]] % 32;
		
		final float t0 = 0.6F - x0 * x0 - y0 * y0 - z0 * z0 - w0 * w0;
		final float n0 = t0 < 0.0F ? 0.0F : (t0 * t0) * (t0 * t0) * (this.simplexGradient4Array[gi0 * 4] * x0 + this.simplexGradient4Array[gi0 * 4 + 1] * y0 + this.simplexGradient4Array[gi0 * 4 + 2] * z0 + this.simplexGradient4Array[gi0 * 4 + 3] * w0);
		
		final float t1 = 0.6F - x1 * x1 - y1 * y1 - z1 * z1 - w1 * w1;
		final float n1 = t1 < 0.0F ? 0.0F : (t1 * t1) * (t1 * t1) * (this.simplexGradient4Array[gi1 * 4] * x1 + this.simplexGradient4Array[gi1 * 4 + 1] * y1 + this.simplexGradient4Array[gi1 * 4 + 2] * z1 + this.simplexGradient4Array[gi1 * 4 + 3] * w1);
		
		final float t2 = 0.6F - x2 * x2 - y2 * y2 - z2 * z2 - w2 * w2;
		final float n2 = t2 < 0.0F ? 0.0F : (t2 * t2) * (t2 * t2) * (this.simplexGradient4Array[gi2 * 4] * x2 + this.simplexGradient4Array[gi2 * 4 + 1] * y2 + this.simplexGradient4Array[gi2 * 4 + 2] * z2 + this.simplexGradient4Array[gi2 * 4 + 3] * w2);
		
		final float t3 = 0.6F - x3 * x3 - y3 * y3 - z3 * z3 - w3 * w3;
		final float n3 = t3 < 0.0F ? 0.0F : (t3 * t3) * (t3 * t3) * (this.simplexGradient4Array[gi3 * 4] * x3 + this.simplexGradient4Array[gi3 * 4 + 1] * y3 + this.simplexGradient4Array[gi3 * 4 + 2] * z3 + this.simplexGradient4Array[gi3 * 4 + 3] * w3);
		
		final float t4 = 0.6F - x4 * x4 - y4 * y4 - z4 * z4 - w4 * w4;
		final float n4 = t4 < 0.0F ? 0.0F : (t4 * t4) * (t4 * t4) * (this.simplexGradient4Array[gi4 * 4] * x4 + this.simplexGradient4Array[gi4 * 4 + 1] * y4 + this.simplexGradient4Array[gi4 * 4 + 2] * z4 + this.simplexGradient4Array[gi4 * 4 + 3] * w4);
		
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
	protected final float simplexTurbulenceX(final float x, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexTurbulenceXY(final float x, final float y, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexTurbulenceXYZ(final float x, final float y, final float z, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	protected final float simplexTurbulenceXYZW(final float x, final float y, final float z, final float w, final float amplitude, final float frequency, final float gain, final float lacunarity, final int octaves) {
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
	 * Solves the cubic system for the quartic system based on the values {@code p}, {@code q} and {@code r}.
	 * <p>
	 * Returns a {@code float} with the result of the operation.
	 * 
	 * @param p a value
	 * @param q a value
	 * @param r a value
	 * @return a {@code float} with the result of the operation
	 */
	protected final float solveCubicForQuarticSystemF(final float p, final float q, final float r) {
		final float pSquared = p * p;
		final float q0 = (pSquared - 3.0F * q) / 9.0F;
		final float q0Cubed = q0 * q0 * q0;
		final float r0 = (p * (pSquared - 4.5F * q) + 13.5F * r) / 27.0F;
		final float r0Squared = r0 * r0;
		final float d = q0Cubed - r0Squared;
		final float e = p / 3.0F;
		final float q1 = d >= 0.0F ? -2.0F * sqrt(q0) : pow(sqrt(r0Squared - q0Cubed) + abs(r0), 1.0F / 3.0F);
		final float q2 = d >= 0.0F ? q1 * cos(acos(r0 / sqrt(q0Cubed)) / 3.0F) - e : r0 < 0.0F ? (q1 + q0 / q1) - e : -(q1 + q0 / q1) - e;
		
		return q2;
	}
	
	/**
	 * Attempts to solve the quadratic system based on the values {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param minimum the minimum boundary
	 * @param maximum the maximum boundary
	 * @return the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}
	 */
	protected final float solveQuadraticSystem(final float a, final float b, final float c, final float minimum, final float maximum) {
		final float discriminantSquared = b * b - 4.0F * a * c;
		
		if(discriminantSquared == -0.0F || discriminantSquared == +0.0F) {
			final float q0 = -0.5F * b / a;
			final float q1 = q0 > minimum && q0 < maximum ? q0 : 0.0F;
			
			return q1;
		} else if(discriminantSquared > 0.0F) {
			final float discriminant = sqrt(discriminantSquared);
			
			final float q0 = -0.5F * (b > 0.0F ? b + discriminant : b - discriminant);
			final float q1 = q0 / a;
			final float q2 = c / q0;
			final float q3 = min(q1, q2);
			final float q4 = max(q1, q2);
			final float q5 = q3 > minimum && q3 < maximum ? q3 : q4 > minimum && q4 < maximum ? q4 : 0.0F;
			
			return q5;
		} else {
			return 0.0F;
		}
	}
	
	/**
	 * Returns the maximum value of the solution to the quadratic system computed by {@link #solveQuadraticSystemToArray(float, float, float, float, float)}.
	 * 
	 * @return the maximum value of the solution to the quadratic system computed by {@code  solveQuadraticSystemToArray(float, float, float, float, float)}
	 */
	protected final float solveQuadraticSystemToArrayGetMaximum() {
		return this.quadraticSystemArray_$private$2[1];
	}
	
	/**
	 * Returns the minimum value of the solution to the quadratic system computed by {@link #solveQuadraticSystemToArray(float, float, float, float, float)}.
	 * 
	 * @return the minimum value of the solution to the quadratic system computed by {@code  solveQuadraticSystemToArray(float, float, float, float, float)}
	 */
	protected final float solveQuadraticSystemToArrayGetMinimum() {
		return this.quadraticSystemArray_$private$2[0];
	}
	
	/**
	 * Attempts to solve the quartic system based on the values {@code a}, {@code b}, {@code c}, {@code d} and {@code e}.
	 * <p>
	 * Returns the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @param e a value
	 * @param minimum the minimum boundary
	 * @param maximum the maximum boundary
	 * @return the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}
	 */
	protected final float solveQuarticSystemD(final double a, final double b, final double c, final double d, final double e, final float minimum, final float maximum) {
		final double aReciprocal = 1.0D / a;
		final double bA = b * aReciprocal;
		final double bASquared = bA * bA;
		final double cA = c * aReciprocal;
		final double dA = d * aReciprocal;
		final double eA = e * aReciprocal;
		final double p = -0.375D * bASquared + cA;
		final double q = 0.125D * bASquared * bA - 0.5D * bA * cA + dA;
		final double r = -0.01171875D * bASquared * bASquared + 0.0625D * bASquared * cA - 0.25D * bA * dA + eA;
		final double z = solveCubicForQuarticSystemD(-0.5D * p, -r, 0.5D * r * p - 0.125D * q * q);
		final double v = 2.0D * z - p;
		
		if(v < 0.0D || v < 1.0e-10D && z * z - r < 0.0D) {
			return 0.0F;
		}
		
		final double d1 = v < 1.0e-10D ? v : sqrt(v);
		final double d2 = v < 1.0e-10D ? sqrt(z * z - r) : 0.5D * q / d1;
		final double q1 = d1 * d1;
		final double q2 = -0.25D * bA;
		final double pm = q1 - 4.0D * (z - d2);
		final double pp = q1 - 4.0D * (z + d2);
		
		if(pm >= 0.0D && pp >= 0.0D) {
			final double pmSqrt = sqrt(pm);
			final double ppSqrt = sqrt(pp);
			
			double result0 = -0.5D * (d1 + pmSqrt) + q2;
			double result1 = -0.5D * (d1 - pmSqrt) + q2;
			double result2 = +0.5D * (d1 + ppSqrt) + q2;
			double result3 = +0.5D * (d1 - ppSqrt) + q2;
			double result4 = 0.0D;
			
			if(result0 > result1) {
				result4 = result0;
				result0 = result1;
				result1 = result4;
			}
			
			if(result2 > result3) {
				result4 = result2;
				result2 = result3;
				result3 = result4;
			}
			
			if(result0 > result2) {
				result4 = result0;
				result0 = result2;
				result2 = result4;
			}
			
			if(result1 > result3) {
				result4 = result1;
				result1 = result3;
				result3 = result4;
			}
			
			if(result1 > result2) {
				result4 = result1;
				result1 = result2;
				result2 = result4;
			}
			
			if(result0 >= maximum || result3 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return (float)(result0);
			} else if(result1 > minimum) {
				return (float)(result1);
			} else if(result2 > minimum) {
				return (float)(result2);
			} else if(result3 > minimum) {
				return (float)(result3);
			} else {
				return 0.0F;
			}
		} else if(pm >= 0.0D) {
			final double pmSqrt = sqrt(pm);
			
			final double result0 = -0.5D * (d1 + pmSqrt) + q2;
			final double result1 = -0.5D * (d1 - pmSqrt) + q2;
			
			if(result0 >= maximum || result1 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return (float)(result0);
			} else if(result1 > minimum) {
				return (float)(result1);
			} else {
				return 0.0F;
			}
		} else if(pp >= 0.0D) {
			final double ppSqrt = sqrt(pp);
			
			final double result0 = +0.5D * (d1 - ppSqrt) + q2;
			final double result1 = +0.5D * (d1 + ppSqrt) + q2;
			
			if(result0 >= maximum || result1 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return (float)(result0);
			} else if(result1 > minimum) {
				return (float)(result1);
			} else {
				return 0.0F;
			}
		} else {
			return 0.0F;
		}
	}
	
	/**
	 * Attempts to solve the quartic system based on the values {@code a}, {@code b}, {@code c}, {@code d} and {@code e}.
	 * <p>
	 * Returns the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param d a value
	 * @param e a value
	 * @param minimum the minimum boundary
	 * @param maximum the maximum boundary
	 * @return the smallest solution that is greater than {@code minimum} and less than {@code maximum} or {@code 0.0F}
	 */
	protected final float solveQuarticSystemF(final float a, final float b, final float c, final float d, final float e, final float minimum, final float maximum) {
		final float aReciprocal = 1.0F / a;
		final float bA = b * aReciprocal;
		final float bASquared = bA * bA;
		final float cA = c * aReciprocal;
		final float dA = d * aReciprocal;
		final float eA = e * aReciprocal;
		final float p = -0.375F * bASquared + cA;
		final float q = 0.125F * bASquared * bA - 0.5F * bA * cA + dA;
		final float r = -0.01171875F * bASquared * bASquared + 0.0625F * bASquared * cA - 0.25F * bA * dA + eA;
		final float z = solveCubicForQuarticSystemF(-0.5F * p, -r, 0.5F * r * p - 0.125F * q * q);
		final float v = 2.0F * z - p;
		
		if(v < 0.0F || v < 1.0e-4F && z * z - r < 0.0F) {
			return 0.0F;
		}
		
//		The expression was changed from 'v < 1.0e-10F' to 'v < 1.0e-4F' because of artifacts on the torus.
		final float d1 = v < 1.0e-4F ? v : sqrt(v);
		final float d2 = v < 1.0e-4F ? sqrt(z * z - r) : 0.5F * q / d1;
		final float q1 = d1 * d1;
		final float q2 = -0.25F * bA;
		final float pm = q1 - 4.0F * (z - d2);
		final float pp = q1 - 4.0F * (z + d2);
		
		if(pm >= 0.0F && pp >= 0.0F) {
			final float pmSqrt = sqrt(pm);
			final float ppSqrt = sqrt(pp);
			
			float result0 = -0.5F * (d1 + pmSqrt) + q2;
			float result1 = -0.5F * (d1 - pmSqrt) + q2;
			float result2 = +0.5F * (d1 + ppSqrt) + q2;
			float result3 = +0.5F * (d1 - ppSqrt) + q2;
			float result4 = 0.0F;
			
			if(result0 > result1) {
				result4 = result0;
				result0 = result1;
				result1 = result4;
			}
			
			if(result2 > result3) {
				result4 = result2;
				result2 = result3;
				result3 = result4;
			}
			
			if(result0 > result2) {
				result4 = result0;
				result0 = result2;
				result2 = result4;
			}
			
			if(result1 > result3) {
				result4 = result1;
				result1 = result3;
				result3 = result4;
			}
			
			if(result1 > result2) {
				result4 = result1;
				result1 = result2;
				result2 = result4;
			}
			
			if(result0 >= maximum || result3 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return result0;
			} else if(result1 > minimum) {
				return result1;
			} else if(result2 > minimum) {
				return result2;
			} else if(result3 > minimum) {
				return result3;
			} else {
				return 0.0F;
			}
		} else if(pm >= 0.0F) {
			final float pmSqrt = sqrt(pm);
			
			final float result0 = -0.5F * (d1 + pmSqrt) + q2;
			final float result1 = -0.5F * (d1 - pmSqrt) + q2;
			
			if(result0 >= maximum || result1 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return result0;
			} else if(result1 > minimum) {
				return result1;
			} else {
				return 0.0F;
			}
		} else if(pp >= 0.0F) {
			final float ppSqrt = sqrt(pp);
			
			final float result0 = +0.5F * (d1 - ppSqrt) + q2;
			final float result1 = +0.5F * (d1 + ppSqrt) + q2;
			
			if(result0 >= maximum || result1 <= minimum) {
				return 0.0F;
			} else if(result0 > minimum) {
				return result0;
			} else if(result1 > minimum) {
				return result1;
			} else {
				return 0.0F;
			}
		} else {
			return 0.0F;
		}
	}
	
	/**
	 * Returns the resolution of this {@code AbstractKernel} instance.
	 * <p>
	 * The resolution is the same as {@code getResolutionX() * getResolutionY()}.
	 * 
	 * @return the resolution of this {@code AbstractKernel} instance
	 */
	protected final int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution for the X-axis of this {@code AbstractKernel} instance.
	 * 
	 * @return the resolution for the X-axis of this {@code AbstractKernel} instance
	 */
	protected final int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution for the Y-axis of this {@code AbstractKernel} instance.
	 * 
	 * @return the resolution for the Y-axis of this {@code AbstractKernel} instance
	 */
	protected final int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Performs a modulo operation on {@code x} and {@code y}.
	 * <p>
	 * Returns an {@code int} value.
	 * <p>
	 * The modulo operation performed by this method differs slightly from the modulo operator in Java.
	 * <p>
	 * If {@code x} is positive, the following occurs:
	 * <pre>
	 * {@code
	 * int z = x % y;
	 * }
	 * </pre>
	 * If {@code x} is negative, the following occurs:
	 * <pre>
	 * {@code
	 * int z = (x % y + y) % y;
	 * }
	 * </pre>
	 * 
	 * @param x an {@code int} value
	 * @param y an {@code int} value
	 * @return an {@code int} value
	 */
	@SuppressWarnings("static-method")
	protected final int positiveModuloI(final int x, final int y) {
		return x < 0 ? (x % y + y) % y : x % y;
	}
	
	/**
	 * Returns a saturated (or clamped) value based on {@code value}.
	 * <p>
	 * If {@code value} is less than {@code valueMinimum}, {@code valueMinimum} will be returned. If {@code value} is greater than {@code valueMaximum}, {@code valueMaximum} will be returned. Otherwise {@code value} will be returned.
	 * 
	 * @param value the value to saturate (or clamp)
	 * @param valueMinimum the minimum value
	 * @param valueMaximum the maximum value
	 * @return a saturated (or clamped) value based on {@code value}
	 */
	protected final int saturateI(final int value, final int valueMinimum, final int valueMaximum) {
		return max(min(value, valueMaximum), valueMinimum);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSet(final int index) {
		eFloatSetValue(index, 0.0F);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetAbs(final int index, final float value, final float lowerBound, final float upperBound) {
		if(lowerBound >= 0.0F) {
			this.eFloatArray_$private$9[index * 3 + 0] = value;
			this.eFloatArray_$private$9[index * 3 + 1] = lowerBound;
			this.eFloatArray_$private$9[index * 3 + 2] = upperBound;
		} else if(upperBound <= 0.0F) {
			this.eFloatArray_$private$9[index * 3 + 0] = -value;
			this.eFloatArray_$private$9[index * 3 + 1] = -upperBound;
			this.eFloatArray_$private$9[index * 3 + 2] = -lowerBound;
		} else {
			this.eFloatArray_$private$9[index * 3 + 0] = abs(value);
			this.eFloatArray_$private$9[index * 3 + 1] = 0.0F;
			this.eFloatArray_$private$9[index * 3 + 2] = max(-lowerBound, upperBound);
		}
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetAdd(final int index, final float aValue, final float aLowerBound, final float aUpperBound, final float bValue, final float bLowerBound, final float bUpperBound) {
		this.eFloatArray_$private$9[index * 3 + 0] = aValue + bValue;
		this.eFloatArray_$private$9[index * 3 + 1] = nextAfter(aLowerBound + bLowerBound, -1);
		this.eFloatArray_$private$9[index * 3 + 2] = nextAfter(aUpperBound + bUpperBound, +1);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetDivide(final int index, final float aValue, final float aLowerBound, final float aUpperBound, final float bValue, final float bLowerBound, final float bUpperBound) {
		final float value = aValue / bValue;
		
		if(bLowerBound < 0.0F && bUpperBound > 0.0F) {
			final float lowerBound = Float.NEGATIVE_INFINITY;
			final float upperBound = Float.POSITIVE_INFINITY;
			
			this.eFloatArray_$private$9[index * 3 + 0] = value;
			this.eFloatArray_$private$9[index * 3 + 1] = lowerBound;
			this.eFloatArray_$private$9[index * 3 + 2] = upperBound;
		} else {
			final float div0 = aLowerBound / bLowerBound;
			final float div1 = aUpperBound / bLowerBound;
			final float div2 = aLowerBound / bUpperBound;
			final float div3 = aUpperBound / bUpperBound;
			
			final float lowerBound = nextAfter(min(min(div0, div1), min(div2, div3)), -1);
			final float upperBound = nextAfter(max(max(div0, div1), max(div2, div3)), +1);
			
			this.eFloatArray_$private$9[index * 3 + 0] = value;
			this.eFloatArray_$private$9[index * 3 + 1] = lowerBound;
			this.eFloatArray_$private$9[index * 3 + 2] = upperBound;
		}
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetMultiply(final int index, final float aValue, final float aLowerBound, final float aUpperBound, final float bValue, final float bLowerBound, final float bUpperBound) {
		final float value = aValue * bValue;
		
		final float prod0 = aLowerBound * bLowerBound;
		final float prod1 = aUpperBound * bLowerBound;
		final float prod2 = aLowerBound * bUpperBound;
		final float prod3 = aUpperBound * bUpperBound;
		
		final float lowerBound = nextAfter(min(min(prod0, prod1), min(prod2, prod3)), -1);
		final float upperBound = nextAfter(max(max(prod0, prod1), max(prod2, prod3)), +1);
		
		this.eFloatArray_$private$9[index * 3 + 0] = value;
		this.eFloatArray_$private$9[index * 3 + 1] = lowerBound;
		this.eFloatArray_$private$9[index * 3 + 2] = upperBound;
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetNegate(final int index, final float value, final float lowerBound, final float upperBound) {
		this.eFloatArray_$private$9[index * 3 + 0] = -value;
		this.eFloatArray_$private$9[index * 3 + 1] = -upperBound;
		this.eFloatArray_$private$9[index * 3 + 2] = -lowerBound;
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetSqrt(final int index, final float value, final float lowerBound, final float upperBound) {
		this.eFloatArray_$private$9[index * 3 + 0] = sqrt(value);
		this.eFloatArray_$private$9[index * 3 + 1] = nextAfter(sqrt(lowerBound), -1);
		this.eFloatArray_$private$9[index * 3 + 2] = nextAfter(sqrt(upperBound), +1);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetSubtract(final int index, final float aValue, final float aLowerBound, final float aUpperBound, final float bValue, final float bLowerBound, final float bUpperBound) {
		this.eFloatArray_$private$9[index * 3 + 0] = aValue - bValue;
		this.eFloatArray_$private$9[index * 3 + 1] = nextAfter(aLowerBound - bUpperBound, -1);
		this.eFloatArray_$private$9[index * 3 + 2] = nextAfter(aUpperBound - bLowerBound, +1);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetValue(final int index, final float value) {
		eFloatSetValueError(index, value, 0.0F);
	}
	
//	TODO: Add Javadocs!
	protected final void eFloatSetValueError(final int index, final float value, final float error) {
		if(error == 0.0F) {
			this.eFloatArray_$private$9[index * 3 + 0] = value;
			this.eFloatArray_$private$9[index * 3 + 1] = value;
			this.eFloatArray_$private$9[index * 3 + 2] = value;
		} else {
			this.eFloatArray_$private$9[index * 3 + 0] = value;
			this.eFloatArray_$private$9[index * 3 + 1] = nextAfter(value - error, -1);
			this.eFloatArray_$private$9[index * 3 + 2] = nextAfter(value + error, +1);
		}
	}
	
	/**
	 * Sets the resolution for this {@code AbstractKernel} instance.
	 * 
	 * @param resolutionX the resolution for the X-axis
	 * @param resolutionY the resolution for the Y-axis
	 */
	protected final void setResolution(final int resolutionX, final int resolutionY) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
	}
	
	/**
	 * Attempts to solve the quadratic system based on the values {@code a}, {@code b} and {@code c}.
	 * 
	 * @param a a value
	 * @param b a value
	 * @param c a value
	 * @param minimum the minimum boundary
	 * @param maximum the maximum boundary
	 */
	protected final void solveQuadraticSystemToArray(final float a, final float b, final float c, final float minimum, final float maximum) {
		final float discriminantSquared = b * b - 4.0F * a * c;
		
		if(discriminantSquared == -0.0F || discriminantSquared == +0.0F) {
			final float q0 = -0.5F * b / a;
			final float q1 = q0 > minimum && q0 < maximum ? q0 : 0.0F;
			
			this.quadraticSystemArray_$private$2[0] = q1;
			this.quadraticSystemArray_$private$2[1] = 0.0F;
		} else if(discriminantSquared > 0.0F) {
			final float discriminant = sqrt(discriminantSquared);
			
			final float q0 = -0.5F * (b > 0.0F ? b + discriminant : b - discriminant);
			final float q1 = q0 / a;
			final float q2 = c / q0;
			final float q3 = min(q1, q2);
			final float q4 = max(q1, q2);
			final float q5 = q3 > minimum && q3 < maximum ? q3 : q4 > minimum && q4 < maximum ? q4 : 0.0F;
			final float q6 = q5 == q3 && q4 > minimum && q4 < maximum ? q4 : 0.0F;
			
			this.quadraticSystemArray_$private$2[0] = q5;
			this.quadraticSystemArray_$private$2[1] = q6;
		} else {
			this.quadraticSystemArray_$private$2[0] = 0.0F;
			this.quadraticSystemArray_$private$2[1] = 0.0F;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private int doFastFloorToInt(final float value) {
		final int i = (int)(value);
		
		return value < i ? i - 1 : i;
	}
	
	private int doNext(final int bits) {
		final long oldSeed = this.seedArray[getGlobalId()];
		final long newSeed = (oldSeed * PRNG_MULTIPLIER + PRNG_ADDEND) & PRNG_MASK;
		
		this.seedArray[getGlobalId()] = newSeed;
		
		return (int)(newSeed >>> (48 - bits));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float[] doCreateSimplexGradient3Array() {
		return new float[] {
			+1.0F, +1.0F, +0.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F, +0.0F,
			+1.0F, +0.0F, +1.0F, -1.0F, +0.0F, +1.0F, +1.0F, +0.0F, -1.0F, -1.0F, +0.0F, -1.0F,
			+0.0F, +1.0F, +1.0F, +0.0F, -1.0F, +1.0F, +0.0F, +1.0F, -1.0F, +0.0F, -1.0F, -1.0F
		};
	}
	
	private static float[] doCreateSimplexGradient4Array() {
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
	
	private static int[] doCreatePermutationsAArray() {
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
	
	private static int[] doCreatePermutationsBArray() {
		final int[] permutationsAArray = doCreatePermutationsAArray();
		final int[] permutationsBArray = new int[permutationsAArray.length * 2];
		
		for(int i = 0; i < permutationsBArray.length; i++) {
			permutationsBArray[i] = permutationsAArray[i % permutationsAArray.length];
		}
		
		return permutationsBArray;
	}
	
	private static int[] doCreatePermutationsBModulo12Array() {
		final int[] permutationsBModulo12Array = doCreatePermutationsBArray();
		
		for(int i = 0; i < permutationsBModulo12Array.length; i++) {
			permutationsBModulo12Array[i] = permutationsBModulo12Array[i] % 12;
		}
		
		return permutationsBModulo12Array;
	}
}