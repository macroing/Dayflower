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
package org.dayflower.scene.bxdf.rayito;

import static org.dayflower.util.Floats.equal;

import java.util.Objects;

import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;

/**
 * A {@code RayitoBXDFResult} represents a result produced by a {@link RayitoBXDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayitoBXDFResult {
	private final Color3F result;
	private final Vector3F incoming;
	private final Vector3F normal;
	private final Vector3F outgoing;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RayitoBXDFResult} instance.
	 * <p>
	 * If either {@code result}, {@code outgoing}, {@code normal} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result that is associated with this {@code RayitoBXDFResult} instance
	 * @param outgoing a {@link Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance
	 * @param normal a {@code Vector3F} instance with the normal that is associated with this {@code RayitoBXDFResult} instance
	 * @param incoming a {@code Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value associated with this {@code RayitoBXDFResult} instance
	 * @throws NullPointerException thrown if, and only if, either {@code outgoing}, {@code normal} or {@code incoming} are {@code null}
	 */
	public RayitoBXDFResult(final Color3F result, final Vector3F outgoing, final Vector3F normal, final Vector3F incoming, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.outgoing = Objects.requireNonNull(outgoing, "outgoing == null");
		this.normal = Objects.requireNonNull(normal, "normal == null");
		this.incoming = Objects.requireNonNull(incoming, "incoming == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the result associated with this {@code RayitoBXDFResult} instance.
	 * 
	 * @return a {@code Color3F} instance with the result associated with this {@code RayitoBXDFResult} instance
	 */
	public Color3F getResult() {
		return this.result;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RayitoBXDFResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code RayitoBXDFResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new RayitoBXDFResult(%s, %s, %s, %s, %+.10f)", this.result, this.outgoing, this.normal, this.incoming, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance.
	 * <p>
	 * The incoming direction is directed from the light source to the surface intersection point.
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getIncoming() {
		return this.incoming;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the normal that is associated with this {@code RayitoBXDFResult} instance.
	 * 
	 * @return a {@code Vector3F} instance with the normal that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getNormal() {
		return this.normal;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance.
	 * <p>
	 * The outgoing direction is directed from the surface intersection point to the origin of the ray. It is the negated ray direction.
	 * 
	 * @return a {@code Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getOutgoing() {
		return this.outgoing;
	}
	
	/**
	 * Compares {@code object} to this {@code RayitoBXDFResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RayitoBXDFResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RayitoBXDFResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RayitoBXDFResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RayitoBXDFResult)) {
			return false;
		} else if(!Objects.equals(this.result, RayitoBXDFResult.class.cast(object).result)) {
			return false;
		} else if(!Objects.equals(this.incoming, RayitoBXDFResult.class.cast(object).incoming)) {
			return false;
		} else if(!Objects.equals(this.normal, RayitoBXDFResult.class.cast(object).normal)) {
			return false;
		} else if(!Objects.equals(this.outgoing, RayitoBXDFResult.class.cast(object).outgoing)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, RayitoBXDFResult.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, the probability density function (PDF) value is represented by a finite value, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the probability density function (PDF) value is represented by a finite value, {@code false} otherwise
	 */
	public boolean isFinite() {
		return Float.isFinite(this.probabilityDensityFunctionValue);
	}
	
	/**
	 * Returns the probability density function (PDF) value associated with this {@code RayitoBXDFResult} instance.
	 * 
	 * @return the probability density function (PDF) value associated with this {@code RayitoBXDFResult} instance
	 */
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code RayitoBXDFResult} instance.
	 * 
	 * @return a hash code for this {@code RayitoBXDFResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.result, this.incoming, this.normal, this.outgoing, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}