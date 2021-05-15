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
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.getOrAdd;
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
 * A {@code Hyperboloid3D} denotes a 3-dimensional hyperboloid that uses the data type {@code double}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * <p>
 * This {@link Shape3D} implementation is not supported on the GPU.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Hyperboloid3D implements Shape3D {
	/**
	 * The name of this {@code Hyperboloid3D} class.
	 */
	public static final String NAME = "Hyperboloid";
	
	/**
	 * The ID of this {@code Hyperboloid3D} class.
	 */
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
	public Hyperboloid3D(final AngleD phiMax, final Point3D a, final Point3D b) {
		Objects.requireNonNull(phiMax, "phiMax == null");
		Objects.requireNonNull(a, "a == null");
		Objects.requireNonNull(b, "b == null");
		
		Point3D pointA = isZero(a.getZ()) ? a : isZero(b.getZ()) ? b : a;
		Point3D pointB = isZero(a.getZ()) ? b : isZero(b.getZ()) ? a : b;
		Point3D pointC = pointA;
		
		double aH = Double.POSITIVE_INFINITY;
		double cH = Double.POSITIVE_INFINITY;
		
		for(int i = 0; i < 10 && (isInfinite(aH) || isNaN(aH)); i++) {
			pointC = Point3D.add(pointC, Vector3D.multiply(Vector3D.direction(pointA, pointB), 2.0D));
			
			final double c = pointC.getX() * pointC.getX() + pointC.getY() * pointC.getY();
			final double d = pointB.getX() * pointB.getX() + pointB.getY() * pointB.getY();
			
			aH = (1.0D / c - (pointC.getZ() * pointC.getZ()) / (c * pointB.getZ() * pointB.getZ())) / (1.0D - (d * pointC.getZ() * pointC.getZ()) / (c * pointB.getZ() * pointB.getZ()));
			cH = (aH * d - 1.0D) / (pointB.getZ() * pointB.getZ());
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
	public AngleD getPhiMax() {
		return this.phiMax;
	}
	
	/**
	 * Returns a {@link BoundingVolume3D} instance that contains this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code BoundingVolume3D} instance that contains this {@code Hyperboloid3D} instance
	 */
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
	@Override
	public Optional<SurfaceIntersection3D> intersection(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double originX = origin.getX();
		final double originY = origin.getY();
		final double originZ = origin.getZ();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		final double directionZ = direction.getZ();
		
		final double aX = this.a.getX();
		final double aY = this.a.getY();
		final double aZ = this.a.getZ();
		final double bX = this.b.getX();
		final double bY = this.b.getY();
		final double bZ = this.b.getZ();
		
		final double aH = this.aH;
		final double cH = this.cH;
		final double phiMax = this.phiMax.getRadians();
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		
		final double a = aH * directionX * directionX + aH * directionY * directionY - cH * directionZ * directionZ;
		final double b = 2.0D * (aH * directionX * originX + aH * directionY * originY - cH * directionZ * originZ);
		final double c = aH * originX * originX + aH * originY * originY - cH * originZ * originZ - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(tClosest)) {
			return SurfaceIntersection3D.EMPTY;
		}
		
		final Point3D pointClosest = Point3D.add(origin, direction, tClosest);
		
		final double xClosest = pointClosest.getX();
		final double yClosest = pointClosest.getY();
		final double zClosest = pointClosest.getZ();
		final double vClosest = (zClosest - aZ) / (bZ - aZ);
		final double xClosestR = (1.0D - vClosest) * aX + vClosest * bX;
		final double yClosestR = (1.0D - vClosest) * aY + vClosest * bY;
		
		final double phiClosest = getOrAdd(atan2(yClosest * xClosestR - xClosest * yClosestR, xClosest * xClosestR + yClosest * yClosestR), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			final double tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
			
			if(isNaN(tClipped)) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			final Point3D pointClipped = Point3D.add(origin, direction, tClipped);
			
			final double xClipped = pointClipped.getX();
			final double yClipped = pointClipped.getY();
			final double zClipped = pointClipped.getZ();
			final double vClipped = (zClipped - aZ) / (bZ - aZ);
			final double xClippedR = (1.0D - vClipped) * aX + vClipped * bX;
			final double yClippedR = (1.0D - vClipped) * aY + vClipped * bY;
			
			final double phiClipped = getOrAdd(atan2(yClipped * xClippedR - xClipped * yClippedR, xClipped * xClippedR + yClipped * yClippedR), 0.0D, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return SurfaceIntersection3D.EMPTY;
			}
			
			return Optional.of(doCreateSurfaceIntersection(ray, phiClipped, tClipped, vClipped, xClipped, yClipped, zClipped));
		}
		
		return Optional.of(doCreateSurfaceIntersection(ray, phiClosest, tClosest, vClosest, xClosest, yClosest, zClosest));
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code A}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code A}
	 */
	public Point3D getA() {
		return this.a;
	}
	
	/**
	 * Returns the {@link Point3D} instance denoted by {@code B}.
	 * 
	 * @return the {@code Point3D} instance denoted by {@code B}
	 */
	public Point3D getB() {
		return this.b;
	}
	
	/**
	 * Returns a {@code String} with the name of this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code String} with the name of this {@code Hyperboloid3D} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Hyperboloid3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Hyperboloid3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Paraboloid3D(%s, %s, %s)", this.phiMax, this.a, this.b);
	}
	
	/**
	 * Compares {@code object} to this {@code Hyperboloid3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Hyperboloid3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Hyperboloid3D}, and their respective values are equal, {@code false} otherwise
	 */
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
	 * Returns the surface area of this {@code Hyperboloid3D} instance.
	 * 
	 * @return the surface area of this {@code Hyperboloid3D} instance
	 */
	@Override
	public double getSurfaceArea() {
		final double aX = this.a.getX();
		final double aXQuad = (aX * aX) * (aX * aX);
		final double aY = this.a.getY();
		final double aZ = this.a.getZ();
		final double bX = this.b.getX();
		final double bXQuad = (bX * bX) * (bX * bX);
		final double bY = this.b.getY();
		final double bZ = this.b.getZ();
		final double aYBYSquared = (aY - bY) * (aY - bY);
		final double aZBZSquared = (aZ - bZ) * (aZ - bZ);
		
		final double c = aY * aY + aY * bY + bY * bY;
		final double d = aYBYSquared + aZBZSquared;
		final double e = 5.0D * aY * aY + 2.0D * aY * bY - 4.0D * bY * bY + 2.0D * aZBZSquared;
		final double f = -4.0D * aY * aY + 2 * aY * bY + 5.0D * bY * bY + 2.0D * aZBZSquared;
		final double g = bX * bX - aY * aY + 5.0D * aY * bY - bY * bY - aZ * aZ + 2.0D * aZ * bZ - bZ * bZ;
		final double h = 2.0D * aXQuad - 2.0D * aX * aX * aX * bX + 2.0D * bXQuad + 2.0D * c * d + bX * bX * e + aX * aX * f - 2.0D * aX * bX * g;
		
		return this.phiMax.getRadians() / 6.0D * h;
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
	@Override
	public double intersectionT(final Ray3D ray, final double tMinimum, final double tMaximum) {
		final Point3D origin = ray.getOrigin();
		
		final Vector3D direction = ray.getDirection();
		
		final double originX = origin.getX();
		final double originY = origin.getY();
		final double originZ = origin.getZ();
		
		final double directionX = direction.getX();
		final double directionY = direction.getY();
		final double directionZ = direction.getZ();
		
		final double aX = this.a.getX();
		final double aY = this.a.getY();
		final double aZ = this.a.getZ();
		final double bX = this.b.getX();
		final double bY = this.b.getY();
		final double bZ = this.b.getZ();
		
		final double aH = this.aH;
		final double cH = this.cH;
		final double phiMax = this.phiMax.getRadians();
		final double zMax = this.zMax;
		final double zMin = this.zMin;
		
		final double a = aH * directionX * directionX + aH * directionY * directionY - cH * directionZ * directionZ;
		final double b = 2.0D * (aH * directionX * originX + aH * directionY * originY - cH * directionZ * originZ);
		final double c = aH * originX * originX + aH * originY * originY - cH * originZ * originZ - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		final double tClosest = !isNaN(t0) && t0 > tMinimum && t0 < tMaximum ? t0 : !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
		
		if(isNaN(tClosest)) {
			return Double.NaN;
		}
		
		final Point3D pointClosest = Point3D.add(origin, direction, tClosest);
		
		final double xClosest = pointClosest.getX();
		final double yClosest = pointClosest.getY();
		final double zClosest = pointClosest.getZ();
		final double vClosest = (zClosest - aZ) / (bZ - aZ);
		final double xClosestR = (1.0D - vClosest) * aX + vClosest * bX;
		final double yClosestR = (1.0D - vClosest) * aY + vClosest * bY;
		
		final double phiClosest = getOrAdd(atan2(yClosest * xClosestR - xClosest * yClosestR, xClosest * xClosestR + yClosest * yClosestR), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(zClosest < zMin || zClosest > zMax || phiClosest > phiMax) {
			if(equal(tClosest, t1)) {
				return Double.NaN;
			}
			
			final double tClipped = !isNaN(t1) && t1 > tMinimum && t1 < tMaximum ? t1 : Double.NaN;
			
			if(isNaN(tClipped)) {
				return Double.NaN;
			}
			
			final Point3D pointClipped = Point3D.add(origin, direction, tClipped);
			
			final double xClipped = pointClipped.getX();
			final double yClipped = pointClipped.getY();
			final double zClipped = pointClipped.getZ();
			final double vClipped = (zClipped - aZ) / (bZ - aZ);
			final double xClippedR = (1.0D - vClipped) * aX + vClipped * bX;
			final double yClippedR = (1.0D - vClipped) * aY + vClipped * bY;
			
			final double phiClipped = getOrAdd(atan2(yClipped * xClippedR - xClipped * yClippedR, xClipped * xClippedR + yClipped * yClippedR), 0.0D, PI_MULTIPLIED_BY_2);
			
			if(zClipped < zMin || zClipped > zMax || phiClipped > phiMax) {
				return Double.NaN;
			}
			
			return tClipped;
		}
		
		return tClosest;
	}
	
	/**
	 * Returns an {@code int} with the ID of this {@code Hyperboloid3D} instance.
	 * 
	 * @return an {@code int} with the ID of this {@code Hyperboloid3D} instance
	 */
	@Override
	public int getID() {
		return ID;
	}
	
	/**
	 * Returns a hash code for this {@code Hyperboloid3D} instance.
	 * 
	 * @return a hash code for this {@code Hyperboloid3D} instance
	 */
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
	
	private SurfaceIntersection3D doCreateSurfaceIntersection(final Ray3D ray, final double phi, final double t, final double v, final double x, final double y, final double z) {
		final double aX = this.a.getX();
		final double aY = this.a.getY();
		final double aZ = this.a.getZ();
		final double bX = this.b.getX();
		final double bY = this.b.getY();
		final double bZ = this.b.getZ();
		
		final double phiMax = this.phiMax.getRadians();
		
		final double u = phi / phiMax;
		
		final double cosPhi = cos(phi);
		final double sinPhi = sin(phi);
		
		final Vector3D dPDU = Vector3D.normalize(new Vector3D(-phiMax * y, phiMax * x, 0.0D));
		final Vector3D dPDV = Vector3D.normalize(new Vector3D((bX - aX) * cosPhi - (bY - aY) * sinPhi, (bX - aX) * sinPhi + (bY - aY) * cosPhi, bZ - aZ));
		
//		final Vector3D d2PDUU = new Vector3D(-phiMax * phiMax * x, -phiMax * phiMax * y, 0.0D);
//		final Vector3D d2PDUV = new Vector3D(phiMax * -dPDV.getY(), phiMax * dPDV.getX(), 0.0D);
//		final Vector3D d2PDVV = new Vector3D(0.0D, 0.0D, 0.0D);
		
//		final double e0 = Vector3D.dotProduct(dPDU, dPDU);
//		final double f0 = Vector3D.dotProduct(dPDU, dPDV);
//		final double g0 = Vector3D.dotProduct(dPDV, dPDV);
		
		final Vector3D surfaceNormalG = Vector3D.crossProduct(dPDU, dPDV);
		
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
		
		final Vector3D surfaceIntersectionPointError = new Vector3D();
		
		return new SurfaceIntersection3D(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t);
	}
}