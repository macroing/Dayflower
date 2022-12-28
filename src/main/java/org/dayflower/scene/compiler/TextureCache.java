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
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.DotProductTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.PolkaDotTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;

final class TextureCache {
	private final List<BlendTexture> distinctBlendTextures;
	private final List<BullseyeTexture> distinctBullseyeTextures;
	private final List<CheckerboardTexture> distinctCheckerboardTextures;
	private final List<ConstantTexture> distinctConstantTextures;
	private final List<DotProductTexture> distinctDotProductTextures;
	private final List<LDRImageTexture> distinctLDRImageTextures;
	private final List<MarbleTexture> distinctMarbleTextures;
	private final List<PolkaDotTexture> distinctPolkaDotTextures;
	private final List<SimplexFractionalBrownianMotionTexture> distinctSimplexFractionalBrownianMotionTextures;
	private final List<SurfaceNormalTexture> distinctSurfaceNormalTextures;
	private final List<UVTexture> distinctUVTextures;
	private final Map<BlendTexture, Integer> distinctToOffsetsBlendTextures;
	private final Map<BullseyeTexture, Integer> distinctToOffsetsBullseyeTextures;
	private final Map<CheckerboardTexture, Integer> distinctToOffsetsCheckerboardTextures;
	private final Map<ConstantTexture, Integer> distinctToOffsetsConstantTextures;
	private final Map<LDRImageTexture, Integer> distinctToOffsetsLDRImageTextures;
	private final Map<MarbleTexture, Integer> distinctToOffsetsMarbleTextures;
	private final Map<PolkaDotTexture, Integer> distinctToOffsetsPolkaDotTextures;
	private final Map<SimplexFractionalBrownianMotionTexture, Integer> distinctToOffsetsSimplexFractionalBrownianMotionTextures;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TextureCache(final NodeCache nodeCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.distinctBlendTextures = new ArrayList<>();
		this.distinctBullseyeTextures = new ArrayList<>();
		this.distinctCheckerboardTextures = new ArrayList<>();
		this.distinctConstantTextures = new ArrayList<>();
		this.distinctDotProductTextures = new ArrayList<>();
		this.distinctLDRImageTextures = new ArrayList<>();
		this.distinctMarbleTextures = new ArrayList<>();
		this.distinctPolkaDotTextures = new ArrayList<>();
		this.distinctSimplexFractionalBrownianMotionTextures = new ArrayList<>();
		this.distinctSurfaceNormalTextures = new ArrayList<>();
		this.distinctUVTextures = new ArrayList<>();
		this.distinctToOffsetsBlendTextures = new LinkedHashMap<>();
		this.distinctToOffsetsBullseyeTextures = new LinkedHashMap<>();
		this.distinctToOffsetsCheckerboardTextures = new LinkedHashMap<>();
		this.distinctToOffsetsConstantTextures = new LinkedHashMap<>();
		this.distinctToOffsetsLDRImageTextures = new LinkedHashMap<>();
		this.distinctToOffsetsMarbleTextures = new LinkedHashMap<>();
		this.distinctToOffsetsPolkaDotTextures = new LinkedHashMap<>();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int findOffsetFor(final Texture texture) {
		Objects.requireNonNull(texture, "texture == null");
		
		if(texture instanceof BlendTexture) {
			return this.distinctToOffsetsBlendTextures.get(texture).intValue();
		} else if(texture instanceof BullseyeTexture) {
			return this.distinctToOffsetsBullseyeTextures.get(texture).intValue();
		} else if(texture instanceof CheckerboardTexture) {
			return this.distinctToOffsetsCheckerboardTextures.get(texture).intValue();
		} else if(texture instanceof ConstantTexture) {
			return this.distinctToOffsetsConstantTextures.get(texture).intValue();
		} else if(texture instanceof LDRImageTexture) {
			return this.distinctToOffsetsLDRImageTextures.get(texture).intValue();
		} else if(texture instanceof MarbleTexture) {
			return this.distinctToOffsetsMarbleTextures.get(texture).intValue();
		} else if(texture instanceof PolkaDotTexture) {
			return this.distinctToOffsetsPolkaDotTextures.get(texture).intValue();
		} else if(texture instanceof SimplexFractionalBrownianMotionTexture) {
			return this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.get(texture).intValue();
		} else if(texture instanceof SurfaceNormalTexture) {
			return 0;
		} else if(texture instanceof UVTexture) {
			return 0;
		} else {
			return 0;
		}
	}
	
	public float[] toBlendTextures() {
		return CompiledTextureCache.toBlendTextures(this.distinctBlendTextures, this::findOffsetFor);
	}
	
	public float[] toBullseyeTextures() {
		return CompiledTextureCache.toBullseyeTextures(this.distinctBullseyeTextures, this::findOffsetFor);
	}
	
	public float[] toCheckerboardTextures() {
		return CompiledTextureCache.toCheckerboardTextures(this.distinctCheckerboardTextures, this::findOffsetFor);
	}
	
	public float[] toConstantTextures() {
		return CompiledTextureCache.toConstantTextures(this.distinctConstantTextures);
	}
	
	public float[] toLDRImageTextures() {
		return CompiledTextureCache.toLDRImageTextures(this.distinctLDRImageTextures);
	}
	
	public float[] toMarbleTextures() {
		return CompiledTextureCache.toMarbleTextures(this.distinctMarbleTextures);
	}
	
	public float[] toPolkaDotTextures() {
		return CompiledTextureCache.toPolkaDotTextures(this.distinctPolkaDotTextures, this::findOffsetFor);
	}
	
	public float[] toSimplexFractionalBrownianMotionTextures() {
		return CompiledTextureCache.toSimplexFractionalBrownianMotionTextures(this.distinctSimplexFractionalBrownianMotionTextures);
	}
	
	public int[] toLDRImageTextureOffsets() {
		return CompiledTextureCache.toLDRImageTextureOffsets(this.distinctLDRImageTextures);
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledTextureCache());
	}
	
