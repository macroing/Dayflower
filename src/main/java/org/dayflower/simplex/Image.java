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
package org.dayflower.simplex;

import static org.dayflower.simplex.Color.color4D;
import static org.dayflower.simplex.Color.color4DBlend;
import static org.dayflower.simplex.Color.color4DFromColor4I;
import static org.dayflower.simplex.Color.color4DSet;
import static org.dayflower.simplex.Color.color4I;
import static org.dayflower.simplex.Color.color4IPacked;
import static org.dayflower.simplex.Color.color4IPackedFromColor4I;
import static org.dayflower.simplex.Color.color4IFromColor4D;
import static org.dayflower.simplex.Color.color4IFromColor4IPacked;
import static org.dayflower.simplex.Color.color4ISet;
import static org.dayflower.utility.Doubles.ceil;
import static org.dayflower.utility.Doubles.floor;
import static org.dayflower.utility.Ints.positiveModulo;
import static org.dayflower.utility.Ints.toInt;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Javadocs!

import javax.imageio.ImageIO;

import org.dayflower.java.awt.image.BufferedImages;

/**
 * A class that consists exclusively of static methods that returns or performs various operations on images.
 * <p>
 * This class currently supports the following:
 * <ul>
 * <li>{@code Image4D} - an image represented by a {@code double[]} that contains four {@code double} values per pixel.</li>
 * <li>{@code Image4I} - an image represented by an {@code int[]} that contains four {@code int} values per pixel.</li>
 * <li>{@code Image4IPacked} - an image represented by an {@code int[]} that contains one {@code int} value per pixel.</li>
 * </ul>
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Image {
	private Image() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Image4D /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static double[] image4D() {
		return image4D(800, 800);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4D(final int resolutionX, final int resolutionY) {
		return image4D(resolutionX, resolutionY, color4D());
	}
	
//	TODO: Add Javadocs!
	public static double[] image4D(final int resolutionX, final int resolutionY, final double[] color4D) {
		return image4D(resolutionX, resolutionY, color4D, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4D(final int resolutionX, final int resolutionY, final double[] color4D, final int color4DOffset) {
		final int resolution = resolutionX * resolutionY;
		
		final double[] image4D = new double[2 + resolution * 4];
		
		image4D[0] = resolutionX;
		image4D[1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			color4DSet(image4D, color4D, 2 + i * 4, color4DOffset);
		}
		
		return image4D;
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DFromImage4I(final int[] image4I) {
		return image4DFromImage4I(image4I, image4D(image4IGetResolutionX(image4I), image4IGetResolutionY(image4I)));
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DFromImage4I(final int[] image4I, final double[] image4DResult) {
		return image4DFromImage4I(image4I, image4DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DFromImage4I(final int[] image4I, final double[] image4DResult, final int image4IOffset, final int image4DResultOffset) {
		final int resolutionX = image4IGetResolutionX(image4I, image4IOffset);
		final int resolutionY = image4IGetResolutionY(image4I, image4IOffset);
		final int resolution = resolutionX * resolutionY;
		
		image4DResult[image4DResultOffset + 0] = resolutionX;
		image4DResult[image4DResultOffset + 1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			color4DSet(image4DResult, color4DFromColor4I(image4I, color4D(), image4IOffset + 2 + i * 4, 0), image4DResultOffset + 2 + i * 4, 0);
		}
		
		return image4DResult;
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final double x, final double y) {
		return image4DGetColor4D(image4D, x, y, color4D());
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final double x, final double y, final double[] color4DResult) {
		return image4DGetColor4D(image4D, x, y, color4DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final double x, final double y, final double[] color4DResult, final int image4DOffset, final int color4DResultOffset) {
		final int minimumX = toInt(floor(x));
		final int maximumX = toInt(ceil(x));
		
		final int minimumY = toInt(floor(y));
		final int maximumY = toInt(ceil(y));
		
		if(minimumX == maximumX && minimumY == maximumY) {
			return image4DGetColor4D(image4D, minimumX, minimumY, color4DResult, image4DOffset, color4DResultOffset);
		}
		
		final double[] color4D11 = image4DGetColor4D(image4D, minimumX, minimumY, color4D(), image4DOffset, 0);
		final double[] color4D12 = image4DGetColor4D(image4D, maximumX, minimumY, color4D(), image4DOffset, 0);
		final double[] color4D21 = image4DGetColor4D(image4D, minimumX, maximumY, color4D(), image4DOffset, 0);
		final double[] color4D22 = image4DGetColor4D(image4D, maximumX, maximumY, color4D(), image4DOffset, 0);
		
		final double tX = x - minimumX;
		final double tY = y - minimumY;
		
		final double[] color4D31 = color4DBlend(color4D11, color4D12, tX);
		final double[] color4D32 = color4DBlend(color4D21, color4D22, tX);
		final double[] color4D41 = color4DBlend(color4D31, color4D32, tY);
		
		return color4DSet(color4DResult, color4D41, color4DResultOffset, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final int x, final int y) {
		return image4DGetColor4D(image4D, x, y, color4D());
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final int x, final int y, final double[] color4DResult) {
		return image4DGetColor4D(image4D, x, y, color4DResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DGetColor4D(final double[] image4D, final int x, final int y, final double[] color4DResult, final int image4DOffset, final int color4DResultOffset) {
		final int resolutionX = image4DGetResolutionX(image4D, image4DOffset);
		final int resolutionY = image4DGetResolutionY(image4D, image4DOffset);
		
		final int currentX = positiveModulo(x, resolutionX);
		final int currentY = positiveModulo(y, resolutionY);
		
		if(currentX >= 0 && currentX < resolutionX && currentY >= 0 && currentY < resolutionY) {
			final int index = (currentY * resolutionX + currentX) * 4;
			
			return color4DSet(color4DResult, image4D, color4DResultOffset, image4DOffset + 2 + index);
		}
		
		return color4D();
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DLoad(final File file) {
		return image4DFromImage4I(image4ILoad(file));
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DLoad(final String pathname) {
		return image4DLoad(new File(pathname));
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DSetColor4D(final double[] image4D, final double[] color4D, final int x, final int y) {
		return image4DSetColor4D(image4D, color4D, x, y, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static double[] image4DSetColor4D(final double[] image4D, final double[] color4D, final int x, final int y, final int image4DOffset, final int color4DOffset) {
		final int resolutionX = image4DGetResolutionX(image4D, image4DOffset);
		final int resolutionY = image4DGetResolutionY(image4D, image4DOffset);
		
		if(x >= 0 && x < resolutionX && y >= 0 && y < resolutionY) {
			final int index = (y * resolutionX + x) * 4;
			
			color4DSet(image4D, color4D, image4DOffset + 2 + index, color4DOffset);
		}
		
		return image4D;
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolution(final double[] image4D) {
		return image4DGetResolution(image4D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolution(final double[] image4D, final int image4DOffset) {
		return image4DGetResolutionX(image4D, image4DOffset) * image4DGetResolutionY(image4D, image4DOffset);
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolutionX(final double[] image4D) {
		return image4DGetResolutionX(image4D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolutionX(final double[] image4D, final int image4DOffset) {
		return toInt(image4D[image4DOffset + 0]);
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolutionY(final double[] image4D) {
		return image4DGetResolutionY(image4D, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4DGetResolutionY(final double[] image4D, final int image4DOffset) {
		return toInt(image4D[image4DOffset + 1]);
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final File file) {
		image4DSave(image4D, file, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final File file, final String formatName) {
		image4DSave(image4D, file, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final File file, final String formatName, final int image4DOffset) {
		image4ISave(image4IFromImage4D(image4D, image4I(image4DGetResolutionX(image4D, image4DOffset), image4DGetResolutionY(image4D, image4DOffset)), image4DOffset, 0), file, formatName);
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final String pathname) {
		image4DSave(image4D, pathname, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final String pathname, final String formatName) {
		image4DSave(image4D, pathname, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4DSave(final double[] image4D, final String pathname, final String formatName, final int image4DOffset) {
		image4DSave(image4D, new File(pathname), formatName, image4DOffset);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Image4I /////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static int image4IGetResolution(final int[] image4I) {
		return image4IGetResolution(image4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IGetResolution(final int[] image4I, final int image4IOffset) {
		return image4IGetResolutionX(image4I, image4IOffset) * image4IGetResolutionY(image4I, image4IOffset);
	}
	
//	TODO: Add Javadocs!
	public static int image4IGetResolutionX(final int[] image4I) {
		return image4IGetResolutionX(image4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IGetResolutionX(final int[] image4I, final int image4IOffset) {
		return image4I[image4IOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static int image4IGetResolutionY(final int[] image4I) {
		return image4IGetResolutionY(image4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IGetResolutionY(final int[] image4I, final int image4IOffset) {
		return image4I[image4IOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static int[] image4I() {
		return image4I(800, 800);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4I(final int resolutionX, final int resolutionY) {
		return image4I(resolutionX, resolutionY, color4I());
	}
	
//	TODO: Add Javadocs!
	public static int[] image4I(final int resolutionX, final int resolutionY, final int[] color4I) {
		return image4I(resolutionX, resolutionY, color4I, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4I(final int resolutionX, final int resolutionY, final int[] color4I, final int color4IOffset) {
		final int resolution = resolutionX * resolutionY;
		
		final int[] image4I = new int[2 + resolution * 4];
		
		image4I[0] = resolutionX;
		image4I[1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			color4ISet(image4I, color4I, 2 + i * 4, color4IOffset);
		}
		
		return image4I;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4D(final double[] image4D) {
		return image4IFromImage4D(image4D, image4I(image4DGetResolutionX(image4D), image4DGetResolutionY(image4D)));
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4D(final double[] image4D, final int[] image4IResult) {
		return image4IFromImage4D(image4D, image4IResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4D(final double[] image4D, final int[] image4IResult, final int image4DOffset, final int image4IResultOffset) {
		final int resolutionX = image4DGetResolutionX(image4D, image4DOffset);
		final int resolutionY = image4DGetResolutionY(image4D, image4DOffset);
		final int resolution = resolutionX * resolutionY;
		
		image4IResult[image4IResultOffset + 0] = resolutionX;
		image4IResult[image4IResultOffset + 1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			color4ISet(image4IResult, color4IFromColor4D(image4D, color4I(), image4DOffset + 2 + i * 4, 0), image4IResultOffset + 2 + i * 4, 0);
		}
		
		return image4IResult;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4IPacked(final int[] image4IPacked) {
		return image4IFromImage4IPacked(image4IPacked, image4I(image4IPackedGetResolutionX(image4IPacked), image4IPackedGetResolutionY(image4IPacked)));
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4IPacked(final int[] image4IPacked, final int[] image4IResult) {
		return image4IFromImage4IPacked(image4IPacked, image4IResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IFromImage4IPacked(final int[] image4IPacked, final int[] image4IResult, final int image4IPackedOffset, final int image4IResultOffset) {
		final int resolutionX = image4IPackedGetResolutionX(image4IPacked, image4IPackedOffset);
		final int resolutionY = image4IPackedGetResolutionY(image4IPacked, image4IPackedOffset);
		final int resolution = resolutionX * resolutionY;
		
		image4IResult[image4IResultOffset + 0] = resolutionX;
		image4IResult[image4IResultOffset + 1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			color4ISet(image4IResult, color4IFromColor4IPacked(image4IPacked[image4IPackedOffset + 2 + i]), image4IResultOffset + 2 + i * 4, 0);
		}
		
		return image4IResult;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4ILoad(final File file) {
		return image4IFromImage4IPacked(image4IPackedLoad(file));
	}
	
//	TODO: Add Javadocs!
	public static int[] image4ILoad(final String pathname) {
		return image4ILoad(new File(pathname));
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final File file) {
		image4ISave(image4I, file, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final File file, final String formatName) {
		image4ISave(image4I, file, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final File file, final String formatName, final int image4IOffset) {
		image4IPackedSave(image4IPackedFromImage4I(image4I, image4IPacked(image4IGetResolutionX(image4I, image4IOffset), image4IGetResolutionY(image4I, image4IOffset)), image4IOffset, 0), file, formatName);
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final String pathname) {
		image4ISave(image4I, pathname, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final String pathname, final String formatName) {
		image4ISave(image4I, pathname, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4ISave(final int[] image4I, final String pathname, final String formatName, final int image4IOffset) {
		image4ISave(image4I, new File(pathname), formatName, image4IOffset);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	// Image4IPacked ///////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolution(final int[] image4IPacked) {
		return image4IPackedGetResolution(image4IPacked, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolution(final int[] image4IPacked, final int image4IPackedOffset) {
		return image4IPackedGetResolutionX(image4IPacked, image4IPackedOffset) * image4IPackedGetResolutionY(image4IPacked, image4IPackedOffset);
	}
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolutionX(final int[] image4IPacked) {
		return image4IPackedGetResolutionX(image4IPacked, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolutionX(final int[] image4IPacked, final int image4IPackedOffset) {
		return image4IPacked[image4IPackedOffset + 0];
	}
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolutionY(final int[] image4IPacked) {
		return image4IPackedGetResolutionY(image4IPacked, 0);
	}
	
//	TODO: Add Javadocs!
	public static int image4IPackedGetResolutionY(final int[] image4IPacked, final int image4IPackedOffset) {
		return image4IPacked[image4IPackedOffset + 1];
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPacked() {
		return image4IPacked(800, 800);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPacked(final int resolutionX, final int resolutionY) {
		return image4IPacked(resolutionX, resolutionY, color4IPacked());
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPacked(final int resolutionX, final int resolutionY, final int color4IPacked) {
		final int resolution = resolutionX * resolutionY;
		
		final int[] image4IPacked = new int[2 + resolution];
		
		image4IPacked[0] = resolutionX;
		image4IPacked[1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			image4IPacked[2 + i] = color4IPacked;
		}
		
		return image4IPacked;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedCopyData(final int[] image4IPacked) {
		return image4IPackedCopyData(image4IPacked, new int[image4IPackedGetResolution(image4IPacked)]);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedCopyData(final int[] image4IPacked, final int[] dataResult) {
		return image4IPackedCopyData(image4IPacked, dataResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedCopyData(final int[] image4IPacked, final int[] dataResult, final int image4IPackedOffset, final int dataResultOffset) {
		final int resolution = image4IPackedGetResolution(image4IPacked, image4IPackedOffset);
		
		System.arraycopy(image4IPacked, image4IPackedOffset + 2, dataResult, dataResultOffset, resolution);
		
		return dataResult;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedFromImage4I(final int[] image4I) {
		return image4IPackedFromImage4I(image4I, image4IPacked(image4IGetResolutionX(image4I), image4IGetResolutionY(image4I)));
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedFromImage4I(final int[] image4I, final int[] image4IPackedResult) {
		return image4IPackedFromImage4I(image4I, image4IPackedResult, 0, 0);
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedFromImage4I(final int[] image4I, final int[] image4IPackedResult, final int image4IOffset, final int image4IPackedResultOffset) {
		final int resolutionX = image4IGetResolutionX(image4I, image4IOffset);
		final int resolutionY = image4IGetResolutionY(image4I, image4IOffset);
		final int resolution = resolutionX * resolutionY;
		
		image4IPackedResult[image4IPackedResultOffset + 0] = resolutionX;
		image4IPackedResult[image4IPackedResultOffset + 1] = resolutionY;
		
		for(int i = 0; i < resolution; i++) {
			image4IPackedResult[2 + i] = color4IPackedFromColor4I(image4I, image4IOffset + 2 + i * 4);
		}
		
		return image4IPackedResult;
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedLoad(final File file) {
		try {
			final BufferedImage bufferedImage = BufferedImages.getCompatibleBufferedImage(ImageIO.read(file), BufferedImage.TYPE_INT_ARGB);
			
			final int[] dataResult = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
			
			final int resolutionX = bufferedImage.getWidth();
			final int resolutionY = bufferedImage.getHeight();
			
			final int[] image4IPacked = image4IPacked(resolutionX, resolutionY);
			
			System.arraycopy(dataResult, 0, image4IPacked, 2, dataResult.length);
			
			return image4IPacked;
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
//	TODO: Add Javadocs!
	public static int[] image4IPackedLoad(final String pathname) {
		return image4IPackedLoad(new File(pathname));
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final File file) {
		image4IPackedSave(image4IPacked, file, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final File file, final String formatName) {
		image4IPackedSave(image4IPacked, file, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final File file, final String formatName, final int image4IPackedOffset) {
		try {
			final File parentFile = file.getParentFile();
			
			if(parentFile != null && !parentFile.isDirectory()) {
				parentFile.mkdirs();
			}
			
			final int resolutionX = image4IPackedGetResolutionX(image4IPacked, image4IPackedOffset);
			final int resolutionY = image4IPackedGetResolutionY(image4IPacked, image4IPackedOffset);
			
			final BufferedImage bufferedImage = new BufferedImage(resolutionX, resolutionY, BufferedImage.TYPE_INT_ARGB);
			
			final int[] dataResult = DataBufferInt.class.cast(bufferedImage.getRaster().getDataBuffer()).getData();
			
			image4IPackedCopyData(image4IPacked, dataResult, image4IPackedOffset, 0);
			
			ImageIO.write(bufferedImage, formatName, file);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final String pathname) {
		image4IPackedSave(image4IPacked, pathname, "png");
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final String pathname, final String formatName) {
		image4IPackedSave(image4IPacked, pathname, formatName, 0);
	}
	
//	TODO: Add Javadocs!
	public static void image4IPackedSave(final int[] image4IPacked, final String pathname, final String formatName, final int image4IPackedOffset) {
		image4IPackedSave(image4IPacked, new File(pathname), formatName, image4IPackedOffset);
	}
}