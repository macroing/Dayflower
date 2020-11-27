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
package org.dayflower.renderer.cpu;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

/**
 * An {@code AbstractCPURenderer} is an abstract implementation of {@link Renderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractCPURenderer implements Renderer {
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererConfiguration> rendererConfiguration;
	private final AtomicReference<RendererObserver> rendererObserver;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractCPURenderer} instance.
	 * <p>
	 * If either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}
	 */
	protected AbstractCPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererConfiguration = new AtomicReference<>(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration.get();
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
	 * Returns {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
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
		
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final Image image = rendererConfiguration.getImage();
		
		final Sampler sampler = rendererConfiguration.getSampler();
		
		final Scene scene = rendererConfiguration.getScene();
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Camera camera = scene.getCameraCopy();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing.compareAndSet(true, false)) {
				rendererConfiguration.setRenderPass(0);
				rendererConfiguration.setRenderTime(0L);
				
				image.filmClear();
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
				
				timer.restart();
			}
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final Sample2F sample = sampler.sample2();
					
					final Optional<Ray3F> optionalRay = camera.createPrimaryRay(x, y, sample.getX(), sample.getY());
					
					if(optionalRay.isPresent()) {
						final Ray3F ray = optionalRay.get();
						
						final Color3F colorRGB = radiance(ray);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						if(!colorXYZ.hasInfinites() && !colorXYZ.hasNaNs()) {
							image.filmAddColorXYZ(x + sample.getX(), y + sample.getY(), colorXYZ);
						}
					}
				}
				
				if(!this.isRendering.get()) {
					return false;
				}
				
				final double percent = ((y + 1.0D) * resolutionX) / (resolutionX * resolutionY);
				
				rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, percent);
			}
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
			}
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			rendererConfiguration.setRenderPass(rendererConfiguration.getRenderPass() + 1);
			rendererConfiguration.setRenderTime(elapsedTimeMillis);
			
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
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
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
	 * Sets the {@link RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance to {@code rendererConfiguration}.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@code RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	@Override
	public final void setRendererConfiguration(final RendererConfiguration rendererConfiguration) {
		this.rendererConfiguration.set(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
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