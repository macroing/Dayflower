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
public final class Sample3F {
	private final float component1;
	private final float component2;
	private final float component3;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
//	TODO: Add Javadocs!
	public Sample3F() {
		this(0.0F, 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Sample3F(final Sample1F sample) {
		this(sample.getComponent1(), 0.0F, 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Sample3F(final Sample2F sample) {
		this(sample.getComponent1(), sample.getComponent2(), 0.0F);
	}
	
//	TODO: Add Javadocs!
	public Sample3F(final float component1, final float component2, final float component3) {
		this.component1 = component1;
		this.component2 = component2;
		this.component3 = component3;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Sample3F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Sample3F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Sample3F(%+.10f, %+.10f, %+.10f)", Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
	
	/**
	 * Compares {@code object} to this {@code Sample3F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Sample3F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Sample3F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Sample3F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Sample3F)) {
			return false;
		} else if(!equal(this.component1, Sample3F.class.cast(object).component1)) {
			return false;
		} else if(!equal(this.component2, Sample3F.class.cast(object).component2)) {
			return false;
		} else if(!equal(this.component3, Sample3F.class.cast(object).component3)) {
			return false;
		} else {
			return true;
		}
	}
	
//	TODO: Add Javadocs!
	public float getComponent1() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getComponent2() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float getComponent3() {
		return this.component3;
	}
	
//	TODO: Add Javadocs!
	public float getU() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getV() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float getW() {
		return this.component3;
	}
	
//	TODO: Add Javadocs!
	public float getX() {
		return this.component1;
	}
	
//	TODO: Add Javadocs!
	public float getY() {
		return this.component2;
	}
	
//	TODO: Add Javadocs!
	public float getZ() {
		return this.component3;
	}
	
	/**
	 * Returns a hash code for this {@code Sample3F} instance.
	 * 
	 * @return a hash code for this {@code Sample3F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1), Float.valueOf(this.component2), Float.valueOf(this.component3));
	}
}