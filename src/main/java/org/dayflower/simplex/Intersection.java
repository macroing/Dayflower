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

import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33D;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DFromW;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DFromWV;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DGetW;
import static org.dayflower.simplex.Point.point2D;
import static org.dayflower.simplex.Point.point2DFromBarycentricCoordinates;
import static org.dayflower.simplex.Point.point2DSphericalCoordinates;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DGetX;
import static org.dayflower.simplex.Point.point3DGetY;
import static org.dayflower.simplex.Point.point3DGetZ;
import static org.dayflower.simplex.Point.point3DLerp;
import static org.dayflower.simplex.Point.point3DMidpoint;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DGetOrigin;
import static org.dayflower.simplex.Ray.ray3DGetTMaximum;
import static org.dayflower.simplex.Ray.ray3DGetTMinimum;
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
import static org.dayflower.simplex.Shape.plane3DGetB;
import static org.dayflower.simplex.Shape.plane3DGetC;
import static org.dayflower.simplex.Shape.plane3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.polygon2DContainsPoint2D;
import static org.dayflower.simplex.Shape.polygon2DFromPolygon3D;
import static org.dayflower.simplex.Shape.polygon3DGetPoint3D;
import static org.dayflower.simplex.Shape.polygon3DGetPoint3DCount;
import static org.dayflower.simplex.Shape.polygon3DGetSurfaceNormal;
import static org.dayflower.simplex.Shape.rectangle3DGetA;
import static org.dayflower.simplex.Shape.rectangle3DGetB;
import static org.dayflower.simplex.Shape.rectangle3DGetC;
import static org.dayflower.simplex.Shape.rectangle3DGetD;
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
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalA;
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalB;
import static org.dayflower.simplex.Shape.triangle3DGetSurfaceNormalC;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesA;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesB;
import static org.dayflower.simplex.Shape.triangle3DGetTextureCoordinatesC;
import static org.dayflower.simplex.Vector.vector2DCrossProduct;
import static org.dayflower.simplex.Vector.vector2DDirection;
import static org.dayflower.simplex.Vector.vector2DDirectionXY;
import static org.dayflower.simplex.Vector.vector2DDirectionYZ;
import static org.dayflower.simplex.Vector.vector2DDirectionZX;
import static org.dayflower.simplex.Vector.vector2DGetX;
import static org.dayflower.simplex.Vector.vector2DGetY;
import static org.dayflower.simplex.Vector.vector2DSubtract;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DFromBarycentricCoordinatesNormalized;
import static org.dayflower.simplex.Vector.vector3DFromPoint3D;
import static org.dayflower.simplex.Vector.vector3DGetX;
import static org.dayflower.simplex.Vector.vector3DGetY;
import static org.dayflower.simplex.Vector.vector3DGetZ;
import static org.dayflower.simplex.Vector.vector3DLength;
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.simplex.Vector.vector3DNormalNormalized;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.simplex.Vector.vector3DReciprocal;
import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.PI_DIVIDED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Doubles.PI_RECIPROCAL;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.asin;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.cos;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.normalize;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Doubles.sin;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.solveQuartic;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Intersection {
	private Intersection() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * Intersection.intersectionCone3D(ray3D, cone3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cone3D a {@code double[]} that contains a cone
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cone3D.length < 5}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cone3D} are {@code null}
	 */
	public static double intersectionCone3D(final double[] ray3D, final double[] cone3D) {
		return intersectionCone3D(ray3D, cone3D, 0, 0);
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
	public static double intersectionCone3D(final double[] ray3D, final double[] cone3D, final int ray3DOffset, final int cone3DOffset) {
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
	 * Intersection.intersectionCylinder3D(ray3D, cylinder3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param cylinder3D a {@code double[]} that contains a cylinder
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code cylinder3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code cylinder3D} are {@code null}
	 */
	public static double intersectionCylinder3D(final double[] ray3D, final double[] cylinder3D) {
		return intersectionCylinder3D(ray3D, cylinder3D, 0, 0);
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
	public static double intersectionCylinder3D(final double[] ray3D, final double[] cylinder3D, final int ray3DOffset, final int cylinder3DOffset) {
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
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the disk contained in {@code disk3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code disk3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code disk3D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Intersection.intersectionDisk3D(ray3D, disk3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param disk3D a {@code double[]} that contains a disk
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code disk3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code disk3D} are {@code null}
	 */
	public static double intersectionDisk3D(final double[] ray3D, final double[] disk3D) {
		return intersectionDisk3D(ray3D, disk3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the disk contained in {@code disk3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code disk3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code disk3D.length < disk3DOffset + 6} or {@code disk3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param disk3D a {@code double[]} that contains a disk
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param disk3DOffset the offset in {@code disk3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code disk3D.length < disk3DOffset + 6} or {@code disk3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code disk3D} are {@code null}
	 */
	public static double intersectionDisk3D(final double[] ray3D, final double[] disk3D, final int ray3DOffset, final int disk3DOffset) {
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
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the hyperboloid contained in {@code hyperboloid3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code hyperboloid3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code hyperboloid3D.length < 14}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Intersection.intersectionHyperboloid3D(ray3D, hyperboloid3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param hyperboloid3D a {@code double[]} that contains a hyperboloid
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code hyperboloid3D.length < 14}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code hyperboloid3D} are {@code null}
	 */
	public static double intersectionHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D) {
		return intersectionHyperboloid3D(ray3D, hyperboloid3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the hyperboloid contained in {@code hyperboloid3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code hyperboloid3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code hyperboloid3D.length < hyperboloid3DOffset + 14} or {@code hyperboloid3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param hyperboloid3D a {@code double[]} that contains a hyperboloid
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param hyperboloid3DOffset the offset in {@code hyperboloid3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code hyperboloid3D.length < hyperboloid3DOffset + 14} or {@code hyperboloid3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code hyperboloid3D} are {@code null}
	 */
	public static double intersectionHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final int ray3DOffset, final int hyperboloid3DOffset) {
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
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the paraboloid contained in {@code paraboloid3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code paraboloid3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code paraboloid3D.length < 6}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Intersection.intersectionParaboloid3D(ray3D, paraboloid3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param paraboloid3D a {@code double[]} that contains a paraboloid
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code paraboloid3D.length < 6}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code paraboloid3D} are {@code null}
	 */
	public static double intersectionParaboloid3D(final double[] ray3D, final double[] paraboloid3D) {
		return intersectionParaboloid3D(ray3D, paraboloid3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the paraboloid contained in {@code paraboloid3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code paraboloid3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code paraboloid3D.length < paraboloid3DOffset + 6} or {@code paraboloid3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param paraboloid3D a {@code double[]} that contains a paraboloid
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param paraboloid3DOffset the offset in {@code paraboloid3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code paraboloid3D.length < paraboloid3DOffset + 6} or {@code paraboloid3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code paraboloid3D} are {@code null}
	 */
	public static double intersectionParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final int ray3DOffset, final int paraboloid3DOffset) {
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
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the plane contained in {@code plane3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code plane3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code plane3D.length < 11}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Intersection.intersectionPlane3D(ray3D, plane3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param plane3D a {@code double[]} that contains a plane
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code plane3D.length < 11}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code plane3D} are {@code null}
	 */
	public static double intersectionPlane3D(final double[] ray3D, final double[] plane3D) {
		return intersectionPlane3D(ray3D, plane3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the plane contained in {@code plane3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code plane3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code plane3D.length < plane3DOffset + 11} or {@code plane3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param plane3D a {@code double[]} that contains a plane
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param plane3DOffset the offset in {@code plane3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code plane3D.length < plane3DOffset + 11} or {@code plane3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code plane3D} are {@code null}
	 */
	public static double intersectionPlane3D(final double[] ray3D, final double[] plane3D, final int ray3DOffset, final int plane3DOffset) {
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
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the polygon contained in {@code polygon3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code polygon3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < 8} or {@code polygon3D.length < 13}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Intersection.intersectionPolygon3D(ray3D, polygon3D, 0, 0);
	 * }
	 * </pre>
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param polygon3D a {@code double[]} that contains a polygon
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < 8} or {@code polygon3D.length < 13}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code polygon3D} are {@code null}
	 */
	public static double intersectionPolygon3D(final double[] ray3D, final double[] polygon3D) {
		return intersectionPolygon3D(ray3D, polygon3D, 0, 0);
	}
	
	/**
	 * Performs an intersection test between the ray contained in {@code ray3D} and the polygon contained in {@code polygon3D}.
	 * <p>
	 * Returns the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection.
	 * <p>
	 * If either {@code ray3D} or {@code polygon3D} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code polygon3D.length < polygon3DOffset + 13} or {@code polygon3DOffset < 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param ray3D a {@code double[]} that contains a ray
	 * @param polygon3D a {@code double[]} that contains a polygon
	 * @param ray3DOffset the offset in {@code ray3D} to start at
	 * @param polygon3DOffset the offset in {@code polygon3D} to start at
	 * @return the parametric distance to the point of intersection, or {@code Doubles.NaN} if there is no intersection
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code ray3D.length < ray3DOffset + 8}, {@code ray3DOffset < 0}, {@code polygon3D.length < polygon3DOffset + 13} or {@code polygon3DOffset < 0}
	 * @throws NullPointerException thrown if, and only if, either {@code ray3D} or {@code polygon3D} are {@code null}
	 */
	public static double intersectionPolygon3D(final double[] ray3D, final double[] polygon3D, final int ray3DOffset, final int polygon3DOffset) {
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
	public static double intersectionRectangle3D(final double[] ray3D, final double[] rectangle3D) {
		return intersectionRectangle3D(ray3D, rectangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRectangle3D(final double[] ray3D, final double[] rectangle3D, final int ray3DOffset, final int rectangle3DOffset) {
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
	public static double intersectionRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D) {
		return intersectionRectangularCuboid3D(ray3D, rectangularCuboid3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final int ray3DOffset, final int rectangularCuboid3DOffset) {
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
	public static double intersectionShape3D(final double[] ray3D, final double[] shape3D) {
		return intersectionShape3D(ray3D, shape3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionShape3D(final double[] ray3D, final double[] shape3D, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return intersectionCone3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return intersectionCylinder3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return intersectionDisk3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return intersectionHyperboloid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return intersectionParaboloid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return intersectionPlane3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return intersectionPolygon3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return intersectionRectangle3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return intersectionRectangularCuboid3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return intersectionSphere3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return intersectionTorus3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return intersectionTriangle3D(ray3D, shape3D, ray3DOffset, shape3DOffset);
			default:
				return NaN;
		}
	}
	
//	TODO: Add Javadocs!
	public static double intersectionSphere3D(final double[] ray3D, final double[] sphere3D) {
		return intersectionSphere3D(ray3D, sphere3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionSphere3D(final double[] ray3D, final double[] sphere3D, final int ray3DOffset, final int sphere3DOffset) {
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
	public static double intersectionTorus3D(final double[] ray3D, final double[] torus3D) {
		return intersectionTorus3D(ray3D, torus3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionTorus3D(final double[] ray3D, final double[] torus3D, final int ray3DOffset, final int torus3DOffset) {
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
	public static double intersectionTriangle3D(final double[] ray3D, final double[] triangle3D) {
		return intersectionTriangle3D(ray3D, triangle3D, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double intersectionTriangle3D(final double[] ray3D, final double[] triangle3D, final int ray3DOffset, final int triangle3DOffset) {
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
	public static double[] intersectionOrthonormalBasisCone3D(final double[] ray3D, final double[] cone3D, final double t) {
		return intersectionOrthonormalBasisCone3D(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisCone3D(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointCone3D(ray3D, cone3D, t, ray3DOffset, cone3DOffset);
		
		final double v = point3DGetZ(point3DSurfaceIntersectionPoint) / zMax;
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = -point3DGetX(point3DSurfaceIntersectionPoint) / (1.0D - v);
		final double vY = -point3DGetY(point3DSurfaceIntersectionPoint) / (1.0D - v);
		final double vZ = +zMax;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t) {
		return intersectionOrthonormalBasisCylinder3D(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointCylinder3D(ray3D, cylinder3D, t, ray3DOffset, cylinder3DOffset);
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = 0.0D;
		final double vY = 0.0D;
		final double vZ = zMax - zMin;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisDisk3D(final double[] ray3D, final double[] disk3D, final double t) {
		return intersectionOrthonormalBasisDisk3D(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisDisk3D(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointDisk3D(ray3D, disk3D, t, ray3DOffset, disk3DOffset);
		
		final double distance = sqrt(point3DGetX(point3DSurfaceIntersectionPoint) * point3DGetX(point3DSurfaceIntersectionPoint) + point3DGetY(point3DSurfaceIntersectionPoint) * point3DGetY(point3DSurfaceIntersectionPoint));
		
		final double uX = -phiMax * point3DGetY(point3DSurfaceIntersectionPoint);
		final double uY = +phiMax * point3DGetX(point3DSurfaceIntersectionPoint);
		final double uZ = +0.0D;
		
		final double vX = point3DGetX(point3DSurfaceIntersectionPoint) * (radiusInner - radiusOuter) / distance;
		final double vY = point3DGetY(point3DSurfaceIntersectionPoint) * (radiusInner - radiusOuter) / distance;
		final double vZ = 0.0D;
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return intersectionOrthonormalBasisHyperboloid3D(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		final double[] point3DTransformed = point3DLerp(point3DA, point3DB, (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA)));
		
		final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
		final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		final double phiCos = cos(phi);
		final double phiSin = sin(phi);
		
		final double uX = -phiMax * point3DGetY(point3D);
		final double uY = +phiMax * point3DGetX(point3D);
		final double uZ = +0.0D;
		
		final double vX = (point3DGetX(point3DB) - point3DGetX(point3DA)) * phiCos - (point3DGetY(point3DB) - point3DGetY(point3DA)) * phiSin;
		final double vY = (point3DGetX(point3DB) - point3DGetX(point3DA)) * phiSin + (point3DGetY(point3DB) - point3DGetY(point3DA)) * phiCos;
		final double vZ = point3DGetZ(point3DB) - point3DGetZ(point3DA);
		
		final double[] vector3DU = vector3DNormalize(vector3D(uX, uY, uZ));
		final double[] vector3DV = vector3DNormalize(vector3D(vX, vY, vZ));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return intersectionOrthonormalBasisParaboloid3D(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DU = vector3DNormalize(vector3D(-phiMax * point3DGetY(point3D), phiMax * point3DGetX(point3D), 0.0D));
		final double[] vector3DV = vector3DNormalize(vector3D((zMax - zMin) * (point3DGetX(point3D) / (2.0D * point3DGetZ(point3D))), (zMax - zMin) * (point3DGetY(point3D) / (2.0D * point3DGetZ(point3D))), zMax - zMin));
		final double[] vector3DW = vector3DCrossProduct(vector3DU, vector3DV);
		
		return orthonormalBasis33D(vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisPlane3D(final double[] ray3D, final double[] plane3D, final double t) {
		return intersectionOrthonormalBasisPlane3D(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionOrthonormalBasisPlane3D(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] vector3DW = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		final double[] vector3DV = vector3DDirectionNormalized(plane3DGetA(plane3D, point3D(), plane3DOffset, 0), plane3DGetB(plane3D, point3D(), plane3DOffset, 0));
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisPolygon3D(final double[] ray3D, final double[] polygon3D, final double t) {
		return intersectionOrthonormalBasisPolygon3D(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionOrthonormalBasisPolygon3D(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		final double[] vector3DW = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		final double[] vector3DV = vector3DDirectionNormalized(polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0), polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0));
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t) {
		return intersectionOrthonormalBasisRectangle3D(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionOrthonormalBasisRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DC = rectangle3DGetC(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DW = vector3DNormalNormalized(point3DA, point3DB, point3DC);
		final double[] vector3DV = vector3DDirectionNormalized(point3DA, point3DB);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return intersectionOrthonormalBasisRectangularCuboid3D(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] point3DMidpoint = point3DMidpoint(point3DMaximum, point3DMinimum);
		
		final double[] vector3DHalfDistance = vector3DMultiply(vector3DDirection(point3DMinimum, point3DMaximum), 0.5D);
		
		final double epsilon = 0.0001D;
		
		if(point3DGetX(point3D) + vector3DGetX(vector3DHalfDistance) - epsilon < point3DGetX(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(-1.0D, +0.0D, +0.0D));
		}
		
		if(point3DGetX(point3D) - vector3DGetX(vector3DHalfDistance) + epsilon > point3DGetX(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+1.0D, +0.0D, +0.0D));
		}
		
		if(point3DGetY(point3D) + vector3DGetY(vector3DHalfDistance) - epsilon < point3DGetY(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, -1.0D, +0.0D));
		}
		
		if(point3DGetY(point3D) - vector3DGetY(vector3DHalfDistance) + epsilon > point3DGetY(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +1.0D, +0.0D));
		}
		
		if(point3DGetZ(point3D) + vector3DGetZ(vector3DHalfDistance) - epsilon < point3DGetZ(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, -1.0D));
		}
		
		if(point3DGetZ(point3D) - vector3DGetZ(vector3DHalfDistance) + epsilon > point3DGetZ(point3DMidpoint)) {
			return orthonormalBasis33DFromW(vector3D(+0.0D, +0.0D, +1.0D));
		}
		
		return orthonormalBasis33D();
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisShape3D(final double[] ray3D, final double[] shape3D, final double t) {
		return intersectionOrthonormalBasisShape3D(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisShape3D(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return intersectionOrthonormalBasisCone3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return intersectionOrthonormalBasisCylinder3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return intersectionOrthonormalBasisDisk3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return intersectionOrthonormalBasisHyperboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return intersectionOrthonormalBasisParaboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return intersectionOrthonormalBasisPlane3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return intersectionOrthonormalBasisPolygon3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return intersectionOrthonormalBasisRectangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return intersectionOrthonormalBasisRectangularCuboid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return intersectionOrthonormalBasisSphere3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return intersectionOrthonormalBasisTorus3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return intersectionOrthonormalBasisTriangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return orthonormalBasis33D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisSphere3D(final double[] ray3D, final double[] sphere3D, final double t) {
		return intersectionOrthonormalBasisSphere3D(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisSphere3D(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DCenter = sphere3DGetCenter(sphere3D, point3D(), sphere3DOffset, 0);
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] vector3DW = vector3DDirectionNormalized(point3DCenter, point3D);
		final double[] vector3DV = vector3D(-PI_MULTIPLIED_BY_2 * vector3DGetY(vector3DW), PI_MULTIPLIED_BY_2 * vector3DGetX(vector3DW), 0.0D);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisTorus3D(final double[] ray3D, final double[] torus3D, final double t) {
		return intersectionOrthonormalBasisTorus3D(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisTorus3D(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		final double radiusInnerSquared = radiusInner * radiusInner;
		
		final double radiusOuter = torus3DGetRadiusOuter(torus3D, torus3DOffset);
		final double radiusOuterSquared = radiusOuter * radiusOuter;
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double derivative = vector3DLengthSquared(vector3DFromPoint3D(point3D)) - radiusInnerSquared - radiusOuterSquared;
		
		final double[] vector3DW = vector3DNormalize(vector3D(point3DGetX(point3D) * derivative, point3DGetY(point3D) * derivative, point3DGetZ(point3D) * derivative + 2.0D * radiusOuterSquared * point3DGetZ(point3D)));
		
		return orthonormalBasis33DFromW(vector3DW);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionOrthonormalBasisTriangle3D(final double[] ray3D, final double[] triangle3D, final double t) {
		return intersectionOrthonormalBasisTriangle3D(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionOrthonormalBasisTriangle3D(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point2DTextureCoordinatesA = triangle3DGetTextureCoordinatesA(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesB = triangle3DGetTextureCoordinatesB(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesC = triangle3DGetTextureCoordinatesC(triangle3D, point2D(), triangle3DOffset, 0);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DSurfaceNormalA = triangle3DGetSurfaceNormalA(triangle3D, vector3D(), triangle3DOffset, 0);
		final double[] vector3DSurfaceNormalB = triangle3DGetSurfaceNormalB(triangle3D, vector3D(), triangle3DOffset, 0);
		final double[] vector3DSurfaceNormalC = triangle3DGetSurfaceNormalC(triangle3D, vector3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeCB = vector3DDirection(point3DPositionC, point3DPositionB);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double barycentricCoordinatesU = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA) / determinant;
		final double barycentricCoordinatesV = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB) / determinant;
		final double barycentricCoordinatesW = 1.0D - barycentricCoordinatesU - barycentricCoordinatesV;
		
		final double[] point3DBarycentricCoordinates = point3D(barycentricCoordinatesU, barycentricCoordinatesV, barycentricCoordinatesW);
		
		final double[] vector2DEdgeCA = vector2DDirection(point2DTextureCoordinatesC, point2DTextureCoordinatesA);
		final double[] vector2DEdgeCB = vector2DDirection(point2DTextureCoordinatesC, point2DTextureCoordinatesB);
		
		final double determinantUV = vector2DCrossProduct(vector2DEdgeCA, vector2DEdgeCB);
		
		if(isZero(determinantUV)) {
			final double[] vector3DW = vector3DFromBarycentricCoordinatesNormalized(vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point3DBarycentricCoordinates);
			
			return orthonormalBasis33DFromW(vector3DW);
		}
		
		final double determinantUVReciprocal = 1.0D / determinantUV;
		
		final double x = (-vector2DGetX(vector2DEdgeCB) * vector3DGetX(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetX(vector3DEdgeCB)) * determinantUVReciprocal;
		final double y = (-vector2DGetX(vector2DEdgeCB) * vector3DGetY(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetY(vector3DEdgeCB)) * determinantUVReciprocal;
		final double z = (-vector2DGetX(vector2DEdgeCB) * vector3DGetZ(vector3DEdgeCA) + vector2DGetX(vector2DEdgeCA) * vector3DGetZ(vector3DEdgeCB)) * determinantUVReciprocal;
		
		final double[] vector3DW = vector3DFromBarycentricCoordinatesNormalized(vector3DSurfaceNormalA, vector3DSurfaceNormalB, vector3DSurfaceNormalC, point3DBarycentricCoordinates);
		final double[] vector3DV = vector3D(x, y, z);
		
		return orthonormalBasis33DFromWV(vector3DW, vector3DV);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointCone3D(final double[] ray3D, final double[] cone3D, final double t) {
		return intersectionSurfaceIntersectionPointCone3D(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointCone3D(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t) {
		return intersectionSurfaceIntersectionPointCylinder3D(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DSurfaceIntersectionPoint = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double radius = cylinder3DGetRadius(cylinder3D, cylinder3DOffset);
		final double length = sqrt(point3DGetX(point3DSurfaceIntersectionPoint) * point3DGetX(point3DSurfaceIntersectionPoint) + point3DGetY(point3DSurfaceIntersectionPoint) * point3DGetY(point3DSurfaceIntersectionPoint));
		final double scale = radius / length;
		
		final double[] point3DSurfaceIntersectionPointTransformed = point3D(point3DGetX(point3DSurfaceIntersectionPoint) * scale, point3DGetY(point3DSurfaceIntersectionPoint) * scale, point3DGetZ(point3DSurfaceIntersectionPoint));
		
		return point3DSurfaceIntersectionPointTransformed;
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointDisk3D(final double[] ray3D, final double[] disk3D, final double t) {
		return intersectionSurfaceIntersectionPointDisk3D(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointDisk3D(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DSurfaceIntersectionPoint = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double zMax = disk3DGetZMax(disk3D, disk3DOffset);
		
		final double[] point3DSurfaceIntersectionPointTransformed = point3D(point3DGetX(point3DSurfaceIntersectionPoint), point3DGetY(point3DSurfaceIntersectionPoint), zMax);
		
		return point3DSurfaceIntersectionPointTransformed;
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return intersectionSurfaceIntersectionPointHyperboloid3D(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return intersectionSurfaceIntersectionPointParaboloid3D(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointPlane3D(final double[] ray3D, final double[] plane3D, final double t) {
		return intersectionSurfaceIntersectionPointPlane3D(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointPlane3D(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointPolygon3D(final double[] ray3D, final double[] polygon3D, final double t) {
		return intersectionSurfaceIntersectionPointPolygon3D(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointPolygon3D(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t) {
		return intersectionSurfaceIntersectionPointRectangle3D(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return intersectionSurfaceIntersectionPointRectangularCuboid3D(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointShape3D(final double[] ray3D, final double[] shape3D, final double t) {
		return intersectionSurfaceIntersectionPointShape3D(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointShape3D(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return intersectionSurfaceIntersectionPointCone3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return intersectionSurfaceIntersectionPointCylinder3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return intersectionSurfaceIntersectionPointDisk3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return intersectionSurfaceIntersectionPointHyperboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return intersectionSurfaceIntersectionPointParaboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return intersectionSurfaceIntersectionPointPlane3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return intersectionSurfaceIntersectionPointPolygon3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return intersectionSurfaceIntersectionPointRectangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return intersectionSurfaceIntersectionPointRectangularCuboid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return intersectionSurfaceIntersectionPointSphere3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return intersectionSurfaceIntersectionPointTorus3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return intersectionSurfaceIntersectionPointTriangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return point2D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointSphere3D(final double[] ray3D, final double[] sphere3D, final double t) {
		return intersectionSurfaceIntersectionPointSphere3D(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointSphere3D(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointTorus3D(final double[] ray3D, final double[] torus3D, final double t) {
		return intersectionSurfaceIntersectionPointTorus3D(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointTorus3D(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceIntersectionPointTriangle3D(final double[] ray3D, final double[] triangle3D, final double t) {
		return intersectionSurfaceIntersectionPointTriangle3D(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionSurfaceIntersectionPointTriangle3D(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		return point3DAdd(ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0), ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0), t);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalCone3D(final double[] ray3D, final double[] cone3D, final double t) {
		return intersectionSurfaceNormalCone3D(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalCone3D(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisCone3D(ray3D, cone3D, t, ray3DOffset, cone3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t) {
		return intersectionSurfaceNormalCylinder3D(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisCylinder3D(ray3D, cylinder3D, t, ray3DOffset, cylinder3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalDisk3D(final double[] ray3D, final double[] disk3D, final double t) {
		return intersectionSurfaceNormalDisk3D(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalDisk3D(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisDisk3D(ray3D, disk3D, t, ray3DOffset, disk3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return intersectionSurfaceNormalHyperboloid3D(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisHyperboloid3D(ray3D, hyperboloid3D, t, ray3DOffset, hyperboloid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return intersectionSurfaceNormalParaboloid3D(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisParaboloid3D(ray3D, paraboloid3D, t, ray3DOffset, paraboloid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalPlane3D(final double[] ray3D, final double[] plane3D, final double t) {
		return intersectionSurfaceNormalPlane3D(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalPlane3D(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisPlane3D(ray3D, plane3D, t, ray3DOffset, plane3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalPolygon3D(final double[] ray3D, final double[] polygon3D, final double t) {
		return intersectionSurfaceNormalPolygon3D(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalPolygon3D(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisPolygon3D(ray3D, polygon3D, t, ray3DOffset, polygon3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t) {
		return intersectionSurfaceNormalRectangle3D(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisRectangle3D(ray3D, rectangle3D, t, ray3DOffset, rectangle3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return intersectionSurfaceNormalRectangularCuboid3D(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisRectangularCuboid3D(ray3D, rectangularCuboid3D, t, ray3DOffset, rectangularCuboid3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalShape3D(final double[] ray3D, final double[] shape3D, final double t) {
		return intersectionSurfaceNormalShape3D(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalShape3D(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisShape3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalSphere3D(final double[] ray3D, final double[] sphere3D, final double t) {
		return intersectionSurfaceNormalSphere3D(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalSphere3D(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisSphere3D(ray3D, sphere3D, t, ray3DOffset, sphere3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalTorus3D(final double[] ray3D, final double[] torus3D, final double t) {
		return intersectionSurfaceNormalTorus3D(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalTorus3D(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisTorus3D(ray3D, torus3D, t, ray3DOffset, torus3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalTriangle3D(final double[] ray3D, final double[] triangle3D, final double t) {
		return intersectionSurfaceNormalTriangle3D(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionSurfaceNormalTriangle3D(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		return orthonormalBasis33DGetW(intersectionOrthonormalBasisTriangle3D(ray3D, triangle3D, t, ray3DOffset, triangle3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesCone3D(final double[] ray3D, final double[] cone3D, final double t) {
		return intersectionTextureCoordinatesCone3D(ray3D, cone3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesCone3D(final double[] ray3D, final double[] cone3D, final double t, final int ray3DOffset, final int cone3DOffset) {
		final double phiMax = cone3DGetPhiMax(cone3D, cone3DOffset);
		final double zMax = cone3DGetZMax(cone3D, cone3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointCone3D(ray3D, cone3D, t, ray3DOffset, cone3DOffset);
		
		final double phi = getOrAdd(atan2(point3DGetY(point3DSurfaceIntersectionPoint), point3DGetX(point3DSurfaceIntersectionPoint)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = point3DGetZ(point3DSurfaceIntersectionPoint) / zMax;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t) {
		return intersectionTextureCoordinatesCylinder3D(ray3D, cylinder3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesCylinder3D(final double[] ray3D, final double[] cylinder3D, final double t, final int ray3DOffset, final int cylinder3DOffset) {
		final double phiMax = cylinder3DGetPhiMax(cylinder3D, cylinder3DOffset);
		final double zMax = cylinder3DGetZMax(cylinder3D, cylinder3DOffset);
		final double zMin = cylinder3DGetZMin(cylinder3D, cylinder3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointCylinder3D(ray3D, cylinder3D, t, ray3DOffset, cylinder3DOffset);
		
		final double phi = getOrAdd(atan2(point3DGetY(point3DSurfaceIntersectionPoint), point3DGetX(point3DSurfaceIntersectionPoint)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (point3DGetZ(point3DSurfaceIntersectionPoint) - zMin) / (zMax - zMin);
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesDisk3D(final double[] ray3D, final double[] disk3D, final double t) {
		return intersectionTextureCoordinatesDisk3D(ray3D, disk3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesDisk3D(final double[] ray3D, final double[] disk3D, final double t, final int ray3DOffset, final int disk3DOffset) {
		final double phiMax = disk3DGetPhiMax(disk3D, disk3DOffset);
		final double radiusInner = disk3DGetRadiusInner(disk3D, disk3DOffset);
		final double radiusOuter = disk3DGetRadiusOuter(disk3D, disk3DOffset);
		
		final double[] point3DSurfaceIntersectionPoint = intersectionSurfaceIntersectionPointDisk3D(ray3D, disk3D, t, ray3DOffset, disk3DOffset);
		
		final double distance = sqrt(point3DGetX(point3DSurfaceIntersectionPoint) * point3DGetX(point3DSurfaceIntersectionPoint) + point3DGetY(point3DSurfaceIntersectionPoint) * point3DGetY(point3DSurfaceIntersectionPoint));
		
		final double phi = getOrAdd(atan2(point3DGetY(point3DSurfaceIntersectionPoint), point3DGetX(point3DSurfaceIntersectionPoint)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (radiusOuter - distance) / (radiusOuter - radiusInner);
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t) {
		return intersectionTextureCoordinatesHyperboloid3D(ray3D, hyperboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesHyperboloid3D(final double[] ray3D, final double[] hyperboloid3D, final double t, final int ray3DOffset, final int hyperboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = hyperboloid3DGetA(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		final double[] point3DB = hyperboloid3DGetB(hyperboloid3D, point3D(), hyperboloid3DOffset, 0);
		
		final double phiMax = hyperboloid3DGetPhiMax(hyperboloid3D, hyperboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double v = (point3DGetZ(point3D) - point3DGetZ(point3DA)) / (point3DGetZ(point3DB) - point3DGetZ(point3DA));
		
		final double[] point3DTransformed = point3DLerp(point3DA, point3DB, v);
		
		final double x = point3DGetX(point3D) * point3DGetX(point3DTransformed) + point3DGetY(point3D) * point3DGetY(point3DTransformed);
		final double y = point3DGetY(point3D) * point3DGetX(point3DTransformed) - point3DGetX(point3D) * point3DGetY(point3DTransformed);
		
		final double phi = getOrAdd(atan2(y, x), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t) {
		return intersectionTextureCoordinatesParaboloid3D(ray3D, paraboloid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesParaboloid3D(final double[] ray3D, final double[] paraboloid3D, final double t, final int ray3DOffset, final int paraboloid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double phiMax = paraboloid3DGetPhiMax(paraboloid3D, paraboloid3DOffset);
		final double zMax = paraboloid3DGetZMax(paraboloid3D, paraboloid3DOffset);
		final double zMin = paraboloid3DGetZMin(paraboloid3D, paraboloid3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double phi = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2);
		
		final double u = phi / phiMax;
		final double v = (point3DGetZ(point3D) - zMin) / (zMax - zMin);
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesPlane3D(final double[] ray3D, final double[] plane3D, final double t) {
		return intersectionTextureCoordinatesPlane3D(ray3D, plane3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesPlane3D(final double[] ray3D, final double[] plane3D, final double t, final int ray3DOffset, final int plane3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = plane3DGetA(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DB = plane3DGetB(plane3D, point3D(), plane3DOffset, 0);
		final double[] point3DC = plane3DGetC(plane3D, point3D(), plane3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = plane3DGetSurfaceNormal(plane3D, vector3D(), plane3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DC) : isY ? vector2DDirectionZX(point3DC) : vector2DDirectionXY(point3DC);
		final double[] vector2DC = isX ? vector2DDirectionYZ(point3DB) : isY ? vector2DDirectionZX(point3DB) : vector2DDirectionXY(point3DB);
		
		final double[] vector2DAB = vector2DSubtract(vector2DB, vector2DA);
		final double[] vector2DAC = vector2DSubtract(vector2DC, vector2DA);
		
		final double determinant = vector2DCrossProduct(vector2DAB, vector2DAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? point3DGetY(point3D) : isY ? point3DGetZ(point3D) : point3DGetX(point3D);
		final double hV = isX ? point3DGetZ(point3D) : isY ? point3DGetX(point3D) : point3DGetY(point3D);
		
		final double u = hU * (-vector2DGetY(vector2DAB) * determinantReciprocal) + hV * (+vector2DGetX(vector2DAB) * determinantReciprocal) + vector2DCrossProduct(vector2DA, vector2DAB) * determinantReciprocal;
		final double v = hU * (+vector2DGetY(vector2DAC) * determinantReciprocal) + hV * (-vector2DGetX(vector2DAC) * determinantReciprocal) + vector2DCrossProduct(vector2DAC, vector2DA) * determinantReciprocal;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesPolygon3D(final double[] ray3D, final double[] polygon3D, final double t) {
		return intersectionTextureCoordinatesPolygon3D(ray3D, polygon3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesPolygon3D(final double[] ray3D, final double[] polygon3D, final double t, final int ray3DOffset, final int polygon3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final int n = polygon3DGetPoint3DCount(polygon3D, polygon3DOffset) - 1;
		
		final double[] point3DA = polygon3DGetPoint3D(polygon3D, 0, point3D(), polygon3DOffset, 0);
		final double[] point3DB = polygon3DGetPoint3D(polygon3D, 1, point3D(), polygon3DOffset, 0);
		final double[] point3DN = polygon3DGetPoint3D(polygon3D, n, point3D(), polygon3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = polygon3DGetSurfaceNormal(polygon3D, vector3D(), polygon3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DN) : isY ? vector2DDirectionZX(point3DN) : vector2DDirectionXY(point3DN);
		final double[] vector2DC = isX ? vector2DDirectionYZ(point3DB) : isY ? vector2DDirectionZX(point3DB) : vector2DDirectionXY(point3DB);
		
		final double[] vector2DAB = vector2DSubtract(vector2DB, vector2DA);
		final double[] vector2DAC = vector2DSubtract(vector2DC, vector2DA);
		
		final double determinant = vector2DCrossProduct(vector2DAB, vector2DAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? point3DGetY(point3D) : isY ? point3DGetZ(point3D) : point3DGetX(point3D);
		final double hV = isX ? point3DGetZ(point3D) : isY ? point3DGetX(point3D) : point3DGetY(point3D);
		
		final double u = hU * (-vector2DGetY(vector2DAB) * determinantReciprocal) + hV * (+vector2DGetX(vector2DAB) * determinantReciprocal) + vector2DCrossProduct(vector2DA, vector2DAB) * determinantReciprocal;
		final double v = hU * (+vector2DGetY(vector2DAC) * determinantReciprocal) + hV * (-vector2DGetX(vector2DAC) * determinantReciprocal) + vector2DCrossProduct(vector2DAC, vector2DA) * determinantReciprocal;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t) {
		return intersectionTextureCoordinatesRectangle3D(ray3D, rectangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesRectangle3D(final double[] ray3D, final double[] rectangle3D, final double t, final int ray3DOffset, final int rectangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DA = rectangle3DGetA(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DB = rectangle3DGetB(rectangle3D, point3D(), rectangle3DOffset, 0);
		final double[] point3DD = rectangle3DGetD(rectangle3D, point3D(), rectangle3DOffset, 0);
		
		final double[] vector3DSurfaceNormal = intersectionSurfaceNormalRectangle3D(ray3D, rectangle3D, t, ray3DOffset, rectangle3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final boolean isX = abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetY(vector3DSurfaceNormal)) && abs(vector3DGetX(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		final boolean isY = abs(vector3DGetY(vector3DSurfaceNormal)) > abs(vector3DGetZ(vector3DSurfaceNormal));
		
		final double[] vector2DA = isX ? vector2DDirectionYZ(point3DA) : isY ? vector2DDirectionZX(point3DA) : vector2DDirectionXY(point3DA);
		final double[] vector2DB = isX ? vector2DDirectionYZ(point3DD) : isY ? vector2DDirectionZX(point3DD) : vector2DDirectionXY(point3DD);
		final double[] vector2DC = isX ? vector2DDirectionYZ(point3DB) : isY ? vector2DDirectionZX(point3DB) : vector2DDirectionXY(point3DB);
		
		final double[] vector2DAB = vector2DSubtract(vector2DB, vector2DA);
		final double[] vector2DAC = vector2DSubtract(vector2DC, vector2DA);
		
		final double determinant = vector2DCrossProduct(vector2DAB, vector2DAC);
		final double determinantReciprocal = 1.0D / determinant;
		
		final double hU = isX ? point3DGetY(point3D) : isY ? point3DGetZ(point3D) : point3DGetX(point3D);
		final double hV = isX ? point3DGetZ(point3D) : isY ? point3DGetX(point3D) : point3DGetY(point3D);
		
		final double u = hU * (-vector2DGetY(vector2DAB) * determinantReciprocal) + hV * (+vector2DGetX(vector2DAB) * determinantReciprocal) + vector2DCrossProduct(vector2DA, vector2DAB) * determinantReciprocal;
		final double v = hU * (+vector2DGetY(vector2DAC) * determinantReciprocal) + hV * (-vector2DGetX(vector2DAC) * determinantReciprocal) + vector2DCrossProduct(vector2DAC, vector2DA) * determinantReciprocal;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t) {
		return intersectionTextureCoordinatesRectangularCuboid3D(ray3D, rectangularCuboid3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesRectangularCuboid3D(final double[] ray3D, final double[] rectangularCuboid3D, final double t, final int ray3DOffset, final int rectangularCuboid3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point3DMaximum = rectangularCuboid3DGetMaximum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		final double[] point3DMinimum = rectangularCuboid3DGetMinimum(rectangularCuboid3D, point3D(), rectangularCuboid3DOffset, 0);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double[] point3DMidpoint = point3DMidpoint(point3DMaximum, point3DMinimum);
		
		final double[] vector3DHalfDistance = vector3DMultiply(vector3DDirection(point3DMinimum, point3DMaximum), 0.5D);
		
		final double epsilon = 0.0001D;
		
		if(point3DGetX(point3D) + vector3DGetX(vector3DHalfDistance) - epsilon < point3DGetX(point3DMidpoint) || point3DGetX(point3D) - vector3DGetX(vector3DHalfDistance) + epsilon > point3DGetX(point3DMidpoint)) {
			return point2D(normalize(point3DGetZ(point3D), point3DGetZ(point3DMinimum), point3DGetZ(point3DMaximum)), normalize(point3DGetY(point3D), point3DGetY(point3DMinimum), point3DGetY(point3DMaximum)));
		}
		
		if(point3DGetY(point3D) + vector3DGetY(vector3DHalfDistance) - epsilon < point3DGetY(point3DMidpoint) || point3DGetY(point3D) - vector3DGetY(vector3DHalfDistance) + epsilon > point3DGetY(point3DMidpoint)) {
			return point2D(normalize(point3DGetX(point3D), point3DGetX(point3DMinimum), point3DGetX(point3DMaximum)), normalize(point3DGetZ(point3D), point3DGetZ(point3DMinimum), point3DGetZ(point3DMaximum)));
		}
		
		if(point3DGetZ(point3D) + vector3DGetZ(vector3DHalfDistance) - epsilon < point3DGetZ(point3DMidpoint) || point3DGetZ(point3D) - vector3DGetZ(vector3DHalfDistance) + epsilon > point3DGetZ(point3DMidpoint)) {
			return point2D(normalize(point3DGetX(point3D), point3DGetX(point3DMinimum), point3DGetX(point3DMaximum)), normalize(point3DGetY(point3D), point3DGetY(point3DMinimum), point3DGetY(point3DMaximum)));
		}
		
		return point2D(0.5D, 0.5D);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesShape3D(final double[] ray3D, final double[] shape3D, final double t) {
		return intersectionTextureCoordinatesShape3D(ray3D, shape3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesShape3D(final double[] ray3D, final double[] shape3D, final double t, final int ray3DOffset, final int shape3DOffset) {
		switch(shape3DGetID(shape3D, shape3DOffset)) {
			case CONE_3_ID:
				return intersectionTextureCoordinatesCone3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case CYLINDER_3_ID:
				return intersectionTextureCoordinatesCylinder3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case DISK_3_ID:
				return intersectionTextureCoordinatesDisk3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case HYPERBOLOID_3_ID:
				return intersectionTextureCoordinatesHyperboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PARABOLOID_3_ID:
				return intersectionTextureCoordinatesParaboloid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case PLANE_3_ID:
				return intersectionTextureCoordinatesPlane3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case POLYGON_3_ID:
				return intersectionTextureCoordinatesPolygon3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGLE_3_ID:
				return intersectionTextureCoordinatesRectangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case RECTANGULAR_CUBOID_3_ID:
				return intersectionTextureCoordinatesRectangularCuboid3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case SPHERE_3_ID:
				return intersectionTextureCoordinatesSphere3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TORUS_3_ID:
				return intersectionTextureCoordinatesTorus3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			case TRIANGLE_3_ID:
				return intersectionTextureCoordinatesTriangle3D(ray3D, shape3D, t, ray3DOffset, shape3DOffset);
			default:
				return point2D();
		}
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesSphere3D(final double[] ray3D, final double[] sphere3D, final double t) {
		return intersectionTextureCoordinatesSphere3D(ray3D, sphere3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesSphere3D(final double[] ray3D, final double[] sphere3D, final double t, final int ray3DOffset, final int sphere3DOffset) {
		return point2DSphericalCoordinates(intersectionSurfaceNormalSphere3D(ray3D, sphere3D, t, ray3DOffset, sphere3DOffset));
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesTorus3D(final double[] ray3D, final double[] torus3D, final double t) {
		return intersectionTextureCoordinatesTorus3D(ray3D, torus3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesTorus3D(final double[] ray3D, final double[] torus3D, final double t, final int ray3DOffset, final int torus3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double radiusInner = torus3DGetRadiusInner(torus3D, torus3DOffset);
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double u = getOrAdd(atan2(point3DGetY(point3D), point3DGetX(point3D)), 0.0D, PI_MULTIPLIED_BY_2) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final double v = (asin(saturate(point3DGetZ(point3D) / radiusInner, -1.0D, 1.0D)) + PI_DIVIDED_BY_2) * PI_RECIPROCAL;
		
		return point2D(u, v);
	}
	
//	TODO: Add Javadocs!
	public static double[] intersectionTextureCoordinatesTriangle3D(final double[] ray3D, final double[] triangle3D, final double t) {
		return intersectionTextureCoordinatesTriangle3D(ray3D, triangle3D, t, 0, 0);
	}
	
//	TODO: Add Javadocs!
	@SuppressWarnings("unused")
	public static double[] intersectionTextureCoordinatesTriangle3D(final double[] ray3D, final double[] triangle3D, final double t, final int ray3DOffset, final int triangle3DOffset) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D, point3D(), ray3DOffset, 0);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D, vector3D(), ray3DOffset, 0);
		
		final double[] point2DTextureCoordinatesA = triangle3DGetTextureCoordinatesA(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesB = triangle3DGetTextureCoordinatesB(triangle3D, point2D(), triangle3DOffset, 0);
		final double[] point2DTextureCoordinatesC = triangle3DGetTextureCoordinatesC(triangle3D, point2D(), triangle3DOffset, 0);
		
		final double[] point3DPositionA = triangle3DGetPositionA(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionB = triangle3DGetPositionB(triangle3D, point3D(), triangle3DOffset, 0);
		final double[] point3DPositionC = triangle3DGetPositionC(triangle3D, point3D(), triangle3DOffset, 0);
		
		final double[] vector3DEdgeAB = vector3DDirection(point3DPositionA, point3DPositionB);
		final double[] vector3DEdgeCA = vector3DDirection(point3DPositionC, point3DPositionA);
		final double[] vector3DEdgeABCrossEdgeCA = vector3DCrossProduct(vector3DEdgeAB, vector3DEdgeCA);
		final double[] vector3DOriginToPositionA = vector3DDirection(point3DOrigin, point3DPositionA);
		final double[] vector3DOriginToPositionACrossDirection = vector3DCrossProduct(vector3DOriginToPositionA, vector3DDirection);
		
		final double determinant = vector3DDotProduct(vector3DDirection, vector3DEdgeABCrossEdgeCA);
		
		final double barycentricCoordinatesU = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeCA) / determinant;
		final double barycentricCoordinatesV = vector3DDotProduct(vector3DOriginToPositionACrossDirection, vector3DEdgeAB) / determinant;
		final double barycentricCoordinatesW = 1.0D - barycentricCoordinatesU - barycentricCoordinatesV;
		
		final double[] point3DBarycentricCoordinates = point3D(barycentricCoordinatesU, barycentricCoordinatesV, barycentricCoordinatesW);
		
		final double[] point2DTextureCoordinates = point2DFromBarycentricCoordinates(point2DTextureCoordinatesA, point2DTextureCoordinatesB, point2DTextureCoordinatesC, point3DBarycentricCoordinates);
		
		return point2DTextureCoordinates;
	}
}