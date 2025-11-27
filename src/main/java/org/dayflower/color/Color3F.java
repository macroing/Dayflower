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
 * A {@code Color3F} represents a color with three {@code float}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color3F {
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for silver (Ag).
	 */
	public static final Color3F AG_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for silver (Ag).
	 */
	public static final Color3F AG_K;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for aluminum (Al).
	 */
	public static final Color3F AL_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for aluminum (Al).
	 */
	public static final Color3F AL_K;
	
	/**
	 * A {@code Color3F} denoting the color Aztek gold (Au).
	 */
	public static final Color3F AU_AZTEK;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for gold (Au).
	 */
	public static final Color3F AU_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for gold (Au).
	 */
	public static final Color3F AU_K;
	
	/**
	 * A {@code Color3F} denoting the color metallic gold (Au).
	 */
	public static final Color3F AU_METALLIC;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for beryllium (Be).
	 */
	public static final Color3F BE_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for beryllium (Be).
	 */
	public static final Color3F BE_K;
	
	/**
	 * A {@code Color3F} instance that represents the color black.
	 */
	public static final Color3F BLACK;
	
	/**
	 * A {@code Color3F} instance that represents the color blue.
	 */
	public static final Color3F BLUE;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for chromium (Cr).
	 */
	public static final Color3F CR_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for chromium (Cr).
	 */
	public static final Color3F CR_K;
	
	/**
	 * A {@code Color3F} denoting the color copper (Cu).
	 */
	public static final Color3F CU;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for copper (Cu).
	 */
	public static final Color3F CU_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for copper (Cu).
	 */
	public static final Color3F CU_K;
	
	/**
	 * A {@code Color3F} instance that represents the color cyan.
	 */
	public static final Color3F CYAN;
	
	/**
	 * A {@code Color3F} instance that represents the color gray.
	 */
	public static final Color3F GRAY;
	
	/**
	 * A {@code Color3F} instance that represents the color green.
	 */
	public static final Color3F GREEN;
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for mercury (Hg).
	 */
	public static final Color3F HG_ETA;
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for mercury (Hg).
	 */
	public static final Color3F HG_K;
	
	/**
	 * A {@code Color3F} instance that represents the color magenta.
	 */
	public static final Color3F MAGENTA;
	
	/**
	 * A {@code Color3F} instance that represents the color orange.
	 */
	public static final Color3F ORANGE;
	
	/**
	 * A {@code Color3F} instance that represents the color red.
	 */
	public static final Color3F RED;
	
	/**
	 * A {@code Color3F} instance that represents the color white.
	 */
	public static final Color3F WHITE;
	
	/**
	 * A {@code Color3F} instance that represents the color yellow.
	 */
	public static final Color3F YELLOW;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color3F, Color3F> CACHE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		CACHE = new HashMap<>();
		
		AG_ETA = getCached(new Color3F(0.15496026F, 0.116471656F, 0.13806625F));
		AG_K = getCached(new Color3F(4.818879F, 3.115515F, 2.1420743F));
		AL_ETA = getCached(new Color3F(1.6542087F, 0.8784699F, 0.52004325F));
		AL_K = getCached(new Color3F(9.205794F, 6.2560005F, 4.826011F));
		AU_AZTEK = getCached(new Color3F(0.76F, 0.6F, 0.33F));
		AU_ETA = getCached(new Color3F(0.14284283F, 0.37413108F, 1.4392234F));
		AU_K = getCached(new Color3F(3.9753616F, 2.3805823F, 1.599566F));
		AU_METALLIC = getCached(new Color3F(0.83F, 0.69F, 0.22F));
		BE_ETA = getCached(new Color3F(4.17685F, 3.178197F, 2.777768F));
		BE_K = getCached(new Color3F(3.827915F, 3.0036383F, 2.8624895F));
		BLACK = getCached(new Color3F(0.0F, 0.0F, 0.0F));
		BLUE = getCached(new Color3F(0.0F, 0.0F, 1.0F));
		CR_ETA = getCached(new Color3F(4.361113F, 2.910425F, 1.6509345F));
		CR_K = getCached(new Color3F(5.1962233F, 4.222245F, 3.746424F));
		CU = getCached(new Color3F(0.72F, 0.45F, 0.2F));
		CU_ETA = getCached(new Color3F(0.20002282F, 0.92205405F, 1.0997076F));
		CU_K = getCached(new Color3F(3.905267F, 2.4475532F, 2.1373255F));
		CYAN = getCached(new Color3F(0.0F, 1.0F, 1.0F));
		GRAY = getCached(new Color3F(0.5F, 0.5F, 0.5F));
		GREEN = getCached(new Color3F(0.0F, 1.0F, 0.0F));
		HG_ETA = getCached(new Color3F(2.3942256F, 1.4369211F, 0.90748405F));
		HG_K = getCached(new Color3F(6.315217F, 4.362519F, 3.414016F));
		MAGENTA = getCached(new Color3F(1.0F, 0.0F, 1.0F));
		ORANGE = getCached(new Color3F(1.0F, 0.5F, 0.0F));
		RED = getCached(new Color3F(1.0F, 0.0F, 0.0F));
		WHITE = getCached(new Color3F(1.0F, 1.0F, 1.0F));
		YELLOW = getCached(new Color3F(1.0F, 1.0F, 0.0F));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	 * Constructs a new {@code Color3F} instance that represents black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(0.0F);
	 * }
	 * </pre>
	 */
	public Color3F() {
		this(0.0F);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color3D color) {
		this((float)(color.r), (float)(color.g), (float)(color.b));
	}
	
	/**
	 * Constructs a new {@code Color3F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color3I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color4D color) {
		this((float)(color.r), (float)(color.g), (float)(color.b));
	}
	
	/**
	 * Constructs a new {@code Color3F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color4F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color4I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3F(final float grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3F(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Constructs a new {@code Color3F} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3F(final int grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(r / 255.0F, g / 255.0F, b / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3F(final int r, final int g, final int b) {
		this(r / 255.0F, g / 255.0F, b / 255.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color3F(%s, %s, %s)", Strings.toNonScientificNotationJava(this.r), Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.b));
	}
	
	/**
	 * Compares {@code color} to this {@code Color3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is equal to this {@code Color3F} instance, {@code false} otherwise.
	 * 
	 * @param color the {@code Color3F} to compare to this {@code Color3F} instance for equality
	 * @return {@code true} if, and only if, {@code color} is equal to this {@code Color3F} instance, {@code false} otherwise
	 */
	public boolean equals(final Color3F color) {
		if(color == this) {
			return true;
		} else if(color == null) {
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
	 * Compares {@code object} to this {@code Color3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color3F}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color3F}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color3F)) {
			return false;
		} else {
			return equals(Color3F.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return Floats.isInfinite(this.r) || Floats.isInfinite(this.g) || Floats.isInfinite(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return Floats.isNaN(this.r) || Floats.isNaN(this.g) || Floats.isNaN(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return Floats.isZero(this.r) && Floats.isZero(this.g) && Floats.isZero(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is blue if, and only if, {@code color.b - deltaR >= color.r} and {@code color.b - deltaG >= color.g}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue(final float deltaR, final float deltaG) {
		return this.b - deltaR >= this.r && this.b - deltaG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return Floats.equals(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is grayscale, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is grayscale, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return Floats.equals(this.r, this.g) && Floats.equals(this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is green, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is green if, and only if, {@code color.g - deltaR >= color.r} and {@code color.g - deltaB >= color.b}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is green, {@code false} otherwise
	 */
	public boolean isGreen(final float deltaR, final float deltaB) {
		return this.g - deltaR >= this.r && this.g - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return Floats.equals(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is red, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is red if, and only if, {@code color.r - deltaG >= color.g} and {@code color.r - deltaB >= color.b}.
	 * 
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is red, {@code false} otherwise
	 */
	public boolean isRed(final float deltaG, final float deltaB) {
		return this.r - deltaG >= this.g && this.r - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return Floats.equals(this.r, 1.0F) && Floats.equals(this.g, 1.0F) && Floats.equals(this.b, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is yellow, {@code false} otherwise
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
		return average(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the lightness for this {@code Color3F} instance.
	 * 
	 * @return the lightness for this {@code Color3F} instance
	 */
	public float lightness() {
		return lightness(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the largest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the largest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public float max() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the smallest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public float min() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the relative luminance for this {@code Color3F} instance.
	 * 
	 * @return the relative luminance for this {@code Color3F} instance
	 */
	public float relativeLuminance() {
		return relativeLuminance(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color3F} instance.
	 * 
	 * @return a hash code for this {@code Color3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.b), Float.valueOf(this.g), Float.valueOf(this.r));
	}
	
	/**
	 * Returns an {@code int} with the component values in a packed form.
	 * <p>
	 * This method assumes that the component values are within the range [0.0, 1.0]. Any component value outside of this range will be saturated or clamped.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color3F.pack(PackedIntComponentOrder.ARGB);
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
		return packedIntComponentOrder.pack(toIntR(), toIntG(), toIntB(), 255);
	}
	
	/**
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * <p>
	 * The alpha component is treated as if it was {@code 1.0F} or {@code 255}.
	 * 
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public int toIntARGB() {
		return toIntARGB(this.r, this.g, this.b);
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
	 * Writes this {@code Color3F} instance to {@code dataOutput}.
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
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F add(final Color3F colorLHS, final Color3F colorRHS) {
		final float r = colorLHS.r + colorRHS.r;
		final float g = colorLHS.g + colorRHS.g;
		final float b = colorLHS.b + colorRHS.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds {@code scalarRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3F add(final Color3F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r + scalarRHS;
		final float g = colorLHS.g + scalarRHS;
		final float b = colorLHS.b + scalarRHS;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the result of one multiplication to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorAdd the {@code Color3F} instance to add
	 * @param colorMultiplyA the {@code Color3F} instance used on the left-hand side of the multiplication
	 * @param colorMultiplyB the {@code Color3F} instance used on the right-hand side of the multiplication
	 * @return a new {@code Color3F} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3F addAndMultiply(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB) {
		final float r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r;
		final float g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g;
		final float b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the result of two multiplications to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorAdd the {@code Color3F} instance to add
	 * @param colorMultiplyA the {@code Color3F} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3F} instance used on the right-hand side of the first multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the second multiplication
	 * @return a new {@code Color3F} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3F addAndMultiply(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final float scalarMultiply) {
		final float r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * scalarMultiply;
		final float g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * scalarMultiply;
		final float b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * scalarMultiply;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the result of three multiplications followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA}, {@code colorMultiplyB} or {@code colorMultiplyC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3F.add(colorAdd, Color3F.divide(Color3F.multiply(Color3F.multiply(Color3F.multiply(colorMultiplyA, colorMultiplyB), colorMultiplyC), scalarMultiply), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3F} instance to add
	 * @param colorMultiplyA the {@code Color3F} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3F} instance used on the right-hand side of the first multiplication
	 * @param colorMultiplyC the {@code Color3F} instance used on the right-hand side of the second multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the third multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3F} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA}, {@code colorMultiplyB} or {@code colorMultiplyC} are {@code null}
	 */
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final Color3F colorMultiplyC, final float scalarMultiply, final float scalarDivide) {
		final float r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * colorMultiplyC.r * scalarMultiply / scalarDivide;
		final float g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * colorMultiplyC.g * scalarMultiply / scalarDivide;
		final float b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * colorMultiplyC.b * scalarMultiply / scalarDivide;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the result of one multiplication followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3F.add(colorAdd, Color3F.divide(Color3F.multiply(colorMultiplyA, colorMultiplyB), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3F} instance to add
	 * @param colorMultiplyA the {@code Color3F} instance used on the left-hand side of the multiplication
	 * @param colorMultiplyB the {@code Color3F} instance used on the right-hand side of the multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3F} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final float scalarDivide) {
		final float r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r / scalarDivide;
		final float g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g / scalarDivide;
		final float b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b / scalarDivide;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the result of two multiplications followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3F.add(colorAdd, Color3F.divide(Color3F.multiply(Color3F.multiply(colorMultiplyA, colorMultiplyB), scalarMultiply), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3F} instance to add
	 * @param colorMultiplyA the {@code Color3F} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3F} instance used on the right-hand side of the first multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the second multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3F} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final float scalarMultiply, final float scalarDivide) {
		final float r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * scalarMultiply / scalarDivide;
		final float g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * scalarMultiply / scalarDivide;
		final float b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * scalarMultiply / scalarDivide;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method differs from {@link #add(Color3F, Color3F)} in that it assumes {@code colorLHS} to be an average color sample. It uses a stable moving average algorithm to compute a new average color sample as a result of adding {@code colorRHS}. This method is suitable for Monte Carlo-method based algorithms.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @param sampleCount the current sample count
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F addSample(final Color3F colorLHS, final Color3F colorRHS, final int sampleCount) {
		final float r = colorLHS.r + ((colorRHS.r - colorLHS.r) / sampleCount);
		final float g = colorLHS.g + ((colorRHS.g - colorLHS.g) / sampleCount);
		final float b = colorLHS.b + ((colorRHS.b - colorLHS.b) / sampleCount);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.blend(colorLHS, colorRHS, 0.5F);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F blend(final Color3F colorLHS, final Color3F colorRHS) {
		return blend(colorLHS, colorRHS, 0.5F);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.blend(Color3F.blend(color11, color12, tX), Color3F.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color3F} instance on row 1 and column 1
	 * @param color12 the {@code Color3F} instance on row 1 and column 2
	 * @param color21 the {@code Color3F} instance on row 2 and column 1
	 * @param color22 the {@code Color3F} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color3F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
	public static Color3F blend(final Color3F color11, final Color3F color12, final Color3F color21, final Color3F color22, final float tX, final float tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.blend(colorLHS, colorRHS, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color3F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F blend(final Color3F colorLHS, final Color3F colorRHS, final float t) {
		return blend(colorLHS, colorRHS, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @return a new {@code Color3F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F blend(final Color3F colorLHS, final Color3F colorRHS, final float tR, final float tG, final float tB) {
		final float r = Floats.lerp(colorLHS.r, colorRHS.r, tR);
		final float g = Floats.lerp(colorLHS.g, colorRHS.g, tG);
		final float b = Floats.lerp(colorLHS.b, colorRHS.b, tB);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Divides the component values of {@code colorLHS} with the component values of {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the division.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F divide(final Color3F colorLHS, final Color3F colorRHS) {
		final float r = colorLHS.r / colorRHS.r;
		final float g = colorLHS.g / colorRHS.g;
		final float b = colorLHS.b / colorRHS.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Divides the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the division.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3F divide(final Color3F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r / scalarRHS;
		final float g = colorLHS.g / scalarRHS;
		final float b = colorLHS.b / scalarRHS;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance from the {@code int} {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains the alpha, red, green and blue components
	 * @return a {@code Color3F} instance from the {@code int} {@code colorARGB}
	 */
	public static Color3F fromIntARGB(final int colorARGB) {
		final int r = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance from the {@code int} {@code colorRGB}.
	 * 
	 * @param colorRGB an {@code int} that contains the red, green and blue components
	 * @return a {@code Color3F} instance from the {@code int} {@code colorRGB}
	 */
	public static Color3F fromIntRGB(final int colorRGB) {
		final int r = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a cached {@code Color3F} instance that is equal to {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a cached {@code Color3F} instance that is equal to {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F getCached(final Color3F color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleAverage(final Color3F color) {
		return new Color3F(color.average());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.b}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.b}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleB(final Color3F color) {
		return new Color3F(color.b);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.g}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.g}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleG(final Color3F color) {
		return new Color3F(color.g);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleLightness(final Color3F color) {
		return new Color3F(color.lightness());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.max()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.max()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleMax(final Color3F color) {
		return new Color3F(color.max());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.min()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.min()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleMin(final Color3F color) {
		return new Color3F(color.min());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.r}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.r}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleR(final Color3F color) {
		return new Color3F(color.r);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.relativeLuminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.luminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleRelativeLuminance(final Color3F color) {
		return new Color3F(color.relativeLuminance());
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F invert(final Color3F color) {
		return new Color3F(1.0F - color.r, 1.0F - color.g, 1.0F - color.b);
	}
	
	/**
	 * Returns a new {@code Color3F} instance with the largest component values of {@code colorA} and {@code colorB}.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the largest component values of {@code colorA} and {@code colorB}
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3F max(final Color3F colorA, final Color3F colorB) {
		final float r = Floats.max(colorA.r, colorB.r);
		final float g = Floats.max(colorA.g, colorB.g);
		final float b = Floats.max(colorA.b, colorB.b);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Restricts the component values of {@code color} by dividing them with the maximum component value that is greater than {@code 1.0}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the division, or {@code color} if no division occurred.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method can overflow if the delta between the minimum and maximum component values are large.
	 * <p>
	 * If at least one of the component values are negative, consider calling {@link #minTo0(Color3F)} before calling this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
	 * Color3F b = Color3F.maxTo1(a);
	 * 
	 * //a.r = 0.0F, a.g = 1.0F, a.b = 2.0F
	 * //b.r = 0.0F, b.g = 0.5F, b.b = 1.0F
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the division, or {@code color} if no division occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F maxTo1(final Color3F color) {
		final float max = color.max();
		
		if(max > 1.0F) {
			final float r = color.r / max;
			final float g = color.g / max;
			final float b = color.b / max;
			
			return new Color3F(r, g, b);
		}
		
		return color;
	}
	
	/**
	 * Returns a new {@code Color3F} instance with the smallest component values of {@code colorA} and {@code colorB}.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the smallest component values of {@code colorA} and {@code colorB}
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3F min(final Color3F colorA, final Color3F colorB) {
		final float r = Floats.min(colorA.r, colorB.r);
		final float g = Floats.min(colorA.g, colorB.g);
		final float b = Floats.min(colorA.b, colorB.b);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Restricts the component values of {@code color} by adding the minimum component value that is less than {@code 0.0} to them.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition, or {@code color} if no addition occurred.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method can overflow if the delta between the minimum and maximum component values are large.
	 * <p>
	 * Consider calling {@link #maxTo1(Color3F)} after a call to this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3F a = new Color3F(-2.0F, 0.0F, 1.0F);
	 * Color3F b = Color3F.minTo0(a);
	 * 
	 * //a.r = -2.0F, a.g = 0.0F, a.b = 1.0F
	 * //b.r =  0.0F, b.g = 2.0F, b.b = 3.0F
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the addition, or {@code color} if no addition occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F minTo0(final Color3F color) {
		final float min = color.min();
		
		if(min < 0.0F) {
			final float r = color.r + -min;
			final float g = color.g + -min;
			final float b = color.b + -min;
			
			return new Color3F(r, g, b);
		}
		
		return color;
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with the component values of {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F multiply(final Color3F colorLHS, final Color3F colorRHS) {
		final float r = colorLHS.r * colorRHS.r;
		final float g = colorLHS.g * colorRHS.g;
		final float b = colorLHS.b * colorRHS.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA}, {@code colorB} and {@code colorC}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @param colorC a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public static Color3F multiply(final Color3F colorA, final Color3F colorB, final Color3F colorC) {
		final float r = colorA.r * colorB.r * colorC.r;
		final float g = colorA.g * colorB.g * colorC.g;
		final float b = colorA.b * colorB.b * colorC.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA}, {@code colorB}, {@code colorC} and {@code colorD}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA}, {@code colorB}, {@code colorC} or {@code colorD} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @param colorC a {@code Color3F} instance
	 * @param colorD a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB}, {@code colorC} or {@code colorD} are {@code null}
	 */
	public static Color3F multiply(final Color3F colorA, final Color3F colorB, final Color3F colorC, final Color3F colorD) {
		final float r = colorA.r * colorB.r * colorC.r * colorD.r;
		final float g = colorA.g * colorB.g * colorC.g * colorD.g;
		final float b = colorA.b * colorB.b * colorC.b * colorD.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA} with the component values of {@code colorB} and its result with {@code scalarC}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @param scalarC a scalar value
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3F multiply(final Color3F colorA, final Color3F colorB, final float scalarC) {
		final float r = colorA.r * colorB.r * scalarC;
		final float g = colorA.g * colorB.g * scalarC;
		final float b = colorA.b * colorB.b * scalarC;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3F multiply(final Color3F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r * scalarRHS;
		final float g = colorLHS.g * scalarRHS;
		final float b = colorLHS.b * scalarRHS;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS} and saturates or clamps all negative component values.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3F multiplyAndSaturateNegative(final Color3F colorLHS, final float scalarRHS) {
		final float r = Floats.max(colorLHS.r * scalarRHS, 0.0F);
		final float g = Floats.max(colorLHS.g * scalarRHS, 0.0F);
		final float b = Floats.max(colorLHS.b * scalarRHS, 0.0F);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Negates the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the negation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F negate(final Color3F color) {
		final float r = -color.r;
		final float g = -color.g;
		final float b = -color.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Normalizes the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F normalize(final Color3F color) {
		final float sum = color.r + color.g + color.b;
		
		if(sum < 0.000001F) {
			return color;
		}
		
		final float sumReciprocal = 1.0F / sum;
		
		final float r = color.r * sumReciprocal;
		final float g = color.g * sumReciprocal;
		final float b = color.b * sumReciprocal;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Normalizes the component values of {@code color} based on its relative luminance.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F normalizeRelativeLuminance(final Color3F color) {
		final float relativeLuminance = color.relativeLuminance();
		
		if(relativeLuminance > 0.0F) {
			return divide(color, relativeLuminance);
		}
		
		return WHITE;
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)));
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values
	 */
	public static Color3F random() {
		return new Color3F(Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)), Randoms.nextFloat(Floats.nextUp(1.0F)));
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a blue color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomBlue(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a blue color
	 */
	public static Color3F randomBlue() {
		return randomBlue(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a blue color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3F} instance with random component values that represents a blue color
	 */
	public static Color3F randomBlue(final float maxR, final float maxG) {
		final float b = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float r = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), b));
		final float g = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), b));
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a cyan color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomCyan(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a cyan color
	 */
	public static Color3F randomCyan() {
		return randomCyan(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a cyan color.
	 * 
	 * @param minGB the minimum value to use for the G- and B-components
	 * @param maxR the maximum value to use for the R-component
	 * @return a {@code Color3F} instance with random component values that represents a cyan color
	 */
	public static Color3F randomCyan(final float minGB, final float maxR) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minGB, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), x));
		
		return new Color3F(y, x, x);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a grayscale color.
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a grayscale color
	 */
	public static Color3F randomGrayscale() {
		return new Color3F(Randoms.nextFloat(Floats.nextUp(1.0F)));
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a green color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomGreen(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a green color
	 */
	public static Color3F randomGreen() {
		return randomGreen(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a green color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3F} instance with random component values that represents a green color
	 */
	public static Color3F randomGreen(final float maxR, final float maxB) {
		final float g = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float r = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxR, 0.0F)), g));
		final float b = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), g));
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a magenta color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomMagenta(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a magenta color
	 */
	public static Color3F randomMagenta() {
		return randomMagenta(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a magenta color.
	 * 
	 * @param minRB the minimum value to use for the R- and B-components
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3F} instance with random component values that represents a magenta color
	 */
	public static Color3F randomMagenta(final float minRB, final float maxG) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minRB, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), x));
		
		return new Color3F(x, y, x);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a red color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomRed(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a red color
	 */
	public static Color3F randomRed() {
		return randomRed(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a red color.
	 * 
	 * @param maxG the maximum value to use for the G-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3F} instance with random component values that represents a red color
	 */
	public static Color3F randomRed(final float maxG, final float maxB) {
		final float r = Randoms.nextFloat(Floats.nextUp(0.0F), Floats.nextUp(1.0F));
		final float g = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxG, 0.0F)), r));
		final float b = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), r));
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a yellow color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.randomYellow(0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values that represents a yellow color
	 */
	public static Color3F randomYellow() {
		return randomYellow(0.0F, 0.0F);
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values that represents a yellow color.
	 * 
	 * @param minRG the minimum value to use for the R- and G-components
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3F} instance with random component values that represents a yellow color
	 */
	public static Color3F randomYellow(final float minRG, final float maxB) {
		final float x = Randoms.nextFloat(Floats.max(Floats.min(minRG, 1.0F), Floats.nextUp(0.0F)), Floats.nextUp(1.0F));
		final float y = Randoms.nextFloat(0.0F, Floats.min(Floats.nextUp(Floats.max(maxB, 0.0F)), x));
		
		return new Color3F(x, x, y);
	}
	
	/**
	 * Returns a new {@code Color3F} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color3F} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Color3F read(final DataInput dataInput) {
		try {
			final float r = dataInput.readFloat();
			final float g = dataInput.readFloat();
			final float b = dataInput.readFloat();
			
			return new Color3F(r, g, b);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [0.0F, 1.0F]}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.saturate(color, 0.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F saturate(final Color3F color) {
		return saturate(color, 0.0F, 1.0F);
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [Floats.min(componentMinMax, componentMaxMin), Floats.max(componentMinMax, componentMaxMin)]}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param componentMinMax the minimum or maximum component value
	 * @param componentMaxMin the maximum or minimum component value
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F saturate(final Color3F color, final float componentMinMax, final float componentMaxMin) {
		final float r = Floats.saturate(color.r, componentMinMax, componentMaxMin);
		final float g = Floats.saturate(color.g, componentMinMax, componentMaxMin);
		final float b = Floats.saturate(color.b, componentMinMax, componentMaxMin);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F sepia(final Color3F color) {
		final float r = color.r * 0.393F + color.g * 0.769F + color.b * 0.189F;
		final float g = color.r * 0.349F + color.g * 0.686F + color.b * 0.168F;
		final float b = color.r * 0.272F + color.g * 0.534F + color.b * 0.131F;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a {@code Color3F} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F sqrt(final Color3F color) {
		final float r = Floats.sqrt(color.r);
		final float g = Floats.sqrt(color.g);
		final float b = Floats.sqrt(color.b);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorRHS} from the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F subtract(final Color3F colorLHS, final Color3F colorRHS) {
		final float r = colorLHS.r - colorRHS.r;
		final float g = colorLHS.g - colorRHS.g;
		final float b = colorLHS.b - colorRHS.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorB} from the component values of {@code colorA} and the component values of {@code colorC} from the previous result.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @param colorC a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public static Color3F subtract(final Color3F colorA, final Color3F colorB, final Color3F colorC) {
		final float r = colorA.r - colorB.r - colorC.r;
		final float g = colorA.g - colorB.g - colorC.g;
		final float b = colorA.b - colorB.b - colorC.b;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorB} from the component values of {@code colorA} and {@code valueC} from the previous result.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3F} instance
	 * @param colorB a {@code Color3F} instance
	 * @param valueC a {@code float} value
	 * @return a new {@code Color3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3F subtract(final Color3F colorA, final Color3F colorB, final float valueC) {
		final float r = colorA.r - colorB.r - valueC;
		final float g = colorA.g - colorB.g - valueC;
		final float b = colorA.b - colorB.b - valueC;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Subtracts {@code scalarRHS} from the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the subtraction.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3F} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3F subtract(final Color3F colorLHS, final float scalarRHS) {
		final float r = colorLHS.r - scalarRHS;
		final float g = colorLHS.g - scalarRHS;
		final float b = colorLHS.b - scalarRHS;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, -Float.MAX_VALUE);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @param a a {@code float} value for the filmic curve itself
	 * @param b a {@code float} value for the filmic curve itself
	 * @param c a {@code float} value for the filmic curve itself
	 * @param d a {@code float} value for the filmic curve itself
	 * @param e a {@code float} value for the filmic curve itself
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e) {
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, -Float.MAX_VALUE);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method is implemented in the following way for each component:
	 * <pre>
	 * {@code
	 * float r1 = Floats.max(color.getComponent1() * exposure - subtract, minimum);
	 * float r2 = Floats.saturate((r1 * (a * r1 + b)) / (r1 * (c * r1 + d) + e));
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @param a a {@code float} value for the filmic curve itself
	 * @param b a {@code float} value for the filmic curve itself
	 * @param c a {@code float} value for the filmic curve itself
	 * @param d a {@code float} value for the filmic curve itself
	 * @param e a {@code float} value for the filmic curve itself
	 * @param subtract a value to subtract from the component values when they have been multiplied by {@code exposure}
	 * @param minimum the minimum component value allowed
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e, final float subtract, final float minimum) {
		final float r1 = Floats.max(color.r * exposure - subtract, minimum);
		final float g1 = Floats.max(color.g * exposure - subtract, minimum);
		final float b1 = Floats.max(color.b * exposure - subtract, minimum);
		
		final float r2 = Floats.saturate((r1 * (a * r1 + b)) / (r1 * (c * r1 + d) + e));
		final float g2 = Floats.saturate((g1 * (a * g1 + b)) / (g1 * (c * g1 + d) + e));
		final float b2 = Floats.saturate((b1 * (a * b1 + b)) / (b1 * (c * b1 + d) + e));
		
		return new Color3F(r2, g2, b2);
	}
	
	/**
	 * Applies a modified ACES filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.toneMapFilmicCurve(color, exposure, 2.51F, 0.03F, 2.43F, 0.59F, 0.14F);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapFilmicCurveACESModifiedVersion1(final Color3F color, final float exposure) {
//		Source: https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
		return toneMapFilmicCurve(color, exposure, 2.51F, 0.03F, 2.43F, 0.59F, 0.14F);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This tone map operator also performs gamma correction with a gamma of 2.2.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.toneMapFilmicCurve(color, exposure, 6.2F, 0.5F, 6.2F, 1.7F, 0.06F, 0.004F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapFilmicCurveGammaCorrection22(final Color3F color, final float exposure) {
//		Source: http://filmicworlds.com/blog/why-a-filmic-curve-saturates-your-blacks/
		return toneMapFilmicCurve(color, exposure, 6.2F, 0.5F, 6.2F, 1.7F, 0.06F, 0.004F, 0.0F);
	}
	
	/**
	 * Applies a Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapReinhard(final Color3F color, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float r1 = color.r * exposure;
		final float g1 = color.g * exposure;
		final float b1 = color.b * exposure;
		
		final float r2 = r1 / (1.0F + r1);
		final float g2 = g1 / (1.0F + g1);
		final float b2 = b1 / (1.0F + b1);
		
		return new Color3F(r2, g2, b2);
	}
	
	/**
	 * Applies a modified Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapReinhardModifiedVersion1(final Color3F color, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float lWhite = 4.0F;
		
		final float r1 = color.r * exposure;
		final float g1 = color.g * exposure;
		final float b1 = color.b * exposure;
		
		final float r2 = r1 * (1.0F + r1 / (lWhite * lWhite)) / (1.0F + r1);
		final float g2 = g1 * (1.0F + g1 / (lWhite * lWhite)) / (1.0F + g1);
		final float b2 = b1 * (1.0F + b1 / (lWhite * lWhite)) / (1.0F + b1);
		
		return new Color3F(r2, g2, b2);
	}
	
	/**
	 * Applies a modified Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapReinhardModifiedVersion2(final Color3F color, final float exposure) {
		final float r1 = color.r * exposure;
		final float g1 = color.g * exposure;
		final float b1 = color.b * exposure;
		
		final float r2 = 1.0F - Floats.exp(-r1 * exposure);
		final float g2 = 1.0F - Floats.exp(-g1 * exposure);
		final float b2 = 1.0F - Floats.exp(-b1 * exposure);
		
		return new Color3F(r2, g2, b2);
	}
	
	/**
	 * Applies an Unreal 3 tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F toneMapUnreal3(final Color3F color, final float exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final float r1 = color.r * exposure;
		final float g1 = color.g * exposure;
		final float b1 = color.b * exposure;
		
		final float r2 = r1 / (r1 + 0.155F) * 1.019F;
		final float g2 = g1 / (g1 + 0.155F) * 1.019F;
		final float b2 = b1 / (b1 + 0.155F) * 1.019F;
		
		return new Color3F(r2, g2, b2);
	}
	
	/**
	 * Returns a {@code Color3F} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.unpack(color, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @return a {@code Color3F} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}
	 */
	public static Color3F unpack(final int color) {
		return unpack(color, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color3F} instance by unpacking {@code color} using {@code packedIntComponentOrder}.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @param packedIntComponentOrder the {@link PackedIntComponentOrder} to unpack the component values with
	 * @return a {@code Color3F} instance by unpacking {@code color} using {@code packedIntComponentOrder}
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public static Color3F unpack(final int color, final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = packedIntComponentOrder.unpackR(color);
		final int g = packedIntComponentOrder.unpackG(color);
		final int b = packedIntComponentOrder.unpackB(color);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F.BLACK}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.array(length, index -> Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3F[] array(final int length) {
		return array(length, index -> BLACK);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F} instances produced by {@code function}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code function} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param function an {@code IntFunction}
	 * @return a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F} instances produced by {@code function}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or returns {@code null}
	 */
	public static Color3F[] array(final int length, final IntFunction<Color3F> function) {
		final Color3F[] colors = new Color3F[Ints.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(function, "function == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(function.apply(i));
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code length} and contains random {@code Color3F} instances.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.array(length, index -> Color3F.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3F[]} with a length of {@code length} and contains random {@code Color3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3F[] arrayRandom(final int length) {
		return array(length, index -> random());
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.arrayRead(array, ArrayComponentOrder.RGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3F[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.RGB);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3F[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color3F[] colors = new Color3F[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readRAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readGAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readBAsInt(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color3F(r, g, b);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.arrayRead(array, ArrayComponentOrder.RGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3F[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.RGB);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3F[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color3F[] colors = new Color3F[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readR(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readG(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readB(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color3F(r, g, b);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length} and contains {@code Color3F} instances unpacked from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.arrayUnpack(array, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to unpack from
	 * @return a {@code Color3F[]} with a length of {@code array.length} and contains {@code Color3F} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3F[] arrayUnpack(final int[] array) {
		return arrayUnpack(array, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length} and contains {@code Color3F} instances unpacked from {@code array}.
	 * <p>
	 * If either {@code array} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the array to unpack from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @return a {@code Color3F[]} with a length of {@code array.length} and contains {@code Color3F} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code packedIntComponentOrder} are {@code null}
	 */
	public static Color3F[] arrayUnpack(final int[] array, final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final Color3F[] colors = new Color3F[array.length];
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = unpack(array[i], packedIntComponentOrder);
		}
		
		return colors;
	}
	
	/**
	 * Returns the average component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the average component value of {@code r}, {@code g} and {@code b}
	 */
	public static float average(final float r, final float g, final float b) {
		return (r + g + b) / 3.0F;
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
	 * Returns the lightness for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the lightness for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static float lightness(final float r, final float g, final float b) {
		return (max(r, g, b) + min(r, g, b)) / 2.0F;
	}
	
	/**
	 * Returns the largest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the largest component value of {@code r}, {@code g} and {@code b}
	 */
	public static float max(final float r, final float g, final float b) {
		return Floats.max(r, g, b);
	}
	
	/**
	 * Returns the smallest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the smallest component value of {@code r}, {@code g} and {@code b}
	 */
	public static float min(final float r, final float g, final float b) {
		return Floats.min(r, g, b);
	}
	
	/**
	 * Returns the relative luminance for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the relative luminance for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static float relativeLuminance(final float r, final float g, final float b) {
		return r * 0.212671F + g * 0.715160F + b * 0.072169F;
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
	 * Returns the alpha, red, green and blue components as an {@code int} in the format ARGB.
	 * <p>
	 * The alpha component is treated as if it was {@code 1.0F} or {@code 255}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public static int toIntARGB(final float r, final float g, final float b) {
		final int colorA = ((255       & 0xFF) << Utilities.COLOR_A_R_G_B_SHIFT_A);
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