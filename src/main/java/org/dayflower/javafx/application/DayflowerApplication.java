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
package org.dayflower.javafx.application;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;
import org.dayflower.javafx.scene.control.HierarchicalMenuBar;
import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.cpu.CPURenderer;
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
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

//TODO: Add Javadocs!
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
	private final HierarchicalMenuBar hierarchicalMenuBar;
	private final NodeSelectionTabPane<RendererTabPane, Renderer> nodeSelectionTabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public DayflowerApplication() {
		this.stage = new AtomicReference<>();
		this.borderPane = new BorderPane();
		this.executorService = Executors.newFixedThreadPool(1);
		this.hierarchicalMenuBar = new HierarchicalMenuBar();
		this.nodeSelectionTabPane = new NodeSelectionTabPane<>(RendererTabPane.class, rendererPane -> rendererPane.getRenderer(), renderer -> new RendererTabPane(renderer, this.executorService), (a, b) -> a.equals(b), renderer -> renderer.getRendererConfiguration().getScene().getName());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void start(final Stage stage) {
		doSetStage(stage);
		doConfigureBorderPane();
		doConfigureHierarchicalMenuBar();
		doConfigureNodeSelectionTabPane();
		doConfigureAndShowStage();
		doCreateAndStartAnimationTimer();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
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
			RendererConfiguration rendererConfiguration = new RendererConfiguration();
			rendererConfiguration.setImage(new Image(resolutionX, resolutionY));
			rendererConfiguration.setRenderPasses(1);
			rendererConfiguration.setRenderPassesPerDisplayUpdate(1);
			rendererConfiguration.setSamples(1);
			rendererConfiguration.setSampler(new RandomSampler());
			rendererConfiguration.setScene(scene);
			
			final Renderer renderer = new CPURenderer(rendererConfiguration, new NoOpRendererObserver());
			
			Platform.runLater(() -> this.nodeSelectionTabPane.add(renderer));
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
		this.borderPane.setTop(this.hierarchicalMenuBar);
	}
	
	private void doConfigureHierarchicalMenuBar() {
		this.hierarchicalMenuBar.setPathElementText(PATH_ELEMENT_FILE, TEXT_FILE);
		this.hierarchicalMenuBar.addMenuItem(PATH_FILE, TEXT_NEW, this::doHandleEventFileNew, new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN), true);
		this.hierarchicalMenuBar.addMenuItem(PATH_FILE, TEXT_OPEN, this::doHandleEventFileOpen, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), true);
		this.hierarchicalMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.hierarchicalMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE, this::doHandleEventFileSave, new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), false);
		this.hierarchicalMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE_AS, this::doHandleEventFileSaveAs, null, false);
		this.hierarchicalMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.hierarchicalMenuBar.addMenuItem(PATH_FILE, TEXT_EXIT, this::doHandleEventFileExit, null, true);
	}
	
	private void doConfigureNodeSelectionTabPane() {
		this.nodeSelectionTabPane.getSelectionModel().selectedItemProperty().addListener(this::doHandleTabChangeSelectionTabPane);
	}
	
	private void doCreateAndStartAnimationTimer() {
		final
		AnimationTimer animationTimer = new RendererAnimationTimer(this.nodeSelectionTabPane);
		animationTimer.start();
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileExit(final ActionEvent actionEvent) {
		this.executorService.shutdown();
		
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
			this.hierarchicalMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE).setDisable(false);
			this.hierarchicalMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_AS).setDisable(false);
		} else {
			this.hierarchicalMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE).setDisable(true);
			this.hierarchicalMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_AS).setDisable(true);
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererAnimationTimer extends AnimationTimer {
		private final NodeSelectionTabPane<RendererTabPane, Renderer> selectionTabPane;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererAnimationTimer(final NodeSelectionTabPane<RendererTabPane, Renderer> selectionTabPane) {
			this.selectionTabPane = Objects.requireNonNull(selectionTabPane, "selectionTabPane == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void handle(final long now) {
			final Optional<RendererTabPane> optionalRendererTabPane = this.selectionTabPane.getSelectedNode();
			
			if(optionalRendererTabPane.isPresent()) {
				final
				RendererViewPane rendererViewPane = optionalRendererTabPane.get().getRendererViewPane();
				rendererViewPane.render();
			}
		}
	}
}