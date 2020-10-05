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
package org.dayflower.scene.light;

import java.util.Objects;

import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Light;

/**
 * A {@code DirectionalLight} is an implementation of {@link Light} that represents a directional light.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DirectionalLight implements Light {
	private final Color3F emittance;
	private final Vector3F direction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(Vector3F.x());
	 * }
	 * </pre>
	 */
	public DirectionalLight() {
		this(Vector3F.x());
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DirectionalLight(direction, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param direction a {@link Vector3F} instance with the direction associated with this {@code DirectionalLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
	public DirectionalLight(final Vector3F direction) {
		this(direction, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code DirectionalLight} instance.
	 * <p>
	 * If either {@code direction} or {@code emittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance with the direction associated with this {@code DirectionalLight} instance
	 * @param emittance a {@link Color3F} instance with the emittance associated with this {@code DirectionalLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code emittance} are {@code null}
	 */
	public DirectionalLight(final Vector3F direction, final Color3F emittance) {
		this.direction = Objects.requireNonNull(direction, "direction == null");
		this.emittance = Objects.requireNonNull(emittance, "emittance == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance for {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the emittance for {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Color3F emittance(final Ray3F ray) {
		Objects.requireNonNull(ray, "ray == null");
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the emittance associated with this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the emittance associated with this {@code DirectionalLight} instance
	 */
	public Color3F getEmittance() {
		return this.emittance;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code DirectionalLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new DirectionalLight(%s, %s)", this.direction, this.emittance);
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the direction associated with this {@code DirectionalLight} instance.
	 * 
	 * @return a {@code Vector3F} instance with the direction associated with this {@code DirectionalLight} instance
	 */
	public Vector3F getDirection() {
		return this.direction;
	}
	
	/**
	 * Compares {@code object} to this {@code DirectionalLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DirectionalLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DirectionalLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DirectionalLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DirectionalLight)) {
			return false;
		} else if(!Objects.equals(this.emittance, DirectionalLight.class.cast(object).emittance)) {
			return false;
		} else if(!Objects.equals(this.direction, DirectionalLight.class.cast(object).direction)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code DirectionalLight} instance.
	 * 
	 * @return a hash code for this {@code DirectionalLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.emittance, this.direction);
	}
}