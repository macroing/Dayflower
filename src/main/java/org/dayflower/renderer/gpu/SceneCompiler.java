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

import java.util.List;
import java.util.Map;

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Scene;

final class SceneCompiler {
	public SceneCompiler() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene compile(final Scene scene) {
//		Retrieve Lists for all distinct types:
		final List<Point2F> distinctPoint2Fs = Point2F.filterAllDistinct(scene);
		final List<Point3F> distinctPoint3Fs = Point3F.filterAllDistinct(scene);
		final List<Vector3F> distinctVector3Fs = Vector3F.filterAllDistinct(scene);
		
//		Retrieve index mappings for all distinct types:
		final Map<Point2F, Integer> distinctToIndicesPoint2Fs = Point2F.mapDistinctToIndices(distinctPoint2Fs, 2);
		final Map<Point3F, Integer> distinctToIndicesPoint3Fs = Point3F.mapDistinctToIndices(distinctPoint3Fs, 3);
		final Map<Vector3F, Integer> distinctToIndicesVector3Fs = Vector3F.mapDistinctToIndices(distinctVector3Fs, 3);
		
//		Retrieve float[] for all distinct types:
		final float[] matrix44Fs = new float[0];
		final float[] point2Fs = Point2F.toArray(distinctPoint2Fs);
		final float[] point3Fs = Point3F.toArray(distinctPoint3Fs);
		final float[] vector3Fs = Vector3F.toArray(distinctVector3Fs);
		
		return new CompiledScene(matrix44Fs, point2Fs, point3Fs, vector3Fs);
	}
}