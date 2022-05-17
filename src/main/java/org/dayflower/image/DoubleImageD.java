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
import org.dayflower.color.Color4D;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.awt.image.BufferedImages;
import org.macroing.java.util.Arrays;

/**
 * A {@code DoubleImageD} is an {@link ImageD} implementation that stores individual pixels as four {@code double} values in a {@code double[]}.
 * <p>
 * The {@code double} values for a single pixel are ordered as {@link ArrayComponentOrder#RGBA}.
 * <p>
 * Because each pixel is represented by four {@code double} values, its associated {@code double[]} has a length of {@code doubleImage.getResolution() * 4}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DoubleImageD extends ImageD {
	private final double[] data;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DoubleImageD} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DoubleImageD(800, 800);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code DoubleImageD} instance from {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD(final BufferedImage bufferedImage) {
		super(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		this.data = doGetData(bufferedImage);
	}
	
	/**
	 * Constructs a new {@code DoubleImageD} instance from {@code doubleImage}.
	 * <p>
	 * If {@code doubleImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param doubleImage a {@code DoubleImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code doubleImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD(final DoubleImageD doubleImage) {
		super(doubleImage.getResolutionX(), doubleImage.getResolutionY());
		
		this.data = doubleImage.getData();
	}
	
	/**
	 * Constructs a new {@code DoubleImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.data = Arrays.repeat(new double[] {0.0D, 0.0D, 0.0D, 1.0D}, resolutionX * resolutionY);
	}
	
	/**
	 * Constructs a new {@code DoubleImageD} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DoubleImageD(resolutionX, resolutionY, data, false);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code double[]} to copy
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD(final int resolutionX, final int resolutionY, final double[] data) {
		this(resolutionX, resolutionY, data, false);
	}
	
	/**
	 * Constructs a new {@code DoubleImageD} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * This constructor will either copy or wrap {@code data}, depending on the value of {@code isWrapping}. If {@code isWrapping} is {@code true}, {@code data} will be wrapped, which means it will be associated with this {@code DoubleImageD} instance.
	 * If {@code isWrapping} is {@code false}, a copy of {@code data} will be associated with this {@code DoubleImageD} instance.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code double[]} to copy or wrap
	 * @param isWrapping {@code true} if, and only if, this {@code DoubleImageD} instance should wrap {@code data}, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public DoubleImageD(final int resolutionX, final int resolutionY, final double[] data, final boolean isWrapping) {
		super(resolutionX, resolutionY);
		
		this.data = ParameterArguments.requireExactArrayLength(isWrapping ? Objects.requireNonNull(data, "data == null") : Objects.requireNonNull(data, "data == null").clone(), getResolution() * 4, "data");
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
//	TODO: Add Unit Tests!
	@Override
	public Color4D getColorRGBA(final int index, final PixelOperation pixelOperation) {
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			final double r = this.data[indexTransformed * 4 + 0];
			final double g = this.data[indexTransformed * 4 + 1];
			final double b = this.data[indexTransformed * 4 + 2];
			final double a = this.data[indexTransformed * 4 + 3];
			
			return new Color4D(r, g, b, a);
		}
		
		return Color4D.BLACK;
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
//	TODO: Add Unit Tests!
	@Override
	public Color4D getColorRGBA(final int x, final int y, final Function<Point2I, Color4D> function) {
		Objects.requireNonNull(function, "function == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
			final int index = y * resolutionX + x;
			
			final double r = this.data[index * 4 + 0];
			final double g = this.data[index * 4 + 1];
			final double b = this.data[index * 4 + 2];
			final double a = this.data[index * 4 + 3];
			
			return new Color4D(r, g, b, a);
		}
		
		return Objects.requireNonNull(function.apply(new Point2I(x, y)));
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
//	TODO: Add Unit Tests!
	@Override
	public Color4D getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			final double r = this.data[index * 4 + 0];
			final double g = this.data[index * 4 + 1];
			final double b = this.data[index * 4 + 2];
			final double a = this.data[index * 4 + 3];
			
			return new Color4D(r, g, b, a);
		}
		
		return Color4D.BLACK;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DoubleImageD} instance.
	 * 
	 * @return a {@code String} representation of this {@code DoubleImageD} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new DoubleImageD(%d, %d)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code DoubleImageD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DoubleImageD}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DoubleImageD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DoubleImageD}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		synchronized(this.data) {
			if(object == this) {
				return true;
			} else if(!(object instanceof DoubleImageD)) {
				return false;
			} else if(getResolution() != DoubleImageD.class.cast(object).getResolution()) {
				return false;
			} else if(getResolutionX() != DoubleImageD.class.cast(object).getResolutionX()) {
				return false;
			} else if(getResolutionY() != DoubleImageD.class.cast(object).getResolutionY()) {
				return false;
			} else if(!Arrays.equals(this.data, DoubleImageD.class.cast(object).data)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Returns a copy of the associated {@code double[]}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * doubleImage.getData(false);
	 * }
	 * </pre>
	 * 
	 * @return a copy of the associated {@code double[]}
	 */
//	TODO: Add Unit Tests!
	public double[] getData() {
		return getData(false);
	}
	
	/**
	 * Returns a copy of the associated {@code double[]}, or the associated {@code double[]} itself if {@code isWrapping} is {@code true}.
	 * 
	 * @param isWrapping {@code true} if, and only if, the associated {@code double[]} should be returned, {@code false} otherwise
	 * @return a copy of the associated {@code double[]}, or the associated {@code double[]} itself if {@code isWrapping} is {@code true}
	 */
