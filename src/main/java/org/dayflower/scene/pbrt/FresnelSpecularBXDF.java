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
 * A {@code FresnelSpecularBXDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) and a BTDF (Bidirectional Transmittance Distribution Function) for specular reflection and transmission.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FresnelSpecularBXDF extends BXDF {
	private final Color3F reflectanceScale;
	private final Color3F transmittanceScale;
	private final TransportMode transportMode;
	private final float etaA;
	private final float etaB;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FresnelSpecularBXDF} instance.
	 * <p>
	 * If either {@code reflectanceScale}, {@code transmittanceScale} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param transmittanceScale a {@code Color3F} instance that represents the transmittance scale
	 * @param transportMode a {@link TransportMode} instance
	 * @param etaA one of the indices of refraction (IOR)
	 * @param etaB one of the indices of refraction (IOR)
	 * @throws NullPointerException thrown if, and only if, either {@code reflectanceScale}, {@code transmittanceScale} or {@code transportMode} are {@code null}
	 */
	public FresnelSpecularBXDF(final Color3F reflectanceScale, final Color3F transmittanceScale, final TransportMode transportMode, final float etaA, final float etaB) {
		super(BXDFType.SPECULAR_REFLECTION_AND_TRANSMISSION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.transmittanceScale = Objects.requireNonNull(transmittanceScale, "transmittanceScale == null");
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
			
			final Optional<BXDFResult> optionalBXDFDistributionFunctionResult = sampleDistributionFunction(outgoing, sampleA);
			
			if(optionalBXDFDistributionFunctionResult.isPresent()) {
				final BXDFResult bXDFDistributionFunctionResult = optionalBXDFDistributionFunctionResult.get();
				
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
			
			final Optional<BXDFResult> optionalBXDFDistributionFunctionResult = sampleDistributionFunction(outgoing, sampleA);
			
			if(optionalBXDFDistributionFunctionResult.isPresent()) {
				final BXDFResult bXDFDistributionFunctionResult = optionalBXDFDistributionFunctionResult.get();
				
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
//		PBRT: Implementation of FresnelSpecular.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Color3F.BLACK;
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
//		PBRT: Implementation of FresnelSpecular.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final float reflectance = DielectricFresnel.evaluate(outgoing.cosTheta(), this.etaA, this.etaB);
		
		if(sample.getU() < reflectance) {
			final BXDFType bXDFType = BXDFType.SPECULAR_REFLECTION;
			
			final Vector3F incoming = new Vector3F(-outgoing.getX(), -outgoing.getY(), outgoing.getZ());
			
			final Color3F result = Color3F.divide(Color3F.multiply(this.reflectanceScale, reflectance), incoming.cosThetaAbs());
			
			final float probabilityDensityFunctionValue = reflectance;
			
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final boolean isEntering = outgoing.cosTheta() > 0.0F;
		
		final float etaI = isEntering ? this.etaA : this.etaB;
		final float etaT = isEntering ? this.etaB : this.etaA;
		
		final Optional<Vector3F> optionalIncoming = Vector3F.refraction(outgoing, Vector3F.faceForward(Vector3F.z(), outgoing), etaI / etaT);
		
		if(optionalIncoming.isPresent()) {
			final BXDFType bXDFType = BXDFType.SPECULAR_TRANSMISSION;
			
			final Vector3F incoming = optionalIncoming.get();
			
			final Color3F result = doComputeResult(incoming, etaI, etaT, reflectance);
			
			final float probabilityDensityFunctionValue = 1.0F - reflectance;
			
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FresnelSpecularBXDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code FresnelSpecularBXDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new FresnelSpecularBXDF(%s, %s, %s, %+.10f, %+.10f)", this.reflectanceScale, this.transmittanceScale, this.transportMode, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
	
	/**
	 * Compares {@code object} to this {@code FresnelSpecularBXDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FresnelSpecularBXDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FresnelSpecularBXDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FresnelSpecularBXDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FresnelSpecularBXDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, FresnelSpecularBXDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!Objects.equals(this.transmittanceScale, FresnelSpecularBXDF.class.cast(object).transmittanceScale)) {
			return false;
		} else if(!Objects.equals(this.transportMode, FresnelSpecularBXDF.class.cast(object).transportMode)) {
			return false;
		} else if(!equal(this.etaA, FresnelSpecularBXDF.class.cast(object).etaA)) {
			return false;
		} else if(!equal(this.etaB, FresnelSpecularBXDF.class.cast(object).etaB)) {
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
//		PBRT: Implementation of FresnelSpecular.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code FresnelSpecularBXDF} instance.
	 * 
	 * @return a hash code for this {@code FresnelSpecularBXDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, this.transmittanceScale, this.transportMode, Float.valueOf(this.etaA), Float.valueOf(this.etaB));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doComputeResult(final Vector3F incoming, final float etaI, final float etaT, final float reflectance) {
		switch(this.transportMode) {
			case IMPORTANCE:
				return Color3F.divide(Color3F.multiply(this.transmittanceScale, 1.0F - reflectance), incoming.cosThetaAbs());
			case RADIANCE:
				return Color3F.divide(Color3F.multiply(Color3F.multiply(this.transmittanceScale, 1.0F - reflectance), (etaI * etaI) / (etaT * etaT)), incoming.cosThetaAbs());
			default:
				return Color3F.divide(Color3F.multiply(this.transmittanceScale, 1.0F - reflectance), incoming.cosThetaAbs());
		}
	}
}