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

import static org.dayflower.utility.Floats.NEXT_DOWN_1_3;
import static org.dayflower.utility.Floats.NEXT_UP_1_1;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.finiteOrDefault;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.java.lang.Strings;
import org.dayflower.node.Node;
import org.dayflower.utility.Floats;

/**
 * A {@code Vector2F} represents a vector with two {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Vector2F implements Node {
	/**
	 * A {@code Vector2F} instance given the component values {@code Float.NaN} and {@code Float.NaN}.
	 */
	public static final Vector2F NaN = new Vector2F(Float.NaN, Float.NaN);
	
	/**
	 * A {@code Vector2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 */
	public static final Vector2F ZERO = new Vector2F(0.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Vector2F, Vector2F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	private final float component1;
	private final float component2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Vector2F() {
		this(0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code point.getComponent1()} and {@code point.getComponent2()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(point.getComponent1(), point.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector2F(final Point2F point) {
		this(point.getComponent1(), point.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code point.getComponent1()} and {@code point.getComponent2()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(point.getComponent1(), point.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Vector2F(final Point3F point) {
		this(point.getComponent1(), point.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Vector2F(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of both components
	 */
	public Vector2F(final float component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Vector2F} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Vector2F(final float component1, final float component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Vector2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Vector2F(%s, %s)", Strings.toNonScientificNotationJava(this.component1), Strings.toNonScientificNotationJava(this.component2));
	}
	
	/**
	 * Compares {@code object} to this {@code Vector2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Vector2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Vector2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Vector2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Vector2F)) {
			return false;
		} else if(!equal(this.component1, Vector2F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Vector2F.class.cast(object).component2)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Vector2F} instance is a unit vector, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Vector2F} instance is a unit vector, {@code false} otherwise
	 */
	public boolean isUnitVector() {
		final float length = length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		return isLengthGTEThreshold && isLengthLTEThreshold;
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
	 * Returns the length of this {@code Vector2F} instance.
	 * 
	 * @return the length of this {@code Vector2F} instance
	 */
	public float length() {
		return sqrt(lengthSquared());
	}
	
	/**
	 * Returns the squared length of this {@code Vector2F} instance.
	 * 
	 * @return the squared length of this {@code Vector2F} instance
	 */
	public float lengthSquared() {
		return this.component1 * this.component1 + this.component2 * this.component2;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Vector2F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Vector2F} instance
	 */
	public float[] toArray() {
		return new float[] {
			this.component1,
			this.component2
		};
	}
	
	/**
	 * Returns a hash code for this {@code Vector2F} instance.
	 * 
	 * @return a hash code for this {@code Vector2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	/**
	 * Writes this {@code Vector2F} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code Vector2F} instance with the absolute component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the absolute component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F absolute(final Vector2F vector) {
		final float component1 = abs(vector.component1);
		final float component2 = abs(vector.component2);
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} to the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F add(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		final float component1 = vectorLHS.component1 + vectorRHS.component1;
		final float component2 = vectorLHS.component2 + vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Adds the component values of {@code vectorA}, {@code vectorB} and {@code vectorC}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the addition.
	 * <p>
	 * If either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector addition is performed componentwise.
	 * 
	 * @param vectorA a {@code Vector2F} instance
	 * @param vectorB a {@code Vector2F} instance
	 * @param vectorC a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code vectorA}, {@code vectorB} or {@code vectorC} are {@code null}
	 */
	public static Vector2F add(final Vector2F vectorA, final Vector2F vectorB, final Vector2F vectorC) {
		final float component1 = vectorA.component1 + vectorB.component1 + vectorC.component1;
		final float component2 = vectorA.component2 + vectorB.component2 + vectorC.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2F direction(final Point2F eye, final Point2F lookAt) {
		final float component1 = lookAt.getComponent1() - eye.getComponent1();
		final float component2 = lookAt.getComponent2() - eye.getComponent2();
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@link Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return a new {@code Vector2F} instance that is pointing in the direction of {@code eye} to {@code lookAt} and is normalized
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static Vector2F directionNormalized(final Point2F eye, final Point2F lookAt) {
		return normalize(direction(eye, lookAt));
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code point.getX()} and {@code point.getY()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code point.getX()} and {@code point.getY()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2F directionXY(final Point3F point) {
		return new Vector2F(point.getX(), point.getY());
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code point.getY()} and {@code point.getZ()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code point.getY()} and {@code point.getZ()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2F directionYZ(final Point3F point) {
		return new Vector2F(point.getY(), point.getZ());
	}
	
	/**
	 * Returns a {@code Vector2F} instance that points in the direction of {@code point.getZ()} and {@code point.getX()}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a {@code Vector2F} instance that points in the direction of {@code point.getZ()} and {@code point.getX()}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Vector2F directionZX(final Point3F point) {
		return new Vector2F(point.getZ(), point.getX());
	}
	
	/**
	 * Divides the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the division.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector division is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2F divide(final Vector2F vectorLHS, final float scalarRHS) {
		final float component1 = finiteOrDefault(vectorLHS.component1 / scalarRHS, 0.0F);
		final float component2 = finiteOrDefault(vectorLHS.component2 / scalarRHS, 0.0F);
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a cached version of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a cached version of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F getCached(final Vector2F vector) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(vector, "vector == null"), key -> vector);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Vector2F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Vector2F} instance
	 * @param b a {@code Vector2F} instance
	 * @param t the factor
	 * @return a {@code Vector2F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Vector2F lerp(final Vector2F a, final Vector2F b, final float t) {
		final float component1 = Floats.lerp(a.component1, b.component1, t);
		final float component2 = Floats.lerp(a.component2, b.component2, t);
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Multiplies the component values of {@code vectorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the multiplication.
	 * <p>
	 * If {@code vectorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector multiplication is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code vectorLHS} is {@code null}
	 */
	public static Vector2F multiply(final Vector2F vectorLHS, final float scalarRHS) {
		final float component1 = vectorLHS.component1 * scalarRHS;
		final float component2 = vectorLHS.component2 * scalarRHS;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Negates the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F negate(final Vector2F vector) {
		final float component1 = -vector.component1;
		final float component2 = -vector.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Negates the component 1 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F negateComponent1(final Vector2F vector) {
		final float component1 = -vector.component1;
		final float component2 = +vector.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Negates the component 2 value of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the negation.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F negateComponent2(final Vector2F vector) {
		final float component1 = +vector.component1;
		final float component2 = -vector.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Normalizes the component values of {@code vector}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the normalization.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F normalize(final Vector2F vector) {
		final float length = vector.length();
		
		final boolean isLengthGTEThreshold = length >= NEXT_DOWN_1_3;
		final boolean isLengthLTEThreshold = length <= NEXT_UP_1_1;
		
		if(isLengthGTEThreshold && isLengthLTEThreshold) {
			return vector;
		}
		
		return divide(vector, length);
	}
	
	/**
	 * Returns a {@code Vector2F} instance that is perpendicular to {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a {@code Vector2F} instance that is perpendicular to {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F perpendicular(final Vector2F vector) {
		final float component1 = +vector.component2;
		final float component2 = -vector.component1;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a random {@code Vector2F} instance.
	 * 
	 * @return a random {@code Vector2F} instance
	 */
	public static Vector2F random() {
		final float component1 = Floats.random() * 2.0F - 1.0F;
		final float component2 = Floats.random() * 2.0F - 1.0F;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a random and normalized {@code Vector2F} instance.
	 * 
	 * @return a random and normalized {@code Vector2F} instance
	 */
	public static Vector2F randomNormalized() {
		return normalize(random());
	}
	
	/**
	 * Returns a new {@code Vector2F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Vector2F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Vector2F read(final DataInput dataInput) {
		try {
			final float component1 = dataInput.readFloat();
			final float component2 = dataInput.readFloat();
			
			return new Vector2F(component1, component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code Vector2F} instance with the reciprocal (or inverse) component values of {@code vector}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vector a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the reciprocal (or inverse) component values of {@code vector}
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public static Vector2F reciprocal(final Vector2F vector) {
		final float component1 = 1.0F / vector.component1;
		final float component2 = 1.0F / vector.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code vectorLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Vector subtraction is performed componentwise.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return a new {@code Vector2F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F subtract(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		final float component1 = vectorLHS.component1 - vectorRHS.component1;
		final float component2 = vectorLHS.component2 - vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vectorRHS} with the {@link Matrix33F} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33F} instance
	 * @param vectorRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F transform(final Matrix33F matrixLHS, final Vector2F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement12() * vectorRHS.component2;
		final float component2 = matrixLHS.getElement21() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Transforms the {@code Vector2F} {@code vectorRHS} with the {@link Matrix33F} {@code matrixLHS} in transpose order.
	 * <p>
	 * Returns a new {@code Vector2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33F} instance
	 * @param vectorRHS a {@code Vector2F} instance
	 * @return a new {@code Vector2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Vector2F transformTranspose(final Matrix33F matrixLHS, final Vector2F vectorRHS) {
		final float component1 = matrixLHS.getElement11() * vectorRHS.component1 + matrixLHS.getElement21() * vectorRHS.component2;
		final float component2 = matrixLHS.getElement12() * vectorRHS.component1 + matrixLHS.getElement22() * vectorRHS.component2;
		
		return new Vector2F(component1, component2);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}
	 */
	public static Vector2F u() {
		return u(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(u, 0.0F)}.
	 * 
	 * @param u the value of the U-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(u, 0.0F)}
	 */
	public static Vector2F u(final float u) {
		return new Vector2F(u, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}
	 */
	public static Vector2F v() {
		return v(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, v)}.
	 * 
	 * @param v the value of the V-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, v)}
	 */
	public static Vector2F v(final float v) {
		return new Vector2F(0.0F, v);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(1.0F, 0.0F)}
	 */
	public static Vector2F x() {
		return x(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(x, 0.0F)}.
	 * 
	 * @param x the value of the X-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(x, 0.0F)}
	 */
	public static Vector2F x(final float x) {
		return new Vector2F(x, 0.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}.
	 * 
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, 1.0F)}
	 */
	public static Vector2F y() {
		return y(1.0F);
	}
	
	/**
	 * Returns a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, y)}.
	 * 
	 * @param y the value of the Y-component
	 * @return a new {@code Vector2F} instance equivalent to {@code new Vector2F(0.0F, y)}
	 */
	public static Vector2F y(final float y) {
		return new Vector2F(0.0F, y);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise.
	 * <p>
	 * If either {@code vLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vLHS the {@code Vector2F} instance on the left-hand side
	 * @param vRHS the {@code Vector2F} instance on the right-hand side
	 * @return {@code true} if, and only if, {@code vLHS} and {@code vRHS} are orthogonal, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code vLHS} or {@code vRHS} are {@code null}
	 */
	public static boolean orthogonal(final Vector2F vLHS, final Vector2F vRHS) {
		return isZero(dotProduct(vLHS, vRHS));
	}
	
	/**
	 * Returns the cross product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return the cross product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float crossProduct(final Vector2F vectorLHS, final Vector2F vectorRHS) {
		return vectorLHS.component1 * vectorRHS.component2 - vectorLHS.component2 * vectorRHS.component1;
	}
	
	/**
	 * Returns the dot product of {@code vectorLHS} and {@code vectorRHS}.
	 * <p>
	 * If either {@code vectorLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param vectorLHS the {@code Vector2F} instance on the left-hand side
	 * @param vectorRHS the {@code Vector2F} instance on the right-hand side
	 * @return the dot product of {@code vectorLHS} and {@code vectorRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code vectorLHS} or {@code vectorRHS} are {@code null}
	 */
	public static float dotProduct(final Vector2F vectorLHS, final Vector2F vectorRHS) {
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