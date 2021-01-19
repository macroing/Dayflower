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

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;

import java.util.List;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
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
import org.dayflower.scene.Sample;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.pbrt.PBRTMaterial;
import org.dayflower.scene.material.rayito.RayitoMaterial;

final class PathTracing {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracing() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Sampler sampler, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int minimumBounceRussianRoulette) {
		final List<Light> lights = scene.getLights();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		boolean isSpecularBounce = false;
		
		float etaScale = 1.0F;
		
		for(int currentBounce = 0; true; currentBounce++) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			final boolean hasFoundIntersection = optionalIntersection.isPresent();
			
			if(currentBounce == 0 && !hasFoundIntersection && isPreviewMode) {
				return Color3F.WHITE;
			}
			
			if(currentBounce == 0 || isSpecularBounce) {
				if(hasFoundIntersection) {
					final Intersection intersection = optionalIntersection.get();
					
					final Material material = intersection.getPrimitive().getMaterial();
					
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, intersection.evaluateRadianceEmitted(Vector3F.negate(currentRay.getDirection()))));
					
					if(material instanceof RayitoMaterial) {
						radiance = Color3F.add(radiance, Color3F.multiply(throughput, RayitoMaterial.class.cast(material).emittance(intersection)));
					}
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
			
			if(!(material instanceof PBRTMaterial || material instanceof RayitoMaterial)) {
				break;
			}
			
			final Optional<? extends BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalBSDF.isPresent()) {
				currentRay = surfaceIntersection.createRay(currentRay.getDirection());
				
				currentBounce--;
				
				continue;
			}
			
			final BSDF bSDF = optionalBSDF.get();
			
			final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
			
			if(bSDF.countBXDFsBySpecularType(false) > 0) {
				radiance = Color3F.add(radiance, doLightEstimateAllPrimitiveLights(throughput, bSDF, scene, surfaceIntersection, outgoing, lights, primitive));
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.sampleOneLightUniformDistribution(bSDF, intersection)));
			}
			
			final Sample2F sample = sampler.sample2();
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, surfaceNormalS, new Point2F(sample.getU(), sample.getV()));
			
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
			/*
			currentRay = surfaceIntersection.createRay(incoming);
			
			if(currentBounce >= minimumBounceRussianRoulette) {
				final float probability = throughput.maximum();
				
				if(random() > probability) {
					break;
				}
				
				throughput = Color3F.divide(throughput, probability);
			}
			*/
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doLightEstimateAllPrimitiveLights(final Color3F throughput, final BSDF bSDF, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO, final List<Light> lights, final Primitive primitiveToSkip) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		final float throughputR = throughput.getR();
		final float throughputG = throughput.getG();
		final float throughputB = throughput.getB();
		
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
				
				for(int currentSample = 0; currentSample < samples; currentSample++) {
					final Optional<Sample> optionalSample = primitive.sample(surfaceIntersectionPoint, surfaceNormal, random(), random());
					
					if(optionalSample.isPresent()) {
						final Sample sample = optionalSample.get();
						
						final Point3F point = sample.getSurfaceSampleWorldSpace().getPoint();
						
						final float probabilityDensityFunctionValueA1 = sample.getSurfaceSampleWorldSpace().getProbabilityDensityFunctionValue();
						
						if(probabilityDensityFunctionValueA1 > 0.0F) {
							final Vector3F selectedDirectionI = Vector3F.normalize(Vector3F.direction(point, surfaceIntersectionPoint));
							final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
							
							final float probabilityDensityFunctionValueB1 = bSDF.evaluateProbabilityDensityFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionO);
							
							final Color3F result = bSDF.evaluateDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionO);
							
							if(probabilityDensityFunctionValueB1 > 0.0F && !result.isBlack()) {
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
											final float probabilityDensityFunctionValueReciprocal = 1.0F / probabilityDensityFunctionValueA1;
											
											radianceLightR += emittance.getR() * result.getR() * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
											radianceLightG += emittance.getG() * result.getG() * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
											radianceLightB += emittance.getB() * result.getB() * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										}
									}
								}
							}
						}
						
						final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, new Point2F(random(), random()));
						
						if(optionalBSDFResult.isPresent()) {
							final BSDFResult bSDFResult = optionalBSDFResult.get();
							
							final Color3F result = bSDFResult.getResult();
							
							final float probabilityDensityFunctionValueA2 = bSDFResult.getProbabilityDensityFunctionValue();
							
							if(probabilityDensityFunctionValueA2 > 0.0F && !result.isBlack()) {
								final Vector3F selectedDirectionI = bSDFResult.getIncoming();
								final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
								
								final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
								
								final Optional<Intersection> optionalIntersection = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
								
								if(optionalIntersection.isPresent()) {
									final Intersection intersection = optionalIntersection.get();
									
									if(primitive == intersection.getPrimitive()) {
										final Material material = primitive.getMaterial();
										
										if(material instanceof RayitoMaterial) {
											final RayitoMaterial rayitoMaterial = RayitoMaterial.class.cast(material);
											
											final float probabilityDensityFunctionValueB2 = primitive.evaluateProbabilityDensityFunction(ray, intersection);
											
											if(probabilityDensityFunctionValueB2 > 0.0F) {
												final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA2, probabilityDensityFunctionValueB2, 1, 1);
												
												final Color3F emittance = rayitoMaterial.emittance(intersection);
												
												final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
												final float probabilityDensityFunctionValueReciprocal = 1.0F / probabilityDensityFunctionValueA2;
												
												radianceLightR += emittance.getR() * result.getR() * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
												radianceLightG += emittance.getG() * result.getG() * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
												radianceLightB += emittance.getB() * result.getB() * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
											}
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
}