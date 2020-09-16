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
	public abstract Vector3F sampleN(final Vector3F o, final Point2F p);
	
//	TODO: Add Javadocs!
	public abstract float computeDifferentialArea(final Vector3F n);
	
//	TODO: Add Javadocs!
	public abstract float computeLambda(final Vector3F v);
	
//	TODO: Add Javadocs!
	public abstract float computeProbabilityDensityFunctionValue(final Vector3F o, final Vector3F n);
	
//	TODO: Add Javadocs!
	public final float computeShadowingAndMasking(final Vector3F v) {
		return 1.0F / (1.0F + computeLambda(v));
	}
	
//	TODO: Add Javadocs!
	public final float computeShadowingAndMasking(final Vector3F o, final Vector3F i) {
		return 1.0F / (1.0F + computeLambda(o) + computeLambda(i));
	}
}