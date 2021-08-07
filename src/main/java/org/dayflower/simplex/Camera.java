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

import static org.dayflower.simplex.Point.point2DSampleDiskUniformDistribution;
import static org.dayflower.simplex.Point.point3DAdd;
import static org.dayflower.simplex.Point.point3DFromVector3D;
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
	private Camera() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[][] cameraCreatePrimaryRay(final double cameraApertureRadius, final double cameraFieldOfViewX, final double cameraFieldOfViewY, final double cameraFocalDistance, final double cameraResolutionX, final double cameraResolutionY, final double[] cameraEye, final double[] cameraOrthonormalBasisU, final double[] cameraOrthonormalBasisV, final double[] cameraOrthonormalBasisW, final double imageX, final double imageY) {
		return cameraCreatePrimaryRay(cameraApertureRadius, cameraFieldOfViewX, cameraFieldOfViewY, cameraFocalDistance, cameraResolutionX, cameraResolutionY, cameraEye, cameraOrthonormalBasisU, cameraOrthonormalBasisV, cameraOrthonormalBasisW, imageX, imageY, 0.5D, 0.5D);
	}
	
//	TODO: Add Javadocs!
//	TODO: Refactor!
	public static double[][] cameraCreatePrimaryRay(final double cameraApertureRadius, final double cameraFieldOfViewX, final double cameraFieldOfViewY, final double cameraFocalDistance, final double cameraResolutionX, final double cameraResolutionY, final double[] cameraEye, final double[] cameraOrthonormalBasisU, final double[] cameraOrthonormalBasisV, final double[] cameraOrthonormalBasisW, final double imageX, final double imageY, final double pixelX, final double pixelY) {
		Objects.requireNonNull(cameraEye, "cameraEye == null");
		Objects.requireNonNull(cameraOrthonormalBasisU, "cameraOrthonormalBasisU == null");
		Objects.requireNonNull(cameraOrthonormalBasisV, "cameraOrthonormalBasisV == null");
		Objects.requireNonNull(cameraOrthonormalBasisW, "cameraOrthonormalBasisW == null");
		
		ParameterArguments.requireExactArrayLength(cameraEye, 3, "cameraEye");
		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisU, 3, "cameraOrthonormalBasisU");
		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisV, 3, "cameraOrthonormalBasisV");
		ParameterArguments.requireExactArrayLength(cameraOrthonormalBasisW, 3, "cameraOrthonormalBasisW");
		
		final double fieldOfViewX = tan(+cameraFieldOfViewX * 0.5D);
		final double fieldOfViewY = tan(-cameraFieldOfViewY * 0.5D);
		
		final double cameraX = 2.0D * ((imageX + pixelX) / (cameraResolutionX - 1.0D)) - 1.0D;
		final double cameraY = 2.0D * ((imageY + pixelY) / (cameraResolutionY - 1.0D)) - 1.0D;
		
		final double[] point2D = point2DSampleDiskUniformDistribution();
		
		final double[] point3DOnPlaneOneUnitAwayFromEye = point3DFromVector3D(vector3DAdd(vector3DAdd(vector3DMultiply(cameraOrthonormalBasisU, fieldOfViewX * cameraX), vector3DMultiply(cameraOrthonormalBasisV, fieldOfViewY * cameraY)), vector3DAdd(vector3DFromPoint3D(cameraEye), cameraOrthonormalBasisW)));
		final double[] point3DOnImagePlane = point3DAdd(cameraEye, vector3DDirection(cameraEye, point3DOnPlaneOneUnitAwayFromEye), cameraFocalDistance);
		final double[] point3DOrigin = cameraApertureRadius > 0.00001D ? point3DAdd(cameraEye, vector3DAdd(vector3DMultiply(cameraOrthonormalBasisU, point2D[0] * cameraApertureRadius), vector3DMultiply(cameraOrthonormalBasisV, point2D[1] * cameraApertureRadius))) : cameraEye;
		
		final double[] vector3Direction = vector3DDirectionNormalized(point3DOrigin, point3DOnImagePlane);
		
		return new double[][] {point3DOrigin, vector3Direction};
	}
}