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
package org.dayflower.wavefront.material;

import java.util.Objects;

import org.macroing.java.lang.Strings;

/**
 * A {@code SpecularExponentStatement} represents a specular exponent statement ({@code "Ns"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SpecularExponentStatement implements MaterialFileStatement {
	/**
	 * The name of this {@code SpecularExponentStatement} which is {@code "Ns"}.
	 */
	public static final String NAME = "Ns";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float exponent;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SpecularExponentStatement} instance.
	 * 
	 * @param exponent the exponent to use
	 */
	public SpecularExponentStatement(final float exponent) {
		this.exponent = exponent;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code SpecularExponentStatement} instance.
	 * 
	 * @return the name of this {@code SpecularExponentStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SpecularExponentStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code SpecularExponentStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), Strings.toNonScientificNotation(getExponent()));
	}
	
	/**
	 * Compares {@code object} to this {@code SpecularExponentStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SpecularExponentStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SpecularExponentStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SpecularExponentStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SpecularExponentStatement)) {
			return false;
		} else if(!Objects.equals(getName(), SpecularExponentStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getExponent(), SpecularExponentStatement.class.cast(object).getExponent()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the exponent.
	 * 
	 * @return the exponent
	 */
	public float getExponent() {
		return this.exponent;
	}
	
	/**
	 * Returns a hash code for this {@code SpecularExponentStatement} instance.
	 * 
	 * @return a hash code for this {@code SpecularExponentStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getExponent()));
	}
}