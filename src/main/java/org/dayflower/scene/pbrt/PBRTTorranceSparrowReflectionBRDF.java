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
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public final class PBRTTorranceSparrowReflectionBRDF implements PBRTBXDF {
	private final PBRTMicrofacetDistribution pBRTMicrofacetDistribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public PBRTTorranceSparrowReflectionBRDF(final PBRTMicrofacetDistribution pBRTMicrofacetDistribution) {
		this.pBRTMicrofacetDistribution = Objects.requireNonNull(pBRTMicrofacetDistribution, "pBRTMicrofacetDistribution == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Optional<PBRTBXDFDistributionFunctionResult> evaluateDistributionFunction(final Vector3F outgoing, final Vector3F incoming) {
		return Optional.empty();
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<PBRTBXDFDistributionFunctionResult> sampleDistributionFunction(final Vector3F outgoing, final Point2F sample) {
		if(equal(outgoing.getZ(), 0.0F)) {
			return Optional.empty();
		}
		
		final Vector3F normal = this.pBRTMicrofacetDistribution.sampleN(outgoing, sample.getU(), sample.getV());
		
		if(Vector3F.dotProduct(outgoing, normal) < 0.0F) {
			return Optional.empty();
		}
		
		final Vector3F incoming = Vector3F.reflection(outgoing, normal);
		
		if(outgoing.getZ() * normal.getZ() <= 0.0F) {
			return Optional.empty();
		}
		
		final float probabilityDensityFunctionValue = this.pBRTMicrofacetDistribution.computeProbabilityDensityFunctionValue(outgoing, normal) / (4.0F * Vector3F.dotProduct(outgoing, normal));
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
		final float reflectance = this.pBRTMicrofacetDistribution.computeDifferentialArea(normalNormalized) * this.pBRTMicrofacetDistribution.computeShadowingAndMasking(outgoing, incoming) * fresnel / (4.0F * cosThetaAbsIncoming * cosThetaAbsOutgoing);
		
		return Optional.empty();
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<PBRTBXDFReflectanceFunctionResult> computeReflectanceFunction(final int samples) {
		return Optional.empty();
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<PBRTBXDFReflectanceFunctionResult> computeReflectanceFunction(final int samples, final Vector3F outgoing) {
		return Optional.empty();
	}
}