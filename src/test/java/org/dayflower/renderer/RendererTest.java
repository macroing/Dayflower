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

import org.dayflower.image.ByteImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.loader.JavaSceneLoader;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestCPURendererAmbientOcclusion();
		doTestCPURendererPathTracing();
//		doTestCPURendererRayCasting();
//		doTestGPURenderer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestCPURendererAmbientOcclusion() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.AMBIENT_OCCLUSION, "./resources/scenes/RayitoDefault.java");
		renderer.render();
	}
	
	static void doTestCPURendererPathTracing() {
		final
		Renderer renderer = doCreateCPURenderer(RenderingAlgorithm.PATH_TRACING, "./resources/scenes/PBRTShowcaseMaterial.java");
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
		cPURenderer.setImage(new PixelImageF((int)(cPURenderer.getScene().getCamera().getResolutionX()), (int)(cPURenderer.getScene().getCamera().getResolutionY())));
		cPURenderer.setRenderingAlgorithm(renderingAlgorithm);
		cPURenderer.setRendererObserver(doCreateRendererObserver(renderingAlgorithm.getName(), cPURenderer.getScene().getName()));
		
		return cPURenderer;
	}
	
	private static GPURenderer doCreateGPURenderer(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final
		GPURenderer gPURenderer = new GPURenderer();
		gPURenderer.setScene(new JavaSceneLoader().load(pathname));
		gPURenderer.setImage(new ByteImageF((int)(gPURenderer.getScene().getCamera().getResolutionX()), (int)(gPURenderer.getScene().getCamera().getResolutionY())));
		gPURenderer.setRenderPasses(10);
		gPURenderer.setRenderingAlgorithm(renderingAlgorithm);
		gPURenderer.setRendererObserver(doCreateRendererObserver(renderingAlgorithm.getName(), gPURenderer.getScene().getName()));
		
		return gPURenderer;
	}
	
	private static RendererObserver doCreateRendererObserver(final String renderingAlgorithmName, final String sceneName) {
		return new FileRendererObserver(String.format("./generated/%s-%s.png", renderingAlgorithmName, sceneName), true, false);
	}
}