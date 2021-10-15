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
package org.dayflower.renderer.gpu;

import org.dayflower.scene.compiler.CompiledModifierCache;
import org.dayflower.scene.modifier.LDRImageNormalMapModifier;
import org.dayflower.scene.modifier.Modifier;
import org.dayflower.scene.modifier.NoOpModifier;
import org.dayflower.scene.modifier.SimplexNoiseNormalMapModifier;

/**
 * An {@code AbstractModifierKernel} is an abstract extension of the {@link AbstractGeometryKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link LDRImageNormalMapModifier}</li>
 * <li>{@link NoOpModifier}</li>
 * <li>{@link SimplexNoiseNormalMapModifier}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractModifierKernel extends AbstractGeometryKernel {
	/**
	 * A {@code float[]} that contains {@link LDRImageNormalMapModifier} instances.
	 */
	protected float[] modifierLDRImageNormalMapModifierArray;
	
	/**
	 * A {@code float[]} that contains {@link SimplexNoiseNormalMapModifier} instances.
	 */
	protected float[] modifierSimplexNoiseNormalMapModifierArray;
	
	/**
	 * An {@code int[]} that contains an offset lookup table for {@link LDRImageNormalMapModifier} instances in {@link #modifierLDRImageNormalMapModifierArray}.
	 */
	protected int[] modifierLDRImageNormalMapModifierOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractModifierKernel} instance.
	 */
	protected AbstractModifierKernel() {
		this.modifierLDRImageNormalMapModifierArray = new float[1];
		this.modifierLDRImageNormalMapModifierOffsetArray = new int[1];
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
		if(modifierID == LDRImageNormalMapModifier.ID) {
			final float textureCoordinatesU = intersectionGetTextureCoordinatesComponent1();
			final float textureCoordinatesV = intersectionGetTextureCoordinatesComponent2();
			
			final int currentModifierOffsetAbsolute = this.modifierLDRImageNormalMapModifierOffsetArray[modifierOffset];
			
			final float angleRadians = this.modifierLDRImageNormalMapModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_ANGLE_RADIANS];
			final float angleRadiansCos = cos(angleRadians);
			final float angleRadiansSin = sin(angleRadians);
			
			final float scaleU = this.modifierLDRImageNormalMapModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_SCALE + 0];
			final float scaleV = this.modifierLDRImageNormalMapModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_SCALE + 1];
			
			final int resolutionX = (int)(this.modifierLDRImageNormalMapModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_X]);
			final int resolutionY = (int)(this.modifierLDRImageNormalMapModifierArray[currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_RESOLUTION_Y]);
			
			final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
			final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
			
			final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU * resolutionX - 0.5F;
			final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV * resolutionY - 0.5F;
			
			final float x = positiveModuloF(textureCoordinatesScaledU, resolutionX);
			final float y = positiveModuloF(textureCoordinatesScaledV, resolutionY);
			
			final int minimumX = (int)(floor(x));
			final int maximumX = (int)(ceil(x));
			
			final int minimumY = (int)(floor(y));
			final int maximumY = (int)(ceil(y));
			
			final int offsetImage = currentModifierOffsetAbsolute + CompiledModifierCache.L_D_R_IMAGE_NORMAL_MAP_MODIFIER_OFFSET_IMAGE;
			final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
			final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
			final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
			final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
			
			final int color00RGB = (int)(this.modifierLDRImageNormalMapModifierArray[offsetColor00RGB]);
			final int color01RGB = (int)(this.modifierLDRImageNormalMapModifierArray[offsetColor01RGB]);
			final int color10RGB = (int)(this.modifierLDRImageNormalMapModifierArray[offsetColor10RGB]);
			final int color11RGB = (int)(this.modifierLDRImageNormalMapModifierArray[offsetColor11RGB]);
			
			final float tX = x - minimumX;
			final float tY = y - minimumY;
			
			final float r = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
			final float g = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
			final float b = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
			
			final float directionX = r * 2.0F - 1.0F;
			final float directionY = g * 2.0F - 1.0F;
			final float directionZ = b * 2.0F - 1.0F;
			
			intersectionSetOrthonormalBasisSWTransform(directionX, directionY, directionZ);
		} else if(modifierID == NoOpModifier.ID) {
			return;
		} else if(modifierID == SimplexNoiseNormalMapModifier.ID) {
			final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
			final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
			final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
			
			final float surfaceNormalSX = intersectionGetOrthonormalBasisSWComponent1();
			final float surfaceNormalSY = intersectionGetOrthonormalBasisSWComponent2();
			final float surfaceNormalSZ = intersectionGetOrthonormalBasisSWComponent3();
			
			final float frequency = this.modifierSimplexNoiseNormalMapModifierArray[modifierOffset + CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_FREQUENCY];
			final float frequencyReciprocal = 1.0F / frequency;
			
			final float scale = this.modifierSimplexNoiseNormalMapModifierArray[modifierOffset + CompiledModifierCache.SIMPLEX_NOISE_NORMAL_MAP_MODIFIER_OFFSET_SCALE];
			
			final float x0 = surfaceIntersectionPointX * frequencyReciprocal;
			final float y0 = surfaceIntersectionPointY * frequencyReciprocal;
			final float z0 = surfaceIntersectionPointZ * frequencyReciprocal;
			
			final float x1 = simplexFractionalBrownianMotionXYZ(x0, y0, z0, frequency, 0.5F, surfaceNormalSX - 0.25F, surfaceNormalSX + 0.25F, 16) * scale;
			final float y1 = simplexFractionalBrownianMotionXYZ(y0, z0, x0, frequency, 0.5F, surfaceNormalSY - 0.25F, surfaceNormalSY + 0.25F, 16) * scale;
			final float z1 = simplexFractionalBrownianMotionXYZ(z0, x0, y0, frequency, 0.5F, surfaceNormalSZ - 0.25F, surfaceNormalSZ + 0.25F, 16) * scale;
			
			final float lengthReciprocal = vector3FLengthReciprocal(x1, y1, z1);
			
			final float x2 = x1 * lengthReciprocal;
			final float y2 = y1 * lengthReciprocal;
			final float z2 = z1 * lengthReciprocal;
			
			intersectionSetOrthonormalBasisSW(x2, y2, z2);
		}
	}
}