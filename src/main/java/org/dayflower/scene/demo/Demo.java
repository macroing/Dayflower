/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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
package org.dayflower.scene.demo;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.ConstructiveSolidGeometry3F;
import org.dayflower.geometry.shape.ConstructiveSolidGeometry3F.Operation;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.material.CheckerboardMaterial;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.FunctionMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.texture.CheckerboardTexture;

/**
 * A class that consists exclusively of methods that creates {@link Scene} instances that are used for demonstration purposes.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Demo {
	private Demo() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link CheckerboardMaterial}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code CheckerboardMaterial}
	 */
	public static Scene createCheckerboardMaterialScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial(new CheckerboardTexture());
		final Material material2 = new CheckerboardMaterial(new MetalMaterial(), new MirrorMaterial(Color3F.GRAY_0_50), AngleF.degrees(0.0F), new Vector2F(8.0F, 8.0F));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F();
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("CheckerboardMaterial");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link ClearCoatMaterial}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code ClearCoatMaterial}
	 */
	public static Scene createClearCoatMaterialScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial(new CheckerboardTexture());
		final Material material2 = new ClearCoatMaterial(new Color3F(0.5F, 0.01F, 0.01F));
		final Material material3 = new ClearCoatMaterial(new Color3F(0.5F, 0.01F, 0.01F));
		final Material material4 = new ClearCoatMaterial(new Color3F(0.5F, 0.01F, 0.01F));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Torus3F();
		final Shape3F shape3 = new Cone3F();
		final Shape3F shape4 = new Sphere3F(0.5F);
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 0.75F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
		final Transform transform3 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		final Transform transform4 = new Transform(new Point3F(0.0F, 0.5F, 0.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		final Primitive primitive3 = new Primitive(material3, shape3, transform3);
		final Primitive primitive4 = new Primitive(material4, shape4, transform4);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.addPrimitive(primitive3);
		scene.addPrimitive(primitive4);
		scene.setCamera(camera);
		scene.setName("ClearCoatMaterial");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link Cone3F}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code Cone3F}
	 */
	public static Scene createCone3FScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial();
		final Material material2 = new MatteMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F)));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Cone3F();
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("Cone3F");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link ConstructiveSolidGeometry3F}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code ConstructiveSolidGeometry3F}
	 */
	public static Scene createConstructiveSolidGeometry3FScene() {
		final Light light1 = new PerezLight();
		
		final Shape3F shapeL = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));
		final Shape3F shapeR = new Sphere3F(0.5F, new Point3F(0.0F, 0.0F, -1.0F));
		
		final Material material1 = new MatteMaterial();
		final Material material2 = FunctionMaterial.createShapeIdentity(shapeL, new MetalMaterial(), shapeR, new PlasticMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F))));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new ConstructiveSolidGeometry3F(Operation.DIFFERENCE, shapeL, shapeR);
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F), Quaternion4F.from(Matrix44F.multiply(Matrix44F.rotateX(AngleF.degrees(0.0F)), Matrix44F.rotateY(AngleF.degrees(60.0F)))));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("ConstructiveSolidGeometry3F");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that represents a version of the popular Cornell Box scene.
	 * 
	 * @return a {@code Scene} instance that represents a version of the popular Cornell Box scene
	 */
	public static Scene createCornellBoxScene() {
		final Light light1 = new PointLight(new Color3F(12.0F), new Point3F(0.0F, 4.0F, 9.0F));
		
		final Material material1 = new MatteMaterial();
		final Material material2 = new MatteMaterial();
		final Material material3 = new MatteMaterial();
		final Material material4 = new MatteMaterial();
		final Material material5 = new MatteMaterial(new Color3F(0.25F, 0.25F, 0.75F));
		final Material material6 = new MatteMaterial(new Color3F(0.75F, 0.25F, 0.25F));
		final Material material7 = new MirrorMaterial(Color3F.GRAY_0_50);
		final Material material8 = new GlassMaterial();
		
		final Shape3F shape1 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F,  1.0F), new Point3F(1.0F, 0.0F,  0.0F));
		final Shape3F shape2 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F, -1.0F), new Point3F(1.0F, 0.0F,  0.0F));
		final Shape3F shape3 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));
		final Shape3F shape4 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));
		final Shape3F shape5 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F, -1.0F));
		final Shape3F shape6 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F,  1.0F));
		final Shape3F shape7 = new RectangularCuboid3F(new Point3F(-1.0F, -1.0F, -1.0F), new Point3F(1.0F, 1.0F, 1.0F));
		final Shape3F shape8 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		
		final Transform transform1 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F));
		final Transform transform2 = new Transform(new Point3F( 0.0F, 5.0F,  0.0F));
		final Transform transform3 = new Transform(new Point3F( 0.0F, 0.0F,  0.0F));
		final Transform transform4 = new Transform(new Point3F( 0.0F, 0.0F, 10.0F));
		final Transform transform5 = new Transform(new Point3F( 3.0F, 0.0F,  0.0F));
		final Transform transform6 = new Transform(new Point3F(-3.0F, 0.0F,  0.0F));
		final Transform transform7 = new Transform(new Point3F(-1.5F, 1.0F,  8.0F));
		final Transform transform8 = new Transform(new Point3F( 1.5F, 1.0F,  7.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		final Primitive primitive3 = new Primitive(material3, shape3, transform3);
		final Primitive primitive4 = new Primitive(material4, shape4, transform4);
		final Primitive primitive5 = new Primitive(material5, shape5, transform5);
		final Primitive primitive6 = new Primitive(material6, shape6, transform6);
		final Primitive primitive7 = new Primitive(material7, shape7, transform7);
		final Primitive primitive8 = new Primitive(material8, shape8, transform8);
		
		final
		Camera camera = new Camera(new Point3F(0.0F, 2.5F, 1.0F));
		camera.setResolution(1024.0F, 768.0F);
		camera.setFieldOfViewY(AngleF.degrees(58.0F));
		camera.setFieldOfViewX();
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.addPrimitive(primitive3);
		scene.addPrimitive(primitive4);
		scene.addPrimitive(primitive5);
		scene.addPrimitive(primitive6);
		scene.addPrimitive(primitive7);
		scene.addPrimitive(primitive8);
		scene.setCamera(camera);
		scene.setName("CornellBox");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link Cylinder3F}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code Cylinder3F}
	 */
	public static Scene createCylinder3FScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial();
		final Material material2 = new MatteMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F)));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Cylinder3F();
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 0.0F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("Cylinder3F");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link Disk3F}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code Disk3F}
	 */
	public static Scene createDisk3FScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial();
		final Material material2 = new MatteMaterial(new CheckerboardTexture(Color3F.GRAY_0_50, Color3F.WHITE, AngleF.degrees(0.0F), new Vector2F(4.0F, 4.0F)));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Disk3F();
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("Disk3F");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link DisneyMaterial}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code DisneyMaterial}
	 */
	public static Scene createDisneyMaterialScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial(new CheckerboardTexture());
		final Material material2 = new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);
		final Material material3 = new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);
		final Material material4 = new DisneyMaterial(new Color3F(255, 127, 80), Color3F.BLACK, Color3F.BLACK, 0.4F, 1.75F, 1.0F, 1.0F, 1.2F, 0.0F, 0.39F, 0.475F, 0.1F, 0.5F, 0.5F, 0.0F);
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Torus3F();
		final Shape3F shape3 = new Cone3F();
		final Shape3F shape4 = new Sphere3F(0.5F);
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 0.75F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
		final Transform transform3 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		final Transform transform4 = new Transform(new Point3F(0.0F, 0.5F, 0.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		final Primitive primitive3 = new Primitive(material3, shape3, transform3);
		final Primitive primitive4 = new Primitive(material4, shape4, transform4);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.addPrimitive(primitive3);
		scene.addPrimitive(primitive4);
		scene.setCamera(camera);
		scene.setName("DisneyMaterial");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link GlassMaterial}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code GlassMaterial}
	 */
	public static Scene createGlassMaterialScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial(new CheckerboardTexture());
		final Material material2 = new GlassMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F();
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 1.0F, 0.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("GlassMaterial");
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance that demonstrates {@link GlossyMaterial}.
	 * 
	 * @return a {@code Scene} instance that demonstrates {@code GlossyMaterial}
	 */
	public static Scene createGlossyMaterialScene() {
		final Light light1 = new PerezLight();
		
		final Material material1 = new MatteMaterial(new CheckerboardTexture());
		final Material material2 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
		final Material material3 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
		final Material material4 = new GlossyMaterial(new Color3F(0.01F, 0.5F, 0.01F), Color3F.BLACK, 0.1F);
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Torus3F();
		final Shape3F shape3 = new Cone3F();
		final Shape3F shape4 = new Sphere3F(0.5F);
		
		final Transform transform1 = new Transform(new Point3F(0.0F, 0.0F, 0.0F));
		final Transform transform2 = new Transform(new Point3F(0.0F, 0.75F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(90.0F))));
		final Transform transform3 = new Transform(new Point3F(0.0F, 0.5F, 0.0F), Quaternion4F.from(Matrix44F.rotateX(AngleF.degrees(270.0F))));
		final Transform transform4 = new Transform(new Point3F(0.0F, 0.5F, 0.0F));
		
		final Primitive primitive1 = new Primitive(material1, shape1, transform1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		final Primitive primitive3 = new Primitive(material3, shape3, transform3);
		final Primitive primitive4 = new Primitive(material4, shape4, transform4);
		
		final Camera camera = new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F));
		
		final
		Scene scene = new Scene();
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.addPrimitive(primitive3);
		scene.addPrimitive(primitive4);
		scene.setCamera(camera);
		scene.setName("GlossyMaterial");
		
		return scene;
	}
}