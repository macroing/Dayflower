/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;

/**
 * An {@code InfiniteBoundingVolume3D} is an implementation of {@link BoundingVolume3D} that represents an infinite bounding volume.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class InfiniteBoundingVolume3D implements BoundingVolume3D {
	/**
	 * The ID of this {@code InfiniteBoundingVolume3D} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code InfiniteBoundingVolume3D} instance.
	 */
	public InfiniteBoundingVolume3D() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @return this {@code InfiniteBoundingVolume3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public BoundingVolume3D transform(final Matrix44D matrix) {
		Objects.requireNonNull(matrix, "matrix == null");
		
		return this;
	}
	
	/**
	 * Returns a {@link Point3D} instance that represents the closest point to {@code point} and is contained in this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will return {@code point}.
	 * 
	 * @param point a {@code Point3D} instance
	 * @return a {@code Point3D} instance that represents the closest point to {@code point} and is contained in this {@code InfiniteBoundingVolume3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3D getClosestPointTo(final Point3D point) {
		return Objects.requireNonNull(point, "point == null");
	}
	
	/**
	 * Returns a {@link Point3D} with the largest component values that are contained in this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * This implementation will return {@code new Point3D(Double.POSITIVE_INFINITY)}.
	 * 
	 * @return a {@code Point3D} with the largest component values that are contained in this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public Point3D getMaximum() {
		return new Point3D(Double.POSITIVE_INFINITY);
	}
	
	/**
	 * Returns a {@link Point3D} with the smallest component values that are contained in this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * This implementation will return {@code new Point3D(Double.NEGATIVE_INFINITY)}.
	 * 
	 * @return a {@code Point3D} with the smallest component values that are contained in this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public Point3D getMinimum() {
		return new Point3D(Double.NEGATIVE_INFINITY);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code InfiniteBoundingVolume3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public String toString() {
		return "new InfiniteBoundingVolume3D()";
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code InfiniteBoundingVolume3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will always return {@code true}.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code InfiniteBoundingVolume3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3D point) {
		Objects.requireNonNull(point, "point == null");
		
		return true;
	}
	
	/**
	 * Compares {@code object} to this {@code InfiniteBoundingVolume3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code InfiniteBoundingVolume3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code InfiniteBoundingVolume3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code InfiniteBoundingVolume3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof InfiniteBoundingVolume3D)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * This implementation will return {@code Double.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Returns the volume of this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * This implementation will return {@code Double.POSITIVE_INFINITY}.
	 * 
	 * @return the volume of this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public double getVolume() {
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code InfiniteBoundingVolume3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code InfiniteBoundingVolume3D} instance, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This implementation will return {@code tMinimum}.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code InfiniteBoundingVolume3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code InfiniteBoundingVolume3D} instance, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		Objects.requireNonNull(ray, "ray == null");
		
		return tMinimum;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code InfiniteBoundingVolume3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code InfiniteBoundingVolume3D} instance.
	 * 
	 * @return a hash code for this {@code InfiniteBoundingVolume3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
	
	/**
	 * Writes this {@code InfiniteBoundingVolume3D} instance to {@code dataOutput}.
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