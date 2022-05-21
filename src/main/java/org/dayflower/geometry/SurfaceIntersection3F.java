/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code SurfaceIntersection3F} contains information about the surface of a {@link Shape3F} instance where a {@link Ray3F} instance intersects.
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@code Shape3F} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceIntersection3F {
	/**
	 * An empty {@code Optional} instance.
	 */
//	TODO: Add Unit Tests!
	public static final Optional<SurfaceIntersection3F> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final OrthonormalBasis33F orthonormalBasisG;
	private final OrthonormalBasis33F orthonormalBasisS;
	private final Point2F textureCoordinates;
	private final Point3F surfaceIntersectionPoint;
	private final Ray3F ray;
	private final Shape3F shape;
	private final Vector3F surfaceIntersectionPointError;
	private final float t;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceIntersection3F} instance.
	 * <p>
	 * If either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape} or {@code surfaceIntersectionPointError} are {@code null}, a {@code NullPointerException}
	 * will be thrown.
	 * 
	 * @param orthonormalBasisG the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 * @param orthonormalBasisS the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading
	 * @param textureCoordinates the {@link Point2F} instance that is used as the texture coordinates
	 * @param surfaceIntersectionPoint the {@link Point3F} instance that is used as the surface intersection point
	 * @param ray the {@link Ray3F} instance that was used in the intersection operation
	 * @param shape the {@link Shape3F} instance that was intersected
	 * @param surfaceIntersectionPointError the {@link Vector3F} instance that contains the floating-point precision error of {@code surfaceIntersectionPoint}
	 * @param t the parametric {@code t} value that represents the distance to the intersection
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape} or {@code surfaceIntersectionPointError}
	 *                              are {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersection3F(final OrthonormalBasis33F orthonormalBasisG, final OrthonormalBasis33F orthonormalBasisS, final Point2F textureCoordinates, final Point3F surfaceIntersectionPoint, final Ray3F ray, final Shape3F shape, final Vector3F surfaceIntersectionPointError, final float t) {
		this.orthonormalBasisG = Objects.requireNonNull(orthonormalBasisG, "orthonormalBasisG == null");
		this.orthonormalBasisS = Objects.requireNonNull(orthonormalBasisS, "orthonormalBasisS == null");
		this.textureCoordinates = Objects.requireNonNull(textureCoordinates, "textureCoordinates == null");
		this.surfaceIntersectionPoint = Objects.requireNonNull(surfaceIntersectionPoint, "surfaceIntersectionPoint == null");
		this.ray = Objects.requireNonNull(ray, "ray == null");
		this.shape = Objects.requireNonNull(shape, "shape == null");
		this.surfaceIntersectionPointError = Objects.requireNonNull(surfaceIntersectionPointError, "surfaceIntersectionPointError == null");
		this.t = t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for the geometry
	 */
//	TODO: Add Unit Tests!
	public OrthonormalBasis33F getOrthonormalBasisG() {
		return this.orthonormalBasisG;
	}
	
	/**
	 * Returns the {@link OrthonormalBasis33F} instance that is used as the orthonormal basis for shading.
	 * 
	 * @return the {@code OrthonormalBasis33F} instance that is used as the orthonormal basis for shading
	 */
//	TODO: Add Unit Tests!
	public OrthonormalBasis33F getOrthonormalBasisS() {
		return this.orthonormalBasisS;
	}
	
	/**
	 * Returns the {@link Point2F} instance that is used as the texture coordinates.
	 * 
	 * @return the {@code Point2F} instance that is used as the texture coordinates
	 */
//	TODO: Add Unit Tests!
	public Point2F getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	/**
	 * Returns the {@link Point3F} instance that is used as the surface intersection point.
	 * 
	 * @return the {@code Point3F} instance that is used as the surface intersection point
	 */
//	TODO: Add Unit Tests!
	public Point3F getSurfaceIntersectionPoint() {
		return this.surfaceIntersectionPoint;
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction towards {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return a new {@code Ray3F} in the direction towards {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3F createRay(final Point3F point) {
		return createRay(point, getSurfaceNormalS());
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction towards {@code point} using the surface normal {@code surfaceNormal}.
	 * <p>
	 * If either {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @param surfaceNormal a {@code Vector3F} instance with the surface normal
	 * @return a new {@code Ray3F} in the direction towards {@code point} using the surface normal {@code surfaceNormal}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code surfaceNormal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3F createRay(final Point3F point, final Vector3F surfaceNormal) {
		return createRay(Vector3F.direction(this.surfaceIntersectionPoint, point), surfaceNormal);
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance with the direction
	 * @return a new {@code Ray3F} in the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3F createRay(final Vector3F direction) {
		return createRay(direction, getSurfaceNormalS());
	}
	
	/**
	 * Returns a new {@link Ray3F} in the direction {@code direction} using the surface normal {@code surfaceNormal}.
	 * <p>
	 * If either {@code direction} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3F} instance with the direction
	 * @param surfaceNormal a {@code Vector3F} instance with the surface normal
	 * @return a new {@code Ray3F} in the direction {@code direction} using the surface normal {@code surfaceNormal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code surfaceNormal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3F createRay(final Vector3F direction, final Vector3F surfaceNormal) {
		return new Ray3F(Point3F.offset(this.surfaceIntersectionPoint, direction, surfaceNormal, this.surfaceIntersectionPointError), direction);
	}
	
	/**
	 * Returns the {@link Ray3F} instance that was used in the intersection operation.
	 * 
	 * @return the {@code Ray3F} instance that was used in the intersection operation
	 */
//	TODO: Add Unit Tests!
	public Ray3F getRay() {
		return this.ray;
	}
	
	/**
	 * Returns the {@link Shape3F} instance that was intersected.
	 * 
	 * @return the {@code Shape3F} instance that was intersected
	 */
//	TODO: Add Unit Tests!
	public Shape3F getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceIntersection3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceIntersection3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new SurfaceIntersection3F(%s, %s, %s, %s, %s, %s, %s, %+.10f)", this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceIntersectionPointError, Float.valueOf(this.t));
	}
	
	/**
	 * Returns the {@link Vector3F} instance that contains the floating-point precision error of the surface intersection point.
	 * 
	 * @return the {@code Vector3F} instance that contains the floating-point precision error of the surface intersection point
	 */
//	TODO: Add Unit Tests!
	public Vector3F getSurfaceIntersectionPointError() {
		return this.surfaceIntersectionPointError;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for the geometry.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersection.getOrthonormalBasisG().w;
	 * }
	 * </pre>
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for the geometry
	 */
//	TODO: Add Unit Tests!
	public Vector3F getSurfaceNormalG() {
		return this.orthonormalBasisG.w;
	}
	
	/**
	 * Returns the {@link Vector3F} instance that represents the surface normal for shading.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersection.getOrthonormalBasisS().w;
	 * }
	 * </pre>
	 * 
	 * @return the {@code Vector3F} instance that represents the surface normal for shading
	 */
//	TODO: Add Unit Tests!
	public Vector3F getSurfaceNormalS() {
		return this.orthonormalBasisS.w;
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceIntersection3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceIntersection3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
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
		} else if(!Objects.equals(this.surfaceIntersectionPointError, SurfaceIntersection3F.class.cast(object).surfaceIntersectionPointError)) {
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
//	TODO: Add Unit Tests!
	public float getT() {
		return this.t;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceIntersection3F} instance.
	 * 
	 * @return a hash code for this {@code SurfaceIntersection3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceIntersectionPointError, Float.valueOf(this.t));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the closest {@code SurfaceIntersection3F} instance.
	 * <p>
	 * If either {@code optionalSurfaceIntersectionA} or {@code optionalSurfaceIntersectionB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param optionalSurfaceIntersectionA an {@code Optional} with an optional {@code SurfaceIntersection3F} instance
	 * @param optionalSurfaceIntersectionB an {@code Optional} with an optional {@code SurfaceIntersection3F} instance
	 * @return the closest {@code SurfaceIntersection3F} instance
	 * @throws NullPointerException thrown if, and only if, either {@code optionalSurfaceIntersectionA} or {@code optionalSurfaceIntersectionB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Optional<SurfaceIntersection3F> closest(final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionA, final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionB) {
		final SurfaceIntersection3F surfaceIntersectionA = optionalSurfaceIntersectionA.orElse(null);
		final SurfaceIntersection3F surfaceIntersectionB = optionalSurfaceIntersectionB.orElse(null);
		
		if(surfaceIntersectionA != null && surfaceIntersectionB != null) {
			return surfaceIntersectionA.getT() <= surfaceIntersectionB.getT() ? optionalSurfaceIntersectionA : optionalSurfaceIntersectionB;
		} else if(surfaceIntersectionA != null) {
			return optionalSurfaceIntersectionA;
		} else if(surfaceIntersectionB != null) {
			return optionalSurfaceIntersectionB;
		} else {
			return EMPTY;
		}
	}
	
	/**
	 * Orients {@code surfaceIntersection} based on {@code direction}.
	 * <p>
	 * Returns {@code surfaceIntersection} or an oriented version of it.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection a {@code SurfaceIntersection3F} instance
	 * @param direction a {@link Vector3F} instance
	 * @return {@code surfaceIntersection} or an oriented version of it
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code direction} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceIntersection3F orient(final SurfaceIntersection3F surfaceIntersection, final Vector3F direction) {
		if(Vector3F.dotProduct(direction, surfaceIntersection.getOrthonormalBasisG().w) >= 0.0F) {
			final OrthonormalBasis33F orthonormalBasisG = OrthonormalBasis33F.flipW(surfaceIntersection.orthonormalBasisG);
			final OrthonormalBasis33F orthonormalBasisS = OrthonormalBasis33F.flipW(surfaceIntersection.orthonormalBasisS);
			
			final Point2F textureCoordinates = surfaceIntersection.textureCoordinates;
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.surfaceIntersectionPoint;
			
			final Ray3F ray = surfaceIntersection.ray;
			
			final Shape3F shape = surfaceIntersection.shape;
			
			final Vector3F surfaceIntersectionPointError = surfaceIntersection.surfaceIntersectionPointError;
			
			final float t = surfaceIntersection.t;
			
			return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, shape, surfaceIntersectionPointError, t);
		}
		
		return surfaceIntersection;
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceIntersection3F} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code matrix} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
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
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code matrix} are {@code null}
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static SurfaceIntersection3F transform(final SurfaceIntersection3F surfaceIntersection, final Matrix44F matrix, final Matrix44F matrixInverse) {
		final OrthonormalBasis33F orthonormalBasisGOldSpace = surfaceIntersection.orthonormalBasisG;
		final OrthonormalBasis33F orthonormalBasisSOldSpace = surfaceIntersection.orthonormalBasisS;
		final OrthonormalBasis33F orthonormalBasisGNewSpace = OrthonormalBasis33F.transformTranspose(matrixInverse, orthonormalBasisGOldSpace);
		final OrthonormalBasis33F orthonormalBasisSNewSpace = OrthonormalBasis33F.transformTranspose(matrixInverse, orthonormalBasisSOldSpace);
		final OrthonormalBasis33F orthonormalBasisSNewSpaceCorrectlyOriented = Vector3F.dotProduct(orthonormalBasisSNewSpace.w, orthonormalBasisGNewSpace.w) < 0.0F ? OrthonormalBasis33F.flipW(orthonormalBasisSNewSpace) : orthonormalBasisSNewSpace;
		
		final Point2F textureCoordinates = surfaceIntersection.textureCoordinates;
		
		final Point3F surfaceIntersectionPointOldSpace = surfaceIntersection.surfaceIntersectionPoint;
		final Point3F surfaceIntersectionPointNewSpace = Point3F.transformAndDivide(matrix, surfaceIntersectionPointOldSpace);
		
		final Ray3F rayOldSpace = surfaceIntersection.ray;
		final Ray3F rayNewSpace = Ray3F.transform(matrix, rayOldSpace);
		
		final Shape3F shape = surfaceIntersection.shape;
		
		final Vector3F surfaceIntersectionPointErrorOldSpace = surfaceIntersection.surfaceIntersectionPointError;
		final Vector3F surfaceIntersectionPointErrorNewSpace = Vector3F.transformError(matrix, surfaceIntersectionPointOldSpace, surfaceIntersectionPointErrorOldSpace);
		
		final float tNewSpace = abs(Point3F.distance(rayNewSpace.getOrigin(), surfaceIntersectionPointNewSpace));
		
		return new SurfaceIntersection3F(orthonormalBasisGNewSpace, orthonormalBasisSNewSpaceCorrectlyOriented, textureCoordinates, surfaceIntersectionPointNewSpace, rayNewSpace, shape, surfaceIntersectionPointErrorNewSpace, tNewSpace);
	}
}