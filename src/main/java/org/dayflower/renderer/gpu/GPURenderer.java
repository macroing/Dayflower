/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Floats.PI;
import static org.dayflower.utility.Floats.PI_RECIPROCAL;

import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.observer.FileRendererObserver;

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
		
		if(ray3FCameraGenerate(random(), random()) && primitiveIntersectionCompute()) {
			orthonormalBasis33FSetIntersectionOrthonormalBasisG();
			
			for(int sample = 0; sample < samples; sample++) {
				vector3FSetSampleHemisphereUniformDistribution(random(), random());
				vector3FSetOrthonormalBasis33FTransformNormalizeFromVector3F();
				
				ray3FSetFromSurfaceIntersectionPointAndVector3F();
				
				if(maximumDistance > 0.0F) {
					final float t = primitiveIntersectionT();
					final float s = t > 0.0F ? normalize(saturateF(t, 0.0F, maximumDistance), 0.0F, maximumDistance) : 1.0F;
					
					radiance += s;
				} else if(!primitiveIntersects()) {
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
	
	void doRunPathTracing(final int maximumBounce, final int minimumBounceRussianRoulette) {
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
			
			boolean isSpecularBounce = false;
			
			while(currentBounce < maximumBounce) {
				if(primitiveIntersectionCompute()) {
					if(currentBounce == 0 || isSpecularBounce) {
						materialEmittance(primitiveGetMaterialID(), primitiveGetMaterialOffset());
						
						radianceR += throughputR * color3FLHSGetComponent1();
						radianceG += throughputG * color3FLHSGetComponent2();
						radianceB += throughputB * color3FLHSGetComponent3();
					}
					
					if(materialBSDFCompute(primitiveGetMaterialID(), primitiveGetMaterialOffset())) {
						if(materialBSDFCountBXDFsBySpecularType(false) > 0) {
							lightSampleOneLightUniformDistribution();
							
							radianceR += throughputR * color3FLHSGetComponent1();
							radianceG += throughputG * color3FLHSGetComponent2();
							radianceB += throughputB * color3FLHSGetComponent3();
						}
						
						if(materialBSDFSampleDistributionFunction(B_X_D_F_TYPE_BIT_FLAG_ALL, random(), random())) {
							final float incomingX = materialBSDFResultGetIncomingX();
							final float incomingY = materialBSDFResultGetIncomingY();
							final float incomingZ = materialBSDFResultGetIncomingZ();
							
							final float probabilityDensityFunctionValue = materialBSDFResultGetProbabilityDensityFunctionValue();
							
							final float resultR = materialBSDFResultGetResultR();
							final float resultG = materialBSDFResultGetResultG();
							final float resultB = materialBSDFResultGetResultB();
							
							final float surfaceNormalSX = intersectionGetOrthonormalBasisSWComponent1();
							final float surfaceNormalSY = intersectionGetOrthonormalBasisSWComponent2();
							final float surfaceNormalSZ = intersectionGetOrthonormalBasisSWComponent3();
							
							final float incomingDotSurfaceNormalS = vector3FDotProduct(incomingX, incomingY, incomingZ, surfaceNormalSX, surfaceNormalSY, surfaceNormalSZ);
							final float incomingDotSurfaceNormalSAbs = abs(incomingDotSurfaceNormalS);
							
							final boolean isProbabilityDensityFunctionValueValue = checkIsFinite(probabilityDensityFunctionValue) && probabilityDensityFunctionValue > 0.0F;
							
							if(isProbabilityDensityFunctionValueValue) {
								throughputR *= resultR * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
								throughputG *= resultG * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
								throughputB *= resultB * incomingDotSurfaceNormalSAbs / probabilityDensityFunctionValue;
							}
							
							isSpecularBounce = materialBSDFResultBXDFIsSpecular();
							
							vector3FSet(incomingX, incomingY, incomingZ);
							
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
							
							currentBounce = isProbabilityDensityFunctionValueValue ? currentBounce + 1 : maximumBounce;
						} else {
							currentBounce = maximumBounce;
						}
					} else {
						currentBounce = maximumBounce;
					}
				} else if(currentBounce == 0 || isSpecularBounce) {
					lightEvaluateRadianceEmittedAll();
					
					radianceR += throughputR * color3FLHSGetComponent1();
					radianceG += throughputG * color3FLHSGetComponent2();
					radianceB += throughputB * color3FLHSGetComponent3();
					
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
		
		filmAddColor(radianceR, radianceG, radianceB);
		
		imageBegin();
		imageRedoGammaCorrectionPBRT();
		imageEnd();
	}
	
	void doRunRayCasting() {
		if(ray3FCameraGenerate(random(), random())) {
			if(primitiveIntersectionCompute()) {
				final float rayDirectionX = ray3FGetDirectionComponent1();
				final float rayDirectionY = ray3FGetDirectionComponent2();
				final float rayDirectionZ = ray3FGetDirectionComponent3();
				
				final float surfaceNormalX = intersectionGetOrthonormalBasisSWComponent1();
				final float surfaceNormalY = intersectionGetOrthonormalBasisSWComponent2();
				final float surfaceNormalZ = intersectionGetOrthonormalBasisSWComponent3();
				
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
	
	void doRunRayTracing() {
		filmAddColor(0.0F, 0.0F, 0.0F);
		
		imageBegin();
		imageRedoGammaCorrectionPBRT();
		imageEnd();
	}
}