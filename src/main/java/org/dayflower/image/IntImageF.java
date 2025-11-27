/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import static org.dayflower.utility.Ints.max;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color4F;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.awt.image.BufferedImages;
import org.macroing.java.util.Arrays;

/**
 * An {@code IntImageF} is an {@link ImageF} implementation that stores individual pixels as a packed {@code int} value in an {@code int[]}.
 * <p>
 * The packed {@code int} value for a single pixel is ordered as {@link PackedIntComponentOrder#ARGB}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IntImageF extends ImageF {
	private final int[] data;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IntImageF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new IntImageF(800, 800);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public IntImageF() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code IntImageF} instance from {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public IntImageF(final BufferedImage bufferedImage) {
		super(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		this.data = DataBufferInt.class.cast(BufferedImages.getCompatibleBufferedImage(bufferedImage).getRaster().getDataBuffer()).getData().clone();
	}
	
	/**
	 * Constructs a new {@code IntImageF} instance from {@code intImage}.
	 * <p>
	 * If {@code intImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param intImage an {@code IntImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code intImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public IntImageF(final IntImageF intImage) {
		super(intImage.getResolutionX(), intImage.getResolutionY());
		
		this.data = intImage.data.clone();
	}
	
	/**
	 * Constructs a new {@code IntImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public IntImageF(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.data = Arrays.repeat(new int[] {PackedIntComponentOrder.ARGB.pack(0, 0, 0, 255)}, resolutionX * resolutionY);
	}
	
	/**
	 * Constructs a new {@code IntImageF} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new IntImageF(resolutionX, resolutionY, data, false);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code int[]} to copy
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public IntImageF(final int resolutionX, final int resolutionY, final int[] data) {
		this(resolutionX, resolutionY, data, false);
	}
	
	/**
	 * Constructs a new {@code IntImageF} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * This constructor will either copy or wrap {@code data}, depending on the value of {@code isWrapping}. If {@code isWrapping} is {@code true}, {@code data} will be wrapped, which means it will be associated with this {@code IntImageF} instance.
	 * If {@code isWrapping} is {@code false}, a copy of {@code data} will be associated with this {@code IntImageF} instance.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code int[]} to copy or wrap
	 * @param isWrapping {@code true} if, and only if, this {@code IntImageF} instance should wrap {@code data}, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public IntImageF(final int resolutionX, final int resolutionY, final int[] data, final boolean isWrapping) {
		super(resolutionX, resolutionY);
		
		this.data = ParameterArguments.requireExactArrayLength(isWrapping ? Objects.requireNonNull(data, "data == null") : Objects.requireNonNull(data, "data == null").clone(), getResolution(), "data");
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
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			return Color4F.unpack(this.data[indexTransformed], PackedIntComponentOrder.ARGB);
		}
		
		return Color4F.BLACK;
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
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
			return Color4F.unpack(this.data[y * resolutionX + x], PackedIntComponentOrder.ARGB);
		}
		
		return Objects.requireNonNull(function.apply(new Point2I(x, y)));
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
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			return Color4F.unpack(this.data[yTransformed * resolutionX + xTransformed], PackedIntComponentOrder.ARGB);
		}
		
		return Color4F.BLACK;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code IntImageF} instance.
	 * 
	 * @return a {@code String} representation of this {@code IntImageF} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new IntImageF(%d, %d)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code IntImageF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code IntImageF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code IntImageF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code IntImageF}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof IntImageF)) {
			return false;
		} else if(getResolution() != IntImageF.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != IntImageF.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != IntImageF.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Arrays.equals(this.data, IntImageF.class.cast(object).data)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code IntImageF} instance.
	 * 
	 * @return a hash code for this {@code IntImageF} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), Integer.valueOf(Arrays.hashCode(this.data)));
	}
	
	/**
	 * Returns a copy of the associated {@code int[]}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * intImage.getData(false);
	 * }
	 * </pre>
	 * 
	 * @return a copy of the associated {@code int[]}
	 */
//	TODO: Add Unit Tests!
	public int[] getData() {
		return getData(false);
	}
	
	/**
	 * Returns a copy of the associated {@code int[]}, or the associated {@code int[]} itself if {@code isWrapping} is {@code true}.
	 * 
	 * @param isWrapping {@code true} if, and only if, the associated {@code int[]} should be returned, {@code false} otherwise
	 * @return a copy of the associated {@code int[]}, or the associated {@code int[]} itself if {@code isWrapping} is {@code true}
	 */
