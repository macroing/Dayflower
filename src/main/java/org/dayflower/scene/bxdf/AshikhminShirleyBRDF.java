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
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.pow;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.fresnel.Schlick;
import org.dayflower.utility.ParameterArguments;

/**
 * An {@code AshikhminShirleyBRDF} is an implementation of {@link BXDF} that represents an Ashikhmin-Shirley BRDF (Bidirectional Reflectance Distribution Function).
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code BXDF} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AshikhminShirleyBRDF extends BXDF {
	private final Color3F reflectanceScale;
	private final float exponent;
	private final float roughness;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AshikhminShirleyBRDF} instance.
	 * <p>
	 * If {@code reflectanceScale} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param roughness the roughness to use
	 * @throws NullPointerException thrown if, and only if, {@code reflectanceScale} is {@code null}
	 */
	public AshikhminShirleyBRDF(final Color3F reflectanceScale, final float roughness) {
		super(BXDFType.GLOSSY_REFLECTION);
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.exponent = 1.0F / (roughness * roughness);
		this.roughness = roughness;
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
	public final Color3F computeReflectanceFunction(final List<Point2F> samplesA, final List<Point2F> samplesB, final Vector3F normal) {
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		ParameterArguments.requireNonNullList(samplesB, "samplesB");
		
		Objects.requireNonNull(normal, "normal == null");
		
		return Color3F.BLACK;
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
	public final Color3F computeReflectanceFunction(final List<Point2F> samplesA, final Vector3F outgoing, final Vector3F normal) {
		ParameterArguments.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(normal, "normal == null");
		
		return Color3F.BLACK;
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
		
		/*
		 * final float nDotI = Vector3F.dotProduct(incoming, normal);
		 * final float nDotO = Vector3F.dotProduct(outgoing, normal);
		 * 
		 * if((nDotI > 0.0F && nDotO > 0.0F) || (nDotI < 0.0F && nDotO < 0.0F)) {
		 * 	return Color3F.BLACK;
		 * }
		 * 
		 * final Vector3F half = Vector3F.dotProduct(outgoing, incoming) > 0.999F ? normal : Vector3F.normalize(Vector3F.subtract(outgoing, incoming));
		 * 
		 * final float fresnel = 1.0F;
		 * final float d = (this.exponent + 1.0F) * pow(Vector3F.dotProductAbs(normal, half), this.exponent) / (2.0F * PI);
		 * final float result = fresnel * d / (4.0F * abs(nDotO + -nDotI - nDotO * -nDotI));
		 * 
		 * return Color3F.multiply(this.reflectanceScale, result);
		 */
		
		final float cosThetaAbsOutgoing = outgoing.cosThetaAbs();
		final float cosThetaAbsIncoming = incoming.cosThetaAbs();
		
		if(isZero(cosThetaAbsOutgoing) || isZero(cosThetaAbsIncoming)) {
			return Color3F.BLACK;
		}
		
		final Vector3F n = Vector3F.subtract(outgoing, incoming);
		
		if(isZero(n.x) && isZero(n.y) && isZero(n.z)) {
			return Color3F.BLACK;
		}
		
		final Vector3F nNormalized = Vector3F.normalize(n);
		
		final float d = (this.exponent + 1.0F) * pow(abs(nNormalized.cosTheta()), this.exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = Schlick.fresnelDielectric(Vector3F.dotProduct(outgoing, nNormalized), 1.0F);
		
		return Color3F.divide(Color3F.multiply(Color3F.multiply(this.reflectanceScale, d), f), 4.0F * abs(outgoing.cosTheta() + -incoming.cosTheta() - outgoing.cosTheta() * -incoming.cosTheta()));
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
		
		/*
		 * final float phi = 2.0F * PI * sample.getU();
		 * final float cosTheta = pow(1.0F - sample.getV(), 1.0F / (this.exponent + 1.0F));
		 * final float sinThetaSquared = max(0.0F, 1.0F - cosTheta * cosTheta);
		 * final float sinTheta = sqrt(sinThetaSquared);
		 * 
		 * final Vector3F halfLocal = new Vector3F(sinTheta * cos(phi), sinTheta * sin(phi), cosTheta);
		 * final Vector3F half = Vector3F.transformReverse(halfLocal, new OrthonormalBasis33F(normal));
		 * final Vector3F halfCorrectlyOriented = Vector3F.dotProduct(outgoing, normal) < 0.0F ? Vector3F.negate(half) : half;
		 * final Vector3F incoming = Vector3F.subtract(outgoing, Vector3F.multiply(halfCorrectlyOriented, 2.0F * Vector3F.dotProduct(outgoing, halfCorrectlyOriented)));
		 * 
		 * final BXDFType bXDFType = getBXDFType();
		 * 
		 * final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		 * 
		 * final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		 * 
		 * return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
		 */
		
		if(isZero(outgoing.z)) {
			return Optional.empty();
		}
		
		final Vector3F nSample = SampleGeneratorF.sampleHemispherePowerCosineDistribution(sample.x, sample.y, this.exponent);
		final Vector3F n = Vector3F.faceForward(normal, outgoing, nSample);
		
		if(Vector3F.dotProduct(outgoing, n) < 0.0F) {
			return Optional.empty();
		}
		
		final Vector3F incoming = Vector3F.reflection(outgoing, n, true);
		
		final BXDFType bXDFType = getBXDFType();
		
		final Color3F result = evaluateDistributionFunction(outgoing, normal, incoming);
		
		final float probabilityDensityFunctionValue = evaluateProbabilityDensityFunction(outgoing, normal, incoming);
		
		return Optional.of(new BXDFResult(bXDFType, result, incoming, outgoing, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AshikhminShirleyBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code AshikhminShirleyBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new AshikhminShirleyBRDF(%s, %+.10f)", this.reflectanceScale, Float.valueOf(this.roughness));
	}
	
	/**
	 * Compares {@code object} to this {@code AshikhminShirleyBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AshikhminShirleyBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AshikhminShirleyBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AshikhminShirleyBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, AshikhminShirleyBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!equal(this.exponent, AshikhminShirleyBRDF.class.cast(object).exponent)) {
			return false;
		} else if(!equal(this.roughness, AshikhminShirleyBRDF.class.cast(object).roughness)) {
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
		
		/*
		 * final float nDotI = Vector3F.dotProduct(incoming, normal);
		 * final float nDotO = Vector3F.dotProduct(outgoing, normal);
		 * 
		 * if((nDotI > 0.0F && nDotO > 0.0F) || (nDotI < 0.0F && nDotO < 0.0F)) {
		 * 	return 0.0F;
		 * }
		 * 
		 * final Vector3F half = Vector3F.dotProduct(outgoing, incoming) > 0.999F ? normal : Vector3F.normalize(Vector3F.subtract(outgoing, incoming));
		 * 
		 * return (this.exponent + 1.0F) * pow(Vector3F.dotProductAbs(normal, half), this.exponent) / (8.0F * PI * Vector3F.dotProductAbs(outgoing, half));
		 */
		
		if(Vector3F.sameHemisphereZ(outgoing, incoming)) {
			return 0.0F;
		}
		
		final Vector3F n = Vector3F.normalize(Vector3F.subtract(outgoing, incoming));
		
		return (this.exponent + 1.0F) * pow(abs(n.cosTheta()), this.exponent) / (PI * 8.0F * abs(Vector3F.dotProduct(outgoing, n)));
	}
	
	/**
	 * Returns a hash code for this {@code AshikhminShirleyBRDF} instance.
	 * 
	 * @return a hash code for this {@code AshikhminShirleyBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, Float.valueOf(this.exponent), Float.valueOf(this.roughness));
	}
}