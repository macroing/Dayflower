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
package org.dayflower.filter;

import static org.dayflower.utility.Doubles.equal;

import java.util.Objects;

/**
 * A {@code BoxFilter2D} is an implementation of {@link Filter2D} that represents a Box filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BoxFilter2D extends Filter2D {
	/**
	 * Constructs a new {@code BoxFilter2D} instance given {@code 0.5D} and {@code 0.5D}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoxFilter2D(0.5D, 0.5D);
	 * }
	 * </pre>
	 */
	public BoxFilter2D() {
		this(0.5D, 0.5D);
	}
	
	/**
	 * Constructs a new {@code BoxFilter2D} instance given {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
	public BoxFilter2D(final double resolutionX, final double resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BoxFilter2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code BoxFilter2D} instance
	 */
	@Override
	public String toString() {
		return String.format("new BoxFilter2D(%+.10f, %+.10f)", Double.valueOf(getResolutionX()), Double.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code BoxFilter2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BoxFilter2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BoxFilter2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BoxFilter2D}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BoxFilter2D)) {
			return false;
		} else if(!equal(getResolutionX(), BoxFilter2D.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), BoxFilter2D.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), BoxFilter2D.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), BoxFilter2D.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code BoxFilter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public double evaluate(final double x, final double y) {
		return 1.0D;
	}
	
	/**
	 * Returns a hash code for this {@code BoxFilter2D} instance.
	 * 
	 * @return a hash code for this {@code BoxFilter2D} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(getResolutionX()), Double.valueOf(getResolutionXReciprocal()), Double.valueOf(getResolutionY()), Double.valueOf(getResolutionYReciprocal()));
	}
}