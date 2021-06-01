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
import java.util.concurrent.TimeUnit;

import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Tab;

final class FileExitEventHandler implements EventHandler<ActionEvent> {
	private final ExecutorService executorService;
	private final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public FileExitEventHandler(final ExecutorService executorService, final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane) {
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.nodeSelectionTabPane = Objects.requireNonNull(nodeSelectionTabPane, "nodeSelectionTabPane == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final ActionEvent actionEvent) {
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
			
		}
		
		Platform.exit();
	}
}