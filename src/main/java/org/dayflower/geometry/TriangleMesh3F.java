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

import java.lang.reflect.Field;
import java.util.Optional;

//TODO: Add Javadocs!
public final class TriangleMesh3F implements Shape3F {
//	TODO: Add Javadocs!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		return null;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray) {
		return intersection(ray, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return null;//TODO: Implement!
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean isIntersecting(final Ray3F ray) {
		return isIntersecting(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code TriangleMesh3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code TriangleMesh3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean isIntersecting(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return false;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		return 0;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float getSurfaceArea() {
		return 0;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0;//TODO: Implement!
	}
	
//	TODO: Add Javadocs!
	@Override
	public float getVolume() {
		return 0;//TODO: Implement!
	}
}