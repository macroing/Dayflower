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

import java.util.Optional;

import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.Matrix44F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.image.Pixel;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.light.SpotLight;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;

import javafx.scene.image.ImageView;

final class ImageViews {
	private ImageViews() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static ImageView createPreview(final Material material) {
		final Scene scene = doCreateScene(material);
		
		final RenderingAlgorithm renderingAlgorithm = doCreateRenderingAlgorithm(material);
		
		final Renderer renderer = doCreateRenderer(renderingAlgorithm, scene);
		
		final Image image = renderer.getRendererConfiguration().getImage();
		
		doAddBorder(image);
		
		final
		ImageView imageView = new ImageView();
		imageView.setImage(image.toWritableImage());
		
		return imageView;
	}
	
	public static ImageView createPreview() {
		final Image image = new Image(32, 32);
		
		for(int i = 0; i < image.getResolution(); i++) {
			final Optional<Pixel> optionalPixel = image.getPixel(i);
			
			if(optionalPixel.isPresent()) {
				final
				Pixel pixel = optionalPixel.get();
				pixel.setAlpha(0.0F);
				pixel.setColorRGB(Color3F.BLACK);
			}
		}
		
		doAddBorder(image);
		
		final
		ImageView imageView = new ImageView();
		imageView.setImage(image.toWritableImage());
		
		return imageView;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Light doCreateLight(final Material material) {
		if(material instanceof PBRTMaterial) {
			return new SpotLight(AngleF.degrees(50.0F), AngleF.degrees(10.0F), new Color3F(200.0F), Matrix44F.translate(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 2.0F, 0.0F), new Point3F(0.0F, 2.0F, 20.0F));
		} else if(material instanceof RayitoMaterial) {
			return new PerezLight();
		} else {
			return new PerezLight();
		}
	}
	
	private static Renderer doCreateRenderer(final RenderingAlgorithm renderingAlgorithm, final Scene scene) {
		final
		RendererConfiguration rendererConfiguration = new RendererConfiguration();
		rendererConfiguration.setExcludingIntersectionMisses(true);
		rendererConfiguration.setImage(new Image(32, 32));
		rendererConfiguration.setRenderingAlgorithm(renderingAlgorithm);
		rendererConfiguration.setRenderPasses(10);
		rendererConfiguration.setSampler(new RandomSampler());
		rendererConfiguration.setScene(scene);
		
		final
		Renderer renderer = new CPURenderer(rendererConfiguration, new NoOpRendererObserver());
		renderer.render();
		
		return renderer;
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
	
	private static Scene doCreateScene(final Material material) {
		final
		Camera camera = new Camera();
		camera.setResolution(32.0F, 32.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Shape3F shape = new Sphere3F();
		
		final Transform transform = new Transform(camera.getPointInfrontOfEye(5.0F));
		
		final Primitive primitive = new Primitive(material, shape, transform);
		
		final Light light = doCreateLight(material);
		
		final
		Scene scene = new Scene();
		scene.addLight(light);
		scene.addPrimitive(primitive);
		scene.setCamera(camera);
		
		return scene;
	}
	
	private static void doAddBorder(final Image image) {
		for(int y = 0; y < image.getResolutionY(); y++) {
			for(int x = 0; x < image.getResolutionX(); x++) {
				if(x == 0 || x == image.getResolutionX() - 1 || y == 0 || y == image.getResolutionY() - 1) {
					final Optional<Pixel> optionalPixel = image.getPixel(x, y);
					
					if(optionalPixel.isPresent()) {
						final
						Pixel pixel = optionalPixel.get();
						pixel.setAlpha(1.0F);
						pixel.setColorRGB(new Color3F(181, 181, 181));
					}
				}
			}
		}
	}
}