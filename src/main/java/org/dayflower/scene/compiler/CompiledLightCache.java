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

import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.pack;
import static org.dayflower.utility.Ints.padding;

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
import org.dayflower.scene.light.ImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

/**
 * A {@code CompiledLightCache} contains {@link Light} instances in compiled form.
 * <p>
 * The {@code Light} implementations that are supported are the following:
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
	 * The offset for the angle in radians in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_ANGLE_RADIANS = 32;
	
	/**
	 * The offset for the {@link Distribution2F} denoted by {@code Distribution} in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_DISTRIBUTION = 40;
	
	/**
	 * The offset for the image in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_IMAGE = 2352;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code Object to World} in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD = 0;
	
	/**
	 * The offset for the radius in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_RADIUS = 35;
	
	/**
	 * The offset for the resolution of the X-axis in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_RESOLUTION_X = 36;
	
	/**
	 * The offset for the resolution of the Y-axis in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_RESOLUTION_Y = 37;
	
	/**
	 * The offset for the {@link Vector2F} instance that represents the scale in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_SCALE = 33;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link ImageLight} instance.
	 */
	public static final int IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT = 16;
	
	/**
	 * The offset for the {@link Distribution2F} denoted by {@code Distribution} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_DISTRIBUTION = 48;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code Object to World} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD = 0;
	
	/**
	 * The offset for the radius in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_RADIUS = 45;
	
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
	public static final int PEREZ_LIGHT_OFFSET_THETA = 46;
	
	/**
	 * The offset for the turbidity in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_TURBIDITY = 41;
	
	/**
	 * The offset for the {@link Matrix44F} denoted by {@code World to Object} in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT = 16;
	
	/**
	 * The offset for the zenith in a compiled {@link PerezLight} instance.
	 */
	public static final int PEREZ_LIGHT_OFFSET_ZENITH = 42;
	
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
	
	private float[] diffuseAreaLights;
	private float[] directionalLights;
	private float[] imageLights;
	private float[] perezLights;
	private float[] pointLights;
	private float[] spotLights;
	private int[] imageLightOffsets;
	private int[] lightIDsAndOffsets;
	private int[] perezLightOffsets;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledLightCache} instance.
	 */
	public CompiledLightCache() {
		setDiffuseAreaLights(new float[0]);
		setDirectionalLights(new float[0]);
		setImageLightOffsets(new int[0]);
		setImageLights(new float[0]);
		setLightIDsAndOffsets(new int[0]);
		setPerezLightOffsets(new int[0]);
		setPerezLights(new float[0]);
		setPointLights(new float[0]);
		setSpotLights(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code diffuseAreaLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code diffuseAreaLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code diffuseAreaLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public boolean removeDiffuseAreaLight(final float[] diffuseAreaLight) {
		final int absoluteOffset = getDiffuseAreaLightOffsetAbsolute(diffuseAreaLight);
		final int relativeOffset = getDiffuseAreaLightOffsetRelative(diffuseAreaLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), DiffuseAreaLight.ID, relativeOffset));
			setDiffuseAreaLights(Arrays.splice(getDiffuseAreaLights(), absoluteOffset, DIFFUSE_AREA_LIGHT_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code directionalLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code directionalLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code directionalLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param directionalLight a {@link DirectionalLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code directionalLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code directionalLight} is {@code null}
	 */
	public boolean removeDirectionalLight(final float[] directionalLight) {
		final int absoluteOffset = getDirectionalLightOffsetAbsolute(directionalLight);
		final int relativeOffset = getDirectionalLightOffsetRelative(directionalLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), DirectionalLight.ID, relativeOffset));
			setDirectionalLights(Arrays.splice(getDirectionalLights(), absoluteOffset, DIRECTIONAL_LIGHT_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code imageLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code imageLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code imageLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code imageLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code imageLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public boolean removeImageLight(final float[] imageLight) {
		final int absoluteOffset = getImageLightOffsetAbsolute(imageLight);
		final int relativeOffset = getImageLightOffsetRelative(imageLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), ImageLight.ID, relativeOffset));
			setImageLightOffsets(Structures.removeStructureOffset(getImageLightOffsets(), absoluteOffset, imageLight.length));
			setImageLights(Arrays.splice(getImageLights(), absoluteOffset, imageLight.length));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code perezLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code perezLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code perezLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code perezLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code perezLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public boolean removePerezLight(final float[] perezLight) {
		final int absoluteOffset = getPerezLightOffsetAbsolute(perezLight);
		final int relativeOffset = getPerezLightOffsetRelative(perezLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), PerezLight.ID, relativeOffset));
			setPerezLightOffsets(Structures.removeStructureOffset(getPerezLightOffsets(), absoluteOffset, perezLight.length));
			setPerezLights(Arrays.splice(getPerezLights(), absoluteOffset, perezLight.length));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code pointLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code pointLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code pointLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param pointLight a {@link PointLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code pointLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code pointLight} is {@code null}
	 */
	public boolean removePointLight(final float[] pointLight) {
		final int absoluteOffset = getPointLightOffsetAbsolute(pointLight);
		final int relativeOffset = getPointLightOffsetRelative(pointLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), PointLight.ID, relativeOffset));
			setPointLights(Arrays.splice(getPointLights(), absoluteOffset, POINT_LIGHT_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code spotLight} from this {@code CompiledLightCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code spotLight} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code spotLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param spotLight a {@link SpotLight} instance in compiled form
	 * @return {@code true} if, and only if, {@code spotLight} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code spotLight} is {@code null}
	 */
	public boolean removeSpotLight(final float[] spotLight) {
		final int absoluteOffset = getSpotLightOffsetAbsolute(spotLight);
		final int relativeOffset = getSpotLightOffsetRelative(spotLight);
		
		if(absoluteOffset != -1 && relativeOffset != -1) {
			setLightIDsAndOffsets(Structures.removeStructureIDAndOffset(getLightIDsAndOffsets(), SpotLight.ID, relativeOffset));
			setSpotLights(Arrays.splice(getSpotLights(), absoluteOffset, SPOT_LIGHT_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link DiffuseAreaLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code DiffuseAreaLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getDiffuseAreaLights() {
		return this.diffuseAreaLights;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link DirectionalLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code DirectionalLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getDirectionalLights() {
		return this.directionalLights;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link ImageLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code ImageLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getImageLights() {
		return this.imageLights;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PerezLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PerezLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getPerezLights() {
		return this.perezLights;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link PointLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code PointLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getPointLights() {
		return this.pointLights;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link SpotLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code SpotLight} instances in compiled form that are associated with this {@code CompiledLightCache} instance
	 */
	public float[] getSpotLights() {
		return this.spotLights;
	}
	
	/**
	 * Adds {@code diffuseAreaLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code diffuseAreaLight}.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance in compiled form
	 * @return the relative offset to {@code diffuseAreaLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public int addDiffuseAreaLight(final float[] diffuseAreaLight) {
		final int relativeOffsetOld = getDiffuseAreaLightOffsetRelative(diffuseAreaLight);
		final int relativeOffsetNew = getDiffuseAreaLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), DiffuseAreaLight.ID, relativeOffsetNew));
		setDiffuseAreaLights(Arrays.merge(getDiffuseAreaLights(), diffuseAreaLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code directionalLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code directionalLight}.
	 * <p>
	 * If {@code directionalLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param directionalLight a {@link DirectionalLight} instance in compiled form
	 * @return the relative offset to {@code directionalLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code directionalLight} is {@code null}
	 */
	public int addDirectionalLight(final float[] directionalLight) {
		final int relativeOffsetOld = getDirectionalLightOffsetRelative(directionalLight);
		final int relativeOffsetNew = getDirectionalLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), DirectionalLight.ID, relativeOffsetNew));
		setDirectionalLights(Arrays.merge(getDirectionalLights(), directionalLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code imageLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code imageLight}.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code imageLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance in compiled form
	 * @return the relative offset to {@code imageLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code imageLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public int addImageLight(final float[] imageLight) {
		final int absoluteOffsetNew = this.imageLights.length;
		final int relativeOffsetOld = getImageLightOffsetRelative(imageLight);
		final int relativeOffsetNew = getImageLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), ImageLight.ID, relativeOffsetNew));
		setImageLightOffsets(Arrays.merge(getImageLightOffsets(), new int[] {absoluteOffsetNew}));
		setImageLights(Arrays.merge(getImageLights(), imageLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code perezLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code perezLight}.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code perezLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance in compiled form
	 * @return the relative offset to {@code perezLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code perezLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public int addPerezLight(final float[] perezLight) {
		final int absoluteOffsetNew = this.perezLights.length;
		final int relativeOffsetOld = getPerezLightOffsetRelative(perezLight);
		final int relativeOffsetNew = getPerezLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), PerezLight.ID, relativeOffsetNew));
		setPerezLightOffsets(Arrays.merge(getPerezLightOffsets(), new int[] {absoluteOffsetNew}));
		setPerezLights(Arrays.merge(getPerezLights(), perezLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code pointLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code pointLight}.
	 * <p>
	 * If {@code pointLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param pointLight a {@link PointLight} instance in compiled form
	 * @return the relative offset to {@code pointLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code pointLight} is {@code null}
	 */
	public int addPointLight(final float[] pointLight) {
		final int relativeOffsetOld = getPointLightOffsetRelative(pointLight);
		final int relativeOffsetNew = getPointLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), PointLight.ID, relativeOffsetNew));
		setPointLights(Arrays.merge(getPointLights(), pointLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code spotLight} to this {@code CompiledLightCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code spotLight}.
	 * <p>
	 * If {@code spotLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param spotLight a {@link SpotLight} instance in compiled form
	 * @return the relative offset to {@code spotLight}
	 * @throws IllegalArgumentException thrown if, and only if, {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code spotLight} is {@code null}
	 */
	public int addSpotLight(final float[] spotLight) {
		final int relativeOffsetOld = getSpotLightOffsetRelative(spotLight);
		final int relativeOffsetNew = getSpotLightCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setLightIDsAndOffsets(Structures.addStructureIDAndOffset(getLightIDsAndOffsets(), SpotLight.ID, relativeOffsetNew));
		setSpotLights(Arrays.merge(getSpotLights(), spotLight));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link DiffuseAreaLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code DiffuseAreaLight} count in this {@code CompiledLightCache} instance
	 */
	public int getDiffuseAreaLightCount() {
		return Structures.getStructureCount(this.diffuseAreaLights, DIFFUSE_AREA_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code diffuseAreaLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance in compiled form
	 * @return the absolute offset of {@code diffuseAreaLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public int getDiffuseAreaLightOffsetAbsolute(final float[] diffuseAreaLight) {
		Objects.requireNonNull(diffuseAreaLight, "diffuseAreaLight == null");
		
		ParameterArguments.requireExactArrayLength(diffuseAreaLight, DIFFUSE_AREA_LIGHT_LENGTH, "diffuseAreaLight");
		
		return Structures.getStructureOffsetAbsolute(this.diffuseAreaLights, diffuseAreaLight);
	}
	
	/**
	 * Returns the relative offset of {@code diffuseAreaLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance in compiled form
	 * @return the relative offset of {@code diffuseAreaLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code diffuseAreaLight.length} is not equal to {@code CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public int getDiffuseAreaLightOffsetRelative(final float[] diffuseAreaLight) {
		Objects.requireNonNull(diffuseAreaLight, "diffuseAreaLight == null");
		
		ParameterArguments.requireExactArrayLength(diffuseAreaLight, DIFFUSE_AREA_LIGHT_LENGTH, "diffuseAreaLight");
		
		return Structures.getStructureOffsetRelative(this.diffuseAreaLights, diffuseAreaLight);
	}
	
	/**
	 * Returns the {@link DirectionalLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code DirectionalLight} count in this {@code CompiledLightCache} instance
	 */
	public int getDirectionalLightCount() {
		return Structures.getStructureCount(this.directionalLights, DIRECTIONAL_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code directionalLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code directionalLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param directionalLight a {@link DirectionalLight} instance in compiled form
	 * @return the absolute offset of {@code directionalLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code directionalLight} is {@code null}
	 */
	public int getDirectionalLightOffsetAbsolute(final float[] directionalLight) {
		Objects.requireNonNull(directionalLight, "directionalLight == null");
		
		ParameterArguments.requireExactArrayLength(directionalLight, DIRECTIONAL_LIGHT_LENGTH, "directionalLight");
		
		return Structures.getStructureOffsetAbsolute(this.directionalLights, directionalLight);
	}
	
	/**
	 * Returns the relative offset of {@code directionalLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code directionalLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param directionalLight a {@link DirectionalLight} instance in compiled form
	 * @return the relative offset of {@code directionalLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code directionalLight.length} is not equal to {@code CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code directionalLight} is {@code null}
	 */
	public int getDirectionalLightOffsetRelative(final float[] directionalLight) {
		Objects.requireNonNull(directionalLight, "directionalLight == null");
		
		ParameterArguments.requireExactArrayLength(directionalLight, DIRECTIONAL_LIGHT_LENGTH, "directionalLight");
		
		return Structures.getStructureOffsetRelative(this.directionalLights, directionalLight);
	}
	
	/**
	 * Returns the {@link ImageLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code ImageLight} count in this {@code CompiledLightCache} instance
	 */
	public int getImageLightCount() {
		return this.imageLightOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code imageLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code imageLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance in compiled form
	 * @return the absolute offset of {@code imageLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code imageLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public int getImageLightOffsetAbsolute(final float[] imageLight) {
		Objects.requireNonNull(imageLight, "imageLight == null");
		
		ParameterArguments.requireExact(imageLight.length % 8, 0, "imageLight.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.imageLights, imageLight, this.imageLightOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code imageLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code imageLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance in compiled form
	 * @return the relative offset of {@code imageLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code imageLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public int getImageLightOffsetRelative(final float[] imageLight) {
		Objects.requireNonNull(imageLight, "imageLight == null");
		
		ParameterArguments.requireExact(imageLight.length % 8, 0, "imageLight.length % 8");
		
		return Structures.getStructureOffsetRelative(this.imageLights, imageLight, this.imageLightOffsets);
	}
	
	/**
	 * Returns the {@link Light} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code Light} count in this {@code CompiledLightCache} instance
	 */
	public int getLightCount() {
		return getDiffuseAreaLightCount() + getDirectionalLightCount() + getImageLightCount() + getPerezLightCount() + getPointLightCount() + getSpotLightCount();
	}
	
	/**
	 * Returns the {@link PerezLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code PerezLight} count in this {@code CompiledLightCache} instance
	 */
	public int getPerezLightCount() {
		return this.perezLightOffsets.length;
	}
	
	/**
	 * Returns the absolute offset of {@code perezLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code perezLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance in compiled form
	 * @return the absolute offset of {@code perezLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code perezLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public int getPerezLightOffsetAbsolute(final float[] perezLight) {
		Objects.requireNonNull(perezLight, "perezLight == null");
		
		ParameterArguments.requireExact(perezLight.length % 8, 0, "perezLight.length % 8");
		
		return Structures.getStructureOffsetAbsolute(this.perezLights, perezLight, this.perezLightOffsets);
	}
	
	/**
	 * Returns the relative offset of {@code perezLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code perezLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code perezLight.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLight a {@link PerezLight} instance in compiled form
	 * @return the relative offset of {@code perezLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code perezLight.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLight} is {@code null}
	 */
	public int getPerezLightOffsetRelative(final float[] perezLight) {
		Objects.requireNonNull(perezLight, "perezLight == null");
		
		ParameterArguments.requireExact(perezLight.length % 8, 0, "perezLight.length % 8");
		
		return Structures.getStructureOffsetRelative(this.perezLights, perezLight, this.perezLightOffsets);
	}
	
	/**
	 * Returns the {@link PointLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code PointLight} count in this {@code CompiledLightCache} instance
	 */
	public int getPointLightCount() {
		return Structures.getStructureCount(this.pointLights, POINT_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code pointLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code pointLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param pointLight a {@link PointLight} instance in compiled form
	 * @return the absolute offset of {@code pointLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code pointLight} is {@code null}
	 */
	public int getPointLightOffsetAbsolute(final float[] pointLight) {
		Objects.requireNonNull(pointLight, "pointLight == null");
		
		ParameterArguments.requireExactArrayLength(pointLight, POINT_LIGHT_LENGTH, "pointLight");
		
		return Structures.getStructureOffsetAbsolute(this.pointLights, pointLight);
	}
	
	/**
	 * Returns the relative offset of {@code pointLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code pointLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param pointLight a {@link PointLight} instance in compiled form
	 * @return the relative offset of {@code pointLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code pointLight.length} is not equal to {@code CompiledLightCache.POINT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code pointLight} is {@code null}
	 */
	public int getPointLightOffsetRelative(final float[] pointLight) {
		Objects.requireNonNull(pointLight, "pointLight == null");
		
		ParameterArguments.requireExactArrayLength(pointLight, POINT_LIGHT_LENGTH, "pointLight");
		
		return Structures.getStructureOffsetRelative(this.pointLights, pointLight);
	}
	
	/**
	 * Returns the {@link SpotLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code SpotLight} count in this {@code CompiledLightCache} instance
	 */
	public int getSpotLightCount() {
		return Structures.getStructureCount(this.spotLights, SPOT_LIGHT_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code spotLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code spotLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param spotLight a {@link SpotLight} instance in compiled form
	 * @return the absolute offset of {@code spotLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code spotLight} is {@code null}
	 */
	public int getSpotLightOffsetAbsolute(final float[] spotLight) {
		Objects.requireNonNull(spotLight, "spotLight == null");
		
		ParameterArguments.requireExactArrayLength(spotLight, SPOT_LIGHT_LENGTH, "spotLight");
		
		return Structures.getStructureOffsetAbsolute(this.spotLights, spotLight);
	}
	
	/**
	 * Returns the relative offset of {@code spotLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code spotLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param spotLight a {@link SpotLight} instance in compiled form
	 * @return the relative offset of {@code spotLight} in this {@code CompiledLightCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code spotLight.length} is not equal to {@code CompiledLightCache.SPOT_LIGHT_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code spotLight} is {@code null}
	 */
	public int getSpotLightOffsetRelative(final float[] spotLight) {
		Objects.requireNonNull(spotLight, "spotLight == null");
		
		ParameterArguments.requireExactArrayLength(spotLight, SPOT_LIGHT_LENGTH, "spotLight");
		
		return Structures.getStructureOffsetRelative(this.spotLights, spotLight);
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link ImageLight} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code ImageLight} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getImageLightOffsets() {
		return this.imageLightOffsets;
	}
	
	/**
	 * Returns an {@code int[]} that contains the ID and offset for all {@link Light} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the ID and offset for all {@code Light} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getLightIDsAndOffsets() {
		return this.lightIDsAndOffsets;
	}
	
	/**
	 * Returns an {@code int[]} that contains the offsets for all {@link PerezLight} instances in this {@code CompiledLightCache} instance.
	 * 
	 * @return an {@code int[]} that contains the offsets for all {@code PerezLight} instances in this {@code CompiledLightCache} instance
	 */
	public int[] getPerezLightOffsets() {
		return this.perezLightOffsets;
	}
	
	/**
	 * Sets all {@link DiffuseAreaLight} instances in compiled form to {@code diffuseAreaLights}.
	 * <p>
	 * If {@code diffuseAreaLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code diffuseAreaLights.length % CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param diffuseAreaLights the {@code DiffuseAreaLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code diffuseAreaLights.length % CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLights} is {@code null}
	 */
	public void setDiffuseAreaLights(final float[] diffuseAreaLights) {
		Objects.requireNonNull(diffuseAreaLights, "diffuseAreaLights == null");
		
		ParameterArguments.requireExact(diffuseAreaLights.length % DIFFUSE_AREA_LIGHT_LENGTH, 0, "diffuseAreaLights.length % CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH");
		
		this.diffuseAreaLights = diffuseAreaLights;
	}
	
	/**
	 * Sets all {@link DirectionalLight} instances in compiled form to {@code directionalLights}.
	 * <p>
	 * If {@code directionalLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code directionalLights.length % CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param directionalLights the {@code DirectionalLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code directionalLights.length % CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code directionalLights} is {@code null}
	 */
	public void setDirectionalLights(final float[] directionalLights) {
		Objects.requireNonNull(directionalLights, "directionalLights == null");
		
		ParameterArguments.requireExact(directionalLights.length % DIRECTIONAL_LIGHT_LENGTH, 0, "directionalLights.length % CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH");
		
		this.directionalLights = directionalLights;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link ImageLight} instances to {@code imageLightOffsets}.
	 * <p>
	 * If {@code imageLightOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code imageLightOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLightOffsets the {@code int[]} that contains the offsets for all {@code ImageLight} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code imageLightOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLightOffsets} is {@code null}
	 */
	public void setImageLightOffsets(final int[] imageLightOffsets) {
		Objects.requireNonNull(imageLightOffsets, "imageLightOffsets == null");
		
		ParameterArguments.requireRange(imageLightOffsets, 0, Integer.MAX_VALUE, "imageLightOffsets");
		
		this.imageLightOffsets = imageLightOffsets;
	}
	
	/**
	 * Sets all {@link ImageLight} instances in compiled form to {@code imageLights}.
	 * <p>
	 * If {@code imageLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code imageLights.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param imageLights the {@code ImageLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code imageLights.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code imageLights} is {@code null}
	 */
	public void setImageLights(final float[] imageLights) {
		Objects.requireNonNull(imageLights, "imageLights == null");
		
		ParameterArguments.requireExact(imageLights.length % 8, 0, "imageLights.length % 8");
		
		this.imageLights = imageLights;
	}
	
	/**
	 * Sets the {@code int[]} that contains the ID and offset for all {@link Light} instances to {@code lightIDsAndOffsets}.
	 * <p>
	 * If {@code lightIDsAndOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lightIDsAndOffsets the {@code int[]} that contains the ID and offset for all {@code Light} instances
	 * @throws NullPointerException thrown if, and only if, {@code lightIDsAndOffsets} is {@code null}
	 */
	public void setLightIDsAndOffsets(final int[] lightIDsAndOffsets) {
		Objects.requireNonNull(lightIDsAndOffsets, "lightIDsAndOffsets == null");
		
		this.lightIDsAndOffsets = lightIDsAndOffsets;
	}
	
	/**
	 * Sets the {@code int[]} that contains the offsets for all {@link PerezLight} instances to {@code perezLightOffsets}.
	 * <p>
	 * If {@code perezLightOffsets} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If at least one offset in {@code perezLightOffsets} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLightOffsets the {@code int[]} that contains the offsets for all {@code PerezLight} instances
	 * @throws IllegalArgumentException thrown if, and only if, at least one offset in {@code perezLightOffsets} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLightOffsets} is {@code null}
	 */
	public void setPerezLightOffsets(final int[] perezLightOffsets) {
		Objects.requireNonNull(perezLightOffsets, "perezLightOffsets == null");
		
		ParameterArguments.requireRange(perezLightOffsets, 0, Integer.MAX_VALUE, "perezLightOffsets");
		
		this.perezLightOffsets = perezLightOffsets;
	}
	
	/**
	 * Sets all {@link PerezLight} instances in compiled form to {@code perezLights}.
	 * <p>
	 * If {@code perezLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code perezLights.length % 8} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param perezLights the {@code PerezLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code perezLights.length % 8} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code perezLights} is {@code null}
	 */
	public void setPerezLights(final float[] perezLights) {
		Objects.requireNonNull(perezLights, "perezLights == null");
		
		ParameterArguments.requireExact(perezLights.length % 8, 0, "perezLights.length % 8");
		
		this.perezLights = perezLights;
	}
	
	/**
	 * Sets all {@link PointLight} instances in compiled form to {@code pointLights}.
	 * <p>
	 * If {@code pointLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code pointLights.length % CompiledLightCache.POINT_LIGHT_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param pointLights the {@code PointLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code pointLights.length % CompiledLightCache.POINT_LIGHT_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code pointLights} is {@code null}
	 */
	public void setPointLights(final float[] pointLights) {
		Objects.requireNonNull(pointLights, "pointLights == null");
		
		ParameterArguments.requireExact(pointLights.length % POINT_LIGHT_LENGTH, 0, "pointLights.length % CompiledLightCache.POINT_LIGHT_LENGTH");
		
		this.pointLights = pointLights;
	}
	
	/**
	 * Sets all {@link SpotLight} instances in compiled form to {@code spotLights}.
	 * <p>
	 * If {@code spotLights} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code spotLights.length % CompiledLightCache.SPOT_LIGHT_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param spotLights the {@code SpotLight} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code spotLights.length % CompiledLightCache.SPOT_LIGHT_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code spotLights} is {@code null}
	 */
	public void setSpotLights(final float[] spotLights) {
		Objects.requireNonNull(spotLights, "spotLights == null");
		
		ParameterArguments.requireExact(spotLights.length % SPOT_LIGHT_LENGTH, 0, "spotLights.length % CompiledLightCache.SPOT_LIGHT_LENGTH");
		
		this.spotLights = spotLights;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, {@code light} is supported, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light a {@link Light} instance
	 * @return {@code true} if, and only if, {@code light} is supported, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public static boolean isSupported(final Light light) {
		Objects.requireNonNull(light, "light == null");
		
		if(light instanceof DiffuseAreaLight) {
			return true;
		} else if(light instanceof DirectionalLight) {
			return true;
		} else if(light instanceof ImageLight) {
			return true;
		} else if(light instanceof PerezLight) {
			return true;
		} else if(light instanceof PointLight) {
			return true;
		} else if(light instanceof SpotLight) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Returns a {@code float[]} with {@code diffuseAreaLight} in compiled form.
	 * <p>
	 * If {@code diffuseAreaLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledLightCache.toDiffuseAreaLight(diffuseAreaLight, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param diffuseAreaLight a {@link DiffuseAreaLight} instance
	 * @return a {@code float[]} with {@code diffuseAreaLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLight} is {@code null}
	 */
	public static float[] toDiffuseAreaLight(final DiffuseAreaLight diffuseAreaLight) {
		return toDiffuseAreaLight(diffuseAreaLight, shape3F -> 0);
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
	public static float[] toDiffuseAreaLight(final DiffuseAreaLight diffuseAreaLight, final ToIntFunction<Shape3F> shapeOffsetFunction) {
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
	 * Returns a {@code float[]} with all {@link DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form.
	 * <p>
	 * If {@code diffuseAreaLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledLightCache.toDiffuseAreaLights(diffuseAreaLights, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param diffuseAreaLights a {@code List} of {@code DiffuseAreaLight} instances
	 * @return a {@code float[]} with all {@code DiffuseAreaLight} instances in {@code diffuseAreaLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code diffuseAreaLights} or at least one of its elements are {@code null}
	 */
	public static float[] toDiffuseAreaLights(final List<DiffuseAreaLight> diffuseAreaLights) {
		return toDiffuseAreaLights(diffuseAreaLights, shape3F -> 0);
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
	public static float[] toDiffuseAreaLights(final List<DiffuseAreaLight> diffuseAreaLights, final ToIntFunction<Shape3F> shapeOffsetFunction) {
		return Arrays.toFloatArray(diffuseAreaLights, diffuseAreaLight -> toDiffuseAreaLight(diffuseAreaLight, shapeOffsetFunction));
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
	public static float[] toDirectionalLight(final DirectionalLight directionalLight) {
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
	 * Returns a {@code float[]} with all {@link DirectionalLight} instances in {@code directionalLights} in compiled form.
	 * <p>
	 * If {@code directionalLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param directionalLights a {@code List} of {@code DirectionalLight} instances
	 * @return a {@code float[]} with all {@code DirectionalLight} instances in {@code directionalLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code directionalLights} or at least one of its elements are {@code null}
	 */
	public static float[] toDirectionalLights(final List<DirectionalLight> directionalLights) {
		return Arrays.toFloatArray(directionalLights, directionalLight -> toDirectionalLight(directionalLight));
	}
	
	/**
	 * Returns a {@code float[]} with {@code imageLight} in compiled form.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance
	 * @return a {@code float[]} with {@code imageLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public static float[] toImageLight(final ImageLight imageLight) {
		final AngleF angle = imageLight.getAngle();
		
		final Matrix44F objectToWorld = imageLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = imageLight.getTransform().getWorldToObject();
		
		final Vector2F scale = imageLight.getScale();
		
		final float radius = imageLight.getRadius();
		
		final float[] distribution = imageLight.getDistribution().toArray();
		
		final int resolutionX = imageLight.getImage().getResolutionX();
		final int resolutionY = imageLight.getImage().getResolutionY();
		
		final int[] image = imageLight.getImage().toIntArrayPackedForm();
		
		final float[] array = new float[getImageLightLength(imageLight)];
		
//		Block #1:
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();
		
//		Block #2:
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();
		array[IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();
		
//		Block #3:
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();
		
//		Block #4:
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();
		array[IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();
		
//		Block #5:
		array[IMAGE_LIGHT_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[IMAGE_LIGHT_OFFSET_SCALE + 0] = scale.x;
		array[IMAGE_LIGHT_OFFSET_SCALE + 1] = scale.y;
		array[IMAGE_LIGHT_OFFSET_RADIUS] = radius;
		array[IMAGE_LIGHT_OFFSET_RESOLUTION_X] = resolutionX;
		array[IMAGE_LIGHT_OFFSET_RESOLUTION_Y] = resolutionY;
		array[38] = 0.0F;
		array[39] = 0.0F;
		
		for(int i = 0; i < distribution.length; i++) {
			array[IMAGE_LIGHT_OFFSET_DISTRIBUTION + i] = distribution[i];
		}
		
		for(int i = 0; i < image.length; i++) {
			array[IMAGE_LIGHT_OFFSET_IMAGE + i] = image[i];
		}
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link ImageLight} instances in {@code imageLights} in compiled form.
	 * <p>
	 * If {@code imageLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageLights a {@code List} of {@code ImageLight} instances
	 * @return a {@code float[]} with all {@code ImageLight} instances in {@code imageLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code imageLights} or at least one of its elements are {@code null}
	 */
	public static float[] toImageLights(final List<ImageLight> imageLights) {
		return Arrays.toFloatArray(imageLights, imageLight -> toImageLight(imageLight));
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
	public static float[] toPerezLight(final PerezLight perezLight) {
		final Color3F sunColor = perezLight.getSunColor();
		
		final Matrix44F objectToWorld = perezLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = perezLight.getTransform().getWorldToObject();
		
		final Vector3F sunDirectionObjectSpace = perezLight.getSunDirectionObjectSpace();
		final Vector3F sunDirectionWorldSpace = perezLight.getSunDirectionWorldSpace();
		
		final double[] zenith = perezLight.getZenith();
		
		final float turbidity = perezLight.getTurbidity();
		final float radius = perezLight.getRadius();
		final float theta = perezLight.getTheta();
		
		final float[] distribution = perezLight.getDistribution().toArray();
		
		final float[] array = new float[getPerezLightLength(perezLight)];
		
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
		array[PEREZ_LIGHT_OFFSET_TURBIDITY] = turbidity;
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
	 * Returns a {@code float[]} with all {@link PerezLight} instances in {@code perezLights} in compiled form.
	 * <p>
	 * If {@code perezLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLights a {@code List} of {@code PerezLight} instances
	 * @return a {@code float[]} with all {@code PerezLight} instances in {@code perezLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code perezLights} or at least one of its elements are {@code null}
	 */
	public static float[] toPerezLights(final List<PerezLight> perezLights) {
		return Arrays.toFloatArray(perezLights, perezLight -> toPerezLight(perezLight));
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
	public static float[] toPointLight(final PointLight pointLight) {
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
	 * Returns a {@code float[]} with all {@link PointLight} instances in {@code pointLights} in compiled form.
	 * <p>
	 * If {@code pointLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pointLights a {@code List} of {@code PointLight} instances
	 * @return a {@code float[]} with all {@code PointLight} instances in {@code pointLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code pointLights} or at least one of its elements are {@code null}
	 */
	public static float[] toPointLights(final List<PointLight> pointLights) {
		return Arrays.toFloatArray(pointLights, pointLight -> toPointLight(pointLight));
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
	public static float[] toSpotLight(final SpotLight spotLight) {
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
	 * Returns a {@code float[]} with all {@link SpotLight} instances in {@code spotLights} in compiled form.
	 * <p>
	 * If {@code spotLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param spotLights a {@code List} of {@code SpotLight} instances
	 * @return a {@code float[]} with all {@code SpotLight} instances in {@code spotLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code spotLights} or at least one of its elements are {@code null}
	 */
	public static float[] toSpotLights(final List<SpotLight> spotLights) {
		return Arrays.toFloatArray(spotLights, spotLight -> toSpotLight(spotLight));
	}
	
	/**
	 * Returns the length of {@code imageLight} in compiled form.
	 * <p>
	 * If {@code imageLight} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageLight an {@link ImageLight} instance
	 * @return the length of {@code imageLight} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code imageLight} is {@code null}
	 */
	public static int getImageLightLength(final ImageLight imageLight) {
		final int a = 16 + 16 + 1 + 2 + 1 + 1 + 1;
		final int b = imageLight.getDistribution().toArray().length;
		final int c = imageLight.getImage().getResolution();
		
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
	public static int getPerezLightLength(final PerezLight perezLight) {
		final int a = 16 + 16 + 3 + 3 + 3 + 1 + 3 + 1 + 1 + 1;
		final int b = perezLight.getDistribution().toArray().length;
		
		return a + b + padding(a + b);
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link ImageLight} instances in {@code imageLights} in compiled form.
	 * <p>
	 * If {@code imageLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageLights a {@code List} of {@code ImageLight} instances
	 * @return an {@code int[]} with the offsets for all {@code ImageLight} instances in {@code imageLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code imageLights} or at least one of its elements are {@code null}
	 */
	public static int[] toImageLightOffsets(final List<ImageLight> imageLights) {
		ParameterArguments.requireNonNullList(imageLights, "imageLights");
		
		final int[] imageLightOffsets = new int[imageLights.size()];
		
		for(int i = 0, j = 0; i < imageLights.size(); j += getImageLightLength(imageLights.get(i)), i++) {
			imageLightOffsets[i] = j;
		}
		
		return imageLightOffsets;
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
	public static int[] toLightIDsAndOffsets(final List<Light> lights, final ToIntFunction<Light> lightOffsetFunction) {
		ParameterArguments.requireNonNullList(lights, "lights");
		
		Objects.requireNonNull(lightOffsetFunction, "lightOffsetFunction == null");
		
		final int[] lightIDsAndOffsets = new int[lights.size()];
		
		for(int i = 0; i < lights.size(); i++) {
			final Light light = lights.get(i);
			
			lightIDsAndOffsets[i] = pack(light.getID(), lightOffsetFunction.applyAsInt(light));
		}
		
		return lightIDsAndOffsets;
	}
	
	/**
	 * Returns an {@code int[]} with the offsets for all {@link PerezLight} instances in {@code perezLights} in compiled form.
	 * <p>
	 * If {@code perezLights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param perezLights a {@code List} of {@code PerezLight} instances
	 * @return an {@code int[]} with the offsets for all {@code PerezLight} instances in {@code perezLights} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code perezLights} or at least one of its elements are {@code null}
	 */
	public static int[] toPerezLightOffsets(final List<PerezLight> perezLights) {
		ParameterArguments.requireNonNullList(perezLights, "perezLights");
		
		final int[] perezLightOffsets = new int[perezLights.size()];
		
		for(int i = 0, j = 0; i < perezLights.size(); j += getPerezLightLength(perezLights.get(i)), i++) {
			perezLightOffsets[i] = j;
		}
		
		return perezLightOffsets;
	}
}