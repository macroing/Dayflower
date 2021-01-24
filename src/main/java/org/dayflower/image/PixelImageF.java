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
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.filter.Filter2F;
import org.dayflower.filter.MitchellFilter2F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.utility.BufferedImages;

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
	 * new PixelImageF(bufferedImage, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public PixelImageF(final BufferedImage bufferedImage) {
		this(bufferedImage, new MitchellFilter2F());
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
	 * new PixelImageF(resolutionX, resolutionY, colorRGBA, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBA the {@link Color4F} to fill the {@code PixelImageF} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F colorRGBA) {
		this(resolutionX, resolutionY, colorRGBA, new MitchellFilter2F());
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
	 * new PixelImageF(resolutionX, resolutionY, colorRGBAs, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the {@code Color4F} instances to fill the {@code PixelImageF} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or at least one of its elements are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F[] colorRGBAs) {
		this(resolutionX, resolutionY, colorRGBAs, new MitchellFilter2F());
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
	public PixelImageF(final int resolutionX, final int resolutionY, final Color4F[] colorRGBAs, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, colorRGBAs);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances read from {@code colorRGBAs}.
	 * <p>
	 * If {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, colorRGBAs, arrayComponentOrder, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to read {@code Color4F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or {@code arrayComponentOrder} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final byte[] colorRGBAs, final ArrayComponentOrder arrayComponentOrder) {
		this(resolutionX, resolutionY, colorRGBAs, arrayComponentOrder, new MitchellFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances read from {@code colorRGBAs}.
	 * <p>
	 * If {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs}, {@code arrayComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to read {@code Color4F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @param filter the {@link Filter2F} to use
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs}, {@code arrayComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final byte[] colorRGBAs, final ArrayComponentOrder arrayComponentOrder, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, Color4F.arrayRead(colorRGBAs, arrayComponentOrder));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances read from {@code colorRGBAs}.
	 * <p>
	 * If {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, colorRGBAs, arrayComponentOrder, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to read {@code Color4F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or {@code arrayComponentOrder} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final int[] colorRGBAs, final ArrayComponentOrder arrayComponentOrder) {
		this(resolutionX, resolutionY, colorRGBAs, arrayComponentOrder, new MitchellFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances read from {@code colorRGBAs}.
	 * <p>
	 * If {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs}, {@code arrayComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to read {@code Color4F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @param filter the {@link Filter2F} to use
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBAs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color4F.arrayRead(colorRGBAs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs}, {@code arrayComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final int[] colorRGBAs, final ArrayComponentOrder arrayComponentOrder, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, Color4F.arrayRead(colorRGBAs, arrayComponentOrder));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances unpacked from {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImageF(resolutionX, resolutionY, colorRGBAs, packedIntComponentOrder, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to unpack {@code Color4F} instances from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs} or {@code packedIntComponentOrder} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final int[] colorRGBAs, final PackedIntComponentOrder packedIntComponentOrder) {
		this(resolutionX, resolutionY, colorRGBAs, packedIntComponentOrder, new MitchellFilter2F());
	}
	
	/**
	 * Constructs a new {@code PixelImageF} instance filled with {@link Color4F} instances unpacked from {@code colorRGBAs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBAs}, {@code packedIntComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBAs the array to unpack {@code Color4F} instances from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @param filter the {@link Filter2F} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBAs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBAs}, {@code packedIntComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImageF(final int resolutionX, final int resolutionY, final int[] colorRGBAs, final PackedIntComponentOrder packedIntComponentOrder, final Filter2F filter) {
		super(resolutionX, resolutionY);
		
		this.pixels = PixelF.createPixels(resolutionX, resolutionY, Color4F.arrayUnpack(colorRGBAs, packedIntComponentOrder));
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
	@Override
	public Color4F getColorRGBA(final int index, final PixelOperation pixelOperation) {
		return getPixel(index, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4F.BLACK);
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
	@Override
	public Color4F getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		return getPixel(x, y, pixelOperation).map(pixel -> pixel.getColorRGBA()).orElse(Color4F.BLACK);
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
	public Optional<PixelF> getPixel(final int x, final int y, final PixelOperation pixelOperation) {
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
	 * Returns a copy of this {@code PixelImageF} instance.
	 * 
	 * @return a copy of this {@code PixelImageF} instance
	 */
	@Override
	public PixelImageF copy() {
		return new PixelImageF(this);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PixelImageF} instance.
	 * 
	 * @return a {@code String} representation of this {@code PixelImageF} instance
	 */
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
	 * Returns a {@code byte[]} representation of this {@code PixelImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code byte[]} representation of this {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	@Override
	public byte[] toByteArray(final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		final int resolution = getResolution();
		
		final byte[] byteArray = new byte[resolution * arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final PixelF pixel = this.pixels[i];
			
			final Color4F colorRGBA = pixel.getColorRGBA();
			
			final byte r = colorRGBA.getAsByteR();
			final byte g = colorRGBA.getAsByteG();
			final byte b = colorRGBA.getAsByteB();
			final byte a = colorRGBA.getAsByteA();
			
			if(arrayComponentOrder.hasOffsetR()) {
				byteArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetR()] = r;
			}
			
			if(arrayComponentOrder.hasOffsetG()) {
				byteArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetG()] = g;
			}
			
			if(arrayComponentOrder.hasOffsetB()) {
				byteArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetB()] = b;
			}
			
			if(arrayComponentOrder.hasOffsetA()) {
				byteArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetA()] = a;
			}
		}
		
		return byteArray;
	}
	
	/**
	 * Returns a hash code for this {@code PixelImageF} instance.
	 * 
	 * @return a hash code for this {@code PixelImageF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), this.filter, Integer.valueOf(Arrays.hashCode(this.pixels)), Integer.valueOf(Arrays.hashCode(this.filterTable)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code PixelImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return an {@code int[]} representation of this {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	@Override
	public int[] toIntArray(final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution * arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final PixelF pixel = this.pixels[i];
			
			final Color4F colorRGBA = pixel.getColorRGBA();
			
			final int r = colorRGBA.getAsIntR();
			final int g = colorRGBA.getAsIntG();
			final int b = colorRGBA.getAsIntB();
			final int a = colorRGBA.getAsIntA();
			
			if(arrayComponentOrder.hasOffsetR()) {
				intArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetR()] = r;
			}
			
			if(arrayComponentOrder.hasOffsetG()) {
				intArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetG()] = g;
			}
			
			if(arrayComponentOrder.hasOffsetB()) {
				intArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetB()] = b;
			}
			
			if(arrayComponentOrder.hasOffsetA()) {
				intArray[i * arrayComponentOrder.getComponentCount() + arrayComponentOrder.getOffsetA()] = a;
			}
		}
		
		return intArray;
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
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			intArray[i] = this.pixels[i].pack(packedIntComponentOrder);
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
					PixelF pixel = pixels[filterYResolutionX + filterX];
					pixel.addColorXYZ(colorXYZ, sampleWeight, filterTable[filterOffsetYOffsetFilterTableSize + filterOffsetX[filterX - minimumFilterX]]);
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
	public void filmAddSplatXYZ(final int x, final int y, final Color3F splatXYZ) {
		Objects.requireNonNull(splatXYZ, "splatXYZ == null");
		
		getPixel(x, y).ifPresent(pixel -> pixel.addSplatXYZ(splatXYZ));
	}
	
	/**
	 * Clears the film.
	 */
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
	public void filmRender() {
		filmRender(1.0F);
	}
	
	/**
	 * Renders the film to the image.
	 * 
	 * @param splatScale the splat scale to use
	 */
	public void filmRender(final float splatScale) {
		for(final PixelF pixel : this.pixels) {
			Color3F colorRGB = Color3F.convertXYZToRGBUsingPBRT(pixel.getColorXYZ());
			Color3F splatRGB = Color3F.convertXYZToRGBUsingPBRT(pixel.getSplatXYZ());
			
			if(!isZero(pixel.getFilterWeightSum())) {
				colorRGB = Color3F.multiplyAndSaturateNegative(colorRGB, 1.0F / pixel.getFilterWeightSum());
			}
			
			splatRGB = Color3F.multiply(splatRGB, splatScale);
			colorRGB = Color3F.add(colorRGB, splatRGB);
			colorRGB = Color3F.redoGammaCorrectionPBRT(colorRGB);
			
			pixel.setColorRGB(colorRGB);
		}
	}
	
	/**
	 * Flips this {@code PixelImageF} instance along the X-axis.
	 */
	public void flipX() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int xL = 0, xR = resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < resolutionY; y++) {
				PixelF.swap(this.pixels[y * resolutionX + xL], this.pixels[y * resolutionX + xR]);
			}
		}
	}
	
	/**
	 * Flips this {@code PixelImageF} instance along the Y-axis.
	 */
	public void flipY() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int yT = 0, yB = resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < resolutionX; x++) {
				PixelF.swap(this.pixels[yT * resolutionX + x], this.pixels[yB * resolutionX + x]);
			}
		}
	}
	
	/**
	 * Inverts this {@code PixelImageF} instance.
	 */
	public void invert() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorRGB(Color3F.invert(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Multiplies this {@code PixelImageF} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel33F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final PixelImageF pixelImage = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + -1), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + -1), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + -1), convolutionKernel.getElement13()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + +0), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + +0), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + +0), convolutionKernel.getElement23()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + +1), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + +1), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + +1), convolutionKernel.getElement33()));
				
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
	 * Multiplies this {@code PixelImageF} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel55F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final PixelImageF pixelImage = copy();
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				Color3F colorRGB = Color3F.BLACK;
				
