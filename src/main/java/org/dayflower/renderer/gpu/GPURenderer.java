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
	private static final int MATERIAL_CLEAR_COAT = 1;
	private static final int MATERIAL_GLASS = 2;
	private static final int MATERIAL_MATTE = 3;
	private static final int MATERIAL_METAL = 4;
	private static final int MATERIAL_MIRROR = 5;
	
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
	
	private int doCalculateEmittanceForPrimitiveIndex(final int primitiveIndex) {
		if(primitiveIndex == 0) {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		} else if(primitiveIndex == 1) {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		} else if(primitiveIndex == 2) {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		} else if(primitiveIndex == 3) {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		} else if(primitiveIndex == 4) {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		} else {
			return colorRGBFloatToRGBInt(0.0F, 0.0F, 0.0F);
		}
	}
	
	@SuppressWarnings("static-method")
	private int doCalculateMaterialForPrimitiveIndex(final int primitiveIndex) {
		if(primitiveIndex == 0) {
			return MATERIAL_CLEAR_COAT;
		} else if(primitiveIndex == 1) {
			return MATERIAL_GLASS;
		} else if(primitiveIndex == 2) {
			return MATERIAL_MIRROR;
		} else if(primitiveIndex == 3) {
			return MATERIAL_METAL;
		} else if(primitiveIndex == 4) {
			return MATERIAL_MATTE;
		} else {
			return MATERIAL_MATTE;
		}
	}
	
	private int doCalculateReflectanceForPrimitiveIndex(final int primitiveIndex) {
		if(primitiveIndex == 0) {
			return colorRGBFloatToRGBInt(1.0F, 0.01F, 0.01F);
		} else if(primitiveIndex == 1) {
			return colorRGBFloatToRGBInt(1.0F, 1.0F, 1.0F);
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
					final float s = t > 0.0F ? normalize(saturateFloat(t, 0.0F, maximumDistance), 0.0F, maximumDistance) : 1.0F;
					
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
					final int primitiveIndex = (int)(super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_PRIMITIVE_INDEX]);
					
					final float directionX = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 0];
					final float directionY = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 1];
					final float directionZ = super.ray3FArray_$private$8[RAY_3_F_ARRAY_OFFSET_DIRECTION + 2];
					
					final int emittanceRGB = doCalculateEmittanceForPrimitiveIndex(primitiveIndex);
					final int material = doCalculateMaterialForPrimitiveIndex(primitiveIndex);
					final int reflectanceRGB = doCalculateReflectanceForPrimitiveIndex(primitiveIndex);
					
					float emittanceR = colorRGBIntToRFloat(emittanceRGB);
					float emittanceG = colorRGBIntToGFloat(emittanceRGB);
					float emittanceB = colorRGBIntToBFloat(emittanceRGB);
					
					final float reflectanceR = colorRGBIntToRFloat(reflectanceRGB);
					final float reflectanceG = colorRGBIntToGFloat(reflectanceRGB);
					final float reflectanceB = colorRGBIntToBFloat(reflectanceRGB);
					
					final float surfaceNormalX = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 0];
					final float surfaceNormalY = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 1];
					final float surfaceNormalZ = super.intersectionArray_$private$24[INTERSECTION_ARRAY_OFFSET_ORTHONORMAL_BASIS_S_W + 2];
					
					final float directionDotSurfaceNormal = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
					
					final float surfaceNormalCorrectlyOrientedX = directionDotSurfaceNormal < 0.0F ? surfaceNormalX : -surfaceNormalX;
					final float surfaceNormalCorrectlyOrientedY = directionDotSurfaceNormal < 0.0F ? surfaceNormalY : -surfaceNormalY;
					final float surfaceNormalCorrectlyOrientedZ = directionDotSurfaceNormal < 0.0F ? surfaceNormalZ : -surfaceNormalZ;
					
					radianceR += throughputR * emittanceR;
					radianceG += throughputG * emittanceG;
					radianceB += throughputB * emittanceB;
					
//					The clear coat material is almost identical to the glass material. Instead of combining specular reflection with specular transmission, it combines specular reflection with diffuse reflection.
					if(material == MATERIAL_CLEAR_COAT) {
						final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
						
						final float etaA = 1.0F;
						final float etaB = 1.5F;
						final float etaI = isEntering ? etaA : etaB;
						final float etaT = isEntering ? etaB : etaA;
						final float eta = etaI / etaT;
						
						final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
						final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
						final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
						final float cosThetaT = sqrt(sinThetaTSquared);
						
						final boolean isTotalInternalReflection = sinThetaTSquared < 0.0F;
						
						if(isTotalInternalReflection) {
							vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
						}
						
						if(!isTotalInternalReflection) {
							final float transmissionDirectionSign = isEntering ? +1.0F : -1.0F;
							final float transmissionDirectionX = directionX * eta - surfaceNormalX * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionY = directionY * eta - surfaceNormalY * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionZ = directionZ * eta - surfaceNormalZ * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionLengthReciprocal = vector3FLengthReciprocal(transmissionDirectionX, transmissionDirectionY, transmissionDirectionZ);
							final float transmissionDirectionNormalizedX = transmissionDirectionX * transmissionDirectionLengthReciprocal;
							final float transmissionDirectionNormalizedY = transmissionDirectionY * transmissionDirectionLengthReciprocal;
							final float transmissionDirectionNormalizedZ = transmissionDirectionZ * transmissionDirectionLengthReciprocal;
							
							final float cosTheta = isEntering ? -cosThetaI : vector3FDotProduct(transmissionDirectionNormalizedX, transmissionDirectionNormalizedY, transmissionDirectionNormalizedZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
							final float f0 = ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA));
							
							final float reflectance = fresnelDielectricSchlick(cosTheta, f0);
							final float transmittance = 1.0F - reflectance;
							
							final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
							final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
							final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
							
							final boolean isReflection = random() < probabilityRussianRoulette;
							final boolean isTransmission = !isReflection;
							
							if(isReflection) {
								vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
								
								throughputR *= probabilityRussianRouletteReflection;
								throughputG *= probabilityRussianRouletteReflection;
								throughputB *= probabilityRussianRouletteReflection;
							}
							
							if(isTransmission) {
								vector3FSetDiffuseReflection(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, random(), random());
								
								throughputR *= reflectanceR * probabilityRussianRouletteTransmission;
								throughputG *= reflectanceG * probabilityRussianRouletteTransmission;
								throughputB *= reflectanceB * probabilityRussianRouletteTransmission;
							}
						}
					}
					
