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

import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.gamma;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.sqrt;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleD;
import org.dayflower.geometry.BoundingVolume3D;
import org.dayflower.geometry.OrthonormalBasis33D;
import org.dayflower.geometry.Point2D;
import org.dayflower.geometry.Point3D;
import org.dayflower.geometry.Ray3D;
import org.dayflower.geometry.Shape3D;
import org.dayflower.geometry.SurfaceIntersection3D;
import org.dayflower.geometry.Vector3D;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3D;

/**
 * A {@code Cylinder3D} denotes a 3-dimensional cylinder that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Cylinder3D implements Shape3D {
	/**
	 * The name of this {@code Cylinder3D} class.
	 */
	public static final String NAME = "Cylinder";
	
	/**
	 * The ID of this {@code Cylinder3D} class.
	 */
	public static final int ID = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleD phiMax;
	private final double radius;
	private final double zMax;
	private final double zMin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Cylinder3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Cylinder3D(AngleD.degrees(360.0D));
	 * }
	 * </pre>
	 */
	public Cylinder3D() {
		this(AngleD.degrees(360.0D));
	}
	
	/**
	 * Constructs a new {@code Cylinder3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Cylinder3D(phiMax, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Cylinder3D(final AngleD phiMax) {
		this(phiMax, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Cylinder3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Cylinder3D(phiMax, radius, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Cylinder3D(final AngleD phiMax, final double radius) {
		this(phiMax, radius, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Cylinder3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Cylinder3D(phiMax, radius, zMax, -1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Cylinder3D(final AngleD phiMax, final double radius, final double zMax) {
		this(phiMax, radius, zMax, -1.0D);
	}
	
	/**
	 * Constructs a new {@code Cylinder3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @param zMin the minimum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Cylinder3D(final AngleD phiMax, final double radius, final double zMax, final double zMin) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.radius = radius;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Cylinder3D} instance.
	 * 
	 * @return the maximum phi of this {@code Cylinder3D} instance
	 */
	public AngleD getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Cylinder3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Cylinder3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(new Point3D(-this.radius, -this.radius, this.zMin), new Point3D(this.radius, this.radius, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Cylinder3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Cylinder3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double originX = origin.getX();
		final double originY = origin.getY();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		
		final double phiMax = this.phiMax.getRadians();
		final double radius = this.radius;
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		
		final double a = directionX * directionX + directionY * directionY;
		final double b = 2.0D * (directionX * originX + directionY * originY);
		final double c = originX * originX + originY * originY - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(tClosest)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D pointClosest = Point3D.add(origin, direction, tClosest);
		
		final double radiusClosest = sqrt(pointClosest.getX() * pointClosest.getX() + pointClosest.getY() * pointClosest.getY());
		
		final double xClosest = pointClosest.getX() * (radius / radiusClosest);
		final double yClosest = pointClosest.getY() * (radius / radiusClosest);
		final double zClosest = pointClosest.getZ();
		
		final double phiClosest = getOrAdd(atan2(yClosest, xClosest), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			final double tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
			
			if(isNaN(tClipped)) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			final Point3D pointClipped = Point3D.add(origin, direction, tClipped);
			
			final double radiusClipped = sqrt(pointClipped.getX() * pointClipped.getX() + pointClipped.getY() * pointClipped.getY());
			
			final double xClipped = pointClipped.getX() * (radius / radiusClipped);
			final double yClipped = pointClipped.getY() * (radius / radiusClipped);
			final double zClipped = pointClipped.getZ();
			
			final double phiClipped = getOrAdd(atan2(yClipped, xClipped), 0.0D, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			return Optional.of(doCreateSurfaceIntersection(ray, phiClipped, tClipped, xClipped, yClipped, zClipped));
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, phiClosest, tClosest, xClosest, yClosest, zClosest));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Cylinder3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Cylinder3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Cylinder3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Cylinder3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Cylinder3D(%s, %+.10f, %+.10f)", this.phiMax, Double.valueOf(this.radius), Double.valueOf(this.zMax), Double.valueOf(this.zMin));
	}
	
	/**
	 * Compares {@code object} to this {@code Cylinder3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Cylinder3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Cylinder3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Cylinder3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Cylinder3D)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Cylinder3D.class.cast(object).phiMax)) {
			return false;
		} else if(!equal(this.radius, Cylinder3D.class.cast(object).radius)) {
			return false;
		} else if(!equal(this.zMax, Cylinder3D.class.cast(object).zMax)) {
			return false;
		} else if(!equal(this.zMin, Cylinder3D.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code Cylinder3D} instance.
	 * 
	 * @return the radius of this {@code Cylinder3D} instance
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code Cylinder3D} instance.
	 * 
	 * @return the surface area of this {@code Cylinder3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return (this.zMax - this.zMin) * this.radius * this.phiMax.getRadians();
	}
	
	/**
	 * Returns the maximum Z of this {@code Cylinder3D} instance.
	 * 
	 * @return the maximum Z of this {@code Cylinder3D} instance
	 */
	public double getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z of this {@code Cylinder3D} instance.
	 * 
	 * @return the minimum Z of this {@code Cylinder3D} instance
	 */
	public double getZMin() {
		return this.zMin;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Cylinder3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Cylinder3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double originX = origin.getX();
		final double originY = origin.getY();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		
		final double phiMax = this.phiMax.getRadians();
		final double radius = this.radius;
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		
		final double a = directionX * directionX + directionY * directionY;
		final double b = 2.0D * (directionX * originX + directionY * originY);
		final double c = originX * originX + originY * originY - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(tClosest)) {
			return Double.NaN;
		}
		
		final Point3D pointClosest = Point3D.add(origin, direction, tClosest);
		
		final double radiusClosest = sqrt(pointClosest.getX() * pointClosest.getX() + pointClosest.getY() * pointClosest.getY());
		
		final double xClosest = pointClosest.getX() * (radius / radiusClosest);
		final double yClosest = pointClosest.getY() * (radius / radiusClosest);
		final double zClosest = pointClosest.getZ();
		
		final double phiClosest = getOrAdd(atan2(yClosest, xClosest), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return Double.NaN;
			}
			
			final double tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
			
			if(isNaN(tClipped)) {
				return Double.NaN;
			}
			
			final Point3D pointClipped = Point3D.add(origin, direction, tClipped);
			
			final double radiusClipped = sqrt(pointClipped.getX() * pointClipped.getX() + pointClipped.getY() * pointClipped.getY());
			
			final double xClipped = pointClipped.getX() * (radius / radiusClipped);
			final double yClipped = pointClipped.getY() * (radius / radiusClipped);
			final double zClipped = pointClipped.getZ();
			
			final double phiClipped = getOrAdd(atan2(yClipped, xClipped), 0.0D, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return Double.NaN;
			}
			
			return tClipped;
		}
		
		return tClosest;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Cylinder3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Cylinder3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Cylinder3D} instance.
	 * 
	 * @return a hash code for this {@code Cylinder3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, Double.valueOf(this.radius), Double.valueOf(this.zMax), Double.valueOf(this.zMin));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double phi, final double t, final double x, final double y, final double z) {
		final double phiMax = this.phiMax.getRadians();
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		
		final double u = phi / phiMax;
		final double v = (z - zMin) / (zMax - zMin);
		
		final Vector3D dPDU = Vector3D.normalize(new Vector3D(-phiMax * y, phiMax * x, 0.0D));
		final Vector3D dPDV = Vector3D.normalize(new Vector3D(0.0D, 0.0D, zMax - zMin));
		
//		final Vector3D d2PDUU = new Vector3D(x * -phiMax * phiMax, y * -phiMax * phiMax, 0.0D);
//		final Vector3D d2PDUV = new Vector3D();
//		final Vector3D d2PDVV = new Vector3D();
		
//		final double e0 = Vector3D.dotProduct(dPDU, dPDU);
//		final double f0 = Vector3D.dotProduct(dPDU, dPDV);
//		final double g0 = Vector3D.dotProduct(dPDV, dPDV);
		
		final Vector3D surfaceNormalG = Vector3D.normalize(Vector3D.crossProduct(dPDU, dPDV));
		
//		final double e1 = Vector3D.dotProduct(surfaceNormalG, d2PDUU);
//		final double f1 = Vector3D.dotProduct(surfaceNormalG, d2PDUV);
//		final double g1 = Vector3D.dotProduct(surfaceNormalG, d2PDVV);
		
//		final double inverseEGFF = 1.0D / (e0 * g0 - f0 * f0);
		
//		final Vector3D dNDU = Vector3D.add(Vector3D.multiply(dPDU, (f1 * f0 - e1 * g0) * inverseEGFF), Vector3D.multiply(dPDV, (e1 * f0 - f1 * e0) * inverseEGFF));
//		final Vector3D dNDV = Vector3D.add(Vector3D.multiply(dPDU, (g1 * f0 - f1 * g0) * inverseEGFF), Vector3D.multiply(dPDV, (f1 * f0 - g1 * e0) * inverseEGFF));
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point3D surfaceIntersectionPoint = new Point3D(x, y, z);
		
		final Point2D textureCoordinates = new Point2D(u, v);
		
		final Vector3D surfaceIntersectionPointError = Vector3D.multiply(Vector3D.absolute(new Vector3D(x, y, 0.0D)), gamma(3));
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
}