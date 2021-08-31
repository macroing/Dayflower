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
package org.dayflower.simplex;

import static org.dayflower.simplex.Point.point2D;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DDistance;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.simplex.Point.point3DLerp;
import static org.dayflower.simplex.Point.point3DSet;
import static org.dayflower.simplex.Point.point3DTransformAndDivideMatrix44D;
import static org.dayflower.simplex.Shape.CONE_3_ID;
import static org.dayflower.simplex.Shape.CYLINDER_3_ID;
import static org.dayflower.simplex.Shape.DISK_3_ID;
import static org.dayflower.simplex.Shape.HYPERBOLOID_3_ID;
import static org.dayflower.simplex.Shape.PARABOLOID_3_ID;
import static org.dayflower.simplex.Shape.PLANE_3_ID;
import static org.dayflower.simplex.Shape.POLYGON_3_ID;
import static org.dayflower.simplex.Shape.RECTANGLE_3_ID;
import static org.dayflower.simplex.Shape.RECTANGULAR_CUBOID_3_ID;
import static org.dayflower.simplex.Shape.SPHERE_3_ID;
import static org.dayflower.simplex.Shape.TORUS_3_ID;
import static org.dayflower.simplex.Shape.TRIANGLE_3_ID;
import static org.dayflower.simplex.Shape.cone3DGetPhiMax;
import static org.dayflower.simplex.Shape.cone3DGetRadius;
import static org.dayflower.simplex.Shape.cone3DGetZMax;
import static org.dayflower.simplex.Shape.cylinder3DGetPhiMax;
import static org.dayflower.simplex.Shape.cylinder3DGetRadius;
import static org.dayflower.simplex.Shape.cylinder3DGetZMax;
import static org.dayflower.simplex.Shape.cylinder3DGetZMin;
import static org.dayflower.simplex.Shape.disk3DGetPhiMax;
import static org.dayflower.simplex.Shape.disk3DGetRadiusInner;
import static org.dayflower.simplex.Shape.disk3DGetRadiusOuter;
import static org.dayflower.simplex.Shape.disk3DGetZMax;
import static org.dayflower.simplex.Shape.hyperboloid3DGetA;
import static org.dayflower.simplex.Shape.hyperboloid3DGetAH;
import static org.dayflower.simplex.Shape.hyperboloid3DGetB;
import static org.dayflower.simplex.Shape.hyperboloid3DGetCH;
import static org.dayflower.simplex.Shape.hyperboloid3DGetPhiMax;
import static org.dayflower.simplex.Shape.hyperboloid3DGetZMax;
import static org.dayflower.simplex.Shape.hyperboloid3DGetZMin;
import static org.dayflower.simplex.Shape.paraboloid3DGetPhiMax;
import static org.dayflower.simplex.Shape.paraboloid3DGetRadius;
import static org.dayflower.simplex.Shape.paraboloid3DGetZMax;
import static org.dayflower.simplex.Shape.paraboloid3DGetZMin;
import static org.dayflower.simplex.Shape.plane3DGetA;
import static org.dayflower.simplex.Shape.plane3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.polygon2DContainsPoint2D;
import static org.dayflower.simplex.Shape.polygon2DFromPolygon3D;
import static org.dayflower.simplex.Shape.polygon3DGetPoint3D;
import static org.dayflower.simplex.Shape.polygon3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.rectangle3DGetA;
import static org.dayflower.simplex.Shape.rectangle3DGetB;
import static org.dayflower.simplex.Shape.rectangle3DGetC;
import static org.dayflower.simplex.Shape.rectangularCuboid3DGetMaximum;
import static org.dayflower.simplex.Shape.rectangularCuboid3DGetMinimum;
import static org.dayflower.simplex.Shape.shape3DGetID;
import static org.dayflower.simplex.Shape.sphere3DGetCenter;
import static org.dayflower.simplex.Shape.sphere3DGetRadius;
import static org.dayflower.simplex.Shape.torus3DGetRadiusInner;
import static org.dayflower.simplex.Shape.torus3DGetRadiusOuter;
import static org.dayflower.simplex.Shape.triangle3DGetPositionA;
import static org.dayflower.simplex.Shape.triangle3DGetPositionB;
import static org.dayflower.simplex.Shape.triangle3DGetPositionC;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DFromPoint3D;
import static org.dayflower.simplex.Vector.vector3DGetX;
import static org.dayflower.simplex.Vector.vector3DGetY;
import static org.dayflower.simplex.Vector.vector3DGetZ;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DNormalNormalized;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DReciprocal;
import static org.dayflower.simplex.Vector.vector3DSet;
import static org.dayflower.simplex.Vector.vector3DTransformMatrix44D;
import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.solveQuartic;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Ray {
//	TODO: Add Javadocs!
	public static final int RAY_OFFSET_DIRECTION = 3;
	
//	TODO: Add Javadocs!
	public static final int RAY_OFFSET_ORIGIN = 0;
	
//	TODO: Add Javadocs!
	public static final int RAY_OFFSET_T_MAXIMUM = 7;
	
//	TODO: Add Javadocs!
	public static final int RAY_OFFSET_T_MINIMUM = 6;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Ray() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Ray3D ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double ray3DGetTMaximum(final double[] ray3D) {
		return ray3DGetTMaximum(ray3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DGetTMaximum(final double[] ray3D, final int ray3DOffset) {
		return ray3D[ray3DOffset + RAY_OFFSET_T_MAXIMUM];
	}
	
//	TODO: Add Javadocs!
	public static double ray3DGetTMinimum(final double[] ray3D) {
		return ray3DGetTMinimum(ray3D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DGetTMinimum(final double[] ray3D, final int ray3DOffset) {
		return ray3D[ray3DOffset + RAY_OFFSET_T_MINIMUM];
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cone contained in {@code cone3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cone3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code cone3D.length < 5}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ray.ray3DIntersectionCone3D(ray3D, cone3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cone3D.length < 5}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cone3D} are {@code null}
	 */
	public static double ray3DIntersectionCone3D(final double[] ray3D, final double[] cone3D) {
		return ray3DIntersectionCone3D(ray3D, cone3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cone contained in {@code cone3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cone3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cone3D a {@code double[]} that contains a cone
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param cone3DOffset the offset in {@code cone3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cone3D.length < cone3DOffset + 5} or {@code cone3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cone3D} are {@code null}
	 */
	public static double ray3DIntersectionCone3D(final double[] ray3D, final double[] cone3D, final int ray3DOffset, final int cone3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double radius = cone3DGetRadius(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double k = (radius / zMax) * (radius / zMax);
		
		final double a = vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection) - k * vector3DGetZ(vector3DDirection) * vector3DGetZ(vector3DDirection);
		final double b = 2.0D * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin) - k * vector3DGetZ(vector3DDirection) * (point3DGetZ(point3DOrigin) - zMax));
		final double c = point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - k * (point3DGetZ(point3DOrigin) - zMax) * (point3DGetZ(point3DOrigin) - zMax);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= 0.0D && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cylinder contained in {@code cylinder3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cylinder3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code cylinder3D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ray.ray3DIntersectionCylinder3D(ray3D, cylinder3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cylinder3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cylinder3D} are {@code null}
	 */
	public static double ray3DIntersectionCylinder3D(final double[] ray3D, final double[] cylinder3D) {
		return ray3DIntersectionCylinder3D(ray3D, cylinder3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the cylinder contained in {@code cylinder3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code cylinder3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param cylinder3DOffset the offset in {@code cylinder3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code cylinder3D.length < cylinder3DOffset + 6} or {@code cylinder3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cylinder3D} are {@code null}
	 */
	public static double ray3DIntersectionCylinder3D(final double[] ray3D, final double[] cylinder3D, final int ray3DOffset, final int cylinder3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double radius = cylinder3DGetRadius(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double a = vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection);
		final double b = 2.0D * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin));
		final double c = point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double r = sqrt(point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D));
				final double phi = getOrAdd(atan2(point3DGetY(point3D) * (radius / r), point3DGetX(point3D) * (radius / r)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionDisk3D(final double[] ray3D, final double[] disk3D) {
		return ray3DIntersectionDisk3D(ray3D, disk3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionDisk3D(final double[] ray3D, final double[] disk3D, final int ray3DOffset, final int disk3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		if(isZero(vector3DGetZ(vector3DDirection))) {
			return NaN;
		}
		
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		final double zMax = disk3DGetZMax(disk3D, disk3DOffset);
		
		final double t = (zMax - point3DGetZ(point3DOrigin)) / vector3DGetZ(vector3DDirection);
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double distanceSquared = point3DGetX(point3D) * point3DGetX(point3D) + point3DGetY(point3D) * point3DGetY(point3D);
		
		if(distanceSquared > radiusOuter * radiusOuter || distanceSquared < radiusInner * radiusInner) {
			return NaN;
		}
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(phi > phiMax) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D) {
		return ray3DIntersectionHyperboloid3D(ray3D, hyperboloid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		final double aH = hyperboloid3DGetAH(hyperboloid3D, hyperboloid3DOffset);
		final double cH = hyperboloid3DGetCH(hyperboloid3D, hyperboloid3DOffset);
		final double zMax = hyperboloid3DGetZMax(hyperboloid3D, hyperboloid3DOffset);
		final double zMin = hyperboloid3DGetZMin(hyperboloid3D, hyperboloid3DOffset);
		
		final double a = aH * vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + aH * vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection) - cH * vector3DGetZ(vector3DDirection) * vector3DGetZ(vector3DDirection);
		final double b = 2.0D * (aH * vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + aH * vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin) - cH * vector3DGetZ(vector3DDirection) * point3DGetZ(point3DOrigin));
		final double c = aH * point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + aH * point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin) - cH * point3DGetZ(point3DOrigin) * point3DGetZ(point3DOrigin) - 1.0D;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				final double[] point3DTransformed = point3DLerp(point3DA, point3DB, (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA)));
				
				final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
				final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
				
				final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionParaboloid3D(final double[] ray3D, final double[] paraboloid3D) {
		return ray3DIntersectionParaboloid3D(ray3D, paraboloid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double radius = paraboloid3DGetRadius(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double k = zMax / (radius * radius);
		
		final double a = k * (vector3DGetX(vector3DDirection) * vector3DGetX(vector3DDirection) + vector3DGetY(vector3DDirection) * vector3DGetY(vector3DDirection));
		final double b = 2.0D * k * (vector3DGetX(vector3DDirection) * point3DGetX(point3DOrigin) + vector3DGetY(vector3DDirection) * point3DGetY(point3DOrigin)) - vector3DGetZ(vector3DDirection);
		final double c = k * (point3DGetX(point3DOrigin) * point3DGetX(point3DOrigin) + point3DGetY(point3DOrigin) * point3DGetY(point3DOrigin)) - point3DGetZ(point3DOrigin);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3DGetZ(point3D) >= zMin && point3DGetZ(point3D) <= zMax && phi <= phiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionPlane3D(final double[] ray3D, final double[] plane3D) {
		return ray3DIntersectionPlane3D(ray3D, plane3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionPlane3D(final double[] ray3D, final double[] plane3D, final int ray3DOffset, final int plane3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] vector3DSurfaceNormal = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t > tMinimum && t < tMaximum) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionPolygon3D(final double[] ray3D, final double[] polygon3D) {
		return ray3DIntersectionPolygon3D(ray3D, polygon3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionPolygon3D(final double[] ray3D, final double[] polygon3D, final int ray3DOffset, final int polygon3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] polygon2D = polygon2DFromPolygon3D(polygon3D, polygon3DOffset);
		
		final double[] point3DP = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DW = vector3DSurfaceNormal;
		final double[] vector3DU = vector3DDirectionNormalized(point3DA, point3DB);
		final double[] vector3DV = vector3DCrossProduct(vector3DW, vector3DU);
		
		final double[] vector3DDirectionAP = vector3DDirection(point3DA, point3DP);
		
		final double[] point2DP = point2D(vector3DDotProduct(vector3DDirectionAP, vector3DU), vector3DDotProduct(vector3DDirectionAP, vector3DV));
		
		final boolean contains = polygon2DContainsPoint2D(polygon2D, point2DP);
		
		if(contains) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionRectangle3D(final double[] ray3D, final double[] rectangle3D) {
		return ray3DIntersectionRectangle3D(ray3D, rectangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionRectangle3D(final double[] ray3D, final double[] rectangle3D, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = vector3DNormalNormalized(point3DA, point3DB, point3DC);
		
		final double surfaceNormalDotDirection = vector3DDotProduct(vector3DSurfaceNormal, vector3DDirection);
		
		if(isZero(surfaceNormalDotDirection)) {
			return NaN;
		}
		
		final double[] vector3DOriginToA = vector3DDirection(point3DOrigin, point3DA);
		
		final double t = vector3DDotProduct(vector3DOriginToA, vector3DSurfaceNormal) / surfaceNormalDotDirection;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] point3DP = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DA, point3DB);
		final double[] vector3DEdgeBC = vector3DDirection(point3DB, point3DC);
		final double[] vector3DEdgeAP = vector3DDirection(point3DA, point3DP);
		
		final double dotProductAPAB = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeAB));
		final double dotProductAPBC = vector3DDotProduct(vector3DEdgeAP, vector3DNormalize(vector3DEdgeBC));
		
		final boolean containsAPAB = dotProductAPAB >= 0.0D && dotProductAPAB <= vector3DLength(vector3DEdgeAB);
		final boolean containsAPBC = dotProductAPBC >= 0.0D && dotProductAPBC <= vector3DLength(vector3DEdgeBC);
		final boolean contains = containsAPAB && containsAPBC;
		
		if(contains) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D) {
		return ray3DIntersectionRectangularCuboid3D(ray3D, rectangularCuboid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		final double[] vector3DDirectionReciprocal = vector3DReciprocal(vector3DDirection);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] vector3DOriginToMaximum = vector3DDirection(point3DOrigin, point3DMaximum);
		final double[] vector3DOriginToMinimum = vector3DDirection(point3DOrigin, point3DMinimum);
		
		final double t0X = vector3DGetX(vector3DOriginToMinimum) * vector3DGetX(vector3DDirectionReciprocal);
		final double t0Y = vector3DGetY(vector3DOriginToMinimum) * vector3DGetY(vector3DDirectionReciprocal);
		final double t0Z = vector3DGetZ(vector3DOriginToMinimum) * vector3DGetZ(vector3DDirectionReciprocal);
		final double t1X = vector3DGetX(vector3DOriginToMaximum) * vector3DGetX(vector3DDirectionReciprocal);
		final double t1Y = vector3DGetY(vector3DOriginToMaximum) * vector3DGetY(vector3DDirectionReciprocal);
		final double t1Z = vector3DGetZ(vector3DOriginToMaximum) * vector3DGetZ(vector3DDirectionReciprocal);
		
		final double t0 = max(min(t0X, t1X), min(t0Y, t1Y), min(t0Z, t1Z));
		final double t1 = min(max(t0X, t1X), max(t0Y, t1Y), max(t0Z, t1Z));
		
		if(t0 > t1) {
			return NaN;
		}
		
		if(t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionShape3D(final double[] ray3D, final double[] shape3D) {
		return ray3DIntersectionShape3D(ray3D, shape3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionShape3D(final double[] ray3D, final double[] shape3D, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return ray3DIntersectionCone3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return ray3DIntersectionCylinder3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return ray3DIntersectionDisk3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return ray3DIntersectionHyperboloid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return ray3DIntersectionParaboloid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return ray3DIntersectionPlane3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return ray3DIntersectionPolygon3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return ray3DIntersectionRectangle3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return ray3DIntersectionRectangularCuboid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return ray3DIntersectionSphere3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return ray3DIntersectionTorus3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return ray3DIntersectionTriangle3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			default:
				return NaN;
		}
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionSphere3D(final double[] ray3D, final double[] sphere3D) {
		return ray3DIntersectionSphere3D(ray3D, sphere3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionSphere3D(final double[] ray3D, final double[] sphere3D, final int ray3DOffset, final int sphere3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		
		final double radius = sphere3DGetRadius(sphere3D, sphere3DOffset);
		
		final double[] vector3DCenterToOrigin = vector3DDirection(point3DCenter, point3DOrigin);
		
		final double a = vector3DLengthSquared(vector3DDirection);
		final double b = vector3DDotProduct(vector3DCenterToOrigin, vector3DDirection) * 2.0D;
		final double c = vector3DLengthSquared(vector3DCenterToOrigin) - radius * radius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		if(!isNaN(t0) && t0 > tMinimum && t0 < tMaximum) {
			return t0;
		}
		
		if(!isNaN(t1) && t1 > tMinimum && t1 < tMaximum) {
			return t1;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionTorus3D(final double[] ray3D, final double[] torus3D) {
		return ray3DIntersectionTorus3D(ray3D, torus3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionTorus3D(final double[] ray3D, final double[] torus3D, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		final double radiusOuter = torus3DGetRadiusOuter(torus3D, torus3DOffset);
		
		final double[] vector3DOrigin = vector3DFromPoint3D(point3DOrigin);
		
		final double f0 = vector3DLengthSquared(vector3DDirection);
		final double f1 = vector3DDotProduct(vector3DOrigin, vector3DDirection) * 2.0D;
		final double f2 = radiusInner * radiusInner;
		final double f3 = radiusOuter * radiusOuter;
		final double f4 = vector3DLengthSquared(vector3DOrigin) - f2 - f3;
		final double f5 = vector3DDirection[2];
		final double f6 = vector3DOrigin[2];
		
		final double a = f0 * f0;
		final double b = f0 * 2.0D * f1;
		final double c = f1 * f1 + 2.0D * f0 * f4 + 4.0D * f3 * f5 * f5;
		final double d = f1 * 2.0D * f4 + 8.0D * f3 * f6 * f5;
		final double e = f4 * f4 + 4.0D * f3 * f6 * f6 - 4.0D * f3 * f2;
		
		final double[] ts = solveQuartic(a, b, c, d, e);
		
		if(ts.length == 0) {
			return NaN;
		}
		
		if(ts[0] >= tMaximum || ts[ts.length - 1] <= tMinimum) {
			return NaN;
		}
		
		for(int i = 0; i < ts.length; i++) {
			if(ts[i] > tMinimum) {
				return ts[i];
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionTriangle3D(final double[] ray3D, final double[] triangle3D) {
		return ray3DIntersectionTriangle3D(ray3D, triangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DIntersectionTriangle3D(final double[] ray3D, final double[] triangle3D, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double tMinimum = ray3DGetTMinimum(ray3D, ray3DOffset);
		final double tMaximum = ray3DGetTMaximum(ray3D, ray3DOffset);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double t = vector3DDotProduct(vector3DEdgeABCrossEdgeCA, vector3DOriginToPositionA) / determinant;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double barycentricCoordinatesUScaled = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA);
		final double barycentricCoordinatesU = barycentricCoordinatesUScaled / determinant;
		
		if(barycentricCoordinatesU < 0.0D) {
			return NaN;
		}
		
		final double barycentricCoordinatesVScaled = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB);
		final double barycentricCoordinatesV = barycentricCoordinatesVScaled / determinant;
		
		if(barycentricCoordinatesV < 0.0D) {
			return NaN;
		}
		
		if((barycentricCoordinatesUScaled + barycentricCoordinatesVScaled) * determinant > determinant * determinant) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
	public static double ray3DTransformT(final double t, final double[] matrix44DLHS, final double[] ray3DRHS) {
		return ray3DTransformT(t, matrix44DLHS, ray3DRHS, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double ray3DTransformT(final double t, final double[] matrix44DLHS, final double[] ray3DRHS, final int matrix44DLHSOffset, final int ray3DRHSOffset) {
		final double[] point3DOriginOldSpace = ray3DGetOrigin(ray3DRHS, point3D(), ray3DRHSOffset, 0);
		final double[] point3DOriginNewSpace = point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DOriginOldSpace, point3D(), matrix44DLHSOffset, 0, 0);
		
		final double[] vector3DDirectionOldSpace = ray3DGetDirection(ray3DRHS, vector3D(), ray3DRHSOffset, 0);
		
		return !isNaN(t) && !isZero(t) && t < MAX_VALUE ? abs(point3DDistance(point3DOriginNewSpace, point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DAdd(point3DOriginOldSpace, vector3DDirectionOldSpace, t)))) : t;
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3D() {
		return new double[] {0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D};
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3D(final double[] point3DOrigin, final double[] vector3DDirection) {
		return ray3D(point3DOrigin, vector3DDirection, 0.0001D, MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3D(final double[] point3DOrigin, final double[] vector3DDirection, final double tMinimum, final double tMaximum) {
		return ray3D(point3DOrigin, vector3DDirection, tMinimum, tMaximum, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3D(final double[] point3DOrigin, final double[] vector3DDirection, final double tMinimum, final double tMaximum, final int point3DOriginOffset, final int vector3DDirectionOffset) {
		final double originX = point3DOrigin[point3DOriginOffset + 0];
		final double originY = point3DOrigin[point3DOriginOffset + 1];
		final double originZ = point3DOrigin[point3DOriginOffset + 2];
		
		final double directionX = vector3DDirection[vector3DDirectionOffset + 0];
		final double directionY = vector3DDirection[vector3DDirectionOffset + 1];
		final double directionZ = vector3DDirection[vector3DDirectionOffset + 2];
		
		return new double[] {originX, originY, originZ, directionX, directionY, directionZ, tMinimum, tMaximum};
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetDirection(final double[] ray3D) {
		return ray3DGetDirection(ray3D, vector3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetDirection(final double[] ray3D, final double[] vector3DDirectionResult) {
		return ray3DGetDirection(ray3D, vector3DDirectionResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetDirection(final double[] ray3D, final double[] vector3DDirectionResult, final int ray3DOffset, final int vector3DDirectionResultOffset) {
		final double component1 = ray3D[ray3DOffset + RAY_OFFSET_DIRECTION + 0];
		final double component2 = ray3D[ray3DOffset + RAY_OFFSET_DIRECTION + 1];
		final double component3 = ray3D[ray3DOffset + RAY_OFFSET_DIRECTION + 2];
		
		return vector3DSet(vector3DDirectionResult, component1, component2, component3, vector3DDirectionResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetOrigin(final double[] ray3D) {
		return ray3DGetOrigin(ray3D, point3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetOrigin(final double[] ray3D, final double[] point3DOriginResult) {
		return ray3DGetOrigin(ray3D, point3DOriginResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DGetOrigin(final double[] ray3D, final double[] point3DOriginResult, final int ray3DOffset, final int point3DOriginResultOffset) {
		final double component1 = ray3D[ray3DOffset + RAY_OFFSET_ORIGIN + 0];
		final double component2 = ray3D[ray3DOffset + RAY_OFFSET_ORIGIN + 1];
		final double component3 = ray3D[ray3DOffset + RAY_OFFSET_ORIGIN + 2];
		
		return point3DSet(point3DOriginResult, component1, component2, component3, point3DOriginResultOffset);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DSet(final double[] ray3DResult, final double[] point3DOrigin, final double[] vector3DDirection) {
		return ray3DSet(ray3DResult, point3DOrigin, vector3DDirection, 0.0001D, MAX_VALUE);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DSet(final double[] ray3DResult, final double[] point3DOrigin, final double[] vector3DDirection, final double tMinimum, final double tMaximum) {
		return ray3DSet(ray3DResult, point3DOrigin, vector3DDirection, tMinimum, tMaximum, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DSet(final double[] ray3DResult, final double[] point3DOrigin, final double[] vector3DDirection, final double tMinimum, final double tMaximum, final int ray3DResultOffset, final int point3DOriginOffset, final int vector3DDirectionOffset) {
		ray3DResult[ray3DResultOffset + RAY_OFFSET_ORIGIN + 0] = point3DOrigin[point3DOriginOffset + 0];
		ray3DResult[ray3DResultOffset + RAY_OFFSET_ORIGIN + 1] = point3DOrigin[point3DOriginOffset + 1];
		ray3DResult[ray3DResultOffset + RAY_OFFSET_ORIGIN + 2] = point3DOrigin[point3DOriginOffset + 2];
		
		ray3DResult[ray3DResultOffset + RAY_OFFSET_DIRECTION + 0] = vector3DDirection[vector3DDirectionOffset + 0];
		ray3DResult[ray3DResultOffset + RAY_OFFSET_DIRECTION + 1] = vector3DDirection[vector3DDirectionOffset + 1];
		ray3DResult[ray3DResultOffset + RAY_OFFSET_DIRECTION + 2] = vector3DDirection[vector3DDirectionOffset + 2];
		
		ray3DResult[ray3DResultOffset + RAY_OFFSET_T_MINIMUM] = tMinimum;
		ray3DResult[ray3DResultOffset + RAY_OFFSET_T_MAXIMUM] = tMaximum;
		
		return ray3DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DTransformMatrix44D(final double[] matrix44DLHS, final double[] ray3DRHS) {
		return ray3DTransformMatrix44D(matrix44DLHS, ray3DRHS, ray3D());
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DTransformMatrix44D(final double[] matrix44DLHS, final double[] ray3DRHS, final double[] ray3DResult) {
		return ray3DTransformMatrix44D(matrix44DLHS, ray3DRHS, ray3DResult, 0, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] ray3DTransformMatrix44D(final double[] matrix44DLHS, final double[] ray3DRHS, final double[] ray3DResult, final int matrix44DLHSOffset, final int ray3DRHSOffset, final int ray3DResultOffset) {
		final double[] point3DOriginOldSpace = ray3DGetOrigin(ray3DRHS, point3D(), ray3DRHSOffset, 0);
		final double[] point3DOriginNewSpace = point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DOriginOldSpace, point3D(), matrix44DLHSOffset, 0, 0);
		
		final double[] vector3DDirectionOldSpace = ray3DGetDirection(ray3DRHS, vector3D(), ray3DRHSOffset, 0);
		final double[] vector3DDirectionNewSpace = vector3DTransformMatrix44D(matrix44DLHS, vector3DDirectionOldSpace, vector3D(), matrix44DLHSOffset, 0, 0);
		
		final double tMaximumOldSpace = ray3DGetTMaximum(ray3DRHS, ray3DRHSOffset);
		final double tMaximumNewSpace = !isNaN(tMaximumOldSpace) && !isZero(tMaximumOldSpace) && tMaximumOldSpace < MAX_VALUE ? abs(point3DDistance(point3DOriginNewSpace, point3DTransformAndDivideMatrix44D(matrix44DLHS, point3DAdd(point3DOriginOldSpace, vector3DDirectionOldSpace, tMaximumOldSpace)))) : tMaximumOldSpace;
		
		final double tMinimumOldSpace = ray3DGetTMinimum(ray3DRHS, ray3DRHSOffset);
		final double tMinimumNewSpace = tMinimumOldSpace;
		
		return ray3DSet(ray3DResult, point3DOriginNewSpace, vector3DDirectionNewSpace, tMinimumNewSpace, tMaximumNewSpace, ray3DResultOffset, 0, 0);
	}
}