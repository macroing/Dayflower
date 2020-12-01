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
package org.dayflower.javafx;

import java.util.Objects;

import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.SceneObserver;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

final class SceneTreeView extends TreeView<String> implements SceneObserver {
	public SceneTreeView(final Scene scene) {
		super(new SceneTreeItem(scene));
		
		scene.addSceneObserver(this);
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
		
		Platform.runLater(() -> {
			final
			SceneTreeItem sceneTreeItem = SceneTreeItem.class.cast(getRoot());
			sceneTreeItem.addPrimitive(newPrimitive);
		});
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
		
		Platform.runLater(() -> {
			final
			SceneTreeItem sceneTreeItem = SceneTreeItem.class.cast(getRoot());
			sceneTreeItem.removePrimitive(oldPrimitive);
		});
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class MaterialTreeItem extends TreeItem<String> {
		private final Material material;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public MaterialTreeItem(final Material material) {
			super(material.getName());
			
			this.material = material;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class PrimitiveTreeItem extends TreeItem<String> {
		private final Primitive primitive;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public PrimitiveTreeItem(final Primitive primitive) {
			super("Primitive");
			
			this.primitive = Objects.requireNonNull(primitive, "primitive == null");
			
			getChildren().add(new MaterialTreeItem(primitive.getMaterial()));
			getChildren().add(new ShapeTreeItem(primitive.getShape()));
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Primitive getPrimitive() {
			return this.primitive;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class SceneTreeItem extends TreeItem<String> {
		private final Scene scene;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public SceneTreeItem(final Scene scene) {
			super("Scene");
			
			this.scene = Objects.requireNonNull(scene, "scene == null");
			
			for(final Primitive primitive : scene.getPrimitives()) {
				getChildren().add(new PrimitiveTreeItem(primitive));
			}
			
			setExpanded(true);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public void addPrimitive(final Primitive primitive) {
			getChildren().add(new PrimitiveTreeItem(primitive));
		}
		
		public void removePrimitive(final Primitive primitive) {
			for(final TreeItem<String> treeItem : getChildren()) {
				if(treeItem instanceof PrimitiveTreeItem) {
					final PrimitiveTreeItem primitiveTreeItem = PrimitiveTreeItem.class.cast(treeItem);
					
					if(primitiveTreeItem.getPrimitive().equals(primitive)) {
						getChildren().remove(treeItem);
						
						break;
					}
				}
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class ShapeTreeItem extends TreeItem<String> {
		private final Shape3F shape;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ShapeTreeItem(final Shape3F shape) {
			super(shape.getName());
			
			this.shape = shape;
		}
	}
}