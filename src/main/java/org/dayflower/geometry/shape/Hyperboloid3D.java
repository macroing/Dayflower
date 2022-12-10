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

import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.sqrt;

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
 * A {@code Hyperboloid3D} is an implementation of {@link Shape3D} that represents a hyperboloid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Hyperboloid3D implements Shape3D {
	/**
	 * The name of this {@code Hyperboloid3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Hyperboloid";
	
	/**
	 * The ID of this {@code Hyperboloid3D} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleD phiMax;
	private final Point3D a;
	private final Point3D b;
	private final double aH;
	private final double cH;
	private final double rMax;
	private final double zMax;
	private final double zMin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Hyperboloid3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3D(AngleD.degrees(360.0D));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3D() {
		this(AngleD.degrees(360.0D));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3D} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3D(phiMax, new Point3D(0.0001D, 0.0001D, 0.0D));
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3D(final AngleD phiMax) {
		this(phiMax, new Point3D(0.0001D, 0.0001D, 0.0D));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3D} instance.
	 * <p>
	 * If either {@code phiMax} or {@code a} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3D(phiMax, a, new Point3D(1.0D, 1.0D, 1.0D));
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3D} instance denoted by {@code A}
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax} or {@code a} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3D(final AngleD phiMax, final Point3D a) {
		this(phiMax, a, new Point3D(1.0D, 1.0D, 1.0D));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3D} instance.
	 * <p>
	 * If either {@code phiMax}, {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3D} instance denoted by {@code A}
	 * @param b the {@code Point3D} instance denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax}, {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3D(final AngleD phiMax, final Point3D a, final Point3D b) {
		Objects.requireNonNull(phiMax, "phiMax == null");
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		
		Point3D pointA = isZero(a.z) ? a : isZero(b.z) ? b : a;
		Point3D pointB = isZero(a.z) ? b : isZero(b.z) ? a : b;
		Point3D pointC = pointA;
		
		double aH = Double.POSITIVE_INFINITY;
		double cH = Double.POSITIVE_INFINITY;
		
		for(int i = 0; i < 10 && (isInfinite(aH) || isNaN(aH)); i++) {
			pointC = Point3D.add(pointC, Vector3D.multiply(Vector3D.direction(pointA, pointB), 2.0D));
			
			final double c = pointC.x * pointC.x + pointC.y * pointC.y;
			final double d = pointB.x * pointB.x + pointB.y * pointB.y;
			
			aH = (1.0D / c - (pointC.z * pointC.z) / (c * pointB.z * pointB.z)) / (1.0D - (d * pointC.z * pointC.z) / (c * pointB.z * pointB.z));
			cH = (aH * d - 1.0D) / (pointB.z * pointB.z);
		}
		
		if(isInfinite(aH) || isNaN(aH)) {
			throw new IllegalArgumentException();
		}
		
		this.phiMax = phiMax;
		this.a = pointA;
		this.b = pointB;
		this.aH = aH;
		this.cH = cH;
		this.rMax = max(sqrt(a.x * a.x + a.y * a.y), sqrt(b.x * b.x + b.y * b.y));
		this.zMax = max(a.z, b.z);
		this.zMin = min(a.z, b.z);
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3D} instance.
	 * <p>
	 * If either {@code phiMax}, {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3D} instance denoted by {@code A}
	 * @param b the {@link Point3D} instance denoted by {@code B}
	 * @param aH a {@code double} value
	 * @param cH a {@code double} value
	 * @param rMax the maximum radius
	 * @param zMax the maximum Z value
	 * @param zMin the minimum Z value
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax}, {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3D(final AngleD phiMax, final Point3D a, final Point3D b, final double aH, final double cH, final double rMax, final double zMax, final double zMin) {
		this.phiMax = Objects.requireNonNull(phiMax, "phiMax == null");
		this.a = Objects.requireNonNull(a, "a == null");
		this.b = Objects.requireNonNull(b, "b == null");
		this.aH = aH;
		this.cH = cH;
		this.rMax = rMax;
		this.zMax = zMax;
		this.zMin = zMin;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the maximum phi of this {@code Hyperboloid3D} instance.
	 * 
	 * @return the maximum phi of this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	public AngleD getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3D getBoundingVolume() {
		return new AxisAlignedBoundingBox3D(new Point3D(-this.rMax, -this.rMax, this.zMin), new Point3D(this.rMax, this.rMax, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Hyperboloid3D} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3D} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Hyperboloid3D} instance
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
	 * Returns the {@link Point3D} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code A}
	 */
//	TODO: Add Unit Tests!
	public Point3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code B}
	 */
