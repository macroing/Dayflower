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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.NormalMapLDRImageModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;

final class ModifierCache {
	private final List<NoOpModifier> distinctNoOpModifiers;
	private final List<NormalMapLDRImageModifier> distinctNormalMapLDRImageModifiers;
	private final List<SimplexNoiseNormalMapModifier> distinctSimplexNoiseNormalMapModifiers;
	private final Map<NormalMapLDRImageModifier, Integer> distinctToOffsetsNormalMapLDRImageModifiers;
	private final Map<SimplexNoiseNormalMapModifier, Integer> distinctToOffsetsSimplexNoiseNormalMapModifiers;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ModifierCache(final NodeCache nodeCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.distinctNoOpModifiers = new ArrayList<>();
		this.distinctNormalMapLDRImageModifiers = new ArrayList<>();
		this.distinctSimplexNoiseNormalMapModifiers = new ArrayList<>();
		this.distinctToOffsetsNormalMapLDRImageModifiers = new LinkedHashMap<>();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int findOffsetFor(final Modifier modifier) {
		Objects.requireNonNull(modifier, "modifier == null");
		
		if(modifier instanceof NormalMapLDRImageModifier) {
			return this.distinctToOffsetsNormalMapLDRImageModifiers.get(modifier).intValue();
		} else if(modifier instanceof NoOpModifier) {
			return 0;
		} else if(modifier instanceof SimplexNoiseNormalMapModifier) {
			return this.distinctToOffsetsSimplexNoiseNormalMapModifiers.get(modifier).intValue();
		} else {
			return 0;
		}
	}
	
	public float[] toNormalMapLDRImageModifiers() {
		return CompiledModifierCache.toNormalMapLDRImageModifiers(this.distinctNormalMapLDRImageModifiers);
	}
	
	public float[] toSimplexNoiseNormalMapModifiers() {
		return CompiledModifierCache.toSimplexNoiseNormalMapModifiers(this.distinctSimplexNoiseNormalMapModifiers);
	}
	
	public int[] toNormalMapLDRImageModifierOffsets() {
		return CompiledModifierCache.toNormalMapLDRImageModifierOffsets(this.distinctNormalMapLDRImageModifiers);
	}
	
	public void build(final CompiledModifierCache compiledModifierCache) {
		compiledModifierCache.setNormalMapLDRImageModifierOffsets(toNormalMapLDRImageModifierOffsets());
		compiledModifierCache.setNormalMapLDRImageModifiers(toNormalMapLDRImageModifiers());
		compiledModifierCache.setSimplexNoiseNormalMapModifiers(toSimplexNoiseNormalMapModifiers());
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledModifierCache());
	}
	
	public void clear() {
		this.distinctNoOpModifiers.clear();
		this.distinctNormalMapLDRImageModifiers.clear();
		this.distinctSimplexNoiseNormalMapModifiers.clear();
		this.distinctToOffsetsNormalMapLDRImageModifiers.clear();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.clear();
	}
	
	public void setup() {
//		Add all distinct NoOpModifier instances:
		this.distinctNoOpModifiers.clear();
		this.distinctNoOpModifiers.addAll(this.nodeCache.getAllDistinct(NoOpModifier.class));
		
//		Add all distinct NormalMapLDRImageModifier instances:
		this.distinctNormalMapLDRImageModifiers.clear();
		this.distinctNormalMapLDRImageModifiers.addAll(this.nodeCache.getAllDistinct(NormalMapLDRImageModifier.class));
		
//		Add all distinct SimplexNoiseNormalMapModifier instances:
		this.distinctSimplexNoiseNormalMapModifiers.clear();
		this.distinctSimplexNoiseNormalMapModifiers.addAll(this.nodeCache.getAllDistinct(SimplexNoiseNormalMapModifier.class));
		
//		Create offset mappings for all distinct NormalMapLDRImageModifier instances:
		this.distinctToOffsetsNormalMapLDRImageModifiers.clear();
		this.distinctToOffsetsNormalMapLDRImageModifiers.putAll(NodeFilter.mapDistinctToOffsets(this.distinctNormalMapLDRImageModifiers));
		
//		Create offset mappings for all distinct SimplexNoiseNormalMapModifier instances:
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.clear();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexNoiseNormalMapModifiers));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Modifier;
	}
}