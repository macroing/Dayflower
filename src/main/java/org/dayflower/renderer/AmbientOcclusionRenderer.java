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
import static org.dayflower.util.Floats.random;

import java.lang.reflect.Field;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Scene;

//TODO: Add Javadocs!
public final class AmbientOcclusionRenderer implements Renderer {
//	TODO: Add Javadocs!
	public AmbientOcclusionRenderer() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void render(final Image image, final Scene scene) {
		render(image, scene, new RendererConfiguration());
	}
	
//	TODO: Add Javadocs!
	@Override
	public void render(final Image image, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerImageUpdate = rendererConfiguration.getRenderPassesPerImageUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			long l1 = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final float sampleX = random();
					final float sampleY = random();
					
					final Optional<Ray3F> optionalRayWorldSpace = scene.getCamera().createPrimaryRay(x, y, sampleX, sampleY);
					
					if(optionalRayWorldSpace.isPresent()) {
						final Ray3F rayWorldSpace = optionalRayWorldSpace.get();
						
						final Color3F colorRGB = doGetRadiance(rayWorldSpace, scene, rendererConfiguration);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						image.filmAddColorXYZ(x + sampleX, y + sampleY, colorXYZ);
					}
				}
			}
			
			System.out.printf("Pass: %s/%s%n", Integer.toString(renderPass), Integer.toString(renderPasses));
			
			if(renderPass == 1 || renderPass % renderPassesPerImageUpdate == 0 || renderPass == renderPasses) {
				image.filmRender(0.5F);
				image.save("./generated/Image-Ambient-Occlusion.png");
			}
			
			long l2 = System.currentTimeMillis();
			long l3 = l2 - l1;
			
			System.out.println(l3 + " millis");
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiance(final Ray3F rayWorldSpace, final Scene scene, final RendererConfiguration rendererConfiguration) {
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
				
				if(!scene.isIntersecting(rayWorldSpaceShadow)) {
					radiance = Color3F.add(radiance, Color3F.WHITE);
				}
			}
			
			radiance = Color3F.multiply(radiance, PI / samples);
			radiance = Color3F.divide(radiance, PI);
		}
		
		return radiance;
	}
}