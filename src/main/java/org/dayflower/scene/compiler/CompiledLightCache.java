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

import java.util.Objects;

import org.dayflower.scene.Light;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;

/**
 * A {@code CompiledLightCache} contains {@link Light} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledLightCache {
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
		setLightDirectionalLightArray(new float[1]);
		setLightIDAndOffsetArray(new int[1]);
		setLightLDRImageLightArray(new float[1]);
		setLightLDRImageLightOffsetArray(new int[1]);
		setLightPerezLightArray(new float[1]);
		setLightPerezLightOffsetArray(new int[1]);
		setLightPointLightArray(new float[1]);
		setLightSpotLightArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
		return getLightDirectionalLightCount() + getLightLDRImageLightCount() + getLightPerezLightCount() + getLightPointLightCount() + getLightSpotLightCount();
	}
	
	/**
	 * Returns the {@link DirectionalLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code DirectionalLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightDirectionalLightCount() {
		return Structures.getStructureCount(this.lightDirectionalLightArray, DirectionalLight.ARRAY_LENGTH);
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
		return Structures.getStructureCount(this.lightPointLightArray, PointLight.ARRAY_LENGTH);
	}
	
	/**
	 * Returns the {@link SpotLight} count in this {@code CompiledLightCache} instance.
	 * 
	 * @return the {@code SpotLight} count in this {@code CompiledLightCache} instance
	 */
	public int getLightSpotLightCount() {
		return Structures.getStructureCount(this.lightSpotLightArray, SpotLight.ARRAY_LENGTH);
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
}