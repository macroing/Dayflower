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

import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Ray.ray3DGetDirection;
import static org.dayflower.simplex.Ray.ray3DGetOrigin;
import static org.dayflower.simplex.Ray.ray3DGetTMaximum;
import static org.dayflower.simplex.Ray.ray3DGetTMinimum;
import static org.dayflower.simplex.Vector.vector3DCrossProduct;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDotProduct;
import static org.dayflower.simplex.Vector.vector3DLengthSquared;
import static org.dayflower.simplex.Vector.vector3DNormalize;
import static org.dayflower.utility.Doubles.NaN;
import static org.dayflower.utility.Doubles.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Doubles.atan2;
import static org.dayflower.utility.Doubles.getOrAdd;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.solveQuadraticSystem;
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Shape {
	private Shape() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRayCone(final double[] ray3D, final double conePhiMax, final double coneRadius, final double coneZMax) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double k = (coneRadius / coneZMax) * (coneRadius / coneZMax);
		
		final double a = vector3DDirection[0] * vector3DDirection[0] + vector3DDirection[1] * vector3DDirection[1] - k * vector3DDirection[2] * vector3DDirection[2];
		final double b = 2.0D * (vector3DDirection[0] * point3DOrigin[0] + vector3DDirection[1] * point3DOrigin[1] - k * vector3DDirection[2] * (point3DOrigin[2] - coneZMax));
		final double c = point3DOrigin[0] * point3DOrigin[0] + point3DOrigin[1] * point3DOrigin[1] - k * (point3DOrigin[2] - coneZMax) * (point3DOrigin[2] - coneZMax);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point3D[1], point3D[0]), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3D[2] >= 0.0D && point3D[2] <= coneZMax && phi <= conePhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRayCylinder(final double[] ray3D, final double cylinderPhiMax, final double cylinderRadius, final double cylinderZMax, final double cylinderZMin) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double a = vector3DDirection[0] * vector3DDirection[0] + vector3DDirection[1] * vector3DDirection[1];
		final double b = 2.0D * (vector3DDirection[0] * point3DOrigin[0] + vector3DDirection[1] * point3DOrigin[1]);
		final double c = point3DOrigin[0] * point3DOrigin[0] + point3DOrigin[1] * point3DOrigin[1] - cylinderRadius * cylinderRadius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double radius = sqrt(point3D[0] * point3D[0] + point3D[1] * point3D[1]);
				final double phi = getOrAdd(atan2(point3D[1] * (cylinderRadius / radius), point3D[0] * (cylinderRadius / radius)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point3D[2] >= cylinderZMin && point3D[2] <= cylinderZMax && phi <= cylinderPhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRayDisk(final double[] ray3D, final double diskPhiMax, final double diskRadiusInner, final double diskRadiusOuter, final double diskZMax) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		if(isZero(vector3DDirection[2])) {
			return NaN;
		}
		
		final double t = (diskZMax - point3DOrigin[2]) / vector3DDirection[2];
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] point3D = point3DAdd(point3DOrigin, vector3DDirection, t);
		
		final double distanceSquared = point3D[0] * point3D[0] + point3D[1] * point3D[1];
		
		if(distanceSquared > diskRadiusOuter * diskRadiusOuter || distanceSquared < diskRadiusInner * diskRadiusInner) {
			return NaN;
		}
		
		final double phi = getOrAdd(atan2(point3D[1], point3D[0]), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(phi > diskPhiMax) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRayParaboloid(final double[] ray3D, final double paraboloidPhiMax, final double paraboloidRadius, final double paraboloidZMax, final double paraboloidZMin) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double k = paraboloidZMax / (paraboloidRadius * paraboloidRadius);
		
		final double a = k * (vector3DDirection[0] * vector3DDirection[0] + vector3DDirection[1] * vector3DDirection[1]);
		final double b = 2.0D * k * (vector3DDirection[0] * point3DOrigin[0] + vector3DDirection[1] * point3DOrigin[1]) - vector3DDirection[2];
		final double c = k * (point3DOrigin[0] * point3DOrigin[0] + point3DOrigin[1] * point3DOrigin[1]) - point3DOrigin[2];
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > tMinimum && t < tMaximum) {
				final double[] point = point3DAdd(point3DOrigin, vector3DDirection, t);
				
				final double phi = getOrAdd(atan2(point[1], point[0]), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point[2] >= paraboloidZMin && point[2] <= paraboloidZMax && phi <= paraboloidPhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRayPlane(final double[] ray3D, final double[] planeA, final double[] planeB, final double[] planeC) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double[] planeAB = vector3DDirection(planeA, planeB);
		final double[] planeABNormalized = vector3DNormalize(planeAB);
		
		final double[] planeAC = vector3DDirection(planeA, planeC);
		final double[] planeACNormalized = vector3DNormalize(planeAC);
		
		final double[] planeSurfaceNormal = vector3DCrossProduct(planeABNormalized, planeACNormalized);
		
		final double planeSurfaceNormalDotRayDirection = vector3DDotProduct(planeSurfaceNormal, vector3DDirection);
		
		if(isZero(planeSurfaceNormalDotRayDirection)) {
			return NaN;
		}
		
		final double[] rayOriginToPlaneA = vector3DDirection(point3DOrigin, planeA);
		
		final double t = vector3DDotProduct(rayOriginToPlaneA, planeSurfaceNormal) / planeSurfaceNormalDotRayDirection;
		
		if(t > tMinimum && t < tMaximum) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double intersectionRaySphere(final double[] ray3D, final double[] sphereCenter, final double sphereRadius) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double[] sphereCenterToRayOrigin = vector3DDirection(sphereCenter, point3DOrigin);
		
		final double a = vector3DLengthSquared(vector3DDirection);
		final double b = vector3DDotProduct(sphereCenterToRayOrigin, vector3DDirection) * 2.0D;
		final double c = vector3DLengthSquared(sphereCenterToRayOrigin) - sphereRadius * sphereRadius;
		
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
//	TODO: Refactor!
	public static double intersectionRayTriangle(final double[] ray3D, final double[] triangleA, final double[] triangleB, final double[] triangleC) {
		final double[] point3DOrigin = ray3DGetOrigin(ray3D);
		
		final double[] vector3DDirection = ray3DGetDirection(ray3D);
		
		final double tMinimum = ray3DGetTMinimum(ray3D);
		final double tMaximum = ray3DGetTMaximum(ray3D);
		
		final double[] triangleAB = vector3DDirection(triangleA, triangleB);
		final double[] triangleCA = vector3DDirection(triangleC, triangleA);
		final double[] triangleSurfaceNormal = vector3DCrossProduct(triangleAB, triangleCA);
		
		final double determinant = vector3DDotProduct(vector3DDirection, triangleSurfaceNormal);
		
		final double[] rayOriginToTriangleA = vector3DDirection(point3DOrigin, triangleA);
		
		final double t = vector3DDotProduct(triangleSurfaceNormal, rayOriginToTriangleA) / determinant;
		
		if(t <= tMinimum || t >= tMaximum) {
			return NaN;
		}
		
		final double[] direction = vector3DCrossProduct(rayOriginToTriangleA, vector3DDirection);
		
		final double uScaled = vector3DDotProduct(direction, triangleCA);
		final double u = uScaled / determinant;
		
		if(u < 0.0D) {
			return NaN;
		}
		
		final double vScaled = vector3DDotProduct(direction, triangleAB);
		final double v = vScaled / determinant;
		
		if(v < 0.0D) {
			return NaN;
		}
		
		if((uScaled + vScaled) * determinant > determinant * determinant) {
			return NaN;
		}
		
		return t;
	}
}