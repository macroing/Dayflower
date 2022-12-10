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

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isZero;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code Shape3D} is a 3-dimensional extension of {@link Shape} that adds additional methods that operates on {@code double}-based data types.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape3D extends Shape {
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Shape3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Shape3D} instance
	 */
	BoundingVolume3D getBoundingVolume();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Shape3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Shape3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum);
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Shape3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Shape3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	boolean contains(final Point3D point);
	
	/**
	 * Returns the surface area of this {@code Shape3D} instance.
	 * 
	 * @return the surface area of this {@code Shape3D} instance
	 */
	double getSurfaceArea();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Shape3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Shape3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples this {@code Shape3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2D} instance with a sample point
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	default Optional<SurfaceSample3D> sample(final Point2D sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		return SurfaceSample3D.EMPTY;
	}
	
	/**
	 * Samples this {@code Shape3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If either {@code sample} or {@code surfaceIntersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2D} instance with a sample point
	 * @param surfaceIntersection a {@link SurfaceIntersection3D} instance
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code sample} or {@code surfaceIntersection} are {@code null}
	 */
//	TODO: Add Unit Tests!
	default Optional<SurfaceSample3D> sample(final Point2D sample, final SurfaceIntersection3D surfaceIntersection) {
		Objects.requireNonNull(sample, "sample == null");
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		
		final Optional<SurfaceSample3D> optionalSurfaceSample = sample(sample);
		
		if(optionalSurfaceSample.isPresent()) {
			final SurfaceSample3D surfaceSample = optionalSurfaceSample.get();
			
			final Point3D surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			final Point3D point = surfaceSample.getPoint();
			
			final Vector3D incoming = Vector3D.direction(surfaceIntersectionPoint, point);
			
			if(isZero(incoming.lengthSquared())) {
				return Optional.empty();
			}
			
			final Vector3D surfaceNormal = surfaceSample.getSurfaceNormal();
			final Vector3D incomingNormalized = Vector3D.normalize(incoming);
			
			final double probabilityDensityFunctionValue = Point3D.distanceSquared(point, surfaceIntersectionPoint) / abs(Vector3D.dotProduct(surfaceNormal, Vector3D.negate(incomingNormalized)));
			
			if(isInfinite(probabilityDensityFunctionValue)) {
				return Optional.empty();
			}
			
			return Optional.of(new SurfaceSample3D(point, surfaceNormal, probabilityDensityFunctionValue));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code Shape3D} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code Shape3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3D} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code Shape3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
	default boolean intersection(final SurfaceIntersector3D surfaceIntersector) {
		return surfaceIntersector.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Shape3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Shape3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Shape3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	default boolean intersects(final Ray3D ray, final double tMinimum, final double tMaximum) {
		return !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Evaluates the probability density function (PDF) for {@code surfaceIntersection} and {@code incoming}.
	 * <p>
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection a {@link SurfaceIntersection3D} instance
	 * @param incoming a {@link Vector3D} instance with the incoming direction
	 * @return the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code incoming} are {@code null}
	 */
//	TODO: Add Unit Tests!
	default double evaluateProbabilityDensityFunction(final SurfaceIntersection3D surfaceIntersection, final Vector3D incoming) {
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Ray3D ray = surfaceIntersection.createRay(incoming);
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionShape = intersection(ray, 0.001D, MAX_VALUE);
		
		if(optionalSurfaceIntersectionShape.isPresent()) {
			final SurfaceIntersection3D surfaceIntersectionShape = optionalSurfaceIntersectionShape.get();
			
			final Point3D surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			final Point3D surfaceIntersectionPointShape = surfaceIntersectionShape.getSurfaceIntersectionPoint();
			
			final double probabilityDensityFunctionValue = Point3D.distanceSquared(surfaceIntersectionPointShape, surfaceIntersectionPoint) / (abs(Vector3D.dotProduct(surfaceIntersectionShape.getSurfaceNormalS(), Vector3D.negate(incoming)) * getSurfaceArea()));
			
			if(isInfinite(probabilityDensityFunctionValue)) {
				return 0.0D;
			}
			
			return probabilityDensityFunctionValue;
		}
		
		return 0.0D;
	}
}