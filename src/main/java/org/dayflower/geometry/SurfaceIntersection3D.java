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

import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.equal;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code SurfaceIntersection3D} contains information about the surface of a {@link Shape3D} instance where a {@link Ray3D} instance intersects.
 * <p>
 * This class can be considered immutable and thread-safe if, and only if, its associated {@code Shape3D} instance is.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SurfaceIntersection3D {
	/**
	 * An empty {@code Optional} instance.
	 */
//	TODO: Add Unit Tests!
	public static final Optional<SurfaceIntersection3D> EMPTY = Optional.empty();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final OrthonormalBasis33D orthonormalBasisG;
	private final OrthonormalBasis33D orthonormalBasisS;
	private final Point2D textureCoordinates;
	private final Point3D surfaceIntersectionPoint;
	private final Ray3D ray;
	private final Shape3D shape;
	private final Vector3D surfaceIntersectionPointError;
	private final double t;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SurfaceIntersection3D} instance.
	 * <p>
	 * If either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape} or {@code surfaceIntersectionPointError} are {@code null}, a {@code NullPointerException}
	 * will be thrown.
	 * 
	 * @param orthonormalBasisG the {@link OrthonormalBasis33D} instance that is used as the orthonormal basis for the geometry
	 * @param orthonormalBasisS the {@code OrthonormalBasis33D} instance that is used as the orthonormal basis for shading
	 * @param textureCoordinates the {@link Point2D} instance that is used as the texture coordinates
	 * @param surfaceIntersectionPoint the {@link Point3D} instance that is used as the surface intersection point
	 * @param ray the {@link Ray3D} instance that was used in the intersection operation
	 * @param shape the {@link Shape3D} instance that was intersected
	 * @param surfaceIntersectionPointError the {@link Vector3D} instance that contains the floating-point precision error of {@code surfaceIntersectionPoint}
	 * @param t the parametric {@code t} value that represents the distance to the intersection
	 * @throws NullPointerException thrown if, and only if, either {@code orthonormalBasisG}, {@code orthonormalBasisS}, {@code textureCoordinates}, {@code surfaceIntersectionPoint}, {@code ray}, {@code shape} or {@code surfaceIntersectionPointError}
	 *                              are {@code null}
	 */
