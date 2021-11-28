/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.Node;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code BVHNode3D} is a {@code double}-based node in a bounding volume hierarchy (BVH) structure.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class BVHNode3D implements Node {
	private final BoundingVolume3D boundingVolume;
	private final int depth;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BVHNode3D} instance.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a reference {@link Point3D} for the {@link AxisAlignedBoundingBox3D} instance that will be used
	 * @param b a reference {@code Point3D} for the {@code AxisAlignedBoundingBox3D} instance that will be used
	 * @param depth the depth
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected BVHNode3D(final Point3D a, final Point3D b, final int depth) {
		this.boundingVolume = new AxisAlignedBoundingBox3D(a, b);
		this.depth = ParameterArguments.requireRange(depth, 0, Integer.MAX_VALUE, "depth");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link BoundingVolume3D} instance associated with this {@code BVHNode3D} instance.
	 * 
	 * @return the {@code BoundingVolume3D} instance associated with this {@code BVHNode3D} instance
	 */
//	TODO: Add Unit Tests!
	public final BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BVHNode3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return intersection(Objects.requireNonNull(ray, "ray == null"), new double[] {tMinimum, tMaximum});
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code BVHNode3D} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code BVHNode3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3D} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code BVHNode3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public abstract boolean intersection(final SurfaceIntersector3D surfaceIntersector);
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code BVHNode3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BVHNode3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code BVHNode3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public abstract boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum);
	
	/**
	 * Returns the surface area of this {@code BVHNode3D} instance.
	 * 
	 * @return the surface area of this {@code BVHNode3D} instance
	 */
//	TODO: Add Unit Tests!
	public abstract double getSurfaceArea();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BVHNode3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return intersectionT(Objects.requireNonNull(ray, "ray == null"), new double[] {tMinimum, tMaximum});
	}
	
	/**
	 * Returns the depth associated with this {@code BVHNode3D} instance.
	 * 
	 * @return the depth associated with this {@code BVHNode3D} instance
	 */
//	TODO: Add Unit Tests!
	public final int getDepth() {
		return this.depth;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BVHNode3D} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected abstract Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double[] tBounds);
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code BVHNode3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code BVHNode3D} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	protected abstract double intersectionT(final Ray3D ray, final double[] tBounds);
}