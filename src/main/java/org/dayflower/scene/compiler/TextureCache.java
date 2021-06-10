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

import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.pack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Scene;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.utility.Floats;

final class TextureCache {
	private final List<BlendTexture> distinctBlendTextures;
	private final List<BullseyeTexture> distinctBullseyeTextures;
	private final List<CheckerboardTexture> distinctCheckerboardTextures;
	private final List<ConstantTexture> distinctConstantTextures;
	private final List<LDRImageTexture> distinctLDRImageTextures;
	private final List<MarbleTexture> distinctMarbleTextures;
	private final List<SimplexFractionalBrownianMotionTexture> distinctSimplexFractionalBrownianMotionTextures;
	private final List<SurfaceNormalTexture> distinctSurfaceNormalTextures;
	private final List<UVTexture> distinctUVTextures;
	private final Map<BlendTexture, Integer> distinctToOffsetsBlendTextures;
	private final Map<BullseyeTexture, Integer> distinctToOffsetsBullseyeTextures;
	private final Map<CheckerboardTexture, Integer> distinctToOffsetsCheckerboardTextures;
	private final Map<ConstantTexture, Integer> distinctToOffsetsConstantTextures;
	private final Map<LDRImageTexture, Integer> distinctToOffsetsLDRImageTextures;
	private final Map<MarbleTexture, Integer> distinctToOffsetsMarbleTextures;
	private final Map<SimplexFractionalBrownianMotionTexture, Integer> distinctToOffsetsSimplexFractionalBrownianMotionTextures;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public TextureCache() {
		this.distinctBlendTextures = new ArrayList<>();
		this.distinctBullseyeTextures = new ArrayList<>();
		this.distinctCheckerboardTextures = new ArrayList<>();
		this.distinctConstantTextures = new ArrayList<>();
		this.distinctLDRImageTextures = new ArrayList<>();
		this.distinctMarbleTextures = new ArrayList<>();
		this.distinctSimplexFractionalBrownianMotionTextures = new ArrayList<>();
		this.distinctSurfaceNormalTextures = new ArrayList<>();
		this.distinctUVTextures = new ArrayList<>();
		this.distinctToOffsetsBlendTextures = new LinkedHashMap<>();
		this.distinctToOffsetsBullseyeTextures = new LinkedHashMap<>();
		this.distinctToOffsetsCheckerboardTextures = new LinkedHashMap<>();
		this.distinctToOffsetsConstantTextures = new LinkedHashMap<>();
		this.distinctToOffsetsLDRImageTextures = new LinkedHashMap<>();
		this.distinctToOffsetsMarbleTextures = new LinkedHashMap<>();
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
	
	public float[] toTextureBlendTextureArray() {
		final float[] textureBlendTextureArray = Floats.toArray(this.distinctBlendTextures, blendTexture -> blendTexture.toArray(), 1);
		
		for(int i = 0; i < this.distinctBlendTextures.size(); i++) {
			final BlendTexture blendTexture = this.distinctBlendTextures.get(i);
			
			final Texture textureA = blendTexture.getTextureA();
			final Texture textureB = blendTexture.getTextureB();
			
			final int textureBlendTextureArrayTextureAOffset = i * BlendTexture.ARRAY_LENGTH + BlendTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureBlendTextureArrayTextureBOffset = i * BlendTexture.ARRAY_LENGTH + BlendTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureBlendTextureArray[textureBlendTextureArrayTextureAOffset] = pack(textureA.getID(), findOffsetFor(textureA));
			textureBlendTextureArray[textureBlendTextureArrayTextureBOffset] = pack(textureB.getID(), findOffsetFor(textureB));
		}
		
		return textureBlendTextureArray;
	}
	
	public float[] toTextureBullseyeTextureArray() {
		final float[] textureBullseyeTextureArray = Floats.toArray(this.distinctBullseyeTextures, bullseyeTexture -> bullseyeTexture.toArray(), 1);
		
		for(int i = 0; i < this.distinctBullseyeTextures.size(); i++) {
			final BullseyeTexture bullseyeTexture = this.distinctBullseyeTextures.get(i);
			
			final Texture textureA = bullseyeTexture.getTextureA();
			final Texture textureB = bullseyeTexture.getTextureB();
			
			final int textureBullseyeTextureArrayTextureAOffset = i * BullseyeTexture.ARRAY_LENGTH + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureBullseyeTextureArrayTextureBOffset = i * BullseyeTexture.ARRAY_LENGTH + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureBullseyeTextureArray[textureBullseyeTextureArrayTextureAOffset] = pack(textureA.getID(), findOffsetFor(textureA));
			textureBullseyeTextureArray[textureBullseyeTextureArrayTextureBOffset] = pack(textureB.getID(), findOffsetFor(textureB));
		}
		
		return textureBullseyeTextureArray;
	}
	
	public float[] toTextureCheckerboardTextureArray() {
		final float[] textureCheckerboardTextureArray = Floats.toArray(this.distinctCheckerboardTextures, checkerboardTexture -> checkerboardTexture.toArray(), 1);
		
		for(int i = 0; i < this.distinctCheckerboardTextures.size(); i++) {
			final CheckerboardTexture checkerboardTexture = this.distinctCheckerboardTextures.get(i);
			
			final Texture textureA = checkerboardTexture.getTextureA();
			final Texture textureB = checkerboardTexture.getTextureB();
			
			final int textureCheckerboardTextureArrayTextureAOffset = i * CheckerboardTexture.ARRAY_LENGTH + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A;
			final int textureCheckerboardTextureArrayTextureBOffset = i * CheckerboardTexture.ARRAY_LENGTH + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B;
			
			textureCheckerboardTextureArray[textureCheckerboardTextureArrayTextureAOffset] = pack(textureA.getID(), findOffsetFor(textureA));
			textureCheckerboardTextureArray[textureCheckerboardTextureArrayTextureBOffset] = pack(textureB.getID(), findOffsetFor(textureB));
		}
		
		return textureCheckerboardTextureArray;
	}
	
	public float[] toTextureConstantTextureArray() {
		return Floats.toArray(this.distinctConstantTextures, constantTexture -> constantTexture.toArray(), 1);
	}
	
	public float[] toTextureLDRImageTextureArray() {
		return Floats.toArray(this.distinctLDRImageTextures, lDRImageTexture -> lDRImageTexture.toArray(), 1);
	}
	
	public float[] toTextureMarbleTextureArray() {
		return Floats.toArray(this.distinctMarbleTextures, marbleTexture -> marbleTexture.toArray(), 1);
	}
	
	public float[] toTextureSimplexFractionalBrownianMotionTextureArray() {
		return Floats.toArray(this.distinctSimplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture -> simplexFractionalBrownianMotionTexture.toArray(), 1);
	}
	
	public int[] toTextureLDRImageTextureOffsetArray() {
		final int[] textureLDRImageTextureOffsetArray = new int[max(this.distinctLDRImageTextures.size(), 1)];
		
		for(int i = 0, j = 0; i < this.distinctLDRImageTextures.size(); i++) {
			final LDRImageTexture lDRImageTexture = this.distinctLDRImageTextures.get(i);
			
			textureLDRImageTextureOffsetArray[i] = j;
			
			j += lDRImageTexture.getArrayLength();
		}
		
		return textureLDRImageTextureOffsetArray;
	}
	
	public void clear() {
		this.distinctBlendTextures.clear();
		this.distinctBullseyeTextures.clear();
		this.distinctCheckerboardTextures.clear();
		this.distinctConstantTextures.clear();
		this.distinctLDRImageTextures.clear();
		this.distinctMarbleTextures.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSurfaceNormalTextures.clear();
		this.distinctUVTextures.clear();
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsLDRImageTextures.clear();
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
	}
	
	public void setup(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct BlendTexture instances:
		this.distinctBlendTextures.clear();
		this.distinctBlendTextures.addAll(NodeFilter.filterAllDistinct(scene, BlendTexture.class));
		
//		Add all distinct BullseyeTexture instances:
		this.distinctBullseyeTextures.clear();
		this.distinctBullseyeTextures.addAll(NodeFilter.filterAllDistinct(scene, BullseyeTexture.class));
		
//		Add all distinct CheckerboardTexture instances:
		this.distinctCheckerboardTextures.clear();
		this.distinctCheckerboardTextures.addAll(NodeFilter.filterAllDistinct(scene, CheckerboardTexture.class));
		
//		Add all distinct ConstantTexture instances:
		this.distinctConstantTextures.clear();
		this.distinctConstantTextures.addAll(NodeFilter.filterAllDistinct(scene, ConstantTexture.class));
		
//		Add all distinct LDRImageTexture instances:
		this.distinctLDRImageTextures.clear();
		this.distinctLDRImageTextures.addAll(NodeFilter.filterAllDistinct(scene, LDRImageTexture.class));
		
//		Add all distinct MarbleTexture instances:
		this.distinctMarbleTextures.clear();
		this.distinctMarbleTextures.addAll(NodeFilter.filterAllDistinct(scene, MarbleTexture.class));
		
//		Add all distinct SimplexFractionalBrownianMotionTexture instances:
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.addAll(NodeFilter.filterAllDistinct(scene, SimplexFractionalBrownianMotionTexture.class));
		
//		Add all distinct SurfaceNormalTexture instances:
		this.distinctSurfaceNormalTextures.clear();
		this.distinctSurfaceNormalTextures.addAll(NodeFilter.filterAllDistinct(scene, SurfaceNormalTexture.class));
		
//		Add all distinct UVTexture instances:
		this.distinctUVTextures.clear();
		this.distinctUVTextures.addAll(NodeFilter.filterAllDistinct(scene, UVTexture.class));
		
//		Create offset mappings for all distinct BlendTexture instances:
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBlendTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBlendTextures, 1));
		
//		Create offset mappings for all distinct BullseyeTexture instances:
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsBullseyeTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBullseyeTextures, 1));
		
//		Create offset mappings for all distinct CheckerboardTexture instances:
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCheckerboardTextures, 1));
		
//		Create offset mappings for all distinct ConstantTexture instances:
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsConstantTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctConstantTextures, 1));
		
//		Create offset mappings for all distinct LDRImageTexture instances:
		this.distinctToOffsetsLDRImageTextures.clear();
		this.distinctToOffsetsLDRImageTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageTextures, 1));
		
//		Create offset mappings for all distinct MarbleTexture instances:
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsMarbleTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMarbleTextures, 1));
		
//		Create offset mappings for all distinct SimplexFractionalBrownianMotionTexture instances:
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexFractionalBrownianMotionTextures, 1));
	}
}