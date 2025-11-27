/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Vector2F} represents a vector with two {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2F implements Node {
	/**
	 * A {@code Vector2F} instance given the component values {@code Float.NaN} and {@code Float.NaN}.
	 */
	public static final Vector2F NaN = new Vector2F(Float.NaN, Float.NaN);
	
	/**
	 * A {@code Vector2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 */
	public static final Vector2F ZERO = new Vector2F(0.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector2F, Vector2F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Vector2F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Vector2F} instance.
	 */
	public final float y;
	
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
	 * Constructs a new {@code Vector2F} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector2F(final Point2F p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector2F(final Point3F p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of both components
	 */
	public Vector2F(final float component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code x} and {@code y}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	public Vector2F(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2F(%s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y));
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
		} else if(!Floats.equals(this.x, Vector2F.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, Vector2F.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector2F} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector2F} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final float length = length();
		
		final boolean isLengthGTEThreshold = length >= Floats.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Floats.NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
	}
	
	/**
	 * Returns the length of this {@code Vector2F} instance.
	 * 
	 * @return the length of this {@code Vector2F} instance
	 */
	public float length() {
		return Floats.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector2F} instance.
	 * 
	 * @return the squared length of this {@code Vector2F} instance
	 */
	public float lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector2F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y};
	}
	
	/**
	 * Returns a hash code for this {@code Vector2F} instance.
	 * 
	 * @return a hash code for this {@code Vector2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y));
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
			dataOutput.writeFloat(this.x);
			dataOutput.writeFloat(this.y);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Vector2F} instance with the absolute component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the absolute component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F absolute(final Vector2F v) {
		return new Vector2F(Floats.abs(v.x), Floats.abs(v.y));
	}
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2F add(final Vector2F vLHS, final Vector2F vRHS) {
		return new Vector2F(vLHS.x + vRHS.x, vLHS.y + vRHS.y);
	}
	
	/**
	 * Adds the component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the addition.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param a a {@code Vector2F} instance
	 * @param b a {@code Vector2F} instance
	 * @param c a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector2F add(final Vector2F a, final Vector2F b, final Vector2F c) {
		return new Vector2F(a.x + b.x + c.x, a.y + b.y + c.y);
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
		return new Vector2F(lookAt.x - eye.x, lookAt.y - eye.y);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2F directionNormalized(final Point2F eye, final Point2F lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code p.x} and {@code p.y}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2F directionXY(final Point3F p) {
		return new Vector2F(p.x, p.y);
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code p.y} and {@code p.z}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2F directionYZ(final Point3F p) {
		return new Vector2F(p.y, p.z);
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code p.z} and {@code p.x}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code p.z} and {@code p.x}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2F directionZX(final Point3F p) {
		return new Vector2F(p.z, p.x);
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2F divide(final Vector2F vLHS, final float sRHS) {
		return new Vector2F(Floats.finiteOrDefault(vLHS.x / sRHS, 0.0F), Floats.finiteOrDefault(vLHS.y / sRHS, 0.0F));
	}
	
	/**
	 * Returns a cached version of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a cached version of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F getCached(final Vector2F v) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(v, "v == null"), key -> v);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector2F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector2F} instance
	 * @param b a {@code Vector2F} instance
	 * @param t the factor
	 * @return a {@code Vector2F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector2F lerp(final Vector2F a, final Vector2F b, final float t) {
		return new Vector2F(Floats.lerp(a.x, b.x, t), Floats.lerp(a.y, b.y, t));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2F multiply(final Vector2F vLHS, final float sRHS) {
		return new Vector2F(vLHS.x * sRHS, vLHS.y * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F negate(final Vector2F v) {
		return new Vector2F(-v.x, -v.y);
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F normalize(final Vector2F v) {
		final float length = v.length();
		
		final boolean isLengthGTEThreshold = length >= Floats.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Floats.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
	}
	
	/**
	 * Returns a {@code Vector2F} instance that is perpendicular to {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a {@code Vector2F} instance that is perpendicular to {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F perpendicular(final Vector2F v) {
		return new Vector2F(v.y, -v.x);
	}
	
	/**
	 * Returns a random {@code Vector2F} instance.
	 * 
	 * @return a random {@code Vector2F} instance
	 */
	public static Vector2F random() {
		return new Vector2F(Randoms.nextFloat() * 2.0F - 1.0F, Randoms.nextFloat() * 2.0F - 1.0F);
	}
	
	/**
	 * Returns a random and normalized {@code Vector2F} instance.
	 * 
	 * @return a random and normalized {@code Vector2F} instance
	 */
	public static Vector2F randomNormalized() {
		return normalize(random());
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
			return new Vector2F(dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code Vector2F} instance with the reciprocal (or inverse) component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the reciprocal (or inverse) component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2F reciprocal(final Vector2F v) {
		return new Vector2F(1.0F / v.x, 1.0F / v.y);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2F subtract(final Vector2F vLHS, final Vector2F vRHS) {
		return new Vector2F(vLHS.x - vRHS.x, vLHS.y - vRHS.y);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vRHS} with the {@link Matrix33F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33F} instance
	 * @param vRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2F transform(final Matrix33F mLHS, final Vector2F vRHS) {
		final float x = mLHS.element11 * vRHS.x + mLHS.element12 * vRHS.y;
		final float y = mLHS.element21 * vRHS.x + mLHS.element22 * vRHS.y;
		
		return new Vector2F(x, y);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vRHS} with the {@link Matrix33F} {@code mLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33F} instance
	 * @param vRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2F transformTranspose(final Matrix33F mLHS, final Vector2F vRHS) {
		final float x = mLHS.element11 * vRHS.x + mLHS.element21 * vRHS.y;
		final float y = mLHS.element12 * vRHS.x + mLHS.element22 * vRHS.y;
		
		return new Vector2F(x, y);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}
	 */
	public static Vector2F x() {
		return x(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(x, 0.0F)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(x, 0.0F)}
	 */
	public static Vector2F x(final float x) {
		return new Vector2F(x, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}
	 */
	public static Vector2F y() {
		return y(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, y)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, y)}
	 */
	public static Vector2F y(final float y) {
		return new Vector2F(0.0F, y);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector2F vLHS, final Vector2F vRHS) {
		return Floats.isZero(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the cross product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return the cross product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static float crossProduct(final Vector2F vLHS, final Vector2F vRHS) {
		return vLHS.x * vRHS.y - vLHS.y * vRHS.x;
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static float dotProduct(final Vector2F vLHS, final Vector2F vRHS) {
		return vLHS.x * vRHS.x + vLHS.y * vRHS.y;
	}
	
	/**
	 * Returns the size of the cache.
	 * 
	 * @return the size of the cache
	 */
	public static int getCacheSize() {
		return CACHE.size();
	}
	
	/**
	 * Clears the cache.
	 */
	public static void clearCache() {
		CACHE.clear();
	}
}