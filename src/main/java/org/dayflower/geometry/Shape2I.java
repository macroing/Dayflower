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
package org.dayflower.geometry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A {@code Shape2I} is a 2-dimensional extension of {@link Shape} that adds additional methods that operates on {@code int}-based data types.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape2I extends Shape {
	/**
	 * Returns a {@code List} with {@link Point2I} instances contained in this {@code Shape2I} instance.
	 * 
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Shape2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances contained in this {@code Shape2I} instance
	 */
	List<Point2I> findPoints(final boolean isIncludingBorderOnly);
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Shape2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Shape2I} instance, {@code false} otherwise
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Shape2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	boolean contains(final Point2I point, final boolean isIncludingBorderOnly);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances contained in this {@code Shape2I} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * shape2I.findPoints(false);
	 * }
	 * </pre>
	 * 
	 * @return a {@code List} with {@code Point2I} instances contained in this {@code Shape2I} instance
	 */
	default List<Point2I> findPoints() {
		return findPoints(false);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the complement of this {@code Shape2I} instance within {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * shape2I.findPointsOfComplement(shape, false);
	 * }
	 * </pre>
	 * 
	 * @param shape a {@code Shape2I} instance
	 * @return a {@code List} with {@code Point2I} instances that represents the complement of this {@code Shape2I} instance within {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	default List<Point2I> findPointsOfComplement(final Shape2I shape) {
		return findPointsOfComplement(shape, false);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the complement of this {@code Shape2I} instance within {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape a {@code Shape2I} instance
	 * @param isExcludingBorderOnly {@code true} if, and only if, this method should only exclude {@code Point2I} instances on the border of this {@code Shape2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances that represents the complement of this {@code Shape2I} instance within {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	default List<Point2I> findPointsOfComplement(final Shape2I shape, final boolean isExcludingBorderOnly) {
		return shape.findPoints().stream().filter(point -> !contains(point) || isExcludingBorderOnly && !contains(point, true)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the intersection between this {@code Shape2I} instance and {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * shape2I.findPointsOfIntersection(shape, false);
	 * }
	 * </pre>
	 * 
	 * @param shape a {@code Shape2I} instance
	 * @return a {@code List} with {@code Point2I} instances that represents the intersection between this {@code Shape2I} instance and {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	default List<Point2I> findPointsOfIntersection(final Shape2I shape) {
		return findPointsOfIntersection(shape, false);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the intersection between this {@code Shape2I} instance and {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape a {@code Shape2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Shape2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances that represents the intersection between this {@code Shape2I} instance and {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	default List<Point2I> findPointsOfIntersection(final Shape2I shape, final boolean isIncludingBorderOnly) {
		return shape.findPoints().stream().filter(point -> contains(point, isIncludingBorderOnly)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Shape2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * shape2I.contains(point, false);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Shape2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	default boolean contains(final Point2I point) {
		return contains(point, false);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in {@code shapeLHS} but not in {@code shapeRHS}, {@code false} otherwise.
	 * <p>
	 * If either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @param shapeLHS the {@code Shape2I} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2I} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in {@code shapeLHS} but not in {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsDifference(final Point2I point, final Shape2I shapeLHS, final Shape2I shapeRHS) {
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
	 * @param point a {@link Point2I} instance
	 * @param shapeLHS the {@code Shape2I} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2I} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in both {@code shapeLHS} and {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsIntersection(final Point2I point, final Shape2I shapeLHS, final Shape2I shapeRHS) {
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
	 * @param point a {@link Point2I} instance
	 * @param shapeLHS the {@code Shape2I} instance on the left-hand side of the operation
	 * @param shapeRHS the {@code Shape2I} instance on the right-hand side of the operation
	 * @return {@code true} if, and only if, {@code point} is contained in either {@code shapeLHS} or {@code shapeRHS}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code shapeLHS} or {@code shapeRHS} are {@code null}
	 */
	static boolean containsUnion(final Point2I point, final Shape2I shapeLHS, final Shape2I shapeRHS) {
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(shapeLHS, "shapeLHS == null");
		Objects.requireNonNull(shapeRHS, "shapeRHS == null");
		
		final boolean containsShapeLHS = shapeLHS.contains(point);
		final boolean containsShapeRHS = shapeRHS.contains(point);
		final boolean containsUnion = containsShapeLHS || containsShapeRHS;
		
		return containsUnion;
	}
}