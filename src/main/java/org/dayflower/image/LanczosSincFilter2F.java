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
package org.dayflower.image;

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.equal;
import static org.dayflower.util.Floats.sin;

import java.util.Objects;

/**
 * A {@code LanczosSincFilter2F} is an implementation of {@link Filter2F} that represents a Lanczos-Sinc filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LanczosSincFilter2F extends Filter2F {
	private final float tau;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LanczosSincFilter2F} given {@code 4.0F}, {@code 4.0F} and {@code 3.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LanczosSincFilter2F(4.0F, 4.0F, 3.0F);
	 * }
	 * </pre>
	 */
	public LanczosSincFilter2F() {
		this(4.0F, 4.0F, 3.0F);
	}
	
	/**
	 * Constructs a new {@code LanczosSincFilter2F} given {@code resolutionX}, {@code resolutionY} and {@code tau}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param tau the Tau to use
	 */
	public LanczosSincFilter2F(final float resolutionX, final float resolutionY, final float tau) {
		super(resolutionX, resolutionY);
		
		this.tau = tau;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code LanczosSincFilter2F} instance.
	 * 
	 * @return a {@code String} representation of this {@code LanczosSincFilter2F} instance
	 */
	@Override
	public String toString() {
		return String.format("new LanczosSincFilter2F(%+.10f, %+.10f, %+.10f)", Float.valueOf(getResolutionX()), Float.valueOf(getResolutionY()), Float.valueOf(this.tau));
	}
	
	/**
	 * Compares {@code object} to this {@code LanczosSincFilter2F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code LanczosSincFilter2F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code LanczosSincFilter2F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code LanczosSincFilter2F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof LanczosSincFilter2F)) {
			return false;
		} else if(!equal(getResolutionX(), LanczosSincFilter2F.class.cast(object).getResolutionX())) {
			return false;
		} else if(!equal(getResolutionXReciprocal(), LanczosSincFilter2F.class.cast(object).getResolutionXReciprocal())) {
			return false;
		} else if(!equal(getResolutionY(), LanczosSincFilter2F.class.cast(object).getResolutionY())) {
			return false;
		} else if(!equal(getResolutionYReciprocal(), LanczosSincFilter2F.class.cast(object).getResolutionYReciprocal())) {
			return false;
		} else if(!equal(this.tau, LanczosSincFilter2F.class.cast(object).tau)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Evaluates this {@code LanczosSincFilter2F} instance given {@code x} and {@code y}.
	 * <p>
	 * Returns the evaluated value.
	 * 
	 * @param x the X-coordinate
	 * @param y the Y-coordinate
	 * @return the evaluated value
	 */
	@Override
	public float evaluate(final float x, final float y) {
		return doLanczosSinc(x * getResolutionXReciprocal(), this.tau) * doLanczosSinc(y * getResolutionYReciprocal(), this.tau);
	}
	
	/**
	 * Returns the Tau used by this {@code LanczosSincFilter2F} instance.
	 * 
	 * @return the Tau used by this {@code LanczosSincFilter2F} instance
	 */
	public float getTau() {
		return this.tau;
	}
	
	/**
	 * Returns a hash code for this {@code LanczosSincFilter2F} instance.
	 * 
	 * @return a hash code for this {@code LanczosSincFilter2F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(getResolutionX()), Float.valueOf(getResolutionXReciprocal()), Float.valueOf(getResolutionY()), Float.valueOf(getResolutionYReciprocal()), Float.valueOf(this.tau));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doLanczosSinc(final float x, final float tau) {
		final float xAbs = abs(x);
		
		if(xAbs < 1.0e-5F) {
			return 1.0F;
		} else if(xAbs > 1.0F) {
			return 0.0F;
		}
		
		final float xAbsPi = xAbs * PI;
		final float y = xAbsPi * tau;
		final float sinc = sin(y) / y;
		final float lanczos = sin(xAbsPi) / xAbsPi;
		
		return sinc * lanczos;
	}
}