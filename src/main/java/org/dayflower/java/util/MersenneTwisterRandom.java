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
package org.dayflower.java.util;

import java.util.Random;

/**
 * A {@code MersenneTwisterRandom} is a {@code Random} implementation that implements the Mersenne Twister algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class MersenneTwisterRandom extends Random {
	private static final int LOWER_MASK = 0x7FFFFFFF;
	private static final int M = 397;
	private static final int MATRIX_A = 0x9908B0DF;
	private static final int N = 624;
	private static final int TEMPERING_MASK_B = 0x9D2C5680;
	private static final int TEMPERING_MASK_C = 0xEFC60000;
	private static final int UPPER_MASK = 0x80000000;
	private static final long serialVersionUID = 1L;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int mti;
	private int[] mag01;
	private int[] mt;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code MersenneTwisterRandom} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new MersenneTwisterRandom(System.nanoTime());
	 * }
	 * </pre>
	 */
	public MersenneTwisterRandom() {
		this(System.nanoTime());
	}
	
	/**
	 * Constructs a new {@code MersenneTwisterRandom} instance given a seed.
	 * 
	 * @param seed the seed to use
	 */
	public MersenneTwisterRandom(final long seed) {
		super(seed);
		
		setSeed(seed);
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
		int y;
		
		if(this.mti >= N) {
			int kk;
			
			final int[] mt = this.mt;
			final int[] mag01 = this.mag01;
			
			for(kk = 0; kk < N - M; kk++) {
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				
				mt[kk] = mt[kk + M] ^ (y >>> 1) ^ mag01[y & 0x1];
			}
			
			for(; kk < N - 1; kk++) {
				y = (mt[kk] & UPPER_MASK) | (mt[kk + 1] & LOWER_MASK);
				
				mt[kk] = mt[kk + (M - N)] ^ (y >>> 1) ^ mag01[y & 0x1];
			}
			
			y = (mt[N - 1] & UPPER_MASK) | (mt[0] & LOWER_MASK);
			
			mt[N - 1] = mt[M - 1] ^ (y >>> 1) ^ mag01[y & 0x1];
			
			this.mti = 0;
		}
		
		y = this.mt[this.mti++];
		y ^= y >>> 11;
		y ^= (y << 7) & TEMPERING_MASK_B;
		y ^= (y << 15) & TEMPERING_MASK_C;
		y ^= (y >>> 18);
		
		return y >>> (32 - bits);
    }
	
	/**
	 * Sets the seed for this {@code MersenneTwisterRandom} instance.
	 * 
	 * @param seed the new seed
	 */
	@Override
	public synchronized void setSeed(final long seed) {
		super.setSeed(seed);
		
		this.mt = new int[N];
		
		this.mag01 = new int[2];
		this.mag01[0] = 0x0;
		this.mag01[1] = MATRIX_A;
		
		this.mt[0] = (int)(seed & 0xFFFFFFFF);
		
		for(this.mti = 1; this.mti < N; this.mti++) {
			this.mt[this.mti] = (1812433253 * (this.mt[this.mti - 1] ^ (this.mt[this.mti - 1] >>> 30)) + this.mti);
			this.mt[this.mti] &= 0xFFFFFFFF;
		}
	}
}