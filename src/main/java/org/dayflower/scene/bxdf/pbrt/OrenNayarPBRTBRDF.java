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
import static org.dayflower.utility.Floats.PI_RECIPROCAL;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.random;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code OrenNayarPBRTBRDF} is an implementation of {@link PBRTBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for Oren-Nayar.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class OrenNayarPBRTBRDF extends PBRTBXDF {
	private final AngleF angle;
	private final Color3F reflectanceScale;
	private final float a;
	private final float b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code OrenNayarPBRTBRDF} instance.
	 * <p>
	 * If either {@code angle} or {@code reflectanceScale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param angle an {@link AngleF} instance
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @throws NullPointerException thrown if, and only if, either {@code angle} or {@code reflectanceScale} are {@code null}
	 */
	public OrenNayarPBRTBRDF(final AngleF angle, final Color3F reflectanceScale) {
		super(BXDFType.DIFFUSE_REFLECTION);
		
		this.angle = Objects.requireNonNull(angle, "angle == null");
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.a = 1.0F - ((angle.getRadians() * angle.getRadians()) / (2.0F * ((angle.getRadians() * angle.getRadians()) + 0.33F)));
		this.b = 0.45F * (angle.getRadians() * angle.getRadians()) / ((angle.getRadians() * angle.getRadians()) + 0.09F);
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
//		PBRT: Implementation of OrenNayar.
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final float cosThetaAbsIncoming = incoming.cosThetaAbs();
		final float cosThetaAbsOutgoing = outgoing.cosThetaAbs();
		
		final float sinThetaIncoming = incoming.sinTheta();
		final float sinThetaOutgoing = outgoing.sinTheta();
		
		final float maxCos = sinThetaIncoming > 1.0e-4F && sinThetaOutgoing > 1.0e-4F ? max(0.0F, incoming.cosPhi() * outgoing.cosPhi() + incoming.sinPhi() * outgoing.sinPhi()) : 0.0F;
		
		final float sinA = cosThetaAbsIncoming > cosThetaAbsOutgoing ? sinThetaOutgoing : sinThetaIncoming;
		final float tanB = cosThetaAbsIncoming > cosThetaAbsOutgoing ? sinThetaIncoming / cosThetaAbsIncoming : sinThetaOutgoing / cosThetaAbsOutgoing;
		
		final float a = this.a;
		final float b = this.b;
		final float c = (a + b * maxCos * sinA * tanB);
		
		return Color3F.multiply(Color3F.multiply(this.reflectanceScale, PI_RECIPROCAL), c);
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
	 * Returns a {@code String} representation of this {@code OrenNayarPBRTBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code OrenNayarPBRTBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new OrenNayarPBRTBRDF(%s, %s)", this.angle, this.reflectanceScale);
	}
	
	/**
	 * Compares {@code object} to this {@code OrenNayarPBRTBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarPBRTBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code OrenNayarPBRTBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code OrenNayarPBRTBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof OrenNayarPBRTBRDF)) {
			return false;
		} else if(!Objects.equals(this.angle, OrenNayarPBRTBRDF.class.cast(object).angle)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, OrenNayarPBRTBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!equal(this.a, OrenNayarPBRTBRDF.class.cast(object).a)) {
			return false;
		} else if(!equal(this.b, OrenNayarPBRTBRDF.class.cast(object).b)) {
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
	 * Returns a hash code for this {@code OrenNayarPBRTBRDF} instance.
	 * 
	 * @return a hash code for this {@code OrenNayarPBRTBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.angle, this.reflectanceScale, Float.valueOf(this.a), Float.valueOf(this.b));
	}
}