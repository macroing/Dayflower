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
package org.dayflower.renderer.observer;

import java.io.File;
import java.util.Objects;

import org.dayflower.image.ImageF;
import org.dayflower.renderer.Renderer;
import org.dayflower.renderer.RendererObserver;

/**
 * A {@code FileRendererObserver} is a {@link RendererObserver} implementation that writes the {@link ImageF} to a file.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FileRendererObserver implements RendererObserver {
	private final File file;
	private final boolean isPrintingOnComplete;
	private final boolean isPrintingOnProgress;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(new File("Image.png"));
	 * }
	 * </pre>
	 */
	public FileRendererObserver() {
		this(new File("Image.png"));
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(file, false);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public FileRendererObserver(final File file) {
		this(file, false);
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(file, isPrintingOnComplete, false);
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} instance
	 * @param isPrintingOnComplete {@code true} if, and only if, printing to standard output on complete should be enabled, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public FileRendererObserver(final File file, final boolean isPrintingOnComplete) {
		this(file, isPrintingOnComplete, false);
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param file a {@code File} instance
	 * @param isPrintingOnComplete {@code true} if, and only if, printing to standard output on complete should be enabled, {@code false} otherwise
	 * @param isPrintingOnProgress {@code true} if, and only if, printing to standard output on progress should be enabled, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 */
	public FileRendererObserver(final File file, final boolean isPrintingOnComplete, final boolean isPrintingOnProgress) {
		this.file = Objects.requireNonNull(file, "file == null");
		this.isPrintingOnComplete = isPrintingOnComplete;
		this.isPrintingOnProgress = isPrintingOnProgress;
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(pathname, false);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a {@code File} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 */
	public FileRendererObserver(final String pathname) {
		this(pathname, false);
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(pathname, isPrintingOnComplete, false);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a {@code File} instance
	 * @param isPrintingOnComplete {@code true} if, and only if, printing to standard output on complete should be enabled, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 */
	public FileRendererObserver(final String pathname, final boolean isPrintingOnComplete) {
		this(pathname, isPrintingOnComplete, false);
	}
	
	/**
	 * Constructs a new {@code FileRendererObserver} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileRendererObserver(new File(pathname), isPrintingOnComplete, isPrintingOnProgress);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} instance with the pathname to a {@code File} instance
	 * @param isPrintingOnComplete {@code true} if, and only if, printing to standard output on complete should be enabled, {@code false} otherwise
	 * @param isPrintingOnProgress {@code true} if, and only if, printing to standard output on progress should be enabled, {@code false} otherwise
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 */
	public FileRendererObserver(final String pathname, final boolean isPrintingOnComplete, final boolean isPrintingOnProgress) {
		this(new File(pathname), isPrintingOnComplete, isPrintingOnProgress);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This method is called by {@code renderer} when {@code image} should be displayed.
	 * <p>
	 * If either {@code renderer} or {@code image} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param image the {@link ImageF} instance that is rendered to
	 * @throws NullPointerException thrown if, and only if, either {@code renderer} or {@code image} are {@code null}
	 */
	@Override
	public void onRenderDisplay(final Renderer renderer, final ImageF image) {
		Objects.requireNonNull(renderer, "renderer == null");
		Objects.requireNonNull(image, "image == null");
		
		image.save(this.file);
	}
	
	/**
	 * This method is called by {@code renderer} when {@code renderPass} is complete.
	 * <p>
	 * If {@code renderer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param renderPass the current render pass
	 * @param renderPasses the total number of render passes
	 * @param elapsedTimeMillis the total number of milliseconds required to complete this render pass
	 * @throws NullPointerException thrown if, and only if, {@code renderer} is {@code null}
	 */
	@Override
	public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
		Objects.requireNonNull(renderer, "renderer == null");
		
		if(this.isPrintingOnComplete) {
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
		}
	}
	
	/**
	 * This method is called by {@code renderer} when {@code renderPass} is processed.
	 * <p>
	 * If {@code renderer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param renderer the {@link Renderer} instance that called this method
	 * @param renderPass the current render pass
	 * @param renderPasses the total number of render passes
	 * @param percent the progress in percent between {@code 0.0D} and {@code 1.0D}
	 * @throws NullPointerException thrown if, and only if, {@code renderer} is {@code null}
	 */
	@Override
	public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
		Objects.requireNonNull(renderer, "renderer == null");
		
		if(this.isPrintingOnProgress) {
			System.out.printf("%f%n", Double.valueOf(percent * 100.0D));
		}
	}
}