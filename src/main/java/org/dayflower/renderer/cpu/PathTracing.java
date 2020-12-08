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
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;
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
import org.dayflower.scene.BSDF;
import org.dayflower.scene.BSDFResult;
import org.dayflower.scene.BXDFType;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceIncomingResult;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.TransportMode;
import org.dayflower.scene.bxdf.pbrt.PBRTBSDF;
import org.dayflower.scene.bxdf.rayito.RayitoBSDF;
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
	
	public static Color3F radiance(final Ray3F ray, final RendererConfiguration rendererConfiguration) {
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
			
			if(currentBounce == 0 && !hasFoundIntersection && rendererConfiguration.isPreviewMode()) {
				return Color3F.WHITE;
			}
			
			if(currentBounce == 0 || isSpecularBounce) {
				if(hasFoundIntersection) {
					final Intersection intersection = optionalIntersection.get();
					
					final Material material = intersection.getPrimitive().getMaterial();
					
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, intersection.evaluateRadianceEmitted(Vector3F.negate(ray.getDirection()))));
					
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
			
			if(bSDF instanceof PBRTBSDF && PBRTBSDF.class.cast(bSDF).countBXDFsBySpecularType(false) > 0 || bSDF instanceof RayitoBSDF && RayitoBSDF.class.cast(bSDF).countBXDFsBySpecularType(false) > 0) {
				radiance = Color3F.add(radiance, doLightEstimateAllPrimitiveLights(throughput, bSDF, scene, surfaceIntersection, outgoing, lights, primitive));
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, doLightSampleOneUniformDistribution(bSDF, intersection, sampler, scene)));
			}
			
			final Sample2F sample = sampler.sample2();
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).sampleDistributionFunction(BXDFType.ALL, outgoing, new Point2F(sample.getU(), sample.getV())) : RayitoBSDF.class.cast(bSDF).sampleDistributionFunction(BXDFType.ALL, outgoing, surfaceNormalS, new Point2F(sample.getU(), sample.getV()));
			
			if(!optionalBSDFResult.isPresent()) {
				break;
			}
			
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final BXDFType bXDFType = bSDFResult.getBXDFType();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDF instanceof PBRTBSDF ? bSDFResult.getIncoming() : Vector3F.negate(bSDFResult.getIncoming());
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			if(result.isBlack() || isZero(probabilityDensityFunctionValue)) {
				break;
			}
			
			throughput = Color3F.multiply(throughput, Color3F.divide(Color3F.multiply(result, abs(Vector3F.dotProduct(incoming, surfaceNormalS))), probabilityDensityFunctionValue));
			
			isSpecularBounce = bXDFType.isSpecular();
			
			if(bXDFType.hasTransmission() && bXDFType.isSpecular()) {
				final float eta = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).getEta() : RayitoBSDF.class.cast(bSDF).getEta();
				
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
				
				for(int sample = 0; sample < samples; sample++) {
					final Optional<SurfaceSample3F> optionalSurfaceSample = primitive.sample(surfaceIntersectionPoint, surfaceNormal, random(), random());
					
					if(optionalSurfaceSample.isPresent()) {
						final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
						
						final Point3F point = surfaceSample.getPoint();
						
						final float probabilityDensityFunctionValueA1 = surfaceSample.getProbabilityDensityFunctionValue();
						
						if(probabilityDensityFunctionValueA1 > 0.0F) {
							final Vector3F selectedDirectionI = Vector3F.normalize(Vector3F.direction(point, surfaceIntersectionPoint));
							final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
							
							final float probabilityDensityFunctionValueB1 = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).evaluateProbabilityDensityFunction(BXDFType.ALL, directionO, selectedDirectionO) : RayitoBSDF.class.cast(bSDF).evaluateProbabilityDensityFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionI);
							
							final Color3F result = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).evaluateDistributionFunction(BXDFType.ALL, directionO, selectedDirectionO) : RayitoBSDF.class.cast(bSDF).evaluateDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, selectedDirectionI);
							
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
						
						final Optional<BSDFResult> optionalBSDFResult = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).sampleDistributionFunction(BXDFType.ALL, directionO, new Point2F(random(), random())) : RayitoBSDF.class.cast(bSDF).sampleDistributionFunction(BXDFType.ALL, directionO, surfaceNormal, new Point2F(random(), random()));
						
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
					
					final Color3F scatteringResult = Color3F.multiply(bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).evaluateDistributionFunction(bXDFType, outgoing, incoming) : RayitoBSDF.class.cast(bSDF).evaluateDistributionFunction(bXDFType, outgoing, normal, Vector3F.negate(incoming)), incomingDotNormalAbs);
					
					final float scatteringProbabilityDensityFunctionValue = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).evaluateProbabilityDensityFunction(bXDFType, outgoing, incoming) : RayitoBSDF.class.cast(bSDF).evaluateProbabilityDensityFunction(bXDFType, outgoing, normal, Vector3F.negate(incoming));
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						final float weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightProbabilityDensityFunctionValue, scatteringProbabilityDensityFunctionValue, 1, 1);
						
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming, weight), lightProbabilityDensityFunctionValue));
					}
				}
			}
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).sampleDistributionFunction(bXDFType, outgoing, sampleB) : RayitoBSDF.class.cast(bSDF).sampleDistributionFunction(bXDFType, outgoing, normal, sampleB);
			
			if(optionalBSDFResult.isPresent()) {
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Vector3F incoming = bSDF instanceof PBRTBSDF ? bSDFResult.getIncoming() : bSDFResult.getIncoming();//Vector3F.negate(bSDFResult.getIncoming());
				
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
							final Color3F lightIncoming = intersectionLight.evaluateRadianceEmitted(incoming);
							
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
					
					final Color3F scatteringResult = Color3F.multiply(bSDF instanceof PBRTBSDF ? PBRTBSDF.class.cast(bSDF).evaluateDistributionFunction(bXDFType, outgoing, incoming) : RayitoBSDF.class.cast(bSDF).evaluateDistributionFunction(bXDFType, outgoing, normal, Vector3F.negate(incoming)), incomingDotNormalAbs);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming), lightProbabilityDensityFunctionValue));
					}
				}
			}
		}
		
		return lightDirect;
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