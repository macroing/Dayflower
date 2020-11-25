/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.minOrNaN;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

//TODO: Add Javadocs!
public final class ConstructiveSolidGeometry3F implements Shape3F {
	private final BoundingVolume3F boundingVolume;
	private final Operation operation;
	private final Shape3F shapeL;
	private final Shape3F shapeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ConstructiveSolidGeometry3F(final Operation operation, final Shape3F shapeL, final Shape3F shapeR) {
		this.operation = Objects.requireNonNull(operation, "operation == null");
		this.shapeL = Objects.requireNonNull(shapeL, "shapeL == null");
		this.shapeR = Objects.requireNonNull(shapeR, "shapeR == null");
		this.boundingVolume = AxisAlignedBoundingBox3F.union(shapeL.getBoundingVolume(), shapeR.getBoundingVolume());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Samples this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code ConstructiveSolidGeometry3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code ConstructiveSolidGeometry3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return SurfaceSample3F.EMPTY;//TODO: Implement!
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
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionL = this.shapeL.intersection(ray, tMinimum, tMaximum);
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionR = this.shapeR.intersection(ray, tMinimum, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE:
				if(optionalSurfaceIntersectionL.isPresent() && !optionalSurfaceIntersectionR.isPresent()) {
					return optionalSurfaceIntersectionL;
				}
				
				if(optionalSurfaceIntersectionL.isPresent() && optionalSurfaceIntersectionR.isPresent()) {
					final SurfaceIntersection3F surfaceIntersectionL = optionalSurfaceIntersectionL.get();
					final SurfaceIntersection3F surfaceIntersectionR = optionalSurfaceIntersectionR.get();
					
					final float tL = surfaceIntersectionL.getT();
					final float tR = surfaceIntersectionR.getT();
					
					if(tL < tR) {
						return optionalSurfaceIntersectionL;
					}
					
					float t0 = tR;
					float t1 = tR;
					
					while(!isNaN(t0)) {
						t1 = t0;
						t0 = this.shapeR.intersectionT(ray, t0 + 0.001F, tMaximum);
					}
					
					return this.shapeL.intersection(ray, t1 + 0.001F, tMaximum);
				}
				
				return SurfaceIntersection3F.EMPTY;
			case INTERSECTION:
				return optionalSurfaceIntersectionL.isPresent() && optionalSurfaceIntersectionR.isPresent() ? SurfaceIntersection3F.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR) : SurfaceIntersection3F.EMPTY;
			case UNION:
				return SurfaceIntersection3F.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR);
			default:
				return SurfaceIntersection3F.EMPTY;
		}
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstructiveSolidGeometry3F(%s, %s, %s)", this.operation, this.shapeL, this.shapeR);
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
				if(!this.boundingVolume.accept(nodeHierarchicalVisitor)) {
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
	 * Compares {@code object} to this {@code ConstructiveSolidGeometry3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstructiveSolidGeometry3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstructiveSolidGeometry3F)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, ConstructiveSolidGeometry3F.class.cast(object).boundingVolume)) {
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
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code ConstructiveSolidGeometry3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code ConstructiveSolidGeometry3F} instance
	 * @param point the point on this {@code ConstructiveSolidGeometry3F} instance
	 * @param surfaceNormal the surface normal on this {@code ConstructiveSolidGeometry3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code ConstructiveSolidGeometry3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code ConstructiveSolidGeometry3F} instance
	 * @param direction the direction to this {@code ConstructiveSolidGeometry3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the surface area of this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area of this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the volume of this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the volume of this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public float getVolume() {
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
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float tL = this.shapeL.intersectionT(ray, tMinimum, tMaximum);
		final float tR = this.shapeR.intersectionT(ray, tMinimum, tMaximum);
		
		switch(this.operation) {
			case DIFFERENCE:
				if(!isNaN(tL) && isNaN(tR)) {
					return tL;
				}
				
				if(!isNaN(tL) && !isNaN(tR)) {
					if(tL < tR) {
						return tL;
					}
					
					float t0 = tR;
					float t1 = tR;
					
					while(!isNaN(t0)) {
						t1 = t0;
						t0 = this.shapeR.intersectionT(ray, t0 + 0.001F, tMaximum);
					}
					
					return this.shapeL.intersectionT(ray, t1 + 0.001F, tMaximum);
				}
				
				return Float.NaN;
			case INTERSECTION:
				return !isNaN(tL) && !isNaN(tR) ? min(tL, tR) : Float.NaN;
			case UNION:
				return minOrNaN(tL, tR);
			default:
				return Float.NaN;
		}
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code ConstructiveSolidGeometry3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return a {@code float[]} representation of this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code ConstructiveSolidGeometry3F} instance.
	 * 
	 * @return a hash code for this {@code ConstructiveSolidGeometry3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.operation, this.shapeL, this.shapeR);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static enum Operation {
//		TODO: Add Javadocs!
		DIFFERENCE,
		
//		TODO: Add Javadocs!
		INTERSECTION,
		
//		TODO: Add Javadocs!
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
}