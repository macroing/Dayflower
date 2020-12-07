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
package org.dayflower.javafx.application;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Material;
import org.dayflower.scene.Scene;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;
import org.dayflower.scene.preview.Previews;

import javafx.scene.image.ImageView;

final class ImageViews {
	private ImageViews() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static ImageView createPreview(final Material material) {
		final
		Renderer renderer = new CPURenderer(doCreateRendererConfiguration(doCreateRenderingAlgorithm(material), Previews.createMaterialPreviewScene(material)), new NoOpRendererObserver());
		renderer.render();
		
		final
		Image image = renderer.getRendererConfiguration().getImage();
		image.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(image.getResolutionX() - 1, image.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		final
		ImageView imageView = new ImageView();
		imageView.setImage(image.toWritableImage());
		
		return imageView;
	}
	
	public static ImageView createPreview() {
		final
		Image image = new Image(32, 32, Color3F.WHITE);
		image.drawRectangle(new Rectangle2I(new Point2I(0, 0), new Point2I(image.getResolutionX() - 1, image.getResolutionY() - 1)), new Color3F(181, 181, 181));
		
		final
		ImageView imageView = new ImageView();
		imageView.setImage(image.toWritableImage());
		
		return imageView;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static RendererConfiguration doCreateRendererConfiguration(final RenderingAlgorithm renderingAlgorithm, final Scene scene) {
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setImage(new Image(32, 32));
		rendererConfiguration.setPreviewMode(true);
		rendererConfiguration.setRenderingAlgorithm(renderingAlgorithm);
		rendererConfiguration.setRenderPasses(10);
		rendererConfiguration.setSampler(new RandomSampler());
		rendererConfiguration.setScene(scene);
		
		return rendererConfiguration;
	}
	
	private static RenderingAlgorithm doCreateRenderingAlgorithm(final Material material) {
		if(material instanceof PBRTMaterial) {
			return RenderingAlgorithm.PATH_TRACING_P_B_R_T;
		} else if(material instanceof RayitoMaterial) {
			return RenderingAlgorithm.PATH_TRACING_RAYITO;
		} else {
			return RenderingAlgorithm.PATH_TRACING;
		}
	}
}