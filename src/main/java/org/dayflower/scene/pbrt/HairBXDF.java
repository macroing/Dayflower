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
import static org.dayflower.util.Floats.asin;
import static org.dayflower.util.Floats.atan2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.exp;
import static org.dayflower.util.Floats.log;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sinh;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Floats.toRadians;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Lists;

/**
 * A {@code HairBXDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) and a BTDF (Bidirectional Transmittance Distribution Function) for hair.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class HairBXDF extends BXDF {
	private final Color3F sigmaA;
//	private final float alpha;
//	private final float betaM;
//	private final float betaN;
	private final float eta;
	private final float gammaOutgoing;
	private final float h;
	private final float s;
	private final float[] cos2KAlpha;
	private final float[] sin2KAlpha;
	private final float[] v;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public HairBXDF(final Color3F sigmaA, final float alpha, final float betaM, final float betaN, final float eta, final float h) {
		super(BXDFType.GLOSSY_REFLECTION_AND_TRANSMISSION);
		
		this.sigmaA = Objects.requireNonNull(sigmaA, "sigmaA == null");
//		this.alpha = alpha;
//		this.betaM = betaM;
//		this.betaN = betaN;
		this.eta = eta;
		this.gammaOutgoing = asin(saturate(h, -1.0F, 1.0F));
		this.h = h;
		this.s = 0.626657069F * (0.265F * betaN + 1.194F * (betaN * betaN) + 5.372F * doPow(betaN, 22));
		this.cos2KAlpha = new float[3];
		this.sin2KAlpha = new float[3];
		this.v = new float[4];
		this.v[0] = (0.726F * betaM + 0.812F * (betaM * betaM) + 3.7F * doPow(betaM, 20)) * (0.726F * betaM + 0.812F * (betaM * betaM) + 3.7F * doPow(betaM, 20));
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
//		PBRT: Implementation of HairBSDF.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final float sinThetaOutgoing = outgoing.getX();
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.getZ(), outgoing.getY());
		
		final float sinThetaIncoming = incoming.getX();
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		final float phiIncoming = atan2(incoming.getZ(), incoming.getY());
		
		final float sinThetaTransmission = sinThetaOutgoing / this.eta;
		final float cosThetaTransmission = sqrt(max(0.0F, 1.0F - sinThetaTransmission * sinThetaTransmission));
		
		final float etap = sqrt(this.eta * this.eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		final float sinGammaTransmission = this.h / etap;
		final float cosGammaTransmission = sqrt(max(0.0F, 1.0F - sinGammaTransmission * sinGammaTransmission));
		final float gammaTransmission = asin(saturate(sinGammaTransmission, -1.0F, 1.0F));
		
		final Color3F transmittance = new Color3F(exp(-this.sigmaA.getR() * (2.0F * cosGammaTransmission / cosThetaTransmission)), exp(-this.sigmaA.getG() * (2.0F * cosGammaTransmission / cosThetaTransmission)), exp(-this.sigmaA.getB() * (2.0F * cosGammaTransmission / cosThetaTransmission)));
		
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
//		PBRT: Implementation of HairBSDF.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final float sinThetaOutgoing = outgoing.getX();
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.getZ(), outgoing.getY());
		
		final Point2F[] samples = new Point2F[] {doDemuxFloat(sample.getU()), doDemuxFloat(sample.getV())};
		
		final float[] probabilityDensityFunctionValues = doComputeAPProbabilityDensityFunctionValues(this.sigmaA, cosThetaOutgoing, this.eta, this.h);
		
		int p = 0;
		
		for(int i = 0; i < 3; i++) {
			if(samples[0].getU() < probabilityDensityFunctionValues[i]) {
				p = i;
				
				break;
			}
			
			samples[0] = new Point2F(samples[0].getU() - probabilityDensityFunctionValues[i], samples[0].getV());
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
		
		samples[1] = new Point2F(max(samples[1].getU(), 1.0e-5F), samples[1].getV());
		
		final float cosTheta = 1.0F + this.v[p] * log(samples[1].getU() + (1.0F - samples[1].getU()) * exp(-2.0F / this.v[p]));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float cosPhi = cos(2.0F * PI * samples[1].getV());
		final float sinThetaIncoming = -cosTheta * sinThetaOutgoingP + sinTheta * cosPhi * cosThetaOutgoingP;
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		
		final float etap = sqrt(this.eta * this.eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		final float sinGammaTransmission = this.h / etap;
		final float gammaTransmission = asin(saturate(sinGammaTransmission, -1.0F, 1.0F));
		final float deltaPhi = p < 3 ? doComputePhi(this.gammaOutgoing, gammaTransmission, p) + doLogisticTrimmedSample(samples[0].getV(), this.s, -PI, PI) : 2.0F * PI * samples[0].getV();
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
		
		final Color3F result = evaluateDistributionFunction(outgoing, incoming);
		
		return Optional.of(new BXDFDistributionFunctionResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
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
//		PBRT: Implementation of HairBSDF.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final float sinThetaOutgoing = outgoing.getX();
		final float cosThetaOutgoing = sqrt(max(0.0F, 1.0F - sinThetaOutgoing * sinThetaOutgoing));
		final float phiOutgoing = atan2(outgoing.getZ(), outgoing.getY());
		
		final float sinThetaIncoming = incoming.getX();
		final float cosThetaIncoming = sqrt(max(0.0F, 1.0F - sinThetaIncoming * sinThetaIncoming));
		final float phiIncoming = atan2(incoming.getZ(), incoming.getY());
		
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
	
	private static Point2F doDemuxFloat(final float value) {
		final long a = (int)(value * (1L << 32));
		
		final int b = doCompact1By1((int)(a));
		final int c = doCompact1By1((int)(a >> 1));
		
		final float x = b / (float)(1 << 16);
		final float y = c / (float)(1 << 16);
		
		return new Point2F(x, y);
	}
	
	private static float doComputePhi(final float gammaOutgoing, final float gammaTransmission, final int p) {
		return 2.0F * p * gammaTransmission - 2.0F * gammaOutgoing + p * PI;
	}
	
	private static float doComputeY(final Color3F[] colors) {
		float y = 0.0F;
		
		for(final Color3F color : colors) {
			y += color.getY();
		}
		
		return y;
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
		final float c = abs(a);
		final float d = exp(-c / b);
		final float e = 1.0F + exp(-c / b);
		
		return d / (b * (e * e));
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
	
	private static float doPow(final float value, final int exponent) {
		if(exponent == 0) {
			return 1.0F;
		} else if(exponent == 1) {
			return value;
		} else {
			final float n2 = doPow(value, exponent / 2);
			
			return n2 * n2 * doPow(value, exponent & 1);
		}
	}
	
	private static float[] doComputeAPProbabilityDensityFunctionValues(final Color3F sigmaA, final float cosThetaOutgoing, final float eta, final float h) {
		final float sinThetaOutgoing = sqrt(max(0.0F, 1.0F - cosThetaOutgoing * cosThetaOutgoing));
		
		final float sinThetaTransmission = sinThetaOutgoing / eta;
		final float cosThetaTransmission = sqrt(max(0.0F, 1.0F - sinThetaTransmission * sinThetaTransmission));
		
		final float etap = sqrt(eta * eta - sinThetaOutgoing * sinThetaOutgoing) / cosThetaOutgoing;
		
		final float sinGammaTransmission = h / etap;
		final float cosGammaTransmission = sqrt(max(0.0F, 1.0F - sinGammaTransmission * sinGammaTransmission));
		
		final Color3F transmittance = new Color3F(exp(-sigmaA.getR() * (2.0F * cosGammaTransmission / cosThetaTransmission)), exp(-sigmaA.getG() * (2.0F * cosGammaTransmission / cosThetaTransmission)), exp(-sigmaA.getB() * (2.0F * cosGammaTransmission / cosThetaTransmission)));
		
		final Color3F[] colors = doComputeAP(transmittance, cosThetaOutgoing, eta, h);
		
		final float y = doComputeY(colors);
		
		final float[] probabilityDensityFunctionValues = new float[colors.length];
		
		for(int i = 0; i < probabilityDensityFunctionValues.length; i++) {
			probabilityDensityFunctionValues[i] = colors[i].getY() / y;
		}
		
		return probabilityDensityFunctionValues;
	}
	
	private static int doCompact1By1(final int x) {
		final int a = x & 0x55555555;
		final int b = (a ^ (a >> 1)) & 0x33333333;
		final int c = (b ^ (b >> 2)) & 0x0f0f0f0f;
		final int d = (c ^ (c >> 4)) & 0x00ff00ff;
		final int e = (d ^ (d >> 8)) & 0x0000ffff;
		
		return e;
	}
}