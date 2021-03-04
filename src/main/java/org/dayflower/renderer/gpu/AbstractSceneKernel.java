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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;

import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Cone3F;
import org.dayflower.geometry.shape.Disk3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.geometry.shape.TriangleMesh3F;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.utility.Floats;

/**
 * An {@code AbstractSceneKernel} is an abstract extension of the {@code AbstractGeometryKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>Camera ray generation methods</li>
 * <li>Light evaluation methods</li>
 * <li>Material evaluation methods</li>
 * <li>Primitive intersection methods</li>
 * <li>Texture evaluation methods</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractSceneKernel extends AbstractGeometryKernel {
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING = 0;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL = 3;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING = 6;
	
//	TODO: Add Javadocs!
	protected static final int MATERIAL_B_X_D_F_ARRAY_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] cameraArray;
	
//	TODO: Add Javadocs!
	protected float[] lightLDRImageLightArray;
	
//	TODO: Add Javadocs!
	protected float[] materialBXDFResultArray_$private$16;
	
//	TODO: Add Javadocs!
	protected float[] matrix44FArray;
	
//	TODO: Add Javadocs!
	protected float[] pixelArray;
	
//	TODO: Add Javadocs!
	protected float[] textureBlendTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureBullseyeTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureCheckerboardTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureConstantTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureLDRImageTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureMarbleTextureArray;
	
//	TODO: Add Javadocs!
	protected float[] textureSimplexFractionalBrownianMotionTextureArray;
	
//	TODO: Add Javadocs!
	protected int lightLDRImageLightCount;
	
//	TODO: Add Javadocs!
	protected int primitiveCount;
	
//	TODO: Add Javadocs!
	protected int[] lightLDRImageLightOffsetArray;
	
//	TODO: Add Javadocs!
	protected int[] materialClearCoatMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialGlassMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialGlossyMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMatteMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] materialMirrorMaterialArray;
	
//	TODO: Add Javadocs!
	protected int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Scene scene;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractSceneKernel} instance.
	 */
	protected AbstractSceneKernel() {
		this.cameraArray = new float[1];
		this.lightLDRImageLightArray = new float[1];
		this.materialBXDFResultArray_$private$16 = new float[MATERIAL_B_X_D_F_ARRAY_SIZE];
		this.matrix44FArray = new float[1];
		this.pixelArray = new float[1];
		this.textureBlendTextureArray = new float[1];
		this.textureBullseyeTextureArray = new float[1];
		this.textureCheckerboardTextureArray = new float[1];
		this.textureConstantTextureArray = new float[1];
		this.textureLDRImageTextureArray = new float[1];
		this.textureMarbleTextureArray = new float[1];
		this.textureSimplexFractionalBrownianMotionTextureArray = new float[1];
		this.lightLDRImageLightCount = 0;
		this.primitiveCount = 0;
		this.lightLDRImageLightOffsetArray = new int[1];
		this.materialClearCoatMaterialArray = new int[1];
		this.materialGlassMaterialArray = new int[1];
		this.materialGlossyMaterialArray = new int[1];
		this.materialMatteMaterialArray = new int[1];
		this.materialMirrorMaterialArray = new int[1];
		this.primitiveArray = new int[1];
		this.scene = new Scene();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Scene} instance that is associated with this {@code AbstractSceneKernel} instance.
	 * 
	 * @return the {@code Scene} instance that is associated with this {@code AbstractSceneKernel} instance
	 */
	public final Scene getScene() {
		return this.scene;
	}
	
	/**
	 * Sets the {@link Scene} instance that is associated with this {@code AbstractSceneKernel} instance to {@code scene}.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene a {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	public final void setScene(final Scene scene) {
		this.scene = Objects.requireNonNull(scene, "scene == null");
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractSceneKernel} instance.
	 */
	@Override
	public void setup() {
		super.setup();
		
		doSetupPixelArray();
		doSetupScene();
	}
	
	/**
	 * Updates the {@link Camera} instance.
	 */
	public final void updateCamera() {
		put(this.cameraArray = getScene().getCamera().toArray());
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the material of the currently intersected primitive is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the material of the currently intersected primitive is specular, {@code false} otherwise
	 */
	protected final boolean materialIsSpecular() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		
		if(materialID == GlassMaterial.ID) {
			return true;
		}
		
		if(materialID == MirrorMaterial.ID) {
			return true;
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunction() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		
		if(materialID == ClearCoatMaterial.ID) {
			return materialSampleDistributionFunctionClearCoatMaterial();
		}
		
		if(materialID == GlassMaterial.ID) {
			return materialSampleDistributionFunctionGlassMaterial();
		}
		
		if(materialID == GlossyMaterial.ID) {
			return materialSampleDistributionFunctionGlossyMaterial();
		}
		
		if(materialID == MatteMaterial.ID) {
			return materialSampleDistributionFunctionMatteMaterial();
		}
		
		if(materialID == MirrorMaterial.ID) {
			return materialSampleDistributionFunctionMirrorMaterial();
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionClearCoatMaterial() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKD = this.materialClearCoatMaterialArray[materialOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		final int textureKS = this.materialClearCoatMaterialArray[materialOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_S];
		final int textureKSID = (textureKS >>> 16) & 0xFFFF;
		final int textureKSOffset = textureKS & 0xFFFF;
		
		textureEvaluate(textureKDID, textureKDOffset);
		
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKSID, textureKSOffset);
		
		final float colorKSR = color3FLHSGetComponent1();
		final float colorKSG = color3FLHSGetComponent2();
		final float colorKSB = color3FLHSGetComponent3();
		
		final float directionX = ray3FGetDirectionComponent1();
		final float directionY = ray3FGetDirectionComponent2();
		final float directionZ = ray3FGetDirectionComponent3();
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
		final float surfaceNormalCorrectlyOrientedX = vector3FGetComponent1();
		final float surfaceNormalCorrectlyOrientedY = vector3FGetComponent2();
		final float surfaceNormalCorrectlyOrientedZ = vector3FGetComponent3();
		
		final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
		
		final float etaA = 1.0F;
		final float etaB = 1.5F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final boolean isRefracting = vector3FSetRefraction(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
//			final float reflectance = fresnelDielectricSchlick(cosThetaICorrectlyOriented, ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA)));
			final float reflectance = fresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
				
				color3FLHSSet(colorKSR * probabilityRussianRouletteReflection, colorKSG * probabilityRussianRouletteReflection, colorKSB * probabilityRussianRouletteReflection);
			} else {
				vector3FSetDiffuseReflection(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, random(), random());
				
				color3FLHSSet(colorKDR * probabilityRussianRouletteTransmission, colorKDG * probabilityRussianRouletteTransmission, colorKDB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
			
			color3FLHSSet(colorKSR, colorKSG, colorKSB);
		}
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionGlassMaterial() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureEta = this.materialGlassMaterialArray[materialOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_ETA];
		final int textureEtaID = (textureEta >>> 16) & 0xFFFF;
		final int textureEtaOffset = textureEta & 0xFFFF;
		final int textureKR = this.materialGlassMaterialArray[materialOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		final int textureKT = this.materialGlassMaterialArray[materialOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_T];
		final int textureKTID = (textureKT >>> 16) & 0xFFFF;
		final int textureKTOffset = textureKT & 0xFFFF;
		
		textureEvaluate(textureEtaID, textureEtaOffset);
		
		final float colorEtaR = color3FLHSGetComponent1();
		final float colorEtaG = color3FLHSGetComponent2();
		final float colorEtaB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKRID, textureKROffset);
		
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
		textureEvaluate(textureKTID, textureKTOffset);
		
		final float colorKTR = color3FLHSGetComponent1();
		final float colorKTG = color3FLHSGetComponent2();
		final float colorKTB = color3FLHSGetComponent3();
		
		final float directionX = ray3FGetDirectionComponent1();
		final float directionY = ray3FGetDirectionComponent2();
		final float directionZ = ray3FGetDirectionComponent3();
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
		final float surfaceNormalCorrectlyOrientedX = vector3FGetComponent1();
		final float surfaceNormalCorrectlyOrientedY = vector3FGetComponent2();
		final float surfaceNormalCorrectlyOrientedZ = vector3FGetComponent3();
		
		final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
		
		final float etaA = 1.0F;
		final float etaB = (colorEtaR + colorEtaG + colorEtaB) / 3.0F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		final boolean isRefracting = vector3FSetRefraction(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
//			final float reflectance = fresnelDielectricSchlick(cosThetaICorrectlyOriented, ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA)));
			final float reflectance = fresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
				
				color3FLHSSet(colorKRR * probabilityRussianRouletteReflection, colorKRG * probabilityRussianRouletteReflection, colorKRB * probabilityRussianRouletteReflection);
			} else {
				color3FLHSSet(colorKTR * probabilityRussianRouletteTransmission, colorKTG * probabilityRussianRouletteTransmission, colorKTB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
			
			color3FLHSSet(colorKRR, colorKRG, colorKRB);
		}
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionGlossyMaterial() {
		/*
		 * Material:
		 */
		
//		Retrieve indices and offsets:
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKR = this.materialGlossyMaterialArray[materialOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		final int textureRoughness = this.materialGlossyMaterialArray[materialOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS];
		final int textureRoughnessID = (textureRoughness >>> 16) & 0xFFFF;
		final int textureRoughnessOffset = textureRoughness & 0xFFFF;
		
//		Evaluate the KR texture:
		textureEvaluate(textureKRID, textureKROffset);
		
//		Retrieve the color from the KR texture:
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
//		Evaluate the Roughness texture:
		textureEvaluate(textureRoughnessID, textureRoughnessOffset);
		
//		Retrieve the color from the Roughness texture:
		final float colorRoughnessR = color3FLHSGetComponent1();
		final float colorRoughnessG = color3FLHSGetComponent2();
		final float colorRoughnessB = color3FLHSGetComponent3();
		
//		Compute the roughness and exponent:
		final float roughness = (colorRoughnessR + colorRoughnessG + colorRoughnessB) / 3.0F;
		final float exponent = 1.0F / (roughness * roughness);
		
		
		
		/*
		 * BSDF:
		 */
		
//		Perform world space to shade space transformations:
		materialBXDFBegin();
		
		
		
		/*
		 * BXDF:
		 */
		
//		Retrieve the normal in shade space:
		final float normalShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0];
		final float normalShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1];
		final float normalShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2];
		
//		Retrieve the outgoing direction in shade space:
		final float outgoingShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0];
		final float outgoingShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1];
		final float outgoingShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2];
		
//		Compute the dot product between the normal in shade space and the outgoing direction in shade space:
		final float normalShadeSpaceDotOutgoingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
		
//		Sample the hemisphere in local space using a power-cosine distribution:
		vector3FSetSampleHemispherePowerCosineDistribution(random(), random(), exponent);
		
//		Retrieve the half vector in local space:
		final float halfLocalSpaceX = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -vector3FGetComponent1() : vector3FGetComponent1();
		final float halfLocalSpaceY = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -vector3FGetComponent2() : vector3FGetComponent2();
		final float halfLocalSpaceZ = normalShadeSpaceDotOutgoingShadeSpace < 0.0F ? -vector3FGetComponent3() : vector3FGetComponent3();
		
//		Compute the dot product between the outgoing direction in shade space and the half vector in local space:
		final float outgoingShadeSpaceDotHalfLocalSpace = vector3FDotProduct(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, halfLocalSpaceX, halfLocalSpaceY, halfLocalSpaceZ);
		
//		Compute the incoming direction in shade space:
		final float incomingShadeSpaceX = outgoingShadeSpaceX - halfLocalSpaceX * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		final float incomingShadeSpaceY = outgoingShadeSpaceY - halfLocalSpaceY * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		final float incomingShadeSpaceZ = outgoingShadeSpaceZ - halfLocalSpaceZ * 2.0F * outgoingShadeSpaceDotHalfLocalSpace;
		
//		Compute the half vector in shade space:
		vector3FSetHalf(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Retrieve the half vector in shade space:
		final float halfShadeSpaceX = vector3FGetComponent1();
		final float halfShadeSpaceY = vector3FGetComponent2();
		final float halfShadeSpaceZ = vector3FGetComponent3();
		
//		Compute the dot product between the normal in shade space and the half vector in shade space:
		final float normalShadeSpaceDotHalfShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, halfShadeSpaceX, halfShadeSpaceY, halfShadeSpaceZ);
		
//		Compute the dot product between the normal in shade space and the incoming direction in shade space and its absolute representation:
		final float normalShadeSpaceDotIncomingShadeSpace = vector3FDotProduct(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ, incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Compute the dot product between the outgoing direction in shade space and the half vector in shade space:
		final float outgoingShadeSpaceDotHalfShadeSpace = vector3FDotProduct(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ, halfShadeSpaceX, halfShadeSpaceY, halfShadeSpaceZ);
		
//		Check that the dot products are opposite:
		if(normalShadeSpaceDotIncomingShadeSpace > 0.0F && normalShadeSpaceDotOutgoingShadeSpace > 0.0F || normalShadeSpaceDotIncomingShadeSpace < 0.0F && normalShadeSpaceDotOutgoingShadeSpace < 0.0F) {
			return false;
		}
		
//		Compute the probability density function (PDF) value:
		final float probabilityDensityFunctionValue = (exponent + 1.0F) * pow(abs(normalShadeSpaceDotHalfShadeSpace), exponent) / (PI * 8.0F * abs(outgoingShadeSpaceDotHalfShadeSpace));
		
		if(probabilityDensityFunctionValue <= 0.0F) {
			return false;
		}
		
		final float d = (exponent + 1.0F) * pow(abs(normalShadeSpaceDotHalfShadeSpace), exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = fresnelDielectricSchlick(outgoingShadeSpaceDotHalfShadeSpace, 1.0F);
		final float g = 4.0F * abs(normalShadeSpaceDotOutgoingShadeSpace + -normalShadeSpaceDotIncomingShadeSpace - normalShadeSpaceDotOutgoingShadeSpace * -normalShadeSpaceDotIncomingShadeSpace);
		
//		Compute the result:
		final float resultR = colorKRR * d * f / g;
		final float resultG = colorKRG * d * f / g;
		final float resultB = colorKRB * d * f / g;
		
		
		
		/*
		 * BSDF:
		 */
		
//		Set the incoming direction in shade space and perform shade space to world space transformations:
		materialBXDFSetIncoming(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		materialBXDFEnd();
		
		
		
		/*
		 * Material:
		 */
		
		final float incomingWorldSpaceX = vector3FGetComponent1();
		final float incomingWorldSpaceY = vector3FGetComponent2();
		final float incomingWorldSpaceZ = vector3FGetComponent3();
		
		final float normalWorldSpaceX = intersectionGetOrthonormalBasisSWComponent1();
		final float normalWorldSpaceY = intersectionGetOrthonormalBasisSWComponent2();
		final float normalWorldSpaceZ = intersectionGetOrthonormalBasisSWComponent3();
		
		final float incomingWorldSpaceDotNormalWorldSpace = vector3FDotProduct(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ, normalWorldSpaceX, normalWorldSpaceY, normalWorldSpaceZ);
		final float incomingWorldSpaceDotNormalWorldSpaceAbs = abs(incomingWorldSpaceDotNormalWorldSpace);
		
		final float colorR = resultR * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorG = resultG * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		final float colorB = resultB * (incomingWorldSpaceDotNormalWorldSpaceAbs / probabilityDensityFunctionValue);
		
		color3FLHSSet(colorR, colorG, colorB);
		
		
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMatteMaterial() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKD = this.materialMatteMaterialArray[materialOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
		textureEvaluate(textureKDID, textureKDOffset);
		
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
		final float directionX = ray3FGetDirectionComponent1();
		final float directionY = ray3FGetDirectionComponent2();
		final float directionZ = ray3FGetDirectionComponent3();
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		vector3FSetFaceForwardNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		vector3FSetDiffuseReflection(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3(), random(), random());
		
		color3FLHSSet(colorKDR, colorKDG, colorKDB);
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionMirrorMaterial() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int textureKR = this.materialMirrorMaterialArray[materialOffset + MirrorMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		
		textureEvaluate(textureKRID, textureKROffset);
		
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
		final float directionX = ray3FGetDirectionComponent1();
		final float directionY = ray3FGetDirectionComponent2();
		final float directionZ = ray3FGetDirectionComponent3();
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
		
		color3FLHSSet(colorKRR, colorKRG, colorKRB);
		
		return true;
	}
	
	/**
	 * Performs an intersection test against all primitives in the scene and computes intersection information for the closest.
	 * <p>
	 * Returns {@code true} if, and only if, an intersection was found, {@code false} otherwise.
	 * <p>
	 * If an intersection was found, the computed information will be present in {@link #intersectionArray_$private$24} in world space.
	 * 
	 * @return {@code true} if, and only if, an intersection was found, {@code false} otherwise
	 */
	protected final boolean primitiveIntersectionCompute() {
		int primitiveIndex = -1;
		
		this.shape3FTriangleMesh3FArrayToShape3FTriangle3FArray_$private$1[0] = -1;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FBoundingSphere3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = ray3FGetTMinimum();
				final float tMaximumObjectSpace = ray3FGetTMaximum();
				
				if(shapeID == Cone3F.ID) {
					tObjectSpace = shape3FCone3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Disk3F.ID) {
					tObjectSpace = shape3FDisk3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = shape3FTorus3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = shape3FTriangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = shape3FTriangleMesh3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					ray3FSetTMaximum(tObjectSpace);
					
					primitiveIndex = index;
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
			}
		}
		
		if(primitiveIndex != -1) {
			final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			ray3FSetMatrix44FTransformWorldToObject(primitiveIndex);
			
			final float tObjectSpace = ray3FGetTMaximum();
			
			if(shapeID == Cone3F.ID) {
				shape3FCone3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Disk3F.ID) {
				shape3FDisk3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Plane3F.ID) {
				shape3FPlane3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == RectangularCuboid3F.ID) {
				shape3FRectangularCuboid3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Sphere3F.ID) {
				shape3FSphere3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Torus3F.ID) {
				shape3FTorus3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == Triangle3F.ID) {
				shape3FTriangle3FIntersectionCompute(tObjectSpace, primitiveIndex, shapeOffset);
			} else if(shapeID == TriangleMesh3F.ID) {
				shape3FTriangleMesh3FIntersectionCompute(tObjectSpace, primitiveIndex);
			}
			
			ray3FSetMatrix44FTransformObjectToWorld(primitiveIndex);
			
			intersectionTransformObjectToWorld(primitiveIndex);
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current ray intersects a given primitive in world space, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current ray intersects a given primitive in world space, {@code false} otherwise
	 */
	protected final boolean primitiveIntersects() {
		return primitiveIntersectionT() > 0.0F;
	}
	
	/**
	 * Generates a ray for the current pixel using the current camera.
	 * <p>
	 * Returns {@code true} if, and only if, a ray was generated, {@code false} otherwise.
	 * <p>
	 * If the current camera uses a thin lens, this method should always return {@code true}. If, on the other hand, the current camera uses a fisheye lens, this may not always be the case.
	 * <p>
	 * If this method returns {@code true}, {@code pixelX} and {@code pixelY} will be set in {@link #pixelArray}.
	 * 
	 * @param pixelX the sample on the X-axis within the current pixel
	 * @param pixelY the sample on the Y-axis within the current pixel
	 * @return {@code true} if, and only if, a ray was generated, {@code false} otherwise
	 */
	protected final boolean ray3FCameraGenerate(final float pixelX, final float pixelY) {
//		Retrieve the image coordinates:
		final float imageX = getGlobalId() % this.resolutionX;
		final float imageY = getGlobalId() / this.resolutionX;
		
//		Retrieve all values from the 'cameraArray' in the correct order:
		final float fieldOfViewX = tan(+this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_X] * 0.5F);
		final float fieldOfViewY = tan(-this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_Y] * 0.5F);
		final float lens = this.cameraArray[Camera.ARRAY_OFFSET_LENS];
		final float uX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 0];
		final float uY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 1];
		final float uZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 2];
		final float vX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 0];
		final float vY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 1];
		final float vZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 2];
		final float wX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 0];
		final float wY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 1];
		final float wZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 2];
		final float eyeX = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 0];
		final float eyeY = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 1];
		final float eyeZ = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 2];
		final float apertureRadius = this.cameraArray[Camera.ARRAY_OFFSET_APERTURE_RADIUS];
		final float focalDistance = this.cameraArray[Camera.ARRAY_OFFSET_FOCAL_DISTANCE];
		final float resolutionX = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_X];
		final float resolutionY = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_Y];
		
