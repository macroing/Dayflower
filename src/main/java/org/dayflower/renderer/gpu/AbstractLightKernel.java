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

import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;

import java.lang.reflect.Field;

import org.dayflower.scene.light.LDRImageLight;

/**
 * An {@code AbstractLightKernel} is an abstract extension of the {@link AbstractMaterialKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link LDRImageLight}</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractLightKernel extends AbstractMaterialKernel {
	private static final int LIGHT_SAMPLE_ARRAY_LENGTH = 10;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING = 6;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_POINT = 3;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 9;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_RESULT = 0;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected float[] lightLDRImageLightArray;
	
//	TODO: Add Javadocs!
	protected float[] lightPointLightArray;
	
//	TODO: Add Javadocs!
	protected float[] lightSampleArray_$private$10;
	
//	TODO: Add Javadocs!
	protected int lightLDRImageLightCount;
	
//	TODO: Add Javadocs!
	protected int lightPointLightCount;
	
//	TODO: Add Javadocs!
	protected int[] lightLDRImageLightOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractLightKernel} instance.
	 */
	protected AbstractLightKernel() {
		this.lightLDRImageLightArray = new float[1];
		this.lightLDRImageLightCount = 0;
		this.lightLDRImageLightOffsetArray = new int[1];
		this.lightPointLightArray = new float[1];
		this.lightSampleArray_$private$10 = new float[LIGHT_SAMPLE_ARRAY_LENGTH];
		this.lightPointLightCount = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected final void lightEvaluateRadianceEmittedAny() {
		doLightEvaluateRadianceEmittedClear();
		doLightEvaluateRadianceEmittedAnyLDRImageLight();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doLightEvaluateRadianceEmittedAnyLDRImageLight() {
		final int lightLDRImageLightCount = this.lightLDRImageLightCount;
		
		if(lightLDRImageLightCount > 0) {
			final int offset = min((int)(random() * lightLDRImageLightCount), lightLDRImageLightCount - 1);
			
			final float probabilityDensityFunctionValue = 1.0F / lightLDRImageLightCount;
			
			doLightEvaluateRadianceEmittedOneLDRImageLight(offset, probabilityDensityFunctionValue);
		}
	}
	
	private void doLightEvaluateRadianceEmittedClear() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightEvaluateRadianceEmittedOneLDRImageLight(final int offset, final float probabilityDensityFunctionValue) {
		final float rayDirectionX = ray3FGetDirectionComponent1();
		final float rayDirectionY = ray3FGetDirectionComponent2();
		final float rayDirectionZ = ray3FGetDirectionComponent3();
		
		final float textureCoordinatesU = 0.5F + atan2(rayDirectionZ, rayDirectionX) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = 0.5F - asinpi(rayDirectionY);
//		final float textureCoordinatesU = vector3FSphericalPhi(rayDirectionX, rayDirectionY, rayDirectionZ) * PI_MULTIPLIED_BY_2_RECIPROCAL;
//		final float textureCoordinatesV = vector3FSphericalTheta(rayDirectionX, rayDirectionY, rayDirectionZ) * PI_RECIPROCAL;
		
		final float angleRadians = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_ANGLE_RADIANS];
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float scaleU = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 0];
		final float scaleV = this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_SCALE + 1];
		
		final int resolutionX = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_X]);
		final int resolutionY = (int)(this.lightLDRImageLightArray[offset + LDRImageLight.ARRAY_OFFSET_RESOLUTION_Y]);
		
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
		
		final int offsetImage = offset + LDRImageLight.ARRAY_OFFSET_IMAGE;
		final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		
		final int color00RGB = (int)(this.lightLDRImageLightArray[offsetColor00RGB]);
		final int color01RGB = (int)(this.lightLDRImageLightArray[offsetColor01RGB]);
		final int color10RGB = (int)(this.lightLDRImageLightArray[offsetColor10RGB]);
		final int color11RGB = (int)(this.lightLDRImageLightArray[offsetColor11RGB]);
		
		final float tX = x - minimumX;
		final float tY = y - minimumY;
		
		final float component1 = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
		final float component2 = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
		final float component3 = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
		
		color3FLHSSet(component1 / probabilityDensityFunctionValue, component2 / probabilityDensityFunctionValue, component3 / probabilityDensityFunctionValue);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/*
	 * The following is a test implementation of the Light API in the CPU-renderer.
	 */
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// LightSample /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doLightSampleSetIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 0] = incomingX;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 1] = incomingY;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 2] = incomingZ;
	}
	
	private void doLightSampleSetPoint(final float pointX, final float pointY, final float pointZ) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 0] = pointX;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 1] = pointY;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 2] = pointZ;
	}
	
	private void doLightSampleSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	private void doLightSampleSetResult(final float resultR, final float resultG, final float resultB) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 0] = resultR;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 1] = resultG;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 2] = resultB;
	}
}