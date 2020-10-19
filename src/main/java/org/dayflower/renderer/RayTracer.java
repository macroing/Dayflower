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

import java.lang.reflect.Field;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.Ray3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.ConstantBackground;

/**
 * A {@code RayTracer} is a {@link Renderer} implementation that renders using Ray Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayTracer extends AbstractCPURenderer {
//	TODO: Add Javadocs!
	public RayTracer() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	}
	
//	TODO: Add Javadocs!
	public RayTracer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Scene scene) {
		super(display, image, rendererConfiguration, scene);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Color3F radiance(final Ray3F ray) {
		return Color3F.BLACK;
	}
}