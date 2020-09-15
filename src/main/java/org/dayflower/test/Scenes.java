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
package org.dayflower.test;

import java.io.File;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Plane3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.RectangularCuboid3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.ProceduralTerrain3F;
import org.dayflower.geometry.Rectangle2I;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.Torus3F;
import org.dayflower.geometry.Triangle3F;
import org.dayflower.geometry.Triangle3F.Vertex3F;
import org.dayflower.geometry.TriangleMesh3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Texture;
import org.dayflower.scene.background.PerezBackground;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.material.AshikhminShirleyMaterial;
import org.dayflower.scene.material.LambertianMaterial;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.ImageTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.UVTexture;

/**
 * A class that consists exclusively of static methods that returns {@link Scene} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scenes {
	private Scenes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newDefaultScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new AshikhminShirleyMaterial();
		final Material material3 = new AshikhminShirleyMaterial(0.02F);
		final Material material4 = new AshikhminShirleyMaterial(0.02F);
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		final Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F));
		final Shape3F shape4 = TriangleMesh3F.readWavefrontObject(new File("./resources/smoothMonkey2.obj"), true).get(0);
		
		final Texture texture11 = new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F));
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new BullseyeTexture(new Color3F(1.0F, 0.1F, 0.1F), new Color3F(0.5F, 0.1F, 0.1F), new Point3F(0.0F, 10.0F, 0.0F), 2.0F);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		final Texture texture31 = new CheckerboardTexture(new Color3F(0.2F, 0.4F, 0.2F), new Color3F(0.6F, 0.8F, 0.6F), AngleF.degrees(90.0F), new Vector2F(1.5F, 1.5F));
		final Texture texture32 = new ConstantTexture();
		final Texture texture33 = new ConstantTexture();
		final Texture texture41 = new ConstantTexture(new Color3F(0.5F));
		final Texture texture42 = new ConstantTexture();
		final Texture texture43 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		final Matrix44F matrix3 = Matrix44F.translate(-2.0F, 1.0F, 5.0F);
		final Matrix44F matrix4 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(1.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(190.0F))), Matrix44F.scale(1.0F));
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addLight(new PointLight(new Point3F(0.0F, 2.0F, 5.0F), new Color3F(1.0F, 1.0F, 1.0F)));
		scene.addLight(new PointLight(new Point3F(0.0F, 1.0F, 0.0F), new Color3F(1.0F, 1.0F, 1.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		scene.addPrimitive(new Primitive(material3, shape3, texture31, texture32, texture33, matrix3));
		scene.addPrimitive(new Primitive(material4, shape4, texture41, texture42, texture43, matrix4));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link Plane3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code Plane3F}
	 */
	public static Scene newShowcaseShape3FPlane3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = new Plane3F();
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.identity();
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link ProceduralTerrain3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code ProceduralTerrain3F}
	 */
	public static Scene newShowcaseShape3FProceduralTerrain3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = ProceduralTerrain3F.sin();
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.scale(1.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(10.0F, 4.0F, 10.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link RectangularCuboid3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code RectangularCuboid3F}
	 */
	public static Scene newShowcaseShape3FRectangularCuboid3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = new RectangularCuboid3F(new Point3F(-5.0F), new Point3F(5.0F));
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link Sphere3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code Sphere3F}
	 */
	public static Scene newShowcaseShape3FSphere3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = new Sphere3F(10.0F);
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link Torus3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code Torus3F}
	 */
	public static Scene newShowcaseShape3FTorus3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = new Torus3F();
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 5.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link Triangle3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code Triangle3F}
	 */
	public static Scene newShowcaseShape3FTriangle3FScene() {
		final Material material = new LambertianMaterial();
		
		final Point3F positionA = new Point3F(+0.0F, +5.0F, 0.0F);
		final Point3F positionB = new Point3F(+5.0F, -5.0F, 0.0F);
		final Point3F positionC = new Point3F(-5.0F, -5.0F, 0.0F);
		
		final Vector3F surfaceNormal = Vector3F.normalNormalized(positionA, positionB, positionC);
		
		final Vertex3F a = new Vertex3F(new Point2F(0.5F, 0.0F), positionA, surfaceNormal, new Vector3F());
		final Vertex3F b = new Vertex3F(new Point2F(1.0F, 1.0F), positionB, surfaceNormal, new Vector3F());
		final Vertex3F c = new Vertex3F(new Point2F(0.0F, 1.0F), positionC, surfaceNormal, new Vector3F());
		
		final Shape3F shape = new Triangle3F(a, b, c);
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link TriangleMesh3F}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code TriangleMesh3F}
	 */
	public static Scene newShowcaseShape3FTriangleMesh3FScene() {
		final Material material = new LambertianMaterial();
		
		final Shape3F shape = TriangleMesh3F.readWavefrontObject(new File("./resources/smoothMonkey2.obj"), true).get(0);
		
		final Texture texture1 = new ConstantTexture(Color3F.GRAY);
		final Texture texture2 = new ConstantTexture();
		final Texture texture3 = new ConstantTexture();
		
		final Matrix44F matrix = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(1.0F));
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material, shape, texture1, texture2, texture3, matrix));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link BlendTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code BlendTexture}
	 */
	public static Scene newShowcaseTextureBlendTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new BlendTexture(new CheckerboardTexture(new Color3F(1.0F, 0.1F, 0.1F), new Color3F(0.1F, 1.0F, 0.1F), AngleF.degrees(90.0F), new Vector2F(5.0F, 5.0F)), new BullseyeTexture(new Color3F(0.1F, 1.0F, 0.1F), new Color3F(0.1F, 0.1F, 1.0F), new Point3F(0.0F, 10.0F, 0.0F), 2.0F));
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link BullseyeTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code BullseyeTexture}
	 */
	public static Scene newShowcaseTextureBullseyeTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new BullseyeTexture(new Color3F(0.1F, 1.0F, 0.1F), new Color3F(0.1F, 0.1F, 1.0F), new Point3F(0.0F, 10.0F, 0.0F), 2.0F);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link CheckerboardTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code CheckerboardTexture}
	 */
	public static Scene newShowcaseTextureCheckerboardTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new CheckerboardTexture(new Color3F(1.0F, 0.1F, 0.1F), new Color3F(0.1F, 1.0F, 0.1F), AngleF.degrees(90.0F), new Vector2F(5.0F, 5.0F));
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link ImageTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code ImageTexture}
	 */
	public static Scene newShowcaseTextureImageTextureScene() {
		final
		Image image = new Image(800, 800, Color3F.WHITE);
		image.fillRectangle(new Rectangle2I(new Point2I(300, 300), new Point2I(500, 500)), pixel -> Color3F.simplexFractionalBrownianMotion(new Color3F(0.75F, 0.5F, 0.75F), new Point2F(pixel.getX(), pixel.getY()), new Point2F(), new Point2F(800.0F, 800.0F)));
		
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ImageTexture(image, AngleF.degrees(90.0F));
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 20.0F), Matrix44F.rotateY(AngleF.degrees(90.0F)));//Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link SimplexFractionalBrownianMotionTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code SimplexFractionalBrownianMotionTexture}
	 */
	public static Scene newShowcaseTextureSimplexFractionalBrownianMotionTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new SimplexFractionalBrownianMotionTexture();
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link SurfaceNormalTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code SurfaceNormalTexture}
	 */
	public static Scene newShowcaseTextureSurfaceNormalTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new SurfaceNormalTexture();
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link UVTexture}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code UVTexture}
	 */
	public static Scene newShowcaseTextureUVTextureScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new UVTexture();
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newZealotScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new AshikhminShirleyMaterial(0.02F);
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = TriangleMesh3F.readWavefrontObject(new File("./resources/Zealot.obj"), true).get(0);
		
		final Texture texture11 = new CheckerboardTexture(new Color3F(0.1F), new Color3F(1.0F), AngleF.degrees(90.0F), new Vector2F(0.5F, 0.5F));
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = ImageTexture.load(new File("./resources/Zealot_albedo.png"));
		final Texture texture22 = ImageTexture.load(new File("./resources/Zealot_emissive.png"));
		final Texture texture23 = ImageTexture.load(new File("./resources/Zealot_normal.png"));
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.05F));
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
}