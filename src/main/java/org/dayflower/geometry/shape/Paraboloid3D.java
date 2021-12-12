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

import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;

import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
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
 * A {@code Paraboloid3D} is an implementation of {@link Shape3D} that represents a paraboloid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Paraboloid3D implements Shape3D {
	/**
	 * The name of this {@code Paraboloid3D} class.
	 */
	public static final String NAME = "Paraboloid";
	
	/**
	 * The ID of this {@code Paraboloid3D} class.
	 */
	public static final int ID = 9;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleD phiMax;
	private final double radius;
	private final double zMax;
	private final double zMin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Paraboloid3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3D(AngleD.degrees(360.0D));
	 * }
	 * </pre>
	 */
	public Paraboloid3D() {
		this(AngleD.degrees(360.0D));
	}
	
	/**
	 * Constructs a new {@code Paraboloid3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3D(phiMax, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Paraboloid3D(final AngleD phiMax) {
		this(phiMax, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3D(phiMax, radius, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Paraboloid3D(final AngleD phiMax, final double radius) {
		this(phiMax, radius, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Paraboloid3D(phiMax, radius, zMax, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Paraboloid3D(final AngleD phiMax, final double radius, final double zMax) {
		this(phiMax, radius, zMax, 0.0D);
	}
	
	/**
	 * Constructs a new {@code Paraboloid3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param radius the radius
	 * @param zMax the maximum Z
	 * @param zMin the minimum Z
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
	public Paraboloid3D(final AngleD phiMax, final double radius, final double zMax, final double zMin) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.radius = radius;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Paraboloid3D} instance.
	 * 
	 * @return the maximum phi of this {@code Paraboloid3D} instance
	 */
	public AngleD getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Paraboloid3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Paraboloid3D} instance
	 */
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(new Point3D(-this.radius, -this.radius, this.zMin), new Point3D(this.radius, this.radius, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Paraboloid3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Paraboloid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3D} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final double t = intersectionT(ray, tMinimum, tMaximum);
		
		if(isNaN(t)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, t));
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Paraboloid3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Paraboloid3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Paraboloid3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Paraboloid3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Paraboloid3D(%s, %+.10f, %+.10f, %+.10f)", this.phiMax, Double.valueOf(this.radius), Double.valueOf(this.zMax), Double.valueOf(this.zMin));
	}
	
	/**
	 * Compares {@code object} to this {@code Paraboloid3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Paraboloid3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Paraboloid3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Paraboloid3D)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Paraboloid3D.class.cast(object).phiMax)) {
			return false;
		} else if(!equal(this.radius, Paraboloid3D.class.cast(object).radius)) {
			return false;
		} else if(!equal(this.zMax, Paraboloid3D.class.cast(object).zMax)) {
			return false;
		} else if(!equal(this.zMin, Paraboloid3D.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the radius of this {@code Paraboloid3D} instance.
	 * 
	 * @return the radius of this {@code Paraboloid3D} instance
	 */
	public double getRadius() {
		return this.radius;
	}
	
	/**
	 * Returns the surface area of this {@code Paraboloid3D} instance.
	 * 
	 * @return the surface area of this {@code Paraboloid3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		final double phiMax = this.phiMax.getRadians();
		final double radius = this.radius;
		final double radiusSquared = radius * radius;
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		final double k = 4.0D * zMax / radiusSquared;
		final double a = radiusSquared * radiusSquared * phiMax / (12.0D * zMax * zMax);
		final double b = pow(k * zMax + 1.0D, 1.5D) - pow(k * zMin + 1.0D, 1.5D);
		
		return a * b;
	}
	
	/**
	 * Returns the maximum Z of this {@code Paraboloid3D} instance.
	 * 
	 * @return the maximum Z of this {@code Paraboloid3D} instance
	 */
	public double getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z of this {@code Paraboloid3D} instance.
	 * 
	 * @return the minimum Z of this {@code Paraboloid3D} instance
	 */
	public double getZMin() {
		return this.zMin;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Paraboloid3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Paraboloid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double k = this.zMax / (this.radius * this.radius);
		
		final double a = k * (direction.getX() * direction.getX() + direction.getY() * direction.getY());
		final double b = 2.0D * k * (direction.getX() * origin.getX() + direction.getY() * origin.getY()) - direction.getZ();
		final double c = k * (origin.getX() * origin.getX() + origin.getY() * origin.getY()) - origin.getZ();
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return Double.NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
				
				if(surfaceIntersectionPoint.getZ() >= this.zMin && surfaceIntersectionPoint.getZ() <= this.zMax && surfaceIntersectionPoint.sphericalPhi() <= this.phiMax.getRadians()) {
					return t;
				}
			}
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Paraboloid3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Paraboloid3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Paraboloid3D} instance.
	 * 
	 * @return a hash code for this {@code Paraboloid3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, Double.valueOf(this.radius), Double.valueOf(this.zMax), Double.valueOf(this.zMin));
	}
	
	/**
	 * Writes this {@code Paraboloid3D} instance to {@code dataOutput}.
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
			
			dataOutput.writeDouble(this.radius);
			dataOutput.writeDouble(this.zMax);
			dataOutput.writeDouble(this.zMin);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG(final Point3D surfaceIntersectionPoint) {
		final double uX = -this.phiMax.getRadians() * surfaceIntersectionPoint.getY();
		final double uY = +this.phiMax.getRadians() * surfaceIntersectionPoint.getX();
		final double uZ = +0.0D;
		
		final double vX = (this.zMax - this.zMin) * (surfaceIntersectionPoint.getX() / (2.0D * surfaceIntersectionPoint.getZ()));
		final double vY = (this.zMax - this.zMin) * (surfaceIntersectionPoint.getY() / (2.0D * surfaceIntersectionPoint.getZ()));
		final double vZ = this.zMax - this.zMin;
		
		final Vector3D u = Vector3D.normalize(new Vector3D(uX, uY, uZ));
		final Vector3D v = Vector3D.normalize(new Vector3D(vX, vY, vZ));
		final Vector3D w = Vector3D.crossProduct(u, v);
		
		return new OrthonormalBasis33D(w, v, u);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final double u = surfaceIntersectionPoint.sphericalPhi() / this.phiMax.getRadians();
		final double v = (surfaceIntersectionPoint.getZ() - this.zMin) / (this.zMax - this.zMin);
		
		return new Point2D(u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}