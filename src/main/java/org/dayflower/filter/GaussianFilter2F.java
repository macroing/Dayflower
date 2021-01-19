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

import static org.dayflower.utility.Floats.equal;
import static org.dayflower.utility.Floats.exp;
import static org.dayflower.utility.Floats.max;

import java.util.Objects;

/**
 * A {@code GaussianFilter2F} is an implementation of {@link Filter2F} that represents a Gaussian filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GaussianFilter2F extends Filter2F {
	private final float falloff;
	private final float x;
	private final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GaussianFilter2F} given {@code 2.0F}, {@code 2.0F} and {@code 2.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GaussianFilter2F(2.0F, 2.0F, 2.0F);
	 * }
	 * </pre>
	 */
	public GaussianFilter2F() {
		this(2.0F, 2.0F, 2.0F);
	}
	
	/**
	 * Constructs a new {@code GaussianFilter2F} given {@code resolutionX}, {@code resolutionY} and {@code falloff}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param falloff the falloff to use
	 */
	public GaussianFilter2F(final float resolutionX, final float resolutionY, final float falloff) {
		super(resolutionX, resolutionY);
		
		this.falloff = falloff;
		this.x = exp(-falloff * resolutionX * resolutionX);
		this.y = exp(-falloff * resolutionY * resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code GaussianFilter2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code GaussianFilter2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new GaussianFilter2F(%+.10f, %+.10f, %+.10f)", Float.valueOf(getResolutionX()), Float.valueOf(getResolutionY()), Float.valueOf(this.falloff));
	}
	
	/**
	 * Compares {@code object} to this {@code GaussianFilter2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GaussianFilter2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GaussianFilter2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GaussianFilter2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GaussianFilter2F)) {
			return false;
		} else if(!equal(getResolutionX(), GaussianFilter2F.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), GaussianFilter2F.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), GaussianFilter2F.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), GaussianFilter2F.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else if(!equal(this.falloff, GaussianFilter2F.class.cast(object).falloff)) {
			return false;
		} else if(!equal(this.x, GaussianFilter2F.class.cast(object).x)) {
			return false;
		} else if(!equal(this.y, GaussianFilter2F.class.cast(object).y)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code GaussianFilter2F} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public float evaluate(final float x, final float y) {
		return doGaussian(x, this.x, this.falloff) * doGaussian(y, this.y, this.falloff);
	}
	
	/**
	 * Returns the falloff used by this {@code GaussianFilter2F} instance.
	 * 
	 * @return the falloff used by this {@code GaussianFilter2F} instance
	 */
	public float getFalloff() {
		return this.falloff;
	}
	
	/**
	 * Returns a hash code for this {@code GaussianFilter2F} instance.
	 * 
	 * @return a hash code for this {@code GaussianFilter2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(getResolutionX()), Float.valueOf(getResolutionXReciprocal()), Float.valueOf(getResolutionY()), Float.valueOf(getResolutionYReciprocal()), Float.valueOf(this.falloff), Float.valueOf(this.x), Float.valueOf(this.y));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGaussian(final float a, final float b, final float c) {
		return max(0.0F, exp(-c * a * a) - b);
	}
}