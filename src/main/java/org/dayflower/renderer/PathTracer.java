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
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sqrt;

import java.util.List;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.BXDF;
import org.dayflower.scene.BXDFResult;
import org.dayflower.scene.Background;
import org.dayflower.scene.BackgroundSample;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.MaterialResult;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.PerezBackground;
import org.dayflower.scene.bxdf.Fresnel;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.AshikhminShirleyMaterial;
import org.dayflower.scene.material.LambertianMaterial;
import org.dayflower.scene.material.OrenNayarMaterial;
import org.dayflower.scene.material.ReflectionMaterial;
import org.dayflower.scene.material.RefractionMaterial;
import org.dayflower.scene.pbrt.BSDF;
import org.dayflower.scene.pbrt.BSDFDistributionFunctionResult;
import org.dayflower.scene.pbrt.BXDFType;
import org.dayflower.scene.pbrt.PBRTMaterial;
import org.dayflower.scene.pbrt.TransportMode;

/**
 * A {@code PathTracer} is a {@link Renderer} implementation that renders using Path Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PathTracer implements Renderer {
	/**
	 * Constructs a new {@code PathTracer} instance.
	 */
	public PathTracer() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Renders {@code scene} to {@code image} and displays it using {@code display}.
	 * <p>
	 * If either {@code display}, {@code image} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pathTracer.render(display, image, scene, new RendererConfiguration());
	 * }
	 * </pre>
	 * 
	 * @param display the {@link Display} instance to display with
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image} or {@code scene} are {@code null}
	 */
	@Override
	public void render(final Display display, final Image image, final Scene scene) {
		render(display, image, scene, new RendererConfiguration());
	}
	
	/**
	 * Renders {@code scene} to {@code image} and displays it using {@code display}.
	 * <p>
	 * If either {@code display}, {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance to display with
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @param rendererConfiguration the {@link RendererConfiguration} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}
	 */
	@Override
	public void render(final Display display, final Image image, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final List<Light> lights = scene.getLights();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			final long currentTimeMillis1 = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final float sampleX = random();
					final float sampleY = random();
					
					final Optional<Ray3F> optionalRay = scene.getCamera().createPrimaryRay(x, y, sampleX, sampleY);
					
					if(optionalRay.isPresent()) {
						final Ray3F ray = optionalRay.get();
						
						final Color3F colorRGB = doGetRadiancePBRT(lights, ray, scene, rendererConfiguration);
//						final Color3F colorRGB = doGetRadianceRayito(lights, ray, scene, rendererConfiguration);
//						final Color3F colorRGB = doGetRadianceSmallPTIterative(ray, scene, rendererConfiguration);
//						final Color3F colorRGB = doGetRadianceSmallPTRecursive(ray, scene, rendererConfiguration, 1);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						image.filmAddColorXYZ(x + sampleX, y + sampleY, colorXYZ);
					}
				}
			}
			
			final long currentTimeMillis2 = System.currentTimeMillis();
			final long elapsedTimeMillis = currentTimeMillis2 - currentTimeMillis1;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender(0.5F);
				
				display.update(image);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiancePBRT(final List<Light> lights, final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
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
					
//					radiance = Color3F.add(radiance, Color3F.multiply(throughput, new Color3F(0.2F)));
				} else {
//					for (const auto &light : scene.infiniteLights) {
//						radiance += throughput * light->Le(ray);
//					}
					
//					for(final Light light : lights) {
//						radiance = Color3F.add(radiance, Color3F.multiply(throughput, new Color3F(0.5F)));
//					}
				}
			}
			
			if(!hasFoundIntersection || currentBounce >= maximumBounce) {
				break;
			}
			
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
			
			if(!(material instanceof PBRTMaterial)) {
				break;
			}
			
			final PBRTMaterial pBRTMaterial = PBRTMaterial.class.cast(material);
			
			final Optional<BSDF> optionalBSDF = pBRTMaterial.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalBSDF.isPresent()) {
				currentRay = surfaceIntersection.createRay(ray.getDirection());
				
				currentBounce--;
				
				continue;
			}
			
			final BSDF bSDF = optionalBSDF.get();
			
			/*
			 * TODO: Implement light sampling!
			 */
			
