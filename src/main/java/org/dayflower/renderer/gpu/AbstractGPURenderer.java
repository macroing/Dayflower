/**
 * Copyright 2014 - 2022 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.toInt;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.dayflower.image.ByteImageF;
import org.dayflower.image.ImageF;
import org.dayflower.image.PixelImageF;
import org.dayflower.renderer.CombinedProgressiveImageOrderRenderer;
import org.dayflower.renderer.RendererObserver;
import org.dayflower.renderer.RenderingAlgorithm;
import org.dayflower.scene.Camera;
import org.dayflower.scene.Scene;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.Color3F;
import org.macroing.art4j.color.ColorSpaceF;
import org.macroing.java.util.Timer;

import com.aparapi.Range;

/**
 * An {@code AbstractGPURenderer} is an abstract implementation of {@link CombinedProgressiveImageOrderRenderer} that takes care of most aspects.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class AbstractGPURenderer extends AbstractSceneKernel implements CombinedProgressiveImageOrderRenderer {
	private static final int RENDERING_ALGORITHM_ORDINAL_AMBIENT_OCCLUSION = 0;//RenderingAlgorithm.AMBIENT_OCCLUSION.ordinal();
	private static final int RENDERING_ALGORITHM_ORDINAL_DEPTH_CAMERA = 1;//RenderingAlgorithm.DEPTH_CAMERA.ordinal();
	private static final int RENDERING_ALGORITHM_ORDINAL_PATH_TRACING = 2;//RenderingAlgorithm.PATH_TRACING.ordinal();
	private static final int RENDERING_ALGORITHM_ORDINAL_RAY_CASTING = 3;//RenderingAlgorithm.RAY_CASTING.ordinal();
	private static final int RENDERING_ALGORITHM_ORDINAL_RAY_TRACING = 4;//RenderingAlgorithm.RAY_TRACING.ordinal();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The maximum distance.
	 */
	protected float maximumDistance;
	
	/**
	 * The maximum bounce.
	 */
	protected int maximumBounce;
	
	/**
	 * The minimum bounce before Russian roulette termination occurs.
	 */
	protected int minimumBounceRussianRoulette;
	
	/**
	 * The ordinal of the current {@link RenderingAlgorithm} instance.
	 */
	protected int renderingAlgorithmOrdinal;
	
	/**
	 * The samples to use per render pass.
	 */
	protected int samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final AtomicBoolean isClearing;
	private final AtomicBoolean isPreviewMode;
	private final AtomicBoolean isRendering;
	private final AtomicInteger renderPass;
	private final AtomicReference<ImageF> image;
	private final AtomicReference<Range> range;
	private final AtomicReference<RendererObserver> rendererObserver;
	private final AtomicReference<RenderingAlgorithm> renderingAlgorithm;
	private final Timer timer;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code AbstractGPURenderer} instance.
	 * <p>
	 * If {@code rendererObserver} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rendererObserver the {@link RendererObserver} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code rendererObserver} is {@code null}
	 */
	protected AbstractGPURenderer(final RendererObserver rendererObserver) {
		this.isClearing = new AtomicBoolean();
		this.isPreviewMode = new AtomicBoolean();
		this.isRendering = new AtomicBoolean();
		this.renderPass = new AtomicInteger();
		this.image = new AtomicReference<>(new ByteImageF(800, 800));
		this.range = new AtomicReference<>(Range.create(this.image.get().getResolution()));
		this.rendererObserver = new AtomicReference<>(Objects.requireNonNull(rendererObserver, "rendererObserver == null"));
		this.renderingAlgorithm = new AtomicReference<>(RenderingAlgorithm.PATH_TRACING);
		this.renderingAlgorithmOrdinal = this.renderingAlgorithm.get().ordinal();
		this.timer = new Timer();
		this.maximumDistance = 20.0F;
		this.maximumBounce = 20;
		this.minimumBounceRussianRoulette = 5;
		this.samples = 10;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link ImageF} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code ImageF} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final ImageF getImage() {
		return this.image.get();
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
	 * Returns the {@link RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final RenderingAlgorithm getRenderingAlgorithm() {
		return this.renderingAlgorithm.get();
	}
	
	/**
	 * Returns the {@link Timer} instance associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the {@code Timer} instance associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final Timer getTimer() {
		return this.timer;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@link ImageF} instance in the next {@link #render()} call, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code AbstractGPURenderer} instance is clearing the {@code ImageF} instance in the next {@code  render()} call, {@code false} otherwise
	 */
	@Override
	public final boolean isClearing() {
		return this.isClearing.get();
	}
	
	/**
	 * Returns the preview mode state associated with this {@code AbstractGPURenderer} instance.
	 * 
	 * @return the preview mode state associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final boolean isPreviewMode() {
		return this.isPreviewMode.get();
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link ImageF} instance and, optionally, updates the associated {@link RendererObserver} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, rendering was performed, {@code false} otherwise
	 */
	@Override
	public final synchronized boolean render() {
		this.isRendering.set(true);
		
		final RendererObserver rendererObserver = getRendererObserver();
		
		final ImageF image = getImage();
		
		synchronized(image) {
			if(updateCompiledScene()) {
				clear();
			}
			
			final int resolutionX = image.getResolutionX();
			final int resolutionY = image.getResolutionY();
			
			if(resolutionX != getResolutionX() || resolutionY != getResolutionY()) {
				setup(false);
			}
			
			final Range range = this.range.get();
			
			if(this.isClearing.compareAndSet(true, false)) {
				this.renderPass.set(0);
				
				filmClear();
				
				doClearImageF(image);
				
				rendererObserver.onRenderDisplay(this, image);
				
				final
				Timer timer = getTimer();
				timer.restart();
			} else {
				filmClearFilmFlags();
			}
			
			final int renderPass = this.renderPass.incrementAndGet();
			
			rendererObserver.onRenderPassProgress(this, renderPass, 0.0D);
			
			final long currentTimeMillis = System.currentTimeMillis();
			
			execute(range);
			
			doUpdateImageF(image);
			
			final long elapsedTimeMillis = System.currentTimeMillis() - currentTimeMillis;
			
			rendererObserver.onRenderPassProgress(this, renderPass, 1.0D);
			rendererObserver.onRenderDisplay(this, image);
			rendererObserver.onRenderPassComplete(this, renderPass, elapsedTimeMillis);
			
			this.isRendering.set(false);
		}
		
		return true;
	}
	
	/**
	 * Renders the associated {@link Scene} instance to the associated {@link ImageF} instance and, optionally, updates the associated {@link RendererObserver} instance.
	 * <p>
	 * Returns {@code true} if, and only if, rendering was performed for all render passes, {@code false} otherwise.
	 * <p>
	 * If {@code renderPasses} is less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * <code>
	 * for(int renderPass = 0; renderPass &lt; renderPasses; renderPass++) {
	 *     if(!abstractGPURenderer.render()) {
	 *         break;
	 *     }
	 * }
	 * </code>
	 * </pre>
	 * 
	 * @param renderPasses the number of render passes to perform rendering
	 * @return {@code true} if, and only if, rendering was performed for all render passes, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, {@code renderPasses} is less than {@code 1}
	 */
	@Override
	public final synchronized boolean render(final int renderPasses) {
		ParameterArguments.requireRange(renderPasses, 1, Integer.MAX_VALUE, "renderPasses");
		
		for(int renderPass = 0; renderPass < renderPasses; renderPass++) {
			if(!render()) {
				return false;
			}
		}
		
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
	 * Returns the maximum distance.
	 * 
	 * @return the maximum distance
	 */
	@Override
	public final float getMaximumDistance() {
		return this.maximumDistance;
	}
	
	/**
	 * Returns the maximum bounce.
	 * 
	 * @return the maximum bounce
	 */
	@Override
	public final int getMaximumBounce() {
		return this.maximumBounce;
	}
	
	/**
	 * Returns the minimum bounce before Russian roulette termination occurs.
	 * 
	 * @return the minimum bounce before Russian roulette termination occurs
	 */
	@Override
	public final int getMinimumBounceRussianRoulette() {
		return this.minimumBounceRussianRoulette;
	}
	
	/**
	 * Returns the current render pass.
	 * 
	 * @return the current render pass
	 */
	@Override
	public final int getRenderPass() {
		return this.renderPass.get();
	}
	
	/**
	 * Returns the samples to use per render pass.
	 * 
	 * @return the samples to use per render pass
	 */
	@Override
	public final int getSamples() {
		return this.samples;
	}
	
	/**
	 * Call this method to clear the {@link ImageF} in the next {@link #render()} call.
	 */
	@Override
	public final void clear() {
		this.isClearing.set(true);
	}
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code AbstractGPURenderer} instance based on the current setup.
	 */
	@Override
	public final void setImage() {
		final Scene scene = getScene();
		
		final Camera camera = scene.getCamera();
		
		final int resolutionX = toInt(camera.getResolutionX());
		final int resolutionY = toInt(camera.getResolutionY());
		
		setImage(new ByteImageF(resolutionX, resolutionY));
	}
	
	/**
	 * Sets the {@link ImageF} instance associated with this {@code AbstractGPURenderer} instance to {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image the {@code ImageF} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	@Override
	public final void setImage(final ImageF image) {
		this.image.set(Objects.requireNonNull(image, "image == null"));
		this.range.set(Range.create(this.image.get().getResolution()));
	}
	
	/**
	 * Sets the maximum bounce to {@code maximumBounce}.
	 * 
	 * @param maximumBounce the maximum bounce
	 */
	@Override
	public final void setMaximumBounce(final int maximumBounce) {
		this.maximumBounce = maximumBounce;
	}
	
	/**
	 * Sets the maximum distance to {@code maximumDistance}.
	 * 
	 * @param maximumDistance the maximum distance
	 */
	@Override
	public final void setMaximumDistance(final float maximumDistance) {
		this.maximumDistance = maximumDistance;
	}
	
	/**
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	@Override
	public final void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette) {
		this.minimumBounceRussianRoulette = minimumBounceRussianRoulette;
	}
	
	/**
	 * Sets the preview mode state associated with this {@code AbstractGPURenderer} instance to {@code isPreviewMode}.
	 * 
	 * @param isPreviewMode the preview mode state associated with this {@code AbstractGPURenderer} instance
	 */
	@Override
	public final void setPreviewMode(final boolean isPreviewMode) {
		this.isPreviewMode.set(isPreviewMode);
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
	 * Sets the {@link RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance to {@code renderingAlgorithm}.
	 * <p>
	 * If {@code renderingAlgorithm} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderingAlgorithm the {@code RenderingAlgorithm} instance associated with this {@code AbstractGPURenderer} instance
	 * @throws NullPointerException thrown if, and only if, {@code renderingAlgorithm} is {@code null}
	 */
	@Override
	public final void setRenderingAlgorithm(final RenderingAlgorithm renderingAlgorithm) {
		this.renderingAlgorithm.set(Objects.requireNonNull(renderingAlgorithm, "renderingAlgorithm == null"));
		this.renderingAlgorithmOrdinal = this.renderingAlgorithm.get().ordinal();
	}
	
	/**
	 * Sets the samples to use per render pass to {@code samples}.
	 * 
	 * @param samples the samples to use per render pass
	 */
	@Override
	public final void setSamples(final int samples) {
		this.samples = samples;
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 */
	@Override
	public void setup() {
		setup(true);
	}
	
	/**
	 * Sets up all necessary resources for this {@code AbstractGPURenderer} instance.
	 * 
	 * @param isSettingUpScene {@code true} if, and only if, the scene should be setup, {@code false} otherwise
	 */
	@Override
	public void setup(final boolean isSettingUpScene) {
		final ImageF image = getImage();
		
		synchronized(image) {
			final Scene scene = getScene();
			
			final int resolutionX = image.getResolutionX();
			final int resolutionY = image.getResolutionY();
			
			setResolution(resolutionX, resolutionY);
			setScene(scene);
			
			super.setup(isSettingUpScene);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the current {@link RenderingAlgorithm} is set to {@link RenderingAlgorithm#AMBIENT_OCCLUSION}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current {@code RenderingAlgorithm} is set to {@code RenderingAlgorithm.AMBIENT_OCCLUSION}, {@code false} otherwise
	 */
	protected final boolean renderingAlgorithmIsAmbientOcclusion() {
		return this.renderingAlgorithmOrdinal == RENDERING_ALGORITHM_ORDINAL_AMBIENT_OCCLUSION;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current {@link RenderingAlgorithm} is set to {@link RenderingAlgorithm#DEPTH_CAMERA}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current {@code RenderingAlgorithm} is set to {@code RenderingAlgorithm.DEPTH_CAMERA}, {@code false} otherwise
	 */
	protected final boolean renderingAlgorithmIsDepthCamera() {
		return this.renderingAlgorithmOrdinal == RENDERING_ALGORITHM_ORDINAL_DEPTH_CAMERA;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current {@link RenderingAlgorithm} is set to {@link RenderingAlgorithm#PATH_TRACING}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current {@code RenderingAlgorithm} is set to {@code RenderingAlgorithm.PATH_TRACING}, {@code false} otherwise
	 */
	protected final boolean renderingAlgorithmIsPathTracing() {
		return this.renderingAlgorithmOrdinal == RENDERING_ALGORITHM_ORDINAL_PATH_TRACING;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current {@link RenderingAlgorithm} is set to {@link RenderingAlgorithm#RAY_CASTING}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current {@code RenderingAlgorithm} is set to {@code RenderingAlgorithm.RAY_CASTING}, {@code false} otherwise
	 */
	protected final boolean renderingAlgorithmIsRayCasting() {
		return this.renderingAlgorithmOrdinal == RENDERING_ALGORITHM_ORDINAL_RAY_CASTING;
	}
	
	/**
	 * Returns {@code true} if, and only if, the current {@link RenderingAlgorithm} is set to {@link RenderingAlgorithm#RAY_TRACING}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the current {@code RenderingAlgorithm} is set to {@code RenderingAlgorithm.RAY_TRACING}, {@code false} otherwise
	 */
	protected final boolean renderingAlgorithmIsRayTracing() {
		return this.renderingAlgorithmOrdinal == RENDERING_ALGORITHM_ORDINAL_RAY_TRACING;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void doUpdateByteImageF(final ByteImageF byteImage) {
		final byte[] bytes = byteImage.getData(true);
		final byte[] imageColorByteArray = getImageColorByteArray();
		
		System.arraycopy(imageColorByteArray, 0, bytes, 0, bytes.length);
	}
	
	private void doUpdateImageF(final ImageF image) {
		synchronized(image) {
			if(image instanceof ByteImageF) {
				doUpdateByteImageF(ByteImageF.class.cast(image));
			} else if(image instanceof PixelImageF) {
				doUpdatePixelImageF(PixelImageF.class.cast(image));
			}
		}
	}
	
	private void doUpdatePixelImageF(final PixelImageF pixelImage) {
		final int resolutionX = pixelImage.getResolutionX();
		final int resolutionY = pixelImage.getResolutionY();
		
		final float[] imageColorFloatArray = getImageColorFloatArray();
		final float[] pixelArray = getPixelArray();
		
		final ColorSpaceF colorSpace = ColorSpaceF.getDefault();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final int index = y * resolutionX + x;
				final int indexPixelArray = index * 2;
				final int indexRadianceRGBFloatArray = index * 3;
				
				final float r = imageColorFloatArray[indexRadianceRGBFloatArray + 0];
				final float g = imageColorFloatArray[indexRadianceRGBFloatArray + 1];
				final float b = imageColorFloatArray[indexRadianceRGBFloatArray + 2];
				
				final float imageX = x;
				final float imageY = y;
				final float pixelX = pixelArray[indexPixelArray + 0];
				final float pixelY = pixelArray[indexPixelArray + 1];
				
				final Color3F colorRGB = new Color3F(r, g, b);
				final Color3F colorXYZ = colorSpace.convertRGBToXYZ(colorRGB);
				
				if(!colorXYZ.hasInfinites() && !colorXYZ.hasNaNs()) {
					pixelImage.filmAddColorXYZ(imageX + pixelX, imageY + pixelY, colorXYZ);
				}
			}
		}
		
		pixelImage.filmRender();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static void doClearImageF(final ImageF image) {
		synchronized(image) {
			if(image instanceof PixelImageF) {
				doClearPixelImageF(PixelImageF.class.cast(image));
			}
		}
	}
	
	private static void doClearPixelImageF(final PixelImageF pixelImage) {
		pixelImage.filmClear();
		pixelImage.filmRender();
	}
}