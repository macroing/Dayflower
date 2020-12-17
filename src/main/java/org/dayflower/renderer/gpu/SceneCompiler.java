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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

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
import org.dayflower.scene.texture.Texture;
import org.dayflower.scene.texture.UVTexture;
import org.dayflower.util.Floats;
import org.dayflower.util.Lists;

final class SceneCompiler {
	private final AtomicLong timeMillis;
	private final AtomicReference<CompiledScene> compiledScene;
	private final List<AxisAlignedBoundingBox3F> distinctAxisAlignedBoundingBoxes;
	private final List<BlendTexture> distinctBlendTextures;
	private final List<BoundingSphere3F> distinctBoundingSpheres;
	private final List<BoundingVolume3F> distinctBoundingVolumes;
	private final List<BullseyeTexture> distinctBullseyeTextures;
	private final List<CheckerboardTexture> distinctCheckerboardTextures;
	private final List<ConstantTexture> distinctConstantTextures;
	private final List<FunctionTexture> distinctFunctionTextures;
	private final List<ImageTexture> distinctImageTextures;
	private final List<InfiniteBoundingVolume3F> distinctInfiniteBoundingVolumes;
	private final List<MarbleTexture> distinctMarbleTextures;
	private final List<Plane3F> distinctPlanes;
	private final List<Primitive> filteredPrimitives;
	private final List<RectangularCuboid3F> distinctRectangularCuboids;
	private final List<Shape3F> distinctShapes;
	private final List<SimplexFractionalBrownianMotionTexture> distinctSimplexFractionalBrownianMotionTextures;
	private final List<Sphere3F> distinctSpheres;
	private final List<SurfaceNormalTexture> distinctSurfaceNormalTextures;
	private final List<Torus3F> distinctToruses;
	private final List<Triangle3F> distinctTriangles;
	private final List<UVTexture> distinctUVTextures;
	private final Map<AxisAlignedBoundingBox3F, Integer> distinctToOffsetsAxisAlignedBoundingBoxes;
	private final Map<BlendTexture, Integer> distinctToOffsetsBlendTextures;
	private final Map<BoundingSphere3F, Integer> distinctToOffsetsBoundingSpheres;
	private final Map<BullseyeTexture, Integer> distinctToOffsetsBullseyeTextures;
	private final Map<CheckerboardTexture, Integer> distinctToOffsetsCheckerboardTextures;
	private final Map<ConstantTexture, Integer> distinctToOffsetsConstantTextures;
	private final Map<ImageTexture, Integer> distinctToOffsetsImageTextures;
	private final Map<MarbleTexture, Integer> distinctToOffsetsMarbleTextures;
	private final Map<Plane3F, Integer> distinctToOffsetsPlanes;
	private final Map<RectangularCuboid3F, Integer> distinctToOffsetsRectangularCuboids;
	private final Map<SimplexFractionalBrownianMotionTexture, Integer> distinctToOffsetsSimplexFractionalBrownianMotionTextures;
	private final Map<Sphere3F, Integer> distinctToOffsetsSpheres;
	private final Map<Torus3F, Integer> distinctToOffsetsToruses;
	private final Map<Triangle3F, Integer> distinctToOffsetsTriangles;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public SceneCompiler() {
		this.timeMillis = new AtomicLong();
		this.compiledScene = new AtomicReference<>();
		this.distinctAxisAlignedBoundingBoxes = new ArrayList<>();
		this.distinctBlendTextures = new ArrayList<>();
		this.distinctBoundingSpheres = new ArrayList<>();
		this.distinctBoundingVolumes = new ArrayList<>();
		this.distinctBullseyeTextures = new ArrayList<>();
		this.distinctCheckerboardTextures = new ArrayList<>();
		this.distinctConstantTextures = new ArrayList<>();
		this.distinctFunctionTextures = new ArrayList<>();
		this.distinctImageTextures = new ArrayList<>();
		this.distinctInfiniteBoundingVolumes = new ArrayList<>();
		this.distinctMarbleTextures = new ArrayList<>();
		this.distinctPlanes = new ArrayList<>();
		this.filteredPrimitives = new ArrayList<>();
		this.distinctRectangularCuboids = new ArrayList<>();
		this.distinctShapes = new ArrayList<>();
		this.distinctSimplexFractionalBrownianMotionTextures = new ArrayList<>();
		this.distinctSpheres = new ArrayList<>();
		this.distinctSurfaceNormalTextures = new ArrayList<>();
		this.distinctToruses = new ArrayList<>();
		this.distinctTriangles = new ArrayList<>();
		this.distinctUVTextures = new ArrayList<>();
		this.distinctToOffsetsAxisAlignedBoundingBoxes = new LinkedHashMap<>();
		this.distinctToOffsetsBlendTextures = new LinkedHashMap<>();
		this.distinctToOffsetsBoundingSpheres = new LinkedHashMap<>();
		this.distinctToOffsetsBullseyeTextures = new LinkedHashMap<>();
		this.distinctToOffsetsCheckerboardTextures = new LinkedHashMap<>();
		this.distinctToOffsetsConstantTextures = new LinkedHashMap<>();
		this.distinctToOffsetsImageTextures = new LinkedHashMap<>();
		this.distinctToOffsetsMarbleTextures = new LinkedHashMap<>();
		this.distinctToOffsetsPlanes = new LinkedHashMap<>();
		this.distinctToOffsetsRectangularCuboids = new LinkedHashMap<>();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures = new LinkedHashMap<>();
		this.distinctToOffsetsSpheres = new LinkedHashMap<>();
		this.distinctToOffsetsToruses = new LinkedHashMap<>();
		this.distinctToOffsetsTriangles = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public CompiledScene compile(final Scene scene) {
		doReportInit();
		doSetCurrentTimeMillis();
		doClear();
		doFilterAllDistinctBoundingVolumes(scene);
		doFilterAllDistinctShapes(scene);
		doFilterAllDistinctTextures(scene);
		doFilterPrimitives(scene);
		doMapAllDistinctBoundingVolumes();
		doMapAllDistinctShapes();
		doMapAllDistinctTextures();
		doBuildCompiledScene(scene);
		doClear();
		doSetElapsedTimeMillis();
		doReportDone();
		
		return this.compiledScene.getAndSet(null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int doFindTextureOffset(final Texture texture) {
		if(texture instanceof BlendTexture) {
			return this.distinctToOffsetsBlendTextures.get(BlendTexture.class.cast(texture)).intValue();
		} else if(texture instanceof BullseyeTexture) {
			return this.distinctToOffsetsBullseyeTextures.get(BullseyeTexture.class.cast(texture)).intValue();
		} else if(texture instanceof CheckerboardTexture) {
			return this.distinctToOffsetsCheckerboardTextures.get(CheckerboardTexture.class.cast(texture)).intValue();
		} else if(texture instanceof ConstantTexture) {
			return this.distinctToOffsetsConstantTextures.get(ConstantTexture.class.cast(texture)).intValue();
		} else if(texture instanceof FunctionTexture) {
			return 0;
		} else if(texture instanceof ImageTexture) {
			return this.distinctToOffsetsImageTextures.get(ImageTexture.class.cast(texture)).intValue();
		} else if(texture instanceof MarbleTexture) {
			return this.distinctToOffsetsMarbleTextures.get(MarbleTexture.class.cast(texture)).intValue();
		} else if(texture instanceof SimplexFractionalBrownianMotionTexture) {
			return this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.get(SimplexFractionalBrownianMotionTexture.class.cast(texture)).intValue();
		} else if(texture instanceof SurfaceNormalTexture) {
			return 0;
		} else if(texture instanceof UVTexture) {
			return 0;
		} else {
			return 0;
		}
	}
	
	private void doBuildCompiledScene(final Scene scene) {
//		Retrieve the float[] for all BoundingVolume3F instances:
		final float[] boundingVolume3FAxisAlignedBoundingBox3FArray = Floats.toArray(this.distinctAxisAlignedBoundingBoxes, axisAlignedBoundingBox -> axisAlignedBoundingBox.toArray(), 1);
		final float[] boundingVolume3FBoundingSphere3FArray = Floats.toArray(this.distinctBoundingSpheres, boundingSphere -> boundingSphere.toArray(), 1);
		
//		Retrieve the float[] for the Camera instance:
		final float[] cameraArray = scene.getCamera().toArray();
		
//		Retrieve the float[] for the Matrix44F instances:
		final float[] matrix44FArray = Floats.toArray(this.filteredPrimitives, primitive -> primitive.getTransform().toArray(), 1);
		
//		Retrieve the float[] for all Shape3F instances:
		final float[] shape3FPlane3FArray = Floats.toArray(this.distinctPlanes, plane -> plane.toArray(), 1);
		final float[] shape3FRectangularCuboid3FArray = Floats.toArray(this.distinctRectangularCuboids, rectangularCuboid -> rectangularCuboid.toArray(), 1);
		final float[] shape3FSphere3FArray = Floats.toArray(this.distinctSpheres, sphere -> sphere.toArray(), 1);
		final float[] shape3FTorus3FArray = Floats.toArray(this.distinctToruses, torus -> torus.toArray(), 1);
		final float[] shape3FTriangle3FArray = Floats.toArray(this.distinctTriangles, triangle -> triangle.toArray(), 1);
		
//		Retrieve the float[] for all Texture instances:
		final float[] textureBlendTextureArray = Floats.toArray(this.distinctBlendTextures, blendTexture -> blendTexture.toArray(), 1);
		final float[] textureBullseyeTextureArray = Floats.toArray(this.distinctBullseyeTextures, bullseyeTexture -> bullseyeTexture.toArray(), 1);
		final float[] textureCheckerboardTextureArray = Floats.toArray(this.distinctCheckerboardTextures, checkerboardTexture -> checkerboardTexture.toArray(), 1);
		final float[] textureConstantTextureArray = Floats.toArray(this.distinctConstantTextures, constantTexture -> constantTexture.toArray(), 1);
		final float[] textureImageTextureArray = Floats.toArray(this.distinctImageTextures, imageTexture -> imageTexture.toArray(), 1);
		final float[] textureMarbleTextureArray = Floats.toArray(this.distinctMarbleTextures, marbleTexture -> marbleTexture.toArray(), 1);
		final float[] textureSimplexFractionalBrownianMotionTextureArray = Floats.toArray(this.distinctSimplexFractionalBrownianMotionTextures, simplexFractionalBrownianMotionTexture -> simplexFractionalBrownianMotionTexture.toArray(), 1);
		
//		Retrieve the int[] for all primitives:
		final int[] primitiveArray = Primitive.toArray(this.filteredPrimitives);
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArrayWithBoundingVolumes(primitiveArray);
		doPopulatePrimitiveArrayWithShapes(primitiveArray);
		doPopulateTextureBlendTextureArrayWithTextures(textureBlendTextureArray);
		
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
		
		this.compiledScene.set(compiledScene);
	}
	
	private void doClear() {
		this.distinctAxisAlignedBoundingBoxes.clear();
		this.distinctBlendTextures.clear();
		this.distinctBoundingSpheres.clear();
		this.distinctBoundingVolumes.clear();
		this.distinctBullseyeTextures.clear();
		this.distinctCheckerboardTextures.clear();
		this.distinctConstantTextures.clear();
		this.distinctFunctionTextures.clear();
		this.distinctImageTextures.clear();
		this.distinctInfiniteBoundingVolumes.clear();
		this.distinctMarbleTextures.clear();
		this.distinctPlanes.clear();
		this.filteredPrimitives.clear();
		this.distinctRectangularCuboids.clear();
		this.distinctShapes.clear();
		this.distinctSimplexFractionalBrownianMotionTextures.clear();
		this.distinctSpheres.clear();
		this.distinctSurfaceNormalTextures.clear();
		this.distinctToruses.clear();
		this.distinctTriangles.clear();
		this.distinctUVTextures.clear();
		this.distinctToOffsetsAxisAlignedBoundingBoxes.clear();
		this.distinctToOffsetsBlendTextures.clear();
		this.distinctToOffsetsBoundingSpheres.clear();
		this.distinctToOffsetsBullseyeTextures.clear();
		this.distinctToOffsetsCheckerboardTextures.clear();
		this.distinctToOffsetsConstantTextures.clear();
		this.distinctToOffsetsImageTextures.clear();
		this.distinctToOffsetsMarbleTextures.clear();
		this.distinctToOffsetsPlanes.clear();
		this.distinctToOffsetsRectangularCuboids.clear();
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.clear();
		this.distinctToOffsetsSpheres.clear();
		this.distinctToOffsetsToruses.clear();
		this.distinctToOffsetsTriangles.clear();
	}
	
	private void doFilterAllDistinctBoundingVolumes(final Scene scene) {
		this.distinctAxisAlignedBoundingBoxes.addAll(NodeFilter.filterAllDistinct(scene, AxisAlignedBoundingBox3F.class));
		this.distinctBoundingSpheres.addAll(NodeFilter.filterAllDistinct(scene, BoundingSphere3F.class));
		this.distinctInfiniteBoundingVolumes.addAll(NodeFilter.filterAllDistinct(scene, InfiniteBoundingVolume3F.class));
		this.distinctBoundingVolumes.addAll(Lists.merge(this.distinctAxisAlignedBoundingBoxes, this.distinctBoundingSpheres, this.distinctInfiniteBoundingVolumes));
	}
	
	private void doFilterAllDistinctShapes(final Scene scene) {
		this.distinctPlanes.addAll(NodeFilter.filterAllDistinct(scene, Plane3F.class));
		this.distinctRectangularCuboids.addAll(NodeFilter.filterAllDistinct(scene, RectangularCuboid3F.class));
		this.distinctSpheres.addAll(NodeFilter.filterAllDistinct(scene, Sphere3F.class));
		this.distinctToruses.addAll(NodeFilter.filterAllDistinct(scene, Torus3F.class));
		this.distinctTriangles.addAll(NodeFilter.filterAllDistinct(scene, Triangle3F.class));
		this.distinctShapes.addAll(Lists.merge(this.distinctPlanes, this.distinctRectangularCuboids, this.distinctSpheres, this.distinctToruses, this.distinctTriangles));
	}
	
	private void doFilterAllDistinctTextures(final Scene scene) {
		this.distinctBlendTextures.addAll(NodeFilter.filterAllDistinct(scene, BlendTexture.class));
		this.distinctBullseyeTextures.addAll(NodeFilter.filterAllDistinct(scene, BullseyeTexture.class));
		this.distinctCheckerboardTextures.addAll(NodeFilter.filterAllDistinct(scene, CheckerboardTexture.class));
		this.distinctConstantTextures.addAll(NodeFilter.filterAllDistinct(scene, ConstantTexture.class));
		this.distinctFunctionTextures.addAll(NodeFilter.filterAllDistinct(scene, FunctionTexture.class));
		this.distinctImageTextures.addAll(NodeFilter.filterAllDistinct(scene, ImageTexture.class));
		this.distinctMarbleTextures.addAll(NodeFilter.filterAllDistinct(scene, MarbleTexture.class));
		this.distinctSimplexFractionalBrownianMotionTextures.addAll(NodeFilter.filterAllDistinct(scene, SimplexFractionalBrownianMotionTexture.class));
		this.distinctSurfaceNormalTextures.addAll(NodeFilter.filterAllDistinct(scene, SurfaceNormalTexture.class));
		this.distinctUVTextures.addAll(NodeFilter.filterAllDistinct(scene, UVTexture.class));
	}
	
	private void doFilterPrimitives(final Scene scene) {
		this.filteredPrimitives.addAll(scene.getPrimitives().stream().filter(primitive -> this.distinctBoundingVolumes.contains(primitive.getBoundingVolume()) && this.distinctShapes.contains(primitive.getShape())).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
	}
	
	private void doMapAllDistinctBoundingVolumes() {
		this.distinctToOffsetsAxisAlignedBoundingBoxes.putAll(NodeFilter.mapDistinctToOffsets(this.distinctAxisAlignedBoundingBoxes, AxisAlignedBoundingBox3F.ARRAY_SIZE));
		this.distinctToOffsetsBoundingSpheres.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBoundingSpheres, BoundingSphere3F.ARRAY_SIZE));
	}
	
	private void doMapAllDistinctShapes() {
		this.distinctToOffsetsPlanes.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPlanes, Plane3F.ARRAY_SIZE));
		this.distinctToOffsetsRectangularCuboids.putAll(NodeFilter.mapDistinctToOffsets(this.distinctRectangularCuboids, RectangularCuboid3F.ARRAY_SIZE));
		this.distinctToOffsetsSpheres.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpheres, Sphere3F.ARRAY_SIZE));
		this.distinctToOffsetsToruses.putAll(NodeFilter.mapDistinctToOffsets(this.distinctToruses, Torus3F.ARRAY_SIZE));
		this.distinctToOffsetsTriangles.putAll(NodeFilter.mapDistinctToOffsets(this.distinctTriangles, Triangle3F.ARRAY_SIZE));
	}
	
	private void doMapAllDistinctTextures() {
		this.distinctToOffsetsBlendTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBlendTextures, BlendTexture.ARRAY_SIZE));
		this.distinctToOffsetsBullseyeTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctBullseyeTextures, BullseyeTexture.ARRAY_SIZE));
		this.distinctToOffsetsCheckerboardTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctCheckerboardTextures, CheckerboardTexture.ARRAY_SIZE));
		this.distinctToOffsetsConstantTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctConstantTextures, ConstantTexture.ARRAY_SIZE));
		this.distinctToOffsetsImageTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctImageTextures, imageTexture -> imageTexture.getArraySize()));
		this.distinctToOffsetsMarbleTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctMarbleTextures, MarbleTexture.ARRAY_SIZE));
		this.distinctToOffsetsSimplexFractionalBrownianMotionTextures.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSimplexFractionalBrownianMotionTextures, SimplexFractionalBrownianMotionTexture.ARRAY_SIZE));
	}
	
	private void doPopulatePrimitiveArrayWithBoundingVolumes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final Primitive primitive = this.filteredPrimitives.get(i);
			
			final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
			
			if(boundingVolume instanceof AxisAlignedBoundingBox3F) {
				final int axisAlignedBoundingBoxOffset = this.distinctToOffsetsAxisAlignedBoundingBoxes.get(AxisAlignedBoundingBox3F.class.cast(boundingVolume)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = axisAlignedBoundingBoxOffset;
			} else if(boundingVolume instanceof BoundingSphere3F) {
				final int boundingSphereOffset = this.distinctToOffsetsBoundingSpheres.get(BoundingSphere3F.class.cast(boundingVolume)).intValue();
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = boundingSphereOffset;
			} else if(boundingVolume instanceof InfiniteBoundingVolume3F) {
				final int infiniteBoundingVolumeOffset = 0;
				final int primitiveArrayOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET;
				
				primitiveArray[primitiveArrayOffset] = infiniteBoundingVolumeOffset;
			}
		}
	}
	
	private void doPopulatePrimitiveArrayWithShapes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final Primitive primitive = this.filteredPrimitives.get(i);
			
			final Shape3F shape = primitive.getShape();
			
			final int primitiveArrayShapeOffset = i * Primitive.ARRAY_SIZE + Primitive.ARRAY_OFFSET_SHAPE_OFFSET;
			
			if(shape instanceof Plane3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsPlanes.get(Plane3F.class.cast(shape)).intValue();
			} else if(shape instanceof RectangularCuboid3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsRectangularCuboids.get(RectangularCuboid3F.class.cast(shape)).intValue();
			} else if(shape instanceof Sphere3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsSpheres.get(Sphere3F.class.cast(shape)).intValue();
			} else if(shape instanceof Torus3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsToruses.get(Torus3F.class.cast(shape)).intValue();
			} else if(shape instanceof Triangle3F) {
				primitiveArray[primitiveArrayShapeOffset] = this.distinctToOffsetsTriangles.get(Triangle3F.class.cast(shape)).intValue();
			} else {
				primitiveArray[primitiveArrayShapeOffset] = 0;
			}
		}
	}
	
	private void doPopulateTextureBlendTextureArrayWithTextures(final float[] textureBlendTextureArray) {
		for(int i = 0; i < this.distinctBlendTextures.size(); i++) {
			final BlendTexture blendTexture = this.distinctBlendTextures.get(i);
			
			final Texture textureA = blendTexture.getTextureA();
			final Texture textureB = blendTexture.getTextureB();
			
			final int textureBlendTextureArrayTextureAOffset = i * BlendTexture.ARRAY_SIZE + BlendTexture.ARRAY_OFFSET_TEXTURE_A_OFFSET;
			final int textureBlendTextureArrayTextureBOffset = i * BlendTexture.ARRAY_SIZE + BlendTexture.ARRAY_OFFSET_TEXTURE_B_OFFSET;
			
			textureBlendTextureArray[textureBlendTextureArrayTextureAOffset] = doFindTextureOffset(textureA);
			textureBlendTextureArray[textureBlendTextureArrayTextureBOffset] = doFindTextureOffset(textureB);
		}
	}
	
	private void doReportDone() {
		System.out.println("- Compilation took " + this.timeMillis.get() + " milliseconds.");
	}
	
	@SuppressWarnings("static-method")
	private void doReportInit() {
		System.out.println("Compiling...");
	}
	
	private void doSetCurrentTimeMillis() {
		this.timeMillis.set(System.currentTimeMillis());
	}
	
	private void doSetElapsedTimeMillis() {
		this.timeMillis.getAndAccumulate(System.currentTimeMillis(), (currentTimeMillisA, currentTimeMillisB) -> currentTimeMillisB - currentTimeMillisA);
	}
}