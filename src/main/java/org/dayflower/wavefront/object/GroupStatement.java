/**
 * Copyright 2014 - 2021 J&#246;rgen Lundgren
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@code GroupStatement} represents a group statement ({@code "g"}) of a Wavefront Object file.
 * <p>
 * This class is not thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class GroupStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code GroupStatement} which is {@code "g"}.
	 */
	public static final String NAME = "g";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final List<String> groupNames = new ArrayList<>();
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new empty {@code GroupStatement} instance.
	 */
	public GroupStatement() {
		
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns a {@code List} of {@code String}s with all group names currently added to this {@code GroupStatement} instance.
	 * <p>
	 * Modifying the returned {@code List} will not affect this {@code GroupStatement} instance.
	 * 
	 * @return a {@code List} of {@code String}s with all group names currently added to this {@code GroupStatement} instance
	 */
	public List<String> getGroupNames() {
		return new ArrayList<>(this.groupNames);
	}
	
	/**
	 * Returns the name of this {@code GroupStatement} instance.
	 * 
	 * @return the name of this {@code GroupStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code GroupStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code GroupStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), this.groupNames.stream().collect(Collectors.joining(" ")));
	}
	
	/**
	 * Compares {@code object} to this {@code GroupStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code GroupStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code GroupStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code GroupStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof GroupStatement)) {
			return false;
		} else if(!Objects.equals(getName(), GroupStatement.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getGroupNames(), GroupStatement.class.cast(object).getGroupNames())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code GroupStatement} instance.
	 * 
	 * @return a hash code for this {@code GroupStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getGroupNames());
	}
	
	/**
	 * Adds {@code groupName} to this {@code GroupStatement} instance.
	 * <p>
	 * If {@code groupName} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param groupName the group name to add
	 * @throws NullPointerException thrown if, and only if, {@code groupName} is {@code null}
	 */
	public void addGroupName(final String groupName) {
		this.groupNames.add(Objects.requireNonNull(groupName, "groupName == null"));
	}
	
	/**
	 * Removes {@code groupName} from this {@code GroupStatement} instance, if present.
	 * <p>
	 * If {@code groupName} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param groupName the group name to remove
	 * @throws NullPointerException thrown if, and only if, {@code groupName} is {@code null}
	 */
	public void removeGroupName(final String groupName) {
		this.groupNames.remove(Objects.requireNonNull(groupName, "groupName == null"));
	}
}