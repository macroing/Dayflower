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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
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
	 * Constructs a new {@code AbstractCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code AbstractCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code AbstractCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance
	 * @param sampler the {@link Sampler} instance associated with this {@code AbstractCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}
	 */
	protected AbstractCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Sampler sampler, final Scene scene) {
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
	 * Returns the {@link Display} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code Display} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final Display getDisplay() {
		return this.display;
	}
	
	/**
	 * Returns the {@link Image} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code Image} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final Image getImage() {
		return this.image;
	}
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration;
	}
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code AbstractCPURenderer} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code AbstractCPURenderer} instance
	 */
	@Override
	public final Sampler getSampler() {
		return this.sampler;
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
	 * Returns {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractCPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
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
		
		final Sampler sampler = this.sampler;
		
		final Scene scene = this.scene;
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Camera camera = scene.getCameraCopy();
		
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
				
//				System.out.printf("%d/%d%n", Integer.valueOf((y + 1) * resolutionX), Integer.valueOf(resolutionX * resolutionY));
			}
			
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
	 * Disposes of any resources created by this {@code AbstractCPURenderer} instance.
	 */
	@Override
	public final void dispose() {
//		Do nothing!
	}
	
	/**
	 * Sets the {@link Display} instance associated with this {@code AbstractCPURenderer} instance to {@code display}.
	 * <p>
	 * If {@code display} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@code Display} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code display} is {@code null}
	 */
	@Override
	public final void setDisplay(final Display display) {
		this.display = Objects.requireNonNull(display, "display == null");
	}
	
	/**
	 * Sets the {@link Image} instance associated with this {@code AbstractCPURenderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code Image} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public final void setImage(final Image image) {
		this.image = Objects.requireNonNull(image, "image == null");
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
		this.rendererConfiguration = Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null");
	}
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code AbstractCPURenderer} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code AbstractCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	@Override
	public final void setSampler(final Sampler sampler) {
		this.sampler = Objects.requireNonNull(sampler, "sampler == null");
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