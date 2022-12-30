/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;

import org.macroing.javafx.scene.control.ObjectTreeView;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

final class ScenePropertyView extends VBox {
	private final ObjectTreeView<String, Object> objectTreeView;
	private final PrimitiveConfigurationView primitiveConfigurationView;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ScenePropertyView(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
		this.objectTreeView = doCreateObjectTreeView(scene);
		this.primitiveConfigurationView = new PrimitiveConfigurationView();
		
		for(final Primitive primitive : scene.getPrimitives()) {
			this.objectTreeView.add(primitive);
		}
		
		setBorder(new Border(new BorderStroke(Color.rgb(181, 181, 181), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.0D, 0.0D, 0.0D, 1.0D))));
		setFillWidth(true);
		setPadding(new Insets(10.0D, 10.0D, 10.0D, 10.0D));
		setSpacing(20.0D);
		
		getChildren().add(this.objectTreeView);
		getChildren().add(this.primitiveConfigurationView);
		
		VBox.setVgrow(this.primitiveConfigurationView, Priority.ALWAYS);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ObjectTreeView<String, Object> getObjectTreeView() {
		return this.objectTreeView;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Function<Object, ContextMenu> doCreateMapperUToContextMenu(final Scene scene) {
		return object -> {
			if(object instanceof Primitive) {
				final
				MenuItem menuItemDelete = new MenuItem();
				menuItemDelete.setOnAction(actionEvent -> scene.removePrimitive(Primitive.class.cast(object)));
				menuItemDelete.setText("Delete");
				
				final
				MenuItem menuItemToggle = new MenuItem();
				menuItemToggle.setOnAction(actionEvent -> scene.togglePrimitiveInstanceID(Primitive.class.cast(object).getInstanceID()));
				menuItemToggle.setText("Toggle");
				
				final
				ContextMenu contextMenu = new ContextMenu();
				contextMenu.getItems().add(menuItemDelete);
				contextMenu.getItems().add(menuItemToggle);
				
				return contextMenu;
			}
			
			return null;
		};
	}
	
	private static Function<Object, List<Object>> doCreateMapperUToListU() {
		return object -> {
			final List<Object> list = new ArrayList<>();
			
			if(object instanceof Primitive) {
				final Primitive primitive = Primitive.class.cast(object);
				
				list.add(primitive.getMaterial());
				list.add(primitive.getShape());
//				list.add(primitive.getTransform());
			} else if(object instanceof Transform) {
				final Transform transform = Transform.class.cast(object);
				
				list.add(transform.getPosition());
				list.add(transform.getRotation());
				list.add(transform.getScale());
			}
			
			return list;
		};
	}
	
	private static Function<Object, Node> doCreateMapperUToGraphic() {
		return object -> {
			if(object instanceof Material) {
				return new ImageView(WritableImageCaches.get(Material.class.cast(object)));
			} else if(object instanceof Point3F) {
				return null;
			} else if(object instanceof Primitive) {
				return null;
			} else if(object instanceof Quaternion4F) {
				return null;
			} else if(object instanceof Scene) {
				return null;
			} else if(object instanceof Shape3F) {
				return new ImageView(WritableImageCaches.get(Shape3F.class.cast(object)));
			} else if(object instanceof Transform) {
				return null;
			} else if(object instanceof Vector3F) {
				return null;
			} else {
				return null;
			}
		};
	}
	
	private static Function<Object, String> doCreateMapperUToT() {
		return object -> {
			if(object instanceof Material) {
				return Material.class.cast(object).getName();
			} else if(object instanceof Point3F) {
				final Point3F position = Point3F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f]", Float.valueOf(position.x), Float.valueOf(position.y), Float.valueOf(position.z));
			} else if(object instanceof Primitive) {
				return "Primitive " + Primitive.class.cast(object).getInstanceID();
			} else if(object instanceof Quaternion4F) {
				final Quaternion4F rotation = Quaternion4F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f, %+.10f]", Float.valueOf(rotation.x), Float.valueOf(rotation.y), Float.valueOf(rotation.z), Float.valueOf(rotation.w));
			} else if(object instanceof Scene) {
				return Scene.class.cast(object).getName();
			} else if(object instanceof TriangleMesh3F) {
				final TriangleMesh3F triangleMesh = TriangleMesh3F.class.cast(object);
				
				final String string = (triangleMesh.getName() + " " + triangleMesh.getGroupName() + " " + triangleMesh.getObjectName() + " " + triangleMesh.getMaterialName()).trim().replaceAll("\\s\\s+", " ");
				
				return string;
			} else if(object instanceof Shape3F) {
				return Shape3F.class.cast(object).getName();
			} else if(object instanceof Transform) {
				return "Transform";
			} else if(object instanceof Vector3F) {
				final Vector3F scale = Vector3F.class.cast(object);
				
				return String.format("[%+.10f, %+.10f, %+.10f]", Float.valueOf(scale.x), Float.valueOf(scale.y), Float.valueOf(scale.z));
			} else {
				return "";
			}
		};
	}
	
	private static ObjectTreeView<String, Object> doCreateObjectTreeView(final Scene scene) {
		return new ObjectTreeView<>(doCreateMapperUToContextMenu(scene), doCreateMapperUToListU(), doCreateMapperUToGraphic(), doCreateMapperUToT(), scene);
	}
}