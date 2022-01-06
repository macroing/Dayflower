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
package org.dayflower.scene.compiler;

import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;

final class CameraCache {
	private Camera camera;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CameraCache() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void build(final CompiledCameraCache compiledCameraCache) {
		compiledCameraCache.setCamera(CompiledCameraCache.toCamera(this.camera));
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledCameraCache());
	}
	
	public void clear() {
		this.camera = null;
	}
	
	public void setup(final Scene scene) {
		this.camera = scene.getCamera();
	}
}