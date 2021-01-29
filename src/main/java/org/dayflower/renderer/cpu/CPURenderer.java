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
package org.dayflower.renderer.cpu;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

/**
 * A {@code CPURenderer} is an implementation of {@link AbstractCPURenderer} that supports various rendering algorithms.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CPURenderer extends AbstractCPURenderer {
	/**
	 * Constructs a new {@code CPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CPURenderer(new FileRendererObserver());
	 * }
	 * </pre>
	 */
	public CPURenderer() {
		this(new FileRendererObserver());
	}
	
	/**
	 * Constructs a new {@code CPURenderer} instance.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code CPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	public CPURenderer(final RendererObserver rendererObserver) {
		super(rendererObserver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	protected Color3F radiance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		switch(getRenderingAlgorithm()) {
			case AMBIENT_OCCLUSION:
				return AmbientOcclusion.radiance(ray, getScene(), isPreviewMode(), getMaximumDistance(), getSamples());
			case PATH_TRACING:
				return PathTracingPBRT.radiance(ray, getScene(), isPreviewMode(), getMaximumBounce(), getMinimumBounceRussianRoulette());
			case PATH_TRACING_P_B_R_T:
				return PathTracingPBRT.radiance(ray, getScene(), isPreviewMode(), getMaximumBounce(), getMinimumBounceRussianRoulette());
			case PATH_TRACING_RAYITO:
				return PathTracingRayito.radiance(ray, getScene(), isPreviewMode(), getMaximumBounce(), getMinimumBounceRussianRoulette());
			case PATH_TRACING_SMALL_P_T_ITERATIVE:
				return PathTracingSmallPTIterative.radiance(ray, getScene(), isPreviewMode(), getMaximumBounce(), getMinimumBounceRussianRoulette());
			case PATH_TRACING_SMALL_P_T_RECURSIVE:
				return PathTracingSmallPTRecursive.radiance(ray, getScene(), getMaximumBounce(), getMinimumBounceRussianRoulette());
			case RAY_CASTING:
				return RayCasting.radiance(ray, getScene(), isPreviewMode());
			case RAY_TRACING_P_B_R_T:
				return RayTracingPBRT.radiance(ray, getSampler(), getScene(), isPreviewMode(), getMaximumBounce(), 0);
			default:
				return Color3F.BLACK;
		}
	}
}