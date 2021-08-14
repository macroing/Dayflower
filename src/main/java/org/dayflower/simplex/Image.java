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
import static org.dayflower.simplex.Color.color4I;
import static org.dayflower.utility.Doubles.saturate;
import static org.dayflower.utility.Ints.saturate;
import static org.dayflower.utility.Ints.toInt;

import java.lang.reflect.Field;//TODO: Add Javadocs!

//TODO: Add Javadocs!
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
			image4D[2 + i * 4 + 0] = color4D[color4DOffset + 0];
			image4D[2 + i * 4 + 1] = color4D[color4DOffset + 1];
			image4D[2 + i * 4 + 2] = color4D[color4DOffset + 2];
			image4D[2 + i * 4 + 3] = color4D[color4DOffset + 3];
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
			image4DResult[image4DResultOffset + 2 + i * 4 + 0] = saturate(image4I[image4IOffset + 2 + i * 4 + 0]) / 255.0D;
			image4DResult[image4DResultOffset + 2 + i * 4 + 1] = saturate(image4I[image4IOffset + 2 + i * 4 + 1]) / 255.0D;
			image4DResult[image4DResultOffset + 2 + i * 4 + 2] = saturate(image4I[image4IOffset + 2 + i * 4 + 2]) / 255.0D;
			image4DResult[image4DResultOffset + 2 + i * 4 + 3] = saturate(image4I[image4IOffset + 2 + i * 4 + 3]) / 255.0D;
		}
		
		return image4DResult;
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
			image4I[2 + i * 4 + 0] = color4I[color4IOffset + 0];
			image4I[2 + i * 4 + 1] = color4I[color4IOffset + 1];
			image4I[2 + i * 4 + 2] = color4I[color4IOffset + 2];
			image4I[2 + i * 4 + 3] = color4I[color4IOffset + 3];
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
			image4IResult[image4IResultOffset + 2 + i * 4 + 0] = toInt(saturate(image4D[image4DOffset + 2 + i * 4 + 0]) * 255.0D + 0.5D);
			image4IResult[image4IResultOffset + 2 + i * 4 + 1] = toInt(saturate(image4D[image4DOffset + 2 + i * 4 + 1]) * 255.0D + 0.5D);
			image4IResult[image4IResultOffset + 2 + i * 4 + 2] = toInt(saturate(image4D[image4DOffset + 2 + i * 4 + 2]) * 255.0D + 0.5D);
			image4IResult[image4IResultOffset + 2 + i * 4 + 3] = toInt(saturate(image4D[image4DOffset + 2 + i * 4 + 3]) * 255.0D + 0.5D);
		}
		
		return image4IResult;
	}
}