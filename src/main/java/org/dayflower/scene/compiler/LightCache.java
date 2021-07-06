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

import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.pack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.geometry.Shape3F;
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
		
		final float[] lightDiffuseAreaLightArray = Floats.toArray(this.distinctDiffuseAreaLights, diffuseAreaLight -> diffuseAreaLight.toArray(), 1);
		
		for(int i = 0; i < this.distinctDiffuseAreaLights.size(); i++) {
			final DiffuseAreaLight diffuseAreaLight = this.distinctDiffuseAreaLights.get(i);
			
			final Shape3F shape = diffuseAreaLight.getShape();
			
			final int lightDiffuseAreaLightArrayShapeOffset = i * DiffuseAreaLight.ARRAY_LENGTH + DiffuseAreaLight.ARRAY_OFFSET_SHAPE_OFFSET;
			
			lightDiffuseAreaLightArray[lightDiffuseAreaLightArrayShapeOffset] = shape3FCache.findOffsetFor(shape);
		}
		
		return lightDiffuseAreaLightArray;
	}
	
	public float[] toLightDirectionalLightArray() {
		return Floats.toArray(this.distinctDirectionalLights, directionalLight -> directionalLight.toArray(), 1);
	}
	
	public float[] toLightLDRImageLightArray() {
		return Floats.toArray(this.distinctLDRImageLights, lDRImageLight -> lDRImageLight.toArray(), 1);
	}
	
	public float[] toLightPerezLightArray() {
		return Floats.toArray(this.distinctPerezLights, perezLight -> perezLight.toArray(), 1);
	}
	
	public float[] toLightPointLightArray() {
		return Floats.toArray(this.distinctPointLights, pointLight -> pointLight.toArray(), 1);
	}
	
	public float[] toLightSpotLightArray() {
		return Floats.toArray(this.distinctSpotLights, spotLight -> spotLight.toArray(), 1);
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
		this.distinctToOffsetsDiffuseAreaLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDiffuseAreaLights, DiffuseAreaLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct DirectionalLight instances:
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights, DirectionalLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct LDRImageLight instances:
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsLDRImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageLights, lDRImageLight -> lDRImageLight.getArrayLength()));
		
//		Create offset mappings for all distinct PerezLight instances:
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPerezLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPerezLights, perezLight -> perezLight.getArrayLength()));
		
//		Create offset mappings for all distinct PointLight instances:
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights, PointLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct SpotLight instances:
		this.distinctToOffsetsSpotLights.clear();
		this.distinctToOffsetsSpotLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpotLights, SpotLight.ARRAY_LENGTH));
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
		this.distinctToOffsetsDiffuseAreaLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDiffuseAreaLights, DiffuseAreaLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct DirectionalLight instances:
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights, DirectionalLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct LDRImageLight instances:
		this.distinctToOffsetsLDRImageLights.clear();
		this.distinctToOffsetsLDRImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctLDRImageLights, lDRImageLight -> lDRImageLight.getArrayLength()));
		
//		Create offset mappings for all distinct PerezLight instances:
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPerezLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPerezLights, perezLight -> perezLight.getArrayLength()));
		
//		Create offset mappings for all distinct PointLight instances:
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights, PointLight.ARRAY_LENGTH));
		
//		Create offset mappings for all distinct SpotLight instances:
		this.distinctToOffsetsSpotLights.clear();
		this.distinctToOffsetsSpotLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpotLights, SpotLight.ARRAY_LENGTH));
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
}