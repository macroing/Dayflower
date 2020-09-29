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
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sqrt;

import java.util.List;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
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
import org.dayflower.scene.bxdf.Fresnel;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.material.AshikhminShirleyMaterial;
import org.dayflower.scene.material.LambertianMaterial;
import org.dayflower.scene.material.OrenNayarMaterial;
import org.dayflower.scene.material.ReflectionMaterial;
import org.dayflower.scene.material.RefractionMaterial;

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
						
						final Color3F colorRGB = doGetRadiance(lights, ray, scene, rendererConfiguration);
//						final Color3F colorRGB = doGetRadiance2(ray, scene, rendererConfiguration);
//						final Color3F colorRGB = doGetRadiance3(ray, scene, rendererConfiguration, 1);
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
	
	private static Color3F doGetRadiance(final List<Light> lights, final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
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
					
//					radiance = Color3F.add(radiance, doGetRadianceBackground(throughput, intersection, materialResult, scene));
				} else {
					radiance = Color3F.add(radiance, doGetRadianceLights(throughput, lights, materialResult, primitive, scene, surfaceIntersection, currentRayDirectionO));
//					radiance = Color3F.add(radiance, doGetRadianceLight(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO));
//					radiance = Color3F.add(radiance, doGetRadianceBackground(throughput, intersection, materialResult, scene));
				}
				
				final BXDFResult selectedBXDFResult = selectedBXDF.sampleSolidAngle(currentRayDirectionO, surfaceNormalS, orthonormalBasisS, random(), random());
				
				if(!selectedBXDFResult.isFinite()) {
					System.out.printf("A BXDFResult must have finite values!%n");
					System.out.printf("BXDF: %s%n", selectedBXDF.getClass().getSimpleName());
					System.out.printf("Probability Density Function Value: %d%n", Float.valueOf(selectedBXDFResult.getProbabilityDensityFunctionValue()));
					System.out.printf("Reflectance: %d%n", Float.valueOf(selectedBXDFResult.getReflectance()));
					
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
			} else {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.getBackground().radiance(currentRay)));
				
				break;
			}
		}
		
		return radiance;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadiance2(final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration) {
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
	private static Color3F doGetRadiance3(final Ray3F ray, final Scene scene, final RendererConfiguration rendererConfiguration, final int bounce) {
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
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
			} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
			} else if(material instanceof ReflectionMaterial) {
				final Vector3F d = Vector3F.reflection(direction, surfaceNormal, true);
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(new Ray3F(surfaceIntersectionPoint, d), scene, rendererConfiguration, currentBounce)));
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
					return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(reflectionRay, scene, rendererConfiguration, currentBounce)));
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
					return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(reflectionRay, scene, rendererConfiguration, currentBounce), probabilityRussianRouletteReflection));
				}
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doGetRadiance3(transmissionRay, scene, rendererConfiguration, currentBounce), probabilityRussianRouletteTransmission));
			} else {
				return scene.getBackground().radiance(ray);
			}
		}
		
		return scene.getBackground().radiance(ray);
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadianceBackground(final Color3F throughput, final Intersection intersection, final MaterialResult materialResult, final Scene scene) {
		Color3F radiance = Color3F.BLACK;
		
		final Background background = scene.getBackground();
		
		final BXDF selectedBXDF = materialResult.getSelectedBXDF();
		
		final Color3F color = materialResult.getColor();
		
		final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
		
		final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
		
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		
		final List<BackgroundSample> backgroundSamples = background.sample(intersection);
		
		for(final BackgroundSample backgroundSample : backgroundSamples) {
			if(!scene.intersects(backgroundSample.getRay())) {
				final Vector3F d = backgroundSample.getRay().getDirection();
				final Vector3F o = Vector3F.negate(surfaceIntersection.getRay().getDirection());
				final Vector3F n = surfaceNormal;
				final Vector3F i = Vector3F.negate(d);
				
				final BXDFResult selectedBXDFResult = selectedBXDF.evaluateSolidAngle(o, n, i);
				
				if(selectedBXDFResult.isFinite()) {
					final float probabilityDensityFunctionValue = selectedBXDFResult.getProbabilityDensityFunctionValue();
					final float reflectance = selectedBXDFResult.getReflectance();
					
					if(probabilityDensityFunctionValue > 0.0F && reflectance > 0.0F) {
						final Color3F color1 = Color3F.multiply(backgroundSample.getRadiance(), color);
						final Color3F color2 = Color3F.multiply(color1, reflectance);
						final Color3F color3 = Color3F.multiply(color2, abs(Vector3F.dotProduct(d, n)));
						final Color3F color4 = Color3F.divide(color3, probabilityDensityFunctionValue * selectedBXDFWeight);
						
						radiance = Color3F.add(radiance, color4);
					}
				}
			}
		}
		
		radiance = Color3F.multiply(throughput, radiance);
		
		return radiance;
	}
	
	@SuppressWarnings("unused")
	private static Color3F doGetRadianceLight(final Color3F throughput, final MaterialResult materialResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO) {
		Color3F radiance = Color3F.BLACK;
		
		final BXDF selectedBXDF = materialResult.getSelectedBXDF();
		
		final Color3F color = materialResult.getColor();
		
		final float selectedBXDFWeight = materialResult.getSelectedBXDFWeight();
		
		final OrthonormalBasis33F orthonormalBasis = surfaceIntersection.getOrthonormalBasisS();
		
		final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
		
		final Vector3F surfaceNormal = surfaceIntersection.getSurfaceNormalS();
		
		final int samples = 1;
		
		for(int sample = 0; sample < samples; sample++) {
			final BXDFResult selectedBXDFResult = selectedBXDF.sampleSolidAngle(directionO, surfaceNormal, orthonormalBasis, random(), random());
			
			final float probabilityDensityFunctionValueA = selectedBXDFResult.getProbabilityDensityFunctionValue();
			final float reflectance = selectedBXDFResult.getReflectance();
			
			if(probabilityDensityFunctionValueA > 0.0F && reflectance > 0.0F) {
				final Vector3F selectedDirectionI = Vector3F.normalize(selectedBXDFResult.getI());
				final Vector3F selectedDirectionO = Vector3F.negate(selectedDirectionI);
				
				final Ray3F ray = new Ray3F(surfaceIntersectionPoint, selectedDirectionO);
				
				final Optional<Intersection> optionalIntersection = scene.intersection(ray);
				
				if(!optionalIntersection.isPresent()) {
					final Color3F color1 = Color3F.multiply(scene.getBackground().radiance(ray), color);
					final Color3F color2 = Color3F.multiply(color1, reflectance);
					final Color3F color3 = Color3F.multiply(color2, abs(Vector3F.dotProduct(selectedDirectionO, surfaceNormal)) / (probabilityDensityFunctionValueA * selectedBXDFWeight));
					
					radiance = Color3F.add(radiance, color3);
				}
			}
		}
		
		radiance = Color3F.multiply(throughput, Color3F.divide(radiance, samples));
		
		return radiance;
	}
	
	private static Color3F doGetRadianceLights(final Color3F throughput, final List<Light> lights, final MaterialResult materialResult, final Primitive primitiveToSkip, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO) {
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
}