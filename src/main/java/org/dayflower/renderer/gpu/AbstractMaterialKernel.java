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
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Material;
import org.dayflower.scene.bxdf.AshikhminShirleyBRDF;
import org.dayflower.scene.bxdf.DisneyClearCoatBRDF;
import org.dayflower.scene.bxdf.DisneyDiffuseBRDF;
import org.dayflower.scene.bxdf.DisneyFakeSSBRDF;
import org.dayflower.scene.bxdf.DisneyRetroBRDF;
import org.dayflower.scene.bxdf.DisneySheenBRDF;
import org.dayflower.scene.bxdf.FresnelBlendBRDF;
import org.dayflower.scene.bxdf.LambertianBRDF;
import org.dayflower.scene.bxdf.LambertianBTDF;
import org.dayflower.scene.bxdf.OrenNayarBRDF;
import org.dayflower.scene.bxdf.SpecularBRDF;
import org.dayflower.scene.bxdf.SpecularBTDF;
import org.dayflower.scene.bxdf.TorranceSparrowBRDF;
import org.dayflower.scene.bxdf.TorranceSparrowBTDF;
import org.dayflower.scene.fresnel.ConductorFresnel;
import org.dayflower.scene.fresnel.ConstantFresnel;
import org.dayflower.scene.fresnel.DielectricFresnel;
import org.dayflower.scene.fresnel.DisneyFresnel;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.microfacet.TrowbridgeReitzMicrofacetDistribution;
import org.dayflower.scene.texture.Texture;

/**
 * An {@code AbstractMaterialKernel} is an abstract extension of the {@link AbstractTextureKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link ClearCoatMaterial}</li>
 * <li>{@link GlassMaterial}</li>
 * <li>{@link GlossyMaterial}</li>
 * <li>{@link MatteMaterial}</li>
 * <li>{@link MetalMaterial}</li>
 * <li>{@link MirrorMaterial}</li>
 * <li>{@link PlasticMaterial}</li>
 * <li>{@link SubstrateMaterial}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractMaterialKernel extends AbstractTextureKernel {
	/**
	 * A bit flag that represents {@code BXDFType.ALL}.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_ALL = (1 << 0) | (1 << 1) | (1 << 2) | (1 << 3) | (1 << 4);
	
	/**
	 * A bit flag that represents {@code BXDFType.ALL_EXCEPT_SPECULAR}.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_ALL_EXCEPT_SPECULAR = (1 << 0) | (1 << 1) | (1 << 2) | (1 << 3);
	
	/**
	 * A bit flag that is used for BXDFs with reflection, which are called BRDFs.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION = 1 << 0;
	
	/**
	 * A bit flag that is used for BXDFs with transmission, which are called BTDFs.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION = 1 << 1;
	
	/**
	 * A bit flag that is used for BXDFs with a diffuse component.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE = 1 << 2;
	
	/**
	 * A bit flag that is used for BXDFs with a glossy component.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY = 1 << 3;
	
	/**
	 * A bit flag that is used for BXDFs with a specular component.
	 */
	protected static final int B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR = 1 << 4;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	Constants for BSDF:
	private static final int B_S_D_F_ARRAY_LENGTH = 11;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F = 3;
	private static final int B_S_D_F_ARRAY_OFFSET_B_X_D_F_COUNT = 2;
	private static final int B_S_D_F_ARRAY_OFFSET_ETA = 0;
	private static final int B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING = 1;
	
