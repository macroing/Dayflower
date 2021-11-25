/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Doubles.PI;
import static org.dayflower.utility.Doubles.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Doubles.PI_DIVIDED_BY_4;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_4_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.random;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * The class {@code SampleGeneratorD} contains methods to generate {@code double}-based samples.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SampleGeneratorD {
	private SampleGeneratorD() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleDiskUniformDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleDiskUniformDistribution() {
		return sampleDiskUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleDiskUniformDistribution(final double u, final double v) {
		final double r = sqrt(u);
		final double theta = PI_MULTIPLIED_BY_2 * v;
		
		final double component1 = r * cos(theta);
		final double component2 = r * sin(theta);
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleDiskUniformDistributionByConcentricMapping(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleDiskUniformDistributionByConcentricMapping() {
		return sampleDiskUniformDistributionByConcentricMapping(random(), random());
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleDiskUniformDistributionByConcentricMapping(u, v, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleDiskUniformDistributionByConcentricMapping(final double u, final double v) {
		return sampleDiskUniformDistributionByConcentricMapping(u, v, 1.0D);
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution using concentric mapping.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param radius the radius of the disk
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleDiskUniformDistributionByConcentricMapping(final double u, final double v, final double radius) {
		if(isZero(u) && isZero(v)) {
			return new Point2D();
		}
		
		final double a = u * 2.0D - 1.0D;
		final double b = v * 2.0D - 1.0D;
		
		if(a * a > b * b) {
			final double phi = PI_DIVIDED_BY_4 * (b / a);
			final double r = radius * a;
			
			final double component1 = r * cos(phi);
			final double component2 = r * sin(phi);
			
			return new Point2D(component1, component2);
		}
		
		final double phi = PI_DIVIDED_BY_2 - PI_DIVIDED_BY_4 * (a / b);
		final double r = radius * b;
		
		final double component1 = r * cos(phi);
		final double component2 = r * sin(phi);
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Samples a point using an exact inverse tent filter.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleExactInverseTentFilter(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleExactInverseTentFilter() {
		return sampleExactInverseTentFilter(random(), random());
	}
	
	/**
	 * Samples a point using an exact inverse tent filter.
	 * <p>
	 * Returns a {@code Point2D} instance with the sampled point.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Point2D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point2D sampleExactInverseTentFilter(final double u, final double v) {
		final double x = u * 2.0D;
		final double y = v * 2.0D;
		
		final double component1 = x < 1.0D ? sqrt(x) - 1.0D : 1.0D - sqrt(2.0D - x);
		final double component2 = y < 1.0D ? sqrt(y) - 1.0D : 1.0D - sqrt(2.0D - y);
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Samples a point on a triangle with a uniform distribution.
	 * <p>
	 * Returns a {@link Point3D} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleTriangleUniformDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point3D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point3D sampleTriangleUniformDistribution() {
		return sampleTriangleUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a point on a triangle with a uniform distribution.
	 * <p>
	 * Returns a {@link Point3D} instance with the sampled point.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Point3D} instance with the sampled point
	 */
//	TODO: Add Unit Tests!
	public static Point3D sampleTriangleUniformDistribution(final double u, final double v) {
		final double sqrtU = sqrt(u);
		
		final double component1 = 1.0D - sqrtU;
		final double component2 = v * sqrtU;
		final double component3 = 1.0D - component1 - component2;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a cone with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param cosThetaMax the maximum cos theta value
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleConeUniformDistribution(final double u, final double v, final double cosThetaMax) {
		final double cosTheta = (1.0D - u) + u * cosThetaMax;
		final double sinTheta = sqrt(1.0D - cosTheta * cosTheta);
		final double phi = v * PI_MULTIPLIED_BY_2;
		
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleHemisphereCosineDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereCosineDistribution() {
		return sampleHemisphereCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereCosineDistribution(final double u, final double v) {
		final Point2D point = sampleDiskUniformDistributionByConcentricMapping(u, v);
		
		final double component1 = point.getComponent1();
		final double component2 = point.getComponent2();
		final double component3 = sqrt(max(0.0D, 1.0D - component1 * component1 - component2 * component2));
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleHemisphereCosineDistribution2(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereCosineDistribution2() {
		return sampleHemisphereCosineDistribution2(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereCosineDistribution2(final double u, final double v) {
		final double sinTheta = sqrt(v);
		final double cosTheta = sqrt(1.0D - v);
		final double phi = PI_MULTIPLIED_BY_2 * u;
		
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleHemispherePowerCosineDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemispherePowerCosineDistribution() {
		return sampleHemispherePowerCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleHemispherePowerCosineDistribution(u, v, 20.0D);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemispherePowerCosineDistribution(final double u, final double v) {
		return sampleHemispherePowerCosineDistribution(u, v, 20.0D);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param exponent the exponent to use
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemispherePowerCosineDistribution(final double u, final double v, final double exponent) {
		final double cosTheta = pow(1.0D - u, 1.0D / (exponent + 1.0D));
		final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
		final double phi = PI_MULTIPLIED_BY_2 * v;
		
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleHemisphereUniformDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereUniformDistribution() {
		return sampleHemisphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleHemisphereUniformDistribution(final double u, final double v) {
		final double cosTheta = u;
		final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
		final double phi = PI_MULTIPLIED_BY_2 * v;
		
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SampleGeneratorD.sampleSphereUniformDistribution(Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleSphereUniformDistribution() {
		return sampleSphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3D} instance with the sampled direction.
	 * 
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return a {@code Vector3D} instance with the sampled direction
	 */
//	TODO: Add Unit Tests!
	public static Vector3D sampleSphereUniformDistribution(final double u, final double v) {
		final double cosTheta = 1.0D - 2.0D * u;
		final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
		final double phi = v * PI_MULTIPLIED_BY_2;
		
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Returns the probability density function (PDF) value for {@code cosThetaMax}.
	 * <p>
	 * This method is used together with {@link #sampleConeUniformDistribution(double, double, double)}.
	 * 
	 * @param cosThetaMax the maximum cos theta value
	 * @return the probability density function (PDF) value for {@code cosThetaMax}
	 */
//	TODO: Add Unit Tests!
	public static double coneUniformDistributionProbabilityDensityFunction(final double cosThetaMax) {
		return 1.0D / (2.0D * PI * (1.0D - cosThetaMax));
	}
	
	/**
	 * Returns the probability density function (PDF) value for {@code cosTheta}.
	 * <p>
	 * This method is used together with {@link #sampleHemisphereCosineDistribution(double, double)}.
	 * 
	 * @param cosTheta the cos theta value
	 * @return the probability density function (PDF) value for {@code cosTheta}
	 */
//	TODO: Add Unit Tests!
	public static double hemisphereCosineDistributionProbabilityDensityFunction(final double cosTheta) {
		return cosTheta * PI_RECIPROCAL;
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * This method is used together with {@link #sampleHemisphereUniformDistribution(double, double)}.
	 * 
	 * @return the probability density function (PDF) value
	 */
//	TODO: Add Unit Tests!
	public static double hemisphereUniformDistributionProbabilityDensityFunction() {
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
	public static double multipleImportanceSamplingBalanceHeuristic(final double probabilityDensityFunctionValueA, final double probabilityDensityFunctionValueB, final int sampleCountA, final int sampleCountB) {
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
	public static double multipleImportanceSamplingPowerHeuristic(final double probabilityDensityFunctionValueA, final double probabilityDensityFunctionValueB, final int sampleCountA, final int sampleCountB) {
		final double weightA = sampleCountA * probabilityDensityFunctionValueA;
		final double weightB = sampleCountB * probabilityDensityFunctionValueB;
		
		return weightA * weightA / (weightA * weightA + weightB * weightB);
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * This method is used together with {@link #sampleSphereUniformDistribution(double, double)}.
	 * 
	 * @return the probability density function (PDF) value
	 */
//	TODO: Add Unit Tests!
	public static double sphereUniformDistributionProbabilityDensityFunction() {
		return PI_MULTIPLIED_BY_4_RECIPROCAL;
	}
}