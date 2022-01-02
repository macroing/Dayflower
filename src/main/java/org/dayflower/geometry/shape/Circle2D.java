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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Doubles.equal;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Shape2D;
import org.dayflower.geometry.Vector2D;
import org.dayflower.java.lang.Strings;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Circle2D} is an implementation of {@link Shape2D} that represents a circle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Circle2D implements Shape2D {
	/**
	 * The name of this {@code Circle2D} class.
	 */
	public static final String NAME = "Circle";
	
	/**
	 * The ID of this {@code Circle2D} class.
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2D center;
	private final double radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Circle2D} instance with a center of {@code new Point2D()} and a radius of {@code 10.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2D(new Point2D());
	 * }
	 * </pre>
	 */
	public Circle2D() {
		this(new Point2D());
	}
	
	/**
	 * Constructs a new {@code Circle2D} instance with a center of {@code center} and a radius of {@code 10.0D}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2D(center, 10.0D);
	 * }
	 * </pre>
	 * 
	 * @param center the center of this {@code Circle2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Circle2D(final Point2D center) {
		this(center, 10.0D);
	}
	
	/**
	 * Constructs a new {@code Circle2D} instance with a center of {@code center} and a radius of {@code radius}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param center the center of this {@code Circle2D} instance
	 * @param radius the radius of this {@code Circle2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Circle2D(final Point2D center, final double radius) {
		this.center = Objects.requireNonNull(center, "center == null");
		this.radius = radius;
	}
	
	/**
	 * Constructs a new {@code Circle2D} instance that contains {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public Circle2D(final Rectangle2D rectangle) {
		this.center = Point2D.midpoint(rectangle.getA(), rectangle.getC());
		this.radius = Point2D.distance(rectangle.getA(), rectangle.getC()) / 2.0D;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the center of this {@code Circle2D} instance.
	 * 
	 * @return the center of this {@code Circle2D} instance
	 */
	public Point2D getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Circle2D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Circle2D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Circle2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Circle2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Circle2D(%s, %s)", this.center, Strings.toNonScientificNotationJava(this.radius));
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Circle2D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Circle2D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2D point) {
		return Vector2D.direction(this.center, point).lengthSquared() <= this.radius * this.radius;
	}
	
	/**
	 * Compares {@code object} to this {@code Circle2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Circle2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Circle2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Circle2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Circle2D)) {
			return false;
		} else if(!Objects.equals(this.center, Circle2D.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, Circle2D.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code Circle2D} instance.
	 * 
	 * @return the radius of this {@code Circle2D} instance
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Circle2D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Circle2D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Circle2D} instance.
	 * 
	 * @return a hash code for this {@code Circle2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Double.valueOf(this.radius));
	}
	
	/**
	 * Writes this {@code Circle2D} instance to {@code dataOutput}.
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
			
			dataOutput.writeDouble(this.radius);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}