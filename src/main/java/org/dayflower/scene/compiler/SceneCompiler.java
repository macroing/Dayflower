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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;

/**
 * A {@code SceneCompiler} compiles a {@link Scene} instance into a {@link CompiledScene} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SceneCompiler {
	private final AtomicLong timeMillis;
	private final AtomicReference<CompiledScene> compiledScene;
	private final BoundingVolume3FCache boundingVolume3FCache;
	private final LightCache lightCache;
	private final List<Primitive> filteredPrimitives;
	private final MaterialCache materialCache;
	private final Shape3FCache shape3FCache;
	private final TextureCache textureCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SceneCompiler} instance.
	 */
	public SceneCompiler() {
		this.timeMillis = new AtomicLong();
		this.compiledScene = new AtomicReference<>();
		this.boundingVolume3FCache = new BoundingVolume3FCache();
		this.lightCache = new LightCache();
		this.filteredPrimitives = new ArrayList<>();
		this.materialCache = new MaterialCache();
		this.shape3FCache = new Shape3FCache();
		this.textureCache = new TextureCache();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Compiles {@code scene} into a {@link CompiledScene} instance.
	 * <p>
	 * Returns a {@code CompiledScene} instance.
	 * <p>
	 * If {@code scene} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param scene the {@link Scene} instance to compile
	 * @return a {@code CompiledScene} instance
	 * @throws NullPointerException thrown if, and only if, {@code scene} is {@code null}
	 */
	public CompiledScene compile(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
		doReportInit();
		doSetCurrentTimeMillis();
		doClear();
		doSetup(scene);
		doFilterPrimitives(scene);
		doBuildCompiledScene(scene);
		doClear();
		doSetElapsedTimeMillis();
		doReportDone();
		
		return this.compiledScene.getAndSet(null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doFilterPrimitive(final Primitive primitive) {
		if(!this.boundingVolume3FCache.contains(primitive.getBoundingVolume())) {
			return false;
		}
		
		if(!this.materialCache.contains(primitive.getMaterial())) {
			return false;
		}
		
		if(!this.shape3FCache.contains(primitive.getShape())) {
			return false;
		}
		
		return true;
	}
	
	private void doBuildCompiledScene(final Scene scene) {
//		Retrieve the float[] for the Matrix44F instances:
		final float[] matrix44FArray = Floats.toArray(this.filteredPrimitives, primitive -> primitive.getTransform().toArray(), 1);
		
//		Retrieve the int[] for all primitives:
		final int[] primitiveArray = Ints.toArray(this.filteredPrimitives, primitive -> primitive.toArray(), 1);
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArrayWithBoundingVolumes(primitiveArray);
		doPopulatePrimitiveArrayWithMaterials(primitiveArray);
		doPopulatePrimitiveArrayWithShapes(primitiveArray);
		
		final
		CompiledScene compiledScene = new CompiledScene();
		compiledScene.setBoundingVolume3FAxisAlignedBoundingBox3FArray(this.boundingVolume3FCache.toBoundingVolume3FAxisAlignedBoundingBox3FArray());
		compiledScene.setBoundingVolume3FBoundingSphere3FArray(this.boundingVolume3FCache.toBoundingVolume3FBoundingSphere3FArray());
		compiledScene.setCameraArray(scene.getCamera().toArray());
		compiledScene.setLightDirectionalLightArray(this.lightCache.toLightDirectionalLightArray());
		compiledScene.setLightIDAndOffsetArray(this.lightCache.toLightIDAndOffsetArray());
		compiledScene.setLightLDRImageLightArray(this.lightCache.toLightLDRImageLightArray());
		compiledScene.setLightLDRImageLightOffsetArray(this.lightCache.toLightLDRImageLightOffsetArray());
		compiledScene.setLightPerezLightArray(this.lightCache.toLightPerezLightArray());
		compiledScene.setLightPerezLightOffsetArray(this.lightCache.toLightPerezLightOffsetArray());
		compiledScene.setLightPointLightArray(this.lightCache.toLightPointLightArray());
		compiledScene.setLightSpotLightArray(this.lightCache.toLightSpotLightArray());
		compiledScene.setMaterialClearCoatMaterialArray(this.materialCache.toMaterialClearCoatMaterialArray(this.textureCache));
		compiledScene.setMaterialDisneyMaterialArray(this.materialCache.toMaterialDisneyMaterialArray(this.textureCache));
		compiledScene.setMaterialGlassMaterialArray(this.materialCache.toMaterialGlassMaterialArray(this.textureCache));
		compiledScene.setMaterialGlossyMaterialArray(this.materialCache.toMaterialGlossyMaterialArray(this.textureCache));
		compiledScene.setMaterialMatteMaterialArray(this.materialCache.toMaterialMatteMaterialArray(this.textureCache));
		compiledScene.setMaterialMetalMaterialArray(this.materialCache.toMaterialMetalMaterialArray(this.textureCache));
		compiledScene.setMaterialMirrorMaterialArray(this.materialCache.toMaterialMirrorMaterialArray(this.textureCache));
		compiledScene.setMaterialPlasticMaterialArray(this.materialCache.toMaterialPlasticMaterialArray(this.textureCache));
		compiledScene.setMaterialSubstrateMaterialArray(this.materialCache.toMaterialSubstrateMaterialArray(this.textureCache));
		compiledScene.setMatrix44FArray(matrix44FArray);
		compiledScene.setPrimitiveArray(primitiveArray);
		compiledScene.setShape3FCone3FArray(this.shape3FCache.toShape3FCone3FArray());
		compiledScene.setShape3FCylinder3FArray(this.shape3FCache.toShape3FCylinder3FArray());
		compiledScene.setShape3FDisk3FArray(this.shape3FCache.toShape3FDisk3FArray());
		compiledScene.setShape3FParaboloid3FArray(this.shape3FCache.toShape3FParaboloid3FArray());
		compiledScene.setShape3FPlane3FArray(this.shape3FCache.toShape3FPlane3FArray());
		compiledScene.setShape3FRectangle3FArray(this.shape3FCache.toShape3FRectangle3FArray());
		compiledScene.setShape3FRectangularCuboid3FArray(this.shape3FCache.toShape3FRectangularCuboid3FArray());
		compiledScene.setShape3FSphere3FArray(this.shape3FCache.toShape3FSphere3FArray());
		compiledScene.setShape3FTorus3FArray(this.shape3FCache.toShape3FTorus3FArray());
		compiledScene.setShape3FTriangle3FArray(this.shape3FCache.toShape3FTriangle3FArray());
		compiledScene.setShape3FTriangleMesh3FArray(this.shape3FCache.toShape3FTriangleMesh3FArray(this.boundingVolume3FCache));
		compiledScene.setTextureBlendTextureArray(this.textureCache.toTextureBlendTextureArray());
		compiledScene.setTextureBullseyeTextureArray(this.textureCache.toTextureBullseyeTextureArray());
		compiledScene.setTextureCheckerboardTextureArray(this.textureCache.toTextureCheckerboardTextureArray());
		compiledScene.setTextureConstantTextureArray(this.textureCache.toTextureConstantTextureArray());
		compiledScene.setTextureLDRImageTextureArray(this.textureCache.toTextureLDRImageTextureArray());
		compiledScene.setTextureLDRImageTextureOffsetArray(this.textureCache.toTextureLDRImageTextureOffsetArray());
		compiledScene.setTextureMarbleTextureArray(this.textureCache.toTextureMarbleTextureArray());
		compiledScene.setTextureSimplexFractionalBrownianMotionTextureArray(this.textureCache.toTextureSimplexFractionalBrownianMotionTextureArray());
		
		this.compiledScene.set(compiledScene);
	}
	
	private void doClear() {
		this.boundingVolume3FCache.clear();
		this.lightCache.clear();
		this.filteredPrimitives.clear();
		this.materialCache.clear();
		this.shape3FCache.clear();
		this.textureCache.clear();
	}
	
	private void doFilterPrimitives(final Scene scene) {
		this.filteredPrimitives.clear();
		this.filteredPrimitives.addAll(scene.getPrimitives().stream().filter(this::doFilterPrimitive).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
	}
	
	private void doPopulatePrimitiveArrayWithBoundingVolumes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_BOUNDING_VOLUME_OFFSET] = this.boundingVolume3FCache.findOffsetFor(this.filteredPrimitives.get(i).getBoundingVolume());
		}
	}
	
	private void doPopulatePrimitiveArrayWithMaterials(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_MATERIAL_OFFSET] = this.materialCache.findOffsetFor(this.filteredPrimitives.get(i).getMaterial());
		}
	}
	
	private void doPopulatePrimitiveArrayWithShapes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * Primitive.ARRAY_LENGTH + Primitive.ARRAY_OFFSET_SHAPE_OFFSET] = this.shape3FCache.findOffsetFor(this.filteredPrimitives.get(i).getShape());
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
	
	private void doSetup(final Scene scene) {
		this.boundingVolume3FCache.setup(scene);
		this.lightCache.setup(scene);
		this.materialCache.setup(scene);
		this.shape3FCache.setup(scene);
		this.textureCache.setup(scene);
	}
}