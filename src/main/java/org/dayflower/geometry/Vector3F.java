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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Vector3F} represents a vector with three {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector3F implements Node {
	/**
	 * A {@code Vector3F} instance given the component values {@code Float.NaN}, {@code Float.NaN} and {@code Float.NaN}.
	 */
	public static final Vector3F NaN = new Vector3F(Float.NaN, Float.NaN, Float.NaN);
	
	/**
	 * A {@code Vector3F} instance given the component values {@code 0.0F}, {@code 0.0F} and {@code 0.0F}.
	 */
	public static final Vector3F ZERO = new Vector3F(0.0F, 0.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector3F, Vector3F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Vector3F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Vector3F} instance.
	 */
	public final float y;
	
	/**
	 * The Z-component of this {@code Vector3F} instance.
	 */
	public final float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector3F} instance given the component values {@code 0.0F}, {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3F(0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Vector3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Vector3F} instance given the component values {@code p.x}, {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3F(p.x, p.y, p.z);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector3F(final Point3F p) {
		this(p.x, p.y, p.z);
	}
	
	/**
	 * Constructs a new {@code Vector3F} instance given the component values {@code component}, {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Vector3F(final float component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Vector3F} instance given the component values {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Vector3F(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector3F(%s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector3F)) {
			return false;
		} else if(!Floats.equals(this.x, Vector3F.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, Vector3F.class.cast(object).y)) {
			return false;
		} else if(!Floats.equals(this.z, Vector3F.class.cast(object).z)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector3F} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector3F} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final float length = length();
		
		final boolean isLengthGTEThreshold = length >= Floats.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Floats.NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
	}
	
	/**
	 * Returns the cosine of the angle phi.
	 * 
	 * @return the cosine of the angle phi
	 */
	public float cosPhi() {
		final float sinTheta = sinTheta();
		
		if(Floats.isZero(sinTheta)) {
			return 1.0F;
		}
		
		return Floats.saturate(this.x / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the cosine of the angle phi in squared form.
	 * 
	 * @return the cosine of the angle phi in squared form
	 */
	public float cosPhiSquared() {
		return cosPhi() * cosPhi();
	}
	
	/**
	 * Returns the cosine of the angle theta.
	 * 
	 * @return the cosine of the angle theta
	 */
	public float cosTheta() {
		return this.z;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @return the cosine of the angle theta in absolute form
	 */
	public float cosThetaAbs() {
		return Floats.abs(cosTheta());
	}
	
	/**
	 * Returns the cosine of the angle theta in quartic form.
	 * 
	 * @return the cosine of the angle theta in quartic form
	 */
	public float cosThetaQuartic() {
		return cosThetaSquared() * cosThetaSquared();
	}
	
	/**
	 * Returns the cosine of the angle theta in squared form.
	 * 
	 * @return the cosine of the angle theta in squared form
	 */
	public float cosThetaSquared() {
		return cosTheta() * cosTheta();
	}
	
	/**
	 * Returns the component at {@code index}.
	 * <p>
	 * If {@code index < 0 || index >= 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the component
	 * @return the component at {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index < 0 || index >= 3}
	 */
//	TODO: Add Unit Tests!
	public float getComponentAt(final int index) {
		switch(index) {
			case 0:
				return this.x;
			case 1:
				return this.y;
			case 2:
				return this.z;
			default:
				throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Returns the length of this {@code Vector3F} instance.
	 * 
	 * @return the length of this {@code Vector3F} instance
	 */
	public float length() {
		return Floats.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector3F} instance.
	 * 
	 * @return the squared length of this {@code Vector3F} instance
	 */
	public float lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @return the sine of the angle phi
	 */
	public float sinPhi() {
		final float sinTheta = sinTheta();
		
		if(Floats.isZero(sinTheta)) {
			return 0.0F;
		}
		
		return Floats.saturate(this.y / sinTheta, -1.0F, 1.0F);
	}
	
	/**
	 * Returns the sine of the angle phi in squared form.
	 * 
	 * @return the sine of the angle phi in squared form
	 */
	public float sinPhiSquared() {
		return sinPhi() * sinPhi();
	}
	
	/**
	 * Returns the sine of the angle theta.
	 * 
	 * @return the sine of the angle theta
	 */
	public float sinTheta() {
		return Floats.sqrt(sinThetaSquared());
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @return the sine of the angle theta in squared form
	 */
	public float sinThetaSquared() {
		return Floats.max(0.0F, 1.0F - cosThetaSquared());
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @return the spherical phi angle
	 */
//	TODO: Add Unit Tests!
	public float sphericalPhi() {
		return Floats.addLessThan(Floats.atan2(this.y, this.x), 0.0F, Floats.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @return the spherical theta angle
	 */
//	TODO: Add Unit Tests!
	public float sphericalTheta() {
		return Floats.acos(Floats.saturate(this.z, -1.0F, 1.0F));
	}
	
	/**
	 * Returns the tangent of the angle theta.
	 * 
	 * @return the tangent of the angle theta
	 */
	public float tanTheta() {
		return sinTheta() / cosTheta();
	}
	
	/**
	 * Returns the tangent of the angle theta in absolute form.
	 * 
	 * @return the tangent of the angle theta in absolute form
	 */
	public float tanThetaAbs() {
		return Floats.abs(tanTheta());
	}
	
	/**
	 * Returns the tangent of the angle theta in squared form.
	 * 
	 * @return the tangent of the angle theta in squared form
	 */
	public float tanThetaSquared() {
		return sinThetaSquared() / cosThetaSquared();
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector3F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y, this.z};
	}
	
	/**
	 * Returns a hash code for this {@code Vector3F} instance.
	 * 
	 * @return a hash code for this {@code Vector3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Vector3F} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an optional {@code Vector3F} instance that represents the refraction of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction the {@code Vector3F} instance that will be refracted with regards to {@code normal}
	 * @param normal the {@code Vector3F} instance that represents the normal of the surface
	 * @param eta the index of refraction
	 * @return an optional {@code Vector3F} instance that represents the refraction of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
	public static Optional<Vector3F> refraction(final Vector3F direction, final Vector3F normal, final float eta) {
		final float cosThetaI = dotProduct(direction, normal);
		final float sinThetaISquared = Floats.max(0.0F, 1.0F - cosThetaI * cosThetaI);
		final float sinThetaTSquared = eta * eta * sinThetaISquared;
		
		if(sinThetaTSquared >= 1.0F) {
			return Optional.empty();
		}
		
		final float cosThetaT = Floats.sqrt(1.0F - sinThetaTSquared);
		
		return Optional.of(add(multiply(negate(direction), eta), multiply(normal, eta * cosThetaI - cosThetaT)));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance with the absolute component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the absolute component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F absolute(final Vector3F v) {
		return new Vector3F(Floats.abs(v.x), Floats.abs(v.y), Floats.abs(v.z));
	}
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F add(final Vector3F vLHS, final Vector3F vRHS) {
		return new Vector3F(vLHS.x + vRHS.x, vLHS.y + vRHS.y, vLHS.z + vRHS.z);
	}
	
	/**
	 * Adds the component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the addition.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param a a {@code Vector3F} instance
	 * @param b a {@code Vector3F} instance
	 * @param c a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3F add(final Vector3F a, final Vector3F b, final Vector3F c) {
		return new Vector3F(a.x + b.x + c.x, a.y + b.y + c.y, a.z + b.z + c.z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance denoting {@code V} in an orthonormal basis.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param w the {@code Vector3F} instance that denotes {@code W} in an orthonormal basis
	 * @return a new {@code Vector3F} instance denoting {@code V} in an orthonormal basis
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public static Vector3F computeV(final Vector3F w) {
		final Vector3F wNormalized = normalize(w);
		
		final float absWNormalizedComponent1 = Floats.abs(wNormalized.x);
		final float absWNormalizedComponent2 = Floats.abs(wNormalized.y);
		final float absWNormalizedComponent3 = Floats.abs(wNormalized.z);
		
		if(absWNormalizedComponent1 < absWNormalizedComponent2 && absWNormalizedComponent1 < absWNormalizedComponent3) {
			return normalize(new Vector3F(0.0F, wNormalized.z, -wNormalized.y));
		}
		
		if(absWNormalizedComponent2 < absWNormalizedComponent3) {
			return normalize(new Vector3F(wNormalized.z, 0.0F, -wNormalized.x));
		}
		
		return normalize(new Vector3F(wNormalized.y, -wNormalized.x, 0.0F));
	}
	
	/**
	 * Computes the cross product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the operation.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F crossProduct(final Vector3F vLHS, final Vector3F vRHS) {
		return new Vector3F(vLHS.y * vRHS.z - vLHS.z * vRHS.y, vLHS.z * vRHS.x - vLHS.x * vRHS.z, vLHS.x * vRHS.y - vLHS.y * vRHS.x);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} instance denoting the eye to look from
	 * @param lookAt a {@code Point3F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3F direction(final Point3F eye, final Point3F lookAt) {
		return new Vector3F(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point4F} instance denoting the eye to look from
	 * @param lookAt a {@code Point4F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3F direction(final Point4F eye, final Point4F lookAt) {
		return new Vector3F(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3F} instance denoting the eye to look from
	 * @param lookAt a {@code Point3F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3F directionNormalized(final Point3F eye, final Point3F lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point4F} instance denoting the eye to look from
	 * @param lookAt a {@code Point4F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3F directionNormalized(final Point4F eye, final Point4F lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F directionSpherical(final float u, final float v) {
		final float phi = u * Floats.PI_MULTIPLIED_BY_2;
		final float theta = v * Floats.PI;
		
		return directionSpherical(Floats.sin(theta), Floats.cos(theta), phi);
		
//		final float cosTheta = 1.0F - 2.0F * u;
//		final float sinTheta = Floats.sqrt(Floats.max(0.0F, 1.0F - cosTheta * cosTheta));
//		final float phi = v * Floats.PI_MULTIPLIED_BY_2;
		
//		return directionSpherical(sinTheta, cosTheta, phi);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}.
	 * <p>
	 * This method is based on PBRT.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 * @return a new {@code Vector3F} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F directionSpherical(final float sinTheta, final float cosTheta, final float phi) {
		return new Vector3F(sinTheta * Floats.cos(phi), sinTheta * Floats.sin(phi), cosTheta);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}, as well as the coordinate system {@code x}, {@code y} and {@code z}.
	 * <p>
	 * If either {@code x}, {@code y} or {@code z} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method is based on PBRT.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 * @param x the X-direction of the coordinate system
	 * @param y the Y-direction of the coordinate system
	 * @param z the Z-direction of the coordinate system
	 * @return a new {@code Vector3F} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}, as well as the coordinate system {@code x}, {@code y} and {@code z}
	 * @throws NullPointerException thrown if, and only if, either {@code x}, {@code y} or {@code z} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F directionSpherical(final float sinTheta, final float cosTheta, final float phi, final Vector3F x, final Vector3F y, final Vector3F z) {
		return add(multiply(x, sinTheta * Floats.cos(phi)), multiply(y, sinTheta * Floats.sin(phi)), multiply(z, cosTheta));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v} and is normalized.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v} and is normalized
	 */
//	TODO: Add Unit Tests!
	public static Vector3F directionSphericalNormalized(final float u, final float v) {
		return normalize(directionSpherical(u, v));
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector3F divide(final Vector3F vLHS, final float sRHS) {
		return new Vector3F(Floats.finiteOrDefault(vLHS.x / sRHS, 0.0F), Floats.finiteOrDefault(vLHS.y / sRHS, 0.0F), Floats.finiteOrDefault(vLHS.z / sRHS, 0.0F));
	}
	
	/**
	 * Returns {@code Vector3F.negate(vLHS)} or {@code vLHS} as {@code Vector3F.dotProduct(vLHS, vRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code Vector3F.negate(vLHS)} or {@code vLHS} as {@code Vector3F.dotProduct(vLHS, vRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F faceForward(final Vector3F vLHS, final Vector3F vRHS) {
		return dotProduct(vLHS, vRHS) < 0.0F ? negate(vLHS) : vLHS;
	}
	
	/**
	 * Returns {@code Vector3F.negate(direction)} or {@code direction} as {@code Vector3F.dotProduct(vLHS, vRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vLHS}, {@code vRHS} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @param direction the {@code Vector3F} instance to potentially negate and then return
	 * @return {@code Vector3F.negate(direction)} or {@code direction} as {@code Vector3F.dotProduct(vLHS, vRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS}, {@code vRHS} or {@code direction} are {@code null}
	 */
	public static Vector3F faceForward(final Vector3F vLHS, final Vector3F vRHS, final Vector3F direction) {
		return dotProduct(vLHS, vRHS) < 0.0F ? negate(direction) : Objects.requireNonNull(direction, "direction == null");
	}
	
	/**
	 * Returns {@code Vector3F.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3F.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F faceForwardZ(final Vector3F vLHS, final Vector3F vRHS) {
		return vLHS.z < 0.0F ? negateZ(vRHS) : Objects.requireNonNull(vRHS, "vRHS == null");
	}
	
	/**
	 * Returns {@code Vector3F.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3F.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F faceForwardZNegated(final Vector3F vLHS, final Vector3F vRHS) {
		return vLHS.z > 0.0F ? negateZ(vRHS) : Objects.requireNonNull(vRHS, "vRHS == null");
	}
	
	/**
	 * Returns {@code Vector3F.negate(vLHS)} or {@code vLHS} as {@code Vector3F.dotProduct(vLHS, vRHS)} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code Vector3F.negate(vLHS)} or {@code vLHS} as {@code Vector3F.dotProduct(vLHS, vRHS)} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F faceForwardNegated(final Vector3F vLHS, final Vector3F vRHS) {
		return dotProduct(vLHS, vRHS) > 0.0F ? negate(vLHS) : vLHS;
	}
	
	/**
	 * Returns a cached version of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a cached version of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F getCached(final Vector3F v) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(v, "v == null"), key -> v);
	}
	
	/**
	 * Returns a {@code Vector3F} instance that contains the Hadamard product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return a {@code Vector3F} instance that contains the Hadamard product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F hadamardProduct(final Vector3F vLHS, final Vector3F vRHS) {
		return new Vector3F(vLHS.x * vRHS.x, vLHS.y * vRHS.y, vLHS.z * vRHS.z);
	}
	
	/**
	 * Returns {@code n} or {@code Vector3F.normalize(Vector3F.subtract(o, i))} as {@code Vector3F.dotProduct(o, i)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@code Vector3F} instance that points in the opposite direction of the ray
	 * @param n a {@code Vector3F} instance that points in the direction of the surface normal
	 * @param i a {@code Vector3F} instance that points in the direction of the light source to the surface intersection point
	 * @return {@code n} or {@code Vector3F.normalize(Vector3F.subtract(o, i))} as {@code Vector3F.dotProduct(o, i)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F half(final Vector3F o, final Vector3F n, final Vector3F i) {
		return dotProduct(o, i) > 0.999F ? n : normalize(subtract(o, i));
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector3F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector3F} instance
	 * @param b a {@code Vector3F} instance
	 * @param t the factor
	 * @return a {@code Vector3F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector3F lerp(final Vector3F a, final Vector3F b, final float t) {
		return new Vector3F(Floats.lerp(a.x, b.x, t), Floats.lerp(a.y, b.y, t), Floats.lerp(a.z, b.z, t));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector3F multiply(final Vector3F vLHS, final float sRHS) {
		return new Vector3F(vLHS.x * sRHS, vLHS.y * sRHS, vLHS.z * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F negate(final Vector3F v) {
		return new Vector3F(-v.x, -v.y, -v.z);
	}
	
	/**
	 * Negates the X-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F negateX(final Vector3F v) {
		return new Vector3F(-v.x, v.y, v.z);
	}
	
	/**
	 * Negates the Y-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F negateY(final Vector3F v) {
		return new Vector3F(v.x, -v.y, v.z);
	}
	
	/**
	 * Negates the Z-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F negateZ(final Vector3F v) {
		return new Vector3F(v.x, v.y, -v.z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance denoting the normal of the plane defined by the {@link Point3F} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @param b a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @param c a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @return a new {@code Vector3F} instance denoting the normal of the plane defined by the {@code Point3F} instances {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3F normal(final Point3F a, final Point3F b, final Point3F c) {
		return crossProduct(directionNormalized(a, b), directionNormalized(a, c));
	}
	
	/**
	 * Computes a normal vector from three other normal vectors via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the interpolated normal.
	 * <p>
	 * If either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This method does not normalize {@code normalA}, {@code normalB} or {@code normalC}.
	 * 
	 * @param normalA a {@code Vector3F} instance denoting the normal of vertex {@code A} of a triangle
	 * @param normalB a {@code Vector3F} instance denoting the normal of vertex {@code B} of a triangle
	 * @param normalC a {@code Vector3F} instance denoting the normal of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3F} instance denoting the Barycentric coordinates
	 * @return a new {@code Vector3F} instance with the interpolated normal
	 * @throws NullPointerException thrown if, and only if, either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}
	 */
	public static Vector3F normal(final Vector3F normalA, final Vector3F normalB, final Vector3F normalC, final Point3F barycentricCoordinates) {
		final float x = normalA.x * barycentricCoordinates.x + normalB.x * barycentricCoordinates.y + normalC.x * barycentricCoordinates.z;
		final float y = normalA.y * barycentricCoordinates.x + normalB.y * barycentricCoordinates.y + normalC.y * barycentricCoordinates.z;
		final float z = normalA.z * barycentricCoordinates.x + normalB.z * barycentricCoordinates.y + normalC.z * barycentricCoordinates.z;
		
		return new Vector3F(x, y, z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance denoting the normalized normal of the plane defined by the {@link Point3F} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @param b a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @param c a {@code Point3F} instance, corresponding to one of the three points in the plane
	 * @return a new {@code Vector3F} instance denoting the normalized normal of the plane defined by the {@code Point3F} instances {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3F normalNormalized(final Point3F a, final Point3F b, final Point3F c) {
		return normalize(normal(a, b, c));
	}
	
	/**
	 * Computes a normalized normal vector from three other normal vectors via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the interpolated and normalized normal.
	 * <p>
	 * If either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This method does not normalize {@code normalA}, {@code normalB} or {@code normalC}.
	 * 
	 * @param normalA a {@code Vector3F} instance denoting the normal of vertex {@code A} of a triangle
	 * @param normalB a {@code Vector3F} instance denoting the normal of vertex {@code B} of a triangle
	 * @param normalC a {@code Vector3F} instance denoting the normal of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3F} instance denoting the Barycentric coordinates
	 * @return a new {@code Vector3F} instance with the interpolated and normalized normal
	 * @throws NullPointerException thrown if, and only if, either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}
	 */
	public static Vector3F normalNormalized(final Vector3F normalA, final Vector3F normalB, final Vector3F normalC, final Point3F barycentricCoordinates) {
		return normalize(normal(normalA, normalB, normalC, barycentricCoordinates));
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F normalize(final Vector3F v) {
		final float length = v.length();
		
		final boolean isLengthGTEThreshold = length >= Floats.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Floats.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
	}
	
	/**
	 * Returns a random {@code Vector3F} instance.
	 * 
	 * @return a random {@code Vector3F} instance
	 */
	public static Vector3F random() {
		return new Vector3F(Randoms.nextFloat() * 2.0F - 1.0F, Randoms.nextFloat() * 2.0F - 1.0F, Randoms.nextFloat() * 2.0F - 1.0F);
	}
	
	/**
	 * Returns a random and normalized {@code Vector3F} instance.
	 * 
	 * @return a random and normalized {@code Vector3F} instance
	 */
	public static Vector3F randomNormalized() {
		return normalize(random());
	}
	
	/**
	 * Returns a new {@code Vector3F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector3F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector3F read(final DataInput dataInput) {
		try {
			return new Vector3F(dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code Vector3F} instance with the reciprocal (or inverse) component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the reciprocal (or inverse) component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F reciprocal(final Vector3F v) {
		return new Vector3F(1.0F / v.x, 1.0F / v.y, 1.0F / v.z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.reflection(direction, normal, false);
	 * }
	 * </pre>
	 * 
	 * @param direction the {@code Vector3F} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3F} instance that represents the normal of the surface
	 * @return a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F reflection(final Vector3F direction, final Vector3F normal) {
		return reflection(direction, normal, false);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isFacingSurface} is {@code true}, it is assumed that {@code direction} is facing the surface. This is usually the case for the direction of a ray that intersects the surface. If {@code isFacingSurface} is {@code false}, it is assumed
	 * that {@code direction} is pointing in the opposite direction. That is, the ray starts at the surface intersection point and is directed away from the surface.
	 * 
	 * @param direction the {@code Vector3F} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3F} instance that represents the normal of the surface
	 * @param isFacingSurface {@code true} if, and only if, {@code direction} is facing the surface, {@code false} otherwise
	 * @return a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F reflection(final Vector3F direction, final Vector3F normal, final boolean isFacingSurface) {
		return isFacingSurface ? subtract(direction, multiply(normal, dotProduct(direction, normal) * 2.0F)) : subtract(multiply(normal, dotProduct(direction, normal) * 2.0F), direction);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.reflectionNormalized(direction, normal, false);
	 * }
	 * </pre>
	 * 
	 * @param direction the {@code Vector3F} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3F} instance that represents the normal of the surface
	 * @return a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F reflectionNormalized(final Vector3F direction, final Vector3F normal) {
		return reflectionNormalized(direction, normal, false);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isFacingSurface} is {@code true}, it is assumed that {@code direction} is facing the surface. This is usually the case for the direction of a ray that intersects the surface. If {@code isFacingSurface} is {@code false}, it is assumed
	 * that {@code direction} is pointing in the opposite direction. That is, the ray starts at the surface intersection point and is directed away from the surface.
	 * 
	 * @param direction the {@code Vector3F} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3F} instance that represents the normal of the surface
	 * @param isFacingSurface {@code true} if, and only if, {@code direction} is facing the surface, {@code false} otherwise
	 * @return a new {@code Vector3F} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F reflectionNormalized(final Vector3F direction, final Vector3F normal, final boolean isFacingSurface) {
		return normalize(reflection(direction, normal, isFacingSurface));
	}
	
	/**
	 * Returns a {@code Vector3F} instance that represents {@code v} rotated by {@code q}.
	 * <p>
	 * If either {@code q} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@link Quaternion4F} instance
	 * @param v a {@code Vector3F} instance
	 * @return a {@code Vector3F} instance that represents {@code v} rotated by {@code q}
	 * @throws NullPointerException thrown if, and only if, either {@code q} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F rotate(final Quaternion4F q, final Vector3F v) {
		final Quaternion4F q0 = Quaternion4F.conjugate(q);
		final Quaternion4F q1 = Quaternion4F.multiply(q, v);
		final Quaternion4F q2 = Quaternion4F.multiply(q1, q0);
		
		return new Vector3F(q2.x, q2.y, q2.z);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3F subtract(final Vector3F vLHS, final Vector3F vRHS) {
		return new Vector3F(vLHS.x - vRHS.x, vLHS.y - vRHS.y, vLHS.z - vRHS.z);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vRHS} with the {@link Matrix44F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param vRHS a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F transform(final Matrix44F mLHS, final Vector3F vRHS) {
		final float x = mLHS.element11 * vRHS.x + mLHS.element12 * vRHS.y + mLHS.element13 * vRHS.z;
		final float y = mLHS.element21 * vRHS.x + mLHS.element22 * vRHS.y + mLHS.element23 * vRHS.z;
		final float z = mLHS.element31 * vRHS.x + mLHS.element32 * vRHS.y + mLHS.element33 * vRHS.z;
		
		return new Vector3F(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vLHS} with the {@link OrthonormalBasis33F} {@code oRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3F} instance
	 * @param oRHS an {@code OrthonormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F transform(final Vector3F vLHS, final OrthonormalBasis33F oRHS) {
		final float x = vLHS.x * oRHS.u.x + vLHS.y * oRHS.v.x + vLHS.z * oRHS.w.x;
		final float y = vLHS.x * oRHS.u.y + vLHS.y * oRHS.v.y + vLHS.z * oRHS.w.y;
		final float z = vLHS.x * oRHS.u.z + vLHS.y * oRHS.v.z + vLHS.z * oRHS.w.z;
		
		return new Vector3F(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vLHS} with the {@link OrthonormalBasis33F} {@code oRHS} in reverse order.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3F} instance
	 * @param oRHS an {@code OrthonormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F transformReverse(final Vector3F vLHS, final OrthonormalBasis33F oRHS) {
		final float x = dotProduct(vLHS, oRHS.u);
		final float y = dotProduct(vLHS, oRHS.v);
		final float z = dotProduct(vLHS, oRHS.w);
		
		return new Vector3F(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vRHS} with the {@link Matrix44F} {@code mLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param vRHS a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3F transformTranspose(final Matrix44F mLHS, final Vector3F vRHS) {
		final float x = mLHS.element11 * vRHS.x + mLHS.element21 * vRHS.y + mLHS.element31 * vRHS.z;
		final float y = mLHS.element12 * vRHS.x + mLHS.element22 * vRHS.y + mLHS.element32 * vRHS.z;
		final float z = mLHS.element13 * vRHS.x + mLHS.element23 * vRHS.y + mLHS.element33 * vRHS.z;
		
		return new Vector3F(x, y, z);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(1.0F, 0.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(1.0F, 0.0F, 0.0F)}
	 */
	public static Vector3F u() {
		return u(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(u, 0.0F, 0.0F)}.
	 * 
	 * @param u the value of the U-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(u, 0.0F, 0.0F)}
	 */
	public static Vector3F u(final float u) {
		return new Vector3F(u, 0.0F, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 1.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 1.0F, 0.0F)}
	 */
	public static Vector3F v() {
		return v(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, v, 0.0F)}.
	 * 
	 * @param v the value of the V-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, v, 0.0F)}
	 */
	public static Vector3F v(final float v) {
		return new Vector3F(0.0F, v, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, 1.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, 1.0F)}
	 */
	public static Vector3F w() {
		return w(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, w)}.
	 * 
	 * @param w the value of the W-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, w)}
	 */
	public static Vector3F w(final float w) {
		return new Vector3F(0.0F, 0.0F, w);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(1.0F, 0.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(1.0F, 0.0F, 0.0F)}
	 */
	public static Vector3F x() {
		return x(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(x, 0.0F, 0.0F)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(x, 0.0F, 0.0F)}
	 */
	public static Vector3F x(final float x) {
		return new Vector3F(x, 0.0F, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 1.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 1.0F, 0.0F)}
	 */
	public static Vector3F y() {
		return y(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, y, 0.0F)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, y, 0.0F)}
	 */
	public static Vector3F y(final float y) {
		return new Vector3F(0.0F, y, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, 1.0F)}.
	 * 
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, 1.0F)}
	 */
	public static Vector3F z() {
		return z(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, z)}.
	 * 
	 * @param z the value of the Z-component
	 * @return a new {@code Vector3F} instance equivalent to {@code new Vector3F(0.0F, 0.0F, z)}
	 */
	public static Vector3F z(final float z) {
		return new Vector3F(0.0F, 0.0F, z);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector3F vLHS, final Vector3F vRHS) {
		final float dotProduct = dotProduct(vLHS, vRHS);
		
		final boolean isDotProductGTEThreshold = dotProduct >= 0.0F - 0.000001F;
		final boolean isDotProductLTEThreshold = dotProduct <= 0.0F + 0.000001F;
		
		return isDotProductGTEThreshold && isDotProductLTEThreshold;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean sameHemisphere(final Vector3F vLHS, final Vector3F vRHS) {
		return dotProduct(vLHS, vRHS) > 0.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method only operates on the Z-component (component 3), just like PBRT.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean sameHemisphereZ(final Vector3F vLHS, final Vector3F vRHS) {
		return vLHS.z * vRHS.z > 0.0F;
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static float dotProduct(final Vector3F vLHS, final Vector3F vRHS) {
		return vLHS.x * vRHS.x + vLHS.y * vRHS.y + vLHS.z * vRHS.z;
	}
	
	/**
	 * Returns the absolute dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param vRHS the {@code Vector3F} instance on the right-hand side
	 * @return the absolute dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static float dotProductAbs(final Vector3F vLHS, final Vector3F vRHS) {
		return Floats.abs(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the triple product of {@code vLHSDP}, {@code vLHSCP} and {@code vRHSCP}.
	 * <p>
	 * If either {@code vLHSDP}, {@code vLHSCP} or {@code vRHSCP} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.dotProduct(vLHSDP, Vector3F.crossProduct(vLHSCP, vRHSCP));
	 * }
	 * </pre>
	 * 
	 * @param vLHSDP the {@code Vector3F} instance on the left-hand side of the dot product
	 * @param vLHSCP the {@code Vector3F} instance of the left-hand side of the cross product
	 * @param vRHSCP the {@code Vector3F} instance of the right-hand side of the cross product
	 * @return the triple product of {@code vLHSDP}, {@code vLHSCP} and {@code vRHSCP}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHSDP}, {@code vLHSCP} or {@code vRHSCP} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static float tripleProduct(final Vector3F vLHSDP, final Vector3F vLHSCP, final Vector3F vRHSCP) {
		return dotProduct(vLHSDP, crossProduct(vLHSCP, vRHSCP));
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