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
package org.dayflower.parameter;

import static org.dayflower.utility.Floats.equal;

import java.util.Objects;

import org.dayflower.java.util.concurrent.atomic.AtomicFloat;

/**
 * A {@code FloatParameter} is a {@link Parameter} implementation for a {@code float} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FloatParameter extends Parameter {
	private final AtomicFloat value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FloatParameter} instance with a name of {@code name} and a default value of {@code 0.0F}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FloatParameter(name, 0.0F);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public FloatParameter(final String name) {
		this(name, 0.0F);
	}
	
	/**
	 * Constructs a new {@code FloatParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public FloatParameter(final String name, final float valueDefault) {
		super(name);
		
		this.value = new AtomicFloat(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code FloatParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code FloatParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new FloatParameter(\"%s\", %+.10f)", getName(), Float.valueOf(getValue()));
	}
	
	/**
	 * Compares {@code object} to this {@code FloatParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FloatParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FloatParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FloatParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FloatParameter)) {
			return false;
		} else if(!Objects.equals(getName(), FloatParameter.class.cast(object).getName())) {
			return false;
		} else if(!equal(getValue(), FloatParameter.class.cast(object).getValue())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public float getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a hash code for this {@code FloatParameter} instance.
	 * 
	 * @return a hash code for this {@code FloatParameter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getValue()));
	}
	
	/**
	 * Sets the value to {@code value}.
	 * 
	 * @param value the new value
	 */
	public void setValue(final float value) {
		this.value.set(value);
	}
}