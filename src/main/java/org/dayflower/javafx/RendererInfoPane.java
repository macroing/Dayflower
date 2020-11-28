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
package org.dayflower.javafx;

import java.util.Objects;

import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RenderingAlgorithm;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

final class RendererInfoPane extends BorderPane {
	private final ComboBox<RenderingAlgorithm> comboBoxRenderingAlgorithm;
	private final GridPane gridPaneRendererConfiguration;
	private final Label labelRendererConfiguration;
	private final Label labelRenderingAlgorithm;
	private final Renderer renderer;
	private final VBox vBox;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererInfoPane(final Renderer renderer) {
		this.comboBoxRenderingAlgorithm = new ComboBox<>();
		this.gridPaneRendererConfiguration = JavaFX.createGridPane(10.0D, 10.0D);
		this.labelRendererConfiguration = new Label();
		this.labelRenderingAlgorithm = new Label();
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		this.vBox = new VBox();
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Configure the ComboBox for Rendering Algorithm:
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.AMBIENT_OCCLUSION);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_P_B_R_T);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_RAYITO);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_RECURSIVE);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.RAY_CASTING);
		this.comboBoxRenderingAlgorithm.getSelectionModel().selectedItemProperty().addListener(this::doHandleChangeComboBoxRenderingAlgorithm);
		this.comboBoxRenderingAlgorithm.setValue(this.renderer.getRendererConfiguration().getRenderingAlgorithm());
		
//		Configure the Label for Renderer Configuration:
		this.labelRendererConfiguration.setText("Configuration");
		
//		Configure the Label for Rendering Algorithm:
		this.labelRenderingAlgorithm.setText("Algorithm:");
		
		this.gridPaneRendererConfiguration.add(this.labelRenderingAlgorithm, 0, 0);
		this.gridPaneRendererConfiguration.add(this.comboBoxRenderingAlgorithm, 1, 0);
		this.gridPaneRendererConfiguration.getColumnConstraints().addAll(JavaFX.createHorizontalColumnConstraints(Priority.ALWAYS, 200.0D), JavaFX.createHorizontalColumnConstraints(Priority.NEVER));
		
//		Configure the VBox:
		this.vBox.getChildren().addAll(this.labelRendererConfiguration, JavaFX.createRegion(10.0D, 0.0D, 0.0D, 0.0D), JavaFX.createSeparator(), JavaFX.createRegion(10.0D, 0.0D, 0.0D, 0.0D), this.gridPaneRendererConfiguration);
		this.vBox.setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		
//		Configure the RendererInfoPane:
		setCenter(this.vBox);
	}
	
	@SuppressWarnings("unused")
	private void doHandleChangeComboBoxRenderingAlgorithm(final ObservableValue<? extends RenderingAlgorithm> observableValue, final RenderingAlgorithm oldRenderingAlgorithm, final RenderingAlgorithm newRenderingAlgorithm) {
		this.renderer.getRendererConfiguration().setRenderingAlgorithm(newRenderingAlgorithm);
		this.renderer.clear();
	}
}