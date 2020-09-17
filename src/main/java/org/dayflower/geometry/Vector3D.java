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

import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.equal;
import static org.dayflower.util.Doubles.sqrt;

import java.util.Objects;

/**
 * A {@code Vector3D} denotes a 3-dimensional vector with three components, of type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector3D {
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
		final double component1 = vectorLHS.component1 / scalarRHS;
		final double component2 = vectorLHS.component2 / scalarRHS;
		final double component3 = vectorLHS.component3 / scalarRHS;
		
		return new Vector3D(component1, component2, component3);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with the component values of {@code vectorRHS}.
	 * <p>
	 * Returns a new {@code Vector3D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector3D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector3D} instance on the right-hand side
	 * @return a new {@code Vector3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector3D multiply(final Vector3D vectorLHS, final Vector3D vectorRHS) {
		final double component1 = vectorLHS.component1 * vectorRHS.component1;
		final double component2 = vectorLHS.component2 * vectorRHS.component2;
		final double component3 = vectorLHS.component3 * vectorRHS.component3;
		
		return new Vector3D(component1, component2, component3);
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
}