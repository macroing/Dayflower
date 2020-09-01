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

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class SurfaceIntersection3F {
	private final OrthoNormalBasis33F orthoNormalBasisG;
	private final OrthoNormalBasis33F orthoNormalBasisS;
	private final Point2F textureCoordinates;
	private final Point3F surfaceIntersectionPoint;
	private final Ray3F ray;
	private final Shape3F shape;
	private final Vector3F surfaceNormalG;
	private final Vector3F surfaceNormalS;
	private final float t;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public SurfaceIntersection3F(final OrthoNormalBasis33F orthoNormalBasisG, final OrthoNormalBasis33F orthoNormalBasisS, final Point2F textureCoordinates, final Point3F surfaceIntersectionPoint, final Ray3F ray, final Shape3F shape, final Vector3F surfaceNormalG, final Vector3F surfaceNormalS, final float t) {
		this.orthoNormalBasisG = Objects.requireNonNull(orthoNormalBasisG, "orthoNormalBasisG == null");
		this.orthoNormalBasisS = Objects.requireNonNull(orthoNormalBasisS, "orthoNormalBasisS == null");
		this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
		this.surfaceIntersectionPoint = Objects.requireNonNull(surfaceIntersectionPoint, "surfaceIntersectionPoint == null");
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.surfaceNormalG = Objects.requireNonNull(surfaceNormalG, "surfaceNormalG == null");
		this.surfaceNormalS = Objects.requireNonNull(surfaceNormalS, "surfaceNormalS == null");
		this.t = t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F getOrthoNormalBasisG() {
		return this.orthoNormalBasisG;
	}
	
//	TODO: Add Javadocs!
	public OrthoNormalBasis33F getOrthoNormalBasisS() {
		return this.orthoNormalBasisS;
	}
	
//	TODO: Add Javadocs!
	public Point2F getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
//	TODO: Add Javadocs!
	public Point3F getSurfaceIntersectionPoint() {
		return this.surfaceIntersectionPoint;
	}
	
//	TODO: Add Javadocs!
	public Ray3F getRay() {
		return this.ray;
	}
	
//	TODO: Add Javadocs!
	public Shape3F getShape() {
		return this.shape;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new SurfaceIntersection3F(%s, %s, %s, %s, %s, %s, %s, %s, %+.10f)", this.orthoNormalBasisG, this.orthoNormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
	}
	
//	TODO: Add Javadocs!
	public Vector3F getSurfaceNormalG() {
		return this.surfaceNormalG;
	}
	
//	TODO: Add Javadocs!
	public Vector3F getSurfaceNormalS() {
		return this.surfaceNormalS;
	}
	
//	TODO: Add Javadocs!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceIntersection3F)) {
			return false;
		} else if(!Objects.equals(this.orthoNormalBasisG, SurfaceIntersection3F.class.cast(object).orthoNormalBasisG)) {
			return false;
		} else if(!Objects.equals(this.orthoNormalBasisS, SurfaceIntersection3F.class.cast(object).orthoNormalBasisS)) {
			return false;
		} else if(!Objects.equals(this.textureCoordinates, SurfaceIntersection3F.class.cast(object).textureCoordinates)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionPoint, SurfaceIntersection3F.class.cast(object).surfaceIntersectionPoint)) {
			return false;
		} else if(!Objects.equals(this.ray, SurfaceIntersection3F.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.shape, SurfaceIntersection3F.class.cast(object).shape)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormalG, SurfaceIntersection3F.class.cast(object).surfaceNormalG)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormalS, SurfaceIntersection3F.class.cast(object).surfaceNormalS)) {
			return false;
		} else if(!equal(this.t, SurfaceIntersection3F.class.cast(object).t)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getT() {
		return this.t;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.orthoNormalBasisG, this.orthoNormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
	}
}