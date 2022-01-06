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
package org.dayflower.scene;

import static org.dayflower.utility.Floats.minOrNaN;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

final class TreeBVHNode extends BVHNode {
	private final BVHNode bVHNodeL;
	private final BVHNode bVHNodeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TreeBVHNode(final Point3F a, final Point3F b, final int depth, final BVHNode bVHNodeL, final BVHNode bVHNodeR) {
		super(a, b, depth);
		
		this.bVHNodeL = Objects.requireNonNull(bVHNodeL, "bVHNodeL == null");
		this.bVHNodeR = Objects.requireNonNull(bVHNodeR, "bVHNodeR == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TreeBVHNode)) {
			return false;
		} else if(!Objects.equals(getBoundingVolume(), TreeBVHNode.class.cast(object).getBoundingVolume())) {
			return false;
		} else if(getDepth() != TreeBVHNode.class.cast(object).getDepth()) {
			return false;
		} else if(!Objects.equals(this.bVHNodeL, TreeBVHNode.class.cast(object).bVHNodeL)) {
			return false;
		} else if(!Objects.equals(this.bVHNodeR, TreeBVHNode.class.cast(object).bVHNodeR)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean intersection(final Intersector intersector) {
		if(intersector.isIntersecting(getBoundingVolume())) {
			final boolean isIntersectingL = this.bVHNodeL.intersection(intersector);
			final boolean isIntersectingR = this.bVHNodeR.intersection(intersector);
			
			return isIntersectingL || isIntersectingR;
		}
		
		return false;
	}
	
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.bVHNodeL.intersects(ray, tMinimum, tMaximum) || this.bVHNodeR.intersects(ray, tMinimum, tMaximum));
	}
	
	@Override
	public float intersectionT(final Ray3F ray, final float[] tBounds) {
		return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? minOrNaN(this.bVHNodeL.intersectionT(ray, tBounds), this.bVHNodeR.intersectionT(ray, tBounds)) : Float.NaN;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.bVHNodeL, this.bVHNodeR);
	}
}