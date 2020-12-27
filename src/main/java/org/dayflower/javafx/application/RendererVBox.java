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

import org.dayflower.javafx.scene.control.Labels;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RenderingAlgorithm;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

final class RendererVBox extends VBox {
	private final Button buttonUpdateRenderer;
	private final ComboBox<RenderingAlgorithm> comboBoxRenderingAlgorithm;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererVBox(final Renderer renderer) {
		this.buttonUpdateRenderer = new Button();
		this.comboBoxRenderingAlgorithm = new ComboBox<>();
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doConfigure() {
//		Configure the Button for Update Renderer:
		this.buttonUpdateRenderer.setMaxWidth(Double.MAX_VALUE);
		this.buttonUpdateRenderer.setOnAction(this::doOnActionButtonUpdateRenderer);
		this.buttonUpdateRenderer.setText("Update Renderer");
		
//		Configure the ComboBox for Rendering Algorithm:
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.AMBIENT_OCCLUSION);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_P_B_R_T);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_RAYITO);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.PATH_TRACING_SMALL_P_T_RECURSIVE);
		this.comboBoxRenderingAlgorithm.getItems().add(RenderingAlgorithm.RAY_CASTING);
		this.comboBoxRenderingAlgorithm.setValue(this.renderer.getRendererConfiguration().getRenderingAlgorithm());
		
//		Configure the RendererBox:
		getChildren().add(Labels.createLabel("Renderer Configuration", 16.0D));
		getChildren().add(this.comboBoxRenderingAlgorithm);
		getChildren().add(this.buttonUpdateRenderer);
		setAlignment(Pos.CENTER);
		setFillWidth(true);
		setSpacing(10.0D);
	}
	
	@SuppressWarnings("unused")
	private void doOnActionButtonUpdateRenderer(final ActionEvent actionEvent) {
		final RenderingAlgorithm renderingAlgorithm = this.comboBoxRenderingAlgorithm.getValue();
		
		if(renderingAlgorithm != null) {
			this.renderer.getRendererConfiguration().setRenderingAlgorithm(renderingAlgorithm);
			this.renderer.renderShutdown();
			this.renderer.clear();
		}
	}
}