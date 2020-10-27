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

import static org.dayflower.util.Floats.equal;

import java.lang.reflect.Field;
import java.util.Objects;

//TODO: Add Javadocs!
public final class ConstantSampler implements Sampler {
	private final float component1;
	private final float component2;
	private final float component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public ConstantSampler() {
		this(0.0F, 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public ConstantSampler(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	@Override
	public Sample1F sample1() {
		return new Sample1F(this.component1);
	}
	
//	TODO: Add Javadocs!
	@Override
	public Sample2F sample2() {
		return new Sample2F(this.component1, this.component2);
	}
	
//	TODO: Add Javadocs!
	@Override
	public Sample3F sample3() {
		return new Sample3F(this.component1, this.component2, this.component3);
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ConstantSampler} instance.
	 * 
	 * @return a {@code String} representation of this {@code ConstantSampler} instance
	 */
	@Override
	public String toString() {
		return String.format("new ConstantSampler(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	/**
	 * Compares {@code object} to this {@code ConstantSampler} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ConstantSampler}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ConstantSampler} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ConstantSampler}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ConstantSampler)) {
			return false;
		} else if(!equal(this.component1, ConstantSampler.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, ConstantSampler.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, ConstantSampler.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ConstantSampler} instance.
	 * 
	 * @return a hash code for this {@code ConstantSampler} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
}