//				Row #1:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -2, y + -2), convolutionKernel.getElement11()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + -2), convolutionKernel.getElement12()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + -2), convolutionKernel.getElement13()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + -2), convolutionKernel.getElement14()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +2, y + -2), convolutionKernel.getElement15()));
				
//				Row #2:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -2, y + -1), convolutionKernel.getElement21()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + -1), convolutionKernel.getElement22()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + -1), convolutionKernel.getElement23()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + -1), convolutionKernel.getElement24()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +2, y + -1), convolutionKernel.getElement25()));
				
//				Row #3:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -2, y + +0), convolutionKernel.getElement31()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + +0), convolutionKernel.getElement32()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + +0), convolutionKernel.getElement33()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + +0), convolutionKernel.getElement34()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +2, y + +0), convolutionKernel.getElement35()));
				
//				Row #4:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -2, y + +1), convolutionKernel.getElement41()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + +1), convolutionKernel.getElement42()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + +1), convolutionKernel.getElement43()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + +1), convolutionKernel.getElement44()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +2, y + +1), convolutionKernel.getElement45()));
				
//				Row #5:
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -2, y + +2), convolutionKernel.getElement51()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + -1, y + +2), convolutionKernel.getElement52()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +0, y + +2), convolutionKernel.getElement53()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +1, y + +2), convolutionKernel.getElement54()));
				colorRGB = Color3F.add(colorRGB, Color3F.multiply(pixelImage.getColorRGB(x + +2, y + +2), convolutionKernel.getElement55()));
				
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
	 * Redoes gamma correction on this {@code PixelImageF} instance using PBRT.
	 */
	public void redoGammaCorrectionPBRT() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Redoes gamma correction on this {@code PixelImageF} instance using SRGB.
	 */
	public void redoGammaCorrectionSRGB() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	@Override
	public void setColorRGBA(final Color4F colorRGBA, final int index, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			this.pixels[indexTransformed].setColorRGBA(colorRGBA);
		}
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	@Override
	public void setColorRGBA(final Color4F colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			this.pixels[index].setColorRGBA(colorRGBA);
		}
	}
	
	/**
	 * Undoes gamma correction on this {@code PixelImageF} instance using PBRT.
	 */
	public void undoGammaCorrectionPBRT() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Undoes gamma correction on this {@code PixelImageF} instance using SRGB.
	 */
	public void undoGammaCorrectionSRGB() {
		for(final PixelF pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Updates this {@code PixelImageF} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
	 * <p>
	 * If {@code function} is {@code null} or {@code function.apply(oldColor)} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.update(function, 0, 0, pixelImage.getResolutionX(), pixelImage.getResolutionY());
	 * }
	 * </pre>
	 * 
	 * @param function a {@code Function} that updates the old {@code Color3F} instances with new {@code Color3F} instances
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or {@code function.apply(oldColor)} returns {@code null}
	 */
	public void update(final Function<Color3F, Color3F> function) {
		update(function, 0, 0, getResolutionX(), getResolutionY());
	}
	
	/**
	 * Updates this {@code PixelImageF} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
	 * <p>
	 * If either {@code function}, {@code a} or {@code b} are {@code null} or {@code function.apply(oldColor)} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.update(function, a.getX(), a.getY(), b.getX(), b.getY());
	 * }
	 * </pre>
	 * 
	 * @param function a {@code Function} that updates the old {@code Color3F} instances with new {@code Color3F} instances
	 * @param a a {@link Point2I} instance with the minimum (inclusive) or maximum (exclusive) X-coordinate and the minimum (inclusive) or maximum (exclusive) Y-coordinate of the region to be updated
	 * @param b a {@code Point2I} instance with the maximum (exclusive) or minimum (inclusive) X-coordinate and the maximum (exclusive) or minimum (inclusive) Y-coordinate of the region to be updated
	 * @throws NullPointerException thrown if, and only if, either {@code function}, {@code a} or {@code b} are {@code null} or {@code function.apply(oldColor)} returns {@code null}
	 */
	public void update(final Function<Color3F, Color3F> function, final Point2I a, final Point2I b) {
		update(function, a.getX(), a.getY(), b.getX(), b.getY());
	}
	
	/**
	 * Updates this {@code PixelImageF} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
	 * <p>
	 * If {@code function} is {@code null} or {@code function.apply(oldColor)} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param function a {@code Function} that updates the old {@code Color3F} instances with new {@code Color3F} instances
	 * @param aX the minimum (inclusive) or maximum (exclusive) X-coordinate of the region to be updated
	 * @param aY the minimum (inclusive) or maximum (exclusive) Y-coordinate of the region to be updated
	 * @param bX the maximum (exclusive) or minimum (inclusive) X-coordinate of the region to be updated
	 * @param bY the maximum (exclusive) or minimum (inclusive) Y-coordinate of the region to be updated
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or {@code function.apply(oldColor)} returns {@code null}
	 */
	public void update(final Function<Color3F, Color3F> function, final int aX, final int aY, final int bX, final int bY) {
		Objects.requireNonNull(function, "function == null");
		
		final int minimumX = max(min(aX, bX), 0);
		final int minimumY = max(min(aY, bY), 0);
		final int maximumX = min(max(aX, bX), getResolutionX());
		final int maximumY = min(max(aY, bY), getResolutionY());
		
		for(int y = minimumY; y < maximumY; y++) {
			for(int x = minimumX; x < maximumX; x++) {
				final Color3F oldColor = getColorRGB(x, y);
				final Color3F newColor = function.apply(oldColor);
				
				if(newColor == null) {
					throw new NullPointerException(String.format("function.apply(%s) == null: x=%s, y=%s", oldColor, Integer.valueOf(x), Integer.valueOf(y)));
				}
				
				setColorRGB(newColor, x, y);
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends {@code pixelImageA} and {@code pixelImageB} using the factor {@code 0.5F}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.blend(pixelImageA, pixelImageB, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param pixelImageA one of the {@code PixelImageF} instances to blend
	 * @param pixelImageB one of the {@code PixelImageF} instances to blend
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImageF blend(final PixelImageF pixelImageA, final PixelImageF pixelImageB) {
		return blend(pixelImageA, pixelImageB, 0.5F);
	}
	
	/**
	 * Blends {@code pixelImageA} and {@code pixelImageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImageF.blend(pixelImageA, pixelImageB, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param pixelImageA one of the {@code PixelImageF} instances to blend
	 * @param pixelImageB one of the {@code PixelImageF} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImageF blend(final PixelImageF pixelImageA, final PixelImageF pixelImageB, final float t) {
		return blend(pixelImageA, pixelImageB, t, t, t);
	}
	
	/**
	 * Blends {@code pixelImageA} and {@code pixelImageB} using the factors {@code tComponent1}, {@code tComponent2} and {@code tComponent3}.
	 * <p>
	 * Returns a new {@code PixelImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImageA one of the {@code PixelImageF} instances to blend
	 * @param pixelImageB one of the {@code PixelImageF} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 1 in the blending process
	 * @param tComponent3 the factor to use for component 1 in the blending process
	 * @return a new {@code PixelImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImageF blend(final PixelImageF pixelImageA, final PixelImageF pixelImageB, final float tComponent1, final float tComponent2, final float tComponent3) {
		final int pixelImageAResolutionX = pixelImageA.getResolutionX();
		final int pixelImageAResolutionY = pixelImageA.getResolutionY();
		
		final int pixelImageBResolutionX = pixelImageB.getResolutionX();
		final int pixelImageBResolutionY = pixelImageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(pixelImageAResolutionX, pixelImageBResolutionX);
		final int pixelImageCResolutionY = max(pixelImageAResolutionY, pixelImageBResolutionY);
		
		final PixelImageF pixelImageC = new PixelImageF(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color3F colorA = pixelImageA.getColorRGB(x, y);
				final Color3F colorB = pixelImageB.getColorRGB(x, y);
				final Color3F colorC = Color3F.blend(colorA, colorB, tComponent1, tComponent2, tComponent3);
				
				pixelImageC.setColorRGB(colorC, x, y);
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
	public static PixelImageF createScreenCapture(final Rectangle2I rectangle) {
		return new PixelImageF(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
	}
	
	/**
	 * Returns a {@code PixelImageF} that shows the difference between {@code pixelImageA} and {@code pixelImageB} with {@code Color3F.BLACK}.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImageA an {@code PixelImageF} instance
	 * @param pixelImageB an {@code PixelImageF} instance
	 * @return a {@code PixelImage} that shows the difference between {@code pixelImageA} and {@code pixelImageB} with {@code Color3F.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImageF difference(final PixelImageF pixelImageA, final PixelImageF pixelImageB) {
		final int resolutionX = max(pixelImageA.getResolutionX(), pixelImageB.getResolutionX());
		final int resolutionY = max(pixelImageA.getResolutionY(), pixelImageB.getResolutionY());
		
		final PixelImageF pixelImageC = new PixelImageF(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color3F colorA = pixelImageA.getColorRGB(x, y);
				final Color3F colorB = pixelImageB.getColorRGB(x, y);
				final Color3F colorC = colorA.equals(colorB) ? colorA : Color3F.BLACK;
				
				pixelImageC.setColorRGB(colorC, x, y);
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
	 * PixelImageF.load(file, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageF load(final File file) {
		return load(file, new MitchellFilter2F());
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
	public static PixelImageF load(final File file, final Filter2F filter) {
		try {
			return new PixelImageF(BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null"))), filter);
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
	 * PixelImageF.load(pathname, new MitchellFilter2F());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code PixelImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImageF load(final String pathname) {
		return load(pathname, new MitchellFilter2F());
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
	public static PixelImageF load(final String pathname, final Filter2F filter) {
		return load(new File(pathname), filter);
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
	public static PixelImageF random(final int resolutionX, final int resolutionY) {
		return new PixelImageF(resolutionX, resolutionY, Color4F.arrayRandom(resolutionX * resolutionY));
	}
}