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
package org.dayflower.geometry.boundingvolume.hierarchy;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

import org.macroing.java.lang.Doubles;

/**
 * A {@code TreeBVHNode3D} is an implementation of {@link BVHNode3D} that represents a tree node.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TreeBVHNode3D extends BVHNode3D {
	/**
	 * The ID for all tree nodes in the bounding volume hierarchy (BVH).
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BVHNode3D bVHNodeL;
	private final BVHNode3D bVHNodeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TreeBVHNode3D} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code bVHNodeL} or {@code bVHNodeR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a reference {@link Point3D} for the {@link AxisAlignedBoundingBox3D} instance that will be used
	 * @param b a reference {@code Point3D} for the {@code AxisAlignedBoundingBox3D} instance that will be used
	 * @param depth the depth
	 * @param bVHNodeL the {@link BVHNode3D} instance on the left-hand side
	 * @param bVHNodeR the {@code BVHNode3D} instance on the right-hand side
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code bVHNodeL} or {@code bVHNodeR} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public TreeBVHNode3D(final Point3D a, final Point3D b, final int depth, final BVHNode3D bVHNodeL, final BVHNode3D bVHNodeR) {
		super(a, b, depth);
		
		this.bVHNodeL = Objects.requireNonNull(bVHNodeL, "bVHNodeL == null");
		this.bVHNodeR = Objects.requireNonNull(bVHNodeR, "bVHNodeR == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!getBoundingVolume().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.bVHNodeL.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.bVHNodeR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code TreeBVHNode3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TreeBVHNode3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TreeBVHNode3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TreeBVHNode3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TreeBVHNode3D)) {
			return false;
		} else if(!Objects.equals(getBoundingVolume(), TreeBVHNode3D.class.cast(object).getBoundingVolume())) {
			return false;
		} else if(getDepth() != TreeBVHNode3D.class.cast(object).getDepth()) {
			return false;
		} else if(!Objects.equals(this.bVHNodeL, TreeBVHNode3D.class.cast(object).bVHNodeL)) {
			return false;
		} else if(!Objects.equals(this.bVHNodeR, TreeBVHNode3D.class.cast(object).bVHNodeR)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code TreeBVHNode3D} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code TreeBVHNode3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3D} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code TreeBVHNode3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean intersection(final SurfaceIntersector3D surfaceIntersector) {
		if(surfaceIntersector.isIntersecting(getBoundingVolume())) {
			final boolean isIntersectingL = this.bVHNodeL.intersection(surfaceIntersector);
			final boolean isIntersectingR = this.bVHNodeR.intersection(surfaceIntersector);
			
			return isIntersectingL || isIntersectingR;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TreeBVHNode3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TreeBVHNode3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TreeBVHNode3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.bVHNodeL.intersects(ray, tMinimum, tMaximum) || this.bVHNodeR.intersects(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Returns the surface area of this {@code TreeBVHNode3D} instance.
	 * 
	 * @return the surface area of this {@code TreeBVHNode3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		return this.bVHNodeL.getSurfaceArea() + this.bVHNodeR.getSurfaceArea();
	}
	
	/**
	 * Returns a hash code for this {@code TreeBVHNode3D} instance.
	 * 
	 * @return a hash code for this {@code TreeBVHNode3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.bVHNodeL, this.bVHNodeR);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TreeBVHNode3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TreeBVHNode3D} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double[] tBounds) {
		return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? SurfaceIntersection3D.closest(this.bVHNodeL.intersection(ray, tBounds), this.bVHNodeR.intersection(ray, tBounds)) : Optional.empty();
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code TreeBVHNode3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code TreeBVHNode3D} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected double intersectionT(final Ray3D ray, final double[] tBounds) {
		return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? Doubles.minOrDefault(this.bVHNodeL.intersectionT(ray, tBounds), this.bVHNodeR.intersectionT(ray, tBounds), Double.NaN) : Double.NaN;
	}
}