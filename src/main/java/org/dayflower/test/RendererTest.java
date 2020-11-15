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
import org.dayflower.sampler.NRooksSampler;
import org.dayflower.sampler.RandomSampler;
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
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/RayitoDefault.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new RandomSampler());
		renderer.render();
	}
	
	static void doTestPBRTPathTracingCPURenderer() {
		final
		Renderer renderer = new PBRTPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/PBRTSL500.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new RandomSampler());
		renderer.render();
	}
	
	static void doTestRayCastingCPURenderer() {
		final
		Renderer renderer = new RayCastingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/RayitoDefault.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new RandomSampler());
		renderer.render();
	}
	
	static void doTestRayitoPathTracingCPURenderer() {
		final
		Renderer renderer = new RayitoPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/RayitoCornellBox.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new NRooksSampler());
		renderer.render();
	}
	
	static void doTestSmallPTIPathTracingCPURenderer() {
		final
		Renderer renderer = new SmallPTIPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/RayitoDefault.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new RandomSampler());
		renderer.render();
	}
	
	static void doTestSmallPTRPathTracingCPURenderer() {
		final
		Renderer renderer = new SmallPTRPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(new JavaSceneLoader().load("./resources/scenes/RayitoDefault.java"));
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.setSampler(new RandomSampler());
		renderer.render();
	}
}