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

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;

/**
 * A {@code CompiledBoundingVolume3FCache} contains {@link BoundingVolume3F} instances in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledBoundingVolume3FCache {
	private float[] boundingVolume3FAxisAlignedBoundingBox3FArray;
	private float[] boundingVolume3FBoundingSphere3FArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledBoundingVolume3FCache} instance.
	 */
	public CompiledBoundingVolume3FCache() {
		setBoundingVolume3FAxisAlignedBoundingBox3FArray(new float[1]);
		setBoundingVolume3FBoundingSphere3FArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains all {@link AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code AxisAlignedBoundingBox3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance
	 */
	public float[] getBoundingVolume3FAxisAlignedBoundingBox3FArray() {
		return this.boundingVolume3FAxisAlignedBoundingBox3FArray;
	}
	
	/**
	 * Returns a {@code float[]} that contains all {@link BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance.
	 * 
	 * @return a {@code float[]} that contains all {@code BoundingSphere3F} instances in compiled form that are associated with this {@code CompiledBoundingVolume3FCache} instance
	 */
	public float[] getBoundingVolume3FBoundingSphere3FArray() {
		return this.boundingVolume3FBoundingSphere3FArray;
	}
	
	/**
	 * Sets all {@link AxisAlignedBoundingBox3F} instances in compiled form to {@code boundingVolume3FAxisAlignedBoundingBox3FArray}.
	 * <p>
	 * If {@code boundingVolume3FAxisAlignedBoundingBox3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume3FAxisAlignedBoundingBox3FArray the {@code AxisAlignedBoundingBox3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume3FAxisAlignedBoundingBox3FArray} is {@code null}
	 */
	public void setBoundingVolume3FAxisAlignedBoundingBox3FArray(final float[] boundingVolume3FAxisAlignedBoundingBox3FArray) {
		this.boundingVolume3FAxisAlignedBoundingBox3FArray = Objects.requireNonNull(boundingVolume3FAxisAlignedBoundingBox3FArray, "boundingVolume3FAxisAlignedBoundingBox3FArray == null");
	}
	
	/**
	 * Sets all {@link BoundingSphere3F} instances in compiled form to {@code boundingVolume3FBoundingSphere3FArray}.
	 * <p>
	 * If {@code boundingVolume3FBoundingSphere3FArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param boundingVolume3FBoundingSphere3FArray the {@code BoundingSphere3F} instances in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code boundingVolume3FBoundingSphere3FArray} is {@code null}
	 */
	public void setBoundingVolume3FBoundingSphere3FArray(final float[] boundingVolume3FBoundingSphere3FArray) {
		this.boundingVolume3FBoundingSphere3FArray = Objects.requireNonNull(boundingVolume3FBoundingSphere3FArray, "boundingVolume3FBoundingSphere3FArray == null");
	}
}