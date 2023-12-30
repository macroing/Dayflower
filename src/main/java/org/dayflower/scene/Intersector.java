/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceIntersector3F;

import org.macroing.java.lang.Floats;

/**
 * An {@code Intersector} is an utility useful for performing intersection tests.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Intersector {
	/**
	 * The default minimum parametric {@code t} value.
	 */
	public static final float T_MAXIMUM = Floats.MAX_VALUE;
	
	/**
	 * The default maximum parametric {@code t} value.
	 */
	public static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Primitive primitive;
	private SurfaceIntersector3F surfaceIntersector;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Intersector} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Intersector(ray, Intersector.T_MINIMUM, Intersector.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Intersector(final Ray3F ray) {
		this(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Constructs a new {@code Intersector} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Intersector(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.primitive = null;
		this.surfaceIntersector = new SurfaceIntersector3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Computes an {@link Intersection} for the current intersection.
	 * <p>
	 * Returns an optional {@code Intersection} instance.
	 * 
	 * @return an optional {@code Intersection} instance
	 */
	public Optional<Intersection> computeIntersection() {
		final Primitive primitive = this.primitive;
		
		if(primitive != null) {
			final Transform transform = primitive.getTransform();
			
			final Matrix44F worldToObject = transform.getWorldToObject();
			
			final Ray3F rayWorldSpace = this.surfaceIntersector.getRay();
			final Ray3F rayObjectSpace = Ray3F.transform(worldToObject, rayWorldSpace);
			
			final Optional<Shape3F> optionalShape = this.surfaceIntersector.getShape();
			
			if(optionalShape.isPresent()) {
				final Shape3F shape = optionalShape.get();
				
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionObjectSpace = shape.intersection(rayObjectSpace, T_MINIMUM, T_MAXIMUM);
				
				if(optionalSurfaceIntersectionObjectSpace.isPresent()) {
					final SurfaceIntersection3F surfaceIntersectionObjectSpace = optionalSurfaceIntersectionObjectSpace.get();
					
					final Intersection intersection = new Intersection(primitive, surfaceIntersectionObjectSpace);
					
					return Optional.of(intersection);
				}
			}
		}
		
		return Intersection.EMPTY;
	}
	
	/**
	 * Returns the optional {@link Primitive} of the current intersection.
	 * 
	 * @return the optional {@code Primitive} of the current intersection
	 */
	public Optional<Primitive> getPrimitive() {
		return Optional.ofNullable(this.primitive);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Intersector} instance.
	 * 
	 * @return a {@code String} representation of this {@code Intersector} instance
	 */
	@Override
	public String toString() {
		return String.format("new Intersector(%s, %+.10f, %+.10f)", this.surfaceIntersector.getRay(), Float.valueOf(this.surfaceIntersector.getTMinimum()), Float.valueOf(this.surfaceIntersector.getTMaximum()));
	}
	
	/**
	 * Returns the {@link SurfaceIntersector3F} instance that is associated with this {@code Intersector} instance.
	 * 
	 * @return the {@code SurfaceIntersector3F} instance that is associated with this {@code Intersector} instance
	 */
	public SurfaceIntersector3F getSurfaceIntersector() {
		return this.surfaceIntersector;
	}
	
	/**
	 * Compares {@code object} to this {@code Intersector} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Intersector}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Intersector} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Intersector}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Intersector)) {
			return false;
		} else if(!Objects.equals(this.primitive, Intersector.class.cast(object).primitive)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersector, Intersector.class.cast(object).surfaceIntersector)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code primitive} and this {@code Intersector} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} intersects this {@code Intersector} instance, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive} intersects this {@code Intersector} instance, its state will get updated.
	 * 
	 * @param primitive a {@link Primitive} instance
	 * @return {@code true} if, and only if, {@code primitive} intersects this {@code Intersector} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean intersection(final Primitive primitive) {
		boolean isIntersecting = false;
		
		if(this.surfaceIntersector.isIntersecting(primitive.getBoundingVolume())) {
			final Transform transform = primitive.getTransform();
			
			this.surfaceIntersector.transform(transform.getWorldToObject());
			
			if(primitive.getShape().intersection(this.surfaceIntersector)) {
				this.primitive = primitive;
				
				isIntersecting = true;
			}
			
			this.surfaceIntersector.transform(transform.getObjectToWorld());
		}
		
		return isIntersecting;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Intersector} instance contains an intersection, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Intersector} instance contains an intersection, {@code false} otherwise
	 */
	public boolean isIntersecting() {
		return this.primitive != null;
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code boundingVolume} intersects this {@code Intersector} instance, {@code false} otherwise.
	 * <p>
	 * If {@code boundingVolume} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume a {@link BoundingVolume3F} instance
	 * @return {@code true} if, and only if, {@code boundingVolume} intersects this {@code Intersector} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume} is {@code null}
	 */
	public boolean isIntersecting(final BoundingVolume3F boundingVolume) {
		return this.surfaceIntersector.isIntersecting(boundingVolume);
	}
	
	/**
	 * Returns a hash code for this {@code Intersector} instance.
	 * 
	 * @return a hash code for this {@code Intersector} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive, this.surfaceIntersector);
	}
	
	/**
	 * Initializes this {@code Intersector} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * intersector.initialize(ray, Intersector.T_MINIMUM, Intersector.T_MAXIMUM);
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
	 * Initializes this {@code Intersector} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.primitive = null;
		this.surfaceIntersector = new SurfaceIntersector3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
	}
}