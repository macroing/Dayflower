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

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.dayflower.renderer.Renderer;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneObserver;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

final class RendererTabPane extends TabPane {
	private static final String TEXT_VIEW = "View";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Renderer renderer;
	private final RendererViewPane rendererViewPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererTabPane(final Renderer renderer, final ExecutorService executorService) {
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		this.rendererViewPane = new RendererViewPane(renderer, Objects.requireNonNull(executorService, "executorService == null"));
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public RendererViewPane getRendererViewPane() {
		return this.rendererViewPane;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Configure the Scene:
		this.renderer.getRendererConfiguration().getScene().addSceneObserver(new SceneObserverImpl(this.renderer, this));
		
//		Configure the RendererTabPane:
		getTabs().add(new Tab(TEXT_VIEW, this.rendererViewPane));
		getSelectionModel().select(0);
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class SceneObserverImpl implements SceneObserver {
		private final Renderer renderer;
		private final RendererTabPane rendererTabPane;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public SceneObserverImpl(final Renderer renderer, final RendererTabPane rendererTabPane) {
			this.renderer = Objects.requireNonNull(renderer, "renderer == null");
			this.rendererTabPane = Objects.requireNonNull(rendererTabPane, "rendererTabPane == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onAddLight(final Scene scene, final Light newLight) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(newLight, "newLight == null");
		}
		
		@Override
		public void onAddPrimitive(final Scene scene, final Primitive newPrimitive) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(newPrimitive, "newPrimitive == null");
			
			Platform.runLater(() -> this.rendererTabPane.getRendererViewPane().getObjectTreeView().add(newPrimitive));
			
			this.renderer.renderShutdown();
			this.renderer.clear();
		}
		
		@Override
		public void onChangeCamera(final Scene scene, final Camera oldCamera) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldCamera, "oldCamera == null");
		}
		
		@Override
		public void onChangeCamera(final Scene scene, final Camera oldCamera, final Camera newCamera) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldCamera, "oldCamera == null");
			Objects.requireNonNull(newCamera, "newCamera == null");
		}
		
		@Override
		public void onChangeName(final Scene scene, final String oldName, final String newName) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldName, "oldName == null");
			Objects.requireNonNull(newName, "newName == null");
		}
		
		@Override
		public void onChangePrimitive(final Scene scene, final Primitive oldPrimitive) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
		}
		
		@Override
		public void onRemoveLight(final Scene scene, final Light oldLight) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldLight, "oldLight == null");
		}
		
		@Override
		public void onRemovePrimitive(final Scene scene, final Primitive oldPrimitive) {
			Objects.requireNonNull(scene, "scene == null");
			Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
			
			Platform.runLater(() -> this.rendererTabPane.getRendererViewPane().getObjectTreeView().remove(oldPrimitive));
			
			this.renderer.renderShutdown();
			this.renderer.clear();
		}
	}
}