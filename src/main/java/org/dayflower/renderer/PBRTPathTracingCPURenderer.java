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
package org.dayflower.renderer;

import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Ints.min;

import java.util.List;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.AreaLight;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.LightRadianceIncomingResult;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.ConstantBackground;
import org.dayflower.scene.pbrt.BSDF;
import org.dayflower.scene.pbrt.BSDFDistributionFunctionResult;
import org.dayflower.scene.pbrt.BXDFType;
import org.dayflower.scene.pbrt.PBRTMaterial;
import org.dayflower.scene.pbrt.TransportMode;

/**
 * A {@code PBRTPathTracingCPURenderer} is a {@link Renderer} implementation that renders using Path Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PBRTPathTracingCPURenderer extends AbstractCPURenderer {
	/**
	 * Constructs a new {@code PBRTPathTracingCPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PBRTPathTracingCPURenderer(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	 * }
	 * </pre>
	 */
	public PBRTPathTracingCPURenderer() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	}
	
	/**
	 * Constructs a new {@code PBRTPathTracingCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code PBRTPathTracingCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code PBRTPathTracingCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code PBRTPathTracingCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code PBRTPathTracingCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}
	 */
	public PBRTPathTracingCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Scene scene) {
		super(display, image, rendererConfiguration, scene, true);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray}.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray a {@link Ray3F} instance
	 * @return a {@code Color3F} instance with the radiance along {@code ray}
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	@Override
	protected Color3F radiance(final Ray3F ray) {
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final Scene scene = getScene();
		
		final List<Light> lights = scene.getLights();
		
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		boolean isSpecularBounce = false;
		
		float etaScale = 1.0F;
		
		for(int currentBounce = 0; true; currentBounce++) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay);
			
			final boolean hasFoundIntersection = optionalIntersection.isPresent();
			
			if(currentBounce == 0 || isSpecularBounce) {
				if(hasFoundIntersection) {
//					radiance += throughput * isect.Le(-ray.d);
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, optionalIntersection.get().evaluateRadianceEmitted(Vector3F.negate(ray.getDirection()))));
				} else {
					for(final Light light : lights) {
//						radiance += throughput * light->Le(ray);
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
			
			final Vector3F surfaceNormalG = surfaceIntersection.getSurfaceNormalG();
			final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
			
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
//				radiance += throughput * UniformSampleOneLight(isect, scene, arena, sampler, false, distrib);
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, doLightSampleOneUniformDistribution(bSDF, intersection, scene)));
			}
			
			final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
			
			final Optional<BSDFDistributionFunctionResult> optionalBSDFDistributionFunctionResult = bSDF.sampleDistributionFunction(BXDFType.ALL, outgoing, new Point2F(random(), random()));
			
			if(!optionalBSDFDistributionFunctionResult.isPresent()) {
				break;
			}
			
			final BSDFDistributionFunctionResult bSDFDistributionFunctionResult = optionalBSDFDistributionFunctionResult.get();
			
			final BXDFType bXDFType = bSDFDistributionFunctionResult.getBXDFType();
			
			final Color3F result = bSDFDistributionFunctionResult.getResult();
			
			final Vector3F incoming = bSDFDistributionFunctionResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFDistributionFunctionResult.getProbabilityDensityFunctionValue();
			
			if(result.isBlack() || equal(probabilityDensityFunctionValue, 0.0F)) {
				break;
			}
			
