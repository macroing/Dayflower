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
import java.util.concurrent.ExecutorService;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.renderer.Renderer;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.pbrt.GlassMaterial;
import org.dayflower.scene.pbrt.HairMaterial;
import org.dayflower.scene.pbrt.MatteMaterial;
import org.dayflower.scene.pbrt.MetalMaterial;
import org.dayflower.scene.pbrt.MirrorMaterial;
import org.dayflower.scene.pbrt.PlasticMaterial;
import org.dayflower.scene.pbrt.SubstrateMaterial;
import org.dayflower.scene.pbrt.UberMaterial;
import org.dayflower.scene.rayito.AshikhminShirleyMaterial;
import org.dayflower.scene.rayito.LambertianMaterial;
import org.dayflower.scene.rayito.ReflectionMaterial;
import org.dayflower.scene.rayito.RefractionMaterial;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

final class SceneBox extends VBox {
	private final Button buttonBuildAccelerationStructure;
	private final Button buttonClearAccelerationStructure;
	private final Button buttonPrimitiveAdd;
	private final ComboBox<String> comboBoxPrimitiveAddMaterial;
	private final ComboBox<String> comboBoxPrimitiveAddShape;
	private final ExecutorService executorService;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneBox(final Renderer renderer, final ExecutorService executorService) {
		this.buttonBuildAccelerationStructure = new Button();
		this.buttonClearAccelerationStructure = new Button();
		this.buttonPrimitiveAdd = new Button();
		this.comboBoxPrimitiveAddMaterial = new ComboBox<>();
		this.comboBoxPrimitiveAddShape = new ComboBox<>();
		this.executorService = Objects.requireNonNull(executorService, "executorService == null");
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ExecutorService getExecutorService() {
		return this.executorService;
	}
	
	public Renderer getRenderer() {
		return this.renderer;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Camera doGetCamera() {
		return doGetScene().getCamera();
	}
	
	private Material doCreateMaterial() {
		final String selectedItem = this.comboBoxPrimitiveAddMaterial.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null) {
			switch(selectedItem) {
				case GlassMaterial.NAME:
					return new GlassMaterial();
				case HairMaterial.NAME:
					return new HairMaterial();
				case MatteMaterial.NAME:
					return new MatteMaterial();
				case MetalMaterial.NAME:
					return new MetalMaterial();
				case MirrorMaterial.NAME:
					return new MirrorMaterial();
				case PlasticMaterial.NAME:
					return new PlasticMaterial();
				case SubstrateMaterial.NAME:
					return new SubstrateMaterial();
				case UberMaterial.NAME:
					return new UberMaterial();
				case RefractionMaterial.NAME:
					return new RefractionMaterial();
				case LambertianMaterial.NAME:
					return new LambertianMaterial();
				case AshikhminShirleyMaterial.NAME:
					return new AshikhminShirleyMaterial();
				case ReflectionMaterial.NAME:
					return new ReflectionMaterial();
				default:
					return null;
			}
		}
		
		return null;
	}
	
	private Point3F doGetPointByShape(final Shape3F shape) {
		if(shape instanceof Plane3F) {
			return doGetCamera().getPointBelowEye(1.0F);
		} else if(shape instanceof RectangularCuboid3F) {
			return doGetCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Sphere3F) {
			return doGetCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Torus3F) {
			return doGetCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Triangle3F) {
			return doGetCamera().getPointInfrontOfEye(7.5F);
		} else {
			return new Point3F();
		}
	}
	
	private Scene doGetScene() {
		return getRenderer().getRendererConfiguration().getScene();
	}
	
	private Shape3F doCreateShape() {
		final String selectedItem = this.comboBoxPrimitiveAddShape.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null) {
			switch(selectedItem) {
				case Plane3F.NAME:
					return new Plane3F();
				case RectangularCuboid3F.NAME:
					return new RectangularCuboid3F();
				case Sphere3F.NAME:
					return new Sphere3F();
				case Torus3F.NAME:
					return new Torus3F();
				case Triangle3F.NAME:
					return new Triangle3F();
				default:
					return null;
			}
		}
		
		return null;
	}
	
