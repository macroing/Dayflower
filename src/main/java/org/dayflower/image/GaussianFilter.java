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

import static org.dayflower.util.Floats.exp;
import static org.dayflower.util.Floats.max;

/**
 * A {@code GaussianFilter} is an implementation of {@link Filter} that represents a Gaussian filter.
 * <p>
 * This class is immutable and therefore also suitable for concurrent use without external synchronization.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GaussianFilter extends Filter {
	private final float falloff;
	private final float x;
	private final float y;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code GaussianFilter} given {@code 2.0F}, {@code 2.0F} and {@code 2.0F}.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new GaussianFilter(2.0F, 2.0F, 2.0F);
	 * }
	 * </pre>
	 */
	public GaussianFilter() {
		this(2.0F, 2.0F, 2.0F);
	}
	
	/**
	 * Constructs a new {@code GaussianFilter} given {@code resolutionX}, {@code resolutionY} and {@code falloff}.
	 * 
	 * @param resolutionX the resolution of the X-axis
	 * @param resolutionY the resolution of the Y-axis
	 * @param falloff the falloff to use
	 */
	public GaussianFilter(final float resolutionX, final float resolutionY, final float falloff) {
		super(resolutionX, resolutionY);
		
		this.falloff = falloff;
		this.x = exp(-falloff * resolutionX * resolutionX);
		this.y = exp(-falloff * resolutionY * resolutionY);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Evaluates this {@code GaussianFilter} instance given {@code x} and {@code y}.
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
	 * Returns the falloff used by this {@code GaussianFilter} instance.
	 * 
	 * @return the falloff used by this {@code GaussianFilter} instance
	 */
	public float getFalloff() {
		return this.falloff;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private static float doGaussian(final float a, final float b, final float c) {
		return max(0.0F, exp(-c * a * a) - b);
	}
}