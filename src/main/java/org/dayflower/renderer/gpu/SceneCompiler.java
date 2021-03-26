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
package org.dayflower.renderer.gpu;

import static org.dayflower.utility.Ints.pack;
import static org.dayflower.utility.Ints.padding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.FunctionTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;

final class SceneCompiler {
	private final AtomicLong timeMillis;
	private final AtomicReference<CompiledScene> compiledScene;
	private final List<AxisAlignedBoundingBox3F> distinctAxisAlignedBoundingBoxes;
	private final List<BlendTexture> distinctBlendTextures;
	private final List<BoundingSphere3F> distinctBoundingSpheres;
	private final List<BoundingVolume3F> distinctBoundingVolumes;
	private final List<BullseyeTexture> distinctBullseyeTextures;
	private final List<CheckerboardTexture> distinctCheckerboardTextures;
	private final List<ClearCoatMaterial> distinctClearCoatMaterials;
	private final List<Cone3F> distinctCones;
	private final List<ConstantTexture> distinctConstantTextures;
	private final List<Cylinder3F> distinctCylinders;
	private final List<DirectionalLight> distinctDirectionalLights;
	private final List<Disk3F> distinctDisks;
	private final List<DisneyMaterial> distinctDisneyMaterials;
	private final List<FunctionTexture> distinctFunctionTextures;
	private final List<GlassMaterial> distinctGlassMaterials;
	private final List<GlossyMaterial> distinctGlossyMaterials;
	private final List<InfiniteBoundingVolume3F> distinctInfiniteBoundingVolumes;
	private final List<LDRImageLight> distinctLDRImageLights;
	private final List<LDRImageTexture> distinctLDRImageTextures;
	private final List<MarbleTexture> distinctMarbleTextures;
	private final List<Material> distinctMaterials;
	private final List<MatteMaterial> distinctMatteMaterials;
	private final List<MetalMaterial> distinctMetalMaterials;
	private final List<MirrorMaterial> distinctMirrorMaterials;
	private final List<Paraboloid3F> distinctParaboloids;
	private final List<Plane3F> distinctPlanes;
	private final List<PlasticMaterial> distinctPlasticMaterials;
	private final List<PointLight> distinctPointLights;
	private final List<Primitive> filteredPrimitives;
	private final List<RectangularCuboid3F> distinctRectangularCuboids;
	private final List<Shape3F> distinctShapes;
	private final List<SimplexFractionalBrownianMotionTexture> distinctSimplexFractionalBrownianMotionTextures;
	private final List<Sphere3F> distinctSpheres;
	private final List<SubstrateMaterial> distinctSubstrateMaterials;
	private final List<SurfaceNormalTexture> distinctSurfaceNormalTextures;
	private final List<Torus3F> distinctToruses;
	private final List<Triangle3F> distinctTriangles;
	private final List<TriangleMesh3F> distinctTriangleMeshes;
	private final List<UVTexture> distinctUVTextures;
	private final Map<AxisAlignedBoundingBox3F, Integer> distinctToOffsetsAxisAlignedBoundingBoxes;
	private final Map<BlendTexture, Integer> distinctToOffsetsBlendTextures;
	private final Map<BoundingSphere3F, Integer> distinctToOffsetsBoundingSpheres;
	private final Map<BullseyeTexture, Integer> distinctToOffsetsBullseyeTextures;
	private final Map<CheckerboardTexture, Integer> distinctToOffsetsCheckerboardTextures;
	private final Map<ClearCoatMaterial, Integer> distinctToOffsetsClearCoatMaterials;
	private final Map<Cone3F, Integer> distinctToOffsetsCones;
	private final Map<ConstantTexture, Integer> distinctToOffsetsConstantTextures;
	private final Map<Cylinder3F, Integer> distinctToOffsetsCylinders;
//	private final Map<DirectionalLight, Integer> distinctToOffsetsDirectionalLights;
	private final Map<Disk3F, Integer> distinctToOffsetsDisks;
	private final Map<DisneyMaterial, Integer> distinctToOffsetsDisneyMaterials;
	private final Map<GlassMaterial, Integer> distinctToOffsetsGlassMaterials;
	private final Map<GlossyMaterial, Integer> distinctToOffsetsGlossyMaterials;
	private final Map<LDRImageLight, Integer> distinctToOffsetsLDRImageLights;
	private final Map<LDRImageTexture, Integer> distinctToOffsetsLDRImageTextures;
	private final Map<MarbleTexture, Integer> distinctToOffsetsMarbleTextures;
	private final Map<MatteMaterial, Integer> distinctToOffsetsMatteMaterials;
	private final Map<MetalMaterial, Integer> distinctToOffsetsMetalMaterials;
	private final Map<MirrorMaterial, Integer> distinctToOffsetsMirrorMaterials;
	private final Map<Paraboloid3F, Integer> distinctToOffsetsParaboloids;
	private final Map<Plane3F, Integer> distinctToOffsetsPlanes;
	private final Map<PlasticMaterial, Integer> distinctToOffsetsPlasticMaterials;
//	private final Map<PointLight, Integer> distinctToOffsetsPointLights;
	private final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboids;
	private final Map<SimplexFractionalBrownianMotionTexture, Integer> distinctToOffsetsSimplexFractionalBrownianMotionTextures;
	private final Map<Sphere3F, Integer> distinctToOffsetsSpheres;
	private final Map<SubstrateMaterial, Integer> distinctToOffsetsSubstrateMaterials;
	private final Map<Torus3F, Integer> distinctToOffsetsToruses;
	private final Map<Triangle3F, Integer> distinctToOffsetsTriangles;
	private final Map<TriangleMesh3F, Integer> distinctToOffsetsTriangleMeshes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneCompiler() {
		this.timeMillis = new AtomicLong();
		this.compiledScene = new AtomicReference<>();
		this.distinctAxisAlignedBoundingBoxes = new ArrayList<>();
		this.distinctBlendTextures = new ArrayList<>();
		this.distinctBoundingSpheres = new ArrayList<>();
		this.distinctBoundingVolumes = new ArrayList<>();
		this.distinctBullseyeTextures = new ArrayList<>();
		this.distinctCheckerboardTextures = new ArrayList<>();
		this.distinctClearCoatMaterials = new ArrayList<>();
		this.distinctCones = new ArrayList<>();
		this.distinctConstantTextures = new ArrayList<>();
		this.distinctCylinders = new ArrayList<>();
		this.distinctDirectionalLights = new ArrayList<>();
		this.distinctDisks = new ArrayList<>();
		this.distinctDisneyMaterials = new ArrayList<>();
		this.distinctFunctionTextures = new ArrayList<>();
		this.distinctGlassMaterials = new ArrayList<>();
		this.distinctGlossyMaterials = new ArrayList<>();
		this.distinctInfiniteBoundingVolumes = new ArrayList<>();
		this.distinctLDRImageLights = new ArrayList<>();
		this.distinctLDRImageTextures = new ArrayList<>();
		this.distinctMarbleTextures = new ArrayList<>();
		this.distinctMaterials = new ArrayList<>();
		this.distinctMatteMaterials = new ArrayList<>();
		this.distinctMetalMaterials = new ArrayList<>();
		this.distinctMirrorMaterials = new ArrayList<>();
		this.distinctParaboloids = new ArrayList<>();
		this.distinctPlanes = new ArrayList<>();
		this.distinctPlasticMaterials = new ArrayList<>();
		this.distinctPointLights = new ArrayList<>();
		this.filteredPrimitives = new ArrayList<>();
		this.distinctRectangularCuboids = new ArrayList<>();
		this.distinctShapes = new ArrayList<>();
		this.distinctSimplexFractionalBrownianMotionTextures = new ArrayList<>();
		this.distinctSpheres = new ArrayList<>();
		this.distinctSubstrateMaterials = new ArrayList<>();
		this.distinctSurfaceNormalTextures = new ArrayList<>();
		this.distinctToruses = new ArrayList<>();
		this.distinctTriangles = new ArrayList<>();
		this.distinctTriangleMeshes = new ArrayList<>();
		this.distinctUVTextures = new ArrayList<>();
		this.distinctToOffsetsAxisAlignedBoundingBoxes = new LinkedHashMap<>();
		this.distinctToOffsetsBlendTextures = new LinkedHashMap<>();
		this.distinctToOffsetsBoundingSpheres = new LinkedHashMap<>();
		this.distinctToOffsetsBullseyeTextures = new LinkedHashMap<>();
		this.distinctToOffsetsCheckerboardTextures = new LinkedHashMap<>();
		this.distinctToOffsetsClearCoatMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsCones = new LinkedHashMap<>();
		this.distinctToOffsetsConstantTextures = new LinkedHashMap<>();
		this.distinctToOffsetsCylinders = new LinkedHashMap<>();
//		this.distinctToOffsetsDirectionalLights = new LinkedHashMap<>();
		this.distinctToOffsetsDisks = new LinkedHashMap<>();
		this.distinctToOffsetsDisneyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlassMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlossyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsLDRImageLights = new LinkedHashMap<>();
		this.distinctToOffsetsLDRImageTextures = new LinkedHashMap<>();
		this.distinctToOffsetsMarbleTextures = new LinkedHashMap<>();
		this.distinctToOffsetsMatteMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMetalMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMirrorMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsParaboloids = new LinkedHashMap<>();
		this.distinctToOffsetsPlanes = new LinkedHashMap<>();
		this.distinctToOffsetsPlasticMaterials = new LinkedHashMap<>();
//		this.distinctToOffsetsPointLights = new LinkedHashMap<>();
		this.distinctToOffsetsRectangularCuboids = new LinkedHashMap<>();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures = new LinkedHashMap<>();
		this.distinctToOffsetsSpheres = new LinkedHashMap<>();
		this.distinctToOffsetsSubstrateMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsToruses = new LinkedHashMap<>();
		this.distinctToOffsetsTriangles = new LinkedHashMap<>();
		this.distinctToOffsetsTriangleMeshes = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene compile(final Scene scene) {
		doReportInit();
		doSetCurrentTimeMillis();
		doClear();
		doFilterAllDistinctBoundingVolumes(scene);
		doFilterAllDistinctLights(scene);
		doFilterAllDistinctMaterials(scene);
		doFilterAllDistinctShapes(scene);
		doFilterAllDistinctTextures(scene);
		doFilterPrimitives(scene);
		doMapAllDistinctBoundingVolumes();
		doMapAllDistinctLights();
		doMapAllDistinctMaterials();
		doMapAllDistinctShapes();
		doMapAllDistinctTextures();
		doBuildCompiledScene(scene);
		doClear();
		doSetElapsedTimeMillis();
		doReportDone();
		
		return this.compiledScene.getAndSet(null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doFilterPrimitive(final Primitive primitive) {
		if(!this.distinctBoundingVolumes.contains(primitive.getBoundingVolume())) {
			return false;
		}
		
		if(!this.distinctMaterials.contains(primitive.getMaterial())) {
			return false;
		}
		
		if(!this.distinctShapes.contains(primitive.getShape())) {
			return false;
		}
		
		return true;
	}
	
	private int doFindTextureOffset(final Texture texture) {
		if(texture instanceof BlendTexture) {
			return this.distinctToOffsetsBlendTextures.get(texture).intValue();
		} else if(texture instanceof BullseyeTexture) {
			return this.distinctToOffsetsBullseyeTextures.get(texture).intValue();
		} else if(texture instanceof CheckerboardTexture) {
			return this.distinctToOffsetsCheckerboardTextures.get(texture).intValue();
		} else if(texture instanceof ConstantTexture) {
			return this.distinctToOffsetsConstantTextures.get(texture).intValue();
		} else if(texture instanceof FunctionTexture) {
			return 0;
		} else if(texture instanceof LDRImageTexture) {
			return this.distinctToOffsetsLDRImageTextures.get(texture).intValue();
		} else if(texture instanceof MarbleTexture) {
			return this.distinctToOffsetsMarbleTextures.get(texture).intValue();
		} else if(texture instanceof SimplexFractionalBrownianMotionTexture) {
			return this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.get(texture).intValue();
		} else if(texture instanceof SurfaceNormalTexture) {
			return 0;
		} else if(texture instanceof UVTexture) {
			return 0;
		} else {
			return 0;
		}
	}
	
	private void doBuildCompiledScene(final Scene scene) {
//		Retrieve the float[] for all BoundingVolume3F instances:
		final float[] boundingVolume3FAxisAlignedBoundingBox3FArray = Floats.toArray(this.distinctAxisAlignedBoundingBoxes, axisAlignedBoundingBox -> axisAlignedBoundingBox.toArray(), 1);
		final float[] boundingVolume3FBoundingSphere3FArray = Floats.toArray(this.distinctBoundingSpheres, boundingSphere -> boundingSphere.toArray(), 1);
		
//		Retrieve the float[] for the Camera instance:
		final float[] cameraArray = scene.getCamera().toArray();
		
//		Retrieve the float[] for all Light instances:
		final float[] lightDirectionalLightArray = Floats.toArray(this.distinctDirectionalLights, directionalLight -> directionalLight.toArray(), 1);
		final float[] lightLDRImageLightArray = Floats.toArray(this.distinctLDRImageLights, lDRImageLight -> lDRImageLight.toArray(), 1);
		final float[] lightPointLightArray = Floats.toArray(this.distinctPointLights, pointLight -> pointLight.toArray(), 1);
		
//		Retrieve the int[] for all Light instances:
		final int[] lightLDRImageLightOffsetArray = new int[this.distinctLDRImageLights.size()];
		
//		Retrieve the int[] for all Material instances:
		final int[] materialClearCoatMaterialArray = Ints.toArray(this.distinctClearCoatMaterials, clearCoatMaterial -> clearCoatMaterial.toArray(), 1);
		final int[] materialDisneyMaterialArray = Ints.toArray(this.distinctDisneyMaterials, disneyMaterial -> disneyMaterial.toArray(), 1);
		final int[] materialGlassMaterialArray = Ints.toArray(this.distinctGlassMaterials, glassMaterial -> glassMaterial.toArray(), 1);
		final int[] materialGlossyMaterialArray = Ints.toArray(this.distinctGlossyMaterials, glossyMaterial -> glossyMaterial.toArray(), 1);
		final int[] materialMatteMaterialArray = Ints.toArray(this.distinctMatteMaterials, matteMaterial -> matteMaterial.toArray(), 1);
		final int[] materialMetalMaterialArray = Ints.toArray(this.distinctMetalMaterials, metalMaterial -> metalMaterial.toArray(), 1);
		final int[] materialMirrorMaterialArray = Ints.toArray(this.distinctMirrorMaterials, mirrorMaterial -> mirrorMaterial.toArray(), 1);
		final int[] materialPlasticMaterialArray = Ints.toArray(this.distinctPlasticMaterials, plasticMaterial -> plasticMaterial.toArray(), 1);
		final int[] materialSubstrateMaterialArray = Ints.toArray(this.distinctSubstrateMaterials, substrateMaterial -> substrateMaterial.toArray(), 1);
		
//		Retrieve the float[] for the Matrix44F instances:
		final float[] matrix44FArray = Floats.toArray(this.filteredPrimitives, primitive -> primitive.getTransform().toArray(), 1);
		
//		Retrieve the float[] for all Shape3F instances:
		final float[] shape3FCone3FArray = Floats.toArray(this.distinctCones, cone -> cone.toArray(), 1);
		final float[] shape3FCylinder3FArray = Floats.toArray(this.distinctCylinders, cylinder -> cylinder.toArray(), 1);
		final float[] shape3FDisk3FArray = Floats.toArray(this.distinctDisks, disk -> disk.toArray(), 1);
		final float[] shape3FParaboloid3FArray = Floats.toArray(this.distinctParaboloids, paraboloid -> paraboloid.toArray(), 1);
		final float[] shape3FPlane3FArray = Floats.toArray(this.distinctPlanes, plane -> plane.toArray(), 1);
		final float[] shape3FRectangularCuboid3FArray = Floats.toArray(this.distinctRectangularCuboids, rectangularCuboid -> rectangularCuboid.toArray(), 1);
		final float[] shape3FSphere3FArray = Floats.toArray(this.distinctSpheres, sphere -> sphere.toArray(), 1);
		final float[] shape3FTorus3FArray = Floats.toArray(this.distinctToruses, torus -> torus.toArray(), 1);
		final float[] shape3FTriangle3FArray = Floats.toArray(this.distinctTriangles, triangle -> triangle.toArray(), 1);
		
//		Retrieve the int[] for all Shape3F instances:
		final int[] shape3FTriangleMesh3FArray = Ints.toArray(this.distinctTriangleMeshes, triangleMesh -> triangleMesh.toArray(), 1);
		
//		Retrieve the float[] for all Texture instances:
		final float[] textureBlendTextureArray = Floats.toArray(this.distinctBlendTextures, blendTexture -> blendTexture.toArray(), 1);
		final float[] textureBullseyeTextureArray = Floats.toArray(this.distinctBullseyeTextures, bullseyeTexture -> bullseyeTexture.toArray(), 1);
		final float[] textureCheckerboardTextureArray = Floats.toArray(this.distinctCheckerboardTextures, checkerboardTexture -> checkerboardTexture.toArray(), 1);
		final float[] textureConstantTextureArray = Floats.toArray(this.distinctConstantTextures, constantTexture -> constantTexture.toArray(), 1);
		final float[] textureLDRImageTextureArray = Floats.toArray(this.distinctLDRImageTextures, lDRImageTexture -> lDRImageTexture.toArray(), 1);
		final float[] textureMarbleTextureArray = Floats.toArray(this.distinctMarbleTextures, marbleTexture -> marbleTexture.toArray(), 1);
		final float[] textureSimplexFractionalBrownianMotionTextureArray = Floats.toArray(this.distinctSimplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture -> simplexFractionalBrownianMotionTexture.toArray(), 1);
		
//		Retrieve the int[] for all primitives:
		final int[] primitiveArray = Ints.toArray(this.filteredPrimitives, primitive -> primitive.toArray(), 1);
		
//		Populate the float[] or int[] with data:
		doPopulateLightLDRImageLightOffsetArray(lightLDRImageLightOffsetArray);
		doPopulateMaterialClearCoatMaterialArrayWithTextures(materialClearCoatMaterialArray);
		doPopulateMaterialDisneyMaterialArrayWithTextures(materialDisneyMaterialArray);
		doPopulateMaterialGlassMaterialArrayWithTextures(materialGlassMaterialArray);
		doPopulateMaterialGlossyMaterialArrayWithTextures(materialGlossyMaterialArray);
		doPopulateMaterialMatteMaterialArrayWithTextures(materialMatteMaterialArray);
		doPopulateMaterialMetalMaterialArrayWithTextures(materialMetalMaterialArray);
		doPopulateMaterialMirrorMaterialArrayWithTextures(materialMirrorMaterialArray);
		doPopulateMaterialPlasticMaterialArrayWithTextures(materialPlasticMaterialArray);
		doPopulateMaterialSubstrateMaterialArrayWithTextures(materialSubstrateMaterialArray);
		doPopulatePrimitiveArrayWithBoundingVolumes(primitiveArray);
		doPopulatePrimitiveArrayWithMaterials(primitiveArray);
		doPopulatePrimitiveArrayWithShapes(primitiveArray);
		doPopulateShape3FTriangleMesh3FArray(shape3FTriangleMesh3FArray);
		doPopulateTextureBlendTextureArrayWithTextures(textureBlendTextureArray);
		doPopulateTextureBullseyeTextureArrayWithTextures(textureBullseyeTextureArray);
		doPopulateTextureCheckerboardTextureArrayWithTextures(textureCheckerboardTextureArray);
		
		final
		CompiledScene compiledScene = new CompiledScene();
		compiledScene.setBoundingVolume3FAxisAlignedBoundingBox3FArray(boundingVolume3FAxisAlignedBoundingBox3FArray);
		compiledScene.setBoundingVolume3FBoundingSphere3FArray(boundingVolume3FBoundingSphere3FArray);
		compiledScene.setCameraArray(cameraArray);
		compiledScene.setLightDirectionalLightArray(lightDirectionalLightArray);
		compiledScene.setLightLDRImageLightArray(lightLDRImageLightArray);
		compiledScene.setLightLDRImageLightOffsetArray(lightLDRImageLightOffsetArray);
		compiledScene.setLightPointLightArray(lightPointLightArray);
		compiledScene.setMaterialClearCoatMaterialArray(materialClearCoatMaterialArray);
		compiledScene.setMaterialDisneyMaterialArray(materialDisneyMaterialArray);
		compiledScene.setMaterialGlassMaterialArray(materialGlassMaterialArray);
		compiledScene.setMaterialGlossyMaterialArray(materialGlossyMaterialArray);
		compiledScene.setMaterialMatteMaterialArray(materialMatteMaterialArray);
		compiledScene.setMaterialMetalMaterialArray(materialMetalMaterialArray);
		compiledScene.setMaterialMirrorMaterialArray(materialMirrorMaterialArray);
		compiledScene.setMaterialPlasticMaterialArray(materialPlasticMaterialArray);
		compiledScene.setMaterialSubstrateMaterialArray(materialSubstrateMaterialArray);
		compiledScene.setMatrix44FArray(matrix44FArray);
		compiledScene.setPrimitiveArray(primitiveArray);
		compiledScene.setShape3FCone3FArray(shape3FCone3FArray);
		compiledScene.setShape3FCylinder3FArray(shape3FCylinder3FArray);
		compiledScene.setShape3FDisk3FArray(shape3FDisk3FArray);
		compiledScene.setShape3FParaboloid3FArray(shape3FParaboloid3FArray);
		compiledScene.setShape3FPlane3FArray(shape3FPlane3FArray);
		compiledScene.setShape3FRectangularCuboid3FArray(shape3FRectangularCuboid3FArray);
		compiledScene.setShape3FSphere3FArray(shape3FSphere3FArray);
		compiledScene.setShape3FTorus3FArray(shape3FTorus3FArray);
		compiledScene.setShape3FTriangle3FArray(shape3FTriangle3FArray);
		compiledScene.setShape3FTriangleMesh3FArray(shape3FTriangleMesh3FArray);
		compiledScene.setTextureBlendTextureArray(textureBlendTextureArray);
		compiledScene.setTextureBullseyeTextureArray(textureBullseyeTextureArray);
		compiledScene.setTextureCheckerboardTextureArray(textureCheckerboardTextureArray);
		compiledScene.setTextureConstantTextureArray(textureConstantTextureArray);
		compiledScene.setTextureLDRImageTextureArray(textureLDRImageTextureArray);
		compiledScene.setTextureMarbleTextureArray(textureMarbleTextureArray);
		compiledScene.setTextureSimplexFractionalBrownianMotionTextureArray(textureSimplexFractionalBrownianMotionTextureArray);
		
		this.compiledScene.set(compiledScene);
	}
	
	private void doClear() {
		this.distinctAxisAlignedBoundingBoxes.clear();
		this.distinctBlendTextures.clear();
		this.distinctBoundingSpheres.clear();
		this.distinctBoundingVolumes.clear();
		this.distinctBullseyeTextures.clear();
		this.distinctCheckerboardTextures.clear();
		this.distinctClearCoatMaterials.clear();
		this.distinctCones.clear();
		this.distinctConstantTextures.clear();
		this.distinctCylinders.clear();
		this.distinctDirectionalLights.clear();
		this.distinctDisks.clear();
		this.distinctDisneyMaterials.clear();
		this.distinctFunctionTextures.clear();
		this.distinctGlassMaterials.clear();
		this.distinctGlossyMaterials.clear();
		this.distinctInfiniteBoundingVolumes.clear();
		this.distinctLDRImageLights.clear();
		this.distinctLDRImageTextures.clear();
		this.distinctMarbleTextures.clear();
		this.distinctMaterials.clear();
		this.distinctMatteMaterials.clear();
		this.distinctMetalMaterials.clear();
		this.distinctMirrorMaterials.clear();
		this.distinctParaboloids.clear();
		this.distinctPlanes.clear();
		this.distinctPlasticMaterials.clear();
		this.distinctPointLights.clear();
		this.filteredPrimitives.clear();
		this.distinctRectangularCuboids.clear();
		this.distinctShapes.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSpheres.clear();
		this.distinctSubstrateMaterials.clear();
		this.distinctSurfaceNormalTextures.clear();
		this.distinctToruses.clear();
		this.distinctTriangles.clear();
		this.distinctTriangleMeshes.clear();
		this.distinctUVTextures.clear();
		this.distinctToOffsetsAxisAlignedBoundingBoxes.clear();
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBoundingSpheres.clear();
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsClearCoatMaterials.clear();
		this.distinctToOffsetsCones.clear();
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsCylinders.clear();
//		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDisks.clear();
		this.distinctToOffsetsDisneyMaterials.clear();
		this.distinctToOffsetsGlassMaterials.clear();
		this.distinctToOffsetsGlossyMaterials.clear();
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsLDRImageTextures.clear();
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsMatteMaterials.clear();
		this.distinctToOffsetsMetalMaterials.clear();
		this.distinctToOffsetsMirrorMaterials.clear();
		this.distinctToOffsetsParaboloids.clear();
		this.distinctToOffsetsPlanes.clear();
		this.distinctToOffsetsPlasticMaterials.clear();
//		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsRectangularCuboids.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
		this.distinctToOffsetsSpheres.clear();
		this.distinctToOffsetsSubstrateMaterials.clear();
		this.distinctToOffsetsToruses.clear();
		this.distinctToOffsetsTriangles.clear();
		this.distinctToOffsetsTriangleMeshes.clear();
	}
	
	private void doFilterAllDistinctBoundingVolumes(final Scene scene) {
		this.distinctAxisAlignedBoundingBoxes.addAll(NodeFilter.filterAllDistinct(scene, AxisAlignedBoundingBox3F.class));
		this.distinctBoundingSpheres.addAll(NodeFilter.filterAllDistinct(scene, BoundingSphere3F.class));
		this.distinctInfiniteBoundingVolumes.addAll(NodeFilter.filterAllDistinct(scene, InfiniteBoundingVolume3F.class));
		this.distinctBoundingVolumes.addAll(this.distinctAxisAlignedBoundingBoxes);
		this.distinctBoundingVolumes.addAll(this.distinctBoundingSpheres);
		this.distinctBoundingVolumes.addAll(this.distinctInfiniteBoundingVolumes);
	}
	
	private void doFilterAllDistinctLights(final Scene scene) {
		this.distinctDirectionalLights.addAll(NodeFilter.filterAllDistinct(scene, DirectionalLight.class));
		this.distinctLDRImageLights.addAll(NodeFilter.filterAllDistinct(scene, LDRImageLight.class));
		this.distinctPointLights.addAll(NodeFilter.filterAllDistinct(scene, PointLight.class));
	}
	
	private void doFilterAllDistinctMaterials(final Scene scene) {
		this.distinctClearCoatMaterials.addAll(NodeFilter.filterAllDistinct(scene, ClearCoatMaterial.class));
		this.distinctDisneyMaterials.addAll(NodeFilter.filterAllDistinct(scene, DisneyMaterial.class));
		this.distinctGlassMaterials.addAll(NodeFilter.filterAllDistinct(scene, GlassMaterial.class));
		this.distinctGlossyMaterials.addAll(NodeFilter.filterAllDistinct(scene, GlossyMaterial.class));
		this.distinctMatteMaterials.addAll(NodeFilter.filterAllDistinct(scene, MatteMaterial.class));
		this.distinctMetalMaterials.addAll(NodeFilter.filterAllDistinct(scene, MetalMaterial.class));
		this.distinctMirrorMaterials.addAll(NodeFilter.filterAllDistinct(scene, MirrorMaterial.class));
		this.distinctPlasticMaterials.addAll(NodeFilter.filterAllDistinct(scene, PlasticMaterial.class));
		this.distinctSubstrateMaterials.addAll(NodeFilter.filterAllDistinct(scene, SubstrateMaterial.class));
		this.distinctMaterials.addAll(this.distinctClearCoatMaterials);
		this.distinctMaterials.addAll(this.distinctDisneyMaterials);
		this.distinctMaterials.addAll(this.distinctGlassMaterials);
		this.distinctMaterials.addAll(this.distinctGlossyMaterials);
		this.distinctMaterials.addAll(this.distinctMatteMaterials);
		this.distinctMaterials.addAll(this.distinctMetalMaterials);
		this.distinctMaterials.addAll(this.distinctMirrorMaterials);
		this.distinctMaterials.addAll(this.distinctPlasticMaterials);
		this.distinctMaterials.addAll(this.distinctSubstrateMaterials);
	}
	
	private void doFilterAllDistinctShapes(final Scene scene) {
		this.distinctCones.addAll(NodeFilter.filterAllDistinct(scene, Cone3F.class));
		this.distinctCylinders.addAll(NodeFilter.filterAllDistinct(scene, Cylinder3F.class));
		this.distinctDisks.addAll(NodeFilter.filterAllDistinct(scene, Disk3F.class));
		this.distinctParaboloids.addAll(NodeFilter.filterAllDistinct(scene, Paraboloid3F.class));
		this.distinctPlanes.addAll(NodeFilter.filterAllDistinct(scene, Plane3F.class));
		this.distinctRectangularCuboids.addAll(NodeFilter.filterAllDistinct(scene, RectangularCuboid3F.class));
		this.distinctSpheres.addAll(NodeFilter.filterAllDistinct(scene, Sphere3F.class));
		this.distinctToruses.addAll(NodeFilter.filterAllDistinct(scene, Torus3F.class));
		this.distinctTriangles.addAll(NodeFilter.filterAllDistinct(scene, Triangle3F.class));
		this.distinctTriangleMeshes.addAll(NodeFilter.filterAllDistinct(scene, TriangleMesh3F.class));
		this.distinctShapes.addAll(this.distinctCones);
		this.distinctShapes.addAll(this.distinctCylinders);
		this.distinctShapes.addAll(this.distinctDisks);
		this.distinctShapes.addAll(this.distinctParaboloids);
		this.distinctShapes.addAll(this.distinctPlanes);
		this.distinctShapes.addAll(this.distinctRectangularCuboids);
		this.distinctShapes.addAll(this.distinctSpheres);
		this.distinctShapes.addAll(this.distinctToruses);
		this.distinctShapes.addAll(this.distinctTriangles);
		this.distinctShapes.addAll(this.distinctTriangleMeshes);
	}
	
	private void doFilterAllDistinctTextures(final Scene scene) {
		this.distinctBlendTextures.addAll(NodeFilter.filterAllDistinct(scene, BlendTexture.class));
		this.distinctBullseyeTextures.addAll(NodeFilter.filterAllDistinct(scene, BullseyeTexture.class));
		this.distinctCheckerboardTextures.addAll(NodeFilter.filterAllDistinct(scene, CheckerboardTexture.class));
		this.distinctConstantTextures.addAll(NodeFilter.filterAllDistinct(scene, ConstantTexture.class));
		this.distinctFunctionTextures.addAll(NodeFilter.filterAllDistinct(scene, FunctionTexture.class));
		this.distinctLDRImageTextures.addAll(NodeFilter.filterAllDistinct(scene, LDRImageTexture.class));
		this.distinctMarbleTextures.addAll(NodeFilter.filterAllDistinct(scene, MarbleTexture.class));
		this.distinctSimplexFractionalBrownianMotionTextures.addAll(NodeFilter.filterAllDistinct(scene, SimplexFractionalBrownianMotionTexture.class));
		this.distinctSurfaceNormalTextures.addAll(NodeFilter.filterAllDistinct(scene, SurfaceNormalTexture.class));
		this.distinctUVTextures.addAll(NodeFilter.filterAllDistinct(scene, UVTexture.class));
	}
	
	private void doFilterPrimitives(final Scene scene) {
		this.filteredPrimitives.addAll(scene.getPrimitives().stream().filter(this::doFilterPrimitive).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
	}
	
	private void doMapAllDistinctBoundingVolumes() {
		this.distinctToOffsetsAxisAlignedBoundingBoxes.putAll(NodeFilter.mapDistinctToOffsets(this.distinctAxisAlignedBoundingBoxes, AxisAlignedBoundingBox3F.ARRAY_LENGTH));
		this.distinctToOffsetsBoundingSpheres.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBoundingSpheres, BoundingSphere3F.ARRAY_LENGTH));
	}
	
	private void doMapAllDistinctLights() {
//		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights, DirectionalLight.ARRAY_LENGTH));
		this.distinctToOffsetsLDRImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageLights, lDRImageLight -> lDRImageLight.getArrayLength()));
//		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights, PointLight.ARRAY_LENGTH));
	}
	
