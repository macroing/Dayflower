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

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
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
import org.dayflower.geometry.Vector2F;
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
//	TODO: Add Unit Tests!
	public static final String NAME = "Disk";
	
	/**
	 * The ID of this {@code Disk3F} class.
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public AngleF getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Disk3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Disk3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Disk3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Disk3F(%s, %+.10f, %+.10f, %+.10f)", this.phiMax, Float.valueOf(this.radiusInner), Float.valueOf(this.radiusOuter), Float.valueOf(this.zMax));
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Disk3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Disk3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		final float lengthSquared = new Vector2F(point).lengthSquared();
		
		return lengthSquared <= this.radiusOuter * this.radiusOuter && lengthSquared >= this.radiusInner * this.radiusInner && point.sphericalPhi() <= this.phiMax.getRadians();
	}
	
	/**
	 * Compares {@code object} to this {@code Disk3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Disk3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Disk3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Disk3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public float getRadiusInner() {
		return this.radiusInner;
	}
	
	/**
	 * Returns the outer radius of this {@code Disk3F} instance.
	 * 
	 * @return the outer radius of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
	public float getRadiusOuter() {
		return this.radiusOuter;
	}
	
	/**
	 * Returns the surface area of this {@code Disk3F} instance.
	 * 
	 * @return the surface area of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		return this.phiMax.getRadians() * 0.5F * (this.radiusOuter * this.radiusOuter - this.radiusInner * this.radiusInner);
	}
	
	/**
	 * Returns the maximum Z of this {@code Disk3F} instance.
	 * 
	 * @return the maximum Z of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F origin = ray.getOrigin();
		
		final Vector3F direction = ray.getDirection();
		
		if(isZero(direction.z)) {
			return Float.NaN;
		}
		
		final float t = (this.zMax - origin.z) / direction.z;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final float lengthSquared = new Vector2F(surfaceIntersectionPoint).lengthSquared();
		
		if(lengthSquared > this.radiusOuter * this.radiusOuter || lengthSquared < this.radiusInner * this.radiusInner || surfaceIntersectionPoint.sphericalPhi() > this.phiMax.getRadians()) {
			return Float.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Disk3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Disk3F} instance.
	 * 
	 * @return a hash code for this {@code Disk3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG(final Point3F surfaceIntersectionPoint) {
		final float length = new Vector2F(surfaceIntersectionPoint).length();
		
		final float uX = -this.phiMax.getRadians() * surfaceIntersectionPoint.y;
		final float uY = +this.phiMax.getRadians() * surfaceIntersectionPoint.x;
		final float uZ = +0.0F;
		
		final float vX = surfaceIntersectionPoint.x * (this.radiusInner - this.radiusOuter) / length;
		final float vY = surfaceIntersectionPoint.y * (this.radiusInner - this.radiusOuter) / length;
		final float vZ = 0.0F;
		
		final Vector3F u = Vector3F.normalize(new Vector3F(uX, uY, uZ));
		final Vector3F v = Vector3F.normalize(new Vector3F(vX, vY, vZ));
		final Vector3F w = Vector3F.crossProduct(u, v);
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint) {
		final float u = surfaceIntersectionPoint.sphericalPhi() / this.phiMax.getRadians();
		final float v = (this.radiusOuter - new Vector2F(surfaceIntersectionPoint).length()) / (this.radiusOuter - this.radiusInner);
		
		return new Point2F(u, v);
	}
	
	private Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = Point3F.add(ray.getOrigin(), ray.getDirection(), t);
		final Point3F surfaceIntersectionPointTransformed = new Point3F(surfaceIntersectionPoint.x, surfaceIntersectionPoint.y, this.zMax);
		
		return surfaceIntersectionPointTransformed;
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
}