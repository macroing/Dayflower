/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import static org.dayflower.util.Doubles.PI;
import static org.dayflower.util.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.acos;
import static org.dayflower.util.Doubles.atan2;
import static org.dayflower.util.Doubles.cos;
import static org.dayflower.util.Doubles.equal;
import static org.dayflower.util.Doubles.finiteOrDefault;
import static org.dayflower.util.Doubles.gamma;
import static org.dayflower.util.Doubles.isZero;
import static org.dayflower.util.Doubles.max;
import static org.dayflower.util.Doubles.saturate;
import static org.dayflower.util.Doubles.sin;
import static org.dayflower.util.Doubles.sqrt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.node.Node;

/**
 * A {@code Vector3D} denotes a 3-dimensional vector with three components, of type {@code double}.
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
	
	private final double component1;
	private final double component2;
	private final double component3;
	
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
	 * Constructs a new {@code Vector3D} instance given the component values {@code point.getComponent1()}, {@code point.getComponent2()} and {@code point.getComponent3()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3D(point.getComponent1(), point.getComponent2(), point.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector3D(final Point3D point) {
		this(point.getComponent1(), point.getComponent2(), point.getComponent3());
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
	 * Constructs a new {@code Vector3D} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Vector3D(final double component1, final double component2, final double component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector3D(%+.10f, %+.10f, %+.10f)", Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
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
		} else if(!equal(this.component1, Vector3D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector3D.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Vector3D.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the cosine of the angle phi.
	 * 
	 * @return the cosine of the angle phi
	 */
	public double cosPhi() {
		final double sinTheta = sinTheta();
		
		if(equal(sinTheta, 0.0D)) {
			return 1.0D;
		}
		
		return saturate(this.component1 / sinTheta, -1.0D, 1.0D);
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
		return this.component3;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @return the cosine of the angle theta in absolute form
	 */
	public double cosThetaAbs() {
		return abs(cosTheta());
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
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public double getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public double getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public double getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public double getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public double getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public double getW() {
		return this.component3;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public double getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public double getY() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public double getZ() {
		return this.component3;
	}
	
	/**
	 * Returns the length of this {@code Vector3D} instance.
	 * 
	 * @return the length of this {@code Vector3D} instance
	 */
	public double length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector3D} instance.
	 * 
	 * @return the squared length of this {@code Vector3D} instance
	 */
	public double lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2 + this.component3 * this.component3;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @return the sine of the angle phi
	 */
	public double sinPhi() {
		final double sinTheta = sinTheta();
		
		if(isZero(sinTheta)) {
			return 0.0D;
		}
		
		return saturate(this.component2 / sinTheta, -1.0D, 1.0D);
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
		return sqrt(sinThetaSquared());
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @return the sine of the angle theta in squared form
	 */
	public double sinThetaSquared() {
		return max(0.0D, 1.0D - cosThetaSquared());
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @return the spherical phi angle
	 */
	public double sphericalPhi() {
		final double sphericalPhi0 = atan2(this.component2, this.component1);
		final double sphericalPhi1 = sphericalPhi0 < 0.0D ? sphericalPhi0 + 2.0D * PI : sphericalPhi0;
		
		return sphericalPhi1;
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @return the spherical theta angle
	 */
	public double sphericalTheta() {
		return acos(saturate(this.component3, -1.0D, 1.0D));
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
		return abs(tanTheta());
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
	 * Returns a hash code for this {@code Vector3D} instance.
	 * 
	 * @return a hash code for this {@code Vector3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
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
		final double sinThetaISquared = max(0.0D, 1.0D - cosThetaI * cosThetaI);
		final double sinThetaTSquared = eta * eta * sinThetaISquared;
		
		if(sinThetaTSquared >= 1.0D) {
			return Optional.empty();
		}
		
		final double cosThetaT = sqrt(1.0D - sinThetaTSquared);
		
		return Optional.of(add(multiply(negate(direction), eta), multiply(normal, eta * cosThetaI - cosThetaT)));
	}
	
	/**
	 * Returns a new {@code Vector3D} instance with the absolute component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the absolute component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D absolute(final Vector3D vector) {
		final double component1 = abs(vector.component1);
		final double component2 = abs(vector.component2);
		final double component3 = abs(vector.component3);
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D add(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		final double component1 = vectorLHS.component1 + vectorRHS.component1;
		final double component2 = vectorLHS.component2 + vectorRHS.component2;
		final double component3 = vectorLHS.component3 + vectorRHS.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code vectorA}, {@code vectorB} and {@code vectorC}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorA a {@code Vector3D} instance
	 * @param vectorB a {@code Vector3D} instance
	 * @param vectorC a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}
	 */
	public static Vector3D add(final Vector3D vectorA, final Vector3D vectorB, final Vector3D vectorC) {
		final double component1 = vectorA.component1 + vectorB.component1 + vectorC.component1;
		final double component2 = vectorA.component2 + vectorB.component2 + vectorC.component2;
		final double component3 = vectorA.component3 + vectorB.component3 + vectorC.component3;
		
		return new Vector3D(component1, component2, component3);
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
		
		final double absWNormalizedComponent1 = abs(wNormalized.component1);
		final double absWNormalizedComponent2 = abs(wNormalized.component2);
		final double absWNormalizedComponent3 = abs(wNormalized.component3);
		
		if(absWNormalizedComponent1 < absWNormalizedComponent2 && absWNormalizedComponent1 < absWNormalizedComponent3) {
			return normalize(new Vector3D(0.0D, wNormalized.component3, -wNormalized.component2));
		}
		
		if(absWNormalizedComponent2 < absWNormalizedComponent3) {
			return normalize(new Vector3D(wNormalized.component3, 0.0D, -wNormalized.component1));
		}
		
		return normalize(new Vector3D(wNormalized.component2, -wNormalized.component1, 0.0D));
	}
	
	/**
	 * Computes the cross product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the operation.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D crossProduct(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		final double component1 = vectorLHS.component2 * vectorRHS.component3 - vectorLHS.component3 * vectorRHS.component2;
		final double component2 = vectorLHS.component3 * vectorRHS.component1 - vectorLHS.component1 * vectorRHS.component3;
		final double component3 = vectorLHS.component1 * vectorRHS.component2 - vectorLHS.component2 * vectorRHS.component1;
		
		return new Vector3D(component1, component2, component3);
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
		final double component1 = lookAt.getComponent1() - eye.getComponent1();
		final double component2 = lookAt.getComponent2() - eye.getComponent2();
		final double component3 = lookAt.getComponent3() - eye.getComponent3();
		
		return new Vector3D(component1, component2, component3);
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
	 * Returns a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}.
	 * <p>
	 * This method is based on PBRT.
	 * 
	 * @param sinTheta the sine of the angle theta
	 * @param cosTheta the cosine of the angle theta
	 * @param phi the angle phi
	 * @return a new {@code Vector3D} instance that is pointing in the spherical direction given {@code sinTheta}, {@code cosTheta} and {@code phi}
	 */
	public static Vector3D directionSpherical(final double sinTheta, final double cosTheta, final double phi) {
		final double component1 = sinTheta * cos(phi);
		final double component2 = sinTheta * sin(phi);
		final double component3 = cosTheta;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v}
	 */
	public static Vector3D directionSpherical(final double u, final double v) {
		final double cosTheta = 1.0D - 2.0D * u;
		final double sinTheta = sqrt(max(0.0D, 1.0D - cosTheta * cosTheta));
		final double phi = v * PI_MULTIPLIED_BY_2;
		
		return directionSpherical(sinTheta, cosTheta, phi);
	}
	
	/**
	 * Returns a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v} and is normalized.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3D} instance that is pointing in the direction of {@code u} and {@code v} and is normalized
	 */
	public static Vector3D directionSphericalNormalized(final double u, final double v) {
		return normalize(directionSpherical(u, v));
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector3D divide(final Vector3D vectorLHS, final double scalarRHS) {
		final double component1 = finiteOrDefault(vectorLHS.component1 / scalarRHS, 0.0D);
		final double component2 = finiteOrDefault(vectorLHS.component2 / scalarRHS, 0.0D);
		final double component3 = finiteOrDefault(vectorLHS.component3 / scalarRHS, 0.0D);
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Returns {@code Vector3D.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3D.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return {@code Vector3D.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3D.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0D} or greater than or equal to {@code 0.0D}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D faceForward(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		return dotProduct(vectorLHS, vectorRHS) < 0.0D ? negate(vectorLHS) : vectorLHS;
	}
	
	/**
	 * Returns a cached version of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a cached version of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D getCached(final Vector3D vector) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(vector, "vector == null"), key -> vector);
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
	public static Vector3D half(final Vector3D o, final Vector3D n, final Vector3D i) {
		return dotProduct(o, i) > 0.999D ? n : normalize(subtract(o, i));
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector3D multiply(final Vector3D vectorLHS, final double scalarRHS) {
		final double component1 = vectorLHS.component1 * scalarRHS;
		final double component2 = vectorLHS.component2 * scalarRHS;
		final double component3 = vectorLHS.component3 * scalarRHS;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D negate(final Vector3D vector) {
		final double component1 = -vector.component1;
		final double component2 = -vector.component2;
		final double component3 = -vector.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Negates the component 1 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D negateComponent1(final Vector3D vector) {
		final double component1 = -vector.component1;
		final double component2 = +vector.component2;
		final double component3 = +vector.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Negates the component 2 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D negateComponent2(final Vector3D vector) {
		final double component1 = +vector.component1;
		final double component2 = -vector.component2;
		final double component3 = +vector.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Negates the component 3 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D negateComponent3(final Vector3D vector) {
		final double component1 = +vector.component1;
		final double component2 = +vector.component2;
		final double component3 = -vector.component3;
		
		return new Vector3D(component1, component2, component3);
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
		final Vector3D edgeAB = directionNormalized(a, b);
		final Vector3D edgeAC = directionNormalized(a, c);
		
		return crossProduct(edgeAB, edgeAC);
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
		final double component1 = normalA.component1 * barycentricCoordinates.getU() + normalB.component1 * barycentricCoordinates.getV() + normalC.component1 * barycentricCoordinates.getW();
		final double component2 = normalA.component2 * barycentricCoordinates.getU() + normalB.component2 * barycentricCoordinates.getV() + normalC.component2 * barycentricCoordinates.getW();
		final double component3 = normalA.component3 * barycentricCoordinates.getU() + normalB.component3 * barycentricCoordinates.getV() + normalC.component3 * barycentricCoordinates.getW();
		
		return new Vector3D(component1, component2, component3);
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
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D normalize(final Vector3D vector) {
		return divide(vector, vector.length());
	}
	
	/**
	 * Returns a new {@code Vector3D} instance with the reciprocal (or inverse) component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the reciprocal (or inverse) component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3D reciprocal(final Vector3D vector) {
		final double component1 = finiteOrDefault(1.0D / vector.component1, 0.0D);
		final double component2 = finiteOrDefault(1.0D / vector.component2, 0.0D);
		final double component3 = finiteOrDefault(1.0D / vector.component3, 0.0D);
		
		return new Vector3D(component1, component2, component3);
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
	public static Vector3D reflectionNormalized(final Vector3D direction, final Vector3D normal, final boolean isFacingSurface) {
		return normalize(reflection(direction, normal, isFacingSurface));
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D subtract(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		final double component1 = vectorLHS.component1 - vectorRHS.component1;
		final double component2 = vectorLHS.component2 - vectorRHS.component2;
		final double component3 = vectorLHS.component3 - vectorRHS.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vectorRHS} with the {@link Matrix44D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param vectorRHS a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D transform(final Matrix44D matrixLHS, final Vector3D vectorRHS) {
		final double component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement12() * vectorRHS.component2 + matrixLHS.getElement13() * vectorRHS.component3;
		final double component2 = matrixLHS.getElement21() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2 + matrixLHS.getElement23() * vectorRHS.component3;
		final double component3 = matrixLHS.getElement31() * vectorRHS.component1 + matrixLHS.getElement32() * vectorRHS.component2 + matrixLHS.getElement33() * vectorRHS.component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vectorRHS} with the {@link OrthonormalBasis33D} {@code orthonormalBasisRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS a {@code Vector3D} instance
	 * @param orthonormalBasisRHS an {@code OrthonormalBasis33D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static Vector3D transform(final Vector3D vectorLHS, final OrthonormalBasis33D orthonormalBasisRHS) {
		final double component1 = vectorLHS.component1 * orthonormalBasisRHS.getU().component1 + vectorLHS.component2 * orthonormalBasisRHS.getV().component1 + vectorLHS.component3 * orthonormalBasisRHS.getW().component1;
		final double component2 = vectorLHS.component1 * orthonormalBasisRHS.getU().component2 + vectorLHS.component2 * orthonormalBasisRHS.getV().component2 + vectorLHS.component3 * orthonormalBasisRHS.getW().component2;
		final double component3 = vectorLHS.component1 * orthonormalBasisRHS.getU().component3 + vectorLHS.component2 * orthonormalBasisRHS.getV().component3 + vectorLHS.component3 * orthonormalBasisRHS.getW().component3;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the error of the {@link Point3D} {@code point} with the {@link Matrix44D} {@code matrix}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrix} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44D} instance
	 * @param point a {@code Point3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrix} or {@code point} are {@code null}
	 */
	public static Vector3D transformError(final Matrix44D matrix, final Point3D point) {
		final double component1 = gamma(3) * (abs(matrix.getElement11() * point.getComponent1()) + abs(matrix.getElement12() * point.getY()) + abs(matrix.getElement13() * point.getZ()) + abs(matrix.getElement14()));
		final double component2 = gamma(3) * (abs(matrix.getElement21() * point.getComponent1()) + abs(matrix.getElement22() * point.getY()) + abs(matrix.getElement23() * point.getZ()) + abs(matrix.getElement24()));
		final double component3 = gamma(3) * (abs(matrix.getElement31() * point.getComponent1()) + abs(matrix.getElement32() * point.getY()) + abs(matrix.getElement33() * point.getZ()) + abs(matrix.getElement34()));
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the error of the {@code Vector3D} {@code vector} with the {@link Matrix44D} {@code matrix}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrix}, {@code point} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44D} instance
	 * @param point a {@link Point3D} instance
	 * @param vector a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrix}, {@code point} or {@code vector} are {@code null}
	 */
	public static Vector3D transformError(final Matrix44D matrix, final Point3D point, final Vector3D vector) {
		final double component1 = (gamma(3) + 1.0D) * (abs(matrix.getElement11()) * vector.component1 + abs(matrix.getElement12()) * vector.component2 + abs(matrix.getElement13()) * vector.component3) + gamma(3) * (abs(matrix.getElement11() * point.getComponent1()) + abs(matrix.getElement12() * point.getComponent2()) + abs(matrix.getElement13() * point.getComponent3()) + abs(matrix.getElement14()));
		final double component2 = (gamma(3) + 1.0D) * (abs(matrix.getElement21()) * vector.component1 + abs(matrix.getElement22()) * vector.component2 + abs(matrix.getElement23()) * vector.component3) + gamma(3) * (abs(matrix.getElement21() * point.getComponent1()) + abs(matrix.getElement22() * point.getComponent2()) + abs(matrix.getElement23() * point.getComponent3()) + abs(matrix.getElement24()));
		final double component3 = (gamma(3) + 1.0D) * (abs(matrix.getElement31()) * vector.component1 + abs(matrix.getElement32()) * vector.component2 + abs(matrix.getElement33()) * vector.component3) + gamma(3) * (abs(matrix.getElement31() * point.getComponent1()) + abs(matrix.getElement32() * point.getComponent2()) + abs(matrix.getElement33() * point.getComponent3()) + abs(matrix.getElement34()));
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vectorRHS} with the {@link OrthonormalBasis33D} {@code orthonormalBasisRHS} in reverse order.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS a {@code Vector3D} instance
	 * @param orthonormalBasisRHS an {@code OrthonormalBasis33D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static Vector3D transformReverse(final Vector3D vectorLHS, final OrthonormalBasis33D orthonormalBasisRHS) {
		final double component1 = dotProduct(vectorLHS, orthonormalBasisRHS.getU());
		final double component2 = dotProduct(vectorLHS, orthonormalBasisRHS.getV());
		final double component3 = dotProduct(vectorLHS, orthonormalBasisRHS.getW());
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3D} {@code vectorRHS} with the {@link Matrix44D} {@code matrixLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param vectorRHS a {@code Vector3D} instance
	 * @return a new {@code Vector3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D transformTranspose(final Matrix44D matrixLHS, final Vector3D vectorRHS) {
		final double component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement21() * vectorRHS.component2 + matrixLHS.getElement31() * vectorRHS.component3;
		final double component2 = matrixLHS.getElement12() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2 + matrixLHS.getElement32() * vectorRHS.component3;
		final double component3 = matrixLHS.getElement13() * vectorRHS.component1 + matrixLHS.getElement23() * vectorRHS.component2 + matrixLHS.getElement33() * vectorRHS.component3;
		
		return new Vector3D(component1, component2, component3);
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
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static double dotProduct(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component1 + vectorLHS.component2 * vectorRHS.component2 + vectorLHS.component3 * vectorRHS.component3;
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