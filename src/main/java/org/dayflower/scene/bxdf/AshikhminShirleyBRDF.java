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
package org.dayflower.scene.bxdf;

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.pow;

import java.util.Objects;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;

/**
 * An {@code AshikhminShirleyBRDF} is an implementation of {@link BXDF} that represents an Ashikhmin-Shirley BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AshikhminShirleyBRDF implements BXDF {
	private final float exponent;
	private final float fresnel;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AshikhminShirleyBRDF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AshikhminShirleyBRDF(0.05F);
	 * }
	 * </pre>
	 */
	public AshikhminShirleyBRDF() {
		this(0.05F);
	}
	
	/**
	 * Constructs a new {@code AshikhminShirleyBRDF} instance.
	 * 
	 * @param roughness the roughness to use
	 */
	public AshikhminShirleyBRDF(final float roughness) {
		this.exponent = 1.0F / (roughness * roughness);
		this.fresnel = 1.0F;
		this.roughness = roughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ashikhminShirleyBRDF.evaluateSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return evaluateSolidAngle(o, n, i, false);
	}
	
	/**
	 * Evaluates the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be evaluated, {@code false} otherwise
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		final Vector3F h = Vector3F.half(o, n, i);
		
		final float nDotH = Vector3F.dotProduct(n, h);
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		final float oDotH = Vector3F.dotProduct(o, h);
		
		final float d = (this.exponent + 1.0F) * pow(abs(nDotH), this.exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = 1.0F;//TODO: Implement Fresnel!
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return new BXDFResult(o, n, i, 0.0F, 0.0F);
		} else if(isProjected) {
			return new BXDFResult(o, n, i, d / (4.0F * abs(oDotH) * abs(nDotI)), d * f / (4.0F * abs(nDotO + -nDotI - nDotO * -nDotI)));
		} else {
			return new BXDFResult(o, n, i, d / (4.0F * abs(oDotH)),              d * f / (4.0F * abs(nDotO + -nDotI - nDotO * -nDotI)));
		}
	}
	
	/**
	 * Samples the solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ashikhminShirleyBRDF.sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		return sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	}
	
	/**
	 * Samples the solid angle or the projected solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link BXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be sampled, {@code false} otherwise
	 * @return a {@code BXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v, final boolean isProjected) {
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F hLocalSpace = SampleGeneratorF.sampleHemispherePowerCosineDistribution(u, v, this.exponent);
		final Vector3F hTransformed = Vector3F.transform(hLocalSpace, orthonormalBasis);
		final Vector3F h = nDotO < 0.0F ? Vector3F.negate(hTransformed) : hTransformed;
		
		final float oDotH = Vector3F.dotProduct(o, h);
		
		final Vector3F i = Vector3F.subtract(o, Vector3F.multiply(h, 2.0F * oDotH));
		
		return evaluateSolidAngle(o, n, i, isProjected);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AshikhminShirleyBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code AshikhminShirleyBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new AshikhminShirleyBRDF(%+.10f)", Float.valueOf(this.roughness));
	}
	
	/**
	 * Compares {@code object} to this {@code AshikhminShirleyBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AshikhminShirleyBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AshikhminShirleyBRDF)) {
			return false;
		} else if(!equal(this.exponent, AshikhminShirleyBRDF.class.cast(object).exponent)) {
			return false;
		} else if(!equal(this.fresnel, AshikhminShirleyBRDF.class.cast(object).fresnel)) {
			return false;
		} else if(!equal(this.roughness, AshikhminShirleyBRDF.class.cast(object).roughness)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AshikhminShirleyBRDF} instance is using a Dirac distribution, {@code false} otherwise.
	 * <p>
	 * This method always returns {@code false}.
	 * 
	 * @return {@code true} if, and only if, this {@code AshikhminShirleyBRDF} instance is using a Dirac distribution, {@code false} otherwise
	 */
	@Override
	public boolean isDiracDistribution() {
		return false;
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ashikhminShirleyBRDF.probabilityDensityFunctionSolidAngle(o, n, i, false);
	 * }
	 * </pre>
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return probabilityDensityFunctionSolidAngle(o, n, i, false);
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @param isProjected {@code true} if, and only if, the projected solid angle should be used, {@code false} otherwise
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		final Vector3F h = Vector3F.half(o, n, i);
		
		final float nDotH = Vector3F.dotProduct(n, h);
		final float nDotI = Vector3F.dotProduct(n, i);
		final float nDotO = Vector3F.dotProduct(n, o);
		final float oDotH = Vector3F.dotProduct(o, h);
		
		if(nDotI > 0.0F && nDotO > 0.0F || nDotI < 0.0F && nDotO < 0.0F) {
			return 0.0F;
		} else if(isProjected) {
			return (this.exponent + 1.0F) * pow(abs(nDotH), this.exponent) / (PI * 8.0F * abs(oDotH) * abs(nDotI));
		} else {
			return (this.exponent + 1.0F) * pow(abs(nDotH), this.exponent) / (PI * 8.0F * abs(oDotH));
		}
	}
	
	/**
	 * Returns a hash code for this {@code AshikhminShirleyBRDF} instance.
	 * 
	 * @return a hash code for this {@code AshikhminShirleyBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.exponent), Float.valueOf(this.fresnel), Float.valueOf(this.roughness));
	}
}