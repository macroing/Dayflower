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

import static org.dayflower.utility.Floats.ceil;
import static org.dayflower.utility.Floats.floor;
import static org.dayflower.utility.Ints.toInt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import javax.imageio.ImageIO;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color3F;
import org.dayflower.color.Color4F;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.rasterizer.Rasterizer2I;
import org.dayflower.geometry.shape.Circle2I;
import org.dayflower.geometry.shape.Line2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.geometry.shape.Triangle2I;
import org.dayflower.utility.ParameterArguments;
import org.dayflower.utility.TriFunction;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

/**
 * An {@code ImageF} is an image that can be drawn to.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class ImageF {
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	protected ImageF(final int resolutionX, final int resolutionY) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code ImageF} instance.
	 * 
	 * @return a {@code BufferedImage} representation of this {@code ImageF} instance
	 */
	public final BufferedImage toBufferedImage() {
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
	public final Color3F getColorRGB(final float x, final float y) {
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
	public final Color3F getColorRGB(final float x, final float y, final PixelOperation pixelOperation) {
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
	public final Color3F getColorRGB(final int index) {
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
	public final Color3F getColorRGB(final int index, final PixelOperation pixelOperation) {
		return new Color3F(getColorRGBA(index, pixelOperation));
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
	public final Color3F getColorRGB(final int x, final int y) {
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
	public final Color3F getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		return new Color3F(getColorRGBA(x, y, pixelOperation));
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color4F getColorRGBA(final float x, final float y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * This method performs bilinear interpolation on the four closest {@code Color4F} instances.
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
	public final Color4F getColorRGBA(final float x, final float y, final PixelOperation pixelOperation) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return getColorRGBA(minimumX, minimumY, pixelOperation);
		}
		
		final Color4F color00 = getColorRGBA(minimumX, minimumY, pixelOperation);
		final Color4F color01 = getColorRGBA(maximumX, minimumY, pixelOperation);
		final Color4F color10 = getColorRGBA(minimumX, maximumY, pixelOperation);
		final Color4F color11 = getColorRGBA(maximumX, maximumY, pixelOperation);
		
		final float xFactor = x - minimumX;
		final float yFactor = y - minimumY;
		
		final Color4F color = Color4F.blend(Color4F.blend(color00, color01, xFactor), Color4F.blend(color10, color11, xFactor), yFactor);
		
		return color;
	}
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code index}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param index the index of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code index}
	 */
	public final Color4F getColorRGBA(final int index) {
		return getColorRGBA(index, PixelOperation.NO_CHANGE);
	}
	
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
	public abstract Color4F getColorRGBA(final int index, final PixelOperation pixelOperation);
	
	/**
	 * Returns the {@link Color4F} of the pixel represented by {@code x} and {@code y}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.getColorRGBA(x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @return the {@code Color4F} of the pixel represented by {@code x} and {@code y}
	 */
	public final Color4F getColorRGBA(final int x, final int y) {
		return getColorRGBA(x, y, PixelOperation.NO_CHANGE);
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
	public abstract Color4F getColorRGBA(final int x, final int y, final PixelOperation pixelOperation);
	
	/**
	 * Returns a copy of this {@code ImageF} instance.
	 * 
	 * @return a copy of this {@code ImageF} instance
	 */
	public abstract ImageF copy();
	
	/**
	 * Finds the bounds for {@code image} in this {@code ImageF} instance.
	 * <p>
	 * Returns a {@code List} with all {@link Rectangle2I} bounds found for {@code image} in this {@code ImageF} instance.
	 * <p>
	 * If {@code image} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param image an {@code ImageF} instance
	 * @return a {@code List} with all {@code Rectangle2I} bounds found for {@code image} in this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code image} is {@code null}
	 */
	public final List<Rectangle2I> findBoundsFor(final ImageF image) {
		Objects.requireNonNull(image, "image == null");
		
		final List<Rectangle2I> rectangles = new ArrayList<>();
		
		for(int y = 0; y < getResolutionY() - image.getResolutionY(); y++) {
			for(int x = 0; x < getResolutionX() - image.getResolutionX(); x++) {
				Rectangle2I rectangle = new Rectangle2I(new Point2I(x, y), new Point2I(x, y));
				
				labelImage:
				if(getColorRGB(x, y).equals(image.getColorRGB(0, 0))) {
					for(int imageY = 0; imageY < image.getResolutionY(); imageY++) {
						for(int imageX = 0; imageX < image.getResolutionX(); imageX++) {
							if(!getColorRGB(x + imageX, y + imageY).equals(image.getColorRGB(imageX, imageY))) {
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
	 * Returns a {@link Rectangle2I} with the bounds of this {@code ImageF} instance.
	 * 
	 * @return a {@code Rectangle2I} with the bounds of this {@code ImageF} instance
	 */
	public final Rectangle2I getBounds() {
		return new Rectangle2I(new Point2I(), new Point2I(this.resolutionX, this.resolutionY));
	}
	
	/**
	 * Returns a {@code WritableImage} representation of this {@code ImageF} instance.
	 * 
	 * @return a {@code WritableImage} representation of this {@code ImageF} instance
	 */
	public final WritableImage toWritableImage() {
		final
		WritableImage writableImage = new WritableImage(this.resolutionX, this.resolutionY);
		writableImage.getPixelWriter().setPixels(0, 0, this.resolutionX, this.resolutionY, PixelFormat.getIntArgbInstance(), toIntArrayPackedForm(), 0, this.resolutionX);
		
		return writableImage;
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code ImageF} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toByteArray(ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @return a {@code byte[]} representation of this {@code ImageF} instance
	 */
	public final byte[] toByteArray() {
		return toByteArray(ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code ImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code byte[]} representation of this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	public abstract byte[] toByteArray(final ArrayComponentOrder arrayComponentOrder);
	
	/**
	 * Returns the resolution of this {@code ImageF} instance.
	 * <p>
	 * The resolution of {@code image} can be computed by:
	 * <pre>
	 * {@code
	 * int resolution = image.getResolutionX() * image.getResolutionY();
	 * }
	 * </pre>
	 * 
	 * @return the resolution of this {@code ImageF} instance
	 */
	public final int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis of this {@code ImageF} instance.
	 * <p>
	 * The resolution of the X-axis is also known as the width.
	 * 
	 * @return the resolution of the X-axis of this {@code ImageF} instance
	 */
	public final int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis of this {@code ImageF} instance.
	 * <p>
	 * The resolution of the Y-axis is also known as the height.
	 * 
	 * @return the resolution of the Y-axis of this {@code ImageF} instance
	 */
	public final int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ImageF} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toIntArray(ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code ImageF} instance
	 */
	public final int[] toIntArray() {
		return toIntArray(ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return an {@code int[]} representation of this {@code ImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	public abstract int[] toIntArray(final ArrayComponentOrder arrayComponentOrder);
	
	/**
	 * Returns an {@code int[]} representation of this {@code ImageF} instance in a packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code ImageF} instance in a packed form
	 */
	public final int[] toIntArrayPackedForm() {
		return toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ImageF} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code ImageF} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public abstract int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder);
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color4F} of {@code Color4F.BLACK}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(Color4F.BLACK);
	 * }
	 * </pre>
	 */
	public final void clear() {
		clear(Color3F.BLACK);
	}
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color3F} of {@code colorRGB}.
	 * <p>
	 * If {@code colorRGB} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.clear(new Color4F(colorRGB));
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to clear with
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final void clear(final Color3F colorRGB) {
		clear(new Color4F(colorRGB));
	}
	
	/**
	 * Clears this {@code ImageF} instance with a {@link Color4F} of {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorRGBA the {@code Color4F} to clear with
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void clear(final Color4F colorRGBA) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		for(int i = 0; i < this.resolution; i++) {
			setColorRGBA(colorRGBA, i);
		}
	}
	
	/**
	 * Copies the individual component values of the colors in this {@code ImageF} instance to the {@code byte[]} {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length != image.getResolution() * ArrayComponentOrder.BGRA.getComponentCount()}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.copyTo(array, ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @param array the {@code byte[]} to copy the individual component values of the colors in this {@code ImageF} instance to
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != image.getResolution() * ArrayComponentOrder.BGRA.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public final void copyTo(final byte[] array) {
		copyTo(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Copies the individual component values of the colors in this {@code ImageF} instance to the {@code byte[]} {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length != image.getResolution() * arrayComponentOrder.getComponentCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the {@code byte[]} to copy the individual component values of the colors in this {@code ImageF} instance to
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} to copy the components to {@code array} in the correct order
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != image.getResolution() * arrayComponentOrder.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public final void copyTo(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length, this.resolution * arrayComponentOrder.getComponentCount(), "array");
		
		final byte[] sourceArray = toByteArray(arrayComponentOrder);
		final byte[] targetArray = array;
		
		System.arraycopy(sourceArray, 0, targetArray, 0, targetArray.length);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawCircle(circle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final void drawCircle(final Circle2I circle) {
		drawCircle(circle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawCircle(circle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawCircle(final Circle2I circle, final Color4F colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code circle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawCircle(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius() && x * x + y * y > (circle.getRadius() - 1) * (circle.getRadius() - 1)) {
					final int circleX = x + circle.getCenter().getX();
					final int circleY = y + circle.getCenter().getY();
					
					if(circleX >= 0 && circleX < this.resolutionX && circleY >= 0 && circleY < this.resolutionY) {
						final Point2I point = new Point2I(circleX, circleY);
						
						final Color4F oldColorRGBA = getColorRGBA(circleX, circleY);
						final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
						
						setColorRGBA(newColorRGBA, circleX, circleY);
					}
				}
			}
		}
	}
	
	/**
	 * Draws {@code line} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code line} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawLine(line, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code line} is {@code null}
	 */
	public final void drawLine(final Line2I line) {
		drawLine(line, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code line} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code line} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawLine(line, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code colorRGBA} are {@code null}
	 */
	public final void drawLine(final Line2I line, final Color4F colorRGBA) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawLine(line, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code line} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code line} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param line the {@link Line2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code line} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawLine(final Line2I line, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(line, "line == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[] scanline = Rasterizer2I.rasterize(line, rectangle);
		
		for(final Point2I point : scanline) {
			final int x = point.getX();
			final int y = point.getY();
			
			if(x >= 0 && x < this.resolutionX && y >= 0 && y < this.resolutionY) {
				final Color4F oldColorRGBA = getColorRGBA(x, y);
				final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
				
				setColorRGBA(newColorRGBA, point.getX(), point.getY());
			}
		}
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawRectangle(rectangle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle) {
		drawRectangle(rectangle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawRectangle(rectangle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle, final Color4F colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code rectangle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawRectangle(final Rectangle2I rectangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				if((x == minimumX || x == maximumX || y == minimumY || y == maximumY) && x >= 0 && x < this.resolutionX && y >= 0 && y < this.resolutionY) {
					final Point2I point = new Point2I(x, y);
					
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, x, y);
				}
			}
		}
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.drawTriangle(triangle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle) {
		drawTriangle(triangle, Color4F.BLACK);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.drawTriangle(triangle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle, final Color4F colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		drawTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Draws {@code triangle} to this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to draw
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void drawTriangle(final Triangle2I triangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		drawLine(new Line2I(triangle.getA(), triangle.getB()), biFunction);
		drawLine(new Line2I(triangle.getB(), triangle.getC()), biFunction);
		drawLine(new Line2I(triangle.getC(), triangle.getA()), biFunction);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code circle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillCircle(circle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code circle} is {@code null}
	 */
	public final void fillCircle(final Circle2I circle) {
		fillCircle(circle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code circle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillCircle(circle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillCircle(final Circle2I circle, final Color4F colorRGBA) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillCircle(circle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code circle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param circle the {@link Circle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code circle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillCircle(final Circle2I circle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(circle, "circle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		for(int y = -circle.getRadius(); y <= circle.getRadius(); y++) {
			for(int x = -circle.getRadius(); x <= circle.getRadius(); x++) {
				if(x * x + y * y <= circle.getRadius() * circle.getRadius()) {
					final int circleX = x + circle.getCenter().getX();
					final int circleY = y + circle.getCenter().getY();
					
					if(circleX >= 0 && circleX < this.resolutionX && circleY >= 0 && circleY < this.resolutionY) {
						final Point2I point = new Point2I(circleX, circleY);
						
						final Color4F oldColorRGBA = getColorRGBA(circleX, circleY);
						final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
						
						setColorRGBA(newColorRGBA, circleX, circleY);
					}
				}
			}
		}
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
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
	 * @param sourceImage the {@code ImageF} to fill
	 * @throws NullPointerException thrown if, and only if, {@code sourceImage} is {@code null}
	 */
	public final void fillImage(final ImageF sourceImage) {
		fillImage(sourceImage, sourceImage.getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage} or {@code sourceBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, image.getBounds());
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage} or {@code sourceBounds} are {@code null}
	 */
	public final void fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds) {
		fillImage(sourceImage, sourceBounds, getBounds());
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> sourceColorRGBA);
	 * }
	 * </pre>
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageF} instance to use
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds} or {@code targetBounds} are {@code null}
	 */
	public final void fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds) {
		fillImage(sourceImage, sourceBounds, targetBounds, (sourceColorRGBA, targetColorRGBA, targetPoint) -> sourceColorRGBA);
	}
	
	/**
	 * Fills {@code sourceImage} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code triFunction} as its color.
	 * <p>
	 * If either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param sourceImage the {@code ImageF} to fill
	 * @param sourceBounds a {@link Rectangle2I} that represents the bounds of the region in {@code sourceImage} to use
	 * @param targetBounds a {@code Rectangle2I} that represents the bounds of the region in this {@code ImageF} instance to use
	 * @param triFunction a {@code TriFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code sourceImage}, {@code sourceBounds}, {@code targetBounds} or {@code triFunction} are {@code null} or {@code triFunction} returns {@code null}
	 */
	public final void fillImage(final ImageF sourceImage, final Rectangle2I sourceBounds, final Rectangle2I targetBounds, final TriFunction<Color4F, Color4F, Point2I, Color4F> triFunction) {
		Objects.requireNonNull(sourceImage, "sourceImage == null");
		Objects.requireNonNull(sourceBounds, "sourceBounds == null");
		Objects.requireNonNull(targetBounds, "targetBounds == null");
		Objects.requireNonNull(triFunction, "triFunction == null");
		
		final ImageF targetImage = this;
		
		final int sourceMinimumX = sourceBounds.getA().getX();
		final int sourceMinimumY = sourceBounds.getA().getY();
		final int sourceMaximumX = sourceBounds.getC().getX();
		final int sourceMaximumY = sourceBounds.getC().getY();
		final int targetMinimumX = targetBounds.getA().getX();
		final int targetMinimumY = targetBounds.getA().getY();
		final int targetMaximumX = targetBounds.getC().getX();
		final int targetMaximumY = targetBounds.getC().getY();
		
		for(int sourceY = sourceMinimumY, targetY = targetMinimumY; sourceY < sourceMaximumY && targetY < targetMaximumY; sourceY++, targetY++) {
			for(int sourceX = sourceMinimumX, targetX = targetMinimumX; sourceX < sourceMaximumX && targetX < targetMaximumX; sourceX++, targetX++) {
				if(targetX >= 0 && targetX < this.resolutionX && targetY >= 0 && targetY < this.resolutionY) {
					final Color4F sourceColorRGBA = sourceImage.getColorRGBA(sourceX, sourceY);
					final Color4F targetColorRGBA = targetImage.getColorRGBA(targetX, targetY);
					final Color4F colorRGBA = Objects.requireNonNull(triFunction.apply(sourceColorRGBA, targetColorRGBA, new Point2I(targetX, targetY)));
					
					targetImage.setColorRGBA(colorRGBA, targetX, targetY);
				}
			}
		}
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillRectangle(rectangle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle) {
		fillRectangle(rectangle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code colorRGB} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillRectangle(rectangle, new Color4F(colorRGB));
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGB the {@link Color3F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGB} are {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final Color3F colorRGB) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		
		fillRectangle(rectangle, new Color4F(colorRGB));
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillRectangle(rectangle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final Color4F colorRGBA) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillRectangle(rectangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code rectangle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param rectangle the {@link Rectangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code rectangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillRectangle(final Rectangle2I rectangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final int minimumX = rectangle.getA().getX();
		final int minimumY = rectangle.getA().getY();
		final int maximumX = rectangle.getC().getX();
		final int maximumY = rectangle.getC().getY();
		
		for(int y = minimumY; y <= maximumY; y++) {
			for(int x = minimumX; x <= maximumX; x++) {
				if(x >= 0 && x < this.resolutionX && y >= 0 && y < this.resolutionY) {
					final Point2I point = new Point2I(x, y);
					
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, x, y);
				}
			}
		}
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@code Color4F.BLACK} as its color.
	 * <p>
	 * If {@code triangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * image.fillTriangle(triangle, Color4F.BLACK);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @throws NullPointerException thrown if, and only if, {@code triangle} is {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle) {
		fillTriangle(triangle, Color4F.BLACK);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@code colorRGBA} as its color.
	 * <p>
	 * If either {@code triangle} or {@code colorRGBA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is essentially equivalent to the following:
	 * <pre>
	 * image.fillTriangle(triangle, (color, point) -> colorRGBA);
	 * </pre>
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param colorRGBA the {@link Color4F} to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code colorRGBA} are {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle, final Color4F colorRGBA) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		fillTriangle(triangle, (color, point) -> colorRGBA);
	}
	
	/**
	 * Fills {@code triangle} in this {@code ImageF} instance with {@link Color4F} instances returned by {@code biFunction} as its color.
	 * <p>
	 * If either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param triangle the {@link Triangle2I} to fill
	 * @param biFunction a {@code BiFunction} that returns {@code Color4F} instances to use as its color
	 * @throws NullPointerException thrown if, and only if, either {@code triangle} or {@code biFunction} are {@code null} or {@code biFunction} returns {@code null}
	 */
	public final void fillTriangle(final Triangle2I triangle, final BiFunction<Color4F, Point2I, Color4F> biFunction) {
		Objects.requireNonNull(triangle, "triangle == null");
		Objects.requireNonNull(biFunction, "biFunction == null");
		
		final Rectangle2I rectangle = new Rectangle2I(new Point2I(), new Point2I(getResolutionX(), getResolutionY()));
		
		final Point2I[][] scanlines = Rasterizer2I.rasterize(triangle, rectangle);
		
		for(final Point2I[] scanline : scanlines) {
			for(final Point2I point : scanline) {
				final int x = point.getX();
				final int y = point.getY();
				
				if(x >= 0 && x < this.resolutionX && y >= 0 && y < this.resolutionY) {
					final Color4F oldColorRGBA = getColorRGBA(x, y);
					final Color4F newColorRGBA = Objects.requireNonNull(biFunction.apply(oldColorRGBA, point));
					
					setColorRGBA(newColorRGBA, point.getX(), point.getY());
				}
			}
		}
	}
	
	/**
	 * Saves this {@code ImageF} as a .PNG image to the file represented by {@code file}.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public final void save(final File file) {
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
	 * Saves this {@code ImageF} as a .PNG image to the file represented by the pathname {@code pathname}.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.save(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to save to
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public final void save(final String pathname) {
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
	 * image.setColorRGB(colorRGB, index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGB the {@code Color3F} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGB} is {@code null}
	 */
	public final void setColorRGB(final Color3F colorRGB, final int index) {
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
	public final void setColorRGB(final Color3F colorRGB, final int index, final PixelOperation pixelOperation) {
		setColorRGBA(new Color4F(colorRGB), index, pixelOperation);
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
	public final void setColorRGB(final Color3F colorRGB, final int x, final int y) {
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
	public final void setColorRGB(final Color3F colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		setColorRGBA(new Color4F(colorRGB), x, y, pixelOperation);
	}
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param index the index of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void setColorRGBA(final Color4F colorRGBA, final int index) {
		setColorRGBA(colorRGBA, index, PixelOperation.NO_CHANGE);
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
	public abstract void setColorRGBA(final Color4F colorRGBA, final int index, final PixelOperation pixelOperation);
	
	/**
	 * Sets the {@link Color4F} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * If {@code colorRGBA} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
	 * }
	 * </pre>
	 * 
	 * @param colorRGBA the {@code Color4F} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @throws NullPointerException thrown if, and only if, {@code colorRGBA} is {@code null}
	 */
	public final void setColorRGBA(final Color4F colorRGBA, final int x, final int y) {
		setColorRGBA(colorRGBA, x, y, PixelOperation.NO_CHANGE);
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
	public abstract void setColorRGBA(final Color4F colorRGBA, final int x, final int y, final PixelOperation pixelOperation);
}