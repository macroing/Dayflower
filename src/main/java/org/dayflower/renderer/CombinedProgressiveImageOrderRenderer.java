/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

/**
 * A {@code CombinedProgressiveImageOrderRenderer} is a {@link ProgressiveImageOrderRenderer} that performs progressive image order rendering using different algorithms.
 * <p>
 * This interface also extends {@link AmbientOcclusion} and {@link PathTracer} for configuration purposes.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface CombinedProgressiveImageOrderRenderer extends AmbientOcclusion, PathTracer, ProgressiveImageOrderRenderer {
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code CombinedProgressiveImageOrderRenderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code CombinedProgressiveImageOrderRenderer} instance
	 */
	RenderingAlgorithm getRenderingAlgorithm();
	
	/**
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code CombinedProgressiveImageOrderRenderer} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code CombinedProgressiveImageOrderRenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm);
}