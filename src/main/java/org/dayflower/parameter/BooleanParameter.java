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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A {@code BooleanParameter} is a {@link Parameter} implementation for a {@code boolean} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class BooleanParameter extends Parameter {
	private final AtomicBoolean value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code BooleanParameter} instance with a name of {@code name} and a default value of {@code false}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new BooleanParameter(name, false);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public BooleanParameter(final String name) {
		this(name, false);
	}
	
	/**
	 * Constructs a new {@code BooleanParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public BooleanParameter(final String name, final boolean valueDefault) {
		super(name);
		
		this.value = new AtomicBoolean(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code BooleanParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code BooleanParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new BooleanParameter(\"%s\", %s)", getName(), Boolean.toString(getValue()));
	}
	
	/**
	 * Compares {@code object} to this {@code BooleanParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code BooleanParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code BooleanParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code BooleanParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof BooleanParameter)) {
			return false;
		} else if(!Objects.equals(getName(), BooleanParameter.class.cast(object).getName())) {
			return false;
		} else if(getValue() != BooleanParameter.class.cast(object).getValue()) {
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
	public boolean getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a hash code for this {@code BooleanParameter} instance.
	 * 
	 * @return a hash code for this {@code BooleanParameter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Boolean.valueOf(getValue()));
	}
	
	/**
	 * Sets the value to {@code value}.
	 * 
	 * @param value the new value
	 */
	public void setValue(final boolean value) {
		this.value.set(value);
	}
}