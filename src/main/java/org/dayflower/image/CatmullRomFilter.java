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
import static org.dayflower.util.Floats.sqrt;

import java.util.Objects;

/**
 * A {@code CatmullRomFilter} is an implementation of {@link Filter} that represents a Catmull-Rom filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CatmullRomFilter extends Filter {
	/**
	 * Constructs a new {@code CatmullRomFilter} instance given {@code resolutionX} and {@code resolutionY}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 */
	public CatmullRomFilter(final float resolutionX, final float resolutionY) {
		super(resolutionX, resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code CatmullRomFilter} instance.
	 * 
	 * @return a {@code String} representation of this {@code CatmullRomFilter} instance
	 */
	@Override
	public String toString() {
		return String.format("new CatmullRomFilter(%+.10f, %+.10f)", Float.valueOf(getResolutionX()), Float.valueOf(getResolutionY()));
	}
	
	/**
	 * Compares {@code object} to this {@code CatmullRomFilter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CatmullRomFilter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CatmullRomFilter)) {
			return false;
		} else if(!equal(getResolutionX(), CatmullRomFilter.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), CatmullRomFilter.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), CatmullRomFilter.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), CatmullRomFilter.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code CatmullRomFilter} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public float evaluate(final float x, final float y) {
		final float a = x * x + y * y;
		final float b = sqrt(a);
		final float c = b < 2.0F ? b < 1.0F ? 3.0F * b * a - 5.0F * a + 2.0F : -b * a + 5.0F * a - 8.0F * b + 4.0F : 0.0F;
		
		return c;
	}
	
	/**
	 * Returns a hash code for this {@code CatmullRomFilter} instance.
	 * 
	 * @return a hash code for this {@code CatmullRomFilter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(getResolutionX()), Float.valueOf(getResolutionXReciprocal()), Float.valueOf(getResolutionY()), Float.valueOf(getResolutionYReciprocal()));
	}
}