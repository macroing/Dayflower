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

import org.dayflower.node.Node;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;

/**
 * A {@code Vector4D} represents a vector with four {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector4D implements Node {
	/**
	 * The W-component of this {@code Vector4D} instance.
	 */
	public final double w;
	
	/**
	 * The X-component of this {@code Vector4D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Vector4D} instance.
	 */
	public final double y;
	
	/**
	 * The Z-component of this {@code Vector4D} instance.
	 */
	public final double z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector4D} instance given the component values {@code 0.0D}, {@code 0.0D}, {@code 0.0D} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4D(0.0D, 0.0D, 0.0D, 1.0D);
	 * }
	 * </pre>
	 */
	public Vector4D() {
		this(0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Vector4D} instance given the component values {@code p.x}, {@code p.y}, {@code p.z} and {@code p.w}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4D(p.x, p.y, p.z, p.w);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector4D(final Point4D p) {
		this(p.x, p.y, p.z, p.w);
	}
	
	/**
	 * Constructs a new {@code Vector4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector4D(x, y, z, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Vector4D(final double x, final double y, final double z) {
		this(x, y, z, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Vector4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Vector4D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector4D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector4D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector4D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector4D)) {
			return false;
		} else if(!Doubles.equals(this.w, Vector4D.class.cast(object).w)) {
			return false;
		} else if(!Doubles.equals(this.x, Vector4D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Vector4D.class.cast(object).y)) {
			return false;
		} else if(!Doubles.equals(this.z, Vector4D.class.cast(object).z)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the length of this {@code Vector4D} instance.
	 * 
	 * @return the length of this {@code Vector4D} instance
	 */
	public double length() {
		return Doubles.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector4D} instance.
	 * 
	 * @return the squared length of this {@code Vector4D} instance
	 */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Vector4D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Vector4D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y, this.z, this.w};
	}
	
	/**
	 * Returns a hash code for this {@code Vector4D} instance.
	 * 
	 * @return a hash code for this {@code Vector4D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.w), Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Vector4D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.x);
			dataOutput.writeDouble(this.y);
			dataOutput.writeDouble(this.z);
			dataOutput.writeDouble(this.w);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4D} instance on the left-hand side
	 * @param vRHS the {@code Vector4D} instance on the right-hand side
	 * @return a new {@code Vector4D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector4D add(final Vector4D vLHS, final Vector4D vRHS) {
		return new Vector4D(vLHS.x + vRHS.x, vLHS.y + vRHS.y, vLHS.z + vRHS.z, vLHS.w + vRHS.w);
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector4D divide(final Vector4D vLHS, final double sRHS) {
		return new Vector4D(Doubles.finiteOrDefault(vLHS.x / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.y / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.z / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.w / sRHS, 0.0D));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector4D multiply(final Vector4D vLHS, final double sRHS) {
		return new Vector4D(vLHS.x * sRHS, vLHS.y * sRHS, vLHS.z * sRHS, vLHS.w * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector4D} instance
	 * @return a new {@code Vector4D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector4D negate(final Vector4D v) {
		return new Vector4D(-v.x, -v.y, -v.z, -v.w);
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector4D} instance
	 * @return a new {@code Vector4D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector4D normalize(final Vector4D v) {
		final double length = v.length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
	}
	
	/**
	 * Returns a new {@code Vector4D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector4D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector4D read(final DataInput dataInput) {
		try {
			return new Vector4D(dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector4D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector4D} instance on the left-hand side
	 * @param vRHS the {@code Vector4D} instance on the right-hand side
	 * @return a new {@code Vector4D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector4D subtract(final Vector4D vLHS, final Vector4D vRHS) {
		return new Vector4D(vLHS.x - vRHS.x, vLHS.y - vRHS.y, vLHS.z - vRHS.z, vLHS.w - vRHS.w);
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector4D} instance on the left-hand side
	 * @param vRHS the {@code Vector4D} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static double dotProduct(final Vector4D vLHS, final Vector4D vRHS) {
		return vLHS.x * vRHS.x + vLHS.y * vRHS.y + vLHS.z * vRHS.z + vLHS.w * vRHS.w;
	}
}