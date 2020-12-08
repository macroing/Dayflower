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
import static org.dayflower.util.Floats.random;

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
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.rayito.RayitoMaterial;

final class PathTracingRayito {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingRayito() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Color3F radiance(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
		final Scene scene = rendererConfiguration.getScene();
		
		final List<Light> lights = scene.getLights();
		
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceSpecular = 0;
		
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
				
				final Optional<RayitoBSDF> optionalRayitoBSDF = rayitoMaterial.computeBSDF(intersection, TransportMode.RADIANCE, true);
				
				if(!optionalRayitoBSDF.isPresent()) {
					break;
				}
				
				final RayitoBSDF rayitoBSDF = optionalRayitoBSDF.get();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final OrthonormalBasis33F orthonormalBasisS = surfaceIntersection.getOrthonormalBasisS();
				
				final Vector3F surfaceNormalS = orthonormalBasisS.getW();
				
				if(currentBounce == 0 || currentBounce == currentBounceSpecular) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, intersection.evaluateRadianceEmitted(Vector3F.negate(ray.getDirection()))));
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, rayitoMaterial.emittance(intersection)));
				}
				
				if(rayitoBSDF.countBXDFsBySpecularType(true) > 0) {
					currentBounceSpecular++;
				} else {
					radiance = Color3F.add(radiance, doLightEstimateAllPrimitiveLights(throughput, rayitoBSDF, scene, surfaceIntersection, currentRayDirectionO, lights, primitive));
				}
				
				final Optional<BSDFResult> optionalBSDFResult = rayitoBSDF.sampleDistributionFunction(BXDFType.ALL, currentRayDirectionO, surfaceNormalS, new Point2F(random(), random()));
				
				if(!optionalBSDFResult.isPresent()) {
					break;
				}
				
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Color3F result = bSDFResult.getResult();
				
				final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(probabilityDensityFunctionValue > 0.0F) {
					currentRay = surfaceIntersection.createRay(Vector3F.negate(Vector3F.normalize(bSDFResult.getIncoming())));
					
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
			} else if(currentBounce == 0 && rendererConfiguration.isPreviewMode()) {
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
	
	private static Color3F doLightEstimateAllPrimitiveLights(final Color3F throughput, final RayitoBSDF rayitoBSDF, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO, final List<Light> lights, final Primitive primitiveToSkip) {
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
				
				for(int sample = 0; sample < samples; sample++) {
					final Optional<SurfaceSample3F> optionalSurfaceSample = primitive.sample(surfaceIntersectionPoint, surfaceNormal, random(), random());
					
					if(optionalSurfaceSample.isPresent()) {
						final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
						
						final Point3F point = surfaceSample.getPoint();
						
						final float probabilityDensityFunctionValueA1 = surfaceSample.getProbabilityDensityFunctionValue();
						
						if(probabilityDensityFunctionValueA1 > 0.0F) {
							final Vector3F selectedDirectionI = Vector3F.normalize(Vector3F.direction(point, surfaceIntersectionPoint));
							final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
							
							final float probabilityDensityFunctionValueB1 = rayitoBSDF.evaluateProbabilityDensityFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionI);
//							final float probabilityDensityFunctionValueB1 = rayitoBSDF.evaluateProbabilityDensityFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionO);
							
							final Color3F result = rayitoBSDF.evaluateDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionI);
//							final Color3F result = rayitoBSDF.evaluateDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionO);
							
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
						
						final Optional<BSDFResult> optionalBSDFResult = rayitoBSDF.sampleDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, new Point2F(random(), random()));
						
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
											
											final float probabilityDensityFunctionValueB2 = primitive.calculateProbabilityDensityFunctionValueForSolidAngle(ray, intersection);
											
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