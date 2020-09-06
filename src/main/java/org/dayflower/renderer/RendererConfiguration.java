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

//TODO: Add Javadocs!
public final class RendererConfiguration {
	private int maximumBounce;
	private int minimumBounceRussianRoulette;
	private int renderPasses;
	private int renderPassesPerImageUpdate;
	private int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public RendererConfiguration() {
		setMaximumBounce(20);
		setMinimumBounceRussianRoulette(5);
		setRenderPasses(1000);
		setRenderPassesPerImageUpdate(10);
		setSamples(10);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return "new RendererConfiguration()";
	}
	
//	TODO: Add Javadocs!
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
		} else if(this.renderPassesPerImageUpdate != RendererConfiguration.class.cast(object).renderPassesPerImageUpdate) {
			return false;
		} else if(this.samples != RendererConfiguration.class.cast(object).samples) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public int getMaximumBounce() {
		return this.maximumBounce;
	}
	
//	TODO: Add Javadocs!
	public int getMinimumBounceRussianRoulette() {
		return this.minimumBounceRussianRoulette;
	}
	
//	TODO: Add Javadocs!
	public int getRenderPasses() {
		return this.renderPasses;
	}
	
//	TODO: Add Javadocs!
	public int getRenderPassesPerImageUpdate() {
		return this.renderPassesPerImageUpdate;
	}
	
//	TODO: Add Javadocs!
	public int getSamples() {
		return this.samples;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.maximumBounce), Integer.valueOf(this.minimumBounceRussianRoulette), Integer.valueOf(this.renderPasses), Integer.valueOf(this.renderPassesPerImageUpdate), Integer.valueOf(this.samples));
	}
	
//	TODO: Add Javadocs!
	public void setMaximumBounce(final int maximumBounce) {
		this.maximumBounce = maximumBounce;
	}
	
//	TODO: Add Javadocs!
	public void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette) {
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
	}
	
//	TODO: Add Javadocs!
	public void setRenderPasses(final int renderPasses) {
		this.renderPasses = renderPasses;
	}
	
//	TODO: Add Javadocs!
	public void setRenderPassesPerImageUpdate(final int renderPassesPerImageUpdate) {
		this.renderPassesPerImageUpdate = renderPassesPerImageUpdate;
	}
	
//	TODO: Add Javadocs!
	public void setSamples(final int samples) {
		this.samples = samples;
	}
}