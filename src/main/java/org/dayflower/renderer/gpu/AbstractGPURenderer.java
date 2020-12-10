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
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.Color3F;
import org.dayflower.image.Image;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererConfiguration;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.util.Floats;
import org.dayflower.util.Longs;
import org.dayflower.util.Timer;

import com.amd.aparapi.Kernel;
import com.amd.aparapi.Range;

/**
 * An {@code AbstractGPURenderer} is an abstract implementation of {@link Renderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGPURenderer extends Kernel implements Renderer {
	private static final float PRNG_NEXT_FLOAT_RECIPROCAL = 1.0F / (1 << 24);
	private static final long PRNG_ADDEND = 0xBL;
	private static final long PRNG_MASK = (1L << 48L) - 1L;
	private static final long PRNG_MULTIPLIER = 0x5DEECE66DL;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected float[] cameraArray;
	protected float[] pixelArray;
	protected float[] radianceArray;
	protected float[] rayArray_$private$6;
	protected int resolutionX;
	protected int resolutionY;
	protected long[] seedArray;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isRendering;
	private final AtomicReference<RendererConfiguration> rendererConfiguration;
	private final AtomicReference<RendererObserver> rendererObserver;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, either {@code rendererConfiguration} or {@code rendererObserver} are {@code null}
	 */
	protected AbstractGPURenderer(final RendererConfiguration rendererConfiguration, final RendererObserver rendererObserver) {
		this.cameraArray = new float[0];
		this.pixelArray = new float[0];
		this.radianceArray = new float[0];
		this.rayArray_$private$6 = new float[0];
		this.seedArray = new long[0];
		this.isClearing = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.rendererConfiguration = new AtomicReference<>(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererConfiguration getRendererConfiguration() {
		return this.rendererConfiguration.get();
	}
	
	/**
	 * Returns the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RendererObserver getRendererObserver() {
		return this.rendererObserver.get();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link Image} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code Image} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link Image} instance and, optionally, updates the associated {@link Display} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	@Override
	public final boolean render() {
		this.isRendering.set(true);
		
		final RendererConfiguration rendererConfiguration = getRendererConfiguration();
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final Image image = rendererConfiguration.getImage();
		
		final Timer timer = rendererConfiguration.getTimer();
		
		final int renderPasses = rendererConfiguration.getRenderPasses();
		final int renderPassesPerDisplayUpdate = rendererConfiguration.getRenderPassesPerDisplayUpdate();
		final int resolutionX = image.getResolutionX();
		final int resolutionY = image.getResolutionY();
		
		final Range range = Range.create(resolutionX * resolutionY);
		
		for(int renderPass = 1; renderPass <= renderPasses; renderPass++) {
			if(this.isClearing.compareAndSet(true, false)) {
				rendererConfiguration.setRenderPass(0);
				rendererConfiguration.setRenderTime(0L);
				
				image.filmClear();
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
				
				timer.restart();
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 0.0D);
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			execute(range);
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			final float[] pixelArray = doGetPixelArray();
			final float[] radianceArray = doGetRadianceArray();
			
//			TODO: Fix the following bottleneck using the Kernel itself in a different mode! It taks around 200 milliseconds per render pass.
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final int index = y * resolutionX + x;
					final int indexPixelArray = index * 2;
					final int indexRadianceArray = index * 3;
					
					final float r = radianceArray[indexRadianceArray + 0];
					final float g = radianceArray[indexRadianceArray + 1];
					final float b = radianceArray[indexRadianceArray + 2];
					
					final float imageX = x;
					final float imageY = y;
					final float pixelX = pixelArray[indexPixelArray + 0];
					final float pixelY = pixelArray[indexPixelArray + 1];
					
					final Color3F colorRGB = new Color3F(r, g, b);
					final Color3F colorXYZ = Color3F.convertRGBToXYZUsingPBRT(colorRGB);
					
					if(!colorXYZ.hasInfinites() && !colorXYZ.hasNaNs()) {
						image.filmAddColorXYZ(imageX + pixelX, imageY + pixelY, colorXYZ);
					}
				}
			}
			
			rendererObserver.onRenderPassProgress(this, renderPass, renderPasses, 1.0D);
			
			if(renderPass == 1 || renderPass % renderPassesPerDisplayUpdate == 0 || renderPass == renderPasses) {
				image.filmRender();
				
				rendererObserver.onRenderDisplay(this, image);
			}
			
			rendererConfiguration.setRenderPass(rendererConfiguration.getRenderPass() + 1);
			rendererConfiguration.setRenderTime(elapsedTimeMillis);
			
			rendererObserver.onRenderPassComplete(this, renderPass, renderPasses, elapsedTimeMillis);
		}
		
		this.isRendering.set(false);
		
		return true;
	}
	
	/**
	 * Attempts to shutdown the rendering process of this {@code AbstractGPURenderer} instance.
	 * <p>
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance was rendering and is shutting down, {@code false} otherwise
	 */
	@Override
	public final boolean renderShutdown() {
		return this.isRendering.compareAndSet(true, false);
	}
	
	/**
	 * Call this method to clear the {@link Image} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Disposes of any resources created by this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public final synchronized void dispose() {
		super.dispose();
	}
	
	/**
	 * Sets the {@link RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererConfiguration}.
	 * <p>
	 * If {@code rendererConfiguration} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererConfiguration the {@code RendererConfiguration} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererConfiguration} is {@code null}
	 */
	@Override
	public final void setRendererConfiguration(final RendererConfiguration rendererConfiguration) {
		this.rendererConfiguration.set(Objects.requireNonNull(rendererConfiguration, "rendererConfiguration == null"));
	}
	
	/**
	 * Sets the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance to {@code rendererObserver}.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@code RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	@Override
	public final void setRendererObserver(final RendererObserver rendererObserver) {
		this.rendererObserver.set(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public final void setup() {
		setExplicit(true);
		
		doSetupPixelArray();
		doSetupRadianceArray();
		doSetupResolution();
		doSetupScene();
		doSetupSeedArray();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	protected final boolean cameraGenerateRay() {
//		Retrieve the image coordinates:
		final float imageX = getImageX();
		final float imageY = getImageY();
		
//		Retrieve the pixel coordinates:
		final float pixelX = random();
		final float pixelY = random();
		
//		Retrieve all values from the 'cameraArray' in the correct order:
		final float fieldOfViewX = tan(+this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_X] * 0.5F);
		final float fieldOfViewY = tan(-this.cameraArray[Camera.ARRAY_OFFSET_FIELD_OF_VIEW_Y] * 0.5F);
		final float lens = this.cameraArray[Camera.ARRAY_OFFSET_LENS];
		final float uX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 0];
		final float uY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 1];
		final float uZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_U + 2];
		final float vX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 0];
		final float vY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 1];
		final float vZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_V + 2];
		final float wX = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 0];
		final float wY = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 1];
		final float wZ = this.cameraArray[Camera.ARRAY_OFFSET_ORTHONORMAL_BASIS_W + 2];
		final float eyeX = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 0];
		final float eyeY = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 1];
		final float eyeZ = this.cameraArray[Camera.ARRAY_OFFSET_EYE + 2];
		final float apertureRadius = this.cameraArray[Camera.ARRAY_OFFSET_APERTURE_RADIUS];
		final float focalDistance = this.cameraArray[Camera.ARRAY_OFFSET_FOCAL_DISTANCE];
		final float resolutionX = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_X];
		final float resolutionY = this.cameraArray[Camera.ARRAY_OFFSET_RESOLUTION_Y];
		
//		Compute the camera coordinates:
		final float cameraX = 2.0F * ((imageX + pixelX) / (resolutionX - 1.0F)) - 1.0F;
		final float cameraY = 2.0F * ((imageY + pixelY) / (resolutionY - 1.0F)) - 1.0F;
		
//		Compute the 'wFactor' that is used for the fisheye lens:
		float wFactor = 1.0F;
		
		if(lens == 0.0F) {
			final float dotProduct = cameraX * cameraX + cameraY * cameraY;
			
			if(dotProduct > 1.0F) {
				return false;
			}
			
			wFactor = sqrt(1.0F - dotProduct);
		}
		
//		Sample the disk with a uniform distribution:
		final float u = random();
		final float v = random();
		final float r = sqrt(u);
		final float theta = PI_MULTIPLIED_BY_2 * v;
		final float diskX = r * cos(theta);
		final float diskY = r * sin(theta);
		
//		Compute the point on the plane one unit away from the eye:
		final float pointOnPlaneOneUnitAwayFromEyeX = (uX * fieldOfViewX * cameraX) + (vX * fieldOfViewY * cameraY) + (eyeX + wX * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeY = (uY * fieldOfViewX * cameraX) + (vY * fieldOfViewY * cameraY) + (eyeY + wY * wFactor);
		final float pointOnPlaneOneUnitAwayFromEyeZ = (uZ * fieldOfViewX * cameraX) + (vZ * fieldOfViewY * cameraY) + (eyeZ + wZ * wFactor);
		
//		Compute the point on the image plane:
		final float pointOnImagePlaneX = eyeX + (pointOnPlaneOneUnitAwayFromEyeX - eyeX) * focalDistance;
		final float pointOnImagePlaneY = eyeY + (pointOnPlaneOneUnitAwayFromEyeY - eyeY) * focalDistance;
		final float pointOnImagePlaneZ = eyeZ + (pointOnPlaneOneUnitAwayFromEyeZ - eyeZ) * focalDistance;
		
//		Compute the ray origin:
		final float originX = apertureRadius > 0.00001F ? eyeX + ((uX * diskX * apertureRadius) + (vX * diskY * apertureRadius)) : eyeX;
		final float originY = apertureRadius > 0.00001F ? eyeY + ((uY * diskX * apertureRadius) + (vY * diskY * apertureRadius)) : eyeY;
		final float originZ = apertureRadius > 0.00001F ? eyeZ + ((uZ * diskX * apertureRadius) + (vZ * diskY * apertureRadius)) : eyeZ;
		
//		Compute the ray direction:
		final float directionX = pointOnImagePlaneX - originX;
		final float directionY = pointOnImagePlaneY - originY;
		final float directionZ = pointOnImagePlaneZ - originZ;
		final float directionLengthReciprocal = rsqrt(directionX * directionX + directionY * directionY + directionZ * directionZ);
		final float directionNormalizedX = directionX * directionLengthReciprocal;
		final float directionNormalizedY = directionY * directionLengthReciprocal;
		final float directionNormalizedZ = directionZ * directionLengthReciprocal;
		
//		Compute offsets:
		final int pixelArrayOffset = getGlobalId() * 2;
		
//		Fill in the ray array:
		this.rayArray_$private$6[0] = originX;
		this.rayArray_$private$6[1] = originY;
		this.rayArray_$private$6[2] = originZ;
		this.rayArray_$private$6[3] = directionNormalizedX;
		this.rayArray_$private$6[4] = directionNormalizedY;
		this.rayArray_$private$6[5] = directionNormalizedZ;
		
//		Fill in the pixel array:
		this.pixelArray[pixelArrayOffset + 0] = pixelX;
		this.pixelArray[pixelArrayOffset + 1] = pixelY;
		
		return true;
	}
	
//	TODO: Add Javadocs!
	protected final float getImageX() {
		return getGlobalId() % this.resolutionX;
	}
	
//	TODO: Add Javadocs!
	protected final float getImageY() {
		return getGlobalId() / this.resolutionX;
	}
	
	/**
	 * Returns a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive).
	 * 
	 * @return a pseudorandom {@code float} value between {@code 0.0F} (inclusive) and {@code 1.0F} (exclusive)
	 */
	protected final float random() {
		return doNext(24) * PRNG_NEXT_FLOAT_RECIPROCAL;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Image doGetImage() {
		return getRendererConfiguration().getImage();
	}
	
	private Scene doGetScene() {
		return getRendererConfiguration().getScene();
	}
	
	private float[] doGetAndReturn(final float[] array) {
		get(array);
		
		return array;
	}
	
	private float[] doGetPixelArray() {
		return doGetAndReturn(this.pixelArray);
	}
	
	private float[] doGetRadianceArray() {
		return doGetAndReturn(this.radianceArray);
	}
	
	private int doGetResolution() {
		return doGetImage().getResolution();
	}
	
	private int doNext(final int bits) {
		final int index = getGlobalId();
		
		final long oldSeed = this.seedArray[index];
		final long newSeed = (oldSeed * PRNG_MULTIPLIER + PRNG_ADDEND) & PRNG_MASK;
		
		this.seedArray[index] = newSeed;
		
		return (int)(newSeed >>> (48 - bits));
	}
	
	private void doSetupPixelArray() {
		put(this.pixelArray = Floats.array(doGetResolution() * 2, 0.0F));
	}
	
	private void doSetupRadianceArray() {
		put(this.radianceArray = Floats.array(doGetResolution() * 3, 0.0F));
	}
	
	private void doSetupResolution() {
		this.resolutionX = doGetImage().getResolutionX();
		this.resolutionY = doGetImage().getResolutionY();
	}
	
	private void doSetupScene() {
		put(this.cameraArray = doGetScene().getCamera().toArray());
	}
	
	private void doSetupSeedArray() {
		put(this.seedArray = Longs.array(doGetResolution(), () -> ThreadLocalRandom.current().nextLong()));
	}
}