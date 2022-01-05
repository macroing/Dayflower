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
package org.dayflower.renderer;

/**
 * A {@code PathTracer} is an {@link ImageOrderRenderer} for the Path Tracer algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface PathTracer extends ImageOrderRenderer {
	/**
	 * Returns the maximum bounce.
	 * 
	 * @return the maximum bounce
	 */
	int getMaximumBounce();
	
	/**
	 * Returns the minimum bounce before Russian roulette termination occurs.
	 * 
	 * @return the minimum bounce before Russian roulette termination occurs
	 */
	int getMinimumBounceRussianRoulette();
	
	/**
	 * Sets the maximum bounce to {@code maximumBounce}.
	 * 
	 * @param maximumBounce the maximum bounce
	 */
	void setMaximumBounce(final int maximumBounce);
	
	/**
	 * Sets the minimum bounce before Russian roulette termination occurs to {@code minimumBounceRussianRoulette}.
	 * 
	 * @param minimumBounceRussianRoulette the minimum bounce before Russian roulette termination occurs
	 */
	void setMinimumBounceRussianRoulette(final int minimumBounceRussianRoulette);
}