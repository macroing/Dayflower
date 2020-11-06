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
package org.dayflower.scene;

import java.util.List;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;

/**
 * A {@code Background} represents the background.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Background {
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	Color3F radiance(final Ray3F ray);
	
	/**
	 * Samples this {@code Background} instance from {@code intersection}.
	 * <p>
	 * Returns a {@code List} of {@link BackgroundSample} instances.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} instance to sample from
	 * @return a {@code List} of {@code BackgroundSample} instances
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	List<BackgroundSample> sample(final Intersection intersection);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Background} instance that represents a black background.
	 * 
	 * @return a new {@code Background} instance that represents a black background
	 */
	static Background newBlackBackground() {
		return new BlackBackground();
	}
}