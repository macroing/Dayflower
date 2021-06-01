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
package org.dayflower.javafx.application;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import org.dayflower.image.ByteImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.loader.JavaSceneLoader;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

final class FileOpenEventHandler implements EventHandler<ActionEvent> {
	private static final File INITIAL_DIRECTORY_SCENES = new File("./resources/scenes");
	private static final String TITLE_OPEN = "Open";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isUsingGPU;
	private final ExecutorService executorService;
	private final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane;
	private final Stage stage;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public FileOpenEventHandler(final AtomicBoolean isUsingGPU, final ExecutorService executorService, final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane, final Stage stage) {
		this.isUsingGPU = Objects.requireNonNull(isUsingGPU, "isUsingGPU == null");
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.nodeSelectionTabPane = Objects.requireNonNull(nodeSelectionTabPane, "nodeSelectionTabPane == null");
		this.stage = Objects.requireNonNull(stage, "stage == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final ActionEvent actionEvent) {
		try {
			final
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(doFindDirectory(INITIAL_DIRECTORY_SCENES));
			fileChooser.setTitle(TITLE_OPEN);
			
			final File file = fileChooser.showOpenDialog(this.stage);
			
			if(file != null) {
				this.executorService.execute(() -> {
					final SceneLoader sceneLoader = new JavaSceneLoader();
					
					final Scene scene = sceneLoader.load(file);
					
					final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = doCreateCombinedProgressiveImageOrderRenderer(scene);
					
					this.nodeSelectionTabPane.addLater(combinedProgressiveImageOrderRenderer, tab -> tab.setOnClosed(new TabOnClosedEventHandler(combinedProgressiveImageOrderRenderer, tab)));
				});
			}
		} catch(final IllegalArgumentException e) {
//			Do nothing for now.
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private CombinedProgressiveImageOrderRenderer doCreateCombinedProgressiveImageOrderRenderer(final Scene scene) {
		final Camera camera = scene.getCamera();
		
		final int resolutionX = (int)(camera.getResolutionX());
		final int resolutionY = (int)(camera.getResolutionY());
		
		final boolean isUsingGPU = this.isUsingGPU.get();
		
		final
		CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = isUsingGPU ? new GPURenderer(new NoOpRendererObserver()) : new CPURenderer(new NoOpRendererObserver());
		combinedProgressiveImageOrderRenderer.setImage(isUsingGPU ? new ByteImageF(resolutionX, resolutionY) : new PixelImageF(resolutionX, resolutionY));
		combinedProgressiveImageOrderRenderer.setRenderPasses(1);
		combinedProgressiveImageOrderRenderer.setRenderPassesPerDisplayUpdate(1);
		combinedProgressiveImageOrderRenderer.setSamples(1);
		combinedProgressiveImageOrderRenderer.setScene(scene);
		combinedProgressiveImageOrderRenderer.setup();
		
		return combinedProgressiveImageOrderRenderer;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static File doFindDirectory(final File directory) {
		File currentDirectory = directory;
		
		while(currentDirectory != null && !currentDirectory.isDirectory()) {
			currentDirectory = currentDirectory.getParentFile();
		}
		
		return currentDirectory != null && currentDirectory.isDirectory() ? currentDirectory : new File(".");
	}
}