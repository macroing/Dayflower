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
package org.dayflower.scene.microfacet;

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.acos;
import static org.dayflower.utility.Floats.atan;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.error;
import static org.dayflower.utility.Floats.errorInverse;
import static org.dayflower.utility.Floats.exp;
import static org.dayflower.utility.Floats.isInfinite;
import static org.dayflower.utility.Floats.log;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.pow2;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.sqrt;
import static org.dayflower.utility.Floats.tan;

import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code BeckmannMicrofacetDistribution} is an implementation of {@link MicrofacetDistribution} that represents a Beckmann microfacet distribution.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BeckmannMicrofacetDistribution extends MicrofacetDistribution {
	private final float alphaX;
	private final float alphaY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BeckmannMicrofacetDistribution} instance.
	 * 
	 * @param isSamplingVisibleArea {@code true} if, and only if, the visible area should be sampled, {@code false} otherwise
	 * @param isSeparableModel {@code true} if, and only if, the separable shadowing and masking model should be used, {@code false} otherwise
	 * @param alphaX the alpha value on the X-axis
	 * @param alphaY the alpha value on the Y-axis
	 */
	public BeckmannMicrofacetDistribution(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		super(isSamplingVisibleArea, isSeparableModel);
		
		this.alphaX = max(alphaX, 0.001F);
		this.alphaY = max(alphaY, 0.001F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BeckmannMicrofacetDistribution} instance.
	 * 
	 * @return a {@code String} representation of this {@code BeckmannMicrofacetDistribution} instance
	 */
	@Override
	public String toString() {
		return String.format("new BeckmannMicrofacetDistribution(%s, %s, %+.10f, %+.10f)", Boolean.toString(isSamplingVisibleArea()), Boolean.toString(isSeparableModel()), Float.valueOf(this.alphaX), Float.valueOf(this.alphaY));
	}
	
	/**
	 * Samples a halfway vector given {@code outgoing} and {@code sample}.
	 * <p>
	 * Returns a {@link Vector3F} instance with the sampled halfway vector.
	 * <p>
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code Sample_wh(const Vector3f &wo, const Point2f &u)} that returns a {@code Vector3f} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point, called {@code u} in PBRT
	 * @return a {@code Vector3F} instance with the sampled halfway vector
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	@Override
	public Vector3F sampleHalfway(final Vector3F outgoing, final Point2F sample) {
		final float alphaX = this.alphaX;
		final float alphaY = this.alphaY;
		
		final float u = sample.x;
		final float v = sample.y;
		
		if(isSamplingVisibleArea()) {
			return outgoing.z >= 0.0F ? doSample(outgoing, alphaX, alphaY, u, v) : Vector3F.negate(doSample(Vector3F.negate(outgoing), alphaX, alphaY, u, v));
		} else if(equal(alphaX, alphaY)) {
			final float phi = v * 2.0F * PI;
			final float cosTheta = 1.0F / sqrt(1.0F + -alphaX * alphaX * log(1.0F - u));
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F halfway = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F halfwayCorrectlyOriented = Vector3F.sameHemisphereZ(outgoing, halfway) ? halfway : Vector3F.negate(halfway);
			
			return halfwayCorrectlyOriented;
		} else {
			final float phi = atan(alphaY / alphaX * tan(2.0F * PI * v + 0.5F * PI)) + (v > 0.5F ? PI : 0.0F);
			final float cosTheta = 1.0F / sqrt(1.0F + (-log(1.0F - u) / (pow2(cos(phi)) / (alphaX * alphaX) + pow2(sin(phi)) / (alphaY * alphaY))));
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F halfway = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F halfwayCorrectlyOriented = Vector3F.sameHemisphereZ(outgoing, halfway) ? halfway : Vector3F.negate(halfway);
			
			return halfwayCorrectlyOriented;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code BeckmannMicrofacetDistribution} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BeckmannMicrofacetDistribution}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BeckmannMicrofacetDistribution} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BeckmannMicrofacetDistribution}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BeckmannMicrofacetDistribution)) {
			return false;
		} else if(isSamplingVisibleArea() != BeckmannMicrofacetDistribution.class.cast(object).isSamplingVisibleArea()) {
			return false;
		} else if(isSeparableModel() != BeckmannMicrofacetDistribution.class.cast(object).isSeparableModel()) {
			return false;
		} else if(!equal(this.alphaX, BeckmannMicrofacetDistribution.class.cast(object).alphaX)) {
			return false;
		} else if(!equal(this.alphaY, BeckmannMicrofacetDistribution.class.cast(object).alphaY)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Computes the differential area term for {@code halfway}.
	 * <p>
	 * Returns a {@code float} value with the computed differential area term.
	 * <p>
	 * If {@code halfway} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code D(const Vector3f &wh)} that returns a {@code Float} in PBRT.
	 * 
	 * @param halfway the halfway vector, called {@code wh} in PBRT
	 * @return a {@code float} value with the computed differential area term
	 * @throws NullPointerException thrown if, and only if, {@code halfway} is {@code null}
	 */
	@Override
	public float computeDifferentialArea(final Vector3F halfway) {
		final float tanThetaSquared = halfway.tanThetaSquared();
		
		if(isInfinite(tanThetaSquared)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquared = halfway.cosPhiSquared();
		final float sinPhiSquared = halfway.sinPhiSquared();
		
		final float cosThetaQuartic = halfway.cosThetaQuartic();
		
		final float differentialArea = exp(-tanThetaSquared * (cosPhiSquared / alphaXSquared + sinPhiSquared / alphaYSquared)) / (PI * alphaX * alphaY * cosThetaQuartic);
		
		return differentialArea;
	}
	
	/**
	 * Computes the Lambda term for {@code outgoing}.
	 * <p>
	 * Returns a {@code float} value with the computed Lambda term.
	 * <p>
	 * If {@code outgoing} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code Lambda(const Vector3f &w)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code w} in PBRT
	 * @return a {@code float} value with the computed Lambda term
	 * @throws NullPointerException thrown if, and only if, {@code outgoing} is {@code null}
	 */
	@Override
	public float computeLambda(final Vector3F outgoing) {
		final float tanThetaAbs = outgoing.tanThetaAbs();
		
		if(isInfinite(tanThetaAbs)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquared = outgoing.cosPhiSquared();
		final float sinPhiSquared = outgoing.sinPhiSquared();
		
		final float alpha = sqrt(cosPhiSquared * alphaXSquared + sinPhiSquared * alphaYSquared);
		final float alphaReciprocal = 1.0F / (alpha * tanThetaAbs);
		
		if(alphaReciprocal >= 1.6F) {
			return 0.0F;
		}
		
		final float lambda = (1.0F - 1.259F * alphaReciprocal + 0.396F * alphaReciprocal * alphaReciprocal) / (3.535F * alphaReciprocal + 2.181F * alphaReciprocal * alphaReciprocal);
		
		return lambda;
	}
	
	/**
	 * Returns a hash code for this {@code BeckmannMicrofacetDistribution} instance.
	 * 
	 * @return a hash code for this {@code BeckmannMicrofacetDistribution} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(isSamplingVisibleArea()), Boolean.valueOf(isSeparableModel()), Float.valueOf(this.alphaX), Float.valueOf(this.alphaY));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Vector2F doComputeSlope(final float cosTheta, final float u, final float v) {
		if(cosTheta > 0.9999F) {
			final float r = sqrt(-log(1.0F - u));
			final float phi = 2.0F * PI * v;
			final float x = r * cos(phi);
			final float y = r * sin(phi);
			
			return new Vector2F(x, y);
		}
		
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float tanTheta = sinTheta / cosTheta;
		final float cotTheta = 1.0F / tanTheta;
		
		final float sampleX = max(u, 1.0e-6F);
		final float theta = acos(cosTheta);
		final float fit = 1.0F + theta * (-0.876F + theta * (0.4265F - 0.0594F * theta));
		final float sqrtPiReciprocal = 1.0F / sqrt(PI);
		
		float a = -1.0F;
		float b = error(cotTheta);
		float c = b - (1.0F + b) * pow(1.0F - sampleX, fit);
		
		final float normalization = 1.0F / (1.0F + c + sqrtPiReciprocal * tanTheta * exp(-cotTheta * cotTheta));
		
		for(int i = 1; i < 10; i++) {
			if(!(c >= a && c <= b)) {
				c = 0.5F * (a + b);
			}
			
			final float errorReciprocal = errorInverse(c);
			final float value = normalization * (1.0F + b + sqrtPiReciprocal * tanTheta * exp(-errorReciprocal * errorReciprocal)) - sampleX;
			final float derivative = normalization * (1.0F - errorReciprocal * tanTheta);
			
			if(abs(value) < 1.0e-5F) {
				break;
			}
			
			if(value > 0.0F) {
				b = c;
			} else {
				a = c;
			}
			
			b -= value / derivative;
		}
		
		final float x = errorInverse(c);
		final float y = errorInverse(2.0F * max(v, 1.0e-6F) - 1.0F);
		
		return new Vector2F(x, y);
	}
	
	private static Vector3F doSample(final Vector3F i, final float alphaX, final float alphaY, final float u, final float v) {
		final Vector3F iStretched = Vector3F.normalize(new Vector3F(i.x * alphaX, i.y * alphaY, i.z));
		
		final Vector2F slope = doComputeSlope(iStretched.cosTheta(), u, v);
		
		final float x = -((iStretched.cosPhi() * slope.x - iStretched.sinPhi() * slope.y) * alphaX);
		final float y = -((iStretched.sinPhi() * slope.x + iStretched.cosPhi() * slope.y) * alphaY);
		final float z = 1.0F;
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
}