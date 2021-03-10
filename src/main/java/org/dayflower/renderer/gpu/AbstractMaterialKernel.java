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
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;

import java.lang.reflect.Field;

import org.dayflower.scene.Material;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MirrorMaterial;

//TODO: Add Javadocs!
public abstract class AbstractMaterialKernel extends AbstractTextureKernel {
//	TODO: Add Javadocs!
	protected static final int B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION = 1 << 0;
	
//	TODO: Add Javadocs!
	protected static final int B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION = 1 << 1;
	
//	TODO: Add Javadocs!
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE = 1 << 2;
	
//	TODO: Add Javadocs!
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY = 1 << 3;
	
//	TODO: Add Javadocs!
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR = 1 << 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final int B_S_D_F_ARRAY_LENGTH = 11;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F = 3;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT = 2;
	private static final int B_S_D_F_ARRAY_OFFSET_ETA = 0;
	private static final int B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING = 1;
	private static final int B_S_D_F_RESULT_ARRAY_LENGTH = 13;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING = 3;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL = 6;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING = 9;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 12;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_RESULT = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ID = 1;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH = 6;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A = 4;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_ANGLE_RADIANS = 0;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B = 5;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 1;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ID = 2;
	private static final int B_X_D_F_RESULT_ARRAY_LENGTH = 13;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING = 3;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL = 6;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING = 9;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 12;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_RESULT = 0;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING = 0;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL = 3;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING = 6;
	private static final int MATERIAL_B_X_D_F_ARRAY_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] bSDFArray_$private$11;
	
//	TODO: Add Javadocs!
	protected float[] bSDFResultArray_$private$10;
	
//	TODO: Add Javadocs!
	protected float[] bXDFLambertianBRDFArray_$private$3;
	
//	TODO: Add Javadocs!
	protected float[] bXDFOrenNayarBRDFArray_$private$6;
	
//	TODO: Add Javadocs!
	protected float[] bXDFResultArray_$private$10;
	
