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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.BXDFResult;
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
	private final Color3F reflectanceScale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LambertianRayitoBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public LambertianRayitoBRDF(final Color3F reflectanceScale) {
		super(BXDFType.DIFFUSE_REFLECTION_AND_TRANSMISSION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
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
		
		final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
		final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
		
		if(normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F) {
			return Color3F.BLACK;
		}
		
		return Color3F.multiply(this.reflectanceScale, PI_RECIPROCAL);
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
		
		final Vector3F incomingSample = Vector3F.negate(SampleGeneratorF.sampleHemisphereCosineDistribution(sample.getU(), sample.getV()));
		final Vector3F incoming = normalDotOutgoing < 0.0F ? Vector3F.negate(incomingSample) : incomingSample;
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LambertianRayitoBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code LambertianRayitoBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new LambertianRayitoBRDF(%s)", this.reflectanceScale);
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
		} else if(!Objects.equals(this.reflectanceScale, LambertianRayitoBRDF.class.cast(object).reflectanceScale)) {
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
		return Objects.hash(this.reflectanceScale);
	}
}