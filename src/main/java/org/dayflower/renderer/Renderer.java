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

import org.dayflower.display.Display;
import org.dayflower.image.Image;
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

/**
 * A {@code Renderer} is a renderer that can render a {@link Scene} instance to an {@link Image} instance and display the result with a {@link Display} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Renderer {
	/**
	 * Returns the {@link Display} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Display} instance associated with this {@code Renderer} instance
	 */
	Display getDisplay();
	
	/**
	 * Returns the {@link Image} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Image} instance associated with this {@code Renderer} instance
	 */
	Image getImage();
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code Renderer} instance
	 */
	RendererConfiguration getRendererConfiguration();
	
	/**
	 * Returns the {@link Scene} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Scene} instance associated with this {@code Renderer} instance
	 */
	Scene getScene();
	
	/**
	 * Returns the {@link Timer} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code Renderer} instance
	 */
	Timer getTimer();
	
	/**
	 * Returns {@code true} if, and only if, this {@code Renderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Renderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	boolean isClearing();
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link Image} instance and, optionally, updates the associated {@link Display} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	boolean render();
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	int getRenderPass();
	
	/**
	 * Returns the current render time in milliseconds.
	 * 
	 * @return the current render time in milliseconds
	 */
	long getRenderTime();
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	void clear();
	
	/**
	 * Disposes of any resources created by this {@code Renderer} instance.
	 */
	void dispose();
	
	/**
	 * Sets the {@link Display} instance associated with this {@code Renderer} instance to {@code display}.
	 * <p>
	 * If {@code display} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@code Display} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code display} is {@code null}
	 */
	void setDisplay(final Display display);
	
	/**
	 * Sets the {@link Image} instance associated with this {@code Renderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code Image} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	void setImage(final Image image);
	
	/**
	 * Sets the {@link RendererConfiguration} instance associated with this {@code Renderer} instance to {@code rendererConfiguration}.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@code RendererConfiguration} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	void setRendererConfiguration(final RendererConfiguration rendererConfiguration);
	
	/**
	 * Sets the {@link Scene} instance associated with this {@code Renderer} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@code Scene} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	void setScene(final Scene scene);
}