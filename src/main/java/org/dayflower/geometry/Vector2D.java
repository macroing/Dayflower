/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Vector2D} represents a vector with two {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2D implements Node {
	/**
	 * A {@code Vector2D} instance given the component values {@code Double.NaN} and {@code Double.NaN}.
	 */
	public static final Vector2D NaN = new Vector2D(Double.NaN, Double.NaN);
	
	/**
	 * A {@code Vector2D} instance given the component values {@code 0.0D} and {@code 0.0D}.
	 */
	public static final Vector2D ZERO = new Vector2D(0.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector2D, Vector2D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Vector2D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Vector2D} instance.
	 */
	public final double y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code 0.0D} and {@code 0.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Vector2D() {
		this(0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector2D(final Point2D p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector2D(final Point3D p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of both components
	 */
	public Vector2D(final double component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code x} and {@code y}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	public Vector2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2D(%s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2D)) {
			return false;
		} else if(!Doubles.equals(this.x, Vector2D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Vector2D.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector2D} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector2D} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final double length = length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
	}
	
	/**
	 * Returns the length of this {@code Vector2D} instance.
	 * 
	 * @return the length of this {@code Vector2D} instance
	 */
	public double length() {
		return Doubles.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector2D} instance.
	 * 
	 * @return the squared length of this {@code Vector2D} instance
	 */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Vector2D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Vector2D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y};
	}
	
	/**
	 * Returns a hash code for this {@code Vector2D} instance.
	 * 
	 * @return a hash code for this {@code Vector2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y));
	}
	
	/**
	 * Writes this {@code Vector2D} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Vector2D} instance with the absolute component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the absolute component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D absolute(final Vector2D v) {
		return new Vector2D(Doubles.abs(v.x), Doubles.abs(v.y));
	}
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2D add(final Vector2D vLHS, final Vector2D vRHS) {
		return new Vector2D(vLHS.x + vRHS.x, vLHS.y + vRHS.y);
	}
	
	/**
	 * Adds the component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the addition.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param a a {@code Vector2D} instance
	 * @param b a {@code Vector2D} instance
	 * @param c a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector2D add(final Vector2D a, final Vector2D b, final Vector2D c) {
		return new Vector2D(a.x + b.x + c.x, a.y + b.y + c.y);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2D direction(final Point2D eye, final Point2D lookAt) {
		return new Vector2D(lookAt.x - eye.x, lookAt.y - eye.y);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2D directionNormalized(final Point2D eye, final Point2D lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code p.x} and {@code p.y}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2D directionXY(final Point3D p) {
		return new Vector2D(p.x, p.y);
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code p.y} and {@code p.z}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2D directionYZ(final Point3D p) {
		return new Vector2D(p.y, p.z);
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code p.z} and {@code p.x}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code p.z} and {@code p.x}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Vector2D directionZX(final Point3D p) {
		return new Vector2D(p.z, p.x);
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2D divide(final Vector2D vLHS, final double sRHS) {
		return new Vector2D(Doubles.finiteOrDefault(vLHS.x / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.y / sRHS, 0.0D));
	}
	
	/**
	 * Returns a cached version of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a cached version of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D getCached(final Vector2D v) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(v, "v == null"), key -> v);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector2D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector2D} instance
	 * @param b a {@code Vector2D} instance
	 * @param t the factor
	 * @return a {@code Vector2D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector2D lerp(final Vector2D a, final Vector2D b, final double t) {
		return new Vector2D(Doubles.lerp(a.x, b.x, t), Doubles.lerp(a.y, b.y, t));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector2D multiply(final Vector2D vLHS, final double sRHS) {
		return new Vector2D(vLHS.x * sRHS, vLHS.y * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D negate(final Vector2D v) {
		return new Vector2D(-v.x, -v.y);
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D normalize(final Vector2D v) {
		final double length = v.length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
	}
	
	/**
	 * Returns a {@code Vector2D} instance that is perpendicular to {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a {@code Vector2D} instance that is perpendicular to {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D perpendicular(final Vector2D v) {
		return new Vector2D(v.y, -v.x);
	}
	
	/**
	 * Returns a random {@code Vector2D} instance.
	 * 
	 * @return a random {@code Vector2D} instance
	 */
	public static Vector2D random() {
		return new Vector2D(Randoms.nextDouble() * 2.0D - 1.0D, Randoms.nextDouble() * 2.0D - 1.0D);
	}
	
	/**
	 * Returns a random and normalized {@code Vector2D} instance.
	 * 
	 * @return a random and normalized {@code Vector2D} instance
	 */
	public static Vector2D randomNormalized() {
		return normalize(random());
	}
	
	/**
	 * Returns a new {@code Vector2D} instance with the reciprocal (or inverse) component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the reciprocal (or inverse) component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector2D reciprocal(final Vector2D v) {
		return new Vector2D(1.0D / v.x, 1.0D / v.y);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector2D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector2D read(final DataInput dataInput) {
		try {
			return new Vector2D(dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2D subtract(final Vector2D vLHS, final Vector2D vRHS) {
		return new Vector2D(vLHS.x - vRHS.x, vLHS.y - vRHS.y);
	}
	
	/**
	 * Transforms the {@code Vector2D} {@code vRHS} with the {@link Matrix33D} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33D} instance
	 * @param vRHS a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2D transform(final Matrix33D mLHS, final Vector2D vRHS) {
		final double x = mLHS.element11 * vRHS.x + mLHS.element12 * vRHS.y;
		final double y = mLHS.element21 * vRHS.x + mLHS.element22 * vRHS.y;
		
		return new Vector2D(x, y);
	}
	
	/**
	 * Transforms the {@code Vector2D} {@code vRHS} with the {@link Matrix33D} {@code mLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33D} instance
	 * @param vRHS a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector2D transformTranspose(final Matrix33D mLHS, final Vector2D vRHS) {
		final double x = mLHS.element11 * vRHS.x + mLHS.element21 * vRHS.y;
		final double y = mLHS.element12 * vRHS.x + mLHS.element22 * vRHS.y;
		
		return new Vector2D(x, y);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}
	 */
	public static Vector2D x() {
		return x(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(x, 0.0D)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(x, 0.0D)}
	 */
	public static Vector2D x(final double x) {
		return new Vector2D(x, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}
	 */
	public static Vector2D y() {
		return y(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, y)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, y)}
	 */
	public static Vector2D y(final double y) {
		return new Vector2D(0.0D, y);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector2D vLHS, final Vector2D vRHS) {
		return Doubles.isZero(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the cross product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return the cross product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static double crossProduct(final Vector2D vLHS, final Vector2D vRHS) {
		return vLHS.x * vRHS.y - vLHS.y * vRHS.x;
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static double dotProduct(final Vector2D vLHS, final Vector2D vRHS) {
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