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
import static org.dayflower.utility.Floats.cos;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.getOrAdd;
import static org.dayflower.utility.Floats.isInfinite;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.sin;
import static org.dayflower.utility.Floats.solveQuadraticSystem;
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
 * A {@code Hyperboloid3F} denotes a 3-dimensional hyperboloid that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@link Shape3F} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Hyperboloid3F implements Shape3F {
	/**
	 * The name of this {@code Hyperboloid3F} class.
	 */
	public static final String NAME = "Hyperboloid";
	
	/**
	 * The ID of this {@code Hyperboloid3F} class.
	 */
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
	public Hyperboloid3F(final AngleF phiMax, final Point3F a, final Point3F b) {
		Objects.requireNonNull(phiMax, "phiMax == null");
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		
		Point3F pointA = isZero(a.getZ()) ? a : isZero(b.getZ()) ? b : a;
		Point3F pointB = isZero(a.getZ()) ? b : isZero(b.getZ()) ? a : b;
		Point3F pointC = pointA;
		
		float aH = Float.POSITIVE_INFINITY;
		float cH = Float.POSITIVE_INFINITY;
		
		for(int i = 0; i < 10 && (isInfinite(aH) || isNaN(aH)); i++) {
			pointC = Point3F.add(pointC, Vector3F.multiply(Vector3F.direction(pointA, pointB), 2.0F));
			
			final float c = pointC.getX() * pointC.getX() + pointC.getY() * pointC.getY();
			final float d = pointB.getX() * pointB.getX() + pointB.getY() * pointB.getY();
			
			aH = (1.0F / c - (pointC.getZ() * pointC.getZ()) / (c * pointB.getZ() * pointB.getZ())) / (1.0F - (d * pointC.getZ() * pointC.getZ()) / (c * pointB.getZ() * pointB.getZ()));
			cH = (aH * d - 1.0F) / (pointB.getZ() * pointB.getZ());
		}
		
		if(isInfinite(aH) || isNaN(aH)) {
			throw new IllegalArgumentException();
		}
		
		this.phiMax = phiMax;
		this.a = pointA;
		this.b = pointB;
		this.aH = aH;
		this.cH = cH;
		this.rMax = max(sqrt(a.getX() * a.getX() + a.getY() * a.getY()), sqrt(b.getX() * b.getX() + b.getY() * b.getY()));
		this.zMax = max(a.getZ(), b.getZ());
		this.zMin = min(a.getZ(), b.getZ());
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
	public AngleF getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Hyperboloid3F} instance
	 */
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
		
		final float aX = this.a.getX();
		final float aY = this.a.getY();
		final float aZ = this.a.getZ();
		final float bX = this.b.getX();
		final float bY = this.b.getY();
		final float bZ = this.b.getZ();
		
		final float aH = this.aH;
		final float cH = this.cH;
		final float phiMax = this.phiMax.getRadians();
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		
		final float a = aH * directionX * directionX + aH * directionY * directionY - cH * directionZ * directionZ;
		final float b = 2.0F * (aH * directionX * originX + aH * directionY * originY - cH * directionZ * originZ);
		final float c = aH * originX * originX + aH * originY * originY - cH * originZ * originZ - 1.0F;
		
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
		final float vClosest = (zClosest - aZ) / (bZ - aZ);
		final float xClosestR = (1.0F - vClosest) * aX + vClosest * bX;
		final float yClosestR = (1.0F - vClosest) * aY + vClosest * bY;
		
		final float phiClosest = getOrAdd(atan2(yClosest * xClosestR - xClosest * yClosestR, xClosest * xClosestR + yClosest * yClosestR), 0.0F, PI_MULTIPLIED_BY_2);
		
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
			final float vClipped = (zClipped - aZ) / (bZ - aZ);
			final float xClippedR = (1.0F - vClipped) * aX + vClipped * bX;
			final float yClippedR = (1.0F - vClipped) * aY + vClipped * bY;
			
			final float phiClipped = getOrAdd(atan2(yClipped * xClippedR - xClipped * yClippedR, xClipped * xClippedR + yClipped * yClippedR), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return SurfaceIntersection3F.EMPTY;
			}
			
			return Optional.of(doCreateSurfaceIntersection(ray, phiClipped, tClipped, vClipped, xClipped, yClipped, zClipped));
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, phiClosest, tClosest, vClosest, xClosest, yClosest, zClosest));
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code A}
	 */
	public Point3F getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3F} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code B}
	 */
	public Point3F getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Hyperboloid3F} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Hyperboloid3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Hyperboloid3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Paraboloid3F(%s, %s, %s)", this.phiMax, this.a, this.b);
	}
	
	/**
	 * Compares {@code object} to this {@code Hyperboloid3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Hyperboloid3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3F}, and their respective values are equal, {@code false} otherwise
	 */
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
		} else if(!equal(this.aH, Hyperboloid3F.class.cast(object).aH)) {
			return false;
		} else if(!equal(this.cH, Hyperboloid3F.class.cast(object).cH)) {
			return false;
		} else if(!equal(this.rMax, Hyperboloid3F.class.cast(object).rMax)) {
			return false;
		} else if(!equal(this.zMax, Hyperboloid3F.class.cast(object).zMax)) {
			return false;
		} else if(!equal(this.zMin, Hyperboloid3F.class.cast(object).zMin)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the surface area of this {@code Hyperboloid3F} instance.
	 * 
	 * @return the surface area of this {@code Hyperboloid3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final float aX = this.a.getX();
		final float aXQuad = (aX * aX) * (aX * aX);
		final float aY = this.a.getY();
		final float aZ = this.a.getZ();
		final float bX = this.b.getX();
		final float bXQuad = (bX * bX) * (bX * bX);
		final float bY = this.b.getY();
		final float bZ = this.b.getZ();
		final float aYBYSquared = (aY - bY) * (aY - bY);
		final float aZBZSquared = (aZ - bZ) * (aZ - bZ);
		
		final float c = aY * aY + aY * bY + bY * bY;
		final float d = aYBYSquared + aZBZSquared;
		final float e = 5.0F * aY * aY + 2.0F * aY * bY - 4.0F * bY * bY + 2.0F * aZBZSquared;
		final float f = -4.0F * aY * aY + 2 * aY * bY + 5.0F * bY * bY + 2.0F * aZBZSquared;
		final float g = bX * bX - aY * aY + 5.0F * aY * bY - bY * bY - aZ * aZ + 2.0F * aZ * bZ - bZ * bZ;
		final float h = 2.0F * aXQuad - 2.0F * aX * aX * aX * bX + 2.0F * bXQuad + 2.0F * c * d + bX * bX * e + aX * aX * f - 2.0F * aX * bX * g;
		
		return this.phiMax.getRadians() / 6.0F * h;
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
		
		final float aX = this.a.getX();
		final float aY = this.a.getY();
		final float aZ = this.a.getZ();
		final float bX = this.b.getX();
		final float bY = this.b.getY();
		final float bZ = this.b.getZ();
		
		final float aH = this.aH;
		final float cH = this.cH;
		final float phiMax = this.phiMax.getRadians();
		final float zMax = this.zMax;
		final float zMin = this.zMin;
		
		final float a = aH * directionX * directionX + aH * directionY * directionY - cH * directionZ * directionZ;
		final float b = 2.0F * (aH * directionX * originX + aH * directionY * originY - cH * directionZ * originZ);
		final float c = aH * originX * originX + aH * originY * originY - cH * originZ * originZ - 1.0F;
		
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
		final float vClosest = (zClosest - aZ) / (bZ - aZ);
		final float xClosestR = (1.0F - vClosest) * aX + vClosest * bX;
		final float yClosestR = (1.0F - vClosest) * aY + vClosest * bY;
		
		final float phiClosest = getOrAdd(atan2(yClosest * xClosestR - xClosest * yClosestR, xClosest * xClosestR + yClosest * yClosestR), 0.0F, PI_MULTIPLIED_BY_2);
		
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
			final float vClipped = (zClipped - aZ) / (bZ - aZ);
			final float xClippedR = (1.0F - vClipped) * aX + vClipped * bX;
			final float yClippedR = (1.0F - vClipped) * aY + vClipped * bY;
			
			final float phiClipped = getOrAdd(atan2(yClipped * xClippedR - xClipped * yClippedR, xClipped * xClippedR + yClipped * yClippedR), 0.0F, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return Float.NaN;
			}
			
			return tClipped;
		}
		
		return tClosest;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Hyperboloid3F} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Hyperboloid3F} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Hyperboloid3F} instance.
	 * 
	 * @return a hash code for this {@code Hyperboloid3F} instance
	 */
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
	
	private SurfaceIntersection3F doCreateSurfaceIntersection(final Ray3F ray, final float phi, final float t, final float v, final float x, final float y, final float z) {
		final float aX = this.a.getX();
		final float aY = this.a.getY();
		final float aZ = this.a.getZ();
		final float bX = this.b.getX();
		final float bY = this.b.getY();
		final float bZ = this.b.getZ();
		
		final float phiMax = this.phiMax.getRadians();
		
		final float u = phi / phiMax;
		
		final float cosPhi = cos(phi);
		final float sinPhi = sin(phi);
		
		final Vector3F dPDU = Vector3F.normalize(new Vector3F(-phiMax * y, phiMax * x, 0.0F));
		final Vector3F dPDV = Vector3F.normalize(new Vector3F((bX - aX) * cosPhi - (bY - aY) * sinPhi, (bX - aX) * sinPhi + (bY - aY) * cosPhi, bZ - aZ));
		
//		final Vector3F d2PDUU = new Vector3F(-phiMax * phiMax * x, -phiMax * phiMax * y, 0.0F);
//		final Vector3F d2PDUV = new Vector3F(phiMax * -dPDV.getY(), phiMax * dPDV.getX(), 0.0F);
//		final Vector3F d2PDVV = new Vector3F(0.0F, 0.0F, 0.0F);
		
//		final float e0 = Vector3F.dotProduct(dPDU, dPDU);
//		final float f0 = Vector3F.dotProduct(dPDU, dPDV);
//		final float g0 = Vector3F.dotProduct(dPDV, dPDV);
		
		final Vector3F surfaceNormalG = Vector3F.crossProduct(dPDU, dPDV);
		
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