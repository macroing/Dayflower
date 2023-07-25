/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.random;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.interpolation.Interpolation;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.TransportMode;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.Color3F;

/**
 * A {@code FourierBXDF} is an implementation of {@link BXDF} that represents a BRDF and a BTDF for glossy reflection and transmission.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code BXDF} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FourierBXDF extends BXDF {
	private final FourierBXDFTable fourierBXDFTable;
	private final TransportMode transportMode;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FourierBXDF} instance.
	 * <p>
	 * If either {@code fourierBXDFTable} or {@code transportMode} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param fourierBXDFTable a {@link FourierBXDFTable} instance
	 * @param transportMode a {@link TransportMode} instance
	 * @throws NullPointerException thrown if, and only if, either {@code fourierBXDFTable} or {@code transportMode} are {@code null}
	 */
	public FourierBXDF(final FourierBXDFTable fourierBXDFTable, final TransportMode transportMode) {
		super(BXDFType.GLOSSY_REFLECTION_AND_TRANSMISSION);
		
		this.fourierBXDFTable = Objects.requireNonNull(fourierBXDFTable, "fourierBXDFTable == null");
		this.transportMode = Objects.requireNonNull(transportMode, "transportMode == null");
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
		
		final float muI = Vector3F.negate(incoming).cosTheta();
		final float muO = outgoing.cosTheta();
		final float cosPhi = Vector3F.cosDPhi(Vector3F.negate(incoming), outgoing);
		
		final int[] offsetI = new int[1];
		final int[] offsetO = new int[1];
		
		final float[] weightsI = new float[4];
		final float[] weightsO = new float[4];
		
		if(!this.fourierBXDFTable.getWeightsAndOffset(muI, offsetI, weightsI) || !this.fourierBXDFTable.getWeightsAndOffset(muO, offsetO, weightsO)) {
			return new Color3F();
		}
		
		final float[] ak = new float[this.fourierBXDFTable.getMMax() * this.fourierBXDFTable.getNChannels()];
		
		int mMax = 0;
		
		for(int b = 0; b < 4; b++) {
			for(int a = 0; a < 4; a++) {
				final float weight = weightsI[a] * weightsO[b];
				
				if(weight != 0.0F) {
					final int[] m = new int[1];
					final int[] n = new int[1];
					
					final float[] ap = this.fourierBXDFTable.getAk(offsetI[0] + a, offsetO[0] + b, m, n);
					
					mMax = Ints.max(mMax, m[0]);
					
					for(int c = 0; c < this.fourierBXDFTable.getNChannels(); c++) {
						for(int k = 0; k < m[0]; k++) {
							ak[c * this.fourierBXDFTable.getMMax() + k] += weight * ap[n[0] + c * m[0] + k];
						}
					}
				}
			}
		}
		
		final float y = Floats.max(0.0F, Interpolation.fourier(ak, mMax, cosPhi, 0));
		
		float scale = muI != 0.0F ? 1.0F / Floats.abs(muI) : 0.0F;
		
		if(this.transportMode == TransportMode.RADIANCE && muI * muO > 0.0F) {
			final float eta = muI > 0.0F ? 1.0F / this.fourierBXDFTable.getEta() : this.fourierBXDFTable.getEta();
			
			scale *= eta * eta;
		}
		
		if(this.fourierBXDFTable.getNChannels() == 1) {
			return new Color3F(y * scale);
		}
		
		final float r = Interpolation.fourier(ak, mMax, cosPhi, 1 * this.fourierBXDFTable.getMMax());
		final float b = Interpolation.fourier(ak, mMax, cosPhi, 2 * this.fourierBXDFTable.getMMax());
		final float g = 1.39829F * y - 0.100913F * b - 0.297375F * r;
		
		final float[] rgb = {r * scale, g * scale, b * scale};
		
		return Color3F.saturate(new Color3F(rgb[0], rgb[1], rgb[2]), 0.0F, Floats.MAX_VALUE);
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
		
		final float muO = outgoing.cosTheta();
		
		final float[] pdfMu = new float[1];
		
		final float muI = Interpolation.sampleCatmullRom2D(this.fourierBXDFTable.getNMu(), this.fourierBXDFTable.getNMu(), this.fourierBXDFTable.getMu(), this.fourierBXDFTable.getMu(), this.fourierBXDFTable.getA0(), this.fourierBXDFTable.getCDF(), muO, sample.x, null, pdfMu);
		
		final int[] offsetI = new int[1];
		final int[] offsetO = new int[1];
		
		final float[] weightsI = new float[4];
		final float[] weightsO = new float[4];
		
		if(!this.fourierBXDFTable.getWeightsAndOffset(muI, offsetI, weightsI) || !this.fourierBXDFTable.getWeightsAndOffset(muO, offsetO, weightsO)) {
			return Optional.empty();
		}
		
		final float[] ak = new float[this.fourierBXDFTable.getMMax() * this.fourierBXDFTable.getNChannels()];
		
		int mMax = 0;
		
		for(int b = 0; b < 4; b++) {
			for(int a = 0; a < 4; a++) {
				final float weight = weightsI[a] * weightsO[b];
				
				if(weight != 0.0F) {
					final int[] m = new int[1];
					final int[] n = new int[1];
					
					final float[] ap = this.fourierBXDFTable.getAk(offsetI[0] + a, offsetO[0] + b, m, n);
					
					mMax = Ints.max(mMax, m[0]);
					
					for(int c = 0; c < this.fourierBXDFTable.getNChannels(); c++) {
						for(int k = 0; k < m[0]; k++) {
							ak[c * this.fourierBXDFTable.getMMax() + k] += weight * ap[n[0] + c * m[0] + k];
						}
					}
				}
			}
		}
		
		final float[] phi = new float[1];
		final float[] pdfPhi = new float[1];
		
		final float y = Interpolation.sampleFourier(ak, this.fourierBXDFTable.getRecip(), mMax, sample.x, pdfPhi, phi);
		
		final float probabilityDensityFunctionValue = Floats.max(0.0F, pdfPhi[0] * pdfMu[0]);
		
		final float sin2ThetaI = Floats.max(0.0F, 1.0F - muI * muI);
		
		float norm = Floats.sqrt(sin2ThetaI / outgoing.sinThetaSquared());
		
		if(Float.isInfinite(norm)) {
			norm = 0.0F;
		}
		
		final float sinPhi = Floats.sin(phi[0]);
		final float cosPhi = Floats.cos(phi[0]);
		
		final Vector3F incoming = Vector3F.normalize(new Vector3F(-(norm * (cosPhi * outgoing.x - sinPhi * outgoing.y)), -(norm * (sinPhi * outgoing.x + cosPhi * outgoing.y)), -muI));
		
		float scale = muI != 0.0F ? 1.0F / Floats.abs(muI) : 0.0F;
		
		if(this.transportMode == TransportMode.RADIANCE && muI * muO > 0.0F) {
			final float eta = muI > 0.0F ? 1.0F / this.fourierBXDFTable.getEta() : this.fourierBXDFTable.getEta();
			
			scale *= eta * eta;
		}
		
		if(this.fourierBXDFTable.getNChannels() == 1) {
			return Optional.of(new BXDFResult(getBXDFType(), new Color3F(y * scale), incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final float r = Interpolation.fourier(ak, mMax, cosPhi, 1 * this.fourierBXDFTable.getMMax());
		final float b = Interpolation.fourier(ak, mMax, cosPhi, 2 * this.fourierBXDFTable.getMMax());
		final float g = 1.39829F * y - 0.100913F * b - 0.297375F * r;
		
		final float[] rgb = {r * scale, g * scale, b * scale};
		
		return Optional.of(new BXDFResult(getBXDFType(), Color3F.saturate(new Color3F(rgb[0], rgb[1], rgb[2]), 0.0F, Floats.MAX_VALUE), incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FourierBXDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code FourierBXDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new FourierBXDF(%s, %s)", this.fourierBXDFTable, this.transportMode);
	}
	
	/**
	 * Compares {@code object} to this {@code FourierBXDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FourierBXDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FourierBXDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FourierBXDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FourierBXDF)) {
			return false;
		} else if(!Objects.equals(this.fourierBXDFTable, FourierBXDF.class.cast(object).fourierBXDFTable)) {
			return false;
		} else if(!Objects.equals(this.transportMode, FourierBXDF.class.cast(object).transportMode)) {
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
		
		final float muI = Vector3F.negate(incoming).cosTheta();
		final float muO = outgoing.cosTheta();
		final float cosPhi = Vector3F.cosDPhi(Vector3F.negate(incoming), outgoing);
		
		final int[] offsetI = new int[1];
		final int[] offsetO = new int[1];
		
		final float[] weightsI = new float[4];
		final float[] weightsO = new float[4];
		
		if(!this.fourierBXDFTable.getWeightsAndOffset(muI, offsetI, weightsI) || !this.fourierBXDFTable.getWeightsAndOffset(muO, offsetO, weightsO)) {
			return 0.0F;
		}
		
		final float[] ak = new float[this.fourierBXDFTable.getMMax() * this.fourierBXDFTable.getNChannels()];
		
		int mMax = 0;
		
		for(int o = 0; o < 4; o++) {
			for(int i = 0; i < 4; i++) {
				final float weight = weightsI[i] * weightsO[o];
				
				if(weight == 0.0F) {
					continue;
				}
				
				final int[] order = new int[1];
				final int[] offset = new int[1];
				
				final float[] coeffs = this.fourierBXDFTable.getAk(offsetI[0] + i, offsetO[0] + o, order, offset);
				
				mMax = Ints.max(mMax, order[0]);
				
				for(int k = 0; k < order[0]; k++) {
					ak[k] += coeffs[offset[0] + k] * weight;
				}
			}
		}
		
		float rho = 0.0F;
		
		for(int o = 0; o < 4; o++) {
			if(weightsO[o] == 0.0F) {
				continue;
			}
			
			rho += weightsO[o] * this.fourierBXDFTable.getCDF()[(offsetO[0] + o) * this.fourierBXDFTable.getNMu() + this.fourierBXDFTable.getNMu() - 1] * (2.0F * Floats.PI);
		}
		
		final float y = Interpolation.fourier(ak, mMax, cosPhi, 0);
		
		return rho > 0.0F && y > 0.0F ? y / rho : 0.0F;
	}
	
	/**
	 * Returns a hash code for this {@code FourierBXDF} instance.
	 * 
	 * @return a hash code for this {@code FourierBXDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.fourierBXDFTable, this.transportMode);
	}
}