//		Compute the camera coordinates:
		final float cameraX = 2.0F * ((imageX + pixelX) / (resolutionX - 1.0F)) - 1.0F;
		final float cameraY = 2.0F * ((imageY + pixelY) / (resolutionY - 1.0F)) - 1.0F;
		
//		Compute the 'wFactor' that is used for the fisheye lens:
		float wFactor = 1.0F;
		
		if(lens == 0.0F) {
			final float dotProduct = cameraX * cameraX + cameraY * cameraY;
			
			if(dotProduct > 1.0F) {
				return false;
			}
			
			wFactor = sqrt(1.0F - dotProduct);
		}
		
//		Sample the disk with a uniform distribution:
		final float u = random();
		final float v = random();
		final float r = sqrt(u);
		final float theta = PI_MULTIPLIED_BY_2 * v;
		final float diskX = r * cos(theta);
		final float diskY = r * sin(theta);
		
//		Compute the point on the plane one unit away from the eye:
		final float pointOnPlaneOneUnitAwayFromEyeX = (uX * fieldOfViewX * cameraX) + (vX * fieldOfViewY * cameraY) + (eyeX + wX * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeY = (uY * fieldOfViewX * cameraX) + (vY * fieldOfViewY * cameraY) + (eyeY + wY * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeZ = (uZ * fieldOfViewX * cameraX) + (vZ * fieldOfViewY * cameraY) + (eyeZ + wZ * wFactor);
		
//		Compute the point on the image plane:
		final float pointOnImagePlaneX = eyeX + (pointOnPlaneOneUnitAwayFromEyeX - eyeX) * focalDistance;
		final float pointOnImagePlaneY = eyeY + (pointOnPlaneOneUnitAwayFromEyeY - eyeY) * focalDistance;
		final float pointOnImagePlaneZ = eyeZ + (pointOnPlaneOneUnitAwayFromEyeZ - eyeZ) * focalDistance;
		
//		Compute the ray origin:
		final float originX = apertureRadius > 0.00001F ? eyeX + ((uX * diskX * apertureRadius) + (vX * diskY * apertureRadius)) : eyeX;
		final float originY = apertureRadius > 0.00001F ? eyeY + ((uY * diskX * apertureRadius) + (vY * diskY * apertureRadius)) : eyeY;
		final float originZ = apertureRadius > 0.00001F ? eyeZ + ((uZ * diskX * apertureRadius) + (vZ * diskY * apertureRadius)) : eyeZ;
		
//		Compute the ray direction:
		final float directionX = pointOnImagePlaneX - originX;
		final float directionY = pointOnImagePlaneY - originY;
		final float directionZ = pointOnImagePlaneZ - originZ;
		final float directionLengthReciprocal = rsqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
//		Compute offsets:
		final int pixelArrayOffset = getGlobalId() * 2;
		
//		Fill in the ray array:
		ray3FSetOrigin(originX, originY, originZ);
		ray3FSetDirection(directionNormalizedX, directionNormalizedY, directionNormalizedZ);
		ray3FSetTMinimum(DEFAULT_T_MINIMUM);
		ray3FSetTMaximum(DEFAULT_T_MAXIMUM);
		
//		Fill in the pixel array:
		this.pixelArray[pixelArrayOffset + 0] = pixelX;
		this.pixelArray[pixelArrayOffset + 1] = pixelY;
		
		return true;
	}
	
	/**
	 * Returns the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found.
	 * 
	 * @return the parametric T value for the closest primitive in world space, or {@code 0.0F} if no intersection was found
	 */
	protected final float primitiveIntersectionT() {
		boolean hasFoundIntersection = false;
		
		for(int index = 0; index < this.primitiveCount; index++) {
			final int primitiveArrayOffset = index * Primitive.ARRAY_LENGTH;
			final int primitiveArrayOffsetBoundingVolumeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_ID;
			final int primitiveArrayOffsetBoundingVolumeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
			final int primitiveArrayOffsetShapeID = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_ID;
			final int primitiveArrayOffsetShapeOffset = primitiveArrayOffset + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			final int boundingVolumeID = this.primitiveArray[primitiveArrayOffsetBoundingVolumeID];
			final int boundingVolumeOffset = this.primitiveArray[primitiveArrayOffsetBoundingVolumeOffset];
			final int shapeID = this.primitiveArray[primitiveArrayOffsetShapeID];
			final int shapeOffset = this.primitiveArray[primitiveArrayOffsetShapeOffset];
			
			final float tMinimumWorldSpace = ray3FGetTMinimum();
			final float tMaximumWorldSpace = ray3FGetTMaximum();
			
			boolean isIntersectingBoundingVolume = false;
			
//			TODO: Find out what causes the order of the if-statements to fail. If InfiniteBoundingVolume3F.ID is checked in the last if-statement, the plane will disappear.
			if(boundingVolumeID == InfiniteBoundingVolume3F.ID) {
				isIntersectingBoundingVolume = true;
			} else if(boundingVolumeID == AxisAlignedBoundingBox3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FAxisAlignedBoundingBox3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			} else if(boundingVolumeID == BoundingSphere3F.ID) {
				isIntersectingBoundingVolume = boundingVolume3FBoundingSphere3FContainsOrIntersects(boundingVolumeOffset, tMinimumWorldSpace, tMaximumWorldSpace);
			}
			
			if(isIntersectingBoundingVolume) {
				ray3FSetMatrix44FTransformWorldToObject(index);
				
				float tObjectSpace = 0.0F;
				
				final float tMinimumObjectSpace = ray3FGetTMinimum();
				final float tMaximumObjectSpace = ray3FGetTMaximum();
				
				if(shapeID == Cone3F.ID) {
					tObjectSpace = shape3FCone3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Disk3F.ID) {
					tObjectSpace = shape3FDisk3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Plane3F.ID) {
					tObjectSpace = shape3FPlane3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == RectangularCuboid3F.ID) {
					tObjectSpace = shape3FRectangularCuboid3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Sphere3F.ID) {
					tObjectSpace = shape3FSphere3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Torus3F.ID) {
					tObjectSpace = shape3FTorus3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == Triangle3F.ID) {
					tObjectSpace = shape3FTriangle3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				} else if(shapeID == TriangleMesh3F.ID) {
					tObjectSpace = shape3FTriangleMesh3FIntersectionT(shapeOffset, tMinimumObjectSpace, tMaximumObjectSpace);
				}
				
				if(tObjectSpace > tMinimumObjectSpace && tObjectSpace < tMaximumObjectSpace) {
					ray3FSetTMaximum(tObjectSpace);
					
					hasFoundIntersection = true;
				}
				
				ray3FSetMatrix44FTransformObjectToWorld(index);
			}
		}
		
		return hasFoundIntersection ? ray3FGetTMaximum() : 0.0F;
	}
	
	/**
	 * Returns the {@code float[]} with the pixel samples.
	 * 
	 * @return the {@code float[]} with the pixel samples
	 */
	protected final float[] getPixelArray() {
		return getAndReturn(this.pixelArray);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransform(final int matrix44FArrayOffsetMatrix, final int matrix44FArrayOffsetMatrixInverse) {
//		Retrieve the matrix elements:
		final float matrixElement11 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixElement12 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixElement13 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixElement14 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float matrixElement21 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixElement22 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixElement23 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixElement24 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float matrixElement31 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixElement32 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixElement33 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float matrixElement34 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float matrixElement41 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float matrixElement42 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float matrixElement43 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float matrixElement44 = this.matrix44FArray[matrix44FArrayOffsetMatrix + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the matrix inverse elements:
		final float matrixInverseElement11 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float matrixInverseElement12 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float matrixInverseElement13 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float matrixInverseElement21 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float matrixInverseElement22 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float matrixInverseElement23 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float matrixInverseElement31 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float matrixInverseElement32 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float matrixInverseElement33 = this.matrix44FArray[matrix44FArrayOffsetMatrixInverse + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		
//		Retrieve the old variables from the intersection array:
		final float oldOrthonormalBasisGUX = intersectionGetOrthonormalBasisGUComponent1();
		final float oldOrthonormalBasisGUY = intersectionGetOrthonormalBasisGUComponent2();
		final float oldOrthonormalBasisGUZ = intersectionGetOrthonormalBasisGUComponent3();
		final float oldOrthonormalBasisGVX = intersectionGetOrthonormalBasisGVComponent1();
		final float oldOrthonormalBasisGVY = intersectionGetOrthonormalBasisGVComponent2();
		final float oldOrthonormalBasisGVZ = intersectionGetOrthonormalBasisGVComponent3();
		final float oldOrthonormalBasisGWX = intersectionGetOrthonormalBasisGWComponent1();
		final float oldOrthonormalBasisGWY = intersectionGetOrthonormalBasisGWComponent2();
		final float oldOrthonormalBasisGWZ = intersectionGetOrthonormalBasisGWComponent3();
		final float oldOrthonormalBasisSUX = intersectionGetOrthonormalBasisSUComponent1();
		final float oldOrthonormalBasisSUY = intersectionGetOrthonormalBasisSUComponent2();
		final float oldOrthonormalBasisSUZ = intersectionGetOrthonormalBasisSUComponent3();
		final float oldOrthonormalBasisSVX = intersectionGetOrthonormalBasisSVComponent1();
		final float oldOrthonormalBasisSVY = intersectionGetOrthonormalBasisSVComponent2();
		final float oldOrthonormalBasisSVZ = intersectionGetOrthonormalBasisSVComponent3();
		final float oldOrthonormalBasisSWX = intersectionGetOrthonormalBasisSWComponent1();
		final float oldOrthonormalBasisSWY = intersectionGetOrthonormalBasisSWComponent2();
		final float oldOrthonormalBasisSWZ = intersectionGetOrthonormalBasisSWComponent3();
		final float oldSurfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float oldSurfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float oldSurfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
//		Transform the U-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGUX, oldOrthonormalBasisGUY, oldOrthonormalBasisGUZ);
		
//		Retrieve the transformed U-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGUX = vector3FGetComponent1();
		final float newOrthonormalBasisGUY = vector3FGetComponent2();
		final float newOrthonormalBasisGUZ = vector3FGetComponent3();
		
//		Transform the V-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGVX, oldOrthonormalBasisGVY, oldOrthonormalBasisGVZ);
		
//		Retrieve the transformed V-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGVX = vector3FGetComponent1();
		final float newOrthonormalBasisGVY = vector3FGetComponent2();
		final float newOrthonormalBasisGVZ = vector3FGetComponent3();
		
//		Transform the W-direction of the geometric orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisGWX, oldOrthonormalBasisGWY, oldOrthonormalBasisGWZ);
		
//		Retrieve the transformed W-direction of the geometric orthonormal basis:
		final float newOrthonormalBasisGWX = vector3FGetComponent1();
		final float newOrthonormalBasisGWY = vector3FGetComponent2();
		final float newOrthonormalBasisGWZ = vector3FGetComponent3();
		
//		Transform the U-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSUX, oldOrthonormalBasisSUY, oldOrthonormalBasisSUZ);
		
//		Retrieve the transformed U-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSUX = vector3FGetComponent1();
		final float newOrthonormalBasisSUY = vector3FGetComponent2();
		final float newOrthonormalBasisSUZ = vector3FGetComponent3();
		
//		Transform the V-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSVX, oldOrthonormalBasisSVY, oldOrthonormalBasisSVZ);
		
//		Retrieve the transformed V-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSVX = vector3FGetComponent1();
		final float newOrthonormalBasisSVY = vector3FGetComponent2();
		final float newOrthonormalBasisSVZ = vector3FGetComponent3();
		
//		Transform the W-direction of the shading orthonormal basis:
		vector3FSetMatrix44FTransformTransposeNormalize(matrixInverseElement11, matrixInverseElement12, matrixInverseElement13, matrixInverseElement21, matrixInverseElement22, matrixInverseElement23, matrixInverseElement31, matrixInverseElement32, matrixInverseElement33, oldOrthonormalBasisSWX, oldOrthonormalBasisSWY, oldOrthonormalBasisSWZ);
		
//		Retrieve the transformed W-direction of the shading orthonormal basis:
		final float newOrthonormalBasisSWX = vector3FGetComponent1();
		final float newOrthonormalBasisSWY = vector3FGetComponent2();
		final float newOrthonormalBasisSWZ = vector3FGetComponent3();
		
//		Transform the surface intersection point:
		point3FSetMatrix44FTransformAndDivide(matrixElement11, matrixElement12, matrixElement13, matrixElement14, matrixElement21, matrixElement22, matrixElement23, matrixElement24, matrixElement31, matrixElement32, matrixElement33, matrixElement34, matrixElement41, matrixElement42, matrixElement43, matrixElement44, oldSurfaceIntersectionPointX, oldSurfaceIntersectionPointY, oldSurfaceIntersectionPointZ);
		
//		Retrieve the transformed surface intersection point:
		final float newSurfaceIntersectionPointX = point3FGetComponent1();
		final float newSurfaceIntersectionPointY = point3FGetComponent2();
		final float newSurfaceIntersectionPointZ = point3FGetComponent3();
		
//		Update the intersection array:
		intersectionSetOrthonormalBasisG(newOrthonormalBasisGUX, newOrthonormalBasisGUY, newOrthonormalBasisGUZ, newOrthonormalBasisGVX, newOrthonormalBasisGVY, newOrthonormalBasisGVZ, newOrthonormalBasisGWX, newOrthonormalBasisGWY, newOrthonormalBasisGWZ);
		intersectionSetOrthonormalBasisS(newOrthonormalBasisSUX, newOrthonormalBasisSUY, newOrthonormalBasisSUZ, newOrthonormalBasisSVX, newOrthonormalBasisSVY, newOrthonormalBasisSVZ, newOrthonormalBasisSWX, newOrthonormalBasisSWY, newOrthonormalBasisSWZ);
		intersectionSetSurfaceIntersectionPoint(newSurfaceIntersectionPointX, newSurfaceIntersectionPointY, newSurfaceIntersectionPointZ);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformObjectToWorld(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2, primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
//	TODO: Add Javadocs!
	protected final void intersectionTransformWorldToObject(final int primitiveIndex) {
		intersectionTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE, primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedAny() {
		lightEvaluateRadianceEmittedClear();
		lightEvaluateRadianceEmittedAnyLDRImageLight();
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedAnyLDRImageLight() {
		final int lightLDRImageLightCount = this.lightLDRImageLightCount;
		
		if(lightLDRImageLightCount > 0) {
			final int offset = min((int)(random() * lightLDRImageLightCount), lightLDRImageLightCount - 1);
			
			final float probabilityDensityFunctionValue = 1.0F / lightLDRImageLightCount;
			
			lightEvaluateRadianceEmittedOneLDRImageLight(offset, probabilityDensityFunctionValue);
		}
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedClear() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedOneLDRImageLight(final int offset, final float probabilityDensityFunctionValue) {
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
		final float textureCoordinatesU = 0.5F + atan2(rayDirectionZ, rayDirectionX) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = 0.5F - asinpi(rayDirectionY);
		
		final float angleRadians = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_ANGLE_RADIANS];
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float scaleU = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 0];
		final float scaleV = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 1];
		
		final int resolutionX = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_X]);
		final int resolutionY = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_Y]);
		
		final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
		final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
		
		final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
		final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
		
		final float x = positiveModuloF(textureCoordinatesScaledU, resolutionX);
		final float y = positiveModuloF(textureCoordinatesScaledV, resolutionY);
		
		final int minimumX = (int)(floor(x));
		final int maximumX = (int)(ceil(x));
		
		final int minimumY = (int)(floor(y));
		final int maximumY = (int)(ceil(y));
		
		final int offsetImage = offset + LDRImageLight.ARRAY_OFFSET_IMAGE;
		final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		
		final int color00RGB = (int)(this.lightLDRImageLightArray[offsetColor00RGB]);
		final int color01RGB = (int)(this.lightLDRImageLightArray[offsetColor01RGB]);
		final int color10RGB = (int)(this.lightLDRImageLightArray[offsetColor10RGB]);
		final int color11RGB = (int)(this.lightLDRImageLightArray[offsetColor11RGB]);
		
		final float tX = x - minimumX;
		final float tY = y - minimumY;
		
		final float component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
		final float component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
		final float component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
		
		color3FLHSSet(component1 / probabilityDensityFunctionValue, component2 / probabilityDensityFunctionValue, component3 / probabilityDensityFunctionValue);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFBegin() {
//		Initialize the orthonormal basis:
		orthonormalBasis33FSetIntersectionOrthonormalBasisS();
		
//		Retrieve the ray direction in world space:
		final float rayDirectionWorldSpaceX = ray3FGetDirectionComponent1();
		final float rayDirectionWorldSpaceY = ray3FGetDirectionComponent2();
		final float rayDirectionWorldSpaceZ = ray3FGetDirectionComponent3();
		
//		Compute the outgoing direction in world space as the negated ray direction in world space:
		final float outgoingWorldSpaceX = -rayDirectionWorldSpaceX;
		final float outgoingWorldSpaceY = -rayDirectionWorldSpaceY;
		final float outgoingWorldSpaceZ = -rayDirectionWorldSpaceZ;
		
//		Transform the outgoing direction in world space to the outgoing direction in shade space:
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(outgoingWorldSpaceX, outgoingWorldSpaceY, outgoingWorldSpaceZ);
		
//		Retrieve the outgoing direction in shade space:
		final float outgoingShadeSpaceX = vector3FGetComponent1();
		final float outgoingShadeSpaceY = vector3FGetComponent2();
		final float outgoingShadeSpaceZ = vector3FGetComponent3();
		
//		Retrieve the normal in world space:
		final float normalWorldSpaceX = intersectionGetOrthonormalBasisSWComponent1();
		final float normalWorldSpaceY = intersectionGetOrthonormalBasisSWComponent2();
		final float normalWorldSpaceZ = intersectionGetOrthonormalBasisSWComponent3();
		
//		Transform the normal in world space to the normal in shade space:
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(normalWorldSpaceX, normalWorldSpaceY, normalWorldSpaceZ);
		
//		Retrieve the normal in shade space:
		final float normalShadeSpaceX = vector3FGetComponent1();
		final float normalShadeSpaceY = vector3FGetComponent2();
		final float normalShadeSpaceZ = vector3FGetComponent3();
		
//		Set the values:
		materialBXDFSetNormal(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ);
		materialBXDFSetOutgoing(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFEnd() {
//		Retrieve the incoming direction in shade space:
		final float incomingShadeSpaceX = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 0];
		final float incomingShadeSpaceY = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 1];
		final float incomingShadeSpaceZ = this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 2];
		
//		Transform the incoming direction in shade space to the incoming direction in world space:
		vector3FSetOrthonormalBasis33FTransformNormalize(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		
//		Retrieve the incoming direction in world space:
		final float incomingWorldSpaceX = -vector3FGetComponent1();
		final float incomingWorldSpaceY = -vector3FGetComponent2();
		final float incomingWorldSpaceZ = -vector3FGetComponent3();
		
//		Set the values:
		vector3FSet(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 0] = incomingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 1] = incomingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 2] = incomingZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetNormal(final float normalX, final float normalY, final float normalZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0] = normalX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1] = normalY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2] = normalZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialBXDFSetOutgoing(final float outgoingX, final float outgoingY, final float outgoingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0] = outgoingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1] = outgoingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2] = outgoingZ;
	}
	