//	TODO: Add Unit Tests!
	public Point3D getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Paraboloid3D(%s, %s, %s)", this.phiMax, this.a, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Hyperboloid3D} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3D} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Hyperboloid3D} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3D point) {
		return point.z >= this.zMin && point.z <= this.zMax && doComputePhi(point) <= this.phiMax.getRadians();
	}
	
	/**
	 * Compares {@code object} to this {@code Hyperboloid3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Hyperboloid3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Hyperboloid3D)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Hyperboloid3D.class.cast(object).phiMax)) {
			return false;
		} else if(!Objects.equals(this.a, Hyperboloid3D.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Hyperboloid3D.class.cast(object).b)) {
			return false;
		} else if(!equal(this.aH, Hyperboloid3D.class.cast(object).aH)) {
			return false;
		} else if(!equal(this.cH, Hyperboloid3D.class.cast(object).cH)) {
			return false;
		} else if(!equal(this.rMax, Hyperboloid3D.class.cast(object).rMax)) {
			return false;
		} else if(!equal(this.zMax, Hyperboloid3D.class.cast(object).zMax)) {
			return false;
		} else if(!equal(this.zMin, Hyperboloid3D.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of the variable {@code AH}.
	 * 
	 * @return the value of the variable {@code AH}
	 */
//	TODO: Add Unit Tests!
	public double getAH() {
		return this.aH;
	}
	
	/**
	 * Returns the value of the variable {@code CH}.
	 * 
	 * @return the value of the variable {@code CH}
	 */
//	TODO: Add Unit Tests!
	public double getCH() {
		return this.cH;
	}
	
	/**
	 * Returns the maximum radius.
	 * 
	 * @return the maximum radius
	 */
//	TODO: Add Unit Tests!
	public double getRMax() {
		return this.rMax;
	}
	
	/**
	 * Returns the surface area of this {@code Hyperboloid3D} instance.
	 * 
	 * @return the surface area of this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public double getSurfaceArea() {
		final double aX11 = this.a.x;
		final double aX21 = aX11 * aX11;
		final double aX31 = aX21 * aX11;
		final double aX41 = aX31 * aX11;
		final double aX42 = aX41 * 2.0D;
		final double aY11 = this.a.y;
		final double aY21 = aY11 * aY11;
		final double aY25 = aY21 * 5.0D;
		final double aZ11 = this.a.z;
		final double aZ21 = aZ11 * aZ11;
		
		final double bX11 = this.b.x;
		final double bX21 = bX11 * bX11;
		final double bX31 = bX21 * bX11;
		final double bX41 = bX31 * bX11;
		final double bX42 = bX41 * 2.0D;
		final double bY11 = this.b.y;
		final double bY21 = bY11 * bY11;
		final double bY24 = bY21 * 4.0D;
		final double bY25 = bY21 * 5.0D;
		final double bZ11 = this.b.z;
		final double bZ21 = bZ11 * bZ11;
		
		final double cX11 = aX11 * bX11;
		final double cX12 = cX11 * 2.0D;
		final double cY11 = aY11 * bY11;
		final double cY12 = cY11 * 2.0D;
		final double cY15 = cY11 * 5.0D;
		final double cZ11 = aZ11 * bZ11;
		final double cZ12 = cZ11 * 2.0D;
		
		final double dY11 = (aY11 - bY11) * (aY11 - bY11);
		final double dZ11 = (aZ11 - bZ11) * (aZ11 - bZ11);
		final double dZ12 = dZ11 * 2.0D;
		
		final double a = aX42;
		final double b = aX31 * bX11 * 2.0D;
		final double c = bX42;
		final double d = (aY21 + cY11 + bY21) * (dY11 + dZ11) * 2.0D;
		final double e = bX21 * (aY25 + cY12 - bY24 + dZ12);
		final double f = aX21 * ((aY21 * -4.0D) + cY12 + bY25 + dZ12);
		final double g = cX12 * (bX21 - aY21 + cY15 - bY21 - aZ21 + cZ12 - bZ21);
		final double h = a - b + c + d + e + f - g;
		
		final double phiMax = this.phiMax.getRadians();
		
		final double surfaceArea = phiMax / 6.0D * h;
		
		return surfaceArea;
	}
	
	/**
	 * Returns the maximum Z value.
	 * 
	 * @return the maximum Z value
	 */
