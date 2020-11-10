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

import java.util.List;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Plane3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.RectangularCuboid3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.Torus3F;
import org.dayflower.geometry.TriangleMesh3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.image.Color3F;
import org.dayflower.image.IrregularSpectralCurve;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Texture;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.pbrt.MatteMaterial;
import org.dayflower.scene.pbrt.MetalMaterial;
import org.dayflower.scene.pbrt.MirrorMaterial;
import org.dayflower.scene.pbrt.PlasticMaterial;
import org.dayflower.scene.pbrt.SubstrateMaterial;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.ImageTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;

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
	public static Scene newPBRTBilScene() {
		final Texture textureHjul = ImageTexture.load("./resources/textures/hjul.png");
		final Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));
		final Texture textureTextur = ImageTexture.load("./resources/textures/textur.png");
		
		final Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material21 = new PlasticMaterial(textureTextur, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
		final Material material22 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
		final Material material23 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
		final Material material24 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
		final Material material25 = new SubstrateMaterial(textureHjul, new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
		final Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));
		
		final List<TriangleMesh3F> triangleMeshes = TriangleMesh3F.readWavefrontObject("./resources/models/bil.obj", true);
		
		final Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//T
		final Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
		final Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//R
		final Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//L
		final Shape3F shape21 = triangleMeshes.get(0);
		final Shape3F shape22 = triangleMeshes.get(1);
		final Shape3F shape23 = triangleMeshes.get(2);
		final Shape3F shape24 = triangleMeshes.get(3);
		final Shape3F shape25 = triangleMeshes.get(4);
		final Shape3F shape31 = new Sphere3F();
		
		final Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
		final Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
		final Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
		final Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
		final Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix21 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix22 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix23 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix24 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix25 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(+0.0F, 0.25F, 3.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), Matrix44F.scale(1.0F));
		final Matrix44F matrix31 = Matrix44F.translate(0.0F, 5.0F, 0.0F);
		
		final AreaLight areaLight31 = new DiffuseAreaLight(matrix31, 1, new Color3F(20.0F), shape31, true);
		
		final
		Scene scene = new Scene(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)), "PBRTBil");
		scene.addLight(areaLight31);
		scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(Color3F.GRAY), new ConstantTexture(), matrix11));
		scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), matrix12));
		scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), matrix13));
		scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), matrix14));
		scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), matrix15));
		scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), matrix16));
		scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
		scene.addPrimitive(new Primitive(material22, shape22, new ConstantTexture(), new ConstantTexture(), matrix22));
		scene.addPrimitive(new Primitive(material23, shape23, new ConstantTexture(), new ConstantTexture(), matrix23));
		scene.addPrimitive(new Primitive(material24, shape24, new ConstantTexture(), new ConstantTexture(), matrix24));
		scene.addPrimitive(new Primitive(material25, shape25, new ConstantTexture(), new ConstantTexture(), matrix25));
		scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), matrix31, areaLight31));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newPBRTScene() {
//		final Color3F colorCopperEta = Color3F.maximumTo1(IrregularSpectralCurve.COPPER_ETA.toColorXYZ());
//		final Color3F colorCopperK = Color3F.maximumTo1(IrregularSpectralCurve.COPPER_K.toColorXYZ());
		final Color3F colorGoldEta = Color3F.maximumTo1(IrregularSpectralCurve.GOLD_ETA.toColorXYZ());
		final Color3F colorGoldK = Color3F.maximumTo1(IrregularSpectralCurve.GOLD_K.toColorXYZ());
		
		final Material material1 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), new SimplexFractionalBrownianMotionTexture(new Color3F(0.8F, 0.5F, 0.0F), 5.0F, 0.5F, 16));
//		final Material material2 = new PlasticMaterial(new BullseyeTexture(new Color3F(1.0F, 0.1F, 0.1F), new Color3F(0.5F, 0.1F, 0.1F), new Point3F(0.0F, 10.0F, 0.0F), 2.0F), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(1.0F)), true);
		final Material material2 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
//		final Material material3 = new MetalMaterial(new ConstantTexture(colorCopperEta), new ConstantTexture(colorCopperK), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), true);
//		final Material material3 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), true);
//		final Material material3 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
		final Material material3 = new MirrorMaterial();
		final Material material4 = new MetalMaterial(new ConstantTexture(colorGoldEta), new ConstantTexture(colorGoldK), new ConstantTexture(new Color3F(0.05F)), new ConstantTexture(new Color3F(0.05F)), true);
