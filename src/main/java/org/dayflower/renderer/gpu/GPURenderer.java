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
	private static final int MATERIAL_MATTE = 1;
	private static final int MATERIAL_METAL = 2;
	private static final int MATERIAL_MIRROR = 3;
	
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
		doRunPathTracingSmallPT();
//		doRunRayCasting();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("static-method")
	private int doCalculateMaterialForPrimitiveIndex(final int primitiveIndex) {
		if(primitiveIndex == 0) {
			return MATERIAL_METAL;
		} else if(primitiveIndex == 1) {
			return MATERIAL_MIRROR;
		} else if(primitiveIndex == 2) {
			return MATERIAL_MATTE;
		} else if(primitiveIndex == 3) {
			return MATERIAL_MIRROR;
		} else if(primitiveIndex == 4) {
			return MATERIAL_METAL;
		} else {
			return MATERIAL_MATTE;
		}
	}
	
	private int doCalculateReflectanceForPrimitiveIndex(final int primitiveIndex) {
		if(primitiveIndex == 0) {
			return colorRGBFloatToRGBInt(0.5F, 0.5F, 0.5F);
		} else if(primitiveIndex == 1) {
			return colorRGBFloatToRGBInt(1.0F, 0.1F, 0.1F);
		} else if(primitiveIndex == 2) {
			return colorRGBFloatToRGBInt(0.1F, 1.0F, 0.1F);
		} else if(primitiveIndex == 3) {
			return colorRGBFloatToRGBInt(0.5F, 0.5F, 0.5F);
		} else if(primitiveIndex == 4) {
			return colorRGBFloatToRGBInt(0.1F, 0.1F, 1.0F);
		} else {
			return colorRGBFloatToRGBInt(0.5F, 0.5F, 0.5F);
		}
	}
	
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
					final int primitiveIndex = (int)(super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
					
					final float directionX = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
					final float directionY = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
					final float directionZ = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
					
					float emittanceR = 0.0F;
					float emittanceG = 0.0F;
					float emittanceB = 0.0F;
					
					final int material = doCalculateMaterialForPrimitiveIndex(primitiveIndex);
					final int reflectanceRGB = doCalculateReflectanceForPrimitiveIndex(primitiveIndex);
					
					float reflectanceR = colorRGBIntToRFloat(reflectanceRGB);
					float reflectanceG = colorRGBIntToGFloat(reflectanceRGB);
					float reflectanceB = colorRGBIntToBFloat(reflectanceRGB);
					
					final float surfaceNormalX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
					final float surfaceNormalY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
					final float surfaceNormalZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
					
					final float directionDotSurfaceNormal = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
					
					final float surfaceNormalCorrectlyOrientedX = directionDotSurfaceNormal < 0.0F ? surfaceNormalX : -surfaceNormalX;
					final float surfaceNormalCorrectlyOrientedY = directionDotSurfaceNormal < 0.0F ? surfaceNormalY : -surfaceNormalY;
					final float surfaceNormalCorrectlyOrientedZ = directionDotSurfaceNormal < 0.0F ? surfaceNormalZ : -surfaceNormalZ;
					
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
					
					if(material == MATERIAL_MATTE) {
						orthonormalBasis33FSetFromW(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
						
						vector3FSetSampleHemisphereCosineDistribution2(random(), random());
						vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
						
						ray3FSetFromSurfaceIntersectionPointAndVector3F();
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					} else if(material == MATERIAL_METAL) {
						vector3FSetReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
						
						orthonormalBasis33FSetVector3F();
						
						vector3FSetSampleHemispherePowerCosineDistribution(random(), random(), 20.0F);
						vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
						
						ray3FSetFromSurfaceIntersectionPointAndVector3F();
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					} else if(material == MATERIAL_MIRROR) {
						vector3FSetReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
						
						ray3FSetFromSurfaceIntersectionPointAndVector3F();
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					}
					
					currentBounce++;
				} else {
					radianceR += throughputR * (135.0F / 255.0F);//1.0F;
					radianceG += throughputG * (206.0F / 255.0F);//1.0F;
					radianceB += throughputB * (235.0F / 255.0F);//1.0F;
					
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