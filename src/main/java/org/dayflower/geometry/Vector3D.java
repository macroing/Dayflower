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

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Vector3D} represents a vector with three {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector3D implements Node {
	/**
	 * A {@code Vector3D} instance given the component values {@code Double.NaN}, {@code Double.NaN} and {@code Double.NaN}.
	 */
	public static final Vector3D NaN = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
	
	/**
	 * A {@code Vector3D} instance given the component values {@code 0.0D}, {@code 0.0D} and {@code 0.0D}.
	 */
	public static final Vector3D ZERO = new Vector3D(0.0D, 0.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector3D, Vector3D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Vector3D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Vector3D} instance.
	 */
	public final double y;
	
	/**
	 * The Z-component of this {@code Vector3D} instance.
	 */
	public final double z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector3D} instance given the component values {@code 0.0D}, {@code 0.0D} and {@code 0.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Vector3D() {
		this(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Vector3D} instance given the component values {@code p.x}, {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3D(p.x, p.y, p.z);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector3D(final Point3D p) {
		this(p.x, p.y, p.z);
	}
	
	/**
	 * Constructs a new {@code Vector3D} instance given the component values {@code component}, {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Vector3D(final double component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Vector3D} instance given the component values {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Vector3D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector3D(%s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector3D)) {
			return false;
		} else if(!Doubles.equals(this.x, Vector3D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Vector3D.class.cast(object).y)) {
			return false;
		} else if(!Doubles.equals(this.z, Vector3D.class.cast(object).z)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector3D} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector3D} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final double length = length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
	}
	
	/**
	 * Returns the cosine of the angle phi.
	 * 
	 * @return the cosine of the angle phi
	 */
	public double cosPhi() {
		final double sinTheta = sinTheta();
		
		if(Doubles.isZero(sinTheta)) {
			return 1.0D;
		}
		
		return Doubles.saturate(this.x / sinTheta, -1.0D, 1.0D);
	}
	
	/**
	 * Returns the cosine of the angle phi in squared form.
	 * 
	 * @return the cosine of the angle phi in squared form
	 */
	public double cosPhiSquared() {
		return cosPhi() * cosPhi();
	}
	
	/**
	 * Returns the cosine of the angle theta.
	 * 
	 * @return the cosine of the angle theta
	 */
	public double cosTheta() {
		return this.z;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @return the cosine of the angle theta in absolute form
	 */
	public double cosThetaAbs() {
		return Doubles.abs(cosTheta());
	}
	
	/**
	 * Returns the cosine of the angle theta in quartic form.
	 * 
	 * @return the cosine of the angle theta in quartic form
	 */
	public double cosThetaQuartic() {
		return cosThetaSquared() * cosThetaSquared();
	}
	
	/**
	 * Returns the cosine of the angle theta in squared form.
	 * 
	 * @return the cosine of the angle theta in squared form
	 */
	public double cosThetaSquared() {
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
	public double getComponentAt(final int index) {
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
	 * Returns the length of this {@code Vector3D} instance.
	 * 
	 * @return the length of this {@code Vector3D} instance
	 */
	public double length() {
		return Doubles.sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector3D} instance.
	 * 
	 * @return the squared length of this {@code Vector3D} instance
	 */
	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @return the sine of the angle phi
	 */
	public double sinPhi() {
		final double sinTheta = sinTheta();
		
		if(Doubles.isZero(sinTheta)) {
			return 0.0D;
		}
		
		return Doubles.saturate(this.y / sinTheta, -1.0D, 1.0D);
	}
	
	/**
	 * Returns the sine of the angle phi in squared form.
	 * 
	 * @return the sine of the angle phi in squared form
	 */
	public double sinPhiSquared() {
		return sinPhi() * sinPhi();
	}
	
	/**
	 * Returns the sine of the angle theta.
	 * 
	 * @return the sine of the angle theta
	 */
	public double sinTheta() {
		return Doubles.sqrt(sinThetaSquared());
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @return the sine of the angle theta in squared form
	 */
	public double sinThetaSquared() {
		return Doubles.max(0.0D, 1.0D - cosThetaSquared());
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @return the spherical phi angle
	 */
//	TODO: Add Unit Tests!
	public double sphericalPhi() {
		return Doubles.addLessThan(Doubles.atan2(this.y, this.x), 0.0D, Doubles.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @return the spherical theta angle
	 */
//	TODO: Add Unit Tests!
	public double sphericalTheta() {
		return Doubles.acos(Doubles.saturate(this.z, -1.0D, 1.0D));
	}
	
	/**
	 * Returns the tangent of the angle theta.
	 * 
	 * @return the tangent of the angle theta
	 */
	public double tanTheta() {
		return sinTheta() / cosTheta();
	}
	
	/**
	 * Returns the tangent of the angle theta in absolute form.
	 * 
	 * @return the tangent of the angle theta in absolute form
	 */
	public double tanThetaAbs() {
		return Doubles.abs(tanTheta());
	}
	
	/**
	 * Returns the tangent of the angle theta in squared form.
	 * 
	 * @return the tangent of the angle theta in squared form
	 */
	public double tanThetaSquared() {
		return sinThetaSquared() / cosThetaSquared();
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Vector3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Vector3D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y, this.z};
	}
	
	/**
	 * Returns a hash code for this {@code Vector3D} instance.
	 * 
	 * @return a hash code for this {@code Vector3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Vector3D} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an optional {@code Vector3D} instance that represents the refraction of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction the {@code Vector3D} instance that will be refracted with regards to {@code normal}
	 * @param normal the {@code Vector3D} instance that represents the normal of the surface
	 * @param eta the index of refraction
	 * @return an optional {@code Vector3D} instance that represents the refraction of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
	public static Optional<Vector3D> refraction(final Vector3D direction, final Vector3D normal, final double eta) {
		final double cosThetaI = dotProduct(direction, normal);
		final double sinThetaISquared = Doubles.max(0.0D, 1.0D - cosThetaI * cosThetaI);
		final double sinThetaTSquared = eta * eta * sinThetaISquared;
		
		if(sinThetaTSquared >= 1.0D) {
			return Optional.empty();
		}
		
		final double cosThetaT = Doubles.sqrt(1.0D - sinThetaTSquared);
		
		return Optional.of(add(multiply(negate(direction), eta), multiply(normal, eta * cosThetaI - cosThetaT)));
	}
	
	/**
	 * Returns a new {@code Vector3D} instance with the absolute component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the absolute component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D absolute(final Vector3D v) {
		return new Vector3D(Doubles.abs(v.x), Doubles.abs(v.y), Doubles.abs(v.z));
	}
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the addition.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D add(final Vector3D vLHS, final Vector3D vRHS) {
		return new Vector3D(vLHS.x + vRHS.x, vLHS.y + vRHS.y, vLHS.z + vRHS.z);
	}
	
	/**
	 * Adds the component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the addition.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param a a {@code Vector3D} instance
	 * @param b a {@code Vector3D} instance
	 * @param c a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3D add(final Vector3D a, final Vector3D b, final Vector3D c) {
		return new Vector3D(a.x + b.x + c.x, a.y + b.y + c.y, a.z + b.z + c.z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance denoting {@code V} in an orthonormal basis.
	 * <p>
	 * If {@code w} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param w the {@code Vector3D} instance that denotes {@code W} in an orthonormal basis
	 * @return a new {@code Vector3D} instance denoting {@code V} in an orthonormal basis
	 * @throws NullPointerException thrown if, and only if, {@code w} is {@code null}
	 */
	public static Vector3D computeV(final Vector3D w) {
		final Vector3D wNormalized = normalize(w);
		
		final double absWNormalizedComponent1 = Doubles.abs(wNormalized.x);
		final double absWNormalizedComponent2 = Doubles.abs(wNormalized.y);
		final double absWNormalizedComponent3 = Doubles.abs(wNormalized.z);
		
		if(absWNormalizedComponent1 < absWNormalizedComponent2 && absWNormalizedComponent1 < absWNormalizedComponent3) {
			return normalize(new Vector3D(0.0D, wNormalized.z, -wNormalized.y));
		}
		
		if(absWNormalizedComponent2 < absWNormalizedComponent3) {
			return normalize(new Vector3D(wNormalized.z, 0.0D, -wNormalized.x));
		}
		
		return normalize(new Vector3D(wNormalized.y, -wNormalized.x, 0.0D));
	}
	
	/**
	 * Computes the cross product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the operation.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D crossProduct(final Vector3D vLHS, final Vector3D vRHS) {
		return new Vector3D(vLHS.y * vRHS.z - vLHS.z * vRHS.y, vLHS.z * vRHS.x - vLHS.x * vRHS.z, vLHS.x * vRHS.y - vLHS.y * vRHS.x);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3D} instance denoting the eye to look from
	 * @param lookAt a {@code Point3D} instance denoting the target to look at
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3D direction(final Point3D eye, final Point3D lookAt) {
		return new Vector3D(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point4D} instance denoting the eye to look from
	 * @param lookAt a {@code Point4D} instance denoting the target to look at
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3D direction(final Point4D eye, final Point4D lookAt) {
		return new Vector3D(lookAt.x - eye.x, lookAt.y - eye.y, lookAt.z - eye.z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point3D} instance denoting the eye to look from
	 * @param lookAt a {@code Point3D} instance denoting the target to look at
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3D directionNormalized(final Point3D eye, final Point3D lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point4D} instance denoting the eye to look from
	 * @param lookAt a {@code Point4D} instance denoting the target to look at
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector3D directionNormalized(final Point4D eye, final Point4D lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D directionSpherical(final double u, final double v) {
		final double phi = u * Doubles.PI_MULTIPLIED_BY_2;
		final double theta = v * Doubles.PI;
		
		return directionSpherical(Doubles.sin(theta), Doubles.cos(theta), phi);
		
//		final double cosTheta = 1.0D - 2.0D * u;
//		final double sinTheta = Doubles.sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
//		final double phi = v * Doubles.PI_MULTIPLIED_BY_2;
		
//		return directionSpherical(sinTheta, cosTheta, phi);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}.
	 * <p>
	 * This method is based on PBRT.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 * @return a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D directionSpherical(final double sinTheta, final double cosTheta, final double phi) {
		return new Vector3D(sinTheta * Doubles.cos(phi), sinTheta * Doubles.sin(phi), cosTheta);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}, as well as the coordinate system {@code x}, {@code y} and {@code z}.
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
	 * @return a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}, as well as the coordinate system {@code x}, {@code y} and {@code z}
	 * @throws NullPointerException thrown if, and only if, either {@code x}, {@code y} or {@code z} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D directionSpherical(final double sinTheta, final double cosTheta, final double phi, final Vector3D x, final Vector3D y, final Vector3D z) {
		return add(multiply(x, sinTheta * Doubles.cos(phi)), multiply(y, sinTheta * Doubles.sin(phi)), multiply(z, cosTheta));
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v} and is normalized.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v} and is normalized
	 */
//	TODO: Add Unit Tests!
	public static Vector3D directionSphericalNormalized(final double u, final double v) {
		return normalize(directionSpherical(u, v));
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the division.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector3D divide(final Vector3D vLHS, final double sRHS) {
		return new Vector3D(Doubles.finiteOrDefault(vLHS.x / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.y / sRHS, 0.0D), Doubles.finiteOrDefault(vLHS.z / sRHS, 0.0D));
	}
	
	/**
	 * Returns {@code Vector3D.negate(vLHS)} or {@code vLHS} as {@code Vector3D.dotProduct(vLHS, vRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code Vector3D.negate(vLHS)} or {@code vLHS} as {@code Vector3D.dotProduct(vLHS, vRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D faceForward(final Vector3D vLHS, final Vector3D vRHS) {
		return dotProduct(vLHS, vRHS) < 0.0D ? negate(vLHS) : vLHS;
	}
	
	/**
	 * Returns {@code Vector3D.negate(direction)} or {@code direction} as {@code Vector3D.dotProduct(vLHS, vRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vLHS}, {@code vRHS} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @param direction the {@code Vector3D} instance to potentially negate and then return
	 * @return {@code Vector3D.negate(direction)} or {@code direction} as {@code Vector3D.dotProduct(vLHS, vRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS}, {@code vRHS} or {@code direction} are {@code null}
	 */
	public static Vector3D faceForward(final Vector3D vLHS, final Vector3D vRHS, final Vector3D direction) {
		return dotProduct(vLHS, vRHS) < 0.0D ? negate(direction) : Objects.requireNonNull(direction, "direction == null");
	}
	
	/**
	 * Returns {@code Vector3D.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3D.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D faceForwardZ(final Vector3D vLHS, final Vector3D vRHS) {
		return vLHS.z < 0.0D ? negateZ(vRHS) : Objects.requireNonNull(vRHS, "vRHS == null");
	}
	
	/**
	 * Returns {@code Vector3D.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is greater than {@code 0.0D} or less than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3D.negateZ(vRHS)} or {@code vRHS} as {@code vLHS.z} is greater than {@code 0.0D} or less than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D faceForwardZNegated(final Vector3D vLHS, final Vector3D vRHS) {
		return vLHS.z > 0.0D ? negateZ(vRHS) : Objects.requireNonNull(vRHS, "vRHS == null");
	}
	
	/**
	 * Returns {@code Vector3D.negate(vLHS)} or {@code vLHS} as {@code Vector3D.dotProduct(vLHS, vRHS)} is greater than {@code 0.0D} or less than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code Vector3D.negate(vLHS)} or {@code vLHS} as {@code Vector3D.dotProduct(vLHS, vRHS)} is greater than {@code 0.0D} or less than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D faceForwardNegated(final Vector3D vLHS, final Vector3D vRHS) {
		return dotProduct(vLHS, vRHS) > 0.0D ? negate(vLHS) : vLHS;
	}
	
	/**
	 * Returns a cached version of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a cached version of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D getCached(final Vector3D v) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(v, "v == null"), key -> v);
	}
	
	/**
	 * Returns a {@code Vector3D} instance that contains the Hadamard product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return a {@code Vector3D} instance that contains the Hadamard product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D hadamardProduct(final Vector3D vLHS, final Vector3D vRHS) {
		return new Vector3D(vLHS.x * vRHS.x, vLHS.y * vRHS.y, vLHS.z * vRHS.z);
	}
	
	/**
	 * Returns {@code n} or {@code Vector3D.normalize(Vector3D.subtract(o, i))} as {@code Vector3D.dotProduct(o, i)} is greater than {@code 0.999D} or less than or equal to {@code 0.999D}, respectively.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param o a {@code Vector3D} instance that points in the opposite direction of the ray
	 * @param n a {@code Vector3D} instance that points in the direction of the surface normal
	 * @param i a {@code Vector3D} instance that points in the direction of the light source to the surface intersection point
	 * @return {@code n} or {@code Vector3D.normalize(Vector3D.subtract(o, i))} as {@code Vector3D.dotProduct(o, i)} is greater than {@code 0.999D} or less than or equal to {@code 0.999D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D half(final Vector3D o, final Vector3D n, final Vector3D i) {
		return dotProduct(o, i) > 0.999D ? n : normalize(subtract(o, i));
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector3D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector3D} instance
	 * @param b a {@code Vector3D} instance
	 * @param t the factor
	 * @return a {@code Vector3D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector3D lerp(final Vector3D a, final Vector3D b, final double t) {
		return new Vector3D(Doubles.lerp(a.x, b.x, t), Doubles.lerp(a.y, b.y, t), Doubles.lerp(a.z, b.z, t));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the multiplication.
	 * <p>
	 * If {@code vLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vLHS} is {@code null}
	 */
	public static Vector3D multiply(final Vector3D vLHS, final double sRHS) {
		return new Vector3D(vLHS.x * sRHS, vLHS.y * sRHS, vLHS.z * sRHS);
	}
	
	/**
	 * Negates the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D negate(final Vector3D v) {
		return new Vector3D(-v.x, -v.y, -v.z);
	}
	
	/**
	 * Negates the X-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D negateX(final Vector3D v) {
		return new Vector3D(-v.x, v.y, v.z);
	}
	
	/**
	 * Negates the Y-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D negateY(final Vector3D v) {
		return new Vector3D(v.x, -v.y, v.z);
	}
	
	/**
	 * Negates the Z-component of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D negateZ(final Vector3D v) {
		return new Vector3D(v.x, v.y, -v.z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance denoting the normal of the plane defined by the {@link Point3D} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @param b a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @param c a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @return a new {@code Vector3D} instance denoting the normal of the plane defined by the {@code Point3D} instances {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3D normal(final Point3D a, final Point3D b, final Point3D c) {
		return crossProduct(directionNormalized(a, b), directionNormalized(a, c));
	}
	
	/**
	 * Computes a normal vector from three other normal vectors via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the interpolated normal.
	 * <p>
	 * If either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This method does not normalize {@code normalA}, {@code normalB} or {@code normalC}.
	 * 
	 * @param normalA a {@code Vector3D} instance denoting the normal of vertex {@code A} of a triangle
	 * @param normalB a {@code Vector3D} instance denoting the normal of vertex {@code B} of a triangle
	 * @param normalC a {@code Vector3D} instance denoting the normal of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3D} instance denoting the Barycentric coordinates
	 * @return a new {@code Vector3D} instance with the interpolated normal
	 * @throws NullPointerException thrown if, and only if, either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}
	 */
	public static Vector3D normal(final Vector3D normalA, final Vector3D normalB, final Vector3D normalC, final Point3D barycentricCoordinates) {
		final double x = normalA.x * barycentricCoordinates.x + normalB.x * barycentricCoordinates.y + normalC.x * barycentricCoordinates.z;
		final double y = normalA.y * barycentricCoordinates.x + normalB.y * barycentricCoordinates.y + normalC.y * barycentricCoordinates.z;
		final double z = normalA.z * barycentricCoordinates.x + normalB.z * barycentricCoordinates.y + normalC.z * barycentricCoordinates.z;
		
		return new Vector3D(x, y, z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance denoting the normalized normal of the plane defined by the {@link Point3D} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @param b a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @param c a {@code Point3D} instance, corresponding to one of the three points in the plane
	 * @return a new {@code Vector3D} instance denoting the normalized normal of the plane defined by the {@code Point3D} instances {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Vector3D normalNormalized(final Point3D a, final Point3D b, final Point3D c) {
		return normalize(normal(a, b, c));
	}
	
	/**
	 * Computes a normalized normal vector from three other normal vectors via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the interpolated and normalized normal.
	 * <p>
	 * If either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * <strong>Note:</strong> This method does not normalize {@code normalA}, {@code normalB} or {@code normalC}.
	 * 
	 * @param normalA a {@code Vector3D} instance denoting the normal of vertex {@code A} of a triangle
	 * @param normalB a {@code Vector3D} instance denoting the normal of vertex {@code B} of a triangle
	 * @param normalC a {@code Vector3D} instance denoting the normal of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3D} instance denoting the Barycentric coordinates
	 * @return a new {@code Vector3D} instance with the interpolated and normalized normal
	 * @throws NullPointerException thrown if, and only if, either {@code normalA}, {@code normalB}, {@code normalC} or {@code barycentricCoordinates} are {@code null}
	 */
	public static Vector3D normalNormalized(final Vector3D normalA, final Vector3D normalB, final Vector3D normalC, final Point3D barycentricCoordinates) {
		return normalize(normal(normalA, normalB, normalC, barycentricCoordinates));
	}
	
	/**
	 * Normalizes the component values of {@code v}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the normalization.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D normalize(final Vector3D v) {
		final double length = v.length();
		
		final boolean isLengthGTEThreshold = length >= Doubles.NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= Doubles.NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return v;
		}
		
		return divide(v, length);
	}
	
	/**
	 * Returns a random {@code Vector3D} instance.
	 * 
	 * @return a random {@code Vector3D} instance
	 */
	public static Vector3D random() {
		return new Vector3D(Randoms.nextDouble() * 2.0D - 1.0D, Randoms.nextDouble() * 2.0D - 1.0D, Randoms.nextDouble() * 2.0D - 1.0D);
	}
	
	/**
	 * Returns a random and normalized {@code Vector3D} instance.
	 * 
	 * @return a random and normalized {@code Vector3D} instance
	 */
	public static Vector3D randomNormalized() {
		return normalize(random());
	}
	
	/**
	 * Returns a new {@code Vector3D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector3D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector3D read(final DataInput dataInput) {
		try {
			return new Vector3D(dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code Vector3D} instance with the reciprocal (or inverse) component values of {@code v}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param v a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the reciprocal (or inverse) component values of {@code v}
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3D reciprocal(final Vector3D v) {
		return new Vector3D(1.0D / v.x, 1.0D / v.y, 1.0D / v.z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3D.reflection(direction, normal, false);
	 * }
	 * </pre>
	 * 
	 * @param direction the {@code Vector3D} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3D} instance that represents the normal of the surface
	 * @return a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D reflection(final Vector3D direction, final Vector3D normal) {
		return reflection(direction, normal, false);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal}.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isFacingSurface} is {@code true}, it is assumed that {@code direction} is facing the surface. This is usually the case for the direction of a ray that intersects the surface. If {@code isFacingSurface} is {@code false}, it is assumed
	 * that {@code direction} is pointing in the opposite direction. That is, the ray starts at the surface intersection point and is directed away from the surface.
	 * 
	 * @param direction the {@code Vector3D} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3D} instance that represents the normal of the surface
	 * @param isFacingSurface {@code true} if, and only if, {@code direction} is facing the surface, {@code false} otherwise
	 * @return a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D reflection(final Vector3D direction, final Vector3D normal, final boolean isFacingSurface) {
		return isFacingSurface ? subtract(direction, multiply(normal, dotProduct(direction, normal) * 2.0D)) : subtract(multiply(normal, dotProduct(direction, normal) * 2.0D), direction);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3D.reflectionNormalized(direction, normal, false);
	 * }
	 * </pre>
	 * 
	 * @param direction the {@code Vector3D} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3D} instance that represents the normal of the surface
	 * @return a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D reflectionNormalized(final Vector3D direction, final Vector3D normal) {
		return reflectionNormalized(direction, normal, false);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized.
	 * <p>
	 * If either {@code direction} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code isFacingSurface} is {@code true}, it is assumed that {@code direction} is facing the surface. This is usually the case for the direction of a ray that intersects the surface. If {@code isFacingSurface} is {@code false}, it is assumed
	 * that {@code direction} is pointing in the opposite direction. That is, the ray starts at the surface intersection point and is directed away from the surface.
	 * 
	 * @param direction the {@code Vector3D} instance that will be reflected with regards to {@code normal}
	 * @param normal the {@code Vector3D} instance that represents the normal of the surface
	 * @param isFacingSurface {@code true} if, and only if, {@code direction} is facing the surface, {@code false} otherwise
	 * @return a new {@code Vector3D} instance that represents the reflection of {@code direction} with regards to {@code normal} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code normal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D reflectionNormalized(final Vector3D direction, final Vector3D normal, final boolean isFacingSurface) {
		return normalize(reflection(direction, normal, isFacingSurface));
	}
	
	/**
	 * Returns a {@code Vector3D} instance that represents {@code v} rotated by {@code q}.
	 * <p>
	 * If either {@code q} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param q a {@link Quaternion4D} instance
	 * @param v a {@code Vector3D} instance
	 * @return a {@code Vector3D} instance that represents {@code v} rotated by {@code q}
	 * @throws NullPointerException thrown if, and only if, either {@code q} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D rotate(final Quaternion4D q, final Vector3D v) {
		final Quaternion4D q0 = Quaternion4D.conjugate(q);
		final Quaternion4D q1 = Quaternion4D.multiply(q, v);
		final Quaternion4D q2 = Quaternion4D.multiply(q1, q0);
		
		return new Vector3D(q2.x, q2.y, q2.z);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code vLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static Vector3D subtract(final Vector3D vLHS, final Vector3D vRHS) {
		return new Vector3D(vLHS.x - vRHS.x, vLHS.y - vRHS.y, vLHS.z - vRHS.z);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vRHS} with the {@link Matrix44D} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param vRHS a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D transform(final Matrix44D mLHS, final Vector3D vRHS) {
		final double x = mLHS.element11 * vRHS.x + mLHS.element12 * vRHS.y + mLHS.element13 * vRHS.z;
		final double y = mLHS.element21 * vRHS.x + mLHS.element22 * vRHS.y + mLHS.element23 * vRHS.z;
		final double z = mLHS.element31 * vRHS.x + mLHS.element32 * vRHS.y + mLHS.element33 * vRHS.z;
		
		return new Vector3D(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vLHS} with the {@link OrthonormalBasis33D} {@code oRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3D} instance
	 * @param oRHS an {@code OrthonormalBasis33D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D transform(final Vector3D vLHS, final OrthonormalBasis33D oRHS) {
		final double x = vLHS.x * oRHS.u.x + vLHS.y * oRHS.v.x + vLHS.z * oRHS.w.x;
		final double y = vLHS.x * oRHS.u.y + vLHS.y * oRHS.v.y + vLHS.z * oRHS.w.y;
		final double z = vLHS.x * oRHS.u.z + vLHS.y * oRHS.v.z + vLHS.z * oRHS.w.z;
		
		return new Vector3D(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vLHS} with the {@link OrthonormalBasis33D} {@code oRHS} in reverse order.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3D} instance
	 * @param oRHS an {@code OrthonormalBasis33D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D transformReverse(final Vector3D vLHS, final OrthonormalBasis33D oRHS) {
		final double x = dotProduct(vLHS, oRHS.u);
		final double y = dotProduct(vLHS, oRHS.v);
		final double z = dotProduct(vLHS, oRHS.w);
		
		return new Vector3D(x, y, z);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vRHS} with the {@link Matrix44D} {@code mLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param vRHS a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code vRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Vector3D transformTranspose(final Matrix44D mLHS, final Vector3D vRHS) {
		final double x = mLHS.element11 * vRHS.x + mLHS.element21 * vRHS.y + mLHS.element31 * vRHS.z;
		final double y = mLHS.element12 * vRHS.x + mLHS.element22 * vRHS.y + mLHS.element32 * vRHS.z;
		final double z = mLHS.element13 * vRHS.x + mLHS.element23 * vRHS.y + mLHS.element33 * vRHS.z;
		
		return new Vector3D(x, y, z);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(1.0D, 0.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(1.0D, 0.0D, 0.0D)}
	 */
	public static Vector3D u() {
		return u(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(u, 0.0D, 0.0D)}.
	 * 
	 * @param u the value of the U-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(u, 0.0D, 0.0D)}
	 */
	public static Vector3D u(final double u) {
		return new Vector3D(u, 0.0D, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 1.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 1.0D, 0.0D)}
	 */
	public static Vector3D v() {
		return v(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, v, 0.0D)}.
	 * 
	 * @param v the value of the V-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, v, 0.0D)}
	 */
	public static Vector3D v(final double v) {
		return new Vector3D(0.0D, v, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, 1.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, 1.0D)}
	 */
	public static Vector3D w() {
		return w(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, w)}.
	 * 
	 * @param w the value of the W-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, w)}
	 */
	public static Vector3D w(final double w) {
		return new Vector3D(0.0D, 0.0D, w);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(1.0D, 0.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(1.0D, 0.0D, 0.0D)}
	 */
	public static Vector3D x() {
		return x(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(x, 0.0D, 0.0D)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(x, 0.0D, 0.0D)}
	 */
	public static Vector3D x(final double x) {
		return new Vector3D(x, 0.0D, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 1.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 1.0D, 0.0D)}
	 */
	public static Vector3D y() {
		return y(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, y, 0.0D)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, y, 0.0D)}
	 */
	public static Vector3D y(final double y) {
		return new Vector3D(0.0D, y, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, 1.0D)}.
	 * 
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, 1.0D)}
	 */
	public static Vector3D z() {
		return z(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, z)}.
	 * 
	 * @param z the value of the Z-component
	 * @return a new {@code Vector3D} instance equivalent to {@code new Vector3D(0.0D, 0.0D, z)}
	 */
	public static Vector3D z(final double z) {
		return new Vector3D(0.0D, 0.0D, z);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector3D vLHS, final Vector3D vRHS) {
		final double dotProduct = dotProduct(vLHS, vRHS);
		
		final boolean isDotProductGTEThreshold = dotProduct >= 0.0D - 0.000001D;
		final boolean isDotProductLTEThreshold = dotProduct <= 0.0D + 0.000001D;
		
		return isDotProductGTEThreshold && isDotProductLTEThreshold;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean sameHemisphere(final Vector3D vLHS, final Vector3D vRHS) {
		return dotProduct(vLHS, vRHS) > 0.0D;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method only operates on the Z-component (component 3), just like PBRT.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are in the same hemisphere, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean sameHemisphereZ(final Vector3D vLHS, final Vector3D vRHS) {
		return vLHS.z * vRHS.z > 0.0D;
	}
	
	/**
	 * Performs the operation {@code CosDPhi(...)} of PBRT.
	 * <p>
	 * Returns the result of the operation.
	 * <p>
	 * If either {@code wa} or {@code wb} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param wa a {@code Vector3D} instance
	 * @param wb a {@code Vector3D} instance
	 * @return the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code wa} or {@code wb} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static double cosDPhi(final Vector3D wa, final Vector3D wb) {
		final double waxy = wa.x * wa.x + wa.y * wa.y;
		final double wbxy = wb.x * wb.x + wb.y * wb.y;
		
		if(waxy == 0.0D || wbxy == 0.0D) {
			return 1.0D;
		}
		
		return Doubles.saturate((wa.x * wb.x + wa.y * wb.y) / Doubles.sqrt(waxy * wbxy), -1.0D, 1.0D);
	}
	
	/**
	 * Returns the dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return the dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static double dotProduct(final Vector3D vLHS, final Vector3D vRHS) {
		return vLHS.x * vRHS.x + vLHS.y * vRHS.y + vLHS.z * vRHS.z;
	}
	
	/**
	 * Returns the absolute dot product of {@code vLHS} and {@code vRHS}.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector3D} instance on the left-hand side
	 * @param vRHS the {@code Vector3D} instance on the right-hand side
	 * @return the absolute dot product of {@code vLHS} and {@code vRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static double dotProductAbs(final Vector3D vLHS, final Vector3D vRHS) {
		return Doubles.abs(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the triple product of {@code vLHSDP}, {@code vLHSCP} and {@code vRHSCP}.
	 * <p>
	 * If either {@code vLHSDP}, {@code vLHSCP} or {@code vRHSCP} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3D.dotProduct(vLHSDP, Vector3D.crossProduct(vLHSCP, vRHSCP));
	 * }
	 * </pre>
	 * 
	 * @param vLHSDP the {@code Vector3D} instance on the left-hand side of the dot product
	 * @param vLHSCP the {@code Vector3D} instance of the left-hand side of the cross product
	 * @param vRHSCP the {@code Vector3D} instance of the right-hand side of the cross product
	 * @return the triple product of {@code vLHSDP}, {@code vLHSCP} and {@code vRHSCP}
	 * @throws NullPointerException thrown if, and only if, either {@code vLHSDP}, {@code vLHSCP} or {@code vRHSCP} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static double tripleProduct(final Vector3D vLHSDP, final Vector3D vLHSCP, final Vector3D vRHSCP) {
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