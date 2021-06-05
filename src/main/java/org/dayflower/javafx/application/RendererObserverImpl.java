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

import org.dayflower.image.ImageF;
import org.dayflower.renderer.ProgressiveImageOrderRenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;

final class RendererObserverImpl implements RendererObserver {
	private final StatusBar statusBar;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public RendererObserverImpl(final StatusBar statusBar) {
		this.statusBar = Objects.requireNonNull(statusBar, "statusBar == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onRenderDisplay(final Renderer renderer, final ImageF image) {
//		Do nothing.
	}
	
	@Override
	public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
		if(renderer instanceof ProgressiveImageOrderRenderer) {
			final ProgressiveImageOrderRenderer progressiveImageOrderRenderer = ProgressiveImageOrderRenderer.class.cast(renderer);
			
			final
			StatusBar statusBar = this.statusBar;
			statusBar.setComplete(progressiveImageOrderRenderer.getRenderPass(), progressiveImageOrderRenderer.getTimer().getTime(), elapsedTimeMillis);
		}
	}
	
	@Override
	public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
		if(renderer instanceof ProgressiveImageOrderRenderer) {
			final ProgressiveImageOrderRenderer progressiveImageOrderRenderer = ProgressiveImageOrderRenderer.class.cast(renderer);
			
			final
			StatusBar statusBar = this.statusBar;
			statusBar.setProgress(progressiveImageOrderRenderer.getRenderPass(), progressiveImageOrderRenderer.getTimer().getTime(), percent);
		}
	}
}