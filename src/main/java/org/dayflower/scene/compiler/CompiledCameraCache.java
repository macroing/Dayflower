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
package org.dayflower.scene.compiler;

import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Lens;

/**
 * A {@code CompiledCameraCache} contains the {@link Camera} instance in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledCameraCache {
	/**
	 * The offset for the aperture radius in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_APERTURE_RADIUS = 15;
	
	/**
	 * The offset for the {@link Point3F} instance representing the eye in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_EYE = 12;
	
	/**
	 * The offset for the {@link AngleF} instance representing the field of view on the X-axis in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_FIELD_OF_VIEW_X = 0;
	
	/**
	 * The offset for the {@link AngleF} instance representing the field of view on the Y-axis in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_FIELD_OF_VIEW_Y = 1;
	
	/**
	 * The offset for the focal distance in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_FOCAL_DISTANCE = 16;
	
	/**
	 * The offset for the {@link Lens} instance representing the lens in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_LENS = 2;
	
	/**
	 * The offset for the {@link Vector3F} instance pointing in the U-direction of the {@link OrthonormalBasis33F} instance in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_ORTHONORMAL_BASIS_U = 3;
	
	/**
	 * The offset for the {@link Vector3F} instance pointing in the V-direction of the {@link OrthonormalBasis33F} instance in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_ORTHONORMAL_BASIS_V = 6;
	
	/**
	 * The offset for the {@link Vector3F} instance pointing in the W-direction of the {@link OrthonormalBasis33F} instance in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_ORTHONORMAL_BASIS_W = 9;
	
	/**
	 * The offset for the resolution on the X-axis in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_RESOLUTION_X = 17;
	
	/**
	 * The offset for the resolution on the Y-axis in a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_OFFSET_RESOLUTION_Y = 18;
	
	/**
	 * The length of a compiled {@link Camera} instance.
	 */
	public static final int CAMERA_LENGTH = 19;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] cameraArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledCameraCache} instance.
	 */
	public CompiledCameraCache() {
		setCameraArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains the {@link Camera} instances in compiled form that is associated with this {@code CompiledCameraCache} instance.
	 * 
	 * @return a {@code float[]} that contains the {@code Camera} instances in compiled form that is associated with this {@code CompiledCameraCache} instance
	 */
	public float[] getCameraArray() {
		return this.cameraArray;
	}
	
	/**
	 * Sets the {@link Camera} instance in compiled form to {@code cameraArray}.
	 * <p>
	 * If {@code cameraArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraArray the {@code Camera} instance in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cameraArray} is {@code null}
	 */
	public void setCameraArray(final float[] cameraArray) {
		this.cameraArray = Objects.requireNonNull(cameraArray, "cameraArray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code camera} in compiled form.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera a {@link Camera} instance
	 * @return a {@code float[]} with {@code camera} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	public static float[] toArray(final Camera camera) {
		final AngleF fieldOfViewX = camera.getFieldOfViewX();
		final AngleF fieldOfViewY = camera.getFieldOfViewY();
		
		final Lens lens = camera.getLens();
		
		final OrthonormalBasis33F orthonormalBasis = camera.getOrthonormalBasis();
		
		final Point3F eye = camera.getEye();
		
		final float apertureRadius = camera.getApertureRadius();
		final float focalDistance = camera.getFocalDistance();
		
		final float resolutionX = camera.getResolutionX();
		final float resolutionY = camera.getResolutionY();
		
		final float[] array = new float[CAMERA_LENGTH];
		
		array[CAMERA_OFFSET_FIELD_OF_VIEW_X] = fieldOfViewX.getRadians();
		array[CAMERA_OFFSET_FIELD_OF_VIEW_Y] = fieldOfViewY.getRadians();
		array[CAMERA_OFFSET_LENS] = lens.ordinal();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 0] = orthonormalBasis.getU().getX();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 1] = orthonormalBasis.getU().getY();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_U + 2] = orthonormalBasis.getU().getZ();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 0] = orthonormalBasis.getV().getX();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 1] = orthonormalBasis.getV().getY();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_V + 2] = orthonormalBasis.getV().getZ();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 0] = orthonormalBasis.getW().getX();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 1] = orthonormalBasis.getW().getY();
		array[CAMERA_OFFSET_ORTHONORMAL_BASIS_W + 2] = orthonormalBasis.getW().getZ();
		array[CAMERA_OFFSET_EYE + 0] = eye.getX();
		array[CAMERA_OFFSET_EYE + 1] = eye.getY();
		array[CAMERA_OFFSET_EYE + 2] = eye.getZ();
		array[CAMERA_OFFSET_APERTURE_RADIUS] = apertureRadius;
		array[CAMERA_OFFSET_FOCAL_DISTANCE] = focalDistance;
		array[CAMERA_OFFSET_RESOLUTION_X] = resolutionX;
		array[CAMERA_OFFSET_RESOLUTION_Y] = resolutionY;
		
		return array;
	}
}