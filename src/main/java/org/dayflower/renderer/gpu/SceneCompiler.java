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

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.util.Floats;
import org.dayflower.util.Lists;

final class SceneCompiler {
	private SceneCompiler() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CompiledScene compile(final Scene scene) {
		System.out.println("Compiling...");
		
		final long currentTimeMillis = System.currentTimeMillis();
		
//		Retrieve Lists for all distinct types:
		final List<Sphere3F> distinctSpheres = NodeFilter.filterAllDistinct(scene, Sphere3F.class);
		final List<Torus3F> distinctToruses = NodeFilter.filterAllDistinct(scene, Torus3F.class);
		final List<Shape3F> distinctShapes = Lists.merge(distinctSpheres, distinctToruses);
		
//		Retrieve a List of filtered Primitives:
		final List<Primitive> filteredPrimitives = doFilterPrimitives(scene, distinctShapes);
		
//		Retrieve index mappings for all distinct types:
		final Map<Sphere3F, Integer> distinctToOffsetsSpheres = NodeFilter.mapDistinctToOffsets(distinctSpheres, Sphere3F.ARRAY_SIZE);
		final Map<Torus3F, Integer> distinctToOffsetsToruses = NodeFilter.mapDistinctToOffsets(distinctToruses, Torus3F.ARRAY_SIZE);
		
//		Retrieve float[] or int[] for all types:
		final float[] cameraArray = scene.getCamera().toArray();
		final float[] matrix44FArray = doCreateMatrix44FArray(filteredPrimitives);
		final float[] shape3FSphere3FArray = Floats.toArray(distinctSpheres, sphere -> sphere.toArray(), 1);
		final float[] shape3FTorus3FArray = Floats.toArray(distinctToruses, torus -> torus.toArray(), 1);
		
		final int[] primitiveArray = Primitive.toArray(filteredPrimitives);
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArray(filteredPrimitives, distinctToOffsetsSpheres, distinctToOffsetsToruses, primitiveArray);
		
		final
		CompiledScene compiledScene = new CompiledScene();
		compiledScene.setCameraArray(cameraArray);
		compiledScene.setMatrix44FArray(matrix44FArray);
		compiledScene.setPrimitiveArray(primitiveArray);
		compiledScene.setShape3FSphere3FArray(shape3FSphere3FArray);
		compiledScene.setShape3FTorus3FArray(shape3FTorus3FArray);
		
		final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
		
		System.out.println("- Compilation took " + elapsedTimeMillis + " milliseconds.");
		
		return compiledScene;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<Primitive> doFilterPrimitives(final Scene scene, final List<Shape3F> distinctShapes) {
		return scene.getPrimitives().stream().filter(primitive -> distinctShapes.contains(primitive.getShape())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private static float[] doCreateMatrix44FArray(final List<Primitive> primitives) {
		return Floats.toArray(primitives, primitive -> primitive.getTransform().toArray(), 1);
	}
	
	private static void doPopulatePrimitiveArray(final List<Primitive> primitives, final Map<Sphere3F, Integer> distinctToOffsetsSpheres, final Map<Torus3F, Integer> distinctToOffsetsToruses, final int[] primitiveArray) {
		for(int i = 0; i < primitives.size(); i++) {
			final Primitive primitive = primitives.get(i);
			
			final Shape3F shape = primitive.getShape();
			
			if(shape instanceof Sphere3F) {
				final int sphereOffset = distinctToOffsetsSpheres.get(Sphere3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = sphereOffset;
			} else if(shape instanceof Torus3F) {
				final int torusOffset = distinctToOffsetsToruses.get(Torus3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = torusOffset;
			}
		}
	}
}