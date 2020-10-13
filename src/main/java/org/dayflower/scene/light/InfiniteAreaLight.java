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
package org.dayflower.scene.light;

import java.lang.reflect.Field;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.LightRadianceEmittedResult;
import org.dayflower.scene.LightRadianceIncomingResult;

//TODO: Add Javadocs!
public final class InfiniteAreaLight extends AreaLight {
//	TODO: Add Javadocs!
	public InfiniteAreaLight(final Matrix44F lightToWorld, final int samples) {
		super(lightToWorld, samples);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction) {
		return Color3F.BLACK;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		return Color3F.BLACK;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Color3F power() {
		return Color3F.BLACK;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceEmittedResult> evaluateProbabilityDensityFunctionRadianceEmitted(final Ray3F ray, final Vector3F normal) {
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceEmittedResult> sampleRadianceEmitted(final Point2F sampleA, final Point2F sampleB) {
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<LightRadianceIncomingResult> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		return Optional.empty();//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return "";//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		return false;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean isDeltaDistribution() {
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		return 0.0F;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return 0;//TODO: Implement!
	}
}