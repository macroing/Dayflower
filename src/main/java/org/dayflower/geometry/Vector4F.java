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

import org.dayflower.node.Node;

import org.macroing.java.lang.Strings;

/**
 * A {@code Vector4F} represents a vector with four {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector4F implements Node {
	/**
	 * The W-component of this {@code Vector4F} instance.
	 */
	public final float w;
	
	/**
	 * The X-component of this {@code Vector4F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Vector4F} instance.
	 */
	public final float y;
	
	/**
	 * The Z-component of this {@code Vector4F} instance.
	 */
	public final float z;
	
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
	 * Constructs a new {@code Vector4F} instance given the component values {@code p.x}, {@code p.y}, {@code p.z} and {@code p.w}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4F(p.x, p.y, p.z, p.w);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector4F(final Point4F p) {
		this(p.x, p.y, p.z, p.w);
	}
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4F(x, y, z, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Vector4F(final float x, final float y, final float z) {
		this(x, y, z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Vector4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Vector4F(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector4F(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
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
		} else if(!equal(this.w, Vector4F.class.cast(object).w)) {
			return false;
		} else if(!equal(this.x, Vector4F.class.cast(object).x)) {
			return false;
		} else if(!equal(this.y, Vector4F.class.cast(object).y)) {
			return false;
		} else if(!equal(this.z, Vector4F.class.cast(object).z)) {
			return false;
		} else {
			return true;
		}
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
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector4F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector4F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y, this.z, this.w};
	}
	
	/**
	 * Returns a hash code for this {@code Vector4F} instance.
	 * 
	 * @return a hash code for this {@code Vector4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.w), Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
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
			dataOutput.writeFloat(this.x);
			dataOutput.writeFloat(this.y);
			dataOutput.writeFloat(this.z);
			dataOutput.writeFloat(this.w);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4F} instance on the left-hand side
	 * @param vRHS the {@code Vector4F} instance on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector4F add(final Vector4F vLHS, final Vector4F vRHS) {
		return new Vector4F(vLHS.x + vRHS.x, vLHS.y + vRHS.y, vLHS.z + vRHS.z, vLHS.w + vRHS.w);
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector4F divide(final Vector4F vLHS, final float sRHS) {
		return new Vector4F(finiteOrDefault(vLHS.x / sRHS, 0.0F), finiteOrDefault(vLHS.y / sRHS, 0.0F), finiteOrDefault(vLHS.z / sRHS, 0.0F), finiteOrDefault(vLHS.w / sRHS, 0.0F));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector4F multiply(final Vector4F vLHS, final float sRHS) {
		return new Vector4F(vLHS.x * sRHS, vLHS.y * sRHS, vLHS.z * sRHS, vLHS.w * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector4F} instance
	 * @return a new {@code Vector4F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector4F negate(final Vector4F v) {
		return new Vector4F(-v.x, -v.y, -v.z, -v.w);
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector4F} instance
	 * @return a new {@code Vector4F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector4F normalize(final Vector4F v) {
		final float length = v.length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
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
			return new Vector4F(dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector4F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4F} instance on the left-hand side
	 * @param vRHS the {@code Vector4F} instance on the right-hand side
	 * @return a new {@code Vector4F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector4F subtract(final Vector4F vLHS, final Vector4F vRHS) {
		return new Vector4F(vLHS.x - vRHS.x, vLHS.y - vRHS.y, vLHS.z - vRHS.z, vLHS.w - vRHS.w);
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector4F} instance on the left-hand side
	 * @param vRHS the {@code Vector4F} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static float dotProduct(final Vector4F vLHS, final Vector4F vRHS) {
		return vLHS.x * vRHS.x + vLHS.y * vRHS.y + vLHS.z * vRHS.z + vLHS.w * vRHS.w;
	}
}