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
	
	private float[] bullseyeMaterialArray;
	private float[] checkerboardMaterialArray;
	private float[] polkaDotMaterialArray;
	private int[] clearCoatMaterialArray;
	private int[] disneyMaterialArray;
	private int[] glassMaterialArray;
	private int[] glossyMaterialArray;
	private int[] matteMaterialArray;
	private int[] metalMaterialArray;
	private int[] mirrorMaterialArray;
	private int[] plasticMaterialArray;
	private int[] substrateMaterialArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledMaterialCache} instance.
	 */
	public CompiledMaterialCache() {
		setBullseyeMaterialArray(new float[0]);
		setCheckerboardMaterialArray(new float[0]);
		setClearCoatMaterialArray(new int[0]);
		setDisneyMaterialArray(new int[0]);
		setGlassMaterialArray(new int[0]);
		setGlossyMaterialArray(new int[0]);
		setMatteMaterialArray(new int[0]);
		setMetalMaterialArray(new int[0]);
		setMirrorMaterialArray(new int[0]);
		setPlasticMaterialArray(new int[0]);
		setPolkaDotMaterialArray(new float[0]);
		setSubstrateMaterialArray(new int[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getBullseyeMaterialArray() {
		return this.bullseyeMaterialArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getCheckerboardMaterialArray() {
		return this.checkerboardMaterialArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PolkaDotMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public float[] getPolkaDotMaterialArray() {
		return this.polkaDotMaterialArray;
	}
	
	/**
	 * Returns the {@link BullseyeMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code BullseyeMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getBullseyeMaterialCount() {
		return Structures.getStructureCount(this.bullseyeMaterialArray, BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the absolute offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public int getBullseyeMaterialOffsetAbsolute(final float[] bullseyeMaterial) {
		return Structures.getStructureOffsetAbsolute(this.bullseyeMaterialArray, Objects.requireNonNull(bullseyeMaterial, "bullseyeMaterial == null"), getBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance in compiled form
	 * @return the relative offset of {@code bullseyeMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public int getMaterialBullseyeMaterialOffsetRelative(final float[] bullseyeMaterial) {
		return Structures.getStructureOffsetRelative(this.bullseyeMaterialArray, Objects.requireNonNull(bullseyeMaterial, "bullseyeMaterial == null"), getBullseyeMaterialCount(), BULLSEYE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code CheckerboardMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getCheckerboardMaterialCount() {
		return Structures.getStructureCount(this.checkerboardMaterialArray, CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the absolute offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public int getCheckerboardMaterialOffsetAbsolute(final float[] checkerboardMaterial) {
		return Structures.getStructureOffsetAbsolute(this.checkerboardMaterialArray, Objects.requireNonNull(checkerboardMaterial, "checkerboardMaterial == null"), getCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance in compiled form
	 * @return the relative offset of {@code checkerboardMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public int getCheckerboardMaterialOffsetRelative(final float[] checkerboardMaterial) {
		return Structures.getStructureOffsetRelative(this.checkerboardMaterialArray, Objects.requireNonNull(checkerboardMaterial, "checkerboardMaterial == null"), getCheckerboardMaterialCount(), CHECKERBOARD_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getClearCoatMaterialCount() {
		return Structures.getStructureCount(this.clearCoatMaterialArray, CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the absolute offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public int getClearCoatMaterialOffsetAbsolute(final int[] clearCoatMaterial) {
		return Structures.getStructureOffsetAbsolute(this.clearCoatMaterialArray, Objects.requireNonNull(clearCoatMaterial, "clearCoatMaterial == null"), getClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance in compiled form
	 * @return the relative offset of {@code clearCoatMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public int getClearCoatMaterialOffsetRelative(final int[] clearCoatMaterial) {
		return Structures.getStructureOffsetRelative(this.clearCoatMaterialArray, Objects.requireNonNull(clearCoatMaterial, "clearCoatMaterial == null"), getClearCoatMaterialCount(), CLEAR_COAT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link DisneyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code DisneyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getDisneyMaterialCount() {
		return Structures.getStructureCount(this.disneyMaterialArray, DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the absolute offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public int getDisneyMaterialOffsetAbsolute(final int[] disneyMaterial) {
		return Structures.getStructureOffsetAbsolute(this.disneyMaterialArray, Objects.requireNonNull(disneyMaterial, "disneyMaterial == null"), getDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance in compiled form
	 * @return the relative offset of {@code disneyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public int getDisneyMaterialOffsetRelative(final int[] disneyMaterial) {
		return Structures.getStructureOffsetRelative(this.disneyMaterialArray, Objects.requireNonNull(disneyMaterial, "disneyMaterial == null"), getDisneyMaterialCount(), DISNEY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link GlassMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlassMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getGlassMaterialCount() {
		return Structures.getStructureCount(this.glassMaterialArray, GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the absolute offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public int getGlassMaterialOffsetAbsolute(final int[] glassMaterial) {
		return Structures.getStructureOffsetAbsolute(this.glassMaterialArray, Objects.requireNonNull(glassMaterial, "glassMaterial == null"), getGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance in compiled form
	 * @return the relative offset of {@code glassMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public int getGlassMaterialOffsetRelative(final int[] glassMaterial) {
		return Structures.getStructureOffsetRelative(this.glassMaterialArray, Objects.requireNonNull(glassMaterial, "glassMaterial == null"), getGlassMaterialCount(), GLASS_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link GlossyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlossyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getGlossyMaterialCount() {
		return Structures.getStructureCount(this.glossyMaterialArray, GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the absolute offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public int getGlossyMaterialOffsetAbsolute(final int[] glossyMaterial) {
		return Structures.getStructureOffsetAbsolute(this.glossyMaterialArray, Objects.requireNonNull(glossyMaterial, "glossyMaterial == null"), getGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance in compiled form
	 * @return the relative offset of {@code glossyMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public int getGlossyMaterialOffsetRelative(final int[] glossyMaterial) {
		return Structures.getStructureOffsetRelative(this.glossyMaterialArray, Objects.requireNonNull(glossyMaterial, "glossyMaterial == null"), getGlossyMaterialCount(), GLOSSY_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MatteMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MatteMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMatteMaterialCount() {
		return Structures.getStructureCount(this.matteMaterialArray, MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the absolute offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public int getMatteMaterialOffsetAbsolute(final int[] matteMaterial) {
		return Structures.getStructureOffsetAbsolute(this.matteMaterialArray, Objects.requireNonNull(matteMaterial, "matteMaterial == null"), getMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance in compiled form
	 * @return the relative offset of {@code matteMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public int getMatteMaterialOffsetRelative(final int[] matteMaterial) {
		return Structures.getStructureOffsetRelative(this.matteMaterialArray, Objects.requireNonNull(matteMaterial, "matteMaterial == null"), getMatteMaterialCount(), MATTE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MetalMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MetalMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMetalMaterialCount() {
		return Structures.getStructureCount(this.metalMaterialArray, METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the absolute offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public int getMetalMaterialOffsetAbsolute(final int[] metalMaterial) {
		return Structures.getStructureOffsetAbsolute(this.metalMaterialArray, Objects.requireNonNull(metalMaterial, "metalMaterial == null"), getMetalMaterialCount(), METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance in compiled form
	 * @return the relative offset of {@code metalMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public int getMetalMaterialOffsetRelative(final int[] metalMaterial) {
		return Structures.getStructureOffsetRelative(this.metalMaterialArray, Objects.requireNonNull(metalMaterial, "metalMaterial == null"), getMetalMaterialCount(), METAL_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link MirrorMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MirrorMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMirrorMaterialCount() {
		return Structures.getStructureCount(this.mirrorMaterialArray, MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the absolute offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public int getMirrorMaterialOffsetAbsolute(final int[] mirrorMaterial) {
		return Structures.getStructureOffsetAbsolute(this.mirrorMaterialArray, Objects.requireNonNull(mirrorMaterial, "mirrorMaterial == null"), getMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance in compiled form
	 * @return the relative offset of {@code mirrorMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public int getMirrorMaterialOffsetRelative(final int[] mirrorMaterial) {
		return Structures.getStructureOffsetRelative(this.mirrorMaterialArray, Objects.requireNonNull(mirrorMaterial, "mirrorMaterial == null"), getMirrorMaterialCount(), MIRROR_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link PlasticMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PlasticMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getPlasticMaterialCount() {
		return Structures.getStructureCount(this.plasticMaterialArray, PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the absolute offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public int getPlasticMaterialOffsetAbsolute(final int[] plasticMaterial) {
		return Structures.getStructureOffsetAbsolute(this.plasticMaterialArray, Objects.requireNonNull(plasticMaterial, "plasticMaterial == null"), getPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance in compiled form
	 * @return the relative offset of {@code plasticMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public int getPlasticMaterialOffsetRelative(final int[] plasticMaterial) {
		return Structures.getStructureOffsetRelative(this.plasticMaterialArray, Objects.requireNonNull(plasticMaterial, "plasticMaterial == null"), getPlasticMaterialCount(), PLASTIC_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PolkaDotMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getPolkaDotMaterialCount() {
		return Structures.getStructureCount(this.polkaDotMaterialArray, POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the absolute offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public int getPolkaDotMaterialOffsetAbsolute(final float[] polkaDotMaterial) {
		return Structures.getStructureOffsetAbsolute(this.polkaDotMaterialArray, Objects.requireNonNull(polkaDotMaterial, "polkaDotMaterial == null"), getPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance in compiled form
	 * @return the relative offset of {@code polkaDotMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public int getPolkaDotMaterialOffsetRelative(final float[] polkaDotMaterial) {
		return Structures.getStructureOffsetRelative(this.polkaDotMaterialArray, Objects.requireNonNull(polkaDotMaterial, "polkaDotMaterial == null"), getPolkaDotMaterialCount(), POLKA_DOT_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the {@link SubstrateMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code SubstrateMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getSubstrateMaterialCount() {
		return Structures.getStructureCount(this.substrateMaterialArray, SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the absolute offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public int getSubstrateMaterialOffsetAbsolute(final int[] substrateMaterial) {
		return Structures.getStructureOffsetAbsolute(this.substrateMaterialArray, Objects.requireNonNull(substrateMaterial, "substrateMaterial == null"), getSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance in compiled form
	 * @return the relative offset of {@code substrateMaterial} in this {@code CompiledMaterialCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public int getSubstrateMaterialOffsetRelative(final int[] substrateMaterial) {
		return Structures.getStructureOffsetRelative(this.substrateMaterialArray, Objects.requireNonNull(substrateMaterial, "substrateMaterial == null"), getSubstrateMaterialCount(), SUBSTRATE_MATERIAL_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code ClearCoatMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getClearCoatMaterialArray() {
		return this.clearCoatMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code DisneyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getDisneyMaterialArray() {
		return this.disneyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlassMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getGlassMaterialArray() {
		return this.glassMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code GlossyMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getGlossyMaterialArray() {
		return this.glossyMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MatteMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMatteMaterialArray() {
		return this.matteMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MetalMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMetalMaterialArray() {
		return this.metalMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code MirrorMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getMirrorMaterialArray() {
		return this.mirrorMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code PlasticMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getPlasticMaterialArray() {
		return this.plasticMaterialArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code SubstrateMaterial} instances in compiled form that are associated with this {@code CompiledMaterialCache} instance
	 */
	public int[] getSubstrateMaterialArray() {
		return this.substrateMaterialArray;
	}
	
	/**
	 * Sets all {@link BullseyeMaterial} instances in compiled form to {@code bullseyeMaterialArray}.
	 * <p>
	 * If {@code bullseyeMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeMaterialArray the {@code BullseyeMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterialArray} is {@code null}
	 */
	public void setBullseyeMaterialArray(final float[] bullseyeMaterialArray) {
		this.bullseyeMaterialArray = Objects.requireNonNull(bullseyeMaterialArray, "bullseyeMaterialArray == null");
	}
	
	/**
	 * Sets all {@link CheckerboardMaterial} instances in compiled form to {@code checkerboardMaterialArray}.
	 * <p>
	 * If {@code checkerboardMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardMaterialArray the {@code CheckerboardMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterialArray} is {@code null}
	 */
	public void setCheckerboardMaterialArray(final float[] checkerboardMaterialArray) {
		this.checkerboardMaterialArray = Objects.requireNonNull(checkerboardMaterialArray, "checkerboardMaterialArray == null");
	}
	
	/**
	 * Sets all {@link ClearCoatMaterial} instances in compiled form to {@code clearCoatMaterialArray}.
	 * <p>
	 * If {@code clearCoatMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param clearCoatMaterialArray the {@code ClearCoatMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterialArray} is {@code null}
	 */
	public void setClearCoatMaterialArray(final int[] clearCoatMaterialArray) {
		this.clearCoatMaterialArray = Objects.requireNonNull(clearCoatMaterialArray, "clearCoatMaterialArray == null");
	}
	
	/**
	 * Sets all {@link DisneyMaterial} instances in compiled form to {@code disneyMaterialArray}.
	 * <p>
	 * If {@code disneyMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param disneyMaterialArray the {@code DisneyMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterialArray} is {@code null}
	 */
	public void setDisneyMaterialArray(final int[] disneyMaterialArray) {
		this.disneyMaterialArray = Objects.requireNonNull(disneyMaterialArray, "disneyMaterialArray == null");
	}
	
	/**
	 * Sets all {@link GlassMaterial} instances in compiled form to {@code glassMaterialArray}.
	 * <p>
	 * If {@code glassMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glassMaterialArray the {@code GlassMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterialArray} is {@code null}
	 */
	public void setGlassMaterialArray(final int[] glassMaterialArray) {
		this.glassMaterialArray = Objects.requireNonNull(glassMaterialArray, "glassMaterialArray == null");
	}
	
	/**
	 * Sets all {@link GlossyMaterial} instances in compiled form to {@code glossyMaterialArray}.
	 * <p>
	 * If {@code glossyMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param glossyMaterialArray the {@code GlossyMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterialArray} is {@code null}
	 */
	public void setGlossyMaterialArray(final int[] glossyMaterialArray) {
		this.glossyMaterialArray = Objects.requireNonNull(glossyMaterialArray, "glossyMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MatteMaterial} instances in compiled form to {@code matteMaterialArray}.
	 * <p>
	 * If {@code matteMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matteMaterialArray the {@code MatteMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterialArray} is {@code null}
	 */
	public void setMatteMaterialArray(final int[] matteMaterialArray) {
		this.matteMaterialArray = Objects.requireNonNull(matteMaterialArray, "matteMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MetalMaterial} instances in compiled form to {@code metalMaterialArray}.
	 * <p>
	 * If {@code metalMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param metalMaterialArray the {@code MetalMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterialArray} is {@code null}
	 */
	public void setMetalMaterialArray(final int[] metalMaterialArray) {
		this.metalMaterialArray = Objects.requireNonNull(metalMaterialArray, "metalMaterialArray == null");
	}
	
	/**
	 * Sets all {@link MirrorMaterial} instances in compiled form to {@code mirrorMaterialArray}.
	 * <p>
	 * If {@code mirrorMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param mirrorMaterialArray the {@code MirrorMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterialArray} is {@code null}
	 */
	public void setMirrorMaterialArray(final int[] mirrorMaterialArray) {
		this.mirrorMaterialArray = Objects.requireNonNull(mirrorMaterialArray, "mirrorMaterialArray == null");
	}
	
	/**
	 * Sets all {@link PlasticMaterial} instances in compiled form to {@code plasticMaterialArray}.
	 * <p>
	 * If {@code plasticMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param plasticMaterialArray the {@code PlasticMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterialArray} is {@code null}
	 */
	public void setPlasticMaterialArray(final int[] plasticMaterialArray) {
		this.plasticMaterialArray = Objects.requireNonNull(plasticMaterialArray, "plasticMaterialArray == null");
	}
	
	/**
	 * Sets all {@link PolkaDotMaterial} instances in compiled form to {@code polkaDotMaterialArray}.
	 * <p>
	 * If {@code polkaDotMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotMaterialArray the {@code PolkaDotMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterialArray} is {@code null}
	 */
	public void setPolkaDotMaterialArray(final float[] polkaDotMaterialArray) {
		this.polkaDotMaterialArray = Objects.requireNonNull(polkaDotMaterialArray, "polkaDotMaterialArray == null");
	}
	
	/**
	 * Sets all {@link SubstrateMaterial} instances in compiled form to {@code substrateMaterialArray}.
	 * <p>
	 * If {@code substrateMaterialArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param substrateMaterialArray the {@code SubstrateMaterial} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterialArray} is {@code null}
	 */
	public void setSubstrateMaterialArray(final int[] substrateMaterialArray) {
		this.substrateMaterialArray = Objects.requireNonNull(substrateMaterialArray, "substrateMaterialArray == null");
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
	 * compiledMaterialCache.toBullseyeMaterialArray(bullseyeMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeMaterial a {@link BullseyeMaterial} instance
	 * @return a {@code float[]} with {@code bullseyeMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterial} is {@code null}
	 */
	public static float[] toBullseyeMaterialArray(final BullseyeMaterial bullseyeMaterial) {
		return toBullseyeMaterialArray(bullseyeMaterial, material -> 0);
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
	public static float[] toBullseyeMaterialArray(final BullseyeMaterial bullseyeMaterial, final ToIntFunction<Material> materialOffsetFunction) {
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
	 * compiledMaterialCache.toBullseyeMaterialArray(bullseyeMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeMaterials a {@code List} of {@code BullseyeMaterial} instances
	 * @return a {@code float[]} with all {@code BullseyeMaterial} instances in {@code bullseyeMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toBullseyeMaterialArray(final List<BullseyeMaterial> bullseyeMaterials) {
		return toBullseyeMaterialArray(bullseyeMaterials, material -> 0);
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
	public static float[] toBullseyeMaterialArray(final List<BullseyeMaterial> bullseyeMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Floats.toArray(bullseyeMaterials, bullseyeMaterial -> toBullseyeMaterialArray(bullseyeMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardMaterial} in compiled form.
	 * <p>
	 * If {@code checkerboardMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toCheckerboardMaterialArray(checkerboardMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardMaterial a {@link CheckerboardMaterial} instance
	 * @return a {@code float[]} with {@code checkerboardMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterial} is {@code null}
	 */
	public static float[] toCheckerboardMaterialArray(final CheckerboardMaterial checkerboardMaterial) {
		return toCheckerboardMaterialArray(checkerboardMaterial, material -> 0);
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
	public static float[] toCheckerboardMaterialArray(final CheckerboardMaterial checkerboardMaterial, final ToIntFunction<Material> materialOffsetFunction) {
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
	 * compiledMaterialCache.toCheckerboardMaterialArray(checkerboardMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardMaterials a {@code List} of {@code CheckerboardMaterial} instances
	 * @return a {@code float[]} with all {@code CheckerboardMaterial} instances in {@code checkerboardMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toCheckerboardMaterialArray(final List<CheckerboardMaterial> checkerboardMaterials) {
		return toCheckerboardMaterialArray(checkerboardMaterials, material -> 0);
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
	public static float[] toCheckerboardMaterialArray(final List<CheckerboardMaterial> checkerboardMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Floats.toArray(checkerboardMaterials, checkerboardMaterial -> toCheckerboardMaterialArray(checkerboardMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form.
	 * <p>
	 * If {@code polkaDotMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPolkaDotMaterialArray(polkaDotMaterials, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotMaterials a {@code List} of {@code PolkaDotMaterial} instances
	 * @return a {@code float[]} with all {@code PolkaDotMaterial} instances in {@code polkaDotMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterials} or at least one of its elements are {@code null}
	 */
	public static float[] toPolkaDotMaterialArray(final List<PolkaDotMaterial> polkaDotMaterials) {
		return toPolkaDotMaterialArray(polkaDotMaterials, material -> 0);
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
	public static float[] toPolkaDotMaterialArray(final List<PolkaDotMaterial> polkaDotMaterials, final ToIntFunction<Material> materialOffsetFunction) {
		return Floats.toArray(polkaDotMaterials, polkaDotMaterial -> toPolkaDotMaterialArray(polkaDotMaterial, materialOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotMaterial} in compiled form.
	 * <p>
	 * If {@code polkaDotMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPolkaDotMaterialArray(polkaDotMaterial, material -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotMaterial a {@link PolkaDotMaterial} instance
	 * @return a {@code float[]} with {@code polkaDotMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotMaterial} is {@code null}
	 */
	public static float[] toPolkaDotMaterialArray(final PolkaDotMaterial polkaDotMaterial) {
		return toPolkaDotMaterialArray(polkaDotMaterial, material -> 0);
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
	public static float[] toPolkaDotMaterialArray(final PolkaDotMaterial polkaDotMaterial, final ToIntFunction<Material> materialOffsetFunction) {
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
	 * Returns an {@code int[]} with {@code clearCoatMaterial} in compiled form.
	 * <p>
	 * If {@code clearCoatMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toClearCoatMaterialArray(clearCoatMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterial a {@link ClearCoatMaterial} instance
	 * @return an {@code int[]} with {@code clearCoatMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterial} is {@code null}
	 */
	public static int[] toClearCoatMaterialArray(final ClearCoatMaterial clearCoatMaterial) {
		return toClearCoatMaterialArray(clearCoatMaterial, texture -> 0);
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
	public static int[] toClearCoatMaterialArray(final ClearCoatMaterial clearCoatMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * compiledMaterialCache.toClearCoatMaterialArray(clearCoatMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param clearCoatMaterials a {@code List} of {@code ClearCoatMaterial} instances
	 * @return an {@code int[]} with all {@code ClearCoatMaterial} instances in {@code clearCoatMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code clearCoatMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toClearCoatMaterialArray(final List<ClearCoatMaterial> clearCoatMaterials) {
		return toClearCoatMaterialArray(clearCoatMaterials, texture -> 0);
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
	public static int[] toClearCoatMaterialArray(final List<ClearCoatMaterial> clearCoatMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(clearCoatMaterials, clearCoatMaterial -> toClearCoatMaterialArray(clearCoatMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code disneyMaterial} in compiled form.
	 * <p>
	 * If {@code disneyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toDisneyMaterialArray(disneyMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterial a {@link DisneyMaterial} instance
	 * @return an {@code int[]} with {@code disneyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterial} is {@code null}
	 */
	public static int[] toDisneyMaterialArray(final DisneyMaterial disneyMaterial) {
		return toDisneyMaterialArray(disneyMaterial, texture -> 0);
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
	public static int[] toDisneyMaterialArray(final DisneyMaterial disneyMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * compiledMaterialCache.toDisneyMaterialArray(disneyMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param disneyMaterials a {@code List} of {@code DisneyMaterial} instances
	 * @return an {@code int[]} with all {@code DisneyMaterial} instances in {@code disneyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code disneyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toDisneyMaterialArray(final List<DisneyMaterial> disneyMaterials) {
		return toDisneyMaterialArray(disneyMaterials, texture -> 0);
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
	public static int[] toDisneyMaterialArray(final List<DisneyMaterial> disneyMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(disneyMaterials, disneyMaterial -> toDisneyMaterialArray(disneyMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glassMaterial} in compiled form.
	 * <p>
	 * If {@code glassMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlassMaterialArray(glassMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterial a {@link GlassMaterial} instance
	 * @return an {@code int[]} with {@code glassMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterial} is {@code null}
	 */
	public static int[] toGlassMaterialArray(final GlassMaterial glassMaterial) {
		return toGlassMaterialArray(glassMaterial, texture -> 0);
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
	public static int[] toGlassMaterialArray(final GlassMaterial glassMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * compiledMaterialCache.toGlassMaterialArray(glassMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glassMaterials a {@code List} of {@code GlassMaterial} instances
	 * @return an {@code int[]} with all {@code GlassMaterial} instances in {@code glassMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glassMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlassMaterialArray(final List<GlassMaterial> glassMaterials) {
		return toGlassMaterialArray(glassMaterials, texture -> 0);
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
	public static int[] toGlassMaterialArray(final List<GlassMaterial> glassMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(glassMaterials, glassMaterial -> toGlassMaterialArray(glassMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code glossyMaterial} in compiled form.
	 * <p>
	 * If {@code glossyMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toGlossyMaterialArray(glossyMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterial a {@link GlossyMaterial} instance
	 * @return an {@code int[]} with {@code glossyMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterial} is {@code null}
	 */
	public static int[] toGlossyMaterialArray(final GlossyMaterial glossyMaterial) {
		return toGlossyMaterialArray(glossyMaterial, texture -> 0);
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
	public static int[] toGlossyMaterialArray(final GlossyMaterial glossyMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * compiledMaterialCache.toGlossyMaterialArray(glossyMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param glossyMaterials a {@code List} of {@code GlossyMaterial} instances
	 * @return an {@code int[]} with all {@code GlossyMaterial} instances in {@code glossyMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code glossyMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toGlossyMaterialArray(final List<GlossyMaterial> glossyMaterials) {
		return toGlossyMaterialArray(glossyMaterials, texture -> 0);
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
	public static int[] toGlossyMaterialArray(final List<GlossyMaterial> glossyMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(glossyMaterials, glossyMaterial -> toGlossyMaterialArray(glossyMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with all {@link MatteMaterial} instances in {@code matteMaterials} in compiled form.
	 * <p>
	 * If {@code matteMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMatteMaterialArray(matteMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterials a {@code List} of {@code MatteMaterial} instances
	 * @return an {@code int[]} with all {@code MatteMaterial} instances in {@code matteMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMatteMaterialArray(final List<MatteMaterial> matteMaterials) {
		return toMatteMaterialArray(matteMaterials, texture -> 0);
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
	public static int[] toMatteMaterialArray(final List<MatteMaterial> matteMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(matteMaterials, matteMaterial -> toMatteMaterialArray(matteMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code matteMaterial} in compiled form.
	 * <p>
	 * If {@code matteMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMatteMaterialArray(matteMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param matteMaterial a {@link MatteMaterial} instance
	 * @return an {@code int[]} with {@code matteMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matteMaterial} is {@code null}
	 */
	public static int[] toMatteMaterialArray(final MatteMaterial matteMaterial) {
		return toMatteMaterialArray(matteMaterial, texture -> 0);
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
	public static int[] toMatteMaterialArray(final MatteMaterial matteMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * Returns an {@code int[]} with all {@link MetalMaterial} instances in {@code metalMaterials} in compiled form.
	 * <p>
	 * If {@code metalMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMetalMaterialArray(metalMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterials a {@code List} of {@code MetalMaterial} instances
	 * @return an {@code int[]} with all {@code MetalMaterial} instances in {@code metalMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMetalMaterialArray(final List<MetalMaterial> metalMaterials) {
		return toMetalMaterialArray(metalMaterials, texture -> 0);
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
	public static int[] toMetalMaterialArray(final List<MetalMaterial> metalMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(metalMaterials, metalMaterial -> toMetalMaterialArray(metalMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code metalMaterial} in compiled form.
	 * <p>
	 * If {@code metalMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMetalMaterialArray(metalMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param metalMaterial a {@link MetalMaterial} instance
	 * @return an {@code int[]} with {@code metalMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code metalMaterial} is {@code null}
	 */
	public static int[] toMetalMaterialArray(final MetalMaterial metalMaterial) {
		return toMetalMaterialArray(metalMaterial, texture -> 0);
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
	public static int[] toMetalMaterialArray(final MetalMaterial metalMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * Returns an {@code int[]} with all {@link MirrorMaterial} instances in {@code mirrorMaterials} in compiled form.
	 * <p>
	 * If {@code mirrorMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMirrorMaterialArray(mirrorMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterials a {@code List} of {@code MirrorMaterial} instances
	 * @return an {@code int[]} with all {@code MirrorMaterial} instances in {@code mirrorMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toMirrorMaterialArray(final List<MirrorMaterial> mirrorMaterials) {
		return toMirrorMaterialArray(mirrorMaterials, texture -> 0);
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
	public static int[] toMirrorMaterialArray(final List<MirrorMaterial> mirrorMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(mirrorMaterials, mirrorMaterial -> toMirrorMaterialArray(mirrorMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code mirrorMaterial} in compiled form.
	 * <p>
	 * If {@code mirrorMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toMirrorMaterialArray(mirrorMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param mirrorMaterial a {@link MirrorMaterial} instance
	 * @return an {@code int[]} with {@code mirrorMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code mirrorMaterial} is {@code null}
	 */
	public static int[] toMirrorMaterialArray(final MirrorMaterial mirrorMaterial) {
		return toMirrorMaterialArray(mirrorMaterial, texture -> 0);
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
	public static int[] toMirrorMaterialArray(final MirrorMaterial mirrorMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureEmission = mirrorMaterial.getTextureEmission();
		final Texture textureKR = mirrorMaterial.getTextureKR();
		
		final int textureEmissionAndTextureKRValue = pack(textureEmission.getID(), textureOffsetFunction.applyAsInt(textureEmission), textureKR.getID(), textureOffsetFunction.applyAsInt(textureKR));
		
		final int[] array = new int[MIRROR_MATERIAL_LENGTH];
		
//		Because the MirrorMaterial occupy 1/8 positions in a block, it should be aligned.
		array[MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R] = textureEmissionAndTextureKRValue;//Block #1
		
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
	 * compiledMaterialCache.toPlasticMaterialArray(plasticMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterials a {@code List} of {@code PlasticMaterial} instances
	 * @return an {@code int[]} with all {@code PlasticMaterial} instances in {@code plasticMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toPlasticMaterialArray(final List<PlasticMaterial> plasticMaterials) {
		return toPlasticMaterialArray(plasticMaterials, texture -> 0);
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
	public static int[] toPlasticMaterialArray(final List<PlasticMaterial> plasticMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(plasticMaterials, plasticMaterial -> toPlasticMaterialArray(plasticMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code plasticMaterial} in compiled form.
	 * <p>
	 * If {@code plasticMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toPlasticMaterialArray(plasticMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param plasticMaterial a {@link PlasticMaterial} instance
	 * @return an {@code int[]} with {@code plasticMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code plasticMaterial} is {@code null}
	 */
	public static int[] toPlasticMaterialArray(final PlasticMaterial plasticMaterial) {
		return toPlasticMaterialArray(plasticMaterial, texture -> 0);
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
	public static int[] toPlasticMaterialArray(final PlasticMaterial plasticMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
	 * Returns an {@code int[]} with all {@link SubstrateMaterial} instances in {@code substrateMaterials} in compiled form.
	 * <p>
	 * If {@code substrateMaterials} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toSubstrateMaterialArray(substrateMaterials, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterials a {@code List} of {@code SubstrateMaterial} instances
	 * @return an {@code int[]} with all {@code SubstrateMaterial} instances in {@code substrateMaterials} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterials} or at least one of its elements are {@code null}
	 */
	public static int[] toSubstrateMaterialArray(final List<SubstrateMaterial> substrateMaterials) {
		return toSubstrateMaterialArray(substrateMaterials, texture -> 0);
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
	public static int[] toSubstrateMaterialArray(final List<SubstrateMaterial> substrateMaterials, final ToIntFunction<Texture> textureOffsetFunction) {
		return Ints.toArray(substrateMaterials, substrateMaterial -> toSubstrateMaterialArray(substrateMaterial, textureOffsetFunction));
	}
	
	/**
	 * Returns an {@code int[]} with {@code substrateMaterial} in compiled form.
	 * <p>
	 * If {@code substrateMaterial} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledMaterialCache.toSubstrateMaterialArray(substrateMaterial, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param substrateMaterial a {@link SubstrateMaterial} instance
	 * @return an {@code int[]} with {@code substrateMaterial} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code substrateMaterial} is {@code null}
	 */
	public static int[] toSubstrateMaterialArray(final SubstrateMaterial substrateMaterial) {
		return toSubstrateMaterialArray(substrateMaterial, texture -> 0);
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
	public static int[] toSubstrateMaterialArray(final SubstrateMaterial substrateMaterial, final ToIntFunction<Texture> textureOffsetFunction) {
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
}