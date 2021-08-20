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
package org.dayflower.javafx.application;

import org.dayflower.image.ByteImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.gpu.GPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;

final class Renderers {
	private Renderers() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static CombinedProgressiveImageOrderRenderer createCombinedProgressiveImageOrderRenderer(final Scene scene, final boolean isUsingGPU) {
		final Camera camera = scene.getCamera();
		
		final int resolutionX = (int)(camera.getResolutionX());
		final int resolutionY = (int)(camera.getResolutionY());
		
		final
		CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = isUsingGPU ? new GPURenderer(new NoOpRendererObserver()) : new CPURenderer(new NoOpRendererObserver());
		combinedProgressiveImageOrderRenderer.setImage(isUsingGPU ? new ByteImageF(resolutionX, resolutionY) : new PixelImageF(resolutionX, resolutionY));
		combinedProgressiveImageOrderRenderer.setSamples(1);
		combinedProgressiveImageOrderRenderer.setScene(scene);
		combinedProgressiveImageOrderRenderer.setup();
		
		return combinedProgressiveImageOrderRenderer;
	}
}