//	TODO: Add Javadocs!
	protected final void materialEmittance() {
		final int primitiveIndex = intersectionGetPrimitiveIndex();
		final int primitiveArrayOffset = primitiveIndex * Primitive.ARRAY_LENGTH;
		final int materialID = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_ID];
		final int materialOffset = this.primitiveArray[primitiveArrayOffset + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET];
		final int materialOffsetTextureEmission = materialOffset + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
		
		int textureEmission = 0;
		
		if(materialID == ClearCoatMaterial.ID) {
			textureEmission = this.materialClearCoatMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == GlassMaterial.ID) {
			textureEmission = this.materialGlassMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == GlossyMaterial.ID) {
			textureEmission = this.materialGlossyMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == MatteMaterial.ID) {
			textureEmission = this.materialMatteMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == MirrorMaterial.ID) {
			textureEmission = this.materialMirrorMaterialArray[materialOffsetTextureEmission];
		}
		
		final int textureEmissionID = (textureEmission >>> 16) & 0xFFFF;
		final int textureEmissionOffset = textureEmission & 0xFFFF;
		
		textureEvaluate(textureEmissionID, textureEmissionOffset);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray is constructed by transforming the current ray in {@code ray3FArray_$private$8} with the object-to-world matrix of the primitive at index {@code primitiveIndex}.
	 * 
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void ray3FSetMatrix44FTransformObjectToWorld(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2);
	}
	
	/**
	 * Sets a ray in {@link #ray3FArray_$private$8}.
	 * <p>
	 * The ray is constructed by transforming the current ray in {@code ray3FArray_$private$8} with the world-to-object matrix of the primitive at index {@code primitiveIndex}.
	 * 
	 * @param primitiveIndex the index of the primitive
	 */
	protected final void ray3FSetMatrix44FTransformWorldToObject(final int primitiveIndex) {
		doRay3FSetMatrix44FTransform(primitiveIndex * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE);
	}
	
