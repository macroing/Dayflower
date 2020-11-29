/**
 * Copyright 2020 J&#246;rgen Lundgren
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

import java.lang.reflect.Field;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;

//TODO: Add Javadocs!
public interface CameraObserver {
//	TODO: Add Javadocs!
	void onChangeApertureRadius(final Camera camera, final float oldApertureRadius, final float newApertureRadius);
	
//	TODO: Add Javadocs!
	void onChangeEye(final Camera camera, final Point3F oldEye, final Point3F newEye);
	
//	TODO: Add Javadocs!
	void onChangeFieldOfViewX(final Camera camera, final AngleF oldFieldOfViewX, final AngleF newFieldOfViewX);
	
//	TODO: Add Javadocs!
	void onChangeFieldOfViewY(final Camera camera, final AngleF oldFieldOfViewY, final AngleF newFieldOfViewY);
	
//	TODO: Add Javadocs!
	void onChangeFocalDistance(final Camera camera, final float oldFocalDistance, final float newFocalDistance);
	
//	TODO: Add Javadocs!
	void onChangeLens(final Camera camera, final Lens oldLens, final Lens newLens);
	
//	TODO: Add Javadocs!
	void onChangeOrthonormalBasis(final Camera camera, final OrthonormalBasis33F oldOrthonormalBasis, final OrthonormalBasis33F newOrthonormalBasis);
	
//	TODO: Add Javadocs!
	void onChangePitch(final Camera camera, final AngleF oldPitch, final AngleF newPitch);
	
//	TODO: Add Javadocs!
	void onChangeResolutionX(final Camera camera, final float oldResolutionX, final float newResolutionX);
	
//	TODO: Add Javadocs!
	void onChangeResolutionY(final Camera camera, final float oldResolutionY, final float newResolutionY);
	
//	TODO: Add Javadocs!
	void onChangeWalkLockEnabled(final Camera camera, final boolean oldIsWalkLockEnabled, final boolean newIsWalkLockEnabled);
	
//	TODO: Add Javadocs!
	void onChangeYaw(final Camera camera, final AngleF oldYaw, final AngleF newYaw);
}