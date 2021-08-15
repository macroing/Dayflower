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
package org.dayflower.geometry.shape;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Polygon2I} is an implementation of {@link Shape2I} that represents a polygon.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Polygon2I implements Shape2I {
	/**
	 * The name of this {@code Polygon2I} class.
	 */
	public static final String NAME = "Polygon";
	
	/**
	 * The ID of this {@code Polygon2I} class.
	 */
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment2I> lineSegments;
	private final Point2I[] points;
	private final Rectangle2I rectangle;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Polygon2I} instance.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point2I[]} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public Polygon2I(final Point2I... points) {
		this.points = doRequireValidPoints(points);
		this.lineSegments = LineSegment2I.fromPoints(this.points);
		this.rectangle = Rectangle2I.fromPoints(this.points);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} that contains {@link LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Polygon2I} instance.
	 * 
	 * @return a {@code List} that contains {@code LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Polygon2I} instance
	 */
	public List<LineSegment2I> getLineSegments() {
		return new ArrayList<>(this.lineSegments);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the intersection between this {@code Polygon2I} instance and {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * polygon.findPointsOfIntersection(rectangle, false);
	 * }
	 * </pre>
	 * 
	 * @param rectangle a {@link Rectangle2I} instance
	 * @return a {@code List} with {@code Point2I} instances that represents the intersection between this {@code Polygon2I} instance and {@code rectangle}
	 */
	public List<Point2I> findPointsOfIntersection(final Rectangle2I rectangle) {
		return findPointsOfIntersection(rectangle, false);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances that represents the intersection between this {@code Polygon2I} instance and {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Polygon2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances that represents the intersection between this {@code Polygon2I} instance and {@code rectangle}
	 */
	public List<Point2I> findPointsOfIntersection(final Rectangle2I rectangle, final boolean isIncludingBorderOnly) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		
		final List<Point2I> points = new ArrayList<>();
		
		Rectangle2I.intersection(this.rectangle, rectangle).ifPresent(rectangleIntersection -> {
			final Point2I minimum = rectangleIntersection.getA();
			final Point2I maximum = rectangleIntersection.getC();
			
			final int minimumX = minimum.getX();
			final int minimumY = minimum.getY();
			final int maximumX = maximum.getX();
			final int maximumY = maximum.getY();
			
			for(int y = minimumY; y <= maximumY; y++) {
				for(int x = minimumX; x <= maximumX; x++) {
					final Point2I point = new Point2I(x, y);
					
					if(isIncludingBorderOnly && doContainsOnLineSegments(point)) {
						points.add(point);
					} else if(!isIncludingBorderOnly && (doContains(point) || doContainsOnLineSegments(point))) {
						points.add(point);
					}
				}
			}
		});
		
		return points;
	}
	
	/**
	 * Returns a {@code List} that contains all {@link Point2I} instances in this {@code Polygon2I} instance.
	 * 
	 * @return a {@code List} that contains all {@code Point2I} instances in this {@code Polygon2I} instance
	 */
	public List<Point2I> getPoints() {
		return new ArrayList<>(Arrays.asList(this.points));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Polygon2I} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Polygon2I} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Polygon2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Polygon2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Polygon2I(%s)", Point2I.toString(this.points));
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final LineSegment2I lineSegment : this.lineSegments) {
					if(!lineSegment.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				for(final Point2I point : this.points) {
					if(!point.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				if(!this.rectangle.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Polygon2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Polygon2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2I point) {
		if(this.rectangle.contains(point)) {
			return doContains(point) || doContainsOnLineSegments(point);
		}
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code Polygon2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Polygon2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Polygon2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Polygon2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Polygon2I)) {
			return false;
		} else if(!Objects.equals(this.lineSegments, Polygon2I.class.cast(object).lineSegments)) {
			return false;
		} else if(!Arrays.equals(this.points, Polygon2I.class.cast(object).points)) {
			return false;
		} else if(!Objects.equals(this.rectangle, Polygon2I.class.cast(object).rectangle)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Polygon2I} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Polygon2I} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Polygon2I} instance.
	 * 
	 * @return a hash code for this {@code Polygon2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.lineSegments, Integer.valueOf(Arrays.hashCode(this.points)), this.rectangle);
	}
	
	/**
	 * Writes this {@code Polygon2I} instance to {@code dataOutput}.
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
			dataOutput.writeInt(this.points.length);
			
			for(final Point2I point : this.points) {
				point.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doContains(final Point2I point) {
		boolean isInside = false;
		
		final int pX = point.getX();
		final int pY = point.getY();
		
		for(int i = 0, j = this.points.length - 1; i < this.points.length; j = i++) {
			final Point2I pointI = this.points[i];
			final Point2I pointJ = this.points[j];
			
			final int iX = pointI.getX();
			final int iY = pointI.getY();
			final int jX = pointJ.getX();
			final int jY = pointJ.getY();
			
			if((iY > pY) != (jY > pY) && pX < (jX - iX) * (pY - iY) / (jY - iY) + iX) {
				isInside = !isInside;
			}
		}
		
		return isInside;
	}
	
	private boolean doContainsOnLineSegments(final Point2I point) {
		for(final LineSegment2I lineSegment : this.lineSegments) {
			if(lineSegment.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2I[] doRequireValidPoints(final Point2I[] points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		return points.clone();
	}
}