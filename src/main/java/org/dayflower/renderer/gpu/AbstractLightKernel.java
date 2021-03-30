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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;

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
	private static final int LIGHT_ARRAY_LENGTH = 2;
	private static final int LIGHT_ARRAY_OFFSET_ID = 0;
	private static final int LIGHT_ARRAY_OFFSET_OFFSET = 1;
	
	private static final int LIGHT_SAMPLE_ARRAY_LENGTH = 10;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING = 6;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_POINT = 3;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE = 9;
	private static final int LIGHT_SAMPLE_ARRAY_OFFSET_RESULT = 0;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * A {@code float[]} that contains {@link DirectionalLight} instances.
	 */
	protected float[] lightDirectionalLightArray;
	
	/**
	 * A {@code float[]} that contains {@link LDRImageLight} instances.
	 */
	protected float[] lightLDRImageLightArray;
	
	/**
	 * A {@code float[]} that contains {@link PerezLight} instances.
	 */
	protected float[] lightPerezLightArray;
	
	/**
	 * A {@code float[]} that contains {@link PointLight} instances.
	 */
	protected float[] lightPointLightArray;
	
	/**
	 * A {@code float[]} that contains a {@link LightSample} instance.
	 */
	protected float[] lightSampleArray_$private$10;
	
	/**
	 * A {@code float[]} that contains {@link SpotLight} instances.
	 */
	protected float[] lightSpotLightArray;
	
	/**
	 * The {@link Light} count.
	 */
	protected int lightCount;
	
	/**
	 * The {@link DirectionalLight} count.
	 */
	protected int lightDirectionalLightCount;
	
	/**
	 * The {@link LDRImageLight} count.
	 */
	protected int lightLDRImageLightCount;
	
	/**
	 * The {@link PerezLight} count.
	 */
	protected int lightPerezLightCount;
	
	/**
	 * The {@link PointLight} count.
	 */
	protected int lightPointLightCount;
	
	/**
	 * The {@link SpotLight} count.
	 */
	protected int lightSpotLightCount;
	
	/**
	 * An {@code int[]} that contains the ID and offset for the current {@link Light} instance.
	 */
	protected int[] lightArray_$private$2;
	
	/**
	 * An {@code int[]} that contains an ID and offset lookup table for {@link Light} instances.
	 */
	protected int[] lightIDAndOffsetArray;
	
	/**
	 * An {@code int[]} that contains an offset lookup table for {@link LDRImageLight} instances in {@link #lightLDRImageLightArray}.
	 */
	protected int[] lightLDRImageLightOffsetArray;
	
	/**
	 * An {@code int[]} that contains an offset lookup table for {@link PerezLight} instances in {@link #lightPerezLightArray}.
	 */
	protected int[] lightPerezLightOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractLightKernel} instance.
	 */
	protected AbstractLightKernel() {
		this.lightArray_$private$2 = new int[LIGHT_ARRAY_LENGTH];
		this.lightCount = 0;
		this.lightIDAndOffsetArray = new int[1];
		this.lightDirectionalLightArray = new float[1];
		this.lightDirectionalLightCount = 0;
		this.lightLDRImageLightArray = new float[1];
		this.lightLDRImageLightCount = 0;
		this.lightLDRImageLightOffsetArray = new int[1];
		this.lightPerezLightArray = new float[1];
		this.lightPerezLightCount = 0;
		this.lightPerezLightOffsetArray = new int[1];
		this.lightPointLightArray = new float[1];
		this.lightPointLightCount = 0;
		this.lightSampleArray_$private$10 = new float[LIGHT_SAMPLE_ARRAY_LENGTH];
		this.lightSpotLightArray = new float[1];
		this.lightSpotLightCount = 0;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates the radiance emitted for a random {@link LDRImageLight} instance.
	 * <p>
	 * This method is used by the old lighting system and may be removed in the future.
	 */
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
	// Light ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current {@link Light} instance is using a delta distribution, {@code false} otherwise.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @return {@code true} if, and only if, the current {@code Light} instance is using a delta distribution, {@code false} otherwise
	 */
	protected final boolean lightIsUsingDeltaDistribution() {
		final int id = lightGetID();
		
		if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightIsUsingDeltaDistribution();
		} else if(doLightPerezLightIsMatchingID(id)) {
			return doLightPerezLightIsUsingDeltaDistribution();
		} else if(doLightPointLightIsMatchingID(id)) {
			return doLightPointLightIsUsingDeltaDistribution();
		} else if(doLightSpotLightIsMatchingID(id)) {
			return doLightSpotLightIsUsingDeltaDistribution();
		} else {
			return false;
		}
	}
	
	/**
	 * Samples the incoming radiance using the current {@link Light} instance.
	 * <p>
	 * Returns {@code true} if, and only if, a sample was created, {@code false} otherwise.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * <p>
	 * If this method returns {@code true}, a sample will be filled in {@link #lightSampleArray_$private$10}.
	 * <p>
	 * To retrieve the incoming direction of the sample, the methods {@link #lightSampleGetIncomingX()}, {@link #lightSampleGetIncomingY()} and {@link #lightSampleGetIncomingZ()} may be used.
	 * <p>
	 * To retrieve the point of the sample, the methods {@link #lightSampleGetPointX()}, {@link #lightSampleGetPointY()} and {@link #lightSampleGetPointZ()} may be used.
	 * <p>
	 * To retrieve the probability density function (PDF) value, the method {@link #lightSampleGetProbabilityDensityFunctionValue()} may be used.
	 * <p>
	 * To retrieve the result of the sample, the methods {@link #lightSampleGetResultR()}, {@link #lightSampleGetResultG()} and {@link #lightSampleGetResultB()} may be used.
	 * 
	 * @param u the U-component of the sample
	 * @param v the V-component of the sample
	 * @return {@code true} if, and only if, a sample was created, {@code false} otherwise
	 */
	protected final boolean lightSampleRadianceIncoming(final float u, final float v) {
		final int id = lightGetID();
		
		if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightSampleRadianceIncoming(u, v);
		} else if(doLightPerezLightIsMatchingID(id)) {
			return doLightPerezLightSampleRadianceIncoming(u, v);
		} else if(doLightPointLightIsMatchingID(id)) {
			return doLightPointLightSampleRadianceIncoming(u, v);
		} else if(doLightSpotLightIsMatchingID(id)) {
			return doLightSpotLightSampleRadianceIncoming(u, v);
		} else {
			return false;
		}
	}
	
	/**
	 * Evaluates the probability density function (PDF) for the incoming radiance using the current {@link Light} instance.
	 * <p>
	 * Returns the probability density function (PDF) value.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @param incomingX the X-component of the incoming direction in world space
	 * @param incomingY the Y-component of the incoming direction in world space
	 * @param incomingZ the Z-component of the incoming direction in world space
	 * @return the probability density function (PDF) value
	 */
	protected final float lightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		final int id = lightGetID();
		
		if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else if(doLightPerezLightIsMatchingID(id)) {
			return doLightPerezLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else if(doLightPointLightIsMatchingID(id)) {
			return doLightPointLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else if(doLightSpotLightIsMatchingID(id)) {
			return doLightSpotLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else {
			return 0.0F;
		}
	}
	
	/**
	 * Returns the ID of the current {@link Light} instance.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @return the ID of the current {@code Light} instance
	 */
	protected final int lightGetID() {
		return this.lightArray_$private$2[LIGHT_ARRAY_OFFSET_ID];
	}
	
	/**
	 * Returns the offset for the current {@link Light} instance.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @return the offset for the current {@code Light} instance
	 */
	protected final int lightGetOffset() {
		return this.lightArray_$private$2[LIGHT_ARRAY_OFFSET_OFFSET];
	}
	
	/**
	 * Evaluates the radiance emitted along the current ray using the current {@link Light} instance.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called and that the array {@link AbstractGeometryKernel#ray3FArray_$private$8 ray3FArray_$private$8} contains a ray.
	 * <p>
	 * The result will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} or {@link #color3FLHSGetComponent3()} may be used.
	 */
	protected final void lightEvaluateRadianceEmitted() {
		final int id = lightGetID();
		
		if(doLightDirectionalLightIsMatchingID(id)) {
			doLightDirectionalLightEvaluateRadianceEmitted();
		} else if(doLightPerezLightIsMatchingID(id)) {
			doLightPerezLightEvaluateRadianceEmitted();
		} else if(doLightPointLightIsMatchingID(id)) {
			doLightPointLightEvaluateRadianceEmitted();
		} else if(doLightSpotLightIsMatchingID(id)) {
			doLightSpotLightEvaluateRadianceEmitted();
		}
	}
	
	/**
	 * Evaluates the radiance emitted along the current ray using all {@link Light} instances for which this is supported.
	 */
	protected final void lightEvaluateRadianceEmittedAll() {
		final int lightPerezLightCount = this.lightPerezLightCount;
		
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		for(int i = 0; i < lightPerezLightCount; i++) {
			final int lightPerezLightArrayOffset = this.lightPerezLightOffsetArray[i];
			
			lightSet(PerezLight.ID, lightPerezLightArrayOffset);
			lightEvaluateRadianceEmitted();
			
			radianceR += color3FLHSGetR();
			radianceG += color3FLHSGetG();
			radianceB += color3FLHSGetB();
		}
		
		color3FLHSSet(radianceR, radianceG, radianceB);
	}
	
	/**
	 * Computes the power of the current {@link Light} instance.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * <p>
	 * The result will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} or {@link #color3FLHSGetComponent3()} may be used.
	 */
	protected final void lightPower() {
		final int id = lightGetID();
		
		if(doLightDirectionalLightIsMatchingID(id)) {
			doLightDirectionalLightPower();
		} else if(doLightPerezLightIsMatchingID(id)) {
			doLightPerezLightPower();
		} else if(doLightPointLightIsMatchingID(id)) {
			doLightPointLightPower();
		} else if(doLightSpotLightIsMatchingID(id)) {
			doLightSpotLightPower();
		}
	}
	
	/**
	 * Sets the current {@link Light} instance.
	 * 
	 * @param id the ID of the current {@code Light} instance
	 * @param offset the offset for the current {@code Light} instance
	 */
	protected final void lightSet(final int id, final int offset) {
		this.lightArray_$private$2[LIGHT_ARRAY_OFFSET_ID] = id;
		this.lightArray_$private$2[LIGHT_ARRAY_OFFSET_OFFSET] = offset;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// LightSample /////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the X-component of the incoming direction in the current {@link LightSample} instance.
	 * 
	 * @return the X-component of the incoming direction in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetIncomingX() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 0];
	}
	
	/**
	 * Returns the Y-component of the incoming direction in the current {@link LightSample} instance.
	 * 
	 * @return the Y-component of the incoming direction in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetIncomingY() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 1];
	}
	
	/**
	 * Returns the Z-component of the incoming direction in the current {@link LightSample} instance.
	 * 
	 * @return the Z-component of the incoming direction in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetIncomingZ() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 2];
	}
	
	/**
	 * Returns the X-component of the point in the current {@link LightSample} instance.
	 * 
	 * @return the X-component of the point in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetPointX() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 0];
	}
	
	/**
	 * Returns the Y-component of the point in the current {@link LightSample} instance.
	 * 
	 * @return the Y-component of the point in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetPointY() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 1];
	}
	
	/**
	 * Returns the Z-component of the point in the current {@link LightSample} instance.
	 * 
	 * @return the Z-component of the point in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetPointZ() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 2];
	}
	
	/**
	 * Returns the probability density function (PDF) value in the current {@link LightSample} instance.
	 * 
	 * @return the probability density function (PDF) value in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetProbabilityDensityFunctionValue() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE];
	}
	
	/**
	 * Returns the B-component of the color result in the current {@link LightSample} instance.
	 * 
	 * @return the B-component of the color result in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetResultB() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 2];
	}
	
	/**
	 * Returns the G-component of the color result in the current {@link LightSample} instance.
	 * 
	 * @return the G-component of the color result in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetResultG() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 1];
	}
	
	/**
	 * Returns the R-component of the color result in the current {@link LightSample} instance.
	 * 
	 * @return the R-component of the color result in the current {@code LightSample} instance
	 */
	protected final float lightSampleGetResultR() {
		return this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 0];
	}
	
	/**
	 * Sets the incoming direction in the current {@link LightSample} instance.
	 * 
	 * @param incomingX the X-component of the incoming direction in world space
	 * @param incomingY the Y-component of the incoming direction in world space
	 * @param incomingZ the Z-component of the incoming direction in world space
	 */
	protected final void lightSampleSetIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 0] = incomingX;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 1] = incomingY;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_INCOMING + 2] = incomingZ;
	}
	
	/**
	 * Sets the point in the current {@link LightSample} instance.
	 * 
	 * @param pointX the X-component of the point
	 * @param pointY the Y-component of the point
	 * @param pointZ the Z-component of the point
	 */
	protected final void lightSampleSetPoint(final float pointX, final float pointY, final float pointZ) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 0] = pointX;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 1] = pointY;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_POINT + 2] = pointZ;
	}
	
	/**
	 * Sets the probability density function (PDF) value in the current {@link LightSample} instance.
	 * 
	 * @param probabilityDensityFunctionValue the probability density function (PDF) value
	 */
	protected final void lightSampleSetProbabilityDensityFunctionValue(final float probabilityDensityFunctionValue) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_PROBABILITY_DENSITY_FUNCTION_VALUE] = probabilityDensityFunctionValue;
	}
	
	/**
	 * Sets the color result in the current {@link LightSample} instance.
	 * 
	 * @param resultR the R-component of the color result
	 * @param resultG the G-component of the color result
	 * @param resultB the B-component of the color result
	 */
	protected final void lightSampleSetResult(final float resultR, final float resultG, final float resultB) {
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 0] = resultR;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 1] = resultG;
		this.lightSampleArray_$private$10[LIGHT_SAMPLE_ARRAY_OFFSET_RESULT + 2] = resultB;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Light - DirectionalLight ////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightDirectionalLightIsMatchingID(final int id) {
		return id == DirectionalLight.ID;
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightDirectionalLightIsUsingDeltaDistribution() {
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean doLightDirectionalLightSampleRadianceIncoming(final float u, final float v) {
		final float radianceR = doLightDirectionalLightGetRadianceR();
		final float radianceG = doLightDirectionalLightGetRadianceG();
		final float radianceB = doLightDirectionalLightGetRadianceB();
		
		final float directionX = doLightDirectionalLightGetDirectionX();
		final float directionY = doLightDirectionalLightGetDirectionY();
		final float directionZ = doLightDirectionalLightGetDirectionZ();
		
		final float radius = doLightDirectionalLightGetRadius();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float pointX = surfaceIntersectionPointX + directionX * 2.0F * radius;
		final float pointY = surfaceIntersectionPointY + directionY * 2.0F * radius;
		final float pointZ = surfaceIntersectionPointZ + directionZ * 2.0F * radius;
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		lightSampleSetIncoming(directionX, directionY, directionZ);
		lightSampleSetPoint(pointX, pointY, pointZ);
		lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
		lightSampleSetResult(radianceR, radianceG, radianceB);
		
		return true;
	}
	
	@SuppressWarnings({"static-method", "unused"})
	private float doLightDirectionalLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		return 0.0F;
	}
	
	private float doLightDirectionalLightGetDirectionX() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_DIRECTION + 0];
	}
	
	private float doLightDirectionalLightGetDirectionY() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_DIRECTION + 1];
	}
	
	private float doLightDirectionalLightGetDirectionZ() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_DIRECTION + 2];
	}
	
	private float doLightDirectionalLightGetRadianceB() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_RADIANCE + 2];
	}
	
	private float doLightDirectionalLightGetRadianceG() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_RADIANCE + 1];
	}
	
	private float doLightDirectionalLightGetRadianceR() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_RADIANCE + 0];
	}
	
	private float doLightDirectionalLightGetRadius() {
		return this.lightDirectionalLightArray[lightGetOffset() + DirectionalLight.ARRAY_OFFSET_RADIUS];
	}
	
	private void doLightDirectionalLightEvaluateRadianceEmitted() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightDirectionalLightPower() {
		final float radianceR = doLightDirectionalLightGetRadianceR();
		final float radianceG = doLightDirectionalLightGetRadianceG();
		final float radianceB = doLightDirectionalLightGetRadianceB();
		
		final float radius = doLightDirectionalLightGetRadius();
		
		final float scale = PI * radius * radius;
		
		final float powerR = radianceR * scale;
		final float powerG = radianceG * scale;
		final float powerB = radianceB * scale;
		
		color3FLHSSet(powerR, powerG, powerB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Light - PerezLight //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightPerezLightIsMatchingID(final int id) {
		return id == PerezLight.ID;
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightPerezLightIsUsingDeltaDistribution() {
		return false;
	}
	
	private boolean doLightPerezLightSampleRadianceIncoming(final float u, final float v) {
		final float sunDirectionX = doLightPerezLightGetSunDirectionX();
		final float sunDirectionY = doLightPerezLightGetSunDirectionY();
		final float sunDirectionZ = doLightPerezLightGetSunDirectionZ();
		
		final float sunDirectionWorldSpaceX = doLightPerezLightGetSunDirectionWorldSpaceX();
		final float sunDirectionWorldSpaceY = doLightPerezLightGetSunDirectionWorldSpaceY();
		final float sunDirectionWorldSpaceZ = doLightPerezLightGetSunDirectionWorldSpaceZ();
		
		final float radius = doLightPerezLightGetRadius();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float surfaceNormalGX = intersectionGetOrthonormalBasisGWComponent1();
		final float surfaceNormalGY = intersectionGetOrthonormalBasisGWComponent2();
		final float surfaceNormalGZ = intersectionGetOrthonormalBasisGWComponent3();
		
		final float surfaceNormalSX = intersectionGetOrthonormalBasisSWComponent1();
		final float surfaceNormalSY = intersectionGetOrthonormalBasisSWComponent2();
		final float surfaceNormalSZ = intersectionGetOrthonormalBasisSWComponent3();
		
		final float sunDirectionWorldSpaceDotSurfaceNormalG = vector3FDotProduct(sunDirectionWorldSpaceX, sunDirectionWorldSpaceY, sunDirectionWorldSpaceZ, surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ);
		final float sunDirectionWorldSpaceDotSurfaceNormalS = vector3FDotProduct(sunDirectionWorldSpaceX, sunDirectionWorldSpaceY, sunDirectionWorldSpaceZ, surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
		
		final float probability = 0.5F;
		final float probabilityDotProduct = random();
		
		final boolean isOrientedTowardsSun = sunDirectionWorldSpaceDotSurfaceNormalG > probabilityDotProduct && sunDirectionWorldSpaceDotSurfaceNormalS > probabilityDotProduct;
		final boolean isSamplingSun = random() < probability;
		
		final int offsetDistribution = lightGetOffset() + PerezLight.ARRAY_OFFSET_DISTRIBUTION;
		
		if(isOrientedTowardsSun && isSamplingSun) {
			final float incomingObjectSpaceX = sunDirectionX;
			final float incomingObjectSpaceY = sunDirectionY;
			final float incomingObjectSpaceZ = sunDirectionZ;
			
			final float incomingWorldSpaceX = sunDirectionWorldSpaceX;
			final float incomingWorldSpaceY = sunDirectionWorldSpaceY;
			final float incomingWorldSpaceZ = sunDirectionWorldSpaceZ;
			
			final float sinTheta = vector3FSinTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
			
			if(!checkIsZero(sinTheta)) {
				final float resultR = doLightPerezLightGetSunColorR();
				final float resultG = doLightPerezLightGetSunColorG();
				final float resultB = doLightPerezLightGetSunColorB();
				
				final float pointX = surfaceIntersectionPointX + incomingWorldSpaceX * 2.0F * radius;
				final float pointY = surfaceIntersectionPointY + incomingWorldSpaceY * 2.0F * radius;
				final float pointZ = surfaceIntersectionPointZ + incomingWorldSpaceZ * 2.0F * radius;
				
				final float sampleRemappedU = vector3FSphericalPhi(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_MULTIPLIED_BY_2_RECIPROCAL;
				final float sampleRemappedV = vector3FSphericalTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_RECIPROCAL;
				
				final float probabilityDensityFunctionValueRemapped = doLightPerezLightDistribution2FContinuousProbabilityDensityFunction(offsetDistribution, sampleRemappedU, sampleRemappedV, true);
				
				if(!checkIsZero(probabilityDensityFunctionValueRemapped)) {
					final float probabilityDensityFunctionValue = probabilityDensityFunctionValueRemapped / (2.0F * PI * PI * sinTheta);
					
					lightSampleSetIncoming(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
					lightSampleSetPoint(pointX, pointY, pointZ);
					lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
					lightSampleSetResult(resultR, resultG, resultB);
					
					return true;
				}
			}
		}
		
		doLightPerezLightDistribution2FContinuousRemap(lightGetOffset() + PerezLight.ARRAY_OFFSET_DISTRIBUTION, u, v);
		
		final float sampleRemappedU = point2FGetComponent1();
		final float sampleRemappedV = point2FGetComponent2();
		
		final float probabilityDensityFunctionValueRemapped = doLightPerezLightDistribution2FContinuousProbabilityDensityFunction(offsetDistribution, sampleRemappedU, sampleRemappedV, true);
		
		if(checkIsZero(probabilityDensityFunctionValueRemapped)) {
			return false;
		}
		
		vector3FSetDirectionSpherical2(sampleRemappedU, sampleRemappedV);
		
		final float incomingObjectSpaceX = vector3FGetComponent1();
		final float incomingObjectSpaceY = vector3FGetComponent2();
		final float incomingObjectSpaceZ = vector3FGetComponent3();
		
		doLightPerezLightTransformToWorldSpace(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		final float incomingWorldSpaceX = vector3FGetComponent1();
		final float incomingWorldSpaceY = vector3FGetComponent2();
		final float incomingWorldSpaceZ = vector3FGetComponent3();
		
		final float sinTheta = vector3FSinTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		if(checkIsZero(sinTheta)) {
			return false;
		}
		
		doLightPerezLightRadianceSky(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		final float resultR = color3FLHSGetR();
		final float resultG = color3FLHSGetG();
		final float resultB = color3FLHSGetB();
		
		final float pointX = surfaceIntersectionPointX + incomingWorldSpaceX * 2.0F * radius;
		final float pointY = surfaceIntersectionPointY + incomingWorldSpaceY * 2.0F * radius;
		final float pointZ = surfaceIntersectionPointZ + incomingWorldSpaceZ * 2.0F * radius;
		
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueRemapped / (2.0F * PI * PI * sinTheta);
		
		lightSampleSetIncoming(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
		lightSampleSetPoint(pointX, pointY, pointZ);
		lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
		lightSampleSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	private float doLightPerezLightCalculatePerezFunction(final float theta, final float gamma, final float lam0, final float lam1, final float lam2, final float lam3, final float lam4, final float lvz) {
		final float thetaConstant = this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_THETA];
		
		final float cosTheta = cos(theta);
		final float cosThetaConstant = cos(thetaConstant);
		final float cosGamma = cos(gamma);
		
		final float den = (1.0F + lam0 * exp(lam1)) * (1.0F + lam2 * exp(lam3 * thetaConstant) + lam4 * cosThetaConstant * cosThetaConstant);
		final float num = (1.0F + lam0 * exp(lam1 / cosTheta)) * (1.0F + lam2 * exp(lam3 * gamma) + lam4 * cosGamma * cosGamma);
		
		return lvz * num / den;
	}
	
	private float doLightPerezLightCalculatePerezFunctionRelativeLuminance(final float theta, final float gamma) {
		final int offset = lightGetOffset();
		
		final float lam0 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_RELATIVE_LUMINANCE + 0];
		final float lam1 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_RELATIVE_LUMINANCE + 1];
		final float lam2 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_RELATIVE_LUMINANCE + 2];
		final float lam3 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_RELATIVE_LUMINANCE + 3];
		final float lam4 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_RELATIVE_LUMINANCE + 4];
		
		final float lvz = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_ZENITH + 0];
		
		return doLightPerezLightCalculatePerezFunction(theta, gamma, lam0, lam1, lam2, lam3, lam4, lvz);
	}
	
	private float doLightPerezLightCalculatePerezFunctionX(final float theta, final float gamma) {
		final int offset = lightGetOffset();
		
		final float lam0 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_X + 0];
		final float lam1 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_X + 1];
		final float lam2 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_X + 2];
		final float lam3 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_X + 3];
		final float lam4 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_X + 4];
		
		final float lvz = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_ZENITH + 1];
		
		return doLightPerezLightCalculatePerezFunction(theta, gamma, lam0, lam1, lam2, lam3, lam4, lvz);
	}
	
	private float doLightPerezLightCalculatePerezFunctionY(final float theta, final float gamma) {
		final int offset = lightGetOffset();
		
		final float lam0 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_Y + 0];
		final float lam1 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_Y + 1];
		final float lam2 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_Y + 2];
		final float lam3 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_Y + 3];
		final float lam4 = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_PEREZ_Y + 4];
		
		final float lvz = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_ZENITH + 2];
		
		return doLightPerezLightCalculatePerezFunction(theta, gamma, lam0, lam1, lam2, lam3, lam4, lvz);
	}
	
	private float doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(final int offset, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		
		if(index >= 0 && index <= count - 1) {
			final float functionIntegral = doLightPerezLightDistribution1FFunctionIntegral(offset);
			final float function = doLightPerezLightDistribution1FFunction(offset, index);
			
			return functionIntegral > 0.0F ? function / functionIntegral : 0.0F;
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FContinuousRemap(final int offset, final float value, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		
		if(index >= 0 && index <= count - 1) {
			final float cumulativeDistributionFunction0 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 0);
			final float cumulativeDistributionFunction1 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 1);
			
			if(cumulativeDistributionFunction1 - cumulativeDistributionFunction0 > 0.0F) {
				return (index + ((value - cumulativeDistributionFunction0) / (cumulativeDistributionFunction1 - cumulativeDistributionFunction0))) / count;
			}
			
			return (index + (value - cumulativeDistributionFunction0)) / count;
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FCumulativeDistributionFunction(final int offset, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		final int offsetCumulativeDistributionFunction = offset + 1 + 1 + 1 + 1;
		
		if(index >= 0 && index <= count - 1) {
			return this.lightPerezLightArray[offsetCumulativeDistributionFunction + index];
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FDiscreteProbabilityDensityFunction(final int offset, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		
		if(index >= 0 && index <= count - 1) {
			final float functionIntegral = doLightPerezLightDistribution1FFunctionIntegral(offset);
			final float function = doLightPerezLightDistribution1FFunction(offset, index);
			
			return functionIntegral > 0.0F ? function / (functionIntegral * count) : 0.0F;
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FDiscreteRemap(final int offset, final float value, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		
		if(index >= 0 && index <= count - 1) {
			final float cumulativeDistributionFunction0 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 0);
			final float cumulativeDistributionFunction1 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 1);
			
			return (value - cumulativeDistributionFunction0) / (cumulativeDistributionFunction1 - cumulativeDistributionFunction0);
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FFunction(final int offset, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		final int offsetFunction = offset + 1 + 1 + 1 + (int)(this.lightPerezLightArray[offset + 1]) + 1;
		
		if(index >= 0 && index <= count - 1) {
			return this.lightPerezLightArray[offsetFunction + index];
		}
		
		return 0.0F;
	}
	
	private float doLightPerezLightDistribution1FFunctionIntegral(final int offset) {
		return this.lightPerezLightArray[offset + 3];
	}
	
	private float doLightPerezLightDistribution2FContinuousProbabilityDensityFunction(final int offset, final float u, final float v, final boolean isRemapped) {
		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		if(isRemapped) {
			final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
			final int countM = doLightPerezLightDistribution1FCount(offsetM);
			final int indexM = saturateI((int)(m * countM), 0, countM - 1);
			final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
			final int countC = doLightPerezLightDistribution1FCount(offsetC);
			final int indexC = saturateI((int)(c * countC), 0, countC - 1);
			
			final float probabilityDensityFunctionValue = doLightPerezLightDistribution1FFunction(offsetC, indexC) / doLightPerezLightDistribution1FFunctionIntegral(offsetM);
			
			return probabilityDensityFunctionValue;
		}
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int indexM = doLightPerezLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int indexC = doLightPerezLightDistribution1FIndex(offsetC, c);
		
		final float probabilityDensityFunctionValueM = doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(offsetM, indexM);
		final float probabilityDensityFunctionValueC = doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(offsetC, indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
	@SuppressWarnings("unused")
	private float doLightPerezLightDistribution2FDiscreteProbabilityDensityFunction(final int offset, final float u, final float v) {
		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int indexM = doLightPerezLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int indexC = doLightPerezLightDistribution1FIndex(offsetC, c);
		
		final float probabilityDensityFunctionValueM = doLightPerezLightDistribution1FDiscreteProbabilityDensityFunction(offsetM, indexM);
		final float probabilityDensityFunctionValueC = doLightPerezLightDistribution1FDiscreteProbabilityDensityFunction(offsetC, indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightPerezLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		doLightPerezLightTransformToObjectSpace(incomingX, incomingY, incomingZ);
		
		final float incomingObjectSpaceX = vector3FGetComponent1();
		final float incomingObjectSpaceY = vector3FGetComponent2();
		final float incomingObjectSpaceZ = vector3FGetComponent3();
		
		final float sinTheta = vector3FSphericalTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		if(checkIsZero(sinTheta)) {
			return 0.0F;
		}
		
		final float u = vector3FSphericalPhi(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float v = vector3FSphericalTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_RECIPROCAL;
		
		final float probabilityDensityFunctionValue = doLightPerezLightDistribution2FContinuousProbabilityDensityFunction(lightGetOffset() + PerezLight.ARRAY_OFFSET_DISTRIBUTION, u, v, true) / (2.0F * PI * PI * sinTheta);
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightPerezLightGetRadius() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_RADIUS];
	}
	
	private float doLightPerezLightGetSunColorB() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_COLOR + 2];
	}
	
	private float doLightPerezLightGetSunColorG() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_COLOR + 1];
	}
	
	private float doLightPerezLightGetSunColorR() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_COLOR + 0];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceX() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION_WORLD_SPACE + 0];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceY() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION_WORLD_SPACE + 1];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceZ() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION_WORLD_SPACE + 2];
	}
	
	private float doLightPerezLightGetSunDirectionX() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 0];
	}
	
	private float doLightPerezLightGetSunDirectionY() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 1];
	}
	
	private float doLightPerezLightGetSunDirectionZ() {
		return this.lightPerezLightArray[lightGetOffset() + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 2];
	}
	
	private int doLightPerezLightDistribution1FCount(final int offset) {
		return (int)(this.lightPerezLightArray[offset + 2]);
	}
	
	private int doLightPerezLightDistribution1FIndex(final int offset, final float value) {
		final int length = (int)(this.lightPerezLightArray[offset + 1]);
		
		int currentMinimum = 0;
		int currentLength = length;
		
		while(currentLength > 0) {
			int currentHalf = currentLength >> 1;
			int currentMiddle = currentMinimum + currentHalf;
			
			if(doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, currentMiddle) <= value) {
				currentMinimum = currentMiddle + 1;
				currentLength -= currentHalf + 1;
			} else {
				currentLength = currentHalf;
			}
		}
		
		return saturateI(currentMinimum - 1, 0, length - 2);
	}
	
	private void doLightPerezLightDistribution2FContinuousRemap(final int offset, final float u, final float v) {
		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int indexM = doLightPerezLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int indexC = doLightPerezLightDistribution1FIndex(offsetC, c);
		
		final float mRemapped = doLightPerezLightDistribution1FContinuousRemap(offsetM, m, indexM);
		final float cRemapped = doLightPerezLightDistribution1FContinuousRemap(offsetC, c, indexC);
		
		point2FSet(isUV ? mRemapped : cRemapped, isUV ? cRemapped : mRemapped);
	}
	
	@SuppressWarnings("unused")
	private void doLightPerezLightDistribution2FDiscreteRemap(final int offset, final float u, final float v) {
		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int indexM = doLightPerezLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int indexC = doLightPerezLightDistribution1FIndex(offsetC, c);
		
		final float mRemapped = doLightPerezLightDistribution1FDiscreteRemap(offsetM, m, indexM);
		final float cRemapped = doLightPerezLightDistribution1FDiscreteRemap(offsetC, c, indexC);
		
		point2FSet(isUV ? mRemapped : cRemapped, isUV ? cRemapped : mRemapped);
	}
	
	private void doLightPerezLightEvaluateRadianceEmitted() {
		doLightPerezLightTransformToObjectSpace(ray3FGetDirectionComponent1(), ray3FGetDirectionComponent2(), ray3FGetDirectionComponent3());
		doLightPerezLightRadianceSky(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		color3FLHSSetMinimumTo0(color3FLHSGetR(), color3FLHSGetG(), color3FLHSGetB());
	}
	
	private void doLightPerezLightPower() {
		final float radius = doLightPerezLightGetRadius();
		
		final float resultFactor = PI * radius * radius;
		
		vector3FSetDirectionSpherical2(0.5F, 0.5F);
		
		doLightPerezLightRadianceSky(vector3FGetComponent1(), vector3FGetComponent2(), vector3FGetComponent3());
		
		color3FLHSSet(color3FLHSGetR() * resultFactor, color3FLHSGetG() * resultFactor, color3FLHSGetB() * resultFactor);
	}
	
	private void doLightPerezLightRadianceSky(final float directionX, final float directionY, final float directionZ) {
		if(directionZ < 0.0F) {
			color3FLHSSet(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		vector3FSetNormalize(directionX, directionY, max(directionZ, 0.001F));
		
		final float directionSaturatedX = vector3FGetComponent1();
		final float directionSaturatedY = vector3FGetComponent2();
		final float directionSaturatedZ = vector3FGetComponent3();
		
		final int offset = lightGetOffset();
		
		final float sunDirectionX = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 0];
		final float sunDirectionY = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 1];
		final float sunDirectionZ = this.lightPerezLightArray[offset + PerezLight.ARRAY_OFFSET_SUN_DIRECTION + 2];
		
		final float theta = acos(saturateF(directionSaturatedZ, -1.0F, 1.0F));
		final float gamma = acos(saturateF(vector3FDotProduct(directionSaturatedX, directionSaturatedY, directionSaturatedZ, sunDirectionX, sunDirectionY, sunDirectionZ), -1.0F, 1.0F));
		final float relativeLuminance = doLightPerezLightCalculatePerezFunctionRelativeLuminance(theta, gamma) * 1.0e-4F;
		final float x = doLightPerezLightCalculatePerezFunctionX(theta, gamma);
		final float y = doLightPerezLightCalculatePerezFunctionY(theta, gamma);
		
//		ChromaticSpectralCurveF.getColorXYZ(float, float):
		final float m1 = (-1.3515F - 1.7703F  * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		final float m2 = (+0.03F   - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		
		final float colorX0 =  95.6824722290F + m1 *  1.7548348904F + m2 * +1.9917192459F;
		final float colorY0 =  99.7036819458F + m1 *  1.8025954962F + m2 * +0.7136800885F;
		final float colorZ0 = 115.5248031616F + m1 * 32.5218048096F + m2 * -2.1984319687F;
		
//		Multiply by the relative luminance and divide by Y:
		final float colorX1 = colorX0 * relativeLuminance / colorY0;
		final float colorY1 = relativeLuminance;
		final float colorZ1 = colorZ0 * relativeLuminance / colorY0;
		
//		Color3F.convertXYZToRGBUsingPBRT(Color3F):
		final float r = +3.240479F * colorX1 - 1.537150F * colorY1 - 0.498535F * colorZ1;
		final float g = -0.969256F * colorX1 + 1.875991F * colorY1 + 0.041556F * colorZ1;
		final float b = +0.055648F * colorX1 - 0.204043F * colorY1 + 1.057311F * colorZ1;
		
		color3FLHSSet(r, g, b);
	}
	
	private void doLightPerezLightTransformToObjectSpace(final float directionX, final float directionY, final float directionZ) {
		final int offset = lightGetOffset();
		final int offsetWorldToObject = offset + PerezLight.ARRAY_OFFSET_WORLD_TO_OBJECT;
		
		final float element11 = this.lightPerezLightArray[offsetWorldToObject +  0];
		final float element12 = this.lightPerezLightArray[offsetWorldToObject +  1];
		final float element13 = this.lightPerezLightArray[offsetWorldToObject +  2];
		final float element21 = this.lightPerezLightArray[offsetWorldToObject +  4];
		final float element22 = this.lightPerezLightArray[offsetWorldToObject +  5];
		final float element23 = this.lightPerezLightArray[offsetWorldToObject +  6];
		final float element31 = this.lightPerezLightArray[offsetWorldToObject +  8];
		final float element32 = this.lightPerezLightArray[offsetWorldToObject +  9];
		final float element33 = this.lightPerezLightArray[offsetWorldToObject + 10];
		
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, directionX, directionY, directionZ);
	}
	
	private void doLightPerezLightTransformToWorldSpace(final float directionX, final float directionY, final float directionZ) {
		final int offset = lightGetOffset();
		final int offsetObjectToWorld = offset + PerezLight.ARRAY_OFFSET_OBJECT_TO_WORLD;
		
		final float element11 = this.lightPerezLightArray[offsetObjectToWorld +  0];
		final float element12 = this.lightPerezLightArray[offsetObjectToWorld +  1];
		final float element13 = this.lightPerezLightArray[offsetObjectToWorld +  2];
		final float element21 = this.lightPerezLightArray[offsetObjectToWorld +  4];
		final float element22 = this.lightPerezLightArray[offsetObjectToWorld +  5];
		final float element23 = this.lightPerezLightArray[offsetObjectToWorld +  6];
		final float element31 = this.lightPerezLightArray[offsetObjectToWorld +  8];
		final float element32 = this.lightPerezLightArray[offsetObjectToWorld +  9];
		final float element33 = this.lightPerezLightArray[offsetObjectToWorld + 10];
		
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, directionX, directionY, directionZ);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Light - PointLight //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightPointLightIsMatchingID(final int id) {
		return id == PointLight.ID;
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightPointLightIsUsingDeltaDistribution() {
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean doLightPointLightSampleRadianceIncoming(final float u, final float v) {
		final float intensityR = doLightPointLightGetIntensityR();
		final float intensityG = doLightPointLightGetIntensityG();
		final float intensityB = doLightPointLightGetIntensityB();
		
		final float positionX = doLightPointLightGetPositionX();
		final float positionY = doLightPointLightGetPositionY();
		final float positionZ = doLightPointLightGetPositionZ();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float distanceSquared = point3FDistanceSquared(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ, positionX, positionY, positionZ);
		
		final float incomingX = positionX - surfaceIntersectionPointX;
		final float incomingY = positionY - surfaceIntersectionPointY;
		final float incomingZ = positionZ - surfaceIntersectionPointZ;
		final float incomingLengthReciprocal = vector3FLengthReciprocal(incomingX, incomingY, incomingZ);
		final float incomingNormalizedX = incomingX * incomingLengthReciprocal;
		final float incomingNormalizedY = incomingY * incomingLengthReciprocal;
		final float incomingNormalizedZ = incomingZ * incomingLengthReciprocal;
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		final float resultR = intensityR / distanceSquared;
		final float resultG = intensityG / distanceSquared;
		final float resultB = intensityB / distanceSquared;
		
		lightSampleSetIncoming(incomingNormalizedX, incomingNormalizedY, incomingNormalizedZ);
		lightSampleSetPoint(positionX, positionY, positionZ);
		lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
		lightSampleSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	@SuppressWarnings({"static-method", "unused"})
	private float doLightPointLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		return 0.0F;
	}
	
	private float doLightPointLightGetIntensityB() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_INTENSITY + 2];
	}
	
	private float doLightPointLightGetIntensityG() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_INTENSITY + 1];
	}
	
	private float doLightPointLightGetIntensityR() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_INTENSITY + 0];
	}
	
	private float doLightPointLightGetPositionX() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_POSITION + 0];
	}
	
	private float doLightPointLightGetPositionY() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_POSITION + 1];
	}
	
	private float doLightPointLightGetPositionZ() {
		return this.lightPointLightArray[lightGetOffset() + PointLight.ARRAY_OFFSET_POSITION + 2];
	}
	
	private void doLightPointLightEvaluateRadianceEmitted() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightPointLightPower() {
		final float intensityR = doLightPointLightGetIntensityR();
		final float intensityG = doLightPointLightGetIntensityG();
		final float intensityB = doLightPointLightGetIntensityB();
		
		final float powerR = intensityR * PI_MULTIPLIED_BY_4;
		final float powerG = intensityG * PI_MULTIPLIED_BY_4;
		final float powerB = intensityB * PI_MULTIPLIED_BY_4;
		
		color3FLHSSet(powerR, powerG, powerB);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Light - SpotLight ///////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightSpotLightIsMatchingID(final int id) {
		return id == SpotLight.ID;
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightSpotLightIsUsingDeltaDistribution() {
		return true;
	}
	
	@SuppressWarnings("unused")
	private boolean doLightSpotLightSampleRadianceIncoming(final float u, final float v) {
		final float intensityR = doLightSpotLightGetIntensityR();
		final float intensityG = doLightSpotLightGetIntensityG();
		final float intensityB = doLightSpotLightGetIntensityB();
		
		final float positionX = doLightSpotLightGetPositionX();
		final float positionY = doLightSpotLightGetPositionY();
		final float positionZ = doLightSpotLightGetPositionZ();
		
		final float surfaceIntersectionPointX = intersectionGetSurfaceIntersectionPointComponent1();
		final float surfaceIntersectionPointY = intersectionGetSurfaceIntersectionPointComponent2();
		final float surfaceIntersectionPointZ = intersectionGetSurfaceIntersectionPointComponent3();
		
		final float incomingX = positionX - surfaceIntersectionPointX;
		final float incomingY = positionY - surfaceIntersectionPointY;
		final float incomingZ = positionZ - surfaceIntersectionPointZ;
		final float incomingLengthReciprocal = vector3FLengthReciprocal(incomingX, incomingY, incomingZ);
		final float incomingNormalizedX = incomingX * incomingLengthReciprocal;
		final float incomingNormalizedY = incomingY * incomingLengthReciprocal;
		final float incomingNormalizedZ = incomingZ * incomingLengthReciprocal;
		
		final float falloff = doLightSpotLightComputeFalloff(-incomingNormalizedX, -incomingNormalizedY, -incomingNormalizedZ);
		
		final float distanceSquared = point3FDistanceSquared(surfaceIntersectionPointX, surfaceIntersectionPointY, surfaceIntersectionPointZ, positionX, positionY, positionZ);
		
		final float probabilityDensityFunctionValue = 1.0F;
		
		final float resultR = intensityR * falloff / distanceSquared;
		final float resultG = intensityG * falloff / distanceSquared;
		final float resultB = intensityB * falloff / distanceSquared;
		
		lightSampleSetIncoming(incomingNormalizedX, incomingNormalizedY, incomingNormalizedZ);
		lightSampleSetPoint(positionX, positionY, positionZ);
		lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
		lightSampleSetResult(resultR, resultG, resultB);
		
		return true;
	}
	
	private float doLightSpotLightComputeFalloff(final float directionX, final float directionY, final float directionZ) {
		final int offset = lightGetOffset() + SpotLight.ARRAY_OFFSET_WORLD_TO_OBJECT;
		
		final float element11 = this.lightSpotLightArray[offset +  0];
		final float element12 = this.lightSpotLightArray[offset +  1];
		final float element13 = this.lightSpotLightArray[offset +  2];
		final float element21 = this.lightSpotLightArray[offset +  4];
		final float element22 = this.lightSpotLightArray[offset +  5];
		final float element23 = this.lightSpotLightArray[offset +  6];
		final float element31 = this.lightSpotLightArray[offset +  8];
		final float element32 = this.lightSpotLightArray[offset +  9];
		final float element33 = this.lightSpotLightArray[offset + 10];
		
		final float cosConeAngle = doLightSpotLightGetCosConeAngle();
		final float cosConeAngleMinusConeAngleDelta = doLightSpotLightGetCosConeAngleMinusConeAngleDelta();
		
		final float directionObjectSpaceX = element11 * directionX + element12 * directionY + element13 * directionZ;
		final float directionObjectSpaceY = element21 * directionX + element22 * directionY + element23 * directionZ;
		final float directionObjectSpaceZ = element31 * directionX + element32 * directionY + element33 * directionZ;
		final float directionObjectSpaceLengthReciprocal = vector3FLengthReciprocal(directionObjectSpaceX, directionObjectSpaceY, directionObjectSpaceZ);
		final float directionObjectSpaceNormalizedX = directionObjectSpaceX * directionObjectSpaceLengthReciprocal;
		final float directionObjectSpaceNormalizedY = directionObjectSpaceY * directionObjectSpaceLengthReciprocal;
		final float directionObjectSpaceNormalizedZ = directionObjectSpaceZ * directionObjectSpaceLengthReciprocal;
		
		final float cosTheta = vector3FCosTheta(directionObjectSpaceNormalizedX, directionObjectSpaceNormalizedY, directionObjectSpaceNormalizedZ);
		
		if(cosTheta < cosConeAngle) {
			return 0.0F;
		}
		
		if(cosTheta >= cosConeAngleMinusConeAngleDelta) {
			return 1.0F;
		}
		
		final float delta = (cosTheta - cosConeAngle) / (cosConeAngleMinusConeAngleDelta - cosConeAngle);
		
		return (delta * delta) * (delta * delta);
	}
	
	@SuppressWarnings({"static-method", "unused"})
	private float doLightSpotLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		return 0.0F;
	}
	
	private float doLightSpotLightGetCosConeAngle() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_COS_CONE_ANGLE];
	}
	
	private float doLightSpotLightGetCosConeAngleMinusConeAngleDelta() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_COS_CONE_ANGLE_MINUS_CONE_ANGLE_DELTA];
	}
	
	private float doLightSpotLightGetIntensityB() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_INTENSITY + 2];
	}
	
	private float doLightSpotLightGetIntensityG() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_INTENSITY + 1];
	}
	
	private float doLightSpotLightGetIntensityR() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_INTENSITY + 0];
	}
	
	private float doLightSpotLightGetPositionX() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_POSITION + 0];
	}
	
	private float doLightSpotLightGetPositionY() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_POSITION + 1];
	}
	
	private float doLightSpotLightGetPositionZ() {
		return this.lightSpotLightArray[lightGetOffset() + SpotLight.ARRAY_OFFSET_POSITION + 2];
	}
	
	private void doLightSpotLightEvaluateRadianceEmitted() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightSpotLightPower() {
		final float intensityR = doLightSpotLightGetIntensityR();
		final float intensityG = doLightSpotLightGetIntensityG();
		final float intensityB = doLightSpotLightGetIntensityB();
		
		final float cosConeAngle = doLightSpotLightGetCosConeAngle();
		final float cosConeAngleMinusConeAngleDelta = doLightSpotLightGetCosConeAngleMinusConeAngleDelta();
		
		final float scale = PI_MULTIPLIED_BY_2 * (1.0F - 0.5F * (cosConeAngleMinusConeAngleDelta + cosConeAngle));
		
		final float powerR = intensityR * scale;
		final float powerG = intensityG * scale;
		final float powerB = intensityB * scale;
		
		color3FLHSSet(powerR, powerG, powerB);
	}
}