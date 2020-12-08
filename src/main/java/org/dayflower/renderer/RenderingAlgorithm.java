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

import java.util.Objects;

/**
 * A {@code RenderingAlgorithm} represents a rendering algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum RenderingAlgorithm {
	/**
	 * A {@code RenderingAlgorithm} that represents Ambient Occlusion.
	 */
	AMBIENT_OCCLUSION("AmbientOcclusion", "Ambient Occlusion"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Path Tracing.
	 */
	PATH_TRACING("PathTracing", "Path Tracing"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Path Tracing with an implementation similar to PBRT.
	 */
	PATH_TRACING_P_B_R_T("PathTracing-PBRT", "Path Tracing - PBRT"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Path Tracing with an implementation similar to Rayito.
	 */
	PATH_TRACING_RAYITO("PathTracing-Rayito", "Path Tracing - Rayito"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Path Tracing with an implementation similar to SmallPT and is iterative.
	 */
	PATH_TRACING_SMALL_P_T_ITERATIVE("PathTracing-SmallPT-Iterative", "Path Tracing - SmallPT - Iterative"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Path Tracing with an implementation similar to SmallPT and is recursive.
	 */
	PATH_TRACING_SMALL_P_T_RECURSIVE("PathTracing-SmallPT-Recursive", "Path Tracing - SmallPT - Recursive"),
	
	/**
	 * A {@code RenderingAlgorithm} that represents Ray Casting.
	 */
	RAY_CASTING("RayCasting", "Ray Casting");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String name;
	private final String nameExternal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private RenderingAlgorithm(final String name, final String nameExternal) {
		this.name = Objects.requireNonNull(name, "name == null");
		this.nameExternal = Objects.requireNonNull(nameExternal, "nameExternal == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code RenderingAlgorithm} instance.
	 * 
	 * @return the name of this {@code RenderingAlgorithm} instance
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the name of this {@code RenderingAlgorithm} instance in external form.
	 * 
	 * @return the name of this {@code RenderingAlgorithm} instance in external form
	 */
	public String getNameExternal() {
		return this.nameExternal;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RenderingAlgorithm} instance.
	 * 
	 * @return a {@code String} representation of this {@code RenderingAlgorithm} instance
	 */
	@Override
	public String toString() {
		return this.nameExternal;
	}
}