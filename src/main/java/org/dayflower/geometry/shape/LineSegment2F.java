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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Shape2F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code LineSegment2F} is an implementation of {@link Shape2F} that represents a line segment.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LineSegment2F implements Shape2F {
	/**
	 * The name of this {@code LineSegment2F} class.
	 */
	public static final String NAME = "Line Segment";
	
	/**
	 * The ID of this {@code LineSegment2F} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2F a;
	private final Point2F b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LineSegment2F} instance given two {@link Point2F} instances, {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public LineSegment2F(final Point2F a, final Point2F b) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Point2F} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point2F} instance denoted by {@code A}
	 */
	public Point2F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2F} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point2F} instance denoted by {@code B}
	 */
	public Point2F getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code LineSegment2F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code LineSegment2F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LineSegment2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code LineSegment2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new LineSegment2F(%s, %s)", this.a, this.b);
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
				if(!this.a.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.b.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code LineSegment2F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code LineSegment2F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2F point) {
		final float aX = this.a.getX();
		final float aY = this.a.getY();
		final float bX = this.b.getX();
		final float bY = this.b.getY();
		final float pX = point.getX();
		final float pY = point.getY();
		
		final float dAPX = pX - aX;
		final float dAPY = pY - aY;
		final float dABX = bX - aX;
		final float dABY = bY - aY;
		
		final float crossProduct = dAPX * dABY - dAPY * dABX;
		
		if(!isZero(crossProduct)) {
			return false;
		} else if(abs(dABX) >= abs(dABY)) {
			return dABX > 0.0F ? aX <= pX && pX <= bX : bX <= pX && pX <= aX;
		} else {
			return dABY > 0.0F ? aY <= pY && pY <= bY : bY <= pY && pY <= aY;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code LineSegment2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LineSegment2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LineSegment2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LineSegment2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LineSegment2F)) {
			return false;
		} else if(!Objects.equals(this.a, LineSegment2F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, LineSegment2F.class.cast(object).b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code LineSegment2F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code LineSegment2F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code LineSegment2F} instance.
	 * 
	 * @return a hash code for this {@code LineSegment2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b);
	}
	
	/**
	 * Writes this {@code LineSegment2F} instance to {@code dataOutput}.
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
			
			this.a.write(dataOutput);
			this.b.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code LineSegment2F} instances that are connecting the {@link Point2F} instances in {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 2}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point2F[]} instance
	 * @return a {@code List} of {@code LineSegment2F} instances that are connecting the {@code Point2F} instances in {@code points}
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 2}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static List<LineSegment2F> fromPoints(final Point2F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 2, Integer.MAX_VALUE, "points.length");
		
		final List<LineSegment2F> lineSegments = new ArrayList<>(points.length);
		
		for(int i = 0, j = 1; i < points.length; i++, j = (j + 1) % points.length) {
			final Point2F pointI = points[i];
			final Point2F pointJ = points[j];
			
			final LineSegment2F lineSegment = new LineSegment2F(pointI, pointJ);
			
			lineSegments.add(lineSegment);
		}
		
		return lineSegments;
	}
}