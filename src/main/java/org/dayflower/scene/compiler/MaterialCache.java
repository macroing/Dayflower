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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;
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

final class MaterialCache {
	private final List<BullseyeMaterial> distinctBullseyeMaterials;
	private final List<CheckerboardMaterial> distinctCheckerboardMaterials;
	private final List<ClearCoatMaterial> distinctClearCoatMaterials;
	private final List<DisneyMaterial> distinctDisneyMaterials;
	private final List<GlassMaterial> distinctGlassMaterials;
	private final List<GlossyMaterial> distinctGlossyMaterials;
	private final List<Material> distinctMaterials;
	private final List<MatteMaterial> distinctMatteMaterials;
	private final List<MetalMaterial> distinctMetalMaterials;
	private final List<MirrorMaterial> distinctMirrorMaterials;
	private final List<PlasticMaterial> distinctPlasticMaterials;
	private final List<PolkaDotMaterial> distinctPolkaDotMaterials;
	private final List<SubstrateMaterial> distinctSubstrateMaterials;
	private final Map<BullseyeMaterial, Integer> distinctToOffsetsBullseyeMaterials;
	private final Map<CheckerboardMaterial, Integer> distinctToOffsetsCheckerboardMaterials;
	private final Map<ClearCoatMaterial, Integer> distinctToOffsetsClearCoatMaterials;
	private final Map<DisneyMaterial, Integer> distinctToOffsetsDisneyMaterials;
	private final Map<GlassMaterial, Integer> distinctToOffsetsGlassMaterials;
	private final Map<GlossyMaterial, Integer> distinctToOffsetsGlossyMaterials;
	private final Map<MatteMaterial, Integer> distinctToOffsetsMatteMaterials;
	private final Map<MetalMaterial, Integer> distinctToOffsetsMetalMaterials;
	private final Map<MirrorMaterial, Integer> distinctToOffsetsMirrorMaterials;
	private final Map<PlasticMaterial, Integer> distinctToOffsetsPlasticMaterials;
	private final Map<PolkaDotMaterial, Integer> distinctToOffsetsPolkaDotMaterials;
	private final Map<SubstrateMaterial, Integer> distinctToOffsetsSubstrateMaterials;
	private final NodeCache nodeCache;
	private final TextureCache textureCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public MaterialCache(final NodeCache nodeCache, final TextureCache textureCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.textureCache = Objects.requireNonNull(textureCache, "textureCache == null");
		this.distinctBullseyeMaterials = new ArrayList<>();
		this.distinctCheckerboardMaterials = new ArrayList<>();
		this.distinctClearCoatMaterials = new ArrayList<>();
		this.distinctDisneyMaterials = new ArrayList<>();
		this.distinctGlassMaterials = new ArrayList<>();
		this.distinctGlossyMaterials = new ArrayList<>();
		this.distinctMaterials = new ArrayList<>();
		this.distinctMatteMaterials = new ArrayList<>();
		this.distinctMetalMaterials = new ArrayList<>();
		this.distinctMirrorMaterials = new ArrayList<>();
		this.distinctPlasticMaterials = new ArrayList<>();
		this.distinctPolkaDotMaterials = new ArrayList<>();
		this.distinctSubstrateMaterials = new ArrayList<>();
		this.distinctToOffsetsBullseyeMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsCheckerboardMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsClearCoatMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsDisneyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlassMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlossyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMatteMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMetalMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMirrorMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsPlasticMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsPolkaDotMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsSubstrateMaterials = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final Material material) {
		return this.distinctMaterials.contains(Objects.requireNonNull(material, "material == null"));
	}
	
	public float[] toBullseyeMaterials() {
		return CompiledMaterialCache.toBullseyeMaterials(this.distinctBullseyeMaterials, this::findOffsetFor);
	}
	
	public float[] toCheckerboardMaterials() {
		return CompiledMaterialCache.toCheckerboardMaterials(this.distinctCheckerboardMaterials, this::findOffsetFor);
	}
	
