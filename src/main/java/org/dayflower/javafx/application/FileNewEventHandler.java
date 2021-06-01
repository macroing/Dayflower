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
import java.util.concurrent.atomic.AtomicBoolean;

import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.PerezLight;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

final class FileNewEventHandler implements EventHandler<ActionEvent> {
	private final AtomicBoolean isUsingGPU;
	private final ExecutorService executorService;
	private final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public FileNewEventHandler(final AtomicBoolean isUsingGPU, final ExecutorService executorService, final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane) {
		this.isUsingGPU = Objects.requireNonNull(isUsingGPU, "isUsingGPU == null");
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.nodeSelectionTabPane = Objects.requireNonNull(nodeSelectionTabPane, "nodeSelectionTabPane == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final ActionEvent actionEvent) {
		this.executorService.execute(this::doNew);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doNew() {
		final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = Renderers.createCombinedProgressiveImageOrderRenderer(doCreateScene(), this.isUsingGPU.get());
		
		this.nodeSelectionTabPane.addLater(combinedProgressiveImageOrderRenderer, tab -> tab.setOnClosed(new TabOnClosedEventHandler(combinedProgressiveImageOrderRenderer, tab)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Scene doCreateScene() {
		final
		Scene scene = new Scene();
		scene.addLight(new PerezLight());
		
		return scene;
	}
}