	public void build(final CompiledTextureCache compiledTextureCache) {
		compiledTextureCache.setBlendTextures(toBlendTextures());
		compiledTextureCache.setBullseyeTextures(toBullseyeTextures());
		compiledTextureCache.setCheckerboardTextures(toCheckerboardTextures());
		compiledTextureCache.setConstantTextures(toConstantTextures());
		compiledTextureCache.setLDRImageTextureOffsets(toLDRImageTextureOffsets());
		compiledTextureCache.setLDRImageTextures(toLDRImageTextures());
		compiledTextureCache.setMarbleTextures(toMarbleTextures());
		compiledTextureCache.setPolkaDotTextures(toPolkaDotTextures());
		compiledTextureCache.setSimplexFractionalBrownianMotionTextures(toSimplexFractionalBrownianMotionTextures());
	}
	
	public void clear() {
		this.distinctBlendTextures.clear();
		this.distinctBullseyeTextures.clear();
		this.distinctCheckerboardTextures.clear();
		this.distinctConstantTextures.clear();
		this.distinctDotProductTextures.clear();
		this.distinctLDRImageTextures.clear();
		this.distinctMarbleTextures.clear();
		this.distinctPolkaDotTextures.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSurfaceNormalTextures.clear();
		this.distinctUVTextures.clear();
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsLDRImageTextures.clear();
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsPolkaDotTextures.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
	}
	
	public void setup() {
//		Add all distinct BlendTexture instances:
		this.distinctBlendTextures.clear();
		this.distinctBlendTextures.addAll(this.nodeCache.getAllDistinct(BlendTexture.class));
		
//		Add all distinct BullseyeTexture instances:
		this.distinctBullseyeTextures.clear();
		this.distinctBullseyeTextures.addAll(this.nodeCache.getAllDistinct(BullseyeTexture.class));
		
//		Add all distinct CheckerboardTexture instances:
		this.distinctCheckerboardTextures.clear();
		this.distinctCheckerboardTextures.addAll(this.nodeCache.getAllDistinct(CheckerboardTexture.class));
		
//		Add all distinct ConstantTexture instances:
		this.distinctConstantTextures.clear();
		this.distinctConstantTextures.addAll(this.nodeCache.getAllDistinct(ConstantTexture.class));
		
//		Add all distinct DotProductTexture instances:
		this.distinctDotProductTextures.clear();
		this.distinctDotProductTextures.addAll(this.nodeCache.getAllDistinct(DotProductTexture.class));
		
//		Add all distinct LDRImageTexture instances:
		this.distinctLDRImageTextures.clear();
		this.distinctLDRImageTextures.addAll(this.nodeCache.getAllDistinct(LDRImageTexture.class));
		
//		Add all distinct MarbleTexture instances:
		this.distinctMarbleTextures.clear();
		this.distinctMarbleTextures.addAll(this.nodeCache.getAllDistinct(MarbleTexture.class));
		
//		Add all distinct PolkaDotTexture instances:
		this.distinctPolkaDotTextures.clear();
		this.distinctPolkaDotTextures.addAll(this.nodeCache.getAllDistinct(PolkaDotTexture.class));
		
//		Add all distinct SimplexFractionalBrownianMotionTexture instances:
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.addAll(this.nodeCache.getAllDistinct(SimplexFractionalBrownianMotionTexture.class));
		
//		Add all distinct SurfaceNormalTexture instances:
		this.distinctSurfaceNormalTextures.clear();
		this.distinctSurfaceNormalTextures.addAll(this.nodeCache.getAllDistinct(SurfaceNormalTexture.class));
		
//		Add all distinct UVTexture instances:
		this.distinctUVTextures.clear();
		this.distinctUVTextures.addAll(this.nodeCache.getAllDistinct(UVTexture.class));
		
//		Create offset mappings for all distinct BlendTexture instances:
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBlendTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBlendTextures));
		
//		Create offset mappings for all distinct BullseyeTexture instances:
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsBullseyeTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBullseyeTextures));
		
//		Create offset mappings for all distinct CheckerboardTexture instances:
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCheckerboardTextures));
		
//		Create offset mappings for all distinct ConstantTexture instances:
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsConstantTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctConstantTextures));
		
//		Create offset mappings for all distinct LDRImageTexture instances:
		this.distinctToOffsetsLDRImageTextures.clear();
		this.distinctToOffsetsLDRImageTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageTextures));
		
//		Create offset mappings for all distinct MarbleTexture instances:
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsMarbleTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMarbleTextures));
		
//		Create offset mappings for all distinct PolkaDotTexture instances:
		this.distinctToOffsetsPolkaDotTextures.clear();
		this.distinctToOffsetsPolkaDotTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPolkaDotTextures));
		
//		Create offset mappings for all distinct SimplexFractionalBrownianMotionTexture instances:
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexFractionalBrownianMotionTextures));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof Texture;
	}
}