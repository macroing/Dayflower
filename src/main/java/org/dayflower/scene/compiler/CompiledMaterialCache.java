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
import org.dayflower.scene.texture.Texture;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledMaterialCache} contains {@link Material} instances in compiled form.
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
	public static final int DISNEY_MATERIAL_LENGTH = 8;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Clear Coat} and {@code Clear Coat Gloss} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Color} and {@code Diffuse Transmission} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Emission} and {@code Anisotropic} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ANISOTROPIC = 0;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Eta} and {@code Flatness} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Metallic} and {@code Roughness} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS = 4;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Scatter Distance} and {@code Sheen} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN = 5;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Sheen Tint} and {@code Specular Tint} in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT = 6;
	
	/**
	 * The ID and offset for the {@link Texture} instance denoted by {@code Specular Transmission} and the thin flag in a compiled {@link DisneyMaterial} instance.
	 */
	public static final int DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN = 7;
	
	/**
	 * The length of a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_LENGTH = 4;
	
	/**
	 * The offset for the roughness remapping flag in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS = 3;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Emission} and {@code Eta} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ETA = 0;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KR} and {@code KT} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Roughness U} and {@code Roughness V} in a compiled {@link GlassMaterial} instance.
	 */
	public static final int GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V = 2;
	
	/**
	 * The length of a compiled {@link GlossyMaterial} instance.
	 */
	public static final int GLOSSY_MATERIAL_LENGTH = 2;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code KR} and {@code Roughness} in a compiled {@link GlossyMaterial} instance.
	 */
	public static final int GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS = 1;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code Emission} in a compiled {@link Material} instance.
	 */
	public static final int MATERIAL_OFFSET_TEXTURE_EMISSION = 0;
	
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
	public static final int MIRROR_MATERIAL_LENGTH = 1;
	
	/**
	 * The IDs and offsets for the {@link Texture} instances denoted by {@code Emission} and {@code KR} in a compiled {@link MirrorMaterial} instance.
	 */
	public static final int MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R = 0;
	
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
			setBullseyeMaterials(Structures.removeStructure(getBullseyeMaterials(), absoluteOffset, BULLSEYE_MATERIAL_LENGTH));
			
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
			setCheckerboardMaterials(Structures.removeStructure(getCheckerboardMaterials(), absoluteOffset, CHECKERBOARD_MATERIAL_LENGTH));
			
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
			setClearCoatMaterials(Structures.removeStructure(getClearCoatMaterials(), absoluteOffset, CLEAR_COAT_MATERIAL_LENGTH));
			
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
			setDisneyMaterials(Structures.removeStructure(getDisneyMaterials(), absoluteOffset, DISNEY_MATERIAL_LENGTH));
			
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
			setGlassMaterials(Structures.removeStructure(getGlassMaterials(), absoluteOffset, GLASS_MATERIAL_LENGTH));
			
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
			setGlossyMaterials(Structures.removeStructure(getGlossyMaterials(), absoluteOffset, GLOSSY_MATERIAL_LENGTH));
			
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
		
		setBullseyeMaterials(Structures.addStructure(getBullseyeMaterials(), bullseyeMaterial));
		
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
		
		setCheckerboardMaterials(Structures.addStructure(getCheckerboardMaterials(), checkerboardMaterial));
		
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
		
		setClearCoatMaterials(Structures.addStructure(getClearCoatMaterials(), clearCoatMaterial));
		
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
		
		setDisneyMaterials(Structures.addStructure(getDisneyMaterials(), disneyMaterial));
		
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
		
		setGlassMaterials(Structures.addStructure(getGlassMaterials(), glassMaterial));
		
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
		
		setGlossyMaterials(Structures.addStructure(getGlossyMaterials(), glossyMaterial));
		
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
		
		return Structures.getStructureOffsetAbsolute(this.bullseyeMaterials, bullseyeMaterial, getBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.bullseyeMaterials, bullseyeMaterial, getBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.checkerboardMaterials, checkerboardMaterial, getCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.checkerboardMaterials, checkerboardMaterial, getCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.clearCoatMaterials, clearCoatMaterial, getClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.clearCoatMaterials, clearCoatMaterial, getClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.disneyMaterials, disneyMaterial, getDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.disneyMaterials, disneyMaterial, getDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.glassMaterials, glassMaterial, getGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.glassMaterials, glassMaterial, getGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.glossyMaterials, glossyMaterial, getGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.glossyMaterials, glossyMaterial, getGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.matteMaterials, matteMaterial, getMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.matteMaterials, matteMaterial, getMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.metalMaterials, metalMaterial, getMetalMaterialCount(), METAL_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.metalMaterials, metalMaterial, getMetalMaterialCount(), METAL_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.mirrorMaterials, mirrorMaterial, getMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.mirrorMaterials, mirrorMaterial, getMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.plasticMaterials, plasticMaterial, getPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.plasticMaterials, plasticMaterial, getPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.polkaDotMaterials, polkaDotMaterial, getPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.polkaDotMaterials, polkaDotMaterial, getPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetAbsolute(this.substrateMaterials, substrateMaterial, getSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
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
		
		return Structures.getStructureOffsetRelative(this.substrateMaterials, substrateMaterial, getSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 0] = origin.getX();		//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 1] = origin.getY();		//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 2] = origin.getZ();		//Block #1
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
		return Floats.toArray(bullseyeMaterials, bullseyeMaterial -> toBullseyeMaterial(bullseyeMaterial, materialOffsetFunction));
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
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 0] = scale.getX();			//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 1] = scale.getY();			//Block #1
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
		return Floats.toArray(checkerboardMaterials, checkerboardMaterial -> toCheckerboardMaterial(checkerboardMaterial, materialOffsetFunction));
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
		return Floats.toArray(polkaDotMaterials, polkaDotMaterial -> toPolkaDotMaterial(polkaDotMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toClearCoatMaterial(clearCoatMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public static int[] toClearCoatMaterial(final ClearCoatMaterial clearCoatMaterial) {
		return toClearCoatMaterial(clearCoatMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If either {@code clearCoatMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code clearCoatMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toClearCoatMaterial(final ClearCoatMaterial clearCoatMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = clearCoatMaterial.getTextureEmission();
		final Texture textureKD = clearCoatMaterial.getTextureKD();
		final Texture textureKS = clearCoatMaterial.getTextureKS();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		
		final int[] array = new int[CLEAR_COAT_MATERIAL_LENGTH];
		
//		Because the ClearCoatMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;								//Block #1
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
	 * compiledMaterialCache.toClearCoatMaterials(clearCoatMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterials a {@code List} of {@code ClearCoatMaterial} instances
	 * @return an {@code int[]} with all {@code ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toClearCoatMaterials(final List<ClearCoatMaterial> clearCoatMaterials) {
		return toClearCoatMaterials(clearCoatMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form.
	 * <p>
	 * If either {@code clearCoatMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterials a {@code List} of {@code ClearCoatMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code clearCoatMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toClearCoatMaterials(final List<ClearCoatMaterial> clearCoatMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(clearCoatMaterials, clearCoatMaterial -> toClearCoatMaterial(clearCoatMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toDisneyMaterial(disneyMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public static int[] toDisneyMaterial(final DisneyMaterial disneyMaterial) {
		return toDisneyMaterial(disneyMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If either {@code disneyMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code disneyMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toDisneyMaterial(final DisneyMaterial disneyMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
		
		final int textureEmissionAndTextureAnisotropicValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), textureAnisotropic.getID(), textureOffsetFunction.applyAsInt(textureAnisotropic));
		final int textureClearCoatAndTextureClearCoatGlossValue = pack(textureClearCoat.getID(), textureOffsetFunction.applyAsInt(textureClearCoat), textureClearCoatGloss.getID(), textureOffsetFunction.applyAsInt(textureClearCoatGloss));
		final int textureColorAndTextureDiffuseTransmissionValue = pack(textureColor.getID(), textureOffsetFunction.applyAsInt(textureColor), textureDiffuseTransmission.getID(), textureOffsetFunction.applyAsInt(textureDiffuseTransmission));
		final int textureEtaAndTextureFlatnessValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), textureFlatness.getID(), textureOffsetFunction.applyAsInt(textureFlatness));
		final int textureMetallicAndTextureRoughnessValue = pack(textureMetallic.getID(), textureOffsetFunction.applyAsInt(textureMetallic), textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness));
		final int textureScatterDistanceAndTextureSheenValue = pack(textureScatterDistance.getID(), textureOffsetFunction.applyAsInt(textureScatterDistance), textureSheen.getID(), textureOffsetFunction.applyAsInt(textureSheen));
		final int textureSheenTintAndTextureSpecularTintValue = pack(textureSheenTint.getID(), textureOffsetFunction.applyAsInt(textureSheenTint), textureSpecularTint.getID(), textureOffsetFunction.applyAsInt(textureSpecularTint));
		final int textureSpecularTransmissionAndIsThinValue = pack(textureSpecularTransmission.getID(), textureOffsetFunction.applyAsInt(textureSpecularTransmission), isThin ? 1 : 0, 0);
		
		final int[] array = new int[DISNEY_MATERIAL_LENGTH];
		
//		Because the DisneyMaterial occupy 8/8 positions in a block, it should be aligned.
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ANISOTROPIC] = textureEmissionAndTextureAnisotropicValue;				//Block #1
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
	 * compiledMaterialCache.toDisneyMaterials(disneyMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterials a {@code List} of {@code DisneyMaterial} instances
	 * @return an {@code int[]} with all {@code DisneyMaterial} instances in {@code disneyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toDisneyMaterials(final List<DisneyMaterial> disneyMaterials) {
		return toDisneyMaterials(disneyMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link DisneyMaterial} instances in {@code disneyMaterials} in compiled form.
	 * <p>
	 * If either {@code disneyMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterials a {@code List} of {@code DisneyMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code DisneyMaterial} instances in {@code disneyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code disneyMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toDisneyMaterials(final List<DisneyMaterial> disneyMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(disneyMaterials, disneyMaterial -> toDisneyMaterial(disneyMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlassMaterial(glassMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public static int[] toGlassMaterial(final GlassMaterial glassMaterial) {
		return toGlassMaterial(glassMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If either {@code glassMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glassMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlassMaterial(final GlassMaterial glassMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = glassMaterial.getTextureEmission();
		final Texture textureEta = glassMaterial.getTextureEta();
		final Texture textureKR = glassMaterial.getTextureKR();
		final Texture textureKT = glassMaterial.getTextureKT();
		final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = glassMaterial.isRemappingRoughness();
		
		final int textureEmissionAndTextureEtaValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta));
		final int textureKRAndTextureKTValue = pack(textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR), textureKT.getID(), textureOffsetFunction.applyAsInt(textureKT));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[GLASS_MATERIAL_LENGTH];
		
//		Because the GlassMaterial occupy 4/8 positions in a block, it should be aligned.
		array[GLASS_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ETA] = textureEmissionAndTextureEtaValue;						//Block #1
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
	 * compiledMaterialCache.toGlassMaterials(glassMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterials a {@code List} of {@code GlassMaterial} instances
	 * @return an {@code int[]} with all {@code GlassMaterial} instances in {@code glassMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlassMaterials(final List<GlassMaterial> glassMaterials) {
		return toGlassMaterials(glassMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlassMaterial} instances in {@code glassMaterials} in compiled form.
	 * <p>
	 * If either {@code glassMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterials a {@code List} of {@code GlassMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code GlassMaterial} instances in {@code glassMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glassMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlassMaterials(final List<GlassMaterial> glassMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(glassMaterials, glassMaterial -> toGlassMaterial(glassMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlossyMaterial(glossyMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public static int[] toGlossyMaterial(final GlossyMaterial glossyMaterial) {
		return toGlossyMaterial(glossyMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If either {@code glossyMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glossyMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlossyMaterial(final GlossyMaterial glossyMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = glossyMaterial.getTextureEmission();
		final Texture textureKR = glossyMaterial.getTextureKR();
		final Texture textureRoughness = glossyMaterial.getTextureRoughness();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureKRAndTextureRoughnessValue = pack(textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR), textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness));
		
		final int[] array = new int[GLOSSY_MATERIAL_LENGTH];
		
//		Because the GlossyMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;										//Block #1
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
	 * compiledMaterialCache.toGlossyMaterials(glossyMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterials a {@code List} of {@code GlossyMaterial} instances
	 * @return an {@code int[]} with all {@code GlossyMaterial} instances in {@code glossyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlossyMaterials(final List<GlossyMaterial> glossyMaterials) {
		return toGlossyMaterials(glossyMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link GlossyMaterial} instances in {@code glossyMaterials} in compiled form.
	 * <p>
	 * If either {@code glossyMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterials a {@code List} of {@code GlossyMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code GlossyMaterial} instances in {@code glossyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code glossyMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toGlossyMaterials(final List<GlossyMaterial> glossyMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(glossyMaterials, glossyMaterial -> toGlossyMaterial(glossyMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMatteMaterial(matteMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public static int[] toMatteMaterial(final MatteMaterial matteMaterial) {
		return toMatteMaterial(matteMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If either {@code matteMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code matteMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMatteMaterial(final MatteMaterial matteMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = matteMaterial.getTextureEmission();
		final Texture textureAngle = matteMaterial.getTextureAngle();
		final Texture textureKD = matteMaterial.getTextureKD();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureAngleAndTextureKDValue = pack(textureAngle.getID(), textureOffsetFunction.applyAsInt(textureAngle), textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD));
		
		final int[] array = new int[MATTE_MATERIAL_LENGTH];
		
//		Because the MatteMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;								//Block #1
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
	 * compiledMaterialCache.toMatteMaterials(matteMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterials a {@code List} of {@code MatteMaterial} instances
	 * @return an {@code int[]} with all {@code MatteMaterial} instances in {@code matteMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMatteMaterials(final List<MatteMaterial> matteMaterials) {
		return toMatteMaterials(matteMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MatteMaterial} instances in {@code matteMaterials} in compiled form.
	 * <p>
	 * If either {@code matteMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterials a {@code List} of {@code MatteMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MatteMaterial} instances in {@code matteMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code matteMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMatteMaterials(final List<MatteMaterial> matteMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(matteMaterials, matteMaterial -> toMatteMaterial(matteMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMetalMaterial(metalMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public static int[] toMetalMaterial(final MetalMaterial metalMaterial) {
		return toMetalMaterial(metalMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If either {@code metalMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code metalMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMetalMaterial(final MetalMaterial metalMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = metalMaterial.getTextureEmission();
		final Texture textureEta = metalMaterial.getTextureEta();
		final Texture textureK = metalMaterial.getTextureK();
		final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = metalMaterial.isRemappingRoughness();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureEtaAndTextureKValue = pack(textureEta.getID(), textureOffsetFunction.applyAsInt(textureEta), textureK.getID(), textureOffsetFunction.applyAsInt(textureK));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[METAL_MATERIAL_LENGTH];
		
//		Because the MetalMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;															//Block #1
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
	 * compiledMaterialCache.toMetalMaterials(metalMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterials a {@code List} of {@code MetalMaterial} instances
	 * @return an {@code int[]} with all {@code MetalMaterial} instances in {@code metalMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMetalMaterials(final List<MetalMaterial> metalMaterials) {
		return toMetalMaterials(metalMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MetalMaterial} instances in {@code metalMaterials} in compiled form.
	 * <p>
	 * If either {@code metalMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterials a {@code List} of {@code MetalMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MetalMaterial} instances in {@code metalMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code metalMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMetalMaterials(final List<MetalMaterial> metalMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(metalMaterials, metalMaterial -> toMetalMaterial(metalMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMirrorMaterial(mirrorMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public static int[] toMirrorMaterial(final MirrorMaterial mirrorMaterial) {
		return toMirrorMaterial(mirrorMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If either {@code mirrorMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code mirrorMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMirrorMaterial(final MirrorMaterial mirrorMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = mirrorMaterial.getTextureEmission();
		final Texture textureKR = mirrorMaterial.getTextureKR();
		
		final int textureEmissionAndTextureKRValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR));
		
		final int[] array = new int[MIRROR_MATERIAL_LENGTH];
		
//		Because the MirrorMaterial occupy 1/8 positions in a block, it should be aligned.
		array[MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R] = textureEmissionAndTextureKRValue;//Block #1
		
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
	 * compiledMaterialCache.toMirrorMaterials(mirrorMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterials a {@code List} of {@code MirrorMaterial} instances
	 * @return an {@code int[]} with all {@code MirrorMaterial} instances in {@code mirrorMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMirrorMaterials(final List<MirrorMaterial> mirrorMaterials) {
		return toMirrorMaterials(mirrorMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MirrorMaterial} instances in {@code mirrorMaterials} in compiled form.
	 * <p>
	 * If either {@code mirrorMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterials a {@code List} of {@code MirrorMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code MirrorMaterial} instances in {@code mirrorMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code mirrorMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toMirrorMaterials(final List<MirrorMaterial> mirrorMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(mirrorMaterials, mirrorMaterial -> toMirrorMaterial(mirrorMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPlasticMaterial(plasticMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public static int[] toPlasticMaterial(final PlasticMaterial plasticMaterial) {
		return toPlasticMaterial(plasticMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If either {@code plasticMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code plasticMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toPlasticMaterial(final PlasticMaterial plasticMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = plasticMaterial.getTextureEmission();
		final Texture textureKD = plasticMaterial.getTextureKD();
		final Texture textureKS = plasticMaterial.getTextureKS();
		final Texture textureRoughness = plasticMaterial.getTextureRoughness();
		
		final boolean isRemappingRoughness = plasticMaterial.isRemappingRoughness();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		final int textureRoughnessValue = pack(textureRoughness.getID(), textureOffsetFunction.applyAsInt(textureRoughness), 0, 0);
		
		final int[] array = new int[PLASTIC_MATERIAL_LENGTH];
		
//		Because the PlasticMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;							//Block #1
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
	 * compiledMaterialCache.toPlasticMaterials(plasticMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterials a {@code List} of {@code PlasticMaterial} instances
	 * @return an {@code int[]} with all {@code PlasticMaterial} instances in {@code plasticMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toPlasticMaterials(final List<PlasticMaterial> plasticMaterials) {
		return toPlasticMaterials(plasticMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link PlasticMaterial} instances in {@code plasticMaterials} in compiled form.
	 * <p>
	 * If either {@code plasticMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterials a {@code List} of {@code PlasticMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code PlasticMaterial} instances in {@code plasticMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code plasticMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toPlasticMaterials(final List<PlasticMaterial> plasticMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(plasticMaterials, plasticMaterial -> toPlasticMaterial(plasticMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toSubstrateMaterial(substrateMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public static int[] toSubstrateMaterial(final SubstrateMaterial substrateMaterial) {
		return toSubstrateMaterial(substrateMaterial, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If either {@code substrateMaterial} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code substrateMaterial} or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toSubstrateMaterial(final SubstrateMaterial substrateMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = substrateMaterial.getTextureEmission();
		final Texture textureKD = substrateMaterial.getTextureKD();
		final Texture textureKS = substrateMaterial.getTextureKS();
		final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = substrateMaterial.isRemappingRoughness();
		
		final int textureEmissionValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), 0, 0);
		final int textureKDAndTextureKSValue = pack(textureKD.getID(), textureOffsetFunction.applyAsInt(textureKD), textureKS.getID(), textureOffsetFunction.applyAsInt(textureKS));
		final int textureRoughnessUAndTextureRoughnessVValue = pack(textureRoughnessU.getID(), textureOffsetFunction.applyAsInt(textureRoughnessU), textureRoughnessV.getID(), textureOffsetFunction.applyAsInt(textureRoughnessV));
		
		final int[] array = new int[SUBSTRATE_MATERIAL_LENGTH];
		
//		Because the SubstrateMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = textureEmissionValue;																//Block #1
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
	 * compiledMaterialCache.toSubstrateMaterials(substrateMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterials a {@code List} of {@code SubstrateMaterial} instances
	 * @return an {@code int[]} with all {@code SubstrateMaterial} instances in {@code substrateMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toSubstrateMaterials(final List<SubstrateMaterial> substrateMaterials) {
		return toSubstrateMaterials(substrateMaterials, texture -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link SubstrateMaterial} instances in {@code substrateMaterials} in compiled form.
	 * <p>
	 * If either {@code substrateMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterials a {@code List} of {@code SubstrateMaterial} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return an {@code int[]} with all {@code SubstrateMaterial} instances in {@code substrateMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code substrateMaterials}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static int[] toSubstrateMaterials(final List<SubstrateMaterial> substrateMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(substrateMaterials, substrateMaterial -> toSubstrateMaterial(substrateMaterial, textureOffsetFunction));
	}
}