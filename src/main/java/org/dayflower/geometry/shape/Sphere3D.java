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
package org.dayflower.geometry.shape;

import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.PI;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Doubles.gamma;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.sqrt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
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
 * A {@code Sphere3D} is an implementation of {@link Shape3D} that represents a sphere.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
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
	 * The ID of this {@code Sphere3D} class.
	 */
	public static final int ID = 15;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Sphere3D} instance.
	 */
	public Sphere3D() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Sphere3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Sphere3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new BoundingSphere3D(1.0D, new Point3D());
	}
	
	/**
	 * Samples this {@code Sphere3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2D} instance with a sample point
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceSample3D> sample(final Point2D sample) {
		Objects.requireNonNull(sample, "sample == null");
		
		final Vector3D direction = SampleGeneratorD.sampleSphereUniformDistribution(sample.x, sample.y);
		
		final Point3D point0 = Point3D.add(new Point3D(), direction, 1.0D);
		final Point3D point1 = Point3D.add(point0, new Vector3D(1.0D), 1.0D / Point3D.distance(new Point3D(), point0));
		
		final Vector3D pointError = Vector3D.multiply(Vector3D.absolute(new Vector3D(point1)), gamma(5));
		final Vector3D surfaceNormal = Vector3D.directionNormalized(new Point3D(), point0);
		
		final double probabilityDensityFunctionValue = 1.0D / getSurfaceArea();
		
		final SurfaceSample3D surfaceSample = new SurfaceSample3D(point1, pointError, surfaceNormal, probabilityDensityFunctionValue);
		
		return Optional.of(surfaceSample);
	}
	
	/**
	 * Samples this {@code Sphere3D} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3D} with the surface sample.
	 * <p>
	 * If either {@code sample} or {@code surfaceIntersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sample a {@link Point2D} instance with a sample point
	 * @param surfaceIntersection a {@link SurfaceIntersection3D} instance
	 * @return an optional {@code SurfaceSample3D} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code sample} or {@code surfaceIntersection} are {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceSample3D> sample(final Point2D sample, final SurfaceIntersection3D surfaceIntersection) {
		Objects.requireNonNull(sample, "sample == null");
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		
		final Point3D center = new Point3D();
		final Point3D surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3D direction = Vector3D.direction(surfaceIntersectionPoint, center);
		final Vector3D surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		final Vector3D surfaceIntersectionPointError = surfaceIntersection.getSurfaceIntersectionPointError();
		
		final Point3D origin = Point3D.offset(surfaceIntersectionPoint, direction, surfaceNormal, surfaceIntersectionPointError);
		
		final double radius = 1.0D;
		final double radiusSquared = radius * radius;
		
		if(Point3D.distanceSquared(center, origin) <= radiusSquared) {
			final Optional<SurfaceSample3D> optionalSurfaceSample = sample(sample);
			
			if(optionalSurfaceSample.isPresent()) {
				final SurfaceSample3D surfaceSample = optionalSurfaceSample.get();
				
				final Point3D point = surfaceSample.getPoint();
				
				final Vector3D incoming = Vector3D.direction(surfaceIntersectionPoint, point);
				
				if(isZero(incoming.lengthSquared())) {
					return SurfaceSample3D.EMPTY;
				}
				
				final Vector3D incomingNormalized = Vector3D.normalize(incoming);
				final Vector3D incomingNormalizedNegated = Vector3D.negate(incomingNormalized);
				
				final double probabilityDensityFunctionValue = Point3D.distanceSquared(point, surfaceIntersectionPoint) / Vector3D.dotProductAbs(surfaceSample.getSurfaceNormal(), incomingNormalizedNegated);
				
				if(isInfinite(probabilityDensityFunctionValue)) {
					return SurfaceSample3D.EMPTY;
				}
				
				return Optional.of(new SurfaceSample3D(point, surfaceSample.getPointError(), surfaceSample.getSurfaceNormal(), probabilityDensityFunctionValue));
			}
			
			return SurfaceSample3D.EMPTY;
		}
		
		final double distance = Point3D.distance(center, surfaceIntersectionPoint);
		final double distanceReciprocal = 1.0D / distance;
		
		final Vector3D directionToCenter = Vector3D.multiply(Vector3D.direction(surfaceIntersectionPoint, center), distanceReciprocal);
		
		final OrthonormalBasis33D orthonormalBasis = new OrthonormalBasis33D(directionToCenter);
		
		final Vector3D x = Vector3D.negate(orthonormalBasis.getU());
		final Vector3D y = Vector3D.negate(orthonormalBasis.getV());
		final Vector3D z = Vector3D.negate(orthonormalBasis.getW());
		
		final double sinThetaMax = radius * distanceReciprocal;
		final double sinThetaMaxSquared = sinThetaMax * sinThetaMax;
		final double sinThetaMaxReciprocal = 1.0D / sinThetaMax;
		final double cosThetaMax = sqrt(max(0.0D, 1.0D - sinThetaMaxSquared));
		final double cosTheta = sinThetaMaxSquared < 0.00068523D ? sqrt(1.0D - sinThetaMaxSquared * sample.x) : (cosThetaMax - 1.0D) * sample.x + 1.0D;
		final double sinThetaSquared = sinThetaMaxSquared < 0.00068523D ? sinThetaMaxSquared * sample.x : 1.0D - cosTheta * cosTheta;
		final double cosAlpha = sinThetaSquared * sinThetaMaxReciprocal + cosTheta * sqrt(max(0.0D, 1.0D - sinThetaSquared * sinThetaMaxReciprocal * sinThetaMaxReciprocal));
		final double sinAlpha = sqrt(max(0.0D, 1.0D - cosAlpha * cosAlpha));
		final double phi = sample.y * 2.0D * PI;
		
		final Vector3D sphericalDirection = Vector3D.directionSpherical(sinAlpha, cosAlpha, phi, x, y, z);
		
		final Point3D samplePoint = Point3D.add(center, sphericalDirection, radius);
		
		final Vector3D samplePointError = Vector3D.multiply(Vector3D.absolute(new Vector3D(samplePoint)), gamma(5));
		final Vector3D sampleSurfaceNormal = Vector3D.normalize(sphericalDirection);
		
		final double probabilityDensityFunctionValue = SampleGeneratorD.coneUniformDistributionProbabilityDensityFunction(cosThetaMax);
		
		return Optional.of(new SurfaceSample3D(samplePoint, samplePointError, sampleSurfaceNormal, probabilityDensityFunctionValue));
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
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
		return "new Sphere3D()";
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
				return nodeHierarchicalVisitor.visitLeave(this);
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Sphere3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Sphere3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	@Override
	public boolean contains(final Point3D point) {
		return Point3D.distanceSquared(new Point3D(), point) <= 1.0D;
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
	 * @param surfaceIntersection a {@link SurfaceIntersection3D} instance
	 * @param incoming a {@link Vector3D} instance with the incoming direction
	 * @return the probability density function (PDF) value
	 * @throws NullPointerException thrown if, and only if, either {@code surfaceIntersection} or {@code incoming} are {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double evaluateProbabilityDensityFunction(final SurfaceIntersection3D surfaceIntersection, final Vector3D incoming) {
		Objects.requireNonNull(surfaceIntersection, "surfaceIntersection == null");
		Objects.requireNonNull(incoming, "incoming == null");
		
		final Ray3D ray = surfaceIntersection.createRay(incoming);
		
		final Optional<SurfaceIntersection3D> optionalSurfaceIntersectionShape = intersection(ray, 0.001D, MAX_VALUE);
		
		if(optionalSurfaceIntersectionShape.isPresent()) {
			final SurfaceIntersection3D surfaceIntersectionShape = optionalSurfaceIntersectionShape.get();
			
			final double probabilityDensityFunctionValue = Point3D.distanceSquared(surfaceIntersectionShape.getSurfaceIntersectionPoint(), surfaceIntersection.getSurfaceIntersectionPoint()) / Vector3D.dotProductAbs(surfaceIntersectionShape.getSurfaceNormalS(), Vector3D.negate(incoming)) * getSurfaceArea();
			
			if(!isInfinite(probabilityDensityFunctionValue)) {
				return probabilityDensityFunctionValue;
			}
		}
		
		return 0.0D;
	}
	
	/**
	 * Returns the surface area of this {@code Sphere3D} instance.
	 * 
	 * @return the surface area of this {@code Sphere3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return PI_MULTIPLIED_BY_4;
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
		final Vector3D direction = ray.getDirection();
		final Vector3D centerToOrigin = Vector3D.direction(new Point3D(), ray.getOrigin());
		
		final double a = direction.lengthSquared();
		final double b = 2.0D * Vector3D.dotProduct(centerToOrigin, direction);
		final double c = centerToOrigin.lengthSquared() - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return Double.NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				return t;
			}
		}
		
		return Double.NaN;
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
		return Objects.hash();
	}
	
	/**
	 * Writes this {@code Sphere3D} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(ID);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = Point2D.sphericalCoordinates(Vector3D.directionNormalized(new Point3D(), surfaceIntersectionPoint));
		
		final Vector3D surfaceIntersectionPointError = doCreateSurfaceIntersectionPointError(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static OrthonormalBasis33D doCreateOrthonormalBasisG(final Point3D surfaceIntersectionPoint) {
		final Vector3D w = Vector3D.directionNormalized(new Point3D(), surfaceIntersectionPoint);
		final Vector3D v = new Vector3D(-PI_MULTIPLIED_BY_2 * w.y, PI_MULTIPLIED_BY_2 * w.x, 0.0D);
		
		return new OrthonormalBasis33D(w, v);
	}
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
	
	private static Vector3D doCreateSurfaceIntersectionPointError(final Point3D surfaceIntersectionPoint) {
		return Vector3D.multiply(Vector3D.absolute(new Vector3D(surfaceIntersectionPoint)), gamma(5));
	}
}