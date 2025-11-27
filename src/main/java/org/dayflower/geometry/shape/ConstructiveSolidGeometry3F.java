/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Floats.minOrNaN;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;

import org.macroing.java.lang.Floats;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code ConstructiveSolidGeometry3F} is an implementation of {@link Shape3F} that can be used for constructive solid geometry (CSG).
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstructiveSolidGeometry3F implements Shape3F {
	/**
	 * The name of this {@code ConstructiveSolidGeometry3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Constructive Solid Geometry";
	
	/**
	 * The ID of this {@code ConstructiveSolidGeometry3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BoundingVolume3F boundingVolume;
	private final Matrix44F objectToShapeL;
	private final Matrix44F objectToShapeR;
	private final Matrix44F shapeLToObject;
	private final Matrix44F shapeRToObject;
	private final Operation operation;
	private final Shape3F shapeL;
	private final Shape3F shapeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * If either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConstructiveSolidGeometry3F(operation, shapeL, shapeR, new Matrix44F(), new Matrix44F());
	 * }
	 * </pre>
	 * 
	 * @param operation the {@link Operation} instance associated with this {@code ConstructiveSolidGeometry3F} instance
	 * @param shapeL the {@link Shape3F} instance associated with the left-hand side of this {@code ConstructiveSolidGeometry3F} instance
	 * @param shapeR the {@code Shape3F} instance associated with the right-hand side of this {@code ConstructiveSolidGeometry3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public ConstructiveSolidGeometry3F(final Operation operation, final Shape3F shapeL, final Shape3F shapeR) {
		this(operation, shapeL, shapeR, new Matrix44F(), new Matrix44F());
	}
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * If either {@code operation}, {@code shapeL}, {@code shapeR}, {@code shapeLToObject} or {@code shapeRToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code shapeLToObject} or {@code shapeRToObject} cannot be inverted, an {@code IllegalArgumentException} will be thrown
	 * 
	 * @param operation the {@link Operation} instance associated with this {@code ConstructiveSolidGeometry3F} instance
	 * @param shapeL the {@link Shape3F} instance associated with the left-hand side of this {@code ConstructiveSolidGeometry3F} instance
	 * @param shapeR the {@code Shape3F} instance associated with the right-hand side of this {@code ConstructiveSolidGeometry3F} instance
	 * @param shapeLToObject the {@link Matrix44F} instance to transform from {@code shapeL} to object space
	 * @param shapeRToObject the {@code Matrix44F} instance to transform from {@code shapeR} to object space
	 * @throws IllegalArgumentException thrown if, and only if, either {@code shapeLToObject} or {@code shapeRToObject} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code operation}, {@code shapeL}, {@code shapeR}, {@code shapeLToObject} or {@code shapeRToObject} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public ConstructiveSolidGeometry3F(final Operation operation, final Shape3F shapeL, final Shape3F shapeR, final Matrix44F shapeLToObject, final Matrix44F shapeRToObject) {
		this.operation = Objects.requireNonNull(operation, "operation == null");
		this.shapeL = Objects.requireNonNull(shapeL, "shapeL == null");
		this.shapeR = Objects.requireNonNull(shapeR, "shapeR == null");
		this.shapeLToObject = Objects.requireNonNull(shapeLToObject, "shapeLToObject == null");
		this.shapeRToObject = Objects.requireNonNull(shapeRToObject, "shapeRToObject == null");
		this.boundingVolume = AxisAlignedBoundingBox3F.union(shapeL.getBoundingVolume().transform(this.shapeLToObject), shapeR.getBoundingVolume().transform(this.shapeRToObject));
		this.objectToShapeL = Matrix44F.inverse(this.shapeLToObject);
		this.objectToShapeR = Matrix44F.inverse(this.shapeRToObject);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code ConstructiveSolidGeometry3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Ray3F rayShapeSpaceL = Ray3F.transform(this.objectToShapeL, ray);
		final Ray3F rayShapeSpaceR = Ray3F.transform(this.objectToShapeR, ray);
		
		final float tMinimumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMinimum);
		final float tMaximumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMaximum);
		
		final float tMinimumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMinimum);
		final float tMaximumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(rayShapeSpaceL.getOrigin())) {
//					TODO: If the BoundingVolume3F contains the Ray3F, that should probably be handled in a different way.
					return SurfaceIntersection3F.EMPTY;
				}
				
				if(this.shapeR.getBoundingVolume().contains(rayShapeSpaceR.getOrigin())) {
//					TODO: If the BoundingVolume3F contains the Ray3F, that should probably be handled in a different way.
					return SurfaceIntersection3F.EMPTY;
				}
				
				final float[] tIntervalShapeSpaceL = doFindTInterval(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL, this.shapeL);
				final float[] tIntervalShapeSpaceR = doFindTInterval(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR, this.shapeR);
				
				final float tShapeSpaceL0 = tIntervalShapeSpaceL[0];
				final float tShapeSpaceL1 = tIntervalShapeSpaceL[1];
				final float tShapeSpaceR0 = tIntervalShapeSpaceR[0];
				final float tShapeSpaceR1 = tIntervalShapeSpaceR[1];
				
				final float tObjectSpaceL0 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL0);
				final float tObjectSpaceL1 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL1);
				final float tObjectSpaceR0 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR0);
				final float tObjectSpaceR1 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR1);
				
//				No intersection with L and nothing to subtract from:
				if(Floats.isNaN(tObjectSpaceL0)) {
					return SurfaceIntersection3F.EMPTY;
				}
				
//				No intersection with R and nothing to subtract:
				if(Floats.isNaN(tObjectSpaceR0)) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				The closest intersection for L is closer than or equal to the closest intersection for R:
				if(tObjectSpaceL0 <= tObjectSpaceR0) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				No secondary intersection for L and nothing to subtract from:
				if(Floats.isNaN(tObjectSpaceL1)) {
					return SurfaceIntersection3F.EMPTY;
				}
				
//				No secondary intersection for R:
				if(Floats.isNaN(tObjectSpaceR1)) {
					return this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				}
				
				if(tObjectSpaceR1 <= tObjectSpaceL0) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				The secondary intersection for R is farther away than or equal to the secondary intersection for L:
				if(tObjectSpaceR1 >= tObjectSpaceL1) {
					return SurfaceIntersection3F.EMPTY;
				}
				
				return this.shapeR.intersection(rayShapeSpaceR, tShapeSpaceR0 + 0.001F, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
			}
			case INTERSECTION: {
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionL = this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionR = this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = optionalSurfaceIntersectionL.isPresent() && optionalSurfaceIntersectionR.isPresent() ? SurfaceIntersection3F.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR) : SurfaceIntersection3F.EMPTY;
				
				return optionalSurfaceIntersection;
			}
			case UNION: {
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionL = this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionR = this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3F.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = SurfaceIntersection3F.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR);
				
				return optionalSurfaceIntersection;
			}
			default: {
				return SurfaceIntersection3F.EMPTY;
			}
		}
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new ConstructiveSolidGeometry3F(%s, %s, %s, %s, %s)", this.operation, this.shapeL, this.shapeR, this.shapeLToObject, this.shapeRToObject);
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
//	TODO: Add Unit Tests!
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.objectToShapeL.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.objectToShapeR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shapeLToObject.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shapeRToObject.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shapeL.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.shapeR.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code ConstructiveSolidGeometry3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method currently returns {@code false}.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code ConstructiveSolidGeometry3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		Objects.requireNonNull(point, "point == null");
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code ConstructiveSolidGeometry3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstructiveSolidGeometry3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstructiveSolidGeometry3F)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, ConstructiveSolidGeometry3F.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.objectToShapeL, ConstructiveSolidGeometry3F.class.cast(object).objectToShapeL)) {
			return false;
		} else if(!Objects.equals(this.objectToShapeR, ConstructiveSolidGeometry3F.class.cast(object).objectToShapeR)) {
			return false;
		} else if(!Objects.equals(this.shapeLToObject, ConstructiveSolidGeometry3F.class.cast(object).shapeLToObject)) {
			return false;
		} else if(!Objects.equals(this.shapeRToObject, ConstructiveSolidGeometry3F.class.cast(object).shapeRToObject)) {
			return false;
		} else if(!Objects.equals(this.operation, ConstructiveSolidGeometry3F.class.cast(object).operation)) {
			return false;
		} else if(!Objects.equals(this.shapeL, ConstructiveSolidGeometry3F.class.cast(object).shapeL)) {
			return false;
		} else if(!Objects.equals(this.shapeR, ConstructiveSolidGeometry3F.class.cast(object).shapeR)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area of this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code ConstructiveSolidGeometry3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Ray3F rayShapeSpaceL = Ray3F.transform(this.objectToShapeL, ray);
		final Ray3F rayShapeSpaceR = Ray3F.transform(this.objectToShapeR, ray);
		
		final float tMinimumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMinimum);
		final float tMaximumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMaximum);
		
		final float tMinimumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMinimum);
		final float tMaximumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(rayShapeSpaceL.getOrigin())) {
//					TODO: If the BoundingVolume3F contains the Ray3F, that should probably be handled in a different way.
					return Float.NaN;
				}
				
				if(this.shapeR.getBoundingVolume().contains(rayShapeSpaceR.getOrigin())) {
//					TODO: If the BoundingVolume3F contains the Ray3F, that should probably be handled in a different way.
					return Float.NaN;
				}
				
				final float[] tIntervalShapeSpaceL = doFindTInterval(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL, this.shapeL);
				final float[] tIntervalShapeSpaceR = doFindTInterval(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR, this.shapeR);
				
				final float tShapeSpaceL0 = tIntervalShapeSpaceL[0];
				final float tShapeSpaceL1 = tIntervalShapeSpaceL[1];
				final float tShapeSpaceR0 = tIntervalShapeSpaceR[0];
				final float tShapeSpaceR1 = tIntervalShapeSpaceR[1];
				
				final float tObjectSpaceL0 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL0);
				final float tObjectSpaceL1 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL1);
				final float tObjectSpaceR0 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR0);
				final float tObjectSpaceR1 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR1);
				
				if(Floats.isNaN(tShapeSpaceL0)) {
					return Float.NaN;
				}
				
				if(Floats.isNaN(tShapeSpaceR0)) {
					return tObjectSpaceL0;
				}
				
				if(tObjectSpaceL0 <= tObjectSpaceR0) {
					return tObjectSpaceL0;
				}
				
				if(Floats.isNaN(tObjectSpaceL1)) {
					return Float.NaN;
				}
				
				if(Floats.isNaN(tObjectSpaceR1)) {
					return tObjectSpaceR0;
				}
				
				if(tObjectSpaceR1 <= tObjectSpaceL0) {
					return tObjectSpaceL0;
				}
				
				if(tObjectSpaceR1 >= tObjectSpaceL1) {
					return Float.NaN;
				}
				
				return tObjectSpaceR1;
			}
			case INTERSECTION: {
				final float tShapeSpaceL = this.shapeL.intersectionT(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL);
				final float tShapeSpaceR = this.shapeR.intersectionT(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR);
				final float tObjectSpaceL = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL);
				final float tObjectSpaceR = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR);
				final float t = !Floats.isNaN(tObjectSpaceL) && !Floats.isNaN(tObjectSpaceR) ? Floats.min(tObjectSpaceL, tObjectSpaceR) : Float.NaN;
				
				return t;
			}
			case UNION: {
				final float tShapeSpaceL = this.shapeL.intersectionT(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL);
				final float tShapeSpaceR = this.shapeR.intersectionT(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR);
				final float tObjectSpaceL = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL);
				final float tObjectSpaceR = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR);
				final float t = minOrNaN(tObjectSpaceL, tObjectSpaceR);
				
				return t;
			}
			default: {
				return Float.NaN;
			}
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a hash code for this {@code ConstructiveSolidGeometry3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.objectToShapeL, this.objectToShapeR, this.shapeLToObject, this.shapeRToObject, this.operation, this.shapeL, this.shapeR);
	}
	
	/**
	 * Writes this {@code ConstructiveSolidGeometry3F} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
			dataOutput.writeInt(this.operation.ordinal());
			
			this.shapeL.write(dataOutput);
			this.shapeR.write(dataOutput);
			
			this.shapeLToObject.write(dataOutput);
			this.shapeRToObject.write(dataOutput);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * An {@code Operation} represents an operation that can be performed on a {@link ConstructiveSolidGeometry3F} instance.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static enum Operation {
		/**
		 * An {@code Operation} instance that represents a difference operation.
		 */
		DIFFERENCE,
		
		/**
		 * An {@code Operation} instance that represents an intersection operation.
		 */
		INTERSECTION,
		
		/**
		 * An {@code Operation} instance that represents a union operation.
		 */
		UNION;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Operation() {
			
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns a {@code String} representation of this {@code Operation} instance.
		 * 
		 * @return a {@code String} representation of this {@code Operation} instance
		 */
//		TODO: Add Unit Tests!
		@Override
		public String toString() {
			switch(this) {
				case DIFFERENCE:
					return "Operation.DIFFERENCE";
				case INTERSECTION:
					return "Operation.INTERSECTION";
				case UNION:
					return "Operation.UNION";
				default:
					return "";
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !Floats.isNaN(t) && !Floats.isZero(t) && t < Floats.MAX_VALUE ? Floats.abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transformAndDivide(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
	
	private static float[] doFindTInterval(final Ray3F ray, final float tMinimum, final float tMaximum, final Shape3F shape) {
		final float[] tInterval = new float[] {Float.NaN, Float.NaN};
		
		float currentTMinimum = tMinimum;
		float currentTMaximum = tMaximum;
		
		for(int i = 0; i < tInterval.length; i++) {
			if(Floats.isNaN(tInterval[i])) {
				final float t = shape.intersectionT(ray, currentTMinimum, currentTMaximum);
				
				if(Floats.isNaN(t)) {
					return tInterval;
				}
				
				tInterval[i] = t;
				
				currentTMinimum = t + 0.001F;
			}
		}
		
		return tInterval;
	}
}