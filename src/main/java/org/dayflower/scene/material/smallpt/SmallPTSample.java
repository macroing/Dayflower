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
package org.dayflower.scene.material.smallpt;

import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code SmallPTSample} represents a sample produced by a {@link SmallPTMaterial} instance.
 * <p>
 * This class is immutable and thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SmallPTSample {
	private final Color3F result;
	private final Vector3F direction;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SmallPTSample} instance.
	 * <p>
	 * If either {@code result} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result associated with this {@code SmallPTSample} instance
	 * @param direction a {@link Vector3F} instance with the direction associated with this {@code SmallPTSample} instance
	 * @throws NullPointerException thrown if, and only if, either {@code result} or {@code direction} are {@code null}
	 */
	public SmallPTSample(final Color3F result, final Vector3F direction) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.direction = Objects.requireNonNull(direction, "direction == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result associated with this {@code SmallPTSample} instance.
	 * 
	 * @return a {@code Color3F} instance with the result associated with this {@code SmallPTSample} instance
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SmallPTSample} instance.
	 * 
	 * @return a {@code String} representation of this {@code SmallPTSample} instance
	 */
	@Override
	public String toString() {
		return String.format("new SmallPTSample(%s, %s)", this.result, this.direction);
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the direction associated with this {@code SmallPTSample} instance.
	 * 
	 * @return a {@code Vector3F} instance with the direction associated with this {@code SmallPTSample} instance
	 */
	public Vector3F getDirection() {
		return this.direction;
	}
	
	/**
	 * Compares {@code object} to this {@code SmallPTSample} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SmallPTSample}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SmallPTSample} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SmallPTSample}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SmallPTSample)) {
			return false;
		} else if(!Objects.equals(this.result, SmallPTSample.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.direction, SmallPTSample.class.cast(object).direction)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code SmallPTSample} instance.
	 * 
	 * @return a hash code for this {@code SmallPTSample} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.direction);
	}
}