/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
import static org.dayflower.util.Floats.floor;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Ints.max;
import static org.dayflower.util.Ints.min;
import static org.dayflower.util.Ints.toInt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.rasterizer.Rasterizer2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.dayflower.util.BufferedImages;
import org.dayflower.util.ParameterArguments;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

/**
 * A {@code PixelImage} is an {@link Image} implementation that stores individual pixels as {@link Pixel} instances.
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
public final class PixelImage implements Image {
	private final Filter filter;
	private final Pixel[] pixels;
	private final float[] filterTable;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@code Color3F.BLACK}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(800, 800);
	 * }
	 * </pre>
	 */
	public PixelImage() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(bufferedImage, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public PixelImage(final BufferedImage bufferedImage) {
		this(bufferedImage, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance from {@code bufferedImage}.
	 * <p>
	 * If either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code bufferedImage} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code bufferedImage.getWidth()}, {@code bufferedImage.getHeight()} or {@code bufferedImage.getWidth() * bufferedImage.getHeight()} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code bufferedImage} or {@code filter} are {@code null}
	 */
	public PixelImage(final BufferedImage bufferedImage, final Filter filter) {
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.pixels = Pixel.createPixels(bufferedImage);
		this.filterTable = filter.createFilterTable();
		this.resolution = ParameterArguments.requireRange(bufferedImage.getWidth() * bufferedImage.getHeight(), 0, Integer.MAX_VALUE, "bufferedImage.getWidth() * bufferedImage.getHeight()");
		this.resolutionX = ParameterArguments.requireRange(bufferedImage.getWidth(), 0, Integer.MAX_VALUE, "bufferedImage.getWidth()");
		this.resolutionY = ParameterArguments.requireRange(bufferedImage.getHeight(), 0, Integer.MAX_VALUE, "bufferedImage.getHeight()");
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance from {@code pixelImage}.
	 * <p>
	 * If {@code pixelImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImage a {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelImage} is {@code null}
	 */
	public PixelImage(final PixelImage pixelImage) {
		this.filter = pixelImage.filter;
		this.pixels = Arrays.stream(pixelImage.pixels).map(pixel -> pixel.copy()).toArray(Pixel[]::new);
		this.filterTable = pixelImage.filterTable.clone();
		this.resolution = pixelImage.resolution;
		this.resolutionX = pixelImage.resolutionX;
		this.resolutionY = pixelImage.resolutionY;
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@code Color3F.BLACK}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public PixelImage(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, Color3F.BLACK);
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@code colorRGB}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, colorRGB, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGB the {@link Color3F} to fill the {@code PixelImage} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final Color3F colorRGB) {
		this(resolutionX, resolutionY, colorRGB, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@code colorRGB}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGB} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGB the {@link Color3F} to fill the {@code PixelImage} with
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGB} or {@code filter} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final Color3F colorRGB, final Filter filter) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, colorRGB);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with the {@link Color3F} instances in the array {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or at least one of its elements are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, colorRGBs, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the {@code Color3F} instances to fill the {@code PixelImage} with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or at least one of its elements are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs) {
		this(resolutionX, resolutionY, colorRGBs, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with the {@link Color3F} instances in the array {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs}, at least one of its elements or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the {@code Color3F} instances to fill the {@code PixelImage} with
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs}, at least one of its elements or {@code filter} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final Color3F[] colorRGBs, final Filter filter) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, colorRGBs);
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances read from {@code colorRGBs}.
	 * <p>
	 * If {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, colorRGBs, arrayComponentOrder, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to read {@code Color3F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or {@code arrayComponentOrder} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final byte[] colorRGBs, final ArrayComponentOrder arrayComponentOrder) {
		this(resolutionX, resolutionY, colorRGBs, arrayComponentOrder, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances read from {@code colorRGBs}.
	 * <p>
	 * If {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs}, {@code arrayComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to read {@code Color3F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @param filter the {@link Filter} to use
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs}, {@code arrayComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final byte[] colorRGBs, final ArrayComponentOrder arrayComponentOrder, final Filter filter) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, Color3F.arrayRead(colorRGBs, arrayComponentOrder));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances read from {@code colorRGBs}.
	 * <p>
	 * If {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, colorRGBs, arrayComponentOrder, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to read {@code Color3F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or {@code arrayComponentOrder} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final int[] colorRGBs, final ArrayComponentOrder arrayComponentOrder) {
		this(resolutionX, resolutionY, colorRGBs, arrayComponentOrder, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances read from {@code colorRGBs}.
	 * <p>
	 * If {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}, an
	 * {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs}, {@code arrayComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to read {@code Color3F} instances from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @param filter the {@link Filter} to use
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code colorRGBs.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or
	 *                                  {@code resolutionX * resolutionY != Color3F.arrayRead(colorRGBs, arrayComponentOrder).length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs}, {@code arrayComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final int[] colorRGBs, final ArrayComponentOrder arrayComponentOrder, final Filter filter) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, Color3F.arrayRead(colorRGBs, arrayComponentOrder));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances unpacked from {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new PixelImage(resolutionX, resolutionY, colorRGBs, packedIntComponentOrder, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to unpack {@code Color3F} instances from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs} or {@code packedIntComponentOrder} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final int[] colorRGBs, final PackedIntComponentOrder packedIntComponentOrder) {
		this(resolutionX, resolutionY, colorRGBs, packedIntComponentOrder, new MitchellFilter());
	}
	
	/**
	 * Constructs a new {@code PixelImage} instance filled with {@link Color3F} instances unpacked from {@code colorRGBs}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code colorRGBs}, {@code packedIntComponentOrder} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param colorRGBs the array to unpack {@code Color3F} instances from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @param filter the {@link Filter} to use
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code resolutionX * resolutionY != colorRGBs.length}
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBs}, {@code packedIntComponentOrder} or {@code filter} are {@code null}
	 */
	public PixelImage(final int resolutionX, final int resolutionY, final int[] colorRGBs, final PackedIntComponentOrder packedIntComponentOrder, final Filter filter) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.pixels = Pixel.createPixels(resolutionX, resolutionY, Color3F.arrayUnpack(colorRGBs, packedIntComponentOrder));
		this.filter = Objects.requireNonNull(filter, "filter == null");
		this.filterTable = filter.createFilterTable();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code PixelImage} instance.
	 * 
	 * @return a {@code BufferedImage} representation of this {@code PixelImage} instance
	 */
	@Override
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
	 * pixelImage.getColorRGB(x, y, PixelOperation.NO_CHANGE);
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
	 * pixelImage.getColorRGB(index, PixelOperation.NO_CHANGE);
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
	 * pixelImage.getColorRGB(x, y, PixelOperation.NO_CHANGE);
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
	 * Finds the bounds for {@code pixelImage} in this {@code PixelImage} instance.
	 * <p>
	 * Returns a {@code List} with all {@link Rectangle2I} bounds found for {@code pixelImage} in this {@code PixelImage} instance.
	 * <p>
	 * If {@code pixelImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImage a {@code PixelImage} instance
	 * @return a {@code List} with all {@code Rectangle2I} bounds found for {@code pixelImage} in this {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelImage} is {@code null}
	 */
	public List<Rectangle2I> findBoundsFor(final PixelImage pixelImage) {
		Objects.requireNonNull(pixelImage, "pixelImage == null");
		
		final List<Rectangle2I> rectangles = new ArrayList<>();
		
		for(int y = 0; y < getResolutionY() - pixelImage.getResolutionY(); y++) {
			for(int x = 0; x < getResolutionX() - pixelImage.getResolutionX(); x++) {
				Rectangle2I rectangle = new Rectangle2I(new Point2I(x, y), new Point2I(x, y));
				
				labelImage:
				if(getColorRGB(x, y).equals(pixelImage.getColorRGB(0, 0))) {
					for(int imageY = 0; imageY < pixelImage.getResolutionY(); imageY++) {
						for(int imageX = 0; imageX < pixelImage.getResolutionX(); imageX++) {
							if(!getColorRGB(x + imageX, y + imageY).equals(pixelImage.getColorRGB(imageX, imageY))) {
								break labelImage;
							}
							
							rectangle = new Rectangle2I(new Point2I(x, y), new Point2I(x + imageX + 1, y + imageY + 1));
						}
					}
					
					rectangles.add(rectangle);
				}
			}
		}
		
		return rectangles;
	}
	
	/**
	 * Returns the optional {@link Pixel} located at {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.getPixel(index, PixelOperation.NO_CHANGE);
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
	 * pixelImage.getPixel(x, y, PixelOperation.NO_CHANGE);
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
	 * Returns a copy of this {@code PixelImage} instance.
	 * 
	 * @return a copy of this {@code PixelImage} instance
	 */
	@Override
	public PixelImage copy() {
		return new PixelImage(this);
	}
	
	/**
	 * Returns a {@link Rectangle2I} with the bounds of this {@code PixelImage} instance.
	 * 
	 * @return a {@code Rectangle2I} with the bounds of this {@code PixelImage} instance
	 */
	public Rectangle2I getBounds() {
		return new Rectangle2I(new Point2I(), new Point2I(this.resolutionX, this.resolutionY));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code PixelImage} instance.
	 * 
	 * @return a {@code String} representation of this {@code PixelImage} instance
	 */
	@Override
	public String toString() {
		return String.format("new PixelImage(%d, %d, new Color3F[] {...}, %s)", Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY), this.filter);
	}
	
	/**
	 * Returns a {@code WritableImage} representation of this {@code PixelImage} instance.
	 * 
	 * @return a {@code WritableImage} representation of this {@code PixelImage} instance
	 */
	@Override
	public WritableImage toWritableImage() {
		final
		WritableImage writableImage = new WritableImage(this.resolutionX, this.resolutionY);
		writableImage.getPixelWriter().setPixels(0, 0, this.resolutionX, this.resolutionY, PixelFormat.getIntArgbInstance(), toIntArrayPackedForm(), 0, this.resolutionX);
		
		return writableImage;
	}
	
	/**
	 * Compares {@code object} to this {@code PixelImage} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code PixelImage}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code PixelImage} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code PixelImage}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof PixelImage)) {
			return false;
		} else if(!Objects.equals(this.filter, PixelImage.class.cast(object).filter)) {
			return false;
		} else if(!Arrays.equals(this.pixels, PixelImage.class.cast(object).pixels)) {
			return false;
		} else if(!Arrays.equals(this.filterTable, PixelImage.class.cast(object).filterTable)) {
			return false;
		} else if(this.resolution != PixelImage.class.cast(object).resolution) {
			return false;
		} else if(this.resolutionX != PixelImage.class.cast(object).resolutionX) {
			return false;
		} else if(this.resolutionY != PixelImage.class.cast(object).resolutionY) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the resolution of this {@code PixelImage} instance.
	 * <p>
	 * The resolution of {@code pixelImage} can be computed by:
	 * <pre>
	 * {@code
	 * int resolution = pixelImage.getResolutionX() * pixelImage.getResolutionY();
	 * }
	 * </pre>
	 * 
	 * @return the resolution of this {@code PixelImage} instance
	 */
	@Override
	public int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis of this {@code PixelImage} instance.
	 * <p>
	 * The resolution of the X-axis is also known as the width.
	 * 
	 * @return the resolution of the X-axis of this {@code PixelImage} instance
	 */
	@Override
	public int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis of this {@code PixelImage} instance.
	 * <p>
	 * The resolution of the Y-axis is also known as the height.
	 * 
	 * @return the resolution of the Y-axis of this {@code PixelImage} instance
	 */
	@Override
	public int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns a hash code for this {@code PixelImage} instance.
	 * 
	 * @return a hash code for this {@code PixelImage} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.filter, Integer.valueOf(Arrays.hashCode(this.pixels)), Integer.valueOf(Arrays.hashCode(this.filterTable)), Integer.valueOf(this.resolution), Integer.valueOf(this.resolutionX), Integer.valueOf(this.resolutionY));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code PixelImage} instance in a packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code PixelImage} instance in a packed form
	 */
	@Override
	public int[] toIntArrayPackedForm() {
		return toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code PixelImage} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code PixelImage} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int[] intArray = new int[this.resolution];
		
		for(int i = 0; i < this.resolution; i++) {
			intArray[i] = this.pixels[i].pack(packedIntComponentOrder);
		}
		
		return intArray;
	}
	
	/**
	 * Clears this {@code PixelImage} instance with a {@link Color3F} of {@code Color3F.BLACK}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.clear(Color3F.BLACK);
	 * }
	 * </pre>
	 */
	public void clear() {
		clear(Color3F.BLACK);
	}
	
	/**
	 * Clears this {@code PixelImage} instance with a {@link Color3F} of {@code colorRGB}.
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
	 * Copies the individual component values of the {@link Color3F} instances in this {@code PixelImage} instance to the {@code byte[]} {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length != pixelImage.getResolution() * ArrayComponentOrder.BGRA.getComponentCount()}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.copyTo(array, ArrayComponentOrder.BGRA);
	 * }
	 * </pre>
	 * 
	 * @param array the {@code byte[]} to copy the individual component values of the {@code Color3F} instances in this {@code PixelImage} instance to
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != pixelImage.getResolution() * ArrayComponentOrder.BGRA.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	@Override
	public void copyTo(final byte[] array) {
		copyTo(array, ArrayComponentOrder.BGRA);
	}
	
	/**
	 * Copies the individual component values of the {@link Color3F} instances in this {@code PixelImage} instance to the {@code byte[]} {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length != pixelImage.getResolution() * arrayComponentOrder.getComponentCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the {@code byte[]} to copy the individual component values of the {@code Color3F} instances in this {@code PixelImage} instance to
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} to copy the components to {@code array} in the correct order
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != pixelImage.getResolution() * arrayComponentOrder.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	@Override
	public void copyTo(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length, this.pixels.length * arrayComponentOrder.getComponentCount(), "array");
		
		for(int i = 0, j = 0; i < this.pixels.length; i++, j += arrayComponentOrder.getComponentCount()) {
			final Color3F colorRGB = this.pixels[i].getColorRGB();
			
			if(arrayComponentOrder.hasOffsetR()) {
				array[j + arrayComponentOrder.getOffsetR()] = colorRGB.getAsByteR();
			}
			
			if(arrayComponentOrder.hasOffsetG()) {
				array[j + arrayComponentOrder.getOffsetG()] = colorRGB.getAsByteG();
			}
			
			if(arrayComponentOrder.hasOffsetB()) {
				array[j + arrayComponentOrder.getOffsetB()] = colorRGB.getAsByteB();
			}
			
			if(arrayComponentOrder.hasOffsetA()) {
				array[j + arrayComponentOrder.getOffsetA()] = (byte)(255);
			}
		}
	}
	
	/**
	 * Draws {@code circle} to this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.drawCircle(circle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public void drawCircle(final Circle2I circle) {
		drawCircle(circle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code circle} to this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.drawCircle(circle, pixel -> colorRGB);
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
	 * Draws {@code circle} to this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
	 * Draws {@code line} to this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code line} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.drawLine(line, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code line} is {@code null}
	 */
	public void drawLine(final Line2I line) {
		drawLine(line, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code line} to this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code line} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.drawLine(line, pixel -> colorRGB);
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
	 * Draws {@code line} to this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
		
		final Point2I[] scanline = Rasterizer2I.rasterize(line, rectangle);
		
		for(final Point2I point : scanline) {
			final Optional<Pixel> optionalPixel = getPixel(point.getX(), point.getY());
			
			if(optionalPixel.isPresent()) {
				final
				Pixel pixel = optionalPixel.get();
				pixel.setColorRGB(Objects.requireNonNull(function.apply(pixel)));
			}
		}
	}
	
	/**
	 * Draws {@code rectangle} to this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.drawRectangle(rectangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public void drawRectangle(final Rectangle2I rectangle) {
		drawRectangle(rectangle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.drawRectangle(rectangle, pixel -> colorRGB);
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
	 * Draws {@code rectangle} to this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
	 * Draws {@code triangle} to this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.drawTriangle(triangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public void drawTriangle(final Triangle2I triangle) {
		drawTriangle(triangle, Color3F.BLACK);
	}
	
	/**
	 * Draws {@code triangle} to this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.drawTriangle(triangle, pixel -> colorRGB);
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
	 * Draws {@code triangle} to this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
	 * Fills {@code circle} in this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.fillCircle(circle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public void fillCircle(final Circle2I circle) {
		fillCircle(circle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code circle} in this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.fillCircle(circle, pixel -> colorRGB);
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
	 * Fills {@code circle} in this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
	 * Fills {@code sourcePixelImage} in this {@code PixelImage} instance.
	 * <p>
	 * If {@code sourcePixelImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.fillPixelImage(sourcePixelImage, sourcePixelImage.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourcePixelImage the {@code PixelImage} to fill
	 * @throws NullPointerException thrown if, and only if, {@code sourcePixelImage} is {@code null}
	 */
	public void fillPixelImage(final PixelImage sourcePixelImage) {
		fillPixelImage(sourcePixelImage, sourcePixelImage.getBounds());
	}
	
	/**
	 * Fills {@code sourcePixelImage} in this {@code PixelImage} instance.
	 * <p>
	 * If either {@code sourcePixelImage} or {@code sourceRectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.fillPixelImage(sourcePixelImage, sourceRectangle, pixelImage.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourcePixelImage the {@code PixelImage} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourcePixelImage} to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourcePixelImage} or {@code sourceRectangle} are {@code null}
	 */
	public void fillPixelImage(final PixelImage sourcePixelImage, final Rectangle2I sourceBounds) {
		fillPixelImage(sourcePixelImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourcePixelImage} in this {@code PixelImage} instance.
	 * <p>
	 * If either {@code sourcePixelImage}, {@code sourceRectangle} or {@code targetRectangle} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.fillPixelImage(sourcePixelImage, sourceRectangle, targetRectangle, (sourcePixel, targetPixel) -> sourcePixel.getColorRGB());
	 * }
	 * </pre>
	 * 
	 * @param sourcePixelImage the {@code PixelImage} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourcePixelImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code PixelImage} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourcePixelImage}, {@code sourceRectangle} or {@code targetRectangle} are {@code null}
	 */
	public void fillPixelImage(final PixelImage sourcePixelImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		fillPixelImage(sourcePixelImage, sourceBounds, targetBounds, (sourcePixel, targetPixel) -> sourcePixel.getColorRGB());
	}
	
	/**
	 * Fills {@code sourcePixelImage} in this {@code PixelImage} instance with {@link Color3F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code sourcePixelImage}, {@code sourceRectangle}, {@code targetRectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourcePixelImage the {@code PixelImage} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourcePixelImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code PixelImage} instance to use
	 * @param biFunction a {@code BiFunction} that returns {@code Color3F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code sourcePixelImage}, {@code sourceRectangle}, {@code targetRectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public void fillPixelImage(final PixelImage sourcePixelImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final BiFunction<Pixel, Pixel, Color3F> biFunction) {
		Objects.requireNonNull(sourcePixelImage, "sourcePixelImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final PixelImage targetPixelImage = this;
		
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
				final Optional<Pixel> sourceOptionalPixel = sourcePixelImage.getPixel(sourceX, sourceY);
				final Optional<Pixel> targetOptionalPixel = targetPixelImage.getPixel(targetX, targetY);
				
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
	 * Fills {@code rectangle} in this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.fillRectangle(rectangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Line2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public void fillRectangle(final Rectangle2I rectangle) {
		fillRectangle(rectangle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.fillRectangle(rectangle, pixel -> colorRGB);
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
	 * Fills {@code rectangle} in this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
	 * Fills {@code triangle} in this {@code PixelImage} instance with {@code Color3F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * pixelImage.fillTriangle(triangle, Color3F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public void fillTriangle(final Triangle2I triangle) {
		fillTriangle(triangle, Color3F.BLACK);
	}
	
	/**
	 * Fills {@code triangle} in this {@code PixelImage} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * pixelImage.fillTriangle(triangle, pixel -> colorRGB);
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
	 * Fills {@code triangle} in this {@code PixelImage} instance with {@link Color3F} instances returned by {@code function} as its color.
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
		
		final Point2I[][] scanlines = Rasterizer2I.rasterize(triangle, rectangle);
		
		for(final Point2I[] scanline : scanlines) {
			for(final Point2I point : scanline) {
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
	 * pixelImage.filmAddColorXYZ(x, y, colorXYZ, 1.0F);
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
					Pixel pixel = pixels[filterYResolutionX + filterX];
					pixel.addColorXYZ(colorXYZ, sampleWeight, filterTable[filterOffsetYOffsetFilterTableSize + filterOffsetX[filterX - minimumFilterX]]);
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
	 * pixelImage.filmAddSplatXYZ(Ints.toInt(Floats.floor(x)), Ints.toInt(Floats.floor(y)), splatXYZ);
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
		for(final Pixel pixel : this.pixels) {
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
	 * Flips this {@code PixelImage} instance along the X-axis.
	 */
	public void flipX() {
		for(int xL = 0, xR = this.resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < this.resolutionY; y++) {
				Pixel.swap(this.pixels[y * this.resolutionX + xL], this.pixels[y * this.resolutionX + xR]);
			}
		}
	}
	
	/**
	 * Flips this {@code PixelImage} instance along the Y-axis.
	 */
	public void flipY() {
		for(int yT = 0, yB = this.resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < this.resolutionX; x++) {
				Pixel.swap(this.pixels[yT * this.resolutionX + x], this.pixels[yB * this.resolutionX + x]);
			}
		}
	}
	
	/**
	 * Inverts this {@code PixelImage} instance.
	 */
	public void invert() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.invert(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Multiplies this {@code PixelImage} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel33F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel33F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final PixelImage pixelImage = copy();
		
		for(int y = 0; y < this.resolutionY; y++) {
			for(int x = 0; x < this.resolutionX; x++) {
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
	 * Multiplies this {@code PixelImage} instance with {@code convolutionKernel}.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param convolutionKernel a {@link ConvolutionKernel55F} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public void multiply(final ConvolutionKernel55F convolutionKernel) {
		final Color3F factor = new Color3F(convolutionKernel.getFactor());
		final Color3F bias = new Color3F(convolutionKernel.getBias());
		
		final PixelImage pixelImage = copy();
		
		for(int y = 0; y < this.resolutionY; y++) {
			for(int x = 0; x < this.resolutionX; x++) {
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
	 * Redoes gamma correction on this {@code PixelImage} instance using PBRT.
	 */
	public void redoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Redoes gamma correction on this {@code PixelImage} instance using SRGB.
	 */
	public void redoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.redoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Saves this {@code PixelImage} as a .PNG image to the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
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
	 * Saves this {@code PixelImage} as a .PNG image to the file represented by the pathname {@code pathname}.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.save(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	@Override
	public void save(final String pathname) {
		save(new File(pathname));
	}
	
	/**
	 * Sets the {@link Color3F} of the pixel represented by {@code index} to {@code colorRGB}.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * pixelImage.setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
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
	 * pixelImage.setColor(colorRGB, x, y, PixelOperation.NO_CHANGE);
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
	 * Undoes gamma correction on this {@code PixelImage} instance using PBRT.
	 */
	public void undoGammaCorrectionPBRT() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionPBRT(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Undoes gamma correction on this {@code PixelImage} instance using SRGB.
	 */
	public void undoGammaCorrectionSRGB() {
		for(final Pixel pixel : this.pixels) {
			pixel.setColorRGB(Color3F.undoGammaCorrectionSRGB(pixel.getColorRGB()));
		}
	}
	
	/**
	 * Updates this {@code PixelImage} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
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
	 * Updates this {@code PixelImage} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
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
	 * Updates this {@code PixelImage} instance with new {@link Color3F} instances by applying {@code function} to the old {@code Color3F} instances.
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
		final int maximumX = min(max(aX, bX), this.resolutionX);
		final int maximumY = min(max(aY, bY), this.resolutionY);
		
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
	 * Returns a new {@code PixelImage} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.blend(pixelImageA, pixelImageB, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param pixelImageA one of the {@code PixelImage} instances to blend
	 * @param pixelImageB one of the {@code PixelImage} instances to blend
	 * @return a new {@code PixelImage} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImage blend(final PixelImage pixelImageA, final PixelImage pixelImageB) {
		return blend(pixelImageA, pixelImageB, 0.5F);
	}
	
	/**
	 * Blends {@code pixelImageA} and {@code pixelImageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code PixelImage} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.blend(pixelImageA, pixelImageB, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param pixelImageA one of the {@code PixelImage} instances to blend
	 * @param pixelImageB one of the {@code PixelImage} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code PixelImage} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImage blend(final PixelImage pixelImageA, final PixelImage pixelImageB, final float t) {
		return blend(pixelImageA, pixelImageB, t, t, t);
	}
	
	/**
	 * Blends {@code pixelImageA} and {@code pixelImageB} using the factors {@code tComponent1}, {@code tComponent2} and {@code tComponent3}.
	 * <p>
	 * Returns a new {@code PixelImage} instance with the result of the blend operation.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImageA one of the {@code PixelImage} instances to blend
	 * @param pixelImageB one of the {@code PixelImage} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 1 in the blending process
	 * @param tComponent3 the factor to use for component 1 in the blending process
	 * @return a new {@code PixelImage} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImage blend(final PixelImage pixelImageA, final PixelImage pixelImageB, final float tComponent1, final float tComponent2, final float tComponent3) {
		final int pixelImageAResolutionX = pixelImageA.resolutionX;
		final int pixelImageAResolutionY = pixelImageA.resolutionY;
		
		final int pixelImageBResolutionX = pixelImageB.resolutionX;
		final int pixelImageBResolutionY = pixelImageB.resolutionY;
		
		final int pixelImageCResolutionX = max(pixelImageAResolutionX, pixelImageBResolutionX);
		final int pixelImageCResolutionY = max(pixelImageAResolutionY, pixelImageBResolutionY);
		
		final PixelImage pixelImageC = new PixelImage(pixelImageCResolutionX, pixelImageCResolutionY);
		
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
	 * Creates a {@code PixelImage} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code PixelImage} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code PixelImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
	public static PixelImage createScreenCapture(final Rectangle2I rectangle) {
		return new PixelImage(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
	}
	
	/**
	 * Returns a {@code PixelImage} that shows the difference between {@code pixelImageA} and {@code pixelImageB} with {@code Color3F.BLACK}.
	 * <p>
	 * If either {@code pixelImageA} or {@code pixelImageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param pixelImageA an {@code PixelImage} instance
	 * @param pixelImageB an {@code PixelImage} instance
	 * @return a {@code PixelImage} that shows the difference between {@code pixelImageA} and {@code pixelImageB} with {@code Color3F.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code pixelImageA} or {@code pixelImageB} are {@code null}
	 */
	public static PixelImage difference(final PixelImage pixelImageA, final PixelImage pixelImageB) {
		final PixelImage pixelImageC = new PixelImage(max(pixelImageA.resolutionX, pixelImageB.resolutionX), max(pixelImageA.resolutionY, pixelImageB.resolutionY));
		
		for(int y = 0; y < pixelImageC.resolutionY; y++) {
			for(int x = 0; x < pixelImageC.resolutionX; x++) {
				final Color3F colorA = pixelImageA.getColorRGB(x, y);
				final Color3F colorB = pixelImageB.getColorRGB(x, y);
				final Color3F colorC = colorA.equals(colorB) ? colorA : Color3F.BLACK;
				
				pixelImageC.setColorRGB(colorC, x, y);
			}
		}
		
		return pixelImageC;
	}
	
	/**
	 * Loads a {@code PixelImage} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImage} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.load(file, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImage load(final File file) {
		return load(file, new MitchellFilter());
	}
	
	/**
	 * Loads a {@code PixelImage} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code PixelImage} instance.
	 * <p>
	 * If either {@code file} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @param filter the {@link Filter} to use
	 * @return a new {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImage load(final File file, final Filter filter) {
		try {
			final BufferedImage bufferedImage = BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null")));
			
			final int resolutionX = bufferedImage.getWidth();
			final int resolutionY = bufferedImage.getHeight();
			
			return unpackFromARGB(resolutionX, resolutionY, DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData(), Objects.requireNonNull(filter, "filter == null"));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads a {@code PixelImage} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImage} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.load(pathname, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImage load(final String pathname) {
		return load(pathname, new MitchellFilter());
	}
	
	/**
	 * Loads a {@code PixelImage} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code PixelImage} instance.
	 * <p>
	 * If either {@code pathname} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.load(new File(pathname), filter);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @param filter the {@link Filter} to use
	 * @return a new {@code PixelImage} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code filter} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static PixelImage load(final String pathname, final Filter filter) {
		return load(new File(pathname), filter);
	}
	
	/**
	 * Returns a new {@code PixelImage} instance filled with random {@link Color3F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.random(800, 800);
	 * }
	 * </pre>
	 * 
	 * @return a new {@code PixelImage} instance filled with random {@code Color3F} instances
	 */
	public static PixelImage random() {
		return random(800, 800);
	}
	
	/**
	 * Returns a new {@code PixelImage} instance filled with random {@link Color3F} instances.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code PixelImage} instance filled with random {@code Color3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public static PixelImage random(final int resolutionX, final int resolutionY) {
		return new PixelImage(resolutionX, resolutionY, Color3F.arrayRandom(resolutionX * resolutionY));
	}
	
	/**
	 * Unpacks {@code imageARGB} into a {@code PixelImage} instance using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Returns a {@code PixelImage} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code imageARGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * PixelImage.unpackFromARGB(resolutionX, resolutionY, imageARGB, new MitchellFilter());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param imageARGB an {@code int[]} with {@code int} values representing colors in packed form as ARGB
	 * @return a {@code PixelImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code imageARGB} is {@code null}
	 */
	public static PixelImage unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB) {
		return unpackFromARGB(resolutionX, resolutionY, imageARGB, new MitchellFilter());
	}
	
	/**
	 * Unpacks {@code imageARGB} into a {@code PixelImage} instance using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Returns a {@code PixelImage} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code imageARGB} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param imageARGB an {@code int[]} with {@code int} values representing colors in packed form as ARGB
	 * @param filter the {@link Filter} to use
	 * @return a {@code PixelImage} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, or {@code imageARGB.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, either {@code imageARGB} or {@code filter} are {@code null}
	 */
	public static PixelImage unpackFromARGB(final int resolutionX, final int resolutionY, final int[] imageARGB, final Filter filter) {
		final PixelImage pixelImage = new PixelImage(resolutionX, resolutionY, new Color3F(), filter);
		
		ParameterArguments.requireExact(imageARGB.length, resolutionX * resolutionY, "imageARGB.length");
		
		for(int i = 0; i < pixelImage.resolution; i++) {
			pixelImage.setColorRGB(Color3F.unpack(imageARGB[i], PackedIntComponentOrder.ARGB), i);
		}
		
		return pixelImage;
	}
}