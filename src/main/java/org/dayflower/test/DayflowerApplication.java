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

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.dayflower.image.Image;
import org.dayflower.javafx.AbstractCanvasApplication;
import org.dayflower.javafx.HierarchicalMenuBar;
import org.dayflower.renderer.CPURenderer;
import org.dayflower.renderer.NoOpRendererObserver;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.scene.loader.JavaSceneLoader;
import org.dayflower.util.Timer;

/**
 * This {@code DayflowerApplication} class is used to launch Dayflower.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DayflowerApplication extends AbstractCanvasApplication {
	private static final int RESOLUTION_X = 800;
	private static final int RESOLUTION_Y = 800;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isRendering;
	private final AtomicLong maximumRenderTime;
	private final AtomicLong minimumRenderTime;
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DayflowerApplication} instance.
	 */
	public DayflowerApplication() {
		super("Dayflower", RESOLUTION_X, RESOLUTION_Y);
		
		this.isRendering = new AtomicBoolean(true);
		this.maximumRenderTime = new AtomicLong(Long.MIN_VALUE);
		this.minimumRenderTime = new AtomicLong(Long.MAX_VALUE);
		this.labelRenderPass = new Label("Render Pass: 0");
		this.labelRenderTime = new Label("Render Time: 00:00:00");
		this.labelRenderTimePerPass = new Label("Render Time Per Pass: 0");
		this.renderer = new CPURenderer(doCreateRendererConfiguration(), new NoOpRendererObserver(), RenderingAlgorithm.PATH_TRACING_P_B_R_T);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The entry-point for this {@code DayflowerApplication} class.
	 * 
	 * @param args the command line arguments supplied
	 */
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method is called when it's time to render.
	 * <p>
	 * Returns {@code true} if, and only if, something was rendered, {@code false} otherwise.
	 * <p>
	 * This method is not called every frame. It's also not called from the JavaFX Application Thread.
	 * 
	 * @param image the {@code Image} to render to
	 * @return {@code true} if, and only if, something was rendered, {@code false} otherwise
	 */
	@Override
	protected boolean render(final Image image) {
		if(this.isRendering.get()) {
			this.renderer.getRendererConfiguration().setImage(image);
			this.renderer.render();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * This method is called to initialize the GUI.
	 * 
	 * @param borderPane the main {@code BorderPane}
	 */
	@Override
	protected void initializeGUI(final BorderPane borderPane) {
		final
		HierarchicalMenuBar hierarchicalMenuBar = new HierarchicalMenuBar();
		hierarchicalMenuBar.setPathElementText("File", "File");
		hierarchicalMenuBar.addMenuItem("File", "Open", this::doHandleEventFileOpen, null, true);
		hierarchicalMenuBar.addSeparatorMenuItem("File");
		hierarchicalMenuBar.addMenuItem("File", "Save", this::doHandleEventFileSave, null, true);
		hierarchicalMenuBar.addMenuItem("File", "Save As...", this::doHandleEventFileSaveAs, null, true);
		hierarchicalMenuBar.addSeparatorMenuItem("File");
		hierarchicalMenuBar.addMenuItem("File", "Exit", this::doHandleEventFileExit, null, true);
		
		final
		HBox hBox = new HBox();
		hBox.getChildren().add(this.labelRenderPass);
		hBox.getChildren().add(this.labelRenderTime);
		hBox.getChildren().add(this.labelRenderTimePerPass);
		hBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		hBox.setSpacing(20.0D);
		
		borderPane.setBottom(hBox);
		borderPane.setTop(hierarchicalMenuBar);
		
		final int resolutionX = this.renderer.getRendererConfiguration().getImage().getResolutionX();
		final int resolutionY = this.renderer.getRendererConfiguration().getImage().getResolutionY();
		
		setResolution(resolutionX, resolutionY);
	}
	
	/**
	 * This method is called every frame when it's time to update.
	 */
	@Override
	protected void update() {
		final RendererConfiguration rendererConfiguration = this.renderer.getRendererConfiguration();
		
		final Camera camera = rendererConfiguration.getScene().getCamera();
		
		final Label labelRenderPass = this.labelRenderPass;
		final Label labelRenderTime = this.labelRenderTime;
		final Label labelRenderTimePerPass = this.labelRenderTimePerPass;
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final long currentRenderTime = rendererConfiguration.getRenderTime();
		final long maximumRenderTime = currentRenderTime == 0L ? this.maximumRenderTime.get() : Math.max(this.maximumRenderTime.get(), currentRenderTime);
		final long minimumRenderTime = currentRenderTime == 0L ? this.minimumRenderTime.get() : Math.min(this.minimumRenderTime.get(), currentRenderTime);
		
		this.maximumRenderTime.set(maximumRenderTime);
		this.minimumRenderTime.set(minimumRenderTime);
		
		labelRenderPass.setText("Render Pass: " + rendererConfiguration.getRenderPass());
		labelRenderTime.setText("Render Time: " + timer.getTime());
		labelRenderTimePerPass.setText("Render Time Per Pass: " + (minimumRenderTime == Long.MAX_VALUE ? "?" : Long.toString(minimumRenderTime)) + " / " + (currentRenderTime == 0L ? "?" : Long.toString(currentRenderTime)) + " / " + (maximumRenderTime == Long.MIN_VALUE ? "?" : Long.toString(maximumRenderTime)));
		
		synchronized(camera) {
			if(isKeyPressed(KeyCode.A, true)) {
				camera.moveLeft(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.D, true)) {
				camera.moveRight(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.S, true)) {
				camera.moveBackward(1.0F);
				
				this.renderer.clear();
			}
			
			if(isKeyPressed(KeyCode.W, true)) {
				camera.moveForward(1.0F);
				
				this.renderer.clear();
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("unused")
	private void doHandleEventFileExit(final ActionEvent actionEvent) {
		this.renderer.renderShutdown();
		this.renderer.dispose();
		
		exit();
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileOpen(final ActionEvent actionEvent) {
		final
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("."));
		fileChooser.setTitle("Open");
		
		final File file = fileChooser.showOpenDialog(getStage());
		
		if(file != null) {
			final Scene scene = new JavaSceneLoader().load(file);
			
			final Camera camera = scene.getCamera();
			
			final int resolutionX = (int)(camera.getResolutionX());
			final int resolutionY = (int)(camera.getResolutionY());
			
			this.isRendering.set(false);
			
			this.renderer.clear();
			this.renderer.getRendererConfiguration().setImage(new Image(resolutionX, resolutionY));
			this.renderer.getRendererConfiguration().setScene(scene);
			
			setResolution(resolutionX, resolutionY);
			
			this.isRendering.set(true);
		}
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSave(final ActionEvent actionEvent) {
		
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSaveAs(final ActionEvent actionEvent) {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static RendererConfiguration doCreateRendererConfiguration() {
		final Scene scene = new JavaSceneLoader().load("./resources/scenes/PBRTShowcaseMaterial.java");
		
		final Camera camera = scene.getCamera();
		
		final int resolutionX = (int)(camera.getResolutionX());
		final int resolutionY = (int)(camera.getResolutionY());
		
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setImage(new Image(resolutionX, resolutionY));
		rendererConfiguration.setRenderPasses(1);
		rendererConfiguration.setRenderPassesPerDisplayUpdate(1);
		rendererConfiguration.setSamples(1);
		rendererConfiguration.setScene(scene);
		
		return rendererConfiguration;
	}
}