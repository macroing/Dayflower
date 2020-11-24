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

import org.dayflower.image.Image;

/**
 * A {@code RendererObserver} is used to observe the rendering process of a {@link Renderer} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface RendererObserver {
	/**
	 * This method is called by {@code renderer} when {@code image} should be displayed.
	 * <p>
	 * If either {@code renderer} or {@code image} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param image the {@link Image} instance that is rendered to
	 * @throws NullPointerException thrown if, and only if, either {@code renderer} or {@code image} are {@code null}
	 */
	void onRenderDisplay(final Renderer renderer, final Image image);
	
	/**
	 * This method is called by {@code renderer} when {@code renderPass} is complete.
	 * <p>
	 * If {@code renderer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param renderPass the current render pass
	 * @param renderPasses the total number of render passes
	 * @param elapsedTimeMillis the total number of milliseconds required to complete this render pass
	 * @throws NullPointerException thrown if, and only if, {@code renderer} is {@code null}
	 */
	void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis);
	
	/**
	 * This method is called by {@code renderer} when {@code renderPass} is processed.
	 * <p>
	 * If {@code renderer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param renderPass the current render pass
	 * @param renderPasses the total number of render passes
	 * @param percent the progress in percent between {@code 0.0D} and {@code 1.0D}
	 * @throws NullPointerException thrown if, and only if, {@code renderer} is {@code null}
	 */
	void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent);
}