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

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.imageio.ImageIO;

import org.dayflower.change.ChangeHistory;
import org.dayflower.color.ArrayComponentOrder;
import org.dayflower.color.PackedIntComponentOrder;
import org.dayflower.geometry.Point2I;
import org.dayflower.geometry.Shape2I;
import org.dayflower.geometry.shape.Rectangle2I;
import org.dayflower.utility.ByteArrays;
import org.dayflower.utility.ParameterArguments;

import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

/**
 * An {@code Image} represents an image.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public abstract class Image {
	private final AtomicBoolean isChangeHistoryEnabled;
	private final ChangeHistory changeHistory;
	private final int resolution;
	private final int resolutionX;
	private final int resolutionY;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Image} instance.
	 * <p>
	 * If either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @throws IllegalArgumentException thrown if, and only if, either {@code resolutionX}, {@code resolutionY} or {@code resolutionX * resolutionY} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	protected Image(final int resolutionX, final int resolutionY) {
		this.resolutionX = ParameterArguments.requireRange(resolutionX, 0, Integer.MAX_VALUE, "resolutionX");
		this.resolutionY = ParameterArguments.requireRange(resolutionY, 0, Integer.MAX_VALUE, "resolutionY");
		this.resolution = ParameterArguments.requireRange(resolutionX * resolutionY, 0, Integer.MAX_VALUE, "resolutionX * resolutionY");
		this.isChangeHistoryEnabled = new AtomicBoolean();
		this.changeHistory = new ChangeHistory();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} representation of this {@code Image} instance.
	 * <p>
	 * If either {@code image.getResolutionX()} or {@code image.getResolutionY()} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @return a {@code BufferedImage} representation of this {@code Image} instance
	 * @throws IllegalArgumentException thrown if, and only if, either {@code image.getResolutionX()} or {@code image.getResolutionY()} are less than or equal to {@code 0}
	 */
//	TODO: Add Unit Tests!
	public final BufferedImage toBufferedImage() {
		final BufferedImage bufferedImage = new BufferedImage(this.resolutionX, this.resolutionY, BufferedImage.TYPE_INT_ARGB);
		
		final int[] dataSource = toIntArrayPackedForm();
		final int[] dataTarget = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
		
		System.arraycopy(dataSource, 0, dataTarget, 0, dataSource.length);
		
		return bufferedImage;
	}
	
	/**
	 * Returns a copy of this {@code Image} instance.
	 * <p>
	 * This method may or may not copy everything stored in an {@code Image} instance.
	 * 
	 * @return a copy of this {@code Image} instance
	 */
	public abstract Image copy();
	
	/**
	 * Returns a copy of this {@code Image} instance within {@code shape}.
	 * <p>
	 * If {@code shape} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method may or may not copy everything stored in an {@code Image} instance.
	 * 
	 * @param shape a {@link Shape2I} instance that represents the shape within this {@code Image} instance to copy
	 * @return a copy of this {@code Image} instance within {@code shape}
	 * @throws NullPointerException thrown if, and only if, {@code shape} is {@code null}
	 */
	public abstract Image copy(final Shape2I shape);
	
	/**
	 * Returns a {@link Rectangle2I} with the bounds of this {@code Image} instance.
	 * 
	 * @return a {@code Rectangle2I} with the bounds of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final Rectangle2I getBounds() {
		return new Rectangle2I(new Point2I(), new Point2I(this.resolutionX - 1, this.resolutionY - 1));
	}
	
	/**
	 * Returns a {@code WritableImage} representation of this {@code Image} instance.
	 * 
	 * @return a {@code WritableImage} representation of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final WritableImage toWritableImage() {
		final
		WritableImage writableImage = new WritableImage(this.resolutionX, this.resolutionY);
		writableImage.getPixelWriter().setPixels(0, 0, this.resolutionX, this.resolutionY, PixelFormat.getIntArgbInstance(), toIntArrayPackedForm(), 0, this.resolutionX);
		
		return writableImage;
	}
	
	/**
	 * Returns {@code true} if, and only if, the change history is enabled, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the change history is enabled, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public final boolean isChangeHistoryEnabled() {
		return this.isChangeHistoryEnabled.get();
	}
	
	/**
	 * Performs the current redo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the redo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the redo operation was performed, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public final boolean redo() {
		return this.isChangeHistoryEnabled.get() && this.changeHistory.redo();
	}
	
	/**
	 * Performs the current undo operation.
	 * <p>
	 * Returns {@code true} if, and only if, the undo operation was performed, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, the undo operation was performed, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public final boolean undo() {
		return this.isChangeHistoryEnabled.get() && this.changeHistory.undo();
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code Image} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toByteArray(ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @return a {@code byte[]} representation of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final byte[] toByteArray() {
		return toByteArray(ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code byte[]} representation of this {@code Image} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method may be overridden in order to optimize the conversion.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return a {@code byte[]} representation of this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public byte[] toByteArray(final ArrayComponentOrder arrayComponentOrder) {
		return ByteArrays.convert(toIntArray(Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null")));
	}
	
	/**
	 * Returns the resolution of this {@code Image} instance.
	 * <p>
	 * The resolution of {@code image} can be computed by:
	 * <pre>
	 * {@code
	 * int resolution = image.getResolutionX() * image.getResolutionY();
	 * }
	 * </pre>
	 * 
	 * @return the resolution of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final int getResolution() {
		return this.resolution;
	}
	
	/**
	 * Returns the resolution of the X-axis of this {@code Image} instance.
	 * <p>
	 * The resolution of the X-axis is also known as the width.
	 * 
	 * @return the resolution of the X-axis of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final int getResolutionX() {
		return this.resolutionX;
	}
	
	/**
	 * Returns the resolution of the Y-axis of this {@code Image} instance.
	 * <p>
	 * The resolution of the Y-axis is also known as the height.
	 * 
	 * @return the resolution of the Y-axis of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final int getResolutionY() {
		return this.resolutionY;
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toIntArray(ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	public final int[] toIntArray() {
		return toIntArray(ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance.
	 * <p>
	 * If {@code arrayComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method may be overridden in order to optimize the conversion.
	 * 
	 * @param arrayComponentOrder an {@link ArrayComponentOrder}
	 * @return an {@code int[]} representation of this {@code Image} instance
	 * @throws NullPointerException thrown if, and only if, {@code arrayComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public int[] toIntArray(final ArrayComponentOrder arrayComponentOrder) {
		return PackedIntComponentOrder.ARGB.unpack(Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null"), toIntArrayPackedForm());
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance in a packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * image.toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int[]} representation of this {@code Image} instance in a packed form
	 */
