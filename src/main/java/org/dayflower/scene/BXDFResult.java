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

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.requireFiniteValue;

import java.util.Objects;

import org.dayflower.geometry.Vector3F;

/**
 * A {@code BXDFResult} represents a result produced by a {@link BXDF} instance.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BXDFResult {
	private final Vector3F i;
	private final Vector3F n;
	private final Vector3F o;
	private final float probabilityDensityFunctionValue;
	private final float reflectance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BXDFResult} instance.
	 * <p>
	 * If either {@code o}, {@code n} or {@code i} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code Float.isInfinite(probabilityDensityFunctionValue)}, {@code Float.isNaN(probabilityDensityFunctionValue)}, {@code Float.isInfinite(reflectance)} or {@code Float.isNaN(reflectance)} returns {@code true}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param o a {@link Vector3F} instance with the outgoing direction that is associated with this {@code BXDFResult} instance
	 * @param n a {@code Vector3F} instance with the surface normal that is associated with this {@code BXDFResult} instance
	 * @param i a {@code Vector3F} instance with the incoming direction that is associated with this {@code BXDFResult} instance
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value associated with this {@code BXDFResult} instance
	 * @param reflectance the reflectance associated with this {@code BXDFResult} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code Float.isInfinite(probabilityDensityFunctionValue)}, {@code Float.isNaN(probabilityDensityFunctionValue)}, {@code Float.isInfinite(reflectance)} or
	 *                                  {@code Float.isNaN(reflectance)} returns {@code true}
	 * @throws NullPointerException thrown if, and only if, either {@code o}, {@code n} or {@code i} are {@code null}
	 */
	public BXDFResult(final Vector3F o, final Vector3F n, final Vector3F i, final float probabilityDensityFunctionValue, final float reflectance) {
		this.o = Objects.requireNonNull(o, "o == null");
		this.n = Objects.requireNonNull(n, "n == null");
		this.i = Objects.requireNonNull(i, "i == null");
		this.probabilityDensityFunctionValue = requireFiniteValue(probabilityDensityFunctionValue, "probabilityDensityFunctionValue");
		this.reflectance = requireFiniteValue(reflectance, "reflectance");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BXDFResult} instance.
	 * 
	 * @return a {@code String} representation of this {@code BXDFResult} instance
	 */
	@Override
	public String toString() {
		return String.format("new BXDFResult(%s, %s, %s, %+.10f, %+.10f)", this.o, this.n, this.i, Float.valueOf(this.probabilityDensityFunctionValue), Float.valueOf(this.reflectance));
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the incoming direction that is associated with this {@code BXDFResult} instance.
	 * <p>
	 * The incoming direction is directed from the light source to the surface intersection point.
	 * 
	 * @return a {@code Vector3F} instance with the incoming direction that is associated with this {@code BXDFResult} instance
	 */
	public Vector3F getI() {
		return this.i;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the surface normal that is associated with this {@code BXDFResult} instance.
	 * 
	 * @return a {@code Vector3F} instance with the surface normal that is associated with this {@code BXDFResult} instance
	 */
	public Vector3F getN() {
		return this.n;
	}
	
	/**
	 * Returns a {@link Vector3F} instance with the outgoing direction that is associated with this {@code BXDFResult} instance.
	 * <p>
	 * The outgoing direction is directed from the surface intersection point to the origin of the ray. It is the negated ray direction.
	 * 
	 * @return a {@code Vector3F} instance with the outgoing direction that is associated with this {@code BXDFResult} instance
	 */
	public Vector3F getO() {
		return this.o;
	}
	
	/**
	 * Compares {@code object} to this {@code BXDFResult} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BXDFResult}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BXDFResult} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BXDFResult}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BXDFResult)) {
			return false;
		} else if(!Objects.equals(this.i, BXDFResult.class.cast(object).i)) {
			return false;
		} else if(!Objects.equals(this.n, BXDFResult.class.cast(object).n)) {
			return false;
		} else if(!Objects.equals(this.o, BXDFResult.class.cast(object).o)) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, BXDFResult.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else if(!equal(this.reflectance, BXDFResult.class.cast(object).reflectance)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value associated with this {@code BXDFResult} instance.
	 * 
	 * @return the probability density function (PDF) value associated with this {@code BXDFResult} instance
	 */
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the reflectance associated with this {@code BXDFResult} instance.
	 * 
	 * @return the reflectance associated with this {@code BXDFResult} instance
	 */
	public float getReflectance() {
		return this.reflectance;
	}
	
	/**
	 * Returns a hash code for this {@code BXDFResult} instance.
	 * 
	 * @return a hash code for this {@code BXDFResult} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.i, this.n, this.o, Float.valueOf(this.probabilityDensityFunctionValue), Float.valueOf(this.reflectance));
	}
}