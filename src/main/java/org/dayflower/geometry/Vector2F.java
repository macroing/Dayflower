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

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.node.Node;

/**
 * A {@code Vector2F} represents a vector with two {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2F implements Node {
	private final float component1;
	private final float component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Vector2F() {
		this(0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code point.getComponent1()} and {@code point.getComponent2()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(point.getComponent1(), point.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector2F(final Point2F point) {
		this(point.getComponent1(), point.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Vector2F(final float component1, final float component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2F(%+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2F)) {
			return false;
		} else if(!equal(this.component1, Vector2F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector2F.class.cast(object).component2)) {
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
	public float getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public float getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public float getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public float getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public float getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public float getY() {
		return this.component2;
	}
	
	/**
	 * Returns the length of this {@code Vector2F} instance.
	 * 
	 * @return the length of this {@code Vector2F} instance
	 */
	public float length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector2F} instance.
	 * 
	 * @return the squared length of this {@code Vector2F} instance
	 */
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector2F} instance
	 */
	public float[] toArray() {
		return new float[] {
			this.component1,
			this.component2
		};
	}
	
	/**
	 * Returns a hash code for this {@code Vector2F} instance.
	 * 
	 * @return a hash code for this {@code Vector2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	/**
	 * Writes this {@code Vector2F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.component1);
			dataOutput.writeFloat(this.component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F add(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		final float component1 = vectorLHS.component1 + vectorRHS.component1;
		final float component2 = vectorLHS.component2 + vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2F direction(final Point2F eye, final Point2F lookAt) {
		final float component1 = lookAt.getComponent1() - eye.getComponent1();
		final float component2 = lookAt.getComponent2() - eye.getComponent2();
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2F divide(final Vector2F vectorLHS, final float scalarRHS) {
		final float component1 = vectorLHS.component1 / scalarRHS;
		final float component2 = vectorLHS.component2 / scalarRHS;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2F multiply(final Vector2F vectorLHS, final float scalarRHS) {
		final float component1 = vectorLHS.component1 * scalarRHS;
		final float component2 = vectorLHS.component2 * scalarRHS;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F negate(final Vector2F vector) {
		final float component1 = -vector.component1;
		final float component2 = -vector.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F normalize(final Vector2F vector) {
		return divide(vector, vector.length());
	}
	
	/**
	 * Returns a new {@code Vector2F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector2F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector2F read(final DataInput dataInput) {
		try {
			final float component1 = dataInput.readFloat();
			final float component2 = dataInput.readFloat();
			
			return new Vector2F(component1, component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F subtract(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		final float component1 = vectorLHS.component1 - vectorRHS.component1;
		final float component2 = vectorLHS.component2 - vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vectorRHS} with the {@link Matrix33F} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33F} instance
	 * @param vectorRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F transform(final Matrix33F matrixLHS, final Vector2F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement12() * vectorRHS.component2;
		final float component2 = matrixLHS.getElement21() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vectorRHS} with the {@link Matrix33F} {@code matrixLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33F} instance
	 * @param vectorRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F transformTranspose(final Matrix33F matrixLHS, final Vector2F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement21() * vectorRHS.component2;
		final float component2 = matrixLHS.getElement12() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float dotProduct(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component1 + vectorLHS.component2 * vectorRHS.component2;
	}
}