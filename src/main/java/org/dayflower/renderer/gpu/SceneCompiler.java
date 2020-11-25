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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Scene;

final class SceneCompiler {
	public SceneCompiler() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene compile(final Scene scene) {
		final float[] matrix44Fs = new float[0];
		final float[] point2Fs = Point2F.toArray(Point2F.filterAllDistinct(scene));
		final float[] point3Fs = Point3F.toArray(Point3F.filterAllDistinct(scene));
		final float[] vector3Fs = Vector3F.toArray(Vector3F.filterAllDistinct(scene));
		
		return new CompiledScene(matrix44Fs, point2Fs, point3Fs, vector3Fs);
	}
}