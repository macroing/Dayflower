/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.node.Node;
import org.dayflower.node.NodeCache;
import org.dayflower.scene.Scene;

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
	private final CameraCache cameraCache;
	private final LightCache lightCache;
	private final MaterialCache materialCache;
	private final ModifierCache modifierCache;
	private final NodeCache nodeCache;
	private final PrimitiveCache primitiveCache;
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
		this.cameraCache = new CameraCache();
		this.shape3FCache = new Shape3FCache(this.nodeCache, this.boundingVolume3FCache);
		this.lightCache = new LightCache(this.nodeCache, this.shape3FCache);
		this.modifierCache = new ModifierCache(this.nodeCache);
		this.textureCache = new TextureCache(this.nodeCache);
		this.materialCache = new MaterialCache(this.nodeCache, this.modifierCache, this.textureCache);
		this.primitiveCache = new PrimitiveCache(this.boundingVolume3FCache, this.lightCache, this.materialCache, this.shape3FCache);
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
		doBuildCompiledScene();
		doClear();
		doSetElapsedTimeMillis();
		doReportDone();
		
		return this.compiledScene.getAndSet(null);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doBuildCompiledScene() {
		final CompiledScene compiledScene = new CompiledScene();
		
		this.boundingVolume3FCache.build(compiledScene);
		this.cameraCache.build(compiledScene);
		this.lightCache.build(compiledScene);
		this.materialCache.build(compiledScene);
		this.modifierCache.build(compiledScene);
		this.primitiveCache.build(compiledScene);
		this.shape3FCache.build(compiledScene);
		this.textureCache.build(compiledScene);
		this.compiledScene.set(compiledScene);
	}
	
	private void doClear() {
		this.nodeCache.clear();
		this.boundingVolume3FCache.clear();
		this.cameraCache.clear();
		this.lightCache.clear();
		this.materialCache.clear();
		this.modifierCache.clear();
		this.primitiveCache.clear();
		this.shape3FCache.clear();
		this.textureCache.clear();
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
//		Prepare the NodeCache that will be used by BoundingVolume3FCache, LightCache, MaterialCache, Shape3FCache and TextureCache:
		this.nodeCache.add(scene, SceneCompiler::doFilter);
		
//		Setup the CameraCache:
		this.cameraCache.setup(scene);
		
//		Setup the BoundingVolume3FCache, LightCache, MaterialCache, Shape3FCache and TextureCache:
		this.boundingVolume3FCache.setup();
		this.lightCache.setup();
		this.materialCache.setup();
		this.modifierCache.setup();
		this.shape3FCache.setup();
		this.textureCache.setup();
		
//		Setup the PrimitiveCache that will use the BoundingVolume3FCache, LightCache, MaterialCache and Shape3FCache:
		this.primitiveCache.setup(scene);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doFilter(final Node node) {
		return BoundingVolume3FCache.filter(node) || LightCache.filter(node) || MaterialCache.filter(node) || ModifierCache.filter(node) || Shape3FCache.filter(node) || TextureCache.filter(node);
	}
}