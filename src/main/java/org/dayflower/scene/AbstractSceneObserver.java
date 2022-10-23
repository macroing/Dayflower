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
package org.dayflower.scene;

import java.util.Objects;

/**
 * An {@code AbstractSceneObserver} is an abstract implementation of {@link SceneObserver} that does nothing by default.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractSceneObserver implements SceneObserver {
	/**
	 * Constructs a new {@code AbstractSceneObserver} instance.
	 */
	protected AbstractSceneObserver() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method is called by {@code scene} when it is told to update.
	 * <p>
	 * Returns {@code true} if, and only if, anything was changed as a result of this method call, {@code false} otherwise.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param delta the delta in milliseconds since last update
	 * @return {@code true} if, and only if, anything was changed as a result of this method call, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	@Override
	public boolean onUpdate(final Scene scene, final float delta) {
		Objects.requireNonNull(scene, "scene == null");
		
		return false;
	}
	
	/**
	 * This method is called by {@code scene} when a {@link Light} is added.
	 * <p>
	 * If either {@code scene} or {@code newLight} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param newLight the new {@code Light} instance that was added
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code newLight} are {@code null}
	 */
	@Override
	public void onAddLight(final Scene scene, final Light newLight) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newLight, "newLight == null");
	}
	
	/**
	 * This method is called by {@code scene} when a {@link Primitive} is added.
	 * <p>
	 * If either {@code scene} or {@code newPrimitive} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param newPrimitive the new {@code Primitive} instance that was added
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code newPrimitive} are {@code null}
	 */
	@Override
	public void onAddPrimitive(final Scene scene, final Primitive newPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newPrimitive, "newPrimitive == null");
	}
	
	/**
	 * This method is called by {@code scene} when the {@link Camera} changes.
	 * <p>
	 * If either {@code scene} or {@code oldCamera} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldCamera the old {@code Camera} instance that was changed
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code oldCamera} are {@code null}
	 */
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
	}
	
	/**
	 * This method is called by {@code scene} when the {@link Camera} changes.
	 * <p>
	 * If either {@code scene}, {@code oldCamera} or {@code newCamera} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldCamera the old {@code Camera} instance
	 * @param newCamera the new {@code Camera} instance
	 * @throws NullPointerException thrown if, and only if, either {@code scene}, {@code oldCamera} or {@code newCamera} are {@code null}
	 */
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera, final Camera newCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
		Objects.requireNonNull(newCamera, "newCamera == null");
	}
	
	/**
	 * This method is called by {@code scene} when the name changes.
	 * <p>
	 * If either {@code scene}, {@code oldName} or {@code newName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldName the old name
	 * @param newName the new name
	 * @throws NullPointerException thrown if, and only if, either {@code scene}, {@code oldName} or {@code newName} are {@code null}
	 */
	@Override
	public void onChangeName(final Scene scene, final String oldName, final String newName) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldName, "oldName == null");
		Objects.requireNonNull(newName, "newName == null");
	}
	
	/**
	 * This method is called by {@code scene} when a {@link Primitive} changes.
	 * <p>
	 * If either {@code scene} or {@code oldPrimitive} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldPrimitive the old {@code Primitive} instance that was changed
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code oldPrimitive} are {@code null}
	 */
	@Override
	public void onChangePrimitive(final Scene scene, final Primitive oldPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
	}
	
	/**
	 * This method is called by {@code scene} when a {@link Light} is removed.
	 * <p>
	 * If either {@code scene} or {@code oldLight} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldLight the old {@code Light} instance that was removed
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code oldLight} are {@code null}
	 */
	@Override
	public void onRemoveLight(final Scene scene, final Light oldLight) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldLight, "oldLight == null");
	}
	
	/**
	 * This method is called by {@code scene} when a {@link Primitive} is removed.
	 * <p>
	 * If either {@code scene} or {@code oldPrimitive} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance that called this method
	 * @param oldPrimitive the old {@code Primitive} instance that was removed
	 * @throws NullPointerException thrown if, and only if, either {@code scene} or {@code oldPrimitive} are {@code null}
	 */
	@Override
	public void onRemovePrimitive(final Scene scene, final Primitive oldPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldPrimitive, "oldPrimitive == null");
	}
}