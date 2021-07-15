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
import java.util.Optional;

import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;

final class PrimitiveCache {
	private final BoundingVolume3FCache boundingVolume3FCache;
	private final LightCache lightCache;
	private final List<Primitive> primitives;
	private final MaterialCache materialCache;
	private final Shape3FCache shape3FCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public PrimitiveCache(final BoundingVolume3FCache boundingVolume3FCache, final LightCache lightCache, final MaterialCache materialCache, final Shape3FCache shape3FCache) {
		this.boundingVolume3FCache = Objects.requireNonNull(boundingVolume3FCache, "boundingVolume3FCache == null");
		this.lightCache = Objects.requireNonNull(lightCache, "lightCache == null");
		this.primitives = new ArrayList<>();
		this.materialCache = Objects.requireNonNull(materialCache, "materialCache == null");
		this.shape3FCache = Objects.requireNonNull(shape3FCache, "shape3FCache == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public float[] toMatrix44Fs() {
		return CompiledPrimitiveCache.toMatrix44Fs(this.primitives);
	}
	
	public int[] toPrimitives() {
		return CompiledPrimitiveCache.toPrimitives(this.primitives, this.lightCache::findOffsetFor, this.boundingVolume3FCache::findOffsetFor, this.materialCache::findOffsetFor, this.shape3FCache::findOffsetFor);
	}
	
	public void build(final CompiledPrimitiveCache compiledPrimitiveCache) {
		compiledPrimitiveCache.setPrimitives(toPrimitives());
		compiledPrimitiveCache.setMatrix44Fs(toMatrix44Fs());
	}
	
	public void build(final CompiledScene compiledScene) {
		build(compiledScene.getCompiledPrimitiveCache());
	}
	
	public void clear() {
		this.primitives.clear();
	}
	
	public void setup(final Scene scene) {
		this.primitives.clear();
		this.primitives.addAll(scene.getPrimitives().stream().filter(this::doFilter).collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean doFilter(final Primitive primitive) {
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
}