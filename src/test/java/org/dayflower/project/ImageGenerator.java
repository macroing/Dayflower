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
package org.dayflower.project;

import java.io.File;
import java.util.List;

import org.dayflower.java.io.Files;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.loader.JavaSceneLoader;

public final class ImageGenerator {
	private ImageGenerator() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		final List<File> files = Files.findFilesFromDirectory(new File("./resources/scenes"));
		
		for(final File file : files) {
			doRender(file);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doRender(final File file) {
		final SceneLoader sceneLoader = new JavaSceneLoader();
		
		final Scene scene = sceneLoader.load(file);
		
		System.out.println("Rendering " + scene.getName() + "...");
		
		final String pathname = String.format("./generated/scenes/%s-2.png", scene.getName());
		
		final RendererObserver rendererObserver = new FileRendererObserver(pathname, false, false, true, 500);
		
		final
		GPURenderer gPURenderer = new GPURenderer(rendererObserver);
		gPURenderer.setScene(scene);
		gPURenderer.setImage();
		gPURenderer.setMaximumBounce(20);
		gPURenderer.setMinimumBounceRussianRoulette(5);
		gPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
		gPURenderer.setup();
		gPURenderer.render(2000);
		gPURenderer.dispose();
		
		System.out.println("Done.");
	}
}