//	TODO: Add Unit Tests!
	public double[] getData(final boolean isWrapping) {
		return isWrapping ? this.data : this.data.clone();
	}
	
	/**
	 * Returns a hash code for this {@code DoubleImageD} instance.
	 * 
	 * @return a hash code for this {@code DoubleImageD} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), Integer.valueOf(Arrays.hashCode(this.data)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code DoubleImageD} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code DoubleImageD} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			intArray[i] = getColorRGBA(i).pack(packedIntComponentOrder);
		}
		
		return intArray;
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
		
		final double rA = this.data[indexA * 4 + 0];
		final double gA = this.data[indexA * 4 + 1];
		final double bA = this.data[indexA * 4 + 2];
		final double aA = this.data[indexA * 4 + 3];
		
		final double rB = this.data[indexB * 4 + 0];
		final double gB = this.data[indexB * 4 + 1];
		final double bB = this.data[indexB * 4 + 2];
		final double aB = this.data[indexB * 4 + 3];
		
		this.data[indexA + 0] = rB;
		this.data[indexA + 1] = gB;
		this.data[indexA + 2] = bB;
		this.data[indexA + 3] = aB;
		
		this.data[indexB + 0] = rA;
		this.data[indexB + 1] = gA;
		this.data[indexB + 2] = bA;
		this.data[indexB + 3] = aA;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code 0.5D}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * DoubleImageD.blend(imageA, imageB, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @return a new {@code DoubleImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD blend(final ImageD imageA, final ImageD imageB) {
		return blend(imageA, imageB, 0.5D);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * DoubleImageD.blend(imageA, imageB, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code DoubleImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD blend(final ImageD imageA, final ImageD imageB, final double t) {
		return blend(imageA, imageB, t, t, t, t);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factors {@code tComponent1}, {@code tComponent2}, {@code tComponent3} and {@code tComponent4}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code DoubleImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD blend(final ImageD imageA, final ImageD imageB, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4) {
		final int imageAResolutionX = imageA.getResolutionX();
		final int imageAResolutionY = imageA.getResolutionY();
		
		final int imageBResolutionX = imageB.getResolutionX();
		final int imageBResolutionY = imageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(imageAResolutionX, imageBResolutionX);
		final int pixelImageCResolutionY = max(imageAResolutionY, imageBResolutionY);
		
		final DoubleImageD doubleImageC = new DoubleImageD(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = Color4D.blend(colorA, colorB, tComponent1, tComponent2, tComponent3, tComponent4);
				
				doubleImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return doubleImageC;
	}
	
	/**
	 * Creates a {@code DoubleImageD} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().x - rectangle.getA().x} or {@code rectangle.getC().y - rectangle.getA().y} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code DoubleImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().x - rectangle.getA().x} or {@code rectangle.getC().y - rectangle.getA().y} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD createScreenCapture(final Rectangle2I rectangle) {
		return new DoubleImageD(BufferedImages.createScreenCapture(rectangle.getA().x, rectangle.getA().y, rectangle.getC().x - rectangle.getA().x, rectangle.getC().y - rectangle.getA().y));
	}
	
	/**
	 * Returns a {@code DoubleImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code ImageD} instance
	 * @param imageB an {@code ImageD} instance
	 * @return a {@code DoubleImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD difference(final ImageD imageA, final ImageD imageB) {
		final int resolutionX = max(imageA.getResolutionX(), imageB.getResolutionX());
		final int resolutionY = max(imageA.getResolutionY(), imageB.getResolutionY());
		
		final DoubleImageD doubleImageC = new DoubleImageD(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = colorA.equals(colorB) ? colorA : Color4D.BLACK;
				
				doubleImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return doubleImageC;
	}
	
	/**
	 * Loads a {@code DoubleImageD} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code DoubleImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD load(final File file) {
		try {
			return new DoubleImageD(ImageIO.read(Objects.requireNonNull(file, "file == null")));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads a {@code DoubleImageD} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * DoubleImageD.load(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code DoubleImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD load(final String pathname) {
		return load(new File(pathname));
	}
	
	/**
	 * Loads a {@code DoubleImageD} from the URL represented by {@code uRL}.
	 * <p>
	 * Returns a new {@code DoubleImageD} instance.
	 * <p>
	 * If {@code uRL} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param uRL a {@code URL} that represents the URL to load from
	 * @return a new {@code DoubleImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code uRL} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static DoubleImageD load(final URL uRL) {
		try {
			return new DoubleImageD(ImageIO.read(Objects.requireNonNull(uRL, "uRL == null")));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code DoubleImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code DoubleImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	@Override
	protected DoubleImageD newImage(final int resolutionX, final int resolutionY) {
		return new DoubleImageD(resolutionX, resolutionY);
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
//	TODO: Add Unit Tests!
	@Override
	protected void putColorRGBA(final Color4D colorRGBA, final int index) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		if(index >= 0 && index < getResolution()) {
			this.data[index * 4 + 0] = colorRGBA.getR();
			this.data[index * 4 + 1] = colorRGBA.getG();
			this.data[index * 4 + 2] = colorRGBA.getB();
			this.data[index * 4 + 3] = colorRGBA.getA();
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doGetData(final BufferedImage bufferedImage) {
		final BufferedImage compatibleBufferedImage = BufferedImages.getCompatibleBufferedImage(bufferedImage);
		
		final DataBufferInt dataBufferInt = DataBufferInt.class.cast(compatibleBufferedImage.getRaster().getDataBuffer());
		
		final int[] dataInt = dataBufferInt.getData().clone();
		final int[] dataIntUnpacked = PackedIntComponentOrder.ARGB.unpack(ArrayComponentOrder.RGBA, dataInt);
		
		final double[] data = new double[dataIntUnpacked.length];
		
		for(int i = 0; i < data.length; i++) {
			data[i] = Ints.saturate(dataIntUnpacked[i]) / 255.0D;
		}
		
		return data;
	}
}