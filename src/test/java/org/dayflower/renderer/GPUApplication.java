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

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.dayflower.image.ByteImageF;
import org.dayflower.image.Image;
import org.dayflower.image.ImageF;
import org.dayflower.javafx.canvas.ConcurrentImageCanvas;
import org.dayflower.javafx.canvas.ConcurrentImageCanvas.Observer;
import org.dayflower.renderer.gpu.AbstractGPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.scene.Camera;
import org.dayflower.scene.loader.JavaSceneLoader;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public final class GPUApplication extends Application {
	private final ExecutorService executorService;
	private final Label label;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public GPUApplication() {
		this.executorService = Executors.newFixedThreadPool(1);
		this.label = new Label();
		this.renderer = doCreateRenderer(RenderingAlgorithm.PATH_TRACING, "./resources/scenes/GPURenderer.java");
		this.renderer.setRendererObserver(new RendererObserverImpl(this.label));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void start(final Stage stage) {
		final Renderer renderer = this.renderer;
		
		final ByteImageF byteImage = ByteImageF.class.cast(renderer.getImage());
		
		final Camera camera = renderer.getScene().getCamera();
		
		final ExecutorService executorService = this.executorService;
		
		final ConcurrentImageCanvas<ImageF> concurrentImageCanvas = new ConcurrentImageCanvas<>(this.executorService, byteImage, this::doRender, new ObserverImpl(AbstractGPURenderer.class.cast(renderer), camera));
		
		final
		HBox hBox = new HBox();
		hBox.getChildren().add(this.label);
		hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		
		final
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(hBox);
		borderPane.setCenter(concurrentImageCanvas);
		
		final Scene scene = new Scene(borderPane);
		
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setScene(scene);
		stage.setTitle("GPU");
		stage.sizeToScene();
		stage.show();
		
		new AnimationTimer() {
			@Override
			public void handle(final long now) {
				if(concurrentImageCanvas.isKeyPressed(KeyCode.ESCAPE, true)) {
					renderer.renderShutdown();
					renderer.dispose();
					
					try {
						executorService.shutdown();
						executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);
					} catch(final InterruptedException e) {
						
					}
					
					Platform.exit();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.A)) {
					camera.moveLeft(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.D)) {
					camera.moveRight(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.E)) {
					camera.moveDown(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.Q)) {
					camera.moveUp(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.S)) {
					camera.moveBackward(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentImageCanvas.isKeyPressed(KeyCode.W)) {
					camera.moveForward(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				concurrentImageCanvas.render();
			}
		}.start();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private boolean doRender(final ImageF image) {
		return this.renderer.render();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Renderer doCreateRenderer(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final org.dayflower.scene.Scene scene = new JavaSceneLoader().load(pathname);
		
		final
		CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = new GPURenderer();
		combinedProgressiveImageOrderRenderer.setImage(new ByteImageF((int)(scene.getCamera().getResolutionX()), (int)(scene.getCamera().getResolutionY())));
		combinedProgressiveImageOrderRenderer.setRenderPasses(1);
		combinedProgressiveImageOrderRenderer.setRendererObserver(new NoOpRendererObserver());
		combinedProgressiveImageOrderRenderer.setRenderingAlgorithm(renderingAlgorithm);
		combinedProgressiveImageOrderRenderer.setScene(scene);
		combinedProgressiveImageOrderRenderer.setup();
		
		return combinedProgressiveImageOrderRenderer;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ObserverImpl implements Observer {
		private final AbstractGPURenderer abstractGPURenderer;
		private final Camera camera;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ObserverImpl(final AbstractGPURenderer abstractGPURenderer, final Camera camera) {
			this.abstractGPURenderer = Objects.requireNonNull(abstractGPURenderer, "abstractGPURenderer == null");
			this.camera = Objects.requireNonNull(camera, "camera == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onMouseDragged(final ConcurrentImageCanvas<? extends Image> concurrentImageCanvas, final float x, final float y) {
			this.camera.rotate(x, y);
			this.abstractGPURenderer.updateCamera();
			this.abstractGPURenderer.clear();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererObserverImpl implements RendererObserver {
		private final Label label;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererObserverImpl(final Label label) {
			this.label = Objects.requireNonNull(label, "label == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onRenderDisplay(final Renderer renderer, final ImageF image) {
			
		}
		
		@Override
		public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
			if(renderer instanceof ProgressiveImageOrderRenderer) {
				final ProgressiveImageOrderRenderer progressiveImageOrderRenderer = ProgressiveImageOrderRenderer.class.cast(renderer);
				
				Platform.runLater(() -> this.label.setText("Millis: " + Long.toString(elapsedTimeMillis) + " Render Pass: " + progressiveImageOrderRenderer.getRenderPass()));
			}
		}
		
		@Override
		public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
			
		}
	}
}