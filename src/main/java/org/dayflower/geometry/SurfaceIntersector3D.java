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
package org.dayflower.geometry;

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code SurfaceIntersector3D} is an utility class that is useful for performing intersection tests on {@link Shape3D} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceIntersector3D {
	/**
	 * The default minimum parametric {@code t} value.
	 */
//	TODO: Add Unit Tests!
	public static final double T_MAXIMUM = MAX_VALUE;
	
	/**
	 * The default maximum parametric {@code t} value.
	 */
//	TODO: Add Unit Tests!
	public static final double T_MINIMUM = 0.001D;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Ray3D ray;
	private Shape3D shape;
	private double t;
	private double tMaximum;
	private double tMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceIntersector3D} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SurfaceIntersector3D(ray, SurfaceIntersector3D.T_MINIMUM, SurfaceIntersector3D.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersector3D(final Ray3D ray) {
		this(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Constructs a new {@code SurfaceIntersector3D} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3D} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersector3D(final Ray3D ray, final double tMinimum, final double tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.shape = null;
		this.t = Double.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the optional {@link Shape3D} of the current intersection.
	 * 
	 * @return the optional {@code Shape3D} of the current intersection
	 */
//	TODO: Add Unit Tests!
	public Optional<Shape3D> getShape() {
		return Optional.ofNullable(this.shape);
	}
	
	/**
	 * Computes a {@link SurfaceIntersection3D} for the current intersection.
	 * <p>
	 * Returns an optional {@code SurfaceIntersection3D} instance.
	 * 
	 * @return an optional {@code SurfaceIntersection3D} instance
	 */
//	TODO: Add Unit Tests!
	public Optional<SurfaceIntersection3D> computeSurfaceIntersection() {
		return isIntersecting() ? this.shape.intersection(this.ray, this.tMinimum, T_MAXIMUM) : SurfaceIntersection3D.EMPTY;
	}
	
	/**
	 * Returns the {@link Ray3D} instance associated with this {@code SurfaceIntersector3D} instance.
	 * 
	 * @return the {@code Ray3D} instance associated with this {@code SurfaceIntersector3D} instance
	 */
//	TODO: Add Unit Tests!
	public Ray3D getRay() {
		return this.ray;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceIntersector3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceIntersector3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new SurfaceIntersector3D(%s, %+.10f, %+.10f)", this.ray, Double.valueOf(this.tMinimum), Double.valueOf(this.tMaximum));
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceIntersector3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersector3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceIntersector3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersector3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceIntersector3D)) {
			return false;
		} else if(!Objects.equals(this.ray, SurfaceIntersector3D.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.shape, SurfaceIntersector3D.class.cast(object).shape)) {
			return false;
		} else if(!equal(this.t, SurfaceIntersector3D.class.cast(object).t)) {
			return false;
		} else if(!equal(this.tMaximum, SurfaceIntersector3D.class.cast(object).tMaximum)) {
			return false;
		} else if(!equal(this.tMinimum, SurfaceIntersector3D.class.cast(object).tMinimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code shape} and this {@code SurfaceIntersector3D} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code shape} intersects this {@code SurfaceIntersector3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code shape} intersects this {@code SurfaceIntersector3D} instance, its state will get updated.
	 * 
	 * @param shape a {@link Shape3D} instance
	 * @return {@code true} if, and only if, {@code shape} intersects this {@code SurfaceIntersector3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean intersection(final Shape3D shape) {
		final double t = shape.intersectionT(this.ray, this.tMinimum, this.tMaximum);
		
		if(!isNaN(t) && (isNaN(this.t) || t < this.t) && t > this.tMinimum && t < this.tMaximum) {
			this.t = t;
			this.tMaximum = t;
			this.shape = shape;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code SurfaceIntersector3D} has intersected with a {@link Shape3D} instance, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code SurfaceIntersector3D} has intersected with a {@code Shape3D} instance, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isIntersecting() {
		return this.shape != null && !isNaN(this.t);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code boundingVolume} intersects this {@code SurfaceIntersector3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code boundingVolume} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume a {@link BoundingVolume3D} instance
	 * @return {@code true} if, and only if, {@code boundingVolume} intersects this {@code SurfaceIntersector3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean isIntersecting(final BoundingVolume3D boundingVolume) {
		return boundingVolume.contains(this.ray.getOrigin()) || boundingVolume.intersects(this.ray, this.tMinimum, this.tMaximum);
	}
	
	/**
	 * Returns the parametric {@code t} value that represents the distance to the intersection.
	 * 
	 * @return the parametric {@code t} value that represents the distance to the intersection
	 */
//	TODO: Add Unit Tests!
	public double getT() {
		return this.t;
	}
	
	/**
	 * Returns the maximum parametric {@code t} value.
	 * 
	 * @return the maximum parametric {@code t} value
	 */
//	TODO: Add Unit Tests!
	public double getTMaximum() {
		return this.tMaximum;
	}
	
	/**
	 * Returns the minimum parametric {@code t} value.
	 * 
	 * @return the minimum parametric {@code t} value
	 */
//	TODO: Add Unit Tests!
	public double getTMinimum() {
		return this.tMinimum;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceIntersector3D} instance.
	 * 
	 * @return a hash code for this {@code SurfaceIntersector3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.ray, this.shape, Double.valueOf(this.t), Double.valueOf(this.tMaximum), Double.valueOf(this.tMinimum));
	}
	
	/**
	 * Initializes this {@code SurfaceIntersector3D} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersector.initialize(ray, SurfaceIntersector3D.T_MINIMUM, SurfaceIntersector3D.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void initialize(final Ray3D ray) {
		initialize(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Initializes this {@code SurfaceIntersector3D} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3D} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void initialize(final Ray3D ray, final double tMinimum, final double tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.t = Double.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	/**
	 * Transforms this {@code SurfaceIntersector3D} instance with {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@link Matrix44D} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void transform(final Matrix44D matrix) {
		final Ray3D rayOldSpace = this.ray;
		final Ray3D rayNewSpace = Ray3D.transform(matrix, rayOldSpace);
		
		final double tOldSpace = this.t;
		final double tNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tOldSpace);
		
		final double tMaximumOldSpace = this.tMaximum;
		final double tMaximumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMaximumOldSpace);
		
		final double tMinimumOldSpace = this.tMinimum;
		final double tMinimumNewSpace = tMinimumOldSpace;
//		final double tMinimumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMinimumOldSpace);
		
		this.ray = rayNewSpace;
		this.t = tNewSpace;
		this.tMaximum = tMaximumNewSpace;
		this.tMinimum = tMinimumNewSpace;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doTransformT(final Matrix44D matrix, final Ray3D rayOldSpace, final Ray3D rayNewSpace, final double t) {
		return !isNaN(t) && !isZero(t) && t < MAX_VALUE ? abs(Point3D.distance(rayNewSpace.getOrigin(), Point3D.transformAndDivide(matrix, Point3D.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
}