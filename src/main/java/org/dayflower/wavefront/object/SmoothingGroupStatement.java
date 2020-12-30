/**
 * Copyright 2020 - 2021 J&#246;rgen Lundgren
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
package org.dayflower.wavefront.object;

import java.util.Objects;

/**
 * A {@code SmoothingGroupStatement} represents a smoothing group statement ({@code "s"}) of a Wavefront Object file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class SmoothingGroupStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code SmoothingGroupStatement} which is {@code "s"}.
	 */
	public static final String NAME = "s";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final int groupNumber;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code SmoothingGroupStatement} instance.
	 * 
	 * @param groupNumber the group number to use
	 */
	public SmoothingGroupStatement(final int groupNumber) {
		this.groupNumber = groupNumber;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code SmoothingGroupStatement} instance.
	 * 
	 * @return the name of this {@code SmoothingGroupStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code SmoothingGroupStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code SmoothingGroupStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), Integer.toString(getGroupNumber()));
	}
	
	/**
	 * Compares {@code object} to this {@code SmoothingGroupStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code SmoothingGroupStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code SmoothingGroupStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code SmoothingGroupStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof SmoothingGroupStatement)) {
			return false;
		} else if(!Objects.equals(getName(), SmoothingGroupStatement.class.cast(object).getName())) {
			return false;
		} else if(getGroupNumber() != SmoothingGroupStatement.class.cast(object).getGroupNumber()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code SmoothingGroupStatement} is off, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code SmoothingGroupStatement} is off, {@code false} otherwise
	 */
	public boolean isOff() {
		return getGroupNumber() == 0;
	}
	
	/**
	 * Returns {@code true} if, and only if, this {@code SmoothingGroupStatement} is on, {@code false} otherwise.
	 * 
	 * @return {@code true} if, and only if, this {@code SmoothingGroupStatement} is on, {@code false} otherwise
	 */
	public boolean isOn() {
		return getGroupNumber() != 0;
	}
	
	/**
	 * Returns the group number.
	 * 
	 * @return the group number
	 */
	public int getGroupNumber() {
		return this.groupNumber;
	}
	
	/**
	 * Returns a hash code for this {@code SmoothingGroupStatement} instance.
	 * 
	 * @return a hash code for this {@code SmoothingGroupStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), Integer.valueOf(getGroupNumber()));
	}
}