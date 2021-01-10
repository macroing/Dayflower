/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.AreaLight;
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
import org.dayflower.scene.material.pbrt.PBRTMaterial;

final class PathTracingPBRT {
	private static final float T_MAXIMUM = Float.MAX_VALUE;
	private static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PathTracingPBRT() {
		
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
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, optionalIntersection.get().evaluateRadianceEmitted(Vector3F.negate(currentRay.getDirection()))));
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
			
			final Optional<PBRTBSDF> optionalPBRTBSDF = pBRTMaterial.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalPBRTBSDF.isPresent()) {
				currentRay = surfaceIntersection.createRay(currentRay.getDirection());
				
				currentBounce--;
				
				continue;
			}
			
			final PBRTBSDF pBRTBSDF = optionalPBRTBSDF.get();
			
			if(pBRTBSDF.countBXDFsBySpecularType(false) > 0) {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, doLightSampleOneUniformDistribution(pBRTBSDF, intersection, sampler, scene)));
			}
			
			final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
			
			final Sample2F sample = sampler.sample2();
			
			final Optional<BSDFResult> optionalBSDFResult = pBRTBSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, surfaceNormalS, new Point2F(sample.getU(), sample.getV()));
			
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
				final float eta = pBRTBSDF.getEta();
				
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
	
	private static Color3F doLightEstimateDirect(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
		if(light.isDeltaDistribution()) {
			return doLightEstimateDirectDeltaDistributionTrue(pBRTBSDF, intersection, light, sampleA, scene, isSpecular);
		}
		
		return doLightEstimateDirectDeltaDistributionFalse(pBRTBSDF, intersection, light, sampleA, sampleB, scene, isSpecular);
	}
	
	private static Color3F doLightEstimateDirectDeltaDistributionFalse(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
//		Spectrum Ld(0.f);
		Color3F lightDirect = Color3F.BLACK;
		
		if(!light.isDeltaDistribution()) {
//			BxDFType bsdfFlags = specular ? BSDF_ALL : BxDFType(BSDF_ALL & ~BSDF_SPECULAR);
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
//			Spectrum Li = light.Sample_Li(it, uLight, &wi, &lightPdf, &visibility);
			final Optional<LightRadianceIncomingResult> optionalLightRadianceIncomingResult = light.sampleRadianceIncoming(intersection, sampleA);
			
//			const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F normal = surfaceIntersection.getOrthonormalBasisS().getW();
			final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
			
			if(optionalLightRadianceIncomingResult.isPresent()) {
				final LightRadianceIncomingResult lightRadianceIncomingResult = optionalLightRadianceIncomingResult.get();
				
				final Color3F lightIncoming = lightRadianceIncomingResult.getResult();
				
				final Vector3F incoming = lightRadianceIncomingResult.getIncoming();
				
				final float lightPDFValue = lightRadianceIncomingResult.getProbabilityDensityFunctionValue();
				
//				if (lightPdf > 0 && !Li.IsBlack()) {
				if(!lightIncoming.isBlack() && lightPDFValue > 0.0F) {
//					f = isect.bsdf->f(isect.wo, wi, bsdfFlags) * AbsDot(wi, isect.shading.n);
					final Color3F scatteringResult = Color3F.multiply(pBRTBSDF.evaluateDistributionFunction(bXDFType, outgoing, normal, incoming), abs(Vector3F.dotProduct(incoming, normal)));
					
//					scatteringPdf = isect.bsdf->Pdf(isect.wo, wi, bsdfFlags);
					final float scatteringPDFValue = pBRTBSDF.evaluateProbabilityDensityFunction(bXDFType, outgoing, normal, incoming);
					
//					if (!f.IsBlack() && visibility.Unoccluded(scene)) {
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
//						Float weight = PowerHeuristic(1, lightPdf, 1, scatteringPdf);
						final float weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightPDFValue, scatteringPDFValue, 1, 1);
						
//						Ld += f * Li * weight / lightPdf;
						lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, weight, lightPDFValue);
//					}
					}
//				}
				}
			}
			
