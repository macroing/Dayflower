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

import static org.dayflower.utility.Floats.random;

import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.material.smallpt.SmallPTMaterial;
import org.dayflower.scene.material.smallpt.SmallPTSample;

final class PathTracingSmallPTRecursive {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingSmallPTRecursive() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final int maximumBounce, final int minimumBounceRussianRoulette) {
		return radiance(ray, scene, maximumBounce, minimumBounceRussianRoulette, 1);
	}
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final int maximumBounce, final int minimumBounceRussianRoulette, final int bounce) {
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(bounce >= maximumBounce) {
			return Color3F.BLACK;
		} else if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			if(!(material instanceof SmallPTMaterial)) {
				return Color3F.BLACK;
			}
			
			final SmallPTMaterial smallPTMaterial = SmallPTMaterial.class.cast(material);
			
			final SmallPTSample smallPTSample = smallPTMaterial.sampleDistributionFunction(intersection);
			
			Color3F emittance = smallPTMaterial.emittance(intersection);
			Color3F reflectance = smallPTSample.getResult();
			
			final int currentBounce = bounce + 1;
			
			if(currentBounce >= minimumBounceRussianRoulette) {
				final float probability = reflectance.maximum();
				
				if(random() >= probability) {
					return emittance;
				}
				
				reflectance = Color3F.divide(reflectance, probability);
			}
			
			return Color3F.add(emittance, Color3F.multiply(reflectance, radiance(intersection.createRay(smallPTSample.getDirection()), scene, maximumBounce, minimumBounceRussianRoulette, currentBounce)));
		}
		
		Color3F radiance = Color3F.BLACK;
		
		for(final Light light : scene.getLights()) {
			radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
		}
		
		return radiance;
	}
}