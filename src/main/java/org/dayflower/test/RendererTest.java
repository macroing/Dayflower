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
import org.dayflower.renderer.AmbientOcclusionCPURenderer;
import org.dayflower.renderer.PBRTPathTracingCPURenderer;
import org.dayflower.renderer.RayCastingCPURenderer;
import org.dayflower.renderer.RayitoPathTracingCPURenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.SmallPTIPathTracingCPURenderer;
import org.dayflower.renderer.SmallPTRPathTracingCPURenderer;
import org.dayflower.scene.loader.JavaSceneLoader;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestAmbientOcclusionCPURenderer();
		doTestPBRTPathTracingCPURenderer();
//		doTestRayCastingCPURenderer();
//		doTestRayitoPathTracingCPURenderer();
//		doTestSmallPTIPathTracingCPURenderer();
//		doTestSmallPTRPathTracingCPURenderer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestAmbientOcclusionCPURenderer() {
		final
		Renderer renderer = new AmbientOcclusionCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(AmbientOcclusionCPURenderer.class, "./resources/scenes/RayitoDefault.java"));
		renderer.render();
	}
	
	static void doTestPBRTPathTracingCPURenderer() {
		final
		Renderer renderer = new PBRTPathTracingCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(PBRTPathTracingCPURenderer.class, "./resources/scenes/PBRTSL500.java"));
		renderer.render();
	}
	
	static void doTestRayCastingCPURenderer() {
		final
		Renderer renderer = new RayCastingCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(RayCastingCPURenderer.class, "./resources/scenes/RayitoDefault.java"));
		renderer.render();
	}
	
	static void doTestRayitoPathTracingCPURenderer() {
		final
		Renderer renderer = new RayitoPathTracingCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(RayitoPathTracingCPURenderer.class, "./resources/scenes/RayitoDefault.java"));
		renderer.render();
	}
	
	static void doTestSmallPTIPathTracingCPURenderer() {
		final
		Renderer renderer = new SmallPTIPathTracingCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(SmallPTIPathTracingCPURenderer.class, "./resources/scenes/RayitoDefault.java"));
		renderer.render();
	}
	
	static void doTestSmallPTRPathTracingCPURenderer() {
		final
		Renderer renderer = new SmallPTRPathTracingCPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(SmallPTRPathTracingCPURenderer.class, "./resources/scenes/RayitoDefault.java"));
		renderer.render();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static RendererConfiguration doCreateRendererConfiguration(final Class<?> clazz, final String pathname) {
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setScene(new JavaSceneLoader().load(pathname));
		rendererConfiguration.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", clazz.getSimpleName(), rendererConfiguration.getScene().getName())));
		rendererConfiguration.setImage(new Image((int)(rendererConfiguration.getScene().getCamera().getResolutionX()), (int)(rendererConfiguration.getScene().getCamera().getResolutionY())));
		
		return rendererConfiguration;
	}
}