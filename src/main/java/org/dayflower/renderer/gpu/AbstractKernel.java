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
package org.dayflower.renderer.gpu;

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

import org.dayflower.util.Longs;
import org.dayflower.util.ParameterArguments;

import com.amd.aparapi.Kernel;

/**
 * An {@code AbstractKernel} is an abstract implementation of {@code Kernel} that adds functionality.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractKernel extends Kernel {
//	TODO: Add Javadocs!
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U = 0;
	
//	TODO: Add Javadocs!
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V = 3;
	
//	TODO: Add Javadocs!
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W = 6;
	
//	TODO: Add Javadocs!
	protected static final int ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE = 9;
	
//	TODO: Add Javadocs!
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	
//	TODO: Add Javadocs!
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	
//	TODO: Add Javadocs!
	protected static final int POINT_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	
//	TODO: Add Javadocs!
	protected static final int POINT_3_F_ARRAY_SIZE = 3;
	
//	TODO: Add Javadocs!
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1 = 0;
	
//	TODO: Add Javadocs!
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2 = 1;
	
//	TODO: Add Javadocs!
	protected static final int VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3 = 2;
	
//	TODO: Add Javadocs!
	protected static final int VECTOR_3_F_ARRAY_SIZE = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final float PRNG_NEXT_FLOAT_RECIPROCAL = 1.0F / (1 << 24);
	private static final long PRNG_ADDEND = 0xBL;
	private static final long PRNG_MASK = (1L << 48L) - 1L;
	private static final long PRNG_MULTIPLIER = 0x5DEECE66DL;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] orthonormalBasis33FArray_$private$9;
	
//	TODO: Add Javadocs!
	protected float[] point3FArray_$private$3;
	
//	TODO: Add Javadocs!
	protected float[] vector3FArray_$private$3;
	
//	TODO: Add Javadocs!
	protected int resolution;
	
//	TODO: Add Javadocs!
	protected int resolutionX;
	
//	TODO: Add Javadocs!
	protected int resolutionY;
	
//	TODO: Add Javadocs!
	protected long[] seedArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractKernel} instance.
	 */
	protected AbstractKernel() {
		this.orthonormalBasis33FArray_$private$9 = new float[ORTHONORMAL_BASIS_3_3_F_ARRAY_SIZE];
		this.point3FArray_$private$3 = new float[POINT_3_F_ARRAY_SIZE];
		this.vector3FArray_$private$3 = new float[VECTOR_3_F_ARRAY_SIZE];
		this.resolutionX = 0;
		this.resolutionY = 0;
		this.seedArray = new long[0];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Disposes of any resources created by this {@code AbstractKernel} instance.
	 */
	@Override
	public final synchronized void dispose() {
		super.dispose();
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractKernel} instance.
	 */
	public void setup() {
		setExplicit(true);
		
		doSetupSeedArray();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected final byte[] getAndReturn(final byte[] array) {
		get(array);
		
		return array;
	}
	
//	TODO: Add Javadocs!
	protected final double solveCubicForQuarticSystemDouble(final double p, final double q, final double r) {
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
	
//	TODO: Add Javadocs!
	@SuppressWarnings("static-method")
	protected final float addIfLessThanThreshold(final float value, final float threshold, final float valueAdd) {
		return value < threshold ? value + valueAdd : value;
	}
	
//	TODO: Add Javadocs!
	protected final float max(final float a, final float b, final float c) {
		return max(max(a, b), c);
	}
	
//	TODO: Add Javadocs!
	protected final float min(final float a, final float b, final float c) {
		return min(min(a, b), c);
	}
	
//	TODO: Add Javadocs!
	protected final float normalize(final float value, final float a, final float b) {
		final float maximum = max(a, b);
		final float minimum = min(a, b);
		final float valueNormalized = (value - minimum) / (maximum - minimum);
		
		return valueNormalized;
	}
	
//	TODO: Add Javadocs!
	protected final float point3FDistance(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return sqrt(point3FDistanceSquared(eyeComponent1, eyeComponent2, eyeComponent3, lookAtComponent1, lookAtComponent2, lookAtComponent3));
	}
	
//	TODO: Add Javadocs!
	protected final float point3FDistanceSquared(final float eyeComponent1, final float eyeComponent2, final float eyeComponent3, final float lookAtComponent1, final float lookAtComponent2, final float lookAtComponent3) {
		return vector3FLengthSquared(lookAtComponent1 - eyeComponent1, lookAtComponent2 - eyeComponent2, lookAtComponent3 - eyeComponent3);
	}
	
	/**
	 * Returns a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive).
	 * 
	 * @return a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive)
	 */
	protected final float random() {
		return doNext(24) * PRNG_NEXT_FLOAT_RECIPROCAL;
	}
	
//	TODO: Add Javadocs!
	protected final float saturateFloat(final float value, final float edgeA, final float edgeB) {
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
	
//	TODO: Add Javadocs!
	protected final float solveCubicForQuarticSystemFloat(final float p, final float q, final float r) {
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
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	protected final float solveQuarticSystemFloat(final float a, final float b, final float c, final float d, final float e, final float minimum, final float maximum) {
		final float aReciprocal = 1.0F / a;
		final float bA = b * aReciprocal;
		final float bASquared = bA * bA;
		final float cA = c * aReciprocal;
		final float dA = d * aReciprocal;
		final float eA = e * aReciprocal;
		final float p = -0.375F * bASquared + cA;
		final float q = 0.125F * bASquared * bA - 0.5F * bA * cA + dA;
		final float r = -0.01171875F * bASquared * bASquared + 0.0625F * bASquared * cA - 0.25F * bA * dA + eA;
		final float z = solveCubicForQuarticSystemFloat(-0.5F * p, -r, 0.5F * r * p - 0.125F * q * q);
		
		float d1 = 2.0F * z - p;
		float d2 = 0.0F;
		
		if(d1 < 0.0F) {
			return 0.0F;
		} else if(d1 < 1.0e-4F) {
//			The expression in the if-statement was changed from 'd1 < 1.0e-10F' to 'd1 < 1.0e-4F' because of artifacts on the torus.
			
			d2 = z * z - r;
			
			if(d2 < 0.0F) {
				return 0.0F;
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
	
//	TODO: Add Javadocs!
	protected final float solveQuarticSystemDouble(final double a, final double b, final double c, final double d, final double e, final float minimum, final float maximum) {
		final double aReciprocal = 1.0D / a;
		final double bA = b * aReciprocal;
		final double bASquared = bA * bA;
		final double cA = c * aReciprocal;
		final double dA = d * aReciprocal;
		final double eA = e * aReciprocal;
		final double p = -0.375D * bASquared + cA;
		final double q = 0.125D * bASquared * bA - 0.5D * bA * cA + dA;
		final double r = -0.01171875D * bASquared * bASquared + 0.0625D * bASquared * cA - 0.25D * bA * dA + eA;
		final double z = solveCubicForQuarticSystemDouble(-0.5D * p, -r, 0.5D * r * p - 0.125D * q * q);
		
		double d1 = 2.0D * z - p;
		double d2 = 0.0D;
		
		if(d1 < 0.0D) {
			return 0.0F;
		} else if(d1 < 1.0e-10D) {
			d2 = z * z - r;
			
			if(d2 < 0.0D) {
				return 0.0F;
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
	
//	TODO: Add Javadocs!
	@SuppressWarnings("static-method")
	protected final float vector3FDotProduct(final float component1LHS, final float component2LHS, final float component3LHS, final float component1RHS, final float component2RHS, final float component3RHS) {
		return component1LHS * component1RHS + component2LHS * component2RHS + component3LHS * component3RHS;
	}
	
//	TODO: Add Javadocs!
	protected final float vector3FLength(final float component1, final float component2, final float component3) {
		return sqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
//	TODO: Add Javadocs!
	protected final float vector3FLengthReciprocal(final float component1, final float component2, final float component3) {
		return rsqrt(vector3FLengthSquared(component1, component2, component3));
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("static-method")
	protected final float vector3FLengthSquared(final float component1, final float component2, final float component3) {
		return component1 * component1 + component2 * component2 + component3 * component3;
	}
	
//	TODO: Add Javadocs!
	protected final float[] getAndReturn(final float[] array) {
		get(array);
		
		return array;
	}
	
//	TODO: Add Javadocs!
	protected final int getResolution() {
		return this.resolution;
	}
	
//	TODO: Add Javadocs!
	protected final int getResolutionX() {
		return this.resolutionX;
	}
	
//	TODO: Add Javadocs!
	protected final int getResolutionY() {
		return this.resolutionY;
	}
	
//	TODO: Add Javadocs!
	protected final int saturateInt(final int value, final int edgeA, final int edgeB) {
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
	
//	TODO: Add Javadocs!
	protected final void orthonormalBasis33FSetFromVector3F() {
		final float orthonormalBasisWX = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float orthonormalBasisWY = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float orthonormalBasisWZ = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		orthonormalBasis33FSetFromW(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
	}
	
//	TODO: Add Javadocs!
	protected final void orthonormalBasis33FSetFromW(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the absolute component values of the normalized W-direction, which are used to determine the orientation of the V-direction of the orthonormal basis:
		final float orthonormalBasisWNormalizedXAbs = abs(orthonormalBasisWNormalizedX);
		final float orthonormalBasisWNormalizedYAbs = abs(orthonormalBasisWNormalizedY);
		final float orthonormalBasisWNormalizedZAbs = abs(orthonormalBasisWNormalizedZ);
		
//		Compute variables used to determine the orientation of the V-direction of the orthonormal basis:
		final boolean isX = orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedYAbs && orthonormalBasisWNormalizedXAbs < orthonormalBasisWNormalizedZAbs;
		final boolean isY = orthonormalBasisWNormalizedYAbs < orthonormalBasisWNormalizedZAbs;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVX = isX ? +0.0F                         : isY ? +orthonormalBasisWNormalizedZ : +orthonormalBasisWNormalizedY;
		final float orthonormalBasisVY = isX ? +orthonormalBasisWNormalizedZ : isY ? +0.0F                         : -orthonormalBasisWNormalizedX;
		final float orthonormalBasisVZ = isX ? -orthonormalBasisWNormalizedY : isY ? -orthonormalBasisWNormalizedX : +0.0F;
		final float orthonormalBasisVLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVX, orthonormalBasisVY, orthonormalBasisVZ);
		final float orthonormalBasisVNormalizedX = orthonormalBasisVX * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedY = orthonormalBasisVY * orthonormalBasisVLengthReciprocal;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisVZ * orthonormalBasisVLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUNormalizedX = orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUNormalizedY = orthonormalBasisVNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisVNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVNormalizedY * orthonormalBasisWNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
//	TODO: Add Javadocs!
	protected final void orthonormalBasis33FSetFromWV(final float orthonormalBasisWX, final float orthonormalBasisWY, final float orthonormalBasisWZ, final float orthonormalBasisVReferenceX, final float orthonormalBasisVReferenceY, final float orthonormalBasisVReferenceZ) {
//		Compute the normalized W-direction of the orthonormal basis:
		final float orthonormalBasisWLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisWX, orthonormalBasisWY, orthonormalBasisWZ);
		final float orthonormalBasisWNormalizedX = orthonormalBasisWX * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedY = orthonormalBasisWY * orthonormalBasisWLengthReciprocal;
		final float orthonormalBasisWNormalizedZ = orthonormalBasisWZ * orthonormalBasisWLengthReciprocal;
		
//		Compute the normalized V-direction used as a reference for the orthonormal basis:
		final float orthonormalBasisVReferenceLengthReciprocal = vector3FLengthReciprocal(orthonormalBasisVReferenceX, orthonormalBasisVReferenceY, orthonormalBasisVReferenceZ);
		final float orthonormalBasisVReferenceNormalizedX = orthonormalBasisVReferenceX * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedY = orthonormalBasisVReferenceY * orthonormalBasisVReferenceLengthReciprocal;
		final float orthonormalBasisVReferenceNormalizedZ = orthonormalBasisVReferenceZ * orthonormalBasisVReferenceLengthReciprocal;
		
//		Compute the normalized U-direction of the orthonormal basis:
		final float orthonormalBasisUNormalizedX = orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedZ - orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedY;
		final float orthonormalBasisUNormalizedY = orthonormalBasisVReferenceNormalizedZ * orthonormalBasisWNormalizedX - orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedZ;
		final float orthonormalBasisUNormalizedZ = orthonormalBasisVReferenceNormalizedX * orthonormalBasisWNormalizedY - orthonormalBasisVReferenceNormalizedY * orthonormalBasisWNormalizedX;
		
//		Compute the normalized V-direction of the orthonormal basis:
		final float orthonormalBasisVNormalizedX = orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedZ - orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedY;
		final float orthonormalBasisVNormalizedY = orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedX - orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedZ;
		final float orthonormalBasisVNormalizedZ = orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedY - orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedX;
		
//		Set the orthonormal basis:
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0] = orthonormalBasisUNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1] = orthonormalBasisUNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2] = orthonormalBasisUNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0] = orthonormalBasisVNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1] = orthonormalBasisVNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2] = orthonormalBasisVNormalizedZ;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0] = orthonormalBasisWNormalizedX;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1] = orthonormalBasisWNormalizedY;
		this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2] = orthonormalBasisWNormalizedZ;
	}
	
//	TODO: Add Javadocs!
	protected final void point3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element12 * oldComponent2 + element13 * oldComponent3 + element14;
		final float newComponent2 = element21 * oldComponent1 + element22 * oldComponent2 + element23 * oldComponent3 + element24;
		final float newComponent3 = element31 * oldComponent1 + element32 * oldComponent2 + element33 * oldComponent3 + element34;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void point3FSetMatrix44FTransformAndDivide(final float element11, final float element12, final float element13, final float element14, final float element21, final float element22, final float element23, final float element24, final float element31, final float element32, final float element33, final float element34, final float element41, final float element42, final float element43, final float element44, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element12 * oldComponent2 + element13 * oldComponent3 + element14;
		final float newComponent2 = element21 * oldComponent1 + element22 * oldComponent2 + element23 * oldComponent3 + element24;
		final float newComponent3 = element31 * oldComponent1 + element32 * oldComponent2 + element33 * oldComponent3 + element34;
		final float newComponent4 = element41 * oldComponent1 + element42 * oldComponent2 + element43 * oldComponent3 + element44;
		
		final boolean isDividing = newComponent4 != +0.0F && newComponent4 != -0.0F && newComponent4 != 1.0F;
		
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_1] = isDividing ? newComponent1 / newComponent4 : newComponent1;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_2] = isDividing ? newComponent2 / newComponent4 : newComponent2;
		this.point3FArray_$private$3[POINT_3_F_ARRAY_OFFSET_COMPONENT_3] = isDividing ? newComponent3 / newComponent4 : newComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void setResolution(final int resolutionX, final int resolutionY) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetMatrix44FTransform(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element12 * oldComponent2 + element13 * oldComponent3;
		final float newComponent2 = element21 * oldComponent1 + element22 * oldComponent2 + element23 * oldComponent3;
		final float newComponent3 = element31 * oldComponent1 + element32 * oldComponent2 + element33 * oldComponent3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetMatrix44FTransformNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element12 * oldComponent2 + element13 * oldComponent3;
		final float newComponent2 = element21 * oldComponent1 + element22 * oldComponent2 + element23 * oldComponent3;
		final float newComponent3 = element31 * oldComponent1 + element32 * oldComponent2 + element33 * oldComponent3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetMatrix44FTransformTranspose(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element21 * oldComponent2 + element31 * oldComponent3;
		final float newComponent2 = element12 * oldComponent1 + element22 * oldComponent2 + element32 * oldComponent3;
		final float newComponent3 = element13 * oldComponent1 + element23 * oldComponent2 + element33 * oldComponent3;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetMatrix44FTransformTransposeNormalize(final float element11, final float element12, final float element13, final float element21, final float element22, final float element23, final float element31, final float element32, final float element33, final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float newComponent1 = element11 * oldComponent1 + element21 * oldComponent2 + element31 * oldComponent3;
		final float newComponent2 = element12 * oldComponent1 + element22 * oldComponent2 + element32 * oldComponent3;
		final float newComponent3 = element13 * oldComponent1 + element23 * oldComponent2 + element33 * oldComponent3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetNormalize(final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float oldLengthReciprocal = vector3FLengthReciprocal(oldComponent1, oldComponent2, oldComponent3);
		
		final float newComponent1 = oldComponent1 * oldLengthReciprocal;
		final float newComponent2 = oldComponent2 * oldLengthReciprocal;
		final float newComponent3 = oldComponent3 * oldLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetOrthonormalBasis33FTransformNormalize(final float oldComponent1, final float oldComponent2, final float oldComponent3) {
		final float orthonormalBasisUX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 0];
		final float orthonormalBasisUY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 1];
		final float orthonormalBasisUZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_U + 2];
		final float orthonormalBasisVX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 0];
		final float orthonormalBasisVY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 1];
		final float orthonormalBasisVZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_V + 2];
		final float orthonormalBasisWX = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 0];
		final float orthonormalBasisWY = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 1];
		final float orthonormalBasisWZ = this.orthonormalBasis33FArray_$private$9[ORTHONORMAL_BASIS_3_3_F_ARRAY_OFFSET_W + 2];
		
		final float newComponent1 = orthonormalBasisUX * oldComponent1 + orthonormalBasisVX * oldComponent2 + orthonormalBasisWX * oldComponent3;
		final float newComponent2 = orthonormalBasisUY * oldComponent1 + orthonormalBasisVY * oldComponent2 + orthonormalBasisWY * oldComponent3;
		final float newComponent3 = orthonormalBasisUZ * oldComponent1 + orthonormalBasisVZ * oldComponent2 + orthonormalBasisWZ * oldComponent3;
		final float newLengthReciprocal = vector3FLengthReciprocal(newComponent1, newComponent2, newComponent3);
		final float newNormalizedComponent1 = newComponent1 * newLengthReciprocal;
		final float newNormalizedComponent2 = newComponent2 * newLengthReciprocal;
		final float newNormalizedComponent3 = newComponent3 * newLengthReciprocal;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = newNormalizedComponent1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = newNormalizedComponent2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = newNormalizedComponent3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F() {
		final float component1 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
		final float component2 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
		final float component3 = this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
		
		vector3FSetOrthonormalBasis33FTransformNormalize(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetReflection(final float directionX, final float directionY, final float directionZ, final float normalX, final float normalY, final float normalZ, final boolean isFacingSurface) {
		final float directionDotNormal = vector3FDotProduct(directionX, directionY, directionZ, normalX, normalY, normalZ);
		final float directionDotNormalMultipliedByTwo = directionDotNormal * 2.0F;
		
		if(isFacingSurface) {
			final float reflectionX = directionX - normalX * directionDotNormalMultipliedByTwo;
			final float reflectionY = directionY - normalY * directionDotNormalMultipliedByTwo;
			final float reflectionZ = directionZ - normalZ * directionDotNormalMultipliedByTwo;
			
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
		} else {
			final float reflectionX = normalX * directionDotNormalMultipliedByTwo - directionX;
			final float reflectionY = normalY * directionDotNormalMultipliedByTwo - directionY;
			final float reflectionZ = normalZ * directionDotNormalMultipliedByTwo - directionZ;
			
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = reflectionX;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = reflectionY;
			this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = reflectionZ;
		}
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetSampleHemisphereCosineDistribution2(final float u, final float v) {
		final float sinTheta = sqrt(v);
		final float cosTheta = sqrt(1.0F - v);
		final float phi = PI_MULTIPLIED_BY_2 * u;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
//	TODO: Add Javadocs!
	protected final void vector3FSetSampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1] = component1;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2] = component2;
		this.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3] = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int doNext(final int bits) {
		final int index = getGlobalId();
		
		final long oldSeed = this.seedArray[index];
		final long newSeed = (oldSeed * PRNG_MULTIPLIER + PRNG_ADDEND) & PRNG_MASK;
		
		this.seedArray[index] = newSeed;
		
		return (int)(newSeed >>> (48 - bits));
	}
	
	private void doSetupSeedArray() {
		put(this.seedArray = Longs.array(getResolution(), () -> ThreadLocalRandom.current().nextLong()));
	}
}