//		final Material material5 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(Color3F.WHITE), new ConstantTexture(Color3F.WHITE), new ConstantTexture(), new ConstantTexture(), true);
//		final Material material5 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(1.0F)), true);
		final Material material5 = new SubstrateMaterial(new ConstantTexture(new Color3F(1.0F, 0.2F, 0.2F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
//		final Material material5 = new HairMaterial();
		final Material material6 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));
		
		final Shape3F shape1 = new Plane3F();
		final Shape3F shape2 = new Sphere3F(10.0F);
		final Shape3F shape3 = new RectangularCuboid3F(new Point3F(-1.0F), new Point3F(1.0F));
		final Shape3F shape4 = TriangleMesh3F.readWavefrontObject("./resources/models/smoothMonkey2.obj", true, 100.0F).get(0);
		final Shape3F shape5 = new Torus3F();
		final Shape3F shape6 = new Sphere3F();
		
		final Matrix44F matrix1 = Matrix44F.identity();
		final Matrix44F matrix2 = Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 20.0F), Matrix44F.rotateX(AngleF.degrees(0.0F)));
		final Matrix44F matrix3 = Matrix44F.multiply(Matrix44F.translate(-3.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(0.0F)));
		final Matrix44F matrix4 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.01F));
		final Matrix44F matrix5 = Matrix44F.translate(3.0F, 1.25F, 5.0F);
		final Matrix44F matrix6 = Matrix44F.translate(0.0F, 10.0F, 0.0F);
		
		final AreaLight areaLight = new DiffuseAreaLight(matrix6, 1, new Color3F(50.0F), shape6, false);
		
		final
		Scene scene = new Scene(new Camera(new Point3F(0.0F, 2.0F, -10.0F), AngleF.degrees(40.0F)), "PBRT");
		scene.addLight(areaLight);
