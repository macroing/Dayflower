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

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.utility.Floats;

/**
 * A {@code Point4F} represents a point with four {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point4F implements Node {
	private static final Map<Point4F, Point4F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float component1;
	private final float component2;
	private final float component3;
	private final float component4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code 0.0F}, {@code 0.0F}, {@code 0.0F} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(0.0F, 0.0F, 0.0F, 1.0F);
	 * }
	 * </pre>
	 */
	public Point4F() {
		this(0.0F, 0.0F, 0.0F, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code point.getComponent1()}, {@code point.getComponent2()}, {@code point.getComponent3()} and {@code 1.0F}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(point.getComponent1(), point.getComponent2(), point.getComponent3(), 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Point4F(final Point3F point) {
		this(point.getComponent1(), point.getComponent2(), point.getComponent3(), 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()}, {@code vector.getComponent3()} and {@code 1.0F}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Point4F(final Vector3F vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code vector.getComponent1()}, {@code vector.getComponent2()}, {@code vector.getComponent3()} and {@code vector.getComponent4()}.
	 * <p>
	 * If {@code vector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), vector.getComponent4());
	 * }
	 * </pre>
	 * 
	 * @param vector a {@link Vector4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code vector} is {@code null}
	 */
	public Point4F(final Vector4F vector) {
		this(vector.getComponent1(), vector.getComponent2(), vector.getComponent3(), vector.getComponent4());
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code component}, {@code component}, {@code component} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(component, component, component, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param component the value for most components
	 */
	public Point4F(final float component) {
		this(component, component, component, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(component1, component2, component3, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Point4F(final float component1, final float component2, final float component3) {
		this(component1, component2, component3, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Point4F(final float component1, final float component2, final float component3, final float component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point4F(%+.10f, %+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
	}
	
	/**
	 * Compares {@code object} to this {@code Point4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point4F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point4F)) {
			return false;
		} else if(!equal(this.component1, Point4F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point4F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Point4F.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Point4F.class.cast(object).component4)) {
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
	public float getComponent(final int index) {
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
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public float getComponent4() {
		return this.component4;
	}
	
	/**
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component4;
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
	 * Returns a {@code float[]} representation of this {@code Point4F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Point4F} instance
	 */
	public float[] toArray() {
		return new float[] {
			this.component1,
			this.component2,
			this.component3,
			this.component4
		};
	}
	
	/**
	 * Returns a hash code for this {@code Point4F} instance.
	 * 
	 * @return a hash code for this {@code Point4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
	}
	
	/**
	 * Writes this {@code Point4F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a cached version of {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point4F} instance
	 * @return a cached version of {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Point4F getCached(final Point4F point) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(point, "point == null"), key -> point);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point4F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point4F} instance
	 * @param b a {@code Point4F} instance
	 * @param t the factor
	 * @return a {@code Point4F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point4F lerp(final Point4F a, final Point4F b, final float t) {
		final float component1 = Floats.lerp(a.component1, b.component1, t);
		final float component2 = Floats.lerp(a.component2, b.component2, t);
		final float component3 = Floats.lerp(a.component3, b.component3, t);
		final float component4 = Floats.lerp(a.component4, b.component4, t);
		
		return new Point4F(component1, component2, component3, component4);
	}
	
	/**
	 * Returns a new {@code Point4F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point4F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point4F read(final DataInput dataInput) {
		try {
			final float component1 = dataInput.readFloat();
			final float component2 = dataInput.readFloat();
			final float component3 = dataInput.readFloat();
			final float component4 = dataInput.readFloat();
			
			return new Point4F(component1, component2, component3, component4);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Transforms the {@code Point4F} {@code pointRHS} with the {@link Matrix44F} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Point4F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44F} instance
	 * @param pointRHS a {@code Point4F} instance
	 * @return a new {@code Point4F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point4F transform(final Matrix44F matrixLHS, final Point4F pointRHS) {
		final float component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14() * pointRHS.component4;
		final float component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24() * pointRHS.component4;
		final float component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34() * pointRHS.component4;
		final float component4 = matrixLHS.getElement41() * pointRHS.component1 + matrixLHS.getElement42() * pointRHS.component2 + matrixLHS.getElement43() * pointRHS.component3 + matrixLHS.getElement44() * pointRHS.component4;
		
		return new Point4F(component1, component2, component3, component4);
	}
	
	/**
	 * Transforms the {@code Point4F} {@code pointRHS} with the {@link Matrix44F} {@code matrixLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point4F} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix44F} instance
	 * @param pointRHS a {@code Point4F} instance
	 * @return a new {@code Point4F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point4F transformAndDivide(final Matrix44F matrixLHS, final Point4F pointRHS) {
		final float component1 = matrixLHS.getElement11() * pointRHS.component1 + matrixLHS.getElement12() * pointRHS.component2 + matrixLHS.getElement13() * pointRHS.component3 + matrixLHS.getElement14() * pointRHS.component4;
		final float component2 = matrixLHS.getElement21() * pointRHS.component1 + matrixLHS.getElement22() * pointRHS.component2 + matrixLHS.getElement23() * pointRHS.component3 + matrixLHS.getElement24() * pointRHS.component4;
		final float component3 = matrixLHS.getElement31() * pointRHS.component1 + matrixLHS.getElement32() * pointRHS.component2 + matrixLHS.getElement33() * pointRHS.component3 + matrixLHS.getElement34() * pointRHS.component4;
		final float component4 = matrixLHS.getElement41() * pointRHS.component1 + matrixLHS.getElement42() * pointRHS.component2 + matrixLHS.getElement43() * pointRHS.component3 + matrixLHS.getElement44() * pointRHS.component4;
		
		return equal(component4, 1.0F) || isZero(component4) ? new Point4F(component1, component2, component3, component4) : new Point4F(component1 / component4, component2 / component4, component3 / component4, component4);
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