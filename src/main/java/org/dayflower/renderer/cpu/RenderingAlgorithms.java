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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.normalize;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.saturate;
import static org.dayflower.util.Floats.sqrt;
import static org.dayflower.util.Ints.min;

import java.util.List;
import java.util.Optional;

import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceIncomingResult;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.BSDF;
import org.dayflower.scene.bxdf.pbrt.BSDFResult;
import org.dayflower.scene.bxdf.rayito.BXDF;
import org.dayflower.scene.bxdf.rayito.BXDFResult;
import org.dayflower.scene.light.PointLight;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.AshikhminShirleyMaterial;
import org.dayflower.scene.material.rayito.LambertianMaterial;
import org.dayflower.scene.material.rayito.MaterialResult;
import org.dayflower.scene.material.rayito.OrenNayarMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;
import org.dayflower.scene.material.rayito.ReflectionMaterial;
import org.dayflower.scene.material.rayito.RefractionMaterial;

final class RenderingAlgorithms {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private RenderingAlgorithms() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radianceAmbientOcclusion(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		Color3F radiance = Color3F.BLACK;
		
		final Scene scene = rendererConfiguration.getScene();
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
			
			final OrthonormalBasis33F orthonormalBasisGWorldSpace = surfaceIntersectionWorldSpace.getOrthonormalBasisG();
			
			final float maximumDistance = rendererConfiguration.getMaximumDistance();
			
			final int samples = rendererConfiguration.getSamples();
			
			for(int sample = 0; sample < samples; sample++) {
				final Vector3F directionWorldSpace = Vector3F.normalize(Vector3F.transform(SampleGeneratorF.sampleHemisphereUniformDistribution(), orthonormalBasisGWorldSpace));
				
				final Ray3F rayWorldSpaceShadow = surfaceIntersectionWorldSpace.createRay(directionWorldSpace);
				
				if(maximumDistance > 0.0F) {
					final Optional<Intersection> optionalIntersectionShadow = scene.intersection(rayWorldSpaceShadow, T_MINIMUM, T_MAXIMUM);
					
					if(optionalIntersectionShadow.isPresent()) {
						final Intersection intersectionShadow = optionalIntersectionShadow.get();
						
						final float t = intersectionShadow.getSurfaceIntersectionWorldSpace().getT();
						
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
		}
		
		return radiance;
	}
	
	public static Color3F radiancePathTracingPBRT(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		final Sampler sampler = rendererConfiguration.getSampler();
		
		final Scene scene = rendererConfiguration.getScene();
		
		final List<Light> lights = scene.getLights();
		
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		boolean isSpecularBounce = false;
		
		float etaScale = 1.0F;
		
		for(int currentBounce = 0; true; currentBounce++) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			final boolean hasFoundIntersection = optionalIntersection.isPresent();
			
			if(currentBounce == 0 || isSpecularBounce) {
				if(hasFoundIntersection) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, optionalIntersection.get().evaluateRadianceEmitted(Vector3F.negate(ray.getDirection()))));
				} else {
					for(final Light light : lights) {
						radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
					}
				}
			}
			
			if(!hasFoundIntersection || currentBounce >= maximumBounce) {
				break;
			}
			
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F surfaceNormalG = surfaceIntersection.getOrthonormalBasisG().getW();
			final Vector3F surfaceNormalS = surfaceIntersection.getOrthonormalBasisS().getW();
			
			if(!(material instanceof PBRTMaterial)) {
				break;
			}
			
			final PBRTMaterial pBRTMaterial = PBRTMaterial.class.cast(material);
			
