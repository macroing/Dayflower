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

import java.lang.reflect.Field;
import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Light;
import org.dayflower.scene.Primitive;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.ConstantBackground;
import org.dayflower.scene.light.PointLight;

/**
 * A {@code RayCaster} is a {@link Renderer} implementation that renders using Ray Casting.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class RayCaster extends AbstractCPURenderer {
//	TODO: Add Javadocs!
	public RayCaster() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	}
	
//	TODO: Add Javadocs!
	public RayCaster(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Scene scene) {
		super(display, image, rendererConfiguration, scene);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	protected Color3F radiance(final Ray3F ray) {
		return doGetRadiance(ray, getScene());
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
					
					final Color3F intensity = pointLight.getIntensity();
					
					final Point3F position = pointLight.getPosition();
					
					final Vector3F surfaceIntersectionPointToPosition = Vector3F.direction(surfaceIntersectionPoint, position);
					final Vector3F surfaceIntersectionPointToPositionNormalized = Vector3F.normalize(surfaceIntersectionPointToPosition);
					final Vector3F positionToSurfaceIntersectionPointNormalized = Vector3F.negate(surfaceIntersectionPointToPositionNormalized);
					final Vector3F reflectionDirectionNormalized = Vector3F.reflectionNormalized(positionToSurfaceIntersectionPointNormalized, surfaceNormalS, true);
					
					final Point3F origin = Point3F.add(surfaceIntersectionPoint, surfaceNormalG, 0.0001F);
					
					final Vector3F direction = surfaceIntersectionPointToPositionNormalized;
					
					final Ray3F shadowRay = new Ray3F(origin, direction);
					
					final float t = scene.intersectionT(shadowRay);
					
					if(isNaN(t) || t * t > surfaceIntersectionPointToPosition.lengthSquared()) {
						final float nDotL = Vector3F.dotProduct(surfaceNormalS, surfaceIntersectionPointToPositionNormalized);
						final float rDotL = Vector3F.dotProduct(reflectionDirectionNormalized, surfaceIntersectionPointToPositionNormalized);
						
						if(nDotL > 0.0F) {
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(diffuse, Color3F.multiply(albedo, max(0.0F, nDotL)))));
							radiance = Color3F.add(radiance, Color3F.multiply(intensity, Color3F.multiply(specular, pow(max(0.0F, rDotL), specularPower))));
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