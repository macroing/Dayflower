/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import org.macroing.java.lang.Ints;
import org.macroing.java.util.Randoms;

/**
 * A {@code Color3I} represents a color with three {@code int}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color3I {
	/**
	 * A {@code Color3I} instance that represents the color black.
	 */
	public static final Color3I BLACK;
	
	/**
	 * A {@code Color3I} instance that represents the color blue.
	 */
	public static final Color3I BLUE;
	
	/**
	 * A {@code Color3I} instance that represents the color cyan.
	 */
	public static final Color3I CYAN;
	
	/**
	 * A {@code Color3I} instance that represents the color gray.
	 */
	public static final Color3I GRAY;
	
	/**
	 * A {@code Color3I} instance that represents the color green.
	 */
	public static final Color3I GREEN;
	
	/**
	 * A {@code Color3I} instance that represents the color magenta.
	 */
	public static final Color3I MAGENTA;
	
	/**
	 * A {@code Color3I} instance that represents the color red.
	 */
	public static final Color3I RED;
	
	/**
	 * A {@code Color3I} instance that represents the color white.
	 */
	public static final Color3I WHITE;
	
	/**
	 * A {@code Color3I} instance that represents the color yellow.
	 */
	public static final Color3I YELLOW;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color3I, Color3I> CACHE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		CACHE = new HashMap<>();
		
		BLACK = getCached(new Color3I(0, 0, 0));
		BLUE = getCached(new Color3I(0, 0, 255));
		CYAN = getCached(new Color3I(0, 255, 255));
		GRAY = getCached(new Color3I(128, 128, 128));
		GREEN = getCached(new Color3I(0, 255, 0));
		MAGENTA = getCached(new Color3I(255, 0, 255));
		RED = getCached(new Color3I(255, 0, 0));
		WHITE = getCached(new Color3I(255, 255, 255));
		YELLOW = getCached(new Color3I(255, 255, 0));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The value of the blue component.
	 */
	public final int b;
	
	/**
	 * The value of the green component.
	 */
	public final int g;
	
	/**
	 * The value of the red component.
	 */
	public final int r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color3I} instance that represents black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I(0);
	 * }
	 * </pre>
	 */
	public Color3I() {
		this(0);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3I(final Color3D color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3I(final Color3F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3I(final Color4D color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3I(final Color4F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3I(final Color4I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3I(final double grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I((int)(r * 255.0D + 0.5D), (int)(g * 255.0D + 0.5D), (int)(b * 255.0D + 0.5D));
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3I(final double r, final double g, final double b) {
		this((int)(r * 255.0D + 0.5D), (int)(g * 255.0D + 0.5D), (int)(b * 255.0D + 0.5D));
	}
	
	/**
	 * Constructs a new {@code Color3I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3I(final float grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I((int)(r * 255.0F + 0.5F), (int)(g * 255.0F + 0.5F), (int)(b * 255.0F + 0.5F));
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3I(final float r, final float g, final float b) {
		this((int)(r * 255.0F + 0.5F), (int)(g * 255.0F + 0.5F), (int)(b * 255.0F + 0.5F));
	}
	
	/**
	 * Constructs a new {@code Color3I} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3I(final int grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3I} instance.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3I(final int r, final int g, final int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color3I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color3I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color3I(%s, %s, %s)", Integer.toString(this.r), Integer.toString(this.g), Integer.toString(this.b));
	}
	
	/**
	 * Compares {@code color} to this {@code Color3I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is equal to this {@code Color3I} instance, {@code false} otherwise.
	 * 
	 * @param color the {@code Color3I} to compare to this {@code Color3I} instance for equality
	 * @return {@code true} if, and only if, {@code color} is equal to this {@code Color3I} instance, {@code false} otherwise
	 */
	public boolean equals(final Color3I color) {
		if(color == this) {
			return true;
		} else if(color == null) {
			return false;
		} else if(this.b != color.b) {
			return false;
		} else if(this.g != color.g) {
			return false;
		} else if(this.r != color.r) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Color3I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color3I}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color3I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color3I}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color3I)) {
			return false;
		} else {
			return equals(Color3I.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is black, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isBlack(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color3I} instance {@code color} is blue if, and only if, {@code color.b - deltaR >= color.r} and {@code color.b - deltaG >= color.g}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, this {@code Color3I} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue(final int deltaR, final int deltaG) {
		return isBlue(this.r, this.g, this.b, deltaR, deltaG);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return isCyan(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is grayscale, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is grayscale, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return isGrayscale(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is green, {@code false} otherwise.
	 * <p>
	 * The {@code Color3I} instance {@code color} is green if, and only if, {@code color.g - deltaR >= color.r} and {@code color.g - deltaB >= color.b}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3I} instance is green, {@code false} otherwise
	 */
	public boolean isGreen(final int deltaR, final int deltaB) {
		return isGreen(this.r, this.g, this.b, deltaR, deltaB);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return isMagenta(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is red, {@code false} otherwise.
	 * <p>
	 * The {@code Color3I} instance {@code color} is red if, and only if, {@code color.r - deltaG >= color.g} and {@code color.r - deltaB >= color.b}.
	 * 
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3I} instance is red, {@code false} otherwise
	 */
	public boolean isRed(final int deltaG, final int deltaB) {
		return isRed(this.r, this.g, this.b, deltaG, deltaB);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is white, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isWhite(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3I} instance is yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3I} instance is yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return isYellow(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the average component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the average component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int average() {
		return average(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the lightness for this {@code Color3I} instance.
	 * 
	 * @return the lightness for this {@code Color3I} instance
	 */
	public int lightness() {
		return lightness(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the largest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the largest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int max() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the smallest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int min() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the relative luminance for this {@code Color3I} instance.
	 * 
	 * @return the relative luminance for this {@code Color3I} instance
	 */
	public int relativeLuminance() {
		return relativeLuminance(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color3I} instance.
	 * 
	 * @return a hash code for this {@code Color3I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.b), Integer.valueOf(this.g), Integer.valueOf(this.r));
	}
	
	/**
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * <p>
	 * The alpha component is treated as if it was {@code 255}.
	 * 
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public int toIntARGB() {
		return toIntARGB(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the blue component as an {@code int}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(color.b);
	 * }
	 * </pre>
	 * 
	 * @return the blue component as an {@code int}
	 */
	public int toIntB() {
		return Ints.saturate(this.b);
	}
	
	/**
	 * Returns the green component as an {@code int}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(color.g);
	 * }
	 * </pre>
	 * 
	 * @return the green component as an {@code int}
	 */
	public int toIntG() {
		return Ints.saturate(this.g);
	}
	
	/**
	 * Returns the red component as an {@code int}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(color.r);
	 * }
	 * </pre>
	 * 
	 * @return the red component as an {@code int}
	 */
	public int toIntR() {
		return Ints.saturate(this.r);
	}
	
	/**
	 * Returns the red, green and blue components as an {@code int} in the format RGB.
	 * 
	 * @return the red, green and blue components as an {@code int} in the format RGB
	 */
	public int toIntRGB() {
		return toIntRGB(this.r, this.g, this.b);
	}
	
	/**
	 * Writes this {@code Color3I} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeInt(this.r);
			dataOutput.writeInt(this.g);
			dataOutput.writeInt(this.b);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param colorRHS the {@code Color3I} instance on the right-hand side
	 * @return a new {@code Color3I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3I add(final Color3I colorLHS, final Color3I colorRHS) {
		final int r = colorLHS.r + colorRHS.r;
		final int g = colorLHS.g + colorRHS.g;
		final int b = colorLHS.b + colorRHS.b;
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Adds {@code scalarRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the addition.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3I add(final Color3I colorLHS, final int scalarRHS) {
		final int r = colorLHS.r + scalarRHS;
		final int g = colorLHS.g + scalarRHS;
		final int b = colorLHS.b + scalarRHS;
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.blend(colorLHS, colorRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param colorRHS the {@code Color3I} instance on the right-hand side
	 * @return a new {@code Color3I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3I blend(final Color3I colorLHS, final Color3I colorRHS) {
		return blend(colorLHS, colorRHS, 0.5D);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.blend(Color3I.blend(color11, color12, tX), Color3I.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color3I} instance on row 1 and column 1
	 * @param color12 the {@code Color3I} instance on row 1 and column 2
	 * @param color21 the {@code Color3I} instance on row 2 and column 1
	 * @param color22 the {@code Color3I} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color3I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
	public static Color3I blend(final Color3I color11, final Color3I color12, final Color3I color21, final Color3I color22, final double tX, final double tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.blend(colorLHS, colorRHS, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param colorRHS the {@code Color3I} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color3I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3I blend(final Color3I colorLHS, final Color3I colorRHS, final double t) {
		return blend(colorLHS, colorRHS, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param colorRHS the {@code Color3I} instance on the right-hand side
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @return a new {@code Color3I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3I blend(final Color3I colorLHS, final Color3I colorRHS, final double tR, final double tG, final double tB) {
		final int r = Ints.lerp(colorLHS.r, colorRHS.r, tR);
		final int g = Ints.lerp(colorLHS.g, colorRHS.g, tG);
		final int b = Ints.lerp(colorLHS.b, colorRHS.b, tB);
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance from the {@code int} {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains the alpha, red, green and blue components
	 * @return a {@code Color3I} instance from the {@code int} {@code colorARGB}
	 */
	public static Color3I fromIntARGB(final int colorARGB) {
		final int r = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance from the {@code int} {@code colorRGB}.
	 * 
	 * @param colorRGB an {@code int} that contains the red, green and blue components
	 * @return a {@code Color3I} instance from the {@code int} {@code colorRGB}
	 */
	public static Color3I fromIntRGB(final int colorRGB) {
		final int r = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a cached {@code Color3I} instance that is equal to {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a cached {@code Color3I} instance that is equal to {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I getCached(final Color3I color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleAverage(final Color3I color) {
		return new Color3I(color.average());
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.b}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.b}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleB(final Color3I color) {
		return new Color3I(color.b);
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.g}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.g}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleG(final Color3I color) {
		return new Color3I(color.g);
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleLightness(final Color3I color) {
		return new Color3I(color.lightness());
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.max()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.max()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleMax(final Color3I color) {
		return new Color3I(color.max());
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.min()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.min()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleMin(final Color3I color) {
		return new Color3I(color.min());
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.r}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.r}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleR(final Color3I color) {
		return new Color3I(color.r);
	}
	
	/**
	 * Returns a grayscale {@code Color3I} instance based on {@code color.relativeLuminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a grayscale {@code Color3I} instance based on {@code color.relativeLuminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I grayscaleRelativeLuminance(final Color3I color) {
		return new Color3I(color.relativeLuminance());
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a new {@code Color3I} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I invert(final Color3I color) {
		return new Color3I(255 - color.r, 255 - color.g, 255 - color.b);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3I} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3I multiply(final Color3I colorLHS, final int scalarRHS) {
		final int r = colorLHS.r * scalarRHS;
		final int g = colorLHS.g * scalarRHS;
		final int b = colorLHS.b * scalarRHS;
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3I(Randoms.nextInt(256), Randoms.nextInt(256), Randoms.nextInt(256));
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values
	 */
	public static Color3I random() {
		return new Color3I(Randoms.nextInt(256), Randoms.nextInt(256), Randoms.nextInt(256));
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a blue color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomBlue(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a blue color
	 */
	public static Color3I randomBlue() {
		return randomBlue(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a blue color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3I} instance with random component values that represents a blue color
	 */
	public static Color3I randomBlue(final int maxR, final int maxG) {
		final int b = Randoms.nextInt(1, 256);
		final int r = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, b));
		final int g = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, b));
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a cyan color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomCyan(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a cyan color
	 */
	public static Color3I randomCyan() {
		return randomCyan(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a cyan color.
	 * 
	 * @param minGB the minimum value to use for the G- and B-components
	 * @param maxR the maximum value to use for the R-component
	 * @return a {@code Color3I} instance with random component values that represents a cyan color
	 */
	public static Color3I randomCyan(final int minGB, final int maxR) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minGB, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, x));
		
		return new Color3I(y, x, x);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a grayscale color.
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a grayscale color
	 */
	public static Color3I randomGrayscale() {
		return new Color3I(Randoms.nextInt(256));
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a green color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomGreen(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a green color
	 */
	public static Color3I randomGreen() {
		return randomGreen(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a green color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3I} instance with random component values that represents a green color
	 */
	public static Color3I randomGreen(final int maxR, final int maxB) {
		final int g = Randoms.nextInt(1, 256);
		final int r = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, g));
		final int b = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, g));
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a magenta color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomMagenta(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a magenta color
	 */
	public static Color3I randomMagenta() {
		return randomMagenta(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a magenta color.
	 * 
	 * @param minRB the minimum value to use for the R- and B-components
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3I} instance with random component values that represents a magenta color
	 */
	public static Color3I randomMagenta(final int minRB, final int maxG) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minRB, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, x));
		
		return new Color3I(x, y, x);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a red color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomRed(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a red color
	 */
	public static Color3I randomRed() {
		return randomRed(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a red color.
	 * 
	 * @param maxG the maximum value to use for the G-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3I} instance with random component values that represents a red color
	 */
	public static Color3I randomRed(final int maxG, final int maxB) {
		final int r = Randoms.nextInt(1, 256);
		final int g = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, r));
		final int b = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, r));
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a yellow color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.randomYellow(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3I} instance with random component values that represents a yellow color
	 */
	public static Color3I randomYellow() {
		return randomYellow(0, 0);
	}
	
	/**
	 * Returns a {@code Color3I} instance with random component values that represents a yellow color.
	 * 
	 * @param minRG the minimum value to use for the R- and G-components
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3I} instance with random component values that represents a yellow color
	 */
	public static Color3I randomYellow(final int minRG, final int maxB) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minRG, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, x));
		
		return new Color3I(x, x, y);
	}
	
	/**
	 * Returns a new {@code Color3I} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color3I} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Color3I read(final DataInput dataInput) {
		try {
			final int r = dataInput.readInt();
			final int g = dataInput.readInt();
			final int b = dataInput.readInt();
			
			return new Color3I(r, g, b);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [0, 255]}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.saturate(color, 0, 255);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a new {@code Color3I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I saturate(final Color3I color) {
		return saturate(color, 0, 255);
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [Ints.min(componentMinMax, componentMaxMin), Ints.max(componentMinMax, componentMaxMin)]}.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @param componentMinMax the minimum or maximum component value
	 * @param componentMaxMin the maximum or minimum component value
	 * @return a new {@code Color3I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I saturate(final Color3I color, final int componentMinMax, final int componentMaxMin) {
		final int r = Ints.saturate(color.r, componentMinMax, componentMaxMin);
		final int g = Ints.saturate(color.g, componentMinMax, componentMaxMin);
		final int b = Ints.saturate(color.b, componentMinMax, componentMaxMin);
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color3I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3I} instance
	 * @return a new {@code Color3I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3I sepia(final Color3I color) {
		final int r = (int)(color.r * 0.393D + color.g * 0.769D + color.b * 0.189D);
		final int g = (int)(color.r * 0.349D + color.g * 0.686D + color.b * 0.168D);
		final int b = (int)(color.r * 0.272D + color.g * 0.534D + color.b * 0.131D);
		
		return new Color3I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3I[]} with a length of {@code length} and contains {@code Color3I.BLACK}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.array(length, index -> Color3I.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3I[]} with a length of {@code length} and contains {@code Color3I.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3I[] array(final int length) {
		return array(length, index -> BLACK);
	}
	
	/**
	 * Returns a {@code Color3I[]} with a length of {@code length} and contains {@code Color3I} instances produced by {@code function}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code function} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param function an {@code IntFunction}
	 * @return a {@code Color3I[]} with a length of {@code length} and contains {@code Color3I} instances produced by {@code function}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or returns {@code null}
	 */
	public static Color3I[] array(final int length, final IntFunction<Color3I> function) {
		final Color3I[] colors = new Color3I[Ints.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(function, "function == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(function.apply(i));
		}
		
		return colors;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is black, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is black, {@code false} otherwise
	 */
	public static boolean isBlack(final int r, final int g, final int b) {
		return r == 0 && g == 0 && b == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.isBlue(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise
	 */
	public static boolean isBlue(final int r, final int g, final int b) {
		return isBlue(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise.
	 * <p>
	 * The color is blue if, and only if, {@code b - deltaR >= r} and {@code b - deltaG >= g}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is blue, {@code false} otherwise
	 */
	public static boolean isBlue(final int r, final int g, final int b, final int deltaR, final int deltaG) {
		return b - deltaR >= r && b - deltaG >= g;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is cyan, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is cyan, {@code false} otherwise
	 */
	public static boolean isCyan(final int r, final int g, final int b) {
		return g == b && r < g;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is grayscale, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is grayscale, {@code false} otherwise
	 */
	public static boolean isGrayscale(final int r, final int g, final int b) {
		return r == g && g == b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.isGreen(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise
	 */
	public static boolean isGreen(final int r, final int g, final int b) {
		return isGreen(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise.
	 * <p>
	 * The color is green if, and only if, {@code g - deltaR >= r} and {@code g - deltaB >= b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is green, {@code false} otherwise
	 */
	public static boolean isGreen(final int r, final int g, final int b, final int deltaR, final int deltaB) {
		return g - deltaR >= r && g - deltaB >= b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is magenta, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is magenta, {@code false} otherwise
	 */
	public static boolean isMagenta(final int r, final int g, final int b) {
		return r == b && g < b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3I.isRed(r, g, b, 255, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise
	 */
	public static boolean isRed(final int r, final int g, final int b) {
		return isRed(r, g, b, 255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise.
	 * <p>
	 * The color is red if, and only if, {@code r - deltaG >= g} and {@code r - deltaB >= b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is red, {@code false} otherwise
	 */
	public static boolean isRed(final int r, final int g, final int b, final int deltaG, final int deltaB) {
		return r - deltaG >= g && r - deltaB >= b;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is white, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is white, {@code false} otherwise
	 */
	public static boolean isWhite(final int r, final int g, final int b) {
		return r == 255 && g == 255 && b == 255;
	}
	
	/**
	 * Returns {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is yellow, {@code false} otherwise.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return {@code true} if, and only if, the color represented by {@code r}, {@code g} and {@code b} is yellow, {@code false} otherwise
	 */
	public static boolean isYellow(final int r, final int g, final int b) {
		return r == g && b < r;
	}
	
	/**
	 * Returns the size of the cache.
	 * 
	 * @return the size of the cache
	 */
	public static int getCacheSize() {
		return CACHE.size();
	}
	
	/**
	 * Returns the average component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the average component value of {@code r}, {@code g} and {@code b}
	 */
	public static int average(final int r, final int g, final int b) {
		return (r + g + b) / 3;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorARGB} as an {@code int}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the B-component in {@code colorARGB} as an {@code int}
	 */
	public static int fromIntARGBToIntB(final int colorARGB) {
		return (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorARGB} as an {@code int}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the G-component in {@code colorARGB} as an {@code int}
	 */
	public static int fromIntARGBToIntG(final int colorARGB) {
		return (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorARGB} as an {@code int}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the R-component in {@code colorARGB} as an {@code int}
	 */
	public static int fromIntARGBToIntR(final int colorARGB) {
		return (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorRGB} as an {@code int}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the B-component in {@code colorRGB} as an {@code int}
	 */
	public static int fromIntRGBToIntB(final int colorRGB) {
		return (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorRGB} as an {@code int}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the G-component in {@code colorRGB} as an {@code int}
	 */
	public static int fromIntRGBToIntG(final int colorRGB) {
		return (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorRGB} as an {@code int}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the R-component in {@code colorRGB} as an {@code int}
	 */
	public static int fromIntRGBToIntR(final int colorRGB) {
		return (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
	}
	
	/**
	 * Returns the lightness for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the lightness for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static int lightness(final int r, final int g, final int b) {
		return (max(r, g, b) + min(r, g, b)) / 2;
	}
	
	/**
	 * Returns the largest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the largest component value of {@code r}, {@code g} and {@code b}
	 */
	public static int max(final int r, final int g, final int b) {
		return Ints.max(r, g, b);
	}
	
	/**
	 * Returns the smallest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the smallest component value of {@code r}, {@code g} and {@code b}
	 */
	public static int min(final int r, final int g, final int b) {
		return Ints.min(r, g, b);
	}
	
	/**
	 * Returns the relative luminance for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the relative luminance for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static int relativeLuminance(final int r, final int g, final int b) {
		return (int)(r * 0.212671D + g * 0.715160D + b * 0.072169D);
	}
	
	/**
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * <p>
	 * The alpha component is treated as if it was {@code 255}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public static int toIntARGB(final int r, final int g, final int b) {
		final int a = 255;
		
		final int colorA = ((Ints.saturate(a) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_A);
		final int colorR = ((Ints.saturate(r) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_R);
		final int colorG = ((Ints.saturate(g) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_G);
		final int colorB = ((Ints.saturate(b) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_B);
		
		return colorA | colorR | colorG | colorB;
	}
	
	/**
	 * Returns the red, green and blue components as an {@code int} in the format RGB.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the red, green and blue components as an {@code int} in the format RGB
	 */
	public static int toIntRGB(final int r, final int g, final int b) {
		final int colorR = ((Ints.saturate(r) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_R);
		final int colorG = ((Ints.saturate(g) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_G);
		final int colorB = ((Ints.saturate(b) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_B);
		
		return colorR | colorG | colorB;
	}
	
	/**
	 * Clears the cache.
	 */
	public static void clearCache() {
		CACHE.clear();
	}
}