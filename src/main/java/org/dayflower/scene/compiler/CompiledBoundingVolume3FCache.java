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

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.utility.Floats;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code CompiledBoundingVolume3FCache} contains {@link BoundingVolume3F} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledBoundingVolume3FCache {
	/**
	 * The length of a compiled {@link AxisAlignedBoundingBox3F} instance.
	 */
	public static final int AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH = 8;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the maximum point in a compiled {@link AxisAlignedBoundingBox3F} instance.
	 */
	public static final int AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM = 0;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the minimum point in a compiled {@link AxisAlignedBoundingBox3F} instance.
	 */
	public static final int AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM = 3;
	
	/**
	 * The length of a compiled {@link BoundingSphere3F} instance.
	 */
	public static final int BOUNDING_SPHERE_3_F_LENGTH = 4;
	
	/**
	 * The offset for the {@link Point3F} instance that represents the center in a compiled {@link BoundingSphere3F} instance.
	 */
	public static final int BOUNDING_SPHERE_3_F_OFFSET_CENTER = 0;
	
	/**
	 * The offset for the radius in a compiled {@link BoundingSphere3F} instance.
	 */
	public static final int BOUNDING_SPHERE_3_F_OFFSET_RADIUS = 3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private float[] axisAlignedBoundingBox3Fs;
	private float[] boundingSphere3Fs;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledBoundingVolume3FCache} instance.
	 */
	public CompiledBoundingVolume3FCache() {
		setAxisAlignedBoundingBox3Fs(new float[0]);
		setBoundingSphere3Fs(new float[0]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Removes {@code axisAlignedBoundingBox3F} from this {@code CompiledBoundingVolume3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code axisAlignedBoundingBox3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3F an {@link AxisAlignedBoundingBox3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code axisAlignedBoundingBox3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3F} is {@code null}
	 */
	public boolean removeAxisAlignedBoundingBox3F(final float[] axisAlignedBoundingBox3F) {
		final int absoluteOffset = getAxisAlignedBoundingBox3FOffsetAbsolute(axisAlignedBoundingBox3F);
		
		if(absoluteOffset != -1) {
			setAxisAlignedBoundingBox3Fs(Structures.removeStructure(getAxisAlignedBoundingBox3Fs(), absoluteOffset, AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code boundingSphere3F} from this {@code CompiledBoundingVolume3FCache} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code boundingSphere3F} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code boundingSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param boundingSphere3F a {@link BoundingSphere3F} instance in compiled form
	 * @return {@code true} if, and only if, {@code boundingSphere3F} was removed, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3F} is {@code null}
	 */
	public boolean removeBoundingSphere3F(final float[] boundingSphere3F) {
		final int absoluteOffset = getBoundingSphere3FOffsetAbsolute(boundingSphere3F);
		
		if(absoluteOffset != -1) {
			setBoundingSphere3Fs(Structures.removeStructure(getBoundingSphere3Fs(), absoluteOffset, BOUNDING_SPHERE_3_F_LENGTH));
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance
	 */
	public float[] getAxisAlignedBoundingBox3Fs() {
		return this.axisAlignedBoundingBox3Fs;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance
	 */
	public float[] getBoundingSphere3Fs() {
		return this.boundingSphere3Fs;
	}
	
	/**
	 * Adds {@code axisAlignedBoundingBox3F} to this {@code CompiledBoundingVolume3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code axisAlignedBoundingBox3F}.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3F an {@link AxisAlignedBoundingBox3F} instance in compiled form
	 * @return the relative offset to {@code axisAlignedBoundingBox3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3F} is {@code null}
	 */
	public int addAxisAlignedBoundingBox3F(final float[] axisAlignedBoundingBox3F) {
		final int relativeOffsetOld = getAxisAlignedBoundingBox3FOffsetRelative(axisAlignedBoundingBox3F);
		final int relativeOffsetNew = getAxisAlignedBoundingBox3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setAxisAlignedBoundingBox3Fs(Structures.addStructure(getAxisAlignedBoundingBox3Fs(), axisAlignedBoundingBox3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Adds {@code boundingSphere3F} to this {@code CompiledBoundingVolume3FCache} instance, if absent.
	 * <p>
	 * Returns the relative offset to {@code boundingSphere3F}.
	 * <p>
	 * If {@code boundingSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param boundingSphere3F a {@link BoundingSphere3F} instance in compiled form
	 * @return the relative offset to {@code boundingSphere3F}
	 * @throws IllegalArgumentException thrown if, and only if, {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3F} is {@code null}
	 */
	public int addBoundingSphere3F(final float[] boundingSphere3F) {
		final int relativeOffsetOld = getBoundingSphere3FOffsetRelative(boundingSphere3F);
		final int relativeOffsetNew = getBoundingSphere3FCount();
		
		if(relativeOffsetOld != -1) {
			return relativeOffsetOld;
		}
		
		setBoundingSphere3Fs(Structures.addStructure(getBoundingSphere3Fs(), boundingSphere3F));
		
		return relativeOffsetNew;
	}
	
	/**
	 * Returns the {@link AxisAlignedBoundingBox3F} count in this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return the {@code AxisAlignedBoundingBox3F} count in this {@code CompiledBoundingVolume3FCache} instance
	 */
	public int getAxisAlignedBoundingBox3FCount() {
		return Structures.getStructureCount(this.axisAlignedBoundingBox3Fs, AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code axisAlignedBoundingBox3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3F an {@link AxisAlignedBoundingBox3F} instance in compiled form
	 * @return the absolute offset of {@code axisAlignedBoundingBox3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3F} is {@code null}
	 */
	public int getAxisAlignedBoundingBox3FOffsetAbsolute(final float[] axisAlignedBoundingBox3F) {
		Objects.requireNonNull(axisAlignedBoundingBox3F, "axisAlignedBoundingBox3F == null");
		
		ParameterArguments.requireExactArrayLength(axisAlignedBoundingBox3F, AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH, "axisAlignedBoundingBox3F");
		
		return Structures.getStructureOffsetAbsolute(this.axisAlignedBoundingBox3Fs, axisAlignedBoundingBox3F, getAxisAlignedBoundingBox3FCount(), AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code axisAlignedBoundingBox3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3F an {@link AxisAlignedBoundingBox3F} instance in compiled form
	 * @return the relative offset of {@code axisAlignedBoundingBox3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code axisAlignedBoundingBox3F.length} is not equal to {@code CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3F} is {@code null}
	 */
	public int getAxisAlignedBoundingBox3FOffsetRelative(final float[] axisAlignedBoundingBox3F) {
		Objects.requireNonNull(axisAlignedBoundingBox3F, "axisAlignedBoundingBox3F == null");
		
		ParameterArguments.requireExactArrayLength(axisAlignedBoundingBox3F, AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH, "axisAlignedBoundingBox3F");
		
		return Structures.getStructureOffsetRelative(this.axisAlignedBoundingBox3Fs, axisAlignedBoundingBox3F, getAxisAlignedBoundingBox3FCount(), AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH);
	}
	
	/**
	 * Returns the {@link BoundingSphere3F} count in this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return the {@code BoundingSphere3F} count in this {@code CompiledBoundingVolume3FCache} instance
	 */
	public int getBoundingSphere3FCount() {
		return Structures.getStructureCount(this.boundingSphere3Fs, BOUNDING_SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the absolute offset of {@code boundingSphere3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code boundingSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param boundingSphere3F a {@link BoundingSphere3F} instance in compiled form
	 * @return the absolute offset of {@code boundingSphere3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3F} is {@code null}
	 */
	public int getBoundingSphere3FOffsetAbsolute(final float[] boundingSphere3F) {
		Objects.requireNonNull(boundingSphere3F, "boundingSphere3F == null");
		
		ParameterArguments.requireExactArrayLength(boundingSphere3F, BOUNDING_SPHERE_3_F_LENGTH, "boundingSphere3F");
		
		return Structures.getStructureOffsetAbsolute(this.boundingSphere3Fs, boundingSphere3F, getBoundingSphere3FCount(), BOUNDING_SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Returns the relative offset of {@code boundingSphere3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found.
	 * <p>
	 * If {@code boundingSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param boundingSphere3F a {@link BoundingSphere3F} instance in compiled form
	 * @return the relative offset of {@code boundingSphere3F} in this {@code CompiledBoundingVolume3FCache} instance, or {@code -1} if it cannot be found
	 * @throws IllegalArgumentException thrown if, and only if, {@code boundingSphere3F.length} is not equal to {@code CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH}
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3F} is {@code null}
	 */
	public int getBoundingSphere3FOffsetRelative(final float[] boundingSphere3F) {
		Objects.requireNonNull(boundingSphere3F, "boundingSphere3F == null");
		
		ParameterArguments.requireExactArrayLength(boundingSphere3F, BOUNDING_SPHERE_3_F_LENGTH, "boundingSphere3F");
		
		return Structures.getStructureOffsetRelative(this.boundingSphere3Fs, boundingSphere3F, getBoundingSphere3FCount(), BOUNDING_SPHERE_3_F_LENGTH);
	}
	
	/**
	 * Sets all {@link AxisAlignedBoundingBox3F} instances in compiled form to {@code axisAlignedBoundingBox3Fs}.
	 * <p>
	 * If {@code axisAlignedBoundingBox3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code axisAlignedBoundingBox3Fs.length % CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3Fs the {@code AxisAlignedBoundingBox3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code axisAlignedBoundingBox3Fs.length % CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3Fs} is {@code null}
	 */
	public void setAxisAlignedBoundingBox3Fs(final float[] axisAlignedBoundingBox3Fs) {
		Objects.requireNonNull(axisAlignedBoundingBox3Fs, "axisAlignedBoundingBox3Fs == null");
		
		ParameterArguments.requireExact(axisAlignedBoundingBox3Fs.length % AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH, 0, "axisAlignedBoundingBox3Fs.length % CompiledBoundingVolume3FCache.AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH");
		
		this.axisAlignedBoundingBox3Fs = axisAlignedBoundingBox3Fs;
	}
	
	/**
	 * Sets all {@link BoundingSphere3F} instances in compiled form to {@code boundingSphere3Fs}.
	 * <p>
	 * If {@code boundingSphere3Fs} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code boundingSphere3Fs.length % CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH} is not equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param boundingSphere3Fs the {@code BoundingSphere3F} instances in compiled form
	 * @throws IllegalArgumentException thrown if, and only if, {@code boundingSphere3Fs.length % CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH} is not equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3Fs} is {@code null}
	 */
	public void setBoundingSphere3Fs(final float[] boundingSphere3Fs) {
		Objects.requireNonNull(boundingSphere3Fs, "boundingSphere3Fs == null");
		
		ParameterArguments.requireExact(boundingSphere3Fs.length % BOUNDING_SPHERE_3_F_LENGTH, 0, "boundingSphere3Fs.length % CompiledBoundingVolume3FCache.BOUNDING_SPHERE_3_F_LENGTH");
		
		this.boundingSphere3Fs = boundingSphere3Fs;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} with {@code axisAlignedBoundingBox3F} in compiled form.
	 * <p>
	 * If {@code axisAlignedBoundingBox3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3F an {@link AxisAlignedBoundingBox3F} instance
	 * @return a {@code float[]} with {@code axisAlignedBoundingBox3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3F} is {@code null}
	 */
	public static float[] toAxisAlignedBoundingBox3F(final AxisAlignedBoundingBox3F axisAlignedBoundingBox3F) {
		final Point3F maximum = axisAlignedBoundingBox3F.getMaximum();
		final Point3F minimum = axisAlignedBoundingBox3F.getMinimum();
		
		final float[] array = new float[AXIS_ALIGNED_BOUNDING_BOX_3_F_LENGTH];
		
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 0] = maximum.getX();
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 1] = maximum.getY();
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MAXIMUM + 2] = maximum.getZ();
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 0] = minimum.getX();
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 1] = minimum.getY();
		array[AXIS_ALIGNED_BOUNDING_BOX_3_F_OFFSET_MINIMUM + 2] = minimum.getZ();
		array[6] = 0.0F;
		array[7] = 0.0F;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link AxisAlignedBoundingBox3F} instances in {@code axisAlignedBoundingBox3Fs} in compiled form.
	 * <p>
	 * If {@code axisAlignedBoundingBox3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param axisAlignedBoundingBox3Fs a {@code List} of {@code AxisAlignedBoundingBox3F} instances
	 * @return a {@code float[]} with all {@code AxisAlignedBoundingBox3F} instances in {@code axisAlignedBoundingBox3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code axisAlignedBoundingBox3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toAxisAlignedBoundingBox3Fs(final List<AxisAlignedBoundingBox3F> axisAlignedBoundingBox3Fs) {
		return Floats.toArray(axisAlignedBoundingBox3Fs, axisAlignedBoundingBox3F -> toAxisAlignedBoundingBox3F(axisAlignedBoundingBox3F));
	}
	
	/**
	 * Returns a {@code float[]} with {@code boundingSphere3F} in compiled form.
	 * <p>
	 * If {@code boundingSphere3F} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingSphere3F a {@link BoundingSphere3F} instance
	 * @return a {@code float[]} with {@code boundingSphere3F} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3F} is {@code null}
	 */
	public static float[] toBoundingSphere3F(final BoundingSphere3F boundingSphere3F) {
		final Point3F center = boundingSphere3F.getCenter();
		
		final float radius = boundingSphere3F.getRadius();
		
		final float[] array = new float[BOUNDING_SPHERE_3_F_LENGTH];
		
		array[BOUNDING_SPHERE_3_F_OFFSET_CENTER + 0] = center.getX();
		array[BOUNDING_SPHERE_3_F_OFFSET_CENTER + 1] = center.getY();
		array[BOUNDING_SPHERE_3_F_OFFSET_CENTER + 2] = center.getZ();
		array[BOUNDING_SPHERE_3_F_OFFSET_RADIUS] = radius;
		
		return array;
	}
	
	/**
	 * Returns a {@code float[]} with all {@link BoundingSphere3F} instances in {@code boundingSphere3Fs} in compiled form.
	 * <p>
	 * If {@code boundingSphere3Fs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingSphere3Fs a {@code List} of {@code BoundingSphere3F} instances
	 * @return a {@code float[]} with all {@code BoundingSphere3F} instances in {@code boundingSphere3Fs} in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingSphere3Fs} or at least one of its elements are {@code null}
	 */
	public static float[] toBoundingSphere3Fs(final List<BoundingSphere3F> boundingSphere3Fs) {
		return Floats.toArray(boundingSphere3Fs, boundingSphere3F -> toBoundingSphere3F(boundingSphere3F));
	}
}