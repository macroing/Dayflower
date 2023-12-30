/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.random;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code FresnelSpecularBXDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) and a BTDF (Bidirectional Transmittance Distribution Function) for specular reflection and transmission.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code BXDF} implementation is not supported on the GPU.
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
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			final Point2F sampleB = i < samplesB.size() ? samplesB.get(i) : new Point2F(random(), random());
			
			final Vector3F outgoing = SampleGeneratorF.sampleHemisphereUniformDistribution(sampleB.x, sampleB.y);
			
			final Optional<BXDFResult> optionalBXDFResult = sampleDistributionFunction(outgoing, normal, sampleA);
			
			if(optionalBXDFResult.isPresent()) {
				final BXDFResult bXDFResult = optionalBXDFResult.get();
				
				final float probabilityDensityFunctionValueIncoming = bXDFResult.getProbabilityDensityFunctionValue();
				final float probabilityDensityFunctionValueOutgoing = SampleGeneratorF.hemisphereUniformDistributionProbabilityDensityFunction();
				
				if(probabilityDensityFunctionValueIncoming > 0.0F) {
					final Color3F result = bXDFResult.getResult();
					
					final Vector3F incoming = bXDFResult.getIncoming();
					
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
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			
			final Optional<BXDFResult> optionalBXDFResult = sampleDistributionFunction(outgoing, normal, sampleA);
			
			if(optionalBXDFResult.isPresent()) {
				final BXDFResult bXDFResult = optionalBXDFResult.get();
				
				final float probabilityDensityFunctionValue = bXDFResult.getProbabilityDensityFunctionValue();
				
				if(probabilityDensityFunctionValue > 0.0F) {
					final Color3F result = bXDFResult.getResult();
					
					final Vector3F incoming = bXDFResult.getIncoming();
					
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
		
		final float reflectance = DielectricFresnel.evaluate(outgoing.cosTheta(), this.etaA, this.etaB);
		
		if(sample.x < reflectance) {
			final BXDFType bXDFType = BXDFType.SPECULAR_REFLECTION;
			
			final Vector3F incoming = new Vector3F(-outgoing.x, -outgoing.y, outgoing.z);
			
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