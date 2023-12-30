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

import java.util.Objects;

/**
 * A {@code Shape2D} is a 2-dimensional extension of {@link Shape} that adds additional methods that operates on {@code double}-based data types.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape2D extends Shape {
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Shape2D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Shape2D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	boolean contains(final Point2D point);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in {@code shapeLHS} but not in {@code shapeRHS}, {@code false} otherwise.
	 * <p>
	 * If either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @param shapeLHS the {@code Shape2D} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2D} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in {@code shapeLHS} but not in {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsDifference(final Point2D point, final Shape2D shapeLHS, final Shape2D shapeRHS) {
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(shapeLHS, "shapeLHS == null");
		Objects.requireNonNull(shapeRHS, "shapeRHS == null");
		
		final boolean containsShapeLHS = shapeLHS.contains(point);
		final boolean containsShapeRHS = shapeRHS.contains(point);
		final boolean containsDifference = containsShapeLHS && !containsShapeRHS;
		
		return containsDifference;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in both {@code shapeLHS} and {@code shapeRHS}, {@code false} otherwise.
	 * <p>
	 * If either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @param shapeLHS the {@code Shape2D} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2D} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in both {@code shapeLHS} and {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsIntersection(final Point2D point, final Shape2D shapeLHS, final Shape2D shapeRHS) {
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(shapeLHS, "shapeLHS == null");
		Objects.requireNonNull(shapeRHS, "shapeRHS == null");
		
		final boolean containsShapeLHS = shapeLHS.contains(point);
		final boolean containsShapeRHS = shapeRHS.contains(point);
		final boolean containsIntersection = containsShapeLHS && containsShapeRHS;
		
		return containsIntersection;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in either {@code shapeLHS} or {@code shapeRHS}, {@code false} otherwise.
	 * <p>
	 * If either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @param shapeLHS the {@code Shape2D} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2D} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in either {@code shapeLHS} or {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsUnion(final Point2D point, final Shape2D shapeLHS, final Shape2D shapeRHS) {
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(shapeLHS, "shapeLHS == null");
		Objects.requireNonNull(shapeRHS, "shapeRHS == null");
		
		final boolean containsShapeLHS = shapeLHS.contains(point);
		final boolean containsShapeRHS = shapeRHS.contains(point);
		final boolean containsUnion = containsShapeLHS || containsShapeRHS;
		
		return containsUnion;
	}
}