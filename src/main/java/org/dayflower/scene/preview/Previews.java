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
package org.dayflower.scene.preview;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.image.Color3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.scene.material.pbrt.MattePBRTMaterial;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;

//TODO: Add Javadocs!
public final class Previews {
	private Previews() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Scene createMaterialPreviewScene(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		if(material instanceof PBRTMaterial) {
			return doCreateMaterialPreviewScenePBRTMaterial(PBRTMaterial.class.cast(material));
		} else if(material instanceof RayitoMaterial) {
			return doCreateMaterialPreviewSceneRayitoMaterial(RayitoMaterial.class.cast(material));
		} else {
			return doCreateMaterialPreviewSceneMaterial(material);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Scene doCreateMaterialPreviewSceneMaterial(final Material material) {
		final
		Camera camera = new Camera();
		camera.setResolution(32.0F, 32.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Material material0 = material;
		
		final Shape3F shape0 = new Sphere3F();
		
		final Transform transform0 = new Transform(camera.getPointInfrontOfEye(5.0F));
		
		final Primitive primitive0 = new Primitive(material0, shape0, transform0);
		
		final
		Scene scene = new Scene();
		scene.addLight(new PerezLight());
		scene.addPrimitive(primitive0);
		scene.setCamera(camera);
		
		return scene;
	}
	
	private static Scene doCreateMaterialPreviewScenePBRTMaterial(final PBRTMaterial pBRTMaterial) {
		final
		Camera camera = new Camera();
		camera.setResolution(32.0F, 32.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Material material0 = pBRTMaterial;
		final Material material1 = new MattePBRTMaterial();
		
		final Shape3F shape0 = new Sphere3F();
		final Shape3F shape1 = new Sphere3F(10);
		
		final Transform transform0 = new Transform(camera.getPointInfrontOfEye(5.0F));
		final Transform transform1 = new Transform(camera.getEye());
		
		final Primitive primitive0 = new Primitive(material0, shape0, transform0);
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		
		final
		Scene scene = new Scene();
		scene.addLight(new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(10.0F), new Color3F(100.0F), Matrix44F.translate(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 1.0F, 20.0F)));
		scene.addPrimitive(primitive0);
		scene.addPrimitive(primitive1);
		scene.setCamera(camera);
		
		return scene;
	}
	
	private static Scene doCreateMaterialPreviewSceneRayitoMaterial(final RayitoMaterial rayitoMaterial) {
		final
		Camera camera = new Camera();
		camera.setResolution(32.0F, 32.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Material material0 = rayitoMaterial;
		
		final Shape3F shape0 = new Sphere3F();
		
		final Transform transform0 = new Transform(camera.getPointInfrontOfEye(5.0F));
		
		final Primitive primitive0 = new Primitive(material0, shape0, transform0);
		
		final
		Scene scene = new Scene();
		scene.addLight(new PerezLight());
		scene.addPrimitive(primitive0);
		scene.setCamera(camera);
		
		return scene;
	}
}