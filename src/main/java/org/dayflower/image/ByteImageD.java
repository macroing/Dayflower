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
import org.dayflower.color.Color4D;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.utility.Bytes;
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
	 * Constructs a new {@code ByteImageD} instance from {@code byteImage}.
	 * <p>
	 * If {@code byteImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param byteImage a {@code ByteImageD} instance
	 * @throws NullPointerException thrown if, and only if, {@code byteImage} is {@code null}
	 */
	public ByteImageD(final ByteImageD byteImage) {
		super(byteImage.getResolutionX(), byteImage.getResolutionY());
		
		this.data = byteImage.data.clone();
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
		
		this.data = Bytes.array(resolutionX * resolutionY * 4, (byte)(0), (byte)(0), (byte)(0), (byte)(255));
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
	 * Sets the {@link Color4D} of the pixel represented by {@code index} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param index the index of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	@Override
	public void setColorRGBA(final Color4D colorRGBA, final int index, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolution = getResolution();
		
		final int indexTransformed = pixelOperation.getIndex(index, resolution);
		
		if(indexTransformed >= 0 && indexTransformed < resolution) {
			this.data[indexTransformed * 4 + 0] = colorRGBA.getAsByteR();
			this.data[indexTransformed * 4 + 1] = colorRGBA.getAsByteG();
			this.data[indexTransformed * 4 + 2] = colorRGBA.getAsByteB();
			this.data[indexTransformed * 4 + 3] = colorRGBA.getAsByteA();
		}
	}
	
	/**
	 * Sets the {@link Color4D} of the pixel represented by {@code x} and {@code y} to {@code colorRGBA}.
	 * <p>
	 * If either {@code colorRGBA} or {@code pixelOperation} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * See the documentation for {@link PixelOperation} to get a more detailed explanation for different pixel operations.
	 * 
	 * @param colorRGBA the {@code Color4D} to set
	 * @param x the X-coordinate of the pixel
	 * @param y the Y-coordinate of the pixel
	 * @param pixelOperation the {@code PixelOperation} to use
	 * @throws NullPointerException thrown if, and only if, either {@code colorRGBA} or {@code pixelOperation} are {@code null}
	 */
	@Override
	public void setColorRGBA(final Color4D colorRGBA, final int x, final int y, final PixelOperation pixelOperation) {
		Objects.requireNonNull(colorRGBA, "colorRGBA == null");
		Objects.requireNonNull(pixelOperation, "pixelOperation == null");
		
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		final int xTransformed = pixelOperation.getX(x, resolutionX);
		final int yTransformed = pixelOperation.getY(y, resolutionY);
		
		if(xTransformed >= 0 && xTransformed < resolutionX && yTransformed >= 0 && yTransformed < resolutionY) {
			final int index = yTransformed * resolutionX + xTransformed;
			
			this.data[index * 4 + 0] = colorRGBA.getAsByteR();
			this.data[index * 4 + 1] = colorRGBA.getAsByteG();
			this.data[index * 4 + 2] = colorRGBA.getAsByteB();
			this.data[index * 4 + 3] = colorRGBA.getAsByteA();
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
}