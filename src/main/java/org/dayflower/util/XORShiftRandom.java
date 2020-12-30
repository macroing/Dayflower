/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.util;

import java.util.Random;

/**
 * An {@code XORShiftRandom} is a {@code Random} implementation that implements the XOR-Shift algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class XORShiftRandom extends Random {
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private long seed;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code XORShiftRandom} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new XORShiftRandom(System.nanoTime());
	 * }
	 * </pre>
	 */
	public XORShiftRandom() {
		this(System.nanoTime());
	}
	
	/**
	 * Constructs a new {@code XORShiftRandom} instance given a seed.
	 * 
	 * @param seed the seed to use
	 */
	public XORShiftRandom(final long seed) {
		this.seed = seed;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the next pseudorandom number.
	 * 
	 * @param bits random bits
	 * @return the next pseudorandom number
	 */
	@Override
	protected int next(final int bits) {
		long seed = this.seed;
		
		seed ^= seed << 21L;
		seed ^= seed >>> 31L;
		seed ^= seed << 4L;
		
		this.seed = seed;
		
		seed &= (1L << bits) - 1L;
		
		return (int)(seed);
	}
	
	/**
	 * Sets the seed for this {@code XORShiftRandom} instance.
	 * 
	 * @param seed the new seed
	 */
	@Override
	public synchronized void setSeed(final long seed) {
		this.seed = seed;
	}
}