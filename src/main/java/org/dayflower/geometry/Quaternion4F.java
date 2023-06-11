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
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Quaternion4F} represents a quaternion with four {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Quaternion4F implements Node {
	/**
	 * The W-component of this {@code Quaternion4F} instance.
	 */
	public final float w;
	
	/**
	 * The X-component of this {@code Quaternion4F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Quaternion4F} instance.
	 */
	public final float y;
	
	/**
	 * The Z-component of this {@code Quaternion4F} instance.
	 */
	public final float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code 0.0F}, {@code 0.0F}, {@code 0.0F} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Quaternion4F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code 1.0F}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(v.x, v.y, v.z);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Quaternion4F(final Vector3F v) {
		this(v.x, v.y, v.z);
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4F(x, y, z, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Quaternion4F(final float x, final float y, final float z) {
		this(x, y, z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Quaternion4F(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Quaternion4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Quaternion4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Quaternion4F(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
	}
	
	/**
	 * Compares {@code object} to this {@code Quaternion4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Quaternion4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Quaternion4F)) {
			return false;
		} else if(!Floats.equals(this.x, Quaternion4F.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, Quaternion4F.class.cast(object).y)) {
			return false;
		} else if(!Floats.equals(this.z, Quaternion4F.class.cast(object).z)) {
			return false;
		} else if(!Floats.equals(this.w, Quaternion4F.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the length of this {@code Quaternion4F} instance.
	 * 
	 * @return the length of this {@code Quaternion4F} instance
	 */
	public float length() {
		return Floats.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Quaternion4F} instance.
	 * 
	 * @return the squared length of this {@code Quaternion4F} instance
	 */
	public float lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	/**
	 * Returns a hash code for this {@code Quaternion4F} instance.
	 * 
	 * @return a hash code for this {@code Quaternion4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z), Float.valueOf(this.w));
	}
	
	/**
	 * Writes this {@code Quaternion4F} instance to {@code dataOutput}.
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
	 * Adds the component values of {@code qRHS} to the component values of {@code qLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the addition.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion addition is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4F add(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		return new Quaternion4F(qLHS.x + qRHS.x, qLHS.y + qRHS.y, qLHS.z + qRHS.z, qLHS.w + qRHS.w);
	}
	
	/**
	 * Conjugates the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the conjugation.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4F} instance
	 * @return a new {@code Quaternion4F} instance with the result of the conjugation
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4F conjugate(final Quaternion4F q) {
		return new Quaternion4F(-q.x, -q.y, -q.z, q.w);
	}
	
	/**
	 * Divides the component values of {@code qLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the division.
	 * <p>
	 * If {@code qLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion division is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code qLHS} is {@code null}
	 */
	public static Quaternion4F divide(final Quaternion4F qLHS, final float sRHS) {
		return new Quaternion4F(qLHS.x / sRHS, qLHS.y / sRHS, qLHS.z / sRHS, qLHS.w / sRHS);
	}
	
	/**
	 * Returns a new {@code Quaternion4F} instance based on an {@link AngleF} instance and a {@link Vector3F} instance.
	 * <p>
	 * If either {@code a} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a an {@code AngleF} instance
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Quaternion4F} instance based on an {@code AngleF} instance and a {@code Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F from(final AngleF a, final Vector3F v) {
		final AngleF aHalf = AngleF.half(a);
		
		final float sin = Floats.sin(aHalf.getRadians());
		final float cos = Floats.cos(aHalf.getRadians());
		
		final float x = v.x * sin;
		final float y = v.y * sin;
		final float z = v.z * sin;
		final float w = cos;
		
		return new Quaternion4F(x, y, z, w);
	}
	
	/**
	 * Returns a new {@code Quaternion4F} instance based on a {@link Matrix44F} instance.
	 * <p>
	 * If {@code m} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param m a {@code Matrix44F} instance
	 * @return a new {@code Quaternion4F} instance based on a {@code Matrix44F} instance
	 * @throws NullPointerException thrown if, and only if, {@code m} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F from(final Matrix44F m) {
		final float element11 = m.element11;
		final float element12 = m.element12;
		final float element13 = m.element13;
		final float element21 = m.element21;
		final float element22 = m.element22;
		final float element23 = m.element23;
		final float element31 = m.element31;
		final float element32 = m.element32;
		final float element33 = m.element33;
		
		if(element11 + element22 + element33 > 0.0F) {
			final float scalar = 0.5F / Floats.sqrt(element11 + element22 + element33 + 1.0F);
			
			final float x = (element23 - element32) * scalar;
			final float y = (element31 - element13) * scalar;
			final float z = (element12 - element21) * scalar;
			final float w = 0.25F / scalar;
			
			return normalize(new Quaternion4F(x, y, z, w));
		} else if(element11 > element22 && element11 > element33) {
			final float scalar = 2.0F * Floats.sqrt(1.0F + element11 - element22 - element23);
			final float scalarReciprocal = 1.0F / scalar;
			
			final float x = 0.25F * scalar;
			final float y = (element21 + element12) * scalarReciprocal;
			final float z = (element31 + element13) * scalarReciprocal;
			final float w = (element23 - element32) * scalarReciprocal;
			
			return normalize(new Quaternion4F(x, y, z, w));
		} else if(element22 > element33) {
			final float scalar = 2.0F * Floats.sqrt(1.0F + element22 - element11 - element33);
			final float scalarReciprocal = 1.0F / scalar;
			
			final float x = (element21 + element12) * scalarReciprocal;
			final float y = 0.25F * scalar;
			final float z = (element32 + element23) * scalarReciprocal;
			final float w = (element31 - element13) * scalarReciprocal;
			
			return normalize(new Quaternion4F(x, y, z, w));
		} else {
			final float scalar = 2.0F * Floats.sqrt(1.0F + element33 - element11 - element22);
			final float scalarReciprocal = 1.0F / scalar;
			
			final float x = (element31 + element13) * scalarReciprocal;
			final float y = (element23 + element32) * scalarReciprocal;
			final float z = 0.25F * scalar;
			final float w = (element12 - element21) * scalarReciprocal;
			
			return normalize(new Quaternion4F(x, y, z, w));
		}
	}
	
	/**
	 * Returns a new {@code Quaternion4F} instance based on the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4F.from(Matrix44F.rotate(new OrthonormalBasis33F(direction)));
	 * }
	 * </pre>
	 * 
	 * @param direction a {@link Vector3F} instance
	 * @return a new {@code Quaternion4F} instance based on the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F from(final Vector3F direction) {
		return from(Matrix44F.rotate(new OrthonormalBasis33F(direction)));
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4F.linearInterpolationNormalized(qLHS, qRHS, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationNormalized(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		return linearInterpolationNormalized(qLHS, qRHS, 0.5F);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4F.linearInterpolationNormalized(qLHS, qRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationNormalized(final Quaternion4F qLHS, final Quaternion4F qRHS, final float t) {
		return linearInterpolationNormalized(qLHS, qRHS, t, false);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationNormalized(final Quaternion4F qLHS, final Quaternion4F qRHS, final float t, final boolean isInterpolatingShortest) {
		return normalize(add(multiply(subtract(isInterpolatingShortest && dotProduct(qLHS, qRHS) < 0.0F ? negate(qRHS) : qRHS, qLHS), t), qLHS));
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4F.linearInterpolationSpherical(qLHS, qRHS, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationSpherical(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		return linearInterpolationSpherical(qLHS, qRHS, 0.5F);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4F.linearInterpolationSpherical(qLHS, qRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationSpherical(final Quaternion4F qLHS, final Quaternion4F qRHS, final float t) {
		return linearInterpolationSpherical(qLHS, qRHS, t, false);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4F linearInterpolationSpherical(final Quaternion4F qLHS, final Quaternion4F qRHS, final float t, final boolean isInterpolatingShortest) {
		final float cos = dotProduct(qLHS, qRHS);
		
		final float x = isInterpolatingShortest && cos < 0.0F ? -cos : cos;
		final float y = Floats.sqrt(1.0F - x * x);
		
		final Quaternion4F quaternion1 = isInterpolatingShortest && cos < 0.0F ? negate(qRHS) : qRHS;
		
		if(Floats.abs(x) >= 1.0F - 1000.0F) {
			return linearInterpolationNormalized(qLHS, quaternion1, t);
		}
		
		final float theta = Floats.atan2(y, x);
		
		return add(multiply(qLHS, Floats.sin((1.0F - t) * theta) / y), multiply(quaternion1, Floats.sin(t * theta) / y));
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with the component values of {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		final float x = qLHS.x * qRHS.w + qLHS.w * qRHS.x + qLHS.y * qRHS.z - qLHS.z * qRHS.y;
		final float y = qLHS.y * qRHS.w + qLHS.w * qRHS.y + qLHS.z * qRHS.x - qLHS.x * qRHS.z;
		final float z = qLHS.z * qRHS.w + qLHS.w * qRHS.z + qLHS.x * qRHS.y - qLHS.y * qRHS.x;
		final float w = qLHS.w * qRHS.w - qLHS.x * qRHS.x - qLHS.y * qRHS.y - qLHS.z * qRHS.z;
		
		return new Quaternion4F(x, y, z, w);
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with the component values of {@code vRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code qLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code vRHS} are {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F qLHS, final Vector3F vRHS) {
		final float x = +qLHS.w * vRHS.x + qLHS.y * vRHS.z - qLHS.z * vRHS.y;
		final float y = +qLHS.w * vRHS.y + qLHS.z * vRHS.x - qLHS.x * vRHS.z;
		final float z = +qLHS.w * vRHS.z + qLHS.x * vRHS.y - qLHS.y * vRHS.x;
		final float w = -qLHS.x * vRHS.x - qLHS.y * vRHS.y - qLHS.z * vRHS.z;
		
		return new Quaternion4F(x, y, z, w);
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the multiplication.
	 * <p>
	 * If {@code qLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code qLHS} is {@code null}
	 */
	public static Quaternion4F multiply(final Quaternion4F qLHS, final float sRHS) {
		return new Quaternion4F(qLHS.x * sRHS, qLHS.y * sRHS, qLHS.z * sRHS, qLHS.w * sRHS);
	}
	
	/**
	 * Negates the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the negation.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4F} instance
	 * @return a new {@code Quaternion4F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4F negate(final Quaternion4F q) {
		return new Quaternion4F(-q.x, -q.y, -q.z, -q.w);
	}
	
	/**
	 * Normalizes the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the normalization.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4F} instance
	 * @return a new {@code Quaternion4F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4F normalize(final Quaternion4F q) {
		final float length = q.length();
		
		final boolean isLengthGTEThreshold = length >= Floats.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Floats.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return q;
		}
		
		return divide(q, length);
	}
	
	/**
	 * Returns a new {@code Quaternion4F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Quaternion4F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Quaternion4F read(final DataInput dataInput) {
		try {
			return new Quaternion4F(dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code qRHS} from the component values of {@code qLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion subtraction is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return a new {@code Quaternion4F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4F subtract(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		return new Quaternion4F(qLHS.x - qRHS.x, qLHS.y - qRHS.y, qLHS.z - qRHS.z, qLHS.w - qRHS.w);
	}
	
	/**
	 * Returns the dot product of {@code qLHS} and {@code qRHS}.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4F} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4F} instance on the right-hand side
	 * @return the dot product of {@code qLHS} and {@code qRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static float dotProduct(final Quaternion4F qLHS, final Quaternion4F qRHS) {
		return qLHS.x * qRHS.x + qLHS.y * qRHS.y + qLHS.z * qRHS.z + qLHS.w * qRHS.w;
	}
}