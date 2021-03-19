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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.minOrNaN;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code ConstructiveSolidGeometry3D} denotes a 3-dimensional Constructive Solid Geometry (CSG) shape that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@link Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ConstructiveSolidGeometry3D implements Shape3D {
	/**
	 * The name of this {@code ConstructiveSolidGeometry3D} class.
	 */
	public static final String NAME = "Constructive Solid Geometry";
	
	/**
	 * The ID of this {@code ConstructiveSolidGeometry3D} class.
	 */
	public static final int ID = 2;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final BoundingVolume3D boundingVolume;
	private final Operation operation;
	private final Shape3D shapeL;
	private final Shape3D shapeR;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ConstructiveSolidGeometry3D} instance.
	 * <p>
	 * If either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param operation the {@link Operation} instance associated with this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeL the {@link Shape3D} instance associated with the left-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @param shapeR the {@link Shape3D} instance associated with the right-hand side of this {@code ConstructiveSolidGeometry3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code operation}, {@code shapeL} or {@code shapeR} are {@code null}
	 */
	public ConstructiveSolidGeometry3D(final Operation operation, final Shape3D shapeL, final Shape3D shapeR) {
		this.operation = Objects.requireNonNull(operation, "operation == null");
		this.shapeL = Objects.requireNonNull(shapeL, "shapeL == null");
		this.shapeR = Objects.requireNonNull(shapeR, "shapeR == null");
		this.boundingVolume = AxisAlignedBoundingBox3D.union(shapeL.getBoundingVolume(), shapeR.getBoundingVolume());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code ConstructiveSolidGeometry3D} instance
	 */
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
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(ray.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return SurfaceIntersection3D.EMPTY;
				}
				
				if(this.shapeR.getBoundingVolume().contains(ray.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return SurfaceIntersection3D.EMPTY;
				}
				
				final double[] tIntervalL = doFindTInterval(ray, tMinimum, tMaximum, this.shapeL);
				final double[] tIntervalR = doFindTInterval(ray, tMinimum, tMaximum, this.shapeR);
				
				final double tL0 = tIntervalL[0];
				final double tL1 = tIntervalL[1];
				final double tR0 = tIntervalR[0];
				final double tR1 = tIntervalR[1];
				
//				No intersection with L and nothing to subtract from:
				if(isNaN(tL0)) {
					return SurfaceIntersection3D.EMPTY;
				}
				
//				No intersection with R and nothing to subtract:
				if(isNaN(tR0)) {
					return this.shapeL.intersection(ray, tMinimum, tMaximum);
				}
				
//				The closest intersection for L is closer than or equal to the closest intersection for R:
				if(tL0 <= tR0) {
					return this.shapeL.intersection(ray, tMinimum, tMaximum);
				}
				
//				No secondary intersection for L and nothing to subtract from:
				if(isNaN(tL1)) {
					return SurfaceIntersection3D.EMPTY;
				}
				
//				No secondary intersection for R:
				if(isNaN(tR1)) {
					return this.shapeR.intersection(ray, tMinimum, tMaximum);
				}
				
				if(tR1 <= tL0) {
					return this.shapeL.intersection(ray, tMinimum, tMaximum);
				}
				
//				The secondary intersection for R is farther away than or equal to the secondary intersection for L:
				if(tR1 >= tL1) {
					return SurfaceIntersection3D.EMPTY;
				}
				
				return this.shapeR.intersection(ray, tR0 + 0.001D, tMaximum);
			}
			case INTERSECTION: {
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionL = this.shapeL.intersection(ray, tMinimum, tMaximum);
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionR = this.shapeR.intersection(ray, tMinimum, tMaximum);
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = optionalSurfaceIntersectionL.isPresent() && optionalSurfaceIntersectionR.isPresent() ? SurfaceIntersection3D.closest(optionalSurfaceIntersectionL, optionalSurfaceIntersectionR) : SurfaceIntersection3D.EMPTY;
				
				return optionalSurfaceIntersection;
			}
			case UNION: {
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionL = this.shapeL.intersection(ray, tMinimum, tMaximum);
				final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionR = this.shapeR.intersection(ray, tMinimum, tMaximum);
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
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstructiveSolidGeometry3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstructiveSolidGeometry3D(%s, %s, %s)", this.operation, this.shapeL, this.shapeR);
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
	 * Compares {@code object} to this {@code ConstructiveSolidGeometry3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstructiveSolidGeometry3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstructiveSolidGeometry3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstructiveSolidGeometry3D)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, ConstructiveSolidGeometry3D.class.cast(object).boundingVolume)) {
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
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		switch(this.operation) {
			case DIFFERENCE: {
				if(this.shapeL.getBoundingVolume().contains(ray.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return Double.NaN;
				}
				
				if(this.shapeR.getBoundingVolume().contains(ray.getOrigin())) {
//					TODO: If the BoundingVolume3D contains the Ray3D, that should probably be handled in a different way.
					return Double.NaN;
				}
				
				final double[] tIntervalL = doFindTInterval(ray, tMinimum, tMaximum, this.shapeL);
				final double[] tIntervalR = doFindTInterval(ray, tMinimum, tMaximum, this.shapeR);
				
				final double tL0 = tIntervalL[0];
				final double tL1 = tIntervalL[1];
				final double tR0 = tIntervalR[0];
				final double tR1 = tIntervalR[1];
				
				if(isNaN(tL0)) {
					return Double.NaN;
				}
				
				if(isNaN(tR0)) {
					return tL0;
				}
				
				if(tL0 <= tR0) {
					return tL0;
				}
				
				if(isNaN(tL1)) {
					return Double.NaN;
				}
				
				if(isNaN(tR1)) {
					return tR0;
				}
				
				if(tR1 <= tL0) {
					return tL0;
				}
				
				if(tR1 >= tL1) {
					return Double.NaN;
				}
				
				return tR1;
			}
			case INTERSECTION: {
				final double tL = this.shapeL.intersectionT(ray, tMinimum, tMaximum);
				final double tR = this.shapeR.intersectionT(ray, tMinimum, tMaximum);
				final double t = !isNaN(tL) && !isNaN(tR) ? min(tL, tR) : Double.NaN;
				
				return t;
			}
			case UNION: {
				final double tL = this.shapeL.intersectionT(ray, tMinimum, tMaximum);
				final double tR = this.shapeR.intersectionT(ray, tMinimum, tMaximum);
				final double t = minOrNaN(tL, tR);
				
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
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code ConstructiveSolidGeometry3D} instance.
	 * 
	 * @return a hash code for this {@code ConstructiveSolidGeometry3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.operation, this.shapeL, this.shapeR);
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
	
	private static double[] doFindTInterval(final Ray3D ray, final double tMinimum, final double tMaximum, final Shape3D shape) {
		final double[] tInterval = new double[] {Double.NaN, Double.NaN};
		
		double currentTMinimum = tMinimum;
		double currentTMaximum = tMaximum;
		
		for(int i = 0; i < tInterval.length; i++) {
			if(isNaN(tInterval[i])) {
				final double t = shape.intersectionT(ray, currentTMinimum, currentTMaximum);
				
				if(isNaN(t)) {
					return tInterval;
				}
				
				tInterval[i] = t;
				
				currentTMinimum = t + 0.001D;
			}
		}
		
		return tInterval;
	}
}