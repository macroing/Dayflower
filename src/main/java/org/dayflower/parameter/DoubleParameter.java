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

import org.macroing.java.lang.Doubles;
import org.macroing.java.util.concurrent.atomic.AtomicDouble;

/**
 * A {@code DoubleParameter} is a {@link Parameter} implementation for a {@code double} value.
 * <p>
 * This class is mutable and not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DoubleParameter extends Parameter {
	private final AtomicDouble value;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DoubleParameter} instance with a name of {@code name} and a default value of {@code 0.0D}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * <p>
	 * Calling this constructor is equivalent to the following:
	 * <pre>
	 * {@code
	 * new DoubleParameter(name, 0.0D);
	 * }
	 * </pre>
	 * 
	 * @param name the name
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public DoubleParameter(final String name) {
		this(name, 0.0D);
	}
	
	/**
	 * Constructs a new {@code DoubleParameter} instance with a name of {@code name} and a default value of {@code valueDefault}.
	 * <p>
	 * If {@code name} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param name the name
	 * @param valueDefault the default value
	 * @throws NullPointerException thrown if, and only if, {@code name} is {@code null}
	 */
	public DoubleParameter(final String name, final double valueDefault) {
		super(name);
		
		this.value = new AtomicDouble(valueDefault);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code String} representation of this {@code DoubleParameter} instance.
	 * 
	 * @return a {@code String} representation of this {@code DoubleParameter} instance
	 */
	@Override
	public String toString() {
		return String.format("new DoubleParameter(\"%s\", %+.10f)", getName(), Double.valueOf(getValue()));
	}
	
	/**
	 * Compares {@code object} to this {@code DoubleParameter} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DoubleParameter}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DoubleParameter} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DoubleParameter}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DoubleParameter)) {
			return false;
		} else if(!Objects.equals(getName(), DoubleParameter.class.cast(object).getName())) {
			return false;
		} else if(!Doubles.equals(getValue(), DoubleParameter.class.cast(object).getValue())) {
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
	public double getValue() {
		return this.value.get();
	}
	
	/**
	 * Returns a hash code for this {@code DoubleParameter} instance.
	 * 
	 * @return a hash code for this {@code DoubleParameter} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Double.valueOf(getValue()));
	}
	
	/**
	 * Sets the value to {@code value}.
	 * 
	 * @param value the new value
	 */
	public void setValue(final double value) {
		this.value.set(value);
	}
}