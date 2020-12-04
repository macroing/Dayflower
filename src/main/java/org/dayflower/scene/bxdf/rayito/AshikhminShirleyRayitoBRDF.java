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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.pow;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFType;

/**
 * An {@code AshikhminShirleyRayitoBRDF} is an implementation of {@link RayitoBXDF} that represents an Ashikhmin-Shirley BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AshikhminShirleyRayitoBRDF extends RayitoBXDF {
	private final float exponent;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AshikhminShirleyRayitoBRDF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyRayitoBRDF(0.05F);
	 * }
	 * </pre>
	 */
	public AshikhminShirleyRayitoBRDF() {
		this(0.05F);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyRayitoBRDF} instance.
	 * 
	 * @param roughness the roughness to use
	 */
	public AshikhminShirleyRayitoBRDF(final float roughness) {
		super(BXDFType.GLOSSY_REFLECTION);
		
		this.exponent = 1.0F / (roughness * roughness);
		this.roughness = roughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F o, final Vector3F n, final Vector3F i) {
		final Vector3F h = Vector3F.half(o, n, i);
		
		final float nDotH = Vector3F.dotProduct(n, h);
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		final float oDotH = Vector3F.dotProduct(o, h);
		
		final float d = (this.exponent + 1.0F) * pow(abs(nDotH), this.exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = fresnelDielectricSchlick(oDotH, 1.0F);
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return Color3F.BLACK;
		}
		
		return new Color3F(f * d / (4.0F * abs(nDotO + -nDotI - nDotO * -nDotI)));
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link RayitoBXDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return an optional {@code RayitoBXDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public Optional<RayitoBXDFResult> sampleDistributionFunction(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F hLocalSpace = SampleGeneratorF.sampleHemispherePowerCosineDistribution(u, v, this.exponent);
		final Vector3F hTransformed = Vector3F.transform(hLocalSpace, orthonormalBasis);
		final Vector3F h = nDotO < 0.0F ? Vector3F.negate(hTransformed) : hTransformed;
		
		final float oDotH = Vector3F.dotProduct(o, h);
		
		final Vector3F i = Vector3F.subtract(o, Vector3F.multiply(h, 2.0F * oDotH));
		
		final Color3F result = evaluateDistributionFunction(o, n, i);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(o, n, i);
		
		return Optional.of(new RayitoBXDFResult(result, o, n, i, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AshikhminShirleyRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code AshikhminShirleyRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new AshikhminShirleyRayitoBRDF(%+.10f)", Float.valueOf(this.roughness));
	}
	
	/**
	 * Compares {@code object} to this {@code AshikhminShirleyRayitoBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyRayitoBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AshikhminShirleyRayitoBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyRayitoBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AshikhminShirleyRayitoBRDF)) {
			return false;
		} else if(!equal(this.exponent, AshikhminShirleyRayitoBRDF.class.cast(object).exponent)) {
			return false;
		} else if(!equal(this.roughness, AshikhminShirleyRayitoBRDF.class.cast(object).roughness)) {
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
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F o, final Vector3F n, final Vector3F i) {
		final Vector3F h = Vector3F.half(o, n, i);
		
		final float nDotH = Vector3F.dotProduct(n, h);
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		final float oDotH = Vector3F.dotProduct(o, h);
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return 0.0F;
		}
		
		return (this.exponent + 1.0F) * pow(abs(nDotH), this.exponent) / (PI * 8.0F * abs(oDotH));
	}
	
	/**
	 * Returns a hash code for this {@code AshikhminShirleyRayitoBRDF} instance.
	 * 
	 * @return a hash code for this {@code AshikhminShirleyRayitoBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.exponent), Float.valueOf(this.roughness));
	}
}