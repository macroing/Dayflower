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

import java.io.File;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.javafx.scene.control.NodeSelectionTabPane;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.utility.Files;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

final class FileSaveEventHandler implements EventHandler<ActionEvent> {
	private static final File INITIAL_DIRECTORY_IMAGES = new File(".");
	private static final String TITLE_SAVE_AS = "Save As...";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane;
	private final Stage stage;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public FileSaveEventHandler(final NodeSelectionTabPane<RendererTabPane, CombinedProgressiveImageOrderRenderer> nodeSelectionTabPane, final Stage stage) {
		this.nodeSelectionTabPane = Objects.requireNonNull(nodeSelectionTabPane, "nodeSelectionTabPane == null");
		this.stage = Objects.requireNonNull(stage, "stage == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void handle(final ActionEvent actionEvent) {
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
				fileChooser.setInitialDirectory(Files.findClosestExistingDirectoryTo(INITIAL_DIRECTORY_IMAGES));
				fileChooser.setTitle(TITLE_SAVE_AS);
				
				final File file = fileChooser.showSaveDialog(this.stage);
				
				if(file != null) {
					rendererViewPane.setFile(file);
					rendererViewPane.save();
				}
			}
		}
	}
}