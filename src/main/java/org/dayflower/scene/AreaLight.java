/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Vector3F;

/**
 * An {@code AreaLight} is an implementation of {@link Light} that represents an area light.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AreaLight extends Light {
	/**
	 * Constructs a new {@code AreaLight} instance.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	protected AreaLight(final Transform transform, final int sampleCount) {
		super(transform, sampleCount, false);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}.
	 * <p>
	 * If either {@code intersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance from which the radiance is emitted
	 * @param direction a {@link Vector3F} instance with a direction in which the radiance is emitted
	 * @return a {@code Color3F} instance with the radiance on {@code intersection} emitted in the direction of {@code direction}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code direction} are {@code null}
	 */
	public abstract Color3F evaluateRadianceEmitted(final Intersection intersection, final Vector3F direction);
}