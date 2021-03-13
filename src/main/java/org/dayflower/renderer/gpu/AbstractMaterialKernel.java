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
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

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
	protected static final int B_X_D_F_TYPE_BIT_FLAG_ALL = (1 << 0) | (1 << 1) | (1 << 2) | (1 << 3) | (1 << 4);
	
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
	
//	Constants for BSDF:
	private static final int B_S_D_F_ARRAY_LENGTH = 11;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F = 3;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT = 2;
	private static final int B_S_D_F_ARRAY_OFFSET_ETA = 0;
	private static final int B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING = 1;
	
//	Constants for BSDFResult:
	private static final int B_S_D_F_RESULT_ARRAY_LENGTH = 13;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING = 3;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL = 6;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING = 9;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 12;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_RESULT = 0;
	
//	Constants for BXDF AshikhminShirleyBRDF:
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_LENGTH = 4;
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_EXPONENT = 3;
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ID = 1;
	
//	Constants for BXDF LambertianBRDF:
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ID = 2;
	
//	Constants for BXDF LambertianBTDF:
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ID = 3;
	
//	Constants for BXDF OrenNayarBRDF:
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH = 6;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A = 4;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_ANGLE_RADIANS = 0;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B = 5;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 1;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ID = 4;
	
//	Constants for BXDFResult:
	private static final int B_X_D_F_RESULT_ARRAY_LENGTH = 13;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING = 3;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL = 6;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING = 9;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 12;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_RESULT = 0;
	
//	Constants for BXDF SpecularBRDF using Fresnel of type Constant:
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_LENGTH = 6;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT = 3;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ID = 5;
	
//	Constants for BXDF SpecularBRDF using Fresnel of type Dielectric:
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 5;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I = 3;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T = 4;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ID = 6;
	
//	Constants for BXDF TorranceSparrowBRDF using Fresnel of type Dielectric and an implicit MicrofacetDistribution of type TrowbridgeReitz:
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 9;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X = 7;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y = 8;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I = 3;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T = 4;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 5;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 6;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ID = 7;
	
//	Constants for the current implementation:
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING = 0;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL = 3;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING = 6;
	private static final int MATERIAL_B_X_D_F_ARRAY_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] bSDFArray_$private$11;
	
//	TODO: Add Javadocs!
	protected float[] bSDFResultArray_$private$13;
	
//	TODO: Add Javadocs!
	protected float[] bXDFAshikhminShirleyBRDFArray_$private$4;
	
//	TODO: Add Javadocs!
	protected float[] bXDFLambertianBRDFArray_$private$3;
	
//	TODO: Add Javadocs!
	protected float[] bXDFLambertianBTDFArray_$private$3;
	
//	TODO: Add Javadocs!
	protected float[] bXDFOrenNayarBRDFArray_$private$6;
	
//	TODO: Add Javadocs!
	protected float[] bXDFResultArray_$private$13;
	
//	TODO: Add Javadocs!
	protected float[] bXDFSpecularBRDFFresnelConstantArray_$private$6;
	
//	TODO: Add Javadocs!
	protected float[] bXDFSpecularBRDFFresnelDielectricArray_$private$5;
	
