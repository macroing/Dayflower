/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.scene;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;

/**
 * A {@code CameraObserver} is used to observe changes to a {@link Camera} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface CameraObserver {
	/**
	 * This method is called by {@code camera} when the aperture radius changes.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldApertureRadius a {@code float} that represents the old aperture radius
	 * @param newApertureRadius a {@code float} that represents the new aperture radius
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	void onChangeApertureRadius(final Camera camera, final float oldApertureRadius, final float newApertureRadius);
	
	/**
	 * This method is called by {@code camera} when the eye changes.
	 * <p>
	 * If either {@code camera}, {@code oldEye} or {@code newEye} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldEye a {@link Point3F} instance that represents the old eye
	 * @param newEye a {@code Point3F} instance that represents the new eye
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldEye} or {@code newEye} are {@code null}
	 */
	void onChangeEye(final Camera camera, final Point3F oldEye, final Point3F newEye);
	
	/**
	 * This method is called by {@code camera} when the field of view on the X-axis changes.
	 * <p>
	 * If either {@code camera}, {@code oldFieldOfViewX} or {@code newFieldOfViewX} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldFieldOfViewX an {@link AngleF} instance that represents the old field of view on the X-axis
	 * @param newFieldOfViewX an {@code AngleF} instance that represents the new field of view on the X-axis
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldFieldOfViewX} or {@code newFieldOfViewX} are {@code null}
	 */
	void onChangeFieldOfViewX(final Camera camera, final AngleF oldFieldOfViewX, final AngleF newFieldOfViewX);
	
	/**
	 * This method is called by {@code camera} when the field of view on the Y-axis changes.
	 * <p>
	 * If either {@code camera}, {@code oldFieldOfViewY} or {@code newFieldOfViewY} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldFieldOfViewY an {@link AngleF} instance that represents the old field of view on the Y-axis
	 * @param newFieldOfViewY an {@code AngleF} instance that represents the new field of view on the Y-axis
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldFieldOfViewY} or {@code newFieldOfViewY} are {@code null}
	 */
	void onChangeFieldOfViewY(final Camera camera, final AngleF oldFieldOfViewY, final AngleF newFieldOfViewY);
	
	/**
	 * This method is called by {@code camera} when the focal distance changes.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldFocalDistance a {@code float} that represents the old focal distance
	 * @param newFocalDistance a {@code float} that represents the new focal distance
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	void onChangeFocalDistance(final Camera camera, final float oldFocalDistance, final float newFocalDistance);
	
	/**
	 * This method is called by {@code camera} when the lens changes.
	 * <p>
	 * If either {@code camera}, {@code oldLens} or {@code newLens} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldLens a {@link Lens} instance that represents the old lens
	 * @param newLens a {@code Lens} instance that represents the new lens
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldLens} or {@code newLens} are {@code null}
	 */
	void onChangeLens(final Camera camera, final Lens oldLens, final Lens newLens);
	
	/**
	 * This method is called by {@code camera} when the orthonormal basis changes.
	 * <p>
	 * If either {@code camera}, {@code oldOrthonormalBasis} or {@code newOrthonormalBasis} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldOrthonormalBasis an {@link OrthonormalBasis33F} instance that represents the old orthonormal basis
	 * @param newOrthonormalBasis an {@code OrthonormalBasis33F} instance that represents the new orthonormal basis
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldOrthonormalBasis} or {@code newOrthonormalBasis} are {@code null}
	 */
	void onChangeOrthonormalBasis(final Camera camera, final OrthonormalBasis33F oldOrthonormalBasis, final OrthonormalBasis33F newOrthonormalBasis);
	
	/**
	 * This method is called by {@code camera} when the pitch changes.
	 * <p>
	 * If either {@code camera}, {@code oldPitch} or {@code newPitch} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldPitch an {@link AngleF} instance that represents the old pitch
	 * @param newPitch an {@code AngleF} instance that represents the new pitch
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldPitch} or {@code newPitch} are {@code null}
	 */
	void onChangePitch(final Camera camera, final AngleF oldPitch, final AngleF newPitch);
	
	/**
	 * This method is called by {@code camera} when the resolution on the X-axis changes.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldResolutionX a {@code float} that represents the old resolution on the X-axis
	 * @param newResolutionX a {@code float} that represents the new resolution on the X-axis
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	void onChangeResolutionX(final Camera camera, final float oldResolutionX, final float newResolutionX);
	
	/**
	 * This method is called by {@code camera} when the resolution on the Y-axis changes.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldResolutionY a {@code float} that represents the old resolution on the Y-axis
	 * @param newResolutionY a {@code float} that represents the new resolution on the Y-axis
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	void onChangeResolutionY(final Camera camera, final float oldResolutionY, final float newResolutionY);
	
	/**
	 * This method is called by {@code camera} when the walk lock state changes.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldIsWalkLockEnabled a {@code boolean} that represents the old walk lock state
	 * @param newIsWalkLockEnabled a {@code boolean} that represents the new walk lock state
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	void onChangeWalkLockEnabled(final Camera camera, final boolean oldIsWalkLockEnabled, final boolean newIsWalkLockEnabled);
	
	/**
	 * This method is called by {@code camera} when the yaw changes.
	 * <p>
	 * If either {@code camera}, {@code oldYaw} or {@code newYaw} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance that called this method
	 * @param oldYaw an {@link AngleF} instance that represents the old yaw
	 * @param newYaw an {@code AngleF} instance that represents the new yaw
	 * @throws NullPointerException thrown if, and only if, either {@code camera}, {@code oldYaw} or {@code newYaw} are {@code null}
	 */
	void onChangeYaw(final Camera camera, final AngleF oldYaw, final AngleF newYaw);
}