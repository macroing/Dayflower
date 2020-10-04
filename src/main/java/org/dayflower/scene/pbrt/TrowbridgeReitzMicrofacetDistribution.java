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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.isInfinite;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.sqrt;

import java.lang.reflect.Field;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public final class TrowbridgeReitzMicrofacetDistribution extends MicrofacetDistribution {
	private final float alphaX;
	private final float alphaY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TrowbridgeReitzMicrofacetDistribution(final boolean isSamplingVisibleArea, final float alphaX, final float alphaY) {
		super(isSamplingVisibleArea);
		
		this.alphaX = max(alphaX, 0.001F);
		this.alphaY = max(alphaY, 0.001F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Vector3F sampleNormal(final Vector3F outgoing, final Point2F sample) {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float computeDifferentialArea(final Vector3F normal) {
		final float tanThetaSquared = normal.tanThetaSquared();
		
		if(isInfinite(tanThetaSquared)) {
			return 0.0F;
		}
		
		final float alphaX = this.alphaX;
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = this.alphaY;
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquared = normal.cosPhiSquared();
		final float sinPhiSquared = normal.sinPhiSquared();
		
		final float cosThetaQuartic = normal.cosThetaQuartic();
		
		final float exponent = (cosPhiSquared / alphaXSquared + sinPhiSquared / alphaYSquared) * tanThetaSquared;
		
		final float differentialArea = 1.0F / (PI * alphaX * alphaY * cosThetaQuartic * (1.0F + exponent) * (1.0F + exponent));
		
		return differentialArea;
	}
	
//	TODO: Add Javadocs!
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
		final float alphaTanThetaAbsSquared = (alpha * tanThetaAbs) * (alpha * tanThetaAbs);
		
		final float lambda = (-1.0F + sqrt(1.0F + alphaTanThetaAbsSquared)) / 2.0F;
		
		return lambda;
	}
}