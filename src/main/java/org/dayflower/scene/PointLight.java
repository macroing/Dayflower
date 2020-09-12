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

import org.dayflower.geometry.Point3F;
import org.dayflower.image.Color3F;

/**
 * A {@code PointLight} is an implementation of {@link Light} that represents a point light.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PointLight implements Light {
	private final Color3F emittance;
	private final Point3F position;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(new Point3F());
	 * }
	 * </pre>
	 */
	public PointLight() {
		this(new Point3F());
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If {@code position} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PointLight(position, Color3F.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, {@code position} is {@code null}
	 */
	public PointLight(final Point3F position) {
		this(position, Color3F.WHITE);
	}
	
	/**
	 * Constructs a new {@code PointLight} instance.
	 * <p>
	 * If either {@code position} or {@code emittance} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param position a {@link Point3F} instance with the position associated with this {@code PointLight} instance
	 * @param emittance a {@link Color3F} instance with the emittance associated with this {@code PointLight} instance
	 * @throws NullPointerException thrown if, and only if, either {@code position} or {@code emittance} are {@code null}
	 */
	public PointLight(final Point3F position, final Color3F emittance) {
		this.position = Objects.requireNonNull(position, "position == null");
		this.emittance = Objects.requireNonNull(emittance, "emittance == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the emittance associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Color3F} instance with the emittance associated with this {@code PointLight} instance
	 */
	public Color3F getEmittance() {
		return this.emittance;
	}
	
	/**
	 * Returns a {@link Point3F} instance with the position associated with this {@code PointLight} instance.
	 * 
	 * @return a {@code Point3F} instance with the position associated with this {@code PointLight} instance
	 */
	public Point3F getPosition() {
		return this.position;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PointLight} instance.
	 * 
	 * @return a {@code String} representation of this {@code PointLight} instance
	 */
	@Override
	public String toString() {
		return String.format("new PointLight(%s, %s)", this.position, this.emittance);
	}
	
	/**
	 * Compares {@code object} to this {@code PointLight} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PointLight}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PointLight} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PointLight}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PointLight)) {
			return false;
		} else if(!Objects.equals(this.emittance, PointLight.class.cast(object).emittance)) {
			return false;
		} else if(!Objects.equals(this.position, PointLight.class.cast(object).position)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PointLight} instance.
	 * 
	 * @return a hash code for this {@code PointLight} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.emittance, this.position);
	}
}