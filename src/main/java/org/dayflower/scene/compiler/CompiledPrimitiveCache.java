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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.ToIntFunction;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Transform;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;

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
	
	private float[] matrix44Fs;
	private int[] primitives;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledPrimitiveCache} instance.
	 */
	public CompiledPrimitiveCache() {
		setMatrix44Fs(new float[0]);
		setPrimitives(new int[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance
	 */
	public float[] getMatrix44Fs() {
		return this.matrix44Fs;
	}
	
	/**
	 * Returns the {@link Primitive} count in this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return the {@code Primitive} count in this {@code CompiledPrimitiveCache} instance
	 */
	public int getPrimitiveCount() {
		return Structures.getStructureCount(this.primitives, PRIMITIVE_LENGTH);
	}
	
	/**
	 * Returns an {@code int[]} that contains all {@link Primitive} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return an {@code int[]} that contains all {@code Primitive} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance
	 */
	public int[] getPrimitives() {
		return this.primitives;
	}
	
	/**
	 * Sets all {@link Matrix44F} instances in compiled form to {@code matrix44Fs}.
	 * <p>
	 * If {@code matrix44Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param matrix44Fs the {@code Matrix44F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code matrix44Fs} is {@code null}
	 */
	public void setMatrix44Fs(final float[] matrix44Fs) {
		this.matrix44Fs = Objects.requireNonNull(matrix44Fs, "matrix44Fs == null");
	}
	
	/**
	 * Sets all {@link Primitive} instances in compiled form to {@code primitives}.
	 * <p>
	 * If {@code primitives} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitives the {@code Primitive} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitives} is {@code null}
	 */
	public void setPrimitives(final int[] primitives) {
		this.primitives = Objects.requireNonNull(primitives, "primitives == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with all {@link Matrix44F} instances in {@code primitives} in compiled form.
	 * <p>
	 * If {@code primitives} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitives a {@code List} of {@link Primitive} instances
	 * @return a {@code float[]} with all {@code Matrix44F} instances in {@code primitives} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitives} or at least one of its elements are {@code null}
	 */
	public static float[] toMatrix44Fs(final List<Primitive> primitives) {
		return Floats.toArray(primitives, primitive -> toMatrix44Fs(primitive.getTransform()));
	}
	
	/**
	 * Returns a {@code float[]} with both {@link Matrix44F} instances in {@code transform} in compiled form.
	 * <p>
	 * If {@code transform} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param transform a {@link Transform} instance
	 * @return a {@code float[]} with both {@code Matrix44F} instances in {@code transform} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code transform} is {@code null}
	 */
	public static float[] toMatrix44Fs(final Transform transform) {
		return Floats.array(transform.getObjectToWorld().toArray(), transform.getWorldToObject().toArray());
	}
	
	/**
	 * Returns an {@code int[]} with {@code primitive} in compiled form.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledPrimitiveCache.toPrimitive(primitive, areaLight -> 0, boundingVolume3F -> 0, material -> 0, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param primitive a {@link Primitive} instance
	 * @return an {@code int[]} with {@code primitive} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public static int[] toPrimitive(final Primitive primitive) {
		return toPrimitive(primitive, areaLight -> 0, boundingVolume3F -> 0, material -> 0, shape3F -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with {@code primitive} in compiled form.
	 * <p>
	 * If either {@code primitive}, {@code areaLightOffsetFunction}, {@code boundingVolume3FOffsetFunction}, {@code materialOffsetFunction} or {@code shape3FOffsetFunction} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance
	 * @param areaLightOffsetFunction a {@code ToIntFunction} that returns {@link AreaLight} offsets
	 * @param boundingVolume3FOffsetFunction a {@code ToIntFunction} that returns {@link BoundingVolume3F} offsets
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @param shape3FOffsetFunction a {@code ToIntFunction} that returns {@link Shape3F} offsets
	 * @return an {@code int[]} with {@code primitive} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code primitive}, {@code areaLightOffsetFunction}, {@code boundingVolume3FOffsetFunction}, {@code materialOffsetFunction} or {@code shape3FOffsetFunction} are {@code null}
	 */
	public static int[] toPrimitive(final Primitive primitive, final ToIntFunction<AreaLight> areaLightOffsetFunction, final ToIntFunction<BoundingVolume3F> boundingVolume3FOffsetFunction, final ToIntFunction<Material> materialOffsetFunction, final ToIntFunction<Shape3F> shape3FOffsetFunction) {
		final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
		
		final Material material = primitive.getMaterial();
		
		final Optional<AreaLight> optionalAreaLight = primitive.getAreaLight();
		
		final Shape3F shape = primitive.getShape();
		
		final int[] array = new int[CompiledPrimitiveCache.PRIMITIVE_LENGTH];
		
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID] = optionalAreaLight.isPresent() ? optionalAreaLight.get().getID() : 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_OFFSET] = optionalAreaLight.isPresent() ? areaLightOffsetFunction.applyAsInt(optionalAreaLight.get()) : 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID] = boundingVolume.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET] = boundingVolume3FOffsetFunction.applyAsInt(boundingVolume);
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_ID] = material.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_OFFSET] = materialOffsetFunction.applyAsInt(material);
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID] = shape.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET] = shape3FOffsetFunction.applyAsInt(shape);
		
		return array;
	}
	
	/**
	 * Returns an {@code int[]} with all {@link Primitive} instances in {@code primitives} in compiled form.
	 * <p>
	 * If {@code primitives} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * compiledPrimitiveCache.toPrimitives(primitives, areaLight -> 0, boundingVolume3F -> 0, material -> 0, shape3F -> 0);
	 * }
	 * </pre>
	 * 
	 * @param primitives a {@code List} of {@code Primitive} instances
	 * @return an {@code int[]} with all {@code Primitive} instances in {@code primitives} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code primitives} or at least one of its elements are {@code null}
	 */
	public static int[] toPrimitives(final List<Primitive> primitives) {
		return toPrimitives(primitives, areaLight -> 0, boundingVolume3F -> 0, material -> 0, shape3F -> 0);
	}
	
	/**
	 * Returns an {@code int[]} with all {@link Primitive} instances in {@code primitives} in compiled form.
	 * <p>
	 * If either {@code primitives}, at least one of its elements, {@code areaLightOffsetFunction}, {@code boundingVolume3FOffsetFunction}, {@code materialOffsetFunction} or {@code shape3FOffsetFunction} are {@code null}, a {@code NullPointerException}
	 * will be thrown.
	 * 
	 * @param primitives a {@code List} of {@code Primitive} instances
	 * @param areaLightOffsetFunction a {@code ToIntFunction} that returns {@link AreaLight} offsets
	 * @param boundingVolume3FOffsetFunction a {@code ToIntFunction} that returns {@link BoundingVolume3F} offsets
	 * @param materialOffsetFunction a {@code ToIntFunction} that returns {@link Material} offsets
	 * @param shape3FOffsetFunction a {@code ToIntFunction} that returns {@link Shape3F} offsets
	 * @return an {@code int[]} with all {@code Primitive} instances in {@code primitives} in compiled form
	 * @throws NullPointerException thrown if, and only if, either {@code primitives}, at least one of its elements, {@code areaLightOffsetFunction}, {@code boundingVolume3FOffsetFunction}, {@code materialOffsetFunction} or {@code shape3FOffsetFunction}
	 *                              are {@code null}
	 */
	public static int[] toPrimitives(final List<Primitive> primitives, final ToIntFunction<AreaLight> areaLightOffsetFunction, final ToIntFunction<BoundingVolume3F> boundingVolume3FOffsetFunction, final ToIntFunction<Material> materialOffsetFunction, final ToIntFunction<Shape3F> shape3FOffsetFunction) {
		return Ints.toArray(primitives, primitive -> toPrimitive(primitive, areaLightOffsetFunction, boundingVolume3FOffsetFunction, materialOffsetFunction, shape3FOffsetFunction));
	}
}