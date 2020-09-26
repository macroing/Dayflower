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
package org.dayflower.scene;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;

//TODO: Add Javadocs!
public final class BackgroundSample {
	private final Color3F radiance;
	private final Ray3F ray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public BackgroundSample(final Color3F radiance, final Ray3F ray) {
		this.radiance = Objects.requireNonNull(radiance, "radiance == null");
		this.ray = Objects.requireNonNull(ray, "ray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getRadiance() {
		return this.radiance;
	}
	
//	TODO: Add Javadocs!
	public Ray3F getRay() {
		return this.ray;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new BackgroundSample(%s, %s)", this.radiance, this.ray);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BackgroundSample)) {
			return false;
		} else if(!Objects.equals(this.radiance, BackgroundSample.class.cast(object).radiance)) {
			return false;
		} else if(!Objects.equals(this.ray, BackgroundSample.class.cast(object).ray)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.radiance, this.ray);
	}
}