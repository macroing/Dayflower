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
package org.dayflower.image;

import static org.dayflower.filter.Filter2F.FILTER_TABLE_SIZE;
import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Floats.floor;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.color.ColorSpaceF;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.filter.Filter2F;
import org.dayflower.filter.GaussianFilter2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.awt.image.BufferedImages;

/**
 * A {@code PixelImageF} is an {@link ImageF} implementation that stores individual pixels as {@link PixelF} instances.
 * <p>
 * An instance of this class requires a lot of memory. It is therefore not advised to keep multiple instances of it in memory at once, only to retrieve the pixel colors.
 * <p>
 * This class was specifically created for the following reasons:
 * <ul>
 * <li>To render in multiple passes with filtering as anti-aliasing.</li>
 * <li>To create images procedurally.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class PixelImageF extends ImageF {
	private final Filter2F filter;
	private final PixelF[] pixels;
	private final float[] filterTable;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@code Color4F.BLACK}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(800, 800);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public PixelImageF() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(bufferedImage, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final BufferedImage bufferedImage) {
		this(bufferedImage, new GaussianFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code bufferedImage} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param filter the {@link Filter2F} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code bufferedImage} or {@code filter} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final BufferedImage bufferedImage, final Filter2F filter) {
		super(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.pixels = PixelF.createPixels(bufferedImage);
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance from {@code pixelImage}.
	 * <p>
	 * If {@code pixelImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImage a {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final PixelImageF pixelImage) {
		super(pixelImage.getResolutionX(), pixelImage.getResolutionY());
		
		this.filter = pixelImage.filter;
		this.pixels = Arrays.stream(pixelImage.pixels).map(pixel -> pixel.copy()).toArray(PixelF[]::new);
		this.filterTable = pixelImage.filterTable.clone();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@code Color4F.BLACK}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, Color4F.BLACK);
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@code colorRGBA}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, colorRGBA, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4F} to fill the {@code PixelImageF} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F colorRGBA) {
		this(resolutionX, resolutionY, colorRGBA, new GaussianFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@code colorRGBA}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBA} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4F} to fill the {@code PixelImageF} with
	 * @param filter the {@link Filter2F} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code filter} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F colorRGBA, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, colorRGBA);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with the {@link Color4F} instances in the array {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, colorRGBAs, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@code Color4F} instances to fill the {@code PixelImageF} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or at least one of its elements are {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F[] colorRGBAs) {
		this(resolutionX, resolutionY, colorRGBAs, new GaussianFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with the {@link Color4F} instances in the array {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs}, at least one of its elements or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@code Color4F} instances to fill the {@code PixelImageF} with
	 * @param filter the {@link Filter2F} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs}, at least one of its elements or {@code filter} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F[] colorRGBAs, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, colorRGBAs);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Color4F getColorRGBA(final int index, final PixelOperation pixelOperation) {
		return getPixel(index, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4F.BLACK);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4F} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Color4F getColorRGBA(final int x, final int y, final Function<Point2I, Color4F> function) {
		Objects.requireNonNull(function, "function == null");
		
		return getPixel(x, y).map(pixel -> pixel.getColorRGBA()).orElseGet(() -> Objects.requireNonNull(function.apply(new Point2I(x, y))));
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public Color4F getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		return getPixel(x, y, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4F.BLACK);
	}
	
	/**
	 * Returns a {@code List} with all {@link PixelF} instances associated with this {@code PixelImageF} instance.
	 * <p>
	 * Modifications to the returned {@code List} will not affect this {@code PixelImageF} instance.
	 * 
	 * @return a {@code List} with all {@code PixelF} instances associated with this {@code PixelImageF} instance
	 */
//	TODO: Add Unit Tests!
	public List<PixelF> getPixels() {
		return new ArrayList<>(Arrays.asList(this.pixels));
	}
	
	/**
	 * Returns the optional {@link PixelF} located at {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.getPixel(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the {@code PixelF}
	 * @return the optional {@code PixelF} located at {@code index}
	 */
