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
package org.dayflower.image2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

import org.dayflower.color.Color3D;
import org.dayflower.color.Color3F;
import org.dayflower.color.Color4D;
import org.dayflower.color.Color4F;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.geometry.shape.Rectangle2I;

/**
 * An {@code Image} represents an image that can be drawn to and saved to disk.
 * <p>
 * This class is mutable and not thread-safe.
 * <p>
 * Many operations are supported by default. But there are limitations, such as drawing text. In order to circumvent these limitations, the method {@link #draw(Consumer)} was added. It allows for interoperability between this API and the Java2D API.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Image {
	private final Data data;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Image} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(1024, 768);
	 * }
	 * </pre>
	 */
	public Image() {
		this(1024, 768);
	}
	
	/**
	 * Constructs a new {@code Image} instance from {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(bufferedImage, DataFactory.forColorARGB());
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public Image(final BufferedImage bufferedImage) {
		this(bufferedImage, DataFactory.forColorARGB());
	}
	
	/**
	 * Constructs a new {@code Image} instance from {@code bufferedImage} using {@code dataFactory}.
	 * <p>
	 * If either {@code bufferedImage} or {@code dataFactory} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param dataFactory a {@link DataFactory} instance
	 * @throws NullPointerException thrown if, and only if, either {@code bufferedImage} or {@code dataFactory} are {@code null}
	 */
	public Image(final BufferedImage bufferedImage, final DataFactory dataFactory) {
		this.data = dataFactory.create(bufferedImage);
	}
	
	/**
	 * Constructs a new {@code Image} instance from {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param data the {@link Data} instance to copy
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
	public Image(final Data data) {
		this.data = data.copy();
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(file, DataFactory.forColorARGB());
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to read from
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final File file) {
		this(file, DataFactory.forColorARGB());
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the file represented by {@code file} using {@code dataFactory}.
	 * <p>
	 * If either {@code file} or {@code dataFactory} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to read from
	 * @param dataFactory a {@link DataFactory} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code dataFactory} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final File file, final DataFactory dataFactory) {
		this.data = dataFactory.create(file);
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
		this(image.data);
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the file represented by the pathname {@code pathname}.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(pathname, DataFactory.forColorARGB());
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to read from
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final String pathname) {
		this(pathname, DataFactory.forColorARGB());
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the file represented by the pathname {@code pathname} using {@code dataFactory}.
	 * <p>
	 * If either {@code pathname} or {@code dataFactory} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to read from
	 * @param dataFactory a {@link DataFactory} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code dataFactory} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final String pathname, final DataFactory dataFactory) {
		this.data = dataFactory.create(pathname);
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the URL represented by {@code uRL}.
	 * <p>
	 * If {@code uRL} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(uRL, DataFactory.forColorARGB());
	 * }
	 * </pre>
	 * 
	 * @param uRL a {@code URL} that represents the URL to read from
	 * @throws NullPointerException thrown if, and only if, {@code uRL} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final URL uRL) {
		this(uRL, DataFactory.forColorARGB());
	}
	
	/**
	 * Constructs a new {@code Image} instance by reading the content of the URL represented by {@code uRL} using {@code dataFactory}.
	 * <p>
	 * If either {@code uRL} or {@code dataFactory} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param uRL a {@code URL} that represents the URL to read from
	 * @param dataFactory a {@link DataFactory} instance
	 * @throws NullPointerException thrown if, and only if, either {@code uRL} or {@code dataFactory} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image(final URL uRL, final DataFactory dataFactory) {
		this.data = dataFactory.create(uRL);
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code Color4D.WHITE}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(resolutionX, resolutionY, Color4D.WHITE);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution along the X-axis
	 * @param resolutionY the resolution along the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}
	 */
	public Image(final int resolutionX, final int resolutionY) {
		this(resolutionX, resolutionY, Color4D.WHITE);
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code color}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Image(resolutionX, resolutionY, color, DataFactory.forColorARGB());
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution along the X-axis
	 * @param resolutionY the resolution along the Y-axis
	 * @param color the {@link Color4D} to fill with
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color4D color) {
		this(resolutionX, resolutionY, color, DataFactory.forColorARGB());
	}
	
	/**
	 * Constructs a new {@code Image} instance filled with {@code color} using {@code dataFactory}.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If either {@code color} or {@code dataFactory} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param resolutionX the resolution along the X-axis
	 * @param resolutionY the resolution along the Y-axis
	 * @param color the {@link Color4D} to fill with
	 * @param dataFactory a {@link DataFactory} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 1}
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code dataFactory} are {@code null}
	 */
	public Image(final int resolutionX, final int resolutionY, final Color4D color, final DataFactory dataFactory) {
		this.data = dataFactory.create(resolutionX, resolutionY, color);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code Image} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toBufferedImage(false);
	 * }
	 * </pre>
	 * 
	 * @return a {@code BufferedImage} representation of this {@code Image} instance
	 */
	public BufferedImage toBufferedImage() {
		return toBufferedImage(false);
	}
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code Image} instance.
	 * 
	 * @param isRGB {@code true} if, and only if, {@code BufferedImage.TYPE_INT_RGB} should be used instead of {@code BufferedImage.TYPE_INT_ARGB}, {@code false} otherwise
	 * @return a {@code BufferedImage} representation of this {@code Image} instance
	 */
	public BufferedImage toBufferedImage(final boolean isRGB) {
		return this.data.toBufferedImage(isRGB);
	}
	
	/**
	 * Returns the {@link Color3D} at {@code point} in this {@code Image} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color3D.BLACK} will be returned.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color3D.BLACK} will be returned.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColor3D(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the X- and Y-components of the pixel
	 * @return the {@code Color3D} at {@code point} in this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Color3D getColor3D(final Point2I point) {
		return getColor3D(point.x, point.y);
	}
	
	/**
	 * Returns the {@link Color3D} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionX()}, {@code Color3D.BLACK} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionY()}, {@code Color3D.BLACK} will be returned.
	 * <p>
	 * If both {@code x} and {@code y} are equal to mathematical integers, this method is equivalent to {@link #getColor3D(int, int)}. Otherwise, bilinear interpolation will be performed on the closest pixels.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color3D} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color3D getColor3D(final double x, final double y) {
		return this.data.getColor3D(x, y);
	}
	
	/**
	 * Returns the {@link Color3D} at {@code index} in this {@code Image} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, {@code Color3D.BLACK} will be returned.
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color3D} at {@code index} in this {@code Image} instance
	 */
	public Color3D getColor3D(final int index) {
		return this.data.getColor3D(index);
	}
	
	/**
	 * Returns the {@link Color3D} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color3D.BLACK} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color3D.BLACK} will be returned.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color3D} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color3D getColor3D(final int x, final int y) {
		return this.data.getColor3D(x, y);
	}
	
	/**
	 * Returns the {@link Color3F} at {@code point} in this {@code Image} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color3F.BLACK} will be returned.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color3F.BLACK} will be returned.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColor3F(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the X- and Y-components of the pixel
	 * @return the {@code Color3F} at {@code point} in this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Color3F getColor3F(final Point2I point) {
		return getColor3F(point.x, point.y);
	}
	
	/**
	 * Returns the {@link Color3F} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0.0F} or greater than or equal to {@code image.getResolutionX()}, {@code Color3F.BLACK} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0.0F} or greater than or equal to {@code image.getResolutionY()}, {@code Color3F.BLACK} will be returned.
	 * <p>
	 * If both {@code x} and {@code y} are equal to mathematical integers, this method is equivalent to {@link #getColor3F(int, int)}. Otherwise, bilinear interpolation will be performed on the closest pixels.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color3F} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color3F getColor3F(final float x, final float y) {
		return this.data.getColor3F(x, y);
	}
	
	/**
	 * Returns the {@link Color3F} at {@code index} in this {@code Image} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, {@code Color3F.BLACK} will be returned.
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color3F} at {@code index} in this {@code Image} instance
	 */
	public Color3F getColor3F(final int index) {
		return this.data.getColor3F(index);
	}
	
	/**
	 * Returns the {@link Color3F} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color3F.BLACK} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color3F.BLACK} will be returned.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color3F} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color3F getColor3F(final int x, final int y) {
		return this.data.getColor3F(x, y);
	}
	
	/**
	 * Returns the {@link Color4D} at {@code point} in this {@code Image} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color4D.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color4D.TRANSPARENT} will be returned.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColor4D(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the X- and Y-components of the pixel
	 * @return the {@code Color4D} at {@code point} in this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Color4D getColor4D(final Point2I point) {
		return getColor4D(point.x, point.y);
	}
	
	/**
	 * Returns the {@link Color4D} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionX()}, {@code Color4D.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionY()}, {@code Color4D.TRANSPARENT} will be returned.
	 * <p>
	 * If both {@code x} and {@code y} are equal to mathematical integers, this method is equivalent to {@link #getColor4D(int, int)}. Otherwise, bilinear interpolation will be performed on the closest pixels.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color4D} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color4D getColor4D(final double x, final double y) {
		return this.data.getColor4D(x, y);
	}
	
	/**
	 * Returns the {@link Color4D} at {@code index} in this {@code Image} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, {@code Color4D.TRANSPARENT} will be returned.
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color4D} at {@code index} in this {@code Image} instance
	 */
	public Color4D getColor4D(final int index) {
		return this.data.getColor4D(index);
	}
	
	/**
	 * Returns the {@link Color4D} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color4D.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color4D.TRANSPARENT} will be returned.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color4D} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color4D getColor4D(final int x, final int y) {
		return this.data.getColor4D(x, y);
	}
	
	/**
	 * Returns the {@link Color4F} at {@code point} in this {@code Image} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color4F.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color4F.TRANSPARENT} will be returned.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColor4F(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the X- and Y-components of the pixel
	 * @return the {@code Color4F} at {@code point} in this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public Color4F getColor4F(final Point2I point) {
		return getColor4F(point.x, point.y);
	}
	
	/**
	 * Returns the {@link Color4F} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0.0F} or greater than or equal to {@code image.getResolutionX()}, {@code Color4F.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0.0F} or greater than or equal to {@code image.getResolutionY()}, {@code Color4F.TRANSPARENT} will be returned.
	 * <p>
	 * If both {@code x} and {@code y} are equal to mathematical integers, this method is equivalent to {@link #getColor4F(int, int)}. Otherwise, bilinear interpolation will be performed on the closest pixels.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color4F} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color4F getColor4F(final float x, final float y) {
		return this.data.getColor4F(x, y);
	}
	
	/**
	 * Returns the {@link Color4F} at {@code index} in this {@code Image} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, {@code Color4F.TRANSPARENT} will be returned.
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color4F} at {@code index} in this {@code Image} instance
	 */
	public Color4F getColor4F(final int index) {
		return this.data.getColor4F(index);
	}
	
	/**
	 * Returns the {@link Color4F} at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color4F.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color4F.TRANSPARENT} will be returned.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the {@code Color4F} at {@code x} and {@code y} in this {@code Image} instance
	 */
	public Color4F getColor4F(final int x, final int y) {
		return this.data.getColor4F(x, y);
	}
	
	/**
	 * Applies {@code convolutionKernel} to all pixels in this {@code Image} instance that are accepted by {@code filter}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code convolutionKernel} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.convolve(convolutionKernel, (color, point) -> true);
	 * }
	 * </pre>
	 * 
	 * @param convolutionKernel the {@link ConvolutionKernelND} instance to apply
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code convolutionKernel} is {@code null}
	 */
	public Image convolve(final ConvolutionKernelND convolutionKernel) {
		return convolve(convolutionKernel, (color, point) -> true);
	}
	
	/**
	 * Applies {@code convolutionKernel} to all pixels in this {@code Image} instance that are accepted by {@code filter}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code convolutionKernel} or {@code filter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiPredicate} instance, {@code filter}, is supplied with a {@code Color4D} instance and a {@code Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns {@code true} if, and only if, the current pixel should be convolved, {@code false} otherwise.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * 
	 * @param convolutionKernel the {@link ConvolutionKernelND} instance to apply
	 * @param filter a {@code BiPredicate} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code convolutionKernel} or {@code filter} are {@code null}
	 */
	public Image convolve(final ConvolutionKernelND convolutionKernel, final BiPredicate<Color4D, Point2I> filter) {
		Objects.requireNonNull(convolutionKernel, "convolutionKernel == null");
		Objects.requireNonNull(filter, "filter == null");
		
		final int[] indices = doFilter(filter);
		
		this.data.convolve(convolutionKernel, indices);
		
		return this;
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
	 * Draws the contents drawn to the supplied {@code Graphics2D} instance into this {@code Image} instance.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code graphics2DConsumer} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * 
	 * @param graphics2DConsumer a {@code Consumer} that accepts a {@code Graphics2D} instance
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code graphics2DConsumer} is {@code null}
	 */
	public Image draw(final Consumer<Graphics2D> graphics2DConsumer) {
		Objects.requireNonNull(graphics2DConsumer, "graphics2DConsumer == null");
		
		this.data.draw(graphics2DConsumer);
		
		return this;
	}
	
	/**
	 * Draws {@code shape} to this {@code Image} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeColor4D(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public Image drawShapeColor4D(final Shape2I shape) {
		return drawShapeColor4D(shape, Color4D.BLACK);
	}
	
	/**
	 * Draws {@code shape} to this {@code Image} instance with {@link Color4D} instances returned by {@code operator} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @param operator a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image drawShapeColor4D(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> operator) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(operator, "operator == null");
		
		this.data.changeBegin();
		
		shape.findPointsOfIntersection(getBounds(), true).forEach(point -> setColor4D(operator.apply(getColor4D(point), point), point));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Draws {@code shape} to this {@code Image} instance with {@code color} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code color} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeColor4D(shape, (currentColor, currentPoint) -> color);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to draw
	 * @param color the {@link Color4D} to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code color} are {@code null}
	 */
	public Image drawShapeColor4D(final Shape2I shape, final Color4D color) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(color, "color == null");
		
		return drawShapeColor4D(shape, (currentColor, currentPoint) -> color);
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code Image} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeComplementColor4D(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public Image drawShapeComplementColor4D(final Shape2I shape) {
		return drawShapeComplementColor4D(shape, Color4D.BLACK);
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code Image} instance with {@link Color4D} instances returned by {@code operator} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @param operator a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image drawShapeComplementColor4D(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> operator) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(operator, "operator == null");
		
		this.data.changeBegin();
		
		shape.findPointsOfComplement(getBounds(), true).forEach(point -> setColor4D(operator.apply(getColor4D(point), point), point));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Draws everything except for {@code shape} in this {@code Image} instance with {@code color} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code color} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.drawShapeComplementColor4D(shape, (currentColor, currentPoint) -> color);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to draw
	 * @param color the {@link Color4D} to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code color} are {@code null}
	 */
	public Image drawShapeComplementColor4D(final Shape2I shape, final Color4D color) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(color, "color == null");
		
		return drawShapeComplementColor4D(shape, (currentColor, currentPoint) -> color);
	}
	
	/**
	 * Fills all pixels in this {@code Image} instance in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code operator} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fill(operator, (color, point) -> true);
	 * }
	 * </pre>
	 * 
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code operator} is {@code null} or returns {@code null}
	 */
	public Image fill(final BiFunction<Color4D, Point2I, Color4D> operator) {
		return fill(operator, (color, point) -> true);
	}
	
	/**
	 * Fills all pixels in this {@code Image} instance that are accepted by {@code filter} in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * The {@code BiPredicate} instance, {@code filter}, is supplied with a {@code Color4D} instance and a {@code Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns {@code true} if, and only if, the current pixel should be filled, {@code false} otherwise.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * 
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @param filter a {@code BiPredicate} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fill(final BiFunction<Color4D, Point2I, Color4D> operator, final BiPredicate<Color4D, Point2I> filter) {
		Objects.requireNonNull(operator, "operator == null");
		Objects.requireNonNull(filter, "filter == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		this.data.changeBegin();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Point2I point = new Point2I(x, y);
				
				final Color4D oldColor = getColor4D(x, y);
				
				if(filter.test(oldColor, point)) {
					final Color4D newColor = Objects.requireNonNull(operator.apply(oldColor, point));
					
					this.data.setColor4D(newColor, x, y);
				}
			}
		}
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills all pixels in this {@code Image} instance in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code pixelOperator} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fill(pixelOperator, (colorARGB, x, y) -> true);
	 * }
	 * </pre>
	 * 
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperator} is {@code null}
	 */
	public Image fill(final PixelOperator pixelOperator) {
		return fill(pixelOperator, (colorARGB, x, y) -> true);
	}
	
	/**
	 * Fills all pixels in this {@code Image} instance that are accepted by {@code pixelFilter} in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code pixelOperator} or {@code pixelFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * 
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @param pixelFilter a {@link PixelFilter} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pixelOperator} or {@code pixelFilter} are {@code null}
	 */
	public Image fill(final PixelOperator pixelOperator, final PixelFilter pixelFilter) {
		Objects.requireNonNull(pixelOperator, "pixelOperator == null");
		Objects.requireNonNull(pixelFilter, "pixelFilter == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		this.data.changeBegin();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final int oldColorARGB = getColorARGB(x, y);
				
				if(pixelFilter.isAccepted(oldColorARGB, x, y)) {
					final int newColorARGB = pixelOperator.apply(oldColorARGB, x, y);
					
					this.data.setColorARGB(newColorARGB, x, y);
				}
			}
		}
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code point} in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code point} or {@code operator} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(point, operator, (currentColor, currentPoint) -> true);
	 * }
	 * </pre>
	 * 
	 * @param point a {@code Point2I} instance that contains the X- and Y-components of the pixel to start at
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code operator} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fillRegion(final Point2I point, final BiFunction<Color4D, Point2I, Color4D> operator) {
		return fillRegion(point, operator, (currentColor, currentPoint) -> true);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code point} and accepted by {@code filter} in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code point}, {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * The {@code BiPredicate} instance, {@code filter}, is supplied with a {@code Color4D} instance and a {@code Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns {@code true} if, and only if, the current pixel should be filled, {@code false} otherwise.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(point.x, point.y, operator, filter);
	 * }
	 * </pre>
	 * 
	 * @param point a {@code Point2I} instance that contains the X- and Y-components of the pixel to start at
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @param filter a {@code BiPredicate} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fillRegion(final Point2I point, final BiFunction<Color4D, Point2I, Color4D> operator, final BiPredicate<Color4D, Point2I> filter) {
		return fillRegion(point.x, point.y, operator, filter);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code point} in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code point} or {@code pixelOperator} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(point, pixelOperator, (colorARGB, x, y) -> true);
	 * }
	 * </pre>
	 * 
	 * @param point a {@code Point2I} instance that contains the X- and Y-components of the pixel to start at
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code point} or {@code pixelOperator} are {@code null}
	 */
	public Image fillRegion(final Point2I point, final PixelOperator pixelOperator) {
		return fillRegion(point, pixelOperator, (colorARGB, x, y) -> true);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code point} and accepted by {@code pixelFilter} in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code point}, {@code pixelOperator} or {@code pixelFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(point.x, point.y, pixelOperator, pixelFilter);
	 * }
	 * </pre>
	 * 
	 * @param point a {@code Point2I} instance that contains the X- and Y-components of the pixel to start at
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @param pixelFilter a {@link PixelFilter} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code point}, {@code pixelOperator} or {@code pixelFilter} are {@code null}
	 */
	public Image fillRegion(final Point2I point, final PixelOperator pixelOperator, final PixelFilter pixelFilter) {
		return fillRegion(point.x, point.y, pixelOperator, pixelFilter);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code operator} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(x, y, operator, (color, point) -> true);
	 * }
	 * </pre>
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code operator} is {@code null} or returns {@code null}
	 */
	public Image fillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> operator) {
		return fillRegion(x, y, operator, (color, point) -> true);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} and accepted by {@code filter} in the colors provided by {@code operator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The {@code BiFunction} instance, {@code operator}, is supplied with a {@link Color4D} instance and a {@link Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns a {@code Color4D} instance that represents the filled color.
	 * <p>
	 * The {@code BiPredicate} instance, {@code filter}, is supplied with a {@code Color4D} instance and a {@code Point2I} instance. The {@code Color4D} instance represents the current color and the {@code Point2I} instance represents the location of the current pixel. It returns {@code true} if, and only if, the current pixel should be filled, {@code false} otherwise.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColor4D()} than {@link DataFactory#forColorARGB()}.
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param operator a {@code BiFunction} instance that returns a {@code Color4D} instance for each pixel affected
	 * @param filter a {@code BiPredicate} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code operator} or {@code filter} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> operator, final BiPredicate<Color4D, Point2I> filter) {
		this.data.changeBegin();
		
		doFillRegion(x, y, operator, filter, getColorARGB(x, y));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code pixelOperator} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillRegion(x, y, pixelOperator, (currentColorARGB, currentX, currentY) -> true);
	 * }
	 * </pre>
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code pixelOperator} is {@code null}
	 */
	public Image fillRegion(final int x, final int y, final PixelOperator pixelOperator) {
		return fillRegion(x, y, pixelOperator, (currentColorARGB, currentX, currentY) -> true);
	}
	
	/**
	 * Fills the region of pixels that are color-connected to the pixel at {@code x} and {@code y} and accepted by {@code pixelFilter} in the colors provided by {@code pixelOperator}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code pixelOperator} or {@code pixelFilter} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This operation works in a similar way to the Bucket Fill tool in Microsoft Paint.
	 * <p>
	 * This operation is faster for {@link Data} instances created by {@link DataFactory#forColorARGB()} than {@link DataFactory#forColor4D()}.
	 * 
	 * @param x the X-component of the pixel to start at
	 * @param y the Y-component of the pixel to start at
	 * @param pixelOperator a {@link PixelOperator} instance that returns a color for each pixel affected
	 * @param pixelFilter a {@link PixelFilter} instance that accepts or rejects pixels
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pixelOperator} or {@code pixelFilter} are {@code null}
	 */
	public Image fillRegion(final int x, final int y, final PixelOperator pixelOperator, final PixelFilter pixelFilter) {
		this.data.changeBegin();
		
		doFillRegion(x, y, pixelOperator, pixelFilter, getColorARGB(x, y));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code shape} in this {@code Image} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeColor4D(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public Image fillShapeColor4D(final Shape2I shape) {
		return fillShapeColor4D(shape, Color4D.BLACK);
	}
	
	/**
	 * Fills {@code shape} in this {@code Image} instance with {@link Color4D} instances returned by {@code operator} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @param operator a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fillShapeColor4D(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> operator) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(operator, "operator == null");
		
		this.data.changeBegin();
		
		shape.findPointsOfIntersection(getBounds()).forEach(point -> setColor4D(operator.apply(getColor4D(point), point), point));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills {@code shape} in this {@code Image} instance with {@code color} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code color} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeColor4D(shape, (currentColor, currentPoint) -> color);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} to fill
	 * @param color the {@link Color4D} to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code color} are {@code null}
	 */
	public Image fillShapeColor4D(final Shape2I shape, final Color4D color) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(color, "color == null");
		
		return fillShapeColor4D(shape, (currentColor, currentPoint) -> color);
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code Image} instance with {@code Color4D.BLACK} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeComplementColor4D(shape, Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public Image fillShapeComplementColor4D(final Shape2I shape) {
		return fillShapeComplementColor4D(shape, Color4D.BLACK);
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code Image} instance with {@link ColorDF} instances returned by {@code operator} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @param operator a {@code BiFunction} that returns {@code Color4D} instances to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code operator} are {@code null} or {@code operator} returns {@code null}
	 */
	public Image fillShapeComplementColor4D(final Shape2I shape, final BiFunction<Color4D, Point2I, Color4D> operator) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(operator, "operator == null");
		
		this.data.changeBegin();
		
		shape.findPointsOfComplement(getBounds()).forEach(point -> setColor4D(operator.apply(getColor4D(point), point), point));
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Fills everything except for {@code shape} in this {@code Image} instance with {@code color} as its color.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code shape} or {@code color} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillShapeComplementColor4D(shape, (currentColor, currentPoint) -> color);
	 * }
	 * </pre>
	 * 
	 * @param shape the {@link Shape2I} not to fill
	 * @param color the {@link Color4D} to use as its color
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code shape} or {@code color} are {@code null}
	 */
	public Image fillShapeComplementColor4D(final Shape2I shape, final Color4D color) {
		Objects.requireNonNull(shape, "shape == null");
		Objects.requireNonNull(color, "color == null");
		
		return fillShapeComplementColor4D(shape, (currentColor, currentPoint) -> color);
	}
	
	/**
	 * Flips this {@code Image} instance along the X- and Y-axes.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.flipX().flipY();
	 * }
	 * </pre>
	 * 
	 * @return this {@code Image} instance
	 */
	public Image flip() {
		return flipX().flipY();
	}
	
	/**
	 * Flips this {@code Image} instance along the X-axis.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * 
	 * @return this {@code Image} instance
	 */
	public Image flipX() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		this.data.changeBegin();
		
		for(int y = 0; y < resolutionY; y++) {
			for(int xL = 0, xR = resolutionX - 1; xL < xR; xL++, xR--) {
				final int indexL = y * resolutionX + xL;
				final int indexR = y * resolutionX + xR;
				
				this.data.swap(indexL, indexR);
			}
		}
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Flips this {@code Image} instance along the Y-axis.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * 
	 * @return this {@code Image} instance
	 */
	public Image flipY() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		this.data.changeBegin();
		
		for(int yT = 0, yB = resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < resolutionX; x++) {
				final int indexT = yT * resolutionX + x;
				final int indexB = yB * resolutionX + x;
				
				this.data.swap(indexT, indexB);
			}
		}
		
		this.data.changeEnd();
		
		return this;
	}
	
	/**
	 * Rotates this {@code Image} instance by {@code angle} degrees.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * A rotation is performed on the entire image. It scales the image so the content will fit. Therefore, it is not advised to perform multiple rotations on a given {@code Image} instance.
	 * <p>
	 * If you need to perform multiple rotations, consider using the {@link #copy()} method and apply the rotation to the copy.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.rotate(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees
	 * @return this {@code Image} instance
	 */
	public Image rotate(final double angle) {
		return rotate(angle, false);
	}
	
	/**
	 * Rotates this {@code Image} instance by {@code angle} degrees or radians.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * A rotation is performed on the entire image. It scales the image so the content will fit. Therefore, it is not advised to perform multiple rotations on a given {@code Image} instance.
	 * <p>
	 * If you need to perform multiple rotations, consider using the {@link #copy()} method and apply the rotation to the copy.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * 
	 * @param angle an angle in degrees or radians
	 * @param isAngleInRadians {@code true} if, and only if, {@code angle} is in radians, {@code false} otherwise
	 * @return this {@code Image} instance
	 */
	public Image rotate(final double angle, final boolean isAngleInRadians) {
		this.data.rotate(angle, isAngleInRadians);
		
		return this;
	}
	
	/**
	 * Rotates this {@code Image} instance by {@code angle} degrees.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * A rotation is performed on the entire image. It scales the image so the content will fit. Therefore, it is not advised to perform multiple rotations on a given {@code Image} instance.
	 * <p>
	 * If you need to perform multiple rotations, consider using the {@link #copy()} method and apply the rotation to the copy.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.rotate(angle, false);
	 * }
	 * </pre>
	 * 
	 * @param angle an angle in degrees
	 * @return this {@code Image} instance
	 */
	public Image rotate(final float angle) {
		return rotate(angle, false);
	}
	
	/**
	 * Rotates this {@code Image} instance by {@code angle} degrees or radians.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * A rotation is performed on the entire image. It scales the image so the content will fit. Therefore, it is not advised to perform multiple rotations on a given {@code Image} instance.
	 * <p>
	 * If you need to perform multiple rotations, consider using the {@link #copy()} method and apply the rotation to the copy.
	 * <p>
	 * This operation is roughly as fast for {@link Data} instances created by both {@link DataFactory#forColor4D()} and {@link DataFactory#forColorARGB()}.
	 * 
	 * @param angle an angle in degrees or radians
	 * @param isAngleInRadians {@code true} if, and only if, {@code angle} is in radians, {@code false} otherwise
	 * @return this {@code Image} instance
	 */
	public Image rotate(final float angle, final boolean isAngleInRadians) {
		this.data.rotate(angle, isAngleInRadians);
		
		return this;
	}
	
	/**
	 * Saves this {@code Image} instance to the file represented by {@code file} using the informal format name {@code "png"}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.save(file, "png");
	 * }
	 * </pre>
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image save(final File file) {
		return save(file, "png");
	}
	
	/**
	 * Saves this {@code Image} instance to the file represented by {@code file} using the informal format name {@code formatName}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code file} or {@code formatName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @param formatName the informal format name, such as {@code "png"}
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code formatName} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image save(final File file, final String formatName) {
		Objects.requireNonNull(file, "file == null");
		Objects.requireNonNull(formatName, "formatName == null");
		
		try {
			final File parentFile = file.getParentFile();
			
			if(parentFile != null && !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			
			ImageIO.write(toBufferedImage(doIsJPEG(formatName)), formatName, file);
			
			return this;
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saves this {@code Image} instance to the file represented by the pathname {@code pathname} using the informal format name {@code "png"}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.save(pathname, "png");
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to save to
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image save(final String pathname) {
		return save(pathname, "png");
	}
	
	/**
	 * Saves this {@code Image} instance to the file represented by the pathname {@code pathname} using the informal format name {@code formatName}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code pathname} or {@code formatName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.save(new File(pathname), formatName);
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to save to
	 * @param formatName the informal format name, such as {@code "png"}
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code formatName} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public Image save(final String pathname, final String formatName) {
		return save(new File(pathname), formatName);
	}
	
	/**
	 * Sets the color of the pixel at {@code point} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code color} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor3D(color, point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color3D} to set
	 * @param point the {@link Point2I} that contains the X- and Y-components of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code point} are {@code null}
	 */
	public Image setColor3D(final Color3D color, final Point2I point) {
		return setColor3D(color, point.x, point.y);
	}
	
	/**
	 * Sets the color of the pixel at {@code index} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, nothing will happen.
	 * 
	 * @param color the {@link Color3D} to set
	 * @param index the index of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor3D(final Color3D color, final int index) {
		this.data.setColor3D(Objects.requireNonNull(color, "color == null"), index);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code x} and {@code y} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * 
	 * @param color the {@link Color3D} to set
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor3D(final Color3D color, final int x, final int y) {
		this.data.setColor3D(Objects.requireNonNull(color, "color == null"), x, y);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code point} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code color} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor3F(color, point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color3F} to set
	 * @param point the {@link Point2I} that contains the X- and Y-components of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code point} are {@code null}
	 */
	public Image setColor3F(final Color3F color, final Point2I point) {
		return setColor3F(color, point.x, point.y);
	}
	
	/**
	 * Sets the color of the pixel at {@code index} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, nothing will happen.
	 * 
	 * @param color the {@link Color3F} to set
	 * @param index the index of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor3F(final Color3F color, final int index) {
		this.data.setColor3F(Objects.requireNonNull(color, "color == null"), index);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code x} and {@code y} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * 
	 * @param color the {@link Color3F} to set
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor3F(final Color3F color, final int x, final int y) {
		this.data.setColor3F(Objects.requireNonNull(color, "color == null"), x, y);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code point} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code color} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor4D(color, point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color4D} to set
	 * @param point the {@link Point2I} that contains the X- and Y-components of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code point} are {@code null}
	 */
	public Image setColor4D(final Color4D color, final Point2I point) {
		return setColor4D(color, point.x, point.y);
	}
	
	/**
	 * Sets the color of the pixel at {@code index} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, nothing will happen.
	 * 
	 * @param color the {@link Color4D} to set
	 * @param index the index of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor4D(final Color4D color, final int index) {
		this.data.setColor4D(Objects.requireNonNull(color, "color == null"), index);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code x} and {@code y} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * 
	 * @param color the {@link Color4D} to set
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor4D(final Color4D color, final int x, final int y) {
		this.data.setColor4D(Objects.requireNonNull(color, "color == null"), x, y);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code point} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If either {@code color} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColor4F(color, point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param color the {@link Color4F} to set
	 * @param point the {@link Point2I} that contains the X- and Y-components of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code point} are {@code null}
	 */
	public Image setColor4F(final Color4F color, final Point2I point) {
		return setColor4F(color, point.x, point.y);
	}
	
	/**
	 * Sets the color of the pixel at {@code index} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, nothing will happen.
	 * 
	 * @param color the {@link Color4F} to set
	 * @param index the index of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor4F(final Color4F color, final int index) {
		this.data.setColor4F(Objects.requireNonNull(color, "color == null"), index);
		
		return this;
	}
	
	/**
	 * Sets the color of the pixel at {@code x} and {@code y} in this {@code Image} instance to {@code color}.
	 * <p>
	 * Returns this {@code Image} instance.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, nothing will happen.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, nothing will happen.
	 * 
	 * @param color the {@link Color4F} to set
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Image setColor4F(final Color4F color, final int x, final int y) {
		this.data.setColor4F(Objects.requireNonNull(color, "color == null"), x, y);
		
		return this;
	}
	
	/**
	 * Returns a {@link Rectangle2I} with the bounds of this {@code Image} instance.
	 * 
	 * @return a {@code Rectangle2I} with the bounds of this {@code Image} instance
	 */
	public Rectangle2I getBounds() {
		return new Rectangle2I(new Point2I(), new Point2I(getResolutionX() - 1, getResolutionY() - 1));
	}
	
	/**
	 * Returns a {@code String} representation of this {@code Image} instance.
	 * 
	 * @return a {@code String} representation of this {@code Image} instance
	 */
	@Override
	public String toString() {
		return String.format("new Image(%d, %d)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code Image} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Image}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Image} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Image}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Image)) {
			return false;
		} else if(!Objects.equals(this.data, Image.class.cast(object).data)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, the change history is enabled, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the change history is enabled, {@code false} otherwise
	 */
	public boolean isChangeHistoryEnabled() {
		return this.data.isChangeHistoryEnabled();
	}
	
	/**
	 * Performs the current redo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the redo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the redo operation was performed, {@code false} otherwise
	 */
	public boolean redo() {
		return this.data.redo();
	}
	
	/**
	 * Sets the change history enabled state to {@code isChangeHistoryEnabled}.
	 * 
	 * @param isChangeHistoryEnabled the change history enabled state
	 */
	public boolean setChangeHistoryEnabled(final boolean isChangeHistoryEnabled) {
		return this.data.setChangeHistoryEnabled(isChangeHistoryEnabled);
	}
	
	/**
	 * Performs the current undo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the undo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the undo operation was performed, {@code false} otherwise
	 */
	public boolean undo() {
		return this.data.undo();
	}
	
	/**
	 * Performs a cache operation to this {@code Image} instance.
	 * <p>
	 * Returns the number of pixels that were cached as a result of this operation.
	 * 
	 * @return the number of pixels that were cached as a result of this operation
	 */
	public int cache() {
		return this.data.cache();
	}
	
	/**
	 * Returns the color at {@code point} in this {@code Image} instance.
	 * <p>
	 * If {@code point} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code point.x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code point.y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color.TRANSPARENT} will be returned.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorARGB(point.x, point.y);
	 * }
	 * </pre>
	 * 
	 * @param point a {@link Point2I} instance that contains the X- and Y-components of the pixel
	 * @return the color at {@code point} in this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code point} is {@code null}
	 */
	public int getColorARGB(final Point2I point) {
		return getColorARGB(point.x, point.y);
	}
	
	/**
	 * Returns the color at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionX()}, {@code Color.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0.0D} or greater than or equal to {@code image.getResolutionY()}, {@code Color.TRANSPARENT} will be returned.
	 * <p>
	 * If both {@code x} and {@code y} are equal to mathematical integers, this method is equivalent to {@link #getColorARGB(int, int)}. Otherwise, bilinear interpolation will be performed on the closest pixels.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the color at {@code x} and {@code y} in this {@code Image} instance
	 */
	public int getColorARGB(final double x, final double y) {
		return this.data.getColorARGB(x, y);
	}
	
	/**
	 * Returns the color at {@code index} in this {@code Image} instance.
	 * <p>
	 * If {@code index} is less than {@code 0} or greater than or equal to {@code image.getResolution()}, {@code Color.TRANSPARENT} will be returned.
	 * 
	 * @param index the index of the pixel
	 * @return the color at {@code index} in this {@code Image} instance
	 */
	public int getColorARGB(final int index) {
		return this.data.getColorARGB(index);
	}
	
	/**
	 * Returns the color at {@code x} and {@code y} in this {@code Image} instance.
	 * <p>
	 * If {@code x} is less than {@code 0} or greater than or equal to {@code image.getResolutionX()}, {@code Color.TRANSPARENT} will be returned.
	 * <p>
	 * If {@code y} is less than {@code 0} or greater than or equal to {@code image.getResolutionY()}, {@code Color.TRANSPARENT} will be returned.
	 * 
	 * @param x the X-component of the pixel
	 * @param y the Y-component of the pixel
	 * @return the color at {@code x} and {@code y} in this {@code Image} instance
	 */
	public int getColorARGB(final int x, final int y) {
		return this.data.getColorARGB(x, y);
	}
	
	/**
	 * Returns the resolution of this {@code Image} instance.
	 * 
	 * @return the resolution of this {@code Image} instance
	 */
	public int getResolution() {
		return this.data.getResolution();
	}
	
	/**
	 * Returns the resolution along the X-axis of this {@code Image} instance.
	 * 
	 * @return the resolution along the X-axis of this {@code Image} instance
	 */
	public int getResolutionX() {
		return this.data.getResolutionX();
	}
	
	/**
	 * Returns the resolution along the Y-axis of this {@code Image} instance.
	 * 
	 * @return the resolution along the Y-axis of this {@code Image} instance
	 */
	public int getResolutionY() {
		return this.data.getResolutionY();
	}
	
	/**
	 * Returns a hash code for this {@code Image} instance.
	 * 
	 * @return a hash code for this {@code Image} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.data);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This {@code PixelFilter} interface is used by the {@link Image} class to decide what pixels to operate on.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static interface PixelFilter {
		/**
		 * Returns {@code true} if, and only if, the pixel represented by {@code x} and {@code y} is accepted, {@code false} otherwise.
		 * 
		 * @param colorARGB the current color of the pixel
		 * @param x the X-component of the pixel
		 * @param y the Y-component of the pixel
		 * @return {@code true} if, and only if, the pixel represented by {@code x} and {@code y} is accepted, {@code false} otherwise
		 */
		boolean isAccepted(final int colorARGB, final int x, final int y);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * This {@code PixelOperator} interface is used by the {@link Image} class when operating on a given pixel.
	 * 
	 * @since 1.0.0
	 * @author J&#246;rgen Lundgren
	 */
	public static interface PixelOperator {
		/**
		 * Returns a color for the pixel at {@code x} and {@code y}.
		 * 
		 * @param colorARGB the current color of the pixel
		 * @param x the X-component of the pixel
		 * @param y the Y-component of the pixel
		 * @return a color, which may be {@code colorARGB} itself, or a transformed version of it
		 */
		int apply(final int colorARGB, final int x, final int y);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int[] doFilter(final BiPredicate<Color4D, Point2I> filter) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int[] indices = new int[resolutionX * resolutionY];
		
		boolean isFullyCovered = true;
		
		for(int y = 0, index = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++, index++) {
				final Color4D color = getColor4D(x, y);
				
				final Point2I point = new Point2I(x, y);
				
				if(filter.test(color, point)) {
					indices[index] = index;
				} else {
					indices[index] = -1;
					
					isFullyCovered = false;
				}
			}
		}
		
		if(isFullyCovered) {
			return indices;
		}
		
		return Arrays.stream(indices).filter(index -> index != -1).toArray();
	}
	
	private void doFillRegion(final int x, final int y, final BiFunction<Color4D, Point2I, Color4D> operator, final BiPredicate<Color4D, Point2I> filter, final int oldColorARGB) {
		final int resolution = getResolution();
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int minimumX = 0;
		final int maximumX = resolutionX - 1;
		final int minimumY = 0;
		final int maximumY = resolutionY - 1;
		
		if(x >= minimumX && x <= maximumX && y >= minimumY && y <= maximumY) {
			final boolean[] isFilled = new boolean[resolution];
			
			final int[] stackX = new int[resolution];
			final int[] stackY = new int[resolution];
			
			stackX[0] = x;
			stackY[0] = y;
			
			int stackLength = 1;
			
			while(stackLength > 0) {
				final int currentX = stackX[stackLength - 1];
				final int currentY = stackY[stackLength - 1];
				
				stackLength--;
				
				final Color4D color = getColor4D(currentX, currentY);
				
				final Point2I point = new Point2I(currentX, currentY);
				
				if(filter.test(color, point)) {
					this.data.setColor4D(operator.apply(color, point), currentX, currentY);
				}
				
				isFilled[currentY * resolutionX + currentX] = true;
				
				if(currentX + 1 <= maximumX && !isFilled[currentY * resolutionX + currentX + 1] && getColorARGB(currentX + 1, currentY) == oldColorARGB) {
					stackX[stackLength] = currentX + 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentX - 1 >= minimumX && !isFilled[currentY * resolutionX + currentX - 1] && getColorARGB(currentX - 1, currentY) == oldColorARGB) {
					stackX[stackLength] = currentX - 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentY + 1 <= maximumY && !isFilled[(currentY + 1) * resolutionX + currentX] && getColorARGB(currentX, currentY + 1) == oldColorARGB) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY + 1;
					
					stackLength++;
				}
				
				if(currentY - 1 >= minimumY && !isFilled[(currentY - 1) * resolutionX + currentX] && getColorARGB(currentX, currentY - 1) == oldColorARGB) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY - 1;
					
					stackLength++;
				}
			}
		}
	}
	
	private void doFillRegion(final int x, final int y, final PixelOperator pixelOperator, final PixelFilter pixelFilter, final int oldColorARGB) {
		final int resolution = getResolution();
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int minimumX = 0;
		final int maximumX = resolutionX - 1;
		final int minimumY = 0;
		final int maximumY = resolutionY - 1;
		
		if(x >= minimumX && x <= maximumX && y >= minimumY && y <= maximumY) {
			final boolean[] isFilled = new boolean[resolution];
			
			final int[] stackX = new int[resolution];
			final int[] stackY = new int[resolution];
			
			stackX[0] = x;
			stackY[0] = y;
			
			int stackLength = 1;
			
			while(stackLength > 0) {
				final int currentX = stackX[stackLength - 1];
				final int currentY = stackY[stackLength - 1];
				
				stackLength--;
				
				final int colorARGB = getColorARGB(currentX, currentY);
				
				if(pixelFilter.isAccepted(colorARGB, currentX, currentY)) {
					this.data.setColorARGB(pixelOperator.apply(colorARGB, currentX, currentY), currentX, currentY);
				}
				
				isFilled[currentY * resolutionX + currentX] = true;
				
				if(currentX + 1 <= maximumX && !isFilled[currentY * resolutionX + currentX + 1] && getColorARGB(currentX + 1, currentY) == oldColorARGB) {
					stackX[stackLength] = currentX + 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentX - 1 >= minimumX && !isFilled[currentY * resolutionX + currentX - 1] && getColorARGB(currentX - 1, currentY) == oldColorARGB) {
					stackX[stackLength] = currentX - 1;
					stackY[stackLength] = currentY;
					
					stackLength++;
				}
				
				if(currentY + 1 <= maximumY && !isFilled[(currentY + 1) * resolutionX + currentX] && getColorARGB(currentX, currentY + 1) == oldColorARGB) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY + 1;
					
					stackLength++;
				}
				
				if(currentY - 1 >= minimumY && !isFilled[(currentY - 1) * resolutionX + currentX] && getColorARGB(currentX, currentY - 1) == oldColorARGB) {
					stackX[stackLength] = currentX;
					stackY[stackLength] = currentY - 1;
					
					stackLength++;
				}
			}
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static boolean doIsJPEG(final String formatName) {
		return formatName.matches("^\\.?[Jj][Pp][Ee]?[Gg]$");
	}
}