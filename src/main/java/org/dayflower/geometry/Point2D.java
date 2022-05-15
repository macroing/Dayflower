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
	 * Constructs a new {@code Point2D} instance given the component values {@code v.getComponent1()} and {@code v.getComponent2()}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2D(v.getComponent1(), v.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector2D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point2D(final Vector2D v) {
		this(v.getComponent1(), v.getComponent2());
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
	 * Adds the component values of {@code vectorRHS} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector2D} instance on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point2D add(final Point2D pointLHS, final Vector2D vectorRHS) {
		final double component1 = pointLHS.x + vectorRHS.getComponent1();
		final double component2 = pointLHS.y + vectorRHS.getComponent2();
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Adds the component values of {@code vectorRHS} multiplied by {@code scalar} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector2D} instance on the right-hand side
	 * @param scalar the scalar to multiply the component values of {@code vectorRHS} with
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point2D add(final Point2D pointLHS, final Vector2D vectorRHS, final double scalar) {
		final double component1 = pointLHS.x + vectorRHS.getComponent1() * scalar;
		final double component2 = pointLHS.y + vectorRHS.getComponent2() * scalar;
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Adds {@code scalarRHS} to the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the addition.
	 * <p>
	 * If {@code pointLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pointLHS} is {@code null}
	 */
	public static Point2D add(final Point2D pointLHS, final double scalarRHS) {
		final double component1 = pointLHS.x + scalarRHS;
		final double component2 = pointLHS.y + scalarRHS;
		
		return new Point2D(component1, component2);
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
		final double component1 = (a.x + b.x) / 2.0D;
		final double component2 = (a.y + b.y) / 2.0D;
		
		return new Point2D(component1, component2);
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
		final double component1 = (a.x + b.x + c.x) / 3.0D;
		final double component2 = (a.y + b.y + c.y) / 3.0D;
		
		return new Point2D(component1, component2);
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
		final double component1 = (a.x + b.x + c.x + d.x) / 4.0D;
		final double component2 = (a.y + b.y + c.y + d.y) / 4.0D;
		
		return new Point2D(component1, component2);
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
		final double component1 = (a.x + b.x + c.x + d.x + e.x + f.x + g.x + h.x) / 8.0D;
		final double component2 = (a.y + b.y + c.y + d.y + e.y + f.y + g.y + h.y) / 8.0D;
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Computes texture coordinates from three other texture coordinates via Barycentric interpolation.
	 * <p>
	 * Returns a new {@code Point2D} instance with the interpolated texture coordinates.
	 * <p>
	 * If either {@code textureCoordinatesA}, {@code textureCoordinatesB}, {@code textureCoordinatesC} or {@code barycentricCoordinates} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCoordinatesA a {@code Point2D} instance denoting the texture coordinates of vertex {@code A} of a triangle
	 * @param textureCoordinatesB a {@code Point2D} instance denoting the texture coordinates of vertex {@code B} of a triangle
	 * @param textureCoordinatesC a {@code Point2D} instance denoting the texture coordinates of vertex {@code C} of a triangle
	 * @param barycentricCoordinates a {@link Point3D} instance denoting the Barycentric coordinates
	 * @return a new {@code Point2D} instance with the interpolated texture coordinates
	 * @throws NullPointerException thrown if, and only if, either {@code textureCoordinatesA}, {@code textureCoordinatesB}, {@code textureCoordinatesC} or {@code barycentricCoordinates} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D createTextureCoordinates(final Point2D textureCoordinatesA, final Point2D textureCoordinatesB, final Point2D textureCoordinatesC, final Point3D barycentricCoordinates) {
		final double u = textureCoordinatesA.x * barycentricCoordinates.getU() + textureCoordinatesB.x * barycentricCoordinates.getV() + textureCoordinatesC.x * barycentricCoordinates.getW();
		final double v = textureCoordinatesA.y * barycentricCoordinates.getU() + textureCoordinatesB.y * barycentricCoordinates.getV() + textureCoordinatesC.y * barycentricCoordinates.getW();
		
		return new Point2D(u, v);
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
	 * Returns a cached version of {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2D} instance
	 * @return a cached version of {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public static Point2D getCached(final Point2D point) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(point, "point == null"), key -> point);
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
		final double component1 = Doubles.lerp(a.x, b.x, t);
		final double component2 = Doubles.lerp(a.y, b.y, t);
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the largest component values.
	 * 
	 * @return a new {@code Point2D} instance with the largest component values
	 */
	public static Point2D maximum() {
		final double component1 = MAX_VALUE;
		final double component2 = MAX_VALUE;
		
		return new Point2D(component1, component2);
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
		final double component1 = max(a.x, b.x);
		final double component2 = max(a.y, b.y);
		
		return new Point2D(component1, component2);
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
		final double component1 = max(a.x, b.x, c.x);
		final double component2 = max(a.y, b.y, c.y);
		
		return new Point2D(component1, component2);
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
		final double component1 = max(a.x, b.x, c.x, d.x);
		final double component2 = max(a.y, b.y, c.y, d.y);
		
		return new Point2D(component1, component2);
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
		final double component1 = (a.x + b.x) * 0.5D;
		final double component2 = (a.y + b.y) * 0.5D;
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with the smallest component values.
	 * 
	 * @return a new {@code Point2D} instance with the smallest component values
	 */
	public static Point2D minimum() {
		final double component1 = MIN_VALUE;
		final double component2 = MIN_VALUE;
		
		return new Point2D(component1, component2);
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
		final double component1 = min(a.x, b.x);
		final double component2 = min(a.y, b.y);
		
		return new Point2D(component1, component2);
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
		final double component1 = min(a.x, b.x, c.x);
		final double component2 = min(a.y, b.y, c.y);
		
		return new Point2D(component1, component2);
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
		final double component1 = min(a.x, b.x, c.x, d.x);
		final double component2 = min(a.y, b.y, c.y, d.y);
		
		return new Point2D(component1, component2);
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
			final double component1 = dataInput.readDouble();
			final double component2 = dataInput.readDouble();
			
			return new Point2D(component1, component2);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Rotates {@code point} using {@code angle}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the rotation applied.
	 * <p>
	 * If either {@code point} or {@code angle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2D} instance
	 * @param angle an {@link AngleD} instance
	 * @return a new {@code Point2D} instance with the rotation applied
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code angle} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D rotateCounterclockwise(final Point2D point, final AngleD angle) {
		final double angleRadians = angle.getRadians();
		final double angleRadiansCos = cos(angleRadians);
		final double angleRadiansSin = sin(angleRadians);
		
		final double component1 = point.x * angleRadiansCos - point.y * angleRadiansSin;
		final double component2 = point.y * angleRadiansCos + point.x * angleRadiansSin;
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Scales {@code point} using {@code scale}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the scale applied.
	 * <p>
	 * If either {@code point} or {@code scale} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2D} instance
	 * @param scale a {@link Vector2D} instance
	 * @return a new {@code Point2D} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code scale} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D scale(final Point2D point, final Vector2D scale) {
		final double component1 = point.x * scale.getComponent1();
		final double component2 = point.y * scale.getComponent2();
		
		return new Point2D(component1, component2);
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
	 * Subtracts the component values of {@code vectorRHS} from the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pointLHS} or {@code vectorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2D} instance on the left-hand side
	 * @param vectorRHS the {@link Vector2D} instance on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pointLHS} or {@code vectorRHS} are {@code null}
	 */
	public static Point2D subtract(final Point2D pointLHS, final Vector2D vectorRHS) {
		final double component1 = pointLHS.x - vectorRHS.getComponent1();
		final double component2 = pointLHS.y - vectorRHS.getComponent2();
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Subtracts {@code scalarRHS} from the component values of {@code pointLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the subtraction.
	 * <p>
	 * If {@code pointLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLHS the {@code Point2D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Point2D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pointLHS} is {@code null}
	 */
	public static Point2D subtract(final Point2D pointLHS, final double scalarRHS) {
		final double component1 = pointLHS.x - scalarRHS;
		final double component2 = pointLHS.y - scalarRHS;
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Returns a new {@code Point2D} instance with {@code point} inside the image represented by {@code resolutionX} and {@code resolutionY}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point2D} instance
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code Point2D} instance with {@code point} inside the image represented by {@code resolutionX} and {@code resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Point2D toImage(final Point2D point, final double resolutionX, final double resolutionY) {
		final double component1 = positiveModulo(point.x * resolutionX - 0.5D, resolutionX);
		final double component2 = positiveModulo(point.y * resolutionY - 0.5D, resolutionY);
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Transforms the {@code Point2D} {@code pointRHS} with the {@link Matrix33D} {@code matrixLHS}.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33D} instance
	 * @param pointRHS a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point2D transform(final Matrix33D matrixLHS, final Point2D pointRHS) {
		final double component1 = matrixLHS.getElement11() * pointRHS.x + matrixLHS.getElement12() * pointRHS.y + matrixLHS.getElement13();
		final double component2 = matrixLHS.getElement21() * pointRHS.x + matrixLHS.getElement22() * pointRHS.y + matrixLHS.getElement23();
		
		return new Point2D(component1, component2);
	}
	
	/**
	 * Transforms the {@code Point2D} {@code pointRHS} with the {@link Matrix33D} {@code matrixLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point2D} instance with the result of the transformation.
	 * <p>
	 * If either {@code matrixLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrixLHS a {@code Matrix33D} instance
	 * @param pointRHS a {@code Point2D} instance
	 * @return a new {@code Point2D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code matrixLHS} or {@code pointRHS} are {@code null}
	 */
	public static Point2D transformAndDivide(final Matrix33D matrixLHS, final Point2D pointRHS) {
		final double component1 = matrixLHS.getElement11() * pointRHS.x + matrixLHS.getElement12() * pointRHS.y + matrixLHS.getElement13();
		final double component2 = matrixLHS.getElement21() * pointRHS.x + matrixLHS.getElement22() * pointRHS.y + matrixLHS.getElement23();
		final double component3 = matrixLHS.getElement31() * pointRHS.x + matrixLHS.getElement32() * pointRHS.y + matrixLHS.getElement33();
		
		return equal(component3, 1.0D) || isZero(component3) ? new Point2D(component1, component2) : new Point2D(component1 / component3, component2 / component3);
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