//			throughput *= result * AbsDot(incoming, surfaceNormalS) / probabilityDensityFunctionValue;
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doLightEstimateDirect(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
		if(light.isDeltaDistribution()) {
			return doLightEstimateDirectDeltaDistributionTrue(bSDF, intersection, light, sampleA, scene, isSpecular);
		}
		
		return doLightEstimateDirectDeltaDistributionFalse(bSDF, intersection, light, sampleA, sampleB, scene, isSpecular);
	}
	
	private static Color3F doLightEstimateDirectDeltaDistributionFalse(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final Scene scene, final boolean isSpecular) {
		/*
	BxDFType bsdfFlags = specular ? BSDF_ALL : BxDFType(BSDF_ALL & ~BSDF_SPECULAR);
	
	Spectrum Ld(0.f);
	
	Vector3f wi;
	
	Float lightPdf = 0, scatteringPdf = 0;
	
	VisibilityTester visibility;
	
	Spectrum Li = light.Sample_Li(it, uLight, &wi, &lightPdf, &visibility);
	
	if (lightPdf > 0 && !Li.IsBlack()) {
		Spectrum f;
		
		const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
		
		f = isect.bsdf->f(isect.wo, wi, bsdfFlags) * AbsDot(wi, isect.shading.n);
		
		scatteringPdf = isect.bsdf->Pdf(isect.wo, wi, bsdfFlags);
		
		if (!f.IsBlack() && visibility.Unoccluded(scene)) {
			Float weight = PowerHeuristic(1, lightPdf, 1, scatteringPdf);
			
			Ld += f * Li * weight / lightPdf;
		}
	}
	
	BxDFType sampledType;
	
	const SurfaceInteraction &isect = (const SurfaceInteraction &)it;
	
	Spectrum f = isect.bsdf->Sample_f(isect.wo, &wi, uScattering, &scatteringPdf, bsdfFlags, &sampledType) * AbsDot(wi, isect.shading.n);
	
	bool sampledSpecular = (sampledType & BSDF_SPECULAR) != 0;
	
	if (!f.IsBlack() && scatteringPdf > 0) {
		Float weight = 1;
		
		if (!sampledSpecular) {
			lightPdf = light.Pdf_Li(it, wi);
			
			if (lightPdf == 0) {
				return Ld;
			}
			
			weight = PowerHeuristic(1, scatteringPdf, 1, lightPdf);
		}
		
		SurfaceInteraction lightIsect;
		
		Ray ray = it.SpawnRay(wi);
		
		Spectrum Tr(1.f);
		
		bool foundSurfaceInteraction = handleMedia ? scene.IntersectTr(ray, sampler, &lightIsect, &Tr) : scene.Intersect(ray, &lightIsect);
		
		Spectrum Li(0.f);
		
		if (foundSurfaceInteraction) {
			if (lightIsect.primitive->GetAreaLight() == &light) {
				Li = lightIsect.Le(-wi);
			}
		} else {
			Li = light.Le(ray);
		}
		
		if (!Li.IsBlack()) {
			Ld += f * Li * Tr * weight / scatteringPdf;
		}
	}
	
	return Ld;
		 */
		
		Color3F lightDirect = Color3F.BLACK;
		
		if(!light.isDeltaDistribution()) {
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
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), abs(Vector3F.dotProduct(incoming, surfaceIntersection.getSurfaceNormalS())));
					
					final float scatteringProbabilityDensityFunctionValue = bSDF.evaluateProbabilityDensityFunction(bXDFType, outgoing, incoming);
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(Color3F.multiply(scatteringResult, lightIncoming), SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightProbabilityDensityFunctionValue, scatteringProbabilityDensityFunctionValue, 1, 1)), lightProbabilityDensityFunctionValue));
					}
				}
			}
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F outgoing = Vector3F.negate(surfaceIntersection.getRay().getDirection());
			
			final Optional<BSDFDistributionFunctionResult> optionalBSDFDistributionFunctionResult = bSDF.sampleDistributionFunction(bXDFType, outgoing, sampleB);
			
			if(optionalBSDFDistributionFunctionResult.isPresent()) {
				final BSDFDistributionFunctionResult bSDFDistributionFunctionResult = optionalBSDFDistributionFunctionResult.get();
				
				final Vector3F incoming = bSDFDistributionFunctionResult.getIncoming();
				
				final Color3F scatteringResult = Color3F.multiply(bSDFDistributionFunctionResult.getResult(), abs(Vector3F.dotProduct(incoming, surfaceIntersection.getSurfaceNormalS())));
				
				final boolean hasSampledSpecular = bSDFDistributionFunctionResult.getBXDFType().isSpecular();
				
				final float scatteringProbabilityDensityFunctionValue = bSDFDistributionFunctionResult.getProbabilityDensityFunctionValue();
				
				if(!scatteringResult.isBlack() && scatteringProbabilityDensityFunctionValue > 0.0F) {
					float weight = 1.0F;
					
					if(!hasSampledSpecular) {
						final float lightProbabilityDensityFunctionValue = light.evaluateProbabilityDensityFunctionRadianceIncoming(intersection, incoming);
						
						if(equal(lightProbabilityDensityFunctionValue, 0.0F)) {
							return lightDirect;
						}
						
						weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(scatteringProbabilityDensityFunctionValue, lightProbabilityDensityFunctionValue, 1, 1);
					}
					
					final Ray3F ray = surfaceIntersection.createRay(incoming);
					
					final Color3F transmittance = Color3F.WHITE;
					
					final Optional<Intersection> optionalIntersectionLight = scene.intersection(ray);
					
					if(optionalIntersectionLight.isPresent()) {
						final Intersection intersectionLight = optionalIntersectionLight.get();
						
						if(intersectionLight.getPrimitive().getAreaLight().isPresent() && intersectionLight.getPrimitive().getAreaLight().get() == light) {
							final Color3F lightIncoming = intersectionLight.evaluateRadianceEmitted(Vector3F.negate(incoming));
							
							if(!lightIncoming.isBlack()) {
								lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(Color3F.multiply(Color3F.multiply(scatteringResult, lightIncoming), transmittance), weight), scatteringProbabilityDensityFunctionValue));
							}
						}
					} else {
						final Color3F lightIncoming = light.evaluateRadianceEmitted(ray);
						
						if(!lightIncoming.isBlack()) {
							lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(Color3F.multiply(Color3F.multiply(scatteringResult, lightIncoming), transmittance), weight), scatteringProbabilityDensityFunctionValue));
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
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, outgoing, incoming), abs(Vector3F.dotProduct(incoming, surfaceIntersection.getSurfaceNormalS())));
					
					if(!scatteringResult.isBlack() && doIsLightVisible(light, lightRadianceIncomingResult, scene, surfaceIntersection)) {
						lightDirect = Color3F.add(lightDirect, Color3F.divide(Color3F.multiply(scatteringResult, lightIncoming), lightProbabilityDensityFunctionValue));
					}
				}
			}
		}
		
		return lightDirect;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doLightSampleAllUniformDistribution(final BSDF bSDF, final Intersection intersection, final Scene scene) {
		return new Color3F(0.25F);
	}
	
	private static Color3F doLightSampleOneUniformDistribution(final BSDF bSDF, final Intersection intersection, final Scene scene) {
		final int lightCount = scene.getLightCount();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final int lightIndex = min((int)(random() * lightCount), lightCount - 1);
		
		final float lightProbabilityDensityFunctionValue = 1.0F / lightCount;
		
		final Light light = scene.getLight(lightIndex);
		
		final Point2F sampleA = new Point2F(random(), random());
		final Point2F sampleB = new Point2F(random(), random());
		
		return Color3F.divide(doLightEstimateDirect(bSDF, intersection, light, sampleA, sampleB, scene, false), lightProbabilityDensityFunctionValue);
	}
	
	private static boolean doIsLightVisible(final Light light, final LightRadianceIncomingResult lightIncomingRadianceResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection) {
		if(light instanceof AreaLight) {
			final Optional<Intersection> optionalIntersection = scene.intersection(surfaceIntersection.createRay(lightIncomingRadianceResult.getPoint()), 0.0F, abs(Point3F.distance(surfaceIntersection.getSurfaceIntersectionPoint(), lightIncomingRadianceResult.getPoint())) + 0.0001F);
			
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
		
		return !scene.intersects(surfaceIntersection.createRay(lightIncomingRadianceResult.getPoint()), 0.0F, abs(Point3F.distance(surfaceIntersection.getSurfaceIntersectionPoint(), lightIncomingRadianceResult.getPoint())));
	}
}