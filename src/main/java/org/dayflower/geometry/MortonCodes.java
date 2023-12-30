/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
package org.dayflower.geometry;

import java.lang.reflect.Field;//TODO: Add Unit Tests!

import org.dayflower.utility.ParameterArguments;

/**
 * A class that consists exclusively of static methods that performs operations on Morton codes.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MortonCodes {
	private MortonCodes() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Decodes {@code mortonCodePart} into a coordinate.
	 * <p>
	 * Returns the Morton code coordinate of {@code mortonCodePart}.
	 * <p>
	 * If {@code mortonCodePart} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCodePart the Morton code part to decode
	 * @return the Morton code coordinate of {@code mortonCodePart}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCodePart} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By1(final int mortonCodePart) {
		ParameterArguments.requireRange(mortonCodePart, 0, Integer.MAX_VALUE, "mortonCodePart");
		
		final int a = mortonCodePart & 0x55555555;
		final int b = (a ^ (a >>> 1)) & 0x33333333;
		final int c = (b ^ (b >>> 2)) & 0x0F0F0F0F;
		final int d = (c ^ (c >>> 4)) & 0x00FF00FF;
		final int e = (d ^ (d >>> 8)) & 0x0000FFFF;
		
		return e;
	}
	
	/**
	 * Returns the X-coordinate of the Morton code {@code mortonCode}.
	 * <p>
	 * If {@code mortonCode} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return the X-coordinate of the Morton code {@code mortonCode}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCode} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By1X(final int mortonCode) {
		ParameterArguments.requireRange(mortonCode, 0, Integer.MAX_VALUE, "mortonCode");
		
		return decode1By1(mortonCode >>> 0);
	}
	
	/**
	 * Returns the Y-coordinate of the Morton code {@code mortonCode}.
	 * <p>
	 * If {@code mortonCode} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return the Y-coordinate of the Morton code {@code mortonCode}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCode} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By1Y(final int mortonCode) {
		ParameterArguments.requireRange(mortonCode, 0, Integer.MAX_VALUE, "mortonCode");
		
		return decode1By1(mortonCode >>> 1);
	}
	
	/**
	 * Decodes {@code mortonCodePart} into a coordinate.
	 * <p>
	 * Returns the Morton code coordinate of {@code mortonCodePart}.
	 * <p>
	 * If {@code mortonCodePart} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCodePart the Morton code part to decode
	 * @return the Morton code coordinate of {@code mortonCodePart}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCodePart} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By2(final int mortonCodePart) {
		ParameterArguments.requireRange(mortonCodePart, 0, Integer.MAX_VALUE, "mortonCodePart");
		
		final int a = mortonCodePart & 0x09249249;
		final int b = (a ^ (a >>>  2)) & 0x030C30C3;
		final int c = (b ^ (b >>>  4)) & 0x0300F00F;
		final int d = (c ^ (c >>>  8)) & 0xFF0000FF;
		final int e = (d ^ (d >>> 16)) & 0x000003FF;
		
		return e;
	}
	
	/**
	 * Returns the X-coordinate of the Morton code {@code mortonCode}.
	 * <p>
	 * If {@code mortonCode} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return the X-coordinate of the Morton code {@code mortonCode}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCode} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By2X(final int mortonCode) {
		ParameterArguments.requireRange(mortonCode, 0, Integer.MAX_VALUE, "mortonCode");
		
		return decode1By2(mortonCode >>> 0);
	}
	
	/**
	 * Returns the Y-coordinate of the Morton code {@code mortonCode}.
	 * <p>
	 * If {@code mortonCode} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return the Y-coordinate of the Morton code {@code mortonCode}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCode} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By2Y(final int mortonCode) {
		ParameterArguments.requireRange(mortonCode, 0, Integer.MAX_VALUE, "mortonCode");
		
		return decode1By2(mortonCode >>> 1);
	}
	
	/**
	 * Returns the Z-coordinate of the Morton code {@code mortonCode}.
	 * <p>
	 * If {@code mortonCode} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param mortonCode the Morton code to decode
	 * @return the Z-coordinate of the Morton code {@code mortonCode}
	 * @throws IllegalArgumentException thrown if, and only if, {@code mortonCode} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int decode1By2Z(final int mortonCode) {
		ParameterArguments.requireRange(mortonCode, 0, Integer.MAX_VALUE, "mortonCode");
		
		return decode1By2(mortonCode >>> 2);
	}
	
	/**
	 * Encodes {@code coordinate} into a Morton code part.
	 * <p>
	 * Returns the Morton code part of {@code coordinate}.
	 * <p>
	 * If {@code coordinate} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param coordinate the coordinate to encode
	 * @return the Morton code part of {@code coordinate}
	 * @throws IllegalArgumentException thrown if, and only if, {@code coordinate} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int encode1By1(final int coordinate) {
		ParameterArguments.requireRange(coordinate, 0, Integer.MAX_VALUE, "coordinate");
		
		final int a = coordinate & 0x0000FFFF;
		final int b = (a ^ (a << 8)) & 0x00FF00FF;
		final int c = (b ^ (b << 4)) & 0x0F0F0F0F;
		final int d = (c ^ (c << 2)) & 0x33333333;
		final int e = (d ^ (d << 1)) & 0x55555555;
		
		return e;
	}
	
	/**
	 * Returns the Morton code of {@code x} and {@code y}.
	 * <p>
	 * If either {@code x} or {@code y} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param x the X-coordinate to encode
	 * @param y the Y-coordinate to encode
	 * @return the Morton code of {@code x} and {@code y}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code x} or {@code y} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int encode1By1(final int x, final int y) {
		ParameterArguments.requireRange(x, 0, Integer.MAX_VALUE, "x");
		ParameterArguments.requireRange(y, 0, Integer.MAX_VALUE, "y");
		
		return (encode1By1(x) << 0) | (encode1By1(y) << 1);
	}
	
	/**
	 * Encodes {@code coordinate} into a Morton code part.
	 * <p>
	 * Returns the Morton code part of {@code coordinate}.
	 * <p>
	 * If {@code coordinate} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param coordinate the coordinate to encode
	 * @return the Morton code part of {@code coordinate}
	 * @throws IllegalArgumentException thrown if, and only if, {@code coordinate} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int encode1By2(final int coordinate) {
		ParameterArguments.requireRange(coordinate, 0, Integer.MAX_VALUE, "coordinate");
		
		final int a = coordinate & 0x000003FF;
		final int b = (a ^ (a << 16)) & 0xFF0000FF;
		final int c = (b ^ (b <<  8)) & 0x0300F00F;
		final int d = (c ^ (c <<  4)) & 0x030C30C3;
		final int e = (d ^ (d <<  2)) & 0x09249249;
		
		return e;
	}
	
	/**
	 * Returns the Morton code of {@code x}, {@code y} and {@code z}.
	 * <p>
	 * If either {@code x}, {@code y} or {@code z} are less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param x the X-coordinate to encode
	 * @param y the Y-coordinate to encode
	 * @param z the Z-coordinate to encode
	 * @return the Morton code of {@code x}, {@code y} and {@code z}
	 * @throws IllegalArgumentException thrown if, and only if, either {@code x}, {@code y} or {@code z} are less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static int encode1By2(final int x, final int y, final int z) {
		ParameterArguments.requireRange(x, 0, Integer.MAX_VALUE, "x");
		ParameterArguments.requireRange(y, 0, Integer.MAX_VALUE, "y");
		ParameterArguments.requireRange(z, 0, Integer.MAX_VALUE, "z");
		
		return (encode1By2(x) << 0) | (encode1By2(y) << 1) | (encode1By2(z) << 2);
	}
}