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

import static org.dayflower.utility.Floats.abs;
import static org.dayflower.utility.Floats.equal;

import java.util.Objects;

/**
 * A {@code MitchellFilter2F} is an implementation of {@link Filter2F} that represents a Mitchell filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MitchellFilter2F extends Filter2F {
	private final float b;
	private final float c;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MitchellFilter2F} given {@code 2.0F}, {@code 2.0F}, {@code 1.0F / 3.0F} and {@code 1.0F / 3.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MitchellFilter2F(2.0F, 2.0F, 1.0F / 3.0F, 1.0F / 3.0F);
	 * }
	 * </pre>
	 */
	public MitchellFilter2F() {
		this(2.0F, 2.0F, 1.0F / 3.0F, 1.0F / 3.0F);
	}
	
	/**
	 * Constructs a new {@code MitchellFilter2F} given {@code resolutionX}, {@code resolutionY}, {@code b} and {@code c}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param b the B-coefficient to use
	 * @param c the C-coefficient to use
	 */
	public MitchellFilter2F(final float resolutionX, final float resolutionY, final float b, final float c) {
		super(resolutionX, resolutionY);
		
		this.b = b;
		this.c = c;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code MitchellFilter2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code MitchellFilter2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new MitchellFilter2F(%+.10f, %+.10f, %+.10f, %+.10f)", Float.valueOf(getResolutionX()), Float.valueOf(getResolutionY()), Float.valueOf(this.b), Float.valueOf(this.c));
	}
	
	/**
	 * Compares {@code object} to this {@code MitchellFilter2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code MitchellFilter2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code MitchellFilter2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code MitchellFilter2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof MitchellFilter2F)) {
			return false;
		} else if(!equal(getResolutionX(), MitchellFilter2F.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), MitchellFilter2F.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), MitchellFilter2F.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), MitchellFilter2F.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else if(!equal(this.b, MitchellFilter2F.class.cast(object).b)) {
			return false;
		} else if(!equal(this.c, MitchellFilter2F.class.cast(object).c)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code MitchellFilter2F} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public float evaluate(final float x, final float y) {
		return doMitchell(x * getResolutionXReciprocal(), this.b, this.c) * doMitchell(y * getResolutionYReciprocal(), this.b, this.c);
	}
	
	/**
	 * Returns the B-coefficient.
	 * 
	 * @return the B-coefficient
	 */
	public float getB() {
		return this.b;
	}
	
	/**
	 * Returns the C-coefficient.
	 * 
	 * @return the C-coefficient
	 */
	public float getC() {
		return this.c;
	}
	
	/**
	 * Returns a hash code for this {@code MitchellFilter2F} instance.
	 * 
	 * @return a hash code for this {@code MitchellFilter2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(getResolutionX()), Float.valueOf(getResolutionXReciprocal()), Float.valueOf(getResolutionY()), Float.valueOf(getResolutionYReciprocal()), Float.valueOf(this.b), Float.valueOf(this.c));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doMitchell(final float x, final float b, final float c) {
		final float xAbsolute = abs(2.0F * x);
		
		if(xAbsolute > 1.0F) {
			return ((-b - 6.0F * c) * xAbsolute * xAbsolute * xAbsolute + (6.0F * b + 30.0F * c) * xAbsolute * xAbsolute + (-12.0F * b - 48.0F * c) * xAbsolute + (8.0F * b + 24.0F * c)) * (1.0F / 6.0F);
		}
		
		return ((12.0F - 9.0F * b - 6.0F * c) * xAbsolute * xAbsolute * xAbsolute + (-18.0F + 12.0F * b + 6.0F * c) * xAbsolute * xAbsolute + (6.0F - 2.0F * b)) * (1.0F / 6.0F);
	}
}