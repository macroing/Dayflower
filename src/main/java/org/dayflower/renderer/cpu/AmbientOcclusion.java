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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.normalize;
import static org.dayflower.utility.Floats.saturate;

import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Scene;

final class AmbientOcclusion {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private AmbientOcclusion() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final boolean isPreviewMode, final float maximumDistance, final int samples) {
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(optionalIntersection.isPresent()) {
			Color3F radiance = Color3F.BLACK;
			
			final Intersection intersection = optionalIntersection.get();
			
			final OrthonormalBasis33F orthonormalBasisGWorldSpace = intersection.getOrthonormalBasisG();
			
			for(int sample = 0; sample < samples; sample++) {
				final Vector3F directionWorldSpace = Vector3F.normalize(Vector3F.transform(SampleGeneratorF.sampleHemisphereUniformDistribution(), orthonormalBasisGWorldSpace));
				
				final Ray3F rayWorldSpaceShadow = intersection.createRay(directionWorldSpace);
				
				if(maximumDistance > 0.0F) {
					final Optional<Intersection> optionalIntersectionShadow = scene.intersection(rayWorldSpaceShadow, T_MINIMUM, T_MAXIMUM);
					
					if(optionalIntersectionShadow.isPresent()) {
						final Intersection intersectionShadow = optionalIntersectionShadow.get();
						
						final float t = intersectionShadow.getT();
						
						radiance = Color3F.add(radiance, new Color3F(normalize(saturate(t, 0.0F, maximumDistance), 0.0F, maximumDistance)));
					} else {
						radiance = Color3F.add(radiance, Color3F.WHITE);
					}
				} else if(!scene.intersects(rayWorldSpaceShadow, T_MINIMUM, T_MAXIMUM)) {
					radiance = Color3F.add(radiance, Color3F.WHITE);
				}
			}
			
			radiance = Color3F.multiply(radiance, PI / samples);
			radiance = Color3F.divide(radiance, PI);
			
			return radiance;
		} else if(isPreviewMode) {
			return Color3F.WHITE;
		} else {
			return Color3F.BLACK;
		}
	}
}