//	TODO: Add Javadocs!
	protected final void textureEvaluate(final int textureID, final int textureOffset) {
		/*
//		This takes too long. From 9 - 10 ms to 13 - 14 ms.
		if(textureID == BlendTexture.ID) {
			final int textureAID = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
			final int textureAOffset = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
			final int textureBID = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
			final int textureBOffset = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
			
			final float tComponent1 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_1];
			final float tComponent2 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_2];
			final float tComponent3 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_3];
			
			textureEvaluateExcludingBlendTexture(textureAID, textureAOffset);
			
			final float textureAComponent1 = color3FLHSGetComponent1();
			final float textureAComponent2 = color3FLHSGetComponent2();
			final float textureAComponent3 = color3FLHSGetComponent3();
			
			textureEvaluateExcludingBlendTexture(textureBID, textureBOffset);
			
			final float textureBComponent1 = color3FLHSGetComponent1();
			final float textureBComponent2 = color3FLHSGetComponent2();
			final float textureBComponent3 = color3FLHSGetComponent3();
			
			final float component1 = lerp(textureAComponent1, textureBComponent1, tComponent1);
			final float component2 = lerp(textureAComponent2, textureBComponent2, tComponent2);
			final float component3 = lerp(textureAComponent3, textureBComponent3, tComponent3);
			
			color3FLHSSet(component1, component2, component3);
		} else {
			textureEvaluateExcludingBlendTexture(textureID, textureOffset);
		}
		*/
		
		textureEvaluateExcludingBlendTexture(textureID, textureOffset);
	}
	
