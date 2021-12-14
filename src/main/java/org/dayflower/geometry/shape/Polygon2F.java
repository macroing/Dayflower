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
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Shape2F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Polygon2F} is an implementation of {@link Shape2F} that represents a polygon.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Polygon2F implements Shape2F {
	/**
	 * The name of this {@code Polygon2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Polygon";
	
	/**
	 * The ID of this {@code Polygon2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment2F> lineSegments;
	private final Point2F[] points;
	private final Rectangle2F rectangle;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Polygon2F} instance.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point2F[]} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Polygon2F(final Point2F... points) {
		this.points = doRequireValidPoints(points);
		this.lineSegments = LineSegment2F.fromPoints(this.points);
		this.rectangle = Rectangle2F.fromPoints(this.points);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} that contains {@link LineSegment2F} instances that connects all {@link Point2F} instances in this {@code Polygon2F} instance.
	 * 
	 * @return a {@code List} that contains {@code LineSegment2F} instances that connects all {@link Point2F} instances in this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	public List<LineSegment2F> getLineSegments() {
		return new ArrayList<>(this.lineSegments);
	}
	
	/**
	 * Returns the {@link Point2F} instance at index {@code index} in this {@code Polygon2F} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the {@code Point2F} to return
	 * @return the {@code Point2F} instance at index {@code index} in this {@code Polygon2F} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than or equal to {@code polygon.getPointCount()}
	 */
//	TODO: Add Unit Tests!
	public Point2F getPoint(final int index) {
		ParameterArguments.requireRange(index, 0, getPointCount() - 1, "index");
		
		return this.points[index];
	}
	
	/**
	 * Returns the {@link Rectangle2F} instance that contains this {@code Polygon2F} instance.
	 * 
	 * @return the {@code Rectangle2F} instance that contains this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	public Rectangle2F getRectangle() {
		return this.rectangle;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Polygon2F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Polygon2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Polygon2F(%s)", Point2F.toString(this.points));
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
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				for(final LineSegment2F lineSegment : this.lineSegments) {
					if(!lineSegment.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				for(final Point2F point : this.points) {
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Polygon2F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Polygon2F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point2F point) {
		return this.rectangle.contains(point) ? doContains(point) || doContainsOnLineSegments(point) : false;
	}
	
	/**
	 * Compares {@code object} to this {@code Polygon2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Polygon2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Polygon2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Polygon2F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Polygon2F)) {
			return false;
		} else if(!Objects.equals(this.lineSegments, Polygon2F.class.cast(object).lineSegments)) {
			return false;
		} else if(!Arrays.equals(this.points, Polygon2F.class.cast(object).points)) {
			return false;
		} else if(!Objects.equals(this.rectangle, Polygon2F.class.cast(object).rectangle)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Polygon2F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the {@link Point2F} count of this {@code Polygon2F} instance.
	 * 
	 * @return the {@code Point2F} count of this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	public int getPointCount() {
		return this.points.length;
	}
	
	/**
	 * Returns a hash code for this {@code Polygon2F} instance.
	 * 
	 * @return a hash code for this {@code Polygon2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.lineSegments, Integer.valueOf(Arrays.hashCode(this.points)), this.rectangle);
	}
	
	/**
	 * Writes this {@code Polygon2F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			dataOutput.writeInt(this.points.length);
			
			for(final Point2F point : this.points) {
				point.write(dataOutput);
			}
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doContains(final Point2F point) {
		boolean isInside = false;
		
		final float pX = point.getX();
		final float pY = point.getY();
		
		for(int i = 0, j = this.points.length - 1; i < this.points.length; j = i++) {
			final Point2F pointI = this.points[i];
			final Point2F pointJ = this.points[j];
			
			final float iX = pointI.getX();
			final float iY = pointI.getY();
			final float jX = pointJ.getX();
			final float jY = pointJ.getY();
			
			if((iY > pY) != (jY > pY) && pX < (jX - iX) * (pY - iY) / (jY - iY) + iX) {
				isInside = !isInside;
			}
		}
		
		return isInside;
	}
	
	private boolean doContainsOnLineSegments(final Point2F point) {
		for(final LineSegment2F lineSegment : this.lineSegments) {
			if(lineSegment.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point2F[] doRequireValidPoints(final Point2F[] points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		return points.clone();
	}
}