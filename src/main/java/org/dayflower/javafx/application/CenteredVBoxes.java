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

import java.util.Arrays;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.javafx.scene.layout.CenteredVBox;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RenderingAlgorithm;
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
import org.dayflower.scene.material.smallpt.ClearCoatSmallPTMaterial;
import org.dayflower.scene.material.smallpt.GlassSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MatteSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MetalSmallPTMaterial;
import org.dayflower.scene.material.smallpt.MirrorSmallPTMaterial;

import javafx.scene.control.ComboBox;

final class CenteredVBoxes {
	private CenteredVBoxes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CenteredVBox createCenteredVBoxForRenderer(final Renderer renderer) {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Renderer Configuration", 16.0D);
		
		final ComboBox<RenderingAlgorithm> comboBox = centeredVBox.addComboBox(Arrays.asList(RenderingAlgorithm.AMBIENT_OCCLUSION, RenderingAlgorithm.PATH_TRACING, RenderingAlgorithm.PATH_TRACING_P_B_R_T, RenderingAlgorithm.PATH_TRACING_RAYITO, RenderingAlgorithm.PATH_TRACING_SMALL_P_T_ITERATIVE, RenderingAlgorithm.PATH_TRACING_SMALL_P_T_RECURSIVE, RenderingAlgorithm.RAY_CASTING), renderer.getRendererConfiguration().getRenderingAlgorithm());
		
		centeredVBox.addButton("Update Renderer", actionEvent -> {
			final RenderingAlgorithm renderingAlgorithm = comboBox.getValue();
			
			if(renderingAlgorithm != null) {
				renderer.getRendererConfiguration().setRenderingAlgorithm(renderingAlgorithm);
				renderer.renderShutdown();
				renderer.clear();
			}
		});
		
		return centeredVBox;
	}
	
	public static CenteredVBox createCenteredVBoxForScene(final Renderer renderer) {
		final
		CenteredVBox centeredVBox = new CenteredVBox();
		centeredVBox.addLabel("Scene Configuration", 16.0D);
		
		final ComboBox<String> comboBoxMaterial = centeredVBox.addComboBox(Arrays.asList(GlassPBRTMaterial.NAME, HairPBRTMaterial.NAME, MattePBRTMaterial.NAME, MetalPBRTMaterial.NAME, MirrorPBRTMaterial.NAME, PlasticPBRTMaterial.NAME, SubstratePBRTMaterial.NAME, UberPBRTMaterial.NAME, GlassRayitoMaterial.NAME, MatteRayitoMaterial.NAME, MetalRayitoMaterial.NAME, MirrorRayitoMaterial.NAME, ClearCoatSmallPTMaterial.NAME, GlassSmallPTMaterial.NAME, MatteSmallPTMaterial.NAME, MetalSmallPTMaterial.NAME, MirrorSmallPTMaterial.NAME), MattePBRTMaterial.NAME);
		final ComboBox<String> comboBoxShape = centeredVBox.addComboBox(Arrays.asList(Plane3F.NAME, RectangularCuboid3F.NAME, Sphere3F.NAME, Torus3F.NAME, Triangle3F.NAME), Plane3F.NAME);
		
		centeredVBox.addButton("Add Primitive", actionEvent -> {
			final Material material = doCreateMaterial(comboBoxMaterial);
			
			final Shape3F shape = doCreateShape(comboBoxShape);
			
			if(material != null && shape != null) {
				final
				Scene scene = renderer.getRendererConfiguration().getScene();
				scene.addPrimitive(new Primitive(material, shape, new Transform(doGetPointByShape(renderer, shape))));
			}
		});
		centeredVBox.addSeparator();
		centeredVBox.addButton("Build Acceleration Structure", actionEvent -> {
			renderer.getRendererConfiguration().getScene().buildAccelerationStructure();
			renderer.renderShutdown();
			renderer.clear();
		});
		centeredVBox.addButton("Clear Acceleration Structure", actionEvent -> {
			renderer.getRendererConfiguration().getScene().clearAccelerationStructure();
			renderer.renderShutdown();
			renderer.clear();
		});
		
		return centeredVBox;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Material doCreateMaterial(final ComboBox<String> comboBoxMaterial) {
		final String selectedItem = comboBoxMaterial.getSelectionModel().getSelectedItem();
		
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
				case ClearCoatSmallPTMaterial.NAME:
					return new ClearCoatSmallPTMaterial();
				case GlassSmallPTMaterial.NAME:
					return new GlassSmallPTMaterial();
				case MatteSmallPTMaterial.NAME:
					return new MatteSmallPTMaterial();
				case MetalSmallPTMaterial.NAME:
					return new MetalSmallPTMaterial();
				case MirrorSmallPTMaterial.NAME:
					return new MirrorSmallPTMaterial();
				default:
					return null;
			}
		}
		
		return null;
	}
	
	private static Point3F doGetPointByShape(final Renderer renderer, final Shape3F shape) {
		if(shape instanceof Plane3F) {
			return renderer.getRendererConfiguration().getScene().getCamera().getPointBelowEye(1.0F);
		} else if(shape instanceof RectangularCuboid3F) {
			return renderer.getRendererConfiguration().getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Sphere3F) {
			return renderer.getRendererConfiguration().getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Torus3F) {
			return renderer.getRendererConfiguration().getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else if(shape instanceof Triangle3F) {
			return renderer.getRendererConfiguration().getScene().getCamera().getPointInfrontOfEye(7.5F);
		} else {
			return new Point3F();
		}
	}
	
	private static Shape3F doCreateShape(final ComboBox<String> comboBoxShape) {
		final String selectedItem = comboBoxShape.getSelectionModel().getSelectedItem();
		
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
}