//	Constants for BSDFResult:
	private static final int B_S_D_F_RESULT_ARRAY_LENGTH = 14;
	private static final int B_S_D_F_RESULT_ARRAY_OFFSET_B_X_D_F_ID = 13;
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
	private static final int B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ID = (1 << 16) | (B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF DisneyClearCoatBRDF:
	private static final int B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_LENGTH = 2;
	private static final int B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_GLOSS = 0;
	private static final int B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_WEIGHT = 1;
	private static final int B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ID = (2 << 16) | (B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF DisneyDiffuseBRDF:
	private static final int B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ID = (3 << 16) | (B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF DisneyFakeSSBRDF:
	private static final int B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_LENGTH = 4;
	private static final int B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_ROUGHNESS = 3;
	private static final int B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ID = (4 << 16) | (B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF DisneyRetroBRDF:
	private static final int B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_LENGTH = 4;
	private static final int B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_ROUGHNESS = 3;
	private static final int B_X_D_F_DISNEY_RETRO_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_DISNEY_RETRO_B_R_D_F_ID = (5 << 16) | (B_X_D_F_DISNEY_RETRO_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF DisneySheenBRDF:
	private static final int B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_DISNEY_SHEEN_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_DISNEY_SHEEN_B_R_D_F_ID = (6 << 16) | (B_X_D_F_DISNEY_SHEEN_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF FresnelBlendBRDF using an implicit MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_LENGTH = 10;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_X = 8;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_Y = 9;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 6;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 7;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE = 0;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR = 3;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_FRESNEL_BLEND_B_R_D_F_ID = (7 << 16) | (B_X_D_F_FRESNEL_BLEND_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF LambertianBRDF:
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_LAMBERTIAN_B_R_D_F_ID = (8 << 16) | (B_X_D_F_LAMBERTIAN_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF LambertianBTDF:
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_LENGTH = 3;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE = 0;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_LAMBERTIAN_B_T_D_F_ID = (9 << 16) | (B_X_D_F_LAMBERTIAN_B_T_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF OrenNayarBRDF:
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH = 6;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A = 4;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_ANGLE_RADIANS = 0;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B = 5;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE = 1;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_DIFFUSE;
	private static final int B_X_D_F_OREN_NAYAR_B_R_D_F_ID = (10 << 16) | (B_X_D_F_OREN_NAYAR_B_R_D_F_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDFResult:
	private static final int B_X_D_F_RESULT_ARRAY_LENGTH = 13;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_INCOMING = 3;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_NORMAL = 6;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_OUTGOING = 9;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 12;
	private static final int B_X_D_F_RESULT_ARRAY_OFFSET_RESULT = 0;
	
//	Constants for BXDF SpecularBRDF using Fresnel of type ConstantFresnel:
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_LENGTH = 6;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT = 3;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ID = (11 << 16) | (B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF SpecularBRDF using Fresnel of type DielectricFresnel:
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 5;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I = 3;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T = 4;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	private static final int B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ID = (12 << 16) | (B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF SpecularBTDF using Fresnel of type DielectricFresnel:
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 5;
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A = 3;
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B = 4;
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE = 0;
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION | B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	private static final int B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ID = (13 << 16) | (B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF TorranceSparrowBRDF using Fresnel of type ConductorFresnel and an implicit MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_LENGTH = 16;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_X = 14;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_Y = 15;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I = 3;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T = 6;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 12;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 13;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K = 9;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ID = (14 << 16) | (B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF TorranceSparrowBRDF using Fresnel of type DielectricFresnel and an implicit MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 9;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X = 7;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y = 8;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I = 3;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T = 4;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 5;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 6;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ID = (15 << 16) | (B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF TorranceSparrowBRDF using Fresnel of type DisneyFresnel and an implicit MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_LENGTH = 12;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_X = 10;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_Y = 11;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ETA = 6;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 8;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 9;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_METALLIC = 7;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 = 3;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE = 0;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ID = (16 << 16) | (B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_BIT_FLAGS & 0xFFFF);
	
//	Constants for BXDF TorranceSparrowBTDF using Fresnel of type DielectricFresnel and an implicit MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH = 9;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X = 7;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y = 8;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A = 3;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B = 4;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 5;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 6;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE = 0;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS = B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION | B_X_D_F_TYPE_BIT_FLAG_IS_GLOSSY;
	private static final int B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ID = (17 << 16) | (B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_BIT_FLAGS & 0xFFFF);
	
//	Constants for the current implementation:
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING = 0;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL = 3;
	private static final int MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING = 6;
	private static final int MATERIAL_B_X_D_F_ARRAY_SIZE = 16;
	
//	Constants for MicrofacetDistribution of type TrowbridgeReitzMicrofacetDistribution:
	private static final int MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_LENGTH = 4;
	private static final int MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA = 0;
	private static final int MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SEPARABLE_MODEL = 1;
	private static final int MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_X = 2;
	private static final int MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_Y = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code float[]} that contains a {@link BSDF} instance.
	 */
	protected float[] bSDFArray_$private$11;
	
	/**
	 * A {@code float[]} that contains a {@link BSDFResult} instance.
	 */
	protected float[] bSDFResultArray_$private$14;
	
	/**
	 * A {@code float[]} that contains an {@link AshikhminShirleyBRDF} instance.
	 */
	protected float[] bXDFAshikhminShirleyBRDFArray_$private$4;
	
	/**
	 * A {@code float[]} that contains a {@link DisneyClearCoatBRDF} instance.
	 */
	protected float[] bXDFDisneyClearCoatBRDFArray_$private$2;
	
	/**
	 * A {@code float[]} that contains a {@link DisneyDiffuseBRDF} instance.
	 */
	protected float[] bXDFDisneyDiffuseBRDFArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a {@link DisneyFakeSSBRDF} instance.
	 */
	protected float[] bXDFDisneyFakeSSBRDFArray_$private$4;
	
	/**
	 * A {@code float[]} that contains a {@link DisneyRetroBRDF} instance.
	 */
	protected float[] bXDFDisneyRetroBRDFArray_$private$4;
	
	/**
	 * A {@code float[]} that contains a {@link DisneySheenBRDF} instance.
	 */
	protected float[] bXDFDisneySheenBRDFArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a {@link FresnelBlendBRDF} instance that use {@link TrowbridgeReitzMicrofacetDistribution}.
	 */
	protected float[] bXDFFresnelBlendBRDFArray_$private$10;
	
	/**
	 * A {@code float[]} that contains a {@link LambertianBRDF} instance.
	 */
	protected float[] bXDFLambertianBRDFArray_$private$3;
	
	/**
	 * A {@code float[]} that contains a {@link LambertianBTDF} instance.
	 */
	protected float[] bXDFLambertianBTDFArray_$private$3;
	
	/**
	 * A {@code float[]} that contains an {@link OrenNayarBRDF} instance.
	 */
	protected float[] bXDFOrenNayarBRDFArray_$private$6;
	
	/**
	 * A {@code float[]} that contains a {@link BXDFResult} instance.
	 */
	protected float[] bXDFResultArray_$private$13;
	
	/**
	 * A {@code float[]} that contains a {@link SpecularBRDF} instance that use {@link ConstantFresnel}.
	 */
	protected float[] bXDFSpecularBRDFFresnelConstantArray_$private$6;
	
	/**
	 * A {@code float[]} that contains a {@link SpecularBRDF} instance that use {@link DielectricFresnel}.
	 */
	protected float[] bXDFSpecularBRDFFresnelDielectricArray_$private$5;
	
	/**
	 * A {@code float[]} that contains a {@link SpecularBTDF} instance that use {@link DielectricFresnel}.
	 */
	protected float[] bXDFSpecularBTDFFresnelDielectricArray_$private$5;
	
	/**
	 * A {@code float[]} that contains a {@link TorranceSparrowBRDF} instance that use {@link ConductorFresnel} and {@link TrowbridgeReitzMicrofacetDistribution}.
	 */
	protected float[] bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16;
	
	/**
	 * A {@code float[]} that contains a {@link TorranceSparrowBRDF} instance that use {@link DielectricFresnel} and {@link TrowbridgeReitzMicrofacetDistribution}.
	 */
	protected float[] bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9;
	
	/**
	 * A {@code float[]} that contains a {@link TorranceSparrowBRDF} instance that use {@link DisneyFresnel} and {@link TrowbridgeReitzMicrofacetDistribution}.
	 */
	protected float[] bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12;
	
	/**
	 * A {@code float[]} that contains a {@link TorranceSparrowBTDF} instance that use {@link DielectricFresnel} and {@link TrowbridgeReitzMicrofacetDistribution}.
	 */
	protected float[] bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9;
	
	/**
	 * A {@code float[]} that contains a {@link BXDFResult} instance.
	 */
	protected float[] materialBXDFResultArray_$private$16;
	
	/**
	 * A {@code float[]} that contains a {@link TrowbridgeReitzMicrofacetDistribution} instance.
	 */
	protected float[] microfacetDistributionTrowbridgeReitzArray_$private$4;
	
	/**
	 * An {@code int[]} that contains {@link ClearCoatMaterial} instances.
	 */
	protected int[] materialClearCoatMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link DisneyMaterial} instances.
	 */
	protected int[] materialDisneyMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link GlassMaterial} instances.
	 */
	protected int[] materialGlassMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link GlossyMaterial} instances.
	 */
	protected int[] materialGlossyMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link MatteMaterial} instances.
	 */
	protected int[] materialMatteMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link MetalMaterial} instances.
	 */
	protected int[] materialMetalMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link MirrorMaterial} instances.
	 */
	protected int[] materialMirrorMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link PlasticMaterial} instances.
	 */
	protected int[] materialPlasticMaterialArray;
	
	/**
	 * An {@code int[]} that contains {@link SubstrateMaterial} instances.
	 */
	protected int[] materialSubstrateMaterialArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractMaterialKernel} instance.
	 */
	protected AbstractMaterialKernel() {
		this.bSDFArray_$private$11 = new float[B_S_D_F_ARRAY_LENGTH];
		this.bSDFResultArray_$private$14 = new float[B_S_D_F_RESULT_ARRAY_LENGTH];
		this.bXDFAshikhminShirleyBRDFArray_$private$4 = new float[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_LENGTH];
		this.bXDFDisneyClearCoatBRDFArray_$private$2 = new float[B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_LENGTH];
		this.bXDFDisneyDiffuseBRDFArray_$private$3 = new float[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_LENGTH];
		this.bXDFDisneyFakeSSBRDFArray_$private$4 = new float[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_LENGTH];
		this.bXDFDisneyRetroBRDFArray_$private$4 = new float[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_LENGTH];
		this.bXDFDisneySheenBRDFArray_$private$3 = new float[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_LENGTH];
		this.bXDFFresnelBlendBRDFArray_$private$10 = new float[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_LENGTH];
		this.bXDFLambertianBRDFArray_$private$3 = new float[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_LENGTH];
		this.bXDFLambertianBTDFArray_$private$3 = new float[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_LENGTH];
		this.bXDFOrenNayarBRDFArray_$private$6 = new float[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_LENGTH];
		this.bXDFResultArray_$private$13 = new float[B_X_D_F_RESULT_ARRAY_LENGTH];
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6 = new float[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_LENGTH];
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5 = new float[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5 = new float[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16 = new float[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_LENGTH];
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9 = new float[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12 = new float[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_LENGTH];
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9 = new float[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_LENGTH];
		this.materialBXDFResultArray_$private$16 = new float[MATERIAL_B_X_D_F_ARRAY_SIZE];
		this.microfacetDistributionTrowbridgeReitzArray_$private$4 = new float[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_LENGTH];
		this.materialClearCoatMaterialArray = new int[1];
		this.materialDisneyMaterialArray = new int[1];
		this.materialGlassMaterialArray = new int[1];
		this.materialGlossyMaterialArray = new int[1];
		this.materialMatteMaterialArray = new int[1];
		this.materialMetalMaterialArray = new int[1];
		this.materialMirrorMaterialArray = new int[1];
		this.materialPlasticMaterialArray = new int[1];
		this.materialSubstrateMaterialArray = new int[1];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the {@link Material} instance represented by {@code materialID} is specular, {@code false} otherwise.
	 * <p>
	 * This method is used by the old material system and may be removed in the future.
	 * 
	 * @param materialID the ID of the {@code Material} instance
	 * @return {@code true} if, and only if, the {@code Material} instance represented by {@code materialID} is specular, {@code false} otherwise
	 */
	@SuppressWarnings("static-method")
	protected final boolean materialIsSpecular(final int materialID) {
		return materialID == GlassMaterial.ID || materialID == MirrorMaterial.ID;
	}
	
	/**
	 * Samples the distribution function.
	 * <p>
	 * Returns {@code true} if, and only if, a sample was created, {@code false} otherwise.
	 * <p>
	 * This method is used by the old material system and may be removed in the future.
	 * 
	 * @param materialID the ID of the {@link Material} instance
	 * @param materialOffset the offset for the {@code Material} instance
	 * @return {@code true} if, and only if, a sample was created, {@code false} otherwise
	 */
	protected final boolean materialSampleDistributionFunction(final int materialID, final int materialOffset) {
		if(materialID == ClearCoatMaterial.ID) {
			return doMaterialSampleDistributionFunctionClearCoatMaterial(materialOffset);
		} else if(materialID == GlassMaterial.ID) {
			return doMaterialSampleDistributionFunctionGlassMaterial(materialOffset);
		} else if(materialID == GlossyMaterial.ID) {
			return doMaterialSampleDistributionFunctionGlossyMaterial(materialOffset);
		} else if(materialID == MatteMaterial.ID) {
			return doMaterialSampleDistributionFunctionMatteMaterial(materialOffset);
		} else if(materialID == MirrorMaterial.ID) {
			return doMaterialSampleDistributionFunctionMirrorMaterial(materialOffset);
		} else {
			return false;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doMaterialSampleDistributionFunctionClearCoatMaterial(final int materialClearCoatMaterialArrayOffset) {
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
		
		final boolean isRefracting = vector3FSetRefraction2(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			final float reflectance = doFresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
				
				color3FLHSSet(colorKSR * probabilityRussianRouletteReflection, colorKSG * probabilityRussianRouletteReflection, colorKSB * probabilityRussianRouletteReflection);
			} else {
				vector3FSetDiffuseReflection(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, random(), random());
				
				color3FLHSSet(colorKDR * probabilityRussianRouletteTransmission, colorKDG * probabilityRussianRouletteTransmission, colorKDB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			color3FLHSSet(colorKSR, colorKSG, colorKSB);
		}
		
		return true;
	}
	
	private boolean doMaterialSampleDistributionFunctionGlassMaterial(final int materialGlassMaterialArrayOffset) {
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
		
		final boolean isRefracting = vector3FSetRefraction2(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta);
		final boolean isReflecting = !isRefracting;
		
		if(isRefracting) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			final float reflectance = doFresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
				vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
				
				color3FLHSSet(colorKRR * probabilityRussianRouletteReflection, colorKRG * probabilityRussianRouletteReflection, colorKRB * probabilityRussianRouletteReflection);
			} else {
				color3FLHSSet(colorKTR * probabilityRussianRouletteTransmission, colorKTG * probabilityRussianRouletteTransmission, colorKTB * probabilityRussianRouletteTransmission);
			}
		}
		
		if(isReflecting) {
			vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			color3FLHSSet(colorKRR, colorKRG, colorKRB);
		}
		
		return true;
	}
	
	private boolean doMaterialSampleDistributionFunctionGlossyMaterial(final int materialGlossyMaterialArrayOffset) {
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
		doMaterialBXDFBegin();
		
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
		final float f = doSchlickFresnelDielectric1(outgoingShadeSpaceDotHalfShadeSpace, 1.0F);
		final float g = 4.0F * abs(normalShadeSpaceDotOutgoingShadeSpace + -normalShadeSpaceDotIncomingShadeSpace - normalShadeSpaceDotOutgoingShadeSpace * -normalShadeSpaceDotIncomingShadeSpace);
		
//		Compute the result:
		final float resultR = colorKRR * d * f / g;
		final float resultG = colorKRG * d * f / g;
		final float resultB = colorKRB * d * f / g;
		
		/*
		 * BSDF:
		 */
		
//		Set the incoming direction in shade space and perform shade space to world space transformations:
		doMaterialBXDFSetIncoming(incomingShadeSpaceX, incomingShadeSpaceY, incomingShadeSpaceZ);
		doMaterialBXDFEnd();
		
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
	
	private boolean doMaterialSampleDistributionFunctionMatteMaterial(final int materialMatteMaterialArrayOffset) {
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
	
	private boolean doMaterialSampleDistributionFunctionMirrorMaterial(final int materialMirrorMaterialArrayOffset) {
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
		
		vector3FSetSpecularReflectionFacingSurface(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
		
		color3FLHSSet(colorKRR, colorKRG, colorKRB);
		
		return true;
	}
	
	private void doMaterialBXDFBegin() {
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
		doMaterialBXDFSetNormal(normalShadeSpaceX, normalShadeSpaceY, normalShadeSpaceZ);
		doMaterialBXDFSetOutgoing(outgoingShadeSpaceX, outgoingShadeSpaceY, outgoingShadeSpaceZ);
	}
	
	private void doMaterialBXDFEnd() {
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
	
	private void doMaterialBXDFSetIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 0] = incomingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 1] = incomingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_INCOMING + 2] = incomingZ;
	}
	
	private void doMaterialBXDFSetNormal(final float normalX, final float normalY, final float normalZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 0] = normalX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 1] = normalY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_NORMAL + 2] = normalZ;
	}
	
	private void doMaterialBXDFSetOutgoing(final float outgoingX, final float outgoingY, final float outgoingZ) {
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 0] = outgoingX;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 1] = outgoingY;
		this.materialBXDFResultArray_$private$16[MATERIAL_B_X_D_F_ARRAY_OFFSET_OUTGOING + 2] = outgoingZ;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * The following is a test implementation of the Material API in the CPU-renderer that is based on PBRT. It is an early work-in-progress. However, it is necessary in order to support most (if not all) Material implementations.
	 */
	
	/**
	 * Computes a {@link BSDF} at the current intersection.
	 * <p>
	 * Returns {@code true} if, and only if, the {@code BSDF} was computed, {@code false} otherwise.
	 * <p>
	 * This method assumes the {@link AbstractGeometryKernel#intersectionArray_$private$24 intersectionArray_$private$24} array has been filled in with intersection data.
	 * 
	 * @param materialID the ID of the {@link Material} instance
	 * @param materialOffset the offset for the {@code Material} instance
	 * @return {@code true} if, and only if, the {@code BSDF} was computed, {@code false} otherwise
	 */
	protected final boolean materialBSDFCompute(final int materialID, final int materialOffset) {
		if(materialID == ClearCoatMaterial.ID) {
			return doMaterialClearCoatMaterialComputeBSDF(materialOffset);
		} else if(materialID == DisneyMaterial.ID) {
			return doMaterialDisneyMaterialComputeBSDF(materialOffset);
		} else if(materialID == GlassMaterial.ID) {
			return doMaterialGlassMaterialComputeBSDF(materialOffset);
		} else if(materialID == GlossyMaterial.ID) {
			return doMaterialGlossyMaterialComputeBSDF(materialOffset);
		} else if(materialID == MatteMaterial.ID) {
			return doMaterialMatteMaterialComputeBSDF(materialOffset);
		} else if(materialID == MetalMaterial.ID) {
			return doMaterialMetalMaterialComputeBSDF(materialOffset);
		} else if(materialID == MirrorMaterial.ID) {
			return doMaterialMirrorMaterialComputeBSDF(materialOffset);
		} else if(materialID == PlasticMaterial.ID) {
			return doMaterialPlasticMaterialComputeBSDF(materialOffset);
		} else if(materialID == SubstrateMaterial.ID) {
			return doMaterialSubstrateMaterialComputeBSDF(materialOffset);
		} else {
			return false;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, the {@link BXDF} instance that is associated with the current {@link BSDFResult} instance is specular, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the {@code BXDF} instance that is associated with the current {@code BSDFResult} instance is specular, {@code false} otherwise
	 */
	protected final boolean materialBSDFResultBXDFIsSpecular() {
		return doBXDFIsSpecular(materialBSDFResultGetBXDFID());
	}
	
	/**
	 * Samples the distribution function using the current {@link BSDF} instance.
	 * <p>
	 * Returns {@code true} if, and only if, a sample was created, {@code false} otherwise.
	 * <p>
	 * This method assumes the {@link #materialBSDFCompute(int, int)} method was called with a return value of {@code true}.
	 * <p>
	 * The result of the sampling will be set in {@link #bSDFResultArray_$private$14}.
	 * <p>
	 * To retrieve the incoming direction in world space, the methods {@link #materialBSDFResultGetIncomingX()}, {@link #materialBSDFResultGetIncomingY()} and {@link #materialBSDFResultGetIncomingZ()} may be used.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #materialBSDFResultGetResultR()}, {@link #materialBSDFResultGetResultG()} and {@link #materialBSDFResultGetResultB()} may be used.
	 * <p>
	 * To retrieve the probability density function (PDF) value, the method {@link #materialBSDFResultGetProbabilityDensityFunctionValue()} may be used.
	 * 
	 * @param bitFlags the {@link BXDFType} bit flags to match against
	 * @return {@code true} if, and only if, a sample was created, {@code false} otherwise
	 */
	protected final boolean materialBSDFSampleDistributionFunction(final int bitFlags) {
		final int countBXDFs = doBSDFGetBXDFCount();
		
		int matches = 0;
		
		for(int i = 0; i < countBXDFs; i++) {
			final int id = doBSDFGetBXDF(i);
			
			if(doBXDFIsMatchingBitFlags(id, bitFlags)) {
				matches++;
			}
		}
		
		if(matches > 0 && !checkIsZero(doBXDFResultGetOutgoingZ())) {
			final float u = random();
			final float v = random();
			
			final int match = min((int)(floor(u * matches)), matches - 1);
			
			int matchingId = -1;
			
			for(int i = 0, j = match; i < countBXDFs; i++) {
				final int id = doBSDFGetBXDF(i);
				
				if(doBXDFIsMatchingBitFlags(id, bitFlags)) {
					if(j == 0) {
						matchingId = id;
						
						i = countBXDFs;
					}
					
					j--;
				}
			}
			
			if(matchingId != -1) {
				final float uRemapped = min(u * matches - match, 0.99999994F);
				final float vRemapped = v;
				
				if(doBXDFSampleDistributionFunction(uRemapped, vRemapped, matchingId)) {
					doBSDFResultSetIncomingFromBXDFResult();
					
					final float incomingX = materialBSDFResultGetIncomingX();
					final float incomingY = materialBSDFResultGetIncomingY();
					final float incomingZ = materialBSDFResultGetIncomingZ();
					
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
					
					doBSDFResultSetBXDFID(matchingId);
					doBSDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
					doBSDFResultSetResult(resultR, resultG, resultB);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the index of refraction (IOR) that is associated with the current {@link BSDF} instance.
	 * 
	 * @return the index of refraction (IOR) that is associated with the current {@code BSDF} instance
	 */
	protected final float materialBSDFGetEta() {
		return this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA];
	}
	
	/**
	 * Returns the X-component of the incoming direction in world space that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the X-component of the incoming direction in world space that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetIncomingX() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0];
	}
	
	/**
	 * Returns the Y-component of the incoming direction in world space that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the Y-component of the incoming direction in world space that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetIncomingY() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1];
	}
	
	/**
	 * Returns the Z-component of the incoming direction in world space that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the Z-component of the incoming direction in world space that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetIncomingZ() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2];
	}
	
	/**
	 * Returns the probability density function (PDF) value that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the probability density function (PDF) value that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetProbabilityDensityFunctionValue() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE];
	}
	
	/**
	 * Returns the B-component of the result that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the B-component of the result that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetResultB() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 2];
	}
	
	/**
	 * Returns the G-component of the result that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the G-component of the result that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetResultG() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 1];
	}
	
	/**
	 * Returns the R-component of the result that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the R-component of the result that is associated with the current {@code BSDFResult} instance
	 */
	protected final float materialBSDFResultGetResultR() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 0];
	}
	
	/**
	 * Returns the {@link BXDF} count by specular or non-specular type.
	 * 
	 * @param isSpecular {@code true} if, and only if, specular types should be counted, {@code false} otherwise
	 * @return the {@code BXDF} count by specular or non-specular type
	 */
	protected final int materialBSDFCountBXDFsBySpecularType(final boolean isSpecular) {
		int countBySpecular = 0;
		
		final int countBXDFs = doBSDFGetBXDFCount();
		
		for(int i = 0; i < countBXDFs; i++) {
			if(isSpecular == doBXDFIsSpecular(doBSDFGetBXDF(i))) {
				countBySpecular++;
			}
		}
		
		return countBySpecular;
	}
	
	/**
	 * Returns the ID of the {@link BXDF} instance that is associated with the current {@link BSDFResult} instance.
	 * 
	 * @return the ID of the {@code BXDF} instance that is associated with the current {@code BSDFResult} instance
	 */
	protected final int materialBSDFResultGetBXDFID() {
		return (int)(this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_B_X_D_F_ID]);
	}
	
	/**
	 * Evaluates the distribution function using the current {@link BSDF} instance.
	 * <p>
	 * This method assumes the {@link #materialBSDFCompute(int, int)} method was called with a return value of {@code true}.
	 * <p>
	 * The result of the evaluation will be set in {@link #bSDFResultArray_$private$14}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #materialBSDFResultGetResultR()}, {@link #materialBSDFResultGetResultG()} and {@link #materialBSDFResultGetResultB()} may be used.
	 * 
	 * @param bitFlags the {@link BXDFType} bit flags to match against
	 * @param incomingX the X-component of the incoming direction in world space
	 * @param incomingY the Y-component of the incoming direction in world space
	 * @param incomingZ the Z-component of the incoming direction in world space
	 */
	protected final void materialBSDFEvaluateDistributionFunction(final int bitFlags, final float incomingX, final float incomingY, final float incomingZ) {
		doBSDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFResultSetIncomingTransformedFromBSDFResult();
		
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
	
	/**
	 * Evaluates the probability density function (PDF) using the current {@link BSDF} instance.
	 * <p>
	 * This method assumes the {@link #materialBSDFCompute(int, int)} method was called with a return value of {@code true}.
	 * <p>
	 * The result of the evaluation will be set in {@link #bSDFResultArray_$private$14}.
	 * <p>
	 * To retrieve the probability density function (PDF) value, the method {@link #materialBSDFResultGetProbabilityDensityFunctionValue()} may be used.
	 * 
	 * @param bitFlags the {@link BXDFType} bit flags to match against
	 * @param incomingX the X-component of the incoming direction in world space
	 * @param incomingY the Y-component of the incoming direction in world space
	 * @param incomingZ the Z-component of the incoming direction in world space
	 */
	protected final void materialBSDFEvaluateProbabilityDensityFunction(final int bitFlags, final float incomingX, final float incomingY, final float incomingZ) {
		doBSDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFResultSetIncomingTransformedFromBSDFResult();
		
		final int countBXDFs = doBSDFGetBXDFCount();
		
		float probabilityDensityFunctionValue = 0.0F;
		
		if(countBXDFs > 0 && !checkIsZero(doBXDFResultGetOutgoingZ())) {
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
	
	/**
	 * Evaluates the Emission {@link Texture} instance that is associated with the {@link Material} instance that is represented by {@code materialID} and {@code materialOffset}.
	 * <p>
	 * The result of the evaluation will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} or {@link #color3FLHSGetComponent3()} may be used.
	 * 
	 * @param materialID the ID of the current {@code Material}
	 * @param materialOffset the offset for the current {@code Material}
	 */
	protected final void materialEmittance(final int materialID, final int materialOffset) {
		final int materialOffsetTextureEmission = materialOffset + Material.ARRAY_OFFSET_TEXTURE_EMISSION;
		
		int textureEmission = 0;
		
		if(materialID == ClearCoatMaterial.ID) {
			textureEmission = this.materialClearCoatMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == DisneyMaterial.ID) {
			textureEmission = this.materialDisneyMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == GlassMaterial.ID) {
			textureEmission = this.materialGlassMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == GlossyMaterial.ID) {
			textureEmission = this.materialGlossyMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == MatteMaterial.ID) {
			textureEmission = this.materialMatteMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == MetalMaterial.ID) {
			textureEmission = this.materialMetalMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == MirrorMaterial.ID) {
			textureEmission = this.materialMirrorMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == PlasticMaterial.ID) {
			textureEmission = this.materialPlasticMaterialArray[materialOffsetTextureEmission];
		} else if(materialID == SubstrateMaterial.ID) {
			textureEmission = this.materialSubstrateMaterialArray[materialOffsetTextureEmission];
		}
		
		final int textureEmissionID = (textureEmission >>> 16) & 0xFFFF;
		final int textureEmissionOffset = textureEmission & 0xFFFF;
		
		textureEvaluate(textureEmissionID, textureEmissionOffset);
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
		
		if(vector3FSetRefraction2(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, eta)) {
			final float refractionDirectionX = vector3FGetComponent1();
			final float refractionDirectionY = vector3FGetComponent2();
			final float refractionDirectionZ = vector3FGetComponent3();
			
			final float cosThetaI = vector3FDotProduct(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
			final float cosThetaICorrectlyOriented = isEntering ? -cosThetaI : vector3FDotProduct(refractionDirectionX, refractionDirectionY, refractionDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
			
			final float reflectance = doFresnelDielectric(cosThetaICorrectlyOriented, etaA, etaB);
			final float transmittance = 1.0F - reflectance;
			
			final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
			final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
			final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
			
			final boolean isChoosingSpecularReflection = random() < probabilityRussianRoulette;
			
			if(isChoosingSpecularReflection) {
//				Initialize the BSDF:
				doBSDFClear();
				doBSDFSetBXDFCount(1);
				doBSDFSetEta(1.0F);
				doBSDFSetNegatingIncoming(false);
				
//				Set SpecularBRDF:
				doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
				doBXDFSpecularBRDFFresnelConstantSetFresnelConstant(1.0F, 1.0F, 1.0F);
				doBXDFSpecularBRDFFresnelConstantSetReflectanceScale(colorKSR * probabilityRussianRouletteReflection, colorKSG * probabilityRussianRouletteReflection, colorKSB * probabilityRussianRouletteReflection);
				
//				Initialize the BSDFResult:
				doBSDFResultInitialize();
				
				return true;
			}
			
//			Initialize the BSDF:
			doBSDFClear();
			doBSDFSetBXDFCount(1);
			doBSDFSetEta(1.0F);
			doBSDFSetNegatingIncoming(false);
			
//			Set LambertianBRDF:
			doBSDFSetBXDFLambertianBRDF(0);
			doBXDFLambertianBRDFSetReflectanceScale(colorKDR * probabilityRussianRouletteTransmission, colorKDG * probabilityRussianRouletteTransmission, colorKDB * probabilityRussianRouletteTransmission);
			
//			Initialize the BSDFResult:
			doBSDFResultInitialize();
			
			return true;
		}
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
//		Set the SpecularBRDF:
		doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
		doBXDFSpecularBRDFFresnelConstantSetFresnelConstant(1.0F, 1.0F, 1.0F);
		doBXDFSpecularBRDFFresnelConstantSetReflectanceScale(colorKSR, colorKSG, colorKSB);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialDisneyMaterialComputeBSDF(final int materialDisneyMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the Anisotropic Texture:
		final int textureAnisotropic = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ANISOTROPIC];
		final int textureAnisotropicID = (textureAnisotropic >>> 16) & 0xFFFF;
		final int textureAnisotropicOffset = textureAnisotropic & 0xFFFF;
		
//		Retrieve the ID and offset for the Clear Coat Texture:
		final int textureClearCoat = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_CLEAR_COAT];
		final int textureClearCoatID = (textureClearCoat >>> 16) & 0xFFFF;
		final int textureClearCoatOffset = textureClearCoat & 0xFFFF;
		
//		Retrieve the ID and offset for the Clear Coat Gloss Texture:
		final int textureClearCoatGloss = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_CLEAR_COAT_GLOSS];
		final int textureClearCoatGlossID = (textureClearCoatGloss >>> 16) & 0xFFFF;
		final int textureClearCoatGlossOffset = textureClearCoatGloss & 0xFFFF;
		
//		Retrieve the ID and offset for the Color Texture:
		final int textureColor = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_COLOR];
		final int textureColorID = (textureColor >>> 16) & 0xFFFF;
		final int textureColorOffset = textureColor & 0xFFFF;
		
//		Retrieve the ID and offset for the Diffuse Transmission Texture:
		final int textureDiffuseTransmission = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_DIFFUSE_TRANSMISSION];
		final int textureDiffuseTransmissionID = (textureDiffuseTransmission >>> 16) & 0xFFFF;
		final int textureDiffuseTransmissionOffset = textureDiffuseTransmission & 0xFFFF;
		
//		Retrieve the ID and offset for the Eta Texture:
		final int textureEta = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ETA];
		final int textureEtaID = (textureEta >>> 16) & 0xFFFF;
		final int textureEtaOffset = textureEta & 0xFFFF;
		
//		Retrieve the ID and offset for the Flatness Texture:
		final int textureFlatness = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_FLATNESS];
		final int textureFlatnessID = (textureFlatness >>> 16) & 0xFFFF;
		final int textureFlatnessOffset = textureFlatness & 0xFFFF;
		
//		Retrieve the ID and offset for the Metallic Texture:
		final int textureMetallic = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_METALLIC];
		final int textureMetallicID = (textureMetallic >>> 16) & 0xFFFF;
		final int textureMetallicOffset = textureMetallic & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness Texture:
		final int textureRoughness = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS];
		final int textureRoughnessID = (textureRoughness >>> 16) & 0xFFFF;
		final int textureRoughnessOffset = textureRoughness & 0xFFFF;
		
//		Retrieve the ID and offset for the Scatter Distance Texture:
		final int textureScatterDistance = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SCATTER_DISTANCE];
		final int textureScatterDistanceID = (textureScatterDistance >>> 16) & 0xFFFF;
		final int textureScatterDistanceOffset = textureScatterDistance & 0xFFFF;
		
//		Retrieve the ID and offset for the Sheen Texture:
		final int textureSheen = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SHEEN];
		final int textureSheenID = (textureSheen >>> 16) & 0xFFFF;
		final int textureSheenOffset = textureSheen & 0xFFFF;
		
//		Retrieve the ID and offset for the Sheen Tint Texture:
		final int textureSheenTint = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SHEEN_TINT];
		final int textureSheenTintID = (textureSheenTint >>> 16) & 0xFFFF;
		final int textureSheenTintOffset = textureSheenTint & 0xFFFF;
		
//		Retrieve the ID and offset for the Specular Tint Texture:
		final int textureSpecularTint = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SPECULAR_TINT];
		final int textureSpecularTintID = (textureSpecularTint >>> 16) & 0xFFFF;
		final int textureSpecularTintOffset = textureSpecularTint & 0xFFFF;
		
//		Retrieve the ID and offset for the Specular Transmission Texture:
		final int textureSpecularTransmission = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_TEXTURE_SPECULAR_TRANSMISSION];
		final int textureSpecularTransmissionID = (textureSpecularTransmission >>> 16) & 0xFFFF;
		final int textureSpecularTransmissionOffset = textureSpecularTransmission & 0xFFFF;
		
//		Retrieve the thin flag:
		final boolean isThin = this.materialDisneyMaterialArray[materialDisneyMaterialArrayOffset + DisneyMaterial.ARRAY_OFFSET_IS_THIN] != 0;
		
		final float floatAnisotropic = textureEvaluateFloat(textureAnisotropicID, textureAnisotropicOffset);
		final float floatClearCoat = textureEvaluateFloat(textureClearCoatID, textureClearCoatOffset);
		final float floatClearCoatGloss = textureEvaluateFloat(textureClearCoatGlossID, textureClearCoatGlossOffset);
		final float floatDiffuseTransmission = textureEvaluateFloat(textureDiffuseTransmissionID, textureDiffuseTransmissionOffset) / 2.0F;
		final float floatEta = textureEvaluateFloat(textureEtaID, textureEtaOffset);
		final float floatMetallic = textureEvaluateFloat(textureMetallicID, textureMetallicOffset);
		final float floatRoughness = textureEvaluateFloat(textureRoughnessID, textureRoughnessOffset);
		final float floatSheen = textureEvaluateFloat(textureSheenID, textureSheenOffset);
		final float floatSheenTint = textureEvaluateFloat(textureSheenTintID, textureSheenTintOffset);
		final float floatSpecularTint = textureEvaluateFloat(textureSpecularTintID, textureSpecularTintOffset);
		final float floatSpecularTransmission = textureEvaluateFloat(textureSpecularTransmissionID, textureSpecularTransmissionOffset);
		
		final boolean hasClearCoat = floatClearCoat > 0.0F;
		final boolean hasSheen = floatSheen > 0.0F;
		final boolean hasSpecularTransmission = floatSpecularTransmission > 0.0F;
		
		textureEvaluate(textureColorID, textureColorOffset);
		
		final float colorColorR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorColorG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorColorB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		textureEvaluate(textureScatterDistanceID, textureScatterDistanceOffset);
		
		final float colorScatterDistanceR = color3FLHSGetComponent1();
		final float colorScatterDistanceG = color3FLHSGetComponent2();
		final float colorScatterDistanceB = color3FLHSGetComponent3();
		
		final boolean hasScatterDistance = !checkIsZero(colorScatterDistanceR) || !checkIsZero(colorScatterDistanceG) || !checkIsZero(colorScatterDistanceB);
		
		final float luminance = colorColorR * 0.212671F + colorColorG * 0.715160F + colorColorB * 0.072169F;
		
		final boolean hasLuminance = luminance > 0.0F;
		
		final float colorTintR = hasLuminance ? colorColorR / luminance : 1.0F;
		final float colorTintG = hasLuminance ? colorColorG / luminance : 1.0F;
		final float colorTintB = hasLuminance ? colorColorB / luminance : 1.0F;
		
		final float colorSheenR = hasSheen ? lerp(1.0F, colorTintR, floatSheenTint) : 0.0F;
		final float colorSheenG = hasSheen ? lerp(1.0F, colorTintG, floatSheenTint) : 0.0F;
		final float colorSheenB = hasSheen ? lerp(1.0F, colorTintB, floatSheenTint) : 0.0F;
		
		final float diffuseWeight = (1.0F - floatMetallic) * (1.0F - floatSpecularTransmission);
		
		final boolean hasDiffuseWeight = diffuseWeight > 0.0F;
		
		/*
		 * Compute the BSDF:
		 */
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
		int index = 0;
		
		if(hasDiffuseWeight && isThin) {
			final float floatFlatness = textureEvaluateFloat(textureFlatnessID, textureFlatnessOffset);
			
			final float scale0 = diffuseWeight * (1.0F - floatFlatness) * (1.0F - floatDiffuseTransmission);
			final float scale1 = diffuseWeight * (0.0F + floatFlatness) * (1.0F - floatDiffuseTransmission);
			
//			Set DisneyDiffuseBRDF:
			doBSDFSetBXDFDisneyDiffuseBRDF(index++);
			doBXDFDisneyDiffuseBRDFSetReflectanceScale(colorColorR * scale0, colorColorG * scale0, colorColorB * scale0);
			
//			Set DisneyFakeSSBRDF:
			doBSDFSetBXDFDisneyFakeSSBRDF(index++);
			doBXDFDisneyFakeSSBRDFSetReflectanceScale(colorColorR * scale1, colorColorG * scale1, colorColorB * scale1);
			doBXDFDisneyFakeSSBRDFSetRoughness(floatRoughness);
		}
		
		if(hasDiffuseWeight && !isThin && !hasScatterDistance) {
//			Set DisneyDiffuseBRDF:
			doBSDFSetBXDFDisneyDiffuseBRDF(index++);
			doBXDFDisneyDiffuseBRDFSetReflectanceScale(colorColorR * diffuseWeight, colorColorG * diffuseWeight, colorColorB * diffuseWeight);
		}
		
		if(hasDiffuseWeight && !isThin && hasScatterDistance) {
//			Set SpecularBTDF:
			doBSDFSetBXDFSpecularBTDFFresnelDielectric(index++);
			doBXDFSpecularBTDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
			doBXDFSpecularBTDFFresnelDielectricSetTransmittanceScale(1.0F, 1.0F, 1.0F);
		}
		
		if(hasDiffuseWeight) {
//			Set DisneyRetroBRDF:
			doBSDFSetBXDFDisneyRetroBRDF(index++);
			doBXDFDisneyRetroBRDFSetReflectanceScale(colorColorR * diffuseWeight, colorColorG * diffuseWeight, colorColorB * diffuseWeight);
			doBXDFDisneyRetroBRDFSetRoughness(floatRoughness);
		}
		
		if(hasDiffuseWeight && hasSheen) {
//			Set DisneySheenBRDF:
			doBSDFSetBXDFDisneySheenBRDF(index++);
			doBXDFDisneySheenBRDFSetReflectanceScale(colorSheenR * diffuseWeight * floatSheen, colorSheenG * diffuseWeight * floatSheen, colorSheenB * diffuseWeight * floatSheen);
		}
		
		final float aspect = sqrt(1.0F - floatAnisotropic * 0.9F);
		
		final float alphaX = max(0.001F, floatRoughness * floatRoughness / aspect);
		final float alphaY = max(0.001F, floatRoughness * floatRoughness * aspect);
		
		final float floatR0 = ((floatEta - 1.0F) * (floatEta - 1.0F)) / ((floatEta + 1.0F) * (floatEta + 1.0F));
		
		final float colorSpecularR0R = lerp(lerp(1.0F, colorTintR, floatSpecularTint) * floatR0, colorColorR, floatMetallic);
		final float colorSpecularR0G = lerp(lerp(1.0F, colorTintG, floatSpecularTint) * floatR0, colorColorG, floatMetallic);
		final float colorSpecularR0B = lerp(lerp(1.0F, colorTintB, floatSpecularTint) * floatR0, colorColorB, floatMetallic);
		
//		Set TorranceSparrowBRDF:
		doBSDFSetBXDFTorranceSparrowBRDFFresnelDisney(index++);
		doBXDFTorranceSparrowBRDFFresnelDisneySetFresnelDisney(colorSpecularR0R, colorSpecularR0G, colorSpecularR0B, floatEta, floatMetallic);
		doBXDFTorranceSparrowBRDFFresnelDisneySetMicrofacetDistributionTrowbridgeReitz(true, true, alphaX, alphaY);
		doBXDFTorranceSparrowBRDFFresnelDisneySetReflectanceScale(1.0F, 1.0F, 1.0F);
		
		if(hasClearCoat) {
//			Set DisneyClearCoatBRDF:
			doBSDFSetBXDFDisneyClearCoatBRDF(index++);
			doBXDFDisneyClearCoatBRDFSetGloss(lerp(0.1F, 0.001F, floatClearCoatGloss));
			doBXDFDisneyClearCoatBRDFSetWeight(floatClearCoat);
		}
		
		if(hasSpecularTransmission && isThin) {
			final float floatRoughnessScaled = (0.65F * floatEta - 0.35F) * floatRoughness;
			
			final float alphaXScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled / aspect);
			final float alphaYScaled = max(0.001F, floatRoughnessScaled * floatRoughnessScaled * aspect);
			
//			Set TorranceSparrowBTDF:
			doBSDFSetBXDFTorranceSparrowBTDFFresnelDielectric(index++);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(true, false, alphaXScaled, alphaYScaled);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetTransmittanceScale(sqrt(colorColorR) * floatSpecularTransmission, sqrt(colorColorG) * floatSpecularTransmission, sqrt(colorColorB) * floatSpecularTransmission);
		}
		
		if(hasSpecularTransmission && !isThin) {
//			Set TorranceSparrowBTDF:
			doBSDFSetBXDFTorranceSparrowBTDFFresnelDielectric(index++);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(true, true, alphaX, alphaY);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetTransmittanceScale(sqrt(colorColorR) * floatSpecularTransmission, sqrt(colorColorG) * floatSpecularTransmission, sqrt(colorColorB) * floatSpecularTransmission);
		}
		
		if(isThin) {
//			Set LambertianBTDF:
			doBSDFSetBXDFLambertianBTDF(index++);
			doBXDFLambertianBTDFSetTransmittanceScale(colorColorR * floatDiffuseTransmission, colorColorG * floatDiffuseTransmission, colorColorB * floatDiffuseTransmission);
		}
		
//		Finish the BSDF:
		doBSDFSetBXDFCount(index);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialGlassMaterialComputeBSDF(final int materialGlassMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KR Texture:
		final int textureKR = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_R];
		final int textureKRID = (textureKR >>> 16) & 0xFFFF;
		final int textureKROffset = textureKR & 0xFFFF;
		
//		Retrieve the ID and offset for the KT Texture:
		final int textureKT = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_K_T];
		final int textureKTID = (textureKT >>> 16) & 0xFFFF;
		final int textureKTOffset = textureKT & 0xFFFF;
		
