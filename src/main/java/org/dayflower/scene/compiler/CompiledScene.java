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

import org.dayflower.scene.Scene;
import org.dayflower.utility.Document;

/**
 * A {@code CompiledScene} is a compiled version of a {@link Scene} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CompiledScene {
	private final CompiledBoundingVolume3FCache compiledBoundingVolume3FCache;
	private final CompiledCameraCache compiledCameraCache;
	private final CompiledLightCache compiledLightCache;
	private final CompiledMaterialCache compiledMaterialCache;
	private final CompiledModifierCache compiledModifierCache;
	private final CompiledPrimitiveCache compiledPrimitiveCache;
	private final CompiledShape3FCache compiledShape3FCache;
	private final CompiledTextureCache compiledTextureCache;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code CompiledScene} instance.
	 */
	public CompiledScene() {
		this.compiledBoundingVolume3FCache = new CompiledBoundingVolume3FCache();
		this.compiledCameraCache = new CompiledCameraCache();
		this.compiledLightCache = new CompiledLightCache();
		this.compiledMaterialCache = new CompiledMaterialCache();
		this.compiledModifierCache = new CompiledModifierCache();
		this.compiledPrimitiveCache = new CompiledPrimitiveCache();
		this.compiledShape3FCache = new CompiledShape3FCache();
		this.compiledTextureCache = new CompiledTextureCache();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link CompiledBoundingVolume3FCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledBoundingVolume3FCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledBoundingVolume3FCache getCompiledBoundingVolume3FCache() {
		return this.compiledBoundingVolume3FCache;
	}
	
	/**
	 * Returns the {@link CompiledCameraCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledCameraCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledCameraCache getCompiledCameraCache() {
		return this.compiledCameraCache;
	}
	
	/**
	 * Returns the {@link CompiledLightCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledLightCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledLightCache getCompiledLightCache() {
		return this.compiledLightCache;
	}
	
	/**
	 * Returns the {@link CompiledMaterialCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledMaterialCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledMaterialCache getCompiledMaterialCache() {
		return this.compiledMaterialCache;
	}
	
	/**
	 * Returns the {@link CompiledModifierCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledModifierCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledModifierCache getCompiledModifierCache() {
		return this.compiledModifierCache;
	}
	
	/**
	 * Returns the {@link CompiledPrimitiveCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledPrimitiveCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledPrimitiveCache getCompiledPrimitiveCache() {
		return this.compiledPrimitiveCache;
	}
	
	/**
	 * Returns the {@link CompiledShape3FCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledShape3FCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledShape3FCache getCompiledShape3FCache() {
		return this.compiledShape3FCache;
	}
	
	/**
	 * Returns the {@link CompiledTextureCache} instance associated with this {@code CompiledScene} instance.
	 * 
	 * @return the {@code CompiledTextureCache} instance associated with this {@code CompiledScene} instance
	 */
	public CompiledTextureCache getCompiledTextureCache() {
		return this.compiledTextureCache;
	}
	
	/**
	 * Writes this {@code CompiledScene} instance to {@code document}.
	 * <p>
	 * If {@code document} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param document a {@link Document} instance
	 * @throws NullPointerException thrown if, and only if, {@code document} is {@code null}
	 */
	public void write(final Document document) {
		document.line("CompiledScene {");
		document.indent();
		
		this.compiledBoundingVolume3FCache.write(document);
		this.compiledMaterialCache.write(document);
		this.compiledModifierCache.write(document);
		this.compiledShape3FCache.write(document);
		this.compiledTextureCache.write(document);
		
		document.outdent();
		document.line("}");
	}
}