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
package org.dayflower.filter;

import java.util.Objects;

import org.macroing.java.lang.Doubles;
import org.macroing.java.lang.Strings;

/**
 * A {@code MitchellFilter2D} is an implementation of {@link Filter2D} that represents a Mitchell filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MitchellFilter2D extends Filter2D {
	private final double b;
	private final double c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MitchellFilter2D} given {@code 2.0D}, {@code 2.0D}, {@code 1.0D / 3.0D} and {@code 1.0D / 3.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MitchellFilter2D(2.0D, 2.0D, 1.0D / 3.0D, 1.0D / 3.0D);
	 * }
	 * </pre>
	 */
	public MitchellFilter2D() {
		this(2.0D, 2.0D, 1.0D / 3.0D, 1.0D / 3.0D);
	}
	
	/**
	 * Constructs a new {@code MitchellFilter2D} given {@code resolutionX}, {@code resolutionY}, {@code b} and {@code c}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param b the B-coefficient to use
	 * @param c the C-coefficient to use
	 */
	public MitchellFilter2D(final double resolutionX, final double resolutionY, final double b, final double c) {
		super(resolutionX, resolutionY);
		
		this.b = b;
		this.c = c;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code MitchellFilter2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code MitchellFilter2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new MitchellFilter2D(%s, %s, %s, %s)", Strings.toNonScientificNotationJava(getResolutionX()), Strings.toNonScientificNotationJava(getResolutionY()), Strings.toNonScientificNotationJava(this.b), Strings.toNonScientificNotationJava(this.c));
	}
	
	/**
	 * Compares {@code object} to this {@code MitchellFilter2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MitchellFilter2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MitchellFilter2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MitchellFilter2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MitchellFilter2D)) {
			return false;
		} else if(!Doubles.equals(getResolutionX(), MitchellFilter2D.class.cast(object).getResolutionX())) {
			return false;
		} else if(!Doubles.equals(getResolutionY(), MitchellFilter2D.class.cast(object).getResolutionY())) {
			return false;
		} else if(!Doubles.equals(this.b, MitchellFilter2D.class.cast(object).b)) {
			return false;
		} else if(!Doubles.equals(this.c, MitchellFilter2D.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code MitchellFilter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public double evaluate(final double x, final double y) {
		return doMitchell(x * getResolutionXReciprocal(), this.b, this.c) * doMitchell(y * getResolutionYReciprocal(), this.b, this.c);
	}
	
	/**
	 * Returns the B-coefficient.
	 * 
	 * @return the B-coefficient
	 */
	public double getB() {
		return this.b;
	}
	
	/**
	 * Returns the C-coefficient.
	 * 
	 * @return the C-coefficient
	 */
	public double getC() {
		return this.c;
	}
	
	/**
	 * Returns a hash code for this {@code MitchellFilter2D} instance.
	 * 
	 * @return a hash code for this {@code MitchellFilter2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(getResolutionX()), Double.valueOf(getResolutionY()), Double.valueOf(this.b), Double.valueOf(this.c));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doMitchell(final double x, final double b, final double c) {
		final double x1 = Doubles.abs(2.0D * x);
		final double x2 = x1 * x1;
		final double x3 = x1 * x2;
		
		if(x1 > 1.0D) {
			return ((-b - 6.0D * c) * x3 + (6.0D * b + 30.0D * c) * x2 + (-12.0D * b - 48.0D * c) * x1 + (8.0D * b + 24.0D * c)) * (1.0D / 6.0D);
		}
		
		return ((12.0D - 9.0D * b - 6.0D * c) * x3 + (-18.0D + 12.0D * b + 6.0D * c) * x2 + (6.0D - 2.0D * b)) * (1.0D / 6.0D);
	}
}