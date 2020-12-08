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
package org.dayflower.renderer.cpu;

import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;
import org.dayflower.renderer.RendererConfiguration;
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
	 * new CPURenderer(new RendererConfiguration());
	 * }
	 * </pre>
	 */
	public CPURenderer() {
		this(new RendererConfiguration());
	}
	
	/**
	 * Constructs a new {@code CPURenderer} instance.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new CPURenderer(rendererConfiguration, new FileRendererObserver());
	 * }
	 * </pre>
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code CPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	public CPURenderer(final RendererConfiguration rendererConfiguration) {
		this(rendererConfiguration, new FileRendererObserver());
	}
	
	/**
	 * Constructs a new {@code CPURenderer} instance.
	 * <p>
	 * If either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code CPURenderer} instance
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code CPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}
	 */
	public CPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		super(rendererConfiguration, rendererObserver);
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
		
		switch(getRendererConfiguration().getRenderingAlgorithm()) {
			case AMBIENT_OCCLUSION:
				return AmbientOcclusion.radiance(ray, getRendererConfiguration());
			case PATH_TRACING:
				return PathTracing.radiance(ray, getRendererConfiguration());
			case PATH_TRACING_P_B_R_T:
				return PathTracingPBRT.radiance(ray, getRendererConfiguration());
			case PATH_TRACING_RAYITO:
				return PathTracingRayito.radiance(ray, getRendererConfiguration());
			case PATH_TRACING_SMALL_P_T_ITERATIVE:
				return PathTracingSmallPTIterative.radiance(ray, getRendererConfiguration());
			case PATH_TRACING_SMALL_P_T_RECURSIVE:
				return PathTracingSmallPTRecursive.radiance(ray, getRendererConfiguration());
			case RAY_CASTING:
				return RayCasting.radiance(ray, getRendererConfiguration());
			default:
				return Color3F.BLACK;
		}
	}
}