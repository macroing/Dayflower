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
import static org.dayflower.utility.Doubles.sqrt;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

/**
 * A {@code CatmullRomFilter2D} is an implementation of {@link Filter2D} that represents a Catmull-Rom filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CatmullRomFilter2D extends Filter2D {
	/**
	 * Constructs a new {@code CatmullRomFilter2D} instance given {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
//	TODO: Add Unit Tests!
	public CatmullRomFilter2D(final double resolutionX, final double resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code CatmullRomFilter2D} instance.
	 * 
	 * @return a {@code String} representation of this {@code CatmullRomFilter2D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new CatmullRomFilter2D(%+.10f, %+.10f)", Double.valueOf(getResolutionX()), Double.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code CatmullRomFilter2D} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter2D}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CatmullRomFilter2D} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter2D}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CatmullRomFilter2D)) {
			return false;
		} else if(!equal(getResolutionX(), CatmullRomFilter2D.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), CatmullRomFilter2D.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), CatmullRomFilter2D.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), CatmullRomFilter2D.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code CatmullRomFilter2D} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
//	TODO: Add Unit Tests!
	@Override
	public double evaluate(final double x, final double y) {
		final double a = x * x + y * y;
		final double b = sqrt(a);
		final double c = b < 2.0D ? b < 1.0D ? 3.0D * b * a - 5.0D * a + 2.0D : -b * a + 5.0D * a - 8.0D * b + 4.0D : 0.0D;
		
		return c;
	}
	
	/**
	 * Returns a hash code for this {@code CatmullRomFilter2D} instance.
	 * 
	 * @return a hash code for this {@code CatmullRomFilter2D} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Double.valueOf(getResolutionX()), Double.valueOf(getResolutionXReciprocal()), Double.valueOf(getResolutionY()), Double.valueOf(getResolutionYReciprocal()));
	}
}