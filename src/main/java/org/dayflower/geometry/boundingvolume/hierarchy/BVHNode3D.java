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

import java.lang.reflect.Field;//TODO: Add Javadocs!
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

//TODO: Add Javadocs!
public abstract class BVHNode3D implements Node {
	/**
	 * The offset for the offset of the {@link BoundingVolume3D} in the {@code int[]}.
	 * <p>
	 * The {@code BoundingVolume3D} is always an {@link AxisAlignedBoundingBox3D}.
	 * <p>
	 * This offset is used for both leaf and tree nodes.
	 */
	public static final int ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET = 1;
	
	/**
	 * The offset for the ID of the bounding volume hierarchy (BVH) node in the {@code int[]}.
	 * <p>
	 * This offset is used for both leaf and tree nodes.
	 */
	public static final int ARRAY_OFFSET_ID = 0;
	
	/**
	 * The offset for the left bounding volume hierarchy node or the shape count in the {@code int[]}.
	 * <p>
	 * This offset is used for both leaf and tree nodes.
	 */
	public static final int ARRAY_OFFSET_LEFT_OFFSET_OR_SHAPE_COUNT = 3;
	
	/**
	 * The offset for the next bounding volume hierarchy node in the {@code int[]}.
	 * <p>
	 * This offset is used for both leaf and tree nodes.
	 */
	public static final int ARRAY_OFFSET_NEXT_OFFSET = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public final BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
//	TODO: Add Javadocs!
	public final Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return intersection(Objects.requireNonNull(ray, "ray == null"), new double[] {tMinimum, tMaximum});
	}
	
//	TODO: Add Javadocs!
	public abstract boolean intersection(final SurfaceIntersector3D surfaceIntersector);
	
//	TODO: Add Javadocs!
	public abstract boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum);
	
	/**
	 * Returns the surface area of this {@code BVHNode3D} instance.
	 * 
	 * @return the surface area of this {@code BVHNode3D} instance
	 */
	public abstract double getSurfaceArea();
	
//	TODO: Add Javadocs!
	public final double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return intersectionT(Objects.requireNonNull(ray, "ray == null"), new double[] {tMinimum, tMaximum});
	}
	
	/**
	 * Returns the length of the array that contains a compiled version of this {@code BVHNode3D} instance.
	 * 
	 * @return the length of the array that contains a compiled version of this {@code BVHNode3D} instance
	 */
	public abstract int getArrayLength();
	
	/**
	 * Returns the depth associated with this {@code BVHNode3D} instance.
	 * 
	 * @return the depth associated with this {@code BVHNode3D} instance
	 */
	public final int getDepth() {
		return this.depth;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected abstract Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double[] tBounds);
	
//	TODO: Add Javadocs!
	protected abstract double intersectionT(final Ray3D ray, final double[] tBounds);
}