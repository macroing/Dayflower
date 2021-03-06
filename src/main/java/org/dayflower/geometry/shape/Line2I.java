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

import static org.dayflower.utility.Ints.abs;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Line2I} is an implementation of {@link Shape2I} that represents a line.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Line2I implements Shape2I {
	/**
	 * The name of this {@code Line2I} class.
	 */
	public static final String NAME = "Line";
	
	/**
	 * The ID of this {@code Line2I} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point2I a;
	private final Point2I b;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Line2I} instance given two {@link Point2I} instances, {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2I} instance
	 * @param b a {@code Point2I} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public Line2I(final Point2I a, final Point2I b) {
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * Returns a {@code String} with the name of this {@code Line2I} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Line2I} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Line2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Line2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Line2I(%s, %s)", this.a, this.b);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Line2I} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2I} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Line2I} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public boolean contains(final Point2I point) {
		final int aX = this.a.getX();
		final int aY = this.a.getY();
		final int bX = this.b.getX();
		final int bY = this.b.getY();
		final int pX = point.getX();
		final int pY = point.getY();
		
		final int dAPX = pX - aX;
		final int dAPY = pY - aY;
		final int dABX = bX - aX;
		final int dABY = bY - aY;
		
		final int crossProduct = dAPX * dABY - dAPY * dABX;
		
		if(crossProduct != 0) {
			return false;
		} else if(abs(dABX) >= abs(dABY)) {
			return dABX > 0 ? aX <= pX && pX <= bX : bX <= pX && pX <= aX;
		} else {
			return dABY > 0 ? aY <= pY && pY <= bY : bY <= pY && pY <= aY;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Line2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Line2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Line2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Line2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Line2I)) {
			return false;
		} else if(!Objects.equals(this.a, Line2I.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Line2I.class.cast(object).b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Line2I} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Line2I} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Line2I} instance.
	 * 
	 * @return a hash code for this {@code Line2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b);
	}
	
	/**
	 * Writes this {@code Line2I} instance to {@code dataOutput}.
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
}