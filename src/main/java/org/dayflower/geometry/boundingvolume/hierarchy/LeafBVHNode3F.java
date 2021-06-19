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

import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.minOrNaN;
import static org.dayflower.utility.Ints.padding;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceIntersector3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

//TODO: Add Javadocs!
public final class LeafBVHNode3F<T extends Shape3F> extends BVHNode3F {
	/**
	 * The offset for the shape count in the {@code int[]}.
	 * <p>
	 * This offset is used for leaf nodes only.
	 */
	public static final int ARRAY_OFFSET_SHAPE_COUNT = 3;
	
	/**
	 * The ID for all leaf nodes in the bounding volume hierarchy (BVH).
	 */
	public static final int ID = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<T> shapes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public LeafBVHNode3F(final Point3F a, final Point3F b, final int depth, final List<T> shapes) {
		super(a, b, depth);
		
		this.shapes = ParameterArguments.requireNonNullList(shapes, "shapes");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public List<T> getShapes() {
		return new ArrayList<>(this.shapes);
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!getBoundingVolume().accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				for(final Shape3F shape : this.shapes) {
					if(!shape.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
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
		} else if(!(object instanceof LeafBVHNode3F)) {
			return false;
		} else if(!Objects.equals(getBoundingVolume(), LeafBVHNode3F.class.cast(object).getBoundingVolume())) {
			return false;
		} else if(getDepth() != LeafBVHNode3F.class.cast(object).getDepth()) {
			return false;
		} else if(!Objects.equals(this.shapes, LeafBVHNode3F.class.cast(object).shapes)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean intersection(final SurfaceIntersector3F surfaceIntersector) {
		if(surfaceIntersector.isIntersecting(getBoundingVolume())) {
			boolean isIntersecting = false;
			
			for(final Shape3F shape : this.shapes) {
				if(surfaceIntersector.intersection(shape)) {
					isIntersecting = true;
				}
			}
			
			return isIntersecting;
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
			for(final Shape3F shape : this.shapes) {
				if(shape.intersects(ray, tMinimum, tMaximum)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	@Override
	public float getSurfaceArea() {
		float surfaceArea = 0.0F;
		
		for(final Shape3F shape : this.shapes) {
			surfaceArea += shape.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int getArrayLength() {
		return 4 + this.shapes.size() + padding(4 + this.shapes.size());
	}
	
//	TODO: Add Javadocs!
	public int getShapeCount() {
		return this.shapes.size();
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.shapes);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float[] tBounds) {
		Optional<SurfaceIntersection3F> optionalSurfaceIntersection = SurfaceIntersection3F.EMPTY;
		
		if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
			for(final Shape3F shape : this.shapes) {
				optionalSurfaceIntersection = SurfaceIntersection3F.closest(optionalSurfaceIntersection, shape.intersection(ray, tBounds[0], tBounds[1]));
				
				if(optionalSurfaceIntersection.isPresent()) {
					tBounds[1] = optionalSurfaceIntersection.get().getT();
				}
			}
		}
		
		return optionalSurfaceIntersection;
	}
	
//	TODO: Add Javadocs!
	@Override
	protected float intersectionT(final Ray3F ray, final float[] tBounds) {
		float t = Float.NaN;
		
		if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
			for(final Shape3F shape : this.shapes) {
				t = minOrNaN(t, shape.intersectionT(ray, tBounds[0], tBounds[1]));
				
				if(!isNaN(t)) {
					tBounds[1] = t;
				}
			}
		}
		
		return t;
	}
}