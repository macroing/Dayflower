/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.visitor.Node;

/**
 * A {@code Point3F} represents a point with three {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point3F implements Node {
	/**
	 * A {@code Point3F} instance with the largest component values.
	 */
	public static final Point3F MAXIMUM = maximum();
	
	/**
	 * A {@code Point3F} instance with the smallest component values.
	 */
	public static final Point3F MINIMUM = minimum();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Point3F, Point3F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Point3F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Point3F} instance.
	 */
	public final float y;
	
	/**
	 * The Z-component of this {@code Point3F} instance.
	 */
	public final float z;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code 0.0F}, {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(0.0F, 0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Point3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code p.x}, {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(p.x, p.y, p.z);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point3F(final Point4F p) {
		this(p.x, p.y, p.z);
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code v.x}, {@code v.y} and {@code v.z}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(v.x, v.y, v.z);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point3F(final Vector3F v) {
		this(v.x, v.y, v.z);
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code component}, {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value for all components
	 */
	public Point3F(final float component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Point3F} instance given the component values {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Point3F(final float x, final float y, final float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point3F(%s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z));
	}
	
	/**
	 * Compares {@code object} to this {@code Point3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point3F)) {
			return false;
		} else if(!Floats.equals(this.x, Point3F.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, Point3F.class.cast(object).y)) {
			return false;
		} else if(!Floats.equals(this.z, Point3F.class.cast(object).z)) {
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
	public float getComponent(final int index) {
		switch(index) {
			case 0:
				return this.x;
			case 1:
				return this.y;
			case 2:
				return this.z;
			default:
				throw new IllegalArgumentException(String.format("Illegal index: index=%s", Integer.toString(index)));
		}
	}
	
	/**
	 * Returns the spherical phi angle.
	 * 
	 * @return the spherical phi angle
	 */
	public float sphericalPhi() {
		return Floats.addLessThan(Floats.atan2(this.y, this.x), 0.0F, Floats.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Point3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Point3F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y, this.z};
	}
	
	/**
	 * Returns a hash code for this {@code Point3F} instance.
	 * 
	 * @return a hash code for this {@code Point3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Point3F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.x);
			dataOutput.writeFloat(this.y);
			dataOutput.writeFloat(this.z);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3F add(final Point3F pLHS, final Vector3F vRHS) {
		return new Point3F(pLHS.x + vRHS.x, pLHS.y + vRHS.y, pLHS.z + vRHS.z);
	}
	
	/**
	 * Adds the component values of {@code vRHS} multiplied by {@code s} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @param s the scalar to multiply the component values of {@code vRHS} with
	 * @return a new {@code Point3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3F add(final Point3F pLHS, final Vector3F vRHS, final float s) {
		return new Point3F(pLHS.x + vRHS.x * s, pLHS.y + vRHS.y * s, pLHS.z + vRHS.z * s);
	}
	
	/**
	 * Adds {@code sRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the addition.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point3F add(final Point3F pLHS, final float sRHS) {
		return new Point3F(pLHS.x + sRHS, pLHS.y + sRHS, pLHS.z + sRHS);
	}
	
	/**
	 * Returns a new {@code Point3F} instance that represents the centroid of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method is equivalent to {@link #midpoint(Point3F, Point3F)}.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @return a new {@code Point3F} instance that represents the centroid of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3F centroid(final Point3F a, final Point3F b) {
		return new Point3F((a.x + b.x) / 2.0F, (a.y + b.y) / 2.0F, (a.z + b.z) / 2.0F);
	}
	
	/**
	 * Returns a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @return a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3F centroid(final Point3F a, final Point3F b, final Point3F c) {
		return new Point3F((a.x + b.x + c.x) / 3.0F, (a.y + b.y + c.y) / 3.0F, (a.z + b.z + c.z) / 3.0F);
	}
	
	/**
	 * Returns a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @param d a {@code Point3F} instance
	 * @return a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3F centroid(final Point3F a, final Point3F b, final Point3F c, final Point3F d) {
		return new Point3F((a.x + b.x + c.x + d.x) / 4.0F, (a.y + b.y + c.y + d.y) / 4.0F, (a.z + b.z + c.z + d.z) / 4.0F);
	}
	
	/**
	 * Returns a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @param d a {@code Point3F} instance
	 * @param e a {@code Point3F} instance
	 * @param f a {@code Point3F} instance
	 * @param g a {@code Point3F} instance
	 * @param h a {@code Point3F} instance
	 * @return a new {@code Point3F} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}
	 */
	public static Point3F centroid(final Point3F a, final Point3F b, final Point3F c, final Point3F d, final Point3F e, final Point3F f, final Point3F g, final Point3F h) {
		return new Point3F((a.x + b.x + c.x + d.x + e.x + f.x + g.x + h.x) / 8.0F, (a.y + b.y + c.y + d.y + e.y + f.y + g.y + h.y) / 8.0F, (a.z + b.z + c.z + d.z + e.z + f.z + g.z + h.z) / 8.0F);
	}
	
	/**
	 * Returns a cached version of {@code p}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3F} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point3F getCached(final Point3F p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point3F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param t the factor
	 * @return a {@code Point3F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3F lerp(final Point3F a, final Point3F b, final float t) {
		return new Point3F(Floats.lerp(a.x, b.x, t), Floats.lerp(a.y, b.y, t), Floats.lerp(a.z, b.z, t));
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the largest component values.
	 * 
	 * @return a new {@code Point3F} instance with the largest component values
	 */
	public static Point3F maximum() {
		return new Point3F(Floats.MAX_VALUE, Floats.MAX_VALUE, Floats.MAX_VALUE);
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the largest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the largest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3F maximum(final Point3F a, final Point3F b) {
		return new Point3F(Floats.max(a.x, b.x), Floats.max(a.y, b.y), Floats.max(a.z, b.z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the largest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the largest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3F maximum(final Point3F a, final Point3F b, final Point3F c) {
		return new Point3F(Floats.max(a.x, b.x, c.x), Floats.max(a.y, b.y, c.y), Floats.max(a.z, b.z, c.z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @param d a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3F maximum(final Point3F a, final Point3F b, final Point3F c, final Point3F d) {
		return new Point3F(Floats.max(a.x, b.x, c.x, d.x), Floats.max(a.y, b.y, c.y, d.y), Floats.max(a.z, b.z, c.z, d.z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance that represents the midpoint of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @return a new {@code Point3F} instance that represents the midpoint of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3F midpoint(final Point3F a, final Point3F b) {
		return new Point3F((a.x + b.x) * 0.5F, (a.y + b.y) * 0.5F, (a.z + b.z) * 0.5F);
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the smallest component values.
	 * 
	 * @return a new {@code Point3F} instance with the smallest component values
	 */
	public static Point3F minimum() {
		return new Point3F(Floats.MIN_VALUE, Floats.MIN_VALUE, Floats.MIN_VALUE);
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the smallest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the smallest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3F minimum(final Point3F a, final Point3F b) {
		return new Point3F(Floats.min(a.x, b.x), Floats.min(a.y, b.y), Floats.min(a.z, b.z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the smallest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the smallest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point3F minimum(final Point3F a, final Point3F b, final Point3F c) {
		return new Point3F(Floats.min(a.x, b.x, c.x), Floats.min(a.y, b.y, c.y), Floats.min(a.z, b.z, c.z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3F} instance
	 * @param b a {@code Point3F} instance
	 * @param c a {@code Point3F} instance
	 * @param d a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3F minimum(final Point3F a, final Point3F b, final Point3F c, final Point3F d) {
		return new Point3F(Floats.min(a.x, b.x, c.x, d.x), Floats.min(a.y, b.y, c.y, d.y), Floats.min(a.z, b.z, c.z, d.z));
	}
	
	/**
	 * Returns a {@code Point3F} offset from {@code point} based on {@code direction}, {@code normal} and {@code pointError}.
	 * <p>
	 * If either {@code point}, {@code direction}, {@code normal} or {@code pointError} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point the {@code Point3F} instance to offset
	 * @param direction a {@link Vector3F} instance denoting a ray direction
	 * @param normal a {@code Vector3F} instance denoting a normal
	 * @param pointError a {@code Vector3F} instance that contains the precision error
	 * @return a {@code Point3F} offset from {@code point} based on {@code direction}, {@code normal} and {@code pointError}
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code direction}, {@code normal} or {@code pointError} are {@code null}
	 */
	public static Point3F offset(final Point3F point, final Vector3F direction, final Vector3F normal, final Vector3F pointError) {
		final float dotProduct = Vector3F.dotProduct(Vector3F.absolute(normal), pointError);
		
		final Vector3F offset = Vector3F.multiply(normal, dotProduct);
		final Vector3F offsetCorrectlyOriented = Vector3F.dotProduct(direction, normal) < 0.0F ? Vector3F.negate(offset) : offset;
		
		final Point3F pointOffset = add(point, offsetCorrectlyOriented);
		
		final float x = offset.x > 0.0F ? Floats.nextUp(pointOffset.x) : offset.x < 0.0F ? Floats.nextDown(pointOffset.x) : pointOffset.x;
		final float y = offset.y > 0.0F ? Floats.nextUp(pointOffset.y) : offset.y < 0.0F ? Floats.nextDown(pointOffset.y) : pointOffset.y;
		final float z = offset.z > 0.0F ? Floats.nextUp(pointOffset.z) : offset.z < 0.0F ? Floats.nextDown(pointOffset.z) : pointOffset.z;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a permuted {@code Point3F} instance.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code x}, {@code y} or {@code z} are less than {@code 0} or greater than or equal to {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param p a {@code Point3F} instance
	 * @param x the index of the X-axis
	 * @param y the index of the Y-axis
	 * @param z the index of the Z-axis
	 * @return a permuted {@code Point3F} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code x}, {@code y} or {@code z} are less than {@code 0} or greater than or equal to {@code 3}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point3F permute(final Point3F p, final int x, final int y, final int z) {
		return new Point3F(p.getComponent(x), p.getComponent(y), p.getComponent(z));
	}
	
	/**
	 * Returns a new {@code Point3F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point3F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point3F read(final DataInput dataInput) {
		try {
			return new Point3F(dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Scales {@code p} using {@code v}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3F} instance
	 * @param v a {@link Vector2F} instance
	 * @return a new {@code Point3F} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
	public static Point3F scale(final Point3F p, final Vector2F v) {
		return new Point3F(p.x * v.x, p.y * v.y, p.z);
	}
	
	/**
	 * Scales {@code p} using {@code v}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3F} instance
	 * @param v a {@link Vector3F} instance
	 * @return a new {@code Point3F} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
	public static Point3F scale(final Point3F p, final Vector3F v) {
		return new Point3F(p.x * v.x, p.y * v.y, p.z * v.z);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param vRHS the {@link Vector3F} instance on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3F subtract(final Point3F pLHS, final Vector3F vRHS) {
		return new Point3F(pLHS.x - vRHS.x, pLHS.y - vRHS.y, pLHS.z - vRHS.z);
	}
	
	/**
	 * Subtracts {@code sRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the subtraction.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point3F subtract(final Point3F pLHS, final float sRHS) {
		return new Point3F(pLHS.x - sRHS, pLHS.y - sRHS, pLHS.z - sRHS);
	}
	
	/**
	 * Transforms the {@code Point3F} {@code pRHS} with the {@link Matrix44F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param pRHS a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point3F transform(final Matrix44F mLHS, final Point3F pRHS) {
		final float x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14;
		final float y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24;
		final float z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34;
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Transforms the {@code Point3F} {@code pRHS} with the {@link Matrix44F} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param pRHS a {@code Point3F} instance
	 * @return a new {@code Point3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point3F transformAndDivide(final Matrix44F mLHS, final Point3F pRHS) {
		final float x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14;
		final float y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24;
		final float z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34;
		final float w = mLHS.element41 * pRHS.x + mLHS.element42 * pRHS.y + mLHS.element43 * pRHS.z + mLHS.element44;
		
		return Floats.equals(w, 1.0F) || Floats.isZero(w) ? new Point3F(x, y, z) : new Point3F(x / w, y / w, z / w);
	}
	
	/**
	 * Returns a {@code String} representation of {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param points a {@code Point3F[]} instance
	 * @return a {@code String} representation of {@code points}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static String toString(final Point3F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		
		final
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("new Point3F[] {");
		
		for(int i = 0; i < points.length; i++) {
			stringBuilder.append(i > 0 ? ", " : "");
			stringBuilder.append(points[i]);
		}
		
		stringBuilder.append("}");
		
		return stringBuilder.toString();
	}
	
	/**
	 * Returns {@code true} if, and only if, all {@code Point3F} instances in {@code points} are coplanar, {@code false} otherwise.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point3F[]} instance
	 * @return {@code true} if, and only if, all {@code Point3F} instances in {@code points} are coplanar, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static boolean coplanar(final Point3F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		final Point3F p0 = points[0];
		final Point3F p1 = points[1];
		final Point3F p2 = points[2];
		
		final Vector3F v0 = Vector3F.directionNormalized(p0, p1);
		final Vector3F v1 = Vector3F.directionNormalized(p0, p2);
		
		for(int i = 3; i < points.length; i++) {
			final Point3F pI = points[i];
			
			final Vector3F v2 = Vector3F.directionNormalized(p0, pI);
			
			if(!Floats.isZero(Vector3F.tripleProduct(v0, v2, v1))) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point3F} instance denoting the eye to look from
	 * @param lookAt a {@code Point3F} instance denoting the target to look at
	 * @return the distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distance(final Point3F eye, final Point3F lookAt) {
		return Vector3F.direction(eye, lookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point3F} instance denoting the eye to look from
	 * @param lookAt a {@code Point3F} instance denoting the target to look at
	 * @return the squared distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distanceSquared(final Point3F eye, final Point3F lookAt) {
		return Vector3F.direction(eye, lookAt).lengthSquared();
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