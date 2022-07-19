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

import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.lerp;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.toDouble;
import static org.dayflower.utility.Ints.toInt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.utility.Doubles;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

import org.macroing.java.lang.Strings;

/**
 * A {@code Color4D} represents a color with four {@code double}-based components.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color4D {
	/**
	 * A {@code Color4D} denoting the color black.
	 */
	public static final Color4D BLACK = new Color4D();
	
	/**
	 * A {@code Color4D} denoting the color blue.
	 */
	public static final Color4D BLUE = new Color4D(0.0D, 0.0D, 1.0D);
	
	/**
	 * A {@code Color4D} denoting the color cyan.
	 */
	public static final Color4D CYAN = new Color4D(0.0D, 1.0D, 1.0D);
	
	/**
	 * A {@code Color4D} denoting the color green.
	 */
	public static final Color4D GREEN = new Color4D(0.0D, 1.0D, 0.0D);
	
	/**
	 * A {@code Color4D} denoting the color magenta.
	 */
	public static final Color4D MAGENTA = new Color4D(1.0D, 0.0D, 1.0D);
	
	/**
	 * A {@code Color4D} denoting the color orange.
	 */
	public static final Color4D ORANGE = new Color4D(1.0D, 0.5D, 0.0D);
	
	/**
	 * A {@code Color4D} denoting the color red.
	 */
	public static final Color4D RED = new Color4D(1.0D, 0.0D, 0.0D);
	
	/**
	 * A {@code Color4D} denoting a transparent color.
	 */
	public static final Color4D TRANSPARENT = new Color4D(0.0D, 0.0D, 0.0D, 0.0D);
	
	/**
	 * A {@code Color4D} denoting the color white.
	 */
	public static final Color4D WHITE = new Color4D(1.0D, 1.0D, 1.0D);
	
	/**
	 * A {@code Color4D} denoting the color yellow.
	 */
	public static final Color4D YELLOW = new Color4D(1.0D, 1.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color4D, Color4D> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The A-component of this {@code Color4D} instance.
	 */
	public final double a;
	
	/**
	 * The B-component of this {@code Color4D} instance.
	 */
	public final double b;
	
	/**
	 * The G-component of this {@code Color4D} instance.
	 */
	public final double g;
	
	/**
	 * The R-component of this {@code Color4D} instance.
	 */
	public final double r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color4D} instance denoting the color black.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(0.0D);
	 * }
	 * </pre>
	 */
	public Color4D() {
		this(0.0D);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4D(final Color3D color) {
		this(color.r, color.g, color.b);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3D} instance
	 * @param a the value of component 4
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4D(final Color3D color, final double a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4D(final Color3F color) {
		this(toDouble(color.r), toDouble(color.g), toDouble(color.b));
	}
	
	/**
	 * Constructs a new {@code Color4D} instance from {@code color} and {@code a}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color3F} instance
	 * @param a the value of component 4
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4D(final Color3F color, final double a) {
		this(toDouble(color.r), toDouble(color.g), toDouble(color.b), a);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance from {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@link Color4F} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public Color4D(final Color4F color) {
		this(toDouble(color.r), toDouble(color.g), toDouble(color.b), toDouble(color.a));
	}
	
	/**
	 * Constructs a new {@code Color4D} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 */
	public Color4D(final double component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(component, component, component, a);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 * @param a the value of component 4
	 */
	public Color4D(final double component, final double a) {
		this(component, component, component, a);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance given the component values {@code r}, {@code g}, {@code b} and {@code 1.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(r, g, b, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color4D(final double r, final double g, final double b) {
		this(r, g, b, 1.0D);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance given the component values {@code r}, {@code g}, {@code b} and {@code a}.
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 * @param a the value of component 4
	 */
	public Color4D(final double r, final double g, final double b, final double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Constructs a new {@code Color4D} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 */
	public Color4D(final int component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance given the component values {@code r}, {@code g}, {@code b} and {@code 255}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(r, g, b, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color4D(final int r, final int g, final int b) {
		this(r, g, b, 255);
	}
	
	/**
	 * Constructs a new {@code Color4D} instance given the component values {@code r}, {@code g}, {@code b} and {@code a}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(Ints.saturate(r) / 255.0D, Ints.saturate(g) / 255.0D, Ints.saturate(b) / 255.0D, Ints.saturate(a) / 255.0D);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 * @param a the value of component 4
	 */
	public Color4D(final int r, final int g, final int b, final int a) {
		this(Ints.saturate(r) / 255.0D, Ints.saturate(g) / 255.0D, Ints.saturate(b) / 255.0D, Ints.saturate(a) / 255.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color4D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color4D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color4D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(this.r), Strings.toNonScientificNotationJava(this.g), Strings.toNonScientificNotationJava(this.b), Strings.toNonScientificNotationJava(this.a));
	}
	
	/**
	 * Compares {@code color} to this {@code Color4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code color} is an instance of {@code Color4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param color the {@code Color4D} to compare to this {@code Color4D} instance for equality
	 * @return {@code true} if, and only if, {@code color} is an instance of {@code Color4D}, and their respective values are equal, {@code false} otherwise
	 */
	public boolean equals(final Color4D color) {
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
		} else if(!equal(this.a, color.a)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Compares {@code object} to this {@code Color4D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color4D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color4D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color4D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color4D)) {
			return false;
		} else {
			return equals(Color4D.class.cast(object));
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4D} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4D} instance is infinite, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean hasInfinites() {
		return isInfinite(this.r) || isInfinite(this.g) || isInfinite(this.b) || isInfinite(this.a);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4D} instance is equal to {@code Double.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4D} instance is equal to {@code Double.NaN}, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean hasNaNs() {
		return isNaN(this.r) || isNaN(this.g) || isNaN(this.b) || isNaN(this.a);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color4D} instance is black if, and only if, the component values of component 1, component 2 and component 3 are {@code 0.0D}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is black, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isBlack() {
		return isGrayscale() && isZero(this.r);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered blue, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isBlue() {
		return isBlue(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color4D} instance {@code color} is considered blue if, and only if, {@code color.b - thresholdR >= color.r} and {@code color.b - thresholdG >= color.g}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered blue, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isBlue(final double thresholdR, final double thresholdG) {
		return this.b - thresholdR >= this.r && this.b - thresholdG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered cyan, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isCyan() {
		return equal(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color4D} instance is a grayscale color if, and only if, the component values of component 1, component 2 and component 3 are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is a grayscale color, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isGrayscale() {
		return equal(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered green, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isGreen() {
		return isGreen(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered green, {@code false} otherwise.
	 * <p>
	 * The {@code Color4D} instance {@code color} is considered green if, and only if, {@code color.g - thresholdR >= color.r} and {@code color.g - thresholdB >= color.b}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered green, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isGreen(final double thresholdR, final double thresholdB) {
		return this.g - thresholdR >= this.r && this.g - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered magenta, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isMagenta() {
		return equal(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered red, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isRed() {
		return isRed(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered red, {@code false} otherwise.
	 * <p>
	 * The {@code Color4D} instance {@code color} is considered red if, and only if, {@code color.r - thresholdG >= color.g} and {@code color.r - thresholdB >= color.b}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered red, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isRed(final double thresholdG, final double thresholdB) {
		return this.r - thresholdG >= this.g && this.r - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color4D} instance is white if, and only if, the component values of component 1, component 2 and component 3 are equal and greater than or equal to {@code 1.0D}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is white, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isWhite() {
		return isGrayscale() && this.r >= 1.0D;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4D} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4D} instance is considered yellow, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	public boolean isYellow() {
		return equal(this.r, this.g) && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of this {@code Color4D} instance.
	 * <p>
	 * The average component value is computed using component 1, component 2 and component 3.
	 * 
	 * @return the average component value of this {@code Color4D} instance
	 */
//	TODO: Add Unit Tests!
	public double average() {
		return (this.r + this.g + this.b) / 3.0D;
	}
	
	/**
	 * Returns the lightness of this {@code Color4D} instance.
	 * <p>
	 * The lightness is computed using component 1, component 2 and component 3.
	 * 
	 * @return the lightness of this {@code Color4D} instance
	 */
//	TODO: Add Unit Tests!
	public double lightness() {
		return (maximum() + minimum()) / 2.0D;
	}
	
	/**
	 * Returns the relative luminance of this {@code Color4D} instance.
	 * <p>
	 * The relative luminance is computed using component 1, component 2 and component 3.
	 * <p>
	 * The algorithm used is only suitable for linear {@code Color4D} instances.
	 * 
	 * @return the relative luminance of this {@code Color4D} instance
	 */
//	TODO: Add Unit Tests!
	public double luminance() {
		return this.r * 0.212671D + this.g * 0.715160D + this.b * 0.072169D;
	}
	
	/**
	 * Returns the largest component value of component 1, component 2 and component 3.
	 * 
	 * @return the largest component value of component 1, component 2 and component 3
	 */
//	TODO: Add Unit Tests!
	public double maximum() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of component 1, component 2 and component 3.
	 * 
	 * @return the smallest component value of component 1, component 2 and component 3
	 */
//	TODO: Add Unit Tests!
	public double minimum() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color4D} instance.
	 * 
	 * @return a hash code for this {@code Color4D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.r), Double.valueOf(this.g), Double.valueOf(this.b), Double.valueOf(this.a));
	}
	
	/**
	 * Returns an {@code int} with the component values in a packed form.
	 * <p>
	 * This method assumes that the component values are within the range [0.0, 1.0]. Any component value outside of this range will be saturated or clamped.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color4D.pack(PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @return an {@code int} with the component values in a packed form
	 */
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public int pack(final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = toInt(Doubles.saturate(this.r) * 255.0D + 0.5D);
		final int g = toInt(Doubles.saturate(this.g) * 255.0D + 0.5D);
		final int b = toInt(Doubles.saturate(this.b) * 255.0D + 0.5D);
		final int a = toInt(Doubles.saturate(this.a) * 255.0D + 0.5D);
		
		return packedIntComponentOrder.pack(r, g, b, a);
	}
	
	/**
	 * Writes this {@code Color4D} instance to {@code dataOutput}.
	 * <p>
	 * If {@code dataOutput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataOutput the {@code DataOutput} instance to write to
	 * @throws NullPointerException thrown if, and only if, {@code dataOutput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public void write(final DataOutput dataOutput) {
		try {
			dataOutput.writeDouble(this.r);
			dataOutput.writeDouble(this.g);
			dataOutput.writeDouble(this.b);
			dataOutput.writeDouble(this.a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.blend(colorLHS, colorRHS, 0.5D);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4D} instance on the left-hand side
	 * @param colorRHS the {@code Color4D} instance on the right-hand side
	 * @return a new {@code Color4D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D blend(final Color4D colorLHS, final Color4D colorRHS) {
		return blend(colorLHS, colorRHS, 0.5D);
	}
	
	/**
	 * Blends the component values of {@code color11}, {@code color12}, {@code color21} and {@code color22}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the blend.
	 * <p>
	 * If either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.blend(Color4D.blend(color11, color12, tX), Color4D.blend(color21, color22, tX), tY);
	 * }
	 * </pre>
	 * 
	 * @param color11 the {@code Color4D} instance on row 1 and column 1
	 * @param color12 the {@code Color4D} instance on row 1 and column 2
	 * @param color21 the {@code Color4D} instance on row 2 and column 1
	 * @param color22 the {@code Color4D} instance on row 2 and column 2
	 * @param tX the factor to use for all components in the first and second blend operation
	 * @param tY the factor to use for all components in the third blend operation
	 * @return a new {@code Color4D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code color11}, {@code color12}, {@code color21} or {@code color22} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D blend(final Color4D color11, final Color4D color12, final Color4D color21, final Color4D color22, final double tX, final double tY) {
		return blend(blend(color11, color12, tX), blend(color21, color22, tX), tY);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.blend(colorLHS, colorRHS, t, t, t, t);
	 * }
	 * </pre>
	 * 
	 * @param colorLHS the {@code Color4D} instance on the left-hand side
	 * @param colorRHS the {@code Color4D} instance on the right-hand side
	 * @param t the factor to use for all components in the blending process
	 * @return a new {@code Color4D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D blend(final Color4D colorLHS, final Color4D colorRHS, final double t) {
		return blend(colorLHS, colorRHS, t, t, t, t);
	}
	
	/**
	 * Blends the component values of {@code colorLHS} and {@code colorRHS}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorLHS the {@code Color4D} instance on the left-hand side
	 * @param colorRHS the {@code Color4D} instance on the right-hand side
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code Color4D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D blend(final Color4D colorLHS, final Color4D colorRHS, final double tComponent1, final double tComponent2, final double tComponent3, final double tComponent4) {
		final double r = lerp(colorLHS.r, colorRHS.r, tComponent1);
		final double g = lerp(colorLHS.g, colorRHS.g, tComponent2);
		final double b = lerp(colorLHS.b, colorRHS.b, tComponent3);
		final double a = lerp(colorLHS.a, colorRHS.a, tComponent4);
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorA} over the component values of {@code colorB}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the blend.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color4D} instance
	 * @param colorB a {@code Color4D} instance
	 * @return a new {@code Color4D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D blendOver(final Color4D colorA, final Color4D colorB) {
		final double a = colorA.a + colorB.a * (1.0D - colorA.a);
		final double r = (colorA.r * colorA.a + colorB.r * colorB.a * (1.0D - colorA.a)) / a;
		final double g = (colorA.g * colorA.a + colorB.g * colorB.a * (1.0D - colorA.a)) / a;
		final double b = (colorA.b * colorA.a + colorB.b * colorB.a * (1.0D - colorA.a)) / a;
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Returns a cached version of {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a cached version of {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D getCached(final Color4D color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.average()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.average()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleAverage(final Color4D color) {
		return new Color4D(color.average(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.getComponent1()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.getComponent1()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleComponent1(final Color4D color) {
		return new Color4D(color.r, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.getComponent2()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.getComponent2()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleComponent2(final Color4D color) {
		return new Color4D(color.g, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.getComponent3()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.getComponent3()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleComponent3(final Color4D color) {
		return new Color4D(color.b, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.lightness()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.lightness()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleLightness(final Color4D color) {
		return new Color4D(color.lightness(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.luminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.luminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleLuminance(final Color4D color) {
		return new Color4D(color.luminance(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.maximum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.maximum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleMaximum(final Color4D color) {
		return new Color4D(color.maximum(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4D} instance based on {@code color.minimum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a grayscale {@code Color4D} instance based on {@code color.minimum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D grayscaleMinimum(final Color4D color) {
		return new Color4D(color.minimum(), color.a);
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a new {@code Color4D} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D invert(final Color4D color) {
		final double r = 1.0D - color.r;
		final double g = 1.0D - color.g;
		final double b = 1.0D - color.b;
		final double a =        color.a;
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4D} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(Doubles.random(), Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4D} instance with random component values
	 */
//	TODO: Add Unit Tests!
	public static Color4D random() {
		final double r = Doubles.random();
		final double g = Doubles.random();
		final double b = Doubles.random();
		
		return new Color4D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4D} instance with a random component 1 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(Doubles.random(), 0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4D} instance with a random component 1 value
	 */
//	TODO: Add Unit Tests!
	public static Color4D randomComponent1() {
		final double r = Doubles.random();
		final double g = 0.0D;
		final double b = 0.0D;
		
		return new Color4D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4D} instance with a random component 2 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(0.0D, Doubles.random(), 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4D} instance with a random component 2 value
	 */
//	TODO: Add Unit Tests!
	public static Color4D randomComponent2() {
		final double r = 0.0D;
		final double g = Doubles.random();
		final double b = 0.0D;
		
		return new Color4D(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4D} instance with a random component 3 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4D(0.0D, 0.0D, Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4D} instance with a random component 3 value
	 */
//	TODO: Add Unit Tests!
	public static Color4D randomComponent3() {
		final double r = 0.0D;
		final double g = 0.0D;
		final double b = Doubles.random();
		
		return new Color4D(r, g, b);
	}
	
	/**
	 * Returns a new {@code Color4D} instance by reading it from {@code dataInput}.
	 * <p>
	 * If {@code dataInput} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If an I/O error occurs, an {@code UncheckedIOException} will be thrown.
	 * 
	 * @param dataInput the {@code DataInput} instance to read from
	 * @return a new {@code Color4D} instance by reading it from {@code dataInput}
	 * @throws NullPointerException thrown if, and only if, {@code dataInput} is {@code null}
	 * @throws UncheckedIOException thrown if, and only if, an I/O error occurs
	 */
//	TODO: Add Unit Tests!
	public static Color4D read(final DataInput dataInput) {
		try {
			final double r = dataInput.readDouble();
			final double g = dataInput.readDouble();
			final double b = dataInput.readDouble();
			final double a = dataInput.readDouble();
			
			return new Color4D(r, g, b, a);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Converts {@code color} to its sepia-representation.
	 * <p>
	 * Returns a new {@code Color4D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4D} instance
	 * @return a new {@code Color4D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D sepia(final Color4D color) {
		final double r = color.r * 0.393D + color.g * 0.769D + color.b * 0.189D;
		final double g = color.r * 0.349D + color.g * 0.686D + color.b * 0.168D;
		final double b = color.r * 0.272D + color.g * 0.534D + color.b * 0.131D;
		final double a = color.a;
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4D} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.unpack(color, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @return a {@code Color4D} instance by unpacking {@code color} using {@code PackedIntComponentOrder.ARGB}
	 */
//	TODO: Add Unit Tests!
	public static Color4D unpack(final int color) {
		return unpack(color, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color4D} instance by unpacking {@code color} using {@code packedIntComponentOrder}.
	 * <p>
	 * If {@code packedIntComponentOrder} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color an {@code int} with the color in packed form
	 * @param packedIntComponentOrder the {@link PackedIntComponentOrder} to unpack the component values with
	 * @return a {@code Color4D} instance by unpacking {@code color} using {@code packedIntComponentOrder}
	 * @throws NullPointerException thrown if, and only if, {@code packedIntComponentOrder} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D unpack(final int color, final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = packedIntComponentOrder.unpackR(color);
		final int g = packedIntComponentOrder.unpackG(color);
		final int b = packedIntComponentOrder.unpackB(color);
		final int a = packedIntComponentOrder.unpackA(color);
		
		return new Color4D(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code length} and contains {@code Color4D.BLACK}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.array(length, () -> Color4D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4D[]} with a length of {@code length} and contains {@code Color4D.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] array(final int length) {
		return array(length, () -> BLACK);
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code length} and contains {@code Color4D} instances supplied by {@code supplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code supplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param supplier a {@code Supplier}
	 * @return a {@code Color4D[]} with a length of {@code length} and contains {@code Color4D} instances supplied by {@code supplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code supplier} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] array(final int length, final Supplier<Color4D> supplier) {
		final Color4D[] colors = new Color4D[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(supplier, "supplier == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(supplier.get());
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code length} and contains random {@code Color4D} instances.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.array(length, () -> Color4D.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4D[]} with a length of {@code length} and contains random {@code Color4D} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayRandom(final int length) {
		return array(length, () -> random());
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4D} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.arrayRead(array, ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4D[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4D} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
		final Color4D[] colors = new Color4D[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readRAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readGAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readBAsInt(array, i * arrayComponentOrder.getComponentCount());
			final int a = arrayComponentOrder.readAAsInt(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color4D(r, g, b, a);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4D} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.arrayRead(array, ArrayComponentOrder.RGBA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4D[]} with a length of {@code array.length / ArrayComponentOrder.RGBA.getComponentCount()} and contains {@code Color4D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % ArrayComponentOrder.RGBA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.RGBA);
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4D} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4D[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4D} instances read from {@code array}
	 * @throws IllegalArgumentException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
		final Color4D[] colors = new Color4D[array.length / arrayComponentOrder.getComponentCount()];
		
		for(int i = 0; i < colors.length; i++) {
			final int r = arrayComponentOrder.readR(array, i * arrayComponentOrder.getComponentCount());
			final int g = arrayComponentOrder.readG(array, i * arrayComponentOrder.getComponentCount());
			final int b = arrayComponentOrder.readB(array, i * arrayComponentOrder.getComponentCount());
			final int a = arrayComponentOrder.readA(array, i * arrayComponentOrder.getComponentCount());
			
			colors[i] = new Color4D(r, g, b, a);
		}
		
		return colors;
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length} and contains {@code Color4D} instances unpacked from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4D.arrayUnpack(array, PackedIntComponentOrder.ARGB);
	 * }
	 * </pre>
	 * 
	 * @param array the array to unpack from
	 * @return a {@code Color4D[]} with a length of {@code array.length} and contains {@code Color4D} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayUnpack(final int[] array) {
		return arrayUnpack(array, PackedIntComponentOrder.ARGB);
	}
	
	/**
	 * Returns a {@code Color4D[]} with a length of {@code array.length} and contains {@code Color4D} instances unpacked from {@code array}.
	 * <p>
	 * If either {@code array} or {@code packedIntComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param array the array to unpack from
	 * @param packedIntComponentOrder a {@link PackedIntComponentOrder} instance
	 * @return a {@code Color4D[]} with a length of {@code array.length} and contains {@code Color4D} instances unpacked from {@code array}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code packedIntComponentOrder} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4D[] arrayUnpack(final int[] array, final PackedIntComponentOrder packedIntComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(packedIntComponentOrder, "packedIntComponentOrder == null");
		
		final Color4D[] colors = new Color4D[array.length];
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = unpack(array[i], packedIntComponentOrder);
		}
		
		return colors;
	}
	
	/**
	 * Returns the size of the cache.
	 * 
	 * @return the size of the cache
	 */
//	TODO: Add Unit Tests!
	public static int getCacheSize() {
		return CACHE.size();
	}
	
	/**
	 * Clears the cache.
	 */
//	TODO: Add Unit Tests!
	public static void clearCache() {
		CACHE.clear();
	}
}