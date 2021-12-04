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
package org.dayflower.geometry;

import static org.dayflower.utility.Doubles.sqrt;
import static org.dayflower.utility.Ints.toInt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

/**
 * A {@code Vector2I} represents a vector with two {@code int}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2I {
	private final int component1;
	private final int component2;
	
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
//	TODO: Add Unit Tests!
	public Vector2I() {
		this(0, 0);
	}
	
	/**
	 * Constructs a new {@code Vector2I} instance given the component values {@code point.getComponent1()} and {@code point.getComponent2()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2I(point.getComponent1(), point.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Vector2I(final Point2I point) {
		this(point.getComponent1(), point.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Vector2I} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
//	TODO: Add Unit Tests!
	public Vector2I(final int component1, final int component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2I} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Vector2I(%d, %d)", Integer.valueOf(this.component1), Integer.valueOf(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2I}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2I}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2I)) {
			return false;
		} else if(this.component1 != Vector2I.class.cast(object).component1) {
			return false;
		} else if(this.component2 != Vector2I.class.cast(object).component2) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
//	TODO: Add Unit Tests!
	public int getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
//	TODO: Add Unit Tests!
	public int getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
//	TODO: Add Unit Tests!
	public int getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
//	TODO: Add Unit Tests!
	public int getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
//	TODO: Add Unit Tests!
	public int getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
//	TODO: Add Unit Tests!
	public int getY() {
		return this.component2;
	}
	
	/**
	 * Returns a hash code for this {@code Vector2I} instance.
	 * 
	 * @return a hash code for this {@code Vector2I} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.component1), Integer.valueOf(this.component2));
	}
	
	/**
	 * Returns the length of this {@code Vector2I} instance.
	 * 
	 * @return the length of this {@code Vector2I} instance
	 */
//	TODO: Add Unit Tests!
	public int length() {
		return toInt(sqrt(lengthSquared()));
	}
	
	/**
	 * Returns the squared length of this {@code Vector2I} instance.
	 * 
	 * @return the squared length of this {@code Vector2I} instance
	 */
//	TODO: Add Unit Tests!
	public int lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2;
	}
	
	/**
	 * Returns a {@code int[]} representation of this {@code Vector2I} instance.
	 * 
	 * @return a {@code int[]} representation of this {@code Vector2I} instance
	 */
//	TODO: Add Unit Tests!
	public int[] toArray() {
		return new int[] {
			this.component1,
			this.component2
		};
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
//	TODO: Add Unit Tests!
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(this.component1);
			dataOutput.writeInt(this.component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2I} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2I} instance on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I add(final Vector2I vectorLHS, final Vector2I vectorRHS) {
		final int component1 = vectorLHS.component1 + vectorRHS.component1;
		final int component2 = vectorLHS.component2 + vectorRHS.component2;
		
		return new Vector2I(component1, component2);
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
//	TODO: Add Unit Tests!
	public static Vector2I direction(final Point2I eye, final Point2I lookAt) {
		final int component1 = lookAt.getComponent1() - eye.getComponent1();
		final int component2 = lookAt.getComponent2() - eye.getComponent2();
		
		return new Vector2I(component1, component2);
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I divide(final Vector2I vectorLHS, final int scalarRHS) {
		final int component1 = vectorLHS.component1 / scalarRHS;
		final int component2 = vectorLHS.component2 / scalarRHS;
		
		return new Vector2I(component1, component2);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I multiply(final Vector2I vectorLHS, final int scalarRHS) {
		final int component1 = vectorLHS.component1 * scalarRHS;
		final int component2 = vectorLHS.component2 * scalarRHS;
		
		return new Vector2I(component1, component2);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2I} instance
	 * @return a new {@code Vector2I} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I negate(final Vector2I vector) {
		final int component1 = -vector.component1;
		final int component2 = -vector.component2;
		
		return new Vector2I(component1, component2);
	}
	
	/**
	 * Returns a {@code Vector2I} instance that is perpendicular to {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2I} instance
	 * @return a {@code Vector2I} instance that is perpendicular to {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I perpendicular(final Vector2I vector) {
		final int component1 = +vector.component2;
		final int component2 = -vector.component1;
		
		return new Vector2I(component1, component2);
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
//	TODO: Add Unit Tests!
	public static Vector2I read(final DataInput dataInput) {
		try {
			final int component1 = dataInput.readInt();
			final int component2 = dataInput.readInt();
			
			return new Vector2I(component1, component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2I} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2I} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2I} instance on the right-hand side
	 * @return a new {@code Vector2I} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector2I subtract(final Vector2I vectorLHS, final Vector2I vectorRHS) {
		final int component1 = vectorLHS.component1 - vectorRHS.component1;
		final int component2 = vectorLHS.component2 - vectorRHS.component2;
		
		return new Vector2I(component1, component2);
	}
}