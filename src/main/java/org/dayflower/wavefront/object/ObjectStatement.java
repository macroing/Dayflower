/**
 * Copyright 2014 - 2025 J&#246;rgen Lundgren
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
 * An {@code ObjectStatement} represents an object statement ({@code "o"}) of a Wavefront Object file.
 * <p>
 * This class is currently thread-safe.
 * 
 * @since 1.0.0
 * @author J&#246;rgen Lundgren
 */
public final class ObjectStatement implements ObjectFileStatement {
	/**
	 * The name of this {@code ObjectStatement} which is {@code "o"}.
	 */
	public static final String NAME = "o";
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	private final String objectName;
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Constructs a new {@code ObjectStatement} instance.
	 * <p>
	 * If {@code objectName} is {@code null}, a {@code NullPointerException} will be thrown.
	 * 
	 * @param objectName the object name to use
	 * @throws NullPointerException thrown if, and only if, {@code objectName} is {@code null}
	 */
	public ObjectStatement(final String objectName) {
		this.objectName = Objects.requireNonNull(objectName, "objectName == null");
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Returns the name of this {@code ObjectStatement} instance.
	 * 
	 * @return the name of this {@code ObjectStatement} instance
	 */
	@Override
	public String getName() {
		return NAME;
	}
	
	/**
	 * Returns the object name.
	 * 
	 * @return the object name
	 */
	public String getObjectName() {
		return this.objectName;
	}
	
	/**
	 * Returns a {@code String} representation of this {@code ObjectStatement} instance.
	 * 
	 * @return a {@code String} representation of this {@code ObjectStatement} instance
	 */
	@Override
	public String toString() {
		return String.format("%s %s", getName(), getObjectName());
	}
	
	/**
	 * Compares {@code object} to this {@code ObjectStatement} instance for equality.
	 * <p>
	 * Returns {@code true} if, and only if, {@code object} is an instance of {@code ObjectStatement}, and their respective values are equal, {@code false} otherwise.
	 * 
	 * @param object the {@code Object} to compare to this {@code ObjectStatement} instance for equality
	 * @return {@code true} if, and only if, {@code object} is an instance of {@code ObjectStatement}, and their respective values are equal, {@code false} otherwise
	 */
	@Override
	public boolean equals(final Object object) {
		if(object == this) {
			return true;
		} else if(!(object instanceof ObjectStatement)) {
			return false;
		} else if(!Objects.equals(getName(), ObjectStatement.class.cast(object).getName())) {
			return false;
		} else if(!Objects.equals(getObjectName(), ObjectStatement.class.cast(object).getObjectName())) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns a hash code for this {@code ObjectStatement} instance.
	 * 
	 * @return a hash code for this {@code ObjectStatement} instance
	 */
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getObjectName());
	}
}