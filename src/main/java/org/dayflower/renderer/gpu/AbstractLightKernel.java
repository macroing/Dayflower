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

import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
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
	 * The {@link DirectionalLight} count.
	 */
	protected int lightDirectionalLightCount;
	
	/**
	 * The {@link LDRImageLight} count.
	 */
	protected int lightLDRImageLightCount;
	
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
	 * An {@code int[]} that contains an offset lookup table for {@link LDRImageLight} instances in {@link #lightLDRImageLightArray}.
	 */
	protected int[] lightLDRImageLightOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractLightKernel} instance.
	 */
	protected AbstractLightKernel() {
		this.lightArray_$private$2 = new int[LIGHT_ARRAY_LENGTH];
		this.lightDirectionalLightArray = new float[1];
		this.lightDirectionalLightCount = 0;
		this.lightLDRImageLightArray = new float[1];
		this.lightLDRImageLightCount = 0;
		this.lightLDRImageLightOffsetArray = new int[1];
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
		} else if(doLightPointLightIsMatchingID(id)) {
			doLightPointLightEvaluateRadianceEmitted();
		} else if(doLightSpotLightIsMatchingID(id)) {
			doLightSpotLightEvaluateRadianceEmitted();
		}
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