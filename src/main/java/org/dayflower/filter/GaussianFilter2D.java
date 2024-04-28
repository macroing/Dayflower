/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
 * A {@code GaussianFilter2D} is an implementation of {@link Filter2D} that represents a Gaussian filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GaussianFilter2D extends Filter2D {
	private final double falloff;
	private final double x;
	private final double y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GaussianFilter2D} given {@code 2.0D}, {@code 2.0D} and {@code 2.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GaussianFilter2D(2.0D, 2.0D, 2.0D);
	 * }
	 * </pre>
	 */
	public GaussianFilter2D() {
		this(2.0D, 2.0D, 2.0D);
	}
	
	/**
	 * Constructs a new {@code GaussianFilter2D} given {@code resolutionX}, {@code resolutionY} and {@code falloff}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param falloff the falloff to use
	 */
	public GaussianFilter2D(final double resolutionX, final double resolutionY, final double falloff) {
		super(resolutionX, resolutionY);
		
		this.falloff = falloff;
		this.x = Doubles.exp(-falloff * resolutionX * resolutionX);
		this.y = Doubles.exp(-falloff * resolutionY * resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code GaussianFilter2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code GaussianFilter2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new GaussianFilter2D(%s, %s, %s)", Strings.toNonScientificNotationJava(getResolutionX()), Strings.toNonScientificNotationJava(getResolutionY()), Strings.toNonScientificNotationJava(this.falloff));
	}
	
	/**
	 * Compares {@code object} to this {@code GaussianFilter2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GaussianFilter2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GaussianFilter2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GaussianFilter2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GaussianFilter2D)) {
			return false;
		} else if(!Doubles.equals(getResolutionX(), GaussianFilter2D.class.cast(object).getResolutionX())) {
			return false;
		} else if(!Doubles.equals(getResolutionY(), GaussianFilter2D.class.cast(object).getResolutionY())) {
			return false;
		} else if(!Doubles.equals(this.falloff, GaussianFilter2D.class.cast(object).falloff)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code GaussianFilter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public double evaluate(final double x, final double y) {
		return doGaussian(x, this.x, this.falloff) * doGaussian(y, this.y, this.falloff);
	}
	
	/**
	 * Returns the falloff used by this {@code GaussianFilter2D} instance.
	 * 
	 * @return the falloff used by this {@code GaussianFilter2D} instance
	 */
	public double getFalloff() {
		return this.falloff;
	}
	
	/**
	 * Returns a hash code for this {@code GaussianFilter2D} instance.
	 * 
	 * @return a hash code for this {@code GaussianFilter2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(getResolutionX()), Double.valueOf(getResolutionY()), Double.valueOf(this.falloff));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doGaussian(final double a, final double b, final double c) {
		return Doubles.max(0.0D, Doubles.exp(-c * a * a) - b);
	}
}