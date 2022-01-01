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
package org.dayflower.filter;

import static org.dayflower.utility.Floats.equal;

import java.lang.reflect.Field;//TODO: Add Unit Tests!
import java.util.Objects;

/**
 * A {@code BoxFilter2F} is an implementation of {@link Filter2F} that represents a box filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BoxFilter2F extends Filter2F {
	/**
	 * Constructs a new {@code BoxFilter2F} instance given {@code 0.5F} and {@code 0.5F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BoxFilter2F(0.5F, 0.5F);
	 * }
	 * </pre>
	 */
//	TODO: Add Unit Tests!
	public BoxFilter2F() {
		this(0.5F, 0.5F);
	}
	
	/**
	 * Constructs a new {@code BoxFilter2F} instance given {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
//	TODO: Add Unit Tests!
	public BoxFilter2F(final float resolutionX, final float resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BoxFilter2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code BoxFilter2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public String toString() {
		return String.format("new BoxFilter2F(%+.10f, %+.10f)", Float.valueOf(getResolutionX()), Float.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code BoxFilter2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BoxFilter2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BoxFilter2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BoxFilter2F}, and their respective values are equal, {@code false} otherwise
	 */
//	TODO: Add Unit Tests!
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BoxFilter2F)) {
			return false;
		} else if(!equal(getResolutionX(), BoxFilter2F.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), BoxFilter2F.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), BoxFilter2F.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), BoxFilter2F.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code BoxFilter2F} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
//	TODO: Add Unit Tests!
	@Override
	public float evaluate(final float x, final float y) {
		return 1.0F;
	}
	
	/**
	 * Returns a hash code for this {@code BoxFilter2F} instance.
	 * 
	 * @return a hash code for this {@code BoxFilter2F} instance
	 */
//	TODO: Add Unit Tests!
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(getResolutionX()), Float.valueOf(getResolutionXReciprocal()), Float.valueOf(getResolutionY()), Float.valueOf(getResolutionYReciprocal()));
	}
}