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
package org.dayflower.scene.bxdf;

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;

//TODO: Add Javadocs!
public final class TorranceSparrowReflectionBRDF implements BXDF {
	private final MicrofacetDistribution microfacetDistribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TorranceSparrowReflectionBRDF(final MicrofacetDistribution microfacetDistribution) {
		this.microfacetDistribution = Objects.requireNonNull(microfacetDistribution, "microfacetDistribution == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return evaluateSolidAngle(o, n, i, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult evaluateSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v) {
		return sampleSolidAngle(o, n, orthonormalBasis, u, v, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public BXDFResult sampleSolidAngle(final Vector3F o, final Vector3F n, final OrthonormalBasis33F orthonormalBasis, final float u, final float v, final boolean isProjected) {
		final OrthonormalBasis33F orthonormalBasisLocal = new OrthonormalBasis33F();
		
		final Vector3F wO = Vector3F.normalize(Vector3F.transform(o, orthonormalBasisLocal));
		
		if(equal(wO.getZ(), 0.0F)) {
			return new BXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		final Vector3F wN = this.microfacetDistribution.sampleN(wO, u, v);
		
		if(Vector3F.dotProduct(wO, wN) < 0.0F) {
			return new BXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		final Vector3F wI = doReflect(wO, wN);
		
		if(wO.getZ() * wN.getZ() <= 0.0F) {
			return new BXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		final float probabilityDensityFunctionValue = this.microfacetDistribution.computeProbabilityDensityFunctionValue(wO, wN) / (4.0F * Vector3F.dotProduct(wO, wN));
		final float cosThetaAbsWO = wO.cosThetaAbs();
		final float cosThetaAbsWI = wI.cosThetaAbs();
		
		if(equal(cosThetaAbsWO, 0.0F) || equal(cosThetaAbsWI, 0.0F)) {
			return new BXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		if(equal(wN.getX(), 0.0F) && equal(wN.getY(), 0.0F) && equal(wN.getZ(), 0.0F)) {
			return new BXDFResult(o, n, new Vector3F(), 0.0F, 0.0F);
		}
		
		final Vector3F wNNormalized = Vector3F.normalize(wN);
		
		final float fresnel = 1.0F;
		final float reflectance = this.microfacetDistribution.computeDifferentialArea(wNNormalized) * this.microfacetDistribution.computeShadowingAndMasking(wO, wI) * fresnel / (4.0F * cosThetaAbsWI * cosThetaAbsWO);
		
		final Vector3F i = Vector3F.normalize(Vector3F.transformReverse(wI, orthonormalBasisLocal));
		
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDiracDistribution() {
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i) {
		return probabilityDensityFunctionSolidAngle(o, n, i, false);
	}
	
//	TODO: Add Javadocs!
	@Override
	public float probabilityDensityFunctionSolidAngle(final Vector3F o, final Vector3F n, final Vector3F i, final boolean isProjected) {
		return 0.0F;//TODO: Implement!
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Vector3F doReflect(final Vector3F o, final Vector3F n) {
		return Vector3F.add(Vector3F.negate(o), Vector3F.multiply(n, 2.0F * Vector3F.dotProduct(o, n)));
	}
}