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

import static org.dayflower.utility.Ints.positiveModulo;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

/**
 * A {@code PixelOperation} provides a set of operations to perform on a pixel when it is outside the boundaries of an image.
 * <p>
 * A pixel can be defined by, or represented as, a pair of X- and Y-coordinates or an index.
 * <p>
 * The operations that can be performed on a pixel are the no-change operation and the wrap-around operation. More operations may be added in the future.
 * <p>
 * <strong>No-Change Operation</strong>
 * <p>
 * The no-change operation, {@link PixelOperation#NO_CHANGE}, does not change the pixel if it is outside the boundaries of the image.
 * <p>
 * The following list shows what will happen with different methods if the pixel is outside the boundaries of the image:
 * <ul>
 * <li>{@code Return color}: The method returns black</li>
 * <li>{@code Update color}: The method updates nothing</li>
 * </ul>
 * <p>
 * <strong>Wrap-Around Operation</strong>
 * <p>
 * The wrap-around operation, {@link PixelOperation#WRAP_AROUND}, will change the pixel if it is outside the boundaries of the image by wrapping its X- and Y-coordinates or index around.
 * <p>
 * The wrap-around operation is performed on the X- and Y-coordinates individually or on the index. The X-coordinate is wrapped around the resolution of the X-axis, the Y-coordinate is wrapped around the resolution of the Y-axis and the index is
 * wrapped around the resolution.
 * <p>
 * The following list shows what will happen with different methods if the pixel is outside the boundaries of the image:
 * <ul>
 * <li>{@code Return color}: The method returns the color of the changed pixel</li>
 * <li>{@code Update color}: The method updates the color of the changed pixel</li>
 * </ul>
 * <p>
 * The following code snippet shows how the wrap-around operation works, given the two variables {@code x} and {@code y}:
 * <pre>
 * {@code
 * int z = x < 0 ? (x % y + y) % y : x % y;
 * }
 * </pre>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public enum PixelOperation {
	/**
	 * The no-change operation.
	 */
//	TODO: Add Unit Tests!
	NO_CHANGE,
	
	/**
	 * The wrap-around operation.
	 */
//	TODO: Add Unit Tests!
	WRAP_AROUND;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private PixelOperation() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Applies this operation to {@code index}.
	 * <p>
	 * Returns the index that results from applying this operation to {@code index}.
	 * 
	 * @param index the index of the pixel
	 * @param resolution the resolution
	 * @return the index that results from applying this operation to {@code index}
	 */
//	TODO: Add Unit Tests!
	public int getIndex(final int index, final int resolution) {
		switch(this) {
			case NO_CHANGE:
				return index;
			case WRAP_AROUND:
				return positiveModulo(index, resolution);
			default:
				return index;
		}
	}
	
	/**
	 * Applies this operation to {@code x}.
	 * <p>
	 * Returns the X-coordinate that results from applying this operation to {@code x}.
	 * 
	 * @param x the X-coordinate of the pixel
	 * @param resolutionX the resolution of the X-axis
	 * @return the X-coordinate that results from applying this operation to {@code x}
	 */
//	TODO: Add Unit Tests!
	public int getX(final int x, final int resolutionX) {
		switch(this) {
			case NO_CHANGE:
				return x;
			case WRAP_AROUND:
				return positiveModulo(x, resolutionX);
			default:
				return x;
		}
	}
	
	/**
	 * Applies this operation to {@code y}.
	 * <p>
	 * Returns the Y-coordinate that results from applying this operation to {@code y}.
	 * 
	 * @param y the Y-coordinate of the pixel
	 * @param resolutionY the resolution of the Y-axis
	 * @return the Y-coordinate that results from applying this operation to {@code y}
	 */
//	TODO: Add Unit Tests!
	public int getY(final int y, final int resolutionY) {
		switch(this) {
			case NO_CHANGE:
				return y;
			case WRAP_AROUND:
				return positiveModulo(y, resolutionY);
			default:
				return y;
		}
	}
}