	private void doMapAllDistinctMaterials() {
		this.distinctToOffsetsClearCoatMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctClearCoatMaterials, ClearCoatMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsDisneyMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisneyMaterials, DisneyMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsGlassMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctGlassMaterials, GlassMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsGlossyMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctGlossyMaterials, GlossyMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsMatteMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMatteMaterials, MatteMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsMetalMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMetalMaterials, MetalMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsMirrorMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMirrorMaterials, MirrorMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsPlasticMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlasticMaterials, PlasticMaterial.ARRAY_LENGTH));
		this.distinctToOffsetsSubstrateMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSubstrateMaterials, SubstrateMaterial.ARRAY_LENGTH));
	}
	
	private void doMapAllDistinctShapes() {
		this.distinctToOffsetsCones.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCones, Cone3F.ARRAY_LENGTH));
		this.distinctToOffsetsCylinders.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCylinders, Cylinder3F.ARRAY_LENGTH));
		this.distinctToOffsetsDisks.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisks, Disk3F.ARRAY_LENGTH));
		this.distinctToOffsetsParaboloids.putAll(NodeFilter.mapDistinctToOffsets(this.distinctParaboloids, Paraboloid3F.ARRAY_LENGTH));
		this.distinctToOffsetsPlanes.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlanes, Plane3F.ARRAY_LENGTH));
		this.distinctToOffsetsRectangularCuboids.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangularCuboids, RectangularCuboid3F.ARRAY_LENGTH));
		this.distinctToOffsetsSpheres.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpheres, Sphere3F.ARRAY_LENGTH));
		this.distinctToOffsetsToruses.putAll(NodeFilter.mapDistinctToOffsets(this.distinctToruses, Torus3F.ARRAY_LENGTH));
		this.distinctToOffsetsTriangles.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangles, Triangle3F.ARRAY_LENGTH));
		this.distinctToOffsetsTriangleMeshes.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangleMeshes, triangleMesh -> triangleMesh.getArrayLength()));
	}
	
	private void doMapAllDistinctTextures() {
		this.distinctToOffsetsBlendTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBlendTextures, BlendTexture.ARRAY_LENGTH));
		this.distinctToOffsetsBullseyeTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBullseyeTextures, BullseyeTexture.ARRAY_LENGTH));
		this.distinctToOffsetsCheckerboardTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCheckerboardTextures, CheckerboardTexture.ARRAY_LENGTH));
		this.distinctToOffsetsConstantTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctConstantTextures, ConstantTexture.ARRAY_LENGTH));
		this.distinctToOffsetsLDRImageTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageTextures, lDRImageTexture -> lDRImageTexture.getArrayLength()));
		this.distinctToOffsetsMarbleTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMarbleTextures, MarbleTexture.ARRAY_LENGTH));
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexFractionalBrownianMotionTextures, SimplexFractionalBrownianMotionTexture.ARRAY_LENGTH));
	}
	
	private void doPopulateLightLDRImageLightOffsetArray(final int[] lightLDRImageLightOffsetArray) {
		for(int i = 0; i < this.distinctLDRImageLights.size(); i++) {
			final LDRImageLight lDRImageLight = this.distinctLDRImageLights.get(i);
			
			final int offset = this.distinctToOffsetsLDRImageLights.get(lDRImageLight).intValue();
			
			lightLDRImageLightOffsetArray[i] = offset;
		}
	}
	
	private void doPopulateMaterialClearCoatMaterialArrayWithTextures(final int[] materialClearCoatMaterialArray) {
		for(int i = 0; i < this.distinctClearCoatMaterials.size(); i++) {
			final ClearCoatMaterial clearCoatMaterial = this.distinctClearCoatMaterials.get(i);
			
			final Texture textureEmission = clearCoatMaterial.getTextureEmission();
			final Texture textureKD = clearCoatMaterial.getTextureKD();
			final Texture textureKS = clearCoatMaterial.getTextureKS();
			
			final int materialClearCoatMaterialArrayTextureEmission = i * ClearCoatMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialClearCoatMaterialArrayTextureKD = i * ClearCoatMaterial.ARRAY_LENGTH + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_D;
			final int materialClearCoatMaterialArrayTextureKS = i * ClearCoatMaterial.ARRAY_LENGTH + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_S;
			
			materialClearCoatMaterialArray[materialClearCoatMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialClearCoatMaterialArray[materialClearCoatMaterialArrayTextureKD] = pack(textureKD.getID(), doFindTextureOffset(textureKD));
			materialClearCoatMaterialArray[materialClearCoatMaterialArrayTextureKS] = pack(textureKS.getID(), doFindTextureOffset(textureKS));
		}
	}
	
	private void doPopulateMaterialDisneyMaterialArrayWithTextures(final int[] materialDisneyMaterialArray) {
		for(int i = 0; i < this.distinctDisneyMaterials.size(); i++) {
			final DisneyMaterial disneyMaterial = this.distinctDisneyMaterials.get(i);
			
			final Texture textureEmission = disneyMaterial.getTextureEmission();
			final Texture textureAnisotropic = disneyMaterial.getTextureAnisotropic();
			final Texture textureClearCoat = disneyMaterial.getTextureClearCoat();
			final Texture textureClearCoatGloss = disneyMaterial.getTextureClearCoatGloss();
			final Texture textureColor = disneyMaterial.getTextureColor();
			final Texture textureDiffuseTransmission = disneyMaterial.getTextureDiffuseTransmission();
			final Texture textureEta = disneyMaterial.getTextureEta();
			final Texture textureFlatness = disneyMaterial.getTextureFlatness();
			final Texture textureMetallic = disneyMaterial.getTextureMetallic();
			final Texture textureRoughness = disneyMaterial.getTextureRoughness();
			final Texture textureScatterDistance = disneyMaterial.getTextureScatterDistance();
			final Texture textureSheen = disneyMaterial.getTextureSheen();
			final Texture textureSheenTint = disneyMaterial.getTextureSheenTint();
			final Texture textureSpecularTint = disneyMaterial.getTextureSpecularTint();
			final Texture textureSpecularTransmission = disneyMaterial.getTextureSpecularTransmission();
			
			final int materialDisneyMaterialArrayTextureEmission = i * DisneyMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialDisneyMaterialArrayTextureAnisotropic = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ANISOTROPIC;
			final int materialDisneyMaterialArrayTextureClearCoat = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_CLEAR_COAT;
			final int materialDisneyMaterialArrayTextureClearCoatGloss = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_CLEAR_COAT_GLOSS;
			final int materialDisneyMaterialArrayTextureColor = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_COLOR;
			final int materialDisneyMaterialArrayTextureDiffuseTransmission = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_DIFFUSE_TRANSMISSION;
			final int materialDisneyMaterialArrayTextureEta = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ETA;
			final int materialDisneyMaterialArrayTextureFlatness = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_FLATNESS;
			final int materialDisneyMaterialArrayTextureMetallic = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_METALLIC;
			final int materialDisneyMaterialArrayTextureRoughness = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS;
			final int materialDisneyMaterialArrayTextureScatterDistance = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SCATTER_DISTANCE;
			final int materialDisneyMaterialArrayTextureSheen = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SHEEN;
			final int materialDisneyMaterialArrayTextureSheenTint = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SHEEN_TINT;
			final int materialDisneyMaterialArrayTextureSpecularTint = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SPECULAR_TINT;
			final int materialDisneyMaterialArrayTextureSpecularTransmission = i * DisneyMaterial.ARRAY_LENGTH + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SPECULAR_TRANSMISSION;
			
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureAnisotropic] = pack(textureAnisotropic.getID(), doFindTextureOffset(textureAnisotropic));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureClearCoat] = pack(textureClearCoat.getID(), doFindTextureOffset(textureClearCoat));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureClearCoatGloss] = pack(textureClearCoatGloss.getID(), doFindTextureOffset(textureClearCoatGloss));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureColor] = pack(textureColor.getID(), doFindTextureOffset(textureColor));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureDiffuseTransmission] = pack(textureDiffuseTransmission.getID(), doFindTextureOffset(textureDiffuseTransmission));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureEta] = pack(textureEta.getID(), doFindTextureOffset(textureEta));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureFlatness] = pack(textureFlatness.getID(), doFindTextureOffset(textureFlatness));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureMetallic] = pack(textureMetallic.getID(), doFindTextureOffset(textureMetallic));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureRoughness] = pack(textureRoughness.getID(), doFindTextureOffset(textureRoughness));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureScatterDistance] = pack(textureScatterDistance.getID(), doFindTextureOffset(textureScatterDistance));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSheen] = pack(textureSheen.getID(), doFindTextureOffset(textureSheen));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSheenTint] = pack(textureSheenTint.getID(), doFindTextureOffset(textureSheenTint));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSpecularTint] = pack(textureSpecularTint.getID(), doFindTextureOffset(textureSpecularTint));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSpecularTransmission] = pack(textureSpecularTransmission.getID(), doFindTextureOffset(textureSpecularTransmission));
		}
	}
	
	private void doPopulateMaterialGlassMaterialArrayWithTextures(final int[] materialGlassMaterialArray) {
		for(int i = 0; i < this.distinctGlassMaterials.size(); i++) {
			final GlassMaterial glassMaterial = this.distinctGlassMaterials.get(i);
			
			final Texture textureEmission = glassMaterial.getTextureEmission();
			final Texture textureEta = glassMaterial.getTextureEta();
			final Texture textureKR = glassMaterial.getTextureKR();
			final Texture textureKT = glassMaterial.getTextureKT();
			final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
			
			final int materialGlassMaterialArrayTextureEmission = i * GlassMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialGlassMaterialArrayTextureEta = i * GlassMaterial.ARRAY_LENGTH + GlassMaterial.ARRAY_OFFSET_TEXTURE_ETA;
			final int materialGlassMaterialArrayTextureKR = i * GlassMaterial.ARRAY_LENGTH + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_R;
			final int materialGlassMaterialArrayTextureKT = i * GlassMaterial.ARRAY_LENGTH + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_T;
			final int materialGlassMaterialArrayTextureRoughnessU = i * GlassMaterial.ARRAY_LENGTH + GlassMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U;
			final int materialGlassMaterialArrayTextureRoughnessV = i * GlassMaterial.ARRAY_LENGTH + GlassMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V;
			
			materialGlassMaterialArray[materialGlassMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureEta] = pack(textureEta.getID(), doFindTextureOffset(textureEta));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureKR] = pack(textureKR.getID(), doFindTextureOffset(textureKR));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureKT] = pack(textureKT.getID(), doFindTextureOffset(textureKT));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureRoughnessU] = pack(textureRoughnessU.getID(), doFindTextureOffset(textureRoughnessU));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureRoughnessV] = pack(textureRoughnessV.getID(), doFindTextureOffset(textureRoughnessV));
		}
	}
	
	private void doPopulateMaterialGlossyMaterialArrayWithTextures(final int[] materialGlossyMaterialArray) {
		for(int i = 0; i < this.distinctGlossyMaterials.size(); i++) {
			final GlossyMaterial glossyMaterial = this.distinctGlossyMaterials.get(i);
			
			final Texture textureEmission = glossyMaterial.getTextureEmission();
			final Texture textureKR = glossyMaterial.getTextureKR();
			final Texture textureRoughness = glossyMaterial.getTextureRoughness();
			
			final int materialGlossyMaterialArrayTextureEmission = i * GlossyMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialGlossyMaterialArrayTextureKR = i * GlossyMaterial.ARRAY_LENGTH + GlossyMaterial.ARRAY_OFFSET_TEXTURE_K_R;
			final int materialGlossyMaterialArrayTextureRoughness = i * GlossyMaterial.ARRAY_LENGTH + GlossyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS;
			
			materialGlossyMaterialArray[materialGlossyMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialGlossyMaterialArray[materialGlossyMaterialArrayTextureKR] = pack(textureKR.getID(), doFindTextureOffset(textureKR));
			materialGlossyMaterialArray[materialGlossyMaterialArrayTextureRoughness] = pack(textureRoughness.getID(), doFindTextureOffset(textureRoughness));
		}
	}
	
	private void doPopulateMaterialMatteMaterialArrayWithTextures(final int[] materialMatteMaterialArray) {
		for(int i = 0; i < this.distinctMatteMaterials.size(); i++) {
			final MatteMaterial matteMaterial = this.distinctMatteMaterials.get(i);
			
			final Texture textureEmission = matteMaterial.getTextureEmission();
			final Texture textureAngle = matteMaterial.getTextureAngle();
			final Texture textureKD = matteMaterial.getTextureKD();
			
			final int materialMatteMaterialArrayTextureEmission = i * MatteMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialMatteMaterialArrayTextureAngle = i * MatteMaterial.ARRAY_LENGTH + MatteMaterial.ARRAY_OFFSET_TEXTURE_ANGLE;
			final int materialMatteMaterialArrayTextureKD = i * MatteMaterial.ARRAY_LENGTH + MatteMaterial.ARRAY_OFFSET_TEXTURE_K_D;
			
			materialMatteMaterialArray[materialMatteMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialMatteMaterialArray[materialMatteMaterialArrayTextureAngle] = pack(textureAngle.getID(), doFindTextureOffset(textureAngle));
			materialMatteMaterialArray[materialMatteMaterialArrayTextureKD] = pack(textureKD.getID(), doFindTextureOffset(textureKD));
		}
	}
	
	private void doPopulateMaterialMetalMaterialArrayWithTextures(final int[] materialMetalaterialArray) {
		for(int i = 0; i < this.distinctMetalMaterials.size(); i++) {
			final MetalMaterial metalMaterial = this.distinctMetalMaterials.get(i);
			
			final Texture textureEmission = metalMaterial.getTextureEmission();
			final Texture textureEta = metalMaterial.getTextureEta();
			final Texture textureK = metalMaterial.getTextureK();
			final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
			
			final int materialMetalMaterialArrayTextureEmission = i * MetalMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialMetalMaterialArrayTextureEta = i * MetalMaterial.ARRAY_LENGTH + MetalMaterial.ARRAY_OFFSET_TEXTURE_ETA;
			final int materialMetalMaterialArrayTextureK = i * MetalMaterial.ARRAY_LENGTH + MetalMaterial.ARRAY_OFFSET_TEXTURE_K;
			final int materialMetalMaterialArrayTextureRoughnessU = i * MetalMaterial.ARRAY_LENGTH + MetalMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U;
			final int materialMetalMaterialArrayTextureRoughnessV = i * MetalMaterial.ARRAY_LENGTH + MetalMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V;
			
			materialMetalaterialArray[materialMetalMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialMetalaterialArray[materialMetalMaterialArrayTextureEta] = pack(textureEta.getID(), doFindTextureOffset(textureEta));
			materialMetalaterialArray[materialMetalMaterialArrayTextureK] = pack(textureK.getID(), doFindTextureOffset(textureK));
			materialMetalaterialArray[materialMetalMaterialArrayTextureRoughnessU] = pack(textureRoughnessU.getID(), doFindTextureOffset(textureRoughnessU));
			materialMetalaterialArray[materialMetalMaterialArrayTextureRoughnessV] = pack(textureRoughnessV.getID(), doFindTextureOffset(textureRoughnessV));
		}
	}
	
	private void doPopulateMaterialMirrorMaterialArrayWithTextures(final int[] materialMirrorMaterialArray) {
		for(int i = 0; i < this.distinctMirrorMaterials.size(); i++) {
			final MirrorMaterial mirrorMaterial = this.distinctMirrorMaterials.get(i);
			
			final Texture textureEmission = mirrorMaterial.getTextureEmission();
			final Texture textureKR = mirrorMaterial.getTextureKR();
			
			final int materialMirrorMaterialArrayTextureEmission = i * MirrorMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialMirrorMaterialArrayTextureKR = i * MirrorMaterial.ARRAY_LENGTH + MirrorMaterial.ARRAY_OFFSET_TEXTURE_K_R;
			
			materialMirrorMaterialArray[materialMirrorMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialMirrorMaterialArray[materialMirrorMaterialArrayTextureKR] = pack(textureKR.getID(), doFindTextureOffset(textureKR));
		}
	}
	
	private void doPopulateMaterialPlasticMaterialArrayWithTextures(final int[] materialPlasticMaterialArray) {
		for(int i = 0; i < this.distinctPlasticMaterials.size(); i++) {
			final PlasticMaterial plasticMaterial = this.distinctPlasticMaterials.get(i);
			
			final Texture textureEmission = plasticMaterial.getTextureEmission();
			final Texture textureKD = plasticMaterial.getTextureKD();
			final Texture textureKS = plasticMaterial.getTextureKS();
			final Texture textureRoughness = plasticMaterial.getTextureRoughness();
			
			final int materialPlasticMaterialArrayTextureEmission = i * PlasticMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialPlasticMaterialArrayTextureKD = i * PlasticMaterial.ARRAY_LENGTH + PlasticMaterial.ARRAY_OFFSET_TEXTURE_K_D;
			final int materialPlasticMaterialArrayTextureKS = i * PlasticMaterial.ARRAY_LENGTH + PlasticMaterial.ARRAY_OFFSET_TEXTURE_K_S;
			final int materialPlasticMaterialArrayTextureRoughness = i * PlasticMaterial.ARRAY_LENGTH + PlasticMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS;
			
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureKD] = pack(textureKD.getID(), doFindTextureOffset(textureKD));
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureKS] = pack(textureKS.getID(), doFindTextureOffset(textureKS));
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureRoughness] = pack(textureRoughness.getID(), doFindTextureOffset(textureRoughness));
		}
	}
	
	private void doPopulateMaterialSubstrateMaterialArrayWithTextures(final int[] materialSubstrateMaterialArray) {
		for(int i = 0; i < this.distinctSubstrateMaterials.size(); i++) {
			final SubstrateMaterial substrateMaterial = this.distinctSubstrateMaterials.get(i);
			
			final Texture textureEmission = substrateMaterial.getTextureEmission();
			final Texture textureKD = substrateMaterial.getTextureKD();
			final Texture textureKS = substrateMaterial.getTextureKS();
			final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
			
			final int materialSubstrateMaterialArrayTextureEmission = i * SubstrateMaterial.ARRAY_LENGTH + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
			final int materialSubstrateMaterialArrayTextureKD = i * SubstrateMaterial.ARRAY_LENGTH + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_K_D;
			final int materialSubstrateMaterialArrayTextureKS = i * SubstrateMaterial.ARRAY_LENGTH + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_K_S;
			final int materialSubstrateMaterialArrayTextureRoughnessU = i * SubstrateMaterial.ARRAY_LENGTH + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U;
			final int materialSubstrateMaterialArrayTextureRoughnessV = i * SubstrateMaterial.ARRAY_LENGTH + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V;
			
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureEmission] = pack(textureEmission.getID(), doFindTextureOffset(textureEmission));
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureKD] = pack(textureKD.getID(), doFindTextureOffset(textureKD));
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureKS] = pack(textureKS.getID(), doFindTextureOffset(textureKS));
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureRoughnessU] = pack(textureRoughnessU.getID(), doFindTextureOffset(textureRoughnessU));
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureRoughnessV] = pack(textureRoughnessV.getID(), doFindTextureOffset(textureRoughnessV));
		}
	}
	
	private void doPopulatePrimitiveArrayWithBoundingVolumes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final Primitive primitive = this.filteredPrimitives.get(i);
			
			final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
			
			final int primitiveArrayBoundingVolumeOffset = i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			
			if(boundingVolume instanceof AxisAlignedBoundingBox3F) {
				primitiveArray[primitiveArrayBoundingVolumeOffset] = this.distinctToOffsetsAxisAlignedBoundingBoxes.get(boundingVolume).intValue();
			} else if(boundingVolume instanceof BoundingSphere3F) {
				primitiveArray[primitiveArrayBoundingVolumeOffset] = this.distinctToOffsetsBoundingSpheres.get(boundingVolume).intValue();
			} else if(boundingVolume instanceof InfiniteBoundingVolume3F) {
				primitiveArray[primitiveArrayBoundingVolumeOffset] = 0;
			}
		}
	}
	
	private void doPopulatePrimitiveArrayWithMaterials(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final Primitive primitive = this.filteredPrimitives.get(i);
			
			final Material material = primitive.getMaterial();
			
			final int primitiveArrayMaterialOffset = i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET;
			
			if(material instanceof ClearCoatMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsClearCoatMaterials.get(material).intValue();
			} else if(material instanceof DisneyMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsDisneyMaterials.get(material).intValue();
			} else if(material instanceof GlassMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsGlassMaterials.get(material).intValue();
			} else if(material instanceof GlossyMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsGlossyMaterials.get(material).intValue();
			} else if(material instanceof MatteMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsMatteMaterials.get(material).intValue();
			} else if(material instanceof MetalMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsMetalMaterials.get(material).intValue();
			} else if(material instanceof MirrorMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsMirrorMaterials.get(material).intValue();
			} else if(material instanceof PlasticMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsPlasticMaterials.get(material).intValue();
			} else if(material instanceof SubstrateMaterial) {
				primitiveArray[primitiveArrayMaterialOffset] = this.distinctToOffsetsSubstrateMaterials.get(material).intValue();
			}
		}
	}
	
	private void doPopulatePrimitiveArrayWithShapes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final Primitive primitive = this.filteredPrimitives.get(i);
			
			final Shape3F shape = primitive.getShape();
			
			final int primitiveArrayShapeOffset = i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			if(shape instanceof Cone3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsCones.get(shape).intValue();
			} else if(shape instanceof Cylinder3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsCylinders.get(shape).intValue();
			} else if(shape instanceof Disk3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsDisks.get(shape).intValue();
			} else if(shape instanceof Paraboloid3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsParaboloids.get(shape).intValue();
			} else if(shape instanceof Plane3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsPlanes.get(shape).intValue();
			} else if(shape instanceof RectangularCuboid3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsRectangularCuboids.get(shape).intValue();
			} else if(shape instanceof Sphere3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsSpheres.get(shape).intValue();
			} else if(shape instanceof Torus3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsToruses.get(shape).intValue();
			} else if(shape instanceof Triangle3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsTriangles.get(shape).intValue();
			} else if(shape instanceof TriangleMesh3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsTriangleMeshes.get(shape).intValue();
			} else {
				primitiveArray[primitiveArrayShapeOffset] = 0;
			}
		}
	}
	
	private void doPopulateShape3FTriangleMesh3FArray(final int[] shape3FTriangleMesh3FArray) {
		for(int i = 0; i < this.distinctTriangleMeshes.size(); i++) {
			final TriangleMesh3F triangleMesh = this.distinctTriangleMeshes.get(i);
			
			final List<BoundingVolume3F> boundingVolumes = triangleMesh.getBoundingVolumes();
			final List<Triangle3F> triangles = triangleMesh.getTriangles();
			
			final int shape3FTriangleMesh3FArrayOffset = this.distinctToOffsetsTriangleMeshes.get(triangleMesh).intValue();
			final int shape3FTriangleMesh3FArrayLength = shape3FTriangleMesh3FArrayOffset + triangleMesh.getArrayLength();
			
			for(int j = shape3FTriangleMesh3FArrayOffset; j < shape3FTriangleMesh3FArrayLength;) {
				final int boundingVolumeOffset = j + TriangleMesh3F.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				final int idOffset = j + TriangleMesh3F.ARRAY_OFFSET_ID;
				final int id = shape3FTriangleMesh3FArray[idOffset];
				
				shape3FTriangleMesh3FArray[boundingVolumeOffset] = this.distinctToOffsetsAxisAlignedBoundingBoxes.get(boundingVolumes.get(shape3FTriangleMesh3FArray[boundingVolumeOffset])).intValue();
				
				if(id == TriangleMesh3F.ID_LEAF_B_V_H_NODE) {
					final int triangleCountOffset = j + TriangleMesh3F.ARRAY_OFFSET_TRIANGLE_COUNT;
					final int triangleCount = shape3FTriangleMesh3FArray[triangleCountOffset];
					final int triangleStartOffset = triangleCountOffset + 1;
					
					for(int k = triangleStartOffset; k < triangleStartOffset + triangleCount; k++) {
						shape3FTriangleMesh3FArray[k] = this.distinctToOffsetsTriangles.get(triangles.get(shape3FTriangleMesh3FArray[k])).intValue();
					}
					
					j += 4 + triangleCount + padding(4 + triangleCount);
				} else if(id == TriangleMesh3F.ID_TREE_B_V_H_NODE) {
					j += 8;
				} else {
					break;
				}
			}
		}
	}
	
	private void doPopulateTextureBlendTextureArrayWithTextures(final float[] textureBlendTextureArray) {
		for(int i = 0; i < this.distinctBlendTextures.size(); i++) {
			final BlendTexture blendTexture = this.distinctBlendTextures.get(i);
			
			final Texture textureA = blendTexture.getTextureA();
			final Texture textureB = blendTexture.getTextureB();
			
			final int textureBlendTextureArrayTextureAOffset = i * BlendTexture.ARRAY_LENGTH + BlendTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureBlendTextureArrayTextureBOffset = i * BlendTexture.ARRAY_LENGTH + BlendTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureBlendTextureArray[textureBlendTextureArrayTextureAOffset] = pack(textureA.getID(), doFindTextureOffset(textureA));
			textureBlendTextureArray[textureBlendTextureArrayTextureBOffset] = pack(textureB.getID(), doFindTextureOffset(textureB));
		}
	}
	
	private void doPopulateTextureBullseyeTextureArrayWithTextures(final float[] textureBullseyeTextureArray) {
		for(int i = 0; i < this.distinctBullseyeTextures.size(); i++) {
			final BullseyeTexture bullseyeTexture = this.distinctBullseyeTextures.get(i);
			
			final Texture textureA = bullseyeTexture.getTextureA();
			final Texture textureB = bullseyeTexture.getTextureB();
			
			final int textureBullseyeTextureArrayTextureAOffset = i * BullseyeTexture.ARRAY_LENGTH + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureBullseyeTextureArrayTextureBOffset = i * BullseyeTexture.ARRAY_LENGTH + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureBullseyeTextureArray[textureBullseyeTextureArrayTextureAOffset] = pack(textureA.getID(), doFindTextureOffset(textureA));
			textureBullseyeTextureArray[textureBullseyeTextureArrayTextureBOffset] = pack(textureB.getID(), doFindTextureOffset(textureB));
		}
	}
	
	private void doPopulateTextureCheckerboardTextureArrayWithTextures(final float[] textureCheckerboardTextureArray) {
		for(int i = 0; i < this.distinctCheckerboardTextures.size(); i++) {
			final CheckerboardTexture checkerboardTexture = this.distinctCheckerboardTextures.get(i);
			
			final Texture textureA = checkerboardTexture.getTextureA();
			final Texture textureB = checkerboardTexture.getTextureB();
			
			final int textureCheckerboardTextureArrayTextureAOffset = i * CheckerboardTexture.ARRAY_LENGTH + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureCheckerboardTextureArrayTextureBOffset = i * CheckerboardTexture.ARRAY_LENGTH + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureCheckerboardTextureArray[textureCheckerboardTextureArrayTextureAOffset] = pack(textureA.getID(), doFindTextureOffset(textureA));
			textureCheckerboardTextureArray[textureCheckerboardTextureArrayTextureBOffset] = pack(textureB.getID(), doFindTextureOffset(textureB));
		}
	}
	
	private void doReportDone() {
		System.out.println("- Compilation took " + this.timeMillis.get() + " milliseconds.");
	}
	
	@SuppressWarnings("static-method")
	private void doReportInit() {
		System.out.println("Compiling...");
	}
	
	private void doSetCurrentTimeMillis() {
		this.timeMillis.set(System.currentTimeMillis());
	}
	
	private void doSetElapsedTimeMillis() {
		this.timeMillis.getAndAccumulate(System.currentTimeMillis(), (currentTimeMillisA, currentTimeMillisB) -> currentTimeMillisB - currentTimeMillisA);
	}
}