/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.acos;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.finiteOrDefault;
import static org.dayflower.utility.Floats.gamma;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.saturate;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.node.Node;
import org.dayflower.utility.Floats;

/**
 * A {@code Vector3F} denotes a 3-dimensional vector with three components, of type {@code float}.
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
	 * Constructs a new {@code Vector3F} instance given the component values {@code point.getComponent1()}, {@code point.getComponent2()} and {@code point.getComponent3()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector3F(point.getComponent1(), point.getComponent2(), point.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector3F(final Point3F point) {
		this(point.getComponent1(), point.getComponent2(), point.getComponent3());
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
	 * Returns {@code true} if, and only if, this {@code Vector3F} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector3F} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final float length = length();
		
		final boolean isLengthGTEThreshold = length >= 1.0F - 0.000001F;
		final boolean isLengthLTEThreshold = length <= 1.0F + 0.000001F;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
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
		
		if(isZero(sinTheta)) {
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
	 * Returns the spherical phi angle.
	 * 
	 * @return the spherical phi angle
	 */
	public float sphericalPhi() {
		final float sphericalPhi0 = atan2(this.component2, this.component1);
		final float sphericalPhi1 = sphericalPhi0 < 0.0F ? sphericalPhi0 + 2.0F * PI : sphericalPhi0;
		
		return sphericalPhi1;
	}
	
	/**
	 * Returns the spherical theta angle.
	 * 
	 * @return the spherical theta angle
	 */
	public float sphericalTheta() {
		return acos(saturate(this.component3, -1.0F, 1.0F));
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
		return abs(tanTheta());
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
		return new float[] {
			this.component1,
			this.component2,
			this.component3
		};
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
			dataOutput.writeFloat(this.component1);
			dataOutput.writeFloat(this.component2);
			dataOutput.writeFloat(this.component3);
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
		final float sinThetaISquared = max(0.0F, 1.0F - cosThetaI * cosThetaI);
		final float sinThetaTSquared = eta * eta * sinThetaISquared;
		
		if(sinThetaTSquared >= 1.0F) {
			return Optional.empty();
		}
		
		final float cosThetaT = sqrt(1.0F - sinThetaTSquared);
		
		return Optional.of(add(multiply(negate(direction), eta), multiply(normal, eta * cosThetaI - cosThetaT)));
	}
	
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
	public static Optional<Vector3F> refraction2(final Vector3F direction, final Vector3F normal, final float eta) {
		final float cosThetaI = dotProduct(direction, normal);
		final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
		final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
		final float cosThetaT = sqrt(sinThetaTSquared);
		
		if(sinThetaTSquared < 0.0F) {
			return Optional.empty();
		}
		
		return Optional.of(subtract(multiply(direction, eta), multiply(normal, eta * cosThetaI + cosThetaT)));
	}
	
	/**
	 * Returns a new {@code Vector3F} instance with the absolute component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the absolute component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F absolute(final Vector3F vector) {
		final float component1 = abs(vector.component1);
		final float component2 = abs(vector.component2);
		final float component3 = abs(vector.component3);
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F add(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		final float component1 = vectorLHS.component1 + vectorRHS.component1;
		final float component2 = vectorLHS.component2 + vectorRHS.component2;
		final float component3 = vectorLHS.component3 + vectorRHS.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code vectorA}, {@code vectorB} and {@code vectorC}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorA a {@code Vector3F} instance
	 * @param vectorB a {@code Vector3F} instance
	 * @param vectorC a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}
	 */
	public static Vector3F add(final Vector3F vectorA, final Vector3F vectorB, final Vector3F vectorC) {
		final float component1 = vectorA.component1 + vectorB.component1 + vectorC.component1;
		final float component2 = vectorA.component2 + vectorB.component2 + vectorC.component2;
		final float component3 = vectorA.component3 + vectorB.component3 + vectorC.component3;
		
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
	 * Computes the cross product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the operation.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F crossProduct(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		final float component1 = vectorLHS.component2 * vectorRHS.component3 - vectorLHS.component3 * vectorRHS.component2;
		final float component2 = vectorLHS.component3 * vectorRHS.component1 - vectorLHS.component1 * vectorRHS.component3;
		final float component3 = vectorLHS.component1 * vectorRHS.component2 - vectorLHS.component2 * vectorRHS.component1;
		
		return new Vector3F(component1, component2, component3);
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
		final float component1 = lookAt.getComponent1() - eye.getComponent1();
		final float component2 = lookAt.getComponent2() - eye.getComponent2();
		final float component3 = lookAt.getComponent3() - eye.getComponent3();
		
		return new Vector3F(component1, component2, component3);
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
		final float component1 = lookAt.getComponent1() - eye.getComponent1();
		final float component2 = lookAt.getComponent2() - eye.getComponent2();
		final float component3 = lookAt.getComponent3() - eye.getComponent3();
		
		return new Vector3F(component1, component2, component3);
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
	public static Vector3F directionSpherical(final float u, final float v) {
		final float phi = u * PI_MULTIPLIED_BY_2;
		final float theta = v * PI;
		
		return directionSpherical(sin(theta), cos(theta), phi);
		
//		final float cosTheta = 1.0F - 2.0F * u;
//		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
//		final float phi = v * PI_MULTIPLIED_BY_2;
		
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
	public static Vector3F directionSpherical(final float sinTheta, final float cosTheta, final float phi) {
		final float component1 = sinTheta * cos(phi);
		final float component2 = sinTheta * sin(phi);
		final float component3 = cosTheta;
		
		return new Vector3F(component1, component2, component3);
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
	public static Vector3F directionSpherical(final float sinTheta, final float cosTheta, final float phi, final Vector3F x, final Vector3F y, final Vector3F z) {
		final Vector3F sphericalX = multiply(x, sinTheta * cos(phi));
		final Vector3F sphericalY = multiply(y, sinTheta * sin(phi));
		final Vector3F sphericalZ = multiply(z, cosTheta);
		
		return add(sphericalX, sphericalY, sphericalZ);
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
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector3F divide(final Vector3F vectorLHS, final float scalarRHS) {
		final float component1 = finiteOrDefault(vectorLHS.component1 / scalarRHS, 0.0F);
		final float component2 = finiteOrDefault(vectorLHS.component2 / scalarRHS, 0.0F);
		final float component3 = finiteOrDefault(vectorLHS.component3 / scalarRHS, 0.0F);
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Returns {@code Vector3F.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code Vector3F.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F faceForward(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return dotProduct(vectorLHS, vectorRHS) < 0.0F ? negate(vectorLHS) : vectorLHS;
	}
	
	/**
	 * Returns {@code Vector3F.negate(direction)} or {@code direction} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vectorLHS}, {@code vectorRHS} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @param direction the {@code Vector3F} instance to potentially negate and then return
	 * @return {@code Vector3F.negate(direction)} or {@code direction} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS}, {@code vectorRHS} or {@code direction} are {@code null}
	 */
	public static Vector3F faceForward(final Vector3F vectorLHS, final Vector3F vectorRHS, final Vector3F direction) {
		return dotProduct(vectorLHS, vectorRHS) < 0.0F ? negate(direction) : Objects.requireNonNull(direction, "direction == null");
	}
	
	/**
	 * Returns {@code Vector3F.negateComponent3(vectorRHS)} or {@code vectorRHS} as {@code vectorLHS.getComponent3()} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3F.negateComponent3(vectorRHS)} or {@code vectorRHS} as {@code vectorLHS.getComponent3()} is less than {@code 0.0F} or greater than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F faceForwardComponent3(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return vectorLHS.component3 < 0.0F ? negateComponent3(vectorRHS) : Objects.requireNonNull(vectorRHS, "vectorRHS == null");
	}
	
	/**
	 * Returns {@code Vector3F.negateComponent3(vectorRHS)} or {@code vectorRHS} as {@code vectorLHS.getComponent3()} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side to potentially negate and then return
	 * @return {@code Vector3F.negateComponent3(vectorRHS)} or {@code vectorRHS} as {@code vectorLHS.getComponent3()} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F faceForwardComponent3Negated(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return vectorLHS.component3 > 0.0F ? negateComponent3(vectorRHS) : Objects.requireNonNull(vectorRHS, "vectorRHS == null");
	}
	
	/**
	 * Returns {@code Vector3F.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return {@code Vector3F.negate(vectorLHS)} or {@code vectorLHS} as {@code Vector3F.dotProduct(vectorLHS, vectorRHS)} is greater than {@code 0.0F} or less than or equal to {@code 0.0F}, respectively
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F faceForwardNegated(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return dotProduct(vectorLHS, vectorRHS) > 0.0F ? negate(vectorLHS) : vectorLHS;
	}
	
	/**
	 * Returns a cached version of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a cached version of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F getCached(final Vector3F vector) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(vector, "vector == null"), key -> vector);
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
		final float component1 = Floats.lerp(a.component1, b.component1, t);
		final float component2 = Floats.lerp(a.component2, b.component2, t);
		final float component3 = Floats.lerp(a.component3, b.component3, t);
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector3F multiply(final Vector3F vectorLHS, final float scalarRHS) {
		final float component1 = vectorLHS.component1 * scalarRHS;
		final float component2 = vectorLHS.component2 * scalarRHS;
		final float component3 = vectorLHS.component3 * scalarRHS;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F negate(final Vector3F vector) {
		final float component1 = -vector.component1;
		final float component2 = -vector.component2;
		final float component3 = -vector.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Negates the component 1 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F negateComponent1(final Vector3F vector) {
		final float component1 = -vector.component1;
		final float component2 = +vector.component2;
		final float component3 = +vector.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Negates the component 2 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F negateComponent2(final Vector3F vector) {
		final float component1 = +vector.component1;
		final float component2 = -vector.component2;
		final float component3 = +vector.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Negates the component 3 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F negateComponent3(final Vector3F vector) {
		final float component1 = +vector.component1;
		final float component2 = +vector.component2;
		final float component3 = -vector.component3;
		
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
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F normalize(final Vector3F vector) {
		return divide(vector, vector.length());
	}
	
	/**
	 * Returns a random {@code Vector3F} instance.
	 * 
	 * @return a random {@code Vector3F} instance
	 */
	public static Vector3F random() {
		final float component1 = Floats.random();
		final float component2 = Floats.random();
		final float component3 = Floats.random();
		
		return new Vector3F(component1, component2, component3);
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
			final float component1 = dataInput.readFloat();
			final float component2 = dataInput.readFloat();
			final float component3 = dataInput.readFloat();
			
			return new Vector3F(component1, component2, component3);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code Vector3F} instance with the reciprocal (or inverse) component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the reciprocal (or inverse) component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector3F reciprocal(final Vector3F vector) {
		final float component1 = finiteOrDefault(1.0F / vector.component1, 0.0F);
		final float component2 = finiteOrDefault(1.0F / vector.component2, 0.0F);
		final float component3 = finiteOrDefault(1.0F / vector.component3, 0.0F);
		
		return new Vector3F(component1, component2, component3);
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
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return a new {@code Vector3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F subtract(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		final float component1 = vectorLHS.component1 - vectorRHS.component1;
		final float component2 = vectorLHS.component2 - vectorRHS.component2;
		final float component3 = vectorLHS.component3 - vectorRHS.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vectorRHS} with the {@link Matrix44F} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44F} instance
	 * @param vectorRHS a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F transform(final Matrix44F matrixLHS, final Vector3F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement12() * vectorRHS.component2 + matrixLHS.getElement13() * vectorRHS.component3;
		final float component2 = matrixLHS.getElement21() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2 + matrixLHS.getElement23() * vectorRHS.component3;
		final float component3 = matrixLHS.getElement31() * vectorRHS.component1 + matrixLHS.getElement32() * vectorRHS.component2 + matrixLHS.getElement33() * vectorRHS.component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vectorRHS} with the {@link OrthonormalBasis33F} {@code orthonormalBasisRHS}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS a {@code Vector3F} instance
	 * @param orthonormalBasisRHS an {@code OrthonormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static Vector3F transform(final Vector3F vectorLHS, final OrthonormalBasis33F orthonormalBasisRHS) {
		final float component1 = vectorLHS.component1 * orthonormalBasisRHS.getU().component1 + vectorLHS.component2 * orthonormalBasisRHS.getV().component1 + vectorLHS.component3 * orthonormalBasisRHS.getW().component1;
		final float component2 = vectorLHS.component1 * orthonormalBasisRHS.getU().component2 + vectorLHS.component2 * orthonormalBasisRHS.getV().component2 + vectorLHS.component3 * orthonormalBasisRHS.getW().component2;
		final float component3 = vectorLHS.component1 * orthonormalBasisRHS.getU().component3 + vectorLHS.component2 * orthonormalBasisRHS.getV().component3 + vectorLHS.component3 * orthonormalBasisRHS.getW().component3;
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the error of the {@link Point3F} {@code point} with the {@link Matrix44F} {@code matrix}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrix} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44F} instance
	 * @param point a {@code Point3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrix} or {@code point} are {@code null}
	 */
	public static Vector3F transformError(final Matrix44F matrix, final Point3F point) {
		final float component1 = gamma(3) * (abs(matrix.getElement11() * point.getComponent1()) + abs(matrix.getElement12() * point.getY()) + abs(matrix.getElement13() * point.getZ()) + abs(matrix.getElement14()));
		final float component2 = gamma(3) * (abs(matrix.getElement21() * point.getComponent1()) + abs(matrix.getElement22() * point.getY()) + abs(matrix.getElement23() * point.getZ()) + abs(matrix.getElement24()));
		final float component3 = gamma(3) * (abs(matrix.getElement31() * point.getComponent1()) + abs(matrix.getElement32() * point.getY()) + abs(matrix.getElement33() * point.getZ()) + abs(matrix.getElement34()));
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the error of the {@code Vector3F} {@code vector} with the {@link Matrix44F} {@code matrix}.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrix}, {@code point} or {@code vector} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@code Matrix44F} instance
	 * @param point a {@link Point3F} instance
	 * @param vector a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrix}, {@code point} or {@code vector} are {@code null}
	 */
	public static Vector3F transformError(final Matrix44F matrix, final Point3F point, final Vector3F vector) {
		final float component1 = (gamma(3) + 1.0F) * (abs(matrix.getElement11()) * vector.component1 + abs(matrix.getElement12()) * vector.component2 + abs(matrix.getElement13()) * vector.component3) + gamma(3) * (abs(matrix.getElement11() * point.getComponent1()) + abs(matrix.getElement12() * point.getComponent2()) + abs(matrix.getElement13() * point.getComponent3()) + abs(matrix.getElement14()));
		final float component2 = (gamma(3) + 1.0F) * (abs(matrix.getElement21()) * vector.component1 + abs(matrix.getElement22()) * vector.component2 + abs(matrix.getElement23()) * vector.component3) + gamma(3) * (abs(matrix.getElement21() * point.getComponent1()) + abs(matrix.getElement22() * point.getComponent2()) + abs(matrix.getElement23() * point.getComponent3()) + abs(matrix.getElement24()));
		final float component3 = (gamma(3) + 1.0F) * (abs(matrix.getElement31()) * vector.component1 + abs(matrix.getElement32()) * vector.component2 + abs(matrix.getElement33()) * vector.component3) + gamma(3) * (abs(matrix.getElement31() * point.getComponent1()) + abs(matrix.getElement32() * point.getComponent2()) + abs(matrix.getElement33() * point.getComponent3()) + abs(matrix.getElement34()));
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vectorRHS} with the {@link OrthonormalBasis33F} {@code orthonormalBasisRHS} in reverse order.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS a {@code Vector3F} instance
	 * @param orthonormalBasisRHS an {@code OrthonormalBasis33F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code orthonormalBasisRHS} are {@code null}
	 */
	public static Vector3F transformReverse(final Vector3F vectorLHS, final OrthonormalBasis33F orthonormalBasisRHS) {
		final float component1 = dotProduct(vectorLHS, orthonormalBasisRHS.getU());
		final float component2 = dotProduct(vectorLHS, orthonormalBasisRHS.getV());
		final float component3 = dotProduct(vectorLHS, orthonormalBasisRHS.getW());
		
		return new Vector3F(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Vector3F} {@code vectorRHS} with the {@link Matrix44F} {@code matrixLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44F} instance
	 * @param vectorRHS a {@code Vector3F} instance
	 * @return a new {@code Vector3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3F transformTranspose(final Matrix44F matrixLHS, final Vector3F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement21() * vectorRHS.component2 + matrixLHS.getElement31() * vectorRHS.component3;
		final float component2 = matrixLHS.getElement12() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2 + matrixLHS.getElement32() * vectorRHS.component3;
		final float component3 = matrixLHS.getElement13() * vectorRHS.component1 + matrixLHS.getElement23() * vectorRHS.component2 + matrixLHS.getElement33() * vectorRHS.component3;
		
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
		return vLHS.component3 * vRHS.component3 > 0.0F;
	}
	
	/**
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float dotProduct(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component1 + vectorLHS.component2 * vectorRHS.component2 + vectorLHS.component3 * vectorRHS.component3;
	}
	
	/**
	 * Returns the absolute dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector3F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3F} instance on the right-hand side
	 * @return the absolute dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float dotProductAbs(final Vector3F vectorLHS, final Vector3F vectorRHS) {
		return abs(dotProduct(vectorLHS, vectorRHS));
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