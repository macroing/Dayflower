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

import org.macroing.java.lang.Floats;
import org.macroing.java.lang.Ints;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;

/**
 * A {@code Color4F} represents a color with four {@code float}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color4F {
	/**
	 * A {@code Color4F} instance that represents the color black.
	 */
	public static final Color4F BLACK;
	
	/**
	 * A {@code Color4F} instance that represents the color blue.
	 */
	public static final Color4F BLUE;
	
	/**
	 * A {@code Color4F} instance that represents the color cyan.
	 */
	public static final Color4F CYAN;
	
	/**
	 * A {@code Color4F} instance that represents the color gray.
	 */
	public static final Color4F GRAY;
	
	/**
	 * A {@code Color4F} instance that represents the color green.
	 */
	public static final Color4F GREEN;
	
	/**
	 * A {@code Color4F} instance that represents the color magenta.
	 */
	public static final Color4F MAGENTA;
	
	/**
	 * A {@code Color4F} instance that represents the color red.
	 */
	public static final Color4F RED;
	
	/**
	 * A {@code Color4F} instance that represents the color transparent.
	 */
	public static final Color4F TRANSPARENT;
	
	/**
	 * A {@code Color4F} instance that represents the color white.
	 */
	public static final Color4F WHITE;
	
	/**
	 * A {@code Color4F} instance that represents the color yellow.
	 */
	public static final Color4F YELLOW;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color4F, Color4F> CACHE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		CACHE = new HashMap<>();
		
		BLACK = getCached(new Color4F(0.0F, 0.0F, 0.0F));
		BLUE = getCached(new Color4F(0.0F, 0.0F, 1.0F));
		CYAN = getCached(new Color4F(0.0F, 1.0F, 1.0F));
		GRAY = getCached(new Color4F(0.5F, 0.5F, 0.5F));
		GREEN = getCached(new Color4F(0.0F, 1.0F, 0.0F));
		MAGENTA = getCached(new Color4F(1.0F, 0.0F, 1.0F));
		RED = getCached(new Color4F(1.0F, 0.0F, 0.0F));
		TRANSPARENT = getCached(new Color4F(0.0F, 0.0F, 0.0F, 0.0F));
		WHITE = getCached(new Color4F(1.0F, 1.0F, 1.0F));
		YELLOW = getCached(new Color4F(1.0F, 1.0F, 0.0F));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The value of the alpha component.
	 */
	public final float a;
	
	/**
	 * The value of the blue component.
	 */
	public final float b;
	
	/**
	 * The value of the green component.
	 */
	public final float g;
	
	/**
	 * The value of the red component.
	 */
	public final float r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color4F} instance that represents black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(0.0F);
	 * }
	 * </pre>
	 */
	public Color4F() {
		this(0.0F);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3D color) {
		this((float)(color.r), (float)(color.g), (float)(color.b));
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3D color, final float a) {
		this((float)(color.r), (float)(color.g), (float)(color.b), a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3F color, final float a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @param a the value of the alpha component
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3I color, final int a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color4D color) {
		this((float)(color.r), (float)(color.g), (float)(color.b), (float)(color.a));
	}
	
	/**
	 * Constructs a new {@code Color4F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color4I color) {
		this(color.r, color.g, color.b, color.a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(grayscale, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color4F(final float grayscale) {
		this(grayscale, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 * @param a the value of the alpha component
	 */
	public Color4F(final float grayscale, final float a) {
		this(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(r, g, b, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color4F(final float r, final float g, final float b) {
		this(r, g, b, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 */
	public Color4F(final float r, final float g, final float b, final float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Constructs a new {@code Color4F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(grayscale, 255);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color4F(final int grayscale) {
		this(grayscale, 255);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(grayscale, grayscale, grayscale, a);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 * @param a the value of the alpha component
	 */
	public Color4F(final int grayscale, final int a) {
		this(grayscale, grayscale, grayscale, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(r, g, b, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color4F(final int r, final int g, final int b) {
		this(r, g, b, 255);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @param a the value of the alpha component
	 */
	public Color4F(final int r, final int g, final int b, final int a) {
		this(r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color4F(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.r), Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.b), Strings.toNonScientificNotationJava(this.a));
	}
	
	/**
	 * Compares {@code color} to this {@code Color4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is equal to this {@code Color4F} instance, {@code false} otherwise.
	 * 
	 * @param color the {@code Color4F} to compare to this {@code Color4F} instance for equality
	 * @return {@code true} if, and only if, {@code color} is equal to this {@code Color4F} instance, {@code false} otherwise
	 */
	public boolean equals(final Color4F color) {
		if(color == this) {
			return true;
		} else if(color == null) {
			return false;
		} else if(!Floats.equals(this.a, color.a)) {
			return false;
		} else if(!Floats.equals(this.b, color.b)) {
			return false;
		} else if(!Floats.equals(this.g, color.g)) {
			return false;
		} else if(!Floats.equals(this.r, color.r)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Color4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color4F}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color4F}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color4F)) {
			return false;
		} else {
			return equals(Color4F.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return Floats.isZero(this.r) && Floats.isZero(this.g) && Floats.isZero(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is blue if, and only if, {@code color.b - deltaR >= color.r} and {@code color.b - deltaG >= color.g}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue(final float deltaR, final float deltaG) {
		return this.b - deltaR >= this.r && this.b - deltaG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return Floats.equals(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is grayscale, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is grayscale, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return Floats.equals(this.r, this.g) && Floats.equals(this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is green, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is green if, and only if, {@code color.g - deltaR >= color.r} and {@code color.g - deltaB >= color.b}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is green, {@code false} otherwise
	 */
	public boolean isGreen(final float deltaR, final float deltaB) {
		return this.g - deltaR >= this.r && this.g - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return Floats.equals(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is red, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is red if, and only if, {@code color.r - deltaG >= color.g} and {@code color.r - deltaB >= color.b}.
	 * 
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is red, {@code false} otherwise
	 */
	public boolean isRed(final float deltaG, final float deltaB) {
		return this.r - deltaG >= this.g && this.r - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return Floats.equals(this.r, 1.0F) && Floats.equals(this.g, 1.0F) && Floats.equals(this.b, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return Floats.equals(this.r, this.g) && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the average component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public float average() {
		return (this.r + this.g + this.b) / 3.0F; 
	}
	
	/**
	 * Returns the lightness for this {@code Color4F} instance.
	 * 
	 * @return the lightness for this {@code Color4F} instance
	 */
	public float lightness() {
		return (max() + min()) / 2.0F;
	}
	
	/**
	 * Returns the largest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the largest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public float max() {
		return Floats.max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the smallest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public float min() {
		return Floats.min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the relative luminance for this {@code Color4F} instance.
	 * 
	 * @return the relative luminance for this {@code Color4F} instance
	 */
	public float relativeLuminance() {
		return this.r * 0.212671F + this.g * 0.715160F + this.b * 0.072169F;
	}
	
	/**
	 * Returns a hash code for this {@code Color4F} instance.
	 * 
	 * @return a hash code for this {@code Color4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.a), Float.valueOf(this.b), Float.valueOf(this.g), Float.valueOf(this.r));
	}
	
	/**
	 * Returns an {@code int} with the component values in a packed form.
	 * <p>
	 * This method assumes that the component values are within the range [0.0, 1.0]. Any component value outside of this range will be saturated or clamped.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color4F.pack(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} with the component values in a packed form
	 */
	public int pack() {
		return pack(PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns an {@code int} with the component values in a packed form.
	 * <p>
	 * This method assumes that the component values are within the range [0.0, 1.0]. Any component value outside of this range will be saturated or clamped.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param packedIntComponentOrder the {@link PackedIntComponentOrder} to pack the component values with
	 * @return an {@code int} with the component values in a packed form
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public int pack(final PackedIntComponentOrder packedIntComponentOrder) {
		return packedIntComponentOrder.pack(toIntR(), toIntG(), toIntB(), toIntA());
	}
	
	/**
	 * Returns the alpha component as an {@code int}.
	 * 
	 * @return the alpha component as an {@code int}
	 */
	public int toIntA() {
		return toIntA(this.a);
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
	 * 
	 * @return the blue component as an {@code int}
	 */
	public int toIntB() {
		return toIntB(this.b);
	}
	
	/**
	 * Returns the green component as an {@code int}.
	 * 
	 * @return the green component as an {@code int}
	 */
	public int toIntG() {
		return toIntG(this.g);
	}
	
	/**
	 * Returns the red component as an {@code int}.
	 * 
	 * @return the red component as an {@code int}
	 */
	public int toIntR() {
		return toIntR(this.r);
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
	 * Writes this {@code Color4F} instance to {@code dataOutput}.
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
			dataOutput.writeFloat(this.r);
			dataOutput.writeFloat(this.g);
			dataOutput.writeFloat(this.b);
			dataOutput.writeFloat(this.a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the red, green and blue component values of {@code colorRHS} to the red, green and blue component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The alpha component of {@code colorLHS} will be used.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4F add(final Color4F colorLHS, final Color4F colorRHS) {
		final float r = colorLHS.r + colorRHS.r;
		final float g = colorLHS.g + colorRHS.g;
		final float b = colorLHS.b + colorRHS.b;
		final float a = colorLHS.a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Adds {@code scalarRHS} to the red, green and blue component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * The alpha component of {@code colorLHS} will be used.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color4F add(final Color4F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r + scalarRHS;
		final float g = colorLHS.g + scalarRHS;
		final float b = colorLHS.b + scalarRHS;
		final float a = colorLHS.a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.blend(colorLHS, colorRHS, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4F} instance on the left-hand side
	 * @param colorRHS the {@code Color4F} instance on the right-hand side
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4F blend(final Color4F colorLHS, final Color4F colorRHS) {
		return blend(colorLHS, colorRHS, 0.5F);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.blend(Color4F.blend(color11, color12, tX), Color4F.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color4F} instance on row 1 and column 1
	 * @param color12 the {@code Color4F} instance on row 1 and column 2
	 * @param color21 the {@code Color4F} instance on row 2 and column 1
	 * @param color22 the {@code Color4F} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
	public static Color4F blend(final Color4F color11, final Color4F color12, final Color4F color21, final Color4F color22, final float tX, final float tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.blend(colorLHS, colorRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4F} instance on the left-hand side
	 * @param colorRHS the {@code Color4F} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4F blend(final Color4F colorLHS, final Color4F colorRHS, final float t) {
		return blend(colorLHS, colorRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4F} instance on the left-hand side
	 * @param colorRHS the {@code Color4F} instance on the right-hand side
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @param tA the factor to use for the A-component in the blending process
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4F blend(final Color4F colorLHS, final Color4F colorRHS, final float tR, final float tG, final float tB, final float tA) {
		final float r = Floats.lerp(colorLHS.r, colorRHS.r, tR);
		final float g = Floats.lerp(colorLHS.g, colorRHS.g, tG);
		final float b = Floats.lerp(colorLHS.b, colorRHS.b, tB);
		final float a = Floats.lerp(colorLHS.a, colorRHS.a, tA);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} over the component values of {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4F} instance on the left-hand side
	 * @param colorRHS the {@code Color4F} instance on the right-hand side
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color4F blendOver(final Color4F colorLHS, final Color4F colorRHS) {
		final float a = colorLHS.a + colorRHS.a * (1.0F - colorLHS.a);
		final float r = (colorLHS.r * colorLHS.a + colorRHS.r * colorRHS.a * (1.0F - colorLHS.a)) / a;
		final float g = (colorLHS.g * colorLHS.a + colorRHS.g * colorRHS.a * (1.0F - colorLHS.a)) / a;
		final float b = (colorLHS.b * colorLHS.a + colorRHS.b * colorRHS.a * (1.0F - colorLHS.a)) / a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance from the {@code int} {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains the alpha, red, green and blue components
	 * @return a {@code Color4F} instance from the {@code int} {@code colorARGB}
	 */
	public static Color4F fromIntARGB(final int colorARGB) {
		final int a = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_A) & 0xFF;
		final int r = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance from the {@code int} {@code colorRGB}.
	 * 
	 * @param colorRGB an {@code int} that contains the red, green and blue components
	 * @return a {@code Color4F} instance from the {@code int} {@code colorRGB}
	 */
	public static Color4F fromIntRGB(final int colorRGB) {
		final int r = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a cached {@code Color4F} instance that is equal to {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a cached {@code Color4F} instance that is equal to {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F getCached(final Color4F color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.a}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleA(final Color4F color) {
		return new Color4F(color.a, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleAverage(final Color4F color) {
		return new Color4F(color.average(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.b}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.b}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleB(final Color4F color) {
		return new Color4F(color.b, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.g}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.g}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleG(final Color4F color) {
		return new Color4F(color.g, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleLightness(final Color4F color) {
		return new Color4F(color.lightness(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.max()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.max()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleMax(final Color4F color) {
		return new Color4F(color.max(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.min()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.min()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleMin(final Color4F color) {
		return new Color4F(color.min(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.r}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.r}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleR(final Color4F color) {
		return new Color4F(color.r, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.relativeLuminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.relativeLuminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F grayscaleRelativeLuminance(final Color4F color) {
		return new Color4F(color.relativeLuminance(), color.a);
	}
	
	/**
	 * Inverts the red, green and blue component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F invert(final Color4F color) {
		return new Color4F(1.0F - color.r, 1.0F - color.g, 1.0F - color.b, color.a);
	}
	
	/**
	 * Multiplies the red, green and blue component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color4F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color4F multiply(final Color4F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r * scalarRHS;
		final float g = colorLHS.g * scalarRHS;
		final float b = colorLHS.b * scalarRHS;
		final float a = colorLHS.a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)));
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values
	 */
	public static Color4F random() {
		return new Color4F(Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)));
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a blue color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomBlue(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a blue color
	 */
	public static Color4F randomBlue() {
		return randomBlue(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a blue color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color4F} instance with random component values that represents a blue color
	 */
	public static Color4F randomBlue(final float maxR, final float maxG) {
		final float b = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float r = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), b));
		final float g = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), b));
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a cyan color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomCyan(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a cyan color
	 */
	public static Color4F randomCyan() {
		return randomCyan(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a cyan color.
	 * 
	 * @param minGB the minimum value to use for the G- and B-components
	 * @param maxR the maximum value to use for the R-component
	 * @return a {@code Color4F} instance with random component values that represents a cyan color
	 */
	public static Color4F randomCyan(final float minGB, final float maxR) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minGB, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), x));
		
		return new Color4F(y, x, x);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a grayscale color.
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a grayscale color
	 */
	public static Color4F randomGrayscale() {
		return new Color4F(Randoms.nextFloat(Floats.nextUp(1.0F)));
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a green color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomGreen(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a green color
	 */
	public static Color4F randomGreen() {
		return randomGreen(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a green color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4F} instance with random component values that represents a green color
	 */
	public static Color4F randomGreen(final float maxR, final float maxB) {
		final float g = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float r = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), g));
		final float b = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), g));
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a magenta color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomMagenta(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a magenta color
	 */
	public static Color4F randomMagenta() {
		return randomMagenta(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a magenta color.
	 * 
	 * @param minRB the minimum value to use for the R- and B-components
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color4F} instance with random component values that represents a magenta color
	 */
	public static Color4F randomMagenta(final float minRB, final float maxG) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minRB, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), x));
		
		return new Color4F(x, y, x);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a red color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomRed(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a red color
	 */
	public static Color4F randomRed() {
		return randomRed(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a red color.
	 * 
	 * @param maxG the maximum value to use for the G-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4F} instance with random component values that represents a red color
	 */
	public static Color4F randomRed(final float maxG, final float maxB) {
		final float r = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float g = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), r));
		final float b = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), r));
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a yellow color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.randomYellow(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values that represents a yellow color
	 */
	public static Color4F randomYellow() {
		return randomYellow(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values that represents a yellow color.
	 * 
	 * @param minRG the minimum value to use for the R- and G-components
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color4F} instance with random component values that represents a yellow color
	 */
	public static Color4F randomYellow(final float minRG, final float maxB) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minRG, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), x));
		
		return new Color4F(x, x, y);
	}
	
	/**
	 * Returns a new {@code Color4F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color4F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Color4F read(final DataInput dataInput) {
		try {
			final float r = dataInput.readFloat();
			final float g = dataInput.readFloat();
			final float b = dataInput.readFloat();
			final float a = dataInput.readFloat();
			
			return new Color4F(r, g, b, a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [0.0F, 1.0F]}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.saturate(color, 0.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F saturate(final Color4F color) {
		return saturate(color, 0.0F, 1.0F);
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [Floats.min(componentMinMax, componentMaxMin), Floats.max(componentMinMax, componentMaxMin)]}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @param componentMinMax the minimum or maximum component value
	 * @param componentMaxMin the maximum or minimum component value
	 * @return a new {@code Color4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F saturate(final Color4F color, final float componentMinMax, final float componentMaxMin) {
		final float r = Floats.saturate(color.r, componentMinMax, componentMaxMin);
		final float g = Floats.saturate(color.g, componentMinMax, componentMaxMin);
		final float b = Floats.saturate(color.b, componentMinMax, componentMaxMin);
		final float a = Floats.saturate(color.a, componentMinMax, componentMaxMin);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F sepia(final Color4F color) {
		final float r = color.r * 0.393F + color.g * 0.769F + color.b * 0.189F;
		final float g = color.r * 0.349F + color.g * 0.686F + color.b * 0.168F;
		final float b = color.r * 0.272F + color.g * 0.534F + color.b * 0.131F;
		final float a = color.a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a {@code Color4F} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color4F sqrt(final Color4F color) {
		final float r = Floats.sqrt(color.r);
		final float g = Floats.sqrt(color.g);
		final float b = Floats.sqrt(color.b);
		final float a = Floats.sqrt(color.a);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.unpack(color, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @return a {@code Color4F} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}
	 */
	public static Color4F unpack(final int color) {
		return unpack(color, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color4F} instance by unpacking {@code color} using {@code packedIntComponentOrder}.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @param packedIntComponentOrder the {@link PackedIntComponentOrder} to unpack the component values with
	 * @return a {@code Color4F} instance by unpacking {@code color} using {@code packedIntComponentOrder}
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public static Color4F unpack(final int color, final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = packedIntComponentOrder.unpackR(color);
		final int g = packedIntComponentOrder.unpackG(color);
		final int b = packedIntComponentOrder.unpackB(color);
		final int a = packedIntComponentOrder.unpackA(color);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F.TRANSPARENT}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.array(length, index -> Color4F.TRANSPARENT);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F.TRANSPARENT}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color4F[] array(final int length) {
		return array(length, index -> TRANSPARENT);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F} instances produced by {@code function}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code function} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param function an {@code IntFunction}
	 * @return a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F} instances produced by {@code function}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or returns {@code null}
	 */
	public static Color4F[] array(final int length, final IntFunction<Color4F> function) {
		final Color4F[] colors = new Color4F[Ints.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(function, "function == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(function.apply(i));
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code length} and contains random {@code Color4F} instances.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.array(length, index -> Color4F.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4F[]} with a length of {@code length} and contains random {@code Color4F} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color4F[] arrayRandom(final int length) {
		return array(length, index -> random());
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.arrayRead(array, ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color4F[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color4F[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color4F[] colors = new Color4F[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readRAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readGAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readBAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int a = arrayComponentOrder.readAAsInt(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color4F(r, g, b, a);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.arrayRead(array, ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color4F[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color4F[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color4F[] colors = new Color4F[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readR(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readG(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readB(array, i * arrayComponentOrder.getComponentCount());
			final int a = arrayComponentOrder.readA(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color4F(r, g, b, a);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length} and contains {@code Color4F} instances unpacked from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.arrayUnpack(array, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to unpack from
	 * @return a {@code Color4F[]} with a length of {@code array.length} and contains {@code Color4F} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color4F[] arrayUnpack(final int[] array) {
		return arrayUnpack(array, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length} and contains {@code Color4F} instances unpacked from {@code array}.
	 * <p>
	 * If either {@code array} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the array to unpack from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @return a {@code Color4F[]} with a length of {@code array.length} and contains {@code Color4F} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code packedIntComponentOrder} are {@code null}
	 */
	public static Color4F[] arrayUnpack(final int[] array, final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final Color4F[] colors = new Color4F[array.length];
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = unpack(array[i], packedIntComponentOrder);
		}
		
		return colors;
	}
	
	/**
	 * Returns the value of the A-component in {@code colorARGB} as a {@code float}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the A-component in {@code colorARGB} as a {@code float}
	 */
	public static float fromIntARGBToFloatA(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_A) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorARGB} as a {@code float}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the B-component in {@code colorARGB} as a {@code float}
	 */
	public static float fromIntARGBToFloatB(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorARGB} as a {@code float}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the G-component in {@code colorARGB} as a {@code float}
	 */
	public static float fromIntARGBToFloatG(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorARGB} as a {@code float}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the R-component in {@code colorARGB} as a {@code float}
	 */
	public static float fromIntARGBToFloatR(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorRGB} as a {@code float}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the B-component in {@code colorRGB} as a {@code float}
	 */
	public static float fromIntRGBToFloatB(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorRGB} as a {@code float}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the G-component in {@code colorRGB} as a {@code float}
	 */
	public static float fromIntRGBToFloatG(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF) / 255.0F;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorRGB} as a {@code float}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the R-component in {@code colorRGB} as a {@code float}
	 */
	public static float fromIntRGBToFloatR(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF) / 255.0F;
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
	 * Returns the alpha component as an {@code int}.
	 * 
	 * @param a the value of the alpha component
	 * @return the alpha component as an {@code int}
	 */
	public static int toIntA(final float a) {
		return Ints.saturateAndScaleToInt(a);
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
	public static int toIntARGB(final float r, final float g, final float b, final float a) {
		final int colorA = ((toIntA(a) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_A);
		final int colorR = ((toIntR(r) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_R);
		final int colorG = ((toIntG(g) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_G);
		final int colorB = ((toIntB(b) & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_B);
		
		return colorA | colorR | colorG | colorB;
	}
	
	/**
	 * Returns the blue component as an {@code int}.
	 * 
	 * @param b the value of the blue component
	 * @return the blue component as an {@code int}
	 */
	public static int toIntB(final float b) {
		return Ints.saturateAndScaleToInt(b);
	}
	
	/**
	 * Returns the green component as an {@code int}.
	 * 
	 * @param g the value of the green component
	 * @return the green component as an {@code int}
	 */
	public static int toIntG(final float g) {
		return Ints.saturateAndScaleToInt(g);
	}
	
	/**
	 * Returns the red component as an {@code int}.
	 * 
	 * @param r the value of the red component
	 * @return the red component as an {@code int}
	 */
	public static int toIntR(final float r) {
		return Ints.saturateAndScaleToInt(r);
	}
	
	/**
	 * Returns the red, green and blue components as an {@code int} in the format RGB.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the red, green and blue components as an {@code int} in the format RGB
	 */
	public static int toIntRGB(final float r, final float g, final float b) {
		final int colorR = ((toIntR(r) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_R);
		final int colorG = ((toIntG(g) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_G);
		final int colorB = ((toIntB(b) & 0xFF) << Utilities.COLOR_R_G_B_SHIFT_B);
		
		return colorR | colorG | colorB;
	}
	
	/**
	 * Clears the cache.
	 */
	public static void clearCache() {
		CACHE.clear();
	}
}