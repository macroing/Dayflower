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

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.MutableSurfaceIntersection3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;

//TODO: Add Javadocs!
public final class MutableIntersection {
	private MutableSurfaceIntersection3F mutableSurfaceIntersection;
	private Primitive primitive;
	private Ray3F ray;
	private float tMaximum;
	private float tMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public MutableIntersection(final Ray3F ray) {
		this(ray, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public MutableIntersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.mutableSurfaceIntersection = new MutableSurfaceIntersection3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
		this.primitive = null;
		this.ray = ray;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public MutableSurfaceIntersection3F getMutableSurfaceIntersection() {
		return this.mutableSurfaceIntersection;
	}
	
//	TODO: Add Javadocs!
	public Optional<Intersection> intersection() {
		final Primitive primitive = this.primitive;
		
		if(primitive != null) {
			final Matrix44F worldToObject = primitive.getWorldToObject();
			
			final Ray3F rayWorldSpace = this.mutableSurfaceIntersection.getRay();
			final Ray3F rayObjectSpace = Ray3F.transform(worldToObject, rayWorldSpace);
			
			final Optional<Shape3F> optionalShape = this.mutableSurfaceIntersection.getShape();
			
			if(optionalShape.isPresent()) {
				final Shape3F shape = optionalShape.get();
				
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionObjectSpace = shape.intersection(rayObjectSpace);
				
				if(optionalSurfaceIntersectionObjectSpace.isPresent()) {
					final SurfaceIntersection3F surfaceIntersectionObjectSpace = optionalSurfaceIntersectionObjectSpace.get();
					
					final Intersection intersection = new Intersection(primitive, surfaceIntersectionObjectSpace);
					
					return Optional.of(intersection);
				}
			}
		}
		
		return Intersection.EMPTY;
	}
	
//	TODO: Add Javadocs!
	public Optional<Primitive> getPrimitive() {
		return Optional.ofNullable(this.primitive);
	}
	
//	TODO: Add Javadocs!
	public boolean isIntersecting() {
		return this.primitive != null;
	}
	
//	TODO: Add Javadocs!
	public void initialize(final Ray3F ray) {
		initialize(ray, 0.0001F, Float.MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public void initialize(final Ray3F ray, final float tMinimum, final float tMaximum) {
		this.mutableSurfaceIntersection = new MutableSurfaceIntersection3F(Objects.requireNonNull(ray, "ray == null"), tMinimum, tMaximum);
		this.primitive = null;
		this.ray = ray;
		this.tMaximum = tMaximum;
		this.tMinimum = tMinimum;
	}
	
//	TODO: Add Javadocs!
	public void intersection(final Primitive primitive) {
		if(this.mutableSurfaceIntersection.isIntersecting(primitive.getBoundingVolume())) {
			this.mutableSurfaceIntersection.transform(primitive.getWorldToObject());
			
			if(this.mutableSurfaceIntersection.intersection(primitive.getShape())) {
				this.primitive = primitive;
			}
			
			this.mutableSurfaceIntersection.transform(primitive.getObjectToWorld());
		}
	}
}