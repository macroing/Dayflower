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

import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.utility.Doubles;

/**
 * A {@code Point4D} denotes a 4-dimensional point with four coordinates, of type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point4D implements Node {
	private static final Map<Point4D, Point4D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double component1;
	private final double component2;
	private final double component3;
	private final double component4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code 0.0D}, {@code 0.0D}, {@code 0.0D} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(0.0D, 0.0D, 0.0D, 1.0D);
	 * }
	 * </pre>
	 */
	public Point4D() {
		this(0.0D, 0.0D, 0.0D, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code point.getComponent1()}, {@code point.getComponent2()}, {@code point.getComponent3()} and {@code 1.0D}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(point.getComponent1(), point.getComponent2(), point.getComponent3(), 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Point4D(final Point3D point) {
		this(point.getComponent1(), point.getComponent2(), point.getComponent3(), 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()}, {@code vector.getComponent3()} and {@code 1.0D}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Point4D(final Vector3D vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code component}, {@code component}, {@code component} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(component, component, component, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param component the value for most components
	 */
	public Point4D(final double component) {
		this(component, component, component, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(component1, component2, component3, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Point4D(final double component1, final double component2, final double component3) {
		this(component1, component2, component3, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Point4D(final double component1, final double component2, final double component3, final double component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point4D(%+.10f, %+.10f, %+.10f, %+.10f)", Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3), Double.valueOf(this.component4));
	}
	
	/**
	 * Compares {@code object} to this {@code Point4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point4D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point4D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point4D)) {
			return false;
		} else if(!equal(this.component1, Point4D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point4D.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Point4D.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Point4D.class.cast(object).component4)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of the component at index {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of the component whose value to return
	 * @return the value of the component at index {@code index}
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0} or greater than {@code 3}
	 */
	public double getComponent(final int index) {
		switch(index) {
			case 0:
				return this.component1;
			case 1:
				return this.component2;
			case 2:
				return this.component3;
			case 3:
				return this.component4;
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
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public double getComponent4() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public double getW() {
		return this.component4;
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
	 * Returns a {@code double[]} representation of this {@code Point4D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Point4D} instance
	 */
	public double[] toArray() {
		return new double[] {
			this.component1,
			this.component2,
			this.component3,
			this.component4
		};
	}
	
	/**
	 * Returns a hash code for this {@code Point4D} instance.
	 * 
	 * @return a hash code for this {@code Point4D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3), Double.valueOf(this.component4));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a cached version of {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point4D} instance
	 * @return a cached version of {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Point4D getCached(final Point4D point) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(point, "point == null"), key -> point);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point4D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point4D} instance
	 * @param b a {@code Point4D} instance
	 * @param t the factor
	 * @return a {@code Point4D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point4D lerp(final Point4D a, final Point4D b, final double t) {
		final double component1 = Doubles.lerp(a.component1, b.component1, t);
		final double component2 = Doubles.lerp(a.component2, b.component2, t);
		final double component3 = Doubles.lerp(a.component3, b.component3, t);
		final double component4 = Doubles.lerp(a.component4, b.component4, t);
		
		return new Point4D(component1, component2, component3, component4);
	}
	
	/**
	 * Transforms the {@code Point4D} {@code pointRHS} with the {@link Matrix44D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Point4D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param pointRHS a {@code Point4D} instance
	 * @return a new {@code Point4D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point4D transform(final Matrix44D matrixLHS, final Point4D pointRHS) {
		final double component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14() * pointRHS.component4;
		final double component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24() * pointRHS.component4;
		final double component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34() * pointRHS.component4;
		final double component4 = matrixLHS.getElement41() * pointRHS.component1 + matrixLHS.getElement42() * pointRHS.component2 + matrixLHS.getElement43() * pointRHS.component3 + matrixLHS.getElement44() * pointRHS.component4;
		
		return new Point4D(component1, component2, component3, component4);
	}
	
	/**
	 * Transforms the {@code Point4D} {@code pointRHS} with the {@link Matrix44D} {@code matrixLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point4D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44D} instance
	 * @param pointRHS a {@code Point4D} instance
	 * @return a new {@code Point4D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point4D transformAndDivide(final Matrix44D matrixLHS, final Point4D pointRHS) {
		final double component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14() * pointRHS.component4;
		final double component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24() * pointRHS.component4;
		final double component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34() * pointRHS.component4;
		final double component4 = matrixLHS.getElement41() * pointRHS.component1 + matrixLHS.getElement42() * pointRHS.component2 + matrixLHS.getElement43() * pointRHS.component3 + matrixLHS.getElement44() * pointRHS.component4;
		
		return equal(component4, 1.0D) || isZero(component4) ? new Point4D(component1, component2, component3, component4) : new Point4D(component1 / component4, component2 / component4, component3 / component4, component4);
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