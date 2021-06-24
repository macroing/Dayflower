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

import org.dayflower.scene.Material;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.SubstrateMaterial;

/**
 * A {@code CompiledMaterialCache} contains {@link Material} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledMaterialCache {
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
		setMaterialClearCoatMaterialArray(new int[1]);
		setMaterialDisneyMaterialArray(new int[1]);
		setMaterialGlassMaterialArray(new int[1]);
		setMaterialGlossyMaterialArray(new int[1]);
		setMaterialMatteMaterialArray(new int[1]);
		setMaterialMetalMaterialArray(new int[1]);
		setMaterialMirrorMaterialArray(new int[1]);
		setMaterialPlasticMaterialArray(new int[1]);
		setMaterialSubstrateMaterialArray(new int[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code ClearCoatMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialClearCoatMaterialCount() {
		return Structures.getStructureCount(this.materialClearCoatMaterialArray, ClearCoatMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialClearCoatMaterialArray, Objects.requireNonNull(materialClearCoatMaterial, "materialClearCoatMaterial == null"), getMaterialClearCoatMaterialCount(), ClearCoatMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialClearCoatMaterialArray, Objects.requireNonNull(materialClearCoatMaterial, "materialClearCoatMaterial == null"), getMaterialClearCoatMaterialCount(), ClearCoatMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link DisneyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code DisneyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialDisneyMaterialCount() {
		return Structures.getStructureCount(this.materialDisneyMaterialArray, DisneyMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialDisneyMaterialArray, Objects.requireNonNull(materialDisneyMaterial, "materialDisneyMaterial == null"), getMaterialDisneyMaterialCount(), DisneyMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialDisneyMaterialArray, Objects.requireNonNull(materialDisneyMaterial, "materialDisneyMaterial == null"), getMaterialDisneyMaterialCount(), DisneyMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link GlassMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlassMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialGlassMaterialCount() {
		return Structures.getStructureCount(this.materialGlassMaterialArray, GlassMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialGlassMaterialArray, Objects.requireNonNull(materialGlassMaterial, "materialGlassMaterial == null"), getMaterialGlassMaterialCount(), GlassMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialGlassMaterialArray, Objects.requireNonNull(materialGlassMaterial, "materialGlassMaterial == null"), getMaterialGlassMaterialCount(), GlassMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link GlossyMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code GlossyMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialGlossyMaterialCount() {
		return Structures.getStructureCount(this.materialGlossyMaterialArray, GlossyMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialGlossyMaterialArray, Objects.requireNonNull(materialGlossyMaterial, "materialGlossyMaterial == null"), getMaterialGlossyMaterialCount(), GlossyMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialGlossyMaterialArray, Objects.requireNonNull(materialGlossyMaterial, "materialGlossyMaterial == null"), getMaterialGlossyMaterialCount(), GlossyMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link MatteMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MatteMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMatteMaterialCount() {
		return Structures.getStructureCount(this.materialMatteMaterialArray, MatteMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialMatteMaterialArray, Objects.requireNonNull(materialMatteMaterial, "materialMatteMaterial == null"), getMaterialMatteMaterialCount(), MatteMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialMatteMaterialArray, Objects.requireNonNull(materialMatteMaterial, "materialMatteMaterial == null"), getMaterialMatteMaterialCount(), MatteMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link MetalMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MetalMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMetalMaterialCount() {
		return Structures.getStructureCount(this.materialMetalMaterialArray, MetalMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialMetalMaterialArray, Objects.requireNonNull(materialMetalMaterial, "materialMetalMaterial == null"), getMaterialMetalMaterialCount(), MetalMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialMetalMaterialArray, Objects.requireNonNull(materialMetalMaterial, "materialMetalMaterial == null"), getMaterialMetalMaterialCount(), MetalMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link MirrorMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code MirrorMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialMirrorMaterialCount() {
		return Structures.getStructureCount(this.materialMirrorMaterialArray, MirrorMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialMirrorMaterialArray, Objects.requireNonNull(materialMirrorMaterial, "materialMirrorMaterial == null"), getMaterialMirrorMaterialCount(), MirrorMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialMirrorMaterialArray, Objects.requireNonNull(materialMirrorMaterial, "materialMirrorMaterial == null"), getMaterialMirrorMaterialCount(), MirrorMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link PlasticMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code PlasticMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialPlasticMaterialCount() {
		return Structures.getStructureCount(this.materialPlasticMaterialArray, PlasticMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialPlasticMaterialArray, Objects.requireNonNull(materialPlasticMaterial, "materialPlasticMaterial == null"), getMaterialPlasticMaterialCount(), PlasticMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialPlasticMaterialArray, Objects.requireNonNull(materialPlasticMaterial, "materialPlasticMaterial == null"), getMaterialPlasticMaterialCount(), PlasticMaterial.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link SubstrateMaterial} count in this {@code CompiledMaterialCache} instance.
	 * 
	 * @return the {@code SubstrateMaterial} count in this {@code CompiledMaterialCache} instance
	 */
	public int getMaterialSubstrateMaterialCount() {
		return Structures.getStructureCount(this.materialSubstrateMaterialArray, SubstrateMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.materialSubstrateMaterialArray, Objects.requireNonNull(materialSubstrateMaterial, "materialSubstrateMaterial == null"), getMaterialSubstrateMaterialCount(), SubstrateMaterial.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.materialSubstrateMaterialArray, Objects.requireNonNull(materialSubstrateMaterial, "materialSubstrateMaterial == null"), getMaterialSubstrateMaterialCount(), SubstrateMaterial.ARRAY_LENGTH);
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
}