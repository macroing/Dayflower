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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.normalize;
import static org.dayflower.util.Floats.saturate;

import java.util.Optional;

import org.dayflower.display.Display;
import org.dayflower.display.FileDisplay;
import org.dayflower.geometry.OrthonormalBasis33F;
import org.dayflower.geometry.Point3F;
import org.dayflower.geometry.Ray3F;
import org.dayflower.geometry.SampleGeneratorF;
import org.dayflower.geometry.SurfaceIntersection3F;
import org.dayflower.geometry.Vector3F;
import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Intersection;
import org.dayflower.scene.Scene;
import org.dayflower.scene.background.ConstantBackground;

/**
 * An {@code AmbientOcclusionCPURenderer} is a {@link Renderer} implementation that renders using Ambient Occlusion.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class AmbientOcclusionCPURenderer extends AbstractCPURenderer {
	private final float maximumDistance;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AmbientOcclusionCPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AmbientOcclusionCPURenderer(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	 * }
	 * </pre>
	 */
	public AmbientOcclusionCPURenderer() {
		this(new FileDisplay("Image.png"), new Image(800, 800), new RendererConfiguration(), new Scene(new ConstantBackground(), new Camera(), "Scene"));
	}
	
	/**
	 * Constructs a new {@code AmbientOcclusionCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new AmbientOcclusionCPURenderer(display, image, rendererConfiguration, scene, 20.0F);
	 * }
	 * </pre>
	 * 
	 * @param display the {@link Display} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}
	 */
	public AmbientOcclusionCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Scene scene) {
		this(display, image, rendererConfiguration, scene, 20.0F);
	}
	
	/**
	 * Constructs a new {@code AmbientOcclusionCPURenderer} instance.
	 * <p>
	 * If either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param display the {@link Display} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param image the {@link Image} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param scene the {@link Scene} instance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @param maximumDistance the maximum distance associated with this {@code AmbientOcclusionCPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code display}, {@code image}, {@code rendererConfiguration} or {@code scene} are {@code null}
	 */
	public AmbientOcclusionCPURenderer(final Display display, final Image image, final RendererConfiguration rendererConfiguration, final Scene scene, final float maximumDistance) {
		super(display, image, rendererConfiguration, scene, false);
		
		this.maximumDistance = maximumDistance;
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
		Color3F radiance = Color3F.BLACK;
		
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final Scene scene = getScene();
		
		final Optional<Intersection> optionalIntersection = scene.intersection(ray);
		
		if(optionalIntersection.isPresent()) {
			final Intersection intersection = optionalIntersection.get();
			
			final SurfaceIntersection3F surfaceIntersectionWorldSpace = intersection.getSurfaceIntersectionWorldSpace();
			
			final OrthonormalBasis33F orthonormalBasisGWorldSpace = surfaceIntersectionWorldSpace.getOrthonormalBasisG();
			
			final Point3F surfaceIntersectionPointWorldSpace = surfaceIntersectionWorldSpace.getSurfaceIntersectionPoint();
			
			final Vector3F surfaceNormalGWorldSpace = surfaceIntersectionWorldSpace.getSurfaceNormalG();
			
			final float maximumDistance = this.maximumDistance;
			
			final int samples = rendererConfiguration.getSamples();
			
			for(int sample = 0; sample < samples; sample++) {
				final Point3F originWorldSpace = Point3F.add(surfaceIntersectionPointWorldSpace, surfaceNormalGWorldSpace, 0.0001F);
				
				final Vector3F directionWorldSpace = Vector3F.normalize(Vector3F.transform(SampleGeneratorF.sampleHemisphereUniformDistribution(), orthonormalBasisGWorldSpace));
				
				final Ray3F rayWorldSpaceShadow = new Ray3F(originWorldSpace, directionWorldSpace);
				
				if(maximumDistance > 0.0F) {
					final Optional<Intersection> optionalIntersectionShadow = scene.intersection(rayWorldSpaceShadow);
					
					if(optionalIntersectionShadow.isPresent()) {
						final Intersection intersectionShadow = optionalIntersectionShadow.get();
						
						final float t = intersectionShadow.getSurfaceIntersectionWorldSpace().getT();
						
						radiance = Color3F.add(radiance, new Color3F(normalize(saturate(t, 0.0F, maximumDistance), 0.0F, maximumDistance)));
					} else {
						radiance = Color3F.add(radiance, Color3F.WHITE);
					}
				} else if(!scene.intersects(rayWorldSpaceShadow)) {
					radiance = Color3F.add(radiance, Color3F.WHITE);
				}
			}
			
			radiance = Color3F.multiply(radiance, PI / samples);
			radiance = Color3F.divide(radiance, PI);
		}
		
		return radiance;
	}
}