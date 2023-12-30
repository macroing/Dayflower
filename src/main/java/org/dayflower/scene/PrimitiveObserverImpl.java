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
package org.dayflower.scene;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.utility.ParameterArguments;

final class PrimitiveObserverImpl implements PrimitiveObserver {
	private final List<SceneObserver> sceneObservers;
	private final Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PrimitiveObserverImpl(final Scene scene, final List<SceneObserver> sceneObservers) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
		this.sceneObservers = ParameterArguments.requireNonNullList(sceneObservers, "sceneObservers");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onChangeAreaLight(final Primitive primitive, final Optional<AreaLight> oldOptionalAreaLight, final Optional<AreaLight> newOptionalAreaLight) {
		Objects.requireNonNull(primitive, "primitive == null");
		Objects.requireNonNull(oldOptionalAreaLight, "oldOptionalAreaLight == null");
		Objects.requireNonNull(newOptionalAreaLight, "newOptionalAreaLight == null");
		
		doOnChangePrimitive(primitive);
	}
	
	@Override
	public void onChangeBoundingVolume(final Primitive primitive, final BoundingVolume3F oldBoundingVolume, final BoundingVolume3F newBoundingVolume) {
		Objects.requireNonNull(primitive, "primitive == null");
		Objects.requireNonNull(oldBoundingVolume, "oldBoundingVolume == null");
		Objects.requireNonNull(newBoundingVolume, "newBoundingVolume == null");
		
		doOnChangePrimitive(primitive);
	}
	
	@Override
	public void onChangeMaterial(final Primitive primitive, final Material oldMaterial, final Material newMaterial) {
		Objects.requireNonNull(primitive, "primitive == null");
		Objects.requireNonNull(oldMaterial, "oldMaterial == null");
		Objects.requireNonNull(newMaterial, "newMaterial == null");
		
		doOnChangePrimitive(primitive);
	}
	
	@Override
	public void onChangeShape(final Primitive primitive, final Shape3F oldShape, final Shape3F newShape) {
		Objects.requireNonNull(primitive, "primitive == null");
		Objects.requireNonNull(oldShape, "oldShape == null");
		Objects.requireNonNull(newShape, "newShape == null");
		
		doOnChangePrimitive(primitive);
	}
	
	@Override
	public void onChangeTransform(final Primitive primitive, final Transform oldTransform, final Transform newTransform) {
		Objects.requireNonNull(primitive, "primitive == null");
		Objects.requireNonNull(oldTransform, "oldTransform == null");
		Objects.requireNonNull(newTransform, "newTransform == null");
		
		doOnChangePrimitive(primitive);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doOnChangePrimitive(final Primitive oldPrimitive) {
		for(final SceneObserver sceneObserver : this.sceneObservers) {
			sceneObserver.onChangePrimitive(this.scene, oldPrimitive);
		}
	}
}