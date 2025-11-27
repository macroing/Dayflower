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
 * A {@code Hyperboloid3F} is an implementation of {@link Shape3F} that represents a hyperboloid.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@code Shape3F} implementation is supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Hyperboloid3F implements Shape3F {
	/**
	 * The name of this {@code Hyperboloid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final String NAME = "Hyperboloid";
	
	/**
	 * The ID of this {@code Hyperboloid3F} class.
	 */
//	TODO: Add Unit Tests!
	public static final int ID = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AngleF phiMax;
	private final Point3F a;
	private final Point3F b;
	private final float aH;
	private final float cH;
	private final float rMax;
	private final float zMax;
	private final float zMin;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Hyperboloid3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3F(AngleF.degrees(360.0F));
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3F() {
		this(AngleF.degrees(360.0F));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3F} instance.
	 * <p>
	 * If {@code phiMax} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3F(phiMax, new Point3F(0.0001F, 0.0001F, 0.0F));
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @throws NullPointerException thrown if, and only if, {@code phiMax} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3F(final AngleF phiMax) {
		this(phiMax, new Point3F(0.0001F, 0.0001F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3F} instance.
	 * <p>
	 * If either {@code phiMax} or {@code a} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Hyperboloid3F(phiMax, a, new Point3F(1.0F, 1.0F, 1.0F));
	 * }
	 * </pre>
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3F} instance denoted by {@code A}
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax} or {@code a} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3F(final AngleF phiMax, final Point3F a) {
		this(phiMax, a, new Point3F(1.0F, 1.0F, 1.0F));
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3F} instance.
	 * <p>
	 * If either {@code phiMax}, {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3F} instance denoted by {@code A}
	 * @param b the {@code Point3F} instance denoted by {@code B}
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax}, {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3F(final AngleF phiMax, final Point3F a, final Point3F b) {
		Objects.requireNonNull(phiMax, "phiMax == null");
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		
		Point3F pointA = Floats.isZero(a.z) ? a : Floats.isZero(b.z) ? b : a;
		Point3F pointB = Floats.isZero(a.z) ? b : Floats.isZero(b.z) ? a : b;
		Point3F pointC = pointA;
		
		float aH = Float.POSITIVE_INFINITY;
		float cH = Float.POSITIVE_INFINITY;
		
		for(int i = 0; i < 10 && (Floats.isInfinite(aH) || Floats.isNaN(aH)); i++) {
			pointC = Point3F.add(pointC, Vector3F.multiply(Vector3F.direction(pointA, pointB), 2.0F));
			
			final float c = pointC.x * pointC.x + pointC.y * pointC.y;
			final float d = pointB.x * pointB.x + pointB.y * pointB.y;
			
			aH = (1.0F / c - (pointC.z * pointC.z) / (c * pointB.z * pointB.z)) / (1.0F - (d * pointC.z * pointC.z) / (c * pointB.z * pointB.z));
			cH = (aH * d - 1.0F) / (pointB.z * pointB.z);
		}
		
		if(Floats.isInfinite(aH) || Floats.isNaN(aH)) {
			throw new IllegalArgumentException();
		}
		
		this.phiMax = phiMax;
		this.a = pointA;
		this.b = pointB;
		this.aH = aH;
		this.cH = cH;
		this.rMax = Floats.max(Floats.sqrt(a.x * a.x + a.y * a.y), Floats.sqrt(b.x * b.x + b.y * b.y));
		this.zMax = Floats.max(a.z, b.z);
		this.zMin = Floats.min(a.z, b.z);
	}
	
	/**
	 * Constructs a new {@code Hyperboloid3F} instance.
	 * <p>
	 * If either {@code phiMax}, {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param phiMax the maximum phi
	 * @param a the {@link Point3F} instance denoted by {@code A}
	 * @param b the {@link Point3F} instance denoted by {@code B}
	 * @param aH a {@code float} value
	 * @param cH a {@code float} value
	 * @param rMax the maximum radius
	 * @param zMax the maximum Z value
	 * @param zMin the minimum Z value
	 * @throws NullPointerException thrown if, and only if, either {@code phiMax}, {@code a} or {@code b} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public Hyperboloid3F(final AngleF phiMax, final Point3F a, final Point3F b, final float aH, final float cH, final float rMax, final float zMax, final float zMin) {
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
	 * Returns the maximum phi of this {@code Hyperboloid3F} instance.
	 * 
	 * @return the maximum phi of this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	public AngleF getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new AxisAlignedBoundingBox3F(new Point3F(-this.rMax, -this.rMax, this.zMin), new Point3F(this.rMax, this.rMax, this.zMax));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Hyperboloid3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Hyperboloid3F} instance
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
	 * Returns the {@link Point3F} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code A}
	 */
//	TODO: Add Unit Tests!
	public Point3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code B}
	 */
