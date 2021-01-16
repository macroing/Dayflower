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
package org.dayflower.geometry.shape;

import static org.dayflower.util.Doubles.PI;
import static org.dayflower.util.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Doubles.PI_MULTIPLIED_BY_4;
import static org.dayflower.util.Doubles.abs;
import static org.dayflower.util.Doubles.asinpi;
import static org.dayflower.util.Doubles.atan2;
import static org.dayflower.util.Doubles.equal;
import static org.dayflower.util.Doubles.gamma;
import static org.dayflower.util.Doubles.isNaN;
import static org.dayflower.util.Doubles.max;
import static org.dayflower.util.Doubles.pow;
import static org.dayflower.util.Doubles.solveQuadraticSystem;
import static org.dayflower.util.Doubles.sqrt;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.SampleGeneratorD;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.SurfaceSample3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.BoundingSphere3D;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Sphere3D} denotes a 3-dimensional sphere that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Sphere3D implements Shape3D {
	/**
	 * The name of this {@code Sphere3D} class.
	 */
	public static final String NAME = "Sphere";
	
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the {@link Point3D} instance representing the center in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_CENTER = 0;
	
	/**
	 * The offset for the radius in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS = 3;
	
	/**
	 * The ID of this {@code Sphere3D} class.
	 */
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3D center;
	private final double radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Sphere3D} instance with a radius of {@code 1.0D} and a center of {@code new Point3D()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sphere3D(1.0D);
	 * }
	 * </pre>
	 */
	public Sphere3D() {
		this(1.0D);
	}
	
	/**
	 * Constructs a new {@code Sphere3D} instance with a radius of {@code radius} and a center of {@code new Point3D()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sphere3D(radius, new Point3D());
	 * }
	 * </pre>
	 * 
	 * @param radius the radius of this {@code Sphere3D} instance
	 */
	public Sphere3D(final double radius) {
		this(radius, new Point3D());
	}
	
	/**
	 * Constructs a new {@code Sphere3D} instance with a radius of {@code radius} and a center of {@code center}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radius the radius of this {@code Sphere3D} instance
	 * @param center the center of this {@code Sphere3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Sphere3D(final double radius, final Point3D center) {
		this.center = Point3D.getCached(Objects.requireNonNull(center, "center == null"));
		this.radius = radius;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Sphere3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Sphere3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new BoundingSphere3D(this.radius, this.center);
	}
	
	/**
	 * Samples this {@code Sphere3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Sphere3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Sphere3D} instance
	 * @param u a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @param v a random {@code double} with a uniform distribution between {@code 0.0D} and {@code 1.0D}
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3D> sample(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final double u, final double v) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3D center = getCenter();
		
		final Vector3D directionToCenter = Vector3D.direction(referencePoint, center);
		
		final double lengthSquared = directionToCenter.lengthSquared();
		final double radius = getRadius();
		final double radiusSquared = getRadiusSquared();
		
		if(lengthSquared < radiusSquared * 1.00001D) {
			final Vector3D surfaceNormal = SampleGeneratorD.sampleSphereUniformDistribution(u, v);
			
			final Point3D point = Point3D.add(center, surfaceNormal, radius);
			
			final Vector3D directionToSurface = Vector3D.direction(point, referencePoint);
//			final Vector3D directionToSurface = Vector3D.direction(referencePoint, point);
			final Vector3D directionToSurfaceNormalized = Vector3D.normalize(directionToSurface);
			
//			final double probabilityDensityFunctionValue = (1.0D / getSurfaceArea()) * (directionToSurface.lengthSquared() / abs(Vector3D.dotProduct(Vector3D.negate(directionToSurfaceNormalized), surfaceNormal)));
			final double probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3D.dotProduct(directionToSurfaceNormalized, surfaceNormal));
			
			return Optional.of(new SurfaceSample3D(point, surfaceNormal, probabilityDensityFunctionValue));
		}
		
		final double sinThetaMaxSquared = radiusSquared / lengthSquared;
		final double cosThetaMax = sqrt(max(0.0D, 1.0D - sinThetaMaxSquared));
		
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(directionToCenter);
		
		final Vector3D coneLocalSpace = SampleGeneratorD.sampleConeUniformDistribution(u, v, cosThetaMax);
		final Vector3D coneGlobalSpace = Vector3D.normalize(Vector3D.transform(coneLocalSpace, orthonormalBasis));
		
		final Ray3D ray = new Ray3D(referencePoint, coneGlobalSpace);
		
//		TODO: Check if these variables should be supplied as parameters?
		final double tMinimum = 0.001D;
		final double tMaximum = Double.MAX_VALUE;
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = intersection(ray, tMinimum, tMaximum);
		
		final double t = optionalSurfaceIntersection.isPresent() ? optionalSurfaceIntersection.get().getT() : Vector3D.dotProduct(directionToCenter, coneGlobalSpace);
		
		final Point3D point = Point3D.add(ray.getOrigin(), ray.getDirection(), t);
		
		final Vector3D surfaceNormal = Vector3D.directionNormalized(center, point);
		
		final double probabilityDensityFunctionValue = SampleGeneratorD.coneUniformDistributionProbabilityDensityFunction(cosThetaMax);
		
		return Optional.of(new SurfaceSample3D(point, surfaceNormal, probabilityDensityFunctionValue));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Sphere3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Sphere3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		final Point3D center = this.center;
		
		final Vector3D direction = ray.getDirection();
		final Vector3D centerToOrigin = Vector3D.direction(center, origin);
		
		final double radius = this.radius;
		final double radiusSquared = radius * radius;
		
		final double a = direction.lengthSquared();
		final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
		final double c = centerToOrigin.lengthSquared() - radiusSquared;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D surfaceIntersectionPoint = Point3D.add(origin, direction, t);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(Vector3D.directionNormalized(center, surfaceIntersectionPoint));
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = new Point2D(0.5D + atan2(orthonormalBasisG.getW().getZ(), orthonormalBasisG.getW().getX()) * PI_MULTIPLIED_BY_2_RECIPROCAL, 0.5D - asinpi(orthonormalBasisG.getW().getY()));
		
		final Vector3D surfaceIntersectionPointError = Vector3D.multiply(Vector3D.absolute(new Vector3D(surfaceIntersectionPoint)), gamma(5));
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns the center of this {@code Sphere3D} instance.
	 * 
	 * @return the center of this {@code Sphere3D} instance
	 */
	public Point3D getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Sphere3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Sphere3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Sphere3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Sphere3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Sphere3D(%+.10f, %s)", Double.valueOf(this.radius), this.center);
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(!this.center.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Sphere3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Sphere3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Sphere3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Sphere3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Sphere3D)) {
			return false;
		} else if(!Objects.equals(this.center, Sphere3D.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, Sphere3D.class.cast(object).radius)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Sphere3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Sphere3D} instance
	 * @param point the point on this {@code Sphere3D} instance
	 * @param surfaceNormal the surface normal on this {@code Sphere3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public double calculateProbabilityDensityFunctionValueForSolidAngle(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Point3D point, final Vector3D surfaceNormal) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3D center = this.center;
		
		final Vector3D directionToCenter = Vector3D.direction(referencePoint, center);
		
		final double lengthSquared = directionToCenter.lengthSquared();
		final double radius = this.radius;
		final double radiusSquared = radius * radius;
		
		if(lengthSquared < radiusSquared * 1.00001D) {
			final Vector3D directionToSurface = Vector3D.direction(point, referencePoint);
			final Vector3D directionToSurfaceNormalized = Vector3D.normalize(directionToSurface);
			
			final double probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3D.dotProduct(directionToSurfaceNormalized, surfaceNormal));
			
			return probabilityDensityFunctionValue;
		}
		
		final double sinThetaMaxSquared = radiusSquared / lengthSquared;
		final double cosThetaMax = sqrt(max(0.0D, 1.0D - sinThetaMaxSquared));
		final double probabilityDensityFunctionValue = SampleGeneratorD.coneUniformDistributionProbabilityDensityFunction(cosThetaMax);
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Sphere3D} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Sphere3D} instance
	 * @param direction the direction to this {@code Sphere3D} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public double calculateProbabilityDensityFunctionValueForSolidAngle(final Point3D referencePoint, final Vector3D referenceSurfaceNormal, final Vector3D direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
//		TODO: Check if these variables should be supplied as parameters?
		final double tMinimum = 0.001D;
		final double tMaximum = Double.MAX_VALUE;
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersection = intersection(new Ray3D(referencePoint, direction), tMinimum, tMaximum);
		
		if(optionalSurfaceIntersection.isPresent()) {
			final SurfaceIntersection3D surfaceIntersection = optionalSurfaceIntersection.get();
			
			final Point3D point = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3D surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
			
			return calculateProbabilityDensityFunctionValueForSolidAngle(referencePoint, referenceSurfaceNormal, point, surfaceNormal);
		}
		
		return 0.0D;
	}
	
	/**
	 * Returns the radius of this {@code Sphere3D} instance.
	 * 
	 * @return the radius of this {@code Sphere3D} instance
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the squared radius of this {@code Sphere3D} instance.
	 * 
	 * @return the squared radius of this {@code Sphere3D} instance
	 */
	public double getRadiusSquared() {
		return this.radius * this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code Sphere3D} instance.
	 * 
	 * @return the surface area of this {@code Sphere3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return PI_MULTIPLIED_BY_4 * getRadiusSquared();
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Sphere3D} instance.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Sphere3D} instance
	 */
	@Override
	public double getSurfaceAreaProbabilityDensityFunctionValue() {
		return 3.0D / getSurfaceArea();
	}
	
	/**
	 * Returns the volume of this {@code Sphere3D} instance.
	 * 
	 * @return the volume of this {@code Sphere3D} instance
	 */
	@Override
	public double getVolume() {
		return 4.0D / 3.0D * PI * pow(this.radius, 3.0D);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Sphere3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Sphere3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		final Point3D center = this.center;
		
		final Vector3D direction = ray.getDirection();
//		final Vector3D centerToOrigin = Vector3D.direction(center, origin);
		
//		final double radius = this.radius;
//		final double radiusSquared = radius * radius;
		
//		final double a = direction.lengthSquared();
//		final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
//		final double c = centerToOrigin.lengthSquared() - radiusSquared;
		
//		The code below resulted in an optimization, but not in the intersection(Ray3D, double, double) method:
		final double originX = origin.getX();
		final double originY = origin.getY();
		final double originZ = origin.getZ();
		
		final double centerX = center.getX();
		final double centerY = center.getY();
		final double centerZ = center.getZ();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		final double directionZ = direction.getZ();
		
		final double centerToOriginX = originX - centerX;
		final double centerToOriginY = originY - centerY;
		final double centerToOriginZ = originZ - centerZ;
		
		final double radius = this.radius;
		final double radiusSquared = radius * radius;
		
		final double a = directionX * directionX + directionY * directionY + directionZ * directionZ;
		final double b = 2.0D * (centerToOriginX * directionX + centerToOriginY * directionY + centerToOriginZ * directionZ);
		final double c = (centerToOriginX * centerToOriginX + centerToOriginY * centerToOriginY + centerToOriginZ * centerToOriginZ) - radiusSquared;
//		The code above resulted in an optimization, but not in the intersection(Ray3D, double, double) method.
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Sphere3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Sphere3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_CENTER + 0] = this.center.getX();
		array[ARRAY_OFFSET_CENTER + 1] = this.center.getY();
		array[ARRAY_OFFSET_CENTER + 2] = this.center.getZ();
		array[ARRAY_OFFSET_RADIUS] = this.radius;
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Sphere3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Sphere3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Sphere3D} instance.
	 * 
	 * @return a hash code for this {@code Sphere3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Double.valueOf(this.radius));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	private double doIntersectionTAnalytic(final Ray3D ray, final double tMinimum, final double tMaximum) {
//		final Point3D origin = ray.getOrigin();
//		final Point3D center = getCenter();
//		
//		final Vector3D direction = ray.getDirection();
//		final Vector3D centerToOrigin = Vector3D.direction(center, origin);
//		
//		final double radiusSquared = getRadiusSquared();
//		
//		final double a = direction.lengthSquared();
//		final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
//		final double c = centerToOrigin.lengthSquared() - radiusSquared;
//		
//		final double[] ts = solveQuadraticSystem(a, b, c);
//		
//		final double t0 = ts[0];
//		final double t1 = ts[1];
//		
//		final double t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
//		
//		return t;
//	}
	
//	private double doIntersectionTGeometric1(final Ray3D ray, final double tMinimum, final double tMaximum) {
//		final Point3D origin = ray.getOrigin();
//		final Point3D center = getCenter();
//		
//		final Vector3D direction = ray.getDirection();
//		final Vector3D originToCenter = Vector3D.direction(origin, center);
//		
//		final double radiusSquared = getRadiusSquared();
//		
//		final double b = Vector3D.dotProduct(originToCenter, direction);
//		
//		final double discriminantSquared = originToCenter.lengthSquared() - b * b;
//		
//		if(discriminantSquared > radiusSquared) {
//			return Double.NaN;
//		}
//		
//		final double discriminant = sqrt(radiusSquared - discriminantSquared);
//		
//		final double t0 = min(b - discriminant, b + discriminant);
//		final double t1 = max(b - discriminant, b + discriminant);
//		
//		final double t = t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
//		
//		return t;
//	}
	
//	private double doIntersectionTGeometric2(final Ray3D ray, final double tMinimum, final double tMaximum) {
//		final Point3D origin = ray.getOrigin();
//		final Point3D center = getCenter();
//		
//		final Vector3D direction = ray.getDirection();
//		final Vector3D originToCenter = Vector3D.direction(origin, center);
//		
//		final double radiusSquared = getRadiusSquared();
//		
//		final double b = Vector3D.dotProduct(originToCenter, direction);
//		
//		final double discriminantSquared = b * b - originToCenter.lengthSquared() + radiusSquared;
//		
//		if(discriminantSquared < 0.0D) {
//			return Double.NaN;
//		}
//		
//		final double discriminant = sqrt(discriminantSquared);
//		
//		final double t0 = min(b - discriminant, b + discriminant);
//		final double t1 = max(b - discriminant, b + discriminant);
//		
//		final double t = t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
//		
//		return t;
//	}
}