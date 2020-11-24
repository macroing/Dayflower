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
package org.dayflower.geometry.shape;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.acos;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.lerp;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sin;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Floats.toFloat;
import static org.dayflower.util.Ints.saturate;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.util.ParameterArguments;

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
	
	/**
	 * Constructs a new {@code Curve3F} instance.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param data a {@link Data} instance
	 * @param uMinimum the minimum U-value
	 * @param uMaximum the maximum U-value
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
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
		
		final Matrix44F rayToObject = Matrix44F.lookAt(ray.getOrigin(), Point3F.add(ray.getOrigin(), ray.getDirection()), directionX);
		
		if(!rayToObject.isInvertible()) {
			return Optional.empty();
		}
		
		final Matrix44F objectToRay = Matrix44F.inverse(rayToObject);
		
		final Point3F pointI = Point3F.transform(objectToRay, pointE);
		final Point3F pointJ = Point3F.transform(objectToRay, pointF);
		final Point3F pointK = Point3F.transform(objectToRay, pointG);
		final Point3F pointL = Point3F.transform(objectToRay, pointH);
		
		final float xMaximum = 0.0F;
		final float yMaximum = 0.0F;
		final float zMaximum = ray.getDirection().length() * tMaximum;
		
		if(max(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) + widthE < tMinimum || min(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) - widthE > xMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		if(max(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) + widthE < tMinimum || min(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) - widthE > yMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		if(max(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) + widthE < tMinimum || min(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) - widthE > zMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float l01 = max(abs(pointI.getX() - 2.0F * pointJ.getX() + pointK.getX()), abs(pointI.getY() - 2.0F * pointJ.getY() + pointK.getY()), abs(pointI.getZ() - 2.0F * pointJ.getZ() + pointK.getZ()));
		final float l02 = max(abs(pointJ.getX() - 2.0F * pointK.getX() + pointL.getX()), abs(pointJ.getY() - 2.0F * pointK.getY() + pointL.getY()), abs(pointJ.getZ() - 2.0F * pointK.getZ() + pointL.getZ()));
		final float l03 = max(l01, l02);
		
		final int depth = saturate(doLog2(1.41421356237F * 6.0F * l03 / (8.0F * (max(widthA, widthB) * 0.05F))) / 2, 0, 10);
		
		return doIntersectionRecursive(ray, tMinimum, tMaximum, objectToRay, rayToObject, pointI, pointJ, pointK, pointL, uMinimum, uMaximum, depth);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Curve3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Curve3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Curve3F(%s, %+.10f, %+.10f)", this.data, Float.valueOf(this.uMinimum), Float.valueOf(this.uMaximum));
	}
	
	/**
	 * Compares {@code object} to this {@code Curve3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Curve3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Curve3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Curve3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Curve3F)) {
			return false;
		} else if(!Objects.equals(this.data, Curve3F.class.cast(object).data)) {
			return false;
		} else if(!equal(this.uMaximum, Curve3F.class.cast(object).uMaximum)) {
			return false;
		} else if(!equal(this.uMinimum, Curve3F.class.cast(object).uMinimum)) {
			return false;
		} else {
			return true;
		}
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
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
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
		
		final Matrix44F rayToObject = Matrix44F.lookAt(ray.getOrigin(), Point3F.add(ray.getOrigin(), ray.getDirection()), directionX);
		
		if(!rayToObject.isInvertible()) {
			return Float.NaN;
		}
		
		final Matrix44F objectToRay = Matrix44F.inverse(rayToObject);
		
		final Point3F pointI = Point3F.transform(objectToRay, pointE);
		final Point3F pointJ = Point3F.transform(objectToRay, pointF);
		final Point3F pointK = Point3F.transform(objectToRay, pointG);
		final Point3F pointL = Point3F.transform(objectToRay, pointH);
		
		final float xMaximum = 0.0F;
		final float yMaximum = 0.0F;
		final float zMaximum = ray.getDirection().length() * tMaximum;
		
		if(max(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) + widthE < tMinimum || min(pointI.getX(), pointJ.getX(), pointK.getX(), pointL.getX()) - widthE > xMaximum) {
			return Float.NaN;
		}
		
		if(max(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) + widthE < tMinimum || min(pointI.getY(), pointJ.getY(), pointK.getY(), pointL.getY()) - widthE > yMaximum) {
			return Float.NaN;
		}
		
		if(max(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) + widthE < tMinimum || min(pointI.getZ(), pointJ.getZ(), pointK.getZ(), pointL.getZ()) - widthE > zMaximum) {
			return Float.NaN;
		}
		
		final float l01 = max(abs(pointI.getX() - 2.0F * pointJ.getX() + pointK.getX()), abs(pointI.getY() - 2.0F * pointJ.getY() + pointK.getY()), abs(pointI.getZ() - 2.0F * pointJ.getZ() + pointK.getZ()));
		final float l02 = max(abs(pointJ.getX() - 2.0F * pointK.getX() + pointL.getX()), abs(pointJ.getY() - 2.0F * pointK.getY() + pointL.getY()), abs(pointJ.getZ() - 2.0F * pointK.getZ() + pointL.getZ()));
		final float l03 = max(l01, l02);
		
		final int depth = saturate(doLog2(1.41421356237F * 6.0F * l03 / (8.0F * (max(widthA, widthB) * 0.05F))) / 2, 0, 10);
		
		return doIntersectionTRecursive(ray, tMinimum, tMaximum, pointI, pointJ, pointK, pointL, uMinimum, uMaximum, depth);
	}
	
	/**
	 * Returns a {@code float[]} representation of this {@code Curve3F} instance.
	 * <p>
	 * Note: This method has not been implemented yet.
	 * 
	 * @return a {@code float[]} representation of this {@code Curve3F} instance
	 */
	@Override
	public float[] toArray() {
		return new float[0];//TODO: Implement!
	}
	
	/**
	 * Returns a hash code for this {@code Curve3F} instance.
	 * 
	 * @return a hash code for this {@code Curve3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.data, Float.valueOf(this.uMaximum), Float.valueOf(this.uMinimum));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code Curve3F} instances.
	 * <p>
	 * If either {@code pointA}, {@code pointB}, {@code pointC}, {@code pointD}, {@code type}, {@code normalA} or {@code normalB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointA a {@link Point3F} instance with the control point denoted by A
	 * @param pointB a {@code Point3F} instance with the control point denoted by B
	 * @param pointC a {@code Point3F} instance with the control point denoted by C
	 * @param pointD a {@code Point3F} instance with the control point denoted by D
	 * @param type a {@link Type} instance
	 * @param normalA a {@link Vector3F} instance with the normal denoted by A
	 * @param normalB a {@code Vector3F} instance with the normal denoted by B
	 * @param widthA the width denoted by A
	 * @param widthB the width denoted by B
	 * @param splitDepth the split depth
	 * @return a {@code List} of {@code Curve3F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code pointA}, {@code pointB}, {@code pointC}, {@code pointD}, {@code type}, {@code normalA} or {@code normalB} are {@code null}
	 */
	public static List<Curve3F> createCurves(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final Type type, final Vector3F normalA, final Vector3F normalB, final float widthA, final float widthB, final int splitDepth) {
		Objects.requireNonNull(pointA, "pointA == null");
		Objects.requireNonNull(pointB, "pointB == null");
		Objects.requireNonNull(pointC, "pointC == null");
		Objects.requireNonNull(pointD, "pointD == null");
		Objects.requireNonNull(type, "type == null");
		Objects.requireNonNull(normalA, "normalA == null");
		Objects.requireNonNull(normalB, "normalB == null");
		
		final Data data = new Data(pointA, pointB, pointC, pointD, type, normalA, normalB, widthA, widthB);
		
		final List<Curve3F> curves = new ArrayList<>();
		
		final int segments = 1 << splitDepth;
		
		for(int segment = 0; segment < segments; segment++) {
			final float uMinimum = toFloat(segment + 0) / toFloat(segments);
			final float uMaximum = toFloat(segment + 1) / toFloat(segments);
			
			curves.add(new Curve3F(data, uMinimum, uMaximum));
		}
		
		return curves;
	}
	
	/**
	 * Returns a {@code List} of {@code Curve3F} instances.
	 * <p>
	 * If either {@code points}, at least one element in {@code points}, {@code normals}, at least one element in {@code normals} or {@code type} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code degree} is less than {@code 2} or greater than {@code 3}, {@code points} contains the wrong number of {@link Point3F} instances or {@code normals} contains the wrong number of {@link Vector3F} instances, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Below follows the constraints:
	 * <ul>
	 * <li>For any given {@code degree}, there must exist at least {@code (degree + 1)} {@code Point3F} instances in {@code points}.</li>
	 * <li>For a {@link Type} of {@link Type#RIBBON}, there must exist exactly {@code (points.size() - degree + 1)} {@code Vector3F} instances in {@code normals} and {@code 0} otherwise.</li>
	 * </ul>
	 * 
	 * @param points a {@code List} of {@code Point3F} instances that represents the control points
	 * @param normals a {@code List} of {@code Vector3F} instances that represents the normals
	 * @param type a {@code Type} instance
	 * @param widthA the width denoted by A
	 * @param widthB the width denoted by B
	 * @param degree the degree
	 * @param splitDepth the split depth
	 * @return a {@code List} of {@code Curve3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code degree} is less than {@code 2} or greater than {@code 3}, {@code points} contains the wrong number of {@code Point3F} instances or {@code normals} contains the wrong number
	 *                                  of {@code Vector3F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code points}, at least one element in {@code points}, {@code normals}, at least one element in {@code normals} or {@code type} are {@code null}
	 */
	public static List<Curve3F> createCurvesByBSpline(final List<Point3F> points, final List<Vector3F> normals, final Type type, final float widthA, final float widthB, final int degree, final int splitDepth) {
		ParameterArguments.requireNonNullList(points, "points");
		ParameterArguments.requireNonNullList(normals, "normals");
		
		Objects.requireNonNull(type, "type == null");
		
		if(degree != 2 && degree != 3) {
			throw new IllegalArgumentException(String.format("Parameter argument degree must be 2 or 3: degree == %d", Integer.valueOf(degree)));
		}
		
		if(points.size() < degree + 1) {
			throw new IllegalArgumentException(String.format("Parameter argument points must contain at least %d Point3F instances: points.size() == %d", Integer.valueOf(degree + 1), Integer.valueOf(points.size())));
		}
		
		final int segments = points.size() - degree;
		
		switch(type) {
			case CYLINDER:
				ParameterArguments.requireExact(normals.size(), 0, "normals.size()");
				
				break;
			case FLAT:
				ParameterArguments.requireExact(normals.size(), 0, "normals.size()");
				
				break;
			case RIBBON:
				ParameterArguments.requireExact(normals.size(), segments + 1, "normals.size()");
				
				break;
			default:
				break;
		}
		
		final List<Curve3F> curves = new ArrayList<>();
		
		for(int segment = 0; segment < segments; segment++) {
			final int offset = segment;
			
			if(degree == 2) {
				final Point3F point00 = points.get(offset + 0);
				final Point3F point01 = points.get(offset + 1);
				final Point3F point02 = points.get(offset + 2);
				
				final Point3F point10 = Point3F.lerp(point00, point01, 0.5F);
				final Point3F point11 = Point3F.lerp(point01, point02, 0.5F);
				
				final Point3F pointA = point10;
				final Point3F pointB = Point3F.lerp(point10, point01, 2.0F / 3.0F);
				final Point3F pointC = Point3F.lerp(point01, point11, 1.0F / 3.0F);
				final Point3F pointD = point11;
				
				final Vector3F normalA = segment + 0 < normals.size() ? normals.get(segment + 0) : new Vector3F();
				final Vector3F normalB = segment + 1 < normals.size() ? normals.get(segment + 1) : new Vector3F();
				
				final float widthC = lerp(widthA, widthB, toFloat(segment + 0) / toFloat(segments));
				final float widthD = lerp(widthA, widthB, toFloat(segment + 1) / toFloat(segments));
				
				curves.addAll(createCurves(pointA, pointB, pointC, pointD, type, normalA, normalB, widthC, widthD, splitDepth));
			} else {
				final Point3F point00 = points.get(offset + 0);
				final Point3F point01 = points.get(offset + 1);
				final Point3F point02 = points.get(offset + 2);
				final Point3F point03 = points.get(offset + 3);
				
				final Point3F point10 = Point3F.lerp(point00, point01, 2.0F / 3.0F);
				final Point3F point11 = Point3F.lerp(point01, point02, 1.0F / 3.0F);
				final Point3F point12 = Point3F.lerp(point01, point02, 2.0F / 3.0F);
				final Point3F point13 = Point3F.lerp(point02, point03, 1.0F / 3.0F);
				
				final Point3F point20 = Point3F.lerp(point10, point11, 0.5F);
				final Point3F point21 = Point3F.lerp(point12, point13, 0.5F);
				
				final Point3F pointA = point20;
				final Point3F pointB = point11;
				final Point3F pointC = point12;
				final Point3F pointD = point21;
				
				final Vector3F normalA = segment + 0 < normals.size() ? normals.get(segment + 0) : new Vector3F();
				final Vector3F normalB = segment + 1 < normals.size() ? normals.get(segment + 1) : new Vector3F();
				
				final float widthC = lerp(widthA, widthB, toFloat(segment + 0) / toFloat(segments));
				final float widthD = lerp(widthA, widthB, toFloat(segment + 1) / toFloat(segments));
				
				curves.addAll(createCurves(pointA, pointB, pointC, pointD, type, normalA, normalB, widthC, widthD, splitDepth));
			}
		}
		
		return curves;
	}
	
	/**
	 * Returns a {@code List} of {@code Curve3F} instances.
	 * <p>
	 * If either {@code points}, at least one element in {@code points}, {@code normals}, at least one element in {@code normals} or {@code type} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code degree} is less than {@code 2} or greater than {@code 3}, {@code points} contains the wrong number of {@link Point3F} instances or {@code normals} contains the wrong number of {@link Vector3F} instances, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Below follows the constraints:
	 * <ul>
	 * <li>For any given {@code degree}, there must exist at least {@code ((degree + 1) + n * degree)} {@code Point3F} instances in {@code points}.</li>
	 * <li>For a {@link Type} of {@link Type#RIBBON}, there must exist exactly {@code ((points.size() - 1) / degree + 1)} {@code Vector3F} instances in {@code normals} and {@code 0} otherwise.</li>
	 * </ul>
	 * 
	 * @param points a {@code List} of {@code Point3F} instances that represents the control points
	 * @param normals a {@code List} of {@code Vector3F} instances that represents the normals
	 * @param type a {@code Type} instance
	 * @param widthA the width denoted by A
	 * @param widthB the width denoted by B
	 * @param degree the degree
	 * @param splitDepth the split depth
	 * @return a {@code List} of {@code Curve3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code degree} is less than {@code 2} or greater than {@code 3}, {@code points} contains the wrong number of {@code Point3F} instances or {@code normals} contains the wrong number
	 *                                  of {@code Vector3F} instances
	 * @throws NullPointerException thrown if, and only if, either {@code points}, at least one element in {@code points}, {@code normals}, at least one element in {@code normals} or {@code type} are {@code null}
	 */
	public static List<Curve3F> createCurvesByBezier(final List<Point3F> points, final List<Vector3F> normals, final Type type, final float widthA, final float widthB, final int degree, final int splitDepth) {
		ParameterArguments.requireNonNullList(points, "points");
		ParameterArguments.requireNonNullList(normals, "normals");
		
		Objects.requireNonNull(type, "type == null");
		
		if(degree != 2 && degree != 3) {
			throw new IllegalArgumentException(String.format("Parameter argument degree must be 2 or 3: degree == %d", Integer.valueOf(degree)));
		}
		
		if((points.size() - 1 - degree) % degree != 0) {
			throw new IllegalArgumentException(String.format("Parameter argument points must contain %d + n * %d Point3F instances: points.size() == %d", Integer.valueOf(degree + 1), Integer.valueOf(degree), Integer.valueOf(points.size())));
		}
		
		final int segments = (points.size() - 1) / degree;
		
		switch(type) {
			case CYLINDER:
				ParameterArguments.requireExact(normals.size(), 0, "normals.size()");
				
				break;
			case FLAT:
				ParameterArguments.requireExact(normals.size(), 0, "normals.size()");
				
				break;
			case RIBBON:
				ParameterArguments.requireExact(normals.size(), segments + 1, "normals.size()");
				
				break;
			default:
				break;
		}
		
		final List<Curve3F> curves = new ArrayList<>();
		
		for(int segment = 0; segment < segments; segment++) {
			final int offset = segment * degree;
			
			if(degree == 2) {
				final Point3F pointA = points.get(offset + 0);
				final Point3F pointB = Point3F.lerp(points.get(offset + 0), points.get(offset + 1), 2.0F / 3.0F);
				final Point3F pointC = Point3F.lerp(points.get(offset + 1), points.get(offset + 2), 1.0F / 3.0F);
				final Point3F pointD = points.get(offset + 2);
				
				final Vector3F normalA = segment + 0 < normals.size() ? normals.get(segment + 0) : new Vector3F();
				final Vector3F normalB = segment + 1 < normals.size() ? normals.get(segment + 1) : new Vector3F();
				
				final float widthC = lerp(widthA, widthB, toFloat(segment + 0) / toFloat(segments));
				final float widthD = lerp(widthA, widthB, toFloat(segment + 1) / toFloat(segments));
				
				curves.addAll(createCurves(pointA, pointB, pointC, pointD, type, normalA, normalB, widthC, widthD, splitDepth));
			} else {
				final Point3F pointA = points.get(offset + 0);
				final Point3F pointB = points.get(offset + 1);
				final Point3F pointC = points.get(offset + 2);
				final Point3F pointD = points.get(offset + 3);
				
				final Vector3F normalA = segment + 0 < normals.size() ? normals.get(segment + 0) : new Vector3F();
				final Vector3F normalB = segment + 1 < normals.size() ? normals.get(segment + 1) : new Vector3F();
				
				final float widthC = lerp(widthA, widthB, toFloat(segment + 0) / toFloat(segments));
				final float widthD = lerp(widthA, widthB, toFloat(segment + 1) / toFloat(segments));
				
				curves.addAll(createCurves(pointA, pointB, pointC, pointD, type, normalA, normalB, widthC, widthD, splitDepth));
			}
		}
		
		return curves;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code Data} stores common data for one or more {@link Curve3F} instances.
	 * <p>
	 * This class is immutable and therefore thread-safe.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
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
		
		/**
		 * Constructs a new {@code Data} instance.
		 * <p>
		 * If either {@code pointA}, {@code pointB}, {@code pointC}, {@code pointD}, {@code type}, {@code normalA} or {@code normalB} are {@code null}, a {@code NullPointerException} will be thrown.
		 * 
		 * @param pointA a {@link Point3F} instance with the control point denoted by A
		 * @param pointB a {@link Point3F} instance with the control point denoted by B
		 * @param pointC a {@link Point3F} instance with the control point denoted by C
		 * @param pointD a {@link Point3F} instance with the control point denoted by D
		 * @param type the {@link Type} instance associated with this {@code Data} instance
		 * @param normalA a {@link Vector3F} instance with the normal denoted by A
		 * @param normalB a {@code Vector3F} instance with the normal denoted by B
		 * @param widthA the width denoted by A
		 * @param widthB the width denoted by B
		 * @throws NullPointerException thrown if, and only if, either {@code pointA}, {@code pointB}, {@code pointC}, {@code pointD}, {@code type}, {@code normalA} or {@code normalB} are {@code null}
		 */
		public Data(final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final Type type, final Vector3F normalA, final Vector3F normalB, final float widthA, final float widthB) {
			this.pointA = Point3F.getCached(Objects.requireNonNull(pointA, "pointA == null"));
			this.pointB = Point3F.getCached(Objects.requireNonNull(pointB, "pointB == null"));
			this.pointC = Point3F.getCached(Objects.requireNonNull(pointC, "pointC == null"));
			this.pointD = Point3F.getCached(Objects.requireNonNull(pointD, "pointD == null"));
			this.type = Objects.requireNonNull(type, "type == null");
			this.normalA = Vector3F.getCached(Vector3F.normalize(Objects.requireNonNull(normalA, "normalA == null")));
			this.normalB = Vector3F.getCached(Vector3F.normalize(Objects.requireNonNull(normalB, "normalB == null")));
			this.normalAngle = acos(saturate(Vector3F.dotProduct(normalA, normalB)));
			this.normalAngleSinReciprocal = 1.0F / sin(this.normalAngle);
			this.widthA = widthA;
			this.widthB = widthB;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		/**
		 * Returns a {@link Point3F} instance with the control point denoted by A.
		 * 
		 * @return a {@code Point3F} instance with the control point denoted by A
		 */
		public Point3F getPointA() {
			return this.pointA;
		}
		
		/**
		 * Returns a {@link Point3F} instance with the control point denoted by B.
		 * 
		 * @return a {@code Point3F} instance with the control point denoted by B
		 */
		public Point3F getPointB() {
			return this.pointB;
		}
		
		/**
		 * Returns a {@link Point3F} instance with the control point denoted by C.
		 * 
		 * @return a {@code Point3F} instance with the control point denoted by C
		 */
		public Point3F getPointC() {
			return this.pointC;
		}
		
		/**
		 * Returns a {@link Point3F} instance with the control point denoted by D.
		 * 
		 * @return a {@code Point3F} instance with the control point denoted by D
		 */
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
		
		/**
		 * Returns the {@link Type} instance associated with this {@code Data} instance.
		 * 
		 * @return the {@code Type} instance associated with this {@code Data} instance
		 */
		public Type getType() {
			return this.type;
		}
		
		/**
		 * Returns a {@link Vector3F} instance with the normal denoted by A.
		 * 
		 * @return a {@code Vector3F} instance with the normal denoted by A
		 */
		public Vector3F getNormalA() {
			return this.normalA;
		}
		
		/**
		 * Returns a {@link Vector3F} instance with the normal denoted by B.
		 * 
		 * @return a {@code Vector3F} instance with the normal denoted by B
		 */
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
		
		/**
		 * Returns the angle of the normal.
		 * 
		 * @return the angle of the normal
		 */
		public float getNormalAngle() {
			return this.normalAngle;
		}
		
		/**
		 * Returns the reciprocal (or inverse) sine of the angle of the normal.
		 * 
		 * @return the reciprocal (or inverse) sine of the angle of the normal
		 */
		public float getNormalAngleSinReciprocal() {
			return this.normalAngleSinReciprocal;
		}
		
		/**
		 * Returns the width denoted by A.
		 * 
		 * @return the width denoted by A
		 */
		public float getWidthA() {
			return this.widthA;
		}
		
		/**
		 * Returns the width denoted by B.
		 * 
		 * @return the width denoted by B
		 */
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
	
	/**
	 * A {@code Type} contains type information about one or more {@link Curve3F} instances.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static enum Type {
		/**
		 * A {@code Type} instance used to represent a {@link Curve3F} instance that looks like a cylinder.
		 */
		CYLINDER,
		
		/**
		 * A {@code Type} instance used to represent a {@link Curve3F} instance that is flat.
		 */
		FLAT,
		
		/**
		 * A {@code Type} instance used to represent a {@link Curve3F} instance that looks like a ribbon.
		 */
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
	
	private Optional<SurfaceIntersection3F> doIntersectionRecursive(final Ray3F ray, final float tMinimum, final float tMaximum, final Matrix44F objectToRay, final Matrix44F rayToObject, final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final float uMinimum, final float uMaximum, final int depth) {
		final Data data = this.data;
		
		final float rayDirectionLength = ray.getDirection().length();
		
		final float widthA = data.getWidthA();
		final float widthB = data.getWidthB();
		
		if(depth > 0) {
			final Point3F[] points = doBezierSubdivide(pointA, pointB, pointC, pointD);
			
			final float uA = uMinimum;
			final float uB = (uMinimum + uMaximum) / 2.0F;
			final float uC = uMaximum;
			
			final float widthC = max(lerp(widthA, widthB, uA), lerp(widthA, widthB, uB)) * 0.5F;
			final float widthD = max(lerp(widthA, widthB, uB), lerp(widthA, widthB, uC)) * 0.5F;
			
			final float xMaximum = 0.0F;
			final float yMaximum = 0.0F;
			final float zMaximum = rayDirectionLength * tMaximum;
			
			final boolean isInsideAX = max(points[0].getX(), points[1].getX(), points[2].getX(), points[3].getX()) + widthC >= tMinimum && min(points[0].getX(), points[1].getX(), points[2].getX(), points[3].getX()) - widthC <= xMaximum;
			final boolean isInsideAY = max(points[0].getY(), points[1].getY(), points[2].getY(), points[3].getY()) + widthC >= tMinimum && min(points[0].getY(), points[1].getY(), points[2].getY(), points[3].getY()) - widthC <= yMaximum;
			final boolean isInsideAZ = max(points[0].getZ(), points[1].getZ(), points[2].getZ(), points[3].getZ()) + widthC >= tMinimum && min(points[0].getZ(), points[1].getZ(), points[2].getZ(), points[3].getZ()) - widthC <= zMaximum;
			final boolean isInsideBX = max(points[3].getX(), points[4].getX(), points[5].getX(), points[6].getX()) + widthD >= tMinimum && min(points[3].getX(), points[4].getX(), points[5].getX(), points[6].getX()) - widthD <= xMaximum;
			final boolean isInsideBY = max(points[3].getY(), points[4].getY(), points[5].getY(), points[6].getY()) + widthD >= tMinimum && min(points[3].getY(), points[4].getY(), points[5].getY(), points[6].getY()) - widthD <= yMaximum;
			final boolean isInsideBZ = max(points[3].getZ(), points[4].getZ(), points[5].getZ(), points[6].getZ()) + widthD >= tMinimum && min(points[3].getZ(), points[4].getZ(), points[5].getZ(), points[6].getZ()) - widthD <= zMaximum;
			
			if(isInsideAX && isInsideAY && isInsideAZ) {
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = doIntersectionRecursive(ray, tMinimum, tMaximum, objectToRay, rayToObject, points[0], points[1], points[2], points[3], uA, uB, depth - 1);
				
				if(optionalSurfaceIntersection.isPresent()) {
					return optionalSurfaceIntersection;
				}
			}
			
			if(isInsideBX && isInsideBY && isInsideBZ) {
				final Optional<SurfaceIntersection3F> optionalSurfaceIntersection = doIntersectionRecursive(ray, tMinimum, tMaximum, objectToRay, rayToObject, points[3], points[4], points[5], points[6], uB, uC, depth - 1);
				
				if(optionalSurfaceIntersection.isPresent()) {
					return optionalSurfaceIntersection;
				}
			}
			
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float edgeA = (pointB.getY() - pointA.getY()) * -pointA.getY() + pointA.getX() * (pointA.getX() - pointB.getX());
		final float edgeB = (pointC.getY() - pointD.getY()) * -pointD.getY() + pointD.getX() * (pointD.getX() - pointC.getX());
		
		if(edgeA < 0.0F || edgeB < 0.0F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final Vector2F segmentDirection = new Vector2F(pointD.getX() - pointA.getX(), pointD.getY() - pointA.getY());
		
		final float denominator = segmentDirection.lengthSquared();
		
		if(isZero(denominator)) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float w = Vector2F.dotProduct(Vector2F.negate(new Vector2F(pointA.getX(), pointA.getY())), segmentDirection) / denominator;
		final float u = saturate(lerp(uMinimum, uMaximum, w), uMinimum, uMaximum);
		final float hitWidth = doComputeHitWidth(data, ray, u);
		
		final Point3F point = doBezierEvaluate(pointA, pointB, pointC, pointD, saturate(w));
		
		final Vector3F derivative = doBezierEvaluateDerivative(pointA, pointB, pointC, pointD, saturate(w));
		
		final float pointCurveDistanceSquared = point.getX() * point.getX() + point.getY() * point.getY();
		
		if(pointCurveDistanceSquared > hitWidth * hitWidth * 0.25F) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float zMaximum = rayDirectionLength * tMaximum;
		
		if(point.getZ() < tMinimum || point.getZ() > zMaximum) {
			return SurfaceIntersection3F.EMPTY;
		}
		
		final float pointCurveDistance = sqrt(pointCurveDistanceSquared);
		final float edgeFunction = derivative.getX() * -point.getY() + point.getX() * derivative.getY();
		final float v = edgeFunction > 0.0F ? 0.5F + pointCurveDistance / hitWidth : 0.5F - pointCurveDistance / hitWidth;
		final float t = point.getZ() / rayDirectionLength;
		
		final OrthonormalBasis33F orthonormalBasisG = doComputeOrthonormalBasisG(data, objectToRay, rayToObject, hitWidth, u, v);
		final OrthonormalBasis33F orthonormalBasisS = orthonormalBasisG;
		
		final Point2F textureCoordinates = new Point2F(u, v);
		
		final Point3F surfaceIntersectionPoint = Point3F.add(ray.getOrigin(), ray.getDirection(), t);
		
		final Vector3F surfaceIntersectionPointError = new Vector3F(2.0F * hitWidth);
		
		return Optional.of(new SurfaceIntersection3F(orthonormalBasisG, orthonormalBasisS, textureCoordinates, surfaceIntersectionPoint, ray, this, surfaceIntersectionPointError, t));
	}
	
	private float doIntersectionTRecursive(final Ray3F ray, final float tMinimum, final float tMaximum, final Point3F pointA, final Point3F pointB, final Point3F pointC, final Point3F pointD, final float uMinimum, final float uMaximum, final int depth) {
		final Data data = this.data;
		
		final float rayDirectionLength = ray.getDirection().length();
		
		final float widthA = data.getWidthA();
		final float widthB = data.getWidthB();
		
		if(depth > 0) {
			final Point3F[] points = doBezierSubdivide(pointA, pointB, pointC, pointD);
			
			final float uA = uMinimum;
			final float uB = (uMinimum + uMaximum) / 2.0F;
			final float uC = uMaximum;
			
			final float widthC = max(lerp(widthA, widthB, uA), lerp(widthA, widthB, uB)) * 0.5F;
			final float widthD = max(lerp(widthA, widthB, uB), lerp(widthA, widthB, uC)) * 0.5F;
			
			final float xMaximum = 0.0F;
			final float yMaximum = 0.0F;
			final float zMaximum = rayDirectionLength * tMaximum;
			
			final boolean isInsideAX = max(points[0].getX(), points[1].getX(), points[2].getX(), points[3].getX()) + widthC >= tMinimum && min(points[0].getX(), points[1].getX(), points[2].getX(), points[3].getX()) - widthC <= xMaximum;
			final boolean isInsideAY = max(points[0].getY(), points[1].getY(), points[2].getY(), points[3].getY()) + widthC >= tMinimum && min(points[0].getY(), points[1].getY(), points[2].getY(), points[3].getY()) - widthC <= yMaximum;
			final boolean isInsideAZ = max(points[0].getZ(), points[1].getZ(), points[2].getZ(), points[3].getZ()) + widthC >= tMinimum && min(points[0].getZ(), points[1].getZ(), points[2].getZ(), points[3].getZ()) - widthC <= zMaximum;
			final boolean isInsideBX = max(points[3].getX(), points[4].getX(), points[5].getX(), points[6].getX()) + widthD >= tMinimum && min(points[3].getX(), points[4].getX(), points[5].getX(), points[6].getX()) - widthD <= xMaximum;
			final boolean isInsideBY = max(points[3].getY(), points[4].getY(), points[5].getY(), points[6].getY()) + widthD >= tMinimum && min(points[3].getY(), points[4].getY(), points[5].getY(), points[6].getY()) - widthD <= yMaximum;
			final boolean isInsideBZ = max(points[3].getZ(), points[4].getZ(), points[5].getZ(), points[6].getZ()) + widthD >= tMinimum && min(points[3].getZ(), points[4].getZ(), points[5].getZ(), points[6].getZ()) - widthD <= zMaximum;
			
			if(isInsideAX && isInsideAY && isInsideAZ) {
				final float t = doIntersectionTRecursive(ray, tMinimum, tMaximum, points[0], points[1], points[2], points[3], uA, uB, depth - 1);
				
				if(!isNaN(t)) {
					return t;
				}
			}
			
			if(isInsideBX && isInsideBY && isInsideBZ) {
				final float t = doIntersectionTRecursive(ray, tMinimum, tMaximum, points[3], points[4], points[5], points[6], uB, uC, depth - 1);
				
				if(!isNaN(t)) {
					return t;
				}
			}
			
			return Float.NaN;
		}
		
		final float edgeA = (pointB.getY() - pointA.getY()) * -pointA.getY() + pointA.getX() * (pointA.getX() - pointB.getX());
		final float edgeB = (pointC.getY() - pointD.getY()) * -pointD.getY() + pointD.getX() * (pointD.getX() - pointC.getX());
		
		if(edgeA < 0.0F || edgeB < 0.0F) {
			return Float.NaN;
		}
		
		final Vector2F segmentDirection = new Vector2F(pointD.getX() - pointA.getX(), pointD.getY() - pointA.getY());
		
		final float denominator = segmentDirection.lengthSquared();
		
		if(isZero(denominator)) {
			return Float.NaN;
		}
		
		final float w = Vector2F.dotProduct(Vector2F.negate(new Vector2F(pointA.getX(), pointA.getY())), segmentDirection) / denominator;
		final float u = saturate(lerp(uMinimum, uMaximum, w), uMinimum, uMaximum);
		final float hitWidth = doComputeHitWidth(data, ray, u);
		
		final Point3F point = doBezierEvaluate(pointA, pointB, pointC, pointD, saturate(w));
		
		final float pointCurveDistanceSquared = point.getX() * point.getX() + point.getY() * point.getY();
		
		if(pointCurveDistanceSquared > hitWidth * hitWidth * 0.25F) {
			return Float.NaN;
		}
		
		final float zMaximum = rayDirectionLength * tMaximum;
		
		if(point.getZ() < tMinimum || point.getZ() > zMaximum) {
			return Float.NaN;
		}
		
		final float t = point.getZ() / rayDirectionLength;
		
		return t;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static OrthonormalBasis33F doComputeOrthonormalBasisG(final Data data, final Matrix44F objectToRay, final Matrix44F rayToObject, final float hitWidth, final float u, final float v) {
		switch(data.getType()) {
			case CYLINDER: {
				final Vector3F directionU = doBezierEvaluateDerivative(data.getPointA(), data.getPointB(), data.getPointC(), data.getPointD(), u);
				final Vector3F directionUPlane = Vector3F.transform(objectToRay, directionU);
				final Vector3F directionVPlane = Vector3F.transform(Matrix44F.rotate(AngleF.degrees(-lerp(-90.0F, 90.0F, v), -90.0F, 90.0F), directionUPlane), Vector3F.multiply(Vector3F.normalize(new Vector3F(-directionUPlane.getY(), directionUPlane.getX(), 0.0F)), hitWidth));
				final Vector3F directionV = Vector3F.transform(rayToObject, directionVPlane);
				final Vector3F directionW = Vector3F.normalize(Vector3F.crossProduct(directionU, directionV));
				
				return new OrthonormalBasis33F(directionW, directionV, directionU);
			}
			case FLAT: {
				final Vector3F directionU = doBezierEvaluateDerivative(data.getPointA(), data.getPointB(), data.getPointC(), data.getPointD(), u);
				final Vector3F directionUPlane = Vector3F.transform(objectToRay, directionU);
				final Vector3F directionVPlane = Vector3F.multiply(Vector3F.normalize(new Vector3F(-directionUPlane.getY(), directionUPlane.getX(), 0.0F)), hitWidth);
				final Vector3F directionV = Vector3F.transform(rayToObject, directionVPlane);
				final Vector3F directionW = Vector3F.normalize(Vector3F.crossProduct(directionU, directionV));
				
				return new OrthonormalBasis33F(directionW, directionV, directionU);
			}
			case RIBBON: {
				final float sinA = sin((1.0F - u) * data.getNormalAngle()) * data.getNormalAngleSinReciprocal();
				final float sinB = sin(u * data.getNormalAngle()) * data.getNormalAngleSinReciprocal();
				
				final Vector3F normal = Vector3F.add(Vector3F.multiply(data.getNormalA(), sinA), Vector3F.multiply(data.getNormalB(), sinB));
				
				final Vector3F directionU = doBezierEvaluateDerivative(data.getPointA(), data.getPointB(), data.getPointC(), data.getPointD(), u);
				final Vector3F directionV = Vector3F.multiply(Vector3F.normalize(Vector3F.crossProduct(normal, directionU)), hitWidth);
				final Vector3F directionW = Vector3F.normalize(Vector3F.crossProduct(directionU, directionV));
				
				return new OrthonormalBasis33F(directionW, directionV, directionU);
			}
			default: {
				throw new IllegalArgumentException();
			}
		}
	}
	
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
		
		if(!isZero(directionX.lengthSquared())) {
			return directionX;
		} else if(abs(directionR.getX()) > abs(directionR.getY())) {
			return Vector3F.normalize(new Vector3F(-directionR.getZ(), 0.0F, directionR.getX()));
		} else {
			return Vector3F.normalize(new Vector3F(0.0F, directionR.getZ(), -directionR.getY()));
		}
	}
	
	private static float doComputeHitWidth(final Data data, final Ray3F ray, final float u) {
		if(data.getType() == Type.RIBBON) {
			final float sinA = sin((1.0F - u) * data.getNormalAngle()) * data.getNormalAngleSinReciprocal();
			final float sinB = sin(u * data.getNormalAngle()) * data.getNormalAngleSinReciprocal();
			
			final Vector3F normal = Vector3F.add(Vector3F.multiply(data.getNormalA(), sinA), Vector3F.multiply(data.getNormalB(), sinB));
			
			return lerp(data.getWidthA(), data.getWidthB(), u) * (abs(Vector3F.dotProduct(normal, ray.getDirection())) / ray.getDirection().length());
		}
		
		return lerp(data.getWidthA(), data.getWidthB(), u);
	}
	
	private static int doLog2(final float value) {
		if(value < 1.0F) {
			return 0;
		}
		
		final int bits = Float.floatToIntBits(value);
		
		return (bits >> 23) - 127 + ((bits & (1 << 22)) != 0 ? 1 : 0);
	}
}