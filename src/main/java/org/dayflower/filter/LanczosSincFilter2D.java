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
 * A {@code LanczosSincFilter2D} is an implementation of {@link Filter2D} that represents a Lanczos-Sinc filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LanczosSincFilter2D extends Filter2D {
	private final double tau;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LanczosSincFilter2D} given {@code 4.0D}, {@code 4.0D} and {@code 3.0D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LanczosSincFilter2D(4.0D, 4.0D, 3.0D);
	 * }
	 * </pre>
	 */
	public LanczosSincFilter2D() {
		this(4.0D, 4.0D, 3.0D);
	}
	
	/**
	 * Constructs a new {@code LanczosSincFilter2D} given {@code resolutionX}, {@code resolutionY} and {@code tau}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param tau the Tau to use
	 */
	public LanczosSincFilter2D(final double resolutionX, final double resolutionY, final double tau) {
		super(resolutionX, resolutionY);
		
		this.tau = tau;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code LanczosSincFilter2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code LanczosSincFilter2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new LanczosSincFilter2D(%s, %s, %s)", Strings.toNonScientificNotationJava(getResolutionX()), Strings.toNonScientificNotationJava(getResolutionY()), Strings.toNonScientificNotationJava(this.tau));
	}
	
	/**
	 * Compares {@code object} to this {@code LanczosSincFilter2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LanczosSincFilter2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LanczosSincFilter2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LanczosSincFilter2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LanczosSincFilter2D)) {
			return false;
		} else if(!Doubles.equals(getResolutionX(), LanczosSincFilter2D.class.cast(object).getResolutionX())) {
			return false;
		} else if(!Doubles.equals(getResolutionY(), LanczosSincFilter2D.class.cast(object).getResolutionY())) {
			return false;
		} else if(!Doubles.equals(this.tau, LanczosSincFilter2D.class.cast(object).tau)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code LanczosSincFilter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public double evaluate(final double x, final double y) {
		return doLanczosSinc(x * getResolutionXReciprocal(), this.tau) * doLanczosSinc(y * getResolutionYReciprocal(), this.tau);
	}
	
	/**
	 * Returns the Tau used by this {@code LanczosSincFilter2D} instance.
	 * 
	 * @return the Tau used by this {@code LanczosSincFilter2D} instance
	 */
	public double getTau() {
		return this.tau;
	}
	
	/**
	 * Returns a hash code for this {@code LanczosSincFilter2D} instance.
	 * 
	 * @return a hash code for this {@code LanczosSincFilter2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(getResolutionX()), Double.valueOf(getResolutionY()), Double.valueOf(this.tau));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static double doLanczosSinc(final double x, final double tau) {
		final double xAbs = Doubles.abs(x);
		
		if(xAbs < 1.0e-5D) {
			return 1.0D;
		} else if(xAbs > 1.0D) {
			return 0.0D;
		}
		
		final double xAbsPi = xAbs * Doubles.PI;
		final double y = xAbsPi * tau;
		final double sinc = Doubles.sin(y) / y;
		final double lanczos = Doubles.sin(xAbsPi) / xAbsPi;
		
		return sinc * lanczos;
	}
}