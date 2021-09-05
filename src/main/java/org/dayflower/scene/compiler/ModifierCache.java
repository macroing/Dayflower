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
import org.dayflower.scene.modifier.LDRImageNormalMapModifier;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;

final class ModifierCache {
	private final List<LDRImageNormalMapModifier> distinctLDRImageNormalMapModifiers;
	private final List<NoOpModifier> distinctNoOpModifiers;
	private final List<SimplexNoiseNormalMapModifier> distinctSimplexNoiseNormalMapModifiers;
	private final Map<LDRImageNormalMapModifier, Integer> distinctToOffsetsLDRImageNormalMapModifiers;
	private final Map<SimplexNoiseNormalMapModifier, Integer> distinctToOffsetsSimplexNoiseNormalMapModifiers;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public ModifierCache(final NodeCache nodeCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.distinctLDRImageNormalMapModifiers = new ArrayList<>();
		this.distinctNoOpModifiers = new ArrayList<>();
		this.distinctSimplexNoiseNormalMapModifiers = new ArrayList<>();
		this.distinctToOffsetsLDRImageNormalMapModifiers = new LinkedHashMap<>();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int findOffsetFor(final Modifier modifier) {
		Objects.requireNonNull(modifier, "modifier == null");
		
		if(modifier instanceof LDRImageNormalMapModifier) {
			return this.distinctToOffsetsLDRImageNormalMapModifiers.get(modifier).intValue();
		} else if(modifier instanceof NoOpModifier) {
			return 0;
		} else if(modifier instanceof SimplexNoiseNormalMapModifier) {
			return this.distinctToOffsetsSimplexNoiseNormalMapModifiers.get(modifier).intValue();
		} else {
			return 0;
		}
	}
	
	public float[] toLDRImageNormalMapModifiers() {
		return CompiledModifierCache.toLDRImageNormalMapModifiers(this.distinctLDRImageNormalMapModifiers);
	}
	
	public float[] toSimplexNoiseNormalMapModifiers() {
		return CompiledModifierCache.toSimplexNoiseNormalMapModifiers(this.distinctSimplexNoiseNormalMapModifiers);
	}
	
	public int[] toLDRImageNormalMapModifierOffsets() {
		return CompiledModifierCache.toLDRImageNormalMapModifierOffsets(this.distinctLDRImageNormalMapModifiers);
	}
	
	public void build(final CompiledModifierCache compiledModifierCache) {
		compiledModifierCache.setLDRImageNormalMapModifierOffsets(toLDRImageNormalMapModifierOffsets());
		compiledModifierCache.setLDRImageNormalMapModifiers(toLDRImageNormalMapModifiers());
		compiledModifierCache.setSimplexNoiseNormalMapModifiers(toSimplexNoiseNormalMapModifiers());
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledModifierCache());
	}
	
	public void clear() {
		this.distinctLDRImageNormalMapModifiers.clear();
		this.distinctNoOpModifiers.clear();
		this.distinctSimplexNoiseNormalMapModifiers.clear();
		this.distinctToOffsetsLDRImageNormalMapModifiers.clear();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.clear();
	}
	
	public void setup() {
//		Add all distinct LDRImageNormalMapModifier instances:
		this.distinctLDRImageNormalMapModifiers.clear();
		this.distinctLDRImageNormalMapModifiers.addAll(this.nodeCache.getAllDistinct(LDRImageNormalMapModifier.class));
		
//		Add all distinct NoOpModifier instances:
		this.distinctNoOpModifiers.clear();
		this.distinctNoOpModifiers.addAll(this.nodeCache.getAllDistinct(NoOpModifier.class));
		
//		Add all distinct SimplexNoiseNormalMapModifier instances:
		this.distinctSimplexNoiseNormalMapModifiers.clear();
		this.distinctSimplexNoiseNormalMapModifiers.addAll(this.nodeCache.getAllDistinct(SimplexNoiseNormalMapModifier.class));
		
//		Create offset mappings for all distinct LDRImageNormalMapModifier instances:
		this.distinctToOffsetsLDRImageNormalMapModifiers.clear();
		this.distinctToOffsetsLDRImageNormalMapModifiers.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageNormalMapModifiers));
		
//		Create offset mappings for all distinct SimplexNoiseNormalMapModifier instances:
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.clear();
		this.distinctToOffsetsSimplexNoiseNormalMapModifiers.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexNoiseNormalMapModifiers));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Modifier;
	}
}