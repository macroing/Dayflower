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
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle2I} is an implementation of {@link Shape2I} that represents a triangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle2I implements Shape2I {
	/**
	 * The name of this {@code Triangle2I} class.
	 */
	public static final String NAME = "Triangle";
	
	/**
	 * The ID of this {@code Triangle2I} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<LineSegment2I> lineSegments;
	private final Point2I a;
	private final Point2I b;
	private final Point2I c;
	private final Rectangle2I rectangle;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle2I} instance given three {@link Point2I} instances, {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2I} instance
	 * @param b a {@code Point2I} instance
	 * @param c a {@code Point2I} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public Triangle2I(final Point2I a, final Point2I b, final Point2I c) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
		this.c = Objects.requireNonNull(c, "c == null");
		this.lineSegments = LineSegment2I.fromPoints(a, b, c);
		this.rectangle = Rectangle2I.fromPoints(a, b, c);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} that contains {@link LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Triangle2I} instance.
	 * 
	 * @return a {@code List} that contains {@code LineSegment2I} instances that connects all {@link Point2I} instances in this {@code Triangle2I} instance
	 */
	public List<LineSegment2I> getLineSegments() {
		return new ArrayList<>(this.lineSegments);
	}
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances contained in this {@code Triangle2I} instance.
	 * 
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Triangle2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances contained in this {@code Triangle2I} instance
	 */
	@Override
	public List<Point2I> findPoints(final boolean isIncludingBorderOnly) {
		return this.rectangle.findPoints().stream().filter(point -> contains(point, isIncludingBorderOnly)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point2I} instance denoted by {@code A}
	 */
	public Point2I getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point2I} instance denoted by {@code B}
	 */
	public Point2I getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point2I} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point2I} instance denoted by {@code C}
	 */
	public Point2I getC() {
		return this.c;
	}
	
	/**
	 * Returns the {@link Rectangle2I} instance that contains this {@code Triangle2I} instance.
	 * 
	 * @return the {@code Rectangle2I} instance that contains this {@code Triangle2I} instance
	 */
	public Rectangle2I getRectangle() {
		return this.rectangle;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle2I} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle2I} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Triangle2I(%s, %s, %s)", this.a, this.b, this.c);
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
				
				if(!this.a.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.b.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.c.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Triangle2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Triangle2I} instance, {@code false} otherwise
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Triangle2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2I point, final boolean isIncludingBorderOnly) {
		return isIncludingBorderOnly ? doContainsOnLineSegments(point) : doContains(point) || doContainsOnLineSegments(point);
	}
	
	/**
	 * Compares {@code object} to this {@code Triangle2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Triangle2I)) {
			return false;
		} else if(!Objects.equals(this.lineSegments, Triangle2I.class.cast(object).lineSegments)) {
			return false;
		} else if(!Objects.equals(this.a, Triangle2I.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Triangle2I.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Triangle2I.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.rectangle, Triangle2I.class.cast(object).rectangle)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Triangle2I} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle2I} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle2I} instance.
	 * 
	 * @return a hash code for this {@code Triangle2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.lineSegments, this.a, this.b, this.c, this.rectangle);
	}
	
	/**
	 * Writes this {@code Triangle2I} instance to {@code dataOutput}.
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
			this.c.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doContains(final Point2I point) {
		final int signA = (point.getX() - this.b.getX()) * (this.a.getY() - this.b.getY()) - (this.a.getX() - this.b.getX()) * (point.getY() - this.b.getY());
		final int signB = (point.getX() - this.c.getX()) * (this.b.getY() - this.c.getY()) - (this.b.getX() - this.c.getX()) * (point.getY() - this.c.getY());
		final int signC = (point.getX() - this.a.getX()) * (this.c.getY() - this.a.getY()) - (this.c.getX() - this.a.getX()) * (point.getY() - this.a.getY());
		
		final boolean hasNegativeSign = signA < 0 || signB < 0 || signC < 0;
		final boolean hasPositiveSign = signA > 0 || signB > 0 || signC > 0;
		
		return !(hasNegativeSign && hasPositiveSign);
	}
	
	private boolean doContainsOnLineSegments(final Point2I point) {
		for(final LineSegment2I lineSegment : this.lineSegments) {
			if(lineSegment.contains(point)) {
				return true;
			}
		}
		
		return false;
	}
}