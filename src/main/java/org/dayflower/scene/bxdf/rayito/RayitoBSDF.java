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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDFResult;
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
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param normal a {@code Vector3F} instance with the normal
	 * @param incoming a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(this.rayitoBXDFs.isEmpty()) {
			return Color3F.BLACK;
		}
		
		return this.rayitoBXDFs.get(0).evaluateDistributionFunction(outgoing, normal, incoming);
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BSDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param normal a {@code Vector3F} instance with the normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return an optional {@code BSDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code orthonormalBasis} are {@code null}
	 */
	public Optional<BSDFResult> sampleDistributionFunction(final Vector3F outgoing, final Vector3F normal, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(orthonormalBasis, "orthonormalBasis == null");
		
		if(this.rayitoBXDFs.isEmpty()) {
			return Optional.empty();
		}
		
		final Optional<BXDFResult> optionalBXDFResult = this.rayitoBXDFs.get(0).sampleDistributionFunction(outgoing, normal, orthonormalBasis, u, v);
		
		if(optionalBXDFResult.isPresent()) {
			final BXDFResult bXDFResult = optionalBXDFResult.get();
			
			final BSDFResult bSDFResult = new BSDFResult(bXDFResult.getBXDFType(), bXDFResult.getResult(), bXDFResult.getIncoming(), bXDFResult.getOutgoing(), bXDFResult.getProbabilityDensityFunctionValue());
			
			return Optional.of(bSDFResult);
		}
		
		return Optional.empty();
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
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param normal a {@code Vector3F} instance with the normal
	 * @param incoming a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(this.rayitoBXDFs.isEmpty()) {
			return 0.0F;
		}
		
		return this.rayitoBXDFs.get(0).evaluateProbabilityDensityFunction(outgoing, normal, incoming);
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
}