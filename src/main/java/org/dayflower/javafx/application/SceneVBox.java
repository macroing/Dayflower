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
package org.dayflower.javafx.application;

import java.util.Objects;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.javafx.scene.control.Labels;
import org.dayflower.renderer.Renderer;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.material.pbrt.GlassPBRTMaterial;
import org.dayflower.scene.material.pbrt.HairPBRTMaterial;
import org.dayflower.scene.material.pbrt.MattePBRTMaterial;
import org.dayflower.scene.material.pbrt.MetalPBRTMaterial;
import org.dayflower.scene.material.pbrt.MirrorPBRTMaterial;
import org.dayflower.scene.material.pbrt.PlasticPBRTMaterial;
import org.dayflower.scene.material.pbrt.SubstratePBRTMaterial;
import org.dayflower.scene.material.pbrt.UberPBRTMaterial;
import org.dayflower.scene.material.rayito.GlassRayitoMaterial;
import org.dayflower.scene.material.rayito.MatteRayitoMaterial;
import org.dayflower.scene.material.rayito.MetalRayitoMaterial;
import org.dayflower.scene.material.rayito.MirrorRayitoMaterial;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

final class SceneVBox extends VBox {
	private final Button buttonBuildAccelerationStructure;
	private final Button buttonClearAccelerationStructure;
	private final Button buttonPrimitiveAdd;
	private final ComboBox<String> comboBoxPrimitiveAddMaterial;
	private final ComboBox<String> comboBoxPrimitiveAddShape;
	private final Renderer renderer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneVBox(final Renderer renderer) {
		this.buttonBuildAccelerationStructure = new Button();
		this.buttonClearAccelerationStructure = new Button();
		this.buttonPrimitiveAdd = new Button();
		this.comboBoxPrimitiveAddMaterial = new ComboBox<>();
		this.comboBoxPrimitiveAddShape = new ComboBox<>();
		this.renderer = Objects.requireNonNull(renderer, "renderer == null");
		
		doConfigure();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
				case GlassPBRTMaterial.NAME:
					return new GlassPBRTMaterial();
				case HairPBRTMaterial.NAME:
					return new HairPBRTMaterial();
				case MattePBRTMaterial.NAME:
					return new MattePBRTMaterial();
				case MetalPBRTMaterial.NAME:
					return new MetalPBRTMaterial();
				case MirrorPBRTMaterial.NAME:
					return new MirrorPBRTMaterial();
				case PlasticPBRTMaterial.NAME:
					return new PlasticPBRTMaterial();
				case SubstratePBRTMaterial.NAME:
					return new SubstratePBRTMaterial();
				case UberPBRTMaterial.NAME:
					return new UberPBRTMaterial();
				case GlassRayitoMaterial.NAME:
					return new GlassRayitoMaterial();
				case MatteRayitoMaterial.NAME:
					return new MatteRayitoMaterial();
				case MetalRayitoMaterial.NAME:
					return new MetalRayitoMaterial();
				case MirrorRayitoMaterial.NAME:
					return new MirrorRayitoMaterial();
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
		final Transform transform = new Transform(doGetPointByShape(shape));
		
		final Primitive primitive = new Primitive(material, shape, transform);
		
		final
		Scene scene = doGetScene();
		scene.addPrimitive(primitive);
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
		this.comboBoxPrimitiveAddMaterial.getItems().add(GlassPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(HairPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MattePBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MetalPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MirrorPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(PlasticPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(SubstratePBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(UberPBRTMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(GlassRayitoMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MatteRayitoMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MetalRayitoMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.getItems().add(MirrorRayitoMaterial.NAME);
		this.comboBoxPrimitiveAddMaterial.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddMaterial.setValue(MattePBRTMaterial.NAME);
		
//		Configure the ComboBox for Primitive Add Shape:
		this.comboBoxPrimitiveAddShape.getItems().add(Plane3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(RectangularCuboid3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Sphere3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Torus3F.NAME);
		this.comboBoxPrimitiveAddShape.getItems().add(Triangle3F.NAME);
		this.comboBoxPrimitiveAddShape.setMaxWidth(Double.MAX_VALUE);
		this.comboBoxPrimitiveAddShape.setValue(Plane3F.NAME);
		
//		Configure the SceneBox:
		getChildren().add(Labels.createLabel("Scene Configuration", 16.0D));
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
		final
		Scene scene = doGetScene();
		scene.buildAccelerationStructure();
		
		this.renderer.renderShutdown();
		this.renderer.clear();
	}
	
	@SuppressWarnings("unused")
	private void doOnActionButtonClearAccelerationStructure(final ActionEvent actionEvent) {
		final
		Scene scene = doGetScene();
		scene.clearAccelerationStructure();
		
		this.renderer.renderShutdown();
		this.renderer.clear();
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