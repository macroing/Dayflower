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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.fresnelSchlickWeightLerp;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.log;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.random;
import static org.dayflower.utility.Floats.sqrt;

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
 * A {@code DisneyClearCoatPBRTBRDF} is an implementation of {@link PBRTBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for Disney Clear Coat reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DisneyClearCoatPBRTBRDF extends PBRTBXDF {
	private final float gloss;
	private final float weight;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DisneyClearCoatPBRTBRDF} instance.
	 * 
	 * @param gloss a {@code float} that represents the gloss
	 * @param weight a {@code float} that represents the weight
	 */
	public DisneyClearCoatPBRTBRDF(final float gloss, final float weight) {
		super(BXDFType.GLOSSY_REFLECTION);
		
		this.gloss = gloss;
		this.weight = weight;
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
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			final Point2F sampleB = i < samplesB.size() ? samplesB.get(i) : new Point2F(random(), random());
			
			final Vector3F outgoing = SampleGeneratorF.sampleHemisphereUniformDistribution(sampleB.getU(), sampleB.getV());
			
			final Optional<BXDFResult> optionalBXDFResult = sampleDistributionFunction(outgoing, sampleA);
			
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
		
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		
		Color3F reflectance = Color3F.BLACK;
		
		for(int i = 0; i < samplesA.size(); i++) {
			final Point2F sampleA = samplesA.get(i);
			
			final Optional<BXDFResult> optionalBXDFResult = sampleDistributionFunction(outgoing, sampleA);
			
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
//		PBRT: Implementation of DisneyClearcoat.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Vector3F normal = Vector3F.add(incoming, outgoing);
		
		if(isZero(normal.getX()) && isZero(normal.getY()) && isZero(normal.getZ())) {
			return Color3F.BLACK;
		}
		
		final Vector3F normalNormalized = Vector3F.normalize(normal);
		
		final float d = doGTR1(normalNormalized.cosThetaAbs(), this.gloss);
		final float f = fresnelSchlickWeightLerp(Vector3F.dotProduct(outgoing, normalNormalized), 0.04F);
		final float g = doSmithGGGX(outgoing.cosThetaAbs(), 0.25F) * doSmithGGGX(incoming.cosThetaAbs(), 0.25F);
		
		return new Color3F(this.weight * g * f * d / 4.0F);
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
//		PBRT: Implementation of DisneyClearcoat.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		if(isZero(outgoing.getZ())) {
			return Optional.empty();
		}
		
		final float alphaSquared = this.gloss * this.gloss;
		final float cosTheta = sqrt(max(0.0F, (1.0F - pow(alphaSquared, 1.0F - sample.getU())) / (1.0F - alphaSquared)));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = 2.0F * PI * sample.getV();
		
		final Vector3F normal = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
		final Vector3F normalCorrectlyOriented = Vector3F.sameHemisphereZ(outgoing, normal) ? normal : Vector3F.negate(normal);
		final Vector3F incoming = Vector3F.reflection(outgoing, normalCorrectlyOriented);
		
		if(!Vector3F.sameHemisphereZ(outgoing, incoming)) {
			return Optional.empty();
		}
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DisneyClearCoatPBRTBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code DisneyClearCoatPBRTBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new DisneyClearCoatPBRTBRDF(%+.10f, %+.10f)", Float.valueOf(this.gloss), Float.valueOf(this.weight));
	}
	
	/**
	 * Compares {@code object} to this {@code DisneyClearCoatPBRTBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DisneyClearCoatPBRTBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DisneyClearCoatPBRTBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DisneyClearCoatPBRTBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DisneyClearCoatPBRTBRDF)) {
			return false;
		} else if(!equal(this.gloss, DisneyClearCoatPBRTBRDF.class.cast(object).gloss)) {
			return false;
		} else if(!equal(this.weight, DisneyClearCoatPBRTBRDF.class.cast(object).weight)) {
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
//		PBRT: Implementation of DisneyClearcoat.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(!Vector3F.sameHemisphereZ(outgoing, incoming)) {
			return 0.0F;
		}
		
		final Vector3F normal = Vector3F.add(incoming, outgoing);
		
		if(isZero(normal.getX()) && isZero(normal.getY()) && isZero(normal.getZ())) {
			return 0.0F;
		}
		
		final Vector3F normalNormalized = Vector3F.normalize(normal);
		
		final float cosThetaAbsNormalNormalized = normalNormalized.cosThetaAbs();
		final float probabilityDensityFunctionValue = doGTR1(cosThetaAbsNormalNormalized, this.gloss) * cosThetaAbsNormalNormalized / (4.0F * Vector3F.dotProduct(outgoing, normalNormalized));
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code DisneyClearCoatPBRTBRDF} instance.
	 * 
	 * @return a hash code for this {@code DisneyClearCoatPBRTBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.gloss), Float.valueOf(this.weight));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGTR1(final float cosTheta, final float alpha) {
		final float a = alpha * alpha;
		final float b = (a - 1.0F) / (PI * log(a) * (1.0F + (a - 1.0F) * cosTheta * cosTheta));
		
		return b;
	}
	
	private static float doSmithGGGX(final float cosTheta, final float alpha) {
		final float a = alpha * alpha;
		final float b = cosTheta * cosTheta;
		final float c = 1.0F / (cosTheta + sqrt(a + b - a * b));
		
		return c;
	}
}