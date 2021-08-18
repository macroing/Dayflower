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

import static org.dayflower.utility.Bytes.toByte;
import static org.dayflower.utility.Ints.max;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color4D;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.java.awt.image.BufferedImages;
import org.dayflower.utility.ByteArrays;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code ByteImageD} is an {@link ImageD} implementation that stores individual pixels as four {@code byte} values in a {@code byte[]}.
 * <p>
 * The {@code byte} values for a single pixel are ordered as {@link ArrayComponentOrder#RGBA}.
 * <p>
 * Because each pixel is represented by four {@code byte} values, its associated {@code byte[]} has a length of {@code byteImage.getResolution() * 4}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ByteImageD extends ImageD {
	private final byte[] data;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ByteImageD} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ByteImageD(800, 800);
	 * }
	 * </pre>
	 */
	public ByteImageD() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code ByteImageD} instance from {@code bufferedImage}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public ByteImageD(final BufferedImage bufferedImage) {
		super(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		this.data = ByteArrays.convert(PackedIntComponentOrder.ARGB.unpack(ArrayComponentOrder.RGBA, DataBufferInt.class.cast(BufferedImages.getCompatibleBufferedImage(bufferedImage).getRaster().getDataBuffer()).getData().clone()));
	}
	
	/**
	 * Constructs a new {@code ByteImageD} instance from {@code byteImage}.
	 * <p>
	 * If {@code byteImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param byteImage a {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code byteImage} is {@code null}
	 */
	public ByteImageD(final ByteImageD byteImage) {
		super(byteImage.getResolutionX(), byteImage.getResolutionY());
		
		this.data = byteImage.getData();
	}
	
	/**
	 * Constructs a new {@code ByteImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public ByteImageD(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.data = ByteArrays.create(resolutionX * resolutionY * 4, toByte(0), toByte(0), toByte(0), toByte(255));
	}
	
	/**
	 * Constructs a new {@code ByteImageD} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ByteImageD(resolutionX, resolutionY, data, false);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code byte[]} to copy
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
	public ByteImageD(final int resolutionX, final int resolutionY, final byte[] data) {
		this(resolutionX, resolutionY, data, false);
	}
	
	/**
	 * Constructs a new {@code ByteImageD} instance for {@code data}.
	 * <p>
	 * If {@code data} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * This constructor will either copy or wrap {@code data}, depending on the value of {@code isWrapping}. If {@code isWrapping} is {@code true}, {@code data} will be wrapped, which means it will be associated with this {@code ByteImageD} instance.
	 * If {@code isWrapping} is {@code false}, a copy of {@code data} will be associated with this {@code ByteImageD} instance.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param data the {@code byte[]} to copy or wrap
	 * @param isWrapping {@code true} if, and only if, this {@code ByteImageD} instance should wrap {@code data}, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code data.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code data} is {@code null}
	 */
	public ByteImageD(final int resolutionX, final int resolutionY, final byte[] data, final boolean isWrapping) {
		super(resolutionX, resolutionY);
		
		this.data = ParameterArguments.requireExactArrayLength(isWrapping ? Objects.requireNonNull(data, "data == null") : Objects.requireNonNull(data, "data == null").clone(), getResolution() * 4, "data");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a copy of this {@code ByteImageD} instance.
	 * 
	 * @return a copy of this {@code ByteImageD} instance
	 */
	@Override
	public ByteImageD copy() {
		return new ByteImageD(this);
	}
	
	/**
	 * Returns a copy of this {@code ByteImageD} instance within {@code bounds}.
	 * <p>
	 * If {@code bounds} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bounds a {@link Rectangle2I} instance that represents the bounds within this {@code ByteImageD} instance to copy
	 * @return a copy of this {@code ByteImageD} instance within {@code bounds}
	 * @throws NullPointerException thrown if, and only if, {@code bounds} is {@code null}
	 */
	@Override
	public ByteImageD copy(final Rectangle2I bounds) {
		Objects.requireNonNull(bounds, "bounds == null");
		
		final ByteImageD byteImageSource = this;
		
		final Rectangle2I boundsSource = byteImageSource.getBounds();
		
		final Optional<Rectangle2I> optionalBoundsTarget = Rectangle2I.intersection(boundsSource, bounds);
		
		if(optionalBoundsTarget.isPresent()) {
			final Rectangle2I boundsTarget = optionalBoundsTarget.get();
			
			final Point2I originTarget = boundsTarget.getA();
			
			final int originTargetX = originTarget.getX();
			final int originTargetY = originTarget.getY();
			
			final int sourceResolutionX = boundsSource.getWidth();
			
			final int targetResolutionX = boundsTarget.getWidth();
			final int targetResolutionY = boundsTarget.getHeight();
			
			final ByteImageD byteImageTarget = new ByteImageD(targetResolutionX, targetResolutionY);
			
			for(int y = 0; y < targetResolutionY; y++) {
				for(int x = 0; x < targetResolutionX; x++) {
					final int sourceIndex = ((y + originTargetY) * sourceResolutionX + (x + originTargetX)) * 4;
					final int targetIndex = (y * targetResolutionX + x) * 4;
					
					byteImageTarget.data[targetIndex + 0] = byteImageSource.data[sourceIndex + 0];
					byteImageTarget.data[targetIndex + 1] = byteImageSource.data[sourceIndex + 1];
					byteImageTarget.data[targetIndex + 2] = byteImageSource.data[sourceIndex + 2];
					byteImageTarget.data[targetIndex + 3] = byteImageSource.data[sourceIndex + 3];
				}
			}
			
			return byteImageTarget;
		}
		
		return new ByteImageD(0, 0);
	}
	
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
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			final int r = this.data[indexTransformed * 4 + 0] & 0xFF;
			final int g = this.data[indexTransformed * 4 + 1] & 0xFF;
			final int b = this.data[indexTransformed * 4 + 2] & 0xFF;
			final int a = this.data[indexTransformed * 4 + 3] & 0xFF;
			
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
	@Override
	public Color4D getColorRGBA(final int x, final int y, final Function<Point2I, Color4D> function) {
		Objects.requireNonNull(function, "function == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
			final int index = y * resolutionX + x;
			
			final int r = this.data[index * 4 + 0] & 0xFF;
			final int g = this.data[index * 4 + 1] & 0xFF;
			final int b = this.data[index * 4 + 2] & 0xFF;
			final int a = this.data[index * 4 + 3] & 0xFF;
			
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
	@Override
	public Color4D getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			final int r = this.data[index * 4 + 0] & 0xFF;
			final int g = this.data[index * 4 + 1] & 0xFF;
			final int b = this.data[index * 4 + 2] & 0xFF;
			final int a = this.data[index * 4 + 3] & 0xFF;
			
			return new Color4D(r, g, b, a);
		}
		
		return Color4D.BLACK;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ByteImageD} instance.
	 * 
	 * @return a {@code String} representation of this {@code ByteImageD} instance
	 */
	@Override
	public String toString() {
		return String.format("new ByteImageD(%d, %d)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code ByteImageD} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ByteImageD}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ByteImageD} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ByteImageD}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		synchronized(this.data) {
			if(object == this) {
				return true;
			} else if(!(object instanceof ByteImageD)) {
				return false;
			} else if(getResolution() != ByteImageD.class.cast(object).getResolution()) {
				return false;
			} else if(getResolutionX() != ByteImageD.class.cast(object).getResolutionX()) {
				return false;
			} else if(getResolutionY() != ByteImageD.class.cast(object).getResolutionY()) {
				return false;
			} else if(!Arrays.equals(this.data, ByteImageD.class.cast(object).data)) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * Returns a copy of the associated {@code byte[]}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * byteImage.getData(false);
	 * }
	 * </pre>
	 * 
	 * @return a copy of the associated {@code byte[]}
	 */
	public byte[] getData() {
		return getData(false);
	}
	
	/**
	 * Returns a copy of the associated {@code byte[]}, or the associated {@code byte[]} itself if {@code isWrapping} is {@code true}.
	 * 
	 * @param isWrapping {@code true} if, and only if, the associated {@code byte[]} should be returned, {@code false} otherwise
	 * @return a copy of the associated {@code byte[]}, or the associated {@code byte[]} itself if {@code isWrapping} is {@code true}
	 */
	public byte[] getData(final boolean isWrapping) {
		return isWrapping ? this.data : this.data.clone();
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code ByteImageD} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code byte[]} representation of this {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	@Override
	public byte[] toByteArray(final ArrayComponentOrder arrayComponentOrder) {
		return ArrayComponentOrder.convert(ArrayComponentOrder.RGBA, Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null"), this.data);
	}
	
	/**
	 * Returns a hash code for this {@code ByteImageD} instance.
	 * 
	 * @return a hash code for this {@code ByteImageD} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), Integer.valueOf(Arrays.hashCode(this.data)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ByteImageD} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code ByteImageD} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		return Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null").pack(ArrayComponentOrder.RGBA, this.data);
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
		
		final byte rA = this.data[indexA * 4 + 0];
		final byte gA = this.data[indexA * 4 + 1];
		final byte bA = this.data[indexA * 4 + 2];
		final byte aA = this.data[indexA * 4 + 3];
		
		final byte rB = this.data[indexB * 4 + 0];
		final byte gB = this.data[indexB * 4 + 1];
		final byte bB = this.data[indexB * 4 + 2];
		final byte aB = this.data[indexB * 4 + 3];
		
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
	 * Returns a new {@code ByteImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ByteImageD.blend(imageA, imageB, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @return a new {@code ByteImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static ByteImageD blend(final ImageD imageA, final ImageD imageB) {
		return blend(imageA, imageB, 0.5D);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factor {@code t}.
	 * <p>
	 * Returns a new {@code ByteImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ByteImageD.blend(imageA, imageB, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code ByteImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static ByteImageD blend(final ImageD imageA, final ImageD imageB, final double t) {
		return blend(imageA, imageB, t, t, t, t);
	}
	
	/**
	 * Blends {@code imageA} and {@code imageB} using the factors {@code tComponent1}, {@code tComponent2}, {@code tComponent3} and {@code tComponent4}.
	 * <p>
	 * Returns a new {@code ByteImageD} instance with the result of the blend operation.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA one of the {@code ImageD} instances to blend
	 * @param imageB one of the {@code ImageD} instances to blend
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code ByteImageD} instance with the result of the blend operation
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static ByteImageD blend(final ImageD imageA, final ImageD imageB, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4) {
		final int imageAResolutionX = imageA.getResolutionX();
		final int imageAResolutionY = imageA.getResolutionY();
		
		final int imageBResolutionX = imageB.getResolutionX();
		final int imageBResolutionY = imageB.getResolutionY();
		
		final int pixelImageCResolutionX = max(imageAResolutionX, imageBResolutionX);
		final int pixelImageCResolutionY = max(imageAResolutionY, imageBResolutionY);
		
		final ByteImageD byteImageC = new ByteImageD(pixelImageCResolutionX, pixelImageCResolutionY);
		
		for(int y = 0; y < pixelImageCResolutionY; y++) {
			for(int x = 0; x < pixelImageCResolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = Color4D.blend(colorA, colorB, tComponent1, tComponent2, tComponent3, tComponent4);
				
				byteImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return byteImageC;
	}
	
	/**
	 * Creates a {@code ByteImageD} by capturing the contents of the screen, without the mouse cursor.
	 * <p>
	 * Returns a new {@code ByteImageD} instance.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code ByteImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
	public static ByteImageD createScreenCapture(final Rectangle2I rectangle) {
		return new ByteImageD(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
	}
	
	/**
	 * Returns a {@code ByteImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}.
	 * <p>
	 * If either {@code imageA} or {@code imageB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param imageA an {@code ImageD} instance
	 * @param imageB an {@code ImageD} instance
	 * @return a {@code ByteImageD} that shows the difference between {@code imageA} and {@code imageB} with {@code Color4D.BLACK}
	 * @throws NullPointerException thrown if, and only if, either {@code imageA} or {@code imageB} are {@code null}
	 */
	public static ByteImageD difference(final ImageD imageA, final ImageD imageB) {
		final int resolutionX = max(imageA.getResolutionX(), imageB.getResolutionX());
		final int resolutionY = max(imageA.getResolutionY(), imageB.getResolutionY());
		
		final ByteImageD byteImageC = new ByteImageD(resolutionX, resolutionY);
		
		for(int y = 0; y < resolutionY; y++) {
			for(int x = 0; x < resolutionX; x++) {
				final Color4D colorA = imageA.getColorRGBA(x, y);
				final Color4D colorB = imageB.getColorRGBA(x, y);
				final Color4D colorC = colorA.equals(colorB) ? colorA : Color4D.BLACK;
				
				byteImageC.setColorRGBA(colorC, x, y);
			}
		}
		
		return byteImageC;
	}
	
	/**
	 * Loads a {@code ByteImageD} from the file represented by {@code file}.
	 * <p>
	 * Returns a new {@code ByteImageD} instance.
	 * <p>
	 * If {@code file} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to load from
	 * @return a new {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ByteImageD load(final File file) {
		try {
			return new ByteImageD(BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null"))));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Loads a {@code ByteImageD} from the file represented by the pathname {@code pathname}.
	 * <p>
	 * Returns a new {@code ByteImageD} instance.
	 * <p>
	 * If {@code pathname} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * ByteImageD.load(new File(pathname));
	 * }
	 * </pre>
	 * 
	 * @param pathname a {@code String} that represents the pathname of the file to load from
	 * @return a new {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ByteImageD load(final String pathname) {
		return load(new File(pathname));
	}
	
	/**
	 * Loads a {@code ByteImageD} from the URL represented by {@code uRL}.
	 * <p>
	 * Returns a new {@code ByteImageD} instance.
	 * <p>
	 * If {@code uRL} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param uRL a {@code URL} that represents the URL to load from
	 * @return a new {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code uRL} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static ByteImageD load(final URL uRL) {
		try {
			return new ByteImageD(BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(uRL, "uRL == null"))));
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a new {@code ByteImageD} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @return a new {@code ByteImageD} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	@Override
	protected ByteImageD newImage(final int resolutionX, final int resolutionY) {
		return new ByteImageD(resolutionX, resolutionY);
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
		
		if(index >= 0 && index < getResolution()) {
			this.data[index * 4 + 0] = colorRGBA.getAsByteR();
			this.data[index * 4 + 1] = colorRGBA.getAsByteG();
			this.data[index * 4 + 2] = colorRGBA.getAsByteB();
			this.data[index * 4 + 3] = colorRGBA.getAsByteA();
		}
	}
}