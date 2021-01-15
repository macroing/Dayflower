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
package org.dayflower.javafx.application;

import java.util.Objects;

import org.dayflower.renderer.ProgressiveImageOrderRenderer;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneObserver;

import javafx.application.Platform;

final class SceneObserverImpl implements SceneObserver {
	private final ProgressiveImageOrderRenderer progressiveImageOrderRenderer;
	private final RendererTabPane rendererTabPane;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneObserverImpl(final ProgressiveImageOrderRenderer progressiveImageOrderRenderer, final RendererTabPane rendererTabPane) {
		this.progressiveImageOrderRenderer = Objects.requireNonNull(progressiveImageOrderRenderer, "progressiveImageOrderRenderer == null");
		this.rendererTabPane = Objects.requireNonNull(rendererTabPane, "rendererTabPane == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onAddLight(final Scene scene, final Light newLight) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newLight, "newLight == null");
	}
	
	@Override
	public void onAddPrimitive(final Scene scene, final Primitive newPrimitive) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(newPrimitive, "newPrimitive == null");
		
		Platform.runLater(() -> this.rendererTabPane.getRendererViewPane().getObjectTreeView().add(newPrimitive));
		
		this.progressiveImageOrderRenderer.renderShutdown();
		this.progressiveImageOrderRenderer.clear();
	}
	
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
		
		this.rendererTabPane.getRendererViewPane().setCameraUpdateRequired(true);
	}
	
	@Override
	public void onChangeCamera(final Scene scene, final Camera oldCamera, final Camera newCamera) {
		Objects.requireNonNull(scene, "scene == null");
		Objects.requireNonNull(oldCamera, "oldCamera == null");
		Objects.requireNonNull(newCamera, "newCamera == null");
		
		this.rendererTabPane.getRendererViewPane().setCameraUpdateRequired(true);
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
		
		Platform.runLater(() -> this.rendererTabPane.getRendererViewPane().getObjectTreeView().remove(oldPrimitive));
		
		this.progressiveImageOrderRenderer.renderShutdown();
		this.progressiveImageOrderRenderer.clear();
	}
}