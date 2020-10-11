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
package org.dayflower.scene;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.MutableSurfaceIntersection3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;

/**
 * A {@code MutableIntersection} is an utility useful for performing intersection tests.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MutableIntersection {
	/**
	 * The default minimum parametric {@code t} value.
	 */
	public static final float T_MAXIMUM = Float.MAX_VALUE;
	
	/**
	 * The default maximum parametric {@code t} value.
	 */
	public static final float T_MINIMUM = 0.0001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private MutableSurfaceIntersection3F mutableSurfaceIntersection;
	private Primitive primitive;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MutableIntersection} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MutableIntersection(ray, MutableIntersection.T_MINIMUM, MutableIntersection.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public MutableIntersection(final Ray3F ray) {
		this(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Constructs a new {@code MutableIntersection} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public MutableIntersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.mutableSurfaceIntersection = new MutableSurfaceIntersection3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
		this.primitive = null;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link MutableSurfaceIntersection3F} instance that is associated with this {@code MutableIntersection} instance.
	 * 
	 * @return the {@code MutableSurfaceIntersection3F} instance that is associated with this {@code MutableIntersection} instance
	 */
	public MutableSurfaceIntersection3F getMutableSurfaceIntersection() {
		return this.mutableSurfaceIntersection;
	}
	
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
			final Matrix44F worldToObject = primitive.getWorldToObject();
			
			final Ray3F rayWorldSpace = this.mutableSurfaceIntersection.getRay();
			final Ray3F rayObjectSpace = Ray3F.transform(worldToObject, rayWorldSpace);
			
			final Optional<Shape3F> optionalShape = this.mutableSurfaceIntersection.getShape();
			
			if(optionalShape.isPresent()) {
				final Shape3F shape = optionalShape.get();
				
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionObjectSpace = shape.intersection(rayObjectSpace);
				
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
	 * Returns a {@code String} representation of this {@code MutableIntersection} instance.
	 * 
	 * @return a {@code String} representation of this {@code MutableIntersection} instance
	 */
	@Override
	public String toString() {
		return String.format("new MutableIntersection(%s, %+.10f, %+.10f)", this.mutableSurfaceIntersection.getRay(), Float.valueOf(this.mutableSurfaceIntersection.getTMinimum()), Float.valueOf(this.mutableSurfaceIntersection.getTMaximum()));
	}
	
	/**
	 * Compares {@code object} to this {@code MutableIntersection} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MutableIntersection}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MutableIntersection} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MutableIntersection}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MutableIntersection)) {
			return false;
		} else if(!Objects.equals(this.mutableSurfaceIntersection, MutableIntersection.class.cast(object).mutableSurfaceIntersection)) {
			return false;
		} else if(!Objects.equals(this.primitive, MutableIntersection.class.cast(object).primitive)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code primitive} and this {@code MutableIntersection} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} intersects this {@code MutableIntersection} instance, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive} intersects this {@code MutableIntersection} instance, its state will get updated.
	 * 
	 * @param primitive a {@link Primitive} instance
	 * @return {@code true} if, and only if, {@code primitive} intersects this {@code MutableIntersection} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean intersection(final Primitive primitive) {
		boolean isIntersecting = false;
		
		if(this.mutableSurfaceIntersection.isIntersecting(primitive.getBoundingVolume())) {
			this.mutableSurfaceIntersection.transform(primitive.getWorldToObject());
			
			if(primitive.getShape().intersection(this.mutableSurfaceIntersection)) {
				this.primitive = primitive;
				
				isIntersecting = true;
			}
			
			this.mutableSurfaceIntersection.transform(primitive.getObjectToWorld());
		}
		
		return isIntersecting;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code MutableIntersection} instance contains an intersection, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code MutableIntersection} instance contains an intersection, {@code false} otherwise
	 */
	public boolean isIntersecting() {
		return this.primitive != null;
	}
	
	/**
	 * Returns a hash code for this {@code MutableIntersection} instance.
	 * 
	 * @return a hash code for this {@code MutableIntersection} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.mutableSurfaceIntersection, this.primitive);
	}
	
	/**
	 * Initializes this {@code MutableIntersection} instance given {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * mutableIntersection.initialize(ray, MutableIntersection.T_MINIMUM, MutableIntersection.T_MAXIMUM);
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
	 * Initializes this {@code MutableIntersection} instance given {@code ray}, {@code tMinimum} and {@code tMaximum}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @param tMinimum the minimum parametric {@code t} value
	 * @param tMaximum the maximum parametric {@code t} value
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.mutableSurfaceIntersection = new MutableSurfaceIntersection3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
		this.primitive = null;
	}
}