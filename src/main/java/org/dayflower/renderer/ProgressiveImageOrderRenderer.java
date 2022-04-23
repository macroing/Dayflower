/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import org.macroing.java.util.Timer;

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
	 * Returns {@code true} if, and only if, this {@code ProgressiveImageOrderRenderer} instance is clearing the {@link ImageF} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code ProgressiveImageOrderRenderer} instance is clearing the {@code ImageF} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	boolean isClearing();
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	int getRenderPass();
	
	/**
	 * Call this method to clear the {@link ImageF} in the next {@link #render()} call.
	 */
	void clear();
}