/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.geometry.boundingvolume;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;

/**
 * An {@code InfiniteBoundingVolume3F} is an implementation of {@link BoundingVolume3F} that represents an infinite bounding volume.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class InfiniteBoundingVolume3F implements BoundingVolume3F {
	/**
	 * The ID of this {@code InfiniteBoundingVolume3F} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code InfiniteBoundingVolume3F} instance.
	 */
	public InfiniteBoundingVolume3F() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return this {@code InfiniteBoundingVolume3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public InfiniteBoundingVolume3F transform(final Matrix44F matrix) {
		Objects.requireNonNull(matrix, "matrix == null");
		
		return this;
	}
	
	/**
	 * Returns a {@link Point3F} instance that represents the closest point to {@code point} and is contained in this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will return {@code point}.
	 * 
	 * @param point a {@code Point3F} instance
	 * @return a {@code Point3F} instance that represents the closest point to {@code point} and is contained in this {@code InfiniteBoundingVolume3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3F getClosestPointTo(final Point3F point) {
		return Objects.requireNonNull(point, "point == null");
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values needed to contain this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * This implementation will return {@code new Point3F(Float.POSITIVE_INFINITY)}.
	 * 
	 * @return a {@code Point3F} with the largest component values needed to contain this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public Point3F getMaximum() {
		return new Point3F(Float.POSITIVE_INFINITY);
	}
	
	/**
	 * Returns a {@link Point3F} with the component values in the middle of this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * This implementation will return {@code new Point3F()}.
	 * 
	 * @return a {@code Point3F} with the component values in the middle of this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public Point3F getMidpoint() {
		return new Point3F();
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values needed to contain this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * This implementation will return {@code new Point3F(Float.NEGATIVE_INFINITY)}.
	 * 
	 * @return a {@code Point3F} with the smallest component values needed to contain this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public Point3F getMinimum() {
		return new Point3F(Float.NEGATIVE_INFINITY);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code InfiniteBoundingVolume3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public String toString() {
		return "new InfiniteBoundingVolume3F()";
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code InfiniteBoundingVolume3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will always return {@code true}.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code InfiniteBoundingVolume3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3F point) {
		Objects.requireNonNull(point, "point == null");
		
		return true;
	}
	
	/**
	 * Compares {@code object} to this {@code InfiniteBoundingVolume3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code InfiniteBoundingVolume3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code InfiniteBoundingVolume3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code InfiniteBoundingVolume3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof InfiniteBoundingVolume3F)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * This implementation will return {@code Float.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Returns the volume of this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * This implementation will return {@code Float.POSITIVE_INFINITY}.
	 * 
	 * @return the volume of this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public float getVolume() {
		return Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code InfiniteBoundingVolume3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code InfiniteBoundingVolume3F} instance, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will return {@code tMinimum}.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code InfiniteBoundingVolume3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code InfiniteBoundingVolume3F} instance, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
		return tMinimum;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code InfiniteBoundingVolume3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code InfiniteBoundingVolume3F} instance.
	 * 
	 * @return a hash code for this {@code InfiniteBoundingVolume3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
	
	/**
	 * Writes this {@code InfiniteBoundingVolume3F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}