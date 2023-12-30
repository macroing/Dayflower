/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.renderer.gpu;

import java.util.Objects;
import java.util.Optional;

import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneObserver;
import org.dayflower.scene.compiler.CompiledScene;
import org.dayflower.scene.compiler.CompiledSceneModifier;

final class SceneObserverImpl implements SceneObserver {
	private final AbstractSceneKernel abstractSceneKernel;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneObserverImpl(final AbstractSceneKernel abstractSceneKernel) {
		this.abstractSceneKernel = Objects.requireNonNull(abstractSceneKernel, "abstractSceneKernel == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public boolean onUpdate(final Scene scene, final float delta) {
		Objects.requireNonNull(scene, "scene == null");
		
		return false;
	}
	
	@Override
	public void onAddLight(final Scene scene, final Light newLight) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newLight, "newLight == null");
	}
	
	@Override
	public void onAddPrimitive(final Scene scene, final Primitive newPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newPrimitive, "newPrimitive == null");
		
		final Optional<CompiledScene> optionalCompiledScene = this.abstractSceneKernel.getCompiledScene();
		
		if(optionalCompiledScene.isPresent()) {
			final CompiledScene compiledScene = optionalCompiledScene.get();
			
			final CompiledSceneModifier compiledSceneModifier = new CompiledSceneModifier(compiledScene);
			
			final boolean isAdded = compiledSceneModifier.addPrimitive(newPrimitive);
			
			if(isAdded) {
				this.abstractSceneKernel.updateCompiledSceneRequest();
			}
		}
	}
	
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
	}
	
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera, final Camera newCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
		Objects.requireNonNull(newCamera, "newCamera == null");
	}
	
	@Override
	public void onChangeName(final Scene scene, final String oldName, final String newName) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldName, "oldName == null");
		Objects.requireNonNull(newName, "newName == null");
	}
	
	@Override
	public void onChangePrimitive(final Scene scene, final Primitive oldPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
	}
	
	@Override
	public void onRemoveLight(final Scene scene, final Light oldLight) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldLight, "oldLight == null");
	}
	
	@Override
	public void onRemovePrimitive(final Scene scene, final Primitive oldPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
		
		final Optional<CompiledScene> optionalCompiledScene = this.abstractSceneKernel.getCompiledScene();
		
		if(optionalCompiledScene.isPresent()) {
			final CompiledScene compiledScene = optionalCompiledScene.get();
			
			final CompiledSceneModifier compiledSceneModifier = new CompiledSceneModifier(compiledScene);
			
			final boolean isRemoved = compiledSceneModifier.removePrimitive(oldPrimitive);
			
			if(isRemoved) {
				this.abstractSceneKernel.updateCompiledSceneRequest();
			}
		}
	}
}