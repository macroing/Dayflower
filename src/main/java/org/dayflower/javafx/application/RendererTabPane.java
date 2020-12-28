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
		this.renderer.getScene().addSceneObserver(new SceneObserverImpl(this.renderer, this));
		
//		Configure the RendererTabPane:
		getTabs().add(new Tab(TEXT_VIEW, this.rendererViewPane));
		getSelectionModel().select(0);
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
}