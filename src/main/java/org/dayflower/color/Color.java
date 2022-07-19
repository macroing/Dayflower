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
package org.dayflower.color;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.concurrent.ThreadLocalRandom;

import org.dayflower.utility.Doubles;
import org.dayflower.utility.Floats;

/**
 * This {@code Color} class consists exclusively of static methods that operates on colors that are defined by primitive types such as {@code double} and {@code int}.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color {
	/**
	 * An {@code int} that represents the color black in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(0, 0, 0)}.
	 */
//	TODO: Add Unit Tests!
	public static final int BLACK = packRGB(0, 0, 0);
	
	/**
	 * An {@code int} that represents the color blue in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(0, 0, 255)}.
	 */
//	TODO: Add Unit Tests!
	public static final int BLUE = packRGB(0, 0, 255);
	
	/**
	 * An {@code int} that represents the color cyan in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(0, 255, 255)}.
	 */
//	TODO: Add Unit Tests!
	public static final int CYAN = packRGB(0, 255, 255);
	
	/**
	 * An {@code int} that represents the color gray in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(128, 128, 128)}.
	 */
//	TODO: Add Unit Tests!
	public static final int GRAY = packRGB(128, 128, 128);
	
	/**
	 * An {@code int} that represents the color green in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(0, 255, 0)}.
	 */
//	TODO: Add Unit Tests!
	public static final int GREEN = packRGB(0, 255, 0);
	
	/**
	 * An {@code int} that represents the color magenta in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(255, 0, 255)}.
	 */
//	TODO: Add Unit Tests!
	public static final int MAGENTA = packRGB(255, 0, 255);
	
	/**
	 * An {@code int} that represents the color red in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(255, 0, 0)}.
	 */
//	TODO: Add Unit Tests!
	public static final int RED = packRGB(255, 0, 0);
	
	/**
	 * An {@code int} that represents the color transparent in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGBA(0, 0, 0, 0)}.
	 */
//	TODO: Add Unit Tests!
	public static final int TRANSPARENT = packRGBA(0, 0, 0, 0);
	
	/**
	 * An {@code int} that represents the color white in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(255, 255, 255)}.
	 */
//	TODO: Add Unit Tests!
	public static final int WHITE = packRGB(255, 255, 255);
	
	/**
	 * An {@code int} that represents the color yellow in packed form.
	 * <p>
	 * The color is equivalent to {@code Color.packRGB(255, 255, 0)}.
	 */
