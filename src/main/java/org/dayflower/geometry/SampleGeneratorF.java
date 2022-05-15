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
package org.dayflower.geometry;

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.random;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.sqrt;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * The class {@code SampleGeneratorF} contains methods to generate {@code float}-based samples.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SampleGeneratorF {
	private SampleGeneratorF() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleDiskUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleDiskUniformDistribution() {
		return sampleDiskUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleDiskUniformDistribution(final float u, final float v) {
		final float r = sqrt(u);
		final float theta = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = r * cos(theta);
		final float component2 = r * sin(theta);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleDiskUniformDistributionByConcentricMapping(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleDiskUniformDistributionByConcentricMapping() {
		return sampleDiskUniformDistributionByConcentricMapping(random(), random());
	}
	
	/**
	 * Samples a point on disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleDiskUniformDistributionByConcentricMapping(u, v, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleDiskUniformDistributionByConcentricMapping(final float u, final float v) {
		return sampleDiskUniformDistributionByConcentricMapping(u, v, 1.0F);
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param radius the radius of the disk
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleDiskUniformDistributionByConcentricMapping(final float u, final float v, final float radius) {
		if(isZero(u) && isZero(v)) {
			return new Point2F();
		}
		
		final float a = u * 2.0F - 1.0F;
		final float b = v * 2.0F - 1.0F;
		
		if(a * a > b * b) {
			final float phi = PI_DIVIDED_BY_4 * (b / a);
			final float r = radius * a;
			
			final float component1 = r * cos(phi);
			final float component2 = r * sin(phi);
			
			return new Point2F(component1, component2);
		}
		
		final float phi = PI_DIVIDED_BY_2 - PI_DIVIDED_BY_4 * (a / b);
		final float r = radius * b;
		
		final float component1 = r * cos(phi);
		final float component2 = r * sin(phi);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Samples a point using an exact inverse tent filter.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleExactInverseTentFilter(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleExactInverseTentFilter() {
		return sampleExactInverseTentFilter(random(), random());
	}
	
	/**
	 * Samples a point using an exact inverse tent filter.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point2F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2F sampleExactInverseTentFilter(final float u, final float v) {
		final float x = u * 2.0F;
		final float y = v * 2.0F;
		
		final float component1 = x < 1.0F ? sqrt(x) - 1.0F : 1.0F - sqrt(2.0F - x);
		final float component2 = y < 1.0F ? sqrt(y) - 1.0F : 1.0F - sqrt(2.0F - y);
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Samples a point on a triangle with a uniform distribution.
	 * <p>
	 * Returns a {@link Point3F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleTriangleUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point3F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point3F sampleTriangleUniformDistribution() {
		return sampleTriangleUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a point on a triangle with a uniform distribution.
	 * <p>
	 * Returns a {@link Point3F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point3F} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point3F sampleTriangleUniformDistribution(final float u, final float v) {
		final float sqrtU = sqrt(u);
		
		final float component1 = 1.0F - sqrtU;
		final float component2 = v * sqrtU;
		final float component3 = 1.0F - component1 - component2;
		
		return new Point3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a cone with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param cosThetaMax the maximum cos theta value
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleConeUniformDistribution(final float u, final float v, final float cosThetaMax) {
		final float cosTheta = (1.0F - u) + u * cosThetaMax;
		final float sinTheta = sqrt(1.0F - cosTheta * cosTheta);
		final float phi = v * PI_MULTIPLIED_BY_2;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleHemisphereCosineDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereCosineDistribution() {
		return sampleHemisphereCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereCosineDistribution(final float u, final float v) {
		final Point2F point = sampleDiskUniformDistributionByConcentricMapping(u, v);
		
		final float component1 = point.x;
		final float component2 = point.y;
		final float component3 = sqrt(max(0.0F, 1.0F - component1 * component1 - component2 * component2));
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleHemisphereCosineDistribution2(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereCosineDistribution2() {
		return sampleHemisphereCosineDistribution2(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereCosineDistribution2(final float u, final float v) {
		final float sinTheta = sqrt(v);
		final float cosTheta = sqrt(1.0F - v);
		final float phi = PI_MULTIPLIED_BY_2 * u;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleHemispherePowerCosineDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemispherePowerCosineDistribution() {
		return sampleHemispherePowerCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleHemispherePowerCosineDistribution(u, v, 20.0F);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v) {
		return sampleHemispherePowerCosineDistribution(u, v, 20.0F);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleHemisphereUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereUniformDistribution() {
		return sampleHemisphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleHemisphereUniformDistribution(final float u, final float v) {
		final float cosTheta = u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorF.sampleSphereUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleSphereUniformDistribution() {
		return sampleSphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3F sampleSphereUniformDistribution(final float u, final float v) {
		final float cosTheta = 1.0F - 2.0F * u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = v * PI_MULTIPLIED_BY_2;
		
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Returns the probability density function (PDF) value for {@code cosThetaMax}.
	 * <p>
	 * This method is used together with {@link #sampleConeUniformDistribution(float, float, float)}.
	 * 
	 * @param cosThetaMax the maximum cos theta value
	 * @return the probability density function (PDF) value for {@code cosThetaMax}
	 */
//	TODO: Add Unit Tests!
	public static float coneUniformDistributionProbabilityDensityFunction(final float cosThetaMax) {
		return 1.0F / (2.0F * PI * (1.0F - cosThetaMax));
	}
	
	/**
	 * Returns the probability density function (PDF) value for {@code cosTheta}.
	 * <p>
	 * This method is used together with {@link #sampleHemisphereCosineDistribution(float, float)}.
	 * 
	 * @param cosTheta the cos theta value
	 * @return the probability density function (PDF) value for {@code cosTheta}
	 */
//	TODO: Add Unit Tests!
	public static float hemisphereCosineDistributionProbabilityDensityFunction(final float cosTheta) {
		return cosTheta * PI_RECIPROCAL;
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * This method is used together with {@link #sampleHemisphereUniformDistribution(float, float)}.
	 * 
	 * @return the probability density function (PDF) value
	 */
//	TODO: Add Unit Tests!
	public static float hemisphereUniformDistributionProbabilityDensityFunction() {
		return PI_MULTIPLIED_BY_2_RECIPROCAL;
	}
	
	/**
	 * Computes the balance heuristic for multiple importance sampling (MIS).
	 * <p>
	 * Returns the result of the computation.
	 * 
	 * @param probabilityDensityFunctionValueA a probability density function (PDF) value
	 * @param probabilityDensityFunctionValueB a probability density function (PDF) value
	 * @param sampleCountA a sample count
	 * @param sampleCountB a sample count
	 * @return the result of the computation
	 */
//	TODO: Add Unit Tests!
	public static float multipleImportanceSamplingBalanceHeuristic(final float probabilityDensityFunctionValueA, final float probabilityDensityFunctionValueB, final int sampleCountA, final int sampleCountB) {
		return sampleCountA * probabilityDensityFunctionValueA / (sampleCountA * probabilityDensityFunctionValueA + sampleCountB * probabilityDensityFunctionValueB);
	}
	
	/**
	 * Computes the power heuristic for multiple importance sampling (MIS).
	 * <p>
	 * Returns the result of the computation.
	 * 
	 * @param probabilityDensityFunctionValueA a probability density function (PDF) value
	 * @param probabilityDensityFunctionValueB a probability density function (PDF) value
	 * @param sampleCountA a sample count
	 * @param sampleCountB a sample count
	 * @return the result of the computation
	 */
//	TODO: Add Unit Tests!
	public static float multipleImportanceSamplingPowerHeuristic(final float probabilityDensityFunctionValueA, final float probabilityDensityFunctionValueB, final int sampleCountA, final int sampleCountB) {
		final float weightA = sampleCountA * probabilityDensityFunctionValueA;
		final float weightB = sampleCountB * probabilityDensityFunctionValueB;
		
		return weightA * weightA / (weightA * weightA + weightB * weightB);
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * This method is used together with {@link #sampleSphereUniformDistribution(float, float)}.
	 * 
	 * @return the probability density function (PDF) value
	 */
//	TODO: Add Unit Tests!
	public static float sphereUniformDistributionProbabilityDensityFunction() {
		return PI_MULTIPLIED_BY_4_RECIPROCAL;
	}
}