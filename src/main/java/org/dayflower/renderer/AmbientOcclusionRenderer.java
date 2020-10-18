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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.normalize;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;

import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Scene;

/**
 * An {@code AmbientOcclusionRenderer} is a {@link Renderer} implementation that renders using Ambient Occlusion.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AmbientOcclusionRenderer implements Renderer {
	private final float maximumDistance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AmbientOcclusionRenderer} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AmbientOcclusionRenderer(20.0F);
	 * }
	 * </pre>
	 */
	public AmbientOcclusionRenderer() {
		this(20.0F);
	}
	
	/**
	 * Constructs a new {@code AmbientOcclusionRenderer} instance.
	 * 
	 * @param maximumDistance the maximum distance to use
	 */
	public AmbientOcclusionRenderer(final float maximumDistance) {
		this.maximumDistance = maximumDistance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Renders {@code scene} to {@code image} and displays it using {@code display}.
	 * <p>
	 * If either {@code display}, {@code image} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ambientOcclusionRenderer.render(display, image, scene, new RendererConfiguration());
	 * }
	 * </pre>
	 * 
	 * @param display the {@link Display} instance to display with
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image} or {@code scene} are {@code null}
	 */
	@Override
	public void render(final Display display, final Image image, final Scene scene) {
		render(display, image, scene, new RendererConfiguration());
	}
	
	/**
	 * Renders {@code scene} to {@code image} and displays it using {@code display}.
	 * <p>
	 * If either {@code display}, {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance to display with
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @param rendererConfiguration the {@link RendererConfiguration} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}
	 */
	@Override
	public void render(final Display display, final Image image, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final float maximumDistance = this.maximumDistance;
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Camera camera = scene.getCameraCopy();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			final long currentTimeMillis1 = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final float sampleX = random();
					final float sampleY = random();
					
					final Optional<Ray3F> optionalRayWorldSpace = camera.createPrimaryRay(x, y, sampleX, sampleY);
					
					if(optionalRayWorldSpace.isPresent()) {
						final Ray3F rayWorldSpace = optionalRayWorldSpace.get();
						
						final Color3F colorRGB = doGetRadiance(rayWorldSpace, scene, rendererConfiguration, maximumDistance);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						image.filmAddColorXYZ(x + sampleX, y + sampleY, colorXYZ);
					}
				}
			}
			
			final long currentTimeMillis2 = System.currentTimeMillis();
			final long elapsedTimeMillis = currentTimeMillis2 - currentTimeMillis1;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender(0.5F);
				
				display.update(image);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiance(final Ray3F rayWorldSpace, final Scene scene, final RendererConfiguration rendererConfiguration, final float maximumDistance) {
		Color3F radiance = Color3F.BLACK;
		
		final Optional<Intersection> optionalIntersection = scene.intersection(rayWorldSpace);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
			
			final OrthonormalBasis33F orthonormalBasisGWorldSpace = surfaceIntersectionWorldSpace.getOrthonormalBasisG();
			
			final Point3F surfaceIntersectionPointWorldSpace = surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormalGWorldSpace = surfaceIntersectionWorldSpace.getSurfaceNormalG();
			
			final int samples = rendererConfiguration.getSamples();
			
			for(int sample = 0; sample < samples; sample++) {
				final Point3F originWorldSpace = Point3F.add(surfaceIntersectionPointWorldSpace, surfaceNormalGWorldSpace, 0.0001F);
				
				final Vector3F directionWorldSpace = Vector3F.normalize(Vector3F.transform(SampleGeneratorF.sampleHemisphereUniformDistribution(), orthonormalBasisGWorldSpace));
				
				final Ray3F rayWorldSpaceShadow = new Ray3F(originWorldSpace, directionWorldSpace);
				
				if(maximumDistance > 0.0F) {
					final Optional<Intersection> optionalIntersectionShadow = scene.intersection(rayWorldSpaceShadow);
					
					if(optionalIntersectionShadow.isPresent()) {
						final Intersection intersectionShadow = optionalIntersectionShadow.get();
						
						final float t = intersectionShadow.getSurfaceIntersectionWorldSpace().getT();
						
						radiance = Color3F.add(radiance, new Color3F(normalize(saturate(t, 0.0F, maximumDistance), 0.0F, maximumDistance)));
					} else {
						radiance = Color3F.add(radiance, Color3F.WHITE);
					}
				} else if(!scene.intersects(rayWorldSpaceShadow)) {
					radiance = Color3F.add(radiance, Color3F.WHITE);
				}
			}
			
			radiance = Color3F.multiply(radiance, PI / samples);
			radiance = Color3F.divide(radiance, PI);
		}
		
		return radiance;
	}
}