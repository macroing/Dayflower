/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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

import java.util.Objects;

import org.macroing.java.lang.Floats;

/**
 * A {@code Sample1F} denotes a 1-dimensional sample with one component, of type {@code float}.
 * <p>
 * This class is immutable and therefore thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class Sample1F {
	private final float component1;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code Sample1F} instance.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sample1F(0.0F);
	 * }
	 * </pre>
	 */
	public Sample1F() {
		this(0.0F);
	}
	
	/**
	 * Constructs a new {@code Sample1F} instance from {@code sample}.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sample1F(sample.getComponent1());
	 * }
	 * </pre>
	 * 
	 * @param sample a {@link Sample2F} instance
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	public Sample1F(final Sample2F sample) {
		this(sample.getComponent1());
	}
	
	/**
	 * Constructs a new {@code Sample1F} instance from {@code sample}.
	 * <p>
	 * If {@code sample} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new Sample1F(sample.getComponent1());
	 * }
	 * </pre>
	 * 
	 * @param sample a {@link Sample3F} instance
	 * @throws NullPointerException thrown if, and only if, {@code sample} is {@code null}
	 */
	public Sample1F(final Sample3F sample) {
		this(sample.getComponent1());
	}
	
	/**
	 * Constructs a new {@code Sample1F} instance.
	 * 
	 * @param component1 the value of component 1
	 */
	public Sample1F(final float component1) {
		this.component1 = component1;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code Sample1F} instance.
	 * 
	 * @return a {@code String} representation of this {@code Sample1F} instance
	 */
	@Override
	public String toString() {
		return String.format("new Sample1F(%+.10f)", Float.valueOf(this.component1));
	}
	
	/**
	 * Compares {@code object} to this {@code Sample1F} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code Sample1F}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code Sample1F} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code Sample1F}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof Sample1F)) {
			return false;
		} else if(!Floats.equals(this.component1, Sample1F.class.cast(object).component1)) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value of component 1.
	 * 
	 * @return the value of component 1
	 */
	public float getComponent1() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the U-component.
	 * 
	 * @return the value of the U-component
	 */
	public float getU() {
		return this.component1;
	}
	
	/**
	 * Returns the value of the X-component.
	 * 
	 * @return the value of the X-component
	 */
	public float getX() {
		return this.component1;
	}
	
	/**
	 * Returns a hash code for this {@code Sample1F} instance.
	 * 
	 * @return a hash code for this {@code Sample1F} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(Float.valueOf(this.component1));
	}
}