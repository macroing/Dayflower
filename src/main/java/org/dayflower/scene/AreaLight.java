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

import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.util.Ints;

/**
 * An {@code AreaLight} is an implementation of {@link Light} that represents an area light.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AreaLight implements Light {
	private final Matrix44F lightToWorld;
	private final Matrix44F worldToLight;
	private final int sampleCount;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AreaLight} instance.
	 * <p>
	 * If {@code lightToWorld} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lightToWorld the {@link Matrix44F} instance that is used to transform from light space to world space and is associated with this {@code AreaLight} instance
	 * @param sampleCount the sample count associated with this {@code AreaLight} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, {@code lightToWorld} is {@code null}
	 */
	protected AreaLight(final Matrix44F lightToWorld, final int sampleCount) {
		this.lightToWorld = Objects.requireNonNull(lightToWorld, "lightToWorld == null");
		this.worldToLight = Matrix44F.inverse(lightToWorld);
		this.sampleCount = Ints.requireRange(sampleCount, 1, Integer.MAX_VALUE, "sampleCount");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance for {@code intersection} and {@code direction}.
	 * <p>
	 * If either {@code intersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method represents the {@code AreaLight} method {@code L(const Interaction &intr, const Vector3f &w)} that returns a {@code Spectrum} in PBRT.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param direction a {@link Vector3F} instance with a direction
	 * @return a {@code Color3F} instance with the radiance for {@code intersection} and {@code direction}
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code direction} are {@code null}
	 */
	public abstract Color3F evaluateRadiance(final Intersection intersection, final Vector3F direction);
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from light space to world space and is associated with this {@code AreaLight} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from light space to world space and is associated with this {@code AreaLight} instance
	 */
	public final Matrix44F getLightToWorld() {
		return this.lightToWorld;
	}
	
	/**
	 * Returns the {@link Matrix44F} instance that is used to transform from world space to light space and is associated with this {@code AreaLight} instance.
	 * 
	 * @return the {@code Matrix44F} instance that is used to transform from world space to light space and is associated with this {@code AreaLight} instance
	 */
	public final Matrix44F getWorldToLight() {
		return this.worldToLight;
	}
	
	/**
	 * Returns the sample count associated with this {@code AreaLight} instance.
	 * 
	 * @return the sample count associated with this {@code AreaLight} instance
	 */
	public final int getSampleCount() {
		return this.sampleCount;
	}
}