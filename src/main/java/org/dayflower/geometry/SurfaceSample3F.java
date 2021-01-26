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
package org.dayflower.geometry;

import static org.dayflower.utility.Floats.equal;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@code SurfaceSample3F} denotes a 3-dimensional surface sample that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceSample3F {
	/**
	 * An empty {@code Optional} instance.
	 */
	public static final Optional<SurfaceSample3F> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F point;
	private final Vector3F pointError;
	private final Vector3F surfaceNormal;
	private final float probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceSample3F} instance.
	 * <p>
	 * If either {@code point}, {@code pointError} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point the sampled point
	 * @param pointError the {@link Vector3F} instance that contains the floating-point precision error of {@code point}
	 * @param surfaceNormal the sampled surface normal
	 * @param probabilityDensityFunctionValue the sampled probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code pointError} or {@code surfaceNormal} are {@code null}
	 */
	public SurfaceSample3F(final Point3F point, final Vector3F pointError, final Vector3F surfaceNormal, final float probabilityDensityFunctionValue) {
		this.point = Objects.requireNonNull(point, "point == null");
		this.pointError = Objects.requireNonNull(pointError, "pointError == null");
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
		return String.format("new SurfaceSample3F(%s, %s, %s, %+.10f)", this.point, this.pointError, this.surfaceNormal, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns the {@link Vector3F} instance that contains the floating-point precision error of the point.
	 * 
	 * @return the {@code Vector3F} instance that contains the floating-point precision error of the point
	 */
	public Vector3F getPointError() {
		return this.pointError;
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
		} else if(!Objects.equals(this.pointError, SurfaceSample3F.class.cast(object).pointError)) {
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
		return Objects.hash(this.point, this.pointError, this.surfaceNormal, Float.valueOf(this.probabilityDensityFunctionValue));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceSample3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceSample} or {@code matrix} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SurfaceSample3F.transform(surfaceSample, matrix, Matrix44F.inverse(matrix));
	 * }
	 * </pre>
	 * 
	 * @param surfaceSample the {@code SurfaceSample3F} instance to transform
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code SurfaceSample3F} instance with the result of the transformation
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceSample} or {@code matrix} are {@code null}
	 */
	public static SurfaceSample3F transform(final SurfaceSample3F surfaceSample, final Matrix44F matrix) {
		return transform(surfaceSample, matrix, Matrix44F.inverse(matrix));
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceSample3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceSample}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceSample the {@code SurfaceSample3F} instance to transform
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @param matrixInverse the {@code Matrix44F} instance to perform the transformation with in inverse transpose order
	 * @return a new {@code SurfaceSample3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceSample}, {@code matrix} or {@code matrixInverse} are {@code null}
	 */
	public static SurfaceSample3F transform(final SurfaceSample3F surfaceSample, final Matrix44F matrix, final Matrix44F matrixInverse) {
		final Point3F pointOldSpace = surfaceSample.point;
		final Point3F pointNewSpace = Point3F.transformAndDivide(matrix, pointOldSpace);
		
		final Vector3F pointErrorOldSpace = surfaceSample.pointError;
		final Vector3F pointErrorNewSpace = Vector3F.transformError(matrix, pointOldSpace, pointErrorOldSpace);
		
		final Vector3F surfaceNormalOldSpace = surfaceSample.surfaceNormal;
		final Vector3F surfaceNormalNewSpace = Vector3F.transformTranspose(matrixInverse, surfaceNormalOldSpace);
		
		final float probabilityDensityFunctionValue = surfaceSample.probabilityDensityFunctionValue;
		
		return new SurfaceSample3F(pointNewSpace, pointErrorNewSpace, surfaceNormalNewSpace, probabilityDensityFunctionValue);
	}
}