	public float[] toPolkaDotMaterials() {
		return CompiledMaterialCache.toPolkaDotMaterials(this.distinctPolkaDotMaterials, this::findOffsetFor);
	}
	
	public int findOffsetFor(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		if(material instanceof BullseyeMaterial) {
			return this.distinctToOffsetsBullseyeMaterials.get(material).intValue();
		} else if(material instanceof CheckerboardMaterial) {
			return this.distinctToOffsetsCheckerboardMaterials.get(material).intValue();
		} else if(material instanceof ClearCoatMaterial) {
			return this.distinctToOffsetsClearCoatMaterials.get(material).intValue();
		} else if(material instanceof DisneyMaterial) {
			return this.distinctToOffsetsDisneyMaterials.get(material).intValue();
		} else if(material instanceof GlassMaterial) {
			return this.distinctToOffsetsGlassMaterials.get(material).intValue();
		} else if(material instanceof GlossyMaterial) {
			return this.distinctToOffsetsGlossyMaterials.get(material).intValue();
		} else if(material instanceof MatteMaterial) {
			return this.distinctToOffsetsMatteMaterials.get(material).intValue();
		} else if(material instanceof MetalMaterial) {
			return this.distinctToOffsetsMetalMaterials.get(material).intValue();
		} else if(material instanceof MirrorMaterial) {
			return this.distinctToOffsetsMirrorMaterials.get(material).intValue();
		} else if(material instanceof PlasticMaterial) {
			return this.distinctToOffsetsPlasticMaterials.get(material).intValue();
		} else if(material instanceof PolkaDotMaterial) {
			return this.distinctToOffsetsPolkaDotMaterials.get(material).intValue();
		} else if(material instanceof SubstrateMaterial) {
			return this.distinctToOffsetsSubstrateMaterials.get(material).intValue();
		} else {
			return 0;
		}
	}
	
