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

import static org.dayflower.util.Doubles.equal;
import static org.dayflower.util.Doubles.max;
import static org.dayflower.util.Doubles.min;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * A {@code Point3D} denotes a 3-dimensional point with three coordinates, of type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point3D {
	private final double component1;
	private final double component2;
	private final double component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point3D} instance given the component values {@code 0.0D}, {@code 0.0D} and {@code 0.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3D(0.0D, 0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Point3D() {
		this(0.0D, 0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Point3D} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()} and {@code vector.getComponent3()}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3D(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Point3D(final Vector3D vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Point3D} instance given the component values {@code component}, {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value for all components
	 */
	public Point3D(final double component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Point3D} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Point3D(final double component1, final double component2, final double component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point3D(%+.10f, %+.10f, %+.10f)", Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
	}
	
	/**
	 * Compares {@code object} to this {@code Point3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point3D)) {
			return false;
		} else if(!equal(this.component1, Point3D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point3D.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Point3D.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of the component at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code 2}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the component whose value to return
	 * @return the value of the component at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code 2}
	 */
	public double getComponent(final int index) {
		switch(index) {
			case 0:
				return this.component1;
			case 1:
				return this.component2;
			case 2:
				return this.component3;
			default:
				throw new IllegalArgumentException(String.format("Illegal index: index=%s", Integer.toString(index)));
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
	 * Returns a hash code for this {@code Point3D} instance.
	 * 
	 * @return a hash code for this {@code Point3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point3D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point3D add(final Point3D pointLHS, final Vector3D vectorRHS) {
		final double component1 = pointLHS.component1 + vectorRHS.getComponent1();
		final double component2 = pointLHS.component2 + vectorRHS.getComponent2();
		final double component3 = pointLHS.component3 + vectorRHS.getComponent3();
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} multiplied by {@code scalar} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point3D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector3D} instance on the right-hand side
	 * @param scalar the scalar to multiply the component values of {@code vectorRHS} with
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point3D add(final Point3D pointLHS, final Vector3D vectorRHS, final double scalar) {
		final double component1 = pointLHS.component1 + vectorRHS.getComponent1() * scalar;
		final double component2 = pointLHS.component2 + vectorRHS.getComponent2() * scalar;
		final double component3 = pointLHS.component3 + vectorRHS.getComponent3() * scalar;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Adds {@code scalarRHS} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If {@code pointLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pointLHS} is {@code null}
	 */
	public static Point3D add(final Point3D pointLHS, final double scalarRHS) {
		final double component1 = pointLHS.component1 + scalarRHS;
		final double component2 = pointLHS.component2 + scalarRHS;
		final double component3 = pointLHS.component3 + scalarRHS;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @return a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3D centroid(final Point3D a, final Point3D b, final Point3D c) {
		final double component1 = (a.component1 + b.component1 + c.component1) / 3.0D;
		final double component2 = (a.component2 + b.component2 + c.component2) / 3.0D;
		final double component3 = (a.component3 + b.component3 + c.component3) / 3.0D;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the largest component values.
	 * 
	 * @return a new {@code Point3D} instance with the largest component values
	 */
	public static Point3D maximum() {
		final double component1 = Double.MAX_VALUE;
		final double component2 = Double.MAX_VALUE;
		final double component3 = Double.MAX_VALUE;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the largest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the largest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3D maximum(final Point3D a, final Point3D b) {
		final double component1 = max(a.component1, b.component1);
		final double component2 = max(a.component2, b.component2);
		final double component3 = max(a.component3, b.component3);
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the largest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the largest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3D maximum(final Point3D a, final Point3D b, final Point3D c) {
		final double component1 = max(a.component1, b.component1, c.component1);
		final double component2 = max(a.component2, b.component2, c.component2);
		final double component3 = max(a.component3, b.component3, c.component3);
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance that represents the midpoint of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @return a new {@code Point3D} instance that represents the midpoint of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3D midpoint(final Point3D a, final Point3D b) {
		final double component1 = (a.component1 + b.component1) * 0.5D;
		final double component2 = (a.component2 + b.component2) * 0.5D;
		final double component3 = (a.component3 + b.component3) * 0.5D;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the smallest component values.
	 * 
	 * @return a new {@code Point3D} instance with the smallest component values
	 */
	public static Point3D minimum() {
		final double component1 = Double.MIN_VALUE;
		final double component2 = Double.MIN_VALUE;
		final double component3 = Double.MIN_VALUE;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the smallest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the smallest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3D minimum(final Point3D a, final Point3D b) {
		final double component1 = min(a.component1, b.component1);
		final double component2 = min(a.component2, b.component2);
		final double component3 = min(a.component3, b.component3);
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the smallest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the smallest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3D minimum(final Point3D a, final Point3D b, final Point3D c) {
		final double component1 = min(a.component1, b.component1, c.component1);
		final double component2 = min(a.component2, b.component2, c.component2);
		final double component3 = min(a.component3, b.component3, c.component3);
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point3D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point3D subtract(final Point3D pointLHS, final Vector3D vectorRHS) {
		final double component1 = pointLHS.component1 - vectorRHS.getComponent1();
		final double component2 = pointLHS.component2 - vectorRHS.getComponent2();
		final double component3 = pointLHS.component3 - vectorRHS.getComponent3();
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Subtracts {@code scalarRHS} from the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the subtraction.
	 * <p>
	 * If {@code pointLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pointLHS} is {@code null}
	 */
	public static Point3D subtract(final Point3D pointLHS, final double scalarRHS) {
		final double component1 = pointLHS.component1 - scalarRHS;
		final double component2 = pointLHS.component2 - scalarRHS;
		final double component3 = pointLHS.component3 - scalarRHS;
		
		return new Point3D(component1, component2, component3);
	}
	
	/**
	 * Transforms the {@code Point3D} {@code pointRHS} with the {@link Matrix44D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param pointRHS a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
//	TODO: Add!
//	public static Point3D transform(final Matrix44D matrixLHS, final Point3D pointRHS) {
//		final double component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14();
//		final double component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24();
//		final double component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34();
		
//		return new Point3D(component1, component2, component3);
//	}
	
	/**
	 * Transforms the {@code Point3D} {@code pointRHS} with the {@link Matrix44D} {@code matrixLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param pointRHS a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
//	TODO: Add!
//	public static Point3D transformAndDivide(final Matrix44D matrixLHS, final Point3D pointRHS) {
//		final double component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14();
//		final double component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24();
//		final double component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34();
//		final double component4 = matrixLHS.getElement41() * pointRHS.component1 + matrixLHS.getElement42() * pointRHS.component2 + matrixLHS.getElement43() * pointRHS.component3 + matrixLHS.getElement44();
		
//		return equal(component4, 1.0D) || equal(component4, 0.0D) ? new Point3D(component1, component2, component3) : new Point3D(component1 / component4, component2 / component4, component3 / component4);
//	}
	
	/**
	 * Returns the distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point3D} instance denoting the eye to look from
	 * @param lookAt a {@code Point3D} instance denoting the target to look at
	 * @return the distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static double distance(final Point3D eye, final Point3D lookAt) {
		return Vector3D.direction(eye, lookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point3D} instance denoting the eye to look from
	 * @param lookAt a {@code Point3D} instance denoting the target to look at
	 * @return the squared distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static double distanceSquared(final Point3D eye, final Point3D lookAt) {
		return Vector3D.direction(eye, lookAt).lengthSquared();
	}
}