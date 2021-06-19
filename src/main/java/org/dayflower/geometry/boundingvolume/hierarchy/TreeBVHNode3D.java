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

import static org.dayflower.utility.Doubles.minOrNaN;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceIntersector3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

//TODO: Add Javadocs!
public final class TreeBVHNode3D extends BVHNode3D {
	/**
	 * The offset for the left bounding volume hierarchy node in the {@code int[]}.
	 * <p>
	 * This offset is used for tree nodes only.
	 */
	public static final int ARRAY_OFFSET_LEFT_OFFSET = 3;
	
	/**
	 * The ID for all tree nodes in the bounding volume hierarchy (BVH).
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BVHNode3D bVHNodeL;
	private final BVHNode3D bVHNodeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public TreeBVHNode3D(final Point3D a, final Point3D b, final int depth, final BVHNode3D bVHNodeL, final BVHNode3D bVHNodeR) {
		super(a, b, depth);
		
		this.bVHNodeL = Objects.requireNonNull(bVHNodeL, "bVHNodeL == null");
		this.bVHNodeR = Objects.requireNonNull(bVHNodeR, "bVHNodeR == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	@Override
	public boolean intersection(final SurfaceIntersector3D surfaceIntersector) {
		if(surfaceIntersector.isIntersecting(getBoundingVolume())) {
			final boolean isIntersectingL = this.bVHNodeL.intersection(surfaceIntersector);
			final boolean isIntersectingR = this.bVHNodeR.intersection(surfaceIntersector);
			
			return isIntersectingL || isIntersectingR;
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.bVHNodeL.intersects(ray, tMinimum, tMaximum) || this.bVHNodeR.intersects(ray, tMinimum, tMaximum));
	}
	
//	TODO: Add Javadocs!
	@Override
	public double getSurfaceArea() {
		return this.bVHNodeL.getSurfaceArea() + this.bVHNodeR.getSurfaceArea();
	}
	
//	TODO: Add Javadocs!
	@Override
	public int getArrayLength() {
		return 8;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.bVHNodeL, this.bVHNodeR);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double[] tBounds) {
		return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? SurfaceIntersection3D.closest(this.bVHNodeL.intersection(ray, tBounds), this.bVHNodeR.intersection(ray, tBounds)) : Optional.empty();
	}
	
//	TODO: Add Javadocs!
	@Override
	protected double intersectionT(final Ray3D ray, final double[] tBounds) {
		return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? minOrNaN(this.bVHNodeL.intersectionT(ray, tBounds), this.bVHNodeR.intersectionT(ray, tBounds)) : Double.NaN;
	}
}