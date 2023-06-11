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
 * A {@code Point2F} represents a point with two {@code float}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point2F implements Node {
	/**
	 * A {@code Point2F} instance with the largest component values.
	 */
	public static final Point2F MAXIMUM = maximum();
	
	/**
	 * A {@code Point2F} instance with the smallest component values.
	 */
	public static final Point2F MINIMUM = minimum();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Point2F, Point2F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Point2F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Point2F} instance.
	 */
	public final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code 0.0F} and {@code 0.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(0.0F, 0.0F);
	 * }
	 * </pre>
	 */
	public Point2F() {
		this(0.0F, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F((float)(p.x), (float)(p.y));
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point2F(final Point2D p) {
		this((float)(p.x), (float)(p.y));
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code v.x} and {@code v.y}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(v.x, v.y);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point2F(final Vector2F v) {
		this(v.x, v.y);
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value for both components
	 */
	public Point2F(final float component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code x} and {@code y}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	public Point2F(final float x, final float y) {
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point2F(%s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code Point2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point2F)) {
			return false;
		} else if(!Floats.equals(this.x, Point2F.class.cast(object).x)) {
			return false;
		} else if(!Floats.equals(this.y, Point2F.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Point2F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Point2F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y};
	}
	
	/**
	 * Returns a hash code for this {@code Point2F} instance.
	 * 
	 * @return a hash code for this {@code Point2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.x), Float.valueOf(this.y));
	}
	
	/**
	 * Writes this {@code Point2F} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2F} instance on the left-hand side
	 * @param vRHS the {@link Vector2F} instance on the right-hand side
	 * @return a new {@code Point2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2F add(final Point2F pLHS, final Vector2F vRHS) {
		return new Point2F(pLHS.x + vRHS.x, pLHS.y + vRHS.y);
	}
	
	/**
	 * Adds the component values of {@code vRHS} multiplied by {@code s} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2F} instance on the left-hand side
	 * @param vRHS the {@link Vector2F} instance on the right-hand side
	 * @param s the scalar to multiply the component values of {@code vRHS} with
	 * @return a new {@code Point2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2F add(final Point2F pLHS, final Vector2F vRHS, final float s) {
		return new Point2F(pLHS.x + vRHS.x * s, pLHS.y + vRHS.y * s);
	}
	
	/**
	 * Adds {@code sRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the addition.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point2F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point2F add(final Point2F pLHS, final float sRHS) {
		return new Point2F(pLHS.x + sRHS, pLHS.y + sRHS);
	}
	
	/**
	 * Returns a new {@code Point2F} instance that represents the centroid of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method is equivalent to {@link #midpoint(Point2F, Point2F)}.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance that represents the centroid of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F centroid(final Point2F a, final Point2F b) {
		return new Point2F((a.x + b.x) / 2.0F, (a.y + b.y) / 2.0F);
	}
	
	/**
	 * Returns a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @return a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2F centroid(final Point2F a, final Point2F b, final Point2F c) {
		return new Point2F((a.x + b.x + c.x) / 3.0F, (a.y + b.y + c.y) / 3.0F);
	}
	
	/**
	 * Returns a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @return a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2F centroid(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		return new Point2F((a.x + b.x + c.x + d.x) / 4.0F, (a.y + b.y + c.y + d.y) / 4.0F);
	}
	
	/**
	 * Returns a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @param e a {@code Point2F} instance
	 * @param f a {@code Point2F} instance
	 * @param g a {@code Point2F} instance
	 * @param h a {@code Point2F} instance
	 * @return a new {@code Point2F} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}
	 */
	public static Point2F centroid(final Point2F a, final Point2F b, final Point2F c, final Point2F d, final Point2F e, final Point2F f, final Point2F g, final Point2F h) {
		return new Point2F((a.x + b.x + c.x + d.x + e.x + f.x + g.x + h.x) / 8.0F, (a.y + b.y + c.y + d.y + e.y + f.y + g.y + h.y) / 8.0F);
	}
	
	/**
	 * Computes texture coordinates from three other texture coordinates via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Point2F} instance with the interpolated texture coordinates.
	 * <p>
	 * If either {@code pTCA}, {@code pTCB}, {@code pTCC} or {@code pBC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pTCA a {@code Point2F} instance denoting the texture coordinates of vertex {@code A} of a triangle
	 * @param pTCB a {@code Point2F} instance denoting the texture coordinates of vertex {@code B} of a triangle
	 * @param pTCC a {@code Point2F} instance denoting the texture coordinates of vertex {@code C} of a triangle
	 * @param pBC a {@link Point3F} instance denoting the Barycentric coordinates
	 * @return a new {@code Point2F} instance with the interpolated texture coordinates
	 * @throws NullPointerException thrown if, and only if, either {@code pTCA}, {@code pTCB}, {@code pTCC} or {@code pBC} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2F createTextureCoordinates(final Point2F pTCA, final Point2F pTCB, final Point2F pTCC, final Point3F pBC) {
		final float x = pTCA.x * pBC.x + pTCB.x * pBC.y + pTCC.x * pBC.z;
		final float y = pTCA.y * pBC.x + pTCB.y * pBC.y + pTCC.y * pBC.z;
		
		return new Point2F(x, y);
	}
	
	/**
	 * Returns a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
//	TODO: Add Unit Tests!
	public static Point2F decodeMortonCode1By1(final float mortonCode) {
		return decodeMortonCode1By1((int)(mortonCode * (1L << 32L)));
	}
	
	/**
	 * Returns a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2F} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
//	TODO: Add Unit Tests!
	public static Point2F decodeMortonCode1By1(final int mortonCode) {
		final int mortonCodeX = MortonCodes.decode1By1X(mortonCode);
		final int mortonCodeY = MortonCodes.decode1By1Y(mortonCode);
		
		final float x = mortonCodeX / (float)(1 << 16);
		final float y = mortonCodeY / (float)(1 << 16);
		
		return new Point2F(x, y);
	}
	
	/**
	 * Returns a cached version of {@code p}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2F} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point2F getCached(final Point2F p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point2F} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param t the factor
	 * @return a {@code Point2F} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F lerp(final Point2F a, final Point2F b, final float t) {
		return new Point2F(Floats.lerp(a.x, b.x, t), Floats.lerp(a.y, b.y, t));
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values.
	 * 
	 * @return a new {@code Point2F} instance with the largest component values
	 */
	public static Point2F maximum() {
		return new Point2F(Floats.MAX_VALUE, Floats.MAX_VALUE);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b) {
		return new Point2F(Floats.max(a.x, b.x), Floats.max(a.y, b.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b, final Point2F c) {
		return new Point2F(Floats.max(a.x, b.x, c.x), Floats.max(a.y, b.y, c.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2F maximum(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		return new Point2F(Floats.max(a.x, b.x, c.x, d.x), Floats.max(a.y, b.y, c.y, d.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance that represents the midpoint of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance that represents the midpoint of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F midpoint(final Point2F a, final Point2F b) {
		return new Point2F((a.x + b.x) * 0.5F, (a.y + b.y) * 0.5F);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values.
	 * 
	 * @return a new {@code Point2F} instance with the smallest component values
	 */
	public static Point2F minimum() {
		return new Point2F(Floats.MIN_VALUE, Floats.MIN_VALUE);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b) {
		return new Point2F(Floats.min(a.x, b.x), Floats.min(a.y, b.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b, final Point2F c) {
		return new Point2F(Floats.min(a.x, b.x, c.x), Floats.min(a.y, b.y, c.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2F} instance
	 * @param b a {@code Point2F} instance
	 * @param c a {@code Point2F} instance
	 * @param d a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2F minimum(final Point2F a, final Point2F b, final Point2F c, final Point2F d) {
		return new Point2F(Floats.min(a.x, b.x, c.x, d.x), Floats.min(a.y, b.y, c.y, d.y));
	}
	
	/**
	 * Returns a new {@code Point2F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point2F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point2F read(final DataInput dataInput) {
		try {
			return new Point2F(dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Rotates {@code p} using {@code a}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the rotation applied.
	 * <p>
	 * If either {@code p} or {@code a} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2F} instance
	 * @param a an {@link AngleF} instance
	 * @return a new {@code Point2F} instance with the rotation applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code a} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2F rotateCounterclockwise(final Point2F p, final AngleF a) {
		final float cos = Floats.cos(a.getRadians());
		final float sin = Floats.sin(a.getRadians());
		
		return new Point2F(p.x * cos - p.y * sin, p.y * cos + p.x * sin);
	}
	
	/**
	 * Scales {@code p} using {@code v}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2F} instance
	 * @param v a {@link Vector2F} instance
	 * @return a new {@code Point2F} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2F scale(final Point2F p, final Vector2F v) {
		return new Point2F(p.x * v.x, p.y * v.y);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with the spherical coordinates of {@code direction} as its component values.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance
	 * @return a new {@code Point2F} instance with the spherical coordinates of {@code direction} as its component values
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2F sphericalCoordinates(final Vector3F direction) {
		return new Point2F(direction.sphericalPhi() * Floats.PI_MULTIPLIED_BY_2_RECIPROCAL, direction.sphericalTheta() * Floats.PI_RECIPROCAL);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2F} instance on the left-hand side
	 * @param vRHS the {@link Vector2F} instance on the right-hand side
	 * @return a new {@code Point2F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2F subtract(final Point2F pLHS, final Vector2F vRHS) {
		return new Point2F(pLHS.x - vRHS.x, pLHS.y - vRHS.y);
	}
	
	/**
	 * Subtracts {@code sRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the subtraction.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2F} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point2F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point2F subtract(final Point2F pLHS, final float sRHS) {
		return new Point2F(pLHS.x - sRHS, pLHS.y - sRHS);
	}
	
	/**
	 * Returns a new {@code Point2F} instance with {@code p} inside the image represented by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2F} instance
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code Point2F} instance with {@code p} inside the image represented by {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2F toImage(final Point2F p, final float resolutionX, final float resolutionY) {
		return new Point2F(Floats.positiveModulo(p.x * resolutionX - 0.5F, resolutionX), Floats.positiveModulo(p.y * resolutionY - 0.5F, resolutionY));
	}
	
	/**
	 * Transforms the {@code Point2F} {@code pRHS} with the {@link Matrix33F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33F} instance
	 * @param pRHS a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point2F transform(final Matrix33F mLHS, final Point2F pRHS) {
		final float x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13;
		final float y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23;
		
		return new Point2F(x, y);
	}
	
	/**
	 * Transforms the {@code Point2F} {@code pRHS} with the {@link Matrix33F} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point2F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33F} instance
	 * @param pRHS a {@code Point2F} instance
	 * @return a new {@code Point2F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point2F transformAndDivide(final Matrix33F mLHS, final Point2F pRHS) {
		final float x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13;
		final float y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23;
		final float z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33;
		
		return Floats.equals(z, 1.0F) || Floats.isZero(z) ? new Point2F(x, y) : new Point2F(x / z, y / z);
	}
	
	/**
	 * Returns a {@code String} representation of {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param points a {@code Point2F[]} instance
	 * @return a {@code String} representation of {@code points}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static String toString(final Point2F... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		
		final
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("new Point2F[] {");
		
		for(int i = 0; i < points.length; i++) {
			stringBuilder.append(i > 0 ? ", " : "");
			stringBuilder.append(points[i]);
		}
		
		stringBuilder.append("}");
		
		return stringBuilder.toString();
	}
	
	/**
	 * Returns the distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return the distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distance(final Point2F eye, final Point2F lookAt) {
		return Vector2F.direction(eye, lookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point2F} instance denoting the eye to look from
	 * @param lookAt a {@code Point2F} instance denoting the target to look at
	 * @return the squared distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static float distanceSquared(final Point2F eye, final Point2F lookAt) {
		return Vector2F.direction(eye, lookAt).lengthSquared();
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