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

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;

/**
 * A {@code LightRadianceEmittedResult} is returned by a {@link Light} instance and contains the computed properties.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LightRadianceEmittedResult {
	private final Color3F result;
	private final Ray3F ray;
	private final Vector3F normal;
	private final float probabilityDensityFunctionValueDirection;
	private final float probabilityDensityFunctionValuePosition;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LightRadianceEmittedResult} instance.
	 * <p>
	 * If either {@code result}, {@code ray} or {@code normal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result associated with this {@code LightRadianceEmittedResult} instance
	 * @param ray the {@link Ray3F} instance associated with this {@code LightRadianceEmittedResult} instance
	 * @param normal a {@link Vector3F} instance with the normal associated with this {@code LightRadianceEmittedResult} instance
	 * @param probabilityDensityFunctionValueDirection the probability density function (PDF) value for the direction
	 * @param probabilityDensityFunctionValuePosition the probability density function (PDF) value for the position
	 * @throws NullPointerException thrown if, and only if, either {@code result}, {@code ray} or {@code normal} are {@code null}
	 */
	public LightRadianceEmittedResult(final Color3F result, final Ray3F ray, final Vector3F normal, final float probabilityDensityFunctionValueDirection, final float probabilityDensityFunctionValuePosition) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.normal = Objects.requireNonNull(normal, "normal == null");
		this.probabilityDensityFunctionValueDirection = probabilityDensityFunctionValueDirection;
		this.probabilityDensityFunctionValuePosition = probabilityDensityFunctionValuePosition;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result associated with this {@code LightRadianceEmittedResult} instance.
	 * 
	 * @return a {@code Color3F} instance with the result associated with this {@code LightRadianceEmittedResult} instance
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns the {@link Ray3F} instance associated with this {@code LightRadianceEmittedResult} instance.
	 * 
	 * @return the {@code Ray3F} instance associated with this {@code LightRadianceEmittedResult} instance
	 */
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LightRadianceEmittedResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code LightRadianceEmittedResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new LightRadianceEmittedResult(%s, %s, %s, %+.10f, %+.10f)", this.result, this.ray, this.normal, Float.valueOf(this.probabilityDensityFunctionValueDirection), Float.valueOf(this.probabilityDensityFunctionValuePosition));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the normal associated with this {@code LightRadianceEmittedResult} instance.
	 * 
	 * @return a {@code Vector3F} instance with the normal associated with this {@code LightRadianceEmittedResult} instance
	 */
	public Vector3F getNormal() {
		return this.normal;
	}
	
	/**
	 * Compares {@code object} to this {@code LightRadianceEmittedResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LightRadianceEmittedResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LightRadianceEmittedResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LightRadianceEmittedResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LightRadianceEmittedResult)) {
			return false;
		} else if(!Objects.equals(this.result, LightRadianceEmittedResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.ray, LightRadianceEmittedResult.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.normal, LightRadianceEmittedResult.class.cast(object).normal)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValueDirection, LightRadianceEmittedResult.class.cast(object).probabilityDensityFunctionValueDirection)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValuePosition, LightRadianceEmittedResult.class.cast(object).probabilityDensityFunctionValuePosition)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value for the direction.
	 * 
	 * @return the probability density function (PDF) value for the direction
	 */
	public float getProbabilityDensityFunctionValueDirection() {
		return this.probabilityDensityFunctionValueDirection;
	}
	
	/**
	 * Returns the probability density function (PDF) value for the position.
	 * 
	 * @return the probability density function (PDF) value for the position
	 */
	public float getProbabilityDensityFunctionValuePosition() {
		return this.probabilityDensityFunctionValuePosition;
	}
	
	/**
	 * Returns a hash code for this {@code LightRadianceEmittedResult} instance.
	 * 
	 * @return a hash code for this {@code LightRadianceEmittedResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.ray, this.normal, Float.valueOf(this.probabilityDensityFunctionValueDirection), Float.valueOf(this.probabilityDensityFunctionValuePosition));
	}
}