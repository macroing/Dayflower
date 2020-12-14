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

import static org.dayflower.util.Floats.PI_MULTIPLIED_BY_2;

import java.lang.reflect.Field;

import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

//TODO: Add Javadocs!
public final class GPURenderer extends AbstractGPURenderer {
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
		doRunPathTracingSmallPT();
//		doRunRayCasting();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doRunPathTracingSmallPT() {
		final int maximumBounce = 20;
		final int minimumBounceRussianRoulette = 5;
		
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		float throughputR = 1.0F;
		float throughputG = 1.0F;
		float throughputB = 1.0F;
		
		if(ray3FCameraGenerate(random(), random())) {
			int currentBounce = 0;
			
			while(currentBounce < maximumBounce) {
				if(intersectionComputeShape3F()) {
					final float oldDirectionX = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
					final float oldDirectionY = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
					final float oldDirectionZ = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
					
					float emittanceR = 0.0F;
					float emittanceG = 0.0F;
					float emittanceB = 0.0F;
					
					float reflectanceR = 0.5F;
					float reflectanceG = 0.5F;
					float reflectanceB = 0.5F;
					
					final float surfaceIntersectionPointX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 0];
					final float surfaceIntersectionPointY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 1];
					final float surfaceIntersectionPointZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_SURFACE_INTERSECTION_POINT + 2];
					
					final float surfaceNormalX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
					final float surfaceNormalY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
					final float surfaceNormalZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
					
					final float oldDirectionDotSurfaceNormal = vector3FDotProduct(oldDirectionX, oldDirectionY, oldDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
					
					final float surfaceNormalCorrectlyOrientedX = oldDirectionDotSurfaceNormal < 0.0F ? surfaceNormalX : -surfaceNormalX;
					final float surfaceNormalCorrectlyOrientedY = oldDirectionDotSurfaceNormal < 0.0F ? surfaceNormalY : -surfaceNormalY;
					final float surfaceNormalCorrectlyOrientedZ = oldDirectionDotSurfaceNormal < 0.0F ? surfaceNormalZ : -surfaceNormalZ;
					
					if(currentBounce >= minimumBounceRussianRoulette) {
						final float probability = max(reflectanceR, reflectanceG, reflectanceB);
						
						if(random() >= probability) {
							filmAddColor(radianceR, radianceG, radianceB);
							
							imageBegin();
							imageRedoGammaCorrectionPBRT();
							imageEnd();
							
							return;
						}
						
						reflectanceR /= probability;
						reflectanceG /= probability;
						reflectanceB /= probability;
					}
					
					radianceR += throughputR * emittanceR;
					radianceG += throughputG * emittanceG;
					radianceB += throughputB * emittanceB;
					
//					Matte Material:
					
					vector3FSampleHemisphereCosineDistribution2(random(), random());
					
					final float sampleX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
					final float sampleY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
					final float sampleZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
					
					final float orthonormalBasisWNormalizedX = surfaceNormalCorrectlyOrientedX;
					final float orthonormalBasisWNormalizedY = surfaceNormalCorrectlyOrientedY;
					final float orthonormalBasisWNormalizedZ = surfaceNormalCorrectlyOrientedZ;
					
					vector3FGenerateOrthonormalBasis33FUNormalizedFromWNormalized(orthonormalBasisWNormalizedX, orthonormalBasisWNormalizedY, orthonormalBasisWNormalizedZ);
					
					final float orthonormalBasisUNormalizedX = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_1];
					final float orthonormalBasisUNormalizedY = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_2];
					final float orthonormalBasisUNormalizedZ = super.vector3FArray_$private$3[VECTOR_3_F_ARRAY_OFFSET_COMPONENT_3];
					
					final float orthonormalBasisVNormalizedX = orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedZ - orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedY;
					final float orthonormalBasisVNormalizedY = orthonormalBasisWNormalizedZ * orthonormalBasisUNormalizedX - orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedZ;
					final float orthonormalBasisVNormalizedZ = orthonormalBasisWNormalizedX * orthonormalBasisUNormalizedY - orthonormalBasisWNormalizedY * orthonormalBasisUNormalizedX;
					
					final float newDirectionX = orthonormalBasisUNormalizedX * sampleX + orthonormalBasisVNormalizedX * sampleY + orthonormalBasisWNormalizedX * sampleZ;
					final float newDirectionY = orthonormalBasisUNormalizedY * sampleX + orthonormalBasisVNormalizedY * sampleY + orthonormalBasisWNormalizedY * sampleZ;
					final float newDirectionZ = orthonormalBasisUNormalizedZ * sampleX + orthonormalBasisVNormalizedZ * sampleY + orthonormalBasisWNormalizedZ * sampleZ;
					final float newDirectionLengthReciprocal = vector3FLengthReciprocal(newDirectionX, newDirectionY, newDirectionZ);
					final float newDirectionNormalizedX = newDirectionX * newDirectionLengthReciprocal;
					final float newDirectionNormalizedY = newDirectionY * newDirectionLengthReciprocal;
					final float newDirectionNormalizedZ = newDirectionZ * newDirectionLengthReciprocal;
					
					final float newOriginX = surfaceIntersectionPointX + newDirectionNormalizedX * 0.001F;
					final float newOriginY = surfaceIntersectionPointY + newDirectionNormalizedY * 0.001F;
					final float newOriginZ = surfaceIntersectionPointZ + newDirectionNormalizedZ * 0.001F;
					
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 0] = newOriginX;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 1] = newOriginY;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_ORIGIN + 2] = newOriginZ;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0] = newDirectionNormalizedX;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1] = newDirectionNormalizedY;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2] = newDirectionNormalizedZ;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MINIMUM] = DEFAULT_T_MINIMUM;
					super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_T_MAXIMUM] = DEFAULT_T_MAXIMUM;
					
					throughputR *= reflectanceR;
					throughputG *= reflectanceG;
					throughputB *= reflectanceB;
					
//					Matte Material!
					
					currentBounce++;
				} else {
					radianceR += throughputR * 1.0F;
					radianceG += throughputG * 1.0F;
					radianceB += throughputB * 1.0F;
					
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
				
				final float surfaceNormalX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 0];
				final float surfaceNormalY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 1];
				final float surfaceNormalZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_G_W + 2];
				
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