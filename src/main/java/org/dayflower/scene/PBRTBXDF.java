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
package org.dayflower.scene;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code PBRTBXDF} represents a BRDF (Bidirectional Reflectance Distribution Function) or a BTDF (Bidirectional Transmittance Distribution Function).
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface PBRTBXDF {
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@code PBRTBXDFDistributionFunctionResult} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoing} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code f(const Vector3f &wo, const Vector3f &wi)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code PBRTBXDFDistributionFunctionResult} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code incoming} are {@code null}
	 */
	PBRTBXDFDistributionFunctionResult evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming);
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns a {@code PBRTBXDFDistributionFunctionResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &sample, Float *pdf, BxDFType *sampledType = nullptr)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point
	 * @return a {@code PBRTBXDFDistributionFunctionResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	PBRTBXDFDistributionFunctionResult sampleDistributionFunction(final Vector3F outgoing, final Point2F sample);
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@code PBRTBXDFReflectanceFunctionResult} with the result of the computation.
	 * <p>
	 * This method represents the {@code BxDF} method {@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samples the samples to compute
	 * @return a {@code PBRTBXDFReflectanceFunctionResult} with the result of the computation
	 */
	PBRTBXDFReflectanceFunctionResult computeReflectanceFunction(final int samples);
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@code PBRTBXDFReflectanceFunctionResult} with the result of the computation.
	 * <p>
	 * If {@code outgoing} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code rho(const Vector3f &wo, int nSamples, const Point2f *samples)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samples the samples to compute
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @return a {@code PBRTBXDFReflectanceFunctionResult} with the result of the computation
	 * @throws NullPointerException thrown if, and only if, {@code outgoing} is {@code null}
	 */
	PBRTBXDFReflectanceFunctionResult computeReflectanceFunction(final int samples, final Vector3F outgoing);
}