/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
package org.dayflower.renderer.gpu;

import org.dayflower.scene.compiler.CompiledModifierCache;
import org.dayflower.scene.modifier.NormalMapLDRImageModifier;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;

/**
 * An {@code AbstractModifierKernel} is an abstract extension of the {@link AbstractShape3FKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link NoOpModifier}</li>
 * <li>{@link NormalMapLDRImageModifier}</li>
 * <li>{@link SimplexNoiseNormalMapModifier}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractModifierKernel extends AbstractShape3FKernel {
	/**
	 * A {@code float[]} that contains {@link NormalMapLDRImageModifier} instances.
	 */
	protected float[] modifierNormalMapLDRImageModifierArray;
	
	/**
	 * A {@code float[]} that contains {@link SimplexNoiseNormalMapModifier} instances.
	 */
	protected float[] modifierSimplexNoiseNormalMapModifierArray;
	
	/**
	 * An {@code int[]} that contains an offset lookup table for {@link NormalMapLDRImageModifier} instances in {@link #modifierNormalMapLDRImageModifierArray}.
	 */
	protected int[] modifierNormalMapLDRImageModifierOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractModifierKernel} instance.
	 */
	protected AbstractModifierKernel() {
		this.modifierNormalMapLDRImageModifierArray = new float[1];
		this.modifierNormalMapLDRImageModifierOffsetArray = new int[1];
		this.modifierSimplexNoiseNormalMapModifierArray = new float[1];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Performs modification to the surface using the {@link Modifier} instance represented by {@code modifierID} and {@code modifierOffset}.
	 * 
	 * @param modifierID the ID of the {@code Modifier} instance
	 * @param modifierOffset the offset of the {@code Modifier} instance
	 */
	protected final void modifierModify(final int modifierID, final int modifierOffset) {
		if(modifierID == NoOpModifier.ID) {
			return;
		} else if(modifierID == NormalMapLDRImageModifier.ID) {
			final float textureCoordinatesU = intersectionLHSGetTextureCoordinatesX();
			final float textureCoordinatesV = intersectionLHSGetTextureCoordinatesY();
			
			final int currentModifierOffsetAbsolute = this.modifierNormalMapLDRImageModifierOffsetArray[modifierOffset];
			
			final float angleRadians = this.modifierNormalMapLDRImageModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_ANGLE_RADIANS];
			final float angleRadiansCos = cos(angleRadians);
			final float angleRadiansSin = sin(angleRadians);
			
			final float scaleU = this.modifierNormalMapLDRImageModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_SCALE + 0];
			final float scaleV = this.modifierNormalMapLDRImageModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_SCALE + 1];
			
			final int resolutionX = (int)(this.modifierNormalMapLDRImageModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_X]);
			final int resolutionY = (int)(this.modifierNormalMapLDRImageModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_RESOLUTION_Y]);
			
			final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
			final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
			
			final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
			final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
			
			final int x = positiveModuloI((int)(textureCoordinatesScaledU), resolutionX);
			final int y = positiveModuloI((int)(textureCoordinatesScaledV), resolutionY);
			
			final int offsetImage = currentModifierOffsetAbsolute + CompiledModifierCache.NORMAL_MAP_L_D_R_IMAGE_MODIFIER_OFFSET_IMAGE;
			final int offsetColorRGB = offsetImage + (y * resolutionX + x);
			
			final int colorRGB = (int)(this.modifierNormalMapLDRImageModifierArray[offsetColorRGB]);
			
			final float r = colorRGBIntToRFloat(colorRGB);
			final float g = colorRGBIntToGFloat(colorRGB);
			final float b = colorRGBIntToBFloat(colorRGB);
			
			final float directionX = r * 2.0F - 1.0F;
			final float directionY = g * 2.0F - 1.0F;
			final float directionZ = b * 2.0F - 1.0F;
			
			intersectionLHSSetOrthonormalBasisSWTransform(directionX, directionY, directionZ);
		} else if(modifierID == SimplexNoiseNormalMapModifier.ID) {
			final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
			final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
			final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
			
			final float surfaceNormalSX = intersectionLHSGetOrthonormalBasisSWX();
			final float surfaceNormalSY = intersectionLHSGetOrthonormalBasisSWY();
			final float surfaceNormalSZ = intersectionLHSGetOrthonormalBasisSWZ();
			
			final float frequency = this.modifierSimplexNoiseNormalMapModifierArray[modifierOffset + CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_FREQUENCY];
			final float frequencyReciprocal = 1.0F / frequency;
			
			final float scale = this.modifierSimplexNoiseNormalMapModifierArray[modifierOffset + CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_SCALE];
			
			final float x0 = surfaceIntersectionPointX * frequencyReciprocal;
			final float y0 = surfaceIntersectionPointY * frequencyReciprocal;
			final float z0 = surfaceIntersectionPointZ * frequencyReciprocal;
			
			final float x1 = surfaceNormalSX + simplexFractionalBrownianMotionXYZ(x0, y0, z0, frequency, 0.5F, -0.25F, 0.25F, 16) * scale;
			final float y1 = surfaceNormalSY + simplexFractionalBrownianMotionXYZ(y0, z0, x0, frequency, 0.5F, -0.25F, 0.25F, 16) * scale;
			final float z1 = surfaceNormalSZ + simplexFractionalBrownianMotionXYZ(z0, x0, y0, frequency, 0.5F, -0.25F, 0.25F, 16) * scale;
			
			final float lengthReciprocal = vector3FLengthReciprocal(x1, y1, z1);
			
			final float x2 = x1 * lengthReciprocal;
			final float y2 = y1 * lengthReciprocal;
			final float z2 = z1 * lengthReciprocal;
			
			intersectionLHSSetOrthonormalBasisSW(x2, y2, z2);
		}
	}
}