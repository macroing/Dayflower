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

import static org.dayflower.util.Floats.PI;
import static org.dayflower.util.Floats.abs;
import static org.dayflower.util.Floats.sin;

/**
 * A {@code LanczosSincFilter} is an implementation of {@link Filter} that represents a Lanczos-Sinc filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class LanczosSincFilter extends Filter {
	private final float tau;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code LanczosSincFilter} given {@code 4.0F}, {@code 4.0F} and {@code 3.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new LanczosSincFilter(4.0F, 4.0F, 3.0F);
	 * }
	 * </pre>
	 */
	public LanczosSincFilter() {
		this(4.0F, 4.0F, 3.0F);
	}
	
	/**
	 * Constructs a new {@code LanczosSincFilter} given {@code resolutionX}, {@code resolutionY} and {@code tau}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param tau the Tau to use
	 */
	public LanczosSincFilter(final float resolutionX, final float resolutionY, final float tau) {
		super(resolutionX, resolutionY);
		
		this.tau = tau;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates this {@code LanczosSincFilter} instance given {@code x} and {@code y}.
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
	 * Returns the Tau used by this {@code LanczosSincFilter} instance.
	 * 
	 * @return the Tau used by this {@code LanczosSincFilter} instance
	 */
	public float getTau() {
		return this.tau;
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