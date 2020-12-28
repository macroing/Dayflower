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

import org.dayflower.image.Image;
import org.dayflower.renderer.ImageOrderRenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

final class RendererObserverImpl implements RendererObserver {
	private final Label labelRenderPass;
	private final Label labelRenderTime;
	private final Label labelRenderTimePerPass;
	private final ProgressBar progressBar;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererObserverImpl(final Label labelRenderPass, final Label labelRenderTime, final Label labelRenderTimePerPass, final ProgressBar progressBar) {
		this.labelRenderPass = Objects.requireNonNull(labelRenderPass, "labelRenderPass == null");
		this.labelRenderTime = Objects.requireNonNull(labelRenderTime, "labelRenderTime == null");
		this.labelRenderTimePerPass = Objects.requireNonNull(labelRenderTimePerPass, "labelRenderTimePerPass == null");
		this.progressBar = Objects.requireNonNull(progressBar, "progressBar == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onRenderDisplay(final Renderer renderer, final Image image) {
//		Do nothing.
	}
	
	@Override
	public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
		Platform.runLater(() -> {
			this.labelRenderPass.setText("Render Pass: " + (renderer instanceof ImageOrderRenderer ? ImageOrderRenderer.class.cast(renderer).getRenderPass() : 0));
			this.labelRenderTime.setText("Render Time: " + renderer.getTimer().getTime());
			this.labelRenderTimePerPass.setText("Render Time Per Pass: " + elapsedTimeMillis);
			
			this.progressBar.setProgress(1.0D);
		});
	}
	
	@Override
	public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
		Platform.runLater(() -> {
			this.labelRenderPass.setText("Render Pass: " + (renderer instanceof ImageOrderRenderer ? ImageOrderRenderer.class.cast(renderer).getRenderPass() : 0));
			this.labelRenderTime.setText("Render Time: " + renderer.getTimer().getTime());
			
			this.progressBar.setProgress(percent);
		});
	}
}