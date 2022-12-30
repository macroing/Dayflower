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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.parameter.ParameterList;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.loader.JavaSceneLoader;

import org.macroing.java.io.Files;
import org.macroing.javafx.scene.control.NodeSelectionTabPane;
import org.macroing.javafx.scene.control.PathMenuBar;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
 * {@code DayflowerApplication} is the main {@code Application} implementation for Dayflower.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DayflowerApplication extends Application {
	private static final File INITIAL_DIRECTORY_SCENES = new File("./resources/scenes");
	private static final String PATH_ELEMENT_C_P_U_RENDERER = "CPU Renderer";
	private static final String PATH_ELEMENT_FILE = "File";
	private static final String PATH_ELEMENT_G_P_U_RENDERER = "GPU Renderer";
	private static final String PATH_ELEMENT_SCENE = "Scene";
	private static final String PATH_FILE = "File";
	private static final String PATH_FILE_NEW = "File.New";
	private static final String PATH_FILE_OPEN = "File.Open";
	private static final String TEXT_C_P_U_RENDERER = "CPU Renderer";
	private static final String TEXT_EXIT = "Exit";
	private static final String TEXT_FILE = "File";
	private static final String TEXT_G_P_U_RENDERER = "GPU Renderer";
	private static final String TEXT_SAVE_IMAGE = "Save Image";
	private static final String TEXT_SAVE_IMAGE_AS = "Save Image As...";
	private static final String TEXT_SCENE = "Scene";
	private static final String TITLE = "Dayflower";
	private static final String TITLE_OPEN = "Open";
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
		this.executorService = Executors.newFixedThreadPool(1);
		this.nodeSelectionTabPane = new NodeSelectionTabPane<>(RendererTabPane.class, rendererTabPane -> rendererTabPane.getCombinedProgressiveImageOrderRenderer(), renderer -> new RendererTabPane(renderer, this.executorService, doGetStage()), (a, b) -> a.equals(b), renderer -> renderer.getScene().getName());
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
	
	private void doConfigureAndShowStage() {
		final
		Stage stage = doGetStage();
		stage.setMinHeight(MINIMUM_RESOLUTION_Y);
		stage.setMinWidth(MINIMUM_RESOLUTION_X);
		stage.setOnCloseRequest(e -> doExit());
		stage.setResizable(true);
		stage.setScene(new javafx.scene.Scene(this.borderPane));
		stage.setTitle(TITLE);
		stage.show();
	}
	
	private void doConfigureBorderPane() {
		this.borderPane.setCenter(this.nodeSelectionTabPane);
		this.borderPane.setTop(this.pathMenuBar);
	}
	
	private void doConfigureNodeSelectionTabPane() {
		this.nodeSelectionTabPane.getSelectionModel().selectedItemProperty().addListener(this::doHandleTabChangeNodeSelectionTabPane);
	}
	
	private void doConfigurePathMenuBar() {
		this.pathMenuBar.setPathElementText(PATH_ELEMENT_C_P_U_RENDERER, TEXT_C_P_U_RENDERER);
		this.pathMenuBar.setPathElementText(PATH_ELEMENT_FILE, TEXT_FILE);
		this.pathMenuBar.setPathElementText(PATH_ELEMENT_G_P_U_RENDERER, TEXT_G_P_U_RENDERER);
		this.pathMenuBar.setPathElementText(PATH_ELEMENT_SCENE, TEXT_SCENE);
		this.pathMenuBar.addMenuItem(PATH_FILE_NEW, TEXT_C_P_U_RENDERER, e -> doNew(false), null, true);
		this.pathMenuBar.addMenuItem(PATH_FILE_NEW, TEXT_G_P_U_RENDERER, e -> doNew(true), null, true);
		this.pathMenuBar.addMenuItem(PATH_FILE_OPEN, TEXT_C_P_U_RENDERER, e -> doOpen(false), null, true);
		this.pathMenuBar.addMenuItem(PATH_FILE_OPEN, TEXT_G_P_U_RENDERER, e -> doOpen(true), null, true);
		this.pathMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE_IMAGE, e -> doSave(), new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), false);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_SAVE_IMAGE_AS, e -> doSaveAs(), null, false);
		this.pathMenuBar.addSeparatorMenuItem(PATH_FILE);
		this.pathMenuBar.addMenuItem(PATH_FILE, TEXT_EXIT, e -> doExit(), null, true);
	}
	
	private void doCreateAndStartAnimationTimer() {
		final
		AnimationTimer animationTimer = new AnimationTimerImpl(this.nodeSelectionTabPane);
		animationTimer.start();
	}
	
	@SuppressWarnings("unused")
	private void doExit() {
		for(final Tab tab : this.nodeSelectionTabPane.getTabs()) {
			final Node content = tab.getContent();
			
			if(content instanceof RendererTabPane) {
				final
				RendererTabPane rendererTabPane = RendererTabPane.class.cast(content);
				rendererTabPane.handleExitRequest();
			}
		}
		
		try {
			this.executorService.shutdown();
			this.executorService.awaitTermination(10000L, TimeUnit.MILLISECONDS);
		} catch(final InterruptedException e) {
//			Do nothing for now.
		}
		
		Platform.exit();
	}
	
	@SuppressWarnings("unused")
	private void doHandleTabChangeNodeSelectionTabPane(final ObservableValue<? extends Tab> observableValue, final Tab oldTab, final Tab newTab) {
		if(newTab != null) {
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_IMAGE).setDisable(false);
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_IMAGE_AS).setDisable(false);
		} else {
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_IMAGE).setDisable(true);
			this.pathMenuBar.getMenuItem(PATH_FILE, TEXT_SAVE_IMAGE_AS).setDisable(true);
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
	
	private void doNew(final boolean isUsingGPU) {
		this.executorService.execute(() -> {
			final
			Scene scene = new Scene();
			scene.addLight(new PerezLight());
			
			final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = Renderers.createCombinedProgressiveImageOrderRenderer(scene, isUsingGPU);
			
			this.nodeSelectionTabPane.addLater(combinedProgressiveImageOrderRenderer, tab -> tab.setOnClosed(new TabOnClosedEventHandler(combinedProgressiveImageOrderRenderer, tab)));
		});
	}
	
	@SuppressWarnings("unused")
	private void doOpen(final boolean isUsingGPU) {
		try {
			final
			FileChooser fileChooser = new FileChooser();
			fileChooser.setInitialDirectory(Files.findClosestExistingDirectoryTo(INITIAL_DIRECTORY_SCENES));
			fileChooser.setTitle(TITLE_OPEN);
			
			final File file = fileChooser.showOpenDialog(doGetStage());
			
			if(file != null) {
				this.executorService.execute(() -> doOpen(isUsingGPU, file));
			}
		} catch(final IllegalArgumentException e) {
//			Do nothing for now.
		}
	}
	
	private void doOpen(final boolean isUsingGPU, final File file) {
		final SceneLoader sceneLoader = new JavaSceneLoader();
		
		final Scene scene = sceneLoader.load(file, new Scene(), new ParameterList(new ParameterLoaderImpl(doGetStage())));
		
		final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = Renderers.createCombinedProgressiveImageOrderRenderer(scene, isUsingGPU);
		
		this.nodeSelectionTabPane.addLater(combinedProgressiveImageOrderRenderer, tab -> tab.setOnClosed(new TabOnClosedEventHandler(combinedProgressiveImageOrderRenderer, tab)));
	}
	
	private void doSave() {
		this.nodeSelectionTabPane.getSelectedNode().ifPresent(rendererTabPane -> rendererTabPane.save());
	}
	
	private void doSaveAs() {
		this.nodeSelectionTabPane.getSelectedNode().ifPresent(rendererTabPane -> rendererTabPane.saveAs());
	}
	
	private void doSetStage(final Stage stage) {
		this.stage.set(Objects.requireNonNull(stage, "stage == null"));
	}
}