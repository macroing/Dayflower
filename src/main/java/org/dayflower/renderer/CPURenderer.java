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
package org.dayflower.renderer;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class CPURenderer extends AbstractCPURenderer {
	private RenderingAlgorithm renderingAlgorithm;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public CPURenderer() {
		this(new RendererConfiguration());
	}
	
//	TODO: Add Javadocs!
	public CPURenderer(final RendererConfiguration rendererConfiguration) {
		this(rendererConfiguration, new FileRendererObserver());
	}
	
//	TODO: Add Javadocs!
	public CPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		this(rendererConfiguration, rendererObserver, RenderingAlgorithm.PATH_TRACING_P_B_R_T);
	}
	
//	TODO: Add Javadocs!
	public CPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver, final RenderingAlgorithm renderingAlgorithm) {
		super(rendererConfiguration, rendererObserver);
		
		this.renderingAlgorithm = Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public RenderingAlgorithm getRenderingAlgorithm() {
		return this.renderingAlgorithm;
	}
	
//	TODO: Add Javadocs!
	public void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm) {
		this.renderingAlgorithm = Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Color3F radiance(final Ray3F ray) {
		switch(this.renderingAlgorithm) {
			case AMBIENT_OCCLUSION:
				return RenderingAlgorithms.radianceAmbientOcclusion(ray, getRendererConfiguration());
			case PATH_TRACING_P_B_R_T:
				return RenderingAlgorithms.radiancePathTracingPBRT(ray, getRendererConfiguration());
			case PATH_TRACING_SMALL_P_T_ITERATIVE:
				return RenderingAlgorithms.radiancePathTracingSmallPTIterative(ray, getRendererConfiguration());
			case PATH_TRACING_SMALL_P_T_RECURSIVE:
				return RenderingAlgorithms.radiancePathTracingSmallPTRecursive(ray, getRendererConfiguration());
			case PATH_TRACING_RAYITO:
				return RenderingAlgorithms.radiancePathTracingRayito(ray, getRendererConfiguration());
			case RAY_CASTING:
				return RenderingAlgorithms.radianceRayCasting(ray, getRendererConfiguration());
			default:
				return Color3F.BLACK;
		}
	}
}