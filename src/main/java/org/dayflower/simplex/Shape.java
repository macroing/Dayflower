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
	public static double intersectionRayCone(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double conePhiMax, final double coneRadius, final double coneZMax) {
		final double k = (coneRadius / coneZMax) * (coneRadius / coneZMax);
		
		final double a = rayDirection[0] * rayDirection[0] + rayDirection[1] * rayDirection[1] - k * rayDirection[2] * rayDirection[2];
		final double b = 2.0D * (rayDirection[0] * rayOrigin[0] + rayDirection[1] * rayOrigin[1] - k * rayDirection[2] * (rayOrigin[2] - coneZMax));
		final double c = rayOrigin[0] * rayOrigin[0] + rayOrigin[1] * rayOrigin[1] - k * (rayOrigin[2] - coneZMax) * (rayOrigin[2] - coneZMax);
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > rayTMinimum && t < rayTMaximum) {
				final double[] point = point3DAdd(rayOrigin, rayDirection, t);
				
				final double phi = getOrAdd(atan2(point[1], point[0]), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point[2] >= 0.0D && point[2] <= coneZMax && phi <= conePhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRayCylinder(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double cylinderPhiMax, final double cylinderRadius, final double cylinderZMax, final double cylinderZMin) {
		final double a = rayDirection[0] * rayDirection[0] + rayDirection[1] * rayDirection[1];
		final double b = 2.0D * (rayDirection[0] * rayOrigin[0] + rayDirection[1] * rayOrigin[1]);
		final double c = rayOrigin[0] * rayOrigin[0] + rayOrigin[1] * rayOrigin[1] - cylinderRadius * cylinderRadius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > rayTMinimum && t < rayTMaximum) {
				final double[] point = point3DAdd(rayOrigin, rayDirection, t);
				
				final double radius = sqrt(point[0] * point[0] + point[1] * point[1]);
				final double phi = getOrAdd(atan2(point[1] * (cylinderRadius / radius), point[0] * (cylinderRadius / radius)), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point[2] >= cylinderZMin && point[2] <= cylinderZMax && phi <= cylinderPhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRayDisk(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double diskPhiMax, final double diskRadiusInner, final double diskRadiusOuter, final double diskZMax) {
		if(isZero(rayDirection[2])) {
			return NaN;
		}
		
		final double t = (diskZMax - rayOrigin[2]) / rayDirection[2];
		
		if(t <= rayTMinimum || t >= rayTMaximum) {
			return NaN;
		}
		
		final double[] point = point3DAdd(rayOrigin, rayDirection, t);
		
		final double distanceSquared = point[0] * point[0] + point[1] * point[1];
		
		if(distanceSquared > diskRadiusOuter * diskRadiusOuter || distanceSquared < diskRadiusInner * diskRadiusInner) {
			return NaN;
		}
		
		final double phi = getOrAdd(atan2(point[1], point[0]), 0.0D, PI_MULTIPLIED_BY_2);
		
		if(phi > diskPhiMax) {
			return NaN;
		}
		
		return t;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRayParaboloid(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double paraboloidPhiMax, final double paraboloidRadius, final double paraboloidZMax, final double paraboloidZMin) {
		final double k = paraboloidZMax / (paraboloidRadius * paraboloidRadius);
		
		final double a = k * (rayDirection[0] * rayDirection[0] + rayDirection[1] * rayDirection[1]);
		final double b = 2.0D * k * (rayDirection[0] * rayOrigin[0] + rayDirection[1] * rayOrigin[1]) - rayDirection[2];
		final double c = k * (rayOrigin[0] * rayOrigin[0] + rayOrigin[1] * rayOrigin[1]) - rayOrigin[2];
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		for(int i = 0; i < ts.length; i++) {
			final double t = ts[i];
			
			if(isNaN(t)) {
				return NaN;
			}
			
			if(t > rayTMinimum && t < rayTMaximum) {
				final double[] point = point3DAdd(rayOrigin, rayDirection, t);
				
				final double phi = getOrAdd(atan2(point[1], point[0]), 0.0D, PI_MULTIPLIED_BY_2);
				
				if(point[2] >= paraboloidZMin && point[2] <= paraboloidZMax && phi <= paraboloidPhiMax) {
					return t;
				}
			}
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRayPlane(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double[] planeA, final double[] planeB, final double[] planeC) {
		final double[] planeAB = vector3DDirection(planeA, planeB);
		final double[] planeABNormalized = vector3DNormalize(planeAB);
		
		final double[] planeAC = vector3DDirection(planeA, planeC);
		final double[] planeACNormalized = vector3DNormalize(planeAC);
		
		final double[] planeSurfaceNormal = vector3DCrossProduct(planeABNormalized, planeACNormalized);
		
		final double planeSurfaceNormalDotRayDirection = vector3DDotProduct(planeSurfaceNormal, rayDirection);
		
		if(isZero(planeSurfaceNormalDotRayDirection)) {
			return NaN;
		}
		
		final double[] rayOriginToPlaneA = vector3DDirection(rayOrigin, planeA);
		
		final double t = vector3DDotProduct(rayOriginToPlaneA, planeSurfaceNormal) / planeSurfaceNormalDotRayDirection;
		
		if(t > rayTMinimum && t < rayTMaximum) {
			return t;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRaySphere(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double[] sphereCenter, final double sphereRadius) {
		final double[] sphereCenterToRayOrigin = vector3DDirection(sphereCenter, rayOrigin);
		
		final double a = vector3DLengthSquared(rayDirection);
		final double b = vector3DDotProduct(sphereCenterToRayOrigin, rayDirection) * 2.0D;
		final double c = vector3DLengthSquared(sphereCenterToRayOrigin) - sphereRadius * sphereRadius;
		
		final double[] ts = solveQuadraticSystem(a, b, c);
		
		final double t0 = ts[0];
		final double t1 = ts[1];
		
		if(!isNaN(t0) && t0 > rayTMinimum && t0 < rayTMaximum) {
			return t0;
		}
		
		if(!isNaN(t1) && t1 > rayTMinimum && t1 < rayTMaximum) {
			return t1;
		}
		
		return NaN;
	}
	
//	TODO: Add Javadocs!
	public static double intersectionRayTriangle(final double[] rayOrigin, final double[] rayDirection, final double rayTMinimum, final double rayTMaximum, final double[] triangleA, final double[] triangleB, final double[] triangleC) {
		final double[] triangleAB = vector3DDirection(triangleA, triangleB);
		final double[] triangleCA = vector3DDirection(triangleC, triangleA);
		final double[] triangleSurfaceNormal = vector3DCrossProduct(triangleAB, triangleCA);
		
		final double determinant = vector3DDotProduct(rayDirection, triangleSurfaceNormal);
		
		final double[] rayOriginToTriangleA = vector3DDirection(rayOrigin, triangleA);
		
		final double t = vector3DDotProduct(triangleSurfaceNormal, rayOriginToTriangleA) / determinant;
		
		if(t <= rayTMinimum || t >= rayTMaximum) {
			return NaN;
		}
		
		final double[] direction = vector3DCrossProduct(rayOriginToTriangleA, rayDirection);
		
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