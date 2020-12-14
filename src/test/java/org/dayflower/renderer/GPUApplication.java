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
package org.dayflower.renderer;

import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dayflower.geometry.AngleF;
import org.dayflower.image.ByteImage;
import org.dayflower.image.Image;
import org.dayflower.javafx.canvas.ConcurrentByteArrayCanvas;
import org.dayflower.javafx.canvas.ConcurrentByteArrayCanvas.Observer;
import org.dayflower.renderer.gpu.AbstractGPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
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
		this.renderer = doCreateRenderer(RenderingAlgorithm.PATH_TRACING, "./resources/scenes/GPUTest.java");
		this.renderer.setRendererObserver(new RendererObserverImpl(this.label));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void start(final Stage stage) {
		final Renderer renderer = this.renderer;
		
		final ByteImage byteImage = ByteImage.class.cast(renderer.getRendererConfiguration().getImage());
		
		final Camera camera = renderer.getRendererConfiguration().getScene().getCamera();
		
		final ExecutorService executorService = this.executorService;
		
		final ConcurrentByteArrayCanvas concurrentByteArrayCanvas = new ConcurrentByteArrayCanvas(this.executorService, new ObserverImpl(AbstractGPURenderer.class.cast(renderer), camera), this::doRender, byteImage.getResolutionX(), byteImage.getResolutionY());
		
		final
		HBox hBox = new HBox();
		hBox.getChildren().add(this.label);
		hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		
		final
		BorderPane borderPane = new BorderPane();
		borderPane.setBottom(hBox);
		borderPane.setCenter(concurrentByteArrayCanvas);
		
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
				if(concurrentByteArrayCanvas.isKeyPressed(KeyCode.ESCAPE, true)) {
					renderer.renderShutdown();
					renderer.dispose();
					
					executorService.shutdown();
					
					Platform.exit();
				}
				
				if(concurrentByteArrayCanvas.isKeyPressed(KeyCode.A)) {
					camera.moveLeft(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentByteArrayCanvas.isKeyPressed(KeyCode.D)) {
					camera.moveRight(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentByteArrayCanvas.isKeyPressed(KeyCode.S)) {
					camera.moveBackward(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				if(concurrentByteArrayCanvas.isKeyPressed(KeyCode.W)) {
					camera.moveForward(0.3F);
					
					final
					AbstractGPURenderer abstractGPURenderer = AbstractGPURenderer.class.cast(renderer);
					abstractGPURenderer.updateCamera();
					abstractGPURenderer.clear();
				}
				
				concurrentByteArrayCanvas.render();
			}
		}.start();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doRender(final byte[] updateBytes) {
		if(this.renderer.render()) {
			final ByteImage byteImage = ByteImage.class.cast(this.renderer.getRendererConfiguration().getImage());
			
			final byte[] renderBytes = byteImage.getBytes(true);
			
			System.arraycopy(renderBytes, 0, updateBytes, 0, renderBytes.length);
			
			return true;
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Renderer doCreateRenderer(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final
		Renderer renderer = new GPURenderer();
		renderer.setRendererConfiguration(doCreateRendererConfiguration(renderingAlgorithm, pathname));
		renderer.setRendererObserver(new NoOpRendererObserver());
		renderer.setup();
		
		return renderer;
	}
	
	private static RendererConfiguration doCreateRendererConfiguration(final RenderingAlgorithm renderingAlgorithm, final String pathname) {
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setScene(new JavaSceneLoader().load(pathname));
		rendererConfiguration.setImage(new ByteImage((int)(rendererConfiguration.getScene().getCamera().getResolutionX()), (int)(rendererConfiguration.getScene().getCamera().getResolutionY())));
		rendererConfiguration.setRenderPasses(1);
		rendererConfiguration.setRenderingAlgorithm(renderingAlgorithm);
		rendererConfiguration.setSampler(new RandomSampler());
		
		return rendererConfiguration;
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
		public void onMouseDragged(final float x, final float y) {
			this.camera.setYaw(AngleF.add(this.camera.getYaw(), AngleF.degrees(-x * 0.5F)));
			this.camera.setPitch(AngleF.degrees(max(min(this.camera.getPitch().getDegrees() + AngleF.degrees((y * 0.5F), -90.0F, 90.0F).getDegrees(), 89.99F), -89.99F), -89.99F, 89.99F));
			this.camera.setOrthonormalBasis();
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
		public void onRenderDisplay(final Renderer renderer, final Image image) {
			
		}
		
		@Override
		public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
			Platform.runLater(() -> this.label.setText("Millis: " + Long.toString(elapsedTimeMillis)));
		}
		
		@Override
		public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
			
		}
	}
}