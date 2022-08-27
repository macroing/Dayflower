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

import static org.dayflower.utility.Floats.MIN_VALUE;
import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.exp;
import static org.dayflower.utility.Floats.finiteOrDefault;
import static org.dayflower.utility.Floats.isInfinite;
import static org.dayflower.utility.Floats.isNaN;
import static org.dayflower.utility.Floats.isZero;
import static org.dayflower.utility.Floats.lerp;
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.toInt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.ArrayComponentOrder;
import org.macroing.art4j.color.PackedIntComponentOrder;
import org.macroing.java.lang.Strings;

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
//	TODO: Add Unit Tests!
	public static final Color3F AG_ETA = IrregularSpectralCurveF.AG_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for silver (Ag).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F AG_K = IrregularSpectralCurveF.AG_K.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for aluminum (Al).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F AL_ETA = IrregularSpectralCurveF.AL_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for aluminum (Al).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F AL_K = IrregularSpectralCurveF.AL_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color Aztek gold (Au).
	 */
	public static final Color3F AU_AZTEK = new Color3F(0.76F, 0.6F, 0.33F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for gold (Au).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F AU_ETA = IrregularSpectralCurveF.AU_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for gold (Au).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F AU_K = IrregularSpectralCurveF.AU_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color metallic gold (Au).
	 */
	public static final Color3F AU_METALLIC = new Color3F(0.83F, 0.69F, 0.22F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for beryllium (Be).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F BE_ETA = IrregularSpectralCurveF.BE_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for beryllium (Be).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F BE_K = IrregularSpectralCurveF.BE_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color black.
	 */
	public static final Color3F BLACK = new Color3F();
	
	/**
	 * A {@code Color3F} denoting the color blue.
	 */
	public static final Color3F BLUE = new Color3F(0.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for chromium (Cr).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F CR_ETA = IrregularSpectralCurveF.CR_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for chromium (Cr).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F CR_K = IrregularSpectralCurveF.CR_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color copper (Cu).
	 */
	public static final Color3F CU = new Color3F(0.72F, 0.45F, 0.2F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for copper (Cu).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F CU_ETA = IrregularSpectralCurveF.CU_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for copper (Cu).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F CU_K = IrregularSpectralCurveF.CU_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color cyan.
	 */
	public static final Color3F CYAN = new Color3F(0.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.01F}.
	 */
	public static final Color3F GRAY_0_01 = new Color3F(0.01F, 0.01F, 0.01F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.1F}.
	 */
	public static final Color3F GRAY_0_10 = new Color3F(0.1F, 0.1F, 0.1F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.2F}.
	 */
	public static final Color3F GRAY_0_20 = new Color3F(0.2F, 0.2F, 0.2F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.25F}.
	 */
	public static final Color3F GRAY_0_25 = new Color3F(0.25F, 0.25F, 0.25F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.3F}.
	 */
	public static final Color3F GRAY_0_30 = new Color3F(0.3F, 0.3F, 0.3F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 0.5F}.
	 */
	public static final Color3F GRAY_0_50 = new Color3F(0.5F, 0.5F, 0.5F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 1.5F}.
	 */
	public static final Color3F GRAY_1_50 = new Color3F(1.5F, 1.5F, 1.5F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 1.55F}.
	 */
	public static final Color3F GRAY_1_55 = new Color3F(1.55F, 1.55F, 1.55F);
	
	/**
	 * A {@code Color3F} denoting the color gray, with component values of {@code 2.0F}.
	 */
	public static final Color3F GRAY_2_00 = new Color3F(2.0F, 2.0F, 2.0F);
	
	/**
	 * A {@code Color3F} denoting the color green.
	 */
	public static final Color3F GREEN = new Color3F(0.0F, 1.0F, 0.0F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for mercury (Hg).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F HG_ETA = IrregularSpectralCurveF.HG_ETA.toColorRGB();
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for mercury (Hg).
	 */
//	TODO: Add Unit Tests!
	public static final Color3F HG_K = IrregularSpectralCurveF.HG_K.toColorRGB();
	
	/**
	 * A {@code Color3F} denoting the color magenta.
	 */
	public static final Color3F MAGENTA = new Color3F(1.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code Color3F} denoting the color orange.
	 */
	public static final Color3F ORANGE = new Color3F(1.0F, 0.5F, 0.0F);
	
	/**
	 * A {@code Color3F} denoting the color red.
	 */
	public static final Color3F RED = new Color3F(1.0F, 0.0F, 0.0F);
	
	/**
	 * A {@code Color3F} denoting the color white.
	 */
	public static final Color3F WHITE = new Color3F(1.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code Color3F} denoting the color yellow.
	 */
	public static final Color3F YELLOW = new Color3F(1.0F, 1.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final float[] R_G_B_EXPONENTS = doCreateRGBExponents();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The B-component of this {@code Color3F} instance.
	 */
	public final float b;
	
	/**
	 * The G-component of this {@code Color3F} instance.
	 */
	public final float g;
	
	/**
	 * The R-component of this {@code Color3F} instance.
	 */
	public final float r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color3F} instance denoting the color black.
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
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color4F color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Color3F(final float component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance given the component values {@code r}, {@code g} and {@code b}.
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color3F(final float r, final float g, final float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * Constructs a new {@code Color3F} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Color3F(final int component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color3F} instance given the component values {@code r}, {@code g} and {@code b}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(Ints.saturate(r) / 255.0F, Ints.saturate(g) / 255.0F, Ints.saturate(b) / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color3F(final int r, final int g, final int b) {
		this(Ints.saturate(r) / 255.0F, Ints.saturate(g) / 255.0F, Ints.saturate(b) / 255.0F);
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
	 * Returns {@code true} if, and only if, {@code color} is an instance of {@code Color3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param color the {@code Color3F} to compare to this {@code Color3F} instance for equality
	 * @return {@code true} if, and only if, {@code color} is an instance of {@code Color3F}, and their respective values are equal, {@code false} otherwise
	 */
	public boolean equals(final Color3F color) {
		if(color == this) {
			return true;
		} else if(color == null) {
			return false;
		} else if(!equal(this.r, color.r)) {
			return false;
		} else if(!equal(this.g, color.g)) {
			return false;
		} else if(!equal(this.b, color.b)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Color3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color3F}, and their respective values are equal, {@code false} otherwise
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
		return isInfinite(this.r) || isInfinite(this.g) || isInfinite(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return isNaN(this.r) || isNaN(this.g) || isNaN(this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is black if all component values are {@code 0.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isGrayscale() && isZero(this.r);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is considered blue if, and only if, {@code color.b - thresholdR >= color.r} and {@code color.b - thresholdG >= color.g}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue(final float thresholdR, final float thresholdG) {
		return this.b - thresholdR >= this.r && this.b - thresholdG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return equal(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is a grayscale color if all component values are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is a grayscale color, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return equal(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered green, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is considered green if, and only if, {@code color.g - thresholdR >= color.r} and {@code color.g - thresholdB >= color.b}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen(final float thresholdR, final float thresholdB) {
		return this.g - thresholdR >= this.r && this.g - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return equal(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered red, {@code false} otherwise.
	 * <p>
	 * The {@code Color3F} instance {@code color} is considered red if, and only if, {@code color.r - thresholdG >= color.g} and {@code color.r - thresholdB >= color.b}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed(final float thresholdG, final float thresholdB) {
		return this.r - thresholdG >= this.g && this.r - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is white if all component values are equal and greater than or equal to {@code 1.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isGrayscale() && this.r >= 1.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return equal(this.r, this.g) && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of this {@code Color3F} instance.
	 * 
	 * @return the average component value of this {@code Color3F} instance
	 */
	public float average() {
		return (this.r + this.g + this.b) / 3.0F;
	}
	
	/**
	 * Returns the lightness of this {@code Color3F} instance.
	 * 
	 * @return the lightness of this {@code Color3F} instance
	 */
	public float lightness() {
		return (maximum() + minimum()) / 2.0F;
	}
	
	/**
	 * Returns the relative luminance of this {@code Color3F} instance.
	 * <p>
	 * The algorithm used is only suitable for linear {@code Color3F} instances.
	 * 
	 * @return the relative luminance of this {@code Color3F} instance
	 */
	public float luminance() {
		return this.r * 0.212671F + this.g * 0.715160F + this.b * 0.072169F;
	}
	
	/**
	 * Returns the largest component value.
	 * 
	 * @return the largest component value
	 */
	public float maximum() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value.
	 * 
	 * @return the smallest component value
	 */
	public float minimum() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color3F} instance.
	 * 
	 * @return a hash code for this {@code Color3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.r), Float.valueOf(this.g), Float.valueOf(this.b));
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
		final int r = toInt(Floats.saturate(this.r) * 255.0F + 0.5F);
		final int g = toInt(Floats.saturate(this.g) * 255.0F + 0.5F);
		final int b = toInt(Floats.saturate(this.b) * 255.0F + 0.5F);
		
		return packedIntComponentOrder.pack(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} with the component values of this {@code Color3F} instance in RGBE-form.
	 * 
	 * @return an {@code int} with the component values of this {@code Color3F} instance in RGBE-form
	 */
//	TODO: Add Unit Tests!
	public int toRGBE() {
		final float maximum = max(this.r, this.g, this.b);
		
		if(maximum < 1.0e-32F) {
			return 0;
		}
		
		float mantissa = maximum;
		
		int exponent = 0;
		
		if(maximum > 1.0F) {
			while(mantissa > 1.0F) {
				mantissa *= 0.5F;
				
				exponent++;
			}
		} else if(maximum <= 0.5F) {
			while(mantissa <= 0.5F) {
				mantissa *= 2.0F;
				
				exponent--;
			}
		}
		
//		Find out if 255.0F should be 256.0F or maybe 255.5F? In Sunflow it is 255.0F and other places mention 256.0F in some cases.
//		Performing multiple conversions seems to decrease the values over time.
		final float multiplier = (mantissa * 255.0F) / maximum;
		
		final int r = toInt(this.r * multiplier) << 24;
		final int g = toInt(this.g * multiplier) << 16;
		final int b = toInt(this.b * multiplier) <<  8;
		final int e = exponent + 128;
		
		final int colorRGBE = r | g | b | e;
		
		return colorRGBE;
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
//	TODO: Add Unit Tests!
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
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @return a new {@code Color3F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F blend(final Color3F colorLHS, final Color3F colorRHS, final float tComponent1, final float tComponent2, final float tComponent3) {
		final float r = lerp(colorLHS.r, colorRHS.r, tComponent1);
		final float g = lerp(colorLHS.g, colorRHS.g, tComponent2);
		final float b = lerp(colorLHS.b, colorRHS.b, tComponent3);
		
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
		final float r = finiteOrDefault(colorLHS.r / colorRHS.r, 0.0F);
		final float g = finiteOrDefault(colorLHS.g / colorRHS.g, 0.0F);
		final float b = finiteOrDefault(colorLHS.b / colorRHS.b, 0.0F);
		
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
		final float r = finiteOrDefault(colorLHS.r / scalarRHS, 0.0F);
		final float g = finiteOrDefault(colorLHS.g / scalarRHS, 0.0F);
		final float b = finiteOrDefault(colorLHS.b / scalarRHS, 0.0F);
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a new {@code Color3F} instance with the result of the conversion from RGBE to RGB.
	 * 
	 * @param colorRGBE an {@code int} with a color in the RGBE format
	 * @return a new {@code Color3F} instance with the result of the conversion from RGBE to RGB
	 */
//	TODO: Add Unit Tests!
	public static Color3F fromRGBE(final int colorRGBE) {
		final float exponent = R_G_B_EXPONENTS[colorRGBE & 0xFF];
		
		final float r = exponent * ((colorRGBE >>> 24)         + 0.5F);
		final float g = exponent * (((colorRGBE >> 16) & 0xFF) + 0.5F);
		final float b = exponent * (((colorRGBE >>  8) & 0xFF) + 0.5F);
		
		return new Color3F(r, g, b);
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
	 * Returns a grayscale {@code Color3F} instance based on {@code color.getComponent1()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.getComponent1()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleComponent1(final Color3F color) {
		return new Color3F(color.r);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.getComponent2()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.getComponent2()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleComponent2(final Color3F color) {
		return new Color3F(color.g);
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.getComponent3()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.getComponent3()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleComponent3(final Color3F color) {
		return new Color3F(color.b);
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
	 * Returns a grayscale {@code Color3F} instance based on {@code color.luminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.luminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleLuminance(final Color3F color) {
		return new Color3F(color.luminance());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.maximum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.maximum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleMaximum(final Color3F color) {
		return new Color3F(color.maximum());
	}
	
	/**
	 * Returns a grayscale {@code Color3F} instance based on {@code color.minimum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a grayscale {@code Color3F} instance based on {@code color.minimum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F grayscaleMinimum(final Color3F color) {
		return new Color3F(color.minimum());
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
		final float r = 1.0F - color.r;
		final float g = 1.0F - color.g;
		final float b = 1.0F - color.b;
		
		return new Color3F(r, g, b);
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
	public static Color3F maximum(final Color3F colorA, final Color3F colorB) {
		final float r = max(colorA.r, colorB.r);
		final float g = max(colorA.g, colorB.g);
		final float b = max(colorA.b, colorB.b);
		
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
	 * If at least one of the component values are negative, consider calling {@link #minimumTo0(Color3F)} before calling this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3F a = new Color3F(0.0F, 1.0F, 2.0F);
	 * Color3F b = Color3F.maximumTo1(a);
	 * 
	 * //a.getComponent1() = 0.0F, a.getComponent2() = 1.0F, a.getComponent3() = 2.0F
	 * //b.getComponent1() = 0.0F, b.getComponent2() = 0.5F, b.getComponent3() = 1.0F
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the division, or {@code color} if no division occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F maximumTo1(final Color3F color) {
		final float maximum = color.maximum();
		
		if(maximum > 1.0F) {
			final float r = color.r / maximum;
			final float g = color.g / maximum;
			final float b = color.b / maximum;
			
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
	public static Color3F minimum(final Color3F colorA, final Color3F colorB) {
		final float r = min(colorA.r, colorB.r);
		final float g = min(colorA.g, colorB.g);
		final float b = min(colorA.b, colorB.b);
		
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
	 * Consider calling {@link #maximumTo1(Color3F)} after a call to this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3F a = new Color3F(-2.0F, 0.0F, 1.0F);
	 * Color3F b = Color3F.minimumTo0(a);
	 * 
	 * //a.getComponent1() = -2.0F, a.getComponent2() = 0.0F, a.getComponent3() = 1.0F
	 * //b.getComponent1() =  0.0F, b.getComponent2() = 2.0F, b.getComponent3() = 3.0F
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the addition, or {@code color} if no addition occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F minimumTo0(final Color3F color) {
		final float minimum = color.minimum();
		
		if(minimum < 0.0F) {
			final float r = color.r + -minimum;
			final float g = color.g + -minimum;
			final float b = color.b + -minimum;
			
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
		final float r = max(colorLHS.r * scalarRHS, 0.0F);
		final float g = max(colorLHS.g * scalarRHS, 0.0F);
		final float b = max(colorLHS.b * scalarRHS, 0.0F);
		
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
	 * Normalizes the component values of {@code color} based on its luminance.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F normalizeLuminance(final Color3F color) {
		final float luminance = color.luminance();
		
		if(luminance > 0.0F) {
			return divide(color, luminance);
		}
		
		return WHITE;
	}
	
	/**
	 * Returns a {@code Color3F} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(Floats.random(), Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with random component values
	 */
	public static Color3F random() {
		final float r = Floats.random();
		final float g = Floats.random();
		final float b = Floats.random();
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with a random component 1 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(Floats.random(), 0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with a random component 1 value
	 */
	public static Color3F randomComponent1() {
		final float r = Floats.random();
		final float g = 0.0F;
		final float b = 0.0F;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with a random component 2 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(0.0F, Floats.random(), 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with a random component 2 value
	 */
	public static Color3F randomComponent2() {
		final float r = 0.0F;
		final float g = Floats.random();
		final float b = 0.0F;
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color3F} instance with a random component 3 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(0.0F, 0.0F, Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3F} instance with a random component 3 value
	 */
	public static Color3F randomComponent3() {
		final float r = 0.0F;
		final float g = 0.0F;
		final float b = Floats.random();
		
		return new Color3F(r, g, b);
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
	 * Saturates or clamps {@code color} to the range {@code [Floats.min(edgeA, edgeB), Floats.max(edgeA, edgeB)]}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @param edgeA the minimum or maximum component value
	 * @param edgeB the maximum or minimum component value
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F saturate(final Color3F color, final float edgeA, final float edgeB) {
		final float r = Floats.saturate(color.r, edgeA, edgeB);
		final float g = Floats.saturate(color.g, edgeA, edgeB);
		final float b = Floats.saturate(color.b, edgeA, edgeB);
		
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
	 * Color3F.toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, Floats.MIN_VALUE);
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
//	TODO: Add Unit Tests!
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e) {
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, MIN_VALUE);
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
//	TODO: Add Unit Tests!
	public static Color3F toneMapFilmicCurve(final Color3F color, final float exposure, final float a, final float b, final float c, final float d, final float e, final float subtract, final float minimum) {
		final float r1 = max(color.r * exposure - subtract, minimum);
		final float g1 = max(color.g * exposure - subtract, minimum);
		final float b1 = max(color.b * exposure - subtract, minimum);
		
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static Color3F toneMapReinhardModifiedVersion2(final Color3F color, final float exposure) {
		final float r1 = color.r * exposure;
		final float g1 = color.g * exposure;
		final float b1 = color.b * exposure;
		
		final float r2 = 1.0F - exp(-r1 * exposure);
		final float g2 = 1.0F - exp(-g1 * exposure);
		final float b2 = 1.0F - exp(-b1 * exposure);
		
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
//	TODO: Add Unit Tests!
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
	 * Color3F.array(length, () -> Color3F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3F[] array(final int length) {
		return array(length, () -> BLACK);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F} instances supplied by {@code supplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code supplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param supplier a {@code Supplier}
	 * @return a {@code Color3F[]} with a length of {@code length} and contains {@code Color3F} instances supplied by {@code supplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code supplier} is {@code null}
	 */
	public static Color3F[] array(final int length, final Supplier<Color3F> supplier) {
		final Color3F[] colors = new Color3F[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(supplier, "supplier == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(supplier.get());
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
	 * Color3F.array(length, () -> Color3F.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3F[]} with a length of {@code length} and contains random {@code Color3F} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3F[] arrayRandom(final int length) {
		return array(length, () -> random());
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
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float[] doCreateRGBExponents() {
		final float[] exponents = new float[256];
		
		exponents[0] = 0.0F;
		
		for(int i = 1; i < exponents.length; i++) {
			final int exponentInt = i - (128 + 8);
			
			float exponentFloat = 1.0F;
			
			if(exponentInt > 0) {
				for(int j = 0; j < +exponentInt; j++) {
					exponentFloat *= 2.0F;
				}
			} else {
				for(int j = 0; j < -exponentInt; j++) {
					exponentFloat *= 0.5F;
				}
			}
			
			exponents[i] = exponentFloat;
		}
		
		return exponents;
	}
}