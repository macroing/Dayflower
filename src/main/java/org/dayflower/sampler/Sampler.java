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
package org.dayflower.sampler;

/**
 * A {@code Sampler} is used for sampling in different dimensions.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public interface Sampler {
	/**
	 * Returns a {@link Sample1F} with a 1-dimensional sample.
	 * 
	 * @return a {@code Sample1F} with a 1-dimensional sample
	 */
	Sample1F sample1();
	
	/**
	 * Returns a {@link Sample2F} with a 2-dimensional sample.
	 * 
	 * @return a {@code Sample2F} with a 2-dimensional sample
	 */
	Sample2F sample2();
	
	/**
	 * Returns a {@link Sample3F} with a 3-dimensional sample.
	 * 
	 * @return a {@code Sample3F} with a 3-dimensional sample
	 */
	Sample3F sample3();
}