	public int[] toClearCoatMaterials() {
		return CompiledMaterialCache.toClearCoatMaterials(this.distinctClearCoatMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toDisneyMaterials() {
		return CompiledMaterialCache.toDisneyMaterials(this.distinctDisneyMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toGlassMaterials() {
		return CompiledMaterialCache.toGlassMaterials(this.distinctGlassMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toGlossyMaterials() {
		return CompiledMaterialCache.toGlossyMaterials(this.distinctGlossyMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toMatteMaterials() {
		return CompiledMaterialCache.toMatteMaterials(this.distinctMatteMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toMetalMaterials() {
		return CompiledMaterialCache.toMetalMaterials(this.distinctMetalMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toMirrorMaterials() {
		return CompiledMaterialCache.toMirrorMaterials(this.distinctMirrorMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toPlasticMaterials() {
		return CompiledMaterialCache.toPlasticMaterials(this.distinctPlasticMaterials, this.textureCache::findOffsetFor);
	}
	
	public int[] toSubstrateMaterials() {
		return CompiledMaterialCache.toSubstrateMaterials(this.distinctSubstrateMaterials, this.textureCache::findOffsetFor);
	}
	
	public void build(final CompiledMaterialCache compiledMaterialCache) {
		compiledMaterialCache.setBullseyeMaterials(toBullseyeMaterials());
		compiledMaterialCache.setCheckerboardMaterials(toCheckerboardMaterials());
		compiledMaterialCache.setClearCoatMaterials(toClearCoatMaterials());
		compiledMaterialCache.setDisneyMaterials(toDisneyMaterials());
		compiledMaterialCache.setGlassMaterials(toGlassMaterials());
		compiledMaterialCache.setGlossyMaterials(toGlossyMaterials());
		compiledMaterialCache.setMatteMaterials(toMatteMaterials());
		compiledMaterialCache.setMetalMaterials(toMetalMaterials());
		compiledMaterialCache.setMirrorMaterials(toMirrorMaterials());
		compiledMaterialCache.setPlasticMaterials(toPlasticMaterials());
		compiledMaterialCache.setPolkaDotMaterials(toPolkaDotMaterials());
		compiledMaterialCache.setSubstrateMaterials(toSubstrateMaterials());
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledMaterialCache());
	}
	
	public void clear() {
		this.distinctBullseyeMaterials.clear();
		this.distinctCheckerboardMaterials.clear();
		this.distinctClearCoatMaterials.clear();
		this.distinctDisneyMaterials.clear();
		this.distinctGlassMaterials.clear();
		this.distinctGlossyMaterials.clear();
		this.distinctMaterials.clear();
		this.distinctMatteMaterials.clear();
		this.distinctMetalMaterials.clear();
		this.distinctMirrorMaterials.clear();
		this.distinctPlasticMaterials.clear();
		this.distinctPolkaDotMaterials.clear();
		this.distinctSubstrateMaterials.clear();
		this.distinctToOffsetsBullseyeMaterials.clear();
		this.distinctToOffsetsCheckerboardMaterials.clear();
		this.distinctToOffsetsClearCoatMaterials.clear();
		this.distinctToOffsetsDisneyMaterials.clear();
		this.distinctToOffsetsGlassMaterials.clear();
		this.distinctToOffsetsGlossyMaterials.clear();
		this.distinctToOffsetsMatteMaterials.clear();
		this.distinctToOffsetsMetalMaterials.clear();
		this.distinctToOffsetsMirrorMaterials.clear();
		this.distinctToOffsetsPlasticMaterials.clear();
		this.distinctToOffsetsPolkaDotMaterials.clear();
		this.distinctToOffsetsSubstrateMaterials.clear();
	}
	
	public void setup() {
//		Add all distinct BullseyeMaterial instances:
		this.distinctBullseyeMaterials.clear();
		this.distinctBullseyeMaterials.addAll(this.nodeCache.getAllDistinct(BullseyeMaterial.class));
		
//		Add all distinct CheckerboardMaterial instances:
		this.distinctCheckerboardMaterials.clear();
		this.distinctCheckerboardMaterials.addAll(this.nodeCache.getAllDistinct(CheckerboardMaterial.class));
		
//		Add all distinct ClearCoatMaterial instances:
		this.distinctClearCoatMaterials.clear();
		this.distinctClearCoatMaterials.addAll(this.nodeCache.getAllDistinct(ClearCoatMaterial.class));
		
//		Add all distinct DisneyMaterial instances:
		this.distinctDisneyMaterials.clear();
		this.distinctDisneyMaterials.addAll(this.nodeCache.getAllDistinct(DisneyMaterial.class));
		
//		Add all distinct GlassMaterial instances:
		this.distinctGlassMaterials.clear();
		this.distinctGlassMaterials.addAll(this.nodeCache.getAllDistinct(GlassMaterial.class));
		
//		Add all distinct GlossyMaterial instances:
		this.distinctGlossyMaterials.clear();
		this.distinctGlossyMaterials.addAll(this.nodeCache.getAllDistinct(GlossyMaterial.class));
		
//		Add all distinct MatteMaterial instances:
		this.distinctMatteMaterials.clear();
		this.distinctMatteMaterials.addAll(this.nodeCache.getAllDistinct(MatteMaterial.class));
		
//		Add all distinct MetalMaterial instances:
		this.distinctMetalMaterials.clear();
		this.distinctMetalMaterials.addAll(this.nodeCache.getAllDistinct(MetalMaterial.class));
		
//		Add all distinct MirrorMaterial instances:
		this.distinctMirrorMaterials.clear();
		this.distinctMirrorMaterials.addAll(this.nodeCache.getAllDistinct(MirrorMaterial.class));
		
//		Add all distinct PlasticMaterial instances:
		this.distinctPlasticMaterials.clear();
		this.distinctPlasticMaterials.addAll(this.nodeCache.getAllDistinct(PlasticMaterial.class));
		
//		Add all distinct PolkaDotMaterial instances:
		this.distinctPolkaDotMaterials.clear();
		this.distinctPolkaDotMaterials.addAll(this.nodeCache.getAllDistinct(PolkaDotMaterial.class));
		
//		Add all distinct SubstrateMaterial instances:
		this.distinctSubstrateMaterials.clear();
		this.distinctSubstrateMaterials.addAll(this.nodeCache.getAllDistinct(SubstrateMaterial.class));
		
//		Add all distinct Material instances:
		this.distinctMaterials.clear();
		this.distinctMaterials.addAll(this.distinctBullseyeMaterials);
		this.distinctMaterials.addAll(this.distinctCheckerboardMaterials);
		this.distinctMaterials.addAll(this.distinctClearCoatMaterials);
		this.distinctMaterials.addAll(this.distinctDisneyMaterials);
		this.distinctMaterials.addAll(this.distinctGlassMaterials);
		this.distinctMaterials.addAll(this.distinctGlossyMaterials);
		this.distinctMaterials.addAll(this.distinctMatteMaterials);
		this.distinctMaterials.addAll(this.distinctMetalMaterials);
		this.distinctMaterials.addAll(this.distinctMirrorMaterials);
		this.distinctMaterials.addAll(this.distinctPlasticMaterials);
		this.distinctMaterials.addAll(this.distinctPolkaDotMaterials);
		this.distinctMaterials.addAll(this.distinctSubstrateMaterials);
		
//		Create offset mappings for all distinct BullseyeMaterial instances:
		this.distinctToOffsetsBullseyeMaterials.clear();
		this.distinctToOffsetsBullseyeMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBullseyeMaterials, CompiledMaterialCache.BULLSEYE_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct CheckerboardMaterial instances:
		this.distinctToOffsetsCheckerboardMaterials.clear();
		this.distinctToOffsetsCheckerboardMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCheckerboardMaterials, CompiledMaterialCache.CHECKERBOARD_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct ClearCoatMaterial instances:
		this.distinctToOffsetsClearCoatMaterials.clear();
		this.distinctToOffsetsClearCoatMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctClearCoatMaterials, CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct DisneyMaterial instances:
		this.distinctToOffsetsDisneyMaterials.clear();
		this.distinctToOffsetsDisneyMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDisneyMaterials, CompiledMaterialCache.DISNEY_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct GlassMaterial instances:
		this.distinctToOffsetsGlassMaterials.clear();
		this.distinctToOffsetsGlassMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctGlassMaterials, CompiledMaterialCache.GLASS_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct GlossyMaterial instances:
		this.distinctToOffsetsGlossyMaterials.clear();
		this.distinctToOffsetsGlossyMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctGlossyMaterials, CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct MatteMaterial instances:
		this.distinctToOffsetsMatteMaterials.clear();
		this.distinctToOffsetsMatteMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMatteMaterials, CompiledMaterialCache.MATTE_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct MetalMaterial instances:
		this.distinctToOffsetsMetalMaterials.clear();
		this.distinctToOffsetsMetalMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMetalMaterials, CompiledMaterialCache.METAL_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct MirrorMaterial instances:
		this.distinctToOffsetsMirrorMaterials.clear();
		this.distinctToOffsetsMirrorMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMirrorMaterials, CompiledMaterialCache.MIRROR_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct PlasticMaterial instances:
		this.distinctToOffsetsPlasticMaterials.clear();
		this.distinctToOffsetsPlasticMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlasticMaterials, CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct PolkaDotMaterial instances:
		this.distinctToOffsetsPolkaDotMaterials.clear();
		this.distinctToOffsetsPolkaDotMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPolkaDotMaterials, CompiledMaterialCache.POLKA_DOT_MATERIAL_LENGTH));
		
//		Create offset mappings for all distinct SubstrateMaterial instances:
		this.distinctToOffsetsSubstrateMaterials.clear();
		this.distinctToOffsetsSubstrateMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSubstrateMaterials, CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Material;
	}
}