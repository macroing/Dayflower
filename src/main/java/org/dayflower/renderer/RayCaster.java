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

import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.random;

import java.util.Optional;

import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.light.PointLight;

/**
 * A {@code RayCaster} is a {@link Renderer} implementation that renders using Ray Casting.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayCaster implements Renderer {
	/**
	 * Constructs a new {@code RayCaster} instance.
	 */
	public RayCaster() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Renders {@code scene} to {@code image}.
	 * <p>
	 * If either {@code image} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * rayCaster.render(image, scene, new RendererConfiguration());
	 * }
	 * </pre>
	 * 
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @throws NullPointerException thrown if, and only if, either {@code image} or {@code scene} are {@code null}
	 */
	@Override
	public void render(final Image image, final Scene scene) {
		render(image, scene, new RendererConfiguration());
	}
	
	/**
	 * Renders {@code scene} to {@code image}.
	 * <p>
	 * If either {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@link Image} instance to render to
	 * @param scene the {@link Scene} instance to render
	 * @param rendererConfiguration the {@link RendererConfiguration} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code image}, {@code scene} or {@code rendererConfiguration} are {@code null}
	 */
	@Override
	public void render(final Image image, final Scene scene, final RendererConfiguration rendererConfiguration) {
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerImageUpdate = rendererConfiguration.getRenderPassesPerImageUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			final long currentTimeMillis1 = System.currentTimeMillis();
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final float sampleX = random();
					final float sampleY = random();
					
					final Optional<Ray3F> optionalRay = scene.getCamera().createPrimaryRay(x, y, sampleX, sampleY);
					
					if(optionalRay.isPresent()) {
						final Ray3F ray = optionalRay.get();
						
						final Color3F colorRGB = doGetRadiance(ray, scene);
						final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
						
						image.filmAddColorXYZ(x + sampleX, y + sampleY, colorXYZ);
					}
				}
			}
			
			final long currentTimeMillis2 = System.currentTimeMillis();
			final long elapsedTimeMillis = currentTimeMillis2 - currentTimeMillis1;
			
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
			
			if(renderPass == 1 || renderPass % renderPassesPerImageUpdate == 0 || renderPass == renderPasses) {
				image.filmRender(0.5F);
				image.save("./generated/Image-Ray-Caster.png");
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Color3F doGetRadiance(final Ray3F ray, final Scene scene) {
		Color3F radiance = Color3F.BLACK;
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final Primitive primitive = intersection.getPrimitive();
			
			final SurfaceIntersection3F surfaceIntersection = intersection.getSurfaceIntersectionWorldSpace();
			
			final Point3F surfaceIntersectionPoint = surfaceIntersection.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormalG = surfaceIntersection.getSurfaceNormalG();
			final Vector3F surfaceNormalS = surfaceIntersection.getSurfaceNormalS();
			
			final Color3F albedo = primitive.getTextureAlbedo().getColor(intersection);
			final Color3F ambient = new Color3F(0.05F);
			final Color3F diffuse = new Color3F(1.0F);
			final Color3F specular = new Color3F(1.0F);
			
			final float specularPower = 250.0F;
			
			radiance = Color3F.multiply(ambient, albedo);
			
			for(final Light light : scene.getLights()) {
				if(light instanceof PointLight) {
					final PointLight pointLight = PointLight.class.cast(light);
					
					final Color3F emittance = pointLight.getEmittance();
					
					final Point3F position = pointLight.getPosition();
					
					final Vector3F surfaceIntersectionPointToPosition = Vector3F.direction(surfaceIntersectionPoint, position);
					final Vector3F surfaceIntersectionPointToPositionNormalized = Vector3F.normalize(surfaceIntersectionPointToPosition);
					final Vector3F positionToSurfaceIntersectionPointNormalized = Vector3F.negate(surfaceIntersectionPointToPositionNormalized);
					final Vector3F reflectionDirectionNormalized = Vector3F.reflectionNormalized(positionToSurfaceIntersectionPointNormalized, surfaceNormalS, true);
					
					final Point3F origin = Point3F.add(surfaceIntersectionPoint, surfaceNormalG, 0.0001F);
					
					final Vector3F direction = surfaceIntersectionPointToPositionNormalized;
					
					final Ray3F shadowRay = new Ray3F(origin, direction);
					
//					final Optional<Intersection> optionalShadowIntersection = scene.intersection(shadowRay);
					
					final float t = scene.intersectionT(shadowRay);
//					final float t0 = optionalShadowIntersection.isPresent() ? optionalShadowIntersection.get().getSurfaceIntersectionWorldSpace().getT() : Float.NaN;
					
//					TODO: Find out why intersectionT(Ray3F) does not seem to work as expected. The result is different from intersection(Ray3F).
//					if(Float.compare(t, t0) != 0) {
//						System.out.println(t + "," + t0);
//					}
					
					if(isNaN(t) || t * t > surfaceIntersectionPointToPosition.lengthSquared()) {
						final float nDotL = Vector3F.dotProduct(surfaceNormalS, surfaceIntersectionPointToPositionNormalized);
						final float rDotL = Vector3F.dotProduct(reflectionDirectionNormalized, surfaceIntersectionPointToPositionNormalized);
						
						if(nDotL > 0.0F) {
							radiance = Color3F.add(radiance, Color3F.multiply(emittance, Color3F.multiply(diffuse, Color3F.multiply(albedo, max(0.0F, nDotL)))));
							radiance = Color3F.add(radiance, Color3F.multiply(emittance, Color3F.multiply(specular, pow(max(0.0F, rDotL), specularPower))));
						}
					}
				}
			}
		} else {
			radiance = scene.getBackground().radiance(ray);
		}
		
		return radiance;
	}
}