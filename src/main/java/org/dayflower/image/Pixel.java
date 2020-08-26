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
package org.dayflower.image;

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Ints.requireExact;
import static org.dayflower.util.Ints.requireRange;

import java.util.Objects;

public final class Pixel {
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static Pixel[] createPixels(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = new Pixel(colorRGB);
		}
		
		return pixels;
	}
	
	public static Pixel[] createPixels(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs) {
		requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGBs, "colorRGBs == null");
		
		requireExact(colorRGBs.length, resolutionX * resolutionY, "colorRGBs.length");
		
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = new Pixel(Objects.requireNonNull(colorRGBs[i], String.format("colorRGBs[%d] == null", Integer.valueOf(i))));
		}
		
		return pixels;
	}
}