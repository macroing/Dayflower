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
package org.dayflower.image;

import static org.dayflower.utility.Doubles.equal;

import java.awt.image.BufferedImage;
import java.util.Objects;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color4D;
import org.dayflower.utility.BufferedImages;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code PixelD} represents a pixel in a {@link PixelImageD} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PixelD {
	private Color3D colorXYZ;
	private Color3D splatXYZ;
	private Color4D colorRGBA;
	private double filterWeightSum;
	private int index;
	private int x;
	private int y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PixelD} instance.
	 * <p>
	 * If either {@code colorRGBA}, {@code colorXYZ} or {@code splatXYZ} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code index}, {@code x} or {@code y} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param colorRGBA the current color of this {@code PixelD} instance
	 * @param colorXYZ the current color of this {@code PixelD} instance that is used by the film
	 * @param splatXYZ the current splat of this {@code PixelD} instance that is used by the film
	 * @param filterWeightSum the filter weight sum of this {@code PixelD} instance that is used by the film
	 * @param index the index of this {@code PixelD} instance
	 * @param x the X-coordinate of this {@code PixelD} instance
	 * @param y the Y-coordinate of this {@code PixelD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code index}, {@code x} or {@code y} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA}, {@code colorXYZ} or {@code splatXYZ} are {@code null}
	 */
	public PixelD(final Color4D colorRGBA, final Color3D colorXYZ, final Color3D splatXYZ, final double filterWeightSum, final int index, final int x, final int y) {
		this.colorRGBA = Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		this.filterWeightSum = filterWeightSum;
		this.index = ParameterArguments.requireRange(index, 0, Integer.MAX_VALUE, "index");
		this.x = ParameterArguments.requireRange(x, 0, Integer.MAX_VALUE, "x");
		this.y = ParameterArguments.requireRange(y, 0, Integer.MAX_VALUE, "y");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the current color of this {@code PixelD} instance.
	 * 
	 * @return the current color of this {@code PixelD} instance
	 */
	public Color3D getColorRGB() {
		return new Color3D(this.colorRGBA);
	}
	
	/**
	 * Returns the current color of this {@code PixelD} instance that is used by the film.
	 * 
	 * @return the current color of this {@code PixelD} instance that is used by the film
	 */
	public Color3D getColorXYZ() {
		return this.colorXYZ;
	}
	
	/**
	 * Returns the current splat of this {@code PixelD} instance that is used by the film.
	 * 
	 * @return the current splat of this {@code PixelD} instance that is used by the film
	 */
	public Color3D getSplatXYZ() {
		return this.splatXYZ;
	}
	
	/**
	 * Returns the current color of this {@code PixelD} instance.
	 * 
	 * @return the current color of this {@code PixelD} instance
	 */
	public Color4D getColorRGBA() {
		return this.colorRGBA;
	}
	
	/**
	 * Returns a copy of this {@code PixelD} instance.
	 * 
	 * @return a copy of this {@code PixelD} instance
	 */
	public PixelD copy() {
		return new PixelD(this.colorRGBA, this.colorXYZ, this.splatXYZ, this.filterWeightSum, this.index, this.x, this.y);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PixelD} instance.
	 * 
	 * @return a {@code String} representation of this {@code PixelD} instance
	 */
	@Override
	public String toString() {
		return String.format("new PixelD(%s, %s, %s, %+.10f, %d, %d, %d)", this.colorRGBA, this.colorXYZ, this.splatXYZ, Double.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code PixelD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PixelD}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PixelD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PixelD}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PixelD)) {
			return false;
		} else if(!Objects.equals(this.colorXYZ, PixelD.class.cast(object).colorXYZ)) {
			return false;
		} else if(!Objects.equals(this.splatXYZ, PixelD.class.cast(object).splatXYZ)) {
			return false;
		} else if(!Objects.equals(this.colorRGBA, PixelD.class.cast(object).colorRGBA)) {
			return false;
		} else if(!equal(this.filterWeightSum, PixelD.class.cast(object).filterWeightSum)) {
			return false;
		} else if(this.index != PixelD.class.cast(object).index) {
			return false;
		} else if(this.x != PixelD.class.cast(object).x) {
			return false;
		} else if(this.y != PixelD.class.cast(object).y) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the filter weight sum of this {@code PixelD} instance that is used by the film.
	 * 
	 * @return the filter weight sum of this {@code PixelD} instance that is used by the film
	 */
	public double getFilterWeightSum() {
		return this.filterWeightSum;
	}
	
	/**
	 * Returns the index of this {@code PixelD} instance.
	 * 
	 * @return the index of this {@code PixelD} instance
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Returns the X-coordinate of this {@code PixelD} instance.
	 * 
	 * @return the X-coordinate of this {@code PixelD} instance
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-coordinate of this {@code PixelD} instance.
	 * 
	 * @return the Y-coordinate of this {@code PixelD} instance
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Returns a hash code for this {@code PixelD} instance.
	 * 
	 * @return a hash code for this {@code PixelD} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.colorXYZ, this.splatXYZ, this.colorRGBA, Double.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Adds {@code colorXYZ} to the color of this {@code PixelD} instance that is used by the film.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ the color to add to this {@code PixelD} instance
	 * @param sampleWeight the sample weight to use
	 * @param filterWeight the filter weight to use
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void addColorXYZ(final Color3D colorXYZ, final double sampleWeight, final double filterWeight) {
		setColorXYZ(Color3D.add(getColorXYZ(), Color3D.multiply(Objects.requireNonNull(colorXYZ, "colorXYZ == null"), sampleWeight * filterWeight)));
		setFilterWeightSum(getFilterWeightSum() + filterWeight);
	}
	
	/**
	 * Adds {@code splatXYZ} to the splat of this {@code PixelD} instance that is used by the film.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param splatXYZ the splat to add to this {@code PixelD} instance
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void addSplatXYZ(final Color3D splatXYZ) {
		setSplatXYZ(Color3D.add(getSplatXYZ(), Objects.requireNonNull(splatXYZ, "splatXYZ == null")));
	}
	
	/**
	 * Sets {@code colorRGB} as the color of this {@code PixelD} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGB the color of this {@code PixelD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public void setColorRGB(final Color3D colorRGB) {
		this.colorRGBA = new Color4D(colorRGB);
	}
	
	/**
	 * Sets {@code colorRGBA} as the color of this {@code PixelD} instance.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the color of this {@code PixelD} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public void setColorRGBA(final Color4D colorRGBA) {
		this.colorRGBA = Objects.requireNonNull(colorRGBA, "colorRGBA == null");
	}
	
	/**
	 * Sets {@code colorXYZ} as the color of this {@code PixelD} instance that is used by the film.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ the color of this {@code PixelD} instance that is used by the film
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void setColorXYZ(final Color3D colorXYZ) {
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
	}
	
	/**
	 * Sets {@code filterWeightSum} as the filter weight sum of this {@code PixelD} instance that is used by the film.
	 * 
	 * @param filterWeightSum the filter weight sum of this {@code PixelD} instance that is used by the film
	 */
	public void setFilterWeightSum(final double filterWeightSum) {
		this.filterWeightSum = filterWeightSum;
	}
	
	/**
	 * Sets {@code index} as the index of this {@code PixelD} instance.
	 * <p>
	 * If {@code index} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of this {@code PixelD} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0}
	 */
	public void setIndex(final int index) {
		this.index = ParameterArguments.requireRange(index, 0, Integer.MAX_VALUE, "index");
	}
	
	/**
	 * Sets {@code splatXYZ} as the splat of this {@code PixelD} instance that is used by the film.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param splatXYZ the splat of this {@code PixelD} instance that is used by the film
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void setSplatXYZ(final Color3D splatXYZ) {
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
	}
	
	/**
	 * Sets {@code x} as the X-coordinate of this {@code PixelD} instance.
	 * <p>
	 * If {@code x} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param x the X-coordinate of this {@code PixelD} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code x} is less than {@code 0}
	 */
	public void setX(final int x) {
		this.x = ParameterArguments.requireRange(x, 0, Integer.MAX_VALUE, "x");
	}
	
	/**
	 * Sets {@code y} as the Y-coordinate of this {@code PixelD} instance.
	 * <p>
	 * If {@code y} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param y the Y-coordinate of this {@code PixelD} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code y} is less than {@code 0}
	 */
	public void setY(final int y) {
		this.y = ParameterArguments.requireRange(y, 0, Integer.MAX_VALUE, "y");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an array with {@code PixelD} instances filled with the {@code Color4D} instances in {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @return an array with {@code PixelD} instances filled with the {@code Color4D} instances in {@code bufferedImage}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public static PixelD[] createPixels(final BufferedImage bufferedImage) {
		final BufferedImage compatibleBufferedImage = BufferedImages.getCompatibleBufferedImage(bufferedImage);
		
		final int resolutionX = compatibleBufferedImage.getWidth();
		final int resolutionY = compatibleBufferedImage.getHeight();
		
		final PixelD[] pixels = new PixelD[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			final Color4D colorRGBA = Color4D.unpack(compatibleBufferedImage.getRGB(x, y));
			
			pixels[i] = new PixelD(colorRGBA, Color3D.BLACK, Color3D.BLACK, 0.0D, index, x, y);
		}
		
		return pixels;
	}
	
	/**
	 * Returns an array with {@code PixelD} instances filled with {@code colorRGBA}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4D} to fill the {@code PixelD} instances with
	 * @return an array with {@code PixelD} instances filled with {@code colorRGBA}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public static PixelD[] createPixels(final int resolutionX, final int resolutionY, final Color4D colorRGBA) {
		ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		final PixelD[] pixels = new PixelD[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			pixels[i] = new PixelD(colorRGBA, Color3D.BLACK, Color3D.BLACK, 0.0D, index, x, y);
		}
		
		return pixels;
	}
	
	/**
	 * Returns an array with {@code PixelD} instances filled with the {@code Color4D} instances in the array {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@link Color4D} instances to fill the {@code PixelD} instances with
	 * @return an array with {@code PixelD} instances filled with the {@code Color4D} instances in the array {@code colorRGBAs}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or at least one of its elements are {@code null}
	 */
	public static PixelD[] createPixels(final int resolutionX, final int resolutionY, final Color4D[] colorRGBAs) {
		ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		
		Objects.requireNonNull(colorRGBAs, "colorRGBAs == null");
		
		ParameterArguments.requireExact(colorRGBAs.length, resolutionX * resolutionY, "colorRGBAs.length");
		
		final PixelD[] pixels = new PixelD[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			pixels[i] = new PixelD(Objects.requireNonNull(colorRGBAs[i], String.format("colorRGBAs[%d] == null", Integer.valueOf(i))), Color3D.BLACK, Color3D.BLACK, 0.0D, index, x, y);
		}
		
		return pixels;
	}
	
	/**
	 * Swaps the data contained in the two {@code PixelD} instances.
	 * <p>
	 * If either {@code pixelA} or {@code pixelB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelA one of the {@code PixelD} instances to swap
	 * @param pixelB one of the {@code PixelD} instances to swap
	 * @throws NullPointerException thrown if, and only if, either {@code pixelA} or {@code pixelB} are {@code null}
	 */
	public static void swap(final PixelD pixelA, final PixelD pixelB) {
		final Color3D colorXYZA = pixelA.getColorXYZ();
		final Color3D colorXYZB = pixelB.getColorXYZ();
		final Color3D splatXYZA = pixelA.getSplatXYZ();
		final Color3D splatXYZB = pixelB.getSplatXYZ();
		
		final Color4D colorRGBAA = pixelA.getColorRGBA();
		final Color4D colorRGBAB = pixelB.getColorRGBA();
		
		final double filterWeightSumA = pixelA.getFilterWeightSum();
		final double filterWeightSumB = pixelB.getFilterWeightSum();
		
		final int indexA = pixelA.getIndex();
		final int indexB = pixelB.getIndex();
		final int xA = pixelA.getX();
		final int xB = pixelB.getX();
		final int yA = pixelA.getY();
		final int yB = pixelB.getY();
		
		pixelA.setColorRGBA(colorRGBAB);
		pixelA.setColorXYZ(colorXYZB);
		pixelA.setSplatXYZ(splatXYZB);
		pixelA.setFilterWeightSum(filterWeightSumB);
		pixelA.setIndex(indexB);
		pixelA.setX(xB);
		pixelA.setY(yB);
		
		pixelB.setColorRGBA(colorRGBAA);
		pixelB.setColorXYZ(colorXYZA);
		pixelB.setSplatXYZ(splatXYZA);
		pixelB.setFilterWeightSum(filterWeightSumA);
		pixelB.setIndex(indexA);
		pixelB.setX(xA);
		pixelB.setY(yA);
	}
}