//	TODO: Add Javadocs!
	protected final void textureEvaluateExcludingBlendTexture(final int textureID, final int textureOffset) {
		int currentTextureID = textureID;
		int currentTextureOffset = textureOffset;
		
		float component1 = 0.0F;
		float component2 = 0.0F;
		float component3 = 0.0F;
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float textureCoordinatesU = intersectionGetTextureCoordinatesComponent1();
		final float textureCoordinatesV = intersectionGetTextureCoordinatesComponent2();
		
		while(currentTextureID != -1 && currentTextureOffset != -1) {
			if(currentTextureID == BullseyeTexture.ID) {
				final float originX = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 0];
				final float originY = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 1];
				final float originZ = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 2];
				
				final int textureAID = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
				final int textureAOffset = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
				final int textureBID = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
				final int textureBOffset = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
				
				final float scale = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_SCALE];
				
				final float distance = point3FDistance(originX, originY, originZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
				final float distanceScaled = distance * scale;
				final float distanceScaledRemainder = remainder(distanceScaled, 1.0F);
				
				final boolean isTextureA = distanceScaledRemainder > 0.5F;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == CheckerboardTexture.ID) {
				final float angleRadians = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final int textureAID = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A_ID]);
				final int textureAOffset = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET]);
				final int textureBID = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B_ID]);
				final int textureBOffset = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET]);
				
				final float scaleU = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 1];
				
				final boolean isU = fractionalPart((textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin) * scaleU, false) > 0.5F;
				final boolean isV = fractionalPart((textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin) * scaleV, false) > 0.5F;
				
				final boolean isTextureA = isU ^ isV;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == ConstantTexture.ID) {
				component1 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 0];
				component2 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 1];
				component3 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 2];
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == LDRImageTexture.ID) {
				final float angleRadians = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final float scaleU = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 1];
				
				final int resolutionX = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_X]);
				final int resolutionY = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_Y]);
				
				final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
				final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
				
				final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
				final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
				
				final float x = positiveModuloF(textureCoordinatesScaledU, resolutionX);
				final float y = positiveModuloF(textureCoordinatesScaledV, resolutionY);
				
				final int minimumX = (int)(floor(x));
				final int maximumX = (int)(ceil(x));
				
				final int minimumY = (int)(floor(y));
				final int maximumY = (int)(ceil(y));
				
				final int offsetImage = currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_IMAGE;
				final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				
				final int color00RGB = (int)(this.textureLDRImageTextureArray[offsetColor00RGB]);
				final int color01RGB = (int)(this.textureLDRImageTextureArray[offsetColor01RGB]);
				final int color10RGB = (int)(this.textureLDRImageTextureArray[offsetColor10RGB]);
				final int color11RGB = (int)(this.textureLDRImageTextureArray[offsetColor11RGB]);
				
				final float tX = x - minimumX;
				final float tY = y - minimumY;
				
				component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
				component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
				component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == MarbleTexture.ID) {
				final int colorARGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_A]);
				final int colorBRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_B]);
				final int colorCRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_C]);
				
				final float frequency = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_FREQUENCY];
				final float scale = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_SCALE];
				
				final int octaves = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float x = surfaceIntersectionPointX * frequency;
				final float y = surfaceIntersectionPointY * frequency;
				final float z = surfaceIntersectionPointZ * frequency;
				final float r = scale * perlinTurbulenceXYZ(x, y, z, octaves);
				final float s = 2.0F * abs(sin(x + r));
				final float t = s < 1.0F ? s : s - 1.0F;
				
				component1 = s < 1.0F ? lerp(colorRGBIntToRFloat(colorCRGB), colorRGBIntToRFloat(colorBRGB), t) : lerp(colorRGBIntToRFloat(colorBRGB), colorRGBIntToRFloat(colorARGB), t);
				component2 = s < 1.0F ? lerp(colorRGBIntToGFloat(colorCRGB), colorRGBIntToGFloat(colorBRGB), t) : lerp(colorRGBIntToGFloat(colorBRGB), colorRGBIntToGFloat(colorARGB), t);
				component3 = s < 1.0F ? lerp(colorRGBIntToBFloat(colorCRGB), colorRGBIntToBFloat(colorBRGB), t) : lerp(colorRGBIntToBFloat(colorBRGB), colorRGBIntToBFloat(colorARGB), t);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SimplexFractionalBrownianMotionTexture.ID) {
				final int colorRGB = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_COLOR]);
				
				final float frequency = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_FREQUENCY];
				final float gain = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_GAIN];
				
				final int octaves = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float colorR = colorRGBIntToRFloat(colorRGB);
				final float colorG = colorRGBIntToGFloat(colorRGB);
				final float colorB = colorRGBIntToBFloat(colorRGB);
				
				final float noise = simplexFractionalBrownianMotionXYZ(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ, frequency, gain, 0.0F, 1.0F, octaves);
				
				component1 = colorR * noise;
				component2 = colorG * noise;
				component3 = colorB * noise;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SurfaceNormalTexture.ID) {
				component1 = (surfaceNormalX + 1.0F) * 0.5F;
				component2 = (surfaceNormalY + 1.0F) * 0.5F;
				component3 = (surfaceNormalZ + 1.0F) * 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == UVTexture.ID) {
				component1 = textureCoordinatesU;
				component2 = textureCoordinatesV;
				component3 = 0.0F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else {
//				The Texture is not supported:
				component1 = 0.5F;
				component2 = 0.5F;
				component3 = 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			}
		}
		
		color3FLHSSet(component1, component2, component3);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doRay3FSetMatrix44FTransform(final int matrix44FArrayOffset) {
//		Retrieve the matrix elements:
		final float element11 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_1];
		final float element12 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_2];
		final float element13 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_3];
		final float element14 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_1_4];
		final float element21 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_1];
		final float element22 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_2];
		final float element23 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_3];
		final float element24 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_2_4];
		final float element31 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_1];
		final float element32 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_2];
		final float element33 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_3];
		final float element34 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_3_4];
		final float element41 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_1];
		final float element42 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_2];
		final float element43 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_3];
		final float element44 = this.matrix44FArray[matrix44FArrayOffset + Matrix44F.ARRAY_OFFSET_ELEMENT_4_4];
		
