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
package org.dayflower.geometry;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.macroing.java.lang.Doubles;

/**
 * A {@code Vector2I} represents a vector with two {@code int}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2I {
	/**
	 * The X-component of this {@code Vector2I} instance.
	 */
	public final int x;
	
	/**
	 * The Y-component of this {@code Vector2I} instance.
	 */
	public final int y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector2I} instance given the component values {@code 0} and {@code 0}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2I(0, 0);
	 * }
	 * </pre>
	 */
	public Vector2I() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new {@code Vector2I} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2I(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point2I} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector2I(final Point2I p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Vector2I} instance given the component values {@code x} and {@code y}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	public Vector2I(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2I(%d, %d)", Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2I}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2I)) {
			return false;
		} else if(this.x != Vector2I.class.cast(object).x) {
			return false;
		} else if(this.y != Vector2I.class.cast(object).y) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Vector2I} instance.
	 * 
	 * @return a hash code for this {@code Vector2I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Returns the length of this {@code Vector2I} instance.
	 * 
	 * @return the length of this {@code Vector2I} instance
	 */
	public int length() {
		return (int)(Doubles.sqrt(lengthSquared()));
	}
	
	/**
	 * Returns the squared length of this {@code Vector2I} instance.
	 * 
	 * @return the squared length of this {@code Vector2I} instance
	 */
	public int lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	/**
	 * Returns a {@code int[]} representation of this {@code Vector2I} instance.
	 * 
	 * @return a {@code int[]} representation of this {@code Vector2I} instance
	 */
	public int[] toArray() {
		return new int[] {this.x, this.y};
	}
	
	/**
	 * Writes this {@code Vector2I} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(this.x);
			dataOutput.writeInt(this.y);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2I} instance on the left-hand side
	 * @param vRHS the {@code Vector2I} instance on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2I add(final Vector2I vLHS, final Vector2I vRHS) {
		return new Vector2I(vLHS.x + vRHS.x, vLHS.y + vRHS.y);
	}
	
	/**
	 * Returns a new {@code Vector2I} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2I} instance denoting the eye to look from
	 * @param lookAt a {@code Point2I} instance denoting the target to look at
	 * @return a new {@code Vector2I} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2I direction(final Point2I eye, final Point2I lookAt) {
		return new Vector2I(lookAt.x - eye.x, lookAt.y - eye.y);
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2I} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2I divide(final Vector2I vLHS, final int sRHS) {
		return new Vector2I(vLHS.x / sRHS, vLHS.y / sRHS);
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2I} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2I multiply(final Vector2I vLHS, final int sRHS) {
		return new Vector2I(vLHS.x * sRHS, vLHS.y * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2I} instance
	 * @return a new {@code Vector2I} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2I negate(final Vector2I v) {
		return new Vector2I(-v.x, -v.y);
	}
	
	/**
	 * Returns a {@code Vector2I} instance that is perpendicular to {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2I} instance
	 * @return a {@code Vector2I} instance that is perpendicular to {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2I perpendicular(final Vector2I v) {
		return new Vector2I(v.y, -v.x);
	}
	
	/**
	 * Returns a new {@code Vector2I} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector2I} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector2I read(final DataInput dataInput) {
		try {
			return new Vector2I(dataInput.readInt(), dataInput.readInt());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2I} instance on the left-hand side
	 * @param vRHS the {@code Vector2I} instance on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2I subtract(final Vector2I vLHS, final Vector2I vRHS) {
		return new Vector2I(vLHS.x - vRHS.x, vLHS.y - vRHS.y);
	}
}