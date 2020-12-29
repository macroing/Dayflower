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
import org.dayflower.util.Timer;

/**
 * A {@code ProgressiveImageOrderRenderer} is an {@link ImageOrderRenderer} that performs progressive image order rendering.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface ProgressiveImageOrderRenderer extends ImageOrderRenderer {
	/**
	 * Returns the {@link Timer} instance associated with this {@code ProgressiveImageOrderRenderer} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code ProgressiveImageOrderRenderer} instance
	 */
	Timer getTimer();
	
	/**
	 * Returns {@code true} if, and only if, this {@code ProgressiveImageOrderRenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ProgressiveImageOrderRenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	boolean isClearing();
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	int getRenderPass();
	
	/**
	 * Returns the render passes to perform.
	 * 
	 * @return the render passes to perform
	 */
	int getRenderPasses();
	
	/**
	 * Returns the render passes to perform before the display is updated.
	 * 
	 * @return the render passes to perform before the display is updated
	 */
	int getRenderPassesPerDisplayUpdate();
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	void clear();
	
	/**
	 * Sets the current render pass to {@code renderPass}.
	 * 
	 * @param renderPass the current render pass
	 */
	void setRenderPass(final int renderPass);
	
	/**
	 * Sets the render passes to perform to {@code renderPasses}.
	 * 
	 * @param renderPasses the render passes to perform
	 */
	void setRenderPasses(final int renderPasses);
	
	/**
	 * Sets the render passes to perform before the display is updated to {@code renderPassesPerDisplayUpdate}.
	 * 
	 * @param renderPassesPerDisplayUpdate the render passes to perform before the display is updated
	 */
	void setRenderPassesPerDisplayUpdate(final int renderPassesPerDisplayUpdate);
	
	/**
	 * Sets the {@link Timer} instance associated with this {@code ProgressiveImageOrderRenderer} instance to {@code timer}.
	 * <p>
	 * If {@code timer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param timer the {@code Timer} instance associated with this {@code ProgressiveImageOrderRenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code timer} is {@code null}
	 */
	void setTimer(final Timer timer);
}