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
import static org.dayflower.utility.Floats.random;

import java.util.List;
import java.util.Optional;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Vector3F;
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

final class PathTracingRayito {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingRayito() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final Scene scene, final boolean isPreviewMode, final int maximumBounce, final int minimumBounceRussianRoulette) {
		final List<Light> lights = scene.getLights();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceSpecular = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay, T_MINIMUM, T_MAXIMUM);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
				
				if(!optionalBSDF.isPresent()) {
					break;
				}
				
				final BSDF bSDF = optionalBSDF.get();
				
				final Vector3F surfaceNormalS = intersection.getSurfaceNormalS();
				
				if(currentBounce == 0 || currentBounce == currentBounceSpecular) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, material.emittance(intersection)));
				}
				
				if(bSDF.countBXDFsBySpecularType(true) > 0) {
					currentBounceSpecular++;
				} else {
					radiance = Color3F.add(radiance, doLightEstimateAllPrimitiveLights(bSDF, throughput, intersection, scene, outgoing, lights, primitive));
				}
				
				final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, surfaceNormalS, new Point2F(random(), random()));
				
				if(!optionalBSDFResult.isPresent()) {
					break;
				}
				
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Color3F result = bSDFResult.getResult();
				
				final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(probabilityDensityFunctionValue > 0.0F) {
					currentRay = intersection.createRay(Vector3F.normalize(bSDFResult.getIncoming()));
					
					throughput = Color3F.multiply(throughput, result);
					throughput = Color3F.multiply(throughput, abs(Vector3F.dotProduct(currentRay.getDirection(), surfaceNormalS)) / probabilityDensityFunctionValue);
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
			} else if(currentBounce == 0 && isPreviewMode) {
				return Color3F.WHITE;
			} else {
				for(final Light light : lights) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
				}
				
				break;
			}
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doLightEstimateAllPrimitiveLights(final BSDF bSDF, final Color3F throughput, final Intersection intersection, final Scene scene, final Vector3F outgoing, final List<Light> lights, final Primitive primitiveToSkip) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		final float throughputR = throughput.getR();
		final float throughputG = throughput.getG();
		final float throughputB = throughput.getB();
		
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormal = intersection.getSurfaceNormalS();
		
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
							final Vector3F incoming = Vector3F.directionNormalized(surfaceIntersectionPoint, point);
							
							final float probabilityDensityFunctionValueB1 = bSDF.evaluateProbabilityDensityFunction(BXDFType.ALL, outgoing, surfaceNormal, incoming);
							
							final Color3F result = bSDF.evaluateDistributionFunction(BXDFType.ALL, outgoing, surfaceNormal, incoming);
							
							if(probabilityDensityFunctionValueB1 > 0.0F && !result.isBlack()) {
								final Ray3F ray = intersection.createRay(incoming);
								
								final Optional<Intersection> optionalIntersectionLight = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
								
								if(optionalIntersectionLight.isPresent()) {
									final Intersection intersectionLight = optionalIntersectionLight.get();
									
									if(primitive == intersectionLight.getPrimitive()) {
										final Material material = primitive.getMaterial();
										
										final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA1, probabilityDensityFunctionValueB1, 1, 1);
										
										final Color3F emittance = material.emittance(intersectionLight);
										
										final float incomingDotSurfaceNormalAbs = abs(Vector3F.dotProduct(incoming, surfaceNormal));
										final float probabilityDensityFunctionValueReciprocal = 1.0F / probabilityDensityFunctionValueA1;
										
										radianceLightR += emittance.getR() * result.getR() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										radianceLightG += emittance.getG() * result.getG() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										radianceLightB += emittance.getB() * result.getB() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
									}
								}
							}
						}
						
						final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, surfaceNormal, new Point2F(random(), random()));
						
						if(optionalBSDFResult.isPresent()) {
							final BSDFResult bSDFResult = optionalBSDFResult.get();
							
							final Color3F result = bSDFResult.getResult();
							
							final float probabilityDensityFunctionValueA2 = bSDFResult.getProbabilityDensityFunctionValue();
							
							if(probabilityDensityFunctionValueA2 > 0.0F && !result.isBlack()) {
								final Vector3F incoming = bSDFResult.getIncoming();
								
								final Ray3F ray = intersection.createRay(incoming);
								
								final Optional<Intersection> optionalIntersectionLight = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
								
								if(optionalIntersectionLight.isPresent()) {
									final Intersection intersectionLight = optionalIntersectionLight.get();
									
									if(primitive == intersectionLight.getPrimitive()) {
										final Material material = primitive.getMaterial();
										
										final float probabilityDensityFunctionValueB2 = primitive.evaluateProbabilityDensityFunction(ray, intersectionLight);
										
										if(probabilityDensityFunctionValueB2 > 0.0F) {
											final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA2, probabilityDensityFunctionValueB2, 1, 1);
											
											final Color3F emittance = material.emittance(intersectionLight);
											
											final float incomingDotSurfaceNormalAbs = abs(Vector3F.dotProduct(incoming, surfaceNormal));
											final float probabilityDensityFunctionValueReciprocal = 1.0F / probabilityDensityFunctionValueA2;
											
											radianceLightR += emittance.getR() * result.getR() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
											radianceLightG += emittance.getG() * result.getG() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
											radianceLightB += emittance.getB() * result.getB() * incomingDotSurfaceNormalAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
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