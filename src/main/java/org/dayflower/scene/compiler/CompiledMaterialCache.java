/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.pack;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.scene.Material;
import org.dayflower.scene.material.BullseyeMaterial;
import org.dayflower.scene.material.CheckerboardMaterial;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.PolkaDotMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.material.UberMaterial;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.texture.Texture;
import org.dayflower.utility.Document;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

/**
 * A {@code CompiledMaterialCache} contains {@link Material} instances in compiled form.
 * <p>
 * The {@code Material} implementations that are supported are the following:
 * <ul>
 * <li>{@link BullseyeMaterial}</li>
 * <li>{@link CheckerboardMaterial}</li>
 * <li>{@link ClearCoatMaterial}</li>
 * <li>{@link DisneyMaterial}</li>
 * <li>{@link GlassMaterial}</li>
 * <li>{@link GlossyMaterial}</li>
 * <li>{@link MatteMaterial}</li>
 * <li>{@link MetalMaterial}</li>
 * <li>{@link MirrorMaterial}</li>
 * <li>{@link PlasticMaterial}</li>
 * <li>{@link PolkaDotMaterial}</li>
 * <li>{@link SubstrateMaterial}</li>
 * <li>{@link UberMaterial}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledMaterialCache {
	/**
	 * The length of a compiled {@link BullseyeMaterial} instance.
	 */
	public static final int BULLSEYE_MATERIAL_LENGTH = 8;
	
	/**
	 * The offset for the {@link Material} denoted by {@code A} in a compiled {@link BullseyeMaterial} instance.
	 */
	public static final int BULLSEYE_MATERIAL_OFFSET_MATERIAL_A = 3;
	
	/**
	 * The offset for the {@link Material} denoted by {@code B} in a compiled {@link BullseyeMaterial} instance.
	 */
	public static final int BULLSEYE_MATERIAL_OFFSET_MATERIAL_B = 4;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the origin in a compiled {@link BullseyeMaterial} instance.
	 */
	public static final int BULLSEYE_MATERIAL_OFFSET_ORIGIN = 0;
	
	/**
	 * The offset for the scale in a compiled {@link BullseyeMaterial} instance.
	 */
	public static final int BULLSEYE_MATERIAL_OFFSET_SCALE = 5;
	
	/**
	 * The length of a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_LENGTH = 8;
	
	/**
	 * The offset for the angle in degrees in a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_OFFSET_ANGLE_DEGREES = 0;
	
	/**
	 * The offset for the angle in radians in a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_OFFSET_ANGLE_RADIANS = 1;
	
	/**
	 * The offset for the {@link Material} denoted by {@code A} in a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_A = 2;
	
	/**
	 * The offset for the {@link Material} denoted by {@code B} in a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_B = 3;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link CheckerboardMaterial} instance.
	 */
	public static final int CHECKERBOARD_MATERIAL_OFFSET_SCALE = 4;
	
	/**
	 * The length of a compiled {@link ClearCoatMaterial} instance.
	 */
	public static final int CLEAR_COAT_MATERIAL_LENGTH = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KD} and {@code KS} in a compiled {@link ClearCoatMaterial} instance.
	 */
	public static final int CLEAR_COAT_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S = 1;
	
	/**
	 * The length of a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_LENGTH = 16;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Clear Coat} and {@code Clear Coat Gloss} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Color} and {@code Diffuse Transmission} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION = 3;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code Anisotropic} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_ANISOTROPIC = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Eta} and {@code Flatness} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS = 4;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Metallic} and {@code Roughness} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS = 5;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Scatter Distance} and {@code Sheen} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN = 6;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Sheen Tint} and {@code Specular Tint} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT = 7;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code Specular Transmission} and the thin flag in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN = 8;
	
	/**
	 * The length of a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_LENGTH = 8;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 4;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code Eta} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_ETA = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KR} and {@code KT} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 3;
	
	/**
	 * The length of a compiled {@link GlossyMaterial} instance.
	 */
	public static final int GLOSSY_MATERIAL_LENGTH = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KR} and {@code Roughness} in a compiled {@link GlossyMaterial} instance.
	 */
	public static final int GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instance denoted by {@code Emission} and the {@link Modifier} instance in a compiled {@link Material} instance.
	 */
	public static final int MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER = 0;
	
	/**
	 * The length of a compiled {@link MatteMaterial} instance.
	 */
	public static final int MATTE_MATERIAL_LENGTH = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Angle} and {@code KD} in a compiled {@link MatteMaterial} instance.
	 */
	public static final int MATTE_MATERIAL_OFFSET_TEXTURE_ANGLE_AND_TEXTURE_K_D = 1;
	
	/**
	 * The length of a compiled {@link MetalMaterial} instance.
	 */
	public static final int METAL_MATERIAL_LENGTH = 4;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link MetalMaterial} instance.
	 */
	public static final int METAL_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Eta} and {@code K} in a compiled {@link MetalMaterial} instance.
	 */
	public static final int METAL_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_K = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in a compiled {@link MetalMaterial} instance.
	 */
	public static final int METAL_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 2;
	
	/**
	 * The length of a compiled {@link MirrorMaterial} instance.
	 */
	public static final int MIRROR_MATERIAL_LENGTH = 2;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code KR} in a compiled {@link MirrorMaterial} instance.
	 */
	public static final int MIRROR_MATERIAL_OFFSET_TEXTURE_K_R = 1;
	
	/**
	 * The length of a compiled {@link PlasticMaterial} instance.
	 */
	public static final int PLASTIC_MATERIAL_LENGTH = 4;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link PlasticMaterial} instance.
	 */
	public static final int PLASTIC_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KD} and {@code KS} in a compiled {@link PlasticMaterial} instance.
	 */
	public static final int PLASTIC_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S = 1;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code Roughness} in a compiled {@link PlasticMaterial} instance.
	 */
	public static final int PLASTIC_MATERIAL_OFFSET_TEXTURE_ROUGHNESS = 2;
	
	/**
	 * The length of a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_LENGTH = 8;
	
	/**
	 * The offset for the angle in degrees in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_ANGLE_DEGREES = 0;
	
	/**
	 * The offset for the angle in radians in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_ANGLE_RADIANS = 1;
	
	/**
	 * The offset for the cell resolution in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_CELL_RESOLUTION = 4;
	
	/**
	 * The offset for the {@link Material} denoted by {@code A} in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_MATERIAL_A = 2;
	
	/**
	 * The offset for the {@link Material} denoted by {@code B} in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_MATERIAL_B = 3;
	
	/**
	 * The offset for the polka dot radius in a compiled {@link PolkaDotMaterial} instance.
	 */
	public static final int POLKA_DOT_MATERIAL_OFFSET_POLKA_DOT_RADIUS = 5;
	
	/**
	 * The length of a compiled {@link SubstrateMaterial} instance.
	 */
	public static final int SUBSTRATE_MATERIAL_LENGTH = 4;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link SubstrateMaterial} instance.
	 */
	public static final int SUBSTRATE_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KD} and {@code KS} in a compiled {@link SubstrateMaterial} instance.
	 */
	public static final int SUBSTRATE_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in a compiled {@link SubstrateMaterial} instance.
	 */
	public static final int SUBSTRATE_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 2;
	
	/**
	 * The length of a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_LENGTH = 8;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KD} and {@code KR} in a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_R = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KS} and {@code KT} in a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_OFFSET_TEXTURE_K_S_AND_TEXTURE_K_T = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Eta} and {@code Opacity} in a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_OPACITY = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 4;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link UberMaterial} instance.
	 */
	public static final int UBER_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 5;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] bullseyeMaterials;
	private float[] checkerboardMaterials;
	private float[] polkaDotMaterials;
	private int[] clearCoatMaterials;
	private int[] disneyMaterials;
	private int[] glassMaterials;
	private int[] glossyMaterials;
	private int[] matteMaterials;
	private int[] metalMaterials;
	private int[] mirrorMaterials;
	private int[] plasticMaterials;
	private int[] substrateMaterials;
	private int[] uberMaterials;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledMaterialCache} instance.
	 */
	public CompiledMaterialCache() {
		setBullseyeMaterials(new float[0]);
		setCheckerboardMaterials(new float[0]);
		setClearCoatMaterials(new int[0]);
		setDisneyMaterials(new int[0]);
		setGlassMaterials(new int[0]);
		setGlossyMaterials(new int[0]);
		setMatteMaterials(new int[0]);
		setMetalMaterials(new int[0]);
		setMirrorMaterials(new int[0]);
		setPlasticMaterials(new int[0]);
		setPolkaDotMaterials(new float[0]);
		setSubstrateMaterials(new int[0]);
		setUberMaterials(new int[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code bullseyeMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code bullseyeMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code bullseyeMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public boolean removeBullseyeMaterial(final float[] bullseyeMaterial) {
		final int absoluteOffset = getBullseyeMaterialOffsetAbsolute(bullseyeMaterial);
		
		if(absoluteOffset != -1) {
			setBullseyeMaterials(Arrays.splice(getBullseyeMaterials(), absoluteOffset, BULLSEYE_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code checkerboardMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code checkerboardMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code checkerboardMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public boolean removeCheckerboardMaterial(final float[] checkerboardMaterial) {
		final int absoluteOffset = getCheckerboardMaterialOffsetAbsolute(checkerboardMaterial);
		
		if(absoluteOffset != -1) {
			setCheckerboardMaterials(Arrays.splice(getCheckerboardMaterials(), absoluteOffset, CHECKERBOARD_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code clearCoatMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code clearCoatMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code clearCoatMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public boolean removeClearCoatMaterial(final int[] clearCoatMaterial) {
		final int absoluteOffset = getClearCoatMaterialOffsetAbsolute(clearCoatMaterial);
		
		if(absoluteOffset != -1) {
			setClearCoatMaterials(Arrays.splice(getClearCoatMaterials(), absoluteOffset, CLEAR_COAT_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code disneyMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code disneyMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code disneyMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public boolean removeDisneyMaterial(final int[] disneyMaterial) {
		final int absoluteOffset = getDisneyMaterialOffsetAbsolute(disneyMaterial);
		
		if(absoluteOffset != -1) {
			setDisneyMaterials(Arrays.splice(getDisneyMaterials(), absoluteOffset, DISNEY_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code glassMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code glassMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code glassMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public boolean removeGlassMaterial(final int[] glassMaterial) {
		final int absoluteOffset = getGlassMaterialOffsetAbsolute(glassMaterial);
		
		if(absoluteOffset != -1) {
			setGlassMaterials(Arrays.splice(getGlassMaterials(), absoluteOffset, GLASS_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code glossyMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code glossyMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code glossyMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public boolean removeGlossyMaterial(final int[] glossyMaterial) {
		final int absoluteOffset = getGlossyMaterialOffsetAbsolute(glossyMaterial);
		
		if(absoluteOffset != -1) {
			setGlossyMaterials(Arrays.splice(getGlossyMaterials(), absoluteOffset, GLOSSY_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code matteMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code matteMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code matteMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public boolean removeMatteMaterial(final int[] matteMaterial) {
		final int absoluteOffset = getMatteMaterialOffsetAbsolute(matteMaterial);
		
		if(absoluteOffset != -1) {
			setMatteMaterials(Arrays.splice(getMatteMaterials(), absoluteOffset, MATTE_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code metalMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code metalMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code metalMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public boolean removeMetalMaterial(final int[] metalMaterial) {
		final int absoluteOffset = getMetalMaterialOffsetAbsolute(metalMaterial);
		
		if(absoluteOffset != -1) {
			setMetalMaterials(Arrays.splice(getMetalMaterials(), absoluteOffset, METAL_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code mirrorMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code mirrorMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code mirrorMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public boolean removeMirrorMaterial(final int[] mirrorMaterial) {
		final int absoluteOffset = getMirrorMaterialOffsetAbsolute(mirrorMaterial);
		
		if(absoluteOffset != -1) {
			setMirrorMaterials(Arrays.splice(getMirrorMaterials(), absoluteOffset, MIRROR_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code plasticMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code plasticMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code plasticMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public boolean removePlasticMaterial(final int[] plasticMaterial) {
		final int absoluteOffset = getPlasticMaterialOffsetAbsolute(plasticMaterial);
		
		if(absoluteOffset != -1) {
			setPlasticMaterials(Arrays.splice(getPlasticMaterials(), absoluteOffset, PLASTIC_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code polkaDotMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code polkaDotMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code polkaDotMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public boolean removePolkaDotMaterial(final float[] polkaDotMaterial) {
		final int absoluteOffset = getPolkaDotMaterialOffsetAbsolute(polkaDotMaterial);
		
		if(absoluteOffset != -1) {
			setPolkaDotMaterials(Arrays.splice(getPolkaDotMaterials(), absoluteOffset, POLKA_DOT_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code substrateMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code substrateMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code substrateMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public boolean removeSubstrateMaterial(final int[] substrateMaterial) {
		final int absoluteOffset = getSubstrateMaterialOffsetAbsolute(substrateMaterial);
		
		if(absoluteOffset != -1) {
			setSubstrateMaterials(Arrays.splice(getSubstrateMaterials(), absoluteOffset, SUBSTRATE_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code uberMaterial} from this {@code CompiledMaterialCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code uberMaterial} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code uberMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance in compiled form
	 * @return {@code true} if, and only if, {@code uberMaterial} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterial} is {@code null}
	 */
	public boolean removeUberMaterial(final int[] uberMaterial) {
		final int absoluteOffset = getUberMaterialOffsetAbsolute(uberMaterial);
		
		if(absoluteOffset != -1) {
			setUberMaterials(Arrays.splice(getUberMaterials(), absoluteOffset, UBER_MATERIAL_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getBullseyeMaterials() {
		return this.bullseyeMaterials;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getCheckerboardMaterials() {
		return this.checkerboardMaterials;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getPolkaDotMaterials() {
		return this.polkaDotMaterials;
	}
	
	/**
	 * Adds {@code bullseyeMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code bullseyeMaterial}.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the relative offset to {@code bullseyeMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public int addBullseyeMaterial(final float[] bullseyeMaterial) {
		final int relativeOffsetOld = getBullseyeMaterialOffsetRelative(bullseyeMaterial);
		final int relativeOffsetNew = getBullseyeMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setBullseyeMaterials(Arrays.merge(getBullseyeMaterials(), bullseyeMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code checkerboardMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code checkerboardMaterial}.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the relative offset to {@code checkerboardMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public int addCheckerboardMaterial(final float[] checkerboardMaterial) {
		final int relativeOffsetOld = getCheckerboardMaterialOffsetRelative(checkerboardMaterial);
		final int relativeOffsetNew = getCheckerboardMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setCheckerboardMaterials(Arrays.merge(getCheckerboardMaterials(), checkerboardMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code clearCoatMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code clearCoatMaterial}.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the relative offset to {@code clearCoatMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public int addClearCoatMaterial(final int[] clearCoatMaterial) {
		final int relativeOffsetOld = getClearCoatMaterialOffsetRelative(clearCoatMaterial);
		final int relativeOffsetNew = getClearCoatMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setClearCoatMaterials(Arrays.merge(getClearCoatMaterials(), clearCoatMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code disneyMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code disneyMaterial}.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the relative offset to {@code disneyMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public int addDisneyMaterial(final int[] disneyMaterial) {
		final int relativeOffsetOld = getDisneyMaterialOffsetRelative(disneyMaterial);
		final int relativeOffsetNew = getDisneyMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setDisneyMaterials(Arrays.merge(getDisneyMaterials(), disneyMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code glassMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code glassMaterial}.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the relative offset to {@code glassMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public int addGlassMaterial(final int[] glassMaterial) {
		final int relativeOffsetOld = getGlassMaterialOffsetRelative(glassMaterial);
		final int relativeOffsetNew = getGlassMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setGlassMaterials(Arrays.merge(getGlassMaterials(), glassMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code glossyMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code glossyMaterial}.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the relative offset to {@code glossyMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public int addGlossyMaterial(final int[] glossyMaterial) {
		final int relativeOffsetOld = getGlossyMaterialOffsetRelative(glossyMaterial);
		final int relativeOffsetNew = getGlossyMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setGlossyMaterials(Arrays.merge(getGlossyMaterials(), glossyMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code matteMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code matteMaterial}.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the relative offset to {@code matteMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public int addMatteMaterial(final int[] matteMaterial) {
		final int relativeOffsetOld = getMatteMaterialOffsetRelative(matteMaterial);
		final int relativeOffsetNew = getMatteMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setMatteMaterials(Arrays.merge(getMatteMaterials(), matteMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code metalMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code metalMaterial}.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the relative offset to {@code metalMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public int addMetalMaterial(final int[] metalMaterial) {
		final int relativeOffsetOld = getMetalMaterialOffsetRelative(metalMaterial);
		final int relativeOffsetNew = getMetalMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setMetalMaterials(Arrays.merge(getMetalMaterials(), metalMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code mirrorMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code mirrorMaterial}.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the relative offset to {@code mirrorMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public int addMirrorMaterial(final int[] mirrorMaterial) {
		final int relativeOffsetOld = getMirrorMaterialOffsetRelative(mirrorMaterial);
		final int relativeOffsetNew = getMirrorMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setMirrorMaterials(Arrays.merge(getMirrorMaterials(), mirrorMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code plasticMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code plasticMaterial}.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the relative offset to {@code plasticMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public int addPlasticMaterial(final int[] plasticMaterial) {
		final int relativeOffsetOld = getPlasticMaterialOffsetRelative(plasticMaterial);
		final int relativeOffsetNew = getPlasticMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setPlasticMaterials(Arrays.merge(getPlasticMaterials(), plasticMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code polkaDotMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code polkaDotMaterial}.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the relative offset to {@code polkaDotMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public int addPolkaDotMaterial(final float[] polkaDotMaterial) {
		final int relativeOffsetOld = getPolkaDotMaterialOffsetRelative(polkaDotMaterial);
		final int relativeOffsetNew = getPolkaDotMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setPolkaDotMaterials(Arrays.merge(getPolkaDotMaterials(), polkaDotMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code substrateMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code substrateMaterial}.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the relative offset to {@code substrateMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public int addSubstrateMaterial(final int[] substrateMaterial) {
		final int relativeOffsetOld = getSubstrateMaterialOffsetRelative(substrateMaterial);
		final int relativeOffsetNew = getSubstrateMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setSubstrateMaterials(Arrays.merge(getSubstrateMaterials(), substrateMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code uberMaterial} to this {@code CompiledMaterialCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code uberMaterial}.
	 * <p>
	 * If {@code uberMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance in compiled form
	 * @return the relative offset to {@code uberMaterial}
	 * @throws IllegalArgumentException thrown if, and only if, {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterial} is {@code null}
	 */
	public int addUberMaterial(final int[] uberMaterial) {
		final int relativeOffsetOld = getUberMaterialOffsetRelative(uberMaterial);
		final int relativeOffsetNew = getUberMaterialCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setUberMaterials(Arrays.merge(getUberMaterials(), uberMaterial));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link BullseyeMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code BullseyeMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getBullseyeMaterialCount() {
		return Structures.getStructureCount(this.bullseyeMaterials, BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the absolute offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public int getBullseyeMaterialOffsetAbsolute(final float[] bullseyeMaterial) {
		Objects.requireNonNull(bullseyeMaterial, "bullseyeMaterial == null");
		
		ParameterArguments.requireExactArrayLength(bullseyeMaterial, BULLSEYE_MATERIAL_LENGTH, "bullseyeMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.bullseyeMaterials, bullseyeMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the relative offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeMaterial.length} is not equal to {@code CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public int getBullseyeMaterialOffsetRelative(final float[] bullseyeMaterial) {
		Objects.requireNonNull(bullseyeMaterial, "bullseyeMaterial == null");
		
		ParameterArguments.requireExactArrayLength(bullseyeMaterial, BULLSEYE_MATERIAL_LENGTH, "bullseyeMaterial");
		
		return Structures.getStructureOffsetRelative(this.bullseyeMaterials, bullseyeMaterial);
	}
	
	/**
	 * Returns the {@link CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getCheckerboardMaterialCount() {
		return Structures.getStructureCount(this.checkerboardMaterials, CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the absolute offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public int getCheckerboardMaterialOffsetAbsolute(final float[] checkerboardMaterial) {
		Objects.requireNonNull(checkerboardMaterial, "checkerboardMaterial == null");
		
		ParameterArguments.requireExactArrayLength(checkerboardMaterial, CHECKERBOARD_MATERIAL_LENGTH, "checkerboardMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.checkerboardMaterials, checkerboardMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the relative offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardMaterial.length} is not equal to {@code CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public int getCheckerboardMaterialOffsetRelative(final float[] checkerboardMaterial) {
		Objects.requireNonNull(checkerboardMaterial, "checkerboardMaterial == null");
		
		ParameterArguments.requireExactArrayLength(checkerboardMaterial, CHECKERBOARD_MATERIAL_LENGTH, "checkerboardMaterial");
		
		return Structures.getStructureOffsetRelative(this.checkerboardMaterials, checkerboardMaterial);
	}
	
	/**
	 * Returns the {@link ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getClearCoatMaterialCount() {
		return Structures.getStructureCount(this.clearCoatMaterials, CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the absolute offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public int getClearCoatMaterialOffsetAbsolute(final int[] clearCoatMaterial) {
		Objects.requireNonNull(clearCoatMaterial, "clearCoatMaterial == null");
		
		ParameterArguments.requireExactArrayLength(clearCoatMaterial, CLEAR_COAT_MATERIAL_LENGTH, "clearCoatMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.clearCoatMaterials, clearCoatMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the relative offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code clearCoatMaterial.length} is not equal to {@code CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public int getClearCoatMaterialOffsetRelative(final int[] clearCoatMaterial) {
		Objects.requireNonNull(clearCoatMaterial, "clearCoatMaterial == null");
		
		ParameterArguments.requireExactArrayLength(clearCoatMaterial, CLEAR_COAT_MATERIAL_LENGTH, "clearCoatMaterial");
		
		return Structures.getStructureOffsetRelative(this.clearCoatMaterials, clearCoatMaterial);
	}
	
	/**
	 * Returns the {@link DisneyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code DisneyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getDisneyMaterialCount() {
		return Structures.getStructureCount(this.disneyMaterials, DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the absolute offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public int getDisneyMaterialOffsetAbsolute(final int[] disneyMaterial) {
		Objects.requireNonNull(disneyMaterial, "disneyMaterial == null");
		
		ParameterArguments.requireExactArrayLength(disneyMaterial, DISNEY_MATERIAL_LENGTH, "disneyMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.disneyMaterials, disneyMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the relative offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code disneyMaterial.length} is not equal to {@code CompiledMaterialCache.DISNEY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public int getDisneyMaterialOffsetRelative(final int[] disneyMaterial) {
		Objects.requireNonNull(disneyMaterial, "disneyMaterial == null");
		
		ParameterArguments.requireExactArrayLength(disneyMaterial, DISNEY_MATERIAL_LENGTH, "disneyMaterial");
		
		return Structures.getStructureOffsetRelative(this.disneyMaterials, disneyMaterial);
	}
	
	/**
	 * Returns the {@link GlassMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlassMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getGlassMaterialCount() {
		return Structures.getStructureCount(this.glassMaterials, GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the absolute offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public int getGlassMaterialOffsetAbsolute(final int[] glassMaterial) {
		Objects.requireNonNull(glassMaterial, "glassMaterial == null");
		
		ParameterArguments.requireExactArrayLength(glassMaterial, GLASS_MATERIAL_LENGTH, "glassMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.glassMaterials, glassMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the relative offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code glassMaterial.length} is not equal to {@code CompiledMaterialCache.GLASS_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public int getGlassMaterialOffsetRelative(final int[] glassMaterial) {
		Objects.requireNonNull(glassMaterial, "glassMaterial == null");
		
		ParameterArguments.requireExactArrayLength(glassMaterial, GLASS_MATERIAL_LENGTH, "glassMaterial");
		
		return Structures.getStructureOffsetRelative(this.glassMaterials, glassMaterial);
	}
	
	/**
	 * Returns the {@link GlossyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlossyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getGlossyMaterialCount() {
		return Structures.getStructureCount(this.glossyMaterials, GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the absolute offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public int getGlossyMaterialOffsetAbsolute(final int[] glossyMaterial) {
		Objects.requireNonNull(glossyMaterial, "glossyMaterial == null");
		
		ParameterArguments.requireExactArrayLength(glossyMaterial, GLOSSY_MATERIAL_LENGTH, "glossyMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.glossyMaterials, glossyMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the relative offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code glossyMaterial.length} is not equal to {@code CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public int getGlossyMaterialOffsetRelative(final int[] glossyMaterial) {
		Objects.requireNonNull(glossyMaterial, "glossyMaterial == null");
		
		ParameterArguments.requireExactArrayLength(glossyMaterial, GLOSSY_MATERIAL_LENGTH, "glossyMaterial");
		
		return Structures.getStructureOffsetRelative(this.glossyMaterials, glossyMaterial);
	}
	
	/**
	 * Returns the {@link MatteMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MatteMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMatteMaterialCount() {
		return Structures.getStructureCount(this.matteMaterials, MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the absolute offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public int getMatteMaterialOffsetAbsolute(final int[] matteMaterial) {
		Objects.requireNonNull(matteMaterial, "matteMaterial == null");
		
		ParameterArguments.requireExactArrayLength(matteMaterial, MATTE_MATERIAL_LENGTH, "matteMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.matteMaterials, matteMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the relative offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code matteMaterial.length} is not equal to {@code CompiledMaterialCache.MATTE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public int getMatteMaterialOffsetRelative(final int[] matteMaterial) {
		Objects.requireNonNull(matteMaterial, "matteMaterial == null");
		
		ParameterArguments.requireExactArrayLength(matteMaterial, MATTE_MATERIAL_LENGTH, "matteMaterial");
		
		return Structures.getStructureOffsetRelative(this.matteMaterials, matteMaterial);
	}
	
	/**
	 * Returns the {@link MetalMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MetalMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMetalMaterialCount() {
		return Structures.getStructureCount(this.metalMaterials, METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the absolute offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public int getMetalMaterialOffsetAbsolute(final int[] metalMaterial) {
		Objects.requireNonNull(metalMaterial, "metalMaterial == null");
		
		ParameterArguments.requireExactArrayLength(metalMaterial, METAL_MATERIAL_LENGTH, "metalMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.metalMaterials, metalMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the relative offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code metalMaterial.length} is not equal to {@code CompiledMaterialCache.METAL_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public int getMetalMaterialOffsetRelative(final int[] metalMaterial) {
		Objects.requireNonNull(metalMaterial, "metalMaterial == null");
		
		ParameterArguments.requireExactArrayLength(metalMaterial, METAL_MATERIAL_LENGTH, "metalMaterial");
		
		return Structures.getStructureOffsetRelative(this.metalMaterials, metalMaterial);
	}
	
	/**
	 * Returns the {@link MirrorMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MirrorMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMirrorMaterialCount() {
		return Structures.getStructureCount(this.mirrorMaterials, MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the absolute offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public int getMirrorMaterialOffsetAbsolute(final int[] mirrorMaterial) {
		Objects.requireNonNull(mirrorMaterial, "mirrorMaterial == null");
		
		ParameterArguments.requireExactArrayLength(mirrorMaterial, MIRROR_MATERIAL_LENGTH, "mirrorMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.mirrorMaterials, mirrorMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the relative offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code mirrorMaterial.length} is not equal to {@code CompiledMaterialCache.MIRROR_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public int getMirrorMaterialOffsetRelative(final int[] mirrorMaterial) {
		Objects.requireNonNull(mirrorMaterial, "mirrorMaterial == null");
		
		ParameterArguments.requireExactArrayLength(mirrorMaterial, MIRROR_MATERIAL_LENGTH, "mirrorMaterial");
		
		return Structures.getStructureOffsetRelative(this.mirrorMaterials, mirrorMaterial);
	}
	
	/**
	 * Returns the {@link PlasticMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PlasticMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getPlasticMaterialCount() {
		return Structures.getStructureCount(this.plasticMaterials, PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the absolute offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public int getPlasticMaterialOffsetAbsolute(final int[] plasticMaterial) {
		Objects.requireNonNull(plasticMaterial, "plasticMaterial == null");
		
		ParameterArguments.requireExactArrayLength(plasticMaterial, PLASTIC_MATERIAL_LENGTH, "plasticMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.plasticMaterials, plasticMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the relative offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code plasticMaterial.length} is not equal to {@code CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public int getPlasticMaterialOffsetRelative(final int[] plasticMaterial) {
		Objects.requireNonNull(plasticMaterial, "plasticMaterial == null");
		
		ParameterArguments.requireExactArrayLength(plasticMaterial, PLASTIC_MATERIAL_LENGTH, "plasticMaterial");
		
		return Structures.getStructureOffsetRelative(this.plasticMaterials, plasticMaterial);
	}
	
	/**
	 * Returns the {@link PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getPolkaDotMaterialCount() {
		return Structures.getStructureCount(this.polkaDotMaterials, POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the absolute offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public int getPolkaDotMaterialOffsetAbsolute(final float[] polkaDotMaterial) {
		Objects.requireNonNull(polkaDotMaterial, "polkaDotMaterial == null");
		
		ParameterArguments.requireExactArrayLength(polkaDotMaterial, POLKA_DOT_MATERIAL_LENGTH, "polkaDotMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.polkaDotMaterials, polkaDotMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the relative offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotMaterial.length} is not equal to {@code CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public int getPolkaDotMaterialOffsetRelative(final float[] polkaDotMaterial) {
		Objects.requireNonNull(polkaDotMaterial, "polkaDotMaterial == null");
		
		ParameterArguments.requireExactArrayLength(polkaDotMaterial, POLKA_DOT_MATERIAL_LENGTH, "polkaDotMaterial");
		
		return Structures.getStructureOffsetRelative(this.polkaDotMaterials, polkaDotMaterial);
	}
	
	/**
	 * Returns the {@link SubstrateMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code SubstrateMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getSubstrateMaterialCount() {
		return Structures.getStructureCount(this.substrateMaterials, SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the absolute offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public int getSubstrateMaterialOffsetAbsolute(final int[] substrateMaterial) {
		Objects.requireNonNull(substrateMaterial, "substrateMaterial == null");
		
		ParameterArguments.requireExactArrayLength(substrateMaterial, SUBSTRATE_MATERIAL_LENGTH, "substrateMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.substrateMaterials, substrateMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the relative offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code substrateMaterial.length} is not equal to {@code CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public int getSubstrateMaterialOffsetRelative(final int[] substrateMaterial) {
		Objects.requireNonNull(substrateMaterial, "substrateMaterial == null");
		
		ParameterArguments.requireExactArrayLength(substrateMaterial, SUBSTRATE_MATERIAL_LENGTH, "substrateMaterial");
		
		return Structures.getStructureOffsetRelative(this.substrateMaterials, substrateMaterial);
	}
	
	/**
	 * Returns the {@link UberMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code UberMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getUberMaterialCount() {
		return Structures.getStructureCount(this.uberMaterials, UBER_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code uberMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code uberMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance in compiled form
	 * @return the absolute offset of {@code uberMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterial} is {@code null}
	 */
	public int getUberMaterialOffsetAbsolute(final int[] uberMaterial) {
		Objects.requireNonNull(uberMaterial, "uberMaterial == null");
		
		ParameterArguments.requireExactArrayLength(uberMaterial, UBER_MATERIAL_LENGTH, "uberMaterial");
		
		return Structures.getStructureOffsetAbsolute(this.uberMaterials, uberMaterial);
	}
	
	/**
	 * Returns the relative offset of {@code uberMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code uberMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance in compiled form
	 * @return the relative offset of {@code uberMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code uberMaterial.length} is not equal to {@code CompiledMaterialCache.UBER_MATERIAL_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterial} is {@code null}
	 */
	public int getUberMaterialOffsetRelative(final int[] uberMaterial) {
		Objects.requireNonNull(uberMaterial, "uberMaterial == null");
		
		ParameterArguments.requireExactArrayLength(uberMaterial, UBER_MATERIAL_LENGTH, "uberMaterial");
		
		return Structures.getStructureOffsetRelative(this.uberMaterials, uberMaterial);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getClearCoatMaterials() {
		return this.clearCoatMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getDisneyMaterials() {
		return this.disneyMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getGlassMaterials() {
		return this.glassMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getGlossyMaterials() {
		return this.glossyMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMatteMaterials() {
		return this.matteMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMetalMaterials() {
		return this.metalMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMirrorMaterials() {
		return this.mirrorMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getPlasticMaterials() {
		return this.plasticMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getSubstrateMaterials() {
		return this.substrateMaterials;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link UberMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code UberMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getUberMaterials() {
		return this.uberMaterials;
	}
	
	/**
	 * Sets all {@link BullseyeMaterial} instances in compiled form to {@code bullseyeMaterials}.
	 * <p>
	 * If {@code bullseyeMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeMaterials.length % CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeMaterials the {@code BullseyeMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeMaterials.length % CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterials} is {@code null}
	 */
	public void setBullseyeMaterials(final float[] bullseyeMaterials) {
		Objects.requireNonNull(bullseyeMaterials, "bullseyeMaterials == null");
		
		ParameterArguments.requireExact(bullseyeMaterials.length % BULLSEYE_MATERIAL_LENGTH, 0, "bullseyeMaterials.length % CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH");
		
		this.bullseyeMaterials = bullseyeMaterials;
	}
	
	/**
	 * Sets all {@link CheckerboardMaterial} instances in compiled form to {@code checkerboardMaterials}.
	 * <p>
	 * If {@code checkerboardMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardMaterials.length % CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardMaterials the {@code CheckerboardMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardMaterials.length % CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterials} is {@code null}
	 */
	public void setCheckerboardMaterials(final float[] checkerboardMaterials) {
		Objects.requireNonNull(checkerboardMaterials, "checkerboardMaterials == null");
		
		ParameterArguments.requireExact(checkerboardMaterials.length % CHECKERBOARD_MATERIAL_LENGTH, 0, "checkerboardMaterials.length % CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH");
		
		this.checkerboardMaterials = checkerboardMaterials;
	}
	
	/**
	 * Sets all {@link ClearCoatMaterial} instances in compiled form to {@code clearCoatMaterials}.
	 * <p>
	 * If {@code clearCoatMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code clearCoatMaterials.length % CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param clearCoatMaterials the {@code ClearCoatMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code clearCoatMaterials.length % CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterials} is {@code null}
	 */
	public void setClearCoatMaterials(final int[] clearCoatMaterials) {
		Objects.requireNonNull(clearCoatMaterials, "clearCoatMaterials == null");
		
		ParameterArguments.requireExact(clearCoatMaterials.length % CLEAR_COAT_MATERIAL_LENGTH, 0, "clearCoatMaterials.length % CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH");
		
		this.clearCoatMaterials = clearCoatMaterials;
	}
	
	/**
	 * Sets all {@link DisneyMaterial} instances in compiled form to {@code disneyMaterials}.
	 * <p>
	 * If {@code disneyMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code disneyMaterials.length % CompiledMaterialCache.DISNEY_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param disneyMaterials the {@code DisneyMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code disneyMaterials.length % CompiledMaterialCache.DISNEY_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterials} is {@code null}
	 */
	public void setDisneyMaterials(final int[] disneyMaterials) {
		Objects.requireNonNull(disneyMaterials, "disneyMaterials == null");
		
		ParameterArguments.requireExact(disneyMaterials.length % DISNEY_MATERIAL_LENGTH, 0, "disneyMaterials.length % CompiledMaterialCache.DISNEY_MATERIAL_LENGTH");
		
		this.disneyMaterials = disneyMaterials;
	}
	
	/**
	 * Sets all {@link GlassMaterial} instances in compiled form to {@code glassMaterials}.
	 * <p>
	 * If {@code glassMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glassMaterials.length % CompiledMaterialCache.GLASS_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glassMaterials the {@code GlassMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code glassMaterials.length % CompiledMaterialCache.GLASS_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterials} is {@code null}
	 */
	public void setGlassMaterials(final int[] glassMaterials) {
		Objects.requireNonNull(glassMaterials, "glassMaterials == null");
		
		ParameterArguments.requireExact(glassMaterials.length % GLASS_MATERIAL_LENGTH, 0, "glassMaterials.length % CompiledMaterialCache.GLASS_MATERIAL_LENGTH");
		
		this.glassMaterials = glassMaterials;
	}
	
	/**
	 * Sets all {@link GlossyMaterial} instances in compiled form to {@code glossyMaterials}.
	 * <p>
	 * If {@code glossyMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code glossyMaterials.length % CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param glossyMaterials the {@code GlossyMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code glossyMaterials.length % CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterials} is {@code null}
	 */
	public void setGlossyMaterials(final int[] glossyMaterials) {
		Objects.requireNonNull(glossyMaterials, "glossyMaterials == null");
		
		ParameterArguments.requireExact(glossyMaterials.length % GLOSSY_MATERIAL_LENGTH, 0, "glossyMaterials.length % CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH");
		
		this.glossyMaterials = glossyMaterials;
	}
	
	/**
	 * Sets all {@link MatteMaterial} instances in compiled form to {@code matteMaterials}.
	 * <p>
	 * If {@code matteMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code matteMaterials.length % CompiledMaterialCache.MATTE_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param matteMaterials the {@code MatteMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code matteMaterials.length % CompiledMaterialCache.MATTE_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterials} is {@code null}
	 */
	public void setMatteMaterials(final int[] matteMaterials) {
		Objects.requireNonNull(matteMaterials, "matteMaterials == null");
		
		ParameterArguments.requireExact(matteMaterials.length % MATTE_MATERIAL_LENGTH, 0, "matteMaterials.length % CompiledMaterialCache.MATTE_MATERIAL_LENGTH");
		
		this.matteMaterials = matteMaterials;
	}
	
	/**
	 * Sets all {@link MetalMaterial} instances in compiled form to {@code metalMaterials}.
	 * <p>
	 * If {@code metalMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code metalMaterials.length % CompiledMaterialCache.METAL_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param metalMaterials the {@code MetalMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code metalMaterials.length % CompiledMaterialCache.METAL_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterials} is {@code null}
	 */
	public void setMetalMaterials(final int[] metalMaterials) {
		Objects.requireNonNull(metalMaterials, "metalMaterials == null");
		
		ParameterArguments.requireExact(metalMaterials.length % METAL_MATERIAL_LENGTH, 0, "metalMaterials.length % CompiledMaterialCache.METAL_MATERIAL_LENGTH");
		
		this.metalMaterials = metalMaterials;
	}
	
	/**
	 * Sets all {@link MirrorMaterial} instances in compiled form to {@code mirrorMaterials}.
	 * <p>
	 * If {@code mirrorMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code mirrorMaterials.length % CompiledMaterialCache.MIRROR_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mirrorMaterials the {@code MirrorMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code mirrorMaterials.length % CompiledMaterialCache.MIRROR_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterials} is {@code null}
	 */
	public void setMirrorMaterials(final int[] mirrorMaterials) {
		Objects.requireNonNull(mirrorMaterials, "mirrorMaterials == null");
		
		ParameterArguments.requireExact(mirrorMaterials.length % MIRROR_MATERIAL_LENGTH, 0, "mirrorMaterials.length % CompiledMaterialCache.MIRROR_MATERIAL_LENGTH");
		
		this.mirrorMaterials = mirrorMaterials;
	}
	
	/**
	 * Sets all {@link PlasticMaterial} instances in compiled form to {@code plasticMaterials}.
	 * <p>
	 * If {@code plasticMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code plasticMaterials.length % CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param plasticMaterials the {@code PlasticMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code plasticMaterials.length % CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterials} is {@code null}
	 */
	public void setPlasticMaterials(final int[] plasticMaterials) {
		Objects.requireNonNull(plasticMaterials, "plasticMaterials == null");
		
		ParameterArguments.requireExact(plasticMaterials.length % PLASTIC_MATERIAL_LENGTH, 0, "plasticMaterials.length % CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH");
		
		this.plasticMaterials = plasticMaterials;
	}
	
	/**
	 * Sets all {@link PolkaDotMaterial} instances in compiled form to {@code polkaDotMaterials}.
	 * <p>
	 * If {@code polkaDotMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotMaterials.length % CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotMaterials the {@code PolkaDotMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotMaterials.length % CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterials} is {@code null}
	 */
	public void setPolkaDotMaterials(final float[] polkaDotMaterials) {
		Objects.requireNonNull(polkaDotMaterials, "polkaDotMaterials == null");
		
		ParameterArguments.requireExact(polkaDotMaterials.length % POLKA_DOT_MATERIAL_LENGTH, 0, "polkaDotMaterials.length % CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH");
		
		this.polkaDotMaterials = polkaDotMaterials;
	}
	
	/**
	 * Sets all {@link SubstrateMaterial} instances in compiled form to {@code substrateMaterials}.
	 * <p>
	 * If {@code substrateMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code substrateMaterials.length % CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param substrateMaterials the {@code SubstrateMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code substrateMaterials.length % CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterials} is {@code null}
	 */
	public void setSubstrateMaterials(final int[] substrateMaterials) {
		Objects.requireNonNull(substrateMaterials, "substrateMaterials == null");
		
		ParameterArguments.requireExact(substrateMaterials.length % SUBSTRATE_MATERIAL_LENGTH, 0, "substrateMaterials.length % CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH");
		
		this.substrateMaterials = substrateMaterials;
	}
	
	/**
	 * Sets all {@link UberMaterial} instances in compiled form to {@code uberMaterials}.
	 * <p>
	 * If {@code uberMaterials} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code uberMaterials.length % CompiledMaterialCache.UBER_MATERIAL_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param uberMaterials the {@code UberMaterial} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code uberMaterials.length % CompiledMaterialCache.UBER_MATERIAL_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterials} is {@code null}
	 */
	public void setUberMaterials(final int[] uberMaterials) {
		Objects.requireNonNull(uberMaterials, "uberMaterials == null");
		
		ParameterArguments.requireExact(uberMaterials.length % UBER_MATERIAL_LENGTH, 0, "uberMaterials.length % CompiledMaterialCache.UBER_MATERIAL_LENGTH");
		
		this.uberMaterials = uberMaterials;
	}
	
	/**
	 * Writes this {@code CompiledMaterialCache} instance to {@code document}.
	 * <p>
	 * If {@code document} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param document a {@link Document} instance
	 * @throws NullPointerException thrown if, and only if, {@code document} is {@code null}
	 */
	public void write(final Document document) {
		document.line("CompiledMaterialCache {");
		document.indent();
		document.linef("bullseyeMaterials[%d]", Integer.valueOf(getBullseyeMaterialCount()));
		document.linef("checkerboardMaterials[%d]", Integer.valueOf(getCheckerboardMaterialCount()));
		document.linef("clearCoatMaterials[%d]", Integer.valueOf(getClearCoatMaterialCount()));
		document.linef("disneyMaterials[%d]", Integer.valueOf(getDisneyMaterialCount()));
		document.linef("glassMaterials[%d]", Integer.valueOf(getGlassMaterialCount()));
		document.linef("glossyMaterials[%d]", Integer.valueOf(getGlossyMaterialCount()));
		document.linef("matteMaterials[%d]", Integer.valueOf(getMatteMaterialCount()));
		document.linef("metalMaterials[%d]", Integer.valueOf(getMetalMaterialCount()));
		document.linef("mirrorMaterials[%d]", Integer.valueOf(getMirrorMaterialCount()));
		document.linef("plasticMaterials[%d]", Integer.valueOf(getPlasticMaterialCount()));
		document.linef("polkaDotMaterials[%d]", Integer.valueOf(getPolkaDotMaterialCount()));
		document.linef("substrateMaterials[%d]", Integer.valueOf(getSubstrateMaterialCount()));
		document.linef("uberMaterials[%d]", Integer.valueOf(getUberMaterialCount()));
		document.outdent();
		document.line("}");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code material} and all of its associated {@link Material} or {@link Modifier} and {@link Texture} instances are supported, {@code false} otherwise.
	 * <p>
	 * If {@code material} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param material a {@code Material} instance
	 * @return {@code true} if, and only if, {@code material} and all of its associated {@code Material} or {@code Modifier} and {@code Texture} instances are supported, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code material} is {@code null}
	 */
	public static boolean isSupported(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		if(material instanceof BullseyeMaterial) {
			return doIsSupported(BullseyeMaterial.class.cast(material));
		} else if(material instanceof CheckerboardMaterial) {
			return doIsSupported(CheckerboardMaterial.class.cast(material));
		} else if(material instanceof ClearCoatMaterial) {
			return doIsSupported(ClearCoatMaterial.class.cast(material));
		} else if(material instanceof DisneyMaterial) {
			return doIsSupported(DisneyMaterial.class.cast(material));
		} else if(material instanceof GlassMaterial) {
			return doIsSupported(GlassMaterial.class.cast(material));
		} else if(material instanceof GlossyMaterial) {
			return doIsSupported(GlossyMaterial.class.cast(material));
		} else if(material instanceof MatteMaterial) {
			return doIsSupported(MatteMaterial.class.cast(material));
		} else if(material instanceof MetalMaterial) {
			return doIsSupported(MetalMaterial.class.cast(material));
		} else if(material instanceof MirrorMaterial) {
			return doIsSupported(MirrorMaterial.class.cast(material));
		} else if(material instanceof PlasticMaterial) {
			return doIsSupported(PlasticMaterial.class.cast(material));
		} else if(material instanceof PolkaDotMaterial) {
			return doIsSupported(PolkaDotMaterial.class.cast(material));
		} else if(material instanceof SubstrateMaterial) {
			return doIsSupported(SubstrateMaterial.class.cast(material));
		} else if(material instanceof UberMaterial) {
			return doIsSupported(UberMaterial.class.cast(material));
		} else {
			return false;
		}
	}
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeMaterial} in compiled form.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toBullseyeMaterial(bullseyeMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance
	 * @return a {@code float[]} with {@code bullseyeMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public static float[] toBullseyeMaterial(final BullseyeMaterial bullseyeMaterial) {
		return toBullseyeMaterial(bullseyeMaterial, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeMaterial} in compiled form.
	 * <p>
	 * If either {@code bullseyeMaterial} or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with {@code bullseyeMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code bullseyeMaterial} or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toBullseyeMaterial(final BullseyeMaterial bullseyeMaterial, final ToIntFunction<Material> materialOffsetFunction) {
		final Material materialA = bullseyeMaterial.getMaterialA();
		final Material materialB = bullseyeMaterial.getMaterialB();
		
		final Point3F origin = bullseyeMaterial.getOrigin();
		
		final float scale = bullseyeMaterial.getScale();
		
		final int materialAValue = pack(materialA.getID(), materialOffsetFunction.applyAsInt(materialA));
		final int materialBValue = pack(materialB.getID(), materialOffsetFunction.applyAsInt(materialB));
		
		final float[] array = new float[BULLSEYE_MATERIAL_LENGTH];
		
//		Because the BullseyeMaterial occupy 8/8 positions in a block, it should be aligned.
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 0] = origin.x;			//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 1] = origin.y;			//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 2] = origin.z;			//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_MATERIAL_A] = materialAValue;	//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_MATERIAL_B] = materialBValue;	//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_SCALE] = scale;					//Block #1
		array[6] = 0.0F;												//Block #1
		array[7] = 0.0F;												//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BullseyeMaterial} instances in {@code bullseyeMaterials} in compiled form.
	 * <p>
	 * If {@code bullseyeMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toBullseyeMaterials(bullseyeMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeMaterials a {@code List} of {@code BullseyeMaterial} instances
	 * @return a {@code float[]} with all {@code BullseyeMaterial} instances in {@code bullseyeMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toBullseyeMaterials(final List<BullseyeMaterial> bullseyeMaterials) {
		return toBullseyeMaterials(bullseyeMaterials, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BullseyeMaterial} instances in {@code bullseyeMaterials} in compiled form.
	 * <p>
	 * If either {@code bullseyeMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterials a {@code List} of {@code BullseyeMaterial} instances
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with all {@code BullseyeMaterial} instances in {@code bullseyeMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code bullseyeMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toBullseyeMaterials(final List<BullseyeMaterial> bullseyeMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Arrays.toFloatArray(bullseyeMaterials, bullseyeMaterial -> toBullseyeMaterial(bullseyeMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardMaterial} in compiled form.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toCheckerboardMaterial(checkerboardMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance
	 * @return a {@code float[]} with {@code checkerboardMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public static float[] toCheckerboardMaterial(final CheckerboardMaterial checkerboardMaterial) {
		return toCheckerboardMaterial(checkerboardMaterial, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardMaterial} in compiled form.
	 * <p>
	 * If either {@code checkerboardMaterial} or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with {@code checkerboardMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code checkerboardMaterial} or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toCheckerboardMaterial(final CheckerboardMaterial checkerboardMaterial, final ToIntFunction<Material> materialOffsetFunction) {
		final AngleF angle = checkerboardMaterial.getAngle();
		
		final Material materialA = checkerboardMaterial.getMaterialA();
		final Material materialB = checkerboardMaterial.getMaterialB();
		
		final Vector2F scale = checkerboardMaterial.getScale();
		
		final int materialAValue = pack(materialA.getID(), materialOffsetFunction.applyAsInt(materialA));
		final int materialBValue = pack(materialB.getID(), materialOffsetFunction.applyAsInt(materialB));
		
		final float[] array = new float[CHECKERBOARD_MATERIAL_LENGTH];
		
//		Because the CheckerboardMaterial occupy 8/8 positions in a block, it should be aligned.
		array[CHECKERBOARD_MATERIAL_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_A] = materialAValue;		//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_B] = materialBValue;		//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 0] = scale.x;				//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 1] = scale.y;				//Block #1
		array[6] = 0.0F;														//Block #1
		array[7] = 0.0F;														//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link CheckerboardMaterial} instances in {@code checkerboardMaterials} in compiled form.
	 * <p>
	 * If {@code checkerboardMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toCheckerboardMaterials(checkerboardMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardMaterials a {@code List} of {@code CheckerboardMaterial} instances
	 * @return a {@code float[]} with all {@code CheckerboardMaterial} instances in {@code checkerboardMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toCheckerboardMaterials(final List<CheckerboardMaterial> checkerboardMaterials) {
		return toCheckerboardMaterials(checkerboardMaterials, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link CheckerboardMaterial} instances in {@code checkerboardMaterials} in compiled form.
	 * <p>
	 * If either {@code checkerboardMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterials a {@code List} of {@code CheckerboardMaterial} instances
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with all {@code CheckerboardMaterial} instances in {@code checkerboardMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code checkerboardMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toCheckerboardMaterials(final List<CheckerboardMaterial> checkerboardMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Arrays.toFloatArray(checkerboardMaterials, checkerboardMaterial -> toCheckerboardMaterial(checkerboardMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotMaterial} in compiled form.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPolkaDotMaterial(polkaDotMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance
	 * @return a {@code float[]} with {@code polkaDotMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public static float[] toPolkaDotMaterial(final PolkaDotMaterial polkaDotMaterial) {
		return toPolkaDotMaterial(polkaDotMaterial, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotMaterial} in compiled form.
	 * <p>
	 * If either {@code polkaDotMaterial} or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with {@code polkaDotMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code polkaDotMaterial} or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toPolkaDotMaterial(final PolkaDotMaterial polkaDotMaterial, final ToIntFunction<Material> materialOffsetFunction) {
		final AngleF angle = polkaDotMaterial.getAngle();
		
		final Material materialA = polkaDotMaterial.getMaterialA();
		final Material materialB = polkaDotMaterial.getMaterialB();
		
		final float cellResolution = polkaDotMaterial.getCellResolution();
		final float polkaDotRadius = polkaDotMaterial.getPolkaDotRadius();
		
		final int materialAValue = pack(materialA.getID(), materialOffsetFunction.applyAsInt(materialA));
		final int materialBValue = pack(materialB.getID(), materialOffsetFunction.applyAsInt(materialB));
		
		final float[] array = new float[POLKA_DOT_MATERIAL_LENGTH];
		
//		Because the PolkaDotMaterial occupy 8/8 positions in a block, it should be aligned.
		array[POLKA_DOT_MATERIAL_OFFSET_ANGLE_DEGREES] = angle.getDegrees();//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_ANGLE_RADIANS] = angle.getRadians();//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_MATERIAL_A] = materialAValue;		//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_MATERIAL_B] = materialBValue;		//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_CELL_RESOLUTION] = cellResolution;	//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_POLKA_DOT_RADIUS] = polkaDotRadius;	//Block #1
		array[6] = 0.0F;													//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form.
	 * <p>
	 * If {@code polkaDotMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPolkaDotMaterials(polkaDotMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotMaterials a {@code List} of {@code PolkaDotMaterial} instances
	 * @return a {@code float[]} with all {@code PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toPolkaDotMaterials(final List<PolkaDotMaterial> polkaDotMaterials) {
		return toPolkaDotMaterials(polkaDotMaterials, material -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form.
	 * <p>
	 * If either {@code polkaDotMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterials a {@code List} of {@code PolkaDotMaterial} instances
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @return a {@code float[]} with all {@code PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code polkaDotMaterials}, at least one of its elements or {@code materialOffsetFunction} are {@code null}
	 */
	public static float[] toPolkaDotMaterials(final List<PolkaDotMaterial> polkaDotMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Arrays.toFloatArray(polkaDotMaterials, polkaDotMaterial -> toPolkaDotMaterial(polkaDotMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toClearCoatMaterial(clearCoatMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public static int[] toClearCoatMaterial(final ClearCoatMaterial clearCoatMaterial) {
		return toClearCoatMaterial(clearCoatMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If either {@code clearCoatMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code clearCoatMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toClearCoatMaterial(final ClearCoatMaterial clearCoatMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = clearCoatMaterial.getModifier();
		
		final Texture textureEmission = clearCoatMaterial.getTextureEmission();
		final Texture textureKD = clearCoatMaterial.getTextureKD();
		final Texture textureKS = clearCoatMaterial.getTextureKS();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		
		final int[] array = new int[CLEAR_COAT_MATERIAL_LENGTH];
		
//		Because the ClearCoatMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;		//Block #1
		array[CLEAR_COAT_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = textureKDAndTextureKSValue;	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form.
	 * <p>
	 * If {@code clearCoatMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toClearCoatMaterials(clearCoatMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterials a {@code List} of {@code ClearCoatMaterial} instances
	 * @return an {@code int[]} with all {@code ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toClearCoatMaterials(final List<ClearCoatMaterial> clearCoatMaterials) {
		return toClearCoatMaterials(clearCoatMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form.
	 * <p>
	 * If either {@code clearCoatMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterials a {@code List} of {@code ClearCoatMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code clearCoatMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toClearCoatMaterials(final List<ClearCoatMaterial> clearCoatMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(clearCoatMaterials, clearCoatMaterial -> toClearCoatMaterial(clearCoatMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toDisneyMaterial(disneyMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public static int[] toDisneyMaterial(final DisneyMaterial disneyMaterial) {
		return toDisneyMaterial(disneyMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If either {@code disneyMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code disneyMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toDisneyMaterial(final DisneyMaterial disneyMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = disneyMaterial.getModifier();
		
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
		
		final boolean isThin = disneyMaterial.isThin();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureAnisotropicValue = pack(textureAnisotropic.getID(), textureOffsetFunction.applyAsInt(textureAnisotropic), 0, 0);
		final int textureClearCoatAndTextureClearCoatGlossValue = pack(textureClearCoat.getID(), textureOffsetFunction.applyAsInt(textureClearCoat), textureClearCoatGloss.getID(), textureOffsetFunction.applyAsInt(textureClearCoatGloss));
		final int textureColorAndTextureDiffuseTransmissionValue = pack(textureColor.getID(), textureOffsetFunction.applyAsInt(textureColor), textureDiffuseTransmission.getID(), textureOffsetFunction.applyAsInt(textureDiffuseTransmission));
		final int textureEtaAndTextureFlatnessValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), textureFlatness.getID(), textureOffsetFunction.applyAsInt(textureFlatness));
		final int textureMetallicAndTextureRoughnessValue = pack(textureMetallic.getID(), textureOffsetFunction.applyAsInt(textureMetallic), textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness));
		final int textureScatterDistanceAndTextureSheenValue = pack(textureScatterDistance.getID(), textureOffsetFunction.applyAsInt(textureScatterDistance), textureSheen.getID(), textureOffsetFunction.applyAsInt(textureSheen));
		final int textureSheenTintAndTextureSpecularTintValue = pack(textureSheenTint.getID(), textureOffsetFunction.applyAsInt(textureSheenTint), textureSpecularTint.getID(), textureOffsetFunction.applyAsInt(textureSpecularTint));
		final int textureSpecularTransmissionAndIsThinValue = pack(textureSpecularTransmission.getID(), textureOffsetFunction.applyAsInt(textureSpecularTransmission), isThin ? 1 : 0, 0);
		
		final int[] array = new int[DISNEY_MATERIAL_LENGTH];
		
//		Because the DisneyMaterial occupy 16/16 positions in two blocks, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;											//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_ANISOTROPIC] = textureAnisotropicValue;													//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS] = textureClearCoatAndTextureClearCoatGlossValue;	//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION] = textureColorAndTextureDiffuseTransmissionValue;	//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS] = textureEtaAndTextureFlatnessValue;								//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS] = textureMetallicAndTextureRoughnessValue;					//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN] = textureScatterDistanceAndTextureSheenValue;			//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT] = textureSheenTintAndTextureSpecularTintValue;		//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN] = textureSpecularTransmissionAndIsThinValue;			//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link DisneyMaterial} instances in {@code disneyMaterials} in compiled form.
	 * <p>
	 * If {@code disneyMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toDisneyMaterials(disneyMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterials a {@code List} of {@code DisneyMaterial} instances
	 * @return an {@code int[]} with all {@code DisneyMaterial} instances in {@code disneyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toDisneyMaterials(final List<DisneyMaterial> disneyMaterials) {
		return toDisneyMaterials(disneyMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link DisneyMaterial} instances in {@code disneyMaterials} in compiled form.
	 * <p>
	 * If either {@code disneyMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterials a {@code List} of {@code DisneyMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code DisneyMaterial} instances in {@code disneyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code disneyMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toDisneyMaterials(final List<DisneyMaterial> disneyMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(disneyMaterials, disneyMaterial -> toDisneyMaterial(disneyMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlassMaterial(glassMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public static int[] toGlassMaterial(final GlassMaterial glassMaterial) {
		return toGlassMaterial(glassMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If either {@code glassMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glassMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlassMaterial(final GlassMaterial glassMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = glassMaterial.getModifier();
		
		final Texture textureEmission = glassMaterial.getTextureEmission();
		final Texture textureEta = glassMaterial.getTextureEta();
		final Texture textureKR = glassMaterial.getTextureKR();
		final Texture textureKT = glassMaterial.getTextureKT();
		final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = glassMaterial.isRemappingRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureEtaValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), 0, 0);
		final int textureKRAndTextureKTValue = pack(textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR), textureKT.getID(), textureOffsetFunction.applyAsInt(textureKT));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[GLASS_MATERIAL_LENGTH];
		
//		Because the GlassMaterial occupy 8/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;									//Block #1
		array[GLASS_MATERIAL_OFFSET_TEXTURE_ETA] = textureEtaValue;																//Block #1
		array[GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T] = textureKRAndTextureKTValue;									//Block #1
		array[GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = textureRoughnessUAndTextureRoughnessVValue;	//Block #1
		array[GLASS_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlassMaterial} instances in {@code glassMaterials} in compiled form.
	 * <p>
	 * If {@code glassMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlassMaterials(glassMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterials a {@code List} of {@code GlassMaterial} instances
	 * @return an {@code int[]} with all {@code GlassMaterial} instances in {@code glassMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlassMaterials(final List<GlassMaterial> glassMaterials) {
		return toGlassMaterials(glassMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlassMaterial} instances in {@code glassMaterials} in compiled form.
	 * <p>
	 * If either {@code glassMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterials a {@code List} of {@code GlassMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code GlassMaterial} instances in {@code glassMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glassMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlassMaterials(final List<GlassMaterial> glassMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(glassMaterials, glassMaterial -> toGlassMaterial(glassMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlossyMaterial(glossyMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public static int[] toGlossyMaterial(final GlossyMaterial glossyMaterial) {
		return toGlossyMaterial(glossyMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If either {@code glossyMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glossyMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlossyMaterial(final GlossyMaterial glossyMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = glossyMaterial.getModifier();
		
		final Texture textureEmission = glossyMaterial.getTextureEmission();
		final Texture textureKR = glossyMaterial.getTextureKR();
		final Texture textureRoughness = glossyMaterial.getTextureRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKRAndTextureRoughnessValue = pack(textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR), textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness));
		
		final int[] array = new int[GLOSSY_MATERIAL_LENGTH];
		
//		Because the GlossyMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;				//Block #1
		array[GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS] = textureKRAndTextureRoughnessValue;//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlossyMaterial} instances in {@code glossyMaterials} in compiled form.
	 * <p>
	 * If {@code glossyMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlossyMaterials(glossyMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterials a {@code List} of {@code GlossyMaterial} instances
	 * @return an {@code int[]} with all {@code GlossyMaterial} instances in {@code glossyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlossyMaterials(final List<GlossyMaterial> glossyMaterials) {
		return toGlossyMaterials(glossyMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlossyMaterial} instances in {@code glossyMaterials} in compiled form.
	 * <p>
	 * If either {@code glossyMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterials a {@code List} of {@code GlossyMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code GlossyMaterial} instances in {@code glossyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glossyMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlossyMaterials(final List<GlossyMaterial> glossyMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(glossyMaterials, glossyMaterial -> toGlossyMaterial(glossyMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMatteMaterial(matteMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public static int[] toMatteMaterial(final MatteMaterial matteMaterial) {
		return toMatteMaterial(matteMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If either {@code matteMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code matteMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMatteMaterial(final MatteMaterial matteMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = matteMaterial.getModifier();
		
		final Texture textureEmission = matteMaterial.getTextureEmission();
		final Texture textureAngle = matteMaterial.getTextureAngle();
		final Texture textureKD = matteMaterial.getTextureKD();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureAngleAndTextureKDValue = pack(textureAngle.getID(), textureOffsetFunction.applyAsInt(textureAngle), textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD));
		
		final int[] array = new int[MATTE_MATERIAL_LENGTH];
		
//		Because the MatteMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;		//Block #1
		array[MATTE_MATERIAL_OFFSET_TEXTURE_ANGLE_AND_TEXTURE_K_D] = textureAngleAndTextureKDValue;	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MatteMaterial} instances in {@code matteMaterials} in compiled form.
	 * <p>
	 * If {@code matteMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMatteMaterials(matteMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterials a {@code List} of {@code MatteMaterial} instances
	 * @return an {@code int[]} with all {@code MatteMaterial} instances in {@code matteMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMatteMaterials(final List<MatteMaterial> matteMaterials) {
		return toMatteMaterials(matteMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MatteMaterial} instances in {@code matteMaterials} in compiled form.
	 * <p>
	 * If either {@code matteMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterials a {@code List} of {@code MatteMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MatteMaterial} instances in {@code matteMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code matteMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMatteMaterials(final List<MatteMaterial> matteMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(matteMaterials, matteMaterial -> toMatteMaterial(matteMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMetalMaterial(metalMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public static int[] toMetalMaterial(final MetalMaterial metalMaterial) {
		return toMetalMaterial(metalMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If either {@code metalMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code metalMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMetalMaterial(final MetalMaterial metalMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = metalMaterial.getModifier();
		
		final Texture textureEmission = metalMaterial.getTextureEmission();
		final Texture textureEta = metalMaterial.getTextureEta();
		final Texture textureK = metalMaterial.getTextureK();
		final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = metalMaterial.isRemappingRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureEtaAndTextureKValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), textureK.getID(), textureOffsetFunction.applyAsInt(textureK));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[METAL_MATERIAL_LENGTH];
		
//		Because the MetalMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;									//Block #1
		array[METAL_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_K] = textureEtaAndTextureKValue;									//Block #1
		array[METAL_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = textureRoughnessUAndTextureRoughnessVValue;	//Block #1
		array[METAL_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MetalMaterial} instances in {@code metalMaterials} in compiled form.
	 * <p>
	 * If {@code metalMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMetalMaterials(metalMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterials a {@code List} of {@code MetalMaterial} instances
	 * @return an {@code int[]} with all {@code MetalMaterial} instances in {@code metalMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMetalMaterials(final List<MetalMaterial> metalMaterials) {
		return toMetalMaterials(metalMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MetalMaterial} instances in {@code metalMaterials} in compiled form.
	 * <p>
	 * If either {@code metalMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterials a {@code List} of {@code MetalMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MetalMaterial} instances in {@code metalMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code metalMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMetalMaterials(final List<MetalMaterial> metalMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(metalMaterials, metalMaterial -> toMetalMaterial(metalMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMirrorMaterial(mirrorMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public static int[] toMirrorMaterial(final MirrorMaterial mirrorMaterial) {
		return toMirrorMaterial(mirrorMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If either {@code mirrorMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code mirrorMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMirrorMaterial(final MirrorMaterial mirrorMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = mirrorMaterial.getModifier();
		
		final Texture textureEmission = mirrorMaterial.getTextureEmission();
		final Texture textureKR = mirrorMaterial.getTextureKR();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKRValue = pack(textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR), 0, 0);
		
		final int[] array = new int[MIRROR_MATERIAL_LENGTH];
		
//		Because the MirrorMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;	//Block #1
		array[MIRROR_MATERIAL_OFFSET_TEXTURE_K_R] = textureKRValue;								//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MirrorMaterial} instances in {@code mirrorMaterials} in compiled form.
	 * <p>
	 * If {@code mirrorMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMirrorMaterials(mirrorMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterials a {@code List} of {@code MirrorMaterial} instances
	 * @return an {@code int[]} with all {@code MirrorMaterial} instances in {@code mirrorMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMirrorMaterials(final List<MirrorMaterial> mirrorMaterials) {
		return toMirrorMaterials(mirrorMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MirrorMaterial} instances in {@code mirrorMaterials} in compiled form.
	 * <p>
	 * If either {@code mirrorMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterials a {@code List} of {@code MirrorMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MirrorMaterial} instances in {@code mirrorMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code mirrorMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMirrorMaterials(final List<MirrorMaterial> mirrorMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(mirrorMaterials, mirrorMaterial -> toMirrorMaterial(mirrorMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPlasticMaterial(plasticMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public static int[] toPlasticMaterial(final PlasticMaterial plasticMaterial) {
		return toPlasticMaterial(plasticMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If either {@code plasticMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code plasticMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toPlasticMaterial(final PlasticMaterial plasticMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = plasticMaterial.getModifier();
		
		final Texture textureEmission = plasticMaterial.getTextureEmission();
		final Texture textureKD = plasticMaterial.getTextureKD();
		final Texture textureKS = plasticMaterial.getTextureKS();
		final Texture textureRoughness = plasticMaterial.getTextureRoughness();
		
		final boolean isRemappingRoughness = plasticMaterial.isRemappingRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		final int textureRoughnessValue = pack(textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness), 0, 0);
		
		final int[] array = new int[PLASTIC_MATERIAL_LENGTH];
		
//		Because the PlasticMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;	//Block #1
		array[PLASTIC_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = textureKDAndTextureKSValue;//Block #1
		array[PLASTIC_MATERIAL_OFFSET_TEXTURE_ROUGHNESS] = textureRoughnessValue;				//Block #1
		array[PLASTIC_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link PlasticMaterial} instances in {@code plasticMaterials} in compiled form.
	 * <p>
	 * If {@code plasticMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPlasticMaterials(plasticMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterials a {@code List} of {@code PlasticMaterial} instances
	 * @return an {@code int[]} with all {@code PlasticMaterial} instances in {@code plasticMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toPlasticMaterials(final List<PlasticMaterial> plasticMaterials) {
		return toPlasticMaterials(plasticMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link PlasticMaterial} instances in {@code plasticMaterials} in compiled form.
	 * <p>
	 * If either {@code plasticMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterials a {@code List} of {@code PlasticMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code PlasticMaterial} instances in {@code plasticMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code plasticMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toPlasticMaterials(final List<PlasticMaterial> plasticMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(plasticMaterials, plasticMaterial -> toPlasticMaterial(plasticMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toSubstrateMaterial(substrateMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public static int[] toSubstrateMaterial(final SubstrateMaterial substrateMaterial) {
		return toSubstrateMaterial(substrateMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If either {@code substrateMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code substrateMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toSubstrateMaterial(final SubstrateMaterial substrateMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = substrateMaterial.getModifier();
		
		final Texture textureEmission = substrateMaterial.getTextureEmission();
		final Texture textureKD = substrateMaterial.getTextureKD();
		final Texture textureKS = substrateMaterial.getTextureKS();
		final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = substrateMaterial.isRemappingRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[SUBSTRATE_MATERIAL_LENGTH];
		
//		Because the SubstrateMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;										//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = textureKDAndTextureKSValue;									//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = textureRoughnessUAndTextureRoughnessVValue;	//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link SubstrateMaterial} instances in {@code substrateMaterials} in compiled form.
	 * <p>
	 * If {@code substrateMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toSubstrateMaterials(substrateMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterials a {@code List} of {@code SubstrateMaterial} instances
	 * @return an {@code int[]} with all {@code SubstrateMaterial} instances in {@code substrateMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toSubstrateMaterials(final List<SubstrateMaterial> substrateMaterials) {
		return toSubstrateMaterials(substrateMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link SubstrateMaterial} instances in {@code substrateMaterials} in compiled form.
	 * <p>
	 * If either {@code substrateMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterials a {@code List} of {@code SubstrateMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code SubstrateMaterial} instances in {@code substrateMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code substrateMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toSubstrateMaterials(final List<SubstrateMaterial> substrateMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(substrateMaterials, substrateMaterial -> toSubstrateMaterial(substrateMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code uberMaterial} in compiled form.
	 * <p>
	 * If {@code uberMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toUberMaterial(uberMaterial, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance
	 * @return an {@code int[]} with {@code uberMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterial} is {@code null}
	 */
	public static int[] toUberMaterial(final UberMaterial uberMaterial) {
		return toUberMaterial(uberMaterial, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code uberMaterial} in compiled form.
	 * <p>
	 * If either {@code uberMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param uberMaterial an {@link UberMaterial} instance
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code uberMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code uberMaterial}, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toUberMaterial(final UberMaterial uberMaterial, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		final Modifier modifier = uberMaterial.getModifier();
		
		final Texture textureEmission = uberMaterial.getTextureEmission();
		final Texture textureEta = uberMaterial.getTextureEta();
		final Texture textureKD = uberMaterial.getTextureKD();
		final Texture textureKR = uberMaterial.getTextureKR();
		final Texture textureKS = uberMaterial.getTextureKS();
		final Texture textureKT = uberMaterial.getTextureKT();
		final Texture textureOpacity = uberMaterial.getTextureOpacity();
		final Texture textureRoughnessU = uberMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = uberMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = uberMaterial.isRemappingRoughness();
		
		final int textureEmissionAndModifierValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), modifier.getID(), modifierOffsetFunction.applyAsInt(modifier));
		final int textureKDAndTextureKRValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR));
		final int textureKSAndTextureKTValue = pack(textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS), textureKT.getID(), textureOffsetFunction.applyAsInt(textureKT));
		final int textureEtaAndTextureOpacityValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), textureOpacity.getID(), textureOffsetFunction.applyAsInt(textureOpacity));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[UBER_MATERIAL_LENGTH];
		
//		Because the UberMaterial occupy 8/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION_AND_MODIFIER] = textureEmissionAndModifierValue;									//Block #1
		array[UBER_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_R] = textureKDAndTextureKRValue;									//Block #1
		array[UBER_MATERIAL_OFFSET_TEXTURE_K_S_AND_TEXTURE_K_T] = textureKSAndTextureKTValue;									//Block #1
		array[UBER_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_OPACITY] = textureEtaAndTextureOpacityValue;							//Block #1
		array[UBER_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = textureRoughnessUAndTextureRoughnessVValue;	//Block #1
		array[UBER_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link UberMaterial} instances in {@code uberMaterials} in compiled form.
	 * <p>
	 * If {@code uberMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toUberMaterials(uberMaterials, modifier -> 0, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param uberMaterials a {@code List} of {@code UberMaterial} instances
	 * @return an {@code int[]} with all {@code UberMaterial} instances in {@code uberMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code uberMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toUberMaterials(final List<UberMaterial> uberMaterials) {
		return toUberMaterials(uberMaterials, modifier -> 0, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link UberMaterial} instances in {@code uberMaterials} in compiled form.
	 * <p>
	 * If either {@code uberMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param uberMaterials a {@code List} of {@code UberMaterial} instances
	 * @param modifierOffsetFunction a {@code ToIntFunction} that returns {@link Modifier} offsets
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code UberMaterial} instances in {@code uberMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code uberMaterials}, at least one of its elements, {@code modifierOffsetFunction} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toUberMaterials(final List<UberMaterial> uberMaterials, final ToIntFunction<Modifier> modifierOffsetFunction, final ToIntFunction<Texture> textureOffsetFunction) {
		return Arrays.toIntArray(uberMaterials, uberMaterial -> toUberMaterial(uberMaterial, modifierOffsetFunction, textureOffsetFunction));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doIsSupported(final BullseyeMaterial bullseyeMaterial) {
		if(!isSupported(bullseyeMaterial.getMaterialA())) {
			return false;
		}
		
		if(!isSupported(bullseyeMaterial.getMaterialB())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final CheckerboardMaterial checkerboardMaterial) {
		if(!isSupported(checkerboardMaterial.getMaterialA())) {
			return false;
		}
		
		if(!isSupported(checkerboardMaterial.getMaterialB())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final ClearCoatMaterial clearCoatMaterial) {
		if(!CompiledModifierCache.isSupported(clearCoatMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(clearCoatMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(clearCoatMaterial.getTextureKD())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(clearCoatMaterial.getTextureKS())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final DisneyMaterial disneyMaterial) {
		if(!CompiledModifierCache.isSupported(disneyMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureAnisotropic())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureClearCoat())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureClearCoatGloss())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureColor())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureDiffuseTransmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureEta())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureFlatness())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureMetallic())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureRoughness())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureScatterDistance())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureSheen())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureSheenTint())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureSpecularTint())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(disneyMaterial.getTextureSpecularTransmission())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final GlassMaterial glassMaterial) {
		if(!CompiledModifierCache.isSupported(glassMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureEta())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureKR())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureKT())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureRoughnessU())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glassMaterial.getTextureRoughnessV())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final GlossyMaterial glossyMaterial) {
		if(!CompiledModifierCache.isSupported(glossyMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glossyMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glossyMaterial.getTextureKR())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(glossyMaterial.getTextureRoughness())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final MatteMaterial matteMaterial) {
		if(!CompiledModifierCache.isSupported(matteMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(matteMaterial.getTextureAngle())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(matteMaterial.getTextureEmission())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final MetalMaterial metalMaterial) {
		if(!CompiledModifierCache.isSupported(metalMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(metalMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(metalMaterial.getTextureEta())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(metalMaterial.getTextureK())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(metalMaterial.getTextureRoughnessU())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(metalMaterial.getTextureRoughnessV())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final MirrorMaterial mirrorMaterial) {
		if(!CompiledModifierCache.isSupported(mirrorMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(mirrorMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(mirrorMaterial.getTextureKR())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final PlasticMaterial plasticMaterial) {
		if(!CompiledModifierCache.isSupported(plasticMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(plasticMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(plasticMaterial.getTextureKD())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(plasticMaterial.getTextureKS())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(plasticMaterial.getTextureRoughness())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final PolkaDotMaterial polkaDotMaterial) {
		if(!isSupported(polkaDotMaterial.getMaterialA())) {
			return false;
		}
		
		if(!isSupported(polkaDotMaterial.getMaterialB())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final SubstrateMaterial substrateMaterial) {
		if(!CompiledModifierCache.isSupported(substrateMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(substrateMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(substrateMaterial.getTextureKD())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(substrateMaterial.getTextureKS())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(substrateMaterial.getTextureRoughnessU())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(substrateMaterial.getTextureRoughnessV())) {
			return false;
		}
		
		return true;
	}
	
	private static boolean doIsSupported(final UberMaterial uberMaterial) {
		if(!CompiledModifierCache.isSupported(uberMaterial.getModifier())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureEmission())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureEta())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureKD())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureKR())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureKS())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureKT())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureOpacity())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureRoughnessU())) {
			return false;
		}
		
		if(!CompiledTextureCache.isSupported(uberMaterial.getTextureRoughnessV())) {
			return false;
		}
		
		return true;
	}
}