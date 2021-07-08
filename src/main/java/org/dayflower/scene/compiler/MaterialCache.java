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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.material.ClearCoatMaterial;
import org.dayflower.scene.material.DisneyMaterial;
import org.dayflower.scene.material.GlassMaterial;
import org.dayflower.scene.material.GlossyMaterial;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.material.MetalMaterial;
import org.dayflower.scene.material.MirrorMaterial;
import org.dayflower.scene.material.PlasticMaterial;
import org.dayflower.scene.material.SubstrateMaterial;
import org.dayflower.scene.texture.Texture;
import org.dayflower.utility.Ints;

final class MaterialCache {
	private final List<ClearCoatMaterial> distinctClearCoatMaterials;
	private final List<DisneyMaterial> distinctDisneyMaterials;
	private final List<GlassMaterial> distinctGlassMaterials;
	private final List<GlossyMaterial> distinctGlossyMaterials;
	private final List<Material> distinctMaterials;
	private final List<MatteMaterial> distinctMatteMaterials;
	private final List<MetalMaterial> distinctMetalMaterials;
	private final List<MirrorMaterial> distinctMirrorMaterials;
	private final List<PlasticMaterial> distinctPlasticMaterials;
	private final List<SubstrateMaterial> distinctSubstrateMaterials;
	private final Map<ClearCoatMaterial, Integer> distinctToOffsetsClearCoatMaterials;
	private final Map<DisneyMaterial, Integer> distinctToOffsetsDisneyMaterials;
	private final Map<GlassMaterial, Integer> distinctToOffsetsGlassMaterials;
	private final Map<GlossyMaterial, Integer> distinctToOffsetsGlossyMaterials;
	private final Map<MatteMaterial, Integer> distinctToOffsetsMatteMaterials;
	private final Map<MetalMaterial, Integer> distinctToOffsetsMetalMaterials;
	private final Map<MirrorMaterial, Integer> distinctToOffsetsMirrorMaterials;
	private final Map<PlasticMaterial, Integer> distinctToOffsetsPlasticMaterials;
	private final Map<SubstrateMaterial, Integer> distinctToOffsetsSubstrateMaterials;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public MaterialCache(final NodeCache nodeCache) {
		this.nodeCache = nodeCache;
		this.distinctClearCoatMaterials = new ArrayList<>();
		this.distinctDisneyMaterials = new ArrayList<>();
		this.distinctGlassMaterials = new ArrayList<>();
		this.distinctGlossyMaterials = new ArrayList<>();
		this.distinctMaterials = new ArrayList<>();
		this.distinctMatteMaterials = new ArrayList<>();
		this.distinctMetalMaterials = new ArrayList<>();
		this.distinctMirrorMaterials = new ArrayList<>();
		this.distinctPlasticMaterials = new ArrayList<>();
		this.distinctSubstrateMaterials = new ArrayList<>();
		this.distinctToOffsetsClearCoatMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsDisneyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlassMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsGlossyMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMatteMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMetalMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsMirrorMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsPlasticMaterials = new LinkedHashMap<>();
		this.distinctToOffsetsSubstrateMaterials = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final Material material) {
		return this.distinctMaterials.contains(Objects.requireNonNull(material, "material == null"));
	}
	
	public int findOffsetFor(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		if(material instanceof ClearCoatMaterial) {
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
		} else if(material instanceof SubstrateMaterial) {
			return this.distinctToOffsetsSubstrateMaterials.get(material).intValue();
		} else {
			return 0;
		}
	}
	
	public int[] toMaterialClearCoatMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialClearCoatMaterialArray = Ints.toArray(this.distinctClearCoatMaterials, clearCoatMaterial -> doToArray(clearCoatMaterial), 1);
		
