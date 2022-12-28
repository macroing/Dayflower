/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import org.dayflower.image.ImageF;
import org.dayflower.javafx.scene.layout.Regions;
import org.dayflower.renderer.ProgressiveImageOrderRenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

final class RendererStatusBar extends HBox {
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final ProgressBar progressBar;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererStatusBar(final Renderer renderer) {
		Objects.requireNonNull(renderer, "renderer == null");
		
		this.labelRenderPass = new Label("Render Pass: 0");
		this.labelRenderTime = new Label("Render Time: 00:00:00");
		this.labelRenderTimePerPass = new Label("Render Time Per Pass: 0");
		this.progressBar = new ProgressBar(0.0D);
		
		getChildren().add(this.labelRenderPass);
		getChildren().add(this.labelRenderTime);
		getChildren().add(this.labelRenderTimePerPass);
		getChildren().add(Regions.createRegionHBoxHorizontalGrowAlways());
		getChildren().add(this.progressBar);
		
		setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1.0D, 0.0D, 0.0D, 0.0D))));
		setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		setSpacing(20.0D);
		
		renderer.setRendererObserver(new RendererObserverImpl());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void setComplete(final int renderPass, final String renderTime, final long renderTimePerPass) {
		if(Platform.isFxApplicationThread()) {
			this.labelRenderPass.setText("Render Pass: " + renderPass);
			this.labelRenderTime.setText("Render Time: " + renderTime);
			this.labelRenderTimePerPass.setText("Render Time Per Pass: " + renderTimePerPass);
			
			this.progressBar.setProgress(1.0D);
		} else {
			Platform.runLater(() -> setComplete(renderPass, renderTime, renderTimePerPass));
		}
	}
	
	public void setProgress(final int renderPass, final String renderTime, final double percent) {
		if(Platform.isFxApplicationThread()) {
			this.labelRenderPass.setText("Render Pass: " + renderPass);
			this.labelRenderTime.setText("Render Time: " + renderTime);
			
			this.progressBar.setProgress(percent);
		} else {
			Platform.runLater(() -> setProgress(renderPass, renderTime, percent));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final class RendererObserverImpl implements RendererObserver {
		public RendererObserverImpl() {
			
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onRenderDisplay(final Renderer renderer, final ImageF image) {
//			Do nothing.
		}
		
		@Override
		public void onRenderPassComplete(final Renderer renderer, final int renderPass, final long elapsedTimeMillis) {
			if(renderer instanceof ProgressiveImageOrderRenderer) {
				final ProgressiveImageOrderRenderer progressiveImageOrderRenderer = ProgressiveImageOrderRenderer.class.cast(renderer);
				
				setComplete(progressiveImageOrderRenderer.getRenderPass(), progressiveImageOrderRenderer.getTimer().getTime(), elapsedTimeMillis);
			}
		}
		
		@Override
		public void onRenderPassProgress(final Renderer renderer, final int renderPass, final double percent) {
			if(renderer instanceof ProgressiveImageOrderRenderer) {
				final ProgressiveImageOrderRenderer progressiveImageOrderRenderer = ProgressiveImageOrderRenderer.class.cast(renderer);
				
				setProgress(progressiveImageOrderRenderer.getRenderPass(), progressiveImageOrderRenderer.getTimer().getTime(), percent);
			}
		}
	}
}