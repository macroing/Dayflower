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
package org.dayflower.test;

import org.dayflower.display.FileDisplay;
import org.dayflower.image.Image;
import org.dayflower.renderer.AmbientOcclusionRenderer;
import org.dayflower.renderer.PBRTPathTracingCPURenderer;
import org.dayflower.renderer.RayCaster;
import org.dayflower.renderer.RayitoPathTracingCPURenderer;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.SmallPTIPathTracingCPURenderer;
import org.dayflower.renderer.SmallPTRPathTracingCPURenderer;

public final class RendererTest {
	private RendererTest() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void main(final String[] args) {
//		doTestAmbientOcclusionRenderer();
//		doTestPathTracerPBRT();
//		doTestPathTracerRayito();
//		doTestPathTracerSmallPTIterative();
		doTestPathTracerSmallPTRecursive();
//		doTestRayCaster();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static void doTestAmbientOcclusionRenderer() {
		final
		Renderer renderer = new AmbientOcclusionRenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newDefaultScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
	
	static void doTestPathTracerPBRT() {
		final
		Renderer renderer = new PBRTPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newPBRTScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
	
	static void doTestPathTracerRayito() {
		final
		Renderer renderer = new RayitoPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newDefaultScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
	
	static void doTestPathTracerSmallPTIterative() {
		final
		Renderer renderer = new SmallPTIPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newDefaultScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
	
	static void doTestPathTracerSmallPTRecursive() {
		final
		Renderer renderer = new SmallPTRPathTracingCPURenderer();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newDefaultScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
	
	static void doTestRayCaster() {
		final
		Renderer renderer = new RayCaster();
		renderer.setRendererConfiguration(new RendererConfiguration());
		renderer.setScene(Scenes.newBedroomScene());
		renderer.setDisplay(new FileDisplay(String.format("./generated/%s-%s.png", renderer.getClass().getSimpleName(), renderer.getScene().getName())));
		renderer.setImage(new Image((int)(renderer.getScene().getCamera().getResolutionX()), (int)(renderer.getScene().getCamera().getResolutionY())));
		renderer.render();
	}
}