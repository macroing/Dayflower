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

import java.util.Arrays;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.javafx.scene.layout.CenteredVBox;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.ProgressiveImageOrderRenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.HairMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.material.UberMaterial;

import javafx.scene.control.ComboBox;

final class CenteredVBoxes {
	private CenteredVBoxes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CenteredVBox createCenteredVBoxForCombinedProgressiveImageOrderRenderer(final CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer) {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Renderer Configuration", 16.0D);
		
		final ComboBox<RenderingAlgorithm> comboBox = centeredVBox.addComboBox(Arrays.asList(RenderingAlgorithm.AMBIENT_OCCLUSION, RenderingAlgorithm.PATH_TRACING, RenderingAlgorithm.RAY_CASTING, RenderingAlgorithm.RAY_TRACING), combinedProgressiveImageOrderRenderer.getRenderingAlgorithm());
		
		centeredVBox.addButton("Update Renderer", actionEvent -> {
			final RenderingAlgorithm renderingAlgorithm = comboBox.getValue();
			
			if(renderingAlgorithm != null) {
				combinedProgressiveImageOrderRenderer.setRenderingAlgorithm(renderingAlgorithm);
				combinedProgressiveImageOrderRenderer.renderShutdown();
				combinedProgressiveImageOrderRenderer.clear();
			}
		});
		
		return centeredVBox;
	}
	
	public static CenteredVBox createCenteredVBoxForScene(final ProgressiveImageOrderRenderer progressiveImageOrderRenderer) {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Scene Configuration", 16.0D);
		
		final ComboBox<String> comboBoxMaterial = centeredVBox.addComboBox(Arrays.asList(ClearCoatMaterial.NAME, DisneyMaterial.NAME, GlassMaterial.NAME, GlossyMaterial.NAME, HairMaterial.NAME, MatteMaterial.NAME, MetalMaterial.NAME, MirrorMaterial.NAME, PlasticMaterial.NAME, SubstrateMaterial.NAME, UberMaterial.NAME), MatteMaterial.NAME);
		final ComboBox<String> comboBoxShape = centeredVBox.addComboBox(Arrays.asList(Cone3F.NAME, Cylinder3F.NAME, Disk3F.NAME, Plane3F.NAME, RectangularCuboid3F.NAME, Sphere3F.NAME, Torus3F.NAME, Triangle3F.NAME), Plane3F.NAME);
		
		centeredVBox.addButton("Add Primitive", actionEvent -> {
			final Material material = doCreateMaterial(comboBoxMaterial);
			
			final Shape3F shape = doCreateShape(comboBoxShape);
			
			if(material != null && shape != null) {
				final
				Scene scene = progressiveImageOrderRenderer.getScene();
				scene.addPrimitive(new Primitive(material, shape, new Transform(doGetPointByShape(progressiveImageOrderRenderer, shape))));
			}
		});
		centeredVBox.addSeparator();
		centeredVBox.addButton("Build Acceleration Structure", actionEvent -> {
			progressiveImageOrderRenderer.getScene().buildAccelerationStructure();
			progressiveImageOrderRenderer.renderShutdown();
			progressiveImageOrderRenderer.clear();
		});
		centeredVBox.addButton("Clear Acceleration Structure", actionEvent -> {
			progressiveImageOrderRenderer.getScene().clearAccelerationStructure();
			progressiveImageOrderRenderer.renderShutdown();
			progressiveImageOrderRenderer.clear();
		});
		
		return centeredVBox;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Material doCreateMaterial(final ComboBox<String> comboBoxMaterial) {
		final String selectedItem = comboBoxMaterial.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null) {
			switch(selectedItem) {
				case ClearCoatMaterial.NAME:
					return new ClearCoatMaterial();
				case DisneyMaterial.NAME:
					return new DisneyMaterial();
				case GlassMaterial.NAME:
					return new GlassMaterial();
				case GlossyMaterial.NAME:
					return new GlossyMaterial();
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
				default:
					return null;
			}
		}
		
		return null;
	}
	
	private static Point3F doGetPointByShape(final Renderer renderer, final Shape3F shape) {
		if(shape instanceof Cone3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Cylinder3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Disk3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Plane3F) {
			return renderer.getScene().getCamera().getPointBelowEye(1.0F);
		} else if(shape instanceof RectangularCuboid3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Sphere3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Torus3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Triangle3F) {
			return renderer.getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else {
			return new Point3F();
		}
	}
	
	private static Shape3F doCreateShape(final ComboBox<String> comboBoxShape) {
		final String selectedItem = comboBoxShape.getSelectionModel().getSelectedItem();
		
		if(selectedItem != null) {
			switch(selectedItem) {
				case Cone3F.NAME:
					return new Cone3F();
				case Cylinder3F.NAME:
					return new Cylinder3F();
				case Disk3F.NAME:
					return new Disk3F();
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
}