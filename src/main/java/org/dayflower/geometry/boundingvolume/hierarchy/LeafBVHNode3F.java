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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceIntersector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code LeafBVHNode3F} is an implementation of {@link BVHNode3F} that represents a leaf node.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
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
	
	/**
	 * Constructs a new {@code LeafBVHNode3F} instance.
	 * <p>
	 * If either {@code a}, {@code b}, {@code shapes} or any of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code depth} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param a a reference {@link Point3F} for the {@link AxisAlignedBoundingBox3F} instance that will be used
	 * @param b a reference {@code Point3F} for the {@code AxisAlignedBoundingBox3F} instance that will be used
	 * @param depth the depth
	 * @param shapes a {@code List} of {@link Shape3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code depth} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b}, {@code shapes} or any of its elements are {@code null}
	 */
	public LeafBVHNode3F(final Point3F a, final Point3F b, final int depth, final List<T> shapes) {
		super(a, b, depth);
		
		this.shapes = ParameterArguments.requireNonNullList(shapes, "shapes");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} with all {@link Shape3F} instances associated with this {@code LeafBVHNode3F} instance.
	 * <p>
	 * Modification to the returned {@code List} will not affect this {@code LeafBVHNode3F} instance.
	 * 
	 * @return a {@code List} with all {@code Shape3F} instances associated with this {@code LeafBVHNode3F} instance
	 */
	public List<T> getShapes() {
		return new ArrayList<>(this.shapes);
	}
	
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
	
	/**
	 * Compares {@code object} to this {@code LeafBVHNode3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LeafBVHNode3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LeafBVHNode3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LeafBVHNode3F}, and their respective values are equal, {@code false} otherwise
	 */
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
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code LeafBVHNode3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code LeafBVHNode3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3F} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code LeafBVHNode3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
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
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code LeafBVHNode3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code LeafBVHNode3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code LeafBVHNode3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
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
	
	/**
	 * Returns the surface area of this {@code LeafBVHNode3F} instance.
	 * 
	 * @return the surface area of this {@code LeafBVHNode3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		float surfaceArea = 0.0F;
		
		for(final Shape3F shape : this.shapes) {
			surfaceArea += shape.getSurfaceArea();
		}
		
		return surfaceArea;
	}
	
	/**
	 * Returns the length of the array that contains a compiled version of this {@code LeafBVHNode3F} instance.
	 * 
	 * @return the length of the array that contains a compiled version of this {@code LeafBVHNode3F} instance
	 */
	@Override
	public int getArrayLength() {
		return 4 + this.shapes.size() + padding(4 + this.shapes.size());
	}
	
	/**
	 * Returns the {@link Shape3F} instance count of this {@code LeafBVHNode3F} instance.
	 * 
	 * @return the {@code Shape3F} instance count of this {@code LeafBVHNode3F} instance
	 */
	public int getShapeCount() {
		return this.shapes.size();
	}
	
	/**
	 * Returns a hash code for this {@code LeafBVHNode3F} instance.
	 * 
	 * @return a hash code for this {@code LeafBVHNode3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.shapes);
	}
	
	/**
	 * Adds all {@link Shape3F} instances associated with this {@code LeafBVHNode3F} to {@code shapes}.
	 * <p>
	 * If {@code shapes} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shapes the {@code List} to add all {@code Shape3F} instances to
	 * @throws NullPointerException thrown if, and only if, {@code shapes} is {@code null}
	 */
	public void addShapesTo(final List<T> shapes) {
		Objects.requireNonNull(shapes, "shapes == null");
		
		for(final T shape : this.shapes) {
			shapes.add(shape);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code LeafBVHNode3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code LeafBVHNode3F} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
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
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code LeafBVHNode3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If either {@code ray} or {@code tBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code LeafBVHNode3F} instance
	 * @param tBounds the minimum and maximum parametric distances
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, either {@code ray} or {@code tBounds} are {@code null}
	 */
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