//	TODO: Add Javadocs!
	protected float[] bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9;
	
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
		this.bSDFResultArray_$private$13 = new float[B_S_D_F_RESULT_ARRAY_LENGTH];
		this.bXDFAshikhminShirleyBRDFArray_$private$4 = new float[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_LENGTH];
		this.bXDFLambertianBRDFArray_$private$3 = new float[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH];
		this.bXDFLambertianBTDFArray_$private$3 = new float[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_LENGTH];
		this.bXDFOrenNayarBRDFArray_$private$6 = new float[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH];
		this.bXDFResultArray_$private$13 = new float[B_X_D_F_RESULT_ARRAY_LENGTH];
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6 = new float[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_LENGTH];
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5 = new float[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9 = new float[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
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
		
		vector3FSetFaceForwardLHSNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
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
		
		vector3FSetFaceForwardLHSNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
		
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
		
		vector3FSetFaceForwardLHSNegated(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
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
	protected final boolean testBSDFCompute(final int materialID, final int materialOffset) {
		if(materialID == ClearCoatMaterial.ID) {
			return doMaterialClearCoatMaterialComputeBSDF(materialOffset);
		} else if(materialID == GlossyMaterial.ID) {
			return doMaterialGlossyMaterialComputeBSDF(materialOffset);
		} else if(materialID == MatteMaterial.ID) {
			return doMaterialMatteMaterialComputeBSDF(materialOffset);
		} else if(materialID == MirrorMaterial.ID) {
			return doMaterialMirrorMaterialComputeBSDF(materialOffset);
		} else {
			return false;
		}
	}
	
//	TODO: Add Javadocs!
	protected final boolean testBSDFSampleDistributionFunction(final int bitFlags) {
		final int countBXDFs = doBSDFGetBXDFCount();
		
		int matches = 0;
		
		for(int i = 0; i < countBXDFs; i++) {
			final int id = doBSDFGetBXDF(i);
			
			if(doBXDFIsMatchingBitFlags(id, bitFlags)) {
				matches++;
			}
		}
		
		if(matches > 0 && !zero(doBXDFResultGetOutgoingZ())) {
			final float u = random();
			final float v = random();
			
			final int match = min((int)(floor(u * matches)), matches - 1);
			
			int matchingId = -1;
			
			for(int i = 0, j = match; i < countBXDFs; i++) {
				final int id = doBSDFGetBXDF(i);
				
				if(doBXDFIsMatchingBitFlags(id, bitFlags)) {
					if(j == 0) {
						matchingId = id;
					}
					
					j--;
				}
			}
			
			if(matchingId != -1) {
				final float uRemapped = min(u * matches - match, 0.99999994F);
				final float vRemapped = v;
				
				if(doBXDFSampleDistributionFunction(uRemapped, vRemapped, matchingId)) {
					doBSDFResultSetIncomingFromBXDFResult();
					
					final float incomingX = testBSDFResultGetIncomingX();
					final float incomingY = testBSDFResultGetIncomingY();
					final float incomingZ = testBSDFResultGetIncomingZ();
					
					final float outgoingX = doBSDFResultGetOutgoingX();
					final float outgoingY = doBSDFResultGetOutgoingY();
					final float outgoingZ = doBSDFResultGetOutgoingZ();
					
					final float normalX = intersectionGetOrthonormalBasisGWComponent1();
					final float normalY = intersectionGetOrthonormalBasisGWComponent2();
					final float normalZ = intersectionGetOrthonormalBasisGWComponent3();
					
					float probabilityDensityFunctionValue = doBXDFResultGetProbabilityDensityFunctionValue();
					
					float resultR = doBXDFResultGetResultR();
					float resultG = doBXDFResultGetResultG();
					float resultB = doBXDFResultGetResultB();
					
					if(matches > 1 && !doBXDFIsSpecular(matchingId)) {
						for(int i = 0; i < countBXDFs; i++) {
							final int id = doBSDFGetBXDF(i);
							
							if(id != matchingId && doBXDFIsMatchingBitFlags(id, bitFlags)) {
								doBXDFEvaluateProbabilityDensityFunction(id);
								
								probabilityDensityFunctionValue += doBXDFResultGetProbabilityDensityFunctionValue();
							}
						}
					}
					
					if(matches > 1) {
						probabilityDensityFunctionValue /= matches;
					}
					
					if(!doBXDFIsSpecular(matchingId)) {
						final float iDotN = vector3FDotProduct(incomingX, incomingY, incomingZ, normalX, normalY, normalZ);
						final float oDotN = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
						
						final boolean isReflecting = iDotN * oDotN > 0.0F;
						
						resultR = 0.0F;
						resultG = 0.0F;
						resultB = 0.0F;
						
						for(int i = 0; i < countBXDFs; i++) {
							final int id = doBSDFGetBXDF(i);
							
							if(doBXDFIsMatchingBitFlags(id, bitFlags) && (isReflecting && doBXDFHasReflection(id) || !isReflecting && doBXDFHasTransmission(id))) {
								doBXDFEvaluateDistributionFunction(id);
								
								resultR += doBXDFResultGetResultR();
								resultG += doBXDFResultGetResultG();
								resultB += doBXDFResultGetResultB();
							}
						}
					}
					
					doBSDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
					doBSDFResultSetResult(resultR, resultG, resultB);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFGetEta() {
		return this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetIncomingX() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetIncomingY() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetIncomingZ() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetProbabilityDensityFunctionValue() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetResultB() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 2];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetResultG() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 1];
	}
	
//	TODO: Add Javadocs!
	protected final float testBSDFResultGetResultR() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 0];
	}
	
//	TODO: Add Javadocs!
	protected final int testBSDFCountBXDFsBySpecularType(final boolean isSpecular) {
		int countBySpecular = 0;
		
		final int countBXDFs = doBSDFGetBXDFCount();
		
		for(int i = 0; i < countBXDFs; i++) {
			if(isSpecular == doBXDFIsSpecular(doBSDFGetBXDF(i))) {
				countBySpecular++;
			}
		}
		
		return countBySpecular;
	}
	
//	TODO: Add Javadocs!
	protected final void testBSDFEvaluateDistributionFunction(final int bitFlags) {
		final float incomingX = testBSDFResultGetIncomingX();
		final float incomingY = testBSDFResultGetIncomingY();
		final float incomingZ = testBSDFResultGetIncomingZ();
		
		final float outgoingX = doBSDFResultGetOutgoingX();
		final float outgoingY = doBSDFResultGetOutgoingY();
		final float outgoingZ = doBSDFResultGetOutgoingZ();
		
		final float normalX = intersectionGetOrthonormalBasisGWComponent1();
		final float normalY = intersectionGetOrthonormalBasisGWComponent2();
		final float normalZ = intersectionGetOrthonormalBasisGWComponent3();
		
		final float iDotN = vector3FDotProduct(incomingX, incomingY, incomingZ, normalX, normalY, normalZ);
		final float oDotN = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		
		final boolean isReflecting = iDotN * oDotN > 0.0F;
		
		float resultR = 0.0F;
		float resultG = 0.0F;
		float resultB = 0.0F;
		
		final int countBXDFs = doBSDFGetBXDFCount();
		
		for(int i = 0; i < countBXDFs; i++) {
			final int id = doBSDFGetBXDF(i);
			
			if(doBXDFIsMatchingBitFlags(id, bitFlags) && (isReflecting && doBXDFHasReflection(id) || !isReflecting && doBXDFHasTransmission(id))) {
				doBXDFEvaluateDistributionFunction(id);
				
				resultR += doBXDFResultGetResultR();
				resultG += doBXDFResultGetResultG();
				resultB += doBXDFResultGetResultB();
			}
		}
		
		doBSDFResultSetResult(resultR, resultG, resultB);
	}
	
//	TODO: Add Javadocs!
	protected final void testBSDFEvaluateProbabilityDensityFunction(final int bitFlags) {
		final int countBXDFs = doBSDFGetBXDFCount();
		
		float probabilityDensityFunctionValue = 0.0F;
		
		if(countBXDFs > 0 && !zero(doBXDFResultGetOutgoingZ())) {
			int matches = 0;
			
			for(int i = 0; i < countBXDFs; i++) {
				final int id = doBSDFGetBXDF(i);
				
				if(doBXDFIsMatchingBitFlags(id, bitFlags)) {
					matches++;
					
					doBXDFEvaluateProbabilityDensityFunction(id);
					
					probabilityDensityFunctionValue += doBXDFResultGetProbabilityDensityFunctionValue();
				}
			}
			
			if(matches > 1) {
				probabilityDensityFunctionValue /= matches;
			}
		}
		
		doBSDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Materials ///////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doMaterialClearCoatMaterialComputeBSDF(final int materialClearCoatMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KD Texture:
		final int textureKD = this.materialClearCoatMaterialArray[materialClearCoatMaterialArrayOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
//		Retrieve the ID and offset for the KS Texture:
		final int textureKS = this.materialClearCoatMaterialArray[materialClearCoatMaterialArrayOffset + ClearCoatMaterial.ARRAY_OFFSET_TEXTURE_K_S];
		final int textureKSID = (textureKS >>> 16) & 0xFFFF;
		final int textureKSOffset = textureKS & 0xFFFF;
		
//		Evaluate the KD Texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD Texture:
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
//		Evaluate the KS Texture:
		textureEvaluate(textureKSID, textureKSOffset);
		
//		Retrieve the color from the KS Texture:
		final float colorKSR = color3FLHSGetComponent1();
		final float colorKSG = color3FLHSGetComponent2();
		final float colorKSB = color3FLHSGetComponent3();
		
		/*
		 * Compute the BSDF:
		 */
		
//		Clear the BSDF:
		doBSDFClear();
		
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		final float surfaceNormalDotRayDirection = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		
		final float surfaceNormalCorrectlyOrientedX = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalX : surfaceNormalX;
		final float surfaceNormalCorrectlyOrientedY = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalY : surfaceNormalY;
		final float surfaceNormalCorrectlyOrientedZ = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalZ : surfaceNormalZ;
		
		final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
		
		final float etaA = 1.0F;
		final float etaB = 1.5F;
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = etaI / etaT;
		
		if(vector3FSetRefraction(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta)) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			final float reflectance = fresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
//				Set the SpecularBRDF:
				doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
				doBSDFSetBXDFCount(1);
				doBSDFSetEta(1.0F);
				doBSDFSetNegatingIncoming(false);
				doBSDFResultInitialize();
				doBXDFSpecularBRDFFresnelConstantSet(colorKSR * probabilityRussianRouletteReflection, colorKSG * probabilityRussianRouletteReflection, colorKSB * probabilityRussianRouletteReflection, 1.0F, 1.0F, 1.0F);
				
				return true;
			}
			
//			Set LambertianBRDF:
			doBSDFSetBXDFLambertianBRDF(0);
			doBSDFSetBXDFCount(1);
			doBSDFSetEta(1.0F);
			doBSDFSetNegatingIncoming(false);
			doBSDFResultInitialize();
			doBXDFLambertianBRDFSet(colorKDR * probabilityRussianRouletteTransmission, colorKDG * probabilityRussianRouletteTransmission, colorKDB * probabilityRussianRouletteTransmission);
			
			return true;
		}
		
//		Set the SpecularBRDF:
		doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		doBSDFResultInitialize();
		doBXDFSpecularBRDFFresnelConstantSet(colorKSR, colorKSG, colorKSB, 1.0F, 1.0F, 1.0F);
		
		return true;
	}
	
	private boolean doMaterialGlossyMaterialComputeBSDF(final int materialGlossyMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KR Texture:
		final int textureKR = this.materialGlossyMaterialArray[materialGlossyMaterialArrayOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness Texture:
		final int textureRoughness = this.materialGlossyMaterialArray[materialGlossyMaterialArrayOffset + GlossyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS];
		final int textureRoughnessID = (textureRoughness >>> 16) & 0xFFFF;
		final int textureRoughnessOffset = textureRoughness & 0xFFFF;
		
//		Evaluate the KR Texture:
		textureEvaluate(textureKRID, textureKROffset);
		
//		Retrieve the color from the KR Texture:
		final float colorKRR = color3FLHSGetComponent1();
		final float colorKRG = color3FLHSGetComponent2();
		final float colorKRB = color3FLHSGetComponent3();
		
//		Evaluate the Roughness Texture:
		textureEvaluate(textureRoughnessID, textureRoughnessOffset);
		
//		Retrieve the average color from the Roughness Texture:
		final float floatRoughness = color3FLHSGetAverage();
		
		/*
		 * Compute the BSDF:
		 */
		
//		Clear the BSDF:
		doBSDFClear();
		
//		Set AshikhminShirleyBRDF:
		doBSDFSetBXDFAshikhminShirleyBRDF(0);
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(true);
		doBSDFResultInitialize();
		doBXDFAshikhminShirleyBRDFSet(colorKRR, colorKRG, colorKRB, floatRoughness);
		
		return true;
	}
	
	private boolean doMaterialMatteMaterialComputeBSDF(final int materialMatteMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KD Texture:
		final int textureKD = this.materialMatteMaterialArray[materialMatteMaterialArrayOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
//		Retrieve the ID and offset for the Angle Texture:
		final int textureAngle = this.materialMatteMaterialArray[materialMatteMaterialArrayOffset + MatteMaterial.ARRAY_OFFSET_TEXTURE_ANGLE];
		final int textureAngleID = (textureAngle >>> 16) & 0xFFFF;
		final int textureAngleOffset = textureAngle & 0xFFFF;
		
//		Evaluate the KD Texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD Texture:
		final float colorKDR = color3FLHSGetComponent1();
		final float colorKDG = color3FLHSGetComponent2();
		final float colorKDB = color3FLHSGetComponent3();
		
//		Evaluate the Angle Texture:
		textureEvaluate(textureAngleID, textureAngleOffset);
		
//		Retrieve the average color from the Angle Texture:
		final float floatAngle = color3FLHSGetAverage();
		
		if(zero(colorKDR) && zero(colorKDG) && zero(colorKDB)) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
//		Clear the BSDF:
		doBSDFClear();
		
		if(zero(floatAngle)) {
//			Set LambertianBRDF:
			doBSDFSetBXDFLambertianBRDF(0);
			doBSDFSetBXDFCount(1);
			doBSDFSetEta(1.0F);
			doBSDFSetNegatingIncoming(false);
			doBSDFResultInitialize();
			doBXDFLambertianBRDFSet(colorKDR, colorKDG, colorKDB);
			
			return true;
		}
		
//		Set OrenNayarBRDF:
		doBSDFSetBXDFOrenNayarBRDF(0);
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		doBSDFResultInitialize();
		doBXDFOrenNayarBRDFSet(floatAngle, colorKDR, colorKDG, colorKDB);
		
		return true;
	}
	
	private boolean doMaterialMirrorMaterialComputeBSDF(final int materialMirrorMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KR Texture:
		final int textureKR = this.materialMirrorMaterialArray[materialMirrorMaterialArrayOffset + MirrorMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		
//		Evaluate the KR Texture:
		textureEvaluate(textureKRID, textureKROffset);
		
//		Retrieve the color from the KR Texture:
		final float colorKRR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKRG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKRB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		if(zero(colorKRR) && zero(colorKRG) && zero(colorKRB)) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
//		Clear the BSDF:
		doBSDFClear();
		
//		Set the SpecularBRDF:
		doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		doBSDFResultInitialize();
		doBXDFSpecularBRDFFresnelConstantSet(colorKRR, colorKRG, colorKRB, 1.0F, 1.0F, 1.0F);
		
		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BSDF ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doBSDFIsNegatingIncoming() {
		return !zero(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING]);
	}
	
	private int doBSDFGetBXDF(final int index) {
		return (int)(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + index]);
	}
	
	private int doBSDFGetBXDFCount() {
		return (int)(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT]);
	}
	
	private void doBSDFClear() {
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
	
	private void doBSDFSetBXDF(final int index, final int id) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F + index] = id;
	}
	
	private void doBSDFSetBXDFAshikhminShirleyBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFCount(final int count) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT] = count;
	}
	
	private void doBSDFSetBXDFLambertianBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_LAMBERTIAN_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFOrenNayarBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_OREN_NAYAR_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFSpecularBRDFFresnelConstant(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ID);
	}
	
	private void doBSDFSetBXDFSpecularBRDFFresnelDielectric(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ID);
	}
	
	private void doBSDFSetEta(final float eta) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA] = eta;
	}
	
	private void doBSDFSetNegatingIncoming(final boolean isNegatingIncoming) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING] = isNegatingIncoming ? 1.0F : 0.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BSDF Result /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doBSDFResultGetNormalX() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0];
	}
	
	private float doBSDFResultGetNormalY() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1];
	}
	
	private float doBSDFResultGetNormalZ() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2];
	}
	
	private float doBSDFResultGetOutgoingX() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0];
	}
	
	private float doBSDFResultGetOutgoingY() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1];
	}
	
	private float doBSDFResultGetOutgoingZ() {
		return this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2];
	}
	
	private void doBSDFResultInitialize() {
		doBSDFResultSetNormalFromIntersection();
		doBSDFResultSetOutgoingFromRay();
		
		orthonormalBasis33FSetIntersectionOrthonormalBasisS();
		
		doBXDFResultSetNormalTransformedFromBSDFResult();
		doBXDFResultSetOutgoingTransformedFromBSDFResult();
	}
	
	private void doBSDFResultSetIncoming(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0] = x;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1] = y;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2] = z;
	}
	
	private void doBSDFResultSetIncomingFromBXDFResult() {
		final boolean isNegatingIncoming = doBSDFIsNegatingIncoming();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		vector3FSetOrthonormalBasis33FTransformNormalize(incomingX, incomingY, incomingZ);
		
		final float incomingTransformedX = isNegatingIncoming ? -vector3FGetComponent1() : vector3FGetComponent1();
		final float incomingTransformedY = isNegatingIncoming ? -vector3FGetComponent2() : vector3FGetComponent2();
		final float incomingTransformedZ = isNegatingIncoming ? -vector3FGetComponent3() : vector3FGetComponent3();
		
		doBSDFResultSetIncoming(incomingTransformedX, incomingTransformedY, incomingTransformedZ);
	}
	
	private void doBSDFResultSetNormal(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0] = x;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1] = y;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2] = z;
	}
	
	private void doBSDFResultSetNormalFromIntersection() {
		final float x = intersectionGetOrthonormalBasisSWComponent1();
		final float y = intersectionGetOrthonormalBasisSWComponent2();
		final float z = intersectionGetOrthonormalBasisSWComponent3();
		
		doBSDFResultSetNormal(x, y, z);
	}
	
	private void doBSDFResultSetOutgoing(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0] = x;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1] = y;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2] = z;
	}
	
	private void doBSDFResultSetOutgoingFromRay() {
		final float x = -ray3FGetDirectionComponent1();
		final float y = -ray3FGetDirectionComponent2();
		final float z = -ray3FGetDirectionComponent3();
		
		doBSDFResultSetOutgoing(x, y, z);
	}
	
	private void doBSDFResultSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doBSDFResultSetResult(final float r, final float g, final float b) {
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 0] = r;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 1] = g;
		this.bSDFResultArray_$private$13[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 2] = b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doBXDFHasReflection(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return true;
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			return true;
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			return true;
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			return true;
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean doBXDFHasTransmission(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			return true;
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			return false;
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			return false;
		} else {
			return false;
		}
	}
	
	private boolean doBXDFIsMatchingBitFlags(final int id, final int bitFlags) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return doBXDFAshikhminShirleyBRDFIsMatchingBitFlags(bitFlags);
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			return doBXDFLambertianBRDFIsMatchingBitFlags(bitFlags);
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			return doBXDFLambertianBTDFIsMatchingBitFlags(bitFlags);
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			return doBXDFOrenNayarBRDFIsMatchingBitFlags(bitFlags);
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			return doBXDFSpecularBRDFFresnelConstantIsMatchingBitFlags(bitFlags);
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			return doBXDFSpecularBRDFFresnelDielectricIsMatchingBitFlags(bitFlags);
		} else {
			return false;
		}
	}
	
	private boolean doBXDFIsSpecular(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			return false;
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			return true;
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean doBXDFSampleDistributionFunction(final float u, final float v, final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return doBXDFAshikhminShirleyBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			return doBXDFLambertianBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			return doBXDFLambertianBTDFSampleDistributionFunction(u, v);
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			return doBXDFOrenNayarBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			return doBXDFSpecularBRDFFresnelConstantSampleDistributionFunction(u, v);
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			return doBXDFSpecularBRDFFresnelDielectricSampleDistributionFunction(u, v);
		} else {
			return false;
		}
	}
	
	private void doBXDFEvaluateDistributionFunction(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			doBXDFAshikhminShirleyBRDFEvaluateDistributionFunction();
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			doBXDFLambertianBRDFEvaluateDistributionFunction();
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			doBXDFLambertianBTDFEvaluateDistributionFunction();
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			doBXDFOrenNayarBRDFEvaluateDistributionFunction();
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			doBXDFSpecularBRDFFresnelConstantEvaluateDistributionFunction();
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			doBXDFSpecularBRDFFresnelDielectricEvaluateDistributionFunction();
		}
	}
	
	private void doBXDFEvaluateProbabilityDensityFunction(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			doBXDFAshikhminShirleyBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFLambertianBRDFIsMatchingID(id)) {
			doBXDFLambertianBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFLambertianBTDFIsMatchingID(id)) {
			doBXDFLambertianBTDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFOrenNayarBRDFIsMatchingID(id)) {
			doBXDFOrenNayarBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFSpecularBRDFFresnelConstantIsMatchingID(id)) {
			doBXDFSpecularBRDFFresnelConstantEvaluateProbabilityDensityFunction();
		} else if(doBXDFSpecularBRDFFresnelDielectricIsMatchingID(id)) {
			doBXDFSpecularBRDFFresnelDielectricEvaluateProbabilityDensityFunction();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Ashikhmin-Shirley BRDF /////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFAshikhminShirleyBRDFIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_BIT_FLAGS & bitFlags) == B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFAshikhminShirleyBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ID;
	}
	
	private boolean doBXDFAshikhminShirleyBRDFSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float normalX = doBXDFResultGetNormalX();
		final float normalY = doBXDFResultGetNormalY();
		final float normalZ = doBXDFResultGetNormalZ();
		
		if(zero(outgoingZ)) {
			return false;
		}
		
		final float exponent = doBXDFAshikhminShirleyBRDFGetExponent();
		
		vector3FSetSampleHemispherePowerCosineDistribution(u, v, exponent);
		vector3FSetFaceForwardDirection(normalX, normalY, normalZ, outgoingX, outgoingY, outgoingZ, vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		final float normalTransformedX = vector3FGetComponent1();
		final float normalTransformedY = vector3FGetComponent2();
		final float normalTransformedZ = vector3FGetComponent3();
		
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalTransformedX, normalTransformedY, normalTransformedZ) < 0.0F) {
			return false;
		}
		
		vector3FSetSpecularReflection(outgoingX, outgoingY, outgoingZ, normalTransformedX, normalTransformedY, normalTransformedZ, true);
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFAshikhminShirleyBRDFEvaluateDistributionFunction();
		doBXDFAshikhminShirleyBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFAshikhminShirleyBRDFGetExponent() {
		return this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_EXPONENT];
	}
	
	private float doBXDFAshikhminShirleyBRDFGetReflectanceScaleB() {
		return this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFAshikhminShirleyBRDFGetReflectanceScaleG() {
		return this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFAshikhminShirleyBRDFGetReflectanceScaleR() {
		return this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFAshikhminShirleyBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosThetaOutgoing = vector3FCosTheta(outgoingX, outgoingY, outgoingZ);
		final float cosThetaOutgoingAbs = abs(cosThetaOutgoing);
		final float cosThetaIncoming = vector3FCosTheta(incomingX, incomingY, incomingZ);
		final float cosThetaIncomingAbs = abs(cosThetaIncoming);
		
		if(zero(cosThetaOutgoingAbs) || zero(cosThetaIncomingAbs)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalX = outgoingX - incomingX;
		final float normalY = outgoingY - incomingY;
		final float normalZ = outgoingZ - incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalNormalizedCosThetaAbs = vector3FCosThetaAbs(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		if(zero(normalNormalizedX) && zero(normalNormalizedY) && zero(normalNormalizedZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float exponent = doBXDFAshikhminShirleyBRDFGetExponent();
		
		final float outgoingDotNormalNormalized = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float d = (exponent + 1.0F) * pow(normalNormalizedCosThetaAbs, exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = fresnelDielectricSchlick(outgoingDotNormalNormalized, 1.0F);
		final float g = 4.0F * abs(cosThetaOutgoing + -cosThetaIncoming - cosThetaOutgoing * -cosThetaIncoming);
		
		final float reflectanceScaleR = doBXDFAshikhminShirleyBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFAshikhminShirleyBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFAshikhminShirleyBRDFGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * d * f / g;
		final float resultG = reflectanceScaleG * d * f / g;
		final float resultB = reflectanceScaleB * d * f / g;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFAshikhminShirleyBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = outgoingX - incomingX;
		final float normalY = outgoingY - incomingY;
		final float normalZ = outgoingZ - incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalNormalizedCosThetaAbs = vector3FCosThetaAbs(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float exponent = doBXDFAshikhminShirleyBRDFGetExponent();
		
		final float outgoingDotNormalNormalizedAbs = abs(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ));
		
		final float probabilityDensityFunctionValue = (exponent + 1.0F) * pow(normalNormalizedCosThetaAbs, exponent) / (PI * 8.0F * outgoingDotNormalNormalizedAbs);
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFAshikhminShirleyBRDFSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB, final float roughness) {
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_EXPONENT] = 1.0F / (roughness * roughness);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Lambertian BRDF ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFLambertianBRDFIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS & bitFlags) == B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFLambertianBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_LAMBERTIAN_B_R_D_F_ID;
	}
	
	private boolean doBXDFLambertianBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFLambertianBRDFEvaluateDistributionFunction();
		doBXDFLambertianBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFLambertianBRDFGetReflectanceScaleB() {
		return this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFLambertianBRDFGetReflectanceScaleG() {
		return this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFLambertianBRDFGetReflectanceScaleR() {
		return this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFLambertianBRDFEvaluateDistributionFunction() {
		final float reflectanceScaleR = doBXDFLambertianBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFLambertianBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFLambertianBRDFGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * PI_RECIPROCAL;
		final float resultG = reflectanceScaleG * PI_RECIPROCAL;
		final float resultB = reflectanceScaleB * PI_RECIPROCAL;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFLambertianBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFLambertianBRDFSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Lambertian BTDF ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFLambertianBTDFIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_LAMBERTIAN_B_T_D_F_BIT_FLAGS & bitFlags) == B_X_D_F_LAMBERTIAN_B_T_D_F_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFLambertianBTDFIsMatchingID(final int id) {
		return id == B_X_D_F_LAMBERTIAN_B_T_D_F_ID;
	}
	
	private boolean doBXDFLambertianBTDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3Negated(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFLambertianBTDFEvaluateDistributionFunction();
		doBXDFLambertianBTDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFLambertianBTDFGetTransmittanceScaleB() {
		return this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2];
	}
	
	private float doBXDFLambertianBTDFGetTransmittanceScaleG() {
		return this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1];
	}
	
	private float doBXDFLambertianBTDFGetTransmittanceScaleR() {
		return this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0];
	}
	
	private void doBXDFLambertianBTDFEvaluateDistributionFunction() {
		final float transmittanceScaleR = doBXDFLambertianBTDFGetTransmittanceScaleR();
		final float transmittanceScaleG = doBXDFLambertianBTDFGetTransmittanceScaleG();
		final float transmittanceScaleB = doBXDFLambertianBTDFGetTransmittanceScaleB();
		
		final float resultR = transmittanceScaleR * PI_RECIPROCAL;
		final float resultG = transmittanceScaleG * PI_RECIPROCAL;
		final float resultB = transmittanceScaleB * PI_RECIPROCAL;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFLambertianBTDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? 0.0F : vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFLambertianBTDFSet(final float transmittanceScaleR, final float transmittanceScaleG, final float transmittanceScaleB) {
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0] = transmittanceScaleR;
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1] = transmittanceScaleG;
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2] = transmittanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Oren-Nayar BRDF ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFOrenNayarBRDFIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS & bitFlags) == B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFOrenNayarBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_OREN_NAYAR_B_R_D_F_ID;
	}
	
	private boolean doBXDFOrenNayarBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFOrenNayarBRDFEvaluateDistributionFunction();
		doBXDFOrenNayarBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFOrenNayarBRDFGetA() {
		return this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A];
	}
	
	private float doBXDFOrenNayarBRDFGetB() {
		return this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B];
	}
	
	private float doBXDFOrenNayarBRDFGetReflectanceScaleB() {
		return this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFOrenNayarBRDFGetReflectanceScaleG() {
		return this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFOrenNayarBRDFGetReflectanceScaleR() {
		return this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFOrenNayarBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosPhiIncoming = vector3FCosPhi(incomingX, incomingY, incomingZ);
		final float cosPhiOutgoing = vector3FCosPhi(outgoingX, outgoingY, outgoingZ);
		
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		
		final float sinPhiIncoming = vector3FSinPhi(incomingX, incomingY, incomingZ);
		final float sinPhiOutgoing = vector3FSinPhi(outgoingX, outgoingY, outgoingZ);
		
		final float sinThetaIncoming = vector3FSinTheta(incomingX, incomingY, incomingZ);
		final float sinThetaOutgoing = vector3FSinTheta(outgoingX, outgoingY, outgoingZ);
		
		final float maxCos = sinThetaIncoming > 1.0e-4F && sinThetaOutgoing > 1.0e-4F ? max(0.0F, cosPhiIncoming * cosPhiOutgoing + sinPhiIncoming * sinPhiOutgoing) : 0.0F;
		
		final float sinA = cosThetaAbsIncoming > cosThetaAbsOutgoing ? sinThetaOutgoing : sinThetaIncoming;
		final float tanB = cosThetaAbsIncoming > cosThetaAbsOutgoing ? sinThetaIncoming / cosThetaAbsIncoming : sinThetaOutgoing / cosThetaAbsOutgoing;
		
		final float a = doBXDFOrenNayarBRDFGetA();
		final float b = doBXDFOrenNayarBRDFGetB();
		final float c = (a + b * maxCos * sinA * tanB);
		
		final float reflectanceScaleR = doBXDFOrenNayarBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFOrenNayarBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFOrenNayarBRDFGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * PI_RECIPROCAL * c;
		final float resultG = reflectanceScaleG * PI_RECIPROCAL * c;
		final float resultB = reflectanceScaleB * PI_RECIPROCAL * c;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFOrenNayarBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFOrenNayarBRDFSet(final float angleDegrees, final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Result /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doBXDFResultGetIncomingX() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0];
	}
	
	private float doBXDFResultGetIncomingY() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1];
	}
	
	private float doBXDFResultGetIncomingZ() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2];
	}
	
	private float doBXDFResultGetNormalX() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0];
	}
	
	private float doBXDFResultGetNormalY() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1];
	}
	
	private float doBXDFResultGetNormalZ() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2];
	}
	
	private float doBXDFResultGetOutgoingX() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0];
	}
	
	private float doBXDFResultGetOutgoingY() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1];
	}
	
	private float doBXDFResultGetOutgoingZ() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2];
	}
	
	private float doBXDFResultGetProbabilityDensityFunctionValue() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE];
	}
	
	private float doBXDFResultGetResultB() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 2];
	}
	
	private float doBXDFResultGetResultG() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 1];
	}
	
	private float doBXDFResultGetResultR() {
		return this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 0];
	}
	
	private void doBXDFResultSetIncoming(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0] = x;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1] = y;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2] = z;
	}
	
	private void doBXDFResultSetNormal(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0] = x;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1] = y;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2] = z;
	}
	
	private void doBXDFResultSetNormalTransformedFromBSDFResult() {
		final float normalX = doBSDFResultGetNormalX();
		final float normalY = doBSDFResultGetNormalY();
		final float normalZ = doBSDFResultGetNormalZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(normalX, normalY, normalZ);
		
		final float normalTransformedX = vector3FGetComponent1();
		final float normalTransformedY = vector3FGetComponent2();
		final float normalTransformedZ = vector3FGetComponent3();
		
		doBXDFResultSetNormal(normalTransformedX, normalTransformedY, normalTransformedZ);
	}
	
	private void doBXDFResultSetOutgoing(final float x, final float y, final float z) {
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0] = x;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1] = y;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2] = z;
	}
	
	private void doBXDFResultSetOutgoingTransformedFromBSDFResult() {
		final float outgoingX = doBSDFResultGetOutgoingX();
		final float outgoingY = doBSDFResultGetOutgoingY();
		final float outgoingZ = doBSDFResultGetOutgoingZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(outgoingX, outgoingY, outgoingZ);
		
		final float outgoingTransformedX = vector3FGetComponent1();
		final float outgoingTransformedY = vector3FGetComponent2();
		final float outgoingTransformedZ = vector3FGetComponent3();
		
		doBXDFResultSetOutgoing(outgoingTransformedX, outgoingTransformedY, outgoingTransformedZ);
	}
	
	private void doBXDFResultSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doBXDFResultSetResult(final float r, final float g, final float b) {
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 0] = r;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 1] = g;
		this.bXDFResultArray_$private$13[B_X_D_F_RESULT_ARRAY_OFFSET_RESULT + 2] = b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Specular BRDF Fresnel Constant /////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFSpecularBRDFFresnelConstantIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_BIT_FLAGS & bitFlags) == B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFSpecularBRDFFresnelConstantIsMatchingID(final int id) {
		return id == B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ID;
	}
	
	@SuppressWarnings("unused")
	private boolean doBXDFSpecularBRDFFresnelConstantSampleDistributionFunction(final float u, final float v) {
		final float incomingX = -doBXDFResultGetOutgoingX();
		final float incomingY = -doBXDFResultGetOutgoingY();
		final float incomingZ = +doBXDFResultGetOutgoingZ();
		
		final float cosTheta = vector3FCosTheta(incomingX, incomingY, incomingZ);
		final float cosThetaAbs = abs(cosTheta);
		
		doBXDFSpecularBRDFFresnelConstantEvaluateFresnel(cosTheta);
		
		final float fresnelR = color3FLHSGetComponent1();
		final float fresnelG = color3FLHSGetComponent2();
		final float fresnelB = color3FLHSGetComponent3();
		
		final float reflectanceScaleR = doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleB();
		final float reflectanceScaleG = doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleR();
		
		final float resultR = fresnelR * reflectanceScaleR / cosThetaAbs;
		final float resultG = fresnelG * reflectanceScaleG / cosThetaAbs;
		final float resultB = fresnelB * reflectanceScaleB / cosThetaAbs;
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFResultSetProbabilityDensityFunctionValue(1.0F);
		doBXDFResultSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetLightB() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 2];
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetLightG() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 1];
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetLightR() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 0];
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleB() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleG() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFSpecularBRDFFresnelConstantGetReflectanceScaleR() {
		return this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFSpecularBRDFFresnelConstantEvaluateDistributionFunction() {
		doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
	}
	
	@SuppressWarnings("unused")
	private void doBXDFSpecularBRDFFresnelConstantEvaluateFresnel(final float cosThetaI) {
		final float lightR = doBXDFSpecularBRDFFresnelConstantGetLightR();
		final float lightG = doBXDFSpecularBRDFFresnelConstantGetLightG();
		final float lightB = doBXDFSpecularBRDFFresnelConstantGetLightB();
		
		color3FLHSSet(lightR, lightG, lightB);
	}
	
	private void doBXDFSpecularBRDFFresnelConstantEvaluateProbabilityDensityFunction() {
		doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
	}
	
	private void doBXDFSpecularBRDFFresnelConstantSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB, final float lightR, final float lightG, final float lightB) {
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 0] = lightR;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 1] = lightG;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 2] = lightB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Specular BRDF Fresnel Dielectric ///////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFSpecularBRDFFresnelDielectricIsMatchingBitFlags(final int bitFlags) {
		return (B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS & bitFlags) == B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFSpecularBRDFFresnelDielectricIsMatchingID(final int id) {
		return id == B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ID;
	}
	
	@SuppressWarnings("unused")
	private boolean doBXDFSpecularBRDFFresnelDielectricSampleDistributionFunction(final float u, final float v) {
		final float incomingX = -doBXDFResultGetOutgoingX();
		final float incomingY = -doBXDFResultGetOutgoingY();
		final float incomingZ = +doBXDFResultGetOutgoingZ();
		
		final float cosTheta = vector3FCosTheta(incomingX, incomingY, incomingZ);
		final float cosThetaAbs = abs(cosTheta);
		
		doBXDFSpecularBRDFFresnelDielectricEvaluateFresnel(cosTheta);
		
		final float fresnelR = color3FLHSGetComponent1();
		final float fresnelG = color3FLHSGetComponent2();
		final float fresnelB = color3FLHSGetComponent3();
		
		final float reflectanceScaleR = doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleB();
		final float reflectanceScaleG = doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleR();
		
		final float resultR = fresnelR * reflectanceScaleR / cosThetaAbs;
		final float resultG = fresnelG * reflectanceScaleG / cosThetaAbs;
		final float resultB = fresnelB * reflectanceScaleB / cosThetaAbs;
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFResultSetProbabilityDensityFunctionValue(1.0F);
		doBXDFResultSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	private float doBXDFSpecularBRDFFresnelDielectricGetEtaI() {
		return this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I];
	}
	
	private float doBXDFSpecularBRDFFresnelDielectricGetEtaT() {
		return this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T];
	}
	
	private float doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleB() {
		return this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleG() {
		return this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFSpecularBRDFFresnelDielectricGetReflectanceScaleR() {
		return this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricEvaluateDistributionFunction() {
		doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricEvaluateFresnel(final float cosThetaI) {
		final float etaI = doBXDFSpecularBRDFFresnelDielectricGetEtaI();
		final float etaT = doBXDFSpecularBRDFFresnelDielectricGetEtaT();
		
		final float reflectance = fresnelDielectric(cosThetaI, etaI, etaT);
		
		color3FLHSSet(reflectance, reflectance, reflectance);
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricEvaluateProbabilityDensityFunction() {
		doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB, final float etaI, final float etaT) {
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I] = etaI;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T] = etaT;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF Torrance-Sparrow BRDF Fresnel Dielectric ///////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricSet(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB, final float etaI, final float etaT, final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I] = etaI;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T] = etaT;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
}