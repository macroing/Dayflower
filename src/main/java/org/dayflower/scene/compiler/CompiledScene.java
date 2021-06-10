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
package org.dayflower.scene.compiler;

import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Cylinder3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Paraboloid3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.Rectangle3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;
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
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;

/**
 * A {@code CompiledScene} is a compiled version of a {@link Scene} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledScene {
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
	private float[] shape3FRectangle3FArray;
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
	private int[] lightIDAndOffsetArray;
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
	private int[] textureLDRImageTextureOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledScene} instance.
	 */
	public CompiledScene() {
		setBoundingVolume3FAxisAlignedBoundingBox3FArray(new float[1]);
		setBoundingVolume3FBoundingSphere3FArray(new float[1]);
		setCameraArray(new float[1]);
		setLightDirectionalLightArray(new float[1]);
		setLightIDAndOffsetArray(new int[1]);
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
		setShape3FRectangle3FArray(new float[1]);
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
		setTextureLDRImageTextureOffsetArray(new int[1]);
		setTextureMarbleTextureArray(new float[1]);
		setTextureSimplexFractionalBrownianMotionTextureArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getBoundingVolume3FAxisAlignedBoundingBox3FArray() {
		return this.boundingVolume3FAxisAlignedBoundingBox3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getBoundingVolume3FBoundingSphere3FArray() {
		return this.boundingVolume3FBoundingSphere3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains the {@link Camera} instances in compiled form that is associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains the {@code Camera} instances in compiled form that is associated with this {@code CompiledScene} instance
	 */
	public float[] getCameraArray() {
		return this.cameraArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link DirectionalLight} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code DirectionalLight} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getLightDirectionalLightArray() {
		return this.lightDirectionalLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageLight} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageLight} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getLightLDRImageLightArray() {
		return this.lightLDRImageLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PerezLight} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PerezLight} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getLightPerezLightArray() {
		return this.lightPerezLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PointLight} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PointLight} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getLightPointLightArray() {
		return this.lightPointLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SpotLight} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SpotLight} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getLightSpotLightArray() {
		return this.lightSpotLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Matrix44F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Matrix44F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getMatrix44FArray() {
		return this.matrix44FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cone3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cone3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FCone3FArray() {
		return this.shape3FCone3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Cylinder3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Cylinder3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FCylinder3FArray() {
		return this.shape3FCylinder3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Disk3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Disk3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FDisk3FArray() {
		return this.shape3FDisk3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Paraboloid3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Paraboloid3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FParaboloid3FArray() {
		return this.shape3FParaboloid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Plane3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Plane3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FPlane3FArray() {
		return this.shape3FPlane3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Rectangle3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Rectangle3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FRectangle3FArray() {
		return this.shape3FRectangle3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code RectangularCuboid3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FRectangularCuboid3FArray() {
		return this.shape3FRectangularCuboid3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Sphere3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Sphere3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FSphere3FArray() {
		return this.shape3FSphere3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Torus3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Torus3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FTorus3FArray() {
		return this.shape3FTorus3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Triangle3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Triangle3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getShape3FTriangle3FArray() {
		return this.shape3FTriangle3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BlendTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BlendTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureBlendTextureArray() {
		return this.textureBlendTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureBullseyeTextureArray() {
		return this.textureBullseyeTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureCheckerboardTextureArray() {
		return this.textureCheckerboardTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link ConstantTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code ConstantTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureConstantTextureArray() {
		return this.textureConstantTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureLDRImageTextureArray() {
		return this.textureLDRImageTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link MarbleTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code MarbleTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureMarbleTextureArray() {
		return this.textureMarbleTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public float[] getTextureSimplexFractionalBrownianMotionTextureArray() {
		return this.textureSimplexFractionalBrownianMotionTextureArray;
	}
	
	/**
	 * Returns the {@link Light} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code Light} count in this {@code CompiledScene} instance
	 */
	public int getLightCount() {
		return getLightDirectionalLightCount() + getLightLDRImageLightCount() + getLightPerezLightCount() + getLightPointLightCount() + getLightSpotLightCount();
	}
	
	/**
	 * Returns the {@link DirectionalLight} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code DirectionalLight} count in this {@code CompiledScene} instance
	 */
	public int getLightDirectionalLightCount() {
		return this.lightDirectionalLightArray.length % DirectionalLight.ARRAY_LENGTH == 0 ? this.lightDirectionalLightArray.length / DirectionalLight.ARRAY_LENGTH : 0;
	}
	
	/**
	 * Returns the {@link LDRImageLight} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code LDRImageLight} count in this {@code CompiledScene} instance
	 */
	public int getLightLDRImageLightCount() {
		return this.lightLDRImageLightArray.length % 8 == 0 ? this.lightLDRImageLightOffsetArray.length : 0;
	}
	
	/**
	 * Returns the {@link PerezLight} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code PerezLight} count in this {@code CompiledScene} instance
	 */
	public int getLightPerezLightCount() {
		return this.lightPerezLightArray.length % 8 == 0 ? this.lightPerezLightOffsetArray.length : 0;
	}
	
	/**
	 * Returns the {@link PointLight} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code PointLight} count in this {@code CompiledScene} instance
	 */
	public int getLightPointLightCount() {
		return this.lightPointLightArray.length % PointLight.ARRAY_LENGTH == 0 ? this.lightPointLightArray.length / PointLight.ARRAY_LENGTH : 0;
	}
	
	/**
	 * Returns the {@link SpotLight} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code SpotLight} count in this {@code CompiledScene} instance
	 */
	public int getLightSpotLightCount() {
		return this.lightSpotLightArray.length % SpotLight.ARRAY_LENGTH == 0 ? this.lightSpotLightArray.length / SpotLight.ARRAY_LENGTH : 0;
	}
	
	/**
	 * Returns the {@link Primitive} count in this {@code CompiledScene} instance.
	 * 
	 * @return the {@code Primitive} count in this {@code CompiledScene} instance
	 */
	public int getPrimitiveCount() {
		return this.primitiveArray.length / Primitive.ARRAY_LENGTH;
	}
	
	/**
	 * Returns an {@code int[]} that contains the ID and offset for all {@link Light} instances in this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains the ID and offset for all {@code Light} instances in this {@code CompiledScene} instance
	 */
	public int[] getLightIDAndOffsetArray() {
		return this.lightIDAndOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageLight} instances in this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageLight} instances in this {@code CompiledScene} instance
	 */
	public int[] getLightLDRImageLightOffsetArray() {
		return this.lightLDRImageLightOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link PerezLight} instances in this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code PerezLight} instances in this {@code CompiledScene} instance
	 */
	public int[] getLightPerezLightOffsetArray() {
		return this.lightPerezLightOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialClearCoatMaterialArray() {
		return this.materialClearCoatMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link DisneyMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code DisneyMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialDisneyMaterialArray() {
		return this.materialDisneyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlassMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlassMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialGlassMaterialArray() {
		return this.materialGlassMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlossyMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlossyMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialGlossyMaterialArray() {
		return this.materialGlossyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MatteMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MatteMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialMatteMaterialArray() {
		return this.materialMatteMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MetalMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MetalMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialMetalMaterialArray() {
		return this.materialMetalMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MirrorMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MirrorMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialMirrorMaterialArray() {
		return this.materialMirrorMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link PlasticMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code PlasticMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialPlasticMaterialArray() {
		return this.materialPlasticMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getMaterialSubstrateMaterialArray() {
		return this.materialSubstrateMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link Primitive} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code Primitive} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getPrimitiveArray() {
		return this.primitiveArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code TriangleMesh3F} instances in compiled form that are associated with this {@code CompiledScene} instance
	 */
	public int[] getShape3FTriangleMesh3FArray() {
		return this.shape3FTriangleMesh3FArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances in this {@code CompiledScene} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances in this {@code CompiledScene} instance
	 */
	public int[] getTextureLDRImageTextureOffsetArray() {
		return this.textureLDRImageTextureOffsetArray;
	}
	
	/**
	 * Sets all {@link AxisAlignedBoundingBox3F} instances in compiled form to {@code boundingVolume3FAxisAlignedBoundingBox3FArray}.
	 * <p>
	 * If {@code boundingVolume3FAxisAlignedBoundingBox3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArray the {@code AxisAlignedBoundingBox3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume3FAxisAlignedBoundingBox3FArray} is {@code null}
	 */
	public void setBoundingVolume3FAxisAlignedBoundingBox3FArray(final float[] boundingVolume3FAxisAlignedBoundingBox3FArray) {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = Objects.requireNonNull(boundingVolume3FAxisAlignedBoundingBox3FArray, "boundingVolume3FAxisAlignedBoundingBox3FArray == null");
	}
	
	/**
	 * Sets all {@link BoundingSphere3F} instances in compiled form to {@code boundingVolume3FBoundingSphere3FArray}.
	 * <p>
	 * If {@code boundingVolume3FBoundingSphere3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArray the {@code BoundingSphere3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume3FBoundingSphere3FArray} is {@code null}
	 */
	public void setBoundingVolume3FBoundingSphere3FArray(final float[] boundingVolume3FBoundingSphere3FArray) {
		this.boundingVolume3FBoundingSphere3FArray = Objects.requireNonNull(boundingVolume3FBoundingSphere3FArray, "boundingVolume3FBoundingSphere3FArray == null");
	}
	
	/**
	 * Sets the {@link Camera} instance in compiled form to {@code cameraArray}.
	 * <p>
	 * If {@code cameraArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraArray the {@code Camera} instance in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cameraArray} is {@code null}
	 */
	public void setCameraArray(final float[] cameraArray) {
		this.cameraArray = Objects.requireNonNull(cameraArray, "cameraArray == null");
	}
	
	/**
	 * Sets all {@link DirectionalLight} instances in compiled form to {@code lightDirectionalLightArray}.
	 * <p>
	 * If {@code lightDirectionalLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightDirectionalLightArray the {@code DirectionalLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightDirectionalLightArray} is {@code null}
	 */
	public void setLightDirectionalLightArray(final float[] lightDirectionalLightArray) {
		this.lightDirectionalLightArray = Objects.requireNonNull(lightDirectionalLightArray, "lightDirectionalLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the ID and offset for all {@link Light} instances to {@code lightIDAndOffsetArray}.
	 * <p>
	 * If {@code lightIDAndOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightIDAndOffsetArray the {@code int[]} that contains the ID and offset for all {@code Light} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightIDAndOffsetArray} is {@code null}
	 */
	public void setLightIDAndOffsetArray(final int[] lightIDAndOffsetArray) {
		this.lightIDAndOffsetArray = Objects.requireNonNull(lightIDAndOffsetArray, "lightIDAndOffsetArray == null");
	}
	
	/**
	 * Sets all {@link LDRImageLight} instances in compiled form to {@code lightLDRImageLightArray}.
	 * <p>
	 * If {@code lightLDRImageLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightLDRImageLightArray the {@code LDRImageLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightLDRImageLightArray} is {@code null}
	 */
	public void setLightLDRImageLightArray(final float[] lightLDRImageLightArray) {
		this.lightLDRImageLightArray = Objects.requireNonNull(lightLDRImageLightArray, "lightLDRImageLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageLight} instances to {@code lightLDRImageLightOffsetArray}.
	 * <p>
	 * If {@code lightLDRImageLightOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightLDRImageLightOffsetArray the {@code int[]} that contains the offsets for all {@code LDRImageLight} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightLDRImageLightOffsetArray} is {@code null}
	 */
	public void setLightLDRImageLightOffsetArray(final int[] lightLDRImageLightOffsetArray) {
		this.lightLDRImageLightOffsetArray = Objects.requireNonNull(lightLDRImageLightOffsetArray, "lightLDRImageLightOffsetArray == null");
	}
	
	/**
	 * Sets all {@link PerezLight} instances in compiled form to {@code lightPerezLightArray}.
	 * <p>
	 * If {@code lightPerezLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPerezLightArray the {@code PerezLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightPerezLightArray} is {@code null}
	 */
	public void setLightPerezLightArray(final float[] lightPerezLightArray) {
		this.lightPerezLightArray = Objects.requireNonNull(lightPerezLightArray, "lightPerezLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link PerezLight} instances to {@code lightPerezLightOffsetArray}.
	 * <p>
	 * If {@code lightPerezLightOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPerezLightOffsetArray the {@code int[]} that contains the offsets for all {@code PerezLight} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightPerezLightOffsetArray} is {@code null}
	 */
	public void setLightPerezLightOffsetArray(final int[] lightPerezLightOffsetArray) {
		this.lightPerezLightOffsetArray = Objects.requireNonNull(lightPerezLightOffsetArray, "lightPerezLightOffsetArray == null");
	}
	
	/**
	 * Sets all {@link PointLight} instances in compiled form to {@code lightPointLightArray}.
	 * <p>
	 * If {@code lightPointLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPointLightArray the {@code PointLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightPointLightArray} is {@code null}
	 */
	public void setLightPointLightArray(final float[] lightPointLightArray) {
		this.lightPointLightArray = Objects.requireNonNull(lightPointLightArray, "lightPointLightArray == null");
	}
	
	/**
	 * Sets all {@link SpotLight} instances in compiled form to {@code lightSpotLightArray}.
	 * <p>
	 * If {@code lightSpotLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightSpotLightArray the {@code SpotLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightSpotLightArray} is {@code null}
	 */
	public void setLightSpotLightArray(final float[] lightSpotLightArray) {
		this.lightSpotLightArray = Objects.requireNonNull(lightSpotLightArray, "lightSpotLightArray == null");
	}
	
	/**
	 * Sets all {@link ClearCoatMaterial} instances in compiled form to {@code materialClearCoatMaterialArray}.
	 * <p>
	 * If {@code materialClearCoatMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialClearCoatMaterialArray the {@code ClearCoatMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialClearCoatMaterialArray} is {@code null}
	 */
	public void setMaterialClearCoatMaterialArray(final int[] materialClearCoatMaterialArray) {
		this.materialClearCoatMaterialArray = Objects.requireNonNull(materialClearCoatMaterialArray, "materialClearCoatMaterialArray == null");
	}
	
	/**
	 * Sets all {@link DisneyMaterial} instances in compiled form to {@code materialDisneyMaterialArray}.
	 * <p>
	 * If {@code materialDisneyMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialDisneyMaterialArray the {@code DisneyMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialDisneyMaterialArray} is {@code null}
	 */
	public void setMaterialDisneyMaterialArray(final int[] materialDisneyMaterialArray) {
		this.materialDisneyMaterialArray = Objects.requireNonNull(materialDisneyMaterialArray, "materialDisneyMaterialArray == null");
	}
	
	/**
	 * Sets all {@link GlassMaterial} instances in compiled form to {@code materialGlassMaterialArray}.
	 * <p>
	 * If {@code materialGlassMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlassMaterialArray the {@code GlassMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialGlassMaterialArray} is {@code null}
	 */
	public void setMaterialGlassMaterialArray(final int[] materialGlassMaterialArray) {
		this.materialGlassMaterialArray = Objects.requireNonNull(materialGlassMaterialArray, "materialGlassMaterialArray == null");
	}
	
	/**
	 * Sets all {@link GlossyMaterial} instances in compiled form to {@code materialGlossyMaterialArray}.
	 * <p>
	 * If {@code materialGlossyMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlossyMaterialArray the {@code GlossyMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialGlossyMaterialArray} is {@code null}
	 */
	public void setMaterialGlossyMaterialArray(final int[] materialGlossyMaterialArray) {
		this.materialGlossyMaterialArray = Objects.requireNonNull(materialGlossyMaterialArray, "materialGlossyMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MatteMaterial} instances in compiled form to {@code materialMatteMaterialArray}.
	 * <p>
	 * If {@code materialMatteMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMatteMaterialArray the {@code MatteMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialMatteMaterialArray} is {@code null}
	 */
	public void setMaterialMatteMaterialArray(final int[] materialMatteMaterialArray) {
		this.materialMatteMaterialArray = Objects.requireNonNull(materialMatteMaterialArray, "materialMatteMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MetalMaterial} instances in compiled form to {@code materialMetalMaterialArray}.
	 * <p>
	 * If {@code materialMetalMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMetalMaterialArray the {@code MetalMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialMetalMaterialArray} is {@code null}
	 */
	public void setMaterialMetalMaterialArray(final int[] materialMetalMaterialArray) {
		this.materialMetalMaterialArray = Objects.requireNonNull(materialMetalMaterialArray, "materialMetalMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MirrorMaterial} instances in compiled form to {@code materialMirrorMaterialArray}.
	 * <p>
	 * If {@code materialMirrorMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMirrorMaterialArray the {@code MirrorMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialMirrorMaterialArray} is {@code null}
	 */
	public void setMaterialMirrorMaterialArray(final int[] materialMirrorMaterialArray) {
		this.materialMirrorMaterialArray = Objects.requireNonNull(materialMirrorMaterialArray, "materialMirrorMaterialArray == null");
	}
	
	/**
	 * Sets all {@link PlasticMaterial} instances in compiled form to {@code materialPlasticMaterialArray}.
	 * <p>
	 * If {@code materialPlasticMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPlasticMaterialArray the {@code PlasticMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialPlasticMaterialArray} is {@code null}
	 */
	public void setMaterialPlasticMaterialArray(final int[] materialPlasticMaterialArray) {
		this.materialPlasticMaterialArray = Objects.requireNonNull(materialPlasticMaterialArray, "materialPlasticMaterialArray == null");
	}
	
	/**
	 * Sets all {@link SubstrateMaterial} instances in compiled form to {@code materialSubstrateMaterialArray}.
	 * <p>
	 * If {@code materialSubstrateMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialSubstrateMaterialArray the {@code SubstrateMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialSubstrateMaterialArray} is {@code null}
	 */
	public void setMaterialSubstrateMaterialArray(final int[] materialSubstrateMaterialArray) {
		this.materialSubstrateMaterialArray = Objects.requireNonNull(materialSubstrateMaterialArray, "materialSubstrateMaterialArray == null");
	}
	
	/**
	 * Sets all {@link Matrix44F} instances in compiled form to {@code matrix44FArray}.
	 * <p>
	 * If {@code matrix44FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix44FArray the {@code Matrix44F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matrix44FArray} is {@code null}
	 */
	public void setMatrix44FArray(final float[] matrix44FArray) {
		this.matrix44FArray = Objects.requireNonNull(matrix44FArray, "matrix44FArray == null");
	}
	
	/**
	 * Sets all {@link Primitive} instances in compiled form to {@code primitiveArray}.
	 * <p>
	 * If {@code primitiveArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveArray the {@code Primitive} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitiveArray} is {@code null}
	 */
	public void setPrimitiveArray(final int[] primitiveArray) {
		this.primitiveArray = Objects.requireNonNull(primitiveArray, "primitiveArray == null");
	}
	
	/**
	 * Sets all {@link Cone3F} instances in compiled form to {@code shape3FCone3FArray}.
	 * <p>
	 * If {@code shape3FCone3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCone3FArray the {@code Cone3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCone3FArray} is {@code null}
	 */
	public void setShape3FCone3FArray(final float[] shape3FCone3FArray) {
		this.shape3FCone3FArray = Objects.requireNonNull(shape3FCone3FArray, "shape3FCone3FArray == null");
	}
	
	/**
	 * Sets all {@link Cylinder3F} instances in compiled form to {@code shape3FCylinder3FArray}.
	 * <p>
	 * If {@code shape3FCylinder3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FCylinder3FArray the {@code Cylinder3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FCylinder3FArray} is {@code null}
	 */
	public void setShape3FCylinder3FArray(final float[] shape3FCylinder3FArray) {
		this.shape3FCylinder3FArray = Objects.requireNonNull(shape3FCylinder3FArray, "shape3FCylinder3FArray == null");
	}
	
	/**
	 * Sets all {@link Disk3F} instances in compiled form to {@code shape3FDisk3FArray}.
	 * <p>
	 * If {@code shape3FDisk3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FDisk3FArray the {@code Disk3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FDisk3FArray} is {@code null}
	 */
	public void setShape3FDisk3FArray(final float[] shape3FDisk3FArray) {
		this.shape3FDisk3FArray = Objects.requireNonNull(shape3FDisk3FArray, "shape3FDisk3FArray == null");
	}
	
	/**
	 * Sets all {@link Paraboloid3F} instances in compiled form to {@code shape3FParaboloid3FArray}.
	 * <p>
	 * If {@code shape3FParaboloid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FParaboloid3FArray the {@code Paraboloid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FParaboloid3FArray} is {@code null}
	 */
	public void setShape3FParaboloid3FArray(final float[] shape3FParaboloid3FArray) {
		this.shape3FParaboloid3FArray = Objects.requireNonNull(shape3FParaboloid3FArray, "shape3FParaboloid3FArray == null");
	}
	
	/**
	 * Sets all {@link Plane3F} instances in compiled form to {@code shape3FPlane3FArray}.
	 * <p>
	 * If {@code shape3FPlane3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FPlane3FArray the {@code Plane3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FPlane3FArray} is {@code null}
	 */
	public void setShape3FPlane3FArray(final float[] shape3FPlane3FArray) {
		this.shape3FPlane3FArray = Objects.requireNonNull(shape3FPlane3FArray, "shape3FPlane3FArray == null");
	}
	
	/**
	 * Sets all {@link Rectangle3F} instances in compiled form to {@code shape3FRectangle3FArray}.
	 * <p>
	 * If {@code shape3FRectangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangle3FArray the {@code Rectangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangle3FArray} is {@code null}
	 */
	public void setShape3FRectangle3FArray(final float[] shape3FRectangle3FArray) {
		this.shape3FRectangle3FArray = Objects.requireNonNull(shape3FRectangle3FArray, "shape3FRectangle3FArray == null");
	}
	
	/**
	 * Sets all {@link RectangularCuboid3F} instances in compiled form to {@code shape3FRectangularCuboid3FArray}.
	 * <p>
	 * If {@code shape3FRectangularCuboid3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FRectangularCuboid3FArray the {@code RectangularCuboid3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FRectangularCuboid3FArray} is {@code null}
	 */
	public void setShape3FRectangularCuboid3FArray(final float[] shape3FRectangularCuboid3FArray) {
		this.shape3FRectangularCuboid3FArray = Objects.requireNonNull(shape3FRectangularCuboid3FArray, "shape3FRectangularCuboid3FArray == null");
	}
	
	/**
	 * Sets all {@link Sphere3F} instances in compiled form to {@code shape3FSphere3FArray}.
	 * <p>
	 * If {@code shape3FSphere3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FSphere3FArray the {@code Sphere3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FSphere3FArray} is {@code null}
	 */
	public void setShape3FSphere3FArray(final float[] shape3FSphere3FArray) {
		this.shape3FSphere3FArray = Objects.requireNonNull(shape3FSphere3FArray, "shape3FSphere3FArray == null");
	}
	
	/**
	 * Sets all {@link Torus3F} instances in compiled form to {@code shape3FTorus3FArray}.
	 * <p>
	 * If {@code shape3FTorus3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTorus3FArray the {@code Torus3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTorus3FArray} is {@code null}
	 */
	public void setShape3FTorus3FArray(final float[] shape3FTorus3FArray) {
		this.shape3FTorus3FArray = Objects.requireNonNull(shape3FTorus3FArray, "shape3FTorus3FArray == null");
	}
	
	/**
	 * Sets all {@link Triangle3F} instances in compiled form to {@code shape3FTriangle3FArray}.
	 * <p>
	 * If {@code shape3FTriangle3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangle3FArray the {@code Triangle3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangle3FArray} is {@code null}
	 */
	public void setShape3FTriangle3FArray(final float[] shape3FTriangle3FArray) {
		this.shape3FTriangle3FArray = Objects.requireNonNull(shape3FTriangle3FArray, "shape3FTriangle3FArray == null");
	}
	
	/**
	 * Sets all {@link TriangleMesh3F} instances in compiled form to {@code shape3FTriangleMesh3FArray}.
	 * <p>
	 * If {@code shape3FTriangleMesh3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape3FTriangleMesh3FArray the {@code TriangleMesh3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code shape3FTriangleMesh3FArray} is {@code null}
	 */
	public void setShape3FTriangleMesh3FArray(final int[] shape3FTriangleMesh3FArray) {
		this.shape3FTriangleMesh3FArray = Objects.requireNonNull(shape3FTriangleMesh3FArray, "shape3FTriangleMesh3FArray == null");
	}
	
	/**
	 * Sets all {@link BlendTexture} instances in compiled form to {@code textureBlendTextureArray}.
	 * <p>
	 * If {@code textureBlendTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBlendTextureArray the {@code BlendTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureBlendTextureArray} is {@code null}
	 */
	public void setTextureBlendTextureArray(final float[] textureBlendTextureArray) {
		this.textureBlendTextureArray = Objects.requireNonNull(textureBlendTextureArray, "textureBlendTextureArray == null");
	}
	
	/**
	 * Sets all {@link BullseyeTexture} instances in compiled form to {@code textureBullseyeTextureArray}.
	 * <p>
	 * If {@code textureBullseyeTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBullseyeTextureArray the {@code BullseyeTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureBullseyeTextureArray} is {@code null}
	 */
	public void setTextureBullseyeTextureArray(final float[] textureBullseyeTextureArray) {
		this.textureBullseyeTextureArray = Objects.requireNonNull(textureBullseyeTextureArray, "textureBullseyeTextureArray == null");
	}
	
	/**
	 * Sets all {@link CheckerboardTexture} instances in compiled form to {@code textureCheckerboardTextureArray}.
	 * <p>
	 * If {@code textureCheckerboardTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCheckerboardTextureArray the {@code CheckerboardTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureCheckerboardTextureArray} is {@code null}
	 */
	public void setTextureCheckerboardTextureArray(final float[] textureCheckerboardTextureArray) {
		this.textureCheckerboardTextureArray = Objects.requireNonNull(textureCheckerboardTextureArray, "textureCheckerboardTextureArray == null");
	}
	
	/**
	 * Sets all {@link ConstantTexture} instances in compiled form to {@code textureConstantTextureArray}.
	 * <p>
	 * If {@code textureConstantTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureConstantTextureArray the {@code ConstantTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureConstantTextureArray} is {@code null}
	 */
	public void setTextureConstantTextureArray(final float[] textureConstantTextureArray) {
		this.textureConstantTextureArray = Objects.requireNonNull(textureConstantTextureArray, "textureConstantTextureArray == null");
	}
	
	/**
	 * Sets all {@link LDRImageTexture} instances in compiled form to {@code textureLDRImageTextureArray}.
	 * <p>
	 * If {@code textureLDRImageTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureLDRImageTextureArray the {@code LDRImageTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureLDRImageTextureArray} is {@code null}
	 */
	public void setTextureLDRImageTextureArray(final float[] textureLDRImageTextureArray) {
		this.textureLDRImageTextureArray = Objects.requireNonNull(textureLDRImageTextureArray, "textureLDRImageTextureArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances to {@code textureLDRImageTextureOffsetArray}.
	 * <p>
	 * If {@code textureLDRImageTextureOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureLDRImageTextureOffsetArray the {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances
	 * @throws NullPointerException thrown if, and only if, {@code textureLDRImageTextureOffsetArray} is {@code null}
	 */
	public void setTextureLDRImageTextureOffsetArray(final int[] textureLDRImageTextureOffsetArray) {
		this.textureLDRImageTextureOffsetArray = Objects.requireNonNull(textureLDRImageTextureOffsetArray, "textureLDRImageTextureOffsetArray == null");
	}
	
	/**
	 * Sets all {@link MarbleTexture} instances in compiled form to {@code textureMarbleTextureArray}.
	 * <p>
	 * If {@code textureMarbleTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureMarbleTextureArray the {@code MarbleTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureMarbleTextureArray} is {@code null}
	 */
	public void setTextureMarbleTextureArray(final float[] textureMarbleTextureArray) {
		this.textureMarbleTextureArray = Objects.requireNonNull(textureMarbleTextureArray, "textureMarbleTextureArray == null");
	}
	
	/**
	 * Sets all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form to {@code textureSimplexFractionalBrownianMotionTextureArray}.
	 * <p>
	 * If {@code textureSimplexFractionalBrownianMotionTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureSimplexFractionalBrownianMotionTextureArray the {@code SimplexFractionalBrownianMotionTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureSimplexFractionalBrownianMotionTextureArray} is {@code null}
	 */
	public void setTextureSimplexFractionalBrownianMotionTextureArray(final float[] textureSimplexFractionalBrownianMotionTextureArray) {
		this.textureSimplexFractionalBrownianMotionTextureArray = Objects.requireNonNull(textureSimplexFractionalBrownianMotionTextureArray, "textureSimplexFractionalBrownianMotionTextureArray == null");
	}
}