//	TODO: Add Unit Tests!
	public Optional<PixelF> getPixel(final int index) {
		return getPixel(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link PixelF} located at {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the {@code PixelF}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code PixelF} located at {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Optional<PixelF> getPixel(final int index, final PixelOperation pixelOperation) {
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			return Optional.of(this.pixels[indexTransformed]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the optional {@link PixelF} located at {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.getPixel(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @return the optional {@code PixelF} located at {@code x} and {@code y}
	 */
//	TODO: Add Unit Tests!
	public Optional<PixelF> getPixel(final int x, final int y) {
		return getPixel(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link PixelF} located at {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code PixelF} located at {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Optional<PixelF> getPixel(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			return Optional.of(this.pixels[yTransformed * resolutionX + xTransformed]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PixelImageF} instance.
	 * 
	 * @return a {@code String} representation of this {@code PixelImageF} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new PixelImageF(%d, %d, new Color3F[] {...}, %s)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), this.filter);
	}
	
	/**
	 * Compares {@code object} to this {@code PixelImageF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PixelImageF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PixelImageF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PixelImageF}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PixelImageF)) {
			return false;
		} else if(getResolution() != PixelImageF.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != PixelImageF.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != PixelImageF.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Objects.equals(this.filter, PixelImageF.class.cast(object).filter)) {
			return false;
		} else if(!Arrays.equals(this.pixels, PixelImageF.class.cast(object).pixels)) {
			return false;
		} else if(!Arrays.equals(this.filterTable, PixelImageF.class.cast(object).filterTable)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PixelImageF} instance.
	 * 
	 * @return a hash code for this {@code PixelImageF} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), this.filter, Integer.valueOf(Arrays.hashCode(this.pixels)), Integer.valueOf(Arrays.hashCode(this.filterTable)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code PixelImageF} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code PixelImageF} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			intArray[i] = this.pixels[i].getColorRGBA().pack(packedIntComponentOrder);
		}
		
		return intArray;
	}
	
	/**
	 * Adds {@code colorXYZ} to the {@link PixelF} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmAddColorXYZ(x, y, colorXYZ, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @param colorXYZ the color to add
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void filmAddColorXYZ(final float x, final float y, final Color3F colorXYZ) {
		filmAddColorXYZ(x, y, colorXYZ, 1.0F);
	}
	
	/**
	 * Adds {@code colorXYZ} to the {@link PixelF} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @param colorXYZ the color to add
	 * @param sampleWeight the sample weight to use
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void filmAddColorXYZ(final float x, final float y, final Color3F colorXYZ, final float sampleWeight) {
		final Filter2F filter = this.filter;
		
		final PixelF[] pixels = this.pixels;
		
		final float[] filterTable = this.filterTable;
		
		final float filterResolutionX = filter.getResolutionX();
		final float filterResolutionY = filter.getResolutionY();
		final float filterResolutionXReciprocal = filter.getResolutionXReciprocal();
		final float filterResolutionYReciprocal = filter.getResolutionYReciprocal();
		
		final float deltaX = x - 0.5F;
		final float deltaY = y - 0.5F;
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int minimumFilterX = toInt(max(ceil(deltaX - filterResolutionX), 0));
		final int maximumFilterX = toInt(min(floor(deltaX + filterResolutionX), resolutionX - 1));
		final int minimumFilterY = toInt(max(ceil(deltaY - filterResolutionY), 0));
		final int maximumFilterY = toInt(min(floor(deltaY + filterResolutionY), resolutionY - 1));
		final int maximumFilterXMinimumFilterX = maximumFilterX - minimumFilterX;
		final int maximumFilterYMinimumFilterY = maximumFilterY - minimumFilterY;
		
		if(maximumFilterXMinimumFilterX >= 0 && maximumFilterYMinimumFilterY >= 0) {
			final int[] filterOffsetX = new int[maximumFilterXMinimumFilterX + 1];
			final int[] filterOffsetY = new int[maximumFilterYMinimumFilterY + 1];
			
			for(int filterX = minimumFilterX; filterX <= maximumFilterX; filterX++) {
				filterOffsetX[filterX - minimumFilterX] = min(toInt(floor(abs((filterX - deltaX) * filterResolutionXReciprocal * FILTER_TABLE_SIZE))), FILTER_TABLE_SIZE - 1);
			}
			
			for(int filterY = minimumFilterY; filterY <= maximumFilterY; filterY++) {
				filterOffsetY[filterY - minimumFilterY] = min(toInt(floor(abs((filterY - deltaY) * filterResolutionYReciprocal * FILTER_TABLE_SIZE))), FILTER_TABLE_SIZE - 1);
			}
			
			for(int filterY = minimumFilterY; filterY <= maximumFilterY; filterY++) {
				final int filterYResolutionX = filterY * resolutionX;
				final int filterOffsetYOffsetFilterTableSize = filterOffsetY[filterY - minimumFilterY] * FILTER_TABLE_SIZE;
				
				for(int filterX = minimumFilterX; filterX <= maximumFilterX; filterX++) {
					final
					PixelF pixelF = pixels[filterYResolutionX + filterX];
					pixelF.addColorXYZ(colorXYZ, sampleWeight, filterTable[filterOffsetYOffsetFilterTableSize + filterOffsetX[filterX - minimumFilterX]]);
				}
			}
		}
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link PixelF} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmAddSplatXYZ(Ints.toInt(Floats.floor(x)), Ints.toInt(Floats.floor(y)), splatXYZ);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void filmAddSplatXYZ(final float x, final float y, final Color3F splatXYZ) {
		filmAddSplatXYZ(toInt(floor(x)), toInt(floor(y)), splatXYZ);
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link PixelF} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code PixelF}
	 * @param y the Y-coordinate of the {@code PixelF}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public void filmAddSplatXYZ(final int x, final int y, final Color3F splatXYZ) {
		Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		
		getPixel(x, y).ifPresent(pixel -> pixel.addSplatXYZ(splatXYZ));
	}
	
	/**
	 * Clears the film.
	 */
//	TODO: Add Unit Tests!
	public void filmClear() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorXYZ(new Color3F());
			pixel.setSplatXYZ(new Color3F());
			pixel.setFilterWeightSum(0.0F);
		}
	}
	
	/**
	 * Renders the film to the image.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmRender(1.0F);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public void filmRender() {
		filmRender(1.0F);
	}
	
	/**
	 * Renders the film to the image.
	 * 
	 * @param splatScale the splat scale to use
	 */
//	TODO: Add Unit Tests!
	public void filmRender(final float splatScale) {
		final ColorSpaceF colorSpace = ColorSpaceF.getDefault();
		
		for(final PixelF pixel : this.pixels) {
			Color3F colorRGB = colorSpace.convertXYZToRGB(pixel.getColorXYZ());
			
			if(!isZero(pixel.getFilterWeightSum())) {
				colorRGB = Color3F.multiplyAndSaturateNegative(colorRGB, 1.0F / pixel.getFilterWeightSum());
			}
			
			colorRGB = colorSpace.redoGammaCorrection(Color3F.add(colorRGB, Color3F.multiply(colorSpace.convertXYZToRGB(pixel.getSplatXYZ()), splatScale)));
			
			pixel.setColorRGB(colorRGB);
		}
	}
	
	/**
	 * Swaps the pixels represented by {@code indexA} and {@code indexB}.
	 * <p>
	 * If either {@code indexA} or {@code indexB} are less than {@code 0} or greater than or equal to {@code getResolution()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param indexA one of the pixel indices
	 * @param indexB one of the pixel indices
	 * @throws IllegalArgumentException thrown if, and only if, either {@code indexA} or {@code indexB} are less than {@code 0} or greater than or equal to {@code getResolution()}
	 */
//	TODO: Add Unit Tests!
	@Override
	public void swap(final int indexA, final int indexB) {
		ParameterArguments.requireRange(indexA, 0, getResolution() - 1, "indexA");
		ParameterArguments.requireRange(indexB, 0, getResolution() - 1, "indexB");
		
		PixelF.swap(this.pixels[indexA], this.pixels[indexB]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code 0.5F}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.blend(imageA, imageB, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF blend(final ImageF imageA, final ImageF imageB) {
		return blend(imageA, imageB, 0.5F);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.blend(imageA, imageB, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF blend(final ImageF imageA, final ImageF imageB, final float t) {
		return blend(imageA, imageB, t, t, t, t);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factors {@code tComponent1}, {@code tComponent2}, {@code tComponent3} and {@code tComponent4}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF blend(final ImageF imageA, final ImageF imageB, final float tComponent1, final float tComponent2, final float tComponent3, final float tComponent4) {
		final int imageAResolutionX = imageA.getResolutionX();
		final int imageAResolutionY = imageA.getResolutionY();
		
		final int imageBResolutionX = imageB.getResolutionX();
		final int imageBResolutionY = imageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(imageAResolutionX, imageBResolutionX);
		final int pixelImageCResolutionY = max(imageAResolutionY, imageBResolutionY);
		
		final PixelImageF pixelImageC = new PixelImageF(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color4F colorA = imageA.getColorRGBA(x, y);
				final Color4F colorB = imageB.getColorRGBA(x, y);
				final Color4F colorC = Color4F.blend(colorA, colorB, tComponent1, tComponent2, tComponent3, tComponent4);
				
				pixelImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return pixelImageC;
	}
	
	/**
	 * Creates a {@code PixelImageF} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code PixelImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF createScreenCapture(final Rectangle2I rectangle) {
		return new PixelImageF(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
	}
	
	/**
	 * Returns a {@code PixelImageF} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4F.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code ImageF} instance
	 * @param imageB an {@code ImageF} instance
	 * @return a {@code PixelImageF} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4F.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF difference(final ImageF imageA, final ImageF imageB) {
		final int resolutionX = max(imageA.getResolutionX(), imageB.getResolutionX());
		final int resolutionY = max(imageA.getResolutionY(), imageB.getResolutionY());
		
		final PixelImageF pixelImageC = new PixelImageF(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color4F colorA = imageA.getColorRGBA(x, y);
				final Color4F colorB = imageB.getColorRGBA(x, y);
				final Color4F colorC = colorA.equals(colorB) ? colorA : Color4F.BLACK;
				
				pixelImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return pixelImageC;
	}
	
	/**
	 * Loads a {@code PixelImageF} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.load(file, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final File file) {
		return load(file, new GaussianFilter2F());
	}
	
	/**
	 * Loads a {@code PixelImageF} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If either {@code file} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param filter the {@link Filter2F} to use
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final File file, final Filter2F filter) {
		try {
			return new PixelImageF(ImageIO.read(Objects.requireNonNull(file, "file == null")), filter);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads a {@code PixelImageF} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.load(pathname, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final String pathname) {
		return load(pathname, new GaussianFilter2F());
	}
	
	/**
	 * Loads a {@code PixelImageF} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If either {@code pathname} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.load(new File(pathname), filter);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @param filter the {@link Filter2F} to use
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final String pathname, final Filter2F filter) {
		return load(new File(pathname), filter);
	}
	
	/**
	 * Loads a {@code PixelImageF} from the URL represented by {@code uRL}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If {@code uRL} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.load(uRL, new GaussianFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param uRL a {@code URL} that represents the URL to load from
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code uRL} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final URL uRL) {
		return load(uRL, new GaussianFilter2F());
	}
	
	/**
	 * Loads a {@code PixelImageF} from the URL represented by {@code uRL}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If either {@code uRL} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param uRL a {@code URL} that represents the URL to load from
	 * @param filter the {@link Filter2F} to use
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, either {@code uRL} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF load(final URL uRL, final Filter2F filter) {
		try {
			return new PixelImageF(ImageIO.read(Objects.requireNonNull(uRL, "uRL == null")), filter);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Returns a new {@code PixelImageF} instance filled with random {@link Color4F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.random(800, 800);
	 * }
	 * </pre>
	 * 
	 * @return a new {@code PixelImageF} instance filled with random {@code Color4F} instances
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF random() {
		return random(800, 800);
	}
	
	/**
	 * Returns a new {@code PixelImageF} instance filled with random {@link Color4F} instances.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code PixelImageF} instance filled with random {@code Color4F} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static PixelImageF random(final int resolutionX, final int resolutionY) {
		return new PixelImageF(resolutionX, resolutionY, Color4F.arrayRandom(resolutionX * resolutionY));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code PixelImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code PixelImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected PixelImageF newImage(final int resolutionX, final int resolutionY) {
		return new PixelImageF(resolutionX, resolutionY, Color4F.BLACK, this.filter);
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected void putColorRGBA(final Color4F colorRGBA, final int index) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		if(index >= 0 && index < this.pixels.length) {
			this.pixels[index].setColorRGBA(colorRGBA);
		}
	}
}