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

import static org.dayflower.util.Floats.array;

import java.lang.reflect.Field;

import org.dayflower.geometry.Point3F;
import org.dayflower.sampler.Distribution1F;
import org.dayflower.scene.LightDistribution;
import org.dayflower.scene.Scene;

//TODO: Add Javadocs!
public final class UniformLightDistribution implements LightDistribution {
	private final Distribution1F distribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public UniformLightDistribution(final Scene scene) {
		this.distribution = new Distribution1F(array(scene.getLightCount(), 1.0F));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Distribution1F find(final Point3F point) {
		return this.distribution;
	}
}