//					The glass material combines specular reflection and specular transmission using Fresnel.
					if(material == MATERIAL_GLASS) {
						final boolean isEntering = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ) > 0.0F;
						
						final float etaA = 1.0F;
						final float etaB = 1.5F;
						final float etaI = isEntering ? etaA : etaB;
						final float etaT = isEntering ? etaB : etaA;
						final float eta = etaI / etaT;
						
						final float cosThetaI = vector3FDotProduct(directionX, directionY, directionZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
						final float sinThetaISquared = 1.0F - cosThetaI * cosThetaI;
						final float sinThetaTSquared = 1.0F - eta * eta * sinThetaISquared;
						final float cosThetaT = sqrt(sinThetaTSquared);
						
						final boolean isTotalInternalReflection = sinThetaTSquared < 0.0F;
						
						if(isTotalInternalReflection) {
							vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
							
							throughputR *= reflectanceR;
							throughputG *= reflectanceG;
							throughputB *= reflectanceB;
						}
						
						if(!isTotalInternalReflection) {
							final float transmissionDirectionSign = isEntering ? +1.0F : -1.0F;
							final float transmissionDirectionX = directionX * eta - surfaceNormalX * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionY = directionY * eta - surfaceNormalY * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionZ = directionZ * eta - surfaceNormalZ * transmissionDirectionSign * (eta * cosThetaI + cosThetaT);
							final float transmissionDirectionLengthReciprocal = vector3FLengthReciprocal(transmissionDirectionX, transmissionDirectionY, transmissionDirectionZ);
							final float transmissionDirectionNormalizedX = transmissionDirectionX * transmissionDirectionLengthReciprocal;
							final float transmissionDirectionNormalizedY = transmissionDirectionY * transmissionDirectionLengthReciprocal;
							final float transmissionDirectionNormalizedZ = transmissionDirectionZ * transmissionDirectionLengthReciprocal;
							
							final float cosTheta = isEntering ? -cosThetaI : vector3FDotProduct(transmissionDirectionNormalizedX, transmissionDirectionNormalizedY, transmissionDirectionNormalizedZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
							final float f0 = ((etaB - etaA) * (etaB - etaA)) / ((etaB + etaA) * (etaB + etaA));
							
							final float reflectance = fresnelDielectricSchlick(cosTheta, f0);
							final float transmittance = 1.0F - reflectance;
							
							final float probabilityRussianRoulette = 0.25F + 0.5F * reflectance;
							final float probabilityRussianRouletteReflection = reflectance / probabilityRussianRoulette;
							final float probabilityRussianRouletteTransmission = transmittance / (1.0F - probabilityRussianRoulette);
							
							final boolean isReflection = random() < probabilityRussianRoulette;
							final boolean isTransmission = !isReflection;
							
							if(isReflection) {
								vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
								
								throughputR *= reflectanceR * probabilityRussianRouletteReflection;
								throughputG *= reflectanceG * probabilityRussianRouletteReflection;
								throughputB *= reflectanceB * probabilityRussianRouletteReflection;
							}
							
							if(isTransmission) {
								vector3FSet(transmissionDirectionNormalizedX, transmissionDirectionNormalizedY, transmissionDirectionNormalizedZ);
								
								throughputR *= reflectanceR * probabilityRussianRouletteTransmission;
								throughputG *= reflectanceG * probabilityRussianRouletteTransmission;
								throughputB *= reflectanceB * probabilityRussianRouletteTransmission;
							}
						}
					}
					
					if(material == MATERIAL_MATTE) {
						vector3FSetDiffuseReflection(surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ, random(), random());
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					}
					
					if(material == MATERIAL_METAL) {
						vector3FSetGlossyReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true, random(), random(), 20.0F);
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					}
					
					if(material == MATERIAL_MIRROR) {
						vector3FSetSpecularReflection(directionX, directionY, directionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ, true);
						
						throughputR *= reflectanceR;
						throughputG *= reflectanceG;
						throughputB *= reflectanceB;
					}
					
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