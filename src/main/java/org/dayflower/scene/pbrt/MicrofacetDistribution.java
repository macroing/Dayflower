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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.log;
import static org.dayflower.util.Floats.max;

import java.lang.reflect.Field;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Vector3F;

//TODO: Add Javadocs!
public abstract class MicrofacetDistribution {
	private final boolean isSamplingVisibleArea;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected MicrofacetDistribution(final boolean isSamplingVisibleArea) {
		this.isSamplingVisibleArea = isSamplingVisibleArea;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public abstract Vector3F sampleNormal(final Vector3F outgoing, final Point2F sample);
	
//	TODO: Add Javadocs!
	public final boolean isSamplingVisibleArea() {
		return this.isSamplingVisibleArea;
	}
	
//	TODO: Add Javadocs!
	public abstract float computeDifferentialArea(final Vector3F normal);
	
//	TODO: Add Javadocs!
	public abstract float computeLambda(final Vector3F outgoing);
	
//	TODO: Add Javadocs!
	public final float computeProbabilityDensityFunctionValue(final Vector3F outgoing, final Vector3F normal) {
		return this.isSamplingVisibleArea ? computeDifferentialArea(normal) * computeShadowingAndMasking(outgoing) * abs(Vector3F.dotProduct(outgoing, normal)) / outgoing.cosThetaAbs() : computeDifferentialArea(normal) * normal.cosThetaAbs();
	}
	
//	TODO: Add Javadocs!
	public final float computeShadowingAndMasking(final Vector3F outgoing) {
		return 1.0F / (1.0F + computeLambda(outgoing));
	}
	
//	TODO: Add Javadocs!
	public final float computeShadowingAndMasking(final Vector3F outgoing, final Vector3F incoming) {
		return 1.0F / (1.0F + computeLambda(outgoing) + computeLambda(incoming));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static float computeRoughnessToAlpha(final float roughness) {
		final float x = max(roughness, 1.0e-3F);
		final float y = log(x);
		final float z = 1.62142F + 0.819955F * y + 0.1734F * y * y + 0.0171201F * y * y * y + 0.000640711F * y * y * y * y;
		
		return z;
	}
}