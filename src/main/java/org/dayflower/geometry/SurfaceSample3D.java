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
package org.dayflower.geometry;

import static org.dayflower.utility.Doubles.equal;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code SurfaceSample3D} contains information about the surface of a {@link Shape3D} instance where it is being sampled.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceSample3D {
	/**
	 * An empty {@code Optional} instance.
	 */
//	TODO: Add Unit Tests!
	public static final Optional<SurfaceSample3D> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D point;
	private final Vector3D pointError;
	private final Vector3D surfaceNormal;
	private final double probabilityDensityFunctionValue;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceSample3D} instance.
	 * <p>
	 * If either {@code point}, {@code pointError} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point the sampled point
	 * @param pointError the {@link Vector3D} instance that contains the floating-point precision error of {@code point}
	 * @param surfaceNormal the sampled surface normal
	 * @param probabilityDensityFunctionValue the sampled probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code pointError} or {@code surfaceNormal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceSample3D(final Point3D point, final Vector3D pointError, final Vector3D surfaceNormal, final double probabilityDensityFunctionValue) {
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
//	TODO: Add Unit Tests!
	public Point3D getPoint() {
		return this.point;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceSample3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceSample3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new SurfaceSample3D(%s, %s, %s, %+.10f)", this.point, this.pointError, this.surfaceNormal, Double.valueOf(this.probabilityDensityFunctionValue));
	}
	
	/**
	 * Returns the {@link Vector3D} instance that contains the floating-point precision error of the point.
	 * 
	 * @return the {@code Vector3D} instance that contains the floating-point precision error of the point
	 */
//	TODO: Add Unit Tests!
	public Vector3D getPointError() {
		return this.pointError;
	}
	
	/**
	 * Returns the sampled surface normal.
	 * 
	 * @return the sampled surface normal
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceSample3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceSample3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceSample3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceSample3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceSample3D)) {
			return false;
		} else if(!(Objects.equals(this.point, SurfaceSample3D.class.cast(object).point))) {
			return false;
		} else if(!Objects.equals(this.pointError, SurfaceSample3D.class.cast(object).pointError)) {
			return false;
		} else if(!(Objects.equals(this.surfaceNormal, SurfaceSample3D.class.cast(object).surfaceNormal))) {
			return false;
		} else if(!equal(this.probabilityDensityFunctionValue, SurfaceSample3D.class.cast(object).probabilityDensityFunctionValue)) {
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
//	TODO: Add Unit Tests!
	public double getProbabilityDensityFunctionValue() {
		return this.probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceSample3D} instance.
	 * 
	 * @return a hash code for this {@code SurfaceSample3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.point, this.pointError, this.surfaceNormal, Double.valueOf(this.probabilityDensityFunctionValue));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceSample3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceSample} or {@code matrix} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SurfaceSample3D.transform(surfaceSample, matrix, Matrix44D.inverse(matrix));
	 * }
	 * </pre>
	 * 
	 * @param surfaceSample the {@code SurfaceSample3D} instance to transform
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @return a new {@code SurfaceSample3D} instance with the result of the transformation
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceSample} or {@code matrix} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceSample3D transform(final SurfaceSample3D surfaceSample, final Matrix44D matrix) {
		return transform(surfaceSample, matrix, Matrix44D.inverse(matrix));
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceSample3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceSample}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceSample the {@code SurfaceSample3D} instance to transform
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @param matrixInverse the {@code Matrix44D} instance to perform the transformation with in inverse transpose order
	 * @return a new {@code SurfaceSample3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceSample}, {@code matrix} or {@code matrixInverse} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceSample3D transform(final SurfaceSample3D surfaceSample, final Matrix44D matrix, final Matrix44D matrixInverse) {
		final Point3D pointOldSpace = surfaceSample.point;
		final Point3D pointNewSpace = Point3D.transformAndDivide(matrix, pointOldSpace);
		
		final Vector3D pointErrorOldSpace = surfaceSample.pointError;
		final Vector3D pointErrorNewSpace = Vector3D.transformError(matrix, pointOldSpace, pointErrorOldSpace);
		
		final Vector3D surfaceNormalOldSpace = surfaceSample.surfaceNormal;
		final Vector3D surfaceNormalNewSpace = Vector3D.transformTranspose(matrixInverse, surfaceNormalOldSpace);
		
		final double probabilityDensityFunctionValue = surfaceSample.probabilityDensityFunctionValue;
		
		return new SurfaceSample3D(pointNewSpace, pointErrorNewSpace, surfaceNormalNewSpace, probabilityDensityFunctionValue);
	}
}