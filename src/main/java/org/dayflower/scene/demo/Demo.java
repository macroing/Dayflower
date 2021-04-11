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
package org.dayflower.scene.demo;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.material.CheckerboardMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
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
		Scene scene = new Scene(camera, "CheckerboardMaterial");
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		
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
		Scene scene = new Scene(camera, "CornellBox");
		scene.addLight(light1);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.addPrimitive(primitive3);
		scene.addPrimitive(primitive4);
		scene.addPrimitive(primitive5);
		scene.addPrimitive(primitive6);
		scene.addPrimitive(primitive7);
		scene.addPrimitive(primitive8);
		
		return scene;
	}
}