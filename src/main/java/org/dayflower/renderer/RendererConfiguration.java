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

/**
 * A {@code RendererConfiguration} is used to configure the rendering process of a {@link Renderer} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RendererConfiguration {
	private int maximumBounce;
	private int minimumBounceRussianRoulette;
	private int renderPasses;
	private int renderPassesPerDisplayUpdate;
	private int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RendererConfiguration} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RendererConfiguration(20, 5, 1000, 10, 10);
	 * }
	 * </pre>
	 */
	public RendererConfiguration() {
		this(20, 5, 1000, 10, 10);
	}
	
	/**
	 * Constructs a new {@code RendererConfiguration} instance.
	 * 
	 * @param maximumBounce the maximum bounce
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 * @param renderPasses the render passes to perform
	 * @param renderPassesPerDisplayUpdate the render passes to perform before the display is updated
	 * @param samples the samples to use per render pass
	 */
	public RendererConfiguration(final int maximumBounce, final int minimumBounceRussianRoulette, final int renderPasses, final int renderPassesPerDisplayUpdate, final int samples) {
		this.maximumBounce = maximumBounce;
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
		this.renderPasses = renderPasses;
		this.renderPassesPerDisplayUpdate = renderPassesPerDisplayUpdate;
		this.samples = samples;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code RendererConfiguration} instance.
	 * 
	 * @return a {@code String} representation of this {@code RendererConfiguration} instance
	 */
	@Override
	public String toString() {
		return String.format("new RendererConfiguration(%d, %d, %d, %d, %d)", Integer.valueOf(this.maximumBounce), Integer.valueOf(this.minimumBounceRussianRoulette), Integer.valueOf(this.renderPasses), Integer.valueOf(this.renderPassesPerDisplayUpdate), Integer.valueOf(this.samples));
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
		} else if(this.maximumBounce != RendererConfiguration.class.cast(object).maximumBounce) {
			return false;
		} else if(this.minimumBounceRussianRoulette != RendererConfiguration.class.cast(object).minimumBounceRussianRoulette) {
			return false;
		} else if(this.renderPasses != RendererConfiguration.class.cast(object).renderPasses) {
			return false;
		} else if(this.renderPassesPerDisplayUpdate != RendererConfiguration.class.cast(object).renderPassesPerDisplayUpdate) {
			return false;
		} else if(this.samples != RendererConfiguration.class.cast(object).samples) {
			return false;
		} else {
			return true;
		}
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
		return Objects.hash(Integer.valueOf(this.maximumBounce), Integer.valueOf(this.minimumBounceRussianRoulette), Integer.valueOf(this.renderPasses), Integer.valueOf(this.renderPassesPerDisplayUpdate), Integer.valueOf(this.samples));
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
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	public void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette) {
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
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
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	public void setSamples(final int samples) {
		this.samples = samples;
	}
}