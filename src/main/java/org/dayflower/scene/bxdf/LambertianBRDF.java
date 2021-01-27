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
package org.dayflower.scene.bxdf;

import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.abs;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code LambertianBRDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for Lambertian reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LambertianBRDF extends BXDF {
	private final Color3F reflectanceScale;
	private final boolean isNegatingIncoming;
	private final boolean isUsingNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	

	/**
	 * Constructs a new {@code LambertianBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LambertianBRDF(reflectanceScale, false, false);
	 * }
	 * </pre>
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public LambertianBRDF(final Color3F reflectanceScale) {
		this(reflectanceScale, false, false);
	}
	
	/**
	 * Constructs a new {@code LambertianBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param isNegatingIncoming {@code true} if, and only if, the incoming direction should be negated, {@code false} otherwise
	 * @param isUsingNormal {@code true} if, and only if, the normal supplied should be used, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public LambertianBRDF(final Color3F reflectanceScale, final boolean isNegatingIncoming, final boolean isUsingNormal) {
		super(BXDFType.DIFFUSE_REFLECTION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.isNegatingIncoming = isNegatingIncoming;
		this.isUsingNormal = isUsingNormal;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code samplesA}, {@code samplesB}, {@code normal} or an element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples
	 * @param samplesB a {@code List} of {@code Point2F} instances that represents samples
	 * @param normal the normal
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code samplesB}, {@code normal} or an element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	@Override
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final List<Point2F> samplesB, final Vector3F normal) {
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		Objects.requireNonNull(normal, "normal == null");
		
		return this.reflectanceScale;
	}
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code samplesA}, {@code outgoing}, {@code normal} or an element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples
	 * @param outgoing the outgoing direction
	 * @param normal the normal
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code outgoing}, {@code normal} or an element in {@code samplesA} are {@code null}
	 */
	@Override
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final Vector3F outgoing, final Vector3F normal) {
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		
		return this.reflectanceScale;
	}
	
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
		
		if(doCheckAngles(outgoing, normal, incoming)) {
			return Color3F.multiply(this.reflectanceScale, PI_RECIPROCAL);
		}
		
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
		
		final Vector3F incomingSample = SampleGeneratorF.sampleHemisphereCosineDistribution(sample.getU(), sample.getV());
		final Vector3F incomingSampleCorrectlyOriented = this.isNegatingIncoming ? Vector3F.negate(incomingSample) : incomingSample;
		final Vector3F incoming = this.isUsingNormal ? Vector3F.dotProduct(normal, outgoing) < 0.0F ? Vector3F.negate(incomingSampleCorrectlyOriented) : incomingSampleCorrectlyOriented : outgoing.getZ() < 0.0F ? Vector3F.negateComponent3(incomingSampleCorrectlyOriented) : incomingSampleCorrectlyOriented;
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LambertianBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code LambertianBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new LambertianBRDF(%s)", this.reflectanceScale);
	}
	
	/**
	 * Compares {@code object} to this {@code LambertianBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LambertianBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LambertianBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LambertianBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LambertianBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, LambertianBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(this.isNegatingIncoming != LambertianBRDF.class.cast(object).isNegatingIncoming) {
			return false;
		} else if(this.isUsingNormal != LambertianBRDF.class.cast(object).isUsingNormal) {
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
		
		if(doCheckAngles(outgoing, normal, incoming)) {
			return (this.isUsingNormal ? abs(Vector3F.dotProduct(normal, incoming)) : incoming.cosThetaAbs()) * PI_RECIPROCAL;
		}
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code LambertianBRDF} instance.
	 * 
	 * @return a hash code for this {@code LambertianBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, Boolean.valueOf(this.isNegatingIncoming), Boolean.valueOf(this.isUsingNormal));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doCheckAngles(final Vector3F outgoing, final Vector3F normal, final Vector3F incoming) {
		if(this.isUsingNormal) {
			final float normalDotIncoming = Vector3F.dotProduct(normal, incoming);
			final float normalDotOutgoing = Vector3F.dotProduct(normal, outgoing);
			
			if(this.isNegatingIncoming && (normalDotIncoming > 0.0F && normalDotOutgoing > 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing < 0.0F)) {
				return false;
			}
			
			if(!this.isNegatingIncoming && (normalDotIncoming > 0.0F && normalDotOutgoing < 0.0F || normalDotIncoming < 0.0F && normalDotOutgoing > 0.0F)) {
				return false;
			}
			
			return true;
		}
		
		return this.isNegatingIncoming ? !Vector3F.sameHemisphereZ(outgoing, incoming) : Vector3F.sameHemisphereZ(outgoing, incoming);
	}
}