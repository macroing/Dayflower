/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.random;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.fresnel.DielectricFresnel;

/**
 * A {@code SpecularRayitoBTDF} is an implementation of {@link RayitoBXDF} that represents a BTDF (Bidirectional Transmittance Distribution Function) for specular transmission.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpecularRayitoBTDF extends RayitoBXDF {
	private final Color3F reflectanceScale;
	private final Color3F transmittanceScale;
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpecularRayitoBTDF} instance.
	 * <p>
	 * If either {@code reflectanceScale} or {@code transmittanceScale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param transmittanceScale a {@code Color3F} instance that represents the transmittance scale
	 * @param etaA the index of refraction denoted by {@code A}
	 * @param etaB the index of refraction denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code reflectanceScale} or {@code transmittanceScale} are {@code null}
	 */
	public SpecularRayitoBTDF(final Color3F reflectanceScale, final Color3F transmittanceScale, final float etaA, final float etaB) {
		super(BXDFType.SPECULAR_REFLECTION_AND_TRANSMISSION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
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
		final Vector3F normalCorrectlyOriented = Vector3F.faceForwardNegated(normal, direction);
		
		final boolean isEntering = Vector3F.dotProduct(normal, normalCorrectlyOriented) > 0.0F;
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final Optional<Vector3F> optionalRefractionDirection = Vector3F.refraction2(direction, normalCorrectlyOriented, eta);
		
		if(optionalRefractionDirection.isPresent()) {
			final Vector3F refractionDirection = optionalRefractionDirection.get();
			
			final float cosThetaI = Vector3F.dotProduct(direction, normalCorrectlyOriented);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : Vector3F.dotProduct(refractionDirection, normal);
			
			final float reflectance = DielectricFresnel.evaluate(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				final BXDFType bXDFType = getBXDFType();
				
				final Color3F result = Color3F.multiply(this.reflectanceScale, probabilityRussianRouletteReflection);
				
				final Vector3F incoming = Vector3F.negate(Vector3F.reflection(direction, normal, true));
				
				final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, incoming));
				
				return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
			}
			
			final BXDFType bXDFType = getBXDFType();
			
			final Color3F result = Color3F.multiply(this.transmittanceScale, probabilityRussianRouletteTransmission);
			
			final Vector3F incoming = Vector3F.negate(optionalRefractionDirection.get());
			
			final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, incoming));
			
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = this.reflectanceScale;
		
		final Vector3F incoming = Vector3F.negate(Vector3F.reflection(direction, normal, true));
		
		final float probabilityDensityFunctionValue = abs(Vector3F.dotProduct(normal, incoming));
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpecularRayitoBTDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpecularRayitoBTDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new SpecularRayitoBTDF(%s, %s, %+.10f, %+.10f)", this.reflectanceScale, this.transmittanceScale, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
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
		} else if(!Objects.equals(this.reflectanceScale, SpecularRayitoBTDF.class.cast(object).reflectanceScale)) {
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
		return Objects.hash(this.reflectanceScale, this.transmittanceScale, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
}