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

import static org.dayflower.filter.Filter2D.FILTER_TABLE_SIZE;
import static org.dayflower.utility.Doubles.abs;
import static org.dayflower.utility.Doubles.ceil;
import static org.dayflower.utility.Doubles.floor;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Ints.max;
import static org.dayflower.utility.Ints.min;
import static org.dayflower.utility.Ints.toInt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color4D;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.filter.Filter2D;
import org.dayflower.filter.MitchellFilter2D;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.java.awt.image.BufferedImages;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code PixelImageD} is an {@link ImageD} implementation that stores individual pixels as {@link PixelD} instances.
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
public final class PixelImageD extends ImageD {
	private final Filter2D filter;
	private final PixelD[] pixels;
	private final double[] filterTable;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with {@code Color4D.BLACK}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageD(800, 800);
	 * }
	 * </pre>
	 */
	public PixelImageD() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageD(bufferedImage, new MitchellFilter2D());
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public PixelImageD(final BufferedImage bufferedImage) {
		this(bufferedImage, new MitchellFilter2D());
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code bufferedImage} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param filter the {@link Filter2D} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code bufferedImage} or {@code filter} are {@code null}
	 */
	public PixelImageD(final BufferedImage bufferedImage, final Filter2D filter) {
		super(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.pixels = PixelD.createPixels(bufferedImage);
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance from {@code pixelImage}.
	 * <p>
	 * If {@code pixelImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImage a {@code PixelImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelImage} is {@code null}
	 */
	public PixelImageD(final PixelImageD pixelImage) {
		super(pixelImage.getResolutionX(), pixelImage.getResolutionY());
		
		this.filter = pixelImage.filter;
		this.pixels = Arrays.stream(pixelImage.pixels).map(pixel -> pixel.copy()).toArray(PixelD[]::new);
		this.filterTable = pixelImage.filterTable.clone();
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with {@code Color4D.BLACK}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageD(resolutionX, resolutionY, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public PixelImageD(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, Color4D.BLACK);
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with {@code colorRGBA}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageD(resolutionX, resolutionY, colorRGBA, new MitchellFilter2D());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4D} to fill the {@code PixelImageD} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public PixelImageD(final int resolutionX, final int resolutionY, final Color4D colorRGBA) {
		this(resolutionX, resolutionY, colorRGBA, new MitchellFilter2D());
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with {@code colorRGBA}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBA} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4D} to fill the {@code PixelImageD} with
	 * @param filter the {@link Filter2D} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code filter} are {@code null}
	 */
	public PixelImageD(final int resolutionX, final int resolutionY, final Color4D colorRGBA, final Filter2D filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelD.createPixels(resolutionX, resolutionY, colorRGBA);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with the {@link Color4D} instances in the array {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageD(resolutionX, resolutionY, colorRGBAs, new MitchellFilter2D());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@code Color4D} instances to fill the {@code PixelImageD} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or at least one of its elements are {@code null}
	 */
	public PixelImageD(final int resolutionX, final int resolutionY, final Color4D[] colorRGBAs) {
		this(resolutionX, resolutionY, colorRGBAs, new MitchellFilter2D());
	}
	
	/**
	 * Constructs a new {@code PixelImageD} instance filled with the {@link Color4D} instances in the array {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs}, at least one of its elements or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@code Color4D} instances to fill the {@code PixelImageD} with
	 * @param filter the {@link Filter2D} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs}, at least one of its elements or {@code filter} are {@code null}
	 */
	public PixelImageD(final int resolutionX, final int resolutionY, final Color4D[] colorRGBAs, final Filter2D filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelD.createPixels(resolutionX, resolutionY, colorRGBAs);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	@Override
	public Color4D getColorRGBA(final int index, final PixelOperation pixelOperation) {
		return getPixel(index, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4D.BLACK);
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code function} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param function a {@code Function} that returns a {@code Color4D} instance if {@code x} or {@code y} are outside the bounds
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null}
	 */
	@Override
	public Color4D getColorRGBA(final int x, final int y, final Function<Point2I, Color4D> function) {
		Objects.requireNonNull(function, "function == null");
		
		return getPixel(x, y).map(pixel -> pixel.getColorRGBA()).orElseGet(() -> Objects.requireNonNull(function.apply(new Point2I(x, y))));
	}
	
	/**
	 * Returns the {@link Color4D} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the {@code Color4D} of the pixel represented by {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	@Override
	public Color4D getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		return getPixel(x, y, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4D.BLACK);
	}
	
	/**
	 * Returns a {@code List} with all {@link PixelD} instances associated with this {@code PixelImageD} instance.
	 * <p>
	 * Modifications to the returned {@code List} will not affect this {@code PixelImageD} instance.
	 * 
	 * @return a {@code List} with all {@code PixelD} instances associated with this {@code PixelImageD} instance
	 */
	public List<PixelD> getPixels() {
		return new ArrayList<>(Arrays.asList(this.pixels));
	}
	
	/**
	 * Returns the optional {@link PixelD} located at {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.getPixel(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the {@code PixelD}
	 * @return the optional {@code PixelD} located at {@code index}
	 */
	public Optional<PixelD> getPixel(final int index) {
		return getPixel(index, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link PixelD} located at {@code index}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param index the index of the {@code PixelD}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code PixelD} located at {@code index}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Optional<PixelD> getPixel(final int index, final PixelOperation pixelOperation) {
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			return Optional.of(this.pixels[indexTransformed]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns the optional {@link PixelD} located at {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.getPixel(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @return the optional {@code PixelD} located at {@code x} and {@code y}
	 */
	public Optional<PixelD> getPixel(final int x, final int y) {
		return getPixel(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the optional {@link PixelD} located at {@code x} and {@code y}.
	 * <p>
	 * If {@code pixelOperation} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @return the optional {@code PixelD} located at {@code x} and {@code y}
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperation} is {@code null}
	 */
	public Optional<PixelD> getPixel(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			return Optional.of(this.pixels[y * resolutionX + x]);
		}
		
		return Optional.empty();
	}
	
	/**
	 * Returns a copy of this {@code PixelImageD} instance.
	 * 
	 * @return a copy of this {@code PixelImageD} instance
	 */
	@Override
	public PixelImageD copy() {
		return new PixelImageD(this);
	}
	
	/**
	 * Returns a copy of this {@code PixelImageD} instance within {@code bounds}.
	 * <p>
	 * If {@code bounds} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bounds a {@link Rectangle2I} instance that represents the bounds within this {@code PixelImageD} instance to copy
	 * @return a copy of this {@code PixelImageD} instance within {@code bounds}
	 * @throws NullPointerException thrown if, and only if, {@code bounds} is {@code null}
	 */
	@Override
	public PixelImageD copy(final Rectangle2I bounds) {
		Objects.requireNonNull(bounds, "bounds == null");
		
		final PixelImageD pixelImageSource = this;
		
		final Rectangle2I boundsSource = pixelImageSource.getBounds();
		
		final Optional<Rectangle2I> optionalBoundsTarget = Rectangle2I.intersection(boundsSource, bounds);
		
		if(optionalBoundsTarget.isPresent()) {
			final Rectangle2I boundsTarget = optionalBoundsTarget.get();
			
			final Point2I originTarget = boundsTarget.getA();
			
			final int originTargetX = originTarget.getX();
			final int originTargetY = originTarget.getY();
			
			final int resolutionX = boundsTarget.getWidth();
			final int resolutionY = boundsTarget.getHeight();
			
			final PixelImageD pixelImageTarget = new PixelImageD(resolutionX, resolutionY, Color4D.BLACK, this.filter);
			
			for(int y = 0; y < resolutionY; y++) {
				for(int x = 0; x < resolutionX; x++) {
					final Optional<PixelD> optionalPixelSource = pixelImageSource.getPixel(x + originTargetX, y + originTargetY);
					final Optional<PixelD> optionalPixelTarget = pixelImageTarget.getPixel(x,                 y);
					
					if(optionalPixelSource.isPresent() && optionalPixelTarget.isPresent()) {
						final PixelD pixelSource = optionalPixelSource.get();
						final PixelD pixelTarget = optionalPixelTarget.get();
						
						pixelTarget.setColorRGBA(pixelSource.getColorRGBA());
						pixelTarget.setColorXYZ(pixelSource.getColorXYZ());
						pixelTarget.setFilterWeightSum(pixelSource.getFilterWeightSum());
						pixelTarget.setSplatXYZ(pixelSource.getSplatXYZ());
					}
				}
			}
			
			return pixelImageTarget;
		}
		
		return new PixelImageD(0, 0);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PixelImageD} instance.
	 * 
	 * @return a {@code String} representation of this {@code PixelImageD} instance
	 */
	@Override
	public String toString() {
		return String.format("new PixelImageD(%d, %d, new Color3F[] {...}, %s)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), this.filter);
	}
	
	/**
	 * Compares {@code object} to this {@code PixelImageD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PixelImageD}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PixelImageD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PixelImageD}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PixelImageD)) {
			return false;
		} else if(getResolution() != PixelImageD.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != PixelImageD.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != PixelImageD.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Objects.equals(this.filter, PixelImageD.class.cast(object).filter)) {
			return false;
		} else if(!Arrays.equals(this.pixels, PixelImageD.class.cast(object).pixels)) {
			return false;
		} else if(!Arrays.equals(this.filterTable, PixelImageD.class.cast(object).filterTable)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code PixelImageD} instance.
	 * 
	 * @return a hash code for this {@code PixelImageD} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), this.filter, Integer.valueOf(Arrays.hashCode(this.pixels)), Integer.valueOf(Arrays.hashCode(this.filterTable)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code PixelImageD} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code PixelImageD} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
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
	 * Adds {@code colorXYZ} to the {@link PixelD} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmAddColorXYZ(x, y, colorXYZ, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @param colorXYZ the color to add
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void filmAddColorXYZ(final double x, final double y, final Color3D colorXYZ) {
		filmAddColorXYZ(x, y, colorXYZ, 1.0D);
	}
	
	/**
	 * Adds {@code colorXYZ} to the {@link PixelD} instances located around {@code x} and {@code y}.
	 * <p>
	 * If {@code colorXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @param colorXYZ the color to add
	 * @param sampleWeight the sample weight to use
	 * @throws NullPointerException thrown if, and only if, {@code colorXYZ} is {@code null}
	 */
	public void filmAddColorXYZ(final double x, final double y, final Color3D colorXYZ, final double sampleWeight) {
		final Filter2D filter = this.filter;
		
		final PixelD[] pixels = this.pixels;
		
		final double[] filterTable = this.filterTable;
		
		final double filterResolutionX = filter.getResolutionX();
		final double filterResolutionY = filter.getResolutionY();
		final double filterResolutionXReciprocal = filter.getResolutionXReciprocal();
		final double filterResolutionYReciprocal = filter.getResolutionYReciprocal();
		
		final double deltaX = x - 0.5D;
		final double deltaY = y - 0.5D;
		
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
					PixelD pixelD = pixels[filterYResolutionX + filterX];
					pixelD.addColorXYZ(colorXYZ, sampleWeight, filterTable[filterOffsetYOffsetFilterTableSize + filterOffsetX[filterX - minimumFilterX]]);
				}
			}
		}
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link PixelD} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmAddSplatXYZ(Ints.toInt(Doubles.floor(x)), Ints.toInt(Doubles.floor(y)), splatXYZ);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void filmAddSplatXYZ(final double x, final double y, final Color3D splatXYZ) {
		filmAddSplatXYZ(toInt(floor(x)), toInt(floor(y)), splatXYZ);
	}
	
	/**
	 * Adds {@code splatXYZ} to the {@link PixelD} instance located at {@code x} and {@code y}.
	 * <p>
	 * If {@code splatXYZ} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param x the X-coordinate of the {@code PixelD}
	 * @param y the Y-coordinate of the {@code PixelD}
	 * @param splatXYZ the splat to add
	 * @throws NullPointerException thrown if, and only if, {@code splatXYZ} is {@code null}
	 */
	public void filmAddSplatXYZ(final int x, final int y, final Color3D splatXYZ) {
		Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		
		getPixel(x, y).ifPresent(pixel -> pixel.addSplatXYZ(splatXYZ));
	}
	
	/**
	 * Clears the film.
	 */
	public void filmClear() {
		for(final PixelD pixel : this.pixels) {
			pixel.setColorXYZ(new Color3D());
			pixel.setSplatXYZ(new Color3D());
			pixel.setFilterWeightSum(0.0D);
		}
	}
	
	/**
	 * Renders the film to the image.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.filmRender(1.0D);
	 * }
	 * </pre>
	 */
	public void filmRender() {
		filmRender(1.0D);
	}
	
	/**
	 * Renders the film to the image.
	 * 
	 * @param splatScale the splat scale to use
	 */
	public void filmRender(final double splatScale) {
		for(final PixelD pixel : this.pixels) {
			Color3D colorRGB = Color3D.convertXYZToRGBUsingPBRT(pixel.getColorXYZ());
			Color3D splatRGB = Color3D.convertXYZToRGBUsingPBRT(pixel.getSplatXYZ());
			
			if(!isZero(pixel.getFilterWeightSum())) {
				colorRGB = Color3D.multiplyAndSaturateNegative(colorRGB, 1.0D / pixel.getFilterWeightSum());
			}
			
			splatRGB = Color3D.multiply(splatRGB, splatScale);
			colorRGB = Color3D.add(colorRGB, splatRGB);
			colorRGB = Color3D.redoGammaCorrectionPBRT(colorRGB);
			
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
	@Override
	public void swap(final int indexA, final int indexB) {
		ParameterArguments.requireRange(indexA, 0, getResolution() - 1, "indexA");
		ParameterArguments.requireRange(indexB, 0, getResolution() - 1, "indexB");
		
		PixelD.swap(this.pixels[indexA], this.pixels[indexB]);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code 0.5D}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.blend(imageA, imageB, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @return a new {@code PixelImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static PixelImageD blend(final ImageD imageA, final ImageD imageB) {
		return blend(imageA, imageB, 0.5D);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.blend(imageA, imageB, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code PixelImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static PixelImageD blend(final ImageD imageA, final ImageD imageB, final double t) {
		return blend(imageA, imageB, t, t, t, t);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factors {@code tComponent1}, {@code tComponent2}, {@code tComponent3} and {@code tComponent4}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code PixelImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static PixelImageD blend(final ImageD imageA, final ImageD imageB, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4) {
		final int imageAResolutionX = imageA.getResolutionX();
		final int imageAResolutionY = imageA.getResolutionY();
		
		final int imageBResolutionX = imageB.getResolutionX();
		final int imageBResolutionY = imageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(imageAResolutionX, imageBResolutionX);
		final int pixelImageCResolutionY = max(imageAResolutionY, imageBResolutionY);
		
		final PixelImageD pixelImageC = new PixelImageD(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = Color4D.blend(colorA, colorB, tComponent1, tComponent2, tComponent3, tComponent4);
				
				pixelImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return pixelImageC;
	}
	
	/**
	 * Creates a {@code PixelImageD} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code PixelImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
	public static PixelImageD createScreenCapture(final Rectangle2I rectangle) {
		return new PixelImageD(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
	}
	
	/**
	 * Returns a {@code PixelImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code ImageD} instance
	 * @param imageB an {@code ImageD} instance
	 * @return a {@code PixelImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static PixelImageD difference(final ImageD imageA, final ImageD imageB) {
		final int resolutionX = max(imageA.getResolutionX(), imageB.getResolutionX());
		final int resolutionY = max(imageA.getResolutionY(), imageB.getResolutionY());
		
		final PixelImageD pixelImageC = new PixelImageD(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = colorA.equals(colorB) ? colorA : Color4D.BLACK;
				
				pixelImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return pixelImageC;
	}
	
	/**
	 * Loads a {@code PixelImageD} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.load(file, new MitchellFilter2D());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code PixelImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageD load(final File file) {
		return load(file, new MitchellFilter2D());
	}
	
	/**
	 * Loads a {@code PixelImageD} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If either {@code file} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param filter the {@link Filter2D} to use
	 * @return a new {@code PixelImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageD load(final File file, final Filter2D filter) {
		try {
			return new PixelImageD(BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null"))), filter);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads a {@code PixelImageD} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.load(pathname, new MitchellFilter2D());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code PixelImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageD load(final String pathname) {
		return load(pathname, new MitchellFilter2D());
	}
	
	/**
	 * Loads a {@code PixelImageD} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If either {@code pathname} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.load(new File(pathname), filter);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @param filter the {@link Filter2D} to use
	 * @return a new {@code PixelImageD} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageD load(final String pathname, final Filter2D filter) {
		return load(new File(pathname), filter);
	}
	
	/**
	 * Returns a new {@code PixelImageD} instance filled with random {@link Color4D} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageD.random(800, 800);
	 * }
	 * </pre>
	 * 
	 * @return a new {@code PixelImageD} instance filled with random {@code Color4D} instances
	 */
	public static PixelImageD random() {
		return random(800, 800);
	}
	
	/**
	 * Returns a new {@code PixelImageD} instance filled with random {@link Color4D} instances.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code PixelImageD} instance filled with random {@code Color4D} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public static PixelImageD random(final int resolutionX, final int resolutionY) {
		return new PixelImageD(resolutionX, resolutionY, Color4D.arrayRandom(resolutionX * resolutionY));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code PixelImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code PixelImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	@Override
	protected PixelImageD newImage(final int resolutionX, final int resolutionY) {
		return new PixelImageD(resolutionX, resolutionY, Color4D.BLACK, this.filter);
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	@Override
	protected void putColorRGBA(final Color4D colorRGBA, final int index) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		if(index >= 0 && index < this.pixels.length) {
			this.pixels[index].setColorRGBA(colorRGBA);
		}
	}
}