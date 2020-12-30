/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.ByteImage;
import org.dayflower.image.PixelImage;
import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.javafx.scene.control.PathMenuBar;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.loader.JavaSceneLoader;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This {@code DayflowerApplication} class is the main entry point for the Dayflower GUI.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DayflowerApplication extends Application {
	private static final File INITIAL_DIRECTORY_IMAGES = new File(".");
	private static final File INITIAL_DIRECTORY_SCENES = new File("./resources/scenes");
	private static final String PATH_ELEMENT_FILE = "File";
	private static final String PATH_FILE = "File";
	private static final String TEXT_EXIT = "Exit";
	private static final String TEXT_FILE = "File";
	private static final String TEXT_NEW = "New";
	private static final String TEXT_OPEN = "Open";
	private static final String TEXT_SAVE = "Save";
	private static final String TEXT_SAVE_AS = "Save As...";
	private static final String TITLE = "Dayflower";
	private static final String TITLE_OPEN = "Open";
	private static final String TITLE_SAVE_AS = "Save As...";
	private static final double MINIMUM_RESOLUTION_X = 400.0D;
	private static final double MINIMUM_RESOLUTION_Y = 400.0D;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicReference<Stage> stage;
	private final BorderPane borderPane;
	private final ExecutorService executorService;
	private final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane;
	private final PathMenuBar pathMenuBar;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DayflowerApplication} instance.
	 */
	public DayflowerApplication() {
		this.stage = new AtomicReference<>();
		this.borderPane = new BorderPane();
		this.executorService = Executors.newFixedThreadPool(4);
		this.nodeSelectionTabPane = new NodeSelectionTabPane<>(RendererTabPane.class, rendererPane -> rendererPane.getCombinedProgressiveImageOrderRenderer(), renderer -> new RendererTabPane(renderer, this.executorService), (a, b) -> a.equals(b), renderer -> renderer.getScene().getName());
		this.pathMenuBar = new PathMenuBar();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts this {@code DayflowerApplication} instance.
	 * <p>
	 * If {@code stage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param stage the main {@code Stage}
	 * @throws NullPointerException thrown if, and only if, {@code stage} is {@code null}
	 */
	@Override
	public void start(final Stage stage) {
		doSetStage(stage);
		doConfigureBorderPane();
		doConfigureNodeSelectionTabPane();
		doConfigurePathMenuBar();
		doConfigureAndShowStage();
		doCreateAndStartAnimationTimer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Starts this {@code DayflowerApplication} instance.
	 * 
	 * @param args the parameter arguments supplied
	 */
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Stage doGetStage() {
		return this.stage.get();
	}
	
	private void doAddRenderer(final File file) {
		this.executorService.execute(() -> {
			final SceneLoader sceneLoader = new JavaSceneLoader();
			
			final Scene scene = sceneLoader.load(file);
			
			doAddRenderer(scene);
		});
	}
	
	private void doAddRenderer(final Scene scene) {
		this.executorService.execute(() -> {
			final Camera camera = scene.getCamera();
			
			final int resolutionX = (int)(camera.getResolutionX());
			final int resolutionY = (int)(camera.getResolutionY());
			
			final
			CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = new CPURenderer(new NoOpRendererObserver());
//			CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = new GPURenderer(new NoOpRendererObserver());
			combinedProgressiveImageOrderRenderer.setImage(new PixelImage(resolutionX, resolutionY));
//			combinedProgressiveImageOrderRenderer.setImage(new ByteImage(resolutionX, resolutionY));
			combinedProgressiveImageOrderRenderer.setRenderPasses(1);
			combinedProgressiveImageOrderRenderer.setRenderPassesPerDisplayUpdate(1);
			combinedProgressiveImageOrderRenderer.setSamples(1);
			combinedProgressiveImageOrderRenderer.setSampler(new RandomSampler());
			combinedProgressiveImageOrderRenderer.setScene(scene);
			combinedProgressiveImageOrderRenderer.setup();
			
			Platform.runLater(() -> {
				final
				Tab tab = this.nodeSelectionTabPane.add(combinedProgressiveImageOrderRenderer);
				tab.setOnClosed(event -> {
					final Object source = event.getSource();
					
					if(source == tab) {
						combinedProgressiveImageOrderRenderer.renderShutdown();
						combinedProgressiveImageOrderRenderer.dispose();
					}
				});
			});
		});
	}
	
	private void doConfigureAndShowStage() {
		final
		Stage stage = doGetStage();
		stage.setMinHeight(MINIMUM_RESOLUTION_Y);
		stage.setMinWidth(MINIMUM_RESOLUTION_X);
		stage.setResizable(false);
		stage.setScene(new javafx.scene.Scene(this.borderPane));
		stage.setTitle(TITLE);
		stage.show();
	}
	
	private void doConfigureBorderPane() {
		this.borderPane.setCenter(this.nodeSelectionTabPane);
		this.borderPane.setTop(this.pathMenuBar);
	}
	
	private void doConfigureNodeSelectionTabPane() {
		this.nodeSelectionTabPane.getSelectionModel().selectedItemProperty().addListener(this::doHandleTabChangeSelectionTabPane);
	}
	
	private void doConfigurePathMenuBar() {
		this.pathMenuBar.setPathElementText(PATH_ELEMENT_FILE, TEXT_FILE);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_NEW, this::doHandleEventFileNew, new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), true);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_OPEN, this::doHandleEventFileOpen, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), true);
		this.pathMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE, this::doHandleEventFileSave, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), false);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE_AS, this::doHandleEventFileSaveAs, null, false);
		this.pathMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_EXIT, this::doHandleEventFileExit, null, true);
	}
	
	private void doCreateAndStartAnimationTimer() {
		final
		AnimationTimer animationTimer = new AnimationTimerImpl(this.nodeSelectionTabPane);
		animationTimer.start();
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileExit(final ActionEvent actionEvent) {
		for(final Tab tab : this.nodeSelectionTabPane.getTabs()) {
			final Node content = tab.getContent();
			
			if(content instanceof RendererTabPane) {
				final RendererTabPane rendererTabPane = RendererTabPane.class.cast(content);
				
				final
				CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = rendererTabPane.getCombinedProgressiveImageOrderRenderer();
				combinedProgressiveImageOrderRenderer.renderShutdown();
				combinedProgressiveImageOrderRenderer.dispose();
			}
		}
		
		try {
			this.executorService.shutdown();
			this.executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);
		} catch(final InterruptedException e) {
			
		}
		
		Platform.exit();
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileOpen(final ActionEvent actionEvent) {
		final
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(INITIAL_DIRECTORY_SCENES);
		fileChooser.setTitle(TITLE_OPEN);
		
		final File file = fileChooser.showOpenDialog(doGetStage());
		
		if(file != null) {
			doAddRenderer(file);
		}
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileNew(final ActionEvent actionEvent) {
		final
		Scene scene = new Scene();
		scene.addLight(new PerezLight());
		
		doAddRenderer(scene);
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSave(final ActionEvent actionEvent) {
		final Optional<RendererTabPane> optionalRendererTabPane = this.nodeSelectionTabPane.getSelectedNode();
		
		if(optionalRendererTabPane.isPresent()) {
			final RendererTabPane rendererTabPane = optionalRendererTabPane.get();
			
			final RendererViewPane rendererViewPane = rendererTabPane.getRendererViewPane();
			
			final Optional<File> optionalFile = rendererViewPane.getFile();
			
			if(optionalFile.isPresent()) {
				rendererViewPane.save();
			} else {
				final
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialDirectory(INITIAL_DIRECTORY_IMAGES);
				fileChooser.setTitle(TITLE_SAVE_AS);
				
				final File file = fileChooser.showSaveDialog(doGetStage());
				
				if(file != null) {
					rendererViewPane.setFile(file);
					rendererViewPane.save();
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSaveAs(final ActionEvent actionEvent) {
		final Optional<RendererTabPane> optionalRendererTabPane = this.nodeSelectionTabPane.getSelectedNode();
		
		if(optionalRendererTabPane.isPresent()) {
			final RendererTabPane rendererTabPane = optionalRendererTabPane.get();
			
			final RendererViewPane rendererViewPane = rendererTabPane.getRendererViewPane();
			
			final
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(INITIAL_DIRECTORY_IMAGES);
			fileChooser.setTitle(TITLE_SAVE_AS);
			
			final File file = fileChooser.showSaveDialog(doGetStage());
			
			if(file != null) {
				rendererViewPane.setFile(file);
				rendererViewPane.save();
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void doHandleTabChangeSelectionTabPane(final ObservableValue<? extends Tab> observableValue, final Tab oldTab, final Tab newTab) {
		if(newTab != null) {
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE).setDisable(false);
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_AS).setDisable(false);
		} else {
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE).setDisable(true);
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_AS).setDisable(true);
		}
		
		Platform.runLater(() -> {
			final Rectangle2D rectangle = Screen.getPrimary().getVisualBounds();
			
			final
			Stage stage = doGetStage();
			stage.sizeToScene();
			stage.setX((rectangle.getWidth()  - stage.getWidth())  / 2.0D);
			stage.setY((rectangle.getHeight() - stage.getHeight()) / 2.0D);
		});
	}
	
	private void doSetStage(final Stage stage) {
		this.stage.set(Objects.requireNonNull(stage, "stage == null"));
	}
}