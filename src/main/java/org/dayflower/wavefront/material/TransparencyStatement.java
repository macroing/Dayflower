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
package org.dayflower.wavefront.material;

import java.util.Objects;

import org.macroing.java.lang.Strings;

/**
 * A {@code TransparencyStatement} represents a non-official transparency statement ({@code "Tr"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class TransparencyStatement implements MaterialFileStatement {
	/**
	 * The name of this {@code TransparencyStatement} which is {@code "Tr"}.
	 */
	public static final String NAME = "Tr";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code TransparencyStatement} instance.
	 * 
	 * @param factor the factor to use
	 */
	public TransparencyStatement(final float factor) {
		this.factor = factor;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code TransparencyStatement} instance.
	 * 
	 * @return the name of this {@code TransparencyStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code TransparencyStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code TransparencyStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), Strings.toNonScientificNotation(getFactor()));
	}
	
	/**
	 * Compares {@code object} to this {@code TransparencyStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code TransparencyStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code TransparencyStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code TransparencyStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof TransparencyStatement)) {
			return false;
		} else if(!Objects.equals(getName(), TransparencyStatement.class.cast(object).getName())) {
			return false;
		} else if(Float.compare(getFactor(), TransparencyStatement.class.cast(object).getFactor()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the factor.
	 * 
	 * @return the factor
	 */
	public float getFactor() {
		return this.factor;
	}
	
	/**
	 * Returns a hash code for this {@code TransparencyStatement} instance.
	 * 
	 * @return a hash code for this {@code TransparencyStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Float.valueOf(getFactor()));
	}
}