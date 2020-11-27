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

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;
import org.dayflower.javafx.HierarchicalMenuBar;
import org.dayflower.javafx.SelectionTabPane;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneLoader;
import org.dayflower.scene.loader.JavaSceneLoader;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public final class DayflowerApplication2 extends Application {
	private final AtomicReference<Stage> stage;
	private final BorderPane borderPane;
	private final HierarchicalMenuBar hierarchicalMenuBar;
	private final SelectionTabPane<RendererMainPane, Renderer> selectionTabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public DayflowerApplication2() {
		this.stage = new AtomicReference<>();
		this.borderPane = new BorderPane();
		this.hierarchicalMenuBar = new HierarchicalMenuBar();
		this.selectionTabPane = new SelectionTabPane<>(RendererMainPane.class, (a, b) -> a.equals(b), rendererPane -> rendererPane.getRenderer(), renderer -> new RendererMainPane(renderer), renderer -> renderer.getRendererConfiguration().getScene().getName());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void start(final Stage stage) {
		doSetStage(stage);
		doConfigureBorderPane();
		doConfigureHierarchicalMenuBar();
		doConfigureSelectionTabPane();
//		doAddRenderer();
		doConfigureAndShowStage();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
		launch(args);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Stage doGetStage() {
		return this.stage.get();
	}
	
	private void doAddRenderer() {
		doAddRenderer(new File("./resources/scenes/PBRTDefault.java"));
	}
	
	private void doAddRenderer(final File file) {
		final SceneLoader sceneLoader = new JavaSceneLoader();
		
		final Scene scene = sceneLoader.load(file);
		
		final Camera camera = scene.getCamera();
		
		final int resolutionX = (int)(camera.getResolutionX());
		final int resolutionY = (int)(camera.getResolutionY());
		
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setImage(new Image(resolutionX, resolutionY));
		rendererConfiguration.setSampler(new RandomSampler());
		rendererConfiguration.setScene(scene);
		
		final Renderer renderer = new CPURenderer(rendererConfiguration, new NoOpRendererObserver());
		
		this.selectionTabPane.add(renderer);
		
//		final
//		Stage stage = doGetStage();
//		stage.sizeToScene();
	}
	
	private void doConfigureAndShowStage() {
		final
		Stage stage = doGetStage();
		stage.setResizable(false);
		stage.setScene(new javafx.scene.Scene(this.borderPane, 800.0D, 800.0D));
		stage.setTitle("Dayflower");
//		stage.sizeToScene();
		stage.show();
	}
	
	private void doConfigureBorderPane() {
		this.borderPane.setCenter(this.selectionTabPane);
		this.borderPane.setTop(this.hierarchicalMenuBar);
	}
	
	private void doConfigureHierarchicalMenuBar() {
		this.hierarchicalMenuBar.setPathElementText("File", "File");
		this.hierarchicalMenuBar.addMenuItem("File", "Open", this::doHandleEventFileOpen, new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN), true);
		this.hierarchicalMenuBar.addSeparatorMenuItem("File");
		this.hierarchicalMenuBar.addMenuItem("File", "Save", this::doHandleEventFileSave, null, false);
		this.hierarchicalMenuBar.addMenuItem("File", "Save As...", this::doHandleEventFileSaveAs, null, false);
		this.hierarchicalMenuBar.addSeparatorMenuItem("File");
		this.hierarchicalMenuBar.addMenuItem("File", "Exit", this::doHandleEventFileExit, null, true);
	}
	
	private void doConfigureSelectionTabPane() {
		this.selectionTabPane.getSelectionModel().selectedItemProperty().addListener(this::doHandleTabChangeSelectionTabPane);
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileExit(final ActionEvent actionEvent) {
//		TODO: Implement!
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileOpen(final ActionEvent actionEvent) {
		final
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("./resources/scenes"));
		fileChooser.setTitle("Open");
		
		final File file = fileChooser.showOpenDialog(doGetStage());
		
		if(file != null) {
			doAddRenderer(file);
		}
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSave(final ActionEvent actionEvent) {
//		TODO: Implement!
	}
	
	@SuppressWarnings("unused")
	private void doHandleEventFileSaveAs(final ActionEvent actionEvent) {
//		TODO: Implement!
	}
	
	@SuppressWarnings("unused")
	private void doHandleTabChangeSelectionTabPane(final ObservableValue<? extends Tab> observableValue, final Tab oldTab, final Tab newTab) {
		if(newTab != null) {
			this.hierarchicalMenuBar.getMenuItem("File", "Save").setDisable(false);
			this.hierarchicalMenuBar.getMenuItem("File", "Save As...").setDisable(false);
		} else {
			this.hierarchicalMenuBar.getMenuItem("File", "Save").setDisable(true);
			this.hierarchicalMenuBar.getMenuItem("File", "Save As...").setDisable(true);
		}
	}
	
	private void doSetStage(final Stage stage) {
		this.stage.set(Objects.requireNonNull(stage, "stage == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererInfoPane extends BorderPane {
		private final Renderer renderer;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererInfoPane(final Renderer renderer) {
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererMainPane extends BorderPane {
		private final Renderer renderer;
		private final RendererInfoPane rendererInfoPane;
		private final RendererViewPane rendererViewPane;
		private final TabPane tabPane;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererMainPane(final Renderer renderer) {
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
			this.rendererInfoPane = new RendererInfoPane(renderer);
			this.rendererViewPane = new RendererViewPane(renderer);
			this.tabPane = new TabPane();
			this.tabPane.getTabs().add(new Tab("Info", this.rendererInfoPane));
			this.tabPane.getTabs().add(new Tab("View", this.rendererViewPane));
			this.tabPane.getSelectionModel().select(0);
			this.tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
			
			setCenter(this.tabPane);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Renderer getRenderer() {
			return this.renderer;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class RendererViewPane extends BorderPane {
		private final Canvas canvas;
		private final Renderer renderer;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public RendererViewPane(final Renderer renderer) {
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
			this.canvas = new Canvas(800.0D, 800.0D);
			
			setCenter(this.canvas);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		
	}
}