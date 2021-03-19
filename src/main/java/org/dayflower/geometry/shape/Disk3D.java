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
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isZero;
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
 * A {@code Disk3D} denotes a 3-dimensional disk that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@link Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Disk3D implements Shape3D {
	/**
	 * The name of this {@code Disk3D} class.
	 */
	public static final String NAME = "Disk";
	
	/**
	 * The length of the {@code double[]}.
	 */
	public static final int ARRAY_LENGTH = 4;
	
	/**
	 * The offset for the {@link AngleD} instance representing the maximum phi in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_PHI_MAX = 0;
	
	/**
	 * The offset for the inner radius in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS_INNER = 1;
	
	/**
	 * The offset for the outer radius in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_RADIUS_OUTER = 2;
	
	/**
	 * The offset for the maximum Z in the {@code double[]}.
	 */
	public static final int ARRAY_OFFSET_Z_MAX = 3;
	
	/**
	 * The ID of this {@code Disk3D} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleD phiMax;
	private final double radiusInner;
	private final double radiusOuter;
	private final double zMax;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Disk3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3D(AngleD.degrees(360.0D));
	 * }
	 * </pre>
	 */
	public Disk3D() {
		this(AngleD.degrees(360.0D));
	}
	
	/**
	 * Constructs a new {@code Disk3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3D(phiMax, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3D(final AngleD phiMax) {
		this(phiMax, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Disk3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3D(phiMax, radiusInner, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3D(final AngleD phiMax, final double radiusInner) {
		this(phiMax, radiusInner, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Disk3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3D(phiMax, radiusInner, radiusOuter, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @param radiusOuter the outer radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3D(final AngleD phiMax, final double radiusInner, final double radiusOuter) {
		this(phiMax, radiusInner, radiusOuter, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Disk3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @param radiusOuter the outer radius
	 * @param zMax the maximum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3D(final AngleD phiMax, final double radiusInner, final double radiusOuter, final double zMax) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.radiusInner = radiusInner;
		this.radiusOuter = radiusOuter;
		this.zMax = zMax;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Disk3D} instance.
	 * 
	 * @return the maximum phi of this {@code Disk3D} instance
	 */
	public AngleD getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Disk3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Disk3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(new Point3D(-this.radiusOuter, -this.radiusOuter, this.zMax), new Point3D(this.radiusOuter, this.radiusOuter, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Disk3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Disk3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		if(isZero(direction.getZ())) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double t = (this.zMax - origin.getZ()) / direction.getZ();
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D point = Point3D.add(origin, direction, t);
		
		final double x = point.getX();
		final double y = point.getY();
		
		final double distanceSquared = x * x + y * y;
		
		final double radiusInner = this.radiusInner;
		final double radiusOuter = this.radiusOuter;
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		final double phiMax = this.phiMax.getRadians();
		
		if(phi > phiMax) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final double distance = sqrt(distanceSquared);
		
		final double u = phi / phiMax;
		final double v = (radiusOuter - distance) / (radiusOuter - radiusInner);
		
		final Vector3D dPDU = Vector3D.normalize(new Vector3D(-phiMax * y, phiMax * x, 0.0D));
		final Vector3D dPDV = Vector3D.normalize(new Vector3D(x * (radiusInner - radiusOuter) / distance, y * (radiusInner - radiusOuter) / distance, 0.0D));
		
		final Vector3D surfaceNormalG = Vector3D.crossProduct(dPDU, dPDV);
		
		final OrthonormalBasis33D orthonormalBasisG = new OrthonormalBasis33D(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point3D surfaceIntersectionPoint = new Point3D(x, y, this.zMax);
		
		final Point2D textureCoordinates = new Point2D(u, v);
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return Optional.of(new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Disk3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Disk3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Disk3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Disk3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Disk3D(%s, %+.10f, %+.10f, %+.10f)", this.phiMax, Double.valueOf(this.radiusInner), Double.valueOf(this.radiusOuter), Double.valueOf(this.zMax));
	}
	
	/**
	 * Compares {@code object} to this {@code Disk3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Disk3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Disk3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Disk3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Disk3D)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Disk3D.class.cast(object).phiMax)) {
			return false;
		} else if(!equal(this.radiusInner, Disk3D.class.cast(object).radiusInner)) {
			return false;
		} else if(!equal(this.radiusOuter, Disk3D.class.cast(object).radiusOuter)) {
			return false;
		} else if(!equal(this.zMax, Disk3D.class.cast(object).zMax)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the inner radius of this {@code Disk3D} instance.
	 * 
	 * @return the inner radius of this {@code Disk3D} instance
	 */
	public double getRadiusInner() {
		return this.radiusInner;
	}
	
	/**
	 * Returns the outer radius of this {@code Disk3D} instance.
	 * 
	 * @return the outer radius of this {@code Disk3D} instance
	 */
	public double getRadiusOuter() {
		return this.radiusOuter;
	}
	
	/**
	 * Returns the surface area of this {@code Disk3D} instance.
	 * 
	 * @return the surface area of this {@code Disk3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		return this.phiMax.getRadians() * 0.5D * (this.radiusOuter * this.radiusOuter - this.radiusInner * this.radiusInner);
	}
	
	/**
	 * Returns the maximum Z of this {@code Disk3D} instance.
	 * 
	 * @return the maximum Z of this {@code Disk3D} instance
	 */
	public double getZMax() {
		return this.zMax;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Disk3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Disk3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		if(isZero(direction.getZ())) {
			return Double.NaN;
		}
		
		final double t = (this.zMax - origin.getZ()) / direction.getZ();
		
		if(t <= tMinimum || t >= tMaximum) {
			return Double.NaN;
		}
		
		final Point3D point = Point3D.add(origin, direction, t);
		
		final double x = point.getX();
		final double y = point.getY();
		
		final double distanceSquared = x * x + y * y;
		
		final double radiusInner = this.radiusInner;
		final double radiusOuter = this.radiusOuter;
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return Double.NaN;
		}
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		final double phiMax = this.phiMax.getRadians();
		
		if(phi > phiMax) {
			return Double.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code double[]} representation of this {@code Disk3D} instance.
	 * 
	 * @return a {@code double[]} representation of this {@code Disk3D} instance
	 */
	public double[] toArray() {
		final double[] array = new double[ARRAY_LENGTH];
		
		array[ARRAY_OFFSET_PHI_MAX] = this.phiMax.getRadians();
		array[ARRAY_OFFSET_RADIUS_INNER] = this.radiusInner;
		array[ARRAY_OFFSET_RADIUS_OUTER] = this.radiusOuter;
		array[ARRAY_OFFSET_Z_MAX] = this.zMax;
		
		return array;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Disk3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Disk3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Disk3D} instance.
	 * 
	 * @return a hash code for this {@code Disk3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, Double.valueOf(this.radiusInner), Double.valueOf(this.radiusOuter), Double.valueOf(this.zMax));
	}
}