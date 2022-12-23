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
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;

/**
 * A {@code Point3D} represents a point with three {@code double}-based components.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point3D implements Node {
	/**
	 * A {@code Point3D} instance with the largest component values.
	 */
	public static final Point3D MAXIMUM = maximum();
	
	/**
	 * A {@code Point3D} instance with the smallest component values.
	 */
	public static final Point3D MINIMUM = minimum();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Point3D, Point3D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The X-component of this {@code Point3D} instance.
	 */
	public final double x;
	
	/**
	 * The Y-component of this {@code Point3D} instance.
	 */
	public final double y;
	
	/**
	 * The Z-component of this {@code Point3D} instance.
	 */
	public final double z;
	
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
	 * Constructs a new {@code Point3D} instance given the component values {@code p.x}, {@code p.y} and {@code p.z}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3D(p.x, p.y, p.z);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point3D(final Point4D p) {
		this(p.x, p.y, p.z);
	}
	
	/**
	 * Constructs a new {@code Point3D} instance given the component values {@code v.x}, {@code v.y} and {@code v.z}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point3D(v.x, v.y, v.z);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point3D(final Vector3D v) {
		this(v.x, v.y, v.z);
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
	 * Constructs a new {@code Point3D} instance given the component values {@code x}, {@code y} and {@code z}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Point3D(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point3D(%s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z));
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
		} else if(!Doubles.equals(this.x, Point3D.class.cast(object).x)) {
			return false;
		} else if(!Doubles.equals(this.y, Point3D.class.cast(object).y)) {
			return false;
		} else if(!Doubles.equals(this.z, Point3D.class.cast(object).z)) {
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
	public double sphericalPhi() {
		return Doubles.addLessThan(Doubles.atan2(this.y, this.x), 0.0D, Doubles.PI_MULTIPLIED_BY_2);
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Point3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Point3D} instance
	 */
	public double[] toArray() {
		return new double[] {this.x, this.y, this.z};
	}
	
	/**
	 * Returns a hash code for this {@code Point3D} instance.
	 * 
	 * @return a hash code for this {@code Point3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z));
	}
	
	/**
	 * Writes this {@code Point3D} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code vRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3D} instance on the left-hand side
	 * @param vRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3D add(final Point3D pLHS, final Vector3D vRHS) {
		return new Point3D(pLHS.x + vRHS.x, pLHS.y + vRHS.y, pLHS.z + vRHS.z);
	}
	
	/**
	 * Adds the component values of {@code vRHS} multiplied by {@code s} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3D} instance on the left-hand side
	 * @param vRHS the {@link Vector3D} instance on the right-hand side
	 * @param s the scalar to multiply the component values of {@code vRHS} with
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3D add(final Point3D pLHS, final Vector3D vRHS, final double s) {
		return new Point3D(pLHS.x + vRHS.x * s, pLHS.y + vRHS.y * s, pLHS.z + vRHS.z * s);
	}
	
	/**
	 * Adds {@code sRHS} to the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the addition.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point3D add(final Point3D pLHS, final double sRHS) {
		return new Point3D(pLHS.x + sRHS, pLHS.y + sRHS, pLHS.z + sRHS);
	}
	
	/**
	 * Returns a new {@code Point3D} instance that represents the centroid of {@code a} and {@code b}.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method is equivalent to {@link #midpoint(Point3D, Point3D)}.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @return a new {@code Point3D} instance that represents the centroid of {@code a} and {@code b}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3D centroid(final Point3D a, final Point3D b) {
		return new Point3D((a.x + b.x) / 2.0D, (a.y + b.y) / 2.0D, (a.z + b.z) / 2.0D);
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
		return new Point3D((a.x + b.x + c.x) / 3.0D, (a.y + b.y + c.y) / 3.0D, (a.z + b.z + c.z) / 3.0D);
	}
	
	/**
	 * Returns a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @param d a {@code Point3D} instance
	 * @return a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3D centroid(final Point3D a, final Point3D b, final Point3D c, final Point3D d) {
		return new Point3D((a.x + b.x + c.x + d.x) / 4.0D, (a.y + b.y + c.y + d.y) / 4.0D, (a.z + b.z + c.z + d.z) / 4.0D);
	}
	
	/**
	 * Returns a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @param d a {@code Point3D} instance
	 * @param e a {@code Point3D} instance
	 * @param f a {@code Point3D} instance
	 * @param g a {@code Point3D} instance
	 * @param h a {@code Point3D} instance
	 * @return a new {@code Point3D} instance that represents the centroid of {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} and {@code h}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c}, {@code d}, {@code e}, {@code f}, {@code g} or {@code h} are {@code null}
	 */
	public static Point3D centroid(final Point3D a, final Point3D b, final Point3D c, final Point3D d, final Point3D e, final Point3D f, final Point3D g, final Point3D h) {
		return new Point3D((a.x + b.x + c.x + d.x + e.x + f.x + g.x + h.x) / 8.0D, (a.y + b.y + c.y + d.y + e.y + f.y + g.y + h.y) / 8.0D, (a.z + b.z + c.z + d.z + e.z + f.z + g.z + h.z) / 8.0D);
	}
	
	/**
	 * Returns a cached version of {@code p}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3D} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point3D getCached(final Point3D p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
	}
	
	/**
	 * Performs a linear interpolation operation on the supplied values.
	 * <p>
	 * Returns a {@code Point3D} instance with the result of the linear interpolation operation.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param t the factor
	 * @return a {@code Point3D} instance with the result of the linear interpolation operation
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Point3D lerp(final Point3D a, final Point3D b, final double t) {
		return new Point3D(Doubles.lerp(a.x, b.x, t), Doubles.lerp(a.y, b.y, t), Doubles.lerp(a.z, b.z, t));
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the largest component values.
	 * 
	 * @return a new {@code Point3D} instance with the largest component values
	 */
	public static Point3D maximum() {
		return new Point3D(Doubles.MAX_VALUE, Doubles.MAX_VALUE, Doubles.MAX_VALUE);
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
		return new Point3D(Doubles.max(a.x, b.x), Doubles.max(a.y, b.y), Doubles.max(a.z, b.z));
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
		return new Point3D(Doubles.max(a.x, b.x, c.x), Doubles.max(a.y, b.y, c.y), Doubles.max(a.z, b.z, c.z));
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @param d a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the largest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3D maximum(final Point3D a, final Point3D b, final Point3D c, final Point3D d) {
		return new Point3D(Doubles.max(a.x, b.x, c.x, d.x), Doubles.max(a.y, b.y, c.y, d.y), Doubles.max(a.z, b.z, c.z, d.z));
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
		return new Point3D((a.x + b.x) * 0.5D, (a.y + b.y) * 0.5D, (a.z + b.z) * 0.5D);
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the smallest component values.
	 * 
	 * @return a new {@code Point3D} instance with the smallest component values
	 */
	public static Point3D minimum() {
		return new Point3D(Doubles.MIN_VALUE, Doubles.MIN_VALUE, Doubles.MIN_VALUE);
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
		return new Point3D(Doubles.min(a.x, b.x), Doubles.min(a.y, b.y), Doubles.min(a.z, b.z));
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
		return new Point3D(Doubles.min(a.x, b.x, c.x), Doubles.min(a.y, b.y, c.y), Doubles.min(a.z, b.z, c.z));
	}
	
	/**
	 * Returns a new {@code Point3D} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}.
	 * <p>
	 * If either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Point3D} instance
	 * @param b a {@code Point3D} instance
	 * @param c a {@code Point3D} instance
	 * @param d a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the smallest component values of {@code a}, {@code b}, {@code c} and {@code d}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code c} or {@code d} are {@code null}
	 */
	public static Point3D minimum(final Point3D a, final Point3D b, final Point3D c, final Point3D d) {
		return new Point3D(Doubles.min(a.x, b.x, c.x, d.x), Doubles.min(a.y, b.y, c.y, d.y), Doubles.min(a.z, b.z, c.z, d.z));
	}
	
	/**
	 * Returns a {@code Point3D} offset from {@code point} based on {@code direction}, {@code normal} and {@code pointError}.
	 * <p>
	 * If either {@code point}, {@code direction}, {@code normal} or {@code pointError} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point the {@code Point3D} instance to offset
	 * @param direction a {@link Vector3D} instance denoting a ray direction
	 * @param normal a {@code Vector3D} instance denoting a normal
	 * @param pointError a {@code Vector3D} instance that contains the precision error
	 * @return a {@code Point3D} offset from {@code point} based on {@code direction}, {@code normal} and {@code pointError}
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code direction}, {@code normal} or {@code pointError} are {@code null}
	 */
	public static Point3D offset(final Point3D point, final Vector3D direction, final Vector3D normal, final Vector3D pointError) {
		final double dotProduct = Vector3D.dotProduct(Vector3D.absolute(normal), pointError);
		
		final Vector3D offset = Vector3D.multiply(normal, dotProduct);
		final Vector3D offsetCorrectlyOriented = Vector3D.dotProduct(direction, normal) < 0.0D ? Vector3D.negate(offset) : offset;
		
		final Point3D pointOffset = add(point, offsetCorrectlyOriented);
		
		final double x = offset.x > 0.0D ? Doubles.nextUp(pointOffset.x) : offset.x < 0.0D ? Doubles.nextDown(pointOffset.x) : pointOffset.x;
		final double y = offset.y > 0.0D ? Doubles.nextUp(pointOffset.y) : offset.y < 0.0D ? Doubles.nextDown(pointOffset.y) : pointOffset.y;
		final double z = offset.z > 0.0D ? Doubles.nextUp(pointOffset.z) : offset.z < 0.0D ? Doubles.nextDown(pointOffset.z) : pointOffset.z;
		
		return new Point3D(x, y, z);
	}
	
	/**
	 * Returns a new {@code Point3D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Point3D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Point3D read(final DataInput dataInput) {
		try {
			return new Point3D(dataInput.readDouble(), dataInput.readDouble(), dataInput.readDouble());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Scales {@code p} using {@code scale}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3D} instance
	 * @param v a {@link Vector2D} instance
	 * @return a new {@code Point3D} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
	public static Point3D scale(final Point3D p, final Vector2D v) {
		return new Point3D(p.x * v.x, p.y * v.y, p.z);
	}
	
	/**
	 * Scales {@code p} using {@code v}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the scale applied.
	 * <p>
	 * If either {@code p} or {@code v} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param p a {@code Point3D} instance
	 * @param v a {@link Vector3D} instance
	 * @return a new {@code Point3D} instance with the scale applied
	 * @throws NullPointerException thrown if, and only if, either {@code p} or {@code v} are {@code null}
	 */
	public static Point3D scale(final Point3D p, final Vector3D v) {
		return new Point3D(p.x * v.x, p.y * v.y, p.z * v.z);
	}
	
	/**
	 * Subtracts the component values of {@code vRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code pLHS} or {@code vRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3D} instance on the left-hand side
	 * @param vRHS the {@link Vector3D} instance on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code pLHS} or {@code vRHS} are {@code null}
	 */
	public static Point3D subtract(final Point3D pLHS, final Vector3D vRHS) {
		return new Point3D(pLHS.x - vRHS.x, pLHS.y - vRHS.y, pLHS.z - vRHS.z);
	}
	
	/**
	 * Subtracts {@code sRHS} from the component values of {@code pLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the subtraction.
	 * <p>
	 * If {@code pLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pLHS the {@code Point3D} instance on the left-hand side
	 * @param sRHS the scalar value on the right-hand side
	 * @return a new {@code Point3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code pLHS} is {@code null}
	 */
	public static Point3D subtract(final Point3D pLHS, final double sRHS) {
		return new Point3D(pLHS.x - sRHS, pLHS.y - sRHS, pLHS.z - sRHS);
	}
	
	/**
	 * Transforms the {@code Point3D} {@code pRHS} with the {@link Matrix44D} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param pRHS a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point3D transform(final Matrix44D mLHS, final Point3D pRHS) {
		final double x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14;
		final double y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24;
		final double z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34;
		
		return new Point3D(x, y, z);
	}
	
	/**
	 * Transforms the {@code Point3D} {@code pRHS} with the {@link Matrix44D} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44D} instance
	 * @param pRHS a {@code Point3D} instance
	 * @return a new {@code Point3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point3D transformAndDivide(final Matrix44D mLHS, final Point3D pRHS) {
		final double x = mLHS.element11 * pRHS.x + mLHS.element12 * pRHS.y + mLHS.element13 * pRHS.z + mLHS.element14;
		final double y = mLHS.element21 * pRHS.x + mLHS.element22 * pRHS.y + mLHS.element23 * pRHS.z + mLHS.element24;
		final double z = mLHS.element31 * pRHS.x + mLHS.element32 * pRHS.y + mLHS.element33 * pRHS.z + mLHS.element34;
		final double w = mLHS.element41 * pRHS.x + mLHS.element42 * pRHS.y + mLHS.element43 * pRHS.z + mLHS.element44;
		
		return Doubles.equals(w, 1.0D) || Doubles.isZero(w) ? new Point3D(x, y, z) : new Point3D(x / w, y / w, z / w);
	}
	
	/**
	 * Returns a {@code String} representation of {@code points}.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param points a {@code Point3D[]} instance
	 * @return a {@code String} representation of {@code points}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static String toString(final Point3D... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		
		final
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("new Point3D[] {");
		
		for(int i = 0; i < points.length; i++) {
			stringBuilder.append(i > 0 ? ", " : "");
			stringBuilder.append(points[i]);
		}
		
		stringBuilder.append("}");
		
		return stringBuilder.toString();
	}
	
	/**
	 * Returns {@code true} if, and only if, all {@code Point3D} instances in {@code points} are coplanar, {@code false} otherwise.
	 * <p>
	 * If either {@code points} or an element in {@code points} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code points.length} is less than {@code 3}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param points a {@code Point3D[]} instance
	 * @return {@code true} if, and only if, all {@code Point3D} instances in {@code points} are coplanar, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code points.length} is less than {@code 3}
	 * @throws NullPointerException thrown if, and only if, either {@code points} or an element in {@code points} are {@code null}
	 */
	public static boolean coplanar(final Point3D... points) {
		ParameterArguments.requireNonNullArray(points, "points");
		ParameterArguments.requireRange(points.length, 3, Integer.MAX_VALUE, "points.length");
		
		final Point3D p0 = points[0];
		final Point3D p1 = points[1];
		final Point3D p2 = points[2];
		
		final Vector3D v0 = Vector3D.directionNormalized(p0, p1);
		final Vector3D v1 = Vector3D.directionNormalized(p0, p2);
		
		for(int i = 3; i < points.length; i++) {
			final Point3D pI = points[i];
			
			final Vector3D v2 = Vector3D.directionNormalized(p0, pI);
			
			if(!Doubles.isZero(Vector3D.tripleProduct(v0, v2, v1))) {
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