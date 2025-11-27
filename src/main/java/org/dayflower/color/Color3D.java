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

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Ints;
import org.macroing.java.lang.Strings;
import org.macroing.java.util.Randoms;

/**
 * A {@code Color3D} represents a color with three {@code double}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color3D {
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for silver (Ag).
	 */
	public static final Color3D AG_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for silver (Ag).
	 */
	public static final Color3D AG_K;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for aluminum (Al).
	 */
	public static final Color3D AL_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for aluminum (Al).
	 */
	public static final Color3D AL_K;
	
	/**
	 * A {@code Color3D} denoting the color Aztek gold (Au).
	 */
	public static final Color3D AU_AZTEK;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for gold (Au).
	 */
	public static final Color3D AU_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for gold (Au).
	 */
	public static final Color3D AU_K;
	
	/**
	 * A {@code Color3D} denoting the color metallic gold (Au).
	 */
	public static final Color3D AU_METALLIC;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for beryllium (Be).
	 */
	public static final Color3D BE_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for beryllium (Be).
	 */
	public static final Color3D BE_K;
	
	/**
	 * A {@code Color3D} instance that represents the color black.
	 */
	public static final Color3D BLACK;
	
	/**
	 * A {@code Color3D} instance that represents the color blue.
	 */
	public static final Color3D BLUE;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for chromium (Cr).
	 */
	public static final Color3D CR_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for chromium (Cr).
	 */
	public static final Color3D CR_K;
	
	/**
	 * A {@code Color3D} denoting the color copper (Cu).
	 */
	public static final Color3D CU;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for copper (Cu).
	 */
	public static final Color3D CU_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for copper (Cu).
	 */
	public static final Color3D CU_K;
	
	/**
	 * A {@code Color3D} instance that represents the color cyan.
	 */
	public static final Color3D CYAN;
	
	/**
	 * A {@code Color3D} instance that represents the color gray.
	 */
	public static final Color3D GRAY;
	
	/**
	 * A {@code Color3D} instance that represents the color green.
	 */
	public static final Color3D GREEN;
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for mercury (Hg).
	 */
	public static final Color3D HG_ETA;
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for mercury (Hg).
	 */
	public static final Color3D HG_K;
	
	/**
	 * A {@code Color3D} instance that represents the color magenta.
	 */
	public static final Color3D MAGENTA;
	
	/**
	 * A {@code Color3D} instance that represents the color orange.
	 */
	public static final Color3D ORANGE;
	
	/**
	 * A {@code Color3D} instance that represents the color red.
	 */
	public static final Color3D RED;
	
	/**
	 * A {@code Color3D} instance that represents the color white.
	 */
	public static final Color3D WHITE;
	
	/**
	 * A {@code Color3D} instance that represents the color yellow.
	 */
	public static final Color3D YELLOW;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color3D, Color3D> CACHE;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	static {
		CACHE = new HashMap<>();
		
		AG_ETA = getCached(new Color3D(0.15496000609474664D, 0.11647174926802867D, 0.13806632158349902D));
		AG_K = getCached(new Color3D(4.8188730622739175D, 3.115517176753568D, 2.1420735301196467D));
		AL_ETA = getCached(new Color3D(1.6542074904327757D, 0.8784699245201935D, 0.5200436909954684D));
		AL_K = getCached(new Color3D(9.205783890522829D, 6.256010137000009D, 4.826012510420723D));
		AU_AZTEK = getCached(new Color3D(0.76D, 0.6D, 0.33D));
		AU_ETA = getCached(new Color3D(0.14284310835842717D, 0.3741312033192202D, 1.4392239236981954D));
		AU_K = getCached(new Color3D(3.975360769687202D, 2.380584839029059D, 1.5995662411380493D));
		AU_METALLIC = getCached(new Color3D(0.83D, 0.69D, 0.22D));
		BE_ETA = getCached(new Color3D(4.17685492348954D, 3.178196056715122D, 2.777767905501223D));
		BE_K = getCached(new Color3D(3.8279172522105473D, 3.0036378212398547D, 2.8624926111158966D));
		BLACK = getCached(new Color3D(0.0D, 0.0D, 0.0D));
		BLUE = getCached(new Color3D(0.0D, 0.0D, 1.0D));
		CR_ETA = getCached(new Color3D(4.361113549925775D, 2.910427696793282D, 1.6509344663124783D));
		CR_K = getCached(new Color3D(5.196218158260541D, 4.222247331143831D, 3.746427104520829D));
		CU = getCached(new Color3D(0.72D, 0.45D, 0.2D));
		CU_ETA = getCached(new Color3D(0.20002143877100975D, 0.922055401869726D, 1.0997080930365142D));
		CU_K = getCached(new Color3D(3.905268765023564D, 2.4475532558370405D, 2.1373247532273054D));
		CYAN = getCached(new Color3D(0.0D, 1.0D, 1.0D));
		GRAY = getCached(new Color3D(0.5D, 0.5D, 0.5D));
		GREEN = getCached(new Color3D(0.0D, 1.0D, 0.0D));
		HG_ETA = getCached(new Color3D(2.394226334560135D, 1.4369220731460042D, 0.9074837682505891D));
		HG_K = getCached(new Color3D(6.315220111971925D, 4.362519054861604D, 3.4140169529925513D));
		MAGENTA = getCached(new Color3D(1.0D, 0.0D, 1.0D));
		ORANGE = getCached(new Color3D(1.0D, 0.5D, 0.0D));
		RED = getCached(new Color3D(1.0D, 0.0D, 0.0D));
		WHITE = getCached(new Color3D(1.0D, 1.0D, 1.0D));
		YELLOW = getCached(new Color3D(1.0D, 1.0D, 0.0D));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The value of the blue component.
	 */
	public final double b;
	
	/**
	 * The value of the green component.
	 */
	public final double g;
	
	/**
	 * The value of the red component.
	 */
	public final double r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color3D} instance that represents black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(0.0D);
	 * }
	 * </pre>
	 */
	public Color3D() {
		this(0.0D);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3D(final Color3F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3D(final Color3I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3D(final Color4D color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3D(final Color4F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4I} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3D(final Color4I color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3D(final double grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3D(final double r, final double g, final double b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Constructs a new {@code Color3D} instance that represents gray.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(grayscale, grayscale, grayscale);
	 * }
	 * </pre>
	 * 
	 * @param grayscale the value of the red, green and blue components
	 */
	public Color3D(final int grayscale) {
		this(grayscale, grayscale, grayscale);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(r / 255.0D, g / 255.0D, b / 255.0D);
	 * }
	 * </pre>
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 */
	public Color3D(final int r, final int g, final int b) {
		this(r / 255.0D, g / 255.0D, b / 255.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color3D(%s, %s, %s)", Strings.toNonScientificNotationJava(this.r), Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.b));
	}
	
	/**
	 * Compares {@code color} to this {@code Color3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is equal to this {@code Color3D} instance, {@code false} otherwise.
	 * 
	 * @param color the {@code Color3D} to compare to this {@code Color3D} instance for equality
	 * @return {@code true} if, and only if, {@code color} is equal to this {@code Color3D} instance, {@code false} otherwise
	 */
	public boolean equals(final Color3D color) {
		if(color == this) {
			return true;
		} else if(color == null) {
			return false;
		} else if(!Doubles.equals(this.b, color.b)) {
			return false;
		} else if(!Doubles.equals(this.g, color.g)) {
			return false;
		} else if(!Doubles.equals(this.r, color.r)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Color3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color3D}, and they are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color3D}, and they are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color3D)) {
			return false;
		} else {
			return equals(Color3D.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return Doubles.isInfinite(this.r) || Doubles.isInfinite(this.g) || Doubles.isInfinite(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return Doubles.isNaN(this.r) || Doubles.isNaN(this.g) || Doubles.isNaN(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is black, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return Doubles.isZero(this.r) && Doubles.isZero(this.g) && Doubles.isZero(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is blue if, and only if, {@code color.b - deltaR >= color.r} and {@code color.b - deltaG >= color.g}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaG the delta for the G-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is blue, {@code false} otherwise
	 */
	public boolean isBlue(final double deltaR, final double deltaG) {
		return this.b - deltaR >= this.r && this.b - deltaG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return Doubles.equals(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is grayscale, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is grayscale, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return Doubles.equals(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is green, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is green if, and only if, {@code color.g - deltaR >= color.r} and {@code color.g - deltaB >= color.b}.
	 * 
	 * @param deltaR the delta for the R-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is green, {@code false} otherwise
	 */
	public boolean isGreen(final double deltaR, final double deltaB) {
		return this.g - deltaR >= this.r && this.g - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return Doubles.equals(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is red, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is red if, and only if, {@code color.r - deltaG >= color.g} and {@code color.r - deltaB >= color.b}.
	 * 
	 * @param deltaG the delta for the G-component
	 * @param deltaB the delta for the B-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is red, {@code false} otherwise
	 */
	public boolean isRed(final double deltaG, final double deltaB) {
		return this.r - deltaG >= this.g && this.r - deltaB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is white, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return Doubles.equals(this.r, 1.0D) && Doubles.equals(this.g, 1.0D) && Doubles.equals(this.b, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return Doubles.equals(this.r, this.g) && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the average component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public double average() {
		return average(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the lightness for this {@code Color3D} instance.
	 * 
	 * @return the lightness for this {@code Color3D} instance
	 */
	public double lightness() {
		return lightness(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the largest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the largest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public double max() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of {@link #r}, {@link #g} and {@link #b}.
	 * 
	 * @return the smallest component value of {@code  r}, {@code  g} and {@code  b}
	 */
	public double min() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the relative luminance for this {@code Color3D} instance.
	 * 
	 * @return the relative luminance for this {@code Color3D} instance
	 */
	public double relativeLuminance() {
		return relativeLuminance(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color3D} instance.
	 * 
	 * @return a hash code for this {@code Color3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.b), Double.valueOf(this.g), Double.valueOf(this.r));
	}
	
	/**
	 * Returns an {@code int} with the component values in a packed form.
	 * <p>
	 * This method assumes that the component values are within the range [0.0, 1.0]. Any component value outside of this range will be saturated or clamped.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color3D.pack(PackedIntComponentOrder.ARGB);
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
	 * The alpha component is treated as if it was {@code 1.0D} or {@code 255}.
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
	 * Writes this {@code Color3D} instance to {@code dataOutput}.
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
			dataOutput.writeDouble(this.r);
			dataOutput.writeDouble(this.g);
			dataOutput.writeDouble(this.b);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D add(final Color3D colorLHS, final Color3D colorRHS) {
		final double r = colorLHS.r + colorRHS.r;
		final double g = colorLHS.g + colorRHS.g;
		final double b = colorLHS.b + colorRHS.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds {@code scalarRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the addition.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3D add(final Color3D colorLHS, final double scalarRHS) {
		final double r = colorLHS.r + scalarRHS;
		final double g = colorLHS.g + scalarRHS;
		final double b = colorLHS.b + scalarRHS;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the result of one multiplication to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorAdd the {@code Color3D} instance to add
	 * @param colorMultiplyA the {@code Color3D} instance used on the left-hand side of the multiplication
	 * @param colorMultiplyB the {@code Color3D} instance used on the right-hand side of the multiplication
	 * @return a new {@code Color3D} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3D addAndMultiply(final Color3D colorAdd, final Color3D colorMultiplyA, final Color3D colorMultiplyB) {
		final double r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r;
		final double g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g;
		final double b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the result of two multiplications to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorAdd the {@code Color3D} instance to add
	 * @param colorMultiplyA the {@code Color3D} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3D} instance used on the right-hand side of the first multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the second multiplication
	 * @return a new {@code Color3D} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3D addAndMultiply(final Color3D colorAdd, final Color3D colorMultiplyA, final Color3D colorMultiplyB, final double scalarMultiply) {
		final double r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * scalarMultiply;
		final double g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * scalarMultiply;
		final double b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * scalarMultiply;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the result of three multiplications followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA}, {@code colorMultiplyB} or {@code colorMultiplyC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3D.add(colorAdd, Color3D.divide(Color3D.multiply(Color3D.multiply(Color3D.multiply(colorMultiplyA, colorMultiplyB), colorMultiplyC), scalarMultiply), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3D} instance to add
	 * @param colorMultiplyA the {@code Color3D} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3D} instance used on the right-hand side of the first multiplication
	 * @param colorMultiplyC the {@code Color3D} instance used on the right-hand side of the second multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the third multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3D} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA}, {@code colorMultiplyB} or {@code colorMultiplyC} are {@code null}
	 */
	public static Color3D addMultiplyAndDivide(final Color3D colorAdd, final Color3D colorMultiplyA, final Color3D colorMultiplyB, final Color3D colorMultiplyC, final double scalarMultiply, final double scalarDivide) {
		final double r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * colorMultiplyC.r * scalarMultiply / scalarDivide;
		final double g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * colorMultiplyC.g * scalarMultiply / scalarDivide;
		final double b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * colorMultiplyC.b * scalarMultiply / scalarDivide;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the result of one multiplication followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3D.add(colorAdd, Color3D.divide(Color3D.multiply(colorMultiplyA, colorMultiplyB), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3D} instance to add
	 * @param colorMultiplyA the {@code Color3D} instance used on the left-hand side of the multiplication
	 * @param colorMultiplyB the {@code Color3D} instance used on the right-hand side of the multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3D} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3D addMultiplyAndDivide(final Color3D colorAdd, final Color3D colorMultiplyA, final Color3D colorMultiplyB, final double scalarDivide) {
		final double r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r / scalarDivide;
		final double g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g / scalarDivide;
		final double b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b / scalarDivide;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the result of two multiplications followed by one division to the component values of {@code colorAdd}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result.
	 * <p>
	 * If either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following (although faster, because of optimizations):
	 * <pre>
	 * {@code
	 * Color3D.add(colorAdd, Color3D.divide(Color3D.multiply(Color3D.multiply(colorMultiplyA, colorMultiplyB), scalarMultiply), scalarDivide));
	 * }
	 * </pre>
	 * 
	 * @param colorAdd the {@code Color3D} instance to add
	 * @param colorMultiplyA the {@code Color3D} instance used on the left-hand side of the first multiplication
	 * @param colorMultiplyB the {@code Color3D} instance used on the right-hand side of the first multiplication
	 * @param scalarMultiply the scalar value used on the right-hand side of the second multiplication
	 * @param scalarDivide the scalar value used on the right-hand side of the division
	 * @return a new {@code Color3D} instance with the result
	 * @throws NullPointerException thrown if, and only if, either {@code colorAdd}, {@code colorMultiplyA} or {@code colorMultiplyB} are {@code null}
	 */
	public static Color3D addMultiplyAndDivide(final Color3D colorAdd, final Color3D colorMultiplyA, final Color3D colorMultiplyB, final double scalarMultiply, final double scalarDivide) {
		final double r = colorAdd.r + colorMultiplyA.r * colorMultiplyB.r * scalarMultiply / scalarDivide;
		final double g = colorAdd.g + colorMultiplyA.g * colorMultiplyB.g * scalarMultiply / scalarDivide;
		final double b = colorAdd.b + colorMultiplyA.b * colorMultiplyB.b * scalarMultiply / scalarDivide;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method differs from {@link #add(Color3D, Color3D)} in that it assumes {@code colorLHS} to be an average color sample. It uses a stable moving average algorithm to compute a new average color sample as a result of adding {@code colorRHS}. This method is suitable for Monte Carlo-method based algorithms.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @param sampleCount the current sample count
	 * @return a new {@code Color3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D addSample(final Color3D colorLHS, final Color3D colorRHS, final int sampleCount) {
		final double r = colorLHS.r + ((colorRHS.r - colorLHS.r) / sampleCount);
		final double g = colorLHS.g + ((colorRHS.g - colorLHS.g) / sampleCount);
		final double b = colorLHS.b + ((colorRHS.b - colorLHS.b) / sampleCount);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.blend(colorLHS, colorRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D blend(final Color3D colorLHS, final Color3D colorRHS) {
		return blend(colorLHS, colorRHS, 0.5D);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.blend(Color3D.blend(color11, color12, tX), Color3D.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color3D} instance on row 1 and column 1
	 * @param color12 the {@code Color3D} instance on row 1 and column 2
	 * @param color21 the {@code Color3D} instance on row 2 and column 1
	 * @param color22 the {@code Color3D} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color3D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
	public static Color3D blend(final Color3D color11, final Color3D color12, final Color3D color21, final Color3D color22, final double tX, final double tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.blend(colorLHS, colorRHS, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color3D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D blend(final Color3D colorLHS, final Color3D colorRHS, final double t) {
		return blend(colorLHS, colorRHS, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @param tR the factor to use for the R-component in the blending process
	 * @param tG the factor to use for the G-component in the blending process
	 * @param tB the factor to use for the B-component in the blending process
	 * @return a new {@code Color3D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D blend(final Color3D colorLHS, final Color3D colorRHS, final double tR, final double tG, final double tB) {
		final double r = Doubles.lerp(colorLHS.r, colorRHS.r, tR);
		final double g = Doubles.lerp(colorLHS.g, colorRHS.g, tG);
		final double b = Doubles.lerp(colorLHS.b, colorRHS.b, tB);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Divides the component values of {@code colorLHS} with the component values of {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the division.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D divide(final Color3D colorLHS, final Color3D colorRHS) {
		final double r = colorLHS.r / colorRHS.r;
		final double g = colorLHS.g / colorRHS.g;
		final double b = colorLHS.b / colorRHS.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Divides the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the division.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the division
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3D divide(final Color3D colorLHS, final double scalarRHS) {
		final double r = colorLHS.r / scalarRHS;
		final double g = colorLHS.g / scalarRHS;
		final double b = colorLHS.b / scalarRHS;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance from the {@code int} {@code colorARGB}.
	 * 
	 * @param colorARGB an {@code int} that contains the alpha, red, green and blue components
	 * @return a {@code Color3D} instance from the {@code int} {@code colorARGB}
	 */
	public static Color3D fromIntARGB(final int colorARGB) {
		final int r = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance from the {@code int} {@code colorRGB}.
	 * 
	 * @param colorRGB an {@code int} that contains the red, green and blue components
	 * @return a {@code Color3D} instance from the {@code int} {@code colorRGB}
	 */
	public static Color3D fromIntRGB(final int colorRGB) {
		final int r = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF;
		final int g = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF;
		final int b = (colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a cached {@code Color3D} instance that is equal to {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a cached {@code Color3D} instance that is equal to {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D getCached(final Color3D color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleAverage(final Color3D color) {
		return new Color3D(color.average());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.b}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.b}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleB(final Color3D color) {
		return new Color3D(color.b);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.g}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.g}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleG(final Color3D color) {
		return new Color3D(color.g);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleLightness(final Color3D color) {
		return new Color3D(color.lightness());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.max()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.max()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleMax(final Color3D color) {
		return new Color3D(color.max());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.min()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.min()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleMin(final Color3D color) {
		return new Color3D(color.min());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.r}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.r}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleR(final Color3D color) {
		return new Color3D(color.r);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.relativeLuminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.relativeLuminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleRelativeLuminance(final Color3D color) {
		return new Color3D(color.relativeLuminance());
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D invert(final Color3D color) {
		return new Color3D(1.0D - color.r, 1.0D - color.g, 1.0D - color.b);
	}
	
	/**
	 * Returns a new {@code Color3D} instance with the largest component values of {@code colorA} and {@code colorB}.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the largest component values of {@code colorA} and {@code colorB}
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3D max(final Color3D colorA, final Color3D colorB) {
		final double r = Doubles.max(colorA.r, colorB.r);
		final double g = Doubles.max(colorA.g, colorB.g);
		final double b = Doubles.max(colorA.b, colorB.b);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Restricts the component values of {@code color} by dividing them with the maximum component value that is greater than {@code 1.0}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the division, or {@code color} if no division occurred.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method can overflow if the delta between the minimum and maximum component values are large.
	 * <p>
	 * If at least one of the component values are negative, consider calling {@link #minTo0(Color3D)} before calling this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
	 * Color3D b = Color3D.maxTo1(a);
	 * 
	 * //a.r = 0.0D, a.g = 1.0D, a.b = 2.0D
	 * //b.r = 0.0D, b.g = 0.5D, b.b = 1.0D
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the division, or {@code color} if no division occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D maxTo1(final Color3D color) {
		final double max = color.max();
		
		if(max > 1.0D) {
			final double r = color.r / max;
			final double g = color.g / max;
			final double b = color.b / max;
			
			return new Color3D(r, g, b);
		}
		
		return color;
	}
	
	/**
	 * Returns a new {@code Color3D} instance with the smallest component values of {@code colorA} and {@code colorB}.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the smallest component values of {@code colorA} and {@code colorB}
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3D min(final Color3D colorA, final Color3D colorB) {
		final double r = Doubles.min(colorA.r, colorB.r);
		final double g = Doubles.min(colorA.g, colorB.g);
		final double b = Doubles.min(colorA.b, colorB.b);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Restricts the component values of {@code color} by adding the minimum component value that is less than {@code 0.0} to them.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the addition, or {@code color} if no addition occurred.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method can overflow if the delta between the minimum and maximum component values are large.
	 * <p>
	 * Consider calling {@link #maxTo1(Color3D)} after a call to this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3D a = new Color3D(-2.0D, 0.0D, 1.0D);
	 * Color3D b = Color3D.minTo0(a);
	 * 
	 * //a.r = -2.0D, a.g = 0.0D, a.b = 1.0D
	 * //b.r =  0.0D, b.g = 2.0D, b.b = 3.0D
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the addition, or {@code color} if no addition occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D minTo0(final Color3D color) {
		final double min = color.min();
		
		if(min < 0.0D) {
			final double r = color.r + -min;
			final double g = color.g + -min;
			final double b = color.b + -min;
			
			return new Color3D(r, g, b);
		}
		
		return color;
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with the component values of {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D multiply(final Color3D colorLHS, final Color3D colorRHS) {
		final double r = colorLHS.r * colorRHS.r;
		final double g = colorLHS.g * colorRHS.g;
		final double b = colorLHS.b * colorRHS.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA}, {@code colorB} and {@code colorC}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @param colorC a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public static Color3D multiply(final Color3D colorA, final Color3D colorB, final Color3D colorC) {
		final double r = colorA.r * colorB.r * colorC.r;
		final double g = colorA.g * colorB.g * colorC.g;
		final double b = colorA.b * colorB.b * colorC.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA}, {@code colorB}, {@code colorC} and {@code colorD}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA}, {@code colorB}, {@code colorC} or {@code colorD} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @param colorC a {@code Color3D} instance
	 * @param colorD a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB}, {@code colorC} or {@code colorD} are {@code null}
	 */
	public static Color3D multiply(final Color3D colorA, final Color3D colorB, final Color3D colorC, final Color3D colorD) {
		final double r = colorA.r * colorB.r * colorC.r * colorD.r;
		final double g = colorA.g * colorB.g * colorC.g * colorD.g;
		final double b = colorA.b * colorB.b * colorC.b * colorD.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorA} with the component values of {@code colorB} and its result with {@code scalarC}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @param scalarC a scalar value
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3D multiply(final Color3D colorA, final Color3D colorB, final double scalarC) {
		final double r = colorA.r * colorB.r * scalarC;
		final double g = colorA.g * colorB.g * scalarC;
		final double b = colorA.b * colorB.b * scalarC;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3D multiply(final Color3D colorLHS, final double scalarRHS) {
		final double r = colorLHS.r * scalarRHS;
		final double g = colorLHS.g * scalarRHS;
		final double b = colorLHS.b * scalarRHS;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Multiplies the component values of {@code colorLHS} with {@code scalarRHS} and saturates or clamps all negative component values.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the multiplication.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the multiplication
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3D multiplyAndSaturateNegative(final Color3D colorLHS, final double scalarRHS) {
		final double r = Doubles.max(colorLHS.r * scalarRHS, 0.0D);
		final double g = Doubles.max(colorLHS.g * scalarRHS, 0.0D);
		final double b = Doubles.max(colorLHS.b * scalarRHS, 0.0D);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Negates the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the negation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the negation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D negate(final Color3D color) {
		final double r = -color.r;
		final double g = -color.g;
		final double b = -color.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Normalizes the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D normalize(final Color3D color) {
		final double sum = color.r + color.g + color.b;
		
		if(sum < 0.000001D) {
			return color;
		}
		
		final double sumReciprocal = 1.0D / sum;
		
		final double r = color.r * sumReciprocal;
		final double g = color.g * sumReciprocal;
		final double b = color.b * sumReciprocal;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Normalizes the component values of {@code color} based on its relative luminance.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D normalizeRelativeLuminance(final Color3D color) {
		final double relativeLuminance = color.relativeLuminance();
		
		if(relativeLuminance > 0.0D) {
			return divide(color, relativeLuminance);
		}
		
		return WHITE;
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(Randoms.nextDouble(Doubles.nextUp(1.0D)), Randoms.nextDouble(Doubles.nextUp(1.0D)), Randoms.nextDouble(Doubles.nextUp(1.0D)));
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values
	 */
	public static Color3D random() {
		return new Color3D(Randoms.nextDouble(Doubles.nextUp(1.0D)), Randoms.nextDouble(Doubles.nextUp(1.0D)), Randoms.nextDouble(Doubles.nextUp(1.0D)));
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a blue color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomBlue(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a blue color
	 */
	public static Color3D randomBlue() {
		return randomBlue(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a blue color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3D} instance with random component values that represents a blue color
	 */
	public static Color3D randomBlue(final double maxR, final double maxG) {
		final double b = Randoms.nextDouble(Doubles.nextUp(0.0D), Doubles.nextUp(1.0D));
		final double r = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxR, 0.0D)), b));
		final double g = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxG, 0.0D)), b));
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a cyan color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomCyan(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a cyan color
	 */
	public static Color3D randomCyan() {
		return randomCyan(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a cyan color.
	 * 
	 * @param minGB the minimum value to use for the G- and B-components
	 * @param maxR the maximum value to use for the R-component
	 * @return a {@code Color3D} instance with random component values that represents a cyan color
	 */
	public static Color3D randomCyan(final double minGB, final double maxR) {
		final double x = Randoms.nextDouble(Doubles.max(Doubles.min(minGB, 1.0D), Doubles.nextUp(0.0D)), Doubles.nextUp(1.0D));
		final double y = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxR, 0.0D)), x));
		
		return new Color3D(y, x, x);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a grayscale color.
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a grayscale color
	 */
	public static Color3D randomGrayscale() {
		return new Color3D(Randoms.nextDouble(Doubles.nextUp(1.0D)));
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a green color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomGreen(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a green color
	 */
	public static Color3D randomGreen() {
		return randomGreen(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a green color.
	 * 
	 * @param maxR the maximum value to use for the R-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3D} instance with random component values that represents a green color
	 */
	public static Color3D randomGreen(final double maxR, final double maxB) {
		final double g = Randoms.nextDouble(Doubles.nextUp(0.0D), Doubles.nextUp(1.0D));
		final double r = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxR, 0.0D)), g));
		final double b = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxB, 0.0D)), g));
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a magenta color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomMagenta(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a magenta color
	 */
	public static Color3D randomMagenta() {
		return randomMagenta(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a magenta color.
	 * 
	 * @param minRB the minimum value to use for the R- and B-components
	 * @param maxG the maximum value to use for the G-component
	 * @return a {@code Color3D} instance with random component values that represents a magenta color
	 */
	public static Color3D randomMagenta(final double minRB, final double maxG) {
		final double x = Randoms.nextDouble(Doubles.max(Doubles.min(minRB, 1.0D), Doubles.nextUp(0.0D)), Doubles.nextUp(1.0D));
		final double y = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxG, 0.0D)), x));
		
		return new Color3D(x, y, x);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a red color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomRed(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a red color
	 */
	public static Color3D randomRed() {
		return randomRed(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a red color.
	 * 
	 * @param maxG the maximum value to use for the G-component
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3D} instance with random component values that represents a red color
	 */
	public static Color3D randomRed(final double maxG, final double maxB) {
		final double r = Randoms.nextDouble(Doubles.nextUp(0.0D), Doubles.nextUp(1.0D));
		final double g = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxG, 0.0D)), r));
		final double b = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxB, 0.0D)), r));
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a yellow color.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.randomYellow(0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values that represents a yellow color
	 */
	public static Color3D randomYellow() {
		return randomYellow(0.0D, 0.0D);
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values that represents a yellow color.
	 * 
	 * @param minRG the minimum value to use for the R- and G-components
	 * @param maxB the maximum value to use for the B-component
	 * @return a {@code Color3D} instance with random component values that represents a yellow color
	 */
	public static Color3D randomYellow(final double minRG, final double maxB) {
		final double x = Randoms.nextDouble(Doubles.max(Doubles.min(minRG, 1.0D), Doubles.nextUp(0.0D)), Doubles.nextUp(1.0D));
		final double y = Randoms.nextDouble(0.0D, Doubles.min(Doubles.nextUp(Doubles.max(maxB, 0.0D)), x));
		
		return new Color3D(x, x, y);
	}
	
	/**
	 * Returns a new {@code Color3D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color3D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
	public static Color3D read(final DataInput dataInput) {
		try {
			final double r = dataInput.readDouble();
			final double g = dataInput.readDouble();
			final double b = dataInput.readDouble();
			
			return new Color3D(r, g, b);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [0.0D, 1.0D]}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.saturate(color, 0.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D saturate(final Color3D color) {
		return saturate(color, 0.0D, 1.0D);
	}
	
	/**
	 * Saturates or clamps {@code color} to the range {@code [Doubles.min(componentMinMax, componentMaxMin), Doubles.max(componentMinMax, componentMaxMin)]}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param componentMinMax the minimum or maximum component value
	 * @param componentMaxMin the maximum or minimum component value
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D saturate(final Color3D color, final double componentMinMax, final double componentMaxMin) {
		final double r = Doubles.saturate(color.r, componentMinMax, componentMaxMin);
		final double g = Doubles.saturate(color.g, componentMinMax, componentMaxMin);
		final double b = Doubles.saturate(color.b, componentMinMax, componentMaxMin);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D sepia(final Color3D color) {
		final double r = color.r * 0.393D + color.g * 0.769D + color.b * 0.189D;
		final double g = color.r * 0.349D + color.g * 0.686D + color.b * 0.168D;
		final double b = color.r * 0.272D + color.g * 0.534D + color.b * 0.131D;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a {@code Color3D} instance with its component values corresponding to the correctly rounded positive square root of the component values of {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D sqrt(final Color3D color) {
		final double r = Doubles.sqrt(color.r);
		final double g = Doubles.sqrt(color.g);
		final double b = Doubles.sqrt(color.b);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorRHS} from the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D subtract(final Color3D colorLHS, final Color3D colorRHS) {
		final double r = colorLHS.r - colorRHS.r;
		final double g = colorLHS.g - colorRHS.g;
		final double b = colorLHS.b - colorRHS.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorB} from the component values of {@code colorA} and the component values of {@code colorC} from the previous result.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @param colorC a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorA}, {@code colorB} or {@code colorC} are {@code null}
	 */
	public static Color3D subtract(final Color3D colorA, final Color3D colorB, final Color3D colorC) {
		final double r = colorA.r - colorB.r - colorC.r;
		final double g = colorA.g - colorB.g - colorC.g;
		final double b = colorA.b - colorB.b - colorC.b;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Subtracts the component values of {@code colorB} from the component values of {@code colorA} and {@code valueC} from the previous result.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color3D} instance
	 * @param colorB a {@code Color3D} instance
	 * @param valueC a {@code double} value
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
	public static Color3D subtract(final Color3D colorA, final Color3D colorB, final double valueC) {
		final double r = colorA.r - colorB.r - valueC;
		final double g = colorA.g - colorB.g - valueC;
		final double b = colorA.b - colorB.b - valueC;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Subtracts {@code scalarRHS} from the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If {@code colorLHS} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param scalarRHS the scalar value on the right-hand side
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, {@code colorLHS} is {@code null}
	 */
	public static Color3D subtract(final Color3D colorLHS, final double scalarRHS) {
		final double r = colorLHS.r - scalarRHS;
		final double g = colorLHS.g - scalarRHS;
		final double b = colorLHS.b - scalarRHS;
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0D, -Double.MAX_VALUE);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @param a a {@code double} value for the filmic curve itself
	 * @param b a {@code double} value for the filmic curve itself
	 * @param c a {@code double} value for the filmic curve itself
	 * @param d a {@code double} value for the filmic curve itself
	 * @param e a {@code double} value for the filmic curve itself
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapFilmicCurve(final Color3D color, final double exposure, final double a, final double b, final double c, final double d, final double e) {
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0D, -Double.MAX_VALUE);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method is implemented in the following way for each component:
	 * <pre>
	 * {@code
	 * double r1 = Doubles.max(color.r * exposure - subtract, minimum);
	 * double r2 = Doubles.saturate((r1 * (a * r1 + b)) / (r1 * (c * r1 + d) + e));
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @param a a {@code double} value for the filmic curve itself
	 * @param b a {@code double} value for the filmic curve itself
	 * @param c a {@code double} value for the filmic curve itself
	 * @param d a {@code double} value for the filmic curve itself
	 * @param e a {@code double} value for the filmic curve itself
	 * @param subtract a value to subtract from the component values when they have been multiplied by {@code exposure}
	 * @param minimum the minimum component value allowed
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapFilmicCurve(final Color3D color, final double exposure, final double a, final double b, final double c, final double d, final double e, final double subtract, final double minimum) {
		final double r1 = Doubles.max(color.r * exposure - subtract, minimum);
		final double g1 = Doubles.max(color.g * exposure - subtract, minimum);
		final double b1 = Doubles.max(color.b * exposure - subtract, minimum);
		
		final double r2 = Doubles.saturate((r1 * (a * r1 + b)) / (r1 * (c * r1 + d) + e));
		final double g2 = Doubles.saturate((g1 * (a * g1 + b)) / (g1 * (c * g1 + d) + e));
		final double b2 = Doubles.saturate((b1 * (a * b1 + b)) / (b1 * (c * b1 + d) + e));
		
		return new Color3D(r2, g2, b2);
	}
	
	/**
	 * Applies a modified ACES filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.toneMapFilmicCurve(color, exposure, 2.51D, 0.03D, 2.43D, 0.59D, 0.14D);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapFilmicCurveACESModifiedVersion1(final Color3D color, final double exposure) {
//		Source: https://knarkowicz.wordpress.com/2016/01/06/aces-filmic-tone-mapping-curve/
		return toneMapFilmicCurve(color, exposure, 2.51D, 0.03D, 2.43D, 0.59D, 0.14D);
	}
	
	/**
	 * Applies a filmic curve tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This tone map operator also performs gamma correction with a gamma of 2.2.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.toneMapFilmicCurve(color, exposure, 6.2D, 0.5D, 6.2D, 1.7D, 0.06D, 0.004D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapFilmicCurveGammaCorrection22(final Color3D color, final double exposure) {
//		Source: http://filmicworlds.com/blog/why-a-filmic-curve-saturates-your-blacks/
		return toneMapFilmicCurve(color, exposure, 6.2D, 0.5D, 6.2D, 1.7D, 0.06D, 0.004D, 0.0D);
	}
	
	/**
	 * Applies a Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapReinhard(final Color3D color, final double exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final double r1 = color.r * exposure;
		final double g1 = color.g * exposure;
		final double b1 = color.b * exposure;
		
		final double r2 = r1 / (1.0D + r1);
		final double g2 = g1 / (1.0D + g1);
		final double b2 = b1 / (1.0D + b1);
		
		return new Color3D(r2, g2, b2);
	}
	
	/**
	 * Applies a modified Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapReinhardModifiedVersion1(final Color3D color, final double exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final double lWhite = 4.0D;
		
		final double r1 = color.r * exposure;
		final double g1 = color.g * exposure;
		final double b1 = color.b * exposure;
		
		final double r2 = r1 * (1.0D + r1 / (lWhite * lWhite)) / (1.0D + r1);
		final double g2 = g1 * (1.0D + g1 / (lWhite * lWhite)) / (1.0D + g1);
		final double b2 = b1 * (1.0D + b1 / (lWhite * lWhite)) / (1.0D + b1);
		
		return new Color3D(r2, g2, b2);
	}
	
	/**
	 * Applies a modified Reinhard tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapReinhardModifiedVersion2(final Color3D color, final double exposure) {
		final double r1 = color.r * exposure;
		final double g1 = color.g * exposure;
		final double b1 = color.b * exposure;
		
		final double r2 = 1.0F - Doubles.exp(-r1 * exposure);
		final double g2 = 1.0F - Doubles.exp(-g1 * exposure);
		final double b2 = 1.0F - Doubles.exp(-b1 * exposure);
		
		return new Color3D(r2, g2, b2);
	}
	
	/**
	 * Applies an Unreal 3 tone map operator to {@code color}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param exposure the exposure to use
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D toneMapUnreal3(final Color3D color, final double exposure) {
//		Source: https://www.shadertoy.com/view/WdjSW3
		
		final double r1 = color.r * exposure;
		final double g1 = color.g * exposure;
		final double b1 = color.b * exposure;
		
		final double r2 = r1 / (r1 + 0.155D) * 1.019D;
		final double g2 = g1 / (g1 + 0.155D) * 1.019D;
		final double b2 = b1 / (b1 + 0.155D) * 1.019D;
		
		return new Color3D(r2, g2, b2);
	}
	
	/**
	 * Returns a {@code Color3D} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.unpack(color, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @return a {@code Color3D} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}
	 */
	public static Color3D unpack(final int color) {
		return unpack(color, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color3D} instance by unpacking {@code color} using {@code packedIntComponentOrder}.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @param packedIntComponentOrder the {@link PackedIntComponentOrder} to unpack the component values with
	 * @return a {@code Color3D} instance by unpacking {@code color} using {@code packedIntComponentOrder}
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
	public static Color3D unpack(final int color, final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = packedIntComponentOrder.unpackR(color);
		final int g = packedIntComponentOrder.unpackG(color);
		final int b = packedIntComponentOrder.unpackB(color);
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D.BLACK}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.array(length, index -> Color3D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3D[] array(final int length) {
		return array(length, index -> BLACK);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D} instances produced by {@code function}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code function} is {@code null} or returns {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param function an {@code IntFunction}
	 * @return a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D} instances produced by {@code function}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code function} is {@code null} or returns {@code null}
	 */
	public static Color3D[] array(final int length, final IntFunction<Color3D> function) {
		final Color3D[] colors = new Color3D[Ints.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(function, "function == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(function.apply(i));
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code length} and contains random {@code Color3D} instances.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.array(length, index -> Color3F.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3D[]} with a length of {@code length} and contains random {@code Color3D} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3D[] arrayRandom(final int length) {
		return array(length, index -> random());
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3D} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.arrayRead(array, ArrayComponentOrder.RGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3D[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3D[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.RGB);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3D} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3D[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color3D[] colors = new Color3D[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readRAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readGAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readBAsInt(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color3D(r, g, b);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3D} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.arrayRead(array, ArrayComponentOrder.RGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3D[]} with a length of {@code array.length / ArrayComponentOrder.RGB.getComponentCount()} and contains {@code Color3D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGB.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3D[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.RGB);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3D} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3D[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		if(array.length % arrayComponentOrder.getComponentCount() != 0) {
			throw new IllegalArgumentException("array.length % arrayComponentOrder.getComponentCount()");
		}
		
		final Color3D[] colors = new Color3D[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readR(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readG(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readB(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color3D(r, g, b);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length} and contains {@code Color3D} instances unpacked from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3D.arrayUnpack(array, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to unpack from
	 * @return a {@code Color3D[]} with a length of {@code array.length} and contains {@code Color3D} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3D[] arrayUnpack(final int[] array) {
		return arrayUnpack(array, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code array.length} and contains {@code Color3D} instances unpacked from {@code array}.
	 * <p>
	 * If either {@code array} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the array to unpack from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @return a {@code Color3D[]} with a length of {@code array.length} and contains {@code Color3D} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code packedIntComponentOrder} are {@code null}
	 */
	public static Color3D[] arrayUnpack(final int[] array, final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final Color3D[] colors = new Color3D[array.length];
		
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
	public static double average(final double r, final double g, final double b) {
		return (r + g + b) / 3.0D;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorARGB} as a {@code double}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the B-component in {@code colorARGB} as a {@code double}
	 */
	public static double fromIntARGBToDoubleB(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_B) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorARGB} as a {@code double}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the G-component in {@code colorARGB} as a {@code double}
	 */
	public static double fromIntARGBToDoubleG(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_G) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorARGB} as a {@code double}.
	 * 
	 * @param colorARGB an {@code int} that contains a color with components in the format ARGB
	 * @return the value of the R-component in {@code colorARGB} as a {@code double}
	 */
	public static double fromIntARGBToDoubleR(final int colorARGB) {
		return ((colorARGB >> Utilities.COLOR_A_R_G_B_SHIFT_R) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the value of the B-component in {@code colorRGB} as a {@code double}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the B-component in {@code colorRGB} as a {@code double}
	 */
	public static double fromIntRGBToDoubleB(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_B) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the value of the G-component in {@code colorRGB} as a {@code double}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the G-component in {@code colorRGB} as a {@code double}
	 */
	public static double fromIntRGBToDoubleG(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_G) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the value of the R-component in {@code colorRGB} as a {@code double}.
	 * 
	 * @param colorRGB an {@code int} that contains a color with components in the format RGB
	 * @return the value of the R-component in {@code colorRGB} as a {@code double}
	 */
	public static double fromIntRGBToDoubleR(final int colorRGB) {
		return ((colorRGB >> Utilities.COLOR_R_G_B_SHIFT_R) & 0xFF) / 255.0D;
	}
	
	/**
	 * Returns the lightness for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the lightness for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static double lightness(final double r, final double g, final double b) {
		return (max(r, g, b) + min(r, g, b)) / 2.0D;
	}
	
	/**
	 * Returns the largest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the largest component value of {@code r}, {@code g} and {@code b}
	 */
	public static double max(final double r, final double g, final double b) {
		return Doubles.max(r, g, b);
	}
	
	/**
	 * Returns the smallest component value of {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the smallest component value of {@code r}, {@code g} and {@code b}
	 */
	public static double min(final double r, final double g, final double b) {
		return Doubles.min(r, g, b);
	}
	
	/**
	 * Returns the relative luminance for the color represented by {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the relative luminance for the color represented by {@code r}, {@code g} and {@code b}
	 */
	public static double relativeLuminance(final double r, final double g, final double b) {
		return r * 0.212671D + g * 0.715160D + b * 0.072169D;
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
	 * The alpha component is treated as if it was {@code 1.0D} or {@code 255}.
	 * 
	 * @param r the value of the red component
	 * @param g the value of the green component
	 * @param b the value of the blue component
	 * @return the alpha, red, green and blue components as an {@code int} in the format ARGB
	 */
	public static int toIntARGB(final double r, final double g, final double b) {
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
	public static int toIntB(final double b) {
		return Ints.saturateAndScaleToInt(b);
	}
	
	/**
	 * Returns the green component as an {@code int}.
	 * 
	 * @param g the value of the green component
	 * @return the green component as an {@code int}
	 */
	public static int toIntG(final double g) {
		return Ints.saturateAndScaleToInt(g);
	}
	
	/**
	 * Returns the red component as an {@code int}.
	 * 
	 * @param r the value of the red component
	 * @return the red component as an {@code int}
	 */
	public static int toIntR(final double r) {
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
	public static int toIntRGB(final double r, final double g, final double b) {
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