//	TODO: Add Unit Tests!
	public double getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z value.
	 * 
	 * @return the minimum Z value
	 */
//	TODO: Add Unit Tests!
	public double getZMin() {
		return this.zMin;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Hyperboloid3D} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3D} to perform an intersection test against this {@code Hyperboloid3D} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Double.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D o = ray.getOrigin();
		
		final Vector3D d = ray.getDirection();
		
		final double a = this.aH * d.x * d.x + this.aH * d.y * d.y - this.cH * d.z * d.z;
		final double b = 2.0D * (this.aH * d.x * o.x + this.aH * d.y * o.y - this.cH * d.z * o.z);
		final double c = this.aH * o.x * o.x + this.aH * o.y * o.y - this.cH * o.z * o.z - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return Double.NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final Point3D p = doCreateSurfaceIntersectionPoint(ray, t);
				
				if(p.z >= this.zMin && p.z <= this.zMax && doComputePhi(p) <= this.phiMax.getRadians()) {
					return t;
				}
			}
		}
		
		return Double.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Hyperboloid3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Hyperboloid3D} instance.
	 * 
	 * @return a hash code for this {@code Hyperboloid3D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, this.a, this.b, Double.valueOf(this.aH), Double.valueOf(this.cH), Double.valueOf(this.rMax), Double.valueOf(this.zMax), Double.valueOf(this.zMin));
	}
	
	/**
	 * Writes this {@code Hyperboloid3D} instance to {@code dataOutput}.
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
			this.a.write(dataOutput);
			this.b.write(dataOutput);
			
			dataOutput.writeDouble(this.aH);
			dataOutput.writeDouble(this.cH);
			dataOutput.writeDouble(this.rMax);
			dataOutput.writeDouble(this.zMax);
			dataOutput.writeDouble(this.zMin);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33D doCreateOrthonormalBasisG(final Point3D surfaceIntersectionPoint) {
		final double phi = doComputePhi(surfaceIntersectionPoint);
		final double phiCos = cos(phi);
		final double phiSin = sin(phi);
		
		final double uX = -this.phiMax.getRadians() * surfaceIntersectionPoint.y;
		final double uY = +this.phiMax.getRadians() * surfaceIntersectionPoint.x;
		final double uZ = +0.0D;
		
		final double vX = (this.b.x - this.a.x) * phiCos - (this.b.y - this.a.y) * phiSin;
		final double vY = (this.b.x - this.a.x) * phiSin + (this.b.y - this.a.y) * phiCos;
		final double vZ = this.b.z - this.a.z;
		
		final Vector3D u = Vector3D.normalize(new Vector3D(uX, uY, uZ));
		final Vector3D v = Vector3D.normalize(new Vector3D(vX, vY, vZ));
		final Vector3D w = Vector3D.crossProduct(u, v);
		
		return new OrthonormalBasis33D(w, v, u);
	}
	
	private Point2D doCreateTextureCoordinates(final Point3D surfaceIntersectionPoint) {
		final double v = (surfaceIntersectionPoint.z - this.a.z) / (this.b.z - this.a.z);
		final double u = doComputePhi(surfaceIntersectionPoint, v) / this.phiMax.getRadians();
		
		return new Point2D(u, v);
	}
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double t) {
		final Point3D surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33D orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33D orthonormalBasisS = orthonormalBasisG;
		
		final Point2D textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	private double doComputePhi(final Point3D surfaceIntersectionPoint) {
		return doComputePhi(surfaceIntersectionPoint, (surfaceIntersectionPoint.z - this.a.z) / (this.b.z - this.a.z));
	}
	
	private double doComputePhi(final Point3D surfaceIntersectionPoint, final double v) {
		final Point3D a = Point3D.lerp(this.a, this.b, v);
		final Point3D b = new Point3D(surfaceIntersectionPoint.x * a.x + surfaceIntersectionPoint.y * a.y, surfaceIntersectionPoint.y * a.x - surfaceIntersectionPoint.x * a.y, 0.0D);
		
		return b.sphericalPhi();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3D doCreateSurfaceIntersectionPoint(final Ray3D ray, final double t) {
		return Point3D.add(ray.getOrigin(), ray.getDirection(), t);
	}
}