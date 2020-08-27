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

import static org.dayflower.image.Filter.FILTER_TABLE_SIZE;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.ceil;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Ints.min;
import static org.dayflower.util.Ints.requireRange;
import static org.dayflower.util.Ints.toInt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;
import java.util.Objects;

import javax.imageio.ImageIO;

/**
 * An {@code Image} is an image that can be drawn to.
 * <p>
 * This class stores individual pixels as {@link Pixel} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Image {
	private final Filter filter;
	private final Pixel[] pixels;
	private final float[] filterTable;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code Color3F.BLACK}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(800, 800);
	 * }
	 * </pre>
	 */
	public Image() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code Color3F.BLACK}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(resolutionX, resolutionY, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public Image(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code colorRGB}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(resolutionX, resolutionY, colorRGB, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGB the {@link Color3F} to fill the {@code Image} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		this(resolutionX, resolutionY, colorRGB, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code colorRGB}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGB} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGB the {@link Color3F} to fill the {@code Image} with
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code filter} are {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color3F colorRGB, final Filter filter) {
		this.resolutionX = requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, colorRGB);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with the {@code Color3F} instances in the array {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(resolutionX, resolutionY, colorRGBs, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the {@link Color3F} instances to fill the {@code Image} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or at least one of its elements are {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs) {
		this(resolutionX, resolutionY, colorRGBs, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with the {@code Color3F} instances in the array {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs}, at least one of its elements or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the {@link Color3F} instances to fill the {@code Image} with
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs}, at least one of its elements or {@code filter} are {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs, final Filter filter) {
		this.resolutionX = requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, colorRGBs);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code Image} instance.
	 * 
	 * @return a {@code BufferedImage} representation of this {@code Image} instance
	 */
	public BufferedImage toBufferedImage() {
		final BufferedImage bufferedImage = new BufferedImage(this.resolutionX, this.resolutionY, BufferedImage.TYPE_INT_ARGB);
		
		final int[] dataSource = toIntArrayPackedForm();
		final int[] dataTarget = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
		
		System.arraycopy(dataSource, 0, dataTarget, 0, dataSource.length);
		
		return bufferedImage;
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 */
	public Color3F getColorRGB(final float x, final float y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color3F} instances.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Color3F getColorRGB(final float x, final float y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGB(minimumX, minimumY, pixelOperation);
		}
		
		final Color3F color00 = getColorRGB(minimumX, minimumY, pixelOperation);
		final Color3F color01 = getColorRGB(maximumX, minimumY, pixelOperation);
		final Color3F color10 = getColorRGB(minimumX, maximumY, pixelOperation);
		final Color3F color11 = getColorRGB(maximumX, maximumY, pixelOperation);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color3F color = Color3F.blend(Color3F.blend(color00, color01, xFactor), Color3F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code index}
	 */
	public Color3F getColor(final int index) {
		return getColorRGB(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Color3F getColorRGB(final int index, final PixelOperation pixelOperation) {
		return doGetColorRGBOrDefault(pixelOperation.getIndex(index, this.resolution), Color3F.BLACK);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGB(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 */
	public Color3F getColorRGB(final int x, final int y) {
		return getColorRGB(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color3F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color3F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Color3F getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		return doGetColorRGBOrDefault(pixelOperation.getX(x, this.resolutionX), pixelOperation.getY(y, this.resolutionY), Color3F.BLACK);
	}
	
	/**
	 * Returns the resolution of this {@code Image} instance.
	 * <p>
	 * The resolution of {@code image} can be computed by:
	 * <pre>
	 * {@code
	 * int resolution = image.getResolutionX() * image.getResolutionY();
	 * }
	 * </pre>
	 * 
	 * @return the resolution of this {@code Image} instance
	 */
	public int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis of this {@code Image} instance.
	 * <p>
	 * The resolution of the X-axis is also known as the width.
	 * 
	 * @return the resolution of the X-axis of this {@code Image} instance
	 */
	public int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis of this {@code Image} instance.
	 * <p>
	 * The resolution of the Y-axis is also known as the height.
	 * 
	 * @return the resolution of the Y-axis of this {@code Image} instance
	 */
	public int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance in a packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code Image} instance in a packed form
	 */
	public int[] toIntArrayPackedForm() {
		return toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code Image} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int[] intArray = new int[this.resolution];
		
		for(int i = 0; i < this.resolution; i++) {
			intArray[i] = this.pixels[i].getColorRGB().pack(packedIntComponentOrder);
		}
		
		return intArray;
	}
	
	/**
	 * Clears this {@code Image} instance with a {@link Color3F} of {@code Color3F.BLACK}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(Color3F.BLACK);
	 * }
	 * </pre>
	 */
	public void clear() {
		clear(Color3F.BLACK);
	}
	
	/**
	 * Clears this {@code Image} instance with a {@link Color3F} of {@code colorRGB}.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGB the {@code Color3F} to clear with
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public void clear(final Color3F colorRGB) {
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(colorRGB);
		}
	}
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	public void filmClear() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorXYZ(new Color3F());
			pixel.setSplatXYZ(new Color3F());
			pixel.setFilterWeightSum(0.0F);
		}
	}
	
//	TODO: Add Javadocs!
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
	
//	TODO: Add Javadocs!
	public void flipX() {
		for(int xL = 0, xR = this.resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < this.resolutionY; y++) {
				Pixel.swap(this.pixels[y * this.resolutionX + xL], this.pixels[y * this.resolutionX + xR]);
			}
		}
	}
	
//	TODO: Add Javadocs!
	public void flipY() {
		for(int yT = 0, yB = this.resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < this.resolutionX; x++) {
				Pixel.swap(this.pixels[yT * this.resolutionX + x], this.pixels[yB * this.resolutionX + x]);
			}
		}
	}
	
	/**
	 * Redoes gamma correction on this {@code Image} instance using PBRT.
	 */
	public void redoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Redoes gamma correction on this {@code Image} instance using SRGB.
	 */
	public void redoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public void setColorRGB(final Color3F colorRGB, final int index) {
		setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public void setColorRGB(final Color3F colorRGB, final int index, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int indexTransformed = pixelOperation.getIndex(index, this.resolution);
		
		if(indexTransformed >= 0 && indexTransformed < this.resolution) {
			this.pixels[indexTransformed].setColorRGB(colorRGB);
		}
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor(colorRGB, x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public void setColorRGB(final Color3F colorRGB, final int x, final int y) {
		setColorRGB(colorRGB, x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code x} and {@code y} to {@code colorRGB}.
	 * <p>
	 * If either {@code colorRGB} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code pixelOperation} are {@code null}
	 */
	public void setColorRGB(final Color3F colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int xTransformed = pixelOperation.getX(x, this.resolutionX);
		final int yTransformed = pixelOperation.getY(y, this.resolutionY);
		
		if(xTransformed >= 0 && xTransformed < this.resolutionX && yTransformed >= 0 && yTransformed < this.resolutionY) {
			final int index = yTransformed * this.resolutionX + xTransformed;
			
			this.pixels[index].setColorRGB(colorRGB);
		}
	}
	
	/**
	 * Undoes gamma correction on this {@code Image} instance using PBRT.
	 */
	public void undoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Undoes gamma correction on this {@code Image} instance using SRGB.
	 */
	public void undoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads an {@code Image} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code Image} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Image.load(file, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Image load(final File file) {
		return load(file, new MitchellFilter());
	}
	
	/**
	 * Loads an {@code Image} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code Image} instance.
	 * <p>
	 * If either {@code file} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param filter the {@link Filter} to use
	 * @return a new {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Image load(final File file, final Filter filter) {
		try {
			final BufferedImage bufferedImage = doGetCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null")));
			
			final int resolutionX = bufferedImage.getWidth();
			final int resolutionY = bufferedImage.getHeight();
			
			return unpackFromARGB(resolutionX, resolutionY, DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData(), Objects.requireNonNull(filter, "filter == null"));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
//	TODO: Add Javadocs!
	public static Image load(final String filename) {
		return load(filename, new MitchellFilter());
	}
	
//	TODO: Add Javadocs!
	public static Image load(final String filename, final Filter filter) {
		return load(new File(Objects.requireNonNull(filename, "filename == null")), Objects.requireNonNull(filter, "filter == null"));
	}
	
//	TODO: Add Javadocs!
	public static Image unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB) {
		return unpackFromARGB(resolutionX, resolutionY, imageARGB, new MitchellFilter());
	}
	
//	TODO: Add Javadocs!
	public static Image unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB, final Filter filter) {
		final Image image = new Image(resolutionX, resolutionY, new Color3F(), filter);
		
		for(int i = 0; i < image.resolution; i++) {
			image.setColorRGB(Color3F.unpack(imageARGB[i], PackedIntComponentOrder.ARGB), i);
		}
		
		return image;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color3F doGetColorRGBOrDefault(final int index, final Color3F colorRGB) {
		return index >= 0 && index < this.resolution ? this.pixels[index].getColorRGB() : colorRGB;
	}
	
	private Color3F doGetColorRGBOrDefault(final int x, final int y, final Color3F colorRGB) {
		return x >= 0 && x < this.resolutionX && y >= 0 && y < this.resolutionY ? this.pixels[y * this.resolutionX + x].getColorRGB() : colorRGB;
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