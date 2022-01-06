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

import org.dayflower.java.lang.Strings;

/**
 * A {@code DissolveStatement} represents a dissolve statement ({@code "d"}) of a Wavefront Material file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class DissolveStatement implements MaterialFileStatement {
	/**
	 * The name of this {@code DissolveStatement} which is {@code "d"}.
	 */
	public static final String NAME = "d";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final boolean hasHalo;
	private final float factor;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code DissolveStatement} instance.
	 * <p>
	 * Calling this constructor is equivalent to {@code new DissolveStatement(factor, false)}.
	 * 
	 * @param factor the factor to use
	 */
	public DissolveStatement(final float factor) {
		this(factor, false);
	}
	
	/**
	 * Constructs a new {@code DissolveStatement} instance.
	 * 
	 * @param factor the factor to use
	 * @param hasHalo {@code true} if, and only if, a halo should be used, {@code false} otherwise
	 */
	public DissolveStatement(final float factor, final boolean hasHalo) {
		this.factor = factor;
		this.hasHalo = hasHalo;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code DissolveStatement} instance.
	 * 
	 * @return the name of this {@code DissolveStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code DissolveStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code DissolveStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s%s %s", getName(), hasHalo() ? " -halo" : "", Strings.toNonScientificNotation(getFactor()));
	}
	
	/**
	 * Compares {@code object} to this {@code DissolveStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code DissolveStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code DissolveStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code DissolveStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof DissolveStatement)) {
			return false;
		} else if(!Objects.equals(getName(), DissolveStatement.class.cast(object).getName())) {
			return false;
		} else if(hasHalo() != DissolveStatement.class.cast(object).hasHalo()) {
			return false;
		} else if(Float.compare(getFactor(), DissolveStatement.class.cast(object).getFactor()) != 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code DissolveStatement} has a halo, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code DissolveStatement} has a halo, {@code false} otherwise
	 */
	public boolean hasHalo() {
		return this.hasHalo;
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
	 * Returns a hash code for this {@code DissolveStatement} instance.
	 * 
	 * @return a hash code for this {@code DissolveStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Boolean.valueOf(hasHalo()), Float.valueOf(getFactor()));
	}
}