/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.atan;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isInfinite;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Floats.tan;

import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code TrowbridgeReitzMicrofacetDistribution} is an implementation of {@link MicrofacetDistribution} that represents a Trowbridge-Reitz microfacet distribution.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TrowbridgeReitzMicrofacetDistribution extends MicrofacetDistribution {
	private final float alphaX;
	private final float alphaY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TrowbridgeReitzMicrofacetDistribution} instance.
	 * 
	 * @param isSamplingVisibleArea {@code true} if, and only if, the visible area should be sampled, {@code false} otherwise
	 * @param alphaX the alpha value on the X-axis
	 * @param alphaY the alpha value on the Y-axis
	 */
	public TrowbridgeReitzMicrofacetDistribution(final boolean isSamplingVisibleArea, final float alphaX, final float alphaY) {
		super(isSamplingVisibleArea);
		
		this.alphaX = max(alphaX, 0.001F);
		this.alphaY = max(alphaY, 0.001F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code TrowbridgeReitzMicrofacetDistribution} instance.
	 * 
	 * @return a {@code String} representation of this {@code TrowbridgeReitzMicrofacetDistribution} instance
	 */
	@Override
	public String toString() {
		return String.format("new TrowbridgeReitzMicrofacetDistribution(%s, %+.10f, %+.10f)", Boolean.toString(isSamplingVisibleArea()), Float.valueOf(this.alphaX), Float.valueOf(this.alphaY));
	}
	
	/**
	 * Samples a normal given {@code outgoing} and {@code sample}.
	 * <p>
	 * Returns a {@link Vector3F} instance with the sampled normal.
	 * <p>
	 * If either {@code outgoing} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code Sample_wh(const Vector3f &wo, const Point2f &u)} that returns a {@code Vector3f} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param sample the sample point, called {@code u} in PBRT
	 * @return a {@code Vector3F} instance with the sampled normal
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code sample} are {@code null}
	 */
	@Override
	public Vector3F sampleNormal(final Vector3F outgoing, final Point2F sample) {
		final float alphaX = this.alphaX;
		final float alphaY = this.alphaY;
		
		final float u = sample.getU();
		final float v = sample.getV();
		
		if(isSamplingVisibleArea()) {
			if(outgoing.getZ() >= 0.0F) {
				return doSample(outgoing, alphaX, alphaY, u, v);
			}
			
			return Vector3F.negate(doSample(Vector3F.negate(outgoing), alphaX, alphaY, u, v));
		} else if(equal(alphaX, alphaY)) {
			final float phi = v * 2.0F * PI;
			final float tanThetaSquared = alphaX * alphaX * u / (1.0F - u);
			final float cosTheta = 1.0F / sqrt(1.0F + tanThetaSquared);
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F normal = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F normalCorrectlyOriented = Vector3F.sameHemisphere(outgoing, normal) ? normal : Vector3F.negate(normal);
			
			return normalCorrectlyOriented;
		} else {
			final float phi = atan(alphaY / alphaX * tan(2.0F * PI * v + 0.5F * PI)) + (v > 0.5F ? PI : 0.0F);
			final float cosPhi = cos(phi);
			final float sinPhi = sin(phi);
			final float alphaXSquared = alphaX * alphaX;
			final float alphaYSquared = alphaY * alphaY;
			final float alphaSquared = 1.0F / (cosPhi * cosPhi / alphaXSquared + sinPhi * sinPhi / alphaYSquared);
			final float tanThetaSquared = alphaSquared * u / (1.0F - u);
			final float cosTheta = 1.0F / sqrt(1.0F + tanThetaSquared);
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final Vector3F normal = Vector3F.directionSpherical(sinTheta, cosTheta, phi);
			final Vector3F normalCorrectlyOriented = Vector3F.sameHemisphere(outgoing, normal) ? normal : Vector3F.negate(normal);
			
			return normalCorrectlyOriented;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code TrowbridgeReitzMicrofacetDistribution} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TrowbridgeReitzMicrofacetDistribution}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TrowbridgeReitzMicrofacetDistribution} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TrowbridgeReitzMicrofacetDistribution}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TrowbridgeReitzMicrofacetDistribution)) {
			return false;
		} else if(isSamplingVisibleArea() != TrowbridgeReitzMicrofacetDistribution.class.cast(object).isSamplingVisibleArea()) {
			return false;
		} else if(!equal(this.alphaX, TrowbridgeReitzMicrofacetDistribution.class.cast(object).alphaX)) {
			return false;
		} else if(!equal(this.alphaY, TrowbridgeReitzMicrofacetDistribution.class.cast(object).alphaY)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Computes the differential area term for {@code normal}.
	 * <p>
	 * Returns a {@code float} value with the computed differential area term.
	 * <p>
	 * If {@code normal} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code D(const Vector3f &wh)} that returns a {@code Float} in PBRT.
	 * 
	 * @param normal the normal, called {@code wh} in PBRT
	 * @return a {@code float} value with the computed differential area term
	 * @throws NullPointerException thrown if, and only if, {@code normal} is {@code null}
	 */
	@Override
	public float computeDifferentialArea(final Vector3F normal) {
		final float tanThetaSquaredNormal = normal.tanThetaSquared();
		
		if(isInfinite(tanThetaSquaredNormal)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquaredNormal = normal.cosPhiSquared();
		final float sinPhiSquaredNormal = normal.sinPhiSquared();
		
		final float cosThetaQuarticNormal = normal.cosThetaQuartic();
		
		final float exponent = (cosPhiSquaredNormal / alphaXSquared + sinPhiSquaredNormal / alphaYSquared) * tanThetaSquaredNormal;
		
		final float differentialArea = 1.0F / (PI * alphaX * alphaY * cosThetaQuarticNormal * (1.0F + exponent) * (1.0F + exponent));
		
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
		final float tanThetaAbsOutgoing = outgoing.tanThetaAbs();
		
		if(isInfinite(tanThetaAbsOutgoing)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaY = this.alphaY;
		
		final float cosPhiSquaredOutgoing = outgoing.cosPhiSquared();
		final float sinPhiSquaredOutgoing = outgoing.sinPhiSquared();
		
		final float alpha = sqrt(cosPhiSquaredOutgoing * alphaX * alphaX + sinPhiSquaredOutgoing * alphaY * alphaY);
		final float alphaTanThetaAbsOutgoingSquared = (alpha * tanThetaAbsOutgoing) * (alpha * tanThetaAbsOutgoing);
		
		final float lambda = (-1.0F + sqrt(1.0F + alphaTanThetaAbsOutgoingSquared)) / 2.0F;
		
		return lambda;
	}
	
	/**
	 * Returns a hash code for this {@code TrowbridgeReitzMicrofacetDistribution} instance.
	 * 
	 * @return a hash code for this {@code TrowbridgeReitzMicrofacetDistribution} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Boolean.valueOf(isSamplingVisibleArea()), Float.valueOf(this.alphaX), Float.valueOf(this.alphaY));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Vector2F doComputeSlope(final float cosThetaIncoming, final float u, final float v) {
		if(cosThetaIncoming > 0.9999F) {
			final float r = sqrt(u / (1.0F - u));
			final float phi = 2.0F * PI * v;
			
			final float cosPhi = cos(phi);
			final float sinPhi = sin(phi);
			
			final float x = r * cosPhi;
			final float y = r * sinPhi;
			
			return new Vector2F(x, y);
		}
		
		final float sinThetaIncoming = sqrt(max(0.0F, 1.0F - cosThetaIncoming * cosThetaIncoming));
		final float tanThetaIncoming = sinThetaIncoming / cosThetaIncoming;
		final float tanThetaIncomingReciprocal = 1.0F / tanThetaIncoming;
		
		final float a = 2.0F / (1.0F + sqrt(1.0F + 1.0F / (tanThetaIncomingReciprocal * tanThetaIncomingReciprocal)));
		final float b = 2.0F * u / a - 1.0F;
		final float c = min(1.0F / (b * b - 1.0F), 1.0e10F);
		final float d = tanThetaIncoming;
		final float e = sqrt(max(d * d * c * c - (b * b - d * d) * c, 0.0F));
		final float f = d * c - e;
		final float g = d * c + e;
		final float h = v > 0.5F ? 1.0F : -1.0F;
		final float i = v > 0.5F ? 2.0F * (v - 0.5F) : 2.0F * (0.5F - v);
		final float j = (i * (i * (i * 0.27385F - 0.73369F) + 0.46341F)) / (i * (i * (i * 0.093073F + 0.309420F) - 1.000000F) + 0.597999F);
		
		final float x = b < 0.0F || g > 1.0F / tanThetaIncoming ? f : g;
		final float y = h * j * sqrt(1.0F + x * x);
		
		return new Vector2F(x, y);
	}
	
	private static Vector3F doSample(final Vector3F incoming, final float alphaX, final float alphaY, final float u, final float v) {
		final Vector3F incomingStretched = Vector3F.normalize(new Vector3F(incoming.getX() * alphaX, incoming.getY() * alphaY, incoming.getZ()));
		
		final Vector2F slope = doComputeSlope(incomingStretched.cosTheta(), u, v);
		
		final float x = -((incomingStretched.cosPhi() * slope.getX() - incomingStretched.sinPhi() * slope.getY()) * alphaX);
		final float y = -((incomingStretched.sinPhi() * slope.getX() + incomingStretched.cosPhi() * slope.getY()) * alphaY);
		final float z = 1.0F;
		
		return Vector3F.normalize(new Vector3F(x, y, z));
	}
}