/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DDistance;
import static org.dayflower.simplex.Point.point3DSet;
import static org.dayflower.simplex.Point.point3DTransformAndDivideMatrix44D;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DSet;
import static org.dayflower.simplex.Vector.vector3DTransformMatrix44D;
import static org.dayflower.utility.Doubles.MAX_VALUE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;

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