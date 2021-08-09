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
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DToVector3DU;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DToVector3DV;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DToVector3DW;
import static org.dayflower.simplex.OrthonormalBasis.orthonormalBasis33DSet;
import static org.dayflower.simplex.Point.point2DSampleDiskUniformDistribution;
import static org.dayflower.simplex.Point.point3D;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DFromVector3D;
import static org.dayflower.simplex.Vector.vector3D;
import static org.dayflower.simplex.Vector.vector3DAdd;
import static org.dayflower.simplex.Vector.vector3DDirection;
import static org.dayflower.simplex.Vector.vector3DDirectionNormalized;
import static org.dayflower.simplex.Vector.vector3DFromPoint3D;
import static org.dayflower.simplex.Vector.vector3DMultiply;
import static org.dayflower.utility.Doubles.tan;

import java.lang.reflect.Field;//TODO: Add Javadocs!
import java.util.Objects;

import org.dayflower.utility.ParameterArguments;

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
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 0] = orthonormalBasis33D[orthonormalBasis33DOffset + 0];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 1] = orthonormalBasis33D[orthonormalBasis33DOffset + 1];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 2] = orthonormalBasis33D[orthonormalBasis33DOffset + 2];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 3] = orthonormalBasis33D[orthonormalBasis33DOffset + 3];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 4] = orthonormalBasis33D[orthonormalBasis33DOffset + 4];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 5] = orthonormalBasis33D[orthonormalBasis33DOffset + 5];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 6] = orthonormalBasis33D[orthonormalBasis33DOffset + 6];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 7] = orthonormalBasis33D[orthonormalBasis33DOffset + 7];
		camera3D[CAMERA_OFFSET_ORTHONORMAL_BASIS + 8] = orthonormalBasis33D[orthonormalBasis33DOffset + 8];
		camera3D[CAMERA_OFFSET_EYE + 0] = point3DEye[point3DEyeOffset + 0];
		camera3D[CAMERA_OFFSET_EYE + 1] = point3DEye[point3DEyeOffset + 1];
		camera3D[CAMERA_OFFSET_EYE + 2] = point3DEye[point3DEyeOffset + 2];
		camera3D[CAMERA_OFFSET_APERTURE_RADIUS] = apertureRadius;
		camera3D[CAMERA_OFFSET_FOCAL_DISTANCE] = focalDistance;
		camera3D[CAMERA_OFFSET_RESOLUTION_X] = resolutionX;
		camera3D[CAMERA_OFFSET_RESOLUTION_Y] = resolutionY;
		
		return camera3D;
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DGetEye(final double[] camera3D, final int camera3DOffset) {
		final double component1 = camera3D[camera3DOffset + CAMERA_OFFSET_EYE + 0];
		final double component2 = camera3D[camera3DOffset + CAMERA_OFFSET_EYE + 1];
		final double component3 = camera3D[camera3DOffset + CAMERA_OFFSET_EYE + 2];
		
		return point3D(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static double[] camera3DGetOrthonormalBasis33D(final double[] camera3D, final int camera3DOffset) {
		final double[] vector3DU = vector3D(camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 0], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 1], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 2]);
		final double[] vector3DV = vector3D(camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 3], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 4], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 5]);
		final double[] vector3DW = vector3D(camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 6], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 7], camera3D[camera3DOffset + CAMERA_OFFSET_ORTHONORMAL_BASIS + 8]);
		
		return orthonormalBasis33DSet(orthonormalBasis33D(), vector3DU, vector3DV, vector3DW);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[][] camera3DCreatePrimaryRay(final double[] camera3D, final int camera3DOffset, /*final double cameraApertureRadius, final double cameraFieldOfViewX, final double cameraFieldOfViewY, final double cameraFocalDistance, final double cameraResolutionX, final double cameraResolutionY, final double[] cameraEye, final double[] cameraOrthonormalBasisU, final double[] cameraOrthonormalBasisV, final double[] cameraOrthonormalBasisW, */final double imageX, final double imageY) {
		return camera3DCreatePrimaryRay(camera3D, camera3DOffset, /*cameraApertureRadius, cameraFieldOfViewX, cameraFieldOfViewY, cameraFocalDistance, cameraResolutionX, cameraResolutionY, cameraEye, cameraOrthonormalBasisU, cameraOrthonormalBasisV, cameraOrthonormalBasisW, */imageX, imageY, 0.5D, 0.5D);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[][] camera3DCreatePrimaryRay(final double[] camera3D, final int camera3DOffset, /*final double cameraApertureRadius, final double cameraFieldOfViewX, final double cameraFieldOfViewY, final double cameraFocalDistance, final double cameraResolutionX, final double cameraResolutionY, final double[] cameraEye, final double[] cameraOrthonormalBasisU, final double[] cameraOrthonormalBasisV, final double[] cameraOrthonormalBasisW, */final double imageX, final double imageY, final double pixelX, final double pixelY) {
//		Objects.requireNonNull(cameraEye, "cameraEye == null");
//		Objects.requireNonNull(cameraOrthonormalBasisU, "cameraOrthonormalBasisU == null");
//		Objects.requireNonNull(cameraOrthonormalBasisV, "cameraOrthonormalBasisV == null");
//		Objects.requireNonNull(cameraOrthonormalBasisW, "cameraOrthonormalBasisW == null");
		
//		ParameterArguments.requireExactArrayLength(cameraEye, 3, "cameraEye");
//		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisU, 3, "cameraOrthonormalBasisU");
//		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisV, 3, "cameraOrthonormalBasisV");
//		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisW, 3, "cameraOrthonormalBasisW");
		
		
		final double apertureRadius = camera3DGetApertureRadius(camera3D, camera3DOffset);
		final double focalDistance = camera3DGetFocalDistance(camera3D, camera3DOffset);
		
		final double fieldOfViewX = tan(+camera3DGetFieldOfViewX(camera3D, camera3DOffset) * 0.5D);
		final double fieldOfViewY = tan(-camera3DGetFieldOfViewY(camera3D, camera3DOffset) * 0.5D);
		
		final double cameraX = 2.0D * ((imageX + pixelX) / (camera3DGetResolutionX(camera3D, camera3DOffset) - 1.0D)) - 1.0D;
		final double cameraY = 2.0D * ((imageY + pixelY) / (camera3DGetResolutionY(camera3D, camera3DOffset) - 1.0D)) - 1.0D;
		
		final double[] orthonormalBasis33D = camera3DGetOrthonormalBasis33D(camera3D, camera3DOffset);
		
		final double[] vector3DU = orthonormalBasis33DToVector3DU(orthonormalBasis33D);
		final double[] vector3DV = orthonormalBasis33DToVector3DV(orthonormalBasis33D);
		final double[] vector3DW = orthonormalBasis33DToVector3DW(orthonormalBasis33D);
		
		final double[] point2D = point2DSampleDiskUniformDistribution();
		
		final double[] point3DEye = camera3DGetEye(camera3D, camera3DOffset);
		final double[] point3DOnPlaneOneUnitAwayFromEye = point3DFromVector3D(vector3DAdd(vector3DAdd(vector3DMultiply(vector3DU, fieldOfViewX * cameraX), vector3DMultiply(vector3DV, fieldOfViewY * cameraY)), vector3DAdd(vector3DFromPoint3D(point3DEye), vector3DW)));
		final double[] point3DOnImagePlane = point3DAdd(point3DEye, vector3DDirection(point3DEye, point3DOnPlaneOneUnitAwayFromEye), focalDistance);
		final double[] point3DOrigin = apertureRadius > 0.00001D ? point3DAdd(point3DEye, vector3DAdd(vector3DMultiply(vector3DU, point2D[0] * apertureRadius), vector3DMultiply(vector3DV, point2D[1] * apertureRadius))) : point3DEye;
		
		final double[] vector3Direction = vector3DDirectionNormalized(point3DOrigin, point3DOnImagePlane);
		
		return new double[][] {point3DOrigin, vector3Direction};
	}
}