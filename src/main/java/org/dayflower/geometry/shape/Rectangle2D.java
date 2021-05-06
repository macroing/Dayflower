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

import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Shape2D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Rectangle2D} denotes a 2-dimensional rectangle, of type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Rectangle2D implements Shape2D {
	private final Point2D a;
	private final Point2D b;
	private final Point2D c;
	private final Point2D d;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Rectangle2D} instance based on {@code x} and {@code y}.
	 * <p>
	 * If either {@code x} or {@code y} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x a {@link Point2D} instance
	 * @param y a {@code Point2D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code x} or {@code y} are {@code null}
	 */
	public Rectangle2D(final Point2D x, final Point2D y) {
		this.a = new Point2D(min(x.getX(), y.getX()), min(x.getY(), y.getY()));
		this.b = new Point2D(min(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.c = new Point2D(max(x.getX(), y.getX()), max(x.getY(), y.getY()));
		this.d = new Point2D(max(x.getX(), y.getX()), min(x.getY(), y.getY()));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Point2D} with the minimum X and minimum Y coordinate values.
	 * 
	 * @return the {@code Point2D} with the minimum X and minimum Y coordinate values
	 */
	public Point2D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point2D} with the minimum X and maximum Y coordinate values.
	 * 
	 * @return the {@code Point2D} with the minimum X and maximum Y coordinate values
	 */
	public Point2D getB() {
		return this.b;
	}
	
	/**
	 * Returns the {@link Point2D} with the maximum X and maximum Y coordinate values.
	 * 
	 * @return the {@code Point2D} with the maximum X and maximum Y coordinate values
	 */
	public Point2D getC() {
		return this.c;
	}
	
	/**
	 * Returns the {@link Point2D} with the maximum X and minimum Y coordinate values.
	 * 
	 * @return the {@code Point2D} with the maximum X and minimum Y coordinate values
	 */
	public Point2D getD() {
		return this.d;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Rectangle2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Rectangle2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Rectangle2D(%s, %s)", this.a, this.c);
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
				
				if(!this.d.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point2D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Rectangle2D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public boolean contains(final Point2D point) {
		return point.getX() >= this.a.getX() && point.getX() <= this.c.getX() && point.getY() >= this.a.getY() && point.getY() <= this.c.getY();
	}
	
	/**
	 * Compares {@code object} to this {@code Rectangle2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Rectangle2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Rectangle2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Rectangle2D)) {
			return false;
		} else if(!Objects.equals(this.a, Rectangle2D.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Rectangle2D.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Rectangle2D.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.d, Rectangle2D.class.cast(object).d)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the height of this {@code Rectangle2D} instance.
	 * 
	 * @return the height of this {@code Rectangle2D} instance
	 */
	public double getHeight() {
		return this.c.getY() - this.a.getY();
	}
	
	/**
	 * Returns the width of this {@code Rectangle2D} instance.
	 * 
	 * @return the width of this {@code Rectangle2D} instance
	 */
	public double getWidth() {
		return this.c.getX() - this.a.getX();
	}
	
	/**
	 * Returns a hash code for this {@code Rectangle2D} instance.
	 * 
	 * @return a hash code for this {@code Rectangle2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c, this.d);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes the intersection between {@code a} and {@code b}.
	 * <p>
	 * Returns an {@code Optional} with an optional {@code Rectangle2D} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2D} instance
	 * @param b a {@code Rectangle2D} instance
	 * @return an {@code Optional} with an optional {@code Rectangle2D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Optional<Rectangle2D> intersection(final Rectangle2D a, final Rectangle2D b) {
		final Point2D minimumA = a.getA();
		final Point2D minimumB = b.getA();
		final Point2D maximumA = a.getC();
		final Point2D maximumB = b.getC();
		final Point2D minimumC = Point2D.maximum(minimumA, minimumB);
		final Point2D maximumC = Point2D.minimum(maximumA, maximumB);
		
		if(minimumC.getX() > maximumC.getX() || minimumC.getY() > maximumC.getY()) {
			return Optional.empty();
		}
		
		return Optional.of(new Rectangle2D(minimumC, maximumC));
	}
	
	/**
	 * Returns a {@code Rectangle2D} instance that is the union of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Rectangle2D} instance
	 * @param b a {@code Rectangle2D} instance
	 * @return a {@code Rectangle2D} instance that is the union of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Rectangle2D union(final Rectangle2D a, final Rectangle2D b) {
		final Point2D minimum = Point2D.minimum(a.getA(), b.getA());
		final Point2D maximum = Point2D.maximum(a.getC(), b.getC());
		
		return new Rectangle2D(minimum, maximum);
	}
}