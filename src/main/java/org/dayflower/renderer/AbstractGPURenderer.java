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
import org.dayflower.sampler.Sampler;
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
	
	private Display display;
	private Image image;
	private RendererConfiguration rendererConfiguration;
	private Sampler sampler;
	private Scene scene;
	private Timer timer;
	private boolean isClearing;
	private int renderPass;
	private long renderTime;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code AbstractGPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code AbstractGPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @param sampler the {@link Sampler} instance associated with this {@code AbstractGPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}
	 */
	protected AbstractGPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Sampler sampler, final Scene scene) {
		this.display = Objects.requireNonNull(display, "display == null");
		this.image = Objects.requireNonNull(image, "image == null");
		this.rendererConfiguration = Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null");
		this.sampler = Objects.requireNonNull(sampler, "sampler == null");
		this.scene = Objects.requireNonNull(scene, "scene == null");
		this.timer = new Timer();
		this.isClearing = false;
		this.renderPass = 0;
		this.renderTime = 0L;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Display} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Display} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Display getDisplay() {
		return this.display;
	}
	
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
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration;
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
	 * Returns the {@link Scene} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Scene} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Scene getScene() {
		return this.scene;
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
		final Display display = this.display;
		
		final Image image = this.image;
		
		final RendererConfiguration rendererConfiguration = this.rendererConfiguration;
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing) {
				this.isClearing = false;
				this.renderPass = 0;
				this.renderTime = 0L;
				
				image.filmClear();
				image.filmRender();
				
				display.update(image);
				
				this.timer.restart();
			}
			
			final long currentTimeMillis = System.currentTimeMillis();
			
//			TODO: Implement rendering via Kernel!
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender();
				
				display.update(image);
			}
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			this.renderPass++;
			this.renderTime = elapsedTimeMillis;
		}
		
		return true;
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
	 * Returns the current render time in milliseconds.
	 * 
	 * @return the current render time in milliseconds
	 */
	@Override
	public final long getRenderTime() {
		return this.renderTime;
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
	 * Sets the {@link Display} instance associated with this {@code AbstractGPURenderer} instance to {@code display}.
	 * <p>
	 * If {@code display} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@code Display} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code display} is {@code null}
	 */
	@Override
	public final void setDisplay(final Display display) {
		this.display = Objects.requireNonNull(display, "display == null");
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
	 * Sets the {@link Scene} instance associated with this {@code AbstractGPURenderer} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@code Scene} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	@Override
	public final void setScene(final Scene scene) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
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
		this.seeds = new long[this.image.getResolution()];
		
		for(int i = 0; i < this.seeds.length; i++) {
			this.seeds[i] = ThreadLocalRandom.current().nextLong();
		}
		
		put(this.seeds);
	}
}