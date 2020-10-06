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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

//TODO: Add Javadocs!
public final class MutableSurfaceIntersection3F {
	private Ray3F ray;
	private Shape3F shape;
	private float t;
	private float tMaximum;
	private float tMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public MutableSurfaceIntersection3F(final Ray3F ray) {
		this(ray, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public MutableSurfaceIntersection3F(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.shape = null;
		this.t = Float.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Optional<SurfaceIntersection3F> computeSurfaceIntersection() {
		return isIntersecting() ? this.shape.intersection(this.ray) : SurfaceIntersection3F.EMPTY;
	}
	
//	TODO: Add Javadocs!
	public Optional<Shape3F> getShape() {
		return Optional.ofNullable(this.shape);
	}
	
//	TODO: Add Javadocs!
	public Ray3F getRay() {
		return this.ray;
	}
	
//	TODO: Add Javadocs!
	public boolean intersection(final Shape3F shape) {
		final float t = shape.intersectionT(this.ray, this.tMinimum, this.tMaximum);
		
		if(!isNaN(t) && (isNaN(this.t) || t < this.t) && t > this.tMinimum && t < this.tMaximum) {
			this.t = t;
			this.tMaximum = t;
			this.shape = shape;
			
			return true;
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	public boolean isIntersecting() {
		return this.shape != null && !isNaN(this.t);
	}
	
//	TODO: Add Javadocs!
	public boolean isIntersecting(final BoundingVolume3F boundingVolume) {
		return boundingVolume.intersects(this.ray, this.tMinimum, this.tMaximum);
	}
	
//	TODO: Add Javadocs!
	public float getT() {
		return this.t;
	}
	
//	TODO: Add Javadocs!
	public float getTMaximum() {
		return this.tMaximum;
	}
	
//	TODO: Add Javadocs!
	public float getTMinimum() {
		return this.tMinimum;
	}
	
//	TODO: Add Javadocs!
	public void initialize(final Ray3F ray) {
		initialize(ray, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.t = Float.NaN;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
//	TODO: Add Javadocs!
	public void transform(final Matrix44F matrix) {
		final Ray3F rayOldSpace = this.ray;
		final Ray3F rayNewSpace = Ray3F.transform(matrix, rayOldSpace);
		
		final float tOldSpace = this.t;
		final float tNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tOldSpace);
		
		final float tMaximumOldSpace = this.tMaximum;
		final float tMaximumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMaximumOldSpace);
		
		final float tMinimumOldSpace = this.tMinimum;
		final float tMinimumNewSpace = doTransformT(matrix, rayOldSpace, rayNewSpace, tMinimumOldSpace);
		
		this.ray = rayNewSpace;
		this.t = tNewSpace;
		this.tMaximum = tMaximumNewSpace;
		this.tMinimum = tMinimumNewSpace;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doTransformT(final Matrix44F matrix, final Ray3F rayOldSpace, final Ray3F rayNewSpace, final float t) {
		return !equal(t, 0.0F) && t < Float.MAX_VALUE ? abs(Point3F.distance(rayNewSpace.getOrigin(), Point3F.transform(matrix, Point3F.add(rayOldSpace.getOrigin(), rayOldSpace.getDirection(), t)))) : t;
	}
}