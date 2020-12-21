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
package org.dayflower.renderer.gpu;

import java.util.Objects;

import org.dayflower.scene.Primitive;

final class CompiledScene {
	private float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	private float[] boundingVolume3FBoundingSphere3FArray;
	private float[] cameraArray;
	private float[] materialMetalSmallPTMaterialArray;
	private float[] matrix44FArray;
	private float[] shape3FPlane3FArray;
	private float[] shape3FRectangularCuboid3FArray;
	private float[] shape3FSphere3FArray;
	private float[] shape3FTorus3FArray;
	private float[] shape3FTriangle3FArray;
	private float[] textureBlendTextureArray;
	private float[] textureBullseyeTextureArray;
	private float[] textureCheckerboardTextureArray;
	private float[] textureImageTextureArray;
	private float[] textureMarbleTextureArray;
	private float[] textureSimplexFractionalBrownianMotionTextureArray;
	private int[] materialClearCoatSmallPTMaterialArray;
	private int[] materialGlassSmallPTMaterialArray;
	private int[] materialMatteSmallPTMaterialArray;
	private int[] materialMirrorSmallPTMaterialArray;
	private int[] primitiveArray;
	private int[] shape3FTriangleMesh3FArray;
	private int[] textureConstantTextureArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene() {
		setBoundingVolume3FAxisAlignedBoundingBox3FArray(new float[1]);
		setBoundingVolume3FBoundingSphere3FArray(new float[1]);
		setCameraArray(new float[1]);
		setMaterialClearCoatSmallPTMaterialArray(new int[1]);
		setMaterialGlassSmallPTMaterialArray(new int[1]);
		setMaterialMatteSmallPTMaterialArray(new int[1]);
		setMaterialMetalSmallPTMaterialArray(new float[1]);
		setMaterialMirrorSmallPTMaterialArray(new int[1]);
		setMatrix44FArray(new float[1]);
		setPrimitiveArray(new int[1]);
		setShape3FPlane3FArray(new float[1]);
		setShape3FRectangularCuboid3FArray(new float[1]);
		setShape3FSphere3FArray(new float[1]);
		setShape3FTorus3FArray(new float[1]);
		setShape3FTriangle3FArray(new float[1]);
		setShape3FTriangleMesh3FArray(new int[1]);
		setTextureBlendTextureArray(new float[1]);
		setTextureBullseyeTextureArray(new float[1]);
		setTextureCheckerboardTextureArray(new float[1]);
		setTextureConstantTextureArray(new int[1]);
		setTextureImageTextureArray(new float[1]);
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
	
	public float[] getMaterialMetalSmallPTMaterialArray() {
		return this.materialMetalSmallPTMaterialArray;
	}
	
	public float[] getMatrix44FArray() {
		return this.matrix44FArray;
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
	
	public float[] getTextureImageTextureArray() {
		return this.textureImageTextureArray;
	}
	
	public float[] getTextureMarbleTextureArray() {
		return this.textureMarbleTextureArray;
	}
	
	public float[] getTextureSimplexFractionalBrownianMotionTextureArray() {
		return this.textureSimplexFractionalBrownianMotionTextureArray;
	}
	
	public int getPrimitiveCount() {
		return this.primitiveArray.length / Primitive.ARRAY_LENGTH;
	}
	
	public int[] getMaterialClearCoatSmallPTMaterialArray() {
		return this.materialClearCoatSmallPTMaterialArray;
	}
	
	public int[] getMaterialGlassSmallPTMaterialArray() {
		return this.materialGlassSmallPTMaterialArray;
	}
	
	public int[] getMaterialMatteSmallPTMaterialArray() {
		return this.materialMatteSmallPTMaterialArray;
	}
	
	public int[] getMaterialMirrorSmallPTMaterialArray() {
		return this.materialMirrorSmallPTMaterialArray;
	}
	
	public int[] getPrimitiveArray() {
		return this.primitiveArray;
	}
	
	public int[] getShape3FTriangleMesh3FArray() {
		return this.shape3FTriangleMesh3FArray;
	}
	
	public int[] getTextureConstantTextureArray() {
		return this.textureConstantTextureArray;
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
	
	public void setMaterialClearCoatSmallPTMaterialArray(final int[] materialClearCoatSmallPTMaterialArray) {
		this.materialClearCoatSmallPTMaterialArray = Objects.requireNonNull(materialClearCoatSmallPTMaterialArray, "materialClearCoatSmallPTMaterialArray == null");
	}
	
	public void setMaterialGlassSmallPTMaterialArray(final int[] materialGlassSmallPTMaterialArray) {
		this.materialGlassSmallPTMaterialArray = Objects.requireNonNull(materialGlassSmallPTMaterialArray, "materialGlassSmallPTMaterialArray == null");
	}
	
	public void setMaterialMatteSmallPTMaterialArray(final int[] materialMatteSmallPTMaterialArray) {
		this.materialMatteSmallPTMaterialArray = Objects.requireNonNull(materialMatteSmallPTMaterialArray, "materialMatteSmallPTMaterialArray == null");
	}
	
	public void setMaterialMetalSmallPTMaterialArray(final float[] materialMetalSmallPTMaterialArray) {
		this.materialMetalSmallPTMaterialArray = Objects.requireNonNull(materialMetalSmallPTMaterialArray, "materialMetalSmallPTMaterialArray == null");
	}
	
	public void setMaterialMirrorSmallPTMaterialArray(final int[] materialMirrorSmallPTMaterialArray) {
		this.materialMirrorSmallPTMaterialArray = Objects.requireNonNull(materialMirrorSmallPTMaterialArray, "materialMirrorSmallPTMaterialArray == null");
	}
	
	public void setMatrix44FArray(final float[] matrix44FArray) {
		this.matrix44FArray = Objects.requireNonNull(matrix44FArray, "matrix44FArray == null");
	}
	
	public void setPrimitiveArray(final int[] primitiveArray) {
		this.primitiveArray = Objects.requireNonNull(primitiveArray, "primitiveArray == null");
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
	
	public void setTextureConstantTextureArray(final int[] textureConstantTextureArray) {
		this.textureConstantTextureArray = Objects.requireNonNull(textureConstantTextureArray, "textureConstantTextureArray == null");
	}
	
	public void setTextureImageTextureArray(final float[] textureImageTextureArray) {
		this.textureImageTextureArray = Objects.requireNonNull(textureImageTextureArray, "textureImageTextureArray == null");
	}
	
	public void setTextureMarbleTextureArray(final float[] textureMarbleTextureArray) {
		this.textureMarbleTextureArray = Objects.requireNonNull(textureMarbleTextureArray, "textureMarbleTextureArray == null");
	}
	
	public void setTextureSimplexFractionalBrownianMotionTextureArray(final float[] textureSimplexFractionalBrownianMotionTextureArray) {
		this.textureSimplexFractionalBrownianMotionTextureArray = Objects.requireNonNull(textureSimplexFractionalBrownianMotionTextureArray, "textureSimplexFractionalBrownianMotionTextureArray == null");
	}
}