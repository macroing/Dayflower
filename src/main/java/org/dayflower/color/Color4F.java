/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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
import static org.dayflower.utility.Floats.max;
import static org.dayflower.utility.Floats.min;
import static org.dayflower.utility.Floats.toFloat;
import static org.dayflower.utility.Ints.toInt;

import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.utility.Floats;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

/**
 * A {@code Color4F} encapsulates a color using the data type {@code float}.
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
	 * A {@code Color4F} denoting the color white.
	 */
	public static final Color4F WHITE = new Color4F(1.0F, 1.0F, 1.0F);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float component1;
	private final float component2;
	private final float component3;
	private final float component4;
	
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
	 * @param color a {@link Color3D} instance
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public Color4F(final Color3D color) {
		this(toFloat(color.getComponent1()), toFloat(color.getComponent2()), toFloat(color.getComponent3()));
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
		this(color.getComponent1(), color.getComponent2(), color.getComponent3());
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
	 * Constructs a new {@code Color4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 1.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(component1, component2, component3, 1.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color4F(final float component1, final float component2, final float component3) {
		this(component1, component2, component3, 1.0F);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Color4F(final float component1, final float component2, final float component3, final float component4) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
		this.component4 = component4;
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
	 * Constructs a new {@code Color4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code 255}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(component1, component2, component3, 255);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color4F(final int component1, final int component2, final int component3) {
		this(component1, component2, component3, 255);
	}
	
	/**
	 * Constructs a new {@code Color4F} instance given the component values {@code component1}, {@code component2}, {@code component3} and {@code component4}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color4F(Ints.saturate(component1) / 255.0F, Ints.saturate(component2) / 255.0F, Ints.saturate(component3) / 255.0F, Ints.saturate(component4) / 255.0F);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 * @param component4 the value of component 4
	 */
	public Color4F(final int component1, final int component2, final int component3, final int component4) {
		this(Ints.saturate(component1) / 255.0F, Ints.saturate(component2) / 255.0F, Ints.saturate(component3) / 255.0F, Ints.saturate(component4) / 255.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color4F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color4F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color4F(%+.10f, %+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
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
		} else if(!equal(this.component1, Color4F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Color4F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Color4F.class.cast(object).component3)) {
			return false;
		} else if(!equal(this.component4, Color4F.class.cast(object).component4)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return isInfinite(this.component1) || isInfinite(this.component2) || isInfinite(this.component3) || isInfinite(this.component4);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color4F} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return isNaN(this.component1) || isNaN(this.component2) || isNaN(this.component3) || isNaN(this.component4);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is black if, and only if, the component values of component 1, component 2 and component 3 are {@code 0.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isGrayscale() && isZero(this.component1);
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
	 * The {@code Color4F} instance {@code color} is considered blue if, and only if, {@code color.getB() - thresholdR >= color.getR()} and {@code color.getB() - thresholdG >= color.getG()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue(final float thresholdR, final float thresholdG) {
		return getB() - thresholdR >= getR() && getB() - thresholdG >= getG();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return isGreen(1.0F, 0.5F) && isBlue(1.0F, 0.5F);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is a grayscale color if, and only if, the component values of component 1, component 2 and component 3 are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is a grayscale color, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return equal(this.component1, this.component2, this.component3);
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
	 * The {@code Color4F} instance {@code color} is considered green if, and only if, {@code color.getG() - thresholdR >= color.getR()} and {@code color.getG() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen(final float thresholdR, final float thresholdB) {
		return getG() - thresholdR >= getR() && getG() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return isRed(1.0F, 0.5F) && isBlue(0.5F, 1.0F);
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
	 * The {@code Color4F} instance {@code color} is considered red if, and only if, {@code color.getR() - thresholdG >= color.getG()} and {@code color.getR() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed(final float thresholdG, final float thresholdB) {
		return getR() - thresholdG >= getG() && getR() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color4F} instance is white if, and only if, the component values of component 1, component 2 and component 3 are equal and greater than or equal to {@code 1.0F}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isGrayscale() && this.component1 >= 1.0F;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color4F} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color4F} instance is considered yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return isRed(0.5F, 1.0F) && isGreen(0.5F, 1.0F);
	}
	
	/**
	 * Returns the value of the A-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the A-component as a {@code byte}
	 */
	public byte getAsByteA() {
		return (byte)(getAsIntA() & 0xFF);
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
	 * Returns the value of component 4 as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 4 as a {@code byte}
	 */
	public byte getAsByteComponent4() {
		return (byte)(getAsIntComponent4() & 0xFF);
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
	 * Returns the value of the W-component as a {@code byte}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the W-component as a {@code byte}
	 */
	public byte getAsByteW() {
		return (byte)(getAsIntW() & 0xFF);
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
	 * Returns the average component value of this {@code Color4F} instance.
	 * <p>
	 * The average component value is computed using component 1, component 2 and component 3.
	 * 
	 * @return the average component value of this {@code Color4F} instance
	 */
	public float average() {
		return (this.component1 + this.component2 + this.component3) / 3.0F;
	}
	
	/**
	 * Returns the value of the A-component.
	 * 
	 * @return the value of the A-component
	 */
	public float getA() {
		return this.component4;
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
	 * Returns the value of component 4.
	 * 
	 * @return the value of component 4
	 */
	public float getComponent4() {
		return this.component4;
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
	 * Returns the value of the W-component.
	 * 
	 * @return the value of the W-component
	 */
	public float getW() {
		return this.component4;
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
	 * Returns the lightness of this {@code Color4F} instance.
	 * <p>
	 * The lightness is computed using component 1, component 2 and component 3.
	 * 
	 * @return the lightness of this {@code Color4F} instance
	 */
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
	public float luminance() {
		return this.component1 * 0.212671F + this.component2 * 0.715160F + this.component3 * 0.072169F;
	}
	
	/**
	 * Returns the largest component value of component 1, component 2 and component 3.
	 * 
	 * @return the largest component value of component 1, component 2 and component 3
	 */
	public float maximum() {
		return max(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns the smallest component value of component 1, component 2 and component 3.
	 * 
	 * @return the smallest component value of component 1, component 2 and component 3
	 */
	public float minimum() {
		return min(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns the value of the A-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the A-component as an {@code int}
	 */
	public int getAsIntA() {
		return toInt(Floats.saturate(getA()) * 255.0F + 0.5F);
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
	 * Returns the value of component 4 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 4 as an {@code int}
	 */
	public int getAsIntComponent4() {
		return toInt(Floats.saturate(getComponent4()) * 255.0F + 0.5F);
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
	 * Returns the value of the W-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the W-component as an {@code int}
	 */
	public int getAsIntW() {
		return toInt(Floats.saturate(getW()) * 255.0F + 0.5F);
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
	 * Returns a hash code for this {@code Color4F} instance.
	 * 
	 * @return a hash code for this {@code Color4F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3), Float.valueOf(this.component4));
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
		final int r = getAsIntR();
		final int g = getAsIntG();
		final int b = getAsIntB();
		final int a = getAsIntA();
		
		return packedIntComponentOrder.pack(r, g, b, a);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
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
	public static Color4F random() {
		final float component1 = Floats.random();
		final float component2 = Floats.random();
		final float component3 = Floats.random();
		
		return new Color4F(component1, component2, component3);
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
	public static Color4F[] arrayRandom(final int length) {
		return array(length, () -> random());
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.arrayRead(array, ArrayComponentOrder.BGRA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color4F[] arrayRead(final byte[] array) {
		return arrayRead(array, ArrayComponentOrder.BGRA);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color4F[] arrayRead(final byte[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
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
	 * Returns a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If {@code array} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * Color4F.arrayRead(array, ArrayComponentOrder.BGRA);
	 * }
	 * </pre>
	 * 
	 * @param array the array to read from
	 * @return a {@code Color4F[]} with a length of {@code array.length / ArrayComponentOrder.BGRA.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % ArrayComponentOrder.BGRA.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code array} is {@code null}
	 */
	public static Color4F[] arrayRead(final int[] array) {
		return arrayRead(array, ArrayComponentOrder.BGRA);
	}
	
	/**
	 * Returns a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}.
	 * <p>
	 * If either {@code array} or {@code arrayComponentOrder} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * If {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}, an {@code ArrayIndexOutOfBoundsException} will be thrown.
	 * 
	 * @param array the array to read from
	 * @param arrayComponentOrder an {@link ArrayComponentOrder} instance
	 * @return a {@code Color4F[]} with a length of {@code array.length / arrayComponentOrder.getComponentCount()} and contains {@code Color4F} instances read from {@code array}
	 * @throws ArrayIndexOutOfBoundsException thrown if, and only if, {@code array.length % arrayComponentOrder.getComponentCount()} is not {@code 0}
	 * @throws NullPointerException thrown if, and only if, either {@code array} or {@code arrayComponentOrder} are {@code null}
	 */
	public static Color4F[] arrayRead(final int[] array, final ArrayComponentOrder arrayComponentOrder) {
		Objects.requireNonNull(array, "array == null");
		Objects.requireNonNull(arrayComponentOrder, "arrayComponentOrder == null");
		
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
}