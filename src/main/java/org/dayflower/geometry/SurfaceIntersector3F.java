/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.macroing.java.lang.Floats;

/**
 * A {@code SurfaceIntersector3F} is an utility class that is useful for performing intersection tests on {@link Shape3F} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceIntersector3F {
	/**
	 * The default minimum parametric {@code t} value.
	 */
//	TODO: Add Unit Tests!
	public static final float T_MAXIMUM = Floats.MAX_VALUE;
	
	/**
	 * The default maximum parametric {@code t} value.
	 */
//	TODO: Add Unit Tests!
	public static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Ray3F ray;
	private Shape3F shape;
	private float t;
	private float tMaximum;
	private float tMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceIntersector3F} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SurfaceIntersector3F(ray, SurfaceIntersector3F.T_MINIMUM, SurfaceIntersector3F.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersector3F(final Ray3F ray) {
		this(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Constructs a new {@code SurfaceIntersector3F} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersector3F(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.shape = null;
		this.t = Float.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the optional {@link Shape3F} of the current intersection.
	 * 
	 * @return the optional {@code Shape3F} of the current intersection
	 */
//	TODO: Add Unit Tests!
	public Optional<Shape3F> getShape() {
		return Optional.ofNullable(this.shape);
	}
	
	/**
	 * Computes a {@link SurfaceIntersection3F} for the current intersection.
	 * <p>
	 * Returns an optional {@code SurfaceIntersection3F} instance.
	 * 
	 * @return an optional {@code SurfaceIntersection3F} instance
	 */
//	TODO: Add Unit Tests!
	public Optional<SurfaceIntersection3F> computeSurfaceIntersection() {
		return isIntersecting() ? this.shape.intersection(this.ray, this.tMinimum, T_MAXIMUM) : SurfaceIntersection3F.EMPTY;
	}
	
	/**
	 * Returns the {@link Ray3F} instance associated with this {@code SurfaceIntersector3F} instance.
	 * 
	 * @return the {@code Ray3F} instance associated with this {@code SurfaceIntersector3F} instance
	 */
//	TODO: Add Unit Tests!
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceIntersector3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceIntersector3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new SurfaceIntersector3F(%s, %+.10f, %+.10f)", this.ray, Float.valueOf(this.tMinimum), Float.valueOf(this.tMaximum));
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceIntersector3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersector3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceIntersector3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersector3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceIntersector3F)) {
			return false;
		} else if(!Objects.equals(this.ray, SurfaceIntersector3F.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.shape, SurfaceIntersector3F.class.cast(object).shape)) {
			return false;
		} else if(!Floats.equals(this.t, SurfaceIntersector3F.class.cast(object).t)) {
			return false;
		} else if(!Floats.equals(this.tMaximum, SurfaceIntersector3F.class.cast(object).tMaximum)) {
			return false;
		} else if(!Floats.equals(this.tMinimum, SurfaceIntersector3F.class.cast(object).tMinimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code shape} and this {@code SurfaceIntersector3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code shape} intersects this {@code SurfaceIntersector3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code shape} intersects this {@code SurfaceIntersector3F} instance, its state will get updated.
	 * 
	 * @param shape a {@link Shape3F} instance
	 * @return {@code true} if, and only if, {@code shape} intersects this {@code SurfaceIntersector3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean intersection(final Shape3F shape) {
		final float t = shape.intersectionT(this.ray, this.tMinimum, this.tMaximum);
		
		if(!Floats.isNaN(t) && (Floats.isNaN(this.t) || t < this.t) && t > this.tMinimum && t < this.tMaximum) {
			this.t = t;
			this.tMaximum = t;
			this.shape = shape;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code SurfaceIntersector3F} has intersected with a {@link Shape3F} instance, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code SurfaceIntersector3F} has intersected with a {@code Shape3F} instance, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isIntersecting() {
		return this.shape != null && !Floats.isNaN(this.t);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code boundingVolume} intersects this {@code SurfaceIntersector3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code boundingVolume} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume a {@link BoundingVolume3F} instance
	 * @return {@code true} if, and only if, {@code boundingVolume} intersects this {@code SurfaceIntersector3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public boolean isIntersecting(final BoundingVolume3F boundingVolume) {
		return boundingVolume.contains(this.ray.getOrigin()) || boundingVolume.intersects(this.ray, this.tMinimum, this.tMaximum);
	}
	
	/**
	 * Returns the parametric {@code t} value that represents the distance to the intersection.
	 * 
	 * @return the parametric {@code t} value that represents the distance to the intersection
	 */
//	TODO: Add Unit Tests!
	public float getT() {
		return this.t;
	}
	
	/**
	 * Returns the maximum parametric {@code t} value.
	 * 
	 * @return the maximum parametric {@code t} value
	 */
//	TODO: Add Unit Tests!
	public float getTMaximum() {
		return this.tMaximum;
	}
	
	/**
	 * Returns the minimum parametric {@code t} value.
	 * 
	 * @return the minimum parametric {@code t} value
	 */
//	TODO: Add Unit Tests!
	public float getTMinimum() {
		return this.tMinimum;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceIntersector3F} instance.
	 * 
	 * @return a hash code for this {@code SurfaceIntersector3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.ray, this.shape, Float.valueOf(this.t), Float.valueOf(this.tMaximum), Float.valueOf(this.tMinimum));
	}
	
	/**
	 * Initializes this {@code SurfaceIntersector3F} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersector.initialize(ray, SurfaceIntersector3F.T_MINIMUM, SurfaceIntersector3F.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void initialize(final Ray3F ray) {
		initialize(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Initializes this {@code SurfaceIntersector3F} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.t = Float.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	/**
	 * Transforms this {@code SurfaceIntersector3F} instance with {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@link Matrix44F} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void transform(final Matrix44F matrix) {
		final Ray3F rayOldSpace = this.ray;
		final Ray3F rayNewSpace = Ray3F.transform(matrix, rayOldSpace);
		
		final float tOldSpace = this.t;
		final float tNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tOldSpace);
		
		final float tMaximumOldSpace = this.tMaximum;
		final float tMaximumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMaximumOldSpace);
		
		final float tMinimumOldSpace = this.tMinimum;
		final float tMinimumNewSpace = tMinimumOldSpace;
//		final float tMinimumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMinimumOldSpace);
		
		this.ray = rayNewSpace;
		this.t = tNewSpace;
		this.tMaximum = tMaximumNewSpace;
		this.tMinimum = tMinimumNewSpace;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !Floats.isNaN(t) && !Floats.isZero(t) && t < Floats.MAX_VALUE ? Floats.abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transformAndDivide(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
}