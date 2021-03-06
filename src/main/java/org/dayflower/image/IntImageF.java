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

import static org.dayflower.utility.Ints.max;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color4F;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.java.awt.image.BufferedImages;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

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
	public IntImageF(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.data = Ints.array(resolutionX * resolutionY, PackedIntComponentOrder.ARGB.pack(0, 0, 0, 255));
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
	@Override
	public Color4F getColorRGBA(final int index, final PixelOperation pixelOperation) {
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			final int colorARGB = this.data[indexTransformed];
			
			final int r = PackedIntComponentOrder.ARGB.unpackR(colorARGB);
			final int g = PackedIntComponentOrder.ARGB.unpackG(colorARGB);
			final int b = PackedIntComponentOrder.ARGB.unpackB(colorARGB);
			final int a = PackedIntComponentOrder.ARGB.unpackA(colorARGB);
			
			return new Color4F(r, g, b, a);
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
	@Override
	public Color4F getColorRGBA(final int x, final int y, final Function<Point2I, Color4F> function) {
		Objects.requireNonNull(function, "function == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
			final int index = y * resolutionX + x;
			
			final int colorARGB = this.data[index];
			
			final int r = PackedIntComponentOrder.ARGB.unpackR(colorARGB);
			final int g = PackedIntComponentOrder.ARGB.unpackG(colorARGB);
			final int b = PackedIntComponentOrder.ARGB.unpackB(colorARGB);
			final int a = PackedIntComponentOrder.ARGB.unpackA(colorARGB);
			
			return new Color4F(r, g, b, a);
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
	@Override
	public Color4F getColorRGBA(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			final int colorARGB = this.data[index];
			
			final int r = PackedIntComponentOrder.ARGB.unpackR(colorARGB);
			final int g = PackedIntComponentOrder.ARGB.unpackG(colorARGB);
			final int b = PackedIntComponentOrder.ARGB.unpackB(colorARGB);
			final int a = PackedIntComponentOrder.ARGB.unpackA(colorARGB);
			
			return new Color4F(r, g, b, a);
		}
		
		return Color4F.BLACK;
	}
	
	/**
	 * Returns a copy of this {@code IntImageF} instance.
	 * 
	 * @return a copy of this {@code IntImageF} instance
	 */
	@Override
	public IntImageF copy() {
		return new IntImageF(this);
	}
	
	/**
	 * Returns a copy of this {@code IntImageF} instance within {@code bounds}.
	 * <p>
	 * If {@code bounds} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bounds a {@link Rectangle2I} instance that represents the bounds within this {@code IntImageF} instance to copy
	 * @return a copy of this {@code IntImageF} instance within {@code bounds}
	 * @throws NullPointerException thrown if, and only if, {@code bounds} is {@code null}
	 */
	@Override
	public IntImageF copy(final Rectangle2I bounds) {
		Objects.requireNonNull(bounds, "bounds == null");
		
		final IntImageF intImageSource = this;
		
		final Rectangle2I boundsSource = intImageSource.getBounds();
		
		final Optional<Rectangle2I> optionalBoundsTarget = Rectangle2I.intersection(boundsSource, bounds);
		
		if(optionalBoundsTarget.isPresent()) {
			final Rectangle2I boundsTarget = optionalBoundsTarget.get();
			
			final Point2I originTarget = boundsTarget.getA();
			
			final int originTargetX = originTarget.getX();
			final int originTargetY = originTarget.getY();
			
			final int sourceResolutionX = boundsSource.getWidth();
			
			final int targetResolutionX = boundsTarget.getWidth();
			final int targetResolutionY = boundsTarget.getHeight();
			
			final IntImageF intImageTarget = new IntImageF(targetResolutionX, targetResolutionY);
			
			for(int y = 0; y < targetResolutionY; y++) {
				for(int x = 0; x < targetResolutionX; x++) {
					intImageTarget.data[y * targetResolutionX + x] = intImageSource.data[(y + originTargetY) * sourceResolutionX + (x + originTargetX)];
				}
			}
			
			return intImageTarget;
		}
		
		return new IntImageF(0, 0);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code IntImageF} instance.
	 * 
	 * @return a {@code String} representation of this {@code IntImageF} instance
	 */
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
	public int[] getData() {
		return getData(false);
	}
	
	/**
	 * Returns a copy of the associated {@code int[]}, or the associated {@code int[]} itself if {@code isWrapping} is {@code true}.
	 * 
	 * @param isWrapping {@code true} if, and only if, the associated {@code int[]} should be returned, {@code false} otherwise
	 * @return a copy of the associated {@code int[]}, or the associated {@code int[]} itself if {@code isWrapping} is {@code true}
	 */
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
	 * If either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@link Rectangle2I} that contains the bounds
	 * @return a new {@code IntImageF} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code rectangle.getC().getX() - rectangle.getA().getX()} or {@code rectangle.getC().getY() - rectangle.getA().getY()} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
	public static IntImageF createScreenCapture(final Rectangle2I rectangle) {
		return new IntImageF(BufferedImages.createScreenCapture(rectangle.getA().getX(), rectangle.getA().getY(), rectangle.getC().getX() - rectangle.getA().getX(), rectangle.getC().getY() - rectangle.getA().getY()));
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
	public static IntImageF load(final File file) {
		try {
			return new IntImageF(BufferedImages.getCompatibleBufferedImage(ImageIO.read(Objects.requireNonNull(file, "file == null"))));
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
	public static IntImageF load(final String pathname) {
		return load(new File(pathname));
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
	@Override
	protected void putColorRGBA(final Color4F colorRGBA, final int index) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		
		if(index >= 0 && index < this.data.length) {
			this.data[index] = colorRGBA.pack();
		}
	}
}