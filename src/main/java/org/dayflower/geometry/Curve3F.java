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
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.lerp;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Ints.saturate;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@code Curve3F} denotes a 3-dimensional curve that uses the data type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Curve3F implements Shape3F {
	private final Data data;
	private final float uMaximum;
	private final float uMinimum;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Curve3F(final Data data, final float uMinimum, final float uMaximum) {
		this.data = Objects.requireNonNull(data, "data == null");
		this.uMinimum = uMinimum;
		this.uMaximum = uMaximum;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link BoundingVolume3F} instance that contains this {@code Curve3F} instance.
	 * 
	 * @return a {@code BoundingVolume3F} instance that contains this {@code Curve3F} instance
	 */
	@Override
	public BoundingVolume3F getBoundingVolume() {
		final Data data = this.data;
		
		final float uMaximum = this.uMaximum;
		final float uMinimum = this.uMinimum;
		
		final float widthA = data.getWidthA();
		final float widthB = data.getWidthB();
		final float widthC = lerp(widthA, widthB, uMinimum);
		final float widthD = lerp(widthA, widthB, uMaximum);
		final float widthE = max(widthC, widthD) * 0.5F;
		
		final Point3F pointA = data.getPointA();
		final Point3F pointB = data.getPointB();
		final Point3F pointC = data.getPointC();
		final Point3F pointD = data.getPointD();
		
		final Point3F pointE = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMinimum);
		final Point3F pointF = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMaximum);
		final Point3F pointG = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMaximum, uMaximum);
		final Point3F pointH = doBezierBlossom(pointA, pointB, pointC, pointD, uMaximum, uMaximum, uMaximum);
		
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxA = new AxisAlignedBoundingBox3F(pointE, pointF);
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxB = new AxisAlignedBoundingBox3F(pointG, pointH);
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxC = AxisAlignedBoundingBox3F.union(axisAlignedBoundingBoxA, axisAlignedBoundingBoxB);
		final AxisAlignedBoundingBox3F axisAlignedBoundingBoxD = AxisAlignedBoundingBox3F.expand(axisAlignedBoundingBoxC, widthE);
		
		return axisAlignedBoundingBoxD;
	}
	
	/**
	 * Samples this {@code Curve3F} instance.
	 * <p>
	 * Returns an optional {@link SurfaceSample3F} with the surface sample.
	 * <p>
	 * If either {@code referencePoint} or {@code referenceSurfaceNormal} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curve3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curve3F} instance
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
	 * Performs an intersection test between {@code ray} and this {@code Curve3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray) {
		return intersection(ray, 0.0F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Curve3F} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link SurfaceIntersection3F} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code SurfaceIntersection3F} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public Optional<SurfaceIntersection3F> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Data data = this.data;
		
		final float uMaximum = this.uMaximum;
		final float uMinimum = this.uMinimum;
		
		final float widthA = data.getWidthA();
		final float widthB = data.getWidthB();
		final float widthC = lerp(widthA, widthB, uMinimum);
		final float widthD = lerp(widthA, widthB, uMaximum);
		final float widthE = max(widthC, widthD) * 0.5F;
		
		final Point3F pointA = data.getPointA();
		final Point3F pointB = data.getPointB();
		final Point3F pointC = data.getPointC();
		final Point3F pointD = data.getPointD();
		
		final Point3F pointE = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMinimum);
		final Point3F pointF = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMaximum);
		final Point3F pointG = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMaximum, uMaximum);
		final Point3F pointH = doBezierBlossom(pointA, pointB, pointC, pointD, uMaximum, uMaximum, uMaximum);
		
		final Vector3F directionX = doCreateDirectionX(ray, pointE, pointH);
		
		final Matrix44F objectToRay = Matrix44F.lookAt(ray.getOrigin(), Point3F.add(ray.getOrigin(), ray.getDirection()), directionX);
		
		final Point3F pointI = Point3F.transform(objectToRay, pointE);
		final Point3F pointJ = Point3F.transform(objectToRay, pointF);
		final Point3F pointK = Point3F.transform(objectToRay, pointG);
		final Point3F pointL = Point3F.transform(objectToRay, pointH);
		
		final float xMaximum = 0.0F;
		final float yMaximum = 0.0F;
		final float zMaximum = ray.getDirection().length() * tMaximum;
		
		if(max(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) + widthE < 0.0F || min(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) - widthE > xMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		if(max(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) + widthE < 0.0F || min(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) - widthE > yMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		if(max(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) + widthE < 0.0F || min(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) - widthE > zMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float l01 = max(abs(pointI.getX() - 2.0F * pointJ.getX() + pointK.getX()), abs(pointI.getY() - 2.0F * pointJ.getY() + pointK.getY()), abs(pointI.getZ() - 2.0F * pointJ.getZ() + pointK.getZ()));
		final float l02 = max(abs(pointJ.getX() - 2.0F * pointK.getX() + pointL.getX()), abs(pointJ.getY() - 2.0F * pointK.getY() + pointL.getY()), abs(pointJ.getZ() - 2.0F * pointK.getZ() + pointL.getZ()));
		final float l03 = max(l01, l02);
		
		final float epsilon = max(widthA, widthB) * 0.05F;
		
		final int r0 = doLog2(1.41421356237F * 6.0F * l03 / (8.0F * epsilon)) / 2;
		final int maximumDepth = saturate(r0, 0, 10);
		
//		return recursiveIntersect(ray, tHit, isect, cp, Inverse(objectToRay), uMin, uMax, maxDepth);
		
		return null;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code mutableSurfaceIntersection} and this {@code Curve3F} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code Curve3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code mutableSurfaceIntersection} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mutableSurfaceIntersection a {@link MutableSurfaceIntersection3F} instance
	 * @return {@code true} if, and only if, {@code mutableSurfaceIntersection} intersects this {@code Curve3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code mutableSurfaceIntersection} is {@code null}
	 */
	@Override
	public boolean intersection(final MutableSurfaceIntersection3F mutableSurfaceIntersection) {
		return mutableSurfaceIntersection.intersection(this);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Curve3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Curve3F} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public boolean intersects(final Ray3F ray) {
		return intersects(ray, 0.0F, Float.MAX_VALUE);
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects this {@code Curve3F} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects this {@code Curve3F} instance, {@code false} otherwise
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
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curve3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curve3F} instance
	 * @param point the point on this {@code Curve3F} instance
	 * @param surfaceNormal the surface normal on this {@code Curve3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal}, {@code point} or {@code surfaceNormal} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Point3F point, final Vector3F surfaceNormal) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(point, "point == null");
		Objects.requireNonNull(surfaceNormal, "surfaceNormal == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the probability density function (PDF) value for solid angle.
	 * <p>
	 * If either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @param referencePoint the reference point on this {@code Curve3F} instance
	 * @param referenceSurfaceNormal the reference surface normal on this {@code Curve3F} instance
	 * @param direction the direction to this {@code Curve3F} instance
	 * @return the probability density function (PDF) value for solid angle
	 * @throws NullPointerException thrown if, and only if, either {@code referencePoint}, {@code referenceSurfaceNormal} or {@code direction} are {@code null}
	 */
	@Override
	public float calculateProbabilityDensityFunctionValueForSolidAngle(final Point3F referencePoint, final Vector3F referenceSurfaceNormal, final Vector3F direction) {
		Objects.requireNonNull(referencePoint, "referencePoint == null");
		Objects.requireNonNull(referenceSurfaceNormal, "referenceSurfaceNormal == null");
		Objects.requireNonNull(direction, "direction == null");
		
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the surface area of this {@code Curve3F} instance.
	 * 
	 * @return the surface area of this {@code Curve3F} instance
	 */
	@Override
	public float getSurfaceArea() {
		final Data data = this.data;
		
		final float uMaximum = this.uMaximum;
		final float uMinimum = this.uMinimum;
		
		final float widthA = data.getWidthA();
		final float widthB = data.getWidthB();
		final float widthC = lerp(widthA, widthB, uMinimum);
		final float widthD = lerp(widthA, widthB, uMaximum);
		final float widthE = (widthC + widthD) * 0.5F;
		
		final Point3F pointA = data.getPointA();
		final Point3F pointB = data.getPointB();
		final Point3F pointC = data.getPointC();
		final Point3F pointD = data.getPointD();
		
		final Point3F pointE = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMinimum);
		final Point3F pointF = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMinimum, uMaximum);
		final Point3F pointG = doBezierBlossom(pointA, pointB, pointC, pointD, uMinimum, uMaximum, uMaximum);
		final Point3F pointH = doBezierBlossom(pointA, pointB, pointC, pointD, uMaximum, uMaximum, uMaximum);
		
		final float approximateLength = Point3F.distance(pointE, pointF) + Point3F.distance(pointF, pointG) + Point3F.distance(pointG, pointH);
		
		return approximateLength * widthE;
	}
	
	/**
	 * Returns the surface area probability density function (PDF) value of this {@code Curve3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the surface area probability density function (PDF) value of this {@code Curve3F} instance
	 */
	@Override
	public float getSurfaceAreaProbabilityDensityFunctionValue() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Returns the volume of this {@code Curve3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return the volume of this {@code Curve3F} instance
	 */
	@Override
	public float getVolume() {
		return 0.0F;//TODO: Implement!
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Curve3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray) {
		return intersectionT(ray, 0.0F, Float.MAX_VALUE);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Curve3F} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Curve3F} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return 0.0F;//TODO: Implement!
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static final class Data {
		private final Point3F pointA;
		private final Point3F pointB;
		private final Point3F pointC;
		private final Point3F pointD;
		private final Type type;
		private final Vector3F normalA;
		private final Vector3F normalB;
		private final float normalAngle;
		private final float normalAngleSinReciprocal;
		private final float widthA;
		private final float widthB;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
//		TODO: Add Javadocs!
		public Data(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final Type type, final Vector3F normalA, final Vector3F normalB, final float widthA, final float widthB) {
			this.pointA = Objects.requireNonNull(pointA, "pointA == null");
			this.pointB = Objects.requireNonNull(pointB, "pointB == null");
			this.pointC = Objects.requireNonNull(pointC, "pointC == null");
			this.pointD = Objects.requireNonNull(pointD, "pointD == null");
			this.type = Objects.requireNonNull(type, "type == null");
			this.normalA = Vector3F.normalize(Objects.requireNonNull(normalA, "normalA == null"));
			this.normalB = Vector3F.normalize(Objects.requireNonNull(normalB, "normalB == null"));
			this.normalAngle = acos(saturate(Vector3F.dotProduct(normalA, normalB)));
			this.normalAngleSinReciprocal = 1.0F / sin(this.normalAngle);
			this.widthA = widthA;
			this.widthB = widthB;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
//		TODO: Add Javadocs!
		public Point3F getPointA() {
			return this.pointA;
		}
		
//		TODO: Add Javadocs!
		public Point3F getPointB() {
			return this.pointB;
		}
		
//		TODO: Add Javadocs!
		public Point3F getPointC() {
			return this.pointC;
		}
		
//		TODO: Add Javadocs!
		public Point3F getPointD() {
			return this.pointD;
		}
		
		/**
		 * Returns a {@code String} representation of this {@code Data} instance.
		 * 
		 * @return a {@code String} representation of this {@code Data} instance
		 */
		@Override
		public String toString() {
			return String.format("new Data(%s, %s, %s, %s, %s, %s, %s, %+.10f, %+.10f)", this.pointA, this.pointB, this.pointC, this.pointD, this.type, this.normalA, this.normalB, Float.valueOf(this.widthA), Float.valueOf(this.widthB));
		}
		
//		TODO: Add Javadocs!
		public Type getType() {
			return this.type;
		}
		
//		TODO: Add Javadocs!
		public Vector3F getNormalA() {
			return this.normalA;
		}
		
//		TODO: Add Javadocs!
		public Vector3F getNormalB() {
			return this.normalB;
		}
		
		/**
		 * Compares {@code object} to this {@code Data} instance for equality.
		 * <p>
		 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Data}, and their respective values are equal, {@code false} otherwise.
		 * 
		 * @param object the {@code Object} to compare to this {@code Data} instance for equality
		 * @return {@code true} if, and only if, {@code object} is an instance of {@code Data}, and their respective values are equal, {@code false} otherwise
		 */
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Data)) {
				return false;
			} else if(!Objects.equals(this.pointA, Data.class.cast(object).pointA)) {
				return false;
			} else if(!Objects.equals(this.pointB, Data.class.cast(object).pointB)) {
				return false;
			} else if(!Objects.equals(this.pointC, Data.class.cast(object).pointC)) {
				return false;
			} else if(!Objects.equals(this.pointD, Data.class.cast(object).pointD)) {
				return false;
			} else if(!Objects.equals(this.type, Data.class.cast(object).type)) {
				return false;
			} else if(!Objects.equals(this.normalA, Data.class.cast(object).normalA)) {
				return false;
			} else if(!Objects.equals(this.normalB, Data.class.cast(object).normalB)) {
				return false;
			} else if(!equal(this.normalAngle, Data.class.cast(object).normalAngle)) {
				return false;
			} else if(!equal(this.normalAngleSinReciprocal, Data.class.cast(object).normalAngleSinReciprocal)) {
				return false;
			} else if(!equal(this.widthA, Data.class.cast(object).widthA)) {
				return false;
			} else if(!equal(this.widthB, Data.class.cast(object).widthB)) {
				return false;
			} else {
				return true;
			}
		}
		
