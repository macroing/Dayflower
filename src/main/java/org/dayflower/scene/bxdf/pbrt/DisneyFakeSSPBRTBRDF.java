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
package org.dayflower.scene.bxdf.pbrt;

import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.fresnelSchlickWeight;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.lerp;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code DisneyFakeSSPBRTBRDF} is an implementation of {@link PBRTBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for Disney Fake Subsurface Scattering (SS) reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyFakeSSPBRTBRDF extends PBRTBXDF {
	private final Color3F reflectanceScale;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyFakeSSPBRTBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param roughness a {@code float} that represents the roughness
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public DisneyFakeSSPBRTBRDF(final Color3F reflectanceScale, final float roughness) {
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
	 * If either {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code rho(int nSamples, const Point2f *samples1, const Point2f *samples2)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples2} in PBRT
	 * @param samplesB a {@code List} of {@code Point2F} instances that represents samples, called {@code samples1} in PBRT
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code samplesB} or an element in {@code samplesA} or {@code samplesB} are {@code null}
	 */
	@Override
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final List<Point2F> samplesB) {
//		PBRT: Implementation of DisneyFakeSS.
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		return this.reflectanceScale;
	}
	
	/**
	 * Computes the reflectance function.
	 * <p>
	 * Returns a {@link Color3F} instance with the result of the computation.
	 * <p>
	 * If either {@code samplesA}, {@code outgoing} or an element in {@code samplesA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code rho(const Vector3f &wo, int nSamples, const Point2f *samples)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param samplesA a {@code List} of {@link Point2F} instances that represents samples, called {@code samples} in PBRT
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @return a {@code Color3F} instance with the result of the computation
	 * @throws NullPointerException thrown if, and only if, either {@code samplesA}, {@code outgoing} or an element in {@code samplesA} are {@code null}
	 */
	@Override
	public Color3F computeReflectanceFunction(final List<Point2F> samplesA, final Vector3F outgoing) {
//		PBRT: Implementation of DisneyFakeSS.
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		
		return this.reflectanceScale;
	}
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns a {@link Color3F} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoing} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code f(const Vector3f &wo, const Vector3f &wi)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code Color3F} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code incoming} are {@code null}
	 */
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming) {
//		PBRT: Implementation of DisneyFakeSS.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Vector3F normal = Vector3F.add(incoming, outgoing);
		
		if(isZero(normal.getX()) && isZero(normal.getY()) && isZero(normal.getZ())) {
			return Color3F.BLACK;
		}
		
		final Vector3F normalNormalized = Vector3F.normalize(normal);
		
		final float cosThetaD = Vector3F.dotProduct(incoming, normalNormalized);
		final float cosThetaAbsOutgoing = outgoing.cosThetaAbs();
		final float cosThetaAbsIncoming = incoming.cosThetaAbs();
		
		final float fresnelSS90 = cosThetaD * cosThetaD * this.roughness;
		final float fresnelOutgoing = fresnelSchlickWeight(cosThetaAbsOutgoing);
		final float fresnelIncoming = fresnelSchlickWeight(cosThetaAbsIncoming);
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
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &sample, Float *pdf, BxDFType *sampledType = nullptr)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point
	 * @return an optional {@code BXDFResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<BXDFResult> sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
//		PBRT: Implementation of BxDF.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F incoming = SampleGeneratorF.sampleHemisphereCosineDistribution(sample.getU(), sample.getV());
		final Vector3F incomingCorrectlyOriented = outgoing.getZ() < 0.0F ? new Vector3F(incoming.getX(), incoming.getY(), -incoming.getZ()) : incoming;
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, incomingCorrectlyOriented);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, incomingCorrectlyOriented);
		
		return Optional.of(new BXDFResult(bXDFType, result, incomingCorrectlyOriented, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyFakeSSPBRTBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyFakeSSPBRTBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyFakeSSPBRTBRDF(%s, %+.10f)", this.reflectanceScale, Float.valueOf(this.roughness));
	}
	
	/**
	 * Compares {@code object} to this {@code DisneyFakeSSPBRTBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyFakeSSPBRTBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyFakeSSPBRTBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyFakeSSPBRTBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyFakeSSPBRTBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, DisneyFakeSSPBRTBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!equal(this.roughness, DisneyFakeSSPBRTBRDF.class.cast(object).roughness)) {
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
	 * If either {@code outgoing} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code Pdf(const Vector3f &wo, const Vector3f &wi)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F incoming) {
//		PBRT: Implementation of BxDF.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Vector3F.sameHemisphereZ(outgoing, incoming) ? incoming.cosThetaAbs() * PI_RECIPROCAL : 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyFakeSSPBRTBRDF} instance.
	 * 
	 * @return a hash code for this {@code DisneyFakeSSPBRTBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, Float.valueOf(this.roughness));
	}
}