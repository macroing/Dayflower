/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.isZero;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@code Plane3F} denotes a 3-dimensional plane that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Plane3F implements Shape3F {
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code A} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_A = 0;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code B} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_B = 3;
	
	/**
	 * The offset for the {@link Point3F} instance denoted by {@code C} in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_C = 6;
	
	/**
	 * The offset for the {@link Vector3F} instance representing the surface normal in the {@code float[]}.
	 */
	public static final int ARRAY_OFFSET_SURFACE_NORMAL = 9;
	
	/**
	 * The size of the {@code float[]}.
	 */
	public static final int ARRAY_SIZE = 12;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Point3F a;
	private final Point3F b;
	private final Point3F c;
	private final Vector3F surfaceNormal;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Plane3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 0.0F, 0.0F));
	 * }
	 * </pre>
	 */
	public Plane3F() {
		this(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F), new Point3F(1.0F, 0.0F, 0.0F));
	}
	
	/**
	 * Constructs a new {@code Plane3F} instance given the three {@link Point3F} instances {@code a}, {@code b} and {@code c}.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a the {@code Point3F} instance denoted by {@code A}
	 * @param b the {@code Point3F} instance denoted by {@code B}
	 * @param c the {@code Point3F} instance denoted by {@code C}
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public Plane3F(final Point3F a, final Point3F b, final Point3F c) {
		this.a = Point3F.getCached(Objects.requireNonNull(a, "a == null"));
		this.b = Point3F.getCached(Objects.requireNonNull(b, "b == null"));
		this.c = Point3F.getCached(Objects.requireNonNull(c, "c == null"));
		this.surfaceNormal = Vector3F.getCached(Vector3F.normalNormalized(a, b, c));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Plane3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Plane3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		return new InfiniteBoundingVolume3F();
	}
	
	/**
	 * Samples this {@code Plane3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Plane3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Plane3F} instance
	 * @param u a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @param v a random {@code float} with a uniform distribution between {@code 0.0F} and {@code 1.0F}
	 * @return an optional {@code SurfaceSample3F} with the surface sample
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}
	 */
	@Override
	public Optional<SurfaceSample3F> sample(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final float u, final float v) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		
		return SurfaceSample3F.EMPTY;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray) {
		return intersection(ray, 0.001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F a = this.a;
		final Point3F b = this.b;
		final Point3F c = this.c;
		
		final Vector3F originToA = Vector3F.direction(origin, a);
		
		final float t = Vector3F.dotProduct(originToA, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Point3F surfaceIntersectionPoint = Point3F.add(origin, direction, t);
		
		final OrthonormalBasis33F orthonormalBasisG = new OrthonormalBasis33F(surfaceNormal);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final float x = abs(surfaceNormal.getX());
		final float y = abs(surfaceNormal.getY());
		final float z = abs(surfaceNormal.getZ());
		
		final boolean isX = x > y && x > z;
		final boolean isY = y > z;
		
		final float aX = isX ? a.getY()      : isY ? a.getZ()      : a.getX();
		final float aY = isX ? a.getZ()      : isY ? a.getX()      : a.getY();
		final float bX = isX ? c.getY() - aX : isY ? c.getZ() - aX : c.getX() - aX;
		final float bY = isX ? c.getZ() - aY : isY ? c.getX() - aY : c.getY() - aY;
		final float cX = isX ? b.getY() - aX : isY ? b.getZ() - aX : b.getX() - aX;
		final float cY = isX ? b.getZ() - aY : isY ? b.getX() - aY : b.getY() - aY;
		
		final float determinant = bX * cY - bY * cX;
		final float determinantReciprocal = 1.0F / determinant;
		
		final float bNU = -bY * determinantReciprocal;
		final float bNV = +bX * determinantReciprocal;
		final float bND = (bY * aX - bX * aY) * determinantReciprocal;
		final float cNU = +cY * determinantReciprocal;
		final float cNV = -cX * determinantReciprocal;
		final float cND = (cX * aY - cY * aX) * determinantReciprocal;
		
		final float hU = isX ? surfaceIntersectionPoint.getY() : isY ? surfaceIntersectionPoint.getZ() : surfaceIntersectionPoint.getX();
		final float hV = isX ? surfaceIntersectionPoint.getZ() : isY ? surfaceIntersectionPoint.getX() : surfaceIntersectionPoint.getY();
		
		final float u = hU * bNU + hV * bNV + bND;
		final float v = hU * cNU + hV * cNV + cND;
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
//		TODO: Implement!
		final Vector3F surfaceIntersectionPointError = new Vector3F();
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
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
	 * Returns the {@link Point3F} instance denoted by {@code C}.
	 * 
	 * @return the {@code Point3F} instance denoted by {@code C}
	 */
	public Point3F getC() {
		return this.c;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Plane3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Plane3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Plane3F(%s, %s, %s)", this.a, this.b, this.c);
	}
	
	/**
	 * Returns the {@link Vector3F} instance used as surface normal.
	 * 
	 * @return the {@code Vector3F} instance used as surface normal
	 */
	public Vector3F getSurfaceNormal() {
		return this.surfaceNormal;
	}
	
	/**
	 * Compares {@code object} to this {@code Plane3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Plane3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Plane3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Plane3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Plane3F)) {
			return false;
		} else if(!Objects.equals(this.a, Plane3F.class.cast(object).a)) {
			return false;
		} else if(!Objects.equals(this.b, Plane3F.class.cast(object).b)) {
			return false;
		} else if(!Objects.equals(this.c, Plane3F.class.cast(object).c)) {
			return false;
		} else if(!Objects.equals(this.surfaceNormal, Plane3F.class.cast(object).surfaceNormal)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Performs an intersection test between {@code mutableSurfaceIntersection} and this {@code Plane3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code Plane3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code mutableSurfaceIntersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mutableSurfaceIntersection a {@link MutableSurfaceIntersection3F} instance
	 * @return {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code Plane3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code mutableSurfaceIntersection} is {@code null}
	 */
	@Override
	public boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection) {
		return mutableSurfaceIntersection.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Plane3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Plane3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray) {
		return intersects(ray, 0.001F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Plane3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Plane3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return !isNaN(intersectionT(ray, tMinimum, tMaximum));
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @param referencePoint the reference point on this {@code Plane3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Plane3F} instance
	 * @param point the point on this {@code Plane3F} instance
	 * @param surfaceNormal the surface normal on this {@code Plane3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @param referencePoint the reference point on this {@code Plane3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Plane3F} instance
	 * @param direction the direction to this {@code Plane3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0F;
	}
	
	/**
	 * Returns the surface area of this {@code Plane3F} instance.
	 * <p>
	 * This method returns {@code Float.POSITIVE_INFINITY}.
	 * 
	 * @return the surface area of this {@code Plane3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		return Float.POSITIVE_INFINITY;
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Plane3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Plane3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;
	}
	
	/**
	 * Returns the volume of this {@code Plane3F} instance.
	 * <p>
	 * This method returns {@code 0.0F}.
	 * 
	 * @return the volume of this {@code Plane3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray) {
		return intersectionT(ray, 0.001F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Plane3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Plane3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Vector3F direction = ray.getDirection();
		final Vector3F surfaceNormal = this.surfaceNormal;
		
		final float nDotD = Vector3F.dotProduct(surfaceNormal, direction);
		
		if(isZero(nDotD)) {
			return Float.NaN;
		}
		
		final Point3F origin = ray.getOrigin();
		final Point3F a = this.a;
		
		final Vector3F originToA = Vector3F.direction(origin, a);
		
		final float t = Vector3F.dotProduct(originToA, surfaceNormal) / nDotD;
		
		if(t <= tMinimum || t >= tMaximum) {
			return Float.NaN;
		}
		
		return t;
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Plane3F} instance.
	 * 
	 * @return a {@code float[]} representation of this {@code Plane3F} instance
	 */
	@Override
	public float[] toArray() {
		final float[] array = new float[ARRAY_SIZE];
		
		array[ARRAY_OFFSET_A + 0] = this.a.getX();
		array[ARRAY_OFFSET_A + 1] = this.a.getY();
		array[ARRAY_OFFSET_A + 2] = this.a.getZ();
		array[ARRAY_OFFSET_B + 0] = this.b.getX();
		array[ARRAY_OFFSET_B + 1] = this.b.getY();
		array[ARRAY_OFFSET_B + 2] = this.b.getZ();
		array[ARRAY_OFFSET_C + 0] = this.c.getX();
		array[ARRAY_OFFSET_C + 1] = this.c.getY();
		array[ARRAY_OFFSET_C + 2] = this.c.getZ();
		array[ARRAY_OFFSET_SURFACE_NORMAL + 0] = this.surfaceNormal.getX();
		array[ARRAY_OFFSET_SURFACE_NORMAL + 1] = this.surfaceNormal.getY();
		array[ARRAY_OFFSET_SURFACE_NORMAL + 2] = this.surfaceNormal.getZ();
		
		return array;
	}
	
	/**
	 * Returns a hash code for this {@code Plane3F} instance.
	 * 
	 * @return a hash code for this {@code Plane3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.a, this.b, this.c, this.surfaceNormal);
	}
}