//	TODO: Add Unit Tests!
	public SurfaceIntersection3D(final OrthonormalBasis33D orthonormalBasisG, final OrthonormalBasis33D orthonormalBasisS, final Point2D textureCoordinates, final Point3D surfaceIntersectionPoint, final Ray3D ray, final Shape3D shape, final Vector3D surfaceIntersectionPointError, final double t) {
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
	 * Returns the {@link OrthonormalBasis33D} instance that is used as the orthonormal basis for the geometry.
	 * 
	 * @return the {@code OrthonormalBasis33D} instance that is used as the orthonormal basis for the geometry
	 */
//	TODO: Add Unit Tests!
	public OrthonormalBasis33D getOrthonormalBasisG() {
		return this.orthonormalBasisG;
	}
	
	/**
	 * Returns the {@link OrthonormalBasis33D} instance that is used as the orthonormal basis for shading.
	 * 
	 * @return the {@code OrthonormalBasis33D} instance that is used as the orthonormal basis for shading
	 */
//	TODO: Add Unit Tests!
	public OrthonormalBasis33D getOrthonormalBasisS() {
		return this.orthonormalBasisS;
	}
	
	/**
	 * Returns the {@link Point2D} instance that is used as the texture coordinates.
	 * 
	 * @return the {@code Point2D} instance that is used as the texture coordinates
	 */
//	TODO: Add Unit Tests!
	public Point2D getTextureCoordinates() {
		return this.textureCoordinates;
	}
	
	/**
	 * Returns the {@link Point3D} instance that is used as the surface intersection point.
	 * 
	 * @return the {@code Point3D} instance that is used as the surface intersection point
	 */
//	TODO: Add Unit Tests!
	public Point3D getSurfaceIntersectionPoint() {
		return this.surfaceIntersectionPoint;
	}
	
	/**
	 * Returns a new {@link Ray3D} in the direction towards {@code point}.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return a new {@code Ray3D} in the direction towards {@code point}
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3D createRay(final Point3D point) {
		return createRay(point, getSurfaceNormalS());
	}
	
	/**
	 * Returns a new {@link Ray3D} in the direction towards {@code point} using the surface normal {@code surfaceNormal}.
	 * <p>
	 * If either {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @param surfaceNormal a {@code Vector3D} instance with the surface normal
	 * @return a new {@code Ray3D} in the direction towards {@code point} using the surface normal {@code surfaceNormal}
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code surfaceNormal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3D createRay(final Point3D point, final Vector3D surfaceNormal) {
		return createRay(Vector3D.direction(this.surfaceIntersectionPoint, point), surfaceNormal);
	}
	
	/**
	 * Returns a new {@link Ray3D} in the direction {@code direction}.
	 * <p>
	 * If {@code direction} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3D} instance with the direction
	 * @return a new {@code Ray3D} in the direction {@code direction}
	 * @throws NullPointerException thrown if, and only if, {@code direction} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3D createRay(final Vector3D direction) {
		return createRay(direction, getSurfaceNormalS());
	}
	
	/**
	 * Returns a new {@link Ray3D} in the direction {@code direction} using the surface normal {@code surfaceNormal}.
	 * <p>
	 * If either {@code direction} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param direction a {@link Vector3D} instance with the direction
	 * @param surfaceNormal a {@code Vector3D} instance with the surface normal
	 * @return a new {@code Ray3D} in the direction {@code direction} using the surface normal {@code surfaceNormal}
	 * @throws NullPointerException thrown if, and only if, either {@code direction} or {@code surfaceNormal} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Ray3D createRay(final Vector3D direction, final Vector3D surfaceNormal) {
		return new Ray3D(Point3D.offset(this.surfaceIntersectionPoint, direction, surfaceNormal, this.surfaceIntersectionPointError), direction);
	}
	
	/**
	 * Returns the {@link Ray3D} instance that was used in the intersection operation.
	 * 
	 * @return the {@code Ray3D} instance that was used in the intersection operation
	 */
//	TODO: Add Unit Tests!
	public Ray3D getRay() {
		return this.ray;
	}
	
	/**
	 * Returns the {@link Shape3D} instance that was intersected.
	 * 
	 * @return the {@code Shape3D} instance that was intersected
	 */
//	TODO: Add Unit Tests!
	public Shape3D getShape() {
		return this.shape;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SurfaceIntersection3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code SurfaceIntersection3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new SurfaceIntersection3D(%s, %s, %s, %s, %s, %s, %s, %+.10f)", this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceIntersectionPointError, Double.valueOf(this.t));
	}
	
	/**
	 * Returns the {@link Vector3D} instance that contains the floating-point precision error of the surface intersection point.
	 * 
	 * @return the {@code Vector3D} instance that contains the floating-point precision error of the surface intersection point
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceIntersectionPointError() {
		return this.surfaceIntersectionPointError;
	}
	
	/**
	 * Returns the {@link Vector3D} instance that represents the surface normal for the geometry.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersection.getOrthonormalBasisG().w;
	 * }
	 * </pre>
	 * 
	 * @return the {@code Vector3D} instance that represents the surface normal for the geometry
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormalG() {
		return this.orthonormalBasisG.w;
	}
	
	/**
	 * Returns the {@link Vector3D} instance that represents the surface normal for shading.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * surfaceIntersection.getOrthonormalBasisS().w;
	 * }
	 * </pre>
	 * 
	 * @return the {@code Vector3D} instance that represents the surface normal for shading
	 */
//	TODO: Add Unit Tests!
	public Vector3D getSurfaceNormalS() {
		return this.orthonormalBasisS.w;
	}
	
	/**
	 * Compares {@code object} to this {@code SurfaceIntersection3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SurfaceIntersection3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SurfaceIntersection3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SurfaceIntersection3D)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasisG, SurfaceIntersection3D.class.cast(object).orthonormalBasisG)) {
			return false;
		} else if(!Objects.equals(this.orthonormalBasisS, SurfaceIntersection3D.class.cast(object).orthonormalBasisS)) {
			return false;
		} else if(!Objects.equals(this.textureCoordinates, SurfaceIntersection3D.class.cast(object).textureCoordinates)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionPoint, SurfaceIntersection3D.class.cast(object).surfaceIntersectionPoint)) {
			return false;
		} else if(!Objects.equals(this.ray, SurfaceIntersection3D.class.cast(object).ray)) {
			return false;
		} else if(!Objects.equals(this.shape, SurfaceIntersection3D.class.cast(object).shape)) {
			return false;
		} else if(!Objects.equals(this.surfaceIntersectionPointError, SurfaceIntersection3D.class.cast(object).surfaceIntersectionPointError)) {
			return false;
		} else if(!equal(this.t, SurfaceIntersection3D.class.cast(object).t)) {
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
	public double getT() {
		return this.t;
	}
	
	/**
	 * Returns a hash code for this {@code SurfaceIntersection3D} instance.
	 * 
	 * @return a hash code for this {@code SurfaceIntersection3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.orthonormalBasisG, this.orthonormalBasisS, this.textureCoordinates, this.surfaceIntersectionPoint, this.ray, this.shape, this.surfaceIntersectionPointError, Double.valueOf(this.t));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the closest {@code SurfaceIntersection3D} instance.
	 * <p>
	 * If either {@code optionalSurfaceIntersectionA} or {@code optionalSurfaceIntersectionB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param optionalSurfaceIntersectionA an {@code Optional} with an optional {@code SurfaceIntersection3D} instance
	 * @param optionalSurfaceIntersectionB an {@code Optional} with an optional {@code SurfaceIntersection3D} instance
	 * @return the closest {@code SurfaceIntersection3D} instance
	 * @throws NullPointerException thrown if, and only if, either {@code optionalSurfaceIntersectionA} or {@code optionalSurfaceIntersectionB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Optional<SurfaceIntersection3D> closest(final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionA, final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionB) {
		final SurfaceIntersection3D surfaceIntersectionA = optionalSurfaceIntersectionA.orElse(null);
		final SurfaceIntersection3D surfaceIntersectionB = optionalSurfaceIntersectionB.orElse(null);
		
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
	 * @param surfaceIntersection a {@code SurfaceIntersection3D} instance
	 * @param direction a {@link Vector3D} instance
	 * @return {@code surfaceIntersection} or an oriented version of it
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code direction} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceIntersection3D orient(final SurfaceIntersection3D surfaceIntersection, final Vector3D direction) {
		if(Vector3D.dotProduct(direction, surfaceIntersection.getOrthonormalBasisG().w) >= 0.0D) {
			final OrthonormalBasis33D orthonormalBasisG = OrthonormalBasis33D.flipW(surfaceIntersection.orthonormalBasisG);
			final OrthonormalBasis33D orthonormalBasisS = OrthonormalBasis33D.flipW(surfaceIntersection.orthonormalBasisS);
			
			final Point2D textureCoordinates = surfaceIntersection.textureCoordinates;
			
			final Point3D surfaceIntersectionPoint = surfaceIntersection.surfaceIntersectionPoint;
			
			final Ray3D ray = surfaceIntersection.ray;
			
			final Shape3D shape = surfaceIntersection.shape;
			
			final Vector3D surfaceIntersectionPointError = surfaceIntersection.surfaceIntersectionPointError;
			
			final double t = surfaceIntersection.t;
			
			return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, shape, surfaceIntersectionPointError, t);
		}
		
		return surfaceIntersection;
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceIntersection3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code matrix} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matrix} cannot be inverted, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * SurfaceIntersection3D.transform(surfaceIntersection, matrix, Matrix44D.inverse(matrix));
	 * }
	 * </pre>
	 * 
	 * @param surfaceIntersection the {@code SurfaceIntersection3D} instance to transform
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @return a new {@code SurfaceIntersection3D} instance with the result of the transformation
	 * @throws IllegalArgumentException thrown if, and only if, {@code matrix} cannot be inverted
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code matrix} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceIntersection3D transform(final SurfaceIntersection3D surfaceIntersection, final Matrix44D matrix) {
		return transform(surfaceIntersection, matrix, Matrix44D.inverse(matrix));
	}
	
	/**
	 * Performs a transformation.
	 * <p>
	 * Returns a new {@code SurfaceIntersection3D} instance with the result of the transformation.
	 * <p>
	 * If either {@code surfaceIntersection}, {@code matrix} or {@code matrixInverse} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection the {@code SurfaceIntersection3D} instance to transform
	 * @param matrix the {@link Matrix44D} instance to perform the transformation with
	 * @param matrixInverse the {@code Matrix44D} instance to perform the transformation with in inverse transpose order
	 * @return a new {@code SurfaceIntersection3D} instance with the result of the transformation
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection}, {@code matrix} or {@code matrixInverse} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static SurfaceIntersection3D transform(final SurfaceIntersection3D surfaceIntersection, final Matrix44D matrix, final Matrix44D matrixInverse) {
		final OrthonormalBasis33D orthonormalBasisGOldSpace = surfaceIntersection.orthonormalBasisG;
		final OrthonormalBasis33D orthonormalBasisSOldSpace = surfaceIntersection.orthonormalBasisS;
		final OrthonormalBasis33D orthonormalBasisGNewSpace = OrthonormalBasis33D.transformTranspose(matrixInverse, orthonormalBasisGOldSpace);
		final OrthonormalBasis33D orthonormalBasisSNewSpace = OrthonormalBasis33D.transformTranspose(matrixInverse, orthonormalBasisSOldSpace);
		final OrthonormalBasis33D orthonormalBasisSNewSpaceCorrectlyOriented = Vector3D.dotProduct(orthonormalBasisSNewSpace.w, orthonormalBasisGNewSpace.w) < 0.0D ? OrthonormalBasis33D.flipW(orthonormalBasisSNewSpace) : orthonormalBasisSNewSpace;
		
		final Point2D textureCoordinates = surfaceIntersection.textureCoordinates;
		
		final Point3D surfaceIntersectionPointOldSpace = surfaceIntersection.surfaceIntersectionPoint;
		final Point3D surfaceIntersectionPointNewSpace = Point3D.transformAndDivide(matrix, surfaceIntersectionPointOldSpace);
		
		final Ray3D rayOldSpace = surfaceIntersection.ray;
		final Ray3D rayNewSpace = Ray3D.transform(matrix, rayOldSpace);
		
		final Shape3D shape = surfaceIntersection.shape;
		
		final Vector3D surfaceIntersectionPointErrorOldSpace = surfaceIntersection.surfaceIntersectionPointError;
		final Vector3D surfaceIntersectionPointErrorNewSpace = Vector3D.transformError(matrix, surfaceIntersectionPointOldSpace, surfaceIntersectionPointErrorOldSpace);
		
		final double tNewSpace = abs(Point3D.distance(rayNewSpace.getOrigin(), surfaceIntersectionPointNewSpace));
		
		return new SurfaceIntersection3D(orthonormalBasisGNewSpace, orthonormalBasisSNewSpaceCorrectlyOriented, textureCoordinates, surfaceIntersectionPointNewSpace, rayNewSpace, shape, surfaceIntersectionPointErrorNewSpace, tNewSpace);
	}
}