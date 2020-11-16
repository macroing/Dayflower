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
import static org.dayflower.util.Floats.PI_RECIPROCAL;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
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
 * A {@code FresnelBlendBRDF} is an implementation of {@link BXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for glossy reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FresnelBlendBRDF extends BXDF {
	private static final float ONE_MINUS_EPSILON = 0.99999994F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Color3F reflectanceScaleDiffuse;
	private final Color3F reflectanceScaleSpecular;
	private final MicrofacetDistribution microfacetDistribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FresnelBlendBRDF} instance.
	 * <p>
	 * If either {@code reflectanceScaleDiffuse}, {@code reflectanceScaleSpecular} or {@code microfacetDistribution} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScaleDiffuse a {@link Color3F} instance that represents the reflectance scale for the diffuse component
	 * @param reflectanceScaleSpecular a {@code Color3F} instance that represents the reflectance scale for the specular component
	 * @param microfacetDistribution a {@link MicrofacetDistribution} instance
	 * @throws NullPointerException thrown if, and only if, either {@code reflectanceScaleDiffuse}, {@code reflectanceScaleSpecular} or {@code microfacetDistribution} are {@code null}
	 */
	public FresnelBlendBRDF(final Color3F reflectanceScaleDiffuse, final Color3F reflectanceScaleSpecular, final MicrofacetDistribution microfacetDistribution) {
		super(BXDFType.GLOSSY_REFLECTION);
		
		this.reflectanceScaleDiffuse = Objects.requireNonNull(reflectanceScaleDiffuse, "reflectanceScaleDiffuse == null");
		this.reflectanceScaleSpecular = Objects.requireNonNull(reflectanceScaleSpecular, "reflectanceScaleSpecular == null");
		this.microfacetDistribution = Objects.requireNonNull(microfacetDistribution, "microfacetDistribution == null");
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
//		PBRT: Implementation of FresnelBlend.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Vector3F normal = Vector3F.add(outgoing, incoming);
		
		if(isZero(normal.getX()) && isZero(normal.getY()) && isZero(normal.getZ())) {
			return Color3F.BLACK;
		}
		
		final Vector3F normalNormalized = Vector3F.normalize(normal);
		
		final float a = 28.0F / (23.0F * PI);
		final float b = 1.0F - doPow5(1.0F - 0.5F * incoming.cosThetaAbs());
		final float c = 1.0F - doPow5(1.0F - 0.5F * outgoing.cosThetaAbs());
		final float d = this.microfacetDistribution.computeDifferentialArea(normalNormalized);
		final float e = 4.0F * abs(Vector3F.dotProduct(incoming, normalNormalized)) * max(incoming.cosThetaAbs(), outgoing.cosThetaAbs());
		final float f = d / e;
		
		final Color3F reflectanceScaleDiffuse = this.reflectanceScaleDiffuse;
		final Color3F reflectanceScaleSpecular = this.reflectanceScaleSpecular;
		final Color3F fresnel = doFresnelDielectricSchlick(reflectanceScaleSpecular, Vector3F.dotProduct(incoming, normalNormalized));
		final Color3F colorDiffuse = Color3F.multiply(Color3F.multiply(Color3F.multiply(Color3F.multiply(reflectanceScaleDiffuse, a), Color3F.subtract(Color3F.WHITE, reflectanceScaleSpecular)), b), c);
		final Color3F colorSpecular = Color3F.multiply(fresnel, f);
		
		return Color3F.add(colorDiffuse, colorSpecular);
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
//		PBRT: Implementation of FresnelBlend.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		if(sample.getU() < 0.5F) {
			final float u = min(2.0F * sample.getU(), ONE_MINUS_EPSILON);
			final float v = sample.getV();
			
			final Vector3F incomingUnoriented = SampleGeneratorF.sampleHemisphereCosineDistribution(u, v);
			final Vector3F incoming = outgoing.getZ() < 0.0F ? Vector3F.negateComponent3(incomingUnoriented) : incomingUnoriented;
			
			final BXDFType bXDFType = getBXDFType();
			
			final Color3F result = evaluateDistributionFunction(outgoing, incoming);
			
			final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, incoming);
			
			return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		}
		
		final float u = min(2.0F * (sample.getU() - 0.5F), ONE_MINUS_EPSILON);
		final float v = sample.getV();
		
		final Vector3F normal = this.microfacetDistribution.sampleNormal(outgoing, new Point2F(u, v));
		
		final Vector3F incoming = Vector3F.reflection(outgoing, normal);
		
		if(!Vector3F.sameHemisphere(outgoing, incoming)) {
			return Optional.empty();
		}
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FresnelBlendBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code FresnelBlendBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new FresnelBlendBRDF(%s, %s, %s)", this.reflectanceScaleDiffuse, this.reflectanceScaleSpecular, this.microfacetDistribution);
	}
	
	/**
	 * Compares {@code object} to this {@code FresnelBlendBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FresnelBlendBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FresnelBlendBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FresnelBlendBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FresnelBlendBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScaleDiffuse, FresnelBlendBRDF.class.cast(object).reflectanceScaleDiffuse)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScaleSpecular, FresnelBlendBRDF.class.cast(object).reflectanceScaleSpecular)) {
			return false;
		} else if(!Objects.equals(this.microfacetDistribution, FresnelBlendBRDF.class.cast(object).microfacetDistribution)) {
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
//		PBRT: Implementation of FresnelBlend.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		if(!Vector3F.sameHemisphere(outgoing, incoming)) {
			return 0.0F;
		}
		
		final Vector3F normal = Vector3F.normalize(Vector3F.add(outgoing, incoming));
		
		return 0.5F * (incoming.cosThetaAbs() * PI_RECIPROCAL + this.microfacetDistribution.computeProbabilityDensityFunctionValue(outgoing, normal) / (4.0F * Vector3F.dotProduct(outgoing, normal)));
	}
	
	/**
	 * Returns a hash code for this {@code FresnelBlendBRDF} instance.
	 * 
	 * @return a hash code for this {@code FresnelBlendBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScaleDiffuse, this.reflectanceScaleSpecular, this.microfacetDistribution);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doFresnelDielectricSchlick(final Color3F f0, final float cosTheta) {
		return Color3F.add(f0, Color3F.multiply(Color3F.subtract(Color3F.WHITE, f0), doPow5(1.0F - cosTheta)));
	}
	
	private static float doPow5(final float value) {
		return (value * value) * (value * value) * value;
	}
}