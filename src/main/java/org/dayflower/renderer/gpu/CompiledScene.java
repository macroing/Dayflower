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
	private final float[] matrix44Fs;
	private final float[] point2Fs;
	private final float[] point3Fs;
	private final float[] vector3Fs;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene(final float[] matrix44Fs, final float[] point2Fs, final float[] point3Fs, final float[] vector3Fs) {
		this.matrix44Fs = Objects.requireNonNull(matrix44Fs, "matrix44Fs == null");
		this.point2Fs = Objects.requireNonNull(point2Fs, "point2Fs == null");
		this.point3Fs = Objects.requireNonNull(point3Fs, "point3Fs == null");
		this.vector3Fs = Objects.requireNonNull(vector3Fs, "vector3Fs == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float[] getMatrix44Fs() {
		return this.matrix44Fs;
	}
	
	public float[] getPoint2Fs() {
		return this.point2Fs;
	}
	
	public float[] getPoint3Fs() {
		return this.point3Fs;
	}
	
	public float[] getVector3Fs() {
		return this.vector3Fs;
	}
}