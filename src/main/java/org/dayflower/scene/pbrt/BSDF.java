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
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Ints.min;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.util.Lists;

/**
 * A {@code BSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BSDF {
	private final Intersection intersection;
	private final List<BXDF> bXDFs;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code bXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDFs a {@code List} of {@link BXDF} instances
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}
	 */
	public BSDF(final Intersection intersection, final List<BXDF> bXDFs) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = new ArrayList<>(Lists.requireNonNullList(bXDFs, "bXDFs"));
		this.eta = 1.0F;
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code bXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDFs a {@code List} of {@link BXDF} instances
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}
	 */
	public BSDF(final Intersection intersection, final List<BXDF> bXDFs, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = new ArrayList<>(Lists.requireNonNullList(bXDFs, "bXDFs"));
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
		
		Lists.requireNonNullList(samplesA, "samplesA");
		Lists.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
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
		
		Lists.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		
		Color3F reflectance = Color3F.BLACK;
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
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
	 * If either {@code bXDFType}, {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code f(const Vector3f &woW, const Vector3f &wiW, BxDFType flags)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction, called {@code woW} in PBRT
	 * @param incomingWorldSpace the incoming direction, called {@code wiW} in PBRT
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	public Color3F evaluateDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		final boolean isReflecting = Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) > 0.0F;
		
		Color3F result = Color3F.BLACK;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType) && (isReflecting && bXDF.getBXDFType().hasReflection() || !isReflecting && bXDF.getBXDFType().hasTransmission())) {
				result = Color3F.add(result, bXDF.evaluateDistributionFunction(outgoing, incoming));
			}
		}
		
		return result;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BSDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code bXDFType}, {@code outgoingWorldSpace} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code Sample_f(const Vector3f &woWorld, Vector3f *wiWorld, const Point2f &u, Float *pdf, BxDFType type, BxDFType *sampledType)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @param sample the sample point
	 * @return an optional {@code BSDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace} or {@code sample} are {@code null}
	 */
	public Optional<BSDFResult> sampleDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Point2F sample) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final int matches = doComputeMatches(bXDFType);
		
		if(matches == 0) {
			return Optional.empty();
		}
		
		final int match = min((int)(floor(sample.getU() * matches)), matches - 1);
		
		final BXDF matchingBXDF = doGetMatchingBXDF(bXDFType, match);
		
		if(matchingBXDF == null) {
			return Optional.empty();
		}
		
		final Point2F sampleRemapped = new Point2F(min(sample.getU() * matches - match, 0.99999994F), sample.getV());
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		
		if(equal(outgoing.getZ(), 0.0F)) {
			return Optional.empty();
		}
		
		final Optional<BXDFResult> optionalBXDFResult = matchingBXDF.sampleDistributionFunction(outgoing, sampleRemapped);
		
		if(!optionalBXDFResult.isPresent()) {
			return Optional.empty();
		}
		
		final BXDFResult bXDFResult = optionalBXDFResult.get();
		
		final Vector3F incoming = bXDFResult.getIncoming();
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incoming);
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		Color3F result = bXDFResult.getResult();
		
		float probabilityDensityFunctionValue = bXDFResult.getProbabilityDensityFunctionValue();
		
		if(matches > 1 && !matchingBXDF.getBXDFType().isSpecular()) {
			for(final BXDF bXDF : this.bXDFs) {
				if(matchingBXDF != bXDF && bXDF.getBXDFType().matches(bXDFType)) {
					probabilityDensityFunctionValue += bXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
				}
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		if(!matchingBXDF.getBXDFType().isSpecular()) {
			final boolean isReflecting = Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) > 0.0F;
			
			result = Color3F.BLACK;
			
			for(final BXDF bXDF : this.bXDFs) {
				if(bXDF.getBXDFType().matches(bXDFType) && (isReflecting && bXDF.getBXDFType().hasReflection() || !isReflecting && bXDF.getBXDFType().hasTransmission())) {
					result = Color3F.add(result, bXDF.evaluateDistributionFunction(outgoing, incoming));
				}
			}
		}
		
		return Optional.of(new BSDFResult(bXDFResult.getBXDFType(), result, incomingWorldSpace, outgoingWorldSpace, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BSDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code BSDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new BSDF(%s, %s, %+.10f)", this.intersection, "...", Float.valueOf(this.eta));
	}
	
	/**
	 * Compares {@code object} to this {@code BSDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BSDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BSDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BSDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BSDF)) {
			return false;
		} else if(!Objects.equals(this.intersection, BSDF.class.cast(object).intersection)) {
			return false;
		} else if(!Objects.equals(this.bXDFs, BSDF.class.cast(object).bXDFs)) {
			return false;
		} else if(!equal(this.eta, BSDF.class.cast(object).eta)) {
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
	 * If either {@code bXDFType}, {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BSDF} method {@code Pdf(const Vector3f &woWorld, const Vector3f &wiWorld, BxDFType flags)} that returns a {@code Float} in PBRT.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param outgoingWorldSpace the outgoing direction, called {@code woWorld} in PBRT
	 * @param incomingWorldSpace the incoming direction, called {@code wiWorld} in PBRT
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code outgoingWorldSpace} or {@code incomingWorldSpace} are {@code null}
	 */
	public float evaluateProbabilityDensityFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
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
			if(bXDF.getBXDFType().matches(bXDFType)) {
				matches++;
				
				probabilityDensityFunctionValue += bXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
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
	public float getEta() {
		return this.eta;
	}
	
	/**
	 * Returns the {@link BXDF} count by specular or non-specular type.
	 * 
	 * @param isSpecular {@code true} if, and only if, specular types should be counted, {@code false} otherwise
	 * @return the {@code BXDF} count by specular or non-specular type
	 */
	public int countBXDFsBySpecularType(final boolean isSpecular) {
		int count = 0;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().isSpecular() == isSpecular) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Returns a hash code for this {@code BSDF} instance.
	 * 
	 * @return a hash code for this {@code BSDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.intersection, this.bXDFs, Float.valueOf(this.eta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BXDF doGetMatchingBXDF(final BXDFType bXDFType, final int match) {
		for(int i = 0, j = match; i < this.bXDFs.size(); i++) {
			final BXDF bXDF = this.bXDFs.get(i);
			
			if(bXDF.getBXDFType().matches(bXDFType) && j-- == 0) {
				return bXDF;
			}
		}
		
		return null;
	}
	
	private Vector3F doTransformToLocalSpace(final Vector3F vector) {
		return Vector3F.transformReverse(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS());
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.transform(vector, this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisS());
	}
	
	private int doComputeMatches(final BXDFType bXDFType) {
		int matches = 0;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
				matches++;
			}
		}
		
		return matches;
	}
}