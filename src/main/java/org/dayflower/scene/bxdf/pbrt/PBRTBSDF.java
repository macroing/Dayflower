/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.scene.bxdf.pbrt;

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Ints.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Intersection;
import org.dayflower.util.ParameterArguments;

/**
 * A {@code PBRTBSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PBRTBSDF implements BSDF {
	private final Intersection intersection;
	private final List<PBRTBXDF> pBRTBXDFs;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code pBRTBXDFs} or at least one element in {@code pBRTBXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code pBRTBXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param pBRTBXDFs a {@code List} of {@link PBRTBXDF} instances
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code pBRTBXDFs} or at least one element in {@code pBRTBXDFs} are {@code null}
	 */
	public PBRTBSDF(final Intersection intersection, final List<PBRTBXDF> pBRTBXDFs) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.pBRTBXDFs = new ArrayList<>(ParameterArguments.requireNonNullList(pBRTBXDFs, "pBRTBXDFs"));
		this.eta = 1.0F;
	}
	
	/**
	 * Constructs a new {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code pBRTBXDFs} or at least one element in {@code pBRTBXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code pBRTBXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param pBRTBXDFs a {@code List} of {@link PBRTBXDF} instances
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code pBRTBXDFs} or at least one element in {@code pBRTBXDFs} are {@code null}
	 */
	public PBRTBSDF(final Intersection intersection, final List<PBRTBXDF> pBRTBXDFs, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.pBRTBXDFs = new ArrayList<>(ParameterArguments.requireNonNullList(pBRTBXDFs, "pBRTBXDFs"));
		this.eta = eta;
	}
	
	/**
	 * Constructs a new {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code pBRTBXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param pBRTBXDF a {@link PBRTBXDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code pBRTBXDF} are {@code null}
	 */
	public PBRTBSDF(final Intersection intersection, final PBRTBXDF pBRTBXDF) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.pBRTBXDFs = Arrays.asList(Objects.requireNonNull(pBRTBXDF, "pBRTBXDF == null"));
		this.eta = 1.0F;
	}
	
	/**
	 * Constructs a new {@code PBRTBSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code pBRTBXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param pBRTBXDF a {@link PBRTBXDF} instance
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code pBRTBXDF} are {@code null}
	 */
	public PBRTBSDF(final Intersection intersection, final PBRTBXDF pBRTBXDF, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.pBRTBXDFs = Arrays.asList(Objects.requireNonNull(pBRTBXDF, "pBRTBXDF == null"));
		this.eta = eta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code bXDFType}, {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples2} in PBRT
	 * @param samplesB a {@code List} of {@code Point2F} instances that represents samples, called {@code samples1} in PBRT
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final BXDFType bXDFType, final List<Point2F> samplesA, final List<Point2F> samplesB) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().matches(bXDFType)) {
				reflectance = Color3F.add(reflectance, pBRTBXDF.computeReflectanceFunction(samplesA, samplesB));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code bXDFType}, {@code samplesA}, {@code outgoingWorldSpace} or an element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code rho(const Vector3f &woWorld, int nSamples, const Point2f *samples, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples} in PBRT
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code samplesA}, {@code outgoingWorldSpace} or an element in {@code samplesA} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final BXDFType bXDFType, final List<Point2F> samplesA, final Vector3F outgoingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		
		Color3F reflectance = Color3F.BLACK;
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().matches(bXDFType)) {
				reflectance = Color3F.add(reflectance, pBRTBXDF.computeReflectanceFunction(samplesA, outgoing));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(normalWorldSpace, "normalWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		final boolean isReflecting = Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) > 0.0F;
		
		Color3F result = Color3F.BLACK;
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().matches(bXDFType) && (isReflecting && pBRTBXDF.getBXDFType().hasReflection() || !isReflecting && pBRTBXDF.getBXDFType().hasTransmission())) {
				result = Color3F.add(result, pBRTBXDF.evaluateDistributionFunction(outgoing, incoming));
			}
		}
		
		return result;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BSDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param sample the sample point
	 * @return an optional {@code BSDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<BSDFResult> sampleDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Point2F sample) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(normalWorldSpace, "normalWorldSpace == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final int matches = doComputeMatches(bXDFType);
		
		if(matches == 0) {
			return Optional.empty();
		}
		
		final int match = min((int)(floor(sample.getU() * matches)), matches - 1);
		
		final PBRTBXDF matchingPBRTBXDF = doGetMatchingPBRTBXDF(bXDFType, match);
		
		if(matchingPBRTBXDF == null) {
			return Optional.empty();
		}
		
		final Point2F sampleRemapped = new Point2F(min(sample.getU() * matches - match, 0.99999994F), sample.getV());
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		
		if(isZero(outgoing.getZ())) {
			return Optional.empty();
		}
		
		final Optional<BXDFResult> optionalBXDFResult = matchingPBRTBXDF.sampleDistributionFunction(outgoing, sampleRemapped);
		
		if(!optionalBXDFResult.isPresent()) {
			return Optional.empty();
		}
		
		final BXDFResult bXDFResult = optionalBXDFResult.get();
		
		final Vector3F incoming = bXDFResult.getIncoming();
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incoming);
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		Color3F result = bXDFResult.getResult();
		
		float probabilityDensityFunctionValue = bXDFResult.getProbabilityDensityFunctionValue();
		
		if(matches > 1 && !matchingPBRTBXDF.getBXDFType().isSpecular()) {
			for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
				if(matchingPBRTBXDF != pBRTBXDF && pBRTBXDF.getBXDFType().matches(bXDFType)) {
					probabilityDensityFunctionValue += pBRTBXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
				}
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		if(!matchingPBRTBXDF.getBXDFType().isSpecular()) {
			final boolean isReflecting = Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) > 0.0F;
			
			result = Color3F.BLACK;
			
			for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
				if(pBRTBXDF.getBXDFType().matches(bXDFType) && (isReflecting && pBRTBXDF.getBXDFType().hasReflection() || !isReflecting && pBRTBXDF.getBXDFType().hasTransmission())) {
					result = Color3F.add(result, pBRTBXDF.evaluateDistributionFunction(outgoing, incoming));
				}
			}
		}
		
		return Optional.of(new BSDFResult(bXDFResult.getBXDFType(), result, incomingWorldSpace, outgoingWorldSpace, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTBSDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTBSDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new PBRTBSDF(%s, %s, %+.10f)", this.intersection, "...", Float.valueOf(this.eta));
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTBSDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTBSDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTBSDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTBSDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTBSDF)) {
			return false;
		} else if(!Objects.equals(this.intersection, PBRTBSDF.class.cast(object).intersection)) {
			return false;
		} else if(!Objects.equals(this.pBRTBXDFs, PBRTBSDF.class.cast(object).pBRTBXDFs)) {
			return false;
		} else if(!equal(this.eta, PBRTBSDF.class.cast(object).eta)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction
	 * @param normalWorldSpace the normal
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace}, {@code normalWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(normalWorldSpace, "normalWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		if(this.pBRTBXDFs.size() == 0) {
			return 0.0F;
		}
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		if(isZero(outgoing.getZ())) {
			return 0.0F;
		}
		
		float probabilityDensityFunctionValue = 0.0F;
		
		int matches = 0;
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().matches(bXDFType)) {
				matches++;
				
				probabilityDensityFunctionValue += pBRTBXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the index of refraction (IOR).
	 * 
	 * @return the index of refraction (IOR)
	 */
	@Override
	public float getEta() {
		return this.eta;
	}
	
	/**
	 * Returns the {@link PBRTBXDF} count by specular or non-specular type.
	 * 
	 * @param isSpecular {@code true} if, and only if, specular types should be counted, {@code false} otherwise
	 * @return the {@code PBRTBXDF} count by specular or non-specular type
	 */
	@Override
	public int countBXDFsBySpecularType(final boolean isSpecular) {
		int count = 0;
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().isSpecular() == isSpecular) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Returns a hash code for this {@code PBRTBSDF} instance.
	 * 
	 * @return a hash code for this {@code PBRTBSDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.intersection, this.pBRTBXDFs, Float.valueOf(this.eta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PBRTBXDF doGetMatchingPBRTBXDF(final BXDFType bXDFType, final int match) {
		for(int i = 0, j = match; i < this.pBRTBXDFs.size(); i++) {
			final PBRTBXDF pBRTBXDF = this.pBRTBXDFs.get(i);
			
			if(pBRTBXDF.getBXDFType().matches(bXDFType) && j-- == 0) {
				return pBRTBXDF;
			}
		}
		
		return null;
	}
	
	private Vector3F doTransformToLocalSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transformReverse(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS()));
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS()));
	}
	
	private int doComputeMatches(final BXDFType bXDFType) {
		int matches = 0;
		
		for(final PBRTBXDF pBRTBXDF : this.pBRTBXDFs) {
			if(pBRTBXDF.getBXDFType().matches(bXDFType)) {
				matches++;
			}
		}
		
		return matches;
	}
}