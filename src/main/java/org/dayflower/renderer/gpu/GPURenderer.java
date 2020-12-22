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
package org.dayflower.renderer.gpu;

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.PI_RECIPROCAL;

import java.lang.reflect.Field;

import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

//TODO: Add Javadocs!
public final class GPURenderer extends AbstractGPURenderer {
	private static final float SKY_B = 1.0F;//235.0F / 255.0F;
	private static final float SKY_G = 1.0F;//206.0F / 255.0F;
	private static final float SKY_R = 1.0F;//135.0F / 255.0F;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public GPURenderer() {
		this(new RendererConfiguration());
	}
	
//	TODO: Add Javadocs!
	public GPURenderer(final RendererConfiguration rendererConfiguration) {
		this(rendererConfiguration, new FileRendererObserver());
	}
	
//	TODO: Add Javadocs!
	public GPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		super(rendererConfiguration, rendererObserver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void run() {
//		doRunAmbientOcclusion(0.0F, 1);
		doRunPathTracingSmallPT(20, 5);
//		doRunRayCasting();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doRunAmbientOcclusion(final float maximumDistance, final int samples) {
		float radiance = 0.0F;
		
		if(ray3FCameraGenerate(random(), random()) && intersectionComputeShape3F()) {
			orthonormalBasis33FSetIntersectionOrthonormalBasisG();
			
			for(int sample = 0; sample < samples; sample++) {
				vector3FSetSampleHemisphereUniformDistribution(random(), random());
				vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
				
				ray3FSetFromSurfaceIntersectionPointAndVector3F();
				
				if(maximumDistance > 0.0F) {
					final float t = intersectionTShape3F();
					final float s = t > 0.0F ? normalize(saturateF(t, 0.0F, maximumDistance), 0.0F, maximumDistance) : 1.0F;
					
					radiance += s;
				} else if(!intersectsShape3F()) {
					radiance += 1.0F;
				}
			}
			
			radiance *= PI / samples * PI_RECIPROCAL;
		}
		
		filmAddColor(radiance, radiance, radiance);
		
		imageBegin();
		imageRedoGammaCorrectionPBRT();
		imageEnd();
	}
	
	private void doRunPathTracingSmallPT(final int maximumBounce, final int minimumBounceRussianRoulette) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		float throughputR = 1.0F;
		float throughputG = 1.0F;
		float throughputB = 1.0F;
		
		final float pixel0X = 2.0F * random();
		final float pixel1X = pixel0X < 1.0F ? sqrt(pixel0X) - 1.0F : 1.0F - sqrt(2.0F - pixel0X);
		final float pixel0Y = 2.0F * random();
		final float pixel1Y = pixel0Y < 1.0F ? sqrt(pixel0Y) - 1.0F : 1.0F - sqrt(2.0F - pixel0Y);
		
		if(ray3FCameraGenerate(pixel1X, pixel1Y)) {
			int currentBounce = 0;
			
			while(currentBounce < maximumBounce) {
				if(intersectionComputeShape3F()) {
					materialEmittance();
					
					radianceR += throughputR * color3FLHSGetComponent1();
					radianceG += throughputG * color3FLHSGetComponent2();
					radianceB += throughputB * color3FLHSGetComponent3();
					
					materialSampleDistributionFunction();
					
					throughputR *= color3FLHSGetComponent1();
					throughputG *= color3FLHSGetComponent2();
					throughputB *= color3FLHSGetComponent3();
					
					ray3FSetFromSurfaceIntersectionPointAndVector3F();
					
					if(currentBounce >= minimumBounceRussianRoulette) {
						final float probability = max(throughputR, throughputG, throughputB);
						
						if(random() > probability) {
							currentBounce = maximumBounce;
						} else {
							throughputR /= probability;
							throughputG /= probability;
							throughputB /= probability;
						}
					}
					
					currentBounce++;
				} else {
					radianceR += throughputR * SKY_R;
					radianceG += throughputG * SKY_G;
					radianceB += throughputB * SKY_B;
					
					currentBounce = maximumBounce;
				}
			}
		} else {
			radianceR = 1.0F;
			radianceG = 1.0F;
			radianceB = 1.0F;
		}
		
		filmAddColor(radianceR, radianceG, radianceB);
		
		imageBegin();
		imageRedoGammaCorrectionPBRT();
		imageEnd();
	}
	
	private void doRunRayCasting() {
		if(ray3FCameraGenerate(random(), random())) {
			if(intersectionComputeShape3F()) {
				final float rayDirectionX = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
				final float rayDirectionY = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
				final float rayDirectionZ = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
				
				final float surfaceNormalX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
				final float surfaceNormalY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
				final float surfaceNormalZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
				
				final float rayDirectionDotSurfaceNormal = vector3FDotProduct(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
				final float rayDirectionDotSurfaceNormalAbs = abs(rayDirectionDotSurfaceNormal);
				
				final float r = 0.5F * rayDirectionDotSurfaceNormalAbs;
				final float g = 0.5F * rayDirectionDotSurfaceNormalAbs;
				final float b = 0.5F * rayDirectionDotSurfaceNormalAbs;
				
				filmAddColor(r, g, b);
			} else {
				filmAddColor(0.0F, 0.0F, 0.0F);
			}
		} else {
			filmAddColor(1.0F, 1.0F, 1.0F);
		}
		
		imageBegin();
		imageRedoGammaCorrectionPBRT();
		imageEnd();
	}
}