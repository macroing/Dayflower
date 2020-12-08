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
package org.dayflower.renderer.cpu;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sqrt;

import java.util.Optional;

import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.material.pbrt.GlassPBRTMaterial;
import org.dayflower.scene.material.pbrt.MattePBRTMaterial;
import org.dayflower.scene.material.pbrt.MetalPBRTMaterial;
import org.dayflower.scene.material.pbrt.MirrorPBRTMaterial;
import org.dayflower.scene.material.rayito.GlassRayitoMaterial;
import org.dayflower.scene.material.rayito.MatteRayitoMaterial;
import org.dayflower.scene.material.rayito.MetalRayitoMaterial;
import org.dayflower.scene.material.rayito.MirrorRayitoMaterial;

final class PathTracingSmallPTRecursive {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingSmallPTRecursive() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		return radiance(ray, rendererConfiguration, 1);
	}
	
	public static Color3F radiance(final Ray3F ray, final RendererConfiguration rendererConfiguration, final int bounce) {
		final Scene scene = rendererConfiguration.getScene();
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(bounce >= rendererConfiguration.getMaximumBounce()) {
			return Color3F.BLACK;
		} else if(optionalIntersection.isPresent()) {
			final Vector3F direction = ray.getDirection();
			
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
			final Vector3F surfaceNormalCorrectlyOriented = Vector3F.dotProduct(direction, surfaceNormal) < 0.0F ? surfaceNormal : Vector3F.negate(surfaceNormal);
			
			Color3F albedo = Color3F.GRAY;
			Color3F emittance = Color3F.BLACK;
			
			final int currentBounce = bounce + 1;
			
			if(currentBounce >= rendererConfiguration.getMinimumBounceRussianRoulette()) {
				final float probability = albedo.maximum();
				
				if(random() >= probability) {
					return emittance;
				}
				
				albedo = Color3F.divide(albedo, probability);
			}
			
			if(material instanceof MetalPBRTMaterial || material instanceof MetalRayitoMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
				final Vector3F w = Vector3F.normalize(Vector3F.reflection(direction, surfaceNormal, true));
				final Vector3F v = Vector3F.computeV(w);
				final Vector3F u = Vector3F.crossProduct(v, w);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiance(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof MattePBRTMaterial || material instanceof MatteRayitoMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiance(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof MirrorPBRTMaterial || material instanceof MirrorRayitoMaterial) {
				final Vector3F d = Vector3F.reflection(direction, surfaceNormal, true);
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiance(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof GlassPBRTMaterial || material instanceof GlassRayitoMaterial) {
				final Vector3F reflectionDirection = Vector3F.reflection(direction, surfaceNormal, true);
				
				final Ray3F reflectionRay = surfaceIntersection.createRay(reflectionDirection);
				
				final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
				
				final float etaA = 1.0F;
				final float etaB = 1.5F;
				final float eta = isEntering ? etaA / etaB : etaB / etaA;
				
				final float cosTheta = Vector3F.dotProduct(direction, surfaceNormalCorrectlyOriented);
				final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
				
				if(cosTheta2Squared < 0.0F) {
					return Color3F.add(emittance, Color3F.multiply(albedo, radiance(reflectionRay, rendererConfiguration, currentBounce)));
				}
				
				final Vector3F transmissionDirection = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(direction, eta), Vector3F.multiply(surfaceNormal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
				
				final Ray3F transmissionRay = surfaceIntersection.createRay(transmissionDirection);
				
				final float a = etaB - etaA;
				final float b = etaB + etaA;
				
				final float reflectance = fresnelDielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
				final float transmittance = 1.0F - reflectance;
				
				final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
				final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
				final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
				
				if(random() < probabilityRussianRoulette) {
					return Color3F.add(emittance, Color3F.multiply(albedo, radiance(reflectionRay, rendererConfiguration, currentBounce), probabilityRussianRouletteReflection));
				}
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiance(transmissionRay, rendererConfiguration, currentBounce), probabilityRussianRouletteTransmission));
			} else {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiance(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			}
		}
		
		Color3F radiance = Color3F.BLACK;
		
		for(final Light light : scene.getLights()) {
			radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
		}
		
		return radiance;
	}
}