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

import java.lang.reflect.Field;//TODO: Refactor!
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
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
	private final NodeCache nodeCache;
	private final Shape3FCache shape3FCache;
	private final TextureCache textureCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SceneCompiler} instance.
	 */
	public SceneCompiler() {
		this.timeMillis = new AtomicLong();
		this.compiledScene = new AtomicReference<>();
		this.nodeCache = new NodeCache();
		this.boundingVolume3FCache = new BoundingVolume3FCache(this.nodeCache);
		this.lightCache = new LightCache(this.nodeCache);
		this.filteredPrimitives = new ArrayList<>();
		this.materialCache = new MaterialCache(this.nodeCache);
		this.shape3FCache = new Shape3FCache(this.nodeCache);
		this.textureCache = new TextureCache(this.nodeCache);
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
		final Optional<AreaLight> optionalAreaLight = primitive.getAreaLight();
		
		if(optionalAreaLight.isPresent() && !this.lightCache.contains(optionalAreaLight.get())) {
			return false;
		}
		
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
		final float[] primitiveMatrix44FArray = Floats.toArray(this.filteredPrimitives, primitive -> doToArray(primitive.getTransform()));
		
//		Retrieve the int[] for all primitives:
		final int[] primitiveArray = Ints.toArray(this.filteredPrimitives, primitive -> doToArray(primitive));
		
//		Populate the float[] or int[] with data:
		doPopulatePrimitiveArrayWithAreaLights(primitiveArray);
		doPopulatePrimitiveArrayWithBoundingVolumes(primitiveArray);
		doPopulatePrimitiveArrayWithMaterials(primitiveArray);
		doPopulatePrimitiveArrayWithShapes(primitiveArray);
		
		final
		CompiledScene compiledScene = new CompiledScene();
		compiledScene.getCompiledBoundingVolume3FCache().setAxisAlignedBoundingBox3Fs(this.boundingVolume3FCache.toAxisAlignedBoundingBox3Fs());
		compiledScene.getCompiledBoundingVolume3FCache().setBoundingSphere3Fs(this.boundingVolume3FCache.toBoundingSphere3Fs());
		compiledScene.getCompiledCameraCache().setCamera(CompiledCameraCache.toCamera(scene.getCamera()));
		compiledScene.getCompiledLightCache().setDiffuseAreaLights(this.lightCache.toDiffuseAreaLights(this.shape3FCache));
		compiledScene.getCompiledLightCache().setDirectionalLights(this.lightCache.toDirectionalLights());
		compiledScene.getCompiledLightCache().setLDRImageLightOffsets(this.lightCache.toLDRImageLightOffsets());
		compiledScene.getCompiledLightCache().setLDRImageLights(this.lightCache.toLDRImageLights());
		compiledScene.getCompiledLightCache().setLightIDsAndOffsets(this.lightCache.toLightIDsAndOffsets());
		compiledScene.getCompiledLightCache().setPerezLightOffsets(this.lightCache.toPerezLightOffsets());
		compiledScene.getCompiledLightCache().setPerezLights(this.lightCache.toPerezLights());
		compiledScene.getCompiledLightCache().setPointLights(this.lightCache.toPointLights());
		compiledScene.getCompiledLightCache().setSpotLights(this.lightCache.toSpotLights());
		compiledScene.getCompiledMaterialCache().setBullseyeMaterials(this.materialCache.toBullseyeMaterials());
		compiledScene.getCompiledMaterialCache().setCheckerboardMaterials(this.materialCache.toCheckerboardMaterials());
		compiledScene.getCompiledMaterialCache().setClearCoatMaterials(this.materialCache.toClearCoatMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setDisneyMaterials(this.materialCache.toDisneyMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setGlassMaterials(this.materialCache.toGlassMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setGlossyMaterials(this.materialCache.toGlossyMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setMatteMaterials(this.materialCache.toMatteMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setMetalMaterials(this.materialCache.toMetalMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setMirrorMaterials(this.materialCache.toMirrorMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setPlasticMaterials(this.materialCache.toPlasticMaterials(this.textureCache));
		compiledScene.getCompiledMaterialCache().setPolkaDotMaterials(this.materialCache.toPolkaDotMaterials());
		compiledScene.getCompiledMaterialCache().setSubstrateMaterials(this.materialCache.toSubstrateMaterials(this.textureCache));
		compiledScene.getCompiledPrimitiveCache().setPrimitiveArray(primitiveArray);
		compiledScene.getCompiledPrimitiveCache().setPrimitiveMatrix44FArray(primitiveMatrix44FArray);
		compiledScene.getCompiledShape3FCache().setShape3FCone3FArray(this.shape3FCache.toShape3FCone3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FCylinder3FArray(this.shape3FCache.toShape3FCylinder3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FDisk3FArray(this.shape3FCache.toShape3FDisk3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FHyperboloid3FArray(this.shape3FCache.toShape3FHyperboloid3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FParaboloid3FArray(this.shape3FCache.toShape3FParaboloid3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FPlane3FArray(this.shape3FCache.toShape3FPlane3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FRectangle3FArray(this.shape3FCache.toShape3FRectangle3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FRectangularCuboid3FArray(this.shape3FCache.toShape3FRectangularCuboid3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FSphere3FArray(this.shape3FCache.toShape3FSphere3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FTorus3FArray(this.shape3FCache.toShape3FTorus3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FTriangle3FArray(this.shape3FCache.toShape3FTriangle3FArray());
		compiledScene.getCompiledShape3FCache().setShape3FTriangleMesh3FArray(this.shape3FCache.toShape3FTriangleMesh3FArray(this.boundingVolume3FCache));
		compiledScene.getCompiledTextureCache().setTextureBlendTextureArray(this.textureCache.toTextureBlendTextureArray());
		compiledScene.getCompiledTextureCache().setTextureBullseyeTextureArray(this.textureCache.toTextureBullseyeTextureArray());
		compiledScene.getCompiledTextureCache().setTextureCheckerboardTextureArray(this.textureCache.toTextureCheckerboardTextureArray());
		compiledScene.getCompiledTextureCache().setTextureConstantTextureArray(this.textureCache.toTextureConstantTextureArray());
		compiledScene.getCompiledTextureCache().setTextureLDRImageTextureArray(this.textureCache.toTextureLDRImageTextureArray());
		compiledScene.getCompiledTextureCache().setTextureLDRImageTextureOffsetArray(this.textureCache.toTextureLDRImageTextureOffsetArray());
		compiledScene.getCompiledTextureCache().setTextureMarbleTextureArray(this.textureCache.toTextureMarbleTextureArray());
		compiledScene.getCompiledTextureCache().setTexturePolkaDotTextureArray(this.textureCache.toTexturePolkaDotTextureArray());
		compiledScene.getCompiledTextureCache().setTextureSimplexFractionalBrownianMotionTextureArray(this.textureCache.toTextureSimplexFractionalBrownianMotionTextureArray());
		
		this.compiledScene.set(compiledScene);
	}
	
	private void doClear() {
		this.nodeCache.clear();
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
	
	private void doPopulatePrimitiveArrayWithAreaLights(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			final int primitiveArrayOffset = i * CompiledPrimitiveCache.PRIMITIVE_LENGTH;
			
			this.filteredPrimitives.get(i).getAreaLight().ifPresent(areaLight -> {
				primitiveArray[primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID] = areaLight.getID();
				primitiveArray[primitiveArrayOffset + CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_OFFSET] = this.lightCache.findOffsetFor(areaLight);
			});
		}
	}
	
	private void doPopulatePrimitiveArrayWithBoundingVolumes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET] = this.boundingVolume3FCache.findOffsetFor(this.filteredPrimitives.get(i).getBoundingVolume());
		}
	}
	
	private void doPopulatePrimitiveArrayWithMaterials(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_OFFSET] = this.materialCache.findOffsetFor(this.filteredPrimitives.get(i).getMaterial());
		}
	}
	
	private void doPopulatePrimitiveArrayWithShapes(final int[] primitiveArray) {
		for(int i = 0; i < this.filteredPrimitives.size(); i++) {
			primitiveArray[i * CompiledPrimitiveCache.PRIMITIVE_LENGTH + CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET] = this.shape3FCache.findOffsetFor(this.filteredPrimitives.get(i).getShape());
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
		this.nodeCache.add(scene, SceneCompiler::doFilter);
		this.boundingVolume3FCache.setup(scene);
		this.lightCache.setup(scene);
		this.materialCache.setup(scene);
		this.shape3FCache.setup(scene);
		this.textureCache.setup(scene);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilter(final Node node) {
		return BoundingVolume3FCache.filter(node) || LightCache.filter(node) || MaterialCache.filter(node) || Shape3FCache.filter(node) || TextureCache.filter(node);
	}
	
	private static float[] doToArray(final Transform transform) {
		return Floats.array(transform.getObjectToWorld().toArray(), transform.getWorldToObject().toArray());
	}
	
	private static int[] doToArray(final Primitive primitive) {
		final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
		
		final Material material = primitive.getMaterial();
		
		final Optional<AreaLight> optionalAreaLight = primitive.getAreaLight();
		
		final Shape3F shape = primitive.getShape();
		
		final int[] array = new int[CompiledPrimitiveCache.PRIMITIVE_LENGTH];
		
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_ID] = optionalAreaLight.isPresent() ? optionalAreaLight.get().getID() : 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_AREA_LIGHT_OFFSET] = 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_ID] = boundingVolume.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_BOUNDING_VOLUME_OFFSET] = 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_ID] = material.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_MATERIAL_OFFSET] = 0;
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_ID] = shape.getID();
		array[CompiledPrimitiveCache.PRIMITIVE_OFFSET_SHAPE_OFFSET] = 0;
		
		return array;
	}
}