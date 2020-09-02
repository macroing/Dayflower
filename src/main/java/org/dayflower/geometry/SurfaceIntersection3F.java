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

import java.util.Objects;

/**
 * A {@code SurfaceIntersection3F} denotes a surface intersection between a {@link Ray3F} instance and a {@link Shape3F} instance.
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@code Shape3F} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
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
	
	/**
	 * Constructs a new {@code SurfaceIntersection3F} instance.
	 * <p>
	 * If either {@code orthoNormalBasisG}, {@code orthoNormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape}, {@code surfaceNormalG} or {@code surfaceNormalS} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * 
	 * @param orthoNormalBasisG the {@link OrthoNormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 * @param orthoNormalBasisS the {@code OrthoNormalBasis33F} instance that is used as the orthonormal basis for shading
	 * @param textureCoordinates the {@link Point2F} instance that is used as the texture coordinates
	 * @param surfaceIntersectionPoint the {@link Point3F} instance that is used as the surface intersection point
	 * @param ray the {@link Ray3F} instance that was used in the intersection operation
	 * @param shape the {@link Shape3F} instance that was intersected
	 * @param surfaceNormalG the {@link Vector3F} instance that is used as the surface normal for the geometry
	 * @param surfaceNormalS the {@code Vector3F} instance that is used as the surface normal for shading
	 * @param t the parametric {@code t} value that represents the distance to the intersection
	 * @throws NullPointerException thrown if, and only if, either {@code orthoNormalBasisG}, {@code orthoNormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape}, {@code surfaceNormalG} or
	 *                              {@code surfaceNormalS} are {@code null}
	 */
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
	
	/**
	 * Returns the {@link OrthoNormalBasis33F} instance that is used as the orthonormal basis for the geometry.
	 * 
	 * @return the {@code OrthoNormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 */
	public OrthoNormalBasis33F getOrthoNormalBasisG() {
		return this.orthoNormalBasisG;
	}
	
	/**
	 * Returns the {@link OrthoNormalBasis33F} instance that is used as the orthonormal basis for shading.
	 * 
	 * @return the {@code OrthoNormalBasis33F} instance that is used as the orthonormal basis for shading
	 */
	public OrthoNormalBasis33F getOrthoNormalBasisS() {
		return this.orthoNormalBasisS;
	}
	
	/**
	 * Returns the {@link Point2F} instance that is used as the texture coordinates.
	 * 
	 * @return the {@code Point2F} instance that is used as the texture coordinates
	 */
	public Point2F getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	/**
	 * Returns the {@link Point3F} instance that is used as the surface intersection point.
	 * 
	 * @return the {@code Point3F} instance that is used as the surface intersection point
	 */
	public Point3F getSurfaceIntersectionPoint() {
		return this.surfaceIntersectionPoint;
	}
	
	/**
	 * Returns the {@link Ray3F} instance that was used in the intersection operation.
	 * 
	 * @return the {@code Ray3F} instance that was used in the intersection operation
	 */
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns the {@link Shape3F} instance that was intersected.
	 * 
	 * @return the {@code Shape3F} instance that was intersected
	 */
	public Shape3F getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceIntersection3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceIntersection3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new SurfaceIntersection3F(%s, %s, %s, %s, %s, %s, %s, %s, %+.10f)", this.orthoNormalBasisG, this.orthoNormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
	}
	
	/**
	 * Returns the {@link Vector3F} instance that is used as the surface normal for the geometry.
	 * 
	 * @return the {@code Vector3F} instance that is used as the surface normal for the geometry
	 */
	public Vector3F getSurfaceNormalG() {
		return this.surfaceNormalG;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that is used as the surface normal for shading.
	 * 
	 * @return the {@code Vector3F} instance that is used as the surface normal for shading
	 */
	public Vector3F getSurfaceNormalS() {
		return this.surfaceNormalS;
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceIntersection3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceIntersection3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise
	 */
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
	
	/**
	 * Returns the parametric {@code t} value that represents the distance to the intersection.
	 * 
	 * @return the parametric {@code t} value that represents the distance to the intersection
	 */
	public float getT() {
		return this.t;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceIntersection3F} instance.
	 * 
	 * @return a hash code for this {@code SurfaceIntersection3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.orthoNormalBasisG, this.orthoNormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
	}
}