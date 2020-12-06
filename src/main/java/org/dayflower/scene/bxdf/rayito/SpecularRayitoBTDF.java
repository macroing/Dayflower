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
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFResult;
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
	private final Color3F transmittanceScale;
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpecularRayitoBTDF} instance.
	 * <p>
	 * If {@code transmittanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transmittanceScale a {@link Color3F} instance that represents the transmittance scale
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, {@code transmittanceScale} is {@code null}
	 */
	public SpecularRayitoBTDF(final Color3F transmittanceScale, final float etaA, final float etaB) {
		super(BXDFType.SPECULAR_REFLECTION_AND_TRANSMISSION);
		
		this.transmittanceScale = Objects.requireNonNull(transmittanceScale, "transmittanceScale == null");
		this.etaA = etaA;
		this.etaB = etaB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param incoming the incoming direction
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Color3F.BLACK;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BXDFResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param sample the sample point
	 * @return an optional {@code BXDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<BXDFResult> sampleDistributionFunction(final Vector3F outgoing, final Vector3F normal, final Point2F sample) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F direction = Vector3F.negate(outgoing);
		final Vector3F reflection = Vector3F.reflection(direction, normal, true);
		final Vector3F normalCorrectlyOriented = Vector3F.dotProduct(normal, direction) < 0.0F ? normal : Vector3F.negate(normal);
		
		final boolean isEntering = Vector3F.dotProduct(normal, normalCorrectlyOriented) > 0.0F;
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = isEntering ? etaA / etaB : etaB / etaA;
		
		final float cosTheta = Vector3F.dotProduct(direction, normalCorrectlyOriented);
		final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
		
		final BXDFType bXDFType = getBXDFType();
		
		if(cosTheta2Squared < 0.0F) {
			final Color3F result = Color3F.WHITE;
			
			final Vector3F incoming = Vector3F.negate(reflection);
			
			final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, reflection));
			
//			TODO: Find out why the PDF and Result variables seems to be swapped? Swapping them does not work.
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final Vector3F transmission = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(direction, eta), Vector3F.multiply(normal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
		
		final float a = etaB - etaA;
		final float b = etaB + etaA;
		
		final float reflectance = fresnelDielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmission, normal), a * a / (b * b));
		final float transmittance = 1.0F - reflectance;
		
		final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
		final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
		final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
		
		if(random() < probabilityRussianRoulette) {
			final Color3F result = new Color3F(probabilityRussianRouletteReflection);
			
			final Vector3F incoming = Vector3F.negate(reflection);
			
			final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, reflection));
			
//			TODO: Find out why the PDF and Result variables seems to be swapped? Swapping them does not work.
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final Color3F result = new Color3F(probabilityRussianRouletteTransmission);
		
		final Vector3F incoming = Vector3F.negate(transmission);
		
		final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, transmission));
		
//		TODO: Find out why the PDF and Result variables seems to be swapped? Swapping them does not work.
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpecularRayitoBTDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpecularRayitoBTDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new SpecularRayitoBTDF(%s, %+.10f, %+.10f)", this.transmittanceScale, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
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
		} else if(!Objects.equals(this.transmittanceScale, SpecularRayitoBTDF.class.cast(object).transmittanceScale)) {
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
	 * Evaluates the probability density function (PDF).
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code SpecularRayitoBTDF} instance.
	 * 
	 * @return a hash code for this {@code SpecularRayitoBTDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.transmittanceScale, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
}