//	TODO: Add Unit Tests!
	public final int[] toIntArrayPackedForm() {
		return toIntArrayPackedForm(PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns an {@code int[]} representation of this {@code Image} instance in a packed form.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder}
	 * @return an {@code int[]} representation of this {@code Image} instance in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public abstract int[] toIntArrayPackedForm(final PackedIntComponentOrder packedIntComponentOrder);
	
	/**
	 * Copies the individual component values of the colors in this {@code Image} instance to the {@code byte[]} {@code array}.
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
	 * @param array the {@code byte[]} to copy the individual component values of the colors in this {@code Image} instance to
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != image.getResolution() * ArrayComponentOrder.BGRA.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public final void copyTo(final byte[] array) {
		copyTo(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Copies the individual component values of the colors in this {@code Image} instance to the {@code byte[]} {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length != image.getResolution() * arrayComponentOrder.getComponentCount()}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the {@code byte[]} to copy the individual component values of the colors in this {@code Image} instance to
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} to copy the components to {@code array} in the correct order
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length != image.getResolution() * arrayComponentOrder.getComponentCount()}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public final void copyTo(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length, this.resolution * arrayComponentOrder.getComponentCount(), "array");
		
		final byte[] sourceArray = toByteArray(arrayComponentOrder);
		final byte[] targetArray = array;
		
		System.arraycopy(sourceArray, 0, targetArray, 0, targetArray.length);
	}
	
	/**
	 * Flips this {@code Image} instance along the X-axis.
	 */
//	TODO: Add Unit Tests!
	public final void flipX() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int xL = 0, xR = resolutionX - 1; xL < xR; xL++, xR--) {
			for(int y = 0; y < resolutionY; y++) {
				swap(y * resolutionX + xL, y * resolutionX + xR);
			}
		}
	}
	
	/**
	 * Flips this {@code Image} instance along the Y-axis.
	 */
//	TODO: Add Unit Tests!
	public final void flipY() {
		final int resolutionX = getResolutionX();
		final int resolutionY = getResolutionY();
		
		for(int yT = 0, yB = resolutionY - 1; yT < yB; yT++, yB--) {
			for(int x = 0; x < resolutionX; x++) {
				swap(yT * resolutionX + x, yB * resolutionX + x);
			}
		}
	}
	
	/**
	 * Saves this {@code Image} to the file represented by {@code file} using the informal format name {@code "png"}.
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
	 * @throws NullPointerException thrown if, and only if, {@code file} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public final void save(final File file) {
		save(file, "png");
	}
	
	/**
	 * Saves this {@code Image} to the file represented by {@code file} using the informal format name {@code formatName}.
	 * <p>
	 * If either {@code file} or {@code formatName} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param file a {@code File} that represents the file to save to
	 * @param formatName the informal format name, such as {@code "png"}
	 * @throws NullPointerException thrown if, and only if, either {@code file} or {@code formatName} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public final void save(final File file, final String formatName) {
		Objects.requireNonNull(file, "file == null");
		Objects.requireNonNull(formatName, "formatName == null");
		
		try {
			final File parentFile = file.getParentFile();
			
			if(parentFile != null && !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			
			ImageIO.write(toBufferedImage(), formatName, file);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saves this {@code Image} to the file represented by the pathname {@code pathname} using the informal format name {@code "png"}.
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
	 * @throws NullPointerException thrown if, and only if, {@code pathname} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public final void save(final String pathname) {
		save(pathname, "png");
	}
	
	/**
	 * Saves this {@code Image} to the file represented by the pathname {@code pathname} using the informal format name {@code formatName}.
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
	 * @throws NullPointerException thrown if, and only if, either {@code pathname} or {@code formatName} are {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public final void save(final String pathname, final String formatName) {
		save(new File(pathname), formatName);
	}
	
	/**
	 * Sets the change history enabled state to {@code isChangeHistoryEnabled}.
	 * 
	 * @param isChangeHistoryEnabled the change history enabled state
	 */
//	TODO: Add Unit Tests!
	public final void setChangeHistoryEnabled(final boolean isChangeHistoryEnabled) {
		this.isChangeHistoryEnabled.set(isChangeHistoryEnabled);
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
	public abstract void swap(final int indexA, final int indexB);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the {@link ChangeHistory} instance that is associated with this {@code Image} instance.
	 * 
	 * @return the {@code ChangeHistory} instance that is associated with this {@code Image} instance
	 */
//	TODO: Add Unit Tests!
	protected final ChangeHistory getChangeHistory() {
		return this.changeHistory;
	}
}