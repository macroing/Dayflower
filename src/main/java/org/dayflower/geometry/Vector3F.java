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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

/**
 * A {@code Vector3F} denotes a 3-dimensional vector with three components, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector3F {
	private final float component1;
	private final float component2;
	private final float component3;
	
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
	 * Constructs a new {@code Vector3F} instance given the component values {@code p.getComponent1()}, {@code p.getComponent2()} and {@code p.getComponent3()}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3F(p.getComponent1(), p.getComponent2(), p.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Vector3F(final Point3F p) {
		this(p.getComponent1(), p.getComponent2(), p.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Vector3F} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Vector3F(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
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
		} else if(!equal(this.component1, Vector3F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector3F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Vector3F.class.cast(object).component3)) {
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
	public float cosPhi() {
		final float sinTheta = sinTheta();
		
		if(equal(sinTheta, 0.0F)) {
			return 1.0F;
		}
		
		return saturate(this.component1 / sinTheta, -1.0F, 1.0F);
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
		return this.component3;
	}
	
	/**
	 * Returns the cosine of the angle theta in absolute form.
	 * 
	 * @return the cosine of the angle theta in absolute form
	 */
	public float cosThetaAbs() {
		return abs(cosTheta());
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
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public float getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the V-component.
	 * 
	 * @return the value of the V-component
	 */
	public float getV() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component3;
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
	 * Returns the length of this {@code Vector3F} instance.
	 * 
	 * @return the length of this {@code Vector3F} instance
	 */
	public float length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector3F} instance.
	 * 
	 * @return the squared length of this {@code Vector3F} instance
	 */
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2 + this.component3 * this.component3;
	}
	
	/**
	 * Returns the sine of the angle phi.
	 * 
	 * @return the sine of the angle phi
	 */
	public float sinPhi() {
		final float sinTheta = sinTheta();
		
		if(equal(sinTheta, 0.0F)) {
			return 0.0F;
		}
		
		return saturate(this.component2 / sinTheta, -1.0F, 1.0F);
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
		return sqrt(sinThetaSquared());
	}
	
	/**
	 * Returns the sine of the angle theta in squared form.
	 * 
	 * @return the sine of the angle theta in squared form
	 */
	public float sinThetaSquared() {
		return max(0.0F, 1.0F - cosThetaSquared());
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
	 * Returns the tangent of the angle theta in squared form.
	 * 
	 * @return the tangent of the angle theta in squared form
	 */
	public float tanThetaSquared() {
		return sinThetaSquared() / cosThetaSquared();
	}
	
	/**
	 * Returns a hash code for this {@code Vector3F} instance.
	 * 
	 * @return a hash code for this {@code Vector3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		final float component1 = vLHS.component1 + vRHS.component1;
		final float component2 = vLHS.component2 + vRHS.component2;
		final float component3 = vLHS.component3 + vRHS.component3;
		
		return new Vector3F(component1, component2, component3);
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
		
		final float absWNormalizedComponent1 = abs(wNormalized.component1);
		final float absWNormalizedComponent2 = abs(wNormalized.component2);
		final float absWNormalizedComponent3 = abs(wNormalized.component3);
		
		if(absWNormalizedComponent1 < absWNormalizedComponent2 && absWNormalizedComponent1 < absWNormalizedComponent3) {
			return normalize(new Vector3F(0.0F, wNormalized.component3, -wNormalized.component2));
		}
		
		if(absWNormalizedComponent2 < absWNormalizedComponent3) {
			return normalize(new Vector3F(wNormalized.component3, 0.0F, -wNormalized.component1));
		}
		
		return normalize(new Vector3F(wNormalized.component2, -wNormalized.component1, 0.0F));
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
		final float component1 = vLHS.component2 * vRHS.component3 - vLHS.component3 * vRHS.component2;
		final float component2 = vLHS.component3 * vRHS.component1 - vLHS.component1 * vRHS.component3;
		final float component3 = vLHS.component1 * vRHS.component2 - vLHS.component2 * vRHS.component1;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code pEye} to {@code pLookAt}.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@link Point3F} instance denoting the eye to look from
	 * @param pLookAt a {@code Point3F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code pEye} to {@code pLookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static Vector3F direction(final Point3F pEye, final Point3F pLookAt) {
		final float component1 = pLookAt.getComponent1() - pEye.getComponent1();
		final float component2 = pLookAt.getComponent2() - pEye.getComponent2();
		final float component3 = pLookAt.getComponent3() - pEye.getComponent3();
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code pEye} to {@code pLookAt} and is normalized.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@link Point3F} instance denoting the eye to look from
	 * @param pLookAt a {@code Point3F} instance denoting the target to look at
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code pEye} to {@code pLookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static Vector3F directionNormalized(final Point3F pEye, final Point3F pLookAt) {
		return normalize(direction(pEye, pLookAt));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v}.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v}
	 */
	public static Vector3F directionSpherical(final float u, final float v) {
		final float theta = u * PI_MULTIPLIED_BY_2;
		final float phi = v * PI;
		
		final float cosPhi = cos(phi);
		final float cosTheta = cos(theta);
		
		final float sinPhi = sin(phi);
		final float sinTheta = sin(theta);
		
		final float component1 = -sinPhi * cosTheta;
		final float component2 = cosPhi;
		final float component3 = sinPhi * sinTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v} and is normalized.
	 * 
	 * @param u the spherical U-coordinate
	 * @param v the spherical V-coordinate
	 * @return a new {@code Vector3F} instance that is pointing in the direction of {@code u} and {@code v} and is normalized
	 */
	public static Vector3F directionSphericalNormalized(final float u, final float v) {
		return normalize(directionSpherical(u, v));
	}
	
	/**
	 * Divides the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the division.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F divide(final Vector3F vLHS, final float sRHS) {
		final float component1 = vLHS.component1 / sRHS;
		final float component2 = vLHS.component2 / sRHS;
		final float component3 = vLHS.component3 / sRHS;
		
		return new Vector3F(component1, component2, component3);
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
	 * Returns {@code vN} or {@code Vector3F.normalize(Vector3F.subtract(vO, vI))} as {@code Vector3F.dotProduct(vO, vI)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively.
	 * <p>
	 * If either {@code vO}, {@code vN} or {@code vI} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vO a {@code Vector3F} instance that points in the opposite direction of the ray
	 * @param vN a {@code Vector3F} instance that points in the direction of the surface normal
	 * @param vI a {@code Vector3F} instance that points in the direction of the light source to the surface intersection point
	 * @return {@code vN} or {@code Vector3F.normalize(Vector3F.subtract(vO, vI))} as {@code Vector3F.dotProduct(vO, vI)} is greater than {@code 0.999F} or less than or equal to {@code 0.999F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vO}, {@code vN} or {@code vI} are {@code null}
	 */
	public static Vector3F half(final Vector3F vO, final Vector3F vN, final Vector3F vI) {
		return dotProduct(vO, vI) > 0.999F ? vN : normalize(subtract(vO, vI));
	}
	
	/**
	 * Multiplies the component values of {@code vLHS} with {@code sRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the multiplication.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vLHS the {@code Vector3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public static Vector3F multiply(final Vector3F vLHS, final float sRHS) {
		final float component1 = vLHS.component1 * sRHS;
		final float component2 = vLHS.component2 * sRHS;
		final float component3 = vLHS.component3 * sRHS;
		
		return new Vector3F(component1, component2, component3);
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
		final float component1 = -v.component1;
		final float component2 = -v.component2;
		final float component3 = -v.component3;
		
		return new Vector3F(component1, component2, component3);
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
		final Vector3F edgeAB = directionNormalized(a, b);
		final Vector3F edgeAC = directionNormalized(a, c);
		
		return crossProduct(edgeAB, edgeAC);
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
		final float component1 = normalA.component1 * barycentricCoordinates.getU() + normalB.component1 * barycentricCoordinates.getV() + normalC.component1 * barycentricCoordinates.getW();
		final float component2 = normalA.component2 * barycentricCoordinates.getU() + normalB.component2 * barycentricCoordinates.getV() + normalC.component2 * barycentricCoordinates.getW();
		final float component3 = normalA.component3 * barycentricCoordinates.getU() + normalB.component3 * barycentricCoordinates.getV() + normalC.component3 * barycentricCoordinates.getW();
		
		return new Vector3F(component1, component2, component3);
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
		return divide(v, v.length());
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
	public static Vector3F reflectionNormalized(final Vector3F direction, final Vector3F normal, final boolean isFacingSurface) {
		return normalize(reflection(direction, normal, isFacingSurface));
	}
	
	/**
	 * Samples a direction on a cone with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param cosThetaMax the maximum cos theta value
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleConeUniformDistribution(final float u, final float v, final float cosThetaMax) {
		final float cosTheta = u * (cosThetaMax - 1.0F) + 1.0F;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = cos(phi) * sinTheta;
		final float component2 = sin(phi) * sinTheta;
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.sampleHemisphereCosineDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemisphereCosineDistribution() {
		return sampleHemisphereCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemisphereCosineDistribution(final float u, final float v) {
		final Point2F p = Point2F.sampleConcentricDisk(u, v);
		
		final float component1 = p.getComponent1();
		final float component2 = p.getComponent2();
		final float component3 = sqrt(max(0.0F, 1.0F - component1 * component1 - component2 * component2));
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.sampleHemispherePowerCosineDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemispherePowerCosineDistribution() {
		return sampleHemispherePowerCosineDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.sampleHemispherePowerCosineDistribution(u, v, 20.0F);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v) {
		return sampleHemispherePowerCosineDistribution(u, v, 20.0F);
	}
	
	/**
	 * Samples a direction on a hemisphere with a power-cosine distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param exponent the exponent to use
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemispherePowerCosineDistribution(final float u, final float v, final float exponent) {
		final float cosTheta = pow(1.0F - u, 1.0F / (exponent + 1.0F));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = cos(phi) * sinTheta;
		final float component2 = sin(phi) * sinTheta;
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.sampleHemisphereUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemisphereUniformDistribution() {
		return sampleHemisphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a hemisphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleHemisphereUniformDistribution(final float u, final float v) {
		final float cosTheta = u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = cos(phi) * sinTheta;
		final float component2 = sin(phi) * sinTheta;
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Vector3F.sampleSphereUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleSphereUniformDistribution() {
		return sampleSphereUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a direction on a sphere with a uniform distribution.
	 * <p>
	 * Returns a {@code Vector3F} instance with the sampled direction.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Vector3F} instance with the sampled direction
	 */
	public static Vector3F sampleSphereUniformDistribution(final float u, final float v) {
		final float cosTheta = 1.0F - 2.0F * u;
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float component1 = cos(phi) * sinTheta;
		final float component2 = sin(phi) * sinTheta;
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
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
		final float component1 = vLHS.component1 - vRHS.component1;
		final float component2 = vLHS.component2 - vRHS.component2;
		final float component3 = vLHS.component3 - vRHS.component3;
		
		return new Vector3F(component1, component2, component3);
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
	public static Vector3F transform(final Matrix44F mLHS, final Vector3F vRHS) {
		final float component1 = mLHS.getElement11() * vRHS.component1 + mLHS.getElement12() * vRHS.component2 + mLHS.getElement13() * vRHS.component3;
		final float component2 = mLHS.getElement21() * vRHS.component1 + mLHS.getElement22() * vRHS.component2 + mLHS.getElement23() * vRHS.component3;
		final float component3 = mLHS.getElement31() * vRHS.component1 + mLHS.getElement32() * vRHS.component2 + mLHS.getElement33() * vRHS.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vRHS} with the {@link OrthoNormalBasis33F} {@code oRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3F} instance
	 * @param oRHS an {@code OrthoNormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
	public static Vector3F transform(final Vector3F vLHS, final OrthoNormalBasis33F oRHS) {
		final float component1 = vLHS.component1 * oRHS.getU().component1 + vLHS.component2 * oRHS.getV().component1 + vLHS.component3 * oRHS.getW().component1;
		final float component2 = vLHS.component1 * oRHS.getU().component2 + vLHS.component2 * oRHS.getV().component2 + vLHS.component3 * oRHS.getW().component2;
		final float component3 = vLHS.component1 * oRHS.getU().component3 + vLHS.component2 * oRHS.getV().component3 + vLHS.component3 * oRHS.getW().component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vRHS} with the {@link OrthoNormalBasis33F} {@code oRHS} in reverse order.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vLHS} or {@code oRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS a {@code Vector3F} instance
	 * @param oRHS an {@code OrthoNormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code oRHS} are {@code null}
	 */
	public static Vector3F transformReverse(final Vector3F vLHS, final OrthoNormalBasis33F oRHS) {
		final float component1 = dotProduct(vLHS, oRHS.getU());
		final float component2 = dotProduct(vLHS, oRHS.getV());
		final float component3 = dotProduct(vLHS, oRHS.getW());
		
		return new Vector3F(component1, component2, component3);
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
	public static Vector3F transformTranspose(final Matrix44F mLHS, final Vector3F vRHS) {
		final float component1 = mLHS.getElement11() * vRHS.component1 + mLHS.getElement21() * vRHS.component2 + mLHS.getElement31() * vRHS.component3;
		final float component2 = mLHS.getElement12() * vRHS.component1 + mLHS.getElement22() * vRHS.component2 + mLHS.getElement32() * vRHS.component3;
		final float component3 = mLHS.getElement13() * vRHS.component1 + mLHS.getElement23() * vRHS.component2 + mLHS.getElement33() * vRHS.component3;
		
		return new Vector3F(component1, component2, component3);
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
		return vLHS.component1 * vRHS.component1 + vLHS.component2 * vRHS.component2 + vLHS.component3 * vRHS.component3;
	}
}