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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code PBRTSpecularReflectionBRDF} is an implementation of {@link PBRTBXDF} that represents a BRDF (Bidirectional Reflectance Distribution Function) for specular reflection.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * Note: This class will change name from {@code PBRTSpecularReflectionBRDF} to {@code SpecularReflectionBRDF} in the future.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PBRTSpecularReflectionBRDF extends PBRTBXDF {
	private final Color3F reflectanceScale;
	private final PBRTFresnel pBRTFresnel;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PBRTSpecularReflectionBRDF} instance.
	 * <p>
	 * If either {@code reflectanceScale} or {@code pBRTFresnel} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param reflectanceScale a {@link Color3F} instance that represents the reflectance scale
	 * @param pBRTFresnel a {@link PBRTFresnel} instance
	 * @throws NullPointerException thrown if, and only if, either {@code reflectanceScale} or {@code pBRTFresnel} are {@code null}
	 */
	public PBRTSpecularReflectionBRDF(final Color3F reflectanceScale, final PBRTFresnel pBRTFresnel) {
		super(PBRTBXDFType.createSpecularReflection());
		
		this.reflectanceScale = Objects.requireNonNull(reflectanceScale, "reflectanceScale == null");
		this.pBRTFresnel = Objects.requireNonNull(pBRTFresnel, "pBRTFresnel == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the distribution function.
	 * <p>
	 * Returns an optional {@code PBRTBXDFDistributionFunctionResult} with the result of the evaluation.
	 * <p>
	 * If either {@code outgoing} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code f(const Vector3f &wo, const Vector3f &wi)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return an optional {@code PBRTBXDFDistributionFunctionResult} with the result of the evaluation
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code incoming} are {@code null}
	 */
	@Override
	public Optional<PBRTBXDFDistributionFunctionResult> evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Optional.empty();
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns an optional {@code PBRTBXDFDistributionFunctionResult} with the result of the sampling.
	 * <p>
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code BxDF} method {@code Sample_f(const Vector3f &wo, Vector3f *wi, const Point2f &sample, Float *pdf, BxDFType *sampledType = nullptr)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point
	 * @return an optional {@code PBRTBXDFDistributionFunctionResult} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	@Override
	public Optional<PBRTBXDFDistributionFunctionResult> sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F incoming = new Vector3F(-outgoing.getX(), -outgoing.getY(), outgoing.getZ());
		
		final Color3F value = Color3F.divide(Color3F.multiply(this.pBRTFresnel.evaluate(incoming.cosTheta()), this.reflectanceScale), incoming.cosThetaAbs());
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		return Optional.of(new PBRTBXDFDistributionFunctionResult(incoming, outgoing, value, probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PBRTSpecularReflectionBRDF} instance.
	 * 
	 * @return a {@code String} representation of this {@code PBRTSpecularReflectionBRDF} instance
	 */
	@Override
	public String toString() {
		return String.format("new PBRTSpecularReflectionBRDF(%s, %s)", this.reflectanceScale, this.pBRTFresnel);
	}
	
	/**
	 * Compares {@code object} to this {@code PBRTSpecularReflectionBRDF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PBRTSpecularReflectionBRDF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PBRTSpecularReflectionBRDF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PBRTSpecularReflectionBRDF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PBRTSpecularReflectionBRDF)) {
			return false;
		} else if(!Objects.equals(this.reflectanceScale, PBRTSpecularReflectionBRDF.class.cast(object).reflectanceScale)) {
			return false;
		} else if(!Objects.equals(this.pBRTFresnel, PBRTSpecularReflectionBRDF.class.cast(object).pBRTFresnel)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PBRTSpecularReflectionBRDF} instance.
	 * 
	 * @return a hash code for this {@code PBRTSpecularReflectionBRDF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.reflectanceScale, this.pBRTFresnel);
	}
}