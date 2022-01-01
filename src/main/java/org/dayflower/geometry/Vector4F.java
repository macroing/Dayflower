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

import static org.dayflower.utility.Floats.NEXT_DOWN_1_3;
import static org.dayflower.utility.Floats.NEXT_UP_1_1;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.finiteOrDefault;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;

import org.dayflower.java.lang.Strings;
import org.dayflower.node.Node;

/**
 * A {@code Vector4F} represents a vector with four {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector4F implements Node {
	private final float component1;
	private final float component2;
	private final float component3;
	private final float component4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code 0.0F}, {@code 0.0F}, {@code 0.0F} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4F(0.0F, 0.0F, 0.0F, 1.0F);
	 * }
	 * </pre>
	 */
	public Vector4F() {
		this(0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code point.getComponent1()}, {@code point.getComponent2()}, {@code point.getComponent3()} and {@code point.getComponent4()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4F(point.getComponent1(), point.getComponent2(), point.getComponent3(), point.getComponent4());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector4F(final Point4F point) {
		this(point.getComponent1(), point.getComponent2(), point.getComponent3(), point.getComponent4());
	}
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4F(component1, component2, component3, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Vector4F(final float component1, final float component2, final float component3) {
		this(component1, component2, component3, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Vector4F(final float component1, final float component2, final float component3, final float component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector4F(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.component1), Strings.toNonScientificNotationJava(this.component2), Strings.toNonScientificNotationJava(this.component3), Strings.toNonScientificNotationJava(this.component4));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector4F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector4F)) {
			return false;
		} else if(!equal(this.component1, Vector4F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector4F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Vector4F.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Vector4F.class.cast(object).component4)) {
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
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public float getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public float getComponent4() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component4;
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
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public float getZ() {
		return this.component3;
	}
	
	/**
	 * Returns the length of this {@code Vector4F} instance.
	 * 
	 * @return the length of this {@code Vector4F} instance
	 */
	public float length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector4F} instance.
	 * 
	 * @return the squared length of this {@code Vector4F} instance
	 */
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2 + this.component3 * this.component3 + this.component4 * this.component4;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector4F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector4F} instance
	 */
	public float[] toArray() {
		return new float[] {
			this.component1,
			this.component2,
			this.component3,
			this.component4
		};
	}
	
	/**
	 * Returns a hash code for this {@code Vector4F} instance.
	 * 
	 * @return a hash code for this {@code Vector4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
	}
	
	/**
	 * Writes this {@code Vector4F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.component3);
			dataOutput.writeFloat(this.component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector4F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector4F} instance on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector4F add(final Vector4F vectorLHS, final Vector4F vectorRHS) {
		final float component1 = vectorLHS.component1 + vectorRHS.component1;
		final float component2 = vectorLHS.component2 + vectorRHS.component2;
		final float component3 = vectorLHS.component3 + vectorRHS.component3;
		final float component4 = vectorLHS.component4 + vectorRHS.component4;
		
		return new Vector4F(component1, component2, component3, component4);
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector4F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector4F divide(final Vector4F vectorLHS, final float scalarRHS) {
		final float component1 = finiteOrDefault(vectorLHS.component1 / scalarRHS, 0.0F);
		final float component2 = finiteOrDefault(vectorLHS.component2 / scalarRHS, 0.0F);
		final float component3 = finiteOrDefault(vectorLHS.component3 / scalarRHS, 0.0F);
		final float component4 = finiteOrDefault(vectorLHS.component4 / scalarRHS, 0.0F);
		
		return new Vector4F(component1, component2, component3, component4);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector4F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector4F multiply(final Vector4F vectorLHS, final float scalarRHS) {
		final float component1 = vectorLHS.component1 * scalarRHS;
		final float component2 = vectorLHS.component2 * scalarRHS;
		final float component3 = vectorLHS.component3 * scalarRHS;
		final float component4 = vectorLHS.component4 * scalarRHS;
		
		return new Vector4F(component1, component2, component3, component4);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector4F} instance
	 * @return a new {@code Vector4F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector4F negate(final Vector4F vector) {
		final float component1 = -vector.component1;
		final float component2 = -vector.component2;
		final float component3 = -vector.component3;
		final float component4 = -vector.component4;
		
		return new Vector4F(component1, component2, component3, component4);
	}
	
	/**
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector4F} instance
	 * @return a new {@code Vector4F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector4F normalize(final Vector4F vector) {
		final float length = vector.length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return vector;
		}
		
		return divide(vector, length);
	}
	
	/**
	 * Returns a new {@code Vector4F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector4F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector4F read(final DataInput dataInput) {
		try {
			final float component1 = dataInput.readFloat();
			final float component2 = dataInput.readFloat();
			final float component3 = dataInput.readFloat();
			final float component4 = dataInput.readFloat();
			
			return new Vector4F(component1, component2, component3, component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector4F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector4F} instance on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector4F subtract(final Vector4F vectorLHS, final Vector4F vectorRHS) {
		final float component1 = vectorLHS.component1 - vectorRHS.component1;
		final float component2 = vectorLHS.component2 - vectorRHS.component2;
		final float component3 = vectorLHS.component3 - vectorRHS.component3;
		final float component4 = vectorLHS.component4 - vectorRHS.component4;
		
		return new Vector4F(component1, component2, component3, component4);
	}
	
	/**
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector4F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector4F} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float dotProduct(final Vector4F vectorLHS, final Vector4F vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component1 + vectorLHS.component2 * vectorRHS.component2 + vectorLHS.component3 * vectorRHS.component3 + vectorLHS.component4 * vectorRHS.component4;
	}
}