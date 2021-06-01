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
package org.dayflower.javafx.application;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

final class RendererTabPane extends TabPane {
	private static final String TEXT_VIEW = "View";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer;
	private final RendererViewPane rendererViewPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererTabPane(final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer, final ExecutorService executorService) {
		this.combinedProgressiveImageOrderRenderer = Objects.requireNonNull(combinedProgressiveImageOrderRenderer, "combinedProgressiveImageOrderRenderer == null");
		this.rendererViewPane = new RendererViewPane(combinedProgressiveImageOrderRenderer, Objects.requireNonNull(executorService, "executorService == null"));
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CombinedProgressiveImageOrderRenderer getCombinedProgressiveImageOrderRenderer() {
		return this.combinedProgressiveImageOrderRenderer;
	}
	
	public RendererViewPane getRendererViewPane() {
		return this.rendererViewPane;
	}
	
	public void handleExitRequest() {
		this.combinedProgressiveImageOrderRenderer.renderShutdown();
		this.combinedProgressiveImageOrderRenderer.dispose();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Configure the Scene:
		this.combinedProgressiveImageOrderRenderer.getScene().addSceneObserver(new SceneObserverImpl(this.combinedProgressiveImageOrderRenderer, this));
		
//		Configure the RendererTabPane:
		getTabs().add(new Tab(TEXT_VIEW, this.rendererViewPane));
		getSelectionModel().select(0);
		setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
	}
}