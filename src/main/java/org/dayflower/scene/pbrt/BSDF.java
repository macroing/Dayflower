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
package org.dayflower.scene.pbrt;

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.util.Lists;

//TODO: Add Javadocs!
public final class BSDF {
	private final Intersection intersection;
	private final List<BXDF> bXDFs;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BSDF(final Intersection intersection, final List<BXDF> bXDs, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = new ArrayList<>(Lists.requireNonNullList(bXDs, "bXDs"));
		this.eta = eta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples2} in PBRT
	 * @param samplesB a {@code List} of {@code Point2F} instances that represents samples, called {@code samples1} in PBRT
	 * @param hasReflection {@code true} if, and only if, reflection is accepted, {@code false} otherwise
	 * @param hasTransmission {@code true} if, and only if, transmission is accepted, {@code false} otherwise
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final List<Point2F> samplesB, final boolean hasReflection, final boolean hasTransmission) {
		Lists.requireNonNullList(samplesA, "samplesA");
		Lists.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(hasReflection && bXDF.getBXDFType().hasReflection() || hasTransmission && bXDF.getBXDFType().hasTransmission()) {
				reflectance = Color3F.add(reflectance, bXDF.computeReflectanceFunction(samplesA, samplesB));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code samplesA}, {@code outgoingWorldSpace} or an element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code rho(const Vector3f &woWorld, int nSamples, const Point2f *samples, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples} in PBRT
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @param hasReflection {@code true} if, and only if, reflection is accepted, {@code false} otherwise
	 * @param hasTransmission {@code true} if, and only if, transmission is accepted, {@code false} otherwise
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code outgoingWorldSpace} or an element in {@code samplesA} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final Vector3F outgoingWorldSpace, final boolean hasReflection, final boolean hasTransmission) {
		Lists.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		
		Color3F reflectance = Color3F.BLACK;
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		
		for(final BXDF bXDF : this.bXDFs) {
			if(hasReflection && bXDF.getBXDFType().hasReflection() || hasTransmission && bXDF.getBXDFType().hasTransmission()) {
				reflectance = Color3F.add(reflectance, bXDF.computeReflectanceFunction(samplesA, outgoing));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code f(const Vector3f &woW, const Vector3f &wiW, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoingWorldSpace the outgoing direction, called {@code woW} in PBRT
	 * @param incomingWorldSpace the incoming direction, called {@code wiW} in PBRT
	 * @param hasReflection {@code true} if, and only if, reflection is accepted, {@code false} otherwise
	 * @param hasTransmission {@code true} if, and only if, transmission is accepted, {@code false} otherwise
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	public Color3F evaluateDistributionFunction(final Vector3F outgoingWorldSpace, final Vector3F incomingWorldSpace, final boolean hasReflection, final boolean hasTransmission) {
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getSurfaceNormalG();
		
		final boolean isReflecting = Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) > 0.0F;
		
		Color3F result = Color3F.BLACK;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(hasReflection && bXDF.getBXDFType().hasReflection() && isReflecting || hasTransmission && bXDF.getBXDFType().hasTransmission() && !isReflecting) {
				result = Color3F.add(result, bXDF.evaluateDistributionFunction(outgoing, incoming));
			}
		}
		
		return result;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BXDFDistributionFunctionResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoingWorldSpace} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code Sample_f(const Vector3f &woWorld, Vector3f *wiWorld, const Point2f &u, Float *pdf, BxDFType type, BxDFType *sampledType)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @param sample the sample point
	 * @param hasReflection {@code true} if, and only if, reflection is accepted, {@code false} otherwise
	 * @param hasTransmission {@code true} if, and only if, transmission is accepted, {@code false} otherwise
	 * @return an optional {@code BXDFDistributionFunctionResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoingWorldSpace} or {@code sample} are {@code null}
	 */
	public Optional<BXDFDistributionFunctionResult> sampleDistributionFunction(final Vector3F outgoingWorldSpace, final Point2F sample, final boolean hasReflection, final boolean hasTransmission) {
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(sample, "sample == null");
		
		return Optional.empty();//TODO: Implement!
	}
	
	/**
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code Pdf(const Vector3f &woWorld, const Vector3f &wiWorld, BxDFType flags)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @param incomingWorldSpace the incoming direction, called {@code wiWorld} in PBRT
	 * @param hasReflection {@code true} if, and only if, reflection is accepted, {@code false} otherwise
	 * @param hasTransmission {@code true} if, and only if, transmission is accepted, {@code false} otherwise
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	public float evaluateProbabilityDensityFunction(final Vector3F outgoingWorldSpace, final Vector3F incomingWorldSpace, final boolean hasReflection, final boolean hasTransmission) {
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		if(this.bXDFs.size() == 0) {
			return 0.0F;
		}
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		if(equal(outgoing.getZ(), 0.0F)) {
			return 0.0F;
		}
		
		float probabilityDensityFunctionValue = 0.0F;
		
		int matches = 0;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(hasReflection && bXDF.getBXDFType().hasReflection() || hasTransmission && bXDF.getBXDFType().hasTransmission()) {
				matches++;
				
				probabilityDensityFunctionValue += bXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		return probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3F doTransformToLocalSpace(final Vector3F vector) {
		return Vector3F.transformReverse(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS());
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.transform(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS());
	}
}