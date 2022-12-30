/**
 * Copyright 2014 - 2023 J&#246;rgen Lundgren
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
package org.dayflower.javafx.texture;

import java.util.Objects;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Quaternion4F;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Sphere3F;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.renderer.cpu.CPURenderer;
import org.dayflower.renderer.observer.NoOpRendererObserver;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.Transform;
import org.dayflower.scene.light.DiffuseAreaLight;
import org.dayflower.scene.light.PerezLight;
import org.dayflower.scene.material.MatteMaterial;
import org.dayflower.scene.texture.Texture;

import org.macroing.art4j.color.Color3F;
import org.macroing.art4j.color.Color4F;
import org.macroing.art4j.filter.BoxFilter2F;
import org.macroing.javafx.scene.image.WritableImageCache;

import javafx.scene.image.WritableImage;

final class WritableImageCaches {
	private static final WritableImageCache<Texture> WRITABLE_IMAGE_CACHE_TEXTURE = new WritableImageCache<>(WritableImageCaches::doCreateWritableImageTexture);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private WritableImageCaches() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static WritableImage get(final Texture texture) {
		return WRITABLE_IMAGE_CACHE_TEXTURE.get(Objects.requireNonNull(texture, "texture == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Scene doCreateMaterialPreviewScene(final Material material) {
		Objects.requireNonNull(material, "material == null");
		
		final
		Camera camera = new Camera();
		camera.setResolution(16.0F, 16.0F);
		camera.setFieldOfViewY();
		camera.setOrthonormalBasis();
		
		final Material material0 = material;
		final Material material1 = new MatteMaterial();
		final Material material2 = new MatteMaterial();
		
		final Shape3F shape0 = new Sphere3F();
		final Shape3F shape1 = new Sphere3F();
		final Shape3F shape2 = new Sphere3F();
		
		final Transform transform0 = new Transform(camera.getPointInfrontOfEye(4.0F));
		final Transform transform1 = new Transform(camera.getPointBehindEye(4.0F), new Quaternion4F(), new Vector3F(2.0F, 2.0F, 2.0F));
		final Transform transform2 = new Transform(camera.getPointInfrontOfEye(8.0F));
		
		final AreaLight areaLight1 = new DiffuseAreaLight(transform1, 1, new Color3F(12.0F), shape1, true);
		
		final Primitive primitive0 = new Primitive(material0, shape0, transform0);
		final Primitive primitive1 = new Primitive(material1, shape1, transform1, areaLight1);
		final Primitive primitive2 = new Primitive(material2, shape2, transform2);
		
		final
		Scene scene = new Scene();
		scene.addLight(areaLight1);
		scene.addLight(new PerezLight());
		scene.addPrimitive(primitive0);
		scene.addPrimitive(primitive1);
		scene.addPrimitive(primitive2);
		scene.setCamera(camera);
		scene.setName("Preview");
		
		return scene;
	}
	
	private static WritableImage doCreateWritableImageTexture(final Texture texture) {
		final
		CombinedProgressiveImageOrderRenderer combinedProgressiveImageOrderRenderer = new CPURenderer(new NoOpRendererObserver());
		combinedProgressiveImageOrderRenderer.setImage(new PixelImageF(16, 16, Color4F.BLACK, new BoxFilter2F()));
		combinedProgressiveImageOrderRenderer.setPreviewMode(true);
		combinedProgressiveImageOrderRenderer.setRenderingAlgorithm(RenderingAlgorithm.PATH_TRACING);
		combinedProgressiveImageOrderRenderer.setScene(doCreateMaterialPreviewScene(new MatteMaterial(texture)));
		
		for(int i = 0; i < 10; i++) {
			combinedProgressiveImageOrderRenderer.render();
		}
		
		final
		ImageF imageF = combinedProgressiveImageOrderRenderer.getImage();
		imageF.drawShape(new Rectangle2I(new Point2I(0, 0), new Point2I(imageF.getResolutionX() - 1, imageF.getResolutionY() - 1)), new Color4F(181, 181, 181));
		
		return imageF.toWritableImage();
	}
}