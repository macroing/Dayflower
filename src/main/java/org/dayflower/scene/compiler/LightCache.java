/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.geometry.shape.Triangle3F;
import org.dayflower.scene.Light;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.DirectionalLight;
import org.dayflower.scene.light.ImageLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.SpotLight;

import org.macroing.java.util.visitor.Node;
import org.macroing.java.util.visitor.NodeCache;
import org.macroing.java.util.visitor.NodeFilter;

final class LightCache {
	private final List<DiffuseAreaLight> distinctDiffuseAreaLights;
	private final List<DirectionalLight> distinctDirectionalLights;
	private final List<ImageLight> distinctImageLights;
	private final List<Light> distinctLights;
	private final List<PerezLight> distinctPerezLights;
	private final List<PointLight> distinctPointLights;
	private final List<SpotLight> distinctSpotLights;
	private final Map<DiffuseAreaLight, Integer> distinctToOffsetsDiffuseAreaLights;
	private final Map<DirectionalLight, Integer> distinctToOffsetsDirectionalLights;
	private final Map<ImageLight, Integer> distinctToOffsetsImageLights;
	private final Map<PerezLight, Integer> distinctToOffsetsPerezLights;
	private final Map<PointLight, Integer> distinctToOffsetsPointLights;
	private final Map<SpotLight, Integer> distinctToOffsetsSpotLights;
	private final NodeCache nodeCache;
	private final Shape3FCache shape3FCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public LightCache(final NodeCache nodeCache, final Shape3FCache shape3FCache) {
		this.nodeCache = Objects.requireNonNull(nodeCache, "nodeCache == null");
		this.shape3FCache = Objects.requireNonNull(shape3FCache, "shape3FCache == null");
		this.distinctDiffuseAreaLights = new ArrayList<>();
		this.distinctDirectionalLights = new ArrayList<>();
		this.distinctImageLights = new ArrayList<>();
		this.distinctLights = new ArrayList<>();
		this.distinctPerezLights = new ArrayList<>();
		this.distinctPointLights = new ArrayList<>();
		this.distinctSpotLights = new ArrayList<>();
		this.distinctToOffsetsDiffuseAreaLights = new LinkedHashMap<>();
		this.distinctToOffsetsDirectionalLights = new LinkedHashMap<>();
		this.distinctToOffsetsImageLights = new LinkedHashMap<>();
		this.distinctToOffsetsPerezLights = new LinkedHashMap<>();
		this.distinctToOffsetsPointLights = new LinkedHashMap<>();
		this.distinctToOffsetsSpotLights = new LinkedHashMap<>();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean contains(final Light light) {
		return this.distinctLights.contains(Objects.requireNonNull(light, "light == null"));
	}
	
	public float[] toDiffuseAreaLights() {
		return CompiledLightCache.toDiffuseAreaLights(this.distinctDiffuseAreaLights, this.shape3FCache::findOffsetFor);
	}
	
	public float[] toDirectionalLights() {
		return CompiledLightCache.toDirectionalLights(this.distinctDirectionalLights);
	}
	
	public float[] toImageLights() {
		return CompiledLightCache.toImageLights(this.distinctImageLights);
	}
	
	public float[] toPerezLights() {
		return CompiledLightCache.toPerezLights(this.distinctPerezLights);
	}
	
	public float[] toPointLights() {
		return CompiledLightCache.toPointLights(this.distinctPointLights);
	}
	
	public float[] toSpotLights() {
		return CompiledLightCache.toSpotLights(this.distinctSpotLights);
	}
	
	public int findOffsetFor(final Light light) {
		Objects.requireNonNull(light, "light == null");
		
		if(light instanceof DiffuseAreaLight) {
			return this.distinctToOffsetsDiffuseAreaLights.get(light).intValue();
		} else if(light instanceof DirectionalLight) {
			return this.distinctToOffsetsDirectionalLights.get(light).intValue();
		} else if(light instanceof ImageLight) {
			return this.distinctToOffsetsImageLights.get(light).intValue();
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
	
	public int[] toImageLightOffsets() {
		return CompiledLightCache.toImageLightOffsets(this.distinctImageLights);
	}
	
	public int[] toLightIDsAndOffsets() {
		return CompiledLightCache.toLightIDsAndOffsets(this.distinctLights, this::findOffsetFor);
	}
	
	public int[] toPerezLightOffsets() {
		return CompiledLightCache.toPerezLightOffsets(this.distinctPerezLights);
	}
	
	public void build(final CompiledLightCache compiledLightCache) {
		compiledLightCache.setDiffuseAreaLights(toDiffuseAreaLights());
		compiledLightCache.setDirectionalLights(toDirectionalLights());
		compiledLightCache.setImageLightOffsets(toImageLightOffsets());
		compiledLightCache.setImageLights(toImageLights());
		compiledLightCache.setLightIDsAndOffsets(toLightIDsAndOffsets());
		compiledLightCache.setPerezLightOffsets(toPerezLightOffsets());
		compiledLightCache.setPerezLights(toPerezLights());
		compiledLightCache.setPointLights(toPointLights());
		compiledLightCache.setSpotLights(toSpotLights());
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledLightCache());
	}
	
	public void clear() {
		this.distinctDiffuseAreaLights.clear();
		this.distinctDirectionalLights.clear();
		this.distinctImageLights.clear();
		this.distinctLights.clear();
		this.distinctPerezLights.clear();
		this.distinctPointLights.clear();
		this.distinctSpotLights.clear();
		this.distinctToOffsetsDiffuseAreaLights.clear();
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsImageLights.clear();
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsSpotLights.clear();
	}
	
	public void setup() {
//		Add all distinct DiffuseAreaLight instances:
		this.distinctDiffuseAreaLights.clear();
		this.distinctDiffuseAreaLights.addAll(this.nodeCache.getAllDistinct(DiffuseAreaLight.class));
		
//		Add all distinct DirectionalLight instances:
		this.distinctDirectionalLights.clear();
		this.distinctDirectionalLights.addAll(this.nodeCache.getAllDistinct(DirectionalLight.class));
		
//		Add all distinct ImageLight instances:
		this.distinctImageLights.clear();
		this.distinctImageLights.addAll(this.nodeCache.getAllDistinct(ImageLight.class));
		
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
		this.distinctLights.addAll(this.distinctImageLights);
		this.distinctLights.addAll(this.distinctPerezLights);
		this.distinctLights.addAll(this.distinctPointLights);
		this.distinctLights.addAll(this.distinctSpotLights);
		
		/*
		 * The below offset mappings will only work as long as all affected Light instances are not modified during compilation.
		 */
		
//		Create offset mappings for all distinct DiffuseAreaLight instances:
		this.distinctToOffsetsDiffuseAreaLights.clear();
		this.distinctToOffsetsDiffuseAreaLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDiffuseAreaLights));
		
//		Create offset mappings for all distinct DirectionalLight instances:
		this.distinctToOffsetsDirectionalLights.clear();
		this.distinctToOffsetsDirectionalLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctDirectionalLights));
		
//		Create offset mappings for all distinct ImageLight instances:
		this.distinctToOffsetsImageLights.clear();
		this.distinctToOffsetsImageLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctImageLights));
		
//		Create offset mappings for all distinct PerezLight instances:
		this.distinctToOffsetsPerezLights.clear();
		this.distinctToOffsetsPerezLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPerezLights));
		
//		Create offset mappings for all distinct PointLight instances:
		this.distinctToOffsetsPointLights.clear();
		this.distinctToOffsetsPointLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctPointLights));
		
//		Create offset mappings for all distinct SpotLight instances:
		this.distinctToOffsetsSpotLights.clear();
		this.distinctToOffsetsSpotLights.putAll(NodeFilter.mapDistinctToOffsets(this.distinctSpotLights));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static boolean filter(final Node node) {
		return node instanceof DiffuseAreaLight ? doFilterDiffuseAreaLight(DiffuseAreaLight.class.cast(node)) : node instanceof Light;
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
}