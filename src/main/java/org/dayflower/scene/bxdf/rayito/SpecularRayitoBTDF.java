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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDFType;

/**
 * A {@code SpecularRayitoBTDF} is an implementation of {@link RayitoBXDF} that represents a BTDF (Bidirectional Transmittance Distribution Function) for specular transmission.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpecularRayitoBTDF extends RayitoBXDF {
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpecularRayitoBTDF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SpecularRayitoBTDF(1.0F, 1.5F);
	 * }
	 * </pre>
	 */
	public SpecularRayitoBTDF() {
		this(1.0F, 1.5F);
	}
	
	/**
	 * Constructs a new {@code SpecularRayitoBTDF} instance.
	 * 
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 */
	public SpecularRayitoBTDF(final float etaA, final float etaB) {
		super(BXDFType.SPECULAR_TRANSMISSION);
		
		this.etaA = etaA;
		this.etaB = etaB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public RayitoBXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return new RayitoBXDFResult(o, n, i, 0.0F, 0.0F);
	}
	
	/**
	 * Samples the solid angle or the projected solid angle for {@code o}, {@code n} and {@code orthonormalBasis}.
	 * <p>
	 * Returns a {@link RayitoBXDFResult} with the result of the operation.
	 * <p>
	 * If either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param orthonormalBasis an {@link OrthonormalBasis33F} instance
	 * @param u the U-coordinate
	 * @param v the V-coordinate
	 * @return a {@code RayitoBXDFResult} with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code orthonormalBasis} are {@code null}
	 */
	@Override
	public RayitoBXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		return doSampleSolidAngle3(o, n);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpecularRayitoBTDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpecularRayitoBTDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new SpecularRayitoBTDF(%+.10f, %+.10f)", Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
	
	/**
	 * Compares {@code object} to this {@code SpecularRayitoBTDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpecularRayitoBTDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpecularRayitoBTDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpecularRayitoBTDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpecularRayitoBTDF)) {
			return false;
		} else if(!equal(this.etaA, SpecularRayitoBTDF.class.cast(object).etaA)) {
			return false;
		} else if(!equal(this.etaB, SpecularRayitoBTDF.class.cast(object).etaB)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value of the solid angle or the projected solid angle for {@code o}, {@code n} and {@code i}.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction from the surface intersection point to the origin of the ray
	 * @param n a {@code Vector3F} instance with the surface normal
	 * @param i a {@code Vector3F} instance with the incoming direction from the light source to the surface intersection point
	 * @return the probability density function (PDF) value of the solid angle for {@code o}, {@code n} and {@code i}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		Objects.requireNonNull(o, "o == null");
		Objects.requireNonNull(n, "n == null");
		Objects.requireNonNull(i, "i == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code SpecularRayitoBTDF} instance.
	 * 
	 * @return a hash code for this {@code SpecularRayitoBTDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private RayitoBXDFResult doSampleSolidAngle1(final Vector3F o, final Vector3F n) {
		final Vector3F d = Vector3F.negate(o);
		
		final float nDotD = Vector3F.dotProduct(n, d);
		
		final float cosTheta = saturate(nDotD, -1.0F, 1.0F);
		final float cosThetaAbs = abs(cosTheta);
		
		final boolean isEntering = cosTheta < 0.0F;
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		
		final Vector3F nCorrectlyOriented = isEntering ? n : Vector3F.negate(n);
		
		final float eta = etaI / etaT;
		
		final float k = 1.0F - eta * eta * (1.0F - cosThetaAbs * cosThetaAbs);
		
		if(k < 0.0F) {
			return new RayitoBXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		final Vector3F i = Vector3F.normalize(Vector3F.add(Vector3F.multiply(d, eta), Vector3F.multiply(nCorrectlyOriented, eta * cosTheta - sqrt(k))));
		
		final float nDotI = Vector3F.dotProduct(n, i);
		
		return new RayitoBXDFResult(o, n, Vector3F.negate(i), abs(nDotI), 1.0F);
	}
	
	@SuppressWarnings("unused")
	private RayitoBXDFResult doSampleSolidAngle2(final Vector3F o, final Vector3F n) {
		final float nDotO = Vector3F.dotProduct(n, o);
		
		final Vector3F d = Vector3F.negate(o);
		final Vector3F nCorrectlyOriented = Vector3F.dotProduct(n, d) < 0.0F ? n : Vector3F.negate(n);
		final Vector3F reflection = nDotO < 0.0F ? Vector3F.add(o, Vector3F.multiply(n, 2.0F * nDotO)) : Vector3F.subtract(o, Vector3F.multiply(n, 2.0F * nDotO));
		
		final float cosTheta = Vector3F.dotProduct(d, nCorrectlyOriented);
		
		final boolean isEntering = Vector3F.dotProduct(n, nCorrectlyOriented) > 0.0F;
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = isEntering ? etaA / etaB : etaB / etaA;
		
		final float k = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
		
		if(k < 0.0F) {
			return new RayitoBXDFResult(o, n, reflection, abs(Vector3F.dotProduct(n, reflection)), 1.0F);
		}
		
		final Vector3F transmission = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(d, eta), Vector3F.multiply(n, (isEntering ? 1.0F : -1.0F) * (eta * cosTheta + sqrt(k)))));
		
		return new RayitoBXDFResult(o, n, Vector3F.negate(transmission), abs(Vector3F.dotProduct(n, transmission)), 1.0F);
	}
	
	private RayitoBXDFResult doSampleSolidAngle3(final Vector3F o, final Vector3F n) {
		final Vector3F d = Vector3F.negate(o);
		final Vector3F reflection = Vector3F.reflection(d, n, true);
		final Vector3F nCorrectlyOriented = Vector3F.dotProduct(n, d) < 0.0F ? n : Vector3F.negate(n);
		
		final boolean isEntering = Vector3F.dotProduct(n, nCorrectlyOriented) > 0.0F;
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = isEntering ? etaA / etaB : etaB / etaA;
		
		final float cosTheta = Vector3F.dotProduct(d, nCorrectlyOriented);
		final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
		
		if(cosTheta2Squared < 0.0F) {
//			TODO: Find out why the PDF and Reflectance variables seems to be swapped? Swapping them does not work.
			return new RayitoBXDFResult(o, n, Vector3F.negate(reflection), abs(Vector3F.dotProduct(n, reflection)), 1.0F);
		}
		
		final Vector3F transmission = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(d, eta), Vector3F.multiply(n, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
		
		final float a = etaB - etaA;
		final float b = etaB + etaA;
		
		final float reflectance = fresnelDielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmission, n), a * a / (b * b));
		final float transmittance = 1.0F - reflectance;
		
		final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
		final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
		final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
		
		if(random() < probabilityRussianRoulette) {
//			TODO: Find out why the PDF and Reflectance variables seems to be swapped? Swapping them does not work.
			return new RayitoBXDFResult(o, n, Vector3F.negate(reflection), abs(Vector3F.dotProduct(n, reflection)), probabilityRussianRouletteReflection);
		}
		
//		TODO: Find out why the PDF and Reflectance variables seems to be swapped? Swapping them does not work.
		return new RayitoBXDFResult(o, n, Vector3F.negate(transmission), abs(Vector3F.dotProduct(n, transmission)), probabilityRussianRouletteTransmission);
	}
}