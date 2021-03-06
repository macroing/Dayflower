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

import static org.dayflower.utility.Doubles.MIN_VALUE;
import static org.dayflower.utility.Doubles.equal;
import static org.dayflower.utility.Doubles.exp;
import static org.dayflower.utility.Doubles.finiteOrDefault;
import static org.dayflower.utility.Doubles.isInfinite;
import static org.dayflower.utility.Doubles.isNaN;
import static org.dayflower.utility.Doubles.isZero;
import static org.dayflower.utility.Doubles.lerp;
import static org.dayflower.utility.Doubles.max;
import static org.dayflower.utility.Doubles.min;
import static org.dayflower.utility.Doubles.pow;
import static org.dayflower.utility.Doubles.toDouble;
import static org.dayflower.utility.Ints.toInt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.function.Supplier;

import org.dayflower.utility.Doubles;
import org.dayflower.utility.Ints;
import org.dayflower.utility.ParameterArguments;

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
	public static final Color3D AG_ETA = IrregularSpectralCurveD.AG_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for silver (Ag).
	 */
	public static final Color3D AG_K = IrregularSpectralCurveD.AG_K.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for aluminum (Al).
	 */
	public static final Color3D AL_ETA = IrregularSpectralCurveD.AL_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for aluminum (Al).
	 */
	public static final Color3D AL_K = IrregularSpectralCurveD.AL_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color Aztek gold (Au).
	 */
	public static final Color3D AU_AZTEK = new Color3D(0.76D, 0.6D, 0.33D);
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for gold (Au).
	 */
	public static final Color3D AU_ETA = IrregularSpectralCurveD.AU_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for gold (Au).
	 */
	public static final Color3D AU_K = IrregularSpectralCurveD.AU_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color metallic gold (Au).
	 */
	public static final Color3D AU_METALLIC = new Color3D(0.83D, 0.69D, 0.22D);
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for beryllium (Be).
	 */
	public static final Color3D BE_ETA = IrregularSpectralCurveD.BE_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for beryllium (Be).
	 */
	public static final Color3D BE_K = IrregularSpectralCurveD.BE_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color black.
	 */
	public static final Color3D BLACK = new Color3D();
	
	/**
	 * A {@code Color3D} denoting the color blue.
	 */
	public static final Color3D BLUE = new Color3D(0.0D, 0.0D, 1.0D);
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for chromium (Cr).
	 */
	public static final Color3D CR_ETA = IrregularSpectralCurveD.CR_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for chromium (Cr).
	 */
	public static final Color3D CR_K = IrregularSpectralCurveD.CR_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color copper (Cu).
	 */
	public static final Color3D CU = new Color3D(0.72D, 0.45D, 0.2D);
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for copper (Cu).
	 */
	public static final Color3D CU_ETA = IrregularSpectralCurveD.CU_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for copper (Cu).
	 */
	public static final Color3D CU_K = IrregularSpectralCurveD.CU_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color cyan.
	 */
	public static final Color3D CYAN = new Color3D(0.0D, 1.0D, 1.0D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.01D}.
	 */
	public static final Color3D GRAY_0_01 = new Color3D(0.01D, 0.01D, 0.01D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.1D}.
	 */
	public static final Color3D GRAY_0_10 = new Color3D(0.1D, 0.1D, 0.1D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.2D}.
	 */
	public static final Color3D GRAY_0_20 = new Color3D(0.2D, 0.2D, 0.2D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.25D}.
	 */
	public static final Color3D GRAY_0_25 = new Color3D(0.25D, 0.25D, 0.25D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.3D}.
	 */
	public static final Color3D GRAY_0_30 = new Color3D(0.3D, 0.3D, 0.3D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 0.5D}.
	 */
	public static final Color3D GRAY_0_50 = new Color3D(0.5D, 0.5D, 0.5D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 1.5D}.
	 */
	public static final Color3D GRAY_1_50 = new Color3D(1.5D, 1.5D, 1.5D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 1.55D}.
	 */
	public static final Color3D GRAY_1_55 = new Color3D(1.55D, 1.55D, 1.55D);
	
	/**
	 * A {@code Color3D} denoting the color gray, with component values of {@code 2.0D}.
	 */
	public static final Color3D GRAY_2_00 = new Color3D(2.0D, 2.0D, 2.0D);
	
	/**
	 * A {@code Color3D} denoting the color green.
	 */
	public static final Color3D GREEN = new Color3D(0.0D, 1.0D, 0.0D);
	
	/**
	 * A {@code Color3D} that contains the eta, also called the index of refraction (IOR), for mercury (Hg).
	 */
	public static final Color3D HG_ETA = IrregularSpectralCurveD.HG_ETA.toColorRGB();
	
	/**
	 * A {@code Color3D} that contains the absorption coefficient for mercury (Hg).
	 */
	public static final Color3D HG_K = IrregularSpectralCurveD.HG_K.toColorRGB();
	
	/**
	 * A {@code Color3D} denoting the color magenta.
	 */
	public static final Color3D MAGENTA = new Color3D(1.0D, 0.0D, 1.0D);
	
	/**
	 * A {@code Color3D} denoting the color orange.
	 */
	public static final Color3D ORANGE = new Color3D(1.0D, 0.5D, 0.0D);
	
	/**
	 * A {@code Color3D} denoting the color red.
	 */
	public static final Color3D RED = new Color3D(1.0D, 0.0D, 0.0D);
	
	/**
	 * A {@code Color3D} denoting the color white.
	 */
	public static final Color3D WHITE = new Color3D(1.0D, 1.0D, 1.0D);
	
	/**
	 * A {@code Color3D} denoting the color yellow.
	 */
	public static final Color3D YELLOW = new Color3D(1.0D, 1.0D, 0.0D);
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static final double[] R_G_B_EXPONENTS = doCreateRGBExponents();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final double component1;
	private final double component2;
	private final double component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Color3D} instance denoting the color black.
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
		this(toDouble(color.getComponent1()), toDouble(color.getComponent2()), toDouble(color.getComponent3()));
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
		this(color.getComponent1(), color.getComponent2(), color.getComponent3());
	}
	
	/**
	 * Constructs a new {@code Color3D} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Color3D(final double component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color3D(final double component1, final double component2, final double component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	/**
	 * Constructs a new {@code Color3D} instance denoting a grayscale color.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(component, component, component);
	 * }
	 * </pre>
	 * 
	 * @param component the value of all components
	 */
	public Color3D(final int component) {
		this(component, component, component);
	}
	
	/**
	 * Constructs a new {@code Color3D} instance given the component values {@code component1}, {@code component2} and {@code component3}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(Ints.saturate(component1) / 255.0D, Ints.saturate(component2) / 255.0D, Ints.saturate(component3) / 255.0D);
	 * }
	 * </pre>
	 * 
	 * @param component1 the value of component 1
	 * @param component2 the value of component 2
	 * @param component3 the value of component 3
	 */
	public Color3D(final int component1, final int component2, final int component3) {
		this(Ints.saturate(component1) / 255.0D, Ints.saturate(component2) / 255.0D, Ints.saturate(component3) / 255.0D);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Color3D} instance.
	 * 
	 * @return a {@code String} representation of this {@code Color3D} instance
	 */
	@Override
	public String toString() {
		return String.format("new Color3D(%+.10f, %+.10f, %+.10f)", Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
	}
	
	/**
	 * Compares {@code object} to this {@code Color3D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Color3D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Color3D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Color3D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Color3D)) {
			return false;
		} else if(!equal(this.component1, Color3D.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Color3D.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Color3D.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is infinite, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is infinite, {@code false} otherwise
	 */
	public boolean hasInfinites() {
		return isInfinite(this.component1) || isInfinite(this.component2) || isInfinite(this.component3);
	}
	
	/**
	 * Returns {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is equal to {@code Float.NaN}, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, at least one of the component values of this {@code Color3D} instance is equal to {@code Float.NaN}, {@code false} otherwise
	 */
	public boolean hasNaNs() {
		return isNaN(this.component1) || isNaN(this.component2) || isNaN(this.component3);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is black, {@code false} otherwise.
	 * <p>
	 * A {@code Color3D} instance is black if all component values are {@code 0.0D}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is black, {@code false} otherwise
	 */
	public boolean isBlack() {
		return isGrayscale() && isZero(this.component1);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isBlue(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue() {
		return isBlue(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered blue, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is considered blue if, and only if, {@code color.getB() - thresholdR >= color.getR()} and {@code color.getB() - thresholdG >= color.getG()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdG the threshold for the G-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered blue, {@code false} otherwise
	 */
	public boolean isBlue(final double thresholdR, final double thresholdG) {
		return getB() - thresholdR >= getR() && getB() - thresholdG >= getG();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered cyan, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered cyan, {@code false} otherwise
	 */
	public boolean isCyan() {
		return isGreen(1.0D, 0.5D) && isBlue(1.0D, 0.5D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is a grayscale color, {@code false} otherwise.
	 * <p>
	 * A {@code Color3D} instance is a grayscale color if all component values are equal.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is a grayscale color, {@code false} otherwise
	 */
	public boolean isGrayscale() {
		return equal(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered green, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isGreen(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen() {
		return isGreen(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered green, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is considered green if, and only if, {@code color.getG() - thresholdR >= color.getR()} and {@code color.getG() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdR the threshold for the R-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered green, {@code false} otherwise
	 */
	public boolean isGreen(final double thresholdR, final double thresholdB) {
		return getG() - thresholdR >= getR() && getG() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered magenta, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered magenta, {@code false} otherwise
	 */
	public boolean isMagenta() {
		return isRed(1.0D, 0.5D) && isBlue(0.5D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered red, {@code false} otherwise.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * color.isRed(1.0D, 1.0D);
	 * }
	 * </pre>
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed() {
		return isRed(1.0D, 1.0D);
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered red, {@code false} otherwise.
	 * <p>
	 * The {@code Color3D} instance {@code color} is considered red if, and only if, {@code color.getR() - thresholdG >= color.getG()} and {@code color.getR() - thresholdB >= color.getB()}.
	 * 
	 * @param thresholdG the threshold for the G-component
	 * @param thresholdB the threshold for the B-component
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered red, {@code false} otherwise
	 */
	public boolean isRed(final double thresholdG, final double thresholdB) {
		return getR() - thresholdG >= getG() && getR() - thresholdB >= getB();
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is white, {@code false} otherwise.
	 * <p>
	 * A {@code Color3D} instance is white if all component values are equal and greater than or equal to {@code 1.0D}.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is white, {@code false} otherwise
	 */
	public boolean isWhite() {
		return isGrayscale() && this.component1 >= 1.0D;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code Color3D} instance is considered yellow, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code Color3D} instance is considered yellow, {@code false} otherwise
	 */
	public boolean isYellow() {
		return isRed(0.5D, 1.0D) && isGreen(0.5D, 1.0D);
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
	 * Returns the average component value of this {@code Color3D} instance.
	 * 
	 * @return the average component value of this {@code Color3D} instance
	 */
	public double average() {
		return (this.component1 + this.component2 + this.component3) / 3.0D;
	}
	
	/**
	 * Returns the value of the B-component.
	 * 
	 * @return the value of the B-component
	 */
	public double getB() {
		return this.component3;
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public double getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of component 2.
	 * 
	 * @return the value of component 2
	 */
	public double getComponent2() {
		return this.component2;
	}
	
	/**
	 * Returns the value of component 3.
	 * 
	 * @return the value of component 3
	 */
	public double getComponent3() {
		return this.component3;
	}
	
	/**
	 * Returns the value of the G-component.
	 * 
	 * @return the value of the G-component
	 */
	public double getG() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the R-component.
	 * 
	 * @return the value of the R-component
	 */
	public double getR() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public double getX() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the Y-component.
	 * 
	 * @return the value of the Y-component
	 */
	public double getY() {
		return this.component2;
	}
	
	/**
	 * Returns the value of the Z-component.
	 * 
	 * @return the value of the Z-component
	 */
	public double getZ() {
		return this.component3;
	}
	
	/**
	 * Returns the lightness of this {@code Color3D} instance.
	 * 
	 * @return the lightness of this {@code Color3D} instance
	 */
	public double lightness() {
		return (maximum() + minimum()) / 2.0D;
	}
	
	/**
	 * Returns the relative luminance of this {@code Color3D} instance.
	 * <p>
	 * The algorithm used is only suitable for linear {@code Color3D} instances.
	 * 
	 * @return the relative luminance of this {@code Color3D} instance
	 */
	public double luminance() {
		return this.component1 * 0.212671D + this.component2 * 0.715160D + this.component3 * 0.072169D;
	}
	
	/**
	 * Returns the largest component value.
	 * 
	 * @return the largest component value
	 */
	public double maximum() {
		return max(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns the smallest component value.
	 * 
	 * @return the smallest component value
	 */
	public double minimum() {
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
		return toInt(Doubles.saturate(getB()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of component 1 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 1 as an {@code int}
	 */
	public int getAsIntComponent1() {
		return toInt(Doubles.saturate(getComponent1()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of component 2 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 2 as an {@code int}
	 */
	public int getAsIntComponent2() {
		return toInt(Doubles.saturate(getComponent2()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of component 3 as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of component 3 as an {@code int}
	 */
	public int getAsIntComponent3() {
		return toInt(Doubles.saturate(getComponent3()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of the G-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the G-component as an {@code int}
	 */
	public int getAsIntG() {
		return toInt(Doubles.saturate(getG()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of the R-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the R-component as an {@code int}
	 */
	public int getAsIntR() {
		return toInt(Doubles.saturate(getR()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of the X-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the X-component as an {@code int}
	 */
	public int getAsIntX() {
		return toInt(Doubles.saturate(getX()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of the Y-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Y-component as an {@code int}
	 */
	public int getAsIntY() {
		return toInt(Doubles.saturate(getY()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns the value of the Z-component as an {@code int}.
	 * <p>
	 * This method assumes that the component value is within the range [0.0, 1.0]. A component value outside of this range will be saturated or clamped.
	 * 
	 * @return the value of the Z-component as an {@code int}
	 */
	public int getAsIntZ() {
		return toInt(Doubles.saturate(getZ()) * 255.0D + 0.5D);
	}
	
	/**
	 * Returns a hash code for this {@code Color3D} instance.
	 * 
	 * @return a hash code for this {@code Color3D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(this.component1), Double.valueOf(this.component2), Double.valueOf(this.component3));
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
		final int r = getAsIntR();
		final int g = getAsIntG();
		final int b = getAsIntB();
		
		return packedIntComponentOrder.pack(r, g, b, 255);
	}
	
	/**
	 * Returns an {@code int} with the component values of this {@code Color3D} instance in RGBE-form.
	 * 
	 * @return an {@code int} with the component values of this {@code Color3D} instance in RGBE-form
	 */
	public int toRGBE() {
		final double maximum = max(this.component1, this.component2, this.component3);
		
		if(maximum < 1.0e-32D) {
			return 0;
		}
		
		double mantissa = maximum;
		
		int exponent = 0;
		
		if(maximum > 1.0D) {
			while(mantissa > 1.0D) {
				mantissa *= 0.5D;
				
				exponent++;
			}
		} else if(maximum <= 0.5D) {
			while(mantissa <= 0.5D) {
				mantissa *= 2.0D;
				
				exponent--;
			}
		}
		
//		Find out if 255.0D should be 256.0D or maybe 255.5D? In Sunflow it is 255.0D and other places mention 256.0D in some cases.
//		Performing multiple conversions seems to decrease the values over time.
		final double multiplier = (mantissa * 255.0D) / maximum;
		
		final int r = toInt(this.component1 * multiplier) << 24;
		final int g = toInt(this.component2 * multiplier) << 16;
		final int b = toInt(this.component3 * multiplier) <<  8;
		final int e = exponent + 128;
		
		final int colorRGBE = r | g | b | e;
		
		return colorRGBE;
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
			dataOutput.writeDouble(this.component1);
			dataOutput.writeDouble(this.component2);
			dataOutput.writeDouble(this.component3);
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
		final double component1 = colorLHS.component1 + colorRHS.component1;
		final double component2 = colorLHS.component2 + colorRHS.component2;
		final double component3 = colorLHS.component3 + colorRHS.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorLHS.component1 + scalarRHS;
		final double component2 = colorLHS.component2 + scalarRHS;
		final double component3 = colorLHS.component3 + scalarRHS;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1;
		final double component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2;
		final double component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 * scalarMultiply;
		final double component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 * scalarMultiply;
		final double component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 * scalarMultiply;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 * colorMultiplyC.component1 * scalarMultiply / scalarDivide;
		final double component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 * colorMultiplyC.component2 * scalarMultiply / scalarDivide;
		final double component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 * colorMultiplyC.component3 * scalarMultiply / scalarDivide;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 / scalarDivide;
		final double component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 / scalarDivide;
		final double component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 / scalarDivide;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorAdd.component1 + colorMultiplyA.component1 * colorMultiplyB.component1 * scalarMultiply / scalarDivide;
		final double component2 = colorAdd.component2 + colorMultiplyA.component2 * colorMultiplyB.component2 * scalarMultiply / scalarDivide;
		final double component3 = colorAdd.component3 + colorMultiplyA.component3 * colorMultiplyB.component3 * scalarMultiply / scalarDivide;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Adds the component values of {@code colorRHS} to the component values of {@code colorLHS}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the addition.
	 * <p>
	 * If either {@code colorLHS} or {@code colorRHS} are {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * This method differs from {@link #add(Color3D, Color3D)} in that it assumes {@code colorLHS} to be an average color sample. It uses a stable moving average algorithm to compute a new average color sample as a result of adding {@code colorRHS}.
	 * This method is suitable for Monte Carlo-method based algorithms.
	 * 
	 * @param colorLHS the {@code Color3D} instance on the left-hand side
	 * @param colorRHS the {@code Color3D} instance on the right-hand side
	 * @param sampleCount the current sample count
	 * @return a new {@code Color3D} instance with the result of the addition
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D addSample(final Color3D colorLHS, final Color3D colorRHS, final int sampleCount) {
		final double component1 = colorLHS.component1 + ((colorRHS.component1 - colorLHS.component1) / sampleCount);
		final double component2 = colorLHS.component2 + ((colorRHS.component2 - colorLHS.component2) / sampleCount);
		final double component3 = colorLHS.component3 + ((colorRHS.component3 - colorLHS.component3) / sampleCount);
		
		return new Color3D(component1, component2, component3);
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
	 * @param tComponent1 the factor to use for component 1 in the blending process
	 * @param tComponent2 the factor to use for component 2 in the blending process
	 * @param tComponent3 the factor to use for component 3 in the blending process
	 * @return a new {@code Color3D} instance with the result of the blend
	 * @throws NullPointerException thrown if, and only if, either {@code colorLHS} or {@code colorRHS} are {@code null}
	 */
	public static Color3D blend(final Color3D colorLHS, final Color3D colorRHS, final double tComponent1, final double tComponent2, final double tComponent3) {
		final double component1 = lerp(colorLHS.component1, colorRHS.component1, tComponent1);
		final double component2 = lerp(colorLHS.component2, colorRHS.component2, tComponent2);
		final double component3 = lerp(colorLHS.component3, colorRHS.component3, tComponent3);
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Converts {@code color} from the RGB-color space to the XYZ-color space using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance in RGB-color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D convertRGBToXYZUsingPBRT(final Color3D color) {
		final double x = 0.412453D * color.getR() + 0.357580D * color.getG() + 0.180423D * color.getB();
		final double y = 0.212671D * color.getR() + 0.715160D * color.getG() + 0.072169D * color.getB();
		final double z = 0.019334D * color.getR() + 0.119193D * color.getG() + 0.950227D * color.getB();
		
		return new Color3D(x, y, z);
	}
	
	/**
	 * Converts {@code color} from the RGB-color space to the XYZ-color space using sRGB.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance in RGB-color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D convertRGBToXYZUsingSRGB(final Color3D color) {
		return ColorSpaceD.S_R_G_B.convertRGBToXYZ(color);
	}
	
	/**
	 * Converts {@code color} from the XYZ-color space to the RGB-color space using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance in XYZ-color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D convertXYZToRGBUsingPBRT(final Color3D color) {
		final double r = +3.240479D * color.getX() - 1.537150D * color.getY() - 0.498535D * color.getZ();
		final double g = -0.969256D * color.getX() + 1.875991D * color.getY() + 0.041556D * color.getZ();
		final double b = +0.055648D * color.getX() - 0.204043D * color.getY() + 1.057311D * color.getZ();
		
		return new Color3D(r, g, b);
	}
	
	/**
	 * Converts {@code color} from the XYZ-color space to the RGB-color space using sRGB.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the conversion.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance in XYZ-color space
	 * @return a new {@code Color3D} instance with the result of the conversion
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D convertXYZToRGBUsingSRGB(final Color3D color) {
		return ColorSpaceD.S_R_G_B.convertXYZToRGB(color);
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
		final double component1 = finiteOrDefault(colorLHS.component1 / colorRHS.component1, 0.0D);
		final double component2 = finiteOrDefault(colorLHS.component2 / colorRHS.component2, 0.0D);
		final double component3 = finiteOrDefault(colorLHS.component3 / colorRHS.component3, 0.0D);
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = finiteOrDefault(colorLHS.component1 / scalarRHS, 0.0D);
		final double component2 = finiteOrDefault(colorLHS.component2 / scalarRHS, 0.0D);
		final double component3 = finiteOrDefault(colorLHS.component3 / scalarRHS, 0.0D);
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Returns a new {@code Color3D} instance with the result of the conversion from RGBE to RGB.
	 * 
	 * @param colorRGBE an {@code int} with a color in the RGBE format
	 * @return a new {@code Color3D} instance with the result of the conversion from RGBE to RGB
	 */
	public static Color3D fromRGBE(final int colorRGBE) {
		final double exponent = R_G_B_EXPONENTS[colorRGBE & 0xFF];
		
		final double component1 = exponent * ((colorRGBE >>> 24)         + 0.5D);
		final double component2 = exponent * (((colorRGBE >> 16) & 0xFF) + 0.5D);
		final double component3 = exponent * (((colorRGBE >>  8) & 0xFF) + 0.5D);
		
		return new Color3D(component1, component2, component3);
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
	 * Returns a grayscale {@code Color3D} instance based on {@code color.getComponent1()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.getComponent1()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleComponent1(final Color3D color) {
		return new Color3D(color.component1);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.getComponent2()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.getComponent2()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleComponent2(final Color3D color) {
		return new Color3D(color.component2);
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.getComponent3()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.getComponent3()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleComponent3(final Color3D color) {
		return new Color3D(color.component3);
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
	 * Returns a grayscale {@code Color3D} instance based on {@code color.luminance()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.luminance()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleLuminance(final Color3D color) {
		return new Color3D(color.luminance());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.maximum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.maximum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleMaximum(final Color3D color) {
		return new Color3D(color.maximum());
	}
	
	/**
	 * Returns a grayscale {@code Color3D} instance based on {@code color.minimum()}.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a grayscale {@code Color3D} instance based on {@code color.minimum()}
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D grayscaleMinimum(final Color3D color) {
		return new Color3D(color.minimum());
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
		final double component1 = 1.0D - color.component1;
		final double component2 = 1.0D - color.component2;
		final double component3 = 1.0D - color.component3;
		
		return new Color3D(component1, component2, component3);
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
	public static Color3D maximum(final Color3D colorA, final Color3D colorB) {
		final double component1 = max(colorA.component1, colorB.component1);
		final double component2 = max(colorA.component2, colorB.component2);
		final double component3 = max(colorA.component3, colorB.component3);
		
		return new Color3D(component1, component2, component3);
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
	 * If at least one of the component values are negative, consider calling {@link #minimumTo0(Color3D)} before calling this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3D a = new Color3D(0.0D, 1.0D, 2.0D);
	 * Color3D b = Color3D.maximumTo1(a);
	 * 
	 * //a.getComponent1() = 0.0D, a.getComponent2() = 1.0D, a.getComponent3() = 2.0D
	 * //b.getComponent1() = 0.0D, b.getComponent2() = 0.5D, b.getComponent3() = 1.0D
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the division, or {@code color} if no division occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D maximumTo1(final Color3D color) {
		final double maximum = color.maximum();
		
		if(maximum > 1.0D) {
			final double component1 = color.component1 / maximum;
			final double component2 = color.component2 / maximum;
			final double component3 = color.component3 / maximum;
			
			return new Color3D(component1, component2, component3);
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
	public static Color3D minimum(final Color3D colorA, final Color3D colorB) {
		final double component1 = min(colorA.component1, colorB.component1);
		final double component2 = min(colorA.component2, colorB.component2);
		final double component3 = min(colorA.component3, colorB.component3);
		
		return new Color3D(component1, component2, component3);
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
	 * Consider calling {@link #maximumTo1(Color3D)} after a call to this method.
	 * <p>
	 * To use this method consider the following example:
	 * <pre>
	 * {@code
	 * Color3D a = new Color3D(-2.0D, 0.0D, 1.0D);
	 * Color3D b = Color3D.minimumTo0(a);
	 * 
	 * //a.getComponent1() = -2.0D, a.getComponent2() = 0.0D, a.getComponent3() = 1.0D
	 * //b.getComponent1() =  0.0D, b.getComponent2() = 2.0D, b.getComponent3() = 3.0D
	 * }
	 * </pre>
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the addition, or {@code color} if no addition occurred
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D minimumTo0(final Color3D color) {
		final double minimum = color.minimum();
		
		if(minimum < 0.0D) {
			final double component1 = color.component1 + -minimum;
			final double component2 = color.component2 + -minimum;
			final double component3 = color.component3 + -minimum;
			
			return new Color3D(component1, component2, component3);
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
		final double component1 = colorLHS.component1 * colorRHS.component1;
		final double component2 = colorLHS.component2 * colorRHS.component2;
		final double component3 = colorLHS.component3 * colorRHS.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorA.component1 * colorB.component1 * colorC.component1;
		final double component2 = colorA.component2 * colorB.component2 * colorC.component2;
		final double component3 = colorA.component3 * colorB.component3 * colorC.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorA.component1 * colorB.component1 * colorC.component1 * colorD.component1;
		final double component2 = colorA.component2 * colorB.component2 * colorC.component2 * colorD.component2;
		final double component3 = colorA.component3 * colorB.component3 * colorC.component3 * colorD.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorA.component1 * colorB.component1 * scalarC;
		final double component2 = colorA.component2 * colorB.component2 * scalarC;
		final double component3 = colorA.component3 * colorB.component3 * scalarC;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorLHS.component1 * scalarRHS;
		final double component2 = colorLHS.component2 * scalarRHS;
		final double component3 = colorLHS.component3 * scalarRHS;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = max(colorLHS.component1 * scalarRHS, 0.0D);
		final double component2 = max(colorLHS.component2 * scalarRHS, 0.0D);
		final double component3 = max(colorLHS.component3 * scalarRHS, 0.0D);
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = -color.component1;
		final double component2 = -color.component2;
		final double component3 = -color.component3;
		
		return new Color3D(component1, component2, component3);
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
		final double sum = color.component1 + color.component2 + color.component3;
		
		if(sum < 1.0e-6D) {
			return color;
		}
		
		final double sumReciprocal = 1.0D / sum;
		
		final double component1 = color.component1 * sumReciprocal;
		final double component2 = color.component2 * sumReciprocal;
		final double component3 = color.component3 * sumReciprocal;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Normalizes the component values of {@code color} based on its luminance.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the normalization.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the normalization
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D normalizeLuminance(final Color3D color) {
		final double luminance = color.luminance();
		
		if(luminance > 0.0D) {
			return divide(color, luminance);
		}
		
		return WHITE;
	}
	
	/**
	 * Returns a {@code Color3D} instance with random component values.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(Doubles.random(), Doubles.random(), Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with random component values
	 */
	public static Color3D random() {
		final double component1 = Doubles.random();
		final double component2 = Doubles.random();
		final double component3 = Doubles.random();
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Returns a {@code Color3D} instance with a random component 1 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(Doubles.random(), 0.0D, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with a random component 1 value
	 */
	public static Color3D randomComponent1() {
		final double component1 = Doubles.random();
		final double component2 = 0.0D;
		final double component3 = 0.0D;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Returns a {@code Color3D} instance with a random component 2 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(0.0D, Doubles.random(), 0.0D);
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with a random component 2 value
	 */
	public static Color3D randomComponent2() {
		final double component1 = 0.0D;
		final double component2 = Doubles.random();
		final double component3 = 0.0D;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Returns a {@code Color3D} instance with a random component 3 value.
	 * <p>
	 * Calling this method is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Color3D(0.0D, 0.0D, Doubles.random());
	 * }
	 * </pre>
	 * 
	 * @return a {@code Color3D} instance with a random component 3 value
	 */
	public static Color3D randomComponent3() {
		final double component1 = 0.0D;
		final double component2 = 0.0D;
		final double component3 = Doubles.random();
		
		return new Color3D(component1, component2, component3);
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
			final double component1 = dataInput.readDouble();
			final double component2 = dataInput.readDouble();
			final double component3 = dataInput.readDouble();
			
			return new Color3D(component1, component2, component3);
		} catch(final IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	
	/**
	 * Redoes gamma correction on the component values of {@code color} using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D redoGammaCorrectionPBRT(final Color3D color) {
		final double component1 = color.component1 <= 0.0031308D ? 12.92D * color.component1 : 1.055D * pow(color.component1, 1.0D / 2.4D) - 0.055D;
		final double component2 = color.component2 <= 0.0031308D ? 12.92D * color.component2 : 1.055D * pow(color.component2, 1.0D / 2.4D) - 0.055D;
		final double component3 = color.component3 <= 0.0031308D ? 12.92D * color.component3 : 1.055D * pow(color.component3, 1.0D / 2.4D) - 0.055D;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Redoes gamma correction on the component values of {@code color} using sRGB.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D redoGammaCorrectionSRGB(final Color3D color) {
		return ColorSpaceD.S_R_G_B.redoGammaCorrection(color);
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
	 * Saturates or clamps {@code color} to the range {@code [Doubles.min(edgeA, edgeB), Doubles.max(edgeA, edgeB)]}.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @param edgeA the minimum or maximum component value
	 * @param edgeB the maximum or minimum component value
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D saturate(final Color3D color, final double edgeA, final double edgeB) {
		final double component1 = Doubles.saturate(color.component1, edgeA, edgeB);
		final double component2 = Doubles.saturate(color.component2, edgeA, edgeB);
		final double component3 = Doubles.saturate(color.component3, edgeA, edgeB);
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = color.component1 * 0.393D + color.component2 * 0.769D + color.component3 * 0.189D;
		final double component2 = color.component1 * 0.349D + color.component2 * 0.686D + color.component3 * 0.168D;
		final double component3 = color.component1 * 0.272D + color.component2 * 0.534D + color.component3 * 0.131D;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = Doubles.sqrt(color.component1);
		final double component2 = Doubles.sqrt(color.component2);
		final double component3 = Doubles.sqrt(color.component3);
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorLHS.component1 - colorRHS.component1;
		final double component2 = colorLHS.component2 - colorRHS.component2;
		final double component3 = colorLHS.component3 - colorRHS.component3;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Subtracts the component values of {@code b} from the component values of {@code a} and the component values of {@code c} from the previous result.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code a}, {@code b} or {@code c} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Color3D} instance
	 * @param b a {@code Color3D} instance
	 * @param c a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code a}, {@code b} or {@code c} are {@code null}
	 */
	public static Color3D subtract(final Color3D a, final Color3D b, final Color3D c) {
		final double component1 = a.component1 - b.component1 - c.component1;
		final double component2 = a.component2 - b.component2 - c.component2;
		final double component3 = a.component3 - b.component3 - c.component3;
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Subtracts the component values of {@code b} from the component values of {@code a} and {@code c} from the previous result.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the subtraction.
	 * <p>
	 * If either {@code a} or {@code b} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param a a {@code Color3D} instance
	 * @param b a {@code Color3D} instance
	 * @param c a {@code double} value
	 * @return a new {@code Color3D} instance with the result of the subtraction
	 * @throws NullPointerException thrown if, and only if, either {@code a} or {@code b} are {@code null}
	 */
	public static Color3D subtract(final Color3D a, final Color3D b, final double c) {
		final double component1 = a.component1 - b.component1 - c;
		final double component2 = a.component2 - b.component2 - c;
		final double component3 = a.component3 - b.component3 - c;
		
		return new Color3D(component1, component2, component3);
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
		final double component1 = colorLHS.component1 - scalarRHS;
		final double component2 = colorLHS.component2 - scalarRHS;
		final double component3 = colorLHS.component3 - scalarRHS;
		
		return new Color3D(component1, component2, component3);
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
	 * Color3D.toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0D, Doubles.MIN_VALUE);
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
		return toneMapFilmicCurve(color, exposure, a, b, c, d, e, 0.0D, MIN_VALUE);
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
	 * double component11 = Doubles.max(color.getComponent1() * exposure - subtract, minimum);
	 * double component12 = Doubles.saturate((component11 * (a * component11 + b)) / (component11 * (c * component11 + d) + e));
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
		final double component11 = max(color.component1 * exposure - subtract, minimum);
		final double component21 = max(color.component2 * exposure - subtract, minimum);
		final double component31 = max(color.component3 * exposure - subtract, minimum);
		
		final double component12 = Doubles.saturate((component11 * (a * component11 + b)) / (component11 * (c * component11 + d) + e));
		final double component22 = Doubles.saturate((component21 * (a * component21 + b)) / (component21 * (c * component21 + d) + e));
		final double component32 = Doubles.saturate((component31 * (a * component31 + b)) / (component31 * (c * component31 + d) + e));
		
		return new Color3D(component12, component22, component32);
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
		
		final double component11 = color.component1 * exposure;
		final double component21 = color.component2 * exposure;
		final double component31 = color.component3 * exposure;
		
		final double component12 = component11 / (1.0D + component11);
		final double component22 = component21 / (1.0D + component21);
		final double component32 = component31 / (1.0D + component31);
		
		return new Color3D(component12, component22, component32);
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
		
		final double component11 = color.component1 * exposure;
		final double component21 = color.component2 * exposure;
		final double component31 = color.component3 * exposure;
		
		final double component12 = component11 * (1.0D + component11 / (lWhite * lWhite)) / (1.0D + component11);
		final double component22 = component21 * (1.0D + component21 / (lWhite * lWhite)) / (1.0D + component21);
		final double component32 = component31 * (1.0D + component31 / (lWhite * lWhite)) / (1.0D + component31);
		
		return new Color3D(component12, component22, component32);
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
		final double component11 = color.component1 * exposure;
		final double component21 = color.component2 * exposure;
		final double component31 = color.component3 * exposure;
		
		final double component12 = 1.0D - exp(-component11 * exposure);
		final double component22 = 1.0D - exp(-component21 * exposure);
		final double component32 = 1.0D - exp(-component31 * exposure);
		
		return new Color3D(component12, component22, component32);
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
		
		final double component11 = color.component1 * exposure;
		final double component21 = color.component2 * exposure;
		final double component31 = color.component3 * exposure;
		
		final double component12 = component11 / (component11 + 0.155D) * 1.019D;
		final double component22 = component21 / (component21 + 0.155D) * 1.019D;
		final double component32 = component31 / (component31 + 0.155D) * 1.019D;
		
		return new Color3D(component12, component22, component32);
	}
	
	/**
	 * Undoes gamma correction on the component values of {@code color} using the algorithm provided by PBRT.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D undoGammaCorrectionPBRT(final Color3D color) {
		final double component1 = color.component1 <= 0.04045D ? color.component1 * 1.0D / 12.92D : pow((color.component1 + 0.055D) * 1.0D / 1.055D, 2.4D);
		final double component2 = color.component2 <= 0.04045D ? color.component2 * 1.0D / 12.92D : pow((color.component2 + 0.055D) * 1.0D / 1.055D, 2.4D);
		final double component3 = color.component3 <= 0.04045D ? color.component3 * 1.0D / 12.92D : pow((color.component3 + 0.055D) * 1.0D / 1.055D, 2.4D);
		
		return new Color3D(component1, component2, component3);
	}
	
	/**
	 * Undoes gamma correction on the component values of {@code color} using sRGB.
	 * <p>
	 * Returns a new {@code Color3D} instance with the result of the operation.
	 * <p>
	 * If {@code color} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param color a {@code Color3D} instance
	 * @return a new {@code Color3D} instance with the result of the operation
	 * @throws NullPointerException thrown if, and only if, {@code color} is {@code null}
	 */
	public static Color3D undoGammaCorrectionSRGB(final Color3D color) {
		return ColorSpaceD.S_R_G_B.undoGammaCorrection(color);
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
	 * Color3D.array(length, () -> Color3D.BLACK);
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D.BLACK}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3D[] array(final int length) {
		return array(length, () -> BLACK);
	}
	
	/**
	 * Returns a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D} instances supplied by {@code supplier}.
	 * <p>
	 * If {@code length} is less than {@code 0}, an {@code IllegalArgumentException} will be thrown.
	 * <p>
	 * If {@code supplier} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param length the length of the array
	 * @param supplier a {@code Supplier}
	 * @return a {@code Color3D[]} with a length of {@code length} and contains {@code Color3D} instances supplied by {@code supplier}
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 * @throws NullPointerException thrown if, and only if, {@code supplier} is {@code null}
	 */
	public static Color3D[] array(final int length, final Supplier<Color3D> supplier) {
		final Color3D[] colors = new Color3D[ParameterArguments.requireRange(length, 0, Integer.MAX_VALUE, "length")];
		
		Objects.requireNonNull(supplier, "supplier == null");
		
		for(int i = 0; i < colors.length; i++) {
			colors[i] = Objects.requireNonNull(supplier.get());
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
	 * Color3D.array(length, () -> Color3D.random());
	 * }
	 * </pre>
	 * 
	 * @param length the length of the array
	 * @return a {@code Color3D[]} with a length of {@code length} and contains random {@code Color3D} instances
	 * @throws IllegalArgumentException thrown if, and only if, {@code length} is less than {@code 0}
	 */
	public static Color3D[] arrayRandom(final int length) {
		return array(length, () -> random());
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
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
		
		ParameterArguments.requireExact(array.length % arrayComponentOrder.getComponentCount(), 0, "array.length % arrayComponentOrder.getComponentCount()");
		
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
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double[] doCreateRGBExponents() {
		final double[] exponents = new double[256];
		
		exponents[0] = 0.0D;
		
		for(int i = 1; i < exponents.length; i++) {
			final int exponentInt = i - (128 + 8);
			
			double exponentDouble = 1.0D;
			
			if(exponentInt > 0) {
				for(int j = 0; j < +exponentInt; j++) {
					exponentDouble *= 2.0D;
				}
			} else {
				for(int j = 0; j < -exponentInt; j++) {
					exponentDouble *= 0.5D;
				}
			}
			
			exponents[i] = exponentDouble;
		}
		
		return exponents;
	}
}