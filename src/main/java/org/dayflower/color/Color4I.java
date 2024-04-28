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
package org.dayflower.color;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntFunction;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Ints;
import org.macroing.java.util.Randoms;

/**
 * A {@code Color4I} represents a color with four {@code int}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color4I {
	/**
	 * A {@code Color4I} instance that represents the color black.
	 */
	public static final Color4I BLACK;
	
	/**
	 * A {@code Color4I} instance that represents the color blue.
	 */
	public static final Color4I BLUE;
	
	/**
	 * A {@code Color4I} instance that represents the color cyan.
	 */
	public static final Color4I CYAN;
	
	/**
	 * A {@code Color4I} instance that represents the color gray.
	 */
	public static final Color4I GRAY;
	
	/**
	 * A {@code Color4I} instance that represents the color green.
	 */
	public static final Color4I GREEN;
	
	/**
	 * A {@code Color4I} instance that represents the color magenta.
	 */
	public static final Color4I MAGENTA;
	
	/**
	 * A {@code Color4I} instance that represents the color red.
	 */
	public static final Color4I RED;
	
	/**
	 * A {@code Color4I} instance that represents the color transparent.
	 */
	public static final Color4I TRANSPARENT;
	
	/**
	 * A {@code Color4I} instance that represents the color white.
	 */
	public static final Color4I WHITE;
	
	/**
	 * A {@code Color4I} instance that represents the color yellow.
	 */
	public static final Color4I YELLOW;
	
	/**
	 * An {@code int} that represents the color black in the format ARGB.
	 */
	public static final int BLACK_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color blue in the format ARGB.
	 */
	public static final int BLUE_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color cyan in the format ARGB.
	 */
	public static final int CYAN_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color gray in the format ARGB.
	 */
	public static final int GRAY_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color green in the format ARGB.
	 */
	public static final int GREEN_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color magenta in the format ARGB.
	 */
	public static final int MAGENTA_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color red in the format ARGB.
	 */
	public static final int RED_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color transparent in the format ARGB.
	 */
	public static final int TRANSPARENT_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color white in the format ARGB.
	 */
	public static final int WHITE_A_R_G_B;
	
	/**
	 * An {@code int} that represents the color yellow in the format ARGB.
	 */
	public static final int YELLOW_A_R_G_B;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color4I, Color4I> CACHE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		CACHE = new HashMap<>();
		
		BLACK = getCached(new Color4I(0, 0, 0));
		BLUE = getCached(new Color4I(0, 0, 255));
		CYAN = getCached(new Color4I(0, 255, 255));
		GRAY = getCached(new Color4I(128, 128, 128));
		GREEN = getCached(new Color4I(0, 255, 0));
		MAGENTA = getCached(new Color4I(255, 0, 255));
		RED = getCached(new Color4I(255, 0, 0));
		TRANSPARENT = getCached(new Color4I(0, 0, 0, 0));
		WHITE = getCached(new Color4I(255, 255, 255));
		YELLOW = getCached(new Color4I(255, 255, 0));
		
		BLACK_A_R_G_B = toIntARGB(0, 0, 0, 255);
		BLUE_A_R_G_B = toIntARGB(0, 0, 255, 255);
		CYAN_A_R_G_B = toIntARGB(0, 255, 255, 255);
		GRAY_A_R_G_B = toIntARGB(128, 128, 128, 255);
		GREEN_A_R_G_B = toIntARGB(0, 255, 0, 255);
		MAGENTA_A_R_G_B = toIntARGB(255, 0, 255, 255);
		RED_A_R_G_B = toIntARGB(255, 0, 0, 255);
		TRANSPARENT_A_R_G_B = toIntARGB(0, 0, 0, 0);
		WHITE_A_R_G_B = toIntARGB(255, 255, 255, 255);
		YELLOW_A_R_G_B = toIntARGB(255, 255, 0, 255);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The value of the alpha component.
	 */
	public final int a;
	
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
	 * Constructs a new {@code Color4I} instance that represents black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(0);
	 * }
	 * </pre>
	 */
	public Color4I() {
		this(0);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3D color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3D color, final double a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3F color, final float a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color3I color, final int a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color4D color) {
		this(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4I(final Color4F color) {
		this(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color4I(final double grayscale) {
		this(grayscale, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 * @param a the value of the alpha component
	 */
	public Color4I(final double grayscale, final double a) {
		this(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(r, g, b, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color4I(final double r, final double g, final double b) {
		this(r, g, b, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I((int)(r * 255.0D + 0.5D), (int)(g * 255.0D + 0.5D), (int)(b * 255.0D + 0.5D), (int)(a * 255.0D + 0.5D));
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 */
	public Color4I(final double r, final double g, final double b, final double a) {
		this((int)(r * 255.0D + 0.5D), (int)(g * 255.0D + 0.5D), (int)(b * 255.0D + 0.5D), (int)(a * 255.0D + 0.5D));
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color4I(final float grayscale) {
		this(grayscale, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 * @param a the value of the alpha component
	 */
	public Color4I(final float grayscale, final float a) {
		this(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(r, g, b, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color4I(final float r, final float g, final float b) {
		this(r, g, b, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I((int)(r * 255.0F + 0.5F), (int)(g * 255.0F + 0.5F), (int)(b * 255.0F + 0.5F), (int)(a * 255.0F + 0.5F));
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 */
	public Color4I(final float r, final float g, final float b, final float a) {
		this((int)(r * 255.0F + 0.5F), (int)(g * 255.0F + 0.5F), (int)(b * 255.0F + 0.5F), (int)(a * 255.0F + 0.5F));
	}
	
	/**
	 * Constructs a new {@code Color4I} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, 255);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color4I(final int grayscale) {
		this(grayscale, 255);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 * @param a the value of the alpha component
	 */
	public Color4I(final int grayscale, final int a) {
		this(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(r, g, b, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color4I(final int r, final int g, final int b) {
		this(r, g, b, 255);
	}
	
	/**
	 * Constructs a new {@code Color4I} instance.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 */
	public Color4I(final int r, final int g, final int b, final int a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color4I} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color4I} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color4I(%s, %s, %s, %s)", Integer.toString(this.r), Integer.toString(this.g), Integer.toString(this.b), Integer.toString(this.a));
	}
	
	/**
	 * Compares {@code color} to this {@code Color4I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is equal to this {@code Color4I} instance, {@code false} otherwise.
	 * 
	 * @param color the {@code Color3I} to compare to this {@code Color4I} instance for equality
	 * @return {@code true} if, and only if, {@code color} is equal to this {@code Color4I} instance, {@code false} otherwise
	 */
	public boolean equals(final Color4I color) {
		if(color == this) {
			return true;
		} else if(color == null) {
			return false;
		} else if(this.a != color.a) {
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
	 * Compares {@code object} to this {@code Color4I} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color4I}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color4I} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color4I}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color4I)) {
			return false;
		} else {
			return equals(Color4I.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is black, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return this.r == 0 && this.g == 0 && this.b == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color4I} instance {@code color} is blue if, and only if, {@code color.b - deltaR >= color.r} and {@code color.b - deltaG >= color.g}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, this {@code Color4I} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue(final int deltaR, final int deltaG) {
		return this.b - deltaR >= this.r && this.b - deltaG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return this.g == this.b && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is grayscale, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is grayscale, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return this.r == this.g && this.g == this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is green, {@code false} otherwise.
	 * <p>
	 * The {@code Color4I} instance {@code color} is green if, and only if, {@code color.g - deltaR >= color.r} and {@code color.g - deltaB >= color.b}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color4I} instance is green, {@code false} otherwise
	 */
	public boolean isGreen(final int deltaR, final int deltaB) {
		return this.g - deltaR >= this.r && this.g - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return this.r == this.b && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(255, 255);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(255, 255);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is red, {@code false} otherwise.
	 * <p>
	 * The {@code Color4I} instance {@code color} is red if, and only if, {@code color.r - deltaG >= color.g} and {@code color.r - deltaB >= color.b}.
	 * 
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color4I} instance is red, {@code false} otherwise
	 */
	public boolean isRed(final int deltaG, final int deltaB) {
		return this.r - deltaG >= this.g && this.r - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is white, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return this.r == 255 && this.g == 255 && this.b == 255;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4I} instance is yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4I} instance is yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return this.r == this.g && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the average component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int average() {
		return (this.r + this.g + this.b) / 3;
	}
	
	/**
	 * Returns the lightness for this {@code Color4I} instance.
	 * 
	 * @return the lightness for this {@code Color4I} instance
	 */
	public int lightness() {
		return (max() + min()) / 2;
	}
	
	/**
	 * Returns the largest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the largest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int max() {
		return Ints.max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the smallest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public int min() {
		return Ints.min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the relative luminance for this {@code Color4I} instance.
	 * 
	 * @return the relative luminance for this {@code Color4I} instance
	 */
	public int relativeLuminance() {
		return (int)(this.r * 0.212671D + this.g * 0.715160D + this.b * 0.072169D);
	}
	
	/**
	 * Returns a hash code for this {@code Color4I} instance.
	 * 
	 * @return a hash code for this {@code Color4I} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Integer.valueOf(this.a), Integer.valueOf(this.b), Integer.valueOf(this.g), Integer.valueOf(this.r));
	}
	
	/**
	 * Returns the alpha component as an {@code int}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Ints.saturate(color.a);
	 * }
	 * </pre>
	 * 
	 * @return the alpha component as an {@code int}
	 */
	public int toIntA() {
		return Ints.saturate(this.a);
	}
	
	/**
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * 
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public int toIntARGB() {
		return toIntARGB(this.r, this.g, this.b, this.a);
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
	 * Writes this {@code Color4I} instance to {@code dataOutput}.
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
			dataOutput.writeInt(this.a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the red, green and blue component values of {@code colorRHS} to the red, green and blue component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The alpha component of {@code colorLHS} will be used.
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param colorRHS the {@code Color4I} instance on the right-hand side
	 * @return a new {@code Color4I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4I add(final Color4I colorLHS, final Color4I colorRHS) {
		final int r = colorLHS.r + colorRHS.r;
		final int g = colorLHS.g + colorRHS.g;
		final int b = colorLHS.b + colorRHS.b;
		final int a = colorLHS.a;
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Adds {@code scalarRHS} to the red, green and blue component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the addition.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The alpha component of {@code colorLHS} will be used.
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color4I} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color4I add(final Color4I colorLHS, final int scalarRHS) {
		final int r = colorLHS.r + scalarRHS;
		final int g = colorLHS.g + scalarRHS;
		final int b = colorLHS.b + scalarRHS;
		final int a = colorLHS.a;
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blend(colorLHS, colorRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param colorRHS the {@code Color4I} instance on the right-hand side
	 * @return a new {@code Color4I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4I blend(final Color4I colorLHS, final Color4I colorRHS) {
		return blend(colorLHS, colorRHS, 0.5D);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blend(Color4I.blend(color11, color12, tX), Color4I.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color4I} instance on row 1 and column 1
	 * @param color12 the {@code Color4I} instance on row 1 and column 2
	 * @param color21 the {@code Color4I} instance on row 2 and column 1
	 * @param color22 the {@code Color4I} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color4I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
	public static Color4I blend(final Color4I color11, final Color4I color12, final Color4I color21, final Color4I color22, final double tX, final double tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blend(colorLHS, colorRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param colorRHS the {@code Color4I} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color4I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4I blend(final Color4I colorLHS, final Color4I colorRHS, final double t) {
		return blend(colorLHS, colorRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param colorRHS the {@code Color4I} instance on the right-hand side
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return a new {@code Color4I} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4I blend(final Color4I colorLHS, final Color4I colorRHS, final double tR, final double tG, final double tB, final double tA) {
		final int r = Ints.lerp(colorLHS.r, colorRHS.r, tR);
		final int g = Ints.lerp(colorLHS.g, colorRHS.g, tG);
		final int b = Ints.lerp(colorLHS.b, colorRHS.b, tB);
		final int a = Ints.lerp(colorLHS.a, colorRHS.a, tA);
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4I} instance from the {@code int} {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains the alpha, red, green and blue components
	 * @return a {@code Color4I} instance from the {@code int} {@code colorARGB}
	 */
	public static Color4I fromIntARGB(final int colorARGB) {
		final int a = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_A) & 0xFF;
		final int r = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4I} instance from the {@code int} {@code colorRGB}.
	 * 
	 * @param colorRGB an {@code int} that contains the red, green and blue components
	 * @return a {@code Color4I} instance from the {@code int} {@code colorRGB}
	 */
	public static Color4I fromIntRGB(final int colorRGB) {
		final int r = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color4I(r, g, b);
	}
	
	/**
	 * Returns a cached {@code Color4I} instance that is equal to {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a cached {@code Color4I} instance that is equal to {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I getCached(final Color4I color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.a}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleA(final Color4I color) {
		return new Color4I(color.a, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleAverage(final Color4I color) {
		return new Color4I(color.average(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.b}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.b}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleB(final Color4I color) {
		return new Color4I(color.b, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.g}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.g}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleG(final Color4I color) {
		return new Color4I(color.g, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleLightness(final Color4I color) {
		return new Color4I(color.lightness(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.max()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.max()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleMax(final Color4I color) {
		return new Color4I(color.max(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.min()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.min()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleMin(final Color4I color) {
		return new Color4I(color.min(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.r}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.r}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleR(final Color4I color) {
		return new Color4I(color.r, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4I} instance based on {@code color.relativeLuminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a grayscale {@code Color4I} instance based on {@code color.relativeLuminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I grayscaleRelativeLuminance(final Color4I color) {
		return new Color4I(color.relativeLuminance(), color.a);
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a new {@code Color4I} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I invert(final Color4I color) {
		return new Color4I(255 - color.r, 255 - color.g, 255 - color.b, color.a);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4I} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color4I} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color4I multiply(final Color4I colorLHS, final int scalarRHS) {
		final int r = colorLHS.r * scalarRHS;
		final int g = colorLHS.g * scalarRHS;
		final int b = colorLHS.b * scalarRHS;
		final int a = colorLHS.a;
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4I(Randoms.nextInt(256), Randoms.nextInt(256), Randoms.nextInt(256));
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values
	 */
	public static Color4I random() {
		return new Color4I(Randoms.nextInt(256), Randoms.nextInt(256), Randoms.nextInt(256));
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a blue color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomBlue(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a blue color
	 */
	public static Color4I randomBlue() {
		return randomBlue(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a blue color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color4I} instance with random component values that represents a blue color
	 */
	public static Color4I randomBlue(final int maxR, final int maxG) {
		final int b = Randoms.nextInt(1, 256);
		final int r = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, b));
		final int g = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, b));
		
		return new Color4I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a cyan color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomCyan(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a cyan color
	 */
	public static Color4I randomCyan() {
		return randomCyan(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a cyan color.
	 * 
	 * @param minGB the minimum value to use for the G- and B-components
	 * @param maxR the maximum value to use for the R-component
	 * @return a {@code Color4I} instance with random component values that represents a cyan color
	 */
	public static Color4I randomCyan(final int minGB, final int maxR) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minGB, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, x));
		
		return new Color4I(y, x, x);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a grayscale color.
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a grayscale color
	 */
	public static Color4I randomGrayscale() {
		return new Color4I(Randoms.nextInt(256));
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a green color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomGreen(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a green color
	 */
	public static Color4I randomGreen() {
		return randomGreen(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a green color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4I} instance with random component values that represents a green color
	 */
	public static Color4I randomGreen(final int maxR, final int maxB) {
		final int g = Randoms.nextInt(1, 256);
		final int r = Randoms.nextInt(0, Ints.min(Ints.max(maxR, 0) + 1, g));
		final int b = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, g));
		
		return new Color4I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a magenta color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomMagenta(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a magenta color
	 */
	public static Color4I randomMagenta() {
		return randomMagenta(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a magenta color.
	 * 
	 * @param minRB the minimum value to use for the R- and B-components
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color4I} instance with random component values that represents a magenta color
	 */
	public static Color4I randomMagenta(final int minRB, final int maxG) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minRB, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, x));
		
		return new Color4I(x, y, x);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a red color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomRed(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a red color
	 */
	public static Color4I randomRed() {
		return randomRed(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a red color.
	 * 
	 * @param maxG the maximum value to use for the G-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4I} instance with random component values that represents a red color
	 */
	public static Color4I randomRed(final int maxG, final int maxB) {
		final int r = Randoms.nextInt(1, 256);
		final int g = Randoms.nextInt(0, Ints.min(Ints.max(maxG, 0) + 1, r));
		final int b = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, r));
		
		return new Color4I(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a yellow color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.randomYellow(0, 0);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4I} instance with random component values that represents a yellow color
	 */
	public static Color4I randomYellow() {
		return randomYellow(0, 0);
	}
	
	/**
	 * Returns a {@code Color4I} instance with random component values that represents a yellow color.
	 * 
	 * @param minRG the minimum value to use for the R- and G-components
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4I} instance with random component values that represents a yellow color
	 */
	public static Color4I randomYellow(final int minRG, final int maxB) {
		final int x = Randoms.nextInt(Ints.max(Ints.min(minRG, 255), 1), 256);
		final int y = Randoms.nextInt(0, Ints.min(Ints.max(maxB, 0) + 1, x));
		
		return new Color4I(x, x, y);
	}
	
	/**
	 * Returns a new {@code Color4I} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color4I} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Color4I read(final DataInput dataInput) {
		try {
			final int r = dataInput.readInt();
			final int g = dataInput.readInt();
			final int b = dataInput.readInt();
			final int a = dataInput.readInt();
			
			return new Color4I(r, g, b, a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [0, 255]}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.saturate(color, 0, 255);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a new {@code Color4I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I saturate(final Color4I color) {
		return saturate(color, 0, 255);
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [Ints.min(componentMinMax, componentMaxMin), Ints.max(componentMinMax, componentMaxMin)]}.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @param componentMinMax the minimum or maximum component value
	 * @param componentMaxMin the maximum or minimum component value
	 * @return a new {@code Color4I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I saturate(final Color4I color, final int componentMinMax, final int componentMaxMin) {
		final int r = Ints.saturate(color.r, componentMinMax, componentMaxMin);
		final int g = Ints.saturate(color.g, componentMinMax, componentMaxMin);
		final int b = Ints.saturate(color.b, componentMinMax, componentMaxMin);
		final int a = Ints.saturate(color.a, componentMinMax, componentMaxMin);
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color4I} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4I} instance
	 * @return a new {@code Color4I} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4I sepia(final Color4I color) {
		final int r = (int)(color.r * 0.393D + color.g * 0.769D + color.b * 0.189D);
		final int g = (int)(color.r * 0.349D + color.g * 0.686D + color.b * 0.168D);
		final int b = (int)(color.r * 0.272D + color.g * 0.534D + color.b * 0.131D);
		final int a =       color.a;
		
		return new Color4I(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4I[]} with a length of {@code length} and contains {@code Color4I.TRANSPARENT}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.array(length, index -> Color4I.TRANSPARENT);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4I[]} with a length of {@code length} and contains {@code Color4I.TRANSPARENT}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color4I[] array(final int length) {
		return array(length, index -> TRANSPARENT);
	}
	
	/**
	 * Returns a {@code Color4I[]} with a length of {@code length} and contains {@code Color4I} instances produced by {@code function}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code function} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param function an {@code IntFunction}
	 * @return a {@code Color4I[]} with a length of {@code length} and contains {@code Color4I} instances produced by {@code function}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or returns {@code null}
	 */
	public static Color4I[] array(final int length, final IntFunction<Color4I> function) {
		final Color4I[] colors = new Color4I[Ints.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(function, "function == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(function.apply(i));
		}
		
		return colors;
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blendARGB(colorARGBLHS, colorARGBRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorARGBLHS an {@code int} that represents a color in the format ARGB
	 * @param colorARGBRHS an {@code int} that represents a color in the format ARGB
	 * @param t the factor to use for all components in the blending process
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGBLHS, final int colorARGBRHS, final double t) {
		return blendARGB(colorARGBLHS, colorARGBRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * 
	 * @param colorARGBLHS an {@code int} that represents a color in the format ARGB
	 * @param colorARGBRHS an {@code int} that represents a color in the format ARGB
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGBLHS, final int colorARGBRHS, final double tR, final double tG, final double tB, final double tA) {
		final int r = Ints.saturateAndScaleToInt(Doubles.lerp(Color4D.fromIntARGBToDoubleR(colorARGBLHS), Color4D.fromIntARGBToDoubleR(colorARGBRHS), tR));
		final int g = Ints.saturateAndScaleToInt(Doubles.lerp(Color4D.fromIntARGBToDoubleG(colorARGBLHS), Color4D.fromIntARGBToDoubleG(colorARGBRHS), tG));
		final int b = Ints.saturateAndScaleToInt(Doubles.lerp(Color4D.fromIntARGBToDoubleB(colorARGBLHS), Color4D.fromIntARGBToDoubleB(colorARGBRHS), tB));
		final int a = Ints.saturateAndScaleToInt(Doubles.lerp(Color4D.fromIntARGBToDoubleA(colorARGBLHS), Color4D.fromIntARGBToDoubleA(colorARGBRHS), tA));
		
		return toIntARGB(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blendARGB(colorARGBLHS, colorARGBRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorARGBLHS an {@code int} that represents a color in the format ARGB
	 * @param colorARGBRHS an {@code int} that represents a color in the format ARGB
	 * @param t the factor to use for all components in the blending process
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGBLHS, final int colorARGBRHS, final float t) {
		return blendARGB(colorARGBLHS, colorARGBRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorARGBLHS} and {@code colorARGBRHS}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * 
	 * @param colorARGBLHS an {@code int} that represents a color in the format ARGB
	 * @param colorARGBRHS an {@code int} that represents a color in the format ARGB
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGBLHS, final int colorARGBRHS, final float tR, final float tG, final float tB, final float tA) {
		final int r = Ints.saturateAndScaleToInt(Floats.lerp(Color4F.fromIntARGBToFloatR(colorARGBLHS), Color4F.fromIntARGBToFloatR(colorARGBRHS), tR));
		final int g = Ints.saturateAndScaleToInt(Floats.lerp(Color4F.fromIntARGBToFloatG(colorARGBLHS), Color4F.fromIntARGBToFloatG(colorARGBRHS), tG));
		final int b = Ints.saturateAndScaleToInt(Floats.lerp(Color4F.fromIntARGBToFloatB(colorARGBLHS), Color4F.fromIntARGBToFloatB(colorARGBRHS), tB));
		final int a = Ints.saturateAndScaleToInt(Floats.lerp(Color4F.fromIntARGBToFloatA(colorARGBLHS), Color4F.fromIntARGBToFloatA(colorARGBRHS), tA));
		
		return toIntARGB(r, g, b, a);
	}
	
	/**
	 * Blends the components values of {@code colorARGB11}, {@code colorARGB12}, {@code colorARGB21} and {@code colorARGB22}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blendARGB(Color4I.blendARGB(colorARGB11, colorARGB12, tX), Color4I.blendARGB(colorARGB21, colorARGB22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param colorARGB11 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB12 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB21 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB22 an {@code int} that represents a color in the format ARGB
	 * @param tX the factor to use for all components in the first two blend operations
	 * @param tY the factor to use for all components in the third blend operation
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGB11, final int colorARGB12, final int colorARGB21, final int colorARGB22, final double tX, final double tY) {
		return blendARGB(blendARGB(colorARGB11, colorARGB12, tX), blendARGB(colorARGB21, colorARGB22, tX), tY);
	}
	
	/**
	 * Blends the components values of {@code colorARGB11}, {@code colorARGB12}, {@code colorARGB21} and {@code colorARGB22}.
	 * <p>
	 * Returns an {@code int} that represents a color in the format ARGB and contains the result of the blend.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4I.blendARGB(Color4I.blendARGB(colorARGB11, colorARGB12, tX), Color4I.blendARGB(colorARGB21, colorARGB22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param colorARGB11 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB12 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB21 an {@code int} that represents a color in the format ARGB
	 * @param colorARGB22 an {@code int} that represents a color in the format ARGB
	 * @param tX the factor to use for all components in the first two blend operations
	 * @param tY the factor to use for all components in the third blend operation
	 * @return an {@code int} that represents a color in the format ARGB and contains the result of the blend
	 */
	public static int blendARGB(final int colorARGB11, final int colorARGB12, final int colorARGB21, final int colorARGB22, final float tX, final float tY) {
		return blendARGB(blendARGB(colorARGB11, colorARGB12, tX), blendARGB(colorARGB21, colorARGB22, tX), tY);
	}
	
	/**
	 * Returns the value of the A-component in {@code colorARGB} as an {@code int}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the A-component in {@code colorARGB} as an {@code int}
	 */
	public static int fromIntARGBToIntA(final int colorARGB) {
		return (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_A) & 0xFF;
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
	 * Returns the size of the cache.
	 * 
	 * @return the size of the cache
	 */
	public static int getCacheSize() {
		return CACHE.size();
	}
	
	/**
	 * Converts {@code colorARGB} to its Sepia-representation.
	 * <p>
	 * Returns an {@code int} that contains the color in packed form.
	 * 
	 * @param colorARGB an {@code int} that contains packed A-, R-, G- and B-components
	 * @return an {@code int} that contains the color in packed form
	 */
	public static int sepiaARGB(final int colorARGB) {
		final int colorAR = fromIntARGBToIntR(colorARGB);
		final int colorAG = fromIntARGBToIntG(colorARGB);
		final int colorAB = fromIntARGBToIntB(colorARGB);
		final int colorAA = fromIntARGBToIntA(colorARGB);
		
		final int colorBR = (int)(colorAR * 0.393D + colorAG * 0.769D + colorAB * 0.189D);
		final int colorBG = (int)(colorAR * 0.349D + colorAG * 0.686D + colorAB * 0.168D);
		final int colorBB = (int)(colorAR * 0.272D + colorAG * 0.534D + colorAB * 0.131D);
		final int colorBA =       colorAA;
		
		return toIntARGB(colorBR, colorBG, colorBB, colorBA);
	}
	
	/**
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public static int toIntARGB(final int r, final int g, final int b, final int a) {
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