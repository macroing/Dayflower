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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Ints.toInt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.geometry.Vector2I;

import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code Circle2I} is an implementation of {@link Shape2I} that represents a circle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Circle2I implements Shape2I {
	/**
	 * The name of this {@code Circle2I} class.
	 */
	public static final String NAME = "Circle";
	
	/**
	 * The ID of this {@code Circle2F} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2I center;
	private final int radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Circle2I} instance with a center of {@code new Point2I()} and a radius of {@code 10}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2I(new Point2I());
	 * }
	 * </pre>
	 */
	public Circle2I() {
		this(new Point2I());
	}
	
	/**
	 * Constructs a new {@code Circle2I} instance from {@code circle}.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle a {@link Circle2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public Circle2I(final Circle2D circle) {
		this.center = new Point2I(circle.getCenter());
		this.radius = toInt(circle.getRadius());
	}
	
	/**
	 * Constructs a new {@code Circle2I} instance from {@code circle}.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle a {@link Circle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public Circle2I(final Circle2F circle) {
		this.center = new Point2I(circle.getCenter());
		this.radius = toInt(circle.getRadius());
	}
	
	/**
	 * Constructs a new {@code Circle2I} instance with a center of {@code center} and a radius of {@code 10}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2I(center, 10);
	 * }
	 * </pre>
	 * 
	 * @param center the center of this {@code Circle2I} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Circle2I(final Point2I center) {
		this(center, 10);
	}
	
	/**
	 * Constructs a new {@code Circle2I} instance with a center of {@code center} and a radius of {@code radius}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param center the center of this {@code Circle2I} instance
	 * @param radius the radius of this {@code Circle2I} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Circle2I(final Point2I center, final int radius) {
		this.center = Objects.requireNonNull(center, "center == null");
		this.radius = radius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with {@link Point2I} instances contained in this {@code Circle2I} instance.
	 * 
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Circle2I} instance, {@code false} otherwise
	 * @return a {@code List} with {@code Point2I} instances contained in this {@code Circle2I} instance
	 */
	@Override
	public List<Point2I> findPoints(final boolean isIncludingBorderOnly) {
		final List<Point2I> points = new ArrayList<>();
		
		final Point2I maximum = getMaximum();
		final Point2I minimum = getMinimum();
		
		final int maximumX = maximum.x;
		final int minimumX = minimum.x;
		final int maximumY = maximum.y;
		final int minimumY = minimum.y;
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				final Point2I point = new Point2I(x, y);
				
				if(contains(point, isIncludingBorderOnly)) {
					points.add(point);
				}
			}
		}
		
		return points;
	}
	
	/**
	 * Returns the center of this {@code Circle2I} instance.
	 * 
	 * @return the center of this {@code Circle2I} instance
	 */
	public Point2I getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@link Point2I} with the largest component values needed to contain this {@code Circle2I} instance.
	 * 
	 * @return a {@code Point2I} with the largest component values needed to contain this {@code Circle2I} instance
	 */
	@Override
	public Point2I getMaximum() {
		return new Point2I(this.center.x + this.radius, this.center.y + this.radius);
	}
	
	/**
	 * Returns a {@link Point2I} with the smallest component values needed to contain this {@code Circle2I} instance.
	 * 
	 * @return a {@code Point2I} with the smallest component values needed to contain this {@code Circle2I} instance
	 */
	@Override
	public Point2I getMinimum() {
		return new Point2I(this.center.x - this.radius, this.center.y - this.radius);
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Circle2I} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Circle2I} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Circle2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Circle2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Circle2I(%s, %d)", this.center, Integer.valueOf(this.radius));
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
				if(!this.center.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Circle2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @param isIncludingBorderOnly {@code true} if, and only if, this method should only include {@code Point2I} instances on the border of this {@code Circle2I} instance, {@code false} otherwise
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Circle2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2I point, final boolean isIncludingBorderOnly) {
		final int lengthSquared = Vector2I.direction(this.center, point).lengthSquared();
		
		final boolean isInsideCircle = lengthSquared <= this.radius * this.radius;
		final boolean isInsideCircleBorder = isInsideCircle && lengthSquared > (this.radius - 1) * (this.radius - 1);
		
		return isIncludingBorderOnly ? isInsideCircleBorder : isInsideCircle;
	}
	
	/**
	 * Compares {@code object} to this {@code Circle2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Circle2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Circle2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Circle2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Circle2I)) {
			return false;
		} else if(!Objects.equals(this.center, Circle2I.class.cast(object).center)) {
			return false;
		} else if(this.radius != Circle2I.class.cast(object).radius) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Circle2I} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Circle2I} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns the radius of this {@code Circle2I} instance.
	 * 
	 * @return the radius of this {@code Circle2I} instance
	 */
	public int getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns a hash code for this {@code Circle2I} instance.
	 * 
	 * @return a hash code for this {@code Circle2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Integer.valueOf(this.radius));
	}
	
	/**
	 * Writes this {@code Circle2I} instance to {@code dataOutput}.
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
			
			this.center.write(dataOutput);
			
			dataOutput.writeInt(this.radius);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}