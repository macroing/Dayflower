/**
 * Copyright 2014 - 2024 J&#246;rgen Lundgren
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
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@code StringParameter} is a {@link Parameter} implementation for a {@code String} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class StringParameter extends Parameter {
	private final AtomicReference<String> value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code StringParameter} instance with a name of {@code name} and a default value of {@code ""}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new StringParameter(name, "");
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public StringParameter(final String name) {
		this(name, "");
	}
	
	/**
	 * Constructs a new {@code StringParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If either {@code name} or {@code valueDefault} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, either {@code name} or {@code valueDefault} are {@code null}
	 */
	public StringParameter(final String name, final String valueDefault) {
		super(name);
		
		this.value = new AtomicReference<>(Objects.requireNonNull(valueDefault, "valueDefault == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code StringParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code StringParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new StringParameter(\"%s\", \"%s\")", getName(), getValue());
	}
	
	/**
	 * Compares {@code object} to this {@code StringParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code StringParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code StringParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code StringParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof StringParameter)) {
			return false;
		} else if(!Objects.equals(getName(), StringParameter.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getValue(), StringParameter.class.cast(object).getValue())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code StringParameter} instance.
	 * 
	 * @return a hash code for this {@code StringParameter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getValue());
	}
	
	/**
	 * Sets the value to {@code value}.
	 * <p>
	 * If {@code value} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param value the new value
	 * @throws NullPointerException thrown if, and only if, {@code value} is {@code null}
	 */
	public void setValue(final String value) {
		this.value.set(Objects.requireNonNull(value, "value == null"));
	}
}