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

import java.util.Objects;

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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeMaterial} in compiled form.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance
	 * @return a {@code float[]} with {@code bullseyeMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public static float[] toArray(final BullseyeMaterial bullseyeMaterial) {
		final Material materialA = bullseyeMaterial.getMaterialA();
		final Material materialB = bullseyeMaterial.getMaterialB();
		
		final Point3F origin = bullseyeMaterial.getOrigin();
		
		final float scale = bullseyeMaterial.getScale();
		
		final float[] array = new float[BULLSEYE_MATERIAL_LENGTH];
		
//		Because the BullseyeMaterial occupy 8/8 positions in a block, it should be aligned.
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 0] = origin.getX();		//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 1] = origin.getY();		//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_ORIGIN + 2] = origin.getZ();		//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_MATERIAL_A] = materialA.getID();	//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_MATERIAL_B] = materialB.getID();	//Block #1
		array[BULLSEYE_MATERIAL_OFFSET_SCALE] = scale;					//Block #1
		array[6] = 0.0F;												//Block #1
		array[7] = 0.0F;												//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardMaterial} in compiled form.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance
	 * @return a {@code float[]} with {@code checkerboardMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public static float[] toArray(final CheckerboardMaterial checkerboardMaterial) {
		final AngleF angle = checkerboardMaterial.getAngle();
		
		final Material materialA = checkerboardMaterial.getMaterialA();
		final Material materialB = checkerboardMaterial.getMaterialB();
		
		final Vector2F scale = checkerboardMaterial.getScale();
		
		final float[] array = new float[CHECKERBOARD_MATERIAL_LENGTH];
		
//		Because the CheckerboardMaterial occupy 8/8 positions in a block, it should be aligned.
		array[CHECKERBOARD_MATERIAL_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_A] = materialA.getID();		//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_MATERIAL_B] = materialB.getID();		//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 0] = scale.getX();			//Block #1
		array[CHECKERBOARD_MATERIAL_OFFSET_SCALE + 1] = scale.getY();			//Block #1
		array[6] = 0.0F;														//Block #1
		array[7] = 0.0F;														//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotMaterial} in compiled form.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance
	 * @return a {@code float[]} with {@code polkaDotMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public static float[] toArray(final PolkaDotMaterial polkaDotMaterial) {
		final AngleF angle = polkaDotMaterial.getAngle();
		
		final Material materialA = polkaDotMaterial.getMaterialA();
		final Material materialB = polkaDotMaterial.getMaterialB();
		
		final float cellResolution = polkaDotMaterial.getCellResolution();
		final float polkaDotRadius = polkaDotMaterial.getPolkaDotRadius();
		
		final float[] array = new float[POLKA_DOT_MATERIAL_LENGTH];
		
//		Because the PolkaDotMaterial occupy 8/8 positions in a block, it should be aligned.
		array[POLKA_DOT_MATERIAL_OFFSET_ANGLE_DEGREES] = angle.getDegrees();//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_ANGLE_RADIANS] = angle.getRadians();//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_MATERIAL_A] = materialA.getID();	//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_MATERIAL_B] = materialB.getID();	//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_CELL_RESOLUTION] = cellResolution;	//Block #1
		array[POLKA_DOT_MATERIAL_OFFSET_POLKA_DOT_RADIUS] = polkaDotRadius;	//Block #1
		array[6] = 0.0F;													//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public static int[] toArray(final ClearCoatMaterial clearCoatMaterial) {
		final Texture textureEmission = clearCoatMaterial.getTextureEmission();
		final Texture textureKD = clearCoatMaterial.getTextureKD();
		final Texture textureKS = clearCoatMaterial.getTextureKS();
		
		final int[] array = new int[CLEAR_COAT_MATERIAL_LENGTH];
		
//		Because the ClearCoatMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);									//Block #1
		array[CLEAR_COAT_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public static int[] toArray(final DisneyMaterial disneyMaterial) {
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
		
		final int[] array = new int[DISNEY_MATERIAL_LENGTH];
		
//		Because the DisneyMaterial occupy 8/8 positions in a block, it should be aligned.
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ANISOTROPIC] = pack(textureEmission.getID(), 0, textureAnisotropic.getID(), 0);			//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS] = pack(textureClearCoat.getID(), 0, textureClearCoatGloss.getID(), 0);//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION] = pack(textureColor.getID(), 0, textureDiffuseTransmission.getID(), 0);//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS] = pack(textureEta.getID(), 0, textureFlatness.getID(), 0);							//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS] = pack(textureMetallic.getID(), 0, textureRoughness.getID(), 0);				//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN] = pack(textureScatterDistance.getID(), 0, textureSheen.getID(), 0);		//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT] = pack(textureSheenTint.getID(), 0, textureSpecularTint.getID(), 0);		//Block #1
		array[DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN] = pack(textureSpecularTransmission.getID(), 0, isThin ? 1 : 0, 0);			//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public static int[] toArray(final GlassMaterial glassMaterial) {
		final Texture textureEmission = glassMaterial.getTextureEmission();
		final Texture textureEta = glassMaterial.getTextureEta();
		final Texture textureKR = glassMaterial.getTextureKR();
		final Texture textureKT = glassMaterial.getTextureKT();
		final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = glassMaterial.isRemappingRoughness();
		
		final int[] array = new int[GLASS_MATERIAL_LENGTH];
		
//		Because the GlassMaterial occupy 4/8 positions in a block, it should be aligned.
		array[GLASS_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ETA] = pack(textureEmission.getID(), 0, textureEta.getID(), 0);					//Block #1
		array[GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T] = pack(textureKR.getID(), 0, textureKT.getID(), 0);								//Block #1
		array[GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);//Block #1
		array[GLASS_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public static int[] toArray(final GlossyMaterial glossyMaterial) {
		final Texture textureEmission = glossyMaterial.getTextureEmission();
		final Texture textureKR = glossyMaterial.getTextureKR();
		final Texture textureRoughness = glossyMaterial.getTextureRoughness();
		
		final int[] array = new int[GLOSSY_MATERIAL_LENGTH];
		
//		Because the GlossyMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);											//Block #1
		array[GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS] = pack(textureKR.getID(), 0, textureRoughness.getID(), 0);	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public static int[] toArray(final MatteMaterial matteMaterial) {
		final Texture textureEmission = matteMaterial.getTextureEmission();
		final Texture textureAngle = matteMaterial.getTextureAngle();
		final Texture textureKD = matteMaterial.getTextureKD();
		
		final int[] array = new int[MATTE_MATERIAL_LENGTH];
		
//		Because the MatteMaterial occupy 2/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);									//Block #1
		array[MATTE_MATERIAL_OFFSET_TEXTURE_ANGLE_AND_TEXTURE_K_D] = pack(textureAngle.getID(), 0, textureKD.getID(), 0);	//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public static int[] toArray(final MetalMaterial metalMaterial) {
		final Texture textureEmission = metalMaterial.getTextureEmission();
		final Texture textureEta = metalMaterial.getTextureEta();
		final Texture textureK = metalMaterial.getTextureK();
		final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = metalMaterial.isRemappingRoughness();
		
		final int[] array = new int[METAL_MATERIAL_LENGTH];
		
//		Because the MetalMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);															//Block #1
		array[METAL_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_K] = pack(textureEta.getID(), 0, textureK.getID(), 0);									//Block #1
		array[METAL_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);//Block #1
		array[METAL_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public static int[] toArray(final MirrorMaterial mirrorMaterial) {
		final Texture textureEmission = mirrorMaterial.getTextureEmission();
		final Texture textureKR = mirrorMaterial.getTextureKR();
		
		final int[] array = new int[MIRROR_MATERIAL_LENGTH];
		
//		Because the MirrorMaterial occupy 1/8 positions in a block, it should be aligned.
		array[MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R] = pack(textureEmission.getID(), 0, textureKR.getID(), 0);//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public static int[] toArray(final PlasticMaterial plasticMaterial) {
		final Texture textureEmission = plasticMaterial.getTextureEmission();
		final Texture textureKD = plasticMaterial.getTextureKD();
		final Texture textureKS = plasticMaterial.getTextureKS();
		final Texture textureRoughness = plasticMaterial.getTextureRoughness();
		
		final boolean isRemappingRoughness = plasticMaterial.isRemappingRoughness();
		
		final int[] array = new int[PLASTIC_MATERIAL_LENGTH];
		
//		Because the PlasticMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);								//Block #1
		array[PLASTIC_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);	//Block #1
		array[PLASTIC_MATERIAL_OFFSET_TEXTURE_ROUGHNESS] = pack(textureRoughness.getID(), 0, 0, 0);						//Block #1
		array[PLASTIC_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;							//Block #1
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public static int[] toArray(final SubstrateMaterial substrateMaterial) {
		final Texture textureEmission = substrateMaterial.getTextureEmission();
		final Texture textureKD = substrateMaterial.getTextureKD();
		final Texture textureKS = substrateMaterial.getTextureKS();
		final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = substrateMaterial.isRemappingRoughness();
		
		final int[] array = new int[SUBSTRATE_MATERIAL_LENGTH];
		
//		Because the SubstrateMaterial occupy 4/8 positions in a block, it should be aligned.
		array[MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);																//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);								//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);//Block #1
		array[SUBSTRATE_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
}