			final Optional<BSDF> optionalBSDF = pBRTMaterial.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalBSDF.isPresent()) {
				currentRay = surfaceIntersection.createRay(currentRay.getDirection());
				
				currentBounce--;
				
				continue;
			}
			
			final BSDF bSDF = optionalBSDF.get();
			
			if(bSDF.countBXDFsBySpecularType(false) > 0) {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, doLightSampleOneUniformDistribution(bSDF, intersection, sampler, scene)));
			}
			
			final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
			
			final Sample2F sample = sampler.sample2();
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, new Point2F(sample.getU(), sample.getV()));
			
			if(!optionalBSDFResult.isPresent()) {
				break;
			}
			
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final BXDFType bXDFType = bSDFResult.getBXDFType();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDFResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			if(result.isBlack() || isZero(probabilityDensityFunctionValue)) {
				break;
			}
			
			throughput = Color3F.multiply(throughput, Color3F.divide(Color3F.multiply(result, abs(Vector3F.dotProduct(incoming, surfaceNormalS))), probabilityDensityFunctionValue));
			
			isSpecularBounce = bXDFType.isSpecular();
			
			if(bXDFType.hasTransmission() && bXDFType.isSpecular()) {
				final float eta = bSDF.getEta();
				
				etaScale *= Vector3F.dotProduct(outgoing, surfaceNormalG) > 0.0F ? eta * eta : 1.0F / (eta * eta);
			}
			
			currentRay = surfaceIntersection.createRay(incoming);
			
			final Color3F russianRouletteThroughput = Color3F.multiply(throughput, etaScale);
			
			if(russianRouletteThroughput.maximum() < 1.0F && currentBounce > minimumBounceRussianRoulette) {
				final float probability = max(0.05F, 1.0F - russianRouletteThroughput.maximum());
				
				if(random() < probability) {
					break;
				}
				
				throughput = Color3F.divide(throughput, 1.0F - probability);
			}
		}
		
		return radiance;
	}
	
	public static Color3F radiancePathTracingRayito(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		final Scene scene = rendererConfiguration.getScene();
		
		final List<Light> lights = scene.getLights();
		
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceDiracDistribution = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F currentRayDirectionI = currentRay.getDirection();
				final Vector3F currentRayDirectionO = Vector3F.negate(currentRayDirectionI);
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				if(!(material instanceof RayitoMaterial)) {
					break;
				}
				
				final RayitoMaterial rayitoMaterial = RayitoMaterial.class.cast(material);
				
				final MaterialResult materialResult = rayitoMaterial.evaluate(intersection);
				
				final BXDF selectedBXDF = materialResult.getSelectedBXDF();
				
				final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
				
				final Color3F color = materialResult.getColor();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final OrthonormalBasis33F orthonormalBasisS = surfaceIntersection.getOrthonormalBasisS();
				
				final Vector3F surfaceNormalS = orthonormalBasisS.getW();
				
				if(currentBounce == 0 || currentBounce == currentBounceDiracDistribution) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, rayitoMaterial.emittance(intersection)));
				}
				
				if(selectedBXDF.isDiracDistribution()) {
					currentBounceDiracDistribution++;
					
				} else {
					radiance = Color3F.add(radiance, doGetRadianceLights(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO, lights, primitive));
				}
				
				final BXDFResult selectedBXDFResult = selectedBXDF.sampleSolidAngle(currentRayDirectionO, surfaceNormalS, orthonormalBasisS, random(), random());
				
				if(!selectedBXDFResult.isFinite()) {
					System.out.printf("A BXDFResult must have finite values!%n");
					System.out.printf("BXDF: %s%n", selectedBXDF.getClass().getSimpleName());
					System.out.printf("Probability Density Function Value: %f%n", Float.valueOf(selectedBXDFResult.getProbabilityDensityFunctionValue()));
					System.out.printf("Reflectance: %f%n", Float.valueOf(selectedBXDFResult.getReflectance()));
					
					break;
				}
				
				final float probabilityDensityFunctionValue = selectedBXDFResult.getProbabilityDensityFunctionValue();
				final float reflectance = selectedBXDFResult.getReflectance();
				
				if(probabilityDensityFunctionValue > 0.0F) {
					currentRay = surfaceIntersection.createRay(Vector3F.negate(Vector3F.normalize(selectedBXDFResult.getI())));
					
					throughput = Color3F.multiply(throughput, color);
					throughput = Color3F.multiply(throughput, reflectance);
					throughput = Color3F.multiply(throughput, abs(Vector3F.dotProduct(currentRay.getDirection(), surfaceNormalS)) / (probabilityDensityFunctionValue * selectedBXDFWeight));
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
				for(final Light light : lights) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
				}
				
				break;
			}
		}
		
		return radiance;
	}
	
	public static Color3F radiancePathTracingSmallPTIterative(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		final Scene scene = rendererConfiguration.getScene();
		
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F currentDirection = currentRay.getDirection();
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
				final Vector3F surfaceNormalCorrectlyOriented = Vector3F.dotProduct(currentDirection, surfaceNormal) < 0.0F ? surfaceNormal : Vector3F.negate(surfaceNormal);
				
				Color3F albedo = material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).evaluate(intersection).getColor() : Color3F.BLACK;
				Color3F emittance = material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).emittance(intersection) : Color3F.BLACK;
				
				if(currentBounce >= minimumBounceRussianRoulette) {
					final float probability = albedo.maximum();
					
					if(random() >= probability) {
						break;
					}
					
					albedo = Color3F.divide(albedo, probability);
				}
				
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, emittance));
				
				if(material instanceof AshikhminShirleyMaterial) {
					final Vector3F s = SampleGeneratorF.sampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
					final Vector3F w = Vector3F.normalize(Vector3F.reflection(currentDirection, surfaceNormal, true));
					final Vector3F v = Vector3F.computeV(w);
					final Vector3F u = Vector3F.crossProduct(v, w);
					final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
					
//					final Vector3F s = SampleGeneratorF.sampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
//					final Vector3F w = Vector3F.normalize(Vector3F.reflection(currentDirection, surfaceNormal, true));
//					final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? new Vector3F(w.getZ(), 0.0F, -w.getX()) : new Vector3F(0.0F, -w.getZ(), w.getY()), w));
//					final Vector3F v = Vector3F.crossProduct(w, u);
//					final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
					
					currentRay = surfaceIntersection.createRay(d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
					final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
					final Vector3F w = surfaceNormalCorrectlyOriented;
					final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
					final Vector3F v = Vector3F.crossProduct(w, u);
					final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
					
//					final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
//					final Vector3F w = surfaceNormalCorrectlyOriented;
//					final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? new Vector3F(w.getZ(), 0.0F, -w.getX()) : new Vector3F(0.0F, -w.getZ(), w.getY()), w));
//					final Vector3F v = Vector3F.crossProduct(w, u);
//					final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
					
					currentRay = surfaceIntersection.createRay(d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof ReflectionMaterial) {
					final Vector3F d = Vector3F.reflection(currentDirection, surfaceNormal, true);
					
					currentRay = surfaceIntersection.createRay(d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof RefractionMaterial) {
					final Vector3F reflectionDirection = Vector3F.reflection(currentDirection, surfaceNormal, true);
					
					final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
					
					final float etaA = 1.0F;
					final float etaB = 1.5F;
					final float eta = isEntering ? etaA / etaB : etaB / etaA;
					
					final float cosTheta = Vector3F.dotProduct(currentDirection, surfaceNormalCorrectlyOriented);
					final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
					
					if(cosTheta2Squared < 0.0F) {
						currentRay = surfaceIntersection.createRay(reflectionDirection);
						
						throughput = Color3F.multiply(throughput, albedo);
					} else {
						final Vector3F transmissionDirection = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(currentDirection, eta), Vector3F.multiply(surfaceNormal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
						
						final float a = etaB - etaA;
						final float b = etaB + etaA;
						
						final float reflectance = fresnelDielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
						final float transmittance = 1.0F - reflectance;
						
						final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
						final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
						final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
						
						if(random() < probabilityRussianRoulette) {
							currentRay = surfaceIntersection.createRay(reflectionDirection);
							
							throughput = Color3F.multiply(throughput, albedo);
							throughput = Color3F.multiply(throughput, probabilityRussianRouletteReflection);
						} else {
							currentRay = surfaceIntersection.createRay(transmissionDirection);
							
							throughput = Color3F.multiply(throughput, albedo);
							throughput = Color3F.multiply(throughput, probabilityRussianRouletteTransmission);
						}
					}
				}
				
				currentBounce++;
			} else {
				for(final Light light : scene.getLights()) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
				}
				
				break;
			}
		}
		
		return radiance;
	}
	
	public static Color3F radiancePathTracingSmallPTRecursive(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		return radiancePathTracingSmallPTRecursive(ray, rendererConfiguration, 1);
	}
	
	public static Color3F radiancePathTracingSmallPTRecursive(final Ray3F ray, final RendererConfiguration rendererConfiguration, final int bounce) {
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
			
			Color3F albedo = material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).evaluate(intersection).getColor() : Color3F.BLACK;
			Color3F emittance = material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).emittance(intersection) : Color3F.BLACK;
			
			final int currentBounce = bounce + 1;
			
			if(currentBounce >= rendererConfiguration.getMinimumBounceRussianRoulette()) {
				final float probability = albedo.maximum();
				
				if(random() >= probability) {
					return emittance;
				}
				
				albedo = Color3F.divide(albedo, probability);
			}
			
			if(material instanceof AshikhminShirleyMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
				final Vector3F w = Vector3F.normalize(Vector3F.reflection(direction, surfaceNormal, true));
				final Vector3F v = Vector3F.computeV(w);
				final Vector3F u = Vector3F.crossProduct(v, w);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof ReflectionMaterial) {
				final Vector3F d = Vector3F.reflection(direction, surfaceNormal, true);
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(surfaceIntersection.createRay(d), rendererConfiguration, currentBounce)));
			} else if(material instanceof RefractionMaterial) {
				final Vector3F reflectionDirection = Vector3F.reflection(direction, surfaceNormal, true);
				
				final Ray3F reflectionRay = surfaceIntersection.createRay(reflectionDirection);
				
				final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
				
				final float etaA = 1.0F;
				final float etaB = 1.5F;
				final float eta = isEntering ? etaA / etaB : etaB / etaA;
				
				final float cosTheta = Vector3F.dotProduct(direction, surfaceNormalCorrectlyOriented);
				final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
				
				if(cosTheta2Squared < 0.0F) {
					return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(reflectionRay, rendererConfiguration, currentBounce)));
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
					return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(reflectionRay, rendererConfiguration, currentBounce), probabilityRussianRouletteReflection));
				}
				
				return Color3F.add(emittance, Color3F.multiply(albedo, radiancePathTracingSmallPTRecursive(transmissionRay, rendererConfiguration, currentBounce), probabilityRussianRouletteTransmission));
			} else {
				return Color3F.BLACK;
			}
		}
		
		Color3F radiance = Color3F.BLACK;
		
		for(final Light light : scene.getLights()) {
			radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
		}
		
		return radiance;
	}
	
	public static Color3F radianceRayCasting(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		final Sampler sampler = rendererConfiguration.getSampler();
		
		final Scene scene = rendererConfiguration.getScene();
		
		Color3F radiance = Color3F.BLACK;
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			radiance = doGetRadiance(intersection, material, sampler, scene);
		} else {
			for(final Light light : scene.getLights()) {
				radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
			}
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiance(final Intersection intersection, final Material material, final Sampler sampler, final Scene scene) {
		if(material instanceof PBRTMaterial) {
			final PBRTMaterial pBRTMaterial = PBRTMaterial.class.cast(material);
			
			final Optional<BSDF> optionalBSDF = pBRTMaterial.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(optionalBSDF.isPresent()) {
				final BSDF bSDF = optionalBSDF.get();
				
				return doLightSampleOneUniformDistribution(bSDF, intersection, sampler, scene);
			}
			
			return Color3F.BLACK;
		} else if(material instanceof RayitoMaterial) {
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormalG = surfaceIntersection.getOrthonormalBasisG().getW();
			final Vector3F surfaceNormalS = surfaceIntersection.getOrthonormalBasisS().getW();
			
			final RayitoMaterial rayitoMaterial = RayitoMaterial.class.cast(material);
			
			final MaterialResult materialResult = rayitoMaterial.evaluate(intersection);
			
			final Color3F albedo = materialResult.getColor();
			final Color3F ambient = new Color3F(0.05F);
			final Color3F diffuse = new Color3F(1.0F);
			final Color3F specular = new Color3F(1.0F);
			
			final float specularPower = 250.0F;
			
			Color3F radiance = Color3F.multiply(ambient, albedo);
			
			for(final Light light : scene.getLights()) {
				if(light instanceof PointLight) {
					final PointLight pointLight = PointLight.class.cast(light);
					
					final Color3F intensity = pointLight.getIntensity();
					
					final Point3F position = pointLight.getPosition();
					
					final Vector3F surfaceIntersectionPointToPosition = Vector3F.direction(surfaceIntersectionPoint, position);
					final Vector3F surfaceIntersectionPointToPositionNormalized = Vector3F.normalize(surfaceIntersectionPointToPosition);
					final Vector3F positionToSurfaceIntersectionPointNormalized = Vector3F.negate(surfaceIntersectionPointToPositionNormalized);
					final Vector3F reflectionDirectionNormalized = Vector3F.reflectionNormalized(positionToSurfaceIntersectionPointNormalized, surfaceNormalS, true);
					
					final Point3F origin = Point3F.add(surfaceIntersectionPoint, surfaceNormalG, 0.0001F);
					
					final Vector3F direction = surfaceIntersectionPointToPositionNormalized;
					
					final Ray3F shadowRay = new Ray3F(origin, direction);
					
					final float t = scene.intersectionT(shadowRay, T_MINIMUM, T_MAXIMUM);
					
					if(isNaN(t) || t * t > surfaceIntersectionPointToPosition.lengthSquared()) {
						final float nDotL = Vector3F.dotProduct(surfaceNormalS, surfaceIntersectionPointToPositionNormalized);
						final float rDotL = Vector3F.dotProduct(reflectionDirectionNormalized, surfaceIntersectionPointToPositionNormalized);
						
						if(nDotL > 0.0F) {
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(diffuse, Color3F.multiply(albedo, max(0.0F, nDotL)))));
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(specular, pow(max(0.0F, rDotL), specularPower))));
						}
					}
				}
			}
			
			return radiance;
		} else {
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormalG = surfaceIntersection.getOrthonormalBasisG().getW();
			final Vector3F surfaceNormalS = surfaceIntersection.getOrthonormalBasisS().getW();
			
			final Color3F albedo = Color3F.GRAY;
			final Color3F ambient = new Color3F(0.05F);
			final Color3F diffuse = new Color3F(1.0F);
			final Color3F specular = new Color3F(1.0F);
			
			final float specularPower = 250.0F;
			
			Color3F radiance = Color3F.multiply(ambient, albedo);
			
			for(final Light light : scene.getLights()) {
				if(light instanceof PointLight) {
					final PointLight pointLight = PointLight.class.cast(light);
					
					final Color3F intensity = pointLight.getIntensity();
					
					final Point3F position = pointLight.getPosition();
					
					final Vector3F surfaceIntersectionPointToPosition = Vector3F.direction(surfaceIntersectionPoint, position);
					final Vector3F surfaceIntersectionPointToPositionNormalized = Vector3F.normalize(surfaceIntersectionPointToPosition);
					final Vector3F positionToSurfaceIntersectionPointNormalized = Vector3F.negate(surfaceIntersectionPointToPositionNormalized);
					final Vector3F reflectionDirectionNormalized = Vector3F.reflectionNormalized(positionToSurfaceIntersectionPointNormalized, surfaceNormalS, true);
					
					final Point3F origin = Point3F.add(surfaceIntersectionPoint, surfaceNormalG, 0.0001F);
					
					final Vector3F direction = surfaceIntersectionPointToPositionNormalized;
					
					final Ray3F shadowRay = new Ray3F(origin, direction);
					
					final float t = scene.intersectionT(shadowRay, T_MINIMUM, T_MAXIMUM);
					
					if(isNaN(t) || t * t > surfaceIntersectionPointToPosition.lengthSquared()) {
						final float nDotL = Vector3F.dotProduct(surfaceNormalS, surfaceIntersectionPointToPositionNormalized);
						final float rDotL = Vector3F.dotProduct(reflectionDirectionNormalized, surfaceIntersectionPointToPositionNormalized);
						
						if(nDotL > 0.0F) {
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(diffuse, Color3F.multiply(albedo, max(0.0F, nDotL)))));
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(specular, pow(max(0.0F, rDotL), specularPower))));
						}
					}
				}
			}
			
			return radiance;
		}
	}
	
	private static Color3F doGetRadianceLights(final Color3F throughput, final MaterialResult materialResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO, final List<Light> lights, final Primitive primitiveToSkip) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		final BXDF selectedBXDF = materialResult.getSelectedBXDF();
		
		final Color3F color = materialResult.getColor();
		
		final float colorR = color.getR();
		final float colorG = color.getG();
		final float colorB = color.getB();
		
		final float throughputR = throughput.getR();
		final float throughputG = throughput.getG();
		final float throughputB = throughput.getB();
		
		final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
		
		final OrthonormalBasis33F orthonormalBasis = surfaceIntersection.getOrthonormalBasisS();
		
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormal = orthonormalBasis.getW();
		
		for(final Light light : lights) {
			if(light instanceof PrimitiveLight) {
				final Primitive primitive = PrimitiveLight.class.cast(light).getPrimitive();
				
				if(primitive == primitiveToSkip) {
					continue;
				}
				
				float radianceLightR = 0.0F;
				float radianceLightG = 0.0F;
				float radianceLightB = 0.0F;
				
				final int samples = 1;
				
				for(int sample = 0; sample < samples; sample++) {
					final Optional<SurfaceSample3F> optionalSurfaceSample = primitive.sample(surfaceIntersectionPoint, surfaceNormal, random(), random());
					
					if(optionalSurfaceSample.isPresent()) {
						final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
						
						final Point3F point = surfaceSample.getPoint();
						
						final float probabilityDensityFunctionValueA1 = surfaceSample.getProbabilityDensityFunctionValue();
						
						if(probabilityDensityFunctionValueA1 > 0.0F) {
							final Vector3F selectedDirectionI = Vector3F.normalize(Vector3F.direction(point, surfaceIntersectionPoint));
							final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
							
							final BXDFResult selectedBXDFResult = selectedBXDF.evaluateSolidAngle(directionO, surfaceNormal, selectedDirectionI);
							
							final float probabilityDensityFunctionValueB1 = selectedBXDFResult.getProbabilityDensityFunctionValue();
							final float reflectance = selectedBXDFResult.getReflectance();
							
							if(probabilityDensityFunctionValueB1 > 0.0F && reflectance > 0.0F) {
								final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
								
								final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
								
								if(optionalIntersection.isPresent()) {
									final Intersection intersection = optionalIntersection.get();
									
									if(primitive == intersection.getPrimitive()) {
										final Material material = primitive.getMaterial();
										
										if(material instanceof RayitoMaterial) {
											final RayitoMaterial rayitoMaterial = RayitoMaterial.class.cast(material);
											
											final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA1, probabilityDensityFunctionValueB1, 1, 1);
											
											final Color3F emittance = rayitoMaterial.emittance(intersection);
											
											final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
											final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA1 * selectedBXDFWeight);
											
											radianceLightR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
											radianceLightG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
											radianceLightB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										}
									}
								}
							}
						}
						
						final BXDFResult selectedBXDFResult = selectedBXDF.sampleSolidAngle(directionO, surfaceNormal, orthonormalBasis, random(), random());
						
						final float probabilityDensityFunctionValueA2 = selectedBXDFResult.getProbabilityDensityFunctionValue();
						final float reflectance = selectedBXDFResult.getReflectance();
						
						if(probabilityDensityFunctionValueA2 > 0.0F && reflectance > 0.0F) {
							final Vector3F selectedDirectionI = selectedBXDFResult.getI();
							final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
							
							final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
							
							final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
							
							if(optionalIntersection.isPresent()) {
								final Intersection intersection = optionalIntersection.get();
								
								if(primitive == intersection.getPrimitive()) {
									final Material material = primitive.getMaterial();
									
									if(material instanceof RayitoMaterial) {
										final RayitoMaterial rayitoMaterial = RayitoMaterial.class.cast(material);
										
										final float probabilityDensityFunctionValueB2 = primitive.calculateProbabilityDensityFunctionValueForSolidAngle(ray, intersection);
										
										if(probabilityDensityFunctionValueB2 > 0.0F) {
											final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA2, probabilityDensityFunctionValueB2, 1, 1);
											
											final Color3F emittance = rayitoMaterial.emittance(intersection);
											
											final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
											final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA2 * selectedBXDFWeight);
											
											radianceLightR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
											radianceLightG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
											radianceLightB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
										}
									}
								}
							}
						}
					}
				}
				
				final float samplesReciprocal = 1.0F / samples;
				
				radianceR += throughputR * (radianceLightR * samplesReciprocal);
				radianceG += throughputG * (radianceLightG * samplesReciprocal);
				radianceB += throughputB * (radianceLightB * samplesReciprocal);
			}
		}
		
		return new Color3F(radianceR, radianceG, radianceB);
	}
	
	private static Color3F doLightEstimateDirect(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
		if(light.isDeltaDistribution()) {
			return doLightEstimateDirectDeltaDistributionTrue(bSDF, intersection, light, sampleA, scene, isSpecular);
		}
		
		return doLightEstimateDirectDeltaDistributionFalse(bSDF, intersection, light, sampleA, sampleB, scene, isSpecular);
	}
	
	private static Color3F doLightEstimateDirectDeltaDistributionFalse(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
//		TODO: Verify!
//		BxDFType bsdfFlags = specular ? BSDF_ALL : BxDFType(BSDF_ALL & ~BSDF_SPECULAR);
//		
//		Spectrum Ld(0.f);
//		
//		Vector3f wi;
//		
//		Float lightPdf = 0, scatteringPdf = 0;
//		
//		VisibilityTester visibility;
//		
//		Spectrum Li = light.Sample_Li(it, uLight, &wi, &lightPdf, &visibility);
//		
//		if (lightPdf > 0 && !Li.IsBlack()) {
//			Spectrum f;
//			
//			const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
//			
//			f = isect.bsdf->f(isect.wo, wi, bsdfFlags) * AbsDot(wi, isect.shading.n);
//			
//			scatteringPdf = isect.bsdf->Pdf(isect.wo, wi, bsdfFlags);
//			
//			if (!f.IsBlack() && visibility.Unoccluded(scene)) {
//				Float weight = PowerHeuristic(1, lightPdf, 1, scatteringPdf);
//				
//				Ld += f * Li * weight / lightPdf;
//			}
//		}
//		
//		BxDFType sampledType;
//		
//		const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
//		
//		Spectrum f = isect.bsdf->Sample_f(isect.wo, &wi, uScattering, &scatteringPdf, bsdfFlags, &sampledType) * AbsDot(wi, isect.shading.n);
//		
//		bool sampledSpecular = (sampledType & BSDF_SPECULAR) != 0;
//		
//		if (!f.IsBlack() && scatteringPdf > 0) {
//			Float weight = 1;
//			
//			if (!sampledSpecular) {
//				lightPdf = light.Pdf_Li(it, wi);
//				
//				if (lightPdf == 0) {
//					return Ld;
//				}
//				
//				weight = PowerHeuristic(1, scatteringPdf, 1, lightPdf);
//			}
//			
//			SurfaceInteraction lightIsect;
//			
//			Ray ray = it.SpawnRay(wi);
//			
//			Spectrum Tr(1.f);
//			
//			bool foundSurfaceInteraction = handleMedia ? scene.IntersectTr(ray, sampler, &lightIsect, &Tr) : scene.Intersect(ray, &lightIsect);
//			
//			Spectrum Li(0.f);
//			
//			if (foundSurfaceInteraction) {
//				if (lightIsect.primitive->GetAreaLight() == &light) {
//					Li = lightIsect.Le(-wi);
//				}
//			} else {
//				Li = light.Le(ray);
//			}
//			
//			if (!Li.IsBlack()) {
//				Ld += f * Li * Tr * weight / scatteringPdf;
//			}
//		}
//		
//		return Ld;
		
		Color3F lightDirect = Color3F.BLACK;
		
		if(!light.isDeltaDistribution()) {
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
			final Optional<LightRadianceIncomingResult> optionalLightRadianceIncomingResult = light.sampleRadianceIncoming(intersection, sampleA);
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F normal = surfaceIntersection.getOrthonormalBasisS().getW();
			final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
			
			if(optionalLightRadianceIncomingResult.isPresent()) {
				final LightRadianceIncomingResult lightRadianceIncomingResult = optionalLightRadianceIncomingResult.get();
				
				final Color3F lightIncoming = lightRadianceIncomingResult.getResult();
				
				final Vector3F incoming = lightRadianceIncomingResult.getIncoming();
				
				final float lightProbabilityDensityFunctionValue = lightRadianceIncomingResult.getProbabilityDensityFunctionValue();
				
				if(!lightIncoming.isBlack() && lightProbabilityDensityFunctionValue > 0.0F) {
					final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
					final float incomingDotNormalAbs = abs(incomingDotNormal);
					
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), incomingDotNormalAbs);
					
					final float scatteringProbabilityDensityFunctionValue = bSDF.evaluateProbabilityDensityFunction(bXDFType, outgoing, incoming);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						final float weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightProbabilityDensityFunctionValue, scatteringProbabilityDensityFunctionValue, 1, 1);
						
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming, weight), lightProbabilityDensityFunctionValue));
					}
				}
			}
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(bXDFType, outgoing, sampleB);
			
			if(optionalBSDFResult.isPresent()) {
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Vector3F incoming = bSDFResult.getIncoming();
				
				final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
				final float incomingDotNormalAbs = abs(incomingDotNormal);
				
				final Color3F scatteringResult = Color3F.multiply(bSDFResult.getResult(), incomingDotNormalAbs);
				
				final boolean hasSampledSpecular = bSDFResult.getBXDFType().isSpecular();
				
				final float scatteringProbabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(!scatteringResult.isBlack() && scatteringProbabilityDensityFunctionValue > 0.0F) {
					Color3F weight = Color3F.WHITE;
					
					if(!hasSampledSpecular) {
						final float lightProbabilityDensityFunctionValue = light.evaluateProbabilityDensityFunctionRadianceIncoming(intersection, incoming);
						
						if(isZero(lightProbabilityDensityFunctionValue)) {
							return lightDirect;
						}
						
						weight = new Color3F(SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(scatteringProbabilityDensityFunctionValue, lightProbabilityDensityFunctionValue, 1, 1));
					}
					
					final Ray3F ray = surfaceIntersection.createRay(incoming);
					
					final Color3F transmittance = Color3F.WHITE;
					
					final Optional<Intersection> optionalIntersectionLight = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
					
					if(optionalIntersectionLight.isPresent()) {
						final Intersection intersectionLight = optionalIntersectionLight.get();
						
						if(intersectionLight.getPrimitive().getAreaLight().isPresent() && intersectionLight.getPrimitive().getAreaLight().get() == light) {
							final Color3F lightIncoming = intersectionLight.evaluateRadianceEmitted(Vector3F.negate(incoming));
							
							if(!lightIncoming.isBlack()) {
								lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming, transmittance, weight), scatteringProbabilityDensityFunctionValue));
							}
						}
					} else {
						final Color3F lightIncoming = light.evaluateRadianceEmitted(ray);
						
						if(!lightIncoming.isBlack()) {
							lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming, transmittance, weight), scatteringProbabilityDensityFunctionValue));
						}
					}
				}
			}
		}
		
		return lightDirect;
	}
	
	private static Color3F doLightEstimateDirectDeltaDistributionTrue(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Scene scene, final boolean isSpecular) {
		Color3F lightDirect = Color3F.BLACK;
		
		if(light.isDeltaDistribution()) {
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
			final Optional<LightRadianceIncomingResult> optionalLightRadianceIncomingResult = light.sampleRadianceIncoming(intersection, sampleA);
			
			if(optionalLightRadianceIncomingResult.isPresent()) {
				final LightRadianceIncomingResult lightRadianceIncomingResult = optionalLightRadianceIncomingResult.get();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final Color3F lightIncoming = lightRadianceIncomingResult.getResult();
				
				final Vector3F incoming = lightRadianceIncomingResult.getIncoming();
				final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
				
				final float lightProbabilityDensityFunctionValue = lightRadianceIncomingResult.getProbabilityDensityFunctionValue();
				
				if(!lightIncoming.isBlack() && lightProbabilityDensityFunctionValue > 0.0F) {
					final Vector3F normal = surfaceIntersection.getOrthonormalBasisS().getW();
					
					final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
					final float incomingDotNormalAbs = abs(incomingDotNormal);
					
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), incomingDotNormalAbs);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming), lightProbabilityDensityFunctionValue));
					}
				}
			}
		}
		
		return lightDirect;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doLightSampleAllUniformDistribution(final BSDF bSDF, final Intersection intersection, final Sampler sampler, final Scene scene) {
		return new Color3F(0.25F);
	}
	
	private static Color3F doLightSampleOneUniformDistribution(final BSDF bSDF, final Intersection intersection, final Sampler sampler, final Scene scene) {
		final int lightCount = scene.getLightCount();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final int lightIndex = min((int)(random() * lightCount), lightCount - 1);
		
		final float lightProbabilityDensityFunctionValue = 1.0F / lightCount;
		
		final Light light = scene.getLight(lightIndex);
		
		final Sample2F sampleA = sampler.sample2();
		final Sample2F sampleB = sampler.sample2();
		
		return Color3F.divide(doLightEstimateDirect(bSDF, intersection, light, new Point2F(sampleA.getU(), sampleA.getV()), new Point2F(sampleB.getU(), sampleB.getV()), scene, false), lightProbabilityDensityFunctionValue);
	}
	
	private static boolean doIsLightVisible(final Light light, final LightRadianceIncomingResult lightIncomingRadianceResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection) {
		final Point3F point = lightIncomingRadianceResult.getPoint();
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Ray3F ray = surfaceIntersection.createRay(point);
		
		final float tMinimum = T_MINIMUM;
		final float tMaximum = abs(Point3F.distance(surfaceIntersectionPoint, point)) + T_MINIMUM;
		
		if(light instanceof AreaLight) {
			final Optional<Intersection> optionalIntersection = scene.intersection(ray, tMinimum, tMaximum);
			
			if(optionalIntersection.isPresent()) {
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Optional<AreaLight> optionalAreaLight = primitive.getAreaLight();
				
				if(optionalAreaLight.isPresent()) {
					final AreaLight areaLight = optionalAreaLight.get();
					
					return light == areaLight;
				}
				
				return false;
			}
			
			return true;
		}
		
		return !scene.intersects(ray, tMinimum, tMaximum);
	}
}