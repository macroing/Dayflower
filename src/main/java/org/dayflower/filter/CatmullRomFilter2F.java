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

import static org.dayflower.utility.Floats.abs;

import java.util.Objects;

/**
 * A {@code CatmullRomFilter2F} is an implementation of {@link Filter2F} that represents a Catmull-Rom filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class CatmullRomFilter2F extends Filter2F {
	/**
	 * Constructs a new {@code CatmullRomFilter2F} instance.
	 */
	public CatmullRomFilter2F() {
		super(2.0F, 2.0F);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code CatmullRomFilter2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code CatmullRomFilter2F} instance
	 */
	@Override
	public String toString() {
		return "new CatmullRomFilter2F()";
	}
	
	/**
	 * Compares {@code object} to this {@code CatmullRomFilter2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code CatmullRomFilter2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code CatmullRomFilter2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof CatmullRomFilter2F)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code CatmullRomFilter2F} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public float evaluate(final float x, final float y) {
		return doCatmullRom(x) * doCatmullRom(y);
	}
	
	/**
	 * Returns a hash code for this {@code CatmullRomFilter2F} instance.
	 * 
	 * @return a hash code for this {@code CatmullRomFilter2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash();
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doCatmullRom(final float x) {
		final float x1 = abs(x);
		final float x2 = x1 * x1;
		final float x3 = x1 * x2;
		
		if(x1 >= 2.0F) {
			return 0.0F;
		}
		
		if(x1 < 1.0F) {
			return 3.0F * x3 - 5.0F * x2 + 2.0F;
		}
		
		return -x3 + 5.0F * x2 - 8.0F * x1 + 4.0F;
	}
}