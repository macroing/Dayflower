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

import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.pack;
import static org.dayflower.utility.Ints.padding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector2F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.node.NodeFilter;
import org.dayflower.scene.Light;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.LDRImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.utility.Floats;

final class LightCache {
	private final List<DiffuseAreaLight> distinctDiffuseAreaLights;
	private final List<DirectionalLight> distinctDirectionalLights;
	private final List<LDRImageLight> distinctLDRImageLights;
	private final List<Light> distinctLights;
	private final List<PerezLight> distinctPerezLights;
	private final List<PointLight> distinctPointLights;
	private final List<SpotLight> distinctSpotLights;
	private final Map<DiffuseAreaLight, Integer> distinctToOffsetsDiffuseAreaLights;
	private final Map<DirectionalLight, Integer> distinctToOffsetsDirectionalLights;
	private final Map<LDRImageLight, Integer> distinctToOffsetsLDRImageLights;
	private final Map<PerezLight, Integer> distinctToOffsetsPerezLights;
	private final Map<PointLight, Integer> distinctToOffsetsPointLights;
	private final Map<SpotLight, Integer> distinctToOffsetsSpotLights;
	private final NodeCache nodeCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public LightCache(final NodeCache nodeCache) {
		this.nodeCache = nodeCache;
		this.distinctDiffuseAreaLights = new ArrayList<>();
		this.distinctDirectionalLights = new ArrayList<>();
		this.distinctLDRImageLights = new ArrayList<>();
		this.distinctLights = new ArrayList<>();
		this.distinctPerezLights = new ArrayList<>();
		this.distinctPointLights = new ArrayList<>();
		this.distinctSpotLights = new ArrayList<>();
		this.distinctToOffsetsDiffuseAreaLights = new LinkedHashMap<>();
		this.distinctToOffsetsDirectionalLights = new LinkedHashMap<>();
		this.distinctToOffsetsLDRImageLights = new LinkedHashMap<>();
		this.distinctToOffsetsPerezLights = new LinkedHashMap<>();
		this.distinctToOffsetsPointLights = new LinkedHashMap<>();
		this.distinctToOffsetsSpotLights = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final Light light) {
		return this.distinctLights.contains(Objects.requireNonNull(light, "light == null"));
	}
	
	public float[] toLightDiffuseAreaLightArray(final Shape3FCache shape3FCache) {
		Objects.requireNonNull(shape3FCache, "shape3FCache == null");
		
		final float[] lightDiffuseAreaLightArray = Floats.toArray(this.distinctDiffuseAreaLights, diffuseAreaLight -> doToArray(diffuseAreaLight), 1);
		
		for(int i = 0; i < this.distinctDiffuseAreaLights.size(); i++) {
			final DiffuseAreaLight diffuseAreaLight = this.distinctDiffuseAreaLights.get(i);
			
			final Shape3F shape = diffuseAreaLight.getShape();
			
			final int lightDiffuseAreaLightArrayShapeOffset = i * CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH + CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_OFFSET;
			
			lightDiffuseAreaLightArray[lightDiffuseAreaLightArrayShapeOffset] = shape3FCache.findOffsetFor(shape);
		}
		
		return lightDiffuseAreaLightArray;
	}
	
	public float[] toLightDirectionalLightArray() {
		return Floats.toArray(this.distinctDirectionalLights, directionalLight -> doToArray(directionalLight), 1);
	}
	
	public float[] toLightLDRImageLightArray() {
		return Floats.toArray(this.distinctLDRImageLights, lDRImageLight -> doToArray(lDRImageLight), 1);
	}
	
	public float[] toLightPerezLightArray() {
		return Floats.toArray(this.distinctPerezLights, perezLight -> doToArray(perezLight), 1);
	}
	
	public float[] toLightPointLightArray() {
		return Floats.toArray(this.distinctPointLights, pointLight -> doToArray(pointLight), 1);
	}
	
	public float[] toLightSpotLightArray() {
		return Floats.toArray(this.distinctSpotLights, spotLight -> doToArray(spotLight), 1);
	}
	
