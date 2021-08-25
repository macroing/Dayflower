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
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DGetU;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DGetV;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DGetW;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DSet;
import static org.dayflower.simplex.Point.point2DSampleDiskUniformDistribution;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DFromVector3D;
import static org.dayflower.simplex.Point.point3DSet;
import static org.dayflower.simplex.Ray.ray3D;
import static org.dayflower.simplex.Vector.vector3DAdd;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DFromPoint3D;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.utility.Doubles.tan;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
public final class Camera {
	/**
	 * The relative offset for the aperture radius in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_APERTURE_RADIUS = 15;
	
	/**
	 * The relative offset for the eye in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_EYE = 12;
	
	/**
	 * The relative offset for the field of view on the X-axis in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_FIELD_OF_VIEW_X = 0;
	
	/**
	 * The relative offset for the field of view on the Y-axis in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_FIELD_OF_VIEW_Y = 1;
	
	/**
	 * The relative offset for the focal distance in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_FOCAL_DISTANCE = 16;
	
	/**
	 * The relative offset for the lens in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_LENS = 2;
	
	/**
	 * The relative offset for the orthonormal basis in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_ORTHONORMAL_BASIS = 3;
	
	/**
	 * The relative offset for the resolution on the X-axis in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_RESOLUTION_X = 17;
	
	/**
	 * The relative offset for the resolution on the Y-axis in a {@code double[]} that contains a camera.
	 */
	public static final int CAMERA_OFFSET_RESOLUTION_Y = 18;
	
	/**
	 * The length of a camera.
	 */
	public static final int CAMERA_LENGTH = 19;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Camera() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Camera3D ////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double camera3DGetApertureRadius(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_APERTURE_RADIUS];
	}
	
//	TODO: Add Javadocs!
	public static double camera3DGetFieldOfViewX(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_FIELD_OF_VIEW_X];
	}
	
//	TODO: Add Javadocs!
	public static double camera3DGetFieldOfViewY(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_FIELD_OF_VIEW_Y];
	}
	
//	TODO: Add Javadocs!
	public static double camera3DGetFocalDistance(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_FOCAL_DISTANCE];
	}
	
//	TODO: Add Javadocs!
	public static double camera3DGetResolutionX(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_RESOLUTION_X];
	}
	
//	TODO: Add Javadocs!
	public static double camera3DGetResolutionY(final double[] camera3D, final int camera3DOffset) {
		return camera3D[camera3DOffset + CAMERA_OFFSET_RESOLUTION_Y];
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] camera3D(final double fieldOfViewX, final double fieldOfViewY, final int lens, final double[] orthonormalBasis33D, final double[] point3DEye, final double apertureRadius, final double focalDistance, final double resolutionX, final double resolutionY) {
		return camera3D(fieldOfViewX, fieldOfViewY, lens, orthonormalBasis33D, point3DEye, apertureRadius, focalDistance, resolutionX, resolutionY, 0, 0);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[] camera3D(final double fieldOfViewX, final double fieldOfViewY, final int lens, final double[] orthonormalBasis33D, final double[] point3DEye, final double apertureRadius, final double focalDistance, final double resolutionX, final double resolutionY, final int orthonormalBasis33DOffset, final int point3DEyeOffset) {
		final double[] camera3D = new double[CAMERA_LENGTH];
		
		camera3D[CAMERA_OFFSET_FIELD_OF_VIEW_X] = fieldOfViewX;
		camera3D[CAMERA_OFFSET_FIELD_OF_VIEW_Y] = fieldOfViewY;
		camera3D[CAMERA_OFFSET_LENS] = lens;
		camera3D[CAMERA_OFFSET_APERTURE_RADIUS] = apertureRadius;
		camera3D[CAMERA_OFFSET_FOCAL_DISTANCE] = focalDistance;
		camera3D[CAMERA_OFFSET_RESOLUTION_X] = resolutionX;
		camera3D[CAMERA_OFFSET_RESOLUTION_Y] = resolutionY;
		
		orthonormalBasis33DSet(camera3D, orthonormalBasis33D, CAMERA_OFFSET_ORTHONORMAL_BASIS, orthonormalBasis33DOffset);
		
		point3DSet(camera3D, point3DEye, CAMERA_OFFSET_EYE, point3DEyeOffset);
		
		return camera3D;
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DCreatePrimaryRay(final double[] camera3D, final double imageX, final double imageY) {
		return camera3DCreatePrimaryRay(camera3D, imageX, imageY, 0.5D, 0.5D);
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DCreatePrimaryRay(final double[] camera3D, final double imageX, final double imageY, final double pixelX, final double pixelY) {
		return camera3DCreatePrimaryRay(camera3D, imageX, imageY, pixelX, pixelY, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DCreatePrimaryRay(final double[] camera3D, final double imageX, final double imageY, final double pixelX, final double pixelY, final int camera3DOffset) {
		final double apertureRadius = camera3DGetApertureRadius(camera3D, camera3DOffset);
		final double focalDistance = camera3DGetFocalDistance(camera3D, camera3DOffset);
		
		final double fieldOfViewX = tan(+camera3DGetFieldOfViewX(camera3D, camera3DOffset) * 0.5D);
		final double fieldOfViewY = tan(-camera3DGetFieldOfViewY(camera3D, camera3DOffset) * 0.5D);
		
		final double cameraX = 2.0D * ((imageX + pixelX) / (camera3DGetResolutionX(camera3D, camera3DOffset) - 1.0D)) - 1.0D;
		final double cameraY = 2.0D * ((imageY + pixelY) / (camera3DGetResolutionY(camera3D, camera3DOffset) - 1.0D)) - 1.0D;
		
		final double[] orthonormalBasis33D = camera3DGetOrthonormalBasis(camera3D, camera3DOffset);
		
		final double[] vector3DU = orthonormalBasis33DGetU(orthonormalBasis33D);
		final double[] vector3DV = orthonormalBasis33DGetV(orthonormalBasis33D);
		final double[] vector3DW = orthonormalBasis33DGetW(orthonormalBasis33D);
		
		final double[] point2D = point2DSampleDiskUniformDistribution();
		
		final double[] point3DEye = camera3DGetEye(camera3D, camera3DOffset);
		final double[] point3DOnPlaneOneUnitAwayFromEye = point3DFromVector3D(vector3DAdd(vector3DAdd(vector3DMultiply(vector3DU, fieldOfViewX * cameraX), vector3DMultiply(vector3DV, fieldOfViewY * cameraY)), vector3DAdd(vector3DFromPoint3D(point3DEye), vector3DW)));
		final double[] point3DOnImagePlane = point3DAdd(point3DEye, vector3DDirection(point3DEye, point3DOnPlaneOneUnitAwayFromEye), focalDistance);
		final double[] point3DOrigin = apertureRadius > 0.00001D ? point3DAdd(point3DEye, vector3DAdd(vector3DMultiply(vector3DU, point2D[0] * apertureRadius), vector3DMultiply(vector3DV, point2D[1] * apertureRadius))) : point3DEye;
		
		final double[] vector3Direction = vector3DDirectionNormalized(point3DOrigin, point3DOnImagePlane);
		
		return ray3D(point3DOrigin, vector3Direction);
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DGetEye(final double[] camera3D, final int camera3DOffset) {
		return point3DSet(point3D(), camera3D, 0, camera3DOffset + CAMERA_OFFSET_EYE);
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DGetOrthonormalBasis(final double[] camera3D, final int camera3DOffset) {
		return orthonormalBasis33DSet(orthonormalBasis33D(), camera3D, 0, camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS);
	}
}