//		Retrieve the ray origin components from the old space:
		final float oldOriginX = ray3FGetOriginComponent1();
		final float oldOriginY = ray3FGetOriginComponent2();
		final float oldOriginZ = ray3FGetOriginComponent3();
		
//		Retrieve the ray direction components from the old space:
		final float oldDirectionX = ray3FGetDirectionComponent1();
		final float oldDirectionY = ray3FGetDirectionComponent2();
		final float oldDirectionZ = ray3FGetDirectionComponent3();
		
//		Retrieve the ray boundary variables from the old space:
		final float oldTMinimum = ray3FGetTMinimum();
		final float oldTMaximum = ray3FGetTMaximum();
		
//		Transform the ray origin from the old space to the new space:
		point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldOriginX, oldOriginY, oldOriginZ);
		
//		Transform the ray direction from the old space to the new space:
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, oldDirectionX, oldDirectionY, oldDirectionZ);
		
//		Retrieve the ray origin components from the new space:
		final float newOriginX = point3FGetComponent1();
		final float newOriginY = point3FGetComponent2();
		final float newOriginZ = point3FGetComponent3();
		
//		Retrieve the ray direction components from the new space:
		final float newDirectionX = vector3FGetComponent1();
		final float newDirectionY = vector3FGetComponent2();
		final float newDirectionZ = vector3FGetComponent3();
		
