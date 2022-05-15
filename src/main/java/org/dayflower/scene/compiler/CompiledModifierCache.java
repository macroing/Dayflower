/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.NormalMapLDRImageModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;
import org.dayflower.utility.Document;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

/**
 * A {@code CompiledModifierCache} contains {@link Modifier} instances in compiled form.
 * <p>
 * The {@code Modifier} implementations that are supported are the following:
 * <ul>
 * <li>{@link NoOpModifier}</li>
 * <li>{@link NormalMapLDRImageModifier}</li>
 * <li>{@link SimplexNoiseNormalMapModifier}</li>
 * </ul>
 * <p>
 * The {@code NoOpModifier} implementation only requires an ID. It does not have its own compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledModifierCache {
	/**
	 * The offset for the angle in radians in a compiled {@link NormalMapLDRImageModifier} instance.
	 */
	public static final int NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_ANGLE_RADIANS = 0;
	
	/**
	 * The offset for the image in a compiled {@link NormalMapLDRImageModifier} instance.
	 */
	public static final int NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_IMAGE = 5;
	
	/**
	 * The offset for the resolution of the X-axis in a compiled {@link NormalMapLDRImageModifier} instance.
	 */
	public static final int NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_X = 3;
	
	/**
	 * The offset for the resolution of the Y-axis in a compiled {@link NormalMapLDRImageModifier} instance.
	 */
	public static final int NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_Y = 4;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link NormalMapLDRImageModifier} instance.
	 */
	public static final int NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_SCALE = 1;
	
	/**
	 * The length of a compiled {@link SimplexNoiseNormalMapModifier} instance.
	 */
	public static final int SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH = 2;
	
	/**
	 * The offset for the frequency in a compiled {@link SimplexNoiseNormalMapModifier} instance.
	 */
	public static final int SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_FREQUENCY = 0;
	
	/**
	 * The offset for the scale in a compiled {@link SimplexNoiseNormalMapModifier} instance.
	 */
	public static final int SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_SCALE = 1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] normalMapLDRImageModifiers;
	private float[] simplexNoiseNormalMapModifiers;
	private int[] normalMapLDRImageModifierOffsets;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledModifierCache} instance.
	 */
	public CompiledModifierCache() {
		setNormalMapLDRImageModifierOffsets(new int[0]);
		setNormalMapLDRImageModifiers(new float[0]);
		setSimplexNoiseNormalMapModifiers(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code normalMapLDRImageModifier} from this {@code CompiledModifierCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code normalMapLDRImageModifier} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance in compiled form
	 * @return {@code true} if, and only if, {@code normalMapLDRImageModifier} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public boolean removeNormalMapLDRImageModifier(final float[] normalMapLDRImageModifier) {
		final int absoluteOffset = getNormalMapLDRImageModifierOffsetAbsolute(normalMapLDRImageModifier);
		
		if(absoluteOffset != -1) {
			setNormalMapLDRImageModifierOffsets(Structures.removeStructureOffset(getNormalMapLDRImageModifierOffsets(), absoluteOffset, normalMapLDRImageModifier.length));
			setNormalMapLDRImageModifiers(Arrays.splice(getNormalMapLDRImageModifiers(), absoluteOffset, normalMapLDRImageModifier.length));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code simplexNoiseNormalMapModifier} from this {@code CompiledModifierCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code simplexNoiseNormalMapModifier} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifier a {@link SimplexNoiseNormalMapModifier} instance in compiled form
	 * @return {@code true} if, and only if, {@code simplexNoiseNormalMapModifier} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifier} is {@code null}
	 */
	public boolean removeSimplexNoiseNormalMapModifier(final float[] simplexNoiseNormalMapModifier) {
		final int absoluteOffset = getSimplexNoiseNormalMapModifierOffsetAbsolute(simplexNoiseNormalMapModifier);
		
		if(absoluteOffset != -1) {
			setSimplexNoiseNormalMapModifiers(Arrays.splice(getSimplexNoiseNormalMapModifiers(), absoluteOffset, SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link NormalMapLDRImageModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code NormalMapLDRImageModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance
	 */
	public float[] getNormalMapLDRImageModifiers() {
		return this.normalMapLDRImageModifiers;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SimplexNoiseNormalMapModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SimplexNoiseNormalMapModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance
	 */
	public float[] getSimplexNoiseNormalMapModifiers() {
		return this.simplexNoiseNormalMapModifiers;
	}
	
	/**
	 * Adds {@code normalMapLDRImageModifier} to this {@code CompiledModifierCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code normalMapLDRImageModifier}.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance in compiled form
	 * @return the relative offset to {@code normalMapLDRImageModifier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public int addNormalMapLDRImageModifier(final float[] normalMapLDRImageModifier) {
		final int absoluteOffsetNew = this.normalMapLDRImageModifiers.length;
		final int relativeOffsetOld = getNormalMapLDRImageModifierOffsetRelative(normalMapLDRImageModifier);
		final int relativeOffsetNew = getNormalMapLDRImageModifierCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setNormalMapLDRImageModifierOffsets(Arrays.merge(getNormalMapLDRImageModifierOffsets(), new int[] {absoluteOffsetNew}));
		setNormalMapLDRImageModifiers(Arrays.merge(getNormalMapLDRImageModifiers(), normalMapLDRImageModifier));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code simplexNoiseNormalMapModifier} to this {@code CompiledModifierCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code simplexNoiseNormalMapModifier}.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifier a {@link SimplexNoiseNormalMapModifier} instance in compiled form
	 * @return the relative offset to {@code simplexNoiseNormalMapModifier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifier} is {@code null}
	 */
	public int addSimplexNoiseNormalMapModifier(final float[] simplexNoiseNormalMapModifier) {
		final int relativeOffsetOld = getSimplexNoiseNormalMapModifierOffsetRelative(simplexNoiseNormalMapModifier);
		final int relativeOffsetNew = getSimplexNoiseNormalMapModifierCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setSimplexNoiseNormalMapModifiers(Arrays.merge(getSimplexNoiseNormalMapModifiers(), simplexNoiseNormalMapModifier));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link NormalMapLDRImageModifier} count in this {@code CompiledModifierCache} instance.
	 * 
	 * @return the {@code NormalMapLDRImageModifier} count in this {@code CompiledModifierCache} instance
	 */
	public int getNormalMapLDRImageModifierCount() {
		return this.normalMapLDRImageModifierOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code normalMapLDRImageModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance in compiled form
	 * @return the absolute offset of {@code normalMapLDRImageModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public int getNormalMapLDRImageModifierOffsetAbsolute(final float[] normalMapLDRImageModifier) {
		Objects.requireNonNull(normalMapLDRImageModifier, "normalMapLDRImageModifier == null");
		
		ParameterArguments.requireExact(normalMapLDRImageModifier.length % 8, 0, "normalMapLDRImageModifier.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.normalMapLDRImageModifiers, normalMapLDRImageModifier, this.normalMapLDRImageModifierOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code normalMapLDRImageModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance in compiled form
	 * @return the relative offset of {@code normalMapLDRImageModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code normalMapLDRImageModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public int getNormalMapLDRImageModifierOffsetRelative(final float[] normalMapLDRImageModifier) {
		Objects.requireNonNull(normalMapLDRImageModifier, "normalMapLDRImageModifier == null");
		
		ParameterArguments.requireExact(normalMapLDRImageModifier.length % 8, 0, "normalMapLDRImageModifier.length % 8");
		
		return Structures.getStructureOffsetRelative(this.normalMapLDRImageModifiers, normalMapLDRImageModifier, this.normalMapLDRImageModifierOffsets);
	}
	
	/**
	 * Returns the {@link SimplexNoiseNormalMapModifier} count in this {@code CompiledModifierCache} instance.
	 * 
	 * @return the {@code SimplexNoiseNormalMapModifier} count in this {@code CompiledModifierCache} instance
	 */
	public int getSimplexNoiseNormalMapModifierCount() {
		return Structures.getStructureCount(this.simplexNoiseNormalMapModifiers, SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code simplexNoiseNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifier a {@link SimplexNoiseNormalMapModifier} instance in compiled form
	 * @return the absolute offset of {@code simplexNoiseNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifier} is {@code null}
	 */
	public int getSimplexNoiseNormalMapModifierOffsetAbsolute(final float[] simplexNoiseNormalMapModifier) {
		Objects.requireNonNull(simplexNoiseNormalMapModifier, "simplexNoiseNormalMapModifier == null");
		
		ParameterArguments.requireExactArrayLength(simplexNoiseNormalMapModifier, SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH, "simplexNoiseNormalMapModifier");
		
		return Structures.getStructureOffsetAbsolute(this.simplexNoiseNormalMapModifiers, simplexNoiseNormalMapModifier);
	}
	
	/**
	 * Returns the relative offset of {@code simplexNoiseNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifier a {@link SimplexNoiseNormalMapModifier} instance in compiled form
	 * @return the relative offset of {@code simplexNoiseNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexNoiseNormalMapModifier.length} is not equal to {@code CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifier} is {@code null}
	 */
	public int getSimplexNoiseNormalMapModifierOffsetRelative(final float[] simplexNoiseNormalMapModifier) {
		Objects.requireNonNull(simplexNoiseNormalMapModifier, "simplexNoiseNormalMapModifier == null");
		
		ParameterArguments.requireExactArrayLength(simplexNoiseNormalMapModifier, SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH, "simplexNoiseNormalMapModifier");
		
		return Structures.getStructureOffsetRelative(this.simplexNoiseNormalMapModifiers, simplexNoiseNormalMapModifier);
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link NormalMapLDRImageModifier} instances in this {@code CompiledModifierCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code NormalMapLDRImageModifier} instances in this {@code CompiledModifierCache} instance
	 */
	public int[] getNormalMapLDRImageModifierOffsets() {
		return this.normalMapLDRImageModifierOffsets;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link NormalMapLDRImageModifier} instances to {@code normalMapLDRImageModifierOffsets}.
	 * <p>
	 * If {@code normalMapLDRImageModifierOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code normalMapLDRImageModifierOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifierOffsets the {@code int[]} that contains the offsets for all {@code NormalMapLDRImageModifier} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code normalMapLDRImageModifierOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifierOffsets} is {@code null}
	 */
	public void setNormalMapLDRImageModifierOffsets(final int[] normalMapLDRImageModifierOffsets) {
		Objects.requireNonNull(normalMapLDRImageModifierOffsets, "normalMapLDRImageModifierOffsets == null");
		
		ParameterArguments.requireRange(normalMapLDRImageModifierOffsets, 0, Integer.MAX_VALUE, "normalMapLDRImageModifierOffsets");
		
		this.normalMapLDRImageModifierOffsets = normalMapLDRImageModifierOffsets;
	}
	
	/**
	 * Sets all {@link NormalMapLDRImageModifier} instances in compiled form to {@code normalMapLDRImageModifiers}.
	 * <p>
	 * If {@code normalMapLDRImageModifiers} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code normalMapLDRImageModifiers.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifiers the {@code NormalMapLDRImageModifier} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code normalMapLDRImageModifiers.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifiers} is {@code null}
	 */
	public void setNormalMapLDRImageModifiers(final float[] normalMapLDRImageModifiers) {
		Objects.requireNonNull(normalMapLDRImageModifiers, "normalMapLDRImageModifiers == null");
		
		ParameterArguments.requireExact(normalMapLDRImageModifiers.length % 8, 0, "normalMapLDRImageModifiers.length % 8");
		
		this.normalMapLDRImageModifiers = normalMapLDRImageModifiers;
	}
	
	/**
	 * Sets all {@link SimplexNoiseNormalMapModifier} instances in compiled form to {@code simplexNoiseNormalMapModifiers}.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifiers} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifiers.length % CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifiers the {@code SimplexNoiseNormalMapModifier} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code simplexNoiseNormalMapModifiers.length % CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifiers} is {@code null}
	 */
	public void setSimplexNoiseNormalMapModifiers(final float[] simplexNoiseNormalMapModifiers) {
		Objects.requireNonNull(simplexNoiseNormalMapModifiers, "simplexNoiseNormalMapModifiers == null");
		
		ParameterArguments.requireExact(simplexNoiseNormalMapModifiers.length % SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH, 0, "simplexNoiseNormalMapModifiers.length % CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH");
		
		this.simplexNoiseNormalMapModifiers = simplexNoiseNormalMapModifiers;
	}
	
	/**
	 * Writes this {@code CompiledModifierCache} instance to {@code document}.
	 * <p>
	 * If {@code document} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param document a {@link Document} instance
	 * @throws NullPointerException thrown if, and only if, {@code document} is {@code null}
	 */
	public void write(final Document document) {
		document.line("CompiledModifierCache {");
		document.indent();
		document.linef("normalMapLDRImageModifiers[%d]", Integer.valueOf(getNormalMapLDRImageModifierCount()));
		document.linef("simplexNoiseNormalMapModifiers[%d]", Integer.valueOf(getSimplexNoiseNormalMapModifierCount()));
		document.outdent();
		document.line("}");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code modifier} is supported, {@code false} otherwise.
	 * <p>
	 * If {@code modifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param modifier a {@code Modifier} instance
	 * @return {@code true} if, and only if, {@code modifier} is supported, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code modifier} is {@code null}
	 */
	public static boolean isSupported(final Modifier modifier) {
		Objects.requireNonNull(modifier, "modifier == null");
		
		if(modifier instanceof NormalMapLDRImageModifier) {
			return true;
		} else if(modifier instanceof NoOpModifier) {
			return true;
		} else if(modifier instanceof SimplexNoiseNormalMapModifier) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns a {@code float[]} with {@code normalMapLDRImageModifier} in compiled form.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance
	 * @return a {@code float[]} with {@code normalMapLDRImageModifier} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public static float[] toNormalMapLDRImageModifier(final NormalMapLDRImageModifier normalMapLDRImageModifier) {
		final AngleF angle = normalMapLDRImageModifier.getAngle();
		
		final Vector2F scale = normalMapLDRImageModifier.getScale();
		
		final int resolutionX = normalMapLDRImageModifier.getResolutionX();
		final int resolutionY = normalMapLDRImageModifier.getResolutionY();
		
		final int[] image = normalMapLDRImageModifier.getImage();
		
		final float[] array = new float[getNormalMapLDRImageModifierLength(normalMapLDRImageModifier)];
		
		array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_SCALE + 0] = scale.x;
		array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_SCALE + 1] = scale.y;
		array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_X] = resolutionX;
		array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_Y] = resolutionY;
		
		for(int i = 0; i < image.length; i++) {
			array[NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_IMAGE + i] = image[i];
		}
		
		for(int i = NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_IMAGE + image.length; i < array.length; i++) {
			array[i] = 0.0F;
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link NormalMapLDRImageModifier} instances in {@code normalMapLDRImageModifiers} in compiled form.
	 * <p>
	 * If {@code normalMapLDRImageModifiers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifiers a {@code List} of {@code NormalMapLDRImageModifier} instances
	 * @return a {@code float[]} with all {@code NormalMapLDRImageModifier} instances in {@code normalMapLDRImageModifiers} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifiers} or at least one of its elements are {@code null}
	 */
	public static float[] toNormalMapLDRImageModifiers(final List<NormalMapLDRImageModifier> normalMapLDRImageModifiers) {
		return Arrays.toFloatArray(normalMapLDRImageModifiers, normalMapLDRImageModifier -> toNormalMapLDRImageModifier(normalMapLDRImageModifier));
	}
	
	/**
	 * Returns a {@code float[]} with {@code simplexNoiseNormalMapModifier} in compiled form.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifier a {@link SimplexNoiseNormalMapModifier} instance
	 * @return a {@code float[]} with {@code simplexNoiseNormalMapModifier} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifier} is {@code null}
	 */
	public static float[] toSimplexNoiseNormalMapModifier(final SimplexNoiseNormalMapModifier simplexNoiseNormalMapModifier) {
		final float frequency = simplexNoiseNormalMapModifier.getFrequency();
		final float scale = simplexNoiseNormalMapModifier.getScale();
		
		final float[] array = new float[SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH];
		
//		Because the SimplexNoiseNormalMapModifier occupy 2/8 positions in a block, it should be aligned.
		array[SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_FREQUENCY] = frequency;	//Block #1
		array[SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_SCALE] = scale;			//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link SimplexNoiseNormalMapModifier} instances in {@code simplexNoiseNormalMapModifiers} in compiled form.
	 * <p>
	 * If {@code simplexNoiseNormalMapModifiers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param simplexNoiseNormalMapModifiers a {@code List} of {@code SimplexNoiseNormalMapModifier} instances
	 * @return a {@code float[]} with all {@code SimplexNoiseNormalMapModifier} instances in {@code simplexNoiseNormalMapModifiers} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code simplexNoiseNormalMapModifiers} or at least one of its elements are {@code null}
	 */
	public static float[] toSimplexNoiseNormalMapModifiers(final List<SimplexNoiseNormalMapModifier> simplexNoiseNormalMapModifiers) {
		return Arrays.toFloatArray(simplexNoiseNormalMapModifiers, simplexNoiseNormalMapModifier -> toSimplexNoiseNormalMapModifier(simplexNoiseNormalMapModifier));
	}
	
	/**
	 * Returns the length of {@code normalMapLDRImageModifier} in compiled form.
	 * <p>
	 * If {@code normalMapLDRImageModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifier a {@link NormalMapLDRImageModifier} instance
	 * @return the length of {@code normalMapLDRImageModifier} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifier} is {@code null}
	 */
	public static int getNormalMapLDRImageModifierLength(final NormalMapLDRImageModifier normalMapLDRImageModifier) {
		final int a = 5;
		final int b = normalMapLDRImageModifier.getResolution();
		final int c = padding(a + b);
		
		return a + b + c;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link NormalMapLDRImageModifier} instances in {@code normalMapLDRImageModifiers} in compiled form.
	 * <p>
	 * If {@code normalMapLDRImageModifiers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param normalMapLDRImageModifiers a {@code List} of {@code NormalMapLDRImageModifier} instances
	 * @return an {@code int[]} with the offsets for all {@code NormalMapLDRImageModifier} instances in {@code normalMapLDRImageModifiers} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code normalMapLDRImageModifiers} or at least one of its elements are {@code null}
	 */
	public static int[] toNormalMapLDRImageModifierOffsets(final List<NormalMapLDRImageModifier> normalMapLDRImageModifiers) {
		ParameterArguments.requireNonNullList(normalMapLDRImageModifiers, "normalMapLDRImageModifiers");
		
		final int[] normalMapLDRImageModifierOffsets = new int[normalMapLDRImageModifiers.size()];
		
		for(int i = 0, j = 0; i < normalMapLDRImageModifiers.size(); j += getNormalMapLDRImageModifierLength(normalMapLDRImageModifiers.get(i)), i++) {
			normalMapLDRImageModifierOffsets[i] = j;
		}
		
		return normalMapLDRImageModifierOffsets;
	}
}