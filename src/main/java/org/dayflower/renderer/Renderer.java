/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.dayflower.image.ImageF;
import org.dayflower.scene.Scene;

/**
 * A {@code Renderer} is a renderer that can render a {@link Scene} instance to an {@link ImageF} instance and display the result with a {@link RendererObserver} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Renderer {
	/**
	 * Returns the {@link ImageF} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code ImageF} instance associated with this {@code Renderer} instance
	 */
	ImageF getImage();
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code Renderer} instance
	 */
	RendererObserver getRendererObserver();
	
	/**
	 * Returns the {@link Scene} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Scene} instance associated with this {@code Renderer} instance
	 */
	Scene getScene();
	
	/**
	 * Returns the preview mode state associated with this {@code Renderer} instance.
	 * 
	 * @return the preview mode state associated with this {@code Renderer} instance
	 */
	boolean isPreviewMode();
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link ImageF} instance and, optionally, updates the associated {@link RendererObserver} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	boolean render();
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link ImageF} instance and, optionally, updates the associated {@link RendererObserver} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed for all render passes, {@code false} otherwise.
	 * <p>
	 * If {@code renderPasses} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * <code>
	 * for(int renderPass = 0; renderPass &lt; renderPasses; renderPass++) {
	 *     if(!renderer.render()) {
	 *         break;
	 *     }
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param renderPasses the number of render passes to perform rendering
	 * @return {@code true} if, and only if, rendering was performed for all render passes, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code renderPasses} is less than {@code 1}
	 */
	boolean render(final int renderPasses);
	
	/**
	 * Attempts to shutdown the rendering process of this {@code Renderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code Renderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Renderer} instance was rendering and is shutting down, {@code false} otherwise
	 */
	boolean renderShutdown();
	
	/**
	 * Disposes of any resources created by this {@code Renderer} instance.
	 */
	void dispose();
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code Renderer} instance based on the current setup.
	 */
	void setImage();
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code Renderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code ImageF} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	void setImage(final ImageF image);
	
	/**
	 * Sets the preview mode state associated with this {@code Renderer} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code Renderer} instance
	 */
	void setPreviewMode(final boolean isPreviewMode);
	
	/**
	 * Sets the {@link RendererObserver} instance associated with this {@code Renderer} instance to {@code rendererObserver}.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@code RendererObserver} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	void setRendererObserver(final RendererObserver rendererObserver);
	
	/**
	 * Sets the {@link Scene} instance associated with this {@code Renderer} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@code Scene} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	void setScene(final Scene scene);
	
	/**
	 * Sets up all necessary resources for this {@code Renderer} instance.
	 */
	void setup();
}