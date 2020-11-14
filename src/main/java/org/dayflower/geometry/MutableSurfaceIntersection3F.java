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
package org.dayflower.geometry;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@code MutableSurfaceIntersection3F} is an utility useful for performing intersection tests.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MutableSurfaceIntersection3F {
	/**
	 * The default minimum parametric {@code t} value.
	 */
	public static final float T_MAXIMUM = Float.MAX_VALUE;
	
	/**
	 * The default maximum parametric {@code t} value.
	 */
	public static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Ray3F ray;
	private Shape3F shape;
	private float t;
	private float tMaximum;
	private float tMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MutableSurfaceIntersection3F} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MutableSurfaceIntersection3F(ray, MutableSurfaceIntersection3F.T_MINIMUM, MutableSurfaceIntersection3F.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public MutableSurfaceIntersection3F(final Ray3F ray) {
		this(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Constructs a new {@code MutableSurfaceIntersection3F} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public MutableSurfaceIntersection3F(final Ray3F ray, final float tMinimum, final float tMaximum) {
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
	public Optional<SurfaceIntersection3F> computeSurfaceIntersection() {
		return isIntersecting() ? this.shape.intersection(this.ray, this.tMinimum, T_MAXIMUM) : SurfaceIntersection3F.EMPTY;
	}
	
	/**
	 * Returns the {@link Ray3F} instance associated with this {@code MutableSurfaceIntersection3F} instance.
	 * 
	 * @return the {@code Ray3F} instance associated with this {@code MutableSurfaceIntersection3F} instance
	 */
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code MutableSurfaceIntersection3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code MutableSurfaceIntersection3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new MutableSurfaceIntersection3F(%s, %+.10f, %+.10f)", this.ray, Float.valueOf(this.tMinimum), Float.valueOf(this.tMaximum));
	}
	
	/**
	 * Compares {@code object} to this {@code MutableSurfaceIntersection3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MutableSurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MutableSurfaceIntersection3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MutableSurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MutableSurfaceIntersection3F)) {
			return false;
		} else if(!Objects.equals(this.ray, MutableSurfaceIntersection3F.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.shape, MutableSurfaceIntersection3F.class.cast(object).shape)) {
			return false;
		} else if(!equal(this.t, MutableSurfaceIntersection3F.class.cast(object).t)) {
			return false;
		} else if(!equal(this.tMaximum, MutableSurfaceIntersection3F.class.cast(object).tMaximum)) {
			return false;
		} else if(!equal(this.tMinimum, MutableSurfaceIntersection3F.class.cast(object).tMinimum)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code shape} and this {@code MutableSurfaceIntersection3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code shape} intersects this {@code MutableSurfaceIntersection3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code shape} intersects this {@code MutableSurfaceIntersection3F} instance, its state will get updated.
	 * 
	 * @param shape a {@link Shape3F} instance
	 * @return {@code true} if, and only if, {@code shape} intersects this {@code MutableSurfaceIntersection3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public boolean intersection(final Shape3F shape) {
		final float t = shape.intersectionT(this.ray, this.tMinimum, this.tMaximum);
		
		if(!isNaN(t) && (isNaN(this.t) || t < this.t) && t > this.tMinimum && t < this.tMaximum) {
			this.t = t;
			this.tMaximum = t;
			this.shape = shape;
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code MutableSurfaceIntersection3F} has intersected with a {@link Shape3F} instance, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code MutableSurfaceIntersection3F} has intersected with a {@code Shape3F} instance, {@code false} otherwise
	 */
	public boolean isIntersecting() {
		return this.shape != null && !isNaN(this.t);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code boundingVolume} intersects this {@code MutableSurfaceIntersection3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code boundingVolume} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume a {@link BoundingVolume3F} instance
	 * @return {@code true} if, and only if, {@code boundingVolume} intersects this {@code MutableSurfaceIntersection3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume} is {@code null}
	 */
	public boolean isIntersecting(final BoundingVolume3F boundingVolume) {
		return boundingVolume.intersects(this.ray, this.tMinimum, this.tMaximum);
	}
	
	/**
	 * Returns the parametric {@code t} value that represents the distance to the intersection.
	 * 
	 * @return the parametric {@code t} value that represents the distance to the intersection
	 */
	public float getT() {
		return this.t;
	}
	
	/**
	 * Returns the maximum parametric {@code t} value.
	 * 
	 * @return the maximum parametric {@code t} value
	 */
	public float getTMaximum() {
		return this.tMaximum;
	}
	
	/**
	 * Returns the minimum parametric {@code t} value.
	 * 
	 * @return the minimum parametric {@code t} value
	 */
	public float getTMinimum() {
		return this.tMinimum;
	}
	
	/**
	 * Returns a hash code for this {@code MutableSurfaceIntersection3F} instance.
	 * 
	 * @return a hash code for this {@code MutableSurfaceIntersection3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.ray, this.shape, Float.valueOf(this.t), Float.valueOf(this.tMaximum), Float.valueOf(this.tMinimum));
	}
	
	/**
	 * Initializes this {@code MutableSurfaceIntersection3F} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * mutableSurfaceIntersection.initialize(ray, MutableSurfaceIntersection3F.T_MINIMUM, MutableSurfaceIntersection3F.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public void initialize(final Ray3F ray) {
		initialize(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Initializes this {@code MutableSurfaceIntersection3F} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.t = Float.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	/**
	 * Transforms this {@code MutableSurfaceIntersection3F} instance with {@code matrix}.
	 * <p>
	 * If {@code matrix} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix a {@link Matrix44F} instance
	 * @throws NullPointerException thrown if, and only if, {@code matrix} is {@code null}
	 */
	public void transform(final Matrix44F matrix) {
		final Ray3F rayOldSpace = this.ray;
		final Ray3F rayNewSpace = Ray3F.transform(matrix, rayOldSpace);
		
		final float tOldSpace = this.t;
		final float tNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tOldSpace);
		
		final float tMaximumOldSpace = this.tMaximum;
		final float tMaximumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMaximumOldSpace);
		
		final float tMinimumOldSpace = this.tMinimum;
		final float tMinimumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMinimumOldSpace);
		
		this.ray = rayNewSpace;
		this.t = tNewSpace;
		this.tMaximum = tMaximumNewSpace;
		this.tMinimum = tMinimumNewSpace;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !equal(t, 0.0F) && t < Float.MAX_VALUE ? abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transformAndDivide(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
}