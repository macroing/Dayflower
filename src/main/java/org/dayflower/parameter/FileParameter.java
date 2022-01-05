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

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@code FileParameter} is a {@link Parameter} implementation for a {@code File} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class FileParameter extends Parameter {
	private final AtomicReference<File> value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code FileParameter} instance with a name of {@code name} and a default value of {@code new File(".")}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new FileParameter(name, new File("."));
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public FileParameter(final String name) {
		this(name, new File("."));
	}
	
	/**
	 * Constructs a new {@code FileParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If either {@code name} or {@code valueDefault} are {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, either {@code name} or {@code valueDefault} are {@code null}
	 */
	public FileParameter(final String name, final File valueDefault) {
		super(name);
		
		this.value = new AtomicReference<>(Objects.requireNonNull(valueDefault, "valueDefault == null"));
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the value.
	 * 
	 * @return the value
	 */
	public File getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a {@code String} representation of this {@code FileParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code FileParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new FileParameter(\"%s\", new File(\"%s\"))", getName(), getValue().getPath());
	}
	
	/**
	 * Compares {@code object} to this {@code FileParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code FileParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code FileParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code FileParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof FileParameter)) {
			return false;
		} else if(!Objects.equals(getName(), FileParameter.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getValue(), FileParameter.class.cast(object).getValue())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code FileParameter} instance.
	 * 
	 * @return a hash code for this {@code FileParameter} instance
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
	public void setValue(final File value) {
		this.value.set(Objects.requireNonNull(value, "value == null"));
	}
}