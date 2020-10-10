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

import org.dayflower.display.FileDisplay;
import org.dayflower.image.Image;
import org.dayflower.renderer.AmbientOcclusionRenderer;
import org.dayflower.renderer.PathTracer;
import org.dayflower.renderer.RayCaster;
import org.dayflower.renderer.Renderer;
import org.dayflower.scene.Scene;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestAmbientOcclusionRenderer();
		doTestPathTracerPBRT();
//		doTestPathTracerRayito();
//		doTestPathTracerSmallPTIterative();
//		doTestPathTracerSmallPTRecursive();
//		doTestRayCaster();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestAmbientOcclusionRenderer() {
		doTestRenderer(new AmbientOcclusionRenderer(), Scenes.newBedroomScene());
	}
	
	static void doTestPathTracerPBRT() {
		doTestRenderer(new PathTracer(PathTracer.TYPE_P_B_R_T), Scenes.newPBRTScene());
	}
	
	static void doTestPathTracerRayito() {
		doTestRenderer(new PathTracer(PathTracer.TYPE_RAYITO), Scenes.newDefaultScene());
	}
	
	static void doTestPathTracerSmallPTIterative() {
		doTestRenderer(new PathTracer(PathTracer.TYPE_SMALL_P_T_ITERATIVE), Scenes.newDefaultScene());
	}
	
	static void doTestPathTracerSmallPTRecursive() {
		doTestRenderer(new PathTracer(PathTracer.TYPE_SMALL_P_T_RECURSIVE), Scenes.newDefaultScene());
	}
	
	static void doTestRayCaster() {
		doTestRenderer(new RayCaster(), Scenes.newBedroomScene());
	}
	
	static void doTestRenderer(final Renderer renderer, final Scene scene) {
		final int resolutionX = (int)(scene.getCamera().getResolutionX());
		final int resolutionY = (int)(scene.getCamera().getResolutionY());
		
		renderer.render(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), scene.getName())), new Image(resolutionX, resolutionY), scene);
	}
}