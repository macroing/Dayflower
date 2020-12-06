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
package org.dayflower.scene.bxdf.rayito;

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Ints.min;

import java.util.ArrayList;
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
 * A {@code RayitoBSDF} represents a BSDF (Bidirectional Scattering Distribution Function).
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayitoBSDF implements BSDF {
	private final Intersection intersection;
	private final List<RayitoBXDF> rayitoBXDFs;
	private final float eta;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code rayitoBXDFs} or at least one element in {@code rayitoBXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code rayitoBXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param rayitoBXDFs a {@code List} of {@link RayitoBXDF} instances
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code rayitoBXDFs} or at least one element in {@code rayitoBXDFs} are {@code null}
	 */
	public RayitoBSDF(final Intersection intersection, final List<RayitoBXDF> rayitoBXDFs) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.rayitoBXDFs = new ArrayList<>(ParameterArguments.requireNonNullList(rayitoBXDFs, "rayitoBXDFs"));
		this.eta = 1.0F;
	}
	
	/**
	 * Constructs a new {@code RayitoBSDF} instance.
	 * <p>
	 * If either {@code intersection}, {@code rayitoBXDFs} or at least one element in {@code rayitoBXDFs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code List} {@code rayitoBXDFs} will be copied.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param rayitoBXDFs a {@code List} of {@link RayitoBXDF} instances
	 * @param eta the index of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code rayitoBXDFs} or at least one element in {@code rayitoBXDFs} are {@code null}
	 */
	public RayitoBSDF(final Intersection intersection, final List<RayitoBXDF> rayitoBXDFs, final float eta) {
		this.intersection = Objects.requireNonNull(intersection, "intersection == null");
		this.rayitoBXDFs = new ArrayList<>(ParameterArguments.requireNonNullList(rayitoBXDFs, "rayitoBXDFs"));
		this.eta = eta;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public Color3F evaluateDistributionFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(normalWorldSpace, "normalWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F normal = doTransformToLocalSpace(normalWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		final boolean isReflecting = Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) > 0.0F;
		
		Color3F result = Color3F.BLACK;
		
		for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
			if(rayitoBXDF.getBXDFType().matches(bXDFType) && (isReflecting && rayitoBXDF.getBXDFType().hasReflection() || !isReflecting && rayitoBXDF.getBXDFType().hasTransmission())) {
				result = Color3F.add(result, rayitoBXDF.evaluateDistributionFunction(outgoing, normal, incoming));
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
		
		final RayitoBXDF matchingRayitoBXDF = doGetMatchingRayitoBXDF(bXDFType, match);
		
		if(matchingRayitoBXDF == null) {
			return Optional.empty();
		}
		
		final Point2F sampleRemapped = new Point2F(min(sample.getU() * matches - match, 0.99999994F), sample.getV());
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F normal = doTransformToLocalSpace(normalWorldSpace);
		
		if(isZero(outgoing.getZ())) {
			return Optional.empty();
		}
		
		final Optional<BXDFResult> optionalBXDFResult = matchingRayitoBXDF.sampleDistributionFunction(outgoing, normal, sampleRemapped);
		
		if(!optionalBXDFResult.isPresent()) {
			return Optional.empty();
		}
		
		final BXDFResult bXDFResult = optionalBXDFResult.get();
		
		final Vector3F incoming = bXDFResult.getIncoming();
		final Vector3F incomingWorldSpace = doTransformToWorldSpace(incoming);
		final Vector3F surfaceNormalG = this.intersection.getSurfaceIntersectionWorldSpace().getOrthonormalBasisG().getW();
		
		Color3F result = bXDFResult.getResult();
		
		float probabilityDensityFunctionValue = bXDFResult.getProbabilityDensityFunctionValue();
		
		if(matches > 1 && !matchingRayitoBXDF.getBXDFType().isSpecular()) {
			for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
				if(matchingRayitoBXDF != rayitoBXDF && rayitoBXDF.getBXDFType().matches(bXDFType)) {
					probabilityDensityFunctionValue += rayitoBXDF.evaluateProbabilityDensityFunction(outgoing, normal, incoming);
				}
			}
		}
		
		if(matches > 1) {
			probabilityDensityFunctionValue /= matches;
		}
		
		if(!matchingRayitoBXDF.getBXDFType().isSpecular()) {
			final boolean isReflecting = Vector3F.dotProduct(incomingWorldSpace, surfaceNormalG) * Vector3F.dotProduct(outgoingWorldSpace, surfaceNormalG) > 0.0F;
			
			result = Color3F.BLACK;
			
			for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
				if(rayitoBXDF.getBXDFType().matches(bXDFType) && (isReflecting && rayitoBXDF.getBXDFType().hasReflection() || !isReflecting && rayitoBXDF.getBXDFType().hasTransmission())) {
					result = Color3F.add(result, rayitoBXDF.evaluateDistributionFunction(outgoing, normal, incoming));
				}
			}
		}
		
		return Optional.of(new BSDFResult(bXDFResult.getBXDFType(), result, incomingWorldSpace, outgoingWorldSpace, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RayitoBSDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code RayitoBSDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new RayitoBSDF(%s, %s, %+.10f)", this.intersection, "...", Float.valueOf(this.eta));
	}
	
	/**
	 * Compares {@code object} to this {@code RayitoBSDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RayitoBSDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RayitoBSDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RayitoBSDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RayitoBSDF)) {
			return false;
		} else if(!Objects.equals(this.intersection, RayitoBSDF.class.cast(object).intersection)) {
			return false;
		} else if(!Objects.equals(this.rayitoBXDFs, RayitoBSDF.class.cast(object).rayitoBXDFs)) {
			return false;
		} else if(!equal(this.eta, RayitoBSDF.class.cast(object).eta)) {
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
	public float evaluateProbabilityDensityFunction(final BXDFType bXDFType, final Vector3F outgoingWorldSpace, final Vector3F normalWorldSpace, final Vector3F incomingWorldSpace) {
		Objects.requireNonNull(bXDFType, "bXDFType == null");
		Objects.requireNonNull(outgoingWorldSpace, "outgoingWorldSpace == null");
		Objects.requireNonNull(normalWorldSpace, "normalWorldSpace == null");
		Objects.requireNonNull(incomingWorldSpace, "incomingWorldSpace == null");
		
		if(this.rayitoBXDFs.size() == 0) {
			return 0.0F;
		}
		
		final Vector3F outgoing = doTransformToLocalSpace(outgoingWorldSpace);
		final Vector3F normal = doTransformToLocalSpace(normalWorldSpace);
		final Vector3F incoming = doTransformToLocalSpace(incomingWorldSpace);
		
		if(isZero(outgoing.getZ())) {
			return 0.0F;
		}
		
		float probabilityDensityFunctionValue = 0.0F;
		
		int matches = 0;
		
		for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
			if(rayitoBXDF.getBXDFType().matches(bXDFType)) {
				matches++;
				
				probabilityDensityFunctionValue += rayitoBXDF.evaluateProbabilityDensityFunction(outgoing, normal, incoming);
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
	 * Returns the {@link RayitoBXDF} count by specular or non-specular type.
	 * 
	 * @param isSpecular {@code true} if, and only if, specular types should be counted, {@code false} otherwise
	 * @return the {@code RayitoBXDF} count by specular or non-specular type
	 */
	public int countBXDFsBySpecularType(final boolean isSpecular) {
		int count = 0;
		
		for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
			if(rayitoBXDF.getBXDFType().isSpecular() == isSpecular) {
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Returns a hash code for this {@code RayitoBSDF} instance.
	 * 
	 * @return a hash code for this {@code RayitoBSDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.intersection, this.rayitoBXDFs, Float.valueOf(this.eta));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private RayitoBXDF doGetMatchingRayitoBXDF(final BXDFType bXDFType, final int match) {
		for(int i = 0, j = match; i < this.rayitoBXDFs.size(); i++) {
			final RayitoBXDF rayitoBXDF = this.rayitoBXDFs.get(i);
			
			if(rayitoBXDF.getBXDFType().matches(bXDFType) && j-- == 0) {
				return rayitoBXDF;
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
		
		for(final RayitoBXDF rayitoBXDF : this.rayitoBXDFs) {
			if(rayitoBXDF.getBXDFType().matches(bXDFType)) {
				matches++;
			}
		}
		
		return matches;
	}
}