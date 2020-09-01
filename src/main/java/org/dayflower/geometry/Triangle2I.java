/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import java.util.Objects;

/**
 * A {@code Triangle2I} denotes a 2-dimensional triangle that uses the data type {@code int}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Triangle2I implements Shape2I {
	private final Point2I a;
	private final Point2I b;
	private final Point2I c;
	
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
	 * Returns the {@link Point2I} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point2I} instance denoted by {@code C}
	 */
	public Point2I getC() {
		return this.c;
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
		} else if(!Objects.equals(this.a, Triangle2I.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Triangle2I.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Triangle2I.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Triangle2I} instance.
	 * 
	 * @return a hash code for this {@code Triangle2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c);
	}
}