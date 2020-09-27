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

import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;

/**
 * A {@code BackgroundSample} represents a sample produced by a {@link Background} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BackgroundSample {
	private final Color3F radiance;
	private final Ray3F ray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BackgroundSample} instance.
	 * <p>
	 * If either {@code radiance} or {@code ray} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radiance a {@link Color3F} instance representing the radiance associated with this {@code BackgroundSample} instance
	 * @param ray a {@link Ray3F} instance representing the ray associated with this {@code BackgroundSample} instance
	 * @throws NullPointerException thrown if, and only if, either {@code radiance} or {@code ray} are {@code null}
	 */
	public BackgroundSample(final Color3F radiance, final Ray3F ray) {
		this.radiance = Objects.requireNonNull(radiance, "radiance == null");
		this.ray = Objects.requireNonNull(ray, "ray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance representing the radiance associated with this {@code BackgroundSample} instance.
	 * 
	 * @return a {@code Color3F} instance representing the radiance associated with this {@code BackgroundSample} instance
	 */
	public Color3F getRadiance() {
		return this.radiance;
	}
	
	/**
	 * Returns a {@link Ray3F} instance representing the ray associated with this {@code BackgroundSample} instance.
	 * 
	 * @return a {@code Ray3F} instance representing the ray associated with this {@code BackgroundSample} instance
	 */
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code BackgroundSample} instance.
	 * 
	 * @return a {@code String} representation of this {@code BackgroundSample} instance
	 */
	@Override
	public String toString() {
		return String.format("new BackgroundSample(%s, %s)", this.radiance, this.ray);
	}
	
	/**
	 * Compares {@code object} to this {@code BackgroundSample} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BackgroundSample}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BackgroundSample} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BackgroundSample}, and their respective values are equal, {@code false} otherwise
	 */
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
	
	/**
	 * Returns a hash code for this {@code BackgroundSample} instance.
	 * 
	 * @return a hash code for this {@code BackgroundSample} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.radiance, this.ray);
	}
}