//	TODO: Add Unit Tests!
	public int[] getData(final boolean isWrapping) {
		return isWrapping ? this.data : this.data.clone();
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code IntImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return an {@code int[]} representation of this {@code IntImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public int[] toIntArray(final ArrayComponentOrder arrayComponentOrder) {
		return PackedIntComponentOrder.ARGB.unpack(Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null"), this.data);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code IntImageF} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code IntImageF} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		return PackedIntComponentOrder.convert(PackedIntComponentOrder.ARGB, Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null"), this.data);
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
		
		final int colorARGBA = this.data[indexA];
		final int colorARGBB = this.data[indexB];
		
		this.data[indexA] = colorARGBB;
		this.data[indexB] = colorARGBA;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code 0.5F}.
	 * <p>
	 * Returns a new {@code IntImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntImageF.blend(imageA, imageB, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @return a new {@code IntImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static IntImageF blend(final ImageF imageA, final ImageF imageB) {
		return blend(imageA, imageB, 0.5F);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code IntImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntImageF.blend(imageA, imageB, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code IntImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static IntImageF blend(final ImageF imageA, final ImageF imageB, final float t) {
		return blend(imageA, imageB, t, t, t, t);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factors {@code tComponent1}, {@code tComponent2}, {@code tComponent3} and {@code tComponent4}.
	 * <p>
	 * Returns a new {@code IntImageF} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA one of the {@code ImageF} instances to blend
	 * @param imageB one of the {@code ImageF} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code IntImageF} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static IntImageF blend(final ImageF imageA, final ImageF imageB, final float tComponent1, final float tComponent2, final float tComponent3, final float tComponent4) {
		final int imageAResolutionX = imageA.getResolutionX();
		final int imageAResolutionY = imageA.getResolutionY();
		
		final int imageBResolutionX = imageB.getResolutionX();
		final int imageBResolutionY = imageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(imageAResolutionX, imageBResolutionX);
		final int pixelImageCResolutionY = max(imageAResolutionY, imageBResolutionY);
		
		final IntImageF intImageC = new IntImageF(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color4F colorA = imageA.getColorRGBA(x, y);
				final Color4F colorB = imageB.getColorRGBA(x, y);
				final Color4F colorC = Color4F.blend(colorA, colorB, tComponent1, tComponent2, tComponent3, tComponent4);
				
				intImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return intImageC;
	}
	
	/**
	 * Creates an {@code IntImageF} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code IntImageF} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().x - rectangle.getA().x} or {@code rectangle.getC().y - rectangle.getA().y} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code IntImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().x - rectangle.getA().x} or {@code rectangle.getC().y - rectangle.getA().y} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
//	TODO: Add Unit Tests!
	public static IntImageF createScreenCapture(final Rectangle2I rectangle) {
		return new IntImageF(BufferedImages.createScreenCapture(rectangle.getA().x, rectangle.getA().y, rectangle.getC().x - rectangle.getA().x, rectangle.getC().y - rectangle.getA().y));
	}
	
	/**
	 * Returns an {@code IntImageF} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4F.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code ImageF} instance
	 * @param imageB an {@code ImageF} instance
	 * @return an {@code IntImageF} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4F.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static IntImageF difference(final ImageF imageA, final ImageF imageB) {
		final int resolutionX = max(imageA.getResolutionX(), imageB.getResolutionX());
		final int resolutionY = max(imageA.getResolutionY(), imageB.getResolutionY());
		
		final IntImageF intImageC = new IntImageF(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color4F colorA = imageA.getColorRGBA(x, y);
				final Color4F colorB = imageB.getColorRGBA(x, y);
				final Color4F colorC = colorA.equals(colorB) ? colorA : Color4F.BLACK;
				
				intImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return intImageC;
	}
	
	/**
	 * Loads an {@code IntImageF} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code IntImageF} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code IntImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static IntImageF load(final File file) {
		try {
			return new IntImageF(ImageIO.read(Objects.requireNonNull(file, "file == null")));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads an {@code IntImageF} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code IntImageF} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * IntImageF.load(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code IntImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static IntImageF load(final String pathname) {
		return load(new File(pathname));
	}
	
	/**
	 * Loads an {@code IntImageF} from the URL represented by {@code uRL}.
	 * <p>
	 * Returns a new {@code IntImageF} instance.
	 * <p>
	 * If {@code uRL} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param uRL a {@code URL} that represents the URL to load from
	 * @return a new {@code IntImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code uRL} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static IntImageF load(final URL uRL) {
		try {
			return new IntImageF(ImageIO.read(Objects.requireNonNull(uRL, "uRL == null")));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code IntImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code IntImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected IntImageF newImage(final int resolutionX, final int resolutionY) {
		return new IntImageF(resolutionX, resolutionY);
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
		
		if(index >= 0 && index < this.data.length) {
			this.data[index] = colorRGBA.pack();
		}
	}
}