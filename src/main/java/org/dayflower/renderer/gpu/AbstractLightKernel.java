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
package org.dayflower.renderer.gpu;

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_2_RECIPROCAL;
import static org.dayflower.utility.Floats.PI_MULTIPLIED_BY_4;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import org.dayflower.scene.Light;
import org.dayflower.scene.LightSample;
import org.dayflower.scene.compiler.CompiledLightCache;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.ImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;

/**
 * An {@code AbstractLightKernel} is an abstract extension of the {@link AbstractMaterialKernel} class that adds additional features.
 * <p>
 * The features added are the following:
 * <ul>
 * <li>{@link DiffuseAreaLight}</li>
 * <li>{@link DirectionalLight}</li>
 * <li>{@link ImageLight}</li>
 * <li>{@link PerezLight}</li>
 * <li>{@link PointLight}</li>
 * <li>{@link SpotLight}</li>
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
	 * A {@code float[]} that contains {@link DiffuseAreaLight} instances.
	 */
	protected float[] lightDiffuseAreaLightArray;
	
	/**
	 * A {@code float[]} that contains {@link DirectionalLight} instances.
	 */
	protected float[] lightDirectionalLightArray;
	
	/**
	 * A {@code float[]} that contains {@link ImageLight} instances.
	 */
	protected float[] lightImageLightArray;
	
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
	 * The {@link DiffuseAreaLight} count.
	 */
	protected int lightDiffuseAreaLightCount;
	
	/**
	 * The {@link DirectionalLight} count.
	 */
	protected int lightDirectionalLightCount;
	
	/**
	 * The {@link ImageLight} count.
	 */
	protected int lightImageLightCount;
	
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
	 * An {@code int[]} that contains an offset lookup table for {@link ImageLight} instances in {@link #lightImageLightArray}.
	 */
	protected int[] lightImageLightOffsetArray;
	
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
		this.lightDiffuseAreaLightArray = new float[1];
		this.lightDiffuseAreaLightCount = 0;
		this.lightDirectionalLightArray = new float[1];
		this.lightDirectionalLightCount = 0;
		this.lightImageLightArray = new float[1];
		this.lightImageLightCount = 0;
		this.lightImageLightOffsetArray = new int[1];
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
	// Light ///////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current {@link Light} instance is an {@link AreaLight} instance, {@code false} otherwise.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @return {@code true} if, and only if, the current {@code Light} instance is an {@code AreaLight} instance, {@code false} otherwise
	 */
	protected final boolean lightIsAreaLight() {
		final int id = lightGetID();
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current {@link Light} instance is using a delta distribution, {@code false} otherwise.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called.
	 * 
	 * @return {@code true} if, and only if, the current {@code Light} instance is using a delta distribution, {@code false} otherwise
	 */
	protected final boolean lightIsUsingDeltaDistribution() {
		final int id = lightGetID();
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			return doLightDiffuseAreaLightIsUsingDeltaDistribution();
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightIsUsingDeltaDistribution();
		} else if(doLightImageLightIsMatchingID(id)) {
			return doLightImageLightIsUsingDeltaDistribution();
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
	 * @param rayDirectionX the X-coordinate of the ray direction
	 * @param rayDirectionY the Y-coordinate of the ray direction
	 * @param rayDirectionZ the Z-coordinate of the ray direction
	 * @return {@code true} if, and only if, a sample was created, {@code false} otherwise
	 */
	protected final boolean lightSampleRadianceIncoming(final float u, final float v, final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int id = lightGetID();
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			return doLightDiffuseAreaLightSampleRadianceIncoming(u, v);
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightSampleRadianceIncoming(u, v);
		} else if(doLightImageLightIsMatchingID(id)) {
			return doLightImageLightSampleRadianceIncoming(u, v);
		} else if(doLightPerezLightIsMatchingID(id)) {
			return doLightPerezLightSampleRadianceIncoming(u, v, rayDirectionX, rayDirectionY, rayDirectionZ);
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
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			return doLightDiffuseAreaLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
		} else if(doLightImageLightIsMatchingID(id)) {
			return doLightImageLightEvaluateProbabilityDensityFunctionRadianceIncoming(incomingX, incomingY, incomingZ);
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
	 * Returns the absolute offset given the ID {@code id} and the relative offset {@code offset}.
	 * 
	 * @param id the ID
	 * @param offset the relative offset
	 * @return the absolute offset given the ID {@code id} and the relative offset {@code offset}
	 */
	protected final int lightToOffset(final int id, final int offset) {
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			return doLightDiffuseAreaLightToOffset(offset);
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			return doLightDirectionalLightToOffset(offset);
		} else if(doLightImageLightIsMatchingID(id)) {
			return doLightImageLightToOffset(offset);
		} else if(doLightPerezLightIsMatchingID(id)) {
			return doLightPerezLightToOffset(offset);
		} else if(doLightPointLightIsMatchingID(id)) {
			return doLightPointLightToOffset(offset);
		} else if(doLightSpotLightIsMatchingID(id)) {
			return doLightSpotLightToOffset(offset);
		} else {
			return 0;
		}
	}
	
	/**
	 * Evaluates the radiance emitted along the current ray using the current {@link Light} instance.
	 * <p>
	 * This method assumes the method {@link #lightSet(int, int)} has been called and that the array {@link AbstractGeometryKernel#ray3FArray_$private$8 ray3FArray_$private$8} contains a ray.
	 * <p>
	 * The result will be set using {@link #color3FLHSSet(float, float, float)}.
	 * <p>
	 * To retrieve the color components of the result, the methods {@link #color3FLHSGetComponent1()}, {@link #color3FLHSGetComponent2()} or {@link #color3FLHSGetComponent3()} may be used.
	 * 
	 * @param rayDirectionX the X-coordinate of the ray direction
	 * @param rayDirectionY the Y-coordinate of the ray direction
	 * @param rayDirectionZ the Z-coordinate of the ray direction
	 */
	protected final void lightEvaluateRadianceEmitted(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int id = lightGetID();
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			doLightDiffuseAreaLightEvaluateRadianceEmitted();
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			doLightDirectionalLightEvaluateRadianceEmitted();
		} else if(doLightImageLightIsMatchingID(id)) {
			doLightImageLightEvaluateRadianceEmitted(rayDirectionX, rayDirectionY, rayDirectionZ);
		} else if(doLightPerezLightIsMatchingID(id)) {
			doLightPerezLightEvaluateRadianceEmitted(rayDirectionX, rayDirectionY, rayDirectionZ);
		} else if(doLightPointLightIsMatchingID(id)) {
			doLightPointLightEvaluateRadianceEmitted();
		} else if(doLightSpotLightIsMatchingID(id)) {
			doLightSpotLightEvaluateRadianceEmitted();
		}
	}
	
	/**
	 * Evaluates the radiance emitted along the current ray using all {@link Light} instances for which this is supported.
	 * 
	 * @param rayDirectionX the X-coordinate of the ray direction
	 * @param rayDirectionY the Y-coordinate of the ray direction
	 * @param rayDirectionZ the Z-coordinate of the ray direction
	 */
	protected final void lightEvaluateRadianceEmittedAll(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int lightImageLightCount = this.lightImageLightCount;
		final int lightPerezLightCount = this.lightPerezLightCount;
		
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		for(int i = 0; i < lightImageLightCount; i++) {
			final int lightImageLightArrayOffset = this.lightImageLightOffsetArray[i];
			
			lightSet(ImageLight.ID, lightImageLightArrayOffset);
			lightEvaluateRadianceEmitted(rayDirectionX, rayDirectionY, rayDirectionZ);
			
			radianceR += color3FLHSGetR();
			radianceG += color3FLHSGetG();
			radianceB += color3FLHSGetB();
		}
		
		for(int i = 0; i < lightPerezLightCount; i++) {
			final int lightPerezLightArrayOffset = this.lightPerezLightOffsetArray[i];
			
			lightSet(PerezLight.ID, lightPerezLightArrayOffset);
			lightEvaluateRadianceEmitted(rayDirectionX, rayDirectionY, rayDirectionZ);
			
			radianceR += color3FLHSGetR();
			radianceG += color3FLHSGetG();
			radianceB += color3FLHSGetB();
		}
		
		color3FLHSSet(radianceR, radianceG, radianceB);
	}
	
	/**
	 * Evaluates the radiance emitted from some surface intersection in a given direction.
	 * 
	 * @param surfaceNormalX the X-component of the surface normal of the surface intersection
	 * @param surfaceNormalY the Y-component of the surface normal of the surface intersection
	 * @param surfaceNormalZ the Z-component of the surface normal of the surface intersection
	 * @param directionX the X-component of the direction the radiance is emitted
	 * @param directionY the Y-component of the direction the radiance is emitted
	 * @param directionZ the Z-component of the direction the radiance is emitted
	 */
	protected final void lightEvaluateRadianceEmittedAreaLight(final float surfaceNormalX, final float surfaceNormalY, final float surfaceNormalZ, final float directionX, final float directionY, final float directionZ) {
		final int id = lightGetID();
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			doLightDiffuseAreaLightEvaluateRadianceEmittedAreaLight(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ);
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
		
		if(doLightDiffuseAreaLightIsMatchingID(id)) {
			doLightDiffuseAreaLightPower();
		} else if(doLightDirectionalLightIsMatchingID(id)) {
			doLightDirectionalLightPower();
		} else if(doLightImageLightIsMatchingID(id)) {
			doLightImageLightPower();
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
	// Light - DiffuseAreaLight ////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightDiffuseAreaLightIsMatchingID(final int id) {
		return id == DiffuseAreaLight.ID;
	}
	
	private boolean doLightDiffuseAreaLightIsTwoSided() {
		return !checkIsZero(this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_IS_TWO_SIDED]);
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightDiffuseAreaLightIsUsingDeltaDistribution() {
		return false;
	}
	
	private boolean doLightDiffuseAreaLightSampleRadianceIncoming(final float u, final float v) {
		final int offset = lightGetOffset();
		
		final float objectToWorldElement11 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  0];
		final float objectToWorldElement12 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  1];
		final float objectToWorldElement13 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  2];
		final float objectToWorldElement14 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  3];
		final float objectToWorldElement21 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  4];
		final float objectToWorldElement22 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  5];
		final float objectToWorldElement23 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  6];
		final float objectToWorldElement24 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  7];
		final float objectToWorldElement31 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  8];
		final float objectToWorldElement32 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  9];
		final float objectToWorldElement33 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 10];
		final float objectToWorldElement34 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 11];
		final float objectToWorldElement41 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 12];
		final float objectToWorldElement42 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 13];
		final float objectToWorldElement43 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 14];
		final float objectToWorldElement44 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 15];
		
		final float worldToObjectElement11 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  0];
		final float worldToObjectElement12 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  1];
		final float worldToObjectElement13 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  2];
		final float worldToObjectElement14 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  3];
		final float worldToObjectElement21 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  4];
		final float worldToObjectElement22 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  5];
		final float worldToObjectElement23 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  6];
		final float worldToObjectElement24 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  7];
		final float worldToObjectElement31 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  8];
		final float worldToObjectElement32 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  9];
		final float worldToObjectElement33 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 10];
		final float worldToObjectElement34 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 11];
		final float worldToObjectElement41 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 12];
		final float worldToObjectElement42 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 13];
		final float worldToObjectElement43 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 14];
		final float worldToObjectElement44 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 15];
		
		intersectionLHSMoveToRHS();
		intersectionRHSTransform(worldToObjectElement11, worldToObjectElement12, worldToObjectElement13, worldToObjectElement14, worldToObjectElement21, worldToObjectElement22, worldToObjectElement23, worldToObjectElement24, worldToObjectElement31, worldToObjectElement32, worldToObjectElement33, worldToObjectElement34, worldToObjectElement41, worldToObjectElement42, worldToObjectElement43, worldToObjectElement44, objectToWorldElement11, objectToWorldElement12, objectToWorldElement13, objectToWorldElement21, objectToWorldElement22, objectToWorldElement23, objectToWorldElement31, objectToWorldElement32, objectToWorldElement33);
		
		final float probabilityDensityFunctionValue = shape3FSphere3FSampleRHS(u, v);
		
		if(checkIsFinite(probabilityDensityFunctionValue) && probabilityDensityFunctionValue > 0.0F) {
			final float pointOSX = point3FGetX();
			final float pointOSY = point3FGetY();
			final float pointOSZ = point3FGetZ();
			
			point3FSetMatrix44FTransformAndDivide(objectToWorldElement11, objectToWorldElement12, objectToWorldElement13, objectToWorldElement14, objectToWorldElement21, objectToWorldElement22, objectToWorldElement23, objectToWorldElement24, objectToWorldElement31, objectToWorldElement32, objectToWorldElement33, objectToWorldElement34, objectToWorldElement41, objectToWorldElement42, objectToWorldElement43, objectToWorldElement44, pointOSX, pointOSY, pointOSZ);
			
			final float pointWSX = point3FGetX();
			final float pointWSY = point3FGetY();
			final float pointWSZ = point3FGetZ();
			
			final float surfaceNormalOSX = vector3FGetX();
			final float surfaceNormalOSY = vector3FGetY();
			final float surfaceNormalOSZ = vector3FGetZ();
			
			vector3FSetMatrix44FTransformTransposeNormalize(worldToObjectElement11, worldToObjectElement12, worldToObjectElement13, worldToObjectElement21, worldToObjectElement22, worldToObjectElement23, worldToObjectElement31, worldToObjectElement32, worldToObjectElement33, surfaceNormalOSX, surfaceNormalOSY, surfaceNormalOSZ);
			
			final float surfaceNormalWSX = vector3FGetX();
			final float surfaceNormalWSY = vector3FGetY();
			final float surfaceNormalWSZ = vector3FGetZ();
			
			final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
			final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
			final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
			
			final float incomingWSX = pointWSX - surfaceIntersectionPointX;
			final float incomingWSY = pointWSY - surfaceIntersectionPointY;
			final float incomingWSZ = pointWSZ - surfaceIntersectionPointZ;
			final float incomingWSLengthReciprocal = vector3FLengthReciprocal(incomingWSX, incomingWSY, incomingWSZ);
			final float incomingWSNormalizedX = incomingWSX * incomingWSLengthReciprocal;
			final float incomingWSNormalizedY = incomingWSY * incomingWSLengthReciprocal;
			final float incomingWSNormalizedZ = incomingWSZ * incomingWSLengthReciprocal;
			
			final float radianceR = doLightDiffuseAreaLightGetRadianceEmittedR();
			final float radianceG = doLightDiffuseAreaLightGetRadianceEmittedG();
			final float radianceB = doLightDiffuseAreaLightGetRadianceEmittedB();
			
			final boolean isTwoSided = doLightDiffuseAreaLightIsTwoSided();
			
			if(isTwoSided || vector3FDotProduct(surfaceNormalWSX, surfaceNormalWSY, surfaceNormalWSZ, -incomingWSNormalizedX, -incomingWSNormalizedY, -incomingWSNormalizedZ) > 0.0F) {
				lightSampleSetIncoming(incomingWSNormalizedX, incomingWSNormalizedY, incomingWSNormalizedZ);
				lightSampleSetPoint(pointWSX, pointWSY, pointWSZ);
				lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
				lightSampleSetResult(radianceR, radianceG, radianceB);
				
				return true;
			}
			
			return false;
		}
		
		return false;
	}
	
	private float doLightDiffuseAreaLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		final int offset = lightGetOffset();
		
		final float objectToWorldElement11 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  0];
		final float objectToWorldElement12 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  1];
		final float objectToWorldElement13 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  2];
		final float objectToWorldElement21 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  4];
		final float objectToWorldElement22 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  5];
		final float objectToWorldElement23 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  6];
		final float objectToWorldElement31 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  8];
		final float objectToWorldElement32 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  9];
		final float objectToWorldElement33 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 10];
		
		final float worldToObjectElement11 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  0];
		final float worldToObjectElement12 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  1];
		final float worldToObjectElement13 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  2];
		final float worldToObjectElement14 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  3];
		final float worldToObjectElement21 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  4];
		final float worldToObjectElement22 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  5];
		final float worldToObjectElement23 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  6];
		final float worldToObjectElement24 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  7];
		final float worldToObjectElement31 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  8];
		final float worldToObjectElement32 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  9];
		final float worldToObjectElement33 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 10];
		final float worldToObjectElement34 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 11];
		final float worldToObjectElement41 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 12];
		final float worldToObjectElement42 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 13];
		final float worldToObjectElement43 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 14];
		final float worldToObjectElement44 = this.lightDiffuseAreaLightArray[offset + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 15];
		
		vector3FSetMatrix44FTransform(worldToObjectElement11, worldToObjectElement12, worldToObjectElement13, worldToObjectElement21, worldToObjectElement22, worldToObjectElement23, worldToObjectElement31, worldToObjectElement32, worldToObjectElement33, incomingX, incomingY, incomingZ);
		
		final float incomingOSX = vector3FGetX();
		final float incomingOSY = vector3FGetY();
		final float incomingOSZ = vector3FGetZ();
		
		intersectionLHSMoveToRHS();
		intersectionRHSTransform(worldToObjectElement11, worldToObjectElement12, worldToObjectElement13, worldToObjectElement14, worldToObjectElement21, worldToObjectElement22, worldToObjectElement23, worldToObjectElement24, worldToObjectElement31, worldToObjectElement32, worldToObjectElement33, worldToObjectElement34, worldToObjectElement41, worldToObjectElement42, worldToObjectElement43, worldToObjectElement44, objectToWorldElement11, objectToWorldElement12, objectToWorldElement13, objectToWorldElement21, objectToWorldElement22, objectToWorldElement23, objectToWorldElement31, objectToWorldElement32, objectToWorldElement33);
		
		return shape3FSphere3FEvaluateProbabilityDensityFunction(incomingOSX, incomingOSY, incomingOSZ);
	}
	
	private float doLightDiffuseAreaLightGetRadianceEmittedB() {
		return this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 2];
	}
	
	private float doLightDiffuseAreaLightGetRadianceEmittedG() {
		return this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 1];
	}
	
	private float doLightDiffuseAreaLightGetRadianceEmittedR() {
		return this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 0];
	}
	
	private float doLightDiffuseAreaLightGetShapeSurfaceArea() {
		return this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_SURFACE_AREA];
	}
	
	@SuppressWarnings("unused")
	private int doLightDiffuseAreaLightGetShapeID() {
		return (int)(this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_ID]);
	}
	
	@SuppressWarnings("unused")
	private int doLightDiffuseAreaLightGetShapeOffset() {
		return (int)(this.lightDiffuseAreaLightArray[lightGetOffset() + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_OFFSET]);
	}
	
	@SuppressWarnings("static-method")
	private int doLightDiffuseAreaLightToOffset(final int offset) {
		return offset * CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH;
	}
	
	private void doLightDiffuseAreaLightEvaluateRadianceEmitted() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightDiffuseAreaLightEvaluateRadianceEmittedAreaLight(final float surfaceNormalX, final float surfaceNormalY, final float surfaceNormalZ, final float directionX, final float directionY, final float directionZ) {
		final float radianceEmittedR = doLightDiffuseAreaLightGetRadianceEmittedR();
		final float radianceEmittedG = doLightDiffuseAreaLightGetRadianceEmittedG();
		final float radianceEmittedB = doLightDiffuseAreaLightGetRadianceEmittedB();
		
		final boolean isTwoSided = doLightDiffuseAreaLightIsTwoSided();
		final boolean isVisible = isTwoSided || vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, directionX, directionY, directionZ) > 0.0F;
		
		if(isVisible) {
			color3FLHSSet(radianceEmittedR, radianceEmittedG, radianceEmittedB);
		} else {
			color3FLHSSet(0.0F, 0.0F, 0.0F);
		}
	}
	
	private void doLightDiffuseAreaLightPower() {
		final float radianceEmittedR = doLightDiffuseAreaLightGetRadianceEmittedR();
		final float radianceEmittedG = doLightDiffuseAreaLightGetRadianceEmittedG();
		final float radianceEmittedB = doLightDiffuseAreaLightGetRadianceEmittedB();
		
		final float shapeSurfaceArea = doLightDiffuseAreaLightGetShapeSurfaceArea();
		
		final boolean isTwoSided = doLightDiffuseAreaLightIsTwoSided();
		
		final float scale = (isTwoSided ? 2.0F : 1.0F) * shapeSurfaceArea * PI;
		
		final float powerR = radianceEmittedR * scale;
		final float powerG = radianceEmittedG * scale;
		final float powerB = radianceEmittedB * scale;
		
		color3FLHSSet(powerR, powerG, powerB);
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
		
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
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
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 0];
	}
	
	private float doLightDirectionalLightGetDirectionY() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 1];
	}
	
	private float doLightDirectionalLightGetDirectionZ() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 2];
	}
	
	private float doLightDirectionalLightGetRadianceB() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 2];
	}
	
	private float doLightDirectionalLightGetRadianceG() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 1];
	}
	
	private float doLightDirectionalLightGetRadianceR() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 0];
	}
	
	private float doLightDirectionalLightGetRadius() {
		return this.lightDirectionalLightArray[lightGetOffset() + CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIUS];
	}
	
	@SuppressWarnings("static-method")
	private int doLightDirectionalLightToOffset(final int offset) {
		return offset * CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH;
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
	// Light - ImageLight //////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private boolean doLightImageLightIsMatchingID(final int id) {
		return id == ImageLight.ID;
	}
	
	@SuppressWarnings("static-method")
	private boolean doLightImageLightIsUsingDeltaDistribution() {
		return false;
	}
	
	private boolean doLightImageLightSampleRadianceIncoming(final float u, final float v) {
		final int offset = lightGetOffset();
		
		final float radius = doLightImageLightGetRadius(offset);
		
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
		final int offsetDistribution = offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_DISTRIBUTION;
		
		doLightImageLightDistribution2FContinuousRemap(offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_DISTRIBUTION, u, v);
		
		final float sampleRemappedU = point2FGetX();
		final float sampleRemappedV = point2FGetY();
		
		final float probabilityDensityFunctionValueRemapped = doLightImageLightDistribution2FContinuousProbabilityDensityFunctionRemapped(offsetDistribution, sampleRemappedU, sampleRemappedV);
		
		if(checkIsZero(probabilityDensityFunctionValueRemapped)) {
			return false;
		}
		
		vector3FSetDirectionSpherical2(sampleRemappedU, sampleRemappedV);
		
		final float incomingObjectSpaceX = vector3FGetX();
		final float incomingObjectSpaceY = vector3FGetY();
		final float incomingObjectSpaceZ = vector3FGetZ();
		
		doLightImageLightTransformToWorldSpace(offset, incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		final float incomingWorldSpaceX = vector3FGetX();
		final float incomingWorldSpaceY = vector3FGetY();
		final float incomingWorldSpaceZ = vector3FGetZ();
		
		final float sinTheta = sqrt(max(0.0F, 1.0F - incomingObjectSpaceZ * incomingObjectSpaceZ));
		
		if(checkIsZero(sinTheta)) {
			return false;
		}
		
		doLightImageLightRadianceSky(offset, incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
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
	
	private float doLightImageLightDistribution1FContinuousProbabilityDensityFunction(final int offset, final int index) {
		final float functionIntegral = doLightImageLightDistribution1FFunctionIntegral(offset);
		final float function = doLightImageLightDistribution1FFunction(offset, index);
		
		return functionIntegral > 0.0F ? function / functionIntegral : 0.0F;
	}
	
	private float doLightImageLightDistribution1FContinuousRemap(final int offset, final float value, final int index) {
		final int count = doLightImageLightDistribution1FCount(offset);
		
		final float cumulativeDistributionFunction0 = doLightImageLightDistribution1FCumulativeDistributionFunction(offset, index + 0);
		final float cumulativeDistributionFunction1 = doLightImageLightDistribution1FCumulativeDistributionFunction(offset, index + 1);
		
		final float a = cumulativeDistributionFunction1 - cumulativeDistributionFunction0;
		final float b = value - cumulativeDistributionFunction0;
		final float c = a > 0.0F ? b / a : b;
		
		return (index + c) / count;
	}
	
	private float doLightImageLightDistribution1FCumulativeDistributionFunction(final int offset, final int index) {
		return this.lightImageLightArray[offset + 4 + index];
	}
	
	private float doLightImageLightDistribution1FFunction(final int offset, final int index) {
		return this.lightImageLightArray[offset + 4 + index + (int)(this.lightImageLightArray[offset + 1])];
	}
	
	private float doLightImageLightDistribution1FFunctionIntegral(final int offset) {
		return this.lightImageLightArray[offset + 3];
	}
	
	@SuppressWarnings("unused")
	private float doLightImageLightDistribution2FContinuousProbabilityDensityFunction(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightImageLightArray[offset]) != 0;
		final boolean isUV = true;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightImageLightArray[offset + 2]);
		final int indexM = doLightImageLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightImageLightArray[offset + 3 + indexM]);
		final int indexC = doLightImageLightDistribution1FIndex(offsetC, c);
		
		final float probabilityDensityFunctionValueM = doLightImageLightDistribution1FContinuousProbabilityDensityFunction(offsetM, indexM);
		final float probabilityDensityFunctionValueC = doLightImageLightDistribution1FContinuousProbabilityDensityFunction(offsetC, indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightImageLightDistribution2FContinuousProbabilityDensityFunctionRemapped(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightImageLightArray[offset]) != 0;
		final boolean isUV = true;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightImageLightArray[offset + 2]);
		final int countM = doLightImageLightDistribution1FCount(offsetM);
		final int indexM = saturateI((int)(m * countM), 0, countM - 1);
		final int offsetC = offset + (int)(this.lightImageLightArray[offset + 3 + indexM]);
		final int countC = doLightImageLightDistribution1FCount(offsetC);
		final int indexC = saturateI((int)(c * countC), 0, countC - 1);
		
		final float probabilityDensityFunctionValue = doLightImageLightDistribution1FFunction(offsetC, indexC) / doLightImageLightDistribution1FFunctionIntegral(offsetM);
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightImageLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		final int offset = lightGetOffset();
		
		doLightImageLightTransformToObjectSpace(offset, incomingX, incomingY, incomingZ);
		
		final float incomingObjectSpaceX = vector3FGetX();
		final float incomingObjectSpaceY = vector3FGetY();
		final float incomingObjectSpaceZ = vector3FGetZ();
		
		final float sinTheta = vector3FSphericalTheta(incomingObjectSpaceZ);
		
		if(checkIsZero(sinTheta)) {
			return 0.0F;
		}
		
		final float u = vector3FSphericalPhi(incomingObjectSpaceX, incomingObjectSpaceY) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float v = vector3FSphericalTheta(incomingObjectSpaceZ) * PI_RECIPROCAL;
		
		final float probabilityDensityFunctionValue = doLightImageLightDistribution2FContinuousProbabilityDensityFunctionRemapped(lightGetOffset() + CompiledLightCache.IMAGE_LIGHT_OFFSET_DISTRIBUTION, u, v) / (2.0F * PI * PI * sinTheta);
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightImageLightGetAngleRadians(final int offset) {
		return this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_ANGLE_RADIANS];
	}
	
	private float doLightImageLightGetRadius(final int offset) {
		return this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_RADIUS];
	}
	
	private float doLightImageLightGetScaleU(final int offset) {
		return this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_SCALE + 0];
	}
	
	private float doLightImageLightGetScaleV(final int offset) {
		return this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_SCALE + 1];
	}
	
	private int doLightImageLightDistribution1FCount(final int offset) {
		return (int)(this.lightImageLightArray[offset + 2]);
	}
	
	private int doLightImageLightDistribution1FIndex(final int offset, final float value) {
		final int length = (int)(this.lightImageLightArray[offset + 1]);
		
		int currentMinimum = 0;
		int currentLength = length;
		
		while(currentLength > 0) {
			int currentHalf = currentLength >> 1;
			int currentMiddle = currentMinimum + currentHalf;
			
			if(doLightImageLightDistribution1FCumulativeDistributionFunction(offset, currentMiddle) <= value) {
				currentMinimum = currentMiddle + 1;
				currentLength -= currentHalf + 1;
			} else {
				currentLength = currentHalf;
			}
		}
		
		return saturateI(currentMinimum - 1, 0, length - 2);
	}
	
	private int doLightImageLightGetResolutionX(final int offset) {
		return (int)(this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_RESOLUTION_X]);
	}
	
	private int doLightImageLightGetResolutionY(final int offset) {
		return (int)(this.lightImageLightArray[offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_RESOLUTION_Y]);
	}
	
	private int doLightImageLightToOffset(final int offset) {
		return this.lightImageLightOffsetArray[offset];
	}
	
	private void doLightImageLightDistribution2FContinuousRemap(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightImageLightArray[offset]) != 0;
		final boolean isUV = true;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightImageLightArray[offset + 2]);
		final int indexM = doLightImageLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightImageLightArray[offset + 3 + indexM]);
		final int indexC = doLightImageLightDistribution1FIndex(offsetC, c);
		
		final float mRemapped = doLightImageLightDistribution1FContinuousRemap(offsetM, m, indexM);
		final float cRemapped = doLightImageLightDistribution1FContinuousRemap(offsetC, c, indexC);
		
		point2FSet(isUV ? mRemapped : cRemapped, isUV ? cRemapped : mRemapped);
	}
	
	private void doLightImageLightEvaluateRadianceEmitted(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int offset = lightGetOffset();
		
		doLightImageLightTransformToObjectSpace(offset, rayDirectionX, rayDirectionY, rayDirectionZ);
		doLightImageLightRadianceSky(offset, vector3FGetX(), vector3FGetY(), vector3FGetZ());
	}
	
	private void doLightImageLightPower() {
		color3FLHSSet(0.0F, 0.0F, 0.0F);
	}
	
	private void doLightImageLightRadianceSky(final int offset, final float directionX, final float directionY, final float directionZ) {
		final float angleRadians = doLightImageLightGetAngleRadians(offset);
		final float angleRadiansCos = cos(angleRadians);
		final float angleRadiansSin = sin(angleRadians);
		
		final float scaleU = doLightImageLightGetScaleU(offset);
		final float scaleV = doLightImageLightGetScaleV(offset);
		
		final int resolutionX = doLightImageLightGetResolutionX(offset);
		final int resolutionY = doLightImageLightGetResolutionY(offset);
		
		final float textureCoordinatesU = vector3FSphericalPhi(directionX, directionY) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float textureCoordinatesV = vector3FSphericalTheta(directionZ) * PI_RECIPROCAL;
		
		final float textureCoordinatesRotatedU = textureCoordinatesU * angleRadiansCos - textureCoordinatesV * angleRadiansSin;
		final float textureCoordinatesRotatedV = textureCoordinatesV * angleRadiansCos + textureCoordinatesU * angleRadiansSin;
		
		final float textureCoordinatesScaledU = textureCoordinatesRotatedU * scaleU;
		final float textureCoordinatesScaledV = textureCoordinatesRotatedV * scaleV;
		
		final float x = positiveModuloF(textureCoordinatesScaledU * resolutionX - 0.5F, resolutionX);
		final float y = positiveModuloF(textureCoordinatesScaledV * resolutionY - 0.5F, resolutionY);
		
		final int minimumX = (int)(floor(x));
		final int maximumX = (int)(ceil(x));
		
		final int minimumY = (int)(floor(y));
		final int maximumY = (int)(ceil(y));
		
		final int offsetImage = offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_IMAGE;
		final int offsetColor00RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor01RGB = offsetImage + (positiveModuloI(minimumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		final int offsetColor10RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(minimumX, resolutionX));
		final int offsetColor11RGB = offsetImage + (positiveModuloI(maximumY, resolutionY) * resolutionX + positiveModuloI(maximumX, resolutionX));
		
		final int color00RGB = (int)(this.lightImageLightArray[offsetColor00RGB]);
		final int color01RGB = (int)(this.lightImageLightArray[offsetColor01RGB]);
		final int color10RGB = (int)(this.lightImageLightArray[offsetColor10RGB]);
		final int color11RGB = (int)(this.lightImageLightArray[offsetColor11RGB]);
		
		final float tX = x - minimumX;
		final float tY = y - minimumY;
		
		final float r = lerp(lerp(colorRGBIntToRFloat(color00RGB), colorRGBIntToRFloat(color01RGB), tX), lerp(colorRGBIntToRFloat(color10RGB), colorRGBIntToRFloat(color11RGB), tX), tY);
		final float g = lerp(lerp(colorRGBIntToGFloat(color00RGB), colorRGBIntToGFloat(color01RGB), tX), lerp(colorRGBIntToGFloat(color10RGB), colorRGBIntToGFloat(color11RGB), tX), tY);
		final float b = lerp(lerp(colorRGBIntToBFloat(color00RGB), colorRGBIntToBFloat(color01RGB), tX), lerp(colorRGBIntToBFloat(color10RGB), colorRGBIntToBFloat(color11RGB), tX), tY);
		
		color3FLHSSet(r, g, b);
	}
	
	private void doLightImageLightTransformToObjectSpace(final int offset, final float directionX, final float directionY, final float directionZ) {
		final int offsetWorldToObject = offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT;
		
		final float element11 = this.lightImageLightArray[offsetWorldToObject +  0];
		final float element12 = this.lightImageLightArray[offsetWorldToObject +  1];
		final float element13 = this.lightImageLightArray[offsetWorldToObject +  2];
		final float element21 = this.lightImageLightArray[offsetWorldToObject +  4];
		final float element22 = this.lightImageLightArray[offsetWorldToObject +  5];
		final float element23 = this.lightImageLightArray[offsetWorldToObject +  6];
		final float element31 = this.lightImageLightArray[offsetWorldToObject +  8];
		final float element32 = this.lightImageLightArray[offsetWorldToObject +  9];
		final float element33 = this.lightImageLightArray[offsetWorldToObject + 10];
		
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, directionX, directionY, directionZ);
	}
	
	private void doLightImageLightTransformToWorldSpace(final int offset, final float directionX, final float directionY, final float directionZ) {
		final int offsetObjectToWorld = offset + CompiledLightCache.IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD;
		
		final float element11 = this.lightImageLightArray[offsetObjectToWorld +  0];
		final float element12 = this.lightImageLightArray[offsetObjectToWorld +  1];
		final float element13 = this.lightImageLightArray[offsetObjectToWorld +  2];
		final float element21 = this.lightImageLightArray[offsetObjectToWorld +  4];
		final float element22 = this.lightImageLightArray[offsetObjectToWorld +  5];
		final float element23 = this.lightImageLightArray[offsetObjectToWorld +  6];
		final float element31 = this.lightImageLightArray[offsetObjectToWorld +  8];
		final float element32 = this.lightImageLightArray[offsetObjectToWorld +  9];
		final float element33 = this.lightImageLightArray[offsetObjectToWorld + 10];
		
		vector3FSetMatrix44FTransformNormalize(element11, element12, element13, element21, element22, element23, element31, element32, element33, directionX, directionY, directionZ);
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
	
	private boolean doLightPerezLightSampleRadianceIncoming(final float u, final float v, final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int offset = lightGetOffset();
		
		final float sunDirectionObjectSpaceX = doLightPerezLightGetSunDirectionObjectSpaceX(offset);
		final float sunDirectionObjectSpaceY = doLightPerezLightGetSunDirectionObjectSpaceY(offset);
		final float sunDirectionObjectSpaceZ = doLightPerezLightGetSunDirectionObjectSpaceZ(offset);
		
		final float sunDirectionWorldSpaceX = doLightPerezLightGetSunDirectionWorldSpaceX(offset);
		final float sunDirectionWorldSpaceY = doLightPerezLightGetSunDirectionWorldSpaceY(offset);
		final float sunDirectionWorldSpaceZ = doLightPerezLightGetSunDirectionWorldSpaceZ(offset);
		
		final float radius = doLightPerezLightGetRadius(offset);
		
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
		final float surfaceNormalGX = intersectionLHSGetOrthonormalBasisGWX();
		final float surfaceNormalGY = intersectionLHSGetOrthonormalBasisGWY();
		final float surfaceNormalGZ = intersectionLHSGetOrthonormalBasisGWZ();
		final float surfaceNormalGDotRayDirection = vector3FDotProduct(surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		final float surfaceNormalGCorrectlyOrientedX = surfaceNormalGDotRayDirection > 0.0F ? -surfaceNormalGX : surfaceNormalGX;
		final float surfaceNormalGCorrectlyOrientedY = surfaceNormalGDotRayDirection > 0.0F ? -surfaceNormalGY : surfaceNormalGY;
		final float surfaceNormalGCorrectlyOrientedZ = surfaceNormalGDotRayDirection > 0.0F ? -surfaceNormalGZ : surfaceNormalGZ;
		
		final float surfaceNormalSX = intersectionLHSGetOrthonormalBasisSWX();
		final float surfaceNormalSY = intersectionLHSGetOrthonormalBasisSWY();
		final float surfaceNormalSZ = intersectionLHSGetOrthonormalBasisSWZ();
		final float surfaceNormalSDotRayDirection = vector3FDotProduct(surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ, rayDirectionX, rayDirectionY, rayDirectionZ);
		final float surfaceNormalSCorrectlyOrientedX = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSX : surfaceNormalSX;
		final float surfaceNormalSCorrectlyOrientedY = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSY : surfaceNormalSY;
		final float surfaceNormalSCorrectlyOrientedZ = surfaceNormalSDotRayDirection > 0.0F ? -surfaceNormalSZ : surfaceNormalSZ;
		
		final float sunDirectionWorldSpaceDotSurfaceNormalG = vector3FDotProduct(sunDirectionWorldSpaceX, sunDirectionWorldSpaceY, sunDirectionWorldSpaceZ, surfaceNormalGCorrectlyOrientedX, surfaceNormalGCorrectlyOrientedY, surfaceNormalGCorrectlyOrientedZ);
		final float sunDirectionWorldSpaceDotSurfaceNormalS = vector3FDotProduct(sunDirectionWorldSpaceX, sunDirectionWorldSpaceY, sunDirectionWorldSpaceZ, surfaceNormalSCorrectlyOrientedX, surfaceNormalSCorrectlyOrientedY, surfaceNormalSCorrectlyOrientedZ);
		
		final float random = random();
		
		final boolean isSamplingSun = sunDirectionWorldSpaceDotSurfaceNormalG > random && sunDirectionWorldSpaceDotSurfaceNormalS > random;
		
		final int offsetDistribution = offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_DISTRIBUTION;
		
		if(isSamplingSun) {
//			final float incomingObjectSpaceX = sunDirectionObjectSpaceX;
//			final float incomingObjectSpaceY = sunDirectionObjectSpaceY;
//			final float incomingObjectSpaceZ = sunDirectionObjectSpaceZ;
			
			final float incomingWorldSpaceX = sunDirectionWorldSpaceX;
			final float incomingWorldSpaceY = sunDirectionWorldSpaceY;
			final float incomingWorldSpaceZ = sunDirectionWorldSpaceZ;
			
//			final float sinTheta = vector3FSinTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
			
//			if(!checkIsZero(sinTheta)) {
				final float resultR = doLightPerezLightGetSunColorR(offset);
				final float resultG = doLightPerezLightGetSunColorG(offset);
				final float resultB = doLightPerezLightGetSunColorB(offset);
				
				final float pointX = surfaceIntersectionPointX + incomingWorldSpaceX * 2.0F * radius;
				final float pointY = surfaceIntersectionPointY + incomingWorldSpaceY * 2.0F * radius;
				final float pointZ = surfaceIntersectionPointZ + incomingWorldSpaceZ * 2.0F * radius;
				
				final float probabilityDensityFunctionValue = 1.0F;
				
				lightSampleSetIncoming(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
				lightSampleSetPoint(pointX, pointY, pointZ);
				lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
				lightSampleSetResult(resultR, resultG, resultB);
				
				return true;
				
//				final float sampleRemappedU = vector3FSphericalPhi(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_MULTIPLIED_BY_2_RECIPROCAL;
//				final float sampleRemappedV = vector3FSphericalTheta(incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ) * PI_RECIPROCAL;
				
//				final float probabilityDensityFunctionValueRemapped = doLightPerezLightDistribution2FContinuousProbabilityDensityFunctionRemapped(offsetDistribution, sampleRemappedU, sampleRemappedV);
				
//				if(!checkIsZero(probabilityDensityFunctionValueRemapped)) {
//					final float probabilityDensityFunctionValue = probabilityDensityFunctionValueRemapped / (2.0F * PI * PI * sinTheta);
					
//					lightSampleSetIncoming(incomingWorldSpaceX, incomingWorldSpaceY, incomingWorldSpaceZ);
//					lightSampleSetPoint(pointX, pointY, pointZ);
//					lightSampleSetProbabilityDensityFunctionValue(probabilityDensityFunctionValue);
//					lightSampleSetResult(resultR, resultG, resultB);
					
//					return true;
//				}
//			}
		}
		
		doLightPerezLightDistribution2FContinuousRemap(offsetDistribution, u, v);
		
		final float sampleRemappedU = point2FGetX();
		final float sampleRemappedV = point2FGetY();
		
		final float probabilityDensityFunctionValueRemapped = doLightPerezLightDistribution2FContinuousProbabilityDensityFunctionRemapped(offsetDistribution, sampleRemappedU, sampleRemappedV);
		
		if(checkIsZero(probabilityDensityFunctionValueRemapped)) {
			return false;
		}
		
		vector3FSetDirectionSpherical2(sampleRemappedU, sampleRemappedV);
		
		final float incomingObjectSpaceX = vector3FGetX();
		final float incomingObjectSpaceY = vector3FGetY();
		final float incomingObjectSpaceZ = vector3FGetZ();
		
		doLightPerezLightTransformToWorldSpace(offset, incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ);
		
		final float incomingWorldSpaceX = vector3FGetX();
		final float incomingWorldSpaceY = vector3FGetY();
		final float incomingWorldSpaceZ = vector3FGetZ();
		
		final float sinTheta = sqrt(max(0.0F, 1.0F - incomingObjectSpaceZ * incomingObjectSpaceZ));
		
		if(checkIsZero(sinTheta)) {
			return false;
		}
		
		doLightPerezLightRadianceSky(offset, incomingObjectSpaceX, incomingObjectSpaceY, incomingObjectSpaceZ, sunDirectionObjectSpaceX, sunDirectionObjectSpaceY, sunDirectionObjectSpaceZ);
		
		color3FLHSSetMinimumTo0(color3FLHSGetR(), color3FLHSGetG(), color3FLHSGetB());
		
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
	
	private float doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(final int offset, final int index) {
		final float functionIntegral = doLightPerezLightDistribution1FFunctionIntegral(offset);
		final float function = doLightPerezLightDistribution1FFunction(offset, index);
		
		return functionIntegral > 0.0F ? function / functionIntegral : 0.0F;
	}
	
	private float doLightPerezLightDistribution1FContinuousRemap(final int offset, final float value, final int index) {
		final int count = doLightPerezLightDistribution1FCount(offset);
		
		final float cumulativeDistributionFunction0 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 0);
		final float cumulativeDistributionFunction1 = doLightPerezLightDistribution1FCumulativeDistributionFunction(offset, index + 1);
		
		final float a = cumulativeDistributionFunction1 - cumulativeDistributionFunction0;
		final float b = value - cumulativeDistributionFunction0;
		final float c = a > 0.0F ? b / a : b;
		
		return (index + c) / count;
	}
	
	private float doLightPerezLightDistribution1FCumulativeDistributionFunction(final int offset, final int index) {
		return this.lightPerezLightArray[offset + 4 + index];
	}
	
	private float doLightPerezLightDistribution1FFunction(final int offset, final int index) {
		return this.lightPerezLightArray[offset + 4 + index + (int)(this.lightPerezLightArray[offset + 1])];
	}
	
	private float doLightPerezLightDistribution1FFunctionIntegral(final int offset) {
		return this.lightPerezLightArray[offset + 3];
	}
	
	@SuppressWarnings("unused")
	private float doLightPerezLightDistribution2FContinuousProbabilityDensityFunction(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		final boolean isUV = true;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int indexM = doLightPerezLightDistribution1FIndex(offsetM, m);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int indexC = doLightPerezLightDistribution1FIndex(offsetC, c);
		
		final float probabilityDensityFunctionValueM = doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(offsetM, indexM);
		final float probabilityDensityFunctionValueC = doLightPerezLightDistribution1FContinuousProbabilityDensityFunction(offsetC, indexC);
		final float probabilityDensityFunctionValue = probabilityDensityFunctionValueM * probabilityDensityFunctionValueC;
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightPerezLightDistribution2FContinuousProbabilityDensityFunctionRemapped(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		final boolean isUV = true;
		
		final float m = isUV ? u : v;
		final float c = isUV ? v : u;
		
		final int offsetM = offset + (int)(this.lightPerezLightArray[offset + 2]);
		final int countM = doLightPerezLightDistribution1FCount(offsetM);
		final int indexM = saturateI((int)(m * countM), 0, countM - 1);
		final int offsetC = offset + (int)(this.lightPerezLightArray[offset + 3 + indexM]);
		final int countC = doLightPerezLightDistribution1FCount(offsetC);
		final int indexC = saturateI((int)(c * countC), 0, countC - 1);
		
		final float probabilityDensityFunctionValue = doLightPerezLightDistribution1FFunction(offsetC, indexC) / doLightPerezLightDistribution1FFunctionIntegral(offsetM);
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightPerezLightEvaluateProbabilityDensityFunctionRadianceIncoming(final float incomingX, final float incomingY, final float incomingZ) {
		final int offset = lightGetOffset();
		
		doLightPerezLightTransformToObjectSpace(offset, incomingX, incomingY, incomingZ);
		
		final float incomingObjectSpaceX = vector3FGetX();
		final float incomingObjectSpaceY = vector3FGetY();
		final float incomingObjectSpaceZ = vector3FGetZ();
		
		final float sinTheta = vector3FSphericalTheta(incomingObjectSpaceZ);
		
		if(checkIsZero(sinTheta)) {
			return 0.0F;
		}
		
		final float u = vector3FSphericalPhi(incomingObjectSpaceX, incomingObjectSpaceY) * PI_MULTIPLIED_BY_2_RECIPROCAL;
		final float v = vector3FSphericalTheta(incomingObjectSpaceZ) * PI_RECIPROCAL;
		
		final float probabilityDensityFunctionValue = doLightPerezLightDistribution2FContinuousProbabilityDensityFunctionRemapped(lightGetOffset() + CompiledLightCache.PEREZ_LIGHT_OFFSET_DISTRIBUTION, u, v) / (2.0F * PI * PI * sinTheta);
		
		return probabilityDensityFunctionValue;
	}
	
	private float doLightPerezLightGetRadius(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_RADIUS];
	}
	
	private float doLightPerezLightGetSunColorB(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 2];
	}
	
	private float doLightPerezLightGetSunColorG(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 1];
	}
	
	private float doLightPerezLightGetSunColorR(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 0];
	}
	
	private float doLightPerezLightGetSunDirectionObjectSpaceX(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 0];
	}
	
	private float doLightPerezLightGetSunDirectionObjectSpaceY(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 1];
	}
	
	private float doLightPerezLightGetSunDirectionObjectSpaceZ(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 2];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceX(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 0];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceY(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 1];
	}
	
	private float doLightPerezLightGetSunDirectionWorldSpaceZ(final int offset) {
		return this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 2];
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
	
	private int doLightPerezLightToOffset(final int offset) {
		return this.lightPerezLightOffsetArray[offset];
	}
	
	private void doLightPerezLightDistribution2FContinuousRemap(final int offset, final float u, final float v) {
//		final boolean isUV = (int)(this.lightPerezLightArray[offset]) != 0;
		final boolean isUV = true;
		
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
	
	private void doLightPerezLightEvaluateRadianceEmitted(final float rayDirectionX, final float rayDirectionY, final float rayDirectionZ) {
		final int offset = lightGetOffset();
		
		doLightPerezLightTransformToObjectSpace(offset, rayDirectionX, rayDirectionY, rayDirectionZ);
		doLightPerezLightRadianceSky(offset, vector3FGetX(), vector3FGetY(), vector3FGetZ(), doLightPerezLightGetSunDirectionObjectSpaceX(offset), doLightPerezLightGetSunDirectionObjectSpaceY(offset), doLightPerezLightGetSunDirectionObjectSpaceZ(offset));
		
		color3FLHSSetMinimumTo0(color3FLHSGetR(), color3FLHSGetG(), color3FLHSGetB());
	}
	
	private void doLightPerezLightPower() {
		final int offset = lightGetOffset();
		
		final float radius = doLightPerezLightGetRadius(offset);
		
		final float resultFactor = PI * radius * radius;
		
		vector3FSetDirectionSpherical2(0.5F, 0.5F);
		
		doLightPerezLightRadianceSky(offset, vector3FGetX(), vector3FGetY(), vector3FGetZ(), doLightPerezLightGetSunDirectionObjectSpaceX(offset), doLightPerezLightGetSunDirectionObjectSpaceY(offset), doLightPerezLightGetSunDirectionObjectSpaceZ(offset));
		
		color3FLHSSetMinimumTo0(color3FLHSGetR(), color3FLHSGetG(), color3FLHSGetB());
		color3FLHSSet(color3FLHSGetR() * resultFactor, color3FLHSGetG() * resultFactor, color3FLHSGetB() * resultFactor);
	}
	
	private void doLightPerezLightRadianceSky(final int offset, final float directionX, final float directionY, final float directionZ, final float sunDirectionObjectSpaceX, final float sunDirectionObjectSpaceY, final float sunDirectionObjectSpaceZ) {
		if(directionZ < 0.0F) {
			color3FLHSSet(0.0F, 0.0F, 0.0F);
			
			return;
		}
		
		vector3FSetNormalize(directionX, directionY, max(directionZ, 0.001F));
		
		final float directionSaturatedX = vector3FGetX();
		final float directionSaturatedY = vector3FGetY();
		final float directionSaturatedZ = vector3FGetZ();
		
		final float thetaA = acos(saturateF(directionSaturatedZ, -1.0F, 1.0F));
		final float thetaACos = cos(thetaA);
		final float thetaACosReciprocal = 1.0F / thetaACos;
		
		final float turbidity = this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_TURBIDITY];
		
		final float perezRelativeLuminance0 = +0.17872F * turbidity - 1.46303F;
		final float perezRelativeLuminance1 = -0.35540F * turbidity + 0.42749F;
		final float perezRelativeLuminance2 = -0.02266F * turbidity + 5.32505F;
		final float perezRelativeLuminance3 = +0.12064F * turbidity - 2.57705F;
		final float perezRelativeLuminance4 = -0.06696F * turbidity + 0.37027F;
		
		final float perezX0 = -0.01925F * turbidity - 0.25922F;
		final float perezX1 = -0.06651F * turbidity + 0.00081F;
		final float perezX2 = -0.00041F * turbidity + 0.21247F;
		final float perezX3 = -0.06409F * turbidity - 0.89887F;
		final float perezX4 = -0.00325F * turbidity + 0.04517F;
		
		final float perezY0 = -0.01669F * turbidity - 0.26078F;
		final float perezY1 = -0.09495F * turbidity + 0.00921F;
		final float perezY2 = -0.00792F * turbidity + 0.21023F;
		final float perezY3 = -0.04405F * turbidity - 1.65369F;
		final float perezY4 = -0.01092F * turbidity + 0.05291F;
		
		final float zenith0 = this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 0];
		final float zenith1 = this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 1];
		final float zenith2 = this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 2];
		
		final float thetaB = this.lightPerezLightArray[offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_THETA];
		final float thetaBCos = cos(thetaB);
		final float thetaBCosSquared = thetaBCos * thetaBCos;
		
		final float gamma = acos(saturateF(vector3FDotProduct(directionSaturatedX, directionSaturatedY, directionSaturatedZ, sunDirectionObjectSpaceX, sunDirectionObjectSpaceY, sunDirectionObjectSpaceZ), -1.0F, 1.0F));
		final float gammaCos = cos(gamma);
		final float gammaCosSquared = gammaCos * gammaCos;
		
		final float relativeLuminance = (zenith0 * ((1.0F + perezRelativeLuminance0 * exp(perezRelativeLuminance1 * thetaACosReciprocal)) * (1.0F + perezRelativeLuminance2 * exp(perezRelativeLuminance3 * gamma) + perezRelativeLuminance4 * gammaCosSquared)) / ((1.0F + perezRelativeLuminance0 * exp(perezRelativeLuminance1)) * (1.0F + perezRelativeLuminance2 * exp(perezRelativeLuminance3 * thetaB) + perezRelativeLuminance4 * thetaBCosSquared))) * 0.0001F;
		final float x = zenith1 * ((1.0F + perezX0 * exp(perezX1 * thetaACosReciprocal)) * (1.0F + perezX2 * exp(perezX3 * gamma) + perezX4 * gammaCosSquared)) / ((1.0F + perezX0 * exp(perezX1)) * (1.0F + perezX2 * exp(perezX3 * thetaB) + perezX4 * thetaBCosSquared));
		final float y = zenith2 * ((1.0F + perezY0 * exp(perezY1 * thetaACosReciprocal)) * (1.0F + perezY2 * exp(perezY3 * gamma) + perezY4 * gammaCosSquared)) / ((1.0F + perezY0 * exp(perezY1)) * (1.0F + perezY2 * exp(perezY3 * thetaB) + perezY4 * thetaBCosSquared));
		
		final float m1 = (-1.3515F -  1.7703F * x +  5.9114F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		final float m2 = (+0.0300F - 31.4424F * x + 30.0717F * y) / (0.0241F + 0.2562F * x - 0.7341F * y);
		
		final float colorX =  95.6824722290F + m1 *  1.7548348904F + m2 * +1.9917192459F;
		final float colorY =  99.7036819458F + m1 *  1.8025954962F + m2 * +0.7136800885F;
		final float colorZ = 115.5248031616F + m1 * 32.5218048096F + m2 * -2.1984319687F;
		
		final float colorR = colorX * relativeLuminance / colorY;
		final float colorG = relativeLuminance;
		final float colorB = colorZ * relativeLuminance / colorY;
		
		color3FLHSSetConvertXYZToRGB(colorR, colorG, colorB);
	}
	
	private void doLightPerezLightTransformToObjectSpace(final int offset, final float directionX, final float directionY, final float directionZ) {
//		TODO: Find out if the Matrix44F instance should be stored with the PerezLight or not. If it should, may some of its elements be eliminated because they never change?
		
		final int offsetWorldToObject = offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT;
		
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
	
	private void doLightPerezLightTransformToWorldSpace(final int offset, final float directionX, final float directionY, final float directionZ) {
//		TODO: Find out if the Matrix44F instance should be stored with the PerezLight or not. If it should, may some of its elements be eliminated because they never change?
		
		final int offsetObjectToWorld = offset + CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD;
		
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
		
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
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
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 2];
	}
	
	private float doLightPointLightGetIntensityG() {
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 1];
	}
	
	private float doLightPointLightGetIntensityR() {
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 0];
	}
	
	private float doLightPointLightGetPositionX() {
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 0];
	}
	
	private float doLightPointLightGetPositionY() {
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 1];
	}
	
	private float doLightPointLightGetPositionZ() {
		return this.lightPointLightArray[lightGetOffset() + CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 2];
	}
	
	@SuppressWarnings("static-method")
	private int doLightPointLightToOffset(final int offset) {
		return offset * CompiledLightCache.POINT_LIGHT_LENGTH;
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
		
		final float surfaceIntersectionPointX = intersectionLHSGetSurfaceIntersectionPointX();
		final float surfaceIntersectionPointY = intersectionLHSGetSurfaceIntersectionPointY();
		final float surfaceIntersectionPointZ = intersectionLHSGetSurfaceIntersectionPointZ();
		
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
		final int offset = lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT;
		
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
//		final float directionObjectSpaceNormalizedX = directionObjectSpaceX * directionObjectSpaceLengthReciprocal;
//		final float directionObjectSpaceNormalizedY = directionObjectSpaceY * directionObjectSpaceLengthReciprocal;
		final float directionObjectSpaceNormalizedZ = directionObjectSpaceZ * directionObjectSpaceLengthReciprocal;
		
		final float cosTheta = directionObjectSpaceNormalizedZ;
		
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
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_COS_CONE_ANGLE];
	}
	
	private float doLightSpotLightGetCosConeAngleMinusConeAngleDelta() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_COS_CONE_ANGLE_MINUS_CONE_ANGLE_DELTA];
	}
	
	private float doLightSpotLightGetIntensityB() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 2];
	}
	
	private float doLightSpotLightGetIntensityG() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 1];
	}
	
	private float doLightSpotLightGetIntensityR() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 0];
	}
	
	private float doLightSpotLightGetPositionX() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 0];
	}
	
	private float doLightSpotLightGetPositionY() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 1];
	}
	
	private float doLightSpotLightGetPositionZ() {
		return this.lightSpotLightArray[lightGetOffset() + CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 2];
	}
	
	@SuppressWarnings("static-method")
	private int doLightSpotLightToOffset(final int offset) {
		return offset * CompiledLightCache.SPOT_LIGHT_LENGTH;
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