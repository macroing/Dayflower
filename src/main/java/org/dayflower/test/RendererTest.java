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
package org.dayflower.test;

import org.dayflower.image.Image;
import org.dayflower.renderer.AmbientOcclusionRenderer;
import org.dayflower.renderer.PathTracer;
import org.dayflower.renderer.Renderer;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestAmbientOcclusionRenderer();
		doTestPathTracer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestAmbientOcclusionRenderer() {
		final
		Renderer renderer = new AmbientOcclusionRenderer();
		renderer.render(new Image(800, 800), Scenes.newDefaultScene());
	}
	
	static void doTestPathTracer() {
		final
		Renderer renderer = new PathTracer();
		renderer.render(new Image(800, 800), Scenes.newTerrainScene());
	}
}