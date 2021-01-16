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
package org.dayflower.renderer;

import org.dayflower.image.ByteImage;
import org.dayflower.image.PixelImage;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.loader.JavaSceneLoader;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestCPURendererAmbientOcclusion();
		doTestCPURendererPathTracingPBRT();
//		doTestCPURendererPathTracingRayito();
//		doTestCPURendererPathTracingSmallPTIterative();
//		doTestCPURendererPathTracingSmallPTRecursive();
//		doTestCPURendererRayCasting();
//		doTestGPURenderer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestCPURendererAmbientOcclusion() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.AMBIENT_OCCLUSION, "./resources/scenes/RayitoDefault.java");
		renderer.render();
	}
	
	static void doTestCPURendererPathTracingPBRT() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.PATH_TRACING_P_B_R_T, "./resources/scenes/PBRTShowcaseMaterial.java");
		renderer.render();
	}
	
	static void doTestCPURendererPathTracingRayito() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.PATH_TRACING_RAYITO, "./resources/scenes/RayitoShowcaseShape3FConstructiveSolidGeometry3F.java");
		renderer.render();
	}
	
	static void doTestCPURendererPathTracingSmallPTIterative() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE, "./resources/scenes/RayitoDefault.java");
		renderer.render();
	}
	
	static void doTestCPURendererPathTracingSmallPTRecursive() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_RECURSIVE, "./resources/scenes/RayitoDefault.java");
		renderer.render();
	}
	
	static void doTestCPURendererRayCasting() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.RAY_CASTING, "./resources/scenes/RayitoDefault.java");
		renderer.render();
	}
	
	static void doTestGPURenderer() {
		final
		Renderer renderer = doCreateGPURenderer(RenderingAlgorithm.PATH_TRACING, "./resources/scenes/GPUTest.java");
		renderer.setup();
		renderer.render();
		renderer.dispose();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static CPURenderer doCreateCPURenderer(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final
		CPURenderer cPURenderer = new CPURenderer();
		cPURenderer.setScene(new JavaSceneLoader().load(pathname));
		cPURenderer.setImage(new PixelImage((int)(cPURenderer.getScene().getCamera().getResolutionX()), (int)(cPURenderer.getScene().getCamera().getResolutionY())));
		cPURenderer.setRenderingAlgorithm(renderingAlgorithm);
		cPURenderer.setSampler(new RandomSampler());
		cPURenderer.setRendererObserver(doCreateRendererObserver(renderingAlgorithm.getName(), cPURenderer.getScene().getName()));
		
		return cPURenderer;
	}
	
	private static GPURenderer doCreateGPURenderer(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final
		GPURenderer gPURenderer = new GPURenderer();
		gPURenderer.setScene(new JavaSceneLoader().load(pathname));
		gPURenderer.setImage(new ByteImage((int)(gPURenderer.getScene().getCamera().getResolutionX()), (int)(gPURenderer.getScene().getCamera().getResolutionY())));
		gPURenderer.setRenderPasses(10);
		gPURenderer.setRenderingAlgorithm(renderingAlgorithm);
		gPURenderer.setSampler(new RandomSampler());
		gPURenderer.setRendererObserver(doCreateRendererObserver(renderingAlgorithm.getName(), gPURenderer.getScene().getName()));
		
		return gPURenderer;
	}
	
	private static RendererObserver doCreateRendererObserver(final String renderingAlgorithmName, final String sceneName) {
		return new FileRendererObserver(String.format("./generated/%s-%s.png", renderingAlgorithmName, sceneName), true, false);
	}
}