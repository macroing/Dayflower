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

import org.dayflower.geometry.Matrix44F;
import org.dayflower.scene.Primitive;

/**
 * A {@code CompiledPrimitiveCache} contains {@link Primitive} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledPrimitiveCache {
	private float[] primitiveMatrix44FArray;
	private int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledPrimitiveCache} instance.
	 */
	public CompiledPrimitiveCache() {
		setPrimitiveArray(new int[1]);
		setPrimitiveMatrix44FArray(new float[1]);
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
		return Structures.getStructureCount(this.primitiveArray, Primitive.ARRAY_LENGTH);
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