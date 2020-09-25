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
import java.util.List;

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
import org.dayflower.scene.background.ConstantBackground;
import org.dayflower.scene.background.ImageBackground;
import org.dayflower.scene.background.PerezBackground;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.AshikhminShirleyMaterial;
import org.dayflower.scene.material.LambertianMaterial;
import org.dayflower.scene.material.OrenNayarMaterial;
import org.dayflower.scene.material.ReflectionMaterial;
import org.dayflower.scene.material.RefractionMaterial;
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
	public static Scene newCornellBoxScene() {
		final Material material01 = new LambertianMaterial();
		final Material material02 = new LambertianMaterial();
		final Material material03 = new LambertianMaterial();
		final Material material04 = new LambertianMaterial();
		final Material material05 = new LambertianMaterial();
		final Material material06 = new LambertianMaterial();
		final Material material07 = new ReflectionMaterial();
		final Material material08 = new RefractionMaterial();
		final Material material09 = new LambertianMaterial();
		
		final Shape3F shape01 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F,  1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Bottom
		final Shape3F shape02 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F, -1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Top
		final Shape3F shape03 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Back
		final Shape3F shape04 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Front
		final Shape3F shape05 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
		final Shape3F shape06 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
		final Shape3F shape07 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		final Shape3F shape08 = new Sphere3F(1.0F, new Point3F(0.0F, 0.0F, 0.0F));
		final Shape3F shape09 = new Triangle3F();
		
		final Texture texture011 = new ConstantTexture(Color3F.GRAY);
		final Texture texture012 = new ConstantTexture();
		final Texture texture013 = new ConstantTexture();
		final Texture texture021 = new ConstantTexture(Color3F.GRAY);
		final Texture texture022 = new ConstantTexture();
		final Texture texture023 = new ConstantTexture();
		final Texture texture031 = new ConstantTexture(Color3F.GRAY);
		final Texture texture032 = new ConstantTexture();
		final Texture texture033 = new ConstantTexture();
		final Texture texture041 = new ConstantTexture(Color3F.GRAY);
		final Texture texture042 = new ConstantTexture();
		final Texture texture043 = new ConstantTexture();
		final Texture texture051 = new ConstantTexture(new Color3F(0.25F, 0.25F, 0.75F));
		final Texture texture052 = new ConstantTexture();
		final Texture texture053 = new ConstantTexture();
		final Texture texture061 = new ConstantTexture(new Color3F(0.75F, 0.25F, 0.25F));
		final Texture texture062 = new ConstantTexture();
		final Texture texture063 = new ConstantTexture();
		final Texture texture071 = new ConstantTexture(Color3F.WHITE);
		final Texture texture072 = new ConstantTexture();
		final Texture texture073 = new ConstantTexture();
		final Texture texture081 = new ConstantTexture(Color3F.WHITE);
		final Texture texture082 = new ConstantTexture();
		final Texture texture083 = new ConstantTexture();
		final Texture texture091 = new ConstantTexture(Color3F.WHITE);
		final Texture texture092 = new ConstantTexture(new Color3F(12.0F));
		final Texture texture093 = new ConstantTexture();
		
		final Matrix44F matrix01 = Matrix44F.translate( 0.0F, 0.0F,  0.0F);
		final Matrix44F matrix02 = Matrix44F.translate( 0.0F, 5.0F,  0.0F);
		final Matrix44F matrix03 = Matrix44F.translate( 0.0F, 0.0F,  0.0F);
		final Matrix44F matrix04 = Matrix44F.translate( 0.0F, 0.0F, 10.0F);
		final Matrix44F matrix05 = Matrix44F.translate( 3.0F, 0.0F,  0.0F);
		final Matrix44F matrix06 = Matrix44F.translate(-3.0F, 0.0F,  0.0F);
		final Matrix44F matrix07 = Matrix44F.translate(-1.5F, 1.0F,  8.0F);
		final Matrix44F matrix08 = Matrix44F.translate( 1.5F, 1.0F,  7.0F);
		final Matrix44F matrix09 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 4.99F, 9.0F), Matrix44F.rotateX(AngleF.degrees(270.0F))), Matrix44F.scale(0.25F));
		
		final Primitive primitive01 = new Primitive(material01, shape01, texture011, texture012, texture013, matrix01);
		final Primitive primitive02 = new Primitive(material02, shape02, texture021, texture022, texture023, matrix02);
		final Primitive primitive03 = new Primitive(material03, shape03, texture031, texture032, texture033, matrix03);
		final Primitive primitive04 = new Primitive(material04, shape04, texture041, texture042, texture043, matrix04);
		final Primitive primitive05 = new Primitive(material05, shape05, texture051, texture052, texture053, matrix05);
		final Primitive primitive06 = new Primitive(material06, shape06, texture061, texture062, texture063, matrix06);
		final Primitive primitive07 = new Primitive(material07, shape07, texture071, texture072, texture073, matrix07);
		final Primitive primitive08 = new Primitive(material08, shape08, texture081, texture082, texture083, matrix08);
		final Primitive primitive09 = new Primitive(material09, shape09, texture091, texture092, texture093, matrix09);
		
		final
		Camera camera = new Camera(new Point3F(0.0F, 2.5F, 1.0F));
		camera.setResolution(1024.0F, 768.0F);
		camera.setFieldOfViewY(AngleF.degrees(58.0F));
		camera.setFieldOfViewX();
		
		final
		Scene scene = new Scene(new ConstantBackground(Color3F.BLACK), camera, "CornellBox");
		scene.addLight(new PrimitiveLight(primitive09));
		scene.addPrimitive(primitive01);
		scene.addPrimitive(primitive02);
		scene.addPrimitive(primitive03);
		scene.addPrimitive(primitive04);
		scene.addPrimitive(primitive05);
		scene.addPrimitive(primitive06);
		scene.addPrimitive(primitive07);
		scene.addPrimitive(primitive08);
		scene.addPrimitive(primitive09);
		
		return scene;
	}
	
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
		final Material material5 = new RefractionMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		final Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F));
		final Shape3F shape4 = TriangleMesh3F.readWavefrontObject(new File("./resources/smoothMonkey2.obj"), true).get(0);
		final Shape3F shape5 = new Torus3F();
		
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
		final Texture texture51 = new ConstantTexture(Color3F.WHITE);
		final Texture texture52 = new ConstantTexture();
		final Texture texture53 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		final Matrix44F matrix3 = Matrix44F.translate(-3.0F, 1.0F, 5.0F);
		final Matrix44F matrix4 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix5 = Matrix44F.translate(3.0F, 1.25F, 5.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "Default");
		scene.addLight(new PointLight(new Point3F(0.0F, 2.0F, 5.0F), new Color3F(1.0F, 1.0F, 1.0F)));
		scene.addLight(new PointLight(new Point3F(0.0F, 1.0F, 0.0F), new Color3F(1.0F, 1.0F, 1.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		scene.addPrimitive(new Primitive(material3, shape3, texture31, texture32, texture33, matrix3));
		scene.addPrimitive(new Primitive(material4, shape4, texture41, texture42, texture43, matrix4));
		scene.addPrimitive(new Primitive(material5, shape5, texture51, texture52, texture53, matrix5));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newSL500Scene() {
		final List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject(new File("./resources/SL500.obj"), false);
		
		final Color3F colorA = new Color3F(0.3F, 0.7F, 0.3F);
		final Color3F colorB = new Color3F(0.1F, 0.5F, 0.1F);
		final Color3F colorC = Color3F.GRAY;
		
		final Material material01 = new LambertianMaterial();
		final Material material02 = new RefractionMaterial();//new ReflectionMaterial();
		final Material material03 = new AshikhminShirleyMaterial(0.05F);
		final Material material04 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material05 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material06 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material07 = new AshikhminShirleyMaterial(0.05F);
		final Material material08 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material09 = new RefractionMaterial();//new ReflectionMaterial();
		final Material material10 = new RefractionMaterial();
		final Material material11 = new RefractionMaterial();//new ReflectionMaterial();
		final Material material12 = new RefractionMaterial();
		final Material material13 = new RefractionMaterial();
		final Material material14 = new AshikhminShirleyMaterial(0.05F);
		final Material material15 = new AshikhminShirleyMaterial(0.05F);
		final Material material16 = new AshikhminShirleyMaterial(0.02F);
		final Material material17 = new AshikhminShirleyMaterial(0.02F);
		final Material material18 = new AshikhminShirleyMaterial(0.02F);
		final Material material19 = new AshikhminShirleyMaterial(0.02F);
		final Material material20 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material21 = new AshikhminShirleyMaterial(0.05F);
		final Material material22 = new AshikhminShirleyMaterial(0.02F);
		final Material material23 = new AshikhminShirleyMaterial(0.02F);
		final Material material24 = new AshikhminShirleyMaterial(0.02F);
		final Material material25 = new AshikhminShirleyMaterial(0.02F);
		final Material material26 = new AshikhminShirleyMaterial(0.02F);
		final Material material27 = new AshikhminShirleyMaterial(0.02F);
		final Material material28 = new AshikhminShirleyMaterial(0.02F);
		final Material material29 = new AshikhminShirleyMaterial(0.02F);
		final Material material30 = new AshikhminShirleyMaterial(0.02F);
		final Material material31 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material32 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material33 = new AshikhminShirleyMaterial(0.05F);
		final Material material34 = new RefractionMaterial();
		final Material material35 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material36 = new AshikhminShirleyMaterial(0.05F);
		final Material material37 = new RefractionMaterial();
		final Material material38 = new AshikhminShirleyMaterial(0.02F);
		final Material material39 = new AshikhminShirleyMaterial(0.02F);
		final Material material40 = new AshikhminShirleyMaterial(0.05F);
		final Material material41 = new AshikhminShirleyMaterial(0.02F);
		final Material material42 = new AshikhminShirleyMaterial(0.02F);
		final Material material43 = new AshikhminShirleyMaterial(0.05F);
		final Material material44 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material45 = new ReflectionMaterial();
		final Material material46 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material47 = new AshikhminShirleyMaterial(0.02F);
		final Material material48 = new ReflectionMaterial();
		final Material material49 = new AshikhminShirleyMaterial(0.05F);
		final Material material50 = new AshikhminShirleyMaterial(0.02F);
		final Material material51 = new ReflectionMaterial();
		final Material material52 = new AshikhminShirleyMaterial(0.05F);
		final Material material53 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material54 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material55 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material56 = new AshikhminShirleyMaterial(0.02F);
		final Material material57 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material58 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material59 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material60 = new AshikhminShirleyMaterial(0.02F);
		final Material material61 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material62 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material63 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material64 = new AshikhminShirleyMaterial(0.02F);
		final Material material65 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material66 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material67 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		final Material material68 = new AshikhminShirleyMaterial(0.02F);
		final Material material69 = new OrenNayarMaterial(AngleF.degrees(20.0F));
		
		final Material material70 = new LambertianMaterial();
		final Material material71 = new LambertianMaterial();
		final Material material72 = new LambertianMaterial();
		final Material material73 = new LambertianMaterial();
		final Material material74 = new LambertianMaterial();
		final Material material75 = new LambertianMaterial();
		
		final Shape3F shape01 = new Plane3F();
		final Shape3F shape02 = triangleMeshes.get( 0);
		final Shape3F shape03 = triangleMeshes.get( 1);
		final Shape3F shape04 = triangleMeshes.get( 2);
		final Shape3F shape05 = triangleMeshes.get( 3);
		final Shape3F shape06 = triangleMeshes.get( 4);
		final Shape3F shape07 = triangleMeshes.get( 5);
		final Shape3F shape08 = triangleMeshes.get( 6);
		final Shape3F shape09 = triangleMeshes.get( 7);
		final Shape3F shape10 = triangleMeshes.get( 8);
		final Shape3F shape11 = triangleMeshes.get( 9);
		final Shape3F shape12 = triangleMeshes.get(10);
		final Shape3F shape13 = triangleMeshes.get(11);
		final Shape3F shape14 = triangleMeshes.get(12);
		final Shape3F shape15 = triangleMeshes.get(13);
		final Shape3F shape16 = triangleMeshes.get(14);
		final Shape3F shape17 = triangleMeshes.get(15);
		final Shape3F shape18 = triangleMeshes.get(16);
		final Shape3F shape19 = triangleMeshes.get(17);
		final Shape3F shape20 = triangleMeshes.get(18);
		final Shape3F shape21 = triangleMeshes.get(19);
		final Shape3F shape22 = triangleMeshes.get(20);
		final Shape3F shape23 = triangleMeshes.get(21);
		final Shape3F shape24 = triangleMeshes.get(22);
		final Shape3F shape25 = triangleMeshes.get(23);
		final Shape3F shape26 = triangleMeshes.get(24);
		final Shape3F shape27 = triangleMeshes.get(25);
		final Shape3F shape28 = triangleMeshes.get(26);
		final Shape3F shape29 = triangleMeshes.get(27);
		final Shape3F shape30 = triangleMeshes.get(28);
		final Shape3F shape31 = triangleMeshes.get(29);
		final Shape3F shape32 = triangleMeshes.get(30);
		final Shape3F shape33 = triangleMeshes.get(31);
		final Shape3F shape34 = triangleMeshes.get(32);
		final Shape3F shape35 = triangleMeshes.get(33);
		final Shape3F shape36 = triangleMeshes.get(34);
		final Shape3F shape37 = triangleMeshes.get(35);
		final Shape3F shape38 = triangleMeshes.get(36);
		final Shape3F shape39 = triangleMeshes.get(37);
		final Shape3F shape40 = triangleMeshes.get(38);
		final Shape3F shape41 = triangleMeshes.get(39);
		final Shape3F shape42 = triangleMeshes.get(40);
		final Shape3F shape43 = triangleMeshes.get(41);
		final Shape3F shape44 = triangleMeshes.get(42);
		final Shape3F shape45 = triangleMeshes.get(43);
		final Shape3F shape46 = triangleMeshes.get(44);
		final Shape3F shape47 = triangleMeshes.get(45);
		final Shape3F shape48 = triangleMeshes.get(46);
		final Shape3F shape49 = triangleMeshes.get(47);
		final Shape3F shape50 = triangleMeshes.get(48);
		final Shape3F shape51 = triangleMeshes.get(49);
		final Shape3F shape52 = triangleMeshes.get(50);
		final Shape3F shape53 = triangleMeshes.get(51);
		final Shape3F shape54 = triangleMeshes.get(52);
		final Shape3F shape55 = triangleMeshes.get(53);
		final Shape3F shape56 = triangleMeshes.get(54);
		final Shape3F shape57 = triangleMeshes.get(55);
		final Shape3F shape58 = triangleMeshes.get(56);
		final Shape3F shape59 = triangleMeshes.get(57);
		final Shape3F shape60 = triangleMeshes.get(58);
		final Shape3F shape61 = triangleMeshes.get(59);
		final Shape3F shape62 = triangleMeshes.get(60);
		final Shape3F shape63 = triangleMeshes.get(61);
		final Shape3F shape64 = triangleMeshes.get(62);
		final Shape3F shape65 = triangleMeshes.get(63);
		final Shape3F shape66 = triangleMeshes.get(64);
		final Shape3F shape67 = triangleMeshes.get(65);
		final Shape3F shape68 = triangleMeshes.get(66);
		final Shape3F shape69 = triangleMeshes.get(67);
		
		final Shape3F shape70 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  0.0F, -1.0F), new Point3F(1.0F, 0.0F,  0.0F));//Top
		final Shape3F shape71 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Back
		final Shape3F shape72 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(1.0F, 0.0F,  0.0F));//Front
		final Shape3F shape73 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F, -1.0F));//Right
		final Shape3F shape74 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F,  1.0F,  0.0F), new Point3F(0.0F, 0.0F,  1.0F));//Left
		final Shape3F shape75 = new Triangle3F();
		
		final Texture texture011 = new ConstantTexture(Color3F.GRAY);//new SimplexFractionalBrownianMotionTexture(Color3F.GRAY/*new Color3F(0.8F, 0.5F, 0.0F)*/, 5.0F, 0.5F, 16);
		final Texture texture012 = new ConstantTexture();
		final Texture texture013 = new ConstantTexture();
		final Texture texture021 = new ConstantTexture(Color3F.WHITE);
		final Texture texture022 = new ConstantTexture();
		final Texture texture023 = new ConstantTexture();
		final Texture texture031 = new ConstantTexture(colorA);
		final Texture texture032 = new ConstantTexture();
		final Texture texture033 = new ConstantTexture();
		final Texture texture041 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture042 = new ConstantTexture();
		final Texture texture043 = new ConstantTexture();
		final Texture texture051 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture052 = new ConstantTexture();
		final Texture texture053 = new ConstantTexture();
		final Texture texture061 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture062 = new ConstantTexture();
		final Texture texture063 = new ConstantTexture();
		final Texture texture071 = new ConstantTexture(colorA);
		final Texture texture072 = new ConstantTexture();
		final Texture texture073 = new ConstantTexture();
		final Texture texture081 = new ConstantTexture(Color3F.WHITE);
		final Texture texture082 = new ConstantTexture();
		final Texture texture083 = new ConstantTexture();
		final Texture texture091 = new ConstantTexture(Color3F.WHITE);
		final Texture texture092 = new ConstantTexture();
		final Texture texture093 = new ConstantTexture();
		final Texture texture101 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
		final Texture texture102 = new ConstantTexture();
		final Texture texture103 = new ConstantTexture();
		final Texture texture111 = new ConstantTexture(Color3F.WHITE);
		final Texture texture112 = new ConstantTexture();
		final Texture texture113 = new ConstantTexture();
		final Texture texture121 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
		final Texture texture122 = new ConstantTexture();
		final Texture texture123 = new ConstantTexture();
		final Texture texture131 = new ConstantTexture(new Color3F(1.0F, 0.01F, 0.01F));
		final Texture texture132 = new ConstantTexture();
		final Texture texture133 = new ConstantTexture();
		final Texture texture141 = new ConstantTexture(colorB);
		final Texture texture142 = new ConstantTexture();
		final Texture texture143 = new ConstantTexture();
		final Texture texture151 = new ConstantTexture(colorA);
		final Texture texture152 = new ConstantTexture();
		final Texture texture153 = new ConstantTexture();
		final Texture texture161 = new ConstantTexture(colorC);
		final Texture texture162 = new ConstantTexture();
		final Texture texture163 = new ConstantTexture();
		final Texture texture171 = new ConstantTexture(colorC);
		final Texture texture172 = new ConstantTexture();
		final Texture texture173 = new ConstantTexture();
		final Texture texture181 = new ConstantTexture(colorC);
		final Texture texture182 = new ConstantTexture();
		final Texture texture183 = new ConstantTexture();
		final Texture texture191 = new ConstantTexture(colorC);
		final Texture texture192 = new ConstantTexture();
		final Texture texture193 = new ConstantTexture();
		final Texture texture201 = new ConstantTexture(new Color3F(0.2F));
		final Texture texture202 = new ConstantTexture();
		final Texture texture203 = new ConstantTexture();
		final Texture texture211 = new ConstantTexture(colorA);
		final Texture texture212 = new ConstantTexture();
		final Texture texture213 = new ConstantTexture();
		final Texture texture221 = new ConstantTexture(colorC);
		final Texture texture222 = new ConstantTexture();
		final Texture texture223 = new ConstantTexture();
		final Texture texture231 = new ConstantTexture(colorC);
		final Texture texture232 = new ConstantTexture();
		final Texture texture233 = new ConstantTexture();
		final Texture texture241 = new ConstantTexture(colorC);
		final Texture texture242 = new ConstantTexture();
		final Texture texture243 = new ConstantTexture();
		final Texture texture251 = new ConstantTexture(colorC);
		final Texture texture252 = new ConstantTexture();
		final Texture texture253 = new ConstantTexture();
		final Texture texture261 = new ConstantTexture(colorC);
		final Texture texture262 = new ConstantTexture();
		final Texture texture263 = new ConstantTexture();
		final Texture texture271 = new ConstantTexture(colorC);
		final Texture texture272 = new ConstantTexture();
		final Texture texture273 = new ConstantTexture();
		final Texture texture281 = new ConstantTexture(colorC);
		final Texture texture282 = new ConstantTexture();
		final Texture texture283 = new ConstantTexture();
		final Texture texture291 = new ConstantTexture(colorC);
		final Texture texture292 = new ConstantTexture();
		final Texture texture293 = new ConstantTexture();
		final Texture texture301 = new ConstantTexture(colorC);
		final Texture texture302 = new ConstantTexture();
		final Texture texture303 = new ConstantTexture();
		final Texture texture311 = new ConstantTexture(new Color3F(227, 161, 115));
		final Texture texture312 = new ConstantTexture();
		final Texture texture313 = new ConstantTexture();
		final Texture texture321 = new ConstantTexture(Color3F.WHITE);
		final Texture texture322 = new ConstantTexture(new Color3F(12.0F));
		final Texture texture323 = new ConstantTexture();
		final Texture texture331 = new ConstantTexture(colorB);
		final Texture texture332 = new ConstantTexture();
		final Texture texture333 = new ConstantTexture();
		final Texture texture341 = new ConstantTexture(Color3F.WHITE);
		final Texture texture342 = new ConstantTexture();
		final Texture texture343 = new ConstantTexture();
		final Texture texture351 = new ConstantTexture(Color3F.WHITE);
		final Texture texture352 = new ConstantTexture(new Color3F(12.0F));
		final Texture texture353 = new ConstantTexture();
		final Texture texture361 = new ConstantTexture(colorB);
		final Texture texture362 = new ConstantTexture();
		final Texture texture363 = new ConstantTexture();
		final Texture texture371 = new ConstantTexture(Color3F.WHITE);
		final Texture texture372 = new ConstantTexture();
		final Texture texture373 = new ConstantTexture();
		final Texture texture381 = new ConstantTexture(colorC);
		final Texture texture382 = new ConstantTexture();
		final Texture texture383 = new ConstantTexture();
		final Texture texture391 = new ConstantTexture(colorC);
		final Texture texture392 = new ConstantTexture();
		final Texture texture393 = new ConstantTexture();
		final Texture texture401 = new ConstantTexture(colorA);
		final Texture texture402 = new ConstantTexture();
		final Texture texture403 = new ConstantTexture();
		final Texture texture411 = new ConstantTexture(colorC);
		final Texture texture412 = new ConstantTexture();
		final Texture texture413 = new ConstantTexture();
		final Texture texture421 = new ConstantTexture(colorC);
		final Texture texture422 = new ConstantTexture();
		final Texture texture423 = new ConstantTexture();
		final Texture texture431 = new ConstantTexture(colorA);
		final Texture texture432 = new ConstantTexture();
		final Texture texture433 = new ConstantTexture();
		final Texture texture441 = new ConstantTexture(new Color3F(98, 74, 46));
		final Texture texture442 = new ConstantTexture();
		final Texture texture443 = new ConstantTexture();
		final Texture texture451 = new ConstantTexture(Color3F.WHITE);
		final Texture texture452 = new ConstantTexture();
		final Texture texture453 = new ConstantTexture();
		final Texture texture461 = new ConstantTexture(new Color3F(98, 74, 46));
		final Texture texture462 = new ConstantTexture();
		final Texture texture463 = new ConstantTexture();
		final Texture texture471 = new ConstantTexture(colorC);
		final Texture texture472 = new ConstantTexture();
		final Texture texture473 = new ConstantTexture();
		final Texture texture481 = new ConstantTexture(Color3F.WHITE);
		final Texture texture482 = new ConstantTexture();
		final Texture texture483 = new ConstantTexture();
		final Texture texture491 = new ConstantTexture(colorA);
		final Texture texture492 = new ConstantTexture();
		final Texture texture493 = new ConstantTexture();
		final Texture texture501 = new ConstantTexture(colorC);
		final Texture texture502 = new ConstantTexture();
		final Texture texture503 = new ConstantTexture();
		final Texture texture511 = new ConstantTexture(Color3F.WHITE);
		final Texture texture512 = new ConstantTexture();
		final Texture texture513 = new ConstantTexture();
		final Texture texture521 = new ConstantTexture(colorA);
		final Texture texture522 = new ConstantTexture();
		final Texture texture523 = new ConstantTexture();
		final Texture texture531 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture532 = new ConstantTexture();
		final Texture texture533 = new ConstantTexture();
		final Texture texture541 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture542 = new ConstantTexture();
		final Texture texture543 = new ConstantTexture();
		final Texture texture551 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture552 = new ConstantTexture();
		final Texture texture553 = new ConstantTexture();
		final Texture texture561 = new ConstantTexture(colorC);
		final Texture texture562 = new ConstantTexture();
		final Texture texture563 = new ConstantTexture();
		final Texture texture571 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture572 = new ConstantTexture();
		final Texture texture573 = new ConstantTexture();
		final Texture texture581 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture582 = new ConstantTexture();
		final Texture texture583 = new ConstantTexture();
		final Texture texture591 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture592 = new ConstantTexture();
		final Texture texture593 = new ConstantTexture();
		final Texture texture601 = new ConstantTexture(colorC);
		final Texture texture602 = new ConstantTexture();
		final Texture texture603 = new ConstantTexture();
		final Texture texture611 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture612 = new ConstantTexture();
		final Texture texture613 = new ConstantTexture();
		final Texture texture621 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture622 = new ConstantTexture();
		final Texture texture623 = new ConstantTexture();
		final Texture texture631 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture632 = new ConstantTexture();
		final Texture texture633 = new ConstantTexture();
		final Texture texture641 = new ConstantTexture(colorC);
		final Texture texture642 = new ConstantTexture();
		final Texture texture643 = new ConstantTexture();
		final Texture texture651 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture652 = new ConstantTexture();
		final Texture texture653 = new ConstantTexture();
		final Texture texture661 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture662 = new ConstantTexture();
		final Texture texture663 = new ConstantTexture();
		final Texture texture671 = new ConstantTexture(new Color3F(0.1F));
		final Texture texture672 = new ConstantTexture();
		final Texture texture673 = new ConstantTexture();
		final Texture texture681 = new ConstantTexture(colorC);
		final Texture texture682 = new ConstantTexture();
		final Texture texture683 = new ConstantTexture();
		final Texture texture691 = new ConstantTexture(Color3F.WHITE);
		final Texture texture692 = new ConstantTexture();
		final Texture texture693 = new ConstantTexture();
		
		final Texture texture701 = new ConstantTexture(Color3F.GRAY);
		final Texture texture702 = new ConstantTexture();
		final Texture texture703 = new ConstantTexture();
		final Texture texture711 = new ConstantTexture(Color3F.GRAY);
		final Texture texture712 = new ConstantTexture();
		final Texture texture713 = new ConstantTexture();
		final Texture texture721 = new ConstantTexture(Color3F.GRAY);
		final Texture texture722 = new ConstantTexture();
		final Texture texture723 = new ConstantTexture();
		final Texture texture731 = new ConstantTexture(new Color3F(0.25F, 0.25F, 0.75F));
		final Texture texture732 = new ConstantTexture();
		final Texture texture733 = new ConstantTexture();
		final Texture texture741 = new ConstantTexture(new Color3F(0.75F, 0.25F, 0.25F));
		final Texture texture742 = new ConstantTexture();
		final Texture texture743 = new ConstantTexture();
		final Texture texture751 = new ConstantTexture(Color3F.WHITE);
		final Texture texture752 = new ConstantTexture(new Color3F(12.0F));
		final Texture texture753 = new ConstantTexture();
		
		final Matrix44F matrix01 = Matrix44F.identity();
		final Matrix44F matrixNM = Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 5.0F), Matrix44F.multiply(Matrix44F.rotateY(AngleF.degrees(90.0F)), Matrix44F.rotateX(AngleF.degrees(270.0F))));
		final Matrix44F matrix70 = Matrix44F.translate( 0.0F, 5.0F,  0.0F);
		final Matrix44F matrix71 = Matrix44F.translate( 0.0F, 0.0F,  0.0F);
		final Matrix44F matrix72 = Matrix44F.translate( 0.0F, 0.0F, 10.0F);
		final Matrix44F matrix73 = Matrix44F.translate( 3.0F, 0.0F,  0.0F);
		final Matrix44F matrix74 = Matrix44F.translate(-3.0F, 0.0F,  0.0F);
		final Matrix44F matrix75 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 4.99F, 1.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.25F));
		
		final Primitive primitive75 = new Primitive(material75, shape75, texture751, texture752, texture753, matrix75);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "SL500");
		scene.addLight(new PrimitiveLight(primitive75));
		scene.addPrimitive(new Primitive(material01, shape01, texture011, texture012, texture013, matrix01));
		scene.addPrimitive(new Primitive(material02, shape02, texture021, texture022, texture023, matrixNM));//Base			wind_glass
		scene.addPrimitive(new Primitive(material03, shape03, texture031, texture032, texture033, matrixNM));//Base			Body_paint
		scene.addPrimitive(new Primitive(material04, shape04, texture041, texture042, texture043, matrixNM));//Base			Misc
		scene.addPrimitive(new Primitive(material05, shape05, texture051, texture052, texture053, matrixNM));//Base			Misc0
		scene.addPrimitive(new Primitive(material06, shape06, texture061, texture062, texture063, matrixNM));//Base			Material__583
		scene.addPrimitive(new Primitive(material07, shape07, texture071, texture072, texture073, matrixNM));//Base			Body_paint
		scene.addPrimitive(new Primitive(material08, shape08, texture081, texture082, texture083, matrixNM));//Base			License
		scene.addPrimitive(new Primitive(material09, shape09, texture091, texture092, texture093, matrixNM));//Base			wind_glass
		scene.addPrimitive(new Primitive(material10, shape10, texture101, texture102, texture103, matrixNM));//Base			Material__586
		scene.addPrimitive(new Primitive(material11, shape11, texture111, texture112, texture113, matrixNM));//Base			wind_glass
		scene.addPrimitive(new Primitive(material12, shape12, texture121, texture122, texture123, matrixNM));//BLightL		Material__589
		scene.addPrimitive(new Primitive(material13, shape13, texture131, texture132, texture133, matrixNM));//BLightR		Material__589
		scene.addPrimitive(new Primitive(material14, shape14, texture141, texture142, texture143, matrixNM));//Body			DoorLine
		scene.addPrimitive(new Primitive(material15, shape15, texture151, texture152, texture153, matrixNM));//Body			Badging_Chrome
		scene.addPrimitive(new Primitive(material16, shape16, texture161, texture162, texture163, matrixNM));//Body			Misc1
		scene.addPrimitive(new Primitive(material17, shape17, texture171, texture172, texture173, matrixNM));//Body			Misc_Chrome
		scene.addPrimitive(new Primitive(material18, shape18, texture181, texture182, texture183, matrixNM));//Body			Misc_Chrome0
		scene.addPrimitive(new Primitive(material19, shape19, texture191, texture192, texture193, matrixNM));//Body			Misc_Chrome1
		scene.addPrimitive(new Primitive(material20, shape20, texture201, texture202, texture203, matrixNM));//Body			Black
		scene.addPrimitive(new Primitive(material21, shape21, texture211, texture212, texture213, matrixNM));//Body			Body_paint0
		scene.addPrimitive(new Primitive(material22, shape22, texture221, texture222, texture223, matrixNM));//Body			Bottom
		scene.addPrimitive(new Primitive(material23, shape23, texture231, texture232, texture233, matrixNM));//BrakeFL		Brake_Pads
		scene.addPrimitive(new Primitive(material24, shape24, texture241, texture242, texture243, matrixNM));//BrakeFL		Brake_Disc
		scene.addPrimitive(new Primitive(material25, shape25, texture251, texture252, texture253, matrixNM));//BrakeFR		Brake_Pads0
		scene.addPrimitive(new Primitive(material26, shape26, texture261, texture262, texture263, matrixNM));//BrakeFR		Brake_Disc0
		scene.addPrimitive(new Primitive(material27, shape27, texture271, texture272, texture273, matrixNM));//BrakeRL		Brake_Pads
		scene.addPrimitive(new Primitive(material28, shape28, texture281, texture282, texture283, matrixNM));//BrakeRL		Brake_Disc
		scene.addPrimitive(new Primitive(material29, shape29, texture291, texture292, texture293, matrixNM));//BrakeRR		Brake_Pads0
		scene.addPrimitive(new Primitive(material30, shape30, texture301, texture302, texture303, matrixNM));//BrakeRR		Brake_Disc0
		scene.addPrimitive(new Primitive(material31, shape31, texture311, texture312, texture313, matrixNM));//Driver		Driver
		scene.addPrimitive(new Primitive(material32, shape32, texture321, texture322, texture323, matrixNM));//HLightL		Material__593
		scene.addPrimitive(new Primitive(material33, shape33, texture331, texture332, texture333, matrixNM));//HLightL		Misc2
		scene.addPrimitive(new Primitive(material34, shape34, texture341, texture342, texture343, matrixNM));//HLightLG		Material__594
		scene.addPrimitive(new Primitive(material35, shape35, texture351, texture352, texture353, matrixNM));//HLightR		Material__593
		scene.addPrimitive(new Primitive(material36, shape36, texture361, texture362, texture363, matrixNM));//HLightR		Misc2
		scene.addPrimitive(new Primitive(material37, shape37, texture371, texture372, texture373, matrixNM));//HLightRG		Material__594
		scene.addPrimitive(new Primitive(material38, shape38, texture381, texture382, texture383, matrixNM));//Hood			Misc3
		scene.addPrimitive(new Primitive(material39, shape39, texture391, texture392, texture393, matrixNM));//Hood			Misc_Chrome2
		scene.addPrimitive(new Primitive(material40, shape40, texture401, texture402, texture403, matrixNM));//Hood			Body_paint1
		scene.addPrimitive(new Primitive(material41, shape41, texture411, texture412, texture413, matrixNM));//Hood_Carbon	Misc4
		scene.addPrimitive(new Primitive(material42, shape42, texture421, texture422, texture423, matrixNM));//Hood_Carbon	Misc_Chrome3
		scene.addPrimitive(new Primitive(material43, shape43, texture431, texture432, texture433, matrixNM));//Hood_Carbon	Body_paint2
		scene.addPrimitive(new Primitive(material44, shape44, texture441, texture442, texture443, matrixNM));//Interior		Interior
		scene.addPrimitive(new Primitive(material45, shape45, texture451, texture452, texture453, matrixNM));//Interior		Material__597
		scene.addPrimitive(new Primitive(material46, shape46, texture461, texture462, texture463, matrixNM));//Interior		Interior0
		scene.addPrimitive(new Primitive(material47, shape47, texture471, texture472, texture473, matrixNM));//MirrorL		Misc_Chrome4
		scene.addPrimitive(new Primitive(material48, shape48, texture481, texture482, texture483, matrixNM));//MirrorL		Material__598
		scene.addPrimitive(new Primitive(material49, shape49, texture491, texture492, texture493, matrixNM));//MirrorL		Body_paint3
		scene.addPrimitive(new Primitive(material50, shape50, texture501, texture502, texture503, matrixNM));//MirrorR		Misc_Chrome4
		scene.addPrimitive(new Primitive(material51, shape51, texture511, texture512, texture513, matrixNM));//MirrorR		Material__598
		scene.addPrimitive(new Primitive(material52, shape52, texture521, texture522, texture523, matrixNM));//MirrorR		Body_paint3
		scene.addPrimitive(new Primitive(material53, shape53, texture531, texture532, texture533, matrixNM));//TireFL		Tire_Back
		scene.addPrimitive(new Primitive(material54, shape54, texture541, texture542, texture543, matrixNM));//TireFL		Tire_Tread
		scene.addPrimitive(new Primitive(material55, shape55, texture551, texture552, texture553, matrixNM));//TireFL		Tire_Sidewall
		scene.addPrimitive(new Primitive(material56, shape56, texture561, texture562, texture563, matrixNM));//TireFL		Material__600
		scene.addPrimitive(new Primitive(material57, shape57, texture571, texture572, texture573, matrixNM));//TireFR		Tire_Back
		scene.addPrimitive(new Primitive(material58, shape58, texture581, texture582, texture583, matrixNM));//TireFR		Tire_Tread
		scene.addPrimitive(new Primitive(material59, shape59, texture591, texture592, texture593, matrixNM));//TireFR		Tire_Sidewall
		scene.addPrimitive(new Primitive(material60, shape60, texture601, texture602, texture603, matrixNM));//TireFR		Material__600
		scene.addPrimitive(new Primitive(material61, shape61, texture611, texture612, texture613, matrixNM));//TireRL		Tire_Back
		scene.addPrimitive(new Primitive(material62, shape62, texture621, texture622, texture623, matrixNM));//TireRL		Tire_Tread
		scene.addPrimitive(new Primitive(material63, shape63, texture631, texture632, texture633, matrixNM));//TireRL		Tire_Sidewall
		scene.addPrimitive(new Primitive(material64, shape64, texture641, texture642, texture643, matrixNM));//TireRL		Material__600
		scene.addPrimitive(new Primitive(material65, shape65, texture651, texture652, texture653, matrixNM));//TireRR		Tire_Back
		scene.addPrimitive(new Primitive(material66, shape66, texture661, texture662, texture663, matrixNM));//TireRR		Tire_Tread
		scene.addPrimitive(new Primitive(material67, shape67, texture671, texture672, texture673, matrixNM));//TireRR		Tire_Sidewall
		scene.addPrimitive(new Primitive(material68, shape68, texture681, texture682, texture683, matrixNM));//TireRR		Material__600
		scene.addPrimitive(new Primitive(material69, shape69, texture691, texture692, texture693, matrixNM));//License		License0
		scene.addPrimitive(new Primitive(material70, shape70, texture701, texture702, texture703, matrix70));
		scene.addPrimitive(new Primitive(material71, shape71, texture711, texture712, texture713, matrix71));
		scene.addPrimitive(new Primitive(material72, shape72, texture721, texture722, texture723, matrix72));
		scene.addPrimitive(new Primitive(material73, shape73, texture731, texture732, texture733, matrix73));
		scene.addPrimitive(new Primitive(material74, shape74, texture741, texture742, texture743, matrix74));
		scene.addPrimitive(primitive75);
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link ConstantBackground}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code ConstantBackground}
	 */
	public static Scene newShowcaseBackgroundConstantBackgroundScene() {
		return new Scene(new ConstantBackground(), new Camera(), "ShowcaseBackgroundConstantBackground");
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link ImageBackground}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code ImageBackground}
	 */
	public static Scene newShowcaseBackgroundImageBackgroundScene() {
		return new Scene(ImageBackground.load(new File("./resources/Image.jpg"), AngleF.degrees(0.0F), new Vector2F(1.0F, 1.0F)), new Camera(), "ShowcaseBackgroundImageBackground");
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link PerezBackground}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code PerezBackground}
	 */
	public static Scene newShowcaseBackgroundPerezBackgroundScene() {
		return new Scene(new PerezBackground(), new Camera(), "ShowcaseBackgroundPerezBackground");
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link AshikhminShirleyMaterial}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code AshikhminShirleyMaterial}
	 */
	public static Scene newShowcaseMaterialAshikhminShirleyMaterialScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new AshikhminShirleyMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.WHITE);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ConstantTexture(Color3F.GRAY);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseMaterialAshikhminShirleyMaterial");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link LambertianMaterial}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code LambertianMaterial}
	 */
	public static Scene newShowcaseMaterialLambertianMaterialScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new LambertianMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.WHITE);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ConstantTexture(Color3F.GRAY);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseMaterialLambertianMaterial");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link OrenNayarMaterial}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code OrenNayarMaterial}
	 */
	public static Scene newShowcaseMaterialOrenNayarMaterialScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new OrenNayarMaterial(AngleF.degrees(90.0F));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.WHITE);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ConstantTexture(Color3F.GRAY);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseMaterialOrenNayarMaterial");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link ReflectionMaterial}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code ReflectionMaterial}
	 */
	public static Scene newShowcaseMaterialReflectionMaterialScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new ReflectionMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.WHITE);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ConstantTexture(Color3F.GRAY);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 2.0F, 20.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseMaterialReflectionMaterial");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance used as a showcase for {@link RefractionMaterial}.
	 * 
	 * @return a {@code Scene} instance used as a showcase for {@code RefractionMaterial}
	 */
	public static Scene newShowcaseMaterialRefractionMaterialScene() {
		final Material material1 = new LambertianMaterial();
		final Material material2 = new RefractionMaterial();
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(5.0F);
		
		final Texture texture11 = new ConstantTexture(Color3F.GRAY);
		final Texture texture12 = new ConstantTexture();
		final Texture texture13 = new ConstantTexture();
		final Texture texture21 = new ConstantTexture(Color3F.WHITE);
		final Texture texture22 = new ConstantTexture();
		final Texture texture23 = new ConstantTexture();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.translate(0.0F, 5.0F, 10.0F);
		
		final
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 7.5F, 0.0F)), "ShowcaseMaterialRefractionMaterial");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FPlane3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(10.0F, 4.0F, 10.0F)), "ShowcaseShape3FProceduralTerrain3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FRectangularCuboid3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FSphere3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FTorus3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FTriangle3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseShape3FTriangleMesh3F");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureBlendTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureBullseyeTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureCheckerboardTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureImageTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureSimplexFractionalBrownianMotionTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureSurfaceNormalTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "ShowcaseTextureUVTexture");
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
		Scene scene = new Scene(new PerezBackground(), new Camera(new Point3F(0.0F, 2.0F, 0.0F)), "Zealot");
		scene.addPrimitive(new Primitive(material1, shape1, texture11, texture12, texture13, matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, texture21, texture22, texture23, matrix2));
		
		return scene;
	}
}