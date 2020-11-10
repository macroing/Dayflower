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
import static org.dayflower.util.Floats.fresnelDielectricSchlick;
import static org.dayflower.util.Floats.random;
import static org.dayflower.util.Floats.sqrt;

import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.sampler.RandomSampler;
import org.dayflower.sampler.Sampler;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Material;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.rayito.AshikhminShirleyMaterial;
import org.dayflower.scene.rayito.LambertianMaterial;
import org.dayflower.scene.rayito.OrenNayarMaterial;
import org.dayflower.scene.rayito.RayitoMaterial;
import org.dayflower.scene.rayito.ReflectionMaterial;
import org.dayflower.scene.rayito.RefractionMaterial;

/**
 * A {@code SmallPTRPathTracingCPURenderer} is a {@link Renderer} implementation that renders using Path Tracing.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SmallPTRPathTracingCPURenderer extends AbstractCPURenderer {
	/**
	 * Constructs a new {@code SmallPTRPathTracingCPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new SmallPTRPathTracingCPURenderer(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new RandomSampler(), new Scene());
	 * }
	 * </pre>
	 */
	public SmallPTRPathTracingCPURenderer() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new RandomSampler(), new Scene());
	}
	
	/**
	 * Constructs a new {@code SmallPTRPathTracingCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code SmallPTRPathTracingCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code SmallPTRPathTracingCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code SmallPTRPathTracingCPURenderer} instance
	 * @param sampler the {@link Sampler} instance associated with this {@code SmallPTRPathTracingCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code SmallPTRPathTracingCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration}, {@code sampler} or {@code scene} are {@code null}
	 */
	public SmallPTRPathTracingCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Sampler sampler, final Scene scene) {
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
		return doRadiance(ray, 1);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doRadiance(final Ray3F ray, final int bounce) {
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final Scene scene = getScene();
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray);
		
		if(bounce >= rendererConfiguration.getMaximumBounce()) {
			return Color3F.BLACK;
		} else if(optionalIntersection.isPresent()) {
			final Vector3F direction = ray.getDirection();
			
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final Material material = primitive.getMaterial();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Vector3F surfaceNormal = surfaceIntersection.getOrthonormalBasisS().getW();
			final Vector3F surfaceNormalCorrectlyOriented = Vector3F.dotProduct(direction, surfaceNormal) < 0.0F ? surfaceNormal : Vector3F.negate(surfaceNormal);
			
			Color3F albedo = doGetAlbedo(intersection, material);
			Color3F emittance = doGetEmittance(intersection, material);
			
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
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(surfaceIntersection.createRay(d), currentBounce)));
			} else if(material instanceof LambertianMaterial || material instanceof OrenNayarMaterial) {
				final Vector3F s = SampleGeneratorF.sampleHemisphereCosineDistribution2();
				final Vector3F w = surfaceNormalCorrectlyOriented;
				final Vector3F u = Vector3F.normalize(Vector3F.crossProduct(abs(w.getX()) > 0.1F ? Vector3F.y() : Vector3F.x(), w));
				final Vector3F v = Vector3F.crossProduct(w, u);
				final Vector3F d = Vector3F.normalize(Vector3F.add(Vector3F.multiply(u, s.getX()), Vector3F.multiply(v, s.getY()), Vector3F.multiply(w, s.getZ())));
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(surfaceIntersection.createRay(d), currentBounce)));
			} else if(material instanceof ReflectionMaterial) {
				final Vector3F d = Vector3F.reflection(direction, surfaceNormal, true);
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(surfaceIntersection.createRay(d), currentBounce)));
			} else if(material instanceof RefractionMaterial) {
				final Vector3F reflectionDirection = Vector3F.reflection(direction, surfaceNormal, true);
				
				final Ray3F reflectionRay = surfaceIntersection.createRay(reflectionDirection);
				
				final boolean isEntering = Vector3F.dotProduct(surfaceNormal, surfaceNormalCorrectlyOriented) > 0.0F;
				
				final float etaA = 1.0F;
				final float etaB = 1.5F;
				final float eta = isEntering ? etaA / etaB : etaB / etaA;
				
				final float cosTheta = Vector3F.dotProduct(direction, surfaceNormalCorrectlyOriented);
				final float cosTheta2Squared = 1.0F - eta * eta * (1.0F - cosTheta * cosTheta);
				
				if(cosTheta2Squared < 0.0F) {
					return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(reflectionRay, currentBounce)));
				}
				
				final Vector3F transmissionDirection = Vector3F.normalize(Vector3F.subtract(Vector3F.multiply(direction, eta), Vector3F.multiply(surfaceNormal, (isEntering ? 1.0F : -1.0F) * (cosTheta * eta + sqrt(cosTheta2Squared)))));
				
				final Ray3F transmissionRay = surfaceIntersection.createRay(transmissionDirection);
				
				final float a = etaB - etaA;
				final float b = etaB + etaA;
				
				final float reflectance = fresnelDielectricSchlick(isEntering ? -cosTheta : Vector3F.dotProduct(transmissionDirection, surfaceNormal), a * a / (b * b));
				final float transmittance = 1.0F - reflectance;
				
				final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
				final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
				final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
				
				if(random() < probabilityRussianRoulette) {
					return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(reflectionRay, currentBounce), probabilityRussianRouletteReflection));
				}
				
				return Color3F.add(emittance, Color3F.multiply(albedo, doRadiance(transmissionRay, currentBounce), probabilityRussianRouletteTransmission));
			} else {
				return Color3F.BLACK;
			}
		}
		
		Color3F radiance = Color3F.BLACK;
		
		for(final Light light : scene.getLights()) {
			radiance = Color3F.add(radiance, light.evaluateRadianceEmitted(ray));
		}
		
		return radiance;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetAlbedo(final Intersection intersection, final Material material) {
		return material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).evaluate(intersection).getColor() : Color3F.BLACK;
	}
	
	private static Color3F doGetEmittance(final Intersection intersection, final Material material) {
		return material instanceof RayitoMaterial ? RayitoMaterial.class.cast(material).emittance(intersection) : Color3F.BLACK;
	}
}