//	TODO: Add Unit Tests!
	public Point3F getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new Paraboloid3F(%s, %s, %s)", this.phiMax, this.a, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code point} is contained in this {@code Hyperboloid3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param point a {@link Point3F} instance
	 * @return {@code true} if, and only if, {@code point} is contained in this {@code Hyperboloid3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean contains(final Point3F point) {
		return point.z >= this.zMin && point.z <= this.zMax && doComputePhi(point) <= this.phiMax.getRadians();
	}
	
	/**
	 * Compares {@code object} to this {@code Hyperboloid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Hyperboloid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Hyperboloid3F)) {
			return false;
		} else if(!Objects.equals(this.phiMax, Hyperboloid3F.class.cast(object).phiMax)) {
			return false;
		} else if(!Objects.equals(this.a, Hyperboloid3F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Hyperboloid3F.class.cast(object).b)) {
			return false;
		} else if(!Floats.equals(this.aH, Hyperboloid3F.class.cast(object).aH)) {
			return false;
		} else if(!Floats.equals(this.cH, Hyperboloid3F.class.cast(object).cH)) {
			return false;
		} else if(!Floats.equals(this.rMax, Hyperboloid3F.class.cast(object).rMax)) {
			return false;
		} else if(!Floats.equals(this.zMax, Hyperboloid3F.class.cast(object).zMax)) {
			return false;
		} else if(!Floats.equals(this.zMin, Hyperboloid3F.class.cast(object).zMin)) {
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
	public float getAH() {
		return this.aH;
	}
	
	/**
	 * Returns the value of the variable {@code CH}.
	 * 
	 * @return the value of the variable {@code CH}
	 */
//	TODO: Add Unit Tests!
	public float getCH() {
		return this.cH;
	}
	
	/**
	 * Returns the maximum radius.
	 * 
	 * @return the maximum radius
	 */
//	TODO: Add Unit Tests!
	public float getRMax() {
		return this.rMax;
	}
	
	/**
	 * Returns the surface area of this {@code Hyperboloid3F} instance.
	 * 
	 * @return the surface area of this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public float getSurfaceArea() {
		final float aX11 = this.a.x;
		final float aX21 = aX11 * aX11;
		final float aX31 = aX21 * aX11;
		final float aX41 = aX31 * aX11;
		final float aX42 = aX41 * 2.0F;
		final float aY11 = this.a.y;
		final float aY21 = aY11 * aY11;
		final float aY25 = aY21 * 5.0F;
		final float aZ11 = this.a.z;
		final float aZ21 = aZ11 * aZ11;
		
		final float bX11 = this.b.x;
		final float bX21 = bX11 * bX11;
		final float bX31 = bX21 * bX11;
		final float bX41 = bX31 * bX11;
		final float bX42 = bX41 * 2.0F;
		final float bY11 = this.b.y;
		final float bY21 = bY11 * bY11;
		final float bY24 = bY21 * 4.0F;
		final float bY25 = bY21 * 5.0F;
		final float bZ11 = this.b.z;
		final float bZ21 = bZ11 * bZ11;
		
		final float cX11 = aX11 * bX11;
		final float cX12 = cX11 * 2.0F;
		final float cY11 = aY11 * bY11;
		final float cY12 = cY11 * 2.0F;
		final float cY15 = cY11 * 5.0F;
		final float cZ11 = aZ11 * bZ11;
		final float cZ12 = cZ11 * 2.0F;
		
		final float dY11 = (aY11 - bY11) * (aY11 - bY11);
		final float dZ11 = (aZ11 - bZ11) * (aZ11 - bZ11);
		final float dZ12 = dZ11 * 2.0F;
		
		final float a = aX42;
		final float b = aX31 * bX11 * 2.0F;
		final float c = bX42;
		final float d = (aY21 + cY11 + bY21) * (dY11 + dZ11) * 2.0F;
		final float e = bX21 * (aY25 + cY12 - bY24 + dZ12);
		final float f = aX21 * ((aY21 * -4.0F) + cY12 + bY25 + dZ12);
		final float g = cX12 * (bX21 - aY21 + cY15 - bY21 - aZ21 + cZ12 - bZ21);
		final float h = a - b + c + d + e + f - g;
		
		final float phiMax = this.phiMax.getRadians();
		
		final float surfaceArea = phiMax / 6.0F * h;
		
		return surfaceArea;
	}
	
	/**
	 * Returns the maximum Z value.
	 * 
	 * @return the maximum Z value
	 */
//	TODO: Add Unit Tests!
	public float getZMax() {
		return this.zMax;
	}
	
	/**
	 * Returns the minimum Z value.
	 * 
	 * @return the minimum Z value
	 */
//	TODO: Add Unit Tests!
	public float getZMin() {
		return this.zMin;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Hyperboloid3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Hyperboloid3F} instance
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
		
		final float a = this.aH * d.x * d.x + this.aH * d.y * d.y - this.cH * d.z * d.z;
		final float b = 2.0F * (this.aH * d.x * o.x + this.aH * d.y * o.y - this.cH * d.z * o.z);
		final float c = this.aH * o.x * o.x + this.aH * o.y * o.y - this.cH * o.z * o.z - 1.0F;
		
		final float[] ts = Floats.solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final float t = ts[i];
			
			if(Floats.isNaN(t)) {
				return Float.NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final Point3F p = doCreateSurfaceIntersectionPoint(ray, t);
				
				if(p.z >= this.zMin && p.z <= this.zMax && doComputePhi(p) <= this.phiMax.getRadians()) {
					return t;
				}
			}
		}
		
		return Float.NaN;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Hyperboloid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Hyperboloid3F} instance.
	 * 
	 * @return a hash code for this {@code Hyperboloid3F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(this.phiMax, this.a, this.b, Float.valueOf(this.aH), Float.valueOf(this.cH), Float.valueOf(this.rMax), Float.valueOf(this.zMax), Float.valueOf(this.zMin));
	}
	
	/**
	 * Writes this {@code Hyperboloid3F} instance to {@code dataOutput}.
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
			
			dataOutput.writeFloat(this.aH);
			dataOutput.writeFloat(this.cH);
			dataOutput.writeFloat(this.rMax);
			dataOutput.writeFloat(this.zMax);
			dataOutput.writeFloat(this.zMin);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private OrthonormalBasis33F doCreateOrthonormalBasisG(final Point3F surfaceIntersectionPoint) {
		final float phi = doComputePhi(surfaceIntersectionPoint);
		final float phiCos = Floats.cos(phi);
		final float phiSin = Floats.sin(phi);
		
		final float uX = -this.phiMax.getRadians() * surfaceIntersectionPoint.y;
		final float uY = +this.phiMax.getRadians() * surfaceIntersectionPoint.x;
		final float uZ = +0.0F;
		
		final float vX = (this.b.x - this.a.x) * phiCos - (this.b.y - this.a.y) * phiSin;
		final float vY = (this.b.x - this.a.x) * phiSin + (this.b.y - this.a.y) * phiCos;
		final float vZ = this.b.z - this.a.z;
		
		final Vector3F u = Vector3F.normalize(new Vector3F(uX, uY, uZ));
		final Vector3F v = Vector3F.normalize(new Vector3F(vX, vY, vZ));
		final Vector3F w = Vector3F.crossProduct(u, v);
		
		return new OrthonormalBasis33F(w, v, u);
	}
	
	private Point2F doCreateTextureCoordinates(final Point3F surfaceIntersectionPoint) {
		final float v = (surfaceIntersectionPoint.z - this.a.z) / (this.b.z - this.a.z);
		final float u = doComputePhi(surfaceIntersectionPoint, v) / this.phiMax.getRadians();
		
		return new Point2F(u, v);
	}
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float t) {
		final Point3F surfaceIntersectionPoint = doCreateSurfaceIntersectionPoint(ray, t);
		
		final OrthonormalBasis33F orthonormalBasisG = doCreateOrthonormalBasisG(surfaceIntersectionPoint);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = doCreateTextureCoordinates(surfaceIntersectionPoint);
		
		return new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, t);
	}
	
	private float doComputePhi(final Point3F surfaceIntersectionPoint) {
		return doComputePhi(surfaceIntersectionPoint, (surfaceIntersectionPoint.z - this.a.z) / (this.b.z - this.a.z));
	}
	
	private float doComputePhi(final Point3F surfaceIntersectionPoint, final float v) {
		final Point3F a = Point3F.lerp(this.a, this.b, v);
		final Point3F b = new Point3F(surfaceIntersectionPoint.x * a.x + surfaceIntersectionPoint.y * a.y, surfaceIntersectionPoint.y * a.x - surfaceIntersectionPoint.x * a.y, 0.0F);
		
		return b.sphericalPhi();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doCreateSurfaceIntersectionPoint(final Ray3F ray, final float t) {
		return Point3F.add(ray.getOrigin(), ray.getDirection(), t);
	}
}