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
	private final OrthonormalBasis33F orthonormalBasisG;
	private final OrthonormalBasis33F orthonormalBasisS;
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
	 * If either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape}, {@code surfaceNormalG} or {@code surfaceNormalS} are {@code null}, a
	 * {@code NullPointerException} will be thrown.
	 * 
	 * @param orthonormalBasisG the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 * @param orthonormalBasisS the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading
	 * @param textureCoordinates the {@link Point2F} instance that is used as the texture coordinates
	 * @param surfaceIntersectionPoint the {@link Point3F} instance that is used as the surface intersection point
	 * @param ray the {@link Ray3F} instance that was used in the intersection operation
	 * @param shape the {@link Shape3F} instance that was intersected
	 * @param surfaceNormalG the {@link Vector3F} instance that is used as the surface normal for the geometry
	 * @param surfaceNormalS the {@code Vector3F} instance that is used as the surface normal for shading
	 * @param t the parametric {@code t} value that represents the distance to the intersection
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape}, {@code surfaceNormalG} or
	 *                              {@code surfaceNormalS} are {@code null}
	 */
	public SurfaceIntersection3F(final OrthonormalBasis33F orthonormalBasisG, final OrthonormalBasis33F orthonormalBasisS, final Point2F textureCoordinates, final Point3F surfaceIntersectionPoint, final Ray3F ray, final Shape3F shape, final Vector3F surfaceNormalG, final Vector3F surfaceNormalS, final float t) {
		this.orthonormalBasisG = Objects.requireNonNull(orthonormalBasisG, "orthonormalBasisG == null");
		this.orthonormalBasisS = Objects.requireNonNull(orthonormalBasisS, "orthonormalBasisS == null");
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
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 */
	public OrthonormalBasis33F getOrthonormalBasisG() {
		return this.orthonormalBasisG;
	}
	
	/**
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for shading.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading
	 */
	public OrthonormalBasis33F getOrthonormalBasisS() {
		return this.orthonormalBasisS;
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
		return String.format("new SurfaceIntersection3F(%s, %s, %s, %s, %s, %s, %s, %s, %+.10f)", this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
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
		} else if(!Objects.equals(this.orthonormalBasisG, SurfaceIntersection3F.class.cast(object).orthonormalBasisG)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasisS, SurfaceIntersection3F.class.cast(object).orthonormalBasisS)) {
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
		return Objects.hash(this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceNormalG, this.surfaceNormalS, Float.valueOf(this.t));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceIntersection3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code matrix} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an IllegalStateException will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SurfaceIntersection3F.transform(surfaceIntersection, matrix, Matrix44F.inverse(matrix));
	 * }
	 * </pre>
	 * 
	 * @param surfaceIntersection the {@code SurfaceIntersection3F} instance to transform
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @return a new {@code SurfaceIntersection3F} instance with the result of the transformation
	 * @throws IllegalStateException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code matrix} are {@code null}
	 */
	public static SurfaceIntersection3F transform(final SurfaceIntersection3F surfaceIntersection, final Matrix44F matrix) {
		return transform(surfaceIntersection, matrix, Matrix44F.inverse(matrix));
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceIntersection3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceIntersection}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection the {@code SurfaceIntersection3F} instance to transform
	 * @param matrix the {@link Matrix44F} instance to perform the transformation with
	 * @param matrixInverse the {@code Matrix44F} instance to perform the transformation with in inverse transpose order
	 * @return a new {@code SurfaceIntersection3F} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection}, {@code matrix} or {@code matrixInverse} are {@code null}
	 */
	public static SurfaceIntersection3F transform(final SurfaceIntersection3F surfaceIntersection, final Matrix44F matrix, final Matrix44F matrixInverse) {
		final OrthonormalBasis33F orthonormalBasisGWorldSpace = surfaceIntersection.orthonormalBasisG;
		final OrthonormalBasis33F orthonormalBasisSWorldSpace = surfaceIntersection.orthonormalBasisS;
		final OrthonormalBasis33F orthonormalBasisGObjectSpace = OrthonormalBasis33F.transformTranspose(matrixInverse, orthonormalBasisGWorldSpace);
		final OrthonormalBasis33F orthonormalBasisSObjectSpace = OrthonormalBasis33F.transformTranspose(matrixInverse, orthonormalBasisSWorldSpace);
		
		final Point2F textureCoordinates = surfaceIntersection.textureCoordinates;
		
		final Point3F surfaceIntersectionPointWorldSpace = surfaceIntersection.surfaceIntersectionPoint;
		final Point3F surfaceIntersectionPointObjectSpace = Point3F.transform(matrix, surfaceIntersectionPointWorldSpace);
		
		final Ray3F rayWorldSpace = surfaceIntersection.ray;
		final Ray3F rayObjectSpace = Ray3F.transform(matrix, rayWorldSpace);
		
		final Shape3F shape = surfaceIntersection.shape;
		
		final Vector3F surfaceNormalGWorldSpace = surfaceIntersection.surfaceNormalG;
		final Vector3F surfaceNormalSWorldSpace = surfaceIntersection.surfaceNormalS;
		final Vector3F surfaceNormalGObjectSpace = Vector3F.transformTranspose(matrixInverse, surfaceNormalGWorldSpace);
		final Vector3F surfaceNormalSObjectSpace = Vector3F.transformTranspose(matrixInverse, surfaceNormalSWorldSpace);
		
		final float tObjectSpace = abs(Point3F.distance(rayObjectSpace.getOrigin(), surfaceIntersectionPointObjectSpace));
		
		return new SurfaceIntersection3F(orthonormalBasisGObjectSpace, orthonormalBasisSObjectSpace, textureCoordinates, surfaceIntersectionPointObjectSpace, rayObjectSpace, shape, surfaceNormalGObjectSpace, surfaceNormalSObjectSpace, tObjectSpace);
	}
}