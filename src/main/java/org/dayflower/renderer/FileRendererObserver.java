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

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;

import org.dayflower.image.Image;

//TODO: Add Javadocs!
public final class FileRendererObserver implements RendererObserver {
	private final File file;
	private final boolean isPrintingComplete;
	private final boolean isPrintingProgress;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public FileRendererObserver() {
		this(new File("Image.png"));
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final File file) {
		this(file, false);
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final File file, final boolean isPrintingComplete) {
		this(file, isPrintingComplete, false);
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final File file, final boolean isPrintingComplete, final boolean isPrintingProgress) {
		this.file = Objects.requireNonNull(file, "file == null");
		this.isPrintingComplete = isPrintingComplete;
		this.isPrintingProgress = isPrintingProgress;
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final String pathname) {
		this(pathname, false);
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final String pathname, final boolean isPrintingComplete) {
		this(pathname, isPrintingComplete, false);
	}
	
//	TODO: Add Javadocs!
	public FileRendererObserver(final String pathname, final boolean isPrintingComplete, final boolean isPrintingProgress) {
		this(new File(pathname), isPrintingComplete, isPrintingProgress);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderDisplay(final Renderer renderer, final Image image) {
		Objects.requireNonNull(renderer, "renderer == null");
		Objects.requireNonNull(image, "image == null");
		
		image.save(this.file);
	}
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderPassComplete(final Renderer renderer, final int renderPass, final int renderPasses, final long elapsedTimeMillis) {
		Objects.requireNonNull(renderer, "renderer == null");
		
		if(this.isPrintingComplete) {
			System.out.printf("Pass: %s/%s, Millis: %s%n", Integer.toString(renderPass), Integer.toString(renderPasses), Long.toString(elapsedTimeMillis));
		}
	}
	
//	TODO: Add Javadocs!
	@Override
	public void onRenderPassProgress(final Renderer renderer, final int renderPass, final int renderPasses, final double percent) {
		Objects.requireNonNull(renderer, "renderer == null");
		
		if(this.isPrintingProgress) {
			System.out.printf("%f%n", Double.valueOf(percent * 100.0D));
		}
	}
}