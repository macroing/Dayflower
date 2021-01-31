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
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.lerp;

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
import org.dayflower.scene.fresnel.Schlick;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code DisneyFakeSSBRDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for Disney Fake Subsurface Scattering (SS) reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyFakeSSBRDF extends BXDF {
	private final Color3F reflectanceScale;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyFakeSSBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param roughness a {@code float} that represents the roughness
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public DisneyFakeSSBRDF(final Color3F reflectanceScale, final float roughness) {
		super(BXDFType.DIFFUSE_REFLECTION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.roughness = roughness;
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
		
		final Vector3F n = Vector3F.add(incoming, outgoing);
		
		if(isZero(n.getX()) && isZero(n.getY()) && isZero(n.getZ())) {
			return Color3F.BLACK;
		}
		
		final Vector3F nNormalized = Vector3F.normalize(n);
		
		final float cosThetaD = Vector3F.dotProduct(incoming, nNormalized);
		final float cosThetaAbsOutgoing = outgoing.cosThetaAbs();
		final float cosThetaAbsIncoming = incoming.cosThetaAbs();
		
		final float fresnelSS90 = cosThetaD * cosThetaD * this.roughness;
		final float fresnelOutgoing = Schlick.fresnelWeight(cosThetaAbsOutgoing);
		final float fresnelIncoming = Schlick.fresnelWeight(cosThetaAbsIncoming);
		final float fresnelSS = lerp(1.0F, fresnelSS90, fresnelOutgoing) * lerp(1.0F, fresnelSS90, fresnelIncoming);
		
		final float scaleSS = 1.25F * (fresnelSS * (1.0F / (cosThetaAbsOutgoing + cosThetaAbsIncoming) - 0.5F) + 0.5F);
		
		final float component1 = this.reflectanceScale.getComponent1() * PI_RECIPROCAL * scaleSS;
		final float component2 = this.reflectanceScale.getComponent2() * PI_RECIPROCAL * scaleSS;
		final float component3 = this.reflectanceScale.getComponent3() * PI_RECIPROCAL * scaleSS;
		
		return new Color3F(component1, component2, component3);
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
		final Vector3F incoming = Vector3F.faceForwardComponent3(outgoing, incomingSample);
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyFakeSSBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyFakeSSBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyFakeSSBRDF(%s, %+.10f)", this.reflectanceScale, Float.valueOf(this.roughness));
	}
	
	/**
	 * Compares {@code object} to this {@code DisneyFakeSSBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyFakeSSBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyFakeSSBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyFakeSSBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyFakeSSBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, DisneyFakeSSBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!equal(this.roughness, DisneyFakeSSBRDF.class.cast(object).roughness)) {
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
		
		return Vector3F.sameHemisphereZ(outgoing, incoming) ? incoming.cosThetaAbs() * PI_RECIPROCAL : 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyFakeSSBRDF} instance.
	 * 
	 * @return a hash code for this {@code DisneyFakeSSBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, Float.valueOf(this.roughness));
	}
}