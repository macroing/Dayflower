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

import static org.dayflower.utility.Doubles.isNaN;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.node.Node;

/**
 * A {@code Shape3D} denotes a 3-dimensional shape that uses the data type {@code double}.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape3D extends Node {
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
	 * Returns a {@code String} with the name of this {@code Shape3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Shape3D} instance
	 */
	String getName();
	
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
	
	/**
	 * Returns an {@code int} with the ID of this {@code Shape3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Shape3D} instance
	 */
	int getID();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Samples this {@code Shape3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3D} instance
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	default Optional<SurfaceSample3D> sample(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final double u, final double v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return SurfaceSample3D.EMPTY;
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
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3D} instance
	 * @param point the point on this {@code Shape3D} instance
	 * @param surfaceNormal the surface normal on this {@code Shape3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	default double evaluateProbabilityDensityFunction(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Point3D point, final Vector3D surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0D;
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3D} instance
	 * @param direction the direction to this {@code Shape3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	default double evaluateProbabilityDensityFunction(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Vector3D direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0D;
	}
}