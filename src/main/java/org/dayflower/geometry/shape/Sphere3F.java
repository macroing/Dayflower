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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.gamma;
import static org.dayflower.utility.Floats.isInfinite;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.solveQuadraticSystem;
import static org.dayflower.utility.Floats.sqrt;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;

/**
 * A {@code Sphere3F} denotes a 3-dimensional sphere that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Sphere3F implements Shape3F {
	/**
	 * The name of this {@code Sphere3F} class.
	 */
	public static final String NAME = "Sphere";
	
	/**
	 * The length of the {@code float[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the {@link Point3F} instance representing the center in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_CENTER = 0;
	
	/**
	 * The offset for the radius in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS = 3;
	
	/**
	 * The ID of this {@code Sphere3F} class.
	 */
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		this.center = Point3F.getCached(Objects.requireNonNull(center, "center == null"));
		this.radius = radius;
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
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point2F sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3F direction = SampleGeneratorF.sampleSphereUniformDistribution(sample.getU(), sample.getV());
		
		final Point3F point0 = Point3F.add(this.center, direction, this.radius);
		final Point3F point1 = Point3F.add(point0, new Vector3F(1.0F), this.radius / Point3F.distance(this.center, point0));
		
		final Vector3F pointError = Vector3F.multiply(Vector3F.absolute(new Vector3F(point1)), gamma(5));
		final Vector3F surfaceNormal = Vector3F.directionNormalized(this.center, point0);
		
		final float probabilityDensityFunctionValue = 1.0F / getSurfaceArea();
		
		final SurfaceSample3F surfaceSample = new SurfaceSample3F(point1, pointError, surfaceNormal, probabilityDensityFunctionValue);
		
		return Optional.of(surfaceSample);
	}
	
	/**
	 * Samples this {@code Sphere3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code sample} or {@code surfaceIntersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2F} instance with a sample point
	 * @param surfaceIntersection a {@link SurfaceIntersection3F} instance
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code sample} or {@code surfaceIntersection} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point2F sample, final SurfaceIntersection3F surfaceIntersection) {
		Objects.requireNonNull(sample, "sample == null");
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		
		final Point3F center = this.center;
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F direction = Vector3F.direction(surfaceIntersectionPoint, center);
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		final Vector3F surfaceIntersectionPointError = surfaceIntersection.getSurfaceIntersectionPointError();
		
		final Point3F origin = Point3F.offset(surfaceIntersectionPoint, direction, surfaceNormal, surfaceIntersectionPointError);
		
		final float radius = this.radius;
		final float radiusSquared = radius * radius;
		
		if(Point3F.distanceSquared(center, origin) <= radiusSquared) {
			final Optional<SurfaceSample3F> optionalSurfaceSample = sample(sample);
			
			if(optionalSurfaceSample.isPresent()) {
				final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
				
				final Point3F point = surfaceSample.getPoint();
				
				final Vector3F incoming = Vector3F.direction(surfaceIntersectionPoint, point);
				
				if(isZero(incoming.lengthSquared())) {
					return SurfaceSample3F.EMPTY;
				}
				
				final Vector3F incomingNormalized = Vector3F.normalize(incoming);
				final Vector3F incomingNormalizedNegated = Vector3F.negate(incomingNormalized);
				
				final float probabilityDensityFunctionValue = Point3F.distanceSquared(point, surfaceIntersectionPoint) / abs(Vector3F.dotProduct(surfaceSample.getSurfaceNormal(), incomingNormalizedNegated));
				
				if(isInfinite(probabilityDensityFunctionValue)) {
					return SurfaceSample3F.EMPTY;
				}
				
				return Optional.of(new SurfaceSample3F(point, surfaceSample.getPointError(), surfaceSample.getSurfaceNormal(), probabilityDensityFunctionValue));
			}
			
			return SurfaceSample3F.EMPTY;
		}
		
		final float distance = Point3F.distance(center, surfaceIntersectionPoint);
		final float distanceReciprocal = 1.0F / distance;
		
		final Vector3F directionToCenter = Vector3F.multiply(Vector3F.direction(surfaceIntersectionPoint, center), distanceReciprocal);
		
		final OrthonormalBasis33F orthonormalBasis = new OrthonormalBasis33F(directionToCenter);
		
		final float sinThetaMax = radius * distanceReciprocal;
		final float sinThetaMaxSquared = sinThetaMax * sinThetaMax;
		final float sinThetaMaxReciprocal = 1.0F / sinThetaMax;
		final float cosThetaMax = sqrt(max(0.0F, 1.0F - sinThetaMaxSquared));
		final float cosTheta = sinThetaMaxSquared < 0.00068523F ? sqrt(1.0F - sinThetaMaxSquared * sample.getU()) : (cosThetaMax - 1.0F) * sample.getU() + 1.0F;
		final float sinThetaSquared = sinThetaMaxSquared < 0.00068523F ? sinThetaMaxSquared * sample.getU() : 1.0F - cosTheta * cosTheta;
		final float cosAlpha = sinThetaSquared * sinThetaMaxReciprocal + cosTheta * sqrt(max(0.0F, 1.0F - sinThetaSquared * sinThetaMaxReciprocal * sinThetaMaxReciprocal));
		final float sinAlpha = sqrt(max(0.0F, 1.0F - cosAlpha * cosAlpha));
		final float phi = sample.getV() * 2.0F * PI;
		
		final Vector3F sphericalDirectionLocal = Vector3F.directionSpherical(sinAlpha, cosAlpha, phi);
		final Vector3F sphericalDirection = Vector3F.transform(sphericalDirectionLocal, OrthonormalBasis33F.flip(orthonormalBasis));
		
		final Point3F samplePoint = Point3F.add(center, sphericalDirection, radius);
		
		final Vector3F samplePointError = Vector3F.multiply(Vector3F.absolute(new Vector3F(samplePoint)), gamma(5));
		final Vector3F sampleSurfaceNormal = Vector3F.normalize(sphericalDirection);
		
		final float probabilityDensityFunctionValue = 1.0F / (2.0F * PI * (1.0F - cosThetaMax));
		
		return Optional.of(new SurfaceSample3F(samplePoint, samplePointError, sampleSurfaceNormal, probabilityDensityFunctionValue));
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
		final Point3F center = this.center;
		
		final Vector3F direction = ray.getDirection();
		final Vector3F centerToOrigin = Vector3F.direction(center, origin);
		
		final float radius = this.radius;
		final float radiusSquared = radius * radius;
		
		final float a = direction.lengthSquared();
		final float b = 2.0F * Vector3F.dotProduct(centerToOrigin, direction);
		final float c = centerToOrigin.lengthSquared() - radiusSquared;
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		if(isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final Vector3F surfaceNormalG = Vector3F.directionNormalized(center, surfaceIntersectionPoint);
		final Vector3F vG = new Vector3F(-PI_MULTIPLIED_BY_2 * surfaceNormalG.getY(), PI_MULTIPLIED_BY_2 * surfaceNormalG.getX(), 0.0F);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG, vG);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = Point2F.sphericalCoordinates(surfaceNormalG);
		
		final Vector3F surfaceIntersectionPointError = Vector3F.multiply(Vector3F.absolute(new Vector3F(surfaceIntersectionPoint)), gamma(5));
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
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
	 * Returns a {@code String} with the name of this {@code Sphere3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Sphere3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
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
	 * Evaluates the probability density function (PDF) for {@code surfaceIntersection} and {@code incoming}.
	 * <p>
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * If either {@code surfaceIntersection} or {@code incoming} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param surfaceIntersection a {@link SurfaceIntersection3F} instance
	 * @param incoming a {@link Vector3F} instance with the incoming direction
	 * @return the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code incoming} are {@code null}
	 */
	@Override
	public float evaluateProbabilityDensityFunction(final SurfaceIntersection3F surfaceIntersection, final Vector3F incoming) {
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Ray3F ray = surfaceIntersection.createRay(incoming);
		
		final Optional<SurfaceIntersection3F> optionalSurfaceIntersectionShape = intersection(ray, 0.001F, Float.MAX_VALUE);
		
		if(optionalSurfaceIntersectionShape.isPresent()) {
			final SurfaceIntersection3F surfaceIntersectionShape = optionalSurfaceIntersectionShape.get();
			
			final float probabilityDensityFunctionValue = Point3F.distanceSquared(surfaceIntersectionShape.getSurfaceIntersectionPoint(), surfaceIntersection.getSurfaceIntersectionPoint()) / abs(Vector3F.dotProduct(surfaceIntersectionShape.getSurfaceNormalS(), Vector3F.negate(incoming))) * getSurfaceArea();
			
			if(!isInfinite(probabilityDensityFunctionValue)) {
				return probabilityDensityFunctionValue;
			}
		}
		
		return 0.0F;
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
	 * Returns the volume of this {@code Sphere3F} instance.
	 * 
	 * @return the volume of this {@code Sphere3F} instance
	 */
	public float getVolume() {
		return 4.0F / 3.0F * PI * pow(this.radius, 3.0F);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Sphere3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Sphere3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		final Point3F center = this.center;
		
		final Vector3F direction = ray.getDirection();
//		final Vector3F centerToOrigin = Vector3F.direction(center, origin);
		
//		final float radius = this.radius;
//		final float radiusSquared = radius * radius;
		
//		final float a = direction.lengthSquared();
//		final float b = 2.0F * Vector3F.dotProduct(centerToOrigin, direction);
//		final float c = centerToOrigin.lengthSquared() - radiusSquared;
		
//		The code below resulted in an optimization, but not in the intersection(Ray3F, float, float) method:
		final float originX = origin.getX();
		final float originY = origin.getY();
		final float originZ = origin.getZ();
		
		final float centerX = center.getX();
		final float centerY = center.getY();
		final float centerZ = center.getZ();
		
		final float directionX = direction.getX();
		final float directionY = direction.getY();
		final float directionZ = direction.getZ();
		
		final float centerToOriginX = originX - centerX;
		final float centerToOriginY = originY - centerY;
		final float centerToOriginZ = originZ - centerZ;
		
		final float radius = this.radius;
		final float radiusSquared = radius * radius;
		
		final float a = directionX * directionX + directionY * directionY + directionZ * directionZ;
		final float b = 2.0F * (centerToOriginX * directionX + centerToOriginY * directionY + centerToOriginZ * directionZ);
		final float c = (centerToOriginX * centerToOriginX + centerToOriginY * centerToOriginY + centerToOriginZ * centerToOriginZ) - radiusSquared;
//		The code above resulted in an optimization, but not in the intersection(Ray3F, float, float) method.
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		return t;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Sphere3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Sphere3F} instance
	 */
	public float[] toArray() {
		final float[] array = new float[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_CENTER + 0] = this.center.getX();
		array[ARRAY_OFFSET_CENTER + 1] = this.center.getY();
		array[ARRAY_OFFSET_CENTER + 2] = this.center.getZ();
		array[ARRAY_OFFSET_RADIUS] = this.radius;
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Sphere3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Sphere3F} instance
	 */
	@Override
	public int getID() {
		return ID;
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	private float doIntersectionTAnalytic(final Ray3F ray, final float tMinimum, final float tMaximum) {
//		final Point3F origin = ray.getOrigin();
//		final Point3F center = getCenter();
//		
//		final Vector3F direction = ray.getDirection();
//		final Vector3F centerToOrigin = Vector3F.direction(center, origin);
//		
//		final float radiusSquared = getRadiusSquared();
//		
//		final float a = direction.lengthSquared();
//		final float b = 2.0F * Vector3F.dotProduct(centerToOrigin, direction);
//		final float c = centerToOrigin.lengthSquared() - radiusSquared;
//		
//		final float[] ts = solveQuadraticSystem(a, b, c);
//		
//		final float t0 = ts[0];
//		final float t1 = ts[1];
//		
//		final float t = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
//		
//		return t;
//	}
	
//	private float doIntersectionTGeometric1(final Ray3F ray, final float tMinimum, final float tMaximum) {
//		final Point3F origin = ray.getOrigin();
//		final Point3F center = getCenter();
//		
//		final Vector3F direction = ray.getDirection();
//		final Vector3F originToCenter = Vector3F.direction(origin, center);
//		
//		final float radiusSquared = getRadiusSquared();
//		
//		final float b = Vector3F.dotProduct(originToCenter, direction);
//		
//		final float discriminantSquared = originToCenter.lengthSquared() - b * b;
//		
//		if(discriminantSquared > radiusSquared) {
//			return Float.NaN;
//		}
//		
//		final float discriminant = sqrt(radiusSquared - discriminantSquared);
//		
//		final float t0 = min(b - discriminant, b + discriminant);
//		final float t1 = max(b - discriminant, b + discriminant);
//		
//		final float t = t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
//		
//		return t;
//	}
	
//	private float doIntersectionTGeometric2(final Ray3F ray, final float tMinimum, final float tMaximum) {
//		final Point3F origin = ray.getOrigin();
//		final Point3F center = getCenter();
//		
//		final Vector3F direction = ray.getDirection();
//		final Vector3F originToCenter = Vector3F.direction(origin, center);
//		
//		final float radiusSquared = getRadiusSquared();
//		
//		final float b = Vector3F.dotProduct(originToCenter, direction);
//		
//		final float discriminantSquared = b * b - originToCenter.lengthSquared() + radiusSquared;
//		
//		if(discriminantSquared < 0.0F) {
//			return Float.NaN;
//		}
//		
//		final float discriminant = sqrt(discriminantSquared);
//		
//		final float t0 = min(b - discriminant, b + discriminant);
//		final float t1 = max(b - discriminant, b + discriminant);
//		
//		final float t = t0 > tMinimum && t0 < tMaximum ? t0 : t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
//		
//		return t;
//	}
}