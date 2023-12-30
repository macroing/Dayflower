/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Quaternion4D} represents a quaternion with four {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Quaternion4D implements Node {
	/**
	 * The W-component of this {@code Quaternion4D} instance.
	 */
	public final double w;
	
	/**
	 * The X-component of this {@code Quaternion4D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Quaternion4D} instance.
	 */
	public final double y;
	
	/**
	 * The Z-component of this {@code Quaternion4D} instance.
	 */
	public final double z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code 0.0D}, {@code 0.0D}, {@code 0.0D} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Quaternion4D() {
		this(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code 1.0D}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(v.x, v.y, v.z);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Quaternion4D(final Vector3D v) {
		this(v.x, v.y, v.z);
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Quaternion4D(x, y, z, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Quaternion4D(final double x, final double y, final double z) {
		this(x, y, z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Quaternion4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Quaternion4D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Quaternion4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Quaternion4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Quaternion4D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
	}
	
	/**
	 * Compares {@code object} to this {@code Quaternion4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Quaternion4D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Quaternion4D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Quaternion4D)) {
			return false;
		} else if(!Doubles.equals(this.x, Quaternion4D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Quaternion4D.class.cast(object).y)) {
			return false;
		} else if(!Doubles.equals(this.z, Quaternion4D.class.cast(object).z)) {
			return false;
		} else if(!Doubles.equals(this.w, Quaternion4D.class.cast(object).w)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the length of this {@code Quaternion4D} instance.
	 * 
	 * @return the length of this {@code Quaternion4D} instance
	 */
	public double length() {
		return Doubles.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Quaternion4D} instance.
	 * 
	 * @return the squared length of this {@code Quaternion4D} instance
	 */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	/**
	 * Returns a hash code for this {@code Quaternion4D} instance.
	 * 
	 * @return a hash code for this {@code Quaternion4D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), Double.valueOf(this.w));
	}
	
	/**
	 * Writes this {@code Quaternion4D} instance to {@code dataOutput}.
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
	 * Adds the component values of {@code qRHS} to the component values of {@code qLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the addition.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion addition is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4D add(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		return new Quaternion4D(qLHS.x + qRHS.x, qLHS.y + qRHS.y, qLHS.z + qRHS.z, qLHS.w + qRHS.w);
	}
	
	/**
	 * Conjugates the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the conjugation.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the conjugation
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4D conjugate(final Quaternion4D q) {
		return new Quaternion4D(-q.x, -q.y, -q.z, q.w);
	}
	
	/**
	 * Divides the component values of {@code qLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the division.
	 * <p>
	 * If {@code qLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion division is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code qLHS} is {@code null}
	 */
	public static Quaternion4D divide(final Quaternion4D qLHS, final double sRHS) {
		return new Quaternion4D(qLHS.x / sRHS, qLHS.y / sRHS, qLHS.z / sRHS, qLHS.w / sRHS);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on an {@link AngleD} instance and a {@link Vector3D} instance.
	 * <p>
	 * If either {@code a} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a an {@code AngleD} instance
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Quaternion4D} instance based on an {@code AngleD} instance and a {@code Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final AngleD a, final Vector3D v) {
		final AngleD aHalf = AngleD.half(a);
		
		final double sin = Doubles.sin(aHalf.getRadians());
		final double cos = Doubles.cos(aHalf.getRadians());
		
		final double x = v.x * sin;
		final double y = v.y * sin;
		final double z = v.z * sin;
		final double w = cos;
		
		return new Quaternion4D(x, y, z, w);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on a {@link Matrix44D} instance.
	 * <p>
	 * If {@code m} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param m a {@code Matrix44D} instance
	 * @return a new {@code Quaternion4D} instance based on a {@code Matrix44D} instance
	 * @throws NullPointerException thrown if, and only if, {@code m} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final Matrix44D m) {
		final double element11 = m.element11;
		final double element12 = m.element12;
		final double element13 = m.element13;
		final double element21 = m.element21;
		final double element22 = m.element22;
		final double element23 = m.element23;
		final double element31 = m.element31;
		final double element32 = m.element32;
		final double element33 = m.element33;
		
		if(element11 + element22 + element33 > 0.0D) {
			final double scalar = 0.5D / Doubles.sqrt(element11 + element22 + element33 + 1.0D);
			
			final double x = (element23 - element32) * scalar;
			final double y = (element31 - element13) * scalar;
			final double z = (element12 - element21) * scalar;
			final double w = 0.25D / scalar;
			
			return normalize(new Quaternion4D(x, y, z, w));
		} else if(element11 > element22 && element11 > element33) {
			final double scalar = 2.0D * Doubles.sqrt(1.0D + element11 - element22 - element23);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double x = 0.25D * scalar;
			final double y = (element21 + element12) * scalarReciprocal;
			final double z = (element31 + element13) * scalarReciprocal;
			final double w = (element23 - element32) * scalarReciprocal;
			
			return normalize(new Quaternion4D(x, y, z, w));
		} else if(element22 > element33) {
			final double scalar = 2.0D * Doubles.sqrt(1.0D + element22 - element11 - element33);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double x = (element21 + element12) * scalarReciprocal;
			final double y = 0.25D * scalar;
			final double z = (element32 + element23) * scalarReciprocal;
			final double w = (element31 - element13) * scalarReciprocal;
			
			return normalize(new Quaternion4D(x, y, z, w));
		} else {
			final double scalar = 2.0F * Doubles.sqrt(1.0D + element33 - element11 - element22);
			final double scalarReciprocal = 1.0D / scalar;
			
			final double x = (element31 + element13) * scalarReciprocal;
			final double y = (element23 + element32) * scalarReciprocal;
			final double z = 0.25D * scalar;
			final double w = (element12 - element21) * scalarReciprocal;
			
			return normalize(new Quaternion4D(x, y, z, w));
		}
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance based on the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.from(Matrix44D.rotate(new OrthonormalBasis33D(direction)));
	 * }
	 * </pre>
	 * 
	 * @param direction a {@link Vector3D} instance
	 * @return a new {@code Quaternion4D} instance based on the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D from(final Vector3D direction) {
		return from(Matrix44D.rotate(new OrthonormalBasis33D(direction)));
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationNormalized(qLHS, qRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		return linearInterpolationNormalized(qLHS, qRHS, 0.5D);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationNormalized(qLHS, qRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D qLHS, final Quaternion4D qRHS, final double t) {
		return linearInterpolationNormalized(qLHS, qRHS, t, false);
	}
	
	/**
	 * Performs a normalized linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationNormalized(final Quaternion4D qLHS, final Quaternion4D qRHS, final double t, final boolean isInterpolatingShortest) {
		return normalize(add(multiply(subtract(isInterpolatingShortest && dotProduct(qLHS, qRHS) < 0.0D ? negate(qRHS) : qRHS, qLHS), t), qLHS));
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationSpherical(qLHS, qRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		return linearInterpolationSpherical(qLHS, qRHS, 0.5D);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Quaternion4D.linearInterpolationSpherical(qLHS, qRHS, t, false);
	 * }
	 * </pre>
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D qLHS, final Quaternion4D qRHS, final double t) {
		return linearInterpolationSpherical(qLHS, qRHS, t, false);
	}
	
	/**
	 * Performs a spherical linear interpolation between {@code qLHS} and {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the operation.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @param t the factor
	 * @param isInterpolatingShortest {@code true} if, and only if, the shortest interpolation should be used, {@code false} otherwise
	 * @return a new {@code Quaternion4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Quaternion4D linearInterpolationSpherical(final Quaternion4D qLHS, final Quaternion4D qRHS, final double t, final boolean isInterpolatingShortest) {
		final double cos = dotProduct(qLHS, qRHS);
		
		final double x = isInterpolatingShortest && cos < 0.0D ? -cos : cos;
		final double y = Doubles.sqrt(1.0D - x * x);
		
		final Quaternion4D quaternion1 = isInterpolatingShortest && cos < 0.0D ? negate(qRHS) : qRHS;
		
		if(Doubles.abs(x) >= 1.0D - 1000.0D) {
			return linearInterpolationNormalized(qLHS, quaternion1, t);
		}
		
		final double theta = Doubles.atan2(y, x);
		
		return add(multiply(qLHS, Doubles.sin((1.0D - t) * theta) / y), multiply(quaternion1, Doubles.sin(t * theta) / y));
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with the component values of {@code qRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		final double x = qLHS.x * qRHS.w + qLHS.w * qRHS.x + qLHS.y * qRHS.z - qLHS.z * qRHS.y;
		final double y = qLHS.y * qRHS.w + qLHS.w * qRHS.y + qLHS.z * qRHS.x - qLHS.x * qRHS.z;
		final double z = qLHS.z * qRHS.w + qLHS.w * qRHS.z + qLHS.x * qRHS.y - qLHS.y * qRHS.x;
		final double w = qLHS.w * qRHS.w - qLHS.x * qRHS.x - qLHS.y * qRHS.y - qLHS.z * qRHS.z;
		
		return new Quaternion4D(x, y, z, w);
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with the component values of {@code vRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code qLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param vRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code vRHS} are {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D qLHS, final Vector3D vRHS) {
		final double x = +qLHS.w * vRHS.x + qLHS.y * vRHS.z - qLHS.z * vRHS.y;
		final double y = +qLHS.w * vRHS.y + qLHS.z * vRHS.x - qLHS.x * vRHS.z;
		final double z = +qLHS.w * vRHS.z + qLHS.x * vRHS.y - qLHS.y * vRHS.x;
		final double w = -qLHS.x * vRHS.x - qLHS.y * vRHS.y - qLHS.z * vRHS.z;
		
		return new Quaternion4D(x, y, z, w);
	}
	
	/**
	 * Multiplies the component values of {@code qLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the multiplication.
	 * <p>
	 * If {@code qLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion multiplication is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code qLHS} is {@code null}
	 */
	public static Quaternion4D multiply(final Quaternion4D qLHS, final double sRHS) {
		return new Quaternion4D(qLHS.x * sRHS, qLHS.y * sRHS, qLHS.z * sRHS, qLHS.w * sRHS);
	}
	
	/**
	 * Negates the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the negation.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4D negate(final Quaternion4D q) {
		return new Quaternion4D(-q.x, -q.y, -q.z, -q.w);
	}
	
	/**
	 * Normalizes the component values of {@code q}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the normalization.
	 * <p>
	 * If {@code q} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@code Quaternion4D} instance
	 * @return a new {@code Quaternion4D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code q} is {@code null}
	 */
	public static Quaternion4D normalize(final Quaternion4D q) {
		final double length = q.length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return q;
		}
		
		return divide(q, length);
	}
	
	/**
	 * Returns a new {@code Quaternion4D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Quaternion4D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Quaternion4D read(final DataInput dataInput) {
		try {
			return new Quaternion4D(dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code qRHS} from the component values of {@code qLHS}.
	 * <p>
	 * Returns a new {@code Quaternion4D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Quaternion subtraction is performed componentwise.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return a new {@code Quaternion4D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static Quaternion4D subtract(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		return new Quaternion4D(qLHS.x - qRHS.x, qLHS.y - qRHS.y, qLHS.z - qRHS.z, qLHS.w - qRHS.w);
	}
	
	/**
	 * Returns the dot product of {@code qLHS} and {@code qRHS}.
	 * <p>
	 * If either {@code qLHS} or {@code qRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param qLHS the {@code Quaternion4D} instance on the left-hand side
	 * @param qRHS the {@code Quaternion4D} instance on the right-hand side
	 * @return the dot product of {@code qLHS} and {@code qRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code qLHS} or {@code qRHS} are {@code null}
	 */
	public static double dotProduct(final Quaternion4D qLHS, final Quaternion4D qRHS) {
		return qLHS.x * qRHS.x + qLHS.y * qRHS.y + qLHS.z * qRHS.z + qLHS.w * qRHS.w;
	}
}