//			if (isect.bsdf->NumComponents(BxDFType(BSDF_ALL & ~BSDF_SPECULAR)) > 0) {
//				radiance += throughput * UniformSampleOneLight(isect, scene, arena, sampler, false, distrib);
//			}
			
			if(bSDF.countBXDFsBySpecularType(false) > 0) {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, new Color3F(0.5F)));
			}
			
			final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
			
			final Optional<BSDFDistributionFunctionResult> optionalBSDFDistributionFunctionResult = bSDF.sampleDistributionFunction(outgoing, new Point2F(random(), random()), true, true);
			
			if(!optionalBSDFDistributionFunctionResult.isPresent()) {
				break;
			}
			
			final BSDFDistributionFunctionResult bSDFDistributionFunctionResult = optionalBSDFDistributionFunctionResult.get();
			
			final BXDFType bXDFType = bSDFDistributionFunctionResult.getBXDFType();
			
			final Color3F result = bSDFDistributionFunctionResult.getResult();
			
			final Vector3F incoming = bSDFDistributionFunctionResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFDistributionFunctionResult.getProbabilityDensityFunctionValue();
			
			throughput = Color3F.multiply(throughput, Color3F.divide(Color3F.multiply(result, abs(Vector3F.dotProduct(incoming, surfaceNormalS))), probabilityDensityFunctionValue));
			
			isSpecularBounce = bXDFType.isSpecular();
			
			if(bXDFType.hasTransmission() && bXDFType.isSpecular()) {
				final float eta = bSDF.getEta();
				
				etaScale *= Vector3F.dotProduct(outgoing, surfaceNormalS) > 0.0F ? eta * eta : 1.0F / (eta * eta);
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
		
		if(radiance.hasInfinites() || radiance.hasNaNs()) {
			return Color3F.BLACK;
		}
		
		return radiance;
	}
	
	private static Color3F doGetRadianceRayito(final List<Light> lights, final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		int currentBounceDiracDistribution = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F currentRayDirectionI = currentRay.getDirection();
				final Vector3F currentRayDirectionO = Vector3F.negate(currentRayDirectionI);
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final MaterialResult materialResult = material.evaluate(intersection);
				
				final BXDF selectedBXDF = materialResult.getSelectedBXDF();
				
				final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
				
				final Color3F color = materialResult.getColor();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final OrthonormalBasis33F orthonormalBasisS = surfaceIntersection.getOrthonormalBasisS();
				
				final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
				
//				final Vector3F surfaceNormalG = surfaceIntersection.getSurfaceNormalG();
//				final Vector3F surfaceNormalGCorrectlyOriented = Vector3F.dotProduct(currentRayDirectionO, surfaceNormalG) < 0.0F ? Vector3F.negate(surfaceNormalG) : surfaceNormalG;
				final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
				
				if(currentBounce == 0 || currentBounce == currentBounceDiracDistribution) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, material.emittance(intersection)));
				}
				
				if(selectedBXDF.isDiracDistribution()) {
					currentBounceDiracDistribution++;
					
					radiance = Color3F.add(radiance, doGetRadianceRayitoLight(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO));
//					radiance = Color3F.add(radiance, doGetRadianceRayitoBackground(throughput, intersection, materialResult, scene));
				} else {
					radiance = Color3F.add(radiance, doGetRadianceRayitoLight(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO));
					radiance = Color3F.add(radiance, doGetRadianceRayitoLights(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO, lights, primitive));
//					radiance = Color3F.add(radiance, doGetRadianceRayitoBackground(throughput, intersection, materialResult, scene));
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
//					currentRay = new Ray3F(Point3F.add(surfaceIntersectionPoint, surfaceNormalGCorrectlyOriented, 0.000001F), Vector3F.negate(Vector3F.normalize(selectedBXDFResult.getI())));
					currentRay = new Ray3F(surfaceIntersectionPoint, Vector3F.negate(Vector3F.normalize(selectedBXDFResult.getI())));
					
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
			} else if(currentBounce == 0) {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.getBackground().radiance(currentRay)));
				
				break;
			} else {
				break;
			}
		}
		
		return radiance;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadianceRayitoBackground(final Color3F throughput, final Intersection intersection, final MaterialResult materialResult, final Scene scene) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		final Background background = scene.getBackground();
		
		final BXDF selectedBXDF = materialResult.getSelectedBXDF();
		
		final Color3F color = materialResult.getColor();
		
		final float colorR = color.getR();
		final float colorG = color.getG();
		final float colorB = color.getB();
		
		final float throughputR = throughput.getR();
		final float throughputG = throughput.getG();
		final float throughputB = throughput.getB();
		
		final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Vector3F directionI = surfaceIntersection.getRay().getDirection();
		final Vector3F directionO = Vector3F.negate(directionI);
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		
		final List<BackgroundSample> backgroundSamples = background.sample(intersection);
		
		for(final BackgroundSample backgroundSample : backgroundSamples) {
			final Vector3F selectedDirectionO = backgroundSample.getRay().getDirection();
			final Vector3F selectedDirectionI = Vector3F.negate(selectedDirectionO);
			
			final BXDFResult selectedBXDFResult = selectedBXDF.evaluateSolidAngle(directionO, surfaceNormal, selectedDirectionI);
			
			if(selectedBXDFResult.isFinite()) {
				final float probabilityDensityFunctionValueA = SampleGeneratorF.sphereUniformDistributionProbabilityDensityFunction();
				final float probabilityDensityFunctionValueB = selectedBXDFResult.getProbabilityDensityFunctionValue();
				final float reflectance = selectedBXDFResult.getReflectance();
				
				if(probabilityDensityFunctionValueB > 0.0F && reflectance > 0.0F && !scene.intersects(backgroundSample.getRay())) {
					final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA, probabilityDensityFunctionValueB, 1, 1);
					
					final Color3F emittance = backgroundSample.getRadiance();
					
					final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
					final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA * selectedBXDFWeight);
					
					radianceR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
					radianceG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
					radianceB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
				}
			}
		}
		
		final float samplesReciprocal = 1.0F / backgroundSamples.size();
		
		radianceR = throughputR * (radianceR * samplesReciprocal);
		radianceG = throughputG * (radianceG * samplesReciprocal);
		radianceB = throughputB * (radianceB * samplesReciprocal);
		
		return new Color3F(radianceR, radianceG, radianceB);
	}
	
	private static Color3F doGetRadianceRayitoLight(final Color3F throughput, final MaterialResult materialResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO) {
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
		
		final Background background = scene.getBackground();
		
		final OrthonormalBasis33F orthonormalBasis = surfaceIntersection.getOrthonormalBasisS();
		
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		
		int samples = 0;
		
		final int skySamples = 1;
		
		for(int skySample = 0; skySample < skySamples; samples++, skySample++) {
			final BXDFResult selectedBXDFResult = selectedBXDF.sampleSolidAngle(directionO, surfaceNormal, orthonormalBasis, random(), random());
			
			final float probabilityDensityFunctionValueA = selectedBXDFResult.getProbabilityDensityFunctionValue();
			final float reflectance = selectedBXDFResult.getReflectance();
			
			if(probabilityDensityFunctionValueA > 0.0F && reflectance > 0.0F) {
				final Vector3F selectedDirectionI = Vector3F.normalize(selectedBXDFResult.getI());
				final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
				
				final float probabilityDensityFunctionValueB = SampleGeneratorF.sphereUniformDistributionProbabilityDensityFunction();
				
				final Ray3F ray = new Ray3F(surfaceIntersectionPoint, selectedDirectionO);
				
				if(!scene.intersects(ray)) {
					final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA, probabilityDensityFunctionValueB, 1, 1);
					
					final Color3F emittance = background.radiance(ray);
					
					final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
					final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA * selectedBXDFWeight);
					
					radianceR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
					radianceG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
					radianceB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightBRDF * probabilityDensityFunctionValueReciprocal;
				}
			}
		}
		
		if(background instanceof PerezBackground) {
			final PerezBackground perezBackground = PerezBackground.class.cast(background);
			
			final Color3F sunColor = Color3F.multiply(perezBackground.getSunColor(), 10000000.0F);//1000000.0F, 1000000000.0F
			
			final Vector3F sunDirectionWorldSpace = perezBackground.getSunDirectionWorldSpace();
			
			final int sunSamples = 1;
			
			for(int sunSample = 0; sunSample < sunSamples; samples++, sunSample++) {
				final Sphere3F sphere = new Sphere3F(100.0F/*10.0F*/, Point3F.add(new Point3F(), sunDirectionWorldSpace, 1000.0F));
				
				final Optional<SurfaceSample3F> optionalSurfaceSample = sphere.sample(surfaceIntersectionPoint, surfaceNormal, random(), random());
				
				if(optionalSurfaceSample.isPresent()) {
					final SurfaceSample3F surfaceSample = optionalSurfaceSample.get();
					
					final Point3F point = surfaceSample.getPoint();
					
					final float probabilityDensityFunctionValueA1 = surfaceSample.getProbabilityDensityFunctionValue();
					
					if(probabilityDensityFunctionValueA1 > 0.0F) {
						final Vector3F selectedDirectionI = Vector3F.normalize(Vector3F.direction(point, surfaceIntersectionPoint));
						final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
						
						final BXDFResult selectedBXDFResult = selectedBXDF.evaluateSolidAngle(directionO, surfaceNormal, selectedDirectionI);
						
						if(selectedBXDFResult.isFinite()) {
							final float probabilityDensityFunctionValueB1 = selectedBXDFResult.getProbabilityDensityFunctionValue();
							final float reflectance = selectedBXDFResult.getReflectance();
							
							if(probabilityDensityFunctionValueB1 > 0.0F && reflectance > 0.0F) {
								final Ray3F ray = new Ray3F(surfaceIntersectionPoint, selectedDirectionO);
								
								if(!scene.intersects(ray)) {
									final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA1, probabilityDensityFunctionValueB1, 1, 1);
									
									final Color3F emittance = sunColor;
									
									final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
									final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA1 * selectedBXDFWeight);
									
									radianceR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
									radianceG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
									radianceB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
								}
							}
						}
					}
				}
			}
		}
		
		final float samplesReciprocal = 1.0F / samples;
		
		radianceR = throughputR * (radianceR * samplesReciprocal);
		radianceG = throughputG * (radianceG * samplesReciprocal);
		radianceB = throughputB * (radianceB * samplesReciprocal);
		
		if(!Float.isFinite(radianceR) || !Float.isFinite(radianceG) || !Float.isFinite(radianceB)) {
			return Color3F.BLACK;
		}
		
		return new Color3F(radianceR, radianceG, radianceB);
	}
	
	private static Color3F doGetRadianceRayitoLights(final Color3F throughput, final MaterialResult materialResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO, final List<Light> lights, final Primitive primitiveToSkip) {
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
		
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		
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
								final Ray3F ray = new Ray3F(surfaceIntersectionPoint, selectedDirectionO);
								
								final Optional<Intersection> optionalIntersection = scene.intersection(ray);
								
								if(optionalIntersection.isPresent()) {
									final Intersection intersection = optionalIntersection.get();
									
									if(primitive == intersection.getPrimitive()) {
										final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA1, probabilityDensityFunctionValueB1, 1, 1);
										
										final Color3F emittance = primitive.calculateEmittance(intersection);
										
										final float oDotNAbs = abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal));
										final float probabilityDensityFunctionValueReciprocal = 1.0F / (probabilityDensityFunctionValueA1 * selectedBXDFWeight);
										
										radianceLightR += emittance.getR() * colorR * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										radianceLightG += emittance.getG() * colorG * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
										radianceLightB += emittance.getB() * colorB * reflectance * oDotNAbs * multipleImportanceSampleWeightLight * probabilityDensityFunctionValueReciprocal;
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
							
							final Ray3F ray = new Ray3F(surfaceIntersectionPoint, selectedDirectionO);
							
							final Optional<Intersection> optionalIntersection = scene.intersection(ray);
							
							if(optionalIntersection.isPresent()) {
								final Intersection intersection = optionalIntersection.get();
								
								if(primitive == intersection.getPrimitive()) {
									final float probabilityDensityFunctionValueB2 = primitive.calculateProbabilityDensityFunctionValueForSolidAngle(ray, intersection);
									
									if(probabilityDensityFunctionValueB2 > 0.0F) {
										final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA2, probabilityDensityFunctionValueB2, 1, 1);
										
										final Color3F emittance = primitive.calculateEmittance(intersection);
										
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
				
				final float samplesReciprocal = 1.0F / samples;
				
				radianceR += throughputR * (radianceLightR * samplesReciprocal);
				radianceG += throughputG * (radianceLightG * samplesReciprocal);
				radianceB += throughputB * (radianceLightB * samplesReciprocal);
			}
		}
		
		return new Color3F(radianceR, radianceG, radianceB);
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadianceSmallPTIterative(final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int maximumBounce = rendererConfiguration.getMaximumBounce();
		final int minimumBounceRussianRoulette = rendererConfiguration.getMinimumBounceRussianRoulette();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		int currentBounce = 0;
		
		while(currentBounce < maximumBounce) {
			final Optional<Intersection> optionalIntersection = scene.intersection(currentRay);
			
			if(optionalIntersection.isPresent()) {
				final Vector3F currentDirection = currentRay.getDirection();
				
				final Intersection intersection = optionalIntersection.get();
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
				
				final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
				
				final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
				final Vector3F surfaceNormalCorrectlyOriented = Vector3F.dotProduct(currentDirection, surfaceNormal) < 0.0F ? surfaceNormal : Vector3F.negate(surfaceNormal);
				
				Color3F albedo = primitive.getTextureAlbedo().getColor(intersection);
				Color3F emittance = primitive.getTextureEmittance().getColor(intersection);
				
				if(currentBounce >= minimumBounceRussianRoulette) {
					final float probability = albedo.maximum();
					
					if(random() > probability) {
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
					
					currentRay = new Ray3F(surfaceIntersectionPoint, d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
					final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
					final Vector3F w = surfaceNormalCorrectlyOriented;
					final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
					final Vector3F v = Vector3F.crossProduct(w, u);
					final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
					
					currentRay = new Ray3F(surfaceIntersectionPoint, d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof ReflectionMaterial) {
					final Vector3F d = Vector3F.reflection(currentDirection, surfaceNormal, true);
					
					currentRay = new Ray3F(surfaceIntersectionPoint, d);
					
					throughput = Color3F.multiply(throughput, albedo);
				} else if(material instanceof RefractionMaterial) {
					final Vector3F reflectionDirection = Vector3F.reflection(currentDirection, surfaceNormal, true);
					
					final Ray3F reflectionRay = new Ray3F(surfaceIntersectionPoint, reflectionDirection);
					
					final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
					
					final float etaA = 1.0F;
					final float etaB = 1.5F;
					final float eta = isEntering ? etaA / etaB : etaB / etaA;
					
					final float cosTheta = Vector3F.dotProduct(currentDirection, surfaceNormalCorrectlyOriented);
					final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
					
					if(cosTheta2Squared < 0.0F) {
						currentRay = reflectionRay;
						
						throughput = Color3F.multiply(throughput, albedo);
					} else {
						final Vector3F transmissionDirection = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(currentDirection, eta), Vector3F.multiply(surfaceNormal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
						
						final Ray3F transmissionRay = new Ray3F(surfaceIntersectionPoint, transmissionDirection);
						
						final float a = etaB - etaA;
						final float b = etaB + etaA;
						
						final float reflectance = Fresnel.dielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
						final float transmittance = 1.0F - reflectance;
						
						final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
						final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
						final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
						
						if(random() < probabilityRussianRoulette) {
							currentRay = reflectionRay;
							
							throughput = Color3F.multiply(throughput, albedo);
							throughput = Color3F.multiply(throughput, probabilityRussianRouletteReflection);
						} else {
							currentRay = transmissionRay;
							
							throughput = Color3F.multiply(throughput, albedo);
							throughput = Color3F.multiply(throughput, probabilityRussianRouletteTransmission);
						}
					}
				}
				
				currentBounce++;
			} else {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.getBackground().radiance(currentRay)));
				
				break;
			}
		}
		
		return radiance;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadianceSmallPTRecursive(final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration, final int bounce) {
		final Optional<Intersection> optionalIntersection = scene.intersection(ray);
		
		if(bounce >= rendererConfiguration.getMaximumBounce()) {
			return Color3F.BLACK;
		} else if(optionalIntersection.isPresent()) {
			final Vector3F direction = ray.getDirection();
			
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
			final Vector3F surfaceNormalCorrectlyOriented = Vector3F.dotProduct(direction, surfaceNormal) < 0.0F ? surfaceNormal : Vector3F.negate(surfaceNormal);
			
			Color3F albedo = primitive.getTextureAlbedo().getColor(intersection);
			Color3F emittance = primitive.getTextureEmittance().getColor(intersection);
			
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
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
			} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
			} else if(material instanceof ReflectionMaterial) {
				final Vector3F d = Vector3F.reflection(direction, surfaceNormal, true);
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
			} else if(material instanceof RefractionMaterial) {
				final Vector3F reflectionDirection = Vector3F.reflection(direction, surfaceNormal, true);
				
				final Ray3F reflectionRay = new Ray3F(surfaceIntersectionPoint, reflectionDirection);
				
				final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
				
				final float etaA = 1.0F;
				final float etaB = 1.5F;
				final float eta = isEntering ? etaA / etaB : etaB / etaA;
				
				final float cosTheta = Vector3F.dotProduct(direction, surfaceNormalCorrectlyOriented);
				final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
				
				if(cosTheta2Squared < 0.0F) {
					return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(reflectionRay, scene, rendererConfiguration, currentBounce)));
				}
				
				final Vector3F transmissionDirection = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(direction, eta), Vector3F.multiply(surfaceNormal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
				
				final Ray3F transmissionRay = new Ray3F(surfaceIntersectionPoint, transmissionDirection);
				
				final float a = etaB - etaA;
				final float b = etaB + etaA;
				
				final float reflectance = Fresnel.dielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
				final float transmittance = 1.0F - reflectance;
				
				final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
				final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
				final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
				
				if(random() < probabilityRussianRoulette) {
					return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(reflectionRay, scene, rendererConfiguration, currentBounce), probabilityRussianRouletteReflection));
				}
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadianceSmallPTRecursive(transmissionRay, scene, rendererConfiguration, currentBounce), probabilityRussianRouletteTransmission));
			} else {
				return scene.getBackground().radiance(ray);
			}
		}
		
		return scene.getBackground().radiance(ray);
	}
}