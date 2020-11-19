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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import org.dayflower.display.Display;
import org.dayflower.image.Image;
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
	
	private RendererConfiguration rendererConfiguration;
	private boolean isClearing;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	protected AbstractGPURenderer(final RendererConfiguration rendererConfiguration) {
		this.rendererConfiguration = Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null");
		this.isClearing = false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing;
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
		final RendererConfiguration rendererConfiguration = this.rendererConfiguration;
		
		final Display display = rendererConfiguration.getDisplay();
		
		final Image image = rendererConfiguration.getImage();
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing) {
				this.isClearing = false;
				
				rendererConfiguration.setRenderPass(0);
				rendererConfiguration.setRenderTime(0L);
				
				image.filmClear();
				image.filmRender();
				
				display.update(image);
				
				timer.restart();
			}
			
			final long currentTimeMillis = System.currentTimeMillis();
			
//			TODO: Implement rendering via Kernel!
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender();
				
				display.update(image);
			}
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			rendererConfiguration.setRenderPass(rendererConfiguration.getRenderPass() + 1);
			rendererConfiguration.setRenderTime(elapsedTimeMillis);
		}
		
		return true;
	}
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing = true;
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
		this.rendererConfiguration = Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null");
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
		this.seeds = new long[this.rendererConfiguration.getImage().getResolution()];
		
		for(int i = 0; i < this.seeds.length; i++) {
			this.seeds[i] = ThreadLocalRandom.current().nextLong();
		}
		
		put(this.seeds);
	}
}