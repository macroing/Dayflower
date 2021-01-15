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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.pow;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFResult;
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
	private final Color3F reflectanceScale;
	private final float exponent;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AshikhminShirleyRayitoBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public AshikhminShirleyRayitoBRDF(final Color3F reflectanceScale, final float roughness) {
		super(BXDFType.GLOSSY_REFLECTION_AND_TRANSMISSION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.exponent = 1.0F / (roughness * roughness);
		this.roughness = roughness;
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
		
		final Vector3F half = Vector3F.half(outgoing, normal, incoming);
		
		final float normalDotHalf = Vector3F.dotProduct(normal, half);
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		final float outgoingDotHalf = Vector3F.dotProduct(outgoing, half);
		
		final float d = (this.exponent + 1.0F) * pow(abs(normalDotHalf), this.exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = fresnelDielectricSchlick(outgoingDotHalf, 1.0F);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return Color3F.BLACK;
		}
		
		return Color3F.divide(Color3F.multiply(Color3F.multiply(this.reflectanceScale, d), f), 4.0F * abs(normalDotOutgoing + -normalDotIncoming - normalDotOutgoing * -normalDotIncoming));
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
		
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		final Vector3F halfSample = SampleGeneratorF.sampleHemispherePowerCosineDistribution(sample.getU(), sample.getV(), this.exponent);
		final Vector3F half = normalDotOutgoing < 0.0F ? Vector3F.negate(halfSample) : halfSample;
		
		final float outgoingDotHalf = Vector3F.dotProduct(outgoing, half);
		
		final Vector3F incoming = Vector3F.subtract(outgoing, Vector3F.multiply(half, 2.0F * outgoingDotHalf));
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AshikhminShirleyRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code AshikhminShirleyRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new AshikhminShirleyRayitoBRDF(%s, %+.10f)", this.reflectanceScale, Float.valueOf(this.roughness));
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
		} else if(!Objects.equals(this.reflectanceScale, AshikhminShirleyRayitoBRDF.class.cast(object).reflectanceScale)) {
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
		
		final Vector3F half = Vector3F.half(outgoing, normal, incoming);
		
		final float normalDotHalf = Vector3F.dotProduct(normal, half);
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		final float outgoingDotHalf = Vector3F.dotProduct(outgoing, half);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return 0.0F;
		}
		
		return (this.exponent + 1.0F) * pow(abs(normalDotHalf), this.exponent) / (PI * 8.0F * abs(outgoingDotHalf));
	}
	
	/**
	 * Returns a hash code for this {@code AshikhminShirleyRayitoBRDF} instance.
	 * 
	 * @return a hash code for this {@code AshikhminShirleyRayitoBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, Float.valueOf(this.exponent), Float.valueOf(this.roughness));
	}
}