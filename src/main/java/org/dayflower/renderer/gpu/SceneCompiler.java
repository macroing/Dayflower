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

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.BoundingSphere3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.geometry.shape.Plane3F;
import org.dayflower.geometry.shape.RectangularCuboid3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Torus3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.texture.BlendTexture;
import org.dayflower.scene.texture.BullseyeTexture;
import org.dayflower.scene.texture.CheckerboardTexture;
import org.dayflower.scene.texture.ConstantTexture;
import org.dayflower.scene.texture.FunctionTexture;
import org.dayflower.scene.texture.ImageTexture;
import org.dayflower.scene.texture.MarbleTexture;
import org.dayflower.scene.texture.SimplexFractionalBrownianMotionTexture;
import org.dayflower.scene.texture.SurfaceNormalTexture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.util.Floats;
import org.dayflower.util.Lists;

final class SceneCompiler {
	private SceneCompiler() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CompiledScene compile(final Scene scene) {
		System.out.println("Compiling...");
		
		final long currentTimeMillis = System.currentTimeMillis();
		
//		Retrieve Lists for all distinct BoundingVolume3F instances:
		final List<AxisAlignedBoundingBox3F> distinctAxisAlignedBoundingBoxes = NodeFilter.filterAllDistinct(scene, AxisAlignedBoundingBox3F.class);
		final List<BoundingSphere3F> distinctBoundingSpheres = NodeFilter.filterAllDistinct(scene, BoundingSphere3F.class);
		final List<InfiniteBoundingVolume3F> distinctInfiniteBoundingVolumes = NodeFilter.filterAllDistinct(scene, InfiniteBoundingVolume3F.class);
		final List<BoundingVolume3F> distinctBoundingVolumes = Lists.merge(distinctAxisAlignedBoundingBoxes, distinctBoundingSpheres, distinctInfiniteBoundingVolumes);
		
//		Retrieve Lists for all distinct Shape3F instances:
		final List<Plane3F> distinctPlanes = NodeFilter.filterAllDistinct(scene, Plane3F.class);
		final List<RectangularCuboid3F> distinctRectangularCuboids = NodeFilter.filterAllDistinct(scene, RectangularCuboid3F.class);
		final List<Sphere3F> distinctSpheres = NodeFilter.filterAllDistinct(scene, Sphere3F.class);
		final List<Torus3F> distinctToruses = NodeFilter.filterAllDistinct(scene, Torus3F.class);
		final List<Triangle3F> distinctTriangles = NodeFilter.filterAllDistinct(scene, Triangle3F.class);
		final List<Shape3F> distinctShapes = Lists.merge(distinctPlanes, distinctRectangularCuboids, distinctSpheres, distinctToruses, distinctTriangles);
		
//		Retrieve Lists for all distinct Texture instances:
		final List<BlendTexture> distinctBlendTextures = NodeFilter.filterAllDistinct(scene, BlendTexture.class);
		final List<BullseyeTexture> distinctBullseyeTextures = NodeFilter.filterAllDistinct(scene, BullseyeTexture.class);
		final List<CheckerboardTexture> distinctCheckerboardTextures = NodeFilter.filterAllDistinct(scene, CheckerboardTexture.class);
		final List<ConstantTexture> distinctConstantTextures = NodeFilter.filterAllDistinct(scene, ConstantTexture.class);
		final List<FunctionTexture> distinctFunctionTextures = NodeFilter.filterAllDistinct(scene, FunctionTexture.class);
		final List<ImageTexture> distinctImageTextures = NodeFilter.filterAllDistinct(scene, ImageTexture.class);
		final List<MarbleTexture> distinctMarbleTextures = NodeFilter.filterAllDistinct(scene, MarbleTexture.class);
		final List<SimplexFractionalBrownianMotionTexture> distinctSimplexFractionalBrownianMotionTextures = NodeFilter.filterAllDistinct(scene, SimplexFractionalBrownianMotionTexture.class);
		final List<SurfaceNormalTexture> distinctSurfaceNormalTextures = NodeFilter.filterAllDistinct(scene, SurfaceNormalTexture.class);
		final List<UVTexture> distinctUVTextures = NodeFilter.filterAllDistinct(scene, UVTexture.class);
		
//		Retrieve a List of filtered Primitive instances:
		final List<Primitive> filteredPrimitives = doFilterPrimitives(scene, distinctBoundingVolumes, distinctShapes);
		
//		Retrieve index mappings for all distinct BoundingVolume3F instances:
		final Map<AxisAlignedBoundingBox3F, Integer> distinctToOffsetsAxisAlignedBoundingBoxes = NodeFilter.mapDistinctToOffsets(distinctAxisAlignedBoundingBoxes, AxisAlignedBoundingBox3F.ARRAY_SIZE);
		final Map<BoundingSphere3F, Integer> distinctToOffsetsBoundingSpheres = NodeFilter.mapDistinctToOffsets(distinctBoundingSpheres, BoundingSphere3F.ARRAY_SIZE);
		
//		Retrieve index mappings for all distinct Shape3F instances:
		final Map<Plane3F, Integer> distinctToOffsetsPlanes = NodeFilter.mapDistinctToOffsets(distinctPlanes, Plane3F.ARRAY_SIZE);
		final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboids = NodeFilter.mapDistinctToOffsets(distinctRectangularCuboids, RectangularCuboid3F.ARRAY_SIZE);
		final Map<Sphere3F, Integer> distinctToOffsetsSpheres = NodeFilter.mapDistinctToOffsets(distinctSpheres, Sphere3F.ARRAY_SIZE);
		final Map<Torus3F, Integer> distinctToOffsetsToruses = NodeFilter.mapDistinctToOffsets(distinctToruses, Torus3F.ARRAY_SIZE);
		final Map<Triangle3F, Integer> distinctToOffsetsTriangles = NodeFilter.mapDistinctToOffsets(distinctTriangles, Triangle3F.ARRAY_SIZE);
		
//		Retrieve index mappings for all distinct Texture instances:
		final Map<BlendTexture, Integer> distinctToOffsetsBlendTextures = NodeFilter.mapDistinctToOffsets(distinctBlendTextures, BlendTexture.ARRAY_SIZE);
		final Map<BullseyeTexture, Integer> distinctToOffsetsBullseyeTextures = NodeFilter.mapDistinctToOffsets(distinctBullseyeTextures, BullseyeTexture.ARRAY_SIZE);
		final Map<CheckerboardTexture, Integer> distinctToOffsetsCheckerboardTextures = NodeFilter.mapDistinctToOffsets(distinctCheckerboardTextures, CheckerboardTexture.ARRAY_SIZE);
		final Map<ConstantTexture, Integer> distinctToOffsetsConstantTextures = NodeFilter.mapDistinctToOffsets(distinctConstantTextures, ConstantTexture.ARRAY_SIZE);
		final Map<ImageTexture, Integer> distinctToOffsetsImageTextures = NodeFilter.mapDistinctToOffsets(distinctImageTextures, imageTexture -> imageTexture.getArraySize());
		final Map<MarbleTexture, Integer> distinctToOffsetsMarbleTextures = NodeFilter.mapDistinctToOffsets(distinctMarbleTextures, MarbleTexture.ARRAY_SIZE);
		final Map<SimplexFractionalBrownianMotionTexture, Integer> distinctToOffsetsSimplexFractionalBrownianMotionTextures = NodeFilter.mapDistinctToOffsets(distinctSimplexFractionalBrownianMotionTextures, SimplexFractionalBrownianMotionTexture.ARRAY_SIZE);
		
//		Retrieve the float[] for all BoundingVolume3F instances:
		final float[] boundingVolume3FAxisAlignedBoundingBox3FArray = Floats.toArray(distinctAxisAlignedBoundingBoxes, axisAlignedBoundingBox -> axisAlignedBoundingBox.toArray(), 1);
		final float[] boundingVolume3FBoundingSphere3FArray = Floats.toArray(distinctBoundingSpheres, boundingSphere -> boundingSphere.toArray(), 1);
		
//		Retrieve the float[] for the Camera instance:
		final float[] cameraArray = scene.getCamera().toArray();
		
//		Retrieve the float[] for the Matrix44F instances:
		final float[] matrix44FArray = doCreateMatrix44FArray(filteredPrimitives);
		
//		Retrieve the float[] for all Shape3F instances:
		final float[] shape3FPlane3FArray = Floats.toArray(distinctPlanes, plane -> plane.toArray(), 1);
		final float[] shape3FRectangularCuboid3FArray = Floats.toArray(distinctRectangularCuboids, rectangularCuboid -> rectangularCuboid.toArray(), 1);
		final float[] shape3FSphere3FArray = Floats.toArray(distinctSpheres, sphere -> sphere.toArray(), 1);
		final float[] shape3FTorus3FArray = Floats.toArray(distinctToruses, torus -> torus.toArray(), 1);
		final float[] shape3FTriangle3FArray = Floats.toArray(distinctTriangles, triangle -> triangle.toArray(), 1);
		
//		Retrieve the float[] for all Texture instances:
		final float[] textureBlendTextureArray = Floats.toArray(distinctBlendTextures, blendTexture -> blendTexture.toArray(), 1);
		final float[] textureBullseyeTextureArray = Floats.toArray(distinctBullseyeTextures, bullseyeTexture -> bullseyeTexture.toArray(), 1);
		final float[] textureCheckerboardTextureArray = Floats.toArray(distinctCheckerboardTextures, checkerboardTexture -> checkerboardTexture.toArray(), 1);
		final float[] textureConstantTextureArray = Floats.toArray(distinctConstantTextures, constantTexture -> constantTexture.toArray(), 1);
		final float[] textureImageTextureArray = Floats.toArray(distinctImageTextures, imageTexture -> imageTexture.toArray(), 1);
		final float[] textureMarbleTextureArray = Floats.toArray(distinctMarbleTextures, marbleTexture -> marbleTexture.toArray(), 1);
		final float[] textureSimplexFractionalBrownianMotionTextureArray = Floats.toArray(distinctSimplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture -> simplexFractionalBrownianMotionTexture.toArray(), 1);
		
//		Retrieve the int[] for all primitives:
		final int[] primitiveArray = Primitive.toArray(filteredPrimitives);
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArrayWithBoundingVolumes(filteredPrimitives, distinctToOffsetsAxisAlignedBoundingBoxes, distinctToOffsetsBoundingSpheres, primitiveArray);
		doPopulatePrimitiveArrayWithShapes(filteredPrimitives, distinctToOffsetsPlanes, distinctToOffsetsRectangularCuboids, distinctToOffsetsSpheres, distinctToOffsetsToruses, distinctToOffsetsTriangles, primitiveArray);
		
		final
		CompiledScene compiledScene = new CompiledScene();
		compiledScene.setBoundingVolume3FAxisAlignedBoundingBox3FArray(boundingVolume3FAxisAlignedBoundingBox3FArray);
		compiledScene.setBoundingVolume3FBoundingSphere3FArray(boundingVolume3FBoundingSphere3FArray);
		compiledScene.setCameraArray(cameraArray);
		compiledScene.setMatrix44FArray(matrix44FArray);
		compiledScene.setPrimitiveArray(primitiveArray);
		compiledScene.setShape3FPlane3FArray(shape3FPlane3FArray);
		compiledScene.setShape3FRectangularCuboid3FArray(shape3FRectangularCuboid3FArray);
		compiledScene.setShape3FSphere3FArray(shape3FSphere3FArray);
		compiledScene.setShape3FTorus3FArray(shape3FTorus3FArray);
		compiledScene.setShape3FTriangle3FArray(shape3FTriangle3FArray);
		
		final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
		
		System.out.println("- Compilation took " + elapsedTimeMillis + " milliseconds.");
		
		return compiledScene;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static List<Primitive> doFilterPrimitives(final Scene scene, final List<BoundingVolume3F> distinctBoundingVolumes, final List<Shape3F> distinctShapes) {
		return scene.getPrimitives().stream().filter(primitive -> distinctBoundingVolumes.contains(primitive.getBoundingVolume()) && distinctShapes.contains(primitive.getShape())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
	}
	
	private static float[] doCreateMatrix44FArray(final List<Primitive> primitives) {
		return Floats.toArray(primitives, primitive -> primitive.getTransform().toArray(), 1);
	}
	
	private static void doPopulatePrimitiveArrayWithBoundingVolumes(final List<Primitive> primitives, final Map<AxisAlignedBoundingBox3F, Integer> distinctToOffsetsAxisAlignedBoundingBoxes, final Map<BoundingSphere3F, Integer> distinctToOffsetsBoundingSpheres, final int[] primitiveArray) {
		for(int i = 0; i < primitives.size(); i++) {
			final Primitive primitive = primitives.get(i);
			
			final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
			
			if(boundingVolume instanceof AxisAlignedBoundingBox3F) {
				final int axisAlignedBoundingBoxOffset = distinctToOffsetsAxisAlignedBoundingBoxes.get(AxisAlignedBoundingBox3F.class.cast(boundingVolume)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = axisAlignedBoundingBoxOffset;
			} else if(boundingVolume instanceof BoundingSphere3F) {
				final int boundingSphereOffset = distinctToOffsetsBoundingSpheres.get(BoundingSphere3F.class.cast(boundingVolume)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = boundingSphereOffset;
			} else if(boundingVolume instanceof InfiniteBoundingVolume3F) {
				final int infiniteBoundingVolumeOffset = 0;
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = infiniteBoundingVolumeOffset;
			}
		}
	}
	
	private static void doPopulatePrimitiveArrayWithShapes(final List<Primitive> primitives, final Map<Plane3F, Integer> distinctToOffsetsPlanes, final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboids, final Map<Sphere3F, Integer> distinctToOffsetsSpheres, final Map<Torus3F, Integer> distinctToOffsetsToruses, final Map<Triangle3F, Integer> distinctToOffsetsTriangles, final int[] primitiveArray) {
		for(int i = 0; i < primitives.size(); i++) {
			final Primitive primitive = primitives.get(i);
			
			final Shape3F shape = primitive.getShape();
			
			if(shape instanceof Plane3F) {
				final int planeOffset = distinctToOffsetsPlanes.get(Plane3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = planeOffset;
			} else if(shape instanceof RectangularCuboid3F) {
				final int rectangularCuboidOffset = distinctToOffsetsRectangularCuboids.get(RectangularCuboid3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = rectangularCuboidOffset;
			} else if(shape instanceof Sphere3F) {
				final int sphereOffset = distinctToOffsetsSpheres.get(Sphere3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = sphereOffset;
			} else if(shape instanceof Torus3F) {
				final int torusOffset = distinctToOffsetsToruses.get(Torus3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = torusOffset;
			} else if(shape instanceof Triangle3F) {
				final int triangleOffset = distinctToOffsetsTriangles.get(Triangle3F.class.cast(shape)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = triangleOffset;
			}
		}
	}
}