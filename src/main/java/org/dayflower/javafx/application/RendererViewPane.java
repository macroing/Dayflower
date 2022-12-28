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
package org.dayflower.javafx.application;

import java.io.File;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.ImageF;
import org.dayflower.javafx.scene.canvas.ConcurrentImageCanvas;
import org.dayflower.javafx.scene.canvas.ConcurrentImageCanvasPane;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.gpu.AbstractGPURenderer;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.texture.Texture;
import org.macroing.java.io.Files;
import org.macroing.java.util.function.TriFunction;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

final class RendererViewPane extends BorderPane {
	private static final File INITIAL_DIRECTORY_IMAGES = new File(".");
	private static final String TITLE_SAVE_AS = "Save As...";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isCameraUpdateRequired;
	private final AtomicLong lastResizeTimeMillis;
	private final AtomicReference<File> file;
	private final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer;
	private final ConcurrentImageCanvas<ImageF> concurrentImageCanvas;
	private final ConcurrentImageCanvasPane<ImageF> concurrentImageCanvasPane;
	private final RendererConfigurationView rendererConfigurationView;
	private final RendererStatusBar rendererStatusBar;
	private final ScenePropertyView scenePropertyView;
	private final Stage stage;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererViewPane(final AtomicReference<Material> material, final AtomicReference<Texture> texture, final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer, final ExecutorService executorService, final Stage stage) {
		this.isCameraUpdateRequired = new AtomicBoolean();
		this.lastResizeTimeMillis = new AtomicLong();
		this.file = new AtomicReference<>();
		this.combinedProgressiveImageOrderRenderer = Objects.requireNonNull(combinedProgressiveImageOrderRenderer, "combinedProgressiveImageOrderRenderer == null");
		this.concurrentImageCanvas = new ConcurrentImageCanvas<>(executorService, combinedProgressiveImageOrderRenderer.getImage(), this::doRender, new ObserverImpl(combinedProgressiveImageOrderRenderer));
		this.concurrentImageCanvasPane = doCreateConcurrentImageCanvasPane();
		this.rendererConfigurationView = new RendererConfigurationView(material, texture, combinedProgressiveImageOrderRenderer);
		this.rendererStatusBar = new RendererStatusBar(combinedProgressiveImageOrderRenderer);
		this.scenePropertyView = new ScenePropertyView(combinedProgressiveImageOrderRenderer.getScene());
		this.stage = Objects.requireNonNull(stage, "stage == null");
		
		setBottom(this.rendererStatusBar);
		setCenter(this.concurrentImageCanvasPane);
		setLeft(this.rendererConfigurationView);
		setRight(this.scenePropertyView);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Optional<File> getFile() {
		return Optional.ofNullable(this.file.get());
	}
	
	public RendererConfigurationView getRendererConfigurationView() {
		return this.rendererConfigurationView;
	}
	
	public RendererStatusBar getRendererStatusBar() {
		return this.rendererStatusBar;
	}
	
	public ScenePropertyView getScenePropertyView() {
		return this.scenePropertyView;
	}
	
	public void render() {
		if(System.currentTimeMillis() - this.lastResizeTimeMillis.get() < 100L) {
			this.combinedProgressiveImageOrderRenderer.renderShutdown();
			this.combinedProgressiveImageOrderRenderer.clear();
			
			return;
		}
		
		this.concurrentImageCanvas.render();
	}
	
	public void save() {
		final Optional<File> optionalFile = getFile();
		
		if(optionalFile.isPresent()) {
			final
			ImageF imageF = this.combinedProgressiveImageOrderRenderer.getImage();
			imageF.save(optionalFile.get());
		} else {
			saveAs();
		}
	}
	
	public void saveAs() {
		final
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(Files.findClosestExistingDirectoryTo(INITIAL_DIRECTORY_IMAGES));
		fileChooser.setTitle(TITLE_SAVE_AS);
		
		final File file = fileChooser.showSaveDialog(this.stage);
		
		if(file != null) {
			setFile(file);
			
			save();
		}
	}
	
	public void setCameraUpdateRequired(final boolean isCameraUpdateRequired) {
		this.isCameraUpdateRequired.set(isCameraUpdateRequired);
	}
	
	public void setFile(final File file) {
		this.file.set(Objects.requireNonNull(file, "file == null"));
	}
	
	public void update() {
		final Scene scene = this.combinedProgressiveImageOrderRenderer.getScene();
		
		final Camera camera = scene.getCamera();
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.A)) {
			camera.moveLeft(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.D)) {
			camera.moveRight(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.E)) {
			camera.moveDown(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.Q)) {
			camera.moveUp(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.S)) {
			camera.moveBackward(0.3F);
		}
		
		if(this.concurrentImageCanvas.isKeyPressed(KeyCode.W)) {
			camera.moveForward(0.3F);
		}
		
		if(this.isCameraUpdateRequired.compareAndSet(true, false)) {
			if(this.combinedProgressiveImageOrderRenderer instanceof AbstractGPURenderer) {
				AbstractGPURenderer.class.cast(this.combinedProgressiveImageOrderRenderer).updateCamera();
			}
			
			this.combinedProgressiveImageOrderRenderer.renderShutdown();
			this.combinedProgressiveImageOrderRenderer.clear();
		}
		
		if(scene.update()) {
			if(this.combinedProgressiveImageOrderRenderer instanceof AbstractGPURenderer) {
				AbstractGPURenderer.class.cast(this.combinedProgressiveImageOrderRenderer).updateMatrix44Fs();
			}
			
			this.combinedProgressiveImageOrderRenderer.clear();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private ConcurrentImageCanvasPane<ImageF> doCreateConcurrentImageCanvasPane() {
		final
		ConcurrentImageCanvasPane<ImageF> concurrentImageCanvasPane = new ConcurrentImageCanvasPane<>(this.concurrentImageCanvas, doCreateImageConstructionFunction());
		concurrentImageCanvasPane.heightProperty().addListener((observable, oldValue, newValue) -> this.lastResizeTimeMillis.set(System.currentTimeMillis()));
		concurrentImageCanvasPane.widthProperty().addListener((observable, oldValue, newValue) -> this.lastResizeTimeMillis.set(System.currentTimeMillis()));
		
		return concurrentImageCanvasPane;
	}
	
	private TriFunction<ImageF, Number, Number, ImageF> doCreateImageConstructionFunction() {
		return (currentImage, resolutionX, resolutionY) -> {
			final
			Camera camera = this.combinedProgressiveImageOrderRenderer.getScene().getCamera();
			camera.setResolution(resolutionX.intValue(), resolutionY.intValue());
			camera.setFieldOfViewY();
			
			final ImageF image = currentImage.scale(resolutionX.intValue(), resolutionY.intValue()).clear();
			
			this.combinedProgressiveImageOrderRenderer.setImage(image);
			this.combinedProgressiveImageOrderRenderer.renderShutdown();
			this.combinedProgressiveImageOrderRenderer.clear();
			
			return image;
		};
	}
	
	@SuppressWarnings("unused")
	private boolean doRender(final ImageF image) {
		return this.combinedProgressiveImageOrderRenderer.render();
	}
}