//	TODO: Add Unit Tests!
	public static final int YELLOW = packRGB(255, 255, 0);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final int COLOR_A_R_G_B_SHIFT_A;
	private static final int COLOR_A_R_G_B_SHIFT_B;
	private static final int COLOR_A_R_G_B_SHIFT_G;
	private static final int COLOR_A_R_G_B_SHIFT_R;
	private static final double COLOR_SPACE_BREAK_POINT;
	private static final double COLOR_SPACE_GAMMA;
	private static final double COLOR_SPACE_SEGMENT_OFFSET;
	private static final double COLOR_SPACE_SLOPE;
	private static final double COLOR_SPACE_SLOPE_MATCH;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		COLOR_A_R_G_B_SHIFT_A = 24;
		COLOR_A_R_G_B_SHIFT_B =  0;
		COLOR_A_R_G_B_SHIFT_G =  8;
		COLOR_A_R_G_B_SHIFT_R = 16;
		
		COLOR_SPACE_BREAK_POINT = 0.00304D;
		COLOR_SPACE_GAMMA = 2.4D;
		COLOR_SPACE_SLOPE = 1.0D / (COLOR_SPACE_GAMMA / Math.pow(COLOR_SPACE_BREAK_POINT, 1.0D / COLOR_SPACE_GAMMA - 1.0D) - COLOR_SPACE_GAMMA * COLOR_SPACE_BREAK_POINT + COLOR_SPACE_BREAK_POINT);
		COLOR_SPACE_SLOPE_MATCH = COLOR_SPACE_GAMMA * COLOR_SPACE_SLOPE / Math.pow(COLOR_SPACE_BREAK_POINT, 1.0D / COLOR_SPACE_GAMMA - 1.0D);
		COLOR_SPACE_SEGMENT_OFFSET = COLOR_SPACE_SLOPE_MATCH * Math.pow(COLOR_SPACE_BREAK_POINT, 1.0D / COLOR_SPACE_GAMMA) - COLOR_SPACE_SLOPE * COLOR_SPACE_BREAK_POINT;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private Color() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is black, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isBlack(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is black, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isBlack(final int colorARGB) {
		return isBlack(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is black, {@code false} otherwise.
	 * <p>
	 * This method checks that {@code r}, {@code g} and {@code b} are {@code 0}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is black, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isBlack(final int r, final int g, final int b) {
		return r == 0 && g == 0 && b == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isBlue(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is blue, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isBlue(final int colorARGB) {
		return isBlue(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isBlue(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isBlue(final int r, final int g, final int b) {
		return isBlue(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code b - deltaR >= r} and {@code b - deltaG >= g} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isBlue(final int r, final int g, final int b, final int deltaR, final int deltaG) {
		return b - deltaR >= r && b - deltaG >= g;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is cyan, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isCyan(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is cyan, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isCyan(final int colorARGB) {
		return isCyan(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is cyan, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code g == b} and {@code r < g} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is cyan, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isCyan(final int r, final int g, final int b) {
		return g == b && r < g;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is a grayscale color, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isGrayscale(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is a grayscale color, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isGrayscale(final int colorARGB) {
		return isGrayscale(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is a grayscale color, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code r == g} and {@code r == b} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is a grayscale color, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isGrayscale(final int r, final int g, final int b) {
		return r == g && r == b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isGreen(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is green, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isGreen(final int colorARGB) {
		return isGreen(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isGreen(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isGreen(final int r, final int g, final int b) {
		return isGreen(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code g - deltaR >= r} and {@code g - deltaB >= b} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isGreen(final int r, final int g, final int b, final int deltaR, final int deltaB) {
		return g - deltaR >= r && g - deltaB >= b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is magenta, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isMagenta(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is magenta, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isMagenta(final int colorARGB) {
		return isMagenta(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is magenta, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code r == b} and {@code g < b} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is magenta, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isMagenta(final int r, final int g, final int b) {
		return r == b && g < b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isRed(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is red, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isRed(final int colorARGB) {
		return isRed(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isRed(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isRed(final int r, final int g, final int b) {
		return isRed(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code r - deltaG >= g} and {@code r - deltaB >= b} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isRed(final int r, final int g, final int b, final int deltaG, final int deltaB) {
		return r - deltaG >= g && r - deltaB >= b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is white, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isWhite(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is white, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isWhite(final int colorARGB) {
		return isWhite(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is white, {@code false} otherwise.
	 * <p>
	 * This method checks that {@code r}, {@code g} and {@code b} are {@code 255}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is white, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isWhite(final int r, final int g, final int b) {
		return r == 255 && g == 255 && b == 255;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code colorARGB} is yellow, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.isYellow(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return {@code true} if, and only if, the color represented by {@code colorARGB} is yellow, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isYellow(final int colorARGB) {
		return isYellow(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB));
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is yellow, {@code false} otherwise.
	 * <p>
	 * This method checks that both {@code r == g} and {@code b < r} are {@code true}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is yellow, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public static boolean isYellow(final int r, final int g, final int b) {
		return r == g && b < r;
	}
	
	/**
	 * Returns the average component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the average component value of {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static double average(final double r, final double g, final double b) {
		return (r + g + b) / 3.0D;
	}
	
	/**
	 * Returns the relative luminance of the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return the relative luminance of the color represented by {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static double relativeLuminance(final double r, final double g, final double b) {
		return r * 0.212671D + g * 0.715160D + b * 0.072169D;
	}
	
	/**
	 * Returns a {@code double} with the value of the A-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code double} with the value of the A-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static double unpackAAsDouble(final int colorARGB) {
		return unpackA(colorARGB) / 255.0D;
	}
	
	/**
	 * Returns a {@code double} with the value of the B-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code double} with the value of the B-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static double unpackBAsDouble(final int colorARGB) {
		return unpackB(colorARGB) / 255.0D;
	}
	
	/**
	 * Returns a {@code double} with the value of the G-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code double} with the value of the G-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static double unpackGAsDouble(final int colorARGB) {
		return unpackG(colorARGB) / 255.0D;
	}
	
	/**
	 * Returns a {@code double} with the value of the R-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code double} with the value of the R-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static double unpackRAsDouble(final int colorARGB) {
		return unpackR(colorARGB) / 255.0D;
	}
	
	/**
	 * Returns the average component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the average component value of {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static float average(final float r, final float g, final float b) {
		return (r + g + b) / 3.0F;
	}
	
	/**
	 * Returns the relative luminance of the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the R-component of the color
	 * @param g the G-component of the color
	 * @param b the B-component of the color
	 * @return the relative luminance of the color represented by {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static float relativeLuminance(final float r, final float g, final float b) {
		return r * 0.212671F + g * 0.715160F + b * 0.072169F;
	}
	
	/**
	 * Returns a {@code float} with the value of the A-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code float} with the value of the A-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static float unpackAAsFloat(final int colorARGB) {
		return unpackA(colorARGB) / 255.0F;
	}
	
	/**
	 * Returns a {@code float} with the value of the B-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code float} with the value of the B-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static float unpackBAsFloat(final int colorARGB) {
		return unpackB(colorARGB) / 255.0F;
	}
	
	/**
	 * Returns a {@code float} with the value of the G-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code float} with the value of the G-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static float unpackGAsFloat(final int colorARGB) {
		return unpackG(colorARGB) / 255.0F;
	}
	
	/**
	 * Returns a {@code float} with the value of the R-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return a {@code float} with the value of the R-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static float unpackRAsFloat(final int colorARGB) {
		return unpackR(colorARGB) / 255.0F;
	}
	
	/**
	 * Returns the average component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the average component value of {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static int average(final int r, final int g, final int b) {
		return (r + g + b) / 3;
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.blend(colorARGBLHS, colorARGBRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorARGBLHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGBRHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param t the factor to use for all components in the blending process
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGBLHS, final int colorARGBRHS, final double t) {
		return blend(colorARGBLHS, colorARGBRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGBLHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGBRHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGBLHS, final int colorARGBRHS, final double tR, final double tG, final double tB, final double tA) {
		final int r = doConvertComponentFromDoubleToInt(Doubles.lerp(unpackRAsDouble(colorARGBLHS), unpackRAsDouble(colorARGBRHS), tR));
		final int g = doConvertComponentFromDoubleToInt(Doubles.lerp(unpackGAsDouble(colorARGBLHS), unpackGAsDouble(colorARGBRHS), tG));
		final int b = doConvertComponentFromDoubleToInt(Doubles.lerp(unpackBAsDouble(colorARGBLHS), unpackBAsDouble(colorARGBRHS), tB));
		final int a = doConvertComponentFromDoubleToInt(Doubles.lerp(unpackAAsDouble(colorARGBLHS), unpackAAsDouble(colorARGBRHS), tA));
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.blend(colorARGBLHS, colorARGBRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorARGBLHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGBRHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param t the factor to use for all components in the blending process
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGBLHS, final int colorARGBRHS, final float t) {
		return blend(colorARGBLHS, colorARGBRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGBLHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGBRHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGBLHS, final int colorARGBRHS, final float tR, final float tG, final float tB, final float tA) {
		final int r = doConvertComponentFromFloatToInt(Floats.lerp(unpackRAsFloat(colorARGBLHS), unpackRAsFloat(colorARGBRHS), tR));
		final int g = doConvertComponentFromFloatToInt(Floats.lerp(unpackGAsFloat(colorARGBLHS), unpackGAsFloat(colorARGBRHS), tG));
		final int b = doConvertComponentFromFloatToInt(Floats.lerp(unpackBAsFloat(colorARGBLHS), unpackBAsFloat(colorARGBRHS), tB));
		final int a = doConvertComponentFromFloatToInt(Floats.lerp(unpackAAsFloat(colorARGBLHS), unpackAAsFloat(colorARGBRHS), tA));
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorARGB11}, {@code colorARGB12}, {@code colorARGB21} and {@code colorARGB22}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.blend(Color.blend(colorARGB11, colorARGB12, tX), Color.blend(colorARGB21, colorARGB22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param colorARGB11 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB12 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB21 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB22 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param tX the factor to use for all components in the first two blend operations
	 * @param tY the factor to use for all components in the third blend operation
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGB11, final int colorARGB12, final int colorARGB21, final int colorARGB22, final double tX, final double tY) {
		return blend(blend(colorARGB11, colorARGB12, tX), blend(colorARGB21, colorARGB22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorARGB11}, {@code colorARGB12}, {@code colorARGB21} and {@code colorARGB22}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.blend(Color.blend(colorARGB11, colorARGB12, tX), Color.blend(colorARGB21, colorARGB22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param colorARGB11 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB12 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB21 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGB22 an {@code int} that contains packed A-, R-, G- and B-components
	 * @param tX the factor to use for all components in the first two blend operations
	 * @param tY the factor to use for all components in the third blend operation
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blend(final int colorARGB11, final int colorARGB12, final int colorARGB21, final int colorARGB22, final float tX, final float tY) {
		return blend(blend(colorARGB11, colorARGB12, tX), blend(colorARGB21, colorARGB22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} over the component values of {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGBLHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @param colorARGBRHS an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int blendOver(final int colorARGBLHS, final int colorARGBRHS) {
		final double aLHS = unpackAAsDouble(colorARGBLHS);
		final double rLHS = unpackRAsDouble(colorARGBLHS);
		final double gLHS = unpackGAsDouble(colorARGBLHS);
		final double bLHS = unpackBAsDouble(colorARGBLHS);
		
		final double aRHS = unpackAAsDouble(colorARGBRHS);
		final double rRHS = unpackRAsDouble(colorARGBRHS);
		final double gRHS = unpackGAsDouble(colorARGBRHS);
		final double bRHS = unpackBAsDouble(colorARGBRHS);
		
		final double a = aLHS + aRHS * (1.0D - aLHS);
		final double r = (rLHS * aLHS + rRHS * aRHS * (1.0D - aLHS)) / a;
		final double g = (gLHS * aLHS + gRHS * aRHS * (1.0D - aLHS)) / a;
		final double b = (bLHS * aLHS + bRHS * aRHS * (1.0D - aLHS)) / a;
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.average(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB)), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleAverage(final int colorARGB) {
		return packGrayscaleA(average(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB)), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.unpackB(colorARGB), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleB(final int colorARGB) {
		return packGrayscaleA(unpackB(colorARGB), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.unpackG(colorARGB), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleG(final int colorARGB) {
		return packGrayscaleA(unpackG(colorARGB), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.lightness(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB)), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleLightness(final int colorARGB) {
		return packGrayscaleA(lightness(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB)), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.max(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB)), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleMax(final int colorARGB) {
		return packGrayscaleA(max(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB)), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.min(Color.unpackR(colorARGB), Color.unpackG(colorARGB), Color.unpackB(colorARGB)), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleMin(final int colorARGB) {
		return packGrayscaleA(min(unpackR(colorARGB), unpackG(colorARGB), unpackB(colorARGB)), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.unpackR(colorARGB), Color.unpackA(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleR(final int colorARGB) {
		return packGrayscaleA(unpackR(colorARGB), unpackA(colorARGB));
	}
	
	/**
	 * Returns an {@code int} that contains a grayscale color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscaleA(Color.relativeLuminance(Color.unpackRAsDouble(colorARGB), Color.unpackGAsDouble(colorARGB), Color.unpackBAsDouble(colorARGB)), Color.unpackAAsDouble(colorARGB));
	 * }
	 * </pre>
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains a grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int grayscaleRelativeLuminance(final int colorARGB) {
		return packGrayscaleA(relativeLuminance(unpackRAsDouble(colorARGB), unpackGAsDouble(colorARGB), unpackBAsDouble(colorARGB)), unpackAAsDouble(colorARGB));
	}
	
	/**
	 * Inverts the R-, G- and B-component values of the color in {@code colorARGB}.
	 * <p>
	 * Returns an {@code int} that contains the inverted color in packed form.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the inverted color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int invert(final int colorARGB) {
		final int r = 255 - unpackR(colorARGB);
		final int g = 255 - unpackG(colorARGB);
		final int b = 255 - unpackB(colorARGB);
		final int a =       unpackA(colorARGB);
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Returns the lightness of the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the lightness of the color represented by {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static int lightness(final int r, final int g, final int b) {
		return (max(r, g, b) + min(r, g, b)) / 2;
	}
	
	/**
	 * Returns the largest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the largest component value of {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static int max(final int r, final int g, final int b) {
		return Math.max(Math.max(r, g), b);
	}
	
	/**
	 * Returns the smallest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return the smallest component value of {@code r}, {@code g} and {@code b}
	 */
//	TODO: Add Unit Tests!
	public static int min(final int r, final int g, final int b) {
		return Math.min(Math.min(r, g), b);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 1.0D} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscale(grayscale, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 1.0D} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscale(final double grayscale) {
		return packGrayscaleA(grayscale, 1.0D);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 1.0F} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscale(grayscale, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 1.0F} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscale(final float grayscale) {
		return packGrayscaleA(grayscale, 1.0F);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 255} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packGrayscale(grayscale, 255);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code 255} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscale(final int grayscale) {
		return packGrayscaleA(grayscale, 255);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscaleA(final double grayscale, final double a) {
		return packRGBA(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscaleA(final float grayscale, final float a) {
		return packRGBA(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Returns an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the R, G- and B-components
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code grayscale}, {@code grayscale}, {@code grayscale} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packGrayscaleA(final int grayscale, final int a) {
		return packRGBA(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 1.0D} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(r, g, b, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 1.0D} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGB(final double r, final double g, final double b) {
		return packRGBA(r, g, b, 1.0D);
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 1.0D} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(r, g, b, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 1.0D} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGB(final float r, final float g, final float b) {
		return packRGBA(r, g, b, 1.0F);
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 255} in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.packRGBA(r, g, b, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code 255} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGB(final int r, final int g, final int b) {
		return packRGBA(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGBA(final double r, final double g, final double b, final double a) {
		return packRGBA(doConvertComponentFromDoubleToInt(r), doConvertComponentFromDoubleToInt(g), doConvertComponentFromDoubleToInt(b), doConvertComponentFromDoubleToInt(a));
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGBA(final float r, final float g, final float b, final float a) {
		return packRGBA(doConvertComponentFromFloatToInt(r), doConvertComponentFromFloatToInt(g), doConvertComponentFromFloatToInt(b), doConvertComponentFromFloatToInt(a));
	}
	
	/**
	 * Returns an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form.
	 * 
	 * @param r the value of the R-component
	 * @param g the value of the G-component
	 * @param b the value of the B-component
	 * @param a the value of the A-component
	 * @return an {@code int} that contains {@code r}, {@code g}, {@code b} and {@code a} in packed form
	 */
//	TODO: Add Unit Tests!
	public static int packRGBA(final int r, final int g, final int b, final int a) {
		final int packedR = ((r & 0xFF) << COLOR_A_R_G_B_SHIFT_R);
		final int packedG = ((g & 0xFF) << COLOR_A_R_G_B_SHIFT_G);
		final int packedB = ((b & 0xFF) << COLOR_A_R_G_B_SHIFT_B);
		final int packedA = ((a & 0xFF) << COLOR_A_R_G_B_SHIFT_A);
		
		return packedR | packedG | packedB | packedA;
	}
	
	/**
	 * Returns an {@code int} that contains a random color in packed form.
	 * 
	 * @return an {@code int} that contains a random color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int random() {
		return packRGBA(doNextInt(), doNextInt(), doNextInt(), 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random blue color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomBlue(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random blue color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomBlue() {
		return randomBlue(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random blue color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random B-component value, {@code b}, is generated in the range {@code [1, 256)}.</li>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [0, min(max(maxR, 0) + 1, b))}.</li>
	 * <li>The random G-component value, {@code g}, is generated in the range {@code [0, min(max(maxG, 0) + 1, b))}.</li>
	 * </ol>
	 * 
	 * @param maxR the maximum R-component value
	 * @param maxG the maximum G-component value
	 * @return an {@code int} that contains a random blue color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomBlue(final int maxR, final int maxG) {
		final int b = doNextInt(1, 256);
		final int r = doNextInt(0, Math.min(Math.max(maxR, 0) + 1, b));
		final int g = doNextInt(0, Math.min(Math.max(maxG, 0) + 1, b));
		
		return packRGBA(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random cyan color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomCyan(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random cyan color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomCyan() {
		return randomCyan(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random cyan color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random G-component value, {@code g}, is generated in the range {@code [max(min(minGB, 255), 1), 256)}.</li>
	 * <li>The random B-component value, {@code b}, is set to {@code g}.</li>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [0, min(max(maxR, 0) + 1, g))}.</li>
	 * </ol>
	 * 
	 * @param minGB the minimum G- and B-component values
	 * @param maxR the maximum R-component value
	 * @return an {@code int} that contains a random cyan color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomCyan(final int minGB, final int maxR) {
		final int x = doNextInt(Math.max(Math.min(minGB, 255), 1), 256);
		final int y = doNextInt(0, Math.min(Math.max(maxR, 0) + 1, x));
		
		return packRGBA(y, x, x, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random grayscale color in packed form.
	 * 
	 * @return an {@code int} that contains a random grayscale color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomGrayscale() {
		final int x = doNextInt();
		
		return packRGBA(x, x, x, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random green color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomGreen(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random green color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomGreen() {
		return randomGreen(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random green color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random G-component value, {@code g}, is generated in the range {@code [1, 256)}.</li>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [0, min(max(maxR, 0) + 1, g))}.</li>
	 * <li>The random B-component value, {@code b}, is generated in the range {@code [0, min(max(maxB, 0) + 1, g))}.</li>
	 * </ol>
	 * 
	 * @param maxR the maximum R-component value
	 * @param maxB the maximum B-component value
	 * @return an {@code int} that contains a random green color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomGreen(final int maxR, final int maxB) {
		final int g = doNextInt(1, 256);
		final int r = doNextInt(0, Math.min(Math.max(maxR, 0) + 1, g));
		final int b = doNextInt(0, Math.min(Math.max(maxB, 0) + 1, g));
		
		return packRGBA(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random magenta color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomMagenta(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random magenta color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomMagenta() {
		return randomMagenta(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random magenta color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [max(min(minRB, 255), 1), 256)}.</li>
	 * <li>The random B-component value, {@code b}, is set to {@code r}.</li>
	 * <li>The random G-component value, {@code g}, is generated in the range {@code [0, min(max(maxG, 0) + 1, r))}.</li>
	 * </ol>
	 * 
	 * @param minRB the minimum R- and B-component values
	 * @param maxG the maximum G-component value
	 * @return an {@code int} that contains a random magenta color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomMagenta(final int minRB, final int maxG) {
		final int x = doNextInt(Math.max(Math.min(minRB, 255), 1), 256);
		final int y = doNextInt(0, Math.min(Math.max(maxG, 0) + 1, x));
		
		return packRGBA(x, y, x, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random red color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomRed(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random red color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomRed() {
		return randomRed(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random red color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [1, 256)}.</li>
	 * <li>The random G-component value, {@code g}, is generated in the range {@code [0, min(max(maxG, 0) + 1, r))}.</li>
	 * <li>The random B-component value, {@code b}, is generated in the range {@code [0, min(max(maxB, 0) + 1, r))}.</li>
	 * </ol>
	 * 
	 * @param maxG the maximum G-component value
	 * @param maxB the maximum B-component value
	 * @return an {@code int} that contains a random red color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomRed(final int maxG, final int maxB) {
		final int r = doNextInt(1, 256);
		final int g = doNextInt(0, Math.min(Math.max(maxG, 0) + 1, r));
		final int b = doNextInt(0, Math.min(Math.max(maxB, 0) + 1, r));
		
		return packRGBA(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} that contains a random yellow color in packed form.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color.randomYellow(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} that contains a random yellow color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomYellow() {
		return randomYellow(0, 0);
	}
	
	/**
	 * Returns an {@code int} that contains a random yellow color in packed form.
	 * <p>
	 * This method works in the following way:
	 * <ol>
	 * <li>The random R-component value, {@code r}, is generated in the range {@code [max(min(minRG, 255), 1), 256)}.</li>
	 * <li>The random G-component value, {@code g}, is set to {@code r}.</li>
	 * <li>The random B-component value, {@code b}, is generated in the range {@code [0, min(max(maxB, 0) + 1, r))}.</li>
	 * </ol>
	 * 
	 * @param minRG the minimum R- and G-component values
	 * @param maxB the maximum B-component value
	 * @return an {@code int} that contains a random yellow color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int randomYellow(final int minRG, final int maxB) {
		final int x = doNextInt(Math.max(Math.min(minRG, 255), 0), 256);
		final int y = doNextInt(0, Math.min(Math.max(maxB, 0) + 1, x));
		
		return packRGBA(x, x, y, 255);
	}
	
	/**
	 * Redoes gamma correction on {@code colorARGB}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int redoGammaCorrection(final int colorARGB) {
		final double r = doRedoGammaCorrection(unpackRAsDouble(colorARGB));
		final double g = doRedoGammaCorrection(unpackGAsDouble(colorARGB));
		final double b = doRedoGammaCorrection(unpackBAsDouble(colorARGB));
		final double a =                       unpackAAsDouble(colorARGB);
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Converts {@code colorARGB} to its Sepia-representation.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int sepia(final int colorARGB) {
		final double colorAR = unpackRAsDouble(colorARGB);
		final double colorAG = unpackGAsDouble(colorARGB);
		final double colorAB = unpackBAsDouble(colorARGB);
		final double colorAA = unpackAAsDouble(colorARGB);
		
		final double colorBR = colorAR * 0.393D + colorAG * 0.769D + colorAB * 0.189D;
		final double colorBG = colorAR * 0.349D + colorAG * 0.686D + colorAB * 0.168D;
		final double colorBB = colorAR * 0.272D + colorAG * 0.534D + colorAB * 0.131D;
		final double colorBA = colorAA;
		
		return packRGBA(colorBR, colorBG, colorBB, colorBA);
	}
	
	/**
	 * Undoes gamma correction on {@code colorARGB}.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the color in packed form
	 */
//	TODO: Add Unit Tests!
	public static int undoGammaCorrection(final int colorARGB) {
		final double r = doUndoGammaCorrection(unpackRAsDouble(colorARGB));
		final double g = doUndoGammaCorrection(unpackGAsDouble(colorARGB));
		final double b = doUndoGammaCorrection(unpackBAsDouble(colorARGB));
		final double a =                       unpackAAsDouble(colorARGB);
		
		return packRGBA(r, g, b, a);
	}
	
	/**
	 * Returns an {@code int} with the value of the A-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} with the value of the A-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static int unpackA(final int colorARGB) {
		return (colorARGB >> COLOR_A_R_G_B_SHIFT_A) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the value of the B-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} with the value of the B-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static int unpackB(final int colorARGB) {
		return (colorARGB >> COLOR_A_R_G_B_SHIFT_B) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the value of the G-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} with the value of the G-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static int unpackG(final int colorARGB) {
		return (colorARGB >> COLOR_A_R_G_B_SHIFT_G) & 0xFF;
	}
	
	/**
	 * Returns an {@code int} with the value of the R-component in {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} with the value of the R-component in {@code colorARGB}
	 */
//	TODO: Add Unit Tests!
	public static int unpackR(final int colorARGB) {
		return (colorARGB >> COLOR_A_R_G_B_SHIFT_R) & 0xFF;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doRedoGammaCorrection(final double value) {
		return value <= COLOR_SPACE_BREAK_POINT ? value * COLOR_SPACE_SLOPE : COLOR_SPACE_SLOPE_MATCH * Math.pow(value, 1.0D / COLOR_SPACE_GAMMA) - COLOR_SPACE_SEGMENT_OFFSET;
	}
	
	private static double doUndoGammaCorrection(final double value) {
		return value <= COLOR_SPACE_BREAK_POINT * COLOR_SPACE_SLOPE ? value / COLOR_SPACE_SLOPE : Math.pow((value + COLOR_SPACE_SEGMENT_OFFSET) / COLOR_SPACE_SLOPE_MATCH, COLOR_SPACE_GAMMA);
	}
	
	private static int doConvertComponentFromDoubleToInt(final double component) {
		return (int)(Doubles.saturate(component) * 255.0D + 0.5D);
	}
	
	private static int doConvertComponentFromFloatToInt(final float component) {
		return (int)(Floats.saturate(component) * 255.0F + 0.5F);
	}
	
	private static int doNextInt() {
		return doNextInt(0, 256);
	}
	
	private static int doNextInt(final int origin, final int bound) {
		return ThreadLocalRandom.current().nextInt(origin, bound);
	}
}