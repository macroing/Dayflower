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

import java.util.List;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.Point2F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
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
		super(display, image, rendererConfiguration, scene);
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
//					radiance = Color3F.add(radiance, Color3F.multiply(throughput, new Color3F(0.2F)));
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
				radiance = Color3F.add(radiance, Color3F.multiply(throughput, scene.lightSampleOneUniformDistribution(bSDF, intersection)));
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
}