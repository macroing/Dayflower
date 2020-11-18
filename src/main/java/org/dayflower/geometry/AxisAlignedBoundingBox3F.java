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

import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;

import java.util.Objects;

/**
 * An {@code AxisAlignedBoundingBox3F} denotes a 3-dimensional axis-aligned bounding box (AABB) that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AxisAlignedBoundingBox3F implements BoundingVolume3F {
	private final Point3F maximum;
	private final Point3F minimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AxisAlignedBoundingBox3F(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	 * }
	 * </pre>
	 */
	public AxisAlignedBoundingBox3F() {
		this(new Point3F(-0.5F, -0.5F, -0.5F), new Point3F(0.5F, 0.5F, 0.5F));
	}
	
	/**
	 * Constructs a new {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F}
	 * @param b a reference {@code Point3F}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public AxisAlignedBoundingBox3F(final Point3F a, final Point3F b) {
		this.maximum = Point3F.getCached(Point3F.maximum(a, b));
		this.minimum = Point3F.getCached(Point3F.minimum(a, b));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code AxisAlignedBoundingBox3F} instance with the result of the transformation.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code AxisAlignedBoundingBox3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	@Override
	public AxisAlignedBoundingBox3F transform(final Matrix44F matrix) {
		final float maximumX = this.maximum.getX();
		final float maximumY = this.maximum.getY();
		final float maximumZ = this.maximum.getZ();
		final float minimumX = this.minimum.getX();
		final float minimumY = this.minimum.getY();
		final float minimumZ = this.minimum.getZ();
		
		final Point3F[] points = new Point3F[] {
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, minimumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, minimumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, maximumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, minimumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(minimumX, maximumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, maximumY, minimumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, minimumY, maximumZ)),
			Point3F.transformAndDivide(matrix, new Point3F(maximumX, maximumY, maximumZ))
		};
		
		Point3F maximum = Point3F.MINIMUM;
		Point3F minimum = Point3F.MAXIMUM;
		
		for(final Point3F point : points) {
			maximum = Point3F.maximum(maximum, point);
			minimum = Point3F.minimum(minimum, point);
		}
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns a {@link Point3F} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@code Point3F} instance
	 * @return a {@code Point3F} instance that represents the closest point to {@code point} and is contained in this {@code AxisAlignedBoundingBox3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public Point3F getClosestPointTo(final Point3F point) {
		final float maximumX = this.maximum.getX();
		final float maximumY = this.maximum.getY();
		final float maximumZ = this.maximum.getZ();
		
		final float minimumX = this.minimum.getX();
		final float minimumY = this.minimum.getY();
		final float minimumZ = this.minimum.getZ();
		
		final float x = point.getX() < minimumX ? minimumX : point.getX() > maximumX ? maximumX : point.getX();
		final float y = point.getY() < minimumY ? minimumY : point.getY() > maximumY ? maximumY : point.getY();
		final float z = point.getZ() < minimumZ ? minimumZ : point.getZ() > maximumZ ? maximumZ : point.getZ();
		
		return new Point3F(x, y, z);
	}
	
	/**
	 * Returns a {@link Point3F} with the largest component values that are contained in this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code Point3F} with the largest component values that are contained in this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public Point3F getMaximum() {
		return this.maximum;
	}
	
	/**
	 * Returns a {@link Point3F} with the smallest component values that are contained in this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code Point3F} with the smallest component values that are contained in this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public Point3F getMinimum() {
		return this.minimum;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new AxisAlignedBoundingBox3F(%s, %s)", this.maximum, this.minimum);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code AxisAlignedBoundingBox3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3F point) {
		return point.getX() >= this.minimum.getX() && point.getX() <= this.maximum.getX() && point.getY() >= this.minimum.getY() && point.getY() <= this.maximum.getY() && point.getZ() >= this.minimum.getZ() && point.getZ() <= this.maximum.getZ();
	}
	
	/**
	 * Compares {@code object} to this {@code AxisAlignedBoundingBox3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code AxisAlignedBoundingBox3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code AxisAlignedBoundingBox3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof AxisAlignedBoundingBox3F)) {
			return false;
		} else if(!Objects.equals(this.maximum, AxisAlignedBoundingBox3F.class.cast(object).maximum)) {
			return false;
		} else if(!Objects.equals(this.minimum, AxisAlignedBoundingBox3F.class.cast(object).minimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return the surface area of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final float x = this.maximum.getX() - this.minimum.getX();
		final float y = this.maximum.getY() - this.minimum.getY();
		final float z = this.maximum.getZ() - this.minimum.getZ();
		final float surfaceArea = 2.0F * (x * y + y * z + z * x);
		
		return surfaceArea;
	}
	
	/**
	 * Returns the volume of this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return the volume of this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public float getVolume() {
		final float x = this.maximum.getX() - this.minimum.getX();
		final float y = this.maximum.getY() - this.minimum.getY();
		final float z = this.maximum.getZ() - this.minimum.getZ();
		final float volume = x * y * z;
		
		return volume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code AxisAlignedBoundingBox3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3F} instance, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code AxisAlignedBoundingBox3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance from {@code ray} to this {@code AxisAlignedBoundingBox3F} instance, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		/*
		final Point3F maximum = getMaximum();
		final Point3F minimum = getMinimum();
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionReciprocal = Vector3F.reciprocal(direction);
		
		final float t0X = (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t0Y = (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t0Z = (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final float t1X = (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final float t1Y = (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		final float t1Z = (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		final float t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final float t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		final float t = t0 > t1 ? Float.NaN : t0 >= tMinimum && t0 <= tMaximum ? t0 : t1 >= tMinimum && t1 <= tMaximum ? t1 : Float.NaN;
		
		return t;
		*/
		
		final Point3F maximum = getMaximum();
		final Point3F minimum = getMinimum();
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F directionReciprocal = Vector3F.reciprocal(direction);
		
		final boolean isSwappingX = directionReciprocal.getX() < 0.0F;
		final boolean isSwappingY = directionReciprocal.getY() < 0.0F;
		final boolean isSwappingZ = directionReciprocal.getZ() < 0.0F;
		
		final float xTMinimum = isSwappingX ? (maximum.getX() - origin.getX()) * directionReciprocal.getX() : (minimum.getX() - origin.getX()) * directionReciprocal.getX();
		final float xTMaximum = isSwappingX ? (minimum.getX() - origin.getX()) * directionReciprocal.getX() : (maximum.getX() - origin.getX()) * directionReciprocal.getX();
		final float yTMinimum = isSwappingY ? (maximum.getY() - origin.getY()) * directionReciprocal.getY() : (minimum.getY() - origin.getY()) * directionReciprocal.getY();
		final float yTMaximum = isSwappingY ? (minimum.getY() - origin.getY()) * directionReciprocal.getY() : (maximum.getY() - origin.getY()) * directionReciprocal.getY();
		
		float currentTMinimum = xTMinimum;
		float currentTMaximum = xTMaximum;
		
		if(currentTMinimum > yTMaximum || currentTMaximum <= yTMinimum) {
			return Float.NaN;
		}
		
		currentTMinimum = max(currentTMinimum, yTMinimum);
		currentTMaximum = min(currentTMaximum, yTMaximum);
		
		final float zTMinimum = isSwappingZ ? (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ() : (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		final float zTMaximum = isSwappingZ ? (minimum.getZ() - origin.getZ()) * directionReciprocal.getZ() : (maximum.getZ() - origin.getZ()) * directionReciprocal.getZ();
		
		if(currentTMinimum > zTMaximum || currentTMaximum <= zTMinimum) {
			return Float.NaN;
		}
		
		currentTMinimum = max(currentTMinimum, zTMinimum);
		currentTMaximum = min(currentTMaximum, zTMaximum);
		
		final float t = currentTMinimum > tMinimum && currentTMinimum < tMaximum ? currentTMinimum : currentTMaximum > tMinimum && currentTMaximum < tMaximum ? currentTMaximum : Float.NaN;
		
		return t;
	}
	
	/**
	 * Returns a hash code for this {@code AxisAlignedBoundingBox3F} instance.
	 * 
	 * @return a hash code for this {@code AxisAlignedBoundingBox3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.maximum, this.minimum);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is an expanded version of {@code axisAlignedBoundingBox}.
	 * <p>
	 * If {@code axisAlignedBoundingBox} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox an {@code AxisAlignedBoundingBox3F} instance
	 * @param delta the delta to expand with
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is an expanded version of {@code axisAlignedBoundingBox}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox} is {@code null}
	 */
	public static AxisAlignedBoundingBox3F expand(final AxisAlignedBoundingBox3F axisAlignedBoundingBox, final float delta) {
		final Point3F maximum = Point3F.add(axisAlignedBoundingBox.maximum, delta);
		final Point3F minimum = Point3F.subtract(axisAlignedBoundingBox.minimum, delta);
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code axisAlignedBoundingBoxLHS} and {@code axisAlignedBoundingBoxRHS}.
	 * <p>
	 * If either {@code axisAlignedBoundingBoxLHS} or {@code axisAlignedBoundingBoxRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBoxLHS an {@code AxisAlignedBoundingBox3F} instance
	 * @param axisAlignedBoundingBoxRHS an {@code AxisAlignedBoundingBox3F} instance
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code axisAlignedBoundingBoxLHS} and {@code axisAlignedBoundingBoxRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code axisAlignedBoundingBoxLHS} or {@code axisAlignedBoundingBoxRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3F union(final AxisAlignedBoundingBox3F axisAlignedBoundingBoxLHS, final AxisAlignedBoundingBox3F axisAlignedBoundingBoxRHS) {
		final Point3F maximum = Point3F.maximum(axisAlignedBoundingBoxLHS.maximum, axisAlignedBoundingBoxRHS.maximum);
		final Point3F minimum = Point3F.minimum(axisAlignedBoundingBoxLHS.minimum, axisAlignedBoundingBoxRHS.minimum);
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
	
	/**
	 * Returns an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code axisAlignedBoundingBoxLHS} and {@code pointRHS}.
	 * <p>
	 * If either {@code axisAlignedBoundingBoxLHS} or {@code pointRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBoxLHS an {@code AxisAlignedBoundingBox3F} instance
	 * @param pointRHS a {@code Point3F} instance
	 * @return an {@code AxisAlignedBoundingBox3F} instance that is the union of {@code axisAlignedBoundingBoxLHS} and {@code pointRHS}
	 * @throws NullPointerException thrown if, and only if, either {@code axisAlignedBoundingBoxLHS} or {@code pointRHS} are {@code null}
	 */
	public static AxisAlignedBoundingBox3F union(final AxisAlignedBoundingBox3F axisAlignedBoundingBoxLHS, final Point3F pointRHS) {
		final Point3F maximum = Point3F.maximum(axisAlignedBoundingBoxLHS.maximum, pointRHS);
		final Point3F minimum = Point3F.minimum(axisAlignedBoundingBoxLHS.minimum, pointRHS);
		
		return new AxisAlignedBoundingBox3F(maximum, minimum);
	}
}