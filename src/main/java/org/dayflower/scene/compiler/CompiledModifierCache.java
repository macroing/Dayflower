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

import java.util.List;
import java.util.Objects;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Vector2F;
import org.dayflower.scene.modifier.LDRImageNormalMapModifier;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;
import org.dayflower.utility.Document;
import org.dayflower.utility.FloatArrays;
import org.dayflower.utility.IntArrays;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledModifierCache} contains {@link Modifier} instances in compiled form.
 * <p>
 * The {@code Modifier} implementations that are supported are the following:
 * <ul>
 * <li>{@link LDRImageNormalMapModifier}</li>
 * <li>{@link NoOpModifier}</li>
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
	 * The offset for the angle in radians in a compiled {@link LDRImageNormalMapModifier} instance.
	 */
	public static final int L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_ANGLE_RADIANS = 0;
	
	/**
	 * The offset for the image in a compiled {@link LDRImageNormalMapModifier} instance.
	 */
	public static final int L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_IMAGE = 5;
	
	/**
	 * The offset for the resolution of the X-axis in a compiled {@link LDRImageNormalMapModifier} instance.
	 */
	public static final int L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_X = 3;
	
	/**
	 * The offset for the resolution of the Y-axis in a compiled {@link LDRImageNormalMapModifier} instance.
	 */
	public static final int L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_Y = 4;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link LDRImageNormalMapModifier} instance.
	 */
	public static final int L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_SCALE = 1;
	
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
	
	private float[] lDRImageNormalMapModifiers;
	private float[] simplexNoiseNormalMapModifiers;
	private int[] lDRImageNormalMapModifierOffsets;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledModifierCache} instance.
	 */
	public CompiledModifierCache() {
		setLDRImageNormalMapModifierOffsets(new int[0]);
		setLDRImageNormalMapModifiers(new float[0]);
		setSimplexNoiseNormalMapModifiers(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code lDRImageNormalMapModifier} from this {@code CompiledModifierCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code lDRImageNormalMapModifier} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance in compiled form
	 * @return {@code true} if, and only if, {@code lDRImageNormalMapModifier} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public boolean removeLDRImageNormalMapModifier(final float[] lDRImageNormalMapModifier) {
		final int absoluteOffset = getLDRImageNormalMapModifierOffsetAbsolute(lDRImageNormalMapModifier);
		
		if(absoluteOffset != -1) {
			setLDRImageNormalMapModifierOffsets(Structures.removeStructureOffset(getLDRImageNormalMapModifierOffsets(), absoluteOffset, lDRImageNormalMapModifier.length));
			setLDRImageNormalMapModifiers(FloatArrays.splice(getLDRImageNormalMapModifiers(), absoluteOffset, lDRImageNormalMapModifier.length));
			
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
			setSimplexNoiseNormalMapModifiers(FloatArrays.splice(getSimplexNoiseNormalMapModifiers(), absoluteOffset, SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageNormalMapModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageNormalMapModifier} instances in compiled form that are associated with this {@code CompiledModifierCache} instance
	 */
	public float[] getLDRImageNormalMapModifiers() {
		return this.lDRImageNormalMapModifiers;
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
	 * Adds {@code lDRImageNormalMapModifier} to this {@code CompiledModifierCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code lDRImageNormalMapModifier}.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance in compiled form
	 * @return the relative offset to {@code lDRImageNormalMapModifier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public int addLDRImageNormalMapModifier(final float[] lDRImageNormalMapModifier) {
		final int absoluteOffsetNew = this.lDRImageNormalMapModifiers.length;
		final int relativeOffsetOld = getLDRImageNormalMapModifierOffsetRelative(lDRImageNormalMapModifier);
		final int relativeOffsetNew = getLDRImageNormalMapModifierCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLDRImageNormalMapModifierOffsets(IntArrays.merge(getLDRImageNormalMapModifierOffsets(), absoluteOffsetNew));
		setLDRImageNormalMapModifiers(FloatArrays.merge(getLDRImageNormalMapModifiers(), lDRImageNormalMapModifier));
		
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
		
		setSimplexNoiseNormalMapModifiers(FloatArrays.merge(getSimplexNoiseNormalMapModifiers(), simplexNoiseNormalMapModifier));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link LDRImageNormalMapModifier} count in this {@code CompiledModifierCache} instance.
	 * 
	 * @return the {@code LDRImageNormalMapModifier} count in this {@code CompiledModifierCache} instance
	 */
	public int getLDRImageNormalMapModifierCount() {
		return this.lDRImageNormalMapModifierOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code lDRImageNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance in compiled form
	 * @return the absolute offset of {@code lDRImageNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public int getLDRImageNormalMapModifierOffsetAbsolute(final float[] lDRImageNormalMapModifier) {
		Objects.requireNonNull(lDRImageNormalMapModifier, "lDRImageNormalMapModifier == null");
		
		ParameterArguments.requireExact(lDRImageNormalMapModifier.length % 8, 0, "lDRImageNormalMapModifier.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.lDRImageNormalMapModifiers, lDRImageNormalMapModifier, this.lDRImageNormalMapModifierOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code lDRImageNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance in compiled form
	 * @return the relative offset of {@code lDRImageNormalMapModifier} in this {@code CompiledModifierCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageNormalMapModifier.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public int getLDRImageNormalMapModifierOffsetRelative(final float[] lDRImageNormalMapModifier) {
		Objects.requireNonNull(lDRImageNormalMapModifier, "lDRImageNormalMapModifier == null");
		
		ParameterArguments.requireExact(lDRImageNormalMapModifier.length % 8, 0, "lDRImageNormalMapModifier.length % 8");
		
		return Structures.getStructureOffsetRelative(this.lDRImageNormalMapModifiers, lDRImageNormalMapModifier, this.lDRImageNormalMapModifierOffsets);
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
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageNormalMapModifier} instances in this {@code CompiledModifierCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageNormalMapModifier} instances in this {@code CompiledModifierCache} instance
	 */
	public int[] getLDRImageNormalMapModifierOffsets() {
		return this.lDRImageNormalMapModifierOffsets;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageNormalMapModifier} instances to {@code lDRImageNormalMapModifierOffsets}.
	 * <p>
	 * If {@code lDRImageNormalMapModifierOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code lDRImageNormalMapModifierOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifierOffsets the {@code int[]} that contains the offsets for all {@code LDRImageNormalMapModifier} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code lDRImageNormalMapModifierOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifierOffsets} is {@code null}
	 */
	public void setLDRImageNormalMapModifierOffsets(final int[] lDRImageNormalMapModifierOffsets) {
		Objects.requireNonNull(lDRImageNormalMapModifierOffsets, "lDRImageNormalMapModifierOffsets == null");
		
		ParameterArguments.requireRange(lDRImageNormalMapModifierOffsets, 0, Integer.MAX_VALUE, "lDRImageNormalMapModifierOffsets");
		
		this.lDRImageNormalMapModifierOffsets = lDRImageNormalMapModifierOffsets;
	}
	
	/**
	 * Sets all {@link LDRImageNormalMapModifier} instances in compiled form to {@code lDRImageNormalMapModifiers}.
	 * <p>
	 * If {@code lDRImageNormalMapModifiers} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code lDRImageNormalMapModifiers.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifiers the {@code LDRImageNormalMapModifier} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code lDRImageNormalMapModifiers.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifiers} is {@code null}
	 */
	public void setLDRImageNormalMapModifiers(final float[] lDRImageNormalMapModifiers) {
		Objects.requireNonNull(lDRImageNormalMapModifiers, "lDRImageNormalMapModifiers == null");
		
		ParameterArguments.requireExact(lDRImageNormalMapModifiers.length % 8, 0, "lDRImageNormalMapModifiers.length % 8");
		
		this.lDRImageNormalMapModifiers = lDRImageNormalMapModifiers;
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
		document.linef("lDRImageNormalMapModifiers[%d]", Integer.valueOf(getLDRImageNormalMapModifierCount()));
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
		
		if(modifier instanceof LDRImageNormalMapModifier) {
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
	 * Returns a {@code float[]} with {@code lDRImageNormalMapModifier} in compiled form.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance
	 * @return a {@code float[]} with {@code lDRImageNormalMapModifier} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public static float[] toLDRImageNormalMapModifier(final LDRImageNormalMapModifier lDRImageNormalMapModifier) {
		final AngleF angle = lDRImageNormalMapModifier.getAngle();
		
		final Vector2F scale = lDRImageNormalMapModifier.getScale();
		
		final int resolutionX = lDRImageNormalMapModifier.getResolutionX();
		final int resolutionY = lDRImageNormalMapModifier.getResolutionY();
		
		final int[] image = lDRImageNormalMapModifier.getImage();
		
		final float[] array = new float[getLDRImageNormalMapModifierLength(lDRImageNormalMapModifier)];
		
		array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_SCALE + 0] = scale.getU();
		array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_SCALE + 1] = scale.getV();
		array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_X] = resolutionX;
		array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_Y] = resolutionY;
		
		for(int i = 0; i < image.length; i++) {
			array[L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_IMAGE + i] = image[i];
		}
		
		for(int i = L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_IMAGE + image.length; i < array.length; i++) {
			array[i] = 0.0F;
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link LDRImageNormalMapModifier} instances in {@code lDRImageNormalMapModifiers} in compiled form.
	 * <p>
	 * If {@code lDRImageNormalMapModifiers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifiers a {@code List} of {@code LDRImageNormalMapModifier} instances
	 * @return a {@code float[]} with all {@code LDRImageNormalMapModifier} instances in {@code lDRImageNormalMapModifiers} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifiers} or at least one of its elements are {@code null}
	 */
	public static float[] toLDRImageNormalMapModifiers(final List<LDRImageNormalMapModifier> lDRImageNormalMapModifiers) {
		return FloatArrays.convert(lDRImageNormalMapModifiers, lDRImageNormalMapModifier -> toLDRImageNormalMapModifier(lDRImageNormalMapModifier));
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
		return FloatArrays.convert(simplexNoiseNormalMapModifiers, simplexNoiseNormalMapModifier -> toSimplexNoiseNormalMapModifier(simplexNoiseNormalMapModifier));
	}
	
	/**
	 * Returns the length of {@code lDRImageNormalMapModifier} in compiled form.
	 * <p>
	 * If {@code lDRImageNormalMapModifier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifier an {@link LDRImageNormalMapModifier} instance
	 * @return the length of {@code lDRImageNormalMapModifier} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifier} is {@code null}
	 */
	public static int getLDRImageNormalMapModifierLength(final LDRImageNormalMapModifier lDRImageNormalMapModifier) {
		final int a = 5;
		final int b = lDRImageNormalMapModifier.getResolution();
		final int c = padding(a + b);
		
		return a + b + c;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link LDRImageNormalMapModifier} instances in {@code lDRImageNormalMapModifiers} in compiled form.
	 * <p>
	 * If {@code lDRImageNormalMapModifiers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageNormalMapModifiers a {@code List} of {@code LDRImageNormalMapModifier} instances
	 * @return an {@code int[]} with the offsets for all {@code LDRImageNormalMapModifier} instances in {@code lDRImageNormalMapModifiers} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageNormalMapModifiers} or at least one of its elements are {@code null}
	 */
	public static int[] toLDRImageNormalMapModifierOffsets(final List<LDRImageNormalMapModifier> lDRImageNormalMapModifiers) {
		ParameterArguments.requireNonNullList(lDRImageNormalMapModifiers, "lDRImageNormalMapModifiers");
		
		final int[] lDRImageNormalMapModifierOffsets = new int[lDRImageNormalMapModifiers.size()];
		
		for(int i = 0, j = 0; i < lDRImageNormalMapModifiers.size(); j += getLDRImageNormalMapModifierLength(lDRImageNormalMapModifiers.get(i)), i++) {
			lDRImageNormalMapModifierOffsets[i] = j;
		}
		
		return lDRImageNormalMapModifierOffsets;
	}
}