//		Retrieve the ID and offset for the Eta Texture:
		final int textureEta = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_ETA];
		final int textureEtaID = (textureEta >>> 16) & 0xFFFF;
		final int textureEtaOffset = textureEta & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness U Texture:
		final int textureRoughnessU = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U];
		final int textureRoughnessUID = (textureRoughnessU >>> 16) & 0xFFFF;
		final int textureRoughnessUOffset = textureRoughnessU & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness V Texture:
		final int textureRoughnessV = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V];
		final int textureRoughnessVID = (textureRoughnessV >>> 16) & 0xFFFF;
		final int textureRoughnessVOffset = textureRoughnessV & 0xFFFF;
		
//		Retrieve the roughness remapping flag:
		final boolean isRemappingRoughness = this.materialGlassMaterialArray[materialGlassMaterialArrayOffset + GlassMaterial.ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS] != 0;
		
//		Evaluate the KR Texture:
		textureEvaluate(textureKRID, textureKROffset);
		
//		Retrieve the color from the KR Texture:
		final float colorKRR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKRG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKRB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKR = !checkIsZero(colorKRR) || !checkIsZero(colorKRG) || !checkIsZero(colorKRB);
		
//		Evaluate the KT Texture:
		textureEvaluate(textureKTID, textureKTOffset);
		
//		Retrieve the color from the KT Texture:
		final float colorKTR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKTG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKTB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKT = !checkIsZero(colorKTR) || !checkIsZero(colorKTG) || !checkIsZero(colorKTB);
		
//		Evaluate the Eta Texture:
		final float floatEta = textureEvaluateFloat(textureEtaID, textureEtaOffset);
		
//		Evaluate the Roughness U Texture:
		final float floatRoughnessU = textureEvaluateFloat(textureRoughnessUID, textureRoughnessUOffset);
		final float floatRoughnessURemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessU) : floatRoughnessU;
		
//		Evaluate the Roughness V Texture:
		final float floatRoughnessV = textureEvaluateFloat(textureRoughnessVID, textureRoughnessVOffset);
		final float floatRoughnessVRemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessV) : floatRoughnessV;
		
//		final boolean isAllowingMultipleLobes = true;
		final boolean isSpecular = checkIsZero(floatRoughnessU) && checkIsZero(floatRoughnessV);
		
		if(!hasKR && !hasKT) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
//		if(isSpecular && isAllowingMultipleLobes) {
//			
//		}
		
		if(isSpecular) {
			final int count = (hasKR ? 1 : 0) + (hasKT ? 1 : 0);
			
			final int indexKR = hasKR ?           0 : -1;
			final int indexKT = hasKT ? indexKR + 1 : -1;
			
//			Initialize the BSDF:
			doBSDFClear();
			doBSDFSetBXDFCount(count);
			doBSDFSetEta(floatEta);
			doBSDFSetNegatingIncoming(false);
			
			if(hasKR) {
//				Set SpecularBRDF:
				doBSDFSetBXDFSpecularBRDFFresnelDielectric(indexKR);
				doBXDFSpecularBRDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
				doBXDFSpecularBRDFFresnelDielectricSetReflectanceScale(colorKRR, colorKRG, colorKRB);
			}
			
			if(hasKT) {
//				Set SpecularBTDF:
				doBSDFSetBXDFSpecularBTDFFresnelDielectric(indexKT);
				doBXDFSpecularBTDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
				doBXDFSpecularBTDFFresnelDielectricSetTransmittanceScale(colorKTR, colorKTG, colorKTB);
			}
			
//			Initialize the BSDFResult:
			doBSDFResultInitialize();
			
			return true;
		}
		
		final int count = (hasKR ? 1 : 0) + (hasKT ? 1 : 0);
		
		final int indexKR = hasKR ?           0 : -1;
		final int indexKT = hasKT ? indexKR + 1 : -1;
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(count);
		doBSDFSetEta(floatEta);
		doBSDFSetNegatingIncoming(false);
		
		if(hasKR) {
//			Set TorranceSparrowBRDF:
			doBSDFSetBXDFTorranceSparrowBRDFFresnelDielectric(indexKR);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(true, false, floatRoughnessURemapped, floatRoughnessVRemapped);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetReflectanceScale(colorKRR, colorKRG, colorKRB);
		}
		
		if(hasKT) {
//			Set TorranceSparrowBTDF:
			doBSDFSetBXDFTorranceSparrowBTDFFresnelDielectric(indexKT);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetFresnelDielectric(1.0F, floatEta);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(true, false, floatRoughnessURemapped, floatRoughnessVRemapped);
			doBXDFTorranceSparrowBTDFFresnelDielectricSetTransmittanceScale(colorKTR, colorKTG, colorKTB);
		}
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
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
		final float floatRoughness = textureEvaluateFloat(textureRoughnessID, textureRoughnessOffset);
		
		/*
		 * Compute the BSDF:
		 */
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(true);
		
