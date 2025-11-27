/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;

import org.macroing.java.lang.Floats;

/**
 * A {@code Paraboloid3F} is an implementation of {@link Shape3F} that represents a paraboloid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Paraboloid3F implements Shape3F {
	/**
	 * The name of this {@code Paraboloid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Paraboloid";
	
	/**
	 * The ID of this {@code Paraboloid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF phiMax;
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
	 * new Paraboloid3F(AngleF.degrees(360.0F));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Paraboloid3F() {
		this(AngleF.degrees(360.0F));
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3F(phiMax, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Paraboloid3F(final AngleF phiMax) {
		this(phiMax, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
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
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Paraboloid3F(final AngleF phiMax, final float radius) {
		this(phiMax, radius, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
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
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Paraboloid3F(final AngleF phiMax, final float radius, final float zMax) {
		this(phiMax, radius, zMax, 0.0F);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @param zMin the minimum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Paraboloid3F(final AngleF phiMax, final float radius, final float zMax, final float zMin) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.radius = radius;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Paraboloid3F} instance.
	 * 
	 * @return the maximum phi of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	public AngleF getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final float t = intersectionT(ray, tMinimum, tMaximum);
		
		if(Floats.isNaN(t)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Paraboloid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Paraboloid3F(%s, %+.10f, %+.10f, %+.10f)", this.phiMax, Float.valueOf(this.radius), Float.valueOf(this.zMax), Float.valueOf(this.zMin));
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Paraboloid3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Paraboloid3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		return point.z >= this.zMin && point.z <= this.zMax && point.sphericalPhi() <= this.phiMax.getRadians();
	}
	
	/**
	 * Compares {@code object} to this {@code Paraboloid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Paraboloid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Paraboloid3F)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Paraboloid3F.class.cast(object).phiMax)) {
			return false;
		} else if(!Floats.equals(this.radius, Paraboloid3F.class.cast(object).radius)) {
			return false;
		} else if(!Floats.equals(this.zMax, Paraboloid3F.class.cast(object).zMax)) {
			return false;
		} else if(!Floats.equals(this.zMin, Paraboloid3F.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code Paraboloid3F} instance.
	 * 
	 * @return the radius of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code Paraboloid3F} instance.
	 * 
	 * @return the surface area of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		final float phiMax = this.phiMax.getRadians();
		final float radius = this.radius;
		final float radiusSquared = radius * radius;
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		final float k = 4.0F * zMax / radiusSquared;
		final float a = radiusSquared * radiusSquared * phiMax / (12.0F * zMax * zMax);
		final float b = Floats.pow(k * zMax + 1.0F, 1.5F) - Floats.pow(k * zMin + 1.0F, 1.5F);
		
		return a * b;
	}
	
	/**
	 * Returns the maximum Z of this {@code Paraboloid3F} instance.
	 * 
	 * @return the maximum Z of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	public float getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z of this {@code Paraboloid3F} instance.
	 * 
	 * @return the minimum Z of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	public float getZMin() {
		return this.zMin;
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
//	TODO: Add Unit Tests!
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Point3F o = ray.getOrigin();
		
		final Vector3F d = ray.getDirection();
		
		final float k = this.zMax / (this.radius * this.radius);
		
		final float a = k * (d.x * d.x + d.y * d.y);
		final float b = 2.0F * k * (d.x * o.x + d.y * o.y) - d.z;
		final float c = k * (o.x * o.x + o.y * o.y) - o.z;
		
		final float[] ts = Floats.solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final float t = ts[i];
			
			if(Floats.isNaN(t)) {
				return Float.NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final Point3F p = doCreateSurfaceIntersectionPoint(ray, t);
				
				if(p.z >= this.zMin && p.z <= this.zMax && p.sphericalPhi() <= this.phiMax.getRadians()) {
					return t;
				}
			}
		}
		
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Paraboloid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Paraboloid3F} instance.
	 * 
	 * @return a hash code for this {@code Paraboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, Float.valueOf(this.radius), Float.valueOf(this.zMax), Float.valueOf(this.zMin));
	}
	
	/**
	 * Writes this {@code Paraboloid3F} instance to {@code dataOutput}.
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
			
			dataOutput.writeFloat(this.radius);
			dataOutput.writeFloat(this.zMax);
			dataOutput.writeFloat(this.zMin);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG(final Point3F surfaceIntersectionPoint) {
		final float uX = -this.phiMax.getRadians() * surfaceIntersectionPoint.y;
		final float uY = +this.phiMax.getRadians() * surfaceIntersectionPoint.x;
		final float uZ = +0.0F;
		
		final float vX = (this.zMax - this.zMin) * (surfaceIntersectionPoint.x / (2.0F * surfaceIntersectionPoint.z));
		final float vY = (this.zMax - this.zMin) * (surfaceIntersectionPoint.y / (2.0F * surfaceIntersectionPoint.z));
		final float vZ = this.zMax - this.zMin;
		
		final Vector3F u = Vector3F.normalize(new Vector3F(uX, uY, uZ));
		final Vector3F v = Vector3F.normalize(new Vector3F(vX, vY, vZ));
		final Vector3F w = Vector3F.crossProduct(u, v);
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint) {
		final float u = surfaceIntersectionPoint.sphericalPhi() / this.phiMax.getRadians();
		final float v = (surfaceIntersectionPoint.z - this.zMin) / (this.zMax - this.zMin);
		
		return new Point2F(u, v);
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		return Point3F.add(ray.getOrigin(), ray.getDirection(), t);
	}
}