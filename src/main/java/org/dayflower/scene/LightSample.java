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

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;

import org.macroing.java.lang.Floats;

/**
 * A {@code LightSample} is returned by a {@link Light} instance and contains the computed properties.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LightSample {
	private final Color3F result;
	private final Point3F point;
	private final Vector3F incoming;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LightSample} instance.
	 * <p>
	 * If either {@code result}, {@code point} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result associated with this {@code LightSample} instance
	 * @param point the {@link Point3F} instance associated with this {@code LightSample} instance
	 * @param incoming a {@link Vector3F} instance with the incoming direction associated with this {@code LightSample} instance
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code result}, {@code point} or {@code incoming} are {@code null}
	 */
	public LightSample(final Color3F result, final Point3F point, final Vector3F incoming, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.point = Objects.requireNonNull(point, "point == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result associated with this {@code LightSample} instance.
	 * 
	 * @return a {@code Color3F} instance with the result associated with this {@code LightSample} instance
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns the {@link Point3F} instance associated with this {@code LightSample} instance.
	 * 
	 * @return the {@code Point3F} instance associated with this {@code LightSample} instance
	 */
	public Point3F getPoint() {
		return this.point;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LightSample} instance.
	 * 
	 * @return a {@code String} representation of this {@code LightSample} instance
	 */
	@Override
	public String toString() {
		return String.format("new LightSample(%s, %s, %s, %+.10f)", this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction associated with this {@code LightSample} instance.
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction associated with this {@code LightSample} instance
	 */
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
	/**
	 * Compares {@code object} to this {@code LightSample} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LightSample}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LightSample} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LightSample}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LightSample)) {
			return false;
		} else if(!Objects.equals(this.result, LightSample.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.point, LightSample.class.cast(object).point)) {
			return false;
		} else if(!Objects.equals(this.incoming, LightSample.class.cast(object).incoming)) {
			return false;
		} else if(!Floats.equals(this.probabilityDensityFunctionValue, LightSample.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value.
	 * 
	 * @return the probability density function (PDF) value
	 */
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code LightSample} instance.
	 * 
	 * @return a hash code for this {@code LightSample} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}