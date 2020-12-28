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
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

/**
 * A {@code Renderer} is a renderer that can render a {@link Scene} instance to an {@link Image} instance and display the result with a {@link RendererObserver} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Renderer {
	/**
	 * Returns the {@link Image} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Image} instance associated with this {@code Renderer} instance
	 */
	Image getImage();
	
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code Renderer} instance
	 */
	RenderingAlgorithm getRenderingAlgorithm();
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code Renderer} instance
	 */
	RendererObserver getRendererObserver();
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code Renderer} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code Renderer} instance
	 */
	Sampler getSampler();
	
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
	 * Returns the preview mode state associated with this {@code Renderer} instance.
	 * 
	 * @return the preview mode state associated with this {@code Renderer} instance
	 */
	boolean isPreviewMode();
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link Image} instance and, optionally, updates the associated {@link Display} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	boolean render();
	
	/**
	 * Attempts to shutdown the rendering process of this {@code Renderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code Renderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Renderer} instance was rendering and is shutting down, {@code false} otherwise
	 */
	boolean renderShutdown();
	
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
	 * Sets the {@link Image} instance associated with this {@code Renderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code Image} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	void setImage(final Image image);
	
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
	 * Sets the preview mode state associated with this {@code Renderer} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code Renderer} instance
	 */
	void setPreviewMode(final boolean isPreviewMode);
	
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
	 * Sets the current render time in milliseconds to {@code renderTime}.
	 * 
	 * @param renderTime the current render time in milliseconds
	 */
	void setRenderTime(final long renderTime);
	
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
	 * Sets the {@link Timer} instance associated with this {@code Renderer} instance to {@code timer}.
	 * <p>
	 * If {@code timer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param timer the {@code Timer} instance associated with this {@code Renderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code timer} is {@code null}
	 */
	void setTimer(final Timer timer);
	
	/**
	 * Sets up all necessary resources for this {@code Renderer} instance.
	 */
	void setup();
}