//		TODO: Add Javadocs!
		public float getNormalAngle() {
			return this.normalAngle;
		}
		
//		TODO: Add Javadocs!
		public float getNormalAngleSinReciprocal() {
			return this.normalAngleSinReciprocal;
		}
		
//		TODO: Add Javadocs!
		public float getWidthA() {
			return this.widthA;
		}
		
//		TODO: Add Javadocs!
		public float getWidthB() {
			return this.widthB;
		}
		
		/**
		 * Returns a hash code for this {@code Data} instance.
		 * 
		 * @return a hash code for this {@code Data} instance
		 */
		@Override
		public int hashCode() {
			return Objects.hash(this.pointA, this.pointB, this.pointC, this.pointD, this.type, this.normalA, this.normalB, Float.valueOf(this.normalAngle), Float.valueOf(this.normalAngleSinReciprocal), Float.valueOf(this.widthA), Float.valueOf(this.widthB));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static enum Type {
//		TODO: Add Javadocs!
		CYLINDER,
		
//		TODO: Add Javadocs!
		FLAT,
		
//		TODO: Add Javadocs!
		RIBBON;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private Type() {
			
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns a {@code String} representation of this {@code Type} instance.
		 * 
		 * @return a {@code String} representation of this {@code Type} instance
		 */
		@Override
		public String toString() {
			switch(this) {
				case CYLINDER:
					return "Type.CYLINDER";
				case FLAT:
					return "Type.FLAT";
				case RIBBON:
					return "Type.RIBBON";
				default:
					return "";
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Point3F doBezierBlossom(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final float t1, final float t2, final float t3) {
		final Point3F pointAB = Point3F.lerp(pointA, pointB, t1);
		final Point3F pointBC = Point3F.lerp(pointB, pointC, t1);
		final Point3F pointCD = Point3F.lerp(pointC, pointD, t1);
		
		final Point3F pointABBC = Point3F.lerp(pointAB, pointBC, t2);
		final Point3F pointBCCD = Point3F.lerp(pointBC, pointCD, t2);
		
		return Point3F.lerp(pointABBC, pointBCCD, t3);
	}
	
	private static Point3F doBezierEvaluate(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final float t) {
		final Point3F pointAB = Point3F.lerp(pointA, pointB, t);
		final Point3F pointBC = Point3F.lerp(pointB, pointC, t);
		final Point3F pointCD = Point3F.lerp(pointC, pointD, t);
		
		final Point3F pointABBC = Point3F.lerp(pointAB, pointBC, t);
		final Point3F pointBCCD = Point3F.lerp(pointBC, pointCD, t);
		
		return Point3F.lerp(pointABBC, pointBCCD, t);
	}
	
	private static Point3F[] doBezierSubdivide(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD) {
		return new Point3F[] {
			pointA,
			Point3F.centroid(pointA, pointB),
			Point3F.centroid(pointA, pointB, pointB, pointC),
			Point3F.centroid(pointA, pointB, pointB, pointB, pointC, pointC, pointC, pointD),
			Point3F.centroid(pointB, pointC, pointC, pointD),
			Point3F.centroid(pointC, pointD),
			pointD
		};
	}
	
	private static Vector3F doBezierEvaluateDerivative(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final float t) {
		final Point3F pointAB = Point3F.lerp(pointA, pointB, t);
		final Point3F pointBC = Point3F.lerp(pointB, pointC, t);
		final Point3F pointCD = Point3F.lerp(pointC, pointD, t);
		
		final Point3F pointABBC = Point3F.lerp(pointAB, pointBC, t);
		final Point3F pointBCCD = Point3F.lerp(pointBC, pointCD, t);
		
		final Vector3F direction = Vector3F.direction(pointABBC, pointBCCD);
		
		if(direction.lengthSquared() > 0.0F) {
			return Vector3F.multiply(direction, 3.0F);
		}
		
		return Vector3F.direction(pointA, pointD);
	}
	
	private static Vector3F doCreateDirectionX(final Ray3F ray, final Point3F eye, final Point3F lookAt) {
		final Vector3F directionR = ray.getDirection();
		final Vector3F directionX = Vector3F.crossProduct(directionR, Vector3F.direction(eye, lookAt));
		
		if(!equal(directionX.lengthSquared(), 0.0F)) {
			return directionX;
		} else if(abs(directionR.getX()) > abs(directionR.getY())) {
			return Vector3F.normalize(new Vector3F(-directionR.getZ(), 0.0F, directionR.getX()));
		} else {
			return Vector3F.normalize(new Vector3F(0.0F, directionR.getZ(), -directionR.getY()));
		}
	}
	
	private static int doLog2(final float value) {
		if(value < 1.0F) {
			return 0;
		}
		
		final int bits = Float.floatToIntBits(value);
		
		return (bits >> 23) - 127 + ((bits & (1 << 22)) != 0 ? 1 : 0);
	}
}