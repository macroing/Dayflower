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
	
	private float[] materialBullseyeMaterialArray;
	private float[] materialCheckerboardMaterialArray;
	private float[] materialPolkaDotMaterialArray;
	private int[] materialClearCoatMaterialArray;
	private int[] materialDisneyMaterialArray;
	private int[] materialGlassMaterialArray;
	private int[] materialGlossyMaterialArray;
	private int[] materialMatteMaterialArray;
	private int[] materialMetalMaterialArray;
	private int[] materialMirrorMaterialArray;
	private int[] materialPlasticMaterialArray;
	private int[] materialSubstrateMaterialArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledMaterialCache} instance.
	 */
	public CompiledMaterialCache() {
		setMaterialBullseyeMaterialArray(new float[1]);
		setMaterialCheckerboardMaterialArray(new float[1]);
		setMaterialClearCoatMaterialArray(new int[1]);
		setMaterialDisneyMaterialArray(new int[1]);
		setMaterialGlassMaterialArray(new int[1]);
		setMaterialGlossyMaterialArray(new int[1]);
		setMaterialMatteMaterialArray(new int[1]);
		setMaterialMetalMaterialArray(new int[1]);
		setMaterialMirrorMaterialArray(new int[1]);
		setMaterialPlasticMaterialArray(new int[1]);
		setMaterialPolkaDotMaterialArray(new float[1]);
		setMaterialSubstrateMaterialArray(new int[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getMaterialBullseyeMaterialArray() {
		return this.materialBullseyeMaterialArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getMaterialCheckerboardMaterialArray() {
		return this.materialCheckerboardMaterialArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getMaterialPolkaDotMaterialArray() {
		return this.materialPolkaDotMaterialArray;
	}
	
	/**
	 * Returns the {@link BullseyeMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code BullseyeMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialBullseyeMaterialCount() {
		return Structures.getStructureCount(this.materialBullseyeMaterialArray, BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialBullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialBullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialBullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialBullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialBullseyeMaterial} is {@code null}
	 */
	public int getMaterialBullseyeMaterialOffsetAbsolute(final float[] materialBullseyeMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialBullseyeMaterialArray, Objects.requireNonNull(materialBullseyeMaterial, "materialBullseyeMaterial == null"), getMaterialBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialBullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialBullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialBullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the relative offset of {@code materialBullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialBullseyeMaterial} is {@code null}
	 */
	public int getMaterialBullseyeMaterialOffsetRelative(final float[] materialBullseyeMaterial) {
		return Structures.getStructureOffsetRelative(this.materialBullseyeMaterialArray, Objects.requireNonNull(materialBullseyeMaterial, "materialBullseyeMaterial == null"), getMaterialBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialCheckerboardMaterialCount() {
		return Structures.getStructureCount(this.materialCheckerboardMaterialArray, CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialCheckerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialCheckerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialCheckerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialCheckerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialCheckerboardMaterial} is {@code null}
	 */
	public int getMaterialCheckerboardMaterialOffsetAbsolute(final float[] materialCheckerboardMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialCheckerboardMaterialArray, Objects.requireNonNull(materialCheckerboardMaterial, "materialCheckerboardMaterial == null"), getMaterialCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialCheckerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialCheckerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialCheckerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the relative offset of {@code materialCheckerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialCheckerboardMaterial} is {@code null}
	 */
	public int getMaterialCheckerboardMaterialOffsetRelative(final float[] materialCheckerboardMaterial) {
		return Structures.getStructureOffsetRelative(this.materialCheckerboardMaterialArray, Objects.requireNonNull(materialCheckerboardMaterial, "materialCheckerboardMaterial == null"), getMaterialCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialClearCoatMaterialCount() {
		return Structures.getStructureCount(this.materialClearCoatMaterialArray, CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialClearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialClearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialClearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialClearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialClearCoatMaterial} is {@code null}
	 */
	public int getMaterialClearCoatMaterialOffsetAbsolute(final int[] materialClearCoatMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialClearCoatMaterialArray, Objects.requireNonNull(materialClearCoatMaterial, "materialClearCoatMaterial == null"), getMaterialClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialClearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialClearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialClearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the relative offset of {@code materialClearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialClearCoatMaterial} is {@code null}
	 */
	public int getMaterialClearCoatMaterialOffsetRelative(final int[] materialClearCoatMaterial) {
		return Structures.getStructureOffsetRelative(this.materialClearCoatMaterialArray, Objects.requireNonNull(materialClearCoatMaterial, "materialClearCoatMaterial == null"), getMaterialClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link DisneyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code DisneyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialDisneyMaterialCount() {
		return Structures.getStructureCount(this.materialDisneyMaterialArray, DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialDisneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialDisneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialDisneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialDisneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialDisneyMaterial} is {@code null}
	 */
	public int getMaterialDisneyMaterialOffsetAbsolute(final int[] materialDisneyMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialDisneyMaterialArray, Objects.requireNonNull(materialDisneyMaterial, "materialDisneyMaterial == null"), getMaterialDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialDisneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialDisneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialDisneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the relative offset of {@code materialDisneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialDisneyMaterial} is {@code null}
	 */
	public int getMaterialDisneyMaterialOffsetRelative(final int[] materialDisneyMaterial) {
		return Structures.getStructureOffsetRelative(this.materialDisneyMaterialArray, Objects.requireNonNull(materialDisneyMaterial, "materialDisneyMaterial == null"), getMaterialDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link GlassMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlassMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialGlassMaterialCount() {
		return Structures.getStructureCount(this.materialGlassMaterialArray, GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialGlassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialGlassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialGlassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialGlassMaterial} is {@code null}
	 */
	public int getMaterialGlassMaterialOffsetAbsolute(final int[] materialGlassMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialGlassMaterialArray, Objects.requireNonNull(materialGlassMaterial, "materialGlassMaterial == null"), getMaterialGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialGlassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialGlassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the relative offset of {@code materialGlassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialGlassMaterial} is {@code null}
	 */
	public int getMaterialGlassMaterialOffsetRelative(final int[] materialGlassMaterial) {
		return Structures.getStructureOffsetRelative(this.materialGlassMaterialArray, Objects.requireNonNull(materialGlassMaterial, "materialGlassMaterial == null"), getMaterialGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link GlossyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlossyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialGlossyMaterialCount() {
		return Structures.getStructureCount(this.materialGlossyMaterialArray, GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialGlossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialGlossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialGlossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialGlossyMaterial} is {@code null}
	 */
	public int getMaterialGlossyMaterialOffsetAbsolute(final int[] materialGlossyMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialGlossyMaterialArray, Objects.requireNonNull(materialGlossyMaterial, "materialGlossyMaterial == null"), getMaterialGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialGlossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialGlossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialGlossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the relative offset of {@code materialGlossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialGlossyMaterial} is {@code null}
	 */
	public int getMaterialGlossyMaterialOffsetRelative(final int[] materialGlossyMaterial) {
		return Structures.getStructureOffsetRelative(this.materialGlossyMaterialArray, Objects.requireNonNull(materialGlossyMaterial, "materialGlossyMaterial == null"), getMaterialGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MatteMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MatteMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMatteMaterialCount() {
		return Structures.getStructureCount(this.materialMatteMaterialArray, MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialMatteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMatteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMatteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialMatteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMatteMaterial} is {@code null}
	 */
	public int getMaterialMatteMaterialOffsetAbsolute(final int[] materialMatteMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialMatteMaterialArray, Objects.requireNonNull(materialMatteMaterial, "materialMatteMaterial == null"), getMaterialMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialMatteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMatteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMatteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the relative offset of {@code materialMatteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMatteMaterial} is {@code null}
	 */
	public int getMaterialMatteMaterialOffsetRelative(final int[] materialMatteMaterial) {
		return Structures.getStructureOffsetRelative(this.materialMatteMaterialArray, Objects.requireNonNull(materialMatteMaterial, "materialMatteMaterial == null"), getMaterialMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MetalMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MetalMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMetalMaterialCount() {
		return Structures.getStructureCount(this.materialMetalMaterialArray, METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialMetalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMetalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMetalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialMetalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMetalMaterial} is {@code null}
	 */
	public int getMaterialMetalMaterialOffsetAbsolute(final int[] materialMetalMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialMetalMaterialArray, Objects.requireNonNull(materialMetalMaterial, "materialMetalMaterial == null"), getMaterialMetalMaterialCount(), METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialMetalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMetalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMetalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the relative offset of {@code materialMetalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMetalMaterial} is {@code null}
	 */
	public int getMaterialMetalMaterialOffsetRelative(final int[] materialMetalMaterial) {
		return Structures.getStructureOffsetRelative(this.materialMetalMaterialArray, Objects.requireNonNull(materialMetalMaterial, "materialMetalMaterial == null"), getMaterialMetalMaterialCount(), METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MirrorMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MirrorMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMirrorMaterialCount() {
		return Structures.getStructureCount(this.materialMirrorMaterialArray, MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialMirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialMirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMirrorMaterial} is {@code null}
	 */
	public int getMaterialMirrorMaterialOffsetAbsolute(final int[] materialMirrorMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialMirrorMaterialArray, Objects.requireNonNull(materialMirrorMaterial, "materialMirrorMaterial == null"), getMaterialMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialMirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialMirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialMirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the relative offset of {@code materialMirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialMirrorMaterial} is {@code null}
	 */
	public int getMaterialMirrorMaterialOffsetRelative(final int[] materialMirrorMaterial) {
		return Structures.getStructureOffsetRelative(this.materialMirrorMaterialArray, Objects.requireNonNull(materialMirrorMaterial, "materialMirrorMaterial == null"), getMaterialMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link PlasticMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PlasticMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialPlasticMaterialCount() {
		return Structures.getStructureCount(this.materialPlasticMaterialArray, PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialPlasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialPlasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPlasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialPlasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialPlasticMaterial} is {@code null}
	 */
	public int getMaterialPlasticMaterialOffsetAbsolute(final int[] materialPlasticMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialPlasticMaterialArray, Objects.requireNonNull(materialPlasticMaterial, "materialPlasticMaterial == null"), getMaterialPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialPlasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialPlasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPlasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the relative offset of {@code materialPlasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialPlasticMaterial} is {@code null}
	 */
	public int getMaterialPlasticMaterialOffsetRelative(final int[] materialPlasticMaterial) {
		return Structures.getStructureOffsetRelative(this.materialPlasticMaterialArray, Objects.requireNonNull(materialPlasticMaterial, "materialPlasticMaterial == null"), getMaterialPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialPolkaDotMaterialCount() {
		return Structures.getStructureCount(this.materialPolkaDotMaterialArray, POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialPolkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialPolkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPolkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialPolkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialPolkaDotMaterial} is {@code null}
	 */
	public int getMaterialPolkaDotMaterialOffsetAbsolute(final float[] materialPolkaDotMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialPolkaDotMaterialArray, Objects.requireNonNull(materialPolkaDotMaterial, "materialPolkaDotMaterial == null"), getMaterialPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialPolkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialPolkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPolkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the relative offset of {@code materialPolkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialPolkaDotMaterial} is {@code null}
	 */
	public int getMaterialPolkaDotMaterialOffsetRelative(final float[] materialPolkaDotMaterial) {
		return Structures.getStructureOffsetRelative(this.materialPolkaDotMaterialArray, Objects.requireNonNull(materialPolkaDotMaterial, "materialPolkaDotMaterial == null"), getMaterialPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link SubstrateMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code SubstrateMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialSubstrateMaterialCount() {
		return Structures.getStructureCount(this.materialSubstrateMaterialArray, SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code materialSubstrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialSubstrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialSubstrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the absolute offset of {@code materialSubstrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialSubstrateMaterial} is {@code null}
	 */
	public int getMaterialSubstrateMaterialOffsetAbsolute(final int[] materialSubstrateMaterial) {
		return Structures.getStructureOffsetAbsolute(this.materialSubstrateMaterialArray, Objects.requireNonNull(materialSubstrateMaterial, "materialSubstrateMaterial == null"), getMaterialSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code materialSubstrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code materialSubstrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialSubstrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the relative offset of {@code materialSubstrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code materialSubstrateMaterial} is {@code null}
	 */
	public int getMaterialSubstrateMaterialOffsetRelative(final int[] materialSubstrateMaterial) {
		return Structures.getStructureOffsetRelative(this.materialSubstrateMaterialArray, Objects.requireNonNull(materialSubstrateMaterial, "materialSubstrateMaterial == null"), getMaterialSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialClearCoatMaterialArray() {
		return this.materialClearCoatMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialDisneyMaterialArray() {
		return this.materialDisneyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialGlassMaterialArray() {
		return this.materialGlassMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialGlossyMaterialArray() {
		return this.materialGlossyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialMatteMaterialArray() {
		return this.materialMatteMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialMetalMaterialArray() {
		return this.materialMetalMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialMirrorMaterialArray() {
		return this.materialMirrorMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialPlasticMaterialArray() {
		return this.materialPlasticMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMaterialSubstrateMaterialArray() {
		return this.materialSubstrateMaterialArray;
	}
	
	/**
	 * Sets all {@link BullseyeMaterial} instances in compiled form to {@code materialBullseyeMaterialArray}.
	 * <p>
	 * If {@code materialBullseyeMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialBullseyeMaterialArray the {@code BullseyeMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialBullseyeMaterialArray} is {@code null}
	 */
	public void setMaterialBullseyeMaterialArray(final float[] materialBullseyeMaterialArray) {
		this.materialBullseyeMaterialArray = Objects.requireNonNull(materialBullseyeMaterialArray, "materialBullseyeMaterialArray == null");
	}
	
	/**
	 * Sets all {@link CheckerboardMaterial} instances in compiled form to {@code materialCheckerboardMaterialArray}.
	 * <p>
	 * If {@code materialCheckerboardMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialCheckerboardMaterialArray the {@code CheckerboardMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialCheckerboardMaterialArray} is {@code null}
	 */
	public void setMaterialCheckerboardMaterialArray(final float[] materialCheckerboardMaterialArray) {
		this.materialCheckerboardMaterialArray = Objects.requireNonNull(materialCheckerboardMaterialArray, "materialCheckerboardMaterialArray == null");
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
	 * Sets all {@link PolkaDotMaterial} instances in compiled form to {@code materialPolkaDotMaterialArray}.
	 * <p>
	 * If {@code materialPolkaDotMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param materialPolkaDotMaterialArray the {@code PolkaDotMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code materialPolkaDotMaterialArray} is {@code null}
	 */
	public void setMaterialPolkaDotMaterialArray(final float[] materialPolkaDotMaterialArray) {
		this.materialPolkaDotMaterialArray = Objects.requireNonNull(materialPolkaDotMaterialArray, "materialPolkaDotMaterialArray == null");
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
}