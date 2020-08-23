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
package org.dayflower;

import static org.dayflower.Floats.abs;
import static org.dayflower.Floats.ceil;
import static org.dayflower.Floats.equal;
import static org.dayflower.Floats.floor;
import static org.dayflower.Floats.max;
import static org.dayflower.Floats.min;
import static org.dayflower.Ints.min;
import static org.dayflower.Ints.requireRange;
import static org.dayflower.Ints.toInt;

import java.util.Objects;

public final class Image {
	private static final int FILTER_TABLE_SIZE = 16;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final Filter filter;
	private final Pixel[] pixels;
	private final float[] filterTable;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public Image() {
		this(800, 800);
	}
	
	public Image(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, new Color3F());
	}
	
	public Image(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		this(resolutionX, resolutionY, colorRGB, new MitchellFilter());
	}
	
	public Image(final int resolutionX, final int resolutionY, final Color3F colorRGB, final Filter filter) {
		this.resolutionX = requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = doCreatePixels(resolutionX, resolutionY, Objects.requireNonNull(colorRGB, "colorRGB == null"));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = doCreateFilterTable(filter);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getResolution() {
		return this.resolution;
	}
	
	public int getResolutionX() {
		return this.resolutionX;
	}
	
	public int getResolutionY() {
		return this.resolutionY;
	}
	
	public void filmAddColor(final float x, final float y, final Color3F colorXYZ, final float sampleWeight) {
		final Filter filter = this.filter;
		
		final Pixel[] pixels = this.pixels;
		
		final float[] filterTable = this.filterTable;
		
		final float filterResolutionX = filter.getResolutionX();
		final float filterResolutionY = filter.getResolutionY();
		final float filterResolutionXReciprocal = filter.getResolutionXReciprocal();
		final float filterResolutionYReciprocal = filter.getResolutionYReciprocal();
		
		final float deltaX = x - 0.5F;
		final float deltaY = y - 0.5F;
		
		final int resolutionX = this.resolutionX;
		final int resolutionY = this.resolutionY;
		
		final int minimumFilterX = toInt(max(ceil(deltaX - filterResolutionX), 0));
		final int maximumFilterX = toInt(min(floor(deltaX + filterResolutionX), resolutionX - 1));
		final int minimumFilterY = toInt(max(ceil(deltaY - filterResolutionY), 0));
		final int maximumFilterY = toInt(min(floor(deltaY + filterResolutionY), resolutionY - 1));
		
		if(maximumFilterX - minimumFilterX >= 0 && maximumFilterY - minimumFilterY >= 0) {
			final int[] filterOffsetX = new int[maximumFilterX - minimumFilterX + 1];
			final int[] filterOffsetY = new int[maximumFilterY - minimumFilterY + 1];
			
			for(int filterX = minimumFilterX; filterX <= maximumFilterX; filterX++) {
				filterOffsetX[filterX - minimumFilterX] = min(toInt(floor(abs((filterX - deltaX) * filterResolutionXReciprocal * FILTER_TABLE_SIZE))), FILTER_TABLE_SIZE - 1);
			}
			
			for(int filterY = minimumFilterY; filterY <= maximumFilterY; filterY++) {
				filterOffsetY[filterY - minimumFilterY] = min(toInt(floor(abs((filterY - deltaY) * filterResolutionYReciprocal * FILTER_TABLE_SIZE))), FILTER_TABLE_SIZE - 1);
			}
			
			for(int filterY = minimumFilterY; filterY <= maximumFilterY; filterY++) {
				for(int filterX = minimumFilterX; filterX <= maximumFilterX; filterX++) {
					final int filterTableIndex = filterOffsetY[filterY - minimumFilterY] * FILTER_TABLE_SIZE + filterOffsetX[filterX - minimumFilterX];
					final int pixelIndex = filterY * resolutionX + filterX;
					
					final float filterWeight = filterTable[filterTableIndex];
					
					final Pixel pixel = pixels[pixelIndex];
					
					final Color3F oldColorXYZ = pixel.getColorXYZ();
					final Color3F newColorXYZ = Color3F.add(oldColorXYZ, Color3F.multiply(colorXYZ, sampleWeight * filterWeight));
					
					final float oldFilterWeightSum = pixel.getFilterWeightSum();
					final float newFilterWeightSum = oldFilterWeightSum + filterWeight;
					
					pixel.setColorXYZ(newColorXYZ);
					pixel.setFilterWeightSum(newFilterWeightSum);
				}
			}
		}
	}
	
	public void filmAddSplat(final float x, final float y, final Color3F splatXYZ) {
		final Pixel[] pixels = this.pixels;
		
		final int currentX = toInt(floor(x));
		final int currentY = toInt(floor(y));
		
		final int resolutionX = this.resolutionX;
		final int resolutionY = this.resolutionY;
		
		if(currentX >= 0 && currentX < resolutionX && currentY >= 0 && currentY < resolutionY) {
			final int pixelIndex = currentY * resolutionX + currentX;
			
			final Pixel pixel = pixels[pixelIndex];
			
			final Color3F oldSplatXYZ = pixel.getSplatXYZ();
			final Color3F newSplatXYZ = Color3F.add(oldSplatXYZ, splatXYZ);
			
			pixel.setSplatXYZ(newSplatXYZ);
		}
	}
	
	public void filmClear() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorXYZ(new Color3F());
			pixel.setSplatXYZ(new Color3F());
			pixel.setFilterWeightSum(0.0F);
		}
	}
	
	public void filmRender(final float splatScale) {
		for(final Pixel pixel : this.pixels) {
			Color3F colorRGB = Color3F.convertXYZToRGBUsingPBRT(pixel.getColorXYZ());
			Color3F splatRGB = Color3F.convertXYZToRGBUsingPBRT(pixel.getSplatXYZ());
			
			if(!equal(pixel.getFilterWeightSum(), 0.0F)) {
				colorRGB = Color3F.multiplyAndSaturateNegative(colorRGB, 1.0F / pixel.getFilterWeightSum());
			}
			
			splatRGB = Color3F.multiply(splatRGB, splatScale);
			colorRGB = Color3F.add(colorRGB, splatRGB);
			colorRGB = Color3F.redoGammaCorrectionPBRT(colorRGB);
			
			pixel.setColorRGB(colorRGB);
		}
	}
	
	public void flipX() {
		for(int xL = 0, xR = this.resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < this.resolutionY; y++) {
				final int indexL = y * this.resolutionX + xL;
				final int indexR = y * this.resolutionX + xR;
				
				final Pixel pixelL = this.pixels[indexL];
				final Pixel pixelR = this.pixels[indexR];
				
				this.pixels[indexL] = pixelR;
				this.pixels[indexR] = pixelL;
			}
		}
	}
	
	public void flipY() {
		for(int yT = 0, yB = this.resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < this.resolutionX; x++) {
				final int indexT = yT * this.resolutionX + x;
				final int indexB = yB * this.resolutionX + x;
				
				final Pixel pixelT = this.pixels[indexT];
				final Pixel pixelB = this.pixels[indexB];
				
				this.pixels[indexT] = pixelB;
				this.pixels[indexB] = pixelT;
			}
		}
	}
	
	public void redoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	public void redoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	public void undoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	public void undoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Pixel[] doCreatePixels(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = new Pixel(colorRGB);
		}
		
		return pixels;
	}
	
	private static float[] doCreateFilterTable(final Filter filter) {
		final float[] filterTable = new float[FILTER_TABLE_SIZE * FILTER_TABLE_SIZE];
		
		final float filterResolutionX = filter.getResolutionX();
		final float filterResolutionY = filter.getResolutionY();
		final float filterTableSizeReciprocal = 1.0F / FILTER_TABLE_SIZE;
		
		for(int i = 0, y = 0; y < FILTER_TABLE_SIZE; y++) {
			for(int x = 0; x < FILTER_TABLE_SIZE; x++) {
				final float filterX = (x + 0.5F) * filterResolutionX * filterTableSizeReciprocal;
				final float filterY = (y + 0.5F) * filterResolutionY * filterTableSizeReciprocal;
				
				filterTable[i++] = filter.evaluate(filterX, filterY);
			}
		}
		
		return filterTable;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final class Pixel {
		private Color3F colorRGB;
		private Color3F colorXYZ;
		private Color3F splatXYZ;
		private float filterWeightSum;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Pixel(final Color3F colorRGB) {
			this(colorRGB, new Color3F(), new Color3F(), 0.0F);
		}
		
		public Pixel(final Color3F colorRGB, final Color3F colorXYZ, final Color3F splatXYZ, final float filterWeightSum) {
			this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
			this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
			this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
			this.filterWeightSum = filterWeightSum;
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Color3F getColorRGB() {
			return this.colorRGB;
		}
		
		public Color3F getColorXYZ() {
			return this.colorXYZ;
		}
		
		public Color3F getSplatXYZ() {
			return this.splatXYZ;
		}
		
		@Override
		public String toString() {
			return String.format("new Pixel(%s, %s, %s, %+.10f)", this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum));
		}
		
		@Override
		public boolean equals(final Object object) {
			if(object == this) {
				return true;
			} else if(!(object instanceof Pixel)) {
				return false;
			} else if(!Objects.equals(this.colorRGB, Pixel.class.cast(object).colorRGB)) {
				return false;
			} else if(!Objects.equals(this.colorXYZ, Pixel.class.cast(object).colorXYZ)) {
				return false;
			} else if(!Objects.equals(this.splatXYZ, Pixel.class.cast(object).splatXYZ)) {
				return false;
			} else if(!equal(this.filterWeightSum, Pixel.class.cast(object).filterWeightSum)) {
				return false;
			} else {
				return true;
			}
		}
		
		public float getFilterWeightSum() {
			return this.filterWeightSum;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum));
		}
		
		public void setColorRGB(final Color3F colorRGB) {
			this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
		}
		
		public void setColorXYZ(final Color3F colorXYZ) {
			this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
		}
		
		public void setFilterWeightSum(final float filterWeightSum) {
			this.filterWeightSum = filterWeightSum;
		}
		
		public void setSplatXYZ(final Color3F splatXYZ) {
			this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		}
	}
}