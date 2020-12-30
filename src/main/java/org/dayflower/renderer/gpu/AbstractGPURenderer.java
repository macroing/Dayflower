/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.renderer.gpu;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.ByteImage;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.image.PixelImage;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.sampler.NRooksSampler;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

import com.amd.aparapi.Range;

/**
 * An {@code AbstractGPURenderer} is an abstract implementation of {@link CombinedProgressiveImageOrderRenderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGPURenderer extends AbstractSceneKernel implements CombinedProgressiveImageOrderRenderer {
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererObserver> rendererObserver;
	private Image image;
	private RenderingAlgorithm renderingAlgorithm;
	private Sampler sampler;
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
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	protected AbstractGPURenderer(final RendererObserver rendererObserver) {
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
		this.image = new PixelImage(800, 800);
		this.renderingAlgorithm = RenderingAlgorithm.PATH_TRACING;
		this.sampler = new NRooksSampler();
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
	 * Returns the {@link Image} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Image} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Image getImage() {
		return this.image;
	}
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererObserver getRendererObserver() {
		return this.rendererObserver.get();
	}
	
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RenderingAlgorithm getRenderingAlgorithm() {
		return this.renderingAlgorithm;
	}
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Sampler getSampler() {
		return this.sampler;
	}
	
	/**
	 * Returns the {@link Timer} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Timer getTimer() {
		return this.timer;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
	}
	
	/**
	 * Returns the preview mode state associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the preview mode state associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final boolean isPreviewMode() {
		return this.isPreviewMode;
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link Image} instance and, optionally, updates the associated {@link Display} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	@Override
	public final boolean render() {
		this.isRendering.set(true);
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final Image image = getImage();
		
		final Timer timer = getTimer();
		
		final int renderPasses = getRenderPasses();
		final int renderPassesPerDisplayUpdate = getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Range range = Range.create(resolutionX * resolutionY);
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing.compareAndSet(true, false)) {
				setRenderPass(0);
				
				filmClear();
				
				if(image instanceof PixelImage) {
					final
					PixelImage pixelImage = PixelImage.class.cast(image);
					pixelImage.filmClear();
					pixelImage.filmRender();
				}
				
				rendererObserver.onRenderDisplay(this, image);
				
				timer.restart();
			} else {
				filmClearFilmFlags();
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 0.0D);
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			execute(range);
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
//			Test if this may prevent the JVM from crashing.
			if(!this.isRendering.get()) {
				return false;
			}
			
			if(image instanceof ByteImage) {
				final ByteImage byteImage = ByteImage.class.cast(image);
				
				final byte[] bytes = byteImage.getBytes(true);
				final byte[] imageColorByteArray = getImageColorByteArray();
				
				System.arraycopy(imageColorByteArray, 0, bytes, 0, bytes.length);
			} else if(image instanceof PixelImage) {
				final PixelImage pixelImage = PixelImage.class.cast(image);
				
				final float[] imageColorFloatArray = getImageColorFloatArray();
				final float[] pixelArray = getPixelArray();
				
				for(int y = 0; y < resolutionY; y++) {
					for(int x = 0; x < resolutionX; x++) {
						final int index = y * resolutionX + x;
						final int indexPixelArray = index * 2;
						final int indexRadianceRGBFloatArray = index * 3;
						
						final float r = imageColorFloatArray[indexRadianceRGBFloatArray + 0];
						final float g = imageColorFloatArray[indexRadianceRGBFloatArray + 1];
						final float b = imageColorFloatArray[indexRadianceRGBFloatArray + 2];
						
						final float imageX = x;
						final float imageY = y;
						final float pixelX = pixelArray[indexPixelArray + 0];
						final float pixelY = pixelArray[indexPixelArray + 1];
						
						final Color3F colorRGB = new Color3F(r, g, b);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						if(!colorXYZ.hasInfinites() && !colorXYZ.hasNaNs()) {
							pixelImage.filmAddColorXYZ(imageX + pixelX, imageY + pixelY, colorXYZ);
						}
					}
				}
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 1.0D);
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				if(image instanceof PixelImage) {
					final
					PixelImage pixelImage = PixelImage.class.cast(image);
					pixelImage.filmRender();
				}
				
				rendererObserver.onRenderDisplay(this, image);
			}
			
			setRenderPass(getRenderPass() + 1);
			
			rendererObserver.onRenderPassComplete(this, renderPass, renderPasses, elapsedTimeMillis);
		}
		
		this.isRendering.set(false);
		
		return true;
	}
	
	/**
	 * Attempts to shutdown the rendering process of this {@code AbstractGPURenderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise
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
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Sets the {@link Image} instance associated with this {@code AbstractGPURenderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code Image} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public final void setImage(final Image image) {
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
	 * Sets the preview mode state associated with this {@code AbstractGPURenderer} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code AbstractGPURenderer} instance
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
	 * Sets the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererObserver}.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	@Override
	public final void setRendererObserver(final RendererObserver rendererObserver) {
		this.rendererObserver.set(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	/**
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	@Override
	public final void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm) {
		this.renderingAlgorithm = Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null");
	}
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code AbstractGPURenderer} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	@Override
	public final void setSampler(final Sampler sampler) {
		this.sampler = Objects.requireNonNull(sampler, "sampler == null");
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
	 * Sets the {@link Timer} instance associated with this {@code AbstractGPURenderer} instance to {@code timer}.
	 * <p>
	 * If {@code timer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param timer the {@code Timer} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code timer} is {@code null}
	 */
	@Override
	public final void setTimer(final Timer timer) {
		this.timer = Objects.requireNonNull(timer, "timer == null");
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public void setup() {
		final Image image = getImage();
		
		final Scene scene = getScene();
		
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		setResolution(resolutionX, resolutionY);
		setScene(scene);
		
		super.setup();
	}
}