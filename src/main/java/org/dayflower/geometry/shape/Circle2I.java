/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Circle2I} denotes a 2-dimensional circle that uses the data type {@code int}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Circle2I implements Shape2I {
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
	 * Returns the center of this {@code Circle2I} instance.
	 * 
	 * @return the center of this {@code Circle2I} instance
	 */
	public Point2I getCenter() {
		return this.center;
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
}