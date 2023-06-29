/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.min;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.Color3F;
import org.macroing.java.lang.Floats;

/**
 * A {@code BSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BSDF {
	private final Intersection intersection;
	private final List<BXDF> bXDFs;
	private final Vector3F normalLocalSpace;
	private final Vector3F normalWorldSpace;
	private final Vector3F outgoingLocalSpace;
	private final Vector3F outgoingWorldSpace;
	private final boolean isNegatingIncoming;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code bXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDF(intersection, bXDF, false);
	 * }
	 * </pre>
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDF a {@link BXDF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code bXDF} are {@code null}
	 */
	public BSDF(final Intersection intersection, final BXDF bXDF) {
		this(intersection, bXDF, false);
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code bXDF} or {@code outgoing} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDF a {@link BXDF} instance
	 * @param outgoing the outgoing direction
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDF} or {@code outgoing} are {@code null}
	 */
	public BSDF(final Intersection intersection, final BXDF bXDF, final Vector3F outgoing) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = Arrays.asList(Objects.requireNonNull(bXDF, "bXDF == null"));
		this.normalWorldSpace = intersection.getSurfaceNormalS();
		this.normalLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.normalWorldSpace, intersection.getOrthonormalBasisS()));
		this.outgoingWorldSpace = Objects.requireNonNull(outgoing, "outgoing == null");
		this.outgoingLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.outgoingWorldSpace, intersection.getOrthonormalBasisS()));
		this.isNegatingIncoming = false;
		this.eta = 1.0F;
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code bXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDF(intersection, bXDF, isNegatingIncoming, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDF a {@link BXDF} instance
	 * @param isNegatingIncoming {@code true} if, and only if, the incoming direction should be negated, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code bXDF} are {@code null}
	 */
	public BSDF(final Intersection intersection, final BXDF bXDF, final boolean isNegatingIncoming) {
		this(intersection, bXDF, isNegatingIncoming, 1.0F);
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection} or {@code bXDF} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDF a {@link BXDF} instance
	 * @param isNegatingIncoming {@code true} if, and only if, the incoming direction should be negated, {@code false} otherwise
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code bXDF} are {@code null}
	 */
	public BSDF(final Intersection intersection, final BXDF bXDF, final boolean isNegatingIncoming, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = Arrays.asList(Objects.requireNonNull(bXDF, "bXDF == null"));
		this.normalWorldSpace = intersection.getSurfaceNormalS();
		this.normalLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.normalWorldSpace, intersection.getOrthonormalBasisS()));
		this.outgoingWorldSpace = Vector3F.negate(intersection.getRay().getDirection());
		this.outgoingLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.outgoingWorldSpace, intersection.getOrthonormalBasisS()));
		this.isNegatingIncoming = isNegatingIncoming;
		this.eta = eta;
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code bXDFs} will be copied.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDF(intersection, bXDFs, false);
	 * }
	 * </pre>
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDFs a {@code List} of {@link BXDF} instances
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}
	 */
	public BSDF(final Intersection intersection, final List<BXDF> bXDFs) {
		this(intersection, bXDFs, false);
	}
	
	/**
	 * Constructs a new {@code BSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code bXDFs} will be copied.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BSDF(intersection, bXDFs, isNegatingIncoming, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param bXDFs a {@code List} of {@link BXDF} instances
	 * @param isNegatingIncoming {@code true} if, and only if, the incoming direction should be negated, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}
	 */
	public BSDF(final Intersection intersection, final List<BXDF> bXDFs, final boolean isNegatingIncoming) {
		this(intersection, bXDFs, isNegatingIncoming, 1.0F);
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
	 * @param isNegatingIncoming {@code true} if, and only if, the incoming direction should be negated, {@code false} otherwise
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code bXDFs} or at least one element in {@code bXDFs} are {@code null}
	 */
	public BSDF(final Intersection intersection, final List<BXDF> bXDFs, final boolean isNegatingIncoming, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.bXDFs = new ArrayList<>(ParameterArguments.requireNonNullList(bXDFs, "bXDFs"));
		this.normalWorldSpace = intersection.getSurfaceNormalS();
		this.normalLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.normalWorldSpace, intersection.getOrthonormalBasisS()));
		this.outgoingWorldSpace = Vector3F.negate(intersection.getRay().getDirection());
		this.outgoingLocalSpace = Vector3F.normalize(Vector3F.transformReverse(this.outgoingWorldSpace, intersection.getOrthonormalBasisS()));
		this.isNegatingIncoming = isNegatingIncoming;
		this.eta = eta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code bXDFType}, {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples
	 * @param samplesB a {@code List} of {@code Point2F} instances that represents samples
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final BXDFType bXDFType, final List<Point2F> samplesA, final List<Point2F> samplesB) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		final Vector3F normal = this.normalLocalSpace;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
				reflectance = Color3F.add(reflectance, bXDF.computeReflectanceFunction(samplesA, samplesB, normal));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code bXDFType}, {@code samplesA} or an element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType}, {@code samplesA} or an element in {@code samplesA} are {@code null}
	 */
	public Color3F computeReflectanceFunction(final BXDFType bXDFType, final List<Point2F> samplesA) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Color3F reflectance = Color3F.BLACK;
		
		final Vector3F outgoing = this.outgoingLocalSpace;
		final Vector3F normal = this.normalLocalSpace;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
				reflectance = Color3F.add(reflectance, bXDF.computeReflectanceFunction(samplesA, outgoing, normal));
			}
		}
		
		return reflectance;
	}
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code bXDFType} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType} or {@code incomingWorldSpace} are {@code null}
	 */
	public Color3F evaluateDistributionFunction(final BXDFType bXDFType, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		final Vector3F incomingWorldSpaceCorrectlyOriented = this.isNegatingIncoming ? Vector3F.negate(incomingWorldSpace) : incomingWorldSpace;
		final Vector3F outgoing = this.outgoingLocalSpace;
		final Vector3F normal = this.normalLocalSpace;
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpaceCorrectlyOriented);
		
		final Vector3F surfaceNormalG = this.intersection.getSurfaceNormalGCorrectlyOriented();
		
		final float iDotN = Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG);
		final float oDotN = Vector3F.dotProduct(this.outgoingWorldSpace, surfaceNormalG);
		
		final boolean isReflecting = iDotN * oDotN > 0.0F;
		
		Color3F result = Color3F.BLACK;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType) && (isReflecting && bXDF.getBXDFType().hasReflection() || !isReflecting && bXDF.getBXDFType().hasTransmission())) {
				result = Color3F.add(result, bXDF.evaluateDistributionFunction(outgoing, normal, incoming));
			}
		}
		
		return result;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BSDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code bXDFType} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param sample the sample point
	 * @return an optional {@code BSDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType} or {@code sample} are {@code null}
	 */
	public Optional<BSDFResult> sampleDistributionFunction(final BXDFType bXDFType, final Point2F sample) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F outgoing = this.outgoingLocalSpace;
		final Vector3F normal = this.normalLocalSpace;
		
		if(Floats.isZero(outgoing.z)) {
			return Optional.empty();
		}
		
		final BXDF[] matchingBXDFs = new BXDF[8];
		
		final int matches = doComputeMatches(bXDFType, matchingBXDFs);
		
		if(matches == 0) {
			return Optional.empty();
		}
		
		final int match = min((int)(Floats.floor(sample.x * matches)), matches - 1);
		
		final BXDF matchingBXDF = matchingBXDFs[match];
		
		final Point2F sampleRemapped = new Point2F(Floats.min(sample.x * matches - match, 0.99999994F), sample.y);
		
		final Optional<BXDFResult> optionalBXDFResult = matchingBXDF.sampleDistributionFunction(outgoing, normal, sampleRemapped);
		
		if(!optionalBXDFResult.isPresent()) {
			return Optional.empty();
		}
		
		final BXDFResult bXDFResult = optionalBXDFResult.get();
		
		final Vector3F incoming = bXDFResult.getIncoming();
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incoming);
		final Vector3F incomingWorldSpaceCorrectlyOriented = this.isNegatingIncoming ? Vector3F.negate(incomingWorldSpace) : incomingWorldSpace;
		final Vector3F surfaceNormalG = this.intersection.getSurfaceNormalGCorrectlyOriented();
		
		Color3F result = bXDFResult.getResult();
		
		float probabilityDensityFunctionValue = bXDFResult.getProbabilityDensityFunctionValue();
		
		if(!matchingBXDF.getBXDFType().isSpecular()) {
			final float iDotN = Vector3F.dotProduct(incomingWorldSpaceCorrectlyOriented, surfaceNormalG);
			final float oDotN = Vector3F.dotProduct(this.outgoingWorldSpace, surfaceNormalG);
			
			final boolean isReflecting = iDotN * oDotN > 0.0F;
			
			result = Color3F.BLACK;
			
			for(int i = 0; i < matches; i++) {
				final BXDF bXDF = matchingBXDFs[i];
				
				if(matchingBXDF != bXDF) {
					probabilityDensityFunctionValue += bXDF.evaluateProbabilityDensityFunction(outgoing, normal, incoming);
				}
				
				if(isReflecting && bXDF.getBXDFType().hasReflection() || !isReflecting && bXDF.getBXDFType().hasTransmission()) {
					result = Color3F.add(result, bXDF.evaluateDistributionFunction(outgoing, normal, incoming));
				}
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		return Optional.of(new BSDFResult(bXDFResult.getBXDFType(), result, incomingWorldSpaceCorrectlyOriented, this.outgoingWorldSpace, probabilityDensityFunctionValue));
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
	 * Compares {@code bSDF} to this {@code BSDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code bSDF} is an instance of {@code BSDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param bSDF the {@code BSDF} to compare to this {@code BSDF} instance for equality
	 * @return {@code true} if, and only if, {@code bSDF} is an instance of {@code BSDF}, and their respective values are equal, {@code false} otherwise
	 */
	public boolean equals(final BSDF bSDF) {
		if(bSDF == this) {
			return true;
		} else if(bSDF == null) {
			return false;
		} else if(!Objects.equals(this.intersection, bSDF.intersection)) {
			return false;
		} else if(!Objects.equals(this.bXDFs, bSDF.bXDFs)) {
			return false;
		} else if(!Objects.equals(this.normalLocalSpace, bSDF.normalLocalSpace)) {
			return false;
		} else if(!Objects.equals(this.normalWorldSpace, bSDF.normalWorldSpace)) {
			return false;
		} else if(!Objects.equals(this.outgoingLocalSpace, bSDF.outgoingLocalSpace)) {
			return false;
		} else if(!Objects.equals(this.outgoingWorldSpace, bSDF.outgoingWorldSpace)) {
			return false;
		} else if(this.isNegatingIncoming != bSDF.isNegatingIncoming) {
			return false;
		} else if(!Floats.equals(this.eta, bSDF.eta)) {
			return false;
		} else {
			return true;
		}
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
		} else {
			return equals(BSDF.class.cast(object));
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code bXDFType} or {@code incomingWorldSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDFType a {@link BXDFType} instance to match against
	 * @param incomingWorldSpace the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code bXDFType} or {@code incomingWorldSpace} are {@code null}
	 */
	public float evaluateProbabilityDensityFunction(final BXDFType bXDFType, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		if(this.bXDFs.size() == 0) {
			return 0.0F;
		}
		
		final Vector3F incomingWorldSpaceCorrectlyOriented = this.isNegatingIncoming ? Vector3F.negate(incomingWorldSpace) : incomingWorldSpace;
		final Vector3F outgoing = this.outgoingLocalSpace;
		final Vector3F normal = this.normalLocalSpace;
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpaceCorrectlyOriented);
		
		if(Floats.isZero(outgoing.z)) {
			return 0.0F;
		}
		
		float probabilityDensityFunctionValue = 0.0F;
		
		int matches = 0;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
				matches++;
				
				probabilityDensityFunctionValue += bXDF.evaluateProbabilityDensityFunction(outgoing, normal, incoming);
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
		return Objects.hash(this.intersection, this.bXDFs, this.normalLocalSpace, this.normalWorldSpace, this.outgoingLocalSpace, this.outgoingWorldSpace, Boolean.valueOf(this.isNegatingIncoming), Float.valueOf(this.eta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Vector3F doTransformToLocalSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transformReverse(vector, this.intersection.getOrthonormalBasisS()));
	}
	
	private Vector3F doTransformToWorldSpace(final Vector3F vector) {
		return Vector3F.normalize(Vector3F.transform(vector, this.intersection.getOrthonormalBasisS()));
	}
	
	private int doComputeMatches(final BXDFType bXDFType, final BXDF[] matchingBXDFs) {
		int matches = 0;
		
		for(final BXDF bXDF : this.bXDFs) {
			if(bXDF.getBXDFType().matches(bXDFType)) {
				matchingBXDFs[matches] = bXDF;
				
				matches++;
			}
		}
		
		return matches;
	}
}