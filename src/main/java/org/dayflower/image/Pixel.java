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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * A {@code Pixel} is a pixel of an {@link Image} instance.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Pixel {
	private Color3F colorRGB;
	private Color3F colorXYZ;
	private Color3F splatXYZ;
	private float filterWeightSum;
	private int index;
	private int x;
	private int y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Pixel} instance.
	 * <p>
	 * If either {@code colorRGB}, {@code colorXYZ} or {@code splatXYZ} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code index}, {@code x} or {@code y} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param colorRGB the current color of this {@code Pixel} instance
	 * @param colorXYZ the current color of this {@code Pixel} instance that is used by the film
	 * @param splatXYZ the current splat of this {@code Pixel} instance that is used by the film
	 * @param filterWeightSum the filter weight sum of this {@code Pixel} instance that is used by the film
	 * @param index the index of this {@code Pixel} instance
	 * @param x the X-coordinate of this {@code Pixel} instance
	 * @param y the Y-coordinate of this {@code Pixel} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code index}, {@code x} or {@code y} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB}, {@code colorXYZ} or {@code splatXYZ} are {@code null}
	 */
	public Pixel(final Color3F colorRGB, final Color3F colorXYZ, final Color3F splatXYZ, final float filterWeightSum, final int index, final int x, final int y) {
		this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		this.filterWeightSum = filterWeightSum;
		this.index = requireRange(index, 0, Integer.MAX_VALUE, "index");
		this.x = requireRange(x, 0, Integer.MAX_VALUE, "x");
		this.y = requireRange(y, 0, Integer.MAX_VALUE, "y");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the current color of this {@code Pixel} instance.
	 * 
	 * @return the current color of this {@code Pixel} instance
	 */
	public Color3F getColorRGB() {
		return this.colorRGB;
	}
	
	/**
	 * Returns the current color of this {@code Pixel} instance that is used by the film.
	 * 
	 * @return the current color of this {@code Pixel} instance that is used by the film
	 */
	public Color3F getColorXYZ() {
		return this.colorXYZ;
	}
	
	/**
	 * Returns the current splat of this {@code Pixel} instance that is used by the film.
	 * 
	 * @return the current splat of this {@code Pixel} instance that is used by the film
	 */
	public Color3F getSplatXYZ() {
		return this.splatXYZ;
	}
	
	/**
	 * Returns a copy of this {@code Pixel} instance.
	 * 
	 * @return a copy of this {@code Pixel} instance
	 */
	public Pixel copy() {
		return new Pixel(this.colorRGB, this.colorXYZ, this.splatXYZ, this.filterWeightSum, this.index, this.x, this.y);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Pixel} instance.
	 * 
	 * @return a {@code String} representation of this {@code Pixel} instance
	 */
	@Override
	public String toString() {
		return String.format("new Pixel(%s, %s, %s, %+.10f, %d, %d, %d)", this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Compares {@code object} to this {@code Pixel} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Pixel}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Pixel} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Pixel}, and their respective values are equal, {@code false} otherwise
	 */
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
	
	/**
	 * Returns the filter weight sum of this {@code Pixel} instance that is used by the film.
	 * 
	 * @return the filter weight sum of this {@code Pixel} instance that is used by the film
	 */
	public float getFilterWeightSum() {
		return this.filterWeightSum;
	}
	
	/**
	 * Returns the index of this {@code Pixel} instance.
	 * 
	 * @return the index of this {@code Pixel} instance
	 */
	public int getIndex() {
		return this.index;
	}
	
	/**
	 * Returns the X-coordinate of this {@code Pixel} instance.
	 * 
	 * @return the X-coordinate of this {@code Pixel} instance
	 */
	public int getX() {
		return this.x;
	}
	
	/**
	 * Returns the Y-coordinate of this {@code Pixel} instance.
	 * 
	 * @return the Y-coordinate of this {@code Pixel} instance
	 */
	public int getY() {
		return this.y;
	}
	
	/**
	 * Returns a hash code for this {@code Pixel} instance.
	 * 
	 * @return a hash code for this {@code Pixel} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.colorRGB, this.colorXYZ, this.splatXYZ, Float.valueOf(this.filterWeightSum), Integer.valueOf(this.index), Integer.valueOf(this.x), Integer.valueOf(this.y));
	}
	
	/**
	 * Adds {@code colorXYZ} to the color of this {@code Pixel} instance that is used by the film.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ the color to add to this {@code Pixel} instance
	 * @param sampleWeight the sample weight to use
	 * @param filterWeight the filter weight to use
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void addColorXYZ(final Color3F colorXYZ, final float sampleWeight, final float filterWeight) {
		setColorXYZ(Color3F.add(getColorXYZ(), Color3F.multiply(Objects.requireNonNull(colorXYZ, "colorXYZ == null"), sampleWeight * filterWeight)));
		setFilterWeightSum(getFilterWeightSum() + filterWeight);
	}
	
	/**
	 * Adds {@code splatXYZ} to the splat of this {@code Pixel} instance that is used by the film.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param splatXYZ the splat to add to this {@code Pixel} instance
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void addSplatXYZ(final Color3F splatXYZ) {
		setSplatXYZ(Color3F.add(getSplatXYZ(), Objects.requireNonNull(splatXYZ, "splatXYZ == null")));
	}
	
	/**
	 * Sets {@code colorRGB} as the color of this {@code Pixel} instance.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGB the color of this {@code Pixel} instance
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public void setColorRGB(final Color3F colorRGB) {
		this.colorRGB = Objects.requireNonNull(colorRGB, "colorRGB == null");
	}
	
	/**
	 * Sets {@code colorXYZ} as the color of this {@code Pixel} instance that is used by the film.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorXYZ the color of this {@code Pixel} instance that is used by the film
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void setColorXYZ(final Color3F colorXYZ) {
		this.colorXYZ = Objects.requireNonNull(colorXYZ, "colorXYZ == null");
	}
	
	/**
	 * Sets {@code filterWeightSum} as the filter weight sum of this {@code Pixel} instance that is used by the film.
	 * 
	 * @param filterWeightSum the filter weight sum of this {@code Pixel} instance that is used by the film
	 */
	public void setFilterWeightSum(final float filterWeightSum) {
		this.filterWeightSum = filterWeightSum;
	}
	
	/**
	 * Sets {@code index} as the index of this {@code Pixel} instance.
	 * <p>
	 * If {@code index} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param index the index of this {@code Pixel} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code index} is less than {@code 0}
	 */
	public void setIndex(final int index) {
		this.index = requireRange(index, 0, Integer.MAX_VALUE, "index");
	}
	
	/**
	 * Sets {@code splatXYZ} as the splat of this {@code Pixel} instance that is used by the film.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param splatXYZ the splat of this {@code Pixel} instance that is used by the film
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void setSplatXYZ(final Color3F splatXYZ) {
		this.splatXYZ = Objects.requireNonNull(splatXYZ, "splatXYZ == null");
	}
	
	/**
	 * Sets {@code x} as the X-coordinate of this {@code Pixel} instance.
	 * <p>
	 * If {@code x} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param x the X-coordinate of this {@code Pixel} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code x} is less than {@code 0}
	 */
	public void setX(final int x) {
		this.x = requireRange(x, 0, Integer.MAX_VALUE, "x");
	}
	
	/**
	 * Sets {@code y} as the Y-coordinate of this {@code Pixel} instance.
	 * <p>
	 * If {@code y} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param y the Y-coordinate of this {@code Pixel} instance
	 * @throws IllegalArgumentException thrown if, and only if, {@code y} is less than {@code 0}
	 */
	public void setY(final int y) {
		this.y = requireRange(y, 0, Integer.MAX_VALUE, "y");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns an array with {@code Pixel} instances filled with the {@code Color3F} instances in {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @return an array with {@code Pixel} instances filled with the {@code Color3F} instances in {@code bufferedImage}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public static Pixel[] createPixels(final BufferedImage bufferedImage) {
		final BufferedImage compatibleBufferedImage = doGetCompatibleBufferedImage(bufferedImage);
		
		final int resolutionX = compatibleBufferedImage.getWidth();
		final int resolutionY = compatibleBufferedImage.getHeight();
		
		final Pixel[] pixels = new Pixel[resolutionX * resolutionY];
		
		for(int i = 0; i < pixels.length; i++) {
			final int index = i;
			final int x = index % resolutionX;
			final int y = index / resolutionX;
			
			final Color3F colorRGB = new Color3F(compatibleBufferedImage.getRGB(x, y));
			
			pixels[i] = new Pixel(colorRGB, Color3F.BLACK, Color3F.BLACK, 0.0F, index, x, y);
		}
		
		return pixels;
	}
	
	/**
	 * Returns an array with {@code Pixel} instances filled with {@code colorRGB}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGB the {@link Color3F} to fill the {@code Pixel} instances with
	 * @return an array with {@code Pixel} instances filled with {@code colorRGB}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
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
	
	/**
	 * Returns an array with {@code Pixel} instances filled with the {@code Color3F} instances in the array {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the {@link Color3F} instances to fill the {@code Pixel} instances with
	 * @return an array with {@code Pixel} instances filled with the {@code Color3F} instances in the array {@code colorRGBs}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or at least one of its elements are {@code null}
	 */
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
	
	/**
	 * Swaps the data contained in the two {@code Pixel} instances.
	 * <p>
	 * If either {@code pixelA} or {@code pixelB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelA one of the {@code Pixel} instances to swap
	 * @param pixelB one of the {@code Pixel} instances to swap
	 * @throws NullPointerException thrown if, and only if, either {@code pixelA} or {@code pixelB} are {@code null}
	 */
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static BufferedImage doGetCompatibleBufferedImage(final BufferedImage bufferedImage) {
		final int compatibleType = BufferedImage.TYPE_INT_ARGB;
		
		if(bufferedImage.getType() == compatibleType) {
			return bufferedImage;
		}
		
		final BufferedImage compatibleBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), compatibleType);
		
		final
		Graphics2D graphics2D = compatibleBufferedImage.createGraphics();
		graphics2D.drawImage(bufferedImage, 0, 0, null);
		
		return compatibleBufferedImage;
	}
}