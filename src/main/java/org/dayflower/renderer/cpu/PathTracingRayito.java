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
package org.dayflower.renderer.cpu;

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.isZero;

import java.util.List;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;

final class PathTracingRayito {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingRayito() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int minimumBounceRussianRoulette) {
		final List<Light> lights = scene.getLights();
		
		final Sampler sampler = scene.getSampler();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceSpecular = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			if(optionalIntersection.isPresent()) {
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
				
				if(!optionalBSDF.isPresent()) {
					currentRay = intersection.createRay(currentRay.getDirection());
					
					continue;
				}
				
				final BSDF bSDF = optionalBSDF.get();
				
				if(currentBounce == 0 || currentBounce == currentBounceSpecular) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, optionalIntersection.get().evaluateRadianceEmitted(Vector3F.negate(currentRay.getDirection()))));
				}
				
				if(bSDF.countBXDFsBySpecularType(false) > 0) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.sampleOneLightUniformDistribution(bSDF, intersection)));
				}
				
				final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
				final Vector3F normal = intersection.getSurfaceNormalS();
				
				final Sample2F sample = sampler.sample2();
				
				final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, normal, new Point2F(sample.getU(), sample.getV()));
				
				if(!optionalBSDFResult.isPresent()) {
					break;
				}
				
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				if(bSDFResult.getBXDFType().isSpecular()) {
					currentBounceSpecular++;
				}
				
				final Color3F result = bSDFResult.getResult();
				
				final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(result.isBlack() || isZero(probabilityDensityFunctionValue)) {
					break;
				}
				
				final Vector3F incoming = bSDFResult.getIncoming();
				
				currentRay = intersection.createRay(incoming);
				
				throughput = Color3F.multiply(throughput, Color3F.divide(Color3F.multiply(result, abs(Vector3F.dotProduct(incoming, normal))), probabilityDensityFunctionValue));
				
				if(currentBounce >= minimumBounceRussianRoulette) {
					final float probability = throughput.maximum();
					
					if(sampler.sample1().getU() > probability) {
						break;
					}
					
					throughput = Color3F.divide(throughput, probability);
				}
				
				currentBounce++;
			} else if(currentBounce == 0 && isPreviewMode) {
				return Color3F.WHITE;
			} else if(currentBounce == 0 || currentBounce == currentBounceSpecular) {
				for(final Light light : lights) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
				}
				
				break;
			} else {
				break;
			}
		}
		
		return radiance;
	}
}