	public int findOffsetFor(final Light light) {
		Objects.requireNonNull(light, "light == null");
		
		if(light instanceof DiffuseAreaLight) {
			return this.distinctToOffsetsDiffuseAreaLights.get(light).intValue();
		} else if(light instanceof DirectionalLight) {
			return this.distinctToOffsetsDirectionalLights.get(light).intValue();
		} else if(light instanceof LDRImageLight) {
			return this.distinctToOffsetsLDRImageLights.get(light).intValue();
		} else if(light instanceof PerezLight) {
			return this.distinctToOffsetsPerezLights.get(light).intValue();
		} else if(light instanceof PointLight) {
			return this.distinctToOffsetsPointLights.get(light).intValue();
		} else if(light instanceof SpotLight) {
			return this.distinctToOffsetsSpotLights.get(light).intValue();
		} else {
			return 0;
		}
	}
	
	public int[] toLightIDAndOffsetArray() {
		final int[] lightIDAndOffsetArray = new int[max(this.distinctLights.size(), 1)];
		
		for(int i = 0; i < this.distinctLights.size(); i++) {
			final Light light = this.distinctLights.get(i);
			
			lightIDAndOffsetArray[i] = pack(light.getID(), findOffsetFor(light));
		}
		
		return lightIDAndOffsetArray;
	}
	
	public int[] toLightLDRImageLightOffsetArray() {
		final int[] lightLDRImageLightOffsetArray = new int[max(this.distinctLDRImageLights.size(), 1)];
		
		for(int i = 0; i < this.distinctLDRImageLights.size(); i++) {
			lightLDRImageLightOffsetArray[i] = this.distinctToOffsetsLDRImageLights.get(this.distinctLDRImageLights.get(i)).intValue();
		}
		
		return lightLDRImageLightOffsetArray;
	}
	
	public int[] toLightPerezLightOffsetArray() {
		final int[] lightPerezLightOffsetArray = new int[max(this.distinctPerezLights.size(), 1)];
		
		for(int i = 0; i < this.distinctPerezLights.size(); i++) {
			lightPerezLightOffsetArray[i] = this.distinctToOffsetsPerezLights.get(this.distinctPerezLights.get(i)).intValue();
		}
		
		return lightPerezLightOffsetArray;
	}
	
	public void clear() {
		this.distinctDiffuseAreaLights.clear();
		this.distinctDirectionalLights.clear();
		this.distinctLDRImageLights.clear();
		this.distinctLights.clear();
		this.distinctPerezLights.clear();
		this.distinctPointLights.clear();
		this.distinctSpotLights.clear();
		this.distinctToOffsetsDiffuseAreaLights.clear();
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsSpotLights.clear();
	}
	
	public void setup(final Scene scene) {
		if(this.nodeCache != null) {
			doSetupNew(scene);
		} else {
			doSetupOld(scene);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof DiffuseAreaLight ? doFilterDiffuseAreaLight(DiffuseAreaLight.class.cast(node)) : node instanceof Light;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doSetupNew(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct DiffuseAreaLight instances:
		this.distinctDiffuseAreaLights.clear();
		this.distinctDiffuseAreaLights.addAll(this.nodeCache.getAllDistinct(DiffuseAreaLight.class));
		
//		Add all distinct DirectionalLight instances:
		this.distinctDirectionalLights.clear();
		this.distinctDirectionalLights.addAll(this.nodeCache.getAllDistinct(DirectionalLight.class));
		
//		Add all distinct LDRImageLight instances:
		this.distinctLDRImageLights.clear();
		this.distinctLDRImageLights.addAll(this.nodeCache.getAllDistinct(LDRImageLight.class));
		
//		Add all distinct PerezLight instances:
		this.distinctPerezLights.clear();
		this.distinctPerezLights.addAll(this.nodeCache.getAllDistinct(PerezLight.class));
		
//		Add all distinct PointLight instances:
		this.distinctPointLights.clear();
		this.distinctPointLights.addAll(this.nodeCache.getAllDistinct(PointLight.class));
		
//		Add all distinct SpotLight instances:
		this.distinctSpotLights.clear();
		this.distinctSpotLights.addAll(this.nodeCache.getAllDistinct(SpotLight.class));
		
//		Add all distinct Light instances:
		this.distinctLights.clear();
		this.distinctLights.addAll(this.distinctDiffuseAreaLights);
		this.distinctLights.addAll(this.distinctDirectionalLights);
		this.distinctLights.addAll(this.distinctLDRImageLights);
		this.distinctLights.addAll(this.distinctPerezLights);
		this.distinctLights.addAll(this.distinctPointLights);
		this.distinctLights.addAll(this.distinctSpotLights);
		
		/*
		 * The below offset mappings will only work as long as all affected Light instances are not modified during compilation.
		 */
		
//		Create offset mappings for all distinct DiffuseAreaLight instances:
		this.distinctToOffsetsDiffuseAreaLights.clear();
		this.distinctToOffsetsDiffuseAreaLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDiffuseAreaLights, CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct DirectionalLight instances:
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights, CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct LDRImageLight instances:
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsLDRImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageLights, lDRImageLight -> doGetArrayLength(lDRImageLight)));
		
//		Create offset mappings for all distinct PerezLight instances:
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPerezLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPerezLights, perezLight -> doGetArrayLength(perezLight)));
		
