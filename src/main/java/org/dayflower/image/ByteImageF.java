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

import java.util.Arrays;
import java.util.Objects;

import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.Color3F;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code ByteImageF} is an {@link ImageF} implementation that stores individual pixels as four {@code byte} values in a {@code byte[]}.
 * <p>
 * The {@code byte} values for a single pixel are ordered as {@link ArrayComponentOrder#RGBA}.
 * <p>
 * Because each pixel is represented by four {@code byte} values, its associated {@code byte[]} has a length of {@code byteImage.getResolution() * 4}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ByteImageF extends ImageF {
	private final byte[] bytes;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ByteImageF} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ByteImageF(800, 800);
	 * }
	 * </pre>
	 */
	public ByteImageF() {
		this(800, 800);
	}
	
	/**
	 * Constructs a new {@code ByteImageF} instance from {@code byteImage}.
	 * <p>
	 * If {@code byteImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param byteImage a {@code ByteImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code byteImage} is {@code null}
	 */
	public ByteImageF(final ByteImageF byteImage) {
		super(byteImage.getResolutionX(), byteImage.getResolutionY());
		
		this.bytes = byteImage.bytes.clone();
	}
	
	/**
	 * Constructs a new {@code ByteImageF} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
	public ByteImageF(final int resolutionX, final int resolutionY) {
		super(resolutionX, resolutionY);
		
		this.bytes = new byte[getResolution() * 4];
	}
	
	/**
	 * Constructs a new {@code ByteImageF} instance for {@code bytes}.
	 * <p>
	 * If {@code bytes} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code bytes.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new ByteImageF(resolutionX, resolutionY, bytes, false);
	 * }
	 * </pre>
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param bytes the {@code byte[]} to copy
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code bytes.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code bytes} is {@code null}
	 */
	public ByteImageF(final int resolutionX, final int resolutionY, final byte[] bytes) {
		this(resolutionX, resolutionY, bytes, false);
	}
	
	/**
	 * Constructs a new {@code ByteImageF} instance for {@code bytes}.
	 * <p>
	 * If {@code bytes} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code bytes.length != resolutionX * resolutionY * 4}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * This constructor will either copy or wrap {@code bytes}, depending on the value of {@code isWrapping}. If {@code isWrapping} is {@code true}, {@code bytes} will be wrapped, which means it will be associated with this {@code ByteImageF} instance.
	 * If {@code isWrapping} is {@code false}, a copy of {@code bytes} will be associated with this {@code ByteImageF} instance.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param bytes the {@code byte[]} to copy or wrap
	 * @param isWrapping {@code true} if, and only if, this {@code ByteImageF} instance should wrap {@code bytes}, {@code false} otherwise
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0} or {@code bytes.length != resolutionX * resolutionY * 4}
	 * @throws NullPointerException thrown if, and only if, {@code bytes} is {@code null}
	 */
	public ByteImageF(final int resolutionX, final int resolutionY, final byte[] bytes, final boolean isWrapping) {
		super(resolutionX, resolutionY);
		
		this.bytes = ParameterArguments.requireExactArrayLength(isWrapping ? Objects.requireNonNull(bytes, "bytes == null") : Objects.requireNonNull(bytes, "bytes == null").clone(), getResolution() * 4, "bytes");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a copy of this {@code ByteImageF} instance.
	 * 
	 * @return a copy of this {@code ByteImageF} instance
	 */
	@Override
	public ByteImageF copy() {
		return new ByteImageF(this);
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
	@Override
	public Color3F getColorRGB(final int index, final PixelOperation pixelOperation) {
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			final int r = this.bytes[indexTransformed * 4 + 0] & 0xFF;
			final int g = this.bytes[indexTransformed * 4 + 1] & 0xFF;
			final int b = this.bytes[indexTransformed * 4 + 2] & 0xFF;
			
			return new Color3F(r, g, b);
		}
		
		return Color3F.BLACK;
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
	@Override
	public Color3F getColorRGB(final int x, final int y, final PixelOperation pixelOperation) {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			final int r = this.bytes[index * 4 + 0] & 0xFF;
			final int g = this.bytes[index * 4 + 1] & 0xFF;
			final int b = this.bytes[index * 4 + 2] & 0xFF;
			
			return new Color3F(r, g, b);
		}
		
		return Color3F.BLACK;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ByteImageF} instance.
	 * 
	 * @return a {@code String} representation of this {@code ByteImageF} instance
	 */
	@Override
	public String toString() {
		return String.format("new ByteImageF(%d, %d)", Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code ByteImageF} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ByteImageF}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ByteImageF} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ByteImageF}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ByteImageF)) {
			return false;
		} else if(getResolution() != ByteImageF.class.cast(object).getResolution()) {
			return false;
		} else if(getResolutionX() != ByteImageF.class.cast(object).getResolutionX()) {
			return false;
		} else if(getResolutionY() != ByteImageF.class.cast(object).getResolutionY()) {
			return false;
		} else if(!Arrays.equals(this.bytes, ByteImageF.class.cast(object).bytes)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a copy of the associated {@code byte[]}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * byteImage.getBytes(false);
	 * }
	 * </pre>
	 * 
	 * @return a copy of the associated {@code byte[]}
	 */
	public byte[] getBytes() {
		return getBytes(false);
	}
	
	/**
	 * Returns a copy of the associated {@code byte[]}, or the associated {@code byte[]} itself if {@code isWrapping} is {@code true}.
	 * 
	 * @param isWrapping {@code true} if, and only if, the associated {@code byte[]} should be returned, {@code false} otherwise
	 * @return a copy of the associated {@code byte[]}, or the associated {@code byte[]} itself if {@code isWrapping} is {@code true}
	 */
	public byte[] getBytes(final boolean isWrapping) {
		return isWrapping ? this.bytes : this.bytes.clone();
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code ByteImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code byte[]} representation of this {@code ByteImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	@Override
	public byte[] toByteArray(final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		final int resolution = getResolution();
		
		final byte[] byteArray = new byte[resolution * arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final byte r = this.bytes[i * 4 + 0];
			final byte g = this.bytes[i * 4 + 1];
			final byte b = this.bytes[i * 4 + 2];
			final byte a = this.bytes[i * 4 + 3];
			
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
	 * Returns a hash code for this {@code ByteImageF} instance.
	 * 
	 * @return a hash code for this {@code ByteImageF} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(getResolution()), Integer.valueOf(getResolutionX()), Integer.valueOf(getResolutionY()), Integer.valueOf(Arrays.hashCode(this.bytes)));
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code ByteImageF} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return an {@code int[]} representation of this {@code ByteImageF} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
	@Override
	public int[] toIntArray(final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution * arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < resolution; i++) {
			final int r = this.bytes[i * 4 + 0] & 0xFF;
			final int g = this.bytes[i * 4 + 1] & 0xFF;
			final int b = this.bytes[i * 4 + 2] & 0xFF;
			final int a = this.bytes[i * 4 + 3] & 0xFF;
			
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
	 * Returns an {@code int[]} representation of this {@code ByteImageF} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code ByteImageF} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	@Override
	public int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final int resolution = getResolution();
		
		final int[] intArray = new int[resolution];
		
		for(int i = 0; i < resolution; i++) {
			final int r = this.bytes[i * 4 + 0] & 0xFF;
			final int g = this.bytes[i * 4 + 1] & 0xFF;
			final int b = this.bytes[i * 4 + 2] & 0xFF;
			final int a = this.bytes[i * 4 + 3] & 0xFF;
			
			intArray[i] = packedIntComponentOrder.pack(r, g, b, a);
		}
		
		return intArray;
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
	@Override
	public void setColorRGB(final Color3F colorRGB, final int index, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			this.bytes[indexTransformed * 4 + 0] = colorRGB.getAsByteR();
			this.bytes[indexTransformed * 4 + 1] = colorRGB.getAsByteG();
			this.bytes[indexTransformed * 4 + 2] = colorRGB.getAsByteB();
		}
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
	@Override
	public void setColorRGB(final Color3F colorRGB, final int x, final int y, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGB, "colorRGB == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			this.bytes[index * 4 + 0] = colorRGB.getAsByteR();
			this.bytes[index * 4 + 1] = colorRGB.getAsByteG();
			this.bytes[index * 4 + 2] = colorRGB.getAsByteB();
		}
	}
}