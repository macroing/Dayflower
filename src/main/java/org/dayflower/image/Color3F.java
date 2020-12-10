/**
 * Copyright 2020 J&#246;rgen Lundgren
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
package org.dayflower.image;

import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.exp;
import static org.dayflower.util.Floats.finiteOrDefault;
import static org.dayflower.util.Floats.isInfinite;
import static org.dayflower.util.Floats.isNaN;
import static org.dayflower.util.Floats.isZero;
import static org.dayflower.util.Floats.lerp;
import static org.dayflower.util.Floats.max;
import static org.dayflower.util.Floats.min;
import static org.dayflower.util.Floats.pow;
import static org.dayflower.util.Floats.simplexFractionalBrownianMotionXY;
import static org.dayflower.util.Floats.toFloat;
import static org.dayflower.util.Ints.toInt;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.geometry.Point2F;
import org.dayflower.util.Floats;
import org.dayflower.util.Ints;
import org.dayflower.util.ParameterArguments;

/**
 * A {@code Color3F} encapsulates a color using the data type {@code float}.
 * <p>
 * This class is immutable and therefore suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Color3F {
	/**
	 * A {@code Color3F} denoting the color black.
	 */
	public static final Color3F BLACK = new Color3F();
	
	/**
	 * A {@code Color3F} denoting the color blue.
	 */
	public static final Color3F BLUE = new Color3F(0.0F, 0.0F, 1.0F);
	
	/**
	 * A {@code Color3F} denoting the color copper.
	 */
	public static final Color3F COPPER = new Color3F(0.72F, 0.45F, 0.2F);
	
	/**
	 * A {@code Color3F} denoting the color cyan.
	 */
	public static final Color3F CYAN = new Color3F(0.0F, 1.0F, 1.0F);
	
	/**
	 * A {@code Color3F} denoting the color Aztek gold.
	 */
	public static final Color3F GOLD_AZTEK = new Color3F(0.76F, 0.6F, 0.33F);
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for gold.
	 */
	public static final Color3F GOLD_ETA = convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_ETA.toColorXYZ());
	
	/**
	 * A {@code Color3F} that contains the eta, also called the index of refraction (IOR), for gold, but has a restricted range.
	 */
	public static final Color3F GOLD_ETA_MAXIMUM_TO_1 = maximumTo1(GOLD_ETA);
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for gold.
	 */
	public static final Color3F GOLD_K = convertXYZToRGBUsingPBRT(IrregularSpectralCurve.GOLD_K.toColorXYZ());
	
	/**
	 * A {@code Color3F} that contains the absorption coefficient for gold, but has a restricted range.
	 */
	public static final Color3F GOLD_K_MAXIMUM_TO_1 = maximumTo1(GOLD_K);
	
	/**
	 * A {@code Color3F} denoting the color metallic gold.
	 */
	public static final Color3F GOLD_METALLIC = new Color3F(0.83F, 0.69F, 0.22F);
	
	/**
	 * A {@code Color3F} denoting the color gray.
	 */
	public static final Color3F GRAY = new Color3F(0.5F, 0.5F, 0.5F);
	
	/**
	 * A {@code Color3F} denoting the color green.
	 */
	public static final Color3F GREEN = new Color3F(0.0F, 1.0F, 0.0F);
	
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
	
	private final float component1;
	private final float component2;
	private final float component3;
	
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
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color3F(final Color3D color) {
		this(toFloat(color.getComponent1()), toFloat(color.getComponent2()), toFloat(color.getComponent3()));
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
	 * Constructs a new {@code Color3F} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color3F(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
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
	 * Constructs a new {@code Color3F} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3F(Ints.saturate(component1) / 255.0F, Ints.saturate(component2) / 255.0F, Ints.saturate(component3) / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color3F(final int component1, final int component2, final int component3) {
		this(Ints.saturate(component1) / 255.0F, Ints.saturate(component2) / 255.0F, Ints.saturate(component3) / 255.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
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
		} else if(!equal(this.component1, Color3F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Color3F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Color3F.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return isInfinite(this.component1) || isInfinite(this.component2) || isInfinite(this.component3);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3F} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return isNaN(this.component1) || isNaN(this.component2) || isNaN(this.component3);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is black if all component values are {@code 0.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isGrayscale() && isZero(this.component1);
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
	 * The {@code Color3F} instance {@code color} is considered blue if, and only if, {@code color.getB() - thresholdR >= color.getR()} and {@code color.getB() - thresholdG >= color.getG()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue(final float thresholdR, final float thresholdG) {
		return getB() - thresholdR >= getR() && getB() - thresholdG >= getG();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return isGreen(1.0F, 0.5F) && isBlue(1.0F, 0.5F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is a grayscale color if all component values are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is a grayscale color, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return equal(this.component1, this.component2, this.component3);
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
	 * The {@code Color3F} instance {@code color} is considered green if, and only if, {@code color.getG() - thresholdR >= color.getR()} and {@code color.getG() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen(final float thresholdR, final float thresholdB) {
		return getG() - thresholdR >= getR() && getG() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return isRed(1.0F, 0.5F) && isBlue(0.5F, 1.0F);
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
	 * The {@code Color3F} instance {@code color} is considered red if, and only if, {@code color.getR() - thresholdG >= color.getG()} and {@code color.getR() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed(final float thresholdG, final float thresholdB) {
		return getR() - thresholdG >= getG() && getR() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color3F} instance is white if all component values are equal and greater than or equal to {@code 1.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isGrayscale() && this.component1 >= 1.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3F} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3F} instance is considered yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return isRed(0.5F, 1.0F) && isGreen(0.5F, 1.0F);
	}
	
	/**
	 * Returns the value of the B-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the B-component as a {@code byte}
	 */
	public byte getAsByteB() {
		return (byte)(getAsIntB() & 0xFF);
	}
	
	/**
	 * Returns the value of component 1 as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 1 as a {@code byte}
	 */
	public byte getAsByteComponent1() {
		return (byte)(getAsIntComponent1() & 0xFF);
	}
	
	/**
	 * Returns the value of component 2 as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 2 as a {@code byte}
	 */
	public byte getAsByteComponent2() {
		return (byte)(getAsIntComponent2() & 0xFF);
	}
	
	/**
	 * Returns the value of component 3 as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 3 as a {@code byte}
	 */
	public byte getAsByteComponent3() {
		return (byte)(getAsIntComponent3() & 0xFF);
	}
	
	/**
	 * Returns the value of the G-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the G-component as a {@code byte}
	 */
	public byte getAsByteG() {
		return (byte)(getAsIntG() & 0xFF);
	}
	
	/**
	 * Returns the value of the R-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the R-component as a {@code byte}
	 */
	public byte getAsByteR() {
		return (byte)(getAsIntR() & 0xFF);
	}
	
	/**
	 * Returns the value of the X-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the X-component as a {@code byte}
	 */
	public byte getAsByteX() {
		return (byte)(getAsIntX() & 0xFF);
	}
	
	/**
	 * Returns the value of the Y-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Y-component as a {@code byte}
	 */
	public byte getAsByteY() {
		return (byte)(getAsIntY() & 0xFF);
	}
	
	/**
	 * Returns the value of the Z-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Z-component as a {@code byte}
	 */
	public byte getAsByteZ() {
		return (byte)(getAsIntZ() & 0xFF);
	}
	
	/**
	 * Returns the average component value of this {@code Color3F} instance.
	 * 
	 * @return the average component value of this {@code Color3F} instance
	 */
	public float average() {
		return (this.component1 + this.component2 + this.component3) / 3.0F;
	}
	
	/**
	 * Returns the value of the B-component.
	 * 
	 * @return the value of the B-component
	 */
	public float getB() {
		return this.component3;
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public float getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public float getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public float getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of the G-component.
	 * 
	 * @return the value of the G-component
	 */
	public float getG() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the R-component.
	 * 
	 * @return the value of the R-component
	 */
	public float getR() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public float getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public float getY() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public float getZ() {
		return this.component3;
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
		return this.component1 * 0.212671F + this.component2 * 0.715160F + this.component3 * 0.072169F;
	}
	
	/**
	 * Returns the largest component value.
	 * 
	 * @return the largest component value
	 */
	public float maximum() {
		return max(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns the smallest component value.
	 * 
	 * @return the smallest component value
	 */
	public float minimum() {
		return min(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns the value of the B-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the B-component as an {@code int}
	 */
	public int getAsIntB() {
		return toInt(Floats.saturate(getB()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of component 1 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 1 as an {@code int}
	 */
	public int getAsIntComponent1() {
		return toInt(Floats.saturate(getComponent1()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of component 2 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 2 as an {@code int}
	 */
	public int getAsIntComponent2() {
		return toInt(Floats.saturate(getComponent2()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of component 3 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 3 as an {@code int}
	 */
	public int getAsIntComponent3() {
		return toInt(Floats.saturate(getComponent3()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of the G-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the G-component as an {@code int}
	 */
	public int getAsIntG() {
		return toInt(Floats.saturate(getG()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of the R-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the R-component as an {@code int}
	 */
	public int getAsIntR() {
		return toInt(Floats.saturate(getR()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of the X-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the X-component as an {@code int}
	 */
	public int getAsIntX() {
		return toInt(Floats.saturate(getX()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of the Y-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Y-component as an {@code int}
	 */
	public int getAsIntY() {
		return toInt(Floats.saturate(getY()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns the value of the Z-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Z-component as an {@code int}
	 */
	public int getAsIntZ() {
		return toInt(Floats.saturate(getZ()) * 255.0F + 0.5F);
	}
	
	/**
	 * Returns a hash code for this {@code Color3F} instance.
	 * 
	 * @return a hash code for this {@code Color3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
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
		final int r = getAsIntR();
		final int g = getAsIntG();
		final int b = getAsIntB();
		
		return packedIntComponentOrder.pack(r, g, b, 255);
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
		final float component1 = colorLHS.component1 + colorRHS.component1;
		final float component2 = colorLHS.component2 + colorRHS.component2;
		final float component3 = colorLHS.component3 + colorRHS.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorLHS.component1 + scalarRHS;
		final float component2 = colorLHS.component2 + scalarRHS;
		final float component3 = colorLHS.component3 + scalarRHS;
		
		return new Color3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final Color3F colorMultiplyC, final float scalarMultiply, final float scalarDivide) {
		final float component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 * colorMultiplyC.component1 * scalarMultiply / scalarDivide;
		final float component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 * colorMultiplyC.component2 * scalarMultiply / scalarDivide;
		final float component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 * colorMultiplyC.component3 * scalarMultiply / scalarDivide;
		
		return new Color3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final float scalarDivide) {
		final float component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 / scalarDivide;
		final float component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 / scalarDivide;
		final float component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 / scalarDivide;
		
		return new Color3F(component1, component2, component3);
	}
	
//	TODO: Add Javadocs!
	public static Color3F addMultiplyAndDivide(final Color3F colorAdd, final Color3F colorMultiplyA, final Color3F colorMultiplyB, final float scalarMultiply, final float scalarDivide) {
		final float component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 * scalarMultiply / scalarDivide;
		final float component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 * scalarMultiply / scalarDivide;
		final float component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 * scalarMultiply / scalarDivide;
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method differs from {@link #add(Color3F, Color3F)} in that it assumes {@code colorLHS} to be an average color sample. It uses a stable moving average algorithm to compute a new average color sample as a result of adding {@code colorRHS}.
	 * This method is suitable for Monte Carlo-method based algorithms.
	 * 
	 * @param colorLHS the {@code Color3F} instance on the left-hand side
	 * @param colorRHS the {@code Color3F} instance on the right-hand side
	 * @param sampleCount the current sample count
	 * @return a new {@code Color3F} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3F addSample(final Color3F colorLHS, final Color3F colorRHS, final int sampleCount) {
		final float component1 = colorLHS.component1 + ((colorRHS.component1 - colorLHS.component1) / sampleCount);
		final float component2 = colorLHS.component2 + ((colorRHS.component2 - colorLHS.component2) / sampleCount);
		final float component3 = colorLHS.component3 + ((colorRHS.component3 - colorLHS.component3) / sampleCount);
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = lerp(colorLHS.component1, colorRHS.component1, tComponent1);
		final float component2 = lerp(colorLHS.component2, colorRHS.component2, tComponent2);
		final float component3 = lerp(colorLHS.component3, colorRHS.component3, tComponent3);
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Converts {@code color} from the RGB-color space to the XYZ-color space using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance in RGB-color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F convertRGBToXYZUsingPBRT(final Color3F color) {
		final float x = 0.412453F * color.getR() + 0.357580F * color.getG() + 0.180423F * color.getB();
		final float y = 0.212671F * color.getR() + 0.715160F * color.getG() + 0.072169F * color.getB();
		final float z = 0.019334F * color.getR() + 0.119193F * color.getG() + 0.950227F * color.getB();
		
		return new Color3F(x, y, z);
	}
	
	/**
	 * Converts {@code color} from the RGB-color space to the XYZ-color space using sRGB.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance in RGB-color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F convertRGBToXYZUsingSRGB(final Color3F color) {
		return ColorSpace3F.SRGB.convertRGBToXYZ(color);
	}
	
	/**
	 * Converts {@code color} from the XYZ-color space to the RGB-color space using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance in XYZ-color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F convertXYZToRGBUsingPBRT(final Color3F color) {
		final float r = +3.240479F * color.getX() - 1.537150F * color.getY() - 0.498535F * color.getZ();
		final float g = -0.969256F * color.getX() + 1.875991F * color.getY() + 0.041556F * color.getZ();
		final float b = +0.055648F * color.getX() - 0.204043F * color.getY() + 1.057311F * color.getZ();
		
		return new Color3F(r, g, b);
	}
	
	/**
	 * Converts {@code color} from the XYZ-color space to the RGB-color space using sRGB.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance in XYZ-color space
	 * @return a new {@code Color3F} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F convertXYZToRGBUsingSRGB(final Color3F color) {
		return ColorSpace3F.SRGB.convertXYZToRGB(color);
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
		final float component1 = finiteOrDefault(colorLHS.component1 / colorRHS.component1, 0.0F);
		final float component2 = finiteOrDefault(colorLHS.component2 / colorRHS.component2, 0.0F);
		final float component3 = finiteOrDefault(colorLHS.component3 / colorRHS.component3, 0.0F);
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = finiteOrDefault(colorLHS.component1 / scalarRHS, 0.0F);
		final float component2 = finiteOrDefault(colorLHS.component2 / scalarRHS, 0.0F);
		final float component3 = finiteOrDefault(colorLHS.component3 / scalarRHS, 0.0F);
		
		return new Color3F(component1, component2, component3);
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
		return new Color3F(color.component1);
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
		return new Color3F(color.component2);
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
		return new Color3F(color.component3);
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
		final float component1 = 1.0F - color.component1;
		final float component2 = 1.0F - color.component2;
		final float component3 = 1.0F - color.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = max(colorA.component1, colorB.component1);
		final float component2 = max(colorA.component2, colorB.component2);
		final float component3 = max(colorA.component3, colorB.component3);
		
		return new Color3F(component1, component2, component3);
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
			final float component1 = color.component1 / maximum;
			final float component2 = color.component2 / maximum;
			final float component3 = color.component3 / maximum;
			
			return new Color3F(component1, component2, component3);
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
		final float component1 = min(colorA.component1, colorB.component1);
		final float component2 = min(colorA.component2, colorB.component2);
		final float component3 = min(colorA.component3, colorB.component3);
		
		return new Color3F(component1, component2, component3);
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
			final float component1 = color.component1 + -minimum;
			final float component2 = color.component2 + -minimum;
			final float component3 = color.component3 + -minimum;
			
			return new Color3F(component1, component2, component3);
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
		final float component1 = colorLHS.component1 * colorRHS.component1;
		final float component2 = colorLHS.component2 * colorRHS.component2;
		final float component3 = colorLHS.component3 * colorRHS.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorA.component1 * colorB.component1 * colorC.component1;
		final float component2 = colorA.component2 * colorB.component2 * colorC.component2;
		final float component3 = colorA.component3 * colorB.component3 * colorC.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorA.component1 * colorB.component1 * colorC.component1 * colorD.component1;
		final float component2 = colorA.component2 * colorB.component2 * colorC.component2 * colorD.component2;
		final float component3 = colorA.component3 * colorB.component3 * colorC.component3 * colorD.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorA.component1 * colorB.component1 * scalarC;
		final float component2 = colorA.component2 * colorB.component2 * scalarC;
		final float component3 = colorA.component3 * colorB.component3 * scalarC;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorLHS.component1 * scalarRHS;
		final float component2 = colorLHS.component2 * scalarRHS;
		final float component3 = colorLHS.component3 * scalarRHS;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = max(colorLHS.component1 * scalarRHS, 0.0F);
		final float component2 = max(colorLHS.component2 * scalarRHS, 0.0F);
		final float component3 = max(colorLHS.component3 * scalarRHS, 0.0F);
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = -color.component1;
		final float component2 = -color.component2;
		final float component3 = -color.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float sum = color.component1 + color.component2 + color.component3;
		
		if(sum < 1.0e-6F) {
			return color;
		}
		
		final float sumReciprocal = 1.0F / sum;
		
		final float component1 = color.component1 * sumReciprocal;
		final float component2 = color.component2 * sumReciprocal;
		final float component3 = color.component3 * sumReciprocal;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = Floats.random();
		final float component2 = Floats.random();
		final float component3 = Floats.random();
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = Floats.random();
		final float component2 = 0.0F;
		final float component3 = 0.0F;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = 0.0F;
		final float component2 = Floats.random();
		final float component3 = 0.0F;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = 0.0F;
		final float component2 = 0.0F;
		final float component3 = Floats.random();
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Redoes gamma correction on the component values of {@code color} using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F redoGammaCorrectionPBRT(final Color3F color) {
		final float component1 = color.component1 <= 0.0031308F ? 12.92F * color.component1 : 1.055F * pow(color.component1, 1.0F / 2.4F) - 0.055F;
		final float component2 = color.component2 <= 0.0031308F ? 12.92F * color.component2 : 1.055F * pow(color.component2, 1.0F / 2.4F) - 0.055F;
		final float component3 = color.component3 <= 0.0031308F ? 12.92F * color.component3 : 1.055F * pow(color.component3, 1.0F / 2.4F) - 0.055F;
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Redoes gamma correction on the component values of {@code color} using sRGB.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F redoGammaCorrectionSRGB(final Color3F color) {
		return ColorSpace3F.SRGB.redoGammaCorrection(color);
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
		final float component1 = Floats.saturate(color.component1, edgeA, edgeB);
		final float component2 = Floats.saturate(color.component2, edgeA, edgeB);
		final float component3 = Floats.saturate(color.component3, edgeA, edgeB);
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = color.component1 * 0.393F + color.component2 * 0.769F + color.component3 * 0.189F;
		final float component2 = color.component1 * 0.349F + color.component2 * 0.686F + color.component3 * 0.168F;
		final float component3 = color.component1 * 0.272F + color.component2 * 0.534F + color.component3 * 0.131F;
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Returns a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * If either {@code color} or {@code point} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.simplexFractionalBrownianMotion(color, point, new Point2F(800.0F, 800.0F));
	 * }
	 * </pre>
	 * 
	 * @param color the base {@code Color3F} instance
	 * @param point the current {@link Point2F} instance
	 * @return a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm
	 * @throws NullPointerException thrown if, and only if, either {@code color} or {@code point} are {@code null}
	 */
	public static Color3F simplexFractionalBrownianMotion(final Color3F color, final Point2F point) {
		return simplexFractionalBrownianMotion(color, point, new Point2F(800.0F, 800.0F));
	}
	
	/**
	 * Returns a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * If either {@code color}, {@code point} or {@code pointEdgeA} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.simplexFractionalBrownianMotion(color, point, pointEdgeA, new Point2F());
	 * }
	 * </pre>
	 * 
	 * @param color the base {@code Color3F} instance
	 * @param point the current {@link Point2F} instance
	 * @param pointEdgeA a {@code Point2F} instance denoting one of the edges
	 * @return a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm
	 * @throws NullPointerException thrown if, and only if, either {@code color}, {@code point} or {@code pointEdgeA} are {@code null}
	 */
	public static Color3F simplexFractionalBrownianMotion(final Color3F color, final Point2F point, final Point2F pointEdgeA) {
		return simplexFractionalBrownianMotion(color, point, pointEdgeA, new Point2F());
	}
	
	/**
	 * Returns a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * If either {@code color}, {@code point}, {@code pointEdgeA} or {@code pointEdgeB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.simplexFractionalBrownianMotion(color, point, pointEdgeA, pointEdgeB, 5.0F, 0.5F, 16);
	 * }
	 * </pre>
	 * 
	 * @param color the base {@code Color3F} instance
	 * @param point the current {@link Point2F} instance
	 * @param pointEdgeA a {@code Point2F} instance denoting one of the edges
	 * @param pointEdgeB a {@code Point2F} instance denoting one of the edges
	 * @return a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm
	 * @throws NullPointerException thrown if, and only if, either {@code color}, {@code point}, {@code pointEdgeA} or {@code pointEdgeB} are {@code null}
	 */
	public static Color3F simplexFractionalBrownianMotion(final Color3F color, final Point2F point, final Point2F pointEdgeA, final Point2F pointEdgeB) {
		return simplexFractionalBrownianMotion(color, point, pointEdgeA, pointEdgeB, 5.0F, 0.5F, 16);
	}
	
	/**
	 * Returns a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm.
	 * <p>
	 * If either {@code color}, {@code point}, {@code pointEdgeA} or {@code pointEdgeB} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color the base {@code Color3F} instance
	 * @param point the current {@link Point2F} instance
	 * @param pointEdgeA a {@code Point2F} instance denoting one of the edges
	 * @param pointEdgeB a {@code Point2F} instance denoting one of the edges
	 * @param frequency the frequency to use
	 * @param gain the gain to use
	 * @param octaves the octaves to use
	 * @return a {@code Color3F} instance using a Simplex noise-based fractional Brownian motion (fBm) algorithm
	 * @throws NullPointerException thrown if, and only if, either {@code color}, {@code point}, {@code pointEdgeA} or {@code pointEdgeB} are {@code null}
	 */
	public static Color3F simplexFractionalBrownianMotion(final Color3F color, final Point2F point, final Point2F pointEdgeA, final Point2F pointEdgeB, final float frequency, final float gain, final int octaves) {
		final Point2F pointEdgeMaximum = Point2F.maximum(pointEdgeA, pointEdgeB);
		final Point2F pointEdgeMinimum = Point2F.minimum(pointEdgeA, pointEdgeB);
		
		final float x = (point.getX() - pointEdgeMinimum.getX()) / (pointEdgeMaximum.getX() - pointEdgeMinimum.getX());
		final float y = (point.getY() - pointEdgeMinimum.getY()) / (pointEdgeMaximum.getY() - pointEdgeMinimum.getY());
		
		final float noise = simplexFractionalBrownianMotionXY(x, y, frequency, gain, 0.0F, 1.0F, octaves);
		
		return maximumTo1(minimumTo0(multiply(color, noise)));
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
		final float component1 = Floats.sqrt(color.component1);
		final float component2 = Floats.sqrt(color.component2);
		final float component3 = Floats.sqrt(color.component3);
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorLHS.component1 - colorRHS.component1;
		final float component2 = colorLHS.component2 - colorRHS.component2;
		final float component3 = colorLHS.component3 - colorRHS.component3;
		
		return new Color3F(component1, component2, component3);
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
		final float component1 = colorLHS.component1 - scalarRHS;
		final float component2 = colorLHS.component2 - scalarRHS;
		final float component3 = colorLHS.component3 - scalarRHS;
		
		return new Color3F(component1, component2, component3);
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
	 * Color3F.toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, Float.MIN_VALUE);
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
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0F, Float.MIN_VALUE);
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
	 * float component11 = Floats.max(color.getComponent1() * exposure - subtract, minimum);
	 * float component12 = Floats.saturate((component11 * (a * component11 + b)) / (component11 * (c * component11 + d) + e));
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
		final float component11 = max(color.component1 * exposure - subtract, minimum);
		final float component21 = max(color.component2 * exposure - subtract, minimum);
		final float component31 = max(color.component3 * exposure - subtract, minimum);
		
		final float component12 = Floats.saturate((component11 * (a * component11 + b)) / (component11 * (c * component11 + d) + e));
		final float component22 = Floats.saturate((component21 * (a * component21 + b)) / (component21 * (c * component21 + d) + e));
		final float component32 = Floats.saturate((component31 * (a * component31 + b)) / (component31 * (c * component31 + d) + e));
		
		return new Color3F(component12, component22, component32);
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
		
		final float component11 = color.component1 * exposure;
		final float component21 = color.component2 * exposure;
		final float component31 = color.component3 * exposure;
		
		final float component12 = component11 / (1.0F + component11);
		final float component22 = component21 / (1.0F + component21);
		final float component32 = component31 / (1.0F + component31);
		
		return new Color3F(component12, component22, component32);
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
		
		final float component11 = color.component1 * exposure;
		final float component21 = color.component2 * exposure;
		final float component31 = color.component3 * exposure;
		
		final float component12 = component11 * (1.0F + component11 / (lWhite * lWhite)) / (1.0F + component11);
		final float component22 = component21 * (1.0F + component21 / (lWhite * lWhite)) / (1.0F + component21);
		final float component32 = component31 * (1.0F + component31 / (lWhite * lWhite)) / (1.0F + component31);
		
		return new Color3F(component12, component22, component32);
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
		final float component11 = color.component1 * exposure;
		final float component21 = color.component2 * exposure;
		final float component31 = color.component3 * exposure;
		
		final float component12 = 1.0F - exp(-component11 * exposure);
		final float component22 = 1.0F - exp(-component21 * exposure);
		final float component32 = 1.0F - exp(-component31 * exposure);
		
		return new Color3F(component12, component22, component32);
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
		
		final float component11 = color.component1 * exposure;
		final float component21 = color.component2 * exposure;
		final float component31 = color.component3 * exposure;
		
		final float component12 = component11 / (component11 + 0.155F) * 1.019F;
		final float component22 = component21 / (component21 + 0.155F) * 1.019F;
		final float component32 = component31 / (component31 + 0.155F) * 1.019F;
		
		return new Color3F(component12, component22, component32);
	}
	
	/**
	 * Undoes gamma correction on the component values of {@code color} using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F undoGammaCorrectionPBRT(final Color3F color) {
		final float component1 = color.component1 <= 0.04045F ? color.component1 * 1.0F / 12.92F : pow((color.component1 + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float component2 = color.component2 <= 0.04045F ? color.component2 * 1.0F / 12.92F : pow((color.component2 + 0.055F) * 1.0F / 1.055F, 2.4F);
		final float component3 = color.component3 <= 0.04045F ? color.component3 * 1.0F / 12.92F : pow((color.component3 + 0.055F) * 1.0F / 1.055F, 2.4F);
		
		return new Color3F(component1, component2, component3);
	}
	
	/**
	 * Undoes gamma correction on the component values of {@code color} using sRGB.
	 * <p>
	 * Returns a new {@code Color3F} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3F} instance
	 * @return a new {@code Color3F} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3F undoGammaCorrectionSRGB(final Color3F color) {
		return ColorSpace3F.SRGB.undoGammaCorrection(color);
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
	 * Returns a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.arrayRead(array, ArrayComponentOrder.BGRA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3F[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.BGRA);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3F[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
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
	 * Returns a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color3F.arrayRead(array, ArrayComponentOrder.BGRA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color3F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color3F[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.BGRA);
	}
	
	/**
	 * Returns a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color3F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color3F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color3F[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
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
	
	private static final class ColorSpace3F {
		public static final ColorSpace3F SRGB = new ColorSpace3F(0.00304F, 2.4F, 0.6400F, 0.3300F, 0.3000F, 0.6000F, 0.1500F, 0.0600F, 0.31271F, 0.32902F);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private final float breakPoint;
		private final float gamma;
		private final float segmentOffset;
		private final float slope;
		private final float slopeMatch;
		private final float[] matrixRGBToXYZ;
		private final float[] matrixXYZToRGB;
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public ColorSpace3F(final float breakPoint, final float gamma, final float xR, final float yR, final float xG, final float yG, final float xB, final float yB, final float xW, final float yW) {
			this.breakPoint = breakPoint;
			this.gamma = gamma;
			this.slope = breakPoint > 0.0F ? 1.0F / (gamma / pow(breakPoint, 1.0F / gamma - 1.0F) - gamma * breakPoint + breakPoint) : 1.0F;
			this.slopeMatch = breakPoint > 0.0F ? gamma * this.slope / pow(breakPoint, 1.0F / gamma - 1.0F) : 1.0F;
			this.segmentOffset = breakPoint > 0.0F ? this.slopeMatch * pow(breakPoint, 1.0F / gamma) - this.slope * breakPoint : 0.0F;
			this.matrixXYZToRGB = doCreateColorSpace3MatrixXYZToRGB(xR, yR, xG, yG, xB, yB, xW, yW);
			this.matrixRGBToXYZ = doCreateColorSpace3MatrixRGBToXYZ(xW, yW, this.matrixXYZToRGB);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		public Color3F convertRGBToXYZ(final Color3F color) {
			final float r = color.getR();
			final float g = color.getG();
			final float b = color.getB();
			
			final float x = this.matrixRGBToXYZ[0] * r + this.matrixRGBToXYZ[3] * g + this.matrixRGBToXYZ[6] * b;
			final float y = this.matrixRGBToXYZ[1] * r + this.matrixRGBToXYZ[4] * g + this.matrixRGBToXYZ[7] * b;
			final float z = this.matrixRGBToXYZ[2] * r + this.matrixRGBToXYZ[5] * g + this.matrixRGBToXYZ[8] * b;
			
			return new Color3F(x, y, z);
		}
		
		public Color3F convertXYZToRGB(final Color3F color) {
			final float x = color.getX();
			final float y = color.getY();
			final float z = color.getZ();
			
			final float r = this.matrixXYZToRGB[0] * x + this.matrixXYZToRGB[1] * y + this.matrixXYZToRGB[2] * z;
			final float g = this.matrixXYZToRGB[3] * x + this.matrixXYZToRGB[4] * y + this.matrixXYZToRGB[5] * z;
			final float b = this.matrixXYZToRGB[6] * x + this.matrixXYZToRGB[7] * y + this.matrixXYZToRGB[8] * z;
			
			return new Color3F(r, g, b);
		}
		
		public Color3F redoGammaCorrection(final Color3F color) {
			final float component1 = doRedoGammaCorrection(color.getComponent1());
			final float component2 = doRedoGammaCorrection(color.getComponent2());
			final float component3 = doRedoGammaCorrection(color.getComponent3());
			
			return new Color3F(component1, component2, component3);
		}
		
		public Color3F undoGammaCorrection(final Color3F color) {
			final float component1 = doUndoGammaCorrection(color.getComponent1());
			final float component2 = doUndoGammaCorrection(color.getComponent2());
			final float component3 = doUndoGammaCorrection(color.getComponent3());
			
			return new Color3F(component1, component2, component3);
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private float doRedoGammaCorrection(final float value) {
			if(value <= 0.0F) {
				return 0.0F;
			} else if(value >= 1.0F) {
				return 1.0F;
			} else if(value <= this.breakPoint) {
				return value * this.slope;
			} else {
				return this.slopeMatch * pow(value, 1.0F / this.gamma) - this.segmentOffset;
			}
		}
		
		private float doUndoGammaCorrection(final float value) {
			if(value <= 0.0F) {
				return 0.0F;
			} else if(value >= 1.0F) {
				return 1.0F;
			} else if(value <= this.breakPoint * this.slope) {
				return value / this.slope;
			} else {
				return pow((value + this.segmentOffset) / this.slopeMatch, this.gamma);
			}
		}
		
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		private static float[] doCreateColorSpace3MatrixRGBToXYZ(final float xW, final float yW, final float[] m) {
			final float a = m[0] * (m[4] * m[8] - m[7] * m[5]);
			final float b = m[1] * (m[3] * m[8] - m[6] * m[5]);
			final float c = m[2] * (m[3] * m[7] - m[6] * m[4]);
			final float s = 1.0F / (a - b + c);
			
			final float eXR = s * (m[4] * m[8] - m[5] * m[7]);
			final float eYR = s * (m[5] * m[6] - m[3] * m[8]);
			final float eZR = s * (m[3] * m[7] - m[4] * m[6]);
			final float eXG = s * (m[2] * m[7] - m[1] * m[8]);
			final float eYG = s * (m[0] * m[8] - m[2] * m[6]);
			final float eZG = s * (m[1] * m[6] - m[0] * m[7]);
			final float eXB = s * (m[1] * m[5] - m[2] * m[4]);
			final float eYB = s * (m[2] * m[3] - m[0] * m[5]);
			final float eZB = s * (m[0] * m[4] - m[1] * m[3]);
			final float eXW = xW;
			final float eYW = yW;
			final float eZW = 1.0F - (xW + yW);
			
			return new float[] {
				eXR, eYR, eZR,
				eXG, eYG, eZG,
				eXB, eYB, eZB,
				eXW, eYW, eZW
			};
		}
		
		private static float[] doCreateColorSpace3MatrixXYZToRGB(final float xR, final float yR, final float xG, final float yG, final float xB, final float yB, final float xW, final float yW) {
			final float zR = 1.0F - (xR + yR);
			final float zG = 1.0F - (xG + yG);
			final float zB = 1.0F - (xB + yB);
			final float zW = 1.0F - (xW + yW);
			final float rX = (yG * zB) - (yB * zG);
			final float rY = (xB * zG) - (xG * zB);
			final float rZ = (xG * yB) - (xB * yG);
			final float rW = ((rX * xW) + (rY * yW) + (rZ * zW)) / yW;
			final float gX = (yB * zR) - (yR * zB);
			final float gY = (xR * zB) - (xB * zR);
			final float gZ = (xB * yR) - (xR * yB);
			final float gW = ((gX * xW) + (gY * yW) + (gZ * zW)) / yW;
			final float bX = (yR * zG) - (yG * zR);
			final float bY = (xG * zR) - (xR * zG);
			final float bZ = (xR * yG) - (xG * yR);
			final float bW = ((bX * xW) + (bY * yW) + (bZ * zW)) / yW;
			
			final float eRX = rX / rW;
			final float eRY = rY / rW;
			final float eRZ = rZ / rW;
			final float eGX = gX / gW;
			final float eGY = gY / gW;
			final float eGZ = gZ / gW;
			final float eBX = bX / bW;
			final float eBY = bY / bW;
			final float eBZ = bZ / bW;
			final float eRW = rW;
			final float eGW = gW;
			final float eBW = bW;
			
			return new float[] {
				eRX, eRY, eRZ,
				eGX, eGY, eGZ,
				eBX, eBY, eBZ,
				eRW, eGW, eBW
			};
		}
	}
}