//		Set AshikhminShirleyBRDF:
		doBSDFSetBXDFAshikhminShirleyBRDF(0);
		doBXDFAshikhminShirleyBRDFSetReflectanceScale(colorKRR, colorKRG, colorKRB);
		doBXDFAshikhminShirleyBRDFSetRoughness(floatRoughness);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
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
		final float floatAngle = textureEvaluateFloat(textureAngleID, textureAngleOffset);
		
		if(checkIsZero(colorKDR) && checkIsZero(colorKDG) && checkIsZero(colorKDB)) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
		if(checkIsZero(floatAngle)) {
//			Initialize the BSDF:
			doBSDFClear();
			doBSDFSetBXDFCount(1);
			doBSDFSetEta(1.0F);
			doBSDFSetNegatingIncoming(false);
			
//			Set LambertianBRDF:
			doBSDFSetBXDFLambertianBRDF(0);
			doBXDFLambertianBRDFSetReflectanceScale(colorKDR, colorKDG, colorKDB);
			
//			Initialize the BSDFResult:
			doBSDFResultInitialize();
			
			return true;
		}
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
//		Set OrenNayarBRDF:
		doBSDFSetBXDFOrenNayarBRDF(0);
		doBXDFOrenNayarBRDFSetAngle(floatAngle);
		doBXDFOrenNayarBRDFSetReflectanceScale(colorKDR, colorKDG, colorKDB);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialMetalMaterialComputeBSDF(final int materialMetalMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the Eta Texture:
		final int textureEta = this.materialMetalMaterialArray[materialMetalMaterialArrayOffset + MetalMaterial.ARRAY_OFFSET_TEXTURE_ETA];
		final int textureEtaID = (textureEta >>> 16) & 0xFFFF;
		final int textureEtaOffset = textureEta & 0xFFFF;
		
//		Retrieve the ID and offset for the K Texture:
		final int textureK = this.materialMetalMaterialArray[materialMetalMaterialArrayOffset + MetalMaterial.ARRAY_OFFSET_TEXTURE_K];
		final int textureKID = (textureK >>> 16) & 0xFFFF;
		final int textureKOffset = textureK & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness U Texture:
		final int textureRoughnessU = this.materialMetalMaterialArray[materialMetalMaterialArrayOffset + MetalMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U];
		final int textureRoughnessUID = (textureRoughnessU >>> 16) & 0xFFFF;
		final int textureRoughnessUOffset = textureRoughnessU & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness V Texture:
		final int textureRoughnessV = this.materialMetalMaterialArray[materialMetalMaterialArrayOffset + MetalMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V];
		final int textureRoughnessVID = (textureRoughnessV >>> 16) & 0xFFFF;
		final int textureRoughnessVOffset = textureRoughnessV & 0xFFFF;
		
//		Retrieve the roughness remapping flag:
		final boolean isRemappingRoughness = this.materialMetalMaterialArray[materialMetalMaterialArrayOffset + MetalMaterial.ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS] != 0;
		
//		Evaluate the Eta Texture:
		textureEvaluate(textureEtaID, textureEtaOffset);
		
//		Retrieve the color from the Eta Texture:
		final float colorEtaR = color3FLHSGetComponent1();
		final float colorEtaG = color3FLHSGetComponent2();
		final float colorEtaB = color3FLHSGetComponent3();
		
//		Evaluate the K Texture:
		textureEvaluate(textureKID, textureKOffset);
		
//		Retrieve the color from the K Texture:
		final float colorKR = color3FLHSGetComponent1();
		final float colorKG = color3FLHSGetComponent2();
		final float colorKB = color3FLHSGetComponent3();
		
//		Evaluate the Roughness U Texture:
		final float floatRoughnessU = textureEvaluateFloat(textureRoughnessUID, textureRoughnessUOffset);
		final float floatRoughnessURemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessU) : floatRoughnessU;
		
