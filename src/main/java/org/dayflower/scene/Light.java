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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.node.Node;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Light} represents a light.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class Light implements Node {
	private Transform transform;
	private final boolean isUsingDeltaDistribution;
	private final int sampleCount;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Light} instance.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code sampleCount} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param transform the {@link Transform} instance associated with this {@code Light} instance
	 * @param sampleCount the sample count associated with this {@code Light} instance
	 * @param isUsingDeltaDistribution {@code true} if, and only if, this {@code Light} instance is using a delta distribution, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code sampleCount} is less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	protected Light(final Transform transform, final int sampleCount, final boolean isUsingDeltaDistribution) {
		this.transform = Objects.requireNonNull(transform, "transform == null");
		this.sampleCount = ParameterArguments.requireRange(sampleCount, 1, Integer.MAX_VALUE, "sampleCount");
		this.isUsingDeltaDistribution = isUsingDeltaDistribution;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance emitted along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance emitted along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@SuppressWarnings("static-method")
	public Color3F evaluateRadianceEmitted(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the power of this {@code Light} instance.
	 * 
	 * @return a {@code Color3F} instance with the power of this {@code Light} instance
	 */
	@SuppressWarnings("static-method")
	public Color3F power() {
		return Color3F.BLACK;
	}
	
	/**
	 * Samples the incoming radiance.
	 * <p>
	 * Returns an optional {@link LightSample} with the result of the sampling.
	 * <p>
	 * If either {@code intersection} or {@code sample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param sample a {@link Point2F} instance
	 * @return an optional {@code LightSample} with the result of the sampling
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code sample} are {@code null}
	 */
	@SuppressWarnings("static-method")
	public Optional<LightSample> sampleRadianceIncoming(final Intersection intersection, final Point2F sample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(sample, "sample == null");
		
		return Optional.empty();
	}
	
	/**
	 * Returns the {@link Transform} instance associated with this {@code Light} instance.
	 * 
	 * @return the {@code Transform} instance associated with this {@code Light} instance
	 */
	public final Transform getTransform() {
		return this.transform;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Light} instance is using a delta distribution, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Light} instance is using a delta distribution, {@code false} otherwise
	 */
	public final boolean isUsingDeltaDistribution() {
		return this.isUsingDeltaDistribution;
	}
	
	/**
	 * Evaluates the probability density function (PDF) for the incoming radiance.
	 * <p>
	 * Returns a {@code float} with the probability density function (PDF) value.
	 * <p>
	 * If either {@code intersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param incoming the incoming direction
	 * @return a {@code float} with the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code intersection} or {@code incoming} are {@code null}
	 */
	@SuppressWarnings("static-method")
	public float evaluateProbabilityDensityFunctionRadianceIncoming(final Intersection intersection, final Vector3F incoming) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns the sample count associated with this {@code Light} instance.
	 * 
	 * @return the sample count associated with this {@code Light} instance
	 */
	public final int getSampleCount() {
		return this.sampleCount;
	}
	
	/**
	 * Sets the {@link Transform} instance associated with this {@code Light} instance to {@code transform}.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform the {@code Transform} instance associated with this {@code Light} instance
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public final void setTransform(final Transform transform) {
		this.transform = Objects.requireNonNull(transform, "transform == null");
	}
}