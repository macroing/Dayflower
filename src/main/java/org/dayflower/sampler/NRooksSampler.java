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
package org.dayflower.sampler;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.dayflower.utility.ParameterArguments;

/**
 * An {@code NRooksSampler} is a {@link Sampler} implementation that produces samples based on the N-Rooks algorithm.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class NRooksSampler implements Sampler {
	private final AtomicInteger index;
	private final Random random;
	private final Sample2F[] samples;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code NRooksSampler} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NRooksSampler(new Random());
	 * }
	 * </pre>
	 */
	public NRooksSampler() {
		this(new Random());
	}
	
	/**
	 * Constructs a new {@code NRooksSampler} instance.
	 * <p>
	 * If {@code random} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new NRooksSampler(random, 200);
	 * }
	 * </pre>
	 * 
	 * @param random a {@code Random} instance
	 * @throws NullPointerException thrown if, and only if, {@code random} is {@code null}
	 */
	public NRooksSampler(final Random random) {
		this(random, 200);
	}
	
	/**
	 * Constructs a new {@code NRooksSampler} instance.
	 * <p>
	 * If {@code random} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param random a {@code Random} instance
	 * @param size the size to use
	 * @throws NullPointerException thrown if, and only if, {@code random} is {@code null}
	 */
	public NRooksSampler(final Random random, final int size) {
		this.index = new AtomicInteger();
		this.random = Objects.requireNonNull(random, "random == null");
		this.samples = new Sample2F[ParameterArguments.requireRange(size, 0, Integer.MAX_VALUE, "size")];
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@link Sample1F} with a 1-dimensional sample.
	 * 
	 * @return a {@code Sample1F} with a 1-dimensional sample
	 */
	@Override
	public Sample1F sample1() {
		return new Sample1F(sample2());
	}
	
	/**
	 * Returns a {@link Sample2F} with a 2-dimensional sample.
	 * 
	 * @return a {@code Sample2F} with a 2-dimensional sample
	 */
	@Override
	public Sample2F sample2() {
		doUpdateIfNecessary();
		
		return this.samples[doGetAndUpdateIndex()];
	}
	
	/**
	 * Returns a {@link Sample3F} with a 3-dimensional sample.
	 * 
	 * @return a {@code Sample3F} with a 3-dimensional sample
	 */
	@Override
	public Sample3F sample3() {
		return new Sample3F(sample2());
	}
	
	/**
	 * Returns a {@code String} representation of this {@code NRooksSampler} instance.
	 * 
	 * @return a {@code String} representation of this {@code NRooksSampler} instance
	 */
	@Override
	public String toString() {
		return String.format("new NRooksSampler(%s, %d)", this.random, Integer.valueOf(this.samples.length));
	}
	
	/**
	 * Compares {@code object} to this {@code NRooksSampler} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code NRooksSampler}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code NRooksSampler} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code NRooksSampler}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof NRooksSampler)) {
			return false;
		} else if(!Objects.equals(this.index, NRooksSampler.class.cast(object).index)) {
			return false;
		} else if(!Objects.equals(this.random, NRooksSampler.class.cast(object).random)) {
			return false;
		} else if(!Arrays.equals(this.samples, NRooksSampler.class.cast(object).samples)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code NRooksSampler} instance.
	 * 
	 * @return a hash code for this {@code NRooksSampler} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.index, this.random, Integer.valueOf(Arrays.hashCode(this.samples)));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private int doGetAndUpdateIndex() {
		return this.index.getAndUpdate(index -> index + 1 < this.samples.length ? index + 1 : 0);
	}
	
	private void doShuffle() {
		doShuffleComponent1();
		doShuffleComponent2();
	}
	
	private void doShuffleComponent1() {
		for(int i = 0; i < this.samples.length - 1; i++) {
			final int index0 = this.random.nextInt(this.samples.length);
			final int index1 = i + 1;
			
			final Sample2F sample0 = this.samples[index0];
			final Sample2F sample1 = this.samples[index1];
			
			this.samples[index0] = new Sample2F(sample1.getComponent1(), sample0.getComponent2());
			this.samples[index1] = new Sample2F(sample0.getComponent1(), sample1.getComponent2());
		}
	}
	
	private void doShuffleComponent2() {
		for(int i = 0; i < this.samples.length - 1; i++) {
			final int index0 = this.random.nextInt(this.samples.length);
			final int index1 = i + 1;
			
			final Sample2F sample0 = this.samples[index0];
			final Sample2F sample1 = this.samples[index1];
			
			this.samples[index0] = new Sample2F(sample0.getComponent1(), sample1.getComponent2());
			this.samples[index1] = new Sample2F(sample1.getComponent1(), sample0.getComponent2());
		}
	}
	
	private void doUpdate() {
		for(int index = 0; index < this.samples.length; index++) {
			doUpdate(index);
		}
		
		doShuffle();
	}
	
	private void doUpdate(final int index) {
		final float component1 = (index + this.random.nextFloat()) / this.samples.length;
		final float component2 = (index + this.random.nextFloat()) / this.samples.length;
		
		this.samples[index] = new Sample2F(component1, component2);
	}
	
	private void doUpdateIfNecessary() {
		if(this.index.get() == 0) {
			doUpdate();
		}
	}
}