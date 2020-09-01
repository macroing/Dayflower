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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_4;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.asinpi;
import static org.dayflower.util.Floats.atan2;
import static org.dayflower.util.Floats.coneUniformDistributionProbabilityDensityFunction;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.solveQuadraticSystem;
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@code Sphere3F} denotes a 3-dimensional sphere that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Sphere3F implements Shape3F {
	private final Point3F center;
	private final float radius;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Sphere3F} instance with a radius of {@code 1.0F} and a center of {@code new Point3F()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sphere3F(1.0F);
	 * }
	 * </pre>
	 */
	public Sphere3F() {
		this(1.0F);
	}
	
	/**
	 * Constructs a new {@code Sphere3F} instance with a radius of {@code radius} and a center of {@code new Point3F()}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sphere3F(radius, new Point3F());
	 * }
	 * </pre>
	 * 
	 * @param radius the radius of this {@code Sphere3F} instance
	 */
	public Sphere3F(final float radius) {
		this(radius, new Point3F());
	}
	
	/**
	 * Constructs a new {@code Sphere3F} instance with a radius of {@code radius} and a center of {@code center}.
	 * <p>
	 * If {@code center} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param radius the radius of this {@code Sphere3F} instance
	 * @param center the center of this {@code Sphere3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code center} is {@code null}
	 */
	public Sphere3F(final float radius, final Point3F center) {
		this.radius = radius;
		this.center = Objects.requireNonNull(center, "center == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Sphere3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Sphere3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new BoundingSphere3F(this.radius, this.center);
	}
	
	/**
	 * Samples this {@code Sphere3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param referencePoint the reference point on this {@code Sphere3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Sphere3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3F center = getCenter();
		
		final Vector3F directionToCenter = Vector3F.direction(referencePoint, center);
		
		final float lengthSquared = directionToCenter.lengthSquared();
		final float radius = getRadius();
		final float radiusSquared = getRadiusSquared();
		
		if(lengthSquared < radiusSquared * 1.00001F) {
			final Vector3F surfaceNormal = Vector3F.sampleSphereUniformDistribution(u, v);
			
			final Point3F point = Point3F.add(center, surfaceNormal, radius);
			
			final Vector3F directionToSurface = Vector3F.direction(point, referencePoint);
			final Vector3F directionToSurfaceNormalized = Vector3F.normalize(directionToSurface);
			
			final float probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3F.dotProduct(directionToSurfaceNormalized, surfaceNormal));
			
			return Optional.of(new SurfaceSample3F(point, surfaceNormal, probabilityDensityFunctionValue));
		}
		
		final float sinThetaMaxSquared = radiusSquared / lengthSquared;
		final float cosThetaMax = sqrt(max(0.0F, 1.0F - sinThetaMaxSquared));
		
		final OrthoNormalBasis33F orthoNormalBasis = new OrthoNormalBasis33F(directionToCenter);
		
		final Vector3F coneLocalSpace = Vector3F.sampleConeUniformDistribution(u, v, cosThetaMax);
		final Vector3F coneGlobalSpace = Vector3F.normalize(Vector3F.transform(coneLocalSpace, orthoNormalBasis));
		
		final Ray3F ray = new Ray3F(referencePoint, coneGlobalSpace);
		
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = intersection(ray);
		
		final float t = optionalSurfaceIntersection.isPresent() ? optionalSurfaceIntersection.get().getT() : Vector3F.dotProduct(directionToCenter, coneGlobalSpace);
		
		final Point3F point = Point3F.add(ray.getOrigin(), ray.getDirection(), t);
		
		final Vector3F surfaceNormal = Vector3F.directionNormalized(center, point);
		
		final float probabilityDensityFunctionValue = coneUniformDistributionProbabilityDensityFunction(cosThetaMax);
		
		return Optional.of(new SurfaceSample3F(point, surfaceNormal, probabilityDensityFunctionValue));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Sphere3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Sphere3F} instance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray) {
		return intersection(ray, 0.0001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Sphere3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Sphere3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		final Point3F center = getCenter();
		
		final Vector3F direction = ray.getDirection();
		final Vector3F centerToOrigin = Vector3F.direction(center, origin);
		
		final float radiusSquared = getRadiusSquared();
		
		final float a = direction.lengthSquared();
		final float b = 2.0F * Vector3F.dotProduct(centerToOrigin, direction);
		final float c = centerToOrigin.lengthSquared() - radiusSquared;
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		if(isNaN(t)) {
			return Optional.empty();
		}
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final Vector3F surfaceNormalG = Vector3F.directionNormalized(center, surfaceIntersectionPoint);
		final Vector3F surfaceNormalS = surfaceNormalG;
		
		final OrthoNormalBasis33F orthoNormalBasisG = new OrthoNormalBasis33F(surfaceNormalG);
		final OrthoNormalBasis33F orthoNormalBasisS = orthoNormalBasisG;
		
		final Point2F textureCoordinates = new Point2F(0.5F + atan2(surfaceNormalG.getZ(), surfaceNormalG.getX()) * PI_MULTIPLIED_BY_2_RECIPROCAL, 0.5F - asinpi(surfaceNormalG.getY()));
		
		return Optional.of(new SurfaceIntersection3F(orthoNormalBasisG, orthoNormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceNormalG, surfaceNormalS, t));
	}
	
	/**
	 * Returns the center of this {@code Sphere3F} instance.
	 * 
	 * @return the center of this {@code Sphere3F} instance
	 */
	public Point3F getCenter() {
		return this.center;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Sphere3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Sphere3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Sphere3F(%+.10f, %s)", Float.valueOf(this.radius), this.center);
	}
	
	/**
	 * Compares {@code object} to this {@code Sphere3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Sphere3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Sphere3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Sphere3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Sphere3F)) {
			return false;
		} else if(!Objects.equals(this.center, Sphere3F.class.cast(object).center)) {
			return false;
		} else if(!equal(this.radius, Sphere3F.class.cast(object).radius)) {
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
	 * @param referencePoint the reference point on this {@code Sphere3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Sphere3F} instance
	 * @param point the point on this {@code Sphere3F} instance
	 * @param surfaceNormal the surface normal on this {@code Sphere3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		final Point3F center = getCenter();
		
		final Vector3F directionToCenter = Vector3F.direction(referencePoint, center);
		
		final float lengthSquared = directionToCenter.lengthSquared();
		final float radiusSquared = getRadiusSquared();
		
		if(lengthSquared < radiusSquared * 1.00001F) {
			final Vector3F directionToSurface = Vector3F.direction(point, referencePoint);
			final Vector3F directionToSurfaceNormalized = Vector3F.normalize(directionToSurface);
			
			final float probabilityDensityFunctionValue = directionToSurface.lengthSquared() * getSurfaceAreaProbabilityDensityFunctionValue() / abs(Vector3F.dotProduct(directionToSurfaceNormalized, surfaceNormal));
			
			return probabilityDensityFunctionValue;
		}
		
		final float sinThetaMaxSquared = radiusSquared / lengthSquared;
		final float cosThetaMax = sqrt(max(0.0F, 1.0F - sinThetaMaxSquared));
		final float probabilityDensityFunctionValue = coneUniformDistributionProbabilityDensityFunction(cosThetaMax);
		
		return probabilityDensityFunctionValue;
	}
	
	/**
	 * Returns the radius of this {@code Sphere3F} instance.
	 * 
	 * @return the radius of this {@code Sphere3F} instance
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the squared radius of this {@code Sphere3F} instance.
	 * 
	 * @return the squared radius of this {@code Sphere3F} instance
	 */
	public float getRadiusSquared() {
		return this.radius * this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code Sphere3F} instance.
	 * 
	 * @return the surface area of this {@code Sphere3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return PI_MULTIPLIED_BY_4 * getRadiusSquared();
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Sphere3F} instance.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Sphere3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 3.0F / getSurfaceArea();
	}
	
	/**
	 * Returns the volume of this {@code Sphere3F} instance.
	 * 
	 * @return the volume of this {@code Sphere3F} instance
	 */
	@Override
	public float getVolume() {
		return 4.0F / 3.0F * PI * pow(this.radius, 3.0F);
	}
	
	/**
	 * Returns a hash code for this {@code Sphere3F} instance.
	 * 
	 * @return a hash code for this {@code Sphere3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.center, Float.valueOf(this.radius));
	}
}