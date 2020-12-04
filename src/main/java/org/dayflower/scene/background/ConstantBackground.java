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
package org.dayflower.scene.background;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Intersection;

/**
 * A {@code ConstantBackground} is a {@link Background} implementation that returns a constant {@link Color3F} instance as radiance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstantBackground implements Background {
	private final Color3F color;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstantBackground} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConstantBackground(new Color3F(135, 206, 235));
	 * }
	 * </pre>
	 */
	public ConstantBackground() {
		this(new Color3F(135, 206, 235));
	}
	
	/**
	 * Constructs a new {@code ConstantBackground} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public ConstantBackground(final Color3F color) {
		this.color = Objects.requireNonNull(color, "color == null");
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
	@Override
	public Color3F radiance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return this.color;
	}
	
	/**
	 * Samples this {@code ConstantBackground} instance from {@code intersection}.
	 * <p>
	 * Returns a {@code List} of {@link BackgroundSample} instances.
	 * <p>
	 * If {@code intersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection the {@link Intersection} instance to sample from
	 * @return a {@code List} of {@code BackgroundSample} instances
	 * @throws NullPointerException thrown if, and only if, {@code intersection} is {@code null}
	 */
	@Override
	public List<BackgroundSample> sample(final Intersection intersection) {
		Objects.requireNonNull(intersection, "intersection == null");
		
		return new ArrayList<>();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstantBackground} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstantBackground} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstantBackground(%s)", this.color);
	}
	
	/**
	 * Compares {@code object} to this {@code ConstantBackground} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstantBackground}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstantBackground} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstantBackground}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstantBackground)) {
			return false;
		} else if(!Objects.equals(this.color, ConstantBackground.class.cast(object).color)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ConstantBackground} instance.
	 * 
	 * @return a hash code for this {@code ConstantBackground} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.color);
	}
}