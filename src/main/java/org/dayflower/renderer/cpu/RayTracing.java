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
import org.dayflower.scene.LightSample;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;

final class RayTracing {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private RayTracing() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int currentBounce) {
		Color3F radiance = Color3F.BLACK;
		
		final Sampler sampler = scene.getSampler();
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalBSDF.isPresent()) {
				return radiance(intersection.createRay(ray.getDirection()), scene, isPreviewMode, maximumBounce, currentBounce);
			}
			
			final BSDF bSDF = optionalBSDF.get();
			
			final Vector3F normal = intersection.getSurfaceNormalS();
			final Vector3F outgoing = Vector3F.negate(ray.getDirection());
			
			radiance = Color3F.add(radiance, intersection.evaluateRadianceEmitted(outgoing));
			
			for(final Light light : scene.getLights()) {
				final Sample2F sample = sampler.sample2();
				
				final Optional<LightSample> optionalLightSample = light.sampleRadianceIncoming(intersection, new Point2F(sample.getU(), sample.getV()));
				
				if(optionalLightSample.isPresent()) {
					final LightSample lightSample = optionalLightSample.get();
					
					final Color3F result = lightSample.getResult();
					
					final Vector3F incoming = lightSample.getIncoming();
					
					final float probabilityDensityFunctionValue = lightSample.getProbabilityDensityFunctionValue();
					
					if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F) {
						final Color3F scatteringResult = bSDF.evaluateDistributionFunction(BXDFType.ALL, outgoing, normal, incoming);
						
						if(!scatteringResult.isBlack() && scene.checkLightVisibility(intersection, light, lightSample)) {
							radiance = Color3F.addMultiplyAndDivide(radiance, scatteringResult, result, abs(Vector3F.dotProduct(incoming, normal)), probabilityDensityFunctionValue);
						}
					}
				}
			}
			
			if(currentBounce + 1 < maximumBounce) {
				radiance = Color3F.add(radiance, doComputeSpecularReflection(ray, sampler, scene, isPreviewMode, maximumBounce, currentBounce, bSDF, intersection));
				radiance = Color3F.add(radiance, doComputeSpecularTransmission(ray, sampler, scene, isPreviewMode, maximumBounce, currentBounce, bSDF, intersection));
			}
		} else {
			for(final Light light : scene.getLights()) {
				radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
			}
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doComputeSpecularReflection(final Ray3F ray, final Sampler sampler, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int currentBounce, final BSDF bSDF, final Intersection intersection) {
		final Vector3F normal = intersection.getSurfaceNormalS();
		final Vector3F outgoing = Vector3F.negate(ray.getDirection());
		
		final Sample2F sample = sampler.sample2();
		
		final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.SPECULAR_REFLECTION, outgoing, normal, new Point2F(sample.getU(), sample.getV()));
		
		if(optionalBSDFResult.isPresent()) {
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDFResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
			final float incomingDotNormalAbs = abs(incomingDotNormal);
			
			if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F && incomingDotNormalAbs > 0.0F) {
				return Color3F.addMultiplyAndDivide(Color3F.BLACK, result, radiance(intersection.createRay(incoming), scene, isPreviewMode, maximumBounce, currentBounce + 1), incomingDotNormalAbs, probabilityDensityFunctionValue);
			}
		}
		
		return Color3F.BLACK;
	}
	
	private static Color3F doComputeSpecularTransmission(final Ray3F ray, final Sampler sampler, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int currentBounce, final BSDF bSDF, final Intersection intersection) {
		final Vector3F normal = intersection.getSurfaceNormalS();
		final Vector3F outgoing = Vector3F.negate(ray.getDirection());
		
		final Sample2F sample = sampler.sample2();
		
		final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.SPECULAR_TRANSMISSION, outgoing, normal, new Point2F(sample.getU(), sample.getV()));
		
		if(optionalBSDFResult.isPresent()) {
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDFResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
			final float incomingDotNormalAbs = abs(incomingDotNormal);
			
			if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F && incomingDotNormalAbs > 0.0F) {
				return Color3F.addMultiplyAndDivide(Color3F.BLACK, result, radiance(intersection.createRay(incoming), scene, isPreviewMode, maximumBounce, currentBounce + 1), incomingDotNormalAbs, probabilityDensityFunctionValue);
			}
		}
		
		return Color3F.BLACK;
	}
}