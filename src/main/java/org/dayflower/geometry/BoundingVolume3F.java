/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.macroing.java.lang.Floats;

/**
 * A {@code BoundingVolume3F} is a 3-dimensional extension of {@link BoundingVolume} that adds additional methods that operates on {@code float}-based data types.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface BoundingVolume3F extends BoundingVolume {
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code BoundingVolume3F} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code BoundingVolume3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	BoundingVolume3F transform(final Matrix44F matrix);
	
	/**
	 * Returns a {@link Point3F} instance that represents the closest point to {@code point} and is contained in this {@code BoundingVolume3F} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3F} instance
	 * @return a {@code Point3F} instance that represents the closest point to {@code point} and is contained in this {@code BoundingVolume3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	Point3F getClosestPointTo(final Point3F point);
	
	/**
	 * Returns a {@link Point3F} with the largest component values needed to contain this {@code BoundingVolume3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values needed to contain this {@code BoundingVolume3F} instance
	 */
	Point3F getMaximum();
	
	/**
	 * Returns a {@link Point3F} with the smallest component values needed to contain this {@code BoundingVolume3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values needed to contain this {@code BoundingVolume3F} instance
	 */
	Point3F getMinimum();
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code BoundingVolume3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code BoundingVolume3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	boolean contains(final Point3F point);
	
	/**
	 * Returns the surface area of this {@code BoundingVolume3F} instance.
	 * 
	 * @return the surface area of this {@code BoundingVolume3F} instance
	 */
	float getSurfaceArea();
	
	/**
	 * Returns the volume of this {@code BoundingVolume3F} instance.
	 * 
	 * @return the volume of this {@code BoundingVolume3F} instance
	 */
	float getVolume();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BoundingVolume3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code BoundingVolume3F} instance, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BoundingVolume3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code BoundingVolume3F} instance, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	float intersection(final Ray3F ray, final float tMinimum, final float tMaximum);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Point3F} with the component values in the middle of this {@code BoundingVolume3F} instance.
	 * 
	 * @return a {@code Point3F} with the component values in the middle of this {@code BoundingVolume3F} instance
	 */
	default Point3F getMidpoint() {
		return Point3F.midpoint(getMaximum(), getMinimum());
	}
	
	/**
	 * Performs an intersection test between {@code boundingVolume} and this {@code BoundingVolume3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code boundingVolume} intersects this {@code BoundingVolume3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code boundingVolume} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume the {@code BoundingVolume3F} to perform an intersection test against this {@code BoundingVolume3F} instance
	 * @return {@code true} if, and only if, {@code boundingVolume} intersects this {@code BoundingVolume3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume} is {@code null}
	 */
	default boolean intersects(final BoundingVolume3F boundingVolume) {
		return contains(boundingVolume.getClosestPointTo(getMidpoint()));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BoundingVolume3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code BoundingVolume3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BoundingVolume3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code BoundingVolume3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	default boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return !Floats.isNaN(intersection(ray, tMinimum, tMaximum));
	}
}