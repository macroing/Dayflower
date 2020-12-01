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
	private static final String MATERIAL_P_B_R_T_GLASS = "PBRT Glass";
	private static final String MATERIAL_P_B_R_T_HAIR = "PBRT Hair";
	private static final String MATERIAL_P_B_R_T_MATTE = "PBRT Matte";
	private static final String MATERIAL_P_B_R_T_METAL = "PBRT Metal";
	private static final String MATERIAL_P_B_R_T_MIRROR = "PBRT Mirror";
	private static final String MATERIAL_P_B_R_T_PLASTIC = "PBRT Plastic";
	private static final String MATERIAL_P_B_R_T_SUBSTRATE = "PBRT Substrate";
	private static final String MATERIAL_P_B_R_T_UBER = "PBRT Uber";
	private static final String MATERIAL_RAYITO_GLASS = "Rayito Glass";
	private static final String MATERIAL_RAYITO_MATTE = "Rayito Matte";
	private static final String MATERIAL_RAYITO_METAL = "Rayito Metal";
	private static final String MATERIAL_RAYITO_MIRROR = "Rayito Mirror";
	private static final String SHAPE_PLANE = "Plane";
	private static final String SHAPE_RECTANGULAR_CUBOID = "Rectangular Cuboid";
	private static final String SHAPE_SPHERE = "Sphere";
	private static final String SHAPE_TORUS = "Torus";
	private static final String SHAPE_TRIANGLE = "Triangle";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
				case MATERIAL_P_B_R_T_GLASS:
					return new GlassMaterial();
				case MATERIAL_P_B_R_T_HAIR:
					return new HairMaterial();
				case MATERIAL_P_B_R_T_MATTE:
					return new MatteMaterial();
				case MATERIAL_P_B_R_T_METAL:
					return new MetalMaterial();
				case MATERIAL_P_B_R_T_MIRROR:
					return new MirrorMaterial();
				case MATERIAL_P_B_R_T_PLASTIC:
					return new PlasticMaterial();
				case MATERIAL_P_B_R_T_SUBSTRATE:
					return new SubstrateMaterial();
				case MATERIAL_P_B_R_T_UBER:
					return new UberMaterial();
				case MATERIAL_RAYITO_GLASS:
					return new RefractionMaterial();
				case MATERIAL_RAYITO_MATTE:
					return new LambertianMaterial();
				case MATERIAL_RAYITO_METAL:
					return new AshikhminShirleyMaterial();
				case MATERIAL_RAYITO_MIRROR:
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
				case SHAPE_PLANE:
					return new Plane3F();
				case SHAPE_RECTANGULAR_CUBOID:
					return new RectangularCuboid3F();
				case SHAPE_SPHERE:
					return new Sphere3F();
				case SHAPE_TORUS:
					return new Torus3F();
				case SHAPE_TRIANGLE:
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
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_GLASS);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_HAIR);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_MATTE);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_METAL);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_MIRROR);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_PLASTIC);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_SUBSTRATE);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_P_B_R_T_UBER);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_RAYITO_GLASS);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_RAYITO_MATTE);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_RAYITO_METAL);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MATERIAL_RAYITO_MIRROR);
		this.comboBoxPrimitiveAddMaterial.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddMaterial.setValue(MATERIAL_P_B_R_T_MATTE);
		
//		Configure the ComboBox for Primitive Add Shape:
		this.comboBoxPrimitiveAddShape.getItems().add(SHAPE_PLANE);
		this.comboBoxPrimitiveAddShape.getItems().add(SHAPE_RECTANGULAR_CUBOID);
		this.comboBoxPrimitiveAddShape.getItems().add(SHAPE_SPHERE);
		this.comboBoxPrimitiveAddShape.getItems().add(SHAPE_TORUS);
		this.comboBoxPrimitiveAddShape.getItems().add(SHAPE_TRIANGLE);
		this.comboBoxPrimitiveAddShape.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddShape.setValue(SHAPE_PLANE);
		
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