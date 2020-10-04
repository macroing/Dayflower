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

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Lists;

//TODO: Add Javadocs!
public final class TorranceSparrowReflectionBRDF extends BXDF {
	private final MicrofacetDistribution microfacetDistribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TorranceSparrowReflectionBRDF(final MicrofacetDistribution microfacetDistribution) {
		super(null);//TODO: Implement!
		
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
		Lists.requireNonNullList(samplesA, "samplesA");
		Lists.requireNonNullList(samplesB, "samplesB");
		
		return null;//TODO: Implement!
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
		
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return Color3F.BLACK;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<BXDFDistributionFunctionResult> sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(sample, "sample == null");
		
		if(equal(outgoing.getZ(), 0.0F)) {
			return Optional.empty();
		}
		
		final Vector3F normal = this.microfacetDistribution.sampleNormal(outgoing, sample);
		
		if(Vector3F.dotProduct(outgoing, normal) < 0.0F) {
			return Optional.empty();
		}
		
		final Vector3F incoming = Vector3F.reflection(outgoing, normal);
		
		if(outgoing.getZ() * normal.getZ() <= 0.0F) {
			return Optional.empty();
		}
		
		final float probabilityDensityFunctionValue = this.microfacetDistribution.computeProbabilityDensityFunctionValue(outgoing, normal) / (4.0F * Vector3F.dotProduct(outgoing, normal));
		final float cosThetaAbsOutgoing = outgoing.cosThetaAbs();
		final float cosThetaAbsIncoming = incoming.cosThetaAbs();
		
		if(equal(cosThetaAbsOutgoing, 0.0F) || equal(cosThetaAbsIncoming, 0.0F)) {
			return Optional.empty();
		}
		
		if(equal(normal.getX(), 0.0F) && equal(normal.getY(), 0.0F) && equal(normal.getZ(), 0.0F)) {
			return Optional.empty();
		}
		
		final Vector3F normalNormalized = Vector3F.normalize(normal);
		
		final float fresnel = 1.0F;
		final float reflectance = this.microfacetDistribution.computeDifferentialArea(normalNormalized) * this.microfacetDistribution.computeShadowingAndMasking(outgoing, incoming) * fresnel / (4.0F * cosThetaAbsIncoming * cosThetaAbsOutgoing);
		
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunction(final Vector3F outgoing, final Vector3F incoming) {
		Objects.requireNonNull(outgoing, "outgoing == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;//TODO: Implement!
	}
}