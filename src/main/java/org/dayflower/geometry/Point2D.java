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

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.MIN_VALUE;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.positiveModulo;
import static org.dayflower.utility.Doubles.sin;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.utility.Doubles;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Strings;

/**
 * A {@code Point2D} represents a point with two {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point2D implements Node {
	/**
	 * A {@code Point2D} instance with the largest component values.
	 */
	public static final Point2D MAXIMUM = maximum();
	
	/**
	 * A {@code Point2D} instance with the smallest component values.
	 */
	public static final Point2D MINIMUM = minimum();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Point2D, Point2D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Point2D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Point2D} instance.
	 */
	public final double y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Point2D} instance given the component values {@code 0.0D} and {@code 0.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2D(0.0D, 0.0D);
	 * }
	 * </pre>
	 */
	public Point2D() {
		this(0.0D, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Point2D} instance given the component values {@code p.x} and {@code p.y}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2D(p.x, p.y);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point2D(final Point2F p) {
		this(p.x, p.y);
	}
	
	/**
	 * Constructs a new {@code Point2D} instance given the component values {@code v.x} and {@code v.y}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2D(v.x, v.y);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point2D(final Vector2D v) {
		this(v.x, v.y);
	}
	
	/**
	 * Constructs a new {@code Point2D} instance given the component values {@code component} and {@code component}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2D(component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value for both components
	 */
	public Point2D(final double component) {
		this(component, component);
	}
	
	/**
	 * Constructs a new {@code Point2D} instance given the component values {@code x} and {@code y}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 */
	public Point2D(final double x, final double y) {
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point2D(%s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code Point2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Point2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Point2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Point2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Point2D)) {
			return false;
		} else if(!equal(this.x, Point2D.class.cast(object).x)) {
			return false;
		} else if(!equal(this.y, Point2D.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Point2D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Point2D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y};
	}
	
	/**
	 * Returns a hash code for this {@code Point2D} instance.
	 * 
	 * @return a hash code for this {@code Point2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y));
	}
	
	/**
	 * Writes this {@code Point2D} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2D} instance on the left-hand side
	 * @param vRHS the {@link Vector2D} instance on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2D add(final Point2D pLHS, final Vector2D vRHS) {
		return new Point2D(pLHS.x + vRHS.x, pLHS.y + vRHS.y);
	}
	
	/**
	 * Adds the component values of {@code vRHS} multiplied by {@code s} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2D} instance on the left-hand side
	 * @param vRHS the {@link Vector2D} instance on the right-hand side
	 * @param s the scalar to multiply the component values of {@code vRHS} with
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2D add(final Point2D pLHS, final Vector2D vRHS, final double s) {
		return new Point2D(pLHS.x + vRHS.x * s, pLHS.y + vRHS.y * s);
	}
	
	/**
	 * Adds {@code sRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point2D add(final Point2D pLHS, final double sRHS) {
		return new Point2D(pLHS.x + sRHS, pLHS.y + sRHS);
	}
	
	/**
	 * Returns a new {@code Point2D} instance that represents the centroid of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method is equivalent to {@link #midpoint(Point2D, Point2D)}.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @return a new {@code Point2D} instance that represents the centroid of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2D centroid(final Point2D a, final Point2D b) {
		return new Point2D((a.x + b.x) / 2.0D, (a.y + b.y) / 2.0D);
	}
	
	/**
	 * Returns a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @return a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2D centroid(final Point2D a, final Point2D b, final Point2D c) {
		return new Point2D((a.x + b.x + c.x) / 3.0D, (a.y + b.y + c.y) / 3.0D);
	}
	
	/**
	 * Returns a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @param d a {@code Point2D} instance
	 * @return a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2D centroid(final Point2D a, final Point2D b, final Point2D c, final Point2D d) {
		return new Point2D((a.x + b.x + c.x + d.x) / 4.0D, (a.y + b.y + c.y + d.y) / 4.0D);
	}
	
	/**
	 * Returns a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @param d a {@code Point2D} instance
	 * @param e a {@code Point2D} instance
	 * @param f a {@code Point2D} instance
	 * @param g a {@code Point2D} instance
	 * @param h a {@code Point2D} instance
	 * @return a new {@code Point2D} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}
	 */
	public static Point2D centroid(final Point2D a, final Point2D b, final Point2D c, final Point2D d, final Point2D e, final Point2D f, final Point2D g, final Point2D h) {
		return new Point2D((a.x + b.x + c.x + d.x + e.x + f.x + g.x + h.x) / 8.0D, (a.y + b.y + c.y + d.y + e.y + f.y + g.y + h.y) / 8.0D);
	}
	
	/**
	 * Computes texture coordinates from three other texture coordinates via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Point2D} instance with the interpolated texture coordinates.
	 * <p>
	 * If either {@code pTCA}, {@code pTCB}, {@code pTCC} or {@code pBC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pTCA a {@code Point2D} instance denoting the texture coordinates of vertex {@code A} of a triangle
	 * @param pTCB a {@code Point2D} instance denoting the texture coordinates of vertex {@code B} of a triangle
	 * @param pTCC a {@code Point2D} instance denoting the texture coordinates of vertex {@code C} of a triangle
	 * @param pBC a {@link Point3D} instance denoting the Barycentric coordinates
	 * @return a new {@code Point2D} instance with the interpolated texture coordinates
	 * @throws NullPointerException thrown if, and only if, either {@code pTCA}, {@code pTCB}, {@code pTCC} or {@code pBC} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D createTextureCoordinates(final Point2D pTCA, final Point2D pTCB, final Point2D pTCC, final Point3D pBC) {
		final double x = pTCA.x * pBC.getU() + pTCB.x * pBC.getV() + pTCC.x * pBC.getW();
		final double y = pTCA.y * pBC.getU() + pTCB.y * pBC.getV() + pTCC.y * pBC.getW();
		
		return new Point2D(x, y);
	}
	
	/**
	 * Returns a new {@code Point2D} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2D} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
//	TODO: Add Unit Tests!
	public static Point2D decodeMortonCode1By1(final double mortonCode) {
		return decodeMortonCode1By1((int)(mortonCode * (1L << 32L)));
	}
	
	/**
	 * Returns a new {@code Point2D} instance based on the decoded coordinates of the Morton code {@code mortonCode}.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return a new {@code Point2D} instance based on the decoded coordinates of the Morton code {@code mortonCode}
	 */
//	TODO: Add Unit Tests!
	public static Point2D decodeMortonCode1By1(final int mortonCode) {
		final int mortonCodeX = MortonCodes.decode1By1X(mortonCode);
		final int mortonCodeY = MortonCodes.decode1By1Y(mortonCode);
		
		final double x = mortonCodeX / (double)(1 << 16);
		final double y = mortonCodeY / (double)(1 << 16);
		
		return new Point2D(x, y);
	}
	
	/**
	 * Returns a cached version of {@code p}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2D} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point2D getCached(final Point2D p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point2D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param t the factor
	 * @return a {@code Point2D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2D lerp(final Point2D a, final Point2D b, final double t) {
		return new Point2D(Doubles.lerp(a.x, b.x, t), Doubles.lerp(a.y, b.y, t));
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the largest component values.
	 * 
	 * @return a new {@code Point2D} instance with the largest component values
	 */
	public static Point2D maximum() {
		return new Point2D(MAX_VALUE, MAX_VALUE);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the largest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the largest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2D maximum(final Point2D a, final Point2D b) {
		return new Point2D(max(a.x, b.x), max(a.y, b.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the largest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the largest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2D maximum(final Point2D a, final Point2D b, final Point2D c) {
		return new Point2D(max(a.x, b.x, c.x), max(a.y, b.y, c.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @param d a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2D maximum(final Point2D a, final Point2D b, final Point2D c, final Point2D d) {
		return new Point2D(max(a.x, b.x, c.x, d.x), max(a.y, b.y, c.y, d.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance that represents the midpoint of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @return a new {@code Point2D} instance that represents the midpoint of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2D midpoint(final Point2D a, final Point2D b) {
		return new Point2D((a.x + b.x) * 0.5D, (a.y + b.y) * 0.5D);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the smallest component values.
	 * 
	 * @return a new {@code Point2D} instance with the smallest component values
	 */
	public static Point2D minimum() {
		return new Point2D(MIN_VALUE, MIN_VALUE);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the smallest component values of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the smallest component values of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point2D minimum(final Point2D a, final Point2D b) {
		return new Point2D(min(a.x, b.x), min(a.y, b.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the smallest component values of {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the smallest component values of {@code a}, {@code b} and {@code c}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Point2D minimum(final Point2D a, final Point2D b, final Point2D c) {
		return new Point2D(min(a.x, b.x, c.x), min(a.y, b.y, c.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point2D} instance
	 * @param b a {@code Point2D} instance
	 * @param c a {@code Point2D} instance
	 * @param d a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point2D minimum(final Point2D a, final Point2D b, final Point2D c, final Point2D d) {
		return new Point2D(min(a.x, b.x, c.x, d.x), min(a.y, b.y, c.y, d.y));
	}
	
	/**
	 * Returns a new {@code Point2D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point2D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point2D read(final DataInput dataInput) {
		try {
			return new Point2D(dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Rotates {@code p} using {@code a}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the rotation applied.
	 * <p>
	 * If either {@code p} or {@code a} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2D} instance
	 * @param a an {@link AngleD} instance
	 * @return a new {@code Point2D} instance with the rotation applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code a} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D rotateCounterclockwise(final Point2D p, final AngleD a) {
		final double cos = cos(a.getRadians());
		final double sin = sin(a.getRadians());
		
		return new Point2D(p.x * cos - p.y * sin, p.y * cos + p.x * sin);
	}
	
	/**
	 * Scales {@code p} using {@code v}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2D} instance
	 * @param v a {@link Vector2D} instance
	 * @return a new {@code Point2D} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D scale(final Point2D p, final Vector2D v) {
		return new Point2D(p.x * v.x, p.y * v.y);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the spherical coordinates of {@code direction} as its component values.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3D} instance
	 * @return a new {@code Point2D} instance with the spherical coordinates of {@code direction} as its component values
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D sphericalCoordinates(final Vector3D direction) {
		return new Point2D(direction.sphericalPhi() * PI_MULTIPLIED_BY_2_RECIPROCAL, direction.sphericalTheta() * PI_RECIPROCAL);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2D} instance on the left-hand side
	 * @param vRHS the {@link Vector2D} instance on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point2D subtract(final Point2D pLHS, final Vector2D vRHS) {
		return new Point2D(pLHS.x - vRHS.x, pLHS.y - vRHS.y);
	}
	
	/**
	 * Subtracts {@code sRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the subtraction.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point2D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point2D subtract(final Point2D pLHS, final double sRHS) {
		return new Point2D(pLHS.x - sRHS, pLHS.y - sRHS);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with {@code p} inside the image represented by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point2D} instance
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code Point2D} instance with {@code p} inside the image represented by {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D toImage(final Point2D p, final double resolutionX, final double resolutionY) {
		return new Point2D(positiveModulo(p.x * resolutionX - 0.5D, resolutionX), positiveModulo(p.y * resolutionY - 0.5D, resolutionY));
	}
	
	/**
	 * Transforms the {@code Point2D} {@code pRHS} with the {@link Matrix33D} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33D} instance
	 * @param pRHS a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point2D transform(final Matrix33D mLHS, final Point2D pRHS) {
		final double x = mLHS.getElement11() * pRHS.x + mLHS.getElement12() * pRHS.y + mLHS.getElement13();
		final double y = mLHS.getElement21() * pRHS.x + mLHS.getElement22() * pRHS.y + mLHS.getElement23();
		
		return new Point2D(x, y);
	}
	
	/**
	 * Transforms the {@code Point2D} {@code pRHS} with the {@link Matrix33D} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix33D} instance
	 * @param pRHS a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point2D transformAndDivide(final Matrix33D mLHS, final Point2D pRHS) {
		final double x = mLHS.getElement11() * pRHS.x + mLHS.getElement12() * pRHS.y + mLHS.getElement13();
		final double y = mLHS.getElement21() * pRHS.x + mLHS.getElement22() * pRHS.y + mLHS.getElement23();
		final double z = mLHS.getElement31() * pRHS.x + mLHS.getElement32() * pRHS.y + mLHS.getElement33();
		
		return equal(z, 1.0D) || isZero(z) ? new Point2D(x, y) : new Point2D(x / z, y / z);
	}
	
	/**
	 * Returns a {@code String} representation of {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param points a {@code Point2D[]} instance
	 * @return a {@code String} representation of {@code points}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static String toString(final Point2D... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		
		final
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("new Point2D[] {");
		
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
	 * @param eye a {@code Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return the distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static double distance(final Point2D eye, final Point2D lookAt) {
		return Vector2D.direction(eye, lookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code eye} to {@code lookAt}.
	 * <p>
	 * If either {@code eye} or {@code lookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param eye a {@code Point2D} instance denoting the eye to look from
	 * @param lookAt a {@code Point2D} instance denoting the target to look at
	 * @return the squared distance from {@code eye} to {@code lookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code eye} or {@code lookAt} are {@code null}
	 */
	public static double distanceSquared(final Point2D eye, final Point2D lookAt) {
		return Vector2D.direction(eye, lookAt).lengthSquared();
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