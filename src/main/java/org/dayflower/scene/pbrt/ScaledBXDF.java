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

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Lists;

/**
 * A {@code ScaledBXDF} is an implementation of {@link BXDF} that scales the result of another {@code BXDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ScaledBXDF extends BXDF {
	private final BXDF bXDF;
	private final Color3F scale;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ScaledBXDF} instance.
	 * <p>
	 * If either {@code bXDF} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bXDF the {@link BXDF} instance to scale
	 * @param scale the {@link Color3F} instance to use as the scale
	 * @throws NullPointerException thrown if, and only if, either {@code bXDF} or {@code scale} are {@code null}
	 */
	public ScaledBXDF(final BXDF bXDF, final Color3F scale) {
		super(Objects.requireNonNull(bXDF, "bXDF == null").getBXDFType());
		
		this.bXDF = bXDF;
		this.scale = Objects.requireNonNull(scale, "scale == null");
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
		Lists.requireNonNullList(samplesA, "samplesA");
		Lists.requireNonNullList(samplesB, "samplesB");
		
		return Color3F.multiply(this.bXDF.computeReflectanceFunction(samplesA, samplesB), this.scale);
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
		Lists.requireNonNullList(samplesA, "samplesA");
		
		Objects.requireNonNull(outgoing, "outgoing == null");
		
		return Color3F.multiply(this.bXDF.computeReflectanceFunction(samplesA, outgoing), this.scale);
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
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Color3F.multiply(this.bXDF.evaluateDistributionFunction(outgoing, incoming), this.scale);
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
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Optional<BXDFDistributionFunctionResult> optionalBXDFDistributionFunctionResult = this.bXDF.sampleDistributionFunction(outgoing, sample);
		
		if(optionalBXDFDistributionFunctionResult.isPresent()) {
			return Optional.of(BXDFDistributionFunctionResult.scale(optionalBXDFDistributionFunctionResult.get(), this.scale));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ScaledBXDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code ScaledBXDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new ScaledBXDF(%s, %s)", this.bXDF, this.scale);
	}
	
	/**
	 * Compares {@code object} to this {@code ScaledBXDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ScaledBXDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ScaledBXDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ScaledBXDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ScaledBXDF)) {
			return false;
		} else if(!Objects.equals(this.bXDF, ScaledBXDF.class.cast(object).bXDF)) {
			return false;
		} else if(!Objects.equals(this.scale, ScaledBXDF.class.cast(object).scale)) {
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
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return this.bXDF.evaluateProbabilityDensityFunction(outgoing, incoming);
	}
	
	/**
	 * Returns a hash code for this {@code ScaledBXDF} instance.
	 * 
	 * @return a hash code for this {@code ScaledBXDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bXDF, this.scale);
	}
}