		for(int i = 0; i < this.distinctClearCoatMaterials.size(); i++) {
			final ClearCoatMaterial clearCoatMaterial = this.distinctClearCoatMaterials.get(i);
			
			final Texture textureEmission = clearCoatMaterial.getTextureEmission();
			final Texture textureKD = clearCoatMaterial.getTextureKD();
			final Texture textureKS = clearCoatMaterial.getTextureKS();
			
			final int materialClearCoatMaterialArrayTextureEmission = i * CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialClearCoatMaterialArrayTextureKDAndTextureKS = i * CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH + CompiledMaterialCache.CLEAR_COAT_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S;
			
			materialClearCoatMaterialArray[materialClearCoatMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialClearCoatMaterialArray[materialClearCoatMaterialArrayTextureKDAndTextureKS] = pack(textureKD.getID(), textureCache.findOffsetFor(textureKD), textureKS.getID(), textureCache.findOffsetFor(textureKS));
		}
		
		return materialClearCoatMaterialArray;
	}
	
	public int[] toMaterialDisneyMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialDisneyMaterialArray = Ints.toArray(this.distinctDisneyMaterials, disneyMaterial -> doToArray(disneyMaterial), 1);
		
		for(int i = 0; i < this.distinctDisneyMaterials.size(); i++) {
			final DisneyMaterial disneyMaterial = this.distinctDisneyMaterials.get(i);
			
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
			
			final int materialDisneyMaterialArrayTextureEmissionAndTextureAnisotropic = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ANISOTROPIC;
			final int materialDisneyMaterialArrayTextureClearCoatAndTextureClearCoatGloss = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS;
			final int materialDisneyMaterialArrayTextureColorAndTextureDiffuseTransmission = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION;
			final int materialDisneyMaterialArrayTextureEtaAndTextureFlatness = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS;
			final int materialDisneyMaterialArrayTextureMetallicAndTextureRoughness = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS;
			final int materialDisneyMaterialArrayTextureScatterDistanceAndTextureSheen = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN;
			final int materialDisneyMaterialArrayTextureSheenTintAndTextureSpecularTint = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT;
			final int materialDisneyMaterialArrayTextureSpecularTransmissionAndIsThin = i * CompiledMaterialCache.DISNEY_MATERIAL_LENGTH + CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN;
			
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureEmissionAndTextureAnisotropic] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), textureAnisotropic.getID(), textureCache.findOffsetFor(textureAnisotropic));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureClearCoatAndTextureClearCoatGloss] = pack(textureClearCoat.getID(), textureCache.findOffsetFor(textureClearCoat), textureClearCoatGloss.getID(), textureCache.findOffsetFor(textureClearCoatGloss));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureColorAndTextureDiffuseTransmission] = pack(textureColor.getID(), textureCache.findOffsetFor(textureColor), textureDiffuseTransmission.getID(), textureCache.findOffsetFor(textureDiffuseTransmission));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureEtaAndTextureFlatness] = pack(textureEta.getID(), textureCache.findOffsetFor(textureEta), textureFlatness.getID(), textureCache.findOffsetFor(textureFlatness));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureMetallicAndTextureRoughness] = pack(textureMetallic.getID(), textureCache.findOffsetFor(textureMetallic), textureRoughness.getID(), textureCache.findOffsetFor(textureRoughness));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureScatterDistanceAndTextureSheen] = pack(textureScatterDistance.getID(), textureCache.findOffsetFor(textureScatterDistance), textureSheen.getID(), textureCache.findOffsetFor(textureSheen));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSheenTintAndTextureSpecularTint] = pack(textureSheenTint.getID(), textureCache.findOffsetFor(textureSheenTint), textureSpecularTint.getID(), textureCache.findOffsetFor(textureSpecularTint));
			materialDisneyMaterialArray[materialDisneyMaterialArrayTextureSpecularTransmissionAndIsThin] = pack(textureSpecularTransmission.getID(), textureCache.findOffsetFor(textureSpecularTransmission), isThin ? 1 : 0, 0);
		}
		
		return materialDisneyMaterialArray;
	}
	
	public int[] toMaterialGlassMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialGlassMaterialArray = Ints.toArray(this.distinctGlassMaterials, glassMaterial -> doToArray(glassMaterial), 1);
		
		for(int i = 0; i < this.distinctGlassMaterials.size(); i++) {
			final GlassMaterial glassMaterial = this.distinctGlassMaterials.get(i);
			
			final Texture textureEmission = glassMaterial.getTextureEmission();
			final Texture textureEta = glassMaterial.getTextureEta();
			final Texture textureKR = glassMaterial.getTextureKR();
			final Texture textureKT = glassMaterial.getTextureKT();
			final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
			
			final int materialGlassMaterialArrayTextureEmissionAndTextureEta = i * CompiledMaterialCache.GLASS_MATERIAL_LENGTH + CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ETA;
			final int materialGlassMaterialArrayTextureKRAndTextureKT = i * CompiledMaterialCache.GLASS_MATERIAL_LENGTH + CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T;
			final int materialGlassMaterialArrayTextureRoughnessUAndTextureRoughnessV = i * CompiledMaterialCache.GLASS_MATERIAL_LENGTH + CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V;
			
			materialGlassMaterialArray[materialGlassMaterialArrayTextureEmissionAndTextureEta] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), textureEta.getID(), textureCache.findOffsetFor(textureEta));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureKRAndTextureKT] = pack(textureKR.getID(), textureCache.findOffsetFor(textureKR), textureKT.getID(), textureCache.findOffsetFor(textureKT));
			materialGlassMaterialArray[materialGlassMaterialArrayTextureRoughnessUAndTextureRoughnessV] = pack(textureRoughnessU.getID(), textureCache.findOffsetFor(textureRoughnessU), textureRoughnessV.getID(), textureCache.findOffsetFor(textureRoughnessV));
		}
		
		return materialGlassMaterialArray;
	}
	
	public int[] toMaterialGlossyMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialGlossyMaterialArray = Ints.toArray(this.distinctGlossyMaterials, glossyMaterial -> doToArray(glossyMaterial), 1);
		
		for(int i = 0; i < this.distinctGlossyMaterials.size(); i++) {
			final GlossyMaterial glossyMaterial = this.distinctGlossyMaterials.get(i);
			
			final Texture textureEmission = glossyMaterial.getTextureEmission();
			final Texture textureKR = glossyMaterial.getTextureKR();
			final Texture textureRoughness = glossyMaterial.getTextureRoughness();
			
			final int materialGlossyMaterialArrayTextureEmission = i * CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialGlossyMaterialArrayTextureKRAndTextureRoughness = i * CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH + CompiledMaterialCache.GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS;
			
			materialGlossyMaterialArray[materialGlossyMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialGlossyMaterialArray[materialGlossyMaterialArrayTextureKRAndTextureRoughness] = pack(textureKR.getID(), textureCache.findOffsetFor(textureKR), textureRoughness.getID(), textureCache.findOffsetFor(textureRoughness));
		}
		
		return materialGlossyMaterialArray;
	}
	
	public int[] toMaterialMatteMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialMatteMaterialArray = Ints.toArray(this.distinctMatteMaterials, matteMaterial -> doToArray(matteMaterial), 1);
		
		for(int i = 0; i < this.distinctMatteMaterials.size(); i++) {
			final MatteMaterial matteMaterial = this.distinctMatteMaterials.get(i);
			
			final Texture textureEmission = matteMaterial.getTextureEmission();
			final Texture textureAngle = matteMaterial.getTextureAngle();
			final Texture textureKD = matteMaterial.getTextureKD();
			
			final int materialMatteMaterialArrayTextureEmission = i * CompiledMaterialCache.MATTE_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialMatteMaterialArrayTextureAngleAndTextureKD = i * CompiledMaterialCache.MATTE_MATERIAL_LENGTH + CompiledMaterialCache.MATTE_MATERIAL_OFFSET_TEXTURE_ANGLE_AND_TEXTURE_K_D;
			
			materialMatteMaterialArray[materialMatteMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialMatteMaterialArray[materialMatteMaterialArrayTextureAngleAndTextureKD] = pack(textureAngle.getID(), textureCache.findOffsetFor(textureAngle), textureKD.getID(), textureCache.findOffsetFor(textureKD));
		}
		
		return materialMatteMaterialArray;
	}
	
	public int[] toMaterialMetalMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialMetalMaterialArray = Ints.toArray(this.distinctMetalMaterials, metalMaterial -> doToArray(metalMaterial), 1);
		
		for(int i = 0; i < this.distinctMetalMaterials.size(); i++) {
			final MetalMaterial metalMaterial = this.distinctMetalMaterials.get(i);
			
			final Texture textureEmission = metalMaterial.getTextureEmission();
			final Texture textureEta = metalMaterial.getTextureEta();
			final Texture textureK = metalMaterial.getTextureK();
			final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
			
			final int materialMetalMaterialArrayTextureEmission = i * CompiledMaterialCache.METAL_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialMetalMaterialArrayTextureEtaAndTextureK = i * CompiledMaterialCache.METAL_MATERIAL_LENGTH + CompiledMaterialCache.METAL_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_K;
			final int materialMetalMaterialArrayTextureRoughnessUAndTextureRoughnessV = i * CompiledMaterialCache.METAL_MATERIAL_LENGTH + CompiledMaterialCache.METAL_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V;
			
			materialMetalMaterialArray[materialMetalMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialMetalMaterialArray[materialMetalMaterialArrayTextureEtaAndTextureK] = pack(textureEta.getID(), textureCache.findOffsetFor(textureEta), textureK.getID(), textureCache.findOffsetFor(textureK));
			materialMetalMaterialArray[materialMetalMaterialArrayTextureRoughnessUAndTextureRoughnessV] = pack(textureRoughnessU.getID(), textureCache.findOffsetFor(textureRoughnessU), textureRoughnessV.getID(), textureCache.findOffsetFor(textureRoughnessV));
		}
		
		return materialMetalMaterialArray;
	}
	
	public int[] toMaterialMirrorMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialMirrorMaterialArray = Ints.toArray(this.distinctMirrorMaterials, mirrorMaterial -> doToArray(mirrorMaterial), 1);
		
		for(int i = 0; i < this.distinctMirrorMaterials.size(); i++) {
			final MirrorMaterial mirrorMaterial = this.distinctMirrorMaterials.get(i);
			
			final Texture textureEmission = mirrorMaterial.getTextureEmission();
			final Texture textureKR = mirrorMaterial.getTextureKR();
			
			final int materialMirrorMaterialArrayTextureEmissionAndTextureKR = i * CompiledMaterialCache.MIRROR_MATERIAL_LENGTH + CompiledMaterialCache.MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R;
			
			materialMirrorMaterialArray[materialMirrorMaterialArrayTextureEmissionAndTextureKR] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), textureKR.getID(), textureCache.findOffsetFor(textureKR));
		}
		
		return materialMirrorMaterialArray;
	}
	
	public int[] toMaterialPlasticMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialPlasticMaterialArray = Ints.toArray(this.distinctPlasticMaterials, plasticMaterial -> doToArray(plasticMaterial), 1);
		
		for(int i = 0; i < this.distinctPlasticMaterials.size(); i++) {
			final PlasticMaterial plasticMaterial = this.distinctPlasticMaterials.get(i);
			
			final Texture textureEmission = plasticMaterial.getTextureEmission();
			final Texture textureKD = plasticMaterial.getTextureKD();
			final Texture textureKS = plasticMaterial.getTextureKS();
			final Texture textureRoughness = plasticMaterial.getTextureRoughness();
			
			final int materialPlasticMaterialArrayTextureEmission = i * CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialPlasticMaterialArrayTextureKDAndTextureKS = i * CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH + CompiledMaterialCache.PLASTIC_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S;
			final int materialPlasticMaterialArrayTextureRoughness = i * CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH + CompiledMaterialCache.PLASTIC_MATERIAL_OFFSET_TEXTURE_ROUGHNESS;
			
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureKDAndTextureKS] = pack(textureKD.getID(), textureCache.findOffsetFor(textureKD), textureKS.getID(), textureCache.findOffsetFor(textureKS));
			materialPlasticMaterialArray[materialPlasticMaterialArrayTextureRoughness] = pack(textureRoughness.getID(), textureCache.findOffsetFor(textureRoughness), 0, 0);
		}
		
		return materialPlasticMaterialArray;
	}
	
	public int[] toMaterialSubstrateMaterialArray(final TextureCache textureCache) {
		Objects.requireNonNull(textureCache, "textureCache == null");
		
		final int[] materialSubstrateMaterialArray = Ints.toArray(this.distinctSubstrateMaterials, substrateMaterial -> doToArray(substrateMaterial), 1);
		
		for(int i = 0; i < this.distinctSubstrateMaterials.size(); i++) {
			final SubstrateMaterial substrateMaterial = this.distinctSubstrateMaterials.get(i);
			
			final Texture textureEmission = substrateMaterial.getTextureEmission();
			final Texture textureKD = substrateMaterial.getTextureKD();
			final Texture textureKS = substrateMaterial.getTextureKS();
			final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
			final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
			
			final int materialSubstrateMaterialArrayTextureEmission = i * CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH + CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION;
			final int materialSubstrateMaterialArrayTextureKDAndTextureKS = i * CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH + CompiledMaterialCache.SUBSTRATE_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S;
			final int materialSubstrateMaterialArrayTextureRoughnessUAndTextureRoughnessV = i * CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH + CompiledMaterialCache.SUBSTRATE_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V;
			
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureEmission] = pack(textureEmission.getID(), textureCache.findOffsetFor(textureEmission), 0, 0);
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureKDAndTextureKS] = pack(textureKD.getID(), textureCache.findOffsetFor(textureKD), textureKS.getID(), textureCache.findOffsetFor(textureKS));
			materialSubstrateMaterialArray[materialSubstrateMaterialArrayTextureRoughnessUAndTextureRoughnessV] = pack(textureRoughnessU.getID(), textureCache.findOffsetFor(textureRoughnessU), textureRoughnessV.getID(), textureCache.findOffsetFor(textureRoughnessV));
		}
		
		return materialSubstrateMaterialArray;
	}
	
	public void clear() {
		this.distinctClearCoatMaterials.clear();
		this.distinctDisneyMaterials.clear();
		this.distinctGlassMaterials.clear();
		this.distinctGlossyMaterials.clear();
		this.distinctMaterials.clear();
		this.distinctMatteMaterials.clear();
		this.distinctMetalMaterials.clear();
		this.distinctMirrorMaterials.clear();
		this.distinctPlasticMaterials.clear();
		this.distinctSubstrateMaterials.clear();
		this.distinctToOffsetsClearCoatMaterials.clear();
		this.distinctToOffsetsDisneyMaterials.clear();
		this.distinctToOffsetsGlassMaterials.clear();
		this.distinctToOffsetsGlossyMaterials.clear();
		this.distinctToOffsetsMatteMaterials.clear();
		this.distinctToOffsetsMetalMaterials.clear();
		this.distinctToOffsetsMirrorMaterials.clear();
		this.distinctToOffsetsPlasticMaterials.clear();
		this.distinctToOffsetsSubstrateMaterials.clear();
	}
	
	public void setup(final Scene scene) {
		if(this.nodeCache != null) {
			doSetupNew(scene);
		} else {
			doSetupOld(scene);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Material;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doSetupNew(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
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
		
//		Add all distinct SubstrateMaterial instances:
		this.distinctSubstrateMaterials.clear();
		this.distinctSubstrateMaterials.addAll(this.nodeCache.getAllDistinct(SubstrateMaterial.class));
		
//		Add all distinct Material instances:
		this.distinctMaterials.clear();
		this.distinctMaterials.addAll(this.distinctClearCoatMaterials);
		this.distinctMaterials.addAll(this.distinctDisneyMaterials);
		this.distinctMaterials.addAll(this.distinctGlassMaterials);
		this.distinctMaterials.addAll(this.distinctGlossyMaterials);
		this.distinctMaterials.addAll(this.distinctMatteMaterials);
		this.distinctMaterials.addAll(this.distinctMetalMaterials);
		this.distinctMaterials.addAll(this.distinctMirrorMaterials);
		this.distinctMaterials.addAll(this.distinctPlasticMaterials);
		this.distinctMaterials.addAll(this.distinctSubstrateMaterials);
		
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
		
//		Create offset mappings for all distinct SubstrateMaterial instances:
		this.distinctToOffsetsSubstrateMaterials.clear();
		this.distinctToOffsetsSubstrateMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSubstrateMaterials, CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH));
	}
	
	private void doSetupOld(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct Material instances:
		this.distinctMaterials.clear();
		this.distinctMaterials.addAll(NodeFilter.filterAllDistinct(scene, Material.class).stream().filter(MaterialCache::doFilterMaterial).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct ClearCoatMaterial instances:
		this.distinctClearCoatMaterials.clear();
		this.distinctClearCoatMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof ClearCoatMaterial).map(material -> ClearCoatMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct DisneyMaterial instances:
		this.distinctDisneyMaterials.clear();
		this.distinctDisneyMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof DisneyMaterial).map(material -> DisneyMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct GlassMaterial instances:
		this.distinctGlassMaterials.clear();
		this.distinctGlassMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof GlassMaterial).map(material -> GlassMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct GlossyMaterial instances:
		this.distinctGlossyMaterials.clear();
		this.distinctGlossyMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof GlossyMaterial).map(material -> GlossyMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct MatteMaterial instances:
		this.distinctMatteMaterials.clear();
		this.distinctMatteMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof MatteMaterial).map(material -> MatteMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct MetalMaterial instances:
		this.distinctMetalMaterials.clear();
		this.distinctMetalMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof MetalMaterial).map(material -> MetalMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct MirrorMaterial instances:
		this.distinctMirrorMaterials.clear();
		this.distinctMirrorMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof MirrorMaterial).map(material -> MirrorMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct PlasticMaterial instances:
		this.distinctPlasticMaterials.clear();
		this.distinctPlasticMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof PlasticMaterial).map(material -> PlasticMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct SubstrateMaterial instances:
		this.distinctSubstrateMaterials.clear();
		this.distinctSubstrateMaterials.addAll(this.distinctMaterials.stream().filter(material -> material instanceof SubstrateMaterial).map(material -> SubstrateMaterial.class.cast(material)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
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
		
//		Create offset mappings for all distinct SubstrateMaterial instances:
		this.distinctToOffsetsSubstrateMaterials.clear();
		this.distinctToOffsetsSubstrateMaterials.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSubstrateMaterials, CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilterMaterial(final Material material) {
		if(material instanceof ClearCoatMaterial) {
			return true;
		} else if(material instanceof DisneyMaterial) {
			return true;
		} else if(material instanceof GlassMaterial) {
			return true;
		} else if(material instanceof GlossyMaterial) {
			return true;
		} else if(material instanceof MatteMaterial) {
			return true;
		} else if(material instanceof MetalMaterial) {
			return true;
		} else if(material instanceof MirrorMaterial) {
			return true;
		} else if(material instanceof PlasticMaterial) {
			return true;
		} else if(material instanceof SubstrateMaterial) {
			return true;
		} else {
			return false;
		}
	}
	
	private static int[] doToArray(final ClearCoatMaterial clearCoatMaterial) {
		final Texture textureEmission = clearCoatMaterial.getTextureEmission();
		final Texture textureKD = clearCoatMaterial.getTextureKD();
		final Texture textureKS = clearCoatMaterial.getTextureKS();
		
		final int[] array = new int[CompiledMaterialCache.CLEAR_COAT_MATERIAL_LENGTH];
		
//		Because the ClearCoatMaterial occupy 2/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);									//Block #1
		array[CompiledMaterialCache.CLEAR_COAT_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);	//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final DisneyMaterial disneyMaterial) {
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
		
		final int[] array = new int[CompiledMaterialCache.DISNEY_MATERIAL_LENGTH];
		
//		Because the DisneyMaterial occupy 8/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ANISOTROPIC] = pack(textureEmission.getID(), 0, textureAnisotropic.getID(), 0);				//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_CLEAR_COAT_AND_TEXTURE_CLEAR_COAT_GLOSS] = pack(textureClearCoat.getID(), 0, textureClearCoatGloss.getID(), 0);	//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_COLOR_AND_TEXTURE_DIFFUSE_TRANSMISSION] = pack(textureColor.getID(), 0, textureDiffuseTransmission.getID(), 0);	//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_FLATNESS] = pack(textureEta.getID(), 0, textureFlatness.getID(), 0);								//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_METALLIC_AND_TEXTURE_ROUGHNESS] = pack(textureMetallic.getID(), 0, textureRoughness.getID(), 0);					//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SCATTER_DISTANCE_AND_TEXTURE_SHEEN] = pack(textureScatterDistance.getID(), 0, textureSheen.getID(), 0);			//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SHEEN_TINT_AND_TEXTURE_SPECULAR_TINT] = pack(textureSheenTint.getID(), 0, textureSpecularTint.getID(), 0);		//Block #1.
		array[CompiledMaterialCache.DISNEY_MATERIAL_OFFSET_TEXTURE_SPECULAR_TRANSMISSION_AND_IS_THIN] = pack(textureSpecularTransmission.getID(), 0, isThin ? 1 : 0, 0);			//Block #1.
		
		return array;
	}
	
	private static int[] doToArray(final GlassMaterial glassMaterial) {
		final Texture textureEmission = glassMaterial.getTextureEmission();
		final Texture textureEta = glassMaterial.getTextureEta();
		final Texture textureKR = glassMaterial.getTextureKR();
		final Texture textureKT = glassMaterial.getTextureKT();
		final Texture textureRoughnessU = glassMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = glassMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = glassMaterial.isRemappingRoughness();
		
		final int[] array = new int[CompiledMaterialCache.GLASS_MATERIAL_LENGTH];
		
//		Because the GlassMaterial occupy 4/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_ETA] = pack(textureEmission.getID(), 0, textureEta.getID(), 0);						//Block #1
		array[CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_K_T] = pack(textureKR.getID(), 0, textureKT.getID(), 0);									//Block #1
		array[CompiledMaterialCache.GLASS_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);	//Block #1
		array[CompiledMaterialCache.GLASS_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final GlossyMaterial glossyMaterial) {
		final Texture textureEmission = glossyMaterial.getTextureEmission();
		final Texture textureKR = glossyMaterial.getTextureKR();
		final Texture textureRoughness = glossyMaterial.getTextureRoughness();
		
		final int[] array = new int[CompiledMaterialCache.GLOSSY_MATERIAL_LENGTH];
		
//		Because the GlossyMaterial occupy 2/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);											//Block #1
		array[CompiledMaterialCache.GLOSSY_MATERIAL_OFFSET_TEXTURE_K_R_AND_TEXTURE_ROUGHNESS] = pack(textureKR.getID(), 0, textureRoughness.getID(), 0);//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final MatteMaterial matteMaterial) {
		final Texture textureEmission = matteMaterial.getTextureEmission();
		final Texture textureAngle = matteMaterial.getTextureAngle();
		final Texture textureKD = matteMaterial.getTextureKD();
		
		final int[] array = new int[CompiledMaterialCache.MATTE_MATERIAL_LENGTH];
		
//		Because the MatteMaterial occupy 2/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);									//Block #1
		array[CompiledMaterialCache.MATTE_MATERIAL_OFFSET_TEXTURE_ANGLE_AND_TEXTURE_K_D] = pack(textureAngle.getID(), 0, textureKD.getID(), 0);	//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final MetalMaterial metalMaterial) {
		final Texture textureEmission = metalMaterial.getTextureEmission();
		final Texture textureEta = metalMaterial.getTextureEta();
		final Texture textureK = metalMaterial.getTextureK();
		final Texture textureRoughnessU = metalMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = metalMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = metalMaterial.isRemappingRoughness();
		
		final int[] array = new int[CompiledMaterialCache.METAL_MATERIAL_LENGTH];
		
//		Because the MetalMaterial occupy 4/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);																//Block #1
		array[CompiledMaterialCache.METAL_MATERIAL_OFFSET_TEXTURE_ETA_AND_TEXTURE_K] = pack(textureEta.getID(), 0, textureK.getID(), 0);									//Block #1
		array[CompiledMaterialCache.METAL_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);	//Block #1
		array[CompiledMaterialCache.METAL_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final MirrorMaterial mirrorMaterial) {
		final Texture textureEmission = mirrorMaterial.getTextureEmission();
		final Texture textureKR = mirrorMaterial.getTextureKR();
		
		final int[] array = new int[CompiledMaterialCache.MIRROR_MATERIAL_LENGTH];
		
//		Because the MirrorMaterial occupy 1/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MIRROR_MATERIAL_OFFSET_TEXTURE_EMISSION_AND_TEXTURE_K_R] = pack(textureEmission.getID(), 0, textureKR.getID(), 0);//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final PlasticMaterial plasticMaterial) {
		final Texture textureEmission = plasticMaterial.getTextureEmission();
		final Texture textureKD = plasticMaterial.getTextureKD();
		final Texture textureKS = plasticMaterial.getTextureKS();
		final Texture textureRoughness = plasticMaterial.getTextureRoughness();
		
		final boolean isRemappingRoughness = plasticMaterial.isRemappingRoughness();
		
		final int[] array = new int[CompiledMaterialCache.PLASTIC_MATERIAL_LENGTH];
		
//		Because the PlasticMaterial occupy 4/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);								//Block #1
		array[CompiledMaterialCache.PLASTIC_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);//Block #1
		array[CompiledMaterialCache.PLASTIC_MATERIAL_OFFSET_TEXTURE_ROUGHNESS] = pack(textureRoughness.getID(), 0, 0, 0);					//Block #1
		array[CompiledMaterialCache.PLASTIC_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;							//Block #1
		
		return array;
	}
	
	private static int[] doToArray(final SubstrateMaterial substrateMaterial) {
		final Texture textureEmission = substrateMaterial.getTextureEmission();
		final Texture textureKD = substrateMaterial.getTextureKD();
		final Texture textureKS = substrateMaterial.getTextureKS();
		final Texture textureRoughnessU = substrateMaterial.getTextureRoughnessU();
		final Texture textureRoughnessV = substrateMaterial.getTextureRoughnessV();
		
		final boolean isRemappingRoughness = substrateMaterial.isRemappingRoughness();
		
		final int[] array = new int[CompiledMaterialCache.SUBSTRATE_MATERIAL_LENGTH];
		
//		Because the SubstrateMaterial occupy 4/8 positions in a block, it should be aligned.
		array[CompiledMaterialCache.MATERIAL_OFFSET_TEXTURE_EMISSION] = pack(textureEmission.getID(), 0, 0, 0);																	//Block #1
		array[CompiledMaterialCache.SUBSTRATE_MATERIAL_OFFSET_TEXTURE_K_D_AND_TEXTURE_K_S] = pack(textureKD.getID(), 0, textureKS.getID(), 0);									//Block #1
		array[CompiledMaterialCache.SUBSTRATE_MATERIAL_OFFSET_TEXTURE_ROUGHNESS_U_AND_TEXTURE_ROUGHNESS_V] = pack(textureRoughnessU.getID(), 0, textureRoughnessV.getID(), 0);	//Block #1
		array[CompiledMaterialCache.SUBSTRATE_MATERIAL_OFFSET_IS_REMAPPING_ROUGHNESS] = isRemappingRoughness ? 1 : 0;															//Block #1
		
		return array;
	}
}