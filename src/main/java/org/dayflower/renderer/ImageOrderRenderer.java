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
package org.dayflower.renderer;

import org.dayflower.sampler.Sampler;

/**
 * An {@code ImageOrderRenderer} is a {@link Renderer} that performs image order rendering.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface ImageOrderRenderer extends Renderer {
	/**
	 * Returns the {@link Sampler} instance associated with this {@code ImageOrderRenderer} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code ImageOrderRenderer} instance
	 */
	Sampler getSampler();
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code Renderer} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	void setSampler(final Sampler sampler);
}