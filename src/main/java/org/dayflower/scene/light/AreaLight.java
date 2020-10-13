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
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;

//TODO: Add Javadocs!
public abstract class AreaLight implements Light {
	private final Matrix44F lightToWorld;
	private final int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected AreaLight(final Matrix44F lightToWorld, final int samples) {
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
		this.samples = samples;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public abstract Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction);
	
//	TODO: Add Javadocs!
	public final Matrix44F getLightToWorld() {
		return this.lightToWorld;
	}
	
//	TODO: Add Javadocs!
	public final int getSamples() {
		return this.samples;
	}
}