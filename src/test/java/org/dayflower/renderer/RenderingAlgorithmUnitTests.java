/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

@SuppressWarnings("static-method")
public final class RenderingAlgorithmUnitTests {
	public RenderingAlgorithmUnitTests() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Test
	public void testGetName() {
		assertEquals("AmbientOcclusion", RenderingAlgorithm.AMBIENT_OCCLUSION.getName());
		assertEquals("PathTracing", RenderingAlgorithm.PATH_TRACING.getName());
		assertEquals("RayCasting", RenderingAlgorithm.RAY_CASTING.getName());
		assertEquals("RayTracing", RenderingAlgorithm.RAY_TRACING.getName());
	}
	
	@Test
	public void testGetNameExternal() {
		assertEquals("Ambient Occlusion", RenderingAlgorithm.AMBIENT_OCCLUSION.getNameExternal());
		assertEquals("Path Tracing", RenderingAlgorithm.PATH_TRACING.getNameExternal());
		assertEquals("Ray Casting", RenderingAlgorithm.RAY_CASTING.getNameExternal());
		assertEquals("Ray Tracing", RenderingAlgorithm.RAY_TRACING.getNameExternal());
	}
	
	@Test
	public void testToString() {
		assertEquals("Ambient Occlusion", RenderingAlgorithm.AMBIENT_OCCLUSION.toString());
		assertEquals("Path Tracing", RenderingAlgorithm.PATH_TRACING.toString());
		assertEquals("Ray Casting", RenderingAlgorithm.RAY_CASTING.toString());
		assertEquals("Ray Tracing", RenderingAlgorithm.RAY_TRACING.toString());
	}
}