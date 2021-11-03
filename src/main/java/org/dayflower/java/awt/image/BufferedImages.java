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
package org.dayflower.java.awt.image;

import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code BufferedImage} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BufferedImages {
	private static final Robot ROBOT = doCreateRobot();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private BufferedImages() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} instance that contains pixels read from the screen without the mouse cursor.
	 * <p>
	 * If the platform configuration does not allow low-level input control, the {@code BufferedImage} instance returned will be empty. This is the case if, for example, {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
	 * <p>
	 * If {@code rectangle} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code rectangle.width} or {@code rectangle.height} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * 
	 * @param rectangle a {@code Rectangle} instance that contains screen coordinates
	 * @return a {@code BufferedImage} instance that contains pixels read from the screen without the mouse cursor
	 * @throws IllegalArgumentException thrown if, and only if, {@code rectangle.width} or {@code rectangle.height} are less than or equal to {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code rectangle} is {@code null}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
//	TODO: Add Unit Tests!
	public static BufferedImage createScreenCapture(final Rectangle rectangle) {
		Objects.requireNonNull(rectangle, "rectangle == null");
		
		if(ROBOT != null) {
			return ROBOT.createScreenCapture(rectangle);
		}
		
		return new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Returns a {@code BufferedImage} instance that contains pixels read from the screen without the mouse cursor.
	 * <p>
	 * If the platform configuration does not allow low-level input control, the {@code BufferedImage} instance returned will be empty. This is the case if, for example, {@code GraphicsEnvironment.isHeadless()} returns {@code true}.
	 * <p>
	 * If {@code width} or {@code height} are less than or equal to {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If the permission {@code readDisplayPixels} is not granted, a {@code SecurityException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * BufferedImages.createScreenCapture(new Rectangle(x, y, width, height));
	 * }
	 * </pre>
	 * 
	 * @param x the specified X-coordinate
	 * @param y the specified Y-coordinate
	 * @param width the width of the rectangle
	 * @param height the height of the rectangle
	 * @return a {@code BufferedImage} instance that contains pixels read from the screen without the mouse cursor
	 * @throws IllegalArgumentException thrown if, and only if, {@code width} or {@code height} are less than or equal to {@code 0}
	 * @throws SecurityException thrown if, and only if, the permission {@code readDisplayPixels} is not granted
	 */
//	TODO: Add Unit Tests!
	public static BufferedImage createScreenCapture(final int x, final int y, final int width, final int height) {
		return createScreenCapture(new Rectangle(x, y, width, height));
	}
	
	/**
	 * Returns a {@code BufferedImage} instance that contains an image similar to {@code bufferedImage} and a type of {@code BufferedImage.TYPE_INT_ARGB}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * BufferedImages.getCompatibleBufferedImage(bufferedImage, BufferedImage.TYPE_INT_ARGB);
	 * }
	 * </pre>
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @return a {@code BufferedImage} instance that contains an image similar to {@code bufferedImage} and a type of {@code BufferedImage.TYPE_INT_ARGB}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static BufferedImage getCompatibleBufferedImage(final BufferedImage bufferedImage) {
		return getCompatibleBufferedImage(bufferedImage, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Returns a {@code BufferedImage} instance that contains an image similar to {@code bufferedImage} and a type of {@code type}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code type} is invalid, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param type the type to use
	 * @return a {@code BufferedImage} instance that contains an image similar to {@code bufferedImage} and a type of {@code type}
	 * @throws IllegalArgumentException thrown if, and only if, {@code type} is invalid
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static BufferedImage getCompatibleBufferedImage(final BufferedImage bufferedImage, final int type) {
		if(bufferedImage.getType() == type) {
			return bufferedImage;
		}
		
		final BufferedImage compatibleBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), type);
		
		final
		Graphics2D graphics2D = compatibleBufferedImage.createGraphics();
		graphics2D.drawImage(bufferedImage, 0, 0, null);
		
		return compatibleBufferedImage;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static Robot doCreateRobot() {
		try {
			return new Robot();
		} catch(final AWTException e) {
			return null;
		}
	}
}