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
	private final Vector3F i;
	private final Vector3F n;
	private final Vector3F o;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code RayitoBXDFResult} instance.
	 * <p>
	 * If either {@code result}, {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param result a {@link Color3F} instance with the result that is associated with this {@code RayitoBXDFResult} instance
	 * @param o a {@link Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance
	 * @param n a {@code Vector3F} instance with the surface normal that is associated with this {@code RayitoBXDFResult} instance
	 * @param i a {@code Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value associated with this {@code RayitoBXDFResult} instance
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	public RayitoBXDFResult(final Color3F result, final Vector3F o, final Vector3F n, final Vector3F i, final float probabilityDensityFunctionValue) {
		this.result = Objects.requireNonNull(result, "result == null");
		this.o = Objects.requireNonNull(o, "o == null");
		this.n = Objects.requireNonNull(n, "n == null");
		this.i = Objects.requireNonNull(i, "i == null");
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
		return String.format("new RayitoBXDFResult(%s, %s, %s, %s, %+.10f)", this.result, this.o, this.n, this.i, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance.
	 * <p>
	 * The incoming direction is directed from the light source to the surface intersection point.
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getI() {
		return this.i;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the surface normal that is associated with this {@code RayitoBXDFResult} instance.
	 * 
	 * @return a {@code Vector3F} instance with the surface normal that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getN() {
		return this.n;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance.
	 * <p>
	 * The outgoing direction is directed from the surface intersection point to the origin of the ray. It is the negated ray direction.
	 * 
	 * @return a {@code Vector3F} instance with the outgoing direction that is associated with this {@code RayitoBXDFResult} instance
	 */
	public Vector3F getO() {
		return this.o;
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
		} else if(!Objects.equals(this.i, RayitoBXDFResult.class.cast(object).i)) {
			return false;
		} else if(!Objects.equals(this.n, RayitoBXDFResult.class.cast(object).n)) {
			return false;
		} else if(!Objects.equals(this.o, RayitoBXDFResult.class.cast(object).o)) {
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
		return Objects.hash(this.result, this.i, this.n, this.o, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}