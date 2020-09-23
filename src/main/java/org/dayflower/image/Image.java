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
import static org.dayflower.util.Ints.max;
import static org.dayflower.util.Ints.min;
import static org.dayflower.util.Ints.requireExact;
import static org.dayflower.util.Ints.requireRange;
import static org.dayflower.util.Ints.toInt;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.geometry.Circle2I;
import org.dayflower.geometry.Line2I;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Rasterizer2I;
import org.dayflower.geometry.Rectangle2I;
import org.dayflower.geometry.Triangle2I;

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
	 * Constructs a new {@code Image} instance from {@code image}.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public Image(final Image image) {
		this.filter = image.filter;
		this.pixels = Arrays.stream(image.pixels).map(pixel -> pixel.copy()).toArray(Pixel[]::new);
		this.filterTable = image.filterTable.clone();
		this.resolution = image.resolution;
		this.resolutionX = image.resolutionX;
		this.resolutionY = image.resolutionY;
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
	public Color3F getColorRGB(final int index) {
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
		return getPixel(index, pixelOperation).map(pixel -> pixel.getColorRGB()).orElse(Color3F.BLACK);
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
		return getPixel(x, y, pixelOperation).map(pixel -> pixel.getColorRGB()).orElse(Color3F.BLACK);
	}
	
	/**
	 * Returns a copy of this {@code Image} instance.
	 * 
	 * @return a copy of this {@code Image} instance
	 */
	public Image copy() {
		return new Image(this);
	}
	
	/**
	 * Returns the optional {@link Pixel} located at {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getPixel(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the {@code Pixel}
	 * @return the optional {@code Pixel} located at {@code index}
	 */
	public Optional<Pixel> getPixel(final int index) {
		return getPixel(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link Pixel} located at {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the {@code Pixel}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code Pixel} located at {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Optional<Pixel> getPixel(final int index, final PixelOperation pixelOperation) {
		final int resolution = this.resolution;
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			return Optional.of(this.pixels[indexTransformed]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the optional {@link Pixel} located at {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getPixel(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @return the optional {@code Pixel} located at {@code x} and {@code y}
	 */
	public Optional<Pixel> getPixel(final int x, final int y) {
		return getPixel(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link Pixel} located at {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code Pixel} located at {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Optional<Pixel> getPixel(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = this.resolutionX;
		final int resolutionY = this.resolutionY;
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			return Optional.of(this.pixels[y * resolutionX + x]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@link Rectangle2I} with the bounds of this {@code Image} instance.
	 * 
	 * @return a {@code Rectangle2I} with the bounds of this {@code Image} instance
	 */
	public Rectangle2I getBounds() {
		return new Rectangle2I(new Point2I(), new Point2I(this.resolutionX, this.resolutionY));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Image} instance.
	 * 
	 * @return a {@code String} representation of this {@code Image} instance
	 */
	@Override
	public String toString() {
		return String.format("new Image(%d, %d, new Color3F[] {...}, %s)", Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), this.filter);
	}
	
	/**
	 * Compares {@code object} to this {@code Image} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Image}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Image} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Image}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Image)) {
			return false;
		} else if(!Objects.equals(this.filter, Image.class.cast(object).filter)) {
			return false;
		} else if(!Arrays.equals(this.pixels, Image.class.cast(object).pixels)) {
			return false;
		} else if(!Arrays.equals(this.filterTable, Image.class.cast(object).filterTable)) {
			return false;
		} else if(this.resolution != Image.class.cast(object).resolution) {
			return false;
		} else if(this.resolutionX != Image.class.cast(object).resolutionX) {
			return false;
		} else if(this.resolutionY != Image.class.cast(object).resolutionY) {
			return false;
		} else {
			return true;
		}
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
	 * Returns a hash code for this {@code Image} instance.
	 * 
	 * @return a hash code for this {@code Image} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.filter, Integer.valueOf(Arrays.hashCode(this.pixels)), Integer.valueOf(Arrays.hashCode(this.filterTable)), Integer.valueOf(this.resolution), Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY));
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
	
	/**
	 * Draws {@code circle} to this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawCircle(circle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public void drawCircle(final Circle2I circle) {
		drawCircle(circle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code circle} to this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawCircle(circle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGB} are {@code null}
	 */
	public void drawCircle(final Circle2I circle, final Color3F colorRGB) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		drawCircle(circle, pixel -> colorRGB);
	}
	
	/**
	 * Draws {@code circle} to this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code circle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void drawCircle(final Circle2I circle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(function, "function == null");
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius() && x * x + y * y > (circle.getRadius() - 1) * (circle.getRadius() - 1)) {
					final Optional<Pixel> optionalPixel = getPixel(x + circle.getCenter().getX(), y + circle.getCenter().getY());
					
					if(optionalPixel.isPresent()) {
						final
						Pixel pixel = optionalPixel.get();
						pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
					}
				}
			}
		}
	}
	
	/**
	 * Draws {@code line} to this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code line} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawLine(line, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code line} is {@code null}
	 */
	public void drawLine(final Line2I line) {
		drawLine(line, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code line} to this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code line} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawLine(line, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code colorRGB} are {@code null}
	 */
	public void drawLine(final Line2I line, final Color3F colorRGB) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		drawLine(line, pixel -> colorRGB);
	}
	
	/**
	 * Draws {@code line} to this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code line} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void drawLine(final Line2I line, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(function, "function == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(this.resolutionX, this.resolutionY));
		
		final Point2I[] scanLine = Rasterizer2I.rasterize(line, rectangle);
		
		for(final Point2I point : scanLine) {
			final Optional<Pixel> optionalPixel = getPixel(point.getX(), point.getY());
			
			if(optionalPixel.isPresent()) {
				final
				Pixel pixel = optionalPixel.get();
				pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
			}
		}
	}
	
	/**
	 * Draws {@code rectangle} to this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawRectangle(rectangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public void drawRectangle(final Rectangle2I rectangle) {
		drawRectangle(rectangle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawRectangle(rectangle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGB} are {@code null}
	 */
	public void drawRectangle(final Rectangle2I rectangle, final Color3F colorRGB) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		drawRectangle(rectangle, pixel -> colorRGB);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void drawRectangle(final Rectangle2I rectangle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(function, "function == null");
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				if(x == minimumX || x == maximumX || y == minimumY || y == maximumY) {
					final Optional<Pixel> optionalPixel = getPixel(x, y);
					
					if(optionalPixel.isPresent()) {
						final
						Pixel pixel = optionalPixel.get();
						pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
					}
				}
			}
		}
	}
	
	/**
	 * Draws {@code triangle} to this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawTriangle(triangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public void drawTriangle(final Triangle2I triangle) {
		drawTriangle(triangle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code triangle} to this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawTriangle(triangle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGB} are {@code null}
	 */
	public void drawTriangle(final Triangle2I triangle, final Color3F colorRGB) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		drawTriangle(triangle, pixel -> colorRGB);
	}
	
	/**
	 * Draws {@code triangle} to this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code triangle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void drawTriangle(final Triangle2I triangle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(function, "function == null");
		
		drawLine(new Line2I(triangle.getA(), triangle.getB()), function);
		drawLine(new Line2I(triangle.getB(), triangle.getC()), function);
		drawLine(new Line2I(triangle.getC(), triangle.getA()), function);
	}
	
	/**
	 * Fills {@code circle} in this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillCircle(circle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public void fillCircle(final Circle2I circle) {
		fillCircle(circle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code circle} in this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillCircle(circle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGB} are {@code null}
	 */
	public void fillCircle(final Circle2I circle, final Color3F colorRGB) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		fillCircle(circle, pixel -> colorRGB);
	}
	
	/**
	 * Fills {@code circle} in this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code circle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void fillCircle(final Circle2I circle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(function, "function == null");
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius()) {
					final Optional<Pixel> optionalPixel = getPixel(x + circle.getCenter().getX(), y + circle.getCenter().getY());
					
					if(optionalPixel.isPresent()) {
						final
						Pixel pixel = optionalPixel.get();
						pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
					}
				}
			}
		}
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code Image} instance.
	 * <p>
	 * If {@code sourceImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceImage.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code Image} to fill
	 * @throws NullPointerException thrown if, and only if, {@code sourceImage} is {@code null}
	 */
	public void fillImage(final Image sourceImage) {
		fillImage(sourceImage, sourceImage.getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code Image} instance.
	 * <p>
	 * If either {@code sourceImage} or {@code sourceRectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceRectangle, image.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code Image} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code sourceRectangle} are {@code null}
	 */
	public void fillImage(final Image sourceImage, final Rectangle2I sourceBounds) {
		fillImage(sourceImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code Image} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceRectangle} or {@code targetRectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceRectangle, targetRectangle, (sourcePixel, targetPixel) -> sourcePixel.getColorRGB());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code Image} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code Image} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceRectangle} or {@code targetRectangle} are {@code null}
	 */
	public void fillImage(final Image sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		fillImage(sourceImage, sourceBounds, targetBounds, (sourcePixel, targetPixel) -> sourcePixel.getColorRGB());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code Image} instance with {@link Color3F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceRectangle}, {@code targetRectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code Image} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code Image} instance to use
	 * @param biFunction a {@code BiFunction} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceRectangle}, {@code targetRectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public void fillImage(final Image sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final BiFunction<Pixel, Pixel, Color3F> biFunction) {
		Objects.requireNonNull(sourceImage, "sourceImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final Image targetImage = this;
		
		final int sourceMinimumX = sourceBounds.getA().getX();
		final int sourceMinimumY = sourceBounds.getA().getY();
		final int sourceMaximumX = sourceBounds.getC().getX();
		final int sourceMaximumY = sourceBounds.getC().getY();
		final int targetMinimumX = targetBounds.getA().getX();
		final int targetMinimumY = targetBounds.getA().getY();
		final int targetMaximumX = targetBounds.getC().getX();
		final int targetMaximumY = targetBounds.getC().getY();
		
		for(int sourceY = sourceMinimumY, targetY = targetMinimumY; sourceY <= sourceMaximumY && targetY <= targetMaximumY; sourceY++, targetY++) {
			for(int sourceX = sourceMinimumX, targetX = targetMinimumX; sourceX <= sourceMaximumX && targetX <= targetMaximumX; sourceX++, targetX++) {
				final Optional<Pixel> sourceOptionalPixel = sourceImage.getPixel(sourceX, sourceY);
				final Optional<Pixel> targetOptionalPixel = targetImage.getPixel(targetX, targetY);
				
				if(sourceOptionalPixel.isPresent() && targetOptionalPixel.isPresent()) {
					final Pixel sourcePixel = sourceOptionalPixel.get();
					final Pixel targetPixel = targetOptionalPixel.get();
					
					final Color3F colorRGB = biFunction.apply(sourcePixel, targetPixel);
					
					targetPixel.setColorRGB(colorRGB);
				}
			}
		}
	}
	
	/**
	 * Fills {@code rectangle} in this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillRectangle(rectangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Line2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public void fillRectangle(final Rectangle2I rectangle) {
		fillRectangle(rectangle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillRectangle(rectangle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGB} are {@code null}
	 */
	public void fillRectangle(final Rectangle2I rectangle, final Color3F colorRGB) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		fillRectangle(rectangle, pixel -> colorRGB);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void fillRectangle(final Rectangle2I rectangle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(function, "function == null");
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				final Optional<Pixel> optionalPixel = getPixel(x, y);
				
				if(optionalPixel.isPresent()) {
					final
					Pixel pixel = optionalPixel.get();
					pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
				}
			}
		}
	}
	
	/**
	 * Fills {@code triangle} in this {@code Image} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillTriangle(triangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public void fillTriangle(final Triangle2I triangle) {
		fillTriangle(triangle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code triangle} in this {@code Image} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillTriangle(triangle, pixel -> colorRGB);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGB} are {@code null}
	 */
	public void fillTriangle(final Triangle2I triangle, final Color3F colorRGB) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		fillTriangle(triangle, pixel -> colorRGB);
	}
	
	/**
	 * Fills {@code triangle} in this {@code Image} instance with {@link Color3F} instances returned by {@code function} as its color.
	 * <p>
	 * If either {@code triangle} or {@code function} are {@code null} or {@code function} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param function a {@code Function} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code function} are {@code null} or {@code function} returns {@code null}
	 */
	public void fillTriangle(final Triangle2I triangle, final Function<Pixel, Color3F> function) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(function, "function == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(this.resolutionX, this.resolutionY));
		
		final Point2I[][] scanLines = Rasterizer2I.rasterize(triangle, rectangle);
		
		for(final Point2I[] scanLine : scanLines) {
			for(final Point2I point : scanLine) {
				final Optional<Pixel> optionalPixel = getPixel(point.getX(), point.getY());
				
				if(optionalPixel.isPresent()) {
					final
					Pixel pixel = optionalPixel.get();
					pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
				}
			}
		}
	}
	
	/**
	 * Adds {@code colorXYZ} to the {@link Pixel} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.filmAddColorXYZ(x, y, colorXYZ, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @param colorXYZ the color to add
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void filmAddColorXYZ(final float x, final float y, final Color3F colorXYZ) {
		filmAddColorXYZ(x, y, colorXYZ, 1.0F);
	}
	
	/**
	 * Adds {@code colorXYZ} to the {@link Pixel} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @param colorXYZ the color to add
	 * @param sampleWeight the sample weight to use
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void filmAddColorXYZ(final float x, final float y, final Color3F colorXYZ, final float sampleWeight) {
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
					final
					Pixel pixel = pixels[filterY * resolutionX + filterX];
					pixel.addColorXYZ(colorXYZ, sampleWeight, filterTable[filterOffsetY[filterY - minimumFilterY] * FILTER_TABLE_SIZE + filterOffsetX[filterX - minimumFilterX]]);
				}
			}
		}
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link Pixel} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.filmAddSplatXYZ(Ints.toInt(Floats.floor(x)), Ints.toInt(Floats.floor(y)), splatXYZ);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void filmAddSplatXYZ(final float x, final float y, final Color3F splatXYZ) {
		filmAddSplatXYZ(toInt(floor(x)), toInt(floor(y)), splatXYZ);
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link Pixel} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code Pixel}
	 * @param y the Y-coordinate of the {@code Pixel}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void filmAddSplatXYZ(final int x, final int y, final Color3F splatXYZ) {
		Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		
		getPixel(x, y).ifPresent(pixel -> pixel.addSplatXYZ(splatXYZ));
	}
	
	/**
	 * Clears the film.
	 */
	public void filmClear() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorXYZ(new Color3F());
			pixel.setSplatXYZ(new Color3F());
			pixel.setFilterWeightSum(0.0F);
		}
	}
	
	/**
	 * Renders the film to the image.
	 * 
	 * @param splatScale the splat scale to use
	 */
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
	
	/**
	 * Flips this {@code Image} instance along the X-axis.
	 */
	public void flipX() {
		for(int xL = 0, xR = this.resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < this.resolutionY; y++) {
				Pixel.swap(this.pixels[y * this.resolutionX + xL], this.pixels[y * this.resolutionX + xR]);
			}
		}
	}
	
	/**
	 * Flips this {@code Image} instance along the Y-axis.
	 */
	public void flipY() {
		for(int yT = 0, yB = this.resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < this.resolutionX; x++) {
				Pixel.swap(this.pixels[yT * this.resolutionX + x], this.pixels[yB * this.resolutionX + x]);
			}
		}
	}
	
	/**
	 * Multiplies this {@code Image} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel33F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final Image image = copy();
		
		for(int y = 0; y < this.resolutionY; y++) {
			for(int x = 0; x < this.resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement13()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement23()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement33()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3F.multiply(colorRGB, factor);
				colorRGB = Color3F.add(colorRGB, bias);
				colorRGB = Color3F.minimumTo0(colorRGB);
				colorRGB = Color3F.maximumTo1(colorRGB);
				
				setColorRGB(colorRGB, x, y);
			}
		}
	}
	
	/**
	 * Multiplies this {@code Image} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel55F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final Image image = copy();
		
		for(int y = 0; y < this.resolutionY; y++) {
			for(int x = 0; x < this.resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + -2), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -2), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -2), convolutionKernel.getElement13()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -2), convolutionKernel.getElement14()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + -2), convolutionKernel.getElement15()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + -1), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + -1), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + -1), convolutionKernel.getElement23()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + -1), convolutionKernel.getElement24()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + -1), convolutionKernel.getElement25()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +0), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +0), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +0), convolutionKernel.getElement33()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +0), convolutionKernel.getElement34()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +0), convolutionKernel.getElement35()));
				
//				Row #4:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +1), convolutionKernel.getElement41()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +1), convolutionKernel.getElement42()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +1), convolutionKernel.getElement43()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +1), convolutionKernel.getElement44()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +1), convolutionKernel.getElement45()));
				
//				Row #5:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -2, y + +2), convolutionKernel.getElement51()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + -1, y + +2), convolutionKernel.getElement52()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +0, y + +2), convolutionKernel.getElement53()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +1, y + +2), convolutionKernel.getElement54()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(image.getColorRGB(x + +2, y + +2), convolutionKernel.getElement55()));
				
//				Multiply with the factor and add the bias:
				colorRGB = Color3F.multiply(colorRGB, factor);
				colorRGB = Color3F.add(colorRGB, bias);
				colorRGB = Color3F.minimumTo0(colorRGB);
				colorRGB = Color3F.maximumTo1(colorRGB);
				
				setColorRGB(colorRGB, x, y);
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
	 * Saves this {@code Image} as a .PNG image to the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void save(final File file) {
		try {
			final File parentFile = file.getParentFile();
			
			if(parentFile != null && !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			
			ImageIO.write(toBufferedImage(), "png", Objects.requireNonNull(file, "file == null"));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saves this {@code Image} as a .PNG image to the file represented by the filename {@code filename}.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param filename a {@code String} that represents the filename of the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void save(final String filename) {
		save(new File(Objects.requireNonNull(filename, "filename == null")));
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
	 * Returns an {@code Image} that shows the difference between {@code imageA} and {@code imageB} with {@code Color3F.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code Image} instance
	 * @param imageB an {@code Image} instance
	 * @return an {@code Image} that shows the difference between {@code imageA} and {@code imageB} with {@code Color3F.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static Image difference(final Image imageA, final Image imageB) {
		final Image imageC = new Image(max(imageA.resolutionX, imageB.resolutionX), max(imageA.resolutionY, imageB.resolutionY));
		
		for(int y = 0; y < imageC.resolutionY; y++) {
			for(int x = 0; x < imageC.resolutionX; x++) {
				final Color3F colorA = imageA.getColorRGB(x, y);
				final Color3F colorB = imageB.getColorRGB(x, y);
				final Color3F colorC = colorA.equals(colorB) ? colorA : Color3F.BLACK;
				
				imageC.setColorRGB(colorC, x, y);
			}
		}
		
		return imageC;
	}
	
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
	
	/**
	 * Loads an {@code Image} from the file represented by the filename {@code filename}.
	 * <p>
	 * Returns a new {@code Image} instance.
	 * <p>
	 * If {@code filename} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Image.load(filename, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param filename a {@code String} that represents the filename of the file to load from
	 * @return a new {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code filename} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Image load(final String filename) {
		return load(filename, new MitchellFilter());
	}
	
	/**
	 * Loads an {@code Image} from the file represented by the filename {@code filename}.
	 * <p>
	 * Returns a new {@code Image} instance.
	 * <p>
	 * If either {@code filename} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param filename a {@code String} that represents the filename of the file to load from
	 * @param filter the {@link Filter} to use
	 * @return a new {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code filename} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Image load(final String filename, final Filter filter) {
		return load(new File(Objects.requireNonNull(filename, "filename == null")), Objects.requireNonNull(filter, "filter == null"));
	}
	
	/**
	 * Returns a new {@code Image} instance filled with random {@link Color3F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Image.random(800, 800);
	 * }
	 * </pre>
	 * 
	 * @return a new {@code Image} instance filled with random {@code Color3F} instances
	 */
	public static Image random() {
		return random(800, 800);
	}
	
	/**
	 * Returns a new {@code Image} instance filled with random {@link Color3F} instances.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code Image} instance filled with random {@code Color3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public static Image random(final int resolutionX, final int resolutionY) {
		return new Image(resolutionX, resolutionY, Color3F.arrayRandom(resolutionX * resolutionY));
	}
	
	/**
	 * Unpacks {@code imageARGB} into an {@code Image} instance using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Returns an {@code Image} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code imageARGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Image.unpackFromARGB(resolutionX, resolutionY, imageARGB, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param imageARGB an {@code int[]} with {@code int} values representing colors in packed form as ARGB
	 * @return an {@code Image} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code imageARGB} is {@code null}
	 */
	public static Image unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB) {
		return unpackFromARGB(resolutionX, resolutionY, imageARGB, new MitchellFilter());
	}
	
	/**
	 * Unpacks {@code imageARGB} into an {@code Image} instance using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Returns an {@code Image} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code imageARGB} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param imageARGB an {@code int[]} with {@code int} values representing colors in packed form as ARGB
	 * @param filter the {@link Filter} to use
	 * @return an {@code Image} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, either {@code imageARGB} or {@code filter} are {@code null}
	 */
	public static Image unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB, final Filter filter) {
		final Image image = new Image(resolutionX, resolutionY, new Color3F(), filter);
		
		requireExact(imageARGB.length, resolutionX * resolutionY, "imageARGB.length");
		
		for(int i = 0; i < image.resolution; i++) {
			image.setColorRGB(Color3F.unpack(imageARGB[i], PackedIntComponentOrder.ARGB), i);
		}
		
		return image;
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