//		Create offset mappings for all distinct PointLight instances:
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights, CompiledLightCache.POINT_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct SpotLight instances:
		this.distinctToOffsetsSpotLights.clear();
		this.distinctToOffsetsSpotLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpotLights, CompiledLightCache.SPOT_LIGHT_LENGTH));
	}
	
	private void doSetupOld(final Scene scene) {
		Objects.requireNonNull(scene, "scene == null");
		
//		Add all distinct Light instances:
		this.distinctLights.clear();
		this.distinctLights.addAll(NodeFilter.filterAllDistinct(scene, Light.class).stream().filter(LightCache::doFilterLight).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct DiffuseAreaLight instances:
		this.distinctDiffuseAreaLights.clear();
		this.distinctDiffuseAreaLights.addAll(this.distinctLights.stream().filter(light -> light instanceof DiffuseAreaLight).map(light -> DiffuseAreaLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct DirectionalLight instances:
		this.distinctDirectionalLights.clear();
		this.distinctDirectionalLights.addAll(this.distinctLights.stream().filter(light -> light instanceof DirectionalLight).map(light -> DirectionalLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct LDRImageLight instances:
		this.distinctLDRImageLights.clear();
		this.distinctLDRImageLights.addAll(this.distinctLights.stream().filter(light -> light instanceof LDRImageLight).map(light -> LDRImageLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct PerezLight instances:
		this.distinctPerezLights.clear();
		this.distinctPerezLights.addAll(this.distinctLights.stream().filter(light -> light instanceof PerezLight).map(light -> PerezLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct PointLight instances:
		this.distinctPointLights.clear();
		this.distinctPointLights.addAll(this.distinctLights.stream().filter(light -> light instanceof PointLight).map(light -> PointLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
//		Add all distinct SpotLight instances:
		this.distinctSpotLights.clear();
		this.distinctSpotLights.addAll(this.distinctLights.stream().filter(light -> light instanceof SpotLight).map(light -> SpotLight.class.cast(light)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
		
		/*
		 * The below offset mappings will only work as long as all affected Light instances are not modified during compilation.
		 */
		
//		Create offset mappings for all distinct DiffuseAreaLight instances:
		this.distinctToOffsetsDiffuseAreaLights.clear();
		this.distinctToOffsetsDiffuseAreaLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDiffuseAreaLights, CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct DirectionalLight instances:
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights, CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct LDRImageLight instances:
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsLDRImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageLights, lDRImageLight -> doGetArrayLength(lDRImageLight)));
		
//		Create offset mappings for all distinct PerezLight instances:
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPerezLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPerezLights, perezLight -> doGetArrayLength(perezLight)));
		
//		Create offset mappings for all distinct PointLight instances:
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights, CompiledLightCache.POINT_LIGHT_LENGTH));
		
//		Create offset mappings for all distinct SpotLight instances:
		this.distinctToOffsetsSpotLights.clear();
		this.distinctToOffsetsSpotLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpotLights, CompiledLightCache.SPOT_LIGHT_LENGTH));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilterDiffuseAreaLight(final DiffuseAreaLight diffuseAreaLight) {
		return doFilterDiffuseAreaLightByShape(diffuseAreaLight.getShape());
	}
	
	private static boolean doFilterDiffuseAreaLightByShape(final Shape3F shape) {
		if(shape instanceof Sphere3F) {
			return true;
		} else if(shape instanceof Triangle3F) {
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean doFilterLight(final Light light) {
		if(light instanceof DiffuseAreaLight) {
			return doFilterDiffuseAreaLight(DiffuseAreaLight.class.cast(light));
		} else if(light instanceof DirectionalLight) {
			return true;
		} else if(light instanceof LDRImageLight) {
			return true;
		} else if(light instanceof PerezLight) {
			return true;
		} else if(light instanceof PointLight) {
			return true;
		} else if(light instanceof SpotLight) {
			return true;
		} else {
			return false;
		}
	}
	
	private static float[] doToArray(final DiffuseAreaLight diffuseAreaLight) {
		final Color3F radianceEmitted = diffuseAreaLight.getRadianceEmitted();
		
		final Matrix44F objectToWorld = diffuseAreaLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = diffuseAreaLight.getTransform().getWorldToObject();
		
		final Shape3F shape = diffuseAreaLight.getShape();
		
		final boolean isTwoSided = diffuseAreaLight.isTwoSided();
		
		final float[] array = new float[CompiledLightCache.DIFFUSE_AREA_LIGHT_LENGTH];
		
//		Because the DiffuseAreaLight occupy 40/40 positions in five blocks, it should be aligned.
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();//Block #1
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();//Block #2
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();//Block #3
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();//Block #4
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 0] = radianceEmitted.getR();		//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 1] = radianceEmitted.getG();		//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_RADIANCE_EMITTED + 2] = radianceEmitted.getB();		//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_ID] = shape.getID();							//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_OFFSET] = 0.0F;								//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_SHAPE_SURFACE_AREA] = shape.getSurfaceArea();		//Block #5
		array[CompiledLightCache.DIFFUSE_AREA_LIGHT_OFFSET_IS_TWO_SIDED] = isTwoSided ? 1.0F : 0.0F;			//Block #5
		array[39] = 0.0F;																						//Block #5
		
		return array;
	}
	
	private static float[] doToArray(final DirectionalLight directionalLight) {
		final Color3F radiance = directionalLight.getRadiance();
		
		final Vector3F direction = Vector3F.transform(directionalLight.getTransform().getObjectToWorld(), directionalLight.getDirection());
		
		final float radius = directionalLight.getRadius();
		
		final float[] array = new float[CompiledLightCache.DIRECTIONAL_LIGHT_LENGTH];
		
//		Because the DirectionalLight occupy 8/8 positions in a block, it should be aligned.
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 0] = radiance.getR();	//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 1] = radiance.getG();	//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIANCE + 2] = radiance.getB();	//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 0] = direction.getX();//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 1] = direction.getY();//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_DIRECTION + 2] = direction.getZ();//Block #1
		array[CompiledLightCache.DIRECTIONAL_LIGHT_OFFSET_RADIUS] = radius;					//Block #1
		array[7] = 0.0F;																	//Block #1
		
		return array;
	}
	
	private static float[] doToArray(final LDRImageLight lDRImageLight) {
		final AngleF angle = lDRImageLight.getAngle();
		
		final Matrix44F objectToWorld = lDRImageLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = lDRImageLight.getTransform().getWorldToObject();
		
		final Vector2F scale = lDRImageLight.getScale();
		
		final float radius = lDRImageLight.getRadius();
		
		final float[] distribution = lDRImageLight.getDistribution().toArray();
		
		final int resolutionX = lDRImageLight.getResolutionX();
		final int resolutionY = lDRImageLight.getResolutionY();
		
		final int[] image = lDRImageLight.getImage();
		
		final float[] array = new float[doGetArrayLength(lDRImageLight)];
		
//		Block #1:
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();
		
//		Block #2:
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();
		
//		Block #3:
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();
		
//		Block #4:
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();
		
//		Block #5:
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_ANGLE_RADIANS] = angle.getRadians();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_SCALE + 0] = scale.getU();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_SCALE + 1] = scale.getV();
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_RADIUS] = radius;
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_X] = resolutionX;
		array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_RESOLUTION_Y] = resolutionY;
		array[38] = 0.0F;
		array[39] = 0.0F;
		
		for(int i = 0; i < distribution.length; i++) {
			array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_DISTRIBUTION + i] = distribution[i];
		}
		
		for(int i = 0; i < image.length; i++) {
			array[CompiledLightCache.L_D_R_IMAGE_LIGHT_OFFSET_IMAGE + i] = image[i];
		}
		
		return array;
	}
	
	private static float[] doToArray(final PerezLight perezLight) {
		final Color3F sunColor = perezLight.getSunColor();
		
		final Matrix44F objectToWorld = perezLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = perezLight.getTransform().getWorldToObject();
		
		final Vector3F sunDirectionObjectSpace = perezLight.getSunDirectionObjectSpace();
		final Vector3F sunDirectionWorldSpace = perezLight.getSunDirectionWorldSpace();
		
		final double[] perezRelativeLuminance = perezLight.getPerezRelativeLuminance();
		final double[] perezX = perezLight.getPerezX();
		final double[] perezY = perezLight.getPerezY();
		final double[] zenith = perezLight.getZenith();
		
		final float radius = perezLight.getRadius();
		final float theta = perezLight.getTheta();
		
		final float[] distribution = perezLight.getDistribution().toArray();
		
		final float[] array = new float[doGetArrayLength(perezLight)];
		
//		Block #1:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  0] = objectToWorld.getElement11();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  1] = objectToWorld.getElement12();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  2] = objectToWorld.getElement13();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  3] = objectToWorld.getElement14();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  4] = objectToWorld.getElement21();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  5] = objectToWorld.getElement22();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  6] = objectToWorld.getElement23();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  7] = objectToWorld.getElement24();
		
//		Block #2:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  8] = objectToWorld.getElement31();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD +  9] = objectToWorld.getElement32();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 10] = objectToWorld.getElement33();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 11] = objectToWorld.getElement34();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 12] = objectToWorld.getElement41();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 13] = objectToWorld.getElement42();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 14] = objectToWorld.getElement43();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_OBJECT_TO_WORLD + 15] = objectToWorld.getElement44();
		
//		Block #3:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();
		
//		Block #4:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();
		
//		Block #5:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 0] = sunColor.getR();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 1] = sunColor.getG();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_COLOR + 2] = sunColor.getB();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 0] = sunDirectionObjectSpace.getX();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 1] = sunDirectionObjectSpace.getY();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_OBJECT_SPACE + 2] = sunDirectionObjectSpace.getZ();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 0] = sunDirectionWorldSpace.getX();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 1] = sunDirectionWorldSpace.getY();
		
//		Block #6:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_SUN_DIRECTION_WORLD_SPACE + 2] = sunDirectionWorldSpace.getZ();
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 0] = toFloat(perezRelativeLuminance[0]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 1] = toFloat(perezRelativeLuminance[1]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 2] = toFloat(perezRelativeLuminance[2]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 3] = toFloat(perezRelativeLuminance[3]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_RELATIVE_LUMINANCE + 4] = toFloat(perezRelativeLuminance[4]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_X + 0] = toFloat(perezX[0]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_X + 1] = toFloat(perezX[1]);
		