//		Initialize the ray boundary variables of the new space to the ray boundary variables from the old space:
		float newTMinimum = oldTMinimum;
		float newTMaximum = oldTMaximum;
		
//		Check if the new minimum ray boundary should be computed:
		if(newTMinimum > DEFAULT_T_MINIMUM && newTMinimum < DEFAULT_T_MAXIMUM) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMinimumX = oldOriginX + oldDirectionX * oldTMinimum;
			final float oldReferencePointTMinimumY = oldOriginY + oldDirectionY * oldTMinimum;
			final float oldReferencePointTMinimumZ = oldOriginZ + oldDirectionZ * oldTMinimum;
			
//			Transform the reference point from the old space to the new space:
			point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMinimumX, oldReferencePointTMinimumY, oldReferencePointTMinimumZ);
			
//			Retrieve the reference point from the new space:
			final float newReferencePointTMinimumX = point3FGetComponent1();
			final float newReferencePointTMinimumY = point3FGetComponent2();
			final float newReferencePointTMinimumZ = point3FGetComponent3();
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMinimum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMinimumX, newReferencePointTMinimumY, newReferencePointTMinimumZ);
			
//			Update the new minimum ray boundary:
			newTMinimum = abs(distanceOriginToReferencePointTMinimum);
		}
		
