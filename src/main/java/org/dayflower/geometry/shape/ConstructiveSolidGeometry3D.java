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

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Matrix44D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;

import org.macroing.java.lang.Doubles;
import org.macroing.java.util.visitor.NodeHierarchicalVisitor;
import org.macroing.java.util.visitor.NodeTraversalException;

/**
 * A {@code ConstructiveSolidGeometry3D} is an implementation of {@link Shape3D} that can be used for constructive solid geometry (CSG).
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstructiveSolidGeometry3D implements Shape3D {
	/**
	 * The name of this {@code ConstructiveSolidGeometry3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Constructive Solid Geometry";
	
	/**
	 * The ID of this {@code ConstructiveSolidGeometry3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BoundingVolume3D boundingVolume;
	private final Matrix44D objectToShapeL;
	private final Matrix44D objectToShapeR;
	private final Matrix44D shapeLToObject;
	private final Matrix44D shapeRToObject;
	private final Operation operation;
	private final Shape3D shapeL;
	private final Shape3D shapeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * If either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ConstructiveSolidGeometry3D(operation, shapeL, shapeR, new Matrix44D(), new Matrix44D());
	 * }
	 * </pre>
	 * 
	 * @param operation the {@link Operation} instance associated with this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeL the {@link Shape3D} instance associated with the left-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeR the {@code Shape3D} instance associated with the right-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public ConstructiveSolidGeometry3D(final Operation operation, final Shape3D shapeL, final Shape3D shapeR) {
		this(operation, shapeL, shapeR, new Matrix44D(), new Matrix44D());
	}
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * If either {@code operation}, {@code shapeL}, {@code shapeR}, {@code shapeLToObject} or {@code shapeRToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code shapeLToObject} or {@code shapeRToObject} cannot be inverted, an {@code IllegalArgumentException} will be thrown
	 * 
	 * @param operation the {@link Operation} instance associated with this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeL the {@link Shape3D} instance associated with the left-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeR the {@code Shape3D} instance associated with the right-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeLToObject the {@link Matrix44D} instance to transform from {@code shapeL} to object space
	 * @param shapeRToObject the {@code Matrix44D} instance to transform from {@code shapeR} to object space
	 * @throws IllegalArgumentException thrown if, and only if, either {@code shapeLToObject} or {@code shapeRToObject} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code operation}, {@code shapeL}, {@code shapeR}, {@code shapeLToObject} or {@code shapeRToObject} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public ConstructiveSolidGeometry3D(final Operation operation, final Shape3D shapeL, final Shape3D shapeR, final Matrix44D shapeLToObject, final Matrix44D shapeRToObject) {
		this.operation = Objects.requireNonNull(operation, "operation == null");
		this.shapeL = Objects.requireNonNull(shapeL, "shapeL == null");
		this.shapeR = Objects.requireNonNull(shapeR, "shapeR == null");
		this.shapeLToObject = Objects.requireNonNull(shapeLToObject, "shapeLToObject == null");
		this.shapeRToObject = Objects.requireNonNull(shapeRToObject, "shapeRToObject == null");
		this.boundingVolume = AxisAlignedBoundingBox3D.union(shapeL.getBoundingVolume().transform(this.shapeLToObject), shapeR.getBoundingVolume().transform(this.shapeRToObject));
		this.objectToShapeL = Matrix44D.inverse(this.shapeLToObject);
		this.objectToShapeR = Matrix44D.inverse(this.shapeRToObject);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code ConstructiveSolidGeometry3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Ray3D rayShapeSpaceL = Ray3D.transform(this.objectToShapeL, ray);
		final Ray3D rayShapeSpaceR = Ray3D.transform(this.objectToShapeR, ray);
		
		final double tMinimumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMinimum);
		final double tMaximumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMaximum);
		
		final double tMinimumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMinimum);
		final double tMaximumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(rayShapeSpaceL.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return SurfaceIntersection3D.EMPTY;
				}
				
				if(this.shapeR.getBoundingVolume().contains(rayShapeSpaceR.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return SurfaceIntersection3D.EMPTY;
				}
				
				final double[] tIntervalShapeSpaceL = doFindTInterval(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL, this.shapeL);
				final double[] tIntervalShapeSpaceR = doFindTInterval(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR, this.shapeR);
				
				final double tShapeSpaceL0 = tIntervalShapeSpaceL[0];
				final double tShapeSpaceL1 = tIntervalShapeSpaceL[1];
				final double tShapeSpaceR0 = tIntervalShapeSpaceR[0];
				final double tShapeSpaceR1 = tIntervalShapeSpaceR[1];
				
				final double tObjectSpaceL0 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL0);
				final double tObjectSpaceL1 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL1);
				final double tObjectSpaceR0 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR0);
				final double tObjectSpaceR1 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR1);
				
//				No intersection with L and nothing to subtract from:
				if(Doubles.isNaN(tObjectSpaceL0)) {
					return SurfaceIntersection3D.EMPTY;
				}
				
//				No intersection with R and nothing to subtract:
				if(Doubles.isNaN(tObjectSpaceR0)) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				The closest intersection for L is closer than or equal to the closest intersection for R:
				if(tObjectSpaceL0 <= tObjectSpaceR0) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				No secondary intersection for L and nothing to subtract from:
				if(Doubles.isNaN(tObjectSpaceL1)) {
					return SurfaceIntersection3D.EMPTY;
				}
				
//				No secondary intersection for R:
				if(Doubles.isNaN(tObjectSpaceR1)) {
					return this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				}
				
				if(tObjectSpaceR1 <= tObjectSpaceL0) {
					return this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				}
				
//				The secondary intersection for R is farther away than or equal to the secondary intersection for L:
				if(tObjectSpaceR1 >= tObjectSpaceL1) {
					return SurfaceIntersection3D.EMPTY;
				}
				
				return this.shapeR.intersection(rayShapeSpaceR, tShapeSpaceR0 + 0.001D, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
			}
			case INTERSECTION: {
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionL = this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionR = this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = optionalSurfaceIntersectionL.isPresent() && optionalSurfaceIntersectionR.isPresent() ? SurfaceIntersection3D.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR) : SurfaceIntersection3D.EMPTY;
				
				return optionalSurfaceIntersection;
			}
			case UNION: {
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionL = this.shapeL.intersection(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeLToObject, this.objectToShapeL));
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionR = this.shapeR.intersection(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR).map(surfaceIntersection -> SurfaceIntersection3D.transform(surfaceIntersection, this.shapeRToObject, this.objectToShapeR));
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = SurfaceIntersection3D.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR);
				
				return optionalSurfaceIntersection;
			}
			default: {
				return SurfaceIntersection3D.EMPTY;
			}
		}
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new ConstructiveSolidGeometry3D(%s, %s, %s, %s, %s)", this.operation, this.shapeL, this.shapeR, this.shapeLToObject, this.shapeRToObject);
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
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code ConstructiveSolidGeometry3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method currently returns {@code false}.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code ConstructiveSolidGeometry3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		Objects.requireNonNull(point, "point == null");
		
		return false;
	}
	
	/**
	 * Compares {@code object} to this {@code ConstructiveSolidGeometry3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstructiveSolidGeometry3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstructiveSolidGeometry3D)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, ConstructiveSolidGeometry3D.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.objectToShapeL, ConstructiveSolidGeometry3D.class.cast(object).objectToShapeL)) {
			return false;
		} else if(!Objects.equals(this.objectToShapeR, ConstructiveSolidGeometry3D.class.cast(object).objectToShapeR)) {
			return false;
		} else if(!Objects.equals(this.shapeLToObject, ConstructiveSolidGeometry3D.class.cast(object).shapeLToObject)) {
			return false;
		} else if(!Objects.equals(this.shapeRToObject, ConstructiveSolidGeometry3D.class.cast(object).shapeRToObject)) {
			return false;
		} else if(!Objects.equals(this.operation, ConstructiveSolidGeometry3D.class.cast(object).operation)) {
			return false;
		} else if(!Objects.equals(this.shapeL, ConstructiveSolidGeometry3D.class.cast(object).shapeL)) {
			return false;
		} else if(!Objects.equals(this.shapeR, ConstructiveSolidGeometry3D.class.cast(object).shapeR)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area of this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		return 0.0D;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code ConstructiveSolidGeometry3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Ray3D rayShapeSpaceL = Ray3D.transform(this.objectToShapeL, ray);
		final Ray3D rayShapeSpaceR = Ray3D.transform(this.objectToShapeR, ray);
		
		final double tMinimumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMinimum);
		final double tMaximumShapeSpaceL = doTransformT(this.objectToShapeL, ray, rayShapeSpaceL, tMaximum);
		
		final double tMinimumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMinimum);
		final double tMaximumShapeSpaceR = doTransformT(this.objectToShapeR, ray, rayShapeSpaceR, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(rayShapeSpaceL.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return Double.NaN;
				}
				
				if(this.shapeR.getBoundingVolume().contains(rayShapeSpaceR.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return Double.NaN;
				}
				
				final double[] tIntervalShapeSpaceL = doFindTInterval(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL, this.shapeL);
				final double[] tIntervalShapeSpaceR = doFindTInterval(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR, this.shapeR);
				
				final double tShapeSpaceL0 = tIntervalShapeSpaceL[0];
				final double tShapeSpaceL1 = tIntervalShapeSpaceL[1];
				final double tShapeSpaceR0 = tIntervalShapeSpaceR[0];
				final double tShapeSpaceR1 = tIntervalShapeSpaceR[1];
				
				final double tObjectSpaceL0 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL0);
				final double tObjectSpaceL1 = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL1);
				final double tObjectSpaceR0 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR0);
				final double tObjectSpaceR1 = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR1);
				
				if(Doubles.isNaN(tShapeSpaceL0)) {
					return Double.NaN;
				}
				
				if(Doubles.isNaN(tShapeSpaceR0)) {
					return tObjectSpaceL0;
				}
				
				if(tObjectSpaceL0 <= tObjectSpaceR0) {
					return tObjectSpaceL0;
				}
				
				if(Doubles.isNaN(tObjectSpaceL1)) {
					return Double.NaN;
				}
				
				if(Doubles.isNaN(tObjectSpaceR1)) {
					return tObjectSpaceR0;
				}
				
				if(tObjectSpaceR1 <= tObjectSpaceL0) {
					return tObjectSpaceL0;
				}
				
				if(tObjectSpaceR1 >= tObjectSpaceL1) {
					return Double.NaN;
				}
				
				return tObjectSpaceR1;
			}
			case INTERSECTION: {
				final double tShapeSpaceL = this.shapeL.intersectionT(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL);
				final double tShapeSpaceR = this.shapeR.intersectionT(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR);
				final double tObjectSpaceL = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL);
				final double tObjectSpaceR = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR);
				final double t = !Doubles.isNaN(tObjectSpaceL) && !Doubles.isNaN(tObjectSpaceR) ? Doubles.min(tObjectSpaceL, tObjectSpaceR) : Double.NaN;
				
				return t;
			}
			case UNION: {
				final double tShapeSpaceL = this.shapeL.intersectionT(rayShapeSpaceL, tMinimumShapeSpaceL, tMaximumShapeSpaceL);
				final double tShapeSpaceR = this.shapeR.intersectionT(rayShapeSpaceR, tMinimumShapeSpaceR, tMaximumShapeSpaceR);
				final double tObjectSpaceL = doTransformT(this.shapeLToObject, rayShapeSpaceL, ray, tShapeSpaceL);
				final double tObjectSpaceR = doTransformT(this.shapeRToObject, rayShapeSpaceR, ray, tShapeSpaceR);
				final double t = Doubles.minOrDefault(tObjectSpaceL, tObjectSpaceR, Double.NaN);
				
				return t;
			}
			default: {
				return Double.NaN;
			}
		}
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a hash code for this {@code ConstructiveSolidGeometry3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.objectToShapeL, this.objectToShapeR, this.shapeLToObject, this.shapeRToObject, this.operation, this.shapeL, this.shapeR);
	}
	
	/**
	 * Writes this {@code ConstructiveSolidGeometry3D} instance to {@code dataOutput}.
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
	 * An {@code Operation} represents an operation that can be performed on a {@link ConstructiveSolidGeometry3D} instance.
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
	
	private static double doTransformT(final Matrix44D matrix, final Ray3D rayOldSpace, final Ray3D rayNewSpace, final double t) {
		return !Doubles.isNaN(t) && !Doubles.isZero(t) && t < Doubles.MAX_VALUE ? Doubles.abs(Point3D.distance(rayNewSpace.getOrigin(), Point3D.transformAndDivide(matrix, Point3D.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
	
	private static double[] doFindTInterval(final Ray3D ray, final double tMinimum, final double tMaximum, final Shape3D shape) {
		final double[] tInterval = new double[] {Double.NaN, Double.NaN};
		
		double currentTMinimum = tMinimum;
		double currentTMaximum = tMaximum;
		
		for(int i = 0; i < tInterval.length; i++) {
			if(Doubles.isNaN(tInterval[i])) {
				final double t = shape.intersectionT(ray, currentTMinimum, currentTMaximum);
				
				if(Doubles.isNaN(t)) {
					return tInterval;
				}
				
				tInterval[i] = t;
				
				currentTMinimum = t + 0.001D;
			}
		}
		
		return tInterval;
	}
}