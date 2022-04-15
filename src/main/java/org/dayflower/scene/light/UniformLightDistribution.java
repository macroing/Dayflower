/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.sampler.Distribution1F;
import org.dayflower.scene.Scene;

import org.macroing.java.util.Arrays;

/**
 * A {@code UniformLightDistribution} is an implementation of {@link LightDistribution} that is using a uniform probability distribution.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class UniformLightDistribution implements LightDistribution {
	private final Distribution1F distribution;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code UniformLightDistribution} instance.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene a {@link Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	public UniformLightDistribution(final Scene scene) {
		this.distribution = new Distribution1F(Arrays.repeat(new float[] {1.0F}, scene.getLightCount()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Distribution1F} given {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a {@code Distribution1F} given {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Distribution1F find(final Point3F point) {
		Objects.requireNonNull(point, "point == null");
		
		return this.distribution;
	}
}