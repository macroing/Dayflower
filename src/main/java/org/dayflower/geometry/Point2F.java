/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import static org.dayflower.util.Floats.PI_DIVIDED_BY_2;
import static org.dayflower.util.Floats.PI_DIVIDED_BY_4;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.util.Floats.cos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

/**
 * A {@code Point2F} denotes a 2-dimensional point with two coordinates, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Point2F {
	private final float component1;
	private final float component2;
	
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
	 * Constructs a new {@code Point2F} instance given the component values {@code v.getComponent1()} and {@code v.getComponent2()}.
	 * <p>
	 * If {@code v} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Point2F(v.getComponent1(), v.getComponent2());
	 * }
	 * </pre>
	 * 
	 * @param v a {@link Vector2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code v} is {@code null}
	 */
	public Point2F(final Vector2F v) {
		this(v.getComponent1(), v.getComponent2());
	}
	
	/**
	 * Constructs a new {@code Point2F} instance given the component values {@code component1} and {@code component2}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 */
	public Point2F(final float component1, final float component2) {
		this.component1 = component1;
		this.component2 = component2;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Point2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Point2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Point2F(%+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2));
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
		} else if(!equal(this.component1, Point2F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Point2F.class.cast(object).component2)) {
			return false;
		} else {
			return true;
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
	 * Returns a hash code for this {@code Point2F} instance.
	 * 
	 * @return a hash code for this {@code Point2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples a point on a concentric disk.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point2F.sampleConcentricDisk(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2F} instance with the sampled point
	 */
	public static Point2F sampleConcentricDisk() {
		return sampleConcentricDisk(random(), random());
	}
	
	/**
	 * Samples a point on a concentric disk.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point2F.sampleConcentricDisk(u, v, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point2F} instance with the sampled point
	 */
	public static Point2F sampleConcentricDisk(final float u, final float v) {
		return sampleConcentricDisk(u, v, 1.0F);
	}
	
	/**
	 * Samples a point on a concentric disk.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param radius the radius of the disk
	 * @return a {@code Point2F} instance with the sampled point
	 */
	public static Point2F sampleConcentricDisk(final float u, final float v, final float radius) {
		if(equal(u, 0.0F) && equal(v, 0.0F)) {
			return new Point2F();
		}
		
		final float a = u * 2.0F - 1.0F;
		final float b = v * 2.0F - 1.0F;
		
		if(a * a > b * b) {
			final float phi = PI_DIVIDED_BY_4 * (b / a);
			final float r = radius * a;
			
			final float component1 = cos(phi) * r;
			final float component2 = sin(phi) * r;
			
			return new Point2F(component1, component2);
		}
		
		final float phi = PI_DIVIDED_BY_2 - (PI_DIVIDED_BY_4 * (a / b));
		final float r = radius * b;
		
		final float component1 = cos(phi) * r;
		final float component2 = sin(phi) * r;
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Point2F.sampleDiskUniformDistribution(Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Point2F} instance with the sampled point
	 */
	public static Point2F sampleDiskUniformDistribution() {
		return sampleDiskUniformDistribution(random(), random());
	}
	
	/**
	 * Samples a point on a disk with a uniform distribution.
	 * <p>
	 * Returns a {@code Point2F} instance with the sampled point.
	 * 
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return a {@code Point2F} instance with the sampled point
	 */
	public static Point2F sampleDiskUniformDistribution(final float u, final float v) {
		final float phi = PI_MULTIPLIED_BY_2 * v;
		final float r = sqrt(u);
		
		final float component1 = cos(phi) * r;
		final float component2 = sin(phi) * r;
		
		return new Point2F(component1, component2);
	}
	
	/**
	 * Returns the distance from {@code pEye} to {@code pLookAt}.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@code Point2F} instance
	 * @param pLookAt a {@code Point2F} instance
	 * @return the distance from {@code pEye} to {@code pLookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static float distance(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).length();
	}
	
	/**
	 * Returns the squared distance from {@code pEye} to {@code pLookAt}.
	 * <p>
	 * If either {@code pEye} or {@code pLookAt} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pEye a {@code Point2F} instance
	 * @param pLookAt a {@code Point2F} instance
	 * @return the squared distance from {@code pEye} to {@code pLookAt}
	 * @throws NullPointerException thrown if, and only if, either {@code pEye} or {@code pLookAt} are {@code null}
	 */
	public static float distanceSquared(final Point2F pEye, final Point2F pLookAt) {
		return Vector2F.direction(pEye, pLookAt).lengthSquared();
	}
}