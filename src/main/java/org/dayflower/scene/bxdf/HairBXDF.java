/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.asin;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.exp;
import static org.dayflower.utility.Floats.log;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.powR;
import static org.dayflower.utility.Floats.random;
import static org.dayflower.utility.Floats.saturate;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.sinh;
import static org.dayflower.utility.Floats.sqrt;
import static org.dayflower.utility.Floats.toRadians;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.Color3F;
import org.macroing.art4j.color.ColorSpaceF;

/**
 * A {@code HairBXDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) and a BTDF (Bidirectional Transmittance Distribution Function) for hair.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code BXDF} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HairBXDF extends BXDF {
	private final Color3F sigmaA;
	private final float alpha;
	private final float betaM;
	private final float betaN;
	private final float eta;
	private final float gammaOutgoing;
	private final float h;
	private final float s;
	private final float[] cos2KAlpha;
	private final float[] sin2KAlpha;
	private final float[] v;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code HairBXDF} instance.
	 * <p>
	 * If {@code sigmaA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sigmaA a {@link Color3F} instance
	 * @param alpha a {@code float} value
	 * @param betaM a {@code float} value
	 * @param betaN a {@code float} value
	 * @param eta a {@code float} value with the index of refraction (IOR)
	 * @param h a {@code float} value
	 * @throws NullPointerException thrown if, and only if, {@code sigmaA} is {@code null}
	 */
	public HairBXDF(final Color3F sigmaA, final float alpha, final float betaM, final float betaN, final float eta, final float h) {
		super(BXDFType.GLOSSY_REFLECTION_AND_TRANSMISSION);
		
		this.sigmaA = Objects.requireNonNull(sigmaA, "sigmaA == null");
		this.alpha = alpha;
		this.betaM = betaM;
		this.betaN = betaN;
		this.eta = eta;
		this.gammaOutgoing = asin(saturate(h, -1.0F, 1.0F));
		this.h = h;
		this.s = 0.626657069F * (0.265F * betaN + 1.194F * (betaN * betaN) + 5.372F * powR(betaN, 22));
		this.cos2KAlpha = new float[3];
		this.sin2KAlpha = new float[3];
		this.v = new float[4];
		this.v[0] = (0.726F * betaM + 0.812F * (betaM * betaM) + 3.7F * powR(betaM, 20)) * (0.726F * betaM + 0.812F * (betaM * betaM) + 3.7F * powR(betaM, 20));
		this.v[1] = 0.25F * this.v[0];
		this.v[2] = 4.0F * this.v[0];
		this.v[3] = this.v[2];
		
		for(int i = 0; i < 3; i++) {
			if(i == 0) {
				this.sin2KAlpha[i] = sin(toRadians(alpha));
				this.cos2KAlpha[i] = sqrt(max(0.0F, 1.0F - this.sin2KAlpha[i] * this.sin2KAlpha[i]));
			} else {
				this.sin2KAlpha[i] = 2.0F * this.cos2KAlpha[i - 1] * this.sin2KAlpha[i - 1];
				this.cos2KAlpha[i] = this.cos2KAlpha[i - 1] * this.cos2KAlpha[i - 1] - this.sin2KAlpha[i - 1] * this.sin2KAlpha[i - 1];
			}
		}
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
		
		final float sinThetaOutgoing = outgoing.x;
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.z, outgoing.y);
		
		final float sinThetaIncoming = incoming.x;
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		final float phiIncoming = atan2(incoming.z, incoming.y);
		
		final float sinThetaTransmission = sinThetaOutgoing / this.eta;
		final float cosThetaTransmission = sqrt(max(0.0F, 1.0F - sinThetaTransmission * sinThetaTransmission));
		
		final float etap = sqrt(this.eta * this.eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		final float sinGammaTransmission = this.h / etap;
		final float cosGammaTransmission = sqrt(max(0.0F, 1.0F - sinGammaTransmission * sinGammaTransmission));
		final float gammaTransmission = asin(saturate(sinGammaTransmission, -1.0F, 1.0F));
		
		final float scale = 2.0F * cosGammaTransmission / cosThetaTransmission;
		
		final Color3F transmittance = new Color3F(exp(-this.sigmaA.r * scale), exp(-this.sigmaA.g * scale), exp(-this.sigmaA.b * scale));
		
		final float phi = phiIncoming - phiOutgoing;
		
		final Color3F[] colors = doComputeAP(transmittance, cosThetaOutgoing, this.eta, this.h);
		
		Color3F result = Color3F.BLACK;
		
		for(int i = 0; i < 3; i++) {
			float sinThetaOutgoingP = 0.0F;
			float cosThetaOutgoingP = 0.0F;
			
			switch(i) {
				case 0:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[1] - cosThetaOutgoing * this.sin2KAlpha[1];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[1] + sinThetaOutgoing * this.sin2KAlpha[1];
					
					break;
				case 1:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[0] + cosThetaOutgoing * this.sin2KAlpha[0];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[0] - sinThetaOutgoing * this.sin2KAlpha[0];
					
					break;
				case 2:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[2] + cosThetaOutgoing * this.sin2KAlpha[2];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[2] - sinThetaOutgoing * this.sin2KAlpha[2];
					
					break;
				default:
					sinThetaOutgoingP = sinThetaOutgoing;
					cosThetaOutgoingP = cosThetaOutgoing;
					
					break;
			}
			
			cosThetaOutgoingP = abs(cosThetaOutgoingP);
			
			result = Color3F.add(result, Color3F.multiply(Color3F.multiply(colors[i], doMP(cosThetaIncoming, cosThetaOutgoingP, sinThetaIncoming, sinThetaOutgoingP, this.v[i])), doNP(this.gammaOutgoing, gammaTransmission, phi, this.s, i)));
		}
		
		result = Color3F.add(result, Color3F.divide(Color3F.multiply(colors[3], doMP(cosThetaIncoming, cosThetaOutgoing, sinThetaIncoming, sinThetaOutgoing, this.v[3])), 2.0F * PI));
		
		if(incoming.cosThetaAbs() > 0.0F) {
			result = Color3F.divide(result, incoming.cosThetaAbs());
		}
		
		return result;
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
		
		final float sinThetaOutgoing = outgoing.x;
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.z, outgoing.y);
		
		final Point2F[] samples = new Point2F[] {Point2F.decodeMortonCode1By1(sample.x), Point2F.decodeMortonCode1By1(sample.y)};
		
		final float[] probabilityDensityFunctionValues = doComputeAPProbabilityDensityFunctionValues(this.sigmaA, cosThetaOutgoing, this.eta, this.h);
		
		int p = 0;
		
		for(int i = 0; i < 3; i++) {
			if(samples[0].x < probabilityDensityFunctionValues[i]) {
				p = i;
				
				break;
			}
			
			samples[0] = new Point2F(samples[0].x - probabilityDensityFunctionValues[i], samples[0].y);
		}
		
		float sinThetaOutgoingP = 0.0F;
		float cosThetaOutgoingP = 0.0F;
		
		switch(p) {
			case 0:
				sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[1] - cosThetaOutgoing * this.sin2KAlpha[1];
				cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[1] + sinThetaOutgoing * this.sin2KAlpha[1];
				
				break;
			case 1:
				sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[0] + cosThetaOutgoing * this.sin2KAlpha[0];
				cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[0] - sinThetaOutgoing * this.sin2KAlpha[0];
				
				break;
			case 2:
				sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[2] + cosThetaOutgoing * this.sin2KAlpha[2];
				cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[2] - sinThetaOutgoing * this.sin2KAlpha[2];
				
				break;
			default:
				sinThetaOutgoingP = sinThetaOutgoing;
				cosThetaOutgoingP = cosThetaOutgoing;
				
				break;
		}
		
		samples[1] = new Point2F(max(samples[1].x, 1.0e-5F), samples[1].y);
		
		final float cosTheta = 1.0F + this.v[p] * log(samples[1].x + (1.0F - samples[1].x) * exp(-2.0F / this.v[p]));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float cosPhi = cos(2.0F * PI * samples[1].y);
		final float sinThetaIncoming = -cosTheta * sinThetaOutgoingP + sinTheta * cosPhi * cosThetaOutgoingP;
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		
		final float etap = sqrt(this.eta * this.eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		final float sinGammaTransmission = this.h / etap;
		final float gammaTransmission = asin(saturate(sinGammaTransmission, -1.0F, 1.0F));
		final float deltaPhi = p < 3 ? doComputePhi(this.gammaOutgoing, gammaTransmission, p) + doLogisticTrimmedSample(samples[0].y, this.s, -PI, PI) : 2.0F * PI * samples[0].y;
		final float phiIncoming = phiOutgoing + deltaPhi;
		
		final Vector3F incoming = new Vector3F(sinThetaIncoming, cosThetaIncoming * cos(phiIncoming), cosThetaIncoming * sin(phiIncoming));
		
		float probabilityDensityFunctionValue = 0.0F;
		
		for(int i = 0; i < 3; i++) {
			sinThetaOutgoingP = 0.0F;
			cosThetaOutgoingP = 0.0F;
			
			switch(i) {
				case 0:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[1] - cosThetaOutgoing * this.sin2KAlpha[1];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[1] + sinThetaOutgoing * this.sin2KAlpha[1];
					
					break;
				case 1:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[0] + cosThetaOutgoing * this.sin2KAlpha[0];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[0] - sinThetaOutgoing * this.sin2KAlpha[0];
					
					break;
				case 2:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[2] + cosThetaOutgoing * this.sin2KAlpha[2];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[2] - sinThetaOutgoing * this.sin2KAlpha[2];
					
					break;
				default:
					sinThetaOutgoingP = sinThetaOutgoing;
					cosThetaOutgoingP = cosThetaOutgoing;
					
					break;
			}
			
			cosThetaOutgoingP = abs(cosThetaOutgoingP);
			
			probabilityDensityFunctionValue += doMP(cosThetaIncoming, cosThetaOutgoingP, sinThetaIncoming, sinThetaOutgoingP, this.v[i]) * probabilityDensityFunctionValues[i] * doNP(this.gammaOutgoing, gammaTransmission, deltaPhi, this.s, i);
		}
		
		probabilityDensityFunctionValue += doMP(cosThetaIncoming, cosThetaOutgoing, sinThetaIncoming, sinThetaOutgoing, this.v[3]) * probabilityDensityFunctionValues[3] * (1.0F / (2.0F * PI));
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code HairBXDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code HairBXDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new HairBXDF(%s, %+.10f, %+.10f, %+.10f, %+.10f, %+.10f)", this.sigmaA, Float.valueOf(this.alpha), Float.valueOf(this.betaM), Float.valueOf(this.betaN), Float.valueOf(this.eta), Float.valueOf(this.h));
	}
	
	/**
	 * Compares {@code object} to this {@code HairBXDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code HairBXDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code HairBXDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code HairBXDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof HairBXDF)) {
			return false;
		} else if(!Objects.equals(this.sigmaA, HairBXDF.class.cast(object).sigmaA)) {
			return false;
		} else if(!equal(this.alpha, HairBXDF.class.cast(object).alpha)) {
			return false;
		} else if(!equal(this.betaM, HairBXDF.class.cast(object).betaM)) {
			return false;
		} else if(!equal(this.betaN, HairBXDF.class.cast(object).betaN)) {
			return false;
		} else if(!equal(this.eta, HairBXDF.class.cast(object).eta)) {
			return false;
		} else if(!equal(this.gammaOutgoing, HairBXDF.class.cast(object).gammaOutgoing)) {
			return false;
		} else if(!equal(this.h, HairBXDF.class.cast(object).h)) {
			return false;
		} else if(!equal(this.s, HairBXDF.class.cast(object).s)) {
			return false;
		} else if(!Arrays.equals(this.cos2KAlpha, HairBXDF.class.cast(object).cos2KAlpha)) {
			return false;
		} else if(!Arrays.equals(this.sin2KAlpha, HairBXDF.class.cast(object).sin2KAlpha)) {
			return false;
		} else if(!Arrays.equals(this.v, HairBXDF.class.cast(object).v)) {
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
		
		final float sinThetaOutgoing = outgoing.x;
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.z, outgoing.y);
		
		final float sinThetaIncoming = incoming.x;
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		final float phiIncoming = atan2(incoming.z, incoming.y);
		
		final float etap = sqrt(this.eta * this.eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		final float sinGammaTransmission = this.h / etap;
		final float gammaTransmission = asin(saturate(sinGammaTransmission, -1.0F, 1.0F));
		
		final float[] probabilityDensityFunctionValues = doComputeAPProbabilityDensityFunctionValues(this.sigmaA, cosThetaOutgoing, this.eta, this.h);
		
		final float phi = phiIncoming - phiOutgoing;
		
		float probabilityDensityFunctionValue = 0.0F;
		
		for(int i = 0; i < 3; i++) {
			float sinThetaOutgoingP = 0.0F;
			float cosThetaOutgoingP = 0.0F;
			
			switch(i) {
				case 0:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[1] - cosThetaOutgoing * this.sin2KAlpha[1];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[1] + sinThetaOutgoing * this.sin2KAlpha[1];
					
					break;
				case 1:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[0] + cosThetaOutgoing * this.sin2KAlpha[0];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[0] - sinThetaOutgoing * this.sin2KAlpha[0];
					
					break;
				case 2:
					sinThetaOutgoingP = sinThetaOutgoing * this.cos2KAlpha[2] + cosThetaOutgoing * this.sin2KAlpha[2];
					cosThetaOutgoingP = cosThetaOutgoing * this.cos2KAlpha[2] - sinThetaOutgoing * this.sin2KAlpha[2];
					
					break;
				default:
					sinThetaOutgoingP = sinThetaOutgoing;
					cosThetaOutgoingP = cosThetaOutgoing;
					
					break;
			}
			
			cosThetaOutgoingP = abs(cosThetaOutgoingP);
			
			probabilityDensityFunctionValue += doMP(cosThetaIncoming, cosThetaOutgoingP, sinThetaIncoming, sinThetaOutgoingP, this.v[i]) * probabilityDensityFunctionValues[i] * doNP(this.gammaOutgoing, gammaTransmission, phi, this.s, i);
		}
		
		probabilityDensityFunctionValue += doMP(cosThetaIncoming, cosThetaOutgoing, sinThetaIncoming, sinThetaOutgoing, this.v[3]) * probabilityDensityFunctionValues[3] * (1.0F / (2.0F * PI));
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code HairBXDF} instance.
	 * 
	 * @return a hash code for this {@code HairBXDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.sigmaA, Float.valueOf(this.alpha), Float.valueOf(this.betaM), Float.valueOf(this.betaN), Float.valueOf(this.eta), Float.valueOf(this.gammaOutgoing), Float.valueOf(this.h), Float.valueOf(this.s), Integer.valueOf(Arrays.hashCode(this.cos2KAlpha)), Integer.valueOf(Arrays.hashCode(this.sin2KAlpha)), Integer.valueOf(Arrays.hashCode(this.v)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes Sigma A from concentration.
	 * <p>
	 * Returns a {@link Color3F} instance with Sigma A.
	 * 
	 * @param colorEumelanin the eumelanin to use
	 * @param colorPheomelanin the pheomelanin to use
	 * @return a {@code Color3F} instance with Sigma A
	 */
	public static Color3F computeSigmaAFromConcentration(final float colorEumelanin, final float colorPheomelanin) {
		final float[] sigmaA = new float[3];
		final float[] sigmaAEumelanin = new float[] {0.419F, 0.697F, 1.37F};
		final float[] sigmaAPheomelanin = new float[] {0.187F, 0.4F, 1.05F};
		
		for(int i = 0; i < 3; i++) {
			sigmaA[i] = colorEumelanin * sigmaAEumelanin[i] + colorPheomelanin * sigmaAPheomelanin[i];
		}
		
		return ColorSpaceF.getDefault().convertRGBToXYZ(new Color3F(sigmaA[0], sigmaA[1], sigmaA[2]));
	}
	
	/**
	 * Computes Sigma A from reflectance.
	 * <p>
	 * Returns a {@link Color3F} instance with Sigma A.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @param betaN a {@code float} value
	 * @return a {@code Color3F} instance with Sigma A
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F computeSigmaAFromReflectance(final Color3F color, final float betaN) {
		final float constant = (5.969F - 0.215F * betaN + 2.532F * (betaN * betaN) - 10.73F * powR(betaN, 3) + 5.574F * powR(betaN, 4) + 0.245F * powR(betaN, 5));
		
		final float component1 = log(color.r) / constant;
		final float component2 = log(color.g) / constant;
		final float component3 = log(color.b) / constant;
		
		return new Color3F(component1 * component1, component2 * component2, component3 * component3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F[] doComputeAP(final Color3F transmittance, final float cosThetaOutgoing, final float eta, final float h) {
		final float cosGammaOutgoing = sqrt(max(0.0F, 1.0F - h * h));
		final float cosTheta = cosThetaOutgoing * cosGammaOutgoing;
		
		final float fresnel = DielectricFresnel.evaluate(cosTheta, 1.0F, eta);
		
		final Color3F color0 = new Color3F(fresnel);
		final Color3F color1 = Color3F.multiply(transmittance, Color3F.multiply(Color3F.subtract(Color3F.WHITE, color0), Color3F.subtract(Color3F.WHITE, color0)));
		final Color3F color2 = Color3F.multiply(Color3F.multiply(color1, transmittance), color0);
		final Color3F color3 = Color3F.divide(Color3F.multiply(Color3F.multiply(color2, color0), transmittance), Color3F.subtract(Color3F.WHITE, Color3F.multiply(transmittance, color0)));
		
		return new Color3F[] {color0, color1, color2, color3};
	}
	
	private static float doComputeLuminance(final Color3F[] colors) {
		float luminance = 0.0F;
		
		for(final Color3F color : colors) {
			luminance += color.relativeLuminance();
		}
		
		return luminance;
	}
	
	private static float doComputePhi(final float gammaOutgoing, final float gammaTransmission, final int p) {
		return 2.0F * p * gammaTransmission - 2.0F * gammaOutgoing + p * PI;
	}
	
	private static float doI0(final float x) {
		float a = 0.0F;
		float b = 1.0F;
		
		long c = 1L;
		
		int d = 1;
		
		for(int i = 0; i < 10; i++) {
			if(i > 1) {
				c *= i;
			}
			
			a += b / (d * (c * c));
			b *= x * x;
			d *= 4;
		}
		
		return a;
	}
	
	private static float doLogI0(final float x) {
		return x > 12.0F ? x + 0.5F * (-log(2.0F * PI) + log(1.0F / x) + 1.0F / (8.0F * x)) : log(doI0(x));
	}
	
	private static float doLogistic(final float a, final float b) {
		final float c = exp(-abs(a) / b);
		final float d = 1.0F + c;
		
		return c / (b * (d * d));
	}
	
	private static float doLogisticCDF(final float a, final float b) {
		return 1.0F / (1.0F + exp(-a / b));
	}
	
	private static float doLogisticTrimmed(final float a, final float b, final float c, final float d) {
		return doLogistic(a, b) / (doLogisticCDF(d, b) - doLogisticCDF(c, b));
	}
	
	private static float doLogisticTrimmedSample(final float a, final float b, final float c, final float d) {
		final float e = doLogisticCDF(d, b) - doLogisticCDF(c, b);
		final float f = -b * log(1.0F / (a * e + doLogisticCDF(c, b)) - 1.0F);
		
		return saturate(f, c, d);
	}
	
	private static float doMP(final float cosThetaIncoming, final float cosThetaOutgoing, final float sinThetaIncoming, final float sinThetaOutgoing, final float v) {
		final float a = cosThetaIncoming * cosThetaOutgoing / v;
		final float b = sinThetaIncoming * sinThetaOutgoing / v;
		
		if(v <= 0.1F) {
			return exp(doLogI0(a) - b - 1.0F / v + 0.6931F + log(1.0F / (2.0F * v)));
		}
		
		return (exp(-b) * doI0(a)) / (sinh(1.0F / v) * 2.0F * v);
	}
	
	private static float doNP(final float gammaOutgoing, final float gammaTransmission, final float phi, final float s, final int p) {
		float deltaPhi = phi - doComputePhi(gammaOutgoing, gammaTransmission, p);
		
		while(deltaPhi > PI) {
			deltaPhi -= 2.0F * PI;
		}
		
		while(deltaPhi < -PI) {
			deltaPhi += 2.0F * PI;
		}
		
		return doLogisticTrimmed(deltaPhi, s, -PI, PI);
	}
	
	private static float[] doComputeAPProbabilityDensityFunctionValues(final Color3F sigmaA, final float cosThetaOutgoing, final float eta, final float h) {
		final float sinThetaOutgoing = sqrt(max(0.0F, 1.0F - cosThetaOutgoing * cosThetaOutgoing));
		
		final float sinThetaTransmission = sinThetaOutgoing / eta;
		final float cosThetaTransmission = sqrt(max(0.0F, 1.0F - sinThetaTransmission * sinThetaTransmission));
		
		final float etap = sqrt(eta * eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		
		final float sinGammaTransmission = h / etap;
		final float cosGammaTransmission = sqrt(max(0.0F, 1.0F - sinGammaTransmission * sinGammaTransmission));
		
		final float scale = 2.0F * cosGammaTransmission / cosThetaTransmission;
		
		final Color3F transmittance = new Color3F(exp(-sigmaA.r * scale), exp(-sigmaA.g * scale), exp(-sigmaA.b * scale));
		
		final Color3F[] colors = doComputeAP(transmittance, cosThetaOutgoing, eta, h);
		
		final float luminance = doComputeLuminance(colors);
		
		final float[] probabilityDensityFunctionValues = new float[colors.length];
		
		for(int i = 0; i < probabilityDensityFunctionValues.length; i++) {
			probabilityDensityFunctionValues[i] = colors[i].relativeLuminance() / luminance;
		}
		
		return probabilityDensityFunctionValues;
	}
}