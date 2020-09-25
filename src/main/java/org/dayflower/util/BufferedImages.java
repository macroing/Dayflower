/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on {@code BufferedImage} instances.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BufferedImages {
	private BufferedImages() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code BufferedImage} that is compatible with the type {@code BufferedImage.TYPE_INT_ARGB}.
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
	 * @return a {@code BufferedImage} that is compatible with the type {@code BufferedImage.TYPE_INT_ARGB}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
	public static BufferedImage getCompatibleBufferedImage(final BufferedImage bufferedImage) {
		return getCompatibleBufferedImage(bufferedImage, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Returns a {@code BufferedImage} that is compatible with the type {@code type}.
	 * <p>
	 * If {@code bufferedImage} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param bufferedImage a {@code BufferedImage} instance
	 * @param type the type to use
	 * @return a {@code BufferedImage} that is compatible with the type {@code type}
	 * @throws NullPointerException thrown if, and only if, {@code bufferedImage} is {@code null}
	 */
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
}