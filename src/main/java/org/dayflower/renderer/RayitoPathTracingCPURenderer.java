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

import java.util.List;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Sphere3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.SurfaceSample3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Background;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.ConstantBackground;
import org.dayflower.scene.background.PerezBackground;
import org.dayflower.scene.light.PrimitiveLight;
import org.dayflower.scene.rayito.BXDF;
import org.dayflower.scene.rayito.BXDFResult;
import org.dayflower.scene.rayito.MaterialResult;
import org.dayflower.scene.rayito.RayitoMaterial;

/**
 * A {@code RayitoPathTracingCPURenderer} is a {@link Renderer} implementation that renders using Path Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayitoPathTracingCPURenderer extends AbstractCPURenderer {
	/**
	 * Constructs a new {@code RayitoPathTracingCPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new RayitoPathTracingCPURenderer(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new RandomSampler(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	 * }
	 * </pre>
	 */
	public RayitoPathTracingCPURenderer() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new RandomSampler(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	}
	
	/**
	 * Constructs a new {@code RayitoPathTracingCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code RayitoPathTracingCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code RayitoPathTracingCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code RayitoPathTracingCPURenderer} instance
	 * @param sampler the {@link Sampler} instance associated with this {@code RayitoPathTracingCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code RayitoPathTracingCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}
	 */
	public RayitoPathTracingCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Sampler sampler, final Scene scene) {
		super(display, image, rendererConfiguration, sampler, scene, false);
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
					
					radiance = Color3F.add(radiance, doGetRadianceLight(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO));
				} else {
					radiance = Color3F.add(radiance, doGetRadianceLight(throughput, materialResult, scene, surfaceIntersection, currentRayDirectionO));
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
			} else if(currentBounce == 0) {
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.getBackground().radiance(currentRay)));
				
				break;
			} else {
				break;
			}
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadianceLight(final Color3F throughput, final MaterialResult materialResult, final Scene scene, final SurfaceIntersection3F surfaceIntersection, final Vector3F directionO) {
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
		
		final Vector3F surfaceNormal = orthonormalBasis.getW();
		
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
				
				final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
				
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
								final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
								
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
								
								final Optional<Intersection> optionalIntersection = scene.intersection(ray);
								
								if(optionalIntersection.isPresent()) {
									final Intersection intersection = optionalIntersection.get();
									
									if(primitive == intersection.getPrimitive()) {
										final float multipleImportanceSampleWeightLight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA1, probabilityDensityFunctionValueB1, 1, 1);
										
										final Color3F emittance = primitive.calculateEmittanceRGB(intersection);
										
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
							
							final Ray3F ray = surfaceIntersection.createRay(selectedDirectionO);
							
							final Optional<Intersection> optionalIntersection = scene.intersection(ray);
							
							if(optionalIntersection.isPresent()) {
								final Intersection intersection = optionalIntersection.get();
								
								if(primitive == intersection.getPrimitive()) {
									final float probabilityDensityFunctionValueB2 = primitive.calculateProbabilityDensityFunctionValueForSolidAngle(ray, intersection);
									
									if(probabilityDensityFunctionValueB2 > 0.0F) {
										final float multipleImportanceSampleWeightBRDF = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(probabilityDensityFunctionValueA2, probabilityDensityFunctionValueB2, 1, 1);
										
										final Color3F emittance = primitive.calculateEmittanceRGB(intersection);
										
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