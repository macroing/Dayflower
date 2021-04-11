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
package org.dayflower.example;

import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.FileRendererObserver;
import org.dayflower.scene.demo.Demo;

public class CPURendererExample {
	public static void main(String[] args) {
		CPURenderer cPURenderer = new CPURenderer(new FileRendererObserver("Image.png", true, false));
		cPURenderer.setScene(Demo.createCornellBoxScene());
		cPURenderer.setImage();
		cPURenderer.setMaximumBounce(20);
		cPURenderer.setMinimumBounceRussianRoulette(5);
		cPURenderer.setRenderPasses(1000);
		cPURenderer.setRenderPassesPerDisplayUpdate(1);
		cPURenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
		cPURenderer.render();
		cPURenderer.dispose();
	}
}