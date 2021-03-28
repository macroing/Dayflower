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

import java.util.Objects;

import org.dayflower.scene.Primitive;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;

final class CompiledScene {
	private float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	private float[] boundingVolume3FBoundingSphere3FArray;
	private float[] cameraArray;
	private float[] lightDirectionalLightArray;
	private float[] lightLDRImageLightArray;
	private float[] lightPerezLightArray;
	private float[] lightPointLightArray;
	private float[] lightSpotLightArray;
	private float[] matrix44FArray;
	private float[] shape3FCone3FArray;
	private float[] shape3FCylinder3FArray;
	private float[] shape3FDisk3FArray;
	private float[] shape3FParaboloid3FArray;
	private float[] shape3FPlane3FArray;
	private float[] shape3FRectangularCuboid3FArray;
	private float[] shape3FSphere3FArray;
	private float[] shape3FTorus3FArray;
	private float[] shape3FTriangle3FArray;
	private float[] textureBlendTextureArray;
	private float[] textureBullseyeTextureArray;
	private float[] textureCheckerboardTextureArray;
	private float[] textureConstantTextureArray;
	private float[] textureLDRImageTextureArray;
	private float[] textureMarbleTextureArray;
	private float[] textureSimplexFractionalBrownianMotionTextureArray;
	private int[] lightLDRImageLightOffsetArray;
	private int[] lightPerezLightOffsetArray;
	private int[] materialClearCoatMaterialArray;
	private int[] materialDisneyMaterialArray;
	private int[] materialGlassMaterialArray;
	private int[] materialGlossyMaterialArray;
	private int[] materialMatteMaterialArray;
	private int[] materialMetalMaterialArray;
	private int[] materialMirrorMaterialArray;
	private int[] materialPlasticMaterialArray;
	private int[] materialSubstrateMaterialArray;
	private int[] primitiveArray;
	private int[] shape3FTriangleMesh3FArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene() {
		setBoundingVolume3FAxisAlignedBoundingBox3FArray(new float[1]);
		setBoundingVolume3FBoundingSphere3FArray(new float[1]);
		setCameraArray(new float[1]);
		setLightDirectionalLightArray(new float[1]);
		setLightLDRImageLightArray(new float[1]);
		setLightLDRImageLightOffsetArray(new int[1]);
		setLightPerezLightArray(new float[1]);
		setLightPerezLightOffsetArray(new int[1]);
		setLightPointLightArray(new float[1]);
		setLightSpotLightArray(new float[1]);
		setMaterialClearCoatMaterialArray(new int[1]);
		setMaterialDisneyMaterialArray(new int[1]);
		setMaterialGlassMaterialArray(new int[1]);
		setMaterialGlossyMaterialArray(new int[1]);
		setMaterialMatteMaterialArray(new int[1]);
		setMaterialMetalMaterialArray(new int[1]);
		setMaterialMirrorMaterialArray(new int[1]);
		setMaterialPlasticMaterialArray(new int[1]);
		setMaterialSubstrateMaterialArray(new int[1]);
		setMatrix44FArray(new float[1]);
		setPrimitiveArray(new int[1]);
		setShape3FCone3FArray(new float[1]);
		setShape3FCylinder3FArray(new float[1]);
		setShape3FDisk3FArray(new float[1]);
		setShape3FParaboloid3FArray(new float[1]);
		setShape3FPlane3FArray(new float[1]);
		setShape3FRectangularCuboid3FArray(new float[1]);
		setShape3FSphere3FArray(new float[1]);
		setShape3FTorus3FArray(new float[1]);
		setShape3FTriangle3FArray(new float[1]);
		setShape3FTriangleMesh3FArray(new int[1]);
		setTextureBlendTextureArray(new float[1]);
		setTextureBullseyeTextureArray(new float[1]);
		setTextureCheckerboardTextureArray(new float[1]);
		setTextureConstantTextureArray(new float[1]);
		setTextureLDRImageTextureArray(new float[1]);
		setTextureMarbleTextureArray(new float[1]);
		setTextureSimplexFractionalBrownianMotionTextureArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float[] getBoundingVolume3FAxisAlignedBoundingBox3FArray() {
		return this.boundingVolume3FAxisAlignedBoundingBox3FArray;
	}
	
	public float[] getBoundingVolume3FBoundingSphere3FArray() {
		return this.boundingVolume3FBoundingSphere3FArray;
	}
	
	public float[] getCameraArray() {
		return this.cameraArray;
	}
	
	public float[] getLightDirectionalLightArray() {
		return this.lightDirectionalLightArray;
	}
	
	public float[] getLightLDRImageLightArray() {
		return this.lightLDRImageLightArray;
	}
	
	public float[] getLightPerezLightArray() {
		return this.lightPerezLightArray;
	}
	
	public float[] getLightPointLightArray() {
		return this.lightPointLightArray;
	}
	
	public float[] getLightSpotLightArray() {
		return this.lightSpotLightArray;
	}
	
	public float[] getMatrix44FArray() {
		return this.matrix44FArray;
	}
	
	public float[] getShape3FCone3FArray() {
		return this.shape3FCone3FArray;
	}
	
	public float[] getShape3FCylinder3FArray() {
		return this.shape3FCylinder3FArray;
	}
	
	public float[] getShape3FDisk3FArray() {
		return this.shape3FDisk3FArray;
	}
	
	public float[] getShape3FParaboloid3FArray() {
		return this.shape3FParaboloid3FArray;
	}
	
	public float[] getShape3FPlane3FArray() {
		return this.shape3FPlane3FArray;
	}
	
	public float[] getShape3FRectangularCuboid3FArray() {
		return this.shape3FRectangularCuboid3FArray;
	}
	
	public float[] getShape3FSphere3FArray() {
		return this.shape3FSphere3FArray;
	}
	
	public float[] getShape3FTorus3FArray() {
		return this.shape3FTorus3FArray;
	}
	
	public float[] getShape3FTriangle3FArray() {
		return this.shape3FTriangle3FArray;
	}
	
	public float[] getTextureBlendTextureArray() {
		return this.textureBlendTextureArray;
	}
	
	public float[] getTextureBullseyeTextureArray() {
		return this.textureBullseyeTextureArray;
	}
	
	public float[] getTextureCheckerboardTextureArray() {
		return this.textureCheckerboardTextureArray;
	}
	
	public float[] getTextureConstantTextureArray() {
		return this.textureConstantTextureArray;
	}
	
	public float[] getTextureLDRImageTextureArray() {
		return this.textureLDRImageTextureArray;
	}
	
	public float[] getTextureMarbleTextureArray() {
		return this.textureMarbleTextureArray;
	}
	
	public float[] getTextureSimplexFractionalBrownianMotionTextureArray() {
		return this.textureSimplexFractionalBrownianMotionTextureArray;
	}
	
	public int getLightDirectionalLightCount() {
		return this.lightDirectionalLightArray.length % DirectionalLight.ARRAY_LENGTH == 0 ? this.lightDirectionalLightArray.length / DirectionalLight.ARRAY_LENGTH : 0;
	}
	
	public int getLightLDRImageLightCount() {
		/*
		 * The float[] of an LDRImageLight contains padding, such that its length is evenly divisible by 8. If no LDRImageLight has been added, the length of the float[] is 1, which is not evenly divisible by 8.
		 * Aparapi requires all arrays to have a length of at least 1. So the length of 'lightLDRImageLightOffsetArray' cannot be 0, which could have been used to determine the count in all cases.
		 */
		
		return this.lightLDRImageLightArray.length % 8 == 0 ? this.lightLDRImageLightOffsetArray.length : 0;
	}
	
	public int getLightPerezLightCount() {
		/*
		 * The float[] of a PerezLight contains padding, such that its length is evenly divisible by 8. If no PerezLight has been added, the length of the float[] is 1, which is not evenly divisible by 8.
		 * Aparapi requires all arrays to have a length of at least 1. So the length of 'lightPerezLightOffsetArray' cannot be 0, which could have been used to determine the count in all cases.
		 */
		
		return this.lightPerezLightArray.length % 8 == 0 ? this.lightPerezLightOffsetArray.length : 0;
	}
	
	public int getLightPointLightCount() {
		return this.lightPointLightArray.length % PointLight.ARRAY_LENGTH == 0 ? this.lightPointLightArray.length / PointLight.ARRAY_LENGTH : 0;
	}
	
	public int getLightSpotLightCount() {
		return this.lightSpotLightArray.length % SpotLight.ARRAY_LENGTH == 0 ? this.lightSpotLightArray.length / SpotLight.ARRAY_LENGTH : 0;
	}
	
	public int getPrimitiveCount() {
		return this.primitiveArray.length / Primitive.ARRAY_LENGTH;
	}
	
	public int[] getLightLDRImageLightOffsetArray() {
		return this.lightLDRImageLightOffsetArray;
	}
	
	public int[] getLightPerezLightOffsetArray() {
		return this.lightPerezLightOffsetArray;
	}
	
	public int[] getMaterialClearCoatMaterialArray() {
		return this.materialClearCoatMaterialArray;
	}
	
	public int[] getMaterialDisneyMaterialArray() {
		return this.materialDisneyMaterialArray;
	}
	
	public int[] getMaterialGlassMaterialArray() {
		return this.materialGlassMaterialArray;
	}
	
	public int[] getMaterialGlossyMaterialArray() {
		return this.materialGlossyMaterialArray;
	}
	
	public int[] getMaterialMatteMaterialArray() {
		return this.materialMatteMaterialArray;
	}
	
	public int[] getMaterialMetalMaterialArray() {
		return this.materialMetalMaterialArray;
	}
	
	public int[] getMaterialMirrorMaterialArray() {
		return this.materialMirrorMaterialArray;
	}
	
	public int[] getMaterialPlasticMaterialArray() {
		return this.materialPlasticMaterialArray;
	}
	
	public int[] getMaterialSubstrateMaterialArray() {
		return this.materialSubstrateMaterialArray;
	}
	
	public int[] getPrimitiveArray() {
		return this.primitiveArray;
	}
	
	public int[] getShape3FTriangleMesh3FArray() {
		return this.shape3FTriangleMesh3FArray;
	}
	
	public void setBoundingVolume3FAxisAlignedBoundingBox3FArray(final float[] boundingVolume3FAxisAlignedBoundingBox3FArray) {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = Objects.requireNonNull(boundingVolume3FAxisAlignedBoundingBox3FArray, "boundingVolume3FAxisAlignedBoundingBox3FArray == null");
	}
	
	public void setBoundingVolume3FBoundingSphere3FArray(final float[] boundingVolume3FBoundingSphere3FArray) {
		this.boundingVolume3FBoundingSphere3FArray = Objects.requireNonNull(boundingVolume3FBoundingSphere3FArray, "boundingVolume3FBoundingSphere3FArray == null");
	}
	
	public void setCameraArray(final float[] cameraArray) {
		this.cameraArray = Objects.requireNonNull(cameraArray, "cameraArray == null");
	}
	
	public void setLightDirectionalLightArray(final float[] lightDirectionalLightArray) {
		this.lightDirectionalLightArray = Objects.requireNonNull(lightDirectionalLightArray, "lightDirectionalLightArray == null");
	}
	
	public void setLightLDRImageLightArray(final float[] lightLDRImageLightArray) {
		this.lightLDRImageLightArray = Objects.requireNonNull(lightLDRImageLightArray, "lightLDRImageLightArray == null");
	}
	
	public void setLightLDRImageLightOffsetArray(final int[] lightLDRImageLightOffsetArray) {
		this.lightLDRImageLightOffsetArray = Objects.requireNonNull(lightLDRImageLightOffsetArray, "lightLDRImageLightOffsetArray == null");
	}
	
	public void setLightPerezLightArray(final float[] lightPerezLightArray) {
		this.lightPerezLightArray = Objects.requireNonNull(lightPerezLightArray, "lightPerezLightArray == null");
	}
	
	public void setLightPerezLightOffsetArray(final int[] lightPerezLightOffsetArray) {
		this.lightPerezLightOffsetArray = Objects.requireNonNull(lightPerezLightOffsetArray, "lightPerezLightOffsetArray == null");
	}
	
	public void setLightPointLightArray(final float[] lightPointLightArray) {
		this.lightPointLightArray = Objects.requireNonNull(lightPointLightArray, "lightPointLightArray == null");
	}
	
	public void setLightSpotLightArray(final float[] lightSpotLightArray) {
		this.lightSpotLightArray = Objects.requireNonNull(lightSpotLightArray, "lightSpotLightArray == null");
	}
	
	public void setMaterialClearCoatMaterialArray(final int[] materialClearCoatMaterialArray) {
		this.materialClearCoatMaterialArray = Objects.requireNonNull(materialClearCoatMaterialArray, "materialClearCoatMaterialArray == null");
	}
	
	public void setMaterialDisneyMaterialArray(final int[] materialDisneyMaterialArray) {
		this.materialDisneyMaterialArray = Objects.requireNonNull(materialDisneyMaterialArray, "materialDisneyMaterialArray == null");
	}
	
	public void setMaterialGlassMaterialArray(final int[] materialGlassMaterialArray) {
		this.materialGlassMaterialArray = Objects.requireNonNull(materialGlassMaterialArray, "materialGlassMaterialArray == null");
	}
	
	public void setMaterialGlossyMaterialArray(final int[] materialGlossyMaterialArray) {
		this.materialGlossyMaterialArray = Objects.requireNonNull(materialGlossyMaterialArray, "materialGlossyMaterialArray == null");
	}
	
	public void setMaterialMatteMaterialArray(final int[] materialMatteMaterialArray) {
		this.materialMatteMaterialArray = Objects.requireNonNull(materialMatteMaterialArray, "materialMatteMaterialArray == null");
	}
	
	public void setMaterialMetalMaterialArray(final int[] materialMetalMaterialArray) {
		this.materialMetalMaterialArray = Objects.requireNonNull(materialMetalMaterialArray, "materialMetalMaterialArray == null");
	}
	
	public void setMaterialMirrorMaterialArray(final int[] materialMirrorMaterialArray) {
		this.materialMirrorMaterialArray = Objects.requireNonNull(materialMirrorMaterialArray, "materialMirrorMaterialArray == null");
	}
	
	public void setMaterialPlasticMaterialArray(final int[] materialPlasticMaterialArray) {
		this.materialPlasticMaterialArray = Objects.requireNonNull(materialPlasticMaterialArray, "materialPlasticMaterialArray == null");
	}
	
	public void setMaterialSubstrateMaterialArray(final int[] materialSubstrateMaterialArray) {
		this.materialSubstrateMaterialArray = Objects.requireNonNull(materialSubstrateMaterialArray, "materialSubstrateMaterialArray == null");
	}
	
	public void setMatrix44FArray(final float[] matrix44FArray) {
		this.matrix44FArray = Objects.requireNonNull(matrix44FArray, "matrix44FArray == null");
	}
	
	public void setPrimitiveArray(final int[] primitiveArray) {
		this.primitiveArray = Objects.requireNonNull(primitiveArray, "primitiveArray == null");
	}
	
	public void setShape3FCone3FArray(final float[] shape3FCone3FArray) {
		this.shape3FCone3FArray = Objects.requireNonNull(shape3FCone3FArray, "shape3FCone3FArray == null");
	}
	
	public void setShape3FCylinder3FArray(final float[] shape3FCylinder3FArray) {
		this.shape3FCylinder3FArray = Objects.requireNonNull(shape3FCylinder3FArray, "shape3FCylinder3FArray == null");
	}
	
	public void setShape3FDisk3FArray(final float[] shape3FDisk3FArray) {
		this.shape3FDisk3FArray = Objects.requireNonNull(shape3FDisk3FArray, "shape3FDisk3FArray == null");
	}
	
	public void setShape3FParaboloid3FArray(final float[] shape3FParaboloid3FArray) {
		this.shape3FParaboloid3FArray = Objects.requireNonNull(shape3FParaboloid3FArray, "shape3FParaboloid3FArray == null");
	}
	
	public void setShape3FPlane3FArray(final float[] shape3FPlane3FArray) {
		this.shape3FPlane3FArray = Objects.requireNonNull(shape3FPlane3FArray, "shape3FPlane3FArray == null");
	}
	
	public void setShape3FRectangularCuboid3FArray(final float[] shape3FRectangularCuboid3FArray) {
		this.shape3FRectangularCuboid3FArray = Objects.requireNonNull(shape3FRectangularCuboid3FArray, "shape3FRectangularCuboid3FArray == null");
	}
	
	public void setShape3FSphere3FArray(final float[] shape3FSphere3FArray) {
		this.shape3FSphere3FArray = Objects.requireNonNull(shape3FSphere3FArray, "shape3FSphere3FArray == null");
	}
	
	public void setShape3FTorus3FArray(final float[] shape3FTorus3FArray) {
		this.shape3FTorus3FArray = Objects.requireNonNull(shape3FTorus3FArray, "shape3FTorus3FArray == null");
	}
	
	public void setShape3FTriangle3FArray(final float[] shape3FTriangle3FArray) {
		this.shape3FTriangle3FArray = Objects.requireNonNull(shape3FTriangle3FArray, "shape3FTriangle3FArray == null");
	}
	
	public void setShape3FTriangleMesh3FArray(final int[] shape3FTriangleMesh3FArray) {
		this.shape3FTriangleMesh3FArray = Objects.requireNonNull(shape3FTriangleMesh3FArray, "shape3FTriangleMesh3FArray == null");
	}
	
	public void setTextureBlendTextureArray(final float[] textureBlendTextureArray) {
		this.textureBlendTextureArray = Objects.requireNonNull(textureBlendTextureArray, "textureBlendTextureArray == null");
	}
	
	public void setTextureBullseyeTextureArray(final float[] textureBullseyeTextureArray) {
		this.textureBullseyeTextureArray = Objects.requireNonNull(textureBullseyeTextureArray, "textureBullseyeTextureArray == null");
	}
	
	public void setTextureCheckerboardTextureArray(final float[] textureCheckerboardTextureArray) {
		this.textureCheckerboardTextureArray = Objects.requireNonNull(textureCheckerboardTextureArray, "textureCheckerboardTextureArray == null");
	}
	
	public void setTextureConstantTextureArray(final float[] textureConstantTextureArray) {
		this.textureConstantTextureArray = Objects.requireNonNull(textureConstantTextureArray, "textureConstantTextureArray == null");
	}
	
	public void setTextureLDRImageTextureArray(final float[] textureLDRImageTextureArray) {
		this.textureLDRImageTextureArray = Objects.requireNonNull(textureLDRImageTextureArray, "textureLDRImageTextureArray == null");
	}
	
	public void setTextureMarbleTextureArray(final float[] textureMarbleTextureArray) {
		this.textureMarbleTextureArray = Objects.requireNonNull(textureMarbleTextureArray, "textureMarbleTextureArray == null");
	}
	
	public void setTextureSimplexFractionalBrownianMotionTextureArray(final float[] textureSimplexFractionalBrownianMotionTextureArray) {
		this.textureSimplexFractionalBrownianMotionTextureArray = Objects.requireNonNull(textureSimplexFractionalBrownianMotionTextureArray, "textureSimplexFractionalBrownianMotionTextureArray == null");
	}
}