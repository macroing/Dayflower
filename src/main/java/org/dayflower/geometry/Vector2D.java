/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Doubles.NEXT_DOWN_1_3;
import static org.dayflower.utility.Doubles.NEXT_UP_1_1;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.finiteOrDefault;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.utility.Doubles;

import org.macroing.java.lang.Strings;

/**
 * A {@code Vector2D} represents a vector with two {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2D implements Node {
	/**
	 * A {@code Vector2D} instance given the component values {@code Double.NaN} and {@code Double.NaN}.
	 */
	public static final Vector2D NaN = new Vector2D(Double.NaN, Double.NaN);
	
	/**
	 * A {@code Vector2D} instance given the component values {@code 0.0D} and {@code 0.0D}.
	 */
	public static final Vector2D ZERO = new Vector2D(0.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector2D, Vector2D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double component1;
	private final double component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code 0.0D} and {@code 0.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Vector2D() {
		this(0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code point.x} and {@code point.y}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector2D(final Point2D point) {
		this(point.x, point.y);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code point.getComponent1()} and {@code point.getComponent2()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(point.getComponent1(), point.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector2D(final Point3D point) {
		this(point.getComponent1(), point.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2D(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of both components
	 */
	public Vector2D(final double component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Vector2D} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Vector2D(final double component1, final double component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2D(%s, %s)", Strings.toNonScientificNotationJava(this.component1), Strings.toNonScientificNotationJava(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2D)) {
			return false;
		} else if(!equal(this.component1, Vector2D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector2D.class.cast(object).component2)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector2D} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector2D} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final double length = length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
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
	 * Returns the length of this {@code Vector2D} instance.
	 * 
	 * @return the length of this {@code Vector2D} instance
	 */
	public double length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector2D} instance.
	 * 
	 * @return the squared length of this {@code Vector2D} instance
	 */
	public double lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Vector2D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Vector2D} instance
	 */
	public double[] toArray() {
		return new double[] {
			this.component1,
			this.component2
		};
	}
	
	/**
	 * Returns a hash code for this {@code Vector2D} instance.
	 * 
	 * @return a hash code for this {@code Vector2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2));
	}
	
	/**
	 * Writes this {@code Vector2D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.component1);
			dataOutput.writeDouble(this.component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Vector2D} instance with the absolute component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the absolute component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D absolute(final Vector2D vector) {
		final double component1 = abs(vector.component1);
		final double component2 = abs(vector.component2);
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2D} instance on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2D add(final Vector2D vectorLHS, final Vector2D vectorRHS) {
		final double component1 = vectorLHS.component1 + vectorRHS.component1;
		final double component2 = vectorLHS.component2 + vectorRHS.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Adds the component values of {@code vectorA}, {@code vectorB} and {@code vectorC}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorA a {@code Vector2D} instance
	 * @param vectorB a {@code Vector2D} instance
	 * @param vectorC a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}
	 */
	public static Vector2D add(final Vector2D vectorA, final Vector2D vectorB, final Vector2D vectorC) {
		final double component1 = vectorA.component1 + vectorB.component1 + vectorC.component1;
		final double component2 = vectorA.component2 + vectorB.component2 + vectorC.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2D direction(final Point2D eye, final Point2D lookAt) {
		final double component1 = lookAt.x - eye.x;
		final double component2 = lookAt.y - eye.y;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return a new {@code Vector2D} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2D directionNormalized(final Point2D eye, final Point2D lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code point.getX()} and {@code point.getY()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code point.getX()} and {@code point.getY()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2D directionXY(final Point3D point) {
		return new Vector2D(point.getX(), point.getY());
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code point.getY()} and {@code point.getZ()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code point.getY()} and {@code point.getZ()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2D directionYZ(final Point3D point) {
		return new Vector2D(point.getY(), point.getZ());
	}
	
	/**
	 * Returns a {@code Vector2D} instance that points in the direction of {@code point.getZ()} and {@code point.getX()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return a {@code Vector2D} instance that points in the direction of {@code point.getZ()} and {@code point.getX()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2D directionZX(final Point3D point) {
		return new Vector2D(point.getZ(), point.getX());
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2D divide(final Vector2D vectorLHS, final double scalarRHS) {
		final double component1 = finiteOrDefault(vectorLHS.component1 / scalarRHS, 0.0D);
		final double component2 = finiteOrDefault(vectorLHS.component2 / scalarRHS, 0.0D);
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a cached version of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a cached version of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D getCached(final Vector2D vector) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(vector, "vector == null"), key -> vector);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector2D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector2D} instance
	 * @param b a {@code Vector2D} instance
	 * @param t the factor
	 * @return a {@code Vector2D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector2D lerp(final Vector2D a, final Vector2D b, final double t) {
		final double component1 = Doubles.lerp(a.component1, b.component1, t);
		final double component2 = Doubles.lerp(a.component2, b.component2, t);
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2D multiply(final Vector2D vectorLHS, final double scalarRHS) {
		final double component1 = vectorLHS.component1 * scalarRHS;
		final double component2 = vectorLHS.component2 * scalarRHS;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D negate(final Vector2D vector) {
		final double component1 = -vector.component1;
		final double component2 = -vector.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Negates the component 1 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D negateComponent1(final Vector2D vector) {
		final double component1 = -vector.component1;
		final double component2 = +vector.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Negates the component 2 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D negateComponent2(final Vector2D vector) {
		final double component1 = +vector.component1;
		final double component2 = -vector.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D normalize(final Vector2D vector) {
		final double length = vector.length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return vector;
		}
		
		return divide(vector, length);
	}
	
	/**
	 * Returns a {@code Vector2D} instance that is perpendicular to {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a {@code Vector2D} instance that is perpendicular to {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D perpendicular(final Vector2D vector) {
		final double component1 = +vector.component2;
		final double component2 = -vector.component1;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a random {@code Vector2D} instance.
	 * 
	 * @return a random {@code Vector2D} instance
	 */
	public static Vector2D random() {
		final double component1 = Doubles.random() * 2.0D - 1.0D;
		final double component2 = Doubles.random() * 2.0D - 1.0D;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a random and normalized {@code Vector2D} instance.
	 * 
	 * @return a random and normalized {@code Vector2D} instance
	 */
	public static Vector2D randomNormalized() {
		return normalize(random());
	}
	
	/**
	 * Returns a new {@code Vector2D} instance with the reciprocal (or inverse) component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the reciprocal (or inverse) component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2D reciprocal(final Vector2D vector) {
		final double component1 = 1.0D / vector.component1;
		final double component2 = 1.0D / vector.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector2D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector2D read(final DataInput dataInput) {
		try {
			final double component1 = dataInput.readDouble();
			final double component2 = dataInput.readDouble();
			
			return new Vector2D(component1, component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2D} instance on the right-hand side
	 * @return a new {@code Vector2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2D subtract(final Vector2D vectorLHS, final Vector2D vectorRHS) {
		final double component1 = vectorLHS.component1 - vectorRHS.component1;
		final double component2 = vectorLHS.component2 - vectorRHS.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2D} {@code vectorRHS} with the {@link Matrix33D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33D} instance
	 * @param vectorRHS a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2D transform(final Matrix33D matrixLHS, final Vector2D vectorRHS) {
		final double component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement12() * vectorRHS.component2;
		final double component2 = matrixLHS.getElement21() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2D} {@code vectorRHS} with the {@link Matrix33D} {@code matrixLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33D} instance
	 * @param vectorRHS a {@code Vector2D} instance
	 * @return a new {@code Vector2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2D transformTranspose(final Matrix33D matrixLHS, final Vector2D vectorRHS) {
		final double component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement21() * vectorRHS.component2;
		final double component2 = matrixLHS.getElement12() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}
	 */
	public static Vector2D u() {
		return u(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(u, 0.0D)}.
	 * 
	 * @param u the value of the U-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(u, 0.0D)}
	 */
	public static Vector2D u(final double u) {
		return new Vector2D(u, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}
	 */
	public static Vector2D v() {
		return v(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, v)}.
	 * 
	 * @param v the value of the V-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, v)}
	 */
	public static Vector2D v(final double v) {
		return new Vector2D(0.0D, v);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(1.0D, 0.0D)}
	 */
	public static Vector2D x() {
		return x(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(x, 0.0D)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(x, 0.0D)}
	 */
	public static Vector2D x(final double x) {
		return new Vector2D(x, 0.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}.
	 * 
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, 1.0D)}
	 */
	public static Vector2D y() {
		return y(1.0D);
	}
	
	/**
	 * Returns a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, y)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector2D} instance equivalent to {@code new Vector2D(0.0D, y)}
	 */
	public static Vector2D y(final double y) {
		return new Vector2D(0.0D, y);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2D} instance on the left-hand side
	 * @param vRHS the {@code Vector2D} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector2D vLHS, final Vector2D vRHS) {
		return isZero(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the cross product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2D} instance on the right-hand side
	 * @return the cross product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static double crossProduct(final Vector2D vectorLHS, final Vector2D vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component2 - vectorLHS.component2 * vectorRHS.component1;
	}
	
	/**
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector2D} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2D} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static double dotProduct(final Vector2D vectorLHS, final Vector2D vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component1 + vectorLHS.component2 * vectorRHS.component2;
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