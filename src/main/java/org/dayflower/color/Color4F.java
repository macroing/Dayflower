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

import static org.dayflower.utility.Floats.equal;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

import org.macroing.art4j.color.ArrayComponentOrder;
import org.macroing.art4j.color.PackedIntComponentOrder;
import org.macroing.java.lang.Strings;

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
	 * A {@code Color4F} denoting the color black.
	 */
	public static final Color4F BLACK = new Color4F();
	
	/**
	 * A {@code Color4F} denoting the color blue.
	 */
	public static final Color4F BLUE = new Color4F(0.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code Color4F} denoting the color cyan.
	 */
	public static final Color4F CYAN = new Color4F(0.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code Color4F} denoting the color green.
	 */
	public static final Color4F GREEN = new Color4F(0.0F, 1.0F, 0.0F);
	
	/**
	 * A {@code Color4F} denoting the color magenta.
	 */
	public static final Color4F MAGENTA = new Color4F(1.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code Color4F} denoting the color orange.
	 */
	public static final Color4F ORANGE = new Color4F(1.0F, 0.5F, 0.0F);
	
	/**
	 * A {@code Color4F} denoting the color red.
	 */
	public static final Color4F RED = new Color4F(1.0F, 0.0F, 0.0F);
	
	/**
	 * A {@code Color4F} denoting a transparent color.
	 */
	public static final Color4F TRANSPARENT = new Color4F(0.0F, 0.0F, 0.0F, 0.0F);
	
	/**
	 * A {@code Color4F} denoting the color white.
	 */
	public static final Color4F WHITE = new Color4F(1.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code Color4F} denoting the color yellow.
	 */
	public static final Color4F YELLOW = new Color4F(1.0F, 1.0F, 0.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final Map<Color4F, Color4F> CACHE = new HashMap<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * The A-component of this {@code Color4F} instance.
	 */
	public final float a;
	
	/**
	 * The B-component of this {@code Color4F} instance.
	 */
	public final float b;
	
	/**
	 * The G-component of this {@code Color4F} instance.
	 */
	public final float g;
	
	/**
	 * The R-component of this {@code Color4F} instance.
	 */
	public final float r;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color4F} instance denoting the color black.
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
	 * @param a the value of component 4
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3F color, final float a) {
		this(color.r, color.g, color.b, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 */
	public Color4F(final float component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(component, component, component, a);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 * @param a the value of component 4
	 */
	public Color4F(final float component, final float a) {
		this(component, component, component, a);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code r}, {@code g}, {@code b} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(r, g, b, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color4F(final float r, final float g, final float b) {
		this(r, g, b, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code r}, {@code g}, {@code b} and {@code a}.
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 * @param a the value of component 4
	 */
	public Color4F(final float r, final float g, final float b, final float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Constructs a new {@code Color4F} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of component 1, component 2 and component 3
	 */
	public Color4F(final int component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code r}, {@code g}, {@code b} and {@code 255}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(r, g, b, 255);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 */
	public Color4F(final int r, final int g, final int b) {
		this(r, g, b, 255);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code r}, {@code g}, {@code b} and {@code a}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(Ints.saturate(r) / 255.0F, Ints.saturate(g) / 255.0F, Ints.saturate(b) / 255.0F, Ints.saturate(a) / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param r the value of component 1
	 * @param g the value of component 2
	 * @param b the value of component 3
	 * @param a the value of component 4
	 */
	public Color4F(final int r, final int g, final int b, final int a) {
		this(Ints.saturate(r) / 255.0F, Ints.saturate(g) / 255.0F, Ints.saturate(b) / 255.0F, Ints.saturate(a) / 255.0F);
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
	 * Returns {@code true} if, and only if, {@code color} is an instance of {@code Color4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param color the {@code Color4F} to compare to this {@code Color4F} instance for equality
	 * @return {@code true} if, and only if, {@code color} is an instance of {@code Color4F}, and their respective values are equal, {@code false} otherwise
	 */
	public boolean equals(final Color4F color) {
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
	 * Compares {@code object} to this {@code Color4F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color4F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color4F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color4F}, and their respective values are equal, {@code false} otherwise
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
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return isInfinite(this.r) || isInfinite(this.g) || isInfinite(this.b) || isInfinite(this.a);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return isNaN(this.r) || isNaN(this.g) || isNaN(this.b) || isNaN(this.a);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is black if, and only if, the component values of component 1, component 2 and component 3 are {@code 0.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isGrayscale() && isZero(this.r);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is considered blue if, and only if, {@code color.b - thresholdR >= color.r} and {@code color.b - thresholdG >= color.g}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue(final float thresholdR, final float thresholdG) {
		return this.b - thresholdR >= this.r && this.b - thresholdG >= this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return equal(this.g, this.b) && this.r < this.g;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is a grayscale color if, and only if, the component values of component 1, component 2 and component 3 are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is a grayscale color, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return equal(this.r, this.g, this.b);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered green, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is considered green if, and only if, {@code color.g - thresholdR >= color.r} and {@code color.g - thresholdB >= color.b}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen(final float thresholdR, final float thresholdB) {
		return this.g - thresholdR >= this.r && this.g - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return equal(this.r, this.b) && this.g < this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0F, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0F, 1.0F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered red, {@code false} otherwise.
	 * <p>
	 * The {@code Color4F} instance {@code color} is considered red if, and only if, {@code color.r - thresholdG >= color.g} and {@code color.r - thresholdB >= color.b}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed(final float thresholdG, final float thresholdB) {
		return this.r - thresholdG >= this.g && this.r - thresholdB >= this.b;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is white if, and only if, the component values of component 1, component 2 and component 3 are equal and greater than or equal to {@code 1.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isGrayscale() && this.r >= 1.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return equal(this.r, this.g) && this.b < this.r;
	}
	
	/**
	 * Returns the average component value of this {@code Color4F} instance.
	 * <p>
	 * The average component value is computed using component 1, component 2 and component 3.
	 * 
	 * @return the average component value of this {@code Color4F} instance
	 */
//	TODO: Add Unit Tests!
	public float average() {
		return (this.r + this.g + this.b) / 3.0F;
	}
	
	/**
	 * Returns the lightness of this {@code Color4F} instance.
	 * <p>
	 * The lightness is computed using component 1, component 2 and component 3.
	 * 
	 * @return the lightness of this {@code Color4F} instance
	 */
//	TODO: Add Unit Tests!
	public float lightness() {
		return (maximum() + minimum()) / 2.0F;
	}
	
	/**
	 * Returns the relative luminance of this {@code Color4F} instance.
	 * <p>
	 * The relative luminance is computed using component 1, component 2 and component 3.
	 * <p>
	 * The algorithm used is only suitable for linear {@code Color4F} instances.
	 * 
	 * @return the relative luminance of this {@code Color4F} instance
	 */
//	TODO: Add Unit Tests!
	public float luminance() {
		return this.r * 0.212671F + this.g * 0.715160F + this.b * 0.072169F;
	}
	
	/**
	 * Returns the largest component value of component 1, component 2 and component 3.
	 * 
	 * @return the largest component value of component 1, component 2 and component 3
	 */
//	TODO: Add Unit Tests!
	public float maximum() {
		return max(this.r, this.g, this.b);
	}
	
	/**
	 * Returns the smallest component value of component 1, component 2 and component 3.
	 * 
	 * @return the smallest component value of component 1, component 2 and component 3
	 */
//	TODO: Add Unit Tests!
	public float minimum() {
		return min(this.r, this.g, this.b);
	}
	
	/**
	 * Returns a hash code for this {@code Color4F} instance.
	 * 
	 * @return a hash code for this {@code Color4F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.r), Float.valueOf(this.g), Float.valueOf(this.b), Float.valueOf(this.a));
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
		final int r = toInt(Floats.saturate(this.r) * 255.0F + 0.5F);
		final int g = toInt(Floats.saturate(this.g) * 255.0F + 0.5F);
		final int b = toInt(Floats.saturate(this.b) * 255.0F + 0.5F);
		final int a = toInt(Floats.saturate(this.a) * 255.0F + 0.5F);
		
		return packedIntComponentOrder.pack(r, g, b, a);
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @param tComponent4 the factor to use for component 4 in the blending process
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F blend(final Color4F colorLHS, final Color4F colorRHS, final float tComponent1, final float tComponent2, final float tComponent3, final float tComponent4) {
		final float r = lerp(colorLHS.r, colorRHS.r, tComponent1);
		final float g = lerp(colorLHS.g, colorRHS.g, tComponent2);
		final float b = lerp(colorLHS.b, colorRHS.b, tComponent3);
		final float a = lerp(colorLHS.a, colorRHS.a, tComponent4);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Blends the component values of {@code colorA} over the component values of {@code colorB}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the blend.
	 * <p>
	 * If either {@code colorA} or {@code colorB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param colorA a {@code Color4F} instance
	 * @param colorB a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorA} or {@code colorB} are {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F blendOver(final Color4F colorA, final Color4F colorB) {
		final float a = colorA.a + colorB.a * (1.0F - colorA.a);
		final float r = (colorA.r * colorA.a + colorB.r * colorB.a * (1.0F - colorA.a)) / a;
		final float g = (colorA.g * colorA.a + colorB.g * colorB.a * (1.0F - colorA.a)) / a;
		final float b = (colorA.b * colorA.a + colorB.b * colorB.a * (1.0F - colorA.a)) / a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a cached version of {@code color}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a cached version of {@code color}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F getCached(final Color4F color) {
		return CACHE.computeIfAbsent(Objects.requireNonNull(color, "color == null"), key -> color);
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
//	TODO: Add Unit Tests!
	public static Color4F grayscaleAverage(final Color4F color) {
		return new Color4F(color.average(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.getComponent1()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.getComponent1()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleComponent1(final Color4F color) {
		return new Color4F(color.r, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.getComponent2()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.getComponent2()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleComponent2(final Color4F color) {
		return new Color4F(color.g, color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.getComponent3()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.getComponent3()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleComponent3(final Color4F color) {
		return new Color4F(color.b, color.a);
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
//	TODO: Add Unit Tests!
	public static Color4F grayscaleLightness(final Color4F color) {
		return new Color4F(color.lightness(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.luminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.luminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleLuminance(final Color4F color) {
		return new Color4F(color.luminance(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.maximum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.maximum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleMaximum(final Color4F color) {
		return new Color4F(color.maximum(), color.a);
	}
	
	/**
	 * Returns a grayscale {@code Color4F} instance based on {@code color.minimum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a grayscale {@code Color4F} instance based on {@code color.minimum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F grayscaleMinimum(final Color4F color) {
		return new Color4F(color.minimum(), color.a);
	}
	
	/**
	 * Inverts the component values of {@code color}.
	 * <p>
	 * Returns a new {@code Color4F} instance with the result of the inversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color4F} instance
	 * @return a new {@code Color4F} instance with the result of the inversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F invert(final Color4F color) {
		final float r = 1.0F - color.r;
		final float g = 1.0F - color.g;
		final float b = 1.0F - color.b;
		final float a =        color.a;
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(Floats.random(), Floats.random(), Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with random component values
	 */
//	TODO: Add Unit Tests!
	public static Color4F random() {
		final float r = Floats.random();
		final float g = Floats.random();
		final float b = Floats.random();
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with a random component 1 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(Floats.random(), 0.0F, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with a random component 1 value
	 */
//	TODO: Add Unit Tests!
	public static Color4F randomComponent1() {
		final float r = Floats.random();
		final float g = 0.0F;
		final float b = 0.0F;
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with a random component 2 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(0.0F, Floats.random(), 0.0F);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with a random component 2 value
	 */
//	TODO: Add Unit Tests!
	public static Color4F randomComponent2() {
		final float r = 0.0F;
		final float g = Floats.random();
		final float b = 0.0F;
		
		return new Color4F(r, g, b);
	}
	
	/**
	 * Returns a {@code Color4F} instance with a random component 3 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(0.0F, 0.0F, Floats.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color4F} instance with a random component 3 value
	 */
//	TODO: Add Unit Tests!
	public static Color4F randomComponent3() {
		final float r = 0.0F;
		final float g = 0.0F;
		final float b = Floats.random();
		
		return new Color4F(r, g, b);
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static Color4F sepia(final Color4F color) {
		final float r = color.r * 0.393F + color.g * 0.769F + color.b * 0.189F;
		final float g = color.r * 0.349F + color.g * 0.686F + color.b * 0.168F;
		final float b = color.r * 0.272F + color.g * 0.534F + color.b * 0.131F;
		final float a = color.a;
		
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static Color4F unpack(final int color, final PackedIntComponentOrder packedIntComponentOrder) {
		final int r = packedIntComponentOrder.unpackR(color);
		final int g = packedIntComponentOrder.unpackG(color);
		final int b = packedIntComponentOrder.unpackB(color);
		final int a = packedIntComponentOrder.unpackA(color);
		
		return new Color4F(r, g, b, a);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F.BLACK}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.array(length, () -> Color4F.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static Color4F[] array(final int length) {
		return array(length, () -> BLACK);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F} instances supplied by {@code supplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code supplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param supplier a {@code Supplier}
	 * @return a {@code Color4F[]} with a length of {@code length} and contains {@code Color4F} instances supplied by {@code supplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code supplier} is {@code null}
	 */
//	TODO: Add Unit Tests!
	public static Color4F[] array(final int length, final Supplier<Color4F> supplier) {
		final Color4F[] colors = new Color4F[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(supplier, "supplier == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(supplier.get());
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
	 * Color4F.array(length, () -> Color4F.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color4F[]} with a length of {@code length} and contains random {@code Color4F} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
//	TODO: Add Unit Tests!
	public static Color4F[] arrayRandom(final int length) {
		return array(length, () -> random());
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static Color4F[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
	public static Color4F[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
//	TODO: Add Unit Tests!
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
//	TODO: Add Unit Tests!
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