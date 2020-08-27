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

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class Pixel {
	private Color3F colorRGB;
	private Color3F colorXYZ;
	private Color3F splatXYZ;
	private float filterWeightSum;
	private int index;
	private int x;
	private int y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Pixel(final Color3F colorRGB, final Color3F colorXYZ, final Color3F splatXYZ, final float filterWeightSum, final int index, final int x, final int y) {
		this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		this.filterWeightSum = filterWeightSum;
		this.index = index;
		this.x = x;
		this.y = y;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Color3F getColorRGB() {
		return this.colorRGB;
	}
	
//	TODO: Add Javadocs!
	public Color3F getColorXYZ() {
		return this.colorXYZ;
	}
	
//	TODO: Add Javadocs!
	public Color3F getSplatXYZ() {
		return this.splatXYZ;
	}
	
//	TODO: Add Javadocs!
	@Override
	public String toString() {
		return String.format("new Pixel(%s, %s, %s, %+.10f, %d, %d, %d)", this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
//	TODO: Add Javadocs!
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
		} else if(this.index != Pixel.class.cast(object).index) {
			return false;
		} else if(this.x != Pixel.class.cast(object).x) {
			return false;
		} else if(this.y != Pixel.class.cast(object).y) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getFilterWeightSum() {
		return this.filterWeightSum;
	}
	
//	TODO: Add Javadocs!
	public int getIndex() {
		return this.index;
	}
	
//	TODO: Add Javadocs!
	public int getX() {
		return this.x;
	}
	
//	TODO: Add Javadocs!
	public int getY() {
		return this.y;
	}
	
//	TODO: Add Javadocs!
	@Override
	public int hashCode() {
		return Objects.hash(this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
//	TODO: Add Javadocs!
	public void setColorRGB(final Color3F colorRGB) {
		this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
	}
	
//	TODO: Add Javadocs!
	public void setColorXYZ(final Color3F colorXYZ) {
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
	}
	
//	TODO: Add Javadocs!
	public void setFilterWeightSum(final float filterWeightSum) {
		this.filterWeightSum = filterWeightSum;
	}
	
//	TODO: Add Javadocs!
	public void setIndex(final int index) {
		this.index = requireRange(index, 0, Integer.MAX_VALUE, "index");
	}
	
//	TODO: Add Javadocs!
	public void setSplatXYZ(final Color3F splatXYZ) {
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
	}
	
//	TODO: Add Javadocs!
	public void setX(final int x) {
		this.x = requireRange(x, 0, Integer.MAX_VALUE, "x");
	}
	
//	TODO: Add Javadocs!
	public void setY(final int y) {
		this.y = requireRange(y, 0, Integer.MAX_VALUE, "y");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static Pixel[] createPixels(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			pixels[i] = new Pixel(colorRGB, Color3F.BLACK, Color3F.BLACK, 0.0F, index, x, y);
		}
		
		return pixels;
	}
	
//	TODO: Add Javadocs!
	public static Pixel[] createPixels(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs) {
		requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGBs, "colorRGBs == null");
		
		requireExact(colorRGBs.length, resolutionX * resolutionY, "colorRGBs.length");
		
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			pixels[i] = new Pixel(Objects.requireNonNull(colorRGBs[i], String.format("colorRGBs[%d] == null", Integer.valueOf(i))), Color3F.BLACK, Color3F.BLACK, 0.0F, index, x, y);
		}
		
		return pixels;
	}
	
//	TODO: Add Javadocs!
	public static void swap(final Pixel pixelA, final Pixel pixelB) {
		final Color3F colorRGBA = pixelA.getColorRGB();
		final Color3F colorRGBB = pixelB.getColorRGB();
		final Color3F colorXYZA = pixelA.getColorXYZ();
		final Color3F colorXYZB = pixelB.getColorXYZ();
		final Color3F splatXYZA = pixelA.getSplatXYZ();
		final Color3F splatXYZB = pixelB.getSplatXYZ();
		
		final float filterWeightSumA = pixelA.getFilterWeightSum();
		final float filterWeightSumB = pixelB.getFilterWeightSum();
		
		final int indexA = pixelA.getIndex();
		final int indexB = pixelB.getIndex();
		final int xA = pixelA.getX();
		final int xB = pixelB.getX();
		final int yA = pixelA.getY();
		final int yB = pixelB.getY();
		
		pixelA.setColorRGB(colorRGBB);
		pixelA.setColorXYZ(colorXYZB);
		pixelA.setSplatXYZ(splatXYZB);
		pixelA.setFilterWeightSum(filterWeightSumB);
		pixelA.setIndex(indexB);
		pixelA.setX(xB);
		pixelA.setY(yB);
		
		pixelB.setColorRGB(colorRGBA);
		pixelB.setColorXYZ(colorXYZA);
		pixelB.setSplatXYZ(splatXYZA);
		pixelB.setFilterWeightSum(filterWeightSumA);
		pixelB.setIndex(indexA);
		pixelB.setX(xA);
		pixelB.setY(yA);
	}
}