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

//TODO: Add Javadocs!
public enum RenderingAlgorithm {
//	TODO: Add Javadocs!
	AMBIENT_OCCLUSION("AmbientOcclusion"),
	
//	TODO: Add Javadocs!
	PATH_TRACING_P_B_R_T("PathTracing-PBRT"),
	
//	TODO: Add Javadocs!
	PATH_TRACING_RAYITO("PathTracing-Rayito"),
	
//	TODO: Add Javadocs!
	PATH_TRACING_SMALL_P_T_ITERATIVE("PathTracing-SmallPT-Iterative"),
	
//	TODO: Add Javadocs!
	PATH_TRACING_SMALL_P_T_RECURSIVE("PathTracing-SmallPT-Recursive"),
	
//	TODO: Add Javadocs!
	RAY_CASTING("RayCasting");
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private RenderingAlgorithm(final String name) {
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public String getName() {
		return this.name;
	}
}