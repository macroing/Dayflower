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

import org.dayflower.scene.Camera;

/**
 * A {@code CompiledCameraCache} contains the {@link Camera} instance in compiled form.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledCameraCache {
	private float[] cameraArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledCameraCache} instance.
	 */
	public CompiledCameraCache() {
		setCameraArray(new float[1]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code float[]} that contains the {@link Camera} instances in compiled form that is associated with this {@code CompiledCameraCache} instance.
	 * 
	 * @return a {@code float[]} that contains the {@code Camera} instances in compiled form that is associated with this {@code CompiledCameraCache} instance
	 */
	public float[] getCameraArray() {
		return this.cameraArray;
	}
	
	/**
	 * Sets the {@link Camera} instance in compiled form to {@code cameraArray}.
	 * <p>
	 * If {@code cameraArray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param cameraArray the {@code Camera} instance in compiled form
	 * @throws NullPointerException thrown if, and only if, {@code cameraArray} is {@code null}
	 */
	public void setCameraArray(final float[] cameraArray) {
		this.cameraArray = Objects.requireNonNull(cameraArray, "cameraArray == null");
	}
}