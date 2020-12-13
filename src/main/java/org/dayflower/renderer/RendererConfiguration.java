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

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.image.Image;
import org.dayflower.image.PixelImage;
import org.dayflower.sampler.NRooksSampler;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Scene;
import org.dayflower.util.Timer;

/**
 * A {@code RendererConfiguration} is used to configure the rendering process of a {@link Renderer} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RendererConfiguration {
	private Image image;
	private RenderingAlgorithm renderingAlgorithm;
	private Sampler sampler;
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
	private long renderTime;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RendererConfiguration} instance.
	 */
	public RendererConfiguration() {
		this.image = new PixelImage(800, 800);
		this.renderingAlgorithm = RenderingAlgorithm.PATH_TRACING;
		this.sampler = new NRooksSampler();
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
		this.renderTime = 0L;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Image} instance associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the {@code Image} instance associated with this {@code RendererConfiguration} instance
	 */
	public Image getImage() {
		return this.image;
	}
	
	/**
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code RendererConfiguration} instance
	 */
	public RenderingAlgorithm getRenderingAlgorithm() {
		return this.renderingAlgorithm;
	}
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code RendererConfiguration} instance
	 */
	public Sampler getSampler() {
		return this.sampler;
	}
	
	/**
	 * Returns the {@link Scene} instance associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the {@code Scene} instance associated with this {@code RendererConfiguration} instance
	 */
	public Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RendererConfiguration} instance.
	 * 
	 * @return a {@code String} representation of this {@code RendererConfiguration} instance
	 */
	@Override
	public String toString() {
		return "new RendererConfiguration()";
	}
	
	/**
	 * Returns the {@link Timer} instance associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code RendererConfiguration} instance
	 */
	public Timer getTimer() {
		return this.timer;
	}
	
	/**
	 * Compares {@code object} to this {@code RendererConfiguration} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RendererConfiguration}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RendererConfiguration} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RendererConfiguration}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RendererConfiguration)) {
			return false;
		} else if(!Objects.equals(this.image, RendererConfiguration.class.cast(object).image)) {
			return false;
		} else if(!Objects.equals(this.renderingAlgorithm, RendererConfiguration.class.cast(object).renderingAlgorithm)) {
			return false;
		} else if(!Objects.equals(this.sampler, RendererConfiguration.class.cast(object).sampler)) {
			return false;
		} else if(!Objects.equals(this.scene, RendererConfiguration.class.cast(object).scene)) {
			return false;
		} else if(!equal(this.maximumDistance, RendererConfiguration.class.cast(object).maximumDistance)) {
			return false;
		} else if(this.maximumBounce != RendererConfiguration.class.cast(object).maximumBounce) {
			return false;
		} else if(this.minimumBounceRussianRoulette != RendererConfiguration.class.cast(object).minimumBounceRussianRoulette) {
			return false;
		} else if(this.renderPass != RendererConfiguration.class.cast(object).renderPass) {
			return false;
		} else if(this.renderPasses != RendererConfiguration.class.cast(object).renderPasses) {
			return false;
		} else if(this.renderPassesPerDisplayUpdate != RendererConfiguration.class.cast(object).renderPassesPerDisplayUpdate) {
			return false;
		} else if(this.samples != RendererConfiguration.class.cast(object).samples) {
			return false;
		} else if(this.renderTime != RendererConfiguration.class.cast(object).renderTime) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the preview mode state associated with this {@code RendererConfiguration} instance.
	 * 
	 * @return the preview mode state associated with this {@code RendererConfiguration} instance
	 */
	public boolean isPreviewMode() {
		return this.isPreviewMode;
	}
	
	/**
	 * Returns the maximum distance.
	 * 
	 * @return the maximum distance
	 */
	public float getMaximumDistance() {
		return this.maximumDistance;
	}
	
	/**
	 * Returns the maximum bounce.
	 * 
	 * @return the maximum bounce
	 */
	public int getMaximumBounce() {
		return this.maximumBounce;
	}
	
	/**
	 * Returns the minimum bounce before Russian roulette termination occurs.
	 * 
	 * @return the minimum bounce before Russian roulette termination occurs
	 */
	public int getMinimumBounceRussianRoulette() {
		return this.minimumBounceRussianRoulette;
	}
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	public int getRenderPass() {
		return this.renderPass;
	}
	
	/**
	 * Returns the render passes to perform.
	 * 
	 * @return the render passes to perform
	 */
	public int getRenderPasses() {
		return this.renderPasses;
	}
	
	/**
	 * Returns the render passes to perform before the display is updated.
	 * 
	 * @return the render passes to perform before the display is updated
	 */
	public int getRenderPassesPerDisplayUpdate() {
		return this.renderPassesPerDisplayUpdate;
	}
	
	/**
	 * Returns the samples to use per render pass.
	 * 
	 * @return the samples to use per render pass
	 */
	public int getSamples() {
		return this.samples;
	}
	
	/**
	 * Returns a hash code for this {@code RendererConfiguration} instance.
	 * 
	 * @return a hash code for this {@code RendererConfiguration} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.image, this.renderingAlgorithm, this.sampler, this.scene, Float.valueOf(this.maximumDistance), Integer.valueOf(this.maximumBounce), Integer.valueOf(this.minimumBounceRussianRoulette), Integer.valueOf(this.renderPass), Integer.valueOf(this.renderPasses), Integer.valueOf(this.renderPassesPerDisplayUpdate), Integer.valueOf(this.samples), Long.valueOf(this.renderTime));
	}
	
	/**
	 * Returns the current render time in milliseconds.
	 * 
	 * @return the current render time in milliseconds
	 */
	public long getRenderTime() {
		return this.renderTime;
	}
	
	/**
	 * Sets the {@link Image} instance associated with this {@code RendererConfiguration} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code Image} instance associated with this {@code RendererConfiguration} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public void setImage(final Image image) {
		this.image = Objects.requireNonNull(image, "image == null");
	}
	
	/**
	 * Sets the maximum bounce to {@code maximumBounce}.
	 * 
	 * @param maximumBounce the maximum bounce
	 */
	public void setMaximumBounce(final int maximumBounce) {
		this.maximumBounce = maximumBounce;
	}
	
	/**
	 * Sets the maximum distance to {@code maximumDistance}.
	 * 
	 * @param maximumDistance the maximum distance
	 */
	public void setMaximumDistance(final float maximumDistance) {
		this.maximumDistance = maximumDistance;
	}
	
	/**
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	public void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette) {
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
	}
	
	/**
	 * Sets the preview mode state associated with this {@code RendererConfiguration} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code RendererConfiguration} instance
	 */
	public void setPreviewMode(final boolean isPreviewMode) {
		this.isPreviewMode = isPreviewMode;
	}
	
	/**
	 * Sets the current render pass to {@code renderPass}.
	 * 
	 * @param renderPass the current render pass
	 */
	public void setRenderPass(final int renderPass) {
		this.renderPass = renderPass;
	}
	
	/**
	 * Sets the render passes to perform to {@code renderPasses}.
	 * 
	 * @param renderPasses the render passes to perform
	 */
	public void setRenderPasses(final int renderPasses) {
		this.renderPasses = renderPasses;
	}
	
	/**
	 * Sets the render passes to perform before the display is updated to {@code renderPassesPerDisplayUpdate}.
	 * 
	 * @param renderPassesPerDisplayUpdate the render passes to perform before the display is updated
	 */
	public void setRenderPassesPerDisplayUpdate(final int renderPassesPerDisplayUpdate) {
		this.renderPassesPerDisplayUpdate = renderPassesPerDisplayUpdate;
	}
	
	/**
	 * Sets the current render time in milliseconds to {@code renderTime}.
	 * 
	 * @param renderTime the current render time in milliseconds
	 */
	public void setRenderTime(final long renderTime) {
		this.renderTime = renderTime;
	}
	
	/**
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code RendererConfiguration} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code RendererConfiguration} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	public void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm) {
		this.renderingAlgorithm = Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null");
	}
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code RendererConfiguration} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code RendererConfiguration} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	public void setSampler(final Sampler sampler) {
		this.sampler = Objects.requireNonNull(sampler, "sampler == null");
	}
	
	/**
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	public void setSamples(final int samples) {
		this.samples = samples;
	}
	
	/**
	 * Sets the {@link Scene} instance associated with this {@code RendererConfiguration} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@code Scene} instance associated with this {@code RendererConfiguration} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	public void setScene(final Scene scene) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
	}
	
	/**
	 * Sets the {@link Timer} instance associated with this {@code RendererConfiguration} instance to {@code timer}.
	 * <p>
	 * If {@code timer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param timer the {@code Timer} instance associated with this {@code RendererConfiguration} instance
	 * @throws NullPointerException thrown if, and only if, {@code timer} is {@code null}
	 */
	public void setTimer(final Timer timer) {
		this.timer = Objects.requireNonNull(timer, "timer == null");
	}
}