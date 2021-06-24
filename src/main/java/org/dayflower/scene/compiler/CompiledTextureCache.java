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

import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.LDRImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.Texture;

/**
 * A {@code CompiledTextureCache} contains {@link Texture} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledTextureCache {
	private float[] textureBlendTextureArray;
	private float[] textureBullseyeTextureArray;
	private float[] textureCheckerboardTextureArray;
	private float[] textureConstantTextureArray;
	private float[] textureLDRImageTextureArray;
	private float[] textureMarbleTextureArray;
	private float[] textureSimplexFractionalBrownianMotionTextureArray;
	private int[] textureLDRImageTextureOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledTextureCache} instance.
	 */
	public CompiledTextureCache() {
		setTextureBlendTextureArray(new float[1]);
		setTextureBullseyeTextureArray(new float[1]);
		setTextureCheckerboardTextureArray(new float[1]);
		setTextureConstantTextureArray(new float[1]);
		setTextureLDRImageTextureArray(new float[1]);
		setTextureLDRImageTextureOffsetArray(new int[1]);
		setTextureMarbleTextureArray(new float[1]);
		setTextureSimplexFractionalBrownianMotionTextureArray(new float[1]);
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
		return Structures.getStructureCount(this.textureBlendTextureArray, BlendTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureBlendTextureArray, Objects.requireNonNull(textureBlendTexture, "textureBlendTexture == null"), getTextureBlendTextureCount(), BlendTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureBlendTextureArray, Objects.requireNonNull(textureBlendTexture, "textureBlendTexture == null"), getTextureBlendTextureCount(), BlendTexture.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link BullseyeTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code BullseyeTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureBullseyeTextureCount() {
		return Structures.getStructureCount(this.textureBullseyeTextureArray, BullseyeTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureBullseyeTextureArray, Objects.requireNonNull(textureBullseyeTexture, "textureBullseyeTexture == null"), getTextureBullseyeTextureCount(), BullseyeTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureBullseyeTextureArray, Objects.requireNonNull(textureBullseyeTexture, "textureBullseyeTexture == null"), getTextureBullseyeTextureCount(), BullseyeTexture.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link CheckerboardTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code CheckerboardTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureCheckerboardTextureCount() {
		return Structures.getStructureCount(this.textureCheckerboardTextureArray, CheckerboardTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureCheckerboardTextureArray, Objects.requireNonNull(textureCheckerboardTexture, "textureCheckerboardTexture == null"), getTextureCheckerboardTextureCount(), CheckerboardTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureCheckerboardTextureArray, Objects.requireNonNull(textureCheckerboardTexture, "textureCheckerboardTexture == null"), getTextureCheckerboardTextureCount(), CheckerboardTexture.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link ConstantTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code ConstantTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureConstantTextureCount() {
		return Structures.getStructureCount(this.textureConstantTextureArray, ConstantTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureConstantTextureArray, Objects.requireNonNull(textureConstantTexture, "textureConstantTexture == null"), getTextureConstantTextureCount(), ConstantTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureConstantTextureArray, Objects.requireNonNull(textureConstantTexture, "textureConstantTexture == null"), getTextureConstantTextureCount(), ConstantTexture.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link MarbleTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code MarbleTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureMarbleTextureCount() {
		return Structures.getStructureCount(this.textureMarbleTextureArray, MarbleTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureMarbleTextureArray, Objects.requireNonNull(textureMarbleTexture, "textureMarbleTexture == null"), getTextureMarbleTextureCount(), MarbleTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureMarbleTextureArray, Objects.requireNonNull(textureMarbleTexture, "textureMarbleTexture == null"), getTextureMarbleTextureCount(), MarbleTexture.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance.
	 * 
	 * @return the {@code SimplexFractionalBrownianMotionTexture} count in this {@code CompiledTextureCache} instance
	 */
	public int getTextureSimplexFractionalBrownianMotionTextureCount() {
		return Structures.getStructureCount(this.textureSimplexFractionalBrownianMotionTextureArray, SimplexFractionalBrownianMotionTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetAbsolute(this.textureSimplexFractionalBrownianMotionTextureArray, Objects.requireNonNull(textureSimplexFractionalBrownianMotionTexture, "textureSimplexFractionalBrownianMotionTexture == null"), getTextureSimplexFractionalBrownianMotionTextureCount(), SimplexFractionalBrownianMotionTexture.ARRAY_LENGTH);
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
		return Structures.getStructureOffsetRelative(this.textureSimplexFractionalBrownianMotionTextureArray, Objects.requireNonNull(textureSimplexFractionalBrownianMotionTexture, "textureSimplexFractionalBrownianMotionTexture == null"), getTextureSimplexFractionalBrownianMotionTextureCount(), SimplexFractionalBrownianMotionTexture.ARRAY_LENGTH);
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
}