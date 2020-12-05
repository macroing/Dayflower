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

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.BorderPane;

final class RendererMainPane extends BorderPane {
	private final Renderer renderer;
	private final RendererInfoPane rendererInfoPane;
	private final RendererViewPane rendererViewPane;
	private final TabPane tabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererMainPane(final Renderer renderer, final ExecutorService executorService) {
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		this.rendererInfoPane = new RendererInfoPane(renderer);
		this.rendererViewPane = new RendererViewPane(renderer, Objects.requireNonNull(executorService, "executorService == null"));
		this.tabPane = new TabPane();
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	public RendererInfoPane getRendererInfoPane() {
		return this.rendererInfoPane;
	}
	
	public RendererViewPane getRendererViewPane() {
		return this.rendererViewPane;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Configure the TabPane:
//		this.tabPane.getTabs().add(new Tab("Info", this.rendererInfoPane));
		this.tabPane.getTabs().add(new Tab("View", this.rendererViewPane));
//		this.tabPane.getSelectionModel().select(1);
		this.tabPane.getSelectionModel().select(0);
		this.tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
//		Configure the Scene:
		this.renderer.getRendererConfiguration().getScene().addSceneObserver(new SceneObserverImpl(this.renderer, this));
		
//		Configure the RendererMainPane:
		setCenter(this.tabPane);
	}
}