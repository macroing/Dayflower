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

import static org.dayflower.utility.Ints.pack;

import java.util.List;
import java.util.Objects;
import java.util.function.ToIntFunction;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Transform;
import org.dayflower.utility.FloatArrays;
import org.dayflower.utility.IntArrays;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.util.Arrays;

/**
 * A {@code CompiledPrimitiveCache} contains {@link Primitive} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledPrimitiveCache {
	/**
	 * The length of a compiled {@link Matrix44F} instance.
	 */
	public static final int MATRIX_4_4_F_LENGTH = 16;
	
	/**
	 * The length of a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_LENGTH = 8;
	
	/**
	 * The offset for the {@link AreaLight} ID and offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET = 1;
	
	/**
	 * The offset for the {@link BoundingVolume3F} ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID = 2;
	
	/**
	 * The offset for the {@link BoundingVolume3F} offset in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET = 3;
	
	/**
	 * The offset for the instance ID in a compiled {@link Primitive} instance.
	 */
	public static final int PRIMITIVE_OFFSET_INSTANCE_ID = 0;
	
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
	 * Removes {@code primitive} from this {@code CompiledPrimitiveCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance in compiled form
	 * @return {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean removePrimitive(final int[] primitive) {
		final int absoluteOffset = getPrimitiveOffsetAbsolute(primitive);
		
		if(absoluteOffset != -1) {
			final int relativeOffset = absoluteOffset / PRIMITIVE_LENGTH;
			final int absoluteOffsetMatrix44Fs = relativeOffset * MATRIX_4_4_F_LENGTH * 2;
			
			setMatrix44Fs(FloatArrays.splice(getMatrix44Fs(), absoluteOffsetMatrix44Fs, MATRIX_4_4_F_LENGTH * 2));
			setPrimitives(IntArrays.splice(getPrimitives(), absoluteOffset, PRIMITIVE_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code Matrix44F} instances in compiled form that are associated with this {@code CompiledPrimitiveCache} instance
	 */
	public float[] getMatrix44Fs() {
		return this.matrix44Fs;
	}
	
	/**
	 * Adds {@code primitive} and {@code matrix44Fs} to this {@code CompiledPrimitiveCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code primitive}.
	 * <p>
	 * If either {@code primitive} or {@code matrix44Fs} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH} or {@code matrix44Fs.length} is not equal to {@code CompiledPrimitiveCache.MATRIX_4_4_F_LENGTH * 2}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance in compiled form
	 * @param matrix44Fs two {@link Matrix44F} instances in compiled form
	 * @return the relative offset to {@code primitive}
	 * @throws IllegalArgumentException thrown if, and only if, {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH} or {@code matrix44Fs.length} is not equal to {@code CompiledPrimitiveCache.MATRIX_4_4_F_LENGTH * 2}
	 * @throws NullPointerException thrown if, and only if, either {@code primitive} or {@code matrix44Fs} are {@code null}
	 */
	public int addPrimitive(final int[] primitive, final float[] matrix44Fs) {
		final int relativeOffsetOld = getPrimitiveOffsetRelative(primitive);
		final int relativeOffsetNew = getPrimitiveCount();
		
		ParameterArguments.requireExactArrayLength(matrix44Fs, MATRIX_4_4_F_LENGTH * 2, "matrix44Fs");
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setMatrix44Fs(Arrays.merge(getMatrix44Fs(), matrix44Fs));
		setPrimitives(Arrays.merge(getPrimitives(), primitive));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link Matrix44F} count in this {@code CompiledPrimitiveCache} instance.
	 * 
	 * @return the {@code Matrix44F} count in this {@code CompiledPrimitiveCache} instance
	 */
	public int getMatrix44FCount() {
		return Structures.getStructureCount(this.matrix44Fs, MATRIX_4_4_F_LENGTH);
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
	 * Returns the absolute offset of {@code primitive} in this {@code CompiledPrimitiveCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance in compiled form
	 * @return the absolute offset of {@code primitive} in this {@code CompiledPrimitiveCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public int getPrimitiveOffsetAbsolute(final int[] primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		ParameterArguments.requireExactArrayLength(primitive, PRIMITIVE_LENGTH, "primitive");
		
		return Structures.getStructureOffsetAbsolute(this.primitives, primitive);
	}
	
	/**
	 * Returns the relative offset of {@code primitive} in this {@code CompiledPrimitiveCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance in compiled form
	 * @return the relative offset of {@code primitive} in this {@code CompiledPrimitiveCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code primitive.length} is not equal to {@code CompiledPrimitiveCache.PRIMITIVE_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public int getPrimitiveOffsetRelative(final int[] primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		ParameterArguments.requireExactArrayLength(primitive, PRIMITIVE_LENGTH, "primitive");
		
		return Structures.getStructureOffsetRelative(this.primitives, primitive);
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
	 * Returns {@code true} if, and only if, {@code primitive} is supported, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive a {@link Primitive} instance
	 * @return {@code true} if, and only if, {@code primitive} is supported, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public static boolean isSupported(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		final AreaLight areaLight = primitive.getAreaLight().orElse(null);
		
		final BoundingVolume3F boundingVolume3F = primitive.getBoundingVolume();
		
		final Material material = primitive.getMaterial();
		
		final Shape3F shape3F = primitive.getShape();
		
		if(areaLight != null && !CompiledLightCache.isSupported(areaLight)) {
			return false;
		}
		
		if(!CompiledBoundingVolume3FCache.isSupported(boundingVolume3F)) {
			return false;
		}
		
		if(!CompiledMaterialCache.isSupported(material)) {
			return false;
		}
		
		if(!CompiledShape3FCache.isSupported(shape3F)) {
			return false;
		}
		
		return true;
	}
	
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
		return FloatArrays.convert(primitives, primitive -> toMatrix44Fs(primitive.getTransform()));
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
		return Arrays.merge(transform.getObjectToWorld().toArray(), transform.getWorldToObject().toArray());
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
		final AreaLight areaLight = primitive.getAreaLight().orElse(null);
		
		final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
		
		final Material material = primitive.getMaterial();
		
		final Shape3F shape = primitive.getShape();
		
		final int instanceID = primitive.getInstanceID();
		
		final int[] array = new int[PRIMITIVE_LENGTH];
		
		array[PRIMITIVE_OFFSET_INSTANCE_ID] = instanceID;
		array[PRIMITIVE_OFFSET_AREA_LIGHT_ID_AND_OFFSET] = areaLight != null ? pack(areaLight.getID(), areaLightOffsetFunction.applyAsInt(areaLight)) : 0;
		array[PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID] = boundingVolume.getID();
		array[PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET] = boundingVolume3FOffsetFunction.applyAsInt(boundingVolume);
		array[PRIMITIVE_OFFSET_MATERIAL_ID] = material.getID();
		array[PRIMITIVE_OFFSET_MATERIAL_OFFSET] = materialOffsetFunction.applyAsInt(material);
		array[PRIMITIVE_OFFSET_SHAPE_ID] = shape.getID();
		array[PRIMITIVE_OFFSET_SHAPE_OFFSET] = shape3FOffsetFunction.applyAsInt(shape);
		
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
		return IntArrays.convert(primitives, primitive -> toPrimitive(primitive, areaLightOffsetFunction, boundingVolume3FOffsetFunction, materialOffsetFunction, shape3FOffsetFunction));
	}
}