//		Check if the new maximum ray bounday should be computed:
		if(newTMaximum > DEFAULT_T_MINIMUM && newTMaximum < DEFAULT_T_MAXIMUM) {
//			Compute a reference point in the old space:
			final float oldReferencePointTMaximumX = oldOriginX + oldDirectionX * oldTMaximum;
			final float oldReferencePointTMaximumY = oldOriginY + oldDirectionY * oldTMaximum;
			final float oldReferencePointTMaximumZ = oldOriginZ + oldDirectionZ * oldTMaximum;
			
//			Transform the reference point from the old space to the new space:
			point3FSetMatrix44FTransformAndDivide(element11, element12, element13, element14, element21, element22, element23, element24, element31, element32, element33, element34, element41, element42, element43, element44, oldReferencePointTMaximumX, oldReferencePointTMaximumY, oldReferencePointTMaximumZ);
			
//			Retrieve the reference point from the new space:
			final float newReferencePointTMaximumX = point3FGetComponent1();
			final float newReferencePointTMaximumY = point3FGetComponent2();
			final float newReferencePointTMaximumZ = point3FGetComponent3();
			
//			Compute the distance from the origin in the new space to the reference point in the new space:
			final float distanceOriginToReferencePointTMaximum = point3FDistance(newOriginX, newOriginY, newOriginZ, newReferencePointTMaximumX, newReferencePointTMaximumY, newReferencePointTMaximumZ);
			
//			Update the new maximum ray boundary:
			newTMaximum = abs(distanceOriginToReferencePointTMaximum);
		}
		
//		Set the new variables:
		ray3FSetOrigin(newOriginX, newOriginY, newOriginZ);
		ray3FSetDirection(newDirectionX, newDirectionY, newDirectionZ);
		ray3FSetTMinimum(newTMinimum);
		ray3FSetTMaximum(newTMaximum);
	}
	
	private void doSetupPixelArray() {
		put(this.pixelArray = Floats.array(getResolution() * 2, 0.0F));
	}
	
	private void doSetupScene() {
		final SceneCompiler sceneCompiler = new SceneCompiler();
		
		final CompiledScene compiledScene = sceneCompiler.compile(getScene());
		
		put(super.boundingVolume3FAxisAlignedBoundingBox3FArray = compiledScene.getBoundingVolume3FAxisAlignedBoundingBox3FArray());
		put(super.boundingVolume3FBoundingSphere3FArray = compiledScene.getBoundingVolume3FBoundingSphere3FArray());
		put(super.shape3FCone3FArray = compiledScene.getShape3FCone3FArray());
		put(super.shape3FDisk3FArray = compiledScene.getShape3FDisk3FArray());
		put(super.shape3FPlane3FArray = compiledScene.getShape3FPlane3FArray());
		put(super.shape3FRectangularCuboid3FArray = compiledScene.getShape3FRectangularCuboid3FArray());
		put(super.shape3FSphere3FArray = compiledScene.getShape3FSphere3FArray());
		put(super.shape3FTorus3FArray = compiledScene.getShape3FTorus3FArray());
		put(super.shape3FTriangle3FArray = compiledScene.getShape3FTriangle3FArray());
		put(super.shape3FTriangleMesh3FArray = compiledScene.getShape3FTriangleMesh3FArray());
		
		put(this.cameraArray = compiledScene.getCameraArray());
		put(this.lightLDRImageLightArray = compiledScene.getLightLDRImageLightArray());
		put(this.lightLDRImageLightOffsetArray = compiledScene.getLightLDRImageLightOffsetArray());
		put(this.materialClearCoatMaterialArray = compiledScene.getMaterialClearCoatMaterialArray());
		put(this.materialGlassMaterialArray = compiledScene.getMaterialGlassMaterialArray());
		put(this.materialGlossyMaterialArray = compiledScene.getMaterialGlossyMaterialArray());
		put(this.materialMatteMaterialArray = compiledScene.getMaterialMatteMaterialArray());
		put(this.materialMirrorMaterialArray = compiledScene.getMaterialMirrorMaterialArray());
		put(this.matrix44FArray = compiledScene.getMatrix44FArray());
		put(this.primitiveArray = compiledScene.getPrimitiveArray());
		put(this.textureBlendTextureArray = compiledScene.getTextureBlendTextureArray());
		put(this.textureBullseyeTextureArray = compiledScene.getTextureBullseyeTextureArray());
		put(this.textureCheckerboardTextureArray = compiledScene.getTextureCheckerboardTextureArray());
		put(this.textureConstantTextureArray = compiledScene.getTextureConstantTextureArray());
		put(this.textureLDRImageTextureArray = compiledScene.getTextureLDRImageTextureArray());
		put(this.textureMarbleTextureArray = compiledScene.getTextureMarbleTextureArray());
		put(this.textureSimplexFractionalBrownianMotionTextureArray = compiledScene.getTextureSimplexFractionalBrownianMotionTextureArray());
		
		this.lightLDRImageLightCount = compiledScene.getLightLDRImageLightCount();
		this.primitiveCount = compiledScene.getPrimitiveCount();
	}
}