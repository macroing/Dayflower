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

import org.dayflower.image.Image;
import org.dayflower.javafx.scene.canvas.ConcurrentImageCanvas;
import org.dayflower.javafx.scene.canvas.ConcurrentImageCanvas.Observer;
import org.dayflower.renderer.Renderer;

final class ObserverImpl implements Observer {
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ObserverImpl(final Renderer renderer) {
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onMouseDragged(final ConcurrentImageCanvas<? extends Image> concurrentImageCanvas, final float x, final float y) {
		final
		Renderer renderer = this.renderer;
		renderer.getScene().getCamera().rotate(x, y);
	}
}