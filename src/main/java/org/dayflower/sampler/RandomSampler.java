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
package org.dayflower.sampler;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Random;

//TODO: Add Javadocs!
public final class RandomSampler implements Sampler {
	private final Random random;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public RandomSampler() {
		this(new Random());
	}
	
//	TODO: Add Javadocs!
	public RandomSampler(final Random random) {
		this.random = Objects.requireNonNull(random, "random == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Sample1F sample1() {
		return new Sample1F(this.random.nextFloat());
	}
	
//	TODO: Add Javadocs!
	@Override
	public Sample2F sample2() {
		return new Sample2F(this.random.nextFloat(), this.random.nextFloat());
	}
	
//	TODO: Add Javadocs!
	@Override
	public Sample3F sample3() {
		return new Sample3F(this.random.nextFloat(), this.random.nextFloat(), this.random.nextFloat());
	}
	
	/**
	 * Returns a {@code String} representation of this {@code RandomSampler} instance.
	 * 
	 * @return a {@code String} representation of this {@code RandomSampler} instance
	 */
	@Override
	public String toString() {
		return String.format("new RandomSampler(%s)", this.random);
	}
	
	/**
	 * Compares {@code object} to this {@code RandomSampler} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code RandomSampler}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code RandomSampler} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code RandomSampler}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof RandomSampler)) {
			return false;
		} else if(!Objects.equals(this.random, RandomSampler.class.cast(object).random)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code RandomSampler} instance.
	 * 
	 * @return a hash code for this {@code RandomSampler} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.random);
	}
}