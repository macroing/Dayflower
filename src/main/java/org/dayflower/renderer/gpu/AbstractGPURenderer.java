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
package org.dayflower.renderer.gpu;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

import com.amd.aparapi.Kernel;

/**
 * An {@code AbstractGPURenderer} is an abstract implementation of {@link Renderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGPURenderer extends Kernel implements Renderer {
	private static final float PRNG_NEXT_FLOAT_RECIPROCAL = 1.0F / (1 << 24);
	private static final long PRNG_ADDEND = 0xBL;
	private static final long PRNG_MASK = (1L << 48L) - 1L;
	private static final long PRNG_MULTIPLIER = 0x5DEECE66DL;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected long[] seeds;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererConfiguration> rendererConfiguration;
	private final AtomicReference<RendererObserver> rendererObserver;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}
	 */
	protected AbstractGPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererConfiguration = new AtomicReference<>(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration.get();
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
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
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
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		
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
			
//			TODO: Implement rendering via Kernel!
			
			if(!this.isRendering.get()) {
				return false;
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
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Disposes of any resources created by this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public final synchronized void dispose() {
		super.dispose();
	}
	
	/**
	 * Sets the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererConfiguration}.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	@Override
	public final void setRendererConfiguration(final RendererConfiguration rendererConfiguration) {
		this.rendererConfiguration.set(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
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
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public final void setup() {
		doSetupSeeds();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive).
	 * 
	 * @return a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive)
	 */
	protected final float random() {
		return doNext(24) * PRNG_NEXT_FLOAT_RECIPROCAL;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int doNext(final int bits) {
		final int index = getGlobalId();
		
		final long oldSeed = this.seeds[index];
		final long newSeed = (oldSeed * PRNG_MULTIPLIER + PRNG_ADDEND) & PRNG_MASK;
		
		this.seeds[index] = newSeed;
		
		return (int)(newSeed >>> (48 - bits));
	}
	
	private void doSetupSeeds() {
		this.seeds = new long[this.rendererConfiguration.get().getImage().getResolution()];
		
		for(int i = 0; i < this.seeds.length; i++) {
			this.seeds[i] = ThreadLocalRandom.current().nextLong();
		}
		
		put(this.seeds);
	}
}