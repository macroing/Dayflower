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
package org.dayflower.scene;

import static org.dayflower.utility.Floats.MAX_VALUE;
import static org.dayflower.utility.Floats.MIN_VALUE;
import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.minOrNaN;
import static org.dayflower.utility.Floats.normalize;
import static org.dayflower.utility.Floats.random;
import static org.dayflower.utility.Floats.saturate;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.dayflower.color.Color3F;
import org.dayflower.geometry.AngleF;
import org.dayflower.geometry.BoundingVolume3F;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.Shape3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.geometry.boundingvolume.AxisAlignedBoundingBox3F;
import org.dayflower.geometry.boundingvolume.InfiniteBoundingVolume3F;
import org.dayflower.node.Node;
import org.dayflower.node.NodeHierarchicalVisitor;
import org.dayflower.node.NodeTraversalException;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.sampler.Sample2F;
import org.dayflower.sampler.Sampler;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Scene} represents a scene and is associated with a {@link Camera} instance, a {@code List} of {@link Light} instances and a {@code List} of {@link Primitive} instances.
 * <p>
 * This class is mutable and therefore not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Scene implements Node {
	/**
	 * The default maximum parametric distance value that is equal to {@code Floats.MAX_VALUE}.
	 */
	public static final float T_MAXIMUM = MAX_VALUE;
	
	/**
	 * The default minimum parametric distance value that is equal to {@code 0.001F}.
	 */
	public static final float T_MINIMUM = 0.001F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BVHNode bVHNode;
	private Camera camera;
	private final CameraObserver cameraObserver;
	private final List<Light> lights;
	private final List<Primitive> primitives;
	private final List<Primitive> primitivesExternalToBVH;
	private final List<SceneObserver> sceneObservers;
	private final PrimitiveObserver primitiveObserver;
	private Sampler sampler;
	private String name;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Scene(new Camera());
	 * }
	 * </pre>
	 */
	public Scene() {
		this(new Camera());
	}
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Scene(camera, "Default");
	 * }
	 * </pre>
	 * 
	 * @param camera the {@link Camera} instance associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	public Scene(final Camera camera) {
		this(camera, "Default");
	}
	
	/**
	 * Constructs a new {@code Scene} instance.
	 * <p>
	 * If either {@code camera} or {@code name} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@link Camera} instance associated with this {@code Scene} instance
	 * @param name the name associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code camera} or {@code name} are {@code null}
	 */
	public Scene(final Camera camera, final String name) {
		this.bVHNode = null;
		this.sceneObservers = new CopyOnWriteArrayList<>();
		this.cameraObserver = new CameraObserverImpl(this, this.sceneObservers);
		this.camera = Objects.requireNonNull(camera, "camera == null");
		this.camera.addCameraObserver(this.cameraObserver);
		this.lights = new CopyOnWriteArrayList<>();
		this.primitives = new CopyOnWriteArrayList<>();
		this.primitivesExternalToBVH = new CopyOnWriteArrayList<>();
		this.primitiveObserver = new PrimitiveObserverImpl(this, this.sceneObservers);
		this.sampler = new RandomSampler();
		this.name = Objects.requireNonNull(name, "name == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Camera} instance associated with this {@code Scene} instance.
	 * 
	 * @return the {@code Camera} instance associated with this {@code Scene} instance
	 */
	public Camera getCamera() {
		return this.camera;
	}
	
	/**
	 * Returns a copy of the {@link Camera} instance associated with this {@code Scene} instance.
	 * 
	 * @return a copy of the {@code Camera} instance associated with this {@code Scene} instance
	 */
	public Camera getCameraCopy() {
		synchronized(this.camera) {
			return this.camera.copy();
		}
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceAmbientOcclusion(ray, Scene.T_MINIMUM, Scene.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceAmbientOcclusion(final Ray3F ray) {
		return radianceAmbientOcclusion(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceAmbientOcclusion(ray, tMinimum, tMaximum, false);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceAmbientOcclusion(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return radianceAmbientOcclusion(ray, tMinimum, tMaximum, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceAmbientOcclusion(ray, tMinimum, tMaximum, isPreviewMode, 20.0F);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceAmbientOcclusion(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode) {
		return radianceAmbientOcclusion(ray, tMinimum, tMaximum, isPreviewMode, 20.0F);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceAmbientOcclusion(ray, tMinimum, tMaximum, isPreviewMode, maximumDistance, 10);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @param maximumDistance the maximum distance
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceAmbientOcclusion(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final float maximumDistance) {
		return radianceAmbientOcclusion(ray, tMinimum, tMaximum, isPreviewMode, maximumDistance, 10);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @param maximumDistance the maximum distance
	 * @param samples the samples to use
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using an Ambient Occlusion algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceAmbientOcclusion(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final float maximumDistance, final int samples) {
		final Optional<Intersection> optionalIntersection = intersection(ray, tMinimum, tMaximum);
		
		if(optionalIntersection.isPresent()) {
			Color3F radiance = Color3F.BLACK;
			
			final Intersection intersection = optionalIntersection.get();
			
			final OrthonormalBasis33F orthonormalBasisGWorldSpace = intersection.getOrthonormalBasisG();
			
			for(int sample = 0; sample < samples; sample++) {
				final Vector3F directionWorldSpace = Vector3F.normalize(Vector3F.transform(SampleGeneratorF.sampleHemisphereUniformDistribution(), orthonormalBasisGWorldSpace));
				
				final Ray3F rayWorldSpaceShadow = intersection.createRay(directionWorldSpace);
				
				if(maximumDistance > 0.0F) {
					final Optional<Intersection> optionalIntersectionShadow = intersection(rayWorldSpaceShadow, tMinimum, tMaximum);
					
					if(optionalIntersectionShadow.isPresent()) {
						final Intersection intersectionShadow = optionalIntersectionShadow.get();
						
						final float t = intersectionShadow.getT();
						
						radiance = Color3F.add(radiance, new Color3F(normalize(saturate(t, 0.0F, maximumDistance), 0.0F, maximumDistance)));
					} else {
						radiance = Color3F.add(radiance, Color3F.WHITE);
					}
				} else if(!intersects(rayWorldSpaceShadow, tMinimum, tMaximum)) {
					radiance = Color3F.add(radiance, Color3F.WHITE);
				}
			}
			
			radiance = Color3F.multiply(radiance, PI / samples);
			radiance = Color3F.divide(radiance, PI);
			
			return radiance;
		} else if(isPreviewMode) {
			return Color3F.WHITE;
		} else {
			return Color3F.BLACK;
		}
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radiancePathTracer(ray, Scene.T_MINIMUM, Scene.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radiancePathTracer(final Ray3F ray) {
		return radiancePathTracer(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radiancePathTracer(ray, tMinimum, tMaximum, false);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radiancePathTracer(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return radiancePathTracer(ray, tMinimum, tMaximum, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radiancePathTracer(ray, tMinimum, tMaximum, isPreviewMode, 20);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radiancePathTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode) {
		return radiancePathTracer(ray, tMinimum, tMaximum, isPreviewMode, 20);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radiancePathTracer(ray, tMinimum, tMaximum, isPreviewMode, maximumBounce, 5);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @param maximumBounce the maximum bounce
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radiancePathTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final int maximumBounce) {
		return radiancePathTracer(ray, tMinimum, tMaximum, isPreviewMode, maximumBounce, 5);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @param maximumBounce the maximum bounce
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Path Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radiancePathTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final int maximumBounce, final int minimumBounceRussianRoulette) {
		final List<Light> lights = this.lights;
		
		final Sampler sampler = getSampler();
		
		Color3F radiance = Color3F.BLACK;
		Color3F throughput = Color3F.WHITE;
		
		Ray3F currentRay = ray;
		
		boolean isSpecularBounce = false;
		
		float etaScale = 1.0F;
		
		for(int currentBounce = 0; true; currentBounce++) {
			final Optional<Intersection> optionalIntersection = intersection(currentRay, tMinimum, tMaximum);
			
			if(optionalIntersection.isPresent()) {
				final Intersection intersection = optionalIntersection.get();
				
				final Vector3F outgoing = Vector3F.negate(currentRay.getDirection());
				
				if(currentBounce == 0 || isSpecularBounce) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, optionalIntersection.get().evaluateRadianceEmitted(outgoing)));
				}
				
				if(currentBounce >= maximumBounce) {
					break;
				}
				
				final Primitive primitive = intersection.getPrimitive();
				
				final Material material = primitive.getMaterial();
				
				final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
				
				if(!optionalBSDF.isPresent()) {
					currentRay = intersection.createRay(currentRay.getDirection());
					
					currentBounce--;
					
					continue;
				}
				
				final BSDF bSDF = optionalBSDF.get();
				
				if(bSDF.countBXDFsBySpecularType(false) > 0) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, sampleOneLightUniformDistribution(bSDF, intersection)));
				}
				
				final Vector3F surfaceNormalG = intersection.getSurfaceNormalG();
				final Vector3F surfaceNormalS = intersection.getSurfaceNormalS();
				
				final Sample2F sample = sampler.sample2();
				
				final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.ALL, new Point2F(sample.getU(), sample.getV()));
				
				if(!optionalBSDFResult.isPresent()) {
					break;
				}
				
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Color3F result = bSDFResult.getResult();
				
				final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(result.isBlack() || isZero(probabilityDensityFunctionValue)) {
					break;
				}
				
				final Vector3F incoming = bSDFResult.getIncoming();
				
				throughput = Color3F.multiply(throughput, Color3F.divide(Color3F.multiply(result, abs(Vector3F.dotProduct(incoming, surfaceNormalS))), probabilityDensityFunctionValue));
				
				final BXDFType bXDFType = bSDFResult.getBXDFType();
				
				isSpecularBounce = bXDFType.isSpecular();
				
				if(bXDFType.hasTransmission() && bXDFType.isSpecular()) {
					etaScale *= Vector3F.dotProduct(outgoing, surfaceNormalG) > 0.0F ? bSDF.getEta() * bSDF.getEta() : 1.0F / (bSDF.getEta() * bSDF.getEta());
				}
				
				currentRay = intersection.createRay(incoming);
				
				final Color3F russianRouletteThroughput = Color3F.multiply(throughput, etaScale);
				
				if(russianRouletteThroughput.maximum() < 1.0F && currentBounce > minimumBounceRussianRoulette) {
					final float probability = max(0.05F, 1.0F - russianRouletteThroughput.maximum());
					
					if(sampler.sample1().getU() < probability) {
						break;
					}
					
					throughput = Color3F.divide(throughput, 1.0F - probability);
				}
			} else if(currentBounce == 0 && isPreviewMode) {
				radiance = Color3F.WHITE;
				
				break;
			} else if(currentBounce == 0 || isSpecularBounce) {
				for(final Light light : lights) {
					radiance = Color3F.add(radiance, Color3F.multiply(throughput, light.evaluateRadianceEmitted(currentRay)));
				}
				
				break;
			} else {
				break;
			}
		}
		
		return radiance;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceRayCaster(ray, Scene.T_MINIMUM, Scene.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayCaster(final Ray3F ray) {
		return radianceRayCaster(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceRayCaster(ray, tMinimum, tMaximum, false);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayCaster(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return radianceRayCaster(ray, tMinimum, tMaximum, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Caster algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayCaster(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode) {
		Color3F radiance = Color3F.BLACK;
		
		final Optional<Intersection> optionalIntersection = intersection(ray, tMinimum, tMaximum);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Vector3F surfaceNormal = intersection.getSurfaceNormalS();
			
			radiance = Color3F.multiply(Color3F.GRAY_0_50, abs(Vector3F.dotProduct(surfaceNormal, ray.getDirection())));
		} else if(isPreviewMode) {
			return Color3F.WHITE;
		} else {
			for(final Light light : this.lights) {
				radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
			}
		}
		
		return radiance;
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceRayTracer(ray, Scene.T_MINIMUM, Scene.T_MAXIMUM);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayTracer(final Ray3F ray) {
		return radianceRayTracer(ray, T_MINIMUM, T_MAXIMUM);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceRayTracer(ray, tMinimum, tMaximum, false);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayTracer(final Ray3F ray, final float tMinimum, final float tMaximum) {
		return radianceRayTracer(ray, tMinimum, tMaximum, false);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * scene.radianceRayTracer(ray, tMinimum, tMaximum, isPreviewMode, 20);
	 * }
	 * </pre>
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode) {
		return radianceRayTracer(ray, tMinimum, tMaximum, isPreviewMode, 20);
	}
	
	/**
	 * Returns a {@link Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} instance to trace
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @param isPreviewMode {@code true} if, and only if, preview mode is enabled, {@code false} otherwise
	 * @param maximumBounce the maximum bounce
	 * @return a {@code Color3F} instance with the radiance along {@code ray} using a Ray Tracer algorithm
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Color3F radianceRayTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final int maximumBounce) {
		return doRadianceRayTracer(ray, tMinimum, tMaximum, isPreviewMode, maximumBounce, 0);
	}
	
	/**
	 * Samples one {@link Light} instance using a uniform distribution.
	 * <p>
	 * Returns a {@link Color3F} instance with the radiance of the sampled {@code Light} instance.
	 * <p>
	 * If either {@code bSDF} or {@code intersection} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bSDF a {@link BSDF} instance
	 * @param intersection an {@link Intersection} instance
	 * @return a {@code Color3F} instance with the radiance of the sampled {@code Light} instance
	 * @throws NullPointerException thrown if, and only if, either {@code bSDF} or {@code intersection} are {@code null}
	 */
	public Color3F sampleOneLightUniformDistribution(final BSDF bSDF, final Intersection intersection) {
		final int lightCount = getLightCount();
		
		if(lightCount == 0) {
			return Color3F.BLACK;
		}
		
		final Light light = getLight(min(toInt(random() * lightCount), lightCount - 1));
		
		final Sample2F sampleA = this.sampler.sample2();
		final Sample2F sampleB = this.sampler.sample2();
		
		final Point2F pointA = new Point2F(sampleA.getU(), sampleA.getV());
		final Point2F pointB = new Point2F(sampleB.getU(), sampleB.getV());
		
		return Color3F.divide(doEstimateDirectLight(bSDF, intersection, light, pointA, pointB, false), 1.0F / lightCount);
	}
	
	/**
	 * Returns the {@link Light} instance at {@code index}.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code scene.getLightCount()}, an {@code IndexOutOfBoundsException} will be thrown.
	 * 
	 * @param index the index
	 * @return the {@code Light} instance at {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code index} is less than {@code 0} or greater than or equal to {@code scene.getLightCount()}
	 */
	public Light getLight(final int index) {
		return this.lights.get(index);
	}
	
	/**
	 * Returns a {@code List} with all {@link Light} instances currently associated with this {@code Scene} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Scene} instance.
	 * 
	 * @return a {@code List} with all {@code Light} instances currently associated with this {@code Scene} instance
	 */
	public List<Light> getLights() {
		return new ArrayList<>(this.lights);
	}
	
	/**
	 * Returns a {@code List} with all {@link Primitive} instances currently associated with this {@code Scene} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Scene} instance.
	 * 
	 * @return a {@code List} with all {@code Primitive} instances currently associated with this {@code Scene} instance
	 */
	public List<Primitive> getPrimitives() {
		return new ArrayList<>(this.primitives);
	}
	
	/**
	 * Returns a {@code List} with all {@link SceneObserver} instances currently associated with this {@code Scene} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code Scene} instance.
	 * 
	 * @return a {@code List} with all {@code SceneObserver} instances currently associated with this {@code Scene} instance
	 */
	public List<SceneObserver> getSceneObservers() {
		return new ArrayList<>(this.sceneObservers);
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns an {@code Optional} with an optional {@link Intersection} instance that contains information about the intersection, if it was found.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return an {@code Optional} with an optional {@code Intersection} instance that contains information about the intersection, if it was found
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public Optional<Intersection> intersection(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final Intersector intersector = new Intersector(ray, tMinimum, tMaximum);
		
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				intersector.intersection(primitive);
			}
			
			bVHNode.intersection(intersector);
			
			return intersector.computeIntersection();
		}
		
		for(final Primitive primitive : this.primitives) {
			intersector.intersection(primitive);
		}
		
		return intersector.computeIntersection();
	}
	
	/**
	 * Returns the {@link Sampler} instance associated with this {@code Scene} instance.
	 * 
	 * @return the {@code Sampler} instance associated with this {@code Scene} instance
	 */
	public Sampler getSampler() {
		return this.sampler;
	}
	
	/**
	 * Returns the name associated with this {@code Scene} instance.
	 * 
	 * @return the name associated with this {@code Scene} instance
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Scene} instance.
	 * 
	 * @return a {@code String} representation of this {@code Scene} instance
	 */
	@Override
	public String toString() {
		return String.format("new Scene(%s, %s)", this.camera, this.name);
	}
	
	/**
	 * Accepts a {@link NodeHierarchicalVisitor}.
	 * <p>
	 * Returns the result of {@code nodeHierarchicalVisitor.visitLeave(this)}.
	 * <p>
	 * If {@code nodeHierarchicalVisitor} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}, a {@code NodeTraversalException} will be thrown with the {@code RuntimeException} wrapped.
	 * <p>
	 * This implementation will:
	 * <ul>
	 * <li>throw a {@code NullPointerException} if {@code nodeHierarchicalVisitor} is {@code null}.</li>
	 * <li>throw a {@code NodeTraversalException} if {@code nodeHierarchicalVisitor} throws a {@code RuntimeException}.</li>
	 * <li>traverse its child {@code Node} instances.</li>
	 * </ul>
	 * 
	 * @param nodeHierarchicalVisitor the {@code NodeHierarchicalVisitor} to accept
	 * @return the result of {@code nodeHierarchicalVisitor.visitLeave(this)}
	 * @throws NodeTraversalException thrown if, and only if, a {@code RuntimeException} is thrown by the current {@code NodeHierarchicalVisitor}
	 * @throws NullPointerException thrown if, and only if, {@code nodeHierarchicalVisitor} is {@code null}
	 */
	@Override
	public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
		Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
		
		try {
			if(nodeHierarchicalVisitor.visitEnter(this)) {
				if(this.bVHNode != null && !this.bVHNode.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				if(!this.camera.accept(nodeHierarchicalVisitor)) {
					return nodeHierarchicalVisitor.visitLeave(this);
				}
				
				for(final Light light : this.lights) {
					if(!light.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				for(final Primitive primitive : this.primitives) {
					if(!primitive.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
			}
			
			return nodeHierarchicalVisitor.visitLeave(this);
		} catch(final RuntimeException e) {
			throw new NodeTraversalException(e);
		}
	}
	
	/**
	 * Adds {@code light} to this {@code Scene} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was added, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to add
	 * @return {@code true} if, and only if, {@code light} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public boolean addLight(final Light light) {
		Objects.requireNonNull(light, "light == null");
		
		if(this.lights.add(light)) {
			clearAccelerationStructure();
			
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onAddLight(this, light);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds {@code primitive} to this {@code Scene} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to add
	 * @return {@code true} if, and only if, {@code primitive} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean addPrimitive(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		if(this.primitives.add(primitive)) {
			primitive.addPrimitiveObserver(this.primitiveObserver);
			
			clearAccelerationStructure();
			
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onAddPrimitive(this, primitive);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds {@code sceneObserver} to this {@code Scene} instance.
	 * <p>
	 * Returns {@code true} if, and only if, {@code sceneObserver} was added, {@code false} otherwise.
	 * <p>
	 * If {@code sceneObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sceneObserver the {@link SceneObserver} instance to add
	 * @return {@code true} if, and only if, {@code sceneObserver} was added, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code sceneObserver} is {@code null}
	 */
	public boolean addSceneObserver(final SceneObserver sceneObserver) {
		return this.sceneObservers.add(Objects.requireNonNull(sceneObserver, "sceneObserver == null"));
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code intersection} is visible to {@code light} given {@code lightSample}, {@code false} otherwise.
	 * <p>
	 * If either {@code intersection}, {@code light} or {@code lightSample} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intersection an {@link Intersection} instance
	 * @param light a {@link Light} instance
	 * @param lightSample a {@link LightSample} instance
	 * @return {@code true} if, and only if, {@code intersection} is visible to {@code light} given {@code lightSample}, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, either {@code intersection}, {@code light} or {@code lightSample} are {@code null}
	 */
	public boolean checkLightVisibility(final Intersection intersection, final Light light, final LightSample lightSample) {
		Objects.requireNonNull(intersection, "intersection == null");
		Objects.requireNonNull(light, "light == null");
		Objects.requireNonNull(lightSample, "lightSample == null");
		
		final Point3F point = lightSample.getPoint();
		final Point3F surfaceIntersectionPoint = intersection.getSurfaceIntersectionPoint();
		
		final Ray3F ray = intersection.createRay(point);
		
		final float tMinimum = 0.001F;
		final float tMaximum = abs(Point3F.distance(surfaceIntersectionPoint, point)) + 0.001F;
		
		if(light instanceof AreaLight) {
			final Optional<Intersection> optionalIntersection = intersection(ray, tMinimum, tMaximum);
			
			if(optionalIntersection.isPresent()) {
				return optionalIntersection.get().getPrimitive().getAreaLight().orElse(null) == light;
			}
			
			return true;
		}
		
		return !intersects(ray, tMinimum, tMaximum);
	}
	
	/**
	 * Compares {@code object} to this {@code Scene} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Scene}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Scene} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Scene}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Scene)) {
			return false;
		} else if(!Objects.equals(this.bVHNode, Scene.class.cast(object).bVHNode)) {
			return false;
		} else if(!Objects.equals(this.camera, Scene.class.cast(object).camera)) {
			return false;
		} else if(!Objects.equals(this.lights, Scene.class.cast(object).lights)) {
			return false;
		} else if(!Objects.equals(this.primitives, Scene.class.cast(object).primitives)) {
			return false;
		} else if(!Objects.equals(this.primitivesExternalToBVH, Scene.class.cast(object).primitivesExternalToBVH)) {
			return false;
		} else if(!Objects.equals(this.sceneObservers, Scene.class.cast(object).sceneObservers)) {
			return false;
		} else if(!Objects.equals(this.name, Scene.class.cast(object).name)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, {@code ray} intersects any {@link Primitive} instance in this {@code Scene} instance, {@code false} otherwise.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} in world space to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code true} if, and only if, {@code ray} intersects any {@code Primitive} instance in this {@code Scene} instance, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				if(primitive.intersects(ray, tMinimum, tMaximum)) {
					return true;
				}
			}
			
			return bVHNode.intersects(ray, tMinimum, tMaximum);
		}
		
		for(final Primitive primitive : this.primitives) {
			if(primitive.intersects(ray, tMinimum, tMaximum)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Removes {@code light} from this {@code Scene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code light} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code light} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param light the {@link Light} instance to remove
	 * @return {@code true} if, and only if, {@code light} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code light} is {@code null}
	 */
	public boolean removeLight(final Light light) {
		Objects.requireNonNull(light, "light == null");
		
		if(this.lights.remove(light)) {
			clearAccelerationStructure();
			
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onRemoveLight(this, light);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code primitive} from this {@code Scene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code primitive} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitive the {@link Primitive} instance to remove
	 * @return {@code true} if, and only if, {@code primitive} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code primitive} is {@code null}
	 */
	public boolean removePrimitive(final Primitive primitive) {
		Objects.requireNonNull(primitive, "primitive == null");
		
		if(this.primitives.remove(primitive)) {
			primitive.removePrimitiveObserver(this.primitiveObserver);
			
			clearAccelerationStructure();
			
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onRemovePrimitive(this, primitive);
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Removes {@code sceneObserver} from this {@code Scene} instance, if present.
	 * <p>
	 * Returns {@code true} if, and only if, {@code sceneObserver} was removed, {@code false} otherwise.
	 * <p>
	 * If {@code sceneObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sceneObserver the {@link SceneObserver} instance to remove
	 * @return {@code true} if, and only if, {@code sceneObserver} was removed, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code sceneObserver} is {@code null}
	 */
	public boolean removeSceneObserver(final SceneObserver sceneObserver) {
		return this.sceneObservers.remove(Objects.requireNonNull(sceneObserver, "sceneObserver == null"));
	}
	
	/**
	 * Performs an intersection test between {@code ray} and this {@code Scene} instance.
	 * <p>
	 * Returns {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists.
	 * <p>
	 * If {@code ray} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param ray the {@link Ray3F} to perform an intersection test against this {@code Scene} instance
	 * @param tMinimum the minimum parametric distance
	 * @param tMaximum the maximum parametric distance
	 * @return {@code t}, the parametric distance to the surface intersection point, or {@code Float.NaN} if no intersection exists
	 * @throws NullPointerException thrown if, and only if, {@code ray} is {@code null}
	 */
	public float intersectionT(final Ray3F ray, final float tMinimum, final float tMaximum) {
		float t = Float.NaN;
		float tMax = tMaximum;
		float tMin = tMinimum;
		
		final BVHNode bVHNode = this.bVHNode;
		
		if(bVHNode != null) {
			for(final Primitive primitive : this.primitivesExternalToBVH) {
				t = minOrNaN(t, primitive.intersectionT(ray, tMin, tMax));
				
				if(!isNaN(t)) {
					tMax = t;
				}
			}
			
			t = minOrNaN(t, bVHNode.intersectionT(ray, new float[] {tMin, tMax}));
			
			return t;
		}
		
		for(final Primitive primitive : this.primitives) {
			t = minOrNaN(t, primitive.intersectionT(ray, tMin, tMax));
			
			if(!isNaN(t)) {
				tMax = t;
			}
		}
		
		return t;
	}
	
	/**
	 * Returns the {@link Light} count in this {@code Scene} instance.
	 * 
	 * @return the {@code Light} count in this {@code Scene} instance
	 */
	public int getLightCount() {
		return this.lights.size();
	}
	
	/**
	 * Returns a hash code for this {@code Scene} instance.
	 * 
	 * @return a hash code for this {@code Scene} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.bVHNode, this.camera, this.lights, this.primitives, this.primitivesExternalToBVH, this.name);
	}
	
	/**
	 * Builds an acceleration structure for this {@code Scene} instance.
	 */
	public void buildAccelerationStructure() {
		final List<Primitive> primitives = this.primitives;
		final List<Primitive> primitivesExternalToBVH = new ArrayList<>();
		
		this.bVHNode = doCreateBVHNode(primitives, primitivesExternalToBVH);
		this.primitivesExternalToBVH.clear();
		this.primitivesExternalToBVH.addAll(primitivesExternalToBVH);
	}
	
	/**
	 * Clears the acceleration structure for this {@code Scene} instance.
	 */
	public void clearAccelerationStructure() {
		this.bVHNode = null;
		this.primitivesExternalToBVH.clear();
	}
	
	/**
	 * Sets the {@link Camera} instance associated with this {@code Scene} instance to {@code camera}.
	 * <p>
	 * If {@code camera} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param camera the {@code Camera} instance associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code camera} is {@code null}
	 */
	public void setCamera(final Camera camera) {
		Objects.requireNonNull(camera, "camera == null");
		
		final Camera oldCamera = this.camera;
		final Camera newCamera =      camera;
		
		this.camera.removeCameraObserver(this.cameraObserver);
		this.camera = camera;
		this.camera.addCameraObserver(this.cameraObserver);
		
		for(final SceneObserver sceneObserver : this.sceneObservers) {
			sceneObserver.onChangeCamera(this, oldCamera, newCamera);
		}
	}
	
	/**
	 * Sets the {@code List} with all {@link Light} instances associated with this {@code Scene} instance to a copy of {@code lights}.
	 * <p>
	 * If either {@code lights} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param lights a {@code List} with all {@code Light} instances associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code lights} or at least one of its elements are {@code null}
	 */
	public void setLights(final List<Light> lights) {
		ParameterArguments.requireNonNullList(lights, "lights");
		
		for(final Light light : getLights()) {
			removeLight(light);
		}
		
		for(final Light light : lights) {
			addLight(light);
		}
	}
	
	/**
	 * Sets the name associated with this {@code Scene} instance to {@code name}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public void setName(final String name) {
		Objects.requireNonNull(name, "name == null");
		
		if(!Objects.equals(this.name, name)) {
			final String oldName = this.name;
			final String newName =      name;
			
			this.name = name;
			
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onChangeName(this, oldName, newName);
			}
		}
	}
	
	/**
	 * Sets the {@code List} with all {@link Primitive} instances associated with this {@code Scene} instance to a copy of {@code primitives}.
	 * <p>
	 * If either {@code primitives} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param primitives a {@code List} with all {@code Primitive} instances associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code primitives} or at least one of its elements are {@code null}
	 */
	public void setPrimitives(final List<Primitive> primitives) {
		ParameterArguments.requireNonNullList(primitives, "primitives");
		
		for(final Primitive primitive : getPrimitives()) {
			removePrimitive(primitive);
		}
		
		for(final Primitive primitive : primitives) {
			addPrimitive(primitive);
		}
	}
	
	/**
	 * Sets the {@link Sampler} instance associated with this {@code Scene} instance to {@code sampler}.
	 * <p>
	 * If {@code sampler} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sampler the {@code Sampler} instance associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, {@code sampler} is {@code null}
	 */
	public void setSampler(final Sampler sampler) {
		this.sampler = Objects.requireNonNull(sampler, "sampler == null");
	}
	
	/**
	 * Sets the {@code List} with all {@link SceneObserver} instances associated with this {@code Scene} instance to a copy of {@code sceneObservers}.
	 * <p>
	 * If either {@code sceneObservers} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sceneObservers a {@code List} with all {@code SceneObserver} instances associated with this {@code Scene} instance
	 * @throws NullPointerException thrown if, and only if, either {@code sceneObservers} or at least one of its elements are {@code null}
	 */
	public void setSceneObservers(final List<SceneObserver> sceneObservers) {
		this.sceneObservers.clear();
		this.sceneObservers.addAll(ParameterArguments.requireNonNullList(sceneObservers, "sceneObservers"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doComputeSpecularReflection(final float tMinimum, final float tMaximum, final Sampler sampler, final boolean isPreviewMode, final int maximumBounce, final int currentBounce, final BSDF bSDF, final Intersection intersection) {
		final Vector3F normal = intersection.getSurfaceNormalS();
		
		final Sample2F sample = sampler.sample2();
		
		final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.SPECULAR_REFLECTION, new Point2F(sample.getU(), sample.getV()));
		
		if(optionalBSDFResult.isPresent()) {
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDFResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
			final float incomingDotNormalAbs = abs(incomingDotNormal);
			
			if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F && incomingDotNormalAbs > 0.0F) {
				return Color3F.addMultiplyAndDivide(Color3F.BLACK, result, doRadianceRayTracer(intersection.createRay(incoming), tMinimum, tMaximum, isPreviewMode, maximumBounce, currentBounce + 1), incomingDotNormalAbs, probabilityDensityFunctionValue);
			}
		}
		
		return Color3F.BLACK;
	}
	
	private Color3F doComputeSpecularTransmission(final float tMinimum, final float tMaximum, final Sampler sampler, final boolean isPreviewMode, final int maximumBounce, final int currentBounce, final BSDF bSDF, final Intersection intersection) {
		final Vector3F normal = intersection.getSurfaceNormalS();
		
		final Sample2F sample = sampler.sample2();
		
		final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(BXDFType.SPECULAR_TRANSMISSION, new Point2F(sample.getU(), sample.getV()));
		
		if(optionalBSDFResult.isPresent()) {
			final BSDFResult bSDFResult = optionalBSDFResult.get();
			
			final Color3F result = bSDFResult.getResult();
			
			final Vector3F incoming = bSDFResult.getIncoming();
			
			final float probabilityDensityFunctionValue = bSDFResult.getProbabilityDensityFunctionValue();
			
			final float incomingDotNormal = Vector3F.dotProduct(incoming, normal);
			final float incomingDotNormalAbs = abs(incomingDotNormal);
			
			if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F && incomingDotNormalAbs > 0.0F) {
				return Color3F.addMultiplyAndDivide(Color3F.BLACK, result, doRadianceRayTracer(intersection.createRay(incoming), tMinimum, tMaximum, isPreviewMode, maximumBounce, currentBounce + 1), incomingDotNormalAbs, probabilityDensityFunctionValue);
			}
		}
		
		return Color3F.BLACK;
	}
	
	private Color3F doEstimateDirectLight(final BSDF bSDF, final Intersection intersection, final Light light, final Point2F sampleA, final Point2F sampleB, final boolean isSpecular) {
		Color3F lightDirect = Color3F.BLACK;
		
		if(light.isUsingDeltaDistribution()) {
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
			final Optional<LightSample> optionalLightSample = light.sampleRadianceIncoming(intersection, sampleA);
			
			final Vector3F normal = intersection.getSurfaceNormalS();
			
			if(optionalLightSample.isPresent()) {
				final LightSample lightSample = optionalLightSample.get();
				
				final Color3F lightIncoming = lightSample.getResult();
				
				final Vector3F incoming = lightSample.getIncoming();
				
				final float lightPDFValue = lightSample.getProbabilityDensityFunctionValue();
				
				if(!lightIncoming.isBlack() && lightPDFValue > 0.0F) {
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, incoming), abs(Vector3F.dotProduct(incoming, normal)));
					
					if(!scatteringResult.isBlack() && checkLightVisibility(intersection, light, lightSample)) {
						lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, lightPDFValue);
					}
				}
			}
		} else {
			final BXDFType bXDFType = isSpecular ? BXDFType.ALL : BXDFType.ALL_EXCEPT_SPECULAR;
			
			final Optional<LightSample> optionalLightSample = light.sampleRadianceIncoming(intersection, sampleA);
			
			final Vector3F normal = intersection.getSurfaceNormalS();
			
			if(optionalLightSample.isPresent()) {
				final LightSample lightSample = optionalLightSample.get();
				
				final Color3F lightIncoming = lightSample.getResult();
				
				final Vector3F incoming = lightSample.getIncoming();
				
				final float lightPDFValue = lightSample.getProbabilityDensityFunctionValue();
				
				if(!lightIncoming.isBlack() && lightPDFValue > 0.0F) {
					final Color3F scatteringResult = Color3F.multiply(bSDF.evaluateDistributionFunction(bXDFType, incoming), abs(Vector3F.dotProduct(incoming, normal)));
					
					final float scatteringPDFValue = bSDF.evaluateProbabilityDensityFunction(bXDFType, incoming);
					
					if(!scatteringResult.isBlack() && checkLightVisibility(intersection, light, lightSample)) {
						final float weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(lightPDFValue, scatteringPDFValue, 1, 1);
						
						lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, weight, lightPDFValue);
					}
				}
			}
			
			final Optional<BSDFResult> optionalBSDFResult = bSDF.sampleDistributionFunction(bXDFType, sampleB);
			
			if(optionalBSDFResult.isPresent()) {
				final BSDFResult bSDFResult = optionalBSDFResult.get();
				
				final Vector3F incoming = bSDFResult.getIncoming();
				
				final Color3F scatteringResult = Color3F.multiply(bSDFResult.getResult(), abs(Vector3F.dotProduct(incoming, normal)));
				
				final boolean hasSampledSpecular = bSDFResult.getBXDFType().isSpecular();
				
				final float scatteringPDFValue = bSDFResult.getProbabilityDensityFunctionValue();
				
				if(!scatteringResult.isBlack() && scatteringPDFValue > 0.0F) {
					float weight = 1.0F;
					
					if(!hasSampledSpecular) {
						final float lightPDFValue = light.evaluateProbabilityDensityFunctionRadianceIncoming(intersection, incoming);
						
						if(isZero(lightPDFValue)) {
							return lightDirect;
						}
						
						weight = SampleGeneratorF.multipleImportanceSamplingPowerHeuristic(scatteringPDFValue, lightPDFValue, 1, 1);
					}
					
					final Ray3F ray = intersection.createRay(incoming);
					
					final Color3F transmittance = Color3F.WHITE;
					
					final Optional<Intersection> optionalIntersectionLight = intersection(ray, 0.001F, MAX_VALUE);
					
					if(optionalIntersectionLight.isPresent()) {
						final Intersection intersectionLight = optionalIntersectionLight.get();
						
						if(intersectionLight.getPrimitive().getAreaLight().orElse(null) == light) {
							final Color3F lightIncoming = intersectionLight.evaluateRadianceEmitted(Vector3F.negate(incoming));
							
							if(!lightIncoming.isBlack()) {
								lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, transmittance, weight, scatteringPDFValue);
							}
						}
					} else {
						final Color3F lightIncoming = light.evaluateRadianceEmitted(ray);
						
						if(!lightIncoming.isBlack()) {
							lightDirect = Color3F.addMultiplyAndDivide(lightDirect, scatteringResult, lightIncoming, transmittance, weight, scatteringPDFValue);
						}
					}
				}
			}
		}
		
		return lightDirect;
	}
	
	private Color3F doRadianceRayTracer(final Ray3F ray, final float tMinimum, final float tMaximum, final boolean isPreviewMode, final int maximumBounce, final int currentBounce) {
		Color3F radiance = Color3F.BLACK;
		
		final Sampler sampler = getSampler();
		
		final Optional<Intersection> optionalIntersection = intersection(ray, tMinimum, tMaximum);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final Optional<BSDF> optionalBSDF = material.computeBSDF(intersection, TransportMode.RADIANCE, true);
			
			if(!optionalBSDF.isPresent()) {
				return doRadianceRayTracer(intersection.createRay(ray.getDirection()), tMinimum, tMaximum, isPreviewMode, maximumBounce, currentBounce);
			}
			
			final BSDF bSDF = optionalBSDF.get();
			
			final Vector3F normal = intersection.getSurfaceNormalS();
			final Vector3F outgoing = Vector3F.negate(ray.getDirection());
			
			radiance = Color3F.add(radiance, intersection.evaluateRadianceEmitted(outgoing));
			
			for(final Light light : this.lights) {
				final Sample2F sample = sampler.sample2();
				
				final Optional<LightSample> optionalLightSample = light.sampleRadianceIncoming(intersection, new Point2F(sample.getU(), sample.getV()));
				
				if(optionalLightSample.isPresent()) {
					final LightSample lightSample = optionalLightSample.get();
					
					final Color3F result = lightSample.getResult();
					
					final Vector3F incoming = lightSample.getIncoming();
					
					final float probabilityDensityFunctionValue = lightSample.getProbabilityDensityFunctionValue();
					
					if(!result.isBlack() && probabilityDensityFunctionValue > 0.0F) {
						final Color3F scatteringResult = bSDF.evaluateDistributionFunction(BXDFType.ALL, incoming);
						
						if(!scatteringResult.isBlack() && checkLightVisibility(intersection, light, lightSample)) {
							radiance = Color3F.addMultiplyAndDivide(radiance, scatteringResult, result, abs(Vector3F.dotProduct(incoming, normal)), probabilityDensityFunctionValue);
						}
					}
				}
			}
			
			if(currentBounce + 1 < maximumBounce) {
				radiance = Color3F.add(radiance, doComputeSpecularReflection(tMinimum, tMaximum, sampler, isPreviewMode, maximumBounce, currentBounce, bSDF, intersection));
				radiance = Color3F.add(radiance, doComputeSpecularTransmission(tMinimum, tMaximum, sampler, isPreviewMode, maximumBounce, currentBounce, bSDF, intersection));
			}
		} else {
			for(final Light light : this.lights) {
				radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
			}
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BVHNode doCreateBVHNode(final List<LeafBVHNode> processableLeafBVHNodes, final Point3F maximum, final Point3F minimum, final int depth) {
		final int size = processableLeafBVHNodes.size();
		final int sizeHalf = size / 2;
		
		if(size < 4) {
			final List<Primitive> primitives = new ArrayList<>();
			
			for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
				for(final Primitive primitive : processableLeafBVHNode.getPrimitives()) {
					primitives.add(primitive);
				}
			}
			
			return new LeafBVHNode(maximum, minimum, depth, primitives);
		}
		
		final float sideX = maximum.getX() - minimum.getX();
		final float sideY = maximum.getY() - minimum.getY();
		final float sideZ = maximum.getZ() - minimum.getZ();
		
		float minimumCost = size * (sideX * sideY + sideY * sideZ + sideZ * sideX);
		float bestSplit = MAX_VALUE;
		
		int bestAxis = -1;
		
		for(int axis = 0; axis < 3; axis++) {
			final float start = minimum.getComponent(axis);
			final float stop  = maximum.getComponent(axis);
			
			if(abs(stop - start) < 1.0e-4F) {
				continue;
			}
			
			final float step = (stop - start) / (1024.0F / (depth + 1.0F));
			
			for(float oldSplit = 0.0F, newSplit = start + step; newSplit < stop - step; oldSplit = newSplit, newSplit += step) {
//				The following test prevents an infinite loop from occurring:
				if(equal(oldSplit, newSplit)) {
					break;
				}
				
				float maximumLX = MIN_VALUE;
				float maximumLY = MIN_VALUE;
				float maximumLZ = MIN_VALUE;
				float minimumLX = MAX_VALUE;
				float minimumLY = MAX_VALUE;
				float minimumLZ = MAX_VALUE;
				float maximumRX = MIN_VALUE;
				float maximumRY = MIN_VALUE;
				float maximumRZ = MIN_VALUE;
				float minimumRX = MAX_VALUE;
				float minimumRY = MAX_VALUE;
				float minimumRZ = MAX_VALUE;
				
				int countL = 0;
				int countR = 0;
				
				for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
					final BoundingVolume3F boundingVolume = processableLeafBVHNode.getBoundingVolume();
					
					final Point3F max = boundingVolume.getMaximum();
					final Point3F mid = boundingVolume.getMidpoint();
					final Point3F min = boundingVolume.getMinimum();
					
					final float value = mid.getComponent(axis);
					
					if(value < newSplit) {
						maximumLX = max(maximumLX, max.getX());
						maximumLY = max(maximumLY, max.getY());
						maximumLZ = max(maximumLZ, max.getZ());
						minimumLX = min(minimumLX, min.getX());
						minimumLY = min(minimumLY, min.getY());
						minimumLZ = min(minimumLZ, min.getZ());
						
						countL++;
					} else {
						maximumRX = max(maximumRX, max.getX());
						maximumRY = max(maximumRY, max.getY());
						maximumRZ = max(maximumRZ, max.getZ());
						minimumRX = min(minimumRX, min.getX());
						minimumRY = min(minimumRY, min.getY());
						minimumRZ = min(minimumRZ, min.getZ());
						
						countR++;
					}
				}
				
				if(countL <= 1 || countR <= 1) {
					continue;
				}
				
				final float sideLX = maximumLX - minimumLX;
				final float sideLY = maximumLY - minimumLY;
				final float sideLZ = maximumLZ - minimumLZ;
				final float sideRX = maximumRX - minimumRX;
				final float sideRY = maximumRY - minimumRY;
				final float sideRZ = maximumRZ - minimumRZ;
				
				final float surfaceL = sideLX * sideLY + sideLY * sideLZ + sideLZ * sideLX;
				final float surfaceR = sideRX * sideRY + sideRY * sideRZ + sideRZ * sideRX;
				
				final float cost = surfaceL * countL + surfaceR * countR;
				
				if(cost < minimumCost) {
					minimumCost = cost;
					bestSplit = newSplit;
					bestAxis = axis;
				}
			}
		}
		
		if(bestAxis == -1) {
			final List<Primitive> primitives = new ArrayList<>();
			
			for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
				for(final Primitive primitive : processableLeafBVHNode.getPrimitives()) {
					primitives.add(primitive);
				}
			}
			
			return new LeafBVHNode(maximum, minimum, depth, primitives);
		}
		
		final List<LeafBVHNode> leafBVHNodesL = new ArrayList<>(sizeHalf);
		final List<LeafBVHNode> leafBVHNodesR = new ArrayList<>(sizeHalf);
		
		float maximumLX = MIN_VALUE;
		float maximumLY = MIN_VALUE;
		float maximumLZ = MIN_VALUE;
		float minimumLX = MAX_VALUE;
		float minimumLY = MAX_VALUE;
		float minimumLZ = MAX_VALUE;
		float maximumRX = MIN_VALUE;
		float maximumRY = MIN_VALUE;
		float maximumRZ = MIN_VALUE;
		float minimumRX = MAX_VALUE;
		float minimumRY = MAX_VALUE;
		float minimumRZ = MAX_VALUE;
		
		for(final LeafBVHNode processableLeafBVHNode : processableLeafBVHNodes) {
			final BoundingVolume3F boundingVolume = processableLeafBVHNode.getBoundingVolume();
			
			final Point3F max = boundingVolume.getMaximum();
			final Point3F mid = boundingVolume.getMidpoint();
			final Point3F min = boundingVolume.getMinimum();
			
			final float value = mid.getComponent(bestAxis);
			
			if(value < bestSplit) {
				leafBVHNodesL.add(processableLeafBVHNode);
				
				maximumLX = max(maximumLX, max.getX());
				maximumLY = max(maximumLY, max.getY());
				maximumLZ = max(maximumLZ, max.getZ());
				minimumLX = min(minimumLX, min.getX());
				minimumLY = min(minimumLY, min.getY());
				minimumLZ = min(minimumLZ, min.getZ());
			} else {
				leafBVHNodesR.add(processableLeafBVHNode);
				
				maximumRX = max(maximumRX, max.getX());
				maximumRY = max(maximumRY, max.getY());
				maximumRZ = max(maximumRZ, max.getZ());
				minimumRX = min(minimumRX, min.getX());
				minimumRY = min(minimumRY, min.getY());
				minimumRZ = min(minimumRZ, min.getZ());
			}
		}
		
		final Point3F maximumL = new Point3F(maximumLX, maximumLY, maximumLZ);
		final Point3F minimumL = new Point3F(minimumLX, minimumLY, minimumLZ);
		final Point3F maximumR = new Point3F(maximumRX, maximumRY, maximumRZ);
		final Point3F minimumR = new Point3F(minimumRX, minimumRY, minimumRZ);
		
		final BVHNode bVHNodeL = doCreateBVHNode(leafBVHNodesL, maximumL, minimumL, depth + 1);
		final BVHNode bVHNodeR = doCreateBVHNode(leafBVHNodesR, maximumR, minimumR, depth + 1);
		
		return new TreeBVHNode(maximum, minimum, depth, bVHNodeL, bVHNodeR);
	}
	
	private static BVHNode doCreateBVHNode(final List<Primitive> primitives, final List<Primitive> primitivesExternalToBVH) {
		System.out.println("Generating acceleration structure...");
		
		final List<LeafBVHNode> processableLeafBVHNodes = new ArrayList<>(primitives.size());
		
		float maximumX = MIN_VALUE;
		float maximumY = MIN_VALUE;
		float maximumZ = MIN_VALUE;
		float minimumX = MAX_VALUE;
		float minimumY = MAX_VALUE;
		float minimumZ = MAX_VALUE;
		
		for(final Primitive primitive : primitives) {
			final BoundingVolume3F boundingVolume = primitive.getBoundingVolume();
			
			if(boundingVolume instanceof InfiniteBoundingVolume3F) {
				primitivesExternalToBVH.add(primitive);
				
				continue;
			}
			
			final Point3F maximum = boundingVolume.getMaximum();
			final Point3F minimum = boundingVolume.getMinimum();
			
			maximumX = max(maximumX, maximum.getX());
			maximumY = max(maximumY, maximum.getY());
			maximumZ = max(maximumZ, maximum.getZ());
			minimumX = min(minimumX, minimum.getX());
			minimumY = min(minimumY, minimum.getY());
			minimumZ = min(minimumZ, minimum.getZ());
			
			processableLeafBVHNodes.add(new LeafBVHNode(maximum, minimum, 0, Arrays.asList(primitive)));
		}
		
		final BVHNode bVHNode = doCreateBVHNode(processableLeafBVHNodes, new Point3F(maximumX, maximumY, maximumZ), new Point3F(minimumX, minimumY, minimumZ), 0);
		
		System.out.println(" - Done.");
		
		return bVHNode;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static abstract class BVHNode implements Node {
		private final BoundingVolume3F boundingVolume;
		private final int depth;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		protected BVHNode(final Point3F maximum, final Point3F minimum, final int depth) {
			this.boundingVolume = new AxisAlignedBoundingBox3F(maximum, minimum);
			this.depth = depth;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public final BoundingVolume3F getBoundingVolume() {
			return this.boundingVolume;
		}
		
		public abstract boolean intersection(final Intersector intersector);
		
		public abstract boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum);
		
		public abstract float intersectionT(final Ray3F ray, final float[] tBounds);
		
		public final int getDepth() {
			return this.depth;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class CameraObserverImpl implements CameraObserver {
		private final List<SceneObserver> sceneObservers;
		private final Scene scene;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public CameraObserverImpl(final Scene scene, final List<SceneObserver> sceneObservers) {
			this.scene = Objects.requireNonNull(scene, "scene == null");
			this.sceneObservers = ParameterArguments.requireNonNullList(sceneObservers, "sceneObservers");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onChangeApertureRadius(final Camera camera, final float oldApertureRadius, final float newApertureRadius) {
			Objects.requireNonNull(camera, "camera == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeEye(final Camera camera, final Point3F oldEye, final Point3F newEye) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldEye, "oldEye == null");
			Objects.requireNonNull(newEye, "newEye == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeFieldOfViewX(final Camera camera, final AngleF oldFieldOfViewX, final AngleF newFieldOfViewX) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldFieldOfViewX, "oldFieldOfViewX == null");
			Objects.requireNonNull(newFieldOfViewX, "newFieldOfViewX == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeFieldOfViewY(final Camera camera, final AngleF oldFieldOfViewY, final AngleF newFieldOfViewY) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldFieldOfViewY, "oldFieldOfViewY == null");
			Objects.requireNonNull(newFieldOfViewY, "newFieldOfViewY == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeFocalDistance(final Camera camera, final float oldFocalDistance, final float newFocalDistance) {
			Objects.requireNonNull(camera, "camera == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeLens(final Camera camera, final Lens oldLens, final Lens newLens) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldLens, "oldLens == null");
			Objects.requireNonNull(newLens, "newLens == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeOrthonormalBasis(final Camera camera, final OrthonormalBasis33F oldOrthonormalBasis, final OrthonormalBasis33F newOrthonormalBasis) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldOrthonormalBasis, "oldOrthonormalBasis == null");
			Objects.requireNonNull(newOrthonormalBasis, "newOrthonormalBasis == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangePitch(final Camera camera, final AngleF oldPitch, final AngleF newPitch) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldPitch, "oldPitch == null");
			Objects.requireNonNull(newPitch, "newPitch == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeResolutionX(final Camera camera, final float oldResolutionX, final float newResolutionX) {
			Objects.requireNonNull(camera, "camera == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeResolutionY(final Camera camera, final float oldResolutionY, final float newResolutionY) {
			Objects.requireNonNull(camera, "camera == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeWalkLockEnabled(final Camera camera, final boolean oldIsWalkLockEnabled, final boolean newIsWalkLockEnabled) {
			Objects.requireNonNull(camera, "camera == null");
			
			doOnChangeCamera(camera);
		}
		
		@Override
		public void onChangeYaw(final Camera camera, final AngleF oldYaw, final AngleF newYaw) {
			Objects.requireNonNull(camera, "camera == null");
			Objects.requireNonNull(oldYaw, "oldYaw == null");
			Objects.requireNonNull(newYaw, "newYaw == null");
			
			doOnChangeCamera(camera);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private void doOnChangeCamera(final Camera oldCamera) {
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onChangeCamera(this.scene, oldCamera);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class LeafBVHNode extends BVHNode {
		private final List<Primitive> primitives;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public LeafBVHNode(final Point3F maximum, final Point3F minimum, final int depth, final List<Primitive> primitives) {
			super(maximum, minimum, depth);
			
			this.primitives = Objects.requireNonNull(primitives, "primitives == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public List<Primitive> getPrimitives() {
			return this.primitives;
		}
		
		@Override
		public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
			Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
			
			try {
				if(nodeHierarchicalVisitor.visitEnter(this)) {
					if(!getBoundingVolume().accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					for(final Primitive primitive : this.primitives) {
						if(!primitive.accept(nodeHierarchicalVisitor)) {
							return nodeHierarchicalVisitor.visitLeave(this);
						}
					}
				}
				
				return nodeHierarchicalVisitor.visitLeave(this);
			} catch(final RuntimeException e) {
				throw new NodeTraversalException(e);
			}
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof LeafBVHNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), LeafBVHNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != LeafBVHNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.primitives, LeafBVHNode.class.cast(object).primitives)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final Intersector intersector) {
			if(intersector.isIntersecting(getBoundingVolume())) {
				boolean isIntersecting = false;
				
				for(final Primitive primitive : this.primitives) {
					if(intersector.intersection(primitive)) {
						isIntersecting = true;
					}
				}
				
				return isIntersecting;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) {
				for(final Primitive primitive : this.primitives) {
					if(primitive.intersects(ray, tMinimum, tMaximum)) {
						return true;
					}
				}
			}
			
			return false;
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			float t = Float.NaN;
			
			if(getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1])) {
				for(final Primitive primitive : this.primitives) {
					t = minOrNaN(t, primitive.intersectionT(ray, tBounds[0], tBounds[1]));
					
					if(!isNaN(t)) {
						tBounds[1] = t;
					}
				}
			}
			
			return t;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.primitives);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class PrimitiveObserverImpl implements PrimitiveObserver {
		private final List<SceneObserver> sceneObservers;
		private final Scene scene;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public PrimitiveObserverImpl(final Scene scene, final List<SceneObserver> sceneObservers) {
			this.scene = Objects.requireNonNull(scene, "scene == null");
			this.sceneObservers = ParameterArguments.requireNonNullList(sceneObservers, "sceneObservers");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public void onChangeAreaLight(final Primitive primitive, final Optional<AreaLight> oldOptionalAreaLight, final Optional<AreaLight> newOptionalAreaLight) {
			Objects.requireNonNull(primitive, "primitive == null");
			Objects.requireNonNull(oldOptionalAreaLight, "oldOptionalAreaLight == null");
			Objects.requireNonNull(newOptionalAreaLight, "newOptionalAreaLight == null");
			
			doOnChangePrimitive(primitive);
		}
		
		@Override
		public void onChangeBoundingVolume(final Primitive primitive, final BoundingVolume3F oldBoundingVolume, final BoundingVolume3F newBoundingVolume) {
			Objects.requireNonNull(primitive, "primitive == null");
			Objects.requireNonNull(oldBoundingVolume, "oldBoundingVolume == null");
			Objects.requireNonNull(newBoundingVolume, "newBoundingVolume == null");
			
			doOnChangePrimitive(primitive);
		}
		
		@Override
		public void onChangeMaterial(final Primitive primitive, final Material oldMaterial, final Material newMaterial) {
			Objects.requireNonNull(primitive, "primitive == null");
			Objects.requireNonNull(oldMaterial, "oldMaterial == null");
			Objects.requireNonNull(newMaterial, "newMaterial == null");
			
			doOnChangePrimitive(primitive);
		}
		
		@Override
		public void onChangeShape(final Primitive primitive, final Shape3F oldShape, final Shape3F newShape) {
			Objects.requireNonNull(primitive, "primitive == null");
			Objects.requireNonNull(oldShape, "oldShape == null");
			Objects.requireNonNull(newShape, "newShape == null");
			
			doOnChangePrimitive(primitive);
		}
		
		@Override
		public void onChangeTransform(final Primitive primitive, final Transform oldTransform, final Transform newTransform) {
			Objects.requireNonNull(primitive, "primitive == null");
			Objects.requireNonNull(oldTransform, "oldTransform == null");
			Objects.requireNonNull(newTransform, "newTransform == null");
			
			doOnChangePrimitive(primitive);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private void doOnChangePrimitive(final Primitive oldPrimitive) {
			for(final SceneObserver sceneObserver : this.sceneObservers) {
				sceneObserver.onChangePrimitive(this.scene, oldPrimitive);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class TreeBVHNode extends BVHNode {
		private final BVHNode bVHNodeL;
		private final BVHNode bVHNodeR;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public TreeBVHNode(final Point3F maximum, final Point3F minimum, final int depth, final BVHNode bVHNodeL, final BVHNode bVHNodeR) {
			super(maximum, minimum, depth);
			
			this.bVHNodeL = Objects.requireNonNull(bVHNodeL, "bVHNodeL == null");
			this.bVHNodeR = Objects.requireNonNull(bVHNodeR, "bVHNodeR == null");
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		@Override
		public boolean accept(final NodeHierarchicalVisitor nodeHierarchicalVisitor) {
			Objects.requireNonNull(nodeHierarchicalVisitor, "nodeHierarchicalVisitor == null");
			
			try {
				if(nodeHierarchicalVisitor.visitEnter(this)) {
					if(!getBoundingVolume().accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.bVHNodeL.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
					
					if(!this.bVHNodeR.accept(nodeHierarchicalVisitor)) {
						return nodeHierarchicalVisitor.visitLeave(this);
					}
				}
				
				return nodeHierarchicalVisitor.visitLeave(this);
			} catch(final RuntimeException e) {
				throw new NodeTraversalException(e);
			}
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof TreeBVHNode)) {
				return false;
			} else if(!Objects.equals(getBoundingVolume(), TreeBVHNode.class.cast(object).getBoundingVolume())) {
				return false;
			} else if(getDepth() != TreeBVHNode.class.cast(object).getDepth()) {
				return false;
			} else if(!Objects.equals(this.bVHNodeL, TreeBVHNode.class.cast(object).bVHNodeL)) {
				return false;
			} else if(!Objects.equals(this.bVHNodeR, TreeBVHNode.class.cast(object).bVHNodeR)) {
				return false;
			} else {
				return true;
			}
		}
		
		@Override
		public boolean intersection(final Intersector intersector) {
			if(intersector.isIntersecting(getBoundingVolume())) {
				final boolean isIntersectingL = this.bVHNodeL.intersection(intersector);
				final boolean isIntersectingR = this.bVHNodeR.intersection(intersector);
				
				return isIntersectingL || isIntersectingR;
			}
			
			return false;
		}
		
		@Override
		public boolean intersects(final Ray3F ray, final float tMinimum, final float tMaximum) {
			return (getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tMinimum, tMaximum)) && (this.bVHNodeL.intersects(ray, tMinimum, tMaximum) || this.bVHNodeR.intersects(ray, tMinimum, tMaximum));
		}
		
		@Override
		public float intersectionT(final Ray3F ray, final float[] tBounds) {
			return getBoundingVolume().contains(ray.getOrigin()) || getBoundingVolume().intersects(ray, tBounds[0], tBounds[1]) ? minOrNaN(this.bVHNodeL.intersectionT(ray, tBounds), this.bVHNodeR.intersectionT(ray, tBounds)) : Float.NaN;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(getBoundingVolume(), Integer.valueOf(getDepth()), this.bVHNodeL, this.bVHNodeR);
		}
	}
}