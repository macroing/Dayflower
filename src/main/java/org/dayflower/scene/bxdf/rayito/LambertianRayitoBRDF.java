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

import static org.dayflower.util.Floats.PI_RECIPROCAL;
import static org.dayflower.util.Floats.abs;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFType;

/**
 * A {@code LambertianRayitoBRDF} is an implementation of {@link RayitoBXDF} that represents a Lambertian BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LambertianRayitoBRDF extends RayitoBXDF {
	/**
	 * Constructs a new {@code LambertianRayitoBRDF} instance.
	 */
	public LambertianRayitoBRDF() {
		super(BXDFType.DIFFUSE_REFLECTION);
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
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return Color3F.BLACK;
		}
		
		return new Color3F(PI_RECIPROCAL);
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link RayitoBXDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param normal a {@code Vector3F} instance with the normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return an optional {@code RayitoBXDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public Optional<RayitoBXDFResult> sampleDistributionFunction(final Vector3F outgoing, final Vector3F normal, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		final Vector3F incomingLocalSpace = Vector3F.negate(SampleGeneratorF.sampleHemisphereCosineDistribution(u, v));
		final Vector3F incomingTransformed = Vector3F.transform(incomingLocalSpace, orthonormalBasis);
		final Vector3F incoming = normalDotOutgoing < 0.0F ? Vector3F.negate(incomingTransformed) : incomingTransformed;
		
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		
		return Optional.of(new RayitoBXDFResult(new Color3F(PI_RECIPROCAL), outgoing, normal, incoming, PI_RECIPROCAL * abs(normalDotIncoming)));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LambertianRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code LambertianRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return "new LambertianRayitoBRDF()";
	}
	
	/**
	 * Compares {@code object} to this {@code LambertianRayitoBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LambertianRayitoBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LambertianRayitoBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LambertianRayitoBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LambertianRayitoBRDF)) {
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
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return 0.0F;
		}
		
		return PI_RECIPROCAL * abs(normalDotIncoming);
	}
	
	/**
	 * Returns a hash code for this {@code LambertianRayitoBRDF} instance.
	 * 
	 * @return a hash code for this {@code LambertianRayitoBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
}