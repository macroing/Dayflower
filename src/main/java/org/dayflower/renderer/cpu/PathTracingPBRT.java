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

import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.renderer.RendererConfiguration;
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
			
			final Optional<BSDFResult> optionalBSDFResult = pBRTBSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, new Point2F(sample.getU(), sample.getV()));
			
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
					
					final Color3F scatteringResult = Color3F.multiply(pBRTBSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), incomingDotNormalAbs);
					
					final float scatteringProbabilityDensityFunctionValue = pBRTBSDF.evaluateProbabilityDensityFunction(bXDFType, outgoing, incoming);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						final float weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightProbabilityDensityFunctionValue, scatteringProbabilityDensityFunctionValue, 1, 1);
						
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming, weight), lightProbabilityDensityFunctionValue));
					}
				}
			}
			
			final Optional<BSDFResult> optionalBSDFResult = pBRTBSDF.sampleDistributionFunction(bXDFType, outgoing, sampleB);
			
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
	
	private static Color3F doLightEstimateDirectDeltaDistributionTrue(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Scene scene, final boolean isSpecular) {
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
					
					final Color3F scatteringResult = Color3F.multiply(pBRTBSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), incomingDotNormalAbs);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming), lightProbabilityDensityFunctionValue));
					}
				}
			}
		}
		
		return lightDirect;
	}
	
	private static Color3F doLightSampleOneUniformDistribution(final PBRTBSDF pBRTBSDF, final Intersection intersection, final Sampler sampler, final Scene scene) {
		final int lightCount = scene.getLightCount();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final int lightIndex = min((int)(random() * lightCount), lightCount - 1);
		
		final float lightProbabilityDensityFunctionValue = 1.0F / lightCount;
		
		final Light light = scene.getLight(lightIndex);
		
		final Sample2F sampleA = sampler.sample2();
		final Sample2F sampleB = sampler.sample2();
		
		return Color3F.divide(doLightEstimateDirect(pBRTBSDF, intersection, light, new Point2F(sampleA.getU(), sampleA.getV()), new Point2F(sampleB.getU(), sampleB.getV()), scene, false), lightProbabilityDensityFunctionValue);
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