/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

import org.macroing.java.lang.Floats;

/**
 * A {@code Shape3F} is a 3-dimensional extension of {@link Shape} that adds additional methods that operates on {@code float}-based data types.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape3F extends Shape {
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Shape3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Shape3F} instance
	 */
	BoundingVolume3F getBoundingVolume();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Shape3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Shape3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum);
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Shape3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Shape3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	boolean contains(final Point3F point);
	
	/**
	 * Returns the surface area of this {@code Shape3F} instance.
	 * 
	 * @return the surface area of this {@code Shape3F} instance
	 */
	float getSurfaceArea();
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Shape3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Shape3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples this {@code Shape3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	default Optional<SurfaceSample3F> sample(final Point2F sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		return SurfaceSample3F.EMPTY;
	}
	
	/**
	 * Samples this {@code Shape3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code sample} or {@code surfaceIntersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @param surfaceIntersection a {@link SurfaceIntersection3F} instance
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code sample} or {@code surfaceIntersection} are {@code null}
	 */
//	TODO: Add Unit Tests!
	default Optional<SurfaceSample3F> sample(final Point2F sample, final SurfaceIntersection3F surfaceIntersection) {
		Objects.requireNonNull(sample, "sample == null");
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		
		final Optional<SurfaceSample3F> optionalSurfaceSample = sample(sample);
		
		if(optionalSurfaceSample.isPresent()) {
			final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			final Point3F point = surfaceSample.getPoint();
			
			final Vector3F incoming = Vector3F.direction(surfaceIntersectionPoint, point);
			
			if(Floats.isZero(incoming.lengthSquared())) {
				return Optional.empty();
			}
			
			final Vector3F surfaceNormal = surfaceSample.getSurfaceNormal();
			final Vector3F incomingNormalized = Vector3F.normalize(incoming);
			
			final float probabilityDensityFunctionValue = Point3F.distanceSquared(point, surfaceIntersectionPoint) / Floats.abs(Vector3F.dotProduct(surfaceNormal, Vector3F.negate(incomingNormalized)));
			
			if(Floats.isInfinite(probabilityDensityFunctionValue) || Floats.isNaN(probabilityDensityFunctionValue)) {
				return Optional.empty();
			}
			
			return Optional.of(new SurfaceSample3F(point, surfaceNormal, probabilityDensityFunctionValue));
		}
		
		return Optional.empty();
	}
	
	/**
	 * Performs an intersection test between {@code surfaceIntersector} and this {@code Shape3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code Shape3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code surfaceIntersector} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersector a {@link SurfaceIntersector3F} instance
	 * @return {@code true} if, and only if, {@code surfaceIntersector} intersects this {@code Shape3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code surfaceIntersector} is {@code null}
	 */
	default boolean intersection(final SurfaceIntersector3F surfaceIntersector) {
		return surfaceIntersector.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Shape3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Shape3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Shape3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	default boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return !Floats.isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Evaluates the probability density function (PDF) for {@code surfaceIntersection} and {@code incoming}.
	 * <p>
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection a {@link SurfaceIntersection3F} instance
	 * @param incoming a {@link Vector3F} instance with the incoming direction
	 * @return the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code incoming} are {@code null}
	 */
//	TODO: Add Unit Tests!
	default float evaluateProbabilityDensityFunction(final SurfaceIntersection3F surfaceIntersection, final Vector3F incoming) {
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Ray3F ray = surfaceIntersection.createRay(incoming);
		
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionShape = intersection(ray, 0.001F, Floats.MAX_VALUE);
		
		if(optionalSurfaceIntersectionShape.isPresent()) {
			final SurfaceIntersection3F surfaceIntersectionShape = optionalSurfaceIntersectionShape.get();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			final Point3F surfaceIntersectionPointShape = surfaceIntersectionShape.getSurfaceIntersectionPoint();
			
			final float probabilityDensityFunctionValue = Point3F.distanceSquared(surfaceIntersectionPointShape, surfaceIntersectionPoint) / (Floats.abs(Vector3F.dotProduct(surfaceIntersectionShape.getSurfaceNormalS(), Vector3F.negate(incoming)) * getSurfaceArea()));
			
			if(Floats.isInfinite(probabilityDensityFunctionValue) || Floats.isNaN(probabilityDensityFunctionValue)) {
				return 0.0F;
			}
			
			return probabilityDensityFunctionValue;
		}
		
		return 0.0F;
	}
}