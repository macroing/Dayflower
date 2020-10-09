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
package org.dayflower.scene.pbrt;

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.random;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Lists;

/**
 * A {@code TorranceSparrowTransmissionBTDF} is an implementation of {@link BXDF} that represents a BTDF (Bidirectional Transmittance Distribution Function) for Torrance-Sparrow transmission using a microfacet distribution.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TorranceSparrowTransmissionBTDF extends BXDF {
	private final Color3F transmittanceScale;
	private final Fresnel fresnel;
	private final MicrofacetDistribution microfacetDistribution;
	private final TransportMode transportMode;
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TorranceSparrowTransmissionBTDF} instance.
	 * <p>
	 * If either {@code transmittanceScale}, {@code microfacetDistribution} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transmittanceScale a {@link Color3F} instance that represents the transmittance scale
	 * @param microfacetDistribution a {@link MicrofacetDistribution} instance
	 * @param transportMode a {@link TransportMode} instance
	 * @param etaA one of the indices of refraction (IOR)
	 * @param etaB one of the indices of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code transmittanceScale}, {@code microfacetDistribution} or {@code transportMode} are {@code null}
	 */
	public TorranceSparrowTransmissionBTDF(final Color3F transmittanceScale, final MicrofacetDistribution microfacetDistribution, final TransportMode transportMode, final float etaA, final float etaB) {
		super(BXDFType.GLOSSY_TRANSMISSION);
		
		this.transmittanceScale = Objects.requireNonNull(transmittanceScale, "transmittanceScale == null");
		this.fresnel = new DielectricFresnel(etaA, etaB);
		this.microfacetDistribution = Objects.requireNonNull(microfacetDistribution, "microfacetDistribution == null");
		this.transportMode = Objects.requireNonNull(transportMode, "transportMode == null");
		this.etaA = etaA;
		this.etaB = etaB;
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
//		PBRT: Implementation of BxDF.
		
		Lists.requireNonNullList(samplesA, "samplesA");
		Lists.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			final Point2F sampleB = i < samplesB.size() ? samplesB.get(i) : new Point2F(random(), random());
			
			final Vector3F outgoing = SampleGeneratorF.sampleHemisphereUniformDistribution(sampleB.getU(), sampleB.getV());
			
			final Optional<BXDFDistributionFunctionResult> optionalBXDFDistributionFunctionResult = sampleDistributionFunction(outgoing, sampleA);
			
			if(optionalBXDFDistributionFunctionResult.isPresent()) {
				final BXDFDistributionFunctionResult bXDFDistributionFunctionResult = optionalBXDFDistributionFunctionResult.get();
				
				final float probabilityDensityFunctionValueIncoming = bXDFDistributionFunctionResult.getProbabilityDensityFunctionValue();
				final float probabilityDensityFunctionValueOutgoing = SampleGeneratorF.hemisphereUniformDistributionProbabilityDensityFunction();
				
				if(probabilityDensityFunctionValueIncoming > 0.0F) {
					final Color3F result = bXDFDistributionFunctionResult.getResult();
					
					final Vector3F incoming = bXDFDistributionFunctionResult.getIncoming();
					
					reflectance = Color3F.add(reflectance, Color3F.divide(Color3F.multiply(Color3F.multiply(result, incoming.cosThetaAbs()), outgoing.cosThetaAbs()), probabilityDensityFunctionValueOutgoing * probabilityDensityFunctionValueIncoming));
				}
			}
		}
		
		return Color3F.divide(reflectance, PI * samplesA.size());
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
//		PBRT: Implementation of BxDF.
		
		Lists.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			
			final Optional<BXDFDistributionFunctionResult> optionalBXDFDistributionFunctionResult = sampleDistributionFunction(outgoing, sampleA);
			
			if(optionalBXDFDistributionFunctionResult.isPresent()) {
				final BXDFDistributionFunctionResult bXDFDistributionFunctionResult = optionalBXDFDistributionFunctionResult.get();
				
				final float probabilityDensityFunctionValue = bXDFDistributionFunctionResult.getProbabilityDensityFunctionValue();
				
				if(probabilityDensityFunctionValue > 0.0F) {
					final Color3F result = bXDFDistributionFunctionResult.getResult();
					
					final Vector3F incoming = bXDFDistributionFunctionResult.getIncoming();
					
					reflectance = Color3F.add(reflectance, Color3F.divide(Color3F.multiply(result, incoming.cosThetaAbs()), probabilityDensityFunctionValue));
				}
			}
		}
		
		return Color3F.divide(reflectance, samplesA.size());
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
//		PBRT: Implementation of MicrofacetTransmission.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(Vector3F.sameHemisphere(outgoing, incoming)) {
			return Color3F.BLACK;
		}
		
		final float cosThetaIncoming = incoming.cosTheta();
		final float cosThetaOutgoing = outgoing.cosTheta();
		
		if(equal(cosThetaIncoming, 0.0F) || equal(cosThetaOutgoing, 0.0F)) {
			return Color3F.BLACK;
		}
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = cosThetaOutgoing > 0.0F ? etaB / etaA : etaA / etaB;
		
		final Vector3F normalTemporary = Vector3F.normalize(Vector3F.add(Vector3F.multiply(incoming, eta), outgoing));
		final Vector3F normal = normalTemporary.getZ() < 0.0F ? Vector3F.negate(normalTemporary) : normalTemporary;
		
		final float outgoingDotNormal = Vector3F.dotProduct(outgoing, normal);
		final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
		
		if(outgoingDotNormal * incomingDotNormal > 0.0F) {
			return Color3F.BLACK;
		}
		
		final Color3F f = this.fresnel.evaluate(outgoingDotNormal);
		final Color3F t = this.transmittanceScale;
		
		final float a = outgoingDotNormal + eta * incomingDotNormal;
		final float b = this.transportMode == TransportMode.RADIANCE ? 1.0F / eta : 1.0F;
		
		final float d = this.microfacetDistribution.computeDifferentialArea(normal);
		final float g = this.microfacetDistribution.computeShadowingAndMasking(outgoing, incoming);
		
		return Color3F.multiply(Color3F.multiply(Color3F.subtract(Color3F.WHITE, f), t), abs(d * g * eta * eta * abs(incomingDotNormal) * abs(outgoingDotNormal) * b * b / (cosThetaIncoming * cosThetaOutgoing * a * a)));
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@link BXDFDistributionFunctionResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &sample, Float *pdf, BxDFType *sampledType = nullptr)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point
	 * @return an optional {@code BXDFDistributionFunctionResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<BXDFDistributionFunctionResult> sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
//		PBRT: Implementation of MicrofacetTransmission.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		if(equal(outgoing.getZ(), 0.0F)) {
			return Optional.empty();
		}
		
		final Vector3F normal = this.microfacetDistribution.sampleNormal(outgoing, sample);
		
		if(Vector3F.dotProduct(outgoing, normal) < 0.0F) {
			return Optional.empty();
		}
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = outgoing.cosTheta() > 0.0F ? etaA / etaB : etaB / etaA;
		
		final Optional<Vector3F> optionalIncoming = Vector3F.refraction(outgoing, normal, eta);
		
		if(!optionalIncoming.isPresent()) {
			return Optional.empty();
		}
		
		final Vector3F incoming = optionalIncoming.get();
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, incoming);
		
		return Optional.of(new BXDFDistributionFunctionResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TorranceSparrowTransmissionBTDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code TorranceSparrowTransmissionBTDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new TorranceSparrowTransmissionBTDF(%s, %s, %s, %+.10f, %+.10f)", this.transmittanceScale, this.microfacetDistribution, this.transportMode, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
	
	/**
	 * Compares {@code object} to this {@code TorranceSparrowTransmissionBTDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TorranceSparrowTransmissionBTDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TorranceSparrowTransmissionBTDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TorranceSparrowTransmissionBTDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TorranceSparrowTransmissionBTDF)) {
			return false;
		} else if(!Objects.equals(this.transmittanceScale, TorranceSparrowTransmissionBTDF.class.cast(object).transmittanceScale)) {
			return false;
		} else if(!Objects.equals(this.fresnel, TorranceSparrowTransmissionBTDF.class.cast(object).fresnel)) {
			return false;
		} else if(!Objects.equals(this.microfacetDistribution, TorranceSparrowTransmissionBTDF.class.cast(object).microfacetDistribution)) {
			return false;
		} else if(!Objects.equals(this.transportMode, TorranceSparrowTransmissionBTDF.class.cast(object).transportMode)) {
			return false;
		} else if(!equal(this.etaA, TorranceSparrowTransmissionBTDF.class.cast(object).etaA)) {
			return false;
		} else if(!equal(this.etaB, TorranceSparrowTransmissionBTDF.class.cast(object).etaB)) {
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
//		PBRT: Implementation of MicrofacetTransmission.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(Vector3F.sameHemisphere(outgoing, incoming)) {
			return 0.0F;
		}
		
		final float etaA = this.etaA;
		final float etaB = this.etaB;
		final float eta = outgoing.cosTheta() > 0.0F ? etaB / etaA : etaA / etaB;
		
		final Vector3F normal = Vector3F.normalize(Vector3F.add(Vector3F.multiply(incoming, eta), outgoing));
		
		final float outgoingDotNormal = Vector3F.dotProduct(outgoing, normal);
		final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
		
		if(outgoingDotNormal * incomingDotNormal > 0.0F) {
			return 0.0F;
		}
		
		final float a = outgoingDotNormal + eta * incomingDotNormal;
		final float b = abs((eta * eta * incomingDotNormal) / (a * a));
		
		return this.microfacetDistribution.computeProbabilityDensityFunctionValue(outgoing, normal) * b;
	}
	
	/**
	 * Returns a hash code for this {@code TorranceSparrowTransmissionBTDF} instance.
	 * 
	 * @return a hash code for this {@code TorranceSparrowTransmissionBTDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.transmittanceScale, this.fresnel, this.microfacetDistribution, this.transportMode, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
}