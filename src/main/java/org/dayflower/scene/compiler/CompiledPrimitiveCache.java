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

import java.lang.reflect.Field;//TODO: Refactor!
import java.util.Objects;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;

/**
 * A {@code CompiledPrimitiveCache} contains {@link Primitive} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledPrimitiveCache {
	/**
	 * The length of a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_LENGTH = 8;
	
	/**
	 * The offset for the {@link AreaLight} ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_AREA_LIGHT_ID = 0;
	
	/**
	 * The offset for the {@link AreaLight} offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_AREA_LIGHT_OFFSET = 1;
	
	/**
	 * The offset for the {@link BoundingVolume3F} ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID = 2;
	
	/**
	 * The offset for the {@link BoundingVolume3F} offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET = 3;
	
	/**
	 * The offset for the {@link Material} ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_MATERIAL_ID = 4;
	
	/**
	 * The offset for the {@link Material} offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_MATERIAL_OFFSET = 5;
	
	/**
	 * The offset for the {@link Shape3F} ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_SHAPE_ID = 6;
	
	/**
	 * The offset for the {@link Shape3F} offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_SHAPE_OFFSET = 7;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] primitiveMatrix44FArray;
	private int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledPrimitiveCache} instance.
	 */
	public CompiledPrimitiveCache() {
		setPrimitiveArray(new int[0]);
		setPrimitiveMatrix44FArray(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance
	 */
	public float[] getPrimitiveMatrix44FArray() {
		return this.primitiveMatrix44FArray;
	}
	
	/**
	 * Returns the {@link Primitive} count in this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return the {@code Primitive} count in this {@code CompiledPrimitiveCache} instance
	 */
	public int getPrimitiveCount() {
		return Structures.getStructureCount(this.primitiveArray, PRIMITIVE_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link Primitive} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code Primitive} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance
	 */
	public int[] getPrimitiveArray() {
		return this.primitiveArray;
	}
	
	/**
	 * Sets all {@link Primitive} instances in compiled form to {@code primitiveArray}.
	 * <p>
	 * If {@code primitiveArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveArray the {@code Primitive} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitiveArray} is {@code null}
	 */
	public void setPrimitiveArray(final int[] primitiveArray) {
		this.primitiveArray = Objects.requireNonNull(primitiveArray, "primitiveArray == null");
	}
	
	/**
	 * Sets all {@link Matrix44F} instances in compiled form to {@code primitiveMatrix44FArray}.
	 * <p>
	 * If {@code primitiveMatrix44FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitiveMatrix44FArray the {@code Matrix44F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitiveMatrix44FArray} is {@code null}
	 */
	public void setPrimitiveMatrix44FArray(final float[] primitiveMatrix44FArray) {
		this.primitiveMatrix44FArray = Objects.requireNonNull(primitiveMatrix44FArray, "primitiveMatrix44FArray == null");
	}
}