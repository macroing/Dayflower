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
package org.dayflower.scene;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.SurfaceSample3F;

/**
 * An {@code Sample} denotes a sample from a {@link Primitive} instance.
 * <p>
 * This class is indirectly mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Sample {
	/**
	 * An empty {@code Optional} instance.
	 */
	public static final Optional<Sample> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Primitive primitive;
	private final SurfaceSample3F surfaceSampleObjectSpace;
	private final SurfaceSample3F surfaceSampleWorldSpace;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Sample} instance.
	 * <p>
	 * If either {@code primitive} or {@code surfaceSampleObjectSpace} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance associated with this {@code Sample} instance
	 * @param surfaceSampleObjectSpace the {@link SurfaceSample3F} instance associated with this {@code Sample} instance in object space
	 * @throws NullPointerException thrown if, and only if, either {@code primitive} or {@code surfaceSampleObjectSpace} are {@code null}
	 */
	public Sample(final Primitive primitive, final SurfaceSample3F surfaceSampleObjectSpace) {
		this.primitive = Objects.requireNonNull(primitive, "primitive == null");
		this.surfaceSampleObjectSpace = Objects.requireNonNull(surfaceSampleObjectSpace, "surfaceSampleObjectSpace");
		this.surfaceSampleWorldSpace = SurfaceSample3F.transform(surfaceSampleObjectSpace, primitive.getTransform().getObjectToWorld(), primitive.getTransform().getWorldToObject());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Primitive} instance associated with this {@code Sample} instance.
	 * 
	 * @return the {@code Primitive} instance associated with this {@code Sample} instance
	 */
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Sample} instance.
	 * 
	 * @return a {@code String} representation of this {@code Sample} instance
	 */
	@Override
	public String toString() {
		return String.format("new Sample(%s, %s)", this.primitive, this.surfaceSampleObjectSpace);
	}
	
	/**
	 * Returns the {@link SurfaceSample3F} instance associated with this {@code Sample} instance in object space.
	 * 
	 * @return the {@code SurfaceSample3F} instance associated with this {@code Sample} instance in object space
	 */
	public SurfaceSample3F getSurfaceSampleObjectSpace() {
		return this.surfaceSampleObjectSpace;
	}
	
	/**
	 * Returns the {@link SurfaceSample3F} instance associated with this {@code Sample} instance in world space.
	 * 
	 * @return the {@code SurfaceSample3F} instance associated with this {@code Sample} instance in world space
	 */
	public SurfaceSample3F getSurfaceSampleWorldSpace() {
		return this.surfaceSampleWorldSpace;
	}
	
	/**
	 * Compares {@code object} to this {@code Sample} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Sample}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Sample} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Sample}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Sample)) {
			return false;
		} else if(!Objects.equals(this.primitive, Sample.class.cast(object).primitive)) {
			return false;
		} else if(!Objects.equals(this.surfaceSampleObjectSpace, Sample.class.cast(object).surfaceSampleObjectSpace)) {
			return false;
		} else if(!Objects.equals(this.surfaceSampleWorldSpace, Sample.class.cast(object).surfaceSampleWorldSpace)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code Sample} instance.
	 * 
	 * @return a hash code for this {@code Sample} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive, this.surfaceSampleObjectSpace, this.surfaceSampleWorldSpace);
	}
}