//		scene.addLight(new DirectionalLight(Color3F.WHITE, new Point3F(0.0F, 0.0F, 0.0F), Vector3F.transform(Matrix44F.rotateX(AngleF.degrees(90.0F)), new Vector3F(0.0F, 0.0F, -1.0F)), 10.0F));
//		scene.addLight(new PointLight(new Point3F(2.0F, 2.0F, 0.0F), new Color3F(1.0F)));
//		scene.addLight(new PointLight(new Point3F(0.0F, 5.0F, 5.0F), new Color3F(1.0F)));
//		scene.addLight(new SpotLight(AngleF.degrees(75.0F), AngleF.degrees(10.0F), new Color3F(1.0F), Matrix44F.identity(), new Point3F(0.0F, 1.0F, 0.0F), new Point3F(0.0F, 1.0F, 1.0F)));
//		scene.addLight(new SpotLight(AngleF.degrees(75.0F), AngleF.degrees(10.0F), new Color3F(1.0F), Matrix44F.multiply(Matrix44F.translate(-5.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(90.0F))), new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F)));
//		scene.addLight(new SpotLight(AngleF.degrees(75.0F), AngleF.degrees(10.0F), new Color3F(1.0F), Matrix44F.multiply(Matrix44F.translate(+5.0F, 1.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(270.0F))), new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, 0.0F, 1.0F)));
		scene.addPrimitive(new Primitive(material1, shape1, new ConstantTexture(), new ConstantTexture(), matrix1));
		scene.addPrimitive(new Primitive(material2, shape2, new ConstantTexture(), new ConstantTexture(), matrix2));
		scene.addPrimitive(new Primitive(material3, shape3, new ConstantTexture(), new ConstantTexture(), matrix3));
		scene.addPrimitive(new Primitive(material4, shape4, new ConstantTexture(), new ConstantTexture(), matrix4));
		scene.addPrimitive(new Primitive(material5, shape5, new ConstantTexture(), new ConstantTexture(), matrix5));
		scene.addPrimitive(new Primitive(material6, shape6, new ConstantTexture(), new ConstantTexture(), matrix6, areaLight));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newPBRTSmoothMonkey2Scene() {
		final Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));
		
		final Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material21 = new PlasticMaterial(new ConstantTexture(new Color3F(0.2F, 0.2F, 1.0F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
//		final Material material21 = new MetalMaterial(new ConstantTexture(Color3F.maximumTo1(Color3F.convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_ETA.toColorXYZ()))), new ConstantTexture(Color3F.maximumTo1(Color3F.convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_K.toColorXYZ()))), new ConstantTexture(new Color3F(0.05F)), new ConstantTexture(new Color3F(0.05F)), true);
//		final Material material21 = new SubstrateMaterial(new ConstantTexture(new Color3F(1.0F, 0.2F, 0.2F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(new Color3F(0.2F)), true);
//		final Material material21 = new GlassMaterial(new ConstantTexture(new Color3F(1.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(new Color3F(1.0F, 1.0F, 0.5F)), new ConstantTexture(), new ConstantTexture(), true);
//		final Material material21 = new MatteMaterial(new ConstantTexture(new Color3F(90.0F)), new ConstantTexture(new Color3F(0.2F, 1.0F, 0.2F)));
//		final Material material21 = new MirrorMaterial();
//		final Material material21 = new UberMaterial();
//		final Material material21 = new HairMaterial();
		
		final Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//T
		final Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
		final Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//R
		final Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//L
		final Shape3F shape21 = TriangleMesh3F.readWavefrontObject("./resources/models/smoothMonkey2.obj", true, 100.0F).get(0);
		
		final Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
		final Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
		final Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
		final Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
		final Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix21 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 2.0F, 5.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.02F));
		
		final
		Scene scene = new Scene(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)), "PBRTSmoothMonkey2");
		scene.addLight(new DiffuseAreaLight(Matrix44F.translate(0.0F, 0.0F, 0.0F), 1, new Color3F(20.0F), new Sphere3F(), true));
		scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), matrix11));
		scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), matrix12));
		scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), matrix13));
		scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), matrix14));
		scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), matrix15));
		scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), matrix16));
		scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
		
		return scene;
	}
	
	/**
	 * Returns a {@link Scene} instance.
	 * 
	 * @return a {@code Scene} instance
	 */
	public static Scene newPBRTZealotScene() {
		final Texture textureLaminate = ImageTexture.load("./resources/textures/laminate.jpg", AngleF.degrees(0.0F), new Vector2F(0.5F, 0.5F));
		
		final Material material11 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material12 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material13 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material14 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material15 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material16 = new MatteMaterial(new ConstantTexture(new Color3F(20.0F)), textureLaminate);
		final Material material21 = new PlasticMaterial(ImageTexture.load("./resources/textures/Zealot_albedo.png"), new ConstantTexture(new Color3F(0.01F)), new ConstantTexture(Color3F.WHITE), true);
		final Material material31 = new MatteMaterial(new ConstantTexture(new Color3F(0.0F)), new ConstantTexture(Color3F.WHITE));
		
		final Shape3F shape11 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, +1.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape12 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +0.0F, -1.0F), new Point3F(1.0F, 0.0F, +0.0F));//T
		final Shape3F shape13 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//F
		final Shape3F shape14 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, -1.0F, +0.0F), new Point3F(1.0F, 0.0F, +0.0F));//B
		final Shape3F shape15 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F, -1.0F));//R
		final Shape3F shape16 = new Plane3F(new Point3F(0.0F, 0.0F, 0.0F), new Point3F(0.0F, +1.0F, +0.0F), new Point3F(0.0F, 0.0F,  1.0F));//L
		final Shape3F shape21 = TriangleMesh3F.readWavefrontObject("./resources/models/Zealot.obj", true).get(0);
		final Shape3F shape31 = new Sphere3F();
		
		final Matrix44F matrix11 = Matrix44F.translate(+0.0F, 0.00F, + 0.0F);
		final Matrix44F matrix12 = Matrix44F.translate(+0.0F, 8.00F, + 0.0F);
		final Matrix44F matrix13 = Matrix44F.translate(+0.0F, 0.00F, + 7.5F);
		final Matrix44F matrix14 = Matrix44F.translate(+0.0F, 0.00F, -10.0F);
		final Matrix44F matrix15 = Matrix44F.translate(+5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix16 = Matrix44F.translate(-5.0F, 0.00F, + 0.0F);
		final Matrix44F matrix21 = Matrix44F.multiply(Matrix44F.multiply(Matrix44F.translate(0.0F, 0.0F, 4.0F), Matrix44F.rotateY(AngleF.degrees(180.0F))), Matrix44F.scale(0.05F));
		final Matrix44F matrix31 = Matrix44F.translate(0.0F, 8.0F, 0.0F);
		
		final AreaLight areaLight31 = new DiffuseAreaLight(matrix31, 1, new Color3F(50.0F), shape31, false);
		
		final
		Scene scene = new Scene(new Camera(new Point3F(0.0F, 4.0F, -10.0F), AngleF.degrees(40.0F)), "PBRTZealot");
		scene.addLight(areaLight31);
		scene.addPrimitive(new Primitive(material11, shape11, new ConstantTexture(), new ConstantTexture(), matrix11));
		scene.addPrimitive(new Primitive(material12, shape12, new ConstantTexture(), new ConstantTexture(), matrix12));
		scene.addPrimitive(new Primitive(material13, shape13, new ConstantTexture(), new ConstantTexture(), matrix13));
		scene.addPrimitive(new Primitive(material14, shape14, new ConstantTexture(), new ConstantTexture(), matrix14));
		scene.addPrimitive(new Primitive(material15, shape15, new ConstantTexture(), new ConstantTexture(), matrix15));
		scene.addPrimitive(new Primitive(material16, shape16, new ConstantTexture(), new ConstantTexture(), matrix16));
		scene.addPrimitive(new Primitive(material21, shape21, new ConstantTexture(), new ConstantTexture(), matrix21));
		scene.addPrimitive(new Primitive(material31, shape31, new ConstantTexture(), new ConstantTexture(), matrix31, areaLight31));
		
		return scene;
	}
}