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
package org.dayflower.renderer.cpu;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Timer;

/**
 * An {@code AbstractCPURenderer} is an abstract implementation of {@link CombinedProgressiveImageOrderRenderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractCPURenderer implements CombinedProgressiveImageOrderRenderer {
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererObserver> rendererObserver;
	private ImageF image;
	private RenderingAlgorithm renderingAlgorithm;
	private Scene scene;
	private Timer timer;
	private boolean isPreviewMode;
	private float maximumDistance;
	private int maximumBounce;
	private int minimumBounceRussianRoulette;
	private int renderPass;
	private int renderPasses;
	private int renderPassesPerDisplayUpdate;
	private int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractCPURenderer} instance.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	protected AbstractCPURenderer(final RendererObserver rendererObserver) {
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
		this.image = new PixelImageF(800, 800);
		this.renderingAlgorithm = RenderingAlgorithm.PATH_TRACING;
		this.scene = new Scene();
		this.timer = new Timer();
		this.isPreviewMode = false;
		this.maximumDistance = 20.0F;
		this.maximumBounce = 20;
		this.minimumBounceRussianRoulette = 5;
		this.renderPass = 0;
		this.renderPasses = 1000;
		this.renderPassesPerDisplayUpdate = 10;
		this.samples = 10;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link ImageF} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code ImageF} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final ImageF getImage() {
		return this.image;
	}
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final RendererObserver getRendererObserver() {
		return this.rendererObserver.get();
	}
	
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final RenderingAlgorithm getRenderingAlgorithm() {
		return this.renderingAlgorithm;
	}
	
	/**
	 * Returns the {@link Scene} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code Scene} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Returns the {@link Timer} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final Timer getTimer() {
		return this.timer;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@link ImageF} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@code ImageF} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
	}
	
	/**
	 * Returns the preview mode state associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the preview mode state associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final boolean isPreviewMode() {
		return this.isPreviewMode;
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link ImageF} instance and, optionally, updates the associated {@link RendererObserver} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	@Override
	public final boolean render() {
		this.isRendering.set(true);
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final ImageF image = getImage();
		
		if(!(image instanceof PixelImageF)) {
			return false;
		}
		
		final PixelImageF pixelImage = PixelImageF.class.cast(image);
		
		final Sampler sampler = getScene().getSampler();
		
		final Scene scene = getScene();
		
		final Timer timer = getTimer();
		
		final int renderPasses = getRenderPasses();
		final int renderPassesPerDisplayUpdate = getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Camera camera = scene.getCameraCopy();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing.compareAndSet(true, false)) {
				setRenderPass(0);
				
				pixelImage.filmClear();
				pixelImage.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
				
				timer.restart();
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 0.0D);
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final Sample2F sample = sampler.sample2();
					
					final float imageX = x;
					final float imageY = y;
					final float pixelX = sample.getX();
					final float pixelY = sample.getY();
					
					final Optional<Ray3F> optionalRay = camera.createPrimaryRay(imageX, imageY, pixelX, pixelY);
					
					if(optionalRay.isPresent()) {
						final Ray3F ray = optionalRay.get();
						
						final Color3F colorRGB = radiance(ray);
						
						if(!colorRGB.hasInfinites() && !colorRGB.hasNaNs() && colorRGB.luminance() >= -1.0e-5F) {
							final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
							
							pixelImage.filmAddColorXYZ(imageX + pixelX, imageY + pixelY, colorXYZ);
						}
					}
				}
				
				final double percent = ((y + 1.0D) * resolutionX) / (resolutionX * resolutionY);
				
				rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, percent);
				
				if(!this.isRendering.get()) {
					return false;
				}
			}
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				pixelImage.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
			}
			
			setRenderPass(getRenderPass() + 1);
			
			rendererObserver.onRenderPassComplete(this, renderPass, renderPasses, elapsedTimeMillis);
		}
		
		this.isRendering.set(false);
		
		return true;
	}
	
	/**
	 * Attempts to shutdown the rendering process of this {@code AbstractCPURenderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code AbstractCPURenderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractCPURenderer} instance was rendering and is shutting down, {@code false} otherwise
	 */
	@Override
	public final boolean renderShutdown() {
		return this.isRendering.compareAndSet(true, false);
	}
	
	/**
	 * Returns the maximum distance.
	 * 
	 * @return the maximum distance
	 */
	@Override
	public final float getMaximumDistance() {
		return this.maximumDistance;
	}
	
	/**
	 * Returns the maximum bounce.
	 * 
	 * @return the maximum bounce
	 */
	@Override
	public final int getMaximumBounce() {
		return this.maximumBounce;
	}
	
	/**
	 * Returns the minimum bounce before Russian roulette termination occurs.
	 * 
	 * @return the minimum bounce before Russian roulette termination occurs
	 */
	@Override
	public final int getMinimumBounceRussianRoulette() {
		return this.minimumBounceRussianRoulette;
	}
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	@Override
	public final int getRenderPass() {
		return this.renderPass;
	}
	
	/**
	 * Returns the render passes to perform.
	 * 
	 * @return the render passes to perform
	 */
	@Override
	public final int getRenderPasses() {
		return this.renderPasses;
	}
	
	/**
	 * Returns the render passes to perform before the display is updated.
	 * 
	 * @return the render passes to perform before the display is updated
	 */
	@Override
	public final int getRenderPassesPerDisplayUpdate() {
		return this.renderPassesPerDisplayUpdate;
	}
	
	/**
	 * Returns the samples to use per render pass.
	 * 
	 * @return the samples to use per render pass
	 */
	@Override
	public final int getSamples() {
		return this.samples;
	}
	
	/**
	 * Call this method to clear the {@link ImageF} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Disposes of any resources created by this {@code AbstractCPURenderer} instance.
	 */
	@Override
	public final void dispose() {
//		Do nothing!
	}
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code AbstractCPURenderer} instance based on the current setup.
	 */
	@Override
	public final void setImage() {
		final Scene scene = this.scene;
		
		final Camera camera = scene.getCamera();
		
		final int resolutionX = (int)(camera.getResolutionX());
		final int resolutionY = (int)(camera.getResolutionY());
		
		this.image = new PixelImageF(resolutionX, resolutionY);
	}
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code AbstractCPURenderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code ImageF} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public final void setImage(final ImageF image) {
		this.image = Objects.requireNonNull(image, "image == null");
	}
	
	/**
	 * Sets the maximum bounce to {@code maximumBounce}.
	 * 
	 * @param maximumBounce the maximum bounce
	 */
	@Override
	public final void setMaximumBounce(final int maximumBounce) {
		this.maximumBounce = maximumBounce;
	}
	
	/**
	 * Sets the maximum distance to {@code maximumDistance}.
	 * 
	 * @param maximumDistance the maximum distance
	 */
	@Override
	public final void setMaximumDistance(final float maximumDistance) {
		this.maximumDistance = maximumDistance;
	}
	
	/**
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	@Override
	public final void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette) {
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
	}
	
	/**
	 * Sets the preview mode state associated with this {@code AbstractCPURenderer} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final void setPreviewMode(final boolean isPreviewMode) {
		this.isPreviewMode = isPreviewMode;
	}
	
	/**
	 * Sets the current render pass to {@code renderPass}.
	 * 
	 * @param renderPass the current render pass
	 */
	@Override
	public final void setRenderPass(final int renderPass) {
		this.renderPass = renderPass;
	}
	
	/**
	 * Sets the render passes to perform to {@code renderPasses}.
	 * 
	 * @param renderPasses the render passes to perform
	 */
	@Override
	public final void setRenderPasses(final int renderPasses) {
		this.renderPasses = renderPasses;
	}
	
	/**
	 * Sets the render passes to perform before the display is updated to {@code renderPassesPerDisplayUpdate}.
	 * 
	 * @param renderPassesPerDisplayUpdate the render passes to perform before the display is updated
	 */
	@Override
	public final void setRenderPassesPerDisplayUpdate(final int renderPassesPerDisplayUpdate) {
		this.renderPassesPerDisplayUpdate = renderPassesPerDisplayUpdate;
	}
	
	/**
	 * Sets the {@link RendererObserver} instance associated with this {@code AbstractCPURenderer} instance to {@code rendererObserver}.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@code RendererObserver} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	@Override
	public final void setRendererObserver(final RendererObserver rendererObserver) {
		this.rendererObserver.set(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	/**
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code AbstractCPURenderer} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	@Override
	public final void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm) {
		this.renderingAlgorithm = Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null");
	}
	
	/**
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	@Override
	public final void setSamples(final int samples) {
		this.samples = samples;
	}
	
	/**
	 * Sets the {@link Scene} instance associated with this {@code AbstractCPURenderer} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@code Scene} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	@Override
	public final void setScene(final Scene scene) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
	}
	
	/**
	 * Sets the {@link Timer} instance associated with this {@code AbstractCPURenderer} instance to {@code timer}.
	 * <p>
	 * If {@code timer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param timer the {@code Timer} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code timer} is {@code null}
	 */
	@Override
	public final void setTimer(final Timer timer) {
		this.timer = Objects.requireNonNull(timer, "timer == null");
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractCPURenderer} instance.
	 */
	@Override
	public final void setup() {
//		Do nothing!
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	protected abstract Color3F radiance(final Ray3F ray);
}