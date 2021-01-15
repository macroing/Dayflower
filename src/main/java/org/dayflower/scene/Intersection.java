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
package org.dayflower.scene;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * An {@code Intersection} denotes an intersection between a {@link Ray3F} instance and a {@link Primitive} instance.
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Intersection {
	/**
	 * An empty {@code Optional} instance.
	 */
	public static final Optional<Intersection> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Primitive primitive;
	private final SurfaceIntersection3F surfaceIntersectionObjectSpace;
	private final SurfaceIntersection3F surfaceIntersectionWorldSpace;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Intersection} instance.
	 * <p>
	 * If either {@code primitive} or {@code surfaceIntersectionObjectSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance associated with this {@code Intersection} instance
	 * @param surfaceIntersectionObjectSpace the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space
	 * @throws NullPointerException thrown if, and only if, either {@code primitive} or {@code surfaceIntersectionObjectSpace} are {@code null}
	 */
	public Intersection(final Primitive primitive, final SurfaceIntersection3F surfaceIntersectionObjectSpace) {
		this.primitive = Objects.requireNonNull(primitive, "primitive == null");
		this.surfaceIntersectionObjectSpace = Objects.requireNonNull(surfaceIntersectionObjectSpace, "surfaceIntersectionObjectSpace == null");
		this.surfaceIntersectionWorldSpace = SurfaceIntersection3F.transform(surfaceIntersectionObjectSpace, primitive.getTransform().getObjectToWorld(), primitive.getTransform().getWorldToObject());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the emitted radiance along {@code direction}.
	 * <p>
	 * Returns a {@link Color3F} instance with the emitted radiance.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction the direction
	 * @return a {@code Color3F} instance with the emitted radiance
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
	public Color3F evaluateRadianceEmitted(final Vector3F direction) {
		Objects.requireNonNull(direction, "direction == null");
		
		final Optional<AreaLight> optionalAreaLight = this.primitive.getAreaLight();
		
		if(optionalAreaLight.isPresent()) {
			final AreaLight areaLight = optionalAreaLight.get();
			
			return areaLight.evaluateRadiance(this, direction);
		}
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns the {@link Primitive} instance associated with this {@code Intersection} instance.
	 * 
	 * @return the {@code Primitive} instance associated with this {@code Intersection} instance
	 */
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Intersection} instance.
	 * 
	 * @return a {@code String} representation of this {@code Intersection} instance
	 */
	@Override
	public String toString() {
		return String.format("new Intersection(%s, %s)", this.primitive, this.surfaceIntersectionObjectSpace);
	}
	
	/**
	 * Returns the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space.
	 * 
	 * @return the {@code SurfaceIntersection3F} instance associated with this {@code Intersection} instance in object space
	 */
	public SurfaceIntersection3F getSurfaceIntersectionObjectSpace() {
		return this.surfaceIntersectionObjectSpace;
	}
	
	/**
	 * Returns the {@link SurfaceIntersection3F} instance associated with this {@code Intersection} instance in world space.
	 * 
	 * @return the {@code SurfaceIntersection3F} instance associated with this {@code Intersection} instance in world space
	 */
	public SurfaceIntersection3F getSurfaceIntersectionWorldSpace() {
		return this.surfaceIntersectionWorldSpace;
	}
	
	/**
	 * Compares {@code object} to this {@code Intersection} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Intersection}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Intersection} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Intersection}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Intersection)) {
			return false;
		} else if(!Objects.equals(this.primitive, Intersection.class.cast(object).primitive)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionObjectSpace, Intersection.class.cast(object).surfaceIntersectionObjectSpace)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionWorldSpace, Intersection.class.cast(object).surfaceIntersectionWorldSpace)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Intersection} instance.
	 * 
	 * @return a hash code for this {@code Intersection} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive, this.surfaceIntersectionObjectSpace, this.surfaceIntersectionWorldSpace);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the closest {@code Intersection} instance.
	 * <p>
	 * If either {@code optionalIntersectionA} or {@code optionalIntersectionB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param optionalIntersectionA an {@code Optional} with an optional {@code Intersection} instance
	 * @param optionalIntersectionB an {@code Optional} with an optional {@code Intersection} instance
	 * @return the closest {@code Intersection} instance
	 * @throws NullPointerException thrown if, and only if, either {@code optionalIntersectionA} or {@code optionalIntersectionB} are {@code null}
	 */
	public static Optional<Intersection> closest(final Optional<Intersection> optionalIntersectionA, final Optional<Intersection> optionalIntersectionB) {
		final Intersection intersectionA = optionalIntersectionA.orElse(null);
		final Intersection intersectionB = optionalIntersectionB.orElse(null);
		
		if(intersectionA != null && intersectionB != null) {
			return intersectionA.getSurfaceIntersectionWorldSpace().getT() < intersectionB.getSurfaceIntersectionWorldSpace().getT() ? optionalIntersectionA : optionalIntersectionB;
		} else if(intersectionA != null) {
			return optionalIntersectionA;
		} else if(intersectionB != null) {
			return optionalIntersectionB;
		} else {
			return EMPTY;
		}
	}
}