//		Evaluate the Roughness V Texture:
		final float floatRoughnessV = textureEvaluateFloat(textureRoughnessVID, textureRoughnessVOffset);
		final float floatRoughnessVRemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessV) : floatRoughnessV;
		
		/*
		 * Compute the BSDF:
		 */
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
//		Set TorranceSparrowBRDF:
		doBSDFSetBXDFTorranceSparrowBRDFFresnelConductor(0);
		doBXDFTorranceSparrowBRDFFresnelConductorSetReflectanceScale(1.0F, 1.0F, 1.0F);
		doBXDFTorranceSparrowBRDFFresnelConductorSetFresnelConductor(1.0F, 1.0F, 1.0F, colorEtaR, colorEtaG, colorEtaB, colorKR, colorKG, colorKB);
		doBXDFTorranceSparrowBRDFFresnelConductorSetMicrofacetDistributionTrowbridgeReitz(true, false, floatRoughnessURemapped, floatRoughnessVRemapped);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
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
		
		if(checkIsZero(colorKRR) && checkIsZero(colorKRG) && checkIsZero(colorKRB)) {
			return false;
		}
		
		/*
		 * Compute the BSDF:
		 */
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
//		Set SpecularBRDF:
		doBSDFSetBXDFSpecularBRDFFresnelConstant(0);
		doBXDFSpecularBRDFFresnelConstantSetFresnelConstant(1.0F, 1.0F, 1.0F);
		doBXDFSpecularBRDFFresnelConstantSetReflectanceScale(colorKRR, colorKRG, colorKRB);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialPlasticMaterialComputeBSDF(final int materialPlasticMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KD Texture:
		final int textureKD = this.materialPlasticMaterialArray[materialPlasticMaterialArrayOffset + PlasticMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
//		Retrieve the ID and offset for the KS Texture:
		final int textureKS = this.materialPlasticMaterialArray[materialPlasticMaterialArrayOffset + PlasticMaterial.ARRAY_OFFSET_TEXTURE_K_S];
		final int textureKSID = (textureKS >>> 16) & 0xFFFF;
		final int textureKSOffset = textureKS & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness Texture:
		final int textureRoughness = this.materialPlasticMaterialArray[materialPlasticMaterialArrayOffset + PlasticMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS];
		final int textureRoughnessID = (textureRoughness >>> 16) & 0xFFFF;
		final int textureRoughnessOffset = textureRoughness & 0xFFFF;
		
//		Retrieve the roughness remapping flag:
		final boolean isRemappingRoughness = this.materialPlasticMaterialArray[materialPlasticMaterialArrayOffset + PlasticMaterial.ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS] != 0;
		
//		Evaluate the KD Texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD Texture:
		final float colorKDR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKDG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKDB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKD = !checkIsZero(colorKDR) || !checkIsZero(colorKDG) || !checkIsZero(colorKDB);
		
//		Evaluate the KS Texture:
		textureEvaluate(textureKSID, textureKSOffset);
		
//		Retrieve the color from the KS Texture:
		final float colorKSR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKSG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKSB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKS = !checkIsZero(colorKSR) || !checkIsZero(colorKSG) || !checkIsZero(colorKSB);
		
		if(!hasKD && !hasKS) {
			return false;
		}
		
//		Evaluate the Roughness Texture:
		final float floatRoughness = textureEvaluateFloat(textureRoughnessID, textureRoughnessOffset);
		final float floatRoughnessRemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughness) : floatRoughness;
		
		/*
		 * Compute the BSDF:
		 */
		
		final int count = (hasKD ? 1 : 0) + (hasKS ? 1 : 0);
		
		final int indexKD = hasKD ?           0 : -1;
		final int indexKS = hasKS ? indexKD + 1 : -1;
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(count);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
		if(hasKD) {
//			Set LambertianBRDF:
			doBSDFSetBXDFLambertianBRDF(indexKD);
			doBXDFLambertianBRDFSetReflectanceScale(colorKDR, colorKDG, colorKDB);
		}
		
		if(hasKS) {
//			Set TorranceSparrowBRDF:
			doBSDFSetBXDFTorranceSparrowBRDFFresnelDielectric(indexKS);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetReflectanceScale(colorKSR, colorKSG, colorKSB);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetFresnelDielectric(1.5F, 1.0F);
			doBXDFTorranceSparrowBRDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(true, false, floatRoughnessRemapped, floatRoughnessRemapped);
		}
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	private boolean doMaterialSubstrateMaterialComputeBSDF(final int materialSubstrateMaterialArrayOffset) {
		/*
		 * Evaluate the Texture instances:
		 */
		
//		Retrieve the ID and offset for the KD Texture:
		final int textureKD = this.materialSubstrateMaterialArray[materialSubstrateMaterialArrayOffset + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_K_D];
		final int textureKDID = (textureKD >>> 16) & 0xFFFF;
		final int textureKDOffset = textureKD & 0xFFFF;
		
//		Retrieve the ID and offset for the KS Texture:
		final int textureKS = this.materialSubstrateMaterialArray[materialSubstrateMaterialArrayOffset + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_K_S];
		final int textureKSID = (textureKS >>> 16) & 0xFFFF;
		final int textureKSOffset = textureKS & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness U Texture:
		final int textureRoughnessU = this.materialSubstrateMaterialArray[materialSubstrateMaterialArrayOffset + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_U];
		final int textureRoughnessUID = (textureRoughnessU >>> 16) & 0xFFFF;
		final int textureRoughnessUOffset = textureRoughnessU & 0xFFFF;
		
//		Retrieve the ID and offset for the Roughness V Texture:
		final int textureRoughnessV = this.materialSubstrateMaterialArray[materialSubstrateMaterialArrayOffset + SubstrateMaterial.ARRAY_OFFSET_TEXTURE_ROUGHNESS_V];
		final int textureRoughnessVID = (textureRoughnessV >>> 16) & 0xFFFF;
		final int textureRoughnessVOffset = textureRoughnessV & 0xFFFF;
		
//		Retrieve the roughness remapping flag:
		final boolean isRemappingRoughness = this.materialSubstrateMaterialArray[materialSubstrateMaterialArrayOffset + SubstrateMaterial.ARRAY_OFFSET_IS_REMAPPING_ROUGHNESS] != 0;
		
//		Evaluate the KD Texture:
		textureEvaluate(textureKDID, textureKDOffset);
		
//		Retrieve the color from the KD Texture:
		final float colorKDR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKDG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKDB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKD = !checkIsZero(colorKDR) || !checkIsZero(colorKDG) || !checkIsZero(colorKDB);
		
//		Evaluate the KS Texture:
		textureEvaluate(textureKSID, textureKSOffset);
		
//		Retrieve the color from the KS Texture:
		final float colorKSR = saturateF(color3FLHSGetComponent1(), 0.0F, Float.MAX_VALUE);
		final float colorKSG = saturateF(color3FLHSGetComponent2(), 0.0F, Float.MAX_VALUE);
		final float colorKSB = saturateF(color3FLHSGetComponent3(), 0.0F, Float.MAX_VALUE);
		
		final boolean hasKS = !checkIsZero(colorKSR) || !checkIsZero(colorKSG) || !checkIsZero(colorKSB);
		
		if(!hasKD || !hasKS) {
			return false;
		}
		
//		Evaluate the Roughness U Texture:
		final float floatRoughnessU = textureEvaluateFloat(textureRoughnessUID, textureRoughnessUOffset);
		final float floatRoughnessURemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessU) : floatRoughnessU;
		
//		Evaluate the Roughness V Texture:
		final float floatRoughnessV = textureEvaluateFloat(textureRoughnessVID, textureRoughnessVOffset);
		final float floatRoughnessVRemapped = isRemappingRoughness ? doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(floatRoughnessV) : floatRoughnessV;
		
		/*
		 * Compute the BSDF:
		 */
		
//		Initialize the BSDF:
		doBSDFClear();
		doBSDFSetBXDFCount(1);
		doBSDFSetEta(1.0F);
		doBSDFSetNegatingIncoming(false);
		
//		Set FresnelBlendBRDF:
		doBSDFSetBXDFFresnelBlendBRDF(0);
		doBXDFFresnelBlendBRDFSetMicrofacetDistributionTrowbridgeReitz(true, false, floatRoughnessURemapped, floatRoughnessVRemapped);
		doBXDFFresnelBlendBRDFSetReflectanceScaleDiffuse(colorKDR, colorKDG, colorKDB);
		doBXDFFresnelBlendBRDFSetReflectanceScaleSpecular(colorKSR, colorKSG, colorKSB);
		
//		Initialize the BSDFResult:
		doBSDFResultInitialize();
		
		return true;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BSDF ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doBSDFIsNegatingIncoming() {
		return !checkIsZero(this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING]);
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
	
	private void doBSDFSetBXDFDisneyClearCoatBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFDisneyDiffuseBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFDisneyFakeSSBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFDisneyRetroBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_DISNEY_RETRO_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFDisneySheenBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_DISNEY_SHEEN_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFFresnelBlendBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_FRESNEL_BLEND_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFLambertianBRDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_LAMBERTIAN_B_R_D_F_ID);
	}
	
	private void doBSDFSetBXDFLambertianBTDF(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_LAMBERTIAN_B_T_D_F_ID);
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
	
	private void doBSDFSetBXDFSpecularBTDFFresnelDielectric(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ID);
	}
	
	private void doBSDFSetBXDFTorranceSparrowBRDFFresnelConductor(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ID);
	}
	
	private void doBSDFSetBXDFTorranceSparrowBRDFFresnelDielectric(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ID);
	}
	
	private void doBSDFSetBXDFTorranceSparrowBRDFFresnelDisney(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ID);
	}
	
	private void doBSDFSetBXDFTorranceSparrowBTDFFresnelDielectric(final int index) {
		doBSDFSetBXDF(index, B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ID);
	}
	
	private void doBSDFSetEta(final float eta) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_ETA] = eta;
	}
	
	private void doBSDFSetNegatingIncoming(final boolean isNegatingIncoming) {
		this.bSDFArray_$private$11[B_S_D_F_ARRAY_OFFSET_IS_NEGATING_INCOMING] = isNegatingIncoming ? 1.0F : 0.0F;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BSDFResult //////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doBSDFResultGetNormalX() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0];
	}
	
	private float doBSDFResultGetNormalY() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1];
	}
	
	private float doBSDFResultGetNormalZ() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2];
	}
	
	private float doBSDFResultGetOutgoingX() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0];
	}
	
	private float doBSDFResultGetOutgoingY() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1];
	}
	
	private float doBSDFResultGetOutgoingZ() {
		return this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2];
	}
	
	private void doBSDFResultInitialize() {
		doBSDFResultSetNormalFromIntersection();
		doBSDFResultSetOutgoingFromRay();
		
		orthonormalBasis33FSetIntersectionOrthonormalBasisS();
		
		doBXDFResultSetNormalTransformedFromBSDFResult();
		doBXDFResultSetOutgoingTransformedFromBSDFResult();
	}
	
	private void doBSDFResultSetBXDFID(final int id) {
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_B_X_D_F_ID] = id;
	}
	
	private void doBSDFResultSetIncoming(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 0] = x;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 1] = y;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_INCOMING + 2] = z;
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
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 0] = x;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 1] = y;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_NORMAL + 2] = z;
	}
	
	private void doBSDFResultSetNormalFromIntersection() {
		final float x = intersectionGetOrthonormalBasisSWComponent1();
		final float y = intersectionGetOrthonormalBasisSWComponent2();
		final float z = intersectionGetOrthonormalBasisSWComponent3();
		
		doBSDFResultSetNormal(x, y, z);
	}
	
	private void doBSDFResultSetOutgoing(final float x, final float y, final float z) {
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 0] = x;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 1] = y;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_OUTGOING + 2] = z;
	}
	
	private void doBSDFResultSetOutgoingFromRay() {
		final float x = -ray3FGetDirectionComponent1();
		final float y = -ray3FGetDirectionComponent2();
		final float z = -ray3FGetDirectionComponent3();
		
		doBSDFResultSetOutgoing(x, y, z);
	}
	
	private void doBSDFResultSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doBSDFResultSetResult(final float r, final float g, final float b) {
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 0] = r;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 1] = g;
		this.bSDFResultArray_$private$14[B_S_D_F_RESULT_ARRAY_OFFSET_RESULT + 2] = b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF ////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFHasReflection(final int id) {
		return ((id & 0xFFFF) & B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION) == B_X_D_F_TYPE_BIT_FLAG_HAS_REFLECTION;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFHasTransmission(final int id) {
		return ((id & 0xFFFF) & B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION) == B_X_D_F_TYPE_BIT_FLAG_HAS_TRANSMISSION;
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFIsMatchingBitFlags(final int id, final int bitFlags) {
		return ((id & 0xFFFF) & bitFlags) == (id & 0xFFFF);
	}
	
	@SuppressWarnings("static-method")
	private boolean doBXDFIsSpecular(final int id) {
		return ((id & 0xFFFF) & B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR) == B_X_D_F_TYPE_BIT_FLAG_IS_SPECULAR;
	}
	
	private boolean doBXDFSampleDistributionFunction(final float u, final float v, final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			return doBXDFAshikhminShirleyBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFDisneyClearCoatBRDFIsMatchingID(id)) {
			return doBXDFDisneyClearCoatBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFDisneyDiffuseBRDFIsMatchingID(id)) {
			return doBXDFDisneyDiffuseBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFDisneyFakeSSBRDFIsMatchingID(id)) {
			return doBXDFDisneyFakeSSBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFDisneyRetroBRDFIsMatchingID(id)) {
			return doBXDFDisneyRetroBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFDisneySheenBRDFIsMatchingID(id)) {
			return doBXDFDisneySheenBRDFSampleDistributionFunction(u, v);
		} else if(doBXDFFresnelBlendBRDFIsMatchingID(id)) {
			return doBXDFFresnelBlendBRDFSampleDistributionFunction(u, v);
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
		} else if(doBXDFSpecularBTDFFresnelDielectricIsMatchingID(id)) {
			return doBXDFSpecularBTDFFresnelDielectricSampleDistributionFunction(u, v);
		} else if(doBXDFTorranceSparrowBRDFFresnelConductorIsMatchingID(id)) {
			return doBXDFTorranceSparrowBRDFFresnelConductorSampleDistributionFunction(u, v);
		} else if(doBXDFTorranceSparrowBRDFFresnelDielectricIsMatchingID(id)) {
			return doBXDFTorranceSparrowBRDFFresnelDielectricSampleDistributionFunction(u, v);
		} else if(doBXDFTorranceSparrowBRDFFresnelDisneyIsMatchingID(id)) {
			return doBXDFTorranceSparrowBRDFFresnelDisneySampleDistributionFunction(u, v);
		} else if(doBXDFTorranceSparrowBTDFFresnelDielectricIsMatchingID(id)) {
			return doBXDFTorranceSparrowBTDFFresnelDielectricSampleDistributionFunction(u, v);
		} else {
			return false;
		}
	}
	
	private void doBXDFEvaluateDistributionFunction(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			doBXDFAshikhminShirleyBRDFEvaluateDistributionFunction();
		} else if(doBXDFDisneyClearCoatBRDFIsMatchingID(id)) {
			doBXDFDisneyClearCoatBRDFEvaluateDistributionFunction();
		} else if(doBXDFDisneyDiffuseBRDFIsMatchingID(id)) {
			doBXDFDisneyDiffuseBRDFEvaluateDistributionFunction();
		} else if(doBXDFDisneyFakeSSBRDFIsMatchingID(id)) {
			doBXDFDisneyFakeSSBRDFEvaluateDistributionFunction();
		} else if(doBXDFDisneyRetroBRDFIsMatchingID(id)) {
			doBXDFDisneyRetroBRDFEvaluateDistributionFunction();
		} else if(doBXDFDisneySheenBRDFIsMatchingID(id)) {
			doBXDFDisneySheenBRDFEvaluateDistributionFunction();
		} else if(doBXDFFresnelBlendBRDFIsMatchingID(id)) {
			doBXDFFresnelBlendBRDFEvaluateDistributionFunction();
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
		} else if(doBXDFSpecularBTDFFresnelDielectricIsMatchingID(id)) {
			doBXDFSpecularBTDFFresnelDielectricEvaluateDistributionFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelConductorIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelConductorEvaluateDistributionFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelDielectricIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateDistributionFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelDisneyIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateDistributionFunction();
		} else if(doBXDFTorranceSparrowBTDFFresnelDielectricIsMatchingID(id)) {
			doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateDistributionFunction();
		}
	}
	
	private void doBXDFEvaluateProbabilityDensityFunction(final int id) {
		if(doBXDFAshikhminShirleyBRDFIsMatchingID(id)) {
			doBXDFAshikhminShirleyBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFDisneyClearCoatBRDFIsMatchingID(id)) {
			doBXDFDisneyClearCoatBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFDisneyDiffuseBRDFIsMatchingID(id)) {
			doBXDFDisneyDiffuseBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFDisneyFakeSSBRDFIsMatchingID(id)) {
			doBXDFDisneyFakeSSBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFDisneyRetroBRDFIsMatchingID(id)) {
			doBXDFDisneyRetroBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFDisneySheenBRDFIsMatchingID(id)) {
			doBXDFDisneySheenBRDFEvaluateProbabilityDensityFunction();
		} else if(doBXDFFresnelBlendBRDFIsMatchingID(id)) {
			doBXDFFresnelBlendBRDFEvaluateProbabilityDensityFunction();
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
		} else if(doBXDFSpecularBTDFFresnelDielectricIsMatchingID(id)) {
			doBXDFSpecularBTDFFresnelDielectricEvaluateProbabilityDensityFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelConductorIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelConductorEvaluateProbabilityDensityFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelDielectricIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateProbabilityDensityFunction();
		} else if(doBXDFTorranceSparrowBRDFFresnelDisneyIsMatchingID(id)) {
			doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateProbabilityDensityFunction();
		} else if(doBXDFTorranceSparrowBTDFFresnelDielectricIsMatchingID(id)) {
			doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateProbabilityDensityFunction();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - AshikhminShirleyBRDF /////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		if(checkIsZero(outgoingZ)) {
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
		
		vector3FSetSpecularReflectionFacingSurface(outgoingX, outgoingY, outgoingZ, normalTransformedX, normalTransformedY, normalTransformedZ);
		
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
		
		if(checkIsZero(cosThetaOutgoingAbs) || checkIsZero(cosThetaIncomingAbs)) {
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
		
		if(checkIsZero(normalNormalizedX) && checkIsZero(normalNormalizedY) && checkIsZero(normalNormalizedZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float exponent = doBXDFAshikhminShirleyBRDFGetExponent();
		
		final float outgoingDotNormalNormalized = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float d = (exponent + 1.0F) * pow(normalNormalizedCosThetaAbs, exponent) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float f = doSchlickFresnelDielectric1(outgoingDotNormalNormalized, 1.0F);
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
	
	private void doBXDFAshikhminShirleyBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	private void doBXDFAshikhminShirleyBRDFSetRoughness(final float roughness) {
		this.bXDFAshikhminShirleyBRDFArray_$private$4[B_X_D_F_ASHIKHMIN_SHIRLEY_B_R_D_F_ARRAY_OFFSET_EXPONENT] = 1.0F / (roughness * roughness);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - DisneyClearCoatBRDF //////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFDisneyClearCoatBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ID;
	}
	
	private boolean doBXDFDisneyClearCoatBRDFSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(checkIsZero(outgoingZ)) {
			return false;
		}
		
		final float gloss = doBXDFDisneyClearCoatBRDFGetGloss();
		final float glossSquared = gloss * gloss;
		
		final float cosTheta = sqrt(max(0.0F, (1.0F - pow(glossSquared, 1.0F - u)) / (1.0F - glossSquared)));
		final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
		final float phi = PI_MULTIPLIED_BY_2 * v;
		
		final float normalSampleX = sinTheta * cos(phi);
		final float normalSampleY = sinTheta * sin(phi);
		final float normalSampleZ = cosTheta;
		
		final boolean isSameHemisphereZ = vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, normalSampleX, normalSampleY, normalSampleZ);
		
		final float normalX = isSameHemisphereZ ? normalSampleX : -normalSampleX;
		final float normalY = isSameHemisphereZ ? normalSampleY : -normalSampleY;
		final float normalZ = isSameHemisphereZ ? normalSampleZ : -normalSampleZ;
		
		final float outgoingDotNormal = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		final float outgoingDotNormalMultipliedByTwo = outgoingDotNormal * 2.0F;
		
		final float incomingX = normalX * outgoingDotNormalMultipliedByTwo - outgoingX;
		final float incomingY = normalY * outgoingDotNormalMultipliedByTwo - outgoingY;
		final float incomingZ = normalZ * outgoingDotNormalMultipliedByTwo - outgoingZ;
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			return false;
		}
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFDisneyClearCoatBRDFEvaluateDistributionFunction();
		doBXDFDisneyClearCoatBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFDisneyClearCoatBRDFGTR1(final float cosTheta, final float alpha) {
		return (alpha * alpha - 1.0F) / (PI * log(alpha * alpha) * (1.0F + (alpha * alpha - 1.0F) * cosTheta * cosTheta));
	}
	
	private float doBXDFDisneyClearCoatBRDFGetGloss() {
		return this.bXDFDisneyClearCoatBRDFArray_$private$2[B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_GLOSS];
	}
	
	private float doBXDFDisneyClearCoatBRDFGetWeight() {
		return this.bXDFDisneyClearCoatBRDFArray_$private$2[B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_WEIGHT];
	}
	
	private float doBXDFDisneyClearCoatBRDFSmithGGGX(final float cosTheta, final float alpha) {
		return 1.0F / (cosTheta + sqrt(alpha * alpha + cosTheta * cosTheta - alpha * alpha * cosTheta * cosTheta));
	}
	
	private void doBXDFDisneyClearCoatBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float normalX = incomingX + outgoingX;
		final float normalY = incomingY + outgoingY;
		final float normalZ = incomingZ + outgoingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float gloss = doBXDFDisneyClearCoatBRDFGetGloss();
		final float weight = doBXDFDisneyClearCoatBRDFGetWeight();
		
		final float cosThetaAbsNormalNormalized = vector3FCosThetaAbs(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		final float outgoingDotNormalNormalized = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float d = doBXDFDisneyClearCoatBRDFGTR1(cosThetaAbsNormalNormalized, gloss);
		final float f = doSchlickFresnelWeightLerp(outgoingDotNormalNormalized, 0.04F);
		final float g = doBXDFDisneyClearCoatBRDFSmithGGGX(cosThetaAbsOutgoing, 0.25F) * doBXDFDisneyClearCoatBRDFSmithGGGX(cosThetaAbsIncoming, 0.25F);
		
		final float result = weight * g * f * d / 4.0F;
		
		doBXDFResultSetResult(result, result, result);
	}
	
	private void doBXDFDisneyClearCoatBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = incomingX + outgoingX;
		final float normalY = incomingY + outgoingY;
		final float normalZ = incomingZ + outgoingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float gloss = doBXDFDisneyClearCoatBRDFGetGloss();
		
		final float cosThetaAbsNormalNormalized = vector3FCosThetaAbs(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float outgoingDotNormalNormalized = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float d = doBXDFDisneyClearCoatBRDFGTR1(cosThetaAbsNormalNormalized, gloss);
		
		final float probabilityDensityFunctionValue = d * cosThetaAbsNormalNormalized / (4.0F * outgoingDotNormalNormalized);
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFDisneyClearCoatBRDFSetGloss(final float gloss) {
		this.bXDFDisneyClearCoatBRDFArray_$private$2[B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_GLOSS] = gloss;
	}
	
	private void doBXDFDisneyClearCoatBRDFSetWeight(final float weight) {
		this.bXDFDisneyClearCoatBRDFArray_$private$2[B_X_D_F_DISNEY_CLEAR_COAT_B_R_D_F_ARRAY_OFFSET_WEIGHT] = weight;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - DisneyDiffuseBRDF ////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFDisneyDiffuseBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ID;
	}
	
	private boolean doBXDFDisneyDiffuseBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFDisneyDiffuseBRDFEvaluateDistributionFunction();
		doBXDFDisneyDiffuseBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFDisneyDiffuseBRDFGetReflectanceScaleB() {
		return this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFDisneyDiffuseBRDFGetReflectanceScaleG() {
		return this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFDisneyDiffuseBRDFGetReflectanceScaleR() {
		return this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFDisneyDiffuseBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		final float fresnelOutgoing = doSchlickFresnelWeight(cosThetaAbsOutgoing);
		final float fresnelIncoming = doSchlickFresnelWeight(cosThetaAbsIncoming);
		
		final float a = 1.0F - fresnelOutgoing / 2.0F;
		final float b = 1.0F - fresnelIncoming / 2.0F;
		
		final float reflectanceScaleR = doBXDFDisneyDiffuseBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFDisneyDiffuseBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFDisneyDiffuseBRDFGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * PI_RECIPROCAL * a * b;
		final float resultG = reflectanceScaleG * PI_RECIPROCAL * a * b;
		final float resultB = reflectanceScaleB * PI_RECIPROCAL * a * b;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFDisneyDiffuseBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFDisneyDiffuseBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFDisneyDiffuseBRDFArray_$private$3[B_X_D_F_DISNEY_DIFFUSE_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - DisneyFakeSSBRDF /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFDisneyFakeSSBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ID;
	}
	
	private boolean doBXDFDisneyFakeSSBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFDisneyFakeSSBRDFEvaluateDistributionFunction();
		doBXDFDisneyFakeSSBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFDisneyFakeSSBRDFGetReflectanceScaleB() {
		return this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFDisneyFakeSSBRDFGetReflectanceScaleG() {
		return this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFDisneyFakeSSBRDFGetReflectanceScaleR() {
		return this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private float doBXDFDisneyFakeSSBRDFGetRoughness() {
		return this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_ROUGHNESS];
	}
	
	private void doBXDFDisneyFakeSSBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float normalX = incomingX + outgoingX;
		final float normalY = incomingY + outgoingY;
		final float normalZ = incomingZ + outgoingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float cosThetaD = vector3FDotProduct(incomingX, incomingY, incomingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		final float reflectanceScaleR = doBXDFDisneyFakeSSBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFDisneyFakeSSBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFDisneyFakeSSBRDFGetReflectanceScaleB();
		
		final float roughness = doBXDFDisneyFakeSSBRDFGetRoughness();
		
		final float fresnelOutgoing = doSchlickFresnelWeight(cosThetaAbsOutgoing);
		final float fresnelIncoming = doSchlickFresnelWeight(cosThetaAbsIncoming);
		final float fresnelSS90 = cosThetaD * cosThetaD * roughness;
		final float fresnelSS = lerp(1.0F, fresnelSS90, fresnelOutgoing) * lerp(1.0F, fresnelSS90, fresnelIncoming);
		
		final float scaleSS = 1.25F * (fresnelSS * (1.0F / (cosThetaAbsOutgoing + cosThetaAbsIncoming) - 0.5F) + 0.5F);
		
		final float resultR = reflectanceScaleR * PI_RECIPROCAL * scaleSS;
		final float resultG = reflectanceScaleG * PI_RECIPROCAL * scaleSS;
		final float resultB = reflectanceScaleB * PI_RECIPROCAL * scaleSS;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFDisneyFakeSSBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFDisneyFakeSSBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	private void doBXDFDisneyFakeSSBRDFSetRoughness(final float roughness) {
		this.bXDFDisneyFakeSSBRDFArray_$private$4[B_X_D_F_DISNEY_FAKE_S_S_B_R_D_F_ARRAY_OFFSET_ROUGHNESS] = roughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - DisneyRetroBRDF //////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFDisneyRetroBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_DISNEY_RETRO_B_R_D_F_ID;
	}
	
	private boolean doBXDFDisneyRetroBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFDisneyRetroBRDFEvaluateDistributionFunction();
		doBXDFDisneyRetroBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFDisneyRetroBRDFGetReflectanceScaleB() {
		return this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFDisneyRetroBRDFGetReflectanceScaleG() {
		return this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFDisneyRetroBRDFGetReflectanceScaleR() {
		return this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private float doBXDFDisneyRetroBRDFGetRoughness() {
		return this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_ROUGHNESS];
	}
	
	private void doBXDFDisneyRetroBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float normalX = incomingX + outgoingX;
		final float normalY = incomingY + outgoingY;
		final float normalZ = incomingZ + outgoingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float cosThetaD = vector3FDotProduct(incomingX, incomingY, incomingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		final float reflectanceScaleR = doBXDFDisneyRetroBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFDisneyRetroBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFDisneyRetroBRDFGetReflectanceScaleB();
		
		final float roughness = doBXDFDisneyRetroBRDFGetRoughness();
		
		final float fresnelOutgoing = doSchlickFresnelWeight(cosThetaAbsOutgoing);
		final float fresnelIncoming = doSchlickFresnelWeight(cosThetaAbsIncoming);
		
		final float a = 2.0F * roughness * cosThetaD * cosThetaD;
		final float b = fresnelOutgoing + fresnelIncoming + fresnelOutgoing * fresnelIncoming * (a - 1.0F);
		
		final float resultR = reflectanceScaleR * PI_RECIPROCAL * a * b;
		final float resultG = reflectanceScaleG * PI_RECIPROCAL * a * b;
		final float resultB = reflectanceScaleB * PI_RECIPROCAL * a * b;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFDisneyRetroBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFDisneyRetroBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	private void doBXDFDisneyRetroBRDFSetRoughness(final float roughness) {
		this.bXDFDisneyRetroBRDFArray_$private$4[B_X_D_F_DISNEY_RETRO_B_R_D_F_ARRAY_OFFSET_ROUGHNESS] = roughness;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - DisneySheenBRDF //////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFDisneySheenBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_DISNEY_SHEEN_B_R_D_F_ID;
	}
	
	private boolean doBXDFDisneySheenBRDFSampleDistributionFunction(final float u, final float v) {
		vector3FSetSampleHemisphereCosineDistribution(u, v);
		vector3FSetFaceForwardRHSComponent3(doBXDFResultGetOutgoingX(), doBXDFResultGetOutgoingY(), doBXDFResultGetOutgoingZ(), vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		doBXDFDisneySheenBRDFEvaluateDistributionFunction();
		doBXDFDisneySheenBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFDisneySheenBRDFGetReflectanceScaleB() {
		return this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFDisneySheenBRDFGetReflectanceScaleG() {
		return this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFDisneySheenBRDFGetReflectanceScaleR() {
		return this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFDisneySheenBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float normalX = incomingX + outgoingX;
		final float normalY = incomingY + outgoingY;
		final float normalZ = incomingZ + outgoingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float cosThetaD = vector3FDotProduct(incomingX, incomingY, incomingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float fresnel = doSchlickFresnelWeight(cosThetaD);
		
		final float reflectanceScaleR = doBXDFDisneySheenBRDFGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFDisneySheenBRDFGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFDisneySheenBRDFGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * fresnel;
		final float resultG = reflectanceScaleG * fresnel;
		final float resultB = reflectanceScaleB * fresnel;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFDisneySheenBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float probabilityDensityFunctionValue = outgoingZ * incomingZ > 0.0F ? vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL : 0.0F;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFDisneySheenBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFDisneySheenBRDFArray_$private$3[B_X_D_F_DISNEY_SHEEN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - FresnelBlendBRDF /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFFresnelBlendBRDFIsMatchingID(final int id) {
		return id == B_X_D_F_FRESNEL_BLEND_B_R_D_F_ID;
	}
	
	private boolean doBXDFFresnelBlendBRDFIsSamplingVisibleArea() {
		return !checkIsZero(this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doBXDFFresnelBlendBRDFIsSeparableModel() {
		return !checkIsZero(this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private boolean doBXDFFresnelBlendBRDFSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(u < 0.5F) {
			final float uRemapped = min(2.0F * u, 0.99999994F);
			final float vRemapped = v;
			
			vector3FSetSampleHemisphereCosineDistribution(uRemapped, vRemapped);
			vector3FSetFaceForwardRHSComponent3(outgoingX, outgoingY, outgoingZ, vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
			
			doBXDFResultSetIncoming(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
			doBXDFFresnelBlendBRDFEvaluateDistributionFunction();
			doBXDFFresnelBlendBRDFEvaluateProbabilityDensityFunction();
			
			return true;
		}
		
		final float uRemapped = min(2.0F * (u - 0.5F), 0.99999994F);
		final float vRemapped = v;
		
		doBXDFFresnelBlendBRDFInitializeMicrofacetDistributionTrowbridgeReitz();
		
		doMicrofacetDistributionTrowbridgeReitzSampleNormal(outgoingX, outgoingY, outgoingZ, uRemapped, vRemapped);
		
		vector3FSetSpecularReflection(outgoingX, outgoingY, outgoingZ, vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			return false;
		}
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFFresnelBlendBRDFEvaluateDistributionFunction();
		doBXDFFresnelBlendBRDFEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFFresnelBlendBRDFGetAlphaX() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doBXDFFresnelBlendBRDFGetAlphaY() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseB() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 2];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseG() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 1];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseR() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 0];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularB() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 2];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularG() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 1];
	}
	
	private float doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularR() {
		return this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 0];
	}
	
	private void doBXDFFresnelBlendBRDFEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		doBXDFFresnelBlendBRDFInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		final float incomingDotNormal = vector3FDotProduct(incomingX, incomingY, incomingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		final float a = 28.0F / (23.0F * PI);
		final float b = 1.0F - pow5(1.0F - 0.5F * cosThetaAbsIncoming);
		final float c = 1.0F - pow5(1.0F - 0.5F * cosThetaAbsOutgoing);
		final float d = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float e = 4.0F * abs(incomingDotNormal) * max(cosThetaAbsIncoming, cosThetaAbsOutgoing);
		final float f = d / e;
		
		final float reflectanceScaleDiffuseR = doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseR();
		final float reflectanceScaleDiffuseG = doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseG();
		final float reflectanceScaleDiffuseB = doBXDFFresnelBlendBRDFGetReflectanceScaleDiffuseB();
		
		final float reflectanceScaleSpecularR = doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularR();
		final float reflectanceScaleSpecularG = doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularG();
		final float reflectanceScaleSpecularB = doBXDFFresnelBlendBRDFGetReflectanceScaleSpecularB();
		
		doSchlickFresnelDielectric3(incomingDotNormal, reflectanceScaleSpecularR, reflectanceScaleSpecularG, reflectanceScaleSpecularB);
		
		final float fresnelR = color3FLHSGetComponent1();
		final float fresnelG = color3FLHSGetComponent2();
		final float fresnelB = color3FLHSGetComponent3();
		
		final float resultR = reflectanceScaleDiffuseR * a * (1.0F - reflectanceScaleSpecularR) * b * c + fresnelR * f;
		final float resultG = reflectanceScaleDiffuseG * a * (1.0F - reflectanceScaleSpecularG) * b * c + fresnelG * f;
		final float resultB = reflectanceScaleDiffuseB * a * (1.0F - reflectanceScaleSpecularB) * b * c + fresnelB * f;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFFresnelBlendBRDFEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float outgoingDotNormal = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		doBXDFFresnelBlendBRDFInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float probabilityDensityFunctionValue0 = doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float probabilityDensityFunctionValue1 = 0.5F * (vector3FCosThetaAbs(incomingX, incomingY, incomingZ) * PI_RECIPROCAL + probabilityDensityFunctionValue0 / (4.0F * outgoingDotNormal));
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue1);
	}
	
	private void doBXDFFresnelBlendBRDFInitializeMicrofacetDistributionTrowbridgeReitz() {
		final boolean isSamplingVisibleArea = doBXDFFresnelBlendBRDFIsSamplingVisibleArea();
		final boolean isSeparableModel = doBXDFFresnelBlendBRDFIsSeparableModel();
		
		final float alphaX = doBXDFFresnelBlendBRDFGetAlphaX();
		final float alphaY = doBXDFFresnelBlendBRDFGetAlphaY();
		
		doMicrofacetDistributionTrowbridgeReitzSet(isSamplingVisibleArea, isSeparableModel, alphaX, alphaY);
	}
	
	private void doBXDFFresnelBlendBRDFSetMicrofacetDistributionTrowbridgeReitz(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	private void doBXDFFresnelBlendBRDFSetReflectanceScaleDiffuse(final float reflectanceScaleDiffuseR, final float reflectanceScaleDiffuseG, final float reflectanceScaleDiffuseB) {
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 0] = reflectanceScaleDiffuseR;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 1] = reflectanceScaleDiffuseG;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_DIFFUSE + 2] = reflectanceScaleDiffuseB;
	}
	
	private void doBXDFFresnelBlendBRDFSetReflectanceScaleSpecular(final float reflectanceScaleSpecularR, final float reflectanceScaleSpecularG, final float reflectanceScaleSpecularB) {
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 0] = reflectanceScaleSpecularR;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 1] = reflectanceScaleSpecularG;
		this.bXDFFresnelBlendBRDFArray_$private$10[B_X_D_F_FRESNEL_BLEND_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE_SPECULAR + 2] = reflectanceScaleSpecularB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - LambertianBRDF ///////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private void doBXDFLambertianBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFLambertianBRDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - LambertianBTDF ///////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private void doBXDFLambertianBTDFSetTransmittanceScale(final float transmittanceScaleR, final float transmittanceScaleG, final float transmittanceScaleB) {
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0] = transmittanceScaleR;
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1] = transmittanceScaleG;
		this.bXDFLambertianBTDFArray_$private$3[B_X_D_F_LAMBERTIAN_B_T_D_F_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2] = transmittanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - OrenNayarBRDF ////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private void doBXDFOrenNayarBRDFSetAngle(final float angleDegrees) {
		final float angleRadians = toRadians(angleDegrees);
		final float angleRadiansSquared = angleRadians * angleRadians;
		
		final float a = 1.0F - (angleRadiansSquared / (2.0F * (angleRadiansSquared + 0.33F)));
		final float b = 0.45F * angleRadiansSquared / (angleRadiansSquared + 0.09F);
		
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_ANGLE_RADIANS] = angleRadians;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_A] = a;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_B] = b;
	}
	
	private void doBXDFOrenNayarBRDFSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFOrenNayarBRDFArray_$private$6[B_X_D_F_OREN_NAYAR_B_R_D_F_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - SpecularBRDF - ConstantFresnel ///////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	
	private void doBXDFSpecularBRDFFresnelConstantSetFresnelConstant(final float lightR, final float lightG, final float lightB) {
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 0] = lightR;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 1] = lightG;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_LIGHT + 2] = lightB;
	}
	
	private void doBXDFSpecularBRDFFresnelConstantSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFSpecularBRDFFresnelConstantArray_$private$6[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_CONSTANT_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - SpecularBRDF - DielectricFresnel /////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		
		final float reflectance = doFresnelDielectric(cosThetaI, etaI, etaT);
		
		color3FLHSSet(reflectance, reflectance, reflectance);
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricEvaluateProbabilityDensityFunction() {
		doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricSetFresnelDielectric(final float etaI, final float etaT) {
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I] = etaI;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T] = etaT;
	}
	
	private void doBXDFSpecularBRDFFresnelDielectricSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFSpecularBRDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - SpecularBTDF - DielectricFresnel /////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFSpecularBTDFFresnelDielectricIsMatchingID(final int id) {
		return id == B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ID;
	}
	
	@SuppressWarnings("unused")
	private boolean doBXDFSpecularBTDFFresnelDielectricSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final boolean isEntering = vector3FCosTheta(outgoingX, outgoingY, outgoingZ) > 0.0F;
		
		final float etaA = doBXDFSpecularBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFSpecularBTDFFresnelDielectricGetEtaB();
		final float etaI = isEntering ? etaA : etaB;
		final float etaT = isEntering ? etaB : etaA;
		final float eta = (etaI * etaI) / (etaT * etaT);
		
		if(!vector3FSetRefraction(outgoingX, outgoingY, outgoingZ, 0.0F, 0.0F, outgoingZ < 0.0F ? -1.0F : 1.0F, etaI / etaT)) {
			return false;
		}
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		final float cosTheta = vector3FCosTheta(incomingX, incomingY, incomingZ);
		final float cosThetaAbs = abs(cosTheta);
		
		doBXDFSpecularBTDFFresnelDielectricEvaluateFresnel(cosTheta);
		
		final float fresnelR = 1.0F - color3FLHSGetComponent1();
		final float fresnelG = 1.0F - color3FLHSGetComponent2();
		final float fresnelB = 1.0F - color3FLHSGetComponent3();
		
		final float transmittanceScaleR = doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleR();
		final float transmittanceScaleG = doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleG();
		final float transmittanceScaleB = doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleB();
		
		final float resultR = transmittanceScaleR * fresnelR * eta / cosThetaAbs;
		final float resultG = transmittanceScaleG * fresnelG * eta / cosThetaAbs;
		final float resultB = transmittanceScaleB * fresnelB * eta / cosThetaAbs;
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFResultSetProbabilityDensityFunctionValue(1.0F);
		doBXDFResultSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	private float doBXDFSpecularBTDFFresnelDielectricGetEtaA() {
		return this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A];
	}
	
	private float doBXDFSpecularBTDFFresnelDielectricGetEtaB() {
		return this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B];
	}
	
	private float doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleB() {
		return this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2];
	}
	
	private float doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleG() {
		return this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1];
	}
	
	private float doBXDFSpecularBTDFFresnelDielectricGetTransmittanceScaleR() {
		return this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0];
	}
	
	private void doBXDFSpecularBTDFFresnelDielectricEvaluateDistributionFunction() {
		doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
	}
	
	private void doBXDFSpecularBTDFFresnelDielectricEvaluateFresnel(final float cosThetaI) {
		final float etaA = doBXDFSpecularBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFSpecularBTDFFresnelDielectricGetEtaB();
		
		final float reflectance = doFresnelDielectric(cosThetaI, etaA, etaB);
		
		color3FLHSSet(reflectance, reflectance, reflectance);
	}
	
	private void doBXDFSpecularBTDFFresnelDielectricEvaluateProbabilityDensityFunction() {
		doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
	}
	
	private void doBXDFSpecularBTDFFresnelDielectricSetFresnelDielectric(final float etaA, final float etaB) {
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A] = etaA;
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B] = etaB;
	}
	
	private void doBXDFSpecularBTDFFresnelDielectricSetTransmittanceScale(final float transmittanceScaleR, final float transmittanceScaleG, final float transmittanceScaleB) {
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0] = transmittanceScaleR;
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1] = transmittanceScaleG;
		this.bXDFSpecularBTDFFresnelDielectricArray_$private$5[B_X_D_F_SPECULAR_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2] = transmittanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - TorranceSparrowBRDF - ConductorFresnel ///////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFTorranceSparrowBRDFFresnelConductorIsMatchingID(final int id) {
		return id == B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ID;
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelConductorIsSamplingVisibleArea() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelConductorIsSeparableModel() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelConductorSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(checkIsZero(outgoingZ)) {
			return false;
		}
		
		doBXDFTorranceSparrowBRDFFresnelConductorInitializeMicrofacetDistributionTrowbridgeReitz();
		
		doMicrofacetDistributionTrowbridgeReitzSampleNormal(outgoingX, outgoingY, outgoingZ, u, v);
		
		final float normalX = vector3FGetComponent1();
		final float normalY = vector3FGetComponent2();
		final float normalZ = vector3FGetComponent3();
		
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ) < 0.0F) {
			return false;
		}
		
		vector3FSetSpecularReflection(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			return false;
		}
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFTorranceSparrowBRDFFresnelConductorEvaluateDistributionFunction();
		doBXDFTorranceSparrowBRDFFresnelConductorEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetAlphaX() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetAlphaY() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIB() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIG() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIR() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 0];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTB() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTG() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTR() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 0];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetKB() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetKG() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetKR() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 0];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleB() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleG() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleR() {
		return this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		if(checkIsZero(cosThetaAbsOutgoing) || checkIsZero(cosThetaAbsIncoming)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalCorrectlyOrientedX = normalNormalizedZ < 0.0F ? -normalNormalizedX : normalNormalizedX;
		final float normalCorrectlyOrientedY = normalNormalizedZ < 0.0F ? -normalNormalizedY : normalNormalizedY;
		final float normalCorrectlyOrientedZ = normalNormalizedZ < 0.0F ? -normalNormalizedZ : normalNormalizedZ;
		
		doBXDFTorranceSparrowBRDFFresnelConductorInitializeMicrofacetDistributionTrowbridgeReitz();
		doBXDFTorranceSparrowBRDFFresnelConductorEvaluateFresnel(vector3FDotProduct(incomingX, incomingY, incomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
		
		final float fresnelR = color3FLHSGetComponent1();
		final float fresnelG = color3FLHSGetComponent2();
		final float fresnelB = color3FLHSGetComponent3();
		
		final float d = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float g = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking2(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ);
		final float h = 1.0F / (4.0F * cosThetaAbsIncoming * cosThetaAbsOutgoing);
		
		final float reflectanceScaleR = doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFTorranceSparrowBRDFFresnelConductorGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * d * g * fresnelR * h;
		final float resultG = reflectanceScaleG * d * g * fresnelG * h;
		final float resultB = reflectanceScaleB * d * g * fresnelB * h;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorEvaluateFresnel(final float cosThetaI) {
		final float etaIR = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIR();
		final float etaIG = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIG();
		final float etaIB = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaIB();
		final float etaTR = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTR();
		final float etaTG = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTG();
		final float etaTB = doBXDFTorranceSparrowBRDFFresnelConductorGetEtaTB();
		final float kR = doBXDFTorranceSparrowBRDFFresnelConductorGetKR();
		final float kG = doBXDFTorranceSparrowBRDFFresnelConductorGetKG();
		final float kB = doBXDFTorranceSparrowBRDFFresnelConductorGetKB();
		
		doFresnelConductor(abs(cosThetaI), etaIR, etaIG, etaIB, etaTR, etaTG, etaTB, kR, kG, kB);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		doBXDFTorranceSparrowBRDFFresnelConductorInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float probabilityDensityFunctionValue0 = doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float probabilityDensityFunctionValue1 = probabilityDensityFunctionValue0 / (4.0F * vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ));
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue1);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorInitializeMicrofacetDistributionTrowbridgeReitz() {
		final boolean isSamplingVisibleArea = doBXDFTorranceSparrowBRDFFresnelConductorIsSamplingVisibleArea();
		final boolean isSeparableModel = doBXDFTorranceSparrowBRDFFresnelConductorIsSeparableModel();
		
		final float alphaX = doBXDFTorranceSparrowBRDFFresnelConductorGetAlphaX();
		final float alphaY = doBXDFTorranceSparrowBRDFFresnelConductorGetAlphaY();
		
		doMicrofacetDistributionTrowbridgeReitzSet(isSamplingVisibleArea, isSeparableModel, alphaX, alphaY);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorSetFresnelConductor(final float etaIR, final float etaIG, final float etaIB, final float etaTR, final float etaTG, final float etaTB, final float kR, final float kG, final float kB) {
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 0] = etaIR;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 1] = etaIG;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_I + 2] = etaIB;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 0] = etaTR;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 1] = etaTG;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ETA_T + 2] = etaTB;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 0] = kR;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 1] = kG;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_K + 2] = kB;
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorSetMicrofacetDistributionTrowbridgeReitz(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelConductorSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFTorranceSparrowBRDFFresnelConductorArray_$private$16[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_CONDUCTOR_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - TorranceSparrowBRDF - DielectricFresnel //////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFTorranceSparrowBRDFFresnelDielectricIsMatchingID(final int id) {
		return id == B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ID;
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDielectricIsSamplingVisibleArea() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDielectricIsSeparableModel() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDielectricSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(checkIsZero(outgoingZ)) {
			return false;
		}
		
		doBXDFTorranceSparrowBRDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		
		doMicrofacetDistributionTrowbridgeReitzSampleNormal(outgoingX, outgoingY, outgoingZ, u, v);
		
		final float normalX = vector3FGetComponent1();
		final float normalY = vector3FGetComponent2();
		final float normalZ = vector3FGetComponent3();
		
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ) < 0.0F) {
			return false;
		}
		
		vector3FSetSpecularReflection(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			return false;
		}
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateDistributionFunction();
		doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetAlphaX() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetAlphaY() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetEtaI() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetEtaT() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleB() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleG() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleR() {
		return this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		if(checkIsZero(cosThetaAbsOutgoing) || checkIsZero(cosThetaAbsIncoming)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalCorrectlyOrientedX = normalNormalizedZ < 0.0F ? -normalNormalizedX : normalNormalizedX;
		final float normalCorrectlyOrientedY = normalNormalizedZ < 0.0F ? -normalNormalizedY : normalNormalizedY;
		final float normalCorrectlyOrientedZ = normalNormalizedZ < 0.0F ? -normalNormalizedZ : normalNormalizedZ;
		
		doBXDFTorranceSparrowBRDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateFresnel(vector3FDotProduct(incomingX, incomingY, incomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
		
		final float d = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float g = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking2(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ);
		final float f = color3FLHSGetComponent1();
		final float h = 1.0F / (4.0F * cosThetaAbsIncoming * cosThetaAbsOutgoing);
		
		final float reflectanceScaleR = doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFTorranceSparrowBRDFFresnelDielectricGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * d * g * f * h;
		final float resultG = reflectanceScaleG * d * g * f * h;
		final float resultB = reflectanceScaleB * d * g * f * h;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateFresnel(final float cosThetaI) {
		final float etaI = doBXDFTorranceSparrowBRDFFresnelDielectricGetEtaI();
		final float etaT = doBXDFTorranceSparrowBRDFFresnelDielectricGetEtaT();
		
		final float reflectance = doFresnelDielectric(cosThetaI, etaI, etaT);
		
		color3FLHSSet(reflectance, reflectance, reflectance);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		doBXDFTorranceSparrowBRDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float probabilityDensityFunctionValue0 = doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float probabilityDensityFunctionValue1 = probabilityDensityFunctionValue0 / (4.0F * vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ));
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue1);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz() {
		final boolean isSamplingVisibleArea = doBXDFTorranceSparrowBRDFFresnelDielectricIsSamplingVisibleArea();
		final boolean isSeparableModel = doBXDFTorranceSparrowBRDFFresnelDielectricIsSeparableModel();
		
		final float alphaX = doBXDFTorranceSparrowBRDFFresnelDielectricGetAlphaX();
		final float alphaY = doBXDFTorranceSparrowBRDFFresnelDielectricGetAlphaY();
		
		doMicrofacetDistributionTrowbridgeReitzSet(isSamplingVisibleArea, isSeparableModel, alphaX, alphaY);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricSetFresnelDielectric(final float etaI, final float etaT) {
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_I] = etaI;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_T] = etaT;
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDielectricSetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFTorranceSparrowBRDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - TorranceSparrowBRDF - DisneyFresnel //////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFTorranceSparrowBRDFFresnelDisneyIsMatchingID(final int id) {
		return id == B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ID;
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDisneyIsSamplingVisibleArea() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDisneyIsSeparableModel() {
		return !checkIsZero(this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private boolean doBXDFTorranceSparrowBRDFFresnelDisneySampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(checkIsZero(outgoingZ)) {
			return false;
		}
		
		doBXDFTorranceSparrowBRDFFresnelDisneyInitializeMicrofacetDistributionTrowbridgeReitz();
		
		doMicrofacetDistributionTrowbridgeReitzSampleNormal(outgoingX, outgoingY, outgoingZ, u, v);
		
		final float normalX = vector3FGetComponent1();
		final float normalY = vector3FGetComponent2();
		final float normalZ = vector3FGetComponent3();
		
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ) < 0.0F) {
			return false;
		}
		
		vector3FSetSpecularReflection(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			return false;
		}
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateDistributionFunction();
		doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetAlphaX() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetAlphaY() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetEta() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ETA];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetMetallic() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_METALLIC];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetR0B() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetR0G() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetR0R() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 0];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleB() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 2];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleG() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 1];
	}
	
	private float doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleR() {
		return this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 0];
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		final float cosThetaAbsOutgoing = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
		final float cosThetaAbsIncoming = vector3FCosThetaAbs(incomingX, incomingY, incomingZ);
		
		if(checkIsZero(cosThetaAbsOutgoing) || checkIsZero(cosThetaAbsIncoming)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		
		if(checkIsZero(normalX) && checkIsZero(normalY) && checkIsZero(normalZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalCorrectlyOrientedX = normalNormalizedZ < 0.0F ? -normalNormalizedX : normalNormalizedX;
		final float normalCorrectlyOrientedY = normalNormalizedZ < 0.0F ? -normalNormalizedY : normalNormalizedY;
		final float normalCorrectlyOrientedZ = normalNormalizedZ < 0.0F ? -normalNormalizedZ : normalNormalizedZ;
		
		doBXDFTorranceSparrowBRDFFresnelDisneyInitializeMicrofacetDistributionTrowbridgeReitz();
		doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateFresnel(vector3FDotProduct(incomingX, incomingY, incomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ));
		
		final float d = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float g = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking2(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ);
		final float f = color3FLHSGetComponent1();
		final float h = 1.0F / (4.0F * cosThetaAbsIncoming * cosThetaAbsOutgoing);
		
		final float reflectanceScaleR = doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleR();
		final float reflectanceScaleG = doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleG();
		final float reflectanceScaleB = doBXDFTorranceSparrowBRDFFresnelDisneyGetReflectanceScaleB();
		
		final float resultR = reflectanceScaleR * d * g * f * h;
		final float resultG = reflectanceScaleG * d * g * f * h;
		final float resultB = reflectanceScaleB * d * g * f * h;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateFresnel(final float cosThetaI) {
		final float eta = doBXDFTorranceSparrowBRDFFresnelDisneyGetEta();
		
		final float metallic = doBXDFTorranceSparrowBRDFFresnelDisneyGetMetallic();
		
		final float r0R = doBXDFTorranceSparrowBRDFFresnelDisneyGetR0R();
		final float r0G = doBXDFTorranceSparrowBRDFFresnelDisneyGetR0G();
		final float r0B = doBXDFTorranceSparrowBRDFFresnelDisneyGetR0B();
		
		final float fresnel = doFresnelDielectric(cosThetaI, 1.0F, eta);
		final float fresnelWeight = doSchlickFresnelWeight(cosThetaI);
		
		final float reflectanceR = lerp(fresnel, lerp(r0R, 1.0F, fresnelWeight), metallic);
		final float reflectanceG = lerp(fresnel, lerp(r0G, 1.0F, fresnelWeight), metallic);
		final float reflectanceB = lerp(fresnel, lerp(r0B, 1.0F, fresnelWeight), metallic);
		
		color3FLHSSet(reflectanceR, reflectanceG, reflectanceB);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneyEvaluateProbabilityDensityFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(!vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		final float normalX = outgoingX + incomingX;
		final float normalY = outgoingY + incomingY;
		final float normalZ = outgoingZ + incomingZ;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		doBXDFTorranceSparrowBRDFFresnelDisneyInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float probabilityDensityFunctionValue0 = doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float probabilityDensityFunctionValue1 = probabilityDensityFunctionValue0 / (4.0F * vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ));
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue1);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneyInitializeMicrofacetDistributionTrowbridgeReitz() {
		final boolean isSamplingVisibleArea = doBXDFTorranceSparrowBRDFFresnelDisneyIsSamplingVisibleArea();
		final boolean isSeparableModel = doBXDFTorranceSparrowBRDFFresnelDisneyIsSeparableModel();
		
		final float alphaX = doBXDFTorranceSparrowBRDFFresnelDisneyGetAlphaX();
		final float alphaY = doBXDFTorranceSparrowBRDFFresnelDisneyGetAlphaY();
		
		doMicrofacetDistributionTrowbridgeReitzSet(isSamplingVisibleArea, isSeparableModel, alphaX, alphaY);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneySetFresnelDisney(final float r0R, final float r0G, final float r0B, final float eta, final float metallic) {
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 0] = r0R;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 1] = r0G;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_R0 + 2] = r0B;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ETA] = eta;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_METALLIC] = metallic;
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneySetMicrofacetDistributionTrowbridgeReitz(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	private void doBXDFTorranceSparrowBRDFFresnelDisneySetReflectanceScale(final float reflectanceScaleR, final float reflectanceScaleG, final float reflectanceScaleB) {
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 0] = reflectanceScaleR;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 1] = reflectanceScaleG;
		this.bXDFTorranceSparrowBRDFFresnelDisneyArray_$private$12[B_X_D_F_TORRANCE_SPARROW_B_R_D_F_FRESNEL_DISNEY_ARRAY_OFFSET_REFLECTANCE_SCALE + 2] = reflectanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDF - TorranceSparrowBTDF - DielectricFresnel //////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doBXDFTorranceSparrowBTDFFresnelDielectricIsMatchingID(final int id) {
		return id == B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ID;
	}
	
	private boolean doBXDFTorranceSparrowBTDFFresnelDielectricIsSamplingVisibleArea() {
		return !checkIsZero(this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doBXDFTorranceSparrowBTDFFresnelDielectricIsSeparableModel() {
		return !checkIsZero(this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private boolean doBXDFTorranceSparrowBTDFFresnelDielectricSampleDistributionFunction(final float u, final float v) {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		if(checkIsZero(outgoingZ)) {
			return false;
		}
		
		doBXDFTorranceSparrowBTDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		
		doMicrofacetDistributionTrowbridgeReitzSampleNormal(outgoingX, outgoingY, outgoingZ, u, v);
		
		final float normalX = vector3FGetComponent1();
		final float normalY = vector3FGetComponent2();
		final float normalZ = vector3FGetComponent3();
		
		if(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ) < 0.0F) {
			return false;
		}
		
		final float etaA = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaB();
		final float eta = vector3FCosTheta(outgoingX, outgoingY, outgoingZ) > 0.0F ? etaA / etaB : etaB / etaA;
		
		if(!vector3FSetRefraction(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ, eta)) {
			return false;
		}
		
		final float incomingX = vector3FGetComponent1();
		final float incomingY = vector3FGetComponent2();
		final float incomingZ = vector3FGetComponent3();
		
		doBXDFResultSetIncoming(incomingX, incomingY, incomingZ);
		doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateDistributionFunction();
		doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateProbabilityDensityFunction();
		
		return true;
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetAlphaX() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetAlphaY() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaA() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaB() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleB() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleG() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1];
	}
	
	private float doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleR() {
		return this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0];
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateDistributionFunction() {
		final float outgoingX = doBXDFResultGetOutgoingX();
		final float outgoingY = doBXDFResultGetOutgoingY();
		final float outgoingZ = doBXDFResultGetOutgoingZ();
		
		final float incomingX = doBXDFResultGetIncomingX();
		final float incomingY = doBXDFResultGetIncomingY();
		final float incomingZ = doBXDFResultGetIncomingZ();
		
		if(vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float cosThetaOutgoing = vector3FCosTheta(outgoingX, outgoingY, outgoingZ);
		final float cosThetaIncoming = vector3FCosTheta(incomingX, incomingY, incomingZ);
		
		if(checkIsZero(cosThetaOutgoing) || checkIsZero(cosThetaIncoming)) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		final float etaA = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaB();
		final float eta = cosThetaOutgoing > 0.0F ? etaB / etaA : etaA / etaB;
		
		final float normalX = outgoingX + incomingX * eta;
		final float normalY = outgoingY + incomingY * eta;
		final float normalZ = outgoingZ + incomingZ * eta;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		final float normalCorrectlyOrientedX = normalNormalizedZ < 0.0F ? -normalNormalizedX : normalNormalizedX;
		final float normalCorrectlyOrientedY = normalNormalizedZ < 0.0F ? -normalNormalizedY : normalNormalizedY;
		final float normalCorrectlyOrientedZ = normalNormalizedZ < 0.0F ? -normalNormalizedZ : normalNormalizedZ;
		
		final float outgoingDotNormal = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ);
		final float incomingDotNormal = vector3FDotProduct(incomingX, incomingY, incomingZ, normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ);
		
		if(outgoingDotNormal * incomingDotNormal > 0.0F) {
			doBXDFResultSetResult(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		doBXDFTorranceSparrowBTDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateFresnel(outgoingDotNormal);
		
		final float a = outgoingDotNormal + eta * incomingDotNormal;
		final float b = 1.0F / eta;
		final float d = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ);
		final float g = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking2(outgoingX, outgoingY, outgoingZ, incomingX, incomingY, incomingZ);
		final float f = 1.0F - color3FLHSGetComponent1();
		final float h = abs(d * g * eta * eta * abs(incomingDotNormal) * abs(outgoingDotNormal) * b * b / (cosThetaIncoming * cosThetaOutgoing * a * a));
		
		final float transmittanceScaleR = doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleR();
		final float transmittanceScaleG = doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleG();
		final float transmittanceScaleB = doBXDFTorranceSparrowBTDFFresnelDielectricGetTransmittanceScaleB();
		
		final float resultR = transmittanceScaleR * f * h;
		final float resultG = transmittanceScaleG * f * h;
		final float resultB = transmittanceScaleB * f * h;
		
		doBXDFResultSetResult(resultR, resultG, resultB);
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateFresnel(final float cosThetaI) {
		final float etaA = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaB();
		
		final float reflectance = doFresnelDielectric(cosThetaI, etaA, etaB);
		
		color3FLHSSet(reflectance, reflectance, reflectance);
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricEvaluateProbabilityDensityFunction() {
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
		
		final float etaA = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaA();
		final float etaB = doBXDFTorranceSparrowBTDFFresnelDielectricGetEtaB();
		final float eta = vector3FCosTheta(outgoingX, outgoingY, outgoingZ) > 0.0F ? etaB / etaA : etaA / etaB;
		
		final float normalX = outgoingX + incomingX * eta;
		final float normalY = outgoingY + incomingY * eta;
		final float normalZ = outgoingZ + incomingZ * eta;
		final float normalLengthReciprocal = vector3FLengthReciprocal(normalX, normalY, normalZ);
		final float normalNormalizedX = normalX * normalLengthReciprocal;
		final float normalNormalizedY = normalY * normalLengthReciprocal;
		final float normalNormalizedZ = normalZ * normalLengthReciprocal;
		
		final float outgoingDotNormal = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		final float incomingDotNormal = vector3FDotProduct(incomingX, incomingY, incomingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ);
		
		if(outgoingDotNormal * incomingDotNormal > 0.0F) {
			doBXDFResultSetProbabilityDensityFunctionValue(0.0F);
			
			return;
		}
		
		doBXDFTorranceSparrowBTDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz();
		
		final float a = outgoingDotNormal + eta * incomingDotNormal;
		final float b = abs((eta * eta * incomingDotNormal) / (a * a));
		
		final float probabilityDensityFunctionValue = doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(outgoingX, outgoingY, outgoingZ, normalNormalizedX, normalNormalizedY, normalNormalizedZ) * b;
		
		doBXDFResultSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricInitializeMicrofacetDistributionTrowbridgeReitz() {
		final boolean isSamplingVisibleArea = doBXDFTorranceSparrowBTDFFresnelDielectricIsSamplingVisibleArea();
		final boolean isSeparableModel = doBXDFTorranceSparrowBTDFFresnelDielectricIsSeparableModel();
		
		final float alphaX = doBXDFTorranceSparrowBTDFFresnelDielectricGetAlphaX();
		final float alphaY = doBXDFTorranceSparrowBTDFFresnelDielectricGetAlphaY();
		
		doMicrofacetDistributionTrowbridgeReitzSet(isSamplingVisibleArea, isSeparableModel, alphaX, alphaY);
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricSetFresnelDielectric(final float etaA, final float etaB) {
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_A] = etaA;
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ETA_B] = etaB;
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricSetMicrofacetDistributionTrowbridgeReitz(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	private void doBXDFTorranceSparrowBTDFFresnelDielectricSetTransmittanceScale(final float transmittanceScaleR, final float transmittanceScaleG, final float transmittanceScaleB) {
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 0] = transmittanceScaleR;
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 1] = transmittanceScaleG;
		this.bXDFTorranceSparrowBTDFFresnelDielectricArray_$private$9[B_X_D_F_TORRANCE_SPARROW_B_T_D_F_FRESNEL_DIELECTRIC_ARRAY_OFFSET_TRANSMITTANCE_SCALE + 2] = transmittanceScaleB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// BXDFResult //////////////////////////////////////////////////////////////////////////////////////
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
	
	private void doBXDFResultSetIncomingTransformedFromBSDFResult() {
		final float incomingX = materialBSDFResultGetIncomingX();
		final float incomingY = materialBSDFResultGetIncomingY();
		final float incomingZ = materialBSDFResultGetIncomingZ();
		
		vector3FSetOrthonormalBasis33FTransformReverseNormalize(incomingX, incomingY, incomingZ);
		
		final float incomingTransformedX = vector3FGetComponent1();
		final float incomingTransformedY = vector3FGetComponent2();
		final float incomingTransformedZ = vector3FGetComponent3();
		
		doBXDFResultSetIncoming(incomingTransformedX, incomingTransformedY, incomingTransformedZ);
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
	// Fresnel - ConductorFresnel //////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doFresnelConductor(final float cosThetaI, final float etaIR, final float etaIG, final float etaIB, final float etaTR, final float etaTG, final float etaTB, final float kR, final float kG, final float kB) {
		final float saturateCosThetaI = saturateF(cosThetaI, -1.0F, 1.0F);
		final float saturateCosThetaIMultipliedBy2 = saturateCosThetaI * 2.0F;
		
		final float etaR = etaTR / etaIR;
		final float etaG = etaTG / etaIG;
		final float etaB = etaTB / etaIB;
		
		final float etaKR = kR / etaIR;
		final float etaKG = kG / etaIG;
		final float etaKB = kB / etaIB;
		
		final float cosThetaISquared = saturateCosThetaI * saturateCosThetaI;
		final float sinThetaISquared = 1.0F - cosThetaISquared;
		final float sinThetaISquaredSquared = sinThetaISquared * sinThetaISquared;
		
		final float etaSquaredR = etaR * etaR;
		final float etaSquaredG = etaG * etaG;
		final float etaSquaredB = etaB * etaB;
		
		final float etaKSquaredR = etaKR * etaKR;
		final float etaKSquaredG = etaKG * etaKG;
		final float etaKSquaredB = etaKB * etaKB;
		
		final float t0R = etaSquaredR - etaKSquaredR - sinThetaISquared;
		final float t0G = etaSquaredG - etaKSquaredG - sinThetaISquared;
		final float t0B = etaSquaredB - etaKSquaredB - sinThetaISquared;
		
		final float t0SquaredR = t0R * t0R;
		final float t0SquaredG = t0G * t0G;
		final float t0SquaredB = t0B * t0B;
		
		final float aSquaredPlusBSquaredR = sqrt(t0SquaredR + etaSquaredR * etaKSquaredR * 4.0F);
		final float aSquaredPlusBSquaredG = sqrt(t0SquaredG + etaSquaredG * etaKSquaredG * 4.0F);
		final float aSquaredPlusBSquaredB = sqrt(t0SquaredB + etaSquaredB * etaKSquaredB * 4.0F);
		
		final float t1R = aSquaredPlusBSquaredR + cosThetaISquared;
		final float t1G = aSquaredPlusBSquaredG + cosThetaISquared;
		final float t1B = aSquaredPlusBSquaredB + cosThetaISquared;
		
		final float t2R = sqrt((aSquaredPlusBSquaredR + t0R) * 0.5F) * saturateCosThetaIMultipliedBy2;
		final float t2G = sqrt((aSquaredPlusBSquaredG + t0G) * 0.5F) * saturateCosThetaIMultipliedBy2;
		final float t2B = sqrt((aSquaredPlusBSquaredB + t0B) * 0.5F) * saturateCosThetaIMultipliedBy2;
		
		final float t3R = aSquaredPlusBSquaredR * cosThetaISquared + sinThetaISquaredSquared;
		final float t3G = aSquaredPlusBSquaredG * cosThetaISquared + sinThetaISquaredSquared;
		final float t3B = aSquaredPlusBSquaredB * cosThetaISquared + sinThetaISquaredSquared;
		
		final float t4R = t2R * sinThetaISquared;
		final float t4G = t2G * sinThetaISquared;
		final float t4B = t2B * sinThetaISquared;
		
		final float reflectanceSR = (t1R - t2R) / (t1R + t2R);
		final float reflectanceSG = (t1G - t2G) / (t1G + t2G);
		final float reflectanceSB = (t1B - t2B) / (t1B + t2B);
		
		final float reflectancePR = reflectanceSR * (t3R - t4R) / (t3R + t4R);
		final float reflectancePG = reflectanceSG * (t3G - t4G) / (t3G + t4G);
		final float reflectancePB = reflectanceSB * (t3B - t4B) / (t3B + t4B);
		
		final float reflectanceR = (reflectancePR + reflectanceSR) * 0.5F;
		final float reflectanceG = (reflectancePG + reflectanceSG) * 0.5F;
		final float reflectanceB = (reflectancePB + reflectanceSB) * 0.5F;
		
		color3FLHSSet(reflectanceR, reflectanceG, reflectanceB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Fresnel - DielectricFresnel /////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doFresnelDielectric(final float cosThetaI, final float etaI, final float etaT) {
		final float saturateCosThetaI = saturateF(cosThetaI, -1.0F, 1.0F);
		
		final boolean isEntering = saturateCosThetaI > 0.0F;
		
		final float currentCosThetaI = isEntering ? saturateCosThetaI : abs(saturateCosThetaI);
		final float currentEtaI = isEntering ? etaI : etaT;
		final float currentEtaT = isEntering ? etaT : etaI;
		
		final float currentSinThetaI = sqrt(max(0.0F, 1.0F - currentCosThetaI * currentCosThetaI));
		final float currentSinThetaT = currentEtaI / currentEtaT * currentSinThetaI;
		
		if(currentSinThetaT >= 1.0F) {
			return 1.0F;
		}
		
		final float currentCosThetaT = sqrt(max(0.0F, 1.0F - currentSinThetaT * currentSinThetaT));
		
		final float reflectancePara = ((currentEtaT * currentCosThetaI) - (currentEtaI * currentCosThetaT)) / ((currentEtaT * currentCosThetaI) + (currentEtaI * currentCosThetaT));
		final float reflectancePerp = ((currentEtaI * currentCosThetaI) - (currentEtaT * currentCosThetaT)) / ((currentEtaI * currentCosThetaI) + (currentEtaT * currentCosThetaT));
		final float reflectance = (reflectancePara * reflectancePara + reflectancePerp * reflectancePerp) / 2.0F;
		
		return reflectance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// MicrofacetDistribution - TrowbridgeReitzMicrofacetDistribution //////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doMicrofacetDistributionTrowbridgeReitzIsSamplingVisibleArea() {
		return !checkIsZero(this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA]);
	}
	
	private boolean doMicrofacetDistributionTrowbridgeReitzIsSeparableModel() {
		return !checkIsZero(this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SEPARABLE_MODEL]);
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(final float normalX, final float normalY, final float normalZ) {
		final float tanThetaSquaredNormal = vector3FTanThetaSquared(normalX, normalY, normalZ);
		
		if(checkIsInfinite(tanThetaSquaredNormal)) {
			return 0.0F;
		}
		
		final float alphaX = doMicrofacetDistributionTrowbridgeReitzGetAlphaX();
		final float alphaXSquared = alphaX * alphaX;
		final float alphaY = doMicrofacetDistributionTrowbridgeReitzGetAlphaY();
		final float alphaYSquared = alphaY * alphaY;
		
		final float cosPhiSquaredNormal = vector3FCosPhiSquared(normalX, normalY, normalZ);
		final float sinPhiSquaredNormal = vector3FSinPhiSquared(normalX, normalY, normalZ);
		
		final float cosThetaQuarticNormal = vector3FCosThetaQuartic(normalX, normalY, normalZ);
		
		final float exponent = (cosPhiSquaredNormal / alphaXSquared + sinPhiSquaredNormal / alphaYSquared) * tanThetaSquaredNormal;
		
		final float differentialArea = 1.0F / (PI * alphaX * alphaY * cosThetaQuarticNormal * (1.0F + exponent) * (1.0F + exponent));
		
		return differentialArea;
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzComputeLambda(final float outgoingX, final float outgoingY, final float outgoingZ) {
		final float tanThetaAbsOutgoing = vector3FTanThetaAbs(outgoingX, outgoingY, outgoingZ);
		
		if(checkIsInfinite(tanThetaAbsOutgoing)) {
			return 0.0F;
		}
		
		final float alphaX = doMicrofacetDistributionTrowbridgeReitzGetAlphaX();
		final float alphaY = doMicrofacetDistributionTrowbridgeReitzGetAlphaY();
		
		final float cosPhiSquaredOutgoing = vector3FCosPhiSquared(outgoingX, outgoingY, outgoingZ);
		final float sinPhiSquaredOutgoing = vector3FSinPhiSquared(outgoingX, outgoingY, outgoingZ);
		
		final float alpha = sqrt(cosPhiSquaredOutgoing * alphaX * alphaX + sinPhiSquaredOutgoing * alphaY * alphaY);
		final float alphaTanThetaAbsOutgoingSquared = (alpha * tanThetaAbsOutgoing) * (alpha * tanThetaAbsOutgoing);
		
		final float lambda = (-1.0F + sqrt(1.0F + alphaTanThetaAbsOutgoingSquared)) / 2.0F;
		
		return lambda;
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzComputeProbabilityDensityFunctionValue(final float outgoingX, final float outgoingY, final float outgoingZ, final float normalX, final float normalY, final float normalZ) {
		final float differentialArea = doMicrofacetDistributionTrowbridgeReitzComputeDifferentialArea(normalX, normalY, normalZ);
		
		if(doMicrofacetDistributionTrowbridgeReitzIsSamplingVisibleArea()) {
			final float shadowingAndMasking = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking1(outgoingX, outgoingY, outgoingZ);
			final float outgoingDotNormalAbs = abs(vector3FDotProduct(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ));
			final float outgoingCosThetaAbs = vector3FCosThetaAbs(outgoingX, outgoingY, outgoingZ);
			final float probabilityDensityFunctionValue = differentialArea * shadowingAndMasking * outgoingDotNormalAbs / outgoingCosThetaAbs;
			
			return probabilityDensityFunctionValue;
		}
		
		final float normalCosThetaAbs = vector3FCosThetaAbs(normalX, normalY, normalZ);
		final float probabilityDensityFunctionValue = differentialArea * normalCosThetaAbs;
		
		return probabilityDensityFunctionValue;
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking1(final float outgoingX, final float outgoingY, final float outgoingZ) {
		return 1.0F / (1.0F + doMicrofacetDistributionTrowbridgeReitzComputeLambda(outgoingX, outgoingY, outgoingZ));
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking2(final float outgoingX, final float outgoingY, final float outgoingZ, final float incomingX, final float incomingY, final float incomingZ) {
		if(doMicrofacetDistributionTrowbridgeReitzIsSeparableModel()) {
			final float shadowingAndMaskingOutgoing = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking1(outgoingX, outgoingY, outgoingZ);
			final float shadowingAndMaskingIncoming = doMicrofacetDistributionTrowbridgeReitzComputeShadowingAndMasking1(incomingX, incomingY, incomingZ);
			final float shadowingAndMasking = shadowingAndMaskingOutgoing * shadowingAndMaskingIncoming;
			
			return shadowingAndMasking;
		}
		
		final float lambdaOutgoing = doMicrofacetDistributionTrowbridgeReitzComputeLambda(outgoingX, outgoingY, outgoingZ);
		final float lambdaIncoming = doMicrofacetDistributionTrowbridgeReitzComputeLambda(incomingX, incomingY, incomingZ);
		
		final float shadowingAndMasking = 1.0F / (1.0F + lambdaOutgoing + lambdaIncoming);
		
		return shadowingAndMasking;
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzConvertRoughnessToAlpha(final float roughness) {
		final float x = max(roughness, 1.0e-3F);
		final float y = log(x);
		final float z = 1.62142F + 0.819955F * y + 0.1734F * y * y + 0.0171201F * y * y * y + 0.000640711F * y * y * y * y;
		
		return z;
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzGetAlphaX() {
		return this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_X];
	}
	
	private float doMicrofacetDistributionTrowbridgeReitzGetAlphaY() {
		return this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_Y];
	}
	
	private void doMicrofacetDistributionTrowbridgeReitzComputeSlope(final float cosThetaIncoming, final float u, final float v) {
		if(cosThetaIncoming > 0.9999F) {
			final float r = sqrt(u / (1.0F - u));
			final float phi = v * PI_MULTIPLIED_BY_2;
			
			final float x = r * cos(phi);
			final float y = r * sin(phi);
			
			vector3FSet(x, y, 0.0F);
		} else {
			final float sinThetaIncoming = sqrt(max(0.0F, 1.0F - cosThetaIncoming * cosThetaIncoming));
			final float tanThetaIncoming = sinThetaIncoming / cosThetaIncoming;
			final float tanThetaIncomingReciprocal = 1.0F / tanThetaIncoming;
			
			final float a = 2.0F / (1.0F + sqrt(1.0F + 1.0F / (tanThetaIncomingReciprocal * tanThetaIncomingReciprocal)));
			final float b = 2.0F * u / a - 1.0F;
			final float c = min(1.0F / (b * b - 1.0F), 1.0e10F);
			final float d = tanThetaIncoming;
			final float e = sqrt(max(d * d * c * c - (b * b - d * d) * c, 0.0F));
			final float f = d * c - e;
			final float g = d * c + e;
			final float h = v > 0.5F ? 1.0F : -1.0F;
			final float i = v > 0.5F ? 2.0F * (v - 0.5F) : 2.0F * (0.5F - v);
			final float j = (i * (i * (i * 0.27385F - 0.73369F) + 0.46341F)) / (i * (i * (i * 0.093073F + 0.309420F) - 1.000000F) + 0.597999F);
			
			final float x = b < 0.0F || g > 1.0F / tanThetaIncoming ? f : g;
			final float y = h * j * sqrt(1.0F + x * x);
			
			vector3FSet(x, y, 0.0F);
		}
	}
	
	private void doMicrofacetDistributionTrowbridgeReitzSample(final boolean isNegating, final float incomingX, final float incomingY, final float incomingZ, final float alphaX, final float alphaY, final float u, final float v) {
		final float incomingCorrectlyOrientedX = isNegating ? -incomingX : incomingX;
		final float incomingCorrectlyOrientedY = isNegating ? -incomingY : incomingY;
		final float incomingCorrectlyOrientedZ = isNegating ? -incomingZ : incomingZ;
		final float incomingStretchedX = incomingCorrectlyOrientedX * alphaX;
		final float incomingStretchedY = incomingCorrectlyOrientedY * alphaY;
		final float incomingStretchedZ = incomingCorrectlyOrientedZ;
		final float incomingStretchedLengthReciprocal = vector3FLengthReciprocal(incomingStretchedX, incomingStretchedY, incomingStretchedZ);
		final float incomingStretchedNormalizedX = incomingStretchedX * incomingStretchedLengthReciprocal;
		final float incomingStretchedNormalizedY = incomingStretchedY * incomingStretchedLengthReciprocal;
		final float incomingStretchedNormalizedZ = incomingStretchedZ * incomingStretchedLengthReciprocal;
		
		final float cosPhi = vector3FCosPhi(incomingStretchedNormalizedX, incomingStretchedNormalizedY, incomingStretchedNormalizedZ);
		final float sinPhi = vector3FSinPhi(incomingStretchedNormalizedX, incomingStretchedNormalizedY, incomingStretchedNormalizedZ);
		final float cosTheta = vector3FCosTheta(incomingStretchedNormalizedX, incomingStretchedNormalizedY, incomingStretchedNormalizedZ);
		
		doMicrofacetDistributionTrowbridgeReitzComputeSlope(cosTheta, u, v);
		
		final float slopeX = vector3FGetComponent1();
		final float slopeY = vector3FGetComponent2();
		
		final float sampleX = -((cosPhi * slopeX - sinPhi * slopeY) * alphaX);
		final float sampleY = -((sinPhi * slopeX + cosPhi * slopeY) * alphaY);
		final float sampleZ = 1.0F;
		final float sampleCorrectlyOrientedX = isNegating ? -sampleX : sampleX;
		final float sampleCorrectlyOrientedY = isNegating ? -sampleY : sampleY;
		final float sampleCorrectlyOrientedZ = isNegating ? -sampleZ : sampleZ;
		
		vector3FSetNormalize(sampleCorrectlyOrientedX, sampleCorrectlyOrientedY, sampleCorrectlyOrientedZ);
	}
	
	private void doMicrofacetDistributionTrowbridgeReitzSampleNormal(final float outgoingX, final float outgoingY, final float outgoingZ, final float u, final float v) {
		final float alphaX = doMicrofacetDistributionTrowbridgeReitzGetAlphaX();
		final float alphaY = doMicrofacetDistributionTrowbridgeReitzGetAlphaY();
		
		if(doMicrofacetDistributionTrowbridgeReitzIsSamplingVisibleArea()) {
			doMicrofacetDistributionTrowbridgeReitzSample(outgoingZ < 0.0F, outgoingX, outgoingY, outgoingZ, alphaX, alphaY, u, v);
		} else if(alphaX == alphaY) {
			final float phi = v * PI_MULTIPLIED_BY_2;
			final float cosTheta = 1.0F / sqrt(1.0F + (alphaX * alphaX * u / (1.0F - u)));
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final float normalX = sinTheta * cos(phi);
			final float normalY = sinTheta * sin(phi);
			final float normalZ = cosTheta;
			
			final boolean isSameHemisphereZ = vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
			
			final float normalCorrectlyOrientedX = isSameHemisphereZ ? normalX : -normalX;
			final float normalCorrectlyOrientedY = isSameHemisphereZ ? normalY : -normalY;
			final float normalCorrectlyOrientedZ = isSameHemisphereZ ? normalZ : -normalZ;
			
			vector3FSet(normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ);
		} else {
			final float phi = atan(alphaY / alphaX * tan(v * PI_MULTIPLIED_BY_2 + 0.5F * PI)) + (v > 0.5F ? PI : 0.0F);
			final float cosPhi = cos(phi);
			final float sinPhi = sin(phi);
			final float cosTheta = 1.0F / sqrt(1.0F + ((1.0F / (cosPhi * cosPhi / (alphaX * alphaX) + sinPhi * sinPhi / (alphaY * alphaY))) * u / (1.0F - u)));
			final float sinTheta = sqrt(max(0.0F, 1.0F - cosTheta * cosTheta));
			
			final float normalX = sinTheta * cosPhi;
			final float normalY = sinTheta * sinPhi;
			final float normalZ = cosTheta;
			
			final boolean isSameHemisphereZ = vector3FSameHemisphereZ(outgoingX, outgoingY, outgoingZ, normalX, normalY, normalZ);
			
			final float normalCorrectlyOrientedX = isSameHemisphereZ ? normalX : -normalX;
			final float normalCorrectlyOrientedY = isSameHemisphereZ ? normalY : -normalY;
			final float normalCorrectlyOrientedZ = isSameHemisphereZ ? normalZ : -normalZ;
			
			vector3FSet(normalCorrectlyOrientedX, normalCorrectlyOrientedY, normalCorrectlyOrientedZ);
		}
	}
	
	private void doMicrofacetDistributionTrowbridgeReitzSet(final boolean isSamplingVisibleArea, final boolean isSeparableModel, final float alphaX, final float alphaY) {
		this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SAMPLING_VISIBLE_AREA] = isSamplingVisibleArea ? 1.0F : 0.0F;
		this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_IS_SEPARABLE_MODEL] = isSeparableModel ? 1.0F : 0.0F;
		this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_X] = max(alphaX, 0.001F);
		this.microfacetDistributionTrowbridgeReitzArray_$private$4[MICROFACET_DISTRIBUTION_TROWBRIDGE_REITZ_ARRAY_OFFSET_ALPHA_Y] = max(alphaY, 0.001F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Schlick /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float doSchlickFresnelDielectric1(final float cosTheta, final float r0) {
		return r0 + (1.0F - r0) * doSchlickFresnelWeight(cosTheta);
	}
	
	private float doSchlickFresnelWeight(final float cosTheta) {
		return pow5(saturateF(1.0F - cosTheta, 0.0F, 1.0F));
	}
	
	private float doSchlickFresnelWeightLerp(final float cosTheta, final float r0) {
		return lerp(r0, 1.0F, doSchlickFresnelWeight(cosTheta));
	}
	
	private void doSchlickFresnelDielectric3(final float cosTheta, final float r0R, final float r0G, final float r0B) {
		final float pow5OneMinusCosTheta = pow5(1.0F - cosTheta);
		
		final float reflectanceR = r0R + (1.0F - r0R) * pow5OneMinusCosTheta;
		final float reflectanceG = r0G + (1.0F - r0G) * pow5OneMinusCosTheta;
		final float reflectanceB = r0B + (1.0F - r0B) * pow5OneMinusCosTheta;
		
		color3FLHSSet(reflectanceR, reflectanceG, reflectanceB);
	}
}