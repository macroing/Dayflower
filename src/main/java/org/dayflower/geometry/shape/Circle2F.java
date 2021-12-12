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

import static org.dayflower.utility.Floats.equal;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Shape2F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Circle2F} is an implementation of {@link Shape2F} that represents a circle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Circle2F implements Shape2F {
	/**
	 * The name of this {@code Circle2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Circle";
	
	/**
	 * The ID of this {@code Circle2F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2F center;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Circle2F} instance with a center of {@code new Point2F()} and a radius of {@code 10.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2F(new Point2F());
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Circle2F() {
		this(new Point2F());
	}
	
	/**
	 * Constructs a new {@code Circle2F} instance with a center of {@code center} and a radius of {@code 10.0F}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Circle2F(center, 10.0F);
	 * }
	 * </pre>
	 * 
	 * @param center the center of this {@code Circle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Circle2F(final Point2F center) {
		this(center, 10.0F);
	}
	
	/**
	 * Constructs a new {@code Circle2F} instance with a center of {@code center} and a radius of {@code radius}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param center the center of this {@code Circle2F} instance
	 * @param radius the radius of this {@code Circle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Circle2F(final Point2F center, final float radius) {
		this.center = Objects.requireNonNull(center, "center == null");
		this.radius = radius;
	}
	
	/**
	 * Constructs a new {@code Circle2F} instance that contains {@code rectangle}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Circle2F(final Rectangle2F rectangle) {
		this.center = Point2F.midpoint(rectangle.getA(), rectangle.getC());
		this.radius = Point2F.distance(rectangle.getA(), rectangle.getC()) / 2.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the center of this {@code Circle2F} instance.
	 * 
	 * @return the center of this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	public Point2F getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Circle2F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Circle2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Circle2F(%s, %+.10f)", this.center, Float.valueOf(this.radius));
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Circle2F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Circle2F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point2F point) {
		return Vector2F.direction(this.center, point).lengthSquared() <= this.radius * this.radius;
	}
	
	/**
	 * Compares {@code object} to this {@code Circle2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Circle2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Circle2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Circle2F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Circle2F)) {
			return false;
		} else if(!Objects.equals(this.center, Circle2F.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, Circle2F.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code Circle2F} instance.
	 * 
	 * @return the radius of this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Circle2F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Circle2F} instance.
	 * 
	 * @return a hash code for this {@code Circle2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Float.valueOf(this.radius));
	}
	
	/**
	 * Writes this {@code Circle2F} instance to {@code dataOutput}.
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
			
			this.center.write(dataOutput);
			
			dataOutput.writeFloat(this.radius);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}