	private void doAddPrimitiveByMaterialAndShape(final Material material, final Shape3F shape) {
		getExecutorService().execute(() -> {
			final Transform transform = new Transform(doGetPointByShape(shape));
			
			final Primitive primitive = new Primitive(material, shape, transform);
			
			final
			Scene scene = doGetScene();
			scene.addPrimitive(primitive);
			
			final
			Renderer renderer = getRenderer();
			renderer.clear();
		});
	}
	
	private void doConfigure() {
//		Configure the Button for Build Acceleration Structure:
		this.buttonBuildAccelerationStructure.setMaxWidth(Double.MAX_VALUE);
		this.buttonBuildAccelerationStructure.setOnAction(this::doOnActionButtonBuildAccelerationStructure);
		this.buttonBuildAccelerationStructure.setText("Build Acceleration Structure");
		
//		Configure the Button for Clear Acceleration Structure:
		this.buttonClearAccelerationStructure.setMaxWidth(Double.MAX_VALUE);
		this.buttonClearAccelerationStructure.setOnAction(this::doOnActionButtonClearAccelerationStructure);
		this.buttonClearAccelerationStructure.setText("Clear Acceleration Structure");
		
//		Configure the Button for Primitive Add:
		this.buttonPrimitiveAdd.setMaxWidth(Double.MAX_VALUE);
		this.buttonPrimitiveAdd.setOnAction(this::doOnActionButtonPrimitiveAdd);
		this.buttonPrimitiveAdd.setText("Add Primitive");
		
//		Configure the ComboBox for Primitive Add Material:
		this.comboBoxPrimitiveAddMaterial.getItems().add(GlassMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(HairMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MatteMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MetalMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MirrorMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(PlasticMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(SubstrateMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(UberMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(RefractionMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(LambertianMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(AshikhminShirleyMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(ReflectionMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddMaterial.setValue(MatteMaterial.NAME);
		
//		Configure the ComboBox for Primitive Add Shape:
		this.comboBoxPrimitiveAddShape.getItems().add(Plane3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(RectangularCuboid3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Sphere3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Torus3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Triangle3F.NAME);
		this.comboBoxPrimitiveAddShape.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddShape.setValue(Plane3F.NAME);
		
//		Configure the SceneBox:
		getChildren().add(JavaFX.createLabel("Scene Configuration", 16.0D));
		getChildren().add(this.comboBoxPrimitiveAddMaterial);
		getChildren().add(this.comboBoxPrimitiveAddShape);
		getChildren().add(this.buttonPrimitiveAdd);
		getChildren().add(new Separator());
		getChildren().add(this.buttonBuildAccelerationStructure);
		getChildren().add(this.buttonClearAccelerationStructure);
		setAlignment(Pos.CENTER);
		setFillWidth(true);
		setSpacing(10.0D);
	}
	
	@SuppressWarnings("unused")
	private void doOnActionButtonBuildAccelerationStructure(final ActionEvent actionEvent) {
		getExecutorService().execute(() -> {
			final
			Scene scene = doGetScene();
			scene.buildAccelerationStructure();
		});
	}
	
	@SuppressWarnings("unused")
	private void doOnActionButtonClearAccelerationStructure(final ActionEvent actionEvent) {
		getExecutorService().execute(() -> {
			final
			Scene scene = doGetScene();
			scene.clearAccelerationStructure();
		});
	}
	
	@SuppressWarnings("unused")
	private void doOnActionButtonPrimitiveAdd(final ActionEvent actionEvent) {
		final Material material = doCreateMaterial();
		
		final Shape3F shape = doCreateShape();
		
		if(material != null && shape != null) {
			doAddPrimitiveByMaterialAndShape(material, shape);
		}
	}
}