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
package org.dayflower.scene;

import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.utility.ParameterArguments;

final class CameraObserverImpl implements CameraObserver {
	private final List<SceneObserver> sceneObservers;
	private final Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CameraObserverImpl(final Scene scene, final List<SceneObserver> sceneObservers) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
		this.sceneObservers = ParameterArguments.requireNonNullList(sceneObservers, "sceneObservers");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onChangeApertureRadius(final Camera camera, final float oldApertureRadius, final float newApertureRadius) {
		Objects.requireNonNull(camera, "camera == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeEye(final Camera camera, final Point3F oldEye, final Point3F newEye) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldEye, "oldEye == null");
		Objects.requireNonNull(newEye, "newEye == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeFieldOfViewX(final Camera camera, final AngleF oldFieldOfViewX, final AngleF newFieldOfViewX) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldFieldOfViewX, "oldFieldOfViewX == null");
		Objects.requireNonNull(newFieldOfViewX, "newFieldOfViewX == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeFieldOfViewY(final Camera camera, final AngleF oldFieldOfViewY, final AngleF newFieldOfViewY) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldFieldOfViewY, "oldFieldOfViewY == null");
		Objects.requireNonNull(newFieldOfViewY, "newFieldOfViewY == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeFocalDistance(final Camera camera, final float oldFocalDistance, final float newFocalDistance) {
		Objects.requireNonNull(camera, "camera == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeLens(final Camera camera, final Lens oldLens, final Lens newLens) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldLens, "oldLens == null");
		Objects.requireNonNull(newLens, "newLens == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeOrthonormalBasis(final Camera camera, final OrthonormalBasis33F oldOrthonormalBasis, final OrthonormalBasis33F newOrthonormalBasis) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldOrthonormalBasis, "oldOrthonormalBasis == null");
		Objects.requireNonNull(newOrthonormalBasis, "newOrthonormalBasis == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangePitch(final Camera camera, final AngleF oldPitch, final AngleF newPitch) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldPitch, "oldPitch == null");
		Objects.requireNonNull(newPitch, "newPitch == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeResolutionX(final Camera camera, final float oldResolutionX, final float newResolutionX) {
		Objects.requireNonNull(camera, "camera == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeResolutionY(final Camera camera, final float oldResolutionY, final float newResolutionY) {
		Objects.requireNonNull(camera, "camera == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeWalkLockEnabled(final Camera camera, final boolean oldIsWalkLockEnabled, final boolean newIsWalkLockEnabled) {
		Objects.requireNonNull(camera, "camera == null");
		
		doOnChangeCamera(camera);
	}
	
	@Override
	public void onChangeYaw(final Camera camera, final AngleF oldYaw, final AngleF newYaw) {
		Objects.requireNonNull(camera, "camera == null");
		Objects.requireNonNull(oldYaw, "oldYaw == null");
		Objects.requireNonNull(newYaw, "newYaw == null");
		
		doOnChangeCamera(camera);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doOnChangeCamera(final Camera oldCamera) {
		for(final SceneObserver sceneObserver : this.sceneObservers) {
			sceneObserver.onChangeCamera(this.scene, oldCamera);
		}
	}
}