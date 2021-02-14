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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.atan2;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.getOrAdd;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.pow;
import static org.dayflower.utility.Floats.solveQuadraticSystem;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;

/**
 * A {@code Paraboloid3F} denotes a 3-dimensional paraboloid that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Paraboloid3F implements Shape3F {
	/**
	 * The name of this {@code Paraboloid3F} class.
	 */
	public static final String NAME = "Paraboloid";
	
	/**
	 * The ID of this {@code Paraboloid3F} class.
	 */
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float phiMax;
	private final float radius;
	private final float zMax;
	private final float zMin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3F(360.0F);
	 * }
	 * </pre>
	 */
	public Paraboloid3F() {
		this(360.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3F(phiMax, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 */
	public Paraboloid3F(final float phiMax) {
		this(phiMax, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3F(phiMax, radius, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 */
	public Paraboloid3F(final float phiMax, final float radius) {
		this(phiMax, radius, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3F(phiMax, radius, zMax, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 */
	public Paraboloid3F(final float phiMax, final float radius, final float zMax) {
		this(phiMax, radius, zMax, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @param zMin the minimum Z
	 */
	public Paraboloid3F(final float phiMax, final float radius, final float zMax, final float zMin) {
		this.phiMax = phiMax;
		this.radius = radius;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Paraboloid3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(new Point3F(-this.radius, -this.radius, this.zMin), new Point3F(this.radius, this.radius, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Paraboloid3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Paraboloid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		final float originX = origin.getX();
		final float originY = origin.getY();
		final float originZ = origin.getZ();
		
		final float directionX = direction.getX();
		final float directionY = direction.getY();
		final float directionZ = direction.getZ();
		
		final float phiMax = this.phiMax;
		final float radius = this.radius;
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		
		final float k = zMax / (radius * radius);
		
		final float a = k * (directionX * directionX + directionY * directionY);
		final float b = 2.0F * k * (directionX * originX + directionY * originY) - directionZ;
		final float c = k * (originX * originX + originY * originY) - originZ;
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		if(isNaN(tClosest)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F pointClosest = Point3F.add(origin, direction, tClosest);
		
		final float xClosest = pointClosest.getX();
		final float yClosest = pointClosest.getY();
		final float zClosest = pointClosest.getZ();
		
		final float phiClosest = getOrAdd(atan2(yClosest, xClosest), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return SurfaceIntersection3F.EMPTY;
			}
			
			final float tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
			
			if(isNaN(tClipped)) {
				return SurfaceIntersection3F.EMPTY;
			}
			
			final Point3F pointClipped = Point3F.add(origin, direction, tClipped);
			
			final float xClipped = pointClipped.getX();
			final float yClipped = pointClipped.getY();
			final float zClipped = pointClipped.getZ();
			
			final float phiClipped = getOrAdd(atan2(yClipped, xClipped), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return SurfaceIntersection3F.EMPTY;
			}
			
			return Optional.of(doCreateSurfaceIntersection(ray, phiClipped, tClipped, xClipped, yClipped, zClipped));
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, phiClosest, tClosest, xClosest, yClosest, zClosest));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Paraboloid3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Paraboloid3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Paraboloid3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.phiMax), Float.valueOf(this.radius), Float.valueOf(this.zMax), Float.valueOf(this.zMin));
	}
	
	/**
	 * Compares {@code object} to this {@code Paraboloid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Paraboloid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Paraboloid3F)) {
			return false;
		} else if(!equal(this.phiMax, Paraboloid3F.class.cast(object).phiMax)) {
			return false;
		} else if(!equal(this.radius, Paraboloid3F.class.cast(object).radius)) {
			return false;
		} else if(!equal(this.zMax, Paraboloid3F.class.cast(object).zMax)) {
			return false;
		} else if(!equal(this.zMin, Paraboloid3F.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the maximum phi of this {@code Paraboloid3F} instance.
	 * 
	 * @return the maximum phi of this {@code Paraboloid3F} instance
	 */
	public float getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns the radius of this {@code Paraboloid3F} instance.
	 * 
	 * @return the radius of this {@code Paraboloid3F} instance
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the maximum Z of this {@code Paraboloid3F} instance.
	 * 
	 * @return the maximum Z of this {@code Paraboloid3F} instance
	 */
	public float getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z of this {@code Paraboloid3F} instance.
	 * 
	 * @return the minimum Z of this {@code Paraboloid3F} instance
	 */
	public float getZMin() {
		return this.zMin;
	}
	
	/**
	 * Returns the surface area of this {@code Paraboloid3F} instance.
	 * 
	 * @return the surface area of this {@code Paraboloid3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final float phiMax = this.phiMax;
		final float radius = this.radius;
		final float radiusSquared = radius * radius;
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		final float k = 4.0F * zMax / radiusSquared;
		final float a = radiusSquared * radiusSquared * phiMax / (12.0F * zMax * zMax);
		final float b = pow(k * zMax + 1.0F, 1.5F) - pow(k * zMin + 1.0F, 1.5F);
		
		return a * b;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Paraboloid3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Paraboloid3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		final float originX = origin.getX();
		final float originY = origin.getY();
		final float originZ = origin.getZ();
		
		final float directionX = direction.getX();
		final float directionY = direction.getY();
		final float directionZ = direction.getZ();
		
		final float phiMax = this.phiMax;
		final float radius = this.radius;
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		
		final float k = zMax / (radius * radius);
		
		final float a = k * (directionX * directionX + directionY * directionY);
		final float b = 2.0F * k * (directionX * originX + directionY * originY) - directionZ;
		final float c = k * (originX * originX + originY * originY) - originZ;
		
		final float[] ts = solveQuadraticSystem(a, b, c);
		
		final float t0 = ts[0];
		final float t1 = ts[1];
		
		final float tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
		
		if(isNaN(tClosest)) {
			return Float.NaN;
		}
		
		final Point3F pointClosest = Point3F.add(origin, direction, tClosest);
		
		final float xClosest = pointClosest.getX();
		final float yClosest = pointClosest.getY();
		final float zClosest = pointClosest.getZ();
		
		final float phiClosest = getOrAdd(atan2(yClosest, xClosest), 0.0F, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return Float.NaN;
			}
			
			final float tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Float.NaN;
			
			if(isNaN(tClipped)) {
				return Float.NaN;
			}
			
			final Point3F pointClipped = Point3F.add(origin, direction, tClipped);
			
			final float xClipped = pointClipped.getX();
			final float yClipped = pointClipped.getY();
			final float zClipped = pointClipped.getZ();
			
			final float phiClipped = getOrAdd(atan2(yClipped, xClipped), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return Float.NaN;
			}
			
			return tClipped;
		}
		
		return tClosest;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Paraboloid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Paraboloid3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Paraboloid3F} instance.
	 * 
	 * @return a hash code for this {@code Paraboloid3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.phiMax), Float.valueOf(this.radius), Float.valueOf(this.zMax), Float.valueOf(this.zMin));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float phi, final float t, final float x, final float y, final float z) {
		final float phiMax = this.phiMax;
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		
		final float u = phi / phiMax;
		final float v = (z - zMin) / (zMax - zMin);
		
		final Vector3F dPDU = Vector3F.normalize(new Vector3F(-phiMax * y, phiMax * x, 0.0F));
		final Vector3F dPDV = Vector3F.normalize(new Vector3F((zMax - zMin) * (x / (2.0F * z)), (zMax - zMin) * (y / (2.0F * z)), zMax - zMin));
		
//		final Vector3F d2PDUU = new Vector3F(-phiMax * phiMax * x, -phiMax * phiMax * y, 0.0F);
//		final Vector3F d2PDUV = new Vector3F((zMax - zMin) * phiMax * (-y / (2.0F * z)), (zMax - zMin) * phiMax * (x / (2.0F * z)), 0.0F);
//		final Vector3F d2PDVV = new Vector3F(-(zMax - zMin) * (zMax - zMin) * (x / (4.0F * z * z)), -(zMax - zMin) * (zMax - zMin) * (y / (4.0F * z * z)), 0.0F);
		
//		final float e0 = Vector3F.dotProduct(dPDU, dPDU);
//		final float f0 = Vector3F.dotProduct(dPDU, dPDV);
//		final float g0 = Vector3F.dotProduct(dPDV, dPDV);
		
		final Vector3F surfaceNormalG = Vector3F.normalize(Vector3F.crossProduct(dPDU, dPDV));
		
//		final float e1 = Vector3F.dotProduct(surfaceNormalG, d2PDUU);
//		final float f1 = Vector3F.dotProduct(surfaceNormalG, d2PDUV);
//		final float g1 = Vector3F.dotProduct(surfaceNormalG, d2PDVV);
		
//		final float inverseEGFF = 1.0F / (e0 * g0 - f0 * f0);
		
//		final Vector3F dNDU = Vector3F.add(Vector3F.multiply(dPDU, (f1 * f0 - e1 * g0) * inverseEGFF), Vector3F.multiply(dPDV, (e1 * f0 - f1 * e0) * inverseEGFF));
//		final Vector3F dNDV = Vector3F.add(Vector3F.multiply(dPDU, (g1 * f0 - f1 * g0) * inverseEGFF), Vector3F.multiply(dPDV, (f1 * f0 - g1 * e0) * inverseEGFF));
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point3F surfaceIntersectionPoint = new Point3F(x, y, z);
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
}