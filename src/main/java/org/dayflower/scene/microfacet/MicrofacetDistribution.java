/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.log;
import static org.dayflower.utility.Floats.max;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code MicrofacetDistribution} represents a microfacet distribution.
 * <p>
 * All official implementations of this class are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class MicrofacetDistribution {
	private final boolean isSamplingVisibleArea;
	private final boolean isSeparableModel;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MicrofacetDistribution} instance.
	 * 
	 * @param isSamplingVisibleArea {@code true} if, and only if, the visible area should be sampled, {@code false} otherwise
	 * @param isSeparableModel {@code true} if, and only if, the separable shadowing and masking model should be used, {@code false} otherwise
	 */
	protected MicrofacetDistribution(final boolean isSamplingVisibleArea, final boolean isSeparableModel) {
		this.isSamplingVisibleArea = isSamplingVisibleArea;
		this.isSeparableModel = isSeparableModel;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public abstract Vector3F sampleHalfway(final Vector3F outgoing, final Point2F sample);
	
	/**
	 * Returns {@code true} if, and only if, this {@code MicrofacetDistribution} instance is sampling the visible area, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code MicrofacetDistribution} instance is sampling the visible area, {@code false} otherwise
	 */
	public final boolean isSamplingVisibleArea() {
		return this.isSamplingVisibleArea;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code MicrofacetDistribution} instance is using the separable shadowing and masking model, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code MicrofacetDistribution} instance is using the separable shadowing and masking model, {@code false} otherwise
	 */
	public final boolean isSeparableModel() {
		return this.isSeparableModel;
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
	public abstract float computeDifferentialArea(final Vector3F halfway);
	
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
	public abstract float computeLambda(final Vector3F outgoing);
	
	/**
	 * Computes the probability density function (PDF) value for {@code outgoing} and {@code halfway}.
	 * <p>
	 * Returns a {@code float} value with the computed probability density function (PDF) value.
	 * <p>
	 * If either {@code outgoing} or {@code halfway} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code Pdf(const Vector3f &wo, const Vector3f &wh)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param halfway the halfway vector, called {@code wh} in PBRT
	 * @return a {@code float} value with the computed probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code halfway} are {@code null}
	 */
	public final float computeProbabilityDensityFunctionValue(final Vector3F outgoing, final Vector3F halfway) {
		return this.isSamplingVisibleArea ? computeDifferentialArea(halfway) * computeShadowingAndMasking(outgoing) * abs(Vector3F.dotProduct(outgoing, halfway)) / outgoing.cosThetaAbs() : computeDifferentialArea(halfway) * halfway.cosThetaAbs();
	}
	
	/**
	 * Computes the shadowing and masking term for {@code outgoing}.
	 * <p>
	 * Returns a {@code float} value with the computed shadowing and masking term.
	 * <p>
	 * If {@code outgoing} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code G1(const Vector3f &w)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code w} in PBRT
	 * @return a {@code float} value with the computed shadowing and masking term
	 * @throws NullPointerException thrown if, and only if, {@code outgoing} is {@code null}
	 */
	public final float computeShadowingAndMasking(final Vector3F outgoing) {
		return 1.0F / (1.0F + computeLambda(outgoing));
	}
	
	/**
	 * Computes the shadowing and masking term for {@code outgoing} and {@code incoming}.
	 * <p>
	 * Returns a {@code float} value with the computed shadowing and masking term.
	 * <p>
	 * If either {@code outgoing} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code MicrofacetDistribution} method {@code G(const Vector3f &wo, const Vector3f &wi)} that returns a {@code Float} in PBRT.
	 * 
	 * @param outgoing the outgoing direction, called {@code wo} in PBRT
	 * @param incoming the incoming direction, called {@code wi} in PBRT
	 * @return a {@code float} value with the computed shadowing and masking term
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing} or {@code incoming} are {@code null}
	 */
	public final float computeShadowingAndMasking(final Vector3F outgoing, final Vector3F incoming) {
		return this.isSeparableModel ? computeShadowingAndMasking(outgoing) * computeShadowingAndMasking(incoming) : 1.0F / (1.0F + computeLambda(outgoing) + computeLambda(incoming));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Converts {@code roughness} to the alpha value used by some {@code MicrofacetDistribution} implementations.
	 * <p>
	 * Returns a {@code float} value with the alpha representation of {@code roughness}.
	 * 
	 * @param roughness the roughness value to convert
	 * @return a {@code float} value with the alpha representation of {@code roughness}
	 */
	public static float convertRoughnessToAlpha(final float roughness) {
		final float x = max(roughness, 1.0e-3F);
		final float y = log(x);
		final float z = 1.62142F + 0.819955F * y + 0.1734F * y * y + 0.0171201F * y * y * y + 0.000640711F * y * y * y * y;
		
		return z;
	}
}