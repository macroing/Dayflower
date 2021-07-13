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

import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.pack;
import static org.dayflower.utility.Ints.padding;

import java.lang.reflect.Field;//TODO: Refactor!
import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.sampler.Distribution2F;
import org.dayflower.scene.Light;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.utility.Floats;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledLightCache} contains {@link Light} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledLightCache {
	/**
	 * The length of a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_LENGTH = 40;
	
	/**
	 * The offset for the flag denoted by {@code Is Two-Sided} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_IS_TWO_SIDED = 38;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code Object to World} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD = 0;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Radiance Emitted} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED = 32;
	
	/**
	 * The offset for the ID of the {@link Shape3F} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_ID = 35;
	
	/**
	 * The offset for the offset of the {@link Shape3F} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_OFFSET = 36;
	
	/**
	 * The offset for the surface area of the {@link Shape3F} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_SURFACE_AREA = 37;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link DiffuseAreaLight} instance.
	 */
	public static final int DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT = 16;
	
	/**
	 * The length of a compiled {@link DirectionalLight} instance.
	 */
	public static final int DIRECTIONAL_LIGHT_LENGTH = 8;
	
	/**
	 * The offset for the {@link Vector3F} denoted by {@code Direction} in a compiled {@link DirectionalLight} instance.
	 */
	public static final int DIRECTIONAL_LIGHT_OFFSET_DIRECTION = 3;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Radiance} in a compiled {@link DirectionalLight} instance.
	 */
	public static final int DIRECTIONAL_LIGHT_OFFSET_RADIANCE = 0;
	
	/**
	 * The offset for the radius in a compiled {@link DirectionalLight} instance.
	 */
	public static final int DIRECTIONAL_LIGHT_OFFSET_RADIUS = 6;
	
	/**
	 * The offset for the angle in radians in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_ANGLE_RADIANS = 32;
	
	/**
	 * The offset for the {@link Distribution2F} denoted by {@code Distribution} in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_DISTRIBUTION = 40;
	
	/**
	 * The offset for the image in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_IMAGE = 2352;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code Object to World} in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD = 0;
	
	/**
	 * The offset for the radius in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_RADIUS = 35;
	
	/**
	 * The offset for the resolution of the X-axis in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_X = 36;
	
	/**
	 * The offset for the resolution of the Y-axis in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_Y = 37;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_SCALE = 33;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link LDRImageLight} instance.
	 */
	public static final int L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT = 16;
	
	/**
	 * The offset for the {@link Distribution2F} denoted by {@code Distribution} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_DISTRIBUTION = 61;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code Object to World} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD = 0;
	
	/**
	 * The offset for the Perez relative luminance in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE = 41;
	
	/**
	 * The offset for the Perez X in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_PEREZ_X = 46;
	
	/**
	 * The offset for the Perez Y in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_PEREZ_Y = 51;
	
	/**
	 * The offset for the radius in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_RADIUS = 59;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Sun Color} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_SUN_COLOR = 32;
	
	/**
	 * The offset for the {@link Vector3F} denoted by {@code Sun Direction Object Space} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE = 35;
	
	/**
	 * The offset for the {@link Vector3F} denoted by {@code Sun Direction World Space} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE = 38;
	
	/**
	 * The offset for the theta in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_THETA = 60;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT = 16;
	
	/**
	 * The offset for the zenith in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_ZENITH = 56;
	
	/**
	 * The length of a compiled {@link PointLight} instance.
	 */
	public static final int POINT_LIGHT_LENGTH = 8;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Intensity} in a compiled {@link PointLight} instance.
	 */
	public static final int POINT_LIGHT_OFFSET_INTENSITY = 0;
	
	/**
	 * The offset for the {@link Point3F} denoted by {@code Position} in a compiled {@link PointLight} instance.
	 */
	public static final int POINT_LIGHT_OFFSET_POSITION = 3;
	
	/**
	 * The length of a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_LENGTH = 24;
	
	/**
	 * The offset for the cosine of the cone angle in a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_OFFSET_COS_CONE_ANGLE = 22;
	
	/**
	 * The offset for the cosine of the cone angle minus the cone angle delta in a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_OFFSET_COS_CONE_ANGLE_MINUS_CONE_ANGLE_DELTA = 23;
	
	/**
	 * The offset for the {@link Color3F} denoted by {@code Intensity} in a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_OFFSET_INTENSITY = 16;
	
	/**
	 * The offset for the {@link Point3F} denoted by {@code Position} in a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_OFFSET_POSITION = 19;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link SpotLight} instance.
	 */
	public static final int SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT = 0;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] lightDiffuseAreaLightArray;
	private float[] lightDirectionalLightArray;
	private float[] lightLDRImageLightArray;
	private float[] lightPerezLightArray;
	private float[] lightPointLightArray;
	private float[] lightSpotLightArray;
	private int[] lightIDAndOffsetArray;
	private int[] lightLDRImageLightOffsetArray;
	private int[] lightPerezLightOffsetArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledLightCache} instance.
	 */
	public CompiledLightCache() {
		setLightDiffuseAreaLightArray(new float[0]);
		setLightDirectionalLightArray(new float[0]);
		setLightIDAndOffsetArray(new int[0]);
		setLightLDRImageLightArray(new float[0]);
		setLightLDRImageLightOffsetArray(new int[0]);
		setLightPerezLightArray(new float[0]);
		setLightPerezLightOffsetArray(new int[0]);
		setLightPointLightArray(new float[0]);
		setLightSpotLightArray(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link DiffuseAreaLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code DiffuseAreaLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightDiffuseAreaLightArray() {
		return this.lightDiffuseAreaLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link DirectionalLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code DirectionalLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightDirectionalLightArray() {
		return this.lightDirectionalLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link LDRImageLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code LDRImageLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightLDRImageLightArray() {
		return this.lightLDRImageLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PerezLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PerezLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightPerezLightArray() {
		return this.lightPerezLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PointLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PointLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightPointLightArray() {
		return this.lightPointLightArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SpotLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SpotLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getLightSpotLightArray() {
		return this.lightSpotLightArray;
	}
	
	/**
	 * Returns the {@link Light} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code Light} count in this {@code CompiledLightCache} instance
	 */
	public int getLightCount() {
		return getLightDiffuseAreaLightCount() + getLightDirectionalLightCount() + getLightLDRImageLightCount() + getLightPerezLightCount() + getLightPointLightCount() + getLightSpotLightCount();
	}
	
	/**
	 * Returns the {@link DiffuseAreaLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code DiffuseAreaLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightDiffuseAreaLightCount() {
		return Structures.getStructureCount(this.lightDiffuseAreaLightArray, DIFFUSE_AREA_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the {@link DirectionalLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code DirectionalLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightDirectionalLightCount() {
		return Structures.getStructureCount(this.lightDirectionalLightArray, DIRECTIONAL_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the {@link LDRImageLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code LDRImageLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightLDRImageLightCount() {
		return Structures.getStructureCount(this.lightLDRImageLightArray, 8, this.lightLDRImageLightOffsetArray.length);
	}
	
	/**
	 * Returns the {@link PerezLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code PerezLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightPerezLightCount() {
		return Structures.getStructureCount(this.lightPerezLightArray, 8, this.lightPerezLightOffsetArray.length);
	}
	
	/**
	 * Returns the {@link PointLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code PointLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightPointLightCount() {
		return Structures.getStructureCount(this.lightPointLightArray, POINT_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the {@link SpotLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code SpotLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightSpotLightCount() {
		return Structures.getStructureCount(this.lightSpotLightArray, SPOT_LIGHT_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains the ID and offset for all {@link Light} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the ID and offset for all {@code Light} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getLightIDAndOffsetArray() {
		return this.lightIDAndOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link LDRImageLight} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code LDRImageLight} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getLightLDRImageLightOffsetArray() {
		return this.lightLDRImageLightOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link PerezLight} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code PerezLight} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getLightPerezLightOffsetArray() {
		return this.lightPerezLightOffsetArray;
	}
	
	/**
	 * Sets all {@link DiffuseAreaLight} instances in compiled form to {@code lightDiffuseAreaLightArray}.
	 * <p>
	 * If {@code lightDiffuseAreaLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightDiffuseAreaLightArray the {@code DiffuseAreaLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightDiffuseAreaLightArray} is {@code null}
	 */
	public void setLightDiffuseAreaLightArray(final float[] lightDiffuseAreaLightArray) {
		this.lightDiffuseAreaLightArray = Objects.requireNonNull(lightDiffuseAreaLightArray, "lightDiffuseAreaLightArray == null");
	}
	
	/**
	 * Sets all {@link DirectionalLight} instances in compiled form to {@code lightDirectionalLightArray}.
	 * <p>
	 * If {@code lightDirectionalLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightDirectionalLightArray the {@code DirectionalLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightDirectionalLightArray} is {@code null}
	 */
	public void setLightDirectionalLightArray(final float[] lightDirectionalLightArray) {
		this.lightDirectionalLightArray = Objects.requireNonNull(lightDirectionalLightArray, "lightDirectionalLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the ID and offset for all {@link Light} instances to {@code lightIDAndOffsetArray}.
	 * <p>
	 * If {@code lightIDAndOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightIDAndOffsetArray the {@code int[]} that contains the ID and offset for all {@code Light} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightIDAndOffsetArray} is {@code null}
	 */
	public void setLightIDAndOffsetArray(final int[] lightIDAndOffsetArray) {
		this.lightIDAndOffsetArray = Objects.requireNonNull(lightIDAndOffsetArray, "lightIDAndOffsetArray == null");
	}
	
	/**
	 * Sets all {@link LDRImageLight} instances in compiled form to {@code lightLDRImageLightArray}.
	 * <p>
	 * If {@code lightLDRImageLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightLDRImageLightArray the {@code LDRImageLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightLDRImageLightArray} is {@code null}
	 */
	public void setLightLDRImageLightArray(final float[] lightLDRImageLightArray) {
		this.lightLDRImageLightArray = Objects.requireNonNull(lightLDRImageLightArray, "lightLDRImageLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link LDRImageLight} instances to {@code lightLDRImageLightOffsetArray}.
	 * <p>
	 * If {@code lightLDRImageLightOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightLDRImageLightOffsetArray the {@code int[]} that contains the offsets for all {@code LDRImageLight} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightLDRImageLightOffsetArray} is {@code null}
	 */
	public void setLightLDRImageLightOffsetArray(final int[] lightLDRImageLightOffsetArray) {
		this.lightLDRImageLightOffsetArray = Objects.requireNonNull(lightLDRImageLightOffsetArray, "lightLDRImageLightOffsetArray == null");
	}
	
	/**
	 * Sets all {@link PerezLight} instances in compiled form to {@code lightPerezLightArray}.
	 * <p>
	 * If {@code lightPerezLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPerezLightArray the {@code PerezLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightPerezLightArray} is {@code null}
	 */
	public void setLightPerezLightArray(final float[] lightPerezLightArray) {
		this.lightPerezLightArray = Objects.requireNonNull(lightPerezLightArray, "lightPerezLightArray == null");
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link PerezLight} instances to {@code lightPerezLightOffsetArray}.
	 * <p>
	 * If {@code lightPerezLightOffsetArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPerezLightOffsetArray the {@code int[]} that contains the offsets for all {@code PerezLight} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightPerezLightOffsetArray} is {@code null}
	 */
	public void setLightPerezLightOffsetArray(final int[] lightPerezLightOffsetArray) {
		this.lightPerezLightOffsetArray = Objects.requireNonNull(lightPerezLightOffsetArray, "lightPerezLightOffsetArray == null");
	}
	
	/**
	 * Sets all {@link PointLight} instances in compiled form to {@code lightPointLightArray}.
	 * <p>
	 * If {@code lightPointLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightPointLightArray the {@code PointLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightPointLightArray} is {@code null}
	 */
	public void setLightPointLightArray(final float[] lightPointLightArray) {
		this.lightPointLightArray = Objects.requireNonNull(lightPointLightArray, "lightPointLightArray == null");
	}
	
	/**
	 * Sets all {@link SpotLight} instances in compiled form to {@code lightSpotLightArray}.
	 * <p>
	 * If {@code lightSpotLightArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightSpotLightArray the {@code SpotLight} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lightSpotLightArray} is {@code null}
	 */
	public void setLightSpotLightArray(final float[] lightSpotLightArray) {
		this.lightSpotLightArray = Objects.requireNonNull(lightSpotLightArray, "lightSpotLightArray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code diffuseAreaLight} in compiled form.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledLightCache.toArray(diffuseAreaLight, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance
	 * @return a {@code float[]} with {@code diffuseAreaLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public static float[] toArray(final DiffuseAreaLight diffuseAreaLight) {
		return toArray(diffuseAreaLight, shape3F -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with {@code diffuseAreaLight} in compiled form.
	 * <p>
	 * If either {@code diffuseAreaLight} or {@code shapeOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance
	 * @param shapeOffsetFunction a {@code ToIntFunction} that returns the {@link Shape3F} offset
	 * @return a {@code float[]} with {@code diffuseAreaLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code diffuseAreaLight} or {@code shapeOffsetFunction} are {@code null}
	 */
	public static float[] toArray(final DiffuseAreaLight diffuseAreaLight, final ToIntFunction<Shape3F> shapeOffsetFunction) {
		final Color3F radianceEmitted = diffuseAreaLight.getRadianceEmitted();
		
		final Matrix44F objectToWorld = diffuseAreaLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = diffuseAreaLight.getTransform().getWorldToObject();
		
		final Shape3F shape = diffuseAreaLight.getShape();
		
		final boolean isTwoSided = diffuseAreaLight.isTwoSided();
		
		final float[] array = new float[DIFFUSE_AREA_LIGHT_LENGTH];
		
//		Because the DiffuseAreaLight occupy 40/40 positions in five blocks, it should be aligned.
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();	//Block #1
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();	//Block #2
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();	//Block #3
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();	//Block #4
		array[DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 0] = radianceEmitted.getR();			//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 1] = radianceEmitted.getG();			//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 2] = radianceEmitted.getB();			//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_ID] = shape.getID();								//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_OFFSET] = shapeOffsetFunction.applyAsInt(shape);	//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_SURFACE_AREA] = shape.getSurfaceArea();			//Block #5
		array[DIFFUSE_AREA_LIGHT_OFFSET_IS_TWO_SIDED] = isTwoSided ? 1.0F : 0.0F;				//Block #5
		array[39] = 0.0F;																		//Block #5
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code directionalLight} in compiled form.
	 * <p>
	 * If {@code directionalLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param directionalLight a {@link DirectionalLight} instance
	 * @return a {@code float[]} with {@code directionalLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code directionalLight} is {@code null}
	 */
	public static float[] toArray(final DirectionalLight directionalLight) {
		final Color3F radiance = directionalLight.getRadiance();
		
		final Vector3F direction = Vector3F.transform(directionalLight.getTransform().getObjectToWorld(), directionalLight.getDirection());
		
		final float radius = directionalLight.getRadius();
		
		final float[] array = new float[DIRECTIONAL_LIGHT_LENGTH];
		
//		Because the DirectionalLight occupy 8/8 positions in a block, it should be aligned.
		array[DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 0] = radiance.getR();		//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 1] = radiance.getG();		//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 2] = radiance.getB();		//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 0] = direction.getX();	//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 1] = direction.getY();	//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 2] = direction.getZ();	//Block #1
		array[DIRECTIONAL_LIGHT_OFFSET_RADIUS] = radius;					//Block #1
		array[7] = 0.0F;													//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code lDRImageLight} in compiled form.
	 * <p>
	 * If {@code lDRImageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLight an {@link LDRImageLight} instance
	 * @return a {@code float[]} with {@code lDRImageLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageLight} is {@code null}
	 */
	public static float[] toArray(final LDRImageLight lDRImageLight) {
		final AngleF angle = lDRImageLight.getAngle();
		
		final Matrix44F objectToWorld = lDRImageLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = lDRImageLight.getTransform().getWorldToObject();
		
		final Vector2F scale = lDRImageLight.getScale();
		
		final float radius = lDRImageLight.getRadius();
		
		final float[] distribution = lDRImageLight.getDistribution().toArray();
		
		final int resolutionX = lDRImageLight.getResolutionX();
		final int resolutionY = lDRImageLight.getResolutionY();
		
		final int[] image = lDRImageLight.getImage();
		
		final float[] array = new float[getLength(lDRImageLight)];
		
//		Block #1:
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();
		
//		Block #2:
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();
		array[L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();
		
//		Block #3:
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();
		
//		Block #4:
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();
		array[L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();
		
//		Block #5:
		array[L_D_R_IMAGE_LIGHT_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[L_D_R_IMAGE_LIGHT_OFFSET_SCALE + 0] = scale.getU();
		array[L_D_R_IMAGE_LIGHT_OFFSET_SCALE + 1] = scale.getV();
		array[L_D_R_IMAGE_LIGHT_OFFSET_RADIUS] = radius;
		array[L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_X] = resolutionX;
		array[L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_Y] = resolutionY;
		array[38] = 0.0F;
		array[39] = 0.0F;
		
		for(int i = 0; i < distribution.length; i++) {
			array[L_D_R_IMAGE_LIGHT_OFFSET_DISTRIBUTION + i] = distribution[i];
		}
		
		for(int i = 0; i < image.length; i++) {
			array[L_D_R_IMAGE_LIGHT_OFFSET_IMAGE + i] = image[i];
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code perezLight} in compiled form.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance
	 * @return a {@code float[]} with {@code perezLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public static float[] toArray(final PerezLight perezLight) {
		final Color3F sunColor = perezLight.getSunColor();
		
		final Matrix44F objectToWorld = perezLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = perezLight.getTransform().getWorldToObject();
		
		final Vector3F sunDirectionObjectSpace = perezLight.getSunDirectionObjectSpace();
		final Vector3F sunDirectionWorldSpace = perezLight.getSunDirectionWorldSpace();
		
		final double[] perezRelativeLuminance = perezLight.getPerezRelativeLuminance();
		final double[] perezX = perezLight.getPerezX();
		final double[] perezY = perezLight.getPerezY();
		final double[] zenith = perezLight.getZenith();
		
		final float radius = perezLight.getRadius();
		final float theta = perezLight.getTheta();
		
		final float[] distribution = perezLight.getDistribution().toArray();
		
		final float[] array = new float[getLength(perezLight)];
		
//		Block #1:
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();
		
//		Block #2:
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();
		array[PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();
		
//		Block #3:
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();
		
//		Block #4:
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();
		array[PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();
		
//		Block #5:
		array[PEREZ_LIGHT_OFFSET_SUN_COLOR + 0] = sunColor.getR();
		array[PEREZ_LIGHT_OFFSET_SUN_COLOR + 1] = sunColor.getG();
		array[PEREZ_LIGHT_OFFSET_SUN_COLOR + 2] = sunColor.getB();
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 0] = sunDirectionObjectSpace.getX();
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 1] = sunDirectionObjectSpace.getY();
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 2] = sunDirectionObjectSpace.getZ();
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 0] = sunDirectionWorldSpace.getX();
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 1] = sunDirectionWorldSpace.getY();
		
//		Block #6:
		array[PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 2] = sunDirectionWorldSpace.getZ();
		array[PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 0] = toFloat(perezRelativeLuminance[0]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 1] = toFloat(perezRelativeLuminance[1]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 2] = toFloat(perezRelativeLuminance[2]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 3] = toFloat(perezRelativeLuminance[3]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 4] = toFloat(perezRelativeLuminance[4]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_X + 0] = toFloat(perezX[0]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_X + 1] = toFloat(perezX[1]);
		
//		Block #7:
		array[PEREZ_LIGHT_OFFSET_PEREZ_X + 2] = toFloat(perezX[2]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_X + 3] = toFloat(perezX[3]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_X + 4] = toFloat(perezX[4]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_Y + 0] = toFloat(perezY[0]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_Y + 1] = toFloat(perezY[1]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_Y + 2] = toFloat(perezY[2]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_Y + 3] = toFloat(perezY[3]);
		array[PEREZ_LIGHT_OFFSET_PEREZ_Y + 4] = toFloat(perezY[4]);
		
//		Block #8:
		array[PEREZ_LIGHT_OFFSET_ZENITH + 0] = toFloat(zenith[0]);
		array[PEREZ_LIGHT_OFFSET_ZENITH + 1] = toFloat(zenith[1]);
		array[PEREZ_LIGHT_OFFSET_ZENITH + 2] = toFloat(zenith[2]);
		array[PEREZ_LIGHT_OFFSET_RADIUS] = radius;
		array[PEREZ_LIGHT_OFFSET_THETA] = theta;
		
		for(int i = 0; i < distribution.length; i++) {
			array[PEREZ_LIGHT_OFFSET_DISTRIBUTION + i] = distribution[i];
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code pointLight} in compiled form.
	 * <p>
	 * If {@code pointLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLight a {@link PointLight} instance
	 * @return a {@code float[]} with {@code pointLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code pointLight} is {@code null}
	 */
	public static float[] toArray(final PointLight pointLight) {
		final Color3F intensity = pointLight.getIntensity();
		
		final Point3F position = pointLight.getTransform().getPosition();
		
		final float[] array = new float[POINT_LIGHT_LENGTH];
		
//		Because the PointLight occupy 8/8 positions in a block, it should be aligned.
		array[POINT_LIGHT_OFFSET_INTENSITY + 0] = intensity.getR();	//Block #1
		array[POINT_LIGHT_OFFSET_INTENSITY + 1] = intensity.getG();	//Block #1
		array[POINT_LIGHT_OFFSET_INTENSITY + 2] = intensity.getB();	//Block #1
		array[POINT_LIGHT_OFFSET_POSITION + 0] = position.getX();	//Block #1
		array[POINT_LIGHT_OFFSET_POSITION + 1] = position.getY();	//Block #1
		array[POINT_LIGHT_OFFSET_POSITION + 2] = position.getZ();	//Block #1
		array[6] = 0.0F;											//Block #1
		array[7] = 0.0F;											//Block #1
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with {@code spotLight} in compiled form.
	 * <p>
	 * If {@code spotLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param spotLight a {@link SpotLight} instance
	 * @return a {@code float[]} with {@code spotLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code spotLight} is {@code null}
	 */
	public static float[] toArray(final SpotLight spotLight) {
		final Color3F intensity = spotLight.getIntensity();
		
		final Matrix44F objectToWorld = spotLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = spotLight.getTransform().getWorldToObject();
		
		final Point3F position = Point3F.transformAndDivide(objectToWorld, new Point3F());
		
		final float cosConeAngle = spotLight.getCosConeAngle();
		final float cosConeAngleMinusConeAngleDelta = spotLight.getCosConeAngleMinusConeAngleDelta();
		
		final float[] array = new float[SPOT_LIGHT_LENGTH];
		
//		Because the SpotLight occupy 24/24 positions in three blocks, it should be aligned.
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();						//Block #1
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();						//Block #2
		array[SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();						//Block #2
		array[SPOT_LIGHT_OFFSET_INTENSITY + 0] = intensity.getR();											//Block #3
		array[SPOT_LIGHT_OFFSET_INTENSITY + 1] = intensity.getG();											//Block #3
		array[SPOT_LIGHT_OFFSET_INTENSITY + 2] = intensity.getB();											//Block #3
		array[SPOT_LIGHT_OFFSET_POSITION + 0] = position.getX();											//Block #3
		array[SPOT_LIGHT_OFFSET_POSITION + 1] = position.getY();											//Block #3
		array[SPOT_LIGHT_OFFSET_POSITION + 2] = position.getZ();											//Block #3
		array[SPOT_LIGHT_OFFSET_COS_CONE_ANGLE] = cosConeAngle;												//Block #3
		array[SPOT_LIGHT_OFFSET_COS_CONE_ANGLE_MINUS_CONE_ANGLE_DELTA] = cosConeAngleMinusConeAngleDelta;	//Block #3
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form.
	 * <p>
	 * If {@code diffuseAreaLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledLightCache.toLightDiffuseAreaLightArray(diffuseAreaLights, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param diffuseAreaLights a {@code List} of {@code DiffuseAreaLight} instances
	 * @return a {@code float[]} with all {@code DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightDiffuseAreaLightArray(final List<DiffuseAreaLight> diffuseAreaLights) {
		return toLightDiffuseAreaLightArray(diffuseAreaLights, shape3F -> 0);
	}
	
	/**
	 * Returns a {@code float[]} with all {@link DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form.
	 * <p>
	 * If either {@code diffuseAreaLights}, at least one of its elements or {@code shapeOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param diffuseAreaLights a {@code List} of {@code DiffuseAreaLight} instances
	 * @param shapeOffsetFunction a {@code ToIntFunction} that returns the {@link Shape3F} offset
	 * @return a {@code float[]} with all {@code DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code diffuseAreaLights}, at least one of its elements or {@code shapeOffsetFunction} are {@code null}
	 */
	public static float[] toLightDiffuseAreaLightArray(final List<DiffuseAreaLight> diffuseAreaLights, final ToIntFunction<Shape3F> shapeOffsetFunction) {
		return Floats.toArray(diffuseAreaLights, diffuseAreaLight -> toArray(diffuseAreaLight, shapeOffsetFunction));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link DirectionalLight} instances in {@code directionalLights} in compiled form.
	 * <p>
	 * If {@code directionalLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param directionalLights a {@code List} of {@code DirectionalLight} instances
	 * @return a {@code float[]} with all {@code DirectionalLight} instances in {@code directionalLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code directionalLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightDirectionalLightArray(final List<DirectionalLight> directionalLights) {
		return Floats.toArray(directionalLights, directionalLight -> toArray(directionalLight));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link LDRImageLight} instances in {@code lDRImageLights} in compiled form.
	 * <p>
	 * If {@code lDRImageLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLights a {@code List} of {@code LDRImageLight} instances
	 * @return a {@code float[]} with all {@code LDRImageLight} instances in {@code lDRImageLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightLDRImageLightArray(final List<LDRImageLight> lDRImageLights) {
		return Floats.toArray(lDRImageLights, lDRImageLight -> toArray(lDRImageLight));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PerezLight} instances in {@code perezLights} in compiled form.
	 * <p>
	 * If {@code perezLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLights a {@code List} of {@code PerezLight} instances
	 * @return a {@code float[]} with all {@code PerezLight} instances in {@code perezLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code perezLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightPerezLightArray(final List<PerezLight> perezLights) {
		return Floats.toArray(perezLights, perezLight -> toArray(perezLight));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link PointLight} instances in {@code pointLights} in compiled form.
	 * <p>
	 * If {@code pointLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLights a {@code List} of {@code PointLight} instances
	 * @return a {@code float[]} with all {@code PointLight} instances in {@code pointLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code pointLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightPointLightArray(final List<PointLight> pointLights) {
		return Floats.toArray(pointLights, pointLight -> toArray(pointLight));
	}
	
	/**
	 * Returns a {@code float[]} with all {@link SpotLight} instances in {@code spotLights} in compiled form.
	 * <p>
	 * If {@code spotLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param spotLights a {@code List} of {@code SpotLight} instances
	 * @return a {@code float[]} with all {@code SpotLight} instances in {@code spotLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code spotLights} or at least one of its elements are {@code null}
	 */
	public static float[] toLightSpotLightArray(final List<SpotLight> spotLights) {
		return Floats.toArray(spotLights, spotLight -> toArray(spotLight));
	}
	
	/**
	 * Returns the length of {@code lDRImageLight} in compiled form.
	 * <p>
	 * If {@code lDRImageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLight an {@link LDRImageLight} instance
	 * @return the length of {@code lDRImageLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code lDRImageLight} is {@code null}
	 */
	public static int getLength(final LDRImageLight lDRImageLight) {
		final int a = 16 + 16 + 1 + 2 + 1 + 1 + 1;
		final int b = lDRImageLight.getDistribution().toArray().length;
		final int c = lDRImageLight.getResolution();
		
		return a + b + c + padding(a + b + c);
	}
	
	/**
	 * Returns the length of {@code perezLight} in compiled form.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance
	 * @return the length of {@code perezLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public static int getLength(final PerezLight perezLight) {
		final int a = 16 + 16 + 3 + 3 + 3 + 5 + 5 + 5 + 3 + 1 + 1;
		final int b = perezLight.getDistribution().toArray().length;
		
		return a + b + padding(a + b);
	}
	
	/**
	 * Returns an {@code int[]} with the IDs and offsets for all {@link Light} instances in {@code lights} in compiled form.
	 * <p>
	 * If either {@code lights}, at least one of its elements or {@code lightOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lights a {@code List} of {@code Light} instances
	 * @param lightOffsetFunction a {@code ToIntFunction} that returns the {@code Light} offset
	 * @return an {@code int[]} with the IDs and offsets for all {@code Light} instances in {@code lights} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code lights}, at least one of its elements or {@code lightOffsetFunction} are {@code null}
	 */
	public static int[] toLightIDAndOffsetArray(final List<Light> lights, final ToIntFunction<Light> lightOffsetFunction) {
		ParameterArguments.requireNonNullList(lights, "lights");
		
		Objects.requireNonNull(lightOffsetFunction, "lightOffsetFunction == null");
		
		final int[] lightIDAndOffsetArray = new int[lights.size()];
		
		for(int i = 0; i < lights.size(); i++) {
			final Light light = lights.get(i);
			
			lightIDAndOffsetArray[i] = pack(light.getID(), lightOffsetFunction.applyAsInt(light));
		}
		
		return lightIDAndOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link LDRImageLight} instances in {@code lDRImageLights} in compiled form.
	 * <p>
	 * If either {@code lDRImageLights}, at least one of its elements or {@code lDRImageLightOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lDRImageLights a {@code List} of {@code LDRImageLight} instances
	 * @param lDRImageLightOffsetFunction a {@code ToIntFunction} that returns the {@code LDRImageLight} offset
	 * @return an {@code int[]} with the offsets for all {@code LDRImageLight} instances in {@code lDRImageLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code lDRImageLights}, at least one of its elements or {@code lDRImageLightOffsetFunction} are {@code null}
	 */
	public static int[] toLightLDRImageLightOffsetArray(final List<LDRImageLight> lDRImageLights, final ToIntFunction<LDRImageLight> lDRImageLightOffsetFunction) {
		ParameterArguments.requireNonNullList(lDRImageLights, "lDRImageLights");
		
		Objects.requireNonNull(lDRImageLightOffsetFunction, "lDRImageLightOffsetFunction == null");
		
		final int[] lightLDRImageLightOffsetArray = new int[lDRImageLights.size()];
		
		for(int i = 0; i < lDRImageLights.size(); i++) {
			lightLDRImageLightOffsetArray[i] = lDRImageLightOffsetFunction.applyAsInt(lDRImageLights.get(i));
		}
		
		return lightLDRImageLightOffsetArray;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link PerezLight} instances in {@code perezLights} in compiled form.
	 * <p>
	 * If either {@code perezLights}, at least one of its elements or {@code perezLightOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLights a {@code List} of {@code PerezLight} instances
	 * @param perezLightOffsetFunction a {@code ToIntFunction} that returns the {@code PerezLight} offset
	 * @return an {@code int[]} with the offsets for all {@code PerezLight} instances in {@code perezLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code perezLights}, at least one of its elements or {@code perezLightOffsetFunction} are {@code null}
	 */
	public static int[] toLightPerezLightOffsetArray(final List<PerezLight> perezLights, final ToIntFunction<PerezLight> perezLightOffsetFunction) {
		ParameterArguments.requireNonNullList(perezLights, "perezLights");
		
		Objects.requireNonNull(perezLightOffsetFunction, "perezLightOffsetFunction == null");
		
		final int[] lightPerezLightOffsetArray = new int[perezLights.size()];
		
		for(int i = 0; i < perezLights.size(); i++) {
			lightPerezLightOffsetArray[i] = perezLightOffsetFunction.applyAsInt(perezLights.get(i));
		}
		
		return lightPerezLightOffsetArray;
	}
}