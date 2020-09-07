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

import org.dayflower.geometry.SurfaceIntersection3F;

//TODO: Add Javadocs!
public final class Intersection {
	private final Primitive primitive;
	private final SurfaceIntersection3F surfaceIntersectionObjectSpace;
	private final SurfaceIntersection3F surfaceIntersectionWorldSpace;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Intersection(final Primitive primitive, final SurfaceIntersection3F surfaceIntersectionObjectSpace) {
		this.primitive = Objects.requireNonNull(primitive, "primitive == null");
		this.surfaceIntersectionObjectSpace = Objects.requireNonNull(surfaceIntersectionObjectSpace, "surfaceIntersectionObjectSpace == null");
		this.surfaceIntersectionWorldSpace = SurfaceIntersection3F.transform(surfaceIntersectionObjectSpace, primitive.getObjectToWorld(), primitive.getWorldToObject());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Primitive getPrimitive() {
		return this.primitive;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Primitive(%s, %s)", this.primitive, this.surfaceIntersectionObjectSpace);
	}
	
//	TODO: Add Javadocs!
	public SurfaceIntersection3F getSurfaceIntersectionObjectSpace() {
		return this.surfaceIntersectionObjectSpace;
	}
	
//	TODO: Add Javadocs!
	public SurfaceIntersection3F getSurfaceIntersectionWorldSpace() {
		return this.surfaceIntersectionWorldSpace;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Intersection)) {
			return false;
		} else if(!Objects.equals(this.primitive, Intersection.class.cast(object).primitive)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionObjectSpace, Intersection.class.cast(object).surfaceIntersectionObjectSpace)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionWorldSpace, Intersection.class.cast(object).surfaceIntersectionWorldSpace)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.primitive, this.surfaceIntersectionObjectSpace, this.surfaceIntersectionWorldSpace);
	}
}