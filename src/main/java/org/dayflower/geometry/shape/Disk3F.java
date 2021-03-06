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
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.sqrt;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
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
 * A {@code Disk3F} is an implementation of {@link Shape3F} that represents a disk.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Disk3F implements Shape3F {
	/**
	 * The name of this {@code Disk3F} class.
	 */
	public static final String NAME = "Disk";
	
	/**
	 * The ID of this {@code Disk3F} class.
	 */
	public static final int ID = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF phiMax;
	private final float radiusInner;
	private final float radiusOuter;
	private final float zMax;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Disk3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3F(AngleF.degrees(360.0F));
	 * }
	 * </pre>
	 */
	public Disk3F() {
		this(AngleF.degrees(360.0F));
	}
	
	/**
	 * Constructs a new {@code Disk3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3F(phiMax, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3F(final AngleF phiMax) {
		this(phiMax, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Disk3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3F(phiMax, radiusInner, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3F(final AngleF phiMax, final float radiusInner) {
		this(phiMax, radiusInner, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Disk3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Disk3F(phiMax, radiusInner, radiusOuter, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @param radiusOuter the outer radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3F(final AngleF phiMax, final float radiusInner, final float radiusOuter) {
		this(phiMax, radiusInner, radiusOuter, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Disk3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param radiusInner the inner radius
	 * @param radiusOuter the outer radius
	 * @param zMax the maximum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Disk3F(final AngleF phiMax, final float radiusInner, final float radiusOuter, final float zMax) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.radiusInner = radiusInner;
		this.radiusOuter = radiusOuter;
		this.zMax = zMax;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Disk3F} instance.
	 * 
	 * @return the maximum phi of this {@code Disk3F} instance
	 */
	public AngleF getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Disk3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Disk3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(new Point3F(-this.radiusOuter, -this.radiusOuter, this.zMax), new Point3F(this.radiusOuter, this.radiusOuter, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Disk3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Disk3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		if(isZero(direction.getZ())) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float t = (this.zMax - origin.getZ()) / direction.getZ();
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F point = Point3F.add(origin, direction, t);
		
		final float x = point.getX();
		final float y = point.getY();
		
		final float distanceSquared = x * x + y * y;
		
		final float radiusInner = this.radiusInner;
		final float radiusOuter = this.radiusOuter;
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float phi = getOrAdd(atan2(y, x), 0.0F, PI_MULTIPLIED_BY_2);
		final float phiMax = this.phiMax.getRadians();
		
		if(phi > phiMax) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float distance = sqrt(distanceSquared);
		
		final float u = phi / phiMax;
		final float v = (radiusOuter - distance) / (radiusOuter - radiusInner);
		
		final Vector3F dPDU = Vector3F.normalize(new Vector3F(-phiMax * y, phiMax * x, 0.0F));
		final Vector3F dPDV = Vector3F.normalize(new Vector3F(x * (radiusInner - radiusOuter) / distance, y * (radiusInner - radiusOuter) / distance, 0.0F));
		
		final Vector3F surfaceNormalG = Vector3F.crossProduct(dPDU, dPDV);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormalG, dPDV, dPDU);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point3F surfaceIntersectionPoint = new Point3F(x, y, this.zMax);
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Disk3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Disk3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Disk3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Disk3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Disk3F(%s, %+.10f, %+.10f, %+.10f)", this.phiMax, Float.valueOf(this.radiusInner), Float.valueOf(this.radiusOuter), Float.valueOf(this.zMax));
	}
	
	/**
	 * Compares {@code object} to this {@code Disk3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Disk3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Disk3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Disk3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Disk3F)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Disk3F.class.cast(object).phiMax)) {
			return false;
		} else if(!equal(this.radiusInner, Disk3F.class.cast(object).radiusInner)) {
			return false;
		} else if(!equal(this.radiusOuter, Disk3F.class.cast(object).radiusOuter)) {
			return false;
		} else if(!equal(this.zMax, Disk3F.class.cast(object).zMax)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the inner radius of this {@code Disk3F} instance.
	 * 
	 * @return the inner radius of this {@code Disk3F} instance
	 */
	public float getRadiusInner() {
		return this.radiusInner;
	}
	
	/**
	 * Returns the outer radius of this {@code Disk3F} instance.
	 * 
	 * @return the outer radius of this {@code Disk3F} instance
	 */
	public float getRadiusOuter() {
		return this.radiusOuter;
	}
	
	/**
	 * Returns the surface area of this {@code Disk3F} instance.
	 * 
	 * @return the surface area of this {@code Disk3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return this.phiMax.getRadians() * 0.5F * (this.radiusOuter * this.radiusOuter - this.radiusInner * this.radiusInner);
	}
	
	/**
	 * Returns the maximum Z of this {@code Disk3F} instance.
	 * 
	 * @return the maximum Z of this {@code Disk3F} instance
	 */
	public float getZMax() {
		return this.zMax;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Disk3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Disk3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		if(isZero(direction.getZ())) {
			return Float.NaN;
		}
		
		final float t = (this.zMax - origin.getZ()) / direction.getZ();
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		final Point3F point = Point3F.add(origin, direction, t);
		
		final float x = point.getX();
		final float y = point.getY();
		
		final float distanceSquared = x * x + y * y;
		
		final float radiusInner = this.radiusInner;
		final float radiusOuter = this.radiusOuter;
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return Float.NaN;
		}
		
		final float phi = getOrAdd(atan2(y, x), 0.0F, PI_MULTIPLIED_BY_2);
		final float phiMax = this.phiMax.getRadians();
		
		if(phi > phiMax) {
			return Float.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Disk3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Disk3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Disk3F} instance.
	 * 
	 * @return a hash code for this {@code Disk3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, Float.valueOf(this.radiusInner), Float.valueOf(this.radiusOuter), Float.valueOf(this.zMax));
	}
	
	/**
	 * Writes this {@code Disk3F} instance to {@code dataOutput}.
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
			
			this.phiMax.write(dataOutput);
			
			dataOutput.writeFloat(this.radiusInner);
			dataOutput.writeFloat(this.radiusOuter);
			dataOutput.writeFloat(this.zMax);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}