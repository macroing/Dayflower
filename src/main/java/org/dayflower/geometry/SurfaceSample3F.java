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
package org.dayflower.geometry;

import static org.dayflower.geometry.Point3F.transform;
import static org.dayflower.geometry.Vector3F.transformTranspose;
import static org.dayflower.util.Floats.equal;

import java.util.Objects;

/**
 * A {@code SurfaceSample3F} denotes a 3-dimensional surface sample that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceSample3F {
	private final Point3F point;
	private final Vector3F surfaceNormal;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceSample3F} instance.
	 * <p>
	 * If either {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point the sampled point
	 * @param surfaceNormal the sampled surface normal
	 * @param probabilityDensityFunctionValue the sampled probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code surfaceNormal} are {@code null}
	 */
	public SurfaceSample3F(final Point3F point, final Vector3F surfaceNormal, final float probabilityDensityFunctionValue) {
		this.point = Objects.requireNonNull(point, "point == null");
		this.surfaceNormal = Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		this.probabilityDensityFunctionValue = probabilityDensityFunctionValue;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the sampled point.
	 * 
	 * @return the sampled point
	 */
	public Point3F getPoint() {
		return this.point;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceSample3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceSample3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new SurfaceSample3F(%s, %s, %+.10f)", this.point, this.surfaceNormal, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Transforms this {@code SurfaceSample3F} instance from world space to object space.
	 * <p>
	 * Returns a new {@code SurfaceSample3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code objectToWorld} or {@code worldToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectToWorld a {@link Matrix44F} instance that transforms from object space to world space
	 * @param worldToObject a {@code Matrix44F} instance that transforms from world space to object space
	 * @return a new {@code SurfaceSample3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code objectToWorld} or {@code worldToObject} are {@code null}
	 */
	public SurfaceSample3F transformToObjectSpace(final Matrix44F objectToWorld, final Matrix44F worldToObject) {
		return new SurfaceSample3F(transform(worldToObject, this.point), transformTranspose(objectToWorld, this.surfaceNormal), this.probabilityDensityFunctionValue);
	}
	
	/**
	 * Transforms this {@code SurfaceSample3F} instance from object space to world space.
	 * <p>
	 * Returns a new {@code SurfaceSample3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code objectToWorld} or {@code worldToObject} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectToWorld a {@link Matrix44F} instance that transforms from object space to world space
	 * @param worldToObject a {@code Matrix44F} instance that transforms from world space to object space
	 * @return a new {@code SurfaceSample3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code objectToWorld} or {@code worldToObject} are {@code null}
	 */
	public SurfaceSample3F transformToWorldSpace(final Matrix44F objectToWorld, final Matrix44F worldToObject) {
		return new SurfaceSample3F(transform(objectToWorld, this.point), transformTranspose(worldToObject, this.surfaceNormal), this.probabilityDensityFunctionValue);
	}
	
	/**
	 * Returns the sampled surface normal.
	 * 
	 * @return the sampled surface normal
	 */
	public Vector3F getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceSample3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceSample3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceSample3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceSample3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceSample3F)) {
			return false;
		} else if(!(Objects.equals(this.point, SurfaceSample3F.class.cast(object).point))) {
			return false;
		} else if(!(Objects.equals(this.surfaceNormal, SurfaceSample3F.class.cast(object).surfaceNormal))) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, SurfaceSample3F.class.cast(object).probabilityDensityFunctionValue)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the sampled probability density function (PDF) value.
	 * 
	 * @return the sampled probability density function (PDF) value
	 */
	public float getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceSample3F} instance.
	 * 
	 * @return a hash code for this {@code SurfaceSample3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.point, this.surfaceNormal, Float.valueOf(this.probabilityDensityFunctionValue));
	}
}