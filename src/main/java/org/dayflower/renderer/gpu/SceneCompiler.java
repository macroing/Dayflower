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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;

final class SceneCompiler {
	private SceneCompiler() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CompiledScene compile(final Scene scene) {
//		Retrieve Lists for all distinct types:
		final List<Sphere3F> distinctSpheres = Sphere3F.filterAllDistinct(scene);
		
//		Retrieve a List of filtered Primitives:
		final List<Primitive> filteredPrimitives = doFilterPrimitives(scene, distinctSpheres);
		
//		Retrieve index mappings for all distinct types:
		final Map<Sphere3F, Integer> distinctToOffsetsSpheres = Sphere3F.mapDistinctToOffsets(distinctSpheres, Sphere3F.ARRAY_SIZE);
		
//		Retrieve float[] or int[] for all types:
		final float[] cameraArray = scene.getCamera().toArray();
		final float[] matrix44FArray = doCreateMatrix44FArray(filteredPrimitives);
		final float[] sphere3FArray = Sphere3F.toArray(distinctSpheres);
		
		final int[] primitiveArray = Primitive.toArray(filteredPrimitives);
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArray(filteredPrimitives, distinctToOffsetsSpheres, primitiveArray);
		
		return new CompiledScene(cameraArray, matrix44FArray, sphere3FArray, primitiveArray);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<Primitive> doFilterPrimitives(final Scene scene, final List<Sphere3F> distinctSpheres) {
		return scene.getPrimitives().stream().filter(primitive -> distinctSpheres.contains(primitive.getShape())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private static float[] doCreateMatrix44FArray(final List<Primitive> primitives) {
		final float[] array = new float[primitives.size() * Matrix44F.ARRAY_SIZE * 2];
		
		for(int i = 0; i < primitives.size(); i++) {
			final Primitive primitive = primitives.get(i);
			
			final Transform transform = primitive.getTransform();
			
			final Matrix44F objectToWorld = transform.getObjectToWorld();
			final Matrix44F worldToObject = transform.getWorldToObject();
			
			System.arraycopy(objectToWorld.toArray(), 0, array, i * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE * 0, Matrix44F.ARRAY_SIZE);
			System.arraycopy(worldToObject.toArray(), 0, array, i * Matrix44F.ARRAY_SIZE * 2 + Matrix44F.ARRAY_SIZE * 1, Matrix44F.ARRAY_SIZE);
		}
		
		return array;
	}
	
	private static void doPopulatePrimitiveArray(final List<Primitive> primitives, final Map<Sphere3F, Integer> distinctToOffsetsSpheres, final int[] primitiveArray) {
		for(int i = 0; i < primitives.size(); i++) {
			final Primitive primitive = primitives.get(i);
			
			final Shape3F shape = primitive.getShape();
			
			if(shape instanceof Sphere3F) {
				final int sphereOffset = distinctToOffsetsSpheres.get(Sphere3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = sphereOffset;
			}
		}
	}
}