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
import java.util.Objects;

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Shape2D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Triangle2D} is an implementation of {@link Shape2D} that represents a triangle.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle2D implements Shape2D {
	/**
	 * The name of this {@code Triangle2D} class.
	 */
	public static final String NAME = "Triangle";
	
	/**
	 * The ID of this {@code Triangle2D} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2D a;
	private final Point2D b;
	private final Point2D c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Triangle2D} instance given three {@link Point2D} instances, {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public Triangle2D(final Point2D a, final Point2D b, final Point2D c) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
		this.c = Objects.requireNonNull(c, "c == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Point2D} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point2D} instance denoted by {@code A}
	 */
	public Point2D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2D} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point2D} instance denoted by {@code B}
	 */
	public Point2D getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point2D} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point2D} instance denoted by {@code C}
	 */
	public Point2D getC() {
		return this.c;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Triangle2D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Triangle2D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Triangle2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Triangle2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Triangle2D(%s, %s, %s)", this.a, this.b, this.c);
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
				
				if(!this.c.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Triangle2D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Triangle2D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point2D point) {
		final double signA = (point.getX() - this.b.getX()) * (this.a.getY() - this.b.getY()) - (this.a.getX() - this.b.getX()) * (point.getY() - this.b.getY());
		final double signB = (point.getX() - this.c.getX()) * (this.b.getY() - this.c.getY()) - (this.b.getX() - this.c.getX()) * (point.getY() - this.c.getY());
		final double signC = (point.getX() - this.a.getX()) * (this.c.getY() - this.a.getY()) - (this.c.getX() - this.a.getX()) * (point.getY() - this.a.getY());
		
		final boolean hasNegativeSign = signA < 0.0D || signB < 0.0D || signC < 0.0D;
		final boolean hasPositiveSign = signA > 0.0D || signB > 0.0D || signC > 0.0D;
		
		return !(hasNegativeSign && hasPositiveSign);
	}
	
	/**
	 * Compares {@code object} to this {@code Triangle2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Triangle2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Triangle2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Triangle2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Triangle2D)) {
			return false;
		} else if(!Objects.equals(this.a, Triangle2D.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Triangle2D.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Triangle2D.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Triangle2D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Triangle2D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Triangle2D} instance.
	 * 
	 * @return a hash code for this {@code Triangle2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c);
	}
	
	/**
	 * Writes this {@code Triangle2D} instance to {@code dataOutput}.
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
}