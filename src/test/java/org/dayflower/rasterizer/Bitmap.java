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
package org.dayflower.rasterizer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

public class Bitmap {
	private final byte[] components;
	private final int height;
	private final int width;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Bitmap(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.components = new byte[this.width * this.height * 4];
	}
	
	public Bitmap(final String fileName) throws IOException {
		final BufferedImage bufferedImage = ImageIO.read(new File(fileName));
		
		final int width = bufferedImage.getWidth();
		final int height = bufferedImage.getHeight();
		
		final int pixels[] = new int[width * height];
		
		bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		
		final byte[] components = new byte[width * height * 4];
		
		for(int i = 0; i < width * height; i++) {
			final int pixel = pixels[i];
			
			components[i * 4 + 0] = (byte)((pixel >> 24) & 0xFF);
			components[i * 4 + 1] = (byte)((pixel >>  0) & 0xFF);
			components[i * 4 + 2] = (byte)((pixel >>  8) & 0xFF);
			components[i * 4 + 3] = (byte)((pixel >> 16) & 0xFF);
		}
		
		this.width = width;
		this.height = height;
		this.components = components;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public byte getComponent(final int index) {
		try {
			return this.components[index];
		} catch(final ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public void clear(final byte shade) {
		Arrays.fill(this.components, shade);
	}
	
	public void copyPixel(final int destinationX, final int destinationY, final int sourceX, final int sourceY, final Bitmap bitmap, final float lightIntensity) {
		final int destinationIndex = (destinationX + destinationY * this.width) * 4;
		final int sourceIndex = (sourceX + sourceY * bitmap.getWidth()) * 4;
		
		this.components[destinationIndex + 0] = (byte)((bitmap.getComponent(sourceIndex + 0) & 0xFF) * lightIntensity);
		this.components[destinationIndex + 1] = (byte)((bitmap.getComponent(sourceIndex + 1) & 0xFF) * lightIntensity);
		this.components[destinationIndex + 2] = (byte)((bitmap.getComponent(sourceIndex + 2) & 0xFF) * lightIntensity);
		this.components[destinationIndex + 3] = (byte)((bitmap.getComponent(sourceIndex + 3) & 0xFF) * lightIntensity);
	}
	
	public void copyToByteArray(final byte[] destination) {
		for(int i = 0; i < this.width * this.height; i++) {
			destination[i * 3 + 0] = this.components[i * 4 + 1];
			destination[i * 3 + 1] = this.components[i * 4 + 2];
			destination[i * 3 + 2] = this.components[i * 4 + 3];
		}
	}
	
	public void drawPixel(final int x, final int y, final byte a, final byte b, final byte g, final byte r) {
		final int index = (x + y * this.width) * 4;
		
		this.components[index + 0] = a;
		this.components[index + 1] = b;
		this.components[index + 2] = g;
		this.components[index + 3] = r;
	}
}