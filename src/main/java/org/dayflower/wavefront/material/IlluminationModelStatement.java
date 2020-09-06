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
package org.dayflower.wavefront.material;

import java.util.Objects;

/**
 * An {@code IlluminationModelStatement} represents an illumination model statement ({@code "illum"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class IlluminationModelStatement implements MaterialFileStatement {
	/**
	 * The name of this {@code IlluminationModelStatement} which is {@code "illum"}.
	 */
	public static final String NAME = "illum";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int number;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code IlluminationModelStatement} instance.
	 * 
	 * @param number the number to use
	 */
	public IlluminationModelStatement(final int number) {
		this.number = number;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code IlluminationModelStatement} instance.
	 * 
	 * @return the name of this {@code IlluminationModelStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code IlluminationModelStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code IlluminationModelStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), Integer.toString(getNumber()));
	}
	
	/**
	 * Compares {@code object} to this {@code IlluminationModelStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code IlluminationModelStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code IlluminationModelStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code IlluminationModelStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof IlluminationModelStatement)) {
			return false;
		} else if(!Objects.equals(getName(), IlluminationModelStatement.class.cast(object).getName())) {
			return false;
		} else if(getNumber() != IlluminationModelStatement.class.cast(object).getNumber()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the number.
	 * 
	 * @return the number
	 */
	public int getNumber() {
		return this.number;
	}
	
	/**
	 * Returns a hash code for this {@code IlluminationModelStatement} instance.
	 * 
	 * @return a hash code for this {@code IlluminationModelStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Integer.valueOf(getNumber()));
	}
}