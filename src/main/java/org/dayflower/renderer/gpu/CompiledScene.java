/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.renderer.gpu;

import java.util.Objects;

final class CompiledScene {
	private final float[] cameraArray;
	private final float[] matrix44FArray;
	private final float[] sphere3FArray;
	private final int[] primitiveArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene(final float[] cameraArray, final float[] matrix44FArray, final float[] sphere3FArray, final int[] primitiveArray) {
		this.cameraArray = Objects.requireNonNull(cameraArray, "cameraArray == null");
		this.matrix44FArray = Objects.requireNonNull(matrix44FArray, "matrix44FArray == null");
		this.sphere3FArray = Objects.requireNonNull(sphere3FArray, "sphere3FArray == null");
		this.primitiveArray = Objects.requireNonNull(primitiveArray, "primitiveArray == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float[] getCameraArray() {
		return this.cameraArray;
	}
	
	public float[] getMatrix44FArray() {
		return this.matrix44FArray;
	}
	
	public float[] getSphere3FArray() {
		return this.sphere3FArray;
	}
	
	public int[] getPrimitiveArray() {
		return this.primitiveArray;
	}
}