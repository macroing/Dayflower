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
import static org.dayflower.utility.Ints.padding;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.PolkaDotTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.Texture;
import org.dayflower.utility.FloatArrays;
import org.dayflower.utility.Floats;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledTextureCache} contains {@link Texture} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledTextureCache {
	/**
	 * The length of a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_LENGTH = 8;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code A} in a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_OFFSET_TEXTURE_A = 0;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code B} in a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_OFFSET_TEXTURE_B = 1;
	
	/**
	 * The offset for the factor to use for component 1 in a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_OFFSET_T_COMPONENT_1 = 2;
	
	/**
	 * The offset for the factor to use for component 2 in a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_OFFSET_T_COMPONENT_2 = 3;
	
	/**
	 * The offset for the factor to use for component 3 in a compiled {@link BlendTexture} instance.
	 */
	public static final int BLEND_TEXTURE_OFFSET_T_COMPONENT_3 = 4;
	
	/**
	 * The length of a compiled {@link BullseyeTexture} instance.
	 */
	public static final int BULLSEYE_TEXTURE_LENGTH = 8;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the origin in a compiled {@link BullseyeTexture} instance.
	 */
	public static final int BULLSEYE_TEXTURE_OFFSET_ORIGIN = 0;
	
	/**
	 * The offset for the scale in a compiled {@link BullseyeTexture} instance.
	 */
	public static final int BULLSEYE_TEXTURE_OFFSET_SCALE = 5;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code A} in a compiled {@link BullseyeTexture} instance.
	 */
	public static final int BULLSEYE_TEXTURE_OFFSET_TEXTURE_A = 3;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code B} in a compiled {@link BullseyeTexture} instance.
	 */
	public static final int BULLSEYE_TEXTURE_OFFSET_TEXTURE_B = 4;
	
	/**
	 * The length of a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_LENGTH = 8;
	
	/**
	 * The offset for the angle in degrees in a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_OFFSET_ANGLE_DEGREES = 0;
	
	/**
	 * The offset for the angle in radians in a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_OFFSET_ANGLE_RADIANS = 1;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_OFFSET_SCALE = 4;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code A} in a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_A = 2;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code B} in a compiled {@link CheckerboardTexture} instance.
	 */
	public static final int CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_B = 3;
	
	/**
	 * The length of a compiled {@link ConstantTexture} instance.
	 */
	public static final int CONSTANT_TEXTURE_LENGTH = 4;
	
	/**
	 * The offset for the {@link Color3F} instance that represents the color in a compiled {@link ConstantTexture} instance.
	 */
	public static final int CONSTANT_TEXTURE_OFFSET_COLOR = 0;
	
	/**
	 * The offset for the angle in radians in a compiled {@link LDRImageTexture} instance.
	 */
	public static final int L_D_R_IMAGE_TEXTURE_OFFSET_ANGLE_RADIANS = 0;
	
	/**
	 * The offset for the image in a compiled {@link LDRImageTexture} instance.
	 */
	public static final int L_D_R_IMAGE_TEXTURE_OFFSET_IMAGE = 5;
	
	/**
	 * The offset for the resolution of the X-axis in a compiled {@link LDRImageTexture} instance.
	 */
	public static final int L_D_R_IMAGE_TEXTURE_OFFSET_RESOLUTION_X = 3;
	
	/**
	 * The offset for the resolution of the Y-axis in a compiled {@link LDRImageTexture} instance.
	 */
	public static final int L_D_R_IMAGE_TEXTURE_OFFSET_RESOLUTION_Y = 4;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link LDRImageTexture} instance.
	 */
	public static final int L_D_R_IMAGE_TEXTURE_OFFSET_SCALE = 1;
	
	/**
	 * The length of a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_LENGTH = 8;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code A} in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_COLOR_A = 0;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code B} in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_COLOR_B = 1;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code B} in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_COLOR_C = 2;
	
	/**
	 * The offset for the frequency in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_FREQUENCY = 3;
	
	/**
	 * The offset for the octaves in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_OCTAVES = 6;
	
	/**
	 * The offset for the scale in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_SCALE = 4;
	
	/**
	 * The offset for the stripes in a compiled {@link MarbleTexture} instance.
	 */
	public static final int MARBLE_TEXTURE_OFFSET_STRIPES = 5;
	
	/**
	 * The length of a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_LENGTH = 8;
	
	/**
	 * The offset for the angle in degrees in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_ANGLE_DEGREES = 0;
	
	/**
	 * The offset for the angle in radians in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_ANGLE_RADIANS = 1;
	
	/**
	 * The offset for the cell resolution in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_CELL_RESOLUTION = 4;
	
	/**
	 * The offset for the polka dot radius in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_POLKA_DOT_RADIUS = 5;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code A} in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_TEXTURE_A = 2;
	
	/**
	 * The offset for the {@link Texture} denoted by {@code B} in a compiled {@link PolkaDotTexture} instance.
	 */
	public static final int POLKA_DOT_TEXTURE_OFFSET_TEXTURE_B = 3;
	
	/**
	 * The length of a compiled {@link SimplexFractionalBrownianMotionTexture} instance.
	 */
	public static final int SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH = 4;
	
	/**
	 * The offset for the {@link Color3F} instance that represents the color in a compiled {@link SimplexFractionalBrownianMotionTexture} instance.
	 */
	public static final int SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_COLOR = 0;
	
	/**
	 * The offset for the frequency in a compiled {@link SimplexFractionalBrownianMotionTexture} instance.
	 */
	public static final int SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_FREQUENCY = 1;
	
	/**
	 * The offset for the gain in a compiled {@link SimplexFractionalBrownianMotionTexture} instance.
	 */
	public static final int SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_GAIN = 2;
	
	/**
	 * The offset for the octaves in a compiled {@link SimplexFractionalBrownianMotionTexture} instance.
	 */
	public static final int SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_OCTAVES = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] blendTextures;
	private float[] bullseyeTextures;
	private float[] checkerboardTextures;
	private float[] constantTextures;
	private float[] lDRImageTextures;
	private float[] marbleTextures;
	private float[] polkaDotTextures;
	private float[] simplexFractionalBrownianMotionTextures;
	private int[] lDRImageTextureOffsets;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledTextureCache} instance.
	 */
	public CompiledTextureCache() {
		setBlendTextures(new float[0]);
		setBullseyeTextures(new float[0]);
		setCheckerboardTextures(new float[0]);
		setConstantTextures(new float[0]);
		setLDRImageTextureOffsets(new int[0]);
		setLDRImageTextures(new float[0]);
		setMarbleTextures(new float[0]);
		setPolkaDotTextures(new float[0]);
		setSimplexFractionalBrownianMotionTextures(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code blendTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code blendTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code blendTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public boolean removeBlendTexture(final float[] blendTexture) {
		final int absoluteOffset = getBlendTextureOffsetAbsolute(blendTexture);
		
		if(absoluteOffset != -1) {
			setBlendTextures(Structures.removeStructure(getBlendTextures(), absoluteOffset, BLEND_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code bullseyeTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code bullseyeTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code bullseyeTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public boolean removeBullseyeTexture(final float[] bullseyeTexture) {
		final int absoluteOffset = getBullseyeTextureOffsetAbsolute(bullseyeTexture);
		
		if(absoluteOffset != -1) {
			setBullseyeTextures(Structures.removeStructure(getBullseyeTextures(), absoluteOffset, BULLSEYE_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code checkerboardTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code checkerboardTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code checkerboardTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public boolean removeCheckerboardTexture(final float[] checkerboardTexture) {
		final int absoluteOffset = getCheckerboardTextureOffsetAbsolute(checkerboardTexture);
		
		if(absoluteOffset != -1) {
			setCheckerboardTextures(Structures.removeStructure(getCheckerboardTextures(), absoluteOffset, CHECKERBOARD_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code constantTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code constantTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code constantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param constantTexture a {@link ConstantTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code constantTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code constantTexture} is {@code null}
	 */
	public boolean removeConstantTexture(final float[] constantTexture) {
		final int absoluteOffset = getConstantTextureOffsetAbsolute(constantTexture);
		
		if(absoluteOffset != -1) {
			setConstantTextures(Structures.removeStructure(getConstantTextures(), absoluteOffset, CONSTANT_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code lDRImageTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code lDRImageTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageTexture.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code lDRImageTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageTexture.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public boolean removeLDRImageTexture(final float[] lDRImageTexture) {
		final int absoluteOffset = getLDRImageTextureOffsetAbsolute(lDRImageTexture);
		
		if(absoluteOffset != -1) {
			setLDRImageTextureOffsets(Structures.removeStructureOffset(getLDRImageTextureOffsets(), absoluteOffset, lDRImageTexture.length));
			setLDRImageTextures(Structures.removeStructure(getLDRImageTextures(), absoluteOffset, lDRImageTexture.length));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code marbleTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code marbleTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code marbleTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public boolean removeMarbleTexture(final float[] marbleTexture) {
		final int absoluteOffset = getMarbleTextureOffsetAbsolute(marbleTexture);
		
		if(absoluteOffset != -1) {
			setMarbleTextures(Structures.removeStructure(getMarbleTextures(), absoluteOffset, MARBLE_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code polkaDotTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code polkaDotTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code polkaDotTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public boolean removePolkaDotTexture(final float[] polkaDotTexture) {
		final int absoluteOffset = getPolkaDotTextureOffsetAbsolute(polkaDotTexture);
		
		if(absoluteOffset != -1) {
			setPolkaDotTextures(Structures.removeStructure(getPolkaDotTextures(), absoluteOffset, POLKA_DOT_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code simplexFractionalBrownianMotionTexture} from this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code simplexFractionalBrownianMotionTexture} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code simplexFractionalBrownianMotionTexture} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public boolean removeSimplexFractionalBrownianMotionTexture(final float[] simplexFractionalBrownianMotionTexture) {
		final int absoluteOffset = getSimplexFractionalBrownianMotionTextureOffsetAbsolute(simplexFractionalBrownianMotionTexture);
		
		if(absoluteOffset != -1) {
			setSimplexFractionalBrownianMotionTextures(Structures.removeStructure(getSimplexFractionalBrownianMotionTextures(), absoluteOffset, SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Updates {@code oldBlendTexture} to {@code newBlendTexture} in this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code oldBlendTexture} was updated, {@code false} otherwise.
	 * <p>
	 * If either {@code oldBlendTexture} or {@code newBlendTexture} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code oldBlendTexture.length} or {@code newBlendTexture.length} are not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param oldBlendTexture the old {@link BlendTexture} instance in compiled form
	 * @param newBlendTexture the new {@code BlendTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code oldBlendTexture} was updated, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code oldBlendTexture.length} or {@code newBlendTexture.length} are not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, either {@code oldBlendTexture} or {@code newBlendTexture} are {@code null}
	 */
	public boolean updateBlendTexture(final float[] oldBlendTexture, final float[] newBlendTexture) {
		Objects.requireNonNull(oldBlendTexture, "oldBlendTexture == null");
		Objects.requireNonNull(newBlendTexture, "newBlendTexture == null");
		
		ParameterArguments.requireExactArrayLength(oldBlendTexture, BLEND_TEXTURE_LENGTH, "oldBlendTexture");
		ParameterArguments.requireExactArrayLength(newBlendTexture, BLEND_TEXTURE_LENGTH, "newBlendTexture");
		
		final int absoluteOffset = getBlendTextureOffsetAbsolute(oldBlendTexture);
		
		if(absoluteOffset != -1) {
			return Structures.updateStructure(this.blendTextures, oldBlendTexture, newBlendTexture, absoluteOffset);
		}
		
		return false;
	}
	
	/**
	 * Updates {@code oldBlendTexture} to {@code newBlendTexture} in this {@code CompiledTextureCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code oldBlendTexture} was updated, {@code false} otherwise.
	 * <p>
	 * If either {@code oldBlendTexture} or {@code newBlendTexture} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code oldBlendTexture.length} or {@code newBlendTexture.length} are not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param oldBlendTexture the old {@link BlendTexture} instance in compiled form
	 * @param newBlendTexture the new {@code BlendTexture} instance in compiled form
	 * @return {@code true} if, and only if, {@code oldBlendTexture} was updated, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code oldBlendTexture.length} or {@code newBlendTexture.length} are not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, either {@code oldBlendTexture} or {@code newBlendTexture} are {@code null}
	 */
	public boolean updateBullseyeTexture(final float[] oldBullseyeTexture, final float[] newBullseyeTexture) {
		Objects.requireNonNull(oldBullseyeTexture, "oldBullseyeTexture == null");
		Objects.requireNonNull(newBullseyeTexture, "newBullseyeTexture == null");
		
		ParameterArguments.requireExactArrayLength(oldBullseyeTexture, BULLSEYE_TEXTURE_LENGTH, "oldBullseyeTexture");
		ParameterArguments.requireExactArrayLength(newBullseyeTexture, BULLSEYE_TEXTURE_LENGTH, "newBullseyeTexture");
		
		final int absoluteOffset = getBullseyeTextureOffsetAbsolute(oldBullseyeTexture);
		
		if(absoluteOffset != -1) {
			return Structures.updateStructure(this.bullseyeTextures, oldBullseyeTexture, newBullseyeTexture, absoluteOffset);
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BlendTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BlendTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getBlendTextures() {
		return this.blendTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getBullseyeTextures() {
		return this.bullseyeTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getCheckerboardTextures() {
		return this.checkerboardTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link ConstantTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code ConstantTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getConstantTextures() {
		return this.constantTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getLDRImageTextures() {
		return this.lDRImageTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link MarbleTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code MarbleTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getMarbleTextures() {
		return this.marbleTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PolkaDotTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PolkaDotTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getPolkaDotTextures() {
		return this.polkaDotTextures;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getSimplexFractionalBrownianMotionTextures() {
		return this.simplexFractionalBrownianMotionTextures;
	}
	
	/**
	 * Adds {@code blendTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code blendTexture}.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance in compiled form
	 * @return the relative offset to {@code blendTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public int addBlendTexture(final float[] blendTexture) {
		final int relativeOffsetOld = getBlendTextureOffsetRelative(blendTexture);
		final int relativeOffsetNew = getBlendTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setBlendTextures(FloatArrays.merge(getBlendTextures(), blendTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code bullseyeTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code bullseyeTexture}.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return the relative offset to {@code bullseyeTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public int addBullseyeTexture(final float[] bullseyeTexture) {
		final int relativeOffsetOld = getBullseyeTextureOffsetRelative(bullseyeTexture);
		final int relativeOffsetNew = getBullseyeTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setBullseyeTextures(FloatArrays.merge(getBullseyeTextures(), bullseyeTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code checkerboardTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code checkerboardTexture}.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return the relative offset to {@code checkerboardTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public int addCheckerboardTexture(final float[] checkerboardTexture) {
		final int relativeOffsetOld = getCheckerboardTextureOffsetRelative(checkerboardTexture);
		final int relativeOffsetNew = getCheckerboardTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setCheckerboardTextures(FloatArrays.merge(getCheckerboardTextures(), checkerboardTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code constantTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code constantTexture}.
	 * <p>
	 * If {@code constantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param constantTexture a {@link ConstantTexture} instance in compiled form
	 * @return the relative offset to {@code constantTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code constantTexture} is {@code null}
	 */
	public int addConstantTexture(final float[] constantTexture) {
		final int relativeOffsetOld = getConstantTextureOffsetRelative(constantTexture);
		final int relativeOffsetNew = getConstantTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setConstantTextures(FloatArrays.merge(getConstantTextures(), constantTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code lDRImageTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code lDRImageTexture}.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageTexture.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance in compiled form
	 * @return the relative offset to {@code lDRImageTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageTexture.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public int addLDRImageTexture(final float[] lDRImageTexture) {
		final int absoluteOffsetNew = this.lDRImageTextures.length;
		final int relativeOffsetOld = getLDRImageTextureOffsetRelative(lDRImageTexture);
		final int relativeOffsetNew = getLDRImageTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLDRImageTextureOffsets(Structures.addStructureOffset(getLDRImageTextureOffsets(), absoluteOffsetNew));
		setLDRImageTextures(FloatArrays.merge(getLDRImageTextures(), lDRImageTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code marbleTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code marbleTexture}.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return the relative offset to {@code marbleTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public int addMarbleTexture(final float[] marbleTexture) {
		final int relativeOffsetOld = getMarbleTextureOffsetRelative(marbleTexture);
		final int relativeOffsetNew = getMarbleTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setMarbleTextures(FloatArrays.merge(getMarbleTextures(), marbleTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code polkaDotTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code polkaDotTexture}.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return the relative offset to {@code polkaDotTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public int addPolkaDotTexture(final float[] polkaDotTexture) {
		final int relativeOffsetOld = getPolkaDotTextureOffsetRelative(polkaDotTexture);
		final int relativeOffsetNew = getPolkaDotTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setPolkaDotTextures(FloatArrays.merge(getPolkaDotTextures(), polkaDotTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code simplexFractionalBrownianMotionTexture} to this {@code CompiledTextureCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code simplexFractionalBrownianMotionTexture}.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return the relative offset to {@code simplexFractionalBrownianMotionTexture}
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public int addSimplexFractionalBrownianMotionTexture(final float[] simplexFractionalBrownianMotionTexture) {
		final int relativeOffsetOld = getSimplexFractionalBrownianMotionTextureOffsetRelative(simplexFractionalBrownianMotionTexture);
		final int relativeOffsetNew = getSimplexFractionalBrownianMotionTextureCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setSimplexFractionalBrownianMotionTextures(FloatArrays.merge(getSimplexFractionalBrownianMotionTextures(), simplexFractionalBrownianMotionTexture));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link BlendTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code BlendTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getBlendTextureCount() {
		return Structures.getStructureCount(this.blendTextures, BLEND_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code blendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance in compiled form
	 * @return the absolute offset of {@code blendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public int getBlendTextureOffsetAbsolute(final float[] blendTexture) {
		Objects.requireNonNull(blendTexture, "blendTexture == null");
		
		ParameterArguments.requireExactArrayLength(blendTexture, BLEND_TEXTURE_LENGTH, "blendTexture");
		
		return Structures.getStructureOffsetAbsolute(this.blendTextures, blendTexture);
	}
	
	/**
	 * Returns the relative offset of {@code blendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance in compiled form
	 * @return the relative offset of {@code blendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code blendTexture.length} is not equal to {@code CompiledTextureCache.BLEND_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public int getBlendTextureOffsetRelative(final float[] blendTexture) {
		Objects.requireNonNull(blendTexture, "blendTexture == null");
		
		ParameterArguments.requireExactArrayLength(blendTexture, BLEND_TEXTURE_LENGTH, "blendTexture");
		
		return Structures.getStructureOffsetRelative(this.blendTextures, blendTexture);
	}
	
	/**
	 * Returns the {@link BullseyeTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code BullseyeTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getBullseyeTextureCount() {
		return Structures.getStructureCount(this.bullseyeTextures, BULLSEYE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code bullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return the absolute offset of {@code bullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public int getBullseyeTextureOffsetAbsolute(final float[] bullseyeTexture) {
		Objects.requireNonNull(bullseyeTexture, "bullseyeTexture == null");
		
		ParameterArguments.requireExactArrayLength(bullseyeTexture, BULLSEYE_TEXTURE_LENGTH, "bullseyeTexture");
		
		return Structures.getStructureOffsetAbsolute(this.bullseyeTextures, bullseyeTexture);
	}
	
	/**
	 * Returns the relative offset of {@code bullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return the relative offset of {@code bullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeTexture.length} is not equal to {@code CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public int getBullseyeTextureOffsetRelative(final float[] bullseyeTexture) {
		Objects.requireNonNull(bullseyeTexture, "bullseyeTexture == null");
		
		ParameterArguments.requireExactArrayLength(bullseyeTexture, BULLSEYE_TEXTURE_LENGTH, "bullseyeTexture");
		
		return Structures.getStructureOffsetRelative(this.bullseyeTextures, bullseyeTexture);
	}
	
	/**
	 * Returns the {@link CheckerboardTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code CheckerboardTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getCheckerboardTextureCount() {
		return Structures.getStructureCount(this.checkerboardTextures, CHECKERBOARD_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code checkerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return the absolute offset of {@code checkerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public int getCheckerboardTextureOffsetAbsolute(final float[] checkerboardTexture) {
		Objects.requireNonNull(checkerboardTexture, "checkerboardTexture == null");
		
		ParameterArguments.requireExactArrayLength(checkerboardTexture, CHECKERBOARD_TEXTURE_LENGTH, "checkerboardTexture");
		
		return Structures.getStructureOffsetAbsolute(this.checkerboardTextures, checkerboardTexture);
	}
	
	/**
	 * Returns the relative offset of {@code checkerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return the relative offset of {@code checkerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardTexture.length} is not equal to {@code CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public int getCheckerboardTextureOffsetRelative(final float[] checkerboardTexture) {
		Objects.requireNonNull(checkerboardTexture, "checkerboardTexture == null");
		
		ParameterArguments.requireExactArrayLength(checkerboardTexture, CHECKERBOARD_TEXTURE_LENGTH, "checkerboardTexture");
		
		return Structures.getStructureOffsetRelative(this.checkerboardTextures, checkerboardTexture);
	}
	
	/**
	 * Returns the {@link ConstantTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code ConstantTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getConstantTextureCount() {
		return Structures.getStructureCount(this.constantTextures, CONSTANT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code constantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code constantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param constantTexture a {@link ConstantTexture} instance in compiled form
	 * @return the absolute offset of {@code constantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code constantTexture} is {@code null}
	 */
	public int getConstantTextureOffsetAbsolute(final float[] constantTexture) {
		Objects.requireNonNull(constantTexture, "constantTexture == null");
		
		ParameterArguments.requireExactArrayLength(constantTexture, CONSTANT_TEXTURE_LENGTH, "constantTexture");
		
		return Structures.getStructureOffsetAbsolute(this.constantTextures, constantTexture);
	}
	
	/**
	 * Returns the relative offset of {@code constantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code constantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param constantTexture a {@link ConstantTexture} instance in compiled form
	 * @return the relative offset of {@code constantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code constantTexture.length} is not equal to {@code CompiledTextureCache.CONSTANT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code constantTexture} is {@code null}
	 */
	public int getConstantTextureOffsetRelative(final float[] constantTexture) {
		Objects.requireNonNull(constantTexture, "constantTexture == null");
		
		ParameterArguments.requireExactArrayLength(constantTexture, CONSTANT_TEXTURE_LENGTH, "constantTexture");
		
		return Structures.getStructureOffsetRelative(this.constantTextures, constantTexture);
	}
	
	/**
	 * Returns the {@link LDRImageTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code LDRImageTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getLDRImageTextureCount() {
		return this.lDRImageTextureOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code lDRImageTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageTexture.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance in compiled form
	 * @return the absolute offset of {@code lDRImageTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageTexture.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public int getLDRImageTextureOffsetAbsolute(final float[] lDRImageTexture) {
		Objects.requireNonNull(lDRImageTexture, "lDRImageTexture == null");
		
		ParameterArguments.requireExact(lDRImageTexture.length % 8, 0, "lDRImageTexture.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.lDRImageTextures, lDRImageTexture, this.lDRImageTextureOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code lDRImageTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageTexture.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance in compiled form
	 * @return the relative offset of {@code lDRImageTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageTexture.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public int getLDRImageTextureOffsetRelative(final float[] lDRImageTexture) {
		Objects.requireNonNull(lDRImageTexture, "lDRImageTexture == null");
		
		ParameterArguments.requireExact(lDRImageTexture.length % 8, 0, "lDRImageTexture.length % 8");
		
		return Structures.getStructureOffsetRelative(this.lDRImageTextures, lDRImageTexture, this.lDRImageTextureOffsets);
	}
	
	/**
	 * Returns the {@link MarbleTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code MarbleTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getMarbleTextureCount() {
		return Structures.getStructureCount(this.marbleTextures, MARBLE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code marbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return the absolute offset of {@code marbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public int getMarbleTextureOffsetAbsolute(final float[] marbleTexture) {
		Objects.requireNonNull(marbleTexture, "marbleTexture == null");
		
		ParameterArguments.requireExactArrayLength(marbleTexture, MARBLE_TEXTURE_LENGTH, "marbleTexture");
		
		return Structures.getStructureOffsetAbsolute(this.marbleTextures, marbleTexture);
	}
	
	/**
	 * Returns the relative offset of {@code marbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return the relative offset of {@code marbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code marbleTexture.length} is not equal to {@code CompiledTextureCache.MARBLE_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public int getMarbleTextureOffsetRelative(final float[] marbleTexture) {
		Objects.requireNonNull(marbleTexture, "marbleTexture == null");
		
		ParameterArguments.requireExactArrayLength(marbleTexture, MARBLE_TEXTURE_LENGTH, "marbleTexture");
		
		return Structures.getStructureOffsetRelative(this.marbleTextures, marbleTexture);
	}
	
	/**
	 * Returns the {@link PolkaDotTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code PolkaDotTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getPolkaDotTextureCount() {
		return Structures.getStructureCount(this.polkaDotTextures, POLKA_DOT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code polkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return the absolute offset of {@code polkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public int getPolkaDotTextureOffsetAbsolute(final float[] polkaDotTexture) {
		Objects.requireNonNull(polkaDotTexture, "polkaDotTexture == null");
		
		ParameterArguments.requireExactArrayLength(polkaDotTexture, POLKA_DOT_TEXTURE_LENGTH, "polkaDotTexture");
		
		return Structures.getStructureOffsetAbsolute(this.polkaDotTextures, polkaDotTexture);
	}
	
	/**
	 * Returns the relative offset of {@code polkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return the relative offset of {@code polkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotTexture.length} is not equal to {@code CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public int getPolkaDotTextureOffsetRelative(final float[] polkaDotTexture) {
		Objects.requireNonNull(polkaDotTexture, "polkaDotTexture == null");
		
		ParameterArguments.requireExactArrayLength(polkaDotTexture, POLKA_DOT_TEXTURE_LENGTH, "polkaDotTexture");
		
		return Structures.getStructureOffsetRelative(this.polkaDotTextures, polkaDotTexture);
	}
	
	/**
	 * Returns the {@link SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getSimplexFractionalBrownianMotionTextureCount() {
		return Structures.getStructureCount(this.simplexFractionalBrownianMotionTextures, SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code simplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return the absolute offset of {@code simplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public int getSimplexFractionalBrownianMotionTextureOffsetAbsolute(final float[] simplexFractionalBrownianMotionTexture) {
		Objects.requireNonNull(simplexFractionalBrownianMotionTexture, "simplexFractionalBrownianMotionTexture == null");
		
		ParameterArguments.requireExactArrayLength(simplexFractionalBrownianMotionTexture, SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH, "simplexFractionalBrownianMotionTexture");
		
		return Structures.getStructureOffsetAbsolute(this.simplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture);
	}
	
	/**
	 * Returns the relative offset of {@code simplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return the relative offset of {@code simplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture.length} is not equal to {@code CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public int getSimplexFractionalBrownianMotionTextureOffsetRelative(final float[] simplexFractionalBrownianMotionTexture) {
		Objects.requireNonNull(simplexFractionalBrownianMotionTexture, "simplexFractionalBrownianMotionTexture == null");
		
		ParameterArguments.requireExactArrayLength(simplexFractionalBrownianMotionTexture, SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH, "simplexFractionalBrownianMotionTexture");
		
		return Structures.getStructureOffsetRelative(this.simplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture);
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances in this {@code CompiledTextureCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances in this {@code CompiledTextureCache} instance
	 */
	public int[] getLDRImageTextureOffsets() {
		return this.lDRImageTextureOffsets;
	}
	
	/**
	 * Sets all {@link BlendTexture} instances in compiled form to {@code blendTextures}.
	 * <p>
	 * If {@code blendTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code blendTextures.length % CompiledTextureCache.BLEND_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param blendTextures the {@code BlendTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code blendTextures.length % CompiledTextureCache.BLEND_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code blendTextures} is {@code null}
	 */
	public void setBlendTextures(final float[] blendTextures) {
		Objects.requireNonNull(blendTextures, "blendTextures == null");
		
		ParameterArguments.requireExact(blendTextures.length % BLEND_TEXTURE_LENGTH, 0, "blendTextures.length % CompiledTextureCache.BLEND_TEXTURE_LENGTH");
		
		this.blendTextures = blendTextures;
	}
	
	/**
	 * Sets all {@link BullseyeTexture} instances in compiled form to {@code bullseyeTextures}.
	 * <p>
	 * If {@code bullseyeTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code bullseyeTextures.length % CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bullseyeTextures the {@code BullseyeTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code bullseyeTextures.length % CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTextures} is {@code null}
	 */
	public void setBullseyeTextures(final float[] bullseyeTextures) {
		Objects.requireNonNull(bullseyeTextures, "bullseyeTextures == null");
		
		ParameterArguments.requireExact(bullseyeTextures.length % BULLSEYE_TEXTURE_LENGTH, 0, "bullseyeTextures.length % CompiledTextureCache.BULLSEYE_TEXTURE_LENGTH");
		
		this.bullseyeTextures = bullseyeTextures;
	}
	
	/**
	 * Sets all {@link CheckerboardTexture} instances in compiled form to {@code checkerboardTextures}.
	 * <p>
	 * If {@code checkerboardTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code checkerboardTextures.length % CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param checkerboardTextures the {@code CheckerboardTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code checkerboardTextures.length % CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTextures} is {@code null}
	 */
	public void setCheckerboardTextures(final float[] checkerboardTextures) {
		Objects.requireNonNull(checkerboardTextures, "checkerboardTextures == null");
		
		ParameterArguments.requireExact(checkerboardTextures.length % CHECKERBOARD_TEXTURE_LENGTH, 0, "checkerboardTextures.length % CompiledTextureCache.CHECKERBOARD_TEXTURE_LENGTH");
		
		this.checkerboardTextures = checkerboardTextures;
	}
	
	/**
	 * Sets all {@link ConstantTexture} instances in compiled form to {@code constantTextures}.
	 * <p>
	 * If {@code constantTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code constantTextures.length % CompiledTextureCache.CONSTANT_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param constantTextures the {@code ConstantTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code constantTextures.length % CompiledTextureCache.CONSTANT_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code constantTextures} is {@code null}
	 */
	public void setConstantTextures(final float[] constantTextures) {
		Objects.requireNonNull(constantTextures, "constantTextures == null");
		
		ParameterArguments.requireExact(constantTextures.length % CONSTANT_TEXTURE_LENGTH, 0, "constantTextures.length % CompiledTextureCache.CONSTANT_TEXTURE_LENGTH");
		
		this.constantTextures = constantTextures;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances to {@code lDRImageTextureOffsets}.
	 * <p>
	 * If {@code lDRImageTextureOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code lDRImageTextureOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTextureOffsets the {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code lDRImageTextureOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTextureOffsets} is {@code null}
	 */
	public void setLDRImageTextureOffsets(final int[] lDRImageTextureOffsets) {
		Objects.requireNonNull(lDRImageTextureOffsets, "lDRImageTextureOffsets == null");
		
		ParameterArguments.requireRange(lDRImageTextureOffsets, 0, Integer.MAX_VALUE, "lDRImageTextureOffsets");
		
		this.lDRImageTextureOffsets = lDRImageTextureOffsets;
	}
	
	/**
	 * Sets all {@link LDRImageTexture} instances in compiled form to {@code lDRImageTextures}.
	 * <p>
	 * If {@code lDRImageTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageTextures.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageTextures the {@code LDRImageTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageTextures.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTextures} is {@code null}
	 */
	public void setLDRImageTextures(final float[] lDRImageTextures) {
		Objects.requireNonNull(lDRImageTextures, "lDRImageTextures == null");
		
		ParameterArguments.requireExact(lDRImageTextures.length % 8, 0, "lDRImageTextures.length % 8");
		
		this.lDRImageTextures = lDRImageTextures;
	}
	
	/**
	 * Sets all {@link MarbleTexture} instances in compiled form to {@code marbleTextures}.
	 * <p>
	 * If {@code marbleTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code marbleTextures.length % CompiledTextureCache.MARBLE_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param marbleTextures the {@code MarbleTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code marbleTextures.length % CompiledTextureCache.MARBLE_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code marbleTextures} is {@code null}
	 */
	public void setMarbleTextures(final float[] marbleTextures) {
		Objects.requireNonNull(marbleTextures, "marbleTextures == null");
		
		ParameterArguments.requireExact(marbleTextures.length % MARBLE_TEXTURE_LENGTH, 0, "marbleTextures.length % CompiledTextureCache.MARBLE_TEXTURE_LENGTH");
		
		this.marbleTextures = marbleTextures;
	}
	
	/**
	 * Sets all {@link PolkaDotTexture} instances in compiled form to {@code polkaDotTextures}.
	 * <p>
	 * If {@code polkaDotTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code polkaDotTextures.length % CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param polkaDotTextures the {@code PolkaDotTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code polkaDotTextures.length % CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTextures} is {@code null}
	 */
	public void setPolkaDotTextures(final float[] polkaDotTextures) {
		Objects.requireNonNull(polkaDotTextures, "polkaDotTextures == null");
		
		ParameterArguments.requireExact(polkaDotTextures.length % POLKA_DOT_TEXTURE_LENGTH, 0, "polkaDotTextures.length % CompiledTextureCache.POLKA_DOT_TEXTURE_LENGTH");
		
		this.polkaDotTextures = polkaDotTextures;
	}
	
	/**
	 * Sets all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form to {@code simplexFractionalBrownianMotionTextures}.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTextures} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTextures.length % CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTextures the {@code SimplexFractionalBrownianMotionTexture} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexFractionalBrownianMotionTextures.length % CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTextures} is {@code null}
	 */
	public void setSimplexFractionalBrownianMotionTextures(final float[] simplexFractionalBrownianMotionTextures) {
		Objects.requireNonNull(simplexFractionalBrownianMotionTextures, "simplexFractionalBrownianMotionTextures == null");
		
		ParameterArguments.requireExact(simplexFractionalBrownianMotionTextures.length % SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH, 0, "simplexFractionalBrownianMotionTextures.length % CompiledTextureCache.SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH");
		
		this.simplexFractionalBrownianMotionTextures = simplexFractionalBrownianMotionTextures;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code blendTexture} in compiled form.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toBlendTexture(blendTexture, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param blendTexture a {@link BlendTexture} instance
	 * @return a {@code float[]} with {@code blendTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public static float[] toBlendTexture(final BlendTexture blendTexture) {
		return toBlendTexture(blendTexture, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code blendTexture} in compiled form.
	 * <p>
	 * If either {@code blendTexture} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with {@code blendTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code blendTexture} or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toBlendTexture(final BlendTexture blendTexture, final ToIntFunction<Texture> textureOffsetFunction) {
		final Texture textureA = blendTexture.getTextureA();
		final Texture textureB = blendTexture.getTextureB();
		
		final float tComponent1 = blendTexture.getTComponent1();
		final float tComponent2 = blendTexture.getTComponent2();
		final float tComponent3 = blendTexture.getTComponent3();
		
		final int textureAValue = pack(textureA.getID(), textureOffsetFunction.applyAsInt(textureA));
		final int textureBValue = pack(textureB.getID(), textureOffsetFunction.applyAsInt(textureB));
		
		final float[] array = new float[BLEND_TEXTURE_LENGTH];
		
//		Because the BlendTexture occupy 8/8 positions in a block, it should be aligned.
		array[BLEND_TEXTURE_OFFSET_TEXTURE_A] = textureAValue;	//Block #1
		array[BLEND_TEXTURE_OFFSET_TEXTURE_B] = textureBValue;	//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_1] = tComponent1;//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_2] = tComponent2;//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_3] = tComponent3;//Block #1
		array[5] = 0.0F;										//Block #1
		array[6] = 0.0F;										//Block #1
		array[7] = 0.0F;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BlendTexture} instances in {@code blendTextures} in compiled form.
	 * <p>
	 * If {@code blendTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toBlendTextures(blendTextures, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param blendTextures a {@code List} of {@code BlendTexture} instances
	 * @return a {@code float[]} with all {@code BlendTexture} instances in {@code blendTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code blendTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toBlendTextures(final List<BlendTexture> blendTextures) {
		return toBlendTextures(blendTextures, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BlendTexture} instances in {@code blendTextures} in compiled form.
	 * <p>
	 * If either {@code blendTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param blendTextures a {@code List} of {@code BlendTexture} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with all {@code BlendTexture} instances in {@code blendTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code blendTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toBlendTextures(final List<BlendTexture> blendTextures, final ToIntFunction<Texture> textureOffsetFunction) {
		return Floats.toArray(blendTextures, blendTexture -> toBlendTexture(blendTexture, textureOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeTexture} in compiled form.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toBullseyeTexture(bullseyeTexture, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance
	 * @return a {@code float[]} with {@code bullseyeTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public static float[] toBullseyeTexture(final BullseyeTexture bullseyeTexture) {
		return toBullseyeTexture(bullseyeTexture, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeTexture} in compiled form.
	 * <p>
	 * If either {@code bullseyeTexture} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with {@code bullseyeTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code bullseyeTexture} or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toBullseyeTexture(final BullseyeTexture bullseyeTexture, final ToIntFunction<Texture> textureOffsetFunction) {
		final Point3F origin = bullseyeTexture.getOrigin();
		
		final Texture textureA = bullseyeTexture.getTextureA();
		final Texture textureB = bullseyeTexture.getTextureB();
		
		final float scale = bullseyeTexture.getScale();
		
		final int textureAValue = pack(textureA.getID(), textureOffsetFunction.applyAsInt(textureA));
		final int textureBValue = pack(textureB.getID(), textureOffsetFunction.applyAsInt(textureB));
		
		final float[] array = new float[BULLSEYE_TEXTURE_LENGTH];
		
//		Because the BullseyeTexture occupy 8/8 positions in a block, it should be aligned.
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 0] = origin.getX();	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 1] = origin.getY();	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 2] = origin.getZ();	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_TEXTURE_A] = textureAValue;	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_TEXTURE_B] = textureBValue;	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_SCALE] = scale;				//Block #1
		array[6] = 0.0F;											//Block #1
		array[7] = 0.0F;											//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BullseyeTexture} instances in {@code bullseyeTextures} in compiled form.
	 * <p>
	 * If {@code bullseyeTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toBullseyeTextures(bullseyeTextures, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param bullseyeTextures a {@code List} of {@code BullseyeTexture} instances
	 * @return a {@code float[]} with all {@code BullseyeTexture} instances in {@code bullseyeTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toBullseyeTextures(final List<BullseyeTexture> bullseyeTextures) {
		return toBullseyeTextures(bullseyeTextures, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BullseyeTexture} instances in {@code bullseyeTextures} in compiled form.
	 * <p>
	 * If either {@code bullseyeTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeTextures a {@code List} of {@code BullseyeTexture} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with all {@code BullseyeTexture} instances in {@code bullseyeTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code bullseyeTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toBullseyeTextures(final List<BullseyeTexture> bullseyeTextures, final ToIntFunction<Texture> textureOffsetFunction) {
		return Floats.toArray(bullseyeTextures, bullseyeTexture -> toBullseyeTexture(bullseyeTexture, textureOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardTexture} in compiled form.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toCheckerboardTexture(checkerboardTexture, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance
	 * @return a {@code float[]} with {@code checkerboardTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public static float[] toCheckerboardTexture(final CheckerboardTexture checkerboardTexture) {
		return toCheckerboardTexture(checkerboardTexture, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardTexture} in compiled form.
	 * <p>
	 * If either {@code checkerboardTexture} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with {@code checkerboardTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code checkerboardTexture} or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toCheckerboardTexture(final CheckerboardTexture checkerboardTexture, final ToIntFunction<Texture> textureOffsetFunction) {
		final AngleF angle = checkerboardTexture.getAngle();
		
		final Texture textureA = checkerboardTexture.getTextureA();
		final Texture textureB = checkerboardTexture.getTextureB();
		
		final Vector2F scale = checkerboardTexture.getScale();
		
		final int textureAValue = pack(textureA.getID(), textureOffsetFunction.applyAsInt(textureA));
		final int textureBValue = pack(textureB.getID(), textureOffsetFunction.applyAsInt(textureB));
		
		final float[] array = new float[CHECKERBOARD_TEXTURE_LENGTH];
		
//		Because the CheckerboardTexture occupy 8/8 positions in a block, it should be aligned.
		array[CHECKERBOARD_TEXTURE_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_A] = textureAValue;			//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_B] = textureBValue;			//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_SCALE + 0] = scale.getX();			//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_SCALE + 1] = scale.getY();			//Block #1
		array[6] = 0.0F;														//Block #1
		array[7] = 0.0F;														//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link CheckerboardTexture} instances in {@code checkerboardTextures} in compiled form.
	 * <p>
	 * If {@code checkerboardTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toCheckerboardTextures(checkerboardTextures, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param checkerboardTextures a {@code List} of {@code CheckerboardTexture} instances
	 * @return a {@code float[]} with all {@code CheckerboardTexture} instances in {@code checkerboardTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toCheckerboardTextures(final List<CheckerboardTexture> checkerboardTextures) {
		return toCheckerboardTextures(checkerboardTextures, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link CheckerboardTexture} instances in {@code checkerboardTextures} in compiled form.
	 * <p>
	 * If either {@code checkerboardTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardTextures a {@code List} of {@code CheckerboardTexture} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with all {@code CheckerboardTexture} instances in {@code checkerboardTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code checkerboardTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toCheckerboardTextures(final List<CheckerboardTexture> checkerboardTextures, final ToIntFunction<Texture> textureOffsetFunction) {
		return Floats.toArray(checkerboardTextures, checkerboardTexture -> toCheckerboardTexture(checkerboardTexture, textureOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code constantTexture} in compiled form.
	 * <p>
	 * If {@code constantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param constantTexture a {@link ConstantTexture} instance
	 * @return a {@code float[]} with {@code constantTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code constantTexture} is {@code null}
	 */
	public static float[] toConstantTexture(final ConstantTexture constantTexture) {
		final Color3F color = constantTexture.getColor();
		
		final float[] array = new float[CONSTANT_TEXTURE_LENGTH];
		
//		Because the ConstantTexture occupy 4/8 positions in a block, it should be aligned.
		array[CONSTANT_TEXTURE_OFFSET_COLOR + 0] = color.getR();
		array[CONSTANT_TEXTURE_OFFSET_COLOR + 1] = color.getG();
		array[CONSTANT_TEXTURE_OFFSET_COLOR + 2] = color.getB();
		array[3] = 0.0F;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link ConstantTexture} instances in {@code constantTextures} in compiled form.
	 * <p>
	 * If {@code constantTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param constantTextures a {@code List} of {@code ConstantTexture} instances
	 * @return a {@code float[]} with all {@code ConstantTexture} instances in {@code constantTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code constantTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toConstantTextures(final List<ConstantTexture> constantTextures) {
		return Floats.toArray(constantTextures, constantTexture -> toConstantTexture(constantTexture));
	}
	
	/**
	 * Returns a {@code float[]} with {@code lDRImageTexture} in compiled form.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance
	 * @return a {@code float[]} with {@code lDRImageTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public static float[] toLDRImageTexture(final LDRImageTexture lDRImageTexture) {
		final AngleF angle = lDRImageTexture.getAngle();
		
		final Vector2F scale = lDRImageTexture.getScale();
		
		final int resolutionX = lDRImageTexture.getResolutionX();
		final int resolutionY = lDRImageTexture.getResolutionY();
		
		final int[] image = lDRImageTexture.getImage();
		
		final float[] array = new float[getLDRImageTextureLength(lDRImageTexture)];
		
		array[L_D_R_IMAGE_TEXTURE_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[L_D_R_IMAGE_TEXTURE_OFFSET_SCALE + 0] = scale.getU();
		array[L_D_R_IMAGE_TEXTURE_OFFSET_SCALE + 1] = scale.getV();
		array[L_D_R_IMAGE_TEXTURE_OFFSET_RESOLUTION_X] = resolutionX;
		array[L_D_R_IMAGE_TEXTURE_OFFSET_RESOLUTION_Y] = resolutionY;
		
		for(int i = 0; i < image.length; i++) {
			array[L_D_R_IMAGE_TEXTURE_OFFSET_IMAGE + i] = image[i];
		}
		
		for(int i = L_D_R_IMAGE_TEXTURE_OFFSET_IMAGE + image.length; i < array.length; i++) {
			array[i] = 0.0F;
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link LDRImageTexture} instances in {@code lDRImageTextures} in compiled form.
	 * <p>
	 * If {@code lDRImageTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTextures a {@code List} of {@code LDRImageTexture} instances
	 * @return a {@code float[]} with all {@code LDRImageTexture} instances in {@code lDRImageTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toLDRImageTextures(final List<LDRImageTexture> lDRImageTextures) {
		return Floats.toArray(lDRImageTextures, lDRImageTexture -> toLDRImageTexture(lDRImageTexture));
	}
	
	/**
	 * Returns a {@code float[]} with {@code marbleTexture} in compiled form.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance
	 * @return a {@code float[]} with {@code marbleTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public static float[] toMarbleTexture(final MarbleTexture marbleTexture) {
		final Color3F colorA = marbleTexture.getColorA();
		final Color3F colorB = marbleTexture.getColorB();
		final Color3F colorC = marbleTexture.getColorC();
		
		final float frequency = marbleTexture.getFrequency();
		final float scale = marbleTexture.getScale();
		final float stripes = marbleTexture.getStripes();
		
		final int octaves = marbleTexture.getOctaves();
		
		final float[] array = new float[MARBLE_TEXTURE_LENGTH];
		
//		Because the MarbleTexture occupy 8/8 positions in a block, it should be aligned.
		array[MARBLE_TEXTURE_OFFSET_COLOR_A] = colorA.pack();	//Block #1
		array[MARBLE_TEXTURE_OFFSET_COLOR_B] = colorB.pack();	//Block #1
		array[MARBLE_TEXTURE_OFFSET_COLOR_C] = colorC.pack();	//Block #1
		array[MARBLE_TEXTURE_OFFSET_FREQUENCY] = frequency;		//Block #1
		array[MARBLE_TEXTURE_OFFSET_SCALE] = scale;				//Block #1
		array[MARBLE_TEXTURE_OFFSET_STRIPES] = stripes;			//Block #1
		array[MARBLE_TEXTURE_OFFSET_OCTAVES] = octaves;			//Block #1
		array[7] = 0.0F;										//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link MarbleTexture} instances in {@code marbleTextures} in compiled form.
	 * <p>
	 * If {@code marbleTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param marbleTextures a {@code List} of {@code MarbleTexture} instances
	 * @return a {@code float[]} with all {@code MarbleTexture} instances in {@code marbleTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code marbleTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toMarbleTextures(final List<MarbleTexture> marbleTextures) {
		return Floats.toArray(marbleTextures, marbleTexture -> toMarbleTexture(marbleTexture));
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotTexture} in compiled form.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toPolkaDotTexture(polkaDotTexture, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance
	 * @return a {@code float[]} with {@code polkaDotTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public static float[] toPolkaDotTexture(final PolkaDotTexture polkaDotTexture) {
		return toPolkaDotTexture(polkaDotTexture, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code polkaDotTexture} in compiled form.
	 * <p>
	 * If either {@code polkaDotTexture} or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with {@code polkaDotTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code polkaDotTexture} or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toPolkaDotTexture(final PolkaDotTexture polkaDotTexture, final ToIntFunction<Texture> textureOffsetFunction) {
		final AngleF angle = polkaDotTexture.getAngle();
		
		final Texture textureA = polkaDotTexture.getTextureA();
		final Texture textureB = polkaDotTexture.getTextureB();
		
		final float cellResolution = polkaDotTexture.getCellResolution();
		final float polkaDotRadius = polkaDotTexture.getPolkaDotRadius();
		
		final int textureAValue = pack(textureA.getID(), textureOffsetFunction.applyAsInt(textureA));
		final int textureBValue = pack(textureB.getID(), textureOffsetFunction.applyAsInt(textureB));
		
		final float[] array = new float[POLKA_DOT_TEXTURE_LENGTH];
		
//		Because the PolkaDotTexture occupy 8/8 positions in a block, it should be aligned.
		array[POLKA_DOT_TEXTURE_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_TEXTURE_A] = textureAValue;			//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_TEXTURE_B] = textureBValue;			//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_CELL_RESOLUTION] = cellResolution;	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_POLKA_DOT_RADIUS] = polkaDotRadius;	//Block #1
		array[6] = 0.0F;													//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PolkaDotTexture} instances in {@code polkaDotTextures} in compiled form.
	 * <p>
	 * If {@code polkaDotTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledTextureCache.toPolkaDotTextures(polkaDotTextures, texture -> 0);
	 * }
	 * </pre>
	 * 
	 * @param polkaDotTextures a {@code List} of {@code PolkaDotTexture} instances
	 * @return a {@code float[]} with all {@code PolkaDotTexture} instances in {@code polkaDotTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toPolkaDotTextures(final List<PolkaDotTexture> polkaDotTextures) {
		return toPolkaDotTextures(polkaDotTextures, texture -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PolkaDotTexture} instances in {@code polkaDotTextures} in compiled form.
	 * <p>
	 * If either {@code polkaDotTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotTextures a {@code List} of {@code PolkaDotTexture} instances
	 * @param textureOffsetFunction a {@code ToIntFunction} that returns {@link Texture} offsets
	 * @return a {@code float[]} with all {@code PolkaDotTexture} instances in {@code polkaDotTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code polkaDotTextures}, at least one of its elements or {@code textureOffsetFunction} are {@code null}
	 */
	public static float[] toPolkaDotTextures(final List<PolkaDotTexture> polkaDotTextures, final ToIntFunction<Texture> textureOffsetFunction) {
		return Floats.toArray(polkaDotTextures, polkaDotTexture -> toPolkaDotTexture(polkaDotTexture, textureOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with {@code simplexFractionalBrownianMotionTexture} in compiled form.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance
	 * @return a {@code float[]} with {@code simplexFractionalBrownianMotionTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public static float[] toSimplexFractionalBrownianMotionTexture(final SimplexFractionalBrownianMotionTexture simplexFractionalBrownianMotionTexture) {
		final Color3F color = simplexFractionalBrownianMotionTexture.getColor();
		
		final float frequency = simplexFractionalBrownianMotionTexture.getFrequency();
		final float gain = simplexFractionalBrownianMotionTexture.getGain();
		
		final int octaves = simplexFractionalBrownianMotionTexture.getOctaves();
		
		final float[] array = new float[SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH];
		
//		Because the SimplexFractionalBrownianMotionTexture occupy 4/8 positions in a block, it should be aligned.
		array[SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_COLOR] = color.pack();	//Block #1
		array[SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_FREQUENCY] = frequency;	//Block #1
		array[SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_GAIN] = gain;			//Block #1
		array[SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_OFFSET_OCTAVES] = octaves;		//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link SimplexFractionalBrownianMotionTexture} instances in {@code simplexFractionalBrownianMotionTextures} in compiled form.
	 * <p>
	 * If {@code simplexFractionalBrownianMotionTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param simplexFractionalBrownianMotionTextures a {@code List} of {@code SimplexFractionalBrownianMotionTexture} instances
	 * @return a {@code float[]} with all {@code SimplexFractionalBrownianMotionTexture} instances in {@code simplexFractionalBrownianMotionTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code simplexFractionalBrownianMotionTextures} or at least one of its elements are {@code null}
	 */
	public static float[] toSimplexFractionalBrownianMotionTextures(final List<SimplexFractionalBrownianMotionTexture> simplexFractionalBrownianMotionTextures) {
		return Floats.toArray(simplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture -> toSimplexFractionalBrownianMotionTexture(simplexFractionalBrownianMotionTexture));
	}
	
	/**
	 * Returns the length of {@code lDRImageTexture} in compiled form.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance
	 * @return the length of {@code lDRImageTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public static int getLDRImageTextureLength(final LDRImageTexture lDRImageTexture) {
		final int a = 5;
		final int b = lDRImageTexture.getResolution();
		final int c = padding(a + b);
		
		return a + b + c;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link LDRImageTexture} instances in {@code lDRImageTextures} in compiled form.
	 * <p>
	 * If {@code lDRImageTextures} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTextures a {@code List} of {@code LDRImageTexture} instances
	 * @return an {@code int[]} with the offsets for all {@code LDRImageTexture} instances in {@code lDRImageTextures} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTextures} or at least one of its elements are {@code null}
	 */
	public static int[] toLDRImageTextureOffsets(final List<LDRImageTexture> lDRImageTextures) {
		ParameterArguments.requireNonNullList(lDRImageTextures, "lDRImageTextures");
		
		final int[] lDRImageTextureOffsets = new int[lDRImageTextures.size()];
		
		for(int i = 0, j = 0; i < lDRImageTextures.size(); j += getLDRImageTextureLength(lDRImageTextures.get(i)), i++) {
			lDRImageTextureOffsets[i] = j;
		}
		
		return lDRImageTextureOffsets;
	}
}