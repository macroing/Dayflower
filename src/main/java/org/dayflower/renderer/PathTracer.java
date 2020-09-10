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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.random;

import java.lang.reflect.Field;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Material;
import org.dayflower.scene.MaterialResult;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;

//TODO: Add Javadocs!
public final class PathTracer implements Renderer {
//	TODO: Add Javadocs!
	public PathTracer() {
		
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
			final long currentTimeMillis1 = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final float sampleX = random();
					final float sampleY = random();
					
					final Optional<Ray3F> optionalRay = scene.getCamera().createPrimaryRay(x, y, sampleX, sampleY);
					
					if(optionalRay.isPresent()) {
						final Ray3F ray = optionalRay.get();
						
						final Color3F colorRGB = doGetRadiance(ray, scene, rendererConfiguration);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						image.filmAddColorXYZ(x + sampleX, y + sampleY, colorXYZ);
					}
				}
			}
			
			final long currentTimeMillis2 = System.currentTimeMillis();
			final long elapsedTimeMillis = currentTimeMillis2 - currentTimeMillis1;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			if(renderPass == 1 || renderPass % renderPassesPerImageUpdate == 0 || renderPass == renderPasses) {
				image.filmRender(0.5F);
				image.save("./generated/Image-Path-Tracer.png");
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiance(final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceDiracDistribution = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F currentRayDirectionI = currentRay.getDirection();
				final Vector3F currentRayDirectionO = Vector3F.negate(currentRayDirectionI);
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final MaterialResult materialResult = material.evaluate(intersection);
				
				final BXDF selectedBXDF = materialResult.getSelectedBXDF();
				
				final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
				
				final Color3F color = materialResult.getColor();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final OrthonormalBasis33F orthonormalBasisS = surfaceIntersection.getOrthonormalBasisS();
				
				final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
				
				final Vector3F surfaceNormalG = surfaceIntersection.getSurfaceNormalG();
				final Vector3F surfaceNormalGCorrectlyOriented = Vector3F.dotProduct(currentRayDirectionO, surfaceNormalG) < 0.0F ? Vector3F.negate(surfaceNormalG) : surfaceNormalG;
				final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
				
				if(currentBounce == 0 || currentBounce == currentBounceDiracDistribution) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, material.emittance(intersection)));
				}
				
				if(selectedBXDF.isDiracDistribution()) {
					currentBounceDiracDistribution++;
				} else {
//					TODO: Add direct light sampling!
				}
				
				final BXDFResult sampleBXDFResult = selectedBXDF.sampleSolidAngle(currentRayDirectionO, surfaceNormalS, orthonormalBasisS, random(), random());
				
				final float samplePDFValue = sampleBXDFResult.getProbabilityDensityFunctionValue();
				final float sampleReflectance = sampleBXDFResult.getReflectance();
				
				if(samplePDFValue > 0.0F) {
					currentRay = new Ray3F(Point3F.add(surfaceIntersectionPoint, surfaceNormalGCorrectlyOriented, 0.00001F), Vector3F.negate(Vector3F.normalize(sampleBXDFResult.getI())));
					
					throughput = Color3F.multiply(throughput, color);
					throughput = Color3F.multiply(throughput, sampleReflectance);
					throughput = Color3F.multiply(throughput, abs(Vector3F.dotProduct(currentRay.getDirection(), surfaceNormalS)) / (samplePDFValue * selectedBXDFWeight));
				} else {
					break;
				}
				
				if(currentBounce >= minimumBounceRussianRoulette) {
					final float probability = throughput.maximum();
					
					if(random() > probability) {
						break;
					}
					
					throughput = Color3F.divide(throughput, probability);
				}
				
				currentBounce++;
			} else {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.getBackground().radiance(currentRay)));
				
				break;
			}
		}
		
		return radiance;
	}
}