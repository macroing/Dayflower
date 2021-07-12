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

import static org.dayflower.utility.Ints.padding;

import java.util.Objects;

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
	
	private float[] textureBlendTextureArray;
	private float[] textureBullseyeTextureArray;
	private float[] textureCheckerboardTextureArray;
	private float[] textureConstantTextureArray;
	private float[] textureLDRImageTextureArray;
	private float[] textureMarbleTextureArray;
	private float[] texturePolkaDotTextureArray;
	private float[] textureSimplexFractionalBrownianMotionTextureArray;
	private int[] textureLDRImageTextureOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledTextureCache} instance.
	 */
	public CompiledTextureCache() {
		setTextureBlendTextureArray(new float[0]);
		setTextureBullseyeTextureArray(new float[0]);
		setTextureCheckerboardTextureArray(new float[0]);
		setTextureConstantTextureArray(new float[0]);
		setTextureLDRImageTextureArray(new float[0]);
		setTextureLDRImageTextureOffsetArray(new int[0]);
		setTextureMarbleTextureArray(new float[0]);
		setTexturePolkaDotTextureArray(new float[0]);
		setTextureSimplexFractionalBrownianMotionTextureArray(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link BlendTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BlendTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureBlendTextureArray() {
		return this.textureBlendTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BullseyeTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BullseyeTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureBullseyeTextureArray() {
		return this.textureBullseyeTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code CheckerboardTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureCheckerboardTextureArray() {
		return this.textureCheckerboardTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link ConstantTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code ConstantTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureConstantTextureArray() {
		return this.textureConstantTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureLDRImageTextureArray() {
		return this.textureLDRImageTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link MarbleTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code MarbleTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureMarbleTextureArray() {
		return this.textureMarbleTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PolkaDotTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PolkaDotTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTexturePolkaDotTextureArray() {
		return this.texturePolkaDotTextureArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SimplexFractionalBrownianMotionTexture} instances in compiled form that are associated with this {@code CompiledTextureCache} instance
	 */
	public float[] getTextureSimplexFractionalBrownianMotionTextureArray() {
		return this.textureSimplexFractionalBrownianMotionTextureArray;
	}
	
	/**
	 * Returns the {@link BlendTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code BlendTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureBlendTextureCount() {
		return Structures.getStructureCount(this.textureBlendTextureArray, BLEND_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureBlendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureBlendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBlendTexture a {@link BlendTexture} instance in compiled form
	 * @return the absolute offset of {@code textureBlendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureBlendTexture} is {@code null}
	 */
	public int getTextureBlendTextureOffsetAbsolute(final float[] textureBlendTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureBlendTextureArray, Objects.requireNonNull(textureBlendTexture, "textureBlendTexture == null"), getTextureBlendTextureCount(), BLEND_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureBlendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureBlendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBlendTexture a {@link BlendTexture} instance in compiled form
	 * @return the relative offset of {@code textureBlendTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureBlendTexture} is {@code null}
	 */
	public int getTextureBlendTextureOffsetRelative(final float[] textureBlendTexture) {
		return Structures.getStructureOffsetRelative(this.textureBlendTextureArray, Objects.requireNonNull(textureBlendTexture, "textureBlendTexture == null"), getTextureBlendTextureCount(), BLEND_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link BullseyeTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code BullseyeTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureBullseyeTextureCount() {
		return Structures.getStructureCount(this.textureBullseyeTextureArray, BULLSEYE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureBullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureBullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return the absolute offset of {@code textureBullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureBullseyeTexture} is {@code null}
	 */
	public int getTextureBullseyeTextureOffsetAbsolute(final float[] textureBullseyeTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureBullseyeTextureArray, Objects.requireNonNull(textureBullseyeTexture, "textureBullseyeTexture == null"), getTextureBullseyeTextureCount(), BULLSEYE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureBullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureBullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBullseyeTexture a {@link BullseyeTexture} instance in compiled form
	 * @return the relative offset of {@code textureBullseyeTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureBullseyeTexture} is {@code null}
	 */
	public int getTextureBullseyeTextureOffsetRelative(final float[] textureBullseyeTexture) {
		return Structures.getStructureOffsetRelative(this.textureBullseyeTextureArray, Objects.requireNonNull(textureBullseyeTexture, "textureBullseyeTexture == null"), getTextureBullseyeTextureCount(), BULLSEYE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link CheckerboardTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code CheckerboardTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureCheckerboardTextureCount() {
		return Structures.getStructureCount(this.textureCheckerboardTextureArray, CHECKERBOARD_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureCheckerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureCheckerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCheckerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return the absolute offset of {@code textureCheckerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureCheckerboardTexture} is {@code null}
	 */
	public int getTextureCheckerboardTextureOffsetAbsolute(final float[] textureCheckerboardTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureCheckerboardTextureArray, Objects.requireNonNull(textureCheckerboardTexture, "textureCheckerboardTexture == null"), getTextureCheckerboardTextureCount(), CHECKERBOARD_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureCheckerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureCheckerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCheckerboardTexture a {@link CheckerboardTexture} instance in compiled form
	 * @return the relative offset of {@code textureCheckerboardTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureCheckerboardTexture} is {@code null}
	 */
	public int getTextureCheckerboardTextureOffsetRelative(final float[] textureCheckerboardTexture) {
		return Structures.getStructureOffsetRelative(this.textureCheckerboardTextureArray, Objects.requireNonNull(textureCheckerboardTexture, "textureCheckerboardTexture == null"), getTextureCheckerboardTextureCount(), CHECKERBOARD_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link ConstantTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code ConstantTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureConstantTextureCount() {
		return Structures.getStructureCount(this.textureConstantTextureArray, CONSTANT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureConstantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureConstantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureConstantTexture a {@link ConstantTexture} instance in compiled form
	 * @return the absolute offset of {@code textureConstantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureConstantTexture} is {@code null}
	 */
	public int getTextureConstantTextureOffsetAbsolute(final float[] textureConstantTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureConstantTextureArray, Objects.requireNonNull(textureConstantTexture, "textureConstantTexture == null"), getTextureConstantTextureCount(), CONSTANT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureConstantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureConstantTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureConstantTexture a {@link ConstantTexture} instance in compiled form
	 * @return the relative offset of {@code textureConstantTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureConstantTexture} is {@code null}
	 */
	public int getTextureConstantTextureOffsetRelative(final float[] textureConstantTexture) {
		return Structures.getStructureOffsetRelative(this.textureConstantTextureArray, Objects.requireNonNull(textureConstantTexture, "textureConstantTexture == null"), getTextureConstantTextureCount(), CONSTANT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link MarbleTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code MarbleTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureMarbleTextureCount() {
		return Structures.getStructureCount(this.textureMarbleTextureArray, MARBLE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureMarbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureMarbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureMarbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return the absolute offset of {@code textureMarbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureMarbleTexture} is {@code null}
	 */
	public int getTextureMarbleTextureOffsetAbsolute(final float[] textureMarbleTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureMarbleTextureArray, Objects.requireNonNull(textureMarbleTexture, "textureMarbleTexture == null"), getTextureMarbleTextureCount(), MARBLE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureMarbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureMarbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureMarbleTexture a {@link MarbleTexture} instance in compiled form
	 * @return the relative offset of {@code textureMarbleTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureMarbleTexture} is {@code null}
	 */
	public int getTextureMarbleTextureOffsetRelative(final float[] textureMarbleTexture) {
		return Structures.getStructureOffsetRelative(this.textureMarbleTextureArray, Objects.requireNonNull(textureMarbleTexture, "textureMarbleTexture == null"), getTextureMarbleTextureCount(), MARBLE_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link PolkaDotTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code PolkaDotTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTexturePolkaDotTextureCount() {
		return Structures.getStructureCount(this.texturePolkaDotTextureArray, POLKA_DOT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code texturePolkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code texturePolkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param texturePolkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return the absolute offset of {@code texturePolkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code texturePolkaDotTexture} is {@code null}
	 */
	public int getTexturePolkaDotTextureOffsetAbsolute(final float[] texturePolkaDotTexture) {
		return Structures.getStructureOffsetAbsolute(this.texturePolkaDotTextureArray, Objects.requireNonNull(texturePolkaDotTexture, "texturePolkaDotTexture == null"), getTexturePolkaDotTextureCount(), POLKA_DOT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code texturePolkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code texturePolkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param texturePolkaDotTexture a {@link PolkaDotTexture} instance in compiled form
	 * @return the relative offset of {@code texturePolkaDotTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code texturePolkaDotTexture} is {@code null}
	 */
	public int getTexturePolkaDotTextureOffsetRelative(final float[] texturePolkaDotTexture) {
		return Structures.getStructureOffsetRelative(this.texturePolkaDotTextureArray, Objects.requireNonNull(texturePolkaDotTexture, "texturePolkaDotTexture == null"), getTexturePolkaDotTextureCount(), POLKA_DOT_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the {@link SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureSimplexFractionalBrownianMotionTextureCount() {
		return Structures.getStructureCount(this.textureSimplexFractionalBrownianMotionTextureArray, SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code textureSimplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureSimplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureSimplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return the absolute offset of {@code textureSimplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureSimplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public int getTextureSimplexFractionalBrownianMotionTextureOffsetAbsolute(final float[] textureSimplexFractionalBrownianMotionTexture) {
		return Structures.getStructureOffsetAbsolute(this.textureSimplexFractionalBrownianMotionTextureArray, Objects.requireNonNull(textureSimplexFractionalBrownianMotionTexture, "textureSimplexFractionalBrownianMotionTexture == null"), getTextureSimplexFractionalBrownianMotionTextureCount(), SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code textureSimplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code textureSimplexFractionalBrownianMotionTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureSimplexFractionalBrownianMotionTexture a {@link SimplexFractionalBrownianMotionTexture} instance in compiled form
	 * @return the relative offset of {@code textureSimplexFractionalBrownianMotionTexture} in this {@code CompiledTextureCache} instance, or {@code -1} if it cannot be found
	 * @throws NullPointerException thrown if, and only if, {@code textureSimplexFractionalBrownianMotionTexture} is {@code null}
	 */
	public int getTextureSimplexFractionalBrownianMotionTextureOffsetRelative(final float[] textureSimplexFractionalBrownianMotionTexture) {
		return Structures.getStructureOffsetRelative(this.textureSimplexFractionalBrownianMotionTextureArray, Objects.requireNonNull(textureSimplexFractionalBrownianMotionTexture, "textureSimplexFractionalBrownianMotionTexture == null"), getTextureSimplexFractionalBrownianMotionTextureCount(), SIMPLEX_FRACTIONAL_BROWNIAN_MOTION_TEXTURE_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances in this {@code CompiledTextureCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances in this {@code CompiledTextureCache} instance
	 */
	public int[] getTextureLDRImageTextureOffsetArray() {
		return this.textureLDRImageTextureOffsetArray;
	}
	
	/**
	 * Sets all {@link BlendTexture} instances in compiled form to {@code textureBlendTextureArray}.
	 * <p>
	 * If {@code textureBlendTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBlendTextureArray the {@code BlendTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureBlendTextureArray} is {@code null}
	 */
	public void setTextureBlendTextureArray(final float[] textureBlendTextureArray) {
		this.textureBlendTextureArray = Objects.requireNonNull(textureBlendTextureArray, "textureBlendTextureArray == null");
	}
	
	/**
	 * Sets all {@link BullseyeTexture} instances in compiled form to {@code textureBullseyeTextureArray}.
	 * <p>
	 * If {@code textureBullseyeTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureBullseyeTextureArray the {@code BullseyeTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureBullseyeTextureArray} is {@code null}
	 */
	public void setTextureBullseyeTextureArray(final float[] textureBullseyeTextureArray) {
		this.textureBullseyeTextureArray = Objects.requireNonNull(textureBullseyeTextureArray, "textureBullseyeTextureArray == null");
	}
	
	/**
	 * Sets all {@link CheckerboardTexture} instances in compiled form to {@code textureCheckerboardTextureArray}.
	 * <p>
	 * If {@code textureCheckerboardTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureCheckerboardTextureArray the {@code CheckerboardTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureCheckerboardTextureArray} is {@code null}
	 */
	public void setTextureCheckerboardTextureArray(final float[] textureCheckerboardTextureArray) {
		this.textureCheckerboardTextureArray = Objects.requireNonNull(textureCheckerboardTextureArray, "textureCheckerboardTextureArray == null");
	}
	
	/**
	 * Sets all {@link ConstantTexture} instances in compiled form to {@code textureConstantTextureArray}.
	 * <p>
	 * If {@code textureConstantTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureConstantTextureArray the {@code ConstantTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureConstantTextureArray} is {@code null}
	 */
	public void setTextureConstantTextureArray(final float[] textureConstantTextureArray) {
		this.textureConstantTextureArray = Objects.requireNonNull(textureConstantTextureArray, "textureConstantTextureArray == null");
	}
	
	/**
	 * Sets all {@link LDRImageTexture} instances in compiled form to {@code textureLDRImageTextureArray}.
	 * <p>
	 * If {@code textureLDRImageTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureLDRImageTextureArray the {@code LDRImageTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureLDRImageTextureArray} is {@code null}
	 */
	public void setTextureLDRImageTextureArray(final float[] textureLDRImageTextureArray) {
		this.textureLDRImageTextureArray = Objects.requireNonNull(textureLDRImageTextureArray, "textureLDRImageTextureArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageTexture} instances to {@code textureLDRImageTextureOffsetArray}.
	 * <p>
	 * If {@code textureLDRImageTextureOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureLDRImageTextureOffsetArray the {@code int[]} that contains the offsets for all {@code LDRImageTexture} instances
	 * @throws NullPointerException thrown if, and only if, {@code textureLDRImageTextureOffsetArray} is {@code null}
	 */
	public void setTextureLDRImageTextureOffsetArray(final int[] textureLDRImageTextureOffsetArray) {
		this.textureLDRImageTextureOffsetArray = Objects.requireNonNull(textureLDRImageTextureOffsetArray, "textureLDRImageTextureOffsetArray == null");
	}
	
	/**
	 * Sets all {@link MarbleTexture} instances in compiled form to {@code textureMarbleTextureArray}.
	 * <p>
	 * If {@code textureMarbleTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureMarbleTextureArray the {@code MarbleTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureMarbleTextureArray} is {@code null}
	 */
	public void setTextureMarbleTextureArray(final float[] textureMarbleTextureArray) {
		this.textureMarbleTextureArray = Objects.requireNonNull(textureMarbleTextureArray, "textureMarbleTextureArray == null");
	}
	
	/**
	 * Sets all {@link PolkaDotTexture} instances in compiled form to {@code texturePolkaDotTextureArray}.
	 * <p>
	 * If {@code texturePolkaDotTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param texturePolkaDotTextureArray the {@code PolkaDotTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code texturePolkaDotTextureArray} is {@code null}
	 */
	public void setTexturePolkaDotTextureArray(final float[] texturePolkaDotTextureArray) {
		this.texturePolkaDotTextureArray = Objects.requireNonNull(texturePolkaDotTextureArray, "texturePolkaDotTextureArray == null");
	}
	
	/**
	 * Sets all {@link SimplexFractionalBrownianMotionTexture} instances in compiled form to {@code textureSimplexFractionalBrownianMotionTextureArray}.
	 * <p>
	 * If {@code textureSimplexFractionalBrownianMotionTextureArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param textureSimplexFractionalBrownianMotionTextureArray the {@code SimplexFractionalBrownianMotionTexture} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code textureSimplexFractionalBrownianMotionTextureArray} is {@code null}
	 */
	public void setTextureSimplexFractionalBrownianMotionTextureArray(final float[] textureSimplexFractionalBrownianMotionTextureArray) {
		this.textureSimplexFractionalBrownianMotionTextureArray = Objects.requireNonNull(textureSimplexFractionalBrownianMotionTextureArray, "textureSimplexFractionalBrownianMotionTextureArray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code blendTexture} in compiled form.
	 * <p>
	 * If {@code blendTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param blendTexture a {@link BlendTexture} instance
	 * @return a {@code float[]} with {@code blendTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code blendTexture} is {@code null}
	 */
	public static float[] toArray(final BlendTexture blendTexture) {
		final Texture textureA = blendTexture.getTextureA();
		final Texture textureB = blendTexture.getTextureB();
		
		final float tComponent1 = blendTexture.getTComponent1();
		final float tComponent2 = blendTexture.getTComponent2();
		final float tComponent3 = blendTexture.getTComponent3();
		
		final float[] array = new float[BLEND_TEXTURE_LENGTH];
		
//		Because the BlendTexture occupy 8/8 positions in a block, it should be aligned.
		array[BLEND_TEXTURE_OFFSET_TEXTURE_A] = textureA.getID();	//Block #1
		array[BLEND_TEXTURE_OFFSET_TEXTURE_B] = textureB.getID();	//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_1] = tComponent1;	//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_2] = tComponent2;	//Block #1
		array[BLEND_TEXTURE_OFFSET_T_COMPONENT_3] = tComponent3;	//Block #1
		array[5] = 0.0F;											//Block #1
		array[6] = 0.0F;											//Block #1
		array[7] = 0.0F;											//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code bullseyeTexture} in compiled form.
	 * <p>
	 * If {@code bullseyeTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bullseyeTexture a {@link BullseyeTexture} instance
	 * @return a {@code float[]} with {@code bullseyeTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code bullseyeTexture} is {@code null}
	 */
	public static float[] toArray(final BullseyeTexture bullseyeTexture) {
		final Point3F origin = bullseyeTexture.getOrigin();
		
		final Texture textureA = bullseyeTexture.getTextureA();
		final Texture textureB = bullseyeTexture.getTextureB();
		
		final float scale = bullseyeTexture.getScale();
		
		final float[] array = new float[BULLSEYE_TEXTURE_LENGTH];
		
//		Because the BullseyeTexture occupy 8/8 positions in a block, it should be aligned.
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 0] = origin.getX();		//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 1] = origin.getY();		//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_ORIGIN + 2] = origin.getZ();		//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_TEXTURE_A] = textureA.getID();	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_TEXTURE_B] = textureB.getID();	//Block #1
		array[BULLSEYE_TEXTURE_OFFSET_SCALE] = scale;					//Block #1
		array[6] = 0.0F;												//Block #1
		array[7] = 0.0F;												//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code checkerboardTexture} in compiled form.
	 * <p>
	 * If {@code checkerboardTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param checkerboardTexture a {@link CheckerboardTexture} instance
	 * @return a {@code float[]} with {@code checkerboardTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code checkerboardTexture} is {@code null}
	 */
	public static float[] toArray(final CheckerboardTexture checkerboardTexture) {
		final AngleF angle = checkerboardTexture.getAngle();
		
		final Texture textureA = checkerboardTexture.getTextureA();
		final Texture textureB = checkerboardTexture.getTextureB();
		
		final Vector2F scale = checkerboardTexture.getScale();
		
		final float[] array = new float[CHECKERBOARD_TEXTURE_LENGTH];
		
//		Because the CheckerboardTexture occupy 8/8 positions in a block, it should be aligned.
		array[CHECKERBOARD_TEXTURE_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_A] = textureA.getID();		//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_TEXTURE_B] = textureB.getID();		//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_SCALE + 0] = scale.getX();			//Block #1
		array[CHECKERBOARD_TEXTURE_OFFSET_SCALE + 1] = scale.getY();			//Block #1
		array[6] = 0.0F;														//Block #1
		array[7] = 0.0F;														//Block #1
		
		return array;
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
	public static float[] toArray(final ConstantTexture constantTexture) {
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
	 * Returns a {@code float[]} with {@code lDRImageTexture} in compiled form.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance
	 * @return a {@code float[]} with {@code lDRImageTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public static float[] toArray(final LDRImageTexture lDRImageTexture) {
		final AngleF angle = lDRImageTexture.getAngle();
		
		final Vector2F scale = lDRImageTexture.getScale();
		
		final int resolutionX = lDRImageTexture.getResolutionX();
		final int resolutionY = lDRImageTexture.getResolutionY();
		
		final int[] image = lDRImageTexture.getImage();
		
		final float[] array = new float[getLength(lDRImageTexture)];
		
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
	 * Returns a {@code float[]} with {@code marbleTexture} in compiled form.
	 * <p>
	 * If {@code marbleTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param marbleTexture a {@link MarbleTexture} instance
	 * @return a {@code float[]} with {@code marbleTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code marbleTexture} is {@code null}
	 */
	public static float[] toArray(final MarbleTexture marbleTexture) {
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
	 * Returns a {@code float[]} with {@code polkaDotTexture} in compiled form.
	 * <p>
	 * If {@code polkaDotTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param polkaDotTexture a {@link PolkaDotTexture} instance
	 * @return a {@code float[]} with {@code polkaDotTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code polkaDotTexture} is {@code null}
	 */
	public static float[] toArray(final PolkaDotTexture polkaDotTexture) {
		final AngleF angle = polkaDotTexture.getAngle();
		
		final Texture textureA = polkaDotTexture.getTextureA();
		final Texture textureB = polkaDotTexture.getTextureB();
		
		final float cellResolution = polkaDotTexture.getCellResolution();
		final float polkaDotRadius = polkaDotTexture.getPolkaDotRadius();
		
		final float[] array = new float[POLKA_DOT_TEXTURE_LENGTH];
		
//		Because the PolkaDotTexture occupy 8/8 positions in a block, it should be aligned.
		array[POLKA_DOT_TEXTURE_OFFSET_ANGLE_DEGREES] = angle.getDegrees();	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_ANGLE_RADIANS] = angle.getRadians();	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_TEXTURE_A] = textureA.getID();		//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_TEXTURE_B] = textureB.getID();		//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_CELL_RESOLUTION] = cellResolution;	//Block #1
		array[POLKA_DOT_TEXTURE_OFFSET_POLKA_DOT_RADIUS] = polkaDotRadius;	//Block #1
		array[6] = 0.0F;													//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
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
	public static float[] toArray(final SimplexFractionalBrownianMotionTexture simplexFractionalBrownianMotionTexture) {
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
	 * Returns the length of {@code lDRImageTexture} in compiled form.
	 * <p>
	 * If {@code lDRImageTexture} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageTexture an {@link LDRImageTexture} instance
	 * @return the length of {@code lDRImageTexture} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageTexture} is {@code null}
	 */
	public static int getLength(final LDRImageTexture lDRImageTexture) {
		final int a = 5;
		final int b = lDRImageTexture.getResolution();
		final int c = padding(a + b);
		
		return a + b + c;
	}
}