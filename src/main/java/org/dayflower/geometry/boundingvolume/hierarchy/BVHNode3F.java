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
package org.dayflower.geometry.boundingvolume.hierarchy;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceIntersector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.Node;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code BVHNode3F} is a {@code float}-based node in a bounding volume hierarchy (BVH) structure.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class BVHNode3F implements Node {
	private final BoundingVolume3F boundingVolume;
	private final int depth;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BVHNode3F} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F} for the {@link AxisAlignedBoundingBox3F} instance that will be used
	 * @param b a reference {@code Point3F} for the {@code AxisAlignedBoundingBox3F} instance that will be used
	 * @param depth the depth
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected BVHNode3F(final Point3F a, final Point3F b, final int depth) {
		this.boundingVolume = new AxisAlignedBoundingBox3F(a, b);
		this.depth = ParameterArguments.requireRange(depth, 0, Integer.MAX_VALUE, "depth");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link BoundingVolume3F} instance associated with this {@code BVHNode3F} instance.
	 * 
	 * @return the {@code BoundingVolume3F} instance associated with this {@code BVHNode3F} instance
	 */
//	TODO: Add Unit Tests!
	public final BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BVHNode3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return intersection(Objects.requireNonNull(ray, "ray == null"), new float[] {tMinimum, tMaximum});
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code BVHNode3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code BVHNode3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3F} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code BVHNode3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public abstract boolean intersection(final SurfaceIntersector3F surfaceIntersector);
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code BVHNode3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BVHNode3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code BVHNode3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
	
	/**
	 * Returns the surface area of this {@code BVHNode3F} instance.
	 * 
	 * @return the surface area of this {@code BVHNode3F} instance
	 */
//	TODO: Add Unit Tests!
	public abstract float getSurfaceArea();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BVHNode3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return intersectionT(Objects.requireNonNull(ray, "ray == null"), new float[] {tMinimum, tMaximum});
	}
	
	/**
	 * Returns the depth associated with this {@code BVHNode3F} instance.
	 * 
	 * @return the depth associated with this {@code BVHNode3F} instance
	 */
//	TODO: Add Unit Tests!
	public final int getDepth() {
		return this.depth;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BVHNode3F} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected abstract Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float[] tBounds);
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code BVHNode3F} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected abstract float intersectionT(final Ray3F ray, final float[] tBounds);
}