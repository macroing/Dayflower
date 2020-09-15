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
package org.dayflower.renderer;

import org.dayflower.image.Image;
import org.dayflower.scene.Scene;

/**
 * A {@code RayTracer} is a {@link Renderer} implementation that renders using Ray Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayTracer implements Renderer {
	/**
	 * Constructs a new {@code RayTracer} instance.
	 */
	public RayTracer() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Renders {@code scene} to {@code image}.
	 * <p>
	 * If either {@code image} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * rayTracer.render(image, scene, new RendererConfiguration());
	 * }
	 * </pre>
	 * 
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code scene} are {@code null}
	 */
	@Override
	public void render(final Image image, final Scene scene) {
		render(image, scene, new RendererConfiguration());
	}
	
	/**
	 * Renders {@code scene} to {@code image}.
	 * <p>
	 * If either {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @param rendererConfiguration the {@link RendererConfiguration} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}
	 */
	@Override
	public void render(final Image image, final Scene scene, final RendererConfiguration rendererConfiguration) {
//		TODO: Implement!
	}
}