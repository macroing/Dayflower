/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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

import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

import org.macroing.java.lang.Floats;

/**
 * A {@code GPURenderer} is an implementation of {@link AbstractGPURenderer} that supports various rendering algorithms.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GPURenderer extends AbstractGPURenderer {
	/**
	 * Constructs a new {@code GPURenderer} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GPURenderer(new FileRendererObserver());
	 * }
	 * </pre>
	 */
	public GPURenderer() {
		this(new FileRendererObserver());
	}
	
	/**
	 * Constructs a new {@code GPURenderer} instance.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code GPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	public GPURenderer(final RendererObserver rendererObserver) {
		super(rendererObserver);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Runs the rendering on the GPU or CPU.
	 */
	@Override
	public void run() {
		if(renderingAlgorithmIsAmbientOcclusion()) {
			doRunAmbientOcclusion(getMaximumDistance(), getSamples());
		} else if(renderingAlgorithmIsDepthCamera()) {
			doRunDepthCamera();
		} else if(renderingAlgorithmIsPathTracing()) {
			doRunPathTracing(getMaximumBounce(), getMinimumBounceRussianRoulette());
		} else if(renderingAlgorithmIsRayCasting()) {
			doRunRayCasting();
		} else if(renderingAlgorithmIsRayTracing()) {
			doRunRayTracing();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	void doRunAmbientOcclusion(final float maximumDistance, final int samples) {
		float radiance = 0.0F;
		
		if(ray3FCameraGenerate(random(), random()) && primitiveIntersectionComputeLHS()) {
			orthonormalBasis33FSetIntersectionOrthonormalBasisSLHS();
			
			for(int sample = 0; sample < samples; sample++) {
				vector3FSetSampleHemisphereUniformDistribution(random(), random());
				vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
				
				ray3FSetFromSurfaceIntersectionPointAndVector3FLHS();
				
				if(maximumDistance > 0.0F) {
					final float t = primitiveIntersectionTLHS();
					final float s = t > 0.0F ? normalize(saturateF(t, 0.0F, maximumDistance), 0.0F, maximumDistance) : 1.0F;
					
					radiance += s;
				} else if(!primitiveIntersects()) {
					radiance += 1.0F;
				}
			}
			
			radiance *= Floats.PI / samples * Floats.PI_RECIPROCAL;
		}
		
		filmAddColor(radiance, radiance, radiance);
		
		imageBegin();
		imageRedoGammaCorrection();
		imageEnd();
	}
	
	void doRunDepthCamera() {
		float radiance = 0.0F;
		
		if(ray3FCameraGenerate(0.5F, 0.5F) && primitiveIntersectionComputeLHS()) {
			final float eyeX = ray3FGetOriginX();
			final float eyeY = ray3FGetOriginY();
			final float eyeZ = ray3FGetOriginZ();
			
			final float lookAtX = intersectionLHSGetSurfaceIntersectionPointX();
			final float lookAtY = intersectionLHSGetSurfaceIntersectionPointY();
			final float lookAtZ = intersectionLHSGetSurfaceIntersectionPointZ();
			
			final float distanceSquared = point3FDistanceSquared(eyeX, eyeY, eyeZ, lookAtX, lookAtY, lookAtZ);
			
			final float scale = 0.25F;
			
			final float intensity = saturateF(1.0F / (distanceSquared * scale), 0.0F, 1.0F);
			
			radiance = intensity;
		}
		
		filmAddColor(radiance, radiance, radiance);
		
		imageBegin();
		imageRedoGammaCorrection();
		imageEnd();
	}
	
	void doRunPathTracing(final int maximumBounce, final int minimumBounceRussianRoulette) {
		float radianceR = 0.0F;
		float radianceG = 0.0F;
		float radianceB = 0.0F;
		
		float throughputR = 1.0F;
		float throughputG = 1.0F;
		float throughputB = 1.0F;
		
		boolean isSelected = false;
		
		/*
		 * A call to ray3FCameraGenerateTriangleFilter() will compute a ray in world space, if it returns true.
		 */
		
		if(ray3FCameraGenerateTriangleFilter()) {
			int currentBounce = 0;
			
			boolean isSpecularBounce = false;
			
			float etaScale = 1.0F;
			
			while(currentBounce < maximumBounce) {
				final float rayDirectionX = ray3FGetDirectionX();
				final float rayDirectionY = ray3FGetDirectionY();
				final float rayDirectionZ = ray3FGetDirectionZ();
				
				final float outgoingX = -rayDirectionX;
				final float outgoingY = -rayDirectionY;
				final float outgoingZ = -rayDirectionZ;
				
				/*
				 * A call to primitiveIntersectionComputeLHS() will transform the ray from world space, to object space and back to world space for each primitive.
				 */
				
				if(primitiveIntersectionComputeLHS()) {
					if(currentBounce == 0 && primitiveGetInstanceIDLHS() == super.primitiveInstanceID) {
						isSelected = true;
					}
					
					if(currentBounce == 0 || isSpecularBounce) {
						final int areaLightID = primitiveGetAreaLightIDLHS();
						final int areaLightOffset = primitiveGetAreaLightOffsetLHS();
						
						if(areaLightID != 0) {
							lightSet(areaLightID, areaLightOffset);
							
							lightEvaluateRadianceEmittedAreaLight(intersectionLHSGetOrthonormalBasisSWX(), intersectionLHSGetOrthonormalBasisSWY(), intersectionLHSGetOrthonormalBasisSWZ(), outgoingX, outgoingY, outgoingZ);
							
							radianceR += throughputR * color3FLHSGetR();
							radianceG += throughputG * color3FLHSGetG();
							radianceB += throughputB * color3FLHSGetB();
						} else {
							materialEmittance(primitiveGetMaterialIDLHS(), primitiveGetMaterialOffsetLHS(), outgoingX, outgoingY, outgoingZ);
							
							radianceR += throughputR * color3FLHSGetR();
							radianceG += throughputG * color3FLHSGetG();
							radianceB += throughputB * color3FLHSGetB();
						}
					}
					
					if(materialBSDFCompute(primitiveGetMaterialIDLHS(), primitiveGetMaterialOffsetLHS(), rayDirectionX, rayDirectionY, rayDirectionZ)) {
						if(materialBSDFCountBXDFsBySpecularType(false) > 0) {
							/*
							 * A call to lightSampleOneLightUniformDistribution() will start of with a ray in world space and end up with the same ray in world space.
							 */
							
							lightSampleOneLightUniformDistribution();
							
							radianceR += throughputR * color3FLHSGetR();
							radianceG += throughputG * color3FLHSGetG();
							radianceB += throughputB * color3FLHSGetB();
						}
						
						if(materialBSDFSampleDistributionFunction(B_X_D_F_TYPE_BIT_FLAG_ALL, random(), random(), rayDirectionX, rayDirectionY, rayDirectionZ)) {
							final float incomingX = materialBSDFResultGetIncomingX();
							final float incomingY = materialBSDFResultGetIncomingY();
							final float incomingZ = materialBSDFResultGetIncomingZ();
							
							final float probabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
							
							final float resultR = materialBSDFResultGetResultR();
							final float resultG = materialBSDFResultGetResultG();
							final float resultB = materialBSDFResultGetResultB();
							
							final float surfaceNormalGX = intersectionLHSGetOrthonormalBasisGWX();
							final float surfaceNormalGY = intersectionLHSGetOrthonormalBasisGWY();
							final float surfaceNormalGZ = intersectionLHSGetOrthonormalBasisGWZ();
							
							final float surfaceNormalSX = intersectionLHSGetOrthonormalBasisSWX();
							final float surfaceNormalSY = intersectionLHSGetOrthonormalBasisSWY();
							final float surfaceNormalSZ = intersectionLHSGetOrthonormalBasisSWZ();
							
							final float incomingDotSurfaceNormalS = vector3FDotProduct(incomingX, incomingY, incomingZ, surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
							final float incomingDotSurfaceNormalSAbs = abs(incomingDotSurfaceNormalS);
							
							final boolean isProbabilityDensityFunctionValueValid = checkIsFinite(probabilityDensityFunctionValue) && probabilityDensityFunctionValue > 0.0F;
							
							if(isProbabilityDensityFunctionValueValid) {
								throughputR *= resultR * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
								throughputG *= resultG * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
								throughputB *= resultB * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
							}
							
							isSpecularBounce = materialBSDFResultBXDFIsSpecular();
							
							if(isSpecularBounce && materialBSDFResultBXDFHasTransmission()) {
								final float eta = materialBSDFGetEta();
								
								final float outgoingDotSurfaceNormalG = vector3FDotProduct(outgoingX, outgoingY, outgoingZ, surfaceNormalGX, surfaceNormalGY, surfaceNormalGZ);
								
								etaScale *= outgoingDotSurfaceNormalG > 0.0F ? eta * eta : 1.0F / (eta * eta);
							}
							
							vector3FSet(incomingX, incomingY, incomingZ);
							
							ray3FSetFromSurfaceIntersectionPointAndVector3FLHS();
							
							final float russianRouletteThroughputR = throughputR * etaScale;
							final float russianRouletteThroughputG = throughputG * etaScale;
							final float russianRouletteThroughputB = throughputB * etaScale;
							
							final float russianRouletteThroughputMaximum = max(russianRouletteThroughputR, russianRouletteThroughputG, russianRouletteThroughputB);
							
							if(isProbabilityDensityFunctionValueValid && currentBounce >= minimumBounceRussianRoulette && russianRouletteThroughputMaximum < 1.0F) {
								final float probability = max(0.05F, 1.0F - russianRouletteThroughputMaximum);
								final float probabilityReciprocal = 1.0F / (1.0F - probability);
								
								if(random() < probability) {
									currentBounce = maximumBounce;
								} else {
									throughputR *= probabilityReciprocal;
									throughputG *= probabilityReciprocal;
									throughputB *= probabilityReciprocal;
								}
							}
							
							currentBounce = isProbabilityDensityFunctionValueValid ? currentBounce + 1 : maximumBounce;
						} else {
							currentBounce = maximumBounce;
						}
					} else {
						currentBounce = maximumBounce;
					}
				} else if(currentBounce == 0 || isSpecularBounce) {
					lightEvaluateRadianceEmittedAll(rayDirectionX, rayDirectionY, rayDirectionZ);
					
					radianceR += throughputR * color3FLHSGetR();
					radianceG += throughputG * color3FLHSGetG();
					radianceB += throughputB * color3FLHSGetB();
					
					currentBounce = maximumBounce;
				} else {
					currentBounce = maximumBounce;
				}
			}
		} else {
			radianceR = 1.0F;
			radianceG = 1.0F;
			radianceB = 1.0F;
		}
		
		if(isSelected) {
			radianceR = 0.0F;
			radianceG = 1.0F;
			radianceB = 0.0F;
		}
		
		filmAddColor(radianceR, radianceG, radianceB);
		
		imageBegin();
		
		if(toneMapperIsFilmicCurveACESModifiedVersion1()) {
			imageToneMapFilmicCurveACESModifiedVersion1(1.0F);
		}
		
		if(toneMapperIsReinhard()) {
			imageToneMapReinhard(1.0F);
		}
		
		if(toneMapperIsReinhardModifiedVersion1()) {
			imageToneMapReinhardModifiedVersion1(1.0F);
		}
		
		if(toneMapperIsReinhardModifiedVersion2()) {
			imageToneMapReinhardModifiedVersion2(1.0F);
		}
		
		if(toneMapperIsUnreal3()) {
			imageToneMapUnreal3(1.0F);
		}
		
		imageRedoGammaCorrection();
		imageEnd();
	}
	
	void doRunRayCasting() {
		if(ray3FCameraGenerate(random(), random())) {
			if(primitiveIntersectionComputeLHS()) {
				final boolean isSelected = primitiveGetInstanceIDLHS() == super.primitiveInstanceID;
				
				final float rayDirectionX = ray3FGetDirectionX();
				final float rayDirectionY = ray3FGetDirectionY();
				final float rayDirectionZ = ray3FGetDirectionZ();
				
				final float surfaceNormalX = intersectionLHSGetOrthonormalBasisSWX();
				final float surfaceNormalY = intersectionLHSGetOrthonormalBasisSWY();
				final float surfaceNormalZ = intersectionLHSGetOrthonormalBasisSWZ();
				
				final float rayDirectionDotSurfaceNormal = vector3FDotProduct(rayDirectionX, rayDirectionY, rayDirectionZ, surfaceNormalX, surfaceNormalY, surfaceNormalZ);
				final float rayDirectionDotSurfaceNormalAbs = abs(rayDirectionDotSurfaceNormal);
				
				final float r = isSelected ? 0.0F : 0.5F * rayDirectionDotSurfaceNormalAbs;
				final float g = isSelected ? 1.0F : 0.5F * rayDirectionDotSurfaceNormalAbs;
				final float b = isSelected ? 0.0F : 0.5F * rayDirectionDotSurfaceNormalAbs;
				
				filmAddColor(r, g, b);
			} else {
				filmAddColor(0.0F, 0.0F, 0.0F);
			}
		} else {
			filmAddColor(1.0F, 1.0F, 1.0F);
		}
		
		imageBegin();
		imageRedoGammaCorrection();
		imageEnd();
	}
	
	void doRunRayTracing() {
		if(ray3FCameraGenerateTriangleFilter()) {
			if(primitiveIntersectionComputeLHS() && materialBSDFCompute(primitiveGetMaterialIDLHS(), primitiveGetMaterialOffsetLHS(), ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ()) && materialBSDFSampleDistributionFunction(B_X_D_F_TYPE_BIT_FLAG_ALL, random(), random(), ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ())) {
				final boolean isSelected = primitiveGetInstanceIDLHS() == super.primitiveInstanceID;
				
				final float incomingX = materialBSDFResultGetIncomingX();
				final float incomingY = materialBSDFResultGetIncomingY();
				final float incomingZ = materialBSDFResultGetIncomingZ();
				
				final float probabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
				
				final float resultR = materialBSDFResultGetResultR();
				final float resultG = materialBSDFResultGetResultG();
				final float resultB = materialBSDFResultGetResultB();
				
				final float surfaceNormalX = intersectionLHSGetOrthonormalBasisSWX();
				final float surfaceNormalY = intersectionLHSGetOrthonormalBasisSWY();
				final float surfaceNormalZ = intersectionLHSGetOrthonormalBasisSWZ();
				final float surfaceNormalDotRayDirection = vector3FDotProduct(surfaceNormalX, surfaceNormalY, surfaceNormalZ, ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ());
				final float surfaceNormalCorrectlyOrientedX = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalX : surfaceNormalX;
				final float surfaceNormalCorrectlyOrientedY = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalY : surfaceNormalY;
				final float surfaceNormalCorrectlyOrientedZ = surfaceNormalDotRayDirection > 0.0F ? -surfaceNormalZ : surfaceNormalZ;
				
				final float incomingDotSurfaceNormal = vector3FDotProduct(incomingX, incomingY, incomingZ, surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
				final float incomingDotSurfaceNormalAbs = abs(incomingDotSurfaceNormal);
				
				final float rayDirectionDotSurfaceNormal = vector3FDotProduct(ray3FGetDirectionX(), ray3FGetDirectionY(), ray3FGetDirectionZ(), surfaceNormalCorrectlyOrientedX, surfaceNormalCorrectlyOrientedY, surfaceNormalCorrectlyOrientedZ);
				final float rayDirectionDotSurfaceNormalAbs = abs(rayDirectionDotSurfaceNormal);
				
				final boolean isProbabilityDensityFunctionValueValid = checkIsFinite(probabilityDensityFunctionValue) && probabilityDensityFunctionValue > 0.0F;
				
				final float r = isSelected ? 0.0F : isProbabilityDensityFunctionValueValid ? resultR * incomingDotSurfaceNormalAbs / probabilityDensityFunctionValue * rayDirectionDotSurfaceNormalAbs : 0.0F;
				final float g = isSelected ? 1.0F : isProbabilityDensityFunctionValueValid ? resultG * incomingDotSurfaceNormalAbs / probabilityDensityFunctionValue * rayDirectionDotSurfaceNormalAbs : 0.0F;
				final float b = isSelected ? 0.0F : isProbabilityDensityFunctionValueValid ? resultB * incomingDotSurfaceNormalAbs / probabilityDensityFunctionValue * rayDirectionDotSurfaceNormalAbs : 0.0F;
				
				filmAddColor(r, g, b);
			} else {
				filmAddColor(0.0F, 0.0F, 0.0F);
			}
		} else {
			filmAddColor(1.0F, 1.0F, 1.0F);
		}
		
		imageBegin();
		imageRedoGammaCorrection();
		imageEnd();
	}
}