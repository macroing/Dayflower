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

/**
 * An {@code AbstractTextureKernel} is an abstract extension of the {@link AbstractGeometryKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link BlendTexture}</li>
 * <li>{@link BullseyeTexture}</li>
 * <li>{@link CheckerboardTexture}</li>
 * <li>{@link ConstantTexture}</li>
 * <li>{@link LDRImageTexture}</li>
 * <li>{@link MarbleTexture}</li>
 * <li>{@link SimplexFractionalBrownianMotionTexture}</li>
 * <li>{@link SurfaceNormalTexture}</li>
 * <li>{@link UVTexture}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractTextureKernel extends AbstractGeometryKernel {
	/**
	 * A {@code float[]} that contains {@link BlendTexture} instances.
	 */
	protected float[] textureBlendTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link BullseyeTexture} instances.
	 */
	protected float[] textureBullseyeTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link CheckerboardTexture} instances.
	 */
	protected float[] textureCheckerboardTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link ConstantTexture} instances.
	 */
	protected float[] textureConstantTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link LDRImageTexture} instances.
	 */
	protected float[] textureLDRImageTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link MarbleTexture} instances.
	 */
	protected float[] textureMarbleTextureArray;
	
	/**
	 * A {@code float[]} that contains {@link SimplexFractionalBrownianMotionTexture} instances.
	 */
	protected float[] textureSimplexFractionalBrownianMotionTextureArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractTextureKernel} instance.
	 */
	protected AbstractTextureKernel() {
		this.textureBlendTextureArray = new float[1];
		this.textureBullseyeTextureArray = new float[1];
		this.textureCheckerboardTextureArray = new float[1];
		this.textureConstantTextureArray = new float[1];
		this.textureLDRImageTextureArray = new float[1];
		this.textureMarbleTextureArray = new float[1];
		this.textureSimplexFractionalBrownianMotionTextureArray = new float[1];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the current {@link Texture} instance, which may be any of the supported {@code Texture} types.
	 * <p>
	 * Returns a {@code float} with the average value of the color.
	 * 
	 * @param textureID the ID of the {@code Texture} instance
	 * @param textureOffset the offset of the {@code Texture} instance
	 * @return a {@code float} with the average value of the color
	 */
	protected final float textureEvaluateFloat(final int textureID, final int textureOffset) {
		textureEvaluate(textureID, textureOffset);
		
		return color3FLHSGetAverage();
	}
	
	/**
	 * Evaluates the current {@link Texture} instance, which may be any of the supported {@code Texture} types.
	 * <p>
	 * The result of the evaluation will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} and {@link #color3FLHSGetComponent3()} may be used.
	 * 
	 * @param textureID the ID of the {@code Texture} instance
	 * @param textureOffset the offset of the {@code Texture} instance
	 */
	protected final void textureEvaluate(final int textureID, final int textureOffset) {
		if(textureID == BlendTexture.ID) {
			final int textureA = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_A]);
			final int textureAID = (textureA >>> 16) & 0xFFFF;
			final int textureAOffset = textureA & 0xFFFF;
			final int textureB = (int)(this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_TEXTURE_B]);
			final int textureBID = (textureB >>> 16) & 0xFFFF;
			final int textureBOffset = textureB & 0xFFFF;
			
			final float tComponent1 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_1];
			final float tComponent2 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_2];
			final float tComponent3 = this.textureBlendTextureArray[textureOffset + BlendTexture.ARRAY_OFFSET_T_COMPONENT_3];
			
			textureEvaluateExcludingBlendTexture(textureAID, textureAOffset);
			
			final float textureAComponent1 = color3FLHSGetComponent1();
			final float textureAComponent2 = color3FLHSGetComponent2();
			final float textureAComponent3 = color3FLHSGetComponent3();
			
			textureEvaluateExcludingBlendTexture(textureBID, textureBOffset);
			
			final float textureBComponent1 = color3FLHSGetComponent1();
			final float textureBComponent2 = color3FLHSGetComponent2();
			final float textureBComponent3 = color3FLHSGetComponent3();
			
			final float component1 = lerp(textureAComponent1, textureBComponent1, tComponent1);
			final float component2 = lerp(textureAComponent2, textureBComponent2, tComponent2);
			final float component3 = lerp(textureAComponent3, textureBComponent3, tComponent3);
			
			color3FLHSSet(component1, component2, component3);
		} else {
			textureEvaluateExcludingBlendTexture(textureID, textureOffset);
		}
	}
	
	/**
	 * Evaluates the current {@link Texture} instance, which may be any of the supported {@code Texture} types except for {@link BlendTexture}.
	 * 
	 * @param textureID the ID of the {@code Texture} instance
	 * @param textureOffset the offset of the {@code Texture} instance
	 */
	protected final void textureEvaluateExcludingBlendTexture(final int textureID, final int textureOffset) {
		int currentTextureID = textureID;
		int currentTextureOffset = textureOffset;
		
		float component1 = 0.0F;
		float component2 = 0.0F;
		float component3 = 0.0F;
		
		final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float textureCoordinatesU = intersectionGetTextureCoordinatesComponent1();
		final float textureCoordinatesV = intersectionGetTextureCoordinatesComponent2();
		
		while(currentTextureID != -1 && currentTextureOffset != -1) {
			if(currentTextureID == BullseyeTexture.ID) {
				final float originX = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 0];
				final float originY = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 1];
				final float originZ = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_ORIGIN + 2];
				
				final int textureA = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_A]);
				final int textureAID = (textureA >>> 16) & 0xFFFF;
				final int textureAOffset = textureA & 0xFFFF;
				final int textureB = (int)(this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_TEXTURE_B]);
				final int textureBID = (textureB >>> 16) & 0xFFFF;
				final int textureBOffset = textureB & 0xFFFF;
				
				final float scale = this.textureBullseyeTextureArray[currentTextureOffset + BullseyeTexture.ARRAY_OFFSET_SCALE];
				
				final float distance = point3FDistance(originX, originY, originZ, surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ);
				final float distanceScaled = distance * scale;
				final float distanceScaledRemainder = remainder(distanceScaled, 1.0F);
				
				final boolean isTextureA = distanceScaledRemainder > 0.5F;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == CheckerboardTexture.ID) {
				final float angleRadians = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final int textureA = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_A]);
				final int textureAID = (textureA >>> 16) & 0xFFFF;
				final int textureAOffset = textureA & 0xFFFF;
				final int textureB = (int)(this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_TEXTURE_B]);
				final int textureBID = (textureB >>> 16) & 0xFFFF;
				final int textureBOffset = textureB & 0xFFFF;
				
				final float scaleU = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureCheckerboardTextureArray[currentTextureOffset + CheckerboardTexture.ARRAY_OFFSET_SCALE + 1];
				
				final boolean isU = fractionalPart((textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin) * scaleU, false) > 0.5F;
				final boolean isV = fractionalPart((textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin) * scaleV, false) > 0.5F;
				
				final boolean isTextureA = isU ^ isV;
				
				currentTextureID = isTextureA ? textureAID : textureBID;
				currentTextureOffset = isTextureA ? textureAOffset : textureBOffset;
			} else if(currentTextureID == ConstantTexture.ID) {
				component1 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 0];
				component2 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 1];
				component3 = this.textureConstantTextureArray[currentTextureOffset + ConstantTexture.ARRAY_OFFSET_COLOR + 2];
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == LDRImageTexture.ID) {
				final float angleRadians = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_ANGLE_RADIANS];
				final float angleRadiansCos = cos(angleRadians);
				final float angleRadiansSin = sin(angleRadians);
				
				final float scaleU = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 0];
				final float scaleV = this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_SCALE + 1];
				
				final int resolutionX = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_X]);
				final int resolutionY = (int)(this.textureLDRImageTextureArray[currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_RESOLUTION_Y]);
				
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
				
				final int offsetImage = currentTextureOffset + LDRImageTexture.ARRAY_OFFSET_IMAGE;
				final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
				final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
				
				final int color00RGB = (int)(this.textureLDRImageTextureArray[offsetColor00RGB]);
				final int color01RGB = (int)(this.textureLDRImageTextureArray[offsetColor01RGB]);
				final int color10RGB = (int)(this.textureLDRImageTextureArray[offsetColor10RGB]);
				final int color11RGB = (int)(this.textureLDRImageTextureArray[offsetColor11RGB]);
				
				final float tX = x - minimumX;
				final float tY = y - minimumY;
				
				component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
				component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
				component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == MarbleTexture.ID) {
				final int colorARGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_A]);
				final int colorBRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_B]);
				final int colorCRGB = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_COLOR_C]);
				
				final float frequency = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_FREQUENCY];
				final float scale = this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_SCALE];
				
				final int octaves = (int)(this.textureMarbleTextureArray[currentTextureOffset + MarbleTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float x = surfaceIntersectionPointX * frequency;
				final float y = surfaceIntersectionPointY * frequency;
				final float z = surfaceIntersectionPointZ * frequency;
				final float r = scale * perlinTurbulenceXYZ(x, y, z, octaves);
				final float s = 2.0F * abs(sin(x + r));
				final float t = s < 1.0F ? s : s - 1.0F;
				
				component1 = s < 1.0F ? lerp(colorRGBIntToRFloat(colorCRGB), colorRGBIntToRFloat(colorBRGB), t) : lerp(colorRGBIntToRFloat(colorBRGB), colorRGBIntToRFloat(colorARGB), t);
				component2 = s < 1.0F ? lerp(colorRGBIntToGFloat(colorCRGB), colorRGBIntToGFloat(colorBRGB), t) : lerp(colorRGBIntToGFloat(colorBRGB), colorRGBIntToGFloat(colorARGB), t);
				component3 = s < 1.0F ? lerp(colorRGBIntToBFloat(colorCRGB), colorRGBIntToBFloat(colorBRGB), t) : lerp(colorRGBIntToBFloat(colorBRGB), colorRGBIntToBFloat(colorARGB), t);
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SimplexFractionalBrownianMotionTexture.ID) {
				final int colorRGB = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_COLOR]);
				
				final float frequency = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_FREQUENCY];
				final float gain = this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_GAIN];
				
				final int octaves = (int)(this.textureSimplexFractionalBrownianMotionTextureArray[currentTextureOffset + SimplexFractionalBrownianMotionTexture.ARRAY_OFFSET_OCTAVES]);
				
				final float colorR = colorRGBIntToRFloat(colorRGB);
				final float colorG = colorRGBIntToGFloat(colorRGB);
				final float colorB = colorRGBIntToBFloat(colorRGB);
				
				final float noise = simplexFractionalBrownianMotionXYZ(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ, frequency, gain, 0.0F, 1.0F, octaves);
				
				component1 = colorR * noise;
				component2 = colorG * noise;
				component3 = colorB * noise;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == SurfaceNormalTexture.ID) {
				component1 = (surfaceNormalX + 1.0F) * 0.5F;
				component2 = (surfaceNormalY + 1.0F) * 0.5F;
				component3 = (surfaceNormalZ + 1.0F) * 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else if(currentTextureID == UVTexture.ID) {
				component1 = textureCoordinatesU;
				component2 = textureCoordinatesV;
				component3 = 0.0F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			} else {
				component1 = 0.5F;
				component2 = 0.5F;
				component3 = 0.5F;
				
				currentTextureID = -1;
				currentTextureOffset = -1;
			}
		}
		
		color3FLHSSet(component1, component2, component3);
	}
}