//		Block #7:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_X + 2] = toFloat(perezX[2]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_X + 3] = toFloat(perezX[3]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_X + 4] = toFloat(perezX[4]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_Y + 0] = toFloat(perezY[0]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_Y + 1] = toFloat(perezY[1]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_Y + 2] = toFloat(perezY[2]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_Y + 3] = toFloat(perezY[3]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_PEREZ_Y + 4] = toFloat(perezY[4]);
		
//		Block #8:
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 0] = toFloat(zenith[0]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 1] = toFloat(zenith[1]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_ZENITH + 2] = toFloat(zenith[2]);
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_RADIUS] = radius;
		array[CompiledLightCache.PEREZ_LIGHT_OFFSET_THETA] = theta;
		
		for(int i = 0; i < distribution.length; i++) {
			array[CompiledLightCache.PEREZ_LIGHT_OFFSET_DISTRIBUTION + i] = distribution[i];
		}
		
		return array;
	}
	
	private static float[] doToArray(final PointLight pointLight) {
		final Color3F intensity = pointLight.getIntensity();
		
		final Point3F position = pointLight.getTransform().getPosition();
		
		final float[] array = new float[CompiledLightCache.POINT_LIGHT_LENGTH];
		
//		Because the PointLight occupy 8/8 positions in a block, it should be aligned.
		array[CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 0] = intensity.getR();	//Block #1
		array[CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 1] = intensity.getG();	//Block #1
		array[CompiledLightCache.POINT_LIGHT_OFFSET_INTENSITY + 2] = intensity.getB();	//Block #1
		array[CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 0] = position.getX();	//Block #1
		array[CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 1] = position.getY();	//Block #1
		array[CompiledLightCache.POINT_LIGHT_OFFSET_POSITION + 2] = position.getZ();	//Block #1
		array[6] = 0.0F;																//Block #1
		array[7] = 0.0F;																//Block #1
		
		return array;
	}
	
	private static float[] doToArray(final SpotLight spotLight) {
		final Color3F intensity = spotLight.getIntensity();
		
		final Matrix44F objectToWorld = spotLight.getTransform().getObjectToWorld();
		final Matrix44F worldToObject = spotLight.getTransform().getWorldToObject();
		
		final Point3F position = Point3F.transformAndDivide(objectToWorld, new Point3F());
		
		final float cosConeAngle = spotLight.getCosConeAngle();
		final float cosConeAngleMinusConeAngleDelta = spotLight.getCosConeAngleMinusConeAngleDelta();
		
		final float[] array = new float[CompiledLightCache.SPOT_LIGHT_LENGTH];
		
//		Because the SpotLight occupy 24/24 positions in three blocks, it should be aligned.
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  0] = worldToObject.getElement11();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  1] = worldToObject.getElement12();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  2] = worldToObject.getElement13();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  3] = worldToObject.getElement14();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  4] = worldToObject.getElement21();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  5] = worldToObject.getElement22();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  6] = worldToObject.getElement23();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  7] = worldToObject.getElement24();					//Block #1
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  8] = worldToObject.getElement31();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT +  9] = worldToObject.getElement32();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 10] = worldToObject.getElement33();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 11] = worldToObject.getElement34();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 12] = worldToObject.getElement41();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 13] = worldToObject.getElement42();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 14] = worldToObject.getElement43();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_WORLD_TO_OBJECT + 15] = worldToObject.getElement44();					//Block #2
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 0] = intensity.getR();										//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 1] = intensity.getG();										//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_INTENSITY + 2] = intensity.getB();										//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 0] = position.getX();											//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 1] = position.getY();											//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_POSITION + 2] = position.getZ();											//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_COS_CONE_ANGLE] = cosConeAngle;											//Block #3
		array[CompiledLightCache.SPOT_LIGHT_OFFSET_COS_CONE_ANGLE_MINUS_CONE_ANGLE_DELTA] = cosConeAngleMinusConeAngleDelta;//Block #3
		
		return array;
	}
	
	private static int doGetArrayLength(final LDRImageLight lDRImageLight) {
		final int a = 16 + 16 + 1 + 2 + 1 + 1 + 1;
		final int b = lDRImageLight.getDistribution().toArray().length;
		final int c = lDRImageLight.getResolution();
		
		return a + b + c + padding(a + b + c);
	}
	
	private static int doGetArrayLength(final PerezLight perezLight) {
		final int a = 16 + 16 + 3 + 3 + 3 + 5 + 5 + 5 + 3 + 1 + 1;
		final int b = perezLight.getDistribution().toArray().length;
		
		return a + b + padding(a + b);
	}
}