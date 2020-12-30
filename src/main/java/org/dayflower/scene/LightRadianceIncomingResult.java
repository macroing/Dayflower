/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code LightRadianceIncomingResult} is returned by a {@link Light} instance and contains the computed properties.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LightRadianceIncomingResult {
	private final Color3F result;
	private final Point3F point;
	private final Vector3F incoming;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LightRadianceIncomingResult} instance.
	 * <p>
	 * If either {@code result}, {@code point} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result associated with this {@code LightRadianceIncomingResult} instance
	 * @param point the {@link Point3F} instance associated with this {@code LightRadianceIncomingResult} instance
	 * @param incoming a {@link Vector3F} instance with the incoming direction associated with this {@code LightRadianceIncomingResult} instance
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code result}, {@code point} or {@code incoming} are {@code null}
	 */
	public LightRadianceIncomingResult(final Color3F result, final Point3F point, final Vector3F incoming, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.point = Objects.requireNonNull(point, "point == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result associated with this {@code LightRadianceIncomingResult} instance.
	 * 
	 * @return a {@code Color3F} instance with the result associated with this {@code LightRadianceIncomingResult} instance
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns the {@link Point3F} instance associated with this {@code LightRadianceIncomingResult} instance.
	 * 
	 * @return the {@code Point3F} instance associated with this {@code LightRadianceIncomingResult} instance
	 */
	public Point3F getPoint() {
		return this.point;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code LightRadianceIncomingResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code LightRadianceIncomingResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new LightRadianceIncomingResult(%s, %s, %s, %+.10f)", this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction associated with this {@code LightRadianceIncomingResult} instance.
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction associated with this {@code LightRadianceIncomingResult} instance
	 */
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
	/**
	 * Compares {@code object} to this {@code LightRadianceIncomingResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LightRadianceIncomingResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LightRadianceIncomingResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LightRadianceIncomingResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LightRadianceIncomingResult)) {
			return false;
		} else if(!Objects.equals(this.result, LightRadianceIncomingResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.point, LightRadianceIncomingResult.class.cast(object).point)) {
			return false;
		} else if(!Objects.equals(this.incoming, LightRadianceIncomingResult.class.cast(object).incoming)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, LightRadianceIncomingResult.class.cast(object).probabilityDensityFunctionValue)) {
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
	 * Returns a hash code for this {@code LightRadianceIncomingResult} instance.
	 * 
	 * @return a hash code for this {@code LightRadianceIncomingResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.point, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}