//			Spectrum f = isect.bsdf->Sample_f(isect.wo, &wi, uScattering, &scatteringPdf, bsdfFlags, &sampledType) * AbsDot(wi, isect.shading.n);
			final Optional<BSDFResult> optionalBSDFResult = pBRTBSDF.sampleDistributionFunction(bXDFType, outgoing, normal, sampleB);
			
			if(optionalBSDFResult.isPresent()) {
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Vector3F incoming = bSDFResult.getIncoming();
				
				final Color3F scatteringResult = Color3F.multiply(bSDFResult.getResult(), abs(Vector3F.dotProduct(incoming, normal)));
				
//				bool sampledSpecular = (sampledType & BSDF_SPECULAR) != 0;
				final boolean hasSampledSpecular = bSDFResult.getBXDFType().isSpecular();
				
				final float scatteringPDFValue = bSDFResult.getProbabilityDensityFunctionValue();
				
//				if (!f.IsBlack() && scatteringPdf > 0) {
				if(!scatteringResult.isBlack() && scatteringPDFValue > 0.0F) {
//					Float weight = 1;
					float weight = 1.0F;
					
//					if (!sampledSpecular) {
					if(!hasSampledSpecular) {
//						lightPdf = light.Pdf_Li(it, wi);
						final float lightPDFValue = light.evaluateProbabilityDensityFunctionRadianceIncoming(intersection, incoming);
						
//						if (lightPdf == 0) {
						if(isZero(lightPDFValue)) {
//							return Ld;
							return lightDirect;
//						}
						}
						
//						weight = PowerHeuristic(1, scatteringPdf, 1, lightPdf);
						weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(scatteringPDFValue, lightPDFValue, 1, 1);
//					}
					}
					
//					Ray ray = it.SpawnRay(wi);
					final Ray3F ray = surfaceIntersection.createRay(incoming);
					
//					Spectrum Tr(1.f);
					final Color3F transmittance = Color3F.WHITE;
					
//					bool foundSurfaceInteraction = handleMedia ? scene.IntersectTr(ray, sampler, &lightIsect, &Tr) : scene.Intersect(ray, &lightIsect);
					final Optional<Intersection> optionalIntersectionLight = scene.intersection(ray, T_MINIMUM, T_MAXIMUM);
					
//					if (foundSurfaceInteraction) {
					if(optionalIntersectionLight.isPresent()) {
						final Intersection intersectionLight = optionalIntersectionLight.get();
						
//						if (lightIsect.primitive->GetAreaLight() == &light) {
						if(intersectionLight.getPrimitive().getAreaLight().orElse(null) == light) {
//							Li = lightIsect.Le(-wi);
							final Color3F lightIncoming = intersectionLight.evaluateRadianceEmitted(Vector3F.negate(incoming));
							
//							if (!Li.IsBlack()) {
							if(!lightIncoming.isBlack()) {
//								Ld += f * Li * Tr * weight / scatteringPdf;
								lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, transmittance, weight, scatteringPDFValue);
//							}
							}
//						}
						}
//					} else {
					} else {
//						Li = light.Le(ray);
						final Color3F lightIncoming = light.evaluateRadianceEmitted(ray);
						
//						if (!Li.IsBlack()) {
						if(!lightIncoming.isBlack()) {
//							Ld += f * Li * Tr * weight / scatteringPdf;
							lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, transmittance, weight, scatteringPDFValue);
//						}
						}
//					}
					}
//				}
				}
			}
		}
		
//		return Ld;
		return lightDirect;
	}
	
	private static Color3F doLightEstimateDirectDeltaDistributionTrue(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Scene scene, final boolean isSpecular) {
//		Spectrum Ld(0.f);
		Color3F lightDirect = Color3F.BLACK;
		
		if(light.isDeltaDistribution()) {
//			BxDFType bsdfFlags = specular ? BSDF_ALL : BxDFType(BSDF_ALL & ~BSDF_SPECULAR);
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
//			Spectrum Li = light.Sample_Li(it, uLight, &wi, &lightPdf, &visibility);
			final Optional<LightRadianceIncomingResult> optionalLightRadianceIncomingResult = light.sampleRadianceIncoming(intersection, sampleA);
			
//			const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F normal = surfaceIntersection.getOrthonormalBasisS().getW();
			final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
			
			if(optionalLightRadianceIncomingResult.isPresent()) {
				final LightRadianceIncomingResult lightRadianceIncomingResult = optionalLightRadianceIncomingResult.get();
				
				final Color3F lightIncoming = lightRadianceIncomingResult.getResult();
				
				final Vector3F incoming = lightRadianceIncomingResult.getIncoming();
				
				final float lightPDFValue = lightRadianceIncomingResult.getProbabilityDensityFunctionValue();
				
//				if (lightPdf > 0 && !Li.IsBlack()) {
				if(!lightIncoming.isBlack() && lightPDFValue > 0.0F) {
//					f = isect.bsdf->f(isect.wo, wi, bsdfFlags) * AbsDot(wi, isect.shading.n);
					final Color3F scatteringResult = Color3F.multiply(pBRTBSDF.evaluateDistributionFunction(bXDFType, outgoing, normal, incoming), abs(Vector3F.dotProduct(incoming, normal)));
					
//					if (!f.IsBlack() && visibility.Unoccluded(scene)) {
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
//						Ld += f * Li / lightPdf;
						lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, lightPDFValue);
//					}
					}
//				}
				}
			}
		}
		
//		return Ld;
		return lightDirect;
	}
	
	private static Color3F doLightSampleOneUniformDistribution(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Sampler sampler, final Scene scene) {
		final int lightCount = scene.getLightCount();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final int lightIndex = min((int)(random() * lightCount), lightCount - 1);
		
		final float lightPDFValue = 1.0F / lightCount;
		
		final Light light = scene.getLight(lightIndex);
		
		final Sample2F sampleA = sampler.sample2();
		final Sample2F sampleB = sampler.sample2();
		
		return Color3F.divide(doLightEstimateDirect(pBRTBSDF, intersection, light, new Point2F(sampleA.getU(), sampleA.getV()), new Point2F(sampleB.getU(), sampleB.getV()), scene, false), lightPDFValue);
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