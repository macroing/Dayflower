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

import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.minOrNaN;

import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

final class LeafBVHNode extends BVHNode {
	private final List<Primitive> primitives;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public LeafBVHNode(final Point3F a, final Point3F b, final int depth, final List<Primitive> primitives) {
		super(a, b, depth);
		
		this.primitives = ParameterArguments.requireNonNullList(primitives, "primitives");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public List<Primitive> getPrimitives() {
		return this.primitives;
	}
	
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!getBoundingVolume().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				for(final Primitive primitive : this.primitives) {
					if(!primitive.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
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
		} else if(!(object instanceof LeafBVHNode)) {
			return false;
		} else if(!Objects.equals(getBoundingVolume(), LeafBVHNode.class.cast(object).getBoundingVolume())) {
			return false;
		} else if(getDepth() != LeafBVHNode.class.cast(object).getDepth()) {
			return false;
		} else if(!Objects.equals(this.primitives, LeafBVHNode.class.cast(object).primitives)) {
			return false;
		} else {
			return true;
		}
	}
	
	@Override
	public boolean intersection(final Intersector intersector) {
		if(intersector.isIntersecting(getBoundingVolume())) {
			boolean isIntersecting = false;
			
			for(final Primitive primitive : this.primitives) {
				if(intersector.intersection(primitive)) {
					isIntersecting = true;
				}
			}
			
			return isIntersecting;
		}
		
		return false;
	}
	
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
			for(final Primitive primitive : this.primitives) {
				if(primitive.intersects(ray, tMinimum, tMaximum)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Override
	public float intersectionT(final Ray3F ray, final float[] tBounds) {
		float t = Float.NaN;
		
		if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
			for(final Primitive primitive : this.primitives) {
				t = minOrNaN(t, primitive.intersectionT(ray, tBounds[0], tBounds[1]));
				
				if(!isNaN(t)) {
					tBounds[1] = t;
				}
			}
		}
		
		return t;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.primitives);
	}
}