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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * An {@code IntParameter} is a {@link Parameter} implementation for an {@code int} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IntParameter extends Parameter {
	private final AtomicInteger value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IntParameter} instance with a name of {@code name} and a default value of {@code 0}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new IntParameter(name, 0);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public IntParameter(final String name) {
		this(name, 0);
	}
	
	/**
	 * Constructs a new {@code IntParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public IntParameter(final String name, final int valueDefault) {
		super(name);
		
		this.value = new AtomicInteger(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code IntParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code IntParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new IntParameter(\"%s\", %d)", getName(), Integer.valueOf(getValue()));
	}
	
	/**
	 * Compares {@code object} to this {@code IntParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code IntParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code IntParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code IntParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof IntParameter)) {
			return false;
		} else if(!Objects.equals(getName(), IntParameter.class.cast(object).getName())) {
			return false;
		} else if(getValue() != IntParameter.class.cast(object).getValue()) {
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
	public int getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a hash code for this {@code IntParameter} instance.
	 * 
	 * @return a hash code for this {@code IntParameter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Integer.valueOf(getValue()));
	}
	
	/**
	 * Sets the value to {@code value}.
	 * 
	 * @param value the new value
	 */
	public void setValue(final int value) {
		this.value.set(value);
	}
}