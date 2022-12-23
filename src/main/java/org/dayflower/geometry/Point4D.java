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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;

/**
 * A {@code Point4D} represents a point with four {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point4D implements Node {
	private static final Map<Point4D, Point4D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The W-component of this {@code Point4D} instance.
	 */
	public final double w;
	
	/**
	 * The X-component of this {@code Point4D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Point4D} instance.
	 */
	public final double y;
	
	/**
	 * The Z-component of this {@code Point4D} instance.
	 */
	public final double z;
	
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
	 * Constructs a new {@code Point4D} instance given the component values {@code p.x}, {@code p.y}, {@code p.z} and {@code 1.0D}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(p.x, p.y, p.z, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point4D(final Point3D p) {
		this(p.x, p.y, p.z, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code 1.0D}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(v.x, v.y, v.z, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point4D(final Vector3D v) {
		this(v.x, v.y, v.z, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code v.w}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(v.x, v.y, v.z, v.w);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point4D(final Vector4D v) {
		this(v.x, v.y, v.z, v.w);
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
	 * Constructs a new {@code Point4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4D(x, y, z, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Point4D(final double x, final double y, final double z) {
		this(x, y, z, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Point4D} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Point4D(final double x, final double y, final double z, final double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point4D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
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
		} else if(!Doubles.equals(this.w, Point4D.class.cast(object).w)) {
			return false;
		} else if(!Doubles.equals(this.x, Point4D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Point4D.class.cast(object).y)) {
			return false;
		} else if(!Doubles.equals(this.z, Point4D.class.cast(object).z)) {
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
				return this.x;
			case 1:
				return this.y;
			case 2:
				return this.z;
			case 3:
				return this.w;
			default:
				throw new IllegalArgumentException(String.format("Illegal index: index=%s", Integer.toString(index)));
		}
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Point4D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Point4D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y, this.z, this.w};
	}
	
	/**
	 * Returns a hash code for this {@code Point4D} instance.
	 * 
	 * @return a hash code for this {@code Point4D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.w), Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Point4D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.w);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a cached version of {@code p}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point4D} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point4D getCached(final Point4D p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
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
		return new Point4D(Doubles.lerp(a.x, b.x, t), Doubles.lerp(a.y, b.y, t), Doubles.lerp(a.z, b.z, t), Doubles.lerp(a.w, b.w, t));
	}
	
	/**
	 * Returns a new {@code Point4D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point4D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point4D read(final DataInput dataInput) {
		try {
			return new Point4D(dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Transforms the {@code Point4D} {@code pRHS} with the {@link Matrix44D} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point4D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param pRHS a {@code Point4D} instance
	 * @return a new {@code Point4D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point4D transform(final Matrix44D mLHS, final Point4D pRHS) {
		final double x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14 * pRHS.w;
		final double y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24 * pRHS.w;
		final double z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34 * pRHS.w;
		final double w = mLHS.element41 * pRHS.x + mLHS.element42 * pRHS.y + mLHS.element43 * pRHS.z + mLHS.element44 * pRHS.w;
		
		return new Point4D(x, y, z, w);
	}
	
	/**
	 * Transforms the {@code Point4D} {@code pRHS} with the {@link Matrix44D} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point4D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param pRHS a {@code Point4D} instance
	 * @return a new {@code Point4D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point4D transformAndDivide(final Matrix44D mLHS, final Point4D pRHS) {
		final double x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14 * pRHS.w;
		final double y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24 * pRHS.w;
		final double z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34 * pRHS.w;
		final double w = mLHS.element41 * pRHS.x + mLHS.element42 * pRHS.y + mLHS.element43 * pRHS.z + mLHS.element44 * pRHS.w;
		
		return Doubles.equals(w, 1.0D) || Doubles.isZero(w) ? new Point4D(x, y, z, w) : new Point4D(x / w, y / w, z / w, w);
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