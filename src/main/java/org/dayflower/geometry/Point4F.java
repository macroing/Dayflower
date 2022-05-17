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

import org.macroing.java.lang.Strings;

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
	
	/**
	 * The W-component of this {@code Point4F} instance.
	 */
	public final float w;
	
	/**
	 * The X-component of this {@code Point4F} instance.
	 */
	public final float x;
	
	/**
	 * The Y-component of this {@code Point4F} instance.
	 */
	public final float y;
	
	/**
	 * The Z-component of this {@code Point4F} instance.
	 */
	public final float z;
	
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
	 * Constructs a new {@code Point4F} instance given the component values {@code p.x}, {@code p.y}, {@code p.z} and {@code 1.0F}.
	 * <p>
	 * If {@code p} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(p.x, p.y, p.z, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param p a {@link Point3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public Point4F(final Point3F p) {
		this(p.x, p.y, p.z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code 1.0F}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(v.x, v.y, v.z, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point4F(final Vector3F v) {
		this(v.x, v.y, v.z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code v.x}, {@code v.y}, {@code v.z} and {@code v.w}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(v.x, v.y, v.z, v.w);
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point4F(final Vector4F v) {
		this(v.x, v.y, v.z, v.w);
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
	 * Constructs a new {@code Point4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point4F(x, y, z, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 */
	public Point4F(final float x, final float y, final float z) {
		this(x, y, z, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Point4F} instance given the component values {@code x}, {@code y}, {@code z} and {@code w}.
	 * 
	 * @param x the value of the X-component
	 * @param y the value of the Y-component
	 * @param z the value of the Z-component
	 * @param w the value of the W-component
	 */
	public Point4F(final float x, final float y, final float z, final float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point4F(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.x), Strings.toNonScientificNotationJava(this.y), Strings.toNonScientificNotationJava(this.z), Strings.toNonScientificNotationJava(this.w));
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
		} else if(!equal(this.w, Point4F.class.cast(object).w)) {
			return false;
		} else if(!equal(this.x, Point4F.class.cast(object).x)) {
			return false;
		} else if(!equal(this.y, Point4F.class.cast(object).y)) {
			return false;
		} else if(!equal(this.z, Point4F.class.cast(object).z)) {
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
	 * Returns a {@code float[]} representation of this {@code Point4F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Point4F} instance
	 */
	public float[] toArray() {
		return new float[] {this.x, this.y, this.z, this.w};
	}
	
	/**
	 * Returns a hash code for this {@code Point4F} instance.
	 * 
	 * @return a hash code for this {@code Point4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.w), Float.valueOf(this.x), Float.valueOf(this.y), Float.valueOf(this.z));
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
			dataOutput.writeFloat(this.x);
			dataOutput.writeFloat(this.y);
			dataOutput.writeFloat(this.z);
			dataOutput.writeFloat(this.w);
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
	 * @param p a {@code Point4F} instance
	 * @return a cached version of {@code p}
	 * @throws NullPointerException thrown if, and only if, {@code p} is {@code null}
	 */
	public static Point4F getCached(final Point4F p) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(p, "p == null"), key -> p);
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
		return new Point4F(Floats.lerp(a.x, b.x, t), Floats.lerp(a.y, b.y, t), Floats.lerp(a.z, b.z, t), Floats.lerp(a.w, b.w, t));
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
			return new Point4F(dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat(), dataInput.readFloat());
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Transforms the {@code Point4F} {@code pRHS} with the {@link Matrix44F} {@code mLHS}.
	 * <p>
	 * Returns a new {@code Point4F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param pRHS a {@code Point4F} instance
	 * @return a new {@code Point4F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point4F transform(final Matrix44F mLHS, final Point4F pRHS) {
		final float x = mLHS.getElement11() * pRHS.x + mLHS.getElement12() * pRHS.y + mLHS.getElement13() * pRHS.z + mLHS.getElement14() * pRHS.w;
		final float y = mLHS.getElement21() * pRHS.x + mLHS.getElement22() * pRHS.y + mLHS.getElement23() * pRHS.z + mLHS.getElement24() * pRHS.w;
		final float z = mLHS.getElement31() * pRHS.x + mLHS.getElement32() * pRHS.y + mLHS.getElement33() * pRHS.z + mLHS.getElement34() * pRHS.w;
		final float w = mLHS.getElement41() * pRHS.x + mLHS.getElement42() * pRHS.y + mLHS.getElement43() * pRHS.z + mLHS.getElement44() * pRHS.w;
		
		return new Point4F(x, y, z, w);
	}
	
	/**
	 * Transforms the {@code Point4F} {@code pRHS} with the {@link Matrix44F} {@code mLHS} and divides the result.
	 * <p>
	 * Returns a new {@code Point4F} instance with the result of the transformation.
	 * <p>
	 * If either {@code mLHS} or {@code pRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mLHS a {@code Matrix44F} instance
	 * @param pRHS a {@code Point4F} instance
	 * @return a new {@code Point4F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code mLHS} or {@code pRHS} are {@code null}
	 */
	public static Point4F transformAndDivide(final Matrix44F mLHS, final Point4F pRHS) {
		final float x = mLHS.getElement11() * pRHS.x + mLHS.getElement12() * pRHS.y + mLHS.getElement13() * pRHS.z + mLHS.getElement14() * pRHS.w;
		final float y = mLHS.getElement21() * pRHS.x + mLHS.getElement22() * pRHS.y + mLHS.getElement23() * pRHS.z + mLHS.getElement24() * pRHS.w;
		final float z = mLHS.getElement31() * pRHS.x + mLHS.getElement32() * pRHS.y + mLHS.getElement33() * pRHS.z + mLHS.getElement34() * pRHS.w;
		final float w = mLHS.getElement41() * pRHS.x + mLHS.getElement42() * pRHS.y + mLHS.getElement43() * pRHS.z + mLHS.getElement44() * pRHS.w;
		
		return equal(w, 1.0F) || isZero(w) ? new Point4F(x, y, z, w) : new Point4F(x / w, y / w, z / w, w);
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