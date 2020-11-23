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
package org.dayflower.renderer;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.image.Image;

//TODO: Add Javadocs!
public final class NoOpRendererObserver implements RendererObserver {
//	TODO: Add Javadocs!
	public NoOpRendererObserver() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderDisplay(final Renderer renderer, final Image image) {
		Objects.requireNonNull(renderer, "renderer == null");
		Objects.requireNonNull(image, "image == null");
	}
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
		Objects.requireNonNull(renderer, "renderer == null");
	}
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
		Objects.requireNonNull(renderer, "renderer == null");
	}
}