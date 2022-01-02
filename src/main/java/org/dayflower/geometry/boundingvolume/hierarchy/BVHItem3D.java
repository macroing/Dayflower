/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.geometry.boundingvolume.hierarchy;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.Shape3D;

/**
 * A {@code BVHItem3D} stores a {@link BoundingVolume3D} instance and a {@link Shape3D} instance for processing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BVHItem3D<T extends Shape3D> {
	private final BoundingVolume3D boundingVolume;
	private final T shape;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BVHItem3D} instance.
	 * <p>
	 * If either {@code boundingVolume} or {@code shape} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume a {@link BoundingVolume3D} instance
	 * @param shape a {@link Shape3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code boundingVolume} or {@code shape} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public BVHItem3D(final BoundingVolume3D boundingVolume, final T shape) {
		this.boundingVolume = Objects.requireNonNull(boundingVolume, "boundingVolume == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link BoundingVolume3D} instance associated with this {@code BVHItem3D} instance.
	 * 
	 * @return the {@code BoundingVolume3D} instance associated with this {@code BVHItem3D} instance
	 */
//	TODO: Add Unit Tests!
	public BoundingVolume3D getBoundingVolume() {
		return this.boundingVolume;
	}
	
	/**
	 * Returns the {@link Shape3D} instance associated with this {@code BVHItem3D} instance.
	 * 
	 * @return the {@code Shape3D} instance associated with this {@code BVHItem3D} instance
	 */
//	TODO: Add Unit Tests!
	public T getShape() {
		return this.shape;
	}
	
	/**
	 * Compares {@code object} to this {@code BVHItem3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BVHItem3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BVHItem3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BVHItem3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BVHItem3D)) {
			return false;
		} else if(!Objects.equals(this.boundingVolume, BVHItem3D.class.cast(object).boundingVolume)) {
			return false;
		} else if(!Objects.equals(this.shape, BVHItem3D.class.cast(object).shape)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code BVHItem3D} instance.
	 * 
	 * @return a hash code for this {@code BVHItem3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.boundingVolume, this.shape);
	}
}