//	TODO: Add Javadocs!
	protected float[] materialBXDFResultArray_$private$16;
	
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected AbstractMaterialKernel() {
		this.bSDFArray_$private$11 = new float[B_S_D_F_ARRAY_LENGTH];
		this.bSDFResultArray_$private$10 = new float[B_S_D_F_RESULT_ARRAY_LENGTH];
		this.bXDFLambertianBRDFArray_$private$3 = new float[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH];
		this.bXDFOrenNayarBRDFArray_$private$6 = new float[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH];
		this.bXDFResultArray_$private$10 = new float[B_X_D_F_RESULT_ARRAY_LENGTH];
		this.materialBXDFResultArray_$private$16 = new float[MATERIAL_B_X_D_F_ARRAY_SIZE];
		this.materialClearCoatMaterialArray = new int[1];
		this.materialGlassMaterialArray = new int[1];
		this.materialGlossyMaterialArray = new int[1];
		this.materialMatteMaterialArray = new int[1];
		this.materialMirrorMaterialArray = new int[1];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@SuppressWarnings("static-method")
	protected final boolean materialIsSpecular(final int materialID) {
		return materialID == GlassMaterial.ID || materialID == MirrorMaterial.ID;
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunction(final int materialID, final int materialOffset) {
		if(materialID == ClearCoatMaterial.ID) {
			return materialSampleDistributionFunctionClearCoatMaterial(materialOffset);
		} else if(materialID == GlassMaterial.ID) {
			return materialSampleDistributionFunctionGlassMaterial(materialOffset);
		} else if(materialID == GlossyMaterial.ID) {
			return materialSampleDistributionFunctionGlossyMaterial(materialOffset);
		} else if(materialID == MatteMaterial.ID) {
			return materialSampleDistributionFunctionMatteMaterial(materialOffset);
		} else if(materialID == MirrorMaterial.ID) {
			return materialSampleDistributionFunctionMirrorMaterial(materialOffset);
		} else {
			return false;
		}
	}
	
//	TODO: Add Javadocs!
	protected final boolean materialSampleDistributionFunctionClearCoatMaterial(final int materialClearCoatMaterialArrayOffset) {
		final int textureKD = this.materialClearCoatMaterialArray[materialClearCoatMaterialArrayOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		final int textureKS = this.materialClearCoatMaterialArray[materialClearCoatMaterialArrayOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_S];
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
	protected final boolean materialSampleDistributionFunctionGlassMaterial(final int materialGlassMaterialArrayOffset) {
		final int textureEta = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_ETA];
		final int textureEtaID = (textureEta >>> 16) & 0xFFFF;
		final int textureEtaOffset = textureEta & 0xFFFF;
		final int textureKR = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		final int textureKT = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_T];
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
	protected final boolean materialSampleDistributionFunctionGlossyMaterial(final int materialGlossyMaterialArrayOffset) {
		/*
		 * Material:
		 */
		
//		Retrieve indices and offsets:
		final int textureKR = this.materialGlossyMaterialArray[materialGlossyMaterialArrayOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		final int textureRoughness = this.materialGlossyMaterialArray[materialGlossyMaterialArrayOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS];
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
	protected final boolean materialSampleDistributionFunctionMatteMaterial(final int materialMatteMaterialArrayOffset) {
		final int textureKD = this.materialMatteMaterialArray[materialMatteMaterialArrayOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_K_D];
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
	protected final boolean materialSampleDistributionFunctionMirrorMaterial(final int materialMirrorMaterialArrayOffset) {
		final int textureKR = this.materialMirrorMaterialArray[materialMirrorMaterialArrayOffset + MirrorMaterial.ARRAY_OFFSET_TEXTURE_K_R];
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
	protected final void materialEmittance(final int materialID, final int materialOffset) {
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * The following is a test implementation of the Material API in the CPU-renderer that is based on PBRT. It is an early work-in-progress. However, it is necessary in order to support most (if not all) Material implementations.
	 * 
	 * The method prefix "test" is used to avoid method name collisions with the current implementation. It will be removed when this test implementation is used as the official implementation.
	 */
	
//	TODO: Add Javadocs!
	protected final boolean testMaterialBSDFCompute(final int materialID, final int materialOffset) {
		if(materialID == MatteMaterial.ID) {
			return doMaterialBSDFComputeMatteMaterial(materialOffset);
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final float testMaterialBSDFGetEta() {
		return this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA];
	}
	
//	TODO: Add Javadocs!
	protected final int testMaterialBSDFCountBXDFsBySpecularType(final boolean isSpecular) {
		int countBySpecular = 0;
		
		final int countBXDFs = doMaterialBSDFGetBXDFCount();
		
		for(int i = 0; i < countBXDFs; i++) {
			if(isSpecular == doMaterialBXDFIsSpecular(doMaterialBSDFGetBXDF(i))) {
				countBySpecular++;
			}
		}
		
		return countBySpecular;
	}
	
//	TODO: Add Javadocs!
	protected final void testMaterialBSDFEvaluateDistributionFunction() {
		final float incomingX = doMaterialBSDFResultGetIncomingX();
		final float incomingY = doMaterialBSDFResultGetIncomingY();
		final float incomingZ = doMaterialBSDFResultGetIncomingZ();
		
		final float outgoingX = doMaterialBSDFResultGetOutgoingX();
		final float outgoingY = doMaterialBSDFResultGetOutgoingY();
		final float outgoingZ = doMaterialBSDFResultGetOutgoingZ();
		
		final float normalX = intersectionGetOrthonormalBasisGWComponent1();
		final float normalY = intersectionGetOrthonormalBasisGWComponent2();
		final float normalZ = intersectionGetOrthonormalBasisGWComponent3();
		
		final float iDotN = vector3FDotProduct(incomingX, incomingY, incomingZ, normalX, normalY, normalZ);
		final float oDotN = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		
		final boolean isReflecting = iDotN * oDotN > 0.0F;
	}
	
//	TODO: Add Javadocs!
	protected final void testMaterialBSDFEvaluateProbabilityDensityFunction() {
		
	}
	
//	TODO: Add Javadocs!
	protected final void testMaterialBSDFSampleDistributionFunction() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doMaterialBSDFComputeMatteMaterial(final int materialMatteMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KD Texture:
		final int textureKD = this.materialMatteMaterialArray[materialMatteMaterialArrayOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
//		Evaluate the KD Texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD Texture:
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
//		Retrieve the ID and offset for the Angle Texture:
		final int textureAngle = this.materialMatteMaterialArray[materialMatteMaterialArrayOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_ANGLE];
		final int textureAngleID = (textureAngle >>> 16) & 0xFFFF;
		final int textureAngleOffset = textureAngle & 0xFFFF;
		
//		Evaluate the Angle Texture:
		textureEvaluate(textureAngleID, textureAngleOffset);
		
//		Retrieve the average color from the Angle Texture:
		final float floatAngle = color3FLHSGetAverage();
		
		if(isZero(colorKDR) && isZero(colorKDG) && isZero(colorKDB)) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
//		Clear the BSDF:
		doMaterialBSDFClear();
		
		if(isZero(floatAngle)) {
//			Set LambertianBRDF:
			doMaterialBSDFSetBXDF(0, B_X_D_F_LAMBERTIAN_B_R_D_F_ID);
			doMaterialBSDFSetBXDFCount(1);
			doMaterialBSDFSetEta(1.0F);
			doMaterialBSDFSetNegatingIncoming(false);
			
			doMaterialBXDFLambertianBRDFSet(colorKDR, colorKDG, colorKDB);
			
			doMaterialBSDFResultInitialize();
			
			return true;
		}
		
//		Set OrenNayarBRDF:
		doMaterialBSDFSetBXDF(0, B_X_D_F_OREN_NAYAR_B_R_D_F_ID);
		doMaterialBSDFSetBXDFCount(1);
		doMaterialBSDFSetEta(1.0F);
		doMaterialBSDFSetNegatingIncoming(false);
		
		doMaterialBXDFOrenNayarBRDFSet(floatAngle, colorKDR, colorKDG, colorKDB);
		
		doMaterialBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialBSDFIsNegatingIncoming() {
		return !isZero(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING]);
	}
	
	@SuppressWarnings({"static-method", "unused"})
	private boolean doMaterialBXDFIsSpecular(final int id) {
		return false;
	}
	
	private float doMaterialBSDFResultGetIncomingX() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0];
	}
	
	private float doMaterialBSDFResultGetIncomingY() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1];
	}
	
	private float doMaterialBSDFResultGetIncomingZ() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2];
	}
	
	private float doMaterialBSDFResultGetNormalX() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0];
	}
	
	private float doMaterialBSDFResultGetNormalY() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1];
	}
	
	private float doMaterialBSDFResultGetNormalZ() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2];
	}
	
	private float doMaterialBSDFResultGetOutgoingX() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0];
	}
	
	private float doMaterialBSDFResultGetOutgoingY() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1];
	}
	
	private float doMaterialBSDFResultGetOutgoingZ() {
		return this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2];
	}
	
	private float doMaterialBXDFResultGetIncomingX() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0];
	}
	
	private float doMaterialBXDFResultGetIncomingY() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1];
	}
	
	private float doMaterialBXDFResultGetIncomingZ() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2];
	}
	
	private float doMaterialBXDFResultGetNormalX() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0];
	}
	
	private float doMaterialBXDFResultGetNormalY() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1];
	}
	
	private float doMaterialBXDFResultGetNormalZ() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2];
	}
	
	private float doMaterialBXDFResultGetOutgoingX() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0];
	}
	
	private float doMaterialBXDFResultGetOutgoingY() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1];
	}
	
	private float doMaterialBXDFResultGetOutgoingZ() {
		return this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2];
	}
	
	private int doMaterialBSDFGetBXDF(final int index) {
		return (int)(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + index]);
	}
	
	private int doMaterialBSDFGetBXDFCount() {
		return (int)(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT]);
	}
	
	private void doMaterialBSDFClear() {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA] = 1.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 0] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 1] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 2] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 3] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 4] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 5] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 6] = 0.0F;
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + 7] = 0.0F;
	}
	
	private void doMaterialBSDFResultInitialize() {
		doMaterialBSDFResultSetNormalFromIntersection();
		doMaterialBSDFResultSetOutgoingFromRay();
		
		orthonormalBasis33FSetIntersectionOrthonormalBasisS();
		
		doMaterialBXDFResultSetNormalTransformedFromBSDFResult();
		doMaterialBXDFResultSetOutgoingTransformedFromBSDFResult();
	}
	
	private void doMaterialBSDFResultSetIncoming(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0] = x;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1] = y;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2] = z;
	}
	
	private void doMaterialBSDFResultSetIncomingFromBXDFResult() {
		final boolean isNegatingIncoming = doMaterialBSDFIsNegatingIncoming();
		
		final float incomingX = doMaterialBXDFResultGetIncomingX();
		final float incomingY = doMaterialBXDFResultGetIncomingY();
		final float incomingZ = doMaterialBXDFResultGetIncomingZ();
		
		vector3FSetOrthonormalBasis33FTransformNormalize(incomingX, incomingY, incomingZ);
		
		final float incomingTransformedX = isNegatingIncoming ? -vector3FGetComponent1() : vector3FGetComponent1();
		final float incomingTransformedY = isNegatingIncoming ? -vector3FGetComponent2() : vector3FGetComponent2();
		final float incomingTransformedZ = isNegatingIncoming ? -vector3FGetComponent3() : vector3FGetComponent3();
		
		doMaterialBSDFResultSetIncoming(incomingTransformedX, incomingTransformedY, incomingTransformedZ);
	}
	
	private void doMaterialBSDFResultSetNormal(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0] = x;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1] = y;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2] = z;
	}
	
	private void doMaterialBSDFResultSetNormalFromIntersection() {
		final float x = intersectionGetOrthonormalBasisSWComponent1();
		final float y = intersectionGetOrthonormalBasisSWComponent2();
		final float z = intersectionGetOrthonormalBasisSWComponent3();
		
		doMaterialBSDFResultSetNormal(x, y, z);
	}
	
	private void doMaterialBSDFResultSetOutgoing(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0] = x;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1] = y;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2] = z;
	}
	
	private void doMaterialBSDFResultSetOutgoingFromRay() {
		final float x = -ray3FGetDirectionComponent1();
		final float y = -ray3FGetDirectionComponent2();
		final float z = -ray3FGetDirectionComponent3();
		
		doMaterialBSDFResultSetOutgoing(x, y, z);
	}
	
	private void doMaterialBSDFResultSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doMaterialBSDFResultSetResult(final float r, final float g, final float b) {
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 0] = r;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 1] = g;
		this.bSDFResultArray_$private$10[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 2] = b;
	}
	
	private void doMaterialBSDFSetBXDF(final int index, final int id) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + index] = id;
	}
	
	private void doMaterialBSDFSetBXDFCount(final int count) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT] = count;
	}
	
	private void doMaterialBSDFSetEta(final float eta) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA] = eta;
	}
	
	private void doMaterialBSDFSetNegatingIncoming(final boolean isNegatingIncoming) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING] = isNegatingIncoming ? 1.0F : 0.0F;
	}
	
	private void doMaterialBXDFLambertianBRDFSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	private void doMaterialBXDFOrenNayarBRDFSet(final float angleDegrees, final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		final float angleRadians = toRadians(angleDegrees);
		final float angleRadiansSquared = angleRadians * angleRadians;
		
		final float a = 1.0F - (angleRadiansSquared / (2.0F * (angleRadiansSquared + 0.33F)));
		final float b = 0.45F * angleRadiansSquared / (angleRadiansSquared + 0.09F);
		
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_ANGLE_RADIANS] = angleRadians;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A] = a;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B] = b;
	}
	
	private void doMaterialBXDFResultSetIncoming(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0] = x;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1] = y;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2] = z;
	}
	
	private void doMaterialBXDFResultSetIncomingTransformedFromBSDFResult() {
		final boolean isNegatingIncoming = doMaterialBSDFIsNegatingIncoming();
		
		final float incomingX = isNegatingIncoming ? -doMaterialBSDFResultGetIncomingX() : doMaterialBSDFResultGetIncomingX();
		final float incomingY = isNegatingIncoming ? -doMaterialBSDFResultGetIncomingY() : doMaterialBSDFResultGetIncomingY();
		final float incomingZ = isNegatingIncoming ? -doMaterialBSDFResultGetIncomingZ() : doMaterialBSDFResultGetIncomingZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(incomingX, incomingY, incomingZ);
		
		final float incomingTransformedX = vector3FGetComponent1();
		final float incomingTransformedY = vector3FGetComponent2();
		final float incomingTransformedZ = vector3FGetComponent3();
		
		doMaterialBXDFResultSetIncoming(incomingTransformedX, incomingTransformedY, incomingTransformedZ);
	}
	
	private void doMaterialBXDFResultSetNormal(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0] = x;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1] = y;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2] = z;
	}
	
	private void doMaterialBXDFResultSetNormalTransformedFromBSDFResult() {
		final float normalX = doMaterialBSDFResultGetNormalX();
		final float normalY = doMaterialBSDFResultGetNormalY();
		final float normalZ = doMaterialBSDFResultGetNormalZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(normalX, normalY, normalZ);
		
		final float normalTransformedX = vector3FGetComponent1();
		final float normalTransformedY = vector3FGetComponent2();
		final float normalTransformedZ = vector3FGetComponent3();
		
		doMaterialBXDFResultSetNormal(normalTransformedX, normalTransformedY, normalTransformedZ);
	}
	
	private void doMaterialBXDFResultSetOutgoing(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0] = x;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1] = y;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2] = z;
	}
	
	private void doMaterialBXDFResultSetOutgoingTransformedFromBSDFResult() {
		final float outgoingX = doMaterialBSDFResultGetOutgoingX();
		final float outgoingY = doMaterialBSDFResultGetOutgoingY();
		final float outgoingZ = doMaterialBSDFResultGetOutgoingZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(outgoingX, outgoingY, outgoingZ);
		
		final float outgoingTransformedX = vector3FGetComponent1();
		final float outgoingTransformedY = vector3FGetComponent2();
		final float outgoingTransformedZ = vector3FGetComponent3();
		
		doMaterialBXDFResultSetOutgoing(outgoingTransformedX, outgoingTransformedY, outgoingTransformedZ);
	}
	
	private void doMaterialBXDFResultSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doMaterialBXDFResultSetResult(final float r, final float g, final float b) {
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 0] = r;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 1] = g;
		this.bXDFResultArray_$private$10[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 2] = b;
	}
}