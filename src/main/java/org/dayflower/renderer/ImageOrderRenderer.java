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
import org.dayflower.sampler.Sampler;

/**
 * An {@code ImageOrderRenderer} is a {@link Renderer} that performs image order rendering.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface ImageOrderRenderer extends Renderer {
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code ImageOrderRenderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code ImageOrderRenderer} instance
	 */
	RenderingAlgorithm getRenderingAlgorithm();
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code ImageOrderRenderer} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code ImageOrderRenderer} instance
	 */
	Sampler getSampler();
	
	/**
	 * Returns {@code true} if, and only if, this {@code Renderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Renderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	boolean isClearing();
	
	/**
	 * Returns the maximum distance.
	 * 
	 * @return the maximum distance
	 */
	float getMaximumDistance();
	
	/**
	 * Returns the maximum bounce.
	 * 
	 * @return the maximum bounce
	 */
	int getMaximumBounce();
	
	/**
	 * Returns the minimum bounce before Russian roulette termination occurs.
	 * 
	 * @return the minimum bounce before Russian roulette termination occurs
	 */
	int getMinimumBounceRussianRoulette();
	
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
	 * Returns the samples to use per render pass.
	 * 
	 * @return the samples to use per render pass
	 */
	int getSamples();
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	void clear();
	
	/**
	 * Sets the maximum bounce to {@code maximumBounce}.
	 * 
	 * @param maximumBounce the maximum bounce
	 */
	void setMaximumBounce(final int maximumBounce);
	
	/**
	 * Sets the maximum distance to {@code maximumDistance}.
	 * 
	 * @param maximumDistance the maximum distance
	 */
	void setMaximumDistance(final float maximumDistance);
	
	/**
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette);
	
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
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code Renderer} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm);
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code Renderer} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	void setSampler(final Sampler sampler);
	
	/**
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	void setSamples(final int samples);
}