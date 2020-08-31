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
package org.dayflower.geometry;

import java.util.Objects;

/**
 * A {@code Ray3F} denotes a 3-dimensional ray with a point called its origin and a direction vector, both of which are using {@code float} as their type.
 * <p>
 * The 3-dimensional point used by this class is represented by {@link Point3F}. The 3-dimensional vector used by this class is represented by {@link Vector3F}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Ray3F {
	private final Point3F origin;
	private final Vector3F direction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Ray3F} instance given {@code origin} and {@code direction}.
	 * <p>
	 * If either {@code origin} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param origin a {@link Point3F} instance to represent the origin
	 * @param direction a {@link Vector3F} instance to represent the direction
	 */
	public Ray3F(final Point3F origin, final Vector3F direction) {
		this.origin = Objects.requireNonNull(origin, "origin == null");
		this.direction = Objects.requireNonNull(direction, "direction == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Point3F} instance used by this {@code Ray3F} instance to represent its origin.
	 * 
	 * @return the {@code Point3F} instance used by this {@code Ray3F} instance to represent its origin
	 */
	public Point3F getOrigin() {
		return this.origin;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Ray3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Ray3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Ray3F(%s, %s)", this.origin, this.direction);
	}
	
	/**
	 * Returns the {@link Vector3F} instance used by this {@code Ray3F} instance to represent its direction.
	 * 
	 * @return the {@code Vector3F} instance used by this {@code Ray3F} instance to represent its direction
	 */
	public Vector3F getDirection() {
		return this.direction;
	}
	
	/**
	 * Compares {@code object} to this {@code Ray3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Ray3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Ray3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Ray3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Ray3F)) {
			return false;
		} else if(!Objects.equals(this.origin, Ray3F.class.cast(object).origin)) {
			return false;
		} else if(!Objects.equals(this.direction, Ray3F.class.cast(object).direction)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Ray3F} instance.
	 * 
	 * @return a hash code for this {@code Ray3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.origin, this.direction);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Transforms the {@code Ray3F} {@code rRHS} with the {@link Matrix44F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Ray3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code rRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param rRHS a {@code Ray3F} instance
	 * @return a new {@code Ray3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code rRHS} are {@code null}
	 */
	public static Ray3F transform(final Matrix44F mLHS, final Ray3F rRHS) {
		return new Ray3F(Point3F.transform(mLHS, rRHS.origin), Vector3F.transform(mLHS, rRHS.direction));
	}
}