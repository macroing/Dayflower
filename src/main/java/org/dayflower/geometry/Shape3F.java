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

import static org.dayflower.util.Floats.isNaN;

import java.util.Optional;

import org.dayflower.node.Node;

/**
 * A {@code Shape3F} denotes a 3-dimensional shape that uses the data type {@code float}.
 * <p>
 * All official implementations of this interface are immutable and therefore thread-safe. But this cannot be guaranteed for all implementations.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Shape3F extends Node {
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Shape3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Shape3F} instance
	 */
	BoundingVolume3F getBoundingVolume();
	
	/**
	 * Samples this {@code Shape3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v);
	
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
	 * Returns a {@code String} with the name of this {@code Shape3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Shape3F} instance
	 */
	String getName();
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3F} instance
	 * @param point the point on this {@code Shape3F} instance
	 * @param surfaceNormal the surface normal on this {@code Shape3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal);
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Shape3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Shape3F} instance
	 * @param direction the direction to this {@code Shape3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction);
	
	/**
	 * Returns the surface area of this {@code Shape3F} instance.
	 * 
	 * @return the surface area of this {@code Shape3F} instance
	 */
	float getSurfaceArea();
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Shape3F} instance.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Shape3F} instance
	 */
	float getSurfaceAreaProbabilityDensityFunctionValue();
	
	/**
	 * Returns the volume of this {@code Shape3F} instance.
	 * 
	 * @return the volume of this {@code Shape3F} instance
	 */
	float getVolume();
	
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
	
	/**
	 * Returns an {@code int} with